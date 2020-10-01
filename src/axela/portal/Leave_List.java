package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Leave_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../sales/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"../portal/leave-list.jsp?all=yes\">Leave List</a><b>:</b>";
	public String LinkAddPage = "<a href=\"../portal/leave-update.jsp?add=yes\">Add New Leave</a>";
	public String LinkExportPage = "";
	public String LinkListPage = "../portal/leave-list.jsp?all=yes";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String advhtml = "";
	public String all = "";
	public String smart = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "", ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String leave_id = "0", active = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Leave ID", "numeric", "leave_id"},
			{"Leave Type", "text", "leavetype_name"},
			{"Leave From Date", "date", "leave_fromdate"},
			{"Leave To Date", "date", "leave_todate"},
			{"Executive", "text", "CONCAT(emp_name, emp_ref_no)"},
			{"Active", "boolean", "leave_active"},
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_leave_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				leave_id = CNumeric(PadQuotes(request.getParameter("leave_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND leave_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Leave!";
				} else if (!leave_id.equals("0")) {
					msg += "<br>Results for Leave ID = " + leave_id + "!";
					StrSearch += " AND leave_id = " + leave_id + "";
				}

				else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("leavestrsql", request).equals("")) {
						StrSearch += GetSession("leavestrsql", request);
					}
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND leave_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				SetSession("leavestrsql", StrSearch, request);
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
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		int count = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String StrJoin = "";
		try {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			if (!msg.equals("")) {

				StrSql = "SELECT emp_name, leave_id,"
						+ " leave_emp_id, "
						+ " leave_leavetype_id, "
						+ " leavetype_name, "
						+ " leave_fromdate, "
						+ " leave_todate, "
						+ " leave_notes, "
						+ " leave_active ";
				StrJoin = " FROM " + compdb(comp_id) + "axela_emp_leave"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp_leave_type ON leavetype_id = leave_leavetype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = leave_emp_id"
						+ " WHERE 1=1 "
						+ ExeAccess
						+ BranchAccess.replace("branch_id", "emp_branch_id")
						+ StrSearch;
				CountSql = " SELECT COUNT(DISTINCT leave_id) ";
				StrSql = StrSql + StrJoin;
				CountSql = CountSql + StrJoin;

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Leaves(s)";
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
						StrSql = StrSql
								+ " GROUP BY leave_id "
								+ " ORDER BY leave_id desc ";
					} else {
						StrSql = StrSql
								+ " ORDER BY leave_id ";
					}
					StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

					crs = processQuery(StrSql, 0);
					if (crs.next()) {

						crs.beforeFirst();
						Str.append("<div class=\"table-bordered\">\n");
						Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<tr align=\"center\">\n");
						Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
						Str.append("<th data-hide=\"phone\">Leave ID</th>\n");
						Str.append("<th data-toggle=\"true\">Employee</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Leave Type</th>\n");
						Str.append("<th data-hide=\"phone\">From</th>\n");
						Str.append("<th data-hide=\"phone\">To</th>\n");
						Str.append("<th data-hide=\"phone\">Description</th>\n");
						Str.append("<th data-hide=\"phone\">Active</th>\n");
						Str.append("<th data-hide=\"phone\">Action</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead><tr>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							count++;
							Str.append("<tr><td align=center>\n").append(count).append("</td>\n");
							Str.append("<td align=center>\n").append(crs.getString("leave_id")).append("</td>\n");
							// Str.append("<td align=center>\n").append(Exename(comp_id, crs.getInt("leave_emp_id"))).append("</td>\n");
							Str.append("<td align=center>\n").append(ExeDetailsPopover(crs.getInt("leave_emp_id"), crs.getString("emp_name"), "")).append("</td>\n");
							Str.append("<td align=center>\n").append(crs.getString("leavetype_name")).append("</td>\n");
							Str.append("<td align=center>\n").append(strToLongDate(crs.getString("leave_fromdate"))).append("</td>\n");
							Str.append("<td align=center>\n").append(strToLongDate(crs.getString("leave_todate"))).append("</td>\n");
							Str.append("<td align=left>\n").append(crs.getString("leave_notes")).append("</td>\n");
							if (crs.getString("leave_active").equals("1")) {
								active = "Yes";
							} else {
								active = "No";
							}

							Str.append("<td align=center>\n").append(active).append("</td>\n");
							Str.append("<td align=center>\n").append("<a href=\"../portal/leave-update.jsp?update=yes&leave_id=" + crs.getString("leave_id") + "\">Update Leave</a>");
							Str.append("</td></tr>\n");
						}
						Str.append("</tbody><tr>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
					}
				}
				else {
					Str.append("<center><div><br><br><br><br><font color=\"red\"><b>No Leave found!</b></font><br><br></div></center>\n");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
