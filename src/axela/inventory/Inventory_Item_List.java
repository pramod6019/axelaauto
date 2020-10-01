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

public class Inventory_Item_List extends Connect {
	// ////// List page links
	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../inventory/index.jsp>Inventory</a>"
			+ " &gt; <a href=../inventory/inventory-item-list.jsp?all=yes>List Items</a>:";
	public String LinkExportPage = "item-export.jsp?smart=yes";
	public String LinkAddPage = "<a href=../inventory/inventory-item-update.jsp?add=yes>Add New Item..</a>";
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
	public String item_id = "0";
	public String cat_id = "0";
	public String salescat_id = "0";
	public String item_img = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Item ID", "numeric", "item_id"},
			{"Name", "text", "item_name"},
			{"Code", "text", "item_code"},
			{"Service Code", "text", "item_service_code"},
			{"HSN Code", "text", "item_hsn"},
			{"Category", "text", "cat_name"},
			{"Brand", "text", "model_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Model", "text", "model_name"},
			{"Item Type", "text", "type_name"},
			{"Fuel Type", "text", "fueltype_name"},
			{"Small Description", "text", "item_small_desc"},
			{"Big Description", "text", "item_big_desc"},
			{"Perishable", "boolean", "item_perishable"},
			{"Non Stock", "boolean", "item_nonstock"},
			{"UOM", "text", "uom_name"},
			{"EOQ", "numeric", "item_eoq"},
			{"Featured", "boolean", "item_featured"},
			{"Active", "boolean", "item_active"},
			{"Notes", "text", "item_notes"},
			{"Entry By", "text", "item_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "item_entry_date"},
			{"Modified By", "text", "item_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "item_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				// PopulateConfigDetails();

				if (!cat_id.equals("0")) {
					LinkAddPage = "<a href=../inventory/inventory-item-update.jsp?add=yes&item_cat_id=" + cat_id + ">Add New Item..</a>";
				}
				salescat_id = CNumeric(PadQuotes(request.getParameter("salescat_id")));
				if (!salescat_id.equals("0")) {
					LinkAddPage = "<a href=../inventory/inventory-item-update.jsp?add=yes&item_cat_id=" + salescat_id + ">Add New Item..</a>";
				}
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					if (!item_id.equals("0")) {
						StrSearch = " AND item_id = " + item_id + "";
					} else {
						StrSearch = " AND item_id = 0";
					}
				} else if ("yes".equals(all)) {
					msg = "Results for all Items!";
				} else if (!item_id.equals("0")) {
					msg += "<br>Results for Item!";
					StrSearch += " AND item_id = " + item_id + "";
				} else if (!salescat_id.equals("0")) {
					msg += "<br>Results for Sales Items!";
					StrSearch += " AND salescat_id = " + salescat_id + "";
				} else if (!cat_id.equals("0")) {
					msg += "<br>Results for Category!";
					StrSearch += " AND item_cat_id = " + cat_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND item_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("itemsstrsql", request).equals("")) {
						StrSearch = GetSession("itemsstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("itemsstrsql", StrSearch, request);
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
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		String update_info = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				if (!msg.equals("")) {
					StrSql = "SELECT item_id, item_name, item_img, item_type_id, item_aftertaxcal, type_name,"
							+ " COALESCE(fueltype_name, '') AS fueltype_name,";
					if (!salescat_id.equals("0")) {
						StrSql += " salescat_id AS cat_id, salescat_name AS cat_name,";
					} else {
						StrSql += " COALESCE(cat_id,0) AS cat_id, COALESCE(cat_name,'') AS cat_name,";
					}

					StrSql += " COALESCE(item_code, '') AS item_code, COALESCE(item_service_code, '') AS item_service_code, item_loyaltycard_id,"
							+ " item_small_desc, item_uom_id, item_eoq, item_active,"
							+ " COALESCE(uom_name, '') AS uom_name,"
							+ " COALESCE(item_model_id, '') AS model_id,"
							+ " COALESCE(model_name, '') AS model_name";

					StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_item";

					if (!salescat_id.equals("0")) {
						StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id = item_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_salescat ON salescat_id = trans_salescat_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id";
					} else {
						StrJoin += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id";
					}
					StrJoin += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON item_model_id = model_id"
							+ " WHERE 1 = 1";

					CountSql = "SELECT COUNT(DISTINCT(item_id))";

					StrSql += StrJoin;
					CountSql += StrJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " GROUP BY item_id"
								+ " ORDER BY item_id DESC";
						CountSql += StrSearch;
					}
					// SOP("Sql---------" + StrSql);
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Item(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "inventory-item-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

						if (all.equals("yes")) {
							StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_inventory_item\\b",
									"FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN (SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE 1 = 1"
											+ " GROUP BY item_id"
											+ " ORDER BY item_id DESC"
											// + " LIMIT " + (StartRec - 1) + ", " + recperpage

											+ ") AS myresults USING (item_id)");
							StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
									+ " ORDER BY item_id DESC";
						}
						// else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						// }
						SOP("StrSql==" + StrSql);
						crs = processQuery(StrSql, 0);

						int count = StartRec - 1;
						String active = "";
						Str.append("\n<table class=\"table table-responsive table-hover table-bordered\" data-filter=\"#filter\">");
						Str.append("<thead><tr>\n");
						Str.append("<tr align=\"center\">\n");
						Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
						Str.append("<th data-toggle=\"true\">ID</th>\n");
						Str.append("<th>Items</th>\n");
						Str.append("<th data-hide=\"phone\">Model</th>");
						Str.append("<th data-hide=\"phone\">Description</th>\n");
						Str.append("<th data-hide=\"phone\">Type</th>\n");
						Str.append("<th data-hide=\"phone\">Fuel</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">UOM</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">EOQ</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Category</th>\n");
						Str.append("<th  data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");

						while (crs.next()) {

							if (!crs.getString("item_img").equals("")) {
								item_img = "<img src=../Thumbnail.do?itemimg=" + crs.getString("item_img") + "&width=100 alt=" + crs.getString("item_name") + "><br>";
							} else {
								item_img = "";
							}
							count++;
							if (crs.getString("item_active").equals("0")) {
								active = "<b><font color=\"red\">[Inactive]</font></b>";
							} else {
								active = "";
							}
							Str.append("<tr>\n<td>").append(count);
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("item_id"));
							Str.append("</td>\n");
							Str.append("<td>").append(item_img).append(crs.getString("item_name")).append(" ").append(active);
							if (!crs.getString("item_code").equals("")) {
								Str.append(" <br>").append("Code: ").append(crs.getString("item_code"));
							}
							if (!crs.getString("item_service_code").equals("")) {
								Str.append(" <br>").append("Service Code: ").append(crs.getString("item_service_code"));
							}
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("model_name"));
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("item_small_desc").replace("\n", "<br/>"));
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("type_name"));
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("fueltype_name"));
							Str.append("&nbsp;</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("uom_name"));
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(crs.getString("item_eoq"));
							Str.append("</td>\n");
							Str.append("<td>");
							if (!crs.getString("cat_id").equals("")) {
								Str.append(crs.getString("cat_name"));
							}
							Str.append("</td>\n");
							Str.append("<td nowrap>");
							update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
									+ "<i class='fa fa-pencil'></i></button>"
									+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"
									+ "<li role=presentation><a href=\"inventory-item-update.jsp?update=yes&item_id="
									+ crs.getString("item_id") + " \">Update Item</a></li>"
									+ "<li role=presentation><a href=\"inventory-item-image.jsp?item_id=" + crs.getString("item_id") + "\">Update Image</a></li>";
							if (crs.getString("item_aftertaxcal").equals("0")) {
								update_info += "<li role=presentation><a href=\"item-price-list.jsp?cat_id=1&item_id="
										+ crs.getString("item_id") + " \">List Item Price</a></li>";
								if (crs.getString("item_type_id").equals("1") || crs.getString("item_type_id").equals("3")) {
									update_info += "<li role=presentation><a href=\"item-options.jsp?item_id=" + crs.getString("item_id") + "\">Configure Product</a></li>";
								}
								if (crs.getString("item_loyaltycard_id").equals("1")) {
									update_info += "<li role=presentation><a href=\"inventory-prepaiditems.jsp?trans_itemmaster_id=" + crs.getString("item_id")
											+ "\">Manage Prepaid Card Items</a></li>";
								}
							}
							update_info += "</ul></div></center></div>";
							Str.append(update_info);
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						crs.close();
					} else {
						RecCountDisplay = "<br><br><br><br><font color=\"red\">No Item(s) found!</font><br><br>";
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
