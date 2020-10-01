package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managecustomergroup extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt; </li><li><a href=manager.jsp>Business Manager</a> &gt; </li><li><a href=managecustomergroup.jsp?all=yes>List Groups</a>:</li>";
	public String LinkListPage = "managecustomergroup.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managecustomergroup-update.jsp?add=yes>Add Group...</a>";
	public String ExportPerm = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrHTML = "";
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
	public String group_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Group ID", "numeric", "group_id"},
			{"Group Desc", "text", "group_desc"},
			{"Entry Date", "date", "group_entry_date"},
			{"Modified Date", "date", "group_modified_date"},
			{"Entry By", "text", "group_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "group_entry_date"},
			{"Modified By", "text", "group_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "group_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND group_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Groups!";
					StrSearch = StrSearch + " and group_id > 0";
				}

				if (!(group_id.equals("0"))) {
					msg = msg + "<br>Results for Group ID = " + group_id + "!";
					StrSearch = StrSearch + " and group_id = " + group_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("groupstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "Select group_id, group_desc from " + compdb(comp_id) + "axela_customer_group where 1=1";
		CountSql = "SELECT Count(distinct group_id) from " + compdb(comp_id) + "axela_customer_group where 1=1";
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
		}
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Group(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "managesob.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " order by group_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage;

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("\n <table class=\"table table-bordered\">");
				Str.append("<tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Group Details</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>").append(crs.getString("group_desc")).append(" </td>\n");
					Str.append("<td><a href=\"managecustomergroup-update.jsp?update=yes&group_id=")
							.append(crs.getString("group_id"))
							.append(" \">Update Group</a></td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</table>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Group(s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
