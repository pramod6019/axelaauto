package axela.home;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

public class Enquiry_Add extends Connect {
	public String AppRun = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String enquiry_branch_id = "0";
	public String customer_name = "";
	public String contact_id = "0";
	public String contact_title_id = "0";
	public String contact_name = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String enquiry_customer_id = "0";
	public String enquiry_contact_id = "0";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String contact_city_id = "0";
	public String contact_pin = "";
	public String model_name = "";
	public String enquiry_model_id = "0";
	public String enquiry_item_id = "0";
	public String enquiry_desc = "";
	public String enquiry_date = "";
	public String enquiry_emp_id = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_finance = "0";
	public String enquiry_budget = "0";
	public String crmfollowup_crm_emp_id = "";
	public String title = "";
	public String model = "";
	public String branch = "";
	public String captcha = "";
	public String code = "";
	public String msg = "";
	public String submitb = "", urlpath = "";
	public Enquiry_Quickadd enquiry = new Enquiry_Quickadd();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			if (AppRun().equals("1")) {
				comp_id = "1009";
			} else {
				comp_id = "1000";
			}
			SetSession("comp_id", comp_id, request);
			captcha = (String) GetSession("captcha", request);
			code = PadQuotes(request.getParameter("code"));
			submitb = PadQuotes(request.getParameter("submitb"));
			urlpath = request.getRequestURL().toString();
			int index = urlpath.lastIndexOf("/");
			urlpath = urlpath.substring(0, index);
			urlpath = urlpath.substring(0, urlpath.lastIndexOf("/"));
			// SOP("on load........." + captcha);
			// SOP("on load........." + code);
			enquiry.PopulateConfigDetails();
			GetValues(request);

			if (submitb.equals("yes")) {
				if (!captcha.equalsIgnoreCase(code)) {
					msg = "Verification Code does not match!";
					// SOP(msg);
				} else {
					AddFields(request, response);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request) {
		try {
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_contact_title_id")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
			enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
			enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_branch_id")));
			enquiry_desc = PadQuotes(request.getParameter("txt_enquiry_desc"));

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			// SOP("111111111111");
			if (!enquiry_branch_id.equals("0")) {
				StrSql = "SELECT city_id, branch_pin FROM " + compdb(comp_id) + "axela_city "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON city_id = branch_city_id"
						+ " WHERE branch_id = " + enquiry_branch_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				// SOP("StrSql========="+StrSql);
				while (crs.next()) {
					contact_city_id = crs.getString("city_id");
					contact_pin = crs.getString("branch_pin");
				}
				crs.close();

				enquiry_emp_id = ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_active = 1"
						+ " AND emp_branch_id = " + enquiry_branch_id + " ORDER BY RAND() LIMIT 1");
			}

			if (!enquiry_model_id.equals("0")) {
				enquiry_item_id = ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item "
						+ " WHERE item_model_id = " + enquiry_model_id + ""
						+ " LIMIT 1");
			}
			enquiry.enquiry_branch_id = "1";
			enquiry.emp_id = "1";
			enquiry.enquiry_emp_id = "36";
			enquiry.comp_id = comp_id;
			enquiry.customer_name = customer_name;
			enquiry.contact_title_id = contact_title_id;
			enquiry.contact_fname = contact_fname;
			enquiry.contact_lname = contact_lname;
			enquiry.contact_jobtitle = "";
			enquiry.contact_mobile1 = contact_mobile1;
			enquiry.contact_city_id = contact_city_id;
			enquiry.contact_address = "";
			enquiry.contact_pin = contact_pin;
			enquiry.contact_email1 = contact_email1;
			enquiry.contact_phone1 = contact_phone1;
			enquiry.contact_phone2 = contact_phone2;
			enquiry.enquiry_desc = enquiry_desc;
			enquiry.enquiry_date = strToShortDate(ToShortDate(kknow()));;
			enquiry.enquiry_close_date = strToShortDate(ToLongDate(kknow()));
			// enquiry.crmfollowup_crm_emp_id = enquiry_emp_id;
			enquiry.enquiry_soe_id = "8";
			enquiry.enquiry_sob_id = "0";
			enquiry.enquiry_campaign_id = "2";
			enquiry.enquiry_finance = "0";
			enquiry.enquiry_budget = "0";
			enquiry.enquiry_enquirytype_id = "1";
			enquiry.enquiry_buyertype_id = "1";
			enquiry.enquiry_notes = "";
			enquiry.enquiry_model_id = enquiry_model_id;
			enquiry.enquiry_item_id = enquiry_item_id;
			enquiry.enquiry_dmsno = "";
			enquiry.enquiry_priorityenquiry_id = "1";
			enquiry.branch_brand_id = "2";
			enquiry.AddEnquiryFields();
			msg = enquiry.msg;
			if (msg.equals("")) {
				msg = "Enquiry Added Successfully";
				if (AppRun().equals("1")) {
					response.sendRedirect("http://ddmotocrs.in/home/contactus-thanku.php");
				} else {
					response.sendRedirect("http://nahid:8020/ddmotors/home/contactus-thanku.php");
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
