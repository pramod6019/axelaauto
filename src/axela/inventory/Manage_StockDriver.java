// Smitha Nag (16 March 2013)
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_StockDriver extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manage-stockdriver.jsp?all=yes>List Stock Drivers</a><b>:</b>";
	public String LinkExportPage = "colour-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=manage-stockdriver-update.jsp?add=yes>Add Stock Driver...</a>";
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
	public String stockdriver_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String all = "", active = "";
	public String smart = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Driver Id", "numeric", "stockdriver_id"},
			{"Name", "text", "stockdriver_name"},
			// {"Active", "boolean", "driver_active"},
			{"Entry By", "numeric", "driver_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "driver_entry_date"},
			{"Modified By", "numeric", "driver_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "driver_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				stockdriver_id = CNumeric(PadQuotes(request.getParameter("stockdriver_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehstockdriver_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Stock Drivers!";
					StrSearch = StrSearch + " and vehstockdriver_id > 0";
				} else if (!(stockdriver_id.equals("0"))) {
					msg = msg + "<br>Results for Stock Driver ID = " + stockdriver_id + "!";
					StrSearch = StrSearch + " and vehstockdriver_id = " + stockdriver_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
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

			StrSql = "SELECT vehstockdriver_id, vehstockdriver_name,vehstockdriver_active";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_vehstock_driver"
					+ " WHERE 1=1";
			CountSql = "SELECT COUNT(DISTINCT vehstockdriver_id)";

			StrSql = StrSql + SqlJoin;
			// SOP("query......." + StrSql);
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

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock Driver(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "manage-stockdriver.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by vehstockdriver_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String active = "";
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th>Stock Driver Details</th>\n");
					Str.append("<th data-toggle=\"true\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						if (crs.getString("vehstockdriver_active").equals("0")) {
							active = "<b><font color=\"red\"> [Inactive]</font></b>";
						} else {
							active = "";
						}
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>");
						Str.append(crs.getString("vehstockdriver_name")).append(" (");
						Str.append(crs.getString("vehstockdriver_id")).append(")").append(active);
						Str.append("</td>\n");
						Str.append("<td valign=top><a href=\"manage-stockdriver-update.jsp?update=yes&stockdriver_id=");
						Str.append(crs.getString("vehstockdriver_id")).append("\">Update Stock Driver</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");

					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Stock Driver(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
