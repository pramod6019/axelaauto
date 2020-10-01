package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managefranchtype extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp; </li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp; </li><li><a href=managefranchtype.jsp?all=yes>List Franchisee Types</a><b>:</b></li>";
	public String LinkListPage = "managefranchtype.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managefranchtype-update.jsp?add=yes>Add Franchisee Type...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
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
	public String franchiseetype_id = "0";
	public String franchisee_name = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Franchisee Type ID", "numeric", "franchiseetype_id"},
			{"Franchisee Type Name", "text", "franchiseetype_name"},
			{
					"Entry By",
					"text",
					"franchiseetype_entry_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "franchiseetype_entry_date"},
			{
					"Modified By",
					"text",
					"franchiseetype_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "franchiseetype_modified_date"}};

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
				msg = PadQuotes(request.getParameter("msg"));
				franchiseetype_id = CNumeric(PadQuotes(request
						.getParameter("franchiseetype_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				all = PadQuotes(request.getParameter("all"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND franchiseetype_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Franchisee Types!";
					StrSearch = StrSearch + " and franchiseetype_id > 0";
				} else if (!(franchiseetype_id.equals("0"))) {
					msg = msg + "<br>Results for Franchisee ID = "
							+ franchiseetype_id + "!";
					StrSearch = StrSearch + " and franchiseetype_id = "
							+ franchiseetype_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("franchtypestrsql", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("franchiseePrintSearchStr", StrSearch, request);
					SetSession("franchiseeFilterStr", StrSearch, request);
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();

		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search

			StrSql = "Select franchiseetype_id, franchiseetype_name from "
					+ compdb(comp_id) + "axela_franchisee_type where 1 = 1 ";

			CountSql = "SELECT Count(distinct franchiseetype_id) from "
					+ compdb(comp_id) + "axela_franchisee_type where 1 = 1";

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
						+ " of " + TotalRecords + " Franchisee Type(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "managefranchtype.jsp?" + QueryString
						+ "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
						PageListSize);

				if (all.equals("yes")) {
					StrSql = StrSql + " order by franchiseetype_id desc";
				} else {
					StrSql = StrSql + "  order by franchiseetype_id ";
				}
				StrSql = StrSql + " limit " + (StartRec - 1) + ", "
						+ recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>Franchisee Type Details</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>")
								.append(crs.getString("franchiseetype_name"))
								.append(" </td>\n");
						Str.append(
								"<td><a href=\"managefranchtype-update.jsp?update=yes&franchiseetype_id=")
								.append(crs.getString("franchiseetype_id"))
								.append(" \">Update Franchisee Type</a></td>\n");
					}
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Franchisee(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
