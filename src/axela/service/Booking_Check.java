/* Bhagwan Singh*/
package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Booking_Check extends Connect {

	public String parking = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String emp = "";
	public String booking_emp = "";
	public String msg = "";
	public String StrHTML = "";
	public String location_id = "0";
	public String location = "";
	public String item_name = "";
	public String search_text = "";
	public String stock_status = "";
	public String branch_id = "0";
	public String emp_branch_id = "0";
	public String branch_emp = "";
	public String exe_branch_id = "0";
	public String calender = "";
	public String booking_id = "0";
	public String bookingitem_id = "0";
	public String add_apptitem = "";
	public String booking_time = "";
	public String booking_branch_id = "0";
	public String bookingitem_booking_id = "0";
	public String bookingitem_location_id = "0";
	public String bookingitem_item_id = "0";
	public String bookingitem_qty = "";
	public String update_apptitem = "";
	public String delete_apptitem = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			parking = PadQuotes(request.getParameter("parking"));
			booking_emp = PadQuotes(request.getParameter("booking_emp"));
			emp = PadQuotes(request.getParameter("service_emp"));
			stock_status = PadQuotes(request.getParameter("stock_status"));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			location = PadQuotes(request.getParameter("location"));
			item_name = PadQuotes(request.getParameter("item_name"));
			search_text = PadQuotes(request.getParameter("search_text"));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			emp_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
			exe_branch_id = CNumeric(PadQuotes(request.getParameter("exe_branch_id")));
			branch_emp = PadQuotes(request.getParameter("branch_emp"));
			calender = PadQuotes(request.getParameter("calender"));
			booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
			add_apptitem = PadQuotes(request.getParameter("add_apptitem"));
			booking_time = PadQuotes(request.getParameter("booking_time"));
			booking_branch_id = CNumeric(PadQuotes(request.getParameter("booking_branch_id")));
			bookingitem_booking_id = CNumeric(PadQuotes(request.getParameter("bookingitem_booking_id")));
			bookingitem_item_id = CNumeric(PadQuotes(request.getParameter("bookingitem_item_id")));
			bookingitem_location_id = CNumeric(PadQuotes(request.getParameter("bookingitem_location_id")));
			bookingitem_qty = CNumeric(PadQuotes(request.getParameter("bookingitem_qty")));
			bookingitem_id = CNumeric(PadQuotes(request.getParameter("bookingitem_id")));
			update_apptitem = PadQuotes(request.getParameter("update_apptitem"));
			delete_apptitem = PadQuotes(request.getParameter("delete_apptitem"));

			if (parking.equals("yes")) {
				StrHTML = new Booking_Update().PopulateParking(booking_branch_id, booking_time, booking_id, comp_id);
			} else if (booking_emp.equals("yes")) {
				StrHTML = new Booking_Update().PopulateExecutive(booking_branch_id, comp_id);
			} else if (emp.equals("yes")) {
				StrHTML = new Booking_Update().PopulateServiceExecutive(booking_branch_id, "0", comp_id);
			} else if (location.equals("yes")) {
				StrHTML = new Booking_Update().PopulateLocation(booking_branch_id, comp_id);
			} else if (stock_status.equals("yes")) {
				if (location_id.equals("0")) {
					StrHTML = "<br><br><font color=\"red\"><b>Select Location!</b></font><br><br><br>";
				} else if (!location_id.equals("0") && search_text.equals("")) {
					StrHTML = "<br><br><font color=\"red\"><b>Enter Search Text!</b></font><br><br><br>";
				} else {
					StrSearch += " AND location_id = " + location_id + ""
							+ " AND item_name LIKE '%" + item_name + "%'";
					StrHTML = new Call_Check().StockOnHandDetails(comp_id);
				}
			} else if (add_apptitem.equals("yes")) {
				AddApptItem();
			} else if (!bookingitem_id.equals("0") && update_apptitem.equals("yes")) {
				UpdateApptItem();
			} else if (!bookingitem_id.equals("0") && delete_apptitem.equals("yes")) {
				DeleteApptItem();
			} else if (branch_emp.equals("yes") && !emp_branch_id.equals("")) {
				StrHTML = new Leave_Update().PopulateExecutive(emp_branch_id);
			} else if (calender.equals("yes") && !exe_branch_id.equals("")) {
				StrHTML = new Advisor_Cal().PopulateExecutive(exe_branch_id, comp_id);
			}
		}

	}

	public void AddApptItem() throws SQLException {

		StrSql = "SELECT stock_current_qty"
				+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
				+ " WHERE stock_item_id = " + bookingitem_item_id + ""
				+ " AND stock_location_id = " + bookingitem_location_id + "";
		if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) < Integer.parseInt(bookingitem_qty)) {
			msg = "Item out of stock!";
		} else {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_booking_item"
					+ " (bookingitem_booking_id,"
					+ " bookingitem_location_id,"
					+ " bookingitem_item_id,"
					+ " bookingitem_qty)"
					+ " VALUES"
					+ " (" + bookingitem_booking_id + ","
					+ " " + bookingitem_location_id + ","
					+ " " + bookingitem_item_id + ","
					+ " " + bookingitem_qty + ")";
			updateQuery(StrSql);
		}
		StrHTML = ListApptItems(bookingitem_booking_id, comp_id);
	}

	public void UpdateApptItem() {
		StrSql = "SELECT bookingitem_qty"
				+ " FROM " + compdb(comp_id) + "axela_service_booking_item"
				+ " WHERE bookingitem_id = " + bookingitem_id + "";
		int prev_qty = (int) Double.parseDouble(CNumeric(ExecuteQuery(StrSql)));

		StrSql = "SELECT stock_current_qty"
				+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
				+ " WHERE stock_item_id = " + bookingitem_item_id + ""
				+ " AND stock_location_id = " + bookingitem_location_id + "";

		if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) < (Integer.parseInt(bookingitem_qty) - prev_qty)) {
			msg = "Item out of stock!";
		} else {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_booking_item"
					+ " SET"
					+ " bookingitem_qty = " + bookingitem_qty + ""
					+ " WHERE bookingitem_id = " + bookingitem_id + ""
					+ " AND bookingitem_booking_id = " + bookingitem_booking_id + "";
			updateQuery(StrSql);
		}
		StrHTML = ListApptItems(bookingitem_booking_id, comp_id);
	}

	public void DeleteApptItem() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_booking_item"
				+ " WHERE bookingitem_id = " + bookingitem_id + "";
		updateQuery(StrSql);

		StrHTML = ListApptItems(bookingitem_booking_id, comp_id);
	}

	public String ListApptItems(String bookingitem_booking_id, String comp_id) {
		// After Add/Update/Delete update the Current Stock....
		// UpdateStock(comp_id, "0", "0");

		StringBuilder Str = new StringBuilder();
		Double cart_qty = 0.00;
		int count = 0;
		StrSql = "SELECT bookingitem_id, bookingitem_item_id, bookingitem_booking_id, bookingitem_location_id,"
				+ " item_name, bookingitem_qty, item_code, item_id, uom_name"
				+ " FROM " + compdb(comp_id) + "axela_service_booking_item"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = bookingitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
				+ " WHERE bookingitem_booking_id = " + bookingitem_booking_id + ""
				+ " GROUP BY bookingitem_id"
				+ " ORDER BY bookingitem_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		Str.append("<div class=\"table-responsive\">\n");
		Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
		Str.append("<thead>\n");
		try {
			if (!msg.equals("")) {
				Str.append("<tr>\n<td colspan=\"4\" align=\"center\"><font color=\"red\"><b>");
				Str.append(msg).append("</b></font></td>\n</tr>\n");
			}
			Str.append("<tr>\n<th>#</th>\n");
			Str.append("<th>X</th>\n");
			Str.append("<th>Item</th>\n");
			Str.append("<th>Qty</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					cart_qty += Double.parseDouble(crs.getString("bookingitem_qty"));

					String booking_item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						booking_item_name += " (" + crs.getString("item_code") + ")";
					}

					count++;
					Str.append("<tr valign=\"top\"");
					Str.append(" onClick=\"PopulateDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append("'").append(booking_item_name.replace("'", "single_quote").replace("&#39;", "single_quote")).append("',");
					Str.append(crs.getDouble("bookingitem_qty")).append(",");
					Str.append(crs.getDouble("bookingitem_location_id")).append(",'");
					Str.append(crs.getString("uom_name")).append("',");
					Str.append(crs.getString("bookingitem_id")).append(",");
					Str.append("'update');\">\n<td align=\"center\">").append(count);
					Str.append("</td>\n<td align=\"center\">");
					Str.append("<a href=\"javascript:delete_booking_item(").append(crs.getString("bookingitem_id")).append(",");
					Str.append(crs.getString("bookingitem_booking_id")).append(");\">X</a>");
					Str.append("</td>\n<td align=\"left\">").append(booking_item_name);
					Str.append("</td>\n<td align=\"right\">").append(crs.getDouble("bookingitem_qty"));
					Str.append("</td>\n</tr>\n");
				}
			}
			Str.append("<tr>\n");
			Str.append("<td colspan=\"3\" align=\"right\"><b>Total:</b> </td>\n");
			Str.append("<td align=\"right\"><b>").append(cart_qty).append("</b></td>\n</tr>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</tbody>\n");
		Str.append("</table>\n");
		Str.append("</div>\n");
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doGet(request, response);
	}
}
