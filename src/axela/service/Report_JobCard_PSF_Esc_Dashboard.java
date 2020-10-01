package axela.service;
// Sangita 24th june 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Esc_Dashboard extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String exe_id = "";
	public String StrSearch = "";
	public String[] exe_ids;
	public String dr_branch_id = "0";
	public String branch_id = "0";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);

				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND jc_branch_id =" + dr_branch_id;
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and jcpsf_emp_id in (" + exe_id + ")";
					}
				}
				StrHTML = PSFEscDashboard();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_exe");
		exe_ids = request.getParameterValues("dr_exe");
	}

	public String PSFEscDashboard() {
		try {
			int count = 0, jccount = 0;
			int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = "Select branch_id, branch_name, "
					+ " sum(if(jcpsf_trigger=1,1,0)) as level1, "
					+ " sum(if(jcpsf_trigger=2,1,0)) as level2, "
					+ " sum(if(jcpsf_trigger=3,1,0)) as level3, "
					+ " sum(if(jcpsf_trigger=4,1,0)) as level4, "
					+ " sum(if(jcpsf_trigger=5,1,0)) as level5, "
					+ " count(jcpsf_id) as jccount "
					+ " from " + compdb(comp_id) + "axela_branch "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_branch_id = branch_id"
					+ " left JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jc_id = jc_id"
					+ " AND jcpsf_jcpsffeedbacktype_id = 0 AND jcpsf_trigger > 0"
					+ " and jcpsf_jcpsfdays_id=2 and branch_active='1' "
					+ " where 1=1  and jc_active = 1" + StrSearch
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");
			}

			StrSql += " group by branch_id ";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>Branch</th>\n");
			Str.append("<th>Level 1</th>\n");
			Str.append("<th>Level 2</th>\n");
			Str.append("<th>Level 3</th>\n");
			Str.append("<th>Level 4</th>\n");
			Str.append("<th>Level 5</th>\n");
			Str.append("<th>Job Card Count</th>\n");
			Str.append("</tr>");
			while (crs.next()) {
				count++;
				jccount = jccount + crs.getInt("jccount");
				level1 = level1 + crs.getInt("level1");
				level2 = level2 + crs.getInt("level2");
				level3 = level3 + crs.getInt("level3");
				level4 = level4 + crs.getInt("level4");
				level5 = level5 + crs.getInt("level5");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>").append(count).append(".</b></td>\n");
				Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level1")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level2")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level3")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level4")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level5")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("jccount")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("<tr align=center>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td align=center><b>").append(level1).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level2).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level3).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level4).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level5).append("</b></td>\n");
			Str.append("<td align=center><b>").append(jccount).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			Str.append(getFollowupPriority());
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
			StrSql = "Select * "
					+ " from " + compdb(comp_id) + "axela_service_jc_priority "
					+ " order by priorityjc_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr><th colspan=9>Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center><b>#</b></td>\n");
				Str.append("<td align=center><b>Priority</b></td>\n");
				Str.append("<td align=center><b>Description</b></td>\n");
				Str.append("<td align=center><b>Due Hours</b></td>\n");
				Str.append("<td align=center><b>Level 1</b></td>\n");
				Str.append("<td align=center><b>Level 2</b></td>\n");
				Str.append("<td align=center><b>Level 3</b></td>\n");
				Str.append("<td align=center><b>Level 4</b></td>\n");
				Str.append("<td align=center><b>Level 5</b></td>\n");
				Str.append("</tr>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_desc")).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</table>");
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
