package axela.portal;

/** @saiman 21st june 2012 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Faqexecutivecat_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/faq.jsp>FAQ</a> &gt; <a href=../portal/faqexecutivecat-list.jsp?all=yes>List Executive Categories</a>:";
	public String LinkListPage = "../portal/faqexecutivecat-list.jsp";
	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkFilterPage = "lms-filter.jsp";
	public String LinkAddPage = "<a href=../portal/faqexecutivecat-update.jsp?Add=yes>Add New Category...</a>";
	public String ExportPerm = "";
	public String BranchAccess = "";
	public String branch_id = "0";
	public String StrHTML = "";
	public String emp_id = "0";
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
	public String smart = "";
	private String cat_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Category ID", "numeric", "cat_id"},
			{"Name", "text", "cat_name"},
			{"Entry Date", "date", "cat_entry_date"},
			{"Modified Date", "date", "cat_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_faq_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND cat_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Categories!";
					StrSearch = StrSearch + " and cat_id > 0 ";
				} else if (!(cat_id.equals("0"))) {
					msg = msg + "<br>Results for Category!";
					StrSearch = StrSearch + " and cat_id =" + CNumeric(cat_id) + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("rackstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("rackstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("rackPrintSearchStr", StrSearch, request);
					SetSession("rackFilterStr", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("rackstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
		String StrJoin = "";
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		StrSql = " select cat_id,cat_name ";
		StrJoin = " from " + compdb(comp_id) + "axela_faq_cat "
				+ " where 1=1 ";
		CountSql = "SELECT Count(distinct cat_id)  ";

		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		CountSql = ExecuteQuery(CountSql);
		if (!CountSql.equals("")) {
			TotalRecords = Integer.parseInt(CountSql);
		} else {
			TotalRecords = 0;
		}
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
			PageURL = "faqexecutivecat-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (all.equals("yes")) {
				StrSql = StrSql + " group by cat_id order by cat_id desc ";
			} else {
				StrSql = StrSql + " group by cat_id  ";
			}
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Categories</th>\n");
				Str.append("<th >Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("cat_name")).append("");
					Str.append("</td>");
					Str.append("<td valign=top>" + "<a href=\"faqexecutivecat-update.jsp?Update=yes&cat_id=").append(crs.getString("cat_id")).append(" \">Update Category</a>");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Category found!</font><br><br>";
		}
		return Str.toString();
	}
}
