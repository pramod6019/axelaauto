// Ved Prakash (12 Feb 2013)
// added call n jc priorities on 30 march 2013 (smitha nag)
// removed emp_driver, presser, presser_rate n added pickup_driver on 3 april 2013 (smitha nag)
// Added Field - Ved Prakash (28th August 2013)
package axela.portal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.Ecover_WS;

public class Executives_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
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
	// public String emp_upass = "";
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
	public String emp_stock_ageing = "";
	public String emp_emaxpm = "";
	public String emp_clicktocall_username = "";
	public String emp_clicktocall_password = "";
	public String emp_clicktocall_campaign = "";
	public String emp_routeno = "";
	public String emp_callerid = "";
	public String emp_device_id = "";
	public String emp_ip_access = "";
	public String emp_all_exe = "";
	public String emp_all_branches = "";
	// public String emp_all = "";
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
	public String emp_bill_priceupdate = "";
	public String emp_bill_discountupdate = "";
	public String emp_invoice_discountupdate = "";
	public int emp_count = empcount;
	public int active_empcount = 0;
	public String active = "";
	public String emp_uuid = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button1"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						emp_mobile1 = "91-";
						date_of_join = strToShortDate(ToLongDate(kknow()));
					} else {
						CheckPerm(comp_id, "emp_role_id", request, response);
						GetValues(request, response);
						emp_entry_id = CNumeric(GetSession("emp_id", request));
						emp_entry_date = ToLongDate(kknow());
						emp_upass = GenPass(8);
						emp_uuid = "UUID()";
						AddFields();
						if (msg.equals("")) {
							StrSql = "SELECT comp_ecover_integration "
									+ "FROM " + compdb(comp_id) + "axela_comp"
									+ " WHERE comp_id=" + comp_id;
							if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
								WSEcoverRequest(request);
							}
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateExeAccess(emp_id, emp_all_exe, comp_id);
							new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
							response.sendRedirect(response
									.encodeRedirectURL("executive-access.jsp?emp_id="
											+ emp_id + ""));
						}
					}
				}
				if ("yes".equals(update)) {
					active = ExecuteQuery("SELECT emp_active FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_id = "
							+ emp_id + "");
					if (!"yes".equals(updateB)
							&& !"Delete Executive".equals(deleteB)) {
						PopulateFields();
					} else if ("yes".equals(updateB)
							&& !"Delete Executive".equals(deleteB)) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						GetValues(request, response);
						emp_modified_id = CNumeric(GetSession("emp_id", request));
						emp_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (msg.equals("")) {
							StrSql = "SELECT comp_ecover_integration "
									+ "FROM " + compdb(comp_id) + "axela_comp"
									+ " WHERE comp_id=" + comp_id;
							if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
								WSEcoverRequest(request);
							}
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateExeAccess(emp_id, emp_all_exe, comp_id);
							new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
							response.sendRedirect(response
									.encodeRedirectURL("executive-list.jsp?emp_id="
											+ emp_id
											+ "&msg=Executive Details Updated Successfully!"
											+ msg + ""));
						}
					} else if ("Delete Executive".equals(deleteB)) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						response.sendRedirect("../portal/error.jsp?" + "Access denied. Please contact system administrator!");
						// GetValues(request, response);
						// DeleteFields();
						// new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {

							response.sendRedirect(response
									.encodeRedirectURL("executive-list.jsp?msg=Executive Details Deleted Successfully!"));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		emp_name = PadQuotes(request.getParameter("txt_emp_name"));
		emp_department_id = PadQuotes(request.getParameter("drop_emp_department_id"));
		emp_role_id = PadQuotes(request.getParameter("drop_emp_role_id"));
		emp_weeklyoff_id = PadQuotes(request.getParameter("dr_weeklyoff_id"));
		emp_ref_no = PadQuotes(request.getParameter("txt_emp_ref_no"));
		emp_jobtitle_id = PadQuotes(request.getParameter("drop_emp_jobtitle_id"));
		emp_sex = PadQuotes(request.getParameter("drop_emp_sex"));
		drop_month = PadQuotes(request.getParameter("drop_DOBMonth"));
		drop_day = PadQuotes(request.getParameter("drop_DOBDay"));
		drop_year = PadQuotes(request.getParameter("drop_DOBYear"));
		emp_married = PadQuotes(request.getParameter("drop_emp_married"));
		emp_qualification = PadQuotes(request.getParameter("txt_emp_qualification"));
		emp_certification = PadQuotes(request.getParameter("txt_emp_certification"));
		emp_phone1 = PadQuotes(request.getParameter("txt_emp_phone1"));
		emp_phone2 = PadQuotes(request.getParameter("txt_emp_phone2"));
		emp_mobile1 = PadQuotes(request.getParameter("txt_emp_mobile1"));
		emp_mobile2 = PadQuotes(request.getParameter("txt_emp_mobile2"));
		emp_email1 = PadQuotes(request.getParameter("txt_emp_email1"));
		emp_email2 = PadQuotes(request.getParameter("txt_emp_email2"));
		// emp_upass = PadQuotes(request.getParameter("txt_emp_upass"));
		emp_address = PadQuotes(request.getParameter("txt_emp_address"));
		emp_city = PadQuotes(request.getParameter("txt_emp_city"));
		emp_pin = PadQuotes(request.getParameter("txt_emp_pin"));
		emp_state = PadQuotes(request.getParameter("txt_emp_state"));
		emp_landmark = PadQuotes(request.getParameter("txt_emp_landmark"));
		emp_stock_ageing = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_stock_ageing")));
		emp_clicktocall = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_clicktocall")));
		emp_emaxpm = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_emaxpm")));
		emp_clicktocall_username = PadQuotes(request.getParameter("txt_emp_clicktocall_username"));
		// SOP("1----" + emp_clicktocall_username);
		emp_clicktocall_password = PadQuotes(request.getParameter("txt_emp_clicktocall_password"));
		emp_clicktocall_campaign = PadQuotes(request.getParameter("txt_emp_clicktocall_campaign"));
		emp_routeno = PadQuotes(request.getParameter("txt_emp_routeno"));
		emp_callerid = CNumeric(PadQuotes(request.getParameter("txt_emp_caller_id")));

		emp_device_id = PadQuotes(request.getParameter("txt_emp_device_id"));
		emp_ip_access = PadQuotes(request.getParameter("txt_emp_ip_access"));
		emp_notes = PadQuotes(request.getParameter("txt_emp_notes"));
		emp_access_web = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_web")));
		emp_access_app = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_app")));
		emp_active = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_active")));
		emp_branch_id = PadQuotes(request.getParameter("dr_emp_branch_id"));
		exe_branch_trans = request.getParameterValues("exe_branch_trans");
		emp_reason_of_leaving = PadQuotes(request.getParameter("txt_emp_reason_of_leaving"));
		emp_all_exe = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_all_exe")));
		emp_all_branches = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_all_branches")));
		emp_invoice_priceupdate = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_invoice_priceupdate")));
		emp_invoice_discountupdate = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_invoice_discountupdate")));

		emp_bill_priceupdate = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_bill_priceupdate")));
		emp_bill_discountupdate = CheckBoxValue(PadQuotes(request.getParameter("chk_emp_bill_discountupdate")));

		// ......eof payroll....
		// Admin
		emp_module_portal = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_portal")));
		emp_module_activity = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_activity")));
		emp_module_customer = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_customer")));
		emp_module_sales = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_sales")));
		emp_module_preowned = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_preowned")));
		emp_module_service = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_service")));
		emp_module_accessories = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_accessories")));
		emp_module_insurance = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_insurance")));
		emp_module_helpdesk = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_helpdesk")));
		emp_module_inventory = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_inventory")));
		emp_module_accounting = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_accounting")));
		emp_module_invoice = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_invoice")));
		emp_module_app = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_module_app")));
		emp_sales = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_sales")));
		emp_close_enquiry = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_close_enquiry")));
		emp_mtrboard = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_mtrboard")));
		emp_preowned = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_preowned")));
		emp_quote_priceupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_quote_priceupdate")));
		emp_quote_discountupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_quote_discountupdate")));
		emp_so_priceupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_so_priceupdate")));
		emp_so_discountupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_so_discountupdate")));
		// ...price
		emp_service = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_service")));
		emp_technician = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_technician")));
		emp_crm = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_crm")));
		emp_pickup_driver = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_pickup_driver")));
		emp_insur = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_insur")));
		emp_fieldinsur = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_fieldinsur")));
		emp_service_psf = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_service_psf")));
		emp_service_psf_iacs = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_service_psf_iacs")));
		emp_jc_priceupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_jc_priceupdate")));
		emp_jc_discountupdate = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_jc_discountupdate")));
		// ... price
		emp_ticket_owner = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_ticket_owner")));
		emp_ticket_close = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_ticket_close")));
		emp_mis_access = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_mis_access")));
		emp_export_access = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_export_access")));
		emp_report_access = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_report_access")));
		emp_copy_access = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_copy_access")));
		emp_dailystatus_report = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_dailystatus_report")));

		emp_priorityactivity_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityactivity_level1")));
		emp_priorityactivity_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityactivity_level2")));
		emp_priorityactivity_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityactivity_level3")));
		emp_priorityactivity_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityactivity_level4")));
		emp_priorityactivity_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityactivity_level5")));
		emp_priorityproject_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityproject_level1")));
		emp_priorityproject_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityproject_level2")));
		emp_priorityproject_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityproject_level3")));
		emp_priorityproject_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityproject_level4")));
		emp_priorityproject_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityproject_level5")));

		emp_prioritytask_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritytask_level1")));
		emp_prioritytask_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritytask_level2")));
		emp_prioritytask_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritytask_level3")));
		emp_prioritytask_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritytask_level4")));
		emp_prioritytask_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritytask_level5")));

		emp_priorityenquiryfollowup_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiryfollowup_level1")));
		emp_priorityenquiryfollowup_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiryfollowup_level2")));
		emp_priorityenquiryfollowup_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiryfollowup_level3")));
		emp_priorityenquiryfollowup_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiryfollowup_level4")));
		emp_priorityenquiryfollowup_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiryfollowup_level5")));

		emp_priorityenquiry_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiry_level1")));
		emp_priorityenquiry_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiry_level2")));
		emp_priorityenquiry_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiry_level3")));
		emp_priorityenquiry_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiry_level4")));
		emp_priorityenquiry_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityenquiry_level5")));

		emp_prioritycrmfollowup_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycrmfollowup_level1")));
		emp_prioritycrmfollowup_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycrmfollowup_level2")));
		emp_prioritycrmfollowup_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycrmfollowup_level3")));
		emp_prioritycrmfollowup_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycrmfollowup_level4")));
		emp_prioritycrmfollowup_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycrmfollowup_level5")));

		emp_prioritybalance_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritybalance_level1")));
		emp_prioritybalance_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritybalance_level2")));
		emp_prioritybalance_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritybalance_level3")));
		emp_prioritybalance_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritybalance_level4")));
		emp_prioritybalance_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritybalance_level5")));

		emp_prioritycall_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycall_level1")));
		emp_prioritycall_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycall_level2")));
		emp_prioritycall_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycall_level3")));
		emp_prioritycall_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycall_level4")));
		emp_prioritycall_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_prioritycall_level5")));

		emp_priorityjc_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityjc_level1")));
		emp_priorityjc_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityjc_level2")));
		emp_priorityjc_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityjc_level3")));
		emp_priorityjc_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityjc_level4")));
		emp_priorityjc_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityjc_level5")));

		emp_priorityticket_level1 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityticket_level1")));
		emp_priorityticket_level2 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityticket_level2")));
		emp_priorityticket_level3 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityticket_level3")));
		emp_priorityticket_level4 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityticket_level4")));
		emp_priorityticket_level5 = CheckBoxValue(PadQuotes(request.getParameter("ch_emp_priorityticket_level5")));

		emp_prevexp_year = PadQuotes(request.getParameter("dr_emp_prevexp_year"));
		emp_prevexp_month = PadQuotes(request.getParameter("dr_emp_prevexp_month"));
		date_of_join = PadQuotes(request.getParameter("txt_emp_date_of_join"));
		date_of_relieve = PadQuotes(request.getParameter("txt_emp_date_of_relieve"));
		exe_team_trans = request.getParameterValues("exe_team_trans");
		exe_soe_trans = request.getParameterValues("exe_soe_trans");
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			StrSql = "SELECT COUNT(emp_id) AS activeempcount" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " WHERE emp_active = '1'";

			active_empcount = Integer.parseInt(CNumeric(ExecuteQuery(StrSql)));
			if ("Add Executive".equals(addB)) {
				if (active_empcount > emp_count) {
					msg += "<br>Maximum Executive Count Reached!";
				}
			}
			if ("Update Executive".equals(updateB)) {
				if (active.equals("0") && emp_active.equals("1")
						&& emp_count < active_empcount) {
					msg += "<br>Maximum Executive Count Reached!";
				}
			}
			if (emp_id.equals("1")) {
				emp_role_id = "1";
				emp_branch_id = "0";
				emp_mis_access = "1";
				emp_export_access = "1";
				emp_report_access = "1";
				emp_access_web = "1";
				emp_access_app = "1";
				emp_active = "1";
			}
			if (emp_name.equals("")) {
				msg += "<br>Enter Name!";
			}

			if (emp_ref_no.equals("")) {
				msg += "<br>Enter Reference No.!";
			} else {
				StrSql = "SELECT emp_ref_no FROM " + compdb(comp_id)
						+ "axela_emp" + " WHERE emp_ref_no = '" + emp_ref_no
						+ "'";
				if (update.equals("yes") && !emp_ref_no.equals("")) {
					StrSql += " AND emp_id != " + emp_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Reference Number found! ";
				}
			}

			if (emp_role_id.equals("-1")) {
				msg += "<br>Select Role!";
			} else {
				if (emp_role_id.equals("1") && !emp_branch_id.equals("-1")
						&& !emp_branch_id.equals("0")) {
					msg += "<br>Select Head Office Branch for Administrator Role !";
				}
			}
			if (emp_department_id.equals("-1")) {
				msg += "<br>Select Department!";
			}
			if (emp_jobtitle_id.equals("-1")) {
				msg += "<br>Select Job Title!";
			}
			if (!drop_year.equals("-1") || !drop_month.equals("-1")
					|| !drop_day.equals("-1")) {
				if (drop_year.equals("-1")) {
					msg += "<br>DOB: Select Year!";
				}
				if (drop_month.equals("-1")) {
					msg += "<br>DOB: Select Month!";
				}
				if (drop_day.equals("-1")) {
					msg += "<br>DOB: Select Day!";
				}
			}
			if (emp_married.equals("-1")) {
				msg += "<br>Select Marital Status!";
			}
			if (emp_sex.equals("-1")) {
				msg += "<br>Select Gender!";
			}
			if (emp_address.equals("")) {
				msg += "<br>Enter Address!";
			}
			if (!emp_address.equals("")) {
				if (emp_city.equals("")) {
					msg += "<br>Enter City!";
				}
				if (emp_pin.equals("")) {
					msg += "<br>Enter Pin/Zip!";
				}
				if (!emp_pin.equals("") && !isNumeric(emp_pin)) {
					msg += "<br>Pin: Enter Numeric!";
				}
				if (emp_state.equals("")) {
					msg += "<br>Enter State!";
				}
			}
			if (emp_branch_id.equals("-1")) {
				msg += "<br>Select Branch!";
			}
			if (!emp_phone1.equals("")) {
				if (!IsValidPhoneNo11(emp_phone1)) {
					msg = msg + "<br>Enter valid Phone1!";
				}
			}
			if (!emp_phone2.equals("")) {
				if (!IsValidPhoneNo11(emp_phone2)) {
					msg = msg + "<br>Enter valid Phone2!";
				}
			}
			if (emp_mobile1.equals("91-")) {
				emp_mobile1 = "";
			}
			if (!emp_mobile1.equals("") && !IsValidMobileNo11(emp_mobile1)) {
				msg = msg + "<br>Mobile1 is Invalid!";
			}
			if (!emp_mobile2.equals("") && !IsValidMobileNo11(emp_mobile2)) {
				msg = msg + "<br>Mobile2 is Invalid!";
			}
			if (emp_email1.equals("")) {
				msg += "<br>Enter Email1!";
			}
			if (!emp_email1.equals("")) {
				if (IsValidEmail(emp_email1) == false) {
					msg = msg + "<br>Invalid Email1";
				} else {
					StrSql = "SELECT emp_email1 FROM " + compdb(comp_id)
							+ "axela_emp" + " WHERE" + " (emp_email1 = '"
							+ emp_email1 + "' OR emp_email2 = '" + emp_email1
							+ "')";
					if (update.equals("yes")) {
						StrSql += " AND emp_id !=" + emp_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Email1 Found!";
					}

					// //////for universe emp

					StrSql = "SELECT emp_email1 From " + maindb() + "uni_emp"
							+ " WHERE " + " emp_email1 = '" + emp_email1 + "'";
					if (update.equals("yes")) {
						StrSql += " AND emp_id !=" + emp_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg
								+ "<br> User is already present in Universal Database!";
					}
				}
			}

			if (!emp_email2.equals("")) {
				emp_email2 = emp_email2.toLowerCase();
			}
			if (!emp_email2.equals("")) {
				if (IsValidEmail(emp_email2) == false) {
					msg = msg + "<br>Invalid Email2";
				} else if (!emp_email1.equals("")
						&& emp_email1.equals(emp_email2)) {
					msg = msg + "<br>Email2 is same as Email1!";
				} else {
					StrSql = "SELECT emp_id FROM " + compdb(comp_id)
							+ "axela_emp" + " WHERE (emp_email1 = '"
							+ emp_email2 + "' OR emp_email2 = '" + emp_email2
							+ "')";
					if (update.equals("yes")) {
						StrSql += " AND emp_id!=" + emp_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Email2 Found";
					}
				}
			}
			// if (emp_upass.equals("")) {
			// msg = msg + "<br>Enter Password";
			// }
			// if (!emp_upass.equals("")) {
			// if (emp_upass.length() < 8) {
			// msg = msg + "<br>Enter Atleast 8 Characters for Password";
			// }
			//
			// }
			if (emp_clicktocall.equals("1") && emp_routeno.equals("")) {
				msg += "<br>Enter Route No.!";
			}
			// if (!emp_routeno.equals("")) {
			// ValidateRoute(emp_routeno);
			// }
			// if (!emp_callerid.equals("") && !IsValidMobileNo(emp_callerid)) {
			// msg = msg + "<br>Caller ID is Invalid!";
			// }
			String[] str = new String[10];
			if (!emp_ip_access.equals("")) {
				try {
					str = emp_ip_access.split(" ");
					for (int i = 0; i < str.length - 1; i++) {
						if (!str[i].endsWith(",")) {
							msg += "<br>Invalid IP access!";
						}
					}
					if (str[str.length - 1].contains(",")) {
						msg += "<br>Invalid IP access!";
					}
				} catch (Exception ex) {
					SOPError("Axelaauto==="
							+ this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
				}
			}
			if (date_of_join.equals("")) {
				msg = msg + "<br>Enter Join Date!";
			}
			if (!date_of_join.equals("")) {
				if (!isValidDateFormatShort(date_of_join)) {
					msg += "<br>Enter Valid Join Date!";
				} else {
					emp_date_of_join = ConvertShortDateToStr(date_of_join);
				}
			}
			if (!date_of_relieve.equals("")) {
				if (!isValidDateFormatShort(date_of_relieve)) {
					msg += "<br>Enter Valid Relieve Date!";
				} else {
					emp_date_of_relieve = ConvertShortDateToStr(date_of_relieve);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		if (emp_notes.length() > 8000) {
			emp_notes = emp_notes.substring(0, 7999);
		}

	}

	public void ValidateRoute(String routeno) {
		if ((routeno.length() != 11) || (routeno.charAt(0) != '0')) {
			msg += "<br>Invalid Route No.!";
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				emp_id = ExecuteQuery("SELECT COALESCE(MAX(emp_id), 0) + 1 AS emp_id FROM "
						+ compdb(comp_id) + "axela_emp");

				emp_id = "" + CheckCurrentId(Integer.parseInt(emp_id));

				emp_dob = drop_day + "/" + drop_month + "/" + drop_year;
				if ((drop_month.equals("-1")) && (drop_day.equals("-1"))
						&& (drop_year.equals("-1"))) {
					emp_dob = "";
				} else {
					emp_dob = ConvertShortDateToStr(emp_dob);
				}

				StrSql = "INSERT INTO "
						+ compdb(comp_id)
						+ "axela_emp"
						+ " (emp_id,"
						+ " emp_uuid,"
						+ " emp_name,"
						+ " emp_photo,"
						+ " emp_uname,"
						+ " emp_role_id,"
						+ " emp_department_id,"
						+ " emp_ref_no,"
						+ " emp_jobtitle_id,"
						+ " emp_sex,"
						+ " emp_dob,"
						+ " emp_married,"
						+ " emp_qualification,"
						+ " emp_certification,"
						+ " emp_phone1,"
						+ " emp_phone2,"
						+ " emp_mobile1,"
						+ " emp_mobile2,"
						+ " emp_email1,"
						+ " emp_email2,"
						+ " emp_upass,"
						+ " emp_address,"
						+ " emp_city,"
						+ " emp_pin,"
						+ " emp_state,"
						+ " emp_landmark,"
						+ " emp_access_web,"
						+ " emp_access_app,"
						+ " emp_active,"
						+ " emp_branch_id,"
						+ " emp_weeklyoff_id,"
						+ " emp_module_portal,"
						+ " emp_module_activity,"
						+ " emp_module_customer,"
						+ " emp_module_sales,"
						+ " emp_module_preowned,"
						+ " emp_module_service,"
						+ " emp_module_accessories,"
						+ " emp_module_insurance,"
						+ " emp_module_helpdesk,"
						+ " emp_module_inventory,"
						+ " emp_module_accounting,"
						+ " emp_module_invoice,"
						+ " emp_module_app,"
						+ " emp_sales,"
						+ " emp_close_enquiry,"
						+ " emp_mtrboard,"
						+ " emp_preowned,"
						+ " emp_quote_priceupdate,"
						+ " emp_quote_discountupdate,"
						+ " emp_so_priceupdate,"
						+ " emp_so_discountupdate,"
						+ " emp_invoice_priceupdate,"
						+ " emp_invoice_discountupdate,"
						+ " emp_bill_priceupdate,"
						+ " emp_bill_discountupdate,"
						// .... price
						+ " emp_service,"
						+ " emp_technician,"
						+ " emp_crm,"
						+ " emp_pickup_driver,"
						+ " emp_insur,"
						+ " emp_fieldinsur,"
						+ " emp_service_psf,"
						+ " emp_service_psf_iacs,"
						+ " emp_jc_priceupdate,"
						+ " emp_jc_discountupdate,"
						// ... price
						+ " emp_ticket_owner,"
						+ " emp_ticket_close,"
						+ " emp_mis_access,"
						+ " emp_export_access,"
						+ " emp_report_access,"
						+ " emp_copy_access, "
						+ " emp_dailystatus_report,"
						+ " emp_priorityactivity_level1,"
						+ " emp_priorityactivity_level2,"
						+ " emp_priorityactivity_level3,"
						+ " emp_priorityactivity_level4,"
						+ " emp_priorityactivity_level5,"
						+ " emp_priorityproject_level1,"
						+ " emp_priorityproject_level2,"
						+ " emp_priorityproject_level3,"
						+ " emp_priorityproject_level4,"
						+ " emp_priorityproject_level5,"
						+ " emp_prioritytask_level1,"
						+ " emp_prioritytask_level2,"
						+ " emp_prioritytask_level3,"
						+ " emp_prioritytask_level4,"
						+ " emp_prioritytask_level5,"
						+ " emp_priorityenquiryfollowup_level1,"
						+ " emp_priorityenquiryfollowup_level2,"
						+ " emp_priorityenquiryfollowup_level3,"
						+ " emp_priorityenquiryfollowup_level4,"
						+ " emp_priorityenquiryfollowup_level5,"
						+ " emp_priorityenquiry_level1,"
						+ " emp_priorityenquiry_level2,"
						+ " emp_priorityenquiry_level3,"
						+ " emp_priorityenquiry_level4,"
						+ " emp_priorityenquiry_level5,"
						+ " emp_prioritycrmfollowup_level1,"
						+ " emp_prioritycrmfollowup_level2,"
						+ " emp_prioritycrmfollowup_level3,"
						+ " emp_prioritycrmfollowup_level4,"
						+ " emp_prioritycrmfollowup_level5,"
						+ " emp_prioritybalance_level1,"
						+ " emp_prioritybalance_level2,"
						+ " emp_prioritybalance_level3,"
						+ " emp_prioritybalance_level4,"
						+ " emp_prioritybalance_level5,"
						+ " emp_prioritycall_level1,"
						+ " emp_prioritycall_level2,"
						+ " emp_prioritycall_level3,"
						+ " emp_prioritycall_level4,"
						+ " emp_prioritycall_level5,"
						+ " emp_priorityjc_level1,"
						+ " emp_priorityjc_level2,"
						+ " emp_priorityjc_level3,"
						+ " emp_priorityjc_level4,"
						+ " emp_priorityjc_level5,"
						+ " emp_priorityticket_level1,"
						+ " emp_priorityticket_level2,"
						+ " emp_priorityticket_level3,"
						+ " emp_priorityticket_level4,"
						+ " emp_priorityticket_level5,"
						+ " emp_stock_ageing,"
						+ " emp_clicktocall,"
						+ " emp_emaxpm,"
						+ " emp_clicktocall_username,"
						+ " emp_clicktocall_password,"
						+ " emp_clicktocall_campaign,"
						+ " emp_routeno,"
						+ " emp_callerid,"
						+ " emp_device_id,"
						+ " emp_ip_access,"
						// .... payroll....
						+ " emp_all_exe, "
						// ... eof payroll..
						+ " emp_all_branches, "
						+ " emp_prevexp,"
						+ " emp_date_of_join,"
						+ " emp_date_of_relieve,"
						+ " emp_reason_of_leaving,"
						+ " emp_notes,"
						+ " emp_recperpage,"
						+ " emp_timeout,"
						+ " emp_theme_id,"
						+ " emp_entry_id,"
						+ " emp_entry_date,"
						+ " emp_modified_id,"
						+ " emp_modified_date)"
						+ " VALUES"
						+ " ('" + emp_id + "',"
						+ " UUID()," //
						+ " '" + emp_name + "'," // emp_name
						+ " ''," // emp_photo
						+ " '" + emp_uname + "'," // emp_uname
						+ " '" + emp_role_id + "',"
						+ " '" + emp_department_id + "',"
						+ " '" + emp_ref_no + "',"
						+ " '" + emp_jobtitle_id + "',"
						+ " '" + emp_sex + "',"
						+ " '" + emp_dob + "',"
						+ " '" + emp_married + "',"
						+ " '" + emp_qualification + "',"
						+ " '" + emp_certification + "',"
						+ " '" + emp_phone1 + "',"
						+ " '" + emp_phone2 + "',"
						+ " '" + emp_mobile1 + "',"
						+ " '" + emp_mobile2 + "',"
						+ " '" + emp_email1 + "',"
						+ " '" + emp_email2 + "',"
						+ " '" + GenPass(8) + "',"
						// + " '" + emp_upass "',"
						+ " '" + emp_address + "',"
						+ " '" + emp_city + "',"
						+ " '" + emp_pin + "',"
						+ " '" + emp_state + "',"
						+ " '" + emp_landmark + "',"
						+ " '" + emp_access_web + "',"
						+ " '" + emp_access_app + "',"
						+ " '" + emp_active + "',"
						+ " '" + emp_branch_id + "',"
						+ " '" + emp_weeklyoff_id + "',"
						+ " '" + emp_module_portal + "', "
						+ " '" + emp_module_activity + "', "
						+ " '" + emp_module_customer + "', "
						+ " '" + emp_module_sales + "', "
						+ " '" + emp_module_preowned + "', "
						+ " '" + emp_module_service + "', "
						+ " '" + emp_module_accessories + "', "
						+ " '" + emp_module_insurance + "', "
						+ " '" + emp_module_helpdesk + "', "
						+ " '" + emp_module_inventory + "', "
						+ " '" + emp_module_accounting + "', "
						+ " '" + emp_module_invoice + "', "
						+ " '" + emp_module_app + "', "
						+ " '" + emp_sales + "', "
						+ " '" + emp_close_enquiry + "', "
						+ " '" + emp_mtrboard + "', "
						+ " '" + emp_preowned + "',"
						+ " '" + emp_quote_priceupdate + "', "
						+ " '" + emp_quote_discountupdate + "', "
						+ " '" + emp_so_priceupdate + "', "
						+ " '" + emp_so_discountupdate + "', "
						+ " '" + emp_invoice_priceupdate + "', "
						+ " '" + emp_invoice_discountupdate + "', "
						+ " '" + emp_bill_priceupdate + "', "
						+ " '" + emp_bill_discountupdate + "', "
						// .... price
						+ " '" + emp_service + "', "
						+ " '" + emp_technician + "', "
						+ " '" + emp_crm + "', "
						+ " '" + emp_pickup_driver + "', "
						+ " '" + emp_insur + "', "
						+ " '" + emp_fieldinsur + "', "
						+ " '" + emp_service_psf + "', "
						+ " '" + emp_service_psf_iacs + "', "
						+ " '" + emp_jc_priceupdate + "', "
						+ " '" + emp_jc_discountupdate + "', "
						// .... price
						+ " '" + emp_ticket_owner + "', " + " '"
						+ emp_ticket_close + "', " + " '" + emp_mis_access
						+ "', " + " '" + emp_export_access + "', " + " '"
						+ emp_report_access + "', " + " '"
						+ emp_copy_access + "', " + " '"
						+ emp_dailystatus_report + "', " + " '"
						+ emp_priorityactivity_level1 + "'," + " '"
						+ emp_priorityactivity_level2 + "'," + " '"
						+ emp_priorityactivity_level3 + "'," + " '"
						+ emp_priorityactivity_level4 + "'," + " '"
						+ emp_priorityactivity_level5 + "'," + " '"
						+ emp_priorityproject_level1 + "'," + " '"
						+ emp_priorityproject_level2 + "'," + " '"
						+ emp_priorityproject_level3 + "'," + " '"
						+ emp_priorityproject_level4 + "'," + " '"
						+ emp_priorityproject_level5 + "'," + " '"
						+ emp_prioritytask_level1 + "'," + " '"
						+ emp_prioritytask_level2 + "'," + " '"
						+ emp_prioritytask_level3 + "'," + " '"
						+ emp_prioritytask_level4 + "'," + " '"
						+ emp_prioritytask_level5 + "'," + " '"
						+ emp_priorityenquiryfollowup_level1 + "'," + " '"
						+ emp_priorityenquiryfollowup_level2 + "'," + " '"
						+ emp_priorityenquiryfollowup_level3 + "'," + " '"
						+ emp_priorityenquiryfollowup_level4 + "'," + " '"
						+ emp_priorityenquiryfollowup_level5 + "'," + " '"
						+ emp_priorityenquiry_level1 + "'," + " '"
						+ emp_priorityenquiry_level2 + "'," + " '"
						+ emp_priorityenquiry_level3 + "'," + " '"
						+ emp_priorityenquiry_level4 + "'," + " '"
						+ emp_priorityenquiry_level5 + "'," + " '"
						+ emp_prioritycrmfollowup_level1 + "'," + " '"
						+ emp_prioritycrmfollowup_level2 + "'," + " '"
						+ emp_prioritycrmfollowup_level3 + "'," + " '"
						+ emp_prioritycrmfollowup_level4 + "'," + " '"
						+ emp_prioritycrmfollowup_level5 + "'," + " '"
						+ emp_prioritybalance_level1 + "'," + " '"
						+ emp_prioritybalance_level2 + "'," + " '"
						+ emp_prioritybalance_level3 + "'," + " '"
						+ emp_prioritybalance_level4 + "'," + " '"
						+ emp_prioritybalance_level5 + "'," + " '"
						+ emp_prioritycall_level1 + "'," + " '"
						+ emp_prioritycall_level2 + "'," + " '"
						+ emp_prioritycall_level3 + "'," + " '"
						+ emp_prioritycall_level4 + "'," + " '"
						+ emp_prioritycall_level5 + "'," + " '"
						+ emp_priorityjc_level1 + "'," + " '"
						+ emp_priorityjc_level2 + "'," + " '"
						+ emp_priorityjc_level3 + "'," + " '"
						+ emp_priorityjc_level4 + "'," + " '"
						+ emp_priorityjc_level5 + "'," + " '"
						+ emp_priorityticket_level1 + "'," + " '"
						+ emp_priorityticket_level2 + "'," + " '"
						+ emp_priorityticket_level3 + "'," + " '"
						+ emp_priorityticket_level4 + "'," + " '"
						+ emp_priorityticket_level5 + "',"
						+ " '" + emp_stock_ageing + "',"
						+ " '" + emp_clicktocall + "',"
						+ " '" + emp_emaxpm + "',"
						+ " '" + emp_clicktocall_username + "' ,"
						+ " '" + emp_clicktocall_password + "' ,"
						+ " '" + emp_clicktocall_campaign + "' ,"
						+ " '" + emp_routeno + "',"
						+ " '" + emp_callerid + "' ,"
						+ " '" + emp_device_id + "',"
						+ " '" + emp_ip_access + "',"
						// .... payroll....
						+ " " + emp_all_exe + ","
						// ....eof payroll....
						+ " " + emp_all_branches + ","
						+ " '" + emp_prevexp_year + "," + emp_prevexp_month + "'," // emp_prevexp
						+ " '" + emp_date_of_join + "'," + " '"
						+ emp_date_of_relieve + "'," + " '"
						+ emp_reason_of_leaving + "'," + " '" + emp_notes
						+ "'," + " 10,"
						+ " 20," + " " + emp_theme_id + "," + " "
						+ emp_entry_id + "," + " '" + emp_entry_date + "',"
						+ " '0'," + " '')";
				SOP("StrSq===" + StrSql);
				stmttx.addBatch(StrSql);
				UpdateList();
				// Update Executive SOE
				UpdateSoe();
				UpdateBranchList();
				stmttx.executeBatch();
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOPError("connection rollback...\n sql--"
							+ StrSql);
				}
				msg = msg + "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
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
	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				emp_dob = drop_day + "/" + drop_month + "/" + drop_year;
				if ((drop_month.equals("-1")) && (drop_day.equals("-1"))
						&& (drop_year.equals("-1"))) {
					emp_dob = "";
				} else {
					emp_dob = ConvertShortDateToStr(emp_dob);
				}

				StrSql = "UPDATE "
						+ compdb(comp_id)
						+ "axela_emp"
						+ " SET"
						+ " emp_name = '" + emp_name + "',"
						+ " emp_uname = '" + emp_uname + "',"
						+ " emp_department_id = " + emp_department_id + ","
						+ " emp_ref_no = '" + emp_ref_no + "',"
						+ " emp_jobtitle_id = " + emp_jobtitle_id + ","
						+ " emp_sex = '" + emp_sex + "',"
						+ " emp_dob = '" + emp_dob + "',"
						+ " emp_married = '" + emp_married + "',"
						+ " emp_qualification = '" + emp_qualification + "',"
						+ " emp_certification = '" + emp_certification + "',"
						+ " emp_phone1 = '" + emp_phone1 + "',"
						+ " emp_phone2 = '" + emp_phone2 + "',"
						+ " emp_mobile1 = '" + emp_mobile1 + "',"
						+ " emp_mobile2 = '" + emp_mobile2 + "',"
						+ " emp_email1 = '" + emp_email1 + "',"
						+ " emp_email2 = '" + emp_email2 + "',"
						+ " emp_address = '" + emp_address + "',"
						+ " emp_city = '" + emp_city + "',"
						+ " emp_pin = '" + emp_pin + "',"
						+ " emp_state = '" + emp_state + "',"
						+ " emp_landmark = '" + emp_landmark + "',"
						+ " emp_stock_ageing = '" + emp_stock_ageing + "',"
						+ " emp_clicktocall = '" + emp_clicktocall + "',"
						+ " emp_emaxpm = '" + emp_emaxpm + "',"
						+ " emp_clicktocall_username = '" + emp_clicktocall_username + "' ,"
						+ " emp_clicktocall_password = '" + emp_clicktocall_password + "' ,"
						+ " emp_clicktocall_campaign = '" + emp_clicktocall_campaign + "' ,"
						+ " emp_routeno = '" + emp_routeno + "',"
						+ " emp_callerid = '" + emp_callerid + "' ,"
						+ " emp_device_id = '" + emp_device_id + "',"
						+ " emp_ip_access = '" + emp_ip_access + "',"
						+ " emp_notes = '" + emp_notes + "',"
						+ " emp_access_web = '" + emp_access_web + "',"
						+ " emp_access_app = '" + emp_access_app + "',"
						+ " emp_active = '" + emp_active + "',"
						+ " emp_branch_id = '" + emp_branch_id + "',"
						+ " emp_weeklyoff_id = '" + emp_weeklyoff_id + "',"
						// + "emp_bill_discountupdate = '" + emp_sales + "', "
						+ " emp_module_portal = '" + emp_module_portal + "',"
						+ " emp_module_activity = '" + emp_module_activity + "',"
						+ " emp_module_customer = '" + emp_module_customer + "',"
						+ " emp_module_sales = '" + emp_module_sales + "',"
						+ " emp_module_preowned = '" + emp_module_preowned + "',"
						+ " emp_module_service = '" + emp_module_service + "',"
						+ " emp_module_accessories = '" + emp_module_accessories + "',"
						+ " emp_module_insurance = '" + emp_module_insurance + "',"
						+ " emp_module_helpdesk = '" + emp_module_helpdesk + "',"
						+ " emp_module_inventory = '" + emp_module_inventory + "',"
						+ " emp_module_accounting = '" + emp_module_accounting + "',"
						+ " emp_module_invoice = '" + emp_module_invoice + "',"
						+ " emp_module_app = '" + emp_module_app + "',"
						+ " emp_sales = '" + emp_sales + "',"
						+ " emp_close_enquiry = '" + emp_close_enquiry + "',"
						+ " emp_mtrboard = '" + emp_mtrboard + "',"
						+ " emp_preowned ='" + emp_preowned + "',"
						+ " emp_quote_priceupdate = '" + emp_quote_priceupdate + "',"
						+ " emp_quote_discountupdate = '" + emp_quote_discountupdate + "',"
						+ " emp_so_priceupdate = '" + emp_so_priceupdate + "',"
						+ " emp_so_discountupdate = '" + emp_so_discountupdate + "',"
						+ " emp_invoice_priceupdate = '" + emp_invoice_priceupdate + "',"
						+ " emp_invoice_discountupdate = '" + emp_invoice_discountupdate + "',"
						+ " emp_bill_priceupdate = '" + emp_bill_priceupdate + "',"
						+ " emp_bill_discountupdate = '" + emp_bill_discountupdate + "',"
						// ...
						+ " emp_service = '" + emp_service + "',"
						+ " emp_technician = '" + emp_technician + "',"
						+ " emp_crm = '" + emp_crm + "',"
						+ " emp_pickup_driver = '" + emp_pickup_driver + "',"
						+ " emp_insur = '" + emp_insur + "',"
						+ " emp_fieldinsur = '" + emp_fieldinsur + "',"
						+ " emp_service_psf = '" + emp_service_psf + "',"
						+ " emp_service_psf_iacs = '" + emp_service_psf_iacs + "',"
						+ " emp_jc_priceupdate = '" + emp_jc_priceupdate + "',"
						+ " emp_jc_discountupdate = '" + emp_jc_discountupdate + "',"
						// ...
						+ " emp_ticket_owner = '" + emp_ticket_owner + "',"
						+ " emp_ticket_close = '" + emp_ticket_close + "',"
						+ " emp_mis_access = '" + emp_mis_access + "',"
						+ " emp_export_access = '" + emp_export_access + "',"
						+ " emp_report_access = '" + emp_report_access + "',"
						+ " emp_copy_access = '" + emp_copy_access + "',"
						+ " emp_dailystatus_report = '" + emp_dailystatus_report + "',"
						+ " emp_priorityactivity_level1 = '" + emp_priorityactivity_level1 + "',"
						+ " emp_priorityactivity_level2 = '" + emp_priorityactivity_level2 + "',"
						+ " emp_priorityactivity_level3 = '" + emp_priorityactivity_level3 + "',"
						+ " emp_priorityactivity_level4 = '" + emp_priorityactivity_level4 + "',"
						+ " emp_priorityactivity_level5 = '" + emp_priorityactivity_level5 + "',"
						+ " emp_priorityproject_level1 = '" + emp_priorityproject_level1 + "',"
						+ " emp_priorityproject_level2 = '" + emp_priorityproject_level2 + "',"
						+ " emp_priorityproject_level3 = '" + emp_priorityproject_level3 + "',"
						+ " emp_priorityproject_level4 = '" + emp_priorityproject_level4 + "',"
						+ " emp_priorityproject_level5 = '" + emp_priorityproject_level5 + "',"
						+ " emp_prioritytask_level1 = '" + emp_prioritytask_level1 + "',"
						+ " emp_prioritytask_level2 = '" + emp_prioritytask_level2 + "',"
						+ " emp_prioritytask_level3 = '" + emp_prioritytask_level3 + "',"
						+ " emp_prioritytask_level4 = '" + emp_prioritytask_level4 + "',"
						+ " emp_prioritytask_level5 = '" + emp_prioritytask_level5 + "',"
						+ " emp_priorityenquiryfollowup_level1 = '" + emp_priorityenquiryfollowup_level1 + "',"
						+ " emp_priorityenquiryfollowup_level2 = '" + emp_priorityenquiryfollowup_level2 + "',"
						+ " emp_priorityenquiryfollowup_level3 = '" + emp_priorityenquiryfollowup_level3 + "',"
						+ " emp_priorityenquiryfollowup_level4 = '" + emp_priorityenquiryfollowup_level4 + "',"
						+ " emp_priorityenquiryfollowup_level5 = '" + emp_priorityenquiryfollowup_level5 + "',"
						+ " emp_priorityenquiry_level1 = '" + emp_priorityenquiry_level1 + "',"
						+ " emp_priorityenquiry_level2 = '" + emp_priorityenquiry_level2 + "',"
						+ " emp_priorityenquiry_level3 = '" + emp_priorityenquiry_level3 + "',"
						+ " emp_priorityenquiry_level4 = '" + emp_priorityenquiry_level4 + "',"
						+ " emp_priorityenquiry_level5 = '" + emp_priorityenquiry_level5 + "',"
						+ " emp_prioritycrmfollowup_level1 = '" + emp_prioritycrmfollowup_level1 + "',"
						+ " emp_prioritycrmfollowup_level2 = '" + emp_prioritycrmfollowup_level2 + "',"
						+ " emp_prioritycrmfollowup_level3 = '" + emp_prioritycrmfollowup_level3 + "',"
						+ " emp_prioritycrmfollowup_level4 = '" + emp_prioritycrmfollowup_level4 + "',"
						+ " emp_prioritycrmfollowup_level5 = '" + emp_prioritycrmfollowup_level5 + "',"
						+ " emp_prioritybalance_level1 = '" + emp_prioritybalance_level1 + "',"
						+ " emp_prioritybalance_level2 = '" + emp_prioritybalance_level2 + "',"
						+ " emp_prioritybalance_level3 = '" + emp_prioritybalance_level3 + "',"
						+ " emp_prioritybalance_level4 = '" + emp_prioritybalance_level4 + "',"
						+ " emp_prioritybalance_level5 = '" + emp_prioritybalance_level5 + "',"
						+ " emp_prioritycall_level1 = '" + emp_prioritycall_level1 + "',"
						+ " emp_prioritycall_level2 = '" + emp_prioritycall_level2 + "',"
						+ " emp_prioritycall_level3 = '" + emp_prioritycall_level3 + "',"
						+ " emp_prioritycall_level4 = '" + emp_prioritycall_level4 + "',"
						+ " emp_prioritycall_level5 = '" + emp_prioritycall_level5 + "',"
						+ " emp_priorityjc_level1 = '" + emp_priorityjc_level1 + "',"
						+ " emp_priorityjc_level2 = '" + emp_priorityjc_level2 + "',"
						+ " emp_priorityjc_level3 = '" + emp_priorityjc_level3 + "',"
						+ " emp_priorityjc_level4 = '" + emp_priorityjc_level4 + "',"
						+ " emp_priorityjc_level5 = '" + emp_priorityjc_level5 + "',"
						+ " emp_priorityticket_level1 = '" + emp_priorityticket_level1 + "',"
						+ " emp_priorityticket_level2 = '" + emp_priorityticket_level2 + "',"
						+ " emp_priorityticket_level3 = '" + emp_priorityticket_level3 + "',"
						+ " emp_priorityticket_level4 = '" + emp_priorityticket_level4 + "',"
						+ " emp_priorityticket_level5 = '" + emp_priorityticket_level5 + "', "
						// ....payroll....
						+ " emp_all_exe = " + emp_all_exe + ","
						// ... eof payroll...
						+ " emp_all_branches = '" + emp_all_branches + "',"
						+ " emp_prevexp = '" + emp_prevexp_year + "," + emp_prevexp_month + "'," // emp_prevexp
						+ " emp_date_of_join = '" + emp_date_of_join + "',"
						+ " emp_date_of_relieve = '" + emp_date_of_relieve + "',"
						+ " emp_reason_of_leaving = '" + emp_reason_of_leaving + "',"
						+ " emp_role_id = '" + emp_role_id + "',"
						+ " emp_modified_id = " + emp_modified_id + ","
						+ " emp_modified_date = '" + emp_modified_date + "'"
						+ " WHERE emp_id = " + emp_id + "";
				SOP("strsql===" + StrSql);
				UpdateList();
				// Update Executive SOE
				UpdateSoe();
				UpdateBranchList();
				stmttx.addBatch(StrSql);
				stmttx.executeBatch();
				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {

				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();

				}
				msg = msg + "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
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

	protected void DeleteFields() {

		if (emp_id.equals("1")) {
			msg = msg + "<br>Cannot Delete Administrator!";
		}
		// check for Activity
		StrSql = "SELECT activity_emp_id FROM " + compdb(comp_id)
				+ "axela_activity" + " WHERE activity_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Activity!";
		}
		// check for customer
		StrSql = "SELECT customer_emp_id FROM " + compdb(comp_id)
				+ "axela_customer" + " WHERE customer_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Customer!";
		}
		// check for sales
		StrSql = "SELECT voucher_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_emp_id = " + emp_id + ""
				+ " AND voucher_vouchertype_id = 6";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Invoice!";
		}

		StrSql = "SELECT lead_emp_id FROM " + compdb(comp_id)
				+ "axela_sales_lead" + " WHERE lead_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Lead!";
		}

		StrSql = "SELECT enquiry_emp_id FROM " + compdb(comp_id)
				+ "axela_sales_enquiry" + " WHERE enquiry_emp_id = " + emp_id
				+ "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Enquiry!";
		}

		StrSql = "SELECT testdrive_emp_id FROM " + compdb(comp_id)
				+ "axela_sales_testdrive" + " WHERE testdrive_emp_id = "
				+ emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Test Drive!";
		}

		StrSql = "SELECT quote_emp_id FROM " + compdb(comp_id)
				+ "axela_sales_quote" + " WHERE quote_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Quote!";
		}

		StrSql = "SELECT voucher_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_emp_id = " + emp_id + ""
				+ " AND voucher_vouchertype_id = 9";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Receipt!";
		}

		StrSql = "SELECT so_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Sales Order!";
		}

		StrSql = "SELECT target_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " WHERE target_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Target!";
		}

		StrSql = "SELECT team_emp_id FROM " + compdb(comp_id)
				+ "axela_sales_team" + " WHERE team_emp_id = " + emp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Team!";
		}
		StrSql = "SELECT voucher_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_emp_id = " + emp_id + ""
				+ " AND voucher_vouchertype_id = 15";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Invoice Payment!";
		}
		// check for Inventory
		StrSql = "SELECT voucher_emp_id FROM "
				+ compdb(comp_id) + "axela_acc_voucher WHERE"
				+ " voucher_vouchertype_id = 12"
				+ " AND voucher_emp_id = " + emp_id + "";

		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Employee is associated with Purchase Order!";
		}

		if (msg.equals("")) {
			try {

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + maindb() + "uni_emp"
						+ " WHERE emp_id = " + emp_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	public void UpdateList() throws SQLException {
		if (msg.equals("")) {
			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id)
						+ "axela_emp_exe" + " WHERE empexe_emp_id = " + emp_id);
				if (emp_all_exe.equals("0")) {
					if (exe_team_trans != null) {
						for (int i = 0; i < exe_team_trans.length; i++) {
							stmttx.addBatch("INSERT INTO " + compdb(comp_id)
									+ "axela_emp_exe" + " (empexe_emp_id,"
									+ " empexe_id)" + " VALUES" + " (" + emp_id
									+ "," + " " + exe_team_trans[i] + ")");
						}
					}
				}
			} catch (Exception e) {
				conntx.rollback();
				msg += "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			}
		}
	}

	public void UpdateSoe() throws SQLException {
		if (msg.equals("")) {
			try {
				stmttx.addBatch("Delete from " + compdb(comp_id)
						+ "axela_emp_soe" + " where empsoe_emp_id = " + emp_id);
				if (exe_soe_trans != null) {
					for (int i = 0; i < exe_soe_trans.length; i++) {
						stmttx.addBatch("Insert into " + compdb(comp_id)
								+ "axela_emp_soe" + " (empsoe_emp_id,"
								+ " empsoe_soe_id)" + " values" + " (" + emp_id
								+ "," + " " + exe_soe_trans[i] + ")");
					}
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			}
		}
	}

	void UpdateBranchList() throws SQLException {
		if (msg.equals("")) {
			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id)
						+ "axela_emp_branch" + " WHERE emp_id = " + emp_id);
				if (emp_all_branches.equals("0")) {
					if (exe_branch_trans != null && !emp_role_id.equals("1")
							&& emp_branch_id.equals("0")) {
						for (int i = 0; i < exe_branch_trans.length; i++) {
							stmttx.addBatch("INSERT INTO " + compdb(comp_id)
									+ "axela_emp_branch" + " (emp_id,"
									+ " emp_branch_id)" + " VALUES" + " (" + emp_id
									+ "," + " " + exe_branch_trans[i] + ")");
						}
					}
				}
			} catch (Exception e) {
				conntx.rollback();
				msg = msg + "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			}
		}
	}
	protected void PopulateFields() {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_id = crs.getString("emp_id");

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
				// emp_upass = crs.getString("emp_upass");
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
				emp_bill_priceupdate = crs.getString("emp_bill_priceupdate");
				emp_bill_discountupdate = crs.getString("emp_bill_discountupdate");
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
				Str.append("<option value=")
						.append(crs.getString("jobtitle_id"));
				Str.append(StrSelectdrop(crs.getString("jobtitle_id"),
						emp_jobtitle_id));
				Str.append(">").append(crs.getString("jobtitle_desc"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code" + " FROM "
					+ compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = -1> Select </option>\n");
			Str.append("<option value =0 ").append(
					StrSelectdrop("0", emp_branch_id));
			Str.append(">Head Office</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"),
						emp_branch_id));
				Str.append(">").append(crs.getString("branch_name"))
						.append(" (");
				Str.append(crs.getString("branch_code"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
			// SOP("PopulateBranchs======" + StrSqlBreaker(StrSql));
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(">").append(crs.getString("branch_name"))
						.append(" (");
				Str.append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}

	}

	public String PopulateBranchTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_name, branch_id, branch_code" + " FROM "
					+ compdb(comp_id) + "axela_branch" + " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_emp_branch ON branch_id = emp_branch_id"
					+ " WHERE emp_id = " + emp_id + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";

			if ((add.equals("yes") || updateB.equals("yes"))
					&& exe_branch_trans != null) {
				StrSql = "SELECT branch_name, branch_id, branch_code"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " ORDER BY branch_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes"))
						&& exe_branch_trans != null) {
					for (int i = 0; i < exe_branch_trans.length; i++) {
						if (crs.getString("branch_id").equals(
								exe_branch_trans[i])) {
							Str.append("<option value=").append(
									crs.getString("branch_id"));
							Str.append(" selected>").append(
									crs.getString("branch_name"));
							Str.append(" (")
									.append(crs.getString("branch_code"));
							Str.append(")</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {
					Str.append("<option value=").append(
							crs.getString("branch_id"));
					Str.append(" selected>")
							.append(crs.getString("branch_name"));
					Str.append(" (").append(crs.getString("branch_code"));
					Str.append(")</option>\n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " WHERE emp_active = '1'"
					+ " AND emp_id != " + emp_id + "";
			// if (!emp_branch_id.equals("0")) {
			// StrSql += " AND emp_branch_id = " + emp_branch_id + "";
			// }
			StrSql += " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"))
						.append(">");
				Str.append(crs.getString("emp_name")).append(" (")
						.append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_emp_exe AS ex ON ex.empexe_id = emp_id"
					+ " WHERE ex.empexe_emp_id = '" + emp_id + "'" + " AND "
					+ compdb(comp_id) + "axela_emp.emp_active = '1'"
					+ " ORDER BY " + compdb(comp_id) + "axela_emp.emp_name";

			if ((add.equals("yes") || updateB.equals("yes"))
					&& exe_team_trans != null) {
				StrSql = "SELECT emp_id, emp_name, emp_ref_no" + " FROM "
						+ compdb(comp_id) + "axela_emp"
						+ " WHERE emp_active = 1" + " ORDER BY emp_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes"))
						&& exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_trans[i])) {
							Str.append("<option value=").append(
									crs.getString("emp_id"));
							Str.append(" selected>").append(
									crs.getString("emp_name"));
							Str.append(" (").append(crs.getString("emp_ref_no"))
									.append(")</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {

					Str.append("<option value=").append(crs.getString("emp_id"));
					Str.append(" selected>").append(crs.getString("emp_name"));
					Str.append(" (").append(crs.getString("emp_ref_no"));
					Str.append(")</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select soe_id, soe_name " + " from " + compdb(comp_id)
					+ "axela_soe " + " where 1 = 1 " + " group by soe_id"
					+ " order by soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"))
						.append(">");
				Str.append(crs.getString("soe_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSoeTrans(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name" + " from " + compdb(comp_id)
					+ "axela_soe" + " inner join " + compdb(comp_id)
					+ "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " where empsoe_emp_id = " + emp_id + ""
					+ " order by soe_name";

			if ((add.equals("yes") || updateB.equals("yes"))
					&& exe_soe_trans != null) {
				StrSql = "Select soe_id, soe_name" + " from " + compdb(comp_id)
						+ "axela_soe" + " WHERE 1=1" + " order by soe_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("yes"))
						&& exe_soe_trans != null) {
					for (int i = 0; i < exe_soe_trans.length; i++) {
						if (crs.getString("soe_id").equals(exe_soe_trans[i])) {
							Str.append("<option value=").append(
									crs.getString("soe_id"));
							Str.append(" selected>").append(
									crs.getString("soe_name"));
							Str.append("</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("yes")) {
					Str.append("<option value=").append(crs.getString("soe_id"));
					Str.append(" selected>").append(crs.getString("soe_name"));
					Str.append("</option> \n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
				Str.append("<option value=").append(
						crs.getString("department_id"));
				Str.append(StrSelectdrop(crs.getString("department_id"),
						emp_department_id));
				Str.append(">").append(crs.getString("department_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSex() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>\n");
		Str.append("<option value = 1").append(Selectdrop(1, emp_sex))
				.append(">Male</option>\n)");
		Str.append("<option value = 0").append(Selectdrop(0, emp_sex))
				.append(">Female</option>\n");
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
			Str.append("<option value = ").append(i).append("")
					.append(Selectdrop(i, drop_day));
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

	// public void UpdateUniversalEmp(String emp_id, String comp_id) {
	// StrSql = " SELECT emp_id" + " FROM " + maindb() + "uni_emp "
	// + " WHERE " + " emp_id = " + emp_id + " AND comp_id = "
	// + comp_id;
	//
	// if (!ExecuteQuery(StrSql).equals("")) {
	// StrSql = " UPDATE " + maindb() + "uni_emp uniemp" + " INNER JOIN "
	// + compdb(comp_id)
	// + "axela_emp emp ON emp.emp_id = uniemp.emp_id" + " SET "
	// + " uniemp.emp_name = emp.emp_name,"
	// + " uniemp.emp_uuid = emp.emp_uuid,"
	// + " uniemp.emp_role_id = emp.emp_role_id,"
	// + " uniemp.emp_sales =  emp.emp_sales,"
	// + " uniemp.emp_email1 = emp.emp_email1,"
	// + " uniemp.emp_branch_id = emp.emp_branch_id,"
	// + " uniemp.emp_active = emp.emp_active,"
	// + " uniemp.emp_all_exe = emp.emp_all_exe,"
	// + " uniemp.emp_device_id = emp.emp_device_id,"
	// + " uniemp.emp_ip_access = emp.emp_ip_access,"
	// + " uniemp.emp_recperpage = emp.emp_recperpage"
	// + " WHERE uniemp.emp_id = " + emp_id + ""
	// + " AND uniemp.comp_id = " + comp_id + "";
	//
	// updateQuery(StrSql);
	// } else {
	// StrSql = "SELECT " + " emp_id," + comp_id + "," + " emp_uuid,"
	// + " emp_name, " + " emp_role_id, " + " emp_sales, "
	// + " emp_upass, " + " emp_email1, " + " emp_branch_id, "
	// + " emp_active, " + " emp_all_exe, " + " emp_device_id, "
	// + " emp_ip_access, " + " emp_recperpage, "
	// + " emp_entry_id, " + " emp_entry_date, "
	// + " emp_modified_id, " + " emp_modified_date " + " FROM "
	// + compdb(comp_id) + " axela_emp " + " WHERE "
	// + " emp_id = " + emp_id;
	//
	// StrSql = " INSERT INTO " + maindb() + "uni_emp " + "( emp_id,"
	// + " comp_id," + " emp_uuid," + " emp_name,"
	// + " emp_role_id, " + " emp_sales, " + " emp_upass, "
	// + " emp_email1, " + " emp_branch_id, " + " emp_active,"
	// + " emp_all_exe," + " emp_device_id," + " emp_ip_access,"
	// + " emp_recperpage," + " emp_entry_id,"
	// + " emp_entry_date," + " emp_modified_id,"
	// + " emp_modified_date)" + " (" + StrSql + " )";
	// updateQuery(StrSql);
	// }
	// }

	public void UpdateExeAccess(String emp_id, String emp_all_exe,
			String comp_id) {
		String ExeAccess = "", BranchAccess = "";
		// SOP("emp_all_exe=========" + emp_all_exe);
		if (!emp_id.equals("1")) {
			if (!emp_all_exe.equals("1")) {
				try {
					StrSql = "SELECT empexe_id from " + compdb(comp_id)
							+ "axela_emp_exe where empexe_emp_id = " + emp_id;
					// SOP("strsql==empexe_id===" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							ExeAccess = ExeAccess + ""
									+ crs.getString("empexe_id") + ",";
						}
					}
					crs.close();
					ExeAccess = "" + emp_id + "," + ExeAccess + "";
					ExeAccess = ExeAccess.substring(0, ExeAccess.length() - 1);
				} catch (Exception ex) {
					SOPError("AxelaAuto==="
							+ this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + " : " + ex);
				}
			}
		}

		StrSql = " UPDATE " + compdb(comp_id) + "axela_emp" + " SET "
				+ " emp_exeaccess = '" + ExeAccess + "',"
				+ " emp_branchaccess = '" + BranchAccess + "'"
				+ " WHERE emp_id = " + emp_id + "";
		// SOP("strsql==update===" + StrSql);
		updateQuery(StrSql);

	}

	public void WSEcoverRequest(HttpServletRequest request) throws JSONException {
		// Start Ecover Exe Update
		JSONObject input = new JSONObject();
		emp_uuid = ExecuteQuery("SELECT emp_uuid "
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id=emp_id");
		input.put("emp_uuid", emp_uuid);
		input.put("emp_id", emp_id);
		input.put("emp_upass", emp_upass);
		input.put("comp_id", comp_id);
		input.put("emp_name", emp_name);
		input.put("emp_role_id", emp_role_id);
		input.put("emp_mobile1", emp_mobile1);
		input.put("emp_email1", emp_email1);
		input.put("emp_notes", emp_notes);
		input.put("emp_all_exe", emp_all_exe);
		input.put("emp_all_branches", emp_all_branches);
		input.put("emp_active", emp_active);
		input.put("emp_name", emp_name);
		input.put("emp_ref_no", emp_ref_no);
		input.put("emp_sex", emp_sex);
		input.put("emp_dob", emp_dob);
		input.put("drop_month", drop_month);
		input.put("drop_day", drop_day);
		input.put("drop_year", drop_year);
		input.put("emp_married", emp_married);
		input.put("emp_qualification", emp_qualification);
		input.put("emp_certification", emp_certification);
		input.put("emp_phone1", emp_phone1);
		input.put("emp_phone2", emp_phone2);
		input.put("emp_mobile1", emp_mobile1);
		input.put("emp_mobile2", emp_mobile2);
		input.put("emp_email1", emp_email1);
		input.put("emp_email2", emp_email2);
		input.put("emp_address", emp_address);
		input.put("emp_city", emp_city);
		input.put("emp_branch_id", emp_branch_id);
		input.put("emp_pin", emp_pin);
		input.put("emp_state", emp_state);
		input.put("emp_landmark", emp_landmark);
		input.put("emp_active", emp_active);
		input.put("emp_weeklyoff_id", emp_weeklyoff_id);
		input.put("emp_prevexp_year", emp_prevexp_year);
		input.put("emp_prevexp_month", emp_prevexp_month);
		input.put("emp_date_of_join", emp_date_of_join);
		input.put("emp_date_of_relieve", emp_date_of_relieve);
		input.put("emp_reason_of_leaving", emp_reason_of_leaving);
		input.put("emp_notes", emp_notes);
		input.put("emp_theme_id", emp_theme_id);
		input.put("emp_entry_id", emp_entry_id);
		input.put("emp_entry_date", emp_entry_date);
		input.put("emp_modified_id", emp_modified_id);
		input.put("emp_modified_date", emp_modified_date);
		if (add.equals("yes")) {
			input.put("add", "yes");
		}
		if (update.equals("yes")) {
			input.put("update", "yes");
		}
		SOP("input==========================" + input.toString());
		// new Ecover_WS().WSRequest(input, "axelaauto-exe-update", request);
		if (msg.equals("")) {
			StrSql = "SELECT comp_ecover_integration "
					+ "FROM " + compdb(comp_id) + "axela_comp"
					+ " WHERE comp_id=" + comp_id;
			// //SOP("StrSql=======" + StrSql);
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				new Ecover_WS().WSRequest(input, "axelaauto-exe-update", request);
			}
		}
		// if (msg.equals("")) {
		// StrSql = "SELECT comp_axelacrm_integration "
		// + "FROM " + compdb(comp_id) + "axela_comp"
		// + " WHERE comp_id=" + comp_id;
		// // //SOP("StrSql=======" + StrSql);
		// if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
		// new Axelacrm_WS().WSAxelacrmRequest(input, "exe-update", request);
		// }
		// }
		// End Ecover Exe Update
	}

}
