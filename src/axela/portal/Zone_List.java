package axela.portal;
//Murali 21st jun
//saiman 26th oct 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Zone_List extends Connect {

	public String LinkHeader = "<li><a href=home.jsp>Home</a> &gt;&nbsp;</li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp;</li><li><a href=zone-list.jsp?all=yes>List Zones</a><b>:</b></li>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=zone-update.jsp?add=yes>Add Zone...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String zone_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Zone ID", "numeric", "zone_id"},
			{"Zone Name", "text", "zone_name"},
			{
					"Entry By",
					"text",
					"zone_entry_id in (SELECT emp_id FROM " + compdb(comp_id)
							+ "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "zone_entry_date"},
			{
					"Modified By",
					"text",
					"zone_modified_id in (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "zone_modified_date"}};

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
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				zone_id = CNumeric(PadQuotes(request
						.getParameter("zone_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND zone_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Zones!";
					StrSearch = StrSearch + " and zone_id > 0";
				}

				if (!(zone_id.equals("0"))) {
					msg = msg + "<br>Results for Zone ID = " + zone_id
							+ "!";
					StrSearch = StrSearch + " and zone_id = " + zone_id
							+ "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("regionstrsql", StrSearch, request);
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

		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search

			StrSql = "SELECT zone_id, zone_name";

			CountSql = "SELECT Count(distinct zone_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_branch_zone"
					+ " WHERE 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

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
						+ " of " + TotalRecords + " Zones(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "zone-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
						PageListSize);
				StrSql = StrSql + " order by zone_id desc ";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", "
						+ recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Zone Details</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("zone_name"))
								.append(" </td>\n");
						Str.append(
								"<td><a href=\"zone-update.jsp?update=yes&zone_id=")
								.append(crs.getString("zone_id"))
								.append(" \"> Zone Update</a></td>\n");
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
				RecCountDisplay = "<br><br><br><br><font color=red>No Zone(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
