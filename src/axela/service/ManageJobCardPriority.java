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

public class ManageJobCardPriority extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managejobcardpriority.jsp?all=yes>List Job Card Priority</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managejobcardpriority-update.jsp?add=yes>Add Job Card Priority...</a>";
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
	public String priorityjc_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"PrioritySc ID", "numeric", "priorityjc_id"},
			{"Name", "text", "priorityjc_name"},
			{"Desc", "text", "priorityjc_desc"},
			{"Rank", "numeric", "priorityjc_rank"},
			{"DueHrs", "numeric", "priorityjc_duehrs"},
			{"Trigger1", "numeric", "priorityjc_trigger1_hrs"},
			{"Trigger2", "numeric", "priorityjc_trigger2_hrs"},
			{"Trigger3", "numeric", "priorityjc_trigger3_hrs"},
			{"Trigger4", "numeric", "priorityjc_trigger4_hrs"},
			{"Trigger5", "numeric", "priorityjc_trigger5_hrs"},
			{"Entry By", "text", "priorityjc_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "priorityjc_entry_date"},
			{"Modified By", "text", "priorityjc_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "priorityjc_modified_date"}
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
				priorityjc_id = CNumeric(PadQuotes(request.getParameter("priorityjc_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND priorityjc_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Job Card Priority!";
					StrSearch = StrSearch + " and priorityjc_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managejobcardpriority.jsp?msg=Priority Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managejobcardpriority.jsp?msg=Priority Moved Down Successfully!"));
				}

				if (!(priorityjc_id.equals("0"))) {
					msg = msg + "<br>Results for Job Card  Priority Type ID = " + priorityjc_id + "!";
					StrSearch = StrSearch + " and priorityjc_id = " + priorityjc_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND priorityjc_id = 0";
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
			StrSql = "Select priorityjc_id, priorityjc_name, priorityjc_desc";

			CountSql = "SELECT Count(distinct priorityjc_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_service_jc_priority"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by priorityjc_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<br><div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Job Card Priority Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					Str.append("</tr>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("priorityjc_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managejobcardpriority.jsp?Up=yes&priorityjc_id=").append(crs.getString("priorityjc_id"))
								.append(" \">Up</a> - <a href=\"managejobcardpriority.jsp?Down=yes&priorityjc_id=").append(crs.getString("priorityjc_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap><a href=\"managejobcardpriority-update.jsp?update=yes&priorityjc_id=").append(crs.getString("priorityjc_id"))
								.append(" \">Update Job Card Priority</a></td>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No Job Card Priority Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int priorityjc_rank;
		try {
			priorityjc_rank = Integer.parseInt(ExecuteQuery("SELECT priorityjc_rank from " + compdb(comp_id) + "axela_service_jc_priority where  priorityjc_id=" + priorityjc_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select min(priorityjc_rank) as min1 from " + compdb(comp_id) + "axela_service_jc_priority where 1 = 1"));
			if (priorityjc_rank != tempRank) {
				tempRank = priorityjc_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_priority set priorityjc_rank = " + priorityjc_rank + " where priorityjc_rank = " + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_priority set priorityjc_rank = " + tempRank + " where priorityjc_rank = " + priorityjc_rank + " and priorityjc_id = "
						+ priorityjc_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int priorityjc_rank;
		try {
			priorityjc_rank = Integer.parseInt(ExecuteQuery("SELECT priorityjc_rank from " + compdb(comp_id) + "axela_service_jc_priority where priorityjc_id = " + priorityjc_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(priorityjc_rank) as max1 from " + compdb(comp_id) + "axela_service_jc_priority where 1 = 1"));
			if (priorityjc_rank != tempRank) {
				tempRank = priorityjc_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_priority set priorityjc_rank = " + priorityjc_rank + " where priorityjc_rank = " + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_priority set priorityjc_rank = " + tempRank + " where priorityjc_rank = " + priorityjc_rank + " and priorityjc_id="
						+ priorityjc_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
