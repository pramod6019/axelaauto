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

public class Ticket_Faq_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/ticket-faq.jsp>FAQ</a> > "
			+ "<a href=../service/ticket-faq-list.jsp?all=yes>List FAQ</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=../service/ticket-faq-update.jsp?add=yes>Add New FAQ...</a>";
	public String msg = "";
	public String StrHTML = "";
	public String ExportPerm = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String ticketfaq_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"FAQ ID", "numeric", "ticketfaq_id"},
			{"Category", "text", "faqservice_name"},
			{"FAQ Question", "text", "ticketfaq_question"},
			{"FAQ Answer", "text", "ticketfaq_answer"},
			{"Active", "boolean", "ticketfaq_active"},
			{"Entry By", "text", "ticketfaq_entry_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "ticketfaq_entry_date"},
			{"Modified By", "text", "ticketfaq_modified_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "ticketfaq_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_service_faq_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				ticketfaq_id = CNumeric(PadQuotes(request.getParameter("ticketfaq_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND ticketfaq_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all FAQs!";
					StrSearch = StrSearch + " and ticketfaq_id > 0 ";
				} else if (all.equals("recent")) {
					msg = "Recent FAQs!";
					StrSearch = StrSearch + " AND ticketfaq_id > 0";
				} else if (!ticketfaq_id.equals("0")) {
					msg = "Results for FAQ ID = " + ticketfaq_id + "!";
					StrSearch = StrSearch + " and ticketfaq_id = " + ticketfaq_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND ticketfaq_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("ticketfaqstrsql", request).equals("")) {
						StrSearch = GetSession("ticketfaqstrsql", request);
					}
				}

				SetSession("ticketfaqstrsql", StrSearch, request);
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				// Check PageCurrent is valid for parse int
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending on search
				StrSql = " Select ticketfaq_id, ticketfaq_question, ticketfaq_answer, ticketfaq_active, "
						+ " faqservice_name, ticket_dept_name ";

				CountSql = " SELECT Count(distinct(ticketfaq_id))  ";

				SqlJoin = " from " + compdb(comp_id) + "axela_service_ticket_faq "
						+ " inner join " + compdb(comp_id) + "axela_service_ticket_faq_service on faqservice_id = ticketfaq_faqservice_id "
						+ " inner join " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = faqservice_ticket_dept_id "
						+ " where 1 = 1 ";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch;
					// + " GROUP BY ticketfaq_id"
					// + " ORDER BY ticketfaq_id DESC";
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
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " FAQ(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "ticket-faq-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql = StrSql + " GROUP BY ticketfaq_id"
							+ " ORDER BY ticketfaq_id DESC";
					StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}

					// SOP("StrSql = " + StrSqlBreaker(StrSql));
					StrHTML = "";
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5%>#</th>\n");
					Str.append("<th>FAQ Details</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("</tbody>\n");
					while (crs.next()) {
						String active = "";
						count = count + 1;
						if (crs.getString("ticketfaq_active").equals("0")) {
							active = " <font color=red><b>[Inactive]</b></font>";
						}
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top><span id=\"s").append(count).append("\">[+]</span> "
								+ "<a href=\"javascript:ToggleDisplay(").append(count).append(");\">").append(crs.getString("ticketfaq_question")).append("</a> [")
								.append(crs.getString("faqservice_name")).append("]" + " [").append(crs.getString("ticket_dept_name")).append("]").append(active).append("<div id=\"d").append(count)
								.append("\" style=\"display: none;\">").append(crs.getString("ticketfaq_answer")).append("");
						Str.append("</div>");
						Str.append("</td>");
						Str.append("<td valign=top align=left > " + "<a href=\"ticket-faq-update.jsp?update=yes&ticketfaq_id=").append(crs.getString("ticketfaq_id"))
								.append(" \"> Update FAQ </a>" + "<br><a href=\"ticket-faq-doc-list.jsp?ticketfaq_id=").append(crs.getString("ticketfaq_id")).append(" \">List Documents</a>"
										+ " </td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");

				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No FAQ(s) found!</font><br><br>";
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
