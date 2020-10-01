package axela.accounting;

//aJIt 20th November, 2012
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

public class Returns_Details extends Connect {

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
	public String cart_convfactor = "1";
	public double cart_disc = 0.00, cart_discpercent = 0.00;
	public double cart_unit_cost = 0.00;
	public String cart_time = "";
	public String price_sales_customer_id = "0";
	public String price_discount1_customer_id = "0";
	public String tax1 = "", tax1_name = "";
	public String tax2 = "", tax2_name = "";
	public String tax3 = "", tax3_name = "";
	public String tax_rate1 = "0.00";
	public String tax_rate2 = "0.00";
	public String tax_rate3 = "0.00";
	public String tax_id1 = "0";
	public String tax_id2 = "0";
	public String tax_id3 = "0";
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
	public String cart_ledger_tax_dc = "", cart_alt_uom_id = "0", uom_ratio = "1", cart_disc_type = "", discpercentarr = "0.00", discpercent = "0.00";
	// Variable to check duplicate items
	public String cart_dup = "0", cart_dup_item_id = "0", cart_dup_multivoucher_id = "0", temp_cart_voucher_id = "0", cart_multivoucher_id = "0";
	public String from_voucher_quantity = "0.00";
	public double from_voucher_qty = 0.00, cart_item_price = 0.00;
	// -----
	public String vouchertype_name = "", branch_id = "0";

	// variables not required .....use when required
	public String cart_opitem_item_id = "0", cart_item_serial = "", cart_item_batch = "", stockserial_serial_no = "";
	public String cart_option_group = "";
	public String cart_opitem_price = "0.00", cart_opitem_netprice = "0.00", configitems_total = "0.00";
	public String cart_opitem_qty = "0";
	public String updatecount = "", cart_item_code = "", tax = "", uom_id = "0";

	public double tax1_rate1 = 0.00, tax2_rate2 = 0.00, tax3_rate3 = 0.00;
	public String voucher_dcr_request_id = "0", voucher_dcr_id = "0", voucher_grn_return_id = "0";
	public String inv_vouchertype_id = "0", inv_voucherclass_id = "0";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		if (!GetSession("emp_id", request).equals("")) {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				refresh = PadQuotes(request.getParameter("refresh"));
				SetSession("refresh", refresh, request);
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request));
				config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request));
				// SOP("config_inventory_current_stock====" + config_inventory_current_stock);

				para = PadQuotes(request.getParameter("para"));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("cart_vouchertype_id")));
				if (vouchertype_id.equals("23") || vouchertype_id.equals("4") || vouchertype_id.equals("116")) {
					inv_vouchertype_id = "6";
				} else if (vouchertype_id.equals("24") || vouchertype_id.equals("117")) {
					inv_vouchertype_id = "21";
				}
				if (vouchertype_id.equals("23")) {
					cart_cust_disc_dc = "0";
					cart_ledger_tax_dc = "1";
				} else {
					cart_cust_disc_dc = "1";
					cart_ledger_tax_dc = "0";
				}

				voucher_id = CNumeric(PadQuotes(request.getParameter("cart_voucher_id")));
				voucher_dcr_request_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_request_id")));
				// SOP("voucher_dcr_request_id===" + voucher_dcr_request_id);
				voucher_dcr_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_id")));
				voucher_grn_return_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_return_id")));

				cart_session_id = PadQuotes(request.getParameter("cart_session_id"));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				cart_multivoucher_id = CNumeric(PadQuotes(request.getParameter("cart_multivoucher_id")));
				cart_item_id = CNumeric(PadQuotes(request.getParameter("cart_item_id")));
				cart_item_name = PadQuotes(request.getParameter("item_name"));
				// SOP("cart_item_name===" + cart_item_name);
				cart_item_code = PadQuotes(request.getParameter("item_code"));
				// SOP("cart_item_code===" + cart_item_code);
				cart_price = CNumeric(PadQuotes(request.getParameter("cart_price")));
				// SOP("cart_price===" + cart_price);
				if (!cart_price.equals("0")) {
					cart_item_price = Double.parseDouble(cart_price);
				}
				cart_amount = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("cart_amount"))));
				// SOP("cart_amount==123=" + cart_amount);
				cart_alt_qty = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("cart_alt_qty"))));
				// SOP("cart_alt_qty===" + cart_alt_qty);
				// if (!qty.equals("0")) {
				// cart_alt_qty = Double.parseDouble(qty);
				// }
				cart_alt_uom_id = CNumeric(PadQuotes(request.getParameter("cart_alt_uom_id")));
				// SOP("cart_alt_uom_id===" + cart_alt_uom_id);
				uom_id = CNumeric(PadQuotes(request.getParameter("cart_uom_id")));
				if (!cart_alt_uom_id.equals("0")) {
					uom_ratio = ExecuteQuery("SELECT uom_ratio" + " FROM " + compdb(comp_id) + "axela_inventory_uom" + " WHERE uom_id = " + cart_alt_uom_id + "");
					cart_uom_ratio = Double.parseDouble(uom_ratio);
				}
				if (cart_uom_ratio != 0.0) {
					cart_qty = cart_alt_qty * cart_uom_ratio;
				}

				// SOP("cart_qty===" + cart_qty);
				// The above code not required as cart_qty is getting directly still to be checked
				// cart_qty = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("cart_qty"))));
				ledger_id = CNumeric(PadQuotes(request.getParameter("ledger_id")));
				price_sales_customer_id = CNumeric(PadQuotes(request.getParameter("price_sales_customer_id")));
				if (!price_sales_customer_id.equals(ledger_id) && !ledger_id.equals("0")) {
					price_sales_customer_id = ledger_id;
				}
				discpercent = CNumeric(PadQuotes(request.getParameter("cart_discpercent")));
				// SOP("discpercent===" + discpercent);
				price_discount1_customer_id = CNumeric(PadQuotes(request.getParameter("price_discount1_customer_id")));
				// SOP("price_discount1_customer_id===" + price_discount1_customer_id);
				if (!discpercent.equals("0")) {
					cart_discpercent = Double.parseDouble(discpercent);
				}
				if (cart_discpercent != 0.0) {
					cart_disc = ((cart_item_price) * cart_discpercent) / 100;
					// SOP("cart_disc==1=" + cart_disc);
				}
				// Item Discount Amount
				cart_disc_amount = cart_disc * cart_qty;
				// SOP("cart_disc_amount==1=" + cart_disc_amount);

				tax1_name = PadQuotes(request.getParameter("tax1_name"));
				tax2_name = PadQuotes(request.getParameter("tax2_name"));
				tax3_name = PadQuotes(request.getParameter("tax3_name"));
				tax_rate1 = CNumeric(PadQuotes(request.getParameter("tax_rate1")));
				tax1_rate1 = Double.parseDouble(tax_rate1);
				tax_rate2 = CNumeric(PadQuotes(request.getParameter("tax_rate2")));
				tax2_rate2 = Double.parseDouble(tax_rate2);
				tax_rate3 = CNumeric(PadQuotes(request.getParameter("tax_rate3")));
				tax3_rate3 = Double.parseDouble(tax_rate3);
				// cart_disc_type = CNumeric(PadQuotes(request.getParameter("disc_type")));
				cart_unit_cost = cart_item_price - cart_disc;
				tax_customer_id1 = CNumeric(PadQuotes(request.getParameter("tax_customer_id1")));
				tax_customer_id2 = CNumeric(PadQuotes(request.getParameter("tax_customer_id2")));
				tax_customer_id3 = CNumeric(PadQuotes(request.getParameter("tax_customer_id3")));
				tax_id1 = CNumeric(PadQuotes(request.getParameter("tax_id1")));
				tax_id2 = CNumeric(PadQuotes(request.getParameter("tax_id2")));
				tax_id3 = CNumeric(PadQuotes(request.getParameter("tax_id3")));
				price_tax2_after_tax1 = CNumeric(PadQuotes(request.getParameter("price_tax2_after_tax1")));
				price_tax3_after_tax2 = CNumeric(PadQuotes(request.getParameter("price_tax3_after_tax2")));
				boxtype_size = CNumeric(PadQuotes(request.getParameter("cart_boxtype_size")));
				cart_time = PadQuotes(request.getParameter("cart_time"));
				rowcount = PadQuotes(request.getParameter("rowcount"));
				cart_option_id = CNumeric(PadQuotes(request.getParameter("cart_option_id")));
				mode = PadQuotes(request.getParameter("mode"));
				status = PadQuotes(request.getParameter("status"));
				cart_id = CNumeric(PadQuotes(request.getParameter("cart_id")));
				list_cartitems = PadQuotes(request.getParameter("list_cartitems"));
				// SOP("list_cartitems===" + list_cartitems);
				configure = PadQuotes(request.getParameter("configure"));
				// Item Details Ajax call configure1 = yes
				configure1 = PadQuotes(request.getParameter("configure1"));
				add_cartitem = PadQuotes(request.getParameter("add_cartitem"));
				update_cartitem = PadQuotes(request.getParameter("update_cartitem"));
				delete_cartitem = PadQuotes(request.getParameter("delete_cartitem"));
				delete_full_cart = PadQuotes(request.getParameter("delete_full_cart"));

				// Variables not required ....but use when required
				// cart_item_serial = PadQuotes(request.getParameter("cart_item_serial"));
				// cart_item_batch = PadQuotes(request.getParameter("cart_item_batch"));
				// cart_opitem_item_id = CNumeric(PadQuotes(request.getParameter("cart_opitem_item_id")));
				// item_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("item_ticket_dept_id")));
				// cart_option_group = PadQuotes(request.getParameter("cart_option_group"));
				// cart_convfactor = CNumeric(PadQuotes(request.getParameter("cart_convfactor")));
				// cart_opitem_price = CNumeric(PadQuotes(request.getParameter("cart_opitem_price")));
				// cart_opitem_netprice = CNumeric(PadQuotes(request.getParameter("cart_opitem_netprice")));
				// configitems_total = CNumeric(PadQuotes(request.getParameter("configitems_total")));
				// cart_opitem_qty = CNumeric(PadQuotes(request.getParameter("cart_opitem_qty")));
				// //////-------end----------------///////////

				// ///To check item details from converting one voucher to another
				// voucher
				if (!cart_item_id.equals("0") && !cart_session_id.equals("")) {
					StrSql = "SELECT cart_item_id, cart_multivoucher_id FROM " + compdb(comp_id) + "axela_acc_cart"
							+ " WHERE 1=1 "
							+ " AND cart_vouchertype_id = " + vouchertype_id
							+ " AND cart_item_id = " + cart_item_id
							+ " AND cart_emp_id = " + emp_id
							+ " AND cart_session_id = " + cart_session_id;
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						cart_dup_multivoucher_id = crs.getString("cart_multivoucher_id");
						cart_dup_item_id = crs.getString("cart_item_id");
						if (cart_dup_item_id.concat(cart_dup_multivoucher_id).equals("" + cart_item_id.concat(cart_multivoucher_id) + "")) {
							cart_dup = "1";
						}
					}
					// SOP("StrSql==dup=="+StrSql);
				}

				// -------------------- Query to get from voucher qty---------------
				// if (!voucher_dcr_request_id.equals("0")) {
				// temp_cart_voucher_id = voucher_dcr_request_id;
				// } else if (!voucher_dcr_id.equals("0")) {
				// temp_cart_voucher_id = voucher_dcr_id;
				// } else if (!voucher_grn_return_id.equals("0")) {
				// temp_cart_voucher_id = voucher_grn_return_id;
				// }
				// so that we check current voucher qty should not exceed from
				// voucher qty
				// if(!temp_cart_voucher_id.equals("0")){
				// StrSql = "SELECT vouchertype_name FROM  " + compdb(comp_id) + "axela_acc_voucher_type"
				// +
				// " INNER JOIN  "+compdb(comp_id)+"axela_acc_voucher on voucher_vouchertype_id = vouchertype_id"
				// + " WHERE 1=1 "
				// + " AND voucher_id = "+ temp_cart_voucher_id;
				// // SOP("StrSql==vouchertype_name="+StrSql);
				// vouchertype_name = ExecuteQuery(StrSql);
				//
				// StrSql =
				// " SELECT COALESCE(SUM(vouchertrans_alt_qty),0) FROM axela_acc_voucher_trans"
				// + " WHERE 1=1 "
				// + " AND vouchertrans_voucher_id = "+ temp_cart_voucher_id
				// + " AND vouchertrans_item_id =" + cart_item_id;
				// // SOP("StrSql==from_voucher_quantity="+StrSql);
				// from_voucher_quantity = CNumeric(ExecuteQuery(StrSql));
				// if(!from_voucher_quantity.equals("")){
				// from_voucher_qty = Double.parseDouble(from_voucher_quantity);
				// }
				// }
				// ///////////////////////END///////////////////////////////////////////
				// modelvalue = PadQuotes(request.getParameter("modelvalue"));
				// model = PadQuotes(request.getParameter("model"));

				if (!cart_item_id.equals("0") && configure1.equals("yes")) {
					// StrHTML = getItemDetails(location_id, cart_item_id, emp_id, cart_session_id, mode);
					StrHTML = getItemDetails();
				}

				if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes")
						&& cart_dup.equals("1")) {
					msg = "Item Already Present!";
					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes")
						&& msg.equals("")
						// && config_inventory_current_stock.equals("1")
						&& !location_id.equals("0")) {
					AddCartItem();
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && add_cartitem.equals("yes")
						&& msg.equals("")
						// && config_inventory_current_stock.equals("1")
						&& !location_id.equals("0")) {
					AddCartItem();

				}
				else if (!cart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("") && config_inventory_current_stock.equals("1") && !location_id.equals("0")) {
					UpdateCartItem();
				} else if (!cart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("")
				// && config_inventory_current_stock.equals("0")
				// && location_id.equals("0")
				) {
					UpdateCartItem();
				} else if (!emp_id.equals("0") && !cart_session_id.equals("0") && list_cartitems.equals("yes")) {
					StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
				} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0") && config_inventory_current_stock.equals("1")
						&& !location_id.equals("0")) {
					DeleteCartItem();
				} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0")
						// && config_inventory_current_stock.equals("0")
						&& location_id.equals("0")) {
					DeleteCartItem();
				} else if (delete_full_cart.equals("yes") && !emp_id.equals("0") && !cart_session_id.equals("0")) {
					DeleteCartAllItems();
				}
				// else if (!cart_item_id.equals("0") && configure.equals("yes")) {
				// GetConfigurationDetails();
				// }
			}
		} else {
			StrHTML = "SignIn";
		}
	}
	public void AddCartItem() throws SQLException {
		double cart_truckspace = 0.00;
		double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
		double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
		double tax_value1 = 0.00, tax_value2 = 0.00, tax_value3 = 0.00;
		double cart_amount_tax = 0.00;
		String order = "";
		String rateclass = "";

		cart_amount_tax = cart_item_price - cart_disc;
		tax1_amount = ((cart_amount_tax * cart_qty * tax1_rate1) / 100);// including qty
		tax1_price = ((cart_amount_tax * tax1_rate1) / 100);

		if (price_tax2_after_tax1.equals("1") && tax1_amount != 0.0) {
			tax2_amount = ((cart_amount_tax + tax1_price) * cart_qty * tax2_rate2 / 100);
			tax2_price = ((cart_amount_tax + tax1_price) * tax2_rate2 / 100);
		} else {
			tax2_amount = (cart_amount_tax * cart_qty * tax2_rate2 / 100);
			tax2_price = ((cart_amount_tax) * tax2_rate2 / 100);
		}
		if (price_tax3_after_tax2.equals("1") && tax1_amount != 0.0 && tax2_amount != 0.0) {
			tax3_amount = ((cart_amount_tax + tax1_price + tax2_price) * cart_qty * tax3_rate3 / 100);
			tax3_price = ((cart_amount_tax + tax1_price + tax2_price) * tax3_rate3 / 100);
		} else {
			tax3_amount = (cart_amount_tax * cart_qty * tax3_rate3 / 100);
			tax3_price = ((cart_amount_tax) * tax3_rate3 / 100);
		}

		// truck space calculation
		if (Double.parseDouble(boxtype_size) != 0.0) {
			cart_truckspace = ((cart_alt_qty / Double.parseDouble(boxtype_size)) * 100);
		}

		if (rowcount.equals("yes")) {
			cart_rowcount = ExecuteQuery("SELECT COALESCE(MAX(cart_rowcount), 0) + 1" + " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1" + " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_emp_id = "
					+ emp_id + " AND cart_session_id = " + cart_session_id);
			cart_option_id = "0";
		} else if (rowcount.equals("no")) {
			cart_rowcount = "0";
			if (cart_id.equals("0")) {
				StrSql = "SELECT COALESCE(MAX(cart_rowcount), 0)" + " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1" + " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				cart_option_id = ExecuteQuery(StrSql);
				// SOP("cart_option_id===="+cart_option_id);
			} else {
				cart_option_id = ExecuteQuery("SELECT COALESCE(cart_rowcount, 0)" + " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_id = " + cart_id);
			}
			cart_option_id = CNumeric(cart_option_id);
		}
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (!cart_rowcount.equals("0")) {
					// if (!price_sales_customer_id.equals("0")) {
					SOP("cart_cust_disc_dc====" + cart_cust_disc_dc);
					SOP("cart_ledger_tax_dc===" + cart_ledger_tax_dc);

					if (!cart_rowcount.equals("0") && cart_option_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
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
								+ " " + cart_multivoucher_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + location_id + "',"
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
								+ " " + cart_unit_cost + ","
								+ " " + cart_alt_qty + ","
								+ " " + cart_alt_uom_id + ","
								+ " " + cart_convfactor + ","
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'" + " )";
						// SOP("StrSql==cart111=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
						// }
					}

					if (cart_disc != 0.0 || cart_disc == 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
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
								+ " " + price_discount1_customer_id + ","
								+ " " + vouchertype_id + ","
								+ " " + cart_multivoucher_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + location_id + "',"
								+ " " + cart_item_id + ","
								+ " '1',"
								+ " " + cart_discpercent + ","
								+ " '0',"
								+ " 0,"
								+ " " + cart_rowcount + ","
								+ " " + cart_rowcount + ","
								+ " '',"
								+ " '',"
								+ " 0,"
								+ " " + cart_disc + ","
								+ " 0,"
								+ " " + cart_disc_amount + ","
								+ " 0.00,"
								+ " 0,"
								+ " 0,"
								+ " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_cust_disc_dc + "'"
								+ ")";
						// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}

					// credit tax ledger
					// for (int i = 1; i <= 3; i++) {
					// if (!("tax_customer_id" + i).equals("0")) {
					if (tax1_amount != 0.0 || tax1_amount == 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
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
								+ " cart_time," + " cart_dc)" + " VALUES" + " ("
								+ " " + voucher_id + ","
								+ " " + tax_customer_id1 + ","
								+ " " + vouchertype_id + ","
								+ " " + cart_multivoucher_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + location_id + "',"
								+ " " + cart_item_id + ","
								+ " '0'," + " 0.0," + " '1',"
								+ " " + tax_id1 + ","
								+ " " + cart_rowcount + ","
								+ " " + cart_rowcount + ","
								+ " '',"
								+ " '',"
								+ " 0,"
								+ " " + tax1_price + ","
								+ " 0.00,"
								+ " " + tax1_amount + ","
								+ " 0.00,"
								+ " 0,"
								+ " 0,"
								+ " 0,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + cart_ledger_tax_dc + "'"
								+ ")";
						// SOP("StrSql==cart333=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
					if (tax2_amount != 0.0 || tax2_amount == 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
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
								+ " " + tax_customer_id2 + ","
								+ " " + vouchertype_id + ","
								+ " " + cart_multivoucher_id + ","
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
								+ " '" + cart_ledger_tax_dc + "'"
								+ ")";
						// SOP("StrSql==cart444=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}

					if (tax3_amount != 0.0 || tax3_amount == 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
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
								+ " " + tax_customer_id3 + ","
								+ " " + vouchertype_id + ","
								+ " " + cart_multivoucher_id + ","
								+ " " + emp_id + ","
								+ " " + cart_session_id + ","
								+ " '" + location_id + "',"
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
								+ " '" + cart_ledger_tax_dc + "'"
								+ ")";
						// SOP("StrSql==cart555=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}

					// }
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
	}

	public void UpdateCartItem() throws SQLException {
		try {

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			double cart_truckspace = 0.00;
			double tax1_amount = 0.00, tax2_amount = 0.00, tax3_amount = 0.00;
			double tax1_price = 0.00, tax2_price = 0.00, tax3_price = 0.00;
			double cart_amount_tax = 0.00;

			cart_amount_tax = (cart_item_price + Double.parseDouble(configitems_total)) - cart_disc;
			tax1_price = ((cart_amount_tax) * Double.parseDouble(tax_rate1) / 100);
			tax1_amount = tax1_price * cart_qty;

			if (price_tax2_after_tax1.equals("1") && tax1_amount != 0.0) {
				tax2_price = ((cart_amount_tax + tax1_price) * tax2_rate2 / 100);
				tax2_amount = tax2_price * cart_qty;
			} else {
				tax2_price = ((cart_amount_tax) * tax2_rate2 / 100);
				tax2_amount = tax2_price * cart_qty;
			}
			if (price_tax3_after_tax2.equals("1") && tax1_amount != 0.0 && tax2_amount != 0.0) {
				tax3_price = ((cart_amount_tax + tax1_price + tax2_price) * tax3_rate3 / 100);
				tax3_amount = tax3_price * cart_qty;
			} else {
				tax3_price = ((cart_amount_tax) * tax3_rate3 / 100);
				tax3_amount = tax3_price * cart_qty;
			}

			// truck space calculation
			if (Double.parseDouble(boxtype_size) != 0.0) {
				cart_truckspace = ((cart_alt_qty / Double.parseDouble(boxtype_size)) * 100);
			}

			StrSql = "SELECT COALESCE(cart_rowcount, 0) AS cart_rowcount,"
					+ " COALESCE(cart_option_id, 0) AS cart_option_id"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_id = " + cart_id;
			// SOP("StrSql==rowcount=="+StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				cart_rowcount = crs1.getString("cart_rowcount");
				cart_option_id = crs1.getString("cart_option_id");
			}
			crs1.close();
			// SOP("cart_option_id====" + cart_option_id);
			if (!cart_rowcount.equals("0") && cart_option_id.equals("0")) {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_cart"
						+ " SET"
						+ " cart_location_id = " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + cart_price + ","
						+ " cart_qty = " + cart_qty + ","
						+ " cart_alt_qty = " + cart_alt_qty + ","
						+ " cart_alt_uom_id = " + cart_alt_uom_id + ","
						+ " cart_truckspace = " + cart_truckspace + ","
						+ " cart_netprice = " + cart_netprice + ","
						+ " cart_amount = " + cart_amount + ","
						+ " cart_time = " + ToLongDate(kknow()) + ""
						+ " WHERE 1=1 "
						+ " AND cart_rowcount = " + cart_rowcount + ""
						+ " AND cart_option_id = " + cart_option_id
						+ " AND cart_discount = 0"
						+ " AND cart_tax = 0"
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==item price=" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			if (cart_disc != 0.0 || cart_disc == 0.0) {
				String temp_cart_discount = ExecuteQuery("SELECT cart_discount"
						+ " FROM " + compdb(comp_id) + " axela_acc_cart"
						+ " WHERE cart_option_id = " + cart_rowcount + ""
						+ " AND cart_discount = 1"
						+ " AND cart_tax = 0");
				// SOP("temp_cart_discount==="+temp_cart_discount);
				if (!CNumeric(temp_cart_discount).equals("0")) {
					StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_cart"
							+ " SET"
							+ " cart_location_id = " + location_id + ","
							+ " cart_item_id = " + cart_item_id + ","
							+ " cart_discount_perc = " + cart_discpercent + ","
							+ " cart_price = " + cart_disc + ","
							+ " cart_amount = " + cart_disc_amount + ","
							+ " cart_time = " + ToLongDate(kknow())
							+ " WHERE 1 = 1"
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
							+ " cart_vouchertype_id,"
							+ " cart_emp_id,"
							+ " cart_session_id,"
							+ " cart_location_id,"
							+ " cart_item_id,"
							+ " cart_discount,"
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
							+ " VALUES"
							+ " (" + price_discount1_customer_id + ","
							+ " " + vouchertype_id + ","
							+ " " + emp_id + ","
							+ " " + cart_session_id + ","
							+ " '" + location_id + "',"
							+ " " + cart_item_id + ","
							+ " '1',"
							+ " '0',"
							+ " 0,"
							+ " " + cart_rowcount + ","
							+ " " + cart_rowcount + ","
							+ " '',"
							+ " '',"
							+ " 0,"
							+ " " + cart_disc + ","
							+ " 0,"
							+ " " + cart_disc_amount + ","
							+ " 0.00,"
							+ " 0,"
							+ " 0,"
							+ " 0,"
							+ " " + ToLongDate(kknow()) + ","
							+ " '" + cart_cust_disc_dc + "'" + ")";
					// SOP("StrSql==cart222=" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
			}
			if (tax1_amount != 0.0 || tax1_amount == 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart"
						+ " SET"
						+ " cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax1_price + ","
						+ " cart_amount = " + tax1_amount + ","
						+ " cart_time = " + ToLongDate(kknow())
						+ " WHERE 1=1"
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id1
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==tax1=" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax2_amount != 0.0 || tax2_amount == 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart"
						+ " SET" + " cart_location_id= " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax2_price + ","
						+ " cart_amount = " + tax2_amount + ","
						+ " cart_time = " + ToLongDate(kknow())
						+ " WHERE 1=1"
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id2
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==tax2=" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (tax3_amount != 0.0 || tax3_amount == 0.0) {
				StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_cart"
						+ " SET"
						+ " cart_location_id = " + location_id + ","
						+ " cart_item_id = " + cart_item_id + ","
						+ " cart_price = " + tax3_price + ","
						+ " cart_amount = " + tax3_amount + ","
						+ " cart_time = " + ToLongDate(kknow())
						+ " WHERE 1 = 1"
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_tax = 1"
						+ " AND cart_tax_id = " + tax_id3
						+ " AND cart_option_id = " + cart_rowcount
						+ " AND cart_vouchertype_id = " + vouchertype_id
						+ " AND cart_emp_id = " + emp_id
						+ " AND cart_session_id = " + cart_session_id;
				// SOP("StrSql==tax3=" + StrSqlBreaker(StrSql));
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

			cart_option_id = ExecuteQuery("SELECT cart_rowcount"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_id = " + cart_id
					+ " AND cart_vouchertype_id = " + vouchertype_id);
			// SOP("cartoptionid===" + cart_option_id);
			// SOP("cart_id===" + cart_id);
			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_option_id = " + cart_option_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			stmttx.addBatch(StrSql);

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_id = " + cart_id
					+ " AND cart_session_id = " + cart_session_id
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

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_emp_id =" + emp_id
					+ " AND cart_session_id =" + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			// SOP("StrSql==DeleteCartAllItems=="+StrSql);
			updateQuery(StrSql);

			StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		} catch (Exception e) {

			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}

	}

	public String getItemDetails() {
		int count = 0, price_disc_type = 0;
		int para = 0;
		String item_serial = "0";
		String item_batchno = "0";
		String optioncount = "0";
		double item_unit_price = 0.00, itemprice = 0.00, discount = 0.00, quantity = 0.00;
		double cart_nettotal = 0.00;
		double cart_total = 0.00;
		String strconfigoptions = "";
		double temp_item_unit_price = 0.0;
		double temp_item_discount = 0.0;
		String price_tax2_after_tax1 = "0";
		String price_tax3_after_tax2 = "0";
		double tax_value1 = 0.00, tax_value2 = 0.00, tax_value3 = 0.00;
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<div class=\"table table-bordered\" style=\"overflow-x:scroll;width:580px\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" id=\"getItemDetails\" data-filter=\"#filter\">\n");
			// Str.append("<table  class=\"table table-hover table-boardered\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th align=\"center\" colspan=\"6\"><div style=\"display:inline\" id=\"item_name\"> ").append("(" + cart_item_code + ") " + cart_item_name)
					.append("</div>\n");
			Str.append("</th>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n");
			Str.append("<th align=\"center\" valign=\"top\">Quantity:</th>\n");
			Str.append("<th align=\"center\" valign=\"top\">UOM:</th>\n");
			Str.append("<th align=\"center\" valign=\"top\">Price:</th>\n");
			Str.append("<th align=\"center\" valign=\"top\">Discount: </th>\n");
			Str.append("<th align=\"center\" valign=\"top\" nowrap>Tax:</th>\n");
			Str.append("<th align=\"center\" valign=\"top\"><b>Total:</b></th>\n");
			Str.append("</tr></thead>\n");
			Str.append("<tbody>\n");
			count++;

			// price_tax2_after_tax1 = crs.getString("price_tax2_after_tax1");
			// price_tax3_after_tax2 = crs.getString("price_tax3_after_tax2");

			if (mode.equals("add")) {
				// cart_alt_uom_id = crs.getString("item_uom_id");
				// uom_ratio = ExecuteQuery("SELECT COALESCE(uom_ratio, 0) AS uom_ratio" + " FROM axela_inventory_uom" + " WHERE uom_id = " + CNumeric(cart_alt_uom_id) + "");
				// uom_ratio = CNumeric(uom_ratio);
				// if (!uom_ratio.equals("0")) {
				// cart_uom_ratio = Double.parseDouble(uom_ratio);
				// }
				quantity = cart_alt_qty * cart_uom_ratio;
				// SOP("quantity==add=" + quantity);
				item_unit_price = Double.parseDouble(cart_price);
				// SOP("item_unit_price==add=" + item_unit_price);
				itemprice = (item_unit_price * quantity);
				// SOP("itemprice==add=" + itemprice);
				// SOP("price_disc_type==="+price_disc_type);
				// SOP("discpercent=1=="+discpercent);
				if (cart_discpercent != 0.0) {
					discount = ((itemprice) * cart_discpercent) / 100;
					// SOP("discount==add=" + discount);
				}

			} else if (mode.equals("update")) {
				quantity = cart_alt_qty * cart_uom_ratio;
				item_unit_price = Double.parseDouble(cart_price);
				itemprice = item_unit_price * quantity;
				// SOP("itemprice=update==" + itemprice);
				if (cart_discpercent != 0.0) {
					discount = ((itemprice) * cart_discpercent) / 100;
					// SOP("discount==update=" + discount);
				}
			}

			// Populate Configured Items don't delete
			// if (optioncount.equals("1")) {
			// strconfigoptions = GetConfigurationDetails();
			// }

			cart_amount = (item_unit_price - (discount / quantity)) * quantity;

			if (tax1_rate1 != 0.00) {
				// tax_value1 = ((itemprice + optiontotal - discount) *
				// quantity
				// * tax1_rate1 / 100);
				// Note- We are calculating for Alt Uom...
				tax_value1 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax1_rate1 / 100));
				// SOP("tax_value1===" + tax_value1);
			}
			if (tax2_rate2 != 0.00) {
				if ((price_tax2_after_tax1.equals("1")) && (tax_value1 != 0)) {
					tax_value2 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1) * (tax2_rate2 / 100));
				} else {
					tax_value2 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax2_rate2 / 100));
				}
				// SOP("tax_value2===" + tax_value2);
			}
			if (tax3_rate3 != 0.00) {
				if ((price_tax3_after_tax2.equals("1")) && (tax_value1 != 0) && (tax_value2 != 0)) {
					tax_value3 = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1 + tax_value2) * (tax3_rate3 / 100));
				} else {
					tax_value3 = ((item_unit_price - (discount / quantity)) * cart_uom_ratio * (tax3_rate3 / 100));
				}
				// SOP("tax_value3===" + tax_value3);
			}
			// Total amount for No. Of Qty's.
			cart_total = ((((item_unit_price - (discount / quantity)) * cart_uom_ratio) + tax_value1 + tax_value2 + tax_value3) * cart_alt_qty);
			// SOP("cart_total=="+cart_total);
			if (voucherclass_id.equals("101") || voucherclass_id.equals("114")) {
				item_serial = "0";
				item_batchno = "0";
			}

			Str.append("<tr>\n");

			Str.append("<input name=\"uom_ratio\" type=\"hidden\" id=\"uom_ratio\" ").append("value=\"").append(cart_uom_ratio).append("\"/>\n");
			Str.append("<td align=\"left\" valign=\"top\">");
			Str.append("<input name=\"txt_item_qty\" type=\"text\" class=\"form-control\" id=\"txt_item_qty\" ").append("value=\"");
			if (mode.equals("add")) {
				Str.append(df.format(cart_alt_qty));
			} else if (mode.equals("update")) {
				Str.append(df.format(cart_alt_qty));
			}
			Str.append("\" size=\"10\" maxlength=\"10\" onKeyUp=\"toNumber('txt_item_qty','Qty');CalItemTotal(" + para + ");\" />\n");

			// crs.getString("stock_current_qty"), crs.getString("price_variable"), crs.getString("price_sales_customer_id"), crs.getString("price_discount1_customer_id") replaced by 0
			Str.append("<input name=\"txt_item_id\" type=\"hidden\" id=\"txt_item_id\" ").append("value=\"").append(cart_item_id).append("\"/>\n");
			Str.append("<input name=\"txt_boxtype_size\" type=\"hidden\" id=\"txt_boxtype_size\" ").append("value=\"").append(boxtype_size).append("\"/>\n");
			Str.append("<input name=\"txt_stock_qty\" type=\"hidden\" id=\"txt_stock_qty\" ").append("value=\"").append("0").append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_baseprice\" name=\"txt_item_baseprice\" ").append("value=\"").append(item_unit_price).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_itemprice_updatemode\" name=\"txt_itemprice_updatemode\" ").append("value=\"").append(item_unit_price).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_pricevariable\" name=\"txt_item_pricevariable\" ").append("value=\"").append("0").append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_optioncount\" name=\"txt_optioncount\" ").append("value=\"").append("0").append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_ticket_dept_id\" name=\"txt_item_ticket_dept_id\" ").append("value=\"").append("0").append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_price_sales_customer_id\" name=\"txt_item_price_sales_customer_id\" ").append("value=\"")
					.append(price_sales_customer_id).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_price_discount1_customer_id\" name=\"txt_item_price_discount1_customer_id\" ").append("value=\"")
					.append(price_discount1_customer_id).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_item_price_disc_type\" name=\"txt_item_price_disc_type\" ").append("value=\"").append(price_disc_type).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_serial\" name=\"txt_serial\"  ").append("value=\"").append(item_serial).append("\"/>\n");
			Str.append("<input type=\"hidden\" id=\"txt_batch\" name=\"txt_batch\"  ").append("value=\"").append(item_batchno).append("\"/>\n");

			Str.append("</td><td align=\"left\" valign=\"top\">");
			if (!CNumeric(cart_alt_uom_id).equals("0")) {
				Str.append(PopulateUOM(uom_id, cart_alt_uom_id));
			}

			if (cart_alt_qty != 0.0) {
				temp_item_unit_price = (itemprice / cart_alt_qty);
				temp_item_discount = (discount / cart_alt_qty);
			}

			Str.append("</td><td align=\"left\" valign=\"top\">");
			// To be seen 1.
			// if (crs.getString("emp_invoice_priceupdate").equals("1") || crs.getString("price_variable").equals("1")) {
			Str.append("<input name=\"txt_item_price\" type=\"text\" class=\"form-control\" id=\"txt_item_price\" ")
					.append("value=\"")
					.append(df.format(temp_item_unit_price))
					.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price','Price');CalAmount(" + para + ");\" onChange=\"CheckBasePrice();CalItemTotal(" + para
							+ ");\" />");
			// } else {
			// Str.append("<input name=\"txt_item_price\" type=\"text\" class=\"form-control\" id=\"txt_item_price\" ").append("value=\"").append(df.format(temp_item_unit_price))
			// .append("\" size=\"10\" maxlength=\"10\" disabled />");
			// }
			// SOP("dicper=regex=="+((cart_discpercent+"").indexOf(".")<0?(cart_discpercent+""):(cart_discpercent+"").replaceAll("0*$",
			// "").replaceAll("\\.$", "")));
			Str.append("</td>\n<td align=\"left\" width=\"25%\" valign=\"top\">");
			// if (crs.getString("emp_invoice_discountupdate").equals("1")
			// // && mode.equals("add")
			// ) {
			if (price_disc_type == 1) {
				Str.append("<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
				Str.append("value=\"");
				Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
				Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount(" + para + ");CalItemTotal(" + para + ");\"/>")
						.append(" <div style=\"display:inline\">%</div>");
				Str.append("<br/><input name=\"txt_item_price_disc\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc\" ").append("value=\"")
						.append(df.format((temp_item_discount)))
						.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\">")
						.append(" <div style=\"display:inline\"></div>");
			} else {
				Str.append("<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
				Str.append("value=\"");
				Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
				Str.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc_percent_add','Discount');CalAmount(" + para + ");CalItemTotal(" + para + ");\"/>")
						.append(" <div style=\"display:inline\">%</div>");
				Str.append("<br/><input name=\"txt_item_price_disc\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc\" ").append("value=\"")
						.append(df.format((temp_item_discount)))
						.append("\" size=\"10\" maxlength=\"10\"  onKeyUp=\"toNumber('txt_item_price_disc','Discount');CalPercent(" + para + ");\"/>")
						.append(" <div style=\"display:inline\"></div>");
			}

			// }

			// For cart UPDATE "+compdb(comp_id)+"mode to disable disc
			// else {
			// if (price_disc_type == 1) {
			// Str.append("<input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
			// Str.append("value=\"");
			// Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
			// Str.append("\"/>\n");
			// Str.append(" <span>%</span>");
			// Str.append("<br/><input name=\"txt_item_price_disc\" class=\"form-control\" type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc\" disabled ").append("value=\"")
			// .append(df.format((temp_item_discount))).append("\"/>\n");
			//
			// } else {
			// Str.append("<input name=\"txt_item_price_disc\"  class=\"form-control\"  type=\"text\" size=\"10\" maxlength=\"10\" id=\"txt_item_price_disc\" disabled ").append("value=\"")
			// .append(df.format((temp_item_discount))).append("\"/>\n");
			// Str.append("<br/><input name=\"txt_item_price_disc_percent_add\" type=\"text\" class=\"form-control\" id=\"txt_item_price_disc_percent_add\" ");
			// Str.append("value=\"");
			// Str.append(Double.parseDouble(df6.format(cart_discpercent) + ""));
			// Str.append("\"/>\n");
			// Str.append(" <span>%</span>");
			// }
			//
			// }

			Str.append("</td>\n<td align=\"right\" width = \"30%\" valign=\"top\" nowrap>");
			if (tax1_name.equals("0") && tax2_name.equals("0") && tax3_name.equals("0")) {
				Str.append("0.00").append("");
			}
			if (!tax1_name.equals("0")) {
				Str.append(tax1_name).append(": ");
				Str.append("<input name=\"txt_item_tax1\" type=\"text\" class=\"form-control\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						.append(df.format(tax_value1)).append("\"/>\n");
				Str.append("<br/>");
			}
			if (!tax2_name.equals("0")) {
				Str.append(tax2_name).append(": ");
				Str.append("<input name=\"txt_item_tax2\" type=\"text\" class=\"form-control\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						.append(df.format(tax_value2)).append("\"/>\n");
				Str.append("<br/>");
			}
			if (!tax3_name.equals("0")) {
				Str.append(tax3_name).append(": ");
				Str.append("<input name=\"txt_item_tax3\" type=\"text\" class=\"form-control\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						.append(df.format(tax_value3)).append("\"/>\n");
			}

			// crs.getInt("tax_id1") and price_tax2_after_tax1 replaced by ' 0' revert it later
			Str.append("<input name=\"txt_item_price_tax_rate1\" type=\"hidden\" id=\"txt_item_price_tax_rate1\" ").append(" value=\" ").append(tax1_rate1).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_customer_id1\" type=\"hidden\" id=\"txt_item_price_tax_customer_id1\" ").append("value=\"").append(tax_customer_id1).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_id1\" type=\"hidden\" id=\"txt_item_price_tax_id1\" ").append("value=\"").append(tax_id1).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_rate2\" type=\"hidden\" id=\"txt_item_price_tax_rate2\" ").append("value=\"").append(tax2_rate2).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_customer_id2\" type=\"hidden\" id=\"txt_item_price_tax_customer_id2\" ").append("value=\"").append(tax_customer_id2).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_id2\" type=\"hidden\" id=\"txt_item_price_tax_id2\" ").append("value=\"").append(tax_id2).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_rate3\" type=\"hidden\" id=\"txt_item_price_tax_rate3\" ").append("value=\"").append(tax3_rate3).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_customer_id3\" type=\"hidden\" id=\"txt_item_price_tax_customer_id3\" ").append("value=\"").append(tax_customer_id3).append("\"/>\n");
			Str.append("<input name=\"txt_item_price_tax_id3\" type=\"hidden\" id=\"txt_item_price_tax_id3\" ").append("value=\"").append(tax_id3).append("\"/>\n");
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
					Str.append("<td align=\"left\" valign=\"top\" colspan=\"4\"><input name=\"txt_item_serial\" type=\"text\" class=\"form-control\" id=\"txt_item_serial\" value="
							+ stockserial_serial_no + " size=\"20\" maxlength=\"30\" /></td>\n");
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
			Str.append("<td colspan=\"6\"  valign=\"top\">");
			if (vouchertype_id.equals("114")) {
				Str.append("<span style=\"float:left\"><input name=\"stock_location_button\" id=\"stock_location_button\" type=\"button\" class=\"button\" value=\"All Location\"  onClick=\"showlocstockstatus('"
						+ branch_id + "','" + cart_item_id + "');\" /></span>\n");
			}
			// Str.append("</td>");
			if (mode.equals("add")) {
				// Str.append("<td colspan=\"3\" align=\"center\" valign=\"top\">");
				Str.append("<center><input name=\"add_button\" id=\"add_button\" type=\"button\" class=\"btn btn-success\" value=\"Add\"  onClick=\"AddCartItem();\" /></center>\n");
				// // +
				// "<div id=\"mode_button\"> ").append("</div></td>\n");
			} else if (mode.equals("update")) {
				// Str.append("<td colspan=\"3\" align=\"center\" valign=\"top\">");
				Str.append("<center><input name=\"update_button\" id=\"update_button\" type=\"button\" class=\"btn btn-success\" value=\"Update\"  onClick=\"UpdateCartItem();\" /></center>\n");
				// +
				// "<div id=\"mode_button\"> ").append("</div></td>\n");
			}
			Str.append("</td>");
			Str.append("</tr>");
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");

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
					+ " opt.option_select, item_small_desc, group_id, (SELECT COUNT(DISTINCT optitem.option_id)" + " FROM  " + compdb(comp_id) + "axela_inventory_item_option optitem"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = optitem.option_item_id" + " INNER JOIN  " + compdb(comp_id)
					+ "axela_inventory_item_price ON price_item_id = optitem.option_itemmaster_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_item_id = item_id" + " AND pricetrans_price_id = price_id"
					+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id" + " AND optitem.option_group_id = group_id" + " GROUP BY optitem.option_group_id) AS groupitemcount"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option opt" + " INNER JOIN  " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id" + " INNER JOIN  "
					+ compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = option_itemmaster_id" + " LEFT JOIN  " + compdb(comp_id)
					+ "axela_inventory_item_price_trans ON pricetrans_item_id = item_id"
					+ " AND pricetrans_price_id = price_id" + " WHERE option_itemmaster_id = " + cart_item_id + "" + " GROUP BY group_name, group_type, item_id"
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
					if (crs.getString("group_type").equals("1")) { // function
																	// for
																	// Default

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
					} else if (crs.getString("group_type").equals("2")) { // function
																			// for
																			// Multi
																			// Select
						Str.append("/>\n");
					} else if (crs.getString("group_type").equals("3")) {
						Str.append(" onclick=\"CalculateMultiSelect(this.id,this.value,").append(crs.getString("groupitemcount")).append(",");
						Str.append(crs.getString("group_id")).append(",").append(groupcount).append(");\"/>\n");
					}

					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(cart_item_name);
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
		double invoice_grandtotal = 0.00, invoice_total = 0.00, total = 0.00, cart_alt_qty = 0.00, uom_ratio = 0.00;
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
		String tax1 = "", tax1_name = "";
		String tax2 = "", tax2_name = "";
		String tax3 = "", tax3_name = "";
		String tax_rate1 = "0.00";
		String tax_rate2 = "0.00";
		String tax_rate3 = "0.00";
		String tax_id1 = "0";
		String tax_id2 = "0";
		String tax_id3 = "0";
		double tax1_rate1 = 0.00, tax2_rate2 = 0.00, tax3_rate3 = 0.00;
		double tax_value1 = 0.00, tax_value2 = 0.00, tax_value3 = 0.00;
		String tax_amount1 = "0.00", tax_amount2 = "0.00", tax_amount3 = "0.00";
		String disc_amount = "0.00", disc_percent = "0.00", discountpercent = "0.00";

		StrSql = "SELECT cart.cart_id, cart.cart_customer_id AS cart_customer_id,"
				+ " cart.cart_rowcount, cart.cart_voucher_id AS cart_voucher_id, "
				+ " cart.cart_multivoucher_id AS cart_multivoucher_id,"
				+ " item_id, item_uom_id, uom_name,"
				// + " boxtype_size,"
				+ " item_name, item_code, cart.cart_qty, cart.cart_alt_qty,"
				+ " cart.cart_alt_uom_id, cart.cart_truckspace,"
				+ " cart.cart_price, cart.cart_netprice, cart.cart_amount,"
				// //discountperc
				+ " COALESCE((SELECT CONCAT(discount_perc.cart_discount_perc, '-', discount_perc.cart_customer_id, '-', discount_perc.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart discount_perc"
				+ " WHERE discount_perc.cart_item_id = cart.cart_item_id"
				+ " AND discount_perc.cart_option_id = cart.cart_rowcount"
				+ " AND discount_perc.cart_discount = 1"
				+ " AND discount_perc.cart_vouchertype_id = " + vouchertype_id
				+ " AND discount_perc.cart_session_id = " + cart_session_id + ""
				+ " AND discount_perc.cart_emp_id = " + emp_id + ""
				+ " limit 1), '0-0-0') AS discountperc,"
				+ " COALESCE((SELECT CONCAT(tax1.cart_tax_id, '-',REPLACE(customer_name, '%', ''), '-',customer_rate, '-',tax1.cart_customer_id, '-',tax1.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax1"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id =tax1.cart_tax_id"
				+ " WHERE tax1.cart_item_id = cart.cart_item_id"
				+ " AND tax1.cart_option_id = cart.cart_rowcount AND tax1.cart_tax = 1"
				+ " AND tax1.cart_vouchertype_id = " + vouchertype_id + " "
				+ " AND tax1.cart_session_id = " + cart_session_id + " "
				+ " AND tax1.cart_emp_id = " + emp_id + " "
				+ " limit 0,1), '0-0-0-0-0') AS tax1,"
				+ " COALESCE((SELECT CONCAT(tax2.cart_tax_id, '-',REPLACE(customer_name, '%', ''), '-',customer_rate, '-',tax2.cart_customer_id, '-',tax2.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax2"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id =tax2.cart_tax_id"
				+ " WHERE tax2.cart_item_id = cart.cart_item_id"
				+ " AND tax2.cart_option_id = cart.cart_rowcount AND tax2.cart_tax = 1"
				+ " AND tax2.cart_vouchertype_id = " + vouchertype_id + " "
				+ " AND tax2.cart_session_id = " + cart_session_id + " "
				+ " AND tax2.cart_emp_id = " + emp_id + " "
				+ " limit 1,1), '0-0-0-0-0') AS tax2,"
				+ " COALESCE((SELECT CONCAT(tax3.cart_tax_id, '-',REPLACE(customer_name, '%', ''), '-',customer_rate, '-',tax3.cart_customer_id, '-',tax3.cart_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart tax3"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id =tax3.cart_tax_id"
				+ " WHERE tax3.cart_item_id = cart.cart_item_id"
				+ " AND tax3.cart_option_id = cart.cart_rowcount AND tax3.cart_tax = 1"
				+ " AND tax3.cart_vouchertype_id = " + vouchertype_id + " "
				+ " AND tax3.cart_session_id = " + cart_session_id + " "
				+ " AND tax3.cart_emp_id = " + emp_id + " "
				+ " limit 2,1), '0-0-0-0-0') AS tax3"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart cart"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item on item_id = cart.cart_item_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_uom ON uom_id = cart.cart_alt_uom_id"
				// + " LEFT JOIN  "+compdb(comp_id)+"axela_inventory_boxtype ON boxtype_id = item_boxtype_id"
				+ " WHERE 1=1"
				+ " AND cart.cart_session_id = " + cart_session_id + " "
				+ " AND cart.cart_emp_id = " + emp_id + " "
				+ " AND cart.cart_vouchertype_id =" + vouchertype_id
				+ " AND cart.cart_discount=0 "
				+ " AND cart.cart_tax=0 "
				+ " AND cart.cart_item_id!=0 "
				// + " AND cart.cart_alt_uom_id = "+cart_alt_uom_id+""
				+ " AND cart.cart_rowcount!=0 "
				+ " AND cart.cart_option_id=0"
				+ " GROUP BY cart.cart_id" + " order BY cart.cart_id";

		// SOP("StrSql=List cart items==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		if (!vouchertype_id.equals("11")) {
			StrSql = "SELECT COALESCE(cart_id, 0) AS cart_id,"
					+ " COALESCE(customer_id, 0) AS tax_id,"
					+ " COALESCE(customer_name,'') AS tax_name,"
					+ " COALESCE(customer_rate, 0.00) AS tax_rate,"
					+ " COALESCE(cart_amount, 0.00) AS cart_amount"
					+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_rowcount = 0"
					+ " AND cart_option_id = 0"
					+ " GROUP BY cart_id"
					+ " order BY cart_id";
			// SOP("StrSql=List cart bs items==" + StrSqlBreaker(StrSql));
			crs1 = processQuery(StrSql, 0);
		}
		Str.append("<div class=\"table table-bordered\">\n");
		Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
		try {
			if (!msg.equals("")) {
				Str.append("<thead>\n<tr>\n<td colspan=\"10\" align=\"center\"><font color=\"red\"><b>").append(msg).append("</b></font></td>\n</tr>\n</thead>\n");
			}
			Str.append("<thead>\n<tr>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>X</th>\n");
			Str.append("<th>Inv. ID.</th>\n");
			Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item</th>\n");
			Str.append("<th data-hide=\"phone\">Qty</th>\n");
			Str.append("<th data-hide=\"phone\">UOM</th>\n");
			Str.append("<th data-hide=\"phone\">Price</th>\n");
			Str.append("<th data-hide=\"phone\">Discount</th>\n");
			Str.append("<th data-hide=\"phone\">Tax</th>\n");
			// Str.append("<th data-hide=\"phone\"></th>\n");
			Str.append("<th>Amount</th>\n");
			Str.append("</tr>\n</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					cart_alt_qty = 0.0;
					item_price = 0.0;
					uom_ratio = 0.0;
					quantity = 0.0;
					discount = 0.0;
					tax1_name = "";
					tax2_name = "";
					tax3_name = "";
					tax_rate1 = "0.00";
					tax_rate2 = "0.00";
					tax_rate3 = "0.00";
					tax_value1 = 0.0;
					tax_value2 = 0.0;
					tax_value3 = 0.0;
					tax_id1 = "0";
					tax_id2 = "0";
					tax_id3 = "0";

					// boxtype_size = crs.getString("boxtype_size");
					voucher_id = crs.getString("cart_voucher_id");
					item_price = crs.getDouble("cart_price");
					cart_alt_qty = crs.getDouble("cart_alt_qty");
					// SOP("cart_alt_qty===" + cart_alt_qty);
					quantity = crs.getDouble("cart_qty");
					if (!CNumeric(cart_alt_qty + "").equals("0")) {
						uom_ratio = (quantity / cart_alt_qty);
					}

					discountpercent = crs.getString("discountperc");
					// SOP("discountpercent===" + discountpercent);
					if (!discountpercent.equals("")) {
						disc_percent = discountpercent.split("-")[0];
						// SOP("disc_percent===" + disc_percent);
						if (!disc_percent.equals("0")) {
							discpercent = Double.parseDouble(disc_percent);
						}

						// SOP("discpercent===" + discpercent);
						price_discount1_customer_id = discountpercent.split("-")[1];

						if (discpercent != 0.0) {
							discount = (((item_price) * discpercent) / 100);
							// SOP("discount==list=" + discount);
						}
						// disc_amount = discountpercent.split("-")[2];
						// SOP("disc_amount===" + disc_amount);
						// if (!disc_amount.equals("0")) {
						// discount = Double.parseDouble(disc_amount) / quantity;
						// SOP("discount===" + discount);
						// }

					}
					tax1 = crs.getString("tax1");
					tax2 = crs.getString("tax2");
					tax3 = crs.getString("tax3");
					if (!tax1.equals("")) {
						tax_id1 = tax1.split("-")[0];
						// SOP("tax_id1===" + tax_id1);
						tax1_name = tax1.split("-")[1];
						tax_rate1 = tax1.split("-")[2];
						tax1_rate1 = Double.parseDouble(tax_rate1);
						// SOP("tax_rate1===" + tax_rate1);
						tax_customer_id1 = tax1.split("-")[3];
						// tax_amount1 = tax1.split("-")[4];

					}

					if (!tax2.equals("")) {
						tax_id2 = tax2.split("-")[0];
						// SOP("tax_id2===" + tax_id2);
						tax2_name = tax2.split("-")[1];
						tax_rate2 = tax2.split("-")[2];
						tax2_rate2 = Double.parseDouble(tax_rate2);
						tax_customer_id2 = tax2.split("-")[3];
						// tax_amount2 = tax2.split("-")[4];

					}
					if (!tax3.equals("")) {
						tax_id3 = tax3.split("-")[0];
						// SOP("tax_id3===" + tax_id3);
						tax3_name = tax3.split("-")[1];
						tax_rate3 = tax3.split("-")[2];
						tax3_rate3 = Double.parseDouble(tax_rate3);
						tax_customer_id3 = tax3.split("-")[3];
						// tax_amount3 = tax3.split("-")[4];
						// if (!tax_amount3.equals("0")) {
						// tax_amount3 = (Double.parseDouble(tax_amount3) / cart_alt_qty) + "";
						// }
						// SOP("tax_amount3===" + tax_amount3);
					}
					if (tax1_rate1 != 0.00) {
						tax_value1 = (((item_price - discount) * uom_ratio) * (tax1_rate1 / 100));
						// SOP("tax_value1===" + tax_value1);
					}

					if (tax2_rate2 != 0.00) {
						if ((price_tax2_after_tax1.equals("1")) && (tax_value1 != 0)) {
							tax_value2 = ((((item_price - discount) * uom_ratio) + tax_value1) * (tax2_rate2 / 100));
						} else {
							tax_value2 = (((item_price - discount) * uom_ratio) * (tax2_rate2 / 100));
						}
						// SOP("tax_value2===" + tax_value2);
					}
					if (tax3_rate3 != 0.00) {
						if ((price_tax3_after_tax2.equals("1")) && (tax_value1 != 0) && (tax_value2 != 0)) {
							tax_value3 = ((((item_price - discount) * uom_ratio) + tax_value1 + tax_value2) * (tax3_rate3 / 100));
						} else {
							tax_value3 = (((item_price - discount) * uom_ratio) * (tax3_rate3 / 100));
						}
						// SOP("tax_value3===" + tax_value3);
					}
					// Total amount for No. Of Qty's.

					// discpercent = ((discount * 100) / (item_price +
					// optiontotal));
					// SOP("discpercent=list=="+discpercent);
					mainitemamt = (item_price) * quantity;
					invoice_qty += crs.getDouble("cart_alt_qty");
					total_discount += (discount * uom_ratio);
					total = ((((item_price - discount) * uom_ratio) + tax_value1 + tax_value2 + tax_value3) * cart_alt_qty);
					// total = (((item_price - discount) * quantity) + Double.parseDouble(tax_amount1) + Double.parseDouble(tax_amount2) + Double.parseDouble(tax_amount3));
					// SOP("total===" + total);
					if (CNumeric(CheckNull(total + "")).equals(0)) {
						total = 0.0;
					}
					// SOP("total==="+total);
					invoice_total += total;
					total_tax += (tax_value1 + tax_value2 + tax_value3);

					String item_name = "";
					if (!crs.getString("item_name").equals("")) {
						item_name += crs.getString("item_name");
					}
					item_name += "(" + crs.getString("item_code") + ") ";
					if (!crs.getString("cart_rowcount").equals("0")) {
						++count;
					}
					Str.append("\n<tr valign=\"top\"");
					Str.append(">\n<td width=\"5%\" align=\"center\">\n");
					Str.append(count);
					Str.append("</td>\n<td align=\"center\">\n");
					if (!vouchertype_id.equals("24") && !vouchertype_id.equals("23")) {
						Str.append("X");
					} else {
						Str.append("<a href=\"javascript:delete_cart_item(").append(crs.getString("cart_id")).append(");\">X</a>");
					}
					Str.append("</td>\n<td align=\"center\">\n");
					Str.append("<a href=\"../accounting/voucher-list.jsp?voucher_id=" + crs.getString("cart_multivoucher_id") + "").append("&vouchertype_id=" + vouchertype_id + "")
							.append("&voucherclass_id=" + voucherclass_id);
					Str.append("\">" + crs.getString("cart_multivoucher_id") + "</a>");
					Str.append("</td>\n<td id='" + count + "' align=\"left\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("cart_multivoucher_id")).append(",");
					Str.append(crs.getString("item_id")).append(",");
					Str.append("'" + item_name + "'").append(",");
					Str.append("'" + crs.getString("item_code") + "'").append(",");
					Str.append(crs.getString("item_uom_id")).append(",");
					Str.append(crs.getDouble("cart_alt_uom_id")).append(",");
					Str.append(crs.getDouble("cart_alt_qty")).append(",");
					Str.append(crs.getDouble("cart_qty")).append(",");
					Str.append(item_price + ",");
					Str.append(crs.getDouble("cart_amount")).append(",");
					Str.append("'" + disc_percent).append("',");
					Str.append("'" + price_discount1_customer_id).append("',");
					Str.append("'" + disc_amount).append("',");
					Str.append("'" + tax_id1).append("',");
					Str.append("'" + tax_id2).append("',");
					Str.append("'" + tax_id3).append("',");
					Str.append("'" + tax1_name).append("',");
					Str.append("'" + tax2_name).append("',");
					Str.append("'" + tax3_name).append("',");
					Str.append("'" + tax_rate1).append("',");
					Str.append("'" + tax_rate2).append("',");
					Str.append("'" + tax_rate3).append("',");
					Str.append("'" + tax_customer_id1).append("',");
					Str.append("'" + tax_customer_id2).append("',");
					Str.append("'" + tax_customer_id3).append("',");
					Str.append(crs.getString("cart_customer_id")).append(",");
					Str.append(crs.getString("cart_id")).append(",");
					Str.append("'update');setColorById(this.id);\"");
					Str.append(">\n");
					Str.append(item_name);
					Str.append("</td>\n<td align=\"center\">\n");
					Str.append(crs.getDouble("cart_qty"));
					Str.append("</td>");

					Str.append("<td align=\"left\">\n");
					Str.append(crs.getString("uom_name"));
					Str.append("</td>");
					Str.append("\n<td align=\"right\">\n");
					if (item_price != 0.0) {
						Str.append(df.format((item_price * uom_ratio)));
					} else {
						Str.append("0.00");
					}
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(discount * uom_ratio));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format((tax_value1 + tax_value2 + tax_value3)));
					Str.append("</td>\n<td align=\"right\">\n");
					if (item_price != 0.0) {
						Str.append(df.format(total));
					} else {
						Str.append("0.00");
					}
					Str.append("</td>\n</tr>\n");
					// SOP(Str.toString());
				}
				updatecount += count;
				// SOP("updatecount===" + updatecount);
			}

			Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=\"top\" align=\"right\" colspan=\"2\"><b>Total:</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"");
			Str.append(invoice_qty).append("\"><b>").append(invoice_qty).append("</b></td>\n");

			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_discount)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_tax)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\">\n");
			Str.append("<input type=\"hidden\" name=\"txt_updatecount\" id=\"txt_updatecount\" value=\"");
			Str.append(updatecount).append("\">");
			Str.append("<input type=\"hidden\" name=\"txt_invoice_grandtotal\" id=\"txt_invoice_grandtotal\" value=\"");
			Str.append(invoice_total).append("\">\n<b>");
			Str.append(df.format(invoice_total)).append("</b></td>\n");
			Str.append("</tr>\n");
			if (!vouchertype_id.equals("11")) {
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {

						invoice_grandtotal += crs1.getDouble("cart_amount");

						Str.append("<tr valign=\"top\">");
						Str.append("<td valign=\"top\" colspan=\"7\" align=\"left\">").append(crs1.getString("tax_name")).append("</td>");
						Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("tax_rate")).append("</td>");
						Str.append("<td valign=\"top\" align=\"right\">").append(crs1.getString("cart_amount")).append("</td>");
						Str.append("</tr>");
					}
				}

				invoice_grandtotal += invoice_total;
				// Str.append(Listdata());
				Str.append("<tr valign=\"top\">\n");
				Str.append("<td valign=\"top\" colspan='7' align='left'>");
				Str.append("<input type='button' id='delete_full_cart' name='delete_full_cart' class='btn btn-success' value='Delete Cart' onclick=\"DeleteFullCart();\"/>");
				if (vouchertype_id.equals("102")) {
					Str.append("<input type='button' id='multiple_deliverynote' name='multiple_deliverynote' class='btn btn-success' value='Multiple DN' onclick=\"MultipleDN();return false;\"/>");
				}
				Str.append("<input type='button' id='billsundry' name='billsundry' class='btn btn-success' value='Bill Sundry' onclick=\"BillSundry('" + vouchertype_id + "','" + cart_session_id
						+ "','"
						+ voucher_id + "','" + invoice_total + "');\" /></td>");
				Str.append("<td valign=\"top\" align='right' colspan='2'><b>Grand Total: </b></td>");
				Str.append("<td valign=\"top\" align='right'><b>");
				Str.append(df.format(invoice_grandtotal));
				Str.append("</b></td>");
				Str.append("</tr>\n");
				crs1.close();
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</tbody>\n");
		Str.append("</table>\n");
		Str.append("</div>\n");
		return Str.toString();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		doGet(request, response);
	}

	public String PopulateUOM(String uom_id, String alt_uom_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT  uom_id, uom_ratio, uom_name" + " FROM " + compdb(comp_id) + "axela_inventory_uom" + " WHERE 1=1"
					// + " AND (uom_id = 1 OR uom_parent_id =1)"
					+ " AND (uom_id = " + uom_id + " OR uom_parent_id = " + uom_id + ")" + " GROUP BY CONCAT(uom_name,uom_ratio)" + " ORDER BY uom_id";
			// SOP("StrSql=po=uom11="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_alt_uom_id\" id=\"dr_alt_uom_id\" style=\"width:150px\" class=\"form-control\"  onChange=\"CalItemTotal(" + para + ");\">");
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
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
