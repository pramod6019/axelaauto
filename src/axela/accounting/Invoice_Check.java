package axela.accounting;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Invoice_Check extends Connect {

	public String emp_id = "0", emp_branch_id = "0", mode = "add";
	public String rateclass_id = "";
	public String StrSql = "", vouchertype_id = "0";
	public String comp_id = "0";
	public String cart_session_id = "0";
	public String location_id = "";
	public String branch_id = "0";
	public String voucher_branch_id = "0", branch_exe = "";
	public String branch_location = "";
	public String bin_location = "";
	public String bin_id = "0";
	public String location_bin = "";
	public String voucher_contact_id = "";
	public String contact_mobile1 = "";
	public String contact_phone1 = "", id = "";
	public String contact_email1 = "", q = "";
	public String StrHTML = "", StrSearch = "";
	public String config_sales_quote_updateprice = "";
	public String config_sales_quote_updatediscount = "";
	public String contact_fname = "", contact_lname = "";
	public String search_name = "";
	public String comp_module_inventory = "0";
	public String config_inventory_current_stock = "0", item_cat_id = "0", item_serial_id = "0";
	public String customer_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			PropertyVetoException, SQLException {
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				comp_module_inventory = CNumeric(GetSession("comp_module_inventory", request) + "");
				config_inventory_current_stock = CNumeric(GetSession("config_inventory_current_stock", request) + "");
				// comp_id = "2017";
				// comp_id = "1006";
				emp_id = CNumeric(GetSession("emp_id", request) + "");
				contact_fname = PadQuotes(request.getParameter("contact_fname"));
				contact_lname = PadQuotes(request.getParameter("contact_lname"));
				search_name = PadQuotes(request.getParameter("search_name"));

				if (!contact_lname.equals("")) {
					contact_lname = " " + contact_lname;
				}
				contact_mobile1 = PadQuotes(request.getParameter("contact_mobile1"));
				contact_phone1 = PadQuotes(request.getParameter("contact_phone1"));
				contact_email1 = PadQuotes(request.getParameter("contact_email1"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				id = PadQuotes(request.getParameter("id"));
				// To be checked branch_id location_id
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				cart_session_id = PadQuotes(request.getParameter("cart_session_id"));
				voucher_branch_id = CNumeric(PadQuotes(request.getParameter("voucher_branch_id")));
				branch_exe = PadQuotes(request.getParameter("branch_exe"));
				branch_location = PadQuotes(request.getParameter("branch_location"));
				bin_location = PadQuotes(request.getParameter("bin_location"));
				voucher_contact_id = CNumeric(PadQuotes(request.getParameter("voucher_contact_id")));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("cart_vouchertype_id")));
				bin_id = CNumeric(PadQuotes(request.getParameter("bin_id")));
				item_cat_id = PadQuotes(request.getParameter("item_cat_id"));
				item_serial_id = CNumeric(PadQuotes(request.getParameter("item_serial_id")));
				location_bin = PadQuotes(request.getParameter("location_bin"));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				// ///////// Search Contacts for mobile
				if (!contact_mobile1.equals("") && !emp_id.equals("0")) {
					StrHTML = SearchMobile();
				} // ///////// Search Contacts for phone1
				else if (!contact_phone1.equals("") && !emp_id.equals("0")) {
					StrHTML = SearchPhone();
				} // ///////// Search Contacts for email
				else if (!contact_email1.equals("") && !emp_id.equals("0")) {
					StrHTML = SearchEmail();
				} else if (!q.equals("") && !rateclass_id.equals("0") && config_inventory_current_stock.equals("1") && !location_id.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && location_id.equals("0")) {
					StrHTML = "<font color=\"red\"><b>Select Location!</b></font>";
				} else if (!q.equals("") && !rateclass_id.equals("0") && config_inventory_current_stock.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && rateclass_id.equals("0")) {
					StrHTML = "<font color=\"red\"><b>Select Rate Class!</b></font>";
				} else if (search_name.equals("yes") && rateclass_id.equals("0")) {
					StrHTML = SearchName();
				}
				// else if (bin_location.equals("yes")) {
				// StrHTML = new Inventory_Bin_Update().PopulateLocation(branch_id);
				// } else if (location_bin.equals("yes")) {
				// StrHTML = new Inventory_Bin_Update().PopulateBin("0", bin_id,
				// location_id, "1");
				// }
				else if (branch_exe.equals("yes")) {
					StrHTML = PopulateExecutives(emp_id, comp_id);
				}

				if (!voucher_contact_id.equals("0") && id.equals("")) {
					StringBuilder Str = new StringBuilder();
					try {
						StrSql = "SELECT customer_address, customer_phone1, customer_mobile1, customer_branch_id,"
								+ " customer_city_id, city_name, country_name, state_name, customer_pin, customer_id,"
								+ " CONCAT(branch_name, ' (', branch_code, ')') branchname, branch_id, rateclass_id,"
								+ " CONCAT(customer_name, ' (', customer_id, ')') custname, customer_id,"
								+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_country ON country_id = state_country_id"
								+ " WHERE contact_id = " + voucher_contact_id + "";
						// SOP("from Invoice_Check StrSql====" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							Str.append("<input type=\"hidden\" name=\"txt_cust\" id=\"txt_cust\"");
							Str.append(" value=\"").append(crs.getString("customer_address"));
							Str.append("[&%]").append(crs.getString("city_name"));
							Str.append("[&%]").append(crs.getString("customer_pin"));
							Str.append("[&%]").append(crs.getString("state_name"));
							Str.append("[&%]").append(crs.getString("country_name"));
							Str.append("[&%]").append(crs.getString("customer_id"));
							Str.append("[&%]").append(crs.getString("custname"));
							Str.append("[&%]").append(voucher_contact_id);
							Str.append("[&%]").append(crs.getString("contactname"));
							Str.append("[&%]").append(crs.getString("branch_id"));
							Str.append("[&%]").append(crs.getString("branchname"));
							Str.append("[&%]").append(crs.getString("rateclass_id"));
							Str.append("\">");
						}
						crs.close();
						StrHTML = Str.toString();
					} catch (Exception ex) {
						SOPError("Axelaauto===" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}
	// ///////// Search Contacts for Name
	public String SearchName() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name, customer_rateclass_id	"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where concat(contact_fname,' ',contact_lname) like '%" + contact_fname + contact_lname + "%'"
				+ " order by contact_fname" + " limit 5";

		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);

			if (crs1.isBeforeFirst()
					&& !(contact_fname + contact_lname).equals("")) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append(",'");
					Str.append(crs1.getString("contact_name")).append("',");
					Str.append(crs1.getString("customer_id")).append(",");
					Str.append(crs1.getString("customer_rateclass_id	")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id, cart_session_id, vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	// ///////// Search Contacts for mobile
	public String SearchMobile() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_mobile1,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_mobile1 like '%" + contact_mobile1 + "%') "
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append(", ").append(crs1.getString("contact_mobile1")).append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id"));
					Str.append("," + "'").append(crs1.getString("contact_name")).append("',");
					Str.append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id, cart_session_id, vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	// ///////// Search Contacts for phone1

	public String SearchPhone() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_phone1, contact_id, customer_id, customer_name,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_phone1 like '%" + contact_phone1 + "%') "
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					if (!crs1.getString("contact_phone1").equals("")) {
						Str.append(", ").append(crs1.getString("contact_phone1")).append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id"));
					Str.append(",'").append(crs1.getString("contact_name")).append("',");
					Str.append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id, cart_session_id, vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	// ///////// Search Contacts for email

	public String SearchEmail() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_email1, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE (contact_email1 LIKE '%" + contact_email1 + "%') "
				+ " ORDER BY contact_fname" + " LIMIT 5";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("").append(crs.getString("contact_name")).append("");
					if (!crs.getString("contact_email1").equals("")) {
						Str.append(", ").append(crs.getString("contact_email1")).append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs.getString("contact_id"));
					Str.append(", '").append(crs.getString("contact_name")).append("',");
					Str.append(crs.getString("customer_id")).append(",'");
					Str.append(crs.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id, cart_session_id, vouchertype_id));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return Str.toString();
	}

	public String SearchItems() {
		SOP("SearchItems==");
		StringBuilder Str = new StringBuilder();
		String item_id = "0";
		String stock_current_qty = "0", boxtype_size = "0", discpercent = "0";
		try {
			if (CNumeric(item_serial_id).equals("0")
			// || item_serial_id.equals("1")
			) {
				// StrSearch = " AND (";
				// + " item_id = '" + q + "'";
				if (Integer.parseInt(item_cat_id) == -1) {
					StrSearch += " AND ( item_code LIKE '" + q + "%')";
				} else if (Integer.parseInt(item_cat_id) == -2) {
					StrSearch += " AND ( item_name LIKE '" + q + "%')";
				} else if (Integer.parseInt(item_cat_id) == 0) {
					StrSearch += " AND ( item_code LIKE '" + q + "%'";
					StrSearch += " OR item_name LIKE '" + q + "%')";
				}
			}
			if (Integer.parseInt(item_cat_id) > 0) {
				StrSearch += " AND item_cat_id = " + item_cat_id;
			}
			StrSql = "SELECT item_id, item_name,"
					+ " COALESCE ((SELECT CONCAT( price.price_amt, ',', price.price_disc, ',', price.price_rateclass_id )"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price price"
					+ " WHERE price.price_item_id = item_id"
					+ " AND price.price_rateclass_id = " + rateclass_id
					+ " AND price.price_effective_from <= '" + ToLongDate(kknow()) + "'"
					+ " AND price.price_active = '1'"
					+ " ORDER BY price.price_effective_from DESC"
					+ " LIMIT 1 ), '0,0,0' ) AS price, item_code,"
					+ " COALESCE(model_name, '') AS model_name, uom_name, item_uom_id,"
					// + " item_alt_uom_id,"
					// + " IF(item_nonstock = 1, COALESCE(stock_current_qty, 0), '-') AS stock_current_qty,"
					+ " COALESCE (stock_current_qty, 0) AS stock_current_qty"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom on uom_id = item_uom_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
					+ " AND stock_location_id = " + location_id
					+ " WHERE item_active = 1"
					+ " AND price_rateclass_id	= " + rateclass_id
					+ " AND price_effective_from <= " + ToLongDate(kknow())
					+ " AND price_active = 1"
					+ StrSearch
					+ " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 10";
			// SOP("StrSql=search items==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered\" style=\"overflow-x:scroll\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n<tr align=center>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Code</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Name</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Model</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>UOM</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Price</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Stock</th>\n");
				Str.append("</tr>\n</thead>\n");
				while (crs.next()) {
					String item_name = crs.getString("item_name");
					String item_code = crs.getString("item_code");
					stock_current_qty = CNumeric(crs.getString("stock_current_qty"));
					Str.append("\n<tr valign=top onClick=\"ItemDetails(");
					Str.append(crs.getInt("item_id")).append(",");
					Str.append("1,");
					Str.append(crs.getInt("item_uom_id") + ",");
					Str.append(Double.parseDouble(crs.getString("price").split(",")[0]) + ",");
					Str.append(Double.parseDouble(crs.getString("price").split(",")[1]) + ",");
					Str.append(discpercent + ",");
					Str.append(boxtype_size + ",");
					Str.append(stock_current_qty + ",");
					Str.append("0,");
					Str.append("'add'").append(",").append(Double.parseDouble(crs.getString("price").split(",")[2])).append(");\">\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(item_code).append("</td>");
					Str.append("<td valign=\"top\" align=\"left\"><font color='blue'>").append(item_name).append("</font></td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("model_name")).append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("uom_name")).append("</td>");
					Str.append("<td valign=\"top\" align=\"right\">").append(df.format(Double.parseDouble(crs.getString("price").split(",")[0]))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(stock_current_qty);
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=\"#ff0000\">No Items Found!</font></b>");
			}
			// }
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, PropertyVetoException, SQLException {
		doGet(request, response);
	}
}