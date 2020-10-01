package axela.invoice;
//aJIt 20th November, 2012

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Invoice_Details extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String add = "", status = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String cart_session_id = "", cart_id = "", cart_item_serial = "";
	public String cart_item_id = "", cart_price = "", cart_tax_rate = "";
	public String cart_discount = "", cart_tax = "", cart_tax_id = "", cart_option_group = "";
	public String cart_qty = "", cart_time = "", cart_total = "", cart_option_id = "";
	public String list_cartitems = "", msg = "", cart_rowcount = "", rowcount = "";
	public String add_cartitem = "";
	public String update_cartitem = "";
	public String delete_cartitem = "";
	public String configure = "", mode = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{

			emp_id = CNumeric(GetSession("emp_id", request));
			cart_session_id = CNumeric(PadQuotes(request.getParameter("session_id")));
			cart_item_id = CNumeric(PadQuotes(request.getParameter("cart_item_id")));
			cart_item_serial = PadQuotes(request.getParameter("cart_item_serial"));
			cart_price = CNumeric(PadQuotes(request.getParameter("cart_price")));
			cart_discount = CNumeric(PadQuotes(request.getParameter("cart_discount")));
			cart_tax = CNumeric(PadQuotes(request.getParameter("cart_tax")));
			cart_tax_id = CNumeric(PadQuotes(request.getParameter("cart_tax_id")));
			cart_tax_rate = CNumeric(PadQuotes(request.getParameter("cart_tax_rate")));
			cart_qty = CNumeric(PadQuotes(request.getParameter("cart_qty")));
			cart_total = CNumeric(PadQuotes(request.getParameter("cart_total")));
			cart_time = PadQuotes(request.getParameter("cart_time"));
			rowcount = PadQuotes(request.getParameter("rowcount"));
			mode = PadQuotes(request.getParameter("mode"));
			status = PadQuotes(request.getParameter("status"));
			cart_option_group = PadQuotes(request.getParameter("cart_option_group"));
			cart_option_id = CNumeric(PadQuotes(request.getParameter("cart_option_id")));
			cart_id = CNumeric(PadQuotes(request.getParameter("cart_id")));
			list_cartitems = PadQuotes(request.getParameter("list_cartitems"));
			configure = PadQuotes(request.getParameter("configure"));
			add_cartitem = PadQuotes(request.getParameter("add_cartitem"));
			update_cartitem = PadQuotes(request.getParameter("update_cartitem"));
			delete_cartitem = PadQuotes(request.getParameter("delete_cartitem"));
			if (!cart_session_id.equals("0") && add_cartitem.equals("yes") && msg.equals("")) {
				AddCartItem();
			} else if (!cart_id.equals("0") && update_cartitem.equals("yes") && msg.equals("")) {
				UpdateCartItem();
			} else if (!cart_session_id.equals("0") && list_cartitems.equals("yes")) {
				ListCartItems();
			} else if (!cart_id.equals("0") && delete_cartitem.equals("yes") && !cart_session_id.equals("0")) {
				DeleteCartItem();
			} else if (!cart_item_id.equals("0") && configure.equals("yes")) {
				GetConfigurationDetails();
			}
		}

	}

	public void AddCartItem() {
		if (rowcount.equals("yes")) {
			cart_rowcount = ExecuteQuery("SELECT COALESCE(MAX(cart_rowcount), 0) + 1 FROM " + compdb(comp_id) + "axela_invoice_cart");
			cart_option_id = "0";
		} else {
			cart_rowcount = "0";
			if (cart_id.equals("0")) {
				cart_option_id = ExecuteQuery("SELECT coalesce(max(cart_rowcount),0) from " + compdb(comp_id) + "axela_invoice_cart");
			} else {
				cart_option_id = ExecuteQuery("SELECT coalesce(cart_rowcount,0) from " + compdb(comp_id) + "axela_invoice_cart"
						+ " where cart_id = " + cart_id + "");
			}
		}
		StrSql = "INSERT into " + compdb(comp_id) + "axela_invoice_cart"
				+ " (cart_emp_id,"
				+ " cart_session_id,"
				+ " cart_rowcount,"
				+ " cart_item_id,"
				+ " cart_option_id,"
				+ " cart_option_group,"
				+ " cart_item_serial,"
				+ " cart_price,"
				+ " cart_qty,"
				+ " cart_discount,"
				+ " cart_tax,"
				+ " cart_tax_id,"
				+ " cart_tax_rate,"
				+ " cart_total,"
				+ " cart_time)"
				+ " values"
				+ " (" + emp_id + ","
				+ " " + cart_session_id + ","
				+ " " + cart_rowcount + ","
				+ " " + cart_item_id + ","
				+ " " + cart_option_id + ","
				+ " '" + cart_option_group + "',"
				+ " '" + CNumeric(cart_item_serial) + "',"
				+ " " + cart_price + ","
				+ " " + cart_qty + ","
				+ " " + cart_discount + ","
				+ " " + cart_tax + ","
				+ " " + cart_tax_id + ","
				+ " " + cart_tax_rate + ","
				+ " " + cart_total + ","
				+ " '" + ToLongDate(kknow()) + "')";
		updateQuery(StrSql);
		ListCartItems();
	}

	public void UpdateCartItem() {
		StrSql = "Select cart_rowcount from " + compdb(comp_id) + "axela_invoice_cart"
				+ " where cart_id = " + cart_id + "";
		cart_option_id = ExecuteQuery(StrSql); // get all the configured items for that item by using the cart_rowcount

		StrSql = "Delete from " + compdb(comp_id) + "axela_invoice_cart"
				+ " where cart_option_id = " + cart_option_id + "";
		updateQuery(StrSql); // delete all the configured items for that item by using cart_option_id

		StrSql = "Update " + compdb(comp_id) + "axela_invoice_cart"
				+ " SET"
				+ " cart_item_id = " + cart_item_id + ","
				+ " cart_item_serial = '" + cart_item_serial + "',"
				+ " cart_price = " + cart_price + ","
				+ " cart_discount = " + cart_discount + ","
				+ " cart_tax = " + cart_tax + ","
				+ " cart_tax_id = " + cart_tax_id + ","
				+ " cart_tax_rate = " + cart_tax_rate + ","
				+ " cart_qty = " + cart_qty + ","
				+ " cart_total = " + cart_total + ","
				+ " cart_time = '" + ToLongDate(kknow()) + "',"
				+ " cart_total = " + cart_total + ""
				+ " where cart_id = " + cart_id + "";
		updateQuery(StrSql);
		ListCartItems();
	}

	public void DeleteCartItem() {
		try {
			cart_option_id = ExecuteQuery("Select cart_rowcount from " + compdb(comp_id) + "axela_invoice_cart"
					+ " where cart_id = " + cart_id + "");
			StrSql = "Delete from " + compdb(comp_id) + "axela_invoice_cart"
					+ " where cart_option_id = " + cart_option_id + "";
			updateQuery(StrSql);
			StrSql = "Delete from " + compdb(comp_id) + "axela_invoice_cart"
					+ " where cart_id = " + cart_id + "";
			updateQuery(StrSql);
			ListCartItems();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void GetConfigurationDetails() {
		String group = "", checked = "", disabled = "";
		int groupitemcount = 0, groupcount = 0;
		Double all_selected_total = 0.00, multi_select_basetotal = 0.00;
		StringBuilder Str = new StringBuilder();
		if (!cart_id.equals("0")) {
			cart_option_id = ExecuteQuery("Select cart_rowcount from " + compdb(comp_id) + "axela_invoice_cart"
					+ " where cart_id = " + cart_id + "");
		}
		StrSql = "Select item_id, item_name, group_type, group_name, price_amt, opt.option_group_id, item_serial,"
				+ " opt.option_qty, item_code, opt.option_id, opt.option_select, item_small_desc, group_id,"
				+ " (Select count(DISTINCT optitem.option_id) from " + compdb(comp_id) + "axela_inventory_item_option optitem"
				+ " where optitem.option_itemmaster_id = opt.option_itemmaster_id and optitem.option_group_id = group_id"
				+ " group by optitem.option_group_id) as groupitemcount, coalesce(cart_option_id,0) as cart_option_id,"
				+ " coalesce(cart_option_group,'') as cart_option_group"
				+ " from " + compdb(comp_id) + "axela_inventory_item_option opt"
				+ " inner join " + compdb(comp_id) + "axela_inventory_group on group_id = option_group_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = option_item_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
				+ " left join " + compdb(comp_id) + "axela_invoice_cart on cart_item_id = item_id"
				+ " and cart_option_id = '" + cart_option_id + "'"
				+ " where option_itemmaster_id = " + cart_item_id + ""
				+ " group by group_name, group_type, item_id"
				+ " order by group_rank, group_name, cart_option_group DESC";
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>Select</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th>Qty</th>\n");
				Str.append("<th>Price</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					checked = "";
					disabled = "";
					if (!group.equals(crs.getString("group_name")) && !group.equals("")) {
						groupitemcount = 0;
					}
					if (!group.equals(crs.getString("group_name"))) {
						groupcount = groupcount + 1;
						Str.append("<tr align=center>\n");
						Str.append("<td align=center colspan=8><b>");
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
						Str.append("</b><input type=\"hidden\" id=\"txt_").append(groupcount).append("_count\" value=\"").append(crs.getString("groupitemcount")).append("\">");
						Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_value\" name=\"txt_").append(groupcount).append("_value\">");
						Str.append("</td></tr>\n");
					}
					groupitemcount = groupitemcount + 1;
					Str.append("<tr>\n");
					if (crs.getString("option_select").equals("1") && mode.equals("add")) {
						checked = "checked";
					} else if (crs.getString("cart_option_id").equals(cart_option_id) && mode.equals("update")) {
						checked = "checked";
					}

					if (crs.getString("group_type").equals("2")) {
						disabled = "disabled";
						all_selected_total = all_selected_total + crs.getDouble("price_amt");
					}

					if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1") && mode.equals("add")) {
						multi_select_basetotal = multi_select_basetotal + crs.getDouble("price_amt");
					} else if (crs.getString("group_type").equals("3") && crs.getString("cart_option_id").equals(cart_option_id) && mode.equals("update")) {
						multi_select_basetotal = multi_select_basetotal + crs.getDouble("price_amt");
					}

					Str.append("<td valign=top align=center >");
					Str.append("<input type=\"checkbox\" id=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" name=\"chk_").append(groupcount).append("_");
					Str.append(groupitemcount).append("\" value=\"").append(crs.getString("price_amt")).append("\" ").append(checked).append(" ").append(disabled);
					if (crs.getString("group_type").equals("1")) { // function for Default
						Str.append(" onclick=\"CalculateDefault(").append(crs.getString("group_id")).append(",").append(crs.getString("groupitemcount")).append(",this.value,").append(groupitemcount)
								.append(",").append(groupcount).append(");\"/>");
						if (crs.getString("option_select").equals("1") && mode.equals("add")) {
							Str.append("<input type=\"hidden\" id=\"txt_").append(crs.getString("group_id"));
							Str.append("_basevalue\" name=\"txt_").append(crs.getString("group_id")).append("_basevalue\" value=\"");
							Str.append(crs.getString("price_amt")).append("\">");
						} else if (crs.getString("cart_option_id").equals(cart_option_id) && mode.equals("update")) {
							Str.append("<input type=\"hidden\" id=\"txt_").append(crs.getString("group_id"));
							Str.append("_basevalue\" name=\"txt_").append(crs.getString("group_id")).append("_basevalue\" value=\"");
							Str.append(crs.getString("price_amt")).append("\">");
						}
					} else if (crs.getString("group_type").equals("2")) { // function for Multi Select
						Str.append("/>");
					} else if (crs.getString("group_type").equals("3")) { // function for Multi Select
						Str.append(" onclick=\"CalculateMultiSelect(this.id,this.value,").append(crs.getString("groupitemcount")).append(",").append(crs.getString("group_id")).append(",")
								.append(groupcount).append(");\"/>");
					}

					Str.append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("item_name"));
					if (!crs.getString("item_code").equals("")) {
						Str.append(" (").append(crs.getString("item_code")).append(")");
					}

					if (!crs.getString("item_small_desc").equals("")) {
						Str.append(" <br>").append(crs.getString("item_small_desc"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					Str.append("").append(crs.getString("option_qty"));
					Str.append("</td><td valign=top align=right>");
					Str.append("").append(crs.getString("price_amt"));
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_gpname\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_gpname\" value=\"");
					Str.append(crs.getString("group_name")).append("\">");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_id\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_id\" value=\"");
					Str.append(crs.getString("item_id")).append("\">");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_qty\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_qty\" value=\"");
					Str.append(crs.getString("option_qty")).append("\">");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_serial\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_serial\" value=\"");
					Str.append(crs.getString("item_serial")).append("\">");
					Str.append("<input type=\"hidden\" id=\"txt_").append(groupcount).append("_").append(groupitemcount);
					Str.append("_price\" name=\"txt_").append(groupcount).append("_").append(groupitemcount).append("_price\" value=\"");
					Str.append(crs.getString("price_amt")).append("\">");
					Str.append("</td></tr>\n");
					group = crs.getString("group_name");
				}
				Str.append("<tr><td colspan=8><input type=\"hidden\" id=\"txt_group_count\" name\"txt_group_count\" value=\"").append(groupcount).append("\"/>");
				Str.append("<input type=\"hidden\" id=\"txt_multiselect_basetotal\" name\"txt_multiselect_basetotal\" value=\"").append(multi_select_basetotal).append("\"/>");
				Str.append("<input type=\"hidden\" id=\"txt_allselected_total\" name\"txt_allselected_total\" value=\"").append(all_selected_total).append("\"/>");
				Str.append("</td></tr></table>");
			} else {
				Str.append("");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		StrHTML = Str.toString();
	}

	public void ListCartItems() {
		StringBuilder Str = new StringBuilder();
		Double invoice_grandtotal = 0.00, total = 0.00;
		Double invoice_discamt = 0.00, pricetax = 0.00;
		Double invoice_totaltax = 0.00, selling_price = 0.00;
		Double invoice_qty = 0.00, group_total = 0.00;
		Double invoice_netamt = 0.00;
		int count = 0;
		String type_name = "";
		StrSql = "Select crt.cart_id, crt.cart_item_id,"
				+ " item_name, item_type_id, crt.cart_qty,"
				+ " crt.cart_price, item_nonstock, price_amt,"
				+ " crt.cart_discount, crt.cart_tax_id, item_code,"
				+ " item_id, crt.cart_tax_rate, crt.cart_item_serial,"
				+ " coalesce(crt.cart_option_group,'') as cart_option_group,"
				+ " type_name, crt.cart_invoiceitem_id,"
				+ " COALESCE(crt.cart_tax,'0') as cart_tax,"
				+ " COALESCE(customer_name,'') as tax_name, price_variable,"
				+ " if(item_nonstock = '0', coalesce(stock_current_qty, 0), '_') as stock_current_qty,"
				// + " item_laundry_process_id,"
				+ " coalesce((select COUNT(DISTINCT cart_id)"
				+ " from " + compdb(comp_id) + "axela_invoice_cart cart"
				+ " where cart.cart_option_id = crt.cart_rowcount"
				+ " GROUP BY cart.cart_option_id),0) as opitemcount"
				+ " from " + compdb(comp_id) + "axela_invoice_cart crt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = crt.cart_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type on type_id = item_type_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
				+ " left JOIN " + compdb(comp_id) + "axela_inventory_salescat on salescat_id = item_cat_id"
				+ " left join " + compdb(comp_id) + "axela_customer on customer_id = crt.cart_tax_id"
				+ " left join " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
				+ " where crt.cart_session_id = " + cart_session_id + ""
				+ " and crt.cart_rowcount != 0"
				+ " group by crt.cart_id"
				+ " order by type_rank";
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<table width=100% border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=listtable>");
		try {
			if (!msg.equals("")) {
				Str.append("<tr><td colspan=8 align=center><font color=red><b>").append(msg).append("</b></font></td></tr>");
			}
			Str.append("<tr><th>#</th>");
			Str.append("<th>X</th>");
			Str.append("<th>Item</th>");
			Str.append("<th>Qty</th>");
			Str.append("<th>Price</th>");
			Str.append("<th>MRP</th>");
			Str.append("<th>Discount</th>");
			Str.append("<th>Selling Price</th>");
			Str.append("<th>Amount</th>");
			Str.append("</tr>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					invoice_netamt = invoice_netamt + (crs.getDouble("cart_qty") * ((Double.parseDouble(crs.getString("cart_price"))) - Double.parseDouble(crs.getString("cart_discount"))));
					total = Double.parseDouble(crs.getString("cart_qty"))
							* (Double.parseDouble(crs.getString("cart_price")) - Double.parseDouble(crs.getString("cart_discount")) + Double.parseDouble(crs.getString("cart_tax")));
					pricetax = (crs.getDouble("cart_price") + ((crs.getDouble("cart_price")) * (crs.getDouble("cart_tax_rate")) / 100));
					selling_price = (Double.parseDouble(crs.getString("cart_price")) - Double.parseDouble(crs.getString("cart_discount")) + Double.parseDouble(crs.getString("cart_tax")));
					invoice_grandtotal = invoice_grandtotal + total;
					invoice_discamt = invoice_discamt + (Double.parseDouble(crs.getString("cart_qty")) * Double.parseDouble(crs.getString("cart_discount")));
					// if (crs.getString("item_nonstock").equals("0") || !crs.getString("item_laundry_process_id").equals("0")) {
					if (crs.getString("item_nonstock").equals("0")) {
						invoice_qty = invoice_qty + Double.parseDouble(crs.getString("cart_qty"));
					}
					invoice_totaltax = invoice_totaltax + (Double.parseDouble(crs.getString("cart_tax")) * Double.parseDouble(crs.getString("cart_qty")));
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}

					if (!crs.getString("type_name").equals(type_name)) {
						type_name = crs.getString("type_name");
						if (group_total != 0.00) {
							Str.append("<tr>\n");
							Str.append("<td align=\"right\" colspan=\"8\"><b>Total:</b></td>\n");
							Str.append("<td align=\"right\">").append(group_total).append("</td>");
							Str.append("</tr>\n");
						}
						Str.append("<tr>\n");
						Str.append("<td align=\"center\" colspan=\"9\"><b>").append(type_name).append("</b></td>\n");
						Str.append("</tr>\n");
						group_total = 0.00;
						count = 0;
					}
					count = count + 1;
					group_total = group_total + total;

					Str.append("\n<tr valign=top");
					if (!crs.getString("opitemcount").equals("0") && status.equals("Update") && !crs.getString("cart_invoiceitem_id").equals("0")) {
						Str.append(" onClick=\"PreventUpdate();\"");
					} else {
						Str.append(" onClick=\"ItemDetails(");
						Str.append(crs.getString("item_id")).append(",");
						Str.append(crs.getString("item_type_id")).append(",");
						Str.append("'").append(item_name).append("',");
						Str.append(crs.getDouble("cart_qty")).append(",");
						Str.append(crs.getString("price_amt")).append(",");
						Str.append(crs.getString("cart_price")).append(",");
						Str.append(crs.getString("price_variable")).append(",");
						Str.append(crs.getDouble("cart_discount")).append(",");
						Str.append(crs.getString("cart_tax_id")).append(",");
						Str.append(crs.getString("cart_tax_rate")).append(",");
						Str.append("'").append(crs.getString("tax_name")).append("',");
						Str.append("'',");
						Str.append("'").append(crs.getString("cart_item_serial")).append("',");
						Str.append(crs.getString("cart_id")).append(",'");
						Str.append(crs.getString("stock_current_qty")).append("',");
						Str.append("'update');\"");
					}
					Str.append(">\n<td align=\"center\">");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<a href=\"javascript:delete_cart_item(").append(crs.getString("cart_id")).append(");\">X</a>");
					Str.append("</td>");
					Str.append("<td align=left>");
					Str.append(item_name);
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append(crs.getDouble("cart_qty"));
					Str.append("</td>");
					Str.append("<td align=right >");
					Str.append(df.format(crs.getDouble("cart_price")));
					Str.append("</td>");
					Str.append("<td align=right >");
					Str.append(df.format(pricetax));
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append(crs.getDouble("cart_discount"));
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append(df.format(selling_price));
					Str.append("</td>");
					Str.append("<td align=right >");
					Str.append(df.format(total));
					Str.append("</td>");
					Str.append("</tr>");
				}
			}
			if (group_total != 0.00) {
				Str.append("<tr>\n");
				Str.append("<td align=\"right\" colspan=\"8\"><b>Total:</b></td>\n");
				Str.append("<td align=\"right\">").append(df.format(group_total)).append("</td>");
				Str.append("</tr>\n");
			}
			Str.append("<tr>");
			Str.append("<td align=\"right\" colspan=\"8\"><b>Grand Total:</b></td>");
			Str.append("<td align=\"right\"><input type=\"hidden\" name=\"txt_invoice_totaltax\" id=\"txt_invoice_totaltax\" value=\"").append(df.format(invoice_totaltax)).append("\">");
			Str.append("<input type=\"hidden\" name=\"txt_invoice_netamt\" id=\"txt_invoice_netamt\" value=\"").append(df.format(invoice_netamt)).append("\">");
			Str.append("<input type=\"hidden\" name=\"txt_invoice_grandtotal\" id=\"txt_invoice_grandtotal\" value=\"").append(df.format(Math.ceil(invoice_grandtotal))).append("\"><b>")
					.append(df.format(Math.ceil(invoice_grandtotal))).append("</b></td>");
			Str.append("</tr>");
			Str.append("<tr>");
			Str.append("<td align=\"right\" colspan=\"8\">Total Savings:</td>");
			Str.append("<td align=\"right\"><input type=\"hidden\" name=\"txt_invoice_discamt\" id=\"txt_invoice_discamt\" value=\"").append(df.format(invoice_discamt)).append("\">")
					.append(df.format(invoice_discamt)).append("</td>");
			Str.append("</tr>");
			Str.append("<tr>");
			Str.append("<td align=\"right\" colspan=\"8\">Net Quantity:</td>");
			Str.append("<td align=\"right\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"").append(df.format(invoice_qty)).append("\">")
					.append(df.format(invoice_qty)).append("</td>");
			Str.append("</tr>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</table>");
		StrHTML = Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
