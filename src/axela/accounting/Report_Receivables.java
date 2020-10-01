// JEET JAN 23 2015
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

public class Report_Receivables extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";

	public String voucherclass_id = "6";

	public String go = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		double total = 0.00, recetotal = 0.00, netduetotal = 0.00;
		double recamount = 0.00;
		StrSql = "SELECT voucher_id, voucher_date,voucherclass_id, vouchertype_id, voucher_no, voucher_ref_no, "
				+ " voucher_customer_id, vouchertype_prefix, vouchertype_suffix, "
				+ " vouchertype_name,  customer_name, voucher_amount, voucher_payment_date, customer_name,"
				+ " (voucher_amount - COALESCE((select sum(voucherbal_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal where voucherbal_trans_id = voucher_id),0)) AS netdue"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  axela_acc_voucher_class ON voucherclass_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				// + " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
				// + " INNER JOIN  "+compdb(comp_id)+"axela_customer ON customer_id = voucher_customer_id"
				+ " WHERE 1=1"
				+ " AND vouchertype_voucherclass_id =" + voucherclass_id
				+ " AND voucher_active = 1"
				+ " GROUP BY voucher_id"
				+ " HAVING netdue > 0"
				+ " ORDER BY voucher_date, voucher_id"
				+ " LIMIT 100";
		try {
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table \">\n");
				Str.append("<table class=\"table table-bordered table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone,tablet\">#</th>\n");
				Str.append("<th data-toggle=\"true\">ID</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>Voucher</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">No.</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Ref.No</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone\">Party's Name</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Due On</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Received</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Balance</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					total += crs.getDouble("voucher_amount");
					Str.append("<tr>");
					Str.append("<td align='center'>").append(count).append("</td>\n");

					Str.append("<td align='right'>").append(crs.getString("voucher_id")).append("</td>\n");

					Str.append("<td valign=top align=left><a href='../accounting/voucher-list.jsp?voucher_id=" + crs.getString("voucher_id") + "&voucherclass_id=" + crs.getString("voucherclass_id")
							+ "&vouchertype_id=" + crs.getString("vouchertype_id") + "'>");
					Str.append(crs.getString("vouchertype_name")).append("</a> </td>\n");

					Str.append("<td valign=top align='center'><a href='../accounting/voucher-list.jsp?voucher_id=" + crs.getString("voucher_id") + "&voucherclass_id="
							+ crs.getString("voucherclass_id")
							+ "&vouchertype_id=" + crs.getString("vouchertype_id") + "'>");
					Str.append(crs.getString("vouchertype_prefix")).append(crs.getString("voucher_no")).append(crs.getString("vouchertype_suffix")).append("</a> </td>\n");

					Str.append("<td valign=top align='center'>").append(crs.getString("voucher_ref_no")).append("</td>\n");

					Str.append("<td align='center'>").append(strToShortDate(crs.getString("voucher_date"))).append("</td>\n");

					Str.append("<td align='left'>").append("<a href='../accounting/report-ledgerstatement.jsp?ledger=" + crs.getString("voucher_customer_id") + "'>");
					Str.append(crs.getString("customer_name")).append("</a></td>\n");

					Str.append("<td align='right'>").append(deci.format(crs.getDouble("voucher_amount"))).append("</td>\n");

					Str.append("<td align='center'>").append(strToShortDate(crs.getString("voucher_payment_date"))).append("</td>\n");

					recamount = crs.getDouble("voucher_amount") - crs.getDouble("netdue");
					recetotal += recamount;

					Str.append("<td align='right'>").append(deci.format(recamount)).append("</td>\n");

					Str.append("<td align='right'>").append(deci.format(crs.getDouble("netdue"))).append("</td>\n");
					netduetotal += crs.getDouble("netdue");
					Str.append("</tr>");
				}
				crs.close();
				Str.append("<tr>");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'><b>").append("Total:").append("</b></td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'><b>").append(deci.format(total)).append("</b></td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'><b>").append(deci.format(recetotal)).append("</b></td>\n");
				Str.append("<td align='right'><b>").append(deci.format(netduetotal)).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Record(s) Found!</b></font><br><br>");
			}
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
