package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Notification_List extends Connect {

	public String comp_id = "";
	public String emp_id = "";
	public int recperpage = 0;
	public String branch_id = "";
	public String LinkExportPage = "";
	public String BranchAcces = "";
	public String PageCurrents = "";
	public String sent = "";
	public String PageNaviStr = "";
	public int PageCount = 10;
	public int PageSpan = 10;
	public String QueryString = "";
	public int PageCurrent = 0;
	public String LinkAddPage = "<a href=notification-exe-send.jsp?Add=yes>Send New Notification...</a>";
	public String ExportPerm = "";
	public String RecCountDisplay = "";
	public String BranchAccess = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String msg = "";
	public String StrSearch = "";
	public String LinkHeader = "<li><a href=\"../portal/home.jsp\"> Home </a> &gt; </li><li><a href=\"notification-list.jsp?all=yes\"> List Notifications </a>:</li>";
	public String smartarr[][] = {{"Keyword", "text", "keyword_arr"},
			{"Notification ID", "numeric", "notification_id"},
			{"Title", "text", "notification_title"},
			{"Message", "text", "notification_msg"},
			{"Date", "date", "notification_date"},
			{"Sent", "boolean", "Notification_sent"}};
	public String StrHTML = "";
	public String all = "";
	public Object notification_id = 0;
	public String smart = "";
	public String notification_title = "";
	private String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_notification_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				notification_title = CNumeric(PadQuotes(request.getParameter("notification_title")));
				notification_id = CNumeric(PadQuotes(request.getParameter("notification_id")));
				notification_title = CNumeric(PadQuotes(request.getParameter("notification_msg")));
				notification_id = CNumeric(PadQuotes(request.getParameter("notification_date")));
				notification_id = CNumeric(PadQuotes(request.getParameter("Notification_sent")));
				smart = PadQuotes(request.getParameter("smart"));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if ("yes".equals(all)) {
					msg = "Results for all Notification(s)!";
					StrSearch = StrSearch + " AND notification_id > 0";
				} else if (!(notification_id.equals("0"))) {
					msg = msg + "<br>Results for NOTIFICATION ID =" + notification_id + "";

				} else if (advSearch.equals("Search")) {
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Smart Search!";
					StrSearch = StrSearch + GetSession("notificationstrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("notificationstrsql", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("notificationPrintSearchStr", StrSearch, request);
					SetSession("notificationFilterStr", StrSearch, request);
				}
				StrSearch += BranchAccess + ExeAccess.replace("emp_id", "axela_emp.emp_id");
				// SOP(StrSearch + "=============StrSearch");
				StrHTML = Listdata();
			}
		} catch (Exception e) {

		}
	}
	public String Listdata() {
		String StrSql = "";
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		try {
			StrSql = " SELECT notification_id,"
					+ " COALESCE (branch_name, '') AS branch_name,"
					+ " COALESCE (notification_branch_id, '') AS branch_id,"
					+ " COALESCE (notification_emp, '') AS emp_name,"
					+ " COALESCE (emp.emp_ref_no, '') AS emp_ref_no,"
					+ " notification_emp_id,"
					+ " COALESCE (CONCAT( " + compdb(comp_id) + "axela_emp.emp_name,"
					+ "	' (', " + compdb(comp_id) + "axela_emp.emp_ref_no,')'),'') AS entry_name,"
					+ " notification_title,notification_msg,notification_date,notification_sent,notification_entry_id";

			String StrJoin = " FROM " + compdb(comp_id) + "axela_notification"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = notification_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = notification_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS emp ON emp.emp_id = notification_emp_id"
					+ " WHERE 1 = 1";
			String CountSql = " SELECT Count(distinct notification_id)";
			CountSql += StrJoin;
			CountSql += StrSearch;
			// SOP("CountSql===" + CountSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			StrSql += StrJoin;
			StrSql += StrSearch;
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
						+ " of " + TotalRecords + " Notification(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "notification-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
						PageListSize);
				StrSql = StrSql + ""
						+ " ORDER BY notification_id DESC"
						+ " LIMIT " + (StartRec - 1) + ", " + recperpage
						+ "";
				SOP("StrSql=111==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				int count = StartRec - 1;
				Str.append("\n <div class=\"table-hover\"><table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Title</th>\n");
				Str.append("<th data-hide=\"phone\">Message</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sent By</th>\n");
				Str.append("</tr></thead><tbody>\n");
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align=\"center\">").append(count).append("</td>");

						Str.append("<td>");
						if (!crs.getString("emp_name").equals("")) {
							Str.append("<a href=\"executive-summary.jsp?emp_id=")
									.append(crs.getString("notification_emp_id"))
									.append("\">").append(crs.getString("emp_name"))
									.append("(").append(crs.getString("emp_ref_no")).append(")")
									.append("</a>");
						}
						Str.append("</td>");
						Str.append("<td>");
						if (crs.getString("notification_title") != null
								&& !crs.getString("notification_title").equals("")) {
							Str.append(crs.getString("notification_title")).append("");
						} else {
							Str.append("---");
						}
						Str.append("</td align=\"center\">");
						Str.append("<td>");
						if (crs.getString("notification_msg") != null
								&& !crs.getString("notification_msg").equals("")) {
							Str.append(crs.getString("notification_msg")).append("");
						} else {
							Str.append("---");
						}
						Str.append("</td> ");
						Str.append("<td align=\"center\">")
								.append(strToLongDate(crs.getString("notification_date")))
								.append("</td>");
						if (crs.getString("notification_sent").equals("0")) {
							sent = "Not Sent";
						} else {
							sent = "Sent";
						}
						Str.append("<td align=\"center\">").append(sent).append("</td>");
						Str.append("<td><a href=\"executive-summary.jsp?emp_id=")
								.append(crs.getString("notification_entry_id"))
								.append("\" target=_blank>")
								.append(crs.getString("entry_name"))
								.append("</a></td>");
						Str.append("</tr>\n");
					}
					Str.append("</tbody></table></div>\n");
					crs.close();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Notification(s) found!</font><br><br>";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
