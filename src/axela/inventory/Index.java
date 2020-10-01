package axela.inventory;
//Murali 21st jun
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String submitB = "";
	public static String msg = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "";
	public String StrLibrary = "";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String displayprint = "";
	public String location = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=lms-items-issued-list.jsp?smart=yes>Click here to List LMS</a>";
	public String reportURL = "../Campaign_Report.do?target=" + Math.random();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_purchase_order_access, emp_grn_access, emp_purchase_return, emp_item_access, emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
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
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String CampaignSummary(HttpServletRequest request) {
		StrSql = "";
		int issuedCount = 0;
		int enqcount = 0;
		StringBuilder Str = new StringBuilder();
		int count = 0;

		try {
			StrSql = " SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate, "
					+ " coalesce((select count(enquiry_id) from " + compdb(comp_id) + "axela_sales_enquiry where  enquiry_campaign_id = campaign_id"
					+ " ),0)  as enqcount"
					+ " from " + compdb(comp_id) + "axela_sales_campaign"
					+ " where campaign_active='1' "
					+ " and campaign_startdate<" + ToLongDate(kknow()) + " and campaign_enddate>" + ToLongDate(kknow());
			StrSql = StrSql + " group by campaign_id order by campaign_name";

			// SOP("strsql in LmsSummary---" + StrSqlBreaker(StrSql));
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

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 9"
				+ " AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		// SOP("ListReports()---StrSql==== " + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
				while (crs.next()) {
					Str.append("<tr>\n");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a>");
					Str.append("</td>\n</tr>\n");
				}
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
