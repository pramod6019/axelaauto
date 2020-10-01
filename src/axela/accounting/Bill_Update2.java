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

public class Bill_Update2 extends Connect {

	public String emp_id = "0", emp_branch_id = "0", bill_amount = "0";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String voucher_rateclass_id = "0";
	public String status = "";
	public String addledger = "";
	public String StrSql = "";
	public String msg = "";
	public String msgChk = "";
	public String comp_id = "0";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";

	public String rateclass_id = "0";
	public String entry_date = "";
	public String modified_date = "";
	DecimalFormat df = new DecimalFormat("#.####");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String checkPerm = "";
	public String emp_role_id = "0";

	public String voucher_id = "0";
	public String voucherclass_id = "7", voucherclass_file = "", voucherclass_acc = "0";
	public String vouchertype_id = "0";
	public String voucher_customer_id = "0";
	public String voucher_location_id = "";
	public String voucher_date = "";
	public String voucherdate = "";
	public String voucher_enquiry_id = "0", voucher_lead_id = "0";
	public int voucher_colspan = 1;
	public String voucher_emp_id = "0";
	public String voucher_ref_no = "";
	public String voucher_active = "0";
	public String voucher_notes = "";
	public String voucher_grandtotal = "0.00";
	public String voucher_prev_grandtotal = "";
	public String voucher_no = "";
	public String voucher_consignee_add = "", voucher_billing_add = "";
	public String voucherno = "";
	public String voucher_amount = "";
	// public String vouchertrans_location_id = "0";
	public String checkbranch = "0", check_voucher_no = "";
	public String voucher_entry_id = "0";
	public String voucher_entry_by = "";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_by = "";
	public String voucher_modified_date = "";
	public String voucher_contact_id = "0";
	public String emp_bill_priceupdate = "0";
	public String emp_bill_discountupdate = "0";
	/* End Of Voucher Variables */
	public String vouchertrans_voucher_id = "";
	public String vouchertrans_ledger_id = "0";
	public String ledger_add = "";
	public String vouchertrans_amount = "", voucher_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "";
	public String vouchertrans_paymode_id = "";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "";
	public String vouchertrans_cheque_bank = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertrans_option_id = "0";
	public String vouchertrans_option_group = "";
	public String vouchertrans_item_serial = "";
	public String vouchertrans_item_batch_id = "0";
	public String vouchertrans_price = "";
	public String vouchertrans_qty = "";
	public String vouchertype_defaultauthorize = "0";
	public String vouchertype_authorize = "0";

	// config variables ///
	public String vouchertype_mobile = "0";
	public String vouchertype_email = "0";
	public String vouchertype_dnd = "0", vouchertype_dob = "0";
	public String vouchertype_billing_add = "0";
	public String vouchertype_consignee_add = "0";
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

	public String voucher_so_id = "0";
	public String voucher_invoice_id = "0";
	public String voucher_delnote_id = "0";

	public String comp_email_enable;
	public String comp_sms_enable;
	public String config_inventory_current_stock = "0", vouchertype_affects_inventory = "0";
	public String vouchertype_roundoff = "0";
	public String vouchertype_roundoff_ledger_cr = "0";
	public String vouchertype_roundoff_ledger_dr = "0";
	public String vouchertype_gatepass = "0";
	public String vouchertype_lrno = "0";
	public String vouchertype_driver_no = "0";
	public String vouchertype_tempo_no = "0";
	public String vouchertype_cashdiscount = "0";
	public String vouchertype_turnoverdisc = "0";
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String config_bill_reduce_current_stock = "0";
	public String config_customer_address = "0";
	public String comp_module_inventory = "0";
	public String emp_invoice_priceUpdate = "0";
	public String emp_invoice_discountUpdate = "0";
	/* End of Config Variables */
	// contact variables
	public String contact_id = "0";
	public String contact_title_id = "0", voucher_branch_id = "0";
	public String link_contact_name = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_curr_bal = "0";
	public String customer_credit_limit = "0";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String customer_phone1 = "";
	public String customer_address = "";
	public String link_customer_name = "";

	public String branch_city_id = "0";
	public String branch_pin = "";
	public int i = 0;
	public String readOnly = "";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";

	public String formatdigit_id = "0";
	public String customer_type = "0";
	public String customer_typearr[] = null;
	public String[] paymode_amount = null;
	public String[] paymode_chqno = null;
	public String[] paymode_chqdate = null;
	public String[] paymode_chqbank = null;
	public String[] paymode_chqbranch = null;
	public String item_total = "0";
	public Double paymode_total = 0.00;
	public String[] payment_typearr_temp = null;
	public String[] payment_typearr = null;
	public String[] paymode_typearr = null;
	public String AddRowGroupStr = "", updatecount = "";
	public boolean flag = false;
	public String vouchertype_name = "", customer_add = "", customer_branch_id = "0";
	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public int paymode_count = 0;
	public String popcount = "";

	public String bill_contact_id = "0";
	public String gst_type = "";

	public String bill_customer_id = "0";
	public String bill_customer_name = "", bill_contact_name = "";
	public String paymode_id = "0";
	public String item_serial_id = "0", item_itemgroup_id = "0", session_id = "";
	public String item_model_id = "0";
	public Ledger_Check ledgercheck = new Ledger_Check();

	public int row_no = 1;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_bill_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request));
				config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				customer_add = PadQuotes(request.getParameter("customer_add"));
				PopulateConfigDetails();
				if (customer_add.equals("yes")) {
					customer_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + branch_id);

					voucher_customer_id = CNumeric(PadQuotes(request.getParameter("voucher_customer_id")));
					voucher_contact_id = CNumeric(PadQuotes(request.getParameter("voucher_contact_id")));
					voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("voucher_rateclass_id")));
					// contact_dnd = CNumeric(PadQuotes(request
					// .getParameter("contact_dnd")));
				}

				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}

				empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);
				// SOP("empEditperm=====" + empEditperm);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));

				voucher_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_grand_total")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
					paymode_count = 1;
					if (emp_branch_id.equals("0")) {
						branch_id = CNumeric(GetSession("voucher_branch_id", request));
						rateclass_id = CNumeric(GetSession("voucher_rateclass_id", request));
						voucher_location_id = CNumeric(GetSession("voucher_location_id", request));
						if (branch_id.equals("0")) {
							branch_id = getActiveBranchID(request, emp_id);
							SetSession("voucher_branch_id", branch_id, request);
							if (!branch_id.equals("0")) {
								if (comp_module_inventory.equals("1")
										&& config_inventory_current_stock.equals("1")) {
									StrSql = "SELECT location_id FROM " + compdb(comp_id) + "axela_inventory_location"
											+ " WHERE location_branch_id = "
											+ branch_id + "" + " LIMIT 1";
									// SOP("location Query====" + StrSql);
									voucher_location_id = CNumeric(ExecuteQuery(StrSql));
									SetSession("voucher_location_id", voucher_location_id, request);
								}
							}
						}
					} else {
						branch_id = emp_branch_id;
						if (comp_module_inventory.equals("1")
								&& config_inventory_current_stock.equals("1")) {
							StrSql = "SELECT location_id FROM " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = "
									+ branch_id
									+ "" + " LIMIT 1";
							voucher_location_id = CNumeric(ExecuteQuery(StrSql));
							SetSession("voucher_location_id", voucher_location_id, request);
						}
						StrSql = "SELECT branch_rateclass_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_id = " + branch_id + ""
								+ " LIMIT 1";
						rateclass_id = CNumeric(ExecuteQuery(StrSql));
						SetSession("voucher_rateclass_id", rateclass_id, request);
					}
					if (!customer_branch_id.equals("0")) {
						branch_id = customer_branch_id;
						if (comp_module_inventory.equals("1")
								&& config_inventory_current_stock.equals("1")) {
							StrSql = "SELECT location_id FROM " + compdb(comp_id) + "axela_inventory_location"
									+ " WHERE location_branch_id = "
									+ branch_id
									+ " LIMIT 1";
							voucher_location_id = CNumeric(ExecuteQuery(StrSql));
							SetSession("voucher_location_id", voucher_location_id, request);
						}

						StrSql = "SELECT branch_rateclass_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_id = " + branch_id + ""
								+ " LIMIT 1";
						rateclass_id = CNumeric(ExecuteQuery(StrSql));
						SetSession("voucher_rateclass_id", rateclass_id, request);
					}

					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = "
							+ branch_id);

					if (!addB.equals("yes")) {
						voucherdate = DateToShortDate(kknow());
						voucher_date = ConvertShortDateToStr(voucherdate);
						contact_title_id = "0";
						voucher_emp_id = emp_id;
						voucher_active = "1";

					} else {
						if (ReturnPerm(comp_id, "emp_acc_bill_add", request).equals("1")) {
							voucherdate = DateToShortDate(kknow());
							voucher_date = ConvertShortDateToStr(voucherdate);
							GetValues(request, response);
							if (!branch_id.equals("0")) {
							}
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucher_id="
										+ voucher_id
										+ "&voucherclass_id="
										+ voucherclass_id
										+ "&vouchertype_id="
										+ vouchertype_id
										+ "&msg=Bill added successfully!"
										+ msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Bill")) {
						PopulateFields(request, response);
						// CopyVoucherTransToCart(request, emp_id, session_id, voucher_id, vouchertype_id, status);
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Bill")) {
						if (ReturnPerm(comp_id, "emp_acc_bill_edit", request).equals("1")) {
							GetValues(request, response);
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucher_id="
										+ voucher_id
										+ "&voucherclass_id=" + voucherclass_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=Bill updated successfully!"
										+ msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Bill")) {
						if (ReturnPerm(comp_id, "emp_acc_bill_delete", request).equals("1")) {
							response.sendRedirect(AccessDenied());
							// GetValues(request, response);
							// DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?msg=Bill deleted successfully!"));
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
		voucher_location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
		// SOP("voucher_location_id=======" + voucher_location_id);
		voucher_customer_id = CNumeric(PadQuotes(request.getParameter("accountingcustomer")));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		voucher_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_rateclass_id")));

		gst_type = PadQuotes(request.getParameter("txt_gst_type"));
		voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_prev_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_voucher_prev_grandtotal")));
		voucher_billing_add = PadQuotes(request.getParameter("txt_voucher_billing_add"));
		voucher_consignee_add = PadQuotes(request.getParameter("txt_voucher_consignee_add"));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_active")));
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}

		// PopulateConfigDetails();
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));

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
		voucher_entry_by = PadQuotes(request.getParameter("entry_by"));
		voucher_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
		rateclass_id = CNumeric(PadQuotes(request.getParameter("txt_rateclass_id")));
		voucher_lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));

		if (update.equals("yes")) {
			checkbranch = PadQuotes(request.getParameter("hid_check_branch"));
			check_voucher_no = PadQuotes(request.getParameter("hid_check_voucherno"));
		}
		paymode_count = Integer.parseInt(PadQuotes(request.getParameter("txt_paymode_count")));
		payment_typearr_temp = new String[paymode_count];
		payment_typearr = new String[paymode_count];
		paymode_typearr = new String[paymode_count];
		paymode_amount = new String[paymode_count];
		paymode_chqno = new String[paymode_count];
		paymode_chqdate = new String[paymode_count];
		paymode_chqbank = new String[paymode_count];
		paymode_chqbranch = new String[paymode_count];

		for (int i = 0; i < paymode_count; i++) {
			payment_typearr_temp[i] = PadQuotes(request.getParameter("dr_payment" + (i + 1)));
			paymode_typearr[i] = CNumeric(PadQuotes(request.getParameter("dr_paymode" + (i + 1))));
			paymode_amount[i] = CNumeric(PadQuotes(request.getParameter("txt_bill_amt" + (i + 1))));
			paymode_chqno[i] = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + (i + 1)));
			paymode_chqdate[i] = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date" + (i + 1)));
			paymode_chqbank[i] = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + (i + 1)));
			paymode_chqbranch[i] = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch" + (i + 1)));
			paymode_total += Double.parseDouble(paymode_amount[i]);
		}
		for (int i = 0; i < payment_typearr_temp.length; i++) {
			payment_typearr[i] = payment_typearr_temp[i].split("-")[1];
		}
		item_serial_id = CNumeric(PadQuotes(request.getParameter("drop_item_serial_id")));
		item_itemgroup_id = CNumeric(PadQuotes(request.getParameter("drop_item_itemgroup_id")));

	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (voucher_location_id.equals("0")) {
			msg += "<br>Select Location!";
		}
		if (voucherdate.equals("") && !vouchertype_id.equals("7")) {
			msg += "<br>Enter Bill Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				// SOP("voucher_date====" + voucher_date);
				if (Long.parseLong(voucher_date) > Long
						.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Bill Date must be less than or equal to Current Date!";
				}
			} else {
				if (!vouchertype_id.equals("7")) {
					msg += "<br>Enter valid Bill Date!";
				}
			}
		}

		if (voucher_customer_id.equals("0")) {
			msg += "<br>Select Customer!";
		}

		for (int i = 0; i < paymode_count; i++) {
			if (payment_typearr_temp[i].split("-")[1].equals("1") && !paymode_typearr[i].equals("1")) {
				msg += "<br>Payment Mode and Payment By are not Compatible For " + (i + 1) + " Column!";
			}
			if (!payment_typearr_temp[i].split("-")[1].equals("1") && paymode_typearr[i].equals("1")) {
				msg += "<br>Payment Mode and Payment By are not Compatible!";
			}
		}
		if (voucher_contact_id.equals("0")) {
			msg += "<br>Select Contact!";
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}
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
				// SOP("StrSql==Checkform==" + StrSql);
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Voucher No. found!";
				}
			}
		} else {
			if (status.equals("Add") && vouchertype_defaultauthorize.equals("1")) {
				voucher_no = getVoucherNo(branch_id, vouchertype_id, "0", "0");
			}
		}

		if (Double.parseDouble(customer_credit_limit) > 0) {
			if (((Double.parseDouble(voucher_grandtotal) - Double
					.parseDouble(voucher_prev_grandtotal)) + Double
					.parseDouble(customer_curr_bal)) > Double
					.parseDouble(customer_credit_limit)) {
				msg += "<br>" + "Customer" + " Credit limit is exceeding!";
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
					msg += "<br>Enter Bill Reference No.!";
				} else {
					if (voucher_ref_no.length() < 2) {
						msg += "<br>Bill Reference No. should be atleast Two Digits! ";
					}

					if (!branch_id.equals("0")) {
						StrSql = "SELECT voucher_ref_no FROM " + compdb(comp_id) + "axela_acc_voucher"
								+ " WHERE voucher_branch_id = "
								+ branch_id
								+ " "
								+ " AND voucher_ref_no = '"
								+ voucher_ref_no
								+ "'"
								+ " AND voucher_id != "
								+ voucher_id;
						if (!ExecuteQuery(StrSql).equals("")) {
							msg += "<br>Similar Bill Reference No. found!";
						}
					}
				}
			}
		}
		for (int i = 0; i < paymode_count; i++) {
			if (!payment_typearr[i].equals("0")) {
				if (payment_typearr[i].equals("1")
						&& paymode_typearr[i].equals("1")) {
					if (paymode_amount.equals("0")) {
						msg += "<br>Enter Amount for payment " + i;
					}
				} else if (payment_typearr[i].equals("2")
						&& paymode_typearr[i].equals("2")) {
					if (paymode_amount[i].equals("0")) {
						msg += "<br>Enter Amount for payment " + i;
					}
					if (paymode_chqno[i].equals("")) {
						msg += "<br>Enter Cheque NO. for payment " + i;
					}
					if (paymode_chqdate[i].equals("")) {
						msg += "<br>Enter Cheque Date for payment " + i;
					} else {
						if (!isValidDateFormatShort(paymode_chqdate[i])) {
							msg += "<br>Enter valid Cheque Date for payment "
									+ i;
						}
					}
					if (paymode_chqbank[i].equals("")) {
						msg += "<br>Enter Cheque Bank for payment " + i;
					}
					if (paymode_chqbranch[i].equals("")) {
						msg += "<br>Enter Cheque Branch for payment " + i;
					}
				} else if (payment_typearr[i].equals("2")
						&& paymode_typearr[i].equals("3")) {
					if (paymode_amount[i].equals("0")) {
						msg += "<br>Enter Amount for payment " + i;
					}
					if (paymode_chqno[i].equals("")) {
						msg += "<br>Enter Card NO. for payment " + i;
					}
					if (paymode_chqbank[i].equals("")) {
						msg += "<br>Enter Bank for payment " + i;
					}
				} else if (payment_typearr[i].equals("2")
						&& paymode_typearr[i].equals("5")) {
					if (paymode_amount[i].equals("0")) {
						msg += "<br>Enter Amount for payment " + i;
					}
					if (paymode_chqno[i].equals("")) {
						msg += "<br>Enter Transaction NO. for payment " + i;
					}
				} else if (payment_typearr[i].equals("2")
						&& paymode_typearr[i].equals("6")) {
					if (paymode_amount[i].equals("0")) {
						msg += "<br>Enter Amount for payment " + i;
					}
					if (paymode_chqbank[i].equals("")) {
						msg += "<br>Enter Bank for payment " + i;
					}
				}
			}
		}
		if ((Double.parseDouble(voucher_grandtotal) < paymode_total)
				|| (Double.parseDouble(voucher_grandtotal) > paymode_total)) {
			msg += "<br>Payment Amount is not matching with Bill Amount!";
		}
	}
	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher"
						+ " (voucher_vouchertype_id,"
						+ " voucher_branch_id,"
						+ " voucher_no,"
						+ " voucher_location_id,"
						+ " voucher_rateclass_id,";
				if (vouchertype_id.equals("7")) {
					StrSql += " voucher_date,";
				}

				StrSql += " voucher_amount,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_lead_id,"
						+ " voucher_enquiry_id,"
						+ " voucher_emp_id,"
						+ " voucher_billing_add,"
						+ " voucher_consignee_add,"
						+ " voucher_ref_no,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_notes,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES" + " (" + vouchertype_id + ","
						+ " " + branch_id + ","
						+ " " + CNumeric(voucher_no) + ","
						+ "" + voucher_location_id + ","
						+ "" + voucher_rateclass_id + ",";
				if (vouchertype_id.equals("7")) {
					StrSql += " '" + voucher_date + "',";
				}
				StrSql += " " + Double.parseDouble(voucher_grandtotal) + ","
						+ " " + voucher_customer_id + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_lead_id + ","
						+ " " + voucher_enquiry_id + ","
						+ " " + voucher_emp_id + ","
						+ " '" + voucher_billing_add + "',"
						+ " '" + voucher_consignee_add + "',"
						+ " '" + voucher_ref_no + "',"
						+ " '" + vouchertype_terms + "' ,"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " '" + voucher_notes + "',"
						+ " " + emp_id + ","
						+ ToLongDate(kknow()) + ")";

				// SOP("StrSQl=voucher=" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					voucher_id = rs1.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs1.close();

				AddItemFields(request);

				AddPayment(stmttx, request);

				stmttx.executeBatch();

				conntx.commit();

				// //customer current balance
				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// stock
				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& vouchertype_affects_inventory.equals("1")) {
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
							"0", voucher_location_id, comp_id, vouchertype_id, "");
					Thread thread = new Thread(calccurrentstockthread);
					thread.start();
				}

				PopulateContactDetails();

				// if (contact_dnd.equals("0")) {
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
					// }

					// sms sending
					if (config_sms_enable.equals("1")
							&& vouchertype_sms_enable.equals("1")
							&& vouchertype_sms_auto.equals("1")
							&& comp_sms_enable.equals("1")) {
						Voucher_SMS Voucher_SMS_obj = new Voucher_SMS();
						Voucher_SMS_obj.voucher_id = voucher_id;

						// // SOP("before contact_mobile");
						// contact_mobile =
						// ExecuteQuery("SELECT contact_mobile1"
						// + " FROM "+compdb(comp_id)+"axela_customer_contact"
						// + " WHERE contact_id = " + voucher_contact_id);
						// // SOP("aftre contact_mobile");
						// Voucher_SMS_obj.SendSMS(contact_mobile1, comp_id);
						// Voucher_SMS_obj = null;
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
		} else {
			AddRowGroupStr = HoldPayments(request);
		}
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_no, voucher_customer_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " voucher_branch_id,voucher_rateclass_id, voucher_date, voucher_amount, voucher_contact_id,"
					+ " voucher_emp_id, vouchertrans_paymode_id, vouchertrans_cheque_no, vouchertrans_cheque_branch,"
					+ " vouchertrans_cheque_date, vouchertrans_cheque_bank, voucher_payment_date,"
					+ " voucher_billing_add, voucher_consignee_add, voucher_ref_no, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, vouchertrans_location_id, voucher_enquiry_id,"
					+ " voucher_authorize_id,voucher_authorize_time, voucher_authorize,"
					+ " (SELECT COUNT(count.vouchertrans_reconciliation)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans count"
					+ " WHERE count.vouchertrans_voucher_id = voucher_id"
					+ " AND count.vouchertrans_reconciliation = 1) as popcount,"
					+ " voucher_notes"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
					+ " WHERE voucher_id = "
					+ voucher_id
					+ BranchAccess
					+ ExeAccess
					+ ""
					+ " AND vouchertrans_dc = '1'"
					+ " GROUP BY voucher_id";
			// SOP("StrSql=popu==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					branch_id = crs.getString("voucher_branch_id");
					checkbranch = branch_id;
					branch_name = crs.getString("branch_name");
					// rateclass_id = crs.getString("rateclass_id");
					voucher_rateclass_id = crs.getString("voucher_rateclass_id");

					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucher_no = crs.getString("voucher_no");
					check_voucher_no = voucher_no;

					voucher_date = crs.getString("voucher_date");
					voucherdate = strToShortDate(voucher_date);
					voucher_billing_add = crs.getString("voucher_billing_add");
					voucher_consignee_add = crs.getString("voucher_consignee_add");
					voucher_grandtotal = crs.getString("voucher_amount");
					voucher_location_id = crs.getString("vouchertrans_location_id");
					// location_name = crs.getString("location_name");
					voucher_enquiry_id = crs.getString("voucher_enquiry_id");
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_notes = crs.getString("voucher_notes");
					voucher_entry_id = crs.getString("voucher_entry_id");
					voucher_entry_id = crs.getString("voucher_entry_id");
					popcount = crs.getString("popcount");
					paymode_count = Integer.parseInt(popcount);
					if (!voucher_entry_id.equals("0")) {
						voucher_entry_by = Exename(comp_id, Integer.parseInt(voucher_entry_id));
					}
					entry_date = strToLongDate(crs.getString("voucher_entry_date"));
					voucher_modified_id = crs.getString("voucher_modified_id");
					if (!voucher_modified_id.equals("0")) {
						voucher_modified_by = Exename(comp_id, Integer.parseInt(voucher_modified_id));
					}
					modified_date = strToLongDate(crs.getString("voucher_modified_date"));
					// rateclass_id = rs.getString("customer_rateclass_id");
					gst_type = PadQuotes(new Ledger_Check().CompareState(comp_id, voucher_customer_id, branch_id));
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Bill!");
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
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + " axela_acc_voucher" + " SET"
						+ " voucher_vouchertype_id = " + vouchertype_id + ","
						+ " voucher_branch_id = " + branch_id + ","
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_rateclass_id = " + voucher_rateclass_id + ",";
				if (vouchertype_id.equals("7")) {
					StrSql += " voucher_date = " + voucher_date + ",";
				}
				StrSql += " voucher_amount = " + Double.parseDouble(voucher_grandtotal) + ","
						+ " voucher_customer_id = " + voucher_customer_id + ","
						+ " voucher_contact_id = " + voucher_contact_id + ","
						+ " voucher_lead_id = " + voucher_lead_id + ","
						+ " voucher_enquiry_id = " + voucher_enquiry_id + ","
						+ " voucher_emp_id = " + voucher_emp_id + ","
						+ " voucher_billing_add= '" + voucher_billing_add + "',"
						+ " voucher_consignee_add= '" + voucher_consignee_add + "',"
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_notes = '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql===up===" + StrSql);
				stmttx.addBatch(StrSql);

				AddItemFields(request);

				// for mode of payments made
				AddPayment(stmttx, request);

				stmttx.executeBatch();

				conntx.commit();

				// //customer current balance
				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// stock
				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& vouchertype_affects_inventory.equals("1")) {
					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
							"0", voucher_location_id, comp_id, vouchertype_id, "");
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

		} else {
			AddRowGroupStr = HoldPayments(request);
		}
	}
	protected void DeleteFields(HttpServletResponse response) throws Exception {
		CheckPageperm(null);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");
				// //customer current balance
				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				// Delete all the items for the current Invoice
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Finally Delete the Invoice
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

				// item stock
				if (msg.equals("")
						&& comp_module_inventory.equals("1")
						&& vouchertype_affects_inventory.equals("1")) {

					CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(
							"0", voucher_location_id, comp_id, vouchertype_id, "");
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
					&& !PadQuotes(request.getParameter("txt_round_off_amount")).equals("")) {
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
					+ " '0'" + ")";// vouchertrans_dc

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
								.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');CalAmount(" + row_no + ");\" onChange=\"CheckBasePrice(" + row_no
										+ ");CalItemTotal(" + row_no + ");\" ");
						if (!emp_bill_priceupdate.equals("1")) {
							Str.append(" readonly='readonly' ");
						}
						Str.append(" />");

						Str.append("</td>");

						// Discount
						Str.append("<td>");

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
							.append(df.format(Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_price_" + row_no))))))
							.append("' size='20' maxlength='20' onKeyUp=\"toNumber('txt_item_price_" + row_no + "','Price');CalAmount(" + row_no + ");\" onChange=\"CheckBasePrice(" + row_no
									+ ");CalItemTotal(" + row_no + ");\"");

					if (!emp_bill_priceupdate.equals("1")) {
						Str.append(" readonly='readonly' ");
					}

					Str.append(" />");

					Str.append("</td>");

					// Discount
					Str.append("<td>");

					Str.append("<input id='txt_item_price_disc_" + row_no + "' name='txt_item_price_disc_" + row_no + "' type='text' class='form-control text-right' ")
							.append("value='").append(df.format(Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_price_disc_" + row_no)))))).append("' readonly='readonly'"
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
								.append(df.format(Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax1_" + row_no)))))).append("'/>\n");
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
								.append(df.format(Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax2_" + row_no)))))).append("'/>\n");
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
								.append(df.format(Double.parseDouble(CNumeric(PadQuotes(request.getParameter("txt_item_tax3_" + row_no)))))).append("'/>\n");
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
					Str.append("<b><input id=item_total_" + row_no + " name=item_total_" + row_no + " value=").append(df.format(Double.parseDouble(request.getParameter("item_total_" + row_no))))
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

	//
	public void CustomerCurrentBalance(String customer_id) throws SQLException {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + " axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = customer_id"
					+ " SET";
			if (deleteB.equals("Delete Bill")) {
				StrSql += " customer_curr_bal = customer_curr_bal - "
						+ CNumeric(voucher_prev_grandtotal) + "";
			} else {
				StrSql += " customer_curr_bal = customer_curr_bal + "
						+ CNumeric(voucher_grandtotal) + " - "
						+ CNumeric(voucher_prev_grandtotal) + "";
			}
			StrSql += " WHERE voucher_active = 1"
					+ " AND voucher_customer_id = " + customer_id + "";
			stmttx.addBatch(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
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

	protected void PopulateContactDetails() {
		try {
			String StrSearch = "";
			if (!contact_id.equals("0")) {
				StrSearch = " AND contact_id = " + contact_id + "";
			} else if (!voucher_contact_id.equals("0")) {
				StrSearch = " AND contact_id = " + voucher_contact_id + "";
			}

			if (!StrSearch.equals("")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname, contact_lname, customer_curr_bal,"
						+ " contact_email1, contact_mobile1, title_desc, customer_curr_bal, customer_credit_limit"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE 1 = 1" + StrSearch + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					voucher_customer_id = crs.getString("customer_id");
					voucher_contact_id = crs.getString("contact_id");
					customer_credit_limit = new DecimalFormat("0.00").format(crs
							.getDouble("customer_credit_limit"));
					customer_curr_bal = crs.getString("customer_curr_bal");
					link_customer_name = "<a href=../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id")
							+ ">"
							+ crs.getString("customer_name")
							+ " ("
							+ crs.getString("customer_curr_bal") + ")</a>";
					link_contact_name = "<a href=../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id")
							+ ">"
							+ crs.getString("title_desc")
							+ " "
							+ crs.getString("contact_fname")
							+ " "
							+ crs.getString("contact_lname") + "</a>";
				}
				crs.close();
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
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id) + "axela_title"
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

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = " + CNumeric(customer_id)
					+ " GROUP BY contact_id" + " order by contact_id";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			prepmap.clear();
			Str.append("<select name=dr_contact_id class=form-control id=dr_contact_id >");
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				Str.append(Selectdrop(Integer.parseInt(crs.getString("contact_id")), voucher_contact_id)).append(">");
				Str.append(crs.getString("contact_name")).append("</option>\n");
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

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT voucherclass_file, voucherclass_acc,"
					+ " vouchertype_name, vouchertype_affects_inventory,"
					+ " vouchertype_roundoff, vouchertype_roundoff_ledger_cr,"
					+ " vouchertype_roundoff_ledger_dr, vouchertype_ref_no_enable,"
					+ " vouchertype_ref_no_mandatory, vouchertype_defaultauthorize,"
					+ " vouchertype_authorize, vouchertype_terms,"
					+ " COALESCE(vouchertype_mobile, '') AS vouchertype_mobile,"
					+ " COALESCE(vouchertype_email, '') AS vouchertype_email,"
					+ " COALESCE(vouchertype_dob, '') AS vouchertype_dob,"
					+ " COALESCE(vouchertype_dob, '') AS vouchertype_dob,"
					+ " COALESCE(vouchertype_billing_add, '') AS vouchertype_billing_add,"
					+ " COALESCE(vouchertype_dnd, '') AS vouchertype_dnd,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					+ " COALESCE(vouchertype_email_sub, '') AS vouchertype_email_sub,"
					+ " COALESCE(vouchertype_email_format, '') AS vouchertype_email_format,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					+ " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable, vouchertype_billing_add,"
					+ " vouchertype_consignee_add, vouchertype_gatepass, vouchertype_lrno,"
					+ " vouchertype_tempo_no, vouchertype_driver_no,"
					+ " vouchertype_cashdiscount, vouchertype_turnoverdisc,"
					+ " COALESCE(emp.emp_bill_priceupdate,'0') AS emp_bill_priceupdate,"
					+ " COALESCE(emp.emp_bill_discountupdate,'0') AS emp_bill_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + ""
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=PopulateConfigDetails==========" + StrSqlBreaker(StrSql));
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
				vouchertype_mobile = crs.getString("vouchertype_mobile");
				vouchertype_email = crs.getString("vouchertype_email");
				vouchertype_billing_add = crs.getString("vouchertype_billing_add");
				vouchertype_dob = crs.getString("vouchertype_dob");
				vouchertype_dnd = crs.getString("vouchertype_dnd");
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
				emp_bill_priceupdate = crs.getString("emp_bill_priceupdate");
				emp_bill_discountupdate = crs.getString("emp_bill_discountupdate");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulatePayment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			// Str.append("<option value='0-0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), customer_type));
				Str.append(">").append(crs.getString("customer_name")).append("</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String getPayDetails(String voucher_id, String comp_id) {
		String Str = new String();
		String customer_type1 = "0";
		int i = 1;
		StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_type, vouchertrans_paymode_id, vouchertrans_amount, vouchertrans_cheque_no,"
				+ " vouchertrans_cheque_date, vouchertrans_cheque_bank, vouchertrans_cheque_branch"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				+ " WHERE vouchertrans_voucher_id = " + voucher_id
				// + " AND voucher_active = 1"
				+ " AND vouchertrans_reconciliation = 1";

		// SOP("StrSql===hetdetails===" + StrSqlBreaker(StrSql));

		Str = "<div class=\"form-body\">\n"
				+ "<div class=\"form-group\">\n";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				bill_amount = crs.getString("vouchertrans_amount");
				paymode_id = crs.getString("vouchertrans_paymode_id");
				customer_type = crs.getString("customer_type");
				// if (!paymode_id.equals("1")) {
				// customer_type = crs.getString("vouchertrans_customer_id")+"-2";
				// }
				customer_typearr = customer_type.split("-");
				customer_type1 = customer_typearr[1];

				Str += "<div class=\"col-md-12\">"
						+ "<div class=\"form-body\">"
						+ "<div class=\"form-group\">"
						+ "<label class=\"col-md-1 control-label\">Payment<b><font color='#ff0000'>*</font></b>:</label>\n"
						+ "<div class=\"col-md-2\">\n"
						+ "<select name='dr_payment" + i + "' id='dr_payment" + i + "' class='form-control' onChange='Displaypaymode(" + i + ")'>"
						+ PopulatePayment() + "" + "</select>"
						+ "</div>"
						+ "<label class=\"col-md-1 control-label\">By<b><font color='#ff0000'>*</font></b>:</label>"
						+ "<div class=\"col-md-2\">"
						+ "<select name='dr_paymode" + i + "' id='dr_paymode" + i + "' class='form-control' onChange='Displaypaymode(" + i + ")'>"
						+ PopulatePaymode() + "" + "</select>"
						+ "</div>\n"
						+ "<label class=\"col-md-1 control-label\">Amount<b><font color='#ff0000'>*</font></b>:</label>\n"
						+ "<div class=\"col-md-2\">"
						+ "<input name='txt_bill_amt" + i + "' id='txt_bill_amt" + i + "' value='" + bill_amount
						+ "' type='text' class='form-control' onkeyup=toNumber(this) maxlength='10' size='11'/>\n"
						+ "</div>"
						+ "<div class=\"col-md-2\">"
						+ "<img src='../admin-ifx/add.png' align='middle' class='add' type='button' />"
						+ "&nbsp"
						+ "<img src='../admin-ifx/cancel.png' align='middle' class='del' type='button' />"
						+ "</div>"
						+ "<div id='displayrow" + i + "'>";

				if (customer_type1.equals("2") && paymode_id.equals("2")) {
					vouchertrans_cheque_no = crs
							.getString("vouchertrans_cheque_no");
					vouchertrans_cheque_date = strToShortDate(crs
							.getString("vouchertrans_cheque_date"));
					vouchertrans_cheque_bank = crs
							.getString("vouchertrans_cheque_bank");
					vouchertrans_cheque_branch = crs
							.getString("vouchertrans_cheque_branch");
					Str += "<div class='col-md-12'>"
							+ "<div class='col-md-10'style='top:10px'>"
							+ "<label class='control-label col-md-2'>Cheque No<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_no" + i + "' type='text' id='txt_vouchertrans_cheque_no"
							+ i + "' onKeyUp='toInteger('txt_vouchertrans_cheque_no" + i + "')' class='form-control' value='"
							+ vouchertrans_cheque_no + "' size='12' maxlength='6'/>"
							+ "</div>"
							+ "<label class='control-label col-md-2'>"
							+ "Cheque Date<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_date" + i + "' type='text' id='txt_vouchertrans_cheque_date"
							+ i + "' onclick='chequedate(" + i + ")' data-date-format='dd/mm/yyyy' class='form-control date-picker' value='"
							+ vouchertrans_cheque_date + "' size='12' maxlength='6'/>"
							+ " </div>"
							+ "</div>"
							+ "<div class='col-md-10' style='top:10px'>"
							+ "<label class='control-label col-md-2'>Cheque Bank<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ " <input name ='txt_vouchertrans_cheque_bank" + i + "' type='text' id='txt_vouchertrans_cheque_bank"
							+ i + "' onKeyUp='toInteger('txt_vouchertrans_cheque_bank" + i
							+ "')' class='form-control' value='" + vouchertrans_cheque_bank + "' size='12' maxlength='255'/>"
							+ "</div>"
							+ "<label class='control-label col-md-2'>"
							+ "Cheque Branch<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_branch"
							+ i
							+ "' type='text' id='txt_vouchertrans_cheque_branch"
							+ i
							+ "' onKeyUp='toInteger('txt_vouchertrans_cheque_branch"
							+ i
							+ "')' class='form-control' value='"
							+ vouchertrans_cheque_branch
							+ "' size='12' maxlength='255'/>"
							+ "</div></div></div>";
				} else if (customer_type1.equals("2") && paymode_id.equals("3")) {
					vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");
					vouchertrans_cheque_bank = crs.getString("vouchertrans_cheque_bank");
					Str += "<div class='col-md-12'>"
							+ "<div class='col-md-10'style='top:10px'>"
							+ "<label class='control-label col-md-2'>"
							+ "Card No<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_no" + i + "' type='text' id='txt_vouchertrans_cheque_no" + i
							+ "' onKeyUp='toInteger('txt_vouchertrans_cheque_no"
							+ i + "')' class='form-control' value='" + vouchertrans_cheque_no + "' size='12' maxlength='6'/>"
							+ "</div>"
							+ "<label class='control-label col-md-2'>"
							+ "Bank<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_bank" + i + "' type='text' id='txt_vouchertrans_cheque_bank" + i
							+ "' onKeyUp='toInteger('txt_vouchertrans_cheque_bank" + i + "')' class='form-control' value='" + vouchertrans_cheque_bank
							+ "' size='12' maxlength='255'/></div></div></div>";
				} else if (customer_type1.equals("2") && paymode_id.equals("5")) {
					vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");

					Str += "<div class='col-md-12'><div class='col-md-10'style='top:10px'><label class='control-label col-md-2'>"
							+ "Transaction No<font color='#ff0000'>*</font>:</label>"
							+ "<div class='col-md-4'>"
							+ " <input name ='txt_vouchertrans_cheque_no"
							+ i
							+ "' type='text' id='txt_vouchertrans_cheque_no"
							+ i
							+ "' onKeyUp='toInteger('txt_vouchertrans_cheque_no"
							+ i + "')' class='form-control' value='"
							+ vouchertrans_cheque_no
							+ "' size='12' maxlength='6'/></div></div></div>";
				} else if (customer_type1.equals("2") && paymode_id.equals("6")) {
					vouchertrans_cheque_bank = crs.getString("vouchertrans_cheque_bank");
					Str += "<div class='col-md-12'>"
							+ "<div class='col-md-10'style='top:10px'>"
							+ "<label class='control-label col-md-2'>"
							+ "Bank<font color='#ff0000'>*</font>:"
							+ "</label>"
							+ "<div class='col-md-4'>"
							+ "<input name ='txt_vouchertrans_cheque_bank" + i + "' type='text' id='txt_vouchertrans_cheque_bank" + i + "' onKeyUp='toInteger('txt_vouchertrans_cheque_bank"
							+ i + "')' class='form-contorl' value='" + vouchertrans_cheque_bank + "' size='12' maxlength='255'/>";
				}
				Str += "</div>\n" + "</div>\n" + "</div></div>" + "\n";
				i++;
			}
			Str += "";

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("Str=====" + Str);
		return Str;
	}
	public String PopulatePaymode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT paymode_id, paymode_name"
					+ " FROM axela_acc_paymode"
					+ " WHERE 1=1"
					+ " GROUP BY paymode_id"
					+ " ORDER BY paymode_id";

			// SOP("paymode StrSql====" + StrSql);

			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			// Str.append("<option value='1'>Cash</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"), paymode_id));
				Str.append(">").append(crs.getString("paymode_name")).append("</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String HoldPayments(HttpServletRequest request) {
		String AddRowGroupStr = "";
		String ledgertype = "";
		AddRowGroupStr = "\n"
				+ "<div class=\"form-body\">\n"
				+ "<div class=\"form-group\">\n";
		for (int i = 1; i <= paymode_count; i++) {
			bill_amount = PadQuotes(request.getParameter("txt_bill_amt" + i + ""));
			customer_type = PadQuotes(request.getParameter("dr_payment" + i + ""));
			customer_typearr = customer_type.split("-");
			ledgertype = customer_typearr[1];
			paymode_id = CNumeric(PadQuotes(request.getParameter("dr_paymode" + i + "")));
			AddRowGroupStr += "\n"
					+ "<div class='form-body'>"
					+ "<div class='form-group'>"
					+ "<div class='form-element4 form-element'>"
					+ "<label>Payment<b><font color='#ff0000'>*</font></b>:</label>"
					+ "<select name='dr_payment" + i + "' id='dr_payment" + i + "' class='form-control' onChange='Displaypaymode(" + i + ")'>"
					+ PopulatePayment() + "</select>"
					+ "</div>"
					+ "<div class='form-element4 form-element'>"
					+ "<label>By<b><font color='#ff0000'>*</font></b>:</label>"
					+ "<select name='dr_paymode" + i + "' id='dr_paymode" + i + "' class='form-control' onChange='Displaypaymode(" + i + ");'>"
					+ PopulatePaymode() + "</select>"
					+ "</div>\n"
					+ "<div class='form-element2 form-element'>"
					+ "<label>Amount<b><font color='#ff0000'>*</font></b>:</label>"
					+ "<input name='txt_bill_amt" + i + "' id='txt_bill_amt" + i + "' value='" + bill_amount
					+ "' type='text' class='form-control' onKeyUp=toNumber('bill_amount" + i + ")' maxlength='10' size='11'/>"
					+ "</div>"
					+ "<div class='form-element1 form-element-margin'>"
					+ "<img src='../admin-ifx/add.png' class='add' type='button' align='middle'/>"
					+ "&nbsp;<img src='../admin-ifx/cancel.png' class='del' type='button' align='middle'/>"
					+ "</div>\n"
					+ "<div id='displayrow" + i + "'>";
			if (customer_type.equals("2") && paymode_id.equals("2")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i + ""));
				vouchertrans_cheque_date = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date" + i + ""));
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				vouchertrans_cheque_branch = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch" + i + ""));
				AddRowGroupStr += "<div class='col-md-12'>"
						+ "<div class='col-md-10'style='top:10px'>"
						+ "<label class='control-label col-md-2'>"
						+ "Cheque No<font color='#ff0000'>*</font>:"
						+ "</label>"
						+ "<div class='col-md-4'>"
						+ "<input name ='txt_vouchertrans_cheque_no" + i
						+ "' type='text' id='txt_vouchertrans_cheque_no" + i
						+ "' onKeyUp=toInteger('txt_vouchertrans_cheque_no" + i
						+ "') class='form-control' value='" + vouchertrans_cheque_no
						+ "' size='12' maxlength='6'/>"
						+ "</div>"
						+ "<label class='control-label col-md-2'>"
						+ "Cheque Date<font color='#ff0000'>*</font>:"
						+ " </label>"
						+ "<div class='col-md-4'>"
						+ "<input name ='txt_vouchertrans_cheque_date" + i
						+ "' type='text' id='txt_vouchertrans_cheque_date" + i + "' onclick='chequedate(" + i
						+ ")' data-date-format='dd/mm/yyyy' class='form-control date-picker' value='" + vouchertrans_cheque_date + "' size='12' maxlength='6'/>"
						+ "</div>"
						+ "</div>"
						+ "<div class='col-md-10' style='top:10px'>"
						+ "<label class='control-label col-md-2'>"
						+ "Cheque Bank<font color='#ff0000'>*</font>:"
						+ "</label>"
						+ "<div class='col-md-4'>"
						+ "<input name ='txt_vouchertrans_cheque_bank" + i + "' type='text' id='txt_vouchertrans_cheque_bank" + i
						+ "' class='form-control' value='" + vouchertrans_cheque_bank + "' size='12' maxlength='6'/>"
						+ "</div>"
						+ "<label class='control-label col-md-2'>"
						+ "Cheque Branch<font color='#ff0000'>*</font>:"
						+ "</label>"
						+ "<div class='col-md-4'>"
						+ " <input name ='txt_vouchertrans_cheque_branch" + i + "' type='text' id='txt_vouchertrans_cheque_branch" + i
						+ "' class='form-control' value='" + vouchertrans_cheque_branch + "' size='12' maxlength='6'/>"
						+ "</div></div></div>";
			} else if (customer_type.equals("2") && paymode_id.equals("3")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i + ""));
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				AddRowGroupStr += "<div class='col-md-12'>"
						+ "<div class='col-md-10'style='top:10px'>"
						+ "<label class='control-label col-md-2'>"
						+ "Card No<font color='#ff0000'>*</font>:"
						+ "</label>"
						+ "<div class='col-md-4'>"
						+ "<input name ='txt_vouchertrans_cheque_no" + i
						+ "' type='text' id='txt_vouchertrans_cheque_no" + i
						+ "' onKeyUp=toInteger('txt_vouchertrans_cheque_no" + i
						+ "') class='form-control' value='" + vouchertrans_cheque_no
						+ "' size='12' maxlength='6'/>"
						+ "</div>"
						+ "<label class='control-label col-md-2'>"
						+ "Bank<font color='#ff0000'>*</font>:"
						+ "</label>"
						+ "<div class='col-md-4'><input name ='txt_vouchertrans_cheque_bank" + i
						+ "' type='text' id='txt_vouchertrans_cheque_bank" + i
						+ "' o class='form-control' value='"
						+ vouchertrans_cheque_bank
						+ "' size='12' maxlength='255'/></div></div></div>";
			} else if (customer_type.equals("2") && paymode_id.equals("5")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i + ""));
				AddRowGroupStr += "<br>Card No<font color='#ff0000'>*</font>:"
						+ " <input name ='txt_vouchertrans_cheque_no" + i
						+ "' type='text' id='txt_vouchertrans_cheque_no" + i
						+ "' onKeyUp=toInteger('txt_vouchertrans_cheque_no" + i
						+ "') class='form-control' value='" + vouchertrans_cheque_no
						+ "' size='12' maxlength='6'/>";
			} else if (customer_type.equals("2") && paymode_id.equals("6")) {
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				AddRowGroupStr += "<br>Cheque Bank<font color='#ff0000'>*</font>:"
						+ " <input name ='txt_vouchertrans_cheque_bank"
						+ i
						+ "' type='text' id='txt_vouchertrans_cheque_bank"
						+ i
						+ "' class='form-control' value='"
						+ vouchertrans_cheque_bank
						+ "' size='12' maxlength='255'/>";
			}
			AddRowGroupStr += "</div>" + "</div>";
		}
		AddRowGroupStr += "</div></div>";
		return AddRowGroupStr;
	}
	public String PopulateItemSerial() {
		String itemserial = "";
		itemserial = itemserial + "<option value = 1"
				+ Selectdrop(1, item_serial_id) + ">By Item Name</option>\n";
		itemserial = itemserial + "<option value = 2"
				+ Selectdrop(2, item_serial_id) + ">By Serial No.</option>\n";
		return itemserial;
	}

	public String PopulateBranchClass(String customer_branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					+ " AND rateclass_type = 1"
					+ " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";
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

	public String PopulateLocation(String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " ORDER BY location_name";
			// SOP("location===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"), voucher_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void AddPayment(Statement stmttx, HttpServletRequest request) throws SQLException {
		for (int i = 1; i <= paymode_count; i++) {
			bill_amount = PadQuotes(request.getParameter("txt_bill_amt" + i + ""));
			customer_typearr = PadQuotes(request.getParameter("dr_payment" + i + "")).split("-");
			customer_type = customer_typearr[1];
			voucher_customer_id = customer_typearr[0];
			paymode_id = CNumeric(PadQuotes(request.getParameter("dr_paymode" + i + "")));
			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_amount," + " vouchertrans_time,"
					+ " vouchertrans_dc," + " vouchertrans_paymode_id";
			if (customer_type.equals("2") && paymode_id.equals("2")) {
				StrSql += ", vouchertrans_cheque_no,"
						+ " vouchertrans_cheque_date,"
						+ " vouchertrans_cheque_bank,"
						+ " vouchertrans_cheque_branch";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("3")) {
				StrSql += ", vouchertrans_cheque_no,"
						+ " vouchertrans_cheque_bank";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("5")) {
				StrSql += ", vouchertrans_cheque_no";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("6")) {
				StrSql += ", vouchertrans_cheque_bank";
			}
			StrSql += ", vouchertrans_reconciliation)" + " VALUES"
					+ " (" + voucher_id + "," + " "
					+ voucher_customer_id + "," + " "
					+ voucher_location_id + "," + " "
					+ bill_amount + "," + " " + ToLongDate(kknow())
					+ "," + " '1'," + " " + paymode_id + "";
			if (customer_type.equals("2") && paymode_id.equals("2")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i
						+ ""));
				vouchertrans_cheque_date = ConvertShortDateToStr(PadQuotes(request.getParameter("txt_vouchertrans_cheque_date" + i + "")));
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				vouchertrans_cheque_branch = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch" + i + ""));
				StrSql += ", " + vouchertrans_cheque_no + "," + " '"
						+ vouchertrans_cheque_date + "'," + " '"
						+ vouchertrans_cheque_bank + "'," + " '"
						+ vouchertrans_cheque_branch + "'";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("3")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i + ""));
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				StrSql += ", '" + vouchertrans_cheque_no + "'," + " '"
						+ vouchertrans_cheque_bank + "'";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("5")) {
				vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no" + i + ""));
				StrSql += ", '" + vouchertrans_cheque_no + "'";
			} else if (customer_type.equals("2")
					&& paymode_id.equals("6")) {
				vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank" + i + ""));
				StrSql += ", '" + vouchertrans_cheque_bank + "'";
			}
			StrSql += ", '1')";
			// SOP("StrSql=PAYMODE MADE==" + StrSqlBreaker(StrSql));
			stmttx.addBatch(StrSql);
		}
	}
}
