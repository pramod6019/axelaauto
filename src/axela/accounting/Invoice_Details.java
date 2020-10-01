package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Invoice_Details extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String add = "", status = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String ledger_id = "0";
	public String location_id = "0";
	public String item_ticket_dept_id = "0";
	public String add_cartitem = "";
	public String update_cartitem = "";
	public String delete_cartitem = "", delete_full_cart = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String comp_module_inventory = "0";
	public String config_inventory_current_stock = "0", para = "";
	public String configure = "", configure1 = "", mode = "";
	public String cart_session_id = "0", cart_id = "0";
	public String cart_item_id = "0", vouchertype_id = "0", voucherclass_id = "0";
	public String list_cartitems = "", msg = "", cart_rowcount = "", rowcount = "", refresh = "", cart_option_id = "0";
	public String cart_price = "0.00", cart_netprice = "0.00";
	public String qty = "0";
	public double cart_qty = 0.00, cart_uom_ratio = 1, cart_alt_qty = 1;
	public double cart_amount = 0.00, cart_disc_amount = 0.00;
	public String disc = "0.00", item_id = "0", cart_item_name = "";
	public String cart_convfactor = "";
	public double cart_disc = 0.00, cart_discpercent = 0.00;
	public double cart_unit_cost = 0.00;
	public String cart_time = "";
	public String tax_rate1 = "0.00";
	public String tax_rate2 = "0.00";
	public String tax_rate3 = "0.00";
	public String tax_id1 = "0";
	public String tax_id2 = "0";
	public String tax_id3 = "0";
	public String price_sales_customer_id = "0";
	public String price_discount1_customer_id = "0";
	public String tax_customer_id1 = "0";
	public String tax_customer_id2 = "0";
	public String tax_customer_id3 = "0";
	public String price_tax2_after_tax1 = "0";
	public String price_tax3_after_tax2 = "0";
	public String boxtype_size = "0";
	public double optiontotal = 0.00;
	public String usersession = "";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df6 = new DecimalFormat("0.000000");
	// For updating dc field
	public String cart_cust_disc_dc = "", voucher_id = "0";
	public String cart_ledger_tax_dc = "", cart_alt_uom_id = "0", uom_ratio = "1", cart_disc_type = "", discpercent = "0.00";
	// Variable to check duplicate items
	public String cart_dup_item_id = "0", temp_cart_voucher_id = "0";
	public String cart_voucher_so_id = "0", cart_voucher_delnote_id = "0", from_voucher_quantity = "0.00";
	public double from_voucher_qty = 0.00, cart_item_price = 0.00;
	public String cart_voucher_jc_id = "0";
	// -----
	public String vouchertype_name = "", branch_id = "0";
	public String rateclass_id = "", stock_qty = "0";

	// variables not required .....use when required
	public String cart_opitem_item_id = "0", cart_item_serial = "", cart_item_batch = "", stockserial_serial_no = "";
	public String cart_option_group = "";
	public String cart_opitem_price = "0.00", cart_opitem_netprice = "0.00", configitems_total = "0.00";
	public String cart_opitem_qty = "0";
	public String updatecount = "";

	// GST = state or central
	public String gst_type = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				refresh = PadQuotes(request.getParameter("refresh"));
				SetSession("refresh", refresh, request);
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request));
				config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				para = PadQuotes(request.getParameter("para"));

				gst_type = PadQuotes(request.getParameter("gst_type"));

				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("cart_vouchertype_id")));
				if (!vouchertype_id.equals("27")) {
					cart_cust_disc_dc = "1";
					cart_ledger_tax_dc = "0";
				} else {
					cart_cust_disc_dc = "1";
					cart_ledger_tax_dc = "0";
				}

				voucher_id = CNumeric(PadQuotes(request.getParameter("cart_voucher_id")));
				cart_voucher_so_id = CNumeric(PadQuotes(request.getParameter("voucher_so_id")));
				cart_voucher_jc_id = CNumeric(PadQuotes(request.getParameter("txt_jcid")));
				cart_voucher_delnote_id = CNumeric(PadQuotes(request.getParameter("voucher_delnote_id")));
				// SOP("voucher_id====" + voucher_id);
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				stock_qty = CNumeric(PadQuotes(request.getParameter("stock_qty")));
				cart_item_id = CNumeric(PadQuotes(request.getParameter("cart_item_id")));
				cart_item_name = CNumeric(PadQuotes(request.getParameter("cart_item_name")));
				cart_price = CNumeric(PadQuotes(request.getParameter("cart_price")));
				if (!cart_price.equals("0")) {
					cart_item_price = Double.parseDouble(cart_price);
				}
				cart_netprice = CNumeric(PadQuotes(request.getParameter("cart_netprice")));
				qty = CNumeric(PadQuotes(request.getParameter("cart_alt_qty")));
				if (!qty.equals("0")) {
					cart_alt_qty = Double.parseDouble(qty);
				}
				cart_alt_uom_id = CNumeric(PadQuotes(request.getParameter("cart_uom_id")));
				if (!cart_alt_uom_id.equals("0")) {
					uom_ratio = ExecuteQuery("SELECT uom_ratio" + " FROM " + compdb(comp_id) + "axela_inventory_uom" + " WHERE uom_id = " + cart_alt_uom_id + "");
					cart_uom_ratio = Double.parseDouble(uom_ratio);
				}
				if (cart_uom_ratio != 0.0) {
					cart_qty = cart_alt_qty * cart_uom_ratio;
				}
				// cart_amount =
				// CNumeric(PadQuotes(request.getParameter("cart_amount")));
				ledger_id = CNumeric(PadQuotes(request.getParameter("ledger_id")));
				price_sales_customer_id = CNumeric(PadQuotes(request.getParameter("price_sales_customer_id")));
				// SOP("price_sales_customer_id===" + price_sales_customer_id);
				if (!price_sales_customer_id.equals(ledger_id) && !ledger_id.equals("0")) {
					price_sales_customer_id = ledger_id;
				}
				disc = CNumeric(PadQuotes(request.getParameter("disc")));
				if (!disc.equals("0")) {
					cart_disc = Double.parseDouble(disc);
				}
				// SOP("cart_disc====" + cart_disc);
				discpercent = CNumeric(PadQuotes(request.getParameter("cart_discpercent")));
				// SOP("discpercent====" + discpercent);
				if (!discpercent.equals("0")) {
					cart_discpercent = Double.parseDouble(discpercent);
				}
				cart_disc_type = CNumeric(PadQuotes(request.getParameter("disc_type")));
				cart_unit_cost = cart_item_price - cart_disc;
				price_discount1_customer_id = CNumeric(PadQuotes(request.getParameter("price_discount1_customer_id")));
				tax_customer_id1 = CNumeric(PadQuotes(request.getParameter("tax_customer_id1")));
				tax_rate1 = CNumeric(PadQuotes(request.getParameter("tax_rate1")));
				tax_customer_id2 = CNumeric(PadQuotes(request.getParameter("tax_customer_id2")));
				tax_id1 = CNumeric(PadQuotes(request.getParameter("tax_id1")));
				tax_id2 = CNumeric(PadQuotes(request.getParameter("tax_id2")));
				tax_id3 = CNumeric(PadQuotes(request.getParameter("tax_id3")));
				tax_rate2 = CNumeric(PadQuotes(request.getParameter("tax_rate2")));
				tax_customer_id3 = CNumeric(PadQuotes(request.getParameter("tax_customer_id3")));
				tax_rate3 = CNumeric(PadQuotes(request.getParameter("tax_rate3")));
				price_tax2_after_tax1 = CNumeric(PadQuotes(request.getParameter("price_tax2_after_tax1")));
				price_tax3_after_tax2 = CNumeric(PadQuotes(request.getParameter("price_tax3_after_tax2")));
				boxtype_size = CNumeric(PadQuotes(CheckNull(request.getParameter("cart_boxtype_size"))));
				cart_time = PadQuotes(request.getParameter("cart_time"));
				rowcount = PadQuotes(request.getParameter("rowcount"));
				cart_option_id = CNumeric(PadQuotes(request.getParameter("cart_option_id")));
				mode = PadQuotes(request.getParameter("mode"));
				status = PadQuotes(request.getParameter("status"));
				cart_id = CNumeric(PadQuotes(request.getParameter("cart_id")));
				list_cartitems = PadQuotes(request.getParameter("list_cartitems"));
				configure = PadQuotes(request.getParameter("configure"));
				// Item Details Ajax call configure1 = yes
				configure1 = PadQuotes(request.getParameter("configure1"));
				add_cartitem = PadQuotes(request.getParameter("add_cartitem"));
				update_cartitem = PadQuotes(request.getParameter("update_cartitem"));
				delete_cartitem = PadQuotes(request.getParameter("delete_cartitem"));
				delete_full_cart = PadQuotes(request.getParameter("delete_full_cart"));
				cart_session_id = CNumeric(PadQuotes(request.getParameter("cart_session_id")));
				// Variables not required ....but use when required
				cart_item_serial = PadQuotes(request.getParameter("cart_item_serial"));
				cart_item_batch = PadQuotes(request.getParameter("cart_item_batch"));
				cart_opitem_item_id = CNumeric(PadQuotes(request.getParameter("cart_opitem_item_id")));
				item_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("item_ticket_dept_id")));
				cart_option_group = PadQuotes(request.getParameter("cart_option_group"));
				cart_convfactor = CNumeric(PadQuotes(request.getParameter("cart_convfactor")));
				cart_opitem_price = CNumeric(PadQuotes(request.getParameter("cart_opitem_price")));
				cart_opitem_netprice = CNumeric(PadQuotes(request.getParameter("cart_opitem_netprice")));
				configitems_total = CNumeric(PadQuotes(request.getParameter("configitems_total")));
				cart_opitem_qty = CNumeric(PadQuotes(request.getParameter("cart_opitem_qty")));
				// //////-------end----------------///////////

				// Item Amount
				// cart_netprice only we are keeping as cart_amount cos still its
				// not total price ie its excluding disc amt
				if (!CNumeric(cart_netprice).equals("0")) {
					cart_amount = Double.parseDouble(cart_netprice);
				}
				// Item Discount Amount
				cart_disc_amount = cart_disc * cart_qty;

				// To check item details from converting one voucher to another voucher

				if (!cart_item_id.equals("0") && !cart_session_id.equals("")) {
					StrSql = "SELECT cart_item_id FROM " + compdb(comp_id) + "axela_acc_cart"
							+ " WHERE 1=1 "
							+ " AND cart_vouchertype_id = " + vouchertype_id
							+ " AND cart_item_id = " + cart_item_id
							+ " AND cart_emp_id = " + emp_id
							+ " AND cart_session_id = " + cart_session_id;
					// SOP("StrSql==dup=="+StrSql);
					cart_dup_item_id = CNumeric(ExecuteQuery(StrSql));
				}

				if (!cart_item_id.equals("0") && configure1.equals("yes")) {
					if (!gst_type.equals("")) {
						StrHTML = getItemDetails(location_id, cart_item_id, rateclass_id, emp_id, cart_session_id, mode);
					} else {
						StrHTML = "<center><font color='#ff0000'><b>Select Party City!</b></font></center>";
					}
				}
				if (!emp_id.equals("0") && !cart_session_id.equals("0")
						&& add_cartitem.equals("yes")
						&& !cart_dup_item_id.equals("0")) {
					msg = "Item Already Present!";
					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes") && msg.equals("") && !location_id.equals("0")) {
					AddCartItem();
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes") && msg.equals("")
						&& location_id.equals("0") && !stock_qty.equals("0")) {
					AddCartItem();
				}
				else if (!cart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("") && config_inventory_current_stock.equals("1") && !location_id.equals("0")) {
					UpdateCartItem();
				} else if (!cart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("")) {
					UpdateCartItem();
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && list_cartitems.equals("yes")) {
					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0") && config_inventory_current_stock.equals("1")
						&& !location_id.equals("0")) {
					DeleteCartItem();
				} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0")
						&& !location_id.equals("0")) {
					DeleteCartItem();
				} else if (delete_full_cart.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0")) {
					DeleteCartAllItems();
				}

			} else {
				StrHTML = "SignIn";
			}
		}
	}
	public void AddCartItem() throws SQLException {

		// this is to compare main item with sales order
		if (!cart_voucher_so_id.equals("0")) {
			StrSql = "SELECT item_type_id FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + cart_item_id;
			String item_type_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

			if (item_type_id.equals("1")) {
				StrSql = "SELECT soitem_id FROM " + compdb(comp_id) + "axela_sales_so_item"
						+ " WHERE"
						+ " soitem_item_id = " + cart_item_id
						+ " AND soitem_so_id = " + cart_voucher_so_id;
				if (CNumeric(PadQuotes(ExecuteQuery(StrSql))).equals("0")) {
					msg = "Item Doesn't Match Sales Order Variant!";
					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				}
			}
		}

		if (Integer.parseInt(cart_convfactor) == 0) {
			cart_convfactor = "1";
		}
		double cart_truckspace = 0.00;
		double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
		double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
		double cart_amount_tax = 0.00;
		String order = "";
		cart_amount_tax = (cart_item_price + Double.parseDouble(configitems_total)) - cart_disc;

		tax1_amount = (cart_amount_tax * cart_qty * Double.parseDouble(tax_rate1) / 100);// including
																							// qty
		tax1_price = ((cart_amount_tax) * Double.parseDouble(tax_rate1) / 100);

		if (price_tax2_after_tax1.equals("1") && tax1_amount != 0.0) {
			tax2_amount = ((cart_amount_tax + tax1_price) * cart_qty * Double.parseDouble(tax_rate2) / 100);
			tax2_price = ((cart_amount_tax + tax1_price) * Double.parseDouble(tax_rate2) / 100);
			// SOP("tax2_amount==="+tax2_amount);
		} else {
			tax2_amount = (cart_amount_tax * cart_qty * Double.parseDouble(tax_rate2) / 100);
			tax2_price = ((cart_amount_tax) * Double.parseDouble(tax_rate2) / 100);
		}
		if (price_tax3_after_tax2.equals("1") && tax1_amount != 0.0 && tax2_amount != 0.0) {
			tax3_amount = ((cart_amount_tax + tax1_price + tax2_price) * cart_qty * Double.parseDouble(tax_rate3) / 100);
			tax3_price = ((cart_amount_tax + tax1_price + tax2_price) * Double.parseDouble(tax_rate3) / 100);
			// SOP("tax3_amount==="+tax3_amount);
		} else {
			tax3_amount = (cart_amount_tax * cart_qty * Double.parseDouble(tax_rate3) / 100);
			tax3_price = ((cart_amount_tax) * Double.parseDouble(tax_rate3) / 100);
		}

		// truck space calculation
		if (Double.parseDouble(boxtype_size) != 0.0) {
			cart_truckspace = ((cart_alt_qty / Double.parseDouble(boxtype_size)) * 100);
		}

		if (rowcount.equals("yes")) {

			cart_rowcount = ExecuteQuery("SELECT COALESCE(MAX(cart_rowcount), 0) + 1"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id);

			cart_option_id = "0";
		} else if (rowcount.equals("no")) {
			cart_rowcount = "0";
			if (cart_id.equals("0")) {
				StrSql = "SELECT COALESCE(MAX(cart_rowcount), 0)"
						+ " FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1"
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				cart_option_id = ExecuteQuery(StrSql);
				// SOP("cart_option_id===="+cart_option_id);
			} else {
				cart_option_id = ExecuteQuery("SELECT COALESCE(cart_rowcount, 0)"
						+ " FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_id = " + cart_id);
			}
			cart_option_id = CNumeric(cart_option_id);
		}

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (!cart_rowcount.equals("0")) {
					// SOP("cart_rowcount==" + cart_rowcount);
					// SOP("price_sales_customer_id==" + price_sales_customer_id);
					if (!price_sales_customer_id.equals("0")) {
						// SOP("price_sales_customer_id==" + price_sales_customer_id);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_emp_id,"
								+ " cart_session_id,"
								+ " cart_location_id,"
								+ " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount,"
								+ " cart_option_id,"
								+ " cart_option_group,"
								+ " cart_item_serial,"
								+ " cart_item_batch_id,"
								+ " cart_price,"
								+ " cart_qty,"
								+ " cart_truckspace,"
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_discountamount,"
								+ " cart_taxamount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_convfactor,"
								+ " cart_time," + " cart_dc"
								+ " ) VALUES ("
								+ " " + voucher_id + ","
								+ " " + price_sales_customer_id + ","
								+ " " + vouchertype_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " " + location_id + ","
								+ " " + cart_item_id + ","
								+ " '0',"
								+ " 0.0,"
								+ " '0',"
								+ " 0,"
								+ " " + cart_rowcount + ","
								+ " " + cart_option_id + ","
								+ " ''," + " ''," + " 0,"
								+ " " + cart_price + ","
								+ " " + cart_qty + ","
								+ " " + cart_truckspace + ","
								+ " " + cart_netprice + ","
								+ " " + cart_amount + ","
								+ " " + cart_disc_amount + ","
								+ " " + (tax1_amount + tax2_amount + tax3_amount) + ","
								+ " " + cart_unit_cost + ","
								+ " " + cart_alt_qty + ","
								+ " " + cart_alt_uom_id + ","
								+ " " + cart_convfactor + ","
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'" + " )";
						// SOP("StrSql==cart111=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);

						if (cart_disc != 0.0 || cart_disc == 0.0) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
									+ " (cart_voucher_id,"
									+ " cart_customer_id,"
									+ " cart_vouchertype_id,"
									+ " cart_emp_id,"
									+ " cart_session_id,"
									+ " cart_location_id,"
									+ " cart_item_id,"
									+ " cart_discount,"
									+ " cart_discount_perc,"
									+ " cart_tax,"
									+ " cart_tax_id,"
									+ " cart_rowcount,"
									+ " cart_option_id,"
									+ " cart_option_group,"
									+ " cart_item_serial,"
									+ " cart_item_batch_id,"
									+ " cart_price,"
									+ " cart_qty,"
									+ " cart_amount,"
									+ " cart_unit_cost,"
									+ " cart_alt_qty,"
									+ " cart_alt_uom_id,"
									+ " cart_convfactor,"
									+ " cart_time,"
									+ " cart_dc)"
									+ " VALUES" + " ("
									+ " " + voucher_id + ","
									+ " " + price_discount1_customer_id + ","
									+ " " + vouchertype_id + ","
									+ " " + emp_id + ","
									+ " " + cart_session_id + ","
									+ " '" + location_id + "',"
									+ " " + cart_item_id + ","
									+ " '1',"
									+ " " + cart_discpercent + ","
									+ " '0'," + " 0,"
									+ " " + cart_rowcount + ","
									+ " " + cart_rowcount + ","
									+ " ''," + " ''," + " 0,"
									+ " " + cart_disc + ","
									+ " 0,"
									+ " " + cart_disc_amount + ","
									+ " 0.00,"
									+ " 0,"
									+ " 0,"
									+ " 0,"
									+ " " + ToLongDate(kknow()) + ","
									+ " '" + cart_cust_disc_dc + "'" + ")";// cart_cust_disc_dc
							// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						}

						if (tax1_amount != 0.0 || tax1_amount == 0.0) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
									+ " (cart_voucher_id,"
									+ " cart_customer_id,"
									+ " cart_vouchertype_id,"
									+ " cart_emp_id,"
									+ " cart_session_id,"
									+ " cart_location_id,"
									+ " cart_item_id,"
									+ " cart_discount,"
									+ " cart_discount_perc,"
									+ " cart_tax,"
									+ " cart_tax_id,"
									+ " cart_rowcount,"
									+ " cart_option_id,"
									+ " cart_option_group,"
									+ " cart_item_serial,"
									+ " cart_item_batch_id,"
									+ " cart_price,"
									+ " cart_qty,"
									+ " cart_amount,"
									+ " cart_unit_cost,"
									+ " cart_alt_qty,"
									+ " cart_alt_uom_id,"
									+ " cart_convfactor,"
									+ " cart_time,"
									+ " cart_dc"
									+ " ) VALUES ("
									+ " " + voucher_id + ","
									+ " " + tax_customer_id1 + ","
									+ " " + vouchertype_id + ","
									+ " " + emp_id + ","
									+ " " + cart_session_id + ","
									+ " " + location_id + ","
									+ " " + cart_item_id + ","
									+ " '0',"
									+ " 0.0,"
									+ " '1',"
									+ " " + tax_id1 + ","
									+ " " + cart_rowcount + ","
									+ " " + cart_rowcount + ","
									+ " ''," + " ''," + " 0,"
									+ " " + tax1_price + ","
									+ " 0.00," + " "
									+ tax1_amount + ","
									+ " 0.00,"
									+ " 0,"
									+ " 0,"
									+ " 0,"
									+ " " + ToLongDate(kknow()) + ","
									+ " '" + cart_ledger_tax_dc + "'" + ")";
							// SOP("StrSql==cart333=" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						}
						if (tax2_amount != 0.0 || tax2_amount == 0.0) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
									+ " (cart_voucher_id,"
									+ " cart_customer_id,"
									+ " cart_vouchertype_id,"
									+ " cart_emp_id,"
									+ " cart_session_id,"
									+ " cart_location_id,"
									+ " cart_item_id,"
									+ " cart_discount,"
									+ " cart_discount_perc,"
									+ " cart_tax,"
									+ " cart_tax_id,"
									+ " cart_rowcount,"
									+ " cart_option_id,"
									+ " cart_option_group,"
									+ " cart_item_serial,"
									+ " cart_item_batch_id,"
									+ " cart_price,"
									+ " cart_qty,"
									+ " cart_amount,"
									+ " cart_unit_cost,"
									+ " cart_alt_qty,"
									+ " cart_alt_uom_id,"
									+ " cart_convfactor,"
									+ " cart_time,"
									+ " cart_dc"
									+ " ) VALUES ("
									+ " " + voucher_id + ","
									+ " " + tax_customer_id2 + ","
									+ " " + vouchertype_id + ","
									+ " " + emp_id + ","
									+ " " + cart_session_id + ","
									+ " '" + location_id + "',"
									+ " " + cart_item_id + ","
									+ " '0',"
									+ " 0.0,"
									+ " '1',"
									+ " " + tax_id2 + ","
									+ " " + cart_rowcount + ","
									+ " " + cart_rowcount + ","
									+ " '',"
									+ " '',"
									+ " 0,"
									+ " " + tax2_price + ","
									+ " 0.00,"
									+ " " + tax2_amount + ","
									+ " 0.00,"
									+ " 0,"
									+ " 0,"
									+ " 0,"
									+ " " + ToLongDate(kknow()) + ","
									+ " '" + cart_ledger_tax_dc + "'" + ")";
							// SOP("StrSql==cart444=" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						}

						if (tax3_amount != 0.0 || tax3_amount == 0.0) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
									+ " (cart_voucher_id, cart_customer_id,"
									+ " cart_vouchertype_id,"
									+ " cart_emp_id,"
									+ " cart_session_id,"
									+ " cart_location_id,"
									+ " cart_item_id,"
									+ " cart_discount,"
									+ " cart_discount_perc,"
									+ " cart_tax,"
									+ " cart_tax_id,"
									+ " cart_rowcount,"
									+ " cart_option_id,"
									+ " cart_option_group,"
									+ " cart_item_serial,"
									+ " cart_item_batch_id,"
									+ " cart_price,"
									+ " cart_qty,"
									+ " cart_amount,"
									+ " cart_unit_cost,"
									+ " cart_alt_qty,"
									+ " cart_alt_uom_id,"
									+ " cart_convfactor,"
									+ " cart_time,"
									+ " cart_dc"
									+ " ) VALUES ("
									+ " " + voucher_id + ","
									+ " " + tax_customer_id3 + ","
									+ " " + vouchertype_id + ","
									+ " " + emp_id + ","
									+ " " + cart_session_id + ","
									+ " " + location_id + ","
									+ " " + cart_item_id + ","
									+ " '0',"
									+ " 0.0,"
									+ " '1',"
									+ " " + tax_id3 + ","
									+ " " + cart_rowcount + ","
									+ " " + cart_rowcount + ","
									+ " '',"
									+ " '',"
									+ " 0,"
									+ " " + tax3_price + ","
									+ " 0.00,"
									+ " " + tax3_amount + ","
									+ " 0.00,"
									+ " 0,"
									+ " 0,"
									+ " 0,"
									+ " " + ToLongDate(kknow()) + ","
									+ " '" + cart_ledger_tax_dc + "'" + ")";
							// SOP("StrSql==cart555=" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						}
						// }

					}

					stmttx.executeBatch();
					conntx.commit();

					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}

	}
	public void UpdateCartItem() throws SQLException {
		try {
			// SOP("===========UpdateCartItem==========");
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			double cart_truckspace = 0.00;
			double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
			double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
			double cart_amount_tax = 0.00;

			cart_amount_tax = (cart_item_price + Double.parseDouble(configitems_total)) - cart_disc;
			// SOP("cart_item_price==="+cart_item_price);
			// SOP("cart_disc==="+cart_disc);
			// SOP("cart_amount_tax==="+cart_amount_tax);
			tax1_price = ((cart_amount_tax) * Double.parseDouble(tax_rate1) / 100);
			// SOP("tax1_price==="+tax1_price);
			tax1_amount = tax1_price * cart_qty;

			if (price_tax2_after_tax1.equals("1") && tax1_amount != 0.0) {
				tax2_price = ((cart_amount_tax + tax1_price) * Double.parseDouble(tax_rate2) / 100);
				tax2_amount = tax2_price * cart_qty;
			} else {
				tax2_price = ((cart_amount_tax) * Double.parseDouble(tax_rate2) / 100);
				tax2_amount = tax2_price * cart_qty;
			}
			if (price_tax3_after_tax2.equals("1") && tax1_amount != 0.0 && tax2_amount != 0.0) {
				tax3_price = ((cart_amount_tax + tax1_price + tax2_price) * Double.parseDouble(tax_rate3) / 100);
				tax3_amount = tax3_price * cart_qty;
			} else {
				tax3_price = ((cart_amount_tax) * Double.parseDouble(tax_rate3) / 100);
				tax3_amount = tax3_price * cart_qty;
			}
			// SOP("tax1_amount==="+tax1_amount);
			// SOP("tax2_amount==="+tax2_amount);
			// SOP("tax3_amount==="+tax3_amount);

			// truck space calculation
			if (Double.parseDouble(boxtype_size) != 0.0) {
				cart_truckspace = ((cart_alt_qty / Double.parseDouble(boxtype_size)) * 100);
			}

			StrSql = "SELECT COALESCE(cart_rowcount, 0) AS cart_rowcount," + " COALESCE(cart_option_id, 0) AS cart_option_id" + " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = "
					+ cart_id;
			// SOP("StrSql==rowcount=="+StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				cart_rowcount = crs1.getString("cart_rowcount");
				cart_option_id = crs1.getString("cart_option_id");
			}
			crs1.close();
			// cart_price = cart_item_price;
			// truck space calculation
			// SOP("cart_rowcount====" + cart_rowcount);
			// SOP("cart_option_id====" + cart_option_id);
			if (!cart_rowcount.equals("0") && cart_option_id.equals("0")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart"
						+ " SET  cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + cart_price + ","
						+ " cart_qty = " + cart_qty + ","
						+ " cart_alt_qty = " + cart_alt_qty + ","
						+ " cart_alt_uom_id = " + cart_alt_uom_id + ","
						+ " cart_truckspace = " + cart_truckspace + ","
						+ " cart_netprice = " + cart_netprice + ","
						+ " cart_amount = " + cart_amount + ","
						+ " cart_discountamount = " + cart_disc_amount + ","
						+ " cart_taxamount = " + (tax1_amount + tax2_amount + tax3_amount) + ","
						+ " cart_time = " + ToLongDate(kknow()) + ""
						+ " WHERE 1=1 "
						+ " AND cart_rowcount = " + cart_rowcount + ""
						+ " AND cart_option_id = " + cart_option_id + ""
						+ " AND cart_discount = 0"
						+ " AND cart_tax = 0"
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==item price="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			if (cart_disc != 0.0 || cart_disc == 0.0) {
				String temp_cart_discount = ExecuteQuery("SELECT cart_discount from " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_option_id = " + cart_rowcount + ""
						+ " AND cart_discount = 1 AND cart_tax = 0");
				// SOP("temp_cart_discount==="+temp_cart_discount);
				if (!CNumeric(temp_cart_discount).equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart" + " SET" + " cart_location_id= " + location_id + "," + " cart_item_id = " + cart_item_id + "," + " cart_discount_perc = "
							+ cart_discpercent + ","
							+ " cart_price = " + cart_disc + "," + " cart_amount = " + cart_disc_amount + "," + " cart_time = " + ToLongDate(kknow()) + "" + " WHERE 1=1" + " AND cart_discount = 1"
							+ " AND cart_option_id = " + cart_rowcount + " AND cart_vouchertype_id = " + vouchertype_id + " AND cart_emp_id = " + emp_id + " AND cart_session_id = " + cart_session_id;
					// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				} else {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart" + " (cart_customer_id," + " cart_vouchertype_id," + " cart_emp_id," + " cart_session_id," + " cart_location_id,"
							+ " cart_item_id,"
							+ " cart_discount," + " cart_tax," + " cart_tax_id," + " cart_rowcount," + " cart_option_id," + " cart_option_group," + " cart_item_serial," + " cart_item_batch_id,"
							+ " cart_price," + " cart_qty," + " cart_amount," + " cart_unit_cost," + " cart_alt_qty," + " cart_alt_uom_id," + " cart_convfactor," + " cart_time," + " cart_dc)"
							+ " VALUES"
							+ " (" + price_discount1_customer_id + ","
							+ " " + vouchertype_id + ","
							+ " " + emp_id + ","
							+ " " + cart_session_id + ","
							+ " " + location_id + ","
							+ " " + cart_item_id + ","
							+ " '1'," + " '0'," + " 0,"
							+ " " + cart_rowcount + ","
							+ " " + cart_rowcount + ","
							+ " ''," + " ''," + " 0,"
							+ " " + cart_disc + ","
							+ " 0,"
							+ " " + cart_disc_amount + ","
							+ " 0.00," + " 0," + " 0," + " 0," + " " + ToLongDate(kknow()) + "," + " '" + cart_cust_disc_dc + "'" + ")";
					// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
			}
			if (tax1_amount != 0.0 || tax1_amount == 0.0) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart" + " SET" + " cart_location_id= " + location_id + "," + " cart_item_id = " + cart_item_id + "," + " cart_price = " + tax1_price
						+ "," + " cart_amount = "
						+ tax1_amount + "," + " cart_time = " + ToLongDate(kknow()) + "" + " WHERE 1=1" + " AND cart_option_id = " + cart_rowcount + "" + " AND cart_tax = 1" + " AND cart_tax_id = "
						+ tax_id1 + " AND cart_option_id = " + cart_rowcount + " AND cart_vouchertype_id = " + vouchertype_id + " AND cart_emp_id = " + emp_id + " AND cart_session_id = "
						+ cart_session_id;
				// SOP("StrSql==tax1="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax2_amount != 0.0 || tax2_amount == 0.0) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart" + " SET" + " cart_location_id= " + location_id + "," + " cart_item_id = " + cart_item_id + "," + " cart_price = " + tax2_price
						+ "," + " cart_amount = "
						+ tax2_amount + "," + " cart_time = " + ToLongDate(kknow()) + "" + " WHERE 1=1" + " AND cart_option_id = " + cart_rowcount + "" + " AND cart_tax = 1" + " AND cart_tax_id = "
						+ tax_id2 + " AND cart_option_id = " + cart_rowcount + " AND cart_vouchertype_id = " + vouchertype_id + " AND cart_emp_id = " + emp_id + " AND cart_session_id = "
						+ cart_session_id;
				// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax3_amount != 0.0 || tax3_amount == 0.0) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart" + " SET" + " cart_location_id= " + location_id + "," + " cart_item_id = " + cart_item_id + "," + " cart_price = " + tax3_price
						+ "," + " cart_amount = "
						+ tax3_amount + "," + " cart_time = " + ToLongDate(kknow()) + "" + " WHERE 1=1" + " AND cart_option_id = " + cart_rowcount + "" + " AND cart_tax = 1" + " AND cart_tax_id = "
						+ tax_id3 + " AND cart_option_id = " + cart_rowcount + " AND cart_vouchertype_id = " + vouchertype_id + " AND cart_emp_id = " + emp_id + " AND cart_session_id = "
						+ cart_session_id;
				// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();
			conntx.commit();
			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);// Enables auto commit
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void DeleteCartItem() throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			cart_option_id = ExecuteQuery("SELECT cart_rowcount" + " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id + " AND cart_vouchertype_id = " + vouchertype_id);
			// SOP("cart_id===" + cart_id);
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_option_id = " + cart_option_id + "" + " AND cart_session_id = " + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			stmttx.addBatch(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id + "" + " AND cart_session_id = " + cart_session_id + " AND cart_vouchertype_id = "
					+ vouchertype_id;

			// SOP("DeleteCartItem===" + StrSql);
			stmttx.addBatch(StrSql);

			stmttx.executeBatch();

			conntx.commit();
			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);// Enables auto commit
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}

	}

	public void DeleteCartAllItems() throws SQLException {
		try {

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1" + " AND cart_emp_id =" + emp_id + " AND cart_session_id =" + cart_session_id + " AND cart_vouchertype_id = "
					+ vouchertype_id;
			// SOP("StrSql==DeleteCartAllItems=="+StrSql);
			updateQuery(StrSql);

			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception e) {

			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}

	}

	public String getItemDetails(String location_id, String cart_item_id, String rateclass_id, String emp_id, String cart_session_id, String mode) {
		int count = 0, price_disc_type = 0;
		int para = 0;
		double tax_value1 = 0.00, tax_value2 = 0.00, tax_value3 = 0.00;
		String item_serial = "0";
		String item_batchno = "0";
		String optioncount = "0";
		double tax1_rate1 = 0.00, tax2_rate2 = 0.00, tax3_rate3 = 0.00;
		double tax1_customer_id1 = 0.00, tax2_customer_id2 = 0.00, tax3_customer_id3 = 0.00;
		double item_unit_price = 0.00, itemprice = 0.00, discount = 0.00, quantity = 0.00;
		double cart_nettotal = 0.00;
		double cart_total = 0.00;
		String strconfigoptions = "";
		double temp_item_unit_price = 0.0;
		double temp_item_discount = 0.0;
		String price_tax2_after_tax1 = "0";
		String price_tax3_after_tax2 = "0";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name, item_code, uom_id, uom_ratio, item_uom_id,"
					+ " price_amt, price_variable,"
					+ " COALESCE(item_sales_ledger_id,'0') AS price_sales_customer_id,"
					+ " price_disc, price_disc_type,"
					+ " COALESCE(item_salesdiscount_ledger_id ,'0') AS price_discount1_customer_id,";

			if (gst_type.equals("state")) {
				StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax_customer_id1,"
						+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax_rate1,"
						+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax_customer_id2,"
						+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax_rate2,"
						+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
						+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
						+ " COALESCE(tax1.customer_id, 0) AS tax_id1,"
						+ " COALESCE(tax2.customer_id, 0) AS tax_id2,"
						+ " COALESCE(item_salestax2_aftertax1, 0) AS price_tax2_after_tax1,";

				StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id3,"
						+ " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate3,";

				StrSql += " COALESCE(tax4.customer_name, '') AS tax3_name,"
						+ " COALESCE(tax4.customer_id, 0) AS tax_id3,";
			}
			else if (gst_type.equals("central")) {
				StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax_customer_id1,"
						+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax_rate1,"
						+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
						+ " COALESCE(tax3.customer_id, 0) AS tax_id1,";
				StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id2,"
						+ " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate2,";

				StrSql += " COALESCE(tax4.customer_name, '') AS tax2_name,"
						+ " COALESCE(tax4.customer_id, 0) AS tax_id2,";
				StrSql += " '0' AS tax_customer_id3,"
						+ " '0' AS tax_rate3,";

				StrSql += " '' AS tax3_name,"
						+ " '0' AS tax_id3,";

			}

			StrSql += " COALESCE(item_serial, '') AS item_serial,"
					+ " COALESCE(item_ticket_dept_id, 0) AS item_ticket_dept_id,"
					+ " IF(item_nonstock = 1, COALESCE(stock_current_qty, 0), '-') AS stock_current_qty, "
					+ " emp_invoice_priceupdate, emp_invoice_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom on uom_id = item_uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id";

			if (gst_type.equals("state")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id = item_salestax1_ledger_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id = item_salestax2_ledger_id";
			} else if (gst_type.equals("central")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = item_salestax3_ledger_id";
			}
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax4 on tax4.customer_id = item_salestax4_ledger_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stockserial on stockserial_item_id = item_id"
					+ " AND stock_location_id = " + location_id + ""
					+ " , " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1";
			if (!mode.equals("update")) {
				StrSql += " AND item_active = 1";
			}
			StrSql += " AND item_id=" + cart_item_id + ""
					+ " AND price_id = (SELECT price_id FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE 1=1 "
					+ " AND COALESCE(item_sales_ledger_id,'0') != 0" // this condition will retrive sales price tax
					+ " AND price_item_id = item_id"
					+ " AND price_rateclass_id	 = " + rateclass_id + ""
					+ " AND price_effective_from <= " + ToLongDate(kknow())
					+ " AND price_active = 1"
					+ " ORDER BY price_effective_from"
					+ " DESC LIMIT 1)"
					+ " AND emp_id=" + emp_id
					+ " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 6";

			// SOP("StrSql==getItemDetails=" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("<div class=\"table table-bordered\" style=\"overflow-x:scroll;width:580px\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n<tr align=center>\n");
				// Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" id=\"getItemDetails\" class=\"listtable footable\">\n");
				Str.append("<tr>\n");
				Str.append("<th align=\"center\" colspan=\"6\"><div style=\"display:inline\" id=\"item_name\"> ");
				if (!crs.getString("item_code").equals("")) {
					Str.append("(" + crs.getString("item_code") + ") ");
				}
				Str.append(crs.getString("item_name")).append("</div>\n");
				Str.append("</th>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align=\"center\" valign=\"top\">Quantity</td>\n");
				Str.append("<td align=\"center\" valign=\"top\">UOM</td>\n");
				Str.append("<td align=\"center\" valign=\"top\">Price</td>\n");
				Str.append("<td align=\"center\" valign=\"top\">Discount</td>\n");
				Str.append("<td align=\"center\" valign=\"top\" nowrap>Tax</td>\n");
				Str.append("<td align=\"center\" valign=\"top\"><b>Total</b></td>\n");
				Str.append("</thead>");
				Str.append("</tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;

					tax1_rate1 = crs.getDouble("tax_rate1");
					tax2_rate2 = crs.getDouble("tax_rate2");
					tax3_rate3 = crs.getDouble("tax_rate3");
					tax1_customer_id1 = crs.getDouble("tax_customer_id1");
					tax2_customer_id2 = crs.getDouble("tax_customer_id2");
					tax3_customer_id3 = crs.getDouble("tax_customer_id3");
					// price_tax2_after_tax1 = crs.getString("price_tax2_after_tax1");
					// price_tax3_after_tax2 = crs.getString("price_tax3_after_tax2");
					price_tax2_after_tax1 = "0";
					price_tax3_after_tax2 = "0";

					if (mode.equals("add")) {
						cart_alt_uom_id = crs.getString("item_uom_id");
						uom_ratio = ExecuteQuery("SELECT COALESCE(uom_ratio, 0) AS uom_ratio"
								+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
								+ " WHERE uom_id = " + CNumeric(cart_alt_uom_id) + "");
						uom_ratio = CNumeric(uom_ratio);
						if (!uom_ratio.equals("0")) {
							cart_uom_ratio = Double.parseDouble(uom_ratio);
						}
						quantity = cart_alt_qty * cart_uom_ratio;
						item_unit_price = crs.getDouble("price_amt");
						// SOP("item_unit_price===1111===" + item_unit_price);
						itemprice = (item_unit_price * quantity);
						price_disc_type = crs.getInt("price_disc_type");

					} else if (mode.equals("update")) {
						quantity = cart_alt_qty * cart_uom_ratio;
						item_unit_price = cart_item_price;
						itemprice = item_unit_price * quantity;
						// SOP("itemprice==="+itemprice);
						price_disc_type = crs.getInt("price_disc_type");
						discount = cart_disc;

					}

					cart_nettotal = (item_unit_price - discount) * quantity;
					if (tax1_rate1 != 0.00) {
						tax_value1 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax1_rate1 / 100));
					}

					if (tax2_rate2 != 0.00) {
						if ((price_tax2_after_tax1.equals("1")) && (tax_value1 != 0)) {
							tax_value2 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1) * (tax2_rate2 / 100));
						} else {
							tax_value2 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax2_rate2 / 100));
						}
					}

					if (tax3_rate3 != 0.00) {
						if ((price_tax3_after_tax2.equals("1")) && (tax_value1 != 0) && (tax_value2 != 0)) {
							tax_value3 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1 + tax_value2) * (tax3_rate3 / 100));
						} else {
							tax_value3 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax3_rate3 / 100));
						}
					}

					// Total amount for No. Of Qty's.
					cart_total = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1 + tax_value2 + tax_value3) * cart_alt_qty);

					if (voucherclass_id.equals("101") || voucherclass_id.equals("114")) {
						item_serial = "0";
						item_batchno = "0";
					}

					Str.append("<tr>\n");

					Str.append("<input name=\"uom_ratio\" type=\"hidden\" id=\"uom_ratio\" ").append("value=\"").append(cart_uom_ratio).append("\"/>\n");
					Str.append("<td align=\"left\" valign=\"top\">");
					if (item_serial.equals("1")) {
						Str.append("<div align=center>1</div>");
						Str.append("<input name=\"txt_item_qty\" type=\"hidden\" id=\"txt_item_qty\" ").append("value=\"").append(df.format(cart_alt_qty)).append("\"/>\n");
					} else {
						Str.append("<input name=\"txt_item_qty\" type=\"text\" class=\"form-control\" id=\"txt_item_qty\" ").append("value=\"");
						if (mode.equals("add")) {
							Str.append("");
						} else if (mode.equals("update")) {
							Str.append(df.format(cart_alt_qty));
						}
						Str.append("\" size=\"10\" maxlength=\"10\" onKeyUp=\"toNumber('txt_item_qty','Qty');CalItemTotal(" + para + ");\" />\n");
					}
					Str.append("<input name=\"txt_item_id\" type=\"hidden\" id=\"txt_item_id\" ").append("value=\"").append(crs.getString("item_id")).append("\"/>\n");
					Str.append("<input name=\"txt_boxtype_size\" type=\"hidden\" id=\"txt_boxtype_size\" ").append("value=\"").append(boxtype_size).append("\"/>\n");
					Str.append("<input name=\"txt_stock_qty\" type=\"hidden\" id=\"txt_stock_qty\" ").append("value=\"").append(crs.getString("stock_current_qty")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_baseprice\" name=\"txt_item_baseprice\" ").append("value=\"").append(item_unit_price).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_itemprice_updatemode\" name=\"txt_itemprice_updatemode\" ").append("value=\"").append(item_unit_price).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_pricevariable\" name=\"txt_item_pricevariable\" ").append("value=\"").append(crs.getString("price_variable")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_optioncount\" name=\"txt_optioncount\" ").append("value=\"").append(optioncount).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_ticket_dept_id\" name=\"txt_item_ticket_dept_id\" ").append("value=\"").append(crs.getString("item_ticket_dept_id"))
							.append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_price_sales_customer_id\" name=\"txt_item_price_sales_customer_id\" ").append("value=\"")
							.append(crs.getString("price_sales_customer_id")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_price_discount1_customer_id\" name=\"txt_item_price_discount1_customer_id\" ").append("value=\"")
							.append(crs.getString("price_discount1_customer_id")).append("\"/>\n");
					if (crs.getString("price_disc_type").equals("1")) {
						Str.append("<input type=\"hidden\" id=\"txt_item_price_dic\" name=\"txt_item_price_dic\" ").append("value=\"").append(crs.getString("price_disc")).append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_item_price_dic_per\" name=\"txt_item_price_dic_per\" ").append("value=\"")
								.append((crs.getDouble("price_disc") / crs.getDouble("price_amt")) * 100).append("\"/>\n");
					} else {
						Str.append("<input type=\"hidden\" id=\"txt_item_price_dic\" name=\"txt_item_price_dic\" ").append("value=\"")
								.append((crs.getDouble("price_disc") * crs.getDouble("price_amt")) / 100).append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_item_price_dic_per\" name=\"txt_item_price_dic_per\" ").append("value=\"").append(crs.getDouble("price_disc")).append("\"/>\n");
					}
					Str.append("<input type=\"hidden\" id=\"txt_item_price_disc_type\" name=\"txt_item_price_disc_type\" ").append("value=\"").append(price_disc_type).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_emp_invoice_discountupdate\" name=\"txt_emp_invoice_discountupdate\" ").append("value=\"")
							.append(crs.getString("emp_invoice_discountupdate")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_serial\" name=\"txt_serial\"  ").append("value=\"").append(item_serial).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_batch\" name=\"txt_batch\"  ").append("value=\"").append(item_batchno).append("\"/>\n");

					Str.append("</td><td align=\"left\" valign=\"top\">");
					if (!CNumeric(cart_alt_uom_id).equals("0")) {
						Str.append(PopulateUOM(crs.getString("uom_id"), cart_alt_uom_id));
					}

					if (cart_alt_qty != 0.0) {
						temp_item_unit_price = (itemprice / cart_alt_qty);
						temp_item_discount = (discount / cart_alt_qty);
					}
					// SOP("temp_item_unit_price===" + temp_item_unit_price);
					Str.append("</td><td align=\"left\" width='20%' valign=\"top\">");
					if (crs.getString("emp_invoice_priceupdate").equals("1") || crs.getString("price_variable").equals("1")) {
						Str.append("<input name=\"txt_item_price\" style=\"width:90px\" type=\"text\" class=\"form-control\" id=\"txt_item_price\" ")
								.append("value=\"")
								.append(df.format(temp_item_unit_price))
								.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price','Price');CalAmount(" + para + ");\" onChange=\"CheckBasePrice();CalItemTotal(" + para
										+ ");\" />");
					} else {
						Str.append("<input name=\"txt_item_price\" type=\"text\" class=\"form-control\" id=\"txt_item_price\" ").append("value=\"").append(df.format(temp_item_unit_price))
								.append("\" size=\"10\" maxlength=\"10\" disabled />");
					}
					Str.append("</td>\n<td align=\"left\" width=\"25%\" valign=\"top\">");
					if (price_disc_type == 1) {

						Str.append("Amount:<input name=\"txt_item_price_disc\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc\" ").append("value=\"")
								.append(df.format((temp_item_discount)))
								.append("\" size=\"10\" maxlength=\"10\" onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\">")
								.append(" <div style=\"display:inline\"></div><br/>");
						Str.append("%:<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
						Str.append("value=\"");
						Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
						Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount(" + para + ");CalItemTotal(" + para + ");\"/>")
								.append(" <div style=\"display:inline\"></div>");
					} else {

						Str.append("Amount:<input name=\"txt_item_price_disc\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc\" ").append("value=\"")
								.append(df.format((temp_item_discount)))
								.append("\" size=\"10\" maxlength=\"10\" onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\"/>")
								.append(" <div style=\"display:inline\"></div><br/>");
						Str.append("%:<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
						Str.append("value=\"");
						Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
						Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount(" + para + ");CalItemTotal(" + para + ");\"/>")
								.append(" <div style=\"display:inline\"></div>");
					}

					Str.append("</td>\n<td align=\"left\" width = \"25%\" valign=\"top\" nowrap>");
					if (crs.getString("tax1_name").equals("") && crs.getString("tax2_name").equals("") && crs.getString("tax3_name").equals("")) {
						Str.append("0.00").append("");
					}
					if (!crs.getString("tax1_name").equals("")) {
						Str.append(crs.getString("tax1_name")).append(":<br>");
						Str.append("<input name=\"txt_item_tax1\" type=\"text\" class=\"form-element\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
								.append(df.format(tax_value1)).append("\"/>\n");
						Str.append("<br/>");
					}
					if (!crs.getString("tax2_name").equals("")) {
						Str.append(crs.getString("tax2_name")).append(":<br>");
						Str.append("<input name=\"txt_item_tax2\" type=\"text\" class=\"form-element\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
								.append(df.format(tax_value2)).append("\"/>\n");
						Str.append("<br/>");
					}
					if (!crs.getString("tax3_name").equals("")) {
						Str.append(crs.getString("tax3_name")).append(":<br>");
						Str.append("<input name=\"txt_item_tax3\" type=\"text\" class=\"form-element\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
								.append(df.format(tax_value3)).append("\"/>\n");
					}
					Str.append("<input name=\"txt_item_price_tax_rate1\" type=\"hidden\" id=\"txt_item_price_tax_rate1\" ").append(" value=\" ").append(tax1_rate1).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id1\" type=\"hidden\" id=\"txt_item_price_tax_customer_id1\" ").append("value=\"").append(tax1_customer_id1).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id1\" type=\"hidden\" id=\"txt_item_price_tax_id1\" ").append("value=\"").append(crs.getInt("tax_id1")).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_rate2\" type=\"hidden\" id=\"txt_item_price_tax_rate2\" ").append("value=\"").append(tax2_rate2).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id2\" type=\"hidden\" id=\"txt_item_price_tax_customer_id2\" ").append("value=\"").append(tax2_customer_id2).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id2\" type=\"hidden\" id=\"txt_item_price_tax_id2\" ").append("value=\"").append(crs.getInt("tax_id2")).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_rate3\" type=\"hidden\" id=\"txt_item_price_tax_rate3\" ").append("value=\"").append(tax3_rate3).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id3\" type=\"hidden\" id=\"txt_item_price_tax_customer_id3\" ").append("value=\"").append(tax3_customer_id3).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id3\" type=\"hidden\" id=\"txt_item_price_tax_id3\" ").append("value=\"").append(crs.getInt("tax_id3")).append("\"/>\n");
					Str.append("<input name=\"txt_price_tax2_after_tax1\" type=\"hidden\" id=\"txt_price_tax2_after_tax1\" ").append("value=\"").append("0")
							.append("\"/>\n");
					Str.append("<input name=\"txt_price_tax3_after_tax2\" type=\"hidden\" id=\"txt_price_tax3_after_tax2\" ").append("value=\"").append("0")
							.append("\"/>\n");
					Str.append("<input name=\"txt_item_stockserial\" type=\"hidden\" id=\"txt_item_stockserial\" ").append("value=\"").append(stockserial_serial_no).append("\"/>\n");
					Str.append("</td>\n");
					Str.append("<td align=right valign=top><b>");
					Str.append("<div id=\"item_total\">").append(df.format(cart_total)).append("</div>\n");
					Str.append("</b></td>\n");
					Str.append("</tr>");
					if (item_serial.equals("1")) {
						if (!CNumeric(stockserial_serial_no).equals("0")) {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" valign=\"top\" nowrap>Serial No.<font color=\"#ff0000\">*</font>:</td>\n");
							Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_serial\" type=\"text\" class=\"textbox\" id=\"txt_item_serial\" value="
									+ stockserial_serial_no + " size=\"20\" maxlength=\"30\" /></td>\n");
							Str.append("</tr>");
						} else {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" valign=\"top\" nowrap>Serial No.<font color=\"#ff0000\">*</font>:</td>\n");
							Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_serial\" type=\"text\" class=\"textbox\" id=\"txt_item_serial\" size=\"20\" maxlength=\"30\" /></td>\n");
							Str.append("</tr>");
						}
					}

					if (item_batchno.equals("1")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"right\" valign=\"top\" nowrap>Batch No.<font color=\"#ff0000\">*</font>:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_batch\" type=\"text\" class=\"textbox\" id=\"txt_item_batch\" size=\"20\" maxlength=\"30\" /></td>\n");
						Str.append("</tr>");
					}
					Str.append("<tr>\n");
					Str.append("<td colspan=\"6\"  valign=\"top\">");
					if (vouchertype_id.equals("114")) {
						Str.append("<span style=\"float:left\"><input name=\"stock_location_button\" id=\"stock_location_button\" type=\"button\" class=\"button\" value=\"All Location\"  onClick=\"showlocstockstatus('"
								+ branch_id + "','" + cart_item_id + "');\" /></span>\n");
					}

					if (mode.equals("add")) {
						Str.append("<center>"
								+ "<input name=\"add_button\" id=\"add_button\" type=\"button\" class=\"btn btn-success\" value=\"Add\"  onClick=\"AddCartItem();\" /></center>\n");
					} else if (mode.equals("update")) {
						Str.append("<center><input name=\"update_button\" id=\"update_button\" type=\"button\" class=\"btn btn-success\" value=\"Update\"  onClick=\"UpdateCartItem();\" /></center>\n");
					}
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</table>");
				Str.append("</div>");

			}
			crs.close();
			// Str.append(strconfigoptions);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String GetConfigurationDetails() {
		String group = "", checked = "", disabled = "";
		int groupitemcount = 0, groupcount = 0;
		double defaultselected_total = 0;
		double all_selected_total = 0, multi_select_basetotal = 0;
		StringBuilder Str = new StringBuilder();
		optiontotal = 0;
		try {

			StrSql = "SELECT item_id, item_name, group_type, group_name, COALESCE(pricetrans_amt, 0) AS pricetrans_amt,"
					+ " opt.option_group_id, item_serial, item_batchno, opt.option_qty, item_code, opt.option_id,"
					+ " opt.option_select, item_small_desc, group_id, (SELECT COUNT(DISTINCT optitem.option_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option optitem"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = optitem.option_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = optitem.option_itemmaster_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_item_id = item_id" + " AND pricetrans_price_id = price_id"
					+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id" + " AND optitem.option_group_id = group_id" + " GROUP BY optitem.option_group_id) AS groupitemcount"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option opt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = option_itemmaster_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_item_id = item_id"
					+ " AND pricetrans_price_id = price_id"
					+ " WHERE option_itemmaster_id = " + cart_item_id + ""
					+ " GROUP BY group_name, group_type, item_id"
					+ " ORDER BY group_rank, group_name DESC";
			// SOP("StrSql =accinv=config details== ==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable footable\">\n");
				Str.append("<thead>\n<tr align=\"center\">\n");
				Str.append("<th>Select</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>Item</th>\n");
				Str.append("<th>Qty</th>\n");
				Str.append("<th data-hide=\"phone\">Price</th>\n");
				Str.append("</tr>\n</thead>\n");
				while (crs.next()) {
					checked = "";
					disabled = "";
					if (!group.equals(crs.getString("group_name")) && !group.equals("")) {
						groupitemcount = 0;
					}

					if (!group.equals(crs.getString("group_name"))) {
						groupcount++;
						Str.append("<tr align=\"center\">\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td align=\"center\"><b>");

						if (mode.equals("update")) {
							if (crs.getString("group_type").equals("1")) {
								Str.append(crs.getString("cart_option_group")).append(" (Default)");
							} else {
								Str.append(crs.getString("cart_option_group"));
							}
						} else if (crs.getString("group_type").equals("1")) {
							Str.append(crs.getString("group_name")).append(" (Default)");
						} else {

							Str.append(crs.getString("group_name"));
						}
						Str.append("</b><input type=\"hidden\" id=\"txt_").append(groupcount).append("_count\" value=\"").append(crs.getString("groupitemcount")).append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_value\" name=\"txt_").append(groupcount).append("_value\" >\n");
						Str.append("<input type=\"hidden\" id=\"txt_new_value\" name=\"txt_new_value\" >\n");
						Str.append("<input type=\"hidden\" id=\"txt_optnetprice\" name=\"txt_optnetprice\" >\n");
						Str.append("</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n</tr>\n");
					}
					groupitemcount++;
					Str.append("<tr>\n");

					if (crs.getString("option_select").equals("1")) {
						checked = "checked";
						disabled = "disabled";
						defaultselected_total += crs.getDouble("option_qty") * crs.getDouble("pricetrans_amt");
					}

					if (crs.getString("group_type").equals("2")) {
						disabled = "disabled";
						all_selected_total += crs.getDouble("pricetrans_amt");
					}

					if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
						multi_select_basetotal += crs.getDouble("pricetrans_amt");
					}
					Str.append("<td valign=\"top\" align=\"center\">\n");
					Str.append("<input type=\"checkbox\" id=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" name=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" value=\"").append(crs.getString("pricetrans_amt")).append("\" ").append(checked).append(" ").append(disabled);
					if (crs.getString("group_type").equals("1")) {
						// function for Default

						Str.append(" onclick=\"CalculateDefault(").append(crs.getString("group_id")).append(",");
						Str.append(crs.getString("groupitemcount")).append(",this.value,").append(groupitemcount).append(",").append(groupcount).append(");\"/>\n");
						if (crs.getString("option_select").equals("1")) {
							Str.append("<input type=\"hidden\" id=\"txt_").append(crs.getString("group_id"));
							Str.append("_basevalue\" name=\"txt_").append(crs.getString("group_id")).append("_basevalue\" value=\"");
							Str.append(crs.getString("pricetrans_amt")).append("\"/>\n");
						}
					} else if (crs.getString("group_type").equals("2")) { // function for Multi Select
						Str.append("/>\n");
					} else if (crs.getString("group_type").equals("3")) {
						Str.append(" onclick=\"CalculateMultiSelect(this.id,this.value,").append(crs.getString("groupitemcount")).append(",");
						Str.append(crs.getString("group_id")).append(",").append(groupcount).append(");\"/>\n");
					}

					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("item_name"));
					if (!crs.getString("item_code").equals("")) {
						Str.append(" (").append(crs.getString("item_code")).append(")");
					}

					if (!crs.getString("item_small_desc").equals("")) {
						Str.append(" <br>").append(crs.getString("item_small_desc"));
					}

					Str.append("</td>\n<td valign=\"top\" align=\"right\">\n");
					Str.append(crs.getString("option_qty"));
					Str.append("</td>\n<td valign=\"top\" align=\"right\">\n");
					Str.append(crs.getString("pricetrans_amt"));
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_gpname\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_gpname\" value=\"");
					Str.append(crs.getString("group_name")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_id\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_id\" value=\"");
					Str.append(crs.getString("item_id")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_qty\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_qty\" value=\"");
					Str.append(crs.getString("option_qty")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_serial\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_serial\" value=\"");
					Str.append(crs.getString("item_serial")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_batch\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_batch\" value=\"");
					Str.append(crs.getString("item_batchno")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_price\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_price\" value=\"");
					Str.append(crs.getString("pricetrans_amt")).append("\"/>\n");
					Str.append("</td>\n</tr>\n");
					group = crs.getString("group_name");
				}
				Str.append("<tr>\n<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td><input type=\"hidden\" id=\"txt_group_count\" name=\"txt_group_count\" value=\"").append(groupcount).append("\"/>\n");
				Str.append("<input type=\"hidden\" id=\"txt_multiselect_basetotal\" name=\"txt_multiselect_basetotal\" value=\"").append(multi_select_basetotal).append("\"/>\n");
				Str.append("<input type=\"hidden\" id=\"txt_allselected_total\" name=\"txt_allselected_total\" value=\"").append(all_selected_total).append("\"/>\n");
				Str.append("</td>\n</tr>\n</table>\n");
			}
			crs.close();
			Str.append("<input type=\"hidden\" id=\"txt_defaultselected_total\" name\"txt_defaultselected_total\" value=\"").append(defaultselected_total).append("\"/>\n");
			optiontotal = defaultselected_total;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListCartItems(String emp_id, String cart_session_id, String vouchertype_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs1 = null;
		double invoice_grandtotal = 0.00, invoice_total = 0.00, total = 0.00, invoice_optnetamt = 0.00, cart_alt_qty = 0.00, uom_ratio = 0.00;
		double item_price = 0.00, item_unit_price = 0.00;
		double discount = 0.00;
		double discpercent = 0.00;
		double quantity = 0.00;
		double pricetax = 0.00;
		double invoice_qty = 0.00;
		double mainitemamt = 0.00;
		double total_discount = 0.00;
		double total_tax = 0.00;
		int count = 0;
		String voucher_id = "0";
		String itemtype_name = "";

		// for round off
		String vouchertype_roundoff = "0";

		StrSql = "SELECT cart.cart_id, cart.cart_rowcount, cart.cart_voucher_id AS cart_voucher_id, vouchertype_roundoff,"
				+ " item_id,"
				+ " COALESCE(IF(uom_name!='',uom_name, 'Each'),'') AS uom_name,"
				// + " uom_name," + " boxtype_size,"
				+ " item_name," + " item_code, cart.cart_qty, cart.cart_alt_qty, cart.cart_alt_uom_id, cart.cart_truckspace," + " cart.cart_price, cart.cart_netprice, cart.cart_amount,"
				+ " COALESCE((SELECT sum(disc.cart_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart disc"
				+ " WHERE disc.cart_option_id = cart.cart_rowcount AND disc.cart_discount = 1"
				+ " AND disc.cart_vouchertype_id = " + vouchertype_id
				+ " AND disc.cart_session_id = " + cart_session_id + ""
				+ " AND disc.cart_emp_id = " + emp_id + ""
				+ " GROUP BY disc.cart_rowcount), 0) AS discount,"
				// //discount_perc
				+ " COALESCE((SELECT discount_perc.cart_discount_perc"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart discount_perc"
				+ " WHERE discount_perc.cart_option_id = cart.cart_rowcount AND discount_perc.cart_discount = 1"
				+ " AND discount_perc.cart_vouchertype_id = " + vouchertype_id
				+ " AND discount_perc.cart_session_id = " + cart_session_id + ""
				+ " AND discount_perc.cart_emp_id = " + emp_id + ""
				+ " GROUP BY discount_perc.cart_rowcount), 0) AS discount_perc,"
				+ " COALESCE((SELECT sum(tax.cart_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart tax"
				+ " WHERE tax.cart_option_id = cart.cart_rowcount AND tax.cart_tax = 1"
				+ " AND tax.cart_vouchertype_id = " + vouchertype_id + " "
				+ " AND tax.cart_session_id = " + cart_session_id + " "
				+ " AND tax.cart_emp_id = " + emp_id + " "
				+ " GROUP BY tax.cart_rowcount), 0) AS tax"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart cart"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = cart.cart_item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = cart.cart_alt_uom_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id=" + vouchertype_id
				// + " LEFT JOIN  axela_inventory_boxtype ON boxtype_id = item_boxtype_id"
				+ " WHERE 1=1"
				+ " AND cart.cart_session_id = " + cart_session_id + " "
				+ " AND cart.cart_emp_id = " + emp_id + " "
				+ " AND cart.cart_vouchertype_id =" + vouchertype_id
				+ " AND cart.cart_discount=0 " + " AND cart.cart_tax=0 " + " AND cart.cart_item_id!=0 "
				// + " AND cart.cart_alt_uom_id = "+cart_alt_uom_id+""
				+ " AND cart.cart_option_id=0";
		if (cart_voucher_so_id.equals("0")) {
			StrSql += " AND cart.cart_rowcount!=0 ";
		}

		StrSql += " GROUP BY cart.cart_id"
				+ " order BY cart.cart_id";

		// SOP("StrSql=List cart items==" + StrSqlBreaker(StrSql));

		CachedRowSet crs = processQuery(StrSql, 0);

		if (!vouchertype_id.equals("11")) {
			StrSql = "SELECT COALESCE(cart_id, 0) AS cart_id,"
					+ " COALESCE(customer_id, 0) AS tax_id,"
					+ " COALESCE(customer_name,'') AS tax_name,"
					+ " COALESCE(customer_rate, 0.00) AS tax_rate,"
					+ " COALESCE(cart_amount, 0.00) AS cart_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_rowcount = 0"
					+ " AND cart_option_id = 0"
					+ " GROUP BY cart_id"
					+ " order BY cart_id";
			// SOP("\n StrSql=List cart bs items==" + StrSqlBreaker(StrSql));
			crs1 = processQuery(StrSql, 0);
		}

		Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
		// Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"black\" class=\"listtable footable\">\n");
		try {
			if (!msg.equals("")) {
				Str.append("<thead>\n<tr>\n<td colspan=\"10\" align=\"center\"><font color=\"red\"><b>").append(msg).append("</b></font></td>\n</tr>\n</thead>\n");
			}
			Str.append("<thead>\n<tr>\n");
			Str.append("<th>#</th>\n");
			// if (cart_voucher_so_id.equals("0")) {
			Str.append("<th>X</th>\n");
			// }
			Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item</th>\n");
			Str.append("<th data-hide=\"phone\">Qty</th>\n");
			if (!vouchertype_id.equals("11")) {
				Str.append("<th data-hide=\"phone\">UOM</th>\n");
			}
			if (!vouchertype_id.equals("11")) {
				Str.append("<th data-hide=\"phone\">Price</th>\n");
			}
			Str.append("<th data-hide=\"phone\">Discount</th>\n");
			if (!vouchertype_id.equals("11")) {
				Str.append("<th data-hide=\"phone\">Tax</th>\n");
			}
			// Str.append("<th data-hide=\"phone\"></th>\n");
			Str.append("<th>Amount</th>\n");
			Str.append("</tr>\n</thead>\n");
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					vouchertype_roundoff = crs.getString("vouchertype_roundoff");
					voucher_id = crs.getString("cart_voucher_id");
					item_price = crs.getDouble("cart_price");
					cart_alt_qty = crs.getDouble("cart_alt_qty");
					quantity = crs.getDouble("cart_qty");
					uom_ratio = (quantity / cart_alt_qty);
					discount = crs.getDouble("discount") / quantity;
					discpercent = crs.getDouble("discount_perc");
					mainitemamt = (item_price) * quantity;
					invoice_qty += crs.getDouble("cart_alt_qty");
					total_discount += crs.getDouble("discount");
					total = (((item_price - discount) * quantity) + crs.getDouble("tax"));
					if (CNumeric(CheckNull(total + "")).equals(0)) {
						total = 0.0;
					}
					invoice_total += total;
					total_tax += crs.getDouble("tax");

					String item_name = "";
					if (!crs.getString("item_name").equals("")) {
						item_name += crs.getString("item_name");
					}
					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ") ";
					}

					if (!crs.getString("cart_rowcount").equals("0")) {
						++count;
					}
					Str.append("\n<tr valign=\"top\"");

					Str.append(">\n<td width=\"5%\" align=\"center\">\n");
					Str.append(count);
					Str.append("</td>\n");
					Str.append("<td align=\"center\">\n");
					Str.append("<a href=\"javascript:delete_cart_item(").append(crs.getString("cart_id")).append(");\">X</a>");
					Str.append("</td>\n");

					Str.append("<td id='" + count + "' align=\"left\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getDouble("cart_alt_qty")).append(",");
					Str.append(crs.getDouble("cart_alt_uom_id")).append(",");
					Str.append(item_price + ",");
					Str.append(crs.getDouble("discount") + ",");
					Str.append(discpercent + ",");
					Str.append(boxtype_size).append(",");
					Str.append(" 0,");
					Str.append(crs.getString("cart_id")).append(",");
					Str.append("'update',0);setColorById(this.id);\"");
					Str.append(">\n");

					Str.append(item_name);
					Str.append("</td>\n");

					Str.append("<td align=\"center\">\n");
					Str.append(crs.getDouble("cart_alt_qty"));
					Str.append("</td>");

					Str.append("<td align=\"left\">\n");
					Str.append(crs.getString("uom_name"));
					Str.append("</td>");

					Str.append("\n<td align=\"right\">\n");
					if (!cart_voucher_so_id.equals("0")) {
						Str.append(df.format(crs.getDouble("cart_price")));
					} else {
						if (crs.getDouble("cart_price") != 0.0) {
							Str.append(df.format((crs.getDouble("cart_price") * uom_ratio)));
						} else {
							Str.append("0.00");
						}
					}
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("discount")));
					Str.append("</td>\n");
					if (!vouchertype_id.equals("11")) {
						Str.append("<td align=\"right\">\n");
						Str.append(df.format(crs.getDouble("tax")));
						Str.append("</td>\n");
					}
					Str.append("<td align=\"right\">\n");
					if (crs.getDouble("cart_price") != 0.0) {
						Str.append(df.format(total));
					} else {
						Str.append("0.00");
					}
					Str.append("</td>\n</tr>\n");
				}
				updatecount += count;
			}

			Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>Total:</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"");
			Str.append(invoice_qty).append("\"><b>").append(invoice_qty).append("</b></td>\n");

			if (!cart_voucher_so_id.equals("0")) {
				Str.append("<td align=\"right\">&nbsp;</td>\n");
				Str.append("<td align=\"right\">&nbsp;</td>\n");
			} else {
				Str.append("<td align=\"right\">&nbsp;</td>\n");
				Str.append("<td align=\"right\">&nbsp;</td>\n");
			}

			Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_discount)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_tax)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\">\n");
			Str.append("<input type=\"hidden\" name=\"txt_updatecount\" id=\"txt_updatecount\" value=\"");
			Str.append(updatecount).append("\">");
			Str.append("<input type=\"hidden\" name=\"txt_invoice_total\" id=\"txt_invoice_total\" value=\"");
			Str.append(invoice_total).append("\">\n<b>");
			Str.append(df.format(invoice_total)).append("</b></td>\n");
			Str.append("</tr>\n");
			if (!vouchertype_id.equals("11")) {
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {

						invoice_grandtotal += crs1.getDouble("cart_amount");

						Str.append("<tr valign=\"top\">");
						if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {

							Str.append("<td valign=\"top\" colspan=\"6\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
							if (crs1.getString("tax_name").toLowerCase().contains("discount")) {
								Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("tax_rate") + "%").append("</td><td></td>");
								Str.append("<td valign=\"top\" align=\"right\">").append("-" + crs1.getString("cart_amount")).append("</td>");
								invoice_grandtotal = invoice_grandtotal - (crs1.getDouble("cart_amount") * 2);
							} else {
								Str.append("<td></td><td valign=\"top\" align=\"right\">").append(crs1.getString("tax_rate") + "%").append("</td>");
								Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("cart_amount")).append("</td>");
							}

						} else {
							if (!cart_voucher_so_id.equals("0")) {
								Str.append("<td valign=\"top\" colspan=\"7\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
							} else {
								Str.append("<td valign=\"top\" colspan=\"8\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
							}
							Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("tax_rate")).append("</td>");
							Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("cart_amount")).append("</td>");

						}
						Str.append("</tr>");
					}
				}

				invoice_grandtotal += invoice_total;
				if (vouchertype_roundoff.equals("1")) {
					double roundedAmount = (double) Math.round(invoice_grandtotal) - invoice_grandtotal;
					if (roundedAmount != 0) {
						invoice_grandtotal = invoice_grandtotal + roundedAmount;
						Str.append("<tr><td valign=top colspan=8 align=left><b>Round Off</b></td>");
						Str.append("<td align=right><input id='round_off' name='round_off' value='" + df.format(roundedAmount) + "' hidden/> <b>" + df.format(roundedAmount) + "</b></td></tr>");
					}
				}

				Str.append("<tr valign=\"top\">\n");
				if (!cart_voucher_so_id.equals("0")) {
					Str.append("<td valign=\"top\" colspan='7' align='left'>");
				} else {
					Str.append("<td valign=\"top\" colspan='7' align='left'>");
				}
				Str.append("<a id='delete_full_cart' name='delete_full_cart' class='btn btn-success' href='#' onclick=\"DeleteFullCart();\">Delete Cart</a>");

				Str.append("<a class=\" btn btn-success btn-outline sbold\" href=\"../accounting/bill-sundry.jsp?so=yes&add=yes&cart_session_id=" + cart_session_id + "&vouchertype_id="
						+ vouchertype_id + "&voucher_id=" + voucher_id + "&total=" + df.format(invoice_grandtotal) + "\" data-target=\"#Hintclicktocall\" data-toggle=\"modal\">Bill Sundry</a></td>");

				Str.append("<td valign=\"top\" align='right'><b>Grand Total: </b></td>");
				Str.append("<td valign=\"top\" align='right'><b>");
				Str.append(df.format(invoice_grandtotal));
				Str.append("</b></td>");
				Str.append("<input type='text' name='txt_grand_total' id='txt_grand_total' value='" + df.format(invoice_grandtotal) + "' hidden>\n");
				Str.append("</tr>\n");
				crs1.close();
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</table>\n");
		return Str.toString();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		doGet(request, response);
	}

	public String PopulateUOM(String uom_id, String alt_uom_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT uom_id, uom_ratio, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom" + " WHERE 1=1"
					+ " AND (uom_id = " + uom_id + " OR uom_parent_id = " + uom_id + ")"
					+ " GROUP BY CONCAT(uom_name,uom_ratio)"
					+ " ORDER BY uom_id";
			// SOP("StrSql=po=uom11="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_alt_uom_id\" id=\"dr_alt_uom_id\" style=\"width:120px\" class=\"form-control\"  onChange=\"CalItemTotal(" + para + ");\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("uom_id")).append("-").append(crs.getString("uom_ratio"));
				Str.append(StrSelectdrop(crs.getString("uom_id"), alt_uom_id)).append(">");
				Str.append(crs.getString("uom_name")).append(" X ").append(crs.getString("uom_ratio"));
				Str.append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
