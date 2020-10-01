package axela.preowned;
// divya 29th 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_Receipt extends Connect {

	public String preowned_id = "0";
	public String preowned_title = "";
	public String StrSearch = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				// SOP("preowned_id====" + preowned_id);
				StrSql = "SELECT preowned_title "
						+ " FROM " + compdb(comp_id) + "axela_preowned "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id "
						+ " WHERE 1=1 and preowned_id =" + preowned_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY preowned_id "
						+ " ORDER BY preowned_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
					}
					StrHTML = ListData();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Pre Owned!");
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		StrSql = "SELECT receipt_id, receipt_branch_id, CONCAT('RCT', branch_code, receipt_no) AS receipt_no, receipt_date,"
				+ " customer_id, customer_name, receipt_amount, invoice_id,"
				+ " receipt_active, receipt_entry_id, receipt_entry_date,"
				+ " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name, emp_id, branch_name"
				+ " FROM " + compdb(comp_id) + "axela_invoice_receipt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_invoice ON invoice_id = receipt_invoice_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = receipt_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = receipt_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = receipt_emp_id "
				+ " WHERE invoice_preowned_id =" + preowned_id + BranchAccess + ExeAccess + ""
				+ " GROUP BY receipt_id"
				+ " ORDER BY receipt_id desc";
		// SOP("StrSql==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Invoice</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Pre-Owned Consultant</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=center><a href=../invoice/receipt-list.jsp?receipt_id=").append(crs.getInt("receipt_id")).append(">").append(crs.getString("receipt_id"))
							.append("</a>").append("</td>");
					Str.append("<td valign=top align=center>").append(crs.getString("receipt_no")).append("");
					if (crs.getString("receipt_active").equals("0")) {
						Str.append("<br><font color=red><b>Inactive</b></font>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left><a href=invoice-list.jsp?invoice_id=").append(crs.getInt("invoice_id")).append(">Invoice ID: ").append(crs.getString("invoice_id"))
							.append("</a></td>");
					Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=").append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name"))
							.append("</a></td>");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("receipt_date"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("receipt_amount")).append("</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>");
					Str.append("<td valign=top align=left nowrap><a href=\"../invoice/receipt-update.jsp?update=yes&receipt_id=").append(crs.getString("receipt_id"))
							.append(" \" target=_parent>Update Receipt</a>" + ""
									+ "<br><a href=\"../invoice/receipt-print.jsp?receipt_id=").append(crs.getString("receipt_id")).append("\" target=_blank>Print Receipt</a>");
					Str.append("</td></tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Receipt(s) found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
