// Sangita 22nd may 2013
// Added Executive Summary() & Modify - Ved Prakash (26th August 2013)
package axela.portal;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_User_Activity extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String StrSearch = "";
	public String exe_id = "";
	public String BranchAccess = "", ExeAccess = "";
	public String StrHTML = "";
	public String go = "";
	public String comp_id = "0";
	public String dr_branch_id = "0";
	public String[] exe_ids, brand_ids, region_ids, branch_ids, type_ids, jobtitle_ids, department_ids, dr_devices;
	public String emp_id = "", brand_id = "", region_id = "", branch_id = "", type_id = "", department_id = "", jobtitle_id = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String dr_device = "";
	public Executive_Check execheck = new Executive_Check();
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_executive_access", request, response);
				emp_id = CNumeric(PadQuotes(GetSession("emp_id", request)));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					StrSearch = BranchAccess + ExeAccess;

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ActivitySummary();
					}
				} else {
					start_time = strToLongDate(ToShortDate(kknow()) + " 00:00");
					end_time = strToLongDate(ToLongDate(kknow()));
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToLongDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		dr_device = RetrunSelectArrVal(request, "dr_device");
		dr_devices = request.getParameterValues("dr_device");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		jobtitle_id = RetrunSelectArrVal(request, "dr_jobtitle");
		jobtitle_ids = request.getParameterValues("dr_jobtitle");
		department_id = RetrunSelectArrVal(request, "dr_department");
		department_ids = request.getParameterValues("dr_department");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatLong(starttime)) {
				starttime = ConvertLongDateToStr(starttime);
				start_time = strToLongDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
			start_time = strToLongDate(starttime);
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatLong(endtime)) {
				endtime = ConvertLongDateToStr(endtime);
				end_time = strToLongDate(endtime);

				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToLongDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String ActivitySummary() {
		StringBuilder Str = new StringBuilder();
		String userTime = "";
		try {

			StrSql = "SELECT"
					+ " emp_id, CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
					+ " jobtitle_desc,"
					+ " COALESCE (branch_name, 'Head-Office') AS branch_name,"
					+ " COUNT(DISTINCT SUBSTR(empactivity_time, 1, 12) ) AS activity,"
					+ " COALESCE((SELECT MAX(log_signin_time) AS log_signin_time"
					+ " FROM " + compdb(comp_id) + "axela_emp_log"
					+ " WHERE 1=1"
					+ " AND log_emp_id = emp_id"
					+ " GROUP BY log_emp_id),'') AS last_signin"
					+ " FROM ";

			StrSql += " " + compdb(comp_id) + "axela_emp";
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id";

			if (!jobtitle_id.equals("")) {
				StrSql += " AND emp_jobtitle_id IN (" + jobtitle_id + ")";
			}

			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}

			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}

			if (brand_id.equals("") && region_id.equals("") && branch_id.equals("")) {
				StrSql += " AND branch_brand_id IS NULL";
			}

			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_emp_activity ON empactivity_emp_id = emp_id";

			if (dr_device.equals("1")) {
				StrSql += " AND empactivity_app = '1'";
			} else if (dr_device.equals("0")) {
				StrSql += " AND empactivity_app != '1'";
			}
			if (!starttime.equals("")) {
				StrSql += " AND SUBSTR(empactivity_time,1,12) >= SUBSTR('" + starttime + "',1,12)";
			}
			if (!endtime.equals("")) {
				StrSql += " AND SUBSTR(empactivity_time,1,12) <= SUBSTR('" + endtime + "',1,12)";
			}

			StrSql += " WHERE 1=1";

			if (!exe_id.equals("")) {
				StrSql += " AND emp_id IN (" + exe_id + ",1)";
			}

			if (!department_id.equals("")) {
				StrSql += " AND emp_department_id IN (" + department_id + ")";
			}

			if (!branch_id.equals("")) {
				StrSql += " AND emp_branch_id IN (" + branch_id + ")";
			}

			StrSql += StrSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY activity DESC";
			// SOP("emp_id=====" + emp_id);
			// if (emp_id.equals("1")) {
			// SOPInfo("user-activity===" + StrSql);
			// }

			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th >#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th data-toggle=\"true\">Job Title</th>\n");
				Str.append("<th data-toggle=\"true\">Branch</th>\n");
				Str.append("<th data-hide=\"phone\">Activity</th>\n");
				Str.append("<th data-hide=\"phone\">Last Sign-In</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				int count = 0;
				long totalmins = 0;

				String days = "";
				String hours = "";
				String mins = "";
				crs.beforeFirst();

				while (crs.next()) {
					count++;

					// if (crs.getString("emp_id").equals("15")) {
					// SOPInfo("user-activity===totaltime in minute==" + crs.getString("activity"));
					// SOPInfo("user-activity===total days in hrs date min==" + ConvertMintoDaysHrsMins(Long.parseLong(crs.getString("activity"))) + "");
					// }

					userTime = ConvertMintoDaysHrsMins(Long.parseLong(crs.getString("activity"))) + "";
					totalmins += Long.parseLong(crs.getString("activity"));

					days = userTime.split(":")[0];
					hours = userTime.split(":")[1];
					mins = userTime.split(":")[2];

					userTime = days + " Days " + hours + " Hrs " + mins + " Mins";

					Str.append("<tr>");
					Str.append("<td valign='top' align='center'>").append(count).append("</td>");
					Str.append("<td valign=top align=left>");
					Str.append("<a href='../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id") + "'>" + toTitleCase(crs.getString("emp_name")) + "</a>");
					Str.append("</td>");
					Str.append("<td valign=top align=left>").append(unescapehtml(crs.getString("jobtitle_desc"))).append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("branch_name")).append("</td>");
					Str.append("<td valign=top align=center>").append(userTime).append("</td>");

					if (!crs.getString("last_signin").equals("") && crs.getString("last_signin").substring(0, 8).equals(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<td valign=top align=center>").append(strToLongDate(crs.getString("last_signin"))).append("</td>");
					} else {
						Str.append("<td valign=top align=center><font color='#ff0000'>").append(strToLongDate(crs.getString("last_signin"))).append("</font></td>");
					}

					Str.append("</tr>");
				}

				userTime = ConvertMintoDaysHrsMins(totalmins) + "";

				days = userTime.split(":")[0];
				hours = userTime.split(":")[1];
				mins = userTime.split(":")[2];

				userTime = days + " Days " + hours + " Hrs " + mins + " Mins";

				Str.append("<tr><td></td><td></td><td></td><td align=left><b>Total:</b></td><td valign=top align=center><b>" + userTime + "</b></td><td></td></tr>");

				Str.append("</tbody>\n");
				Str.append("</table>");
				// Str.append("</div>\n");

			} else {
				Str.append("<center><font color='ff0000'><b>No Activity found!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDevice() {
		StringBuilder sb = new StringBuilder();
		// sb.append("<SELECT>");
		// sb.append("<option value=0>--</option>\n");
		sb.append("<option value=1" + StrSelectdrop("1", dr_device) + ">App</option>\n");
		sb.append("<option value=0" + StrSelectdrop("0", dr_device) + ">Web</option>\n");
		// sb.append("</SELECT>");
		return sb.toString();
	}

}
