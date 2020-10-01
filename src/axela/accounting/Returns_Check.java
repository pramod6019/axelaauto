package axela.accounting;

//aJIt 5th December, 2012
//Shivaprasad Nov 20 2014

//import axela.inventory.Inventory_Bin_Update;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Returns_Check extends Connect {

	public String emp_id = "0", emp_branch_id = "0", mode = "add";
	public String comp_id = "0", rateclass_id = "";
	public String StrSql = "", vouchertype_id = "0";
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
	public String config_inventory_current_stock = "0",
			item_itemgroup_id = "0", item_serial_id = "0", inv_vouchertype_id = "0";
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
				id = PadQuotes(request.getParameter("id"));
				// To be checked branch_id location_id
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				location_id = CNumeric(PadQuotes(request
						.getParameter("location_id")));
				cart_session_id = PadQuotes(request.getParameter("cart_session_id"));
				voucher_branch_id = CNumeric(PadQuotes(request
						.getParameter("voucher_branch_id")));
				branch_exe = PadQuotes(request.getParameter("branch_exe"));
				branch_location = PadQuotes(request.getParameter("branch_location"));
				bin_location = PadQuotes(request.getParameter("bin_location"));
				voucher_contact_id = CNumeric(PadQuotes(request
						.getParameter("voucher_contact_id")));
				vouchertype_id = CNumeric(PadQuotes(request
						.getParameter("cart_vouchertype_id")));
				if (vouchertype_id.equals("23")) {
					inv_vouchertype_id = "6";
				} else if (vouchertype_id.equals("24")) {
					inv_vouchertype_id = "21";
				}
				bin_id = CNumeric(PadQuotes(request.getParameter("bin_id")));
				item_itemgroup_id = PadQuotes(request.getParameter("itemgroup_id"));
				item_serial_id = CNumeric(PadQuotes(request
						.getParameter("item_serial_id")));
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
				} else if (!q.equals("") && !rateclass_id.equals("0")
						&& config_inventory_current_stock.equals("1")
						&& !location_id.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && config_inventory_current_stock.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && rateclass_id.equals("0")) {
					StrHTML = "<font color=\"red\"><b>Select Rate Class!</b></font>";
				} else if (search_name.equals("yes") && rateclass_id.equals("0")) {
					StrHTML = SearchName();
				} else if (bin_location.equals("yes")) {
					// StrHTML = new Inventory_Bin_Update()
					// .PopulateLocation(branch_id);
				} else if (location_bin.equals("yes")) {
					// StrHTML = new Inventory_Bin_Update().PopulateBin("0", bin_id,
					// location_id, "1");
				} else if (branch_exe.equals("yes")) {
					// StrHTML = PopulateExecutives(emp_id);
				}

				if (!voucher_contact_id.equals("0") && id.equals("")) {
					StringBuilder Str = new StringBuilder();
					try {
						StrSql = "SELECT customer_address, customer_phone1, customer_mobile1, customer_branch_id,"
								+ " customer_city_id, city_name, country_name, state_name, customer_pin, customer_id,"
								+ " CONCAT(branch_name, ' (', branch_code, ')') branchname, branch_id, rateclass_id,"
								+ " CONCAT(customer_name, ' (', customer_id, ')') custname, customer_id,"
								+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname"
								+ " FROM  " + compdb(comp_id) + "axela_customer"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " INNER JOIN  " + compdb(comp_id) + "axela_country ON country_id = state_country_id"
								+ " WHERE contact_id = " + voucher_contact_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							Str.append("<input type=\"hidden\" name=\"txt_cust\" id=\"txt_cust\"");
							Str.append(" value=\"").append(
									crs.getString("customer_address"));
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
							Str.append("[&%]").append(
									crs.getString("rateclass_id"));
							Str.append("\">");
						}
						crs.close();
						StrHTML = Str.toString();
					} catch (Exception ex) {
						SOPError("Axelaauto===" + this.getClass().getName());
						SOPError("Error in "
								+ new Exception().getStackTrace()[0]
										.getMethodName() + ": " + ex);
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
				+ " contact_id, customer_id, customer_name, customer_rateclass_id"
				+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where concat(contact_fname,' ',contact_lname) like '%"
				+ contact_fname
				+ contact_lname
				+ "%'"
				+ " order by contact_fname" + " limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);

			if (crs1.isBeforeFirst()
					&& !(contact_fname + contact_lname).equals("")) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name"))
							.append("");
					Str.append(" [<b><a href=\"javascript:SelectContact(")
							.append(crs1.getString("contact_id")).append(",'");
					Str.append(crs1.getString("contact_name")).append("',");
					Str.append(crs1.getString("customer_id")).append(",");
					Str.append(crs1.getString("customer_rateclass_id"))
							.append(",'");
					Str.append(crs1.getString("customer_name")).append(
							"');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id,
						cart_session_id,
						vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	// ///////// Search Contacts for mobile
	public String SearchMobile() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_mobile1,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_mobile1 like '%" + contact_mobile1 + "%') "
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name"))
							.append("");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append(", ")
								.append(crs1.getString("contact_mobile1"))
								.append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(")
							.append(crs1.getString("contact_id"));
					Str.append("," + "'").append(crs1.getString("contact_name"))
							.append("',");
					Str.append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append(
							"');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id,
						cart_session_id,
						vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";

		}
	}

	// ///////// Search Contacts for phone1

	public String SearchPhone() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_phone1, contact_id, customer_id, customer_name,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name"
				+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_phone1 like '%"
				+ contact_phone1
				+ "%') "
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name"))
							.append("");
					if (!crs1.getString("contact_phone1").equals("")) {
						Str.append(", ")
								.append(crs1.getString("contact_phone1"))
								.append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(")
							.append(crs1.getString("contact_id"));
					Str.append(",'").append(crs1.getString("contact_name"))
							.append("',");
					Str.append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append(
							"');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id,
						cart_session_id,
						vouchertype_id));
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";

		}
	}

	// ///////// Search Contacts for email

	public String SearchEmail() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_email1, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
				+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE (contact_email1 LIKE '%"
				+ contact_email1
				+ "%') "
				+ " ORDER BY contact_fname" + " LIMIT 5";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("").append(crs.getString("contact_name"))
							.append("");
					if (!crs.getString("contact_email1").equals("")) {
						Str.append(", ").append(crs.getString("contact_email1"))
								.append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(")
							.append(crs.getString("contact_id"));
					Str.append(", '").append(crs.getString("contact_name"))
							.append("',");
					Str.append(crs.getString("customer_id")).append(",'");
					Str.append(crs.getString("customer_name")).append(
							"');\">Select</a></b>]<br>");
				}
			} else {
				Str.append(new Invoice_Details().ListCartItems(emp_id,
						cart_session_id,
						vouchertype_id));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
		return Str.toString();
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		String item_id = "0";
		int count = 0;
		String item_name = "";
		String item_code = "";
		String discountpercent = "0.00";
		String tax1 = "0";
		String tax2 = "0";
		String tax3 = "0";
		String boxtype_size = "0", discpercent = "0";
		String tax_rate1 = "0.00", tax_rate2 = "0.00", tax_rate3 = "0.00";
		String tax1_name = "", tax2_name = "", tax3_name = "";
		String tax_amount1 = "0.00", tax_amount2 = "0.00", tax_amount3 = "0.00";
		String disc_amount = "0.00", disc_percent = "0.00";

		String price_sales_customer_id = "0";
		String price_discount1_customer_id = "0";
		String tax_customer_id1 = "0";
		String tax_customer_id2 = "0";
		String tax_customer_id3 = "0";
		String tax_id1 = "0";
		String tax_id2 = "0";
		String tax_id3 = "0";
		try {
			if (CNumeric(item_serial_id).equals("0")
			// || item_serial_id.equals("1")
			) {
				StrSearch = " AND (";
				// + " item_id = '" + q + "'";
				if (Integer.parseInt(item_itemgroup_id) == -1) {
					StrSearch += " item_code LIKE '" + q + "%')";
				} else if (Integer.parseInt(item_itemgroup_id) == -2) {
					StrSearch += " item_name LIKE '" + q + "%')";
				} else if (Integer.parseInt(item_itemgroup_id) == 0) {
					StrSearch += " item_code LIKE '" + q + "%'";
					StrSearch += " OR item_name LIKE '" + q + "%')";
				}
			}
			// IF stock serial present ....Use this code
			// if (item_serial_id.equals("2")) {
			// StrSearch += " AND stockserial_serial_no = '" + q + "'";
			// }
			if (Integer.parseInt(item_itemgroup_id) > 0) {
				StrSearch += " AND item_itemgroup_id = " + item_itemgroup_id;
			}
			StrSql = "SELECT"
					+ " v1.voucher_id,"
					+ " v1.voucher_date,"
					+ " concat("
					+ " vouchertype_prefix,"
					+ " v1.voucher_no,"
					+ " vouchertype_suffix"
					+ " ) AS voucherno,"
					+ " t1.vouchertrans_voucher_id,"
					+ " t1.vouchertrans_multivoucher_id,"
					+ " t1.vouchertrans_customer_id,"
					+ " t1.vouchertrans_item_id,"
					+ " t1.vouchertrans_rowcount,"
					+ " t1.vouchertrans_price,"
					+ " COALESCE(("
					+ " SELECT"
					+ " CONCAT(discountperc.vouchertrans_discount_perc, '-',vouchertrans_customer_id, '-',vouchertrans_amount)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans discountperc"
					+ " WHERE 1 = 1"
					+ " AND discountperc.vouchertrans_voucher_id = t1.vouchertrans_voucher_id"
					+ " AND discountperc.vouchertrans_item_id = t1.vouchertrans_item_id"
					+ " AND discountperc.vouchertrans_option_id = t1.vouchertrans_rowcount"
					+ " AND discountperc.vouchertrans_discount = 1"
					+ " limit 1"
					+ " ),'0-0-0') AS discountpercent,"
					+ " COALESCE(("
					+ " SELECT CONCAT(tax1.vouchertrans_tax_id, '-',REPLACE(customer_name, '%', ''), '-',customer_rate, '-',tax1.vouchertrans_customer_id, '-',tax1.vouchertrans_amount)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans tax1"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = tax1.vouchertrans_tax_id"
					+ " WHERE 1 = 1"
					+ " AND tax1.vouchertrans_item_id = t1.vouchertrans_item_id"
					+ " AND tax1.vouchertrans_voucher_id = t1.vouchertrans_voucher_id"
					+ " AND tax1.vouchertrans_option_id = t1.vouchertrans_rowcount"
					+ " AND tax1.vouchertrans_tax = 1  limit 0,1"
					+ " ),'0-0-0-0-0') AS tax1,"
					+ " COALESCE(("
					+ " SELECT CONCAT(tax2.vouchertrans_tax_id,'-',REPLACE(customer_name, '%', ''), '-',customer_rate,'-',tax2.vouchertrans_customer_id, '-',tax2.vouchertrans_amount)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans tax2"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = tax2.vouchertrans_tax_id"
					+ " WHERE 1 = 1"
					+ " AND tax2.vouchertrans_item_id = t1.vouchertrans_item_id"
					+ " AND tax2.vouchertrans_voucher_id = t1.vouchertrans_voucher_id"
					+ " AND tax2.vouchertrans_option_id = t1.vouchertrans_rowcount"
					+ " AND tax2.vouchertrans_tax = 1  limit 1,1"
					+ " ),'0-0-0-0-0') AS tax2,"
					+ " COALESCE(("
					+ " SELECT CONCAT(tax3.vouchertrans_tax_id,'-',REPLACE(customer_name, '%', ''), '-',customer_rate,'-',tax3.vouchertrans_customer_id, '-',tax3.vouchertrans_amount)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans tax3"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = tax3.vouchertrans_tax_id"
					+ " WHERE 1 = 1"
					+ " AND tax3.vouchertrans_item_id = t1.vouchertrans_item_id"
					+ " AND tax3.vouchertrans_voucher_id = t1.vouchertrans_voucher_id"
					+ " AND tax3.vouchertrans_option_id = t1.vouchertrans_rowcount"
					+ " AND tax3.vouchertrans_tax = 1  limit 2,1"
					+ " ),'0-0-0-0-0') AS tax3,"
					+ " t1.vouchertrans_netprice, t1.vouchertrans_qty,"
					+ " t1.vouchertrans_amount, t1.vouchertrans_alt_qty, t1.vouchertrans_alt_uom_id,"
					+ " item_name, item_code, item_uom_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans t1"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher v1 ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
					+ " WHERE 1 = 1"
					+ " AND vouchertype_id = " + inv_vouchertype_id
					+ " AND v1.voucher_active = '1' "
					+ " AND t1.vouchertrans_item_id != 0"
					+ " AND t1.vouchertrans_discount = 0"
					+ " AND t1.vouchertrans_tax = 0 AND t1.vouchertrans_rowcount != 0 AND t1.vouchertrans_option_id = 0"
					+ StrSearch
					+ " GROUP BY"
					+ " t1.vouchertrans_id"
					+ " ORDER BY"
					+ " v1.voucher_date desc"
					+ " LIMIT 10";
			// SOP("StrSql=search items==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// if (item_serial_id.equals("2")) {
			// if (crs.isBeforeFirst()) {
			// crs.first();
			// item_id = crs.getString("item_id");
			// StrHTML = new Invoice_Details().getItemDetails(location_id,
			// crs.getString("item_id"), rateclass_id, emp_id,
			// cart_session_id,
			// mode);
			// crs.beforeFirst();
			// } else {
			// StrHTML = "<b><font color=\"#ff0000\">No Items Found!</font></b>";
			// }
			// } else {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Date</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Inv. ID.</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Code</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item Name</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Qty</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Price</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Disc.</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Tax</th>\n");
				Str.append("</tr>\n</thead>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					count++;
					item_name = crs.getString("item_name");
					item_code = crs.getString("item_code");
					discountpercent = crs.getString("discountpercent");
					// SOP("discountpercent===" + discountpercent);
					tax1 = crs.getString("tax1");
					tax2 = crs.getString("tax2");
					tax3 = crs.getString("tax3");
					// SOP("tax1===" + tax1);
					if (!tax1.equals("")) {
						tax_id1 = tax1.split("-")[0];
						tax1_name = tax1.split("-")[1];
						// SOP("tax1_name===" + tax1_name);
						tax_rate1 = tax1.split("-")[2];
						tax_customer_id1 = tax1.split("-")[3];
						tax_amount1 = tax1.split("-")[4];
					}
					if (!tax2.equals("")) {
						tax_id2 = tax2.split("-")[0];
						tax2_name = tax2.split("-")[1];
						tax_rate2 = tax2.split("-")[2];
						tax_customer_id2 = tax2.split("-")[3];
						tax_amount2 = tax2.split("-")[4];

					}
					if (!tax3.equals("")) {
						tax_id3 = tax3.split("-")[0];
						tax3_name = tax3.split("-")[1];
						tax_rate3 = tax3.split("-")[2];
						tax_customer_id3 = tax3.split("-")[3];
						tax_amount3 = tax3.split("-")[4];
					}
					if (!discountpercent.equals("")) {
						disc_percent = discountpercent.split("-")[0];
						price_discount1_customer_id = discountpercent.split("-")[1];
						disc_amount = discountpercent.split("-")[2];
					}
					// if (!crs.getString("item_code").equals("")) {
					// item_name = item_name + " ("
					// + item_code + ")";
					// }

					Str.append("\n<tr valign=top"
							+ " onClick=\"ItemDetails(");
					Str.append(crs.getString("voucher_id")).append(",");
					Str.append(crs.getString("vouchertrans_item_id")).append(",");
					Str.append("'" + item_name + "'").append(",");
					Str.append("'" + item_code + "'").append(",");
					Str.append(crs.getString("item_uom_id")).append(",");
					Str.append(crs.getString("vouchertrans_alt_uom_id")).append(",");
					Str.append(crs.getString("vouchertrans_alt_qty")).append(",");
					Str.append(crs.getString("vouchertrans_qty")).append(",");
					Str.append(crs.getString("vouchertrans_price")).append(",");
					Str.append(crs.getString("vouchertrans_amount")).append(",");// Main item Amount(i.e qty * price) without tax disc
					Str.append("" + disc_percent).append(",");
					Str.append("" + price_discount1_customer_id).append(",");
					Str.append("" + disc_amount).append(",");
					Str.append("" + tax_id1).append(",");
					Str.append("" + tax_id2).append(",");
					Str.append("" + tax_id3).append(",");
					Str.append("'" + tax1_name).append("',");
					Str.append("'" + tax2_name).append("',");
					Str.append("'" + tax3_name).append("',");
					Str.append("'" + tax_rate1).append("',");
					Str.append("'" + tax_rate2).append("',");
					Str.append("'" + tax_rate3).append("',");
					Str.append("" + tax_customer_id1).append(",");
					Str.append("" + tax_customer_id2).append(",");
					Str.append("" + tax_customer_id3).append(",");
					Str.append(crs.getString("vouchertrans_customer_id")).append(",");
					Str.append("0,");
					Str.append("'add'").append(");"
							+ "\">\n");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(count)
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(strToShortDate(crs.getString("voucher_date")))
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(crs.getString("voucher_id"))
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(item_code)
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">"
							+ "<a href=\"javascript:ItemDetails(");
					Str.append(crs.getString("voucher_id")).append(",");
					Str.append(crs.getString("vouchertrans_item_id")).append(",");
					Str.append("'" + item_name + "'").append(",");
					Str.append("'" + item_code + "'").append(",");
					Str.append(crs.getString("item_uom_id")).append(",");
					Str.append(crs.getString("vouchertrans_alt_uom_id")).append(",");
					Str.append(crs.getString("vouchertrans_alt_qty")).append(",");
					Str.append(crs.getString("vouchertrans_qty")).append(",");
					Str.append(crs.getString("vouchertrans_price")).append(",");
					Str.append(crs.getString("vouchertrans_amount")).append(",");// Main item Amount(i.e qty * price) without tax disc
					Str.append("'" + disc_percent).append("',");
					Str.append("'" + disc_amount).append("',");
					Str.append("" + tax_id1).append(",");
					Str.append("" + tax_id2).append(",");
					Str.append("" + tax_id3).append(",");
					Str.append("'" + tax1_name).append("',");
					Str.append("'" + tax2_name).append("',");
					Str.append("'" + tax3_name).append("',");
					Str.append("" + tax_rate1).append(",");
					Str.append("" + tax_rate2).append(",");
					Str.append("" + tax_rate3).append(",");
					Str.append("" + tax_customer_id1).append(",");
					Str.append("" + tax_customer_id2).append(",");
					Str.append("" + tax_customer_id3).append(",");
					Str.append("'" + crs.getString("vouchertrans_customer_id")).append(",");
					Str.append("0,");
					Str.append("'add'").append(");"
							+ "\">\n");
					Str.append(item_name)
							.append("</a>")
							.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(crs.getString("vouchertrans_alt_qty"))
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">")
							.append(crs.getString("vouchertrans_price"))
							.append("</td>");
					Str.append("<td valign=\"top\" align=\"left\">");
					if (!disc_amount.equals("0")) {
						Str.append(disc_percent + "%" + " \n " + disc_amount + "");
					} else {
						Str.append("");
					}
					Str.append("</td>");
					Str.append("<td valign=\"top\" width=\"15%\" align=\"left\">");
					if (!tax_amount1.equals("0")) {
						Str.append(tax1_name + ":" + tax_amount1 + "");
					} else {
						Str.append("");
					}
					if (!tax_amount2.equals("0")) {
						Str.append("<br/>" + tax2_name + " :" + tax_amount2 + "");
					} else {
						Str.append("");
					}
					if (!tax_amount3.equals("0")) {
						Str.append("<br/>" + tax3_name + " :" + tax_amount3);
					} else {
						Str.append("");
					}
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=\"#ff0000\">No Items Found!</font></b>");
			}
			// }
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		// if (item_serial_id.equals("2")) {
		// return StrHTML;
		// } else {
		return Str.toString();
		// }
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, PropertyVetoException,
			SQLException {
		doGet(request, response);
	}

}
