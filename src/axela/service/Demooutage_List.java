//Smitha Nag 18 feb 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Demooutage_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../sales/index.jsp>Sales</a>"
			+ " &gt; <a href=demo.jsp>Demos</a>"
			+ " &gt; <a href=demooutage-list.jsp?all=yes>List Demo Outages</a>:";
	public String LinkListPage = "demooutage-list.jsp";
	public String LinkExportPage = "demooutage.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=demooutage-update.jsp?add=yes>Add Demo Outage...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String smart = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String PageURL = "";
	public String StrSql = "";
	public String StrSearch = "";
	public Smart SmartSearch = new Smart();
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	// public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String outage_id = "0";
	public String advSearch = "";
	public String branch_id = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Outage ID", "numeric", "outage_id"},
			{"Vehicle ID", "numeric", "outage_veh_id"},
			{"From time", "date", "outage_fromtime"},
			{"To Time", "date", "outage_totime"},
			{"Description", "text", "outage_desc"},
			{"Notes", "text", "outage_notes"},
			{"Entry By", "text", "outage_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "outage_entry_date"},
			{"Modified By", "text", "outage_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "outage_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_demo_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				outage_id = CNumeric(PadQuotes(request.getParameter("outage_id")));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = StrSearch + " AND outage_id = 0";
				} else if ("yes".equals(all)) // for all students to b displayed
				{
					msg = msg + "<br>Results for all Demo Outage!";
					StrSearch = StrSearch + " and outage_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND outage_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (!outage_id.equals("0")) {
					msg = msg + "<br>Results for Demo Outage ID = " + outage_id + "!";
					StrSearch = StrSearch + " and outage_id = " + outage_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("outagestrsql", request);
				}

				StrSearch += BranchAccess;
				SetSession("outagestrsql", StrSearch, request);
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
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			if (!msg.equals("")) {
				StrSql = "SELECT outage_id, veh_name, branch_code, outage_veh_id, branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " coalesce(model_name, '') AS  model_name, outage_desc,"
						+ " outage_fromtime, outage_totime, outage_desc, outage_desc";

				CountSql = " SELECT COUNT(DISTINCT outage_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_demo_vehicle_outage"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_demo_vehicle ON veh_id = outage_veh_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
						+ " WHERE 1 = 1";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql = StrSql + StrSearch;
					CountSql = CountSql + StrSearch;
				}

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Demo Outage(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "demooutage-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql += " GROUP BY outage_id"
								+ " ORDER BY outage_id desc";
					} else {
						StrSql += " GROUP BY outage_id"
								+ " ORDER BY outage_id";
					}
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					try {
						int count = StartRec - 1;
						Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
						Str.append("<tr align=\"center\">\n");
						Str.append("<th>#</th>\n");
						Str.append("<th>Demo Outage Details</th>\n");
						Str.append("<th>From Time</th>\n");
						Str.append("<th>To Time</th>\n");
						Str.append("<th>Model</th>\n");
						Str.append("<th>Vehicle</th>\n");
						if (branch_id.equals("0")) {
							Str.append("<th>Branch</th>\n");
						}
						Str.append("<th width=\"20%\">Actions</th>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							count++;
							Str.append("<tr>\n");
							Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
							Str.append("<td valign=\"top\"><b>ID: ").append(crs.getString("outage_id")).append("</b>");

							if (!crs.getString("outage_desc").equals("")) {
								Str.append("<br>").append(crs.getString("outage_desc"));
							}
							if (!crs.getString("outage_fromtime").equals("")) {
								Str.append("<td valign=\"top\">").append(strToLongDate(crs.getString("outage_fromtime")));
								Str.append("</td>\n");
							}
							if (!crs.getString("outage_totime").equals("")) {
								Str.append("<td valign=\"top\">").append(strToLongDate(crs.getString("outage_totime")));
								Str.append("</td>\n");
							}
							Str.append("</td>\n<td valign=\"top\">").append(crs.getString("model_name")).append("</td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("veh_name")).append("</td>\n");
							if (branch_id.equals("0")) {
								Str.append("<td valign=\"top\"><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">");
								Str.append(crs.getString("branchname")).append("</a></td>\n");
							}
							Str.append("<td valign=\"top\">");
							Str.append("<a href=\"demooutage-update.jsp?update=yes&outage_id=").append(crs.getString("outage_id")).append("\">Update Demo Outage</a>");
							Str.append("</td>\n</tr>\n");
						}
						Str.append("</table>\n");
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					Str.append("<br><br><br><br><font color=\"red\"><b>No Demo Outage(s) Found!</b></font><br><br>");
				}
			}
		}
		return Str.toString();
	}
}
