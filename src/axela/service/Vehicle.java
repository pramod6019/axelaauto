package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String reportURL = "";
	public String ListLink = "<a href=vehicle-list.jsp?smart=yes>Click here to List Vehicles</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_service_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(veh_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch += " and substr(veh_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " and so_branch_id=" + dr_branch_id;
					}
					StrSearch += " and branch_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("vehstrsql", StrSearch, request);
						StrHTML = VehicleSummary(request);
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
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
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
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String VehicleSummary(HttpServletRequest request) {
		int service_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT coalesce(branch_id, 0) as branch_id, coalesce(concat(branch_name,' (', branch_code, ')'),'') as branchname, "
					+ " count(veh_id) AS servicecount"
					+ " FROM  " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1 = 1 " + StrSearch + ""
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			// SOP("StrSql-ll-"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Service Vehicle Summary</b><br>");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th >Vehicle Count</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					service_count = service_count + crs.getInt("servicecount");
					Str.append("<tr>");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td  valign=top align=right>").append(crs.getString("servicecount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(service_count).append("</b></td>\n");
				Str.append("</tr></tbody></table></div>");
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
