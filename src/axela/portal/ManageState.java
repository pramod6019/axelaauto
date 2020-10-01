package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageState extends Connect {

	public String LinkHeader = "<li><a href=home.jsp>Home</a> &gt;&nbsp; </li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp; </li><li><a href=managestate.jsp?all=yes>List States</a><b>:</b></li>";
	public String LinkListPage = "managestate.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managestate-update.jsp?Add=yes>Add State...</a>";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String go = "";
	public String msg = "";
	public String all = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String state_id = "0";
	public String CountSql = "";
	public String PageURL = "";
	public String StrJoin = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"State ID", "numeric", "state_id"},
			{"Name", "text", "state_name"},
			{
					"Entry By",
					"text",
					"state_entry_id IN (SELECT emp_id FROM " + compdb(comp_id)
							+ "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "state_entry_date"},
			{
					"Modified By",
					"text",
					"state_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "state_modified_date"}};

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
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				state_id = CNumeric(PadQuotes(request.getParameter("state_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				int duehours = 26;
				new Branch_List4().DueTime("20130318150000", 9, 18, duehours,
						"1", "0", "0", "0", "0", "0", "0");

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND state_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All States!";
					StrSearch = StrSearch + " and state_id > 0";
				} else if (!state_id.equals("0")) {
					msg = msg + "<br>Results for State ID = " + state_id + "!";
					StrSearch = StrSearch + " and state_id = " + state_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("statestrsql", StrSearch, request);
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
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			//
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search
			StrSql = "SELECT state_id, state_name,"
					+ " (select count(city_id) from " + compdb(comp_id)
					+ "axela_city where city_state_id = state_id) as citycount";

			StrJoin = " FROM " + compdb(comp_id) + "axela_state"
					+ " where 1 = 1";
			CountSql = "SELECT Count(distinct state_id)";

			StrSql = StrSql + StrJoin;
			CountSql = CountSql + StrJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP(StrSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				// SOP("StartRec==" + StartRec);
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
						+ " of " + TotalRecords + " State(s)";

				// SOP("RecCountDisplay---" + RecCountDisplay);
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "managestate.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
						PageListSize);
				StrSql = StrSql + " order by state_name desc ";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", "
						+ recperpage + "";
				// SOP("StrSql==" + StrSqlBreaker(StrSql));
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>State Details</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=managestate.jsp?state_id=").append(
								crs.getString("state_id"));
						Str.append(">").append(crs.getString("state_name"))
								.append("</a>");
						Str.append(" [").append(crs.getString("citycount"))
								.append(" Cities]</td>\n");
						Str.append("<td align=left'>");
						Str.append(
								"<a href=\"managestate-update.jsp?Update=yes&state_id=")
								.append(crs.getString("state_id"))
								.append(" \">Update State</a>");
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
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No State(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
