// Ved Prakash (12 Feb 2013)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTestDriveVehicle extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../sales/index.jsp>Sales</a> &gt; <a href=testdrive.jsp>Test Drives</a> &gt; <a href=managetestdrivevehicle.jsp?all=yes>List Vehicles</a><b>:</b>";
	public String LinkExportPage = "vehicle-testdrive-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=managetestdrivevehicle-update.jsp?add=yes>Add New Vehicle...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String testdriveveh_id = "0";
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
	public String BranchAccess = "";
	public String RecCountDisplay = "";
	public String smart = "";
	public String PageCurrents = "";
	public String QueryString = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Branch Name", "text", "branch_name"},
			{"Brand", "text", "branch_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Reg. No.", "text", "testdriveveh_regno"},
			{"Model", "text", "model_name"},
			{"Vehicle Name", "text", "testdriveveh_name"},
			{"Entry By", "text", "testdriveveh_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "testdriveveh_entry_date"},
			{"Modified By", "text", "testdriveveh_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "testdriveveh_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND testdriveveh_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Vehicles!";
					StrSearch = StrSearch + " and testdriveveh_id > 0";
				} else if (!(testdriveveh_id.equals("0"))) {
					msg = msg + "<br>Results for Vehicle ID = " + testdriveveh_id + "!";
					StrSearch = StrSearch + " and testdriveveh_id = " + testdriveveh_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("testdrivevehilesql", StrSearch, request);
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
			StrSql = "SELECT testdriveveh_id, testdriveveh_name, testdriveveh_regno, testdriveveh_service_start_date,"
					+ " testdriveveh_service_end_date, testdriveveh_active, branch_id,"
					+ " CONCAT(branch_name, ' (',branch_code,')') AS branchname,"
					+ " COALESCE(item_id, '0') AS item_id,"
					+ " COALESCE(CONCAT(item_name, '(',(CASE WHEN item_code='' THEN item_id ELSE item_code END),')'), '') AS item_name,"
					+ " COALESCE(model_name, '') AS model_name";

			CountSql = "SELECT COUNT(distinct testdriveveh_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON  model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON  branch_id = testdriveveh_branch_id"
					+ " WHERE 1 = 1";

			SqlJoin += BranchAccess.replace("branch_id", "testdriveveh_branch_id");

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

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Vehicle Type(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managetestdrivevehicle.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY testdriveveh_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				// SOP("StrSql==" + StrSql);
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >Vehicle Details</th>\n");
					Str.append("<th data-hide=\"phone\">Service Start Date</th>\n");
					Str.append("<th data-hide=\"phone\">Service End Date</th>\n");
					Str.append("<th data-hide=\"phone\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("testdriveveh_name"));
						if (crs.getString("testdriveveh_active").equals("0")) {
							Str.append("<font color=red><b> [Inactive]</b></font>");
						}
						Str.append("<br>Reg No: ").append(crs.getString("testdriveveh_regno"));
						Str.append("<br>Model: ").append(crs.getString("model_name"));
						Str.append("<br>Item: <a href=\"../inventory/inventory-item-list.jsp?item_id=");
						Str.append(crs.getString("item_id")).append("\">").append(crs.getString("item_name"));
						Str.append("</a></td>\n");
						Str.append("<td>");
						Str.append(strToShortDate(crs.getString("testdriveveh_service_start_date"))).append("</td>\n");
						Str.append("<td valign=top align=center>");
						Str.append(strToShortDate(crs.getString("testdriveveh_service_end_date"))).append("</td>\n");
						Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branchname")).append("</a></td>");
						Str.append("<td>");
						Str.append("<a href=\"managetestdrivevehicle-update.jsp?update=yes&testdriveveh_id=");
						Str.append(crs.getString("testdriveveh_id")).append("\">Update Vehicle</a><br>");
						Str.append("<a href=\"testdrive-gatepass-update.jsp?add=yes&testdriveveh_id=");
						Str.append(crs.getString("testdriveveh_id")).append("\">Add Gate Pass</a><br>");
						Str.append("<a href=\"testdrive-gatepass-list.jsp?testdriveveh_id=");
						Str.append(crs.getString("testdriveveh_id")).append("\">List Gate Pass</a>");
						Str.append("</td>\n");
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
				Str.append("<br><br><br><br><font color=red>No Vehicle Type(s) Found!</font><br><br>");
			}
		}
		return Str.toString();
	}
}
