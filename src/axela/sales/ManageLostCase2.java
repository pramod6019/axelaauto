package axela.sales;
//Dilip 10 Jul 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageLostCase2 extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managelostcase2.jsp?all=yes>List Lost Case 2</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
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
	public String lostcase2_id = "0";
	public String lostcase2_lostcase1_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "lostcase2_id"},
			{"Name", "text", "lostcase2_name"},
			// {"Head ID", "numeric", "evalhead_id"},
			{"Lost Case1 Name", "text", "lostcase1_name"},
			{"Entry By", "text", "lostcase2_name IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "lostcase2_entry_date"},
			{"Modified By", "text", "lostcase2_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "lostcase2_modified_date"}
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
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				lostcase2_id = CNumeric(PadQuotes(request.getParameter("lostcase2_id")));
				lostcase2_lostcase1_id = CNumeric(PadQuotes(request.getParameter("lostcase1_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!lostcase2_lostcase1_id.equals("0")) {
					LinkAddPage = "<a href=managelostcase2-update.jsp?add=yes&lostcase1_id=" + lostcase2_lostcase1_id + ">Add Lost Case 2...</a>";
				} else {
					LinkAddPage = "<a href=managelostcase2-update.jsp?add=yes>Add Lost Case 2...</a>";
				}
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND lostcase2_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Lost Case 2!";
					StrSearch = StrSearch + " AND lostcase2_id > 0";
				}

				if (!lostcase2_lostcase1_id.equals("0")) {
					msg = msg + "<br>Results for Lost Case 1 ID = " + lostcase2_lostcase1_id + "!";
					StrSearch = StrSearch + " AND lostcase2_lostcase1_id = " + lostcase2_lostcase1_id + "";
				} else if (!(lostcase2_id.equals("0"))) {
					msg = msg + "<br>Results for Lost Case 2 ID = " + lostcase2_id + "!";
					StrSearch = StrSearch + " AND lostcase2_id = " + lostcase2_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND lostcase2_id = 0";
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

			// to know no of records depending on search
			StrSql = "SELECT lostcase1_id, lostcase2_id, lostcase2_name, lostcase1_name,"
					+ " COALESCE(COUNT(DISTINCT(lostcase3_id)),0) AS lostcase3count";

			CountSql = "SELECT COUNT(DISTINCT lostcase2_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = lostcase2_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_lostcase2_id = lostcase2_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			// SOP("CountSql=======" + CountSql);
			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY lostcase2_id";
				// SOP("StrSql=======" + StrSql);
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Lost Case 1</th>\n");
					Str.append("<th>Lost Case 2</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top> <a href=\"managelostcase1.jsp?lostcase1_id=").append(crs.getString("lostcase1_id")).append("\">").append(crs.getString("lostcase1_name"))
								.append("</a></td>\n");
						Str.append("<td valign=top>").append(crs.getString("lostcase2_name")).append("</td>\n");
						Str.append("<td valign=top> <a href=\"managelostcase2-update.jsp?update=yes&lostcase2_id=").append(crs.getString("lostcase2_id")).append(" \">Update Lost Case 2</a>\n");
						Str.append("<br><a href=\"managelostcase3.jsp?lostcase2_id=").append(crs.getString("lostcase2_id")).append(" \">List Lost Case 3</a>" + " (")
								.append(crs.getString("lostcase3count")).append(")");
						Str.append("</td>\n");
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
				Str.append("<br><br><br><br><font color=red><b>No Lost Case2 Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int lostcase2_rank;
		try {
			lostcase2_rank = Integer.parseInt(ExecuteQuery("SELECT lostcase2_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE lostcase2_id = " + lostcase2_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(lostcase2_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (lostcase2_rank != tempRank) {
				tempRank = lostcase2_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET lostcase2_rank = " + lostcase2_rank + " WHERE lostcase2_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET lostcase2_rank = " + tempRank + " WHERE lostcase2_rank = " + lostcase2_rank + " AND lostcase2_id = "
						+ lostcase2_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int lostcase2_rank;
		try {
			lostcase2_rank = Integer.parseInt(ExecuteQuery("SELECT lostcase2_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE lostcase2_id = " + lostcase2_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(lostcase2_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (lostcase2_rank != tempRank) {
				tempRank = lostcase2_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " lostcase2_rank = " + lostcase2_rank + " WHERE lostcase2_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " lostcase2_rank=" + tempRank + " WHERE lostcase2_rank = " + lostcase2_rank + " AND lostcase2_id = " + lostcase2_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
