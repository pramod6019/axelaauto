// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Team_List extends Connect {

	public String branch_id = "0";
	public String preownedteam_id = "0";
	public int total_exe_count = 0;
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String SqlJoin = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_pre-owned_team_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				msg = PadQuotes(request.getParameter("msg"));
				preownedteam_id = CNumeric(PadQuotes(request.getParameter("team_id")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND team_id = 0";
				}

				if (!preownedteam_id.equals("0")) {
					msg = msg + "<br>Results for Team ID = " + preownedteam_id + "!";
					StrSearch += " AND team_id = " + preownedteam_id + "";

				}

				if (!branch_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					StrHTML = "<font color=\"red\"><b>Select Pre-Owned Branch!</b></font>";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT preownedteam_id, preownedteam_active,"
				+ " COALESCE (branch_id, 0) AS branch_id,"
				+ " COALESCE ( CONCAT( branch_name, ' (', branch_code, ')' ), '' )"
				+ " AS branch_name, preownedteam_name, preownedteam_emp_id, preownedteam_crm_emp_id, preownedteam_branch_id,"
				+ " COALESCE (emp.emp_id, 0) AS emp_id,"
				+ " COALESCE (emp.emp_name, '') AS emp_name,"
				+ " COALESCE (crm.emp_id, 0) AS crmemp_id,"
				+ " COALESCE (crm.emp_name, '') AS crmemp_name,"
				+ " COALESCE (crm.emp_ref_no, '') AS crmemp_ref_no,"
				+ " COALESCE (psf.emp_id, 0) AS psfemp_id,"
				+ " COALESCE (psf.emp_name, '') AS psfemp_name,"
				+ " COALESCE (psf.emp_ref_no, '') AS psfemp_ref_no,"
				+ " COALESCE (emp.emp_ref_no, '') AS emp_ref_no,"
				+ " COALESCE (COUNT(ecount.emp_id), 0) AS empcount ";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_team"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS emp ON emp.emp_id = preownedteam_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preownedteam_branch_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS crm ON crm.emp_id = preownedteam_crm_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS psf ON psf.emp_id = preownedteam_psf_emp_id"
				// + " LEFT JOIN " + compdb(comp_id) + "axela_emp AS preowned ON preowned.emp_id = team_preownedemp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_team_id = preownedteam_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as ecount ON ecount.emp_id = preownedteamtrans_emp_id"
				+ " WHERE preownedteam_branch_id = " + branch_id + "";

		StrSql = StrSql + SqlJoin;
		if (!StrSearch.equals("")) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY preownedteam_id"
				+ " ORDER BY preownedteam_name";
		// SOP("StrSql----------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Pre-Owned Team</th>\n");
				Str.append("<th>Manager</th>\n");
				Str.append("<th>CRM Executive</th>\n");
				Str.append("<th>PSF Executive</th>\n");
				Str.append("<th data-hide=\"phone\">Consultant Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append(crs.getString("preownedteam_name"));
					if (crs.getString("preownedteam_active").equals("0")) {
						Str.append("<font color='red'> [Inactive] </font>");
					}
					Str.append("</td>");
					Str.append("<td>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id"));
					Str.append("\">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")");
					Str.append("</a></td>");
					Str.append("<td valign=top>");
					if (!crs.getString("crmemp_id").equals("0")) {
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("crmemp_id"));
						Str.append("\">").append(crs.getString("crmemp_name")).append(" (").append(crs.getString("crmemp_ref_no")).append(")");
						Str.append("</a>");
					}
					Str.append("</td>");
					Str.append("<td valign=top>");
					// if (!crs.getString("pbfemp_id").equals("0")) {
					// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("pbfemp_id"));
					// Str.append("\">").append(crs.getString("pbfemp_name")).append(" (").append(crs.getString("pbfemp_ref_no")).append(")");
					// Str.append("</a>");
					// }
					// Str.append("</td>");
					// Str.append("<td valign=top>");
					if (!crs.getString("psfemp_id").equals("0")) {
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("psfemp_id"));
						Str.append("\">").append(crs.getString("psfemp_name")).append(" (").append(crs.getString("psfemp_ref_no")).append(")");
						Str.append("</a>");
					}
					// Str.append("</td>");
					// Str.append("<td >");
					// Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					// Str.append(crs.getString("branch_name")).append("</a></td\n>");
					// Str.append("<td valign=top>");
					// if (!crs.getString("preownedemp_id").equals("0")) {
					// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("preownedemp_id"));
					// Str.append("\">").append(crs.getString("preownedemp_name")).append(" (").append(crs.getString("preownedemp_ref_no")).append(")");
					// Str.append("</a>");
					// }
					// Str.append("</td>");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("empcount").equals("0")) {
						Str.append("").append(crs.getString("empcount"));
					} else {
						Str.append("0");
					}
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
							+ "<button type=button style='margin: 0' class='btn btn-success'>"
							+ "<i class='fa fa-pencil'></i></button>"
							+ "<ul class='dropdown-content dropdown-menu pull-right'>"
							+ "<li role=presentation><a href=\"../preowned/preowned-team-update.jsp?update=yes&preownedteam_id="
							+ crs.getString("preownedteam_id") + "&dr_branch=" + crs.getString("preownedteam_branch_id")
							+ " \">Update Pre-Owned Team</a></li></ul></div></center></div></td>\n");
					Str.append("</tr>\n");
					total_exe_count = (int) (total_exe_count + (crs.getDouble("empcount")));
				}
				Str.append("<tr>");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td><b>Total:</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(total_exe_count).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Pre-Owned Team Found!</b></font>");
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
