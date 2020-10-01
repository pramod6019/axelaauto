// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Team_List extends Connect {

	public String branch_id = "0";
	public String team_id = "0";
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
			CheckPerm(comp_id, "emp_team_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				msg = PadQuotes(request.getParameter("msg"));
				team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND team_id = 0";
				}

				if (!team_id.equals("0")) {
					msg = msg + "<br>Results for Team ID = " + team_id + "!";
					StrSearch += " AND team_id = " + team_id + "";

				}

				if (!branch_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					StrHTML = "<font color=\"red\"><b>Select Branch!</b></font>";
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
		StrSql = "SELECT team_id, team_active, COALESCE(branch_id,0) AS branch_id,"
				+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'), '') AS branch_name,"
				+ " team_name, team_emp_id, team_crm_emp_id,team_branch_id, "
				+ " COALESCE(emp.emp_id,0) AS emp_id, "
				+ " COALESCE(emp.emp_name, '') AS emp_name, "
				+ " COALESCE(crm.emp_id, 0) AS crmemp_id, "
				+ " COALESCE(crm.emp_name,'') AS crmemp_name, "
				+ " COALESCE(crm.emp_ref_no,'') AS crmemp_ref_no, "
				+ " COALESCE(pbf.emp_id, 0) AS pbfemp_id, "
				+ " COALESCE(pbf.emp_name,'') AS pbfemp_name, "
				+ " COALESCE(pbf.emp_ref_no,'') AS pbfemp_ref_no, "
				+ " COALESCE(psf.emp_id, 0) AS psfemp_id, "
				+ " COALESCE(psf.emp_name,'') AS psfemp_name, "
				+ " COALESCE(psf.emp_ref_no,'') AS psfemp_ref_no, "

				+ " COALESCE(servicepsf.emp_id, 0) AS servicepsfemp_id, "
				+ " COALESCE(servicepsf.emp_name,'') AS servicepsfemp_name, "
				+ " COALESCE(servicepsf.emp_ref_no,'') AS servicepsfemp_ref_no, "

				+ " COALESCE(emp.emp_ref_no,'') AS emp_ref_no, "
				+ " COALESCE(preowned.emp_id,0) AS preownedemp_id, "
				+ " COALESCE(preowned.emp_name,'') AS preownedemp_name, "
				+ " COALESCE(preowned.emp_ref_no,'') AS preownedemp_ref_no, "
				+ " COALESCE(COUNT(ecount.emp_id), 0) AS empcount";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_team"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS emp ON emp.emp_id = team_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = team_preownedbranch_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS crm ON crm.emp_id = team_crm_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS pbf ON pbf.emp_id = team_pbf_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS psf ON psf.emp_id = team_psf_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS servicepsf ON servicepsf.emp_id = team_servicepsf_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS preowned ON preowned.emp_id = team_preownedemp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as ecount ON ecount.emp_id = teamtrans_emp_id"
				+ " WHERE team_branch_id = " + branch_id + "";

		StrSql = StrSql + SqlJoin;
		if (!StrSearch.equals("")) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql
				+ " GROUP BY team_id"
				+ " ORDER BY team_name";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Team</th>\n");
				Str.append("<th>Manager</th>\n");
				Str.append("<th>CRM Executives</th>\n");
				Str.append("<th>PBF Executives</th>\n");
				Str.append("<th>PSF Executives</th>\n");
				Str.append("<th>Service PSF Executives</th>\n");
				Str.append("<th>Pre-Owned Branch</th>\n");
				Str.append("<th>Pre-Owned Consultant</th>\n");
				Str.append("<th data-hide=\"phone\">Sales Consultant Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append(crs.getString("team_name"));
					if (crs.getString("team_active").equals("0")) {
						Str.append("<font color='red'> [Inactive] </font>");
					}
					Str.append("</td>");
					Str.append("<td>");
					Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), crs.getString("emp_ref_no")));
					Str.append("</td>");

					Str.append("<td valign=top>");
					if (!crs.getString("crmemp_id").equals("0")) {
						// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("crmemp_id"));
						// Str.append("\">").append(crs.getString("crmemp_name")).append(" (").append(crs.getString("crmemp_ref_no")).append(")");
						// Str.append("</a>");
						Str.append(ExeDetailsPopover(crs.getInt("crmemp_id"), crs.getString("crmemp_name"), crs.getString("crmemp_ref_no")));
					}
					Str.append("</td>");

					Str.append("<td valign=top>");
					if (!crs.getString("pbfemp_id").equals("0")) {
						// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("pbfemp_id"));
						// Str.append("\">").append(crs.getString("pbfemp_name")).append(" (").append(crs.getString("pbfemp_ref_no")).append(")");
						// Str.append("</a>");
						Str.append(ExeDetailsPopover(crs.getInt("pbfemp_id"), crs.getString("pbfemp_name"), crs.getString("pbfemp_ref_no")));
					}
					Str.append("</td>");

					Str.append("<td valign=top>");
					if (!crs.getString("psfemp_id").equals("0")) {
						// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("psfemp_id"));
						// Str.append("\">").append(crs.getString("psfemp_name")).append(" (").append(crs.getString("psfemp_ref_no")).append(")");
						// Str.append("</a>");
						Str.append(ExeDetailsPopover(crs.getInt("psfemp_id"), crs.getString("psfemp_name"), crs.getString("psfemp_ref_no")));
					}
					Str.append("</td>");

					Str.append("<td valign=top>");
					if (!crs.getString("servicepsfemp_id").equals("0")) {
						// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("servicepsfemp_id"));
						// Str.append("\">").append(crs.getString("servicepsfemp_name")).append(" (").append(crs.getString("servicepsfemp_ref_no")).append(")");
						// Str.append("</a>");
						Str.append(ExeDetailsPopover(crs.getInt("servicepsfemp_id"), crs.getString("servicepsfemp_name"), crs.getString("servicepsfemp_ref_no")));
					}
					Str.append("</td>");

					Str.append("<td >");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a></td\n>");

					Str.append("<td valign=top>");
					if (!crs.getString("preownedemp_id").equals("0")) {
						// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("preownedemp_id"));
						// Str.append("\">").append(crs.getString("preownedemp_name")).append(" (").append(crs.getString("preownedemp_ref_no")).append(")");
						// Str.append("</a>");
						Str.append(ExeDetailsPopover(crs.getInt("preownedemp_id"), crs.getString("preownedemp_name"), crs.getString("preownedemp_ref_no")));
					}
					Str.append("</td>");

					Str.append("<td valign=top align=right>");
					if (!crs.getString("empcount").equals("0")) {
						Str.append("").append(crs.getString("empcount"));
					} else {
						Str.append("0");
					}
					Str.append("</td>\n");

					Str.append("<td><a href=\"../sales/team-update.jsp?update=yes&team_id=");
					Str.append(crs.getString("team_id")).append("&dr_branch=");
					Str.append(crs.getString("team_branch_id"));
					Str.append(" \">Update Team</a></td>\n");
					Str.append("</tr>\n");
					total_exe_count = (int) (total_exe_count + (crs.getDouble("empcount")));
				}
				Str.append("<tr>");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td align=left><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(total_exe_count).append("</b></td>\n");
				Str.append("<td></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Team Found!</b></font>");
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
