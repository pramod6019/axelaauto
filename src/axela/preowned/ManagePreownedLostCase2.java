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

public class ManagePreownedLostCase2 extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managepreownedlostcase2.jsp?all=yes>List Pre-Owned Lost Case 2</a><b>:</b>";
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
	public String preownedlostcase2_id = "0";
	public String preownedlostcase2_lostcase1_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"ID", "numeric", "preownedlostcase2_id"},
			{"Name", "text", "preownedlostcase2_name"},
			// {"Head ID", "numeric", "evalhead_id"},
			{"Pre Owned Lost Case1 Name", "text", "preownedlostcase1_name"},
			{"Entry By", "text", "preownedlostcase2_name IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "preownedlostcase2_entry_date"},
			{"Modified By", "text", "preownedlostcase2_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "preownedlostcase2_modified_date"}
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
				preownedlostcase2_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase2_id")));
				preownedlostcase2_lostcase1_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase1_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!preownedlostcase2_lostcase1_id.equals("0")) {
					LinkAddPage = "<a href=managepreownedlostcase2-update.jsp?add=yes&preownedlostcase1_id=" + preownedlostcase2_lostcase1_id + ">Add Pre-Owned Lost Case 2...</a>";
				} else {
					LinkAddPage = "<a href=managepreownedlostcase2-update.jsp?add=yes>Add Pre-Owned Lost Case 2...</a>";
				}
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND preownedlostcase2_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Pre-Owned Lost Case 2!";
					StrSearch = StrSearch + " AND preownedlostcase2_id > 0";
				}

				if (!preownedlostcase2_lostcase1_id.equals("0")) {
					msg = msg + "<br>Results for Pre Owned Lost Case 1 ID = " + preownedlostcase2_lostcase1_id + "!";
					StrSearch = StrSearch + " AND preownedlostcase2_lostcase1_id = " + preownedlostcase2_lostcase1_id + "";
				} else if (!(preownedlostcase2_id.equals("0"))) {
					msg = msg + "<br>Results for Pre Owned Lost Case 2 ID = " + preownedlostcase2_id + "!";
					StrSearch = StrSearch + " AND preownedlostcase2_id = " + preownedlostcase2_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND preownedlostcase2_id = 0";
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
			StrSql = "SELECT preownedlostcase1_id, preownedlostcase2_id, preownedlostcase2_name, preownedlostcase1_name,"
					+ " COALESCE(COUNT(DISTINCT(preownedlostcase3_id)),0) AS preownedlostcase3count";

			CountSql = "SELECT COUNT(DISTINCT preownedlostcase2_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_lostcase1 ON preownedlostcase1_id = preownedlostcase2_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_lostcase3 ON preownedlostcase3_lostcase2_id = preownedlostcase2_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY preownedlostcase2_id";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\"  width=5%>#</th>\n");
					Str.append("<th>Pre-Owned Lost Case 1</th>\n");
					Str.append("<th data-hide=\"phone\" >Pre-Owned Lost Case 2</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top> <a href=\"managepreownedlostcase1.jsp?preownedlostcase1_id=").append(crs.getString("preownedlostcase1_id")).append("\">")
								.append(crs.getString("preownedlostcase1_name")).append("</a></td>\n");
						Str.append("<td valign=top>").append(crs.getString("preownedlostcase2_name")).append("</td>\n");
						Str.append("<td valign=top> <a href=\"managepreownedlostcase2-update.jsp?update=yes&preownedlostcase2_id=").append(crs.getString("preownedlostcase2_id"))
								.append(" \">Update Pre-Owned Lost Case 2</a>\n");
						Str.append("<br><a href=\"managepreownedlostcase3.jsp?preownedlostcase2_id=").append(crs.getString("preownedlostcase2_id")).append(" \">List Pre-Owned Lost Case 3</a>" + " (")
								.append(crs.getString("preownedlostcase3count")).append(")");
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
				Str.append("<br><br><br><br><font color=red><b>No Pre Owned Lost Case2 Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int preownedlostcase2_rank;
		try {
			preownedlostcase2_rank = Integer.parseInt(ExecuteQuery("SELECT preownedlostcase2_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE preownedlostcase2_id = "
					+ preownedlostcase2_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MIN(preownedlostcase2_rank) AS min1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (preownedlostcase2_rank != tempRank) {
				tempRank = preownedlostcase2_rank - 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET preownedlostcase2_rank = " + preownedlostcase2_rank + " WHERE preownedlostcase2_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead SET preownedlostcase2_rank = " + tempRank + " WHERE preownedlostcase2_rank = " + preownedlostcase2_rank
						+ " AND preownedlostcase2_id = " + preownedlostcase2_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int preownedlostcase2_rank;
		try {
			preownedlostcase2_rank = Integer.parseInt(ExecuteQuery("SELECT preownedlostcase2_rank FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE preownedlostcase2_id = "
					+ preownedlostcase2_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("SELECT MAX(preownedlostcase2_rank) AS max1 FROM " + compdb(comp_id) + "axela_preowned_eval_subhead WHERE 1 = 1"));
			if (preownedlostcase2_rank != tempRank) {
				tempRank = preownedlostcase2_rank + 1;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " preownedlostcase2_rank = " + preownedlostcase2_rank + " WHERE preownedlostcase2_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
						+ " SET"
						+ " preownedlostcase2_rank=" + tempRank + " WHERE preownedlostcase2_rank = " + preownedlostcase2_rank + " AND preownedlostcase2_id = " + preownedlostcase2_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
