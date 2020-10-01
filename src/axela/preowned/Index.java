package axela.preowned;
//Murali 21st jun  
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String submitB = "";
	public String comp_id = "0";
	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "";
	public String StrLibrary = "";
	public String dr_branch_id = "";
	public String StrSearch = "", StrSql = "";
	public String smart = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String displayprint = "";
	public String location = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String followupchart_data = "";
	public String evaluationchart_data = "";
	public int TotalRecords = 0;
	public String ListLink = "<a href=lms-items-issued-list.jsp?smart=yes>Click here to List LMS</a>";
	public String reportURL = "../Campaign_Report.do?target=" + Math.random();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (smart == null) {
					smart = "";
				}
				if (msg.equals("")) {
					if (smart.equals("yes")) {
					} else {
						SetSession("ItemIssuedstrsql", StrSearch, request);
					}
					StrLibrary = CampaignSummary(request);
					PreownedFollowupEscStatus(comp_id);
					PreownedEvaluationEscStatus(comp_id);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String CampaignSummary(HttpServletRequest request) {

		int issuedCount = 0;
		int enqcount = 0;
		StringBuilder Str = new StringBuilder();
		int count = 0;

		try {
			StrSql = " SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate,"
					+ " COALESCE((SELECT COUNT(enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE  enquiry_campaign_id = campaign_id"
					+ " ),0) AS enqcount"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " WHERE campaign_active='1' "
					+ " AND campaign_startdate<" + ToLongDate(kknow()) + ""
					+ " AND campaign_enddate>" + ToLongDate(kknow());
			StrSql = StrSql + " GROUP BY campaign_id ORDER BY campaign_name";

			// SOP("strsql in LmsSummary---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>Campaign</th>\n");
			Str.append("<th>Date</th>\n");
			Str.append("<th>Enquiry</th>\n");
			Str.append("</tr>\n");
			while (crs.next()) {
				count = count + 1;
				// issuedCount = issuedCount + crs.getInt("itemscount");
				enqcount = enqcount + crs.getInt("enqcount");
				Str.append("<tr>\n");
				Str.append("<td valign=top align=center><b>" + count + ".</b></td>\n");
				Str.append("<td valign=top align=left><a href=campaign-list.jsp?campaign_id=" + crs.getString("campaign_id") + ">" + crs.getString("campaign_name") + "</a></td>\n");
				Str.append("<td valign=top align=center>" + strToShortDate(crs.getString("campaign_startdate")) + " - " + strToShortDate(crs.getString("campaign_enddate")) + "</td>");
				Str.append("<td valign=top align=right>" + crs.getString("enqcount") + "</td>");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right><b>Total:</b></td>\n");
			Str.append("<td align=right><b>" + enqcount + "</b></td>\n");
			Str.append("</tr></table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PreownedFollowupEscStatus(String comp_id) {
		try {
			int totalpreownedcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " Select gr.group_id as group_id, COALESCE(triggercount,0) AS triggercount"
					+ " from ( "
					+ " select 1 as group_id "
					+ " UNION "
					+ " select 2 as group_id "
					+ " UNION "
					+ " select 3 as group_id "
					+ " UNION "
					+ " select 4 as group_id "
					+ " UNION "
					+ " select 5 as group_id "
					+ " ) as gr "
					+ " LEFT JOIN (SELECT count(preowned_id) AS triggercount, preownedfollowup_trigger"
					+ " FROM " + compdb(comp_id) + "axela_preowned_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedfollowup_preowned_id"
					+ " WHERE preownedfollowup_desc = '' "
					+ " AND preownedfollowup_trigger > 0"
					+ " AND preowned_preownedstatus_id = 1"
					+ BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY preownedfollowup_trigger) AS tr ON tr.preownedfollowup_trigger = gr.group_id"
					+ " where 1=1 "
					+ " group by group_id "
					+ " order by group_id DESC";
			// SOP("StrSql----PreownedFollowupEscStatus---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			TotalRecords = 5;
			int count = 0, level = 5;
			String color = "";
			followupchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (level == 5) {
						color = "#FF0033";
					} else if (level == 4) {
						color = "#FF8030";
					} else if (level == 3) {
						color = "#7ba5de";
					} else if (level == 2) {
						color = "#F8FF01";
					} else if (level == 1) {
						color = "#c3a7e2";
					}
					count++;
					followupchart_data = followupchart_data + "{'level': 'Level " + level + "', 'value':" + crs.getString("triggercount") + ", 'color':'" + color
							+ "', 'url':'preowned-followup-esc-status.jsp'}";
					level--;
					if (count < TotalRecords) {
						followupchart_data = followupchart_data + ",";
					}
				}
				followupchart_data = followupchart_data + "]";

				// SOP("followupchart_data====" + followupchart_data);
			}
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PreownedEvaluationEscStatus(String comp_id) {
		try {
			int totalpreownedevalcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " Select gr.group_id as group_id, COALESCE(triggercount,0) AS triggercount"
					+ " from ( "
					+ " select 1 as group_id "
					+ " UNION "
					+ " select 2 as group_id "
					+ " UNION "
					+ " select 3 as group_id "
					+ " UNION "
					+ " select 4 as group_id "
					+ " UNION "
					+ " select 5 as group_id "
					+ " ) as gr "
					+ " LEFT JOIN (SELECT count(preowned_id) AS triggercount, preowned_eval_trigger"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " WHERE preowned_eval_trigger > 0"
					+ " AND preowned_preownedstatus_id = 1"
					+ BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY preowned_eval_trigger) AS tr ON tr.preowned_eval_trigger = gr.group_id"
					+ " where 1=1 "
					+ " group by group_id "
					+ " order by group_id DESC";
			// SOPInfo("StrSql----PreownedEvaluationEscStatus---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			TotalRecords = 5;
			int count = 0, level = 5;
			String color = "";
			evaluationchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (level == 5) {
						color = "#FF0033";
					} else if (level == 4) {
						color = "#FF8030";
					} else if (level == 3) {
						color = "#7ba5de";
					} else if (level == 2) {
						color = "#F8FF01";
					} else if (level == 1) {
						color = "#c3a7e2";
					}
					count++;
					evaluationchart_data = evaluationchart_data + "{'level': 'Level " + level + "', 'value':" + crs.getString("triggercount") + ", 'color':'" + color
							+ "', 'url':'preowned-evaluation-esc-status.jsp'}";
					level--;
					if (count < TotalRecords) {
						evaluationchart_data = evaluationchart_data + ",";
					}
				}
				evaluationchart_data = evaluationchart_data + "]";
			}
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrintOption() {
		if (printoption.equals("")) {
			printoption = "CampaignDetails";
		}
		String print = "";
		print = print + "<option value = CampaignDetails" + StrSelectdrop("CampaignDetails", printoption) + ">Campaign Details</option>\n";
		return print;
	}

	public String PopulateExport() {
		if (exporttype.equals("")) {
			exporttype = "pdf";
		}
		String export = "";
		export = export + "<option value = xlsx" + StrSelectdrop("xlsx", exporttype) + ">MS Excel Format</option>\n";

		return export;
	}

	public String RefreshForm() {
		if (displayprint.equals("yes")) {
			RefreshForm = "onload=\"remote=window.open('" + location + "','contactsexport','');remote.focus();\"";
		}
		return RefreshForm;
	}

	public String ListReports(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 7"
				+ " AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		// SOP("StrSql==" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered table-hover\">\n");
				Str.append("<table class=\"table\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr>\n");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name").replace("Pre Owned", ReturnPreOwnedName(request)))
							.append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
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
