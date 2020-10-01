package axela.inventory;
//aJIt 12th October, 2012

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_Critical extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrSql = "";
	public String location_id = "", branch_id = "";
	public String BranchAccess;
	public String cat_id = "";
	public String date = "";
	public String msg = "";
	public String go = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_purchese_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				date = strToShortDate(ToLongDate(kknow()));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				cat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("Go")) {
					if (branch_id.equals("0") || branch_id.equals("")) {
						msg = "Select Branch!";
					} else {
						StrSearch = StrSearch + " AND stock_location_id = " + location_id + "";
					}
					if (location_id.equals("0") || location_id.equals("")) {
						msg = msg + "<br>Select Location!";
					} else {
						StrSearch = StrSearch + " and stock_location_id = " + location_id + "";
					}
					if (!cat_id.equals("0")) {
						StrSearch = StrSearch + " and item_cat_id = " + cat_id + "";
					}
					if (msg.equals("")) {
						StrHTML = CriticalItemDetails(request);
					} else {
						StrHTML = "<center><font color='red'><b>" + msg + "</b></font></center>";
					}
				}

			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String CriticalItemDetails(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_location_id = location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " WHERE location_id = " + location_id + BranchAccess + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			CachedRowSet crs1 = processQuery(StrSql, 0);

			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("<center><b>").append(crs1.getString("location_name")).append("</b></center>");

					StrSql = "SELECT stock_item_id, item_code, item_name, cat_name, uom_name,"
							+ " stock_stockinorder, stock_current_qty, stock_stockindemand, stock_reorderlevel,"
							+ " (stock_current_qty + stock_stockinorder) AS available,"
							+ " IF(stock_reorderlevel > stock_stockindemand, stock_reorderlevel, stock_stockindemand) AS required"
							+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON stock_item_id = item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom on item_uom_id = uom_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop on cat_id = item_cat_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = location_branch_id"
							+ " WHERE"
							+ " stock_current_qty + stock_stockinorder < (stock_reorderlevel OR stock_stockindemand)"
							+ " AND item_nonstock = '0'"
							+ " AND item_active = 1"
							+ " AND item_type_id !=1"
							+ StrSearch
							+ BranchAccess
							+ " GROUP BY item_id"
							+ " ORDER BY item_name";
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						int count = 0;
						Str.append("<div class=\"table-responsive table-bordered\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th >#</th>\n");
						Str.append("<th data-toggle=\"true\"><b>Item ID</b></th>\n");
						Str.append("<th align=center><b>Item Name</b></th>\n");
						Str.append("<th align=center><b>Item Code</b></th>\n");
						Str.append("<th align=center><b>UOM</b></th>\n");
						Str.append("<th align=center><b>Reorder Level</b></th>\n");
						Str.append("<th align=center><b>Current Stock</b></th>\n");
						Str.append("<th align=center><b>Stock In Order</b></th>\n");
						Str.append("<th align=center><b>Stock In Demand</b></th>\n");
						Str.append("<th align=center><b>Available</b></th>\n");
						Str.append("<th align=center><b>Required</b></th>\n");
						Str.append("</tr></thead>");
						while (crs.next()) {
							count++;
							Str.append("<tbody>\n");
							Str.append("<tr>\n");
							Str.append("<td align=center valign=top>").append(count).append("</td>");
							Str.append("<td align=center valign=top>").append(crs.getString("stock_item_id")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("item_name")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("item_code")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("uom_name")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("stock_reorderlevel")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("stock_current_qty")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("stock_stockinorder")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("stock_stockindemand")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("available")).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("required")).append("</td>");
							Str.append("</tr>");
							Str.append("</tbody>\n");
						}
						Str.append("</table>");
						Str.append("</div>");
					} else {
						Str.append("<center><font color=red><b>No Items found!</b></font><center></td></tr>");
					}
					crs.close();
				}
			} else {
				Str.append("<center><font color=red><b>No Items found!</b></font></center>");
			}
			crs1.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		if (!branch_id.equals("")) {
			try {

				StrSql = "SELECT location_id, location_name, location_code"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + branch_id + ""
						+ " GROUP BY location_id"
						+ " ORDER BY location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<select name=\"dr_location_id\" id=\"dr_location_id\" class=\"form-control selectbox\">");
				Str.append("<option value = 0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), location_id));
					Str.append(">").append(crs.getString("location_name")).append(" (");
					Str.append(crs.getString("location_code")).append(")");
					Str.append("</option> \n");
				}
				Str.append("</select>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
}
