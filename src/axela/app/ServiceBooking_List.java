package axela.app;
//@Bhagwan Singh 11 feb 2013

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

public class ServiceBooking_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../app/home.jsp>App</a>"
			+ " &gt; <a href=servicebooking.jsp>Service Booking</a>"
			+ " &gt; <a href=../app/servicebooking-list.jsp?all=yes>List Service Bookings</a>:";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String comp_id = "0";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String service_id = "0";
	// CachedowSet parameter
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;
	// //
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String homefilter = "", fromdate = "", todate = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Service Booking ID", "numeric", "service_id"},
			{"Title", "text", "title_desc"},
			{"First Name", "text", "service_fname"},
			{"Last Name", "text", "service_lname"},
			{"Mobile", "text", "service_mobile"},
			{"Email", "text", "service_email"},
			{"Time", "date", "service_time"},
			{"Reg. No.", "text", "service_reg_no"}
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
			service_id = CNumeric(PadQuotes(request.getParameter("service_id")));
			all = PadQuotes(request.getParameter("all"));
			advSearch = PadQuotes(request.getParameter("advsearch_button"));
			homefilter = PadQuotes(request.getParameter("homefilter"));

			if (msg.toLowerCase().contains("delete")) {
				StrSearch = StrSearch + " AND service_id = 0";
			} else if ("yes".equals(all)) {
				msg = "Results for all Service Bookings!";
			} else if (!service_id.equals("0")) {
				msg = msg + "<br>Result for Service Booking ID = " + service_id + "!";
				StrSearch = StrSearch + " AND service_id = " + service_id + "";
			} else if (advSearch.equals("Search")) // for keyword search
			{
				StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				if (StrSearch.equals("")) {
					msg = "Enter search text!";
					StrSearch = StrSearch + " AND service_id = 0";
				} else {
					msg = "Results for Search!";
				}
			} else if (smart.equals("yes")) // for smart search
			{
				msg = msg + "<br>Results for Search!";
				if (session.getAttribute("servicestrsql") != null) {
					StrSearch = StrSearch + session.getAttribute("servicestrsql");
				}
			}
			if (homefilter.equals("yes")) {
				fromdate = PadQuotes(request.getParameter("from"));
				todate = PadQuotes(request.getParameter("to"));
				fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
				todate = ConvertShortDateToStr(todate).substring(0, 8);
				StrSearch += " AND (SUBSTR(service_entry_time, 1,8) >= '" + fromdate + "'"
						+ " AND SUBSTR(service_entry_time, 1,8) <= '" + todate + "')";
				msg = "Results for Service Booking!";
			}

			// session.setAttribute("servicestrsql", StrSearch);

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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("")) {
					StrSql = "SELECT CONCAT(title_desc, ' ', service_fname, ' ', service_lname) as contactname,"
							+ " service_id, service_email, service_mobile, service_reg_no, service_time,"
							+ " service_slot, service_user_id, service_comments";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_app_service"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = service_title_id"
							+ " WHERE 1 = 1";

					CountSql = " SELECT COUNT(DISTINCT(service_id))";

					StrSql += SqlJoin;
					CountSql += SqlJoin;

					if (!(StrSearch.equals(""))) {
						StrSql += StrSearch
								+ " GROUP BY service_id"
								+ " ORDER BY service_id DESC";
					}
					CountSql += StrSearch;
					// SOP("CountSql = " + CountSql);
					TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, map, 0));
					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Service Booking(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "servicebooking-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						if (all.equals("yes")) {
							StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_app_service\\b", "FROM " + compdb(comp_id) + "axela_app_service "
									+ " inner join (select service_id from " + compdb(comp_id) + "axela_app_service "
									+ " where 1=1 "
									+ " group by service_id"
									+ " order by service_id desc"
									+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") as myresults using (service_id)");
							// StrSql = "select * from (" + StrSql + ") as datatable "

							StrSql += " order by service_id desc ";
						} else {
							StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						}
						// SOP("StrSql---------" + StrSql);
						CachedRowSet crs = processPrepQuery(StrSql, map, 0);
						int count = StartRec - 1;
						Str.append("<div class=\"  table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th>ID</th>\n");
						Str.append("<th>Name</th>\n");
						Str.append("<th data-hide=\"phone\">>Contact</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Time</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Reg. No.</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Comments</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						Str.append("</tr>\n");
						String slot = "";
						while (crs.next()) {
							count++;
							Str.append("<tr>\n<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=center >").append(crs.getString("service_id")).append("</td>\n");
							Str.append("<td valign=top align=left >");
							Str.append("<a href=\"../portal/user-list.jsp?user_id=" + crs.getString("service_user_id") + "\">");
							Str.append(crs.getString("contactname")).append("</a></td>\n");
							Str.append("<td valign=top align=left >");
							Str.append(SplitPhoneNo(crs.getString("service_mobile"), 5, "M")).append("<br>");
							Str.append("<a href=mailto:").append(crs.getString("service_email")).append(">");
							Str.append(crs.getString("service_email")).append("</a>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=center >");
							Str.append(strToShortDate(crs.getString("service_time"))).append("<br>");
							slot = crs.getString("service_slot");
							if (slot.equals("1")) {
								Str.append("8am - 1pm");
							} else if (slot.equals("2")) {
								Str.append("1pm - 3pm");
							} else if (slot.equals("3")) {
								Str.append("3pm - 6pm");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=left >").append(unescapehtml(crs.getString("service_reg_no"))).append("</td>\n");
							Str.append("<td valign=top align=left >").append(crs.getString("service_comments")).append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
						map.clear();

					} else {
						RecCountDisplay = "<br><br><br><br><font color=red>No Service Booking(s) found!</font><br><br>";
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
