package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageCity extends Connect {

	public String LinkHeader = "<li><a href=home.jsp>Home</a> &gt;&nbsp; </li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp </li><li> <a href=managecity.jsp?all=yes>List Cities</a><b>:</b></li>";
	public String LinkListPage = "managecity.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managecity-update.jsp?Add=yes>Add City...</a>";
	public String ExportPerm = "";
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
	public String city_id = "0";
	public String all = "";
	public String state_id = "0";
	public String city_name = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"City ID", "numeric", "city_id"},
			{"Name", "text", "city_name"},
			{
					"Entry By",
					"text",
					"city_entry_id IN (SELECT emp_id FROM " + compdb(comp_id)
							+ "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "city_entry_date"},
			{
					"Modified By",
					"text",
					"city_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "city_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_role_id, emp_service_jobcard_add", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				city_id = CNumeric(PadQuotes(request.getParameter("city_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND city_id = 0";
				}
				if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Cities!";
					StrSearch = StrSearch + " and city_id > 0";
				}

				if (!city_id.equals("0")) {
					msg = msg + "<br>Results for City ID = " + city_id + "!";
					StrSearch = StrSearch + " and city_id = " + city_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("citystrsql", StrSearch, request);
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "Select city_id, city_name, " + compdb(comp_id)
				+ "axela_state.state_id, " + compdb(comp_id)
				+ "axela_state.state_name";

		StrJoin = " from " + compdb(comp_id) + "axela_city" + " inner join "
				+ compdb(comp_id) + "axela_state on city_state_id = state_id";

		CountSql = " SELECT Count(distinct city_id)";

		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;
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
					+ " of " + TotalRecords + " City(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "managecity.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " group by city_id order by city_name desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>City</th>\n");
				Str.append("<th>State</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=managecity.jsp?city_id=")
							.append(crs.getString("city_id")).append(">");
					Str.append(crs.getString("city_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=managestate.jsp?state_id=").append(
							crs.getString("state_id"));
					Str.append(">").append(crs.getString("state_name"))
							.append("</a>");
					Str.append("</td>\n");
					Str.append("<td align=left'>");
					Str.append(
							"<a href=\"managecity-update.jsp?Update=yes&city_id=")
							.append(crs.getString("city_id"))
							.append(" \">Update City</a>");
					Str.append("</td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No City(s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
