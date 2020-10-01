package axela.insurance;
//Dilip Kumar 18 APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageInsurFollowupPriority extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageinsurfollowuppriority.jsp?all=yes>List Insurance Follow-up Priority</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
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
	public String priorityinsurfollowup_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Priority insur FollowUp ID", "numeric", "priorityinsurfollowup_id"},
			{"Name", "text", "priorityinsurfollowup_name"},
			{"Desc", "text", "priorityinsurfollowup_desc"},
			{"Rank", "numeric", "priorityinsurfollowup_rank"},
			{"DueHrs", "numeric", "priorityinsurfollowup_duehrs"},
			{"Trigger1", "numeric", "priorityinsurfollowup_trigger1_hrs"},
			{"Trigger2", "numeric", "priorityinsurfollowup_trigger2_hrs"},
			{"Trigger3", "numeric", "priorityinsurfollowup_trigger3_hrs"},
			{"Trigger4", "numeric", "priorityinsurfollowup_trigger4_hrs"},
			{"Trigger5", "numeric", "priorityinsurfollowup_trigger5_hrs"},
			{"Entry By", "text", "priorityinsurfollowup_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "priorityinsurfollowup_entry_date"},
			{"Modified By", "text", "priorityinsurfollowup_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "priorityinsurfollowup_modified_date"}
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
				priorityinsurfollowup_id = CNumeric(PadQuotes(request.getParameter("priorityinsurfollowup_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND priorityinsurfollowup_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for All Insurance Follow-up Priority!";
					StrSearch = StrSearch + " AND priorityinsurfollowup_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageinsurfollowuppriority.jsp?msg=Priority Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageinsurfollowuppriority.jsp?msg=Priority Moved Down Successfully!"));
				}

				if (!(priorityinsurfollowup_id.equals("0"))) {
					msg = msg + "<br>Results for Insurance Follow-up Priority ID = " + priorityinsurfollowup_id + "!";
					StrSearch = StrSearch + " AND priorityinsurfollowup_id = " + priorityinsurfollowup_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND priorityinsurfollowup_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("priorityinsurfollowupstrsql", StrSearch, request);
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "Select priorityinsurfollowup_id, priorityinsurfollowup_name, priorityinsurfollowup_desc";

			CountSql = "SELECT COUNT(DISTINCT priorityinsurfollowup_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " WHERE 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by priorityinsurfollowup_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th class=\"table-responsive table-bordered\" width=5%>#</th>\n");
					Str.append("<th>Priority Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("priorityinsurfollowup_name")).append("</font></td>\n");
						Str.append("<td valign=top align=center><a href=\"manageinsurfollowuppriority.jsp?Up=yes&priorityinsurfollowup_id=").append(crs.getString("priorityinsurfollowup_id"))
								.append(" \">Up</a> - <a href=\"manageinsurfollowuppriority.jsp?Down=yes&priorityinsurfollowup_id=").append(crs.getString("priorityinsurfollowup_id"))
								.append(" \">Down</a></td>\n");
						Str.append("<td align=left nowrap><a href=\"manageinsurfollowuppriority-update.jsp?update=yes&priorityinsurfollowup_id=").append(crs.getString("priorityinsurfollowup_id"))
								.append(" \">Update Insurance Follow-up Priority</a></td>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No Insurance Follow-up Priority Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int priorityinsurfollowup_rank;
		try {
			priorityinsurfollowup_rank = Integer.parseInt(ExecuteQuery("SELECT priorityinsurfollowup_rank from " + compdb(comp_id)
					+ "axela_insurance_followup_priority where priorityinsurfollowup_id = " + priorityinsurfollowup_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select min(priorityinsurfollowup_rank) as min1 from " + compdb(comp_id) + "axela_insurance_followup_priority where 1=1"));
			if (priorityinsurfollowup_rank != tempRank) {
				tempRank = priorityinsurfollowup_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_insurance_followup_priority"
						+ " SET"
						+ " priorityinsurfollowup_rank = " + priorityinsurfollowup_rank + ""
						+ " WHERE priorityinsurfollowup_rank = " + tempRank + "";
				updateQuery(StrSql);

				StrSql = "Update " + compdb(comp_id) + "axela_insurance_followup_priority"
						+ " SET"
						+ " priorityinsurfollowup_rank = " + tempRank + ""
						+ " WHERE priorityinsurfollowup_rank = " + priorityinsurfollowup_rank + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int priorityinsurfollowup_rank;
		try {
			priorityinsurfollowup_rank = Integer.parseInt(ExecuteQuery("SELECT priorityinsurfollowup_rank from " + compdb(comp_id)
					+ "axela_insurance_followup_priority where priorityinsurfollowup_id = " + priorityinsurfollowup_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(priorityinsurfollowup_rank) as max1 from " + compdb(comp_id) + "axela_insurance_followup_priority where 1=1"));
			if (priorityinsurfollowup_rank != tempRank) {
				tempRank = priorityinsurfollowup_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_insurance_followup_priority"
						+ " SET"
						+ " priorityinsurfollowup_rank = " + priorityinsurfollowup_rank + ""
						+ " WHERE priorityinsurfollowup_rank = " + tempRank + "";
				updateQuery(StrSql);

				StrSql = "Update " + compdb(comp_id) + "axela_insurance_followup_priority"
						+ " SET"
						+ " priorityinsurfollowup_rank = " + tempRank + ""
						+ " WHERE priorityinsurfollowup_rank = " + priorityinsurfollowup_rank + ""
						+ " AND priorityinsurfollowup_id = " + priorityinsurfollowup_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
