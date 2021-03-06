package axela.inventory;
//aJIt 20th March, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_Stock_Location extends Connect {

	// ///// List page links
	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manage-stock-location.jsp?all=yes>List Stock Locations</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manage-stock-location-update.jsp?Add=yes>Add Stock Location..</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String vehstocklocation_id = "0";
	public String vehstocklocation_name = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Location ID", "numeric", "vehstocklocation_id"},
			{"Branch ID", "numeric", "vehstocklocation_branch_id"},
			{"Name", "text", "vehstocklocation_name"},};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				vehstocklocation_id = CNumeric(PadQuotes(request.getParameter("vehstocklocation_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehstocklocation_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Stock Locations!";
					StrSearch = StrSearch + " and vehstocklocation_id > 0";
				} else if (!(vehstocklocation_id.equals("0"))) {
					msg = msg + "<br>Results for Stock Location = " + vehstocklocation_id + "!";
					StrSearch = StrSearch + " and vehstocklocation_id = " + vehstocklocation_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND vehstocklocation_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("stocklocationstrsql", StrSearch, request);
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
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			if (!msg.equals("")) {
				if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				if (!msg.equals("")) {
					StrSql = "SELECT vehstocklocation_id, vehstocklocation_name,"
							+ " branch_id,"
							+ " COALESCE(concat(branch_name, ' (',branch_code,')'),'') AS branch_name";

					StrJoin = " FROM " + compdb(comp_id) + "axela_vehstock_location"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = vehstocklocation_branch_id"
							+ " WHERE 1 = 1 ";

					CountSql = "SELECT COUNT(DISTINCT(vehstocklocation_id))";

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
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Location(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "manage-stock-location.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						// StrSql = StrSql + StrSearch;
						if (all.equals("yes")) {
							StrSql = StrSql + " GROUP BY vehstocklocation_id ORDER BY vehstocklocation_id desc";
						} else {
							StrSql = StrSql + " GROUP BY vehstocklocation_id ORDER BY vehstocklocation_id";
						}
						StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

						try {
							CachedRowSet crs = processQuery(StrSql, 0);
							int count = StartRec - 1;
							Str.append("<div class=\"table-responsive table-bordered\">\n");
							Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
							Str.append("<thead><tr>\n");
							Str.append("<th data-hide=\"phone\">#</th>\n");
							Str.append("<th data-toggle=\"true\">ID</th>\n");
							Str.append("<th>Location</th>\n");
							Str.append("<th data-hide=\"phone\">Branch</th>\n");
							Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
							Str.append("</tr>\n");
							Str.append("</thead>\n");
							Str.append("<tbody>\n");
							while (crs.next()) {
								count = count + 1;
								Str.append("<tr>\n");
								Str.append("<td valign=top align=center>").append(count).append("</td>\n");
								Str.append("<td valign=top align=center>").append(crs.getString("vehstocklocation_id")).append("</td>\n");
								Str.append("<td valign=top>").append(crs.getString("vehstocklocation_name")).append("</td>");
								Str.append("<td valign=top>");
								if (!crs.getString("branch_name").equals(""))
								{
									Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">");
									Str.append(crs.getString("branch_name")).append("</a>");
								}
								Str.append("</td>");
								Str.append("<td valign=top nowrap><a href=\"manage-stock-location-update.jsp?Update=yes&vehstocklocation_id=");
								Str.append(crs.getString("vehstocklocation_id")).append(" \">Update Stock Location</a></td>\n");
							}
							Str.append("</tr>\n");
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
						RecCountDisplay = "<br><br><br><br><font color=red>No Stock Location(s) Found!</font><br><br>";
					}
				}
			}
		}
		return Str.toString();
	}
}
