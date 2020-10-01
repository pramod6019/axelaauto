// Bhagwan Singh (10 july 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageInteriorColour extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageinteriorcolour.jsp?all=yes>List Interior Colours</a><b>:</b>";
	public String LinkExportPage = "colour-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=manageinteriorcolour-update.jsp?add=yes>Add New Interior Colour...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String QueryString = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public int recperpage = 0;
	public String RecCountDisplay = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public int PageSize = 0;
	public String PageURL = "";
	public String PageNaviStr = "";
	public String PageCurrents = "";
	public String intcolour_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String all = "";
	public String smart = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Colour ID", "numeric", "intcolour_id"},
			{"Name", "text", "intcolour_name"},
			{"Entry By", "text", "intcolour_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "intcolour_entry_date"},
			{"Modified By", "text", "intcolour_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "intcolour_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				intcolour_id = CNumeric(PadQuotes(request.getParameter("intcolour_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND intcolour_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Interior Colours!";
					StrSearch = StrSearch + " AND intcolour_id > 0";
				} else if (!(intcolour_id.equals("0"))) {
					msg = msg + "<br>Results for Interior Colour ID = " + intcolour_id + "!";
					StrSearch = StrSearch + " AND intcolour_id = " + intcolour_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND intcolour_id = 0";
					} else {
						msg = "Results for Search!";
					}
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT intcolour_id, intcolour_name";

			SqlJoin = " FROM axela_preowned_intcolour"
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT intcolour_id)";

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

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Interior Colour(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "manageinteriorcolour.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY intcolour_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Interior Colour</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>");
						Str.append(crs.getString("intcolour_name")).append("</td>\n");
						Str.append("<td valign=top><a href=\"manageinteriorcolour-update.jsp?update=yes&intcolour_id=");
						Str.append(crs.getString("intcolour_id")).append("\">Update Interior Colour</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><center><b><font color=red>No Interior Colour(s) Found!</font></b></center><br><br>");
			}
		}
		return Str.toString();
	}
}
