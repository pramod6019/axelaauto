// Ved Prakash (18 Feb 2013)
package axela.preowned;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Team extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String CountSql = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String StrSearch = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_pre-owned_team_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				StrHTML = TeamSummary();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String TeamSummary() {
		StringBuilder Str = new StringBuilder();
		int execount = 0;
		StrSql = "SELECT preownedteam_id, preownedteam_branch_id, branch_name, branch_code, preownedteam_name, preownedteam_emp_id, emp_id, emp_name, emp_ref_no,"
				+ " COUNT(preownedteamtrans_emp_id) AS empcount"
				+ " FROM " + compdb(comp_id) + "axela_preowned_team"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= preownedteam_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_team_id = preownedteam_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS emp  ON emp.emp_id= preownedteam_emp_id"
				+ " WHERE 1=1"
				+ " GROUP BY preownedteam_id"
				+ " ORDER BY preownedteam_name";
		try {
			int count = 0;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\"><table class=\"table table-bordered table-responsive table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Pre-Owned Team</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Manager</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Consultant Count</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					execount = execount + crs.getInt("empcount");
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<a href=\"preowned-team-list.jsp?preownedteam_id=").append(crs.getString("preownedteam_id"));
					Str.append("&dr_branch=").append(crs.getString("preownedteam_branch_id"));
					Str.append("\">").append(crs.getString("preownedteam_name"));
					Str.append("</a></td>");
					Str.append("<td valign=top>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id"));
					Str.append("\">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")");
					Str.append("</a></td>");
					Str.append("<td valign=top align=left>");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("preownedteam_branch_id"));
					Str.append("\">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")");
					Str.append("</a></td>");
					Str.append("<td valign=top align=right>").append(crs.getString("empcount")).append("</td>\n");
					Str.append("</tr>\n");
				}

				// Str.append("<tr>\n");
				Str.append("<td colspan=4><b>Total: </b></td>\n");
				Str.append("<td align=right ><b>").append(execount).append("</b></td>\n");
				Str.append("</tbody>\n");
				// Str.append("</tr>");
				Str.append("</table>");

			} else {
				Str.append("<center><br><br><font color=red><b>No Team Found!</b></font></center>");
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
