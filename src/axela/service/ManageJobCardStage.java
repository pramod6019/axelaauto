package axela.service;

/**
 * @author Dilip Kumar P
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobCardStage extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=managejobcardstage.jsp?all=yes>List Job Card Stage</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managejobcardstage-update.jsp?add=yes>Add Job Card Stage...</a>";
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
	public String stage_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Stage ID", "numeric", "jcstage_id"},
			{"Name", "text", "jcstage_name"},
			{"Desc", "text", "jcstage_desc"},
			{"Rank", "numeric", "jcstage_rank"},
			{"DueHrs", "numeric", "jcstage_duehrs"},
			{"Trigger1", "numeric", "jcstage_trigger1_hrs"},
			{"Trigger2", "numeric", "jcstage_trigger2_hrs"},
			{"Trigger3", "numeric", "jcstage_trigger3_hrs"},
			{"Trigger4", "numeric", "jcstage_trigger4_hrs"},
			{"Trigger5", "numeric", "jcstage_trigger5_hrs"},
			{"Entry By", "text", "jcstage_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "jcstage_entry_date"},
			{"Modified By", "text", "jcstage_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "jcstage_modified_date"}
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
				stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND jcstage_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Job Card Stage!";
					StrSearch = StrSearch + " AND jcstage_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Moved Up Successfully!"));
				} else if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Moved Down Successfully!"));
				}

				if (!stage_id.equals("0")) {
					msg = msg + "<br>Results for Job Card Stage ID = " + stage_id + "!";
					StrSearch = StrSearch + " AND jcstage_id = " + stage_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND jcstage_id = 0";
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
			StrSql = "SELECT jcstage_id, jcstage_name, jcstage_desc";

			CountSql = "SELECT COUNT(DISTINCT jcstage_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " ORDER BY jcstage_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th width=5% data-toggle=\"true\"><span class=\"footable-toggle\"></span>#</th>\n");
					Str.append("<th>Job Card Stage Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th width=20%>Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("jcstage_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managejobcardstage.jsp?Up=yes&stage_id=").append(crs.getString("jcstage_id"))
								.append(" \">Up</a> - <a href=\"managejobcardstage.jsp?Down=yes&stage_id=").append(crs.getString("jcstage_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top nowrap>");
						if (!crs.getString("jcstage_id").equals("5") && !crs.getString("jcstage_id").equals("6")) {
							Str.append("<a href=\"managejobcardstage-update.jsp?update=yes&stage_id=").append(crs.getString("jcstage_id")).append(" \">Update Job Card Stage</a>");
						} else {
							Str.append("&nbsp;");
						}
						Str.append("</td>\n</tr>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No Job Card Stage Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int stage_rank;
		try {
			stage_rank = Integer.parseInt(ExecuteQuery("SELECT jcstage_rank"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE jcstage_id = " + stage_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(jcstage_rank) AS min1"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE 1 = 1"));
			if (stage_rank != tempRank) {
				tempRank = stage_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
						+ " SET jcstage_rank = " + stage_rank + ""
						+ " WHERE jcstage_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
						+ " SET jcstage_rank = " + tempRank + ""
						+ " WHERE jcstage_rank = " + stage_rank + ""
						+ " AND jcstage_id = " + stage_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int stage_rank;
		try {
			stage_rank = Integer.parseInt(ExecuteQuery("SELECT jcstage_rank"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE jcstage_id = " + stage_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(jcstage_rank) AS max1"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE 1 = 1"));
			if (stage_rank != tempRank) {
				tempRank = stage_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
						+ " SET jcstage_rank = " + stage_rank + ""
						+ " WHERE jcstage_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
						+ " SET jcstage_rank = " + tempRank + ""
						+ " WHERE jcstage_rank = " + stage_rank + ""
						+ " AND jcstage_id = " + stage_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
