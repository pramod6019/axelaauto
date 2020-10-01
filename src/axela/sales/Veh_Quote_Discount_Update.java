// Ved Prakash (16 Feb 2013)
// modified by Sangita , 7th may 2013
package axela.sales;

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

import cloudify.connect.Connect;

public class Veh_Quote_Discount_Update extends Connect {
	public String BranchAccess = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String quotediscount_id = "0";
	public String quotediscount_quote_id = "0";
	public String quotediscount_requestedamount = "";
	public String quotediscount_request_emp_id = "0";
	public String quotediscount_authorize_emp_id = "0";
	public String quotediscount_authorize_emp_name = "";
	public String quotediscount_authorize_time = "";
	public String quotediscount_authorize_status = "0";
	public String quotediscount_entry_id = "0";
	public String quotediscount_entry_date = "";
	public String quotediscount_modified_id = "0";
	public String quotediscount_modified_date = "";

	public String brandconfig_quote_discount_authorize_email_enable = "";
	public String brandconfig_quote_discount_authorize_email_format = "";
	public String brandconfig_quote_discount_authorize_email_sub = "";
	public String brandconfig_quote_discount_authorize_sms_enable = "";
	public String brandconfig_quote_discount_authorize_sms_format = "";
	public String quote_contact_id = "0";
	public String quote_date = "";
	public String quote_grandtotal = "0.0";
	public String so_id = "0";
	public String so_date = "";
	public String so_active = "0";
	public String customer_name = "";
	public String contact_name = "";
	public String branch_email1 = "", branch_name = "", branch_id = "";
	public String emp_mobile1 = "", emp_email1 = "", emp_name_authorize = "";
	public String config_email_enable = "", config_sms_enable = "";
	public String comp_email_enable = "", comp_sms_enable = "";

	public int quote_emp_id = 0;
	public String quote_emp_name = "";
	public String item_name = "";
	public String model_name = "";

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_discount_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				quotediscount_quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				quotediscount_id = CNumeric(PadQuotes(request.getParameter("quotediscount_id")));
				PopulateExecutives();
				StrSql = " SELECT COUNT(so_id) FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_active = 1"
						+ " AND so_quote_id = " + quotediscount_quote_id;
				if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					response.sendRedirect("../portal/error.jsp?msg=There is no active Sales Order!");
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				try {
					populateQuoteDetails();
					if ("yes".equals(add)) {
						if (!"yes".equals(addB)) {
						} else {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_discount_add", request).equals("1")) {
								quotediscount_request_emp_id = emp_id;
								quotediscount_entry_id = emp_id;
								quotediscount_entry_date = ToLongDate(kknow());
								AddFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("veh-quote-discount-list.jsp?quote_id=" + quotediscount_quote_id + "&quotediscount_id=" + quotediscount_id
											+ "&msg=Discount Authorization Added Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
					if ("yes".equals(update)) {
						if (!"yes".equals(updateB) && !"Delete Discount".equals(deleteB)) {
							PopulateFields(response);
						} else if ("yes".equals(updateB) && !"Delete Discount".equals(deleteB)) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_discount_edit", request).equals("1")) {
								GetValues(request, response);
								quotediscount_modified_id = emp_id;
								quotediscount_modified_date = ToLongDate(kknow());
								UpdateFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("veh-quote-discount-list.jsp?quote_id=" + quotediscount_quote_id + "&quotediscount_id=" + quotediscount_id
											+ "&msg=Discount Authorization Updated Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else if ("Delete Discount".equals(deleteB)) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_discount_delete", request).equals("1")) {
								GetValues(request, response);
								DeleteFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response
											.encodeRedirectURL("veh-quote-discount-list.jsp?quote_id=" + quotediscount_quote_id + "&msg=Discount Authorization Deleted Successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}

				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("conn is closed.....");
					}
					if (!conntx.isClosed() && conntx != null) {
						SOP("Transaction Error==");
						conntx.rollback();
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
					}
					msg = "<br>Transaction Error!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	private void populateQuoteDetails() {
		StrSql = "SELECT branch_id, branch_name,"
				+ " item_name, model_name,"
				+ " emp_id, emp_name,"
				+ " quote_contact_id, quote_date, quote_grandtotal,"
				+ " so_id, so_date, so_active,"
				+ " CONCAT(title_desc,' ', contact_fname,' ', contact_lname) AS contact_name,"
				+ " customer_id, customer_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_quote_id = quote_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " WHERE quote_id = " + quotediscount_quote_id;

		// SOP("StrSql---PopulateQuoteDetails-------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				quote_emp_id = crs.getInt("emp_id");
				quote_emp_name = crs.getString("emp_name");
				item_name = crs.getString("item_name");
				model_name = crs.getString("model_name");
				branch_id = crs.getString("branch_id");
				branch_name = crs.getString("branch_name");
				quote_contact_id = crs.getString("quote_contact_id");
				quote_date = strToShortDate(crs.getString("quote_date"));
				quote_grandtotal = IndDecimalFormat(df.format(Double.parseDouble(crs.getString("quote_grandtotal"))));
				contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("quote_contact_id") + "\">"
						+ crs.getString("contact_name") + "</a>";
				so_id = crs.getString("so_id");
				if (!crs.getString("so_date").equals("")) {
					so_date = strToShortDate(crs.getString("so_date"));
				}
				so_active = crs.getString("so_active");
				customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
						+ crs.getString("customer_name") + "</a>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// quotediscount_authorize_emp_id = CNumeric(PadQuotes(request.getParameter("dr_quotediscount_authorize_emp_id")));
		quotediscount_requestedamount = CNumeric(PadQuotes(request.getParameter("txt_quotediscount_requestedamount")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (quotediscount_authorize_emp_id.equals("0")) {
			msg += "<br>Authorize Executive is not selected !";
		}
		if (quotediscount_requestedamount.equals("0")) {
			msg += "<br>Enter Amount!";
		}
		// if (!quotediscount_authorize_emp_id.equals("0") && !quotediscount_requestedamount.equals("0")) {
		// StrSql = "SELECT"
		// + " discount_amount"
		// + " FROM " + compdb(comp_id) + "axela_emp"
		// + " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
		// + " INNER JOIN " + compdb(comp_id) + "axela_sales_discount ON discount_jobtitle_id = emp_jobtitle_id"
		// + " WHERE emp_id = " + quotediscount_authorize_emp_id;
		// String discountAmount = ExecuteQuery(StrSql);
		//
		// if (Double.parseDouble(quotediscount_requestedamount) > Double.parseDouble(discountAmount)) {
		// msg += "<br>Requested Discount Amount cannot be greater than " + IndDecimalFormat(discountAmount) + " !";
		// }
		// }
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_discount"
						+ " (quotediscount_quote_id,"
						+ " quotediscount_requestedamount,"
						+ " quotediscount_request_emp_id,"
						+ " quotediscount_authorize_emp_id,"
						+ " quotediscount_entry_id,"
						+ " quotediscount_entry_date)"
						+ " VALUES"
						+ " (" + quotediscount_quote_id + ","
						+ " " + quotediscount_requestedamount + ","
						+ " " + quotediscount_request_emp_id + ","
						+ " " + quotediscount_authorize_emp_id + ","
						+ " " + quotediscount_entry_id + ","
						+ " '" + quotediscount_entry_date + "')";
				// SOP("StrSql-----insert---" + StrSql);
				// stmttx.execute(StrSql);
				// conntx.commit();
				quotediscount_id = UpdateQueryReturnID(StrSql);

				if (!quotediscount_id.equals("0")) {
					PopulateConfigDetails();
					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !branch_email1.equals("")) {
						if (!emp_email1.equals("")
								&& brandconfig_quote_discount_authorize_email_enable.equals("1")
								&& !brandconfig_quote_discount_authorize_email_format.equals("")
								&& !brandconfig_quote_discount_authorize_email_sub.equals("")) {
							SendEmail();
						}
					}

					if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
						if (brandconfig_quote_discount_authorize_sms_enable.equals("1")
								&& !brandconfig_quote_discount_authorize_sms_format.equals("")) {
							if (!emp_mobile1.equals("")) {
								SendSMS();
							}
						}
					}
				}

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT quotediscount_id, quotediscount_quote_id, quotediscount_requestedamount,"
					+ " quotediscount_request_emp_id, quotediscount_authorize_emp_id,"
					+ " quotediscount_entry_id, quotediscount_entry_date,"
					+ " quotediscount_modified_id, quotediscount_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
					+ " WHERE quotediscount_id = " + quotediscount_id + "";
			// SOP("StrSql===pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					quotediscount_id = crs.getString("quotediscount_id");
					quotediscount_quote_id = crs.getString("quotediscount_quote_id");
					quotediscount_requestedamount = crs.getString("quotediscount_requestedamount");
					quotediscount_request_emp_id = crs.getString("quotediscount_request_emp_id");
					// quotediscount_authorize_emp_id = crs.getString("quotediscount_authorize_emp_id");
					quotediscount_entry_id = crs.getString("quotediscount_entry_id");
					quotediscount_entry_date = crs.getString("quotediscount_entry_date");
					entry_by = Exename(comp_id, crs.getInt("quotediscount_entry_id"));
					entry_date = strToLongDate(crs.getString("quotediscount_entry_date"));
					quotediscount_modified_id = crs.getString("quotediscount_modified_id");
					if (!quotediscount_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(quotediscount_modified_id));
						modified_date = strToLongDate(crs.getString("quotediscount_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Discount!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote_discount"
						+ " SET"
						+ " quotediscount_requestedamount = '" + quotediscount_requestedamount + "',"
						+ " quotediscount_authorize_emp_id = " + quotediscount_authorize_emp_id + ","
						+ " quotediscount_modified_id = " + quotediscount_modified_id + ","
						+ " quotediscount_modified_date = '" + quotediscount_modified_date + "'"
						+ " WHERE quotediscount_id = " + quotediscount_id + "";
				// SOP("discount===update==" + StrSql);
				stmttx.execute(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_discount"
					+ " WHERE quotediscount_id = " + quotediscount_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT "
					+ " discount_id,"
					+ " discount_brand_id,"
					+ " COALESCE(request.emp_id, 0) AS requestemp_id,"
					+ " jobtitle_report_id,"
					+ " COALESCE(report.emp_id, 0) AS reportemp_id,"
					+ " COALESCE(report.emp_jobtitle_id, 0) AS reportjobtitle_id,"
					+ " CONCAT(report.emp_name,' (',report.emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp request"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = request.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp report ON report.emp_jobtitle_id = jobtitle_report_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_discount ON discount_jobtitle_id = report.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = " + quotediscount_quote_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " AND model_brand_id = discount_brand_id "
					+ " WHERE 1 = 1"
					+ " AND request.emp_id = " + emp_id
					+ " AND report.emp_active = '1'"
					+ " AND SUBSTR(discount_month, 1, 6) = SUBSTR('" + ToLongDate(kknow()) + "', 1, 6)"
					+ " AND (report.emp_id IN (SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE 1 = 1"
					+ " AND branch_brand_id = discount_brand_id"
					+ " AND branch_id = quote_branch_id"
					+ " AND branch_branchtype_id = 1)"
					+ " OR report.emp_all_branches = 1"
					+ " OR report.emp_branch_id = quote_branch_id)"
					+ " AND report.emp_id != 1"
					+ " AND (discount_model_id = 0"
					+ "	OR discount_model_id = item_model_id)"
					+ " GROUP BY report.emp_id"
					+ " ORDER BY RAND()"
					+ " LIMIT 1";

			// SOPInfo("StrSql==PopulateExecutive==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				quotediscount_authorize_emp_id = crs.getString("reportemp_id");
				quotediscount_authorize_emp_name = crs.getString("emp_name");

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {

		StrSql = "SELECT "
				+ " config_email_enable,"
				+ " config_sms_enable,"
				+ " comp_email_enable,"
				+ " comp_sms_enable,"
				+ " COALESCE (branch_email1, '') AS branch_email1,"
				// + " COALESCE (emp_name, '') AS emp_name_authorize,"
				+ " COALESCE (emp_email1, '') AS emp_email1,"
				+ " COALESCE (emp_mobile1, '') AS emp_mobile1,"
				+ " brandconfig_quote_discount_authorize_email_enable,"
				+ " brandconfig_quote_discount_authorize_email_format,"
				+ " brandconfig_quote_discount_authorize_email_sub,"
				+ " brandconfig_quote_discount_authorize_sms_enable,"
				+ " brandconfig_quote_discount_authorize_sms_format"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quotediscount_quote_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quotediscount_authorize_emp_id,"
				+ " " + compdb(comp_id) + "axela_config,"
				+ " " + compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1"
				+ " AND quotediscount_id = " + quotediscount_id;

		// SOP("StrSql---PopulateConfigDetails-------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				// emp_name_authorize = crs.getString("emp_name_authorize");
				emp_email1 = crs.getString("emp_email1");
				emp_mobile1 = crs.getString("emp_mobile1");
				brandconfig_quote_discount_authorize_email_enable = crs.getString("brandconfig_quote_discount_authorize_email_enable");
				brandconfig_quote_discount_authorize_email_format = crs.getString("brandconfig_quote_discount_authorize_email_format");
				brandconfig_quote_discount_authorize_email_sub = crs.getString("brandconfig_quote_discount_authorize_email_sub");
				brandconfig_quote_discount_authorize_sms_enable = crs.getString("brandconfig_quote_discount_authorize_sms_enable");
				brandconfig_quote_discount_authorize_sms_format = crs.getString("brandconfig_quote_discount_authorize_sms_format");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void SendEmail() throws SQLException {
		String emailmsg, emailsub;
		emailmsg = brandconfig_quote_discount_authorize_email_format;
		emailsub = brandconfig_quote_discount_authorize_email_sub;

		emailsub = "REPLACE('" + emailsub + "','[QUOTEID]', quote_id)";
		emailsub = "REPLACE(" + emailsub + ",'[CUSTOMERID]', customer_id)";
		emailsub = "REPLACE(" + emailsub + ",'[CUSTOMERNAME]', customer_name)";
		emailsub = "REPLACE(" + emailsub + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ",'[CONTACTMOBILE1]', contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ",'[CONTACTPHONE1]', contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ",'[CONTACTEMAIL1]', contact_email1)";
		emailsub = "REPLACE(" + emailsub + ",'[EXENAME]', req.emp_name)";
		emailsub = "REPLACE(" + emailsub + ",'[EXEJOBTITLE]', reqjob.jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ",'[EXEMOBILE1]', req.emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ",'[EXEPHONE1]', req.emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ",'[EXEEMAIL1]', req.emp_email1)";

		emailsub = "REPLACE(" + emailsub + ",'[AUTHORIZEEXEJOBTITLE]', authjob.jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ",'[AUTHORIZEEXENAME]', auth.emp_name)";
		emailsub = "REPLACE(" + emailsub + ",'[AUTHORIZEEXEMOBILE1]', auth.emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ",'[AUTHORIZEEXEPHONE1]', auth.emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ",'[AUTHORIZEEXEEMAIL1]', auth.emp_email1)";

		emailsub = "REPLACE(" + emailsub + ",'[MODELNAME]', model_name)";
		emailsub = "REPLACE(" + emailsub + ",'[ITEMNAME]', item_name)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHADDRESS]',branch_add)";
		emailsub = "REPLACE(" + emailsub + ", '[QUOTEAMOUNT]',ROUND(quote_grandtotal,2))";
		emailsub = "REPLACE(" + emailsub + ", '[DISCOUNTAMOUNT]',ROUND(quotediscount_requestedamount,2))";

		emailmsg = "REPLACE('" + emailmsg + "','[QUOTEID]', quote_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]', customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]', customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]', contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTPHONE1]', contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]', contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[EXEJOBTITLE]', reqjob.jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[EXENAME]', req.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[EXEMOBILE1]', req.emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[EXEPHONE1]', req.emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[EXEEMAIL1]', req.emp_email1)";

		emailmsg = "REPLACE(" + emailmsg + ",'[AUTHORIZEEXEJOBTITLE]', authjob.jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[AUTHORIZEEXENAME]', auth.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[AUTHORIZEEXEMOBILE1]', auth.emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[AUTHORIZEEXEPHONE1]', auth.emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[AUTHORIZEEXEEMAIL1]', auth.emp_email1)";

		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]', model_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[ITEMNAME]', item_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";
		emailmsg = "REPLACE(" + emailmsg + ", '[QUOTEAMOUNT]',ROUND(quote_grandtotal,2))";
		emailmsg = "REPLACE(" + emailmsg + ", '[DISCOUNTAMOUNT]',ROUND(quotediscount_requestedamount,2))";

		try {
			StrSql = "SELECT"
					+ " quote_branch_id,"
					+ " '" + quote_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + emp_email1 + "',"
					+ " " + unescapehtml(emailsub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quotediscount_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp req ON req.emp_id = quote_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle reqjob ON reqjob.jobtitle_id = req.emp_jobtitle_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_emp auth ON auth.emp_id = quotediscount_authorize_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle authjob ON authjob.jobtitle_id = auth.emp_jobtitle_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " WHERE quotediscount_id = " + quotediscount_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("=====email====" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS() throws SQLException {
		String smsmsg = brandconfig_quote_discount_authorize_sms_format;

		smsmsg = "REPLACE('" + smsmsg + "','[QUOTEID]', quote_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERID]', customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]', customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTJOBTITLE]', contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]', contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTPHONE1]', contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]', contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[EXEJOBTITLE]', reqjob.jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[EXENAME]', req.emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[EXEMOBILE1]', req.emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[EXEPHONE1]', req.emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[EXEEMAIL1]', req.emp_email1)";

		smsmsg = "REPLACE(" + smsmsg + ",'[AUTHORIZEEXEJOBTITLE]', authjob.jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[AUTHORIZEEXENAME]', auth.emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[AUTHORIZEEXEMOBILE1]', auth.emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[AUTHORIZEEXEPHONE1]', auth.emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[AUTHORIZEEXEEMAIL1]', auth.emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[MODELNAME]', model_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[ITEMNAME]', item_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";
		smsmsg = "REPLACE(" + smsmsg + ", '[QUOTEAMOUNT]',ROUND(quote_grandtotal,2))";
		smsmsg = "REPLACE(" + smsmsg + ", '[DISCOUNTAMOUNT]',ROUND(quotediscount_requestedamount,2))";

		try {
			StrSql = "SELECT"
					+ " quote_branch_id,"
					+ " '" + quote_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + emp_mobile1 + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quotediscount_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp req ON req.emp_id = quote_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle reqjob ON reqjob.jobtitle_id = req.emp_jobtitle_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_emp auth ON auth.emp_id = quotediscount_authorize_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle authjob ON authjob.jobtitle_id = auth.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " WHERE quotediscount_id = " + quotediscount_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("-------Sendsms-----------" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
