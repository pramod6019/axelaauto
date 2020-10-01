package axela.accounting;

// shivaprasad 8 oct 2014

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

public class Journal_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "";

	public String voucher_id = "0", entity_id = "0";
	public String emp_role_id = "0";
	public String voucher_active = "";
	public String voucher_entry_id = "0", voucher_entry_date = "";
	public String voucher_modified_id = "0", voucher_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "";
	public String modified_date = "", session_id = "";
	public String emp_id = "0", emp_branch_id = "0", voucherclass_acc = "0";

	public String BranchAccess = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String voucher_branch_id = "0";
	public String branch_name = "";
	public String voucherdate = "";
	public String comp_module_accounting = "0";

	public String vouchertype_defaultauthorize = "0", voucher_authorize = "0";
	public String vouchertype_authorize = "0";
	public String voucher_authorize_id = "0";
	public String voucher_authorize_time = "";

	public String voucher_no = "0";
	public String voucher_customer_id = "0";
	public String voucher_date = "";
	public String voucher_amount = "0.00";
	public String voucher_emp_id = "0";
	public String voucherclass_id = "18";
	public String voucher_vouchertype_id = "0";
	public String vouchertype_id = "0", vouchertype_name = "", vouchertype_terms = "";
	public String vouchertrans_customer_id;
	public String vouchertrans_dc = "";
	public String vouchertrans_amount_c = "0";
	public String vouchertrans_amount_d = "0";
	public String vouchertrans_reconciliation = "0";
	public String voucher_narration = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String vouchertrans_amount = "";
	public String config_customer_name = "";
	public String ExeAccess = "";
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	public int prepkey = 1;
	public double currentbal_customer_id = 0.00;

	public Connection conntx = null;
	public Statement stmttx = null;
	public String vouchertype_ref_no_enable = "0";
	public String vouchertype_ref_no_mandatory = "0";
	public String voucher_ref_no = "";
	public String voucher_so_id = "0";

	int row_count = 0;

	public Ledger_Check ledger = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_journal_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				voucher_emp_id = emp_id;
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				voucher_branch_id = CNumeric(emp_branch_id);

				config_customer_name = GetSession("config_customer_name", request);
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
				PopulateConfigDetails();
				if (vouchertype_name.equals("")) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Voucher!");
				}
				QueryString = PadQuotes(request.getQueryString());
				session_id = PadQuotes(request.getParameter("txt_session_id"));
				if (session_id.equals("")) {
					String key = "", possible = "0123456789";
					for (int i = 0; i < 9; i++) {
						key += possible.charAt((int) Math.floor(Math.random()
								* possible.length()));
					}
					session_id = key;
				}
				if (add.equals("yes")) {
					status = "Add";
					voucher_active = "1";
					voucherdate = strToShortDate(ToShortDate(kknow()));
					// ----Branch
					if (!CNumeric(PadQuotes(request.getParameter("so_branch_id"))).equals("0")) {
						voucher_so_id = CNumeric(PadQuotes(request.getParameter("voucher_so_id")));
						voucher_branch_id = CNumeric(PadQuotes(request.getParameter("so_branch_id")));
					} else if (emp_branch_id.equals("0")) {
						emp_branch_id = CNumeric(GetSession("voucher_branch_id", request));

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
						if (ReturnPerm(comp_id, "emp_acc_journal_add", request).equals("1")) {
							voucher_entry_id = emp_id;
							voucher_entry_date = ToLongDate(kknow());
							GetValues(request, response);
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucher_id=" + voucher_id
										+ "&vouchertype_id=" + vouchertype_id
										+ "&msg=" + vouchertype_name
										+ " added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else {
					if (update.equals("yes")) {
						status = "Update";
						if (!updateB.equals("yes") && !deleteB.equals("Delete Journal")) {
							// CopyLedgerTransToCart(voucher_id);
							PopulateFields(response);
						} else if (updateB.equals("yes")
								&& !deleteB.equals("Delete Journal")) {
							if (ReturnPerm(comp_id, "emp_acc_journal_edit", request).equals("1")) {
								voucher_modified_id = emp_id;
								voucher_modified_date = ToLongDate(kknow());
								GetValues(request, response);
								UpdateFields(request, response);
								if (!msg.equals("")) {
									msg = "Error! " + msg;
								} else {
									msg = vouchertype_name + " updated successfully!";
									response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucher_id=" + voucher_id
											+ "&vouchertype_id=" + vouchertype_id
											+ "&msg=" + msg));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else if (deleteB.equals("Delete Journal")) {
							if (AppRun().equals("1")) {
								response.sendRedirect(AccessDenied());
							} else {
								GetValues(request, response);
							}

							if (ReturnPerm(comp_id, "emp_acc_journal_delete", request).equals("1")) {
								if (AppRun().equals("0")) {
									DeleteFields(request, response);
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("voucher-list.jsp?voucherclass_id=" + voucherclass_id
											+ "&vouchertype_id=" + voucherclass_id
											+ "&msg=" + vouchertype_name
											+ " deleted successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		voucher_branch_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_branch_id")));
		voucherdate = PadQuotes(request.getParameter("txt_voucher_date"));
		voucher_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		voucher_ref_no = PadQuotes(request.getParameter("txt_voucher_ref_no"));
		voucher_narration = request.getParameter("txt_voucher_narration");
		voucher_active = CheckBoxValue(PadQuotes(request.getParameter("chk_voucher_active")));
		vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_defaultauthorize")));
		vouchertype_authorize = CNumeric(PadQuotes(request.getParameter("txt_vouchertype_authorize")));
		voucher_authorize_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_authorize_id")));
		voucher_authorize_time = PadQuotes(request.getParameter("txt_voucher_authorize_time"));
		voucher_no = CNumeric(PadQuotes(request.getParameter("hid_voucher_no")));
		voucher_so_id = CNumeric(PadQuotes(request.getParameter("txt_voucher_so_id")));
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

		row_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_ledger_count"))));

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";

		if (voucherdate.equals("")) {
			msg += "<br>Select Journal Date!";
		}

		if (voucher_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (voucher_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

		if (voucher_narration.equals("")) {
			msg = msg + "<br>Describe Narration!";
		}

		if (update.equals("yes") || add.equals("yes")) {
			if (voucherdate.equals("")) {
				msg = msg + "<br>Select " + vouchertype_name + " Date!";
			} else {
				voucher_date = ConvertShortDateToStr(voucherdate);
			}
		}

		if (!PadQuotes(request.getParameter("total_dr")).equals(PadQuotes(request.getParameter("total_cr")))) {
			msg += "<br>Debit and Credit Total does not match!";
		} else if (Double.parseDouble(CNumeric(PadQuotes(request.getParameter("total_dr")))) == 0) {
			msg += "<br>Total Amount Can Not Be Zero!";
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

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher"
						+ "("
						+ " voucher_vouchertype_id,"
						+ " voucher_no,"
						+ " voucher_branch_id,"
						+ " voucher_so_id,"
						+ " voucher_emp_id,"
						+ " voucher_date,"
						+ " voucher_amount,"
						+ " voucher_ref_no,"
						+ " voucher_narration,"
						+ " voucher_terms,"
						+ " voucher_active,"
						+ " voucher_authorize,"
						+ " voucher_authorize_id,"
						+ " voucher_authorize_time,"
						+ " voucher_entry_id,"
						+ " voucher_entry_date)"
						+ " VALUES" + " (" + " "
						+ vouchertype_id
						+ ","
						+ " " + CNumeric(voucher_no) + ","
						+ " " + voucher_branch_id + ","
						+ " " + voucher_so_id + ","
						+ " " + voucher_emp_id + ","
						+ " '" + voucher_date + "',"
						+ " " + CNumeric(PadQuotes(request.getParameter("total_dr"))) + ","
						+ " '" + voucher_ref_no + "',"
						+ " '" + voucher_narration + "',"
						+ " '" + vouchertype_terms + "',"
						+ " '" + voucher_active + "',"
						+ " '" + voucher_authorize + "',"
						+ " '" + voucher_authorize_id + "',"
						+ " '" + voucher_authorize_time + "',"
						+ " " + voucher_entry_id + ","
						+ " '" + voucher_entry_date + "')";
				// SOP("StrSql=add==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					voucher_id = rs.getString(1);
				}
				rs.close();

				AddItem(request, stmttx, voucher_id);

				conntx.commit();
				// Update the customer current balance
				if (voucherclass_acc.equals("1")) {
					// CalcuateCurrentVoucherBal(comp_id, voucher_id);
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

	public void AddItem(HttpServletRequest request, Statement stmttx, String voucher_id) {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans WHERE vouchertrans_voucher_id = " + voucher_id;
			stmttx.addBatch(StrSql);

			if (row_count != 0) {
				int count = 1;
				while (count <= row_count) {
					if (!CNumeric(PadQuotes(request.getParameter("hid_ledger_" + count))).equals("0")) {

						StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ "("
								+ " vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_dc,"
								+ " vouchertrans_option_id"
								+ " ) VALUE ("
								+ " " + voucher_id + ","
								+ " " + CNumeric(PadQuotes(request.getParameter("hid_ledger_" + count))) + ",";
						if (Double.parseDouble(CNumeric(PadQuotes(request.getParameter("dr_journal_" + count)))) != 0) {
							StrSql += " " + CNumeric(PadQuotes(request.getParameter("dr_journal_" + count))) + ","
									+ " 1,";
						} else {
							StrSql += " " + CNumeric(PadQuotes(request.getParameter("cr_journal_" + count))) + ","
									+ " 0,";
						}
						StrSql += " 1"
								+ " )";
						// SOP("AddItem===StrSql====" + StrSql);
						stmttx.addBatch(StrSql);
					}
					++count;
				}
				stmttx.executeBatch();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " voucher_no, voucher_branch_id, voucher_date,"
					+ " voucher_so_id, voucher_amount, voucher_emp_id,"
					+ " voucher_ref_no, voucher_narration, voucher_active,"
					+ " voucher_authorize,voucher_authorize_id,voucher_authorize_time,"
					+ " voucher_entry_id, voucher_entry_date, voucher_modified_id,"
					+ " voucher_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_id = " + voucher_id + "";

			// SOP("StrSql==populate==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_no = crs.getString("voucher_no");
					voucher_branch_id = crs.getString("voucher_branch_id");
					voucher_so_id = crs.getString("voucher_so_id");
					voucherdate = strToShortDate(crs.getString("voucher_date"));
					voucher_date = crs.getString("voucher_date");
					voucher_emp_id = crs.getString("voucher_emp_id");
					voucher_ref_no = crs.getString("voucher_ref_no");
					voucher_narration = crs.getString("voucher_narration");
					voucher_active = crs.getString("voucher_active");
					voucher_authorize = crs.getString("voucher_authorize");
					voucher_authorize_id = crs.getString("voucher_authorize_id");
					voucher_authorize_time = crs.getString("voucher_authorize_time");
					voucher_entry_id = crs.getString("voucher_entry_id");
					entry_by = Exename(comp_id, crs.getInt("voucher_entry_id"));
					entry_date = strToLongDate(crs.getString("voucher_entry_date"));
					voucher_modified_id = crs.getString("voucher_modified_id");
					if (!voucher_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(voucher_modified_id));
						modified_date = strToLongDate(crs.getString("voucher_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Voucher!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
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

				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
						+ " SET"
						+ " voucher_branch_id = " + voucher_branch_id + ","
						+ " voucher_so_id = " + voucher_so_id + ","
						+ " voucher_no = " + CNumeric(voucher_no) + ","
						+ " voucher_emp_id = " + voucher_emp_id + ","
						+ " voucher_date = '" + voucher_date + "',"
						+ " voucher_amount = " + CNumeric(PadQuotes(request.getParameter("total_dr"))) + ","
						+ " voucher_ref_no = '" + voucher_ref_no + "',"
						+ " voucher_narration = '" + voucher_narration + "',"
						+ " voucher_active = '" + voucher_active + "',"
						+ " voucher_authorize = '" + voucher_authorize + "',"
						+ " voucher_authorize_id = '" + voucher_authorize_id + "',"
						+ " voucher_authorize_time = '" + voucher_authorize_time + "',"
						+ " voucher_modified_id = " + voucher_modified_id + ","
						+ " voucher_modified_date = '" + voucher_modified_date + "'"
						+ " WHERE voucher_id = " + voucher_id;
				// SOP("StrSql==update==" + StrSql);
				stmttx.executeUpdate(StrSql);
				AddItem(request, stmttx, voucher_id);

				conntx.commit();

				// Update the customer current balance
				// if (voucherclass_acc.equals("1")) {
				// CalcuateCurrentVoucherBal(company_id, voucher_id);
				// }
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			HttpServletResponse response)
			throws SQLException {
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher SET voucher_active = 0 "
						+ " WHERE voucher_id = " + voucher_id + "");
				// Update the customer current balance
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

	public String PopulateDC(String selected) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0").append(Selectdrop(0, selected)).append("> Dr </option>");
		Str.append("<option value = 1").append(Selectdrop(1, selected)).append("> Cr </option>");
		return Str.toString();
	}

	public String PopulateLedgerAC(String vouchertrans_customer_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT customer_id, customer_name" + " FROM  " + compdb(comp_id) + "axela_customer"
				+ " GROUP BY customer_id" + " ORDER BY customer_name";
		Str.append("<option value = 0> Select </option>");
		try {
			CachedRowSet rsled = processPrepQuery(StrSql, prepmap, 0);
			while (rsled.next()) {
				Str.append("<option value=").append(rsled.getString("customer_id"));
				Str.append(StrSelectdrop(rsled.getString("customer_id"), vouchertrans_customer_id));
				Str.append(">").append(rsled.getString("customer_name"));
				Str.append("</option>");
			}
			rsled.close();
			prepmap.clear();
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

	public String custcurrbal(String customer_id) {
		String StrSql = "SELECT COALESCE(customer_curr_bal, 0.00) AS currentbal_amount"
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " WHERE customer_id = " + customer_id;
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		return ExecuteQuery(StrSql);
	}
	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT "
					+ " voucherclass_file, voucherclass_acc, "
					+ " vouchertype_name, "
					// + " vouchertype_mobile, "
					// + " vouchertype_email, vouchertype_dob, vouchertype_dnd,"
					// +
					// " vouchertype_affects_inventory, config_inventory_current_stock,"
					+ " vouchertype_ref_no_enable, vouchertype_ref_no_mandatory,"
					+ " vouchertype_terms,"
					+ " vouchertype_defaultauthorize, vouchertype_authorize"
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
					+ " " + compdb(comp_id) + "axela_comp,"
					+ " " + compdb(comp_id) + "axela_acc_voucher_type"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id,"
					+ "  " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + voucher_branch_id
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id + ""
					+ " AND voucherclass_id = " + voucherclass_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_active = 1";
			// SOP("StrSql=congif==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {

				voucherclass_acc = crs.getString("voucherclass_acc");
				vouchertype_ref_no_enable = crs.getString("vouchertype_ref_no_enable");
				vouchertype_ref_no_mandatory = crs.getString("vouchertype_ref_no_mandatory");
				vouchertype_defaultauthorize = crs.getString("vouchertype_defaultauthorize");
				vouchertype_authorize = crs.getString("vouchertype_authorize");
				voucher_authorize = vouchertype_defaultauthorize;
				vouchertype_name = crs.getString("vouchertype_name");
				vouchertype_terms = crs.getString("vouchertype_terms");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public void CopyLedgerTransToCart(String voucher_id) {
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
	// + " cart_vouchertype_id,"
	// + " cart_customer_id,"
	// + " cart_amount,"
	// + " cart_dc,"
	// + " cart_option_id,"
	// + " cart_emp_id,"
	// + " cart_session_id,"
	// + " cart_time)";
	// StrSql = StrSql
	// + " (SELECT "
	// + " 18,"
	// + " vouchertrans_customer_id,"
	// + " vouchertrans_amount,"
	// + " vouchertrans_dc,"
	// + " vouchertrans_option_id,"
	// + " " + emp_id + ","
	// + " " + session_id + ","
	// + " '" + ToLongDate(kknow()) + "'"
	// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
	// + " WHERE vouchertrans_voucher_id = " + voucher_id
	// + " AND vouchertrans_option_id != 0)";
	// // SOP("StrSql===" + StrSql);
	// updateQuery(StrSql);
	// }
}
