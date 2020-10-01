package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class News_List extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt; </li><li><a href=../portal/news-list.jsp?all=yes> List News </a>:</li>";
	public String LinkListPage = "../portal/news-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String search = "";
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
	public String smart = "";
	public String news_id = "0", honews_id = "0", branch_id = "0",
			BranchAccess = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {{"Keyword", "text", "keyword_arr"},
			{"News ID", "numeric", "news_id"},
			{"News Topic", "text", "news_topic"},
			{"News Description", "text", "news_desc"},
			{"News Date", "date", "news_date"},
			{"News Featured", "boolean", "news_featured"},
			{"News Active", "boolean", "news_active"},
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
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
				search = PadQuotes(request.getParameter("search_button"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
				honews_id = CNumeric(PadQuotes(request
						.getParameter("honews_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND news_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all News!";
					StrSearch = StrSearch + " AND news_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("newsstrsql", request).equals("")) {
						StrSearch = StrSearch
								+ GetSession("newsstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("newsstrsql", StrSearch, request);
				}

				StrHTML = ListNews();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String ListNews() {
		StringBuilder Str = new StringBuilder();
		String news_desc;
		int TotalRecords = 0;
		String CountSql = "";
		String SqlJoin = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		if (branch_id.equals("0")) {
			SqlJoin = "Select news_id, news_topic, news_desc, news_date, 0 as news_branch_id,"
					+ " 'Head Office' as branch_name"
					+ " from "
					+ compdb(comp_id)
					+ "axela_news_ho"
					+ " where news_active = '1'" + " Union";
		}
		SqlJoin = SqlJoin
				+ " Select news_id, news_topic, news_desc, news_date, news_branch_id,"
				+ " branch_name" + " from " + compdb(comp_id)
				+ "axela_news_branch" + " inner join " + compdb(comp_id)
				+ "axela_branch on branch_id = news_branch_id"
				+ " where news_active = '1'" + BranchAccess;

		StrSql = "Select * from (" + SqlJoin + ") as news where 1=1";

		CountSql = "SELECT Count(news_id) from (" + SqlJoin
				+ ") as news where 1=1";

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + " " + StrSearch;
			CountSql = CountSql + " " + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		recperpage = Integer
				.parseInt(ExecuteQuery("Select emp_recperpage from "
						+ compdb(comp_id) + "axela_emp" + " where emp_id = "
						+ emp_id + ""));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " News(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "news-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " order by news_date desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";
			CachedRowSet crs =processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;
				Str.append("\n<div class=\" \"> <table class=\"table table-bordered\">");
				Str.append("<tr></td>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>News Details</th>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>");
					if (crs.getString("news_branch_id").equals("0")) {
						Str.append("<a href=news-summary.jsp?honews_id="
								+ crs.getString("news_id") + " target=_balnk>");
						Str.append(crs.getString("news_topic") + " ("
								+ strToShortDate(crs.getString("news_date"))
								+ ")</a>");
					} else if (!crs.getString("news_branch_id").equals("0")) {
						Str.append("<a href=news-summary.jsp?news_id="
								+ crs.getString("news_id") + " target=_blank>");
						Str.append(crs.getString("news_topic") + " ("
								+ strToShortDate(crs.getString("news_date"))
								+ ")</a>");
					}
					// if (crs.getString("news_branch_id").equals("0")) {
					// Str.append("<a href=news-summary.jsp?news_id=").append(crs.getString("news_id")).append(" target=homemain>").append(crs.getString("news_topic")).append(" (").append(crs.getString("news_id")).append(") (").append(strToShortDate(crs.getString("news_date"))).append(")</a>");
					news_desc = crs.getString("news_desc");
					if (news_desc.length() > 255) {
						news_desc = news_desc.replace(news_desc.substring(255),
								"...");
					}
					Str.append("<br>").append(news_desc)
							.append("<br><br></td>\n");
					Str.append(
							"<td><a href=\"../portal/branch-summary.jsp?branch_id=")
							.append(crs.getInt("news_branch_id")).append("\">")
							.append(crs.getString("branch_name"))
							.append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</td>\n");
				Str.append("</table></table>");

				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		} else {
			Str.append("<font color=red><b>No News found!</b></font>");
		}
		return Str.toString();
	}
}
