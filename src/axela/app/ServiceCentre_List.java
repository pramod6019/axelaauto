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
import cloudify.connect.Smart;

public class ServiceCentre_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../app/home.jsp>Home</a>"
			+ " &gt; Service Centre"
			+ " &gt; <a href=../app/servicecentre-list.jsp?all=yes>List Service Centres</a>:";
	public String LinkExportPage = "servicecentre-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../app/servicecentre-update.jsp?add=yes>Add New ServiceCentre </a>";
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
	public String servicecenter_id = "0";
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
			{"Service Centre ID", "numeric", "servicecenter_id"},
			{"Service Centre Name", "text", "servicecenter_name"},
			{"Address", "text", "servicecenter_address"},
			{"City", "text", "servicecenter_city_id IN (SELECT city_id FROM compdb.axela_city WHERE city_name"},
			{"Pin", "text", "servicecenter_pin"},
			{"Latitude", "numeric", "servicecenter_latitude"},
			{"Longitude", "text", "servicecenter_longitude"},
			{"Active", "boolean", "servicecenter_active"},
			{"Entry By", "text", "servicecenter_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "servicecenter_entry_date"},
			{"Modified By", "text", "servicecenter_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "servicecenter_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
				CheckPerm(comp_id, "emp_role_id", request, response);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				servicecenter_id = CNumeric(PadQuotes(request.getParameter("servicecenter_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				homefilter = PadQuotes(request.getParameter("homefilter"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND servicecenter_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Service Centres!";
				} else if (!servicecenter_id.equals("0")) {
					msg += "<br>Result for Service Centre ID = " + servicecenter_id + "!";
					StrSearch += " AND servicecenter_id = ?";
					mapkey++;
					map.put(mapkey, servicecenter_id);
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND servicecenter_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg += "<br>Results for Search!";
					if (GetSession("servicecentrestrsql", request) != null) {
						StrSearch += GetSession("servicecentrestrsql", request);
					}
				}

				if (homefilter.equals("yes")) {
					fromdate = PadQuotes(request.getParameter("from"));
					todate = PadQuotes(request.getParameter("to"));
					fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
					todate = ConvertShortDateToStr(todate).substring(0, 8);
					StrSearch += " AND (SUBSTR(servicecenter_entry_date, 1,8) >= '" + fromdate + "'"
							+ " AND SUBSTR(servicecenter_entry_date, 1,8) <= '" + todate + "')";
					msg = "Results for Service Centers!";
				}

				SetSession("servicecentrestrsql", StrSearch, request);
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
					StrSql = "SELECT servicecenter_id, "
							+ " CAST(servicecenter_name AS CHAR) AS servicecenter_name, "
							+ " CAST(servicecenter_address AS CHAR) AS servicecenter_address,"
							+ " CAST(servicecenter_pin AS CHAR) AS servicecenter_pin, "
							+ " CAST(servicecenter_active AS CHAR) AS servicecenter_active, "
							+ " CAST(city_name AS CHAR) AS city_name, "
							+ " CAST(state_name AS CHAR) AS state_name";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_app_servicecenter"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = servicecenter_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE 1 = 1";

					CountSql = "SELECT COUNT(DISTINCT(servicecenter_id))";

					StrSql += SqlJoin;
					CountSql += SqlJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch +
								" GROUP BY servicecenter_id"
								+ " ORDER BY servicecenter_id DESC";
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
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Service Centre(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "servicecentre-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						if (all.equals("yes")) {
							StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_app_servicecenter\\b", "FROM " + compdb(comp_id) + "axela_app_servicecenter"
									+ " INNER JOIN (SELECT servicecenter_id FROM " + compdb(comp_id) + "axela_app_servicecenter "
									+ " GROUP BY servicecenter_id"
									+ " ORDER BY servicecenter_id DESC"
									+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (servicecenter_id)");
							StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
									+ " ORDER BY servicecenter_id ";
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
						Str.append("<th data-hide=\"phone\">Service Centre</th>\n");
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
							Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(crs.getString("servicecenter_id")).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("servicecenter_name"));
							if (crs.getString("servicecenter_active").equals("0")) {
								Str.append("<br><font color=\"red\">[Inactive]</font>");
							}
							Str.append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\" nowrap>");
							address = crs.getString("servicecenter_address") + ",";
							if (!crs.getString("city_name").equals("")) {
								address += "<br>" + crs.getString("city_name");
							}
							address += " - " + crs.getString("servicecenter_pin");
							if (!crs.getString("state_name").equals("")) {
								address += ", " + crs.getString("state_name") + ".";
							}
							Str.append(address).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"left\">");
							Str.append("<a href=\"servicecentre-update.jsp?update=yes&servicecenter_id=").append(crs.getInt("servicecenter_id")).append("\">Update Service Centre</a>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
						map.clear();
					} else {
						RecCountDisplay = "<br><br><br><br><font color=\"red\">No Service Centre(s) found!</font><br><br>";
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
