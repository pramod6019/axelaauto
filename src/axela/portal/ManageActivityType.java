package axela.portal;

/**
 * @Gurumurthy TS 11 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageActivityType extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp </li><li><a href=../portal/manager.jsp>Business Manager</a> &gt;&nbsp </li><li><a href=manageactivitytype.jsp?all=yes>List Activity Type</a><b>:</b></li>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageactivitytype-update.jsp?add=yes>Add Activity Type...</a>";
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
	public String type_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Type ID", "numeric", "type_id"},
			{"Name", "text", "type_name"},
			{"Desc", "text", "type_desc"},
			{"Rank", "numeric", "type_rank"},
			{"DueHrs", "numeric", "type_duehrs"},
			{"Trigger1", "numeric", "type_trigger1_hrs"},
			{"Trigger2", "numeric", "type_trigger2_hrs"},
			{"Trigger3", "numeric", "type_trigger3_hrs"},
			{"Trigger4", "numeric", "type_trigger4_hrs"},
			{"Trigger5", "numeric", "type_trigger5_hrs"},
			{
					"Entry By",
					"text",
					"type_entry_id in (select emp_id from " + compdb(comp_id)
							+ "axela_emp where emp_name"},
			{"Entry Date", "date", "type_entry_date"},
			{
					"Modified By",
					"text",
					"type_modified_id in (select emp_id from "
							+ compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "type_modified_date"}};

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
				type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND type_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Activity Types!";
					StrSearch = StrSearch + " and type_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response
							.encodeRedirectURL("manageactivitytype.jsp?msg=Type Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response
							.encodeRedirectURL("manageactivitytype.jsp?msg=Type Moved Down Successfully!"));
				}

				if (!(type_id.equals("0"))) {
					msg = msg + "<br>Results for Activity Type ID = " + type_id
							+ "!";
					StrSearch = StrSearch + " and type_id =" + type_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
			StrSql = "Select type_id, type_name, type_desc";

			CountSql = "SELECT Count(distinct type_id)";
			//
			SqlJoin = " from " + compdb(comp_id) + "axela_activity_type"
					+ " where 1=1";
			//
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by type_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Activity Type Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("type_name"))
								.append("</td>\n");
						Str.append(
								"<td align=center><a href=\"manageactivitytype.jsp?Up=yes&type_id=")
								.append(crs.getString("type_id"))
								.append(" \">Up</a> - <a href=\"manageactivitytype.jsp?Down=yes&type_id=")
								.append(crs.getString("type_id"))
								.append(" \">Down</a></td>\n");
						Str.append(
								"<td><a href=\"manageactivitytype-update.jsp?update=yes&type_id=")
								.append(crs.getString("type_id"))
								.append(" \">Update Activity Type</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><font color=red><b>No Deparment Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int type_rank;
		try {
			type_rank = Integer.parseInt(ExecuteQuery("SELECT type_rank from "
					+ compdb(comp_id) + "axela_activity_type where type_id="
					+ type_id + ""));
			tempRank = Integer
					.parseInt(ExecuteQuery("select min(type_rank) as min1 from "
							+ compdb(comp_id) + "axela_activity_type where 1=1"));
			if (type_rank != tempRank) {
				tempRank = type_rank - 1;
				StrSql = "update " + compdb(comp_id)
						+ "axela_activity_type set type_rank=" + type_rank
						+ " where type_rank=" + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "update " + compdb(comp_id)
						+ "axela_activity_type set type_rank=" + tempRank
						+ " where type_rank=" + type_rank + " and type_id="
						+ type_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void movedown() {
		int tempRank;
		int type_rank;
		try {
			type_rank = Integer.parseInt(ExecuteQuery("SELECT type_rank from "
					+ compdb(comp_id) + "axela_activity_type where type_id="
					+ type_id + ""));
			tempRank = Integer
					.parseInt(ExecuteQuery("select max(type_rank) as max1 from "
							+ compdb(comp_id) + "axela_activity_type where 1=1"));
			if (type_rank != tempRank) {
				tempRank = type_rank + 1;
				StrSql = "update " + compdb(comp_id)
						+ "axela_activity_type set type_rank=" + type_rank
						+ " where type_rank=" + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "update " + compdb(comp_id)
						+ "axela_activity_type set type_rank=" + tempRank
						+ " where type_rank=" + type_rank + " and type_id="
						+ type_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
