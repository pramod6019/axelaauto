package axela.inventory;

/*saiman 9th july 2012*/

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Price_List extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String LinkListPage = "item-price-list.jsp";
	public String LinkExportPage = "item-price-list.jsp?smart=yes&target="
			+ Math.random() + "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String comp_id = "0";

	public String emp_id = "0";
	public String inst_id = "";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String Vat = "";
	public String price_id = "", price_item_id = "", item_id = "",
			branch_id = "", rateclass_id = "0";
	public String price_disc_type = "";
	public String item_name = "";
	public String item_code = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
				branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				price_item_id = CNumeric(PadQuotes(request
						.getParameter("price_item_id")));
				price_id = CNumeric(PadQuotes(request.getParameter("price_id")));
				if (!item_id.equals("0")) {
					StrSql = "select item_name, item_code,"
							+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
							+ " from  " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_id = " + item_id
							+ " order by item_name ";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						item_name = crs.getString("item_name");
						item_code = crs.getString("item_code");
					}
					crs.close();
				} else {
					response.sendRedirect("../inventory/inventory-item-list.jsp?all=yes");
				}

				LinkAddPage = "<a href=item-price-update.jsp?add=yes&item_id="
						+ item_id + ">Add New Item Price...</a>";

				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=index.jsp>Inventory</a> &gt; <a href=inventory-item-list.jsp?all=yes>List Items</a> &gt; <a href=inventory-item-list.jsp?item_id="
						+ item_id
						+ ">"
						+ item_name
						+ "</a> > <a href=../inventory/item-price-list.jsp?item_id="
						+ item_id
						+ "&price_id="
						+ price_id
						+ ">List Item Prices</a>:";
				all = PadQuotes(request.getParameter("all"));

				if (msg.toLowerCase().contains("delete")) {
					if (!item_id.equals("0")) {
						StrSearch += " AND price_item_id = " + item_id;
					} else {
						StrSearch += " AND price_item_id = 0";
					}
				} else if ("yes".equals(all)) {
					msg = "Results for all Item Price!";
					StrSearch = StrSearch + " and price_item_id > 0";
				} else if (!(price_item_id.equals("0"))) {
					msg = msg + "<br>Results for item Price ID = " + item_id + "!";
					StrSearch = StrSearch + " and price_item_id =" + item_id + "";
				} else if (!(item_id.equals("0"))) {
					msg = msg + "<br>Results for Item ID = " + item_id + "!";
					StrSearch = StrSearch + " and price_item_id =" + item_id + "";
				} else if (!(price_id.equals("0"))) {
					msg = msg + "<br>Results for Price Code =" + price_id + "";
					StrSearch = StrSearch + " and price_id =" + price_id + "";
				}
				if (!(rateclass_id.equals("0"))) {
					StrSearch = StrSearch + " and rateclass_id =" + rateclass_id + "";
				}
				if (!StrSearch.equals("")) {
					SetSession("pricestrsql", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("PricesePrintSearchStr", StrSearch, request);
					SetSession("PriceseFilterStr", StrSearch, request);
				} else {
					msg = "Results for all Item Price!";
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {

		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String StrJoin = "";

		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search
			StrSql = "Select price_id, price_item_id, item_code,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " rateclass_name, rateclass_type, price_disc, price_amt, price_active, price_disc_type, price_effective_from";

			StrJoin = " FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item on item_id = price_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_rate_class on rateclass_id = price_rateclass_id	"
					+ " WHERE 1 = 1"
					+ " AND item_id = " + item_id;

			CountSql = " SELECT Count(distinct price_id)";

			StrSql += StrJoin;
			CountSql += StrJoin;

			if (!StrSearch.equals("")) {
				StrSql += StrSearch + " GROUP BY price_id"
						+ " ORDER BY price_rateclass_id	, price_effective_from";
				CountSql += StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {

				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
						+ " of " + TotalRecords + " Item Price(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "item-price-list.jsp?" + QueryString + "&PageCurrent=";

				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}

				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				if (all.equals("yes")) {
					StrSql = StrSql
							.replaceAll(
									"\\bFROM  " + compdb(comp_id) + "axela_inventory_item_price\\b",
									"FROM  axela_inventory_item_price"
											+ " INNER JOIN (SELECT price_id " + compdb(comp_id) + "FROM  axela_inventory_item_price"
											+ " GROUP BY price_id"
											+ " ORDER BY price_id DESC"
											+ " LIMIT "
											+ (StartRec - 1)
											+ ", "
											+ recperpage
											+ ") AS myresults USING (price_id)");

					StrSql = "SELECT * " + compdb(comp_id) + "FROM (" + StrSql + ") AS datatable"
							+ " ORDER BY price_id DESC";
				} else {
					StrSql += " LIMIT " + (StartRec - 1) + ", "
							+ recperpage + "";
				}
				try {
					// SOP("StrSql---------price-list------" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					StrHTML = "";
					int count = StartRec - 1;
					StrHTML = StrHTML + (" <div class=\"table-responsive table-bordered\">\n");
					StrHTML = StrHTML
							+ "\n<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n";
					StrHTML = StrHTML + "<thead><tr>\n";
					StrHTML = StrHTML + "<th data-hide=\"phone, tablet\">#</th>\n";
					StrHTML = StrHTML
							+ "<th data-toggle=\"true\">Item</th>\n";
					StrHTML = StrHTML + "<th>Type</th>\n";
					StrHTML = StrHTML + "<th data-hide=\"phone\">Class</th>\n";
					StrHTML = StrHTML + "<th data-hide=\"phone\">Effective</th>\n";
					StrHTML = StrHTML + "<th data-hide=\"phone, tablet\">Amount</th>\n";
					StrHTML = StrHTML
							+ "<th data-hide=\"phone, tablet\">Actions</th>\n";
					StrHTML = StrHTML + "</tr>\n</thead>\n";
					StrHTML = StrHTML + "</tr>\n<tbody>\n";
					while (crs.next()) {
						String amt = "", st = "0", cst = "0", vat = "0";
						count = count + 1;
						price_disc_type = crs.getString("price_disc_type");
						StrHTML = StrHTML + "<tr>\n";
						StrHTML = StrHTML + "<td>"
								+ count + "</td>\n";
						StrHTML = StrHTML
								+ "<td valign=top align=left ><a href=../inventory/inventory-item-list.jsp?item_id="
								+ item_id + ">" + crs.getString("item_name")
								+ "</a>";
						if (crs.getString("price_active").equals("0")) {
							StrHTML = StrHTML
									+ " &nbsp;<b><font color=red>[Inactive]</font></b>";
						}
						if (crs.getString("rateclass_type").equals("1"))
						{

							StrHTML = StrHTML + "</td>\n";
							StrHTML = StrHTML + "<td>"
									+ "Sales";
						}
						else if (crs.getString("rateclass_type").equals("2"))
						{
							StrHTML = StrHTML + "</td>\n";
							StrHTML = StrHTML + "<td>"
									+ "Purchase";
						}
						StrHTML = StrHTML + "</td>\n";
						StrHTML = StrHTML + "<td>"
								+ crs.getString("rateclass_name");
						StrHTML = StrHTML + "</td>\n";
						StrHTML = StrHTML + "<td>"
								+ strToLongDate(crs.getString("price_effective_from"));
						StrHTML = StrHTML + "</td>\n";
						StrHTML = StrHTML
								+ "<td nowrap>Amount: "
								+ (IndDecimalFormat(df.format(crs.getDouble("price_amt")))) + " " + "<br>";
						if (price_disc_type.equals("1")) {
							StrHTML = StrHTML + "Discount: "
									+ (IndDecimalFormat(df.format(crs.getDouble("price_disc")))) + "";
						} else {
							StrHTML = StrHTML + "Discount: "
									+ (IndDecimalFormat(df.format(crs.getDouble("price_disc")))) + "%";
						}
						if (price_disc_type.equals("1")) {
							amt = Double.toString(crs.getDouble("price_amt")
									- crs.getDouble("price_disc"));
							amt = df.format(Double.parseDouble(amt)
									+ Double.parseDouble(st)
									+ Double.parseDouble(cst)
									+ Double.parseDouble(vat));
						}
						if (price_disc_type.equals("2")) {
							amt = Double
									.toString(crs.getDouble("price_amt")
											- (crs.getDouble("price_amt")
													* crs.getDouble("price_disc") / 100));
							amt = df.format(Double.parseDouble(amt)
									+ Double.parseDouble(st)
									+ Double.parseDouble(cst)
									+ Double.parseDouble(vat));
						}
						StrHTML = StrHTML + "<br><b>Total: "
								+ (IndDecimalFormat(amt)) + " ";
						StrHTML = StrHTML
								+ "</td><td nowrap><a href=item-price-update.jsp?Update=yes&item_id="
								+ item_id + "&price_id="
								+ crs.getString("price_id")
								+ ">Update Price </a>";
						StrHTML = StrHTML + "</td></tr>\n";
					}
					StrHTML = StrHTML + "</tbody>\n";
					StrHTML = StrHTML + "</table>\n";
					StrHTML = StrHTML + "</div>\n";

					crs.close();
				} catch (Exception ex) {
					System.out
							.println("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
					return "";
				}
			} else {
				StrHTML = "<br><br><br><br><font color=red><b>No Item Price(s) found!</b></font><br><br>";
			}
		}
		return StrHTML;
	}
}
