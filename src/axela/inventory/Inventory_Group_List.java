package axela.inventory;
//Bhagwan Singh 23/01/2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Inventory_Group_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=inventory-group-list.jsp?all=yes>List Item Group</a>:";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=inventory-group-update.jsp?add=yes>Add Item Group...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
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
	public String group_id = "0";
	public String group_rank = "";
	public String Up = "";
	public String Down = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Group ID", "numeric", "group_id"},
			{"Group Name", "text", "group_name"},
			{"Group Description", "text", "group_desc"},
			{"Group Rank", "numeric", "group_rank"},
			{"Entry By", "text", "group_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "group_entry_date"},
			{"Modified By", "text", "group_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "group_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
				group_rank = PadQuotes(request.getParameter("group_rank"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND group_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Item Group!";
					StrSearch = StrSearch + " and group_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("inventory-group-list.jsp?msg=Item Group Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("inventory-group-list.jsp?msg=Item Group Down Successfully!"));
				}

				if (!(group_id.equals("0"))) {
					msg = msg + "<br>Results for Item Group ID = " + group_id + "!";
					StrSearch = StrSearch + " and group_id = " + group_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND group_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("groupstrsql", StrSearch, request);
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
			StrSql = "SELECT group_id, group_name, group_rank";

			CountSql = "SELECT Count(distinct group_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_group"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by group_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Item Group Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("group_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"inventory-group-list.jsp?Up=yes&group_id=").append(crs.getString("group_id"))
								.append(" \">Up</a> - <a href=\"inventory-group-list.jsp?Down=yes&group_id=").append(crs.getString("group_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top align=left ><a href=\"inventory-group-update.jsp?update=yes&group_id=").append(crs.getString("group_id")).append(" \">Update Item Group</a></td>\n");
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
				RecCountDisplay = "<br><br><br><br><font color=red>No Item Group(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int group_rank;
		try {
			group_rank = Integer.parseInt(ExecuteQuery("SELECT group_rank from " + compdb(comp_id) + "axela_inventory_group WHERE group_id = " + group_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(group_rank) AS min1 from " + compdb(comp_id) + "axela_inventory_group"));
			if (group_rank != tempRank) {
				tempRank = group_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_group"
						+ " SET"
						+ " group_rank = " + group_rank + ""
						+ " WHERE group_rank = " + tempRank + "";
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_group"
						+ " SET"
						+ " group_rank = " + tempRank + ""
						+ " WHERE group_rank = " + group_rank + ""
						+ " AND group_id = " + group_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int group_rank;
		try {
			group_rank = Integer.parseInt(ExecuteQuery("SELECT group_rank FROM " + compdb(comp_id) + "axela_inventory_group WHERE group_id = " + group_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(group_rank) AS max1 FROM " + compdb(comp_id) + "axela_inventory_group"));
			if (group_rank != tempRank) {
				tempRank = group_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_group"
						+ " SET"
						+ " group_rank = " + group_rank + ""
						+ " WHERE group_rank = " + tempRank + "";
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_group"
						+ " SET"
						+ " group_rank = " + tempRank + ""
						+ " WHERE group_rank = " + group_rank + ""
						+ " AND group_id = " + group_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
