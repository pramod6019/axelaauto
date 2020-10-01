package axela.preowned;
//Dilip Kumar 27 Jun 2013

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_History extends Connect {

	public String preowned_id = "0";
	public String preowned_title = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String StrHTMLHistory = "";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));

				StrSql = "SELECT preowned_title"
						+ " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
						+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY preowned_id"
						+ " ORDER BY preowned_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
					}
					StrHTMLHistory = ListHistoryData();
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

	public String ListHistoryData() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT " + compdb(comp_id) + "axela_preowned_history.*, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id, preowned_title"
				+ " FROM " + compdb(comp_id) + "axela_preowned_history"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedhistory_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preownedhistory_emp_id"
				+ " WHERE preownedhistory_preowned_id = " + preowned_id + ""
				+ " ORDER BY preownedhistory_id DESC";
		// SOP("StrSql@@@==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Date</th>");
				Str.append("<th>Action By</th>");
				Str.append("<th data-hide=\"phone\">Type of Action</th>");
				Str.append("<th data-hide=\"phone\">New Value</th>");
				Str.append("<th data-hide=\"phone\"> Old Value</th>");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(strToLongDate(crs.getString("preownedhistory_datetime"))).append("</td>");
					Str.append("<td valign=top align=left ><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a>").append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedhistory_actiontype")).append(" </td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedhistory_newvalue")).append("</td>");
					Str.append("<td valign=top align=left >").append(crs.getString("preownedhistory_oldvalue")).append("</td>");
					Str.append("</tr>" + "\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<div align=center><br><br><font color=red><b>No History(s) found!</b></font></div>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
