package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

/**
 * 
 * @author Sri Venkatesh 5 apr 2013
 */
public class Courtesy extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String comp_id = "0";
	public String end_time = "";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	// public String ExeAccess = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=courtesy-list.jsp?smart=yes>Click here to List Courtesy Cars</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(courtesycar_time_from,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(courtesycar_time_from,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and courtesyveh_branch_id=" + dr_branch_id;
					}
					StrSearch = StrSearch + " and branch_active = '1' and courtesycar_active='1' ";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("courtesycarstrsql", StrSearch, request);
						StrHTML = CourtesycarSummary(request);
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

	public String CourtesycarSummary(HttpServletRequest request) {
		int courtesycar_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname, "
					+ " count(courtesycar_id) as courtesycarcount"
					+ " from  " + compdb(comp_id) + "axela_service_courtesy_car "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle on courtesyveh_id=courtesycar_courtesyveh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = courtesyveh_branch_id"
					+ " where 1 = 1 " + StrSearch + BranchAccess
					+ " group by branch_id order by branchname";
			// SOP("StrSql-ll-"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Courtesy Car Summary</b><br>");
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th width=20%>Courtesy Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					courtesycar_count = courtesycar_count + crs.getInt("courtesycarcount");
					Str.append("<tr>");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=");
					Str.append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branchname")).append("</a></td>\n");
					Str.append("<td  valign=top align=right>").append(crs.getString("courtesycarcount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(courtesycar_count).append("</b></td>\n");
				Str.append("</tr></table>");
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
