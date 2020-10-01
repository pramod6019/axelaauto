package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Executives_User_Active extends Connect {

	public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=manager.jsp>Business Manager</a> &gt; <a href=executives-user-active.jsp>Active Users</a><b>:</b>";
	public String LinkListPage = "executives-user-active.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String comp_id = "0";
	public String ExportPerm = "";
	public String user_jsessionid = "0";
	public String user_emp_id = "0";
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
	public String user_logintime = "";
	public String user_ip = "";
	public String user_agent = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String StrSearch = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Executive", "text", "emp_name"},
			{"Remote Host", "text", "user_ip"},
			{"Remote Agent", "text", "user_agent"},
			{"Signin Time", "text", "user_logintime"}
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
				} else if (!(user_emp_id.equals("0"))) {
					msg = msg + "<br>Result for User " + user_emp_id + "";
					StrSearch = StrSearch + " and emp_id =" + user_emp_id + "";
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
		StrSql = "Select user_jsessionid,user_emp_id,user_logintime,user_ip,user_agent,emp_name,emp_ref_no ";
		CountSql = " SELECT count(distinct user_jsessionid)";
		Sqljoin = " from " + compdb(comp_id) + "axela_emp_user "
				+ " inner join  " + compdb(comp_id) + "axela_emp on emp_id = user_emp_id "
				+ " where 1=1";
		StrSql = StrSql + Sqljoin;
		CountSql = CountSql + Sqljoin;
		if (!emp_idsession.equals("1")) {
			StrSearch = " and user_emp_id!=1";
		}
		StrSql = StrSql + StrSearch;
		CountSql = CountSql + StrSearch;
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
			PageURL = "executives-user-active.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " order by user_logintime desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
			try {
				int count = StartRec - 1;
				CachedRowSet crs =processQuery(StrSql, 0);
				String col = "white";
				String altcol = "#FFFFCC";
				String bgcol = col;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th>Remote Host</th>\n");
				Str.append("<th data-hide=\"phone\">SignIn Time</th>\n");
				Str.append("<th data-hide=\"phone\">Remote Agent</th>\n");
				Str.append("</tr></thead><tbody>\n");
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
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("user_emp_id")).append(">").append(crs.getString("emp_name")).append("" + " (")
							.append(crs.getString("emp_ref_no")).append(")</a>").append("</td>\n");
					if (crs.getString("user_ip") != null) {
						Str.append("<td>").append(crs.getString("user_ip")).append("</td>\n");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (crs.getString("user_logintime") != null) {
						Str.append("<td>").append(strToLongDate(crs.getString("user_logintime"))).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (crs.getString("user_agent") != null) {
						Str.append("<td>").append(crs.getString("user_agent")).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					Str.append("</tr>");
				}
				crs.close();
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><font color=red><b>No Active User(s) found!</b></font><br><br>");
		}
		return Str.toString();
	}
}
