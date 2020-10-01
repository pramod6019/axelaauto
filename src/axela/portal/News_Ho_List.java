package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class News_Ho_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<li><a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; </li><li> <a href=\"../portal/news.jsp\">News</a>"
			+ " &gt; </li><li><a href=\"../portal/news-ho-list.jsp\">List Head Office News</a>:</li>";
	public String LinkListPage = "../portal/news-ho-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=\"../portal/news-ho-update.jsp?add=yes\">Add News...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0";
	public String StrHTML = "";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String news_id = "0";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {{"Keyword", "text", "keyword_arr"},
			{"News ID", "numeric", "news_id"}, {"Topic", "text", "news_topic"},
			{"Desc", "text", "news_desc"}, {"Date", "date", "news_date"},
			{"Featured", "boolean", "news_featured"},
			{"Active", "boolean", "news_active"},
			{"Entry Date", "date", "news_entry_date"},
			{"Modified Date", "date", "news_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_ho_news_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND news_id = 0";
				} else if (all.equals("yes")) {
					msg = "Results for all News!";
					StrSearch += " AND news_id > 0";
				} else if (!news_id.equals("0")) {
					msg += "<br>Result for News ID: " + news_id + "!";
					StrSearch += " AND news_id = " + news_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("Honewsstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in:  "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		int TotalRecords;
		String CountSql;
		String PageURL;
		String news_desc;
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		StrSql = "SELECT news_id, news_topic, news_desc, news_date, news_featured, news_active"
				+ " FROM " + compdb(comp_id) + "axela_news_ho"
				+ " WHERE 1 = 1";

		CountSql = "SELECT "
				+ " COUNT(DISTINCT news_id) "
				+ " FROM " + compdb(comp_id) + " axela_news_ho"
				+ " WHERE 1 = 1";

		if (!StrSearch.equals("")) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " News";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "news-ho-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			if (all.equals("yes")) {
				StrSql += " ORDER BY news_date DESC";
			} else {
				StrSql += " ORDER BY news_date";
			}
			StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("\n<div class=\" \"><table class=\"table table-hover table-bordered\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>News Details</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><b>").append(crs.getString("news_topic"))
							.append(" (")
							.append(strToShortDate(crs.getString("news_date")))
							.append(")</b>");
					if (crs.getString("news_featured").equals("1")) {
						Str.append("&nbsp;<font color=\"red\"><b>[Featured]</b></font>");
					}
					if (crs.getString("news_active").equals("0")) {
						Str.append("&nbsp;<font color=\"red\"><b>[Inactive]</b></font>");
					}
					news_desc = crs.getString("news_desc");
					if (news_desc.length() > 255) {
						news_desc = news_desc.replace(news_desc.substring(255),
								"...");
					}
					Str.append("<br>").append(news_desc).append("<br><br>");
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append(
							"<a href=\"news-ho-update.jsp?update=yes&news_id=")
							.append(crs.getString("news_id"))
							.append(" \"> Update News</a>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in:  "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No News found!</font><br><br>";
		}
		return Str.toString();
	}
}
