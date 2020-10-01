//Bhagwan Singh 09/01/2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTicketDepartment extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=manageticketdept.jsp?all=yes>List Department</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageticketdept-update.jsp?add=yes>Add Department...</a>";
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
	public String ticket_dept_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Department ID", "numeric", "ticket_dept_id"},
			{"Name", "text", "ticket_dept_name"},
			{"Desc", "text", "ticket_dept_desc"},
			{"Rank", "numeric", "ticket_dept_rank"},
			{"DueHrs", "numeric", "ticket_dept_duehrs"},
			{"Trigger1", "numeric", "ticket_dept_trigger1_hrs"},
			{"Trigger2", "numeric", "ticket_dept_trigger2_hrs"},
			{"Trigger3", "numeric", "ticket_dept_trigger3_hrs"},
			{"Trigger4", "numeric", "ticket_dept_trigger4_hrs"},
			{"Trigger5", "numeric", "ticket_dept_trigger5_hrs"},
			{"Entry By", "text", "ticket_dept_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "ticket_dept_entry_date"},
			{"Modified By", "text", "ticket_dept_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "ticket_dept_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				ticket_dept_id = CNumeric(PadQuotes(request.getParameter("ticket_dept_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND ticket_dept_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Departments!";
					StrSearch = StrSearch + " and ticket_dept_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageticketdept.jsp?msg=Department Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageticketdept.jsp?msg=Department Moved Down Successfully!"));
				}

				if (!ticket_dept_id.equals("0")) {
					msg = msg + "<br>Results for Department ID = " + ticket_dept_id + "!";
					StrSearch = StrSearch + " and ticket_dept_id = " + ticket_dept_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND ticket_dept_id = 0";
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
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name, ticket_dept_desc";

			CountSql = "SELECT Count(distinct ticket_dept_id)";
			//
			SqlJoin = " from " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " where 1=1";
			//
			StrSql = StrSql + SqlJoin + StrSearch;
			CountSql = CountSql + SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by ticket_dept_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Department Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");

					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("ticket_dept_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"manageticketdept.jsp?Up=yes&ticket_dept_id=").append(crs.getString("ticket_dept_id"))
								.append(" \">Up</a> - <a href=\"manageticketdept.jsp?Down=yes&ticket_dept_id=").append(crs.getString("ticket_dept_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap><a href=\"manageticketdept-update.jsp?update=yes&ticket_dept_id=").append(crs.getString("ticket_dept_id"))
								.append(" \">Update Department</a></td>\n");
					}
					Str.append("</tr>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No Department(s) Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int ticket_dept_rank;
		try {
			ticket_dept_rank = Integer.parseInt(ExecuteQuery("SELECT ticket_dept_rank from " + compdb(comp_id) + "axela_service_ticket_dept where ticket_dept_id = " + ticket_dept_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select min(ticket_dept_rank) as min1 from " + compdb(comp_id) + "axela_service_ticket_dept where 1 = 1"));
			if (ticket_dept_rank != tempRank) {
				tempRank = ticket_dept_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_dept set ticket_dept_rank = " + ticket_dept_rank + " where ticket_dept_rank = " + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_dept set ticket_dept_rank = " + tempRank + " where ticket_dept_rank = " + ticket_dept_rank + " and ticket_dept_id = "
						+ ticket_dept_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int ticket_dept_rank;
		try {
			ticket_dept_rank = Integer.parseInt(ExecuteQuery("SELECT ticket_dept_rank from " + compdb(comp_id) + "axela_service_ticket_dept where ticket_dept_id = " + ticket_dept_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(ticket_dept_rank) as max1 from " + compdb(comp_id) + "axela_service_ticket_dept where 1 = 1"));
			if (ticket_dept_rank != tempRank) {
				tempRank = ticket_dept_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_dept set ticket_dept_rank = " + ticket_dept_rank + " where ticket_dept_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_dept set ticket_dept_rank = " + tempRank + " where ticket_dept_rank = " + ticket_dept_rank + " and ticket_dept_id = "
						+ ticket_dept_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
