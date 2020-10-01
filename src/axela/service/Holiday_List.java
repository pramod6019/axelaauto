//Dilip
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Holiday_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../service/index.jsp>Service</a>"
			+ " &gt; <a href=ticket.jsp>Tickets</a>"
			+ " &gt; <a href=holiday-list.jsp?all=yes>List Holidays</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=holiday-update.jsp?add=yes>Add New Holiday...</a>";
	public String ExportPerm = "";
	public String ticketholi_id = "";
	public String BranchAccess = "";
	public String msg = "";
	public String QueryString = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String all = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String smart = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Holiday ID", "numeric", "ticketholi_id"},
			{"Holiday Name", "text", "ticketholi_name"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "concat(branch_name,branch_code)"},
			{"Holiday Date", "date", "ticketholi_date"},
			{"Entry By", "text", "ticketholi_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "ticketholi_entry_date"},
			{"Modified By", "text", "ticketholi_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "ticketholi_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_ticket_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				ticketholi_id = CNumeric(PadQuotes(request.getParameter("ticketholi_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND ticketholi_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Holidays!";
					StrSearch = StrSearch + " AND ticketholi_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Holidays!";
					StrSearch = StrSearch + " AND ticketholi_id > 0";
				} else if (!ticketholi_id.equals("0")) {
					msg = msg + "<br>Result for Holiday ID = " + ticketholi_id + "!";
					StrSearch = StrSearch + " AND ticketholi_id = " + ticketholi_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search Text!";
						StrSearch = StrSearch + " AND ticketholi_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("holidaystrsql", request).equals("")) {
						StrSearch = GetSession("holidaystrsql", request);
					}
				}
				StrSearch += BranchAccess;

				SetSession("holidaystrsql", StrSearch, request);
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		CachedRowSet crs = null;
		int StartRec = 0;
		int EndRec = 0;
		int PageListSize = 10;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT ticketholi_id, ticketholi_name, branch_id, ticketholi_date,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname";

				CountSql = "SELECT COUNT(DISTINCT ticketholi_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket_holi"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticketholi_branch_id"
						+ " WHERE 1 = 1 ";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY ticketholi_id"
							+ " ORDER BY ticketholi_id DESC";
					CountSql += StrSearch;
				}
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
					crs.close();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Holiday(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "holiday-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}

					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th width=\"5%\">ID</th>\n");
					Str.append("<th>Name</th>\n");
					Str.append("<th data-hide=\"phone\">Branch</th>\n");
					Str.append("<th data-hide=\"phone\">Date</th>\n");
					Str.append("<th width=\"20%\" data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("ticketholi_id"));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">\n");
						Str.append(crs.getString("ticketholi_name"));
						Str.append("</td>\n<td valign=\"top\">\n");
						Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">");
						Str.append(crs.getString("branchname")).append("</b></a>");
						Str.append("</td>\n<td valign=\"top\" align=\"center\">\n");
						Str.append(strToShortDate(crs.getString("ticketholi_date")));
						Str.append("</td>\n<td valign=\"top\" nowrap>");
						Str.append("<a href=\"holiday-update.jsp?update=yes&ticketholi_id=").append(crs.getString("ticketholi_id"));
						Str.append("\">Update Holiday</a></td>\n</tr>\n");
					}

					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");

				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Holiday(s) Found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
