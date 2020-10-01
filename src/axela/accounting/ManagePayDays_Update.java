package axela.accounting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePayDays_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String paydays_id = "0";
	public String paydays_name = "";
	public String paydays_days = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String paydays_entry_id = "0";
	public String paydays_entry_date = "";
	public String paydays_modified_id = "0";
	public String paydays_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String entity_id = "0";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request)) + "";
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				entity_id = CNumeric(GetSession("entity_id", request)) + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				paydays_id = CNumeric(PadQuotes(request.getParameter("paydays_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						paydays_id = "";
					} else {
						GetValues(request, response);
						paydays_entry_id = emp_id;
						paydays_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepaydays.jsp?paydays_id=" + paydays_id + "&msg=Pay Days Added Successfully!"));
						}
					}
				} else {
					if ("yes".equals(update)) {
						status = "Update";
						if (!"yes".equals(updateB) && !"Delete Pay Days".equals(deleteB)) {
							PopulateFields(response);
						} else if ("yes".equals(updateB) && !"Delete pay Days".equals(deleteB)) {
							GetValues(request, response);
							paydays_modified_id = emp_id;
							paydays_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managepaydays.jsp?paydays_id=" + paydays_id + "&msg=Pay Days Updated Successfully!"));
							}
						} else if ("Delete Pay Days".equals(deleteB)) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managepaydays.jsp?msg=Pay Days Deleted Successfully!"));
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		paydays_name = PadQuotes(request.getParameter("txt_paydays_name"));
		paydays_days = PadQuotes(request.getParameter("txt_paydays_days"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (paydays_name.equals("")) {
			msg += "<br>Enter Pay Days Name!";
		} else if (!paydays_name.equals("")) {
			StrSql = "SELECT paydays_name FROM axela_acc_paydays"
					+ " WHERE paydays_name = ?"
					+ " AND paydays_id != ?";
			map.put(1, paydays_name);
			map.put(2, paydays_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			try {
				if (crs.next()) {
					msg += "<br>Simillar Name found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOP(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			paydays_id = paydays_days;
			StrSql = "Insert INTO " + compdb(comp_id) + "axela_acc_paydays"
					+ " (paydays_id,"
					+ " paydays_name,"
					+ " paydays_days,"
					+ " values"
					+ " (" + paydays_id + ","
					+ " '" + paydays_name + "',"
					+ " '" + paydays_days + "',"
					+ "')";
			// SOP("StrSql" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from axela_acc_paydays"
					+ " where paydays_id = ?";
			map.put(1, paydays_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					paydays_id = crs.getString("paydays_id");
					paydays_name = crs.getString("paydays_name");
					paydays_days = crs.getString("paydays_days");

					if (!paydays_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(paydays_entry_id));
						entry_date = strToLongDate(crs.getString("paydays_entry_date"));
					}
					paydays_modified_id = crs.getString("paydays_modified_id");
					if (!paydays_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(paydays_modified_id));
						modified_date = strToLongDate(crs.getString("paydays_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Voucher Type!"));
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_paydays"
					+ " SET"
					+ " paydays_name = '" + paydays_name + "',"
					+ " paydays_days = '" + paydays_days + "',"
					+ " paydays_modified_id = " + paydays_modified_id + ","
					+ " paydays_modified_date = '" + paydays_modified_date + "'"
					+ " where paydays_id = " + paydays_id + "";
			// SOP("StrSql" + StrSql);
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "Delete FROM  " + compdb(comp_id) + "axela_acc_paydays where paydays_id = " + paydays_id + "";
		updateQuery(StrSql);
		// StrSql = "SELECT emp_paydays_id FROM axela_emp"
		// + " where emp_paydays_id = " + paydays_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Pay Days is Associated with Executive!";
		// }
		// if (msg.equals("")) {
		// try {
		// StrSql = "Delete FROM  "+compdb(comp_id)+"axela_acc_paydays where paydays_id = " + paydays_id + "";
		// updateQuery(StrSql);
		// } catch (Exception ex) {
		// SOP(" Axelaauto===" + this.getClass().getName());
		// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// }
		// }
	}
}
