// Dilip 
package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Holiday_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";
	public String ticketholi_id = "0";
	public String ticketholi_branch_id = "0";
	public String ticketholi_name = "";
	public String ticketholi_date = "";
	public String holidaydate = "";
	public String ticketholi_entry_id = "";
	public String ticketholi_entry_date = "";
	public String ticketholi_modified_id = "";
	public String ticketholi_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String strHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_ticket_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ticketholi_id = CNumeric(PadQuotes(request.getParameter("ticketholi_id")));
				ticketholi_branch_id = CNumeric(PadQuotes(request.getParameter("ticketholi_branch_id")));
				if (!branch_id.equals("0")) {
					ticketholi_branch_id = branch_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {

					if ("yes".equals(addB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_ticket_add", request).equals("1")) {
							ticketholi_entry_id = emp_id;
							ticketholi_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("holiday-list.jsp?ticketholi_id=" + ticketholi_id + "&msg=Holiday Added Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Holiday".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Holiday".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_ticket_add", request).equals("1")) {
							ticketholi_modified_id = emp_id;
							ticketholi_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("holiday-list.jsp?ticketholi_id=" + ticketholi_id + "&msg=Holiday updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
					if ("Delete Holiday".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_ticket_add", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("holiday-list.jsp?msg=Holiday deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ticketholi_id = CNumeric(PadQuotes(request.getParameter("ticketholi_id")));
		ticketholi_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		ticketholi_name = PadQuotes(request.getParameter("txt_holiday_name"));
		ticketholi_date = PadQuotes(request.getParameter("txt_holiday_date"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

	}

	protected void CheckForm() throws SQLException {

		if (ticketholi_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (ticketholi_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}

		if (ticketholi_date.equals("")) {
			msg += "<br>Select Start Date!";
		} else if (!ticketholi_date.equals("")) {
			if (isValidDateFormatShort(ticketholi_date)) {
				holidaydate = ConvertShortDateToStr(ticketholi_date);
				// SOP("holidaydate===" + holidaydate);
			} else {
				msg += "<br>Enter Valid Date!";
			}
		}

		StrSql = "SELECT ticketholi_name, ticketholi_date FROM " + compdb(comp_id) + "axela_service_ticket_holi"
				+ " WHERE ticketholi_name = '" + ticketholi_name + "'"
				+ " AND ticketholi_date = '" + ConvertShortDateToStr(ticketholi_date) + "'"
				+ " AND ticketholi_id != " + ticketholi_id + "";
		// SOP("StrSql==="+StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.next()) {
			msg = msg + "<br>Similar Holiday Name and Date is Found!";
		}
		crs.close();

	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "Insert into " + compdb(comp_id) + "axela_service_ticket_holi"
						+ " (ticketholi_branch_id,"
						+ " ticketholi_name,"
						+ " ticketholi_date,"
						+ " ticketholi_entry_id,"
						+ " ticketholi_entry_date)"
						+ " values"
						+ " ("
						+ " " + ticketholi_branch_id + ","
						+ " '" + ticketholi_name + "',"
						+ " '" + holidaydate + "',"
						+ " " + ticketholi_entry_id + ","
						+ " '" + ticketholi_entry_date + "')";
				// SOP("StrSql----" + StrSql);
				ticketholi_id = UpdateQueryReturnID(StrSql);
			} catch (Exception e) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_holi"
						+ " SET"
						+ " ticketholi_branch_id = " + ticketholi_branch_id + ","
						+ " ticketholi_name = '" + ticketholi_name + "',"
						+ " ticketholi_date = '" + holidaydate + "',"
						+ " ticketholi_modified_id = " + ticketholi_modified_id + ","
						+ " ticketholi_modified_date = '" + ticketholi_modified_date + "'"
						+ " where ticketholi_id = " + ticketholi_id + "";
				// SOP("StrSql----" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_holi where ticketholi_id =" + ticketholi_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT ticketholi_id, ticketholi_branch_id, ticketholi_date, ticketholi_name, ticketholi_entry_id,"
					+ " ticketholi_entry_date, ticketholi_modified_id, ticketholi_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_holi"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticketholi_branch_id"
					+ " WHERE ticketholi_id = " + ticketholi_id + "";
			// SOP(StrSqlBreaker("StrSql---" + StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					ticketholi_branch_id = crs.getString("ticketholi_branch_id");
					ticketholi_name = crs.getString("ticketholi_name");
					ticketholi_date = strToShortDate(crs.getString("ticketholi_date"));
					ticketholi_entry_id = crs.getString("ticketholi_entry_id");
					entry_by = Exename(comp_id, crs.getInt("ticketholi_entry_id"));
					entry_date = strToShortDate(crs.getString("ticketholi_entry_date"));
					ticketholi_modified_id = crs.getString("ticketholi_modified_id");

					if (!ticketholi_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(ticketholi_modified_id));
						modified_date = strToLongDate(crs.getString("ticketholi_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Holiday"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_holi on ticketholi_branch_id = branch_id";

			if (add.equals("yes")) {
				StrSql += " WHERE branch_active = 1";
			}
			StrSql += " GROUP BY branch_name"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP(StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select Branch</option>\n");
			while (crs.next()) {
				Str.append("<option value =").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), ticketholi_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
