//Dilip Kumar P
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Leave_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/appt.jsp>Booking</a> &gt; <a href=leave-list.jsp?all=yes>List Leaves</a>:";
	public String LinkListPage = "leave-list.jsp";
	public String LinkExportPage = "leave.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=leave-update.jsp?add=yes>Add Leave...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String smart = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String PageURL = "";
	public String StrSql = "";
	public String StrSearch = "";
	public Smart SmartSearch = new Smart();
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String leave_id = "0";
	public String advSearch = "";
	public String branch_id = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Leave ID", "numeric", "leave_id"},
			{"Emp ID", "numeric", "leave_emp_id"},
			{"From time", "date", "leave_fromtime"},
			{"To Time", "date", "leave_totime"},
			{"Description", "text", "leave_desc"},
			{"Notes", "text", "leave_notes"},
			{"Entry By", "text", "leave_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "leave_entry_date"},
			{"Modified By", "text", "leave_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "leave_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				leave_id = CNumeric(PadQuotes(request.getParameter("leave_id")));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = StrSearch + " AND leave_id = 0";
				} else if ("yes".equals(all)) // for all students to b displayed
				{
					msg = msg + "<br>Results for all Leave!";
					StrSearch = StrSearch + " AND leave_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND leave_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (!leave_id.equals("0")) {
					msg = msg + "<br>Results for Leave ID = " + leave_id + "!";
					StrSearch = StrSearch + " AND leave_id = " + leave_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("leavestrsql", request);
				}
				SetSession("leavestrsql", StrSearch, request);
				StrHTML = Listdata();
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

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			if (!msg.equals("")) {
				StrSql = "SELECT leave_id, emp_name, branch_code, leave_emp_id, branch_id,"
						+ " CONCAT(branch_name,' (',branch_code,')') AS branchname, leave_desc,"
						+ " leave_fromtime, leave_totime, leave_desc, leave_desc";

				CountSql = " SELECT COUNT(DISTINCT leave_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_emp_leave"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = leave_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
						+ " WHERE 1 = 1 " + BranchAccess;
				StrSql = StrSql + SqlJoin;
				// SOP("....StrSql....."+StrSql);
				CountSql = CountSql + SqlJoin;
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
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Leave(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "leave-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql + " GROUP BY leave_id ORDER BY leave_id desc";
					} else {
						StrSql = StrSql + " GROUP BY leave_id ORDER BY leave_id";
					}
					StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					try {
						int count = StartRec - 1;
						Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
						Str.append("<tr align=center>\n");
						Str.append("<th>#</th>\n");
						Str.append("<th>Leave Details</th>\n");
						Str.append("<th>From Time</th>\n");
						Str.append("<th>To Time</th>\n");
						Str.append("<th>Executive</th>\n");
						if (branch_id.equals("0")) {
							Str.append("<th>Branch</th>\n");
						}
						Str.append("<th width=20%>Actions</th>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td valign=top align=center>").append(count).append("</td>\n");
							Str.append("<td valign=top>");
							if (!crs.getString("leave_desc").equals("")) {
								Str.append(crs.getString("leave_desc")).append("");
							}
							Str.append("</td>\n");
							if (!crs.getString("leave_fromtime").equals("")) {
								Str.append("<td valign=top>").append(strToLongDate(crs.getString("leave_fromtime"))).append("");
								Str.append("</td>\n");
							}
							if (!crs.getString("leave_totime").equals("")) {
								Str.append("<td valign=top>").append(strToLongDate(crs.getString("leave_totime"))).append("");
								Str.append("</td>\n");
							}

							Str.append("<td valign=top><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("leave_emp_id")).append(">").append(crs.getString("emp_name"))
									.append("</td>");
							if (branch_id.equals("0")) {
								Str.append("<td valign=top><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">").append(crs.getString("branchname"))
										.append("</a></td>");
							}
							Str.append("<td valign=top><a href=\"leave-update.jsp?update=yes&leave_id=").append(crs.getString("leave_id")).append("\">Update Leave</a>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</table>\n");
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					Str.append("<br><br><br><br><font color=red><b>No Leave(s) Found!</b></font><br><br>");
				}
			}
		}
		return Str.toString();
	}
}
