package axela.mktg;
//Sangita 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;
import cloudify.connect.Smart;

public class Campaign_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=campaign.jsp>Campaign</a> &gt; <a href=campaign-list.jsp?all=yes>List Campaign</a>:";
	public String LinkListPage = "campaign-list.jsp";
	public String LinkExportPage = "campaign-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=campaign-update.jsp?add=yes>Add New Campaign...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String campaign_id = "0";
	public Smart SmartSearch = new Smart();
	Encrypt encrypt = new Encrypt();
	public String advSearch = "";
	public String comp_id = "0";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Branch ID", "numeric", "campaign_branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Campaign ID", "numeric", "campaign_id"},
			{"Subject", "text", "campaign_subject"},
			{"Message", "text", "campaign_msg"},
			{"Date", "date", "campaign_sentdate"},
			{"Entry By", "text", "campaign_entry_id in (Select emp_id from axela_emp where emp_name"},
			{"Entry Date", "date", "campaign_entry_date"},
			{"Modified By", "text", "campaign_modified_id in (select emp_id from axela_emp where emp_name"},
			{"Modified Date", "date", "campaign_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = GetSession("emp_id", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			recperpage = Integer.parseInt(GetSession("emp_recperpage", request) + "");
			branch_id = CNumeric(PadQuotes(GetSession("emp_branch_id", request) + ""));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			smart = PadQuotes(request.getParameter("smart"));
			msg = PadQuotes(request.getParameter("msg"));
			campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
			SOP("campaign_id------------" + campaign_id);
			all = PadQuotes(request.getParameter("all"));
			advSearch = PadQuotes(request.getParameter("advsearch_button"));
			if (msg.toLowerCase().contains("delete")) {
				StrSearch = " AND campaign_id = 0";
			} else if (all.equals("yes")) {
				msg = "Results for all Campaigns!";
				StrSearch = StrSearch + " and campaign_id > 0";
			} else if (all.equals("recent")) {
				msg = "Recent Campaigns!";
				StrSearch = StrSearch + " and campaign_id > 0";
			} else if (!campaign_id.equals("0")) {
				msg = msg + "<br>Results for Campaign ID =" + campaign_id + "";
				StrSearch = StrSearch + " and campaign_id = " + campaign_id + "";
			} else if (advSearch.equals("Search")) // for keyword search
			{
				StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				if (StrSearch.equals("")) {
					msg = "Enter search text!";
					StrSearch = StrSearch + " AND campaign_id = 0";
				} else {
					msg = "Results for Search!";
				}
			} else if (smart.equals("yes")) {
				msg = msg + "<br>Results of Search!";
				if (!GetSession("campaignstrsql", request).equals("")) {
					StrSearch = GetSession("campaignstrsql", request);
				}
			}
			if (!StrSearch.equals("")) {
				SetSession("campaignstrsql", StrSearch, request);
			}
			StrHTML = Listdata();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		CachedRowSet crs = null;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending on search
				StrSql = " Select campaign_id, campaign_subject, campaign_msg, campaign_sentdate, branch_id, concat(branch_name, ' (', branch_code, ')') as branchname,"
						+ " coalesce(CONCAT(axela_emp.emp_name,' (',axela_emp.emp_ref_no,')'),'') as entry_name,"
						+ " campaign_entry_id ";
				StrJoin = " from " + compdb(comp_id) + "axela_mktg_campaign"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = campaign_entry_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = campaign_branch_id"
						+ " where 1=1 ";
				CountSql = " SELECT Count(distinct campaign_id) ";

				StrSql = StrSql + StrJoin;
				CountSql = CountSql + StrJoin;

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch + " order by campaign_id desc";
					CountSql = CountSql + StrSearch;
				}
				if (all.equals("recent")) {
					StrSql = StrSql + " limit " + recperpage + "";
					crs = processQuery(StrSql);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
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

					if (!all.equals("recent")) {
						StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql);
					}
					int count = StartRec - 1;
					Str.append("<div class=\"  table\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Subject</th>\n");
					Str.append("<th data-hide=\"phone\">Date</th>\n");
					Str.append("<th data-hide=\"phone\">Sent By</th>\n");
					Str.append("<th data-hide=\"phone\">Branch</th>\n");
					Str.append("<th data-hide=\"phone\">Action</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append("<a href=\"campaign-statistics.jsp?campaign_id=").append(crs.getInt("campaign_id")).append("\">").append(crs.getString("campaign_subject")).append("</a>");
						Str.append("</td>");
						Str.append("<td valign=top align=center>").append(strToLongDate(crs.getString("campaign_sentdate"))).append("</td>");
						Str.append("<td valign=top align=left><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("campaign_entry_id")).append("\" target=_blank>")
								.append(crs.getString("entry_name")).append("</a></td>");
						Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
								.append("</a></td>");
						Str.append("<td valign=top align=left><a href=\"campaign-update.jsp?update=yes&campaign_id=").append(crs.getString("campaign_id")).append(" \">Update Campaign</a>");
						Str.append("<br><a href=\"../mktg/campaign-preview.jsp?campaign_id=").append(encrypt.encrypt(crs.getString("campaign_id"))).append("\" target=\"_blank\">Preview Campaign</a>");
						if (crs.getString("campaign_sentdate").equals("")) {
							Str.append("<br><a href=\"campaign-testmail.jsp?send=yes&campaign_id=").append(crs.getString("campaign_id")).append(" \">Send Now</a>");
						}
						Str.append("<br><a href=\"../portal/email-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append(" \">List Email</a>");
						Str.append("<br><a href=\"campaign-testmail.jsp?campaign_id=").append(crs.getString("campaign_id")).append(" \">Test Mail</a>");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Campaign(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
