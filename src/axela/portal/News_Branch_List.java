package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class News_Branch_List extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt; </li><li><a href=../portal/news.jsp>News</a> &gt; </li><li><a href=../portal/news-branch-list.jsp?all=yes>List Branch News</a>:</li>";
	public String LinkListPage = "../portal/news-branch-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=../portal/news-branch-update.jsp?add=yes>Add News...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0", BranchAccess = "";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
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
	public String search = "";
	public String news_id = "0", news_branch_id = "0";
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
				CheckPerm(comp_id, "emp_branch_news_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search_button"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND news_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all News!";
					StrSearch = StrSearch + " and news_id > 0";
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
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "", SqlJoin;
		String PageURL = "";
		String news_desc = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = " SELECT "
				+ " COALESCE(concat(branch_name,' (', branch_code, ')'),'All Branch') AS branchname, "
				+ " COALESCE(branch_name, '') AS branch_name, "
				+ " news_id, news_branch_id,news_topic,news_desc,news_date, news_featured, news_active";
		CountSql = " SELECT COUNT(distinct news_id)";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_news_branch "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id"
				+ " WHERE 1=1 ";

		StrSql = StrSql + SqlJoin + StrSearch;
		CountSql = CountSql + SqlJoin + StrSearch;
		// SOP("strsql---" + StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " News";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "news-branch-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " order by news_date desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";
			CachedRowSet crs = processQuery(StrSql, 0);

			try {
				int count = StartRec - 1;

				Str.append("<div class=\"table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>News Details</th>\n");
				Str.append("<th data-hide=\"phone\">Branch</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><b>").append(crs.getString("news_topic"))
							.append(" (").append(crs.getString("news_id"))
							.append(") (")
							.append(strToShortDate(crs.getString("news_date")))
							.append(")</b>");
					if (crs.getString("news_featured").equals("1")) {
						Str.append("&nbsp;<font color=red><b>[Featured]</b></font>");
					}
					if (crs.getString("news_active").equals("0")) {
						Str.append("&nbsp;<font color=red><b>[Inactive]</b></font>");
					}
					news_desc = crs.getString("news_desc");
					if (news_desc.length() > 255) {
						news_desc = news_desc.replace(news_desc.substring(255),
								"...");
					}
					Str.append("<br>").append(news_desc)
							.append("<br><br></td>");
					Str.append("<td>");
					if (crs.getInt("news_branch_id") != 0) {
						Str.append(
								"<a href=\"../portal/branch-summary.jsp?branch_id=")
								.append(crs.getInt("news_branch_id"))
								.append("\">")
								.append(crs.getString("branchname"))
								.append("</a>");
					} else {
						Str.append(crs.getString("branchname"));
					}
					Str.append("</td>");
					Str.append(
							"<td><a href=\"news-branch-update.jsp?update=yes&news_id=")
							.append(crs.getString("news_id"))
							.append("\">Update News</a></td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</table>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
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
