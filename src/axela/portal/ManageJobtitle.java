package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobtitle extends Connect {
	// ///// List page links

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp;</li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp;</li><li><a href=managejobtitle.jsp?all=yes>List Job Titles</a><b>:</b></li>";
	public String LinkListPage = "managejobtitle.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managejobtitle-update.jsp?add=yes>Add Job Title...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
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
	public String strq = "";
	public String jobtitle_id = "0";
	public String all = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Jobtitle ID", "numeric", "jobtitle_id"},
			{"Desc", "text", "jobtitle_desc"},
			{
					"Entry By",
					"text",
					"jobtitle_entry_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "jobtitle_entry_date"},
			{
					"Modified By",
					"text",
					"jobtitle_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "jobtitle_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				jobtitle_id = CNumeric(PadQuotes(request
						.getParameter("jobtitle_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND jobtitle_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Job Titles!";
					StrSearch = StrSearch + " and jobtitle_id > 0";
				} else if (!(jobtitle_id.equals("0"))) {
					msg = msg + "<br>Results for Job Title ID = " + jobtitle_id
							+ "!";
					StrSearch = StrSearch + " and jobtitle_id = " + jobtitle_id
							+ "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("jobtitlestrsql", StrSearch, request);
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

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "Select jobtitle_id, jobtitle_desc from " + compdb(comp_id)
				+ "axela_jobtitle where 1 = 1";
		CountSql = "SELECT Count(distinct jobtitle_id) FROM " + compdb(comp_id)
				+ "axela_jobtitle where 1 = 1";

		if (!(StrSearch.equals(""))) {
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
					+ " of " + TotalRecords + " Job Title(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "managejobtitle.jsp?" + strq + QueryString
					+ "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " order by jobtitle_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Job Title Details</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					Str.append("<td>").append(crs.getString("jobtitle_desc"))
							.append("</td>\n");
					Str.append(
							"<td align=left> "
									+ " <a href=\"managejobtitle-update.jsp?update=yes&jobtitle_id=")
							.append(crs.getString("jobtitle_id"))
							.append(" \">Update Job Title</a></td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Job Title (s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
