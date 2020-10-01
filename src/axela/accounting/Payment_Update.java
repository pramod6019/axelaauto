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

public class Payment_Update extends Connect {

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
	public String session_id = "0";
	public String voucher_id = "0";
	public String voucherclass_id = "0", voucherclass_file = "";
	public String vouchertrans_transaction_no = "";
	public String voucher_vouchertype_id = "0";
	public String vouchertype_suffix = "", vouchertype_prefix = "";
	public String voucher_customer_id = "0";
	public String voucher_tag_id = "0";
	public String voucher_entry_type = "0";
	public String voucher_no = "0";
	public String voucher_branch_id = "0";
	public String voucher_date = "";
	public String voucher_amount = "0", mainparty_amount = "0";
	public String voucher_tdsper = "0", voucher_tds_amount = "0";
	public String vouchertype_tds = "0", vouchertype_tds_ledger_id = "0";
	public String voucher_prev_amount = "";
	public String voucher_narration = "";
	public String voucher_contact_id = "0";
	public String voucher_emp_id = "0";
	public String voucher_location_id = "0";
	public String voucher_ref_no = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_notes = "";
	public String voucher_terms = "";
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
	public String voucherclass_acc = "";
	// public String payment_id = "0";
	public String paymentid = "";
	public String paymentidarr[] = null;
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "", vouchertrans_chequedate = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_id = "0", vouchertype_name = "";

	public String vouchertype_defaultauthorize = "0", voucher_authorize = "0";
	public String vouchertype_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String sess_branch_id = "0";
	public String voucherdate = "";
	public String BranchAccess = "";
	public String empEditperm = "1";
	public String ExeAccess = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String emp_id = "0", emp_branch_id = "0";
	public String emp_role_id = "0";

	public String emp_formattime = "", config_customer_name = "";
	public int voucher_count = 0;
	public String checked = "";
	public double voucherpayment_amount = 0.00;
	public int voucherbal_amount = 0;
	// vouchertype variables ///
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String vouchertype_driver_no = "0";
	public String vouchertype_tempo_no = "0";
	public String vouchertype_email_enable;
	public String vouchertype_email_auto;
	public String vouchertype_email_sub;
	public String vouchertype_email_format;
	public String vouchertype_sms_enable;
	public String vouchertype_sms_auto;
	public String vouchertype_sms_format;
	// config variables ///
	public String config_admin_email;
	public String config_email_enable;
	public String config_sms_enable;
	public String comp_email_enable;
	public String comp_sms_enable;
	public String contact_mobile = "";
	public String customer_type = "";
	public Ledger_Check ledger = new Ledger_Check();
	public int prepkey = 1;
	DecimalFormat deci = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;
	public Ledger_Check ledgercheck = new Ledger_Check();
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_payment_access", request, response);
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
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				paymentid = PadQuotes(request.getParameter("dr_voucher_payment"));
				vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));

				PopulateConfigDetails();
				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				session_id = PadQuotes(request.getParameter("txt_session_id"));
				if (session_id.equals("")) {
					String key = "", possible = "0123456789";
					for (int i = 0; i < 9; i++) {
						key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
					}
					session_id = key;
				}
				if (add.equals("yes")) {
					status = "Add";
					voucher_emp_id = emp_id;
					if (voucherdate.equals("")) {
						voucherdate = DateToShortDate(kknow());
					}
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
					if (addB.equals("yes")) {
						voucher_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_payment_add", request).equals("1")) {
							AddFields(request, response);
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
												+ "&msg=Payment Added Successfully!"));

							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Payment")) {
						if (!voucher_id.equals("0")) {
							CopyPartyTransToCart(voucher_id);
							PopulateFields();
							if (!msg.equals("")) {
								response.sendRedirect(response
										.encodeRedirectURL("../portal/error.jsp?msg="
												+ msg + ""));
							}
						}
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Payment")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_payment_edit", request).equals("1")) {
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
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
												+ "&msg="
												+ vouchertype_name
												+ " Updated Successfully!"));

							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Payment")) {
						response.sendRedirect(AccessDenied());
						// GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_payment_delete", request).equals(
								"1")) {
							// DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?" +
												"voucher_id=" + voucher_id
												+ "&voucherclass_id="
												+ voucherclass_id
												+ "&vouchertype_id="
												+ vouchertype_id
												+ "&msg="
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			voucherdate = PadQuotes(request
					.getParameter("txt_hid_voucher_date"));
		}

		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		voucher_customer_id = PadQuotes(request.getParameter("ledger"));
		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		voucher_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		mainparty_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		paymentid = PadQuotes(request.getParameter("dr_voucher_payment"));

		paymentidarr = paymentid.split("-");
		vouchertrans_customer_id = paymentidarr[0];

		vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no"));
			vouchertrans_chequedate = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date"));
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_card_no"));
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_txn_no"));
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_txn_no"));
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_txn_no"));
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_txn_no"));
		}
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_driver_no = PadQuotes(request.getParameter("txt_voucher_driver_no"));
		voucher_tempo_no = PadQuotes(request.getParameter("txt_voucher_tempo_no"));
		voucher_narration = PadQuotes(request.getParameter("txt_voucher_narration"));
		vouchertype_tds = PadQuotes(request.getParameter("txt_vouchertype_tds"));
		vouchertype_tds_ledger_id = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_tds_ledger_id")));
		voucher_tdsper = CNumeric(PadQuotes(request.getParameter("txt_tds_per")));
		voucher_tds_amount = CNumeric(PadQuotes(request.getParameter("txt_tds_amount")));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize")));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));
		voucher_no = PadQuotes(request.getParameter("hid_voucher_no"));
		if (voucher_active.equals("1") && vouchertype_defaultauthorize.equals("1")) {
			if (voucher_no.equals("0")) {
				voucher_no = getVoucherNo(voucher_branch_id, vouchertype_id, "0", "0");
			}
			voucher_authorize = "1";
		} else if (CNumeric(voucher_active).equals("0")) {
			voucher_authorize = "0";
		}
		if (voucher_authorize.equals("1")
				&& voucher_authorize_id.equals("0")
				&& voucher_authorize_time.equals("")) {
			voucher_authorize_id = emp_id;
			voucher_authorize_time = ToLongDate(kknow());
		}

		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		voucher_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_count"))));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		vouchertrans_amount = voucher_amount;
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (voucherdate.equals("")) {
			msg += "<br>Enter Payment Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Payment Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter Valid Payment Date!";
			}
		}

		if (emp_branch_id.equals("0")) {
			if (voucher_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}
		}
		if (vouchertrans_customer_id.equals("0")) {
			msg += "<br>Select Bank Ledger!";
		}
		if (voucher_amount.equals("")) {
			msg += "<br>Enter Amount!";
		} else if (isNumeric(voucher_amount)) {
			if (Double.parseDouble(voucher_amount) <= 0) {
				msg += "<br>Amount: Must be greater than 0!";
			}
		} else {
			msg += "<br>Amount: Must be Numeric!";
		}

		if (vouchertrans_paymode_id.equals("0")) {
			msg += "<br>Select Payment!";
		}
		if (vouchertrans_paymode_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
			if (vouchertrans_cheque_no.equals("")) {
				msg += "<br>Enter Cheque No!";
			} else if (vouchertrans_cheque_no.length() < 6) {
				msg += "<br>Cheque No. con't be less than 6 digits!";
			}

			if (vouchertrans_chequedate.equals("")) {
				msg += "<br>Enter Cheque Date!";
			} else if (!isValidDateFormatShort(vouchertrans_chequedate)) {

				msg += "<br>Enter valid Cheque Date!";
			} else {
				vouchertrans_cheque_date = ConvertShortDateToStr(vouchertrans_chequedate);
			}
			if (!voucherdate.equals("") && isValidDateFormatShort(voucherdate) && !vouchertrans_cheque_date.equals("")
					&& isValidDateFormatShort(vouchertrans_cheque_date)) {
				if (Long.parseLong(vouchertrans_cheque_date) < Long.parseLong(voucher_date)) {
					msg += "<br>Cheque Date should not be less than Payment Date!";
				}
			}

		} else if (vouchertrans_paymode_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
			if (vouchertrans_cheque_no.equals("")) {
				msg += "<br>Enter Card No!";
			}
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}

		} else if (vouchertrans_paymode_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}
		} else if (vouchertrans_paymode_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}
		} else if (vouchertrans_paymode_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}

		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
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

			StrSql = "SELECT voucher_ref_no FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_branch_id = " + voucher_branch_id + " "
					+ " AND voucher_ref_no = '" + voucher_ref_no + "'"
					+ " AND voucher_id != " + voucher_id;
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Reference No. found!";
			}
		}

		if (voucher_narration.equals("")) {
			msg += "<br>Enter Narration!";
		}

		// for (int i = 0; i < voucher_count; i++) {
		// double voucherpayment_amount = 0, voucherbal_amount = 0;
		// String voucher_no = "";
		// checked = PadQuotes(request.getParameter("chk_" + i + ""));
		// voucher_no = PadQuotes(request.getParameter("txt_voucherno_" + i
		// + ""));
		// if (!CNumeric(
		// PadQuotes(request.getParameter("txt_amount_" + i + "")))
		// .equals("0")) {
		// voucherpayment_amount = Double.parseDouble(PadQuotes(request
		// .getParameter("txt_amount_" + i + "")));
		// }
		// if (!CNumeric(
		// PadQuotes(request.getParameter("txt_balamount_" + i + "")))
		// .equals("0")) {
		// voucherbal_amount = Double.parseDouble(PadQuotes(request
		// .getParameter("txt_balamount_" + i + "")));
		// }
		// }

		for (int i = 0; i < voucher_count; i++) {
			checked = PadQuotes(request.getParameter("chk_" + i + ""));
			if (!CNumeric(
					PadQuotes(request.getParameter("txt_amount_" + i + "")))
					.equals("0") && checked.equals("on")) {
				voucherpayment_amount += Double.parseDouble(PadQuotes(request
						.getParameter("txt_amount_" + i + "")));
			}
		}

		// if (voucherpayment_amount < (Double.parseDouble(voucher_amount))) {
		// msg += "<br>Allocated amount is greater than Party Amount!";
		// }

	}

	protected void AddFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				double tempcartamount = 0.0;
				StrSql = "SELECT cart_customer_id, cart_amount"
						+ " FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1 = 1"
						+ " AND cart_session_id = " + session_id
						+ " AND cart_vouchertype_id = " + vouchertype_id;
				CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						tempcartamount += crs.getDouble("cart_amount");
					}
				}
				crs.close();
				voucher_amount = (Double.parseDouble(voucher_amount) + tempcartamount) + "";
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher"
						+ " ("
						+ " voucher_vouchertype_id," + " voucher_tag_id,"
						+ " voucher_entry_type," + " voucher_no,"
						+ " voucher_branch_id," + " voucher_date,"
						+ " voucher_amount," + " voucher_narration,"
						+ " voucher_customer_id,"
						+ " voucher_contact_id,"
						+ " voucher_emp_id," + " voucher_ref_no,"
						+ " voucher_driver_no," + " voucher_tempo_no,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_notes,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES"
						+ " (" + vouchertype_id + ","
						+ " " + voucher_tag_id + ","
						+ " " + voucher_entry_type + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + voucher_branch_id + ","
						+ " '" + voucher_date + "',"
						+ " " + voucher_amount + ","
						+ " '" + voucher_narration + "',"
						+ " " + CNumeric(voucher_customer_id) + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_emp_id + ","
						+ "'" + voucher_ref_no + "',"
						+ "'" + voucher_driver_no + "',"
						+ "'" + voucher_tempo_no + "',"
						+ "'" + voucher_terms + "',"
						+ "'" + voucher_active + "',"
						+ "'" + voucher_notes + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " " + emp_id + ","
						+ " '" + voucher_entry_date
						+ "')";
				// SOP("INSERT===strsql===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs.close();
				AddVoucherTrans(request, response);
				// /////voucher balance
				InsertPendingPurchaseOrder(request, "");
				// delteing cart
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_vouchertype_id = 15"
						+ " AND cart_session_id = " + session_id;
				stmttx.executeUpdate(StrSql);
				conntx.commit();
				// UPDATE "+compdb(comp_id)+"the customer current balance
				// if (voucherclass_acc.equals("1")) {
				CalcCurrentBalanceThread calccurrentbalancethread =
						new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
				Thread thread = new Thread(calccurrentbalancethread);
				thread.start();
				// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				// }
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
					SOPError("connection Rollback...");
					conntx.rollback();
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

	protected void PopulateFields() {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_amount, voucher_no,"
					+ " voucher_date, voucher_branch_id, voucher_customer_id,voucher_contact_id,"
					+ " voucher_customer_id,"
					+ " CONCAT(vouchertrans_customer_id, '-', customer_ledgertype) AS payment_id,"
					+ " vouchertrans_paymode_id, vouchertrans_cheque_no, voucher_tempo_no,"
					+ " voucher_driver_no, vouchertrans_cheque_branch, vouchertrans_transaction_no,"
					+ " vouchertrans_cheque_date, vouchertrans_cheque_bank,"
					+ " COALESCE(( SELECT vouchertrans_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_customer_id = voucher_customer_id"
					+ " AND vouchertrans_voucher_id = voucher_id ),'0') AS partyamount,"
					+ " @tdsamount := COALESCE ( ( SELECT vouchertrans_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_tax = 1"
					+ " AND vouchertrans_voucher_id = voucher_id ), '0' ) AS tdsamount,"
					// + " COALESCE((@partyamount + @tdsamount),'0') AS partytotal,"
					+ " voucher_notes, voucher_emp_id, voucher_ref_no, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, voucher_authorize,voucher_authorize_id,voucher_authorize_time,"
					+ " branch_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id "
					+ " WHERE voucher_id = ?"
					// + BranchAccess.replace("branch_id", "voucher_branch_id")
					+ ExeAccess + "" + " AND vouchertrans_dc = 0";
			// SOP("PopulateFields StrSql===" + StrSqlBreaker(StrSql));
			prepmap.put(prepkey++, voucher_id);
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					voucherdate = strToShortDate(crs.getString("voucher_date"));
					voucher_branch_id = CNumeric(crs.getString("voucher_branch_id"));
					voucher_customer_id = crs.getString("voucher_customer_id");
					mainparty_amount = CNumeric((crs.getDouble("tdsamount") + crs.getDouble("partyamount")) + "");
					voucher_tds_amount = CNumeric(crs.getString("tdsamount"));
					voucher_tdsper = CNumeric(((Double.parseDouble(voucher_tds_amount) / Double.parseDouble(mainparty_amount)) * 100) + "");
					paymentid = crs.getString("payment_id");
					paymentidarr = paymentid.split("-");
					vouchertrans_customer_id = paymentidarr[0];
					voucher_contact_id = crs.getString("voucher_contact_id");
					voucher_amount = crs.getString("voucher_amount");
					vouchertrans_paymode_id = crs.getString("vouchertrans_paymode_id");
					// vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");
					// vouchertrans_chequedate = strToShortDate(crs.getString("vouchertrans_cheque_date"));
					if (!CNumeric(vouchertrans_paymode_id).equals("1")) {
						vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");
						vouchertrans_chequedate = strToShortDate(crs.getString("vouchertrans_cheque_date"));
						vouchertrans_transaction_no = crs.getString("vouchertrans_transaction_no");
					}

					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_narration = crs.getString("voucher_narration");
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_active = crs.getString("voucher_active");

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
				msg += "<br>Invalid Payment!";
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
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				double tempcartamount = 0.0;
				StrSql = "SELECT cart_customer_id, cart_amount"
						+ " from " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1"
						+ " AND cart_session_id=" + session_id
						+ " AND cart_vouchertype_id =" + vouchertype_id;
				CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						tempcartamount += crs.getDouble("cart_amount");
					}
				}
				crs.close();
				voucher_amount = (Double.parseDouble(voucher_amount) + tempcartamount) + "";

				StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher"
						+ " SET"
						+ " voucher_branch_id= " + voucher_branch_id + ","
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_date= '" + voucher_date + "',"
						+ " voucher_amount= " + voucher_amount + ","
						+ " voucher_customer_id= " + voucher_customer_id + ","
						+ " voucher_contact_id= " + voucher_contact_id + ","
						+ " voucher_ref_no= '" + voucher_ref_no + "',"
						+ " voucher_driver_no= '" + voucher_driver_no + "',"
						+ " voucher_tempo_no= '" + voucher_tempo_no + "',"
						+ " voucher_active= '" + voucher_active + "',"
						+ " voucher_narration= '" + voucher_narration + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_notes= '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow())
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql===" + StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);

				stmttx.executeUpdate("DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id);

				// debit party ledger -- trsns
				// main party
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_paymode_id,";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_cheque_date,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " vouchertrans_transaction_no,";
				}
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + ","
						+ " " + voucher_customer_id + ","
						+ " " + (Double.parseDouble(voucher_amount) - Double.parseDouble(voucher_tds_amount)) + ","
						+ " '" + 1 + "',"
						+ " " + CNumeric(vouchertrans_item_id) + ","
						+ " " + vouchertrans_paymode_id + ",";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_cheque_date + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				}

				StrSql += " '" + 0 + "'" + " )";
				// SOP("debit party ledger===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				// multi party
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount," + " vouchertrans_dc,"
						+ " vouchertrans_paymode_id,";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_cheque_date,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " vouchertrans_transaction_no,";
				}
				StrSql += " vouchertrans_reconciliation)";

				StrSql = StrSql +
						" (SELECT "
						+ "" + voucher_id + ", "
						+ " cart_customer_id,"
						+ " cart_amount,"
						+ " '1',"
						+ " " + vouchertrans_paymode_id + ",";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_cheque_date + "',";

				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				}
				StrSql += " '" + 0 + "'" + ""
						+ " FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_session_id = " + session_id + ")";
				// SOP("debit party ledger===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				// ==========this is for tds================
				if (vouchertype_tds.equals("1") && !CNumeric(voucher_tds_amount).equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ( vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_dc,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_paymode_id,";
					if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
						StrSql += " vouchertrans_cheque_no,"
								+ " vouchertrans_cheque_date,";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
						StrSql += " vouchertrans_cheque_no,"
								+ " vouchertrans_transaction_no,";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
						StrSql += " vouchertrans_transaction_no,";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
						StrSql += " vouchertrans_transaction_no,";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
						StrSql += " vouchertrans_transaction_no,";
					}
					StrSql += " vouchertrans_reconciliation)"
							+ " VALUES" + " ("
							+ " " + voucher_id + ","
							+ " " + vouchertype_tds_ledger_id + ","
							+ " 1,"
							+ " " + voucher_tds_amount + ","
							+ " '" + 1 + "',"
							+ " " + CNumeric(vouchertrans_item_id) + ","
							+ " " + vouchertrans_paymode_id + ",";
					if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
						StrSql += " '" + vouchertrans_cheque_no + "',"
								+ " '" + vouchertrans_cheque_date + "',";

					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
						StrSql += " '" + vouchertrans_cheque_no + "',"
								+ " '" + vouchertrans_transaction_no + "',";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
						StrSql += " '" + vouchertrans_transaction_no + "',";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
						StrSql += " '" + vouchertrans_transaction_no + "',";
					} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
						StrSql += " '" + vouchertrans_transaction_no + "',";
					}
					StrSql += " '" + 1 + "'" + " )";
					// SOP("TDS StrSql=====" + StrSql);
					stmttx.execute(StrSql);
				}

				vouchertrans_customer_id = paymentidarr[0];
				// / credit cash/bank ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_dc,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_paymode_id,";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_cheque_date,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " vouchertrans_transaction_no,";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " vouchertrans_transaction_no,";
				}
				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + "," + " "
						+ vouchertrans_customer_id + "," + " "
						+ CNumeric(voucher_amount) + "," + " '" + 0 + "',"
						+ " " + CNumeric(vouchertrans_item_id) + "," + " "
						+ vouchertrans_paymode_id + ",";
				if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_cheque_date + "',";

				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
					StrSql += " '" + vouchertrans_cheque_no + "',"
							+ " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
					StrSql += " '" + vouchertrans_transaction_no + "',";
				}
				StrSql += " '" + 1 + "'" + " )";
				// SOP("credit cash/bank ledger===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
				InsertPendingPurchaseOrder(request, "");
				// delteing cart
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_vouchertype_id = 15"
						+ " AND cart_session_id = " + session_id;
				stmttx.executeUpdate(StrSql);

				conntx.commit();
				// UPDATE "+compdb(comp_id)+"the customer current balance
				// if (voucherclass_acc.equals("1")) {
				CalcCurrentBalanceThread calccurrentbalancethread =
						new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
				Thread thread = new Thread(calccurrentbalancethread);
				thread.start();
				// }

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

	public void AddVoucherTrans(HttpServletRequest request, HttpServletResponse response) throws Exception {

		stmttx.executeUpdate("DELETE FROM  " + compdb(comp_id) + " axela_acc_voucher_trans"
				+ " WHERE vouchertrans_voucher_id = " + voucher_id);

		// / debit party ledger -- trsns//
		// main party
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans" + " ("
				+ " vouchertrans_voucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_dc,"
				+ " vouchertrans_item_id,"
				+ " vouchertrans_paymode_id,";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " vouchertrans_transaction_no,";
		}
		StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
				+ " " + vouchertrans_voucher_id + ","
				+ " " + voucher_customer_id + ","
				+ " " + (Double.parseDouble(voucher_amount) - Double.parseDouble(voucher_tds_amount)) + ","
				+ " '" + 1 + "',"
				+ " " + CNumeric(vouchertrans_item_id) + ","
				+ " " + vouchertrans_paymode_id + ",";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_cheque_date + "',";

		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		}
		StrSql += " '" + 0 + "'" + " )";
		// SOP("debit party ledger===11111111111===========" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql);

		// multi party
		StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
				+ " vouchertrans_voucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_option_id,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_dc,"
				+ " vouchertrans_paymode_id,";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " vouchertrans_transaction_no,";
		}

		StrSql += " vouchertrans_reconciliation)";

		StrSql = StrSql +
				" (SELECT "
				+ "" + vouchertrans_voucher_id + ", "
				+ " cart_customer_id,"
				+ " cart_option_id,"
				+ " cart_amount,"
				+ " '1',"
				+ " " + vouchertrans_paymode_id + ",";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_cheque_date + "',";

		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		}
		StrSql += " '" + 0 + "'" + ""
				+ " FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " WHERE cart_session_id = " + session_id + ")";
		// SOP("debit party ledger===22222222222=========" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql);

		// ==========this is for tds================
		if (vouchertype_tds.equals("1") && !CNumeric(voucher_tds_amount).equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " ( vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_dc,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_paymode_id,";
			if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_cheque_date,";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_transaction_no,";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " vouchertrans_transaction_no,";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
				StrSql += " vouchertrans_transaction_no,";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
				StrSql += " vouchertrans_transaction_no,";
			}
			StrSql += " vouchertrans_reconciliation)"
					+ " VALUES" + " ("
					+ " " + vouchertrans_voucher_id + ","
					+ " " + vouchertype_tds_ledger_id + ","
					+ " 1,"
					+ " " + voucher_tds_amount + ","
					+ " '" + 1 + "',"
					+ " " + CNumeric(vouchertrans_item_id) + ","
					+ " " + vouchertrans_paymode_id + ",";
			if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " '" + vouchertrans_cheque_date + "',";

			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " '" + vouchertrans_transaction_no + "',";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
			} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
			}
			StrSql += " '" + 1 + "'" + " )";
			stmttx.execute(StrSql);
		}

		vouchertrans_customer_id = paymentidarr[0];
		// / credit cash/bank ledger -- trsns//
		StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
				+ " vouchertrans_voucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_amount," + " vouchertrans_dc,"
				+ " vouchertrans_item_id,"
				+ " vouchertrans_paymode_id,";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " vouchertrans_cheque_no,"
					+ " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " vouchertrans_transaction_no,";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " vouchertrans_transaction_no,";
		}
		StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
				+ " " + vouchertrans_voucher_id + "," + " "
				+ vouchertrans_customer_id + "," + " "
				+ CNumeric(voucher_amount) + "," + " '" + 0 + "',"
				+ " " + CNumeric(vouchertrans_item_id) + "," + " "
				+ vouchertrans_paymode_id + ",";
		if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("2")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_cheque_date + "',";

		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("3")) {
			StrSql += " '" + vouchertrans_cheque_no + "',"
					+ " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("5")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("6")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		} else if (paymentidarr[1].equals("2") && vouchertrans_paymode_id.equals("4")) {
			StrSql += " '" + vouchertrans_transaction_no + "',";
		}
		StrSql += " '" + 1 + "'" + " )";
		// SOP("credit cash/bank ledger===" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql);

		// delteing cart
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " WHERE cart_vouchertype_id = 15"
				+ " AND cart_session_id = " + session_id;
		stmttx.executeUpdate(StrSql);
	}

	protected void DeleteFields(HttpServletResponse response)
			throws SQLException {
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

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.execute(StrSql);
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.execute(StrSql);
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id + " ";
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

	public String PopulatePayMode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_ledgertype, customer_name"
					+ " FROM  " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_ledgertype"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("customer_ledgertype"));
				Str.append(StrSelectdrop(crs.getString("customer_ledgertype"),
						vouchertrans_paymode_id));
				Str.append(">").append(crs.getString("customer_name"))
						.append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive(String voucher_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select Executive</option>\n");

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = "
					+ voucher_branch_id
					+ ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM  " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = ?))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			prepmap.put(prepkey++, voucher_branch_id);
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), voucher_emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name=\"dr_contact_id\" class=\"form-control\" id=\"dr_contact_id\" >");
			StrSql = "SELECT contact_id,CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = "
					+ CNumeric(customer_id)
					+ ""
					+ " GROUP BY contact_id" + " order by contact_fname";
			// SOP("StrSql==popcont== "+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			prepmap.clear();
			Str.append("<option value = 0>Select</option>");
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

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT voucherclass_file, vouchertype_name,"
					+ " vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_driver_no, vouchertype_tempo_no, vouchertype_terms,"
					+ " vouchertype_prefix, vouchertype_suffix,"
					+ " vouchertype_defaultauthorize, vouchertype_authorize,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					+ " COALESCE(vouchertype_email_sub, '') AS vouchertype_email_sub,"
					+ " COALESCE(vouchertype_email_format, '') AS vouchertype_email_format,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					+ " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					+ " vouchertype_tds, vouchertype_tds_ledger_id,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ "  " + compdb(comp_id) + "axela_comp,"
					+ "  " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN  axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ "  " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id + ""
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ "" + " WHERE admin.emp_id = " + emp_id + ""
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql-----"+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucherclass_file = crs.getString("voucherclass_file");
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_driver_no = crs.getString("vouchertype_driver_no");
				vouchertype_tempo_no = crs.getString("vouchertype_tempo_no");
				voucher_terms = crs.getString("vouchertype_terms");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				// SOP("voucher_authorize===PopulateConfigDetails===" + voucher_authorize);
				vouchertype_prefix = crs.getString("vouchertype_prefix");
				vouchertype_suffix = crs.getString("vouchertype_suffix");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_auto = crs.getString("vouchertype_email_auto");
				vouchertype_email_sub = crs.getString("vouchertype_email_sub");
				vouchertype_email_format = crs.getString("vouchertype_email_format");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
				vouchertype_sms_format = crs.getString("vouchertype_sms_format");
				vouchertype_tds = crs.getString("vouchertype_tds");
				vouchertype_tds_ledger_id = crs.getString("vouchertype_tds_ledger_id");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// //////////Insert Pending Balance
	protected void InsertPendingPurchaseOrder(HttpServletRequest request,
			String update) {
		String voucherbal_trans_id = "0", voucherbal_amount = "0", checked = "0";
		try {
			if (update.equals("yes")) {
				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id + " ";
				// stmttx.execute(StrSql);
				updateQuery(StrSql);
			}
			for (int i = 0; i <= voucher_count; i++) {
				voucherbal_trans_id = CNumeric(PadQuotes(request
						.getParameter("txt_voucherid_" + i + "")));
				voucherbal_amount = PadQuotes(request
						.getParameter("txt_amount_" + i + ""));// /PO amount
				checked = PadQuotes(request.getParameter("chk_" + i + ""));
				if (checked.equals("on") & voucher_active.equals("1")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_bal" + " ("
							+ " voucherbal_voucher_id,"
							+ " voucherbal_trans_id," + " voucherbal_amount,"
							+ " voucherbal_date)" + " VALUES" + " (" + " "
							+ voucher_id + "," + " " + voucherbal_trans_id
							+ "," + " " + CNumeric(voucherbal_amount) + ","
							+ " '" + voucher_date + "'" + " )";
					// stmttx.execute(StrSql);
					updateQuery(StrSql);
				}
				// SOP("StrSql==="+StrSqlBreaker(StrSql));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulatePayment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM  " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			Str.append("<option value='0-0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), paymentid));
				Str.append(">").append(crs.getString("customer_name"))
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

	public String PopulatePaymode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT paymode_id, paymode_name"
					+ " FROM axela_acc_paymode" + " WHERE 1=1"
					+ " GROUP BY paymode_id" + " ORDER BY paymode_id";
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);

			// Str.append("<option value='1'>Cash</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"),
						vouchertrans_paymode_id));
				Str.append(">").append(crs.getString("paymode_name"))
						.append("</option>");
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

	public void CopyPartyTransToCart(String voucher_id) {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
				+ " cart_vouchertype_id,"
				+ " cart_customer_id,"
				+ " cart_amount,"
				+ " cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_time)";
		StrSql = StrSql
				+
				" (SELECT "
				+ " 15,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_amount,"
				+ " "
				+ emp_id
				+ ","
				+ ""
				+ session_id
				+ ","
				+ " '"
				+ ToLongDate(kknow())
				+ "'"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE vouchertrans_voucher_id = "
				+ voucher_id
				+ ""
				+ " AND vouchertrans_customer_id != (SELECT voucher_customer_id "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_id = " + voucher_id + " )"
				+ " AND vouchertrans_option_id = 1"
				+ " AND vouchertrans_dc = 1)";
		// SOP("StrSql===" + StrSql);
		updateQuery(StrSql);
	}
}
