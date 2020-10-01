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

import axela.sales.Veh_Salesorder_Update;
import cloudify.connect.Connect;

public class SO_Update2 extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "", checkinvoice = "";;
	public String msg = "";
	public String msgChk = "";
	public String ledger_id = "0";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String so_branch_id = "0";
	public String branch_name = "", location_name = "";
	public String voucher_rateclass_id = "0";
	public String voucher_id = "0";
	public String mainvoucher_id = "0";
	public String voucher_quote_id = "0";
	public String voucher_so_id = "0";
	public String voucher_preorder_id = "0", preorder_so_id = "0";
	public String voucher_vehstock_id = "0";
	public String voucher_invoice_id = "0";
	public String voucher_delnote_id = "0";
	public String voucherclass_id = "", voucherclass_file = "", voucherclass_acc = "0";
	public String vouchertype_id = "0";
	public String voucher_payment_date = "";
	public String voucher_customer_id = "0";
	public String voucher_date = "";
	public String voucherdate = "";
	public String voucher_enquiry_id = "0", voucher_lead_id = "0";
	public int voucher_colspan = 1;
	public String voucher_emp_id = "0";
	public String voucher_ref_no = "";
	public String voucher_custref_no = "";
	public String voucher_custref_date = "";
	public String voucher_active = "1";
	public String voucher_fitted = "0";
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
	public String voucher_amount = "", voucher_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";
	public String vouchertype_roundoff = "0";
	public String vouchertype_roundoff_ledger_cr = "0";
	public String vouchertype_roundoff_ledger_dr = "0";

	public String vouchertrans_location_id = "0", temp_vouchertrans_location_id = "0";
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
	public String gst_type = "";
	public String config_inventory_location_name = "";
	/* End Of Voucher Variables */

	public String vouchertrans_option_id = "0";
	public String vouchertype_defaultauthorize = "0";
	public String vouchertype_authorize = "0";
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

	public String voucher_ref_customer_id = "";
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
	public String emp_invoice_priceupdate = "0";
	public String emp_invoice_discountupdate = "0";
	public String emp_role_id = "0";
	/* End of Config Variables */
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
	public String customer_curr_bal = "0";
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
	DecimalFormat df = new DecimalFormat("#.####");

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
	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public String para = "";
	public String voucher_downpayment = "";
	public String voucher_promise_date = "", voucher_promisedate = "", voucher_paymentdate = "";
	public String voucher_delivery_date = "", voucher_delivereydate = "";
	public String voucher_open = "0";
	public String voucher_critical = "0";
	public String voucher_delstatus_id = "0";
	public String postsf_crm_emp_id = "0";
	public String postsf_manager_id = "0";
	public String vouchertype_affects_inventory = "0";
	public String item_itemgroup_id = "0";
	public String so_contact_id = "0", so_customer_id = "0";
	public String so_customer_name = "", so_contact_name = "";
	public String temp_voucher_id = "0", comp_id = "0", adddirect = "0";
	public String parentvoucher = "";
	public String copyvouchertrans_voucher_id = "0", voucher_paydays_id = "0", paydays_days = "0", refresh = "", session_id = "";

	// variables for JC invoice
	public String voucher_jc_id = "0";
	public String voucher_jc_emp_id = "0";

	public int row_no = 1;

	public Ledger_Check ledgercheck = new Ledger_Check();
	public Veh_Salesorder_Update salesorder = new Veh_Salesorder_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);

			if (adddirect.equals("0")) {
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				comp_id = CNumeric(PadQuotes(GetSession("comp_id", request)));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
			}

			emp_id = CNumeric(GetSession("emp_id", request) + "");

			if (!comp_id.equals("0") && !emp_id.equals("0")) {

				voucher_ref_customer_id = CNumeric(PadQuotes(request.getParameter("voucher_ref_customer_id")));

				String StrSql = "SELECT voucherclass_id , voucherclass_file"
						+ " FROM axela_acc_voucher_class"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_voucherclass_id = voucherclass_id"
						+ " WHERE vouchertype_id = ?";
				prepmap.put(prepkey++, Integer.parseInt(vouchertype_id));
				CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
				prepmap.clear();
				prepkey = 1;
				while (crs.next()) {
					if (crs.getString("voucherclass_id").equals("4")) {
						voucherclass_file = "sales_order";
					} else if (crs.getString("voucherclass_id").equals("5")) {
						voucherclass_file = "acc_quote";
					} else if (crs.getString("voucherclass_id").equals("6") && adddirect.equals("0")) {
						voucherclass_file = "acc_sales_invoice";
					} else if (crs.getString("voucherclass_id").equals("25")) {
						voucherclass_file = "sales_order_delivery";
					} else if (crs.getString("voucherclass_id").equals("27")) {
						voucherclass_file = "acc_preorder";
					} else {
						voucherclass_file = crs.getString("voucherclass_file").toLowerCase();
					}
				}

				crs.close();

				if (adddirect.equals("0") && !voucherclass_id.equals("27")) {
					CheckPerm(comp_id, "emp_" + voucherclass_file + "_access", request, response);
				} else {
					CheckPerm(comp_id, "emp_" + voucherclass_file + "_access,emp_preorder_access,emp_sales_order_add,emp_stock_add", request, response);
				}

				if (!comp_id.equals("0")) {
					config_customer_name = GetSession("config_customer_name", request) + "";
					emp_branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
					comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request) + "");
					refresh = GetSession("refresh", request);
					config_inventory_location_name = GetSession("config_inventory_location_name", request) + "";
					config_inventory_current_stock = "1";
					BranchAccess = GetSession("BranchAccess", request) + "";
					ExeAccess = GetSession("ExeAccess", request) + "";
					if (adddirect.equals("0")) {
						add = PadQuotes(request.getParameter("add"));
						update = PadQuotes(request.getParameter("update"));
						addB = PadQuotes(request.getParameter("add_button"));
						updateB = PadQuotes(request.getParameter("update_button"));
						deleteB = PadQuotes(request.getParameter("delete_button"));

						msg = PadQuotes(request.getParameter("msg"));
						voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
						vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
						voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
						voucher_delnote_id = CNumeric(PadQuotes(request.getParameter("voucher_delnote_id")));
						voucher_jc_id = CNumeric(PadQuotes(request.getParameter("voucher_jc_id")));

						if (!voucher_jc_id.equals("0")) {
							vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("jc_location_id")));
						}
						voucher_jc_emp_id = CNumeric(PadQuotes(request.getParameter("jc_emp_id")));
						voucher_so_id = CNumeric(PadQuotes(request.getParameter("voucher_so_id")));
						voucher_preorder_id = CNumeric(PadQuotes(request.getParameter("voucher_preorder_id")));
						if (!voucher_preorder_id.equals("0")) {
							voucher_id = voucher_preorder_id;
							preorder_so_id = CNumeric(PadQuotes(request.getParameter("preorder_so_id")));
						}

						voucher_vehstock_id = CNumeric(PadQuotes(request.getParameter("voucher_vehstock_id")));

						checkinvoice = PadQuotes(request.getParameter("checkinvoice"));
						voucher_invoice_id = CNumeric(PadQuotes(request.getParameter("voucher_invoice_id")));
						if (!voucher_invoice_id.equals("0")) {
							voucher_id = voucher_invoice_id;
						}
						so_branch_id = CNumeric(PadQuotes(request.getParameter("so_branch_id")));
						// SOP("so_branch_id===" + so_branch_id);
						voucher_quote_id = CNumeric(PadQuotes(request.getParameter("voucher_quote_id")));
						voucher_enquiry_id = CNumeric(PadQuotes(request.getParameter("voucher_enquiry_id")));
						voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
						customer_add = PadQuotes(request.getParameter("customer_add"));
						customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
						contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
						voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_rateclass_id")));

						if (!voucher_vehstock_id.equals("0")) {
							branch_id = so_branch_id;
							StrSql = "SELECT location_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = " + so_branch_id
									+ " ORDER BY location_name LIMIT 1";
							vouchertrans_location_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
						}

						if (customer_id.equals("0")) {
							voucher_customer_id = CNumeric(PadQuotes(request.getParameter("vouchertrans_customer_id")));
						}
						QueryString = PadQuotes(request.getQueryString());
						empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);

						// to check only one Voucher is there for one sales-order
						if (checkinvoice.equals("yes") && !emp_id.equals("1")) {
							checkinvoice(response);
						}

						PopulateConfigDetails();

						if (vouchertype_id.equals("")) {
							response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
						}

						if (!empEditperm.equals("1")) {
							readOnly = "readonly";
						}

						// For Generating session each time
						session_id = PadQuotes(request.getParameter("txt_session_id"));

						// -----------1. code to add customer from add ledger link--------------------
						if (customer_add.equals("yes")) {
							customer_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
							branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
									+ " FROM " + compdb(comp_id) + " axela_branch"
									+ " WHERE branch_id = " + branch_id);

							voucher_customer_id = CNumeric(PadQuotes(request.getParameter("voucher_customer_id")));
							voucher_contact_id = CNumeric(PadQuotes(request.getParameter("voucher_contact_id")));
							voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("voucher_rateclass_id")));
							voucher_paydays_id = PadQuotes(request.getParameter("voucher_paydays_id"));
							// SOP("voucher_contact_id==="+voucher_contact_id);
						}
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
							if (session_id.equals("")) {
								String key = "", possible = "0123456789";
								for (int i = 0; i < 9; i++) {
									double math = Math.random();
									key += possible.charAt((int) Math.floor(Math
											.random() * possible.length()));
								}
								session_id = key;
							}
							BeforeAddRoutine(request);
							gst_type = GetGstType(voucher_customer_id, branch_id, comp_id);
							if (!voucher_so_id.equals("0") && vouchertype_id.equals("6")) {
								if (emp_id.equals("1")) {
									// CopySalesItemToCart(request, emp_id, session_id, voucher_so_id, vouchertype_id, status);
								}
								voucher_consignee_add = voucher_billing_add;
							} else if (!voucher_invoice_id.equals("0")) {
								PopulateFields(request, response);
								// CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
							}
							else if (!voucher_jc_id.equals("0")) {
								// CopyJCTransToCart(request, emp_id, session_id, voucher_jc_id, vouchertype_id, status);
							} else if (!voucher_preorder_id.equals("0")) {
								PopulateFields(request, response);
								// CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
							}
						} else {
							boolean perm;
							// while adding receipt from SO it will by-pass this Permission for adding Sales-Invoice
							if (adddirect.equals("0")) {
								perm = ReturnPerm(comp_id, "emp_acc_voucher_add,"
										+ "emp_sales_sales_order_add,"
										+ "emp_acc_sales_order_add,"
										+ "emp_sales_quote_add,"
										+ "emp_acc_delivery_note_access,"
										+ "emp_acc_quote_add,"
										+ "emp_acc_sales_invoice_add,"
										+ "emp_sales_invoice_add,"// from accesories
										+ "emp_acc_preorder_add,"
										+ "emp_preorder_add,emp_sales_order_add,emp_stock_add"// from accesories
								, request).equals("1");
							} else {
								perm = true;
							}
							if (perm) {
								if (adddirect.equals("0")) {
									getValues(request, response);

								} else {
									voucher_no = getVoucherNo(so_branch_id, vouchertype_id, "0", "0");
									// PopulateContact(customer_id);
								}
								AddFields(request);
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (adddirect.equals("0")) {
										response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
												+ "voucher_id=" + voucher_id
												+ "&voucherclass_id=" + voucherclass_id
												+ "&vouchertype_id=" + vouchertype_id
												+ "&msg=" + vouchertype_name
												+ " added successfully!" + msg + ""));
									}
								}
							} else {
								if (adddirect.equals("0")) {
									response.sendRedirect(AccessDenied());
								}
							}
						}
					} else if (update.equals("yes")) {
						status = "Update";
						if (!updateB.equals("yes")
								&& !deleteB.equals("Delete " + vouchertype_name)) {
							if (session_id.equals("")) {
								String key = "", possible = "0123456789";
								for (int i = 0; i < 9; i++) {
									key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
								}
								session_id = key;
							}
							PopulateFields(request, response);

							if (!refresh.equals("no")) {
								// CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
							}
						} else if (updateB.equals("yes")
								&& !deleteB.equals("Delete " + vouchertype_name)) {
							if (ReturnPerm(
									comp_id,
									"emp_acc_voucher_edit," + "emp_sales_order_edit,"
											+ "emp_acc_sales_order_edit,"
											+ "emp_sales_quote_edit,"
											+ "emp_acc_quote_edit,"
											+ "emp_acc_sales_invoice_edit,"
											+ "emp_sales_invoice_edit,"// from accesories
											+ "emp_acc_preorder_edit,"
											+ "emp_preorder_edit"// from accesories
									, request).equals("1")) {

								getValues(request, response);
								UpdateFields(request, response);

								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response
											.encodeRedirectURL("voucher-list.jsp?"
													+ "voucher_id=" + voucher_id
													+ "&voucherclass_id="
													+ voucherclass_id
													+ "&vouchertype_id="
													+ vouchertype_id + "&msg="
													+ vouchertype_name
													+ " updated successfully!" + msg
													+ ""));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else if (deleteB.equals("Delete " + vouchertype_name)) {
							if (ReturnPerm(comp_id, "emp_acc_voucher_delete,"
									+ "emp_sales_order_delete,"
									+ "emp_acc_sales_order_delete,"
									+ "emp_sales_quote_delete,"
									+ "emp_acc_quote_delete,"
									+ "emp_acc_sales_invoice_delete,"
									+ "emp_sales_invoice_delete,"// from accesories
									+ "emp_acc_preorder_delete,"
									+ "emp_preorder_delete"// from accesories
							, request).equals("1")) {
								if (AppRun().equals("1")) {
									response.sendRedirect(AccessDenied());
								} else {
									getValues(request, response);
									DeleteFields(response);
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response
											.encodeRedirectURL("voucher-list.jsp?"
													+ "voucher_id=" + voucher_id
													+ "&voucherclass_id="
													+ voucherclass_id
													+ "&vouchertype_id="
													+ vouchertype_id + "&msg="
													+ vouchertype_name
													+ " deleted successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
				}
			} else {
				// SOP("aaaa");
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

	protected void getValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		para = PadQuotes(request.getParameter("para"));
		voucher_enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_enquiry_id")));
		voucher_quote_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_quote_id")));
		voucher_so_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_so_id")));
		voucher_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_vehstock_id")));
		voucher_delnote_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_delnote_id")));
		voucher_preorder_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_preorder_id")));
		gst_type = PadQuotes(request.getParameter("txt_gst_type"));
		vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));

		contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
		voucher_ref_customer_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_ref_customer_id")));
		customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
		so_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		so_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));

		if (!vouchertype_id.equals("27")) {
			branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
		} else {
			branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		}
		voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_rateclass_id")));
		voucher_lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		location_name = PadQuotes(request.getParameter("txt_location_name"));

		if (!customer_id.equals("0") && !customer_id.equals(so_customer_id)) {
			so_customer_id = customer_id;
		}
		if (!contact_id.equals("0") && !contact_id.equals(so_contact_id) && voucher_jc_id.equals("0")) {
			so_contact_id = contact_id;
			voucher_contact_id = contact_id;
		}

		if (!customer_id.equals("0") && !contact_id.equals("0")) {
			so_customer_name = PadQuotes(request.getParameter("span_customer_name"));
			so_contact_name = PadQuotes(request.getParameter("span_contact_name"));
			so_customer_name = unescapehtml(so_customer_name);
			so_contact_name = unescapehtml(so_contact_name);
		}

		voucher_customer_id = CNumeric(PadQuotes(request.getParameter("accountingcustomer")));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		if (voucher_customer_id.equals("0")) {
			voucher_customer_id = CNumeric(PadQuotes(request.getParameter("span_customer_id")));
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
		voucher_fitted = CNumeric(PadQuotes(CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_fitted")))));

		if (update.equals("yes")) {
			checkbranch = PadQuotes(request.getParameter("hid_check_branch"));
			check_voucher_no = PadQuotes(request.getParameter("hid_check_voucherno"));
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

		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize")));
		voucher_no = CNumeric(PadQuotes(request.getParameter("txt_voucher_no")));
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
		// SOP("voucher_authorize_id========" + voucher_authorize_id);
		// SOP("voucher_authorize_time========" + voucher_authorize_time);
		// SOP("voucher_authorize====" + voucher_authorize);
	}
	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (branch_id.equals("0")) {
			msg += "<br>Select " + vouchertype_name + " Branch!";
		}
		if (voucherdate.equals("")) {
			msg += "<br>Enter Sales Order Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);

			} else {
				msg += "<br>Enter valid Sales Order Date!";
			}
		}

		if (adddirect.equals("0")) {
			if (vouchertrans_location_id.equals("0")) {
				msg += "<br>Select Location!";
			}
		}

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

		if (CNumeric(voucher_customer_id).equals("0") && !vouchertype_id.equals("27")) {
			msg += "<br>Select Party!";
		}

		if (CNumeric(voucher_contact_id).equals("0") && !vouchertype_id.equals("27")) {
			msg += "<br>Select Contact!";
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

		if (vouchertype_id.equals("4")) {
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
		// String voucher_total_item = CNumeric(PadQuotes(request.getParameter("txt_invoice_qty")));
		// if ((Double.parseDouble(voucher_total_item) <= 0) && voucher_so_id.equals("0")) {
		// msg += "<br>Total Number Of Item Can Not Be Zero! ";
		// }

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
				StrSql = "SELECT voucher_ref_no FROM " + compdb(comp_id)
						+ "axela_acc_voucher" + " WHERE voucher_branch_id = "
						+ branch_id + " " + " AND voucher_ref_no = '"
						+ voucher_ref_no + "'" + " AND voucher_id != "
						+ voucher_id + "" + " AND voucher_vouchertype_id = "
						+ vouchertype_id;
				// SOP("StrSql=123=="+StrSqlBreaker(StrSql));
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Reference No. found!";
				}
			}
		}

		if (vouchertype_id.equals("4")) {

			if (!voucher_promisedate.equals("")) {
				if (isValidDateFormatShort(voucher_promisedate)) {
					voucher_promise_date = ConvertShortDateToStr(voucher_promisedate);
					if (Long.parseLong(voucher_date) > Long
							.parseLong(voucher_promise_date)) {
						msg += "<br>Promise Date must be greater than or equal to Sales Order date!";
					}
				} else {
					msg += "<br>Enter Valid Promise Date!";
				}
			}
		}

		if (!voucher_delivereydate.equals("")) {
			if (isValidDateFormatShort(voucher_delivereydate)) {
				voucher_delivery_date = ConvertShortDateToStr(voucher_delivereydate);
				if (Long.parseLong(voucher_date) > Long
						.parseLong(voucher_delivery_date)) {
					msg += "<br>Delivery Date must be greater than or equal to Sales Order date!";
				}
			} else {
				msg += "<br>Enter Valid Delivery Date!";
			}
		}

		msg = msgChk + msg;

	}
	public void AddFields(HttpServletRequest request) throws Exception {

		CachedRowSet crs = null;
		CheckForm(request);
		voucher_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_grand_total")));
		if (Double.parseDouble(voucher_grandtotal) <= 0) {
			msg += "<br>Grand Total can not be Zero!";
		}

		if (!adddirect.equals("0")) {
			voucher_consignee_add = voucher_billing_add;
		}

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// StrSql = "SELECT COALESCE(SUM(cart_amount),0)"
				// + "-(SELECT COALESCE(SUM(discount.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart discount"
				// + " WHERE 1=1"
				// + " AND discount.cart_emp_id = " + emp_id
				// + " AND discount.cart_session_id = " + session_id
				// + " AND discount.cart_vouchertype_id = " + vouchertype_id
				// + " AND discount.cart_discount = 1)"
				// + " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart tax"
				// + " WHERE 1=1"
				// + " AND tax.cart_emp_id = " + emp_id + ""
				// + " AND tax.cart_session_id = " + session_id
				// + " AND tax.cart_vouchertype_id = " + vouchertype_id
				// + " AND tax.cart_tax = 1)"
				// + " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart billsundry"
				// + " WHERE 1=1"
				// + " AND billsundry.cart_emp_id = " + emp_id
				// + " AND billsundry.cart_session_id = " + session_id
				// + " AND billsundry.cart_vouchertype_id = " + vouchertype_id
				// + " AND billsundry.cart_rowcount = 0"
				// + " AND billsundry.cart_option_id = 0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE 1=1"
				// + " AND cart_emp_id = " + emp_id
				// + " AND cart_session_id = " + session_id
				// + " AND cart_vouchertype_id = " + vouchertype_id
				// + " AND cart_item_id != 0"
				// + " AND cart_discount = 0"
				// + " AND cart_tax = 0";

				// SOP("StrSql==grasndtotal=" + StrSql);
				// voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));

				// SOP("voucher_grandtotal====" + voucher_grandtotal);
				// inserting to axela_acc_voucher table
				if (!voucher_so_id.equals("") && !voucher_so_id.equals("0")) {
					voucher_payment_date = ExecuteQuery("SELECT so_payment_date FROM " + compdb(comp_id) + "axela_sales_so where so_id =" + voucher_so_id);
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
						+ " ("
						+ " voucher_vouchertype_id,"
						+ " voucher_no,"
						+ " voucher_branch_id,"
						+ " voucher_location_id,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_lead_id,"
						+ " voucher_enquiry_id,"
						+ " voucher_quote_id,"
						+ " voucher_preorder_id,"
						+ " voucher_so_id,"
						+ " voucher_vehstock_id,"
						+ " voucher_jc_id,"
						+ " voucher_delnote_id,"
						+ " voucher_invoice_id,"
						+ " voucher_rateclass_id,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_ref_customer_id,"
						+ " voucher_emp_id,"
						+ " voucher_downpayment,"
						+ " voucher_payment_date,"
						+ " voucher_promise_date,"
						+ " voucher_delivery_date,"
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
						+ " voucher_fitted,"
						+ " voucher_active,"
						+ " voucher_notes,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES" + " ("
						+ " " + vouchertype_id + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + branch_id + ","
						+ " " + vouchertrans_location_id + ","
						+ " '" + voucher_date + "',"
						+ " " + df.format(Double.parseDouble(voucher_grandtotal)) + ","
						+ " " + voucher_lead_id + ","
						+ " " + voucher_enquiry_id + ","
						+ " " + voucher_quote_id + ","
						+ " " + voucher_preorder_id + ","
						+ " " + voucher_so_id + ","
						+ " " + voucher_vehstock_id + ","
						+ " " + voucher_jc_id + ","
						+ " " + voucher_delnote_id + ","
						+ " " + voucher_invoice_id + ","
						+ " " + voucher_rateclass_id + ","
						+ " " + voucher_customer_id + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_ref_customer_id + ","
						+ " " + voucher_emp_id + ","
						+ " " + voucher_downpayment + ","
						+ " '" + voucher_payment_date + "',"
						+ " '" + voucher_promise_date + "',"
						+ " '" + voucher_delivery_date + "',"
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
						+ " '" + voucher_fitted + "',"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_notes + "',"
						+ " " + emp_id + ","
						+ " " + ToLongDate(kknow()) + ")";

				// SOP("StrSQl=voucher=" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					voucher_id = rs1.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs1.close();

				// adding transaction details to axela_acc_voucher-trans
				AddItemFields(request);

				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE 1=1" + " AND cart_emp_id = " + emp_id + ""
				// + " AND cart_session_id = " + session_id + ""
				// + " AND cart_vouchertype_id = " + vouchertype_id + "";
				// stmttx.addBatch(StrSql);
				stmttx.executeBatch();
				conntx.commit();

				// Update Profitability fields in sales order
				if (!voucher_so_id.equals("0")) {
					salesorder.UpdateProfitability(voucher_so_id, comp_id);
				}

				// customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(
							voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& (vouchertype_id.equals("3")
								|| vouchertype_id.equals("25")
								|| (vouchertype_id.equals("6") && voucher_authorize.equals("1")))) {

					if (voucher_jc_id.equals("0")) {
						CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
								"0", vouchertrans_location_id, comp_id, vouchertype_id, "");
						Thread thread = new Thread(calccurrentstockthread);
						thread.start();
					}

					// CalCurrentStockVoucher(voucher_id, comp_id, vouchertype_id);
				}

				// mail sending

				if (adddirect.equals("0")) {
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
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " WHERE contact_id = " + voucher_contact_id);
						Voucher_SMS_obj.SendSMS(comp_id, voucher_active);
						Voucher_SMS_obj = null;
					}
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
	public void AddItemFields(HttpServletRequest request) throws SQLException {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE 1 = 1"
					+ " AND vouchertrans_voucher_id = " + voucher_id + "";
			stmttx.addBatch(StrSql);

			int row_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_itemlistcount"))));

			for (int row_no = 1; row_no <= row_count; row_no++) {

				if (CheckBoxValue(request.getParameter("chk_itemlist_" + row_no)).equals("1")) {
					// This is for main item Entery
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_rateclass_id,"
							+ " vouchertrans_billtype_id,"
							+ " vouchertrans_item_desc,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_qty,"
							+ " vouchertrans_price,"
							+ " vouchertrans_netprice,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_discountamount,"
							+ " vouchertrans_taxamount,"
							+ " vouchertrans_unit_cost,"
							+ " vouchertrans_alt_qty,"
							+ " vouchertrans_alt_uom_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES" + " ("
							+ " " + voucher_id + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_sales_customer_id_" + row_no))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_id_" + row_no))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_rateclass_id_" + row_no))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_billtype_id_" + row_no))) + ","
							+ " '" + PadQuotes(request.getParameter("txt_item_desc_" + row_no)) + "',"
							+ " " + row_no + ","// rowcount
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_qty_" + row_no))) + ","
							+ " " + PadQuotes(request.getParameter("txt_item_price_" + row_no)) + ",";

					double netprice = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_price_" + row_no))))
							* Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_qty_" + row_no))));

					StrSql += " " + df.format(netprice) + ","// vouchertrans_netprice
							+ " " + df.format(netprice) + ","// vouchertrans_amount
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_disc_" + row_no))) + ",";

					double total_tax = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax1_" + row_no))))
							+ Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax2_" + row_no))))
							+ Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax3_" + row_no))));

					StrSql += " " + total_tax + ","// vouchertrans_taxamount
							+ " " + PadQuotes(request.getParameter("txt_item_price_" + row_no)) + ","
							+ " " + PadQuotes(request.getParameter("txt_item_qty_" + row_no)) + ","
							+ " " + PadQuotes(request.getParameter("txt_item_uom_id_" + row_no)) + ","
							+ " " + ToLongDate(kknow()) + ","
							+ " '0'"// vouchertrans_dc
							+ " )";
					// SOP("StrSql==main item=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);

					// this is for Item Discount

					Double item_total_discount = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_price_disc_" + row_no))))
							* Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_qty_" + row_no))));
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_billtype_id,"
							+ " vouchertrans_discount,"
							+ " vouchertrans_discount_perc,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES" + " ("
							+ " " + voucher_id + ","
							+ " " + PadQuotes(request.getParameter("txt_item_price_discount1_customer_id_" + row_no)) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_id_" + row_no))) + ","
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_billtype_id_" + row_no))) + ","
							+ " '1',"// vouchertrans_discount
							+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_disc_percent_add_" + row_no))) + ","
							+ " " + row_no + ","// rowcount
							+ " " + row_no + ","// vouchertrans_option_id
							+ " " + CNumeric(PadQuotes(df.format(item_total_discount))) + ","
							+ " " + CNumeric(PadQuotes(df.format(item_total_discount))) + ","
							+ " " + ToLongDate(kknow()) + ","
							+ " '1'" + ")";// vouchertrans_dc
					// SOP("StrSql==Item Discount=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);

					// this ids for tax1
					if (!PadQuotes(request.getParameter("txt_tax1_name_" + row_no)).equals("")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " (vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_location_id,"
								+ " vouchertrans_item_id,"
								+ " vouchertrans_billtype_id,"
								+ " vouchertrans_tax,"
								+ " vouchertrans_tax_id,"
								+ " vouchertrans_rowcount,"
								+ " vouchertrans_option_id,"
								+ " vouchertrans_price,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_time,"
								+ " vouchertrans_dc)"
								+ " VALUES" + " ("
								+ " " + voucher_id + ","
								+ " " + PadQuotes(request.getParameter("txt_item_price_tax_customer_id1_" + row_no)) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_id_" + row_no))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_billtype_id_" + row_no))) + ","
								+ " '1',"// vouchertrans_tax
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_tax_customer_id1_" + row_no))) + ","
								+ " " + row_no + ","// rowcount
								+ " " + row_no + ","// vouchertrans_option_id
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax1_" + row_no))))) + ","
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax1_" + row_no))))) + ","
								+ " " + ToLongDate(kknow()) + ","
								+ " '0'" + ")";// vouchertrans_dc
						// SOP("StrSql==tax1=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
					// this is for tax2
					if (!PadQuotes(request.getParameter("txt_tax2_name_" + row_no)).equals("")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " (vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_location_id,"
								+ " vouchertrans_item_id,"
								+ " vouchertrans_billtype_id,"
								+ " vouchertrans_tax,"
								+ " vouchertrans_tax_id,"
								+ " vouchertrans_rowcount,"
								+ " vouchertrans_option_id,"
								+ " vouchertrans_price,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_time,"
								+ " vouchertrans_dc)"
								+ " VALUES" + " ("
								+ " " + voucher_id + ","
								+ " " + PadQuotes(request.getParameter("txt_item_price_tax_customer_id2_" + row_no)) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_id_" + row_no))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_billtype_id_" + row_no))) + ","
								+ " '1',"// vouchertrans_tax
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_tax_customer_id2_" + row_no))) + ","
								+ " " + row_no + ","// rowcount
								+ " " + row_no + ","// vouchertrans_option_id
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax2_" + row_no))))) + ","
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax2_" + row_no))))) + ","
								+ " " + ToLongDate(kknow()) + ","
								+ " '0'" + ")";// vouchertrans_dc
						// SOP("StrSql==tax2=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}

					// This is for tax3
					if (!PadQuotes(request.getParameter("txt_tax3_name_" + row_no)).equals("")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " (vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_location_id,"
								+ " vouchertrans_item_id,"
								+ " vouchertrans_billtype_id,"
								+ " vouchertrans_tax,"
								+ " vouchertrans_tax_id,"
								+ " vouchertrans_rowcount,"
								+ " vouchertrans_option_id,"
								+ " vouchertrans_price,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_time,"
								+ " vouchertrans_dc)"
								+ " VALUES" + " ("
								+ " " + voucher_id + ","
								+ " " + PadQuotes(request.getParameter("txt_item_price_tax_customer_id3_" + row_no)) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_id_" + row_no))) + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_vouchertrans_billtype_id_" + row_no))) + ","
								+ " '1',"// vouchertrans_tax
								+ " " + CNumeric(PadQuotes(request.getParameter("txt_item_price_tax_customer_id3_" + row_no))) + ","
								+ " " + row_no + ","// rowcount
								+ " " + row_no + ","// vouchertrans_option_id
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax3_" + row_no))))) + ","
								+ " " + CNumeric(PadQuotes(df.format(Double.parseDouble(request.getParameter("txt_item_tax3_" + row_no))))) + ","
								+ " " + ToLongDate(kknow()) + ","
								+ " '0'" + ")";// vouchertrans_dc
						// SOP("StrSql= =tax3=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
				}
			}

			// Round-Off Entry
			if (vouchertype_roundoff.equals("1")
					&& !CNumeric(PadQuotes(request.getParameter("txt_round_off_amount"))).equals("0")) {

				double roundoff_amount = Double.parseDouble(PadQuotes(request.getParameter("txt_round_off_amount")));

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " (vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_location_id,"
						+ " vouchertrans_roundoff,"
						+ " vouchertrans_time,"
						+ " vouchertrans_dc,"
						+ " vouchertrans_price,"
						+ " vouchertrans_amount"
						+ " )" + " VALUES" + " ("
						+ " " + voucher_id + ",";

				// this is for roundoff_customer_id
				if (roundoff_amount > 0) {
					StrSql += "'" + vouchertype_roundoff_ledger_cr + "',";
				} else if (roundoff_amount < 0) {
					StrSql += "'" + vouchertype_roundoff_ledger_dr + "',";
				}

				StrSql += " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
						+ " '1',"// vouchertrans_roundoff
						+ " " + ToLongDate(kknow()) + ",";

				// This is for vouchertrans_dc
				if (roundoff_amount > 0) {
					StrSql += " '0'" + ", ";
				} else if (roundoff_amount < 0) {
					StrSql += " '1'" + ",";
				}

				if (roundoff_amount < 0) {
					roundoff_amount = roundoff_amount * (-1);
				}

				StrSql += " " + df.format(roundoff_amount) + ","
						+ " " + df.format(roundoff_amount) + ")";

				// SOP("StrSql==Round-Off=" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			// party entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans ("
					+ " vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES" + " ("
					+ " " + voucher_id + ","
					+ " " + voucher_customer_id + ","
					+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
					+ " " + CNumeric(PadQuotes(request.getParameter("txt_grand_total"))) + ","
					+ " " + ToLongDate(kknow()) + ","
					+ " '1'" + ")";// vouchertrans_dc

			// SOP("StrSql==party entry==" + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			stmttx.executeBatch();
			stmttx.clearBatch();
			conntx.commit();

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
	public void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_no,"
					+ " COALESCE(voucher_preorder_id, 0) AS voucher_preorder_id,"
					+ " COALESCE(voucher_so_id, 0) AS voucher_so_id,"
					+ " COALESCE(voucher_vehstock_id, 0) AS voucher_vehstock_id,"
					+ " COALESCE(voucher_jc_id, 0) AS voucher_jc_id,"
					+ " COALESCE(voucher_delnote_id, 0) AS voucher_delnote_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " voucher_branch_id, voucher_location_id, voucher_date, voucher_amount,"
					+ " voucher_customer_id, voucher_contact_id,voucher_authorize_id,voucher_authorize_time,"
					+ " voucher_downpayment, voucher_emp_id,"
					+ " voucher_quote_id,voucher_so_id,voucher_invoice_id,voucher_delnote_id,"
					+ " voucher_payment_date,"
					+ " voucher_promise_date, voucher_delivery_date, voucher_delstatus_id,"
					+ " voucher_open, voucher_critical, voucher_special, voucher_rateclass_id,"
					+ " voucher_billing_add, voucher_consignee_add, voucher_transporter, voucher_gatepass, voucher_lrno, voucher_driver_no, voucher_tempo_no, voucher_cashdiscount,"
					+ " voucher_turnoverdisc, voucher_ref_no, voucher_custref_no,"
					+ " voucher_custref_date, voucher_fitted, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id,"
					+ " voucher_modified_date,"
					+ " voucher_narration, voucher_authorize,"
					+ " voucher_quote_id, voucher_enquiry_id,"
					+ " voucher_notes,"
					+ " (SELECT vouchertrans_customer_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans sub"
					+ " WHERE sub.vouchertrans_voucher_id = voucher_id"
					+ " AND sub.vouchertrans_item_id !=0"
					+ " AND sub.vouchertrans_tax =0"
					+ " AND sub.vouchertrans_discount =0"
					+ " AND sub.vouchertrans_rowcount !=0 LIMIT 1) AS ledger_id,"
					+ " COALESCE(CONCAT(location_name,' (', location_code, ')'),'') AS location_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = voucher_location_id"
					+ " WHERE voucher_id = " + voucher_id
					+ ExeAccess
					+ " LIMIT 1";
			// SOP("StrSql=popu==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					if (voucher_preorder_id.equals("0")) {
						voucher_preorder_id = crs.getString("voucher_preorder_id");
					}
					voucher_so_id = crs.getString("voucher_so_id");
					voucher_vehstock_id = crs.getString("voucher_vehstock_id");
					voucher_jc_id = CNumeric(crs.getString("voucher_jc_id"));
					voucher_delnote_id = crs.getString("voucher_delnote_id");
					branch_id = crs.getString("voucher_branch_id");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");
					checkbranch = branch_id;
					branch_name = crs.getString("branch_name");
					location_name = crs.getString("location_name");
					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucherdate = strToShortDate(crs.getString("voucher_date"));
					voucher_no = crs.getString("voucher_no");
					voucher_payment_date = crs.getString("voucher_payment_date");
					voucher_paymentdate = strToShortDate(voucher_payment_date);
					voucher_promise_date = crs.getString("voucher_promise_date");
					voucher_promisedate = strToShortDate(voucher_promise_date);
					voucher_delivery_date = crs.getString("voucher_delivery_date");
					voucher_delivereydate = strToShortDate(voucher_delivery_date);
					voucher_critical = crs.getString("voucher_critical");
					voucher_special = crs.getString("voucher_special");
					voucher_open = crs.getString("voucher_open");
					// voucher_delstatus_id = crs.getString("voucher_delstatus_id");
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
					if (vouchertype_id.equals("25") && status.equals("Add")) {
						voucher_invoice_id = crs.getString("voucher_id");
					} else {
						voucher_invoice_id = crs.getString("voucher_invoice_id");
					}
					voucher_delnote_id = crs.getString("voucher_delnote_id");
					vouchertrans_location_id = crs.getString("voucher_location_id");
					voucher_quote_id = crs.getString("voucher_quote_id");
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_custref_no = crs.getString("voucher_custref_no");
					voucher_custref_date = strToShortDate(crs.getString("voucher_custref_date"));
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_fitted = crs.getString("voucher_fitted");
					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_notes = crs.getString("voucher_notes");
					ledger_id = crs.getString("ledger_id");
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
				}

				if (vouchertype_id.equals("6") && !preorder_so_id.equals("0")) {
					populatePreOrder(request, response);
				}
				gst_type = GetGstType(voucher_customer_id, branch_id, comp_id);
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid " + vouchertype_name + "!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String populateItems(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		Double grand_total = 0.00;
		String round_off_amount = "";
		try {
			int itemrow_count = 0, newrow = 5;
			// Str.append("<div class='table-bordered' >\n");
			Str.append("<div class='col-md-12'><h4><center>Item List</center></h4></div>\n");
			Str.append("<table class='table table-bordered table-hover' data-filter='#filter'>\n");
			Str.append("<thead><tr>\n");
			Str.append("<th style='width:5%;' data-toggle='true'>"
					// + "</br><input type='checkbox' id=itemselect_all name=itemselect_all />"
					+ "</th>\n");
			Str.append("<th style='width:20%;'>Item</th>\n");
			Str.append("<th style='width:7%;'>Qty</th>\n");
			Str.append("<th data-hide='phone' style='width:10%;'>UOM</th>\n");
			Str.append("<th data-hide='phone' style='width:13%;'>Price</th>\n");
			Str.append("<th data-hide='phone' style='width:10%;'>Discount</th>\n");
			Str.append("<th data-hide='phone' style='width:15%;' class='tax-row'>Tax</th>\n");
			Str.append("<th data-hide='phone' style='width:15%;'>Total</th>\n");
			Str.append("</tr></thead>");
			Str.append("<tbody id='item_row'>");

			// round_off_amount = CNumeric(PadQuotes(ExecuteQuery("SELECT vouchertrans_amount"
			// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
			// + " WHERE 1 = 1"
			// + " AND vouchertrans_voucher_id = " + voucher_id
			// + " AND vouchertrans_roundoff = 1")));
			if (!updateB.equals("yes") && !addB.equals("yes")) {
				StrSql = "";
				if (update.equals("yes") && !updateB.equals("yes")) {

					StrSql = "SELECT vouchertrans.vouchertrans_id,"
							+ " vouchertrans_rateclass_id,"
							+ " vouchertrans_billtype_id,"
							+ " vouchertrans.vouchertrans_rowcount,"
							+ " vouchertrans.vouchertrans_item_desc,"
							+ " vouchertrans.vouchertrans_voucher_id AS vouchertrans_voucher_id, item_id,"
							+ " COALESCE(IF(uom_name!='',uom_name, 'Each'),'') AS uom_name, item_sales_ledger_id,"
							+ "	IF(item_code!='',CONCAT(item_name,' (',item_code,')'),item_name) AS item_name,"
							+ " item_code, vouchertrans.vouchertrans_qty, vouchertrans.vouchertrans_alt_qty,"
							+ " vouchertrans.vouchertrans_alt_uom_id, vouchertrans.vouchertrans_truckspace, vouchertrans.vouchertrans_price,"
							+ " vouchertrans.vouchertrans_netprice, vouchertrans.vouchertrans_amount,"
							+ " "
							+ " COALESCE((SELECT discount_ledger.vouchertrans_customer_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans discount_ledger"
							+ " WHERE discount_ledger.vouchertrans_option_id = vouchertrans.vouchertrans_rowcount"
							+ " AND discount_ledger.vouchertrans_discount = 1"
							+ " AND discount_ledger.vouchertrans_voucher_id = " + voucher_id
							+ " GROUP BY discount_ledger.vouchertrans_rowcount), 0) AS discount_customer_id,"
							+ " "
							+ " @discount:=COALESCE( (SELECT sum(disc.vouchertrans_amount)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans disc"
							+ " WHERE disc.vouchertrans_option_id = vouchertrans.vouchertrans_rowcount"
							+ " AND disc.vouchertrans_discount = 1"
							+ " AND disc.vouchertrans_voucher_id = " + voucher_id
							+ " GROUP BY disc.vouchertrans_rowcount), 0) AS discount,"
							+ " "
							+ " COALESCE((SELECT discount_perc.vouchertrans_discount_perc"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans discount_perc"
							+ " WHERE discount_perc.vouchertrans_option_id = vouchertrans.vouchertrans_rowcount"
							+ " AND discount_perc.vouchertrans_discount = 1"
							+ " AND discount_perc.vouchertrans_voucher_id = " + voucher_id
							+ " GROUP BY discount_perc.vouchertrans_rowcount), 0) AS discount_perc,"
							+ " "
							+ " COALESCE((SELECT sum(tax.vouchertrans_amount)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans tax"
							+ " WHERE tax.vouchertrans_option_id = vouchertrans.vouchertrans_rowcount"
							+ " AND tax.vouchertrans_tax = 1"
							+ " AND tax.vouchertrans_voucher_id = " + voucher_id
							+ " GROUP BY tax.vouchertrans_rowcount), 0) AS tax,";

					if (gst_type.equals("state")) {
						StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax1_customer_id,"
								+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax1_rate,"
								+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
								+ " COALESCE(( (vouchertrans_amount-@discount) * tax1.customer_rate / 100),'0') AS tax1_amount,"

								+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax2_customer_id,"
								+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax2_rate,"
								+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
								+ " COALESCE(( (vouchertrans_amount-@discount) * tax2.customer_rate / 100),'0') AS tax2_amount,"
								// This is for dummy record
								+ " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax3_customer_id,"
								+ " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax3_rate,"
								+ " COALESCE(tax4.customer_name, '') AS tax3_name,"
								+ " COALESCE(tax4.customer_id, 0) AS tax3_id,"
								+ " COALESCE(( (vouchertrans_amount-@discount) * tax4.customer_rate / 100),'0') AS tax3_amount";
						// =========================

					} else if (gst_type.equals("central")) {

						StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax1_customer_id,"
								+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax1_rate,"
								+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
								+ " COALESCE(( (vouchertrans_amount-@discount) * tax3.customer_rate / 100),'0') AS tax1_amount,"

								+ " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax2_customer_id,"
								+ " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax2_rate,"
								+ " COALESCE(tax4.customer_name, '') AS tax2_name,"
								+ " COALESCE(( (vouchertrans_amount-@discount) * tax4.customer_rate / 100),'0') AS tax2_amount,"

								// This is for dummy record
								+ " '0' AS tax3_customer_id,"
								+ " '0' AS tax3_rate,"
								+ " '' AS tax3_name,"
								+ " '0' AS tax3_id,"
								+ " ('0') AS tax3_amount";
						// =========================
					}

					StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher_trans vouchertrans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = vouchertrans.vouchertrans_item_id";

					if (gst_type.equals("state")) {
						// this is for SGST
						StrSql += " LEFT JOIN ("
								+ " SELECT vouchertrans_voucher_id, vouchertrans_customer_id ,"
								+ " vouchertrans_item_id, taxtype_id"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE vouchertrans_tax = '1'"
								+ " AND taxtype_id = 3"
								+ " AND vouchertrans_voucher_id =" + voucher_id
								+ " ) AS dtTaxId1 ON dtTaxId1.vouchertrans_voucher_id = vouchertrans.vouchertrans_voucher_id"
								+ " AND dtTaxId1.vouchertrans_item_id = item_id";
						// this is for CGST
						StrSql += " LEFT JOIN ("
								+ " SELECT vouchertrans_voucher_id, vouchertrans_customer_id ,"
								+ " vouchertrans_item_id, taxtype_id"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE vouchertrans_tax = '1'"
								+ " AND taxtype_id = 4"
								+ " AND vouchertrans_voucher_id =" + voucher_id
								+ " ) AS dtTaxId2 ON dtTaxId2.vouchertrans_voucher_id = vouchertrans.vouchertrans_voucher_id"
								+ " AND dtTaxId2.vouchertrans_item_id = item_id";
					}
					else if (gst_type.equals("central")) {
						// this is for IGST
						StrSql += " LEFT JOIN ("
								+ " SELECT vouchertrans_voucher_id, vouchertrans_customer_id ,"
								+ " vouchertrans_item_id, taxtype_id"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE vouchertrans_tax = '1'"
								+ " AND taxtype_id = 5"
								+ " AND vouchertrans_voucher_id =" + voucher_id
								+ " ) AS dtTaxId3 ON dtTaxId3.vouchertrans_voucher_id = vouchertrans.vouchertrans_voucher_id"
								+ " AND dtTaxId3.vouchertrans_item_id = item_id";
					}
					// this is for CESS
					StrSql += " LEFT JOIN ("
							+ " SELECT vouchertrans_voucher_id, vouchertrans_customer_id ,"
							+ " vouchertrans_item_id, taxtype_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
							+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
							+ " WHERE vouchertrans_tax = '1'"
							+ " AND taxtype_id = 6"
							+ " AND vouchertrans_voucher_id =" + voucher_id
							+ " ) AS dtTaxId4 ON dtTaxId4.vouchertrans_voucher_id = vouchertrans.vouchertrans_voucher_id"
							+ " AND dtTaxId4.vouchertrans_item_id = item_id";

					if (gst_type.equals("state")) {
						StrSql += " LEFT JOIN " + compdb(comp_id)
								+ "axela_customer tax1 on tax1.customer_id = dtTaxId1.vouchertrans_customer_id AND dtTaxId1.vouchertrans_item_id = item_id"
								+ " LEFT JOIN " + compdb(comp_id)
								+ "axela_customer tax2 on tax2.customer_id = dtTaxId2.vouchertrans_customer_id AND dtTaxId2.vouchertrans_item_id = item_id";
					} else if (gst_type.equals("central")) {
						StrSql += " LEFT JOIN " + compdb(comp_id)
								+ "axela_customer tax3 on tax3.customer_id = dtTaxId3.vouchertrans_customer_id AND dtTaxId3.vouchertrans_item_id = item_id";
					}
					StrSql += " LEFT JOIN " + compdb(comp_id)
							+ "axela_customer tax4 on tax4.customer_id = dtTaxId4.vouchertrans_customer_id AND dtTaxId4.vouchertrans_item_id = item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = vouchertrans.vouchertrans_alt_uom_id"
							+ " WHERE 1=1"
							+ " AND vouchertrans.vouchertrans_voucher_id = " + voucher_id
							+ " AND vouchertrans.vouchertrans_discount=0"
							+ " AND vouchertrans.vouchertrans_tax=0"
							+ " AND vouchertrans.vouchertrans_item_id!=0"
							+ " AND vouchertrans.vouchertrans_option_id=0"
							+ " AND vouchertrans.vouchertrans_rowcount!=0"
							+ " GROUP BY vouchertrans.vouchertrans_id"
							+ " order BY vouchertrans.vouchertrans_id";
				} else if (add.equals("yes") && !addB.equals("yes") && !voucher_jc_id.equals("0")) {

					StrSql = "SELECT"
							+ " jctrans.jctrans_id AS vouchertrans_id,"
							+ " COALESCE(jctrans.jctrans_rateclass_id) AS vouchertrans_rateclass_id,"
							+ " COALESCE(jctrans.jctrans_billtype_id) AS vouchertrans_billtype_id,"
							+ " jctrans.jctrans_rowcount AS vouchertrans_rowcount,"
							+ " '' AS vouchertrans_item_desc,"
							+ " jctrans.jctrans_jc_id AS vouchertrans_voucher_id,"
							+ " item_id,"
							+ " COALESCE ( IF ( uom_name != '', uom_name, 'Each' ), '' ) AS uom_name,"
							+ " item_sales_ledger_id,"
							+ " IF ( item_code != '', CONCAT( item_name, ' (', item_code, ')' ), item_name ) AS item_name,"
							+ " item_code,"
							+ " COALESCE(jctrans.jctrans_qty) AS vouchertrans_qty,"
							+ " COALESCE(jctrans.jctrans_alt_qty) AS vouchertrans_alt_qty,"
							+ " COALESCE(jctrans.jctrans_alt_uom_id) AS vouchertrans_alt_uom_id,"
							+ " COALESCE(jctrans.jctrans_truckspace) AS vouchertrans_truckspace,"
							+ " COALESCE(jctrans.jctrans_price) AS vouchertrans_price,"
							+ " COALESCE(jctrans.jctrans_netprice) AS vouchertrans_netprice,"
							+ " COALESCE(jctrans.jctrans_amount) AS vouchertrans_amount,"
							+ " COALESCE ( ( SELECT discount_ledger.jctrans_customer_id FROM " + compdb(comp_id) + "axela_service_jc_trans discount_ledger"
							+ " WHERE discount_ledger.jctrans_option_id = jctrans.jctrans_rowcount"
							+ " AND discount_ledger.jctrans_discount = 1"
							+ " AND discount_ledger.jctrans_jc_id = " + voucher_jc_id
							+ " GROUP BY discount_ledger.jctrans_rowcount ), 0 ) AS discount_customer_id,"
							+ " @discount := COALESCE ( ( SELECT sum(disc.jctrans_amount)"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans disc"
							+ " WHERE disc.jctrans_option_id = jctrans.jctrans_rowcount"
							+ " AND disc.jctrans_discount = 1"
							+ " AND disc.jctrans_jc_id = " + voucher_jc_id
							+ " GROUP BY disc.jctrans_rowcount ), 0 ) AS discount,"
							+ " COALESCE ( ( SELECT discount_perc.jctrans_discount_perc"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans discount_perc"
							+ " WHERE discount_perc.jctrans_option_id = jctrans.jctrans_rowcount"
							+ " AND discount_perc.jctrans_discount = 1"
							+ " AND discount_perc.jctrans_jc_id = " + voucher_jc_id
							+ " GROUP BY discount_perc.jctrans_rowcount"
							+ " ), 0 ) AS discount_perc,"
							+ " COALESCE ( ( SELECT sum(tax.jctrans_amount)"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans tax"
							+ " WHERE tax.jctrans_option_id = jctrans.jctrans_rowcount"
							+ " AND tax.jctrans_tax = 1"
							+ " AND tax.jctrans_jc_id = " + voucher_jc_id
							+ " GROUP BY tax.jctrans_rowcount ), 0 ) AS tax,";

					if (gst_type.equals("state")) {
						StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax1_customer_id,"
								+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax1_rate,"
								+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
								+ " COALESCE(( (jctrans_amount-@discount) * tax1.customer_rate / 100),'0') AS tax1_amount,"

								+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax2_customer_id,"
								+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax2_rate,"
								+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
								+ " COALESCE(( (jctrans_amount-@discount) * tax2.customer_rate / 100),'0') AS tax2_amount,"

								// This is for dummy record
								+ " '0' AS tax3_customer_id,"
								+ " '0' AS tax3_rate,"
								+ " '' AS tax3_name,"
								+ " '0' AS tax3_id,"
								+ " '0' AS tax3_amount";
						// =========================
					}
					else if (gst_type.equals("central")) {
						StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax1_customer_id,"
								+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax1_rate,"
								+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
								+ " COALESCE(( (jctrans_amount-@discount) * tax3.customer_rate / 100),'0') AS tax1_amount,"

								// This is for dummy record
								+ " '0' AS tax2_customer_id,"
								+ " '0' AS tax2_rate,"
								+ " '' AS tax2_name,"
								+ " '0' AS tax2_amount,"

								+ " '0' AS tax3_customer_id,"
								+ " '0' AS tax3_rate,"
								+ " '' AS tax3_name,"
								+ " '0' AS tax3_id,"
								+ " ('0') AS tax3_amount";
						// =========================
					}

					StrSql += " FROM " + compdb(comp_id) + "axela_service_jc_trans jctrans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = jctrans.jctrans_item_id";
					if (gst_type.equals("state")) {

						// this is for SGST
						StrSql += " LEFT JOIN ("
								+ " SELECT jctrans_customer_id, jctrans_item_id"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jctrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE jctrans_tax = '1'"
								+ " AND taxtype_id = 3"
								+ " AND jctrans_jc_id = " + voucher_jc_id
								+ " ) AS dtTaxId1 ON dtTaxId1.jctrans_item_id = item_id";

						// this is for CGST
						StrSql += " LEFT JOIN ("
								+ " SELECT jctrans_customer_id, jctrans_item_id"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jctrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE jctrans_tax = '1'"
								+ " AND taxtype_id = 4"
								+ " AND jctrans_jc_id = " + voucher_jc_id
								+ " ) AS dtTaxId2 ON dtTaxId2.jctrans_item_id = item_id";

						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = dtTaxId1.jctrans_customer_id AND dtTaxId1.jctrans_item_id = item_id"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = dtTaxId2.jctrans_customer_id AND dtTaxId2.jctrans_item_id = item_id";

					} else if (gst_type.equals("central")) {

						// this is for IGST
						StrSql += " LEFT JOIN ("
								+ " SELECT jctrans_customer_id, jctrans_item_id"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jctrans_customer_id"
								+ " INNER JOIN axelaauto.axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
								+ " WHERE jctrans_tax = '1'"
								+ " AND taxtype_id = 5"
								+ " AND jctrans_jc_id = " + voucher_jc_id
								+ " ) AS dtTaxId3 ON dtTaxId3.jctrans_item_id = item_id";

						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = dtTaxId3.jctrans_customer_id AND dtTaxId3.jctrans_item_id = item_id";
					}
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = jctrans.jctrans_alt_uom_id"
							+ " WHERE 1 = 1"
							+ " AND jctrans.jctrans_jc_id = " + voucher_jc_id
							+ " AND jctrans.jctrans_discount = 0"
							+ " AND jctrans.jctrans_tax = 0"
							+ " AND jctrans.jctrans_item_id != 0"
							+ " AND jctrans.jctrans_option_id = 0"
							+ " AND jctrans.jctrans_rowcount != 0"
							+ " GROUP BY jctrans.jctrans_id"
							+ " ORDER BY jctrans.jctrans_id";
				}

				// SOP("StrSql==populateItems==" + StrSqlBreaker(StrSql));
				if (!StrSql.equals("")) {
					CachedRowSet crs = null;
					crs = processQuery(StrSql, 0);

					while (crs.next()) {
						// check box
						Str.append("<tr id='itemindex_" + row_no + "'>");
						Str.append("<td valign=top align=center>").append("<input class='itemlist' onchange='GrandTotal();' id='chk_itemlist_" + row_no + "'");
						Str.append(" type='checkbox' name='chk_itemlist_" + row_no + "' checked />");
						Str.append("").append("</td>");
						// Item Name
						Str.append("<td>");
						String item_name = "";
						item_name += PadQuotes(crs.getString("item_name"));
						Str.append(item_name);
						// Str.append("<br><label><u>Description:</u> </label>");
						Str.append("<textarea rows='2' id=txt_item_desc_" + row_no + " name=txt_item_desc_" + row_no + " class='form-control' maxlength='255'"
								+ " onkeyup=\"charcount('txt_item_desc_" + row_no + "', 'span_txt_item_desc_" + row_no + "','<font color=red>({CHAR} characters left)</font>', '255')\">"
								+ crs.getString("vouchertrans_item_desc") + "</textarea>");
						Str.append("<span id='span_txt_item_desc_" + row_no + "'></span>");

						Str.append("<input id=txt_item_name_" + row_no + " name=txt_item_name_" + row_no + " value='" + item_name + "' hidden/>");
						Str.append("</td>");

						// QTY
						Str.append("<td>");
						Str.append("<input name='txt_item_qty_" + row_no + "' type='text' class='form-control text-right item_qtys' id='txt_item_qty_" + row_no + "'");
						Str.append(" value='" + crs.getString("vouchertrans_qty") + "' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_qty_" + row_no + "','Qty');CalItemTotal(" + row_no
								+ ");\" />\n");
						Str.append("<input type='hidden' class='item_ids' id=txt_item_id_" + row_no + " name=txt_item_id_" + row_no + " ").append("value='")
								.append(PadQuotes(crs.getString("item_id"))).append("'/>\n");
						Str.append("<input type='hidden' id=txt_vouchertrans_rateclass_id_" + row_no + " name=txt_vouchertrans_rateclass_id_" + row_no + " ").append("value='")
								.append(PadQuotes(crs.getString("vouchertrans_rateclass_id"))).append("'/>\n");
						Str.append("<input type='hidden' id=txt_vouchertrans_billtype_id_" + row_no + " name=txt_vouchertrans_billtype_id_" + row_no + " ").append("value='")
								.append(PadQuotes(crs.getString("vouchertrans_billtype_id"))).append("'/>\n");
						Str.append("<input type='hidden' id=txt_boxtype_size_" + row_no + " name=txt_boxtype_size_" + row_no + " ").append("value='").append(PadQuotes(crs.getString("item_id")))
								.append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_baseprice_" + row_no + " name=txt_item_baseprice_" + row_no + " ").append("value='")
								.append(CNumeric(PadQuotes(crs.getString("vouchertrans_price")))).append("'/>\n");
						Str.append("<input type='hidden' id=txt_itemprice_updatemode_" + row_no + " name=txt_itemprice_updatemode_" + row_no + " ").append("value='").append("update").append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_pricevariable_" + row_no + " name=txt_item_pricevariable_" + row_no + " ").append("value='").append("0").append("'/>\n");
						Str.append("<input type='hidden' id=txt_optioncount_" + row_no + " name=txt_optioncount_" + row_no + " ").append("value='").append(row_no).append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_ticket_dept_id_" + row_no + " name=txt_item_ticket_dept_id_" + row_no + " ").append("value='").append("0").append("'/>\n");

						Str.append("<input type='hidden' id=txt_item_price_sales_customer_id_" + row_no + " name=txt_item_price_sales_customer_id_" + row_no + " ").append("value='")
								.append(crs.getString("item_sales_ledger_id")).append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_price_discount1_customer_id_" + row_no + " name=txt_item_price_discount1_customer_id_" + row_no + " ").append("value='")
								.append(crs.getString("discount_customer_id")).append("'/>\n");
						// Str.append("<input type='hidden' id=txt_item_price_dic_" + row_no + " name=txt_item_price_dic_" + row_no +
						// " ").append("value='").append(crs.getString("discount")).append("'/>\n");
						// Str.append("<input type='hidden' id=txt_item_price_dic_per_" + row_no + " name=txt_item_price_dic_per_" + row_no +
						// " ").append("value='").append(crs.getString("discount_perc")).append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_price_disc_type_" + row_no + " name=txt_item_price_disc_type_" + row_no + " ").append("value='").append("1").append("'/>\n");
						Str.append("<input type='hidden' id=txt_emp_invoice_discountupdate_" + row_no + " name=txt_emp_invoice_discountupdate_" + row_no + " ").append("value='").append("1")
								.append("'/>\n");

						Str.append("<input type='hidden' id=txt_serial_" + row_no + " name=txt_serial_" + row_no + " ").append("value='").append("").append("'/>\n");
						Str.append("<input type='hidden' id=txt_batch_" + row_no + " name=txt_batch_" + row_no + " ").append("value='").append("").append("'/>\n");
						Str.append("<input type='hidden' id=txt_item_uom_id_" + row_no + " name=txt_item_uom_id_" + row_no + " ").append("value='").append(crs.getString("vouchertrans_alt_uom_id"))
								.append("'/>\n");
						Str.append("<input type='hidden' id=txt_uom_id_" + row_no + " name=txt_uom_id_" + row_no + " ").append("value='").append(crs.getString("vouchertrans_alt_uom_id"))
								.append("'/>\n");
						Str.append("</td>");

						// UOM
						Str.append("<td>");
						Str.append(PopulateUOM(row_no + "", "1", crs.getString("vouchertrans_alt_uom_id")));
						Str.append("</td>");

						// Price
						Str.append("<td>");

						Str.append("<input id=txt_item_price_" + row_no + " name=txt_item_price_" + row_no + " type='text' class='form-control text-right item_prices' ")
								.append("value='")
								.append(df.format(crs.getDouble("vouchertrans_price")))
								.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');CalAmount(" + row_no + ");\" onChange=\"CheckBasePrice(" + row_no
										+ ");CalItemTotal(" + row_no + ");\"");

						if (!emp_invoice_priceupdate.equals("1")) {
							Str.append(" readonly='readonly' ");
						}

						Str.append(" /></td>");

						// Discount
						Str.append("<td>");
						// Str.append("Amount:<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no +
						// "' type='text' class='form-control text-right' ").append("value='").append(crs.getString("discount")).append("' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_price_disc_"
						// + row_no + "','Discount');CalPercent(" + row_no + ");\"/>");
						// // Str.append("<br/>");
						// Str.append("Percentage:<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no +
						// "' type='text' class='form-control text-right' ");
						// Str.append("value='").append(crs.getString("discount_perc")).append("' onKeyUp=\"toNumber('txt_item_price_disc_percent_add_" + row_no + "','Discount');CalAmount(" + row_no +
						// ");CalItemTotal(" + row_no + ");\"/>");

						Str.append("<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no + "' type='text' class='form-control text-right discount_amounts' ")
								.append("value='").append(df.format(crs.getDouble("discount") / crs.getDouble("vouchertrans_qty"))).append("' readonly='readonly'"
										+ " onFocus=\"DiscountModel('../accounting/item-discount.jsp?row_no=" + row_no + "');\"/>");

						Str.append("<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no + "' type='text' class='discount_percents'");
						Str.append("value='").append(crs.getString("discount_perc")).append("' hidden/>");
						Str.append("</td>");

						// Tax 1
						Str.append("<td class='tax-row'>");
						if (PadQuotes(crs.getString("tax1_name")).equals("")
								&& PadQuotes(request.getParameter(crs.getString("tax2_name"))).equals("")
								&& PadQuotes(request.getParameter(crs.getString("tax3_name"))).equals("")) {
							Str.append("0.00").append("");
						}
						if (!PadQuotes(crs.getString("tax1_name")).equals("")) {
							Str.append(crs.getString("tax1_name")).append(":<br>");
							Str.append("<input type='hidden' id=txt_tax1_name_" + row_no + " name=txt_tax1_name_" + row_no + " class='tax_name'").append(" value='").append(crs.getString("tax1_name"))
									.append("'/>\n");
							Str.append(
									"<input id=txt_item_tax1_" + row_no + " name=txt_item_tax1_" + row_no + " type='text' class='form-control text-right "
											+ crs.getString("tax1_name").replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
									.append(crs.getDouble("tax1_amount")).append("'/>\n");
							// Str.append("<br/>");
						}

						// Tax 2
						if (!PadQuotes(crs.getString("tax2_name")).equals("")) {
							Str.append(crs.getString("tax2_name")).append(":<br>");
							Str.append("<input type='hidden' id=txt_tax2_name_" + row_no + " name=txt_tax2_name_" + row_no + " class='tax_name'").append(" value='").append(crs.getString("tax2_name"))
									.append("'/>\n");
							Str.append(
									"<input id=txt_item_tax2_" + row_no + " name=txt_item_tax2_" + row_no + " type='text' class='form-control text-right "
											+ crs.getString("tax2_name").replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
									.append(crs.getDouble("tax2_amount")).append("'/>\n");
							// Str.append("<br/>");
						}

						// Tax 3
						if (!PadQuotes(crs.getString("tax3_name")).equals("")) {
							Str.append(crs.getString("tax3_name")).append(":<br>");
							Str.append("<input type='hidden' id=txt_tax3_name_" + row_no + " name=txt_tax3_name_" + row_no + " class='tax_name'").append(" value='").append(crs.getString("tax3_name"))
									.append("'/>\n");
							Str.append(
									"<input id=txt_item_tax3_" + row_no + " name=txt_item_tax3_" + row_no + " type='text' class='form-control text-right "
											+ crs.getString("tax3_name").replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
									.append(crs.getDouble("tax3_amount")).append("'/>\n");
						}

						// hidden fields of tax1
						Str.append("<input id=txt_item_price_tax_rate1_" + row_no + " name=txt_item_price_tax_rate1_" + row_no + " type='hidden' ").append(" value='")
								.append(crs.getString("tax1_rate"))
								.append("'/>\n");
						Str.append("<input id=txt_item_price_tax_customer_id1_" + row_no + " name=txt_item_price_tax_customer_id1_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax1_customer_id")).append("'/>\n");
						Str.append("<input id=txt_item_price_tax_id1_" + row_no + " name=txt_item_price_tax_id1_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax1_customer_id"))
								.append("'/>\n");
						// hidden fields of tax2
						Str.append("<input id=txt_item_price_tax_rate2_" + row_no + " name=txt_item_price_tax_rate2_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax2_rate"))
								.append("'/>\n");
						Str.append("<input id=txt_item_price_tax_customer_id2_" + row_no + " name=txt_item_price_tax_customer_id2_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax2_customer_id")).append("'/>\n");
						Str.append("<input id=txt_item_price_tax_id2_" + row_no + " name=txt_item_price_tax_id2_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax2_customer_id"))
								.append("'/>\n");
						// hidden fields of tax3
						Str.append("<input id=txt_item_price_tax_rate3_" + row_no + " name=txt_item_price_tax_rate3_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax3_rate"))
								.append("'/>\n");
						Str.append("<input id=txt_item_price_tax_customer_id3_" + row_no + " name=txt_item_price_tax_customer_id3_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax3_customer_id")).append("'/>\n");
						Str.append("<input id=txt_item_price_tax_id3_" + row_no + " name=txt_item_price_tax_id3_" + row_no + " type='hidden' ").append("value='")
								.append(crs.getString("tax3_customer_id"))
								.append("'/>\n");
						// hidden fields of after tax calculation
						Str.append("<input id=txt_price_tax2_after_tax1_" + row_no + " name=txt_price_tax2_after_tax1_" + row_no + " type='hidden' ").append("value='").append("0").append("'/>\n");
						Str.append("<input id=txt_price_tax3_after_tax2_" + row_no + " name=txt_price_tax3_after_tax2_" + row_no + " type='hidden' ").append("value='").append("0").append("'/>\n");
						// hidden fields of Stock Serial
						Str.append("<input id=txt_item_stockserial_" + row_no + " name=txt_item_stockserial_" + row_no + " type='hidden' ").append("value='").append("").append("'/>\n");
						Str.append("</td>");

						// Total
						Double item_total = ((crs.getDouble("vouchertrans_amount") - crs.getDouble("discount")) + crs.getDouble("tax"));
						grand_total += item_total;
						Str.append("<td align='right'>");
						Str.append("<b><input id=item_total_" + row_no + " name=item_total_" + row_no + " value=").append(df.format(item_total))
								.append(" class='item_total form-control text-right' readonly='readonly' /></b>\n");
						Str.append("</td>");

						// ===========================

						Str.append("</tr>");
						row_no++;
					}
					crs.close();
				}
				itemrow_count = 5;
			} else {
				itemrow_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_itemlistcount"))));

				if (itemrow_count <= 0) {
					itemrow_count = 5;
				}
			}
			for (; row_no <= itemrow_count; row_no++) {
				Str.append("<tr id='itemindex_" + row_no + "'>");
				if (CheckBoxValue(request.getParameter("chk_itemlist_" + row_no)).equals("1")
						&& !PadQuotes(request.getParameter("txt_item_name_" + row_no)).equals("")) {
					// =================================================

					// check box
					Str.append("<td valign=top align=center>").append("<input class='itemlist' onchange='GrandTotal();' id='chk_itemlist_" + row_no + "'");
					Str.append(" type='checkbox' name='chk_itemlist_" + row_no + "' checked />");
					Str.append("").append("</td>");
					// Item Name
					Str.append("<td>");
					String item_name = "";
					item_name += PadQuotes(request.getParameter("txt_item_name_" + row_no));
					Str.append(item_name);

					// Str.append("<br><label><u>Description:</u> </label>");
					Str.append("<textarea rows='2' id=txt_item_desc_" + row_no + " name=txt_item_desc_" + row_no + " class='form-control' maxlength='255'"
							+ " onkeyup=\"charcount('txt_item_desc_" + row_no + "', 'span_txt_item_desc_" + row_no + "','<font color=red>({CHAR} characters left)</font>', '255')\">"
							+ PadQuotes(request.getParameter("txt_item_desc_" + row_no)) + "</textarea>");
					Str.append("<span id='span_txt_item_desc_" + row_no + "'></span>");

					Str.append("<input id=txt_item_name_" + row_no + " name=txt_item_name_" + row_no + " value='" + item_name + "' hidden/>");
					Str.append("</td>");

					// QTY
					Str.append("<td>");
					Str.append("<input name='txt_item_qty_" + row_no + "' type='text' class='form-control text-right item_qtys' id='txt_item_qty_" + row_no + "' ");
					Str.append("value='" + request.getParameter("txt_item_qty_" + row_no) + "' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_qty_" + row_no + "','Qty');CalItemTotal(" + row_no
							+ ");\" />\n");
					Str.append("<input type='hidden' id=txt_item_id_" + row_no + " name=txt_item_id_" + row_no + " class='item_ids'").append("value='")
							.append(PadQuotes(request.getParameter("txt_item_id_" + row_no))).append("'/>\n");
					// Str.append("<input type='hidden' id=txt_boxtype_size_" + row_no + " name=txt_boxtype_size_" + row_no +
					// " ").append("value='").append(PadQuotes(request.getParameter("txt_boxtype_size_" + row_no))).append("'/>\n");
					Str.append("<input type='hidden' id=txt_stock_qty_" + row_no + " name=txt_stock_qty_" + row_no + " ").append("value='").append(PadQuotes("0")).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_baseprice_" + row_no + " name=txt_item_baseprice_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_baseprice_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_itemprice_updatemode_" + row_no + " name=txt_itemprice_updatemode_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_itemprice_updatemode_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_pricevariable_" + row_no + " name=txt_item_pricevariable_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_pricevariable_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_optioncount_" + row_no + " name=txt_optioncount_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_optioncount_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_ticket_dept_id_" + row_no + " name=txt_item_ticket_dept_id_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_ticket_dept_id_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_price_sales_customer_id_" + row_no + " name=txt_item_price_sales_customer_id_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_price_sales_customer_id_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_price_discount1_customer_id_" + row_no + " name=txt_item_price_discount1_customer_id_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_price_discount1_customer_id_" + row_no)).append("'/>\n");
					// Str.append("<input type='hidden' id=txt_item_price_dic_" + row_no + " name=txt_item_price_dic_" + row_no +
					// " ").append("value='").append(request.getParameter("txt_item_price_dic_" + row_no)).append("'/>\n");
					// Str.append("<input type='hidden' id=txt_item_price_dic_per_" + row_no + " name=txt_item_price_dic_per_" + row_no +
					// " ").append("value='").append(request.getParameter("txt_item_price_dic_per_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_price_disc_type_" + row_no + " name=txt_item_price_disc_type_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_item_price_disc_type_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_emp_invoice_discountupdate_" + row_no + " name=txt_emp_invoice_discountupdate_" + row_no + " ").append("value='")
							.append(request.getParameter("txt_emp_invoice_discountupdate_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_serial_" + row_no + " name=txt_serial_" + row_no + " ").append("value='").append(request.getParameter("txt_serial_" + row_no))
							.append("'/>\n");
					Str.append("<input type='hidden' id=txt_batch_" + row_no + " name=txt_batch_" + row_no + " ").append("value='").append(request.getParameter("txt_batch_" + row_no)).append("'/>\n");
					Str.append("<input type='hidden' id=txt_item_uom_id_" + row_no + " name=txt_item_uom_id_" + row_no + " ").append("value='").append(request.getParameter("dr_alt_uom_id_" + row_no))
							.append("'/>\n");
					Str.append("<input type='hidden' id=txt_uom_id_" + row_no + " name=txt_uom_id_" + row_no + " ").append("value='").append(request.getParameter("txt_uom_id_" + row_no))
							.append("'/>\n");
					Str.append("</td>");

					// UOM
					Str.append("<td>");
					Str.append(PopulateUOM(row_no + "", "1", request.getParameter("dr_alt_uom_id_" + row_no)));
					Str.append("</td>");

					// Price
					Str.append("<td>");

					Str.append("<input id=txt_item_price_" + row_no + " name=txt_item_price_" + row_no + " type='text' class='form-control text-right item_prices' ")
							.append("value='")
							.append(request.getParameter("txt_item_price_" + row_no))
							.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');CalAmount(" + row_no + ");\" onChange=\"CheckBasePrice(" + row_no
									+ ");CalItemTotal(" + row_no + ");\"");

					if (emp_invoice_priceupdate.equals("1")) {
						Str.append(" readonly='readonly' ");
					}

					Str.append(" /></td>");

					// Discount
					Str.append("<td>");
					// Str.append("Amount:<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no +
					// "' type='text' class='form-control text-right' ").append("value='").append(request.getParameter("txt_item_price_disc_" +
					// row_no)).append("' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_price_disc_" + row_no + "','Discount');CalPercent(" + row_no + ");\"/>");
					// // Str.append("<br/>");
					// Str.append("Percentage:<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no +
					// "' type='text' class='form-control text-right' ");
					// Str.append("value='").append(request.getParameter("txt_item_price_disc_percent_add_" + row_no)).append("' onKeyUp=\"toNumber('txt_item_price_disc_percent_add_" + row_no +
					// "','Discount');CalAmount(" + row_no + ");CalItemTotal(" + row_no + ");\"/>");

					Str.append("<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no + "' type='text' class='form-control text-right item_discounts' ")
							.append("value='").append(request.getParameter("txt_item_price_disc_" + row_no)).append("' readonly='readonly'"
									+ " onFocus=\"DiscountModel('../accounting/item-discount.jsp?row_no=" + row_no + "');\"/>");

					Str.append("<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no + "' type='text' class='item_percents'");
					Str.append("value='").append(request.getParameter("txt_item_price_disc_percent_add_" + row_no)).append("' hidden/>");

					Str.append("</td>");

					// Tax 1
					Str.append("<td class='tax-row'>");
					if (PadQuotes(request.getParameter("txt_tax1_name_" + row_no)).equals("")
							&& PadQuotes(request.getParameter("txt_tax1_name_" + row_no)).equals("")
							&& PadQuotes(request.getParameter("txt_tax1_name_" + row_no)).equals("")) {
						Str.append("0.00").append("");
					}
					if (!PadQuotes(request.getParameter("txt_tax1_name_" + row_no)).equals("")) {
						Str.append(request.getParameter("txt_tax1_name_" + row_no)).append(":<br>");
						Str.append("<input type='hidden' id=txt_tax1_name_" + row_no + " name=txt_tax1_name_" + row_no + " class='tax_name'").append(" value='")
								.append(request.getParameter("txt_tax1_name_" + row_no)).append("'/>\n");
						Str.append(
								"<input id=txt_item_tax1_" + row_no + " name=txt_item_tax1_" + row_no + " type='text' class='form-control text-right "
										+ request.getParameter("txt_tax1_name_" + row_no).replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
								.append(request.getParameter("txt_item_tax1_" + row_no)).append("'/>\n");
						// Str.append("<br/>");
					}

					// Tax 2
					if (!PadQuotes(request.getParameter("txt_tax2_name_" + row_no)).equals("")) {
						Str.append(request.getParameter("txt_tax2_name_" + row_no)).append(":<br>");
						Str.append("<input type='hidden' id=txt_tax2_name_" + row_no + " name=txt_tax2_name_" + row_no + " class='tax_name'").append(" value='")
								.append(request.getParameter("txt_tax2_name_" + row_no)).append("'/>\n");
						Str.append(
								"<input id=txt_item_tax2_" + row_no + " name=txt_item_tax2_" + row_no + " type='text' class='form-control text-right "
										+ request.getParameter("txt_tax2_name_" + row_no).replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
								.append(request.getParameter("txt_item_tax2_" + row_no)).append("'/>\n");
						// Str.append("<br/>");
					}

					// Tax 3
					if (!PadQuotes(request.getParameter("txt_tax3_name_" + row_no)).equals("")) {
						Str.append(request.getParameter("txt_tax2_name_" + row_no)).append(":<br>");
						Str.append("<input type='hidden' id=txt_tax3_name_" + row_no + " name=txt_tax3_name_" + row_no + " class='tax_name'").append(" value='")
								.append(request.getParameter("txt_tax3_name_" + row_no)).append("'/>\n");
						Str.append(
								"<input id=txt_item_tax3_" + row_no + " name=txt_item_tax3_" + row_no + " type='text' class='form-control text-right "
										+ request.getParameter("txt_tax3_name_" + row_no).replaceAll("[@% ]", "") + "_" + row_no + "' readonly='readonly' ").append("value='")
								.append(request.getParameter("txt_item_tax3_" + row_no)).append("'/>\n");
					}

					// hidden fields of tax1
					Str.append("<input id=txt_item_price_tax_rate1_" + row_no + " name=txt_item_price_tax_rate1_" + row_no + " type='hidden' ").append(" value='")
							.append(request.getParameter("txt_item_price_tax_rate1_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_customer_id1_" + row_no + " name=txt_item_price_tax_customer_id1_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_customer_id1_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_id1_" + row_no + " name=txt_item_price_tax_id1_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_id1_" + row_no)).append("'/>\n");
					// hidden fields of tax2
					Str.append("<input id=txt_item_price_tax_rate2_" + row_no + " name=txt_item_price_tax_rate2_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_rate2_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_customer_id2_" + row_no + " name=txt_item_price_tax_customer_id2_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_customer_id2_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_id2_" + row_no + " name=txt_item_price_tax_id2_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_id2_" + row_no))
							.append("'/>\n");
					// hidden fields of tax3
					Str.append("<input id=txt_item_price_tax_rate3_" + row_no + " name=txt_item_price_tax_rate3_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_rate3_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_customer_id3_" + row_no + " name=txt_item_price_tax_customer_id3_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_customer_id3_" + row_no)).append("'/>\n");
					Str.append("<input id=txt_item_price_tax_id3_" + row_no + " name=txt_item_price_tax_id3_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_price_tax_id3_" + row_no)).append("'/>\n");

					// hidden fields of after tax calculation
					Str.append("<input id=txt_price_tax2_after_tax1_" + row_no + " name=txt_price_tax2_after_tax1_" + row_no + " type='hidden' ").append("value='").append("0").append("'/>\n");
					Str.append("<input id=txt_price_tax3_after_tax2_" + row_no + " name=txt_price_tax3_after_tax2_" + row_no + " type='hidden' ").append("value='").append("0").append("'/>\n");
					// hidden fields of Stock Serial
					Str.append("<input id=txt_item_stockserial_" + row_no + " name=txt_item_stockserial_" + row_no + " type='hidden' ").append("value='")
							.append(request.getParameter("txt_item_stockserial_" + row_no)).append("'/>\n");
					Str.append("</td>");
					// Total
					grand_total += Double.parseDouble(CNumeric(PadQuotes(request.getParameter("item_total_" + row_no))));
					Str.append("<td align='right'>");
					Str.append("<b><input id=item_total_" + row_no + " name=item_total_" + row_no + " value=").append(request.getParameter("item_total_" + row_no))
							.append(" class='item_total form-control text-right' readonly='readonly' /></b>\n");
					Str.append("</td>");

					// ===========================
				} else {

					// check box
					Str.append("<td valign=top align=center>").append("<input class='itemlist' id='chk_itemlist_" + row_no + "'");
					Str.append(" type='checkbox' name='chk_itemlist_" + row_no + "' />");
					Str.append("").append("</td>");
					// Item Name
					Str.append("<td><input type='text' class='form-control item_name' onkeyup='InvoiceItemSearch(" + row_no + ");' id='txt_itemname_" + row_no + "'");
					Str.append(" name='txt_itemname_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// QTY
					Str.append("<td><input type='text' class='form-control' id='txt_item_qty_" + row_no + "'");
					Str.append(" name='txt_item_qty_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// UOM
					Str.append("<td><input type='text' class='form-control' id='txt_itemuom_" + row_no + "'");
					Str.append(" name='txt_itemuom_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// Price
					Str.append("<td><input type='text' class='form-control' id='txt_itemprice_" + row_no + "'");
					Str.append(" name='txt_itemprice_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// Discount
					Str.append("<td><input type='text' class='form-control' id='txt_itemdiscount_" + row_no + "'");
					Str.append(" name='txt_itemdiscount_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// Tax
					Str.append("<td class='tax-row'><input type='text' class='form-control' id='txt_itemtax_" + row_no + "'");
					Str.append(" name='txt_itemtax_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
					// Total
					Str.append("<td><input type='text' class='form-control' id='txt_itemtotal_" + row_no + "'");
					Str.append(" name='txt_itemtotal_" + row_no + "'");
					Str.append(" value='").append("").append("'></td>");
				}
				Str.append("</tr>");
				Str.append("<tr class='searchresult' id='searchresult_" + row_no + "_tr' style='display: none;'><td colspan='8' id='searchresult_" + row_no + "'></td></tr>");
			}
			if (row_no > 1) {
				row_no--;
			}
			Str.append("<input type='hidden' name='txt_itemlistcount' id='txt_itemlistcount' value='").append(row_no).append("'/>");
			Str.append("</tbody>");
			Str.append("</table>");
			Str.append("<div class='col-md-12' id='total_tax'></div>");
			Str.append("<div class='col-md-8'></div><label class='col-md-2 text-right' style='margin-top:8px;'>Grand Total:</label><div class='col-md-2 text-right'><b><input type='text' name='txt_grand_total' id='txt_grand_total' class='form-control text-right' value='"
					+ df.format(grand_total) + "' readonly='readonly' /></b></div>");
			// Str.append("</div>");

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateUOM(String row_no, String uom_id, String alt_uom_id) {
		try {

			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT uom_id, uom_ratio, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE 1=1"
					+ " AND (uom_id = " + uom_id + " OR uom_parent_id = " + uom_id + ")"
					+ " GROUP BY CONCAT(uom_name,uom_ratio)"
					+ " ORDER BY uom_id";
			// SOP("StrSql=po=uom11="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_alt_uom_id_" + row_no + "' id='dr_alt_uom_id_" + row_no + "' class='form-control' onChange='CalItemTotal(" + row_no + ");'>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("uom_id")).append("-").append(crs.getString("uom_ratio"));
				Str.append(StrSelectdrop(crs.getString("uom_id"), alt_uom_id)).append(">");
				Str.append(crs.getString("uom_name")).append(" X ").append(crs.getString("uom_ratio"));
				Str.append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm(request);
		voucher_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_grand_total")));
		if (Double.parseDouble(voucher_grandtotal) <= 0) {
			msg += "<br>Grand Total can not be Zero!";
		}
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// StrSql = "SELECT COALESCE(SUM(cart_amount),0)"
				// + "-(SELECT COALESCE(SUM(discount.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart discount"
				// + " WHERE 1=1"
				// + " AND discount.cart_emp_id = " + emp_id
				// + " AND discount.cart_session_id = " + session_id
				// + " AND discount.cart_vouchertype_id = " + vouchertype_id
				// + " AND discount.cart_discount = 1)"
				// + " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart tax"
				// + " WHERE 1=1"
				// + " AND tax.cart_emp_id = " + emp_id + ""
				// + " AND tax.cart_session_id = " + session_id
				// + " AND tax.cart_vouchertype_id = " + vouchertype_id
				// + " AND tax.cart_tax = 1)"
				// + " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart billsundry"
				// + " WHERE 1=1"
				// + " AND billsundry.cart_emp_id = " + emp_id
				// + " AND billsundry.cart_session_id = " + session_id
				// + " AND billsundry.cart_vouchertype_id = " + vouchertype_id
				// + " AND billsundry.cart_rowcount = 0"
				// + " AND billsundry.cart_option_id = 0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE 1 = 1"
				// + " AND cart_emp_id = " + emp_id
				// + " AND cart_session_id = " + session_id
				// + " AND cart_vouchertype_id = " + vouchertype_id
				// + " AND cart_item_id != 0"
				// + " AND cart_discount = 0"
				// + " AND cart_tax = 0";
				// SOP("StrSql==grandtotal=" + StrSql);
				// voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));
				// SOP("voucher_grandtotal =111=== " + voucher_grandtotal);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
						+ " SET"
						+ " voucher_vouchertype_id = " + vouchertype_id + ","
						+ " voucher_branch_id = " + branch_id + ","
						+ " voucher_location_id = " + vouchertrans_location_id + ","
						+ " voucher_date = " + voucher_date + ","
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_amount = " + df.format(Double.parseDouble(voucher_grandtotal)) + ","
						+ " voucher_lead_id = " + voucher_lead_id + ","
						+ " voucher_customer_id = " + voucher_customer_id + ","
						+ " voucher_contact_id = " + voucher_contact_id + ","
						+ " voucher_emp_id = " + voucher_emp_id + ","
						+ " voucher_downpayment = " + voucher_downpayment + ","
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
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						+ " voucher_custref_no = '" + voucher_custref_no + "',"
						+ " voucher_custref_date = '" + ConvertShortDateToStr(voucher_custref_date) + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_fitted = '" + voucher_fitted + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_notes = '" + voucher_notes + "'";

				if (!emp_id.equals("1")) {
					StrSql += ", voucher_modified_id = " + emp_id + ","
							+ " voucher_modified_date = " + ToLongDate(kknow());
				}

				StrSql += " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql===up===" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);

				// Copy items FROM axela_acc_cart to axela_vouchertrans
				AddItemFields(request);

				// Delete cart items

				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE cart_session_id = " + session_id
				// + " AND cart_emp_id = " + emp_id
				// + " AND cart_vouchertype_id=" + vouchertype_id;
				// stmttx.addBatch(StrSql);

				// to update the Customer Current Balance
				stmttx.executeBatch();

				conntx.commit();

				if (!voucher_so_id.equals("0")) {
					salesorder.UpdateProfitability(voucher_so_id, comp_id);
				}

				// / customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(
							voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();

					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// item stock
				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& (vouchertype_id.equals("3") || vouchertype_id.equals("116") || vouchertype_id.equals("25"))
						|| (vouchertype_id.equals("6") && voucher_authorize.equals("1"))) {

					if (voucher_jc_id.equals("0")) {
						CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
								"0", vouchertrans_location_id, comp_id, vouchertype_id, "");
						Thread thread = new Thread(calccurrentstockthread);
						thread.start();
					}

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
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
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
		try {

			// Association with Receipt
			StrSql = "SELECT voucher_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1=1 ";
			if (vouchertype_id.equals("4")) {
				StrSql += "AND voucher_so_id = " + voucher_id + " ";
			}
			if (vouchertype_id.equals("5")) {
				StrSql += "AND voucher_quote_id = " + voucher_id + " ";
			}
			if (vouchertype_id.equals("6")) {
				StrSql += "AND voucher_invoice_id = " + voucher_id + " ";
			}
			if (vouchertype_id.equals("25")) {
				StrSql += "AND voucher_delnote_id = " + voucher_id + " ";
			}
			if (vouchertype_id.equals("27")) {
				StrSql += "AND voucher_preorder_id = " + voucher_id + " ";
			}
			// SOP("StrSql====" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Voucher is Associated with Another Vouchers!";
			}

			if (msg.equals("")) {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				updateQuery("UPDATE " + compdb(comp_id)
						+ "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");

				// Update the customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(
							voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// Delete all the cart items for the current session
				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE cart_session_id = " + session_id
				// + " AND cart_emp_id = " + emp_id
				// + " AND cart_vouchertype_id=" + vouchertype_id;
				// stmttx.addBatch(StrSql);

				// Delete all the items for the current Invoice
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Delete all the association with "axela_acc_voucher_bal" table
				StrSql = "DELETE FROM " + compdb(comp_id)
						+ "axela_acc_voucher_bal"
						+ " WHERE voucherbal_trans_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Finally Delete the Invoice
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

				// Update Profitability fields in sales order
				if (!voucher_so_id.equals("0")) {
					salesorder.UpdateProfitability(voucher_so_id, comp_id);
				}
				conntx.commit();

				// item stock
				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")
						&& vouchertype_affects_inventory.equals("1")
						&& (vouchertype_id.equals("3")
								|| vouchertype_id.equals("6")
								|| vouchertype_id.equals("116")
								|| vouchertype_id.equals("25"))) {

					if (voucher_jc_id.equals("0")) {
						CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
								"0", vouchertrans_location_id, comp_id, vouchertype_id, "");
						Thread thread = new Thread(calccurrentstockthread);
						thread.start();
					}

					// CalCurrentStockVoucher(voucher_id, comp_id, vouchertype_id);
				}
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

	void CheckPageperm(HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_id FROM " + compdb(comp_id)
					+ "axela_acc_voucher" + " INNER JOIN " + compdb(comp_id)
					+ "axela_branch ON branch_id = voucher_branch_id"
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
				StrSql = "SELECT COALESCE(customer_rateclass_id, 0) AS customer_rateclass_id,"
						+ " customer_curr_bal, customer_credit_limit,"
						+ " COALESCE(customer_address, '') as customer_address,"
						+ " COALESCE(city_name, '') AS city_name,"
						+ " COALESCE(customer_pin, '') AS customer_pin,"
						+ " COALESCE(state_name, '') AS state_name,"
						+ " COALESCE(customer_landmark, '') as customer_landmark"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_currentbal ON currentbal_customer_id = customer_id"
						+ " AND currentbal_company_id =" + comp_id
						+ " WHERE customer_id = " + voucher_customer_id + "";
				// SOP("StrSql=PopulateCustomerDetails==" + StrSql);
				CachedRowSet crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					customer_curr_bal = crs1.getString("customer_curr_bal");
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
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id)
					+ "axela_title" + " ORDER BY title_desc";
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
			StrSql = "SELECT voucherclass_file, voucherclass_acc, vouchertype_name,"
					+ " vouchertype_mobile, vouchertype_email, vouchertype_dob,"
					+ " vouchertype_dnd, vouchertype_roundoff,"
					+ " vouchertype_roundoff_ledger_cr, vouchertype_roundoff_ledger_dr,"
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
					+ " comp_email_enable, comp_sms_enable, vouchertype_billing_add,"
					+ " vouchertype_consignee_add, vouchertype_transporter,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_tempo_no,"
					+ " vouchertype_driver_no, vouchertype_cashdiscount, vouchertype_turnoverdisc,"
					+ " COALESCE(emp.emp_invoice_priceupdate,'0') AS emp_invoice_priceupdate,"
					+ " COALESCE(emp.emp_invoice_discountupdate,'0') AS emp_invoice_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id,"
					+ " " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id + " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";

			// SOP("StrSql=congif==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucherclass_file = crs.getString("voucherclass_file");
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				vouchertype_roundoff = CNumeric(crs.getString("vouchertype_roundoff"));
				vouchertype_roundoff_ledger_cr = crs.getString("vouchertype_roundoff_ledger_cr");
				vouchertype_roundoff_ledger_dr = crs.getString("vouchertype_roundoff_ledger_dr");
				vouchertype_mobile = crs.getString("vouchertype_mobile");
				vouchertype_email = crs.getString("vouchertype_email");
				vouchertype_dob = crs.getString("vouchertype_dob");
				vouchertype_dnd = crs.getString("vouchertype_dnd");
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
				emp_invoice_priceupdate = CNumeric(PadQuotes(crs.getString("emp_invoice_priceupdate")));
				emp_invoice_discountupdate = CNumeric(PadQuotes(crs.getString("emp_invoice_discountupdate")));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateItemCatPop() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cat_id ,cat_name" + " FROM " + compdb(comp_id)
					+ "axela_inventory_cat_pop" + " GROUP BY cat_id"
					+ " ORDER BY cat_rank, cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Keyword</option>\n");
			Str.append("<option value=-1")
					.append(StrSelectdrop("-1", item_itemgroup_id)).append(">")
					.append("Code");
			Str.append("</option>\n");
			Str.append("<option value=-2")
					.append(StrSelectdrop("-2", item_itemgroup_id)).append(">")
					.append("Name");
			Str.append("</option>\n");

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"),
						item_itemgroup_id));
				Str.append(">").append(crs.getString("cat_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name=dr_contact_id class=form-control id=dr_contact_id >");
			if (voucher_jc_id.equals("0")) {
				Str.append("<option value = 0>Select</option>");
			}
			StrSql = "SELECT contact_id, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " customer_city_id"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = " + CNumeric(customer_id)
					+ " GROUP BY contact_id" + " order by contact_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("STRSQL===popcon=="+StrSql);
			prepmap.clear();
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				if (!voucher_so_id.equals("0") && add.equals("yes")) {
					Str.append(StrSelectdrop(crs.getString("contact_id"), contact_id)).append(">");
				}
				else {
					Str.append(StrSelectdrop(crs.getString("contact_id"), voucher_contact_id)).append(">");
				}
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
	public String PopulateBranchClass(String customer_branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT rateclass_id, rateclass_name" + " FROM "
					+ compdb(comp_id) + "axela_rate_class" + " WHERE 1=1"
					+ " AND rateclass_type = 1" + " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("rateclass_id"));
				Str.append(
						Selectdrop(Integer.parseInt(CNumeric(crs
								.getString("rateclass_id"))),
								voucher_rateclass_id)).append(">");
				Str.append(crs.getString("rateclass_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLocation(String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id
					+ " ORDER BY location_name";
			// SOP("location===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(" " + StrSelectdrop(crs.getString("location_id"), vouchertrans_location_id));
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
					+ " FROM " + compdb(comp_id) + "axela_customer"
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public void BeforeAddRoutine(HttpServletRequest request) {
		try {
			// --------- 1. Code to get branch_id and vouchertrans_location_id---------
			if (emp_branch_id.equals("0")) {
				branch_id = CNumeric(GetSession("voucher_branch_id", request));
				if (voucher_jc_id.equals("0")) {
					vouchertrans_location_id = CNumeric(GetSession("vouchertrans_location_id", request));
				}

				if (!voucher_vehstock_id.equals("0")) {
					branch_id = so_branch_id;
					StrSql = "SELECT location_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + so_branch_id
							+ " ORDER BY location_name LIMIT 1";
					vouchertrans_location_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
				}

				if (branch_id.equals("0")) {
					// branch_id = getActiveBranchID(request, emp_id);
					SetSession("voucher_branch_id", branch_id, request);
					if (!branch_id.equals("0")) {
						if (comp_module_inventory.equals("1")) {
							StrSql = "SELECT location_id FROM " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = " + branch_id + "" + " LIMIT 1";
							vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
							SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
						}
					}
				}
			} else {
				branch_id = emp_branch_id;
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id FROM " + compdb(comp_id)
							+ "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}

			}
			if (!customer_branch_id.equals("0") && customer_add.equals("yes")) {
				branch_id = customer_branch_id;
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id FROM " + compdb(comp_id)
							+ "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}

			}
			// atleast for getting one active branch
			if (branch_id.equals("0")) {
				StrSql = "SELECT branch_id" + " FROM " + compdb(comp_id)
						+ "axela_branch" + " WHERE 1=1"
						// + " branch_company_id = " + comp_id
						+ " LIMIT 1";
				branch_id = CNumeric(ExecuteQuery(StrSql));
				SetSession("voucher_branch_id", branch_id, request);
				if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id FROM " + compdb(comp_id)
							+ "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					// SOP("vouchertrans_location_id==="+vouchertrans_location_id);
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}
			}
			if (!so_branch_id.equals("0")) {
				branch_id = so_branch_id;
			}
			branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id = " + branch_id);
			if (vouchertrans_location_id.equals("0") && !branch_id.equals("0")) {
				StrSql = "SELECT location_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + branch_id + ""
						+ " LIMIT 1";

				vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
			}
			location_name = ExecuteQuery("SELECT CONCAT(location_name, ' (', location_code, ')') AS location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_id = " + vouchertrans_location_id);
			// --------------End Of Code

			// 1.----------Query to delete cart times added other then today dont put session_id filter...--------------------
			// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
			// + " WHERE 1=1"
			// + " AND cart_vouchertype_id = " + vouchertype_id
			// + " AND cart_emp_id = " + emp_id
			// + " AND cart_time < " + ConvertShortDateToStr(AddDayMonthYear(strToLongDate(ToLongDate(kknow())), -1, 0, 0, 0));
			// SOP("StrSql=de=="+StrSql);
			// updateQuery(StrSql);
			// --------------End Of Code 1.----------------------

			// ------2. Code to copy items from one voucher to another
			// voucher---------------
			if (!voucher_enquiry_id.equals("0") || !voucher_customer_id.equals("0")) { // To be checked
				StrSql = "SELECT enquiry_customer_id, enquiry_contact_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + voucher_enquiry_id;
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					voucher_customer_id = crs.getString("enquiry_customer_id");
					voucher_contact_id = crs.getString("enquiry_contact_id");
				}
				crs.close();
				if (!CNumeric(voucher_customer_id).equals("0")) {
					StrSql = "SELECT customer_rateclass_id, customer_curr_bal, customer_credit_limit,"
							+ " COALESCE(customer_address, '') as customer_address,"
							+ " COALESCE(city_name, '') AS city_name, COALESCE(customer_pin, '') as customer_pin,"
							+ " COALESCE(state_name, '') AS state_name,"
							+ " COALESCE(customer_landmark, '') as customer_landmark"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE customer_id = " + voucher_customer_id;
					// SOP("StrSql=enquiry==" + StrSql);
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						customer_curr_bal = crs1.getString("customer_curr_bal");
						customer_credit_limit = crs1
								.getString("customer_credit_limit");
						voucher_billing_add = crs1.getString("customer_address");

						if (!voucher_billing_add.equals("")) {
							if (!crs1.getString("city_name").equals("")) {
								voucher_billing_add += ", "
										+ crs1.getString("city_name");
							}
							if (!crs1.getString("customer_pin").equals("")) {
								voucher_billing_add += " - "
										+ crs1.getString("customer_pin");
							}
							if (!crs1.getString("state_name").equals("")) {
								voucher_billing_add += ", "
										+ crs1.getString("state_name");
							}
							if (!crs1.getString("customer_landmark").equals("")) {
								voucher_billing_add += "\nLandmark: "
										+ crs1.getString("customer_landmark");
							}
						}
					}
					crs1.close();
				}

			}
			if (!voucher_quote_id.equals("0")) {
				StrSql = "SELECT"
						+ " voucher_customer_id "
						+ "FROM " + compdb(comp_id) + "axela_acc_voucher"
						// + " INNER JOIN axela_customer ON voucher_customer_id = customer_id"
						+ " WHERE voucher_id = " + voucher_quote_id;
				voucher_customer_id = ExecuteQuery(StrSql);
			}

			if (!voucher_quote_id.equals("0") || !voucher_customer_id.equals("0")) { // To be checked
				StrSql = "SELECT voucher_customer_id, voucher_contact_id, voucher_rateclass_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_quote_id;
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");
				}
				crs.close();
				if (!CNumeric(voucher_customer_id).equals("0")) {
					StrSql = "SELECT customer_rateclass_id, customer_curr_bal, customer_credit_limit,"
							+ " COALESCE(customer_address, '') as customer_address,"
							+ " COALESCE(city_name, '') AS city_name, COALESCE(customer_pin, '') as customer_pin,"
							+ " COALESCE(state_name, '') AS state_name,"
							+ " COALESCE(customer_landmark, '') as customer_landmark"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE customer_id = " + voucher_customer_id + "";
					// SOP("StrSql=enquiry==" + StrSql);
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						customer_curr_bal = crs1.getString("customer_curr_bal");
						customer_credit_limit = crs1.getString("customer_credit_limit");
						voucher_billing_add = crs1.getString("customer_address");

						if (!voucher_billing_add.equals("")) {
							if (!crs1.getString("city_name").equals("")) {
								voucher_billing_add += ", " + crs1.getString("city_name");
							}
							if (!crs1.getString("customer_pin").equals("")) {
								voucher_billing_add += " - " + crs1.getString("customer_pin");
							}
							if (!crs1.getString("state_name").equals("")) {
								voucher_billing_add += ", " + crs1.getString("state_name");
							}
							if (!crs1.getString("customer_landmark").equals("")) {
								voucher_billing_add += "\nLandmark: " + crs1.getString("customer_landmark");
							}
						}
					}
					crs1.close();
				}
			}

			if (!voucher_quote_id.equals("0")) {
				CopyVoucherTransToCart(request, emp_id, session_id, voucher_quote_id, vouchertype_id, "Update");
			}
			if (!voucher_so_id.equals("0") || !voucher_delnote_id.equals("0")) {
				parentvoucher = "yes";
				if (!voucher_so_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_so_id;
				}
				if (!voucher_so_id.equals("0")) {
					voucher_emp_id = CNumeric(ExecuteQuery("SELECT so_emp_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE so_id = " + voucher_so_id));
				}

				location_name = ExecuteQuery("SELECT location_name"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_id = " + vouchertrans_location_id);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// only one sales-invoice can be there for one sales-order
	public void checkinvoice(HttpServletResponse response) {
		String voucher = "0";
		try {
			StrSql = "SELECT"
					+ " voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1 = 1"
					+ " AND voucher_vouchertype_id = '" + vouchertype_id + "'";

			if (!voucher_preorder_id.equals("0") && !voucher_preorder_id.equals("")) {
				StrSql += " AND voucher_preorder_id = " + voucher_preorder_id + "";
			} else if (!voucher_jc_id.equals("0") && !voucher_jc_id.equals("")) {
				StrSql += " AND voucher_jc_id = " + voucher_jc_id + "";
			} else if (!voucher_so_id.equals("0") && !voucher_so_id.equals("")) {
				StrSql += " AND voucher_so_id = " + voucher_so_id + "";
			}

			voucher = CNumeric(ExecuteQuery(StrSql));
			if (!voucher.equals("0")) {
				msg = "Error!</br>Sales Invoice Already Exist!";
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg + ""));
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void populatePreOrder(HttpServletRequest request, HttpServletResponse response) {
		StrSql = "SELECT so_branch_id, so_customer_id,"
				+ " so_contact_id, so_location_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_id = " + preorder_so_id + "";
		CachedRowSet crs1 = processQuery(StrSql, 0);

		try {
			while (crs1.next()) {
				voucher_customer_id = crs1.getString("so_customer_id");
				voucher_contact_id = crs1.getString("so_contact_id");
				contact_id = crs1.getString("so_contact_id");
			}

			if (!CNumeric(voucher_customer_id).equals("0")) {
				StrSql = "SELECT customer_rateclass_id, customer_curr_bal, customer_credit_limit,"
						+ " COALESCE(customer_address, '') as customer_address,"
						+ " COALESCE(city_name, '') AS city_name, COALESCE(customer_pin, '') as customer_pin,"
						+ " COALESCE(state_name, '') AS state_name,"
						+ " COALESCE(customer_landmark, '') as customer_landmark"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " WHERE customer_id = " + voucher_customer_id + "";
				// SOP("StrSql=enquiry==" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {

					customer_curr_bal = crs1.getString("customer_curr_bal");
					customer_credit_limit = crs1.getString("customer_credit_limit");
					voucher_billing_add = crs1.getString("customer_address");

					if (!voucher_billing_add.equals("")) {
						if (!crs1.getString("city_name").equals("")) {
							voucher_billing_add += ", " + crs1.getString("city_name");
						}
						if (!crs1.getString("customer_pin").equals("")) {
							voucher_billing_add += " - " + crs1.getString("customer_pin");
						}
						if (!crs1.getString("state_name").equals("")) {
							voucher_billing_add += ", " + crs1.getString("state_name");
						}
						if (!crs1.getString("customer_landmark").equals("")) {
							voucher_billing_add += "\nLandmark: " + crs1.getString("customer_landmark");
						}
						voucher_consignee_add = voucher_billing_add;
					}
				}
			}
			crs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
}