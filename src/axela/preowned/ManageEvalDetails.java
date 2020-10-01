//  Bhagwan Singh (10th July 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageEvalDetails extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageevaldetails.jsp?all=yes>List Evaluation Details</a><b>:</b>";
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
	public String all = "";
	public String evaldetails_id = "0";
	public String evaldetails_evalsubhead_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Detail ID", "numeric", "evaldetails_id"},
			{"Detail Name", "text", "evaldetails_name"},
			{"Active", "boolean", "evaldetails_active"},
			{"Rank", "numeric", "evaldetails_rank"},
			// {"Sub Head ID", "numeric", "evaldetails_evalsubhead_id"},
			{"Sub Head Name", "text", "evalsubhead_name"},
			{"Entry By", "text", "evaldetails_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "evaldetails_entry_date"},
			{"Modified By", "text", "evaldetails_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "evaldetails_modified_date"}
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
				evaldetails_id = CNumeric(PadQuotes(request.getParameter("evaldetails_id")));
				evaldetails_evalsubhead_id = CNumeric(PadQuotes(request.getParameter("evalsubhead_id")));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!evaldetails_evalsubhead_id.equals("0")) {
					LinkAddPage = "<a href=manageevaldetails-update.jsp?add=yes&evalsubhead_id=" + evaldetails_evalsubhead_id + ">Add Evaluation Detail...</a>";
				} else {
					LinkAddPage = "<a href=manageevaldetails-update.jsp?add=yes>Add Evaluation Detail...</a>";
				}

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND evaldetails_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Evaluation Details(s)!";
					StrSearch = StrSearch + " AND evaldetails_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageevaldetails.jsp?msg=Details Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageevaldetails.jsp?msg=Details Moved Down Successfully!"));
				}

				if (!(evaldetails_id.equals("0"))) {
					msg = msg + "<br>Results for Evaluation Details ID = " + evaldetails_id + "!";
					StrSearch = StrSearch + " AND evaldetails_id = " + evaldetails_id + "";
				} else if (!evaldetails_evalsubhead_id.equals("0")) {
					msg = msg + "<br>Results for Sub Head ID = " + evaldetails_evalsubhead_id + "!";
					StrSearch = StrSearch + " AND evaldetails_evalsubhead_id = " + evaldetails_evalsubhead_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND evaldetails_id = 0";
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
			StrSql = "SELECT evaldetails_id, evaldetails_name, evaldetails_evalsubhead_id, evalsubhead_id, evalsubhead_name,"
					+ " COALESCE(evalhead_id, '0') AS evalhead_id, COALESCE(evalhead_name, '') AS evalhead_name";

			CountSql = "SELECT Count(DISTINCT evaldetails_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_eval_details"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_subhead ON evalsubhead_id = evaldetails_evalsubhead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval_head ON evalhead_id = evalsubhead_evalhead_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY evaldetails_rank ORDER BY evaldetails_rank";
				// SOP("List Data=====" + StrSqlBreaker(StrSql));
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Evaluation Detail</th>\n");
					Str.append("<th>Evaluation Sub Head</th>\n");
					Str.append("<th data-hide=\"phone\">Evaluation Head</th>\n");
					Str.append("<th data-hide=\"phone\">Order</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("evaldetails_name")).append("</td>\n");
						Str.append("<td valign=top align=left > <a href=\"manageevalsubhead.jsp?evalsubhead_id=").append(crs.getString("evaldetails_evalsubhead_id")).append("\">")
								.append(crs.getString("evalsubhead_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left > <a href=\"manageevalhead.jsp?evalhead_id=").append(crs.getString("evalhead_id")).append("\">").append(crs.getString("evalhead_name"))
								.append("</a></td>\n");
						Str.append("<td valign=top align=center><a href=\"manageevaldetails.jsp?Up=yes&evaldetails_id=").append(crs.getString("evaldetails_id"))
								.append(" \">Up</a> - <a href=\"manageevaldetails.jsp?Down=yes&evaldetails_id=").append(crs.getString("evaldetails_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top align=left > <a href=\"manageevaldetails-update.jsp?update=yes&evaldetails_id=").append(crs.getString("evaldetails_id"))
								.append(" \">Update Evaluation Detail</a></td>\n");
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
				Str.append("<br><br><br><br><font color=red><b>No Evaluation Details(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int evaldetails_rank;
		try {
			evaldetails_rank = Integer.parseInt(ExecuteQuery("SELECT evaldetails_rank FROM " + compdb(comp_id) + "axela_preowned_eval_details WHERE evaldetails_id = " + evaldetails_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(evaldetails_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_eval_details WHERE 1 = 1"));
			if (evaldetails_rank != tempRank) {
				tempRank = evaldetails_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_details SET evaldetails_rank = " + evaldetails_rank + " WHERE evaldetails_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_details SET evaldetails_rank = " + tempRank + " WHERE evaldetails_rank = " + evaldetails_rank + " AND evaldetails_id = "
						+ evaldetails_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int evaldetails_rank;
		try {
			evaldetails_rank = Integer.parseInt(ExecuteQuery("SELECT evaldetails_rank FROM " + compdb(comp_id) + "axela_preowned_eval_details WHERE evaldetails_id = " + evaldetails_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(evaldetails_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_eval_details WHERE 1 = 1"));
			if (evaldetails_rank != tempRank) {
				tempRank = evaldetails_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " SET"
						+ " evaldetails_rank = " + evaldetails_rank + " WHERE evaldetails_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_details"
						+ " SET"
						+ " evaldetails_rank=" + tempRank + " WHERE evaldetails_rank = " + evaldetails_rank + " AND evaldetails_id = " + evaldetails_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
