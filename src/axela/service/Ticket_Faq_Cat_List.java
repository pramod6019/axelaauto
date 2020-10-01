package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Ticket_Faq_Cat_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> > <a href=../service/index.jsp>Service</a> > <a href=../service/ticket-faq-cat-list.jsp?all=yes>List Categories</a><b>:</b>";
	// public String LinkListPage = "../service/ticket-faq-cat-list.jsp";
	public String LinkExportPage = "";
	// public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=../service/ticket-faq-cat-update.jsp?add=yes>Add New Category...</a>";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrJoin = "";
	public String CountSql = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	// public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	// public String txt_search = "";
	// public String drop_search;
	public String all = "";
	public String smart = "";
	public String service_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Category ID", "numeric", "faqservice_id"},
			{"Category Name", "text", "faqservice_name"},
			{"Department", "text", "ticket_dept_name"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));

				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_service_faq_access", request, response);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				service_id = CNumeric(PadQuotes(request.getParameter("service_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND faqservice_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Categories!";
					StrSearch = StrSearch + " and faqservice_id > 0 ";
				} else if (!(service_id.equals("0"))) {
					msg = msg + "<br>Results for Category!";
					StrSearch = StrSearch + " and faqservice_id = " + service_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
					} else {
						msg = "Results for Search!";
					}
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
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = " select faqservice_id, faqservice_name, ticket_dept_name ";

			StrJoin = " from " + compdb(comp_id) + "axela_service_ticket_faq_service "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = faqservice_ticket_dept_id "
					+ " where 1=1 ";

			CountSql = "SELECT Count(distinct(faqservice_id))  ";

			StrSql = StrSql + StrJoin;
			CountSql = CountSql + StrJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("StrSql--"+StrSql);
			CountSql = ExecuteQuery(CountSql);

			TotalRecords = Integer.parseInt(CountSql);

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Category(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "ticket-faq-cat-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

				StrSql = StrSql + " group by faqservice_id order by faqservice_id desc ";

				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5%>#</th>\n");
					Str.append("<th>Categories</th>\n");
					Str.append("<th width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("faqservice_name")).append("");
						Str.append("</td>");
						Str.append("<td valign=top align=left nowrap>" + "<a href=\"ticket-faq-cat-update.jsp?update=yes&service_id=").append(crs.getString("faqservice_id"))
								.append(" \">Update Category</a>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Category found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
