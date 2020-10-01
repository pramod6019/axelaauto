package axela.service;
//aJIt 30th Oct, 2013
//$at!sh 30-Oct-2013

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Booking_Item_Details extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String bookingcart_session_id = "0", bookingcart_id = "0";
	public String bookingcart_item_id = "0", bookingcart_bookingitem_id = "0";
	public String bookingcart_location_id = "0";
	public String bookingcart_qty = "";
	public String list_cartitems = "", msg = "";
	public String add_cartitem = "";
	public String update_cartitem = "";
	public String delete_cartitem = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			bookingcart_session_id = CNumeric(PadQuotes(request.getParameter("session_id")));
			bookingcart_item_id = CNumeric(PadQuotes(request.getParameter("bookingcart_item_id")));
			bookingcart_location_id = CNumeric(PadQuotes(request.getParameter("booking_location_id")));
			bookingcart_bookingitem_id = CNumeric(PadQuotes(request.getParameter("bookingitem_id")));
			bookingcart_qty = CNumeric(PadQuotes(request.getParameter("bookingcart_qty")));
			bookingcart_id = CNumeric(PadQuotes(request.getParameter("bookingcart_id")));
			list_cartitems = PadQuotes(request.getParameter("list_cartitems"));
			add_cartitem = PadQuotes(request.getParameter("add_cartitem"));
			update_cartitem = PadQuotes(request.getParameter("update_cartitem"));
			delete_cartitem = PadQuotes(request.getParameter("delete_cartitem"));
			// SOP("StrSq123==" + bookingcart_session_id);

			if (!bookingcart_session_id.equals("0") && add_cartitem.equals("yes") && msg.equals("")) {
				AddApptCartItem();
			} else if (!bookingcart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("")) {
				UpdateApptCartItem();
			} else if (!bookingcart_session_id.equals("0") && list_cartitems.equals("yes")) {
				StrHTML = ListApptCartItems(bookingcart_session_id);
			} else if (!bookingcart_id.equals("0") && delete_cartitem.equals("yes") && !bookingcart_session_id.equals("0")) {
				DeleteApptCartItem();
			}
		}

	}

	public void AddApptCartItem() throws SQLException {
		StrSql = "SELECT stock_current_qty FROM " + compdb(comp_id) + "axela_inventory_stock"
				+ " WHERE stock_item_id = " + bookingcart_item_id + ""
				+ " AND stock_location_id = " + bookingcart_location_id + "";
		// SOP("StrSql123123==" + StrSqlBreaker(StrSql));
		if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) < Integer.parseInt(bookingcart_qty)) {
			msg = "Item out of stock!";
		} else {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_booking_cart"
					+ " (bookingcart_session_id,"
					+ " bookingcart_bookingitem_id,"
					+ " bookingcart_location_id,"
					+ " bookingcart_item_id,"
					+ " bookingcart_qty)"
					+ " VALUES"
					+ " (" + bookingcart_session_id + ","
					+ " " + bookingcart_bookingitem_id + ","
					+ " " + bookingcart_location_id + ","
					+ " " + bookingcart_item_id + ","
					+ " " + bookingcart_qty + ")";
			updateQuery(StrSql);
		}
		StrHTML = ListApptCartItems(bookingcart_session_id);
	}

	public void UpdateApptCartItem() {
		StrSql = "SELECT stock_current_qty"
				+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
				+ " WHERE stock_item_id = " + bookingcart_item_id + ""
				+ " AND stock_location_id = " + bookingcart_location_id + "";

		if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) < Integer.parseInt(bookingcart_qty)) {
			msg = "Item out of stock!";
		} else {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_booking_cart"
					+ " SET"
					+ " bookingcart_qty = " + bookingcart_qty + ""
					+ " WHERE bookingcart_id = " + bookingcart_id + "";
			updateQuery(StrSql);
		}
		StrHTML = ListApptCartItems(bookingcart_session_id);
	}

	public void DeleteApptCartItem() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_booking_cart"
				+ " WHERE bookingcart_id = " + bookingcart_id + "";
		updateQuery(StrSql);

		StrHTML = ListApptCartItems(bookingcart_session_id);
	}

	public String ListApptCartItems(String cart_session_id) {
		StringBuilder Str = new StringBuilder();
		Double cart_qty = 0.00;
		int count = 0;
		StrSql = "SELECT bookingcart_id, bookingcart_item_id, item_name, bookingcart_location_id,"
				+ " bookingcart_qty, item_code, item_id, uom_name"
				+ " FROM " + compdb(comp_id) + "axela_service_booking_cart"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = bookingcart_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
				+ " WHERE bookingcart_session_id = " + cart_session_id + ""
				+ " GROUP BY bookingcart_id"
				+ " ORDER BY bookingcart_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		// Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
		Str.append("<table class=\"table table-bordered table-hover table-responsive\">\n");
		Str.append("<thead><tr>\n");
		try {
			if (!msg.equals("")) {
				Str.append("<tr>\n<td colspan=\"4\" align=\"center\"><font color=\"red\"><b>").append(msg).append("</b></font></td></tr>");
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
					cart_qty += Double.parseDouble(crs.getString("bookingcart_qty"));

					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ")";
					}

					count++;
					Str.append("<tr valign=\"top\"");
					Str.append(" onClick=\"PopulateDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append("'").append((item_name.replace("'", "single_quote")).replace("&#39;", "single_quote")).append("', ");
					Str.append(crs.getDouble("bookingcart_qty")).append(",");
					Str.append(crs.getDouble("bookingcart_location_id")).append(",'");
					Str.append(crs.getString("uom_name")).append("',");
					Str.append(crs.getString("bookingcart_id")).append(",");
					Str.append("'update');\">\n<td align=\"center\">\n");
					Str.append(count).append("</td>\n<td align=\"center\">");
					Str.append("<a href=\"javascript:delete_cart_item(").append(crs.getString("bookingcart_id")).append(");\">X</a>");
					Str.append("</td>\n<td align=\"left\">");
					Str.append(item_name).append("</td>\n<td align=\"right\">");
					Str.append(crs.getDouble("bookingcart_qty")).append("</td>\n</tr>\n");
				}
			}
			Str.append("<tr>\n<td colspan=\"3\" align=\"right\"><b>Total:</b></td>\n");
			Str.append("<td align=\"right\"><b>").append(cart_qty).append("</b></td>\n</tr>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</tbody>\n");
		Str.append("</table>\n");
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doGet(request, response);
	}
}
