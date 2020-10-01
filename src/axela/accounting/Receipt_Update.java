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

import axela.sales.Veh_SoToSi;
import cloudify.connect.Connect;

public class Receipt_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "", checkinvoice = "";
	public String strHTML = "";
	public String msg = "";
	public String readOnly = "";
	public String QueryString = "";
	public String comp_id = "0";

	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String voucher_vouchertype_id = "0";
	public String vouchertype_suffix = "", vouchertype_prefix = "";
	public String voucher_tag_id = "0";
	public String voucher_entry_type = "0";
	public String voucher_no = "0", voucher_so_id = "0", voucher_jc_id = "0";
	public String voucher_branch_id = "0";

	public String voucher_date = "";
	public String voucher_amount = "", mainparty_amount = "0";
	public String voucher_prev_amount = "";
	public String voucher_narration = "";
	public String voucher_customer_id = "0";
	public String voucher_contact_id = "0", contact_title_id = "0";
	public String contact_fname = "", contact_lname = "";
	public String voucher_emp_id = "0";
	public String so_emp_id = "0";
	public String voucher_location_id = "0";
	public String voucher_ref_no = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_notes = "";
	public String voucher_active = "1";

	public String voucher_inactivestatus_id = "0";
	public String voucher_inactivestatus_date = "";
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_date = "";
	public String vouchertrans_id = "0";
	public String vouchertrans_voucher_id = "0";
	public String vouchertrans_customer_id = "0";
	public String vouchertrans_amount = "0";
	public String vouchertrans_customer_amount = "0";
	public String vouchertrans_dc = "";
	public String vouchertrans_item_id = "0";
	public String vouchertrans_paymode_id = "-1";
	public String payment_id = "0";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "", vouchertrans_chequedate = "";
	public String vouchertrans_cheque_bank = "", vouchertrans_transaction_no = "";
	public String vouchertrans_bank_id = "";
	public String vouchertrans_cheque_branch = "";
	public String vouchertrans_reconciliation_date = "";
	public String vouchertype_id = "0", vouchertype_name = "";
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String vouchertype_driver_no = "0";
	public String vouchertype_tempo_no = "0";
	public String vouchertype_terms = "";
	public String emp_branch_id = "0";
	public String voucherdate = "";
	public String BranchAccess = "";
	public String empEditperm = "0";
	public String ExeAccess = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "", voucherclass_acc = "0", session_id = "0";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String voucher_invoice_id = "0";
	public String customer_type = "0-0";
	public String bank_ledger_name = "";
	public String vouchertype_defaultauthorize = "0", voucher_authorize = "0";
	public String vouchertype_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String emp_formattime = "", config_customer_name = "";
	public int voucher_count = 0;
	public String voucherbal_trans_id = "0", checked = "";
	public double voucherrecpt_amount = 0.00;
	public int voucherbal_amount = 0;
	public String vouchertype_email_enable;
	public String vouchertype_email_auto;
	public String vouchertype_sms_enable;
	public String vouchertype_sms_auto;
	public String config_admin_email;
	public String config_email_enable;
	public String config_sms_enable;
	public String comp_email_enable;
	public String comp_sms_enable;
	public String contact_mobile = "";
	public String StrSqlSearch = "";
	public String StrHTML = "";
	public Ledger_Check ledger = new Ledger_Check();
	public int prepkey = 1;
	DecimalFormat deci = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;

	public String contact_email1 = "", contact_email2 = "";
	public String contact_mobile1 = "", contact_mobile2 = "", emailto = "";

	public String brandconfig_receipt_sms_enable = "", brandconfig_receipt_email_enable = "";
	public String brandconfig_receipt_email_sub = "", brandconfig_receipt_email_format = "";
	public String brandconfig_receipt_sms_format = "";
	public String emailmsg = "", emailsub = "";

	public Ledger_Check ledgercheck = new Ledger_Check();
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_receipt_access, emp_enquiry_access", request, response);
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
				checkinvoice = PadQuotes(request.getParameter("checkinvoice"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				so_emp_id = CNumeric(PadQuotes(request.getParameter("so_emp_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				voucher_invoice_id = CNumeric(PadQuotes(request.getParameter("voucher_invoice_id")));
				voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				customer_type = PadQuotes(request.getParameter("dr_voucher_payment"));
				vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));
				voucher_customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));
				voucher_so_id = CNumeric(PadQuotes(request.getParameter("voucher_so_id")));

				voucher_jc_id = CNumeric(PadQuotes(request.getParameter("voucher_jc_id")));
				empEditperm = ReturnPerm(comp_id, "emp_acc_receipt_edit", request);
				if (checkinvoice.equals("yes")) {
					voucher_invoice_id = CNumeric(PadQuotes(checkinvoice(request, response)));
				}

				PopulateConfigDetails();
				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				if (add.equals("yes")) {
					status = "Add";

					if (!addB.equals("yes")) {
						// ----Branch
						if (!voucher_so_id.equals("0")) {
							voucher_branch_id = CNumeric(PadQuotes(request.getParameter("so_branch")));
						} else {

							if (emp_branch_id.equals("0")) {
								emp_branch_id = CNumeric(GetSession("voucher_branch_id", request));
								//
								if (emp_branch_id.equals("0")) {
									emp_branch_id = getActiveBranchID(request, emp_id);
									SetSession("voucher_branch_id", emp_branch_id, request);
								}
								voucher_branch_id = emp_branch_id;
							} else {
								voucher_branch_id = emp_branch_id;
							}
						}

						if (!voucher_invoice_id.equals("0")) {
							voucher_branch_id = CNumeric(ExecuteQuery("SELECT COALESCE(voucher_branch_id,0) AS voucher_branch_id"
									+ " FROM " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_vouchertype_id = 6"
									+ " AND voucher_id = " + voucher_invoice_id));
						}
						// ----Branch

						if (!so_emp_id.equals("0") && !voucher_so_id.equals("0")) {
							voucher_emp_id = so_emp_id;
						} else {
							voucher_emp_id = emp_id;
						}

						voucher_date = ToLongDate(kknow());
						if (voucherdate.equals("")) {
							voucherdate = strToShortDate(voucher_date);
						}

					} else if (addB.equals("yes")) {
						voucher_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1")) {

							AddFields(request);

							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								voucher_no = vouchertype_prefix + voucher_no + vouchertype_suffix;

								// if (!voucher_so_id.equals("0")) {
								// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Receipt Added Successfully!"));
								// } else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?" + "voucher_id=" + voucher_id + "&voucherclass_id=" + voucherclass_id + "&vouchertype_id="
										+ vouchertype_id + "&msg=Receipt Added Successfully!"));
								// }
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Receipt")) {
						if (!voucher_id.equals("0")) {
							PopulateFields();
							if (!msg.equals("")) {
								response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg + ""));
							}
						}
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Receipt")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_receipt_edit", request).equals("1")) {
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?"
												+ "voucher_id=" + voucher_id
												+ "&voucherclass_id=" + voucherclass_id
												+ "&vouchertype_id=" + vouchertype_id
												+ "&msg=Receipt Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Receipt")) {
						response.sendRedirect(AccessDenied());
						// GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_receipt_delete", request).equals("1")) {
							// DeleteFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?" +
												"voucher_id=" + voucher_id
												+ "&voucherclass_id=" + voucherclass_id
												+ "&vouchertype_id=" + vouchertype_id
												+ "&msg=" + vouchertype_name
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (emp_role_id.equals("1") || empEditperm.equals("1")) {
			voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		} else {
			voucherdate = PadQuotes(request.getParameter("txt_hid_voucher_date"));
		}
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		voucher_customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));

		voucher_contact_id = CNumeric(PadQuotes(request.getParameter("dr_contact_id")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		voucher_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		mainparty_amount = CNumeric(PadQuotes(request.getParameter("txt_main_party_amount")));
		voucher_prev_amount = PadQuotes(request.getParameter("txt_voucher_prev_amount"));
		customer_type = PadQuotes(request.getParameter("dr_voucher_payment"));
		payment_id = customer_type.substring(customer_type.indexOf("-") + 1, customer_type.length());
		vouchertrans_customer_amount = voucher_amount;
		vouchertrans_customer_id = customer_type.substring(0, customer_type.indexOf("-"));
		vouchertrans_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_paymode")));

		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize")));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));
		voucher_no = PadQuotes(request.getParameter("hid_voucher_no"));

		PopulateConfigDetails();
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

		if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no"));
			vouchertrans_chequedate = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
			vouchertrans_cheque_branch = PadQuotes(request.getParameter("txt_vouchertrans_cheque_branch"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
			vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_card_no"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		}
		else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
			vouchertrans_transaction_no = PadQuotes(request.getParameter("txt_vouchertrans_transaction_no"));
			vouchertrans_bank_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		}
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_driver_no = PadQuotes(request.getParameter("txt_voucher_driver_no"));
		voucher_tempo_no = PadQuotes(request.getParameter("txt_voucher_tempo_no"));
		voucher_narration = PadQuotes(request.getParameter("txt_voucher_narration"));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		voucher_inactivestatus_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_inactivestatus_id")));
		if (voucher_active.equals("0")) {
			voucher_inactivestatus_date = ToLongDate(kknow());
		} else {
			voucher_inactivestatus_date = "";
			voucher_inactivestatus_id = "0";
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

	}

	public void CheckForm(HttpServletRequest request) {
		msg = "";

		bank_ledger_name = PadQuotes(ExecuteQuery("SELECT customer_name FROM " + compdb(comp_id) + "axela_customer WHERE customer_id = " + vouchertrans_customer_id));

		if (bank_ledger_name.contains("Used Cars")) {
			payment_id = "1";
			vouchertrans_paymode_id = "7";
		}
		if (voucherdate.equals("")) {
			msg += "<br>Enter Receipt Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Receipt Date must be less than or equal to Current Date!";
				}

			} else {
				msg += "<br>Enter Valid Receipt Date!";
			}
		}

		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (voucher_customer_id.equals("0") || voucher_customer_id.equals("")) {
			msg += "<br>Select Party Ledger!";
		}

		if (voucher_contact_id.equals("0")) {
			msg += "<br>Select Contact!";
		}

		if (vouchertrans_customer_id.equals("0") || payment_id.equals("0")) {
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
			msg += "<br>Select Pay Mode!";
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

			if (vouchertrans_bank_id.equals("0")) {
				msg += "<br>Select Bank Name!";
			}

			if (vouchertrans_cheque_branch.equals("")) {
				msg += "<br>Enter Branch Name!";
			}
		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
			if (vouchertrans_cheque_no.equals("")) {
				msg += "<br>Enter Card No!";
			}
			if (vouchertrans_bank_id.equals("0")) {
				msg += "<br>Select Bank Name!";
			}
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}

		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}
			if (vouchertrans_bank_id.equals("0")) {
				msg += "<br>Select Bank Name!";
			}

		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}
			if (vouchertrans_bank_id.equals("0")) {
				msg += "<br>Select Bank Name!";
			}

		} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
			if (vouchertrans_transaction_no.equals("")) {
				msg += "<br>Enter Transaction No!";
			}
			if (vouchertrans_bank_id.equals("0")) {
				msg += "<br>Select Bank Name!";
			}

		} else if (payment_id.equals("1") && vouchertrans_paymode_id.equals("7")) {
			// check condition for pay mode USED CARS
		}

		if ((customer_type.split("-")[1].equals("1") && !vouchertrans_paymode_id.equals("1"))
				|| (!customer_type.split("-")[1].equals("1") && vouchertrans_paymode_id.equals("1"))) {
			msg += "<br>Bank Ledger and Payment By are not Compatible!";
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}
		if (status.equals("Update") && (voucher_authorize.equals("1") || !CNumeric(voucher_no).equals("0"))) {
			if (!CNumeric(voucher_no).equals("0")) {
				StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE 1=1"
						+ " AND voucher_branch_id = " + voucher_branch_id
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
				voucher_no = getVoucherNo(voucher_branch_id, vouchertype_id, "0", "0");
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

			// if (!voucher_branch_id.equals("0")) {
			StrSql = "SELECT voucher_ref_no FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_branch_id = " + voucher_branch_id + " "
					+ " AND voucher_ref_no = '" + voucher_ref_no + "'"
					+ " AND voucher_id != " + voucher_id;
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Reference No. found!";
			}
			// }
		}

		if (voucher_narration.equals("")) {
			msg += "<br>Enter Narration!";
		}
		if (voucher_active.equals("0")) {
			if (voucher_inactivestatus_id.equals("0")) {
				msg += "<br> Select Status";
			}

		}
		// for (int i = 0; i < voucher_count; i++) {
		// double voucherrecpt_amount = 0, voucherbal_amount = 0;
		// String voucher_no = "";
		// checked = PadQuotes(request.getParameter("chk_" + i + ""));
		// voucher_no = PadQuotes(request.getParameter("txt_voucherno_" + i
		// + ""));
		// if (!CNumeric(
		// PadQuotes(request.getParameter("txt_amount_" + i + "")))
		// .equals("0")) {
		// voucherrecpt_amount = Double.parseDouble(PadQuotes(request
		// .getParameter("txt_amount_" + i + "")));
		// }
		// if (!CNumeric(
		// PadQuotes(request.getParameter("txt_balamount_" + i + "")))
		// .equals("0")) {
		// voucherbal_amount = Double.parseDouble(PadQuotes(request
		// .getParameter("txt_balamount_" + i + "")));
		// }
		//
		// if ((voucherbal_amount < voucherrecpt_amount)
		// && checked.equals("on")) {
		// msg += "<br>Allocated amount for Invoice No.:" + voucher_no
		// + " is greater than Receipt Amount!";
		// }
		// }

		for (int i = 0; i < voucher_count; i++) {
			checked = PadQuotes(request.getParameter("chk_" + i + ""));
			if (!CNumeric(PadQuotes(request.getParameter("txt_amount_" + i + ""))).equals("0") && checked.equals("on")) {
				voucherrecpt_amount += Double.parseDouble(PadQuotes(request.getParameter("txt_amount_" + i + "")));
			}
		}
		// if (voucherrecpt_amount > (Double.parseDouble(voucher_amount))) {
		// msg += "<br>Allocated amount is greater than Party Amount!";
		// }
		// if ((Double.parseDouble(voucher_amount) < voucherrecpt_amount)) {
		// msg += "<br>Allocated amount for Invoices is greater than Receipt Amount!";
		// / }
	}
	public void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (!voucher_so_id.equals("0")) {
					voucher_invoice_id = CNumeric(PadQuotes(ExecuteQuery("SELECT voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_so_id =" + voucher_so_id
							+ " AND voucher_vouchertype_id = 6"
							+ " AND voucher_active = 1")));
				}
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
						+ " voucher_invoice_id,"
						+ " voucher_so_id,"
						+ " voucher_jc_id,"
						+ " voucher_ref_no,"
						+ " voucher_driver_no,"
						+ " voucher_tempo_no,"
						+ " voucher_terms,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"

						+ " voucher_active,"
						+ " voucher_inactivestatus_id,"
						+ " voucher_inactivestatus_date,"
						+ " voucher_notes,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES" + " ("
						+ " " + vouchertype_id + ","
						+ " " + voucher_tag_id + ","
						+ " " + voucher_entry_type + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + voucher_branch_id + ","
						+ " '" + voucher_date + "',"
						+ " " + voucher_amount + ","
						+ " '" + voucher_narration + "',"
						+ " " + voucher_customer_id + ","
						+ " " + voucher_contact_id + ","
						+ " " + voucher_emp_id + ","
						+ " " + voucher_invoice_id + ","
						+ " " + voucher_so_id + ","
						+ " " + voucher_jc_id + ","
						+ " '" + voucher_ref_no + "',"
						+ " '" + voucher_driver_no + "',"
						+ " '" + voucher_tempo_no + "',"
						+ " '" + vouchertype_terms + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"

						+ " '" + voucher_active + "',"
						+ " '" + voucher_inactivestatus_id + "',"
						+ " '" + voucher_inactivestatus_date + "',"
						+ " '" + voucher_notes + "',"
						+ " " + emp_id + ","
						+ " '" + voucher_entry_date + "')";
				// SOP("add fields==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
					vouchertrans_voucher_id = voucher_id;
				}
				rs.close();
				vouchertrans_amount = voucher_amount;
				addReceiptItem();

				// it will add record into axela_acc_voucher_bal table for
				// balancing the sales invoice for which receipt is
				// added if so_id if there

				if (!voucher_so_id.equals("0") || !voucher_jc_id.equals("0")) {

					StrSql = "SELECT"
							+ " ("
							+ " COALESCE (voucher_amount, 0) - COALESCE (sum(voucherbal_amount), 0)"
							+ " ) AS balance"
							+ " FROM" + " " + compdb(comp_id) + "axela_acc_voucher_bal"
							+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = voucherbal_trans_id"
							+ " WHERE 1 = 1"
							+ " AND voucherbal_trans_id = " + voucher_invoice_id;

					double invoice_balance = Double.parseDouble(CNumeric(ExecuteQuery(StrSql)));
					double receipt_amount = Double.parseDouble(CNumeric(vouchertrans_amount));

					if (invoice_balance != 0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_bal"
								+ " VALUES ( "
								+ "'" + vouchertrans_voucher_id + "',"
								+ "'" + voucher_invoice_id + "',";
						if (invoice_balance <= receipt_amount) {
							StrSql += "'" + invoice_balance + "',";
						} else {
							StrSql += "'" + receipt_amount + "',";
						}

						StrSql += "'" + voucher_entry_date + "')";
						stmttx.execute(StrSql);
					}
				}

				conntx.commit();

				// customer balance
				PopulateConfigContactDetails();

				InsertPendingInvoice(request, "");
				// UPDATE "+compdb(comp_id)+"the customer current balance
				if (voucherclass_acc.equals("1") && voucher_authorize.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				if (brandconfig_receipt_email_enable.equals("1")
						&& !brandconfig_receipt_email_sub.equals("")
						&& !brandconfig_receipt_email_format.equals("")
						&& (!contact_email1.equals("") || !contact_email2.equals(""))) {
					SendEmail();
				}

				if (brandconfig_receipt_sms_enable.equals("1") && !brandconfig_receipt_sms_format.equals("")) {
					if (!contact_mobile1.equals("")) {
						SendSMS(contact_mobile1);
					}

					if (!contact_mobile2.equals("")) {
						SendSMS(contact_mobile2);
					}
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
		} else {
			// new Ledger_Check().PopulateInvoiceDetail(request, vouchertrans_customer_id, msg);
		}
	}
	public void addReceiptItem() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + vouchertrans_voucher_id;
			stmttx.executeUpdate(StrSql);
			StrSql = "";

			// / credt customer ledger -- trsns//
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans" + " ("
					+ " vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_dc,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_paymode_id,";
			if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_cheque_date,"
						+ " vouchertrans_bank_id,"
						+ " vouchertrans_cheque_branch,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_bank_id,"
						+ " vouchertrans_transaction_no,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			}
			StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
					+ " " + vouchertrans_voucher_id + ","
					+ " " + voucher_customer_id + ","
					+ " " + CNumeric(vouchertrans_amount) + ","
					+ " '" + 0 + "',"// vouchertrans_dc
					+ " " + CNumeric(vouchertrans_item_id) + "," + " "
					+ vouchertrans_paymode_id + ",";
			if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " '" + vouchertrans_cheque_date + "',"
						+ " " + vouchertrans_bank_id + ","
						+ " '" + vouchertrans_cheque_branch + "',";

			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " " + vouchertrans_bank_id + ","
						+ " '" + vouchertrans_transaction_no + "',";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			} else if (payment_id.equals("2")
					&& vouchertrans_paymode_id.equals("6")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			} else if (payment_id.equals("2")
					&& vouchertrans_paymode_id.equals("4")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			}
			StrSql += " '" + 0 + "'"// vouchertrans_reconciliation
					+ " )";
			// StrSql = CompDB(StrSql);
			// SOP("StrSql===credt customer ledger===" + StrSql);
			stmttx.execute(StrSql);
			StrSql = "";
			// voucher_customer_id = ExecuteQuery("SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
			// + " WHERE customer_ledgertype = " + payment_id);

			// / debit cash/bank ledger -- trsns//
			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
					+ " vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_dc,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_paymode_id,";
			if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_cheque_date,"
						+ " vouchertrans_bank_id,"
						+ " vouchertrans_cheque_branch,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " vouchertrans_cheque_no,"
						+ " vouchertrans_bank_id,"
						+ " vouchertrans_transaction_no,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
				StrSql += " vouchertrans_transaction_no,";
				StrSql += " vouchertrans_bank_id,";
			}
			StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
					+ " " + vouchertrans_voucher_id + ","
					+ " " + vouchertrans_customer_id + ","
					+ " " + CNumeric(vouchertrans_amount) + ","
					+ " '" + 1 + "',"// vouchertrans_dc
					+ " " + CNumeric(vouchertrans_item_id) + ","
					+ " " + vouchertrans_paymode_id + ",";
			if (payment_id.equals("2") && vouchertrans_paymode_id.equals("2")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " '" + vouchertrans_cheque_date + "',"
						+ " " + vouchertrans_bank_id + ","
						+ " '" + vouchertrans_cheque_branch + "',";

			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("3")) {
				StrSql += " '" + vouchertrans_cheque_no + "',"
						+ " " + vouchertrans_bank_id + ","
						+ " '" + vouchertrans_transaction_no + "',";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("5")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("6")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			} else if (payment_id.equals("2") && vouchertrans_paymode_id.equals("4")) {
				StrSql += " '" + vouchertrans_transaction_no + "',";
				StrSql += " " + vouchertrans_bank_id + ",";
			}
			StrSql += " '" + 1 + "'" + " )";
			// StrSql = CompDB(StrSql);
			// SOP("StrSql===debit cash/bank ledger===" + StrSql);
			stmttx.execute(StrSql);
			StrSql = "";
			// this if for GST tax1

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void PopulateFields() {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_amount, voucher_no,"
					+ " voucher_date, voucher_branch_id,voucher_customer_id, voucher_contact_id,"
					+ " vouchertrans_paymode_id, voucher_inactivestatus_id,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_branch,"
					+ " vouchertrans_cheque_date, vouchertrans_cheque_bank,"
					+ " vouchertrans_transaction_no, vouchertrans_bank_id,"
					+ " (SELECT vouchertrans_amount FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_customer_id = voucher_customer_id"
					+ " AND vouchertrans_voucher_id = voucher_id) AS partyamount,"
					+ " voucher_notes, voucher_emp_id, voucher_ref_no,"
					+ " voucher_tempo_no, voucher_driver_no,"
					+ " voucher_active, voucher_notes, voucher_authorize,voucher_authorize_id,voucher_authorize_time,"
					+ " voucher_entry_id, voucher_entry_date,"
					+ " voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, vouchertrans_customer_id,"
					+ " branch_name, customer_ledgertype"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " WHERE voucher_id = ?";
			// + BranchAccess.replace("branch_id", "voucher_branch_id")
			// + ExeAccess + "" + " AND vouchertrans_dc = 0"

			// SOP("StrSql=populate==" + StrSqlBreaker(StrSql));
			prepmap.put(prepkey++, voucher_id);
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			int count = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					voucher_date = crs.getString("voucher_date");
					voucherdate = strToShortDate(voucher_date);
					mainparty_amount = crs.getString("partyamount");
					voucher_branch_id = CNumeric(crs.getString("voucher_branch_id"));
					// branch_name = crs.getString("branch_name");
					voucher_customer_id = crs.getString("voucher_customer_id");
					voucher_contact_id = crs.getString("voucher_contact_id");
					CachedRowSet crs1 = processQuery("SELECT contact_title_id, contact_fname, contact_lname"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " WHERE contact_id = " + voucher_contact_id, 0);
					while (crs1.next()) {
						contact_title_id = crs1.getString("contact_title_id");
						contact_fname = crs1.getString("contact_fname");
						contact_lname = crs1.getString("contact_lname");
					}
					crs1.close();
					voucher_amount = crs.getString("voucher_amount");

					vouchertrans_paymode_id = crs.getString("vouchertrans_paymode_id");
					if (count == 2) {
						customer_type = crs.getString("vouchertrans_customer_id") + "-" + crs.getString("customer_ledgertype");
					}
					if (!CNumeric(vouchertrans_paymode_id).equals("1")) {
						vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");
						vouchertrans_chequedate = strToShortDate(crs.getString("vouchertrans_cheque_date"));
						vouchertrans_cheque_branch = crs.getString("vouchertrans_cheque_branch");
						vouchertrans_transaction_no = crs.getString("vouchertrans_transaction_no");
						vouchertrans_bank_id = crs.getString("vouchertrans_bank_id");
					}
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_driver_no = crs.getString("voucher_driver_no");
					voucher_tempo_no = crs.getString("voucher_tempo_no");
					voucher_narration = crs.getString("voucher_narration");
					voucher_emp_id = crs.getString("voucher_emp_id");

					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_inactivestatus_id = crs.getString("voucher_inactivestatus_id");

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

					// if (!voucher_active.equals("0")) {
					// if (count == 4) {
					// voucher_tax1_id = crs.getString("vouchertrans_customer_id");
					// } else if (count == 5) {
					// voucher_tax2_id = crs.getString("vouchertrans_customer_id");
					// }
					// }
					count++;
				}
			} else {
				msg += "<br>Invalid Reciept!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher" + " SET"
						+ " voucher_tag_id= " + voucher_tag_id + ","
						+ " voucher_entry_type= '" + voucher_entry_type + "',"
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_branch_id= " + voucher_branch_id + ","
						+ " voucher_date= '" + voucher_date + "',"
						+ " voucher_amount= " + voucher_amount + ","
						+ " voucher_customer_id= " + voucher_customer_id + ","
						+ " voucher_contact_id= " + voucher_contact_id + ","
						+ " voucher_ref_no= '" + voucher_ref_no + "',"
						+ " voucher_driver_no= '" + voucher_driver_no + "',"
						+ " voucher_tempo_no= '" + voucher_tempo_no + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_active= '" + voucher_active + "',"
						+ " voucher_inactivestatus_id= '" + voucher_inactivestatus_id + "',"
						+ " voucher_inactivestatus_date= '" + voucher_inactivestatus_date + "',"
						+ " voucher_narration= '" + voucher_narration + "',"
						+ " voucher_notes= '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE voucher_id = " + voucher_id + "";
				// SOP("StrSql=updatefields==" + StrSqlBreaker(StrSql));
				// StrSql=CompDB(StrSql);
				stmttx.executeUpdate(StrSql);
				vouchertrans_voucher_id = voucher_id;
				vouchertrans_amount = voucher_amount;

				addReceiptItem();

				// if inactive then voucher_authorize to 0

				conntx.commit();

				InsertPendingInvoice(request, "yes");

				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
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

	public void DeleteFields(HttpServletRequest request,
			HttpServletResponse response)
			throws SQLException {
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");
				// UPDATE "+compdb(comp_id)+"the customer current balance
				if (voucherclass_acc.equals("1")) {
					CalcCurrentBalanceThread calccurrentbalancethread =
							new CalcCurrentBalanceThread(voucher_id, comp_id, voucher_customer_id, vouchertype_id);
					Thread thread = new Thread(calccurrentbalancethread);
					thread.start();
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				// StrSql=CompDB(StrSql);
				stmttx.execute(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				// StrSql=CompDB(StrSql);
				stmttx.execute(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id + " ";
				// StrSql=CompDB(StrSql);
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
	public String PopulatePayment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));

			Str.append("<select name='dr_voucher_payment' id='dr_voucher_payment'"
					+ " class='form-control' onChange='Displaypaymode();'>");
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			Str.append("<option value='0-0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), customer_type));
				Str.append(">").append(crs.getString("customer_name")).append("</option>");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateInactivestatus(String voucher_inactivestatus_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT inactivestatus_id, inactivestatus_name"
					+ " FROM axela_acc_receipt_inactive_status"
					+ " WHERE 1=1"
					+ " GROUP BY inactivestatus_id" + " ORDER BY inactivestatus_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inactivestatus_id"));
				Str.append(StrSelectdrop(crs.getString("inactivestatus_id"), voucher_inactivestatus_id));
				Str.append(">").append(crs.getString("inactivestatus_name")).append("</option>");
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
		int count = 0;
		try {
			Str.append("<select name=\"dr_contact_id\" class=\"form-control\" id=\"dr_contact_id\" >");
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = "
					+ CNumeric(customer_id)
					+ ""
					+ " GROUP BY contact_id" + " order by contact_fname";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count++;
				if (count == 1) {
					Str.append("<option selected value=").append(
							crs.getString("contact_id"));
				} else {
					Str.append("<option value=").append(
							crs.getString("contact_id"));
				}
				Str.append(
						StrSelectdrop(
								crs.getString("contact_id"),
								voucher_contact_id)).append(">");
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

	public void PopulateConfigDetails() {
		try {

			StrSql = "SELECT voucherclass_acc, vouchertype_name, vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms,"
					+ " vouchertype_prefix, vouchertype_suffix, "
					+ " vouchertype_driver_no, vouchertype_tempo_no,"
					+ " vouchertype_defaultauthorize, vouchertype_authorize,"
					+ " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					+ " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					+ " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					+ " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " comp_email_enable, comp_sms_enable"
					+ " FROM " + compdb(comp_id) + "axela_config,"
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ " " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";

			// SOP("StrSql=config details==" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_prefix = crs.getString("vouchertype_prefix");
				vouchertype_suffix = crs.getString("vouchertype_suffix");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_email_enable = crs.getString("vouchertype_email_enable");
				vouchertype_email_auto = crs.getString("vouchertype_email_auto");
				vouchertype_sms_enable = crs.getString("vouchertype_sms_enable");
				vouchertype_sms_auto = crs.getString("vouchertype_sms_auto");
				vouchertype_driver_no = crs.getString("vouchertype_driver_no");
				vouchertype_tempo_no = crs.getString("vouchertype_tempo_no");
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
	// for sms
	public void PopulateConfigContactDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
					+ " COALESCE(brandconfig_receipt_email_enable, '') AS brandconfig_receipt_email_enable,"
					+ " COALESCE(brandconfig_receipt_email_sub, '') AS brandconfig_receipt_email_sub,"
					+ " COALESCE(brandconfig_receipt_email_format, '') AS brandconfig_receipt_email_format,"
					+ " COALESCE(brandconfig_receipt_sms_enable, '') AS brandconfig_receipt_sms_enable,"
					+ " COALESCE(brandconfig_receipt_sms_format, '') AS brandconfig_receipt_sms_format,"
					+ " branch_email1, branch_mobile1"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = voucher_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " INNER JOIN axela_brand on brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config on brandconfig_brand_id = brand_id,"
					+ compdb(comp_id) + "axela_config, "
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE"
					+ " voucher_id = " + voucher_id + "";

			// SOP("StrSql=config details------------------" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_email1 = PadQuotes(crs.getString("contact_email1"));
				contact_email2 = PadQuotes(crs.getString("contact_email2"));
				contact_mobile1 = PadQuotes(crs.getString("contact_mobile1"));
				contact_mobile2 = PadQuotes(crs.getString("contact_mobile2"));

				if (!contact_email1.equals(""))
					emailto += contact_email1;
				if (!contact_email2.equals(""))
					emailto += "," + contact_email2;

				brandconfig_receipt_email_enable = PadQuotes(crs.getString("brandconfig_receipt_email_enable"));
				brandconfig_receipt_sms_enable = PadQuotes(crs.getString("brandconfig_receipt_sms_enable"));
				brandconfig_receipt_email_sub = PadQuotes(crs.getString("brandconfig_receipt_email_sub"));
				brandconfig_receipt_email_format = PadQuotes(crs.getString("brandconfig_receipt_email_format"));
				brandconfig_receipt_sms_format = PadQuotes(crs.getString("brandconfig_receipt_sms_format"));

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// //////////Insert Pending Balance
	public void InsertPendingInvoice(HttpServletRequest request, String update) {
		String voucherbal_trans_id = "0", voucherbal_amount = "0", checked = "0";
		try {
			if (update.equals("yes")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id + " ";
				// SOP("StrSql=delete=insertpendinginv===" + StrSql);
				updateQuery(StrSql);
			}
			// SOP("voucher_count =111----- " + voucher_count);
			for (int i = 0; i <= voucher_count; i++) {
				voucherbal_trans_id = CNumeric(PadQuotes(request
						.getParameter("txt_voucherid_" + i + "")));
				voucherbal_amount = PadQuotes(request
						.getParameter("txt_amount_" + i + ""));// /receipt
																// amount
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
				// SOP("StrSql==bal==="+StrSqlBreaker(StrSql));
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
				Str.append(StrSelectdrop(crs.getString("title_id"),
						contact_title_id));
				Str.append(">").append(crs.getString("title_desc"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			Str.append("<select name='dr_voucher_paymode' id='dr_voucher_paymode'"
					+ " class='form-control' onChange='Displaypaymode(1)'>");
			// Str.append("<option value='1'>Cash</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"), vouchertrans_paymode_id));
				Str.append(">").append(crs.getString("paymode_name")).append("</option>");
			}
			Str.append("</select>");
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// Before adding Receipt it will check for the sails-invoice of that sails-order
	public String checkinvoice(HttpServletRequest request, HttpServletResponse response) {
		String invoice_id = "0";
		try {
			StrSql = "SELECT"
					+ " voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1 = 1 AND voucher_so_id = " + voucher_so_id
					+ " AND voucher_vouchertype_id = '6'";
			// SOP("checkinvoice====" + StrSqlBreaker(StrSql));
			invoice_id = CNumeric(ExecuteQuery(StrSql));
			if (invoice_id.equals("0")) {

				Veh_SoToSi sotosi = new Veh_SoToSi();

				sotosi.so_id = voucher_so_id;
				sotosi.addSingle = "yes";
				sotosi.allinvoice = "Add Invoive";
				sotosi.Listdata(comp_id, request, response);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return invoice_id;
	}

	public void SendEmail() {
		emailmsg = brandconfig_receipt_email_format;
		emailsub = brandconfig_receipt_email_sub;

		emailmsg = "replace('" + emailmsg + "','[RECEIPTID]', voucher_id)";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		emailmsg = "replace(" + emailmsg + ",'[INVOICEID]', voucher_invoice_id)";
		emailmsg = "replace(" + emailmsg + ",'[SOID]', COALESCE(so_id,''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]', COALESCE(model_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHER]', vouchertype_name)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]', customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		emailmsg = "replace(" + emailmsg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		emailmsg = "replace(" + emailmsg + ",'[AMOUNT]', voucher_amount)";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		emailsub = "replace('" + emailsub + "','[RECEIPTID]', voucher_id)";
		emailsub = "replace(" + emailsub + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		emailsub = "replace(" + emailsub + ",'[INVOICEID]', voucher_invoice_id)";
		emailsub = "replace(" + emailsub + ",'[SOID]', COALESCE(so_id,''))";
		emailsub = "replace(" + emailsub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		emailsub = "replace(" + emailsub + ",'[MODELNAME]', COALESCE(model_name,''))";
		emailsub = "replace(" + emailsub + ",'[VOUCHER]', vouchertype_name)";
		emailsub = "replace(" + emailsub + ",'[CUSTOMERID]', customer_id)";
		emailsub = "replace(" + emailsub + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		emailsub = "replace(" + emailsub + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		emailsub = "replace(" + emailsub + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		emailsub = "replace(" + emailsub + ",'[AMOUNT]', voucher_amount)";
		emailsub = "replace(" + emailsub + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		emailsub = "replace(" + emailsub + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		emailsub = "replace(" + emailsub + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		emailsub = "replace(" + emailsub + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		emailsub = "replace(" + emailsub + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		emailsub = "replace(" + emailsub + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		emailsub = "replace(" + emailsub + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		try {
			StrSql = "SELECT"
					+ " branch_id,"
					+ " voucher_contact_id,"
					+ " emp_id,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " branch_email1,"
					+ " '" + emailto + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " " + ToLongDate(kknow()) + ","
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE voucher_id = " + voucher_id + ""
					+ " GROUP BY voucher_id";

			// SOP("StrSql--Email-----1-----" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_emp_id, "
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql---Email----2-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void SendSMS(String tomobile_no) {
		String smsmsg = (brandconfig_receipt_sms_format);

		smsmsg = "replace('" + smsmsg + "','[RECEIPTID]', voucher_id)";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHERNO]', CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix))";
		smsmsg = "replace(" + smsmsg + ",'[INVOICEID]', voucher_invoice_id)";
		smsmsg = "replace(" + smsmsg + ",'[SOID]', COALESCE(so_id,''))";
		smsmsg = "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'),''))";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]', COALESCE(model_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHER]', vouchertype_name)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]', customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]', COALESCE(customer_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]', COALESCE(CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]', COALESCE(contact_jobtitle,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]', COALESCE(contact_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE2]', COALESCE(contact_mobile2,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]', COALESCE(contact_email1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL2]', COALESCE(contact_email2,''))";
		smsmsg = "replace(" + smsmsg + ",'[VOUCHERDATE]', COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),''))";
		smsmsg = "replace(" + smsmsg + ",'[AMOUNT]', voucher_amount)";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]', COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]', COALESCE(emp_phone1,''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]', COALESCE(emp_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]', COALESCE(emp_email1,''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCH]', COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHPHONE1]', COALESCE(branch_phone1,''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1,''))";

		StrSql = "SELECT"
				+ " branch_id,"
				+ " voucher_contact_id,"
				+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
				+ " emp_name,"
				+ " '" + tomobile_no + "',"
				+ " " + smsmsg + ","
				+ " " + ToLongDate(kknow()) + ","
				+ " 0,"
				+ " " + emp_id + ""
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE voucher_id = " + voucher_id + ""
				+ " GROUP BY voucher_id";

		// SOP("StrSql====----sms------------" + StrSql);

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
				+ " (sms_branch_id,"
				+ " sms_contact_id,"
				+ " sms_contact,"
				+ " sms_emp,"
				+ " sms_mobileno,"
				+ " sms_msg,"
				+ " sms_date,"
				+ " sms_sent,"
				+ " sms_entry_id)"
				+ " " + StrSql + "";

		// SOP("StrSql====----sms-insert-----------" + StrSql);
		updateQuery(StrSql);

	}

	public String PopulateTax(String comp_id, String price_tax_id) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id, customer_rate,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE 1 = 1"
					+ " AND customer_taxtype_id IN (3, 4, 5)"
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1";
			if (!StrSqlSearch.equals("")) {
				StrSql += StrSqlSearch;
			}
			StrSql += " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql===PopulateTax===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<select name=\"dr_voucher_tax1\" id=\"dr_voucher_tax1\" class=\"form-control\">");
			Str.append("<option value='0'> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id") + "_" + crs.getString("customer_rate"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTax2(String comp_id, String price_tax_id) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id, customer_rate,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE 1 = 1"
					+ " AND customer_taxtype_id IN (3, 4, 5)"
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1";
			if (!StrSqlSearch.equals("")) {
				StrSql += StrSqlSearch;
			}
			StrSql += " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql===PopulateTax2===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<select name=\"dr_voucher_tax2\" id=\"dr_voucher_tax2\" class=\"form-control\">");
			Str.append("<option value='0'> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id") + "_" + crs.getString("customer_rate"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			Str.append("</select>");
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
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT emp_name, emp_ref_no, emp_id "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE emp_active = '1'"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql-all-" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), voucher_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFinanceBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT"
					+ " fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY"
					+ " fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id"));
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), vouchertrans_bank_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
