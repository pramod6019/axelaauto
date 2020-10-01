package axela.app;
//Vidyanandan

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

public class TestDrive_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=../app/home.jsp>App</a>"
			+ " &gt; <a href=\"testdrive-list.jsp?all=yes\">List Test Drives</a>:";
	public String LinkAddPage = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageURL = "";
	public String StrSearch = "";
	public String msg = "";
	public String testdrivetype_name = "";
	public String comp_id = "0";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String start_date = "";
	public String end_date = "";
	public String QueryString = "";
	public String all = "";
	public String testdrive_id = "0";
	// CachedowSet parameter
	Map<Integer, Object> map = new HashMap<>();
	public int mapkey = 0;
	// //
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String homefilter = "", fromdate = "", todate = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Test Drive ID", "numeric", "testdrive_id"},
			{"First Name", "text", "testdrive_fname"},
			{"Last Name", "text", "testdrive_lname"},
			{"Mobile", "text", "testdrive_mobile"},
			{"Email", "text", "testdrive_email"},
			// {"Reg. No.", "text", ""}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = GetSession("emp_id", request);
			recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
			CheckPerm(comp_id, "emp_role_id", request, response);
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			smart = PadQuotes(request.getParameter("smart"));
			QueryString = PadQuotes(request.getQueryString());
			msg = PadQuotes(request.getParameter("msg"));
			testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
			all = PadQuotes(request.getParameter("all"));
			advSearch = PadQuotes(request.getParameter("advsearch_button"));
			smart = PadQuotes(request.getParameter("smart"));
			homefilter = PadQuotes(request.getParameter("homefilter"));

			if (msg.toLowerCase().contains("delete")) {
				StrSearch = StrSearch + " AND testdrive_id = 0";
			} else if ("yes".equals(all)) {
				msg = "Results for all Test Drives!";
			} else if (!testdrive_id.equals("0")) {
				msg = msg + "<br>Result for Test Drive Booking ID = " + testdrive_id + "!";
				StrSearch = StrSearch + " AND testdrive_id = " + testdrive_id + "";
				// mapkey++;
				// map.put(mapkey, testdrive_id);
			} else if (advSearch.equals("Search")) // for keyword search
			{
				StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				if (StrSearch.equals("")) {
					msg = "Enter search text!";
					StrSearch = StrSearch + " AND testdrive_id = 0";
				} else {
					msg = "Results for Search!";
				}
			}
			if (homefilter.equals("yes")) {
				fromdate = PadQuotes(request.getParameter("from"));
				todate = PadQuotes(request.getParameter("to"));
				fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
				todate = ConvertShortDateToStr(todate).substring(0, 8);
				StrSearch += " AND (SUBSTR(testdrive_entry_time, 1,8) >= '" + fromdate + "'"
						+ " AND SUBSTR(testdrive_entry_time, 1,8) <= '" + todate + "')";
				msg = "Results for Test Drives!";
			}

			// SetSession("preownedtestdrivestrsql", StrSearch, request);

			StrHTML = ListData();
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
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("")) {
					StrSql = "SELECT CONCAT(title_desc, ' ', testdrive_fname, ' ', testdrive_lname) as contactname,"
							+ " testdrive_id, testdrive_email, testdrive_mobile, model_name,"
							+ " testdrivetype_id, testdrivetype_name, testdrive_comments, testdrive_user_id";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_app_testdrive"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = testdrive_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_app_testdrive_type ON testdrivetype_id = testdrive_testdrivetype_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = testdrive_model_id"

							+ " WHERE 1 = 1";

					CountSql = " SELECT COUNT(DISTINCT(testdrive_id))";

					StrSql += SqlJoin;
					CountSql += SqlJoin;

					if (!(StrSearch.equals(""))) {
						StrSql += StrSearch
								+ " GROUP BY testdrive_id"
								+ " ORDER BY testdrive_id DESC";
					}
					CountSql += StrSearch;
					TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, map, 0));
					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Test Drive(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "testdrive-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
							// display on jsp page
							PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
							if (all.equals("yes")) {
								StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_app_testdrive\\b", "from " + compdb(comp_id) + "axela_app_testdrive "
										+ " inner join (select testdrive_id from " + compdb(comp_id) + "axela_app_testdrive "
										+ " where 1=1 "
										+ " group by testdrive_id "
										+ " order by testdrive_id desc"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") as myresults using (testdrive_id)");

								// StrSql = "select * from (" + StrSql + ") as datatable "
								StrSql += " order by testdrive_id desc ";
								// SOP("StrSql---------" + StrSqlBreaker(StrSql));

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
							Str.append("<th>Name</th>\n");
							Str.append("<th data-hide=\"phone\">Contact</th>\n");
							Str.append("<th data-hide=\"phone\">Type</th>\n");
							Str.append("<th data-hide=\"phone\">Model</th>\n");
							Str.append("<th data-hide=\"phone, tablet\">Comments</th>\n");
							// Str.append("<th width=20%>Actions</th>\n");
							Str.append("</tr>\n");
							Str.append("</thead>\n");
							Str.append("<tbody>\n");
							Str.append("</tr>\n");
							String slot = "";
							while (crs.next()) {

								count++;
								Str.append("<tr>\n<td valign=top align=center >").append(count).append("</td>\n");
								Str.append("<td valign=top align=center >").append(crs.getString("testdrive_id")).append("</td>\n");
								Str.append("<td valign=top align=left >");
								Str.append("<a href=\"../portal/user-list.jsp?user_id=" + crs.getString("testdrive_user_id") + "\">");
								Str.append(crs.getString("contactname")).append("</a></td>\n");
								Str.append("<td valign=top align=left >");
								Str.append(SplitPhoneNo(crs.getString("testdrive_mobile"), 5, "M")).append("<br>");
								Str.append("<a href=mailto:").append(crs.getString("testdrive_email")).append(">");
								Str.append(crs.getString("testdrive_email")).append("</a>");
								Str.append("</td>\n");
								if (crs.getString("testdrivetype_id").equals("1"))
								{
									testdrivetype_name = "New Car";
								}
								else if (crs.getString("testdrivetype_id").equals("2"))
								{
									testdrivetype_name = "Pre-Owned";
								}
								Str.append("<td valign=top align=center>").append(testdrivetype_name);
								Str.append("<td valign=top align=left >").append(crs.getString("model_name")).append("</td>\n");
								Str.append("<td valign=top align=left >").append(crs.getString("testdrive_comments")).append("</td>\n");
								Str.append("</tr>\n");
							}
							Str.append("</tbody>\n");
							Str.append("</table>\n");
							Str.append("</div>\n");
							crs.close();
							map.clear();

						} else {
							RecCountDisplay = "<br><br><br><br><font color=red>No Test Drive(s) found!</font><br><br>";
						}
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
