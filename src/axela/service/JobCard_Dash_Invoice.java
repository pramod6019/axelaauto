/* Ved Prakash (23rd July 2013) */
package axela.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Dash_Invoice extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String branch_id = "";
	public String comp_id = "0";
	public String jc_id = "";
	public String jc_desc = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				jc_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				StrSql = "SELECT so_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + jc_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						jc_id = crs.getString("so_id");
					}
					StrHTML = ListInvoice(jc_id, BranchAccess, ExeAccess, comp_id);
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListInvoice(String jc_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Invoice</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");
		int count = 0;
		StrSql = "SELECT main.voucher_id, main.voucher_jc_id,"
				+ " main.voucher_invoice_id, main.voucher_branch_id, main.voucher_amount,"
				+ " COALESCE ( ( SELECT CAST(SUM( vouchertax.vouchertrans_amount )AS DECIMAL(15,2)) AS tax"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans vouchertax"
				+ " WHERE vouchertax.vouchertrans_tax = '1'"
				+ " AND vouchertax.vouchertrans_voucher_id = main.voucher_id ), '0' ) AS vouchertax,"
				+ " COALESCE ( ( SELECT CAST(SUM( voucherprice.vouchertrans_amount )AS DECIMAL(15,2)) AS price"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans voucherprice"
				+ " WHERE voucherprice.vouchertrans_rowcount != 0"
				+ " AND voucherprice.vouchertrans_option_id = 0"
				+ " AND voucherprice.vouchertrans_voucher_id = main.voucher_id ), '0' ) AS voucherprice,"
				+ " CONCAT( 'INV', branch_code, main.voucher_no ) AS voucher_no,"
				+ " main.voucher_date, customer_id, customer_name,"
				+ " vouchertrans_netprice, vouchertrans_taxamount, main.voucher_authorize,"
				+ " main.voucher_quote_id, main.voucher_active, main.voucher_entry_id, main.voucher_entry_date,"
				+ " vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name, emp_id, branch_name,"
				+ " GROUP_CONCAT(item_name SEPARATOR '<br>') AS items"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher main"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = main.voucher_id"
				+ " AND vouchertrans_rowcount != 0"
				+ " AND vouchertrans_tax = 0"
				+ " AND vouchertrans_discount = 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = main.voucher_vouchertype_id"
				+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = main.voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = main.voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = main.voucher_emp_id"
				+ " WHERE main.voucher_jc_id = " + jc_id
				+ " AND main.voucher_vouchertype_id = 6"
				+ " GROUP BY main.voucher_id"
				+ " ORDER BY main.voucher_id DESC";
		SOP("StrSql==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
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
					SOP("abc==" + crs.getString("items"));
					Str.append("</td>\n<td>Net Total: ");
					SOP("888==" + (crs.getString(("vouchertax"))));
					SOP("555==" + (crs.getDouble(("vouchertax"))));
					SOP("123==" + IndDecimalFormat(crs.getString("voucherprice")));
					Str.append(IndDecimalFormat(crs.getString("voucherprice")));
					SOP("vouchertax==" + (crs.getString("vouchertax")));
					if (!crs.getString("vouchertax").equals("0")) {
						Str.append("<br>Tax: ").append(IndDecimalFormat(crs.getString("vouchertax"))).append("</b>");
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(crs.getString("voucher_amount")));
					Str.append("</b><br></td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					if (!crs.getString("voucher_quote_id").equals("0")) {
						quotecheck = "&quote_id=" + crs.getString("voucher_quote_id");
					}
					Str.append("<td><a href=\"../accounting/so-update2.jsp?update=yes");
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
					Str.append(jc_id).append("\">Add Receipt</a>");
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
}
