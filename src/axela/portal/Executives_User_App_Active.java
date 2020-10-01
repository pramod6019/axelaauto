package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Executives_User_App_Active extends Connect {

	public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=manager.jsp>Business Manager</a> &gt; <a href=executives-user-app-active.jsp>Active App Users</a><b>:</b>";
	public String LinkListPage = "executives-user-app-active.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String user_jsessionid = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_idsession = "";
	public String user_cse = "";
	public String all = "";
	public String smart = "";
	public String search = "";
	public String msg = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String applog_signin_time = "";
	public String log_remote_host = "";
	public String applog_remote_agent = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String StrSearch = "";
	public String advSearch = "";
	public String logedinTwentyminsBefore = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Executive", "text", "emp_name"},
			{"Remote Host", "text", "log_remote_host"},
			{"Remote Agent", "text", "applog_remote_agent"},
			{"Signin Time", "text", "applog_signin_time"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(PadQuotes(CNumeric(GetSession("emp_id", request))));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				search = PadQuotes(request.getParameter("search_button"));
				msg = PadQuotes(request.getParameter("msg"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				CheckPerm(comp_id, "emp_role_id", request, response);
				if (!emp_idsession.equals("1")) {
					StrSearch = StrSearch + " and emp_id!=1";
				}
				if ("yes".equals(all)) {
					msg = "Results for all Users!";
					StrSearch = StrSearch + " and emp_id > 0";
				} else if (!(emp_id.equals("0"))) {
					msg = msg + "<br>Result for User " + emp_id + "";
					StrSearch = StrSearch + " and emp_id =" + emp_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("exeuseractivestrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("exeuseractivestrsql", StrSearch, request);
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
		String StrSql = "";
		String Sqljoin = "";
		int j = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if ((PageCurrents.equals("0"))) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		StrSql = "Select emp_id, emp_name, applog_id,emp_id, applog_signin_time, log_remote_host,applog_remote_agent,emp_name,emp_ref_no ";
		CountSql = " SELECT count(distinct applog_emp_id)";
		Sqljoin = " from " + compdb(comp_id) + "axela_emp_app_log "
				+ " inner join  " + compdb(comp_id) + "axela_emp on emp_id = applog_emp_id "
				+ " where 1=1 "
				+ " AND applog_signin_time > '" + ToLongDate(AddHoursDate(kknow(), 0, 0, -20)) + "'";
		// + " GROUP BY emp_id ";

		// SOP("CountSql-------"+CountSql);
		StrSql = StrSql + Sqljoin;
		CountSql = CountSql + Sqljoin;
		// SOP("CountSql==="+CountSql);
		if (!emp_idsession.equals("1")) {
			StrSearch = " and emp_id!=1";
		}
		StrSql = StrSql + StrSearch;
		CountSql = CountSql + StrSearch;

		TotalRecords = Integer.parseInt(CNumeric(ExecuteQuery(CountSql)));
		// SOP("TotalRecords==="+Integer.parseInt(CNumeric(ExecuteQuery(CountSql))));
		if (TotalRecords != 0) {

			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			// SOP("recperpage==="+recperpage);
			// SOP("StartRec==="+StartRec);
			// SOP("EndRec==="+EndRec);
			// SOP("TotalRecords==="+TotalRecords);
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " User(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "executives-user-app-active.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " GROUP BY emp_id " + " order by applog_signin_time desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql---is------"+StrSqlBreaker(StrSql));
			try {
				int count = StartRec - 1;
				CachedRowSet crs = processQuery(StrSql, 0);
				String col = "white";
				String altcol = "#FFFFCC";
				String bgcol = col;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Remote Host</th>\n");
				Str.append("<th data-hide=\"phone\">SignIn Time</th>\n");
				Str.append("<th data-hide=\"phone\">Remote Agent</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					if (j == 0) {
						j = 1;
						bgcol = col;
					} else {
						j = 0;
						bgcol = altcol;
					}
					count = count + 1;
					Str.append("<tr onmouseover=\"this.style.background='#eeeeee';\" onmouseout=\"this.style.background='").append(bgcol).append("';\" bgcolor='").append(bgcol).append("'>\n");
					Str.append("<td valign=top align=center  >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left  ><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("" + " (").append(crs.getString("emp_ref_no")).append(")</a>").append("</td>\n");
					if (crs.getString("log_remote_host") != null) {
						Str.append("<td valign=top align=center >").append(crs.getString("log_remote_host")).append("</td>\n");
					} else {
						Str.append("<td valign=top align=center  >&nbsp;</td>");
					}
					if (crs.getString("applog_signin_time") != null) {
						Str.append("<td valign=top align=center  >").append(strToLongDate(crs.getString("applog_signin_time"))).append("</td>");
					} else {
						Str.append("<td valign=top align=center  >&nbsp;</td>");
					}
					if (crs.getString("applog_remote_agent") != null) {
						Str.append("<td valign=top align=left>").append(crs.getString("applog_remote_agent")).append("</td>");
					} else {
						Str.append("<td valign=top align=left>&nbsp;</td>");
					}
					Str.append("</tr>");
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><br><br><font color=red><b>No Active App User(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
