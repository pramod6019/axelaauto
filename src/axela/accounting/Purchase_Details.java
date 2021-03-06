package axela.accounting;

//Shivaprasad Nov 20 2014

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

public class Purchase_Details extends Connect {

	public String comp_id = "0", rateclass_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String add = "", status = "";
	public String emp_id = "0";
	public String ledger_id = "0";
	public String location_id = "0";
	public String cart_session_id = "0", cart_id = "0", refresh = "";
	public String cart_item_id = "0", vouchertype_id = "0", voucher_id = "0", voucherclass_id = "0";
	public String list_cartitems = "", msg = "", cart_rowcount = "", rowcount = "";
	public String add_cartitem = "";
	public String update_cartitem = "";
	public String delete_cartitem = "", delete_full_cart = "";
	public String cart_time = "", cart_option_id = "0";
	public String price_sales_customer_id = "0";
	public String price_discount1_customer_id = "0";
	public String cart_price = "0.00", cart_netprice = "0.00";
	public String qty = "0";
	public double cart_qty = 0.00, cart_uom_ratio = 1, cart_alt_qty = 1;
	public double cart_amount = 0.00, cart_disc_amount = 0.00;
	public String disc = "0.00", item_id = "0", cart_item_name = "";
	public String cart_convfactor = "1";
	public double cart_disc = 0.00, cart_discpercent = 0.00;
	public double cart_unit_cost = 0.00;
	public String tax_rate1 = "0.00";
	public String tax_rate2 = "0.00";
	public String tax_rate3 = "0.00";
	public String tax_id1 = "0";
	public String tax_id2 = "0";
	public String tax_id3 = "0";
	public String boxtype_size = "0";
	public String tax_customer_id1 = "0";
	public String tax_customer_id2 = "0";
	public String tax_customer_id3 = "0";
	public String price_tax2_after_tax1 = "0";
	public String price_tax3_after_tax2 = "0";
	public String configure = "", configure1 = "", mode = "";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df6 = new DecimalFormat("0.000000");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String comp_module_inventory = "0";
	public String config_inventory_current_stock = "0", para = "";
	public double optiontotal = 0.00, cart_item_price = 0.00;
	// bs - billsundry i.e additional tax
	public String usersession = "";
	// For updating dc field
	public String cart_cust_disc_dc = "";
	public String cart_ledger_tax_dc = "", cart_alt_uom_id = "0", uom_ratio = "1", cart_disc_type = "", discpercent = "0.00";
	// Variable to check duplicate items
	public String cart_dup_item_id = "0", temp_cart_voucher_id = "0";
	public String cart_voucher_id = "0", cart_voucher_po_id = "0", cart_voucher_git_id = "0", cart_voucher_grn_id = "0", from_voucher_quantity = "0.00";
	public double from_voucher_qty = 0.00;
	public String vouchertype_name = "", branch_id = "0";
	public String updatecount = "";
	// Unused variables
	public String cart_option_group = "";
	public String item_ticket_dept_id = "0", cart_opitem_item_id = "0", cart_item_serial = "", cart_item_batch = "";
	public String cart_opitem_price = "0.00", cart_opitem_netprice = "0.00", configitems_total = "0.00";
	public String cart_opitem_qty = "0", so = "";

	// GST = state or central
	public String gst_type = "";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request) + "");
			comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request) + "");
			config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request));
			rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			cart_session_id = CNumeric(PadQuotes(request.getParameter("cart_session_id")));
			cart_item_id = CNumeric(PadQuotes(request.getParameter("cart_item_id")));
			cart_opitem_item_id = CNumeric(PadQuotes(request.getParameter("cart_opitem_item_id")));
			para = PadQuotes(request.getParameter("para"));
			voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
			vouchertype_id = CNumeric(PadQuotes(request.getParameter("cart_vouchertype_id")));
			// ///Bill Sundry variables
			cart_voucher_po_id = CNumeric(PadQuotes(request.getParameter("voucher_po_id")));
			cart_voucher_git_id = CNumeric(PadQuotes(request.getParameter("voucher_git_id")));
			// SOP("cart_voucher_git_id===" + cart_voucher_git_id);
			cart_voucher_grn_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_id")));
			// SOP("cart_voucher_grn_id===" + cart_voucher_grn_id);
			voucher_id = CNumeric(PadQuotes(request.getParameter("cart_voucher_id")));
			so = PadQuotes(request.getParameter("so"));
			gst_type = PadQuotes(request.getParameter("gst_type"));
			if (vouchertype_id.equals("12") || vouchertype_id.equals("21")) {
				cart_cust_disc_dc = "0";
				cart_ledger_tax_dc = "1";
			} else {
				cart_cust_disc_dc = "1";
				cart_ledger_tax_dc = "0";
			}
			cart_item_name = CNumeric(PadQuotes(request.getParameter("cart_item_name")));
			cart_item_serial = PadQuotes(request.getParameter("cart_item_serial"));
			cart_item_batch = PadQuotes(request.getParameter("cart_item_batch"));
			cart_price = CNumeric(PadQuotes(CheckNull(request.getParameter("cart_price"))));
			if (!cart_price.equals("0")) {
				cart_item_price = Double.parseDouble(cart_price);
			}
			cart_netprice = (PadQuotes(request.getParameter("cart_netprice")));

			qty = PadQuotes(request.getParameter("cart_alt_qty"));
			if (!qty.equals("")) {
				cart_alt_qty = Double.parseDouble(qty);
			}

			cart_alt_uom_id = CNumeric(PadQuotes(request.getParameter("cart_uom_id")));
			if (!cart_alt_uom_id.equals("0")) {
				uom_ratio = ExecuteQuery("SELECT uom_ratio"
						+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
						+ " WHERE uom_id = " + cart_alt_uom_id + "");
				cart_uom_ratio = Double.parseDouble(uom_ratio);
			}
			cart_qty = cart_alt_qty * cart_uom_ratio;
			// refresh = PadQuotes(request.getParameter("refresh"));
			SetSession("refresh", refresh, request);
			// cart_amount =
			// CNumeric(PadQuotes(request.getParameter("cart_amount")));\
			ledger_id = CNumeric(PadQuotes(request.getParameter("ledger_id")));
			price_sales_customer_id = CNumeric(PadQuotes(request.getParameter("price_sales_customer_id")));
			if (!price_sales_customer_id.equals(ledger_id) && !ledger_id.equals("0")) {
				price_sales_customer_id = ledger_id;
			}
			disc = PadQuotes(request.getParameter("disc"));
			discpercent = PadQuotes(request.getParameter("cart_discpercent"));
			if (discpercent.equals("undefined")) {
				discpercent = "0.0";
			}
			if (!discpercent.equals("")) {
				cart_discpercent = Double.parseDouble(discpercent);
			}
			cart_disc_type = CNumeric(PadQuotes(request.getParameter("disc_type")));
			if (!disc.equals("")) {
				cart_disc = Double.parseDouble(disc);
			}
			cart_unit_cost = cart_item_price - cart_disc;
			price_discount1_customer_id = CNumeric(PadQuotes(request.getParameter("price_discount1_customer_id")));
			tax_customer_id1 = CNumeric(PadQuotes(request.getParameter("tax_customer_id1")));
			tax_rate1 = CNumeric(PadQuotes(request.getParameter("tax_rate1")));
			tax_customer_id2 = CNumeric(PadQuotes(request.getParameter("tax_customer_id2")));
			tax_id1 = CNumeric(PadQuotes(request.getParameter("tax_id1")));
			tax_id2 = CNumeric(PadQuotes(request.getParameter("tax_id2")));
			tax_id3 = CNumeric(PadQuotes(request.getParameter("tax_id3")));
			// boxtype_size = CNumeric(PadQuotes(CheckNull(request.getParameter("cart_boxtype_size"))));
			tax_rate2 = CNumeric(PadQuotes(request.getParameter("tax_rate2")));
			tax_customer_id3 = CNumeric(PadQuotes(request.getParameter("tax_customer_id3")));
			tax_rate3 = CNumeric(PadQuotes(request.getParameter("tax_rate3")));
			price_tax2_after_tax1 = CNumeric(PadQuotes(request.getParameter("price_tax2_after_tax1")));
			price_tax3_after_tax2 = CNumeric(PadQuotes(request.getParameter("price_tax3_after_tax2")));
			cart_time = PadQuotes(request.getParameter("cart_time"));
			rowcount = PadQuotes(request.getParameter("rowcount"));
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
			// ------unused variables use when required ie for configured items-------------------
			// cart_convfactor = CNumeric(PadQuotes(request.getParameter("cart_convfactor")));
			// item_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("item_ticket_dept_id")));
			// cart_option_group = PadQuotes(request.getParameter("cart_option_group"));
			// cart_option_id = CNumeric(PadQuotes(request.getParameter("cart_option_id")));
			// cart_opitem_price = CNumeric(PadQuotes(request.getParameter("cart_opitem_price")));
			// cart_opitem_netprice = CNumeric(PadQuotes(request.getParameter("cart_opitem_netprice")));
			// configitems_total = CNumeric(PadQuotes(request.getParameter("configitems_total")));
			// cart_opitem_qty = CNumeric(PadQuotes(request.getParameter("cart_opitem_qty")));
			// modelvalue = PadQuotes(request.getParameter("modelvalue"));
			// model = PadQuotes(request.getParameter("model"));

			// -------------To check item details from converting one voucher to another
			// voucher
			if (!cart_item_id.equals("0") && !emp_id.equals("0")
					&& !cart_session_id.equals("0")) {
				StrSql = "SELECT cart_item_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1 "
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_item_id =" + cart_item_id
						+ " AND cart_emp_id =" + emp_id
						+ " AND cart_session_id =" + cart_session_id;
				// SOP("StrSql==dup=="+StrSql);
				cart_dup_item_id = CNumeric(ExecuteQuery(StrSql));
			}
			// -----------To get from voucher qty, so that we check to voucher qty should not exceed from voucher qty
			// if (!cart_voucher_po_id.equals("0")) {
			// temp_cart_voucher_id = cart_voucher_po_id;
			// } else if (!cart_voucher_git_id.equals("0")) {
			// temp_cart_voucher_id = cart_voucher_git_id;
			// } else if (!cart_voucher_grn_id.equals("0")) {
			// temp_cart_voucher_id = cart_voucher_grn_id;
			// }
			//
			// if (!temp_cart_voucher_id.equals("0")) {
			// StrSql = "SELECT vouchertype_name FROM axela_acc_voucher_type"
			// + " INNER JOIN  "+compdb(comp_id)+"axela_acc_voucher on voucher_vouchertype_id = vouchertype_id"
			// + " WHERE 1=1 "
			// + " AND voucher_id = " + temp_cart_voucher_id;
			// // SOP("StrSql==vouchertype_name="+StrSql);
			// vouchertype_name = ExecuteQuery(StrSql);
			//
			// StrSql = " SELECT COALESCE(SUM(vouchertrans_alt_qty),0) FROM axela_acc_voucher_trans"
			// + " WHERE 1=1 "
			// + " AND vouchertrans_voucher_id = " + temp_cart_voucher_id
			// + " AND vouchertrans_item_id =" + cart_item_id;
			// // SOP("StrSql==from_voucher_quantity="+StrSql);
			// from_voucher_quantity = CNumeric(ExecuteQuery(StrSql));
			// if (!from_voucher_quantity.equals("")) {
			// from_voucher_qty = Double.parseDouble(from_voucher_quantity);
			// }
			// }
			// ///////////////////////END///////////////////////////////////////////

			// Item Amount
			if (!cart_netprice.equals("")) {
				cart_amount = Double.parseDouble(cart_netprice);
				if ((vouchertype_id.equals("7") || vouchertype_id.equals("9")) && qty.contains("-")) {
					cart_amount = (cart_amount);
					cart_netprice = cart_amount + "";
				}
			}
			// Item Discount Amount
			cart_disc_amount = cart_disc * cart_qty;
			if ((vouchertype_id.equals("7") || vouchertype_id.equals("9")) && qty.contains("-")) {
				cart_disc_amount = (cart_disc_amount);
			}

			if (!cart_item_id.equals("0") && configure1.equals("yes")) {
				if (!gst_type.equals("") || vouchertype_id.equals("1")) {
					StrHTML = getItemDetails(location_id, cart_item_id, rateclass_id, emp_id, cart_session_id, mode);
				} else {
					StrHTML = "<center><font color='#ff0000'><b>Select Party City!</b></font></center>";
				}
			}
			// if (!emp_id.equals("0") && add_cartitem.equals("yes")
			// && (cart_alt_qty > from_voucher_qty)
			// && (!temp_cart_voucher_id.equals("0"))) {
			// msg = "Qty. can't be greater than " + vouchertype_name + " Qty.!";
			// StrHTML = ListCartItems(emp_id, vouchertype_id);
			// } else
			if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes") && !cart_dup_item_id.equals("0")) {
				msg = "Item Already Present!";
				StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
			} else if (!emp_id.equals("0") && !cart_session_id.equals("0")
					&& add_cartitem.equals("yes") && msg.equals("")
					// && config_inventory_current_stock.equals("1")
					&& !location_id.equals("0")) {
				AddCartItem();
			} else if (!emp_id.equals("0") && !cart_session_id.equals("0")
					&& add_cartitem.equals("yes") && msg.equals("")
					// && config_inventory_current_stock.equals("0")
					&& location_id.equals("0")) {
				AddCartItem();
			}
			// else if (!emp_id.equals("0") && update_cartitem.equals("yes")
			// && (cart_alt_qty > from_voucher_qty)
			// && !temp_cart_voucher_id.equals("0")) {
			// msg = "Qty. can't be greater than " + vouchertype_name + " Qty.!";
			// StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
			// }
			else if (!cart_id.equals("0") && update_cartitem.equals("yes")
					&& msg.equals("")
					// && config_inventory_current_stock.equals("1")
					&& !location_id.equals("0")) {
				UpdateCartItem();
			} else if (!cart_id.equals("0") && update_cartitem.equals("yes")
					&& msg.equals("")
			// && config_inventory_current_stock.equals("0")
			// && location_id.equals("0")
			) {
				UpdateCartItem();
			} else if (!emp_id.equals("0") && !cart_session_id.equals("0")
					&& list_cartitems.equals("yes")) {
				StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
			} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !emp_id.equals("0")
					// && config_inventory_current_stock.equals("1")
					&& !location_id.equals("0")) {
				DeleteCartItem();
			} else if (!cart_id.equals("0") && delete_cartitem.equals("yes")
					&& !emp_id.equals("0") && !cart_session_id.equals("0")
					// && config_inventory_current_stock.equals("0")
					&& location_id.equals("0")) {
				DeleteCartItem();
			} else if (delete_full_cart.equals("yes") && !cart_session_id.equals("0") && !emp_id.equals("0")) {
				DeleteCartAllItems();
			}
			// else if (!cart_item_id.equals("0") && configure.equals("yes")) {
			// GetConfigurationDetails();
			// }
		}
	}
	public void AddCartItem() throws SQLException {

		if (Integer.parseInt(cart_convfactor) == 0) {
			cart_convfactor = "1";
		}
		int temp_uom_ratio = 0;
		double tempalt_cart_uom_ratio = 1, temp_qty = 0.00;
		double cart_truckspace = 0.00;
		double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
		double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
		double cart_amount_tax = 0.00;

		cart_amount_tax += (cart_item_price + Double.parseDouble(configitems_total)) - cart_disc;
		tax1_amount = (cart_amount_tax * cart_qty * Double.parseDouble(tax_rate1) / 100);
		tax1_price = ((cart_amount_tax) * Double.parseDouble(tax_rate1) / 100);
		// SOP("tax1_amount==="+tax1_amount);
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

		// SOP("tax1_amount==="+tax1_amount);
		// SOP("tax2_amount==="+tax2_amount);
		// SOP("tax3_amount==="+tax3_amount);
		if ((vouchertype_id.equals("7") || vouchertype_id.equals("9")) && qty.contains("-")) {
			tax1_amount = -(tax1_amount);
			tax2_amount = -(tax2_amount);
			tax3_amount = -(tax3_amount);
		}
		// truck space calculation
		// if (Double.parseDouble(boxtype_size) != 0.0) {
		// temp_qty = cart_alt_qty;
		// if (!vouchertype_id.equals("108")) {
		// StrSql = "SELECT COALESCE(uom_ratio,0)"
		// + " FROM " + compdb(comp_id) + "axela_inventory_uom"
		// + " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_alt_uom_id = uom_id"
		// + " WHERE item_id = " + cart_item_id;
		// temp_uom_ratio = Integer.parseInt(CNumeric(ExecuteQuery(StrSql)));
		// if (temp_uom_ratio == 0) {
		// temp_uom_ratio = 1;
		// }
		// temp_qty = (cart_alt_qty / temp_uom_ratio);
		// }
		// cart_truckspace = ((temp_qty / Double.parseDouble(boxtype_size)) * 100);
		// }

		if (rowcount.equals("yes")) {
			cart_rowcount = ExecuteQuery("SELECT COALESCE(MAX(cart_rowcount), 0) + 1"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id);
			cart_option_id = "0";
		} else if (rowcount.equals("no")) {
			cart_rowcount = "0";
			if (cart_id.equals("0")) {
				StrSql = "SELECT COALESCE(MAX(cart_rowcount), 0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE 1=1"
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				cart_option_id = ExecuteQuery(StrSql);
				// SOP("cart_option_id===="+cart_option_id);
			} else {
				cart_option_id = ExecuteQuery("SELECT COALESCE(cart_rowcount, 0)"
						+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_id = " + cart_id);
			}
		}

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (!cart_rowcount.equals("0")) {
					// if (!price_sales_customer_id.equals("0"))
					// {
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
							+ " cart_time,"
							+ " cart_dc"
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
							+ " " + ToLongDate(kknow()) + ",";
					StrSql += " '" + cart_ledger_tax_dc + "'";
					StrSql += " )";
					// SOP("StrSql==cart111=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);

					if (cart_disc != 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
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
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ("
								+ " " + voucher_id + ","
								+ " " + price_discount1_customer_id + ","
								+ " " + vouchertype_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " " + location_id + ","
								+ " " + cart_item_id + ","
								+ " '1',"
								+ " " + cart_discpercent + ","
								+ " '0'," + " 0,"
								+ " " + cart_rowcount + ","
								+ " " + cart_rowcount + ","
								+ " ''," + " ''," + " 0,"
								+ " " + cart_disc + ","
								+ " 0.00,"
								+ " " + cart_disc_amount + ","
								+ " " + cart_disc_amount + ","
								+ " 0,"
								+ " 0,"
								+ " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_cust_disc_dc + "'" + ")";
						// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));

						stmttx.addBatch(StrSql);
					}

					// credit tax ledger
					// for (int i = 1; i <= 3; i++) {
					// if (!("tax_customer_id" + i).equals("0")) {
					if (tax1_amount != 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
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
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ( "
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
								+ " '',"
								+ " '',"
								+ " 0,"
								+ " " + tax1_price + ","
								+ " 0.00,"
								+ " " + tax1_amount + ","
								+ " " + tax1_amount + ","
								+ " 0,"
								+ " 0,"
								+ " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'" + ")";
						// SOP("StrSql==cart333=" + StrSqlBreaker(StrSql));

						stmttx.addBatch(StrSql);
					}
					if (tax2_amount != 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
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
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_time,"
								+ " cart_dc"
								+ " ) VALUES ("
								+ " " + voucher_id + ","
								+ " " + tax_customer_id2 + ","
								+ " " + vouchertype_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " " + location_id + ","
								+ " " + cart_item_id + ","
								+ " '0',"
								+ " 0.0,"
								+ " '1',"
								+ " " + tax_id2 + ","
								+ " " + cart_rowcount + ","
								+ " " + cart_rowcount + ","
								+ " ''," + " ''," + " 0,"
								+ " " + tax2_price + ","
								+ " 0.00,"
								+ " " + tax2_amount + ","
								+ " " + tax2_amount + ","
								+ " 0," + " 0," + " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'" + ")";
						// SOP("StrSql==cart444=" + StrSqlBreaker(StrSql));

						stmttx.addBatch(StrSql);
					}

					if (tax3_amount != 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
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
								+ " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
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
								+ " ''," + " ''," + " 0,"
								+ " " + tax3_price + ","
								+ " 0.00,"
								+ " " + tax3_amount + ","
								+ " " + tax3_amount + ","
								+ " 0," + " 0," + " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'" + ")";
						// SOP("StrSql==cart555=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}

					stmttx.executeBatch();
					conntx.commit();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
	}
	public void UpdateCartItem() throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			int temp_uom_ratio = 0;
			double cart_truckspace = 0.00, tempalt_cart_uom_ratio = 1, temp_qty = 0;
			double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
			double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
			double cart_amount_tax = 0.00;

			cart_amount_tax = (cart_item_price + Double.parseDouble(configitems_total)) - cart_disc;

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

			// SOP("boxtype_size==" + boxtype_size);
			// truck space calculation
			// if (Double.parseDouble(boxtype_size) != 0.0) {
			// temp_qty = cart_alt_qty;
			// if (!vouchertype_id.equals("108")) {
			// StrSql = "SELECT COALESCE(uom_ratio,0)"
			// + " FROM " + compdb(comp_id) + "axela_inventory_uom"
			// + " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_uom_id = uom_id"
			// + " WHERE item_id = " + cart_item_id;
			// temp_uom_ratio = Integer.parseInt(CNumeric(ExecuteQuery(StrSql)));
			// if (temp_uom_ratio == 0) {
			// temp_uom_ratio = 1;
			// }
			// temp_qty = (cart_alt_qty / temp_uom_ratio);
			// }
			// cart_truckspace = ((temp_qty / Double.parseDouble(boxtype_size)) * 100);
			// }

			StrSql = "SELECT COALESCE(cart_rowcount, 0) AS cart_rowcount , COALESCE(cart_option_id, 0) AS cart_option_id "
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id;
			// SOP("StrSql==rowcount=="+StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				cart_rowcount = CNumeric(crs1.getString("cart_rowcount"));
				cart_option_id = CNumeric(crs1.getString("cart_option_id"));
			}
			crs1.close();
			// SOP("cart_rowcount====" + cart_rowcount);
			// SOP("cart_option_id====" + cart_option_id);
			if (!cart_rowcount.equals("0") && cart_option_id.equals("0")) {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_cart" + " SET"
						+ " cart_location_id= " + location_id + ","
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
						+ " AND cart_discount = 0" + " AND cart_tax = 0"
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==item price="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			if (cart_disc == 0.0 || cart_disc != 0.0) {
				String temp_cart_discount = ExecuteQuery("SELECT cart_discount FROM " + compdb(comp_id) + "axela_acc_cart"
						+ " WHERE cart_option_id = " + cart_rowcount + ""
						+ " AND cart_discount = 1");
				// SOP("temp_cart_discount==="+temp_cart_discount);
				if (!CNumeric(temp_cart_discount).equals("0")) {
					// SOP("cart_discpercent====" + cart_discpercent);
					StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart" + " SET"
							+ " cart_location_id= " + location_id + ","
							+ " cart_item_id = " + cart_item_id + ","
							+ " cart_discount_perc = " + cart_discpercent + ","
							+ " cart_price = " + cart_disc + ","
							+ " cart_amount = " + cart_disc_amount + ","
							+ " cart_time = " + ToLongDate(kknow()) + ""
							+ " WHERE 1=1"
							+ " AND cart_option_id = " + cart_rowcount + ""
							+ " AND cart_discount = 1"
							+ " AND cart_option_id = " + cart_rowcount
							+ " AND cart_vouchertype_id = " + vouchertype_id
							+ " AND cart_emp_id = " + emp_id
							+ " AND cart_session_id = " + cart_session_id;
					// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				} else {
					StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
							+ " (cart_customer_id,"
							+ " cart_vouchertype_id," + " cart_emp_id,"
							+ " cart_session_id," + " cart_location_id,"
							+ " cart_item_id," + " cart_discount," + " cart_tax,"
							+ " cart_tax_id," + " cart_rowcount,"
							+ " cart_option_id," + " cart_option_group,"
							+ " cart_item_serial," + " cart_item_batch_id,"
							+ " cart_price," + " cart_qty," + " cart_amount,"
							+ " cart_unit_cost," + " cart_alt_qty,"
							+ " cart_alt_uom_id," + " cart_convfactor,"
							+ " cart_time," + " cart_dc)" + " VALUES" + " ("
							+ price_discount1_customer_id + ","
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
							+ " 0.00," + " 0," + " 0," + " 0,"
							+ " "
							+ ToLongDate(kknow())
							+ "," + " '" + cart_cust_disc_dc + "'" + ")";
					// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
			}
			if (tax1_amount == 0.0 || tax1_amount != 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart" + " SET"
						+ " cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax1_price + ","
						+ " cart_amount = " + tax1_amount + ","
						+ " cart_time = " + ToLongDate(kknow()) + ""
						+ " WHERE 1=1"
						+ " AND cart_option_id = " + cart_rowcount + ""
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id1
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==tax1="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax2_amount == 0.0 || tax2_amount != 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart" + " SET"
						+ " cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax2_price + ","
						+ " cart_amount = " + tax2_amount + ","
						+ " cart_time = " + ToLongDate(kknow()) + ""
						+ " WHERE 1=1"
						+ " AND cart_option_id = " + cart_rowcount + ""
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id2
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax3_amount == 0.0 || tax3_amount != 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart" + " SET"
						+ " cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax3_price + ","
						+ " cart_amount = " + tax3_amount + ","
						+ " cart_time = " + ToLongDate(kknow()) + ""
						+ " WHERE 1=1"
						+ " AND cart_option_id = " + cart_rowcount + ""
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id3
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==item disc="+StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();
			conntx.commit();
			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
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

			cart_option_id = ExecuteQuery("SELECT cart_rowcount"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id
					+ " AND cart_emp_id =" + emp_id + " "
					+ " AND cart_session_id =" + cart_session_id + " "
					+ " AND cart_vouchertype_id = " + vouchertype_id);
			// SOP("cartoptionid===" + cart_option_id);
			// SOP("cart_id===" + cart_id);
			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_option_id = " + cart_option_id + ""
					+ " AND cart_emp_id =" + emp_id
					+ " AND cart_session_id =" + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			stmttx.addBatch(StrSql);

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id + " "
					+ " AND cart_emp_id =" + emp_id
					+ " AND cart_session_id =" + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;

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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
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

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_emp_id =" + emp_id
					+ " AND cart_session_id =" + cart_session_id + " "
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			// SOP("StrSql==DeleteCartAllItems=="+StrSql);
			updateQuery(StrSql);

			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception e) {

			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}

	}

	public String getItemDetails(String location_id, String cart_item_id,
			String rateclass_id, String emp_id, String cart_session_id,
			String mode) {
		int count = 0, price_disc_type = 0;
		int para = 0;
		double tax_value1 = 0.00, tax_value2 = 0.00, tax_value3 = 0.00;
		String item_serial = "0";
		String item_batchno = "0";
		String optioncount = "";
		double tax1_rate1 = 0.00, tax2_rate2 = 0.00, tax3_rate3 = 0.00;
		double tax1_customer_id1 = 0.00, tax2_customer_id2 = 0.00, tax3_customer_id3 = 0.00;
		double item_unit_price = 0.00, itemprice = 0.00, discount = 0.00, quantity = 0.00;
		double cart_nettotal = 0.00;
		double cart_total = 0.00;
		double temp_item_unit_price = 0.0;
		double temp_item_discount = 0.0;

		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT item_id, item_name, item_code, uom_id, uom_ratio, item_uom_id, item_uom_id,"
					+ " COALESCE(stockserial_serial_no,'') AS stockserial_serial_no,"
					+ " price_amt, price_variable,"
					+ " COALESCE(item_purchase_ledger_id ,'0') AS price_sales_customer_id,"
					+ " price_disc, price_disc_type,"
					+ " COALESCE(item_purchasediscount_ledger_id, '0')price_discount1_customer_id,";
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

			// + " COALESCE(boxtype_size, 0) AS boxtype_size,"
			StrSql += " item_serial, item_ticket_dept_id,"
					+ " COALESCE(salescat_name, '') AS salescat_name,"
					+ " IF(item_nonstock = 1, COALESCE(stock_current_qty, 0), '-') AS stock_current_qty, "
					+ " IF(COALESCE(option_id, 0) != 0, 1, 0) as optioncount,"
					+ " emp_invoice_priceupdate, emp_invoice_discountupdate"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_uom on uom_id = item_uom_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id";

			if (gst_type.equals("state")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id = item_purchasetax1_ledger_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id = item_purchasetax2_ledger_id";
			} else if (gst_type.equals("central")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = item_purchasetax3_ledger_id";
			}
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax4 on tax4.customer_id = item_purchasetax4_ledger_id"

					// + " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_boxtype on boxtype_id = item_boxtype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_salescat_trans on trans_item_id = item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_salescat on salescat_id = trans_salescat_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_option on option_itemmaster_id = item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_stockserial on stockserial_item_id = item_id"
					+ " AND stock_location_id = " + location_id + ""
					+ " , " + compdb(comp_id) + "axela_emp "
					+ " WHERE item_active = 1 and item_id=" + cart_item_id + ""
					+ " AND price_id = (SELECT price_id FROM  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_item_id = item_id"
					// + " AND price_sales_customer_id != 0"
					+ " AND price_rateclass_id	 = " + rateclass_id + ""
					+ " AND price_effective_from <= " + ToLongDate(kknow())
					// + " AND price_company_id = " + comp_id
					+ " AND price_active = 1"
					+ " ORDER BY price_effective_from"
					+ " DESC LIMIT 1)"
					+ " AND emp_id=" + emp_id
					+ " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 6";

			SOP("StrSql==get==purchase=" + StrSqlBreaker(StrSql));
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
				Str.append("</tr>\n");
				Str.append("</thead>");
				Str.append("<tbody>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					if (mode.equals("add")) {
						// if (vouchertype_id.equals("10")
						// || vouchertype_id.equals("1")
						// || vouchertype_id.equals("117")
						// || vouchertype_id.equals("2")
						// || vouchertype_id.equals("115")) {
						// cart_alt_uom_id = crs.getString("item_uom_id");
						// } else {
						cart_alt_uom_id = crs.getString("item_uom_id");
						// }
						// SOP("cart_alt_uom_id==="+cart_alt_uom_id);
						uom_ratio = ExecuteQuery("SELECT uom_ratio"
								+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
								+ " WHERE uom_id = " + cart_alt_uom_id + "");
						if (!uom_ratio.equals("0")) {
							cart_uom_ratio = Double.parseDouble(uom_ratio);
						}
						quantity = cart_alt_qty * cart_uom_ratio;
						item_unit_price = crs.getDouble("price_amt");
						// SOP("item_unit_price==="+item_unit_price);
						itemprice = (item_unit_price * quantity);
						price_disc_type = crs.getInt("price_disc_type");
						if (price_disc_type == 1) {
							discount = crs.getDouble("price_disc");
							// SOP("discount==="+discount);
							if (discount != 0.0 && itemprice != 0.0) {
								cart_discpercent = ((discount * 100) / (itemprice + optiontotal));
							}
							// SOP("discpercent==="+discpercent);
						} else {
							cart_discpercent = crs.getDouble("price_disc");
							discount = ((itemprice + optiontotal) * cart_discpercent) / 100;
							// SOP("discpercent=11=="+discpercent);
						}
						tax1_rate1 = crs.getDouble("tax_rate1");
						tax2_rate2 = crs.getDouble("tax_rate2");
						tax3_rate3 = crs.getDouble("tax_rate3");
						tax1_customer_id1 = crs.getDouble("tax_customer_id1");
						tax2_customer_id2 = crs.getDouble("tax_customer_id2");
						tax3_customer_id3 = crs.getDouble("tax_customer_id3");

						item_serial = crs.getString("item_serial");
						// item_batchno = crs.getString("item_batchno");
						optioncount = crs.getString("optioncount");
					} else if (mode.equals("update")) {
						quantity = cart_alt_qty * cart_uom_ratio;
						item_unit_price = cart_item_price;
						itemprice = item_unit_price * quantity;
						price_disc_type = crs.getInt("price_disc_type");
						discount = cart_disc;
						// if (discount != 0.0 && itemprice != 0.0) {
						// discpercent = ((discount * 100) / (itemprice +
						// optiontotal));
						// }
						tax1_rate1 = crs.getDouble("tax_rate1");
						tax2_rate2 = crs.getDouble("tax_rate2");
						tax3_rate3 = crs.getDouble("tax_rate3");
						tax1_customer_id1 = crs.getDouble("tax_customer_id1");
						tax2_customer_id2 = crs.getDouble("tax_customer_id2");
						tax3_customer_id3 = crs.getDouble("tax_customer_id3");

						item_serial = crs.getString("item_serial");
						// item_batchno = crs.getString("item_batchno");
						optioncount = crs.getString("optioncount");

					}
					// Populate Configured Items don't delete
					// if (optioncount.equals("1")) {
					// strconfigoptions = GetConfigurationDetails();
					// }

					cart_nettotal = (item_unit_price - discount) * quantity;

					// --------OLD----------Tax cal for item (price-disc)
					// code....
					/*
					 * if (tax1_rate1 != 0.00) { // tax_value1 = ((itemprice + optiontotal - discount) * // quantity // * tax1_rate1 / 100); tax_value1 = (((itemprice + optiontotal - discount) /
					 * cart_alt_qty) tax1_rate1 / 100); // SOP("tax_value1==="+tax_value1); } if (tax2_rate2 != 0.00) { tax_value2 = (((itemprice + optiontotal - discount) / cart_alt_qty) tax2_rate2 /
					 * 100); } if (tax3_rate3 != 0.00) { tax_value3 = (((itemprice + optiontotal - discount) / cart_alt_qty) tax3_rate3 / 100); } // cart_total = ((((itemprice + optiontotal) -
					 * discount)) * (tax1_rate1 / 100)) + ((((itemprice + optiontotal) - discount)) * (tax2_rate2 / 100)) + ((((itemprice + optiontotal) - discount)) * (tax3_rate3 / 100)) +
					 * (((itemprice + optiontotal) - discount));
					 */
					// ---------------------------------END.-----------------------------------------------------------
					if (tax1_rate1 != 0.00) {
						// tax_value1 = ((itemprice + optiontotal - discount) *
						// quantity
						// * tax1_rate1 / 100);
						// Note- We are calculating for Alt Uom...
						tax_value1 = ((item_unit_price - (discount / quantity))
								* cart_uom_ratio * (tax1_rate1 / 100));
						// SOP("tax_value1==="+tax_value1);
					}
					if (tax2_rate2 != 0.00) {
						if ((price_tax2_after_tax1.equals("1")) && (tax_value1 != 0)) {
							tax_value2 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1) * (tax2_rate2 / 100));
							// tax_value2 = ((((itemprice + optiontotal -
							// discount) + (tax_value1 * cart_alt_qty)) /
							// cart_alt_qty)
							// * tax2_rate2 / 100);
							// SOP("tax_value2==="+tax_value2);
						} else {
							tax_value2 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax2_rate2 / 100));
							// SOP("tax_value2==esle="+tax_value2);
						}
					}
					if (tax3_rate3 != 0.00) {
						if ((price_tax3_after_tax2.equals("1")) && (tax_value1 != 0) && (tax_value2 != 0)) {
							tax_value3 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio)
									+ tax_value1 + tax_value2) * (tax3_rate3 / 100));
							// tax_value3 = ((((itemprice + optiontotal -
							// discount) + ((tax_value1 + tax_value2) *
							// cart_alt_qty)) / cart_alt_qty)
							// * tax3_rate3 / 100);
							// SOP("tax_value3==="+tax_value3);
						} else {
							tax_value3 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax3_rate3 / 100));
							// SOP("tax_value3=else=="+tax_value3);
						}
					}
					// SOP("item_unit_price==="+item_unit_price);
					// Total amount for No. Of Qty's.
					cart_total = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1 + tax_value2 + tax_value3) * cart_alt_qty);
					// SOP("cart_total=="+cart_total);

					if (voucherclass_id.equals("101") || voucherclass_id.equals("114")) {
						item_serial = "0";
						item_batchno = "0";
					}

					Str.append("<tr>\n");
					Str.append("<input name=\"uom_id\" type=\"hidden\" id=\"uom_id\" ").append("value=\"").append(cart_alt_uom_id).append("\"/>\n");
					Str.append("<input name=\"uom_ratio\" type=\"hidden\" id=\"uom_ratio\" ").append("value=\"").append(cart_uom_ratio).append("\"/>\n");
					Str.append("<input name=\"altuom_ratio\" type=\"hidden\" id=\"altuom_ratio\" ").append("value=\"").append(cart_uom_ratio).append("\"/>\n");
					Str.append("<td align=\"left\" valign=\"top\">");
					if (item_serial.equals("1") && vouchertype_id.equals("2")) {

						Str.append("<div align=center>1</div>");
						Str.append("<input name=\"txt_item_qty\" type=\"hidden\" id=\"txt_item_qty\" ")
								.append("value=\"").append(df.format(cart_alt_qty)).append("\"/>\n");
					} else {
						Str.append("<input name=\"txt_item_qty\" type=\"text\" class=\"form-control\" id=\"txt_item_qty\" ")
								.append("value=\"");
						if (mode.equals("add")) {
							Str.append("");
						} else if (mode.equals("update")) {
							Str.append(df.format(cart_alt_qty));
						}
						Str.append("\" size=\"10\" maxlength=\"10\" onKeyUp=\"CalItemTotal(" + para + ");\" />\n");
					}
					Str.append("<input name=\"txt_item_id\" type=\"hidden\" id=\"txt_item_id\" ")
							.append("value=\"").append(crs.getString("item_id")).append("\"/>\n");
					// Str.append("<input name=\"txt_boxtype_size\" type=\"hidden\" id=\"txt_boxtype_size\" ")
					// .append("value=\"").append(crs.getString("boxtype_size")).append("\"/>\n");
					Str.append("<input name=\"txt_stock_qty\" type=\"hidden\" id=\"txt_stock_qty\" ")
							.append("value=\"").append(crs.getString("stock_current_qty")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_baseprice\" name=\"txt_item_baseprice\" ")
							.append("value=\"").append(item_unit_price).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_itemprice_updatemode\" name=\"txt_itemprice_updatemode\" ")
							.append("value=\"").append(item_unit_price).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_pricevariable\" name=\"txt_item_pricevariable\" ")
							.append("value=\"").append(crs.getString("price_variable")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_optioncount\" name=\"txt_optioncount\" ")
							.append("value=\"").append(crs.getString("optioncount")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_ticket_dept_id\" name=\"txt_item_ticket_dept_id\" ")
							.append("value=\"").append(crs.getString("item_ticket_dept_id")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_price_sales_customer_id\" name=\"txt_item_price_sales_customer_id\" ")
							.append("value=\"").append(crs.getString("price_sales_customer_id")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_price_discount1_customer_id\" name=\"txt_item_price_discount1_customer_id\" ")
							.append("value=\"").append(crs.getString("price_discount1_customer_id")).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_item_price_disc_type\" name=\"txt_item_price_disc_type\" ")
							.append("value=\"").append(price_disc_type).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_serial\" name=\"txt_serial\"  ")
							.append("value=\"").append(item_serial).append("\"/>\n");
					Str.append("<input type=\"hidden\" id=\"txt_batch\" name=\"txt_batch\"  ")
							.append("value=\"").append(item_batchno).append("\"/>\n");
					Str.append("</td><td align=\"left\" valign=\"top\">");
					if (!CNumeric(cart_alt_uom_id).equals("0")) {
						Str.append(PopulateUOM(crs.getString("uom_id"),
								cart_alt_uom_id));
					}
					if (cart_alt_qty != 0.0) {
						temp_item_unit_price = (itemprice / cart_alt_qty);
						temp_item_discount = (discount / cart_alt_qty);
					}
					// SOP("item_price====we=="+df.format(itemprice/cart_alt_qty));
					Str.append("</td><td align=\"left\" width=\"20%\" valign=\"top\">");
					if (crs.getString("emp_invoice_priceupdate").equals("1") || crs.getString("price_variable").equals("1")) {
						Str.append("<input name=\"txt_item_price\" type=\"text\" style=\"width:90px\" class=\"form-control\" id=\"txt_item_price\" ")
								.append("value=\"").append(df.format(temp_item_unit_price))
								.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price','Price');CalAmount("
										+ para + ");\" onChange=\"CheckBasePrice();CalItemTotal(" + para + ");\" />");
					} else {
						Str.append("<input name=\"txt_item_price\" type=\"text\" class=\"form-control\" id=\"txt_item_price\" ")
								.append("value=\"").append(df.format(temp_item_unit_price)).append("\" size=\"10\" maxlength=\"10\" disabled />");
						//
					}

					Str.append("</td>\n<td align=\"left\" width=\"25%\" valign=\"top\">");
					if (crs.getString("emp_invoice_discountupdate").equals("1")
					// && mode.equals("add")
					) {
						if (price_disc_type == 1) {
							Str.append("<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ")
									.append("value=\"");
							Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
							Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount("
									+ para + ");CalItemTotal(" + para + ");\"/>").append("%");
							Str.append("<br/><input name=\"txt_item_price_disc\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc\" ")
									.append("value=\"").append(df.format((temp_item_discount)))
									.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\">")
									.append(" <div style=\"display:inline\"></div>");
						} else {
							Str.append("<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ")
									.append("value=\"");
							Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
							Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount("
									+ para + ");CalItemTotal(" + para + ");\"/>").append("%");
							Str.append("<br/><input name=\"txt_item_price_disc\"  type=\"text\" class=\"textbox\" id=\"txt_item_price_disc\" ")
									.append("value=\"").append(df.format((temp_item_discount)))
									.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\"/>")
									.append(" <div style=\"display:inline\"></div>");
						}

					}

					// For cart UPDATE "+compdb(comp_id)+"mode to disable disc
					else {
						if (price_disc_type == 1) {
							Str.append(
									"<input name=\"txt_item_price_disc_percent_add\" class=\"form-control\"  type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc_percent_add\"  ")
									.append("value=\"");
							Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
							Str.append("\"/>\n");
							Str.append(" <div style=\"display:inline\">%</div>");
							Str.append("<input name=\"txt_item_price_disc\" class=\"form-control\"  type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc\"  ")
									.append("value=\"").append(df.format((temp_item_discount))).append("\"/>\n");

						} else {
							Str.append("<input name=\"txt_item_price_disc\" class=\"form-control\"  type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc\" disabled ")
									.append("value=\"").append(df.format((temp_item_discount))).append("\"/>\n");
							Str.append(
									"<br/><input name=\"txt_item_price_disc_percent_add\" class=\"form-control\"  type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc_percent_add\" disabled ")
									.append("value=\"");
							Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
							Str.append("\"/>\n");
							Str.append(" <div style=\"display:inline\">%</div>");
						}

					}

					Str.append("</td>\n<td align=\"right\" width = \"25%\" valign=\"top\" nowrap>");
					if (crs.getString("tax1_name").equals("") && crs.getString("tax2_name").equals("") && crs.getString("tax3_name").equals("")) {
						Str.append("0.00").append("");
					}
					if (!crs.getString("tax1_name").equals("")) {
						Str.append(crs.getString("tax1_name")).append(": ");
						Str.append("<br/><input name=\"txt_item_tax1\" type=\"text\" class=\"text\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ")
								.append("value=\"").append(df.format(tax_value1)).append("\"/>\n");
						Str.append("<br/>");
					}
					if (!crs.getString("tax2_name").equals("")) {
						Str.append(crs.getString("tax2_name")).append(": ");
						Str.append("<br/><input name=\"txt_item_tax2\" type=\"text\" class=\"text\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ")
								.append("value=\"").append(df.format(tax_value2)).append("\"/>\n");
						Str.append("<br/>");
					}
					if (!crs.getString("tax3_name").equals("")) {
						Str.append(crs.getString("tax3_name")).append(": ");
						Str.append("<br/><input name=\"txt_item_tax3\" type=\"text\" class=\"text\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ")
								.append("value=\"").append(df.format(tax_value3)).append("\"/>\n");
					}
					Str.append("<input name=\"txt_item_price_tax_rate1\" type=\"hidden\" id=\"txt_item_price_tax_rate1\" ")
							.append(" value=\" ").append(tax1_rate1).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id1\" type=\"hidden\" id=\"txt_item_price_tax_customer_id1\" ")
							.append("value=\"").append(tax1_customer_id1).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id1\" type=\"hidden\" id=\"txt_item_price_tax_id1\" ")
							.append("value=\"").append(crs.getInt("tax_id1")).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_rate2\" type=\"hidden\" id=\"txt_item_price_tax_rate2\" ")
							.append("value=\"").append(tax2_rate2).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id2\" type=\"hidden\" id=\"txt_item_price_tax_customer_id2\" ")
							.append("value=\"").append(tax2_customer_id2).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id2\" type=\"hidden\" id=\"txt_item_price_tax_id2\" ")
							.append("value=\"").append(crs.getInt("tax_id2")).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_rate3\" type=\"hidden\" id=\"txt_item_price_tax_rate3\" ")
							.append("value=\"").append(tax3_rate3).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_customer_id3\" type=\"hidden\" id=\"txt_item_price_tax_customer_id3\" ")
							.append("value=\"").append(tax3_customer_id3).append("\"/>\n");
					Str.append("<input name=\"txt_item_price_tax_id3\" type=\"hidden\" id=\"txt_item_price_tax_id3\" ")
							.append("value=\"").append(crs.getInt("tax_id3")).append("\"/>\n");
					Str.append("<input name=\"txt_price_tax2_after_tax1\" type=\"hidden\" id=\"txt_price_tax2_after_tax1\" ")
							.append("value=\"").append("\"/>\n");
					Str.append("<input name=\"txt_price_tax3_after_tax2\" type=\"hidden\" id=\"txt_price_tax3_after_tax2\" ")
							.append("value=\"").append("\"/>\n");
					Str.append("<input name=\"txt_item_stockserial\" type=\"hidden\" id=\"txt_item_stockserial\" ")
							.append("value=\"").append(crs.getString("stockserial_serial_no")).append("\"/>\n");
					Str.append("</td>\n");
					Str.append("<td align=right valign=top><b>");
					Str.append("<div id=\"item_total\">").append(df.format(cart_total)).append("</div>\n");
					Str.append("</b></td>\n");
					Str.append("</tr>");
					if (item_serial.equals("1") && vouchertype_id.equals("2")) {
						if (!CNumeric(crs.getString("stockserial_serial_no")).equals("0")) {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" valign=\"top\" nowrap>Serial No.<font color=\"#ff0000\">*</font>:</td>\n");
							Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_serial\" type=\"text\" class=\"form-control\" id=\"txt_item_serial\" value="
									+ crs.getString("stockserial_serial_no") + " size=\"20\" maxlength=\"30\" /></td>\n");
							Str.append("</tr>");
						} else {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" valign=\"top\" nowrap>Serial No.<font color=\"#ff0000\">*</font>:</td>\n");
							Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_serial\" type=\"text\" class=\"form-control\" id=\"txt_item_serial\" size=\"20\" maxlength=\"30\" /></td>\n");
							Str.append("</tr>");
						}
					}

					if (item_batchno.equals("1")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"right\" valign=\"top\" nowrap>Batch No.<font color=\"#ff0000\">*</font>:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_batch\" type=\"text\" class=\"form-control\" id=\"txt_item_batch\" size=\"20\" maxlength=\"30\" /></td>\n");
						Str.append("</tr>");
					}
					Str.append("<tr>\n");
					if (mode.equals("add")) {
						Str.append("<td colspan=\"6\" align=\"center\" valign=\"top\">");
						Str.append("<input name=\"add_button\" id=\"add_button\" type=\"button\" class=\"btn btn-success\" value=\"Add\"  onClick=\"AddCartItem();\" /></td>\n"
								+
								"<div id=\"mode_button\"> ").append("</div></td>\n");
					} else if (mode.equals("update")) {
						Str.append("<td colspan=\"6\" align=\"center\" valign=\"top\">");
						Str.append("<input name=\"update_button\" id=\"update_button\" type=\"button\" class=\"btn btn-success\" value=\"Update\"  onClick=\"UpdateCartItem();\" /></td>\n"
								+
								"<div id=\"mode_button\"> ").append("</div></td>\n");
					}
					Str.append("</tr>");

				}
				Str.append("</tbody></table></div>");

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
					+ " opt.option_group_id, item_serial, opt.option_qty, item_code, opt.option_id,"
					+ " opt.option_select, item_small_desc, group_id, (SELECT COUNT(DISTINCT optitem.option_id)"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option optitem"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = optitem.option_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = optitem.option_itemmaster_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_item_id = item_id"
					+ " AND pricetrans_price_id = price_id"
					+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id"
					+ " AND optitem.option_group_id = group_id"
					+ " GROUP BY optitem.option_group_id) AS groupitemcount"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option opt"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = option_itemmaster_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_item_id = item_id"
					+ " AND pricetrans_price_id = price_id"
					+ " WHERE option_itemmaster_id = " + cart_item_id + ""
					+ " GROUP BY group_name, group_type, item_id"
					+ " ORDER BY group_rank, group_name DESC";
			// SOP("StrSql =accinv=config details== " + StrSqlBreaker(StrSql));
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
						Str.append("</b><input type=\"hidden\" id=\"txt_")
								.append(groupcount).append("_count\" value=\"").append(crs.getString("groupitemcount"))
								.append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_value\" name=\"txt_").append(groupcount).append("_value\" >\n");
						Str.append("<input type=\"hidden\" id=\"txt_new_value\" name=\"txt_new_value\" >\n");
						Str.append("<input type=\"hidden\" id=\"txt_optnetprice\" name=\"txt_optnetprice\" >\n");
						Str.append("</td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td>&nbsp;</td>\n</tr>\n");
					}
					groupitemcount++;
					Str.append("<tr>\n");

					// if (crs.getString("option_select").equals("1") &&
					// mode.equals("add")) {
					if (crs.getString("option_select").equals("1")) {
						checked = "checked";
						disabled = "disabled";
						// SOP("defaultselected_total===" +
						// defaultselected_total);
						defaultselected_total += crs.getDouble("option_qty") * crs.getDouble("pricetrans_amt");
						// SOP("defaultselected_total===" +
						// defaultselected_total);
					}
					// else if
					// (crs.getString("cart_option_id").equals(cart_option_id) &&
					// mode.equals("update")) {
					// checked = "checked";
					// // SOP("defaultselected_total==option=" +
					// defaultselected_total);
					// defaultselected_total += crs.getDouble("option_qty") *
					// crs.getDouble("pricetrans_amt");
					// }

					if (crs.getString("group_type").equals("2")) {
						disabled = "disabled";
						all_selected_total += crs.getDouble("pricetrans_amt");
					}

					// if (crs.getString("group_type").equals("3") &&
					// crs.getString("option_select").equals("1") &&
					// mode.equals("add")) {
					if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
						multi_select_basetotal += crs.getDouble("pricetrans_amt");
					}
					// else if (crs.getString("group_type").equals("3") &&
					// crs.getString("cart_option_id").equals(cart_option_id)) {
					// multi_select_basetotal += crs.getDouble("pricetrans_amt");
					// }
					Str.append("<td valign=\"top\" align=\"center\">\n");
					Str.append("<input type=\"checkbox\" id=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" name=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" value=\"").append(crs.getString("pricetrans_amt")).append("\" ").append(checked).append(" ").append(disabled);
					if (crs.getString("group_type").equals("1")) { // function for Default
						Str.append(" onclick=\"CalculateDefault(").append(crs.getString("group_id")).append(",");
						Str.append(crs.getString("groupitemcount")).append(",this.value,").append(groupitemcount).append(",").append(groupcount).append(");\"/>\n");
						// if (crs.getString("option_select").equals("1") &&
						// mode.equals("add")) {
						if (crs.getString("option_select").equals("1")) {
							Str.append("<input type=\"hidden\" id=\"txt_").append(crs.getString("group_id"));
							Str.append("_basevalue\" name=\"txt_").append(crs.getString("group_id")).append("_basevalue\" value=\"");
							Str.append(crs.getString("pricetrans_amt")).append("\"/>\n");
						}
						// else if
						// (crs.getString("cart_option_id").equals(cart_option_id))
						// {
						// Str.append("<input type=\"hidden\" id=\"txt_").append(crs.getString("group_id"));
						// Str.append("_basevalue\" name=\"txt_").append(crs.getString("group_id")).append("_basevalue\" value=\"");
						// Str.append(crs.getString("pricetrans_amt")).append("\"/>\n");
						// }
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

	public String ListCartItems(String emp_id, String cart_session_id,
			String vouchertype_id) throws SQLException {
		StringBuilder Str = new StringBuilder();
		double invoice_grandtotal = 0.00, invoice_total = 0.00, bs_invoice_amt = 0.00, bs_invoice_total = 0.00, total = 0.00, invoice_optnetamt = 0.00, cart_alt_qty = 0.00, uom_ratio = 0.00;
		double item_price = 0.00, item_unit_price = 0.00;
		double discount = 0.00;
		double discpercent = 0.00;
		double quantity = 0.00;
		double pricetax = 0.00;
		double invoice_qty = 0.00;
		double mainitemamt = 0.00;
		double total_discount = 0.00;
		double total_tax = 0.00, tax = 0.00;
		double total_truckspace = 0.00;
		String voucher_id = "0";
		int count = 0;
		String itemtype_name = "";

		// for round off
		String vouchertype_roundoff = "0";

		StrSql = "SELECT cart.cart_id, cart.cart_rowcount, cart.cart_voucher_id AS cart_voucher_id, vouchertype_roundoff,"
				+ " item_id,"
				+ " uom_name,"
				// + " COALESCE(boxtype_size, 0) AS boxtype_size,"
				+ " item_name, item_code, cart.cart_qty, cart.cart_alt_qty, cart.cart_alt_uom_id, cart.cart_truckspace,"
				+ " cart.cart_price, cart.cart_netprice, cart.cart_amount,"
				+ " COALESCE((SELECT sum(disc.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart disc"
				+ " WHERE disc.cart_option_id = cart.cart_rowcount"
				+ " and disc.cart_discount = 1"
				+ " and disc.cart_vouchertype_id = " + vouchertype_id
				+ " and disc.cart_session_id = " + cart_session_id + ""
				+ " and disc.cart_emp_id = " + emp_id + ""
				+ " GROUP BY disc.cart_rowcount), 0) as discount,"
				// //discount_perc
				+ " COALESCE((SELECT discount_perc.cart_discount_perc"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart discount_perc"
				+ " WHERE discount_perc.cart_option_id = cart.cart_rowcount AND discount_perc.cart_discount = 1"
				+ " AND discount_perc.cart_vouchertype_id = " + vouchertype_id
				+ " AND discount_perc.cart_session_id = " + cart_session_id + ""
				+ " AND discount_perc.cart_emp_id = " + emp_id + ""
				+ " GROUP BY discount_perc.cart_rowcount), 0) AS discount_perc,"
				+ " COALESCE((SELECT sum(tax.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax"
				+ " WHERE tax.cart_option_id = cart.cart_rowcount and tax.cart_tax = 1"
				+ " and tax.cart_vouchertype_id = " + vouchertype_id
				+ " and tax.cart_session_id = " + cart_session_id + ""
				+ " and tax.cart_emp_id = " + emp_id + ""
				+ " GROUP BY tax.cart_rowcount), 0) as tax"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart cart"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item on item_id = cart.cart_item_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_uom ON uom_id = cart.cart_alt_uom_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id=" + vouchertype_id
				// + " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_boxtype ON boxtype_id = item_boxtype_id"
				+ " WHERE 1=1"
				+ " AND cart.cart_emp_id = " + emp_id + ""
				+ " AND cart.cart_session_id = " + cart_session_id + ""
				+ " and cart.cart_vouchertype_id =" + vouchertype_id
				+ " and cart.cart_discount=0 "
				+ " and cart.cart_tax=0 "
				+ " and cart.cart_item_id!=0 "
				// + " and cart.cart_alt_uom_id = "+cart_alt_uom_id+""
				+ " and cart.cart_rowcount!=0 "
				+ " and cart.cart_option_id=0"
				+ " GROUP BY cart.cart_id" + " order BY cart.cart_id";

		// SOP("StrSql=List cart items==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		StrSql = "SELECT cart_id, CONCAT(customer_name) AS tax_name,"
				+ " CONCAT(customer_rate) AS tax_rate, cart_amount"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
				+ " WHERE 1=1"
				+ " AND cart_vouchertype_id = " + vouchertype_id
				+ " AND cart_emp_id = " + emp_id
				+ " AND cart_session_id = " + cart_session_id
				+ " AND cart_rowcount = 0"
				+ " AND cart_option_id = 0";
		// SOP("StrSql===cart bill sundry===" + StrSqlBreaker(StrSql));
		CachedRowSet crs1 = processQuery(StrSql, 0);
		Str.append("<div class=\"table-responsive table-bordered\">\n");
		Str.append("<table class=\"table table-responsive table-hover table-bordered\" data-filter=\"#filter\"><thead>\n");
		try {
			if (!msg.equals("")) {
				if (vouchertype_id.equals("7") || vouchertype_id.equals("9")) {
					Str.append("\n<tr>\n<td colspan=\"9\" align=\"center\"><font color=\"red\"><b>");
				} else {
					Str.append("\n<tr>\n<td colspan=\"9\" align=\"center\"><font color=\"red\"><b>");
				}
				// SOP("msg==="+msg);
				Str.append("(" + msg + ")");
				Str.append("</b></font></td>\n</tr>\n");
			}
			Str.append("\n<tr>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>X</th>\n");
			Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item</th>\n");
			Str.append("<th data-hide=\"phone\">Qty</th>\n");
			Str.append("<th data-hide=\"phone\">UOM</th>\n");
			Str.append("<th data-hide=\"phone\">Price</th>\n");
			Str.append("<th data-hide=\"phone\">Discount</th>\n");
			Str.append("<th data-hide=\"phone\">Tax</th>\n");
			// Str.append("<th data-hide=\"phone\"></th>\n");
			Str.append("<th>Amount</th>\n");

			Str.append("</tr></thead><tbody>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vouchertype_roundoff = crs.getString("vouchertype_roundoff");
					voucher_id = crs.getString("cart_voucher_id");
					item_price = crs.getDouble("cart_price");
					// SOP("item_price==="+item_price);
					cart_alt_qty = crs.getDouble("cart_alt_qty");
					// SOP("cart_alt_qty==="+cart_alt_qty);
					quantity = crs.getDouble("cart_qty");
					// SOP("quantity==="+quantity);
					if (cart_alt_qty != 0.0) {
						uom_ratio = (quantity / cart_alt_qty);
					}
					if (quantity != 0.0) {
						discount = crs.getDouble("discount") / quantity;
					}
					tax = crs.getDouble("tax");
					// SOP("discount==be="+discount);
					// SOP("tax==be="+tax);
					if ((vouchertype_id.equals("7") || vouchertype_id.equals("9")) && crs.getString("cart_qty").contains("-")) {
						discount = -(discount);
						tax = -(tax);
						// SOP("discount==af="+discount);
						// SOP("tax==af="+tax);
					}
					discpercent = crs.getDouble("discount_perc"); // For qty
					// discpercent = cart_discpercent;

					// if (item_price != 0.0) {
					// discpercent = ((discount * 100) / (item_price +
					// optiontotal));
					// }
					mainitemamt = (item_price) * quantity;
					invoice_qty += crs.getDouble("cart_alt_qty");
					total_discount += crs.getDouble("discount");
					total = (((item_price - discount) * quantity) + tax);
					if ((total + "").equals("NaN") || CNumeric(PadQuotes(CheckNull(total + ""))).equals(0)) {
						total = 0.0;
					}
					if ((vouchertype_id.equals("7") || vouchertype_id.equals("9")) && crs.getString("cart_qty").contains("-")) {
						total = -(total);
					}
					invoice_total += total;
					if ((invoice_total + "").equals("NaN") || CNumeric(PadQuotes(CheckNull(invoice_total + ""))).equals(0)) {
						invoice_total = 0.0;
					}
					// if((vouchertype_id.equals("7") ||
					// vouchertype_id.equals("9")) &&
					// crs.getString("cart_qty").contains("-")){
					// invoice_total = -(invoice_total);
					// }

					total_tax += Double.parseDouble(crs.getString("tax"));
					total_truckspace += Double.parseDouble(crs.getString("cart_truckspace"));
					String item_name = "";
					if (!crs.getString("item_name").equals("")) {
						item_name += crs.getString("item_name");

					}
					item_name += " (" + crs.getString("item_code") + ") ";

					if (!crs.getString("cart_rowcount").equals("0")) {
						++count;
					}
					Str.append("\n<tr valign=\"top\"");
					// Str.append(" onClick=\"ItemDetails(");
					// Str.append(crs.getString("item_id")).append(",");
					// Str.append(crs.getDouble("cart_alt_qty")).append(",");
					// Str.append(crs.getDouble("cart_alt_uom_id")).append(",");
					// Str.append(item_price + ",");
					// Str.append(crs.getDouble("discount") + ",");
					// Str.append(discpercent + ",");
					// Str.append(crs.getDouble("boxtype_size")).append(",");
					// Str.append(" 0,");
					// Str.append(crs.getString("cart_id")).append(",");
					// Str.append("'update',0);\"");
					// Str.append("'update');\"");

					// Str.append("\n\n");

					Str.append(">\n<td align=\"center\">\n");
					Str.append(count);
					Str.append("</td>\n<td align=\"center\">\n");
					if (!cart_voucher_git_id.equals("0") || !cart_voucher_grn_id.equals("0")) {
						Str.append("X");
					} else {
						Str.append("<a href=\"javascript:delete_cart_item(").append(crs.getString("cart_id")).append(");\">X</a>");
					}
					Str.append("</td>\n<td id='" + count + "' align=\"left\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getDouble("cart_alt_qty")).append(",");
					Str.append(crs.getDouble("cart_alt_uom_id")).append(",");
					Str.append(item_price + ",");
					Str.append(crs.getDouble("discount") + ",");
					Str.append(discpercent + ",");
					// Str.append(crs.getDouble("boxtype_size")).append(",");
					Str.append(" 0,");
					Str.append(crs.getString("cart_id")).append(",");
					Str.append("'update',0);setColorById(this.id);\"");
					Str.append(">\n");

					Str.append(item_name);
					Str.append("</td>\n<td align=\"center\">\n");
					Str.append(crs.getDouble("cart_alt_qty"));
					Str.append("</td>\n");

					Str.append("<td align=\"left\">\n");
					Str.append(crs.getString("uom_name"));
					Str.append("</td>");
					Str.append("\n<td align=\"right\">\n");
					Str.append(df.format(item_price * uom_ratio));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("discount")));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("tax")));
					Str.append("</td>\n");
					Str.append("<td align=\"right\">\n");
					Str.append(df.format(total));
					Str.append("</td>\n");

					Str.append("</tr>\n");
					// SOP(Str.toString());
				}
				updatecount += count;
			}

			Str.append("<tr>\n<td valign=top align=\"right\"></td>\n");
			Str.append("<td valign=top align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=top align=\"right\"><b>Total:</b></td>\n");
			Str.append("<td valign=top align=\"right\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"");
			Str.append(invoice_qty).append("\"><b>").append(invoice_qty).append("</b></td>\n");

			Str.append("<td valign=top align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=top align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=top align=\"right\"><b>").append(df.format(total_discount)).append("</b></td>\n");
			Str.append("<td valign=top align=\"right\"><b>").append(df.format(total_tax)).append("</b></td>\n");
			Str.append("<td valign=top align=\"right\">\n");
			Str.append("<b>" + df.format(invoice_total)).append("</b></td>\n");

			Str.append("</tr>\n");
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					bs_invoice_amt = crs1.getDouble("cart_amount");
					bs_invoice_total += bs_invoice_amt;

					Str.append("<tr valign=\"top\">");

					if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
						Str.append("<td valign=top colspan=\"6\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
						if (crs1.getString("tax_name").toLowerCase().contains("discount")) {
							Str.append("<td valign=top align=\"right\">").append(crs1.getString("tax_rate") + "%").append("</td><td></td>");
							Str.append("<td valign=top align=\"right\">").append("-" + df.format(crs1.getDouble("cart_amount"))).append("</td>");
							bs_invoice_total = bs_invoice_total - (crs1.getDouble("cart_amount") * 2);
						} else {
							Str.append("<td></td><td valign=top align=\"right\">").append(crs1.getString("tax_rate") + "%").append("</td>");
							Str.append("<td valign=top align=\"right\">").append(df.format(crs1.getDouble("cart_amount"))).append("</td>");
							Str.append("</tr>");
						}
					} else {
						Str.append("<td valign=top colspan=\"7\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
						Str.append("<td valign=top align=\"right\">").append(crs1.getString("tax_rate") + "%").append("</td>");
						Str.append("<td valign=top align=\"right\">").append(df.format(crs1.getDouble("cart_amount"))).append("</td>");
						Str.append("</tr>");
					}
				}
			}

			Str.append("<input type=\"hidden\" name=\"txt_updatecount\" id=\"txt_updatecount\" value=\"");
			Str.append(updatecount).append("\">");
			Str.append("<input type=\"hidden\" name=\"txt_invoice_total\" id=\"txt_invoice_total\" value=\"");
			Str.append(df.format(invoice_total)).append("\">\n");

			invoice_grandtotal = invoice_total + bs_invoice_total;

			if (vouchertype_roundoff.equals("1")) {
				double roundedAmount = (double) Math.round(invoice_grandtotal) - invoice_grandtotal;
				if (roundedAmount != 0) {
					invoice_grandtotal = invoice_grandtotal + roundedAmount;
					Str.append("<tr><td valign=top colspan=8 align=left><b>Round Off</b></td>");
					Str.append("<td align=right><input id='round_off' name='round_off' value='" + df.format(roundedAmount) + "' hidden/> <b>" + df.format(roundedAmount) + "</b></td></tr>");
				}
			}
			// Str.append(Listdata());
			Str.append("<tr valign=\"top\">\n");

			if (vouchertype_id.equals("7")) {
				Str.append("<td valign=top colspan='6' align='left'>"
						+ "<input type='button' id='delete_full_cart' name='delete_full_cart' class='btn btn-success' value='Delele Cart' onclick=\"DeleteFullCart();\"/>");

				Str.append("<a id='billsundry' name='billsundry' type='button' data-toggle=\"modal\" data-target=\"#Hintclicktocall\" class='btn btn-success' value='Bill Sundry'"
						+ " href='../accounting/bill-sundry.jsp?so=yes&add=yes"
						+ "&cart_session_id=" + cart_session_id
						+ "&vouchertype_id=" + vouchertype_id
						+ "&voucher_id=" + voucher_id
						+ "&total=" + invoice_total + "'"
						+ " value='Bill Sundry'>Bill Sundry</a></td>");
			} else if (vouchertype_id.equals("9")) {
				Str.append("<td valign=top colspan='6' align='left'>"
						+ "<input type='button' id='delete_full_cart' name='delete_full_cart' class='btn btn-success' value='Delete Cart' onclick=\"DeleteFullCart();\"/>");
				Str.append("<input type='button' id='billsundry' name='billsundry' data-toggle='model' data-target='#Hintclicktocall' class='btn btn-success' value='Bill Sundry'"
						+ " onclick=\"BillSundry('" + vouchertype_id + "','" + cart_session_id + "','" + voucher_id + "','" + invoice_total + "');return false;\"/></td>");

			} else {
				Str.append("<td valign=top colspan='6' align='left'>"
						+ "<input type='button' id='delete_full_cart' name='delete_full_cart' class='btn btn-success' value='Delele Cart' onclick=\"DeleteFullCart();\"/>");

				if (so.equals("yes")) {
					Str.append("<a id='billsundry' name='billsundry' type='button' data-toggle=\"modal\" data-target=\"#Hintclicktocall\" class='btn btn-success' value='Bill Sundry'"
							+ " href='../accounting/bill-sundry.jsp?so=yes&add=yes"
							+ "&cart_session_id=" + cart_session_id
							+ "&vouchertype_id=" + vouchertype_id
							+ "&voucher_id=" + voucher_id
							+ "&total=" + invoice_total + "'"
							+ " value='Bill Sundry'>Bill Sundry</a></td>");
					SOP("bbbb");
				} else {
					Str.append("<a id='billsundry' name='billsundry' type='button' data-toggle=\"modal\" data-target=\"#Hintclicktocall\" class='btn btn-success' value='Bill Sundry'"
							+ " href='../accounting/bill-sundry.jsp?po=yes&add=yes"
							+ "&cart_session_id=" + cart_session_id
							+ "&vouchertype_id=" + vouchertype_id
							+ "&voucher_id=" + voucher_id
							+ "&total=" + invoice_total + "'"
							+ " value='Bill Sundry'>Bill Sundry</a></td>");
					SOP("ccc");
				}
			}

			Str.append("<td colspan='2' valign=top align='right'><b>Grand Total: </b></td>");
			Str.append("<td valign=top align='right'><b>").append(df.format(invoice_grandtotal)).append("</b></td>");

			Str.append("</tr><input type='text' name='txt_grand_total' id='txt_grand_total' value='" + invoice_grandtotal + "' hidden>\n");
			crs1.close();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</tbody></table></div>\n");
		return Str.toString();
	}
	public String Listdata() {
		int count = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT cart_id, customer_name AS tax_name, customer_rate AS tax_rate, cart_amount"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
				+ " WHERE 1=1" + " AND cart_vouchertype_id = " + vouchertype_id + ""
				+ " AND cart_rowcount = 0" + " AND cart_option_id = 0";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top width=\"5%\" align=center>").append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("tax_name")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("tax_rate")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("cart_amount")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				return "";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doGet(request, response);
	}

	public String PopulateUOM(String uom_id, String alt_uom_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT  uom_id, uom_ratio, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE 1=1"
					// + " AND (uom_id = 1 OR uom_parent_id =1)"
					+ " AND (uom_id = " + uom_id + " "
					+ " OR uom_parent_id = " + uom_id + ")" + " GROUP BY CONCAT(uom_name,uom_ratio)"
					+ " ORDER BY uom_id";
			// SOP("StrSql=po=uom11="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_alt_uom_id\" id=\"dr_alt_uom_id\" class=\"form-control\" style=\"width:150px\" onChange=\"CalItemTotal(" + para + ");\">");
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
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
