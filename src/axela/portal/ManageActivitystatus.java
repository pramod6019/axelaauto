package axela.portal;

/**
 * Gurumurthy TS 10 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageActivitystatus extends Connect {

	public String LinkHeader = "<li><a href=home.jsp>Home</a> &gt;&nbsp </li><li><a href=manager.jsp>Business Master</a> &gt;&nbsp </li><li><a href=manageactivitystatus.jsp?all=yes>List Activity Status<b>:</b></a></li>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageactivitystatus-update.jsp?add=yes>Add Activity Status...</a>";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String search = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String status_id = "0";
	public String all = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Status ID", "numeric", "status_id"},
			{"Status Name", "text", "status_desc"},
			{
					"Entry By",
					"text",
					"status_entry_id in (select emp_id from " + compdb(comp_id)
							+ "axela_emp where emp_name"},
			{"Entry Date", "date", "status_entry_date"},
			{
					"Modified By",
					"text",
					"status_modified_id in (select emp_id from "
							+ compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "status_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search_button"));
				msg = PadQuotes(request.getParameter("msg"));
				status_id = CNumeric(PadQuotes(request
						.getParameter("status_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND status_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Activity Status(es)!";
					StrSearch = StrSearch + " and status_id > 0";
				} else if (!(status_id.equals("0"))) {
					msg = msg + "<br>Results for Activity Status ID = "
							+ status_id + "!";
					StrSearch = StrSearch + " and status_id = " + status_id
							+ "";
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if ((PageCurrents == null) || (PageCurrents.length() < 1)
					|| isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "Select status_id,status_desc";

			CountSql = "SELECT Count(distinct status_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_activity_status"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			// SOP(StrSql);
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
						+ " of " + TotalRecords + " Activity Status(es)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "manageactivitystatus.jsp?" + QueryString
						+ "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
						PageListSize);
				StrSql = StrSql + " order by status_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", "
						+ recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Activity Status Details</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("status_desc"))
								.append("</td>\n");
						Str.append(
								"<td><a href=\"manageactivitystatus-update.jsp?update=yes&status_id=")
								.append(crs.getString("status_id"))
								.append(" \">Update Activity Status</a></td>\n");
						Str.append("</tr>\n");
					}
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
				Str.append("<br><br><br><br><font color=red>No Active Status(s) Found!</font><br><br>");
			}
		}
		return Str.toString();
	}
}
