////////////Divya 4th april 2013
package axela.insurance;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class New_Vehicle_Dash_Insurancefollowup extends Connect {

	public String StrSql = "";
	public String msg = "", contact_msg = "", list_insurfollowup_msg = "", insur_msg = "";
	// public String BranchAccess = "";
	// public String ExeAccess = "";
	public String branch_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_name = "";
	public String modal = "";
	public String insurenquiry_id = "0";
	public String insurcustomerdetail = "";
	public String followup_info = "";
	public String location_id = "0";
	public String insurfollowup_followuptype_id = "0", insurpolicy_field_emp_id = "0";
	public String insurfollowup_entry_id = "0";
	public String insurfollowup_entry_time = "0";
	public String currenttimevalidate = "";
	public String insurenquiryfollowup_id = "0";
	public String vehfollowup_id = "0";

	public String submitB = "";
	public String addContactB = "";
	public String delete = "";
	public String loaddata = "yes";
	public String StrSearch = "", service = "", insurance = "";
	// Insurance Follow-Up
	public String insurfollowup_contactable_id = "0";
	public String disposition_id = "0", dr_inspection_id = "0", dr_inspection_result_id = "0", dr_appoint_verification_id = "0";
	public String dr_field_exe_id = "0", txt_remarks = "";
	public String addInsurFollowup = "", listfollowup_info = "", veh_insurtype_id = "", veh_insur_notinterested_id = "";
	public String[] dr_offer_id;
	String insur_offers = "";
	public String veh_insur_previouscompname = "";
	public String veh_insur_previousyearidv = "";
	public String veh_insur_previousgrosspremium = "";
	public String veh_insur_previousplanname = "";
	public String veh_insur_policyexpirydate = "";
	public String veh_insur_currentidv = "";
	public String veh_insur_premium = "";
	public String veh_insur_premiumwithzerodept = "";
	public String veh_insur_compoffered = "";
	public String veh_insur_plansuggested = "", veh_insur_name = "", veh_insur_variant = "", veh_insur_reg_no = "", veh_insur_chassis_no = "";
	public String veh_insur_ncb = "", veh_insur_address = "";

	public String feedback_desc = "";
	public String txt_nextfollowup_time = "";
	public String dr_feedbacktype_id = "0", dr_nextfollowup_type = "0";

	DecimalFormat df = new DecimalFormat("0.00");

	public Vehicle_Check vehicle = new Vehicle_Check();

	public MIS_Check mischeck = new MIS_Check();
	// public Insurance_Enquiry_Dash insenqdash = new Insurance_Enquiry_Dash();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_insurance_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				insurance = PadQuotes(request.getParameter("insurance"));
				list_insurfollowup_msg = PadQuotes(request.getParameter("insurfollowup_msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				modal = PadQuotes(request.getParameter("modal"));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				insurenquiryfollowup_id = PadQuotes(request.getParameter("insurenquiryfollowup_id"));
				vehfollowup_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				addContactB = PadQuotes(request.getParameter("add_contact_button"));
				currenttimevalidate = ToLongDate(kknow());
				addInsurFollowup = PadQuotes(request.getParameter("add_insurfollowup_button"));
				// SOP("addInsurFollowup=====" + addInsurFollowup);
				delete = PadQuotes(request.getParameter("Delete"));
				insurcustomerdetail = ListCustomerDetails(comp_id, insurenquiry_id, "");

				GetInsurDetails(insurenquiry_id, comp_id);
				followup_info = ListInsuranceFollowup(comp_id, insurenquiry_id);
				if (delete.equals("yes") && !insurenquiryfollowup_id.equals("0") && emp_id.equals("1")) {
					DeleteInsurFollowupFields();
					loaddata = "";
					if (service.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
					}
				} else if (addInsurFollowup.equals("Add")) {

					dr_feedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_feedbacktype_id")));
					dr_nextfollowup_type = CNumeric(PadQuotes(request.getParameter("dr_nextfollowup_type_id")));
					txt_nextfollowup_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));
					feedback_desc = PadQuotes(request.getParameter("txt_feedback_desc"));
					feedback_desc = feedback_desc.trim();
					SOP("insur_msg==11=" + insur_msg);
					if (insur_msg.equals("")) {
						insurfollowup_entry_id = emp_id;
						insurfollowup_entry_time = ToLongDate(kknow());
						AddInsurFollowup();
						if (!insur_msg.equals("")) {
							insur_msg = "Error!" + insur_msg;
						}
						// insenqdash.msg = insur_msg;

						SOP("insur_msg==last==" + insur_msg);
						loaddata = "";

						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?msg=" + insur_msg + "&insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
					} else {
						insur_msg = "Error!" + insur_msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String GetInsurDetails(String insurenquiry_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT  COALESCE(insurenquiry_previouscompname,'') AS insurenquiry_previouscompname,"
				+ " COALESCE(insurenquiry_previousyearidv,'') AS insurenquiry_previousyearidv,"
				+ " COALESCE(insurenquiry_previousgrosspremium,'') AS insurenquiry_previousgrosspremium,"
				+ " COALESCE(insurenquiry_previousplanname,'') AS insurenquiry_previousplanname,"
				+ " COALESCE(insurenquiry_policyexpirydate,'') AS insurenquiry_policyexpirydate,"
				+ " COALESCE(insurenquiry_currentidv,'') AS insurenquiry_currentidv,"
				+ " COALESCE(insurenquiry_premium,'') AS insurenquiry_premium,"
				+ " COALESCE(insurenquiry_premiumwithzerodept,'') AS insurenquiry_premiumwithzerodept,"
				+ " COALESCE(insurenquiry_compoffered,'') AS insurenquiry_compoffered,"
				+ " COALESCE(insurenquiry_plansuggested,'') AS insurenquiry_plansuggested,"
				+ " CONCAT(	title_desc,' ', contact_fname,' ',contact_lname	) AS veh_contactname,"
				+ " COALESCE(insurenquiry_variant,'') AS insurenquiry_variant,"
				+ " COALESCE(insurenquiry_ncb,'') AS insurenquiry_ncb,"
				+ " COALESCE(insurenquiry_reg_no,'') AS insurenquiry_reg_no,"
				+ " COALESCE(insurenquiry_chassis_no,'') AS insurenquiry_chassis_no,"
				+ " IF(insurenquiry_address='',CONCAT(contact_address,', ',city_name,'-',contact_pin),insurenquiry_address) AS insurenquiry_address"
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id "
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
				+ " WHERE 1 = 1"
				+ " AND insurenquiry_id = " + insurenquiry_id;
		// SOP("StrSql==GetInsurDetails==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				veh_insur_previouscompname = crs.getString("insurenquiry_previouscompname");
				veh_insur_previousyearidv = crs.getString("insurenquiry_previousyearidv");
				veh_insur_previousgrosspremium = crs.getString("insurenquiry_previousgrosspremium");
				veh_insur_previousplanname = crs.getString("insurenquiry_previousplanname");
				veh_insur_policyexpirydate = strToShortDate(crs.getString("insurenquiry_policyexpirydate"));
				veh_insur_currentidv = crs.getString("insurenquiry_currentidv");
				veh_insur_premium = crs.getString("insurenquiry_premium");
				veh_insur_premiumwithzerodept = crs.getString("insurenquiry_premiumwithzerodept");
				veh_insur_compoffered = crs.getString("insurenquiry_compoffered");
				veh_insur_plansuggested = crs.getString("insurenquiry_plansuggested");
				veh_insur_name = crs.getString("veh_contactname");
				veh_insur_variant = crs.getString("insurenquiry_variant");
				veh_insur_ncb = crs.getString("insurenquiry_ncb");
				veh_insur_reg_no = crs.getString("insurenquiry_reg_no");
				veh_insur_chassis_no = crs.getString("insurenquiry_chassis_no");
				veh_insur_address = crs.getString("insurenquiry_address");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// Insurance

	public String ListInsuranceFollowup(String comp_id, String insurenquiry_id) {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT"
				+ " insurenquiryfollowup_id,"
				+ " insurenquiryfollowup_insurenquiry_id,"
				+ " insurenquiryfollowup_followup_time,"
				+ " insurenquiryfollowup_followuptype_id,"
				+ " insurfollowuptype_name,"
				+ " COALESCE (insurenquiryfollowup_feedbacktype_id,0) AS insurenquiryfollowup_feedbacktype_id,"
				+ " COALESCE (insurfeedbacktype_name,'') AS insurfeedbacktype_name,"
				+ " insurenquiryfollowup_desc,"
				+ " insurenquiryfollowup_entry_id,"
				+ " insurenquiryfollowup_entry_time,"
				+ " COALESCE (insurenquiryfollowup_modified_id, 0) AS insurenquiryfollowup_modified_id,"
				+ " COALESCE (insurenquiryfollowup_modified_time, 0) AS insurenquiryfollowup_modified_time"
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_followup_type ON insurfollowuptype_id = insurenquiryfollowup_followuptype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_enquiry_feedback_type ON insurfeedbacktype_id = insurenquiryfollowup_feedbacktype_id"
				+ " WHERE insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id
				+ " ORDER BY insurenquiryfollowup_id DESC";

		// SOP("Str====ListInsuranceFollowup==== " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("<th data-hide=\"phone\">Follow-up Type</th>\n");
				Str.append("<th data-hide=\"phone\">Follow-up Description</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Feedback By</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Entry By</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("insurenquiryfollowup_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br/><a href=\"../insurance/new-vehicle-dash-insurancefollowup.jsp?");
						if (service.equals("yes")) {
							Str.append("service=yes&");
						}
						Str.append("Delete=yes&insurenquiry_id=").append(crs.getString("insurenquiryfollowup_insurenquiry_id")).append("&insurenquiryfollowup_id=");
						Str.append(crs.getString("insurenquiryfollowup_id")).append("\">Delete Follow-up</a>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurfollowuptype_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_desc")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurfeedbacktype_name")).append("</td>\n");

					Str.append("<td valign=\"top\" align=\"center\">");
					if (!crs.getString("insurenquiryfollowup_modified_id").equals("0")) {
						Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurenquiryfollowup_modified_id"))));
						Str.append("<br>").append(strToLongDate(crs.getString("insurenquiryfollowup_modified_time")));
					}
					Str.append("&nbsp;</td>\n");

					Str.append("<td valign=\"top\" align=\"center\">");
					Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurenquiryfollowup_entry_id"))));
					Str.append("<br>").append(strToLongDate(crs.getString("insurenquiryfollowup_entry_time")));
					Str.append("&nbsp;</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				// Str.append("<br><br><br><br><center><font color=\"red\"><b>No Follow-up found!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListCustomerDetails(String comp_id, String insurenquiry_id, String type)
	{
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT customer_id, customer_name, contact_id, "
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, "
					+ " contact_mobile1, contact_mobile2, "
					+ " variant_name,"
					+ " insurenquiry_sale_date, insurenquiry_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE insurenquiry_id = " + insurenquiry_id
					+ " GROUP BY insurenquiry_id";
			// SOP("StrSql-----3111-- " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<center>");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");

				// Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					Str.append("<tr align=center>\n");
					Str.append("<td >Customer: <a href=\"../customer/customer-list.jsp?customer_id=")
							.append(crs.getString("customer_id")).append(" \">")
							.append(crs.getString("customer_name")).append(" (")
							.append(crs.getString("customer_id")).append(")</td>\n");
					Str.append("<td>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=")
							.append(crs.getString("contact_id")).append(" \">")
							.append(crs.getString("contacts")).append("</a></td>\n");
					// Str.append("<td>Mobile: ").append(crs.getString("contact_mobile1"))
					// .append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("</td>\n");
					Str.append("<td>Variant: ").append(crs.getString("variant_name")).append("</td>\n");

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<tr align=center><td>Mobile 1: ").append(crs.getString("contact_mobile1"));
						Str.append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<td>Mobile 2: ").append(crs.getString("contact_mobile2"));
						Str.append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}

					// Str.append("</td>");
					Str.append("</tr>\n");

				}
				Str.append("</table></div></center>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void DeleteInsurFollowupFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " WHERE insurenquiryfollowup_id = " + insurenquiryfollowup_id + "";
			updateQuery(StrSql);
		}
	}

	public void InsurCheckForm() {
		// SOP("txt_nextfollowup_time===" + txt_nextfollowup_time);
		// SOP("feedback_desc===" + feedback_desc);
		// SOP("dr_feedbacktype_id===" + dr_feedbacktype_id);
		// SOP("dr_nextfollowup_type===" + dr_nextfollowup_type);
		insur_msg = "";

		if (txt_nextfollowup_time.equals("")) {
			insur_msg = insur_msg + "<br>Enter Next-Follow-up time!";
		}

		if (!txt_nextfollowup_time.equals("") && !isValidDateFormatLong(txt_nextfollowup_time)) {
			insur_msg = insur_msg + "<br>Enter Valid Next-Follow-up time!";
		}

		if (!txt_nextfollowup_time.equals("") && isValidDateFormatLong(txt_nextfollowup_time)) {
			SOP("1");
			if (Long.parseLong(ConvertLongDateToStr(txt_nextfollowup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
				SOP("2");
				insur_msg = insur_msg + "<br>Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
			} else {
				String followuptime = ExecuteQuery("SELECT insurenquiryfollowup_followup_time "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup "
						+ " WHERE insurenquiryfollowup_desc =''"
						+ " AND insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id);

				if (Long.parseLong(ConvertLongDateToStr(txt_nextfollowup_time)) < Long.parseLong(followuptime)) {
					SOP("3");
					insur_msg = insur_msg + "<br>Follow-up time should be greater than previous follow-up time!";
				}
			}

		}

		if (feedback_desc.equals("")) {
			insur_msg += "<br>Feedback cannot be empty!";
		}

		if (dr_feedbacktype_id.equals("0")) {
			insur_msg += "<br>Select Feedback Type!";
		}

		if (dr_nextfollowup_type.equals("0")) {
			insur_msg += "<br>Select Next-Followup Type!";
		}
		SOP("insur_msg=22==" + insur_msg);
	}

	public void AddInsurFollowup() throws Exception {
		InsurCheckForm();
		SOP("insur_msg==3333=" + insur_msg);
		if (insur_msg.equals("")) {
			StrSql = "SELECT insurenquiryfollowup_id FROM " + compdb(comp_id) + " axela_insurance_enquiry_followup"
					+ " WHERE insurenquiryfollowup_desc = '' "
					+ " AND insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id;

			if (!ExecuteQuery(StrSql).equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry_followup"
						+ " SET"
						+ " insurenquiryfollowup_feedbacktype_id = " + dr_feedbacktype_id + ","
						+ " insurenquiryfollowup_desc = '" + feedback_desc + "',"
						+ " insurenquiryfollowup_modified_id = " + emp_id + ","
						+ " insurenquiryfollowup_modified_time = '" + ToLongDate(kknow()) + "'"
						+ " WHERE 1=1 "
						+ " AND insurenquiryfollowup_desc = ''"
						+ " AND insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id + "";
				SOP("StrSql==update followup==" + StrSql);
				updateQuery(StrSql);
			}

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " (insurenquiryfollowup_insurenquiry_id,"
					+ " insurenquiryfollowup_followup_time,"
					+ " insurenquiryfollowup_followuptype_id,"
					+ " insurenquiryfollowup_entry_id,"
					+ " insurenquiryfollowup_entry_time)"
					+ " VALUES"
					+ " (" + insurenquiry_id + ","
					+ " '" + ConvertLongDateToStr(txt_nextfollowup_time) + "',"
					+ " '" + dr_nextfollowup_type + "',"
					+ " '" + emp_id + "',"
					+ " '" + insurfollowup_entry_time + "')";
			SOP("Insert insur==222==" + StrSql);
			updateQuery(StrSql);

		}
	}

	public String PopulateFeedbackType(String comp_id, String dr_feedbacktype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT feedbacktype_id, feedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurnace_enquiry_followup_feedback_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY feedbacktype_id"
					+ " ORDER BY feedbacktype_name";
			// SOP("Feed===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("feedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("feedbacktype_id"), dr_feedbacktype_id));
				Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateNextFollowupType(String comp_id, String dr_followuptype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurfollowuptype_id, insurfollowuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurfollowuptype_id"
					+ " ORDER BY insurfollowuptype_name";
			// SOP("Next===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfollowuptype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfollowuptype_id"), dr_followuptype_id));
				Str.append(">").append(crs.getString("insurfollowuptype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
