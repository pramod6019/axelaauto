package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Item_Conversion_Check extends Connect {
	public String emp_id = "0", comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String q = "";
	public String updatereorder = "";
	public String item_id = "0", itemgroup_id = "0", item = "";
	public String item_reorder_leaddays = "0", id = "";
	public String msg = "";
	public String vouchertype_id = "0";
	public String session_id = "0";
	public String itemgroup_idarr[] = null;
	public String rateclass_id = "0";
	public String location_id = "0";
	public String delete_cartitem = "";
	public String uom_id = "0";
	public String alt_uom_id = "0";
	public String conversion = "";
	public String display = "";
	public String additem = "";
	public String term = "";
	public String item_qty = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public static int count = 0;
	public Item_Conversion_Update obj = new Item_Conversion_Update();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				id = CNumeric(PadQuotes(request.getParameter("itemid")));
				emp_id = GetSession("emp_id", request);
				item = PadQuotes(request.getParameter("item"));
				itemgroup_id = PadQuotes(request.getParameter("itemgroup_id"));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				display = PadQuotes(request.getParameter("display"));
				additem = PadQuotes(request.getParameter("additem"));
				term = PadQuotes(request.getParameter("term"));
				item_id = PadQuotes(request.getParameter("item_id"));
				item_qty = PadQuotes(request.getParameter("item_qty"));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				session_id = CNumeric(PadQuotes(request.getParameter("session_id")));
				delete_cartitem = PadQuotes(request.getParameter("delete_cartitem"));

				if (delete_cartitem.equals("yes")) {
					DeleteCartItem();
				}
				if (item.length() > 1) {
					StrHTML = PopulateItems();
				}
				if (!id.equals("0")) {
					StrHTML = PopulateItems();
				}

				// add and display selected item
				if (additem.equals("yes")) {
					if (term.equals("issue")) {
						item_qty = "-" + item_qty;
					}
					AddItem(item_id, rateclass_id, item_qty, location_id, term);
					StrHTML = obj.ListCartItems(emp_id, vouchertype_id, term, comp_id);
				}
			}

		} else {
			StrHTML = "SignIn";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulateItems() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StrSql = "SELECT item_id, CONCAT(item_code,' (',item_name,')') AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id";
			if (!rateclass_id.equals("0")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = price_rateclass_id	";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
					+ " WHERE 1=1";
			// + " AND price_company_id =" + company_id;

			if (!itemgroup_id.equals("") && !itemgroup_id.equals("0")) {
				if (itemgroup_id.endsWith(",")) {
					itemgroup_id = itemgroup_id.substring(0,
							itemgroup_id.length() - 1);
				}
				StrSql += " AND item_itemgroup_id IN (" + itemgroup_id + ")";
			}
			if (!item.equals("")) {
				StrSql += " AND (item_name LIKE '" + item
						+ "%' OR item_code LIKE '" + item + "%')";
			} else if (!id.equals("0")) {
				StrSql += " AND item_id = " + id + "";
			}
			if (!rateclass_id.equals("0")) {
				StrSql += "  AND rateclass_id = " + rateclass_id;
			}
			StrSql += " GROUP BY item_id" + " ORDER BY item_name";
			if (!item.equals("")) {
				StrSql += " LIMIT 10";
			}
			// SOP("StrSql=====popitem===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!item.equals("")) {
						map.put("id", crs.getString("item_id"));
						map.put("text", crs.getString("item_name"));
						list.add(gson.toJson(map));
					}
					if (!id.equals("0")) {
						output.put("text", crs.getString("item_name"));
					}
				}
				if (!item.equals("")) {
					map.clear();
					output.put("items", list);
					list.clear();
				}
			} else {
				if (!item.equals("")) {
					output.put("items", "");
				}

				if (!id.equals("0")) {
					output.put("text", "");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return JSONPadQuotes(output.toString());
	}
	public void AddItem(String item_id, String rateclass_id, String item_qty,
			String location_id, String term) {
		String itemname = "";
		double itemprice = 0.00, qty = 0.00, altqty = 0.00;
		double itemamount = 0.00;
		double itemdisc = 0.00;
		double total = 0.00, altuomratio = 0.00, uomratio = 0.00;
		double tax1 = 0.00, tax3 = 0.00, tax2 = 0.00;
		String tax1_id = "0", tax2_id = "0", tax3_id = "0", taxid = "0", tax = "0";
		String rowcount = "0";
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT item_id, CONCAT(item_code,'-', item_name) AS item_name, item_code,"
				+ " item_uom_id,"
				+ " COALESCE((SELECT uom_ratio FROM " + compdb(comp_id) + "axela_inventory_uom WHERE uom_id = item_uom_id),0) AS uomratio,"
				// + " COALESCE((SELECT uom_ratio FROM axela_inventory_uom WHERE uom_id = item_alt_uom_id),0) AS altuomratio,"
				+ " price_amt, item_sales_ledger_id, item_salesdiscount_ledger_id,"
				+ " item_salestax1_ledger_id, item_salestax2_ledger_id, item_salestax3_ledger_id,"
				+ " COALESCE((SELECT customer_rate FROM " + compdb(comp_id) + "axela_customer where customer_id = item_salestax1_ledger_id),0) as tax1_rate,"
				+ " COALESCE((SELECT customer_rate FROM " + compdb(comp_id) + "axela_customer where customer_id = item_salestax2_ledger_id),0) as tax2_rate,"
				+ " COALESCE((SELECT customer_rate FROM " + compdb(comp_id) + "axela_customer where customer_id = item_salestax3_ledger_id),0) as tax3_rate,"
				+ " price_disc, price_disc_type"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item"
				// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_group ON itemgroup_id = item_itemgroup_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
				// +
				// " INNER JOIN axela_rate_class ON rateclass_id = price_rateclass_id	"
				+ " WHERE 1=1" + " AND item_id = " + CNumeric(item_id)
				// + " AND rateclass_id = "
				// + rateclass_id
				// + " AND rateclass_type = 2"
				+ " ORDER BY price_effective_from DESC LIMIT 1";
		// SOP("DisplayItem===" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {

			while (crs.next()) {
				count++;
				uomratio = crs.getDouble("uomratio");
				// altuomratio = crs.getDouble("altuomratio");

				altqty = Double.parseDouble(item_qty);
				// qty = altuomratio * altqty;
				// in our table alt_uom_id is not there
				qty = uomratio * altqty;
				itemprice = crs.getDouble("price_amt");
				if (term.equals("issue")) {
					itemamount = ((Double.parseDouble(item_qty.substring(1)) * crs.getDouble("price_amt")));
				} else if (term.equals("receive")) {
					itemamount = ((Double.parseDouble(item_qty) * crs.getDouble("price_amt")));
				}
				itemdisc = itemamount * (crs.getDouble("price_disc") / 100);
				Str.append("<td valign=top align=right>").append(df.format(itemdisc));
				Str.append("</td>");
				tax1_id = crs.getString("item_salestax1_ledger_id");
				tax2_id = crs.getString("item_salestax2_ledger_id");
				tax3_id = crs.getString("item_salestax3_ledger_id");
				tax3 = (itemamount * crs.getDouble("tax3_rate") / 100);
				tax1 = (itemamount * crs.getDouble("tax1_rate") / 100);
				tax2 = (itemamount * crs.getDouble("tax2_rate") / 100);
				tax3 = (itemamount * crs.getDouble("tax3_rate") / 100);

				total = itemamount - itemdisc + tax1 + tax2 + tax3;

				// main item
				rowcount = ExecuteQuery("SELECT COALESCE(MAX(cart_rowcount), 0)+1"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart (cart_vouchertype_id,"
						+ " cart_emp_id," + "cart_session_id,"
						+ " cart_location_id," + "cart_item_id,"
						+ " cart_rowcount," + "cart_qty," + "cart_price,"
						+ " cart_amount," + " cart_alt_qty,"
						// + " cart_alt_uom_id,"
						+ "cart_time" + ")"
						+ " VALUES (3," + ""
						+ emp_id
						+ ","
						+ ""
						+ session_id
						+ ","
						+ ""
						+ location_id
						+ ","
						+ ""
						+ item_id
						+ ","
						+ ""
						+ ""
						+ rowcount
						+ ","
						+ ""
						+ qty
						+ ","
						+ ""
						+ itemprice
						+ ","
						+ ""
						+ itemamount
						+ ","
						+ ""
						+ altqty
						+ ","
						// + ""
						// + crs.getString("uom_id")
						// + ","
						+ ToLongDate(kknow()) + "" + ")";
				// SOP("StrSql=main item==" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);

				// discount
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart (cart_vouchertype_id,"
						+ " cart_emp_id," + "cart_session_id,"
						+ " cart_item_id," + " cart_discount,"
						+ " cart_rowcount," + " cart_option_id,"
						+ " cart_amount," + "cart_time" + ")" + " VALUES (3,"
						+ "" + emp_id + "," + "" + session_id + "," + "" + item_id
						+ "," + "'1'," + "" + rowcount + "," + "" + rowcount
						+ "," + "" + itemdisc + "," + ToLongDate(kknow()) + ""
						+ ")";
				// SOP("StrSql=disc==" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);

				// tax
				for (int i = 1; i <= 3; i++) {
					if (i == 1) {
						taxid = "0";
						tax = "0";
						taxid = tax1_id;
						tax = tax1 + "";
					} else if (i == 2) {
						taxid = "0";
						tax = "0";
						taxid = tax2_id;
						tax = tax2 + "";
					} else if (i == 3) {
						taxid = "0";
						tax = "0";
						taxid = tax3_id;
						tax = tax3 + "";
					}
					if (!taxid.equals("0") && Double.parseDouble(tax) > 0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart (cart_vouchertype_id,"
								+ " cart_emp_id,"
								+ "cart_session_id,"
								+ " cart_item_id,"
								+ " cart_tax, cart_tax_id,"
								+ " cart_rowcount,"
								+ " cart_option_id,"
								+ " cart_amount,"
								+ "cart_time"
								+ ")"
								+ " VALUES (3," + ""
								+ emp_id
								+ ","
								+ ""
								+ session_id
								+ ","
								+ ""
								+ item_id
								+ ","
								+ "'1',"
								+ ""
								+ taxid
								+ ","
								+ ""
								+ rowcount
								+ ","
								+ ""
								+ rowcount
								+ ","
								+ ""
								+ tax1
								+ ","
								+ ToLongDate(kknow())
								+ ""
								+ ")";
						// SOP("StrSql=tax==" + StrSqlBreaker(StrSql));
						updateQuery(StrSql);
					}
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	// deleting cart item
	public void DeleteCartItem() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_item_id = "
					+ item_id + "" + " AND cart_session_id=" + session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			// SOP("StrSql==del==" + StrSql);
			updateQuery(StrSql);

		} catch (Exception e) {

			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}

	}
}
