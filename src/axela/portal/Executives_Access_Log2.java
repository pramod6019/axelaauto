// Ved Prakash (28th August 2013)
package axela.portal;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Access_Log2 extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String search = "";
	public String submitB = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess;
	public String dr_emp_id = "";
	public String startdate = "";
	public String enddate = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				startdate = PadQuotes(request.getParameter("txt_starttime"));
				enddate = PadQuotes(request.getParameter("txt_endtime"));

				if (startdate.equals("")) {
					startdate = strToShortDate(ToLongDate(AddHoursDate(kknow(), -6, 0, 0)));
					enddate = strToShortDate(ToLongDate(kknow()));
				}
				search = PadQuotes(request.getParameter("submit_button"));
				if (search.equals("Go")) {
					msg = "";
					dr_emp_id = PadQuotes(request.getParameter("dr_emp_id")); // Executive
					if (!dr_emp_id.equals("0")) {
						StrSearch += BranchAccess + " AND emp_id = " + dr_emp_id + "";
					}
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						StrHTML = ExecutiveLogSummary2();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (dr_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		} else if (!isValidDateFormatShort(startdate)) {
			msg += "<br>Enter Valid Start Date!";
		}

		if (enddate.equals("")) {
			msg += "<br>Select End Date!";
		} else if (!isValidDateFormatShort(enddate)) {
			msg += "<br>Enter Valid End Date!";
		}

		if (!startdate.equals("") && !enddate.equals("")) {
			if (isValidDateFormatShort(startdate) && isValidDateFormatShort(enddate)) {
				if (Long.parseLong(ConvertShortDateToStr(startdate)) > Long.parseLong(ConvertShortDateToStr(enddate))) {
					msg += "<br>End date shoule be greater than Start Date!";
				}
			}
		}

		int diffdate = (int) getDaysBetween(ConvertShortDateToStr(startdate), ConvertShortDateToStr(enddate));
		if (diffdate > 60) {
			msg += "<br>Date Difference should be less than 60 Days!";
		}
	}

	public String ExecutiveLogSummary2() {
		StringBuilder Str = new StringBuilder();
		Calendar cal;
		try {

			String bgcolor = "";
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-toggle=\"true\">#</th>\n");
			Str.append("<th>Executive</th>\n");
			Str.append("<th>Date</th>\n");
			Str.append("<th data-hide=\"phone\">First Sign In</th>\n");
			Str.append("<th data-hide=\"phone\">Last Sign Out</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");

			int diffdate = (int) getDaysBetween(ConvertShortDateToStr(startdate), ConvertShortDateToStr(enddate));// 6
																													// days
			String sdate = startdate;
			cal = Calendar.getInstance();
			cal.set(Integer.parseInt(SplitYear(ConvertShortDateToStr(sdate))),
					Integer.parseInt(SplitMonth(ConvertShortDateToStr(sdate))) - 1,
					Integer.parseInt(SplitDate(ConvertShortDateToStr(sdate))));

			for (int i = 1; i <= diffdate + 1; i++) {
				if (isSunday(ConvertShortDateToStr(sdate))) {
					bgcolor = "pink";
				} else {
					bgcolor = "white";
				}
				Str.append("<tr bgcolor=").append(bgcolor).append(">\n");
				Str.append("<td width=\"5%\" align=\"center\">").append(i).append("</td>\n");

				StrSql = "SELECT CONCAT('<a href=../portal/executive-summary.jsp?emp_id=', emp_id, '>', emp_name, ' (', emp_ref_no, ')', '</a>')"
						+ " FROM " + compdb(comp_id) + "axela_emp_log"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = log_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
						+ " WHERE 1 = 1" + StrSearch + ""
						+ " GROUP BY SUBSTR(log_signin_time, 1, 8)";

				Str.append("<td align=\"left\">").append(ExecuteQuery(StrSql)).append("</td>\n");
				Str.append("<td align=\"left\">").append(ConvertLongDate(ConvertShortDateToStr(sdate))).append("</td>\n");

				StrSql = "SELECT DATE_FORMAT(MIN(log_signin_time), '%H:%i') AS signintime"
						+ " FROM " + compdb(comp_id) + "axela_emp_log"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = log_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
						+ " WHERE SUBSTR(log_signin_time, 1, 8) = '" + ConvertShortDateToStr(sdate).substring(0, 8) + "'"
						+ StrSearch + " GROUP BY SUBSTR(log_signin_time, 1, 8)"
						+ " ORDER BY log_signin_time DESC";

				Str.append("<td align=\"center\">");
				String signintime = ExecuteQuery(StrSql);
				if (!signintime.equals("")) {
					Str.append(signintime);
				} else {
					Str.append("--");
				}
				Str.append("</td>\n");

				StrSql = "SELECT IF(log_signout_time != '', DATE_FORMAT(MAX(log_signout_time), '%H:%i'), '') AS signouttime"
						+ " FROM " + compdb(comp_id) + "axela_emp_log"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = log_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
						+ " WHERE SUBSTR(log_signout_time, 1, 8) = '" + ConvertShortDateToStr(sdate).substring(0, 8) + "'"
						+ StrSearch + " GROUP BY SUBSTR(log_signout_time, 1, 8)"
						+ " ORDER BY log_signout_time DESC";

				Str.append("<td align=\"center\">");
				String signouttime = ExecuteQuery(StrSql);
				if (!signouttime.equals("")) {
					Str.append(signouttime);
				} else {
					Str.append("--");
				}
				Str.append("</td>\n");
				Str.append("</tr>\n");
				cal.add(cal.DATE, 1);
				int currMonth = cal.get(cal.MONTH);
				int currYear = cal.get(cal.YEAR);
				int currDate = cal.get(cal.DATE);
				sdate = "" + doublenum(currDate) + "/" + doublenum(currMonth + 1) + "/" + currYear;
			}
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE emp_active = '1' " + BranchAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), dr_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (");
				Str.append(crs.getString("emp_ref_no")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
