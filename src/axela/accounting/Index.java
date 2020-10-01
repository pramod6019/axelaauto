// smitha nag 26 june 2013
package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_acc_receipt_access,emp_acc_receipt_add", request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		String StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM axela_module_report"
				+ " INNER JOIN axela_module ON module_id = report_module_id"
				+ " WHERE report_module_id = 12 AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >");
					Str.append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
