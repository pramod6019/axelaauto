package axela.app;
//@Shilpashree 03 oct 2015

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Demo;
import cloudify.connect.Smart;

public class ShowRoom_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../app/home.jsp>App</a>"
			+ " &gt; Showroom "
			+ " &gt; <a href=../app/showroom-list.jsp?all=yes>List Showrooms</a>:";
	public String LinkExportPage = "showroom-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../app/showroom-update.jsp?add=yes>Add New Showroom </a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String StrHTML = "";
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
	public String all = "";
	public String showroom_id = "0";
	public String comp_id = "0";
	public String homefilter = "", fromdate = "", todate = "";
	// CachedowSet parameter
	Map<Integer, Object> map = new HashMap<>();
	public int mapkey = 0;
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Showroom ID", "numeric", "showroom_id"},
			{"Showroom Name", "text", "showroom_name"},
			{"Address", "text", "showroom_address"},
			{"City", "text", "showroom_city_id IN (SELECT city_id FROM compdb.axela_city WHERE city_name"},
			{"Pin", "text", "showroom_pin"},
			{"Latitude", "numeric", "showroom_latitude"},
			{"Longitude", "text", "showroom_longitude"},
			{"Active", "boolean", "showroom_active"},
			{"Entry By", "text", "showroom_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "showroom_entry_date"},
			{"Modified By", "text", "showroom_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "showroom_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = GetSession("emp_id", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request) + "");
				CheckPerm(comp_id, "emp_role_id", request, response);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				showroom_id = CNumeric(PadQuotes(request.getParameter("showroom_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				homefilter = PadQuotes(request.getParameter("homefilter"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND showroom_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Showrooms!";
				} else if (!showroom_id.equals("0")) {
					msg += "<br>Result for Showroom ID = " + showroom_id + "!";
					StrSearch += " AND showroom_id = ?";
					mapkey++;
					map.put(mapkey, showroom_id);
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND showroom_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg += "<br>Results for Search!";
					if (session.getAttribute("showroomstrsql") != null) {
						StrSearch += session.getAttribute("showroomstrsql");
					}
				}
				if (homefilter.equals("yes")) {
					fromdate = PadQuotes(request.getParameter("from"));
					todate = PadQuotes(request.getParameter("to"));
					fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
					todate = ConvertShortDateToStr(todate).substring(0, 8);
					StrSearch += " AND (SUBSTR(showroom_entry_date, 1,8) >= '" + fromdate + "'"
							+ " AND SUBSTR(showroom_entry_date, 1,8) <= '" + todate + "')";
					msg = "Results for Showrooms!";
				}

				session.setAttribute("showroomstrsql", StrSearch);
				StrHTML = ListData();
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

	public String ListData() {
		int TotalRecords;
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		String PageURL;
		String address;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("")) {
					StrSql = "SELECT showroom_id, CAST(showroom_name AS CHAR) AS showroom_name,"
							+ " CAST(showroom_address AS CHAR) AS showroom_address,"
							+ " showroom_city_id, CAST(state_name AS CHAR) AS state_name,"
							+ " CAST(showroom_pin AS CHAR) AS showroom_pin,"
							+ " CAST(showroom_active AS CHAR) AS showroom_active,"
							+ " CAST(city_name AS CHAR) AS city_name";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_app_showroom"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = showroom_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE 1 = 1";

					CountSql = "SELECT COUNT(DISTINCT(showroom_id))";

					StrSql += SqlJoin;
					CountSql += SqlJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch
								// + " GROUP BY showroom_id"
								+ " ORDER BY showroom_id ASC";
					}
					CountSql += StrSearch;
					TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, map, 0));
					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Showroom(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "showroom-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						if (all.equals("yes")) {
							StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_app_showroom\\b", "FROM " + compdb(comp_id) + "axela_app_showroom"
									+ " INNER JOIN (SELECT showroom_id FROM " + compdb(comp_id) + "axela_app_showroom"
									+ " GROUP BY showroom_id"
									+ " ORDER BY showroom_id DESC"
									+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (showroom_id)");
							StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
									+ " ORDER BY showroom_id DESC";
						} else {
							StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						}
						CachedRowSet crs = processPrepQuery(StrSql, map, 0);
						int count = StartRec - 1;
						Str.append("<div class=\"  table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th>ID</th>\n");
						Str.append("<th>Showroom</th>\n");
						Str.append("<th data-hide=\"phone\">Address</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							count++;
							Str.append("<tr>\n");
							Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(crs.getString("showroom_id")).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("showroom_name"));
							if (crs.getString("showroom_active").equals("0")) {
								Str.append("<br><font color=\"red\">[Inactive]</font>");
							}
							Str.append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\" nowrap>");
							address = crs.getString("showroom_address") + ",";
							if (!address.equals("")) {
								if (!crs.getString("city_name").equals("")) {
									address += "<br>" + crs.getString("city_name");
								}
								address += " - " + crs.getString("showroom_pin");
								if (!crs.getString("state_name").equals("")) {
									address += ", " + crs.getString("state_name") + ".";
								}
							}
							Str.append(address).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\">");
							Str.append("<a href=\"../app/showroom-update.jsp?update=yes&showroom_id=").append(crs.getInt("showroom_id")).append("\">Update Showroom</a>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
						map.clear();

					} else {
						RecCountDisplay = "<br><br><br><br><font color=\"red\">No Showroom(s) found!</font><br><br>";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
