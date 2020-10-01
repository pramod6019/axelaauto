package axela.customer;
//Murali 21st jun

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String msg = "";
	public String emp_id = "0", branch_id = "0";
	public String comp_id = "0";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String smart = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String date = "";
	DecimalFormat deci = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_customer_access", request, response);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				date = ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -365, 0, 0));

				// if (smart == null) {
				// smart = "";
				// }
				// if (msg.equals("")) {
				// if (smart.equals("yes")) {
				// } else {
				// SetSession("customerstrsql", StrSearch, request);
				// }
				// }
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String TopCustomers() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, SUM(voucher_amount)  AS voucher_amount "
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = customer_id"
					+ " WHERE customer_active=1"
					+ " AND voucher_active=1"
					+ " AND voucher_vouchertype_id = 6"
					+ " AND SUBSTR(voucher_date,1,8) > SUBSTR('" + date + "',1,8) "
					+ ExeAccess.replace("emp_id", "customer_emp_id") + ""
					+ BranchAccess.replace("branch_id", "customer_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY voucher_amount desc "
					+ " LIMIT 10";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getInt("customer_id")).append("\">").append(crs.getString("customer_name"))
							.append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("voucher_amount"))).append("</td>\n");
					Str.append("</tr>");
				}
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Top Customers!<br><br><br><br><br><br></td></tr>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String RecentCustomers() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT customer_id, customer_name, SUM(voucher_amount)  AS voucher_amount"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = customer_id AND voucher_active = 1"
					+ " WHERE customer_active=1 "
					+ " AND voucher_vouchertype_id = 6"
					+ ExeAccess.replace("emp_id", "customer_emp_id") + ""
					+ BranchAccess.replace("branch_id", "customer_branch_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY customer_entry_date desc "
					+ " LIMIT 10";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=left>\n");
					Str.append("<td align=left><a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getInt("customer_id")).append("\">").append(crs.getString("customer_name"))
							.append("</a></td>");
					Str.append("<td align=right valign=top>").append(deci.format(crs.getDouble("voucher_amount"))).append("</td>\n");
					Str.append("</tr>");
				}
			} else {
				Str.append("<tr><td align=center><br><br>No Recent Customers!<br><br><br><br><br><br></td></tr>");
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
				+ " WHERE report_module_id = 2 AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
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
