package axela.portal;
/*Bhagwan Singh 21/01/2013*/

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Dash extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "", SqlJoin = "";
	public String StrHTML = "";
	public String exe_id = "0";
	public String emp_name = "";
	public String emp_uname = "";
	public String emp_role_id = "";
	public String emp_department_id = "0";
	public String emp_ref_no = "";
	public String emp_jobtitle_id = "0";
	public String emp_sex = "";
	public String emp_dob = "";
	public String drop_month = "";
	public String drop_day = "";
	public String drop_year = "";
	public String emp_married = "";
	public String emp_qualification = "";
	public String emp_certification = "";
	public String emp_phone1 = "";
	public String emp_phone2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_upass = "";
	public String emp_address = "";
	public String emp_city = "";
	public String emp_pin = "";
	public String emp_state = "";
	public String emp_landmark = "";
	public String emp_access_web = "1";
	public String emp_access_app = "1";
	public String emp_active = "1";
	public String emp_branch_id = "0";
	public String emp_weeklyoff_id = "0";
	public String emp_module_portal = "";
	public String emp_module_activity = "";
	public String emp_module_customer = "";
	public String emp_module_sales = "";
	public String emp_module_preowned = "";
	public String emp_module_service = "";
	public String emp_module_accessories = "";
	public String emp_module_insurance = "";
	public String emp_module_helpdesk = "";
	public String emp_module_inventory = "";
	public String emp_module_accounting = "";
	public String emp_module_invoice = "";
	public String emp_module_app = "";
	public String emp_sales = "";
	public String emp_quote_priceupdate = "";
	public String emp_close_enquiry = "0";
	public String emp_mtrboard = "";
	public String emp_preowned = "";
	public String emp_quote_discountupdate = "";
	public String emp_so_priceupdate = "";
	public String emp_so_discountupdate = "";
	// ...price
	public String emp_service = "";
	public String emp_technician = "";
	public String emp_crm = "";
	public String emp_pickup_driver = "";
	public String emp_insur = "";
	public String emp_fieldinsur = "";
	public String emp_service_psf = "";
	public String emp_service_psf_iacs = "";
	public String emp_jc_priceupdate = "";
	public String emp_jc_discountupdate = "";
	// ..//.price
	public String emp_ticket_owner = "";
	public String emp_ticket_close = "";
	public String emp_mis_access = "";
	public String emp_report_access = "";
	public String emp_export_access = "";
	public String emp_copy_access = "";
	public String emp_dailystatus_report = "";
	public String emp_priorityactivity_level1 = "";
	public String emp_priorityactivity_level2 = "";
	public String emp_priorityactivity_level3 = "";
	public String emp_priorityactivity_level4 = "";
	public String emp_priorityactivity_level5 = "";
	public String emp_priorityproject_level1 = "";
	public String emp_priorityproject_level2 = "";
	public String emp_priorityproject_level3 = "";
	public String emp_priorityproject_level4 = "";
	public String emp_priorityproject_level5 = "";
	public String emp_prioritytask_level1 = "";
	public String emp_prioritytask_level2 = "";
	public String emp_prioritytask_level3 = "";
	public String emp_prioritytask_level4 = "";
	public String emp_prioritytask_level5 = "";
	public String emp_priorityenquiryfollowup_level1 = "";
	public String emp_priorityenquiryfollowup_level2 = "";
	public String emp_priorityenquiryfollowup_level3 = "";
	public String emp_priorityenquiryfollowup_level4 = "";
	public String emp_priorityenquiryfollowup_level5 = "";
	public String emp_priorityenquiry_level1 = "";
	public String emp_priorityenquiry_level2 = "";
	public String emp_priorityenquiry_level3 = "";
	public String emp_priorityenquiry_level4 = "";
	public String emp_priorityenquiry_level5 = "";
	public String emp_prioritycrmfollowup_level1 = "";
	public String emp_prioritycrmfollowup_level2 = "";
	public String emp_prioritycrmfollowup_level3 = "";
	public String emp_prioritycrmfollowup_level4 = "";
	public String emp_prioritycrmfollowup_level5 = "";
	public String emp_prioritybalance_level1 = "";
	public String emp_prioritybalance_level2 = "";
	public String emp_prioritybalance_level3 = "";
	public String emp_prioritybalance_level4 = "";
	public String emp_prioritybalance_level5 = "";
	public String emp_prioritycall_level1 = "";
	public String emp_prioritycall_level2 = "";
	public String emp_prioritycall_level3 = "";
	public String emp_prioritycall_level4 = "";
	public String emp_prioritycall_level5 = "";
	public String emp_priorityjc_level1 = "";
	public String emp_priorityjc_level2 = "";
	public String emp_priorityjc_level3 = "";
	public String emp_priorityjc_level4 = "";
	public String emp_priorityjc_level5 = "";
	public String emp_priorityticket_level1 = "";
	public String emp_priorityticket_level2 = "";
	public String emp_priorityticket_level3 = "";
	public String emp_priorityticket_level4 = "";
	public String emp_priorityticket_level5 = "";
	public String emp_clicktocall = "";
	public String emp_emaxpm = "";
	public String emp_stock_ageing = "";
	public String emp_clicktocall_username = "";
	public String emp_clicktocall_password = "";
	public String emp_clicktocall_campaign = "";
	public String emp_routeno = "";
	public String emp_callerid = "";
	public String emp_device_id = "";
	public String emp_ip_access = "";
	public String emp_all_exe = "";
	public String emp_all_branches = "";
	public String emp_prevexp[];
	public String emp_prevexp_year = "0";
	public String emp_prevexp_month = "0";
	public String emp_date_of_join = "";
	public String date_of_join = "";
	public String emp_date_of_relieve = "";
	public String date_of_relieve = "";
	public String emp_reason_of_leaving = "";
	public String emp_notes = "";
	public String[] exe_team_trans = new String[10];
	public String[] exe_soe_trans = new String[10];
	public String[] exe_branch_trans = new String[10];
	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String emp_theme_id = "1";
	public String emp_entry_id = "0";
	public String emp_entry_date = "";
	public String emp_modified_id = "0";
	public String emp_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public DecimalFormat deci = new DecimalFormat("0.00");
	public String emp_invoice_priceupdate = "";
	public String emp_invoice_discountupdate = "";
	public int emp_count = empcount;
	public int active_empcount = 0;
	public String active = "";
	public String emp_uuid = "";
	// for access
	public static ArrayList<String> comp_mod_name_arr = new ArrayList<String>();
	public String StrSearch = "";
	public String RecCountDisplay = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_executive_add", request, response);
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				PopulateFields(response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				exe_id = crs.getString("emp_id");
				emp_name = crs.getString("emp_name");
				emp_uname = crs.getString("emp_uname");
				emp_role_id = crs.getString("emp_role_id");
				emp_department_id = crs.getString("emp_department_id");
				emp_ref_no = crs.getString("emp_ref_no");
				emp_qualification = crs.getString("emp_qualification");
				emp_certification = crs.getString("emp_certification");
				emp_phone1 = crs.getString("emp_phone1");
				emp_phone2 = crs.getString("emp_phone2");
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				emp_address = crs.getString("emp_address");
				emp_city = crs.getString("emp_city");
				emp_pin = crs.getString("emp_pin");
				emp_state = crs.getString("emp_state");
				emp_landmark = crs.getString("emp_landmark");
				emp_stock_ageing = crs.getString("emp_stock_ageing");
				emp_clicktocall = crs.getString("emp_clicktocall");
				emp_emaxpm = crs.getString("emp_emaxpm");
				emp_clicktocall_username = crs.getString("emp_clicktocall_username");
				emp_clicktocall_password = crs.getString("emp_clicktocall_password");
				emp_clicktocall_campaign = crs.getString("emp_clicktocall_campaign");
				emp_routeno = crs.getString("emp_routeno");
				emp_callerid = crs.getString("emp_callerid");
				emp_device_id = crs.getString("emp_device_id");
				emp_ip_access = crs.getString("emp_ip_access");
				emp_notes = crs.getString("emp_notes");
				emp_jobtitle_id = crs.getString("emp_jobtitle_id");
				emp_sex = crs.getString("emp_sex");
				emp_married = crs.getString("emp_married");
				emp_access_web = crs.getString("emp_access_web");
				emp_access_app = crs.getString("emp_access_app");
				emp_active = crs.getString("emp_active");
				emp_branch_id = crs.getString("emp_branch_id");
				emp_weeklyoff_id = crs.getString("emp_weeklyoff_id");
				emp_upass = crs.getString("emp_upass");
				// Admin
				emp_module_portal = crs.getString("emp_module_portal");
				emp_module_activity = crs.getString("emp_module_activity");
				emp_module_customer = crs.getString("emp_module_customer");
				emp_module_sales = crs.getString("emp_module_sales");
				emp_module_preowned = crs.getString("emp_module_preowned");
				emp_module_service = crs.getString("emp_module_service");
				emp_module_accessories = crs.getString("emp_module_accessories");
				emp_module_insurance = crs.getString("emp_module_insurance");
				emp_module_helpdesk = crs.getString("emp_module_helpdesk");
				emp_module_inventory = crs.getString("emp_module_inventory");
				emp_module_accounting = crs.getString("emp_module_accounting");
				emp_module_invoice = crs.getString("emp_module_invoice");
				emp_module_app = crs.getString("emp_module_app");
				emp_sales = crs.getString("emp_sales");
				emp_close_enquiry = crs.getString("emp_close_enquiry");
				emp_mtrboard = crs.getString("emp_mtrboard");
				emp_preowned = crs.getString("emp_preowned");
				emp_quote_priceupdate = crs.getString("emp_quote_priceupdate");
				emp_quote_discountupdate = crs
						.getString("emp_quote_discountupdate");
				emp_so_priceupdate = crs.getString("emp_so_priceupdate");
				emp_so_discountupdate = crs.getString("emp_so_discountupdate");
				emp_invoice_priceupdate = crs.getString("emp_invoice_priceupdate");
				emp_invoice_discountupdate = crs.getString("emp_invoice_discountupdate");
				// ...
				emp_service = crs.getString("emp_service");
				emp_technician = crs.getString("emp_technician");
				emp_crm = crs.getString("emp_crm");
				emp_pickup_driver = crs.getString("emp_pickup_driver");
				emp_service_psf = crs.getString("emp_service_psf");
				emp_service_psf_iacs = crs.getString("emp_service_psf_iacs");
				emp_insur = crs.getString("emp_insur");
				emp_fieldinsur = crs.getString("emp_fieldinsur");
				emp_jc_priceupdate = crs.getString("emp_jc_priceupdate");
				emp_jc_discountupdate = crs.getString("emp_jc_discountupdate");
				// ...
				emp_ticket_owner = crs.getString("emp_ticket_owner");
				emp_ticket_close = crs.getString("emp_ticket_close");

				emp_mis_access = crs.getString("emp_mis_access");
				emp_export_access = crs.getString("emp_export_access");
				emp_report_access = crs.getString("emp_report_access");
				emp_copy_access = crs.getString("emp_copy_access");
				emp_dailystatus_report = crs.getString("emp_dailystatus_report");

				emp_priorityactivity_level1 = crs.getString("emp_priorityactivity_level1");
				emp_priorityactivity_level2 = crs.getString("emp_priorityactivity_level2");
				emp_priorityactivity_level3 = crs.getString("emp_priorityactivity_level3");
				emp_priorityactivity_level4 = crs.getString("emp_priorityactivity_level4");
				emp_priorityactivity_level5 = crs.getString("emp_priorityactivity_level5");
				emp_priorityproject_level1 = crs.getString("emp_priorityproject_level1");
				emp_priorityproject_level2 = crs.getString("emp_priorityproject_level2");
				emp_priorityproject_level3 = crs.getString("emp_priorityproject_level3");
				emp_priorityproject_level4 = crs.getString("emp_priorityproject_level4");
				emp_priorityproject_level5 = crs.getString("emp_priorityproject_level5");
				emp_prioritytask_level1 = crs.getString("emp_prioritytask_level1");
				emp_prioritytask_level2 = crs.getString("emp_prioritytask_level2");
				emp_prioritytask_level3 = crs.getString("emp_prioritytask_level3");
				emp_prioritytask_level4 = crs.getString("emp_prioritytask_level4");
				emp_prioritytask_level5 = crs.getString("emp_prioritytask_level5");
				emp_priorityenquiryfollowup_level1 = crs.getString("emp_priorityenquiryfollowup_level1");
				emp_priorityenquiryfollowup_level2 = crs.getString("emp_priorityenquiryfollowup_level2");
				emp_priorityenquiryfollowup_level3 = crs.getString("emp_priorityenquiryfollowup_level3");
				emp_priorityenquiryfollowup_level4 = crs.getString("emp_priorityenquiryfollowup_level4");
				emp_priorityenquiryfollowup_level5 = crs.getString("emp_priorityenquiryfollowup_level5");
				emp_priorityenquiry_level1 = crs.getString("emp_priorityenquiry_level1");
				emp_priorityenquiry_level2 = crs.getString("emp_priorityenquiry_level2");
				emp_priorityenquiry_level3 = crs.getString("emp_priorityenquiry_level3");
				emp_priorityenquiry_level4 = crs.getString("emp_priorityenquiry_level4");
				emp_priorityenquiry_level5 = crs.getString("emp_priorityenquiry_level5");
				emp_prioritycrmfollowup_level1 = crs.getString("emp_prioritycrmfollowup_level1");
				emp_prioritycrmfollowup_level2 = crs.getString("emp_prioritycrmfollowup_level2");
				emp_prioritycrmfollowup_level3 = crs.getString("emp_prioritycrmfollowup_level3");
				emp_prioritycrmfollowup_level4 = crs.getString("emp_prioritycrmfollowup_level4");
				emp_prioritycrmfollowup_level5 = crs.getString("emp_prioritycrmfollowup_level5");
				emp_prioritybalance_level1 = crs.getString("emp_prioritybalance_level1");
				emp_prioritybalance_level2 = crs.getString("emp_prioritybalance_level2");
				emp_prioritybalance_level3 = crs.getString("emp_prioritybalance_level3");
				emp_prioritybalance_level4 = crs.getString("emp_prioritybalance_level4");
				emp_prioritybalance_level5 = crs.getString("emp_prioritybalance_level5");
				emp_prioritycall_level1 = crs.getString("emp_prioritycall_level1");
				emp_prioritycall_level2 = crs.getString("emp_prioritycall_level2");
				emp_prioritycall_level3 = crs.getString("emp_prioritycall_level3");
				emp_prioritycall_level4 = crs.getString("emp_prioritycall_level4");
				emp_prioritycall_level5 = crs.getString("emp_prioritycall_level5");

				emp_priorityjc_level1 = crs.getString("emp_priorityjc_level1");
				emp_priorityjc_level2 = crs.getString("emp_priorityjc_level2");
				emp_priorityjc_level3 = crs.getString("emp_priorityjc_level3");
				emp_priorityjc_level4 = crs.getString("emp_priorityjc_level4");
				emp_priorityjc_level5 = crs.getString("emp_priorityjc_level5");

				emp_priorityticket_level1 = crs.getString("emp_priorityticket_level1");
				emp_priorityticket_level2 = crs.getString("emp_priorityticket_level2");
				emp_priorityticket_level3 = crs.getString("emp_priorityticket_level3");
				emp_priorityticket_level4 = crs.getString("emp_priorityticket_level4");
				emp_priorityticket_level5 = crs.getString("emp_priorityticket_level5");

				// .... payroll.....
				emp_all_exe = crs.getString("emp_all_exe");
				// .....eof payroll....
				emp_all_branches = crs.getString("emp_all_branches");

				if (!crs.getString("emp_prevexp").equals("")) {
					emp_prevexp = crs.getString("emp_prevexp").split(",");
					emp_prevexp_year = emp_prevexp[0];
					emp_prevexp_month = emp_prevexp[1];
				}
				date_of_join = strToShortDate(crs.getString("emp_date_of_join"));
				date_of_relieve = strToShortDate(crs.getString("emp_date_of_relieve"));
				emp_reason_of_leaving = crs.getString("emp_reason_of_leaving");
				emp_dob = crs.getString("emp_dob");

				drop_month = SplitMonth(emp_dob);
				drop_day = SplitDate(emp_dob);
				drop_year = SplitYear(emp_dob);

				emp_entry_id = crs.getString("emp_entry_id");
				if (!emp_entry_id.equals("0")) {
					entry_by = Exename(comp_id, Integer.parseInt(emp_entry_id));
				}
				entry_date = strToLongDate(crs.getString("emp_entry_date"));
				emp_modified_id = crs.getString("emp_modified_id");
				if (!emp_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(emp_modified_id));
					modified_date = strToLongDate(crs.getString("emp_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateRole() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		StrSql = "SELECT role_id, role_name" + " FROM " + compdb(comp_id)
				+ "axela_emp_role" + " ORDER BY role_name";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("role_id"));
				Str.append(StrSelectdrop(crs.getString("role_id"), emp_role_id));
				Str.append(">").append(crs.getString("role_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		StrSql = "SELECT department_id, department_name" + " FROM "
				+ compdb(comp_id) + "axela_emp_department"
				+ " ORDER BY department_name";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("department_id"));
				Str.append(StrSelectdrop(crs.getString("department_id"), emp_department_id));
				Str.append(">").append(crs.getString("department_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobtitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jobtitle_id, jobtitle_desc" + " FROM "
					+ compdb(comp_id) + "axela_jobtitle "
					+ " ORDER BY jobtitle_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = -1> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jobtitle_id"));
				Str.append(StrSelectdrop(crs.getString("jobtitle_id"), emp_jobtitle_id));
				Str.append(">").append(crs.getString("jobtitle_desc")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateMonth() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		Str.append("<option value = 1").append(Selectdrop(1, drop_month))
				.append(">January</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, drop_month))
				.append(">February</option>\n");
		Str.append("<option value = 3").append(Selectdrop(3, drop_month))
				.append(">March</option>\n");
		Str.append("<option value = 4").append(Selectdrop(4, drop_month))
				.append(">April</option>\n");
		Str.append("<option value = 5").append(Selectdrop(5, drop_month))
				.append(">May</option>\n");
		Str.append("<option value = 6").append(Selectdrop(6, drop_month))
				.append(">June</option>\n");
		Str.append("<option value = 7").append(Selectdrop(7, drop_month))
				.append(">July</option>\n");
		Str.append("<option value = 8").append(Selectdrop(8, drop_month))
				.append(">August</option>\n");
		Str.append("<option value = 9").append(Selectdrop(9, drop_month))
				.append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, drop_month))
				.append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, drop_month))
				.append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, drop_month))
				.append(">December</option>\n");
		return Str.toString();
	}

	public String PopulateDay() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		for (int i = 1; i <= 31; i++) {
			Str.append("<option value = ").append(i).append("").append(Selectdrop(i, drop_day));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		for (int i = 1920; i <= 2007; i++) {
			Str.append("<option value = ").append(i).append("");
			Str.append(Selectdrop(i, drop_year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateMarried() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		Str.append("<option value = 1").append(Selectdrop(1, emp_married))
				.append(">Married</option>\n");
		Str.append("<option value = 0").append(Selectdrop(0, emp_married))
				.append(">Unmarried</option>\n");
		return Str.toString();
	}

	public String PopulateSex() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		Str.append("<option value = 1").append(Selectdrop(1, emp_sex)).append(">Male</option>\n)");
		Str.append("<option value = 0").append(Selectdrop(0, emp_sex)).append(">Female</option>\n");
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code" + " FROM "
					+ compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = -1> Select </option>\n");
			Str.append("<option value =0 ").append(StrSelectdrop("0", emp_branch_id));
			Str.append(">Head Office</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), emp_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (");
				Str.append(crs.getString("branch_code"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateWeeklyOff() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT weeklyoff_name, weeklyoff_id" + " FROM "
					+ compdb(comp_id) + "axela_emp_weeklyoff"
					+ " ORDER BY weeklyoff_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("weeklyoff_id"));
				Str.append(StrSelectdrop(crs.getString("weeklyoff_id"), emp_weeklyoff_id));
				Str.append(">").append(crs.getString("weeklyoff_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchs() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_name, branch_id, branch_code" + " FROM "
					+ compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(">").append(crs.getString("branch_name")).append(" (");
				Str.append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_name, branch_id, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_branch ON branch_id = emp_branch_id"
					+ " WHERE emp_id = " + exe_id + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(" selected>").append(crs.getString("branch_name"));
				Str.append(" (").append(crs.getString("branch_code"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " WHERE emp_active = '1'"
					+ " AND emp_id != " + exe_id + "";
			StrSql += " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append(">");
				Str.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_exe AS ex ON ex.empexe_id = emp_id"
					+ " WHERE ex.empexe_emp_id = '" + exe_id + "'"
					+ " AND " + compdb(comp_id) + "axela_emp.emp_active = '1'"
					+ " ORDER BY " + compdb(comp_id) + "axela_emp.emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(" selected>").append(crs.getString("emp_name"));
				Str.append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name " + " FROM " + compdb(comp_id)
					+ "axela_soe " + " WHERE 1 = 1 " + " GROUP BY soe_id"
					+ " ORDER BY soe_id ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append(">");
				Str.append(crs.getString("soe_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSoeTrans(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name" + " from " + compdb(comp_id)
					+ "axela_soe" + " inner join " + compdb(comp_id)
					+ "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " where empsoe_emp_id = " + exe_id + ""
					+ " order by soe_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"));
				Str.append(" selected>").append(crs.getString("soe_name"));
				Str.append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String HistoryDetails(String comp_id, String exe_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT " + compdb(comp_id) + "axela_emp_history.*,"
					+ " CONCAT(emp.emp_name,' (',emp.emp_ref_no,')') AS emp_name, emp.emp_id "
					+ " FROM " + compdb(comp_id) + "axela_emp_history"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = history_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = history_modified_id "
					+ " WHERE history_emp_id = " + exe_id + ""
					+ " ORDER BY history_id desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div>\n</div>\n");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div class=\"table-responsive \">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">Date</th>");
					Str.append("<th>Action By</th>");
					Str.append("<th data-hide=\"phone, tablet\">Type of Action</th>");
					Str.append("<th data-hide=\"phone, tablet\">New Value</th>");
					Str.append("<th data-hide=\"phone, tablet\"> Old Value</th>");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						Str.append("<tr>\n");
						Str.append("<td valign=top align=left >")
								.append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getString("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>").append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_actiontype")).append(" </td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_newvalue")).append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_oldvalue")).append("</td>");
						Str.append("</tr>" + "\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>");
					Str.append("</div>\n </div>\n </div></div>\n");
				} else {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div></div>");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div align=center><br><br><font color=red><b>No History found!</b></font></div>");
					Str.append("</div> </div></div></div>");
				}
				crs.close();

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String ListAccess() {

		String oldSubmodule_name = "", access_name = "", submodule_name = "";
		String colHead = "";
		String modSearch = "";
		StringBuilder Str = new StringBuilder();

		/*
		 * ====For getting the module names======================================
		 */
		StrSql = "SELECT CONCAT('comp_module_',module_name) AS module_name"
				+ " FROM " + maindb() + "module";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append(crs.getString("module_name")).append(", ");
					comp_mod_name_arr.add(crs.getString("module_name"));
				}
				StrSearch = Str.toString();
				StrSearch = StrSearch.substring(0, StrSearch.lastIndexOf(","));
			}
			crs.close();
			/*
			 * ====End of module names======================================================
			 */

			StrSql = "SELECT " + StrSearch + " FROM " + compdb(comp_id) + "axela_comp";
			crs = processQuery(StrSql, 0);
			int mod_name_arr_count = comp_mod_name_arr.size();
			if (crs.isBeforeFirst()) {
				Str = new StringBuilder();
				Str.append(" and (");
				while (crs.next()) {
					for (int i = 0; i < mod_name_arr_count; i++) {
						if (crs.getString(comp_mod_name_arr.get(i)).equals("1")) {
							String mod_name[] = comp_mod_name_arr.get(i).split("_");
							Str.append(" module_name='").append(mod_name[2].toString().toLowerCase());
							Str.append("' or ");
						}
					}
				}
				modSearch = Str.toString();
				modSearch = modSearch.substring(0, modSearch.lastIndexOf("or")) + " )";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		StrSql = "SELECT access_id, access_module_id, module_name, access_name, access_rank,"
				+ " COALESCE(empaccess_access_id,0) AS empaccess_access_id"
				+ " FROM " + maindb() + "module_access"
				+ " INNER JOIN " + maindb() + "module on module_id = access_module_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_access ON empaccess_access_id = access_id"
				+ " AND empaccess_emp_id = " + exe_id
				+ " WHERE 1=1 "
				+ modSearch
				+ " ORDER BY module_rank, access_rank ";
		int i = 0;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			Str = new StringBuilder();
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				while (crs.next()) {
					i++;
					String check = "";
					access_name = crs.getString("access_name");
					if (!colHead.equals(crs.getString("module_name"))) {
						Str.append("<tr><thead><th colspan=2 align=center><strong>").append(crs.getString("module_name")).append("</strong></th></tr></thead>");
						colHead = crs.getString("module_name");
					}
					submodule_name = access_name.substring(access_name.indexOf("_") + 1, access_name.lastIndexOf("_"));
					if (!submodule_name.equals(oldSubmodule_name)) {
						Str.append("<tr><td align=right width=20%>").append(toTitleCase(submodule_name.replace("_", " ")).trim());
						Str.append(":</td><td>");
					}

					if (crs.getString("empaccess_access_id").equals(crs.getString("access_id"))) {
						check = "checked";
					}

					Str.append("<input type=hidden name=txt_access_").append(i).append(" id=txt_access_");
					Str.append(i).append(" value=").append(crs.getString("access_id")).append(" />\n");
					Str.append("<div id=\"display-line\" nowrap><input type=checkbox name=chk_access_").append(i).append(" ").append(check);
					Str.append(" id=chk_access_").append(i).append(" onchange=\"SecurityCheck3('").append("chk_access_" + i).append("',");
					Str.append("'" + crs.getString("access_id") + "', this, 'hint_chk_access_").append(i).append("');\">\n");
					Str.append(toTitleCase(access_name.substring(access_name.lastIndexOf("_") + 1)).trim());
					Str.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					oldSubmodule_name = submodule_name;
				}
				Str.append("<input type=hidden name=txt_count").append(" id=txt_count").append(" value=");
				Str.append(i).append(">");
				Str.append("</table><br>");
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Module(s) found!</font><br><br>";
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
