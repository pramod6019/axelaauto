package axela.sales;
//aJIt 27th April, 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Quote_Email extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "", msg = "";
	public String branch_id = "0";
	public String quote_id = "0";
	public String comp_email_enable = "", attachment = "";
	public String brandconfig_quote_email_enable = "";
	public String brandconfig_quote_email_format = "";
	public String brandconfig_quote_email_sub = "";
	public String branch_quote_email_exe_sub = "";
	public String branch_quote_email_exe_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String quote_contact_id = "0";
	public String contact_name = "";
	public String contact_email1 = "";
	public String enquiry_id = "0", lead_id = "0";
	public String quote_emp_id = "0";
	public String customer_id = "0";
	public String enquiry_item_id = "0";
	public String item_price = "";
	public String item_netprice = "";
	public String item_tax_id = "";
	public String item_tax_rate = "";
	public String item_netdisc = "";
	public String item_tax = "";
	public String item_netamount = "";
	public String item_rowcount = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_quote_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
				if (!enquiry_id.equals("0")) {
					StrSql = "SELECT enquiry_emp_id, lead_id, customer_id,"
							+ " contact_id, enquiry_branch_id, enquiry_item_id,"
							+ " COALESCE(price_amt, 0 ) AS price_amt,"
							+ " COALESCE(price_disc, 0 ) AS price_disc,"
							+ " COALESCE(customer_id, 0) AS tax_id,"
							+ " COALESCE(customer_rate, 0) AS tax_value,"
							+ " contact_lname, contact_fname, title_desc, contact_email1,"
							+ " COALESCE(brandconfig_quote_email_enable, '') AS brandconfig_quote_email_enable,"
							+ " COALESCE(brandconfig_quote_sms_format, '') AS brandconfig_quote_sms_format,"
							+ " COALESCE(brandconfig_quote_sms_enable, '') AS brandconfig_quote_sms_enable,"
							+ " COALESCE(brandconfig_quote_email_sub, '') AS brandconfig_quote_email_sub,"
							+ " COALESCE(brandconfig_quote_email_format, '') AS brandconfig_quote_email_format,"
							+ " config_admin_email, config_email_enable, config_sms_enable,"
							+ " comp_email_enable, comp_sms_enable"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = enquiry_lead_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax1_ledger_id,"
							+ " " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
							+ " WHERE enquiry_id = " + CNumeric(enquiry_id) + ""
							+ " GROUP BY enquiry_id";
					SOP("Quote email==" + StrSql);

					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							quote_emp_id = crs.getString("enquiry_emp_id");
							customer_id = crs.getString("customer_id");
							lead_id = crs.getString("lead_id");
							quote_contact_id = crs.getString("contact_id");
							branch_id = CNumeric(crs.getString("enquiry_branch_id"));
							contact_name = crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname");
							contact_email1 = crs.getString("contact_email1");
							brandconfig_quote_email_enable = crs.getString("brandconfig_quote_email_enable");
							brandconfig_quote_email_format = crs.getString("brandconfig_quote_email_format");
							brandconfig_quote_email_sub = crs.getString("brandconfig_quote_email_sub");
							config_admin_email = crs.getString("config_admin_email");
							config_email_enable = crs.getString("config_email_enable");
							comp_email_enable = crs.getString("comp_email_enable");
							enquiry_item_id = crs.getString("enquiry_item_id");
							item_price = crs.getString("price_amt");
							item_netprice = Double.toString((crs.getDouble("price_amt") * crs.getDouble("tax_value") / 100) + crs.getDouble("price_amt"));
							item_tax_id = crs.getString("tax_id");
							item_tax_rate = crs.getString("tax_value");
							item_netdisc = Integer.toString(crs.getInt("price_disc"));
							item_tax = Double.toString((crs.getDouble("price_amt") - crs.getInt("price_disc")) * crs.getDouble("tax_value") / 100);
							item_netamount = Double.toString(((crs.getDouble("price_amt") - crs.getInt("price_disc")) * crs.getDouble("tax_value") / 100)
									+ (crs.getDouble("price_amt") - crs.getInt("price_disc")));
						}
					} else {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));
					}
					crs.close();
				}

				// Insert the data in quote and quote_item table
				AddItemFields(request);

				conntx.commit();

				String quote_netamt = "", quote_discamt = "";
				String quote_totaltax = "", quote_grandtotal = "", quote_exprice = "";
				// Get the total from Quote-Item table
				StrSql = "SELECT SUM(quoteitem_price) AS quote_netamt, SUM(quoteitem_disc) AS quote_discamt,"
						+ " SUM(quoteitem_tax) AS quote_totaltax, SUM(quoteitem_total) AS quote_grandtotal,"
						+ " (SELECT SUM(quoteitem_total)"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
						+ " WHERE item.quoteitem_option_group_tax = 1"
						+ " AND item.quoteitem_quote_id = " + quote_id + ") AS quote_exprice"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " WHERE quoteitem_quote_id = " + quote_id + "";

				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					quote_netamt = crs.getString("quote_netamt");
					quote_discamt = crs.getString("quote_discamt");
					quote_totaltax = crs.getString("quote_totaltax");
					quote_grandtotal = crs.getString("quote_grandtotal");
					quote_exprice = crs.getString("quote_exprice");
				}

				// For updating the Quote total feilds
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
						+ " SET"
						+ " quote_netamt = " + quote_netamt + ","
						+ " quote_discamt = " + quote_discamt + ","
						+ " quote_exprice = " + quote_exprice + ","
						+ " quote_totaltax = " + quote_totaltax + ","
						+ " quote_grandtotal = " + Math.ceil(Double.parseDouble(quote_grandtotal)) + ""
						+ " WHERE quote_id = " + quote_id + "";
				stmttx.execute(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_stage_id = 4"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				stmttx.execute(StrSql);

				conntx.commit();

				// after adding quote successfully, create the pdf file and send it by email
				if (!quote_id.equals("0")) {
					if (comp_email_enable.equals("1") && config_email_enable.equals("1")
							&& !config_admin_email.equals("") && brandconfig_quote_email_enable.equals("1")
							&& !contact_email1.equals("") && !brandconfig_quote_email_format.equals("")
							&& !brandconfig_quote_email_sub.equals("")) {
						attachment = CachePath(comp_id) + "Quote_" + quote_id + ".pdf";
						new Veh_Quote_Print_PDF().QuoteDetails(comp_id, request, response, quote_id, BranchAccess, ExeAccess, "file");
						SendEmail();
						response.sendRedirect("veh-quote-list.jsp?quote_id=" + quote_id + "&msg=Email sent successfully!");
					}
				} else {
					if (!comp_email_enable.equals("1")) {
						msg = msg + "<br>Email Option is Disabled!";
					}
					if (!config_email_enable.equals("1")) {
						msg = msg + "<br>Email Gateway is Disabled!";
					}
					if (config_admin_email.equals("")) {
						msg = msg + "<br>Admin Email is Blank!";
					}
					if (contact_email1.equals("")) {
						msg = msg + "<br>Contact Email is Blank!";
					}
					if (!brandconfig_quote_email_enable.equals("1")) {
						msg = msg + "<br>Quote Email Option is Disabled!";
					}
					if (!brandconfig_quote_email_format.equals("")) {
						msg = msg + "<br>Quote Email Format is Blank";
					}
					if (!brandconfig_quote_email_sub.equals("")) {
						msg = msg + "<br>Quote Email Subject is Blank";
					}
					response.sendRedirect("../portal/error.jsp?msg=" + msg + "");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void AddItemFields(HttpServletRequest request) throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			Double quoteitem_tax = 0.00, quoteitem_total = 0.00, price_amt = 0.00;
			Double amount = 0.00, before_tax_total = 0.00;
			String formulae = "";
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine eng = mgr.getEngineByName("JavaScript");

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote"
					+ " (quote_branch_id,"
					+ " quote_no,"
					+ " quote_date,"
					+ " quote_customer_id,"
					+ " quote_contact_id,"
					// + " quote_lead_id,"
					+ " quote_enquiry_id,"
					+ " quote_emp_id,"
					+ " quote_active,"
					+ " quote_desc,"
					+ " quote_terms,"
					+ " quote_notes,"
					+ " quote_entry_id,"
					+ " quote_entry_date)"
					+ " VALUES"
					+ " (" + branch_id + ","
					+ " (SELECT COALESCE(MAX(quote.quote_no), 0) + 1 FROM " + compdb(comp_id) + "axela_sales_quote AS quote"
					+ " WHERE quote.quote_branch_id = " + branch_id + "),"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + customer_id + ","
					+ " " + quote_contact_id + ","
					// + " " + CNumeric(lead_id) + ","
					+ " " + enquiry_id + ","
					+ " " + quote_emp_id + ","
					+ " '1',"
					+ " '',"
					+ " '',"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs1 = stmttx.getGeneratedKeys();
			while (rs1.next()) {
				quote_id = rs1.getString(1);
			}
			rs1.close();
			item_rowcount = ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item");
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
					+ " ('" + quote_id + "',"
					+ " '" + item_rowcount + "',"
					+ " '" + enquiry_item_id + "',"
					+ " '0',"
					+ " '',"
					+ " '1',"
					+ " '',"
					+ " '1',"
					+ " '" + item_price + "',"
					+ " '" + item_netdisc + "',"
					+ " '" + item_tax + "',"
					+ " '" + item_tax_id + "',"
					+ " '" + item_tax_rate + "',"
					+ " '" + item_netamount + "')";

			stmttx.addBatch(StrSql);

			StrSql = "SELECT item_id, item_name, group_type, group_name, price_amt, opt.option_group_id,"
					+ " opt.option_qty, item_code, opt.option_id, opt.option_select, item_small_desc, group_id,"
					+ " COALESCE(customer_rate, 0) AS tax_value,"
					+ " COALESCE(customer_id, 0) AS tax_id, item_serial,"
					+ " (SELECT COUNT(DISTINCT optitem.option_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option optitem"
					+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id"
					+ " AND optitem.option_group_id = group_id"
					+ " GROUP BY option_group_id) AS groupitemcount,"
					+ " item_aftertaxcal, price_disc"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option opt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax1_ledger_id"
					+ " WHERE option_itemmaster_id = " + enquiry_item_id + ""
					+ " AND opt.option_select = '1'"
					+ " AND item_aftertaxcal = 0"
					+ " AND group_active = 1"
					+ " AND group_aftertax = 0"
					+ " GROUP BY group_name, group_type, item_id"
					+ " ORDER BY group_rank, group_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					quoteitem_tax = crs.getDouble("option_qty") * ((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax_value") / 100);
					amount = crs.getDouble("option_qty") * ((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax_value") / 100) + crs.getDouble("price_amt")
							- crs.getDouble("price_disc");
					before_tax_total = before_tax_total + amount;

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // to insert the configured items
							+ " (quoteitem_quote_id,"
							+ " quoteitem_rowcount,"
							+ " quoteitem_item_id,"
							+ " quoteitem_option_id,"
							+ " quoteitem_option_group,"
							+ " quoteitem_option_group_tax,"
							+ " quoteitem_qty,"
							+ " quoteitem_price,"
							+ " quoteitem_disc,"
							+ " quoteitem_tax,"
							+ " quoteitem_tax_id,"
							+ " quoteitem_tax_rate,"
							+ " quoteitem_total)"
							+ " VALUES"
							+ " (" + quote_id + ","
							+ " '0',"
							+ " '" + crs.getString("item_id") + "',"
							+ " '" + crs.getString("option_id") + "',"
							+ " '" + crs.getString("group_name") + "',"
							+ " '1',"
							+ " '" + crs.getString("option_qty") + "',"
							+ " '" + crs.getString("price_amt") + "',"
							+ " '" + crs.getString("price_disc") + "',"
							+ " '" + quoteitem_tax + "',"
							+ " '" + crs.getString("tax_id") + "',"
							+ " '" + crs.getString("tax_value") + "',"
							+ " '" + amount + "')";
					stmttx.addBatch(StrSql);
				}
			}
			crs.close();

			StrSql = "SELECT item_id, item_name, group_type, group_name,"
					+ " COALESCE(price_amt, 0) AS price_amt,"
					+ " opt.option_group_id, item_serial,"
					+ " COALESCE(price_disc, 0) AS price_disc,"
					+ " opt.option_qty, item_code, opt.option_id, opt.option_select, item_small_desc, group_id,"
					+ " COALESCE(customer_rate, 0) AS tax_value,"
					+ " COALESCE(customer_id, 0) AS tax_id, group_aftertax,"
					+ " (SELECT COUNT(DISTINCT optitem.option_id)"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option optitem"
					+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id"
					+ " AND optitem.option_group_id = group_id"
					+ " GROUP BY option_group_id) AS groupitemcount,"
					+ " item_aftertaxcal, item_aftertaxcal_formulae"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_option opt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax1_ledger_id"
					+ " WHERE option_itemmaster_id = " + enquiry_item_id + ""
					+ " AND opt.option_select = '1'"
					+ " AND group_aftertax = 1"
					+ " AND group_active = 1"
					+ " GROUP BY group_name, group_type, item_id"
					+ " ORDER BY group_rank, group_name DESC";

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					if (crs1.getString("item_aftertaxcal_formulae").contains("exprice") && crs1.getString("item_aftertaxcal").equals("1")) {
						formulae = crs1.getString("item_aftertaxcal_formulae").replace("exprice", Double.toString(before_tax_total));
						price_amt = ((Double) eng.eval(unescapehtml(formulae)));
						amount = crs1.getDouble("option_qty") * price_amt;
						quoteitem_tax = 0.00;
					} else if (crs1.getString("item_aftertaxcal_formulae").equals("") && crs1.getString("item_aftertaxcal").equals("0")) {
						quoteitem_tax = crs1.getDouble("option_qty") * ((crs1.getDouble("price_amt") - crs1.getDouble("price_disc")) * crs1.getDouble("tax_value") / 100);
						amount = ((crs1.getDouble("price_amt") - crs1.getDouble("price_disc")) * crs1.getDouble("tax_value") / 100) + crs1.getDouble("price_amt")
								- crs1.getDouble("price_disc");
					}
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // to insert the configured items
							+ " (quoteitem_quote_id,"
							+ " quoteitem_rowcount,"
							+ " quoteitem_item_id,"
							+ " quoteitem_option_id,"
							+ " quoteitem_option_group,"
							+ " quoteitem_option_group_tax,"
							+ " quoteitem_qty,"
							+ " quoteitem_price,"
							+ " quoteitem_disc,"
							+ " quoteitem_tax,"
							+ " quoteitem_tax_id,"
							+ " quoteitem_tax_rate,"
							+ " quoteitem_total)"
							+ " VALUES"
							+ " (" + quote_id + ","
							+ " '0',"
							+ " '" + crs1.getString("item_id") + "',"
							+ " '" + crs1.getString("option_id") + "',"
							+ " '" + crs1.getString("group_name") + "',"
							+ " '2',"
							+ " '" + crs1.getString("option_qty") + "',"
							+ " '" + crs1.getString("price_amt") + "',"
							+ " '" + crs1.getString("price_disc") + "',"
							+ " '" + quoteitem_tax + "',"
							+ " '" + crs1.getString("tax_id") + "',"
							+ " '" + crs1.getString("tax_value") + "',"
							+ " '" + amount + "')";
					stmttx.addBatch(StrSql);
				}
			}
			crs1.close();
			stmttx.executeBatch();
			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
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

	protected void SendEmail() throws SQLException {
		String msg, sub;
		msg = (brandconfig_quote_email_format);
		sub = (brandconfig_quote_email_sub);

		sub = "REPLACE('" + sub + "','[QUOTEID]',quote_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[EXENAME]',emp_name)";
		sub = "REPLACE(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "REPLACE(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "REPLACE(" + sub + ", '[BRANCHADDRESS]',branch_add)";

		msg = "REPLACE('" + msg + "','[QUOTEID]',quote_id)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "REPLACE(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "REPLACE(" + msg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		msg = "REPLACE(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "REPLACE(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "REPLACE(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "REPLACE(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "REPLACE(" + msg + ",'[EXENAME]',emp_name)";
		msg = "REPLACE(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "REPLACE(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "REPLACE(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "REPLACE(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "REPLACE(" + msg + ", '[BRANCHADDRESS]',branch_add)";

		try {

			// postMail(contact_email1, config_admin_email, "info@emax.in", config_admin_email, sub, msg, attachment);
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + quote_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + config_admin_email + "',"
					+ " '" + contact_email1 + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON quote_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " WHERE quote_id = " + quote_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_attach1,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
