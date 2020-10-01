package axela.service;
//aJIt 20th November, 2012

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.SO_Update;
import cloudify.connect.Connect;

public class JobCard_Item_Details extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String add = "", type = "", type_name = "";
	public String emp_id = "0", jc_id = "0", jcitem_rateclass_id = "0", rateclass_id = "0", jcitem_option_id = "0";
	public String jcitem_id = "", jcitem_item_serial = "";
	public String jcitem_item_id = "", jcitem_price = "", jcitem_tax_rate = "";
	public String jcitem_disc = "", jcitem_tax = "", jcitem_tax_id = "0";
	public String jcitem_tax_rate2 = "", jcitem_tax2 = "", jcitem_tax_id2 = "0";
	// public String jcitem_tax_rate3 = "", jcitem_tax3 = "", jcitem_tax_id3 = "0";
	public String jcitem_qty = "", jcitem_total = "", jctrans_billtype_id = "0";
	public String list_jcitems = "", msg = "", insur_msg = "", warranty_msg = "", configure = "";
	public String mode = "", status = "", rowcount = "";
	public String add_jcitem = "";
	public String update_jcitem = "";
	public String delete_jcitem = "";
	public String jc_netamt = "";
	public String jc_discamt = "";
	public String jc_totaltax = "";
	public String jc_grandtotal = "";
	public String jcitem_rowcount = "";
	public String jcitem_option_group = "";
	public String jc_location_id = "0";
	public String item_ticket_dept_id = "0";
	public String brand_id = "0", billcat_billtype = "";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df1 = new DecimalFormat("0");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String customer_id = "0";
	public String stock_current_qty = "0";
	String voucher_id = "0", item_type_id = "0";
	double totalamount = 0.0, quantity = 0.0;
	public String jctrans_id = "0", jctrans_rowcount = "0", jctrans_option_id = "0";
	public SO_Update soupdate = new SO_Update();

	public String updatecount = "";
	public String boxtype_size = "0", gst_type = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
			jcitem_item_id = CNumeric(PadQuotes(request.getParameter("jcitem_item_id")));
			jc_location_id = CNumeric(PadQuotes(request.getParameter("jc_location_id")));
			rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
			jcitem_rateclass_id = CNumeric(PadQuotes(request.getParameter("jcitem_rateclass_id")));
			if (!jcitem_rateclass_id.equals("0")) {
				rateclass_id = jcitem_rateclass_id;
			}
			item_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("item_ticket_dept_id")));
			jcitem_item_serial = PadQuotes(request.getParameter("jcitem_item_serial"));
			jcitem_price = CNumeric(PadQuotes(request.getParameter("jcitem_price")));
			jcitem_disc = CNumeric(PadQuotes(request.getParameter("jcitem_discount")));
			jcitem_tax = CNumeric(PadQuotes(request.getParameter("jcitem_tax1")));
			jcitem_tax2 = CNumeric(PadQuotes(request.getParameter("jcitem_tax2")));
			// jcitem_tax3 = CNumeric(PadQuotes(request.getParameter("jcitem_tax3")));
			jcitem_tax_id = CNumeric(PadQuotes(request.getParameter("jcitem_tax_id1")));
			jcitem_tax_id2 = CNumeric(PadQuotes(request.getParameter("jcitem_tax_id2")));
			// jcitem_tax_id3 = CNumeric(PadQuotes(request.getParameter("jcitem_tax_id3")));
			jcitem_tax_rate = CNumeric(PadQuotes(request.getParameter("jcitem_tax_rate1")));
			jcitem_tax_rate2 = CNumeric(PadQuotes(request.getParameter("jcitem_tax_rate2")));
			// jcitem_tax_rate3 = CNumeric(PadQuotes(request.getParameter("jcitem_tax_rate3")));
			jcitem_qty = CNumeric(PadQuotes(request.getParameter("jcitem_qty")));
			jcitem_total = CNumeric(PadQuotes(request.getParameter("jcitem_total")));
			jctrans_billtype_id = PadQuotes(request.getParameter("jctrans_billtype_id"));
			jcitem_id = CNumeric(PadQuotes(request.getParameter("jcitem_id")));
			jcitem_option_group = PadQuotes(request.getParameter("jcitem_option_group"));
			jcitem_option_id = CNumeric(PadQuotes(request.getParameter("jcitem_option_id")));
			list_jcitems = PadQuotes(request.getParameter("list_jcitems"));
			gst_type = PadQuotes(request.getParameter("gsttype"));
			type = PadQuotes(request.getParameter("type"));
			rowcount = PadQuotes(request.getParameter("rowcount"));
			mode = PadQuotes(request.getParameter("mode"));
			status = PadQuotes(request.getParameter("status"));
			configure = PadQuotes(request.getParameter("configure"));
			add_jcitem = PadQuotes(request.getParameter("add_jcitem"));
			update_jcitem = PadQuotes(request.getParameter("update_jcitem"));
			delete_jcitem = PadQuotes(request.getParameter("delete_jcitem"));

			boxtype_size = CNumeric(PadQuotes(CheckNull(request.getParameter("jctrans_boxtype_size"))));

			// this block is for so-invoive auto-update
			// StrSql = "SELECT voucher_id, voucher_customer_id FROM " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_jc_id = " + jc_id;
			// CachedRowSet crs = processQuery(StrSql);
			// while (crs.next()) {
			// voucher_id = crs.getString("voucher_id");
			// customer_id = crs.getString("voucher_customer_id");
			// }
			SOP("jctransbilltype==" + jctrans_billtype_id);
			if (type.equals("2")) {
				type_name = "ACCESSORIES";
			} else if (type.equals("3")) {
				type_name = "PARTS";
			} else if (type.equals("4")) {
				type_name = "LABOUR";
			}
			if (add_jcitem.equals("yes") && msg.equals("")) {
				AddJCItem(request, response);
			} else if (!jcitem_id.equals("0") && update_jcitem.equals("yes") && msg.equals("")) {
				UpdateJCItem();
			} else if (!jcitem_id.equals("0") && delete_jcitem.equals("yes")) {
				DeleteJCItem();
			}

			if (list_jcitems.equals("yes") && !gst_type.equals("")) {
				ListJCItems();
			} else if (list_jcitems.equals("yes") && gst_type.equals("")) {
				StrHTML = "<center><font color=red>Select Customer City!</font></center>";
			}
			// else if (!jcitem_item_id.equals("0") && configure.equals("yes")) {
			// GetConfigurationDetails();
			// }
		}
	}
	public void AddJCItem(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			String order = "", check = "";
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			check = CNumeric(PadQuotes(ExecuteQuery("SELECT jctrans_item_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_jc_id = " + jc_id
					+ "	AND jctrans_billtype_id = " + jctrans_billtype_id
					+ " AND jctrans_item_id =" + jcitem_item_id)));
			if (check.equals("0")) {

				if (rowcount.equals("yes")) {
					jcitem_rowcount = ExecuteQuery("SELECT COALESCE(MAX(jcitem_rowcount), 0) + 1"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_item");

					jcitem_option_id = "0";
				} else {
					jcitem_rowcount = "0";
					if (jcitem_id.equals("0")) {
						jcitem_option_id = ExecuteQuery("SELECT COALESCE(MAX(jcitem_rowcount), 0) FROM " + compdb(comp_id) + "axela_service_jc_item");
					} else {
						jcitem_option_id = ExecuteQuery("SELECT COALESCE(jcitem_rowcount, 0) FROM " + compdb(comp_id) + "axela_service_jc_item"
								+ " WHERE jcitem_id = " + jcitem_id + "");
					}
				}
				if (item_ticket_dept_id.equals("1")) {
					order = " ASC";
				} else if (item_ticket_dept_id.equals("2")) {
					order = " DESC";
				}
				if (item_ticket_dept_id.equals("0")) {
					if (!jcitem_item_serial.equals("0") && !jcitem_item_serial.equals("s")) {
						StrSql = "SELECT stockserial_serial_no FROM " + compdb(comp_id) + "axela_inventory_stockserial"
								+ " WHERE stockserial_location_id = " + jc_location_id + ""
								+ " AND stockserial_item_id = " + jcitem_item_id + ""
								+ " AND stockserial_serial_no = '" + jcitem_item_serial + "'";
						if (!ExecuteQuery(StrSql).equals(jcitem_item_serial)) {
							msg = "Invalid Serial No.!";
						}
					}
				} else {
					StrSql = "SELECT stockserial_serial_no"
							+ " FROM " + compdb(comp_id) + "axela_inventory_stockserial"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_item ON jcitem_item_id = stockserial_item_id"
							+ " AND jcitem_item_serial =  stockserial_serial_no "
							+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcitem_jc_id"
							+ " AND jc_location_id = stockserial_location_id"
							+ " WHERE stockserial_item_id = " + jcitem_item_id + ""
							+ " AND stockserial_location_id = " + jc_location_id + ""
							+ " AND stockserial_time != ''"
							+ " ORDER BY stockserial_time" + order + ""
							+ " LIMIT 1";
					jcitem_item_serial = ExecuteQuery(StrSql);

					if (jcitem_item_serial.equals("")) {
						msg = "Invalid Item!";
					}
				}
				if (msg.equals("")) {

					String tax1 = "0", tax2 = "0", tax3 = "0";

					jctrans_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(jc.jctrans_rowcount), 0) + 1" + " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"));
					jctrans_option_id = jctrans_rowcount;
					String total = df.format(Double.parseDouble(jcitem_price) * Double.parseDouble(jcitem_qty)) + "";
					if (total.equals("0.00")) {
						total = "1";
					}
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
							+ " ("
							+ " jctrans_jc_id,"
							+ " jctrans_customer_id,"
							+ " jctrans_rateclass_id,"
							+ "	jctrans_location_id,"
							+ " jctrans_item_id,"
							+ " jctrans_discount,"
							+ " jctrans_tax,"
							+ "	jctrans_rowcount,"
							+ "	jctrans_option_id,"
							+ " jctrans_qty,"
							+ " jctrans_price,"
							+ " jctrans_amount,"
							+ " jctrans_netprice,"
							+ " jctrans_discountamount,"
							+ " jctrans_taxamount,"
							+ "	jctrans_alt_qty,"
							+ "	jctrans_alt_uom_id,"
							+ "	jctrans_unit_cost,"
							+ "	jctrans_convfactor,"
							+ " jctrans_time,"
							+ "	jctrans_billtype_id,"
							+ "	jctrans_dc)"
							+ " VALUES"
							+ " ("
							+ " " + jc_id + ","
							+ " 1," // jctrans_customer_id
							+ " " + jcitem_rateclass_id + ","
							+ " " + jc_location_id + ","
							+ " " + jcitem_item_id + ","
							+ " 0," // jctrans_discount
							+ " 0," // jctrans_tax
							+ " " + Integer.parseInt(jctrans_rowcount) + ","
							+ "	0," // jctrans_option_id
							+ " " + jcitem_qty + ","
							+ " " + jcitem_price + ","
							+ " " + total + ","
							+ " " + total + ","
							+ " " + jcitem_disc + ","
							+ " 0,"
							+ " " + jcitem_qty + ","
							+ " 1," // jctrans_alt_uom_id
							+ " " + jcitem_price + ","
							+ " 1," // jctrans_convfactor
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + jctrans_billtype_id + ","
							+ " 0" // jctrans_dc
							+ ")";
					SOP("Main item==" + StrSql);
					stmttx.addBatch(StrSql);
					// Discount entry in jc_trans table
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
							+ " ("
							+ " jctrans_jc_id,"
							+ " jctrans_customer_id,"
							+ " jctrans_item_id,"
							+ " jctrans_discount,"
							+ "	jctrans_rowcount,"
							+ "	jctrans_option_id,"
							+ " jctrans_price,"
							+ " jctrans_discount_perc,"
							+ " jctrans_amount,"
							+ " jctrans_time,"
							+ "	jctrans_billtype_id,"
							+ " jctrans_dc )"
							+ " VALUES"
							+ " ("
							+ " " + jc_id + ","
							+ " 2," // jctrans_customer_id
							+ " " + jcitem_item_id + ","
							+ " 1," // jctrans_discount
							+ " " + Integer.parseInt(jctrans_rowcount) + ","
							+ " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
							+ " " + jcitem_disc + "," // jctrans_price
							+ " " + df.format((Double.parseDouble(jcitem_disc) / Double.parseDouble(total) * 100)) + ","
							+ " " + jcitem_disc + "," // jctrans_amount
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + jctrans_billtype_id + ","
							+ " 1" // jctrans_dc
							+ ")";
					SOP("StrSql==Discount==" + StrSql);
					stmttx.addBatch(StrSql);

					StrSql = "SELECT ";
					if (gst_type.equals("state")) {
						StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax_customer_id1,"
								+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax_rate1,"
								+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax_customer_id2,"
								+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax_rate2,"
								+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
								+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
								+ " COALESCE(tax1.customer_id, 0) AS tax1_id,"
								+ " COALESCE(tax2.customer_id, 0) AS tax2_id,"
								+ " COALESCE(item_salestax2_aftertax1, 0) AS price_tax2_after_tax1";

						// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id3,"
						// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate3,";
						//
						// StrSql += " COALESCE(tax4.customer_name, '') AS tax3_name,"
						// + " COALESCE(tax4.customer_id, 0) AS tax3_id";
					}
					else if (gst_type.equals("central")) {
						StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax_customer_id1,"
								+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax_rate1,"
								+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
								+ " COALESCE(tax3.customer_id, 0) AS tax1_id,";
						// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id2,"
						// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate2,";

						// StrSql += " COALESCE(tax4.customer_name, '') AS tax2_name,"
						// + " COALESCE(tax4.customer_id, 0) AS tax2_id,";
						// StrSql += " '0' AS tax_customer_id3,"
						// + " '0' AS tax_rate3,";
						//
						StrSql += " '' AS tax2_name,"
								+ " '0' AS tax_rate2,"
								+ " '0' AS tax2_id";
					}

					StrSql += " FROM " + compdb(comp_id) + "axela_inventory_item";
					if (gst_type.equals("state")) {
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id = item_salestax1_ledger_id"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id = item_salestax2_ledger_id";
					} else if (gst_type.equals("central")) {
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = item_salestax3_ledger_id";
					}
					StrSql += " WHERE 1 = 1"
							+ " AND item_id = " + jcitem_item_id;
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql==" + StrSql);
					while (crs.next()) {
						tax1 = crs.getString("tax1_id");
						tax2 = crs.getString("tax2_id");
						// tax3 = crs.getString("tax3_id");
					}
					crs.close();

					// Tax entry in jc_trans table
					if (!tax1.equals("0")) {
						StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ("
								+ " jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ "	jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " ( SELECT"
								+ " " + jc_id + ","
								+ " customer_id," // jctrans_customer_id
								+ " " + jcitem_item_id + ","
								+ " 1," // jctrans_tax
								+ " customer_id," // jctrans_tax_id
								+ " " + Integer.parseInt(jctrans_rowcount) + ","
								+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
						if (gst_type.equals("state")) {
							StrSql += " ((tax1.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
									+ " (((tax1.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
						} else if (gst_type.equals("central")) {
							StrSql += " ((tax3.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
									+ " (((tax3.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
						}
						StrSql += " " + jctrans_billtype_id + ","
								+ " '" + ToLongDate(kknow()) + "'"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
								+ " AND price_id = ( SELECT"
								+ " price_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
								+ " WHERE price_rateclass_id = " + rateclass_id
								+ " AND price_item_id = " + jcitem_item_id
								+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
								+ " ORDER BY price_id DESC"
								+ " LIMIT 1 )";
						if (gst_type.equals("state")) {
							StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = item_salestax1_ledger_id";
						} else if (gst_type.equals("central")) {
							StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id";
						}
						StrSql += " WHERE 1=1"
								+ "	AND item_sales_ledger_id = 1"
								+ "	AND item_id = " + jcitem_item_id
								+ ")";

						// SOPInfo("StrSql==Tax1==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!tax2.equals("0")) {
						StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ("
								+ " jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ "	jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " ( SELECT"
								+ " " + jc_id + ","
								+ " customer_id," // jctrans_customer_id
								+ " " + jcitem_item_id + ","
								+ " 1," // jctrans_tax
								+ " customer_id," // jctrans_tax_id
								+ " " + Integer.parseInt(jctrans_rowcount) + ","
								+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
						if (gst_type.equals("state")) {
							StrSql += " ((tax2.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
									+ " (((tax2.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
						}
						StrSql += " " + jctrans_billtype_id + ","
								+ " '" + ToLongDate(kknow()) + "'"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
								+ " AND price_id = ( SELECT"
								+ " price_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
								+ " WHERE price_rateclass_id = " + rateclass_id
								+ " AND price_item_id = " + jcitem_item_id
								+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
								+ " ORDER BY price_id DESC"
								+ " LIMIT 1 )";
						if (gst_type.equals("state")) {
							StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = item_salestax2_ledger_id";
						}
						StrSql += " WHERE 1=1"
								+ "	AND item_sales_ledger_id = 1"
								+ " AND item_id = " + jcitem_item_id
								+ ")";
						// SOPInfo("StrSql==Tax2==" + StrSql);

						stmttx.addBatch(StrSql);
					}
					// if (!tax3.equals("0")) {
					// StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
					// + " ("
					// + " jctrans_jc_id,"
					// + " jctrans_customer_id,"
					// + " jctrans_item_id,"
					// + " jctrans_tax,"
					// + " jctrans_tax_id,"
					// + "	jctrans_rowcount,"
					// + "	jctrans_option_id,"
					// + " jctrans_price,"
					// + " jctrans_amount,"
					// + " jctrans_time)"
					// + " ( SELECT"
					// + " " + jc_id + ","
					// + " customer_id," // jctrans_customer_id
					// + " " + jcitem_item_id + ","
					// + " 1," // jctrans_tax
					// + " customer_id," // jctrans_tax_id
					// + " " + Integer.parseInt(jctrans_rowcount) + ","
					// + " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
					// + " (customer_rate" + " * " + jcitem_price + " / 100) AS jctrans_amount," // jctrans_price
					// + " ((customer_rate  * " + jcitem_price + " / 100) * " + jcitem_qty + ") AS jctrans_amount," // jctrans_amount
					// + " '" + ToLongDate(kknow()) + "'"
					// + " FROM " + compdb(comp_id) + "axela_inventory_item"
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id"
					// + " WHERE 1=1"
					// + "	AND item_sales_ledger_id = 1"
					// + " AND item_id = " + jcitem_item_id
					// + ")";
					//
					// // SOP("StrSql==Tax==" + StrSql);
					//
					// stmttx.addBatch(StrSql);
					// }

					stmttx.executeBatch();

					// To update Jc invoice if exists...
					if (!voucher_id.equals("0")) {
						// SOP("voucher_id===" + voucher_id);

						jctrans_rowcount = ExecuteQuery("SELECT MAX(COALESCE (vouchertrans_rowcount, 0)) + 1"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " WHERE vouchertrans_voucher_id =" + voucher_id);

						jctrans_option_id = jctrans_rowcount;

						// main item entry in voucher_trans table
						StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " ("
								+ " vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_location_id,"
								+ " vouchertrans_item_id,"
								+ " vouchertrans_discount,"
								+ " vouchertrans_tax,"
								+ "	vouchertrans_rowcount,"
								+ "	vouchertrans_option_id,"
								+ " vouchertrans_qty,"
								+ " vouchertrans_price,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_netprice,"
								+ " vouchertrans_discountamount,"
								+ " vouchertrans_taxamount,"
								+ "	vouchertrans_alt_qty,"
								+ "	vouchertrans_alt_uom_id,"
								+ "	vouchertrans_unit_cost,"
								+ "	vouchertrans_convfactor,"
								+ " vouchertrans_time,"
								+ "	vouchertrans_dc)"
								+ " VALUES"
								+ " ("
								+ " " + voucher_id + ","
								+ " 1," // jctrans_customer_id
								+ " " + jc_location_id + ","
								+ " " + jcitem_item_id + ","
								+ " 0," // jctrans_discount
								+ " 0," // jctrans_tax
								+ " " + Integer.parseInt(jctrans_rowcount) + ","
								+ "	0," // jctrans_option_id
								+ " " + jcitem_qty + ","
								+ " " + jcitem_price + ","
								+ " " + total + ","
								+ " " + total + ","
								+ " " + jcitem_disc + ","
								+ " 0,"
								+ " " + jcitem_qty + ","
								+ " 1," // jctrans_alt_uom_id
								+ " " + jcitem_price + ","
								+ " 1," // jctrans_convfactor
								+ " '" + ToLongDate(kknow()) + "',"
								+ " 0" // jctrans_dc
								+ ")";
						SOP("StrSql==Main Voucher Item==" + StrSql);
						stmttx.addBatch(StrSql);

						// Discount entry in voucher_trans table

						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " ("
								+ " vouchertrans_voucher_id,"
								+ " vouchertrans_customer_id,"
								+ " vouchertrans_item_id,"
								+ " vouchertrans_discount,"
								+ "	vouchertrans_rowcount,"
								+ "	vouchertrans_option_id,"
								+ " vouchertrans_price,"
								+ " vouchertrans_discount_perc,"
								+ " vouchertrans_amount,"
								+ " vouchertrans_time"
								+ " vouchertrans_dc"
								+ " )"
								+ " VALUES"
								+ " ("
								+ " " + voucher_id + ","
								+ " 2," // vouchertrans_customer_id
								+ " " + jcitem_item_id + ","
								+ " 1," // vouchertrans_discount
								+ " " + Integer.parseInt(jctrans_rowcount) + ","
								+ " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
								+ " " + jcitem_disc + "," // jctrans_price
								+ " " + df.format((Double.parseDouble(jcitem_disc) / Double.parseDouble(total) * 100)) + ","
								+ " " + jcitem_disc + "," // jctrans_amount
								+ " '" + ToLongDate(kknow()) + "',"
								+ " 1" // vouchertrans_dc
								+ ")";
						SOP("StrSql==Discount==" + StrSql);
						stmttx.addBatch(StrSql);

						// Tax entry in voucher_trans table

						if (!tax1.equals("0")) {
							StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
									+ " ("
									+ " vouchertrans_voucher_id,"
									+ " vouchertrans_customer_id,"
									+ " vouchertrans_item_id,"
									+ " vouchertrans_tax,"
									+ " vouchertrans_tax_id,"
									+ "	vouchertrans_rowcount,"
									+ "	vouchertrans_option_id,"
									+ " vouchertrans_price,"
									+ " vouchertrans_amount,"
									+ " vouchertrans_time)"
									+ " ( SELECT"
									+ " " + voucher_id + ","
									+ " customer_id," // jctrans_customer_id
									+ " " + jcitem_item_id + ","
									+ " 1," // jctrans_tax
									+ " customer_id," // jctrans_tax_id
									+ " " + Integer.parseInt(jctrans_rowcount) + ","
									+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
							if (gst_type.equals("state")) {
								StrSql += " ((tax1.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
										+ " (((tax1.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
							} else if (gst_type.equals("central")) {
								StrSql += " ((tax3.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
										+ " (((tax3.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
							}
							StrSql += " '" + ToLongDate(kknow()) + "'"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
									+ " AND price_id = ( SELECT"
									+ " price_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
									+ " WHERE price_rateclass_id = " + rateclass_id
									+ " AND price_item_id = " + jcitem_item_id
									+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
									+ " ORDER BY price_id DESC"
									+ " LIMIT 1 )";
							if (gst_type.equals("state")) {
								StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = item_salestax1_ledger_id";
							} else if (gst_type.equals("central")) {
								StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id";
							}
							StrSql += " WHERE 1=1"
									+ "	AND item_sales_ledger_id = 1"
									+ " AND item_id = " + jcitem_item_id
									+ ")";

							SOP("Str==VOUCHERSql==Tax==" + StrSql);
							stmttx.addBatch(StrSql);
						}
						if (!tax2.equals("0")) {
							StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
									+ " ("
									+ " vouchertrans_voucher_id,"
									+ " vouchertrans_customer_id,"
									+ " vouchertrans_item_id,"
									+ " vouchertrans_tax,"
									+ " vouchertrans_tax_id,"
									+ "	vouchertrans_rowcount,"
									+ "	vouchertrans_option_id,"
									+ " vouchertrans_price,"
									+ " vouchertrans_amount,"
									+ " vouchertrans_time)"
									+ " ( SELECT"
									+ " " + voucher_id + ","
									+ " customer_id," // jctrans_customer_id
									+ " " + jcitem_item_id + ","
									+ " 1," // jctrans_tax
									+ " customer_id," // jctrans_tax_id
									+ " " + Integer.parseInt(jctrans_rowcount) + ","
									+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
							if (gst_type.equals("state")) {
								StrSql += " ((tax2.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
										+ " (((tax2.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
							}
							StrSql += " '" + ToLongDate(kknow()) + "'"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
									+ " AND price_id = ( SELECT"
									+ " price_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
									+ " WHERE price_rateclass_id = " + rateclass_id
									+ " AND price_item_id = " + jcitem_item_id
									+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
									+ " ORDER BY price_id DESC"
									+ " LIMIT 1 )";
							if (gst_type.equals("state")) {
								StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = item_salestax2_ledger_id";
							}
							StrSql += " WHERE 1=1"
									+ "	AND item_sales_ledger_id = 1"
									+ " AND item_id = " + jcitem_item_id
									+ ")";

							SOP("StrSql voucher==Tax2==" + StrSql);
							stmttx.addBatch(StrSql);
						}
						// if (!tax3.equals("0")) {
						// StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
						// + " ("
						// + " vouchertrans_voucher_id,"
						// + " vouchertrans_customer_id,"
						// + " vouchertrans_item_id,"
						// + " vouchertrans_tax,"
						// + " vouchertrans_tax_id,"
						// + "	vouchertrans_rowcount,"
						// + "	vouchertrans_option_id,"
						// + " vouchertrans_price,"
						// + " vouchertrans_amount,"
						// + " vouchertrans_time)"
						// + " ( SELECT"
						// + " " + voucher_id + ","
						// + " customer_id," // jctrans_customer_id
						// + " " + jcitem_item_id + ","
						// + " 1," // jctrans_tax
						// + " customer_id," // jctrans_tax_id
						// + " " + Integer.parseInt(jctrans_rowcount) + ","
						// + " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
						// + " (customer_rate" + " * " + jcitem_price + " / 100) AS jctrans_amount," // jctrans_price
						// + " ((customer_rate  * " + jcitem_price + " / 100) * " + jcitem_qty + ") AS jctrans_amount," // jctrans_amount
						// + " '" + ToLongDate(kknow()) + "'"
						// + " FROM " + compdb(comp_id) + "axela_inventory_item"
						// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id"
						// + " WHERE 1=1"
						// + "	AND item_sales_ledger_id = 1"
						// + " AND item_id = " + jcitem_item_id
						// + ")";
						//
						// // SOP("StrSql=voucher=Tax3==" + StrSql);
						// stmttx.addBatch(StrSql);
						// }

						stmttx.executeBatch();

						conntx.commit();
						balanceVoucherTrans(voucher_id, customer_id);

					}

					// StrSql = "SELECT CONCAT(item_name, IF(item_code != '', CONCAT(' (', item_code, ')'), ''))"
					// + " FROM " + compdb(comp_id) + "axela_inventory_item"
					// + " WHERE item_id = " + jcitem_item_id + "";

					StrSql = "SELECT CONCAT( item_name, IF ( item_code != '', CONCAT(' (', item_code, ')'), '' ), ' QTY=',"
							+ " jctrans_qty, ' Price=', jctrans_price )"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = " + jcitem_item_id
							+ " WHERE jctrans_jc_id = " + jc_id
							+ " AND jctrans_rowcount != 0"
							+ " AND jctrans_option_id = 0";

					// String history_newvalue = ExecuteQuery(StrSql).replace("'", "&#39;") + " QTY=" + df.format(Double.parseDouble(jcitem_qty)) + " Price="
					// + df.format(Double.parseDouble(jcitem_price))
					// + "";
					String history_newvalue = ExecuteQuery(StrSql).replace("'", "&#39;");
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + type_name + "_ADDED',"
							+ " '',"
							+ " '" + history_newvalue + "')";
					stmttx.execute(StrSql);

					if (!jcitem_item_serial.equals("")) {
						StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_stockserial"
								+ " WHERE stockserial_location_id = " + jc_location_id + ""
								+ " AND stockserial_item_id = " + jcitem_item_id + ""
								+ " AND stockserial_serial_no = '" + jcitem_item_serial + "'";
						stmttx.execute(StrSql);
					}

					conntx.commit();

					UpdateJCDetails();

					conntx.commit();

				}

				// update the Inventory Current Stock by calling Connect class
				// function
				if (msg.equals("")) {
					// UpdateStock(comp_id, "0", "0");
				}
			} else if (jctrans_billtype_id.equals("1")) {
				msg = "<br>Item Already Present!";
			} else if (jctrans_billtype_id.equals("2")) {
				insur_msg = "<br>Item Already Present!";
			} else if (jctrans_billtype_id.equals("3")) {
				warranty_msg = "<br>Item Already Present!";
			}
			// CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", jc_location_id, comp_id, "0", "yes");
			// Thread thread = new Thread(calccurrentstockthread);
			// thread.start();
			ListJCItems();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void UpdateJCItem() throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String tax1 = "0", tax2 = "0", tax3 = "0";

			StrSql = "SELECT CONCAT( item_name, IF ( item_code != '', CONCAT(' (', item_code, ')'), '' ), ' QTY=',"
					+ " jctrans_qty, ' Price=', jctrans_price )"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = " + jcitem_item_id
					+ " WHERE jctrans_jc_id = " + jc_id
					+ " AND jctrans_rowcount != 0"
					+ " AND jctrans_option_id = 0";

			// SOP("UpdateJCItem===" + StrSql);
			String history_oldvalue = ExecuteQuery(StrSql).replace("'", "&#39;");

			// insert jc_trans/////////////////////

			jctrans_rowcount = CNumeric(ExecuteQuery("SELECT jctrans_rowcount"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_jc_id = " + jc_id
					+ " AND jctrans_item_id = " + jcitem_item_id));
			jctrans_option_id = jctrans_rowcount;

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_item_id = " + jcitem_item_id + ""
					+ " AND jctrans_billtype_id = " + jctrans_billtype_id
					+ " AND jctrans_jc_id = " + jc_id;
			SOP("StrSql==Delete==" + StrSql);
			updateQuery(StrSql);

			String total = df.format(Double.parseDouble(jcitem_price) * Double.parseDouble(jcitem_qty)) + "";
			if (total.equals("0.00")) {
				total = "1";
			}
			StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
					+ " ("
					+ " jctrans_jc_id,"
					+ " jctrans_customer_id,"
					+ " jctrans_rateclass_id,"
					+ "	jctrans_location_id,"
					+ " jctrans_item_id,"
					+ " jctrans_discount,"
					+ " jctrans_tax,"
					+ "	jctrans_rowcount,"
					+ "	jctrans_option_id,"
					+ " jctrans_qty,"
					+ " jctrans_price,"
					+ " jctrans_amount,"
					+ " jctrans_netprice,"
					+ " jctrans_discountamount,"
					+ " jctrans_taxamount,"
					+ "	jctrans_alt_qty,"
					+ "	jctrans_alt_uom_id,"
					+ "	jctrans_unit_cost,"
					+ "	jctrans_convfactor,"
					+ " jctrans_time,"
					+ "	jctrans_billtype_id,"
					+ "	jctrans_dc)"
					+ " VALUES"
					+ " ("
					+ " " + jc_id + ","
					+ " 1," // jctrans_customer_id
					+ " " + jcitem_rateclass_id + ","
					+ " " + jc_location_id + ","
					+ " " + jcitem_item_id + ","
					+ " 0," // jctrans_discount
					+ " 0," // jctrans_tax
					+ " " + Integer.parseInt(jctrans_rowcount) + ","
					+ "	0," // jctrans_option_id
					+ " " + jcitem_qty + ","
					+ " " + jcitem_price + ","
					+ " " + total + ","
					+ " " + total + ","
					+ " " + jcitem_disc + ","
					+ " 0,"
					+ " " + jcitem_qty + ","
					+ " 1," // jctrans_alt_uom_id
					+ " " + jcitem_price + ","
					+ " 1," // jctrans_convfactor
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + jctrans_billtype_id + ","
					+ " 1" // jctrans_dc
					+ ")";
			stmttx.addBatch(StrSql);

			// Discount entry in jc_trans table
			StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
					+ " ("
					+ " jctrans_jc_id,"
					+ " jctrans_customer_id,"
					+ " jctrans_item_id,"
					+ " jctrans_discount,"
					+ "	jctrans_rowcount,"
					+ "	jctrans_option_id,"
					+ " jctrans_price,"
					+ " jctrans_discount_perc,"
					+ " jctrans_amount,"
					+ "	jctrans_billtype_id,"
					+ " jctrans_time)"
					+ " VALUES"
					+ " ("
					+ " " + jc_id + ","
					+ " 2," // jctrans_customer_id
					+ " " + jcitem_item_id + ","
					+ " 1," // jctrans_discount
					+ " " + Integer.parseInt(jctrans_rowcount) + ","
					+ " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
					+ " " + jcitem_disc + "," // jctrans_price
					+ " " + df.format((Double.parseDouble(jcitem_disc) / Double.parseDouble(total) * 100)) + ","
					+ " " + jcitem_disc + "," // jctrans_amount
					+ " " + jctrans_billtype_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			SOP("StrSql==Discount==" + StrSql);
			stmttx.addBatch(StrSql);

			StrSql = "SELECT ";
			if (gst_type.equals("state")) {
				StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax_customer_id1,"
						+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax_rate1,"
						+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax_customer_id2,"
						+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax_rate2,"
						+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
						+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
						+ " COALESCE(tax1.customer_id, 0) AS tax1_id,"
						+ " COALESCE(tax2.customer_id, 0) AS tax2_id,"
						+ " COALESCE(item_salestax2_aftertax1, 0) AS price_tax2_after_tax1";

				// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id3,"
				// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate3,";
				//
				// StrSql += " COALESCE(tax4.customer_name, '') AS tax3_name,"
				// + " COALESCE(tax4.customer_id, 0) AS tax3_id";
			}
			else if (gst_type.equals("central")) {
				StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax_customer_id1,"
						+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax_rate1,"
						+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
						+ " COALESCE(tax3.customer_id, 0) AS tax1_id,";
				// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id2,"
				// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate2,";
				//
				// StrSql += " COALESCE(tax4.customer_name, '') AS tax2_name,"
				// + " COALESCE(tax4.customer_id, 0) AS tax2_id,";
				// StrSql += " '0' AS tax_customer_id3,"
				// + " '0' AS tax_rate3,";

				StrSql += " '' AS tax2_name,"
						+ " 0 AS tax_rate2,"
						+ " 0 AS tax2_id";
			}

			StrSql += " FROM " + compdb(comp_id) + "axela_inventory_item";
			if (gst_type.equals("state")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id = item_salestax1_ledger_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id = item_salestax2_ledger_id";
			} else if (gst_type.equals("central")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = item_salestax3_ledger_id";
			}
			// " LEFT JOIN " + compdb(comp_id) + "axela_customer tax4 on tax4.customer_id = item_salestax4_ledger_id"
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " AND price_id = ( SELECT"
					+ " price_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_rateclass_id = " + rateclass_id
					+ " AND price_item_id = " + jcitem_item_id
					+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
					+ " ORDER BY price_id DESC"
					+ " LIMIT 1 )"
					+ " WHERE 1 = 1"
					+ " AND item_id = " + jcitem_item_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("StrSql=1235=" + StrSql);
			while (crs.next()) {
				SOP("tax1 = " + crs.getString("tax1_id"));
				SOP("tax2 = " + crs.getString("tax2_id"));
				tax1 = crs.getString("tax1_id");
				tax2 = crs.getString("tax2_id");
				// tax3 = crs.getString("tax3_id");
			}
			crs.close();
			// Tax entry in jc_trans table
			if (!tax1.equals("0")) {
				StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
						+ " ("
						+ " jctrans_jc_id,"
						+ " jctrans_customer_id,"
						+ " jctrans_item_id,"
						+ " jctrans_tax,"
						+ " jctrans_tax_id,"
						+ "	jctrans_rowcount,"
						+ "	jctrans_option_id,"
						+ " jctrans_price,"
						+ " jctrans_amount,"
						+ "	jctrans_billtype_id,"
						+ " jctrans_time)"
						+ " ( SELECT"
						+ " " + jc_id + ","
						+ " customer_id," // jctrans_customer_id
						+ " " + jcitem_item_id + ","
						+ " 1," // jctrans_tax
						+ " customer_id,"// jctrans_tax_id
						+ " " + Integer.parseInt(jctrans_rowcount) + ","
						+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
				if (gst_type.equals("state")) {
					StrSql += " ((tax1.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
							+ " (((tax1.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
				} else if (gst_type.equals("central")) {
					StrSql += " ((tax3.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
							+ " (((tax3.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
				}
				StrSql += " " + jctrans_billtype_id + ","
						+ " '" + ToLongDate(kknow()) + "'"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
						+ " AND price_id = ( SELECT"
						+ " price_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
						+ " WHERE price_rateclass_id = " + rateclass_id
						+ " AND price_item_id = " + jcitem_item_id
						+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
						+ " ORDER BY price_id DESC"
						+ " LIMIT 1 )";
				if (gst_type.equals("state")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = item_salestax1_ledger_id";
				} else if (gst_type.equals("central")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id";
				}
				StrSql += " WHERE 1=1"
						+ "	AND item_sales_ledger_id = 1"
						+ " AND item_id = " + jcitem_item_id
						+ ")";

				SOP("StrSql==Tax==" + StrSql);

				stmttx.addBatch(StrSql);
			}
			if (!tax2.equals("0")) {
				StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
						+ " ("
						+ " jctrans_jc_id,"
						+ " jctrans_customer_id,"
						+ " jctrans_item_id,"
						+ " jctrans_tax,"
						+ " jctrans_tax_id,"
						+ "	jctrans_rowcount,"
						+ "	jctrans_option_id,"
						+ " jctrans_price,"
						+ " jctrans_amount,"
						+ "	jctrans_billtype_id,"
						+ " jctrans_time)"
						+ " ( SELECT"
						+ " " + jc_id + ","
						+ " customer_id," // jctrans_customer_id
						+ " " + jcitem_item_id + ","
						+ " 1," // jctrans_tax
						+ " customer_id," // jctrans_tax_id
						+ " " + Integer.parseInt(jctrans_rowcount) + ","
						+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
				if (gst_type.equals("state")) {
					StrSql += " ((tax2.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
							+ " (((tax2.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
				}
				StrSql += " " + jctrans_billtype_id + ","
						+ " '" + ToLongDate(kknow()) + "'"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
						+ " AND price_id = ( SELECT"
						+ " price_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
						+ " WHERE price_rateclass_id = " + rateclass_id
						+ " AND price_item_id = " + jcitem_item_id
						+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
						+ " ORDER BY price_id DESC"
						+ " LIMIT 1 )";

				if (gst_type.equals("state")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = item_salestax2_ledger_id";
				}
				StrSql += " WHERE 1=1"
						+ "	AND item_sales_ledger_id = 1"
						+ " AND item_id = " + jcitem_item_id
						+ ")";

//				SOP("StrSql==Tax=2=" + StrSql);

				stmttx.addBatch(StrSql);
			}
			// if (!tax3.equals("0")) {
			// StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
			// + " ("
			// + " jctrans_jc_id,"
			// + " jctrans_customer_id,"
			// + " jctrans_item_id,"
			// + " jctrans_tax,"
			// + " jctrans_tax_id,"
			// + "	jctrans_rowcount,"
			// + "	jctrans_option_id,"
			// + " jctrans_price,"
			// + " jctrans_amount,"
			// + " jctrans_time)"
			// + " ( SELECT"
			// + " " + jc_id + ","
			// + " customer_id," // jctrans_customer_id
			// + " " + jcitem_item_id + ","
			// + " 1," // jctrans_tax
			// + " customer_id," // jctrans_tax_id
			// + " " + Integer.parseInt(jctrans_rowcount) + ","
			// + " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
			// + " (customer_rate" + " * " + jcitem_price + " / 100) AS jctrans_amount," // jctrans_price
			// + " ((customer_rate  * " + jcitem_price + " / 100) * " + jcitem_qty + ") AS jctrans_amount," // jctrans_amount
			// + " '" + ToLongDate(kknow()) + "'"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item"
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id"
			// + " WHERE 1=1"
			// + "	AND item_sales_ledger_id = 1"
			// + " AND item_id = " + jcitem_item_id
			// + ")";
			//
			// // SOP("StrSql==Tax==" + StrSql);
			//
			// stmttx.addBatch(StrSql);
			// }

			stmttx.executeBatch();

			// ////////////////////////////////////

			// add voucher_trans////////////////

			if (!voucher_id.equals("0")) {

				jctrans_rowcount = ExecuteQuery("SELECT vouchertrans_rowcount"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id =" + voucher_id
						+ " AND vouchertrans_item_id = " + jcitem_item_id);

				jctrans_option_id = jctrans_rowcount;

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_item_id = " + jcitem_item_id + ""
						+ " AND vouchertrans_voucher_id = " + voucher_id;

				updateQuery(StrSql);
			}

			if (!voucher_id.equals("0")) {

				// main item entry in voucher_trans table
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ "	vouchertrans_location_id,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_discount,"
						+ " vouchertrans_tax,"
						+ "	vouchertrans_rowcount,"
						+ "	vouchertrans_option_id,"
						+ " vouchertrans_qty,"
						+ " vouchertrans_price,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_netprice,"
						+ " vouchertrans_discountamount,"
						+ " vouchertrans_taxamount,"
						+ "	vouchertrans_alt_qty,"
						+ "	vouchertrans_alt_uom_id,"
						+ "	vouchertrans_unit_cost,"
						+ "	vouchertrans_convfactor,"
						+ " vouchertrans_time,"
						+ "	vouchertrans_dc)"
						+ " VALUES"
						+ " ("
						+ " " + voucher_id + ","
						+ " 1," // jctrans_customer_id
						+ " " + jc_location_id + ","
						+ " " + jcitem_item_id + ","
						+ " 0," // jctrans_discount
						+ " 0," // jctrans_tax
						+ " " + Integer.parseInt(jctrans_rowcount) + ","
						+ "	0," // jctrans_option_id
						+ " " + jcitem_qty + ","
						+ " " + jcitem_price + ","
						+ " " + total + ","
						+ " " + total + ","
						+ " " + jcitem_disc + ","
						+ " 0,"// jctrans_taxamount
						+ " " + jcitem_qty + ","
						+ " 1," // jctrans_alt_uom_id
						+ " " + jcitem_price + ","
						+ " 1," // jctrans_convfactor
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 1" // jctrans_dc
						+ ")";

				stmttx.addBatch(StrSql);

				// Discount entry in voucher_trans table
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_discount,"
						+ "	vouchertrans_rowcount,"
						+ "	vouchertrans_option_id,"
						+ " vouchertrans_price,"
						+ " vouchertrans_discount_perc,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time)"
						+ " VALUES"
						+ " ("
						+ " " + voucher_id + ","
						+ " 2," // jctrans_customer_id
						+ " " + jcitem_item_id + ","
						+ " 1," // jctrans_discount
						+ " " + Integer.parseInt(jctrans_rowcount) + ","
						+ " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
						+ " " + jcitem_disc + "," // jctrans_price
						+ " " + df.format((Double.parseDouble(jcitem_disc) / Double.parseDouble(total) * 100)) + ","
						+ " " + jcitem_disc + "," // jctrans_amount
						+ " '" + ToLongDate(kknow()) + "')";
				// SOP("StrSql==Discount==" + StrSql);
				stmttx.addBatch(StrSql);

				// Tax entry in voucher_trans table
				if (!tax1.equals("0")) {
					StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " ( SELECT"
							+ " " + voucher_id + ","
							+ " customer_id," // jctrans_customer_id
							+ " " + jcitem_item_id + ","
							+ " 1," // jctrans_tax
							+ " customer_id," // jctrans_tax_id
							+ " " + Integer.parseInt(jctrans_rowcount) + ","
							+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
					if (gst_type.equals("state")) {
						StrSql += " ((tax1.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
								+ " (((tax1.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
					} else if (gst_type.equals("central")) {
						StrSql += " ((tax3.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
								+ " (((tax3.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
					}
					StrSql += " '" + ToLongDate(kknow()) + "'"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
							+ " AND price_id = ( SELECT"
							+ " price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
							+ " WHERE price_rateclass_id = " + rateclass_id
							+ " AND price_item_id = " + jcitem_item_id
							+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
							+ " ORDER BY price_id DESC"
							+ " LIMIT 1 )";
					if (gst_type.equals("state")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax1 ON tax1.customer_id = item_salestax1_ledger_id";
					} else if (gst_type.equals("central")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id";
					}
					StrSql += " WHERE 1=1"
							+ "	AND item_sales_ledger_id = 1"
							+ " AND item_id = " + jcitem_item_id
							+ ")";

					// SOP("Str==VOUCHERSql==Tax==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				if (!tax2.equals("0")) {
					StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " ( SELECT"
							+ " " + voucher_id + ","
							+ " customer_id," // jctrans_customer_id
							+ " " + jcitem_item_id + ","
							+ " 1," // jctrans_tax
							+ " customer_id," // jctrans_tax_id
							+ " " + Integer.parseInt(jctrans_rowcount) + ","
							+ " " + Integer.parseInt(jctrans_option_id) + ","; // jctrans_option_id
					if (gst_type.equals("state")) {
						StrSql += " ((tax2.customer_rate * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) AS jctrans_amount," // jctrans_price
								+ " (((tax2.customer_rate  * " + (Double.parseDouble(jcitem_price) - Double.parseDouble(jcitem_disc)) + ") / 100) * " + jcitem_qty + ") AS jctrans_amount,"; // jctrans_amount
					}
					StrSql += " '" + ToLongDate(kknow()) + "'"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
							+ " AND price_id = ( SELECT"
							+ " price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
							+ " WHERE price_rateclass_id = " + rateclass_id
							+ " AND price_item_id = " + jcitem_item_id
							+ " AND price_effective_from < '" + ToLongDate(kknow()) + "'"
							+ " ORDER BY price_id DESC"
							+ " LIMIT 1 )";
					if (gst_type.equals("state")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_customer tax2 ON tax2.customer_id = item_salestax2_ledger_id";
					}
					StrSql += " WHERE 1=1"
							+ "	AND item_sales_ledger_id = 1"
							+ " AND item_id = " + jcitem_item_id
							+ ")";

					// SOP("StrSql voucher==Tax2==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				// if (!tax3.equals("0")) {
				// StrSql = " INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
				// + " ("
				// + " vouchertrans_voucher_id,"
				// + " vouchertrans_customer_id,"
				// + " vouchertrans_item_id,"
				// + " vouchertrans_tax,"
				// + " vouchertrans_tax_id,"
				// + "	vouchertrans_rowcount,"
				// + "	vouchertrans_option_id,"
				// + " vouchertrans_price,"
				// + " vouchertrans_amount,"
				// + " vouchertrans_time)"
				// + " ( SELECT"
				// + " " + voucher_id + ","
				// + " customer_id," // jctrans_customer_id
				// + " " + jcitem_item_id + ","
				// + " 1," // jctrans_tax
				// + " customer_id," // jctrans_tax_id
				// + " " + Integer.parseInt(jctrans_rowcount) + ","
				// + " " + Integer.parseInt(jctrans_option_id) + "," // jctrans_option_id
				// + " (customer_rate" + " * " + jcitem_price + " / 100) AS jctrans_amount," // jctrans_price
				// + " ((customer_rate  * " + jcitem_price + " / 100) * " + jcitem_qty + ") AS jctrans_amount," // jctrans_amount
				// + " '" + ToLongDate(kknow()) + "'"
				// + " FROM " + compdb(comp_id) + "axela_inventory_item"
				// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_customer tax3 ON tax3.customer_id = item_salestax3_ledger_id"
				// + " WHERE 1=1"
				// + "	AND item_sales_ledger_id = 1"
				// + " AND item_id = " + jcitem_item_id
				// + ")";
				//
				// // SOP("StrSql=voucher=Tax3==" + StrSql);
				// stmttx.addBatch(StrSql);
				// }

				stmttx.executeBatch();

				conntx.commit();

				balanceVoucherTrans(voucher_id, customer_id);
			}

			// //////////////////////////////////

			// StrSql = "SELECT CONCAT(item_name, IF(item_code != '', concat(' (', item_code, ')'), ''))"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item"
			// + " WHERE item_id = " + jcitem_item_id + "";

			StrSql = "SELECT CONCAT( item_name, IF ( item_code != '', CONCAT(' (', item_code, ')'), '' ), ' QTY=',"
					+ " jctrans_qty, ' Price=', jctrans_price )"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = " + jcitem_item_id
					+ " WHERE jctrans_jc_id = " + jc_id
					+ " AND jctrans_rowcount != 0"
					+ " AND jctrans_option_id = 0";

			String history_newvalue = ExecuteQuery(StrSql).replace("'", "&#39;") + " QTY=" + df.format(Double.parseDouble(jcitem_qty)) + " Price=" + df.format(Double.parseDouble(jcitem_price)) +
					"";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + type_name + "_UPDATED',"
					+ " '" + history_oldvalue + "',"
					+ " '" + history_newvalue + "')";
			stmttx.execute(StrSql);
			conntx.commit();

			UpdateJCDetails();
			conntx.commit();

			// update the Inventory Current Stock by calling Connect class
			// function
			if (msg.equals("")) {
				// UpdateStock(comp_id, "0", "0");
			}
			// CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", jc_location_id, comp_id, "0", "yes");
			// Thread thread = new Thread(calccurrentstockthread);
			// thread.start();
			ListJCItems();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}
	public void balanceVoucherTrans(String voucher_id, String customer_id) {
		String voucher_total = CNumeric(PadQuotes(ExecuteQuery("SELECT sum( ( COALESCE(vouchertrans_amount,0)"
				+ " - COALESCE(vouchertrans_discountamount,0) )"
				+ " + COALESCE(vouchertrans_taxamount,0) ) AS total"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE 1 = 1"
				+ " AND	vouchertrans_voucher_id = " + voucher_id
				+ " AND vouchertrans_rowcount != 0"
				+ " AND vouchertrans_option_id = 0;")));
		String voucher_tax = CNumeric(PadQuotes(ExecuteQuery("select sum(vouchertrans_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE vouchertrans_voucher_id  = " + voucher_id
				+ " AND vouchertrans_tax = 1")));
		String voucher_dis = CNumeric(PadQuotes(ExecuteQuery("select sum(vouchertrans_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " WHERE vouchertrans_voucher_id  = " + voucher_id
				+ " AND vouchertrans_discount = 1")));

		updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " SET"
				+ " vouchertrans_amount = " + (Double.parseDouble(voucher_total)
						+ Double.parseDouble(voucher_tax)
						- Double.parseDouble(voucher_dis)) + ""
				+ " WHERE vouchertrans_customer_id=" + customer_id
				+ " AND vouchertrans_voucher_id = " + voucher_id);

		updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher"
				+ " SET"
				+ " voucher_amount = " + (Double.parseDouble(voucher_total)
						+ Double.parseDouble(voucher_tax)
						- Double.parseDouble(voucher_dis)) + ""
				+ " WHERE voucher_id = " + voucher_id);

	}
	public void DeleteJCItem() throws SQLException {
		try {
			SOP("DeleteJCItem==");
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String option_id = "";

			SOP("jctrans_billtype_id==" + jctrans_billtype_id);
			// ---------------
			// //Adding Item Serial no. from
			// " + compdb(comp_id) + "axela_inventory_stockserial table while
			// deleting it from " + compdb(comp_id) + "axela_service_jc_item
			// table
			// StrSql = "SELECT"
			// + " jcitem_item_id,"
			// + " jcitem_item_serial"
			// + " FROM " + compdb(comp_id) + "axela_service_jc_item"
			// + " WHERE jcitem_id = " + jcitem_id + "";
			// CachedRowSet crs = processQuery(StrSql, 0);

			// while (crs.next()) {
			// if (!crs.getString("jcitem_item_serial").equals("") && !crs.getString("jcitem_item_serial").equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stockserial"
					+ " (stockserial_item_id,"
					+ " stockserial_serial_no,"
					+ " stockserial_location_id,"
					+ " stockserial_time)"
					+ " VALUES"
					+ " (" + jcitem_id + ","
					+ " ' ',"
					+ " " + jc_location_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			stmttx.execute(StrSql);
			// }
			// }
			// crs.close();
			// //----------------
			// StrSql = "SELECT CONCAT(item_name, IF(item_code != '', CONCAT(' (', item_code, ')'), ''), ' QTY=', jcitem_qty, ' Price=', jcitem_price)"
			// + " FROM " + compdb(comp_id) + "axela_service_jc_item"
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = jcitem_item_id"
			// + " WHERE jcitem_id = " + jcitem_id + "";
			StrSql = "SELECT CONCAT( item_name, IF ( item_code != '', CONCAT(' (', item_code, ')'), '' ), ' QTY=',"
					+ " jctrans_qty, ' Price=', jctrans_price )"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = " + jcitem_item_id
					+ " WHERE jctrans_jc_id = " + jc_id
					+ " AND jctrans_billtype_id = " + jctrans_billtype_id
					+ " AND jctrans_rowcount != 0"
					+ " AND jctrans_option_id = 0";
			SOP("StrSql==" + StrSql);
			String history_oldvalue = ExecuteQuery(StrSql).replace("'", "&#39;");
			// StrSql = "SELECT jcitem_rowcount FROM " + compdb(comp_id) + "axela_service_jc_item"
			// + " WHERE jcitem_id = " + jcitem_id + ""
			// + " AND jcitem_jc_id = " + jc_id + "";
			// option_id = CNumeric(ExecuteQuery(StrSql));
			//
			// if (!option_id.equals("0")) {
			// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_item"
			// + " WHERE jcitem_option_id = " + option_id + ""
			// + " AND jcitem_jc_id = " + jc_id + "";
			// stmttx.execute(StrSql);
			// }
			//
			// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_item"
			// + " WHERE jcitem_id = " + jcitem_id + ""
			// + " AND jcitem_jc_id = " + jc_id + "";
			// stmttx.execute(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_item_id = " + jcitem_item_id + ""
					+ " AND jctrans_billtype_id = " + jctrans_billtype_id
					+ " AND jctrans_jc_id = " + jc_id;
			SOP("StrSql==Delete==" + StrSql);
			stmttx.execute(StrSql);
			if (!voucher_id.equals("0")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_item_id = " + jcitem_item_id + ""
						+ " AND vouchertrans_voucher_id = " + voucher_id;

				stmttx.execute(StrSql);
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + type_name + "_DELETED',"
					+ " '" + history_oldvalue + "',"
					+ " '')";
			stmttx.execute(StrSql);
			conntx.commit();
			if (!voucher_id.equals("0")) {
				balanceVoucherTrans(voucher_id, customer_id);
				// String voucher_total = CNumeric(PadQuotes(ExecuteQuery("SELECT sum( ( COALESCE(vouchertrans_amount,0)"
				// + " - COALESCE(vouchertrans_discountamount,0) )"
				// + " + COALESCE(vouchertrans_taxamount,0) ) AS total"
				// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				// + " WHERE 1 = 1"
				// + " AND	vouchertrans_voucher_id = " + voucher_id
				// + " AND vouchertrans_rowcount != 0"
				// + " AND vouchertrans_option_id = 0;")));
				// updateQuery("UPDATE " + compdb(comp_id) + "axela_acc_voucher"
				// + " SET"
				// + " voucher_amount = " + voucher_total
				// + " WHERE voucher_id = " + voucher_id);
			}
			StrSql = "SELECT jctrans_jc_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE  jctrans_jc_id = " + jc_id
					+ " AND jctrans_billtype_id = " + jctrans_billtype_id;
			SOP("StrSql==StrSql==" + StrSql);
			SOP("156==" + CNumeric(PadQuotes(ExecuteQuery(StrSql))));
			if (CNumeric(PadQuotes(ExecuteQuery(StrSql))).equals("0")) {
				SOP("456");
				if (jctrans_billtype_id.equals("1") || jctrans_billtype_id.equals("4") || jctrans_billtype_id.equals("5")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_cash_parts = 0.00,"
							+ " jc_bill_cash_parts_tyre_qty = 0.00,"
							+ " jc_bill_cash_parts_tyre = 0.00,"
							+ " jc_bill_cash_parts_oil = 0.00,"
							+ " jc_bill_cash_parts_battery_qty = 0.00,"
							+ " jc_bill_cash_parts_battery = 0.00,"
							+ " jc_bill_cash_parts_brake_qty = 0.00,"
							+ " jc_bill_cash_parts_brake = 0.00,"
							+ " jc_bill_cash_parts_accessories = 0.00,"
							+ " jc_bill_cash_parts_valueadd = 0.00,"
							+ " jc_bill_cash_parts_extwarranty_qty = 0.00,"
							+ " jc_bill_cash_parts_extwarranty = 0.00,"
							+ " jc_bill_cash_parts_wheelalign = 0.00,"
							+ " jc_bill_cash_parts_cng = 0.00,"
							+ " jc_bill_cash_labour_tyre_qty = 0.00,"
							+ " jc_bill_cash_labour_tyre = 0.00,"
							+ " jc_bill_cash_labour_oil = 0.00,"
							+ " jc_bill_cash_labour_battery_qty = 0.00,"
							+ " jc_bill_cash_labour_battery = 0.00,"
							+ " jc_bill_cash_labour_brake_qty = 0.00,"
							+ " jc_bill_cash_labour_brake = 0.00,"
							+ " jc_bill_cash_labour_accessories = 0.00,"
							+ " jc_bill_cash_labour_valueadd = 0.00,"
							+ " jc_bill_cash_labour_extwarranty_qty = 0.00,"
							+ " jc_bill_cash_labour_extwarranty = 0.00,"
							+ " jc_bill_cash_labour_wheelalign = 0.00,"
							+ " jc_bill_cash_labour_cng = 0.00"
							+ " WHERE jc_id = " + jc_id;
					stmttx.execute(StrSql);
				}
				if (jctrans_billtype_id.equals("2")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_insur_parts = 0.00,"
							+ " jc_bill_insur_parts_tyre_qty = 0.00,"
							+ " jc_bill_insur_parts_tyre = 0.00,"
							+ " jc_bill_insur_parts_oil = 0.00,"
							+ " jc_bill_insur_parts_battery_qty = 0.00,"
							+ " jc_bill_insur_parts_battery = 0.00,"
							+ " jc_bill_insur_parts_brake_qty = 0.00,"
							+ " jc_bill_insur_parts_brake = 0.00,"
							+ " jc_bill_insur_parts_accessories = 0.00,"
							+ " jc_bill_insur_parts_valueadd = 0.00,"
							+ " jc_bill_insur_parts_extwarranty_qty = 0.00,"
							+ " jc_bill_insur_parts_extwarranty = 0.00,"
							+ " jc_bill_insur_parts_wheelalign = 0.00,"
							+ " jc_bill_insur_parts_cng = 0.00,"
							+ " jc_bill_insur_labour_tyre_qty = 0.00,"
							+ " jc_bill_insur_labour_tyre = 0.00,"
							+ " jc_bill_insur_labour_oil = 0.00,"
							+ " jc_bill_insur_labour_battery_qty = 0.00,"
							+ " jc_bill_insur_labour_battery = 0.00,"
							+ " jc_bill_insur_labour_brake_qty = 0.00,"
							+ " jc_bill_insur_labour_brake = 0.00,"
							+ " jc_bill_insur_labour_accessories = 0.00,"
							+ " jc_bill_insur_labour_valueadd = 0.00,"
							+ " jc_bill_insur_labour_extwarranty_qty = 0.00,"
							+ " jc_bill_insur_labour_extwarranty = 0.00,"
							+ " jc_bill_insur_labour_wheelalign = 0.00,"
							+ " jc_bill_insur_labour_cng = 0.00"
							+ " WHERE jc_id = " + jc_id;
					SOP("StrSql==StrSqlupd==" + StrSql);
					stmttx.execute(StrSql);
				}
				if (jctrans_billtype_id.equals("3")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_warranty_parts = 0.00,"
							+ " jc_bill_warranty_parts_tyre_qty = 0.00,"
							+ " jc_bill_warranty_parts_tyre = 0.00,"
							+ " jc_bill_warranty_parts_oil = 0.00,"
							+ " jc_bill_warranty_parts_battery_qty = 0.00,"
							+ " jc_bill_warranty_parts_battery = 0.00,"
							+ " jc_bill_warranty_parts_brake_qty = 0.00,"
							+ " jc_bill_warranty_parts_brake = 0.00,"
							+ " jc_bill_warranty_parts_accessories = 0.00,"
							+ " jc_bill_warranty_parts_valueadd = 0.00,"
							+ " jc_bill_warranty_parts_extwarranty_qty = 0.00,"
							+ " jc_bill_warranty_parts_extwarranty = 0.00,"
							+ " jc_bill_warranty_parts_wheelalign = 0.00,"
							+ " jc_bill_warranty_parts_cng = 0.00,"
							+ " jc_bill_warranty_labour_tyre_qty = 0.00,"
							+ " jc_bill_warranty_labour_tyre = 0.00,"
							+ " jc_bill_warranty_labour_oil = 0.00,"
							+ " jc_bill_warranty_labour_battery_qty = 0.00,"
							+ " jc_bill_warranty_labour_battery = 0.00,"
							+ " jc_bill_warranty_labour_brake_qty = 0.00,"
							+ " jc_bill_warranty_labour_brake = 0.00,"
							+ " jc_bill_warranty_labour_accessories = 0.00,"
							+ " jc_bill_warranty_labour_valueadd = 0.00,"
							+ " jc_bill_warranty_labour_extwarranty_qty = 0.00,"
							+ " jc_bill_warranty_labour_extwarranty = 0.00,"
							+ " jc_bill_warranty_labour_wheelalign = 0.00,"
							+ " jc_bill_insur_labour_cng = 0.00"
							+ " WHERE jc_id = " + jc_id;
					SOP("StrSql==StrSqlupd==" + StrSql);
					stmttx.execute(StrSql);
				}

			}
			conntx.commit();
			UpdateJCDetails();
			conntx.commit();

			// update the Inventory Current Stock by calling Connect class
			// function
			if (msg.equals("")) {
				// UpdateStock(comp_id, "0", "0");
			}
			// CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", jc_location_id, comp_id, "0", "yes");
			// Thread thread = new Thread(calccurrentstockthread);
			// thread.start();
			ListJCItems();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void UpdateJCDetails() throws SQLException {
		double labouramount = 0.00;
		double partsamount = 0.00;
		StrSql = "SELECT branch_brand_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_branch_id =branch_id"
				+ " WHERE jc_id=" + jc_id;
		SOP("StrSql=brand=" + StrSql);
		brand_id = CNumeric(ExecuteQuery(StrSql));

		try {
			StrSql = "SELECT billcat_billtype, SUM(jctrans_qty) AS quantity,"
					+ " item_type_id, jctrans_billtype_id, "
					+ " SUM(CASE WHEN jctrans_rowcount != 0 AND jctrans_option_id = 0 THEN jctrans_netprice END) AS totalamount, "
					+ " SUM(CASE WHEN jctrans_discount = 1 THEN jctrans_amount END) AS discount"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ "	INNER JOIN " + compdb(comp_id) + " axela_service_jc_trans ON jctrans_jc_id = jc_id"
					+ "	INNER JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = jctrans_item_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item_bill_cat ON billcat_id = item_billcat_id"
					+ "	AND billcat_brand_id = " + brand_id
					+ "	WHERE jctrans_rowcount != 0"
					+ "	AND jctrans_item_id != 0"
					// + " AND jctrans_billtype_id = " + jctrans_billtype_id
					+ "	AND jctrans_jc_id = " + jc_id
					+ "	GROUP BY  billcat_billtype, jctrans_billtype_id";
			SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				labouramount = 0.0;
				partsamount = 0.0;
				StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET ";
				while (crs.next()) {
					billcat_billtype = crs.getString("billcat_billtype");
					quantity = Double.parseDouble(crs.getString("quantity"));
					totalamount = Double.parseDouble(crs.getString("totalamount")) - Double.parseDouble(crs.getString("discount"));
					jctrans_billtype_id = crs.getString("jctrans_billtype_id");
					item_type_id = crs.getString("item_type_id");
					if (jctrans_billtype_id.equals("1") || jctrans_billtype_id.equals("4") || jctrans_billtype_id.equals("5")) {// cash//foc//internal
						if (item_type_id.equals("3")) {
							labouramount += totalamount;
							totalamount = labouramount;
						} else if (item_type_id.equals("4")) {
							partsamount += totalamount;
							totalamount = partsamount;
						}
					}
					if (!billcat_billtype.equals("") && totalamount != 0.0) {
						if (jctrans_billtype_id.equals("1") || jctrans_billtype_id.equals("4") || jctrans_billtype_id.equals("5")) {// cash//foc//internal
							if (item_type_id.equals("3")) {// parts
								if (billcat_billtype.equals("Parts")) {
									StrSql += " jc_bill_cash_parts = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_cash_parts_tyre = " + totalamount + ","
											+ " jc_bill_cash_parts_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_cash_parts_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_cash_parts_battery = " + totalamount + ","
											+ " jc_bill_cash_parts_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_cash_parts_brake = " + totalamount + ","
											+ " jc_bill_cash_parts_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_cash_parts_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_cash_parts_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_cash_parts_extwarranty = " + totalamount + ","
											+ " jc_bill_cash_parts_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_cash_parts_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_cash_parts_cng = " + totalamount + ",";
								}
							} else if (item_type_id.equals("4")) {
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_cash_labour_tyre = " + totalamount + ","
											+ " jc_bill_cash_labour_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_cash_labour_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_cash_labour_battery = " + totalamount + ","
											+ " jc_bill_cash_labour_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_cash_labour_brake = " + totalamount + ","
											+ " jc_bill_cash_labour_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_cash_labour_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_cash_labour_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_cash_labour_extwarranty = " + totalamount + ","
											+ " jc_bill_cash_labour_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_cash_labour_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_cash_labour_cng = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Labour")) {
									StrSql += " jc_bill_cash_labour = " + totalamount + ",";
								}
							}
						}
						if (jctrans_billtype_id.equals("2")) {// insurance
							if (item_type_id.equals("3")) {// parts
								if (billcat_billtype.equals("Parts")) {
									StrSql += " jc_bill_insur_parts = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_insur_parts_tyre = " + totalamount + ","
											+ " jc_bill_insur_parts_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_insur_parts_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_insur_parts_battery = " + totalamount + ","
											+ " jc_bill_insur_parts_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_insur_parts_brake = " + totalamount + ","
											+ " jc_bill_insur_parts_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_insur_parts_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_insur_parts_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_insur_parts_extwarranty = " + totalamount + ","
											+ " jc_bill_insur_parts_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_insur_parts_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_insur_parts_cng = " + totalamount + ",";
								}
							} else if (item_type_id.equals("4")) {
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_insur_labour_tyre = " + totalamount + ","
											+ " jc_bill_insur_labour_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_insur_labour_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_insur_labour_battery = " + totalamount + ","
											+ " jc_bill_insur_labour_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_insur_labour_brake = " + totalamount + ","
											+ " jc_bill_insur_labour_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_insur_labour_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_insur_labour_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_insur_labour_extwarranty = " + totalamount + ","
											+ " jc_bill_insur_labour_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_insur_labour_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_insur_labour_cng = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Labour")) {
									StrSql += " jc_bill_insur_labour = " + totalamount + ",";
								}
							}
						}
						if (jctrans_billtype_id.equals("3")) {// warranty
							if (item_type_id.equals("3")) {// parts
								if (billcat_billtype.equals("Parts")) {
									StrSql += " jc_bill_warranty_parts = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_warranty_parts_tyre = " + totalamount + ","
											+ " jc_bill_warranty_parts_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_warranty_parts_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_warranty_parts_battery = " + totalamount + ","
											+ " jc_bill_warranty_parts_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_warranty_parts_brake = " + totalamount + ","
											+ " jc_bill_warranty_parts_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_warranty_parts_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_warranty_parts_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_warranty_parts_extwarranty = " + totalamount + ","
											+ " jc_bill_warranty_parts_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_warranty_parts_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_warranty_parts_cng = " + totalamount + ",";
								}
							} else if (item_type_id.equals("4")) {
								if (billcat_billtype.equals("Tyre")) {
									StrSql += " jc_bill_warranty_labour_tyre = " + totalamount + ","
											+ " jc_bill_warranty_labour_tyre_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Oil")) {
									StrSql += " jc_bill_warranty_labour_oil = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Battery")) {
									StrSql += " jc_bill_warranty_labour_battery = " + totalamount + ","
											+ " jc_bill_warranty_labour_battery_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Break")) {
									StrSql += " jc_bill_warranty_labour_brake = " + totalamount + ","
											+ " jc_bill_warranty_labour_brake_qty = " + quantity + ",";
								}
								if (billcat_billtype.equals("Accessories")) {
									StrSql += " jc_bill_warranty_labour_accessories = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Valueadd")) {
									StrSql += " jc_bill_warranty_labour_valueadd = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Extwarranty")) {
									StrSql += " jc_bill_warranty_labour_extwarranty = " + totalamount + ","
											+ " jc_bill_warranty_labour_extwarranty_qty= " + quantity + ",";
								}
								if (billcat_billtype.equals("Wheelalign")) {
									StrSql += " jc_bill_warranty_labour_wheelalign = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Cng")) {
									StrSql += " jc_bill_warranty_labour_cng = " + totalamount + ",";
								}
								if (billcat_billtype.equals("Labour")) {
									StrSql += " jc_bill_warranty_labour = " + totalamount + ",";
								}
							}
						}
					}
				}
				StrSql += " jc_modified_id = " + emp_id + ","
						+ " jc_modified_date = '" + ToLongDate(kknow()) + "'"
						+ " WHERE jc_id = " + jc_id;
				SOP("StrSql=parts=" + StrSql);
				stmttx.addBatch(StrSql);
				stmttx.executeBatch();
				conntx.commit();
			}
			StrSql = "SELECT "
					+ " @totalamt := SUM(CASE WHEN jctrans_rowcount != 0 AND jctrans_option_id = 0 THEN jctrans_amount END) AS amount, "
					+ " @totaldis := SUM(CASE WHEN jctrans_discount = 1 THEN jctrans_amount END) AS discount, "
					+ " @totaltax := SUM(CASE WHEN jctrans_tax = 1 THEN jctrans_amount END) AS tax, "
					+ " @total := (@totalamt - @totaldis) + @totaltax AS netamount "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans "
					+ " WHERE jctrans_jc_id = " + jc_id;
			SOP("StrSql=jcgrand=" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {

				jc_discamt = df.format(crs.getDouble("discount"));
				if (!jc_discamt.equals("0") || !jc_discamt.equals("0.00")) {
					jc_netamt = df.format(crs.getDouble("amount") - crs.getDouble("discount"));
				} else {
					jc_netamt = df.format(crs.getDouble("amount"));
				}
				jc_totaltax = df.format(crs.getDouble("tax"));
				jc_grandtotal = df.format((crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax"));
			}
			crs.close();
			StrSql = "Update " + compdb(comp_id) + "axela_service_jc"
					+ " SET"
					+ " jc_netamt = " + jc_netamt + ","
					+ " jc_discamt = " + jc_discamt + ","
					+ " jc_totaltax = " + jc_totaltax + ","
					+ " jc_grandtotal = " + jc_grandtotal + ""
					+ " WHERE jc_id = " + jc_id + "";
			updateQuery(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	public void ListJCItems2() {
		StringBuilder Str = new StringBuilder();
		Double jcitem_grandtotal = 0.00, total = 0.00;
		Double jcitem_discamt = 0.00, pricetax = 0.00;
		Double jcitem_totaltax = 0.00, selling_price = 0.00;
		Double item_qty = 0.00, group_total = 0.00;
		Double jcitem_netamt = 0.00;
		int count = 0;
		StrSql = "SELECT crt.jcitem_id, crt.jcitem_item_id, item_name, item_type_id, item_ticket_dept_id,"
				+ " crt.jcitem_disc, crt.jcitem_tax_id, item_code, item_id, crt.jcitem_tax_rate,"
				+ " COALESCE(crt.jcitem_option_group, '') AS jcitem_option_group, type_name, item_nonstock,"
				+ " COALESCE(crt.jcitem_tax, 0) AS jcitem_tax, COALESCE(customer_name, '') AS tax_name,"
				+ " COALESCE(price_variable, 1) AS price_variable, crt.jcitem_price, crt.jcitem_qty,"
				+ " IF(item_nonstock = 0, COALESCE(stock_current_qty, 0), '_') AS stock_current_qty,"
				+ " COALESCE(price_amt, 0) AS price_amt, crt.jcitem_item_serial,"
				+ " COALESCE((SELECT COUNT(DISTINCT jcitem_id)"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_item cart"
				+ " WHERE cart.jcitem_option_id = crt.jcitem_rowcount"
				+ " GROUP BY cart.jcitem_option_id), 0) AS opitemcount"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_item crt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = crt.jcitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat ON salescat_id = item_cat_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = crt.jcitem_tax_id AND customer_tax = 1"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
				+ " WHERE jcitem_jc_id = " + jc_id + ""
				+ " AND crt.jcitem_rowcount != 0"
				+ " AND item_type_id != 1"
				+ " GROUP BY jcitem_id"
				+ " ORDER BY type_id";
		// SOP("StrSql ListJCItems()====" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		Str.append("<div class=\"table-responsive\">\n");
		Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
		Str.append("<thead><tr>\n");

		try {
			if (!msg.equals("")) {
				Str.append("<tr>\n<td colspan=\"9\" align=\"center\"><font color=\"red\"><b>").append(msg).append("</b></font></td>\n</tr>\n");
			}
			Str.append("<tr>\n<th>#</th>\n");
			Str.append("<th>X</th>\n");
			Str.append("<th>Item</th>\n");
			Str.append("<th>Qty</th>\n");
			Str.append("<th>Price</th>\n");
			Str.append("<th>MRP</th>\n");
			Str.append("<th>Discount</th>\n");
			Str.append("<th>Selling Price</th>\n");
			Str.append("<th>Amount</th>\n</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("stock_current_qty").equals("_")) {
						stock_current_qty = Double.toString(crs.getDouble("jcitem_qty") + crs.getDouble("stock_current_qty"));
					} else {
						stock_current_qty = crs.getString("stock_current_qty");
					}
					jcitem_netamt += (crs.getDouble("jcitem_qty") * ((Double.parseDouble(crs.getString("jcitem_price"))) - Double.parseDouble(crs.getString("jcitem_disc"))));
					total = Double.parseDouble(crs.getString("jcitem_qty"))
							* (Double.parseDouble(crs.getString("jcitem_price")) - Double.parseDouble(crs.getString("jcitem_disc")) + Double.parseDouble(crs.getString("jcitem_tax")));
					pricetax = (crs.getDouble("jcitem_price") + ((crs.getDouble("jcitem_price")) * (crs.getDouble("jcitem_tax_rate")) / 100));
					selling_price = (Double.parseDouble(crs.getString("jcitem_price")) - Double.parseDouble(crs.getString("jcitem_disc")) + Double.parseDouble(crs.getString("jcitem_tax")));
					jcitem_grandtotal += total;
					jcitem_discamt += (Double.parseDouble(crs.getString("jcitem_qty")) * Double.parseDouble(crs.getString("jcitem_disc")));

					if (crs.getString("item_nonstock").equals("0")) {
						item_qty += Double.parseDouble(crs.getString("jcitem_qty"));
					}
					jcitem_totaltax = jcitem_totaltax + (Double.parseDouble(crs.getString("jcitem_tax")) * Double.parseDouble(crs.getString("jcitem_qty")));
					String item_name = crs.getString("item_name");

					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ")";
					}

					if (!crs.getString("type_name").equals(type_name)) {
						type_name = crs.getString("type_name");
						if (group_total != 0.00) {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" colspan=\"8\"><b>Total:</b></td>\n");
							Str.append("<td align=\"right\">").append(df.format(group_total)).append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("<tr>\n");
						Str.append("<td align=\"center\" colspan=\"9\"><b>").append(type_name).append("</b></td>\n");
						Str.append("</tr>\n");
						group_total = 0.00;
						count = 0;
					}
					count++;
					group_total += total;
					Str.append("\n<tr valign=\"top\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append(crs.getString("item_ticket_dept_id")).append(",");
					Str.append("'").append(item_name.replace("'", "single_quote").replace("&#39;", "single_quote")).append("',");
					Str.append(crs.getDouble("jcitem_qty")).append(",");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("jcitem_price")).append(",");
					Str.append(crs.getString("price_variable")).append(",");
					Str.append(crs.getDouble("jcitem_disc")).append(",");
					Str.append(crs.getString("jcitem_tax_id")).append(",");
					Str.append(crs.getString("jcitem_tax_rate")).append(",");
					Str.append("'").append(crs.getString("tax_name")).append("',");
					Str.append("'',");
					Str.append("'").append(crs.getString("jcitem_item_serial")).append("',");
					Str.append(crs.getString("jcitem_id")).append(",'");
					Str.append("").append("',");
					Str.append("'update');\">\n");
					Str.append("<td align=\"center\">");
					Str.append(count);
					Str.append("</td>\n<td align=\"center\">");
					Str.append("<a href=\"javascript:delete_jcitem(").append(crs.getString("jcitem_id")).append(",");
					Str.append(crs.getString("item_id")).append(");\">X</a>");
					Str.append("</td>\n<td align=\"left\">");
					Str.append(item_name);
					Str.append("</td>\n<td align=\"right\">");
					Str.append(crs.getDouble("jcitem_qty"));
					Str.append("</td>\n<td align=\"right\">");
					Str.append(df.format(crs.getDouble("jcitem_price")));
					Str.append("</td>\n<td align=\"right\">");
					Str.append(df.format(pricetax));
					Str.append("</td>\n<td align=\"right\">");
					Str.append(crs.getDouble("jcitem_disc"));
					Str.append("</td>\n<td align=\"right\">");
					Str.append(df.format(selling_price));
					Str.append("</td>\n<td align=\"right\">");
					Str.append(df.format(total));
					Str.append("</td>\n</tr>\n");
				}
			}
			crs.close();
			if (group_total != 0.00) {
				Str.append("<tr>\n");
				Str.append("<td align=\"right\" colspan=\"8\"><b>Total:</b></td>\n");
				Str.append("<td align=\"right\">").append(df.format(group_total)).append("</td>\n");
				Str.append("</tr>\n");
			}
			Str.append("<tr><td align=\"right\" colspan=\"8\"><b>Grand Total:</b></td>\n");
			Str.append("<td align=\"right\"><b>").append(df.format(Math.ceil(jcitem_grandtotal))).append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("<tr><td align=\"right\" colspan=\"8\">Total Savings:</td>\n");
			Str.append("<td align=\"right\">").append(df.format(jcitem_discamt)).append("</td>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n<td align=\"right\" colspan=\"8\">Net Quantity:</td>\n");
			Str.append("<td align=\"right\">").append(df.format(item_qty)).append("</td>\n");
			Str.append("</tr>\n");

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		Str.append("</tbody>\n");
		Str.append("</table>\n");
		Str.append("</div>\n");
		StrHTML = Str.toString();
	}

	public void ListJCItems() {
		StringBuilder Str = new StringBuilder();
		double invoice_grandtotal = 0.00, invoice_total = 0.00, total = 0.00, invoice_optnetamt = 0.00, cart_alt_qty = 0.00, uom_ratio = 0.00;
		double item_price = 0.00, item_unit_price = 0.00, jc_grandtotal = 0.00, jc_totaltax = 0.00, jctotaldiscount = 0.00;
		double discount = 0.00;
		double discpercent = 0.00;
		double quantity = 0.00;
		double pricetax = 0.00;
		double invoice_qty = 0.00;
		double mainitemamt = 0.00;
		double total_discount = 0.00;
		double total_tax = 0.00;
		double total_truckspace = 0.00;
		int count = 0;
		String voucher_id = "0";
		String itemtype_name = "", billtype_name = "", nextbilltype_name = "", nextitemtype_name = "";
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT billtype_id, billtype_name,type_name, jctrans_id, item_id, jctrans_rateclass_id, item_type_id, item_ticket_dept_id,"
					+ " COALESCE (jctrans_discount, 0) AS jctrans_discount,"
					+ " COALESCE (price_variable, 1) AS price_variable,"
					+ " COALESCE (price_amt, 0) AS price_amt,";
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

				// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id3,"
				// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate3,";
				//
				// StrSql += " COALESCE(tax4.customer_name, '') AS tax3_name,"
				// + " COALESCE(tax4.customer_id, 0) AS tax_id3,";
			}
			else if (gst_type.equals("central")) {
				StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax_customer_id1,"
						+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax_rate1,"
						+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
						+ " COALESCE(tax3.customer_id, 0) AS tax_id1,";
				// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id2,"
				// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate2,";
				//
				// StrSql += " COALESCE(tax4.customer_name, '') AS tax2_name,"
				// + " COALESCE(tax4.customer_id, 0) AS tax_id2,";
				// StrSql += " '0' AS tax_customer_id3,"
				// + " '0' AS tax_rate3,";
				//
				StrSql += " '' AS tax2_name,"
						+ " '0' AS tax_rate2,"
						+ " '0' AS tax_id2,";

			}
			StrSql += " cart.jctrans_id, cart.jctrans_rowcount, cart.jctrans_jc_id AS jctrans_jc_id, item_id, uom_name, item_name, item_code,"
					+ " cart.jctrans_qty, cart.jctrans_alt_qty, cart.jctrans_alt_uom_id, cart.jctrans_truckspace, cart.jctrans_price,"
					+ " cart.jctrans_netprice, cart.jctrans_amount,"
					+ " COALESCE ( ( SELECT sum(disc.jctrans_amount) "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans disc"
					+ " WHERE disc.jctrans_option_id = cart.jctrans_rowcount"
					+ " AND disc.jctrans_discount = 1"
					+ "	AND disc.jctrans_billtype_id = cart.jctrans_billtype_id"
					+ " AND disc.jctrans_jc_id = cart.jctrans_jc_id"
					+ " GROUP BY disc.jctrans_rowcount ), 0 ) AS discount,"
					+ " COALESCE ( ( SELECT discount_perc.jctrans_discount_perc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans discount_perc"
					+ " WHERE discount_perc.jctrans_option_id = cart.jctrans_rowcount"
					+ " AND discount_perc.jctrans_discount = 1"
					+ "	AND discount_perc.jctrans_billtype_id = cart.jctrans_billtype_id"
					+ " AND discount_perc.jctrans_jc_id = cart.jctrans_jc_id"
					+ " GROUP BY discount_perc.jctrans_rowcount ), 0 ) AS discount_perc,"
					+ " COALESCE ( ( SELECT sum(tax.jctrans_amount)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans tax"
					+ " INNER JOIN " + compdb(comp_id) + " axela_customer ON customer_id = tax.jctrans_customer_id"
					+ " AND customer_taxtype_id IN (3, 4)"// sgstand cgst
					+ " WHERE tax.jctrans_option_id = cart.jctrans_rowcount"
					+ " AND tax.jctrans_tax = 1"
					+ "	AND tax.jctrans_billtype_id = cart.jctrans_billtype_id"
					+ " AND tax.jctrans_jc_id = cart.jctrans_jc_id"
					+ " GROUP BY tax.jctrans_rowcount ), 0 ) AS tax"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans cart"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = cart.jctrans_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_bill_type ON billtype_id = cart.jctrans_billtype_id";
			if (gst_type.equals("state")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id =item_salestax1_ledger_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id =item_salestax2_ledger_id";
			} else if (gst_type.equals("central")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id =item_salestax3_ledger_id";
			}
			// " LEFT JOIN " + compdb(comp_id) + "axela_customer tax4 on tax4.customer_id =item_salestax4_ledger_id"
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = cart.jctrans_alt_uom_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " WHERE 1 = 1"
					+ " AND cart.jctrans_discount = 0"
					+ " AND cart.jctrans_tax = 0"
					+ " AND cart.jctrans_item_id != 0"
					+ " AND cart.jctrans_option_id = 0"
					+ " AND cart.jctrans_rowcount != 0"
					+ " AND cart.jctrans_jc_id = " + jc_id
					+ " GROUP BY cart.jctrans_id"
					+ " ORDER BY cart.jctrans_billtype_id, item_type_id, cart.jctrans_rowcount";

			SOP("StrSql=List cart items==" + StrSqlBreaker(StrSql));

			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				jc_grandtotal = 0.00;
				jc_totaltax = 0.00;
				jctotaldiscount = 0.00;
				while (crs.next()) {
					if (!(billtype_name.equals(crs.getString("billtype_name")) && itemtype_name.equals(crs.getString("type_name")))) {
						invoice_grandtotal = 0.00;
						invoice_total = 0.00;
						total = 0.00;
						invoice_optnetamt = 0.00;
						cart_alt_qty = 0.00;
						uom_ratio = 0.00;
						item_price = 0.00;
						item_unit_price = 0.00;
						discount = 0.00;
						discpercent = 0.00;
						quantity = 0.00;
						pricetax = 0.00;
						invoice_qty = 0.00;
						mainitemamt = 0.00;
						total_discount = 0.00;
						total_tax = 0.00;
						total_truckspace = 0.00;
						count = 0;
						voucher_id = "0";
						billtype_name = crs.getString("billtype_name");
						itemtype_name = crs.getString("type_name");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						// Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"black\" class=\"listtable footable\">\n");

						if (!msg.equals("")) {
							Str.append("<thead>\n<tr>\n<td colspan=\"10\" align=\"center\"><font color=\"red\"><b>").append(msg).append("</b></font></td>\n</tr>\n</thead>\n");
						}
						Str.append("<thead>\n<tr>\n");
						Str.append("<th align=\"center\" colspan=\"9\">" + billtype_name + " - " + itemtype_name + "</th>\n");
						Str.append("</tr>\n</thead>\n");
						Str.append("<thead>\n<tr>\n");
						Str.append("<th>#</th>\n");
						// if (cart_voucher_so_id.equals("0")) {
						Str.append("<th>X</th>\n");
						// }
						Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item</th>\n");
						Str.append("<th data-hide=\"phone\">Qty</th>\n");
						// Str.append("<th data-hide=\"phone\">Truck Space</th>\n");
						Str.append("<th data-hide=\"phone\">UOM</th>\n");
						Str.append("<th data-hide=\"phone\">Price</th>\n");
						Str.append("<th data-hide=\"phone\">Discount</th>\n");
						Str.append("<th data-hide=\"phone\">Tax</th>\n");
						// Str.append("<th data-hide=\"phone\"></th>\n");
						Str.append("<th>Amount</th>\n");
						Str.append("</tr>\n</thead>\n");

					}
					// boxtype_size = crs.getString("boxtype_size");
					// voucher_id = crs.getString("cart_voucher_id");
					item_price = crs.getDouble("jctrans_price");
					cart_alt_qty = crs.getDouble("jctrans_alt_qty");
					quantity = crs.getDouble("jctrans_qty");
					uom_ratio = (quantity / cart_alt_qty);
					if (quantity != 0.00) {
						discount = crs.getDouble("discount") / quantity;
						jctotaldiscount += discount;
					} else {
						discount = 0.00;
					}
					discpercent = crs.getDouble("discount_perc");
					// discpercent = ((discount * 100) / (item_price +
					// optiontotal));
					mainitemamt = (item_price) * quantity;
					invoice_qty += crs.getDouble("jctrans_alt_qty");
					total_discount += crs.getDouble("discount");

					total = (((item_price - discount) * quantity) + crs.getDouble("tax"));
					if (CNumeric(CheckNull(total + "")).equals(0)) {
						total = 0.0;
					}
					invoice_total += total;
					SOP("invoice_total===" + invoice_total);
					total_tax += crs.getDouble("tax");
					jc_grandtotal += total;
					jc_totaltax += crs.getDouble("tax");
					total_truckspace += crs.getDouble("jctrans_truckspace");
					String item_name = "";
					if (!crs.getString("item_name").equals("")) {
						item_name += crs.getString("item_name");
					}
					if (!crs.getString("item_code").equals("")) {
						item_name += "(" + crs.getString("item_code") + ") ";
					}
					if (!crs.getString("jctrans_rowcount").equals("0")) {
						++count;
					}
					Str.append("\n<tr valign=\"top\"");
					// Str.append("'update');\"");
					// SOP("id===" + crs.getString("jctrans_jc_id"));
					Str.append(">\n<td width=\"5%\" align=\"center\">\n");
					Str.append(count);
					Str.append("</td>\n");
					Str.append("<td align=\"center\">\n");
					Str.append("<a href=\"javascript:delete_jcitem(").append(crs.getString("jctrans_jc_id")).append(",");
					Str.append(crs.getString("billtype_id")).append(",");
					Str.append(crs.getString("item_id")).append(");\">X</a>");
					Str.append("</td>\n");
					Str.append("<td id='" + count + "' align=\"left\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getString("jctrans_rateclass_id")).append(",");
					Str.append(crs.getString("billtype_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append(crs.getString("item_ticket_dept_id")).append(",");
					Str.append("'").append(item_name.replace("'", "single_quote").replace("&#39;", "single_quote")).append("',");
					Str.append(crs.getDouble("jctrans_qty")).append(",");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("jctrans_price")).append(",");
					Str.append(crs.getString("price_variable")).append(",");

					Str.append(crs.getDouble("discount")).append(",");
					Str.append(crs.getString("tax_id1")).append(",");
					Str.append(crs.getString("tax_id2")).append(",");
					// Str.append(crs.getString("tax_id3")).append(",");
					Str.append(crs.getString("tax_rate1")).append(",");
					Str.append(crs.getString("tax_rate2")).append(",");
					// Str.append(crs.getString("tax_rate3")).append(",");
					Str.append("'").append(crs.getString("tax1_name")).append("',");
					Str.append("'").append(crs.getString("tax2_name")).append("',");
					// Str.append("'").append(crs.getString("tax3_name")).append("',");
					Str.append("'',");
					Str.append("'").append("").append("',");
					Str.append(crs.getString("jctrans_id")).append(",'");
					Str.append(10).append("',");
					Str.append("'update');\">\n");
					Str.append(item_name);
					Str.append("</td>\n");
					Str.append("<td align=\"center\">\n");
					Str.append(crs.getDouble("jctrans_alt_qty"));
					Str.append("</td>");

					// Str.append("<td align=\"right\">\n");
					// Str.append(crs.getString("jctrans_truckspace"));
					// Str.append("</td>");

					Str.append("<td align=\"left\">\n");
					Str.append(crs.getString("uom_name"));
					Str.append("</td>");

					Str.append("\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("jctrans_price")));
					Str.append("</td>\n<td align=\"right\">\n");
					// if (!cart_voucher_so_id.equals("0")) {
					Str.append(df.format(crs.getDouble("discount")));
					// } else {
					// Str.append(df.format(crs.getDouble("discount_perc")));
					// }

					Str.append("</td>\n");
					Str.append("<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("tax")));
					Str.append("</td>\n");
					Str.append("<td align=\"right\">\n");
					if (crs.getDouble("jctrans_price") != 0.0) {
						Str.append(df.format(total));
					} else {
						Str.append("0.00");
					}
					Str.append("</td>\n</tr>\n");

					updatecount += count;
					if (!crs.isLast() && crs.next()) {
						nextbilltype_name = crs.getString("billtype_name");
						nextitemtype_name = crs.getString("type_name");
						crs.previous();
					}

					// SOP("prevbilltype_name==" + billtype_name);
					// SOP("nextbilltype_name==" + nextbilltype_name);
					if (!(billtype_name.equals(nextbilltype_name) && itemtype_name.equals(nextitemtype_name)) || crs.isLast()) {
						Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
						Str.append("<td align=\"right\">&nbsp;</td>\n");
						Str.append("<td valign=\"top\" align=\"right\"><b>Total:</b></td>\n");
						Str.append("<td valign=\"top\" align=\"right\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"");
						Str.append(df.format(invoice_qty)).append("\"><b>").append(df.format(invoice_qty)).append("</b></td>\n");
						Str.append("<td align=\"right\">&nbsp;</td>\n");
						Str.append("<td align=\"right\">&nbsp;</td>\n");
						Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_discount)).append("</b></td>\n");
						Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(total_tax)).append("</b></td>\n");
						Str.append("<td valign=\"top\" align=\"right\">\n");
						Str.append("<input type=\"hidden\" name=\"txt_updatecount\" id=\"txt_updatecount\" value=\"");
						Str.append(updatecount).append("\">");
						Str.append("<input type=\"hidden\" name=\"txt_invoice_grandtotal\" id=\"txt_invoice_grandtotal\" value=\"");
						Str.append(df.format(invoice_total)).append("\">\n<b>");
						Str.append(df.format(invoice_total)).append("</b></td>\n");
						Str.append("</tr>\n");
						Str.append("</table>\n");
					}
				}
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>Grand Total:</b></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(jc_grandtotal)).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>Total Tax:</b></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(jc_totaltax)).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>Total Discount:</b></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>").append(df.format(jctotaldiscount)).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		StrHTML = Str.toString();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doGet(request, response);
	}
}
