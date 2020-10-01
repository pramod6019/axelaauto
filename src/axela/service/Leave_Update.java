package axela.service;
//Dilip Kumar P

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Leave_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String checkperm = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String leave_id = "";
	public String leave_emp_id = "0";
	public String leave_fromtime = "";
	public String leavefromtime = "";
	public String leavetotime = "";
	public String leave_totime = "";
	public String leave_desc = "";
	public String leave_notes = "";
	public String leave_entry_id = "0";
	public String leave_entry_date = "";
	public String leave_modified_id = "";
	public String leave_modified_date = "";
	public String emp_branch_id = "0", emp_role_id = "0";
	public String BranchAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				leave_id = CNumeric(PadQuotes(request.getParameter("leave_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						leave_emp_id = "";
						leave_desc = "";
						leave_notes = "";
						leavefromtime = "";
						leave_fromtime = "";
						leavetotime = "";
						leave_totime = "";
					} else {
						if (ReturnPerm(comp_id, "emp_service_jobcard_add", request).equals("1")) {
							GetValues(request, response);
							leave_entry_id = CNumeric(GetSession("emp_id", request));
							leave_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?leave_id=" + leave_id + "&msg=Leave added successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Leave".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Leave".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_service_jobcard_edit", request).equals("1")) {
							GetValues(request, response);
							leave_modified_id = CNumeric(GetSession("emp_id", request));
							leave_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?leave_id=" + leave_id + "&msg=Leave Details Updated Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Leave".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_service_jobcard_edit", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?msg=Leave Details Deleted Successfully"));
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
		leave_id = CNumeric(PadQuotes(request.getParameter("leave_id")));
		emp_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		leave_emp_id = PadQuotes(request.getParameter("dr_emp"));
		leavefromtime = PadQuotes(request.getParameter("txt_leave_fromtime"));
		leave_fromtime = ConvertLongDateToStr(leavefromtime);
		leavetotime = PadQuotes(request.getParameter("txt_leave_totime"));
		leave_totime = ConvertLongDateToStr(leavetotime);
		leave_desc = PadQuotes(request.getParameter("txt_leave_desc"));
		leave_notes = PadQuotes(request.getParameter("txt_leave_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (emp_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (leave_emp_id.equals("0")) {
			msg = msg + "<br>Select Vehicle!";
		}
		if (leavefromtime.equals("")) {
			msg = msg + "<br>Select From Date!";
		}
		if (leavetotime.equals("")) {
			msg = msg + "<br>Select To Date!";
		}
		if (!leavefromtime.equals("")) {
			if (!isValidDateFormatLong(leavefromtime)) {
				msg = msg + "<br>Select valid From Time!";
			} else {
				leave_fromtime = ConvertLongDateToStr(leavefromtime);
			}
		}
		if (!leavetotime.equals("")) {
			if (!isValidDateFormatLong(leavetotime)) {
				msg = msg + "<br>Select valid To Time!";
			} else {
				leave_totime = ConvertLongDateToStr(leavetotime);
			}
			if (Long.parseLong(leave_totime) <= Long.parseLong(leave_fromtime)) {
				msg = msg + "<br>To Time must be greater than " + strToLongDate(leave_fromtime) + "!";
			}
		}
		if (leave_desc.length() > 1000) {
			leave_desc = leave_desc.substring(0, 999);
		}
		if (leave_notes.length() > 1000) {
			leave_notes = leave_notes.substring(0, 999);
		}
	}

	protected void AddFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {

			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_leave"
						+ " (leave_emp_id,"
						+ " leave_fromtime,"
						+ " leave_totime,"
						+ " leave_desc,"
						+ " leave_notes,"
						+ " leave_entry_id,"
						+ " leave_entry_date)"
						+ " VALUES"
						+ " (" + leave_emp_id + ","
						+ " '" + leave_fromtime + "',"
						+ " '" + leave_totime + "',"
						+ " '" + leave_desc + "',"
						+ " '" + leave_notes + "',"
						+ " " + leave_entry_id + ","
						+ " '" + leave_entry_date + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					leave_id = rs.getString(1);
				}
				rs.close();
				conntx.commit();

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
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void UpdateFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp_leave"
						+ " SET"
						+ " leave_emp_id = " + leave_emp_id + ","
						+ " leave_fromtime = '" + leave_fromtime + "',"
						+ " leave_totime = '" + leave_totime + "', "
						+ " leave_desc = '" + leave_desc + "', "
						+ " leave_notes = '" + leave_notes + "', "
						+ " leave_modified_id = " + leave_modified_id + ", "
						+ " leave_modified_date = '" + leave_modified_date + "'"
						+ " WHERE leave_id = " + leave_id + "";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					leave_id = rs.getString(1);
				}
				rs.close();
				conntx.commit();

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
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_emp_leave WHERE leave_id = " + leave_id + "";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select leave_emp_id, leave_fromtime, leave_totime, leave_desc, leave_notes,"
					+ " leave_entry_id, leave_entry_date, leave_modified_id, leave_modified_date,"
					+ " emp_branch_id"
					+ " from " + compdb(comp_id) + "axela_emp_leave"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = leave_emp_id"
					+ " where leave_id = " + leave_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					leave_emp_id = crs.getString("leave_emp_id");
					leave_fromtime = crs.getString("leave_fromtime");
					emp_branch_id = crs.getString("emp_branch_id");
					leavefromtime = strToLongDate(leave_fromtime);
					leave_totime = crs.getString("leave_totime");
					leavetotime = strToLongDate(leave_totime);
					leave_desc = crs.getString("leave_desc");
					leave_notes = crs.getString("leave_notes");
					leave_entry_id = crs.getString("leave_entry_id");
					entry_by = Exename(comp_id, crs.getInt("leave_entry_id"));
					entry_date = strToLongDate(crs.getString("leave_entry_date"));
					leave_modified_id = crs.getString("leave_modified_id");
					if (!leave_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(leave_modified_id));
						modified_date = strToLongDate(crs.getString("leave_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Demo Outage!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "Select branch_id,branch_name, branch_code from " + compdb(comp_id) + "axela_branch"
					+ " where branch_active = '1' " + BranchAccess
					+ " order by branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value=0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), emp_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutive(String branch_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_branch_id = " + branch_id + ""
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<select name=dr_emp class=selectbox id=dr_emp>");
			stringval.append("<option value=0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("emp_id"));
				stringval.append(StrSelectdrop(crs.getString("emp_id"), leave_emp_id)).append(">");
				stringval.append(crs.getString("emp_name")).append("</option>\n");
			}
			stringval.append("</Select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval.toString();
	}
}
