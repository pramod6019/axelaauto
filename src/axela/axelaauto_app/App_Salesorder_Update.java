//Manjur, 18th April, 2015
package axela.axelaauto_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.sales.Veh_Salesorder_Update;
import cloudify.connect.Connect;

public class App_Salesorder_Update extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String emp_name = "";
	public String emp_email = "";
	public String emp_mobile = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_close_enquiry = "0";
	public String so_form60 = "";
	public String strHTML = "";
	public String empEditperm = "";
	public String session_id = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String so_fintype_id = "0";
	public String rateclass_id = "0";
	public String emp_role_id = "0";
	public String enquiry_enquirytype_id = "0";
	// * Sales Order Variables */
	public String so_id = "0", so_reg_no = "";
	public String so_option_id = "0";
	public String so_date = "";
	public String sodate = "", sodate1 = "";
	public String so_finstatus_desc = "";
	public String so_item_id = "0";
	public String so_vehstock_id = "0";
	public String so_preownedstock_id = "0";
	public String so_netqty = "", so_retaildate = "", so_retaildate1 = "";
	public String so_netamt = "";
	public String so_discamt = "";
	public String so_grandtotal = "";
	public String so_quote_id = "0", enquiry_id = "0", lead_id = "0";
	public String enquiry_status_id = "0";
	public String so_totaltax = "";
	public String so_booking_amount = "";
	public String so_po = "";
	public String so_payment_date = "", so_paymentdate = "";
	public String so_paymentdate2 = "", so_paymentdate1 = "";
	public String so_promise_date = "";
	public String promisedate = "", so_regdate = "", so_regdate1 = "";
	public String so_promisedate = "";
	public String so_delivered_date = "", so_delivereddate = "", so_delivereddate1 = "";
	public String so_cancel_date = "", so_canceldate = "", so_canceldate1 = "";
	public String so_mga_amount = "0", so_refund_amount = "0";
	public String so_allot_no = "0";
	public String so_prioritybalance_id = "0";
	public String so_open = "0";
	public String so_critical = "0";
	public String so_hni = "0", so_enquiry_id = "0";
	public String so_emp_id = "0";
	public String so_refno = "";
	public String so_active = "0";
	public String so_notes = "";
	public String so_auth = "0";
	public String so_desc = "", so_terms = "";
	public String so_entry_id = "0";
	public String so_entry_by = "";
	public String so_entry_date = "";
	public String so_modified_id = "0";
	public String so_modified_by = "";
	public String so_modified_date = "";
	public String entry_date = "";
	public String dr_month = "";
	public String dr_day = "";
	public String dr_year = "";
	public String so_dob = "";
	public String so_exchange = "0";
	public String branch_brand_id = "0";
	public String modified_date = "";
	/* End Of Sales Order Variables */
	/* Config Variables */
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";

	public String brandconfig_so_email_enable = "0";
	public String brandconfig_so_email_exe_enable = "0";
	public String brandconfig_so_email_format = "";
	public String brandconfig_so_email_sub = "";
	public String brandconfig_so_sms_enable = "0";
	public String brandconfig_so_sms_exe_enable = "0";
	public String brandconfig_so_sms_format = "";
	public String brandconfig_so_delivered_email_enable = "0";
	public String brandconfig_so_delivered_email_format = "";
	public String brandconfig_so_delivered_email_sub = "";
	public String brandconfig_so_delivered_sms_enable = "0";
	public String brandconfig_so_delivered_sms_format = "";
	public String brandconfig_so_email_exe_sub = "";
	public String brandconfig_so_email_exe_format = "";
	public String brandconfig_so_sms_exe_format = "";

	public String branch_sales_mobile = "";
	public String branch_email1 = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_customer_dupnames = "";
	public String config_sales_so_refno = "";
	public String emp_so_priceupdate = "";
	public String emp_so_discountupdate = "";
	/* End of Config Variables */
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String checkPerm = "";
	public String colspan = "8";
	public String contact_id = "0";
	public String so_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String contact_city_id = "0";
	public String state_id = "0";
	public String so_customer_id = "0";
	public String customer_id = "0";
	public String customer_address = "";
	public String customer_name = "";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String branch_address = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String readOnly = "";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public String exeemailmsg = "", exeemailsub = "";
	public String exesmsmsg = "", so_exprice = "";
	public String so_din_accessories_special_remarks = "";
	public String so_pan = "";
	public String so_hypothecation = "";
	public String so_fincomp_id = "0";
	public String so_finance_amt = "";
	public String so_inscomp_id = "0";
	public String so_cancelreason_id = "0";
	public String so_insur_amount = "";
	public String so_file_status = "";
	public String vehstock_comm_no = "";
	public String display = "";
	public String day = "";
	public String month = "";
	public String year = "", temp = "";
	public String fullaccname = "";
	public String emp_receipt_access = "0";
	public String access = "0", emp_all_exe = "", item_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			BranchAccess = GetSession("BranchAccess", request);
			emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = GetSession("emp_all_exe", request);
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				// emp_branch_id = CNumeric(GetSession("emp_branch_id",
				// request));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				add = PadQuotes(request.getParameter("add"));
				msg = PadQuotes(request.getParameter("msg"));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				so_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				so_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				so_quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
				// For Generating session each time
				session_id = PadQuotes(request.getParameter("txt_session_id"));

				access = ReturnPerm(comp_id, "emp_sales_order_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}

				if (!so_quote_id.equals("") && add.equals("yes")) {
					GetQuoteDetails(response);
					so_id = CNumeric(ExecuteQuery("SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
							+ " AND enquiry_id = " + enquiry_id
							+ " AND so_active = 1"
							+ " LIMIT 1"));
					if (!so_id.equals("0")) {
						response.sendRedirect("callurlapp-error.jsp?msg=Sales Order already present for this Enquiry!");
					}
					// GetConfigurationDetails(request);
				}
				// so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				// PopulateContactDetails();
				// PopulateConfigDetails();
				QueryString = PadQuotes(request.getQueryString());
				PopulateContactDetails();
				empEditperm = ReturnPerm(comp_id, "emp_sales_order_edit", request);
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				emp_close_enquiry = CNumeric(ExecuteQuery("SELECT emp_close_enquiry "
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id));
				PopulateConfigDetails();
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
					if (!so_quote_id.equals("0")) {
						// GetConfigurationDetails(request);
						GetQuoteDetails(response);
					}
				}

				if (!"yes".equals(addB)) {
					if (session_id.equals("")) {
						String key = "", possible = "0123456789";
						for (int i = 0; i < 9; i++) {
							double math = Math.random();
							key += possible.charAt((int) Math.floor(Math
									.random() * possible.length()));
						}
						session_id = key;
					}
					StrSql = "SELECT branch_so_desc, branch_so_terms, city_id, state_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE branch_id = " + branch_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);

					while (crs.next()) {
						contact_city_id = crs.getString("city_id");
						state_id = crs.getString("state_id");
						so_desc = crs.getString("branch_so_desc");
						so_terms = crs.getString("branch_so_terms");
					}
					crs.close();
					so_date = ToLongDate(kknow());
					sodate = strToShortDate(so_date);
					so_auth = "0";
					so_file_status = "";
					contact_title_id = "0";
				} else if ((add.equals("yes")) && ("yes".equals(addB))) {
					if (ReturnPerm(comp_id, "emp_sales_order_add", request).equals("1")) {
						GetValues(request, response);
						so_entry_id = emp_id;
						so_entry_date = ToLongDate(kknow());
						so_active = "1";
						Veh_Salesorder_Update soupdate = new Veh_Salesorder_Update();
						soupdate.emp_id = emp_id;
						soupdate.so_emp_id = so_emp_id;
						soupdate.customer_id = customer_id;
						soupdate.customer_address = customer_address;
						soupdate.rateclass_id = rateclass_id;
						soupdate.branch_id = branch_id;
						soupdate.session_id = session_id;
						soupdate.sodate = sodate;
						soupdate.so_customer_id = so_customer_id;
						soupdate.so_contact_id = so_contact_id;
						soupdate.lead_id = lead_id;
						soupdate.so_entry_id = enquiry_id;
						soupdate.so_booking_amount = so_booking_amount;
						soupdate.so_quote_id = so_quote_id;
						soupdate.item_id = item_id;
						soupdate.so_exprice = so_exprice;
						soupdate.so_netamt = so_netamt;
						soupdate.so_discamt = so_discamt;
						soupdate.so_totaltax = so_totaltax;
						soupdate.so_grandtotal = so_grandtotal;
						soupdate.so_vehstock_id = so_vehstock_id;
						soupdate.dr_day = dr_day;
						soupdate.dr_month = dr_month;
						soupdate.dr_year = dr_year;
						soupdate.so_dob = so_dob;
						soupdate.so_allot_no = so_allot_no;
						soupdate.so_mga_amount = so_mga_amount;
						soupdate.so_refund_amount = so_refund_amount;
						soupdate.enquiry_id = enquiry_id;
						soupdate.emp_close_enquiry = emp_close_enquiry;

						// soupdate.so_preownedvehstock_id = so_preownedvehstock_id;
						// soupdate.so_insur_amount = so_insur_amount;
						soupdate.so_desc = "";
						soupdate.so_terms = "";
						soupdate.so_po = so_po;
						soupdate.so_fintype_id = so_fintype_id;
						// soupdate.so_finstatus_desc = so_finstatus_desc;
						soupdate.so_fincomp_id = so_fincomp_id;
						soupdate.so_finance_amt = so_finance_amt;
						soupdate.so_emp_id = so_emp_id;
						soupdate.so_refno = so_refno;
						soupdate.so_paymentdate = so_paymentdate;
						soupdate.so_promisedate = so_promisedate;
						// soupdate.so_delivereddate = so_delivereddate;
						soupdate.so_pan = so_pan;
						soupdate.so_option_id = so_option_id;
						soupdate.so_open = so_open;
						soupdate.so_canceldate = so_canceldate;
						soupdate.so_cancelreason_id = so_cancelreason_id;
						soupdate.so_active = so_active;
						soupdate.so_notes = so_notes;
						soupdate.so_reg_no = so_reg_no;
						soupdate.so_regdate = so_regdate;
						soupdate.so_exchange = so_exchange;
						soupdate.so_entry_id = emp_id;
						soupdate.so_entry_date = ToLongDate(kknow());
						soupdate.comp_sms_enable = comp_sms_enable;
						soupdate.config_sms_enable = config_sms_enable;

						// Start config
						soupdate.brandconfig_so_email_enable = brandconfig_so_email_enable;
						soupdate.brandconfig_so_email_format = brandconfig_so_email_format;
						soupdate.brandconfig_so_email_sub = brandconfig_so_email_sub;
						soupdate.brandconfig_so_email_exe_enable = brandconfig_so_email_exe_enable;
						soupdate.brandconfig_so_email_exe_format = brandconfig_so_email_exe_format;
						soupdate.brandconfig_so_email_exe_sub = brandconfig_so_email_exe_sub;
						soupdate.brandconfig_so_sms_enable = brandconfig_so_sms_enable;
						soupdate.brandconfig_so_sms_format = brandconfig_so_sms_format;
						soupdate.brandconfig_so_sms_exe_enable = brandconfig_so_sms_exe_enable;
						soupdate.brandconfig_so_sms_exe_format = brandconfig_so_sms_exe_format;
						soupdate.brandconfig_so_delivered_email_enable = brandconfig_so_delivered_email_enable;
						soupdate.brandconfig_so_delivered_email_format = brandconfig_so_delivered_email_format;
						soupdate.brandconfig_so_delivered_email_sub = brandconfig_so_delivered_email_sub;
						soupdate.brandconfig_so_delivered_sms_enable = brandconfig_so_delivered_sms_enable;
						soupdate.brandconfig_so_delivered_sms_format = brandconfig_so_delivered_sms_format;
						soupdate.emp_name = emp_name;
						soupdate.branch_sales_mobile = branch_sales_mobile;
						soupdate.emp_email = emp_email;
						soupdate.emp_mobile = emp_mobile;
						soupdate.branch_email1 = branch_email1;
						soupdate.config_email_enable = config_email_enable;
						soupdate.config_sms_enable = config_sms_enable;
						soupdate.config_customer_dupnames = config_customer_dupnames;
						soupdate.comp_email_enable = comp_email_enable;
						soupdate.comp_sms_enable = comp_sms_enable;
						soupdate.config_sales_so_refno = config_sales_so_refno;
						soupdate.emp_so_priceupdate = emp_so_priceupdate;
						soupdate.emp_so_discountupdate = emp_so_discountupdate;
						soupdate.contact_email1 = contact_email1;
						// End config

						soupdate.contact_mobile1 = contact_mobile1;
						soupdate.branch_sales_mobile = branch_sales_mobile;
						soupdate.emp_mobile = emp_mobile;
						soupdate.so_retail_date = so_retaildate;
						soupdate.comp_id = comp_id;
						soupdate.so_form60 = so_form60;
						soupdate.so_preownedstock_id = so_preownedstock_id;
						soupdate.AddFields(request);
						so_id = soupdate.so_id;
						msg = soupdate.msg;
						// && enquiry_enquirytype_id.equals("1")
						if (so_active.equals("1") && msg.equals("")) {
							soupdate.SendEmailToNewSales(so_id, so_vehstock_id, so_entry_id, so_active, branch_email1, comp_id);
						} else if (so_active.equals("1") && !CNumeric(so_preownedstock_id).equals("0") && msg.equals("")) {
							// soupdate.SendEmailToPreOwnedSales(so_id, so_preownedvehstock_id, so_entry_id, so_active, branch_email1, comp_id);
						}
						if (msg.equals("")) {
							response.sendRedirect(response.encodeRedirectURL("callurl"
									+ "app-veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Sales Order added successfully!"));
						} else if (!msg.equals("")) {
							msg = "Error!" + unescapehtml(msg);
							// response.sendRedirect("callurlapp-salesorder-update.jsp?quote_id="
							// + so_quote_id + "&msg=" + msg + "");
						}
					} else {
						response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App=====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_enquirytype_id")));
		so_preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_so_preownedstock_id")));
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_contact_city_id")));
		state_id = CNumeric(PadQuotes(request.getParameter("dr_contact_state_id")));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		so_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_so_auth")));
		so_item_id = PadQuotes(request.getParameter("txt_so_item_id"));
		so_option_id = CNumeric(PadQuotes(request.getParameter("dr_option_id")));
		enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_so_enquiry_id")));
		so_reg_no = PadQuotes(request.getParameter("txt_so_reg_no"));
		so_regdate = PadQuotes(request.getParameter("txt_so_reg_date"));
		so_form60 = CheckBoxValue(PadQuotes(request.getParameter("chk_so_form60")));
		sodate = PadQuotes(request.getParameter("txt_so_date"));
		so_netqty = CNumeric(PadQuotes(request.getParameter("txt_so_qty")));
		so_netamt = CNumeric(PadQuotes(request.getParameter("txt_so_netamt")));
		so_discamt = CNumeric(PadQuotes(request.getParameter("txt_so_discamt")));
		so_totaltax = CNumeric(PadQuotes(request.getParameter("txt_so_totaltax")));
		so_exprice = CNumeric(PadQuotes(request.getParameter("txt_so_exprice")));
		so_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_so_grandtotal")));
		so_desc = PadQuotes(request.getParameter("txt_so_desc"));
		so_terms = PadQuotes(request.getParameter("txt_so_terms"));
		so_din_accessories_special_remarks = PadQuotes(request.getParameter("txt_so_din_accessories_special_remarks"));
		so_pan = PadQuotes(request.getParameter("txt_so_pan"));
		so_hypothecation = PadQuotes(request.getParameter("txt_so_hypothecation"));
		so_fincomp_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		so_fintype_id = CNumeric(PadQuotes(request.getParameter("dr_so_fintype")));
		so_finstatus_desc = PadQuotes(request.getParameter("txt_so_finstatus_desc"));
		if (so_finstatus_desc.length() > 255) {
			so_finstatus_desc = so_finstatus_desc.substring(0, 254);
		}
		so_finance_amt = CNumeric(PadQuotes(request.getParameter("txt_so_finance_amt")));
		so_inscomp_id = PadQuotes(request.getParameter("dr_insurance_by"));
		so_insur_amount = CNumeric(PadQuotes(request.getParameter("txt_so_insurance_amt")));
		so_file_status = PadQuotes(request.getParameter("txt_so_filestatus"));
		so_refno = PadQuotes(request.getParameter("txt_so_refno"));
		so_booking_amount = CNumeric(PadQuotes(request.getParameter("txt_so_booking_amount")));
		so_po = PadQuotes(request.getParameter("txt_so_po"));
		so_paymentdate = PadQuotes(request.getParameter("txt_so_payment_date"));
		so_retaildate = PadQuotes(request.getParameter("txt_so_retail_date"));
		so_promisedate = PadQuotes(request.getParameter("txt_so_promise_date"));
		so_canceldate = PadQuotes(request.getParameter("txt_so_cancel_date"));
		so_cancelreason_id = PadQuotes(request.getParameter("dr_cancel_reason"));
		so_delivereddate = PadQuotes(request.getParameter("txt_so_delivered_date"));
		vehstock_comm_no = PadQuotes(request.getParameter("txt_vehstock_comm_no"));
		so_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_so_vehstock_id")));
		dr_month = PadQuotes(request.getParameter("dr_DOBMonth"));
		dr_day = PadQuotes(request.getParameter("dr_DOBDay"));
		dr_year = PadQuotes(request.getParameter("dr_DOBYear"));
		if (dr_month.length() < 2) {
			dr_month = "0" + dr_month;
		}
		if (dr_day.length() < 2) {
			dr_day = "0" + dr_day;
		}
		so_dob = dr_day + "/" + dr_month + "/" + dr_year;
		so_allot_no = CNumeric(PadQuotes(request.getParameter("txt_so_allot_no")));
		so_open = CheckBoxValue(PadQuotes(request.getParameter("chk_so_open")));
		so_quote_id = CNumeric(PadQuotes(request.getParameter("txt_so_quote_id")));
		so_critical = CheckBoxValue(PadQuotes(request.getParameter("chk_so_critical")));
		so_hni = CheckBoxValue(PadQuotes(request.getParameter("chk_so_hni")));
		so_active = CheckBoxValue(PadQuotes(request.getParameter("chk_so_active")));
		so_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		so_notes = PadQuotes(request.getParameter("txt_so_notes"));
		so_entry_by = PadQuotes(request.getParameter("entry_by"));
		so_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !so_contact_id.equals("0")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, contact_email1, contact_mobile1, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE 1 = 1";
				if (!contact_id.equals("0")) {
					StrSql += " AND contact_id = " + contact_id + "";
				} else if (!so_contact_id.equals("0")) {
					StrSql += " AND contact_id = " + so_contact_id + "";
				}
				// StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					so_customer_id = crs.getString("customer_id");
					so_contact_id = crs.getString("contact_id");
					contact_email1 = crs.getString("contact_email1");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					link_customer_name = "<a href=\"../customer/app-customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/app-customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE (brandconfig_so_email_enable,'') AS brandconfig_so_email_enable,"
					+ " COALESCE (brandconfig_so_email_sub,'') AS brandconfig_so_email_sub,"
					+ " COALESCE (brandconfig_so_email_format,'') AS brandconfig_so_email_format,"
					+ " COALESCE (brandconfig_so_email_exe_enable,'') AS brandconfig_so_email_exe_enable,"
					+ " COALESCE (brandconfig_so_email_exe_sub,'') AS brandconfig_so_email_exe_sub,"
					+ " COALESCE (brandconfig_so_email_exe_format,'') AS brandconfig_so_email_exe_format,"
					+ " COALESCE (brandconfig_so_sms_enable,'') AS brandconfig_so_sms_enable,"
					+ " COALESCE (brandconfig_so_sms_format,'') AS brandconfig_so_sms_format,"
					+ " COALESCE (brandconfig_so_sms_exe_enable,'') AS brandconfig_so_sms_exe_enable,"
					+ " COALESCE (brandconfig_so_sms_exe_format,'') AS brandconfig_so_sms_exe_format,"
					+ " COALESCE (brandconfig_so_delivered_email_enable,'') AS brandconfig_so_delivered_email_enable,"
					+ " COALESCE (brandconfig_so_delivered_email_sub,'') AS brandconfig_so_delivered_email_sub,"
					+ " COALESCE (brandconfig_so_delivered_email_format,'') AS brandconfig_so_delivered_email_format,"
					+ " COALESCE (brandconfig_so_delivered_sms_enable,'') AS brandconfig_so_delivered_sms_enable,"
					+ " COALESCE (brandconfig_so_delivered_sms_format,'') AS brandconfig_so_delivered_sms_format,"
					+ " COALESCE(IF(emp.emp_email1 != '', emp.emp_email1, emp.emp_email2), '') AS emp_email,"
					+ " COALESCE(emp.emp_name, '') AS emp_name, COALESCE(branch_email1, '') AS branch_email1,"
					+ " COALESCE(IF(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2), '') AS emp_mobile,"
					+ " config_admin_email, config_email_enable, config_sms_enable, comp_sms_enable,"
					+ " config_sales_so_refno, config_customer_dupnames, comp_email_enable, "
					+ " COALESCE(branch_sales_mobile, '') AS branch_sales_mobile,"
					+ " COALESCE(emp.emp_so_priceupdate, '') AS emp_so_priceupdate,"
					+ " COALESCE(emp.emp_so_discountupdate, '') AS emp_so_discountupdate"
					+ " FROM "
					+ compdb(comp_id) + "axela_config, "
					+ compdb(comp_id) + "axela_comp, "
					+ compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
					+ " WHERE"
					+ " admin.emp_id = " + emp_id + "";
			SOP("StrSql=======PopulateConfigDetails=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				brandconfig_so_email_enable = crs.getString("brandconfig_so_email_enable");
				brandconfig_so_email_format = crs.getString("brandconfig_so_email_format");
				brandconfig_so_email_sub = crs.getString("brandconfig_so_email_sub");
				brandconfig_so_email_exe_enable = crs.getString("brandconfig_so_email_exe_enable");
				brandconfig_so_email_exe_format = crs.getString("brandconfig_so_email_exe_format");
				brandconfig_so_email_exe_sub = crs.getString("brandconfig_so_email_exe_sub");
				brandconfig_so_sms_enable = crs.getString("brandconfig_so_sms_enable");
				brandconfig_so_sms_format = crs.getString("brandconfig_so_sms_format");
				brandconfig_so_sms_exe_enable = crs.getString("brandconfig_so_sms_exe_enable");
				brandconfig_so_sms_exe_format = crs.getString("brandconfig_so_sms_exe_format");
				brandconfig_so_delivered_email_enable = crs.getString("brandconfig_so_delivered_email_enable");
				brandconfig_so_delivered_email_format = crs.getString("brandconfig_so_delivered_email_format");
				brandconfig_so_delivered_email_sub = crs.getString("brandconfig_so_delivered_email_sub");
				brandconfig_so_delivered_sms_enable = crs.getString("brandconfig_so_delivered_sms_enable");
				brandconfig_so_delivered_sms_format = crs.getString("brandconfig_so_delivered_sms_format");

				branch_sales_mobile = crs.getString("branch_sales_mobile");
				emp_name = crs.getString("emp_name");
				SOP("emp_name==============" + emp_name);
				emp_email = crs.getString("emp_email");
				emp_mobile = crs.getString("emp_mobile");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sales_so_refno = crs.getString("config_sales_so_refno");
				emp_so_priceupdate = crs.getString("emp_so_priceupdate");
				emp_so_discountupdate = crs.getString("emp_so_discountupdate");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select Sales Consultant</option>\n");

			StrSql = "SELECT emp_id,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), so_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFinanceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fintype_id, fintype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
					+ " GROUP BY fintype_id"
					+ " ORDER BY fintype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fintype_id"));
				Str.append(StrSelectdrop(crs.getString("fintype_id"), so_fintype_id));
				Str.append(">").append(crs.getString("fintype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Axelaauto-App==== "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id"));
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), so_fincomp_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCancelReason() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id"));
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), so_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateInsuranceBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT inscomp_id, inscomp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_active = 1" + " ORDER BY inscomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inscomp_id"));
				Str.append(StrSelectdrop(crs.getString("inscomp_id"),
						so_inscomp_id));
				Str.append(">").append(crs.getString("inscomp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		Str.append("<option value = 1").append(Selectdrop(1, month)).append(">January</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, month)).append(">February</option>\n");
		Str.append("<option value = 3").append(Selectdrop(3, month)).append(">March</option>\n");
		Str.append("<option value = 4").append(Selectdrop(4, month)).append(">April</option>\n");
		Str.append("<option value = 5").append(Selectdrop(5, month)).append(">May</option>\n");
		Str.append("<option value = 6").append(Selectdrop(6, month)).append(">June</option>\n");
		Str.append("<option value = 7").append(Selectdrop(7, month)).append(">July</option>\n");
		Str.append("<option value = 8").append(Selectdrop(8, month)).append(">August</option>\n");
		Str.append("<option value = 9").append(Selectdrop(9, month)).append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month)).append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month)).append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month)).append(">December</option>\n");
		return Str.toString();
	}

	public String PopulateDay(String day) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = 1; i <= 31; i++) {
			Str.append("<option value=").append(i).append(Selectdrop(i, day));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateYear(String year) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = Integer.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 1940; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String GetConfigurationDetails(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String group = "", aftertax = "", grand_total = "0";
		Double quote_exprice = 0.00, total_disc = 0.00;
		String preowned = "";
		int groupitemcount = 0;
		try {
			if (enquiry_enquirytype_id.equals("2") && !so_preownedstock_id.equals("0")) {
				StrSql = "SELECT preownedstock_selling_price, variant_name,"
						+ " preownedmodel_name, preowned_sub_variant"
						+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ "	INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " WHERE preownedstock_id = " + so_preownedstock_id + "";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned = crs.getString("preownedmodel_name") + " - " + crs.getString("variant_name");
						if (!crs.getString("preowned_sub_variant").equals("")) {
							preowned += "<br>" + crs.getString("preowned_sub_variant");
						}
					}
				}
				crs.close();
			}
			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_small_desc, quoteitem_option_group, quoteitem_option_group_tax,"
					+ " quoteitem_price, quoteitem_qty, quoteitem_disc, quoteitem_total,"
					+ " quoteitem_tax_rate, quoteitem_tax2_rate, quoteitem_tax3_rate, quoteitem_tax4_rate,"
					+ " quote_exprice, quote_grandtotal, quoteitem_rowcount"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quoteitem_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " AND quote_id = "
					+ CNumeric(so_quote_id)
					+ ""
					+ " GROUP BY item_id"
					+ " ORDER BY quoteitem_option_id, quoteitem_option_group_tax, quoteitem_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					total_disc += crs.getDouble("quoteitem_disc");
					String group_name = crs.getString("quoteitem_option_group");
					if (!crs.getString("quoteitem_option_group_tax").equals(
							aftertax)
							&& !aftertax.equals("")) {
						Str.append("<div class=\"col-md-12\">")
								.append("<div class=\"form-group\">")
								.append("<div class=\"col-md-2 col-xs-2\">")
								.append("</div>")
								.append("<div class=\"col-md-10 col-xs-10\">")
								.append("<table class=\"table\" align=\"center\">")
								.append("<tr>")
								.append("<td>")
								.append("<b><center>")
								.append("Ex-ShowroomPrice:")
								.append("</b></center>")
								.append("</td>")
								.append("<td style=\"text-align: left\">")
								.append(df.format(crs.getDouble("quote_exprice")))
								.append("</b>").append("</td>").append("</tr>")
								.append("</table>").append("</div>")
								.append("</div>").append("</div>");

						// Str.append("<tr valign=\"top\">\n<td colspan=5 align=\"right\" nowrap><b>");
						// Str.append("Ex-Showroom Price: </td>\n");
						// Str.append("<td align=\"right\"><b>").append(df.format(crs.getDouble("quote_exprice"))).append("</b></td>\n</tr>\n");
					}

					if (!group.equals(group_name) && !group.equals("")) {
						groupitemcount = 0;
					}

					// if (!group.equals(group_name)) {
					//
					// Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #8f3e97\">")
					// .append("<h3 class=\"panel-title\">")
					// .append("<center>")
					// .append("<strong>").append(group_name).append("</strong>")
					// .append("</center>")
					// .append("</h3>")
					// .append("</div>");
					// }
					//
					// groupitemcount++;

					Str.append("<div class=\"col-md-12\">");
					if (!group.equals(group_name)) {

						Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #8E44AD\">")
								.append("<h3 class=\"panel-title\">")
								.append("<center>").append("<strong>")
								.append(group_name).append("</strong>")
								.append("</center>").append("</h3>")
								.append("</div>\n");
					}

					groupitemcount++;
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-2 col-xs-2\">\n")
							.append("<br>\n")
							.append("<input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\"/>\n")
							.append("</div>\n");

					Str.append("<div class=\"col-md-10 col-xs-10\" style=\"border-left: 1px solid #8E44AD;\">\n")
							.append("<center>").append("<br>\n").append("<b>")
							.append(crs.getString("item_name"));

					if (!crs.getString("item_small_desc").equals("") && enquiry_enquirytype_id.equals("1")) {
						Str.append("<br>\n").append(crs.getString("item_small_desc"));

					} else if (enquiry_enquirytype_id.equals("2")) {
						Str.append("<br>\n").append(preowned);
					}

					Str.append("</b>\n</center>\n");
					Str.append("<table class=\"table\" align=\"center\">")
							.append("<tr>\n")
							.append("<td>\n")
							.append("Price:")
							.append("</td>\n")
							.append("<td style=\"text-align: left\">\n")
							.append(df.format((crs.getDouble("quoteitem_price")
									* (crs.getDouble("quoteitem_tax_rate")
											+ crs.getDouble("quoteitem_tax2_rate")
											+ crs.getDouble("quoteitem_tax3_rate")
											+ crs.getDouble("quoteitem_tax4_rate")) / 100)
									+ crs.getDouble("quoteitem_price")))
							.append("</td>\n").append("</tr>\n");

					Str.append("<tr>\n").append("<td>\n").append("Discount:")
							.append("</td>\n")
							.append("<td style=\"text-align: left\">\n")
							.append(df.format(crs.getDouble("quoteitem_disc")))
							.append("</td>\n").append("<tr>\n");

					Str.append("<tr>\n").append("<td>\n").append("Amount:")
							.append("</td>\n")
							.append("<td style=\"text-align: left\">\n")
							.append(df.format(crs.getDouble("quoteitem_total")))
							.append("</td>\n").append("<tr>\n").append("</table>\n")
							.append("</div>\n").append("</div>\n").append("</div>\n");

					group = group_name;
					aftertax = crs.getString("quoteitem_option_group_tax");
					quote_exprice = crs.getDouble("quote_exprice");
					grand_total = crs.getString("quote_grandtotal");
				}
			}

			if (aftertax.equals("1")) {
				Str.append("<div class=\"col-md-12\">\n")
						.append("<div class=\"form-group\">\n")
						.append("<div class=\"col-md-2 col-xs-2\">\n")
						.append("</div>\n")
						.append("<div class=\"col-md-10 col-xs-10\">\n")
						.append("<table class=\"table\" align=\"center\">\n")
						.append("<tr>").append("<td>").append("<b><center>\n")
						.append("Ex-ShowroomPrice:").append("</b></center>\n")
						.append("</td>\n")
						.append("<td style=\"text-align: left\">\n")
						.append("<b>\n").append(df.format(quote_exprice))
						.append("</b>\n").append("</td>\n").append("</tr>\n")
						.append("</table>\n").append("</div>\n").append("</div>\n")
						.append("</div>\n");
				//
			}
			Str.append("<div class=\"col-md-12\">\n")
					.append("<div class=\"form-group\">\n")
					.append("<div class=\"col-md-2 col-xs-2\">\n")
					.append("</div>\n")
					.append("<div class=\"col-md-10 col-xs-10\">\n")
					.append("<table class=\"table\" align=\"center\">\n")
					.append("<tr>\n").append("<td>\n").append("<b>\n")
					.append("On-Road Price:").append("</b>\n").append("</td>\n")
					.append("<td style=\"text-align: left\">\n").append("<b>\n")
					.append(df.format(Double.parseDouble(grand_total)))
					.append("</b>\n").append("</td>\n").append("</tr>\n")
					.append("<tr>\n").append("<td>\n").append("<b>\n")
					.append("Total Savings:").append("</b>\n").append("</td>\n")
					.append("<td style=\"text-align: left\">\n").append("<b>\n")
					.append(df.format(total_disc)).append("</b>\n")
					.append("</td>\n").append("</tr>\n").append("</table>\n")
					.append("</div>\n").append("</div>\n").append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void GetQuoteDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT COALESCE(enquiry_enquirytype_id, 0) AS enquiry_enquirytype_id,"
					+ " quote_emp_id, quote_item_id,"
					+ " COALESCE(enquiry_id, 0) AS enquiry_id,"
					+ " COALESCE(so_id, 0) AS so_id,"
					+ " COALESCE(so_active, '0') AS  so_active, customer_id,"
					+ " contact_id, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, "
					+ "	quote_vehstock_id, quote_preownedstock_id,"
					+ " contact_mobile1, contact_email1, quote_grandtotal, quote_totaltax, quote_netamt, quote_discamt,"
					+ " COALESCE(customer_address, '') AS customer_address,"
					+ " state_name, city_name, customer_address, customer_pin,"
					+ " COALESCE(customer_landmark, '') AS customer_landmark,"
					+ " quote_exprice, quoteitem_item_id, customer_name,"
					+ " quote_branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname, rateclass_id, branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = quote_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " WHERE quote_id = " + CNumeric(so_quote_id) + ""
					+ " GROUP BY quote_id";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					so_emp_id = crs.getString("quote_emp_id");
					customer_id = crs.getString("customer_id");
					customer_address = crs.getString("customer_address");

					if (!customer_address.equals("")) {

						if (!crs.getString("city_name").equals("")) {
							customer_address += ", " + crs.getString("city_name");
						}
						if (!crs.getString("customer_pin").equals("")) {
							customer_address += " - " + crs.getString("customer_pin");
						}
						if (!crs.getString("state_name").equals("")) {
							customer_address += ", " + crs.getString("state_name");
						}

						if (!crs.getString("customer_landmark").equals("")) {
							customer_address += "\nLandmark: " + crs.getString("customer_landmark");
						}
					}

					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					so_preownedstock_id = crs.getString("quote_preownedstock_id");
					so_emp_id = crs.getString("quote_emp_id");
					customer_id = crs.getString("customer_id");
					enquiry_id = crs.getString("enquiry_id");
					item_id = crs.getString("quote_item_id");
					// lead_id = crs.getString("lead_id");
					customer_name = crs.getString("customer_name");
					fullaccname = customer_name + "(" + customer_id + ")";
					contact_id = crs.getString("contact_id");
					contact_name = crs.getString("contactname");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_email1 = crs.getString("contact_email1");
					branch_id = crs.getString("quote_branch_id");
					rateclass_id = crs.getString("rateclass_id");
					branch_name = crs.getString("branchname");
					// so_fintype_id = crs.getString("quote_fintype_id");
					// so_hypothecation = crs.getString("quote_hypothecation");
					so_item_id = crs.getString("quoteitem_item_id");
					so_grandtotal = crs.getString("quote_grandtotal");
					so_totaltax = crs.getString("quote_totaltax");
					so_discamt = crs.getString("quote_discamt");
					so_netamt = crs.getString("quote_netamt");
					so_exprice = crs.getString("quote_exprice");
					so_vehstock_id = crs.getString("quote_vehstock_id");
					branch_brand_id = crs.getString("branch_brand_id");

					// vehstock_comm_no = crs.getString("vehstock_comm_no");
					// so_preownedvehstock_id = rs
					// .getString("quote_preownedvehstock_id");
					if (!crs.getString("so_id").equals("0") && crs.getString("so_active").equals("1")) {
						response.sendRedirect("callurlapp-error.jsp?msg=Sales Order already present for this Enquiry!");
					}
				}
			} else {
				response.sendRedirect("callurlapp-error.jsp?msg=Invalid Quote");
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public String PopulateColour() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT option_id, option_name"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " WHERE 1 = 1"
				+ " AND option_brand_id =" + branch_brand_id + ""
				+ " GROUP BY option_id"
				+ " ORDER BY option_id";
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value='0'>Select</option>");
		try {
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("option_id") + " ");
				Str.append(Selectdrop(crs.getInt("option_id"), so_option_id)).append(">");
				Str.append(crs.getString("option_name")).append("</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
