// Ved Prakash (11 Feb 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Branch_Summary extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				StrHTML = BranchSummary();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String BranchSummary() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT " + compdb(comp_id) + "axela_branch.*, branchtype_name, franchisee_name, rateclass_name, city_name"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN axela_branch_type ON branchtype_id = branch_branchtype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_franchisee ON franchisee_id = branch_franchisee_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
				+ " WHERE branch_id = " + branch_id + BranchAccess + "";
		CachedRowSet crs =processQuery(StrSql, 0);
		Str.append("<div class=\"portlet box  \">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">");
		Str.append("Branch Summary");
		Str.append("</div>");
		Str.append("</div>");
		Str.append("<div class=\"portlet-body portlet-empty\">\n");
		Str.append("<div class=\"tab-pane\">\n");
		Str.append("<div class=\"  table-bordered\">\n");
		Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
		// Str.append("<thead>\n");
		// Str.append("<tr align=\"center\">\n");
		// Str.append("<th data-toggle=\"true\" colspan=\"2\"><b>Branch Summary</b></th></tr>\n");
		// //
		// Str.append("<tr align=\"center\"><th colspan=\"2\"><b>Branch Summary</b></th>\n</tr>\n");
		// Str.append("</thead>");
		Str.append("<tbody>");
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("branch_name").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td width=\"25%\" align=\"left\">Branch Name:</td>\n");
						Str.append("<td width=\"75%\" align=\"left\">\n").append("<a href=\"../portal/branch-list.jsp?branch_id=");
						Str.append(crs.getInt("branch_id")).append("\">").append("<b>").append(crs.getString("branch_name"));
						Str.append("</b></a>\n</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_id").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Branch ID:</td>\n");
						Str.append("<td align=\"left\"><b>");
						Str.append(crs.getString("branch_id")).append("</b></td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_code").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Code: </td>\n");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("branch_code")).append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_branchtype_id").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Type: </td>\n");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("branchtype_name")).append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_franchisee_id").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Franchisee: </td>\n");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("franchisee_name")).append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_rateclass_id").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Class: </td>\n");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("rateclass_name")).append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_phone1").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Phone: </td>\n<td align=\"left\">");
						Str.append(crs.getString("branch_phone1"));
						if (!crs.getString("branch_phone2").equals("")) {
							Str.append(", ").append(crs.getString("branch_phone2"));
						}
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Mobile: </td>\n");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("branch_mobile1"));
						if (!crs.getString("branch_mobile2").equals("")) {
							Str.append(", ").append(crs.getString("branch_mobile2"));
						}
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_email1").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Email: </td>\n");
						Str.append("<td align=\"left\">\n");
						Str.append("<a href=mailto:").append(crs.getString("branch_email1")).append(">");
						Str.append(crs.getString("branch_email1")).append("</a>\n");
						if (!crs.getString("branch_email2").equals("")) {
							Str.append(", <a href=mailto:").append(crs.getString("branch_email2")).append(">");
							Str.append(crs.getString("branch_email2")).append("</a>\n");
						}
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("branch_add").equals("")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\">Address: </td>");
						Str.append("<td align=\"left\">");
						Str.append(crs.getString("branch_add")).append(", ").append(crs.getString("city_name"));
						Str.append(" - ").append(crs.getString("branch_pin")).append("</td>\n");
						Str.append("</tr>\n");
					}
				}
			} else {
				Str.append("<tr>\n");
				Str.append("<td colspan=\"2\" align=\"center\"><br><br>Invalid Branch!<br><br><br><br></td>\n");
				Str.append("</tr>\n");
			}
			crs.close();
			Str.append("</tbody>");
			Str.append("</table>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
