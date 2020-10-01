package axela.service;

//@Sri Venkatesh 03 apr 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageLocation extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=managelocation.jsp?all=yes>List Service Locations</a><b>:</b><br>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managelocation-update.jsp?add=yes>Add Location...</a>";
	public String LinkPrintPage = "";
	public String advhtml = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String advSearch = "";
	public String location_id = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Location ID", "numeric", "location_id"},
			{"Branch ID", "numeric", "location_branch_id"},
			{"Location Name", "text", "location_name"},
			{"Lead Time", "numeric", "location_leadtime"},
			{"Inspection Duration", "numeric", "location_inspection_dur"},
			{"Entry By", "text", "location_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "location_entry_date"},
			{"Modified By", "text", "location_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "location_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND location_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Locations!";
					StrSearch = StrSearch + " and location_id > 0";
				}
				if (!(location_id.equals("0"))) {
					msg = msg + "<br>Results for Location ID = " + location_id + "!";
					StrSearch = StrSearch + " and location_id = " + location_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " and location_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("locationstrsql", StrSearch, request);
				}
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
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			if (!msg.equals("")) {
				StrSql = "SELECT location_id, location_name, location_branch_id, branch_id,"
						+ " CONCAT(branch_name, '(', branch_code, ')') AS branch_name";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_location"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
						+ " WHERE 1 = 1";

				CountSql = "SELECT COUNT(DISTINCT location_id)";

				StrSql = StrSql + SqlJoin + StrSearch;
				CountSql = CountSql + SqlJoin + StrSearch;

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
					PageURL = "managelocation.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " GROUP BY location_id"
							+ " ORDER BY location_name DESC"
							+ " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					try {
						CachedRowSet crs = processQuery(StrSql, 0);
						int count = StartRec - 1;
						Str.append("<div class=\" table-bordered\">\n");
						Str.append("<table class=\"table table-hover sticky-header \" data-filter=\"#filter\">\n");
						Str.append("<thead style=\"margin-top:50px;\">");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th data-hide=\"phone\">Location Details</th>\n");
						Str.append("<th data-hide=\"phone\">Branch</th>\n");
						Str.append("<th data-hide=\"phone\">Actions</th>\n");
						Str.append("</tr>");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");

						while (crs.next()) {
							count++;

							Str.append("<tr align=left valign=top>\n");
							Str.append("<td align=center>").append(count).append("</td>");

							Str.append("<td align=center>").append(crs.getString("location_name")).append("</td>\n");
							Str.append("<td align=center>").append("<a href=\"../portal/branch-summary.jsp?branch_id=")
									.append(crs.getString("location_branch_id")).append("\">")
									.append(crs.getString("branch_name")).append("</a>").append("</td>\n");
							Str.append("<td align=center>").append("<a href=\"managelocation-update.jsp?update=yes&location_id=").append(crs.getString("location_id"))
									.append("\">Update Location</a>").append("</td>\n");

							Str.append("</tr>");

							// Str.append("<tr>\n");
							// Str.append("<td valign=\"top\" align=\"center\">").append(count);
							// Str.append("</td>\n<td valign=\"top\">").append(crs.getString("location_name"));
							// Str.append("</td>\n<td valign=\"top\">");
							// Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("location_branch_id")).append("\">");
							// Str.append(crs.getString("branch_name")).append("</a>");
							// Str.append("</td>\n<td height=\"20\" valign=\"top\">");
							// Str.append("<a href=\"managelocation-update.jsp?update=yes&location_id=").append(crs.getString("location_id")).append("\">Update Location</a>");
							// Str.append("</td>\n</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");

						crs.close();
						Str.append("</table>\n");
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Location(s) Found!</font><br><br>";
				}
			}
		}
		return Str.toString();
	}
}
