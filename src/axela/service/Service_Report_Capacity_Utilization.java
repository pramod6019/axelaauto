package axela.service;
//aJIt

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Report_Capacity_Utilization extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public String startdate = "", start_date = "", start_time = "", starttime = "";
	public String enddate = "", end_date = "", end_time = "", endtime = "";
	public static String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String jc_branch_id = "0", branch_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String go = "";
	public Date date = new Date();
	public DecimalFormat df = new DecimalFormat("0.00");
	public int total_hours = 0;
	public int occupiedTime = 0;
	public int free_hours = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (branch_id.equals("0")) {
					jc_branch_id = CNumeric(PadQuotes(request.getParameter("dr_bay_branch_id")));
				} else {
					jc_branch_id = branch_id;
				}
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = ReportStartdate();
				enddate = strToShortDate(ToShortDate(kknow()));
				starttime = SplitHourMin(ToLongDate(date));
				starttime = "00:00";
				endtime = "23:59";

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListData();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		if (!startdate.equals("")) {
			start_date = ConvertShortDateToStr(startdate);
		}

		if (!enddate.equals("")) {
			end_date = ConvertShortDateToStr(enddate);
		}

		if (starttime.contains(":") && !starttime.equals("") && starttime.length() == 5) {
			start_time = starttime.split(":")[0].concat(starttime.split(":")[1]);
		}

		if (endtime.contains(":") && !endtime.equals("") && endtime.length() == 5) {
			end_time = endtime.split(":")[0].concat(endtime.split(":")[1]);
		}
	}

	protected void CheckForm() {

		msg = "";
		if (jc_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}

		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter valid Start Date!";
				start_date = "";
			}
		}

		if (enddate.equals("")) {
			msg += "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!start_date.equals("") && !end_date.equals("")
						&& Long.parseLong(start_date) > Long.parseLong(end_date)) {
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter valid End Date!";
				end_date = "";
			}
		}

		if (!starttime.equals("") && (!starttime.contains(":") || start_time.length() != 4)) {
			msg += "<br>Enter valid Start Time!";
		}

		if (!endtime.equals("") && (!endtime.contains(":") || end_time.length() != 4)) {
			msg += "<br>Enter valid End Time!";
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		double startdt = 0, enddt = 0;
		StrSql = "SELECT bay_name,"
				+ " SUM(TIME_TO_SEC(TIMEDIFF(baytrans_end_time, baytrans_start_time)) / 60) AS  occtime"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_bay_id = bay_id"
				+ " AND ((SUBSTR(baytrans_start_time, 1, 8) >= SUBSTR('" + start_date + "', 1, 8)"
				+ " AND SUBSTR(baytrans_start_time, 1, 8) < SUBSTR('" + end_date + "', 1, 8)) "
				+ " OR (SUBSTR(baytrans_end_time, 1, 8) > SUBSTR('" + start_date + "', 1, 8)"
				+ " AND SUBSTR(baytrans_end_time, 1, 8) <= SUBSTR('" + end_date + "', 1, 8)) "
				+ " OR (SUBSTR(baytrans_start_time, 1, 8) < SUBSTR('" + start_date + "', 1, 8)"
				+ " AND SUBSTR(baytrans_end_time, 1, 8) > SUBSTR('" + end_date + "', 1, 8)))"
				+ " AND ((SUBSTR(baytrans_start_time, 9, 4) >= SUBSTR('" + start_time + "', 1, 8)"
				+ " AND SUBSTR(baytrans_start_time, 1, 8) < SUBSTR('" + end_time + "', 9, 4)) "
				+ " OR (SUBSTR(baytrans_end_time, 9, 4) > SUBSTR('" + start_time + "', 1, 8)"
				+ " AND SUBSTR(baytrans_end_time, 1, 8) <= SUBSTR('" + end_time + "', 9, 4)) "
				+ " OR (SUBSTR(baytrans_start_time, 9, 4) < SUBSTR('" + start_time + "', 1, 8)"
				+ " AND SUBSTR(baytrans_end_time, 1, 8) > SUBSTR('" + end_time + "', 9, 4)))"
				+ " WHERE bay_branch_id = " + jc_branch_id + ""
				+ " GROUP BY bay_id"
				+ " ORDER BY bay_id";
		startdt = StringToDate(start_date.substring(0, 8) + start_time + "00").getTime() / 60000;
		enddt = StringToDate(end_date.substring(0, 8) + end_time + "00").getTime() / 60000;
		total_hours = (int) (enddt - startdt);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Bay Name</th>\n");
				Str.append("<th>Occupied</th>\n");
				Str.append("<th data-hide=\"phone\">Free</th>\n");
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("bay_name")).append("</td>\n");
					free_hours = total_hours - crs.getInt("occtime");
					Str.append("<td>").append(ConvertMintoHrsMins(crs.getInt("occtime"))).append("</td>\n");
					Str.append("<td>").append(ConvertMintoHrsMins(free_hours)).append("</td>\n");
					Str.append("<td>").append(ConvertMintoHrsMins(total_hours)).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<center><font color=\"red\"><b>No Bay Free!</b></font></center>");
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
