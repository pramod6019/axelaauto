/* Ved Prakash (23rd July 2013) */
package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Dash_History extends Connect {

	public String StrSql = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrHTMLHistory = "";
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
					StrHTMLHistory = ListHistoryData(so_id, BranchAccess, ExeAccess, comp_id);
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

	public String ListHistoryData(String so_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT history_datetime, history_actiontype, history_oldvalue, history_newvalue,"
				+ " so_desc, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so_history"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = history_so_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id"
				+ " WHERE history_so_id = " + so_id + ""
				+ " ORDER BY history_id DESC";
		// SOP("StrSql==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("so_desc1==" + so_desc);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"container-fluid portlet box\">");
				Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">History</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=\"\">");
				Str.append("<div class=\"table-bordered table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th>Action By</th>");
				Str.append("<th data-hide=\"phone,tablet\">Type of Action</th>");
				Str.append("<th data-hide=\"phone,tablet\">New Value</th>");
				Str.append("<th data-hide=\"phone,tablet\">Old Value</th>");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					so_desc = crs.getString("so_desc");
					Str.append("<tr>\n");
					Str.append("<td>");
					Str.append(strToLongDate(crs.getString("history_datetime")));
					Str.append("</td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td>").append(crs.getString("history_actiontype"));
					Str.append("</td>\n");
					Str.append("<td>").append(crs.getString("history_newvalue"));
					Str.append("</td>\n");
					Str.append("<td>").append(crs.getString("history_oldvalue"));
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table>\n");

			} else {
				Str.append("<br><br><font color=red><b>No History(s) found!</b></font>");
			}
			Str.append("</div>");
			Str.append("</div>");
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
