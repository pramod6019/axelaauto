//Shivaprasad 7July2014
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTradeInModel extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=../sales/managetradeinmodel.jsp?all=yes>List Models</a><b>:</b>";
	public String LinkExportPage = "vehicle-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../sales/managetradeinmodel-update.jsp?add=yes>Add New Model...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String tradeinmodel_id = "0";
	public String tradeinmake_id = "0";
	public String type_id = "0";
	public String StrHTML = "";
	public String all = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public int PageSize = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageURL = "";
	public String PageNaviStr = "";
	public int recperpage = 0;
	public String RecCountDisplay = "";
	public String smart = "";
	public String PageCurrents = "";
	public String QueryString = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Model Id", "numeric", "tradeinmodel_id"},
			{"Model Name", "text", "tradeinmodel_name"},
			{"Make Id", "numeric", "tradeinmake_id"},
			{"Make Name", "text", "tradeinmake_name"},
			{"Entry By", "text", "tradeinmodel_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "tradeinmodel_entry_date"},
			{"Modified By", "text", "tradeinmodel_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "tradeinmodel_modified_date"}
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
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				tradeinmodel_id = CNumeric(PadQuotes(request.getParameter("tradeinmodel_id")));
				tradeinmake_id = CNumeric(PadQuotes(request.getParameter("tradeinmake_id")));

				if (!tradeinmake_id.equals("0")) {
					msg = msg + "<br>Results for Make ID = " + tradeinmake_id + "!";
					StrSearch = StrSearch + " and tradeinmodel_tradeinmake_id = " + tradeinmake_id + "";
				} else {
					if (msg.toLowerCase().contains("delete")) {
						StrSearch = " AND tradeinmodel_id = 0";
					} else if ("yes".equals(all)) {
						msg = msg + "<br>Results for all Models!";
						StrSearch = StrSearch + " and tradeinmodel_id > 0";
					} else if (!(tradeinmodel_id.equals("0"))) {
						msg = msg + "<br>Results for Model ID = " + tradeinmodel_id + "!";
						StrSearch = StrSearch + " and tradeinmodel_id = " + tradeinmodel_id + "";
					} else if (advSearch.equals("Search")) {
						StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
						if (StrSearch.equals("")) {
							msg = "Enter Search Text!";
						} else {
							msg = "Results for Search!";
						}
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			StrSql = "Select tradeinmodel_id, tradeinmodel_name, tradeinmodel_tradeinmake_id,"
					+ " tradeinmake_name";

			CountSql = "SELECT Count(distinct tradeinmodel_id)";

			SqlJoin = " From " + compdb(comp_id) + "axela_sales_tradein_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_tradein_make on  tradeinmake_id = tradeinmodel_tradeinmake_id"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("StrSql = " + StrSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Model Type(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managetradeinmodel.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by tradeinmodel_name";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Model</th>\n");
					Str.append("<th>Make</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("tradeinmodel_name")).append("</td>\n");
						Str.append("<td valign=top><a href=../sales/managetradeinmake.jsp?tradeinmake_id=");
						Str.append(crs.getString("tradeinmodel_tradeinmake_id")).append(">");
						Str.append(crs.getString("tradeinmake_name")).append("</a></td>\n");
						Str.append("<td valign=top><a href=\"managetradeinmodel-update.jsp?update=yes&tradeinmodel_id=");
						Str.append(crs.getString("tradeinmodel_id")).append("\">Update Model</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Model(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
