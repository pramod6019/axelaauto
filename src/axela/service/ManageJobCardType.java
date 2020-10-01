package axela.service;

/**
 * @author Gurumurthy TS 17 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobCardType extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=managejobcardtype.jsp?all=yes>List Job Card Type</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managejobcardtype-update.jsp?add=yes>Add Job Card Type...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String type_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Type ID", "numeric", "jctype_id"},
			{"Type Name", "text", "jctype_name"},
			{"Entry By", "text", "jctype_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "jctype_entry_date"},
			{"Modified By", "text", "jctype_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "jctype_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND jctype_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Job Card Type(s)!";
					StrSearch = StrSearch + " and jctype_id > 0";
				} else if (!(type_id.equals("0"))) {
					msg = msg + "<br>Results for Job Card Type ID = " + type_id + "!";
					StrSearch = StrSearch + " and jctype_id = " + type_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND jctype_id = 0";
					} else {
						msg = "Results for Search!";
					}
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "SELECT jctype_id, jctype_name, jctype_workhour";

			CountSql = "SELECT COUNT(DISTINCT jctype_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " WHERE 1 = 1";

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
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Job Card Type(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				// PageURL = "managecontractservice.jsp?" + QueryString + "&PageCurrent=";
				PageURL = "managejobcardtype.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " order by jctype_id desc"
						+ " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th width=5% data-toggle=\"true\"><span class=\"footable-toggle\"></span>#</th>\n");
					Str.append("<th>Job Card Type Details</th>\n");
					Str.append("<th>Work Hours</th>\n");
					Str.append("<th width=20%>Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("jctype_name")).append("</td>\n");
						String jctype_workhour = crs.getString("jctype_workhour");
						if (!jctype_workhour.contains(".")) {
							jctype_workhour = jctype_workhour + ".00";
						}
						String jctype_workhours[] = jctype_workhour.split("\\.");
						if (jctype_workhours[0].length() < 2) {
							jctype_workhours[0] = "0" + jctype_workhours[0];
						}
						if (jctype_workhours[1].length() < 2) {
							jctype_workhours[1] = jctype_workhours[1] + "0";
						}
						jctype_workhour = jctype_workhours[0] + ":" + jctype_workhours[1];
						Str.append("<td valign=top align=\"center\">").append(jctype_workhour).append("</td>\n");
						Str.append("<td valign=top><a href=\"managejobcardtype-update.jsp?update=yes&type_id=").append(crs.getString("jctype_id")).append("\">Update Job Card Type</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Job Card Type(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
