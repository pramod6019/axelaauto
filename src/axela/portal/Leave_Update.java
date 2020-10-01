package axela.portal;

//Saiman 12th Feb 2013
// divya
// modified by sn 6, 7 may 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Leave_Update extends Connect {

	public String add = "", update = "", addB = "", updateB = "", deleteB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "", ExeAccess = "";

	// FOR LEAVE
	public String leave_id = "", leave_emp_id = "0", leavetype_id = "0", leavetype_name = "";
	public String from_date = "", to_date = "", leave_notes = "", leave_active = "1";
	public String entry_id = "", entry_by = "", entry_date = "", modified_id = "", modified_by = "", modified_date = "", status = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_leave_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_leave_add", request).equals("1")) {
							entry_id = CNumeric(GetSession("emp_id", request));
							entry_date = ToLongDate(kknow());
							modified_date = "";
							AddLeaveFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?leave_id=" + leave_id + "&msg=Leave Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					leave_id = PadQuotes(request.getParameter("leave_id"));
					// modified_by = Exename(comp_id, Integer.parseInt(emp_id));
					if (!"yes".equals(updateB) && !"yes".equals(deleteB)) {
						PopulateLeaveFields(response);
					} else if ("yes".equals(updateB) && !"Delete Leave".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_leave_edit", request).equals("1")) {
							modified_id = CNumeric(GetSession("emp_id", request));
							modified_date = ToLongDate(kknow());
							UpdateLeaveFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?leave_id=" + leave_id + "&msg=Leave Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Leave".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_leave_delete", request).equals("1")) {
							DeleteLeaveFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("leave-list.jsp?leave_id=" + leave_id + "&msg=Leave Deleted Successfully!"));
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
		leave_emp_id = PadQuotes(request.getParameter("dr_emp_id"));
		leavetype_id = PadQuotes(request.getParameter("dr_leavetype_id"));
		from_date = PadQuotes(request.getParameter("txt_from_date"));
		if (from_date.equals("")) {
			from_date = strToLongDate(ToLongDate(kknow()));
		}
		to_date = PadQuotes(request.getParameter("txt_to_date"));
		if (to_date.equals("")) {
			to_date = strToLongDate(ToLongDate(kknow()));
		}
		leave_notes = PadQuotes(request.getParameter("txt_leave_note"));
		leave_active = CheckBoxValue(PadQuotes(request.getParameter("chk_leave_active")));
	}

	protected void CheckForm() {
		msg = "";
		if (leave_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}
		if (leavetype_id.equals("0")) {
			msg += "<br>Select Leave Type!";
		}
		if (from_date.equals("")) {
			msg += "<br>Enter From-Date!";
		} else {
			if (!isValidDateFormatLong(from_date)) {
				msg = msg + "<br>Enter Valid From-Date!";
			}
		}
		if (to_date.equals("")) {
			msg += "<br>Enter To-Date!";
		} else {
			if (!isValidDateFormatLong(to_date)) {
				msg = msg + "<br>Enter Valid To-Date!";
			}
			if (Long.parseLong(ConvertLongDateToStr(from_date)) > Long.parseLong(ConvertLongDateToStr(to_date))) {
				msg = msg + " <br>To-Date can't be less than From-Date!";
			}
		}
	}

	public String PopulateLeaveType() {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			StrSql = "SELECT leavetype_id, leavetype_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp_leave_type"
					+ " WHERE leavetype_active = 1"
					+ " GROUP BY leavetype_id"
					+ " ORDER BY leavetype_name";
			// SOP("===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("leavetype_id"));
				Str.append(Selectdrop((int) crs.getInt("leavetype_id"), leavetype_id));
				Str.append(">").append(crs.getString("leavetype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxA Pro===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void AddLeaveFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_leave"
						+ "( leave_emp_id,"
						+ " leave_leavetype_id,"
						+ " leave_fromdate,"
						+ " leave_todate,"
						+ " leave_notes,"
						+ " leave_active ,"
						+ " leave_entry_id ,"
						+ " leave_entry_date )"
						+ " VALUES	"
						+ "(" + leave_emp_id + ","
						+ "'" + leavetype_id + "',"
						+ "'" + ConvertLongDateToStr(from_date) + "',"
						+ "'" + ConvertLongDateToStr(to_date) + "',"
						+ "'" + leave_notes + "',"
						+ "'" + leave_active + "',"
						+ "'" + entry_id + "',"
						+ "'" + entry_date + "'"
						+ ")";

				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					leave_id = rs.getString(1);
				}
				rs.close();
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
	public void UpdateLeaveFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp_leave "
						+ " SET "
						+ " leave_emp_id = " + leave_emp_id + ","
						+ " leave_leavetype_id = " + leavetype_id + ","
						+ " leave_fromdate = " + ConvertLongDateToStr(from_date) + ","
						+ " leave_todate = " + ConvertLongDateToStr(to_date) + ","
						+ " leave_notes = '" + leave_notes + "',"
						+ " leave_active = " + leave_active + ","
						+ " leave_modified_id = " + modified_id + ","
						+ " leave_modified_date = " + ToLongDate(kknow()) + ""
						+ " WHERE leave_id= " + leave_id;

				stmttx.execute(StrSql);
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
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void DeleteLeaveFields() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_emp_leave" + " WHERE leave_id = " + leave_id;
		updateQuery(StrSql);
	}

	public void PopulateLeaveFields(HttpServletResponse response) throws SQLException {

		CachedRowSet crs = null;
		StrSql = "SELECT"
				+ " leave_emp_id, "
				+ " leave_leavetype_id, "
				+ " leavetype_name, "
				+ " leave_fromdate, "
				+ " leave_todate, "
				+ " leave_notes, "
				+ " leave_active , "
				+ " leave_entry_id , "
				+ " leave_entry_date, "
				+ " leave_modified_id , "
				+ " leave_modified_date "
				+ " FROM " + compdb(comp_id) + "axela_emp_leave"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp_leave_type ON leavetype_id = leave_leavetype_id"
				+ " WHERE 1 = 1 "
				+ " AND leave_id =" + leave_id;
		// SOP("strsql====" + StrSql);
		crs = processQuery(StrSql, 0);
		while (crs.next()) {
			leave_emp_id = crs.getString("leave_emp_id");
			leavetype_id = crs.getString("leave_leavetype_id");
			leavetype_name = crs.getString("leavetype_name");
			from_date = strToLongDate(crs.getString("leave_fromdate"));
			to_date = strToLongDate(crs.getString("leave_todate"));
			leave_notes = crs.getString("leave_notes");
			leave_active = crs.getString("leave_active");

			entry_id = crs.getString("leave_entry_id");
			if (!entry_id.equals("")) {
				entry_by = Exename(comp_id, Integer.parseInt(entry_id));
			}
			entry_date = strToLongDate(crs.getString("leave_entry_date"));
			modified_id = crs.getString("leave_modified_id");
			if (!modified_id.equals("0")) {
				modified_by = Exename(comp_id, Integer.parseInt(modified_id));
				modified_date = strToLongDate(crs.getString("leave_modified_date"));
			}

		}
		crs.close();
	}
	//
	public String PopulateExecutives(String emp_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			Str.append("<option value=\"0\">Select Executive</option>\n");
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  "
					+ compdb(comp_id)
					+ "axela_emp"
					+ " WHERE emp_active = 1"
					+ BranchAccess.replace("branch_id", "emp_branch_id")
					+ ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop((int) crs.getDouble("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
