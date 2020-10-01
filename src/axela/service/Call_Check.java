package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Call_Check extends Connect {

	public String StrSearch = "";
	public String BranchAccess = "";
	public String call_branch_id = "0";
	public String call_emp = "";
	public String service_emp = "";
	public String call_veh_id = "0";
	public String call_contact_id = "0";
	public String StrSql = "";
	public String comp_id = "0";
	public String item_name = "";
	public String search_text = "";
	public String parking = "";
	public String location = "";
	public String contact = "";
	public String booking_id = "0";
	public String customer_id = "0";
	public String location_id = "0";
	public String stock_status = "";
	public String StrHTML = "";
	public String booking_time = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			call_veh_id = CNumeric(PadQuotes(request.getParameter("call_veh_id")));
			call_contact_id = CNumeric(PadQuotes(request.getParameter("call_contact_id")));
			call_branch_id = PadQuotes(request.getParameter("call_branch_id"));
			booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
			customer_id = PadQuotes(request.getParameter("customer_id"));
			service_emp = PadQuotes(request.getParameter("service_emp"));
			call_emp = PadQuotes(request.getParameter("call_emp"));
			parking = PadQuotes(request.getParameter("parking"));
			location = PadQuotes(request.getParameter("location"));
			item_name = PadQuotes(request.getParameter("item_name"));
			search_text = PadQuotes(request.getParameter("search_text"));
			booking_time = PadQuotes(request.getParameter("booking_time"));
			contact = PadQuotes(request.getParameter("contact"));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			stock_status = PadQuotes(request.getParameter("stock_status"));
			Call_Update booking_call = new Call_Update();

			if (call_emp.equals("yes")) {
				StrHTML = booking_call.PopulateExecutive(call_branch_id, comp_id);
			} else if (service_emp.equals("yes")) {
				StrHTML = booking_call.PopulateServiceExecutive(call_branch_id, booking_time, comp_id);
			} else if (parking.equals("yes")) {
				StrHTML = booking_call.PopulateParking(call_branch_id, booking_time, booking_id, comp_id);
			} else if (location.equals("yes")) {
				StrHTML = booking_call.PopulateLocation(call_branch_id, comp_id);
			} else if (contact.equals("yes")) {
				StrHTML = booking_call.PopulateBookingContact(customer_id);
			} else if (stock_status.equals("yes")) {
				if (location_id.equals("0")) {
					StrHTML = "<br><br><font color=\"red\"><b>Select Location!</b></font><br><br>";
				} else if (!location_id.equals("0") && search_text.equals("")) {
					StrHTML = "<br><br><font color=\"red\"><b>Enter Search Text!</b></font><br><br><br>";
				} else {
					StrSearch += " AND location_id = " + location_id + ""
							+ " AND item_name LIKE '%" + item_name + "%'";
					StrHTML = StockOnHandDetails(comp_id);
				}
			}
		}
	}

	/* Through Search get Item in Stock Status Tab (Vehicle-Dash.jsp) */
	public String StockOnHandDetails(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, uom_name, stock_reorderlevel,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS itemname,"
					+ " stock_current_qty, stock_stockinorder, stock_stockindemand,"
					+ " stock_unit_cost, stock_onhandvalue, item_eoq"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON item_uom_id = uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON stock_location_id = location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " WHERE item_active = 1" + StrSearch + BranchAccess + ""
					+ " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 10";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				double nett_available = 0.00;
				String short_fall = "", order = "";

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\"><b>#</b></th>\n");
				Str.append("<th><b>Item ID</b></th>\n");
				Str.append("<th><b>Item Name</b></th>\n");
				Str.append("<th><b>UOM</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Unit Cost</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>On Hand Value</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Closing Stock</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Purchase Order Pending</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Sale Orders Due</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Nett Available</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Reorder Level</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Short Fall</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Minimum Order Quantity</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Order to be Placed</b></th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					short_fall = "";
					order = "";
					nett_available = crs.getDouble("stock_current_qty") + crs.getDouble("stock_stockinorder") - crs.getDouble("stock_stockindemand");
					short_fall = Double.toString(crs.getDouble("stock_reorderlevel") - nett_available);
					if (Double.parseDouble(CNumeric(short_fall)) < 1) {
						short_fall = "";
					}

					if (!short_fall.equals("")) {
						if (Double.parseDouble(CNumeric(short_fall)) > crs.getDouble("stock_reorderlevel")) {
							order = short_fall;
						} else {
							order = crs.getString("stock_reorderlevel");
						}
					}

					Str.append("<tr onClick=\"PopulateDetails(").append(crs.getString("item_id")).append(", '");
					Str.append((crs.getString("itemname").replace("'", "single_quote")).replace("&#39;", "single_quote")).append("', ");
					Str.append("'1',");
					Str.append("'0', '");
					Str.append(crs.getString("uom_name")).append("', ");
					Str.append("'0', ");
					Str.append("'add');\"'>\n");
					Str.append("<td align=\"center\" valign=\"top\" height=\"20\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("item_id")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("itemname")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("uom_name")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_unit_cost")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_onhandvalue")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_current_qty")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_stockinorder")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_stockindemand")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(nett_available).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_reorderlevel")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(short_fall).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("item_eoq")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(order).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=\"red\"><b>No Stock!<b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
