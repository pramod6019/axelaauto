package axela.invoice;
//aJIt 5th December, 2012

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Invoice_Check extends Connect {

	public String rateclass_id = "";
	public String comp_id = "0";
	public String invoice_branch_id = "";
	public String StrSql = "", session_id = "";
	public String location_id = "";
	public String invoice_contact_id = "";
	public String contact_mobile1 = "";
	public String contact_phone1 = "", id = "";
	public String contact_email1 = "", q = "";
	public String StrHTML = "", StrSearch = "";
	public String config_invoice_quote_updateprice = "";
	public String config_invoice_quote_updatediscount = "";
	public String contact_fname = "", contact_lname = "";
	DecimalFormat deci = new DecimalFormat("#.##");

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);

		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				contact_fname = PadQuotes(request.getParameter("contact_fname"));
				contact_lname = PadQuotes(request.getParameter("contact_lname"));
				if (!contact_lname.equals("")) {
					contact_lname = " " + contact_lname;
				}
				contact_mobile1 = PadQuotes(request.getParameter("contact_mobile1"));
				contact_phone1 = PadQuotes(request.getParameter("contact_phone1"));
				contact_email1 = PadQuotes(request.getParameter("contact_email1"));
				id = PadQuotes(request.getParameter("id"));
				invoice_branch_id = CNumeric(PadQuotes(request.getParameter("invoice_branch_id")));
				invoice_contact_id = CNumeric(PadQuotes(request.getParameter("invoice_contact_id")));
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				session_id = CNumeric(PadQuotes(request.getParameter("session_id")));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");

				// ///////// Search Contacts for mobile
				if (!contact_mobile1.equals("") && !session_id.equals("0")) {
					StrHTML = SearchMobile();
				} // ///////// Search Contacts for phone1
				else if (!contact_phone1.equals("") && !session_id.equals("0")) {
					StrHTML = SearchPhone();
				} // ///////// Search Contacts for email
				else if (!contact_email1.equals("") && !session_id.equals("0")) {
					StrHTML = SearchEmail();
				} else if (!q.equals("") && !rateclass_id.equals("0") && !location_id.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && rateclass_id.equals("0") && !location_id.equals("0")) {
					StrHTML = "Select Counter!";
				} else if (!rateclass_id.equals("0") && location_id.equals("0")) {
					StrHTML = "<font color=\"red\"><b>Select Location!</b></font>";
				} else if (!contact_fname.equals("") && rateclass_id.equals("0")) {
					StrHTML = SearchName();
				} else if (!invoice_branch_id.equals("0")) {
					StrHTML = PopulateInventoryLocation();
				}

				if (!invoice_contact_id.equals("0") && id.equals("")) {
					StringBuilder Str = new StringBuilder();
					try {
						StrSql = "SELECT customer_address, customer_phone1, customer_mobile1, customer_branch_id, "
								+ " customer_city_id, city_name, state_name, customer_pin, "
								+ " concat(branch_name, ' (',branch_code,')') branchname, branch_id, "
								+ " concat(customer_name, ' (',customer_id,')') customername, customer_id, "
								+ " concat(title_desc, ' ',contact_fname,' ', contact_lname) contactname, customer_id, rateclass_id "
								+ " from " + compdb(comp_id) + "axela_customer"
								+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_customer_id = customer_id "
								+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
								+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
								+ " inner join " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	 "
								+ " inner join " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
								+ " inner join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
								+ " where contact_id = " + invoice_contact_id + "";
						// SOP(StrSqlBreaker(StrSql)+" StrSql");
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							Str.append("<input type=hidden name=\"customer\" id=\"customer\"");
							Str.append(" value=\"").append(crs.getString("customer_address"));
							Str.append("[&%]").append(crs.getString("city_name"));
							Str.append("[&%]").append(crs.getString("customer_pin"));
							Str.append("[&%]").append(crs.getString("state_name"));
							Str.append("[&%]").append(crs.getString("customer_id"));
							Str.append("[&%]").append(crs.getString("customername"));
							Str.append("[&%]").append(invoice_contact_id);
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
				StrHTML = "Session Expired!";
			}
		}

	}

	// ///////// Search Contacts for Name
	public String SearchName() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select concat(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where concat(contact_fname, ' ', contact_lname) like '%" + contact_fname + contact_lname + "%'"
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("" + crs1.getString("contact_name") + "");
				Str.append(" [<b><a href=\"javascript:SelectContact(" + crs1.getString("contact_id") + ","
						+ "'" + crs1.getString("contact_name") + "',"
						+ "" + crs1.getString("customer_id") + ","
						+ "'" + crs1.getString("customer_name") + "'"
						+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	// ///////// Search Contacts for mobile
	public String SearchMobile() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_mobile1,"
				+ " concat(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_mobile1 like '%" + contact_mobile1 + "%') "
				+ " order by contact_fname limit 5";
		// SOP("StrSql--"+StrSql);
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("" + crs1.getString("contact_name") + "");
				if (!crs1.getString("contact_mobile1").equals("")) {
					Str.append(", " + crs1.getString("contact_mobile1") + "");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(" + crs1.getString("contact_id") + ","
						+ "'" + crs1.getString("contact_name") + "',"
						+ "" + crs1.getString("customer_id") + ","
						+ "'" + crs1.getString("customer_name") + "'"
						+ ");\">Select</a></b>]<br>");
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
		StrSql = "Select contact_phone1,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_phone1 like '%" + contact_phone1 + "%') "
				+ " order by contact_fname limit 5";
		// SOP("StrSql--"+StrSql);
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("" + crs1.getString("contact_name") + "");
				if (!crs1.getString("contact_phone1").equals("")) {
					Str.append(", " + crs1.getString("contact_phone1") + "");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(" + crs1.getString("contact_id") + ","
						+ "'" + crs1.getString("contact_name") + "',"
						+ "" + crs1.getString("customer_id") + ","
						+ "'" + crs1.getString("customer_name") + "'"
						+ ");\">Select</a></b>]<br>");
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
		StrSql = "Select contact_email1,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_email1 like '%" + contact_email1 + "%') "
				+ " order by contact_fname limit 5";
		// SOP("StrSql--"+StrSql);
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("" + crs1.getString("contact_name") + "");
				if (!crs1.getString("contact_email1").equals("")) {
					Str.append(", " + crs1.getString("contact_email1") + "");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(" + crs1.getString("contact_id") + ","
						+ "'" + crs1.getString("contact_name") + "',"
						+ "" + crs1.getString("customer_id") + ","
						+ "'" + crs1.getString("customer_name") + "'"
						+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateInventoryLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select id=\"dr_location_id\" name=\"dr_location_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select Location</option>\n");
			StrSql = "Select location_id, location_name"
					+ " from " + compdb(comp_id) + "axela_inventory_location"
					+ " where location_branch_id = " + invoice_branch_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append(">");
				Str.append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " and (item_id like '%" + q + "%'"
					+ " or item_name like '%" + q + "%'"
					+ " or item_code like '%" + q + "%'"
					+ " or salescat_name like '%" + q + "%'"
					+ ")";

			StrSql = "SELECT item_id, item_name, item_code, if(coalesce(option_id,0)!=0, item_type_id = 1,"
					+ " item_type_id = 0) as item_type_id, price_amt, price_disc, price_variable,"
					+ " COALESCE(customer_id,0) as tax_id, item_serial, COALESCE(customer_rate,'0') as tax_value,"
					+ " COALESCE(customer_name,'') as tax_name, COALESCE(salescat_name,'') as salescat_name,"
					+ " if(item_nonstock = '0', coalesce(stock_current_qty, 0), '_') as stock_current_qty"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
					+ " left join " + compdb(comp_id) + "axela_customer on customer_id = price_tax_id AND customer_tax = 1"
					+ " left join " + compdb(comp_id) + "axela_inventory_salescat_trans on trans_item_id = item_id"
					+ " left join " + compdb(comp_id) + "axela_inventory_salescat on salescat_id = trans_salescat_id"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_option on option_itemmaster_id = item_id"
					+ " left join " + compdb(comp_id) + "axela_inventory_stock on stock_item_id = item_id"
					+ " and stock_location_id = " + location_id + ""
					+ " WHERE item_active = '1'"
					+ " and item_type_id != '1'"
					+ " and price_rateclass_id	 = " + rateclass_id + ""
					+ " and price_id = (Select price_id from " + compdb(comp_id) + "axela_inventory_item_price"
					+ " where price_item_id = item_id"
					+ " and price_effective_from <= " + ToLongDate(kknow()) + ""
					+ " and price_active = '1'"
					+ " order by price_effective_from desc limit 1)" + StrSearch + ""
					+ " group by item_id"
					+ " order by item_name"
					+ " limit 6";
			// SOP("StrSql--" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th>Price</th>\n");
				Str.append("<th>Discount</th>\n");
				Str.append("<th>Tax</th>\n");
				Str.append("<th>Current Qty</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					Str.append("\n<tr valign=top onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append("'").append(item_name).append("',");
					Str.append("1,");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("price_variable")).append(",");
					Str.append(crs.getString("price_disc")).append(",");
					Str.append(crs.getString("tax_id")).append(",");
					Str.append(crs.getString("tax_value")).append(",");
					Str.append("'").append(crs.getString("tax_name")).append("',");
					Str.append(crs.getString("item_serial")).append(",");
					Str.append("'',");
					Str.append("0,'");
					Str.append(crs.getString("stock_current_qty")).append("',");
					Str.append("'add');\">\n");
					Str.append("<td valign=top align=left>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td valign=top align=left><a href=\"javascript:ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getString("item_type_id")).append(",");
					Str.append("'").append(item_name).append("',");
					Str.append("1,");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("price_amt")).append(",");
					Str.append(crs.getString("price_variable")).append(",");
					Str.append(crs.getString("price_disc")).append(",");
					Str.append(crs.getString("tax_id")).append(",");
					Str.append(crs.getString("tax_value")).append(",");
					Str.append("'").append(crs.getString("tax_name")).append("',");
					Str.append(crs.getString("item_serial")).append(",");
					Str.append("'',");
					Str.append("0,'");
					Str.append(crs.getString("stock_current_qty")).append("',");
					Str.append("'add');\">").append(item_name).append("</a></td>");
					Str.append("<td valign=top align=right>").append(crs.getString("price_amt")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("price_disc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("tax_value")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("stock_current_qty")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=#ff0000>No Items Found!</font></b>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
