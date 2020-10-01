package axela.sales;
/*saiman 27th june 2012 */

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class TestDrive_Gatepass_List extends Connect {
	// ////// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Sales</a>";

	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String BranchAccess = "";
	public String StrHTML = "", emp_id = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String quotediscount_id = "";
	private String quote_id = "0";
	public String advSearch = "";
	public String testdriveveh_id = "0", salesgatepass_id = "0";
	public Smart SmartSearch = new Smart();

	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Gate Pass ID", "numeric", "salesgatepass_id"},
			{"Test Drive Vehicle ID", "numeric", "salesgatepass_testdriveveh_id"},
			{"Gate Pass Type", "text", "salesgatepass_gatepasstype_id IN (SELECT salesgatepass_gatepasstype_id FROM compdb.axela_sales_testdrive_gatepass_type WHERE salesgatepasstype_name"},
			{"From Time", "date", "salesgatepass_fromtime"},
			{"To Time", "date", "salesgatepass_totime"},
			{"Entry By", "text", "salesgatepass_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "salesgatepass_entry_date"},
			{"Modified By", "text", "salesgatepass_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "salesgatepass_modified_date"}};

	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));
				salesgatepass_id = CNumeric(PadQuotes(request.getParameter("salesgatepass_id")));

				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!testdriveveh_id.equals("0")) {
					LinkAddPage = "<a href=testdrive-gatepass-update.jsp?add=yes&testdriveveh_id=" + testdriveveh_id + ">Add New Test Drive Gate Pass...</a>";
				}

				LinkHeader += " &gt; <a href=managetestdrivevehicle.jsp?all=yes>List Vehicles</a>"
						+ " &gt; <a href=testdrive-gatepass-list.jsp?all=yes>List Test Drive Gate Pass</a>:";

				if ("yes".equals(all)) {
					msg = "Results for all Vehicle!";
					StrSearch = StrSearch + " AND salesgatepass_id > 0 ";
				} else if (!(testdriveveh_id.equals("0"))) {
					msg = msg + "<br>Results for Vehicle ID = " + testdriveveh_id + "!";
					StrSearch = StrSearch + " AND salesgatepass_testdriveveh_id = " + testdriveveh_id;
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND salesgatepass_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("quotediscountstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("quotediscountstrsql", request);
					}
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

		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		String StrJoin = "";
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		StrSql = "SELECT"
				+ " salesgatepass_id,"
				+ " salesgatepass_gatepasstype_id,"
				+ " salesgatepasstype_name,"
				+ " salesgatepass_testdriveveh_id,"
				+ " salesgatepass_fromtime,"
				+ " salesgatepass_totime,"
				+ " salesgatepass_from_branch_id,"
				+ " COALESCE(frombranch.branch_code,'') AS from_branchcode,"
				+ " COALESCE(frombranch.branch_name,'') AS from_branch,"
				+ " COALESCE(tobranch.branch_code,'') AS to_branchcode,"
				+ " salesgatepass_to_branch_id,"
				+ " COALESCE(tobranch.branch_name,'')AS to_branch,"
				+ " salesgatepass_driver_id,"
				+ " COALESCE(testdriveveh_name,'') AS testdriveveh_name,"

				+ " COALESCE(CONCAT(item_name, '(',(case when item_code='' then item_id else item_code end),')'),'') AS item_name,"
				+ " COALESCE(model_name,'') AS model_name,"

				+ " emp_name";

		CountSql = "SELECT COUNT(DISTINCT salesgatepass_id) ";

		StrJoin = " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = salesgatepass_testdriveveh_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON  model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_gatepass_type ON salesgatepasstype_id = salesgatepass_gatepasstype_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch frombranch ON frombranch.branch_id = salesgatepass_from_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch tobranch ON tobranch.branch_id = salesgatepass_to_branch_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = salesgatepass_driver_id"
				+ " WHERE 1 = 1";
		// + " AND salesgatepass_testdriveveh_id = " + testdriveveh_id;

		StrSql = StrSql + StrJoin;
		CountSql = CountSql + StrJoin;

		if (!StrSearch.equals("")) {
			StrSql += StrSearch;
			CountSql += StrSearch;
		}

		CountSql = ExecuteQuery(CountSql);
		if (!CountSql.equals("")) {
			TotalRecords = Integer.parseInt(CountSql);
		} else {
			TotalRecords = 0;
		}

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Gate Pass";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "testdrive-gatepass-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			if (all.equals("yes")) {
				StrSql = StrSql + " GROUP BY salesgatepass_id ORDER BY salesgatepass_id DESC";
			} else {
				StrSql = StrSql + " GROUP BY salesgatepass_id";
			}
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql===list=" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				// Str.append("<th>ID</th>\n");
				Str.append("<th>Name</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th>Variant</th>\n");
				Str.append("<th>Gate Pass Type</th>\n");
				Str.append("<th>From Location</th>\n");
				Str.append("<th>To Location</th>\n");
				Str.append("<th>From Time</th>\n");
				Str.append("<th>To Time</th>\n");
				Str.append("<th>Driver</th>\n");
				Str.append("<th nowrap>Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					// Str.append("<td align='center'>").append(crs.getString("salesgatepass_id")).append("</td>\n");
					Str.append("<td align='center'>").append("<a href=\"../sales/managetestdrivevehicle.jsp?testdriveveh_id=")
							.append(crs.getInt("salesgatepass_testdriveveh_id")).append("\">")
							.append(crs.getString("testdriveveh_name")).append("</a></td>\n");
					Str.append("<td align='center'>").append("<a href=\"../sales/managetestdrivevehicle.jsp?testdriveveh_id=")
							.append(crs.getInt("salesgatepass_testdriveveh_id")).append("\">")
							.append(crs.getString("model_name")).append("</a></td>\n");
					Str.append("<td align='center'>").append("<a href=\"../sales/managetestdrivevehicle.jsp?testdriveveh_id=")
							.append(crs.getInt("salesgatepass_testdriveveh_id")).append("\">")
							.append(crs.getString("item_name")).append("</a></td>\n");
					Str.append("<td align='left'>").append(crs.getString("salesgatepasstype_name")).append("</td>\n");
					Str.append("<td align='left'>")
							.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("salesgatepass_from_branch_id")).append("\">")
							.append(crs.getString("from_branch") + " (" + crs.getString("from_branchcode") + " )").append("</a></td>\n");
					Str.append("<td align='left'>")
							.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("salesgatepass_to_branch_id")).append("\">")
							.append(crs.getString("to_branch") + " (" + crs.getString("to_branchcode") + " )").append("</a></td>\n");
					Str.append("<td align='center'>").append(strToLongDate(crs.getString("salesgatepass_fromtime"))).append("</td>\n");
					Str.append("<td align='center'>").append(strToLongDate(crs.getString("salesgatepass_totime"))).append("</td>\n");

					if (crs.getString("salesgatepass_driver_id").equals("0")) {
						Str.append("<td align='center'>").append("</td>\n");
					} else {
						Str.append("<td align='center'>").append(ExeDetailsPopover(crs.getInt("salesgatepass_driver_id"), crs.getString("emp_name"), "")).append("</td>\n");
					}

					Str.append("<td align='left' nowrap>");
					Str.append("<a href=\"testdrive-gatepass-update.jsp?update=yes&salesgatepass_id=");
					Str.append(crs.getString("salesgatepass_id")).append("&testdriveveh_id=").append(crs.getInt("salesgatepass_testdriveveh_id"))
							.append("\"  target=_blank>Update Gate Pass</a>");
					Str.append("<br><a href=\"testdrive-print-gatepass-new.jsp?salesgatepass_id=");
					Str.append(crs.getString("salesgatepass_id")).append("&testdriveveh_id=").append(crs.getInt("salesgatepass_testdriveveh_id"))
							.append("\"  target=_blank>Print Gate Pass</a>");
					Str.append("<br><a href=\"update-kms.jsp?salesgatepass_id=").append(crs.getString("salesgatepass_id"));
					Str.append("&testdriveveh_id=").append(crs.getInt("salesgatepass_testdriveveh_id"))
							.append("\"data-target='#Hintclicktocall' data-toggle='modal'>Update KMS</a>");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			Str.append("<br><br><font color=\"red\"><b>No Test Drive Gate Pass(s) found!</b></font>");
		}
		return Str.toString();
	}
}
