package axela.accounting;

// JEET 12 DEC 2014

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Contra_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String QueryString = "";
	public String comp_id = "0";

	public int prepkey = 1;
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	// variablesConvertShortDateToStr(
	public String voucher_id = "0";
	public String voucher_branch_id = "0";
	public String voucher_no = "0";
	public String voucherclass_id = "19";
	public String voucher_date = "";
	public String voucherdate = "";
	public String voucher_amount = "";
	public String voucher_narration = "";
	public String vouchertrans_reconciliation = "";
	public String comp_module_accounting = "0";
	public String voucher_emp_id = "0";
	public String voucher_ref_no = "";
	public String voucher_notes = "";
	public String voucher_active = "1";
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_date = "";
	public String vouchertrans_id = "0";
	public String vouchertrans_voucher_id = "0";
	public String vouchertrans_customer_from_id = "0";
	public String vouchertrans_customer_to_id = "0";
	public String vouchertrans_amount = "0";
	// public String vouchertrans_dc = "";
	public String vouchertrans_paymode_id = "-1";
	public String vouchertrans_paymode_from = "";
	public String vouchertrans_paymode_fromarr[];
	public String vouchertrans_paymode_from_id = "-1";
	public String vouchertrans_paymode_from_idtype = "0";
	public String vouchertrans_paymode_to = "";
	public String vouchertrans_paymode_toarr[];
	public String vouchertrans_paymode_to_id = "-1";
	public String vouchertrans_paymode_to_idtype = "0";
	public String vouchertrans_cheque_no = "";
	public String vouchertrans_cheque_date = "", vouchertrans_chequedate = "";
	public String vouchertype_id = "0", vouchertype_name = "",
			voucherclass_acc = "0";
	public String vouchertype_defaultauthorize = "0", voucher_authorize = "0";
	public String vouchertype_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String vouchertype_terms = "";
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	// session_id = "";

	public String emp_id = "0";
	public String emp_branch_id = "0";
	public String emp_role_id = "0";

	public String config_customer_name = "";
	public String BranchAccess = "";
	public String empEditperm = "";
	public String ExeAccess = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	// public String branch_id;
	public String rateclass_id;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_contra_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				voucher_branch_id = emp_branch_id;
				config_customer_name = GetSession("config_customer_name", request);
				// // For Generating session each time
				// session_id = PadQuotes(request.getParameter("txt_session_id"));
				// if (session_id.equals("")) {
				// String key = "", possible = "0123456789";
				// for (int i = 0; i < 9; i++) {
				// key += possible.charAt((int) Math.floor(Math.random()
				// * possible.length()));
				// }
				// session_id = key;
				// }

				comp_module_accounting = CNumeric(GetSession("comp_module_accounting", request));
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
				empEditperm = ReturnPerm(comp_id, "emp_voucher_edit", request);
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));

				PopulateConfigDetails();
				if (add.equals("yes")) {
					status = "Add";
					// ----Branch
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
					// ----Branch

					if (!addB.equals("yes")) {
						voucher_emp_id = emp_id;

						voucherdate = DateToShortDate(kknow());
						voucher_date = ConvertShortDateToStr(voucherdate);
						vouchertrans_chequedate = DateToShortDate(kknow());
						// SOP("vouchertrans_chequedate==" + vouchertrans_chequedate);

					} else if (addB.equals("yes")) {
						voucher_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_contra_add", request).equals("1")) {
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?"
												+ "voucher_id=" + voucher_id
												+ "&voucherclass_id="
												+ voucherclass_id
												+ "&vouchertype_id="
												+ vouchertype_id
												+ "&msg=Contra Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";

					if (!updateB.equals("yes") && !deleteB.equals("Delete Contra")) {
						if (!voucher_id.equals("0")) {
							PopulateFields();
							if (!msg.equals("")) {
								response.sendRedirect(response
										.encodeRedirectURL("../portal/error.jsp?msg="
												+ msg + ""));
							}
						}
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Contra")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_contra_edit", request).equals(
								"1")) {
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?"
												+ "voucher_id="
												+ voucher_id
												+ "&voucherclass_id="
												+ voucherclass_id
												+ "&vouchertype_id="
												+ vouchertype_id
												+ "&msg=Contra Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Contra")) {
						response.sendRedirect(AccessDenied());
						// GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_acc_contra_delete", request)
								.equals("1")) {
							// DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?"
												+ "voucher_id=" + voucher_id
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
		voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_branch_id")));
		vouchertrans_paymode_from = PadQuotes(request.getParameter("dr_vouchertrans_paymode_from_id"));
		vouchertrans_paymode_fromarr = vouchertrans_paymode_from.split("-");
		vouchertrans_paymode_from_id = vouchertrans_paymode_fromarr[0];
		vouchertrans_paymode_from_idtype = vouchertrans_paymode_fromarr[1];
		vouchertrans_cheque_no = PadQuotes(request.getParameter("txt_vouchertrans_cheque_no"));
		vouchertrans_chequedate = PadQuotes(request.getParameter("txt_vouchertrans_cheque_date"));
		vouchertrans_cheque_date = ConvertShortDateToStr(vouchertrans_chequedate);
		vouchertrans_paymode_to = PadQuotes(request.getParameter("dr_vouchertrans_paymode_to_id"));
		vouchertrans_paymode_toarr = vouchertrans_paymode_to.split("-");
		vouchertrans_paymode_to_id = vouchertrans_paymode_toarr[0];
		vouchertrans_paymode_to_idtype = vouchertrans_paymode_toarr[1];
		voucher_amount = PadQuotes(request.getParameter("txt_voucher_amount"));
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_narration = PadQuotes(request.getParameter("txt_voucher_narration"));
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("ch_voucher_active")));
		voucher_notes = PadQuotes(request.getParameter("txt_voucher_notes"));
		if (voucher_notes.length() > 5000) {
			voucher_notes = voucher_notes.substring(0, 4999);
		}
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		voucher_authorize = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize")));
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

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		vouchertrans_amount = voucher_amount;
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (voucherdate.equals("")) {
			msg += "<br>Enter Date!";
		} else {
			if (isValidDateFormatShort(voucherdate)) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				if (Long.parseLong(voucher_date) > Long
						.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Contra Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter Valid Date!";
			}
		}
		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		if (vouchertrans_paymode_from_id.equals("0")) {
			msg += "<br>Select From!";
		}

		if (vouchertrans_paymode_to_id.equals("0")) {
			msg += "<br>Select To!";
		}
		if (!vouchertrans_paymode_from_id.equals("0")
				&& !vouchertrans_paymode_to_id.equals("0")) {
			if (vouchertrans_paymode_from_id.equals(vouchertrans_paymode_to_id)) {
				msg += "<br>From and To are similer!";
			}

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

		if (vouchertrans_paymode_from_idtype.equals("2")
				&& vouchertrans_paymode_to_idtype.equals("1")) {

			if (vouchertrans_cheque_no.equals("")) {
				msg += "<br>Enter Cheque No!";
			} else if (vouchertrans_cheque_no.length() < 6) {
				msg += "<br>Cheque No. con't be less than 6 digits!";
			}
			if (vouchertrans_chequedate.equals("")) {
				msg += "<br>Enter Cheque Date!";
			}
			if (isValidDateFormatShort(vouchertrans_chequedate)) {
				vouchertrans_cheque_date = ConvertShortDateToStr(vouchertrans_chequedate);
			} else {
				msg = msg + "<br>Enter Cheque Valid Date!";
			}
		}
		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}
		if (voucher_ref_no.equals("")) {
			msg += "<br>Enter Reference No.!";
		} else {
			if (voucher_ref_no.length() < 2) {
				msg += "<br>Contra Reference No. should be atleast Two Digit(s)!";
			}
		}
		if (voucher_narration.equals("")) {
			msg += "<br>Enter Narration!";
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				vouchertrans_customer_from_id = vouchertrans_paymode_from_id;

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher" + " ("
						+ " voucher_vouchertype_id,"
						+ " voucher_no,"
						+ " voucher_branch_id,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_customer_id,"
						+ " voucher_narration,"
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
						+ " VALUES" + " ("
						+ vouchertype_id + ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + voucher_branch_id + ","
						+ " '" + voucher_date + "',"
						+ " " + voucher_amount + ","
						+ " " + vouchertrans_customer_from_id + ","
						+ " '" + voucher_narration + "',"
						+ " " + voucher_emp_id + ","
						+ " '" + voucher_ref_no + "',"
						+ " '" + vouchertype_terms + "',"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_notes + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " " + emp_id + ","
						+ " '" + voucher_entry_date + "')";
				// SOP("StrSql==add==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
				}
				rs.close();
				// / credt customer ledger -- trsns//

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time," + " vouchertrans_dc,"
						+ " vouchertrans_paymode_id,";
				if (vouchertrans_paymode_from_idtype.equals("2")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_cheque_date,";
				}

				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + "," + " "
						+ vouchertrans_customer_from_id + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ ToLongDate(kknow()) + "'," + " '" + 0 + "'," + " "
						+ vouchertrans_paymode_from_idtype + ",";
				if (vouchertrans_paymode_from_idtype.equals("2")) {
					StrSql += " '" + vouchertrans_cheque_no + "'," + " '"
							+ vouchertrans_cheque_date + "',";
				}
				StrSql += " '" + 0 + "'" + " )";
				// SOP("StrSql==cust====" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				vouchertrans_customer_to_id = vouchertrans_paymode_to_id;

				vouchertrans_reconciliation = ExecuteQuery("SELECT customer_reconciliation FROM  " + compdb(comp_id) + "axela_customer"
						+ " WHERE customer_id = " + vouchertrans_customer_to_id);

				// / debit cash/bank ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time," + " vouchertrans_dc,"
						+ " vouchertrans_paymode_id,";
				StrSql += " vouchertrans_reconciliation )" + " VALUES" + " ("
						+ " " + voucher_id + "," + " "
						+ vouchertrans_customer_to_id + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ ToLongDate(kknow()) + "'," + " '" + 1 + "'," + " "
						+ vouchertrans_paymode_to_idtype + ",";
				StrSql += " '" + vouchertrans_reconciliation + "'" + " )";
				// SOP("StrSql===" + StrSqlBreaker(StrSql));

				// SOP("StrSql==ban/cash====" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
				conntx.commit();

				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
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

	protected void PopulateFields() {
		try {
			StrSql = "SELECT COALESCE(voucher_id, 0) AS voucher_id, voucher_amount, voucher_no,"
					+ " voucher_date, voucher_branch_id, vouchertrans_customer_id, customer_ledgertype,  vouchertrans_dc,"
					+ " vouchertrans_paymode_id, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date,"
					+ " voucher_emp_id, voucher_ref_no, voucher_active, voucher_notes,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id, voucher_modified_date,"
					+ " voucher_narration, voucher_authorize,voucher_authorize_id,voucher_authorize_time"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " WHERE voucher_id = ?";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			prepmap.put(prepkey++, Integer.parseInt(voucher_id));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			int count = 0;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("voucher_id");
					voucher_no = crs.getString("voucher_no");
					voucher_amount = crs.getString("voucher_amount");
					voucher_date = crs.getString("voucher_date");
					voucherdate = strToShortDate(voucher_date);
					voucher_branch_id = crs.getString("voucher_branch_id");
					prepmap.put(prepkey, Integer.parseInt(voucher_id));
					vouchertrans_paymode_id = crs.getString("vouchertrans_paymode_id");
					voucher_authorize = CNumeric(crs.getString("voucher_authorize"));
					if (crs.getString("vouchertrans_dc").equals("0")) {
						vouchertrans_customer_from_id = crs.getString("vouchertrans_customer_id");

						prepmap.clear();
						prepkey = 1;
						vouchertrans_paymode_from = vouchertrans_customer_from_id + "-" + crs.getString("customer_ledgertype");
					} else if (crs.getString("vouchertrans_dc").equals("1")) {
						vouchertrans_customer_to_id = crs.getString("vouchertrans_customer_id");

						prepmap.clear();
						prepkey = 1;
						vouchertrans_paymode_to = vouchertrans_customer_to_id + "-" + crs.getString("customer_ledgertype");
					}

					if (crs.getString("vouchertrans_dc").equals("0")
							&& crs.getString("vouchertrans_paymode_id").equals("2")) {
						vouchertrans_paymode_from_idtype = vouchertrans_paymode_id;
						vouchertrans_cheque_no = crs.getString("vouchertrans_cheque_no");
						vouchertrans_cheque_date = crs.getString("vouchertrans_cheque_date");
						vouchertrans_chequedate = strToShortDate(vouchertrans_cheque_date);
					} else if (crs.getString("vouchertrans_dc").equals("0")
							&& crs.getString("vouchertrans_paymode_id").equals("1")) {
						vouchertrans_paymode_from_idtype = vouchertrans_paymode_id;
					} else if (crs.getString("vouchertrans_dc").equals("1")) {
						vouchertrans_paymode_to_idtype = vouchertrans_paymode_id;
					}

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

					count++;
				}
			} else {
				msg += "<br>Invalid Contra!";
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
		ResultSet rs;
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				vouchertrans_customer_from_id = vouchertrans_paymode_from_id;

				StrSql = "UPDATE  " + compdb(comp_id) + "  axela_acc_voucher"
						+ " SET"
						+ " voucher_date = '" + voucher_date + "',"
						+ " voucher_branch_id = " + voucher_branch_id + ","
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_amount = " + voucher_amount + ","
						+ " voucher_customer_id = '" + vouchertrans_customer_from_id + "',"
						+ " voucher_narration = '" + voucher_narration + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_emp_id = '" + voucher_emp_id + "',"
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_notes = '" + voucher_notes + "',"
						+ " voucher_modified_id = " + emp_id + ","
						+ " voucher_modified_date = " + ToLongDate(kknow())
						+ "" + " WHERE voucher_id = " + voucher_id + "";

				// SOP("StrSql===" + StrSqlBreaker(StrSql));
				stmttx.executeUpdate(StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
				}
				rs.close();

				stmttx.executeUpdate("DELETE FROM  " + compdb(comp_id) + " axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id);

				// / credt customer ledger -- trsns//

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time," + " vouchertrans_dc,"
						+ " vouchertrans_paymode_id,";
				if (vouchertrans_paymode_from_idtype.equals("2")) {
					StrSql += " vouchertrans_cheque_no,"
							+ " vouchertrans_cheque_date,";
				}

				StrSql += " vouchertrans_reconciliation)" + " VALUES" + " ("
						+ " " + voucher_id + "," + " "
						+ vouchertrans_customer_from_id + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ ToLongDate(kknow()) + "'," + " '" + 0 + "'," + " "
						+ vouchertrans_paymode_from_idtype + ",";
				if (vouchertrans_paymode_from_idtype.equals("2")) {
					StrSql += " '" + vouchertrans_cheque_no + "'," + " '"
							+ vouchertrans_cheque_date + "',";
				}
				StrSql += " '" + 0 + "'" + " )";

				// SOP("StrSql===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				vouchertrans_customer_to_id = vouchertrans_paymode_to_id;

				// debit cash/bank ledger -- trsns//
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans" + " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time," + " vouchertrans_dc,"
						+ " vouchertrans_paymode_id,";
				StrSql += " vouchertrans_reconciliation )" + " VALUES" + " ("
						+ " " + voucher_id + "," + " "
						+ vouchertrans_customer_to_id + "," + " "
						+ CNumeric(vouchertrans_amount) + "," + " '"
						+ ToLongDate(kknow()) + "'," + " '" + 1 + "'," + " "
						+ vouchertrans_paymode_to_idtype + ",";
				StrSql += " '" + 1 + "'" + " )";
				// SOP("StrSql===" + StrSqlBreaker(StrSql));

				stmttx.execute(StrSql);

				conntx.commit();

				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
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

	public String PopulateFrom() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM  " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			// SOP("StrSql=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"),
						vouchertrans_paymode_from));
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

	public String PopulateTo() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(customer_id,'-',customer_ledgertype) AS customer_id, customer_name"
					+ " FROM  " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			// SOP("StrSql=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"),
						vouchertrans_paymode_to));
				Str.append(">").append(crs.getString("customer_name"))
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

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT "
					+ " voucherclass_file, voucherclass_acc, "
					+ " vouchertype_name, "
					+ " vouchertype_defaultauthorize, vouchertype_authorize,"
					// + " vouchertype_mobile, "
					// + " vouchertype_email, vouchertype_dob, vouchertype_dnd,"
					// +
					// " vouchertype_affects_inventory, config_inventory_current_stock,"
					+ " vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms"
					// +
					// " COALESCE(vouchertype_email_enable, '') AS vouchertype_email_enable,"
					// +
					// " COALESCE(vouchertype_email_auto, '') AS vouchertype_email_auto,"
					// +
					// " COALESCE(vouchertype_email_sub, '') AS vouchertype_email_sub,"
					// +
					// " COALESCE(vouchertype_email_format, '') AS vouchertype_email_format,"
					// +
					// " COALESCE(vouchertype_sms_enable, '') AS vouchertype_sms_enable,"
					// +
					// " COALESCE(vouchertype_sms_auto, '') AS vouchertype_sms_auto,"
					// +
					// " COALESCE(vouchertype_sms_format, '') AS vouchertype_sms_format,"
					// +
					// " config_admin_email, config_email_enable, config_sms_enable,"
					// + " comp_email_enable, comp_sms_enable,"
					// + " vouchertype_billing_add, vouchertype_consignee_add,"
					// +
					// " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount, vouchertype_turnoverdisc"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ "  " + compdb(comp_id) + "axela_comp,"
					+ "  " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN  axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ "  " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id
					+ "" + " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = "
					+ emp_id + "" + " WHERE admin.emp_id = " + emp_id + ""
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=congif==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_terms = crs.getString("vouchertype_terms");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				// voucher_authorize = vouchertype_defaultauthorize;
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
