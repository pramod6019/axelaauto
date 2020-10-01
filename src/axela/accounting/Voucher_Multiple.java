package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher_Multiple extends Connect {

	public String StrHTML = "";
	public String StrHTML1 = "";
	public String search = "";
	public String StrSearch = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String cart_session_id = "0";
	public String RecCountDisplay = "";

	public String vouchertype_id = "0";
	public String vouchertype_name = "";
	public String voucher_id = "0", voucher_customer_id = "0";
	public String voucher_vouchertype_id = "0";
	public String multiple_deliverynote = "";
	public String multiple_grn = "";
	public int voucher_count = 0;
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	DecimalFormat dc = new DecimalFormat("#.##");
	Invoice_Details invdetails = new Invoice_Details();

	public static int temp_vouchertrans_rowcount = 0;
	public static int temp_vouchertrans_option_id = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				if (!GetSession("emp_id", request).equals("")) {
					emp_id = CNumeric(GetSession("emp_id", request) + "");
					add = PadQuotes(request.getParameter("add"));
					addB = PadQuotes(request.getParameter("add_button"));
					deleteB = PadQuotes(request.getParameter("delete_button"));
					update = PadQuotes(request.getParameter("update"));
					cart_session_id = CNumeric(PadQuotes(request
							.getParameter("cart_session_id")));
					multiple_deliverynote = PadQuotes(request
							.getParameter("multipledeliverynote"));
					multiple_grn = PadQuotes(request.getParameter("multiplegrn"));
					voucher_id = CNumeric(PadQuotes(request
							.getParameter("voucher_id")));
					voucher_vouchertype_id = CNumeric(PadQuotes(request
							.getParameter("cart_vouchertype_id")));
					voucher_customer_id = CNumeric(PadQuotes(request
							.getParameter("voucher_customer_id")));
					if (add.equals("yes")) {
						status = "Add";
					}

					if (multiple_deliverynote.equals("yes")) {
						GetValues(request, response);
						StrSql = "SELECT vouchertype_name FROM  " + compdb(comp_id) + "axela_acc_voucher_type WHERE vouchertype_id=3";
						vouchertype_name = ExecuteQuery(StrSql);
						StrHTML = Listdata(request);
					}
					if (multiple_grn.equals("yes")) {
						GetValues(request, response);
						StrSql = "SELECT vouchertype_name FROM  " + compdb(comp_id) + "axela_acc_voucher_type WHERE vouchertype_id=1";
						vouchertype_name = ExecuteQuery(StrSql);
						StrHTML = Listdata(request);
					}

					String checked = "";
					if (addB.equals("yes")) {
						StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1 "
								+ " AND cart_session_id = " + cart_session_id
								+ " AND cart_emp_id = " + emp_id
								+ " AND cart_vouchertype_id ="
								+ voucher_vouchertype_id;
						// SOP("StrSql==DeleteCartAllItems==" + StrSql);
						updateQuery(StrSql);

						GetValues(request, response);
						// SOP("voucher_count===" + voucher_count);
						for (int i = 1; i <= voucher_count; i++) {
							String multivoucher_id =
									CNumeric(PadQuotes(request
											.getParameter("txt_voucher_id_" + i)));
							checked = PadQuotes(request.getParameter("chk_" + i
									+ ""));

							if (checked.equals("on")) {
								CopyVoucherTransToCartCheckMultiple(request, emp_id,
										voucher_id,
										multivoucher_id, voucher_vouchertype_id);
							}
						}

						StrHTML1 = invdetails.ListCartItems(emp_id,
								cart_session_id, voucher_vouchertype_id);
					}

				} else {
					StrHTML = "SignIn";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		voucher_count =
				Integer.parseInt(CNumeric(PadQuotes(request
						.getParameter("txt_count"))));
	}

	public String Listdata(HttpServletRequest request) {
		int count = 0;

		StringBuilder Str = new StringBuilder();
		CachedRowSet rs1;
		try {
			StrSql = "SELECT voucher_id, voucher_date,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " voucher_ref_no,"
					+ " vouchertype_name,";
			// if (!voucher_id.equals("0")) {
			StrSql += " COALESCE((select"
					+ " if(vouchertrans_multivoucher_id > 0,1,0)"
					+ " from axela_acc_voucher_trans  "
					+ " where vouchertrans_multivoucher_id = voucher_id "
					+ " and vouchertrans_voucher_id = " + voucher_id + " LIMIT 1 ),0) as checked,";
			// }
			StrSql += " SUM(unbilled.vouchertrans_qty) AS unbilledqty,"
					+ " (SELECT COALESCE(SUM(vouchertrans_qty),0)"
					+ " FROM axela_acc_voucher_trans "
					+ " WHERE vouchertrans_multivoucher_id = voucher_id";
			if (!voucher_id.equals("0")) {
				StrSql += " AND vouchertrans_voucher_id != " + voucher_id;
			}
			StrSql += " ) AS billedqty"
					+ " FROM axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans unbilled ON unbilled.vouchertrans_voucher_id = voucher_id"
					+ " WHERE 1=1";
			if (multiple_grn.equals("yes")) {
				StrSql += " AND vouchertype_id = 1";
			} else if (multiple_deliverynote.equals("yes")) {
				StrSql += " AND vouchertype_id = 3 ";
			}

			StrSql += " AND voucher_customer_id = " + voucher_customer_id
					+ " AND branch_company_id = " + comp_id;
			StrSql += " GROUP BY voucher_id"
					+ " HAVING unbilledqty > billedqty"
					+ " ORDER BY voucher_date, voucher_id";

			// SOP("StrSql=main=multiple dn==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th></th>\n");
				Str.append("<th>" + crs.getString("vouchertype_name")
						+ " No.</th>\n");
				Str.append("<th>" + crs.getString("vouchertype_name")
						+ " Date</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("</tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					String checked = "0";

					count++;
					if (!voucher_id.equals("0")) {
						checked = crs.getString("checked");

					}

					if (voucher_count != 0) {
						checked = PadQuotes(request.getParameter("chk_" + count
								+ ""));
						if (checked.equals("on")) {
							checked = "1";
						} else {
							checked = "0";
						}
					}

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count)
							.append("</td>\n");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucher_id_"
							+ count + "\" name=\"txt_voucher_id_" + count
							+ "\" value=" + crs
									.getString("voucher_id") + ">");
					Str.append("<input type=\"checkbox\" name='chk_" + count
							+ "' id='chk_" + count + "' "
							+ PopulateCheck(checked) + " class='checkbox'/>");
					Str.append("</td>\n");
					Str.append("<td valign=top align=ceneter>").append(
							crs.getString("voucher_no")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(
							strToShortDate(crs.getString("voucher_date")))
							.append("</td>\n");
					Str.append("<td valign=top align=center>").append(
							crs.getString("voucher_ref_no")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
				Str.append("</table>\n");

			} else {
				RecCountDisplay =
						"<br><br><br><br><font color=red><b>No Record Found!</b></font><br><br>";
				return RecCountDisplay;
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {

			SOPError("Error in " + new Exception().getStackTrace()[0]
					.getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();

	}
	public void CopyVoucherTransToCartCheckMultiple(HttpServletRequest request,
			String emp_id, String voucher_id, String multivoucher_id,
			String voucher_vouchertype_id) throws SQLException {
		String msg = "";
		String StrSql = "";
		String vouchertrans_voucher_id = "0";
		String vouchertrans_multivoucher_id = "0";
		String vouchertrans_customer_id = "0";
		String vouchertrans_location_id = "0";
		String vouchertrans_item_id = "0";
		String vouchertrans_discount = "0.00", vouchertrans_discount_perc = "0.00";
		String vouchertrans_tax = "0.00";
		String vouchertrans_rowcount = "0";
		String vouchertrans_option_id = "0";
		String vouchertrans_option_group = "";
		String vouchertrans_item_batch_id = "0";
		String vouchertrans_item_serial = "", vouchertrans_tax_id = "0";
		double vouchertrans_qty = 0.00;
		double vouchertrans_truckspace = 0.00;
		double vouchertrans_price = 0.00;
		double vouchertrans_netprice = 0.00;
		double vouchertrans_unit_cost = 0.00;
		double vouchertrans_amount = 0.00;
		String vouchertrans_supplier_code = "";
		double vouchertrans_alt_qty = 0.00;
		String vouchertrans_alt_uom_id = "0";
		String vouchertrans_delivery_date = "";
		String vouchertrans_dc = "", vouchertrans_convfactor = "1";
		String uom_ratio = "0";
		String uom_parent_id = "0";
		double mul_ratio = 0.00;
		double cart_uom_ratio = 0.00;
		String ratio = "0.00";
		Connection conntx = connectDB();
		Statement stmttx = null;
		try {
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = "DELETE FROM axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id
					+ ""
					+ " AND cart_emp_id = " + emp_id + ""
					+ " AND cart_session_id = " + cart_session_id
					+ " AND SUBSTR(cart_time,1,8) < SUBSTR("
					+ ToShortDate(kknow()) + ",1,8)";
			stmttx.addBatch(StrSql);

			StrSql = "SELECT " + multivoucher_id + " AS vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_option_group,"
					+ " vouchertrans_item_batch_id,"
					+ " vouchertrans_item_serial,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_supplier_code,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " COALESCE(@uom_ratio := (SELECT uom_ratio FROM axela_inventory_uom"
					+ " WHERE uom_id = vouchertrans_alt_uom_id), 0) AS uom_ratio,"
					+ " COALESCE(@uom_parent_id := (SELECT uom_parent_id FROM axela_inventory_uom"
					+ " WHERE uom_id = vouchertrans_alt_uom_id), 0) AS uom_parent_id,"
					+ " vouchertrans_delivery_date,"
					+ " "
					+ ToLongDate(kknow())
					+ ","
					+ " vouchertrans_dc"
					+ " FROM "
					+ " axela_acc_voucher_trans"
					+ " INNER JOIN  " + compdb(comp_id) + ""
					+ " axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " WHERE 1=1 "
					+ " AND (vouchertrans_item_id !=0 || (vouchertrans_tax_id !=0 && vouchertrans_item_id = 0))"
					+ " AND vouchertrans_voucher_id = " + multivoucher_id + "";
			// SOP("StrSql==vouchertrans123==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				vouchertrans_multivoucher_id = crs1
						.getString("vouchertrans_multivoucher_id");
				vouchertrans_customer_id = crs1
						.getString("vouchertrans_customer_id");
				vouchertrans_location_id = crs1
						.getString("vouchertrans_location_id");
				vouchertrans_item_id = crs1.getString("vouchertrans_item_id");
				vouchertrans_discount = crs1.getString("vouchertrans_discount");
				vouchertrans_discount_perc = crs1
						.getString("vouchertrans_discount_perc");
				vouchertrans_tax = crs1.getString("vouchertrans_tax");
				vouchertrans_tax = crs1.getString("vouchertrans_tax");
				vouchertrans_tax_id = crs1.getString("vouchertrans_tax_id");
				vouchertrans_rowcount = crs1.getString("vouchertrans_rowcount");
				vouchertrans_option_id = crs1
						.getString("vouchertrans_option_id");
				vouchertrans_option_group = crs1
						.getString("vouchertrans_option_group");
				vouchertrans_item_batch_id = crs1
						.getString("vouchertrans_item_batch_id");
				vouchertrans_item_serial = crs1
						.getString("vouchertrans_item_serial");
				vouchertrans_qty = crs1.getDouble("vouchertrans_qty");
				vouchertrans_truckspace = crs1
						.getDouble("vouchertrans_truckspace");
				vouchertrans_price = crs1.getDouble("vouchertrans_price");
				vouchertrans_netprice = crs1.getDouble("vouchertrans_netprice");
				vouchertrans_unit_cost = crs1
						.getDouble("vouchertrans_unit_cost");
				vouchertrans_amount = crs1.getDouble("vouchertrans_amount");
				vouchertrans_supplier_code = crs1
						.getString("vouchertrans_supplier_code");
				vouchertrans_alt_qty = crs1.getDouble("vouchertrans_alt_qty");
				vouchertrans_alt_uom_id = crs1
						.getString("vouchertrans_alt_uom_id");
				uom_ratio = crs1.getString("uom_ratio");
				uom_parent_id = crs1.getString("uom_parent_id");
				vouchertrans_delivery_date = crs1
						.getString("vouchertrans_delivery_date");
				vouchertrans_dc = crs1.getString("vouchertrans_dc");
				if (!vouchertrans_rowcount.equals("0")
						&& vouchertrans_option_id.equals("0")) {
					if (!uom_ratio.equals("")) {
						cart_uom_ratio = Double.parseDouble(uom_ratio);
					}
					mul_ratio = 0.00;
					StrSql = "SELECT COALESCE(@ratio:=(IF (vouchertrans_discount = 0 && vouchertrans_tax = 0,"
							+ " (COALESCE (" + vouchertrans_alt_qty + ",0)"// From
																			// voucher
																			// qty
							+ " -";

					if (voucher_vouchertype_id.equals("102")) { // GIT Qty.
						SOP("voucher_vouchertype_id=git==" + voucher_vouchertype_id);
						StrSql += " (COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM axela_acc_voucher_trans"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = vouchertrans_voucher_id"
								+ " WHERE po.voucher_delnote_is  = "
								+ multivoucher_id
								+ ""
								+ " AND vouchertrans_item_id = "
								+ vouchertrans_item_id
								+ ""
								+ " AND vouchertrans_rowcount = "
								+ vouchertrans_rowcount
								+ ""
								+ " AND vouchertrans_option_id = "
								+ vouchertrans_option_id
								+ ""
								+ " AND voucher_vouchertype_id = "
								+ voucher_vouchertype_id
								+ ""
								+ " ),0) / ("
								+ cart_uom_ratio + "))";

					}
					else if (voucher_vouchertype_id.equals("115")) { // purchase invoice
						SOP("purchase invoice=git==" + voucher_vouchertype_id);
						StrSql += " (COALESCE ((SELECT sum(vouchertrans_alt_qty)"
								+ " FROM axela_acc_voucher_trans"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher grn ON grn.voucher_id = vouchertrans_voucher_id"
								+ " WHERE grn.voucher_grn_id  = "
								+ multivoucher_id
								+ ""
								+ " AND vouchertrans_item_id = "
								+ vouchertrans_item_id
								+ ""
								+ " AND vouchertrans_rowcount = "
								+ vouchertrans_rowcount
								+ ""
								+ " AND vouchertrans_option_id = "
								+ vouchertrans_option_id
								+ ""
								+ " AND voucher_vouchertype_id = "
								+ voucher_vouchertype_id
								+ ""
								+ " ),0) / ("
								+ cart_uom_ratio + "))";

					}
					StrSql += " ) ,vouchertrans_alt_qty)"
							+ " /"
							+ " IF (vouchertrans_discount = 0 && vouchertrans_tax = 0,"
							+ " COALESCE ((SELECT sum(vouchertrans_alt_qty)"
							+ " FROM axela_acc_voucher_trans"
							+ " WHERE vouchertrans_voucher_id = "
							+ multivoucher_id
							+ ""
							+ " AND vouchertrans_item_id = "
							+ vouchertrans_item_id
							+ ""
							+ " AND vouchertrans_rowcount = "
							+ vouchertrans_rowcount
							+ ""
							+ " AND vouchertrans_option_id = "
							+ vouchertrans_option_id
							+ ""
							+ "),0)"// From voucher qty
							+ " , vouchertrans_alt_qty)), 0.000000)"
							+ " FROM "
							+ " axela_acc_voucher_trans"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
							+ " WHERE vouchertrans_item_id != 0"
							+ " AND vouchertrans_voucher_id = " + multivoucher_id
							+ ""
							+ " LIMIT 1";
					// SOP("StrSql====ratio===" + StrSql);
					ratio = ExecuteQuery(StrSql);
					if (!ratio.equals("0.0000000000")) {
						mul_ratio = Double.parseDouble(ratio);
					}
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
								+ " cart_customer_id,"
								+ " cart_emp_id," + " cart_session_id,"
								+ " cart_location_id," + " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount," + " cart_option_id,"
								+ " cart_option_group," + " cart_item_serial,"
								+ " cart_item_batch_id," + " cart_price,"
								+ " cart_qty," + " cart_truckspace,"
								+ " cart_netprice," + " cart_amount,"
								+ " cart_unit_cost," + " cart_alt_qty,"
								+ " cart_alt_uom_id," + " cart_convfactor,"
								+ " cart_time," + " cart_dc)" + " VALUES"
								+ " ( 0 ,"
								+ " "
								+ voucher_vouchertype_id
								+ ","
								+ " "
								+ vouchertrans_multivoucher_id
								+ ","
								+ " "
								+ vouchertrans_customer_id
								+ ","
								+ " "
								+ emp_id
								+ ","
								+ " "
								+ cart_session_id
								+ ","
								+ " '"
								+ vouchertrans_location_id
								+ "',"
								+ " "
								+ vouchertrans_item_id
								+ ","
								+ " '0',"
								+ " 0.0,"
								+ " '0',"
								+ " "
								+ vouchertrans_tax_id
								+ ","
								+ " "
								+ vouchertrans_rowcount
								+ ","
								+ " "
								+ vouchertrans_option_id
								+ ","
								+ " '',"
								+ " '"
								+ CNumeric(vouchertrans_item_serial)
								+ "',"
								+ " "
								+ vouchertrans_item_batch_id + ",";
						StrSql += " " + vouchertrans_price + ",";
						StrSql += " " + (mul_ratio * vouchertrans_qty) + ","
								+ " "
								+ (mul_ratio * vouchertrans_truckspace) + ","
								+ " "
								+ (mul_ratio * vouchertrans_netprice) + ","
								+ " "
								+ (mul_ratio * vouchertrans_amount) + "," + " "
								+ vouchertrans_unit_cost + ",";
						if (voucher_vouchertype_id.equals("10")) {
							StrSql += " "
									+ (mul_ratio * vouchertrans_alt_qty * cart_uom_ratio)
									+ ",";
							if (!CNumeric(uom_parent_id).equals("0")) {
								StrSql += " " + uom_parent_id + ",";
							} else {
								StrSql += " " + vouchertrans_alt_uom_id + ",";
							}
						} else {
							StrSql += " " + (mul_ratio * vouchertrans_alt_qty)
									+ ","
									+ " " + vouchertrans_alt_uom_id + ",";
						}
						StrSql += vouchertrans_convfactor + "," + " "
								+ ToLongDate(kknow()) + "," + " '"
								+ vouchertrans_dc
								+ "'" + " )";

						// SOP("StrSql==cart main item=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
				}

				if (!vouchertrans_rowcount.equals("0")
						&& !vouchertrans_option_id.equals("0")) {
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_voucher_id,"
								+ " cart_vouchertype_id,"
								+ " cart_multivoucher_id,"
								+ " cart_customer_id,"
								+ " cart_emp_id," + " cart_session_id,"
								+ " cart_location_id," + " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id,"
								+ " cart_rowcount," + " cart_option_id,"
								+ " cart_option_group," + " cart_item_serial,"
								+ " cart_item_batch_id," + " cart_price,"
								+ " cart_qty," + " cart_truckspace,"
								+ " cart_netprice," + " cart_amount,"
								+ " cart_unit_cost," + " cart_alt_qty,"
								+ " cart_alt_uom_id," + " cart_convfactor,"
								+ " cart_time," + " cart_dc)" + " VALUES"
								+ " ( 0,"
								+ " "
								+ voucher_vouchertype_id
								+ ","
								+ " "
								+ vouchertrans_multivoucher_id
								+ ","
								+ " "
								+ vouchertrans_customer_id
								+ ","
								+ " "
								+ emp_id
								+ ","
								+ " "
								+ cart_session_id
								+ ","
								+ " '"
								+ vouchertrans_location_id
								+ "',"
								+ " "
								+ vouchertrans_item_id
								+ ","
								+ " '"
								+ vouchertrans_discount
								+ "',"
								+ " " + vouchertrans_discount_perc + ","
								+ " '"
								+ vouchertrans_tax
								+ "',"
								+ " "
								+ vouchertrans_tax_id
								+ " ,"
								+ " "
								+ vouchertrans_rowcount
								+ ","
								+ " "
								+ vouchertrans_option_id
								+ ","
								+ " '',"
								+ " '',"
								+ " 0,";
						StrSql += " " + vouchertrans_price + ",";
						StrSql += " 0.00," + " 0.00," + " 0.00," + " "
								+ (mul_ratio * vouchertrans_amount) + ","
								+ " 0.00,"
								+ " 0.00," + " 0," + " 1," + " "
								+ ToLongDate(kknow())
								+ "," + " '" + vouchertrans_dc + "'" + " )";
						// SOP("StrSql==disc tax=" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					}
				}

				// bill sundry tax ...
				if (vouchertrans_rowcount.equals("0")
						&& vouchertrans_option_id.equals("0")
						&& !vouchertrans_tax_id.equals("0")) {
					if (mul_ratio > 0.0) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart"
								+ " (cart_customer_id,"
								+ " cart_vouchertype_id,"
								+ " cart_emp_id," + " cart_session_id,"
								+ " cart_location_id," + " cart_item_id,"
								+ " cart_discount,"
								+ " cart_discount_perc,"
								+ " cart_tax,"
								+ " cart_tax_id," + " cart_rowcount,"
								+ " cart_option_id," + " cart_option_group,"
								+ " cart_item_serial," + " cart_item_batch_id,"
								+ " cart_price," + " cart_qty,"
								+ " cart_truckspace," + " cart_netprice,"
								+ " cart_amount,"
								+ " cart_unit_cost,"
								+ " cart_alt_qty,"
								+ " cart_alt_uom_id,"
								+ " cart_convfactor,"
								+ " cart_time,"
								+ " cart_dc)"
								+ " VALUES" + " ("
								+ vouchertrans_customer_id
								+ ","
								+ " "
								+ voucher_vouchertype_id
								+ ","
								+ " "
								+ emp_id
								+ ","
								+ " "
								+ cart_session_id
								+ ","
								+ " '"
								+ vouchertrans_location_id
								+ "',"
								+ " "
								+ vouchertrans_item_id
								+ ","
								+ " '"
								+ vouchertrans_discount
								+ "',"
								+ " 0.0,"
								+ " '"
								+ vouchertrans_tax
								+ "',"
								+ " "
								+ vouchertrans_tax_id
								+ " ,"
								+ " "
								+ vouchertrans_rowcount
								+ ","
								+ " "
								+ vouchertrans_option_id
								+ ","
								+ " '',"
								+ " '',"
								+ " 0,";
						StrSql += " " + vouchertrans_price + ",";
						StrSql += " 0.00,"
								+ " 0.00,"
								+ " 0.00,"
								+ " "
								+ (vouchertrans_amount)
								+ ","
								+ " 0.00,"
								+ " 0.00,"
								+ " 0,"
								+ " 1,"
								+ " " + ToLongDate(kknow()) + ","
								+ " '" + vouchertrans_dc + "'" + " )";
						// SOP("StrSql==bill sundry tax=" +
						stmttx.addBatch(StrSql);
					}
				}
			}
			stmttx.executeBatch();
			conntx.commit();
			crs1.close();
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
					+ new Exception().getStackTrace()[0].getMethodName()
					+ ": " + e);
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
