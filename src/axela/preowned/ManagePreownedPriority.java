package axela.preowned;
//Bhagwan Singh 28th June 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManagePreownedPriority extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managepreownedpriority.jsp?all=yes>List Pre Owned Priority</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managepreownedpriority-update.jsp?add=yes>Add Priority...</a>";
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
	public String prioritypreowned_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Priority Pre Owned ID", "numeric", "prioritypreowned_id"},
			{"Name", "text", "prioritypreowned_name"},
			{"Desc", "text", "prioritypreowned_desc"},
			{"Rank", "numeric", "prioritypreowned_rank"},
			{"DueHrs", "numeric", "prioritypreowned_duehrs"},
			{"Trigger1", "numeric", "prioritypreowned_trigger1_hrs"},
			{"Trigger2", "numeric", "prioritypreowned_trigger2_hrs"},
			{"Trigger3", "numeric", "prioritypreowned_trigger3_hrs"},
			{"Trigger4", "numeric", "prioritypreowned_trigger4_hrs"},
			{"Trigger5", "numeric", "prioritypreowned_trigger5_hrs"},
			{"Entry By", "text", "prioritypreowned_entry_id in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "prioritypreowned_entry_date"},
			{"Modified By", "text", "prioritypreowned_modified_id in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "prioritypreowned_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				prioritypreowned_id = CNumeric(PadQuotes(request.getParameter("prioritypreowned_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND prioritypreowned_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Priority!";
					StrSearch = StrSearch + " AND prioritypreowned_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managepreownedpriority.jsp?msg=Priority Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managepreownedpriority.jsp?msg=Priority Moved Down Successfully!"));
				}

				if (!(prioritypreowned_id.equals("0"))) {
					msg = msg + "<br>Results for Pre Owned Priority Type ID = " + prioritypreowned_id + "!";
					StrSearch = StrSearch + " AND prioritypreowned_id = " + prioritypreowned_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND prioritypreowned_id = 0";
					} else {
						msg = "Results for Search!";
					}
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
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "SELECT prioritypreowned_id, prioritypreowned_name, prioritypreowned_desc";

			CountSql = "SELECT COUNT(DISTINCT prioritypreowned_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_priority"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " ORDER BY prioritypreowned_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Priority Details</th>\n");
					Str.append("<th data-hide=\"phone\">Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("prioritypreowned_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managepreownedpriority.jsp?Up=yes&prioritypreowned_id=").append(crs.getString("prioritypreowned_id"))
								.append(" \">Up</a> - <a href=\"managepreownedpriority.jsp?Down=yes&prioritypreowned_id=").append(crs.getString("prioritypreowned_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap><a href=\"managepreownedpriority-update.jsp?update=yes&prioritypreowned_id=").append(crs.getString("prioritypreowned_id"))
								.append(" \">Update Priority</a></td>\n");
					}
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><font color=red><b>No Pre Owned Priority Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int prioritypreowned_rank;
		try {
			prioritypreowned_rank = Integer.parseInt(ExecuteQuery("SELECT prioritypreowned_rank FROM " + compdb(comp_id) + "axela_preowned_priority WHERE prioritypreowned_id = " + prioritypreowned_id
					+ ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(prioritypreowned_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_priority WHERE 1 = 1"));
			if (prioritypreowned_rank != tempRank) {
				tempRank = prioritypreowned_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_priority SET prioritypreowned_rank = " + prioritypreowned_rank + " WHERE prioritypreowned_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_priority SET prioritypreowned_rank = " + tempRank + " WHERE prioritypreowned_rank = " + prioritypreowned_rank
						+ " and prioritypreowned_id = " + prioritypreowned_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int prioritypreowned_rank;
		try {
			prioritypreowned_rank = Integer.parseInt(ExecuteQuery("SELECT prioritypreowned_rank FROM " + compdb(comp_id) + "axela_preowned_priority WHERE prioritypreowned_id = " + prioritypreowned_id
					+ ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(prioritypreowned_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_priority WHERE 1 = 1"));
			if (prioritypreowned_rank != tempRank) {
				tempRank = prioritypreowned_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_priority"
						+ " SET"
						+ " prioritypreowned_rank = " + prioritypreowned_rank + " WHERE prioritypreowned_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_priority"
						+ " SET"
						+ " prioritypreowned_rank=" + tempRank + " WHERE prioritypreowned_rank = " + prioritypreowned_rank + " AND prioritypreowned_id = " + prioritypreowned_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
