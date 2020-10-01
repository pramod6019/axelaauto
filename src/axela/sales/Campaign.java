package axela.sales;
//@Bhagwan Singh 11 feb 2013

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
	public String comp_id = "0";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String branch_id = "", dr_branch_id = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=campaign-list.jsp?smart=yes>Click here to List Campaign</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(campaign_startdate,1,8) >= substr('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(campaign_startdate,1,8) <= substr('" + endtime + "',1,8)";
					}
					StrSearch = StrSearch + " and campaign_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("Campaignstrsql", StrSearch, request);
						StrHTML = CampaignSummary(request);
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
		int campaigncount = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT concat(substring(campaign_startdate,1,8),'000000') AS campaignstartdate, "
					+ " count(campaign_id) AS campaigncount "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_campaign_id = campaign_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = camptrans_branch_id"
					+ " WHERE 1=1" + StrSearch + BranchAccess
					+ " GROUP BY campaign_id"
					+ " ORDER BY campaign_startdate";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Campaign Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr align=center>\n");
				Str.append("<th data-hide=\"phone, tablet\" style=\"text-align:center\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" style=\"text-align:center\">Campaign Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					campaigncount = campaigncount + crs.getInt("campaigncount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left>").append(ConvertLongDate(crs.getString("campaignstartdate"))).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("campaigncount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left><b>Total: &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right><b>").append(campaigncount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tr></table></div></div>");
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
