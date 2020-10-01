//Smitha Nag 18 feb 2013
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class TestDriveOutage_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../sales/index.jsp>Sales</a> &gt; <a href=testdrive.jsp>Test Drives</a> &gt; <a href=testdriveoutage-list.jsp?all=yes>List Test Drive Outage</a><b>:</b>";
	public String LinkListPage = "testdriveoutage-list.jsp";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=testdriveoutage-update.jsp?add=yes>Add Test Drive Outage...</a>";
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
			{"Entry By", "text", "outage_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "outage_entry_date"},
			{"Modified By", "text", "outage_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "outage_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				outage_id = CNumeric(PadQuotes(request.getParameter("outage_id")));
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND outage_id = 0";
				} else if ("yes".equals(all)) // for all students to b displayed
				{
					msg = msg + "<br>Results for all Test Drive Outage!";
					StrSearch = StrSearch + " and outage_id > 0";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if (!outage_id.equals("0")) {
					msg = msg + "<br>Results for Test Drive Outage ID = " + outage_id + "!";
					StrSearch = StrSearch + " and outage_id = " + outage_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					StrSearch = StrSearch + GetSession("outagestrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("outagestrsql", StrSearch, request);
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
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			if (!msg.equals("")) {
				StrSql = "Select outage_id, veh_name, branch_code, outage_veh_id, branch_id,"
						+ " CONCAT(branch_name,' (',branch_code,')') as branchname, outage_desc,"
						+ " coalesce(model_name, '') as  model_name,"
						+ " outage_fromtime, outage_totime, outage_desc, outage_desc";
				CountSql = " SELECT Count(distinct outage_id)";
				SqlJoin = " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
						+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on veh_id = outage_veh_id"
						+ " left join " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id"
						+ " inner Join " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id"
						+ " where 1=1 " + BranchAccess;
				StrSql = StrSql + SqlJoin;
				// SOP("....StrSql....."+StrSql);
				CountSql = CountSql + SqlJoin;
				if (!(StrSearch.equals(""))) {
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
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Test Drive Outage(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "testdriveoutage-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql + " GROUP BY outage_id ORDER BY outage_id desc";
					} else {
						StrSql = StrSql + " GROUP BY outage_id ORDER BY outage_id";
					}
					StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					try {
						int count = StartRec - 1;
						Str.append("<div class=\"table-responsive table-bordered\">\n");
						Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th >Test Drive Outage Details</th>\n");
						Str.append("<th data-hide=\"phone\">Vehicle</th>\n");
						if (branch_id.equals("0")) {
							Str.append("<th data-hide=\"phone\">Branch</th>\n");
						}
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td align='center'>").append(count).append("</td>\n");
							Str.append("<td><b>Vehicle Outage ID: ").append(crs.getString("outage_id")).append("</b>");
							// if (crs.getString("outage_fromtime").equals(""));
							if (!crs.getString("outage_fromtime").equals("")) {
								Str.append("<br>From: ").append(strToLongDate(crs.getString("outage_fromtime"))).append("");
							}
							if (!crs.getString("outage_totime").equals("")) {
								Str.append("<br>To: ").append(strToLongDate(crs.getString("outage_totime"))).append("");
							}
							if (!crs.getString("outage_desc").equals("")) {
								Str.append("<br>").append(crs.getString("outage_desc")).append("");
							}
							Str.append("</td>\n");
							Str.append("<td>Model: <b>").append(crs.getString("model_name")).append("</b>" + "<br>Vehicle: <b>").append(crs.getString("veh_name")).append("</b>");
							Str.append("</td>");
							if (branch_id.equals("0")) {
								Str.append("<td><a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">").append(crs.getString("branchname"))
										.append("</a></td>");
							}
							Str.append("<td valign=top><a href=\"testdriveoutage-update.jsp?update=yes&outage_id=").append(crs.getString("outage_id")).append("\">Update Test Drive Outage</a>");
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
					RecCountDisplay = "<br><br><br><br><font color=red><b>No Test Drive Outage(s) Found!</b></font><br><br>";
				}
			}
		}
		return Str.toString();
	}
}
