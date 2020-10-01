package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote_Check extends Connect {

	public String cat_id = "0";
	public String model_id = "0", item_id = "0", enquiry_id = "0";
	public String branch_id = "0";
	public String result = "";
	public String comp_id = "0";
	public String id = "";
	public String StrSql = "";
	public String msg = "";
	public String prefix = "";
	public String rateclass_id = "";
	public String GrandAmount = "";
	public String fromDate = "";
	public String contact_mobile1 = "";
	public String contact_phone1 = "";
	public String contact_email1 = "";
	public String months = "";
	public String maxrows = "";
	public String balance_count = "";
	public String quote_contact_id = "0";
	public String date = "", q = "";
	public String StrHTML = "", StrSearch = "";
	public String service_tax = "";
	public String status = "";
	public String branch_emp = "";
	public String config_sales_quote_updateprice = "";
	public String config_sales_quote_updatediscount = "";
	public String vat = "";
	public String contact_fname = "", contact_lname = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	public String list_model_item = "", list_quote_item = "";
	public String get_config = "";
	public String quote_date = "";
	public String newItemDetails = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				id = PadQuotes(request.getParameter("id"));
				list_model_item = PadQuotes(request.getParameter("list_model_item"));
				list_quote_item = PadQuotes(request.getParameter("list_quote_item"));
				get_config = PadQuotes(request.getParameter("get_config"));
				rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
				prefix = PadQuotes(request.getParameter("prefix"));
				status = PadQuotes(request.getParameter("status"));
				branch_emp = PadQuotes(request.getParameter("branch_emp"));
				GrandAmount = PadQuotes(request.getParameter("GrandAmount"));
				contact_fname = PadQuotes(request.getParameter("contact_fname"));
				contact_lname = PadQuotes(request.getParameter("contact_lname"));
				quote_date = PadQuotes(request.getParameter("quote_date"));
				// SOP("quote_date==55=check==" + quote_date);
				newItemDetails = PadQuotes(request.getParameter("newItemDetails"));
				if (!contact_lname.equals("")) {
					contact_lname = " " + contact_lname;
				}
				contact_mobile1 = PadQuotes(request.getParameter("contact_mobile1"));
				contact_phone1 = PadQuotes(request.getParameter("contact_phone1"));
				contact_email1 = PadQuotes(request.getParameter("contact_email1"));
				fromDate = PadQuotes(request.getParameter("fromDate"));
				months = PadQuotes(request.getParameter("months"));
				balance_count = PadQuotes(request.getParameter("balance_count"));
				quote_contact_id = CNumeric(PadQuotes(request.getParameter("quote_contact_id")));
				cat_id = cat_id.replaceAll("nbsp", "&");
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				// ///////// Search Contacts for mobile
				if (!contact_mobile1.equals("")) {
					StrHTML = SearchMobile();
				} // ///////// Search Contacts for phone1
				else if (!contact_phone1.equals("")) {
					StrHTML = SearchPhone();
				} // ///////// Search Contacts for email
				else if (!contact_email1.equals("")) {
					StrHTML = SearchEmail();
				} else if (!q.equals("") && !rateclass_id.equals("0")) {
					StrHTML = SearchItems();
				} else if (!q.equals("") && rateclass_id.equals("0")) {
					StrHTML = "Select Counter!";
				} else if (!contact_fname.equals("") && rateclass_id.equals("0")) {
					StrHTML = SearchName();
				} else if (list_model_item.equals("yes")) {// calling from vehicle update Page
					StrHTML = new Veh_Quote_Add().PopulateItem(model_id, comp_id);
				} else if (list_quote_item.equals("yes") && !branch_id.equals("0")) {// calling from quote update
					StrHTML = new Veh_Quote_Update().PopulateItem(model_id, branch_id, status, comp_id);
				} else if (get_config.equals("yes") && !branch_id.equals("0")) {// calling from quote update
					if (quote_date.equals("")) {
						StrHTML = "<font color=\"red\"><b>Enter Quote Date!</b></font>";
					} else if (!isValidDateFormatShort(quote_date)) {
						StrHTML = "<font color=\"red\"><b>Enter valid Quote Date!</b></font>";
					} else {
						// SOP("quote_date==check=" + quote_date);
						StrHTML = new Veh_Quote_Update().GetConfigurationDetails(request, item_id, branch_id, "0", "0", ConvertShortDateToStr(quote_date), enquiry_id, comp_id);
					}
				} else if (branch_emp.equals("yes")) {// calling from quote update
					StrHTML = new Veh_Quote_Update().PopulateExecutive(comp_id, branch_id);
				} else if (!cat_id.equals("0") && !cat_id.equals("")) {
					StrHTML = PopulateProduct();
				} else if (!months.equals("")) {
					fromDate = ConvertShortDateToStr(fromDate);
					date = strToShortDate(fromDate);
					maxrows = "" + (Integer.parseInt(months) + 3);
					if (Integer.parseInt(maxrows) > Integer.parseInt(balance_count)) {
						maxrows = balance_count;
					}
				} else if (!quote_contact_id.equals("0") && id.equals("")) {
					StringBuilder Str = new StringBuilder();
					try {
						StrSql = "SELECT customer_address, customer_phone1, customer_mobile1, customer_branch_id,"
								+ " customer_city_id, city_name, state_name, customer_pin,"
								+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, branch_id,"
								+ " CONCAT(customer_name, ' (', customer_id, ')') AS customername, customer_id,"
								+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
								+ " customer_id, rateclass_id"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_customer_id = customer_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	 "
								+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
								+ " WHERE contact_id = " + quote_contact_id + "";
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
							Str.append("[&%]").append(quote_contact_id);
							Str.append("[&%]").append(crs.getString("contactname"));
							Str.append("[&%]").append(crs.getString("branch_id"));
							Str.append("[&%]").append(crs.getString("branchname"));
							Str.append("[&%]").append(crs.getString("rateclass_id"));
							Str.append("\">");
						}
						crs.close();
						StrHTML = Str.toString();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
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
		StrSql = "Select concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where concat(contact_fname,' ',contact_lname) like '%" + contact_fname + contact_lname + "%'"
				+ " order by contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("").append(crs1.getString("contact_name")).append("");
				Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name")).append("'," + "")
						.append(crs1.getString("customer_id")).append("," + "'").append(crs1.getString("customer_name")).append("'"
								+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateProduct() {
		StringBuilder Str1 = new StringBuilder();
		StringBuilder Str2 = new StringBuilder();
		try {
			String variabletext = "";
			variabletext = "readonly=\"readonly\"";
			int i = 0;
			StrSql = "SELECT salescat_id, salescat_name, price_variable, item_id, item_name,"
					+ " item_code, price_tax, price_vat, price_amt, price_disc"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " inner join " + compdb(comp_id) + "axela_inventory_salescat_trans on trans_item_id = item_id"
					+ " inner join " + compdb(comp_id) + "axela_inventory_salescat on salescat_id=trans_salescat_id and salescat_id=" + cat_id
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id=item_id "
					+ " and price_rateclass_id	 = " + rateclass_id
					+ " where item_active = '1' and price_rateclass_id	 = " + rateclass_id
					+ " and price_id = (  "
					+ " select price_id  "
					+ " from " + compdb(comp_id) + "axela_inventory_item_price  "
					+ " where price_item_id = item_id  and price_effective_from <= " + ToLongDate(kknow()) + "  and price_amt!=0 and price_active='1' "
					+ " order by price_effective_from desc limit 1) "
					+ " group by  item_id "
					+ " order by item_name ";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str2.append("<select name=dr_item").append(id).append(" id=dr_item").append(id).append(" class=selectbox onchange=\"QuoteCheck(this.value,'").append(id).append("','','');\">"
					+ "<option value = -1> Select </option>");
			while (crs.next()) {
				Str1.append("<input name=\"baseprice_").append(crs.getString("item_id")).append("\" id=\"baseprice_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
						.append(crs.getString("price_amt")).append("\">\n");
				Str1.append("<input name=\"fee_").append(crs.getString("item_id")).append("\" id=\"fee_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
						.append(crs.getString("price_amt")).append("").append(variabletext).append("\">\n");
				Str1.append("<input name=\"disc_").append(crs.getString("item_id")).append("\" id=\"disc_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
						.append(crs.getString("price_disc")).append("\">\n");
				Str1.append("<input name=\"variable_").append(crs.getString("item_id")).append("\" id=\"variable_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
						.append(crs.getString("price_variable")).append("\">\n");
				if (crs.getString("price_tax").equals("1")) {
					Str1.append("<input name=\"tax_").append(crs.getString("item_id")).append("\" id=\"tax_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
							.append(service_tax)
							.append("\">\n");
				} else {
					Str1.append("<input name=\"tax_").append(crs.getString("item_id")).append("\" id=\"tax_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"0\">");
				}
				if (crs.getString("price_tax").equals("2")) {
					Str1.append("<input name=\"vat_").append(crs.getString("item_id")).append("\" id=\"vat_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"")
							.append(crs.getString("price_vat")).append("\">\n");
				} else {
					Str1.append("<input name=\"vat_").append(crs.getString("item_id")).append("\" id=\"vat_").append(crs.getString("item_id")).append("\" type=\"hidden\" value=\"0\">");
				}
				Str2.append("<option value=").append(crs.getString("item_id")).append(">").append(crs.getString("item_name")).append("");
				if (!crs.getString("item_code").equals("")) {
					Str2.append(" (").append(crs.getString("item_code")).append(")");
				}
				if (!crs.getString("salescat_name").equals("")) {
					Str2.append(" [").append(crs.getString("salescat_name")).append("]");
				}
				Str2.append("</option>\n");
			}
			Str2.append("</select>");
			Str1.append(Str2.toString());
			crs.close();
			return Str1.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		// SOP("StrSql--"+StrSql);
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("").append(crs1.getString("contact_name")).append("");
				if (!crs1.getString("contact_mobile1").equals("")) {
					Str.append(", ").append(crs1.getString("contact_mobile1")).append("");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name")).append("'," + "")
						.append(crs1.getString("customer_id")).append("," + "'").append(crs1.getString("customer_name")).append("'"
								+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
				Str.append("").append(crs1.getString("contact_name")).append("");
				if (!crs1.getString("contact_phone1").equals("")) {
					Str.append(", ").append(crs1.getString("contact_phone1")).append("");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name")).append("'," + "")
						.append(crs1.getString("customer_id")).append("," + "'").append(crs1.getString("customer_name")).append("'"
								+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
				Str.append("").append(crs1.getString("contact_name")).append("");
				if (!crs1.getString("contact_email1").equals("")) {
					Str.append(", ").append(crs1.getString("contact_email1")).append("");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name")).append("'," + "")
						.append(crs1.getString("customer_id")).append("," + "'").append(crs1.getString("customer_name")).append("'"
								+ ");\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " and (item_id like '%" + q + "%'"
					+ " or item_name like '%" + q + "%'"
					+ " or item_code like '%" + q + "%'"
					+ " or salescat_name like '%" + q + "%')";

			StrSql = "SELECT item_id, item_name, item_code,"
					+ " price_amt, price_disc, price_variable,"
					+ " COALESCE(customer_id,0) as customer_id, item_serial,"
					+ " COALESCE(customer_rate,'0') as tax_value,"
					+ " COALESCE(customer_name,'') as tax_name,"
					+ " COALESCE(salescat_name,'') as salescat_name,"
					+ " if(coalesce(option_id,0)!=0, item_type_id = 1, item_type_id = 0) as item_type_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id "
					+ " left join " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax1_ledger_id"
					+ " left join " + compdb(comp_id) + "axela_inventory_salescat_trans on trans_item_id = item_id "
					+ " left join " + compdb(comp_id) + "axela_inventory_salescat on salescat_id = trans_salescat_id "
					+ " left join " + compdb(comp_id) + "axela_inventory_item_option on option_itemmaster_id = item_id"
					+ " WHERE item_active = '1' and price_rateclass_id	 = 1 "
					+ " and price_id = (select price_id from " + compdb(comp_id) + "axela_inventory_item_price  "
					+ " where price_item_id = item_id and price_effective_from <= " + ToLongDate(kknow()) + " and price_active='1' "
					+ " order by price_effective_from desc limit 1) " + StrSearch
					+ " group by item_id "
					+ " order by item_name"
					+ " limit 6";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th>Price</th>\n");
				Str.append("<th>Discount</th>\n");
				Str.append("<th>Tax</th>\n");
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
					Str.append("0,");
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
					Str.append("0,");
					Str.append("'add');\">").append(item_name).append("</a></td>");
					Str.append("<td valign=top align=right>").append(crs.getString("price_amt")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("price_disc")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("tax_value")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=#ff0000>No Items Found!</font></b>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
