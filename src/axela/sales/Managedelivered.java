package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managedelivered extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managedelivered.jsp?all=yes>List Delivery Status</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managedelivered-update.jsp?add=yes>Add Delivery Status...</a>";
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
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String delstatus_id = "0";
	public String delstatus_rank = "";
	public String Up = "";
	public String Down = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Delstatus ID", "numeric", "delstatus_id"},
			{"Delstatus Name", "text", "delstatus_name"},
			{"Delstatus Rank", "numeric", "delstatus_rank"},
			{"Entry By", "text", "delstatus_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "delstatus_entry_date"},
			{"Modified By", "text", "delstatus_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "delstatus_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				delstatus_id = CNumeric(PadQuotes(request.getParameter("delstatus_id")));
				delstatus_rank = PadQuotes(request.getParameter("delstatus_rank"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND delstatus_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for All Delivery Status(es)!";
					StrSearch = StrSearch + " and delstatus_id > 0";
				} else if (!(delstatus_id.equals("0"))) {
					msg = msg + "<br>Results for Delivery Status ID = " + delstatus_id + "!";
					StrSearch = StrSearch + " and delstatus_id = " + delstatus_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("delstatusstrsql", StrSearch, request);
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();

		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int

			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search

			StrSql = "Select delstatus_id, delstatus_name, delstatus_rank";

			CountSql = "SELECT Count(distinct delstatus_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Delivery Status(es)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managedelivered.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by delstatus_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Delivery Status Details</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("delstatus_name")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managedelivered.jsp?Up=yes&delstatus_id=").append(crs.getString("delstatus_id"))
								.append(" \">Up</a> - <a href=\"managedelivered.jsp?Down=yes&delstatus_id=").append(crs.getString("delstatus_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top><a href=\"managedelivered-update.jsp?update=yes&delstatus_id=").append(crs.getString("delstatus_id"))
								.append(" \">Update Delivery Status</a></td>\n");
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
				RecCountDisplay = "<br><br><br><br><font color=red>No Delivery Status Found!</font><br><br>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int delstatus_rank;
		try {
			delstatus_rank = Integer.parseInt(ExecuteQuery("SELECT delstatus_rank from " + compdb(comp_id) + "axela_sales_so_delstatus where  delstatus_id = " + delstatus_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select min(delstatus_rank) as min1 from " + compdb(comp_id) + "axela_sales_so_delstatus where 1=1"));
			if (delstatus_rank != tempRank) {
				tempRank = delstatus_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_so_delstatus set delstatus_rank = " + delstatus_rank + " where delstatus_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_so_delstatus set delstatus_rank = " + tempRank + " where delstatus_rank = " + delstatus_rank + " and delstatus_id = "
						+ delstatus_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int delstatus_rank;
		try {
			delstatus_rank = Integer.parseInt(ExecuteQuery("SELECT delstatus_rank from " + compdb(comp_id) + "axela_sales_so_delstatus where delstatus_id = " + delstatus_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select max(delstatus_rank) as max1 from " + compdb(comp_id) + "axela_sales_so_delstatus where 1=1 "));
			if (delstatus_rank != tempRank) {
				tempRank = delstatus_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_so_delstatus set delstatus_rank = " + delstatus_rank + " where delstatus_rank=" + tempRank + " ";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_so_delstatus set delstatus_rank = " + tempRank + " where delstatus_rank = " + delstatus_rank + " and delstatus_id = "
						+ delstatus_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
