package axela.axelaauto_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.insurance.Insurance_Enquiry;
import cloudify.connect.Connect;

public class App_Insurance_Enquiry_Add extends Connect {

	public String addB = "";
	public String submitB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_enquiry_edit = "";
	public String insurance_customer_id = "0";
	public String insurance_veh_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String branch_city_id = "0";
	public String insurance_veh_customer_id = "0";
	public String insurance_veh_item_id = "0";
	public String insurance_veh_chassis_no = "";
	public String insurance_veh_model_id = "0";
	public String insurance_veh_model_name = "";
	public String insurance_veh_engine_no = "";
	public String insurance_veh_reg_no = "";
	public String insurance_veh_id = "0";
	public String insurfollowup_date = "";
	public String insurfollowup_time = "";
	public String insurfollowup_followuptype_id = "0";
	public String insurfollowup_priorityinsurfollowup_id = "0";
	public String insurance_soe_id = "0";
	public String insurance_sob_id = "0";
	public String insurance_campaign_id = "0";
	public String insurance_refemp_id = "0";
	public String insurance_veh_branch_id = "0";
	public String insurance_contact_id = "0";
	public String insurfollowup_emp_id = "0";
	public String insurfollowup_id = "0";
	public String insurance_entry_id = "0";
	public String insurance_entry_date = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String contact_name = "";
	public String contact_info = "";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";
	public String contact_pin = "", branch_pin = "";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String branch_email1 = "";
	public String brandconfig_insur_enquiry_email_enable = "";
	public String brandconfig_insur_enquiry_email_sub = "";
	public String brandconfig_insur_enquiry_email_format = "";
	public String brandconfig_insur_enquiry_sms_enable = "";
	public String brandconfig_insur_enquiry_sms_format = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_role_id = CNumeric(GetSession("emp_role_id", request));
			insurfollowup_date = strToLongDate(ToShortDate(kknow()));
			insurfollowup_time = insurfollowup_date.substring(11, 16);
			insurfollowup_date = insurfollowup_date.substring(0, 11);
			submitB = request.getParameter("add_button");
			if (!emp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				insurance_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehbranch_id")));
				if (insurance_veh_branch_id.equals("0")) {
					insurance_veh_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (insurance_veh_branch_id.equals("0")) {
						insurance_veh_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 "
								+ " AND branch_branchtype_id IN (3) LIMIT 1");

					}
				}
				if (!insurance_veh_branch_id.equals("0")) {
					branch_city_id = ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch where branch_id =" + insurance_veh_branch_id);
				}
				if ("Add Enquiry".equals(submitB)) {
					// SOP("comi11");
					GetValues(request, response);
					insurance_entry_id = emp_id;
					insurance_entry_date = ToLongDate(kknow());
					AddVehFields(response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("") && !insurance_veh_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-insurance-enquiry-list.jsp?msg=Insurance Enquiry added successfully!"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Branch Details
		insurance_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehbranch_id")));
		insurance_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		// Customer Details
		if (insurance_contact_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_insurance_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		}
		// Vechile Details
		insurance_veh_model_id = CNumeric(PadQuotes(request.getParameter("dr_item_model_id")));
		insurance_veh_item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
		insurance_veh_reg_no = PadQuotes(request.getParameter("txt_insurance_veh_reg_no"));
		insurance_veh_chassis_no = PadQuotes(request.getParameter("txt_insurance_veh_chassis_no"));
		insurance_veh_engine_no = PadQuotes(request.getParameter("txt_insurance_veh_engine_no"));
		insurfollowup_emp_id = CNumeric(PadQuotes(request.getParameter("dr_insurance_veh_executive")));
		insurfollowup_date = PadQuotes(request.getParameter("txt_insurfollowup_date"));
		insurfollowup_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));
		insurfollowup_time = insurfollowup_date + " " + insurfollowup_time;
		insurfollowup_followuptype_id = PadQuotes(request.getParameter("dr_insurfollowup_followuptype_id"));
		insurfollowup_priorityinsurfollowup_id = PadQuotes(request.getParameter("dr_insurfollowup_priorityinsurfollowup_id"));
		// insurance_soe_id = CNumeric(PadQuotes(request.getParameter("dr_insur_soe_id")));
		// SOP("insurance_soe_id fjeifo===" + insurance_soe_id);
		// insurance_sob_id = CNumeric(PadQuotes(request.getParameter("dr_insur_sob_id")));
		// / insurance_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_insur_campaign_id")));
		insurance_campaign_id = CNumeric(ExecuteQuery("SELECT campaign_id FROM " + compdb(comp_id) + "axela_sales_campaign"
				+ " WHERE campaign_name = 'others'"));

	}

	protected void AddVehFields(HttpServletResponse response) throws SQLException {
		try {

			Insurance_Enquiry insuranceenq = new Insurance_Enquiry();
			insuranceenq.comp_id = comp_id;
			insuranceenq.emp_id = emp_id;
			// insuranceenq.insurance_veh_branch_id = insurance_veh_branch_id;
			// insuranceenq.branch_city_id = branch_city_id;
			insuranceenq.insurfollowup_id = insurfollowup_id;
			// insuranceenq.insurance_contact_id = insurance_contact_id;
			// Start Customer and Contact
			// insuranceenq.customer_name = customer_name;
			// insuranceenq.contact_title_id = contact_title_id;
			// insuranceenq.contact_fname = contact_fname;
			// insuranceenq.contact_lname = contact_lname;
			// insuranceenq.contact_mobile1 = contact_mobile1;
			// insuranceenq.contact_email1 = contact_email1;
			// End Customer and Contact
			// Start Insurance Details
			// insuranceenq.insurance_veh_model_id = insurance_veh_model_id;
			// insuranceenq.insurance_veh_item_id = insurance_veh_item_id;
			// insuranceenq.insurance_veh_reg_no = insurance_veh_reg_no;
			// insuranceenq.insurance_veh_chassis_no = insurance_veh_chassis_no;
			// insuranceenq.insurance_veh_engine_no = insurance_veh_engine_no;
			// insuranceenq.insurfollowup_emp_id = insurfollowup_emp_id;
			// insuranceenq.insurfollowup_time = insurfollowup_time;
			// insuranceenq.insurfollowup_followuptype_id = insurfollowup_followuptype_id;
			// insuranceenq.insurfollowup_priorityinsurfollowup_id = insurfollowup_priorityinsurfollowup_id;
			// insuranceenq.insurance_soe_id = insurance_soe_id;
			// insuranceenq.insurance_sob_id = insurance_sob_id;
			// insuranceenq.insurance_campaign_id = insurance_campaign_id;
			// insuranceenq.insurance_entry_id = insurance_entry_id;
			// insuranceenq.insurance_entry_date = insurance_entry_date;
			// insuranceenq.AddVehFields(response);
			msg = insuranceenq.msg;
			// insurance_veh_id = insuranceenq.insurance_veh_id;
			// End Insurance Details

		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranches(String comp_id, String insurance_veh_branch_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_branchtype_id IN (3)"
					+ " AND branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// SOP("StrSql == " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), insurance_veh_branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append("</option>\n");
			}
			crs.close();
			// SOP("StrSql == " + insurance_veh_branch_id);
			return stringval.toString();

		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id) + "axela_title" + " WHERE 1 =  1";
			if (!CNumeric(contact_title_id).equals("0")) {
				StrSql += " AND title_id = " + contact_title_id + "";
			}
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel(String insurance_veh_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE branch_id =" + insurance_veh_branch_id
					+ " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql----PopulateModel----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_model_id\" id=\"dr_item_model_id\" class=\"form-control\" onchange=\"PopulateItem(this.value);\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), insurance_veh_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String veh_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE item_model_id = " + veh_model_id + ""
					+ " ORDER BY model_name, item_name";
			// SOP("StrSql----PopulateItem----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), insurance_veh_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInsurExecutive(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_insur = 1"
					+ " AND emp_active = 1";
			// + " AND emp_branch_id =" + insurance_veh_branch_id;
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + ""
					+ " AND leave_active = 1)";

			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("PopulateInsurExecutive-==" + StrSql);
			// SOP("insurance_veh_branch_id" + insurance_veh_branch_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insurance_veh_executive\" id=\"dr_insurance_veh_executive\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), insurfollowup_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_type"
					+ " ORDER BY followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("followuptype_id"));
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowupPriority(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT priorityinsurfollowup_id, priorityinsurfollowup_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " ORDER BY priorityinsurfollowup_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityinsurfollowup_id"));
				Str.append(StrSelectdrop(crs.getString("priorityinsurfollowup_id"), insurfollowup_priorityinsurfollowup_id));
				Str.append(">").append(crs.getString("priorityinsurfollowup_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
