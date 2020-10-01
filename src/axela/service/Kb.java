package axela.service;

/**
 *
 * @author Divya 22nd June
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Kb extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=index.jsp>Service</a> &gt; <a href=ticket.jsp>Tickets</a> > <a href=kb.jsp>List Tickets</a>:";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkListPage = "../service/kb.jsp";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String all = "";
	public String search = "";
	public String smart = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String CenterAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String ticket_id = "0";
	public String customer_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Ticket ID", "numeric", "ticket_id"},
			{"Ticket Subject", "text", "ticket_subject"},
			{"Ticket Description", "text", "ticket_desc"},
			{"Ticket Closing Summary", "text", "ticket_closed_comments"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_ticket_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				search = PadQuotes(request.getParameter("search_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if ("yes".equals(all)) // for all students to b displayed
				{
					msg = "Results for all Ticket(s)!";
					StrSearch = StrSearch + " and ticket_id > 0";
				} else if (!(ticket_id.equals("0"))) {
					msg = "Results for Ticket ID =" + ticket_id + "!";
					StrSearch = StrSearch + " and ticket_id =" + ticket_id + "";
				} else if (!(customer_id.equals("0"))) {
					msg = "Results for Customer ID =" + customer_id + "!";
					StrSearch = StrSearch + " and ticket_customer_id =" + customer_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("ticketstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("ticketstrsql", request);
					}
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}

				if (!StrSearch.equals("")) {
					SetSession("ticketstrsql", StrSearch, request);
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();

		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search

			StrSql = " SELECT ticket_id, ticket_subject, ticket_desc, ticket_closed_comments, ticket_report_time ";

			CountSql = " SELECT Count(distinct(ticket_id)) ";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
					+ " WHERE 1 = 1"
					+ " AND ticket_ticketticketstatus_id = 3 "
					+ BranchAccess + ExeAccess;

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("StrSql--" + StrSql);

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Ticket(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "ticket-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

				if (all.equals("yes")) {
					StrSql = StrSql + " group by ticket_id order by ticket_report_time desc";
				} else {
					StrSql = StrSql + "  group by ticket_id order by ticket_id ";
				}
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				CachedRowSet crs = processQuery(StrSql, 0);
				try {

					int count = StartRec - 1;

					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5%>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Subject</th>\n");
					Str.append("<th>Description</th>\n");
					Str.append("<th>Report Time</th>\n");
					Str.append("<th>Solution</th>\n");
					Str.append("</tr>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>").append(crs.getString("ticket_id")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap ><b><a href=\"ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("\">")
								.append(crs.getString("ticket_subject")).append("</a></td>\n");
						Str.append("<td valign=top align=left nowrap >").append(crs.getString("ticket_desc")).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td>\n");
						Str.append("<td valign=top align=left nowrap >").append(crs.getString("ticket_closed_comments")).append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Tickets(s) found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
