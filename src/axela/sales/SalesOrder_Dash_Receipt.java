/* Ved Prakash (23rd July 2013) */
package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Dash_Receipt extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String branch_id = "";
	public String so_id = "";
	public String comp_id = "0";
	public String so_desc = "";

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
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));

				StrSql = "SELECT so_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
					}
					StrHTML = ListReceipt(so_id, BranchAccess, ExeAccess, comp_id);
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

	public String ListReceipt(String so_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Receipt</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");

		int count = 0;
		StrSql = "SELECT voucher_id, voucher_so_id, voucher_branch_id,"
				+ " CONCAT( 'RCT', branch_code, voucher_no ) AS voucher_no,"
				+ " voucher_date, customer_id, customer_name, voucher_amount,"
				+ " COALESCE(voucher_invoice_id, 0) AS voucher_invoice_id,voucher_authorize, vouchertype_authorize, vouchertype_defaultauthorize, "
				+ " voucher_active, voucher_entry_id, voucher_entry_date,"
				+ "	vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
				+ " emp_id, branch_name,branch_brand_id,so_desc"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ "	INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE voucher_so_id = " + so_id
				+ "	AND voucher_vouchertype_id = 9"
				+ " GROUP BY voucher_id"
				+ " ORDER BY voucher_id DESC";
		// SOP("StrSql==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th data-hide=\"phone\">Invoice</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					so_desc = crs.getString("so_desc");
					count++;
					Str.append("<tr>\n<td valign=top align=center>").append(count);
					Str.append("</td>\n<td valign=top align=center><a href=../accounting/voucher-list.jsp?voucher_id=");
					Str.append(crs.getInt("voucher_id")).append(">").append(crs.getString("voucher_id"));
					Str.append("</a></td>\n<td valign=top align=center>").append(crs.getString("voucher_no"));
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td valign=top align=left>");
					if (!crs.getString("voucher_invoice_id").equals("0")) {
						Str.append("<a href=../accounting/voucher-list.jsp?vouchertype_id=").append(crs.getInt("vouchertype_id")).append("&voucher_id=").append(crs.getString("voucher_id"));
						Str.append(">Invoice ID: ").append(crs.getString("voucher_id")).append("</a>");
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

					if (crs.getString("vouchertype_authorize").equals("1") || crs.getString("vouchertype_defaultauthorize").equals("1")) {

						Str.append("<br/><a href=\"../accounting/" + "voucher" + "-authorize.jsp?voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
						Str.append("\">Authorize</a>");

					}

					String print = "yes";

					if ((crs.getString("vouchertype_authorize").equals("1")
							|| crs.getString("vouchertype_defaultauthorize").equals("1"))
							&& CNumeric(crs.getString("voucher_authorize")).equals("0")) {
						print = "";
					}
					if (print.equals("yes")) {
						Str.append("</br><a target='_blank' href=\"../accounting/");
						// Go to different report page for "One Triumph" branch "onetriumph-so-print.jsp"
						if (crs.getString("branch_name").toLowerCase().contains("one triumph")
								|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
							Str.append("onetriumph-");
						}

						if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-ford" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-hyundai" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-honda" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-yamaha" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-volvo" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else {
							Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						}

						// Go to different report page for "One Triumph" branch
						if (crs.getString("branch_name").toLowerCase().contains("one triumph")
								|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
							Str.append("onetriumph-");
						}
						// SOP("brand_id==" + crs.getString("branch_brand_id"));
						//

						if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-ford")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {

							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-honda")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {

							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-hyundai")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-yamaha")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-volvo")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						}
						else {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						}

						Str.append("</td>\n</tr>\n");
					}
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
}
