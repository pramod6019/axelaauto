package axela.customer;
/*Bhagwan Singh 21/01/2013*/

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Customer_Dash extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String customer_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "", SqlJoin = "";
	public String StrHTML = "";
	public String customer_name = "";
	public String contact_customer_id = "0";
	public String customer_communication = "";
	public String customer_address = "";
	public String customer_landmark = "";
	public String customer_notes = "";
	public String customer_active = "0";
	public String customer_exe = "";
	public String enquiry_count = "";
	public String quote_count = "";
	public String so_count = "";
	public String invoice_count = "";
	public String receipt_count = "";
	public String payment_count = "";
	public String jc_count = "";
	public String enquiry_total = "";
	public String quote_total = "";
	public String so_total = "";
	public String invoice_total = "";
	public String receipt_total = "";
	public String payment_total = "";
	public String jc_total = "";

	// customer details
	public String customer_alias = "";
	public String customer_code = "";
	public String customer_branch_id = "0";
	public String customer_mobile1 = "";
	public String customer_mobile2 = "";
	public String customer_mobile3 = "";
	public String customer_mobile4 = "";
	public String customer_mobile5 = "";
	public String customer_mobile6 = "";
	public String customer_phone1 = "";
	public String customer_phone2 = "";
	public String customer_phone3 = "";
	public String customer_phone4 = "";
	public String customer_fax1 = "";
	public String customer_fax2 = "";
	public String customer_email1 = "";
	public String customer_email2 = "";

	public String customer_website1 = "";
	public String customer_website2 = "";
	public String customer_gst_no = "";
	public String gst_regdate = "";
	public String customer_arn_no = "";
	public String customer_city_id = "";
	public String customer_pin = "";
	public String customer_tds = "";
	public String customer_pan_no = "";
	public String customersince = "";
	public String customer_type = "";
	public String config_customer_sob = "0";
	public String config_customer_soe = "0";
	public String customer_itstatus_id = "0";
	public String customer_sob_id = "0";
	public String customer_soe_id = "0";
	public String customer_emp_id = "0";

	// for Tickets
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String ticketstatus_id = "0";
	public long closetime = 0;
	public City_Check citycheck = new City_Check();
	public DecimalFormat deci = new DecimalFormat("0.00");
	DecimalFormat df = new DecimalFormat("0.00");

	// Customer Transaction

	public String customer_so_amount = "0";
	public String customer_so_count = "0";
	public String customer_jc_amount = "0";
	public String customer_jc_count = "0";
	// public String customer_insurance_amount = "0";
	// public String customer_insurance_count = "0";
	public String customer_coupons_amount = "0";
	public String customer_coupons_count = "0";
	public String customer_invoice_amount = "0";
	public String customer_invoice_count = "0";
	public String customer_receipt_amount = "0";
	public String customer_receipt_count = "0";

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_customer_access", request, response);
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				PopulateConfigDetails();
				CustomerDetails(response);
				PopulateFields(response);
				PopulateCountTransaction(customer_id);
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

	public void PopulateCountTransaction(String customer_id) {
		StrSql = " SELECT "
				+ " COALESCE(COUNT(so_id), 0) AS 'socount', "
				+ " COALESCE(SUM(so_grandtotal), 0) AS 'soamt', "
				+ " COALESCE(COUNT(jc_id), 0) AS 'jccount', "
				+ " COALESCE(SUM(jc_grandtotal), 0) AS 'jcamt', "
				// + " COALESCE(COUNT(insurpolicy_id), 0) AS 'policycount', "
				// + " COALESCE(SUM(insurpolicy_premium_amt), 0) AS 'policyamt', "
				// + " COALESCE(COUNT(coupon_id),0) AS 'couponcount', "
				+ " COALESCE(SUM(IF(voucher_vouchertype_id = 9, 1, 0)), 0) AS 'receiptcount', "
				+ " COALESCE(SUM(IF(voucher_vouchertype_id = 9, voucher_amount, 0)), 0) AS 'receiptamt', "
				+ " COALESCE(SUM(IF(voucher_vouchertype_id = 6, 1, 0)),0) AS 'invoicecount', "
				+ " COALESCE(SUM(IF(voucher_vouchertype_id = 6, voucher_amount, 0)), 0) AS 'invoiceamt' "
				+ " FROM " + compdb(comp_id) + "axela_customer "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_customer_id = customer_id AND so_active = 1 "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_customer_id = customer_id ANd jc_active = 1 "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_policy ON insurpolicy_customer_id = customer_id AND insurpolicy_active = 1 "
				// + " LEFT JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_customer_id  = customer_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = customer_id AND voucher_active = 1 "
				+ " WHERE customer_id = " + customer_id;
		// SOP("transaction:" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				customer_so_amount = IndDecimalFormat(crs.getString("soamt"));
				// SOP("customer_so_amount" + IndDecimalFormat(crs.getString("soamt")));
				customer_so_count = CNumeric(crs.getString("socount"));
				customer_jc_amount = IndDecimalFormat(crs.getString("jcamt"));
				customer_jc_count = CNumeric(crs.getString("jccount"));
				// customer_insurance_amount = IndDecimalFormat(crs.getString("policyamt"));
				// customer_insurance_count = CNumeric(crs.getString("policycount"));
				// customer_coupons_amount = crs.getString("");
				// customer_coupons_count = CNumeric(crs.getString("couponcount"));
				customer_invoice_amount = IndDecimalFormat(crs.getString("invoiceamt"));
				customer_invoice_count = CNumeric(crs.getString("invoicecount"));
				customer_receipt_amount = IndDecimalFormat(crs.getString("receiptamt"));
				customer_receipt_count = CNumeric(crs.getString("receiptcount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void CustomerDetails(HttpServletResponse response) {
		try {
			StrSql = " SELECT customer_name, customer_alias, customer_code, customer_branch_id"
					+ " customer_address, customer_landmark,"
					+ " customer_phone1, customer_phone2, customer_phone3, customer_phone4,"
					+ " customer_mobile1, customer_mobile2, customer_mobile3, customer_mobile4, customer_mobile5, customer_mobile6, "
					+ " customer_fax1, customer_fax2,"
					+ " customer_email1, customer_email2, customer_emp_id,"
					+ " customer_website1, customer_website2, customer_pin, customer_notes, customer_active,"
					+ " coalesce(city_name,'') as city_name,"
					+ " coalesce(emp_id,0) as customer_exe"
					// + " coalesce(concat(emp_name,' (', emp_ref_no, ')'),'') as customer_exe"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=customer_branch_id"
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = customer_emp_id"
					+ " left join " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
					+ " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " where customer_id = " + customer_id
					+ BranchAccess
					+ ExeAccess
					+ " group by customer_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_name = crs.getString("customer_name");
					customer_alias = crs.getString("customer_alias");
					customer_code = crs.getString("customer_code");
					customer_branch_id = crs.getString("customer_branch_id");
					customer_mobile1 = crs.getString("customer_mobile1");
					customer_mobile2 = crs.getString("customer_mobile2");
					customer_mobile3 = crs.getString("customer_mobile3");
					customer_mobile4 = crs.getString("customer_mobile4");
					customer_mobile5 = crs.getString("customer_mobile5");
					customer_mobile6 = crs.getString("customer_mobile6");
					customer_phone1 = crs.getString("customer_phone1");
					customer_phone2 = crs.getString("customer_phone2");
					customer_phone3 = crs.getString("customer_phone3");
					customer_phone4 = crs.getString("customer_phone4");
					customer_fax1 = crs.getString("customer_fax1");
					customer_fax2 = crs.getString("customer_fax2");
					customer_email1 = crs.getString("customer_email1");
					customer_email2 = crs.getString("customer_email2");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Customer!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// System.out.println(ex);
		}
	}

	public String ListContact(String comp_id, String customer_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		String active = "";
		String address = "";
		try {
			StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
					+ " contact_jobtitle, contact_location, contact_phone1,"
					+ " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
					+ " contact_email1, contact_email2, contact_yahoo, contact_msn, contact_aol,"
					+ " contact_address, contact_pin, contact_landmark, contact_dob, contact_active,"
					+ " coalesce(city_name,'') as city_name,"
					+ " customer_id, customer_name, coalesce(branch_name,'') as branch, coalesce(branch_code,'') as branch_code"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id= customer_branch_id"
					+ " left join " + compdb(comp_id) + "axela_city on city_id = contact_city_id"
					+ " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " where contact_customer_id = " + customer_id + BranchAccess;
			// SOP("StrSql=1====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Contacts</div>\n</div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div class=\"table-hover\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">ID</th>\n");
				Str.append("<th data-hide=\"phone\">Contact Person</th>\n");
				Str.append("<th data-hide=\"phone\">Designation</th>\n");
				Str.append("<th data-hide=\"phone\">Communication</th>\n");
				Str.append("<th data-hide=\"phone\">Address</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("contact_active").equals("0")) {
						active = "<font color=red><b>&nbsp;[Inactive]</b></font>";
					} else {
						active = "";
					}
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td nowrap align=center>");
					Str.append(crs.getString("contact_id")).append("");
					Str.append("</td>");
					Str.append("<td align=left>");
					Str.append("").append(crs.getString("contact_name")).append(active);
					if (!crs.getString("contact_jobtitle").equals("")) {
						Str.append("<br>").append(crs.getString("contact_jobtitle"));
					}
					if (!crs.getString("contact_location").equals("")) {
						Str.append("<br>").append(crs.getString("contact_location"));
					}
					Str.append("</td><td>" + crs.getString("contact_jobtitle") + "</td>");
					Str.append("<td nowrap align=center>");
					if (!crs.getString("contact_phone1").equals("")) {
						Str.append("").append(crs.getString("contact_phone1")).append(ClickToCall(crs.getString("contact_phone1"), comp_id)).append("<br>");
					}
					if (!crs.getString("contact_phone2").equals("")) {
						Str.append("").append(crs.getString("contact_phone2")).append(ClickToCall(crs.getString("contact_phone2"), comp_id)).append("<br>");
					}
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("").append(crs.getString("contact_mobile1")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("<br>");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("").append(crs.getString("contact_mobile2")).append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("<br>");
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("" + "<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a><br>");
					}
					if (!crs.getString("contact_email2").equals("")) {
						Str.append("" + "<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a><br>");
					}
					if (!crs.getString("contact_yahoo").equals("")) {
						Str.append("" + "<a href=mailto:").append(crs.getString("contact_yahoo")).append(">").append(crs.getString("contact_yahoo")).append("</a><br>");
					}
					if (!crs.getString("contact_msn").equals("")) {
						Str.append("" + "<a href=mailto:").append(crs.getString("contact_msn")).append(">").append(crs.getString("contact_msn")).append("</a><br>");
					}
					if (!crs.getString("contact_aol").equals("")) {
						Str.append("" + "<a href=mailto:").append(crs.getString("contact_aol")).append(">").append(crs.getString("contact_aol")).append("</a><br>");
					}
					Str.append("</td><td align=center>");
					address = crs.getString("contact_address");
					if (!address.equals("")) {
						address = crs.getString("contact_address");
						if (!crs.getString("city_name").equals("")) {
							address = address + ", " + crs.getString("city_name");
						}
						address = address + " - " + crs.getString("contact_pin");
						if (!crs.getString("contact_landmark").equals("")) {
							address = address + "<br>Landmark: " + crs.getString("contact_landmark");
						}
					}
					Str.append(address);
					Str.append("</td></tr>");
				}
				Str.append("</tbody></table></div>\n");
				Str.append("</div>\n </div>\n </div></div>\n");
			}
			else {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Contacts</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div align=center><br><br><font color=red><b>No Contact(s) found!</b></font></div>");
				Str.append("</div> </div></div></div>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListCustomerDetails() {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			Str.append("<table class=\"table\"");
			Str.append("<tr>\n");
			Str.append("<th>Name</th>\n");
			Str.append("<th>Count</th>\n");
			Str.append("<th>Value</th>\n");
			Str.append("</tr>\n");
			StrSql = "SELECT count(contact_customer_id) as contactcount,"
					+ " IF(1 = 1, coalesce((select concat(count(enquiry_id), '-', SUM(enquiry_value))"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_customer_id = customer_id), ''), '') as enquirycount,"
					+ " IF(1 = 1, coalesce((select concat(count(quote_id), '-', SUM(quote_grandtotal))"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote WHERE quote_customer_id = customer_id and quote_enquiry_id !=0 and quote_active = 1), ''), '') as quotecount,"
					+ " IF(1 = 1, coalesce((select concat(count(so_id), '-', SUM(so_grandtotal))"
					+ " FROM " + compdb(comp_id) + "axela_sales_so WHERE so_customer_id = customer_id and so_active = 1), ''), '') as socount,"
					+ " IF(1 = 1, COALESCE((SELECT CONCAT(COUNT(voucher_id), '-', SUM(voucher_amount))"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_customer_id = customer_id AND voucher_active = 1 AND voucher_vouchertype_id = 6), ''), '') as invoicecount,"
					+ " IF(1 = 1, coalesce((SELECT CONCAT(COUNT(voucher_id), '-', SUM(voucher_amount))"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_customer_id = customer_id AND voucher_active = 1 AND voucher_vouchertype_id = 9), ''), '') as receiptcount,"
					+ " IF(1 = 1, COALESCE((SELECT CONCAT(COUNT(voucher_id), '-', SUM(voucher_amount))"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_customer_id = customer_id AND voucher_active = 1 AND voucher_vouchertype_id = 15), ''), '') as paymentcount"
					+ " FROM " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_customer"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_customer_id = customer_id"
					+ " WHERE customer_id = " + customer_id + "";
			SOP("StrSql cust dash==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				String[] str;
				if (!crs.getString("contactcount").equals("")) {
					Str.append("<tr><td><a href = \"../customer/customer-contact-list.jsp?contact_customer_id=").append(customer_id).append("\">Contacts</a></td>");
					Str.append("<td>").append(crs.getString("contactcount")).append("</td>");
					Str.append("<td>&nbsp;</td></tr>");
				}
				if (!crs.getString("enquirycount").equals("")) {
					str = crs.getString("enquirycount").split("-");
					enquiry_count = str[0];
					enquiry_total = str[1];
					Str.append("<tr><td><a href = \"../sales/enquiry-list.jsp?enquiry_customer_id=").append(customer_id).append("\">Enquiry</a></td>");
					Str.append("<td>").append(enquiry_count).append("</td>");
					Str.append("<td >").append(IndDecimalFormat(deci.format(Double.parseDouble(enquiry_total)))).append("</td></tr>");
				}

				if (!crs.getString("quotecount").equals("")) {
					str = crs.getString("quotecount").split("-");
					quote_count = str[0];
					quote_total = str[1];
					Str.append("<tr><td><a href = \"../sales/veh-quote-list.jsp?quote_customer_id=").append(customer_id).append("\">Quotes</a></td>");
					Str.append("<td>").append(quote_count).append("</td>");
					Str.append("<td>").append(IndDecimalFormat(deci.format(Double.parseDouble(quote_total)))).append("</td></tr>");
				}

				if (!crs.getString("socount").equals("")) {
					str = crs.getString("socount").split("-");
					so_count = str[0];
					so_total = str[1];
					Str.append("<tr><td><a href = \"../sales/veh-salesorder-list.jsp?so_customer_id=").append(customer_id).append("\">Sales Orders</a></td>");
					Str.append("<td>").append(so_count).append("</td>");
					Str.append("<td>").append(IndDecimalFormat(deci.format(Double.parseDouble(so_total)))).append("</td></tr>");
				}

				if (!crs.getString("invoicecount").equals("")) {
					str = crs.getString("invoicecount").split("-");
					invoice_count = str[0];
					invoice_total = str[1];
					Str.append("<tr><td><a href = \"../invoice/invoice-list.jsp?invoice_customer_id=").append(customer_id).append("\">Invoices</a></td>");
					Str.append("<td>").append(invoice_count).append("</td>");
					Str.append("<td>").append(IndDecimalFormat(deci.format(Double.parseDouble(invoice_total)))).append("</td></tr>");
				}

				if (!crs.getString("receiptcount").equals("")) {
					str = crs.getString("receiptcount").split("-");
					receipt_count = str[0];
					receipt_total = str[1];
					Str.append("<tr><td><a href = \"../invoice/receipt-list.jsp?receipt_customer_id=").append(customer_id).append("\">Receipts</td>");
					Str.append("<td>").append(receipt_count).append("</td>");
					Str.append("<td>").append(IndDecimalFormat(deci.format(Double.parseDouble(receipt_total)))).append("</td></tr>");
				}

				// payment
				if (!crs.getString("paymentcount").equals("")) {
					str = crs.getString("paymentcount").split("-");
					payment_count = str[0];
					payment_total = str[1];
					Str.append("<tr><td><a href = \"../invoice/payment-list.jsp?payment_customer_id=").append(customer_id).append("\">Payments</td>");
					Str.append("<td>").append(payment_count).append("</td>");
					Str.append("<td>").append(IndDecimalFormat(deci.format(Double.parseDouble(payment_total)))).append("</td></tr>");
				}
			}
			crs.close();
			Str.append("</table>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String HistoryDetails(String comp_id, String customer_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT " + compdb(comp_id) + "axela_customer_history.*,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,emp_id "
					+ " FROM " + compdb(comp_id) + "axela_customer_history"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = history_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id "
					+ " WHERE history_customer_id = " + customer_id + ""
					+ " ORDER BY history_id desc";
			// SOP("StrSql------ListHistory------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				/*
				 * Str.append("<div class=\"table-responsive table-bordered\">\n" ); Str.append( "<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n" );
				 * Str.append("<tr>"); Str.append("<td width=\"30%\" align=\"right\">&nbsp;"); Str.append("</td>"); Str.append("</tr>"); Str.append("<TR>"); Str
				 * .append("<TD height=\"200\" align=\"center\" vAlign=\"top\" >" );
				 */
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div>\n</div>\n");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div class=\"table-responsive \">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">Date</th>");
					Str.append("<th>Action By</th>");
					Str.append("<th data-hide=\"phone, tablet\">Type of Action</th>");
					Str.append("<th data-hide=\"phone, tablet\">New Value</th>");
					Str.append("<th data-hide=\"phone, tablet\"> Old Value</th>");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						// enquiry_title = crs.getString("enquiry_title");
						Str.append("<tr>\n");
						Str.append("<td valign=top align=left >")
								.append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=../portal/customer-summary.jsp?emp_id=")
								.append(crs.getString("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>").append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_actiontype")).append(" </td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_newvalue")).append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_oldvalue")).append("</td>");
						Str.append("</tr>" + "\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>");
					Str.append("</div>\n </div>\n </div></div>\n");
				} else {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div></div>");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div align=center><br><br><font color=red><b>No History found!</b></font></div>");
					Str.append("</div> </div></div></div>");
				}
				crs.close();
				/*
				 * Str.append("</td>"); Str.append("</tr>"); Str.append("</table>"); Str.append("</div>");
				 */// /

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String PopulateEmp() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					// + " WHERE emp_sales = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), customer_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
				// SOP("return:" + StrSelectdrop(crs.getString("emp_id"), "1") + " " + crs.getString("emp_id"));
				// SOP(StrSelectdrop(crs.getString("emp_id"), "1"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateGroup() {
		try {
			StrSql = "SELECT group_id, CONCAT(group_desc, '') AS group_desc"
					+ " FROM " + compdb(comp_id) + "axela_customer_group"
					+ " ORDER BY group_desc";

			// SOP("StrSql==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id")).append(">");
				Str.append(crs.getString("group_desc") + "(" + crs.getString("group_id") + ")").append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");

		Str.append("<option value=1").append(StrSelectdrop("1", customer_type))
				.append(">").append("Customer");
		Str.append("</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", customer_type))
				.append(">").append("Supplier");
		Str.append("</option>\n");

		return Str.toString();
	}

	public String PopulateItStatus() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT itstatus_id, itstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_itstatus"
					+ " ORDER BY itstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("itstatus_id"));
				Str.append(StrSelectdrop(crs.getString("itstatus_id"), customer_itstatus_id));
				Str.append(">").append(crs.getString("itstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id"));
				Str.append(StrSelectdrop(crs.getString("sob_id"), customer_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"));
				Str.append(StrSelectdrop(crs.getString("soe_id"), customer_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateGroupTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			// if (add.equals("yes") && fo_group_trans != null) {
			// StrSql = "SELECT CONCAT(group_desc, '') AS group_desc, group_id"
			// + " FROM " + compdb(comp_id) + "axela_customer_group"
			// + " ORDER BY group_desc";
			// } else {
			StrSql = "SELECT group_id, CONCAT(group_desc, '') AS group_desc"
					+ " FROM " + compdb(comp_id) + "axela_customer_group"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_group_trans ON trans_group_id = group_id"
					+ " WHERE trans_customer_id = '" + customer_id + "'"
					+ " ORDER BY group_desc";
			// }
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				// if (add.equals("yes") && fo_group_trans != null) {
				// for (int i = 0; i < fo_group_trans.length; i++) {
				// if (crs.getString("group_id").equals(fo_group_trans[i])) {
				// Str.append("<option value=").append(crs.getString("group_id")).append(" selected>");
				// Str.append(crs.getString("group_desc")).append("</option> \n");
				// }
				// }
				// } else if (update.equals("yes")) {
				Str.append("<option value=").append(crs.getString("group_id")).append(" selected>");
				Str.append(crs.getString("group_desc")).append("</option>\n");
				// }
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_state ON state_id = city_state_id"
					+ " WHERE customer_id = " + customer_id + BranchAccess + "";

			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("PopulateFields=====" + StrSql);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					customer_branch_id = crs.getString("customer_branch_id");
					customer_name = crs.getString("customer_name");
					customer_alias = crs.getString("customer_alias");
					customer_code = crs.getString("customer_code");
					customer_mobile1 = crs.getString("customer_mobile1");
					customer_mobile2 = crs.getString("customer_mobile2");
					customer_mobile3 = crs.getString("customer_mobile3");
					customer_mobile4 = crs.getString("customer_mobile4");
					customer_mobile5 = crs.getString("customer_mobile5");
					customer_mobile6 = crs.getString("customer_mobile6");
					customer_phone1 = crs.getString("customer_phone1");
					customer_phone2 = crs.getString("customer_phone2");
					customer_phone3 = crs.getString("customer_phone3");
					customer_phone4 = crs.getString("customer_phone4");
					customer_fax1 = crs.getString("customer_fax1");
					customer_fax2 = crs.getString("customer_fax2");
					customer_email1 = crs.getString("customer_email1");
					customer_email2 = crs.getString("customer_email2");
					customer_website1 = crs.getString("customer_website1");
					customer_website2 = crs.getString("customer_website2");
					customer_gst_no = crs.getString("customer_gst_no");
					gst_regdate = crs.getString("customer_gst_regdate");
					customer_arn_no = crs.getString("customer_arn_no");
					customer_itstatus_id = crs.getString("customer_itstatus_id");
					customer_address = crs.getString("customer_address");
					customer_city_id = crs.getString("customer_city_id");
					customer_tds = crs.getString("customer_tds");

					customer_pin = crs.getString("customer_pin");
					customer_landmark = crs.getString("customer_landmark");
					customer_pan_no = crs.getString("customer_pan_no");
					customer_soe_id = crs.getString("customer_soe_id");
					customer_sob_id = crs.getString("customer_sob_id");
					// customer_empcount_id =
					// crs.getString("customer_empcount_id");

					customer_emp_id = crs.getString("customer_emp_id");
					customersince = crs.getString("customer_since");
					customer_type = crs.getString("customer_type");
					customer_active = crs.getString("customer_active");
					customer_notes = crs.getString("customer_notes");
				}
			} else {
				// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid " + tag + "!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String EnquiryDetails(String comp_id, String customer_id) {
		CachedRowSet crs = null;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		// if (!msg.equals("")) {
		try {

			StrSql = "SELECT enquiry_id,"
					+ " CONCAT('ENQ', branch_code, enquiry_no) AS enquiry_no, enquiry_title,"
					+ " enquiry_dmsno, enquiry_qcsno, enquiry_date, enquiry_close_date, enquiry_lead_id, enquiry_item_id,"
					+ " customer_id, customer_name, contact_id, contact_mobile1, contact_phone1, contact_phone2, contact_mobile2, contact_email2,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, contact_email1,"
					+ " COALESCE(soe_name, '') AS soe_name,"
					+ " COALESCE(sob_name, '') AS sob_name,"
					+ " COALESCE(enquiry_model_id, 0) as enquiry_model_id, enquiry_status_id, enquiry_desc, status_name,"
					+ " COALESCE(campaign_id, '') AS campaign_id,"
					+ " COALESCE(campaign_name, '') AS campaign_name,"
					+ " emp.emp_id as emp_id, CONCAT(emp.emp_name, ' (', emp.emp_ref_no, ')') AS emp_name,"
					+ " COALESCE(item_name, '') item_name, stage_name, enquiry_enquirytype_id,  enquirytype_name, "
					+ " COALESCE((SELECT CONCAT(carmanuf_name,' - ', preownedmodel_name,' - ',variant_name)"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id "
					+ " where variant_id = enquiry_preownedvariant_id),'') preownedmodel,"
					+ " COALESCE(vehtype_name,'') AS vehtype_name,"
					+ " enquiry_priorityenquiry_id, branch_id, branch_code, "
					+ " branch_brand_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " COALESCE((select so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_enquiry_id = enquiry_id limit 1),'') AS so_id,"
					+ " REPLACE(COALESCE ( ("
					+ " SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
					+ " WHERE tagtrans_customer_id = customer_id ), '' ),',','') AS tag";

			StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN axela_veh_type ON vehtype_id = model_vehtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id";
			// if (preownedmanuf_ids != null && preownedcar.equals("1")) {
			// SqlJoin += " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id";
			// SqlJoin += " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id";
			// }
			StrSql += " WHERE 1 = 1"
					+ " AND enquiry_customer_id = " + customer_id;
			crs = processQuery(StrSql, 0);
			String bgcolor = "";
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Enquiries</div>\n </div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div>");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				// Str.append("<tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th data-hide=\"phone\">No</th>\n");
				Str.append("<th data-hide=\"phone\">Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\">Date</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Variant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Stage</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">SOE</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">SOB</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					if (crs.getString("enquiry_priorityenquiry_id").equals("1")) {
						bgcolor = "red";
					} else if (crs.getString("enquiry_priorityenquiry_id").equals("2")) {
						bgcolor = "blue";
					} else if (crs.getString("enquiry_priorityenquiry_id").equals("3")) {
						bgcolor = "yellow";
					}
					count++;
					Str.append("<tr>");
					Str.append("<td >").append(count).append("</td>\n");
					Str.append("<td >");
					Str.append("<a href=\"javascript:remote=window.open('../sales/enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("','enquirydash','');remote.focus();\">");
					Str.append(crs.getString("enquiry_id")).append("</a></td>\n");
					Str.append("<td>");
					if (crs.getString("enquiry_enquirytype_id").equals("1")) {
						Str.append("New ");
					} else if (crs.getString("enquiry_enquirytype_id").equals("2")) {
						Str.append("Pre-Owned ");
					}
					Str.append(crs.getString("vehtype_name") + "<br/>" + crs.getString("enquiry_no"));
					if (!crs.getString("enquiry_dmsno").equals("")) {
						Str.append("<br>DMS No.: " + crs.getString("enquiry_dmsno"));
					}
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("','enquirydash','');remote.focus();\">");
					Str.append(crs.getString("enquiry_title")).append("</a>");
					if (!crs.getString("enquiry_desc").equals("")) {
						Str.append("<br>").append(crs.getString("enquiry_desc"));
					}

					// if (config_sales_enquiry_refno.equals("1")) {
					// if (!crs.getString("enquiry_qcsno").equals("")) {
					// Str.append("<br>QCS No.: ").append(crs.getString("enquiry_qcsno"));
					// }
					// }

					if (!crs.getString("enquiry_lead_id").equals("0")) {
						Str.append("<br><a href=\"lead-list.jsp?lead_id=").append(crs.getString("enquiry_lead_id")).append("\">Lead ID: ").append(crs.getString("enquiry_lead_id")).append("</a>");
					}
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<div onMouseOver=\"populatefollowup(").append(crs.getString("enquiry_id")).append(")\">");
					Str.append(strToShortDate(crs.getString("enquiry_date"))).append(" - ");
					Str.append(strToShortDate(crs.getString("enquiry_close_date")));
					Str.append("<br><a href=\"enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("#tabs-2\">").append("Follow-up=>").append("</a></div>");
					Str.append("<div id=\"followup_").append(crs.getString("enquiry_id")).append("\">").append("</div>");
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contactname")).append("</a>");

					if (!crs.getString("contact_phone1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone1"), 10, "T", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_phone1"), comp_id));
					}
					if (!crs.getString("contact_phone2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone2"), 10, "T", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_phone2"), comp_id));
					}
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("enquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("enquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}

					// Populating Tags in Enquiry list
					Str.append("<br><br>");

					String Tag = crs.getString("tag");
					Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='border: 1px solid aliceblue;top:-16px; background:");
					Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
					Tag = ReplaceStr(Tag, "StartName", "");
					Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
					Str.append(Tag);
					// Tags End

					Str.append("</td>\n");
					Str.append("<td >").append(crs.getString("item_name"));
					Str.append("<br>").append(crs.getString("preownedmodel")).append("</td>\n");
					Str.append("<td >").append(crs.getString("stage_name")).append("</td>\n");
					Str.append("<td >").append(crs.getString("status_name")).append("</td>\n");

					// if (config_sales_soe.equals("1")) {
					Str.append("<td >").append(crs.getString("soe_name")).append("</td>\n");
					// }
					//
					// if (config_sales_sob.equals("1")) {
					Str.append("<td >").append(crs.getString("sob_name")).append("</td>\n");
					// }
					//
					Str.append("<td >");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
					Str.append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td >");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branchname")).append("</a></td\n>");
					Str.append("<td nowrap>");
					Str.append("<a href=\"enquiry-update.jsp?update=yes&enquiry_id=").append(crs.getString("enquiry_id")).append("\">Update Enquiry</a>");
					if (crs.getString("branch_brand_id").equals("1") || crs.getString("branch_brand_id").equals("2") || crs.getString("branch_brand_id").equals("153")) {
						Str.append("<br><a href=\"enquiry-trackingcard-maruti.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("&brand_id=")
								.append(crs.getString("branch_brand_id"))
								.append("\" target= \"_blank\" >Print Tracking Card</a>");

						// // Str.append("<br><a href=\"enquiry-trackingcard.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("&brand_id=").append(crs.getString("branch_brand_id"))
						// .append("\" target= \"_blank\" >Print Tracking Card</a>");
					} else if (crs.getString("branch_brand_id").equals("6")) {
						Str.append("<br><a href=\"enquiry-trackingcard-hyundai.jsp?dr_report=trackingcard-hyundai").append("&enquiry_id=")
								.append(crs.getString("enquiry_id"))
								.append("&brand_id=")
								.append(crs.getString("branch_brand_id"));
						Str.append("&dr_format=pdf\" target=\"_blank\">Print Tracking Card</a>");
					} else if (crs.getString("branch_brand_id").equals("7")) {
						Str.append("<br><a href=\"enquiry-trackingcard-ford.jsp?dr_report=trackingcard-ford")
								.append("&enquiry_id=").append(crs.getString("enquiry_id"))
								.append("&brand_id=").append(crs.getString("branch_brand_id"));
						Str.append("&dr_format=pdf\" target=\"_blank\">Print Tracking Card</a>");
					}
					else if (crs.getString("branch_brand_id").equals("55")) {
						Str.append("<br><a href=\"enquiry-trackingcard-mb.jsp?dr_report=trackingcard-mb")
								.append("&enquiry_id=").append(crs.getString("enquiry_id"))
								.append("&brand_id=").append(crs.getString("branch_brand_id"));
						Str.append("&dr_format=pdf\" target=\"_blank\">Print Tracking Card</a>");
					} else if (crs.getString("branch_brand_id").equals("151")) {
						Str.append("<br><a href=\"enquiry-trackingcard-triumph.jsp?dr_report=enquiry-trackingcard-triumph")
								.append("&enquiry_id=").append(crs.getString("enquiry_id"))
								.append("&brand_id=").append(crs.getString("branch_brand_id"));
						Str.append("&dr_format=pdf\" target=\"_blank\">Print Tracking Card</a>");
					}
					if (crs.getString("enquiry_enquirytype_id").equals("1")) {
						Str.append("<br><a href=\"testdrive-update.jsp?add=yes&enquiry_id=").append(crs.getString("enquiry_id")).append("\">Add Test Drive</a>");
					} else {
						Str.append("<br><a href=\"../preowned/preowned-testdrive-update.jsp?add=yes&enquiry_id=").append(crs.getString("enquiry_id")).append(" \">Add Pre Owned Test Drive</a>");
					}
					// if (emp_id.equals("1")) {
					if (crs.getString("enquiry_status_id").equals("1")) {
						Str.append("<br><a href=\"veh-quote-add.jsp?enquiry_id=").append(crs.getString("enquiry_id"))
								.append("\">Add Quote</a>");
					}
					Str.append("<br><a href=\"veh-quote-list.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("\">List Quotes</a>");
					// }
					Str.append("<br><a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Ticket</a>");
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><a href=\"enquiry-brochure-email.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("\">Email Brochure</a>");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Enquiries</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div align=center><br><br><font color=red><b>No Enquiries found!</b></font></div>");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
			// SOP("Table ====" + Str);
			// } else {
			// // RecCountDisplay = "<br><br><font color=\"red\">No Enquiry found!</font><br><br>";
			// }

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";

		}
		// }
		return Str.toString();

	}

	public String SODetails(String comp_id, String customer_id) {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			StrSql = "SELECT so_id, so_branch_id, branch_brand_id, branch_rateclass_id,"
					+ " CONCAT(so_prefix, so_no) AS so_no, so_date,"
					+ " so_delivered_date, so_dob, so_pan, so_netamt,"
					+ " so_totaltax, so_grandtotal, so_refno, so_active,"
					+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, so_auth,"
					+ " contact_id, so_quote_id, so_promise_date, COALESCE(vehstock_id, 0) AS vehstock_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, customer_id,"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name),'') AS item_name,"
					+ " customer_name, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " COALESCE(vehstock_delstatus_id, 0) AS vehstock_delstatus_id, so_enquiry_id,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name, customer_curr_bal, so_retail_date,"
					+ " COALESCE(vehstock_delstatus_id, '0') AS vehstock_delstatus_id,"
					+ " COALESCE(vehstock_comm_no, '') AS vehstock_comm_no,"
					+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no,"
					+ " COALESCE(vehstock_chassis_prefix, '') AS vehstock_chassis_prefix,"
					+ " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no,"
					// + " COALESCE(voucher_id, 0) AS voucher_id,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Paintwork',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 2"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Upholstery',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 4"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Package',"
					+ " COALESCE(vehstock_id, 0) AS vehstock_id, "
					+ "	so_preownedstock_id, COALESCE ( ( SELECT CONCAT( carmanuf_name, '-', preownedmodel_name, '-', variant_name )"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE preownedstock_id = so_preownedstock_id ), '' ) AS Preowned, "
					+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
					+ " WHERE tagtrans_customer_id = so_customer_id ), '' ), ',', '' ) AS tag";

			// CountSql = "SELECT COUNT(DISTINCT(so_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_so"
					// + "	INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = so_preownedstock_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_so_id = so_id and voucher_vouchertype_id = 6"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " WHERE 1 = 1 And so_customer_id = " + customer_id;

			StrSql += SqlJoin;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Sales Order</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div class=\"table-hover\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				// Str.append("<tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th data-hide=\"phone\">No.</th>\n");
				Str.append("<th data-hide=\"phone\">Sales Order</th>\n");
				Str.append("<th style=\"width:200px;\">Customer</th></style>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Tentative Delivery Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Retail Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Delivered Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("so_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("so_id") + ");'");
					Str.append("style='height:200px'>\n");
					Str.append("<td >").append(count).append("</td>\n");
					Str.append("<td >").append("<a href=\"javascript:remote=window.open('../sales/salesorder-dash.jsp?so_id=");
					Str.append(crs.getString("so_id")).append("','salesorderdash','');remote.focus();\">");
					Str.append(crs.getString("so_id")).append("</a></td>\n");
					Str.append("<td >").append(crs.getString("so_no")).append("</td>\n");
					Str.append("<td nowrap>\n");
					// if (config_sales_enquiry_refno.equals("1")) {
					// if (!crs.getString("so_refno").equals("")) {
					// Str.append("Ref. No.: ").append(crs.getString("so_refno")).append("<br>");
					// }
					// }

					if (crs.getString("so_active").equals("0")) {
						Str.append("<font color=\"red\">&nbsp;<b>[Inactive]</b></font><br>");
					}

					if (!crs.getString("so_enquiry_id").equals("0")) {
						Str.append("<a href=\"../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("so_enquiry_id")).append("\">Enquiry ID: ");
						Str.append(crs.getString("so_enquiry_id")).append("</a><br>");
					}

					if (!crs.getString("so_quote_id").equals("0")) {
						Str.append("<a href=\"../sales/veh-quote-list.jsp?quote_id=").append(crs.getString("so_quote_id")).append("\">Quote ID: ");
						Str.append(crs.getString("so_quote_id")).append("</a>");
					}

					if (crs.getString("so_auth").equals("1")) {
						Str.append("<br/><font color=red>[Authorized]</font><br>");
					}
					Str.append("</td>\n<td>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("so_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("so_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("so_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("so_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}

					// Populating Tags in Enquiry list
					Str.append("<br><br>");

					String Tag = crs.getString("tag");
					Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='top:-16px; background:");
					Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
					Tag = ReplaceStr(Tag, "StartName", "");
					Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
					Str.append(Tag);
					// Tags End

					Str.append("</td>\n<td >").append(strToShortDate(crs.getString("so_date"))).append("</td>\n");
					Str.append("<td  >").append(strToShortDate(crs.getString("so_promise_date"))).append("</td>\n");
					Str.append("<td  >");
					if (!crs.getString("so_retail_date").equals("")) {
						Str.append(strToLongDate(crs.getString("so_retail_date")));
					}
					Str.append("&nbsp;<td >");
					if (!crs.getString("so_delivered_date").equals("")) {
						Str.append(strToLongDate(crs.getString("so_delivered_date")));
					}
					Str.append("&nbsp;</td>\n<td >").append(crs.getString("delstatus_name")).append("</td>");
					Str.append("</td>\n<td>").append(crs.getString("item_name"));
					if (!crs.getString("vehstock_id").equals("0")) {
						Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\"><br>Stock ID: ");
						Str.append(crs.getString("vehstock_id")).append("</a></b>");

						if (!crs.getString("vehstock_comm_no").equals("")) {
							Str.append("<br>Commission No.: ").append(crs.getString("vehstock_comm_no"));
						}

						if (!crs.getString("vehstock_engine_no").equals("")) {
							Str.append("<br>Engine No.: ").append(crs.getString("vehstock_engine_no"));
						}

						if (!crs.getString("vehstock_chassis_prefix").equals("")) {
							Str.append("<br>Chassis Prefix: ").append(crs.getString("vehstock_chassis_prefix"));
						}

						if (!crs.getString("vehstock_chassis_no").equals("")) {
							Str.append("<br>Chassis No.: ").append(crs.getString("vehstock_chassis_no"));
						}

						if (!crs.getString("Paintwork").equals("")) {
							Str.append("<br>Color : ").append(crs.getString("Paintwork"));
						}

					} else if (!crs.getString("so_preownedstock_id").equals("0")) {
						Str.append("<a href=\"../preowned/preowned-stock-list.jsp?preownedstock_id=").append(crs.getString("so_preownedstock_id")).append("\"><br>Pre-Owned Stock ID: ");
						Str.append(crs.getString("so_preownedstock_id")).append("</a></b><br>");
						Str.append(crs.getString("Preowned"));
					}
					Str.append("</td>");
					Str.append("<td nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("so_netamt"))));
					if (!crs.getString("so_totaltax").equals("0")) {
						Str.append("<br>Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("so_totaltax"))));
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("so_grandtotal")))).append("</b>");
					Str.append("</td>\n<td >");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
					Str.append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td >");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a></td>\n");
					Str.append("<td nowrap>");

					Str.append("<a href=\"veh-salesorder-update.jsp?update=yes").append("&so_id=").append(crs.getString("so_id")).append(" \">Update Sales Order</a>");
					Str.append("<br><a href=\"salesorder-payment-track.jsp?add=yes").append("&so_id=").append(crs.getString("so_id")).append(" \">Payment Track</a>");
					Str.append("<br><a href=\"veh-salesorder-authorize.jsp?so_id=").append(crs.getString("so_id")).append(" \">Authorize</a>");
					Str.append("<br><a href=\"veh-salesorder-doc-list.jsp?so_id=").append(crs.getString("so_id")).append(" \">List Documents</a>");
					Str.append("<br><a href=\"veh-salesorder-wf-doc-list.jsp?so_id=").append(crs.getString("so_id")).append(" \">List Workflow Documents</a>");
					Str.append("<br><a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Ticket</a>");

					if (!crs.getString("so_id").equals("0")) {
						Str.append("<br><a href='../accounting/report-ledgerstatement.jsp?all=yes&so_date=" + crs.getString("so_date") + "&ledger=" + crs.getString("customer_id") + "&so_id="
								+ crs.getString("so_id")
								+ "' targer='_blank'>Ledger Statement</a>");
					}

					Str.append("<br><a href=\"../accounting/so-update.jsp?add=yes&vouchertype_id=27&voucherclass_id=27")
							.append("&dr_executive=" + crs.getString("emp_id"))
							.append("&dr_voucher_rateclass_id=").append(crs.getString("branch_rateclass_id"))
							.append("&voucher_so_id=")
							.append(crs.getString("so_id"))
							.append("&so_branch_id=")
							.append(crs.getString("so_branch_id"))
							.append("\">Add Pre-Order</a>");
					Str.append("<br><a href=\"../accounting/voucher-list.jsp?vouchertype_id=27&voucherclass_id=27")
							.append("&voucher_so_id=").append(crs.getString("so_id"))
							.append("\">List Pre-Order</a>");

					Str.append("<br><a href=\"../accounting/so-update.jsp?add=yes&checkinvoice=yes&vouchertype_id=6&voucherclass_id=6"
							+ "&vouchertrans_customer_id=").append(crs.getString("customer_id")).append("&span_cont_id=").append(crs.getString("contact_id"))
							.append("&dr_voucher_rateclass_id=").append(crs.getString("branch_rateclass_id"))
							.append("&dr_executive=" + crs.getString("emp_id"))
							.append("&voucher_so_id=" + crs.getString("so_id"))
							.append("&so_branch_id=" + crs.getString("so_branch_id"))
							.append("\">Add Invoice</a>");
					Str.append("<br><a href=\"../accounting/voucher-list.jsp?vouchertype_id=6&voucherclass_id=6&customer_id=")
							.append(crs.getString("customer_id"))
							.append("&voucher_so_id=")
							.append(crs.getString("so_id"))
							.append("\">List Invoice</a>");

					Str.append("<br><a href=\"../accounting/so-update.jsp?add=yes&vouchertype_id=6&voucherclass_id=6"
							+ "&vouchertrans_customer_id=").append(crs.getString("customer_id")).append("&span_cont_id=").append(crs.getString("contact_id"))
							.append("&dr_voucher_rateclass_id=").append(crs.getString("branch_rateclass_id"))
							.append("&voucher_so_id=").append(crs.getString("so_id"))
							.append("&so_branch_id=").append(crs.getString("so_branch_id"))
							.append("\">Add Accessories</a>");

					Str.append("<br><a href=\"../accounting/receipt-update.jsp?add=yes")
							// .append("&checkinvoice=yes")
							.append("&vouchertype_id=9&voucherclass_id=9&voucher_so_id=")
							.append(crs.getString("so_id")).append("&so_emp_id=").append(crs.getInt("emp_id")).append("&ledger=")
							.append(crs.getString("customer_id")).append("&dr_branch=")
							.append(crs.getString("so_branch_id"))
							.append("\">Add Receipt</a>");
					Str.append("<br><a href=\"../accounting/voucher-list.jsp?&vouchertype_id=9&voucherclass_id=9&voucher_so_id=")
							.append(crs.getString("so_id") + "&customer_id=" + crs.getString("customer_id"))
							.append(" \">List Receipts</a>");

					if (crs.getString("branch_brand_id").equals("153")) {
						Str.append("<br><a href=\"../sales/print-booking-form-harley.jsp?reportfrom=booking-form-harley&so_id=")
								.append(crs.getString("so_id"))
								.append(" \">Print Booking Form</a>");
					}
					Str.append("<br><a href=\"javascript:remote=window.open('salesorder-dash.jsp?so_id=").append(crs.getString("so_id") + "#tabs-7")
							.append("','sodash','');remote.focus();\">").append("Add Registration").append("</a>\n");

					// Str.append("<br><a href=\"veh-salesorder-print.jsp?so_id=").append(crs.getString("so_id"));
					// Str.append("&target=").append(Math.random()).append("\" target=_blank>Print Sales Order</a>");
					if (!crs.getString("vehstock_id").equals("0")) {
						Str.append("<br><a href=\"veh-salesorder-din-update.jsp?so_id=").append(crs.getString("so_id")).append("\">Update DIN</a>");
					}
					if (!crs.getString("so_delivered_date").equals("")) {
						Str.append("<br><a href=\"veh-salesorder-deliverychallan-print1.jsp?brand_id=")
								.append(crs.getString("branch_brand_id")).append("&so_id=").append(crs.getString("so_id"));
						Str.append("\" target=_blank>Print Delivery Challan</a>");
					}

					if (crs.getString("branch_brand_id").equals("102")) {
						Str.append("<br><a href=\"form-21-print.jsp?reportfrom=form-21&reportname=Form-21&brand_id=")
								.append(crs.getString("branch_brand_id")).append("&so_id=").append(crs.getString("so_id"));
						Str.append("\" target=_blank>Print Form-21</a>");
					}
					// Str.append("<br><a href=\"pbf-print.jsp?so_id=").append(crs.getString("so_id"));
					// Str.append("&target=").append(Math.random()).append("\" target=_blank>Print PBF</a>");
					// Str.append("<br><a href=\"psf-print.jsp?so_id=").append(crs.getString("so_id"));
					// Str.append("&target=").append(Math.random()).append("\" target=_blank>Print PSF</a>");
					// }
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Sales Order</div>\n</div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div align=center><br><br><font color=red><b>No Sales Order found!</b></font></div>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	public String VehicleDetails(String comp_id, String customer_id) {
		CachedRowSet crs;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT veh_id, veh_variant_id, veh_emp_id, veh_insuremp_id, veh_branch_id, COALESCE(preownedmodel_name, '') AS preownedmodel_name,"
					+ " veh_chassis_no, veh_kms,"
					+ " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variantname,"
					+ " COALESCE(variant_id, 0) AS variant_id, veh_engine_no, veh_reg_no, veh_lastservice_kms, veh_sale_date,"
					+ " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(contact_id, 0) AS contact_id, veh_so_id, veh_iacs, veh_cal_kms,"
					+ " veh_modelyear, IF(veh_lastservice != 0, veh_lastservice, '') AS veh_lastservice,"
					+ " veh_calservicedate, branch_id, branch_name,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contactname,"
					+ " COALESCE(contact_address, '') AS contact_address,"
					+ " COALESCE(contact_landmark, '') AS contact_landmark,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile1_phonetype_id ), '') AS phonetypemobile1,"
					+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile2_phonetype_id ), '') AS phonetypemobile2,"
					+ " COALESCE(contact_mobile3, '') AS contact_mobile3,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile3_phonetype_id ), '') AS phonetypemobile3,"
					+ " COALESCE(contact_mobile4, '') AS contact_mobile4,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile4_phonetype_id ), '') AS phonetypemobile4,"
					+ " COALESCE(contact_mobile5, '') AS contact_mobile5,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile5_phonetype_id ), '') AS phonetypemobile5,"
					+ " COALESCE(contact_mobile6, '') AS contact_mobile6,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
					+ " WHERE phonetype_id = contact_mobile6_phonetype_id ), '') AS phonetypemobile6,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2, "
					+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM  " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans"
					+ " ON tagtrans_tag_id = tag_id WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

			// CountSql = "SELECT COUNT(DISTINCT(veh_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE 1 = 1 And veh_customer_id = " + customer_id;

			StrSql += SqlJoin;

			// if (all.equals("yes")) {
			// StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_veh\\b",
			// "FROM " + compdb(comp_id) + "axela_service_veh"
			// + " INNER JOIN (SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
			// // + " GROUP BY veh_id"
			// + " ORDER BY veh_id DESC"
			// // + " LIMIT " + (StartRec - 1) + ", " + recperpage + ""
			// + ") AS myresults USING (veh_id)");
			// // StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
			// }
			// else {
			// StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// }
			// StrSql += " GROUP BY veh_id"
			// + " ORDER BY veh_id DESC";
			crs = processQuery(StrSql, 0);
			// int count = StartRec - 1;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Vehicles</div>\n</div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th >ID</th>\n");
				Str.append("<th style=\"width:200px;\">Customer</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th data-hide=\"phone\">Item</th>\n");
				Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Chassis Number</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Engine No.</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Year</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" nowrap>Sale Date</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" nowrap>Last Service </th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Kms</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Cal. Service</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Advisor</th>\n");
				// Str.append("<th data-hide=\"phone,tablet\">Insurance Executive</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("veh_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("veh_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td >").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
					Str.append(crs.getString("veh_id")).append("</a>");
					Str.append("</td>\n<td nowrap>");
					if (!crs.getString("customer_id").equals("0")) {
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
					}

					if (!crs.getString("contactname").equals("")) {
						Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
						Str.append(crs.getString("contactname")).append("</a>");
					}

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M) (" + crs.getString("phonetypemobile1"), crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, " (M) (" + crs.getString("phonetypemobile2") + ")", crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_mobile3").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile3"), 10, " (M) (" + crs.getString("phonetypemobile3") + ")", crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile3"), comp_id));
					}

					if (!crs.getString("contact_mobile4").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile4"), 10, " (M) (" + crs.getString("phonetypemobile4") + ")", crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile4"), comp_id));
					}

					if (!crs.getString("contact_mobile5").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile5"), 10, " (M) (" + crs.getString("phonetypemobile5") + ")", crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile5"), comp_id));
					}

					if (!crs.getString("contact_mobile6").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile6"), 10, " (M) (" + crs.getString("phonetypemobile6") + ")", crs.getString("veh_id")))
								.append(ClickToCall(crs.getString("contact_mobile6"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}

					// Populating Tags in Enquiry list
					Str.append("<br><br>");

					String Tag = crs.getString("tag");
					Tag = ReplaceStr(Tag, "StartColor", "<button class='btn-xs btn-arrow-left' style='top:-16px; background:");
					Tag = ReplaceStr(Tag, "EndColor", " ; color:white'  disabled>&nbsp");
					Tag = ReplaceStr(Tag, "StartName", "");
					Tag = ReplaceStr(Tag, "EndName", "</button>&nbsp&nbsp&nbsp");
					Str.append(Tag);
					// Tags End

					Str.append("</td>\n<td>").append(crs.getString("preownedmodel_name"));
					Str.append("</td>\n<td>");
					if (!crs.getString("variant_id").equals("0")) {
						Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("variant_id"));
						Str.append("\">").append(crs.getString("variantname")).append("</a>");
					}

					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
					Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
					Str.append("</td>\n<td>").append(crs.getString("veh_chassis_no"));
					Str.append("</td>\n<td>").append(crs.getString("veh_engine_no"));
					if (crs.getString("veh_iacs").equals("1")) {
						Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
					}
					Str.append("</td>\n<td>").append(crs.getString("veh_modelyear"));
					Str.append("</td>\n");
					Str.append("<td nowrap>").append(strToShortDate(crs.getString("veh_sale_date")));
					if (!crs.getString("veh_so_id").equals("0")) {
						Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("veh_so_id"));
						Str.append("\">").append("<br>SO ID: " + crs.getString("veh_so_id")).append("</a>");
					}
					// Str.append("<br>SO ID: " + crs.getString("veh_so_id"));
					Str.append("</td>\n");
					Str.append("<td nowrap> ");
					if (!crs.getString("veh_lastservice").equals("")) {
						Str.append(strToShortDate(crs.getString("veh_lastservice"))).append("<br/>");
					}
					Str.append(IndFormat(crs.getString("veh_lastservice_kms"))).append(" Kms");
					Str.append("</td>\n<td nowrap>Last: ").append(IndFormat(crs.getString("veh_kms")));
					// Str.append("<br/>Cal: ").append(IndFormat(crs.getString("veh_cal_kms")));
					Str.append("</td>\n");
					Str.append("<td nowrap> ");
					if (!crs.getString("veh_calservicedate").equals("")) {
						Str.append(strToShortDate(crs.getString("veh_calservicedate"))).append("<br/>");
					}
					Str.append(IndFormat(crs.getString("veh_cal_kms"))).append(" Kms");
					Str.append("<td>");
					if (!crs.getString("veh_emp_id").equals("0")) {
						Str.append(Exename(comp_id, crs.getInt("veh_emp_id")));
					}
					Str.append("</td>\n<td>");
					// if (!crs.getString("veh_insuremp_id").equals("0")) {
					// Str.append(Exename(comp_id, crs.getInt("veh_insuremp_id")));
					// }
					// // Str.append("\n<td align=\"center\" valign=\"top\">").append(crs.getString("branch_name"));
					// // Str.append("</td>\n");
					// Str.append("\n<td>");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td nowrap>");

					Str.append("<a href=\"vehicle-update.jsp?update=yes&veh_id=").append(crs.getString("veh_id")).append("&veh_branch_id=").append(crs.getString("veh_branch_id"))
							.append("\">Update Vehicle</a>");
					Str.append("<br/><a href=\"../service/vehicle-options.jsp?veh_id=").append(crs.getString("veh_id")).append("\">Configure Vehicle</a>");
					Str.append("<br/><a href=\"../service/kms-list.jsp?vehkms_veh_id=").append(crs.getString("veh_id")).append("\">List Kms</a>");
					Str.append("<br/><a href=\"../service/kms-update.jsp?add=yes&vehkms_veh_id=").append(crs.getString("veh_id")).append("\">Add Kms</a>");
					Str.append("<br/><a href=\"../service/jobcard-list.jsp?jc_veh_id=").append(crs.getString("veh_id")).append("\">List Job Cards</a>");
					// Str.append("<br/><a href=\"../insurance/insurance-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("&veh_branch_id=")
					// .append(crs.getString("veh_branch_id")).append("\">Add Insurance</a>");
					Str.append("<br/><a href=\"../service/veh-ownership-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Ownership</a>");
					Str.append("<br/><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("&branch_id=").append(crs.getString("branch_id"))
							.append("\">Add Job Card</a>");
					Str.append("<br/><a href=\"../service/jobcard-quickadd.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Quick Add Job Card</a>");
					Str.append("<br/><a href=\"../customer/customer-contact-update.jsp?Add=yes&customer_id=").append(crs.getString("customer_id")).append("\">Add New Contact</a>");
					Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Ticket</a>");
					// Str.append("<br/><a href=\"../service/pickup-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Pickup</a>");
					// Str.append("<br/><a href=\"../service/booking-list.jsp?booking_veh_id=").append(crs.getString("veh_id")).append("\">List Bookings</a>");
					// Str.append("<br/><a href=\"../service/booking-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Booking</a>");
				}
				Str.append("</td>\n</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div> </div></div></div>");
			}
			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Vehicles</div>\n</div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div align=center><br><br><font color=red><b>No Vehicles found!</b></font></div>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
			// // Add News Letter Link
			// LinkAddPage += "<br/><a href=\"../portal/news-letter-enquiry.jsp?target=vehicles\" target=_blank>Send News Letter</a>";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String JobCardDetails(String comp_id, String customer_id) {
		CachedRowSet crs;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COALESCE(jc_id, 0) AS jc_id,"
					+ " COALESCE(jc_reg_no, '') AS jc_reg_no,"
					+ " COALESCE(jc_emp_id, 0) AS jc_emp_id,"
					+ " COALESCE(jc_location_id, 0) AS jc_location_id,"
					+ " COALESCE(jc_branch_id, 0) AS jc_branch_id,"
					+ " COALESCE(CONCAT('JC', branch_code, jc_no), '') AS jc_no,"
					+ " COALESCE(jc_contact_id, 0) AS jc_contact_id,"
					+ " COALESCE(jc_title, '') AS jc_title,"
					+ " COALESCE(jc_cust_voice, '') AS jc_cust_voice,"
					+ " COALESCE(jc_time_promised, '') AS jc_time_promised,"
					+ " COALESCE(jc_time_ready, '') AS jc_time_ready,"
					+ " COALESCE(jc_netamt, '') AS jc_netamt,"
					+ " COALESCE(jc_totaltax, '') AS jc_totaltax,"
					+ " COALESCE(jc_grandtotal, '') AS jc_grandtotal,"
					+ " COALESCE(jc_ro_no, '') AS jc_ro_no,"
					+ " COALESCE(jc_bill_cash_no, '') AS jc_bill_cash_no,"
					+ " COALESCE(jc_auth, '') AS jc_auth,"
					+ " COALESCE(jc_active, '') AS jc_active,"
					+ " COALESCE(jc_time_out, '') AS jc_time_out,"
					+ " COALESCE(variant_id, '0') AS variant_id,"
					+ " COALESCE(jc_time_in, '') AS jc_time_in,"
					+ " COALESCE(jc_time_posted, '') AS jc_time_posted,"
					+ " COALESCE(branch_id, '') AS branch_id,"
					+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),'') AS branch_name, branch_brand_id,"
					+ " COALESCE (branch_rateclass_id, 0) AS branch_rateclass_id,"
					+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), '') AS emp_name, COALESCE(emp_id, 0) AS emp_id,"
					+ " COALESCE(jcstage_name, '') AS jcstage_name,"
					+ " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variantname,"
					+ " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no,"
					+ " COALESCE(priorityjc_name, '') AS priorityjc_name, COALESCE(jccat_name, '') AS jccat_name,"
					+ " COALESCE(jctype_name, '') AS jctype_name, COALESCE(jc_stage_trigger, '') AS jc_stage_trigger,"
					+ " COALESCE(jc_priority_trigger, '') AS jc_priority_trigger, COALESCE(customer_id, '0') AS customer_id,"
					+ " COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(contact_id, 0) AS contact_id, COALESCE(veh_iacs, '') AS veh_iacs,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1, COALESCE(contact_mobile2, '') AS contact_mobile2,"
					+ " COALESCE(contact_email1, '') AS contact_email1, COALESCE(contact_email2, '') AS contact_email2";

			// CountSql = "SELECT COUNT(DISTINCT(jc_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE 1 = 1 AND customer_id = " + customer_id;
			// SOP("2---------------------------" + SqlJoin);
			// if (!jc_veh_id.equals("0")) {
			// SqlJoin+= " AND jc_veh_id = " + jc_veh_id + "";
			// }

			StrSql += SqlJoin;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Job Cards</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div>");
				Str.append("<table class=\"table table-bordered table-reponsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Job Card</th>\n");
				Str.append("<th style=\"width:200px;\" data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Time</th>\n");
				Str.append("<th data-hide=\"phone\">Voice</th>\n");
				Str.append("<th data-hide=\"phone\">Item</th>\n");
				Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone\">Stage</th>\n");
				Str.append("<th data-hide=\"phone\">Priority</th>\n");
				Str.append("<th data-hide=\"phone\">Type</th>\n");
				Str.append("<th data-hide=\"phone\">Category</th>\n");
				Str.append("<th data-hide=\"phone\">Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Service Advisor</th>\n");
				Str.append("<th data-hide=\"phone\">Branch</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("jc_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("jc_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=\"javascript:remote=window.open('../service/jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','jcdash','');remote.focus();\">");
					Str.append(crs.getString("jc_id")).append("</a></td>\n");
					Str.append("<td>");
					if (!crs.getString("jc_no").equals("")) {
						Str.append(crs.getString("jc_no"));
					}
					if (!crs.getString("jc_no").equals("") && !crs.getString("jc_ro_no").equals("")) {
						Str.append("<br>Ref No: ").append(crs.getString("jc_ro_no"));
					} else if (crs.getString("jc_no").equals("") && !crs.getString("jc_ro_no").equals("")) {
						Str.append("Ref No: ").append(crs.getString("jc_ro_no"));
					}
					Str.append("</td>\n<td>");
					Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','ticketdash','');remote.focus();\">");
					Str.append(crs.getString("jc_title")).append("</a>").append("<br>").append(crs.getString("jc_reg_no"));
					if (crs.getString("jc_active").equals("0")) {
						Str.append("<br><font color=\"red\"><b>[Inactive]</b></font>");
					}

					if (crs.getString("jc_auth").equals("1")) {
						Str.append("<br><font color=\"red\">[Authorized]</font>");
					}

					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("jc_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("jc_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}
					// SOP("time in---" + crs.getString("jc_time_in"));
					Str.append("</td>\n<td nowrap");
					if (!crs.getString("jc_time_in").equals("0")) {
						Str.append(">Time In: ").append(strToLongDate(crs.getString("jc_time_in")));
					}
					Str.append("<br>Promised: ").append(strToLongDate(crs.getString("jc_time_promised")));

					if (!crs.getString("jc_time_ready").equals("")) {
						Str.append("<br>Ready: ").append(strToLongDate(crs.getString("jc_time_ready")));
					}

					if (!crs.getString("jc_time_out").equals("")) {
						Str.append("<br>Time Out: ").append(strToLongDate(crs.getString("jc_time_out")));
					}

					if (!crs.getString("jc_time_posted").equals("")) {
						Str.append("<br>Posted Date: ").append(strToLongDate(crs.getString("jc_time_posted")));
					}
					Str.append("</td>\n<td>").append(crs.getString("jc_cust_voice"));
					Str.append("</td>\n<td>");
					Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("variant_id")).append("\">");
					Str.append(crs.getString("variantname")).append("</a>");
					Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
					if (crs.getString("veh_id").equals("0")) {
						Str.append("<a href=\"../service/vehicle-update.jsp?add=yes&veh_reg_no=").append(crs.getString("jc_reg_no")).append("\">Add Vehicle</a>");
					} else {
						Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
						if (crs.getString("veh_iacs").equals("1")) {
							Str.append("<br><font color=\"red\"><b>IACS</b></font>");
						}
					}
					Str.append("</td>\n<td nowrap>").append(crs.getString("jcstage_name"));
					if (crs.getString("jc_stage_trigger").equals("5")) {
						Str.append("<br><font color=\"red\">Level: ").append(crs.getString("jc_stage_trigger")).append("</font>");
					} else {
						Str.append("<br>Level: ").append(crs.getString("jc_stage_trigger"));
					}
					Str.append("</td>\n<td nowrap>").append(crs.getString("priorityjc_name"));
					if (crs.getString("jc_priority_trigger").equals("5")) {
						Str.append("<br><font color=\"red\">Level: ").append(crs.getString("jc_priority_trigger")).append("</font>");
					} else {
						Str.append("<br>Level: ").append(crs.getString("jc_priority_trigger"));
					}
					Str.append("</td>\n<td>").append(crs.getString("jctype_name"));
					Str.append("</td>\n<td>").append(crs.getString("jccat_name"));
					Str.append("</td>\n<td nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_netamt"))));
					if (!crs.getString("jc_totaltax").equals("0")) {
						Str.append("<br>Service Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_totaltax"))));
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_grandtotal")))).append("</b>");
					Str.append("<br/><br/></td>\n<td>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a>");
					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"jobcard-update.jsp?update=yes&jc_id=").append(crs.getString("jc_id")).append("\">Update Job Card</a>");
					Str.append("<br/><a href=\"jobcard-authorize.jsp?jc_id=").append(crs.getString("jc_id")).append(" \">Authorize</a>");
					Str.append("<br/><a href=\"../service/call-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Call</a>");
					Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Add Ticket</a>");
					Str.append("<br/><a href=\"../accounting/so-update.jsp?add=yes")
							.append("&checkinvoice=yes")
							.append("&vouchertype_id=6&voucherclass_id=6")
							.append("&voucher_jc_id=").append(crs.getString("jc_id"))
							.append("&so_branch_id=").append(crs.getString("jc_branch_id"))
							.append("&dr_voucher_rateclass_id=").append(crs.getString("branch_rateclass_id"))
							.append("&jc_emp_id=").append(crs.getString("jc_emp_id"))
							.append("&vouchertrans_customer_id=").append(crs.getString("customer_id"))
							.append("&span_cont_id=").append(crs.getString("jc_contact_id"))
							.append("&jc_location_id=").append(crs.getString("jc_location_id"))
							.append("\">Add Invoice</a>");

					// if (!crs.getString("jc_time_out").equals("")) {
					// if (Long.parseLong(crs.getString("jc_time_out")) <=
					// Long.parseLong(ToLongDate(kknow()))) {
					// Str.append("<br/><a href=\"../service/jobcard-cust-feedback.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Customer Feedback</a>");
					// }
					// }

					Str.append("<br/><a href=\"jobcard-print-pdf.jsp?jc_id=").append(crs.getString("jc_id"));
					Str.append("&target=").append(Math.random()).append("&dr_report=jobcard-print").append("\" target=_blank>Print Job Card</a>");
					if (crs.getString("branch_brand_id").equals("102")) {
						Str.append("<br><a href=\"jobcard-salesinvoice-print-yamaha.jsp?jc_id=").append(crs.getString("jc_id"));
						Str.append("&target=").append(Math.random()).append("&dr_report=jobcard-salesinvoice-print-yamaha").append("\" target=_blank>Print Job Card Invoice</a>");
					}
					Str.append("<br/><a href=\"jobcard-email.jsp?jc_id=").append(crs.getString("jc_id")).append("\">Email Job Card</a>");
					Str.append("<br/><a href=\"gate-pass-print-pdf.jsp?jc_id=").append(crs.getString("jc_id"));
					Str.append("&target=").append(Math.random()).append("\" target=_blank>Print Gate Pass</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Job Cards</div>\n </div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div align=center><br><br><font color=red><b>No Job cards found!</b></font></div>");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String InsuranceDetails(String comp_id, String customer_id) {
	// CachedRowSet crs;
	// int count = 0;
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, insurpolicy_insurenquiry_id,"
	// + " COALESCE(insurenquiry_id, 0) AS insurenquiry_id,"
	// + " COALESCE(insurenquiry_reg_no, 0) AS insurenquiry_reg_no, insurpolicy_contact_id,"
	// + " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
	// + " insurpolicy_date,"
	// + " COALESCE(policytype_name, '') AS policytype_name,"
	// + " insurpolicy_policy_no, inscomp_name,"
	// + " insurpolicy_premium_amt, insurpolicy_idv_amt, insurpolicy_od_amt, insurpolicy_od_discount, insurpolicy_payout,"
	// + " insurtype_name, insurpolicy_customer_id, insurpolicy_start_date, insurpolicy_end_date, insurpolicy_entry_date, customer_name,"
	// + " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
	// + " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
	// + " COALESCE(preownedmodel_name, '') AS preownedmodel_name, insurpolicy_active,"
	// + " COALESCE(variant_name, '') AS variant_name, insurpolicy_emp_id,"
	// + " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";
	//
	// SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_policy"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = insurpolicy_inscomp_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurpolicy_insurenquiry_id"
	// + " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
	// + " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
	// + " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
	// + " WHERE 1 = 1 AND insurpolicy_customer_id =" + customer_id;
	//
	// StrSql += SqlJoin;
	// crs = processQuery(StrSql, 0);
	// if (crs.isBeforeFirst()) {
	// Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
	// Str.append("<div class=\"caption\" style='float: none'>Insurance</div>\n </div>\n");
	// Str.append("<div class=\"portlet-body portlet-empty\">");
	// Str.append("<div class=\"tab-pane\" id=''>");
	// Str.append("<div>\n");
	// Str.append("<div class=\"  table-bordered\">\n");
	// Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
	// Str.append("<thead>\n");
	// Str.append("<tr>\n");
	// Str.append("<th data-toggle=\"true\">#</th>\n");
	// Str.append("<th >ID</th>\n");
	// Str.append("<th>Insurance Enquiry ID</th>\n");
	// Str.append("<th>Insurance</th>\n");
	// Str.append("<th style=\"width:200px;\" data-hide=\"phone\">Customer</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Type</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Date</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Term</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Amount</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Executive</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
	// Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
	// Str.append("</tr>\n");
	// Str.append("</thead>\n");
	// Str.append("<tbody>\n");
	// while (crs.next()) {
	// count++;
	// Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("insurpolicy_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("insurpolicy_id") + ");'");
	// Str.append(" style='height:200px'>\n");
	// Str.append("<td >").append(count).append("</td>\n");
	// Str.append("<td nowrap>").append(crs.getString("insurpolicy_id")).append("</td>\n");
	// Str.append("<td nowrap>").append("</td>\n");
	// Str.append("<td nowrap>Company: ").append(crs.getString("inscomp_name"));
	// Str.append("<br>Policy: ").append(crs.getString("policytype_name"));
	// Str.append("<br>Policy No.: ").append(crs.getString("insurpolicy_policy_no"));
	// if (crs.getString("insurpolicy_active").equals("0")) {
	// Str.append("<br><font color=\"red\">[Inactive]</font>");
	// }
	// Str.append("</td>\n<td nowrap>");
	// Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("insurpolicy_customer_id")).append("\">");
	// Str.append(crs.getString("customer_name")).append("</a>");
	// Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
	// Str.append(crs.getString("contact_name")).append("</a>");
	// if (!crs.getString("contact_mobile1").equals("")) {
	// Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("insurpolicy_id")))
	// .append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
	// }
	// if (!crs.getString("contact_mobile2").equals("")) {
	// Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("insurpolicy_id")))
	// .append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
	// }
	// if (!crs.getString("contact_email1").equals("")) {
	// Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
	// .append(crs.getString("contact_email1")).append("\">");
	// Str.append(crs.getString("contact_email1")).append("</a></span>");
	// }
	// if (!crs.getString("contact_email2").equals("")) {
	// Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
	// .append(crs.getString("contact_email2")).append("\">");
	// Str.append(crs.getString("contact_email2")).append("</a></span>");
	// }
	// Str.append("</td>\n<td>").append(crs.getString("insurtype_name")).append("</td>\n");
	//
	// if (!crs.getString("insurpolicy_entry_date").equals("")) {
	// Str.append("<td>").append(strToShortDate(crs.getString("insurpolicy_entry_date"))).append("</td>\n");
	// }
	//
	// Str.append("<td nowrap>");
	// if (!crs.getString("insurpolicy_start_date").equals("")) {
	// Str.append(strToShortDate(crs.getString("insurpolicy_start_date"))).append("-").append(strToShortDate(crs.getString("insurpolicy_end_date"))).append(" ");
	// }
	// String startdate = crs.getString("insurpolicy_start_date").substring(0, 8);
	// String enddate = crs.getString("insurpolicy_end_date").substring(0, 8);
	// if (Long.parseLong(enddate) < Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
	// Str.append("<br><font color=\"red\">[Expired]</font>");
	// } else if (Long.parseLong(startdate) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
	// Str.append("<br><font color=\"blue\">[Future Insurance]</font>");
	// }
	// Str.append("</td>\n<td nowrap>");
	// if (!crs.getString("insurpolicy_premium_amt").equals("0")) {
	// Str.append("Premium Amount: ").append(crs.getString("insurpolicy_premium_amt")).append("<br>");
	// }
	// if (!crs.getString("insurpolicy_idv_amt").equals("0")) {
	// Str.append("IDV Amount: ").append(crs.getString("insurpolicy_idv_amt")).append("<br>");
	// }
	// if (!crs.getString("insurpolicy_od_amt").equals("0")) {
	// Str.append("OD Amount: ").append(crs.getString("insurpolicy_od_amt")).append("<br>");
	// }
	// if (!crs.getString("insurpolicy_od_discount").equals("0")) {
	// Str.append("OD Discount: ").append(crs.getString("insurpolicy_od_discount")).append("<br>");
	// }
	// if (!crs.getString("insurpolicy_payout").equals("0")) {
	// Str.append("Payout Amount: ").append(crs.getString("insurpolicy_payout"));
	// }
	// Str.append("</td>\n<td>");
	// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("insurpolicy_emp_id")).append("\">");
	// Str.append(crs.getString("emp_name")).append("</a>");
	// Str.append("</td>\n<td nowrap>");
	// Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
	// Str.append(crs.getString("branch_name")).append("</a>");
	// Str.append("</td>\n<td nowrap>");
	// Str.append("<a href=\"insurance-update.jsp?update=yes&insurpolicy_id=").append(crs.getString("insurpolicy_id"))
	// .append("\">Update Insurance</a>");
	// Str.append("<br><a href=\"insurance-docs-list.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">List Documents</a>");
	// Str.append("</td>\n</tr>\n");
	// }
	// Str.append("</tbody>\n");
	// Str.append("</table>\n");
	// Str.append("</div>\n");
	// Str.append("</div>\n </div>\n </div>\n </div>\n");
	// crs.close();
	// // Add News Letter Link
	// // LinkAddPage += "<br/><a href=\"../portal/news-letter-enquiry.jsp?target=insurenquiryicles\" target=_blank>Send News Letter</a>";
	// } else {
	// Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
	// Str.append("<div class=\"caption\" style='float: none'>Insurance</div>\n </div>\n");
	// Str.append("<div class=\"portlet-body portlet-empty\">");
	// Str.append("<div class=\"tab-pane\" id=''>");
	// Str.append("<br><br><br><br><font color=red><b><center>No Insurance Enquiry(s) found!</center></b></font><br><br>");
	// Str.append("</div>\n </div>\n </div>\n </div>\n");
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String TicketDetails(String comp_id, String customer_id) {
		CachedRowSet crs = null;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("emp_branch_id=====222==" + emp_branch_id);
			// to know no of records depending on search
			StrSql = "SELECT ticket_id, ticket_subject, ticket_desc, ticket_customer_id, ticket_contact_id,"
					+ " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(ticketcat_name, '') AS ticketcat_name,"
					+ " COALESCE(tickettype_name, '') AS tickettype_name,"
					+ " COALESCE(branch_id, 0) AS branch_id, "
					+ " COALESCE(branch_name, '') AS branch_name,"
					+ " COALESCE (ticket_branch_id, '') AS ticket_branch_id,"
					+ " COALESCE (branch_name,'') AS ticketbranch_name,"
					+ " COALESCE(branch_brand_id,'') AS branch_brand_id,"

					// for ref column
					+ " ticket_enquiry_id, ticket_preowned_id, ticket_preowned_crm_id,"
					+ " ticket_so_id, ticket_crm_id, ticket_veh_id, ticket_jc_id, ticket_jcpsf_id,"
					+ " priorityticket_name, priorityticket_id,"
					+ " ticket_trigger,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name, ticketstatus_id, ticketstatus_name,"
					+ " COALESCE(contact_id,0) AS contact_id,"
					+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
					+ " COALESCE(contact_mobile2,'') AS contact_mobile2,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2, "
					+ " emp_id, CONCAT(emp_name, ' (',emp_ref_no, ')') AS emp_name, ticket_dept_name,"
					+ " ticket_report_time, ticket_due_time, ticket_closed_time";

			// CountSql = " SELECT COUNT(distinct(ticket_id)) ";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id";
			// if ((emp_all_exe.equals("1") && !emp_id.equals("1"))) {
			// if (!emp_branch_id.equals("0")) {
			// SqlJoin += " AND emp_branch_id = " + emp_branch_id;
			// }
			// }
			SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id";

			SqlJoin += " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id";

			// + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"

			SqlJoin += " WHERE 1 = 1 AND ticket_customer_id= " + customer_id
					+ BranchAccess.replace("branch_id", "ticket_branch_id");

			StrSql = StrSql + SqlJoin;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Tickets</div>\n </div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Ticket</th>\n");
				Str.append("<th style=\"width:200px;\">Customer</th>\n");
				// Str.append("<th data-hide=\"phone\">").append("Reference").append("</th>\n");
				Str.append("<th data-hide=\"phone\">Report Time</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Due Time</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Priority</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Level</th>\n");
				if (config_service_ticket_cat.equals("1")) {
					Str.append("<th data-hide=\"phone,tablet\">Category</th>\n");
				}
				if (config_service_ticket_type.equals("1")) {
					Str.append("<th data-hide=\"phone,tablet\">Type</th>\n");
				}
				Str.append("<th data-hide=\"phone,tablet\">Department</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Executive</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					ticketstatus_id = crs.getString("ticketstatus_id");
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("ticket_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("ticket_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><a href=\"javascript:remote=window.open('ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("','ticketdash','');remote.focus();\">");
					Str.append(crs.getString("ticket_id")).append("</td>\n");
					Str.append("<td>");

					// Str.append("<a href=\"ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("\">");
					Str.append(crs.getString("ticket_subject")).append("</a></br>");
					if (!crs.getString("ticket_enquiry_id").equals("") && !crs.getString("ticket_enquiry_id").equals("0")) {
						Str.append("Enquiry ID: ");
						Str.append("<a href=\"../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("ticket_enquiry_id")).append(" \">");
						Str.append(crs.getString("ticket_enquiry_id")).append("</a></br>");
					}
					if (!crs.getString("ticket_preowned_id").equals("") && !crs.getString("ticket_preowned_id").equals("0")) {
						Str.append("Pre-Owned ID: ");
						Str.append("<a href=\"../preowned/preowned-list.jsp?preowned_id=").append(crs.getString("ticket_preowned_id")).append(" \">");
						Str.append(crs.getString("ticket_preowned_id")).append("</a></br>");
					}
					if (!crs.getString("ticket_so_id").equals("") && !crs.getString("ticket_so_id").equals("0")) {
						Str.append("SO ID: ");
						Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("ticket_so_id")).append(" \">");
						Str.append(crs.getString("ticket_so_id")).append("</a></br>");
					}
					if (!crs.getString("ticket_crm_id").equals("") && !crs.getString("ticket_crm_id").equals("0")) {
						Str.append("CRM ID: ");
						Str.append("<a href=\"../sales/enquiry-dash.jsp?enquiry_id=").append(crs.getString("ticket_enquiry_id")).append("#tabs-3").append(" \">");
						Str.append(crs.getString("ticket_crm_id")).append("</a></br>");
					}

					if (!crs.getString("ticket_preowned_id").equals("") && !crs.getString("ticket_preowned_id").equals("0")) {
						Str.append("Preowned CRM ID: ");
						Str.append("<a href=\"../preowned/preowned-dash-crmfollowup.jsp?preowned_id=").append(crs.getString("ticket_preowned_id")).append(" \">");
						Str.append(crs.getString("ticket_preowned_crm_id")).append("</a></br>");
					}

					if (!crs.getString("ticket_veh_id").equals("") && !crs.getString("ticket_veh_id").equals("0")) {
						Str.append("Vehicle ID: ");
						Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=").append(crs.getString("ticket_veh_id")).append(" \">");
						Str.append(crs.getString("ticket_veh_id")).append("</a></br>");
					}
					if (!crs.getString("ticket_jc_id").equals("") && !crs.getString("ticket_jc_id").equals("0")) {
						Str.append("JC ID: ");
						Str.append("<a href=\"../service/jobcard-list.jsp?jc_id=").append(crs.getString("ticket_jc_id")).append(" \">");
						Str.append(crs.getString("ticket_jc_id")).append("</a></br>");
					}
					if (!crs.getString("ticket_jcpsf_id").equals("") && !crs.getString("ticket_jcpsf_id").equals("0")) {
						Str.append("JCPSF ID: ");
						Str.append("<a href=\"../service/jobcard-dash.jsp?jc_id=").append(crs.getString("ticket_jc_id")).append("#tabs-9").append(" \">");
						Str.append(crs.getString("ticket_jcpsf_id")).append("</a></br>");
					}
					Str.append("</td>");

					Str.append("<td>");
					if (!crs.getString("contact_id").equals("0")) {
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">");
						Str.append("").append(crs.getString("customer_name")).append("</a><br>");
						Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("ticket_contact_id")).append(" \">");
						Str.append("").append(crs.getString("contact_name")).append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("ticket_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("ticket_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("ticket_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("ticket_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email2")).append("\">");
							Str.append(crs.getString("contact_email2")).append("</a></span>");
						}
					}
					Str.append("</td>");
					Str.append("<td align=\"center\">").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td>\n");
					Str.append("<td align=\"center\">");
					if (!crs.getString("ticket_closed_time").equals("")) {
						closetime = crs.getLong("ticket_closed_time");
					} else {
						closetime = Long.parseLong(ToLongDate(kknow()));
					}
					if (!crs.getString("ticket_due_time").equals("")) {
						if (closetime >= Long.parseLong(crs.getString("ticket_due_time"))) {
							Str.append("<font color=#ff0000>").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
						} else {
							Str.append("<font color=blue>").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
						}
					}
					Str.append("</td>\n");
					if (ticketstatus_id.equals("3")) {
						Str.append("<td><font color=\"red\">").append(crs.getString("ticketstatus_name")).append("</font></td>\n");
					} else {
						Str.append("<td >").append(crs.getString("ticketstatus_name")).append("</td>\n");
					}
					Str.append("<td nowrap>").append(crs.getString("priorityticket_name")).append("</td>\n");
					Str.append("<td nowrap>").append(crs.getString("ticket_trigger")).append("</td>\n");
					if (config_service_ticket_cat.equals("1")) {
						Str.append("<td nowrap>").append(crs.getString("ticketcat_name")).append("</td>\n");
					}
					if (config_service_ticket_type.equals("1")) {
						Str.append("<td  nowrap>").append(crs.getString("tickettype_name")).append("</td>\n");
					}
					Str.append("<td>").append(crs.getString("ticket_dept_name")).append("</td>\n");
					Str.append("<td>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td>");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("ticket_branch_id")).append("\">").append(crs.getString("ticketbranch_name"))
							.append("</a></td>");
					Str.append("<td nowrap>");
					Str.append("<a href=\"ticket-update.jsp?update=yes&ticket_id=").append(crs.getInt("ticket_id")).append(" \">Update Ticket</a><br>"
							+ "<a href=\"ticket-summary-print.jsp?ticket_id=").append(crs.getInt("ticket_id")).append(" \">Print Ticket</a><br>");

					if ((!crs.getString("ticket_jc_id").equals("0")) && !crs.getString("ticket_jcpsf_id").equals("0")) {
						if (crs.getString("branch_brand_id").equals("2")) {
							Str.append("<a href=\"ticket-maruti-print.jsp?reportfrom=ticket-maruti-print&ticket_id=");
						}
						else {
							Str.append("<a href=\"ticket-general-print.jsp?reportfrom=ticket-general-print&ticket_id=");
						}
						Str.append(crs.getInt("ticket_id")).append(" \"> PSF Ticket Print </a><br>");
						Str.append("<a href=\"ticket-maruti-sa-homevisit-print.jsp?reportfrom=ticket-maruti-sa-homevisit-print&ticket_id=");
						Str.append(crs.getInt("ticket_id")).append(" \">Print Service Advisor Home Visit </a><br>");
					}

					Str.append("</td>");
					Str.append("</tr>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n </div>\n </div>\n </div>\n </div>\n");
				crs.close();

			} else {
				Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
				Str.append("<div class=\"caption\" style='float: none'>Tickets</div>\n </div>\n");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=''>");
				Str.append("<br><br><br><br><font color=red><b><center>No Ticket(s) found!</center></b></font><br><br>");
				Str.append("</div>\n </div>\n </div>\n </div>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String CouponDetails(String comp_id, String customer_id) {
		StringBuilder Str = new StringBuilder();
		String coupon = "0";
		if (coupon.equals("1")) {

		}
		else {
			Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
			Str.append("<div class=\"caption\" style='float: none'>Coupon</div>\n </div>\n");
			Str.append("<div class=\"portlet-body portlet-empty\">");
			Str.append("<div class=\"tab-pane\" id=''>");
			Str.append("<br><br><br><br><font color=red><b><center>No Coupon(s) found!</center></b></font><br><br>");
			Str.append("</div>\n </div>\n </div>\n </div>\n");
		}
		return Str.toString();
	}
	public String InvoiceDetails(String customer_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Invoice</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");
		int count = 0;
		StrSql = "SELECT voucher_id, voucher_jc_id, voucher_invoice_id, voucher_branch_id, voucher_amount,"
				+ " CONCAT( 'INV', branch_code, voucher_no ) AS voucher_no, voucher_date,"
				+ " customer_id, customer_name, vouchertrans_netprice, vouchertrans_taxamount, voucher_authorize,"
				+ " voucher_quote_id, voucher_active, voucher_entry_id, voucher_entry_date,"
				+ "	vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name, emp_id, branch_name,"
				+ " GROUP_CONCAT(item_name SEPARATOR '<br>') AS items"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " AND vouchertrans_rowcount != 0"
				+ "	AND vouchertrans_tax =0"
				+ " AND vouchertrans_discount = 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ "	INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE"
				+ " voucher_customer_id = " + customer_id
				+ " AND voucher_vouchertype_id = 6"
				+ " GROUP BY  voucher_id"
				+ " ORDER BY voucher_id DESC";
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Items</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				String quotecheck = "";
				while (crs.next()) {
					count++;
					// jc_desc = crs.getString("so_desc");
					Str.append("<tr>\n<td>").append(count).append("</td>\n");
					Str.append("<td><a href=/axelaauto/accounting/voucher-list.jsp?vouchertype_id=6&voucherclass_id=6&voucher_id=");
					Str.append(crs.getInt("voucher_id")).append(">").append(crs.getString("voucher_id")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td>").append(crs.getString("voucher_no"));
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					Str.append("</td>\n<td><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name"));
					Str.append("</a></td>\n<td>").append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n<td>").append(crs.getString("items"));
					Str.append("</td>\n<td>Net Total: ");
					Str.append(IndDecimalFormat(crs.getString("vouchertrans_netprice")));
					if (!crs.getString("vouchertrans_taxamount").equals("0")) {
						Str.append("<br>Tax: ").append(IndDecimalFormat(crs.getString("vouchertrans_taxamount"))).append("</b>");
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(crs.getString("voucher_amount")));
					Str.append("</b><br></td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					if (!crs.getString("voucher_quote_id").equals("0")) {
						quotecheck = "&quote_id=" + crs.getString("voucher_quote_id");
					}
					Str.append("<td><a href=\"../accounting/so-update.jsp?update=yes");
					Str.append("&voucher_id=").append(crs.getString("voucher_id"));
					Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
					Str.append("\">Update Invoice</a><br>");
					Str.append("<a href=\"../accounting/voucher-authorize.jsp?voucher_id=");
					Str.append(crs.getString("voucher_id")).append("&voucherclass_id=6");
					Str.append("&vouchertype_id=6").append("&voucher_jc_id=").append(crs.getString("voucher_jc_id"))
							.append("\">Authorize</a><br>");
					Str.append("<a href=\"../accounting/voucher-list.jsp?voucher_invoice_id=");
					Str.append(crs.getString("voucher_id")).append("&voucherclass_id=9");
					Str.append("&vouchertype_id=9").append("\">List Receipts</a>");
					Str.append("<br><a href=\"../accounting/receipt-update.jsp?add=yes&ledger=");
					Str.append(crs.getString("customer_id")).append("&voucherclass_id=9");
					Str.append("&vouchertype_id=9").append("&voucher_invoice_id=");
					Str.append(crs.getString("voucher_id")).append("&voucher_jc_id=");
					// Str.append(jc_id).append("\">Add Receipt</a>");
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("</br><a target='_blank' href=\"../accounting/" + crs.getString("voucherclass_file"));
						Str.append("-print.jsp?voucher_id=").append(crs.getString("voucher_id")).append("&voucherclass_id=");
						Str.append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("&dr_report=");
						Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print");
						Str.append("&dr_format=").append("pdf").append("\">Print " + crs.getString("vouchertype_name")).append("</a>");
					}
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
				Str.append("</tbody>\n");
			} else {
				Str.append("<br><br><center><font color=red><b>No Invoice(s) found!</b></font></center>");
			}
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ReceiptDetails(String customer_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Receipt</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");

		int count = 0;
		StrSql = "SELECT voucher_id, voucher_jc_id, voucher_branch_id,"
				+ " CONCAT( 'RCT', branch_code, voucher_no ) AS voucher_no,"
				+ " voucher_date, customer_id, customer_name, voucher_amount,"
				+ " COALESCE(voucher_invoice_id, 0) AS voucher_invoice_id,"
				+ " voucher_active, voucher_entry_id, voucher_entry_date,"
				+ "	vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
				+ " emp_id, branch_name"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ "	INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE voucher_customer_id = " + customer_id
				+ "	AND voucher_vouchertype_id = 9"
				+ " GROUP BY voucher_id"
				+ " ORDER BY voucher_id DESC";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th data-hide=\"phone\">Invoice</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					count++;
					Str.append("<tr>\n<td valign=top align=center>").append(count);
					Str.append("</td>\n<td valign=top align=center><a href=../accounting/voucher-list.jsp?voucher_id=1096&voucherclass_id=9&vouchertype_id=9&voucher_id=");
					Str.append(crs.getInt("voucher_id")).append(">").append(crs.getString("voucher_id"));
					Str.append("</a></td>\n<td valign=top align=center>").append(crs.getString("voucher_no"));
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td valign=top align=left>");
					if (!crs.getString("voucher_invoice_id").equals("0")) {
						Str.append("<a href=../invoice/invoice-list.jsp?invoice_id=").append(crs.getInt("voucher_invoice_id"));
						Str.append(">Invoice ID: ").append(crs.getString("voucher_invoice_id")).append("</a>");
					} else {
						Str.append("");
					}
					Str.append("</td>\n<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name"));
					Str.append("</a></td>\n<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n<td valign=top align=right>").append(IndDecimalFormat(crs.getString("voucher_amount")));
					Str.append("</td>\n<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name"));
					Str.append("</a></td>\n<td valign=top align=left nowrap><a href=\"../accounting/receipt-update.jsp?update=yes&voucher_id=");
					Str.append(crs.getString("voucher_id")).append("&voucherclass_id=9").append("&vouchertype_id=9");
					Str.append(" \" target=_parent>Update Receipt</a>");
					Str.append("</br><a target='_blank' href=\"../accounting/" + crs.getString("voucherclass_file"));
					Str.append("-print.jsp?voucher_id=").append(crs.getString("voucher_id")).append("&voucherclass_id=");
					Str.append(crs.getString("voucherclass_id"));
					Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id")).append("&dr_report=");
					Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print");
					Str.append("&dr_format=").append("pdf").append("\">Print " + crs.getString("vouchertype_name")).append("</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><center><font color=red><b>No Receipt(s) found!</b></font></center>");
			}
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_customer_soe, config_customer_sob, config_customer_dupnames, config_service_ticket_cat, config_service_ticket_type"
				+ " FROM " + compdb(comp_id) + "axela_config";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_customer_soe = crs.getString("config_customer_soe");
				config_customer_sob = crs.getString("config_customer_sob");
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
