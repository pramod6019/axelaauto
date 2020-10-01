package axela.service;
//aJIt 11th March, 2013

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Coupon_Issue_Check extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String BranchAccess = "", ExeAccess = "";
	public String addB = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String go = "";
	public String coupon_id = "0";
	public String coupon_issue_time = "";
	public String coupon_issue_emp_id = "0";
	public String coupon_balance_count = "";
	public String emp_id = "0";
	public String emailmsg = "", emailsub = "", smsmsg = "";

	public String coupon_contact_id = "0";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";

	public String brandconfig_coupon_email_enable = "0";
	public String brandconfig_coupon_email_sub = "";
	public String brandconfig_coupon_email_format = "";
	public String brandconfig_coupon_sms_enable = "0";
	public String brandconfig_coupon_sms_format = "";

	public String branch_email1 = "";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";

	public String couponoffer = "";

	public String couponcampaign = "", couponcampaign_brand_id = "", couponcampaign_dept_id = "",
			couponcampaign_type_id = "", couponcampaign_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (ReturnPerm(comp_id, "emp_service_coupon_issue", request).equals("1")) {
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					addB = PadQuotes(request.getParameter("add_button"));
					emp_id = CNumeric(GetSession("emp_id", request));

					GetValues(request, response);

					if (couponcampaign.equals("yes")) {
						StrHTML = new Coupon_Issue().PopulateCouponCampaign(couponcampaign_brand_id, couponcampaign_dept_id, couponcampaign_type_id, comp_id);
					}

					if (couponoffer.equals("yes") && !couponcampaign.equals("yes")) {
						StrHTML = GetCouponDetails(couponcampaign_brand_id, couponcampaign_dept_id, couponcampaign_type_id, couponcampaign_id);
					}

					if (couponoffer.equals("update") && !couponoffer.equals("yes") && !couponcampaign.equals("yes")) {
						coupon_id = CNumeric(PadQuotes(request.getParameter("coupon_id")));
						// coupon_customer_id = CNumeric(PadQuotes(request.getParameter("couponcustomerid")));
						coupon_contact_id = CNumeric(PadQuotes(request.getParameter("coupon_contact_id")));
						coupon_balance_count = CNumeric(PadQuotes(request.getParameter("balance_count")));

						if (branch_id.equals("0")) {
							branch_id = CNumeric(PadQuotes(request.getParameter("coupon_branch_id")));
						}
						if (coupon_balance_count.equals("0")) {
							msg += "<br>Coupon Unavaiable";
						}
						StrHTML = IssueCoupon(coupon_id, coupon_contact_id);
						PopulateConfigDetails();
						GetCustomerContactDetails(coupon_contact_id);

						if (comp_email_enable.equals("1") && config_email_enable.equals("1")
								&& !branch_email1.equals("") && brandconfig_coupon_email_enable.equals("1")) {
							if (!contact_email1.equals("") && !brandconfig_coupon_email_format.equals("")
									&& !brandconfig_coupon_email_sub.equals("")) {
								SendEmail();
							}

						}
						if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
								&& brandconfig_coupon_sms_enable.equals("1")) {
							if (!brandconfig_coupon_email_format.equals("") && !contact_mobile1.equals("")) {
								SendSMS();
							}
						}
					}
				} else {
					StrHTML = "Access Denied!";
				}

			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		coupon_id = CNumeric(PadQuotes(request.getParameter("coupon_id")));
		couponcampaign = PadQuotes(request.getParameter("couponcampaign"));
		couponcampaign_brand_id = PadQuotes(request.getParameter("dr_brand_id"));
		couponcampaign_dept_id = PadQuotes(request.getParameter("dr_dept_id"));
		couponcampaign_type_id = PadQuotes(request.getParameter("dr_type_id"));
		couponcampaign_id = CNumeric(PadQuotes(request.getParameter("dr_campaign_id")));
		couponoffer = PadQuotes(request.getParameter("couponoffer"));

	}

	protected void CheckForm() {
		msg = "";
		if (coupon_id.equals("0")) {
			msg += "<br>Select Coupon Campaign";
		}
	}

	private String GetCouponDetails(String brand_id, String couponcampaign_department_id, String couponcampaign_campaigntype_id, String couponcampaign_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT coupon_id, couponcampaign_couponoffer, couponcampaign_couponvalue, COUNT(coupon_id) AS balancecount"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon_campaign ON coupon_couponcampaign_id = couponcampaign_id"
					+ " WHERE 1 = 1"
					+ " AND couponcampaign_brand_id =" + brand_id
					+ " AND couponcampaign_department_id =" + couponcampaign_department_id
					+ " AND couponcampaign_campaigntype_id =" + couponcampaign_campaigntype_id
					+ " AND couponcampaign_id = " + couponcampaign_id
					+ " AND couponcampaign_active = '1'"
					+ " AND SUBSTR(couponcampaign_startdate, 1, 8) <= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8)"
					+ " AND SUBSTR(couponcampaign_enddate, 1, 8) >= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8)"
					+ " AND coupon_contact_id = 0"
					+ " ORDER BY coupon_id"
					+ " LIMIT 1";

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {

				Str.append("<input type=\"hidden\" name=\"coupon_id\" id=\"coupon_id\" value=\"" + crs.getString("coupon_id") + "\" />");

				Str.append("<div class=\"form-element6\">");
				Str.append("<label>Coupon Offer<font color=red>*</font>:&nbsp;</label>");
				Str.append("<span name=\"coupon_offer\" id=\"coupon_offer\">" + crs.getString("couponcampaign_couponoffer") + "</span>");
				Str.append("</div>");

				Str.append("<div class=\"form-element6\">");
				Str.append("<label>Coupon Value<font color=red>*</font>:&nbsp;</label>");
				Str.append("<span name=\"coupon_value\" id=\"coupon_value\">" + crs.getString("couponcampaign_couponvalue") + "</span>");
				Str.append("</div>");

				Str.append("<div class=\"form-element6\">");
				Str.append("<label>Balance Count<font color=red>*</font>:&nbsp;</label>");
				Str.append("<span name=\"balance_count\" id=\"balance_count\">" + crs.getString("balancecount") + "</span>");
				Str.append("</div>");

			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "<br>No Coupon Available";
		}
	}

	private void GetCustomerContactDetails(String coupon_customer_id2) {

		try {
			StrSql = "SELECT customer_id, customer_name, contact_title_id,"
					+ " CONCAT(title_desc, ' ',	contact_fname, ' ',	contact_lname) AS contact_name,"
					+ " contact_id,"
					+ " COALESCE(IF(contact_email1 != '', contact_email1, contact_email2), '') AS contact_email,"
					+ " COALESCE(IF(contact_mobile1 != '', contact_mobile1, contact_mobile2), '') AS contact_mobile"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " WHERE contact_id = " + coupon_contact_id
					+ " GROUP BY contact_id"
					+ " ORDER BY contact_fname";

			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("Customer Contact------" + StrSql);

			while (crs.next()) {
				contact_mobile1 = crs.getString("contact_mobile");
				contact_email1 = crs.getString("contact_email");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	private String IssueCoupon(String coupon_id, String coupon_contact_id) {

		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_coupon"
						+ " SET coupon_contact_id = " + coupon_contact_id + ","
						+ " coupon_issue_time = '" + ToLongDate(kknow()) + "',"
						+ " coupon_issue_emp_id = " + emp_id + ""
						+ " WHERE coupon_id = " + coupon_id + "";

				int check = updateQuery(StrSql);

				if (check > 0) {
					msg = "Coupon Issued!";
				}
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return msg;
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(brandconfig_coupon_email_enable, '') AS brandconfig_coupon_email_enable,"
					+ " COALESCE(brandconfig_coupon_email_sub, '') AS brandconfig_coupon_email_sub,"
					+ " COALESCE(brandconfig_coupon_email_format, '') AS brandconfig_coupon_email_format,"
					+ " COALESCE(brandconfig_coupon_sms_enable, '') AS brandconfig_coupon_sms_enable,"
					+ " COALESCE(brandconfig_coupon_sms_format, '') AS brandconfig_coupon_sms_format,"
					+ " COALESCE(branch_email1, '') AS branch_email1,"
					+ " COALESCE(config_email_enable, '') AS config_email_enable,"
					+ " COALESCE(config_sms_enable, '') AS config_sms_enable,"
					+ " COALESCE(comp_email_enable, '') AS comp_email_enable,"
					+ " COALESCE(comp_sms_enable, '') AS comp_sms_enable"
					+ " FROM " + compdb(comp_id) + " axela_brand_config"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brandconfig_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + ","
					+ compdb(comp_id) + "axela_comp,"
					+ compdb(comp_id) + "axela_config"
					+ " WHERE branch_id = " + branch_id;
			// SOP("Strsql==PopulateConfigDetails==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				brandconfig_coupon_email_enable = crs.getString("brandconfig_coupon_email_enable");
				brandconfig_coupon_email_sub = crs.getString("brandconfig_coupon_email_sub");
				brandconfig_coupon_email_format = crs.getString("brandconfig_coupon_email_format");
				brandconfig_coupon_sms_enable = crs.getString("brandconfig_coupon_sms_enable");
				brandconfig_coupon_sms_format = crs.getString("brandconfig_coupon_sms_format");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmail() throws SQLException {
		emailsub = brandconfig_coupon_email_sub;
		emailmsg = brandconfig_coupon_email_format;

		emailsub = "REPLACE('" + emailsub + "', '[CUSTOMERID]', customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]', customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTID]', contact_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[COUPONID]', coupon_id)";
		emailsub = "REPLACE(" + emailsub + ", '[COUPONOFFER]', couponcampaign_couponoffer)";
		emailsub = "REPLACE(" + emailsub + ", '[COUPONVALUE]', couponcampaign_couponvalue)";
		emailsub = "REPLACE(" + emailsub + ", '[COUPONVALIDITY]', COALESCE(DATE_FORMAT(couponcampaign_enddate,'%d/%m/%Y'), ''))";

		emailmsg = "REPLACE('" + emailmsg + "', '[CUSTOMERID]', customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]', customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTID]', contact_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[COUPONID]', coupon_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[COUPONOFFER]', couponcampaign_couponoffer)";
		emailmsg = "REPLACE(" + emailmsg + ", '[COUPONVALUE]', couponcampaign_couponvalue)";
		emailmsg = "REPLACE(" + emailmsg + ", '[COUPONVALIDITY]', COALESCE(DATE_FORMAT(couponcampaign_enddate,'%d/%m/%Y'), ''))";

		try {

			StrSql = "SELECT"
					+ " " + branch_id + " ,"
					+ " '" + coupon_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + branch_email1 + "',"
					+ " COALESCE(IF(contact_email1 != '', contact_email1, contact_email2), '') AS contact_email,"
					+ " '',"
					+ " " + unescapehtml(emailsub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon_campaign ON couponcampaign_id = coupon_couponcampaign_id "
					+ " WHERE contact_id = " + coupon_contact_id
					+ " AND coupon_id = " + coupon_id
					+ " LIMIT 1 ";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ( "
					+ " email_branch_id,"
					+ " email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ " ) "
					+ StrSql + "";
			// SOP("Send Email-----" + StrSql);
			updateQuery(StrSql);

		} catch (Exception e) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS() throws SQLException {
		smsmsg = brandconfig_coupon_sms_format;

		smsmsg = "REPLACE('" + smsmsg + "', '[CUSTOMERID]', customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]', customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTID]', contact_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ", '[COUPONID]', coupon_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[COUPONOFFER]', couponcampaign_couponoffer)";
		smsmsg = "REPLACE(" + smsmsg + ", '[COUPONVALUE]', couponcampaign_couponvalue)";
		smsmsg = "REPLACE(" + smsmsg + ", '[COUPONVALIDITY]', COALESCE(DATE_FORMAT(couponcampaign_enddate,'%d/%m/%Y'), ''))";

		try {
			StrSql = "SELECT"
					+ " " + branch_id + " ,"
					+ " contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name,"
					+ " COALESCE(IF(contact_mobile1 != '', contact_mobile1, contact_mobile2), '') AS contact_mobile,"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon_campaign ON couponcampaign_id = coupon_couponcampaign_id "
					+ " WHERE contact_id = " + coupon_contact_id
					+ " AND coupon_id = " + coupon_id
					+ " LIMIT 1 ";
			// SOP("StrSql--------se----" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ " )"
					+ " " + StrSql + "";
			// SOP("SendSMS----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
