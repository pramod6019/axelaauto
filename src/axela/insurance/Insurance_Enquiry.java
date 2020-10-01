package axela.insurance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Insurance_Enquiry extends Connect {

	// insurance enq variables
	public String insurenquiry_branch_id = "0";
	public String insurenquiry_date = "";
	public String insurenquiry_customer_id = "0";
	public String insurenquiry_contact_id = "0";
	public String insurenquiry_contact_title_id = "0";
	public String insurenquiry_contact_fname = "";
	public String insurenquiry_contact_lname = "", insurenquiry_contact_jobtitle = "";
	public String insurenquiry_contact_mobile1 = "91-";
	public String insurenquiry_contact_email1 = "";
	public String insurenquiry_branch_city_id = "0";
	public String insurenquiry_variant_id = "0";
	public String emp_name = "";
	// public String insurenquiry_engine_no = "";
	public String insurenquiry_reg_no = "";
	public String insurenquiry_id = "0";
	public String insurenquiry_insurtype_id = "";
	public String insurenquiry_emp_id = "0";
	public String insurenquiry_refemp_id = "0";
	public String insurenquiry_customer_name = "";
	public String insurenquiry_phonetype_id = "0";
	public String insurenquiry_soe_id = "0";
	public String insurenquiry_sob_id = "0";
	public String insurenquiry_campaign_id = "0";

	// followup fields
	public String insurfollowup_id = "0";
	// public String insurfollowup_time = "";
	// public String insurenquiryfollowup_followuptype_id = "0";
	public String insurfollowup_refemp_id = "0";
	public String insurfollowup_entry_id = "0";
	public String insurfollowup_entry_date = "";

	// calss variables
	public String submitB = "";
	public String StrSql = "";
	public String msg = "", msg1 = "";
	public String emp_id = "0";
	public String emp_insurenquiry_edit = "0";
	public String emp_role_id = "0";
	public String comp_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	// config variables
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String branch_enquiry_email_enable = "";

	public String branch_email1 = "";
	public String crm_emp_email1 = "", crm_emp_email2 = "";
	public String crm_emp_mobile1 = "", crm_emp_mobile2 = "";
	public String brandconfig_insur_enquiry_email_enable = "";
	public String brandconfig_insur_enquiry_email_sub = "";
	public String brandconfig_insur_enquiry_email_format = "";
	public String brandconfig_insur_enquiry_exe_email_sub = "";
	public String brandconfig_insur_enquiry_exe_email_format = "";
	public String brandconfig_insur_enquiry_sms_enable = "";
	public String brandconfig_insur_enquiry_sms_format = "";
	public String brandconfig_insur_enquiry_exe_sms_format = "";

	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_role_id = CNumeric(GetSession("emp_role_id", request));
			// insurfollowup_time = strToLongDate(ToShortDate(kknow()));
			insurenquiry_date = strToShortDate(ToShortDate(kknow()));
			submitB = request.getParameter("addbutton");
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_insurenquiry_edit = ReturnPerm(comp_id, "emp_insurance_enquiry_edit", request);
				insurenquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehbranch_id")));
				if (insurenquiry_branch_id.equals("0")) {
					SOP("insurenquiry_branch_id==" + insurenquiry_branch_id);
					insurenquiry_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (insurenquiry_branch_id.equals("0")) {
						StrSql = "SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 "
								+ " AND branch_branchtype_id IN (6) LIMIT 1";
						insurenquiry_branch_id = ExecuteQuery(StrSql);
					}
				}
				// SOP("insurenquiry_branch_id----" + insurenquiry_branch_id);
				if (!insurenquiry_branch_id.equals("0")) {
					insurenquiry_branch_city_id = ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch "
							+ " WHERE branch_id =" + insurenquiry_branch_id);
				}
				// SOP("insurenquiry_branch_id===" + insurenquiry_branch_id);
				if ("Add Enquiry".equals(submitB)) {
					GetValues(request, response);
					populateConfigDetails(comp_id);
					insurfollowup_entry_id = emp_id;
					insurfollowup_entry_date = ToLongDate(kknow());
					AddInsurEnquiryFields(response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						if (ReturnPerm(comp_id, "emp_insurance_enquiry_access", request).equals("0")) {
							response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg1));
						}
						else {
							// SOP("insurance_enquiry_id====" + insurenquiry_id);
							response.sendRedirect(response.encodeRedirectURL("insurance-enquiry-list.jsp?insurenquiry_id=" + insurenquiry_id + "&msg= " + msg1));
						}
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		insurenquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehbranch_id")));
		insurenquiry_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		insurenquiry_customer_name = PadQuotes(request.getParameter("txt_insurance_customer_name"));
		insurenquiry_contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		insurenquiry_contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		insurenquiry_contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		insurenquiry_contact_jobtitle = PadQuotes(request.getParameter("txt_contact_jobtitle"));
		insurenquiry_contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		insurenquiry_contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		insurenquiry_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		insurenquiry_reg_no = PadQuotes(request.getParameter("txt_insurance_veh_reg_no"));
		insurenquiry_insurtype_id = CNumeric(PadQuotes(request.getParameter("dr_insurenquiry_insurtype_id")));
		// insurenquiry_chassis_no = PadQuotes(request.getParameter("txt_insurance_veh_chassis_no"));
		// insurenquiry_engine_no = PadQuotes(request.getParameter("txt_insurance_veh_engine_no"));
		insurenquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_insurance_veh_executive")));
		insurenquiry_soe_id = CNumeric(PadQuotes(request.getParameter("dr_insur_soe_id")));
		insurenquiry_sob_id = CNumeric(PadQuotes(request.getParameter("dr_insur_sob_id")));
		insurenquiry_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_insur_campaign_id")));
		// followup Details
		// insurfollowup_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));
		if (emp_insurenquiry_edit.equals("1")) {
			insurenquiry_date = PadQuotes(request.getParameter("txt_insurenquiry_date"));
		} else {
			insurenquiry_date = strToShortDate(ToShortDate(kknow()));
		}
		// SOP("insurenquiry_date=11==" + insurenquiry_date);
		// insurenquiryfollowup_followuptype_id = PadQuotes(request.getParameter("dr_insurfollowup_followuptype_id"));

	}
	protected void CheckForm() throws SQLException {
		msg = "";
		String customername = "";
		if (insurenquiry_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		// SOP("insurenquiry_date==" + insurenquiry_date);

		if (insurenquiry_date.equals("")) {
			msg = msg + "<br>Enter Insurance Enquiry Date!";
		} else {
			if (!isValidDateFormatShort(insurenquiry_date)) {
				msg = msg + "<br>Enter Valid Insurance Enquiry Date!";
			}
			if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(insurenquiry_date))) {
				msg = msg + " <br>Insurance Enquiry Date can't be greater than Current Date!";
			}
		}

		if (insurenquiry_contact_title_id.equals("0")) {
			msg = msg + "<br>Select Title!";
		}
		if (insurenquiry_contact_fname.equals("")) {
			msg = msg + "<br>Enter the Contact Person First Name!";
		} else {
			insurenquiry_contact_fname = toTitleCase(insurenquiry_contact_fname);
		}
		if (!insurenquiry_contact_lname.equals("")) {
			insurenquiry_contact_lname = toTitleCase(insurenquiry_contact_lname);
		}
		if (insurenquiry_customer_name.equals("")) {
			customername = (insurenquiry_contact_fname + " " + insurenquiry_contact_lname).trim();
			insurenquiry_customer_name = toTitleCase(customername);
		} else {
			customername = toTitleCase(insurenquiry_customer_name);
			insurenquiry_customer_name = customername;
		}

		if (insurenquiry_contact_mobile1.equals("91-")) {
			insurenquiry_contact_mobile1 = "";
		}

		if (insurenquiry_contact_mobile1.equals("")) {
			msg = msg + "<br>Enter Contact Mobile 1!";
		} else if (!insurenquiry_contact_mobile1.equals("")) {
			if (!IsValidMobileNo11(insurenquiry_contact_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			} else {
				StrSql = "SELECT emp_mobile1, emp_mobile2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (insurenquiry_contact_mobile1.equals(crs.getString("emp_mobile1"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
					if (insurenquiry_contact_mobile1.equals(crs.getString("emp_mobile2"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
				}
				crs.close();
				StrSql = "SELECT insurenquiry_id "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = insurenquiry_contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_mobile1 = '" + insurenquiry_contact_mobile1 + "'"
						+ " OR contact_mobile2 = '" + insurenquiry_contact_mobile1 + "')"
						+ " AND contact_contacttype_id = 10"
						+ " AND insurenquiry_insurstatus_id = 1"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
				// SOP("StrSql====" + StrSql);
				// SOP("insurenquiry_id====" + insurenquiry_id);
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					msg += "<br>Simillar Mobile1 Found!";
				}
			}
		}

		if (!insurenquiry_contact_email1.equals("")) {
			if (!IsValidEmail(insurenquiry_contact_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1!";
			} else {
				StrSql = "SELECT emp_email1, emp_email2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (insurenquiry_contact_email1.equals(crs.getString("emp_email1"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
					if (insurenquiry_contact_email1.equals(crs.getString("emp_email2"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
				}
				crs.close();
				StrSql = "SELECT insurenquiry_id "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = insurenquiry_contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_email1 = '" + insurenquiry_contact_email1 + "'"
						+ " OR contact_email2 = '" + insurenquiry_contact_email1 + "')"
						+ " AND contact_contacttype_id = 10"
						+ " AND insurenquiry_insurstatus_id = 1"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
				// SOP("StrSql====" + StrSql);
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					msg += "<br>Simillar Email1 Found!";
				}
			}
		}

		if (insurenquiry_insurtype_id.equals("0")) {
			msg += "<br>Select Insurance Type!";
		}

		if (insurenquiry_emp_id.equals("0")) {
			msg += "<br>Select Insurance Executive!";
		}

		// if (insurfollowup_time.equals("")) {
		// msg += "<br>Enter Insurance Follow-up Time!";
		// }
		// else {
		// if (!isValidDateFormatLong(insurfollowup_time)) {
		// msg = msg + "<br>Enter Valid Insurance Follow-up Time!";
		// }
		// else if (Long.parseLong(ConvertLongDateToStr(insurfollowup_time)) < (Long.parseLong((ToLongDate(kknow()))))) {
		// msg += "<br>Insurance Follow-up Time must be greater than Current Time!";
		// }
		// }
		//
		// if (insurenquiryfollowup_followuptype_id.equals("0")) {
		// msg += "<br>Select Next Follow-up Type!";
		// }

		if (insurenquiry_soe_id.equals("0")) {
			msg = msg + "<br>Select Source of Enquiry!";
		}

		if (insurenquiry_sob_id.equals("0")) {
			msg = msg + "<br>Select Source of Business!";
		}

		if (insurenquiry_campaign_id.equals("0")) {
			msg = msg + "<br>Select Campaign!";
		}
		if (!insurenquiry_emp_id.equals(emp_id)) {
			insurfollowup_refemp_id = emp_id;
		} else {
			insurfollowup_refemp_id = "0";
		}

		if (!insurenquiry_emp_id.equals(emp_id)) {
			insurenquiry_refemp_id = emp_id;
		} else {
			insurenquiry_refemp_id = "0";
		}

	}
	// Adding customer details
	public void AddCustomerFields() throws SQLException {
		try {
			// ////SOP("ADD CUSTOMER");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_mobile1_phonetype_id,"
					+ " customer_email1,"
					+ " customer_notes,"
					+ " customer_city_id,"
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_entry_id,"
					+ " customer_entry_date,"
					+ " customer_modified_id,"
					+ " customer_modified_date) "
					+ " VALUES "
					+ " ('" + insurenquiry_branch_id + " ',"// customer_name
					+ " '" + insurenquiry_customer_name + " ',"// customer_name
					+ " '" + insurenquiry_contact_mobile1 + "'," // contact_mobile1
					+ " 4 ,"// customer_mobile1_phonetype_id
					+ " '" + insurenquiry_contact_email1 + "'," // contact_email1
					+ "'',"// customer notes
					+ " '" + insurenquiry_branch_city_id + "',"// cust_city_id
					+ " '" + insurenquiry_soe_id + "'," // enquiry_soe_id
					+ " '" + insurenquiry_sob_id + "'," // enquiry_sob_id
					+ " 32,"// customer_accgroup_id
					+ " 1,"// customer_type
					+ "'" + emp_id + "'," // entry_id
					+ "'" + ToLongDate(kknow()) + "'," // entry date
					+ " 0," // customer_modified_id
					+ " '')"; // customer_modified_date

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			// ////SOP("StrSql==customer==" + StrSql);
			while (rs.next()) {
				insurenquiry_customer_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// Adding contact details
	public void AddContactFields() throws SQLException {
		try {
			// ////SOP("COMING CONTACT");

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle,"
					+ " contact_mobile1,"
					+ " contact_mobile1_phonetype_id,"
					// + " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_phone1,"
					+ " contact_phone2,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date"
					+ ")"
					+ " VALUES "
					+ " (" + insurenquiry_customer_id + "," // contact_customer_id
					+ " 10," // contact_contacttype_id
					+ " " + insurenquiry_contact_title_id + "," // contact_title_id
					+ " '" + insurenquiry_contact_fname + "'," // contact_fname
					+ " '" + insurenquiry_contact_lname + "'," // contact_lname
					+ " '" + insurenquiry_contact_jobtitle + "'," // contact_jobtitle
					+ " '" + insurenquiry_contact_mobile1 + "'," // contact_mobile1
					+ " 4,"// contact_mobile1_phonetype_id
					// + " ''," // contact_mobile2
					+ " '" + insurenquiry_contact_email1 + "'," // contact_email1
					+ " ''," // contact_email2
					+ " ''," // contact_phone1
					+ " ''," // contact_phone2
					+ " ''," // contact_address
					+ " " + insurenquiry_branch_city_id + "," // contact_city_id
					+ " ''," // contact_pin
					+ " '1'," // contact_active
					+ " ''," // contact_notes
					+ " '" + emp_id + "'," // contact_entry_id
					+ " '" + ToLongDate(kknow()) + "')"; // contact_entry_date
			// ////SOP("StrSql=====contact===" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				insurenquiry_contact_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void AddInsurFollowupFields(String insurenquiry_id) throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " (insurenquiryfollowup_insurenquiry_id,"
					+ " insurenquiryfollowup_followup_time,"
					+ " insurenquiryfollowup_entry_id,"
					+ " insurenquiryfollowup_entry_time,"
					+ " insurenquiryfollowup_trigger)"
					+ " VALUES"
					+ " (" + insurenquiry_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + insurfollowup_entry_id + ","
					+ " '" + insurfollowup_entry_date + "',"// insurfollowup_entry_time
					+ " 0)";
			// SOP("StrSql====followup=" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				insurfollowup_id = rs.getString(1);

			}
			rs.close();
			// ////SOP("insurfollowup_id===123===" + insurfollowup_id);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// Adding vec details
	public void AddInsurEnquiryFields(HttpServletResponse response) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			// ////SOP("add VEH===");
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (insurenquiry_id.equals("0")) {
					AddCustomerFields();
					AddContactFields();
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " (insurenquiry_branch_id,"
							+ " insurenquiry_date,"
							+ " insurenquiry_customer_id,"
							+ " insurenquiry_contact_id,"
							+ " insurenquiry_variant_id,"
							+ " insurenquiry_reg_no,"
							+ " insurenquiry_emp_id,"
							+ " insurenquiry_refemp_id,"
							+ " insurenquiry_insurtype_id,"
							+ " insurenquiry_soe_id,"
							+ " insurenquiry_sob_id,"
							+ " insurenquiry_campaign_id,"
							+ " insurenquiry_insurstage_id,"
							+ " insurenquiry_insurstatus_id,"
							+ " insurenquiry_notes,"
							+ " insurenquiry_entry_id,"
							+ " insurenquiry_entry_date "
							+ ")"
							+ " VALUES "
							+ " (" + insurenquiry_branch_id + "," // insurance_veh_branch_id
							+ "'" + ConvertShortDateToStr(insurenquiry_date) + "',"
							+ " " + insurenquiry_customer_id + "," // customer_id
							+ " " + insurenquiry_contact_id + ","// contact_id
							+ " " + insurenquiry_variant_id + "," // insurenquiry_variant_id
							+ " '" + insurenquiry_reg_no + "'," // reg. no
							+ " " + insurenquiry_emp_id + "," // veh_insuremp_id
							+ " " + insurenquiry_refemp_id + ","// insurenquiry_refemp_id
							+ " " + insurenquiry_insurtype_id + ","
							+ " " + insurenquiry_soe_id + ","
							+ " " + insurenquiry_sob_id + ","
							+ " " + insurenquiry_campaign_id + ","
							+ " 1," // insurenquiry_stage_id
							+ " 1," // insurenquiry_status_id
							+ " ''," // veh_notes
							+ " '" + emp_id + "'," // entry_emp_id
							+ " '" + ToLongDate(kknow()) + "')"; // veh_entry_date

					// SOP("StrSql===insert veh===" + StrSql);
					stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						insurenquiry_id = rs.getString(1);
					}
					rs.close();
					if (!insurenquiry_id.equals("0")) {
						msg1 += "Insurance Enquiry added successfully!";
					}
				}
				if (!insurenquiry_id.equals("0")) {
					AddInsurFollowupFields(insurenquiry_id);

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue"
							+ ") VALUES ("
							+ "'" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 'NEW INSURANCE ENQUIRY ADDED',"
							+ " '',"
							+ " ''"
							+ ")";
					stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);

				}

				if (comp_email_enable.equals("1")
						&& config_email_enable.equals("1")
						&& !branch_email1.equals("")
						&& brandconfig_insur_enquiry_email_enable.equals("1")) {

					if (!brandconfig_insur_enquiry_email_sub.equals("")
							&& !brandconfig_insur_enquiry_email_format.equals("")) {
						if (!branch_email1.equals("") && !insurenquiry_contact_email1.equals("")) {
							SendEnquiryEmail(comp_id, "customer", insurenquiry_contact_email1);
						}
					}

					if (!brandconfig_insur_enquiry_exe_email_sub.equals("")
							&& !brandconfig_insur_enquiry_exe_email_format.equals("")) {
						if (!branch_email1.equals("") && !crm_emp_email1.equals(""))
							SendEnquiryEmail(comp_id, "executive", crm_emp_email1);
					}
				}

				if (comp_sms_enable.equals("1")
						&& config_sms_enable.equals("1")
						&& brandconfig_insur_enquiry_sms_enable.equals("1")) {
					if (!brandconfig_insur_enquiry_sms_format.equals("")) {
						if (!insurenquiry_contact_mobile1.equals("")) {
							SendEnquirySMS(comp_id, "customer", insurenquiry_contact_mobile1);
						}
					}

					if (!brandconfig_insur_enquiry_exe_sms_format.equals("")) {
						if (!crm_emp_mobile1.equals("")) {
							SendEnquirySMS(comp_id, "executive", crm_emp_mobile1);
						}
						if (!crm_emp_mobile2.equals("")) {
							SendEnquirySMS(comp_id, "executive", crm_emp_mobile2);
						}
					}
				}
				// SOP("cooming.....");
				// commit emaiil and sms transactions
				conntx.commit();
				// SOP("cooming.....");
			} catch (Exception ex) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public String PopulateBranches(String comp_id, String insurenquiry_branch_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_branchtype_id IN (6)"
					+ " AND branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// ////SOP("StrSql == " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), insurenquiry_branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append("</option>\n");
			}
			crs.close();
			return stringval.toString();

		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
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
			// ////SOP("PopulateInsurExecutive-==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insurance_veh_executive\" id=\"dr_insurance_veh_executive\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), insurenquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY insurtype_id"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Insurance Type</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(Selectdrop(crs.getInt("insurtype_id"), insurenquiry_insurtype_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();

	}

	// public String PopulateFollowuptype(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"0\"> Select </option>\n");
	// try {
	// StrSql = "SELECT insurfollowuptype_id, insurfollowuptype_name"
	// + " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup_type"
	// + " GROUP BY insurfollowuptype_id"
	// + " ORDER BY insurfollowuptype_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("insurfollowuptype_id"));
	// Str.append(StrSelectdrop(crs.getString("insurfollowuptype_id"), insurenquiryfollowup_followuptype_id));
	// Str.append(">").append(crs.getString("insurfollowuptype_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1=1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id=" + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), insurenquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB(String insurenquiry_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// ////SOP("insurance_sob_id-==" + insurance_sob_id);
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1";
			if (!insurenquiry_soe_id.equals("0")) {
				StrSql += " AND soetrans_soe_id = " + insurenquiry_soe_id + "";
			}
			StrSql += " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// ////SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insur_sob_id\" id=\"dr_insur_sob_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), insurenquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			// ////SOP("Str===" + Str);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	//
	public String PopulateCampaign(String comp_id, String insurenquiry_branch_id) {
		// SOP("insurenquiry_branch_id---populatecampaign-----" + insurenquiry_branch_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1 ";
			if (!insurenquiry_branch_id.equals("0")) {
				StrSql += " AND camptrans_branch_id = " + insurenquiry_branch_id;
			}
			StrSql += " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ToLongDate((kknow())) + "',1,8) "
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ToLongDate(kknow()) + "',1,8) "
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// SOP("PopulateCampaign-------" + StrSql);
			// ////SOP("insurance_veh_branch_id==" + insurance_veh_branch_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insur_campaign_id\" id=\"dr_insur_campaign_id\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), insurenquiry_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
						.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			// ////SOP("Str==" + Str);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	protected void populateConfigDetails(String comp_id) {
		try {
			// ////SOP(" COMING populateConfigDetails ");

			StrSql = "SELECT "
					+ " config_email_enable, "
					+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
					+ " branch_email1,"
					+ " emp_email1, emp_email2,"
					+ " emp_mobile1, emp_mobile2,"
					+ " brandconfig_insur_enquiry_email_enable, "
					+ " brandconfig_insur_enquiry_email_sub,"
					+ " brandconfig_insur_enquiry_email_format, "
					+ " brandconfig_insur_enquiry_exe_email_sub,"
					+ " brandconfig_insur_enquiry_exe_email_format, "
					+ " brandconfig_insur_enquiry_sms_enable, "
					+ " brandconfig_insur_enquiry_sms_format,"
					+ " brandconfig_insur_enquiry_exe_sms_format"

					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + insurenquiry_emp_id + ","
					+ compdb(comp_id) + "axela_config,"
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE 1 = 1"
					+ " AND branch_id = " + insurenquiry_branch_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("strsql==POP=" + StrSql);
			// crs.beforeFirst();
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				crm_emp_email1 = crs.getString("emp_email1");
				crm_emp_email2 = crs.getString("emp_email2");
				crm_emp_mobile1 = crs.getString("emp_mobile1");
				crm_emp_mobile2 = crs.getString("emp_mobile2");
				brandconfig_insur_enquiry_email_enable = crs.getString("brandconfig_insur_enquiry_email_enable");
				brandconfig_insur_enquiry_email_sub = crs.getString("brandconfig_insur_enquiry_email_sub");
				brandconfig_insur_enquiry_email_format = crs.getString("brandconfig_insur_enquiry_email_format");
				brandconfig_insur_enquiry_exe_email_sub = crs.getString("brandconfig_insur_enquiry_exe_email_sub");
				brandconfig_insur_enquiry_exe_email_format = crs.getString("brandconfig_insur_enquiry_exe_email_format");
				brandconfig_insur_enquiry_sms_enable = crs.getString("brandconfig_insur_enquiry_sms_enable");
				brandconfig_insur_enquiry_sms_format = crs.getString("brandconfig_insur_enquiry_sms_format");
				brandconfig_insur_enquiry_exe_sms_format = crs.getString("brandconfig_insur_enquiry_exe_sms_format");

				// //SOP("brandconfig_vehfollowup_insur_email_enable=1=" + brandconfig_insur_enquiry_email_enable);
				// //SOP("brandconfig_vehfollowup_insur_email_sub=1=" + brandconfig_insur_enquiry_email_sub);
				// //SOP("brandconfig_vehfollowup_insur_email_format=1=" + brandconfig_insur_enquiry_email_format);
				// //SOP("brandconfig_vehfollowup_insur_sms_enable=1=" + brandconfig_insur_enquiry_sms_enable);
				// //SOP("brandconfig_vehfollowup_insur_sms_format=1=" + brandconfig_insur_enquiry_sms_format);
				// //SOP("branch_email1=1=" + branch_email1);

				if (!crm_emp_email2.equals("")) {
					crm_emp_email1 = crm_emp_email1 + "," + crm_emp_email2;
				}
			}
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	protected void SendEnquiryEmail(String comp_id, String type, String email_to) throws SQLException {
		String emailmsg = "", sub = "", email_contact_id = "", email_contact_name = "";

		if (type.equals("customer")) {
			email_contact_id = insurenquiry_contact_id;
			email_contact_name = insurenquiry_contact_fname + " " + insurenquiry_contact_lname;
			sub = brandconfig_insur_enquiry_email_sub;
			emailmsg = brandconfig_insur_enquiry_email_format;
		}
		else if (type.equals("executive")) {
			email_contact_id = "0";
			email_contact_name = "";
			sub = brandconfig_insur_enquiry_exe_email_sub;
			emailmsg = brandconfig_insur_enquiry_exe_email_format;
		}

		sub = "REPLACE('" + sub + "','[ENQUIRYID]', insurenquiry_id)";
		sub = "REPLACE(" + sub + ",'[REGNO]',insurenquiry_reg_no)";
		sub = "REPLACE(" + sub + ",'[MODEL]', preownedmodel_name)";
		sub = "REPLACE(" + sub + ",'[VARIANT]',variant_name)";
		sub = "REPLACE(" + sub + ",'[REFEXENAME]',refemp.emp_name)";
		sub = "REPLACE(" + sub + ",'[REFEXEJOBTITLE]',refjt.jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[REFEXEMOBILE1]',refemp.emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[REFEXEEMAIL1]',refemp.emp_email1)";
		sub = "REPLACE(" + sub + ",'[INSUREXENAME]',insuremp.emp_name)";
		sub = "REPLACE(" + sub + ",'[INSUREXEJOBTITLE]',insurjt.jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[INSUREXEMOBILE1]',insuremp.emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[INSUREXEMAIL1]',insuremp.emp_email1)";
		sub = "REPLACE(" + sub + ",'[CONTACTID]',contact_id)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(title_desc,' ',contact_fname,' ',contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[BRANCHNAME]',branch_name)";
		sub = "REPLACE(" + sub + ",'[BRANCHPHONE]', branch_phone1)";
		sub = "REPLACE(" + sub + ",'[BRANCHMOBILE1]',branch_mobile1)";
		sub = "REPLACE(" + sub + ",'[BRANCHEMAIL1]',branch_email1)";

		emailmsg = "REPLACE('" + emailmsg + "','[ENQUIRYID]', insurenquiry_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[REGNO]',insurenquiry_reg_no)";
		emailmsg = "REPLACE(" + emailmsg + ",'[MODEL]', preownedmodel_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[VARIANT]',variant_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[REFEXENAME]',refemp.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[REFEXEJOBTITLE]',refjt.jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[REFEXEMOBILE1]',refemp.emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[REFEXEEMAIL1]',refemp.emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXENAME]',insuremp.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEJOBTITLE]',insurjt.jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEMOBILE1]',insuremp.emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEMAIL1]',insuremp.emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTID]',contact_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc,' ',contact_fname,' ',contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]',branch_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHPHONE]', branch_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHMOBILE1]',branch_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHEMAIL1]',branch_email1)";

		try {
			StrSql = "SELECT "
					+ "	branch_id,"
					+ " " + email_contact_id + ", "
					+ " '" + email_contact_name + "', "
					+ " '" + branch_email1 + "', "
					+ " '" + email_to + "', "
					// + " '" + email_cc + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_followup ON insurenquiryfollowup_insurenquiry_id = insurenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp insuremp ON insuremp.emp_id = insurenquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle insurjt ON insuremp.emp_jobtitle_id = insurjt.jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp refemp ON refemp.emp_id = insurenquiry_refemp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle refjt ON refemp.emp_jobtitle_id = refjt.jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"

					+ " WHERE insurenquiry_id =" + insurenquiry_id;

			// SOP("StrSql--insurance-------mail--" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					// + " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql---------insert--" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void SendEnquirySMS(String comp_id, String type, String sms_to) throws SQLException {
		String smsmsg = "", sms_contact_id = "", sms_contact_name = "";

		if (type.equals("customer")) {
			sms_contact_id = insurenquiry_contact_id;
			sms_contact_name = insurenquiry_contact_fname + " " + insurenquiry_contact_lname;
			smsmsg = brandconfig_insur_enquiry_sms_format;

		}
		else if (type.equals("executive")) {
			sms_contact_id = "0";
			sms_contact_name = "";
			smsmsg = brandconfig_insur_enquiry_exe_sms_format;

		}
		smsmsg = "REPLACE('" + smsmsg + "','[ENQUIRYID]', insurenquiry_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[REGNO]',insurenquiry_reg_no)";
		smsmsg = "REPLACE(" + smsmsg + ",'[MODEL]', preownedmodel_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[VARIANT]',variant_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[REFEXENAME]',refemp.emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[REFEXEJOBTITLE]',refjt.jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[REFEXEMOBILE1]',refemp.emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[REFEXEEMAIL1]',refemp.emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXENAME]',insuremp.emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEJOBTITLE]',insurjt.jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEMOBILE1]',insuremp.emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEMAIL1]',insuremp.emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTID]',contact_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc,' ',contact_fname,' ',contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHPHONE]', branch_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHMOBILE1]',branch_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHEMAIL1]',branch_email1)";

		try {
			StrSql = "SELECT"
					+ " branch_id ,"
					+ " " + sms_contact_id + ","
					+ " '" + sms_contact_name + "',"
					+ " '" + sms_to + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '" + emp_id + "',"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_followup ON insurenquiryfollowup_insurenquiry_id = insurenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp insuremp ON insuremp.emp_id = insurenquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle insurjt ON insuremp.emp_jobtitle_id = insurjt.jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp refemp ON refemp.emp_id = insurenquiry_refemp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle refjt ON refemp.emp_jobtitle_id = refjt.jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE insurenquiry_id =" + insurenquiry_id;

			// SOP("Before sms insert====" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_emp_id,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// ////SOP("StrSql--------SMS--" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
