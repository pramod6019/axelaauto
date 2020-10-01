package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Bay_Dash extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String baytrans_bay_id = "0", jc_time_in = "";
	public String baytrans_jc_id = "0";
	public String baytrans_emp_id = "0";
	public String baytrans_start_time = "", baytransstarttime = "";
	public String baytrans_end_time = "", baytransendtime = "";
	public String baytrans_id = "0";
	public String baytrans_branch_id = "0";
	public String branch_name = "";
	public String baytrans_name = "";
	public String baytrans_start_entry_id = "0";
	public String baytrans_entry_date = "";
	public String baytrans_modified_id = "0";
	public String baytrans_modified_date = "";
	public String modal = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

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
				baytrans_id = CNumeric(PadQuotes(request.getParameter("baytrans_id")));
				baytrans_jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				baytrans_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				modal = PadQuotes(request.getParameter("modal"));

				if (add.equals("yes")) {
					status = "Add";
					if (addB.equals("yes")) {
						GetValues(request, response);
						baytrans_start_entry_id = emp_id;
						baytrans_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							new JobCard_Dash().ListJCManHours(baytrans_jc_id, baytrans_emp_id);
							response.sendRedirect(response.encodeRedirectURL("jobcard-bay-dash.jsp?add=yes&jc_id=" + baytrans_jc_id + "&dr_branch=" + baytrans_branch_id
									+ "&modal=yes&msg=Man Hours added successfully!"));
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Man Hours")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Man Hours")) {
						GetValues(request, response);
						baytrans_modified_id = emp_id;
						baytrans_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + baytrans_jc_id + "&modal=yes&msg=Man Hours updated successfully!"));
						}
					} else if (deleteB.equals("Delete Man Hours")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + baytrans_jc_id + "&modal=yes&msg=Man Hours deleted successfully!"));
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
		baytrans_emp_id = CNumeric(PadQuotes(request.getParameter("dr_baytrans_emp_id")));
		baytrans_bay_id = CNumeric(PadQuotes(request.getParameter("dr_bay")));
		baytrans_start_time = PadQuotes(request.getParameter("txt_baytrans_start_time"));
		baytrans_end_time = PadQuotes(request.getParameter("txt_baytrans_end_time"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (baytrans_emp_id.equals("0")) {
			msg = msg + "<br>Select Technician!";
		}

		if (baytrans_start_time.equals("")) {
			msg += "<br>Enter Start Time!";
		} else if (isValidDateFormatLong(baytrans_start_time)) {
			baytransstarttime = ConvertLongDateToStr(baytrans_start_time);
		} else {
			msg += "<br>Enter valid Start Time!";
		}

		if (baytrans_end_time.equals("")) {
			msg += "<br>Enter Start Time!";
		} else if (isValidDateFormatLong(baytrans_end_time)) {
			baytransendtime = ConvertLongDateToStr(baytrans_end_time);
		} else {
			msg += "<br>Enter valid End Time!";
		}

		StrSql = "SELECT jc_time_in FROM " + compdb(comp_id) + "axela_service_jc"
				+ " WHERE jc_id = " + baytrans_jc_id + "";
		jc_time_in = ExecuteQuery(StrSql);

		if (!baytrans_start_time.equals("") && !jc_time_in.equals("")) {
			if (isValidDateFormatLong(baytrans_start_time) && isValidDateFormatLong(jc_time_in)) {
				if (Long.parseLong(baytransstarttime) < Long.parseLong(jc_time_in)) {
					msg += "<br>Start Time must be greater than JC Time In!";
				}
			}
		}

		if (!baytrans_end_time.equals("")) {
			if (isValidDateFormatLong(baytrans_end_time)) {
				baytransendtime = ConvertLongDateToStr(baytrans_end_time);
			} else {
				msg += "<br>Enter valid End Time!";
			}
		}

		if (!baytrans_start_time.equals("") && !baytrans_end_time.equals("")) {
			if (isValidDateFormatLong(baytrans_start_time) && isValidDateFormatLong(baytrans_end_time)) {
				if (Long.parseLong(baytransendtime) < Long.parseLong(baytransstarttime)) {
					msg += "<br>End Time must be greater than Start Time!";
				}
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " (baytrans_bay_id,"
					+ " baytrans_jc_id,"
					+ " baytrans_emp_id,"
					+ " baytrans_start_time,"
					+ " baytrans_end_time,"
					+ " baytrans_start_entry_id)"
					+ " VALUES"
					+ " (" + baytrans_bay_id + ","
					+ " " + baytrans_jc_id + ","
					+ " " + baytrans_emp_id + ","
					+ " '" + baytransstarttime + "',"
					+ " '" + baytransendtime + "',"
					+ " " + baytrans_start_entry_id + ")";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT baytrans_bay_id, baytrans_jc_id, baytrans_emp_id, baytrans_start_time,"
					+ " baytrans_end_time, baytrans_emp_id, baytrans_start_entry_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " WHERE baytrans_id = " + baytrans_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					baytrans_bay_id = crs.getString("baytrans_bay_id");
					baytrans_emp_id = crs.getString("baytrans_emp_id");
					baytrans_start_time = strToLongDate(crs.getString("baytrans_start_time"));
					baytrans_end_time = strToLongDate(crs.getString("baytrans_end_time"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " SET"
					+ " baytrans_bay_id = " + baytrans_bay_id + ","
					+ " baytrans_emp_id = " + baytrans_emp_id + ","
					+ " baytrans_start_time = '" + baytransstarttime + "',"
					+ " baytrans_end_time = '" + baytransendtime + "'"
					+ " WHERE baytrans_id = " + baytrans_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " WHERE baytrans_id = " + baytrans_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = 1"
					+ " AND (emp_branch_id = " + baytrans_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + baytrans_branch_id + "))"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), baytrans_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBay() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bay_name, bay_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
					+ " WHERE bay_active = 1"
					+ " AND bay_open = 1"
					+ " ORDER BY bay_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Bay</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bay_id"));
				Str.append(Selectdrop(crs.getInt("bay_id"), baytrans_bay_id));
				Str.append(">").append(crs.getString("bay_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
