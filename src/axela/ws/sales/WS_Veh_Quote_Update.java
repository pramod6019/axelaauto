package axela.ws.sales;
//Annappa 31th march 2015

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.sales.Veh_Quote_Update;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Veh_Quote_Update extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "0";
	public String quote_enquiry_id = "0";
	public String item_id = "0";
	public String msg = "";
	public String quote_emp_id = "0";
	public String emp_uuid = "0";
	public String customer_id = "0";
	public String customer_address = "";
	public String customer_pin = "";
	public String lead_id = "0";
	public String contact_id = "0";
	public String branch_id = "0";
	public String branch_name = "0";
	public String rateclass_id = "0";
	public String quote_id = "0";
	public String vehstock_id = "0";
	public String quote_customer_id = "0";
	public String quote_contact_id = "0";
	public String branch_quote_email_enable = "";
	public String branch_quote_email_format = "";
	public String branch_quote_sms_enable = "";
	public String branch_quote_sms_format = "";
	public String branch_quote_email_sub = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_customer_dupnames = "";
	public String config_sales_quote_refno = "";
	public String emp_quote_priceupdate = "";
	public String emp_quote_discountupdate = "";
	// main item vari
	public String model_id = "0";
	public String item_name = "0";
	public String item_price = "0";
	public String item_netprice = "0";
	public String item_tax_id = "0";
	public String item_tax_rate = "0";
	public String item_netdisc = "0";
	public String item_small_desc = "0";
	public String item_tax = "0";
	public String item_netamount = "0";
	public String item_netamountwod = "0";
	public String quote_date = "";
	public String quotedate = "";
	public String quote_netqty = "";
	public String quote_auth = "";
	public String quote_netamt = "";
	public String quote_grandtotal = "";
	public String quoteitem_total = "";
	public String quoteitem_option_group_tax = "";
	public String quote_exprice = "";
	public String quote_totaltax = "0";
	public String quote_desc = "";
	public String quote_discamt = "";
	public String quote_terms = "";
	public String quote_active = "0";
	public String quote_fin_option1 = "";
	public String quote_fin_option2 = "";
	public String quote_fin_option3 = "";
	public String quote_fin_loan1 = "";
	public String quote_fin_loan2 = "";
	public String quote_fin_loan3 = "";
	public String quote_fin_tenure1 = "";
	public String quote_fin_tenure2 = "";
	public String quote_fin_tenure3 = "";
	public String quote_fin_adv_emi1 = "";
	public String quote_fin_adv_emi2 = "";
	public String quote_fin_adv_emi3 = "";
	public String quote_fin_emi1 = "";
	public String quote_fin_emi2 = "";
	public String quote_fin_emi3 = "";
	public String quote_fin_baloonemi1 = "";
	public String quote_fin_baloonemi2 = "";
	public String quote_fin_baloonemi3 = "";
	public String quote_fin_fee1 = "";
	public String quote_fin_fee2 = "";
	public String quote_fin_fee3 = "";
	public String quote_fin_downpayment1 = "";
	public String quote_fin_downpayment2 = "";
	public String quote_fin_downpayment3 = "";
	public String quote_inscomp_id = "";
	public String quote_notes = "";
	public String quote_entry_id = "0";
	public String quote_entry_date = "";
	public String quoteitem_option_id = "0";
	public String quoteitem_tax_rate = "";
	public String quoteitem_tax_id = "";
	public String quoteitem_option_group = "";
	public String quoteitem_item_id = "";
	public String quoteitem_price = "";
	public String quoteitem_disc = "";
	public String quoteitem_qty = "";
	public String quoteitem_rowcount = "";
	public String quoteitemrowcount = "";
	public String quoteitem_tax = "";

	public String add = "";
	public String addB = "";
	public String updateB = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray jarr_config;

	public JSONObject QuoteUpdate(JSONObject input) throws Exception {
		SOP("input = " + input);
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!input.isNull("item_id")) {
				item_id = CNumeric(PadQuotes((String) input.get("item_id")));
			}
			if (!input.isNull("vehstock_id")) {
				vehstock_id = CNumeric(PadQuotes((String) input.get("vehstock_id")));
			}
			if (!input.isNull("add")) {
				add = PadQuotes((String) input.get("add"));
			}
			if (!input.isNull("addB")) {
				addB = PadQuotes((String) input.get("addB"));
			}
			if (!input.isNull("updateB")) {
				updateB = PadQuotes((String) input.get("updateB"));
			}

			if (!enquiry_id.equals("0") && add.equals("yes") || addB.equals("yes")) {
				GetEnquiryDetails();
			}
			// GetValues(input);
			PopulateContactDetails();
			PopulateConfigDetails();
			PopulateExecutives();
			PopulateInsurance();
			PopulateFinOption();
			if (!"yes".equals(addB)) {
				StrSql = "SELECT branch_quote_desc, branch_quote_terms"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_id = " + branch_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					quote_desc = crs.getString("branch_quote_desc");
					quote_terms = crs.getString("branch_quote_terms");
				}
				crs.close();
				quote_date = ToLongDate(kknow());
				quotedate = strToShortDate(quote_date);
				output.put("quotedate", quotedate);
				quote_auth = "0";
				quote_netqty = "0";
				quote_netamt = "0";
				quote_grandtotal = "0";
				quote_totaltax = "0";
				quote_active = "1";
				output.put("quote_active", quote_active);
			}

			if (add.equals("yes") && !addB.equals("yes")) {
				if (!item_id.equals("0")) {

					GetConfigurationDetails(item_id, branch_id, vehstock_id, emp_quote_discountupdate, quote_date);
				}

			} else if (add.equals("yes") && addB.equals("yes")) {
				GetValues(input);

				SOP("emp_id==============" + emp_id);
				Veh_Quote_Update addquote = new Veh_Quote_Update();
				addquote.emp_id = emp_id;
				addquote.comp_id = comp_id;
				addquote.branch_id = branch_id;
				addquote.quotedate = quote_date;
				addquote.quote_customer_id = customer_id;
				// addquote.customer_address = customer_address;
				// addquote.customer_pin = customer_pin;
				addquote.quote_contact_id = contact_id;
				addquote.lead_id = lead_id;
				addquote.item_id = item_id;
				addquote.quote_enquiry_id = enquiry_id;
				addquote.quote_grandtotal = quote_grandtotal;
				addquote.quoteitem_total = quoteitem_total;
				addquote.quote_exprice = quote_exprice;
				addquote.quote_totaltax = "quote_totaltax";
				addquote.quote_netamt = quote_netamt;
				addquote.quote_discamt = quote_discamt;
				addquote.quote_fin_adv_emi1 = quote_fin_adv_emi1;
				addquote.quote_fin_adv_emi2 = quote_fin_adv_emi2;
				addquote.quote_fin_adv_emi3 = quote_fin_adv_emi3;
				addquote.quote_fin_emi1 = quote_fin_emi1;
				addquote.quote_fin_emi2 = quote_fin_emi2;
				addquote.quote_fin_emi3 = quote_fin_emi3;
				addquote.quote_fin_option1 = quote_fin_option1;
				addquote.quote_fin_option2 = quote_fin_option2;
				addquote.quote_fin_option3 = quote_fin_option3;
				addquote.quote_fin_loan1 = quote_fin_loan1;
				addquote.quote_fin_loan2 = quote_fin_loan2;
				addquote.quote_fin_loan3 = quote_fin_loan3;
				addquote.quote_fin_tenure1 = quote_fin_tenure1;
				addquote.quote_fin_tenure2 = quote_fin_tenure2;
				addquote.quote_fin_tenure3 = quote_fin_tenure3;
				addquote.quote_fin_fee1 = quote_fin_fee1;
				addquote.quote_fin_fee2 = quote_fin_fee2;
				addquote.quote_fin_fee3 = quote_fin_fee3;
				addquote.quote_fin_downpayment1 = quote_fin_downpayment1;
				addquote.quote_fin_downpayment2 = quote_fin_downpayment2;
				addquote.quote_fin_downpayment3 = quote_fin_downpayment3;
				addquote.quote_inscomp_id = quote_inscomp_id;
				addquote.quote_active = quote_active;
				addquote.quote_emp_id = quote_emp_id;
				addquote.quote_entry_id = emp_id;
				// addquote.quote_entry_by = emp_n
				// addquote.quote_entry_date = quote_date;
				// config details
				addquote.branch_quote_email_enable = branch_quote_email_enable;
				addquote.branch_quote_email_format = branch_quote_email_format;
				addquote.branch_quote_sms_enable = branch_quote_sms_enable;
				addquote.branch_quote_sms_format = branch_quote_sms_format;
				addquote.branch_quote_email_sub = branch_quote_email_sub;
				addquote.config_admin_email = config_admin_email;
				addquote.config_email_enable = config_email_enable;
				addquote.config_sms_enable = config_sms_enable;
				addquote.comp_email_enable = comp_email_enable;
				addquote.comp_sms_enable = comp_sms_enable;
				addquote.config_customer_dupnames = config_customer_dupnames;
				addquote.config_sales_quote_refno = config_sales_quote_refno;
				addquote.emp_quote_priceupdate = emp_quote_priceupdate;
				addquote.emp_quote_discountupdate = emp_quote_discountupdate;
				addquote.android = "yes";
				addquote.quote_entry_date = ToLongDate(kknow());
				addquote.AddFields(null, input);
				msg = addquote.msg;
				if (msg.equals("")) {
					output.put("quote_id", addquote.quote_id);
					output.put("msg", "Quote Added Successfully!");
				} else {
					// SOP("msg======"+msg);
					output.put("msg", msg);
				}
			}

			// crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return output;
	}

	public void GetEnquiryDetails() {
		try {
			StrSql = "SELECT enquiry_emp_id, lead_id, customer_id, customer_name, contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " contact_mobile1, contact_email1, state_name, city_name,"
					+ " customer_address, customer_pin, enquiry_branch_id, rateclass_id, enquiry_custtype_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = enquiry_lead_id"
					+ " WHERE enquiry_id = " + CNumeric(enquiry_id) + ""
					+ " GROUP BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					quote_emp_id = crs.getString("enquiry_emp_id");
					customer_id = crs.getString("customer_id");
					contact_id = crs.getString("contact_id");
					branch_id = crs.getString("enquiry_branch_id");
					customer_address = crs.getString("customer_address");
					customer_pin = crs.getString("customer_pin");
					output.put("branchname", crs.getString("branchname"));
					output.put("quote_emp_id", quote_emp_id);

				}
				list.add(gson.toJson(map));
			}
			map.clear();
			// output.put("listenquirydata", list);
			list.clear();
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected JSONObject PopulateContactDetails() {
		try {
			if (!contact_id.equals("0")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, contact_email1, contact_mobile1, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE contact_id = " + contact_id + "";

				// SOP("contact===========" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						customer_id = crs.getString("customer_id");
						output.put("customer_name", crs.getString("customer_name"));
						output.put("contact_name", crs.getString("title_desc") + "" + crs.getString("contact_fname") + "" + crs.getString("contact_lname"));

					}
					list.add(gson.toJson(map));
				}
				map.clear();
				// output.put("listcontactdata", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	protected JSONObject PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(branch_quote_email_enable, '') AS branch_quote_email_enable,"
					+ " COALESCE(branch_quote_sms_format, '') AS branch_quote_sms_format,"
					+ " COALESCE(branch_quote_sms_enable, '') AS branch_quote_sms_enable,"
					+ " COALESCE(branch_quote_email_sub, '') AS branch_quote_email_sub,"
					+ " COALESCE(branch_quote_email_format, '') AS branch_quote_email_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " config_sales_quote_refno, config_customer_dupnames,"
					+ " comp_email_enable, comp_sms_enable,"
					+ " COALESCE(emp.emp_quote_priceupdate, 0) AS emp_quote_priceupdate,"
					+ " COALESCE(emp.emp_quote_discountupdate, 0) AS emp_quote_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("config====" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_quote_email_enable = crs.getString("branch_quote_email_enable");
					branch_quote_email_format = crs.getString("branch_quote_email_format");
					branch_quote_sms_enable = crs.getString("branch_quote_sms_enable");
					branch_quote_sms_format = crs.getString("branch_quote_sms_format");
					branch_quote_email_sub = crs.getString("branch_quote_email_sub");
					config_admin_email = crs.getString("config_admin_email");
					config_email_enable = crs.getString("config_email_enable");
					config_sms_enable = crs.getString("config_sms_enable");
					comp_email_enable = crs.getString("comp_email_enable");
					comp_sms_enable = crs.getString("comp_sms_enable");
					config_customer_dupnames = crs.getString("config_customer_dupnames");
					config_sales_quote_refno = crs.getString("config_sales_quote_refno");
					emp_quote_priceupdate = crs.getString("emp_quote_priceupdate");
					emp_quote_discountupdate = crs.getString("emp_quote_discountupdate");
					output.put("config_sales_quote_refno", config_sales_quote_refno);
					output.put("emp_quote_priceupdate", emp_quote_priceupdate);
					output.put("emp_quote_discountupdate", emp_quote_discountupdate);
				}
				list.add(gson.toJson(map));
			}
			map.clear();
			// output.put("listconfigdata", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject GetConfigurationDetails(String item_id, String branch_id, String vehstock_id, String emp_quote_discountupdate, String quote_date) {
		double so_total_disc = 0.00;
		double price_amount;
		try {
			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " price_amt, item_small_desc, price_disc, COALESCE(customer_id, 0) AS tax_id,"
					+ " COALESCE(quoteitem_disc, 0) AS quoteitem_disc,"
					+ " COALESCE(quoteitem_id, 0) AS quoteitem_id,"
					+ " COALESCE(customer_rate, 0) AS tax_rate, model_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id"
					+ " AND quoteitem_rowcount != 0"
					+ " AND quoteitem_quote_id = " + quote_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = price_tax_id AND customer_tax = 1"
					+ " WHERE item_id = " + CNumeric(item_id) + ""
					+ " and price_id = (select price_id from " + compdb(comp_id) + "axela_inventory_item_price  where  price_item_id = item_id"
					+ " and price_effective_from <= " + quote_date + " and price_rateclass_id	 = branch_rateclass_id	  and price_active = 1 order by price_effective_from desc limit 1)";
			SOP("main item  StrSql = " + StrSql);
			CachedRowSet crset = processQuery(StrSql, 0);
			if (crset.isBeforeFirst()) {
				while (crset.next()) {
					map.put("model_id", crset.getString("model_id"));
					map.put("item_name", crset.getString("item_name"));
					map.put("item_small_desc", crset.getString("item_small_desc"));
					item_price = crset.getString("price_amt");
					map.put("item_price", item_price);
					price_amount = Double.parseDouble(CNumeric(item_price));
					item_netprice = new BigDecimal((price_amount * crset.getDouble("tax_rate") / 100) + price_amount).toString();
					map.put("item_netprice", item_netprice);
					map.put("item_tax_id", crset.getString("tax_id"));
					map.put("item_tax_rate", crset.getString("tax_rate"));
					if (!crset.getString("quoteitem_id").equals("0")) {
						item_netdisc = crset.getString("quoteitem_disc");
						map.put("item_netdisc", item_netdisc);
					} else {
						item_netdisc = Integer.toString(crset.getInt("price_disc"));
						map.put("item_netdisc", item_netdisc);
					}
					so_total_disc = Double.parseDouble(item_netdisc);
					item_tax = new BigDecimal((price_amount - so_total_disc) * crset.getDouble("tax_rate") / 100).toString();
					map.put("item_tax", item_tax);
					item_netamount = new BigDecimal(((price_amount - so_total_disc) * crset.getDouble("tax_rate") / 100)
							+ (price_amount - so_total_disc)).toString();
					map.put("item_netamount", item_netamount);
					item_netamountwod = new BigDecimal((price_amount * crset.getDouble("tax_rate") / 100)
							+ price_amount).toString();
					map.put("item_netamountwod", item_netamountwod);
				}
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("listmainitem", list);
			list.clear();
			crset.close();

			Double before_tax_totalwod = Double.parseDouble(CNumeric(item_netamountwod));
			Double amount = 0.00, before_tax_total = Double.parseDouble(CNumeric(item_netamount));

			StrSql = "select item_id, item_name, group_type, group_name,\n"
					+ " coalesce(pricetrans_amt, 0) as pricetrans_amt, opt.option_group_id, opt.option_qty, item_code, opt.option_id, opt.option_select, item_small_desc,\n"
					+ " coalesce(customer_rate, 0) as tax_rate,\n"
					+ " coalesce(customer_id, 0) as tax_id, \n"
					+ " (select count(distinct optitem.option_id)\n"
					+ " from "
					+ compdb(comp_id)
					+ "axela_inventory_item_option optitem\n"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item on item_id = optitem.option_item_id\n"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_branch price_branch on price_branch.branch_id = "
					+ branch_id
					+ "\n"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item_price price on price.price_item_id = optitem.option_itemmaster_id and price.price_rateclass_id	 = price_branch.branch_rateclass_id	 and price.price_effective_from <= 20150310180933 and price.price_active = 1\n"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_price_trans on pricetrans_price_id = price_id and pricetrans_item_id = option_item_id\n"
					+ " where optitem.option_itemmaster_id = opt.option_itemmaster_id and optitem.option_group_id = group_id\n"
					+ " group by option_group_id) as groupitemcount,\n"
					+ " coalesce(quoteitem_option_id, 0) as quoteitem_option_id,\n"
					+ " coalesce(quoteitem_option_group, '') as quoteitem_option_group, item_aftertaxcal, item_serial, group_id,\n"
					+ " coalesce(quoteitem_price, '') as quoteitem_price,\n"
					+ " coalesce(quoteitem_qty, '') as quoteitem_qty,\n"
					+ " coalesce(quoteitem_disc, '0') as quoteitem_disc,\n"
					+ " coalesce(quoteitem_tax_rate, '') as quoteitem_tax_rate,\n"
					+ " coalesce(quoteitem_tax_id, '') as quoteitem_tax_id,\n"
					+ " coalesce(quoteitem_total, '') as quoteitem_total, group_aftertax, item_aftertaxcal_formulae\n"
					+ " from " + compdb(comp_id) + "axela_inventory_item_option opt\n"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + "\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_group on group_id = option_group_id\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = option_item_id\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = option_itemmaster_id \n"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_price_trans on pricetrans_price_id = price_id and pricetrans_item_id = option_item_id\n"
					+ " left join " + compdb(comp_id) + "axela_customer on customer_id = price_tax_id AND customer_tax = 1\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id"
					+ " AND quoteitem_quote_id = " + quote_id + ""
					+ " where option_itemmaster_id = " + item_id + " and group_active = 1 and group_aftertax = 0\n"
					+ " and price_id = (select price_id from " + compdb(comp_id) + "axela_inventory_item_price  where  price_item_id = option_itemmaster_id\n"
					+ " and price_effective_from <= " + quote_date + " and price_rateclass_id	 = branch_rateclass_id	  and price_active = 1 order by price_effective_from desc limit 1)\n"
					+ " group by group_name, group_type, item_id\n"
					+ " order by group_rank, group_name desc, item_id";
			// SOP("additional discounts StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Double item_tax_value = 0.00;
					int item_qty = 0, item_disc = 0;
					map.put("item_id", crs.getString("item_id"));
					if (!crs.getString("item_name").equals("")) {
						map.put("item_name", crs.getString("item_name"));
					} else {
						map.put("item_name", "");
					}

					if (!crs.getString("quoteitem_disc").equals("")) {
						item_disc += crs.getInt("quoteitem_disc");
						// SOP("itemdisc=====" + item_disc);
						map.put("item_disc", crs.getInt("quoteitem_disc") + "");
					} else {
						map.put("item_disc", "0");
					}
					//
					if (crs.getString("quoteitem_price").equals("")) {
						SOP("price==111===" + (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt"));
						map.put("sub_item_price", (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt") + "");
					} else {
						map.put("sub_item_price", (crs.getDouble("quoteitem_price") * crs.getDouble("quoteitem_tax_rate") / 100) + crs.getDouble("quoteitem_price") + "");
						SOP("price==111===" + (crs.getDouble("quoteitem_price") * crs.getDouble("quoteitem_tax_rate") / 100) + crs.getDouble("quoteitem_price"));
					}

					if (crs.getString("quoteitem_qty").equals("")) {
						item_qty = crs.getInt("option_qty");
						map.put("item_qty", item_qty + "");
					} else {
						item_qty = crs.getInt("quoteitem_qty");
						map.put("item_qty", item_qty + "");
					}
					map.put("quoteitem_tax_rate", crs.getDouble("tax_rate") + "");// //////cccccccc
					if (crs.getString("quoteitem_tax_rate").equals("")) {
						map.put("item_tax_value", crs.getDouble("tax_rate") + "");
					} else {
						map.put("item_tax_value", crs.getDouble("quoteitem_tax_rate") + "");
					}
					map.put("group_type", crs.getString("group_type"));
					map.put("option_group_id", crs.getString("option_group_id"));

					if (!crs.getString("quoteitem_option_group").equals("")) { // && !updateB.equals("yes")
						map.put("group_name", crs.getString("quoteitem_option_group"));
					} else {
						if (crs.getString("group_type").equals("1")) {
							map.put("group_name", crs.getString("group_name") + "(Default)");
						} else {
							map.put("group_name", crs.getString("group_name"));
						}
					}
					map.put("quoteitem_tax_id", crs.getInt("tax_id") + "");// /////ccccccccccccc
					if (crs.getString("quoteitem_tax_id").equals("")) {
						map.put("sub_item_tax_id", crs.getInt("tax_id") + "");
					} else {
						map.put("sub_item_tax_id", crs.getInt("quoteitem_tax_id") + "");
					}

					if (crs.getString("quoteitem_option_id").equals("")) {
						map.put("option_id", crs.getString("option_id"));
					} else {
						map.put("option_id", crs.getString("quoteitem_option_id"));
					}
					map.put("option_select", crs.getString("option_select"));

					if (!crs.getString("group_name").equals("Additional Discounts") && crs.getString("option_select").equals("1")) {
						if (crs.getString("quoteitem_total").equals("")) {
							amount += item_qty * (((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + (crs.getDouble("pricetrans_amt") - item_disc));
							map.put("amount", item_qty * (((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + (crs.getDouble("pricetrans_amt") - item_disc)) + "");
							map.put("amountwod", item_qty * ((crs.getDouble("pricetrans_amt") * item_tax_value / 100) + crs.getDouble("pricetrans_amt")) + "");
						} else {
							amount += crs.getDouble("quoteitem_total");
							map.put("amount", crs.getDouble("quoteitem_total") + "");
						}
					} else {
						// amount += crs.getDouble("pricetrans_amt");
						map.put("amount", crs.getDouble("pricetrans_amt") + "");
					}

					if (crs.getString("group_name").equals("Additional Discounts")) {
						if (crs.getString("quoteitem_total").equals("")) {
							amount += (double) item_disc;
							map.put("amount", (double) item_disc + "");
						} else {
							amount += (double) item_disc;
							map.put("amount", (double) item_disc + "");
						}
					}
					SOP("amount==========" + amount);
					so_total_disc = so_total_disc + item_disc;
					list.add(gson.toJson(map));
				}
				before_tax_total = amount + before_tax_total;
				map.clear();
				output.put("listaddtionalitems", list);
				list.clear();

				crs.close();
			}
			output.put("beforetaxtotal", before_tax_total + "");

			// map.clear();
			// output.put("listaddtionalitems", list);
			// output.put("beforetaxtotal", before_tax_total + "");
			// list.clear();
			// crs.close();

			//
			amount = 0.00;
			double aftertaxtotal = 0.00;

			//
			StrSql = "select item_id, item_name, group_type, group_name,\n"
					+ " coalesce(pricetrans_amt, 0) as pricetrans_amt, opt.option_group_id, opt.option_qty, item_code, opt.option_id, opt.option_select, item_small_desc,\n"
					+ " coalesce(customer_rate, 0) as tax_rate,\n"
					+ " coalesce(customer_id, 0) as tax_id, \n"
					+ " (select count(distinct optitem.option_id)\n"
					+ " from " + compdb(comp_id) + "axela_inventory_item_option optitem\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = optitem.option_item_id\n"
					+ " inner join " + compdb(comp_id) + "axela_branch price_branch on price_branch.branch_id = " + branch_id + "\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price price on price.price_item_id = optitem.option_itemmaster_id"
					+ " and price.price_rateclass_id = price_branch.branch_rateclass_id"
					+ "	and price.price_effective_from <= 20150310180933 and price.price_active = 1\n"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_price_trans on pricetrans_price_id = price_id and pricetrans_item_id = option_item_id\n"
					+ " where optitem.option_itemmaster_id = opt.option_itemmaster_id and optitem.option_group_id = group_id\n"
					+ " group by option_group_id) as groupitemcount,\n"
					+ " coalesce(quoteitem_option_id, 0) as quoteitem_option_id,\n"
					+ " coalesce(quoteitem_option_group, '') as quoteitem_option_group, item_aftertaxcal, item_serial, group_id,\n"
					+ " coalesce(quoteitem_price, '') as quoteitem_price,\n"
					+ " coalesce(quoteitem_qty, '') as quoteitem_qty,\n"
					+ " coalesce(quoteitem_disc, '0') as quoteitem_disc,\n"
					+ " coalesce(quoteitem_tax_rate, '') as quoteitem_tax_rate,\n"
					+ " coalesce(quoteitem_tax_id, '') as quoteitem_tax_id,\n"
					+ " coalesce(quoteitem_total, '') as quoteitem_total, group_aftertax, item_aftertaxcal_formulae\n"
					+ " from " + compdb(comp_id) + "axela_inventory_item_option opt\n"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + "\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_group on group_id = option_group_id\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = option_item_id\n"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = option_itemmaster_id \n"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_price_trans on pricetrans_price_id = price_id and pricetrans_item_id = option_item_id\n"
					+ " left join " + compdb(comp_id) + "axela_customer on customer_id = price_tax_id AND customer_tax = 1\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id"
					// + " AND quoteitem_rowcount = 0"
					+ " AND quoteitem_quote_id = " + quote_id + ""
					+ " where option_itemmaster_id = " + item_id + " and group_active = 1 and group_aftertax = 1\n"
					+ " and price_id = (select price_id from " + compdb(comp_id) + "axela_inventory_item_price  where  price_item_id = option_itemmaster_id\n"
					+ " and price_effective_from <= " + quote_date + " and price_rateclass_id	 = branch_rateclass_id	  and price_active = 1 order by price_effective_from desc limit 1)\n"
					+ " group by group_name, group_type, item_id\n"
					+ " order by group_rank, group_name desc, item_id";
			SOP("final StrSql = " + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Double price_amt = 0.0;
					Double sub_item_tax_value = 0.00, sub_item_price = 0.00;
					int item_qty = 0, item_disc = 0;

					map.put("sub_item_price", (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt") + "");
					map.put("item_code", crs.getString("item_code") + "");
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", crs.getString("item_name"));
					map.put("item_small_desc", crs.getString("item_small_desc"));
					map.put("sub_item_tax_id", crs.getInt("tax_id") + "");
					map.put("option_id", crs.getString("option_id"));
					map.put("option_group_id", crs.getString("option_group_id"));
					map.put("option_qty", crs.getString("option_qty"));
					map.put("option_select", crs.getString("option_select"));
					map.put("sub_item_tax_value", crs.getDouble("tax_rate") + "");
					// map.put("group_name", crs.getString("group_name"));
					if (crs.getString("group_type").equals("1")) {
						map.put("group_name", crs.getString("group_name") + "(Default)");
					} else {
						map.put("group_name", crs.getString("group_name") + "");
					}
					map.put("group_type", crs.getInt("group_type") + "");
					map.put("pricetrans_amt", crs.getString("pricetrans_amt"));
					map.put("tax_value", crs.getString("tax_rate"));
					map.put("tax_id", crs.getString("tax_id"));
					map.put("groupitemcount", crs.getString("groupitemcount"));
					map.put("quoteitem_option_id", crs.getString("quoteitem_option_id"));
					map.put("quoteitem_option_group", crs.getString("quoteitem_option_group"));
					map.put("item_aftertaxcal", crs.getString("item_aftertaxcal"));
					map.put("item_serial", crs.getString("item_serial"));
					map.put("group_id", crs.getString("group_id"));
					map.put("quoteitem_price", crs.getString("quoteitem_price"));
					map.put("quoteitem_qty", crs.getString("quoteitem_qty"));
					map.put("quoteitem_disc", crs.getString("quoteitem_disc"));
					map.put("quoteitem_tax_rate", crs.getString("tax_rate"));// ////////cccccc
					map.put("quoteitem_tax_id", crs.getString("tax_id"));// ///////cccccccc
					map.put("quoteitem_total", crs.getString("quoteitem_total"));
					map.put("group_aftertax", crs.getString("group_aftertax"));
					map.put("item_aftertaxcal_formulae", crs.getString("item_aftertaxcal_formulae"));
					map.put("amount", item_qty * (((sub_item_price - item_disc) * sub_item_tax_value / 100) + (sub_item_price - item_disc)) + "");

					if (crs.getString("item_aftertaxcal_formulae").contains("expricewd") && crs.getString("item_aftertaxcal").equals("1")) {
						map.put("formulae", crs.getString("item_aftertaxcal_formulae").replace("expricewd", Double.toString(before_tax_total)));
						map.put("amount", (item_qty * (price_amt - item_disc)) + "");
						map.put("base_price", price_amt + "");
					} else if (crs.getString("item_aftertaxcal_formulae").contains("expricewod") && crs.getString("item_aftertaxcal").equals("1")) {
						map.put("formulae", crs.getString("item_aftertaxcal_formulae").replace("expricewod", Double.toString(before_tax_totalwod)));
						map.put("amount", (item_qty * (price_amt - item_disc)) + "");
						map.put("base_price", price_amt + "");
					} else if (crs.getString("item_aftertaxcal_formulae").equals("") && crs.getString("item_aftertaxcal").equals("0")) {
						map.put("price_amt", sub_item_price + "");
						map.put("amount", (((crs.getDouble("pricetrans_amt") - item_disc) * sub_item_tax_value / 100) + crs.getDouble("pricetrans_amt") - item_disc) + "");
						map.put("base_price", crs.getDouble("pricetrans_amt") + "");
					} else {
						map.put("price_amt", sub_item_price + "");
						map.put("amount", (((crs.getDouble("pricetrans_amt") - item_disc) * sub_item_tax_value / 100) + crs.getDouble("pricetrans_amt") - item_disc) + "");
						map.put("base_price", crs.getDouble("pricetrans_amt") + "");
					}

					if (crs.getString("option_select").equals("1")) {
						amount += (((crs.getDouble("pricetrans_amt") - item_disc) * sub_item_tax_value / 100) + crs.getDouble("pricetrans_amt") - item_disc);
					}

					if (!crs.getString("quoteitem_disc").equals("") && crs.getString("option_select").equals("1")) {
						item_disc += crs.getInt("quoteitem_disc");
						map.put("totaldisc", item_disc + "");
					}
					aftertaxtotal = amount + before_tax_total;
					so_total_disc += item_disc;
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("listconfigureditems", list);
				list.clear();
				output.put("totaldisc", so_total_disc);
				output.put("aftertaxtotal", aftertaxtotal);
				crs.close();
			}
			SOP("output = " + output);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	// update.quote_fin_option1 = quote_fin_option1;

	public JSONObject PopulateExecutives() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateInsurance() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT inscomp_id, inscomp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE 1=1 "
					+ " AND inscomp_active = 1"
					// + " AND inscomp_value != ''"
					+ " GROUP BY inscomp_id"
					+ " ORDER BY inscomp_name";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("inscomp_id", "0");
				map.put("inscomp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("inscomp_id", crs.getString("inscomp_id"));
					map.put("inscomp_name", crs.getString("inscomp_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("inscomp_id", "0");
				map.put("inscomp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateinsurcompany", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	protected void GetValues(JSONObject input) {
		try {
			if (!input.isNull("branch_id")) {
				branch_id = CNumeric(PadQuotes((String) input.get("branch_id")));
			}
			if (!input.isNull("contact_id")) {
				contact_id = CNumeric(PadQuotes((String) input.get("contact_id")));
			}
			if (!input.isNull("quote_entry_date")) {
				quote_date = PadQuotes((String) input.get("quote_entry_date"));
			}
			if (!input.isNull("customer_id")) {
				customer_id = CNumeric(PadQuotes((String) input.get("customer_id")));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!input.isNull("lead_id")) {
				lead_id = CNumeric(PadQuotes((String) input.get("lead_id")));
			}
			if (!input.isNull("item_id")) {
				item_id = CNumeric(PadQuotes((String) input.get("item_id")));
			}
			if (!input.isNull("item_netamount")) {
				item_netamount = CNumeric(PadQuotes((String) input.get("item_netamount")));
			}
			if (!input.isNull("item_netdisc")) {
				item_netdisc = CNumeric(PadQuotes((String) input.get("item_netdisc")));
			}
			if (!input.isNull("item_netdisc")) {
				item_netdisc = CNumeric(PadQuotes((String) input.get("item_netdisc")));
			}
			if (!input.isNull("item_tax")) {
				item_tax = CNumeric(PadQuotes((String) input.get("item_tax")));
			}
			if (!input.isNull("quote_fin_option1")) {
				quote_fin_option1 = PadQuotes((String) input.get("quote_fin_option1"));
				if (quote_fin_option1.equals("0")) {
					quote_fin_option1 = "";
				}
			}
			if (!input.isNull("quote_fin_option2")) {
				quote_fin_option2 = PadQuotes((String) input.get("quote_fin_option2"));
				if (quote_fin_option2.equals("0")) {
					quote_fin_option2 = "";
				}
			}
			if (!input.isNull("quote_fin_option3")) {
				quote_fin_option3 = PadQuotes((String) input.get("quote_fin_option3"));
				if (quote_fin_option3.equals("0")) {
					quote_fin_option3 = "";
				}
			}
			if (!input.isNull("quote_fin_tenure1")) {
				quote_fin_tenure1 = PadQuotes((String) input.get("quote_fin_tenure1"));
			}
			if (!input.isNull("quote_fin_tenure2")) {
				quote_fin_tenure2 = PadQuotes((String) input.get("quote_fin_tenure2"));
			}
			if (!input.isNull("quote_fin_tenure3")) {
				quote_fin_tenure3 = PadQuotes((String) input.get("quote_fin_tenure3"));
			}
			if (!input.isNull("quote_fin_adv_emi1")) {
				quote_fin_adv_emi1 = PadQuotes((String) input.get("quote_fin_adv_emi1"));
			}
			if (!input.isNull("quote_fin_adv_emi2")) {
				quote_fin_adv_emi2 = PadQuotes((String) input.get("quote_fin_adv_emi2"));
			}
			if (!input.isNull("quote_fin_adv_emi3")) {
				quote_fin_adv_emi3 = PadQuotes((String) input.get("quote_fin_adv_emi3"));
			}
			if (!input.isNull("quote_fin_loan1")) {
				quote_fin_loan1 = PadQuotes((String) input.get("quote_fin_loan1"));
			}
			if (!input.isNull("quote_fin_loan2")) {
				quote_fin_loan2 = PadQuotes((String) input.get("quote_fin_loan2"));
			}
			if (!input.isNull("quote_fin_loan3")) {
				quote_fin_loan3 = PadQuotes((String) input.get("quote_fin_loan3"));
			}
			if (!input.isNull("quote_fin_emi1")) {
				quote_fin_emi1 = PadQuotes((String) input.get("quote_fin_emi1"));
			}
			if (!input.isNull("quote_fin_emi2")) {
				quote_fin_emi2 = PadQuotes((String) input.get("quote_fin_emi2"));
			}
			if (!input.isNull("quote_fin_emi3")) {
				quote_fin_emi3 = PadQuotes((String) input.get("quote_fin_emi3"));
			}
			if (!input.isNull("quote_fin_fee1")) {
				quote_fin_fee1 = PadQuotes((String) input.get("quote_fin_fee1"));
			}
			if (!input.isNull("quote_fin_fee2")) {
				quote_fin_fee2 = PadQuotes((String) input.get("quote_fin_fee2"));
			}
			if (!input.isNull("quote_fin_fee3")) {
				quote_fin_fee3 = PadQuotes((String) input.get("quote_fin_fee3"));
			}
			if (!input.isNull("quote_fin_downpayment1")) {
				quote_fin_downpayment1 = PadQuotes((String) input.get("quote_fin_downpayment1"));
			}
			if (!input.isNull("quote_fin_downpayment2")) {
				quote_fin_downpayment2 = PadQuotes((String) input.get("quote_fin_downpayment2"));
			}
			if (!input.isNull("quote_fin_downpayment3")) {
				quote_fin_downpayment3 = PadQuotes((String) input.get("quote_fin_downpayment3"));
			}
			if (!input.isNull("quote_desc")) {
				quote_desc = PadQuotes((String) input.get("quote_desc"));
			}
			if (!input.isNull("quote_terms")) {
				quote_terms = PadQuotes((String) input.get("quote_terms"));
			}
			if (!input.isNull("quote_inscomp_id")) {
				quote_inscomp_id = CNumeric(PadQuotes((String) input.get("quote_inscomp_id")));
			}
			if (!input.isNull("quote_active")) {
				quote_active = CNumeric(PadQuotes((String) input.get("quote_active")));
			}
			if (!input.isNull("quote_grandtotal")) {
				quote_grandtotal = CNumeric(PadQuotes((String) input.get("quote_grandtotal")));
			}
			if (!input.isNull("quoteitem_total")) {
				quoteitem_total = CNumeric(PadQuotes((String) input.get("quoteitem_total")));
			}
			if (!input.isNull("quote_netamt")) {
				quote_netamt = CNumeric(PadQuotes((String) input.get("quote_netamt")));
			}
			if (!input.isNull("quote_discamt")) {
				quote_discamt = CNumeric(PadQuotes((String) input.get("quote_discamt")));
			}
			if (!input.isNull("quote_exprice")) {
				quote_exprice = CNumeric(PadQuotes((String) input.get("quote_exprice")));
			}
			if (!input.isNull("enquiry_id")) {
				quote_enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!input.isNull("quote_emp_id")) {
				quote_emp_id = CNumeric(PadQuotes((String) input.get("quote_emp_id")));
				SOP("qoutempid============" + quote_emp_id);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public JSONObject PopulateFinOption() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT financeoption_id, financeoption_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_financeoption"
					+ " ORDER BY financeoption_name";
			// SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("financeoption_id", "0");
				map.put("financeoption_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("financeoption_id", crs.getString("financeoption_id"));
					map.put("financeoption_name", crs.getString("financeoption_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("financeoption_id", "0");
				map.put("financeoption_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatefinoption", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject ItemDetails(JSONObject input) {
		try {
			if (!input.isNull("mainitemdata")) {
				jarr_config = input.getJSONArray("mainitemdata");
				for (int i = 0; i < jarr_config.length(); i++) {
					SOP("3");
					JSONObject jo = jarr_config.getJSONObject(i);
					quoteitem_option_id = jo.getString("option_id");
					quoteitem_tax_rate = jo.getString("quoteitem_tax_rate");
					quoteitem_tax_id = jo.getString("quoteitem_tax_id");
					quoteitem_option_group = jo.getString("group_name");
					item_id = jo.getString("item_id");
					item_price = jo.getString("quoteitem_price");
					quoteitem_disc = jo.getString("quoteitem_disc");
					quoteitem_total = jo.getString("quoteitem_total");
					quoteitem_option_group_tax = jo.getString("quoteitem_option_group_tax");
					SOP("quoteitem_option_group_tax==1111===" + quoteitem_option_group_tax);
					quoteitem_qty = "1";// quantity is always 1
					quoteitem_rowcount = ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1"
							+ " FROM " + compdb(comp_id) + "axela_sales_quote_item");
					quoteitemrowcount = quoteitem_rowcount;
					// quoteitem_option_id = quoteitem_rowcount;
					quoteitem_tax = Double
							.toString((Double.parseDouble(item_price) - Double
									.parseDouble(quoteitem_disc))
									* Double.parseDouble(quoteitem_tax_rate) / 100);
					AddItemFields();
				}
			}
			if (!input.isNull("listadditionalitems")) {
				jarr_config = input.getJSONArray("listadditionalitems");
				for (int i = 0; i < jarr_config.length(); i++) {
					JSONObject jo = jarr_config.getJSONObject(i);
					// quoteitem_option_id = jo.getString("option_id");
					quoteitem_tax_rate = jo.getString("quoteitem_tax_rate");
					quoteitem_tax_id = jo.getString("quoteitem_tax_id");
					quoteitem_option_group = jo.getString("group_name");
					item_id = jo.getString("item_id");
					item_price = jo.getString("quoteitem_price");
					quoteitem_disc = jo.getString("quoteitem_disc");
					SOP("quoteitem_disc========" + jo.getString("quoteitem_disc"));
					quoteitem_total = jo.getString("quoteitem_total");
					quoteitem_total = (Double.parseDouble(quoteitem_total) - Double.parseDouble(quoteitem_disc)) + "";
					SOP("quoteitem_disc========" + jo.getString("quoteitem_disc"));
					quoteitem_option_group_tax = jo.getString("quoteitem_option_group_tax");
					quoteitem_qty = "1";// quantity is always 1
					// quoteitem_rowcount = ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1"
					// + " FROM " + compdb(comp_id) + "axela_sales_quote_item");
					quoteitem_tax = Double
							.toString((Double.parseDouble(item_price) - Double
									.parseDouble(quoteitem_disc))
									* Double.parseDouble(quoteitem_tax_rate) / 100);
					quoteitem_option_id = quoteitemrowcount;
					quoteitem_rowcount = "0";
					AddItemFields();
				}
			}
			if (!input.isNull("listconfigureditems")) {
				jarr_config = input.getJSONArray("listconfigureditems");
				for (int i = 0; i < jarr_config.length(); i++) {
					JSONObject jo = jarr_config.getJSONObject(i);
					// quoteitem_option_id = jo.getString("option_id");
					quoteitem_tax_rate = jo.getString("quoteitem_tax_rate");
					quoteitem_tax_id = jo.getString("quoteitem_tax_id");
					quoteitem_option_group = jo.getString("group_name");
					item_id = jo.getString("item_id");
					item_price = jo.getString("quoteitem_price");
					quoteitem_disc = jo.getString("quoteitem_disc");
					quoteitem_total = jo.getString("quoteitem_total");
					quoteitem_option_group_tax = jo.getString("quoteitem_option_group_tax");
					quoteitem_qty = "1";// quantity is always 1
					quoteitem_tax = Double
							.toString((Double.parseDouble(item_price) - Double
									.parseDouble(quoteitem_disc))
									* Double.parseDouble(quoteitem_tax_rate) / 100);
					quoteitem_option_id = quoteitemrowcount;
					quoteitem_rowcount = "0";
					AddItemFields();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public void AddItemFields() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // to insert quote item table
					+ " (quoteitem_quote_id,"
					+ " quoteitem_rowcount,"
					+ " quoteitem_item_id,"
					+ " quoteitem_option_id,"
					+ " quoteitem_option_group,"
					+ " quoteitem_option_group_tax,"
					+ " quoteitem_item_serial,"
					+ " quoteitem_qty,"
					+ " quoteitem_price,"
					+ " quoteitem_disc,"
					+ " quoteitem_tax,"
					+ " quoteitem_tax_id,"
					+ " quoteitem_tax_rate,"
					+ " quoteitem_total)"
					+ " VALUES"
					+ " ('"
					+ quote_id
					+ "',"
					+ " '"
					+ quoteitem_rowcount
					+ "',"
					+ " '"
					+ item_id
					+ "',"
					+ " '"
					+ quoteitem_option_id
					+ "',"
					+ " '"
					+ quoteitem_option_group
					+ "',"
					+ " '"
					+ quoteitem_option_group_tax
					+ "',"
					+ " '',"
					+ " '1',"
					+ " '"
					+ item_price
					+ "',"
					+ " '"
					+ quoteitem_disc
					+ "',"
					+ " '"
					+ quoteitem_tax
					+ "',"
					+ " '"
					+ quoteitem_tax_id
					+ "',"
					+ " '"
					+ quoteitem_tax_rate + "'," + " '" + quoteitem_total + "')";
			updateQuery(StrSql);
			// SOP("strsql========="+StrSqlBreaker(StrSql));
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

}
