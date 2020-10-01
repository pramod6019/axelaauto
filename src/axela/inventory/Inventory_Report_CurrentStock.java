package axela.inventory;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_CurrentStock extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrSql = "";
	public String location_id = "", branch_id = "";
	public String BranchAccess;
	public String cat_id = "";
	public String date = "";
	public String msg = "";
	public String go = "", search = "";

	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access,emp_purchese_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				date = strToShortDate(ToLongDate(kknow()));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				cat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));
				// go = PadQuotes(request.getParameter("submit_button"));
				search = PadQuotes(request.getParameter("search"));
				go = PadQuotes(request.getParameter("go"));

				if (go.equals("Go")) {
					// if (search.equals("")) {
					if (branch_id.equals("0")) {
						msg = "Select Branch!";
					} else {
						StrSearch = StrSearch + " and branch_id = " + branch_id + "";
					}
					if (location_id.equals("0")) {
						// msg = msg + "<br>Select Location!";
					} else {
						StrSearch = StrSearch + " and location_id = " + location_id + "";
					}
					// }
					if (!cat_id.equals("0")) {
						StrSearch = StrSearch + " and item_cat_id = " + cat_id + "";
					}
					if (!search.equals("") && search.length() < 4) {
						msg = "Item Name should be more than 3 characters";
					}
					// SOP("msg===" + msg);
					if (msg.equals("")) {
						StrHTML = StockOnHandDetails(request);
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
	public String StockOnHandDetails(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		DecimalFormat fmt1 = new DecimalFormat("#.##");
		double totalDue = 0;
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_location_id = location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id";
			if (!location_id.equals("") && !location_id.equals("0"))
				StrSql += " WHERE location_id = " + location_id;
			StrSql += BranchAccess + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql==StockOnHandDetails==" + StrSql);

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("");
				while (crs1.next()) {
					Str.append("<center><b>").append(crs1.getString("location_name")).append("</b></center>");
					Str.append("");
					StrSql = " SELECT item_id, item_code, COALESCE(model_name,'') AS model_name,"
							+ " item_name, uom_name, stock_reorderlevel,"
							+ " stock_current_qty, stock_stockinorder, stock_stockindemand, stock_unit_cost,"
							+ "	COALESCE ( stock_unit_cost * stock_current_qty, 0 ) AS stock_onhandvalue, item_eoq"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop on cat_id = item_cat_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom on item_uom_id = uom_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location on stock_location_id = location_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = location_branch_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " WHERE item_active = 1"
							+ " AND item_type_id !=1"
							+ " AND item_nonstock =0"
							+ "	AND stock_current_qty != 0"
							+ " AND ( item_name LIKE ('%" + search + "%')"
							+ " OR item_id LIKE ('%" + search + "%')"
							+ " OR item_code LIKE ('%" + search + "%'))"
							+ StrSearch
							+ BranchAccess
							+ " group by item_id"
							+ " order by item_name"
							+ " LIMIT 5000";
					// SOP("StrSql===StockOnHandDetails==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						double due = 0.0, totalonhand = 0.0, totalclosingstock = 0.0, totalnetvalue = 0.0, nett_available = 0.00;
						String short_fall = "", order = "";
						int count = 0;
						Str.append("<div class=\"table-responsive table-bordered\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\"><b>Item ID</b></th>\n");
						Str.append("<th data-hide=\"phone\"><b>Item Name</b></th>\n");
						Str.append("<th><b>Item Code</b></th>\n");
						Str.append("<th><b>Model</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>UOM</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Unit Cost</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>On Hand Value</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Closing Stock</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Purchase Order Pending</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Sale Orders Due</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Nett Available</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Reorder Level</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Short Fall</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Minimum Order Quantity</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Order to be Placed</b></th>\n");
						Str.append("</tr></thead>");
						while (crs.next()) {
							count++;
							totalonhand += crs.getDouble("stock_onhandvalue");
							totalclosingstock += crs.getDouble("stock_current_qty");
							short_fall = "";
							order = "";
							nett_available = Double.parseDouble(df.format(crs.getDouble("stock_current_qty") + crs.getDouble("stock_stockinorder") - crs.getDouble("stock_stockindemand")));
							totalnetvalue += nett_available;
							short_fall = df.format(crs.getDouble("stock_reorderlevel") - nett_available);
							if (Double.parseDouble(CNumeric(short_fall)) < 1) {
								short_fall = "";
							}
							if (!short_fall.equals("")) {
								if (Double.parseDouble(CNumeric(short_fall)) > crs.getDouble("stock_reorderlevel")) {
									order = short_fall;
								} else {
									order = df.format(crs.getDouble("stock_reorderlevel"));
								}
							}
							Str.append("<tbody>\n");
							Str.append("<tr>\n");
							Str.append("<td align=center valign=top height=20>").append(count).append("</td>");
							Str.append("<td align=center valign=top>").append(crs.getString("item_id")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("item_name")).append("</td>");
							Str.append("<td valign=top align=left>").append(crs.getString("item_code")).append(" </td>");
							Str.append("<td valign=top align=left>").append(crs.getString("model_name")).append(" </td>");
							Str.append("<td align=left valign=top>").append(crs.getString("uom_name")).append("</td>");
							Str.append("<td align=right valign=top> ").append(df.format(crs.getDouble("stock_unit_cost"))).append("</td>");
							Str.append("<td align=right valign=top> ").append(df.format(crs.getDouble("stock_onhandvalue"))).append("</td>");
							Str.append("<td align=right valign=top>").append(df.format(crs.getDouble("stock_current_qty"))).append("</td>");
							Str.append("<td align=right valign=top>").append(df.format(crs.getDouble("stock_stockinorder"))).append("</td>");
							Str.append("<td align=right valign=top>").append(df.format(crs.getDouble("stock_stockindemand"))).append("</td>");
							Str.append("<td align=right valign=top>").append(nett_available).append("</td>");
							Str.append("<td align=right valign=top>").append(df.format(crs.getDouble("stock_reorderlevel"))).append("</td>");
							Str.append("<td align=right valign=top>").append(short_fall).append("</td>");
							Str.append("<td align=right valign=top> ").append(df.format(crs.getDouble("item_eoq"))).append("</td>");
							Str.append("<td align=right valign=top> ").append(order).append("</td>");
							Str.append("</tr>");
						}
						Str.append("<tr align=center>\n");
						Str.append("<td height=20 align=right colspan=7> <b>Total : </td><td align=right><b>").append(IndFormat(fmt1.format(totalonhand))).append("</b></td>\n");
						Str.append("<td height=20 align=right><b>").append(IndFormat(fmt1.format(totalclosingstock))).append("</b></td>\n");
						Str.append("<td height=20 align=right colspan=3><b>").append(IndFormat(fmt1.format(totalnetvalue))).append("</b></td>\n");
						Str.append("<td colspan=6>&nbsp;</td>");
						Str.append("</tr>");
						Str.append("</tbody>\n");
						Str.append("</table>");
						Str.append("</div>");
					} else {
						Str.append("<center><font color=red>No Stock!</font></center></td></tr>");
					}
					crs.close();
				}

			} else {
				Str.append("<center><font color=red><b>No Item(s) found!</b></font></center>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
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
				// SOP("StrSql location==" + StrSql);
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
