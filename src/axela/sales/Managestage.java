package axela.sales;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managestage extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managestage.jsp?all=yes>List Stage</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managestage-update.jsp?add=yes>Add Stage...</a>";
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
	public String stage_id = "0";
	public String stage_rank = "";
	public String Up = "";
	public String Down = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Stage ID", "numeric", "stage_id"},
			{"Stage Name", "text", "stage_name"},
			{"Stage Probability", "numeric", "stage_probability"},
			{"Stage Rank", "numeric", "stage_rank"},
			{"Entry By", "text", "stage_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "stage_entry_date"},
			{"Modified By", "text", "stage_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "stage_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
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
				stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
				stage_rank = PadQuotes(request.getParameter("stage_rank"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND stage_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Stages!";
					StrSearch = StrSearch + " and stage_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managestage.jsp?msg=Stage Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managestage.jsp?msg=Stage Moved Down Successfully!"));
				}

				if (!(stage_id.equals("0"))) {
					msg = msg + "<br>Results for Stage ID = " + stage_id + "!";
					StrSearch = StrSearch + " and stage_id = " + stage_id + "";
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
					SetSession("stagestrsql", StrSearch, request);
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
			StrSql = "Select stage_id, stage_name, stage_rank, stage_probability";

			CountSql = "SELECT Count(distinct stage_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_sales_enquiry_stage"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by stage_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Stage Details</th>\n");
					Str.append("<th>Probability%</th>\n");
					Str.append("<th data-hide=\"phone\">Order</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("stage_name")).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("stage_probability")).append("</td>\n");
						Str.append("<td valign=top align=center><a href=\"managestage.jsp?Up=yes&stage_id=").append(crs.getString("stage_id"))
								.append(" \">Up</a> - <a href=\"managestage.jsp?Down=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top><a href=\"managestage-update.jsp?update=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Update Stage</a></td>\n");
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
				RecCountDisplay = "<br><br><br><br><font color=red>No Stage(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int stage_rank;
		try {
			stage_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where  stage_id=" + stage_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select min(stage_rank) as min1 from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1"));
			if (stage_rank != tempRank) {
				tempRank = stage_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank = " + stage_rank + " where stage_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank = " + tempRank + " where stage_rank = " + stage_rank + " and stage_id = " + stage_id + "";
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
			stage_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where stage_id = " + stage_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select max(stage_rank) as max1 from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1"));
			if (stage_rank != tempRank) {
				tempRank = stage_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank = " + stage_rank + " where stage_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank = " + tempRank + " where stage_rank = " + stage_rank + " and stage_id = " + stage_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
