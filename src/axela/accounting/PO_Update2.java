package axela.accounting;

//Shivaprasad Nov 18 2014  

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
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class PO_Update2 extends Connect {

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
	public String incdec_id = "1";
	public String ledger_id = "0";

	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String voucher_rateclass_id = "0";
	public String item_deliverydate = "";
	public String voucher_id = "0";
	public String voucherclass_id = "0", voucherclass_file = "";
	public String voucherclass_acc = "0";
	public String vouchertype_id = "0";
	public String voucher_delivery_date = "";
	public String voucher_supplier_id = "0";
	public String voucher_date = "";
	public String voucherdate = "";
	public String voucher_enquiry_id = "0", voucher_lead_id = "0";
	public int voucher_colspan = 1;
	public String voucher_emp_id = "0";
	public String voucher_ref_no = "";
	public String voucher_active = "1";
	public String voucher_notes = "";

	public String gst_type = "";

	public String price_tax1_id = "0";
	public String price_tax2_id = "0";
	public String price_tax3_id = "0";

	public String voucher_grandtotal = "0.00", voucher_netprice = "0.00";
	public String voucher_payment_date = "", voucher_paydays_id = "0";
	public String paydays_days = "0";
	public String paymentdate = "";
	public String voucher_prev_grandtotal = "";
	public String voucher_no = "";
	public String voucher_consignee_add = "", voucher_billing_add = "";

	public String voucher_special = "0", voucher_authorize = "";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";
	public String voucher_pending = "0";
	public String voucher_gatepass = "";
	public String voucher_lrno = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_cashdiscount = "";
	public String voucher_turnoverdisc = "";

	public String voucherno = "";
	public String voucher_amount = "";
	public String vouchertrans_location_id = "0";
	public String temp_vouchertrans_location_id = "0";
	public String vouchertrans_from_location_id = "0";
	public String checkbranch = "0", check_voucher_no = "";

	public String vouchertype_defaultauthorize = "0";
	public String vouchertype_authorize = "0";
	// for round off
	public String vouchertype_roundoff = "0";
	public String vouchertype_roundoff_ledger_cr = "0";
	public String vouchertype_roundoff_ledger_dr = "0";

	public String voucher_entry_id = "0";
	public String voucher_entry_by = "";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_by = "";
	public String voucher_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String voucher_contact_id = "0";
	public String config_inventory_location_name = "", location_name = "";
	/* End Of Voucher Variables */

	public String vouchertrans_option_id = "0";
	public String vouchertrans_option_group = "";
	public String vouchertrans_item_serial = "";
	public String vouchertrans_item_batch_id = "0";
	public String vouchertrans_price = "";
	public String vouchertrans_qty = "";
	// config variables ///
	public String vouchertype_billing_add = "0";
	public String vouchertype_consignee_add = "0";

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
	public String branch_invoice_email_enable = "0";
	public String branch_invoice_email_format = "";
	public String branch_invoice_email_sub = "";
	public String branch_invoice_sms_enable = "0";
	public String branch_invoice_sms_format = "";
	public String branch_invoice_email_exe_sub = "";
	public String branch_invoice_email_exe_format = "";
	public String config_refno_enable = "0";
	public String config_sales_invoice_refno = "";
	public String config_inventory_current_stock = "0";
	public String config_invoice_reduce_current_stock = "0";
	public String config_customer_dupnames = "0";
	public String comp_businesstype_id = "0";
	public String emp_invoice_priceupdate = "0";
	public String emp_invoice_discountupdate = "0";
	public String emp_role_id = "0";
	/* End of Config Variables */
	DecimalFormat deci = new DecimalFormat("#.##");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String checkPerm = "";
	public String voucher_branch_id = "0";

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

	public String formatdigit_id = "0";
	public String config_format_decimal = "";
	public String config_customer_name = "";
	public String vouchertrans_voucher_id = "";
	public String vouchertrans_amount = "";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "";
	public String vouchertrans_paymode_id = "";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "";
	public String vouchertrans_cheque_bank = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_name = "", customer_add = "";
	public String customer_branch_id = "0";
	public String po_tax_id1, po_tax_rate1, po_tax_customer_id1;
	public String po_tax_id2, po_tax_rate2, po_tax_customer_id2;
	public String po_tax_id3, po_tax_rate3, po_tax_customer_id3;
	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public String vouchertype_affects_inventory = "0";
	public String customer_id = "0";
	public String item_cat_id = "0";
	// All purchase vouchers id
	public String voucher_po_id = "0", voucher_git_id = "0";
	public String voucher_grn_id = "0", voucher_poreturn_id = "0";
	public String voucher_purchaseinvoice_id = "0";
	public String voucher_stockadj_id = "0";
	public String voucher_stocktransfer_id = "0";
	public String temp_voucher_id = "0";
	public String copyvouchertrans_voucher_id = "0", refresh = "";
	public String session_id = "", parentvoucher = "";
	public String stockadjustment = "";

	public int row_no = 1;
	public Ledger_Check ledgercheck = new Ledger_Check();

	// for Stock
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(false);
			comp_id = CNumeric(GetSession("comp_id", request));
			voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
			vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
			voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
			String StrSql = "SELECT voucherclass_id , voucherclass_file"
					+ " FROM axela_acc_voucher_class"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " WHERE vouchertype_id = ?";
			prepmap.put(prepkey++, Integer.parseInt(vouchertype_id));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			while (crs.next()) {
				if (crs.getString("voucherclass_id").equals("1")) {
					voucherclass_file = "stock_adjustment";
				} else if (crs.getString("voucherclass_id").equals("2")) {
					voucherclass_file = "stock_transfer";
				} else if (crs.getString("voucherclass_id").equals("12")) {
					voucherclass_file = "acc_purchase_order";
				} else if (crs.getString("voucherclass_id").equals("20")) {
					voucherclass_file = "acc_grn";
				} else if (crs.getString("voucherclass_id").equals("21")) {
					voucherclass_file = "acc_purchase_invoice";
				} else {
					voucherclass_file = crs.getString("voucherclass_file").toLowerCase();
				}
			}
			crs.close();
			CheckPerm(comp_id, "emp_" + voucherclass_file + "_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request) + "";
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request) + "");
				config_inventory_location_name = GetSession("config_inventory_location_name", request) + "";
				config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request) + "");
				refresh = GetSession("refresh", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				formatdigit_id = GetSession("formatdigit_id", request);
				config_format_decimal = GetSession("config_format_decimal", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				stockadjustment = PadQuotes(request.getParameter("stockadjustment"));
				if (deleteB.contains("&#40") && deleteB.contains("&#41")) {
					deleteB = deleteB.replace("&#40;", "(");
					deleteB = deleteB.replace("&#41;", ")");
				}
				msg = PadQuotes(request.getParameter("msg"));
				// voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				// vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));

				// / for stock transfer setting rateclass by default as "A" rateclass!
				if (vouchertype_id.equals("2")) {
					voucher_rateclass_id = "1";
				}
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				customer_add = PadQuotes(request.getParameter("customer_add"));
				item_deliverydate = DateToShortDate(kknow());
				// purchase vouchers id
				voucher_po_id = CNumeric(PadQuotes(request.getParameter("voucher_po_id")));
				voucher_git_id = CNumeric(PadQuotes(request.getParameter("voucher_git_id")));
				voucher_purchaseinvoice_id = CNumeric(PadQuotes(request.getParameter("voucher_purchaseinvoice_id")));
				voucher_grn_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_id")));
				voucher_poreturn_id = CNumeric(PadQuotes(request.getParameter("voucher_poreturn_id")));
				// stock adjustment
				voucher_stockadj_id = CNumeric(PadQuotes(request.getParameter("voucher_stockadj_id")));
				// stock transfer
				voucher_stocktransfer_id = CNumeric(PadQuotes(request.getParameter("voucher_stocktransfer_id")));
				// for stock
				// For Generating session each time
				session_id = PadQuotes(request.getParameter("txt_session_id"));

				PopulateConfigDetails();

				empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}

				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				// voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				QueryString = PadQuotes(request.getQueryString());

				// -----------1. code to add customer from add ledger link--------------------

				if (customer_add.equals("yes")) {
					customer_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + branch_id);
					voucher_supplier_id = PadQuotes(request.getParameter("voucher_customer_id"));
					voucher_contact_id = PadQuotes(request.getParameter("voucher_contact_id"));
					voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("voucher_rateclass_id")));
				}
				// -----------End Of Code
				if (add.equals("yes")) {
					status = "Add";
					display = "none";
					voucher_date = ToShortDate(kknow());
					voucherdate = strToShortDate(voucher_date);
					paymentdate = DateToShortDate(kknow());
					voucher_emp_id = emp_id;
					if (!addB.equals("yes")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						BeforeAddRoutine(request);
						gst_type = GetGstType(voucher_supplier_id, branch_id, comp_id);
					} else {
						if (ReturnPerm(
								comp_id, "emp_acc_voucher_add,"
										+ " emp_acc_purchase_order_add,"
										+ " emp_acc_purchase_invoice_add,"
										+ " emp_acc_grn_add,"
										+ " emp_acc_purchase_return_add,"
										+ " emp_stock_transfer_add,"
										+ " emp_stock_adjustment_add", request).equals("1")) {
							GetValues(request, response);
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id + "&msg="
										+ vouchertype_name + " added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes")
							&& !deleteB.equals("Delete " + vouchertype_name + "")
							&& temp_voucher_id.equals("0")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						PopulateFields(request, response);
						gst_type = GetGstType(voucher_supplier_id, branch_id, comp_id);
						if (!refresh.equals("no")) {

							// CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
						}
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete " + vouchertype_name + "")) {
						// checking for any sub voucher added for the prest voucher
						temp_voucher_id = CNumeric(ExecuteQuery("SELECT voucher_id"
								+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
								+ " WHERE 1=1"
								+ " AND (voucher_po_id = " + voucher_id + " OR voucher_git_id = " + voucher_id + " OR voucher_grn_id = " + voucher_id + ")"));
						if (status.equals("Update") && !temp_voucher_id.equals("0")) {
							response.sendRedirect("../portal/error.jsp?msg= " + vouchertype_name + " can't be updated!");
						} else {
							if (ReturnPerm(
									comp_id, "emp_acc_voucher_edit,"
											+ "emp_acc_purchase_order_edit,"
											+ "emp_acc_purchase_invoice_edit,"
											+ "emp_acc_grn_edit,"
											+ "emp_acc_purchase_return_edit,"
											+ "emp_stock_transfer_edit,"
											+ "emp_stock_adjustment_edit", request).equals("1")) {

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
						}
					} else if (deleteB.equals("Delete " + vouchertype_name + "")) {
						if (ReturnPerm(
								comp_id, "emp_acc_voucher_delete,"
										+ "emp_acc_purchase_order_delete,"
										+ "emp_acc_purchase_invoice_delete,"
										+ "emp_acc_grn_delete,"
										+ "emp_acc_purchase_return_delete,"
										+ "emp_stock_transfer_delete,"
										+ "emp_stock_adjustment_delete", request).equals("1")) {
							response.sendRedirect(AccessDenied());
							// GetValues(request, response);
							// DeleteFields(request, response);
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		voucher_no = CNumeric(PadQuotes(request.getParameter("txt_voucher_no")));
		location_name = PadQuotes(request.getParameter("txt_location_name"));
		voucher_po_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_po_id")));
		voucher_git_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_git_id")));
		voucher_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_grand_total")));
		voucher_grn_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_grn_id")));
		price_tax1_id = CNumeric(PadQuotes(request.getParameter("dr_price_tax1")));
		price_tax2_id = CNumeric(PadQuotes(request.getParameter("dr_price_tax2")));
		price_tax3_id = CNumeric(PadQuotes(request.getParameter("dr_price_tax3")));
		branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
		voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_rateclass_id")));
		gst_type = PadQuotes(request.getParameter("txt_gst_type"));
		ledger_id = CNumeric(PadQuotes(request.getParameter("dr_Legder_id")));
		voucher_lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		voucher_delivery_date = PadQuotes(request.getParameter("txt_voucher_delivery_date"));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_supplier_id = PadQuotes(request.getParameter("accountingsupplier"));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		if (vouchertype_id.equals("12") || vouchertype_id.equals("20") || (vouchertype_id.equals("21") && voucher_grn_id.equals("0"))) {
			vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
		} else if (vouchertype_id.equals("21") && !voucher_grn_id.equals("0")) {
			vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("txt_location_id")));
		} else {
			vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("txt_location_id")));
		}
		if (vouchertype_id.equals("2") || vouchertype_id.equals("1")) {
			vouchertrans_from_location_id = CNumeric(PadQuotes(request.getParameter("dr_vouchertrans_from_location_id")));
			vouchertrans_location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
		}
		voucher_paydays_id = CNumeric(PadQuotes(request.getParameter("dr_paydays_id")));
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		paymentdate = PadQuotes(request.getParameter("txt_voucher_payment_date"));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		PopulateConfigDetails();
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
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		voucher_entry_by = PadQuotes(request.getParameter("entry_by"));
		voucher_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		voucher_billing_add = PadQuotes(request.getParameter("txt_voucher_billing_add"));
		voucher_consignee_add = PadQuotes(request.getParameter("txt_voucher_consignee_add"));
		voucher_gatepass = PadQuotes(request.getParameter("txt_voucher_gatepass"));
		voucher_lrno = PadQuotes(request.getParameter("txt_voucher_lrno"));
		voucher_driver_no = PadQuotes(request.getParameter("txt_voucher_driver_no"));
		voucher_tempo_no = PadQuotes(request.getParameter("txt_voucher_tempo_no"));
		voucher_cashdiscount = CNumeric(PadQuotes(request.getParameter("txt_voucher_cashdiscount")));
		voucher_turnoverdisc = CNumeric(PadQuotes(request.getParameter("txt_voucher_turnoverdisc")));
		voucher_special = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_special")));
		voucher_pending = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_pending")));
		po_tax_id1 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_id")));
		po_tax_customer_id1 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_customer_id")));
		po_tax_rate1 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_rate")));
		po_tax_id2 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_id")));
		po_tax_customer_id2 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_customer_id")));
		po_tax_rate2 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_rate")));
		po_tax_id3 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_id")));
		po_tax_customer_id3 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_customer_id")));
		po_tax_rate3 = CNumeric(PadQuotes(request.getParameter("txt_po_tax_rate")));
	}
	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (voucherdate.equals("")) {
			msg += "<br>Enter " + vouchertype_name + " Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>" + vouchertype_name + " Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter valid " + vouchertype_name + " Date!";
			}
		}

		if (comp_module_inventory.equals("1")
				&& config_inventory_current_stock.equals("1")
				&& vouchertrans_location_id.equals("0")
				&& location_name.equals("")) {
			msg += "<br>Select Location!";
		}

		if (vouchertype_id.equals("2")) {

			if (!vouchertrans_from_location_id.equals(vouchertrans_location_id)) {
				if (vouchertrans_from_location_id.equals("0")) {
					msg += "<br>Select From Location!";
				}
				if (vouchertrans_location_id.equals("0")) {
					msg += "<br>Select To Location!";
				}
			} else {
				msg += "<br>From and To Location can not be same!";
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

		if (!vouchertype_id.equals("1") && !vouchertype_id.equals("2")) {
			if (voucher_supplier_id.equals("")) {
				msg += "<br>Select Supplier!";
			}

			if (voucher_contact_id.equals("0")) {
				msg += "<br>Select Contact!";
			}
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

		if (!vouchertype_id.equals("1") && !vouchertype_id.equals("2")) {

			if (paymentdate.equals("")) {
				msg += "<br>Select Payment Date!";
			} else {
				if (isValidDateFormatShort(voucherdate)) {
					voucher_payment_date = ConvertShortDateToStr(paymentdate);
					if (Long.parseLong(voucher_payment_date) < Long.parseLong(voucher_date)) {
						msg += "<br>Payment Date must be greater than or equal to " + vouchertype_name + " Date!";
					}
				} else {
					msg += "<br>Select valid Payment Date!";
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

			// if (!branch_id.equals("0")) {
			StrSql = "SELECT voucher_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_branch_id = " + branch_id + " "
					+ " AND voucher_ref_no = '" + voucher_ref_no + "'"
					+ " AND voucher_id != " + voucher_id + ""
					+ " AND voucher_vouchertype_id = " + vouchertype_id;
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Reference No. found!";
			}
			// }
		}

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// StrSql = "SELECT COALESCE(SUM(cart_amount),0)-(SELECT COALESCE(SUM(discount.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart discount"
				// + " WHERE 1=1"
				// + " AND discount.cart_emp_id = " + emp_id + ""
				// + " AND discount.cart_session_id = " + session_id + ""
				// + " AND discount.cart_vouchertype_id = " + vouchertype_id + ""
				// + " AND discount.cart_discount = 1)"
				// + " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart tax"
				// + " WHERE 1 = 1"
				// + " AND tax.cart_emp_id = " + emp_id + ""
				// + " AND tax.cart_session_id = " + session_id + ""
				// + " AND tax.cart_vouchertype_id = " + vouchertype_id + ""
				// + " AND tax.cart_tax = 1)"
				// + " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart billsundry"
				// + " WHERE 1 = 1"
				// + " AND billsundry.cart_emp_id = " + emp_id + ""
				// + " AND billsundry.cart_session_id = " + session_id + ""
				// + " AND billsundry.cart_vouchertype_id = " + vouchertype_id + ""
				// + " AND billsundry.cart_rowcount = 0"
				// + " AND billsundry.cart_option_id = 0)"
				// + " FROM " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE 1 = 1"
				// + " AND cart_emp_id = " + emp_id + ""
				// + " AND cart_session_id = " + session_id + ""
				// + " AND cart_vouchertype_id = " + vouchertype_id + ""
				// + " AND cart_item_id != 0"
				// + " AND cart_discount = 0"
				// + " AND cart_tax = 0";
				// SOP("StrSql==grandtotal=" + StrSql);
				// if (!vouchertype_id.equals("1")) {
				// voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));
				// } else {
				// voucher_grandtotal = ExecuteQuery(StrSql);
				// if (Double.parseDouble(voucher_grandtotal) < 0) {
				// voucher_grandtotal = voucher_grandtotal.substring(1, voucher_grandtotal.length());
				// }
				// }
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher ("
						+ " voucher_vouchertype_id,"
						+ " voucher_branch_id,"
						+ " voucher_location_id,"
						+ " voucher_no,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_lead_id,"
						+ " voucher_enquiry_id,"
						+ " voucher_po_id,"
						+ " voucher_git_id,"
						+ " voucher_grn_id,"
						+ " voucher_rateclass_id,"
						+ " voucher_emp_id,"
						+ " voucher_payment_date,"
						+ " voucher_billing_add,"
						+ " voucher_consignee_add,"
						+ " voucher_gatepass,"
						+ " voucher_lrno,"
						+ " voucher_driver_no,"
						+ " voucher_tempo_no,"
						+ " voucher_cashdiscount,"
						+ " voucher_turnoverdisc,"
						+ " voucher_special,"
						+ " voucher_pending,"
						+ " voucher_ref_no,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_notes,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date"
						+ " )  VALUES" + " ("
						+ " " + vouchertype_id + ","
						+ " " + branch_id + ","
						+ " " + vouchertrans_location_id + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " '" + voucher_date + "',"
						+ " " + Double.parseDouble(voucher_grandtotal) + ","
						+ " " + CNumeric(voucher_supplier_id) + ","
						+ " " + CNumeric(voucher_contact_id) + ","
						+ " " + voucher_lead_id + ","
						+ " " + voucher_enquiry_id + ","
						+ " " + voucher_po_id + ","
						+ " " + voucher_git_id + ","
						+ " " + voucher_grn_id + ","
						+ " " + voucher_rateclass_id + ","
						+ " " + voucher_emp_id + ","
						+ " '" + voucher_payment_date + "',"
						+ " '" + voucher_billing_add + "',"
						+ " '" + voucher_consignee_add + "',"
						+ " '" + voucher_gatepass + "',"
						+ " '" + voucher_lrno + "',"
						+ " '" + voucher_driver_no + "',"
						+ " '" + voucher_tempo_no + "',"
						+ " " + voucher_cashdiscount + ","
						+ " " + voucher_turnoverdisc + ","
						+ " '" + voucher_special + "',"
						+ " '" + voucher_pending + "',"
						+ " '" + voucher_ref_no + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " '" + vouchertype_terms + "',"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_notes + "',"
						+ " " + emp_id + "," + " "
						+ ToLongDate(kknow()) + ")";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					voucher_id = rs1.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs1.close();
				AddItemFields(request);
				// StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
				// + " WHERE 1 = 1"
				// + " AND cart_emp_id = " + emp_id
				// + " AND cart_session_id = " + session_id
				// + " AND cart_vouchertype_id = " + vouchertype_id;
				// // SOP("StrSql=del=="+StrSql);
				// stmttx.addBatch(StrSql);

				stmttx.executeBatch();
				if (!voucher_purchaseinvoice_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + " axela_acc_voucher "
							+ " SET voucher_grn_id = " + voucher_id
							+ " WHERE voucher_id = " + voucher_purchaseinvoice_id;
					stmttx.execute(StrSql);
				}
				conntx.commit();
				// / customer current balance
				if (voucherclass_acc.equals("1")) {
					// test111
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_supplier_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// ---------------
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}
				// item stock
				if (msg.equals("") && comp_module_inventory.equals("1") && vouchertype_affects_inventory.equals("1") && !vouchertype_id.equals("21")) {
					// test111
					// SOP("vouchertrans_location_id==" + vouchertrans_location_id);
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();

					// -------------
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
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
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
							+ " " + ToLongDate(kknow()) + ",";
					if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
						StrSql += " '1'";// vouchertrans_dc
					} else if (vouchertype_id.equals("24")) {
						StrSql += " '0'";// vouchertrans_dc
					}
					StrSql += " )";
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
							+ " " + ToLongDate(kknow()) + ",";
					if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
						StrSql += " '0'";// vouchertrans_dc
					} else if (vouchertype_id.equals("24")) {
						StrSql += " '1'";// vouchertrans_dc
					}
					StrSql += " )";
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
								+ " " + ToLongDate(kknow()) + ",";
						if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
							StrSql += " '1'";// vouchertrans_dc
						} else if (vouchertype_id.equals("24")) {
							StrSql += " '0'";// vouchertrans_dc
						}
						StrSql += " )";
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
								+ " " + ToLongDate(kknow()) + ",";
						if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
							StrSql += " '1'";// vouchertrans_dc
						} else if (vouchertype_id.equals("24")) {
							StrSql += " '0'";// vouchertrans_dc
						}
						StrSql += " )";
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
								+ " " + ToLongDate(kknow()) + ",";
						if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
							StrSql += " '1'";// vouchertrans_dc
						} else if (vouchertype_id.equals("24")) {
							StrSql += " '0'";// vouchertrans_dc
						}
						StrSql += " )";
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
					StrSql += " '1'" + ", ";
				} else if (roundoff_amount < 0) {
					StrSql += " '0'" + ",";
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
					+ " " + voucher_supplier_id + ","
					+ " " + CNumeric(PadQuotes(request.getParameter("dr_location_id"))) + ","
					+ " " + CNumeric(PadQuotes(request.getParameter("txt_grand_total"))) + ","
					+ " " + ToLongDate(kknow()) + ",";
			if (vouchertype_id.equals("20") || vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
				StrSql += " '0'";// vouchertrans_dc
			} else if (vouchertype_id.equals("24")) {
				StrSql += " '1'";// vouchertrans_dc
			}
			StrSql += " )";

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
	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT COALESCE(SUM(vouchertrans_qty), 0) " + " FROM "
					+ compdb(comp_id) + " axela_acc_voucher_trans "
					+ " WHERE vouchertrans_voucher_id = " + voucher_id
					+ "	AND vouchertrans_item_id != 0 "
					+ "	AND vouchertrans_rowcount != 0";
			if (Double.parseDouble(ExecuteQuery(StrSql)) >= 0) {
				incdec_id = "1";
			} else {
				incdec_id = "2";
			}

			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id,"
					+ " voucher_no,"
					+ " COALESCE(voucher_po_id, 0) AS voucher_po_id,"
					+ " COALESCE(voucher_git_id, 0) AS voucher_git_id,"
					+ " COALESCE(voucher_grn_id, 0) AS voucher_grn_id,"
					+ " voucher_amount, voucher_customer_id, voucher_payment_date,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " voucher_rateclass_id,"
					+ " voucher_date, voucher_branch_id, voucher_location_id,"
					+ " voucher_customer_id, voucher_contact_id,"
					+ " vouchertrans_paymode_id, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_branch, vouchertrans_cheque_date,"
					+ " vouchertrans_cheque_bank, voucher_notes,"
					+ " voucher_emp_id, voucher_ref_no,"
					+ " voucher_special, voucher_pending,"
					+ " voucher_active, voucher_notes, voucher_entry_id,"
					+ " voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, voucher_authorize,voucher_authorize_id,voucher_authorize_time,"
					+ " COALESCE(vouchertrans_location_id, 0) AS vouchertrans_location_id,"
					+ " COALESCE(vouchertrans_from_location_id, 0) AS vouchertrans_from_location_id,"
					+ " COALESCE(toloc.location_name, '') AS location_name,"
					+ " COALESCE(voucher_enquiry_id, 0) AS voucher_enquiry_id,"
					+ " COALESCE(fromloc.location_name, '') AS from_location_name,"
					+ " voucher_billing_add, voucher_consignee_add, voucher_gatepass,"
					+ " voucher_lrno, voucher_driver_no, voucher_tempo_no, voucher_cashdiscount, voucher_turnoverdisc,"
					+ " (SELECT vouchertrans_customer_id"
					+ " FROM " + compdb(comp_id) + " axela_acc_voucher_trans sub WHERE sub.vouchertrans_voucher_id = voucher_id"
					+ " AND sub.vouchertrans_item_id !=0 AND sub.vouchertrans_tax =0 AND sub.vouchertrans_discount =0 AND sub.vouchertrans_rowcount !=0 LIMIT 1) AS ledger_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id";
			if (vouchertype_id.equals("1") || vouchertype_id.equals("2")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id";
			} else {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location toloc ON toloc.location_id = voucher_location_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_inventory_location fromloc ON fromloc.location_id = vouchertrans_from_location_id"
					+ " WHERE voucher_id = " + voucher_id + ExeAccess;
			// SOP("StrSql=popu==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					voucher_po_id = crs.getString("voucher_po_id");
					voucher_git_id = crs.getString("voucher_git_id");
					voucher_grn_id = crs.getString("voucher_grn_id");
					branch_id = crs.getString("voucher_branch_id");
					checkbranch = branch_id;
					branch_name = crs.getString("branch_name");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");
					voucher_supplier_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucherdate = strToShortDate(crs.getString("voucher_date"));
					paymentdate = strToShortDate(crs.getString("voucher_payment_date"));
					voucher_billing_add = crs.getString("voucher_billing_add");
					voucher_consignee_add = crs.getString("voucher_consignee_add");
					voucher_gatepass = crs.getString("voucher_gatepass");
					voucher_lrno = crs.getString("voucher_lrno");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_cashdiscount = crs.getString("voucher_cashdiscount");
					voucher_turnoverdisc = crs.getString("voucher_turnoverdisc");
					ledger_id = crs.getString("ledger_id");
					voucher_grandtotal = crs.getString("voucher_amount");
					vouchertrans_location_id = crs.getString("voucher_location_id");
					temp_vouchertrans_location_id = crs.getString("voucher_location_id");
					vouchertrans_from_location_id = crs.getString("vouchertrans_from_location_id");
					location_name = crs.getString("location_name");
					voucher_enquiry_id = crs.getString("voucher_enquiry_id");
					voucher_special = crs.getString("voucher_special");
					voucher_pending = crs.getString("voucher_pending");
					voucher_ref_no = crs.getString("voucher_ref_no");
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

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {

		CheckForm(request);
		if (msg.equals("")) {
			CheckPageperm(response);
		}

		// StrSql = "SELECT COALESCE(SUM(cart_amount),0)"
		// + "-(SELECT COALESCE(SUM(discount.cart_amount),0)"
		// + " FROM  " + compdb(comp_id) + "axela_acc_cart discount"
		// + " WHERE 1=1"
		// + " AND discount.cart_emp_id = " + emp_id
		// + " AND discount.cart_session_id = " + session_id
		// + " AND discount.cart_vouchertype_id = " + vouchertype_id
		// + " AND discount.cart_discount = 1)"
		// + " +(SELECT COALESCE(SUM(tax.cart_amount),0)"
		// + " FROM  " + compdb(comp_id) + "axela_acc_cart tax"
		// + " WHERE 1=1"
		// + " AND tax.cart_emp_id = " + emp_id
		// + " AND tax.cart_vouchertype_id = " + vouchertype_id
		// + " AND tax.cart_tax = 1)"
		// + " +(SELECT COALESCE(SUM(billsundry.cart_amount),0)"
		// + " FROM  " + compdb(comp_id) + "axela_acc_cart billsundry"
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
		// // SOP("StrSql==grandtotal=" + StrSql);
		// if (!vouchertype_id.equals("1")) {
		// voucher_grandtotal = CNumeric(ExecuteQuery(StrSql));
		// } else {
		// voucher_grandtotal = ExecuteQuery(StrSql);
		// if (Double.parseDouble(voucher_grandtotal) < 0) {
		// voucher_grandtotal = voucher_grandtotal.substring(1,
		// voucher_grandtotal.length());
		// }
		// }
		// SOP("voucher_grandtotal===" + voucher_grandtotal);

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
						+ " SET" + " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_vouchertype_id = " + vouchertype_id + ","
						+ " voucher_branch_id = " + branch_id + ","
						+ " voucher_location_id = " + vouchertrans_location_id + ","
						+ " voucher_date = '" + voucher_date + "',"
						+ " voucher_amount = '" + Double.parseDouble(voucher_grandtotal) + "',"
						+ " voucher_lead_id = " + voucher_lead_id + ","
						+ " voucher_enquiry_id = " + voucher_enquiry_id + ","
						+ " voucher_rateclass_id = " + voucher_rateclass_id + ","
						+ " voucher_customer_id = " + CNumeric(voucher_supplier_id) + ","
						+ " voucher_contact_id = " + CNumeric(voucher_contact_id) + ","
						+ " voucher_emp_id = " + voucher_emp_id + ","
						+ " voucher_paydays_id = " + voucher_paydays_id + ","
						+ " voucher_payment_date = '" + voucher_payment_date + "',"
						+ " voucher_billing_add = '" + voucher_billing_add + "',"
						+ " voucher_consignee_add = '" + voucher_consignee_add + "',"
						+ " voucher_gatepass = '" + voucher_gatepass + "',"
						+ " voucher_lrno = '" + voucher_lrno + "',"
						+ " voucher_driver_no = '" + voucher_driver_no + "',"
						+ " voucher_tempo_no = '" + voucher_tempo_no + "',"
						+ " voucher_cashdiscount = " + voucher_cashdiscount + ","
						+ " voucher_turnoverdisc = " + voucher_turnoverdisc + ","
						+ " voucher_special = '" + voucher_special + "',"
						+ " voucher_pending = '" + voucher_pending + "',"
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_notes = '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow())
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql===up===" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);

				// Copy items FROM voucher_cart to voucher_item
				AddItemFields(request);

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1" + " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + session_id
						+ " AND cart_vouchertype_id =" + vouchertype_id;
				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

				// customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(
							voucher_id, comp_id, voucher_supplier_id,
							vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
				}
				// // item stock
				if (msg.equals("") && comp_module_inventory.equals("1")
						// && config_inventory_current_stock.equals("1")
						&& !vouchertype_id.equals("21")) {
					// SOP("vouchertrans_location_id==" + vouchertrans_location_id);
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
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

	protected void DeleteFields(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CheckPageperm(null);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher"
						+ " SET voucher_active = 0"
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql==df up="+StrSql);
				stmttx.addBatch(StrSql);
				// UPDATE "+compdb(comp_id)+"the customer current balance
				if (voucherclass_acc.equals("1")) {
					// test111
					CalcCurrentBalanceThread calccurrentbalancethread = new CalcCurrentBalanceThread(
							voucher_id, comp_id, voucher_supplier_id,
							vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// -------------------
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// Delete all the cart items for the current session
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1" + " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + session_id
						+ " AND cart_vouchertype_id=" + vouchertype_id;
				stmttx.addBatch(StrSql);

				// Delete all the items for the current Voucher
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Finally Delete the Voucher
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";

				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

				// item stock
				if (msg.equals("") && comp_module_inventory.equals("1")
						&& !vouchertype_id.equals("115")) {
					// test111
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("0", vouchertrans_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
					// ----------------
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

	// Removed old customer current balance 19May2015

	void CheckPageperm(HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_id FROM  " + compdb(comp_id)
					+ "axela_acc_voucher" + " INNER JOIN  " + compdb(comp_id)
					+ "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE voucher_id = " + voucher_id + BranchAccess + "";
			if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				response.sendRedirect("../portal/error.jsp?msg=Access denied!");
				return;
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT voucherclass_file, voucherclass_acc, vouchertype_name, vouchertype_affects_inventory, vouchertype_roundoff, vouchertype_roundoff_ledger_cr, vouchertype_roundoff_ledger_dr,"
					+ " vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms,"
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
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_tempo_no,"
					+ " vouchertype_driver_no, vouchertype_cashdiscount, vouchertype_turnoverdisc,"
					+ " COALESCE(emp.emp_invoice_priceupdate,'0') AS emp_invoice_priceupdate,"
					+ " COALESCE(emp.emp_invoice_discountupdate,'0') AS emp_invoice_discountupdate"
					+ " FROM "
					+ compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN " + maindb() + "acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=PopulateConfigDetails==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucherclass_file = crs.getString("voucherclass_file");
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				voucher_authorize = vouchertype_defaultauthorize;
				vouchertype_name = unescapehtml(crs.getString("vouchertype_name"));
				vouchertype_affects_inventory = crs.getString("vouchertype_affects_inventory");
				vouchertype_roundoff = crs.getString("vouchertype_roundoff");
				vouchertype_roundoff_ledger_cr = crs.getString("vouchertype_roundoff_ledger_cr");
				vouchertype_roundoff_ledger_dr = crs.getString("vouchertype_roundoff_ledger_dr");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	public String PopulateLocation(String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name" + " FROM "
					+ compdb(comp_id) + "axela_inventory_location";
			if (vouchertype_id.equals("2")) {
				StrSql += " INNER JOIN " + compdb(comp_id)
						+ "axela_branch ON branch_id =location_branch_id"
						+ " WHERE 1=1 " + BranchAccess;
			} else {
				StrSql += " WHERE 1=1 " + " AND location_branch_id = "
						+ branch_id;
			}
			StrSql += " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"),
						vouchertrans_location_id));
				Str.append(">").append(crs.getString("location_name"))
						.append("</option>\n");
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

	public String PopulateFromLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name" + " FROM  "
					+ compdb(comp_id) + "axela_inventory_location"
					+ " INNER JOIN " + compdb(comp_id)
					+ "axela_branch ON branch_id =location_branch_id"
					+ " WHERE 1=1 " + BranchAccess + " ORDER BY location_name";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"),
						vouchertrans_from_location_id));
				Str.append(">").append(crs.getString("location_name"))
						.append("</option>\n");
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

	public String PopulateItemCatPop() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cat_id ,cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
					+ " GROUP BY cat_id"
					+ " ORDER BY cat_rank, cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Keyword</option>\n");
			Str.append("<option value=-1").append(StrSelectdrop("-1", item_cat_id)).append(">").append("Code");
			Str.append("</option>\n");
			Str.append("<option value=-2").append(StrSelectdrop("-2", item_cat_id)).append(">").append("Name");
			Str.append("</option>\n");

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), item_cat_id));
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

	public String PopulateBranchClass(String customer_branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					+ " AND rateclass_type = 2"
					+ " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";
			// SOP("StrSql=po=branchclass=" + StrSql);
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name=\"dr_contact_id\" class=\"form-control\" id=\"dr_contact_id\" >");
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = " + CNumeric(customer_id) + ""
					+ " GROUP BY contact_id" + " order by contact_fname";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				Str.append(StrSelectdrop(crs.getString("contact_id"), voucher_contact_id)).append(">");
				Str.append(crs.getString("contact_name"));
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulateLedgers() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, CONCAT(customer_name, ' (',customer_code, ')') AS customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE 1=1"
					+ " AND customer_id IN (3259, 16952, 16946, 3260, 7992)"
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

	public String PopulateIncreaseDecrease(String incdec_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value= 1").append(StrSelectdrop("1", incdec_id)).append(">").append("Increase").append("</option>\n");
		Str.append("<option value= 2").append(StrSelectdrop("2", incdec_id)).append(">").append("Decrease").append("</option>\n");
		return Str.toString();
	}

	public void BeforeAddRoutine(HttpServletRequest request) {
		try {

			// ---------------Query to delete cart times added other then today
			// dont put session_id
			// filter...--------------------------------------------------
			StrSql = "DELETE FROM " + compdb(comp_id) + " axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_time < " + ConvertShortDateToStr(AddDayMonthYear(strToLongDate(ToLongDate(kknow())), -1, 0, 0, 0));
			// SOP("StrSql=de==" + StrSql);
			updateQuery(StrSql);

			// --------- 1. Code to get branch_id and
			// vouchertrans_location_id---------
			if (emp_branch_id.equals("0")) {
				branch_id = CNumeric(GetSession("voucher_branch_id", request));
				// /location link removed for branch
				vouchertrans_location_id = CNumeric(GetSession("vouchertrans_location_id", request));
				//
				if (branch_id.equals("0")) {
					branch_id = getActiveBranchID(request, emp_id);
					SetSession("voucher_branch_id", branch_id, request);
					if (!branch_id.equals("0")) {
						// /location link removed for branch
						if (comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")) {
							StrSql = "SELECT location_id"
									+ " FROM  " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = " + branch_id
									+ "" + " LIMIT 1";
							// SOP("StrSql==1==" + StrSql);
							vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
							SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
						}
					}
				}

			} else {
				branch_id = emp_branch_id;
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}
			}

			if (!customer_branch_id.equals("0")) {
				branch_id = customer_branch_id;
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}
			}

			// For getting atleast on branch_id
			if (branch_id.equals("0")) {
				StrSql = "SELECT branch_id"
						+ " FROM  " + compdb(comp_id) + "axela_branch"
						+ " LIMIT 1";
				branch_id = CNumeric(ExecuteQuery(StrSql));
				SetSession("voucher_branch_id", branch_id, request);
				// SOP("branch_id===branch_id==="+branch_id);
				if (comp_module_inventory.equals("1")
						&& config_inventory_current_stock.equals("1")) {
					StrSql = "SELECT location_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_branch_id = " + branch_id + ""
							+ " LIMIT 1";
					vouchertrans_location_id = CNumeric(ExecuteQuery(StrSql));
					SetSession("vouchertrans_location_id", vouchertrans_location_id, request);
				}
			}

			branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id = " + branch_id);
			// --------------End Of Code
			// 1.-----------------------------------------
			if (!voucher_po_id.equals("0") || !voucher_git_id.equals("0") || !voucher_grn_id.equals("0") || !voucher_purchaseinvoice_id.equals("0")) {
				parentvoucher = "yes";
				if (!voucher_po_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_po_id;
					if (!refresh.equals("no")) {
						// CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				} else if (!voucher_git_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_git_id;
					if (!refresh.equals("no")) {
						// CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				} else if (!voucher_grn_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_grn_id;
					if (!refresh.equals("no")) {
						// CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				}
				else if (!voucher_purchaseinvoice_id.equals("0")) {
					copyvouchertrans_voucher_id = voucher_purchaseinvoice_id;
					if (!refresh.equals("no")) {
						// CopyVoucherTransToCartCheck(request, emp_id, session_id, copyvouchertrans_voucher_id, vouchertype_id, comp_id);
					}

				}
				StrSql = "SELECT voucher_date,"
						+ " voucher_po_id, voucher_git_id, voucher_grn_id,"
						+ " voucher_location_id, location_name, voucher_rateclass_id,"
						+ " voucher_customer_id, voucher_contact_id,"
						+ " voucher_billing_add, voucher_consignee_add,"
						+ " voucher_gatepass, voucher_lrno, voucher_tempo_no, voucher_driver_no,"
						+ " voucher_cashdiscount, voucher_turnoverdisc"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location on location_id = voucher_location_id"
						+ " WHERE voucher_id =" + copyvouchertrans_voucher_id;
				// SOP("StrSql====" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					voucher_date = crs.getString("voucher_date");
					if (voucher_po_id.equals("0")) {
						voucher_po_id = crs.getString("voucher_po_id");
					}
					if (voucher_git_id.equals("0")) {
						voucher_git_id = crs.getString("voucher_git_id");
					}
					voucherdate = strToShortDate(voucher_date);
					vouchertrans_location_id = crs.getString("voucher_location_id");
					location_name = crs.getString("location_name");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");
					voucher_supplier_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucher_billing_add = crs.getString("voucher_billing_add");
					voucher_consignee_add = crs.getString("voucher_consignee_add");
					voucher_gatepass = crs.getString("voucher_gatepass");
					voucher_lrno = crs.getString("voucher_lrno");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_cashdiscount = crs.getString("voucher_cashdiscount");
					voucher_turnoverdisc = crs.getString("voucher_turnoverdisc");
				}

				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String populateItems(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		Double grand_total = 0.00;
		String round_off_amount = "";
		String voucher_id = "0";
		if (!this.voucher_id.equals("0")) {
			voucher_id = this.voucher_id;
		} else {
			if (!voucher_po_id.equals("0") && voucher_grn_id.equals("0")) {
				voucher_id = voucher_po_id;
			} else if (!voucher_grn_id.equals("0")) {
				voucher_id = voucher_grn_id;
			}
			else if (!voucher_purchaseinvoice_id.equals("0")) {
				voucher_id = voucher_purchaseinvoice_id;
			}
		}

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
				if ((update.equals("yes") && !updateB.equals("yes")) || !voucher_id.equals("0")) {

					StrSql = "SELECT vouchertrans.vouchertrans_id,"
							+ " vouchertrans_rateclass_id,"
							+ " vouchertrans_billtype_id,"
							+ " vouchertrans.vouchertrans_rowcount,"
							+ " vouchertrans.vouchertrans_item_desc,"
							+ " vouchertrans.vouchertrans_voucher_id AS vouchertrans_voucher_id, item_id,"
							+ " COALESCE(IF(uom_name!='',uom_name, 'Each'),'') AS uom_name,"
							+ " COALESCE(vouchertrans.vouchertrans_customer_id,'0') AS item_sales_ledger_id,"
							+ "	IF(item_code!='',CONCAT(item_name,' (',item_code,')'),item_name) AS item_name,"
							+ " item_code, vouchertrans.vouchertrans_qty, vouchertrans.vouchertrans_alt_qty,"
							+ " vouchertrans.vouchertrans_alt_uom_id, vouchertrans.vouchertrans_truckspace,"
							+ " vouchertrans.vouchertrans_price,"
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
				}

				// SOP("StrSql==populateItems==" + StrSqlBreaker(StrSql));

				if (!StrSql.equals("")) {
					CachedRowSet crs = null;
					crs = processQuery(StrSql, 0);

					while (crs.next()) {
						// check box
						Str.append("<tr>");
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
						Str.append("<input name='txt_item_qty_" + row_no + "' type='text' class='form-control text-right' id='txt_item_qty_" + row_no + "'");
						Str.append(" value='" + crs.getString("vouchertrans_qty") + "' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_qty_" + row_no + "','Qty');CalItemTotal(" + row_no
								+ ");\" />\n");
						Str.append("<input type='hidden' id=txt_item_id_" + row_no + " name=txt_item_id_" + row_no + " ").append("value='").append(PadQuotes(crs.getString("item_id"))).append("'/>\n");
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

						Str.append("<input id=txt_item_price_" + row_no + " name=txt_item_price_" + row_no + " type='text' class='form-control text-right' ")
								.append("value='")
								.append(df.format(crs.getDouble("vouchertrans_price")))
								.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');\" onChange=\"CheckBasePrice(" + row_no
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

						Str.append("<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no + "' type='text' class='form-control text-right' ")
								.append("value='").append(df.format(crs.getDouble("discount") / crs.getDouble("vouchertrans_qty"))).append("' readonly='readonly'"
										+ " onFocus=\"DiscountModel('../accounting/item-discount.jsp?row_no=" + row_no + "');\"/>");

						Str.append("<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no + "' type='text' ");
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
									.append(df.format(crs.getDouble("tax1_amount"))).append("'/>\n");
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
									.append(df.format(crs.getDouble("tax2_amount"))).append("'/>\n");
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
									.append(df.format(crs.getDouble("tax3_amount"))).append("'/>\n");
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
					Str.append("<input name='txt_item_qty_" + row_no + "' type='text' class='form-control text-right' id='txt_item_qty_" + row_no + "' ");
					Str.append("value='" + request.getParameter("txt_item_qty_" + row_no) + "' size='10' maxlength='10' onKeyUp=\"toNumber('txt_item_qty_" + row_no + "','Qty');CalItemTotal(" + row_no
							+ ");\" />\n");
					Str.append("<input type='hidden' id=txt_item_id_" + row_no + " name=txt_item_id_" + row_no + " ").append("value='")
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

					Str.append("<input id=txt_item_price_" + row_no + " name=txt_item_price_" + row_no + " type='text' class='form-control text-right' ")
							.append("value='")
							.append(request.getParameter("txt_item_price_" + row_no))
							.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');\" onChange=\"CheckBasePrice(" + row_no
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

					Str.append("<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no + "' type='text' class='form-control text-right' ")
							.append("value='").append(request.getParameter("txt_item_price_disc_" + row_no)).append("' readonly='readonly'"
									+ " onFocus=\"DiscountModel('../accounting/item-discount.jsp?row_no=" + row_no + "');\"/>");

					Str.append("<input id='txt_item_price_disc_percent_add_" + row_no + "' name='txt_item_price_disc_percent_add_" + row_no + "' type='text' ");
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
}
