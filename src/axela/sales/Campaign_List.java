package axela.sales;
//@Bhagwan Singh 11 feb 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Campaign_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../sales/index.jsp>Sales</a>"
			+ " &gt; <a href=campaign.jsp>Campaign</a>"
			+ " &gt; <a href=campaign-list.jsp?all=yes>List Campaigns</a>:";
	// public String LinkListPage = "testdriveoutage-list.jsp";
	public String LinkExportPage = "campaign-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=campaign-update.jsp?add=yes>Add New Campaign</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String smart = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String PageURL = "";
	public String StrSql = "";
	public String StrSearch = "";
	public Smart SmartSearch = new Smart();
	public String QueryString = "";
	public String campaign_startdate = "";
	public String campaign_enddate = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	// public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "", ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String campaign_id = "0";
	public String campaign_name = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Campaign Name", "text", "campaign_name"},
			{"Campaign Description", "text", "campaign_desc"},
			{"Campaign budget", "text", "campaign_budget"},
			{"Campaign Notes", "text", "campaign_notes"},
			{"Campaign ID", "numeric", "campaign_id"},
			{"Active", "boolean", "campaign_active"},
			{"Campaign StartDate", "date", "campaign_startdate"},
			{"Campaign EndDate", "date", "campaign_enddate"},
			{"Entry By", "text", "campaign_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "campaign_entry_date"},
			{"Modified By", "text", "campaign_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "campaign_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " and campaign_id = 0";
				} else if ("yes".equals(all)) // for all students to b displayed
				{
					msg = "Results for all Campaigns!";
					StrSearch = StrSearch + " and campaign_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Campaigns!";
					StrSearch = StrSearch + " and campaign_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " and campaign_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (!campaign_id.equals("0")) {
					msg = "Results for Campaign ID =" + campaign_id + "!";
					StrSearch = StrSearch + " and campaign_id =" + campaign_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("Campaignstrsql", request);
				}
				StrSearch += BranchAccess;

				if (!StrSearch.equals("")) {
					SetSession("campaignstrsql", StrSearch, request);
					// SOP("StrSearch---c1-----" + StrSearch);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		CachedRowSet crs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				if (!msg.equals("")) {
					StrSql = "SELECT campaign_id, campaign_name, camptype_id, branch_name, camptype_desc,"
							+ " campaign_desc, campaign_active, branch_id,"
							+ " (SELECT GROUP_CONCAT(CONCAT('<a href=../portal/branch-summary.jsp?branch_id=',branch_id, '>', branch_name, '</a>') order by branch_name separator '<br>') "
							+ " FROM " + compdb(comp_id) + "axela_branch trans "
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_branch_id = trans.branch_id"
							+ " WHERE camptrans_campaign_id = campaign_id ) AS branch,"
							+ " campaign_startdate, campaign_enddate,"
							+ " COALESCE(COUNT(DISTINCT(img_id)),0) AS img_id,"
							+ " (SELECT count(enquiry_id) "
							+ "	FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE enquiry_campaign_id=campaign_id) AS enquirycount";
					CountSql = "SELECT count(distinct campaign_id)";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_campaign"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_type ON camptype_id = campaign_campaigntype_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_campaign_id = campaign_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = camptrans_branch_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign_img ON img_campaign_id = campaign_id"
							+ " WHERE 1 = 1";
					StrSql = StrSql + SqlJoin;
					CountSql = CountSql + SqlJoin;
					if (!(StrSearch.equals(""))) {
						StrSql = StrSql + StrSearch + " GROUP BY campaign_id"
								+ " ORDER BY campaign_id desc";
						CountSql = CountSql + StrSearch;
					}
					if (all.equals("recent")) {
						StrSql = StrSql + " LIMIT " + recperpage + "";
						crs = processQuery(StrSql, 0);
						crs.last();
						TotalRecords = crs.getRow();
						crs.beforeFirst();
					} else {
						TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
					}
					// SOP("Campaign list------------/-----------7------" + StrSql);
					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Campaign(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "campaign-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

						StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

						if (!all.equals("recent")) {
							crs = processQuery(StrSql, 0);
						}
						int count = StartRec - 1;

						Str.append("<div class=\"  table\">\n");
						Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
						Str.append("<thead><tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th >ID</th>\n");
						Str.append("<th data-hide=\"phone\">Campaign Details</th>\n");
						Str.append("<th>Branch</th>\n");
						Str.append("<th data-hide=\"phone\">Period</th>\n");
						Str.append("<th data-hide=\"phone\">Enquiry Count</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {

							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td>").append(count).append("</td>\n");
							Str.append("<td>").append(crs.getString("campaign_id")).append("</td>\n");
							Str.append("<td>").append(crs.getString("campaign_name")).append("\n");
							if (crs.getString("campaign_active").equals("0")) {
								Str.append("<font color=red><b> [Inactive]</b></font>");
							}
							Str.append("<br>").append(crs.getString("camptype_desc")).append("");
							Str.append("</td>");
							Str.append("<td>").append(crs.getString("branch"));
							// if (!crs.getString("branch").equals("")) {
							// Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">")
							// .append(crs.getString("branch")).append("<br>")
							// .append("</a>");
							// }
							Str.append("</td>");
							Str.append("<td>").append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
									.append(strToShortDate(crs.getString("campaign_enddate"))).append("</td>\n");
							Str.append("<td>");
							if (!crs.getString("enquirycount").equals("0")) {
								Str.append("<a href=../sales/enquiry-list.jsp?campaign_id=").append(crs.getString("campaign_id"));
								Str.append(">");
								Str.append(crs.getString("enquirycount")).append("</a>");
							} else {
								Str.append(crs.getString("enquirycount"));
							}
							Str.append("</td>\n");
							Str.append("<td nowrap><a href=\"campaign-update.jsp?update=yes&campaign_id=").append(crs.getString("campaign_id"))
									.append(" \"> Update Campaign</a><br>\n");
							Str.append("<a href=\"campaign-image-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append(" \"> List Images</a>" + " (")
									.append(crs.getString("img_id")).append(")");
							Str.append("</td>");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();

					} else {
						Str.append("<br><br><br><br><font color=red><b>No Campaign(s) found!</b></font><br><br>");
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
