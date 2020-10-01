package axela.mktg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=campaign-list.jsp?smart=yes>Click here to List Campaigns</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(PadQuotes(session.getAttribute("emp_id") + ""));
			branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			// ExportPerm = ReturnPerm("emp_export_access", request, response);
			smart = PadQuotes(request.getParameter("smart"));

			if (!smart.equals("yes")) {
				GetValues(request, response);
				CheckForm();
				if (!starttime.equals("")) {
					StrSearch = " and substr(campaign_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
				}
				if (!endtime.equals("")) {
					StrSearch = StrSearch + " and substr(campaign_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
				}
				if (!dr_branch_id.equals("0")) {
					StrSearch = StrSearch + " and campaign_branch_id=" + dr_branch_id;
				}
				StrSearch = StrSearch + " and branch_active = '1' ";
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					session.setAttribute("campaignstrsql", StrSearch);
					StrHTML = CampaignSummary(request);
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
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
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
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

	public String CampaignSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int campaigncount = 0;
		StrHTML = "";
		try {
			StrSql = " SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname, "
					+ " count(campaign_id) as campaigncount "
					+ " from  axela_mktg_campaign "
					+ " inner join axela_branch on branch_id = campaign_branch_id "
					+ " where 1=1 "
					+ StrSearch
					+ " group by branch_id order by branchname";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Campaign Summary</b><br>");
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th width=20%>Campaign Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					campaigncount = campaigncount + crs.getInt("campaigncount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("campaigncount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(campaigncount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</table>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
