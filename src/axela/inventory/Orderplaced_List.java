package axela.inventory;
/* saiman 26th june 2012 */
//aJIt 9th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Orderplaced_List extends Connect {
	// ////// List page links
	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../inventory/index.jsp>Inventory</a>"
			+ " &gt; <a href=../inventory/orderplaced-list.jsp?all=yes>List Orderplaced</a>:";
	public String LinkExportPage = "warehouse.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../inventory/orderplaced-update.jsp?add=yes>Add New Orderplaced..</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";

	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String orderplaced_id = "0";
	public String cat_id = "0";
	public String salescat_id = "0";
	public String item_img = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Orderplaced ID", "numeric", "orderplaced_id"},
			{"Branch", "text", "orderplaced_branch_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Model", "text", "model_name"},
			{"Fuel Type", "text", "fueltype_name"},
			{"Entry By", "text", "orderplaced_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "orderplaced_entry_date"},
			{"Modified By", "text", "orderplaced_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "orderplaced_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_item_access, emp_sales_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				orderplaced_id = CNumeric(PadQuotes(request.getParameter("orderplaced_id")));

				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					if (!orderplaced_id.equals("0")) {
						StrSearch = " AND orderplaced_id = "
								+ orderplaced_id + "";
					} else {
						StrSearch = " AND orderplaced_id = 0";
					}
				} else if ("yes".equals(all)) {
					msg = "Results for all Orderplaced!";
				} else if (!orderplaced_id.equals("0")) {
					msg += "<br>Results for Item!";
					StrSearch += " AND orderplaced_id = " + orderplaced_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND orderplaced_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("orderplacedstrsql", request).equals("")) {
						StrSearch = GetSession("orderplacedstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("orderplacedstrsql", StrSearch, request);
				}

				SOP("smart" + StrSearch);
				SOP("StrSearch--111--" + StrSearch);
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
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("")) {
					StrSql = "SELECT orderplaced_id, orderplaced_branch_id,"
							+ " orderplaced_model_id, orderplaced_fueltype_id,"
							+ " orderplaced_date, branch_id, branch_name, branch_code,"
							+ " model_name, fueltype_name";

					CountSql = " SELECT COUNT(DISTINCT(orderplaced_id)) ";

					StrJoin = " FROM " + compdb(comp_id) + "axela_sales_orderplaced "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = orderplaced_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = orderplaced_model_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = orderplaced_fueltype_id";

					StrSql += StrJoin;
					CountSql += StrJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " GROUP BY orderplaced_id"
								+ " ORDER BY orderplaced_id DESC";
						CountSql += StrSearch;
					}
					// SOP("Sql---------" + StrSql);
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Item(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "orderplaced-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

						if (all.equals("yes")) {
							StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_sales_orderplaced\\b",
									"FROM " + compdb(comp_id) + "axela_sales_orderplaced"
											+ " INNER JOIN (SELECT orderplaced_id FROM " + compdb(comp_id) + "axela_sales_orderplaced"
											+ " WHERE 1 = 1"
											+ " GROUP BY orderplaced_id"
											+ " ORDER BY orderplaced_id DESC"
											+ ") AS myresults USING (orderplaced_id)");
						}

						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql, 0);

						int count = StartRec - 1;
						Str.append("<div class=\"  table\">\n");
						Str.append("<table class=\"table table-bordered  table-hover\" data-filter=\"#filter\">\n");
						Str.append("<div class=\"table-responsive \">\n");
						// Str.append("\n<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th>Branch</th>\n");
						Str.append("<th data-hide=\"phone\">Model Name</th>");
						Str.append("<th data-hide=\"phone\">Fuel Type</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");

						while (crs.next()) {
							count++;
							Str.append("<tr>\n<td align=\"center\">").append(count);
							Str.append("</td>\n");
							Str.append("<td >");
							Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
							Str.append(crs.getString("branch_name") + " (" + crs.getString("branch_code") + ")").append("</a></td\n>");
							Str.append("<td>");
							Str.append(crs.getString("model_name"));
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("fueltype_name"));
							Str.append("&nbsp;</td>\n");
							Str.append("<td nowrap>");
							Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
									+ "<button type=button style='margin: 0' class='btn btn-success'>"
									+ "<i class='fa fa-pencil'></i></button>"
									+ "<ul class='dropdown-content dropdown-menu pull-right'>"
									+ "<li role=presentation><a href=\"orderplaced-update.jsp?update=yes&orderplaced_id="
									+ crs.getString("orderplaced_id") + " \">Update Orderplaced</a></li></ul></div></center></div>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>");
						Str.append("</div>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");

						crs.close();
					} else {
						RecCountDisplay = "<br><br><br><br><font color=\"red\">No Orderplaced(s) found!</font><br><br>";
					}
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
