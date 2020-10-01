package axela.service;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_VehSource extends Connect {

	public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manage-vehsource.jsp?all=yes>List Vehicle Source</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=../service/manage-vehsource-update.jsp?add=yes>Add Vehicle Source</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
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
	public String vehsource_id = "0";
	public String vehsource_name = "";
	public String advSearch = "";
	// public int vehicle source ID =0;
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "vehsource_id"},
			{"Name", "text", "vehsource_name"},
			{"Entry By", "text", "vehsource_entry_id in (select emp_id from axela_emp where emp_name"},
			{"Entry Date", "date", "vehsource_entry_date"},
			{"Modified By", "text", "vehsource_modified_id in (select emp_id from axela_emp where emp_name"},
			{"Modified Date", "date", "vehsource_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				vehsource_id = CNumeric(PadQuotes(request.getParameter("vehsource_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehsource_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Vehicle Source(s)!";
					StrSearch = StrSearch + " and vehsource_id > 0";
				}

				if (!(vehsource_id.equals("0"))) {
					msg = msg + "<br>Results for Vehicle Source ID = " + vehsource_id + "!";
					StrSearch = StrSearch + " and vehsource_id = " + vehsource_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				// if (!StrSearch.equals("")) {
				// SetSession("vehsrcstrsql", StrSearch, request);
				// }
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();

		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "Select vehsource_id, vehsource_name";

			CountSql = "SELECT Count(distinct vehsource_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh_source"
					+ " WHERE 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;

			}

			// SOP("CountSql========" + CountSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Vehicle Source(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "manage-vehsource.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " GROUP BY vehsource_id "
						+ " ORDER BY vehsource_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th width=5% data-toggle=\"true\"><span class=\"footable-toggle\"></span>#</th>\n");
					Str.append("<th>Vehicle Source Details</th>\n");
					Str.append("<th width = 20%>Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("vehsource_name")).append(" </td>\n");
						Str.append("<td valign=top align=left ><a href=\"manage-vehsource-update.jsp?update=yes&vehsource_id=").append(crs.getString("vehsource_id"))
								.append(" \">Update Vehicle Source</a></td>\n");
					}
					crs.close();
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Vehicle Source(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
