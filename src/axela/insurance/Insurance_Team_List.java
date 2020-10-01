// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Team_List extends Connect {

	public String insurteam_branch_id = "0";
	public String insurteam_id = "0";
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
				insurteam_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				msg = PadQuotes(request.getParameter("msg"));
				insurteam_id = CNumeric(PadQuotes(request.getParameter("team_id")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND team_id = 0";
				}

				if (!insurteam_id.equals("0")) {
					msg = msg + "<br>Results for Team ID = " + insurteam_id + "!";
					StrSearch += " AND team_id = " + insurteam_id + "";

				}

				if (!insurteam_branch_id.equals("0")) {
					StrHTML = Listdata();
				} else {
					StrHTML = "<font color=\"red\"><b>Select Insurance Branch!</b></font>";
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
		StrSql = "SELECT insurteam_id, insurteam_active,"
				+ " COALESCE (insurteam_branch_id, 0) AS insurteam_branch_id,"
				+ " COALESCE ( CONCAT( branch_name, ' (', branch_code, ')' ), '' )"
				+ " AS branch_name, insurteam_name, insurteam_emp_id, insurteam_psf_emp_id, insurteam_branch_id,"
				+ " COALESCE (emp.emp_id, 0) AS emp_id,"
				+ " COALESCE (emp.emp_name, '') AS emp_name,"
				+ " COALESCE (emp.emp_ref_no, '') AS emp_ref_no,"
				+ " COALESCE (psf.emp_id, 0) AS psfemp_id,"
				+ " COALESCE (psf.emp_name, '') AS psfemp_name,"
				+ " COALESCE (psf.emp_ref_no, '') AS psfemp_ref_no,"
				+ " COALESCE (COUNT(ecount.emp_id), 0) AS empcount ";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_team"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS emp ON emp.emp_id = insurteam_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurteam_branch_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS psf ON psf.emp_id = insurteam_psf_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_team_exe ON insurteamtrans_team_id = insurteam_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as ecount ON ecount.emp_id = insurteamtrans_emp_id"
				+ " WHERE insurteam_branch_id = " + insurteam_branch_id + "";

		StrSql = StrSql + SqlJoin;
		if (!StrSearch.equals("")) {
			StrSql = StrSql + StrSearch;
		}
		StrSql = StrSql + " GROUP BY insurteam_emp_id"
				+ " ORDER BY insurteam_name";
		// SOP("StrSql----------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Insurance Team</th>\n");
				Str.append("<th>Manager</th>\n");
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
					Str.append(crs.getString("insurteam_name"));
					if (crs.getString("insurteam_active").equals("0")) {
						Str.append("<font color='red'> [Inactive] </font>");
					}
					Str.append("</td>");
					Str.append("<td>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id"));
					Str.append("\">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")");
					Str.append("</a></td>");
					Str.append("<td valign=top>");
					if (!crs.getString("psfemp_id").equals("0")) {
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("psfemp_id"));
						Str.append("\">").append(crs.getString("psfemp_name")).append(" (").append(crs.getString("psfemp_ref_no")).append(")");
						Str.append("</a>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("empcount").equals("0")) {
						Str.append("").append(crs.getString("empcount"));
					} else {
						Str.append("0");
					}
					Str.append("</td>\n");
					Str.append("<td><a href=\"../insurance/insurance-team-update.jsp?update=yes&insurteam_id=");
					Str.append(crs.getString("insurteam_id")).append("&dr_branch=");
					Str.append(crs.getString("insurteam_branch_id"));
					Str.append(" \">Update Insurance Team</a></td>\n");
					Str.append("</tr>\n");
					total_exe_count = (int) (total_exe_count + (crs.getDouble("empcount")));
				}
				Str.append("<tr>");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td></td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(total_exe_count).append("</b></td>\n");
				Str.append("<td></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Insurance Team Found!</b></font>");
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
