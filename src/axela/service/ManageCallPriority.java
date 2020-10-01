package axela.service;

/**
 * @author Dilip Kumar P 04 Apr 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageCallPriority extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managecallpriority.jsp?all=yes>List Call Priority</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managecallpriority-update.jsp?add=yes>Add Call Priority...</a>";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String prioritycall_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Prioritycall ID", "numeric", "prioritycall_id"},
			{"Name", "text", "prioritycall_name"},
			{"Desc", "text", "prioritycall_desc"},
			{"Rank", "numeric", "prioritycall_rank"},
			{"DueHrs", "numeric", "prioritycall_duehrs"},
			{"Trigger1", "numeric", "prioritycall_trigger1_hrs"},
			{"Trigger2", "numeric", "prioritycall_trigger2_hrs"},
			{"Trigger3", "numeric", "prioritycall_trigger3_hrs"},
			{"Trigger4", "numeric", "prioritycall_trigger4_hrs"},
			{"Trigger5", "numeric", "prioritycall_trigger5_hrs"},
			{"Entry By", "text", "prioritycall_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "prioritycall_entry_date"},
			{"Modified By", "text", "prioritycall_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "prioritycall_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				prioritycall_id = CNumeric(PadQuotes(request.getParameter("prioritycall_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND prioritycall_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Call Priority!";
					StrSearch = StrSearch + " and prioritycall_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managecallpriority.jsp?msg=Priority Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managecallpriority.jsp?msg=Priority Moved Down Successfully!"));
				}

				if (!(prioritycall_id.equals("0"))) {
					msg = msg + "<br>Results for Call  Priority Type ID = " + prioritycall_id + "!";
					StrSearch = StrSearch + " and prioritycall_id = " + prioritycall_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " and prioritycall_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
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
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "Select prioritycall_id, prioritycall_name, prioritycall_desc";

			CountSql = "SELECT count(distinct prioritycall_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_service_call_priority"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " ORDER BY prioritycall_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<br><br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5%>#</th>\n");
					Str.append("<th>Call Priority Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th width=20%>Actions</th>\n");

					Str.append("</tr>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("prioritycall_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managecallpriority.jsp?Up=yes&prioritycall_id=").append(crs.getString("prioritycall_id"))
								.append(" \">Up</a> - <a href=\"managecallpriority.jsp?Down=yes&prioritycall_id=").append(crs.getString("prioritycall_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap><a href=\"managecallpriority-update.jsp?update=yes&prioritycall_id=").append(crs.getString("prioritycall_id"))
								.append(" \">Update Call Priority</a></td>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No call Priority Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int prioritycall_rank;
		try {
			prioritycall_rank = Integer.parseInt(ExecuteQuery("SELECT prioritycall_rank from " + compdb(comp_id) + "axela_service_call_priority where  prioritycall_id=" + prioritycall_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT min(prioritycall_rank) as min1 from " + compdb(comp_id) + "axela_service_call_priority where 1 = 1"));
			if (prioritycall_rank != tempRank) {
				tempRank = prioritycall_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call_priority set prioritycall_rank = " + prioritycall_rank + " where prioritycall_rank = " + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call_priority set prioritycall_rank = " + tempRank + " where prioritycall_rank = " + prioritycall_rank
						+ " AND prioritycall_id = " + prioritycall_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int prioritycall_rank;
		try {
			prioritycall_rank = Integer.parseInt(ExecuteQuery("SELECT prioritycall_rank from " + compdb(comp_id) + "axela_service_call_priority where prioritycall_id = " + prioritycall_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(prioritycall_rank) as max1 from " + compdb(comp_id) + "axela_service_call_priority where 1 = 1"));
			if (prioritycall_rank != tempRank) {
				tempRank = prioritycall_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_call_priority set prioritycall_rank = " + prioritycall_rank + " where prioritycall_rank = " + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_call_priority set prioritycall_rank = " + tempRank + " where prioritycall_rank = " + prioritycall_rank + " and prioritycall_id="
						+ prioritycall_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
