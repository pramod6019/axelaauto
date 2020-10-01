package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Returns_Update extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String msgChk = "";
	public String comp_id = "0";
	public String ledger_id = "135";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String voucher_rateclass_id = "0";
	public String voucher_id = "0";
	public String mainvoucher_id = "0";
	public String voucher_quote_id = "0";
	public String voucher_so_id = "0";
	public String voucher_invoice_id = "0";
	public String voucher_delnote_id = "0";
	public String voucherclass_id = "", voucherclass_file = "",
			voucherclass_acc = "0";
	public String vouchertype_id = "0";

	public String vouchertype_defaultauthorize = "0";
	public String vouchertype_authorize = "0";

	public String voucher_payment_date = "";
	public String voucher_customer_id = "";
	public String voucher_date = "";
	public String voucherdate = "";
	public String voucher_enquiry_id = "0", voucher_lead_id = "0";
	public int voucher_colspan = 1;
	public String voucher_emp_id = "0";
	public String voucher_ref_no = "";
	public String voucher_custref_no = "";
	public String voucher_custref_date = "";
	public String voucher_active = "0";
	public String voucher_notes = "";

	public String voucher_grandtotal = "0.00";
	public String voucher_prev_grandtotal = "";
	public String voucher_no = "";
	public String voucher_special = "";
	public String voucher_consignee_add = "", voucher_billing_add = "";

	public String voucher_transporter = "";
	public String voucher_gatepass = "";
	public String voucher_lrno = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_cashdiscount = "";
	public String voucher_turnoverdisc = "";

	public String voucherno = "";
	public String voucher_amount = "", voucher_authorize = "";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String vouchertrans_location_id = "0";
	public String checkbranch = "0", check_voucher_no = "";
	public String voucher_entry_id = "0";
	public String voucher_entry_by = "";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_by = "";
	public String voucher_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String voucher_contact_id = "0";
	public String location_name = "";
	/* End Of Voucher Variables */

	public String vouchertrans_option_id = "0";
	public String vouchertrans_option_group = "";
	public String vouchertrans_item_serial = "";
	public String vouchertrans_item_batch_id = "0";
	public String vouchertrans_price = "";
	public String vouchertrans_qty = "";
	// config variables ///
	public String vouchertype_mobile = "";
	public String vouchertype_email = "";
	public String vouchertype_dob = "0";
	public String vouchertype_dnd = "0";
	public String vouchertype_billing_add = "0";
	public String vouchertype_consignee_add = "0";

	public String vouchertype_transporter = "0";
	public String vouchertype_gatepass = "0";
	public String vouchertype_lrno = "0";
	public String vouchertype_driver_no = "0";
	public String vouchertype_tempo_no = "0";
	public String vouchertype_cashdiscount = "0";
	public String vouchertype_turnoverdisc = "0";

	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String vouchertype_terms = "";
	public String vouchertype_email_enable;
	public String vouchertype_email_auto;
	public String vouchertype_email_sub;
	public String vouchertype_email_format;
	public String vouchertype_sms_enable;
	public String vouchertype_sms_auto;
	public String vouchertype_sms_format;
	public String config_admin_email;
	public String config_email_enable;
	public String config_sms_enable;
	public String comp_email_enable;
	public String comp_sms_enable;
	public String branch_sales_order_email_enable = "0";
	public String branch_sales_order_email_format = "";
	public String branch_sales_order_email_sub = "";
	public String branch_sales_order_sms_enable = "0";
	public String branch_sales_order_sms_format = "";
	public String branch_sales_order_email_exe_sub = "";
	public String branch_sales_order_email_exe_format = "";
	public String config_refno_enable = "0";
	public String config_sales_sales_order_refno = "";
	public String config_inventory_current_stock = "0";
	public String config_sales_order_reduce_current_stock = "0";
	public String config_customer_dupnames = "0";
	public String comp_businesstype_id = "0";
	public String emp_invoice_priceUPDATE = "0";
	public String emp_invoice_discountUPDATE = "0";
	public String emp_role_id = "0";
	/* End of Config Variables */

	public String gst_type = "";

	DecimalFormat deci = new DecimalFormat("#.##");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String checkPerm = "";
	public String contact_id = "0";
	public String contact_title_id = "0", voucher_branch_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String contact_city_id = "0";
	public String customer_zone_id = "0";
	public String contact_dob = "";
	public String dr_month = "";
	public String dr_day = "";
	public String dr_year = "";
	public String contact_dnd = "0";
	public String customer_id = "0";
	public String customer_name = "";
	public String currentbal_amount = "0";
	public String customer_credit_limit = "0";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String contact_mobile = "";
	public int i = 0;
	public String readOnly = "";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public String display = "";
	public String config_customer_address = "0";
	public String comp_module_inventory = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String config_customer_name = "";
	public String vouchertrans_voucher_id = "0";
	// public String customer_id = "0";
	public String vouchertrans_amount = "";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "0";
	public String vouchertrans_paymode_id = "0";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "";
	public String vouchertrans_cheque_bank = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_name = "", customer_add = "", customer_branch_id = "0";
	public Ledger_Check ledgercheck = new Ledger_Check();
	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public String para = "";
	public String voucher_downpayment = "";
	public String voucher_promise_date = "", voucher_promisedate = "",
			voucher_paymentdate = "";
	public String voucher_delivery_date = "", voucher_delivereydate = "";
	public String voucher_open = "0";
	public String voucher_critical = "0";
	public String voucher_delstatus_id = "0";
	public String postsf_crm_emp_id = "0";
	public String postsf_manager_id = "0";
	public String vouchertype_affects_inventory = "0";
	public String item_itemgroup_id = "0";
	public String so_contact_id = "0";
	public String so_customer_id = "0", voucher_grn_id = "0";
	public String so_customer_name = "", so_contact_name = "";
	public String temp_voucher_id = "0";
	public String copyvouchertrans_voucher_id = "0", voucher_paydays_id = "0",
			paydays_days = "0", refresh = "", session_id = "", parentvoucher = "";
	public String voucher_dcr_request_id = "0", voucher_dcr_id = "0", voucher_grn_return_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
			vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
			voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
			String StrSql = "SELECT voucherclass_id , voucherclass_file"
					+ " FROM axela_acc_voucher_class"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_voucherclass_id = voucherclass_id"
					+ " WHERE vouchertype_id = ?";
			prepmap.put(prepkey++, Integer.parseInt(vouchertype_id));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			while (crs.next()) {
				if (crs.getString("voucherclass_id").equals("23")) {
					voucherclass_file = "acc_sales_return";
				} else if (crs.getString("voucherclass_id").equals("24")) {
					voucherclass_file = "acc_purchase_return";
				}
				else {
					voucherclass_file = crs.getString("voucherclass_file").toLowerCase();
				}

			}
			crs.close();
			CheckPerm(comp_id, "emp_" + voucherclass_file + "_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request) + "";
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request));
				refresh = GetSession("refresh", request);
				location_name = GetSession("config_inventory_location_name", request) + "";
				config_inventory_current_stock = "1";
				BranchAccess = GetSession("BranchAccess", request) + "";
				ExeAccess = GetSession("ExeAccess", request) + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				if (deleteB.contains("&#40") && deleteB.contains("&#41")) {
					deleteB = deleteB.replace("&#40;", "(");
					deleteB = deleteB.replace("&#41;", ")");
				}
				msg = PadQuotes(request.getParameter("msg"));
				// voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				// vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				// voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucher_dcr_request_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_request_id")));
				voucher_dcr_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_id")));
				voucher_grn_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_id")));
				if (voucher_id.equals("0") && !voucher_grn_id.equals("0")) {
					voucher_id = voucher_grn_id;
				}
				voucher_grn_return_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_return_id")));

				if (voucher_id.equals("0") && !voucher_grn_return_id.equals("0")) {
					voucher_id = voucher_delnote_id;
				}
				voucher_delnote_id = CNumeric(PadQuotes(request.getParameter("voucher_delnote_id")));
				if (voucher_id.equals("0") && !voucher_delnote_id.equals("0")) {
					voucher_id = voucher_delnote_id;
				}
				customer_add = PadQuotes(request.getParameter("customer_add"));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));

				QueryString = PadQuotes(request.getQueryString());

				empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);

				PopulateConfigDetails();

				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				// For Generating session each time
				session_id = PadQuotes(request.getParameter("txt_session_id"));

				if (customer_add.equals("yes")) {
					customer_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
							+ " FROM  " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + branch_id);
					voucher_customer_id = PadQuotes(request.getParameter("voucher_customer_id"));
					voucher_contact_id = CNumeric(PadQuotes(request.getParameter("voucher_contact_id")));
					voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("voucher_rateclass_id	")));
					voucher_paydays_id = PadQuotes(request.getParameter("voucher_paydays_id"));
				}
				// -----------End Of Code
				if (add.equals("yes")) {
					status = "Add";
					display = "none";
					if (!addB.equals("yes")) {
						voucher_date = ToShortDate(kknow());
						voucherdate = strToShortDate(voucher_date);
						voucher_promisedate = DateToShortDate(kknow());
						voucher_emp_id = emp_id;
						voucher_active = "1";
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								double math = Math.random();
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						if (!voucher_id.equals("0")) {
							PopulateFields(request, response);
						}

						BeforeAddRoutine(request);
						if (!voucher_customer_id.equals("0")) {
							gst_type = GetGstType(voucher_customer_id, branch_id, comp_id);
						}
						if (!refresh.equals("no")) {
							CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
						}

					} else {
						if (ReturnPerm(comp_id, "emp_acc_voucher_add," + "emp_acc_purchase_return_access," + "emp_acc_sales_return_add", request).equals("1")) {

							GetValues(request, response);

							AddFields(request);

							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=" + vouchertype_name + " added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete " + vouchertype_name)) {
						if (vouchertype_id.equals("24") || vouchertype_id.equals("117")
								|| vouchertype_id.equals("23") || vouchertype_id.equals("4")) {
							StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " WHERE 1=1"
									+ " AND (voucher_grn_return_id = " + voucher_id + ""
									+ " || voucher_dcr_request_id = " + voucher_id + ""
									+ " || voucher_dcr_id = " + voucher_id + ")";
							// SOP("StrSql===" + StrSql);
							if (!ExecuteQuery(StrSql).equals("")) {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=" + vouchertype_name + " can't be updated!"));
							}
						}
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						PopulateFields(request, response);
						if (!refresh.equals("no")) {
							CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
						}
					} else if (updateB.equals("yes") && !deleteB.equals("Delete " + vouchertype_name)) {
						if (ReturnPerm(comp_id, "emp_acc_voucher_edit," + "emp_acc_purchase_return_edit," + "emp_acc_sales_return_edit", request).equals("1")) {

							GetValues(request, response);

							UpdateFields(request, response);

							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=" + vouchertype_name + " updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

					} else if (deleteB.equals("Delete " + vouchertype_name)) {
						if (ReturnPerm(comp_id, "emp_acc_voucher_delete," + "emp_acc_purchase_return_delete," + "emp_acc_sales_return_delete", request).equals("1")) {
							response.sendRedirect(AccessDenied());
							// GetValues(request, response);
							// DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=" + vouchertype_name + " deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		para = PadQuotes(request.getParameter("para"));

		voucher_no = PadQuotes(request.getParameter("txt_voucher_no"));
		voucher_grn_return_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_grn_return_id")));
		voucher_dcr_request_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_dcr_request_id")));
		voucher_dcr_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_dcr_id")));

		contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
		customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
		so_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		so_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
		vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_location_id")));
		SOP("vouchertrans_location_id==111=" + vouchertrans_location_id);

		branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
		voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_rateclass_id")));
		voucher_lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		location_name = PadQuotes(request.getParameter("txt_location_name"));
		parentvoucher = PadQuotes(request.getParameter("txt_parentvoucher"));

		if (!customer_id.equals("0") && !customer_id.equals(so_customer_id)) {
			so_customer_id = customer_id;
			voucher_customer_id = customer_id;
		}
		if (!contact_id.equals("0") && !contact_id.equals(so_contact_id)) {
			so_contact_id = contact_id;
			voucher_contact_id = contact_id;
		}

		if (!customer_id.equals("0") && !contact_id.equals("0")) {
			so_customer_name = PadQuotes(request.getParameter("span_customer_name"));
			so_contact_name = PadQuotes(request.getParameter("span_contact_name"));
			so_customer_name = unescapehtml(so_customer_name);
			so_contact_name = unescapehtml(so_contact_name);
		}

		if (vouchertype_id.equals("23") || vouchertype_id.equals("4") || vouchertype_id.equals("116")) {
			voucher_customer_id = PadQuotes(request.getParameter("accountingcustomer"));
		} else {
			voucher_customer_id = PadQuotes(request.getParameter("accountingsupplier"));
		}
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		if (voucher_customer_id.equals("0")) {
			voucher_customer_id = PadQuotes(request.getParameter("span_customer_id"));
		}
		if (voucher_contact_id.equals("0")) {
			voucher_contact_id = contact_id;
		}

		voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));

		voucher_paymentdate = PadQuotes(request.getParameter("txt_voucher_payment_date"));

		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_custref_no = PadQuotes(request.getParameter("txt_voucher_custref_no"));
		voucher_custref_date = PadQuotes(request.getParameter("txt_voucher_custref_date"));
		voucher_prev_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_voucher_prev_grandtotal")));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize")));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));

		if (voucher_active.equals("1") && vouchertype_defaultauthorize.equals("1")) {
			if (voucher_no.equals("0")) {
				voucher_no = getVoucherNo(branch_id, vouchertype_id, "0", "0");
			}
			voucher_authorize = "1";
		} else if (CNumeric(voucher_active).equals("0")) {
			voucher_authorize = "0";
		}
		if (voucher_authorize.equals("1") && voucher_authorize_id.equals("0") && voucher_authorize_time.equals("")) {
			voucher_authorize_id = emp_id;
			voucher_authorize_time = ToLongDate(kknow());
		}

		if (update.equals("yes")) {
			checkbranch = PadQuotes(request.getParameter("hid_check_branch"));
			check_voucher_no = PadQuotes(request.getParameter("hid_check_voucherno"));
		}
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}

		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		voucher_entry_by = PadQuotes(request.getParameter("entry_by"));
		voucher_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

		voucher_billing_add = PadQuotes(request.getParameter("txt_voucher_billing_add"));
		voucher_consignee_add = PadQuotes(request.getParameter("txt_voucher_consignee_add"));

		voucher_transporter = PadQuotes(request.getParameter("txt_voucher_transporter"));
		voucher_gatepass = PadQuotes(request.getParameter("txt_voucher_gatepass"));
		voucher_lrno = PadQuotes(request.getParameter("txt_voucher_lrno"));
		voucher_driver_no = PadQuotes(request.getParameter("txt_voucher_driver_no"));
		voucher_tempo_no = PadQuotes(request.getParameter("txt_voucher_tempo_no"));
		voucher_cashdiscount = CNumeric(PadQuotes(request.getParameter("txt_voucher_cashdiscount")));
		voucher_turnoverdisc = CNumeric(PadQuotes(request.getParameter("txt_voucher_turnoverdisc")));

		voucher_downpayment = CNumeric(PadQuotes(request.getParameter("txt_voucher_downpayment")));
		voucher_promisedate = PadQuotes(request.getParameter("txt_voucher_promise_date"));
		voucher_delivereydate = PadQuotes(request.getParameter("txt_voucher_delivery_date"));
		voucher_open = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_open")));
		voucher_critical = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_critical")));

		voucher_special = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_special")));

		voucher_delstatus_id = PadQuotes(request.getParameter("dr_voucher_delstatus_id"));
		PopulateConfigDetails();
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";

		if (voucherdate.equals("")) {
			msg += "<br>Enter Sales Order Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Sales Order Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter valid Sales Order Date!";
			}
		}

		// if (CNumeric(voucher_customer_id).equals("0")) {
		// msg += "<br>Select Customer!";
		// }

		if (CNumeric(voucher_contact_id).equals("0")) {
			msg += "<br>Select Contact!";
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

		if (comp_module_inventory.equals("1")
				// && config_inventory_current_stock.equals("1")
				&& vouchertrans_location_id.equals("0")) {
			msg += "<br>Select Location!";
		}

		if (vouchertype_id.equals("114")) {
			if (!voucher_paymentdate.equals("")) {
				if (!isValidDateFormatShort(voucher_paymentdate)) {
					msg += "<br>Enter valid Payment Date!";
				} else {
					voucher_payment_date = ConvertShortDateToStr(voucher_paymentdate);
				}
			}
		}

		if (vouchertype_billing_add.equals("1")) {
			if (voucher_billing_add.equals("")) {
				msg += "<br>Enter Billing Address!";
			}
			if (vouchertype_consignee_add.equals("1")) {
				if (voucher_consignee_add.equals("")) {
					msg += "<br>Enter Consignee Address!";
				}
			}

		}

		if (vouchertype_ref_no_enable.equals("1")) {
			if (vouchertype_ref_no_mandatory.equals("1")) {
				if (voucher_ref_no.equals("")) {
					msg += "<br>Enter Reference No.!";
				} else {
					if (voucher_ref_no.length() < 2) {
						msg += "<br>Reference No. should be atleast Two Digits! ";
					}
				}
			}

			if (!branch_id.equals("0")) {
				StrSql = "SELECT voucher_ref_no FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_branch_id = " + branch_id + " "
						+ " AND voucher_ref_no = '" + voucher_ref_no + "'"
						+ " AND voucher_id != " + voucher_id + ""
						+ " AND voucher_vouchertype_id = " + vouchertype_id;
				// SOP("StrSql=123=="+StrSqlBreaker(StrSql));
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Reference No. found!";
				}
			}
		}
		// SOP("status=====" + status);
		// SOP("vouchertype_defaultauthorize=====" + vouchertype_defaultauthorize);
		// SOP("vouchertype_authorize====" + vouchertype_authorize);
		// SOP("voucher_no========" + voucher_no);
		// if (vouchertype_defaultauthorize.equals("1")) {
		if (status.equals("Update") && (voucher_authorize.equals("1") || !CNumeric(voucher_no).equals("0"))) {
			if (voucher_no.equals("") || voucher_no.equals("0")) {
				msg += "<br>Enter Voucher No.!";
			} else if (!CNumeric(voucher_no).equals("0")) {
				StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE 1=1"
						+ " AND voucher_branch_id = " + branch_id
						+ " AND voucher_vouchertype_id = " + vouchertype_id
						+ " AND voucher_no = " + voucher_no
						+ " AND voucher_id !=" + voucher_id;
				// SOP("StrSql====" + StrSql);
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Voucher No. found!";
				}
			}
		} else {
			if (status.equals("Add") && vouchertype_defaultauthorize.equals("1")) {
				voucher_no = getVoucherNo(branch_id, vouchertype_id, "0", "0");
			}
		}

		if (!voucher_delivereydate.equals("")) {
			if (isValidDateFormatShort(voucher_delivereydate)) {
				voucher_delivery_date = ConvertShortDateToStr(voucher_delivereydate);
				if (Long.parseLong(voucher_date) > Long.parseLong(voucher_delivery_date)) {
					msg += "<br>Delivery Date must be greater than or equal to Sales Order date!";
				}
			} else {
				msg += "<br>Enter Valid Delivery Date!";
			}
		}
		//

		// if (!branch_id.equals("0")) {
		// if (!voucher_emp_id.equals("0")) {
		// StrSql = "SELECT COALESCE((SELECT team_postsf_emp_id"
		// + " FROM  " + compdb(comp_id) + "axela_sales_team"
		// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
		// + " WHERE team_branch_id = " + branch_id + ""
		// + " AND teamtrans_emp_id = " + voucher_emp_id + ""
		// + " AND team_postsf_emp_id !=0" + " LIMIT 1), 0)";
		// postsf_crm_emp_id = ExecuteQuery(StrSql);
		// // SOP("StrSql 111team_postsf_emp_id= " + postsf_crm_emp_id);
		// StrSql = "SELECT COALESCE((SELECT team_emp_id"
		// + " FROM  " + compdb(comp_id) + "axela_sales_team"
		// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
		// + " WHERE team_branch_id = " + branch_id + ""
		// + " AND teamtrans_emp_id = " + voucher_emp_id + ""
		// + " LIMIT 1), 0)";
		// // SOP("StrSql manager= " + StrSqlBreaker(StrSql));
		// postsf_manager_id = ExecuteQuery(StrSql);
		// }
		// }
		msg = msgChk + msg;
	}
	public void AddFields(HttpServletRequest request) throws Exception {
		CachedRowSet crs = null;
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "SELECT COALESCE(SUM(cart_amount),0)"
						+ "-(SELECT COALESCE(SUM(discount.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart discount" + " WHERE 1=1"
						+ " AND discount.cart_emp_id = " + emp_id + ""
						+ " AND discount.cart_session_id = " + session_id + ""
						+ " AND discount.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND discount.cart_discount = 1)"
						+ " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax"
						+ " WHERE 1=1"
						+ " AND tax.cart_emp_id = " + emp_id + ""
						+ " AND tax.cart_session_id = " + session_id + ""
						+ " AND tax.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND tax.cart_tax = 1)"
						+ " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart billsundry"
						+ " WHERE 1=1"
						+ " AND billsundry.cart_emp_id = " + emp_id + ""
						+ " AND billsundry.cart_session_id = " + session_id + ""
						+ " AND billsundry.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND billsundry.cart_rowcount = 0"
						+ " AND billsundry.cart_option_id = 0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1"
						+ " AND cart_emp_id = " + emp_id + ""
						+ " AND cart_session_id = " + session_id + ""
						+ " AND cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND cart_item_id != 0"
						+ " AND cart_discount = 0"
						+ " AND cart_tax = 0";
				voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher" + " ("
						+ " voucher_vouchertype_id,"
						+ " voucher_no,"
						+ " voucher_branch_id,"
						+ " voucher_location_id,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_lead_id,"
						+ " voucher_dcr_request_id,"
						+ " voucher_dcr_id,"
						+ " voucher_grn_return_id,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_emp_id,"
						+ " voucher_downpayment,"
						+ " voucher_payment_date,"
						+ " voucher_promise_date,"
						+ " voucher_delivery_date,"
						+ " voucher_delnote_id,"
						+ " voucher_open,"
						+ " voucher_critical,"
						+ " voucher_special,"
						+ " voucher_billing_add,"
						+ " voucher_consignee_add,"
						+ " voucher_transporter,"
						+ " voucher_gatepass,"
						+ " voucher_lrno,"
						+ " voucher_driver_no,"
						+ " voucher_tempo_no,"
						+ " voucher_cashdiscount,"
						+ " voucher_turnoverdisc,"
						+ " voucher_ref_no,"
						+ " voucher_custref_no,"
						+ " voucher_custref_date,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_notes,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES"
						+ " (" + vouchertype_id + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + branch_id + ","
						+ " " + vouchertrans_location_id + ","
						+ " '" + voucher_date + "',"
						+ " " + Double.parseDouble(voucher_grandtotal) + ","
						+ " " + voucher_lead_id + ","
						+ " " + voucher_dcr_request_id + ","
						+ " " + voucher_dcr_id + ","
						+ " " + voucher_grn_return_id + ","
						+ " " + voucher_customer_id + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_emp_id + ","
						+ " " + voucher_downpayment + ","
						+ " '" + voucher_payment_date + "',"
						+ " '" + voucher_promise_date + "',"
						+ " '" + voucher_delivery_date + "',"
						+ " " + voucher_delnote_id + ","
						+ " " + voucher_open + ","
						+ " " + voucher_critical + ","
						+ " '" + voucher_special + "',"
						+ " '" + voucher_billing_add + "',"
						+ " '" + voucher_consignee_add + "',"
						+ " '" + voucher_transporter + "',"
						+ " '" + voucher_gatepass + "',"
						+ " '" + voucher_lrno + "',"
						+ " '" + voucher_driver_no + "',"
						+ " '" + voucher_tempo_no + "',"
						+ " " + voucher_cashdiscount + ","
						+ " " + voucher_turnoverdisc + ","
						+ " '" + voucher_ref_no + "',"
						+ " '" + voucher_custref_no + "',"
						+ " '" + ConvertShortDateToStr(voucher_custref_date) + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " '" + vouchertype_terms + "' ,"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_notes + "',"
						+ " " + emp_id + "," + " " + ToLongDate(kknow()) + ")";

				// SOP("StrSQl=voucher=" + CNumeric(voucher_no));
				// SOP("StrSQl=voucher=" + StrSqlBreaker("voucher No" + voucher_no));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					voucher_id = rs1.getString(1);
					// SOP("voucher_id===" + voucher_id);
					vouchertrans_voucher_id = voucher_id;
				}
				rs1.close();

				AddItemFields(request);

				// AddPaymentTrack();

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1"
						+ " AND cart_emp_id = " + emp_id + ""
						+ " AND cart_session_id = " + session_id + ""
						+ " AND cart_vouchertype_id = " + vouchertype_id + "";
				stmttx.addBatch(StrSql);

				// for updating the customer current balance
				stmttx.executeBatch();

				conntx.commit();

				// // //customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}
				// // item stock
				if (msg.equals("") && comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& vouchertype_id.equals("23")) {
					// SOP("coming222");
					CalcCurrentStockThread calccurrentstockthread =
							new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
					// CalCurrentStockVoucher(voucher_id, comp_id,
					// vouchertype_id);
				}

				// / mail sending
				if (config_email_enable.equals("1")
						&& vouchertype_email_enable.equals("1")
						&& vouchertype_email_auto.equals("1")
						&& comp_email_enable.equals("1")) {
					Voucher_Email Voucher_Email_obj = new Voucher_Email();
					Voucher_Email_obj.voucher_id = voucher_id;
					Voucher_Email_obj.config_admin_email = config_admin_email;
					Voucher_Email_obj.SendEmail(comp_id);
					Voucher_Email_obj = null;
				}

				// sms sending
				if (config_sms_enable.equals("1")
						&& vouchertype_sms_enable.equals("1")
						&& vouchertype_sms_auto.equals("1")
						&& comp_sms_enable.equals("1")) {
					Voucher_SMS Voucher_SMS_obj = new Voucher_SMS();
					Voucher_SMS_obj.voucher_id = voucher_id;

					contact_mobile = ExecuteQuery("SELECT contact_mobile1"
							+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
							+ " WHERE contact_id = " + voucher_contact_id);
					Voucher_SMS_obj.SendSMS(voucher_active, comp_id);
					Voucher_SMS_obj = null;
				}

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void AddItemFields(HttpServletRequest request)
			throws SQLException {
		try {
			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
			stmttx.addBatch(StrSql);

			StrSql = "SELECT"
					+ " " + voucher_id + ","
					+ " cart_multivoucher_id,"
					+ " cart_customer_id,"
					+ " " + vouchertrans_location_id + ","
					+ " cart_item_id,"
					+ " cart_discount,"
					+ " cart_discount_perc,"
					+ " cart_tax,"
					+ " cart_tax_id,"
					+ " cart_rowcount,"
					+ " cart_option_id,"
					+ " cart_price,"
					+ " cart_netprice,"
					+ " cart_delivery_date,"
					+ " cart_convfactor,"
					+ " cart_qty,"
					+ " cart_truckspace,"
					+ " cart_unit_cost,"
					+ " cart_amount,"
					+ " cart_alt_qty,"
					+ " cart_alt_uom_id,"
					+ " cart_time,"
					+ " cart_dc"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id + ""
					+ " AND cart_emp_id = " + emp_id + ""
					+ " AND cart_session_id = " + session_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_delivery_date,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " " + StrSql + "";
			// SOP("StrSql==cart--vouchertrans==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// party entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_delivery_date,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					+ " 0,"
					+ " " + voucher_customer_id + ","
					+ " " + vouchertrans_location_id + ","
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					// + " " + Math.round(Double.parseDouble(voucher_netprice)) + ","
					+ " 0,"
					+ " '',"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " " + Double.parseDouble(voucher_grandtotal)
					+ ","
					+ " 0,"
					+ " 0,"
					+ "'" + ToLongDate(kknow()) + "',";
			// for sales return
			if (vouchertype_id.equals("116") || vouchertype_id.equals("23") || vouchertype_id.equals("4")) {
				StrSql += " '0'" + " )";
				// for so and sales invoice
			} else if (vouchertype_id.equals("102") || vouchertype_id.equals("117") || vouchertype_id.equals("24")) {
				StrSql += " '1'" + " )";

			}
			// SOP("StrSql==sup==" + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// Deleting Item Serial no. FROM axela_inventory_stockserial table
			// DeleteStockSerial();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// public void DeleteStockSerial() throws SQLException {
	// try {
	// StrSql = "SELECT cart_item_id, cart_item_serial"
	// + " FROM  "+compdb(comp_id)+"axela_acc_cart"
	// + " WHERE 1=1"
	// + " cart_session_id = " + session_id
	// + " AND cart_emp_id = " + emp_id;
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// StrSql = "DELETE FROM  "+compdb(comp_id)+"axela_inventory_stockserial"
	// + " WHERE stockserial_item_id = "
	// + crs.getString("cart_item_id") + ""
	// + " AND stockserial_serial_no = '"
	// + crs.getString("cart_item_serial") + "'"
	// + " AND stockserial_location_id = "
	// + vouchertrans_location_id + "";
	// stmttx.execute(StrSql);
	// }
	// crs.close();
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("connection is closed...");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + e);
	// }
	// }

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_no,"
					+ " COALESCE(voucher_so_id, 0) AS voucher_so_id,"
					+ " COALESCE(voucher_delnote_id, 0) AS voucher_delnote_id,"
					+ " COALESCE(voucher_dcr_request_id, 0) AS voucher_dcr_request_id,"
					+ " COALESCE(voucher_dcr_id, 0) AS voucher_dcr_id,"
					+ " COALESCE(voucher_grn_return_id, 0) AS voucher_grn_return_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " voucher_branch_id, voucher_location_id, voucher_date, voucher_amount,"
					+ " voucher_customer_id, voucher_contact_id, voucher_downpayment,"
					+ " voucher_emp_id, voucher_quote_id, voucher_so_id, voucher_invoice_id,"
					+ " voucher_payment_date, voucher_promise_date, voucher_delivery_date,"
					+ " voucher_delstatus_id, voucher_open, voucher_critical, voucher_special,"
					+ " voucher_rateclass_id, voucher_billing_add, voucher_consignee_add,"
					+ " voucher_transporter, voucher_gatepass, voucher_lrno, voucher_driver_no,"
					+ " voucher_tempo_no, voucher_cashdiscount, voucher_turnoverdisc, voucher_ref_no,"
					+ " voucher_custref_no, voucher_custref_date, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, voucher_quote_id, voucher_enquiry_id, location_name, voucher_notes,"
					+ " voucher_authorize,voucher_authorize_id,voucher_authorize_time"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_id = voucher_location_id"
					+ " WHERE voucher_id = " + voucher_id
					// + BranchAccess
					+ ExeAccess + "" + " LIMIT 1";
			// SOP("StrSql=popu==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					voucher_so_id = crs.getString("voucher_so_id");
					if (!vouchertype_id.equals("23")) {
						voucher_delnote_id = crs.getString("voucher_delnote_id");
					}
					voucher_dcr_request_id = crs.getString("voucher_dcr_request_id");
					voucher_grn_return_id = crs.getString("voucher_grn_return_id");
					voucher_dcr_id = crs.getString("voucher_dcr_id");
					branch_id = crs.getString("voucher_branch_id");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");
					checkbranch = branch_id;
					branch_name = crs.getString("branch_name");
					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucherdate = strToShortDate(crs.getString("voucher_date"));
					voucher_payment_date = crs.getString("voucher_payment_date");
					voucher_paymentdate = strToShortDate(voucher_payment_date);

					voucher_promise_date = crs.getString("voucher_promise_date");
					voucher_promisedate = strToShortDate(voucher_promise_date);
					voucher_delivery_date = crs.getString("voucher_delivery_date");
					voucher_delivereydate = strToShortDate(voucher_delivery_date);
					voucher_critical = crs.getString("voucher_critical");
					voucher_special = crs.getString("voucher_special");
					voucher_open = crs.getString("voucher_open");
					// voucher_delstatus_id =
					// crs.getString("voucher_delstatus_id");
					voucher_downpayment = crs.getString("voucher_downpayment");
					voucher_billing_add = crs.getString("voucher_billing_add");
					voucher_consignee_add = crs.getString("voucher_consignee_add");

					voucher_transporter = crs.getString("voucher_transporter");
					voucher_gatepass = crs.getString("voucher_gatepass");
					voucher_lrno = crs.getString("voucher_lrno");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_cashdiscount = crs.getString("voucher_cashdiscount");
					voucher_turnoverdisc = crs.getString("voucher_turnoverdisc");

					voucher_grandtotal = crs.getString("voucher_amount");
					voucher_enquiry_id = crs.getString("voucher_enquiry_id");
					voucher_quote_id = crs.getString("voucher_quote_id");
					voucher_invoice_id = crs.getString("voucher_invoice_id");
					voucher_so_id = crs.getString("voucher_so_id");
					vouchertrans_location_id = crs.getString("voucher_location_id");
					location_name = crs.getString("location_name");
					voucher_quote_id = crs.getString("voucher_quote_id");
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_custref_no = crs.getString("voucher_custref_no");
					voucher_custref_date = strToShortDate(crs.getString("voucher_custref_date"));
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_notes = crs.getString("voucher_notes");
					voucher_entry_id = crs.getString("voucher_entry_id");
					if (!voucher_entry_id.equals("0")) {
						voucher_entry_by = Exename(comp_id, Integer.parseInt(voucher_entry_id));
					}
					entry_date = strToLongDate(crs.getString("voucher_entry_date"));
					voucher_modified_id = crs.getString("voucher_modified_id");
					if (!voucher_modified_id.equals("0")) {
						voucher_modified_by = Exename(comp_id, Integer.parseInt(voucher_modified_id));
					}
					modified_date = strToLongDate(crs.getString("voucher_modified_date"));
					gst_type = GetGstType(voucher_customer_id, branch_id, comp_id);
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid " + vouchertype_name + "!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void UpdateFields(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "SELECT COALESCE(SUM(cart_amount),0)"
						+ "-(SELECT COALESCE(SUM(discount.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart discount" + " WHERE 1=1"
						+ " AND discount.cart_emp_id = " + emp_id + ""
						+ " AND discount.cart_session_id = " + session_id + ""
						+ " AND discount.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND discount.cart_discount = 1)"
						+ " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax"
						+ " WHERE 1=1"
						+ " AND tax.cart_emp_id = " + emp_id + ""
						+ " AND tax.cart_session_id = " + session_id + ""
						+ " AND tax.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND tax.cart_tax = 1)"
						+ " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart billsundry"
						+ " WHERE 1=1"
						+ " AND billsundry.cart_emp_id = " + emp_id + ""
						+ " AND billsundry.cart_session_id = " + session_id + ""
						+ " AND billsundry.cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND billsundry.cart_rowcount = 0"
						+ " AND billsundry.cart_option_id = 0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1"
						+ " AND cart_emp_id = " + emp_id + ""
						+ " AND cart_session_id = " + session_id + ""
						+ " AND cart_vouchertype_id = " + vouchertype_id + ""
						+ " AND cart_item_id != 0"
						+ " AND cart_discount = 0"
						+ " AND cart_tax = 0";
				// SOP("StrSql==grandtotal=" + StrSql);
				voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));
				// SOP("voucher_grandtotal =111=== " + voucher_grandtotal);

				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_voucher"
						+ " SET voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_vouchertype_id = " + vouchertype_id + ","
						+ " voucher_branch_id = " + branch_id + ","
						+ " voucher_location_id = " + vouchertrans_location_id + ","
						+ " voucher_date = " + voucher_date + ","
						+ " voucher_amount = " + Double.parseDouble(voucher_grandtotal) + ","
						+ " voucher_lead_id = " + voucher_lead_id + ","
						+ " voucher_customer_id = " + voucher_customer_id + ","
						+ " voucher_contact_id = " + voucher_contact_id + ","
						+ " voucher_emp_id = " + voucher_emp_id + ","
						+ " voucher_downpayment = " + voucher_downpayment + ","
						// + " voucher_delstatus_id = " + voucher_delstatus_id +
						// ","
						+ " voucher_payment_date= '" + voucher_payment_date + "',"
						+ " voucher_promise_date = '" + voucher_promise_date + "',"
						+ " voucher_delivery_date = '" + voucher_delivery_date + "',"
						+ " voucher_open = '" + voucher_open + "',"
						+ " voucher_critical = '" + voucher_critical + "',"
						+ " voucher_special = '" + voucher_special + "',"
						+ " voucher_rateclass_id = " + voucher_rateclass_id + ","
						+ " voucher_billing_add= '" + voucher_billing_add + "',"
						+ " voucher_consignee_add= '" + voucher_consignee_add + "',"
						+ " voucher_transporter= '" + voucher_transporter + "',"
						+ " voucher_gatepass= '" + voucher_gatepass + "',"
						+ " voucher_lrno= '" + voucher_lrno + "',"
						+ " voucher_driver_no= '" + voucher_driver_no + "',"
						+ " voucher_tempo_no= '" + voucher_tempo_no + "',"
						+ " voucher_cashdiscount = " + voucher_cashdiscount + ","
						+ " voucher_turnoverdisc = " + voucher_turnoverdisc + ","
						// if (config_sales_voucher_refno.equals("1")) {
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						// }
						+ " voucher_custref_no = '" + voucher_custref_no + "',"
						+ " voucher_custref_date = '" + ConvertShortDateToStr(voucher_custref_date) + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_notes = '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql===up===" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);

				// Copy items FROM axela_acc_cart to axela_vouchertrans
				AddItemFields(request);

				// Delete cart items

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE  cart_session_id = " + session_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_vouchertype_id=" + vouchertype_id;
				stmttx.addBatch(StrSql);

				// to UPDATE "+compdb(comp_id)+"the Customer Current Balance
				stmttx.executeBatch();

				conntx.commit();

				// / customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// item stock
				SOP("vouchertype_id==" + vouchertype_id);
				if (msg.equals("") && comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& vouchertype_id.equals("23")) {
					CalcCurrentStockThread calccurrentstockthread =
							new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
					// CalCurrentStockVoucher(voucher_id, comp_id,
					// vouchertype_id);
				}

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void DeleteFields(HttpServletResponse response) throws Exception {
		// CheckPageperm(null);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				updateQuery("UPDATE  " + compdb(comp_id) + "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");
				// UPDATE "+compdb(comp_id)+"the customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// Delete all the cart items for the current session
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_session_id = " + session_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_vouchertype_id=" + vouchertype_id;
				stmttx.addBatch(StrSql);

				// Delete all the items for the current Invoice
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Finally Delete the Invoice
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

				// item stock
				if (msg.equals("") && comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& vouchertype_id.equals("23")) {
					CalcCurrentStockThread calccurrentstockthread =
							new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
					// CalCurrentStockVoucher(voucher_id, comp_id,
					// vouchertype_id);
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	void CheckPageperm(HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE voucher_id = " + voucher_id + BranchAccess + "";

			if (ExecuteQuery(StrSql).equals("")) {
				response.sendRedirect("../portal/error.jsp?msg=Access denied!");
				return;
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateCustomerDetails() {
		try {
			if (!voucher_customer_id.equals("0")) {
				StrSql = "SELECT  customer_rateclass_id, currentbal_amount, customer_credit_limit,"
						+ " COALESCE(customer_address, '') AS customer_address,"
						+ " COALESCE(city_name, '') AS city_name,"
						+ " COALESCE(customer_pin, '') AS customer_pin,"
						+ " COALESCE(state_name, '') AS state_name,"
						+ " COALESCE(country_name, '') AS country_name"
						+ " COALESCE(customer_landmark, '') AS customer_landmark,"
						+ " COALESCE(zone_name, '') AS zone_name"
						+ " FROM  " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_country ON country_id = state_country_id"
						+ " LEFT JOIN  " + compdb(comp_id) + "axela_zone ON zone_id = customer_zone_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_currentbal ON currentbal_customer_id = customer_id"
						+ " AND currentbal_company_id =" + comp_id
						+ " WHERE customer_id = " + voucher_customer_id + "";
				// SOP("StrSql=PopulateCustomerDetails==" + StrSql);
				CachedRowSet crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					currentbal_amount = crs1.getString("currentbal_amount");
					customer_credit_limit = crs1.getString("customer_credit_limit");
					voucher_billing_add = crs1.getString("customer_address");

					if (!voucher_billing_add.equals("")) {
						voucher_billing_add = crs1.getString("customer_address");
						if (!crs1.getString("city_name").equals("")) {
							voucher_billing_add += ", " + crs1.getString("city_name");
						}
						if (!crs1.getString("customer_pin").equals("")) {
							voucher_billing_add += " - " + crs1.getString("customer_pin");
						}
						if (!crs1.getString("state_name").equals("")) {
							voucher_billing_add += ", " + crs1.getString("state_name");
						}
						if (!crs1.getString("country_name").equals("")) {
							voucher_billing_add += ", " + crs1.getString("country_name") + ".";
						}

						if (!crs1.getString("customer_landmark").equals("")) {
							voucher_billing_add += "\nLandmark: " + crs1.getString("customer_landmark");
						}
					}
				}
				crs1.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM  " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT voucherclass_file, voucherclass_acc, vouchertype_name, vouchertype_mobile, "
					+ " vouchertype_email, vouchertype_dob, vouchertype_dnd,"
					+ " vouchertype_affects_inventory, vouchertype_ref_no_enable,"
					+ " vouchertype_ref_no_mandatory, vouchertype_terms,"
					+ " vouchertype_defaultauthorize, vouchertype_authorize,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					+ " COALESCE(vouchertype_email_sub, '') AS vouchertype_email_sub,"
					+ " COALESCE(vouchertype_email_format, '') AS vouchertype_email_format,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					+ " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable,"
					+ " vouchertype_billing_add, vouchertype_consignee_add,"
					+ " vouchertype_transporter, vouchertype_gatepass, vouchertype_lrno,"
					+ " vouchertype_tempo_no, vouchertype_driver_no,"
					+ " vouchertype_cashdiscount, vouchertype_turnoverdisc"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN  " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id,"
					+ "  " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + ""
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=congif==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucherclass_file = crs.getString("voucherclass_file");
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_mobile = crs.getString("vouchertype_mobile");
				vouchertype_email = crs.getString("vouchertype_email");
				vouchertype_dob = crs.getString("vouchertype_dob");
				vouchertype_dnd = crs.getString("vouchertype_dnd");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				// voucher_authorize = vouchertype_defaultauthorize;
				vouchertype_affects_inventory = crs.getString("vouchertype_affects_inventory");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_auto = crs.getString("vouchertype_email_auto");
				vouchertype_email_sub = crs.getString("vouchertype_email_sub");
				vouchertype_email_format = crs.getString("vouchertype_email_format");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
				vouchertype_sms_format = crs.getString("vouchertype_sms_format");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				vouchertype_billing_add = crs.getString("vouchertype_billing_add");
				vouchertype_consignee_add = crs.getString("vouchertype_consignee_add");
				vouchertype_transporter = crs.getString("vouchertype_transporter");
				vouchertype_gatepass = crs.getString("vouchertype_gatepass");
				vouchertype_lrno = crs.getString("vouchertype_lrno");
				vouchertype_driver_no = crs.getString("vouchertype_driver_no");
				vouchertype_tempo_no = crs.getString("vouchertype_tempo_no");
				vouchertype_cashdiscount = crs.getString("vouchertype_cashdiscount");
				vouchertype_turnoverdisc = crs.getString("vouchertype_turnoverdisc");
				vouchertype_terms = crs.getString("vouchertype_terms");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddPostSFDaysFields(String voucher_id,
			String postsf_crm_emp_id, String voucher_emp_id,
			String voucher_delivereddate, String emp_id, String branch_id)
			throws SQLException {
		try {
			StrSql = "SELECT "
					+ voucher_id + ","
					+ " IF(postsfdays_exe_type = 1, " + voucher_emp_id + ","
					+ " IF(postsfdays_exe_type = 2, " + postsf_crm_emp_id + ", " + postsf_manager_id + ")),"
					+ " 0,"
					+ " 0,"
					+ " '',"
					+ " DATE_FORMAT(DATE_ADD(CONCAT(SUBSTR('" + voucher_delivereddate + "', 1, 8), '100000'),"
					+ " INTERVAL (postsfdays_daycount-0) DAY), '%Y%m%d%H%i%s'),"
					+ " postsfdays_id," + " "
					+ emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0"
					+ " FROM  " + compdb(comp_id) + "axela_sales_voucher_postsfdays"
					+ " WHERE postsfdays_branch_id = " + branch_id + "";
			// SOP("StrSql sel= " + StrSqlBreaker(StrSql));

			StrSql = " INSERT INTO " + compdb(comp_id) + " axela_sales_voucher_postsf"
					+ " (postsf_voucher_id,"
					+ " postsf_crm_emp_id,"
					+ " postsf_postsffollowuptype_id,"
					+ " postsf_postsffeedbacktype_id,"
					+ " postsf_desc,"
					+ " postsf_followup_time,"
					+ " postsf_postsfdays_id,"
					+ " postsf_entry_id,"
					+ " postsf_entry_time,"
					+ " postsf_trigger)" + StrSql;
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateItemCatPop() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cat_id ,cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
					+ " GROUP BY cat_id"
					+ " ORDER BY cat_rank, cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Keyword</option>\n");
			Str.append("<option value=-1").append(StrSelectdrop("-1", item_itemgroup_id)).append(">").append("Code");
			Str.append("</option>\n");
			Str.append("<option value=-2").append(StrSelectdrop("-2", item_itemgroup_id)).append(">").append("Name");
			Str.append("</option>\n");

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), item_itemgroup_id));
				Str.append(">").append(crs.getString("cat_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getVoucherNo(String branch_id, String vouchertype_id, String voucher_no, String voucher_id) {
		StrSql = "SELECT COALESCE(MAX(voucher_no),0)+1"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE 1=1"
				+ " AND voucher_branch_id = " + branch_id
				+ " AND voucher_vouchertype_id = " + vouchertype_id;

		if (!voucher_no.equals("0")) {
			StrSql += " AND voucher_no = " + voucher_no;
		}
		if (!voucher_id.equals("0")) {
			StrSql += " AND voucher_id != " + voucher_id;
		}
		// SOP("getVoucherNo==StrSql===" + StrSql);
		StrSql = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
		return StrSql.toString();
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name=\"dr_contact_id\" class=\"form-control\" id=\"dr_contact_id\" >");
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " customer_city_id"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = " + CNumeric(customer_id)
					+ " GROUP BY contact_id"
					+ " ORDER BY contact_fname";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("STRSQL===popcon==" + StrSql);
			prepmap.clear();
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				Str.append(StrSelectdrop(crs.getString("contact_id"), voucher_contact_id)).append(">");
				Str.append(crs.getString("contact_name"));
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			prepmap.clear();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulateRateClass(String voucher_rateclass_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM axela_rate_class" + " WHERE 1=1"
					+ " AND rateclass_type = 1" + " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";
			// SOP("StrSql=po=branchclass="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(Selectdrop(Integer.parseInt(CNumeric(crs.getString("rateclass_id"))), voucher_rateclass_id)).append(">");
				Str.append(crs.getString("rateclass_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateLocation(String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"), vouchertrans_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLedgers() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, CONCAT(customer_name, ' (',customer_code, ')') AS customer_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " WHERE 1=1"
					+ " AND customer_id IN (135, 16948, 16951, 7993)"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), ledger_id));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void BeforeAddRoutine(HttpServletRequest request) {
		try {
			// --------- 1. Code to get branch_id and
			// vouchertrans_location_id---------
			if (emp_branch_id.equals("0")) {
				branch_id = CNumeric(GetSession("voucher_branch_id", request));
				vouchertrans_location_id = CNumeric(GetSession("voucher_location_id", request));
				// SOP("branch_id===" + branch_id);
				// SOP("vouchertrans_location_id===" + vouchertrans_location_id);
				if (vouchertrans_location_id.equals("0") && !branch_id.equals("0")) {
					vouchertrans_location_id = CNumeric(ExecuteQuery("SELECT COALESCE(location_id,0)"
							+ " FROM " + compdb(comp_id) + " axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id
							+ " LIMIT 1"));
				}
				if (branch_id.equals("0")) {
					branch_id = getActiveBranchID(request, emp_id);
					SetSession("voucher_branch_id", branch_id, request);
					if (!branch_id.equals("0")) {
						if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
							StrSql = "SELECT location_id FROM  " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = " + branch_id + ""
									+ " LIMIT 1";
							vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
							SetSession("vouchertrans_location_id", vouchertrans_location_id, request);

							// SOP("vouchertrans_location_id===4444===" + vouchertrans_location_id);
						}
					}
				}
			} else {
				branch_id = emp_branch_id;
				if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}

				SOP("vouchertrans_location_id===333===" + vouchertrans_location_id);

			}
			if (!customer_branch_id.equals("0") && customer_add.equals("yes")) {
				branch_id = customer_branch_id;
				if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}

				SOP("vouchertrans_location_id===22222===" + vouchertrans_location_id);
			}
			// atleast for getting one active branch
			if (branch_id.equals("0")) {
				StrSql = "SELECT branch_id"
						+ " FROM  " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_company_id = " + comp_id
						+ " LIMIT 1";
				branch_id = CNumeric(ExecuteQuery(StrSql));
				SetSession("voucher_branch_id", branch_id, request);
				if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
					SOP("vouchertrans_location_id===111===" + vouchertrans_location_id);
				}
			}
			branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM  " + compdb(comp_id) + "axela_branch" + " WHERE branch_id = " + branch_id);
			// --------------End Of Code

			// 1.----------Query to delete cart times added other then today
			// dont put session_id filter...--------------------
			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_time < " + ConvertShortDateToStr(AddDayMonthYear(strToLongDate(ToLongDate(kknow())), -1, 0, 0, 0));
			// SOP("StrSql=de=="+StrSql);
			updateQuery(StrSql);
			// --------------End Of Code 1.----------------------

			// ------2. Code to copy items from one voucher to another
			// voucher---------------
			if (!voucher_dcr_request_id.equals("0") || !voucher_grn_return_id.equals("0") || !voucher_dcr_id.equals("0")) {
				parentvoucher = "yes";
				if (!voucher_dcr_request_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_dcr_request_id;
					if (!refresh.equals("no")) {
						CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				} else if (!voucher_grn_return_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_grn_return_id;
					if (!refresh.equals("no")) {
						CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				} else if (!voucher_dcr_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_dcr_id;
					if (!refresh.equals("no")) {
						CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				}

				StrSql = "SELECT voucher_date,"
						+ " voucher_dcr_request_id, voucher_grn_return_id, voucher_dcr_id,"
						+ " voucher_location_id, voucher_enquiry_id, voucher_quote_id,"
						+ " voucher_customer_id, voucher_contact_id,"
						+ " voucher_billing_add, voucher_consignee_add,"
						+ " voucher_transporter, voucher_gatepass, voucher_lrno,"
						+ " voucher_tempo_no, voucher_driver_no, voucher_cashdiscount,"
						+ " voucher_turnoverdisc"
						+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id =" + copyvouchertrans_voucher_id;
				// SOP("StrSql====" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					voucher_date = crs.getString("voucher_date");
					voucherdate = strToShortDate(voucher_date);
					if (voucher_dcr_request_id.equals("0")) {
						voucher_dcr_request_id = crs.getString("voucher_dcr_request_id");
					}
					if (voucher_grn_return_id.equals("0")) {
						voucher_grn_return_id = crs.getString("voucher_grn_return_id");
					}
					if (voucher_dcr_id.equals("0")) {
						voucher_dcr_id = crs.getString("voucher_dcr_id");
					}
					vouchertrans_location_id = crs.getString("voucher_location_id");
					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucher_billing_add = crs.getString("voucher_billing_add");
					voucher_consignee_add = crs.getString("voucher_consignee_add");
					voucher_transporter = crs.getString("voucher_transporter");
					voucher_gatepass = crs.getString("voucher_gatepass");
					voucher_lrno = crs.getString("voucher_lrno");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_cashdiscount = crs.getString("voucher_cashdiscount");
					voucher_turnoverdisc = crs.getString("voucher_turnoverdisc");
				}

				location_name = ExecuteQuery("SELECT location_name FROM " + compdb(comp_id) + "axela_inventory_location WHERE location_id = "
						+ vouchertrans_location_id);
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
