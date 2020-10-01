package axela.preowned;
//@Dilip Kumar P 06 MAR 2014

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Preowned_Stock_Gatepass_List extends Connect {

	public String LinkHeader = "";
	public String LinkExportPage = "stock-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String PageURL = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public String preownedstock_id = "0";
	public String preownedstockgatepass_id = "0";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{ "Keyword", "text", "keyword_arr" },
			{ "Stock Gatepass ID", "numeric", "preownedstockgatepass_id" },
			{ "Stock ID", "numeric", "preownedstock_id" },
			{ "Driver Name", "text", "driver_name" },
			{ "Branch Name", "text", "branch_name" },
			{ "Notes", "text", "stockgatepass_notes" },
			{ "Entry By", "text", "stockgatepass_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name" },
			{ "Entry Date", "date", "stockgatepass_entry_date" },
			{ "Modified By", "text", "stockgatepass_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name" },
			{ "Modified Date", "date", "stockgatepass_modified_date" }
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				if (preownedstock_id.equals("0")) {
					preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_stock_id")));
				}
				preownedstockgatepass_id = CNumeric(PadQuotes(request.getParameter("preownedstockgatepass_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				LinkHeader = "<a href=../portal/home.jsp>Home</a>"
						+ " &gt; <a href=../preowned/index.jsp>Pre Owned</a>"
						+ " &gt; <a href=../preowned/preowned-stock.jsp>Stock</a>"
						+ " &gt; <a href=preowned-stock-list.jsp?preownedstock_id=" + preownedstock_id + ">List Stocks</a>"
						+ " &gt; <a href=preowned-stock-gatepass-list.jsp?preownedstock_id=" + preownedstock_id + ">List Stock Gate Passes</a>:";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND preownedstockgatepass_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Stock!";
					StrSearch += " AND preownedstockgatepass_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Stock!";
					StrSearch += " AND preownedstockgatepass_id > 0";
				} else if (!preownedstockgatepass_id.equals("0")) {
					msg += "<br>Results for Stock Gate Pass ID = " + preownedstockgatepass_id + "!";
					StrSearch += " AND preownedstockgatepass_id = " + preownedstockgatepass_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND preownedstockgatepass_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("stockstrsql", request).equals("")) {
						StrSearch += GetSession("stockstrsql", request);
					}
				}
				if (!preownedstock_id.equals("0")) {
					msg += "<br>Results for Stock ID = " + preownedstock_id + "!";
					StrSearch += " AND preownedstockgatepass_preownedstock_id = " + preownedstock_id + "";
				}

				if (!StrSearch.equals("")) {
					SetSession("stockstrsql", StrSearch, request);
				}
				StrHTML = "<input type=\"hidden\" name=\"txt_stock_id\" id=\"txt_stock_id\" value=\"" + preownedstock_id + "\"/>"
						+ Listdata();
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
		CachedRowSet crs = null;
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}

				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT preownedstockgatepass_id, preownedstockgatepass_preownedstock_id, preownedstockgatepass_time,"
						+ " preownedstockgatepass_from_location_id, preownedstockgatepass_to_location_id,"
						+ " preownedstockgatepass_driver_id, driver_name, CONCAT(fromloc.preownedlocation_name, ' (', fromloc.preownedlocation_id, ')') AS from_location_name,"
						+ " CONCAT(toloc.preownedlocation_name, ' (', toloc.preownedlocation_id, ')') AS to_location_name, branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname";

				CountSql = "SELECT COUNT(DISTINCT preownedstockgatepass_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = preownedstockgatepass_preownedstock_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver ON driver_id = preownedstockgatepass_driver_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location fromloc ON fromloc.preownedlocation_id = preownedstockgatepass_from_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location toloc ON toloc.preownedlocation_id = preownedstockgatepass_to_location_id"
						+ " WHERE 1 = 1 ";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY preownedstockgatepass_id"
							+ " ORDER BY preownedstockgatepass_id DESC";
					CountSql += StrSearch;
				}

				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock Gate Pass(es)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "preowned-stock-gatepass-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}
					// SOP("StrSql===" + StrSqlBreaker(StrSql));
					int count = StartRec - 1;

					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th data-hide=\"phone\">ID</th>\n");
					Str.append("<th data-hide=\"phone\">Stock ID</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Time</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Location</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Driver</th>\n");
					if (branch_id.equals("0")) {
						Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					}
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n<td>").append(count);
						Str.append("</td>\n<td>").append(crs.getString("preownedstockgatepass_id"));
						Str.append("</td>\n<td><a href=\"preowned-stock-list.jsp?preownedstock_id=").append(crs.getString("preownedstockgatepass_preownedstock_id"))
								.append(" \">").append(crs.getString("preownedstockgatepass_preownedstock_id")).append("</a>");
						Str.append("</td>\n<td>").append(strToLongDate(crs.getString("preownedstockgatepass_time")));

						Str.append("</td>\n<td>From: <a href=\"managepreownedlocation.jsp?preownedlocation_id=").append(crs.getString("preownedstockgatepass_from_location_id"))
								.append(" \">");
						Str.append(crs.getString("from_location_name")).append("</a><br>");

						Str.append("To: <a href=\"managepreownedlocation.jsp?preownedlocation_id=").append(crs.getString("preownedstockgatepass_to_location_id")).append(" \">");
						Str.append(crs.getString("to_location_name")).append("</a>");

						Str.append("</td>\n<td><a href=\"../sales/managetestdrivedriver.jsp?driver_id=").append(crs.getString("preownedstockgatepass_driver_id")).append(" \">");
						Str.append(crs.getString("driver_name")).append("</a>");
						if (branch_id.equals("0")) {
							Str.append("</td>\n<td><a href=\"../portal/branch-summary.jsp?branch_id=");
							Str.append(crs.getString("branch_id"));
							Str.append("\">").append(crs.getString("branchname")).append("</a>");
						}
						Str.append("</td>\n<td><a href=\"preowned-stock-gatepass-update.jsp?update=yes&preownedstockgatepass_id=");
						Str.append(crs.getString("preownedstockgatepass_id")).append(" \">Update Stock Gate Pass</a><br>\n");
						Str.append("<a href=\"preowned-stock-gatepass-print.jsp?report=gatepass&preownedstockgatepass_id=").append(crs.getString("preownedstockgatepass_id")).append("&target=")
								.append(Math.random()).append("\" target=_blank>Print Gate Pass</a>");

						Str.append("</td>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");

					return Str.toString();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Stock Gate Pass found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
