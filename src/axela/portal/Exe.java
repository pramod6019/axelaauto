//@Shilpashree 06 oct 2015
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Exe extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String dr_branch_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_executive_access", request, response);
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				if (!dr_branch_id.equals("0")) {
					StrSearch = StrSearch + "AND (emp_branch_id = " + dr_branch_id + " OR emp_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp_branch WHERE emp_branch_id = " + dr_branch_id
							+ "))";
				} else {
					StrSearch = StrSearch + "AND emp_branch_id = 0";
				}
				StrHTML = ExecutiveSummary(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ExecutiveStatus() {
		String count = "";
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">"
					+ "<div class=\"caption\" style=\"float: none\">&nbsp;</div></div>"
					+ "<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\" style=\"width:200px;\">\n");
			Str.append("<tr>\n");
			Str.append("<th>&nbsp;</th>\n");
			Str.append("<th>Total</th>\n");
			Str.append("<th>Active</th>\n");
			Str.append("<th>Inactive</th>\n");
			Str.append("</tr>\n");
			Str.append("<tr>\n");
			Str.append("<td><b>Executives<b>:</td>\n");
			count = ExecuteQuery("SELECT COUNT(emp_id) FROM " + compdb(comp_id) + "axela_emp WHERE 1 = 1 AND emp_id != 1");
			Str.append("<td><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("SELECT COUNT(emp_id) FROM " + compdb(comp_id) + "axela_emp WHERE emp_active = '1' AND emp_id != 1");
			Str.append("<td><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("SELECT COUNT(emp_id) FROM " + compdb(comp_id) + "axela_emp WHERE emp_active = '0' AND emp_id != 1");
			Str.append("<td><b>").append(count).append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("</table></div></div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ExecutiveSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " COALESCE(branch_name, 'Head Office') AS branch"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE 1 = 1 AND emp_active = 1 AND emp_id != 1 " + StrSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_branch_id, emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;

			// <table class=\"table table-bordered table-hover\" style=\"width:200px;\">

			// Str.append("<div class=\"  table-bordered\">\n");
			// Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			// Str.append("<thead>\n");
			// Str.append("<tr>\n");
			// Str.append("<th data-toggle=\"true\">#</th>\n");
			// Str.append("<th >ID</th>\n");
			// Str.append("<th style=\"width:200px;\">Customer</th>\n");
			// Str.append("<th>Model</th>\n");
			// Str.append("<th data-hide=\"phone\">Variant</th>\n");

			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			if (crs.isBeforeFirst()) {
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th><b>Executive</th>\n");
				Str.append("<th><b>Branch</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>").append("<a href=\"../portal/exe-list.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append("\">").append(crs.getString("emp_name"));
					Str.append("</a></td>\n");
					Str.append("<td>").append(crs.getString("branch")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Executives(s) found!</b></font>");
			}
			Str.append("</table>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
