package axela.service;
// Sangita 28th may 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Followup_Esc_Dashboard extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String team_id = "", exe_id = "";
	public String StrSearch = "";
	public String[] team_ids, exe_ids;
	public String branch_id = "0";
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "", advisor_id = "", tech_id = "", jccat_id = "";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);

				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch = StrSearch + " AND jc_branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND jcpsf_emp_id in (" + exe_id + ")";
					}
				}
				StrHTML = PSFFollowupEscDashboard();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		exe_id = RetrunSelectArrVal(request, "dr_jc_emp_id");
		exe_ids = request.getParameterValues("dr_jc_emp_id");
		// SOP("brand_id=== " + brand_id);
		// SOP("region_id=== " + region_id);
		// SOP("zone_id=== " + zone_id);
		// SOP("branch_id=== " + branch_id);
		// SOP("exe_id=== " + exe_id);
	}

	public String PSFFollowupEscDashboard() {
		try {
			int count = 0, psfcount = 0;
			int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, branch_name, "
					+ " SUM(if(jcpsf_trigger=1,1,0)) as level1, "
					+ " SUM(if(jcpsf_trigger=2,1,0)) as level2, "
					+ " SUM(if(jcpsf_trigger=3,1,0)) as level3, "
					+ " SUM(if(jcpsf_trigger=4,1,0)) as level4, "
					+ " SUM(if(jcpsf_trigger=5,1,0)) as level5, "
					+ " COUNT(jcpsf_id) as psfcount "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_branch_id = branch_id "
					// + " enquiry_status_id=1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id "
					+ " WHERE 1=1 and jcpsf_trigger > 0 "
					+ "	AND jcpsf_desc = ''"
					+ " AND branch_active='1' " + StrSearch
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");
			}
			StrSql += " GROUP BY branch_id ";
			// SOP("StrSql===PSFFollowupEscDashboard===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table   table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead>");
			Str.append("<tr>\n");
			Str.append("<th align=center data-hide=\"phone\">#</th>\n");
			Str.append("<th data-toggle=\"true\">Branch</th>\n");
			Str.append("<th>Level 1</th>\n");
			Str.append("<th data-hide=\"phone\">Level 2</th>\n");
			Str.append("<th data-hide=\"phone,tablet\">Level 3</th>\n");
			Str.append("<th data-hide=\"phone,tablet\">Level 4</th>\n");
			Str.append("<th data-hide=\"phone,tablet\">Level 5</th>\n");
			Str.append("<th data-hide=\"phone,tablet\">Total</th>\n");
			Str.append("</tr>");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			while (crs.next()) {
				count++;
				psfcount = psfcount + crs.getInt("psfcount");
				level1 = level1 + crs.getInt("level1");
				level2 = level2 + crs.getInt("level2");
				level3 = level3 + crs.getInt("level3");
				level4 = level4 + crs.getInt("level4");
				level5 = level5 + crs.getInt("level5");
				Str.append("<tr>\n");
				Str.append("<td align=center>").append(count).append("</b></td>\n");
				Str.append("<td align=center>").append(crs.getString("branch_name")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level1")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level2")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level3")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level4")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level5")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("psfcount")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("<tr>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td><b>").append(level1).append("</b></td>\n");
			Str.append("<td><b>").append(level2).append("</b></td>\n");
			Str.append("<td><b>").append(level3).append("</b></td>\n");
			Str.append("<td><b>").append(level4).append("</b></td>\n");
			Str.append("<td><b>").append(level5).append("</b></td>\n");
			Str.append("<td><b>").append(psfcount).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			Str.append(getFollowupPriority());
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// public String PopulateTeam() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "select team_id, team_name "
	// + " from " + compdb(comp_id) + "axela_sales_team "
	// + " where 1=1 and team_branch_id=" + dr_branch_id + ""
	// + " group by team_id"
	// + " order by team_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("team_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
	// Str.append(">").append(crs.getString("team_name")).append("</option> \n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulateServiceExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_service = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND emp_branch_id IN (" + branch_id + ") ";
			}
			StrSql += " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id";
			if (!branch_id.equals(""))
			{
				StrSql += " AND empbr.emp_branch_id IN (" + branch_id + ") ";
			}

			StrSql += ExeAccess
					+ ") ORDER BY emp_name";
			SOP("StrSql--" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_jc_emp_id id=dr_jc_emp_id class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 >");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String getFollowupPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_priority "
					+ " ORDER BY priorityjc_name";
			// SOP("StrSql===getFollowupPriority==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br>");
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table   table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr>");
				Str.append("<th colspan=9 style=\"text-align:center\">Priority</th></tr>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\"><b>#</b></th>\n");
				Str.append("<th  data-toggle=\"true\"><b>Priority</b></th>\n");
				Str.append("<th><b>Description</b></td>\n");
				Str.append("<th data-hide=\"phone\"><b>Due Hours</b></td>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 1</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 2</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 3</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 4</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 5</b></th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_desc")).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_duehrs"))).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger1_hrs"))).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger2_hrs"))).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger3_hrs"))).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger4_hrs"))).append("</td>\n");
					Str.append("<td>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
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
