//Bhagwan Singh 10/01/2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTicketStatus extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=manageticketstatus.jsp?all=yes>List Ticket Status</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageticketstatus-update.jsp?add=yes>Add Ticket Status...</a>";
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
	public String ticketstatus_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Status ID", "numeric", "ticketstatus_id"},
			{"Name", "text", "ticketstatus_name"},
			{"Rank", "numeric", "ticketstatus_rank"},
			{"Entry By", "text", "ticketstatus_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "ticketstatus_entry_date"},
			{"Modified By", "text", "ticketstatus_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "ticketstatus_modified_date"}
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
				ticketstatus_id = CNumeric(PadQuotes(request.getParameter("ticketstatus_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND ticketstatus_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Ticket Status!";
					StrSearch = StrSearch + " and ticketstatus_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageticketstatus.jsp?msg=Ticket Status Moved Up Successfully!"));
				}

				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageticketstatus.jsp?msg=Ticket Status Moved Down Successfully!"));
				}

				if (!(ticketstatus_id.equals("0"))) {
					msg = msg + "<br>Results for Ticket Status ID = " + ticketstatus_id + "!";
					StrSearch = StrSearch + " and ticketstatus_id = " + ticketstatus_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND ticketstatus_id = 0";
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
			StrSql = "SELECT ticketstatus_id, ticketstatus_name, ticketstatus_rank";

			CountSql = "SELECT COUNT(DISTINCT ticketstatus_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket_status"
					+ " WHERE 1=1";

			StrSql = StrSql + SqlJoin + StrSearch;
			CountSql = CountSql + SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql += " ORDER BY ticketstatus_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Ticket Status Details</th>\n");
					Str.append("<th >Order</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("ticketstatus_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"manageticketstatus.jsp?Up=yes&ticketstatus_id=").append(crs.getString("ticketstatus_id")).append("\">Up</a> - ");
						Str.append("<a href=\"manageticketstatus.jsp?Down=yes&ticketstatus_id=").append(crs.getString("ticketstatus_id")).append("\">Down</a></td>\n");
						Str.append("<td valign=top nowrap>");
						if (!crs.getString("ticketstatus_id").equals("3")) {
							Str.append("<a href=\"manageticketstatus-update.jsp?update=yes&ticketstatus_id=").append(crs.getString("ticketstatus_id")).append(" \">Update Ticket Status</a></td>\n");
						} else {
							Str.append("&nbsp;");
						}
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><font color=red><b>No Ticket Status Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int ticketstatus_rank;
		try {
			ticketstatus_rank = Integer.parseInt(ExecuteQuery("SELECT ticketstatus_rank from " + compdb(comp_id) + "axela_service_ticket_status where  ticketstatus_id=" + ticketstatus_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select min(ticketstatus_rank) as min1 from " + compdb(comp_id) + "axela_service_ticket_status where 1 = 1"));
			if (ticketstatus_rank != tempRank) {
				tempRank = ticketstatus_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_status set ticketstatus_rank = " + ticketstatus_rank + " where ticketstatus_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_status set ticketstatus_rank = " + tempRank + " where ticketstatus_rank = " + ticketstatus_rank
						+ " and ticketstatus_id = " + ticketstatus_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int ticketstatus_rank;
		try {
			ticketstatus_rank = Integer.parseInt(ExecuteQuery("SELECT ticketstatus_rank from " + compdb(comp_id) + "axela_service_ticket_status where ticketstatus_id = " + ticketstatus_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(ticketstatus_rank) as max1 from " + compdb(comp_id) + "axela_service_ticket_status where 1 = 1"));
			if (ticketstatus_rank != tempRank) {
				tempRank = ticketstatus_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_status set ticketstatus_rank = " + ticketstatus_rank + " where ticketstatus_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_ticket_status set ticketstatus_rank = " + tempRank + " where ticketstatus_rank = " + ticketstatus_rank
						+ " and ticketstatus_id = " + ticketstatus_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
