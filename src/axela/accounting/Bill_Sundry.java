package axela.accounting;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Bill_Sundry extends Connect {

	public String StrHTML = "";
	public String search = "";
	public String StrSearch = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "Add";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String RecCountDisplay = "";
	public String vouchertype_id = "0";
	public String voucher_id = "0";
	public String billsundry = "";
	public String cart_total = "", cart_total_value = "";
	public String cart_total_deci_value = "", cart_voucher_id = "0";
	public String[] cart_total_arr;
	public String tax_name = "";

	public String cart_bs_tax_id = "0";
	public String cart_bs_total = "0";
	public String cart_bs_tax_rate = "0", cart_bs_tax_amt = "0";
	// This cart_bsamt_carttotal var is to add cart_total + cart_bs_tax_amt;
	// Where tax_id != 36,37,56,57
	public double cart_bsamt_carttotal = 0.0;
	public String cart_ledger_tax_dc = "0";
	public String cart_bs_customer_id = "";
	public String cart_id = "0", cart_session_id = "0";
	public String po = "";
	public String so = "";
	public String call = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				if (!GetSession("emp_id", request).equals("")) {
					emp_id = CNumeric(GetSession("emp_id", request));
					add = PadQuotes(request.getParameter("add"));
					addB = PadQuotes(request.getParameter("add_button"));
					deleteB = PadQuotes(request.getParameter("delete_button"));
					update = PadQuotes(request.getParameter("update"));
					billsundry = PadQuotes(request.getParameter("billsundry"));
					po = PadQuotes(request.getParameter("po"));
					so = PadQuotes(request.getParameter("so"));
					call = PadQuotes(request.getParameter("call"));
					cart_session_id = CNumeric(PadQuotes(request.getParameter("cart_session_id")));
					vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
					voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
					cart_total = CNumeric(PadQuotes(request.getParameter("total")));
					cart_id = CNumeric(PadQuotes(request.getParameter("cart_id")));
					if (add.equals("yes") || update.equals("yes")) {
						StrHTML = Listdata();
					}
					if (addB.equals("yes")) {
						GetValues(request, response);
						AddCartBillSundry();
						StrHTML = Listdata();
					}
					if (deleteB.equals("Delete")) {
						DeleteFields(response);
						StrHTML = Listdata();
					}

				} else {
					StrHTML = "SignIn";
				}
			}
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		cart_bs_tax_id = PadQuotes(request.getParameter("dr_bill_sundry"));
		cart_bs_tax_amt = PadQuotes(request.getParameter("txt_billsundry_amt"));
		if (!cart_bs_tax_id.equals("0")) {
			cart_bs_tax_rate = cart_bs_tax_id.substring(cart_bs_tax_id.indexOf("-") + 1, cart_bs_tax_id.length());
			cart_bs_tax_id = cart_bs_tax_id.substring(0, cart_bs_tax_id.indexOf("-"));
			cart_bs_customer_id = CNumeric(ExecuteQuery("SELECT customer_id"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_id = " + cart_bs_tax_id
					+ " AND customer_tax = 1"));
		}
		tax_name = ExecuteQuery("SELECT LOWER(customer_name) FROM " + compdb(comp_id) + "axela_customer"
				+ " WHERE 1 = 1"
				+ " AND customer_tax = 1"
				+ " AND customer_active = 1"
				+ " AND customer_id = " + cart_bs_tax_id);

		if (tax_name.contains("discount")) {
			cart_bs_tax_amt = cart_bs_tax_amt;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (cart_bs_tax_id.equals("0")) {
			msg += "<br>Select Bill Sundry!";
		}
		if (!cart_bs_tax_id.equals("0")) {
			StrSql = "SELECT cart_id FROM  " + compdb(comp_id) + "axela_acc_cart" + " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id + ""
					+ " AND cart_rowcount = 0" + " AND cart_option_id = 0"
					+ " AND cart_tax_id = " + cart_bs_tax_id
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id;
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br>Similer Bill Sundry Tax Present!";
			}
		}
	}

	public void AddCartBillSundry() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			if (tax_name.contains("discount")) {
				cart_ledger_tax_dc = "1";
			} else {
				cart_ledger_tax_dc = "0";
			}

			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_cart"
					+ " (cart_voucher_id,"
					+ " cart_vouchertype_id,"
					+ " cart_customer_id,"
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
					+ " " + vouchertype_id + ","
					+ " " + cart_bs_customer_id + ","
					+ " " + emp_id + ","
					+ " " + cart_session_id + ","
					+ " 0,"
					+ " 0,";
			if (tax_name.contains("discount")) {
				StrSql += "'1',";
			} else {
				StrSql += "'0',";
			}
			StrSql += " '0',"
					+ " " + cart_bs_tax_id + ","
					+ " 0,"
					+ " 0,"
					+ " '',"
					+ " '',"
					+ " 0,"
					+ " "
					+ 0.00
					+ ","
					+ " 0.00,"
					+ " " + Double.parseDouble(cart_bs_tax_amt) + ","
					+ " " + Double.parseDouble(cart_bs_tax_amt) + ","
					+ " 0.00,"
					+ " 0,"
					+ " 0,"
					+ " 0,"
					+ " " + ToLongDate(kknow()) + ","
					+ " '" + cart_ledger_tax_dc + "')";
			// SOP("StrSql==bs-tax===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
			// StrHTML = ListCartItems(emp_id, cart_session_id, vouchertype_id);
		}

	}

	public String Listdata() {
		int count = 0;
		int TotalRecords = 0;
		double cart_bs_total = 0.0;
		int bs_tax_id = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT cart_id, CONCAT(customer_id) AS tax_id,"
				+ " CONCAT(customer_name) AS tax_name,"
				+ " CONCAT(customer_rate) AS tax_rate, cart_amount"
				+ " FROM  " + compdb(comp_id) + "axela_acc_cart"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
				+ " WHERE 1=1"
				+ " AND cart_vouchertype_id = " + vouchertype_id
				+ " AND cart_rowcount = 0"
				+ " AND cart_option_id = 0"
				+ " AND cart_emp_id = " + emp_id
				+ " AND cart_session_id = " + cart_session_id
				+ " AND customer_tax = 1"
				+ " GROUP BY customer_id"
				+ " ORDER BY customer_name, customer_id";
		try {
			// SOP("StrSql==ls==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				// Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<thead>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Tax</th>\n");
				Str.append("<th>Rate</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.beforeFirst();
				while (crs.next()) {
					bs_tax_id = crs.getInt("tax_id");
					// if (bs_tax_id != 36 || bs_tax_id != 56 || bs_tax_id != 37
					// || bs_tax_id != 57 || bs_tax_id != 71) {
					cart_bs_total += crs.getDouble("cart_amount");
					// }
					cart_bsamt_carttotal = cart_bs_total + Double.parseDouble(cart_total);
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("tax_name")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("tax_rate")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("cart_amount"))).append("</td>\n");
					Str.append("<td valign=top align=center>");
					Str.append("<form name=\"form2\"  method=\"post\">");
					Str.append("<input type=\"hidden\" name=\"cart_id\" value=\"" + crs.getString("cart_id") + "\">");
					Str.append(" <input name=\"delete_button\" type=\"button\" class=\"btn btn-success\" id=\"delete_button\""
							+ " OnClick=\"DeleteCartTax(" + crs.getString("cart_id") + ")\" value=\"Delete\" />")
							// return confirmdelete(this)
							.append("</form></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				cart_bsamt_carttotal = Double.parseDouble(cart_total);
				RecCountDisplay = "<br><br><br><br><font color=red><b>No Record Found!</b></font><br><br>";
				return RecCountDisplay;
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBillSundry() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id , customer_rate , customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE 1=1";
			if (po.equals("yes")) {
				StrSql += " AND( customer_taxtype_id = 1 OR customer_taxtype_id = 0)";
			}
			if (so.equals("yes")) {
				StrSql += " AND ( customer_taxtype_id = 2 OR customer_taxtype_id = 0)";
			}
			StrSql += " AND customer_tax = 1"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql=ExecssPopp===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id")).append("-").append(crs.getString("customer_rate"));
				Str.append("").append(">").append(crs.getString("customer_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateBillDiscount() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT 	CONCAT(customer_id) AS tax_id,"
					+ " CONCAT(customer_rate) AS tax_rate, CONCAT(customer_name) AS tax_name"
					+ " FROM " + compdb(comp_id) + "axela_customer" + " WHERE 1=1";
			if (po.equals("yes")) {
				StrSql += " AND( customer_taxtype_id = 1 OR customer_taxtype_id = 0)";
			}
			if (so.equals("yes")) {
				StrSql += " AND ( customer_taxtype_id = 2 OR customer_taxtype_id = 0)";
			}
			StrSql += " AND customer_tax = 1"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql=ExecssPopp===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id")).append("-").append(crs.getString("tax_rate"));
				Str.append("").append(">" + crs.getString("tax_name") + "</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void DeleteFields(HttpServletResponse response)
			throws SQLException {
		try {

			StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_id = " + cart_id + ""
					+ " AND cart_emp_id = " + emp_id
					+ " AND cart_session_id = " + cart_session_id
					+ " AND cart_vouchertype_id = " + vouchertype_id;
			// SOP("StrSql==de==" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception e) {

			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
