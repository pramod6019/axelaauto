package axela.invoice;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String date = "";
	DecimalFormat deci = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_quote_access, emp_voucher_access, emp_receipt_access, emp_payment_access", request, response);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				date = ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -365, 0, 0));
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String TopQuotes() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, sum(quote_grandtotal)  AS quoteamount "
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote on quote_customer_id = customer_id"
					+ " WHERE customer_active=1 and quote_active=1 and substr(quote_date,1,8) > substr('" + date + "',1,8) "
					+ ExeAccess.replace("emp_id", "quote_customer_id") + ""
					+ BranchAccess.replace("branch_id", "quote_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY quoteamount desc "
					+ " limit 10";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../invoice/quote-list.jsp?customer_id=").append(crs.getInt("customer_id")).append("\">").append(crs.getString("customer_name"))
							.append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("quoteamount"))).append("</td>\n");
					Str.append("</tr>");
				}
				crs.close();
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Top Quotes!<br><br><br><br><br><br></td></tr>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String TopInvoices() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, sum(voucher_amount)  AS voucheramount "
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " WHERE customer_active=1 and voucher_active=1 and substr(voucher_date,1,8) > substr('" + date + "',1,8)"
					+ " AND vouchertype_id = 6 AND vouchertype_voucherclass_id = 6 "
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ BranchAccess.replace("branch_id", "voucher_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY voucheramount desc "
					+ " limit 10";
			// SOP("StrSql=TopInvoices===" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../accounting/voucher-list.jsp?voucherclass_id=6&vouchertype_id=6&customer_id=").append(crs.getInt("customer_id")).append("\">")
							.append(crs.getString("customer_name")).append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("voucheramount"))).append("</td>\n");
					Str.append("</tr>");
				}
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Top Invoices!<br><br><br><br><br><br></td></tr>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String TopReceipts() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, sum(voucher_amount)  AS voucheramount "
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " WHERE customer_active=1 and voucher_active=1 and substr(voucher_date,1,8) > substr('" + date + "',1,8)"
					+ " AND vouchertype_id = 9 AND vouchertype_voucherclass_id = 9 "
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ BranchAccess.replace("branch_id", "voucher_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY voucheramount desc "
					+ " limit 10";
			// SOP("StrSql=TopReceipts===" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../accounting/voucher-list.jsp?voucherclass_id=9&vouchertype_id=9&customer_id=").append(crs.getInt("customer_id")).append("\">")
							.append(crs.getString("customer_name")).append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("voucheramount"))).append("</td>\n");
					Str.append("</tr>");
				}
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Top Invoices!<br><br><br><br><br><br></td></tr>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String TopPayments() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, sum(voucher_amount) AS voucheramount "
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " WHERE customer_active=1 and voucher_active=1 and substr(voucher_date,1,8) > substr('" + date + "',1,8)"
					+ " AND vouchertype_id = 15 AND vouchertype_voucherclass_id = 15 "
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ BranchAccess.replace("branch_id", "voucher_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY voucheramount desc "
					+ " limit 10";
			// SOP("StrSql=TopPayments===" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../accounting/voucher-list.jsp?voucherclass_id=15&vouchertype_id=15&customer_id=").append(crs.getInt("customer_id")).append("\">")
							.append(crs.getString("customer_name")).append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("voucheramount"))).append("</td>\n");
					Str.append("</tr>");
				}
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Top Invoices!<br><br><br><br><br><br></td></tr>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 10 AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		try {
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table width=100% border=0 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
