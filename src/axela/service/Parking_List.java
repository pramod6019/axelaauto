package axela.service;
//aJIt 28th Oct 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Parking_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=appt.jsp>Booking</a>"
			+ " &gt; <a href=parking-list.jsp?all=yes>List Parkings</a>:";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=parking-update.jsp?add=yes>Add Parking...</a>";
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
	public String parking_id = "0";
	public String parking_branch_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Parking ID", "numeric", "parking_id"},
			{"Name", "text", "parking_name"},
			{"Notes", "text", "parking_notes"},
			{"Rank", "numeric", "parking_rank"},
			{"Entry By", "text", "parking_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "parking_entry_date"},
			{"Modified By", "text", "parking_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "parking_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_service_booking_access", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				parking_id = CNumeric(PadQuotes(request.getParameter("parking_id")));
				parking_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND parking_id = 0";
				} else if ("yes".equals(all)) {
					msg += "<br>Results for All Parkings!";
					StrSearch += " AND parking_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("parking-list.jsp?msg=Parking moved Up successfully!"));
				}

				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("parking-list.jsp?msg=Parking moved Down successfully!"));
				}

				if (!parking_id.equals("0")) {
					msg += "<br>Results for Parking ID = " + parking_id + "!";
					StrSearch += " AND parking_id = " + parking_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch += " AND parking_id = 0";
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
			StrSql = "SELECT parking_id, parking_name, parking_notes, parking_active, branch_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname";

			CountSql = "SELECT COUNT(DISTINCT parking_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_parking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = parking_branch_id"
					+ " WHERE 1 = 1";

			StrSql += SqlJoin + StrSearch;
			CountSql += SqlJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql += " ORDER by parking_branch_id, parking_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th  data-toggle=\"true\">Parking Details</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th data-hide=\"phone\">Order</th>\n");
					Str.append("<th width=\"20%\" data-hide=\"phone\">Actions</th>\n");
					Str.append("<th width=\"20%\" data-hide=\"phone\"></th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\">").append(crs.getString("parking_name"));
						if (crs.getString("parking_active").equals("0")) {
							Str.append("<br><font color=\"red\"><b>[Inactive]</b></font>");
						}
						Str.append("</td>\n<td valign=\"top\">").append("<a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getString("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>\n");
						Str.append("<td valign=\"top\" align=\"center\"><a href=\"parking-list.jsp?Up=yes&branch_id=");
						Str.append(crs.getString("branch_id")).append("&parking_id=").append(crs.getString("parking_id"));
						Str.append(" \">Up</a> - <a href=\"parking-list.jsp?Down=yes&branch_id=");
						Str.append(crs.getString("branch_id")).append("&parking_id=").append(crs.getString("parking_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=\"top\" nowrap><a href=\"parking-update.jsp?update=yes&parking_id=");
						Str.append(crs.getString("parking_id")).append("\">Update Parking</a></td>\n");
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
				RecCountDisplay = "<br><br><font color=\"red\"><b>No Parking Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int parking_rank;
		parking_rank = Integer.parseInt(ExecuteQuery("SELECT parking_rank FROM " + compdb(comp_id) + "axela_service_parking"
				+ " WHERE parking_id = " + parking_id + ""
				+ " AND parking_branch_id = " + parking_branch_id));

		tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(parking_rank) AS min1"
				+ " FROM " + compdb(comp_id) + "axela_service_parking"
				+ " WHERE parking_branch_id = " + parking_branch_id));

		if (parking_rank != tempRank) {
			tempRank = parking_rank - 1;

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_parking"
					+ " SET"
					+ " parking_rank = " + parking_rank + ""
					+ " WHERE parking_rank = " + tempRank + ""
					+ " AND parking_branch_id = " + parking_branch_id + "";
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_parking"
					+ " SET"
					+ " parking_rank = " + tempRank + ""
					+ " WHERE parking_rank = " + parking_rank + ""
					+ " AND parking_id = " + parking_id + "";
			updateQuery(StrSql);
		}
	}

	public void movedown() {
		int tempRank;
		int parking_rank;
		parking_rank = Integer.parseInt(ExecuteQuery("SELECT parking_rank FROM " + compdb(comp_id) + "axela_service_parking"
				+ " WHERE parking_id = " + parking_id + ""
				+ " AND parking_branch_id = " + parking_branch_id));

		tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(parking_rank) AS max1 FROM " + compdb(comp_id) + "axela_service_parking"
				+ " WHERE parking_branch_id = " + parking_branch_id));

		if (parking_rank != tempRank) {
			tempRank = parking_rank + 1;

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_parking"
					+ " SET"
					+ " parking_rank = " + parking_rank + ""
					+ " WHERE parking_rank = " + tempRank + ""
					+ " AND parking_branch_id = " + parking_branch_id + "";
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_parking"
					+ " SET"
					+ " parking_rank = " + tempRank + ""
					+ " WHERE parking_rank = " + parking_rank + ""
					+ " AND parking_id = " + parking_id + "";
			updateQuery(StrSql);
		}
	}
}
