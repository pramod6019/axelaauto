package axela.portal;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Executives_User_Access extends Connect {

	public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=manager.jsp>Business Manager</a> &gt; <a href=executives-user-access.jsp>User Access</a><b>:</b>";
	public String LinkListPage = "executives-user-access.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_idsession = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String comp_id = "0";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String log_id = "0";
	DecimalFormat deci = new DecimalFormat("#");
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Executive", "text", "emp_name"},
			{"Remote Host", "text", "log_remote_host"},
			{"Remote Agent", "text", "log_remote_agent"},
			{"Signin Time", "date", "log_signin_time"},
			{"Signout Time", "date", "log_signout_time"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(PadQuotes(CNumeric(GetSession("emp_id", request))));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search_button"));
				msg = PadQuotes(request.getParameter("msg"));
				log_id = CNumeric(PadQuotes(request.getParameter("log_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (!emp_idsession.equals("1")) {
					StrSearch = StrSearch + " and emp_id!=1";
				}
				if ("yes".equals(all)) {
					msg = "Results for all User(s)!";
					StrSearch = StrSearch + " and log_id > 0";
				}
				if (!log_id.equals("0")) {
					StrSearch = StrSearch + " and log_id=" + log_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("exeuseraccessstrsql", StrSearch, request);
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String Listdata() {
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
		// to know no of records depending on search
		StrSql = " Select " + compdb(comp_id) + "axela_emp_log.*,emp_name, emp_id, emp_ref_no "
				+ " from " + compdb(comp_id) + "axela_emp_log "
				+ " inner join  " + compdb(comp_id) + "axela_emp on emp_id=log_emp_id where 1 = 1 ";
		CountSql = " SELECT Count(distinct log_id) from " + compdb(comp_id) + "axela_emp_log  "
				+ " inner join  " + compdb(comp_id) + "axela_emp on emp_id=log_emp_id where 1=1 ";
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " User(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "executives-user-access.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by log_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("\n<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th data-hide=\"phone\">Session ID</th>\n");
				Str.append("<th>Remote Host</th>\n");
				Str.append("<th data-hide=\"phone\">Sign In Time</th>\n");
				Str.append("<th data-hide=\"phone\">Sign Out Time</th>\n");
				Str.append("<th data-hide=\"phone\">Duration</th>\n");
				Str.append("<th data-hide=\"phone\">Remote Agent</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("" + " (")
							.append(crs.getString("emp_ref_no")).append(")</a>" + "<br>Executive ID: ").append(crs.getString("emp_id")).append("</td>\n");
					Str.append("<td>").append(crs.getString("log_session_id")).append("</td>\n");
					Str.append("<td>").append(crs.getString("log_remote_host")).append("</td>\n");
					Str.append("<td>").append(strToLongDate(crs.getString("log_signin_time"))).append("</td>\n");
					Str.append("<td>").append(strToLongDate(crs.getString("log_signout_time"))).append("</td>\n");
					if (!crs.getString("log_signout_time").equals("")) {
						int Hours = +(int) getHoursBetween(StringToDate(crs.getString("log_signin_time")), StringToDate(crs.getString("log_signout_time")));
						String Mins = deci.format(getMinBetween(StringToDate(crs.getString("log_signin_time")), StringToDate(crs.getString("log_signout_time"))));
						Str.append("<td>").append(Hours).append(":").append(Mins).append("</td>\n");
					} else {
						Str.append("<td>--</td>\n");
					}
					Str.append("<td>").append(crs.getString("log_remote_agent")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table></div>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><b><font color=red>No User(s) found!</font></b><br><br>");
		}
		return Str.toString();
	}
}
