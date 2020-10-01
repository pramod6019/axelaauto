package axela.preowned;
//Dilip 10 Jul 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageEvalSubHead extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageevalsubhead.jsp?all=yes>List Evaluation Sub Head</a><b>:</b>";
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
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String evalsubhead_id = "0";
	public String evalsubhead_evalhead_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Sub Head ID", "numeric", "evalsubhead_id"},
			{"Sub Head Name", "text", "evalsubhead_name"},
			// {"Head ID", "numeric", "evalhead_id"},
			{"Head Name", "text", "evalhead_name"},
			{"Rank", "numeric", "evalsubhead_rank"},
			{"Entry By", "text", "evalsubhead_name in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "evalsubhead_entry_date"},
			{"Modified By", "text", "evalsubhead_modified_id in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "evalsubhead_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				evalsubhead_id = CNumeric(PadQuotes(request.getParameter("evalsubhead_id")));
				evalsubhead_evalhead_id = CNumeric(PadQuotes(request.getParameter("evalhead_id")));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!evalsubhead_evalhead_id.equals("0")) {
					LinkAddPage = "<a href=manageevalsubhead-update.jsp?add=yes&evalhead_id=" + evalsubhead_evalhead_id + ">Add Evaluation Sub Head...</a>";
				} else {
					LinkAddPage = "<a href=manageevalsubhead-update.jsp?add=yes>Add Evaluation Sub Head...</a>";
				}
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND evalsubhead_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Evaluation Sub Head(s)!";
					StrSearch = StrSearch + " AND evalsubhead_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageevalsubhead.jsp?msg=Sub Head Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageevalsubhead.jsp?msg=Sub Head Moved Down Successfully!"));
				}

				if (!evalsubhead_evalhead_id.equals("0")) {
					msg = msg + "<br>Results for Evaluation Head ID = " + evalsubhead_evalhead_id + "!";
					StrSearch = StrSearch + " AND evalsubhead_evalhead_id = " + evalsubhead_evalhead_id + "";
				} else if (!(evalsubhead_id.equals("0"))) {
					msg = msg + "<br>Results for Evaluation Sub Head ID = " + evalsubhead_id + "!";
					StrSearch = StrSearch + " AND evalsubhead_id = " + evalsubhead_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND evalsubhead_id = 0";
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

			// to know no of records depending on search
			StrSql = "SELECT evalhead_id, COALESCE(COUNT(DISTINCT(evaldetails_id)),0) AS detailscount, evalsubhead_id, evalsubhead_name, evalhead_name";

			CountSql = "SELECT COUNT(DISTINCT evalsubhead_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_head ON evalhead_id = evalsubhead_evalhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_details ON evaldetails_evalsubhead_id = evalsubhead_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY evalsubhead_rank ORDER BY evalsubhead_rank";
				try {
					// SOP("StrSql=====" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Evaluation Sub Head</th>\n");
					Str.append("<th>Evaluation Head</th>\n");
					Str.append("<th data-hide=\"phone\">Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("evalsubhead_name")).append("</td>\n");
						Str.append("<td valign=top> <a href=\"manageevalhead.jsp?evalhead_id=").append(crs.getString("evalhead_id")).append("\">").append(crs.getString("evalhead_name"))
								.append("</a></td>\n");
						Str.append("<td valign=top align=center><a href=\"manageevalsubhead.jsp?Up=yes&evalsubhead_id=").append(crs.getString("evalsubhead_id"))
								.append(" \">Up</a> - <a href=\"manageevalsubhead.jsp?Down=yes&evalsubhead_id=").append(crs.getString("evalsubhead_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top> <a href=\"manageevalsubhead-update.jsp?update=yes&evalsubhead_id=").append(crs.getString("evalsubhead_id"))
								.append(" \">Update Evaluation Sub Head</a>\n");
						Str.append("<br><a href=\"manageevaldetails.jsp?evalsubhead_id=").append(crs.getString("evalsubhead_id")).append(" \">List Evaluation Detail</a>" + " (")
								.append(crs.getString("detailscount")).append(")");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
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
				Str.append("<br><br><br><br><font color=red><b>No Evaluation Sub Head(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int evalsubhead_rank;
		try {
			evalsubhead_rank = Integer.parseInt(ExecuteQuery("SELECT evalsubhead_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE evalsubhead_id = " + evalsubhead_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(evalsubhead_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (evalsubhead_rank != tempRank) {
				tempRank = evalsubhead_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET evalsubhead_rank = " + evalsubhead_rank + " WHERE evalsubhead_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET evalsubhead_rank = " + tempRank + " WHERE evalsubhead_rank = " + evalsubhead_rank + " AND evalsubhead_id = "
						+ evalsubhead_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int evalsubhead_rank;
		try {
			evalsubhead_rank = Integer.parseInt(ExecuteQuery("SELECT evalsubhead_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE evalsubhead_id = " + evalsubhead_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(evalsubhead_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (evalsubhead_rank != tempRank) {
				tempRank = evalsubhead_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " evalsubhead_rank = " + evalsubhead_rank + " WHERE evalsubhead_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " evalsubhead_rank=" + tempRank + " WHERE evalsubhead_rank = " + evalsubhead_rank + " AND evalsubhead_id = " + evalsubhead_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
