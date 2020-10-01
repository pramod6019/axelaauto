//Vidyanandan 11 Nov 2014
package axela.accounting;

//import axela.voucher.*;
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

public class CreditNote_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String strHTML = "";
	public String msg = "";
	public String readOnly = "";
	public String QueryString = "";
	public String comp_id = "0";

	// customer
	public String currentbal_amount = "0.00";
	// voucher
	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String voucher_vouchertype_id = "0";
	public String voucher_tag_id = "0";
	public String voucher_entry_type = "0";
	public String voucher_no = "0";
	public String voucher_branch_id = "0";
	public String voucher_date = "";
	public String voucher_amount = "";
	public String voucher_prev_amount = "";
	public String voucher_narration = "";
	public String voucher_customer_id = "";
	public String voucher_contact_id = "0", contact_title_id = "0",
			contact_fname = "", contact_lname = "";
	public String voucher_emp_id = "0";
	public String voucher_location_id = "0";
	public String voucher_ref_no = "";
	public String voucher_notes = "";
	public String voucher_active = "1";
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_date = "";
	public String vouchertrans_id = "0";
	public String vouchertrans_voucher_id = "0";
	public String vouchertrans_customer_id = "";
	public String vouchertrans_amount = "0";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "0";
	public String vouchertrans_paymode_id = "-1";
	public String payment_id = "0";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "", vouchertrans_chequedate = "";
	public String vouchertrans_cheque_bank = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_id = "0", vouchertype_name = "";

	public String vouchertype_defaultauthorize = "0", voucher_authorize = "0";
	public String vouchertype_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String vouchertypeid = "0";
	public String vouchertype_suffix = "", vouchertype_prefix = "";
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String vouchertype_terms = "";
	public String emp_branch_id = "0";
	public String voucherdate = "";
	public String BranchAccess = "";
	public String empEditperm = "1";
	public String ExeAccess = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String debitcustid = "";
	public String creditcustid = "";
	public String voucherclass_acc = "";
	String emp_formattime = "", config_customer_name = "";
	public int voucher_count = 0;
	public String voucherbal_trans_id = "0", checked = "";
	public double voucherrecpt_amount = 0.00;
	public int voucherbal_amount = 0;
	// config variables ///
	public String vouchertype_email_enable;
	public String vouchertype_email_auto;
	// public String vouchertype_email_sub;
	// public String vouchertype_email_format;
	public String vouchertype_sms_enable;
	public String vouchertype_sms_auto;
	// public String vouchertype_sms_format;
	public String config_admin_email;
	public String config_email_enable;
	public String config_sms_enable;
	public String comp_email_enable;
	public String comp_sms_enable;
	public String contact_mobile = "";
	public Ledger_Check ledger = new Ledger_Check();
	public int prepkey = 1;
	DecimalFormat deci = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public String invoice_no = "";
	// public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_receipt_access," + "emp_acc_credit_note_access",
					request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				config_customer_name = GetSession("config_customer_name", request);
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				// empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);
				// voucher_branch_id = CNumeric(PadQuotes(request
				// .getParameter("dr_branch_id")));
				voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				PopulateConfigDetails();
				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}

				if (add.equals("yes")) {
					status = "Add";
					// ----Branch
					if (emp_branch_id.equals("0")) {
						emp_branch_id = CNumeric(GetSession("voucher_branch_id",
								request));
						//
						if (emp_branch_id.equals("0")) {
							emp_branch_id = getActiveBranchID(request, emp_id);
							SetSession("voucher_branch_id", emp_branch_id, request);
						}
						voucher_branch_id = emp_branch_id;
					} else {
						voucher_branch_id = emp_branch_id;
					}
					// ----Branch
					// branch_name =
					// ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					// + " FROM  "+compdb(comp_id)+"axela_branch"
					// + " WHERE branch_id = "
					// + voucher_branch_id);
					if (voucherdate.equals("")) {
						voucherdate = strToShortDate(ToLongDate(kknow()));
					}
					// ----Branch
					// branch_name =
					// ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					// + " FROM  "+compdb(comp_id)+"axela_branch"
					// + " WHERE branch_id = "
					// + voucher_branch_id);
					if (voucherdate.equals("")) {
						voucherdate = strToShortDate(ToLongDate(kknow()));
					}
					if (!addB.equals("yes")) {
						voucher_emp_id = emp_id;
					} else if (addB.equals("yes")) {
						voucher_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_receipt_add, emp_acc_credit_note_add", request).equals("1")) {
							AddFields(request);
							voucher_no = vouchertype_prefix + voucher_no + vouchertype_suffix;
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?"
												+
												"voucher_id="
												+ voucher_id
												+ "&voucherclass_id="
												+ voucherclass_id
												+ "&vouchertype_id="
												+ vouchertype_id
												+ "&msg=Credit Note Added Successfully!"));

							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Credit Note")) {
						if (!voucher_id.equals("0")) {
							PopulateFields();
							if (!msg.equals("")) {
								response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg + ""));
							}
						}
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Credit Note")) {
						if (ReturnPerm(comp_id, "emp_receipt_edit, emp_acc_credit_note_edit", request).equals("1")) {
							GetValues(request, response);
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?"
										+ "voucher_id=" + voucher_id
										+ "&voucherclass_id="
										+ voucherclass_id
										+ "&vouchertype_id="
										+ vouchertype_id
										+ "&msg=Credit Note Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Credit Note")) {
						response.sendRedirect(AccessDenied());
						// GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_receipt_delete, emp_acc_credit_note_delete", request).equals("1")) {
							// DeleteFields(request, response);
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
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (empEditperm.equals("1")) {
			voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		} else {
			voucherdate = PadQuotes(request.getParameter("hid_txt_voucher_date"));
		}
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		creditcustid = PadQuotes(request.getParameter("ledger1"));
		debitcustid = PadQuotes(request.getParameter("ledger2"));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		// SOP("voucher_contact_id====1---"+voucher_contact_id);
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		voucher_amount = CNumeric(PadQuotes(request.getParameter("txt_voucher_amount")));
		voucher_prev_amount = PadQuotes(request.getParameter("txt_voucher_prev_amount"));
		payment_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_payment")));
		vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));
		if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no"));
			vouchertrans_chequedate = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date"));
			vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_cheque_bank"));
			vouchertrans_cheque_branch = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_card_no"));
			vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_card_bank"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_txn_no"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
			vouchertrans_cheque_bank = PadQuotes(request.getParameter("txt_vouchertrans_bank"));
		}

		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));
		voucher_no = CNumeric(PadQuotes(request.getParameter("hid_voucher_no")));
		if (voucher_active.equals("1") && vouchertype_defaultauthorize.equals("1")) {
			if (voucher_no.equals("0")) {
				voucher_no = getVoucherNo(voucher_branch_id, vouchertype_id, "0", "0");
			}
			voucher_authorize = "1";
		} else if (CNumeric(voucher_active).equals("0")) {
			voucher_authorize = "0";
		}
		if (voucher_authorize.equals("1") && voucher_authorize_id.equals("0") && voucher_authorize_time.equals("")) {
			voucher_authorize_id = emp_id;
			voucher_authorize_time = ToLongDate(kknow());
		}
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_narration = PadQuotes(request.getParameter("txt_voucher_narration"));
		invoice_no = PadQuotes(request.getParameter("txt_invoice_no"));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		voucher_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_count"))));
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		vouchertrans_amount = voucher_amount;
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";

		if (voucherdate.equals("")) {
			msg += "<br>Enter  Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br> Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter Valid  Date!";
			}
		}
		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (CNumeric(creditcustid).equals("0")) {
			msg += "<br>Select Credit To!";
		} else if (voucher_contact_id.equals("0")) {
			msg += "<br>Select Contact!";
		}

		if (voucher_amount.equals("0")) {
			msg += "<br>Enter Amount!";
		} else if (isNumeric(voucher_amount)) {
			if (Double.parseDouble(voucher_amount) <= 0) {
				msg += "<br>Amount: Must be greater than 0!";
			}
		} else {
			msg += "<br>Amount: Must be Numeric!";
		}

		if (CNumeric(debitcustid).equals("0")) {
			msg += "<br>Select Debit To!";
		}
		if (!CNumeric(creditcustid).equals("0") && !CNumeric(debitcustid).equals("0")) {
			if (CNumeric(debitcustid).equals(CNumeric(creditcustid))) {
				msg += "<br> Credit To & Debit To Ledger cannot be same!";
			}
		}

		if (vouchertrans_paymode_id.equals("2")) {
			if (vouchertrans_cheque_no.equals("")) {
				msg += "<br>Enter Cheque No!";
			} else if (vouchertrans_cheque_no.length() < 6) {
				msg += "<br>Cheque No. can't be less than 6 digits!";
			}

			if (vouchertrans_chequedate.equals("")) {
				msg += "<br>Enter Cheque Date!";
			} else if (!isValidDateFormatShort(vouchertrans_chequedate)) {

				msg += "<br>Enter valid Cheque Date!";
			} else {
				vouchertrans_cheque_date = ConvertShortDateToStr(vouchertrans_chequedate);
			}

			if (!voucherdate.equals("") && isValidDateFormatShort(voucherdate)
					&& !vouchertrans_chequedate
							.equals("")
					&& isValidDateFormatShort(vouchertrans_chequedate)) {
				if (Long.parseLong(vouchertrans_cheque_date) < Long
						.parseLong(voucher_date)) {
					msg += "<br>Cheque Date should not be less than  Date!";
				}
			}
			if (vouchertrans_cheque_bank.equals("")) {
				msg += "<br>Enter Bank Name!";
			}

			if (vouchertrans_cheque_branch.equals("")) {
				msg += "<br>Enter Branch Name!";
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

			if (!voucher_branch_id.equals("0")) {
				StrSql = "SELECT voucher_ref_no FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_branch_id = " + voucher_branch_id + " "
						+ " AND voucher_ref_no = '" + voucher_ref_no + "'"
						+ " AND voucher_id != " + voucher_id + ""
						+ " AND voucher_vouchertype_id = " + vouchertype_id;
				// SOP("StrSql=123=="+StrSqlBreaker(StrSql));
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar Reference No. found!";
				}
			}
		}

		if (voucher_narration.equals("")) {
			msg += "<br>Enter Narration!";
		}
		for (int i = 0; i < voucher_count; i++) {
			double voucherrecpt_amount = 0, voucherbal_amount = 0;
			String voucher_no = "";
			checked = PadQuotes(request.getParameter("chk_" + i + ""));
			voucher_no = PadQuotes(request.getParameter("txt_voucherno_" + i + ""));
			if (!CNumeric(PadQuotes(request.getParameter("txt_amount_" + i + ""))).equals("0")) {
				voucherrecpt_amount = Double.parseDouble(PadQuotes(request.getParameter("txt_amount_" + i + "")));
			}
			if (!CNumeric(
					PadQuotes(request.getParameter("txt_balamount_" + i + "")))
					.equals("0")) {
				voucherbal_amount = Double.parseDouble(PadQuotes(request.getParameter("txt_balamount_" + i + "")));
			}

			if ((voucherbal_amount < voucherrecpt_amount) && checked
					.equals("on")) {
				msg += "<br>Allocated amount for Invoice No.:" + voucher_no + " is greater than  Amount!";
			}
		}

		for (int i = 0; i < voucher_count; i++) {
			checked = PadQuotes(request.getParameter("chk_" + i + ""));
			if (!CNumeric(PadQuotes(request.getParameter("txt_amount_" + i + "")))
					.equals("0") && checked.equals("on")) {
				voucherrecpt_amount += Double.parseDouble(PadQuotes(request.getParameter("txt_amount_" + i + "")));
			}
		}
		if ((Double.parseDouble(voucher_amount) < voucherrecpt_amount)) {
			msg += "<br>Allocated amount for Invoices is greater than  Amount!";
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
						+ " ("
						+ " voucher_vouchertype_id,"
						+ " voucher_tag_id,"
						+ " voucher_entry_type,"
						+ " voucher_no,"
						+ " voucher_branch_id,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_narration,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_emp_id,"
						+ " voucher_ref_no,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_notes,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES" + " (" + " "
						+ vouchertype_id + ","
						+ " " + voucher_tag_id + ","
						+ " " + voucher_entry_type + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + voucher_branch_id + ","
						+ " '" + voucher_date + "',"
						+ " " + voucher_amount + ","
						+ " '" + voucher_narration + "',"
						+ " " + creditcustid + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_emp_id + ","
						+ " '" + voucher_ref_no + "',"
						+ " '" + vouchertype_terms + "',"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_notes + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " " + emp_id + "," + " '"
						+ voucher_entry_date + "')";
				// SOP("StrSql==credit ==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs.close();
				// / Credit to ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,";
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + vouchertrans_voucher_id + "," + " "
						+ creditcustid + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ 0 + "'," + " '" + 0 + "'" + " )";
				// SOP("StrSql==Credit-trans ==" + StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);

				// / credit to ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,";
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + vouchertrans_voucher_id + "," + " "
						+ debitcustid + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ 1 + "'," + " '" + 0 + "'" + " )";
				// SOP("StrSql==debit-trans ==" + StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);

				conntx.commit();

				// UPDATE "+compdb(comp_id)+"the customer current balance
				// SOP("voucherclass_acc===1=1==" + voucherclass_acc);
				if (voucherclass_acc.equals("1")) {
					// SOP(" coming voucherclass_acc===1==" + voucherclass_acc);
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, creditcustid, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}
				// /////voucher balance
				InsertPendingInvoice(request, "");

				// / mail sending
				if (config_email_enable.equals("1") && vouchertype_email_enable.equals("1") && vouchertype_email_auto.equals("1") && comp_email_enable
						.equals("1")) {
					Voucher_Email Voucher_Email_obj = new Voucher_Email();
					Voucher_Email_obj.voucher_id = voucher_id;

					Voucher_Email_obj.config_admin_email = config_admin_email;
					Voucher_Email_obj.SendEmail(comp_id);
					Voucher_Email_obj = null;
				}

				// sms sending
				if (config_sms_enable.equals("1") && vouchertype_sms_enable.equals("1") && vouchertype_sms_auto.equals("1")
						&& comp_sms_enable
								.equals("1")) {
					Voucher_SMS Voucher_SMS_obj = new Voucher_SMS();
					Voucher_SMS_obj.voucher_id = voucher_id;

					contact_mobile = ExecuteQuery("SELECT contact_mobile1"
							+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
							+ " WHERE contact_id = "
							+ voucher_contact_id);
					Voucher_SMS_obj.SendSMS(voucher_active, comp_id);
					Voucher_SMS_obj = null;
				}

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOPError("connection Rollback...");
					conntx.rollback();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception()
						.getStackTrace()[0].getMethodName() + ": " + e);
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
			// new Ledger_Check().PopulateInvoiceDetail(request,
			// vouchertrans_customer_id, msg);
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_amount,"
					+ " voucher_no,"
					+ " voucher_date, voucher_branch_id,voucher_customer_id, voucher_contact_id,"
					+ " vouchertrans_paymode_id, vouchertrans_cheque_no, vouchertrans_cheque_branch,"
					+ " vouchertrans_cheque_date, vouchertrans_cheque_bank,"
					+ " (SELECT vouchertrans_customer_id FROM " + compdb(comp_id) + "axela_acc_voucher_trans WHERE"
					+ " vouchertrans_voucher_id = voucher_id AND vouchertrans_dc = 1) AS debitcustid,"
					+ " voucher_notes, voucher_emp_id, voucher_ref_no, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, voucher_authorize,voucher_authorize_id,voucher_authorize_time,"
					+ " COALESCE((select CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix)"
					+ " from " + compdb(comp_id) + "axela_acc_voucher "
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " WHERE voucher_id = voucherbal_trans_id),'') AS invoice_no,"
					+ " branch_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = voucher_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id "
					+ " WHERE voucher_id = ?";
			// + BranchAccess + ExeAccess;
			// SOP("StrSql=populate==" + StrSqlBreaker(StrSql));
			prepmap.put(prepkey++, voucher_id);
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					invoice_no = crs.getString("invoice_no");
					voucher_date = crs.getString("voucher_date");
					voucherdate = strToShortDate(voucher_date);

					voucher_branch_id = CNumeric(crs.getString("voucher_branch_id"));
					creditcustid = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					currentbal_amount = ReturnCustomerCurrBalance(creditcustid, comp_id, vouchertype_id);
					debitcustid = crs.getString("debitcustid");
					voucher_amount = crs.getString("voucher_amount");
					voucher_branch_id = crs.getString("voucher_branch_id");
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_narration = crs.getString("voucher_narration");
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_notes = crs.getString("voucher_notes");
					voucher_entry_id = crs.getString("voucher_entry_id");
					if (!voucher_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(voucher_entry_id));
					}
					entry_date = strToLongDate(crs.getString("voucher_entry_date"));
					voucher_modified_id = crs.getString("voucher_modified_id");
					if (!voucher_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(voucher_modified_id));
					}
					modified_date = strToLongDate(crs.getString("voucher_modified_date"));
				}
			} else {
				msg += "<br>Invalid Credit Note!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE  " + compdb(comp_id) + "  axela_acc_voucher"
						+ " SET"
						+ " voucher_branch_id= " + voucher_branch_id + ","
						+ " voucher_date= '" + voucher_date + "',"
						+ " voucher_entry_type= '" + voucher_entry_type + "',"
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_amount= " + voucher_amount + ","
						+ " voucher_customer_id= " + creditcustid + ","
						+ " voucher_contact_id= " + voucher_contact_id + ","
						+ " voucher_ref_no= '" + voucher_ref_no + "',"
						+ " voucher_active= '" + voucher_active + "',"
						+ " voucher_narration= '" + voucher_narration + "',"
						+ " voucher_notes= '" + voucher_notes + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql=updatefields==" + StrSql);
				stmttx.executeUpdate(StrSql);

				stmttx.executeUpdate("DELETE FROM  " + compdb(comp_id) + " axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id);

				// / Credit to ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,";
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + "," + " " + creditcustid
						+ "," + " " + CNumeric(vouchertrans_amount)
						+ "," + " '" + 0 + "'," + " ";
				StrSql += " '" + 0 + "'" + " )";
				// SOP("StrSql===UPDATE  "+compdb(comp_id)+"credir=="+StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);

				// / debit to ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,";
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + "," + " " + debitcustid
						+ "," + " " + CNumeric(vouchertrans_amount)
						+ "," + " '" + 1 + "'," + " ";
				StrSql += " '" + 1 + "'" + " )";
				// SOP("StrSql===UPDATE  "+compdb(comp_id)+"debit=="+StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);

				conntx.commit();

				// UPDATE "+compdb(comp_id)+"the customer current balance
				// SOP("voucherclass_acc===1==2==" + voucherclass_acc);
				if (voucherclass_acc.equals("1")) {
					// SOP("voucherclass_acc===2==" + voucherclass_acc);
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, creditcustid, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}
				// /////voucher balance
				InsertPendingInvoice(request, "yes");

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOPError("connection Rollback...");
					conntx.rollback();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception()
						.getStackTrace()[0].getMethodName() + ": " + e);
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
			HttpServletResponse response) throws SQLException {
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// Inactive voucher before deleting so balance is calculated
				// excluding this voucher
				updateQuery("UPDATE  " + compdb(comp_id) + "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");
				// UPDATE "+compdb(comp_id)+"the customer current balance
				// SOP("voucherclass_acc==1=3==" + voucherclass_acc);
				if (voucherclass_acc.equals("1")) {
					// SOP("voucherclass_acc===3==" + voucherclass_acc);
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, creditcustid, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.execute(StrSql);
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = "
						+ voucher_id + "";
				stmttx.execute(StrSql);

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = "
						+ voucher_id + " ";
				stmttx.execute(StrSql);
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOPError("connection rollback...");
					conntx.rollback();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception()
						.getStackTrace()[0].getMethodName() + ": " + e);
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

	public String PopulatePayment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_ledgertype, customer_name"
					+ " FROM axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_ledgertype"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("customer_ledgertype"));
				Str.append(StrSelectdrop(crs.getString("customer_ledgertype"),
						payment_id));
				Str.append(">").append(crs.getString("customer_name")).append(
						"</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void CustomerCurrentBalance(String voucher_customer_id)
			throws SQLException {
		try {
			StrSql = "UPDATE  " + compdb(comp_id) + " axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = voucher_customer_id"
					+ " SET";
			if (deleteB.equals("Delete Debit NOte")) {
				StrSql += " customer_curr_bal = customer_curr_bal  - "
						+ CNumeric(voucher_prev_amount) + "";
			} else {
				StrSql += " customer_curr_bal = customer_curr_bal + "
						+ CNumeric(voucher_amount) + " - "
						+ CNumeric(voucher_prev_amount) + "";
			}
			StrSql += " WHERE voucher_active = 1" + " AND voucher_customer_id = "
					+ voucher_customer_id + "";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection Rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name=\"dr_contact_id\" class=\"form-control\" id=\"dr_contact_id\" >");
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = "
					+ CNumeric(customer_id)
					+ ""
					+ " GROUP BY contact_id"
					+ " order by contact_fname";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				Str.append(
						Selectdrop(
								Integer.parseInt(crs.getString("contact_id")),
								voucher_contact_id)).append(">");
				Str.append(crs.getString("contact_name"));
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
			return "";
		}

	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT vouchertype_name, vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms,"
					+ " vouchertype_defaultauthorize, vouchertype_authorize,"
					+ " vouchertype_prefix, vouchertype_suffix,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					// +
					// " COALESCE(vouchertype_email_sub, '') AS vouchertype_email_sub,"
					// +
					// " COALESCE(vouchertype_email_format, '') AS vouchertype_email_format,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					// +
					// " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ "  " + compdb(comp_id) + "axela_comp,"
					+ "  " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ "  " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id
					+ "" + " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=config details==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_prefix = crs.getString("vouchertype_prefix");
				vouchertype_suffix = crs.getString("vouchertype_suffix");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				voucher_authorize = vouchertype_defaultauthorize;
				// SOP("voucher_authorize===PopulateConfigDetails===" + voucher_authorize);
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_auto = crs.getString("vouchertype_email_auto");
				// vouchertype_email_sub =
				// crs.getString("vouchertype_email_sub");
				// vouchertype_email_format =
				// crs.getString("vouchertype_email_format");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
				// vouchertype_sms_format =
				// crs.getString("vouchertype_sms_format");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				vouchertype_terms = crs.getString("vouchertype_terms");
				// SOP("branch_name==="+branch_name);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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

	// //////////Insert Pending Balance
	protected void InsertPendingInvoice(HttpServletRequest request,
			String update) {
		String voucherbal_trans_id = "0", voucherbal_amount = "0", checked =
				"0";
		try {
			if (update.equals("yes")) {
				StrSql =
						"DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
								+ " WHERE voucherbal_voucher_id = "
								+ voucher_id + " ";
				// stmttx.execute(StrSql);
				// SOP("StrSql=de-bal==" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			}
			// SOP("voucher_count =111----- " + voucher_count);
			for (int i = 0; i <= voucher_count; i++) {
				voucherbal_trans_id =
						CNumeric(PadQuotes(request
								.getParameter("txt_voucherid_" + i + "")));
				voucherbal_amount =
						PadQuotes(request.getParameter("txt_amount_" + i + ""));// /Debit
																				// NOte
																				// amount
				checked = PadQuotes(request.getParameter("chk_" + i + ""));
				if (checked.equals("on") & voucher_active.equals("1")) {
					StrSql =
							"INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_bal" + " ("
									+ " voucherbal_voucher_id,"
									+ " voucherbal_trans_id,"
									+ " voucherbal_amount,"
									+ " voucherbal_date)" + " VALUES" + " ("
									+ " " + voucher_id + "," + " "
									+ voucherbal_trans_id + "," + " "
									+ CNumeric(voucherbal_amount) + "," + " '"
									+ voucher_date + "'" + " )";
					// stmttx.execute(StrSql);
					// SOP("StrSql=bal-in==" + StrSqlBreaker(StrSql));
					updateQuery(StrSql);
				}
				// SOP("StrSql==="+StrSqlBreaker(StrSql));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql =
					"SELECT title_id, title_desc" + " FROM  " + compdb(comp_id) + "axela_title"
							+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"),
						contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append(
						"</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePaymode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql =
					"SELECT paymode_id, paymode_name"
							+ " FROM axela_acc_paymode" + " WHERE 1=1"
							+ " GROUP BY paymode_id" + " ORDER BY paymode_id";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			// Str.append("<option value='1'>Cash</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"),
						vouchertrans_paymode_id));
				Str.append(">").append(crs.getString("paymode_name")).append(
						"</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
