package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Report_Bay_Utilization extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public String startdate = "", start_date = "", start_time = "", starttime = "";
	public String enddate = "", end_date = "", end_time = "", endtime = "";
	public static String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String bay_branch_id = "0", branch_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String go = "";
	public String jc_bay_id = "0";
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
				go = PadQuotes(request.getParameter("submit_button"));
				if (branch_id.equals("0")) {
					bay_branch_id = CNumeric(PadQuotes(request.getParameter("dr_bay_branch_id")));
				} else {
					bay_branch_id = branch_id;
				}

				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (!startdate.equals("")) {
			start_date = ConvertShortDateToStr(startdate);
		}
		if (!enddate.equals("")) {
			end_date = ConvertShortDateToStr(enddate);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (bay_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		double startdt = 0, enddt = 0;
		StrSql = "SELECT bay_name,"
				+ " SUM(TIME_TO_SEC(TIMEDIFF(baytrans_end_time, baytrans_start_time))/60) as  occtime"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans on baytrans_bay_id = bay_id"
				+ " AND ((SUBSTR(baytrans_start_time,1,8) >= SUBSTR('" + start_date + "', 1, 8)"
				+ " and SUBSTR(baytrans_start_time,1,8) < SUBSTR('" + end_date + "', 1, 8)) "
				+ " or (SUBSTR(baytrans_end_time,1,8) > SUBSTR('" + start_date + "', 1, 8)"
				+ " and SUBSTR(baytrans_end_time,1,8) <= SUBSTR('" + end_date + "', 1, 8)) "
				+ " or (SUBSTR(baytrans_start_time,1,8) < SUBSTR('" + start_date + "', 1, 8)"
				+ " and SUBSTR(baytrans_end_time,1,8) > SUBSTR('" + end_date + "', 1, 8)))"
				+ " AND ((SUBSTR(baytrans_start_time,9,4) >= SUBSTR('" + start_time + "', 1, 8)"
				+ " and SUBSTR(baytrans_start_time,1,8) < SUBSTR('" + end_time + "', 9, 4)) "
				+ " or (SUBSTR(baytrans_end_time,9,4) > SUBSTR('" + start_time + "', 1, 8)"
				+ " and SUBSTR(baytrans_end_time,1,8) <= SUBSTR('" + end_time + "', 9, 4)) "
				+ " or (SUBSTR(baytrans_start_time,9,4) < SUBSTR('" + start_time + "', 1, 8)"
				+ " and SUBSTR(baytrans_end_time,1,8) > SUBSTR('" + end_time + "', 9, 4)))"
				// + " and SUBSTR(baytrans_start_time,1,8) >= SUBSTR('" + start_date + "', 1, 8)"
				// + " and SUBSTR(baytrans_end_time,1,8) <= SUBSTR('" + end_date + "', 1, 8)"
				// + " and SUBSTR(baytrans_start_time, 9, 4) >= '" + start_time + "'"
				// + " and SUBSTR(baytrans_end_time, 9, 4) <= '" + end_time + "'"
				+ " WHERE bay_branch_id = " + bay_branch_id + ""
				+ " GROUP BY bay_id"
				+ " ORDER BY bay_id";
		// SOP("StrSql===" + StrSql);
		startdt = StringToDate(starttime).getTime() / 60000;
		enddt = StringToDate(endtime).getTime() / 60000;
		total_hours = (int) (enddt - startdt);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Bay Name</th>\n");
				Str.append("<th>Occupied</th>\n");
				Str.append("<th data-hide=\"phone\">Free</th>\n");
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" valign=\"top\">");
					Str.append(count).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">");
					Str.append(crs.getString("bay_name")).append("</td>\n");
					free_hours = total_hours - crs.getInt("occtime");
					Str.append("<td align=\"right\">").append(ConvertMintoHrsMins(crs.getInt("occtime"))).append("</td>\n");
					Str.append("<td align=\"right\">").append(ConvertMintoHrsMins(free_hours)).append("</td>\n");
					Str.append("<td align=\"right\">").append(ConvertMintoHrsMins(total_hours)).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
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
