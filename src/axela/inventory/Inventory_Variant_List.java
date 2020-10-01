/* saiman 26th june 2012 */
//aJIt 9th October, 2012
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Inventory_Variant_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../inventory/index.jsp>Inventory</a>"
			+ " &gt; <a href=../inventory/inventory-variant-list.jsp?all=yes>List Variant</a><b>:</b>";
	public String LinkExportPage = "warehouse.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../inventory/inventory-variant-update.jsp?add=yes>Add New Variant..</a>";
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
	public String brand_name = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String item_id = "0", model_id = "0";
	public String item_img = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Variant ID", "numeric", "item_id"},
			{"Name", "text", "item_name"},
			{"Code", "text", "item_code"},
			{"Model", "text", "model_name"},
			{"Small Description", "text", "item_small_desc"},
			{"Big Description", "text", "item_big_desc"},
			{"Active", "boolean", "item_active"},
			{"Notes", "text", "item_notes"},
			{"Entry By", "text", "item_entry_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "item_entry_date"},
			{"Modified By", "text", "item_modified_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "item_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_service_insurance_add, emp_service_jobcard_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				item_id = CNumeric(PadQuotes(request.getParameter("variant_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				// PopulateConfigDetails();

				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					if (!item_id.equals("0")) {
						StrSearch = " AND item_id = " + item_id;
					} else {
						StrSearch = " AND item_id = 0";
					}
				} else if ("yes".equals(all)) {
					msg = "Results for all Variants!";
					StrSearch += " AND item_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Variants!";
					StrSearch += " AND item_id > 0";
				} else if (!item_id.equals("0")) {
					msg += "<br>Results for Variant ID = " + item_id + "!";
					StrSearch += " AND item_id = " + item_id + "";
				} else if (!model_id.equals("0")) {
					msg += "<br>Results for Model ID = " + model_id + "!";
					StrSearch += " AND item_model_id = " + model_id + "";
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
					if (!GetSession("variantstrsql", request).equals("")) {
						StrSearch += GetSession("variantstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("variantstrsql", StrSearch, request);
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
					StrSql = "SELECT item_id, item_name, item_img, fueltype_name, brand_id, brand_name,"
							+ " item_aftertaxcal, item_code, item_loyaltycard_id,"
							+ " item_small_desc, item_active, model_id, model_name";

					StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_item";

					StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
							+ " WHERE item_type_id = 1";

					CountSql = "SELECT COUNT(DISTINCT(item_id))";

					StrSql += StrJoin;
					CountSql += StrJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " GROUP BY item_id"
								+ " ORDER BY item_id DESC";
						CountSql += StrSearch;
					}
					// SOP("StrSql--------" + StrSql);

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
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Variant(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "inventory-variant-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						if (!all.equals("recent")) {
							crs = processQuery(StrSql, 0);
						}
						int count = StartRec - 1;
						String active = "";
						Str.append("<div class=\"table-responsive table-bordered\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\">Variants</th>\n");
						Str.append("<th>Model</th>");
						Str.append("<th data-hide=\"phone\">Principal</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Description</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Fuel</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
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
								active = "<b><font color=red>[Inactive]</font></b>";
							} else {
								active = "";
							}

							Str.append("<tr>\n");
							Str.append("<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=left >").append(item_img).append(crs.getString("item_name")).append("</a>").append(" ").append(active);
							if (!crs.getString("item_code").equals("")) {
								Str.append(" <br>").append("Code: ").append(crs.getString("item_code"));
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=left>").append("<a href=\"item-model.jsp?model_id=");
							Str.append(crs.getString("model_id")).append("\">").append(crs.getString("model_name")).append("" + " <br></td>");
							if (crs.getString("brand_id").equals("1"))
							{
								brand_name = "Maruti";
							}
							else if (crs.getString("brand_id").equals("2"))
							{
								brand_name = "Mahindra";
							}
							Str.append("<td valign=\"top\">").append(crs.getString("brand_name")).append("</td>\n");
							Str.append("<td valign=top align=left >");
							Str.append(crs.getString("item_small_desc")).append("</td>\n");
							Str.append("<td valign=top align=left>");
							Str.append(crs.getString("fueltype_name")).append("</td>\n");
							Str.append("<td valign=top align=left nowrap><a href=\"inventory-variant-update.jsp?update=yes&item_id=");
							Str.append(crs.getString("item_id")).append(" \">Update Variant</a><br>");
							Str.append("<a href=\"inventory-item-image.jsp?item_id=").append(crs.getString("item_id")).append("\">Update Image</a><br>");
							Str.append("<a href=\"inventory-itemservice-list.jsp?item_id=").append(crs.getString("item_id")).append("\">List Service</a><br>");
							Str.append("<a href=\"inventory-itemservice-update.jsp?add=yes&item_id=").append(crs.getString("item_id")).append("\">Add Service</a>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
					} else {
						RecCountDisplay = "<br><br><br><br><font color=red>No Variant(s) found!</font><br><br>";
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
