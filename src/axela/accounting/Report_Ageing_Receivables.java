// smitha nag june 6 2013
package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ageing_Receivables extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";

	public String customer_id = "";
	public String start_date = "";

	public String startdate = "";
	public String go = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public String voucherclass_id = "6";
	public String ageing_id = "1";
	public String ageing_bygroup = "0";
	public Double oppbalance = 0.00;
	public Double pending = 0.00, pending1 = 0.00, pending2 = 0.00,
			pending3 = 0.00;
	public Double pending4 = 0.00, pending5 = 0.00;
	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			// comp_id = "2017";
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = DateToShortDate(kknow());
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = Listdata();
					}
				} else {
					StrHTML = Listdata();
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (!customer_id.equals("") && !ageing_bygroup.equals("0")) {
			msg += "<br>Ageing can't be seen with both Ledger and Group wise!";;
		}
		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg = msg + "<br>Enter Valid Date!";
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_id = PadQuotes(request.getParameter("accountingcustomer"));
		startdate = PadQuotes(request.getParameter("txt_date"));
		ageing_id = CNumeric(PadQuotes(request.getParameter("dr_aging")));
		ageing_bygroup = CheckBoxValue(PadQuotes(request
				.getParameter("chk_bygroup")));
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		if (ageing_bygroup.equals("1")) {
			Str.append(AgeingByGroup());
		} else {
			Str.append(Ageing());
		}
		return Str.toString();
	}

	public String Ageing() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		StrSql = "SELECT voucher_id, voucher_date, voucher_no, voucher_ref_no,"
				+ " vouchertype_id, vouchertype_name, vouchertype_prefix, vouchertype_suffix, customer_id, customer_name, "
				+ " customer_id, customer_name,"
				+ " title_desc, contact_id, contact_fname, contact_lname, contact_mobile1, contact_mobile2,"
				+ " contact_phone1, contact_phone2,"
				+ " contact_email1, contact_email2,"
				+ " voucher_amount, voucher_delivery_date,"
				+ " @netdue:=(voucher_amount-COALESCE((SELECT SUM(voucherbal_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal WHERE voucherbal_trans_id = voucher_id),0)) AS netdue,"
				+ " IF (SUBSTR(voucher_delivery_date, 1,8) <= SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 29, 0, 0, 0))
				+ ",1,8), @netdue, '') AS '1-30Days',"
				+ " IF (SUBSTR(voucher_delivery_date, 1,8) > SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 30, 0, 0, 0))
				+ ",1,8) AND SUBSTR(voucher_delivery_date, 1,8) <= SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 59, 0, 0, 0))
				+ ",1,8), @netdue, '') AS '>30-60Days',"
				+ " IF (SUBSTR(voucher_delivery_date, 1,8) > SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 60, 0, 0, 0))
				+ ",1,8) AND SUBSTR(voucher_delivery_date, 1,8) <= SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 89, 0, 0, 0))
				+ ",1,8), @netdue, '') AS '>60-90Days',"
				+ " IF (SUBSTR(voucher_delivery_date, 1,8) > SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 90, 0, 0, 0))
				+ ",1,8) AND SUBSTR(voucher_delivery_date, 1,8) <= SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 119, 0, 0, 0))
				+ ",1,8), @netdue, '') AS '>90-120Days',"
				+ " IF (SUBSTR(voucher_delivery_date, 1,8) > SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 120, 0, 0, 0))
				+ ",1,8) , @netdue, '') AS '>120Days'"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				// + " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " WHERE 1=1"
				+ " AND vouchertype_voucherclass_id = " + voucherclass_id;
		if (!customer_id.equals("") && !customer_id.equals("0")) {
			StrSql += " AND voucher_customer_id = " + customer_id;
		}
		StrSql += " AND voucher_active = 1"
				+ " GROUP BY voucher_id"
				+ " HAVING netdue > 0"
				+ " ORDER BY";
		if (ageing_id.equals("1")) {
			StrSql += " voucher_date,";
		} else if (ageing_id.equals("2")) {
			StrSql += " voucher_payment_date,";
		} else {
			StrSql += " netdue desc,";
		}
		StrSql += " voucher_id"
				+ " LIMIT 100";
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th >ID</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Ref No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><span class=\"footable-toggle\"></span>Ledger</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Opening<br>Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pending<br>Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&lt;30 days</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">30 to 60 days</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">60 to 90 days</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">90 to 120 days</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">>120 days</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					oppbalance += Double.parseDouble(crs.getString("voucher_amount"));
					pending += Double.parseDouble(crs.getString("netdue"));
					pending1 += Double.parseDouble(CNumeric(crs.getString("1-30Days")));
					pending2 += Double.parseDouble(CNumeric(crs.getString(">30-60Days")));
					pending3 += Double.parseDouble(CNumeric(crs.getString(">60-90Days")));
					pending4 += Double.parseDouble(CNumeric(crs.getString(">90-120Days")));
					pending5 += Double.parseDouble(CNumeric(crs.getString(">120Days")));
					Str.append("<tbody>\n");
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("voucher_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("voucher_id") + ");'");
					Str.append("style='height:200px'>\n");
					Str.append("<td align='center' valign='top'>")
							.append(count).append("</td>\n");
					Str.append("<td align='center' valign='top'>").append(
							strToShortDate((crs.getString("voucher_date")))).append("</td>\n");
					Str.append("<td align='center' valign='top'>").append(
							"<a href='../accounting/voucher-list.jsp").append(
							"?voucher_id=" + crs.getString("voucher_id") + "&vouchertype_id="
									+ crs.getString("vouchertype_id")).append("'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>\n");
					Str.append("<td align='center' valign='top'>").append(
							crs.getString("vouchertype_prefix") + crs.getString("voucher_no")
									+ crs.getString("vouchertype_suffix")).append("</td>\n");
					Str.append("<td align='center' valign='top'>").append(
							crs.getString("voucher_ref_no")).append("</td>\n");
					Str.append("<td align='left' valign='top'>").append(
							"<a href='../accounting/ledger-list.jsp?customer_id="
									+ crs.getString("customer_id") + "'>")
							.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td align='left' valign='top'>");
					Str.append(DisplayCustomer(
							crs.getString("voucher_id"),
							crs.getString("customer_id"),
							crs.getString("customer_name"),
							crs.getString("contact_id"),
							crs.getString("title_desc"),
							crs.getString("contact_fname"),
							crs.getString("contact_lname"),
							crs.getString("contact_mobile1"),
							crs.getString("contact_mobile2"),
							crs.getString("contact_phone1"),
							crs.getString("contact_phone2"),
							crs.getString("contact_email1"),
							crs.getString("contact_email2"), comp_id));
					Str.append("</td>");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString("voucher_amount")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString("netdue")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString("1-30Days")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString(">30-60Days")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString(">60-90Days")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString(">90-120Days")).append("</td>\n");
					Str.append("<td align='right' valign='top'>").append(
							crs.getString(">120Days")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'><b>").append("Total:").append(
						"</b></td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append(
						"</td>\n");
				Str.append("<td align='right'><b>").append(
						deci.format(oppbalance)).append("</b></td>\n");
				Str.append("<td align='right'><b>")
						.append(deci.format(pending)).append("</b></td>\n");
				if (pending1 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending1)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				if (pending2 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending2)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				if (pending3 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending3)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				if (pending4 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending4)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				if (pending5 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending5)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				Str.append("</tr></tbody>");
				Str.append("</table>");
				Str.append("</div>");
				crs.close();
			} else {
				Str.append("<br><br><br><br><center><font color=red><b>No Record(s) Found!</b></font></center><br><br>");
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String AgeingByGroup() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		String strinvoicedue = "SELECT"
				+ " COALESCE(SUM(invoicedue.voucher_amount),0)-COALESCE(SUM(voucherbal_amount),0)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher invoicedue\n"
				+ " LEFT join " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_trans_id = invoicedue.voucher_id"
				+ " WHERE 1=1\n"
				+ " AND invoicedue.voucher_customer_id = invoice.voucher_customer_id AND invoicedue.voucher_vouchertype_id = invoice.voucher_vouchertype_id ";
		StrSql = "SELECT voucher_id, voucher_customer_id, customer_id, customer_name,"
				+ " customer_id, customer_name,"
				+ " title_desc, contact_id, contact_fname, contact_lname, contact_mobile1, contact_mobile2,"
				+ " contact_phone1, contact_phone2,"
				+ " contact_email1, contact_email2,"
				+ " SUM(voucher_amount) AS openbalance, "
				+ " @netdue:=SUM(voucher_amount)-COALESCE((SELECT SUM(voucherbal_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher invtrans ON invtrans.voucher_id = voucherbal_trans_id"
				+ " WHERE invtrans.voucher_customer_id = invoice.voucher_customer_id),0) AS netdue,"
				+ " ("
				+ strinvoicedue
				+ ""
				+ " AND SUBSTR(voucher_payment_date,1,8)<=SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 30, 0, 0, 0))
				+ ",1,8)"
				+ " ) AS '1-30days',"
				+ " ("
				+ strinvoicedue
				+ ""
				+ " AND SUBSTR(voucher_payment_date,1,8)>SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 30, 0, 0, 0))
				+ ",1,8)"
				+ " AND SUBSTR(voucher_payment_date,1,8)<=SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 60, 0, 0, 0))
				+ ",1,8)) AS '>30-60days',"
				+ " ("
				+ strinvoicedue
				+ ""
				+ " AND SUBSTR(voucher_payment_date,1,8)>SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 60, 0, 0, 0))
				+ ",1,8)"
				+ " AND SUBSTR(voucher_payment_date,1,8)<=SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 90, 0, 0, 0))
				+ ",1,8)) AS '>60-90days',"
				+ " ("
				+ strinvoicedue
				+ ""
				+ " AND SUBSTR(voucher_payment_date,1,8)>SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 90, 0, 0, 0))
				+ ",1,8)"
				+ " AND SUBSTR(voucher_payment_date,1,8)<=SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 120, 0, 0, 0))
				+ ",1,8)) AS '>90-120days',"
				+ " ("
				+ strinvoicedue
				+ ""
				+ " AND SUBSTR(voucher_payment_date,1,8)>SUBSTR("
				+ ConvertShortDateToStr(AddDayMonthYear(startdate, 120, 0, 0, 0))
				+ ",1,8)) AS '>120days'"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher invoice"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = voucher_customer_id"
				// + " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = voucher_customer_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact on contact_id = voucher_contact_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " WHERE 1=1"
				+ " AND vouchertype_voucherclass_id = " + voucherclass_id
				+ " AND voucher_active = 1"
				+ " GROUP BY voucher_customer_id"
				+ " HAVING netdue > 0"
				+ " ORDER BY";
		if (ageing_id.equals("1")) {
			StrSql += " voucher_date,";
		} else if (ageing_id.equals("2")) {
			StrSql += " voucher_payment_date,";
		} else {
			StrSql += " netdue desc,";
		}
		StrSql += " voucher_customer_id";
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Bills To Make</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Pending<br>Amount</th>\n");
				Str.append("<th data-hide=\"phone\"><30 days</th>\n");
				Str.append("<th data-hide=\"phone\">30 to 60 days</th>\n");
				Str.append("<th data-hide=\"phone\">60 to 90 days</th>\n");
				Str.append("<th data-hide=\"phone\">90 to 120 days</th>\n");
				Str.append("<th data-hide=\"phone\">>120 days</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					pending += Double.parseDouble(crs.getString("netdue"));
					pending1 += Double.parseDouble(crs.getString("1-30Days"));
					pending2 += Double.parseDouble(crs.getString(">30-60Days"));
					pending3 += Double.parseDouble(crs.getString(">60-90Days"));
					pending4 += Double.parseDouble(crs.getString(">90-120Days"));
					pending5 += Double.parseDouble(crs.getString(">120Days"));
					Str.append("<tbody>\n");
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("voucher_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("voucher_id") + ");' style='height:200px'");
					Str.append("<td align='center' valign='top'>")
							.append(count).append("</td>\n");
					Str.append("<td align='left' valign='top'>").append(
							"<a href='../accounting/ledger-list.jsp?customer_id="
									+ crs.getString("customer_id") + "'>")
							.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td align='left' valign='top'>");
					Str.append(DisplayCustomer(
							crs.getString("voucher_id"),
							crs.getString("customer_id"),
							crs.getString("customer_name"),
							crs.getString("contact_id"),
							crs.getString("title_desc"),
							crs.getString("contact_fname"),
							crs.getString("contact_lname"),
							crs.getString("contact_mobile1"),
							crs.getString("contact_mobile2"),
							crs.getString("contact_phone1"),
							crs.getString("contact_phone2"),
							crs.getString("contact_email1"),
							crs.getString("contact_email2"), comp_id));
					Str.append("</td>");
					if (crs.getDouble("netdue") != 0) {
						Str.append("<td align='right' valign='top'>").append(
								crs.getString("netdue")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					if (crs.getDouble("1-30Days") != 0) {
						Str.append("<td align='right' valign='top'>")
								.append(crs.getString("1-30Days")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					if (crs.getDouble(">30-60Days") != 0) {
						Str.append("<td align='right' valign='top'>")
								.append(crs.getString(">30-60Days")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					if (crs.getDouble(">60-90Days") != 0) {
						Str.append("<td align='right' valign='top'>")
								.append(crs.getString(">60-90Days")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					if (crs.getDouble(">90-120Days") != 0) {
						Str.append("<td align='right' valign='top'>")
								.append(crs.getString(">90-120Days")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					if (crs.getDouble(">120Days") != 0) {
						Str.append("<td align='right' valign='top'>")
								.append(crs.getString(">120Days")).append("</td>\n");
					} else {
						Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					}
					Str.append("</tr>\n");
				}
				Str.append("<tr>");
				Str.append("<td colspan='3' align='right'><b>")
						.append("Total:").append("</b></td>\n");
				Str.append("<td align='right'><b>")
						.append(deci.format(pending)).append("</b></td>\n");
				if (pending1 != 0) {
					Str.append("<td align='right'><b>")
							.append(deci.format(pending1)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;").append("</b></td>\n");
				}
				if (pending2 != 0) {
					Str.append("<td align='right'><b>")
							.append(deci.format(pending2)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;").append("</b></td>\n");
				}
				if (pending3 != 0) {
					Str.append("<td align='right'><b>")
							.append(deci.format(pending3)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;").append("</b></td>\n");
				}
				if (pending4 != 0) {
					Str.append("<td align='right'><b>")
							.append(deci.format(pending4)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;").append("</b></td>\n");
				}
				if (pending5 != 0) {
					Str.append("<td align='right'><b>").append(
							deci.format(pending5)).append("</b></td>\n");
				} else {
					Str.append("<td align='right'><b>").append("&nbsp;")
							.append("</b></td>\n");
				}
				Str.append("</tr></tbody>");
				Str.append("</table>");
				Str.append("</div>");
				crs.close();
			} else {
				Str.append("<br><br><br><br><center><font color=red><b>No Record(s) Found!</b></font></center><br><br>");
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}

		return Str.toString();
	}

	public String PopulateAgeing() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value='0'>Select</option>");
		Str.append("<option value='1'").append(Selectdrop(1, ageing_id))
				.append(">Bill Date</option>");
		Str.append("<option value='2'").append(Selectdrop(2, ageing_id))
				.append(">Due Date</option>");
		Str.append("<option value='3'").append(Selectdrop(3, ageing_id))
				.append(">Pending Amount</option>");
		return Str.toString();
	}
}
