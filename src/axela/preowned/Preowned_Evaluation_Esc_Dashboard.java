package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Evaluation_Esc_Dashboard extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String[] preowned_team_id, preowned_emp_id;
	public String dr_branch_id = "0";
	public String branch_id = "0";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// SOP("inside==========");
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_preowned_access", request, response);

				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {
					if (!brand_id.equals("")) {
						StrSearch = " AND branch_brand_id in (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND preowned_branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and preowned_emp_id in (" + exe_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND preownedteamtrans_team_id IN (" + team_id + ") ";
					}
				}
				StrHTML = PreownedEscDashboard();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	public String PreownedEscDashboard() {
		try {
			int count = 0, preownedcount = 0;
			int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, branch_name, "
					+ " SUM(IF(preowned_eval_trigger=1,1,0)) AS level1, "
					+ " SUM(IF(preowned_eval_trigger=2,1,0)) AS level2, "
					+ " SUM(IF(preowned_eval_trigger=3,1,0)) AS level3, "
					+ " SUM(IF(preowned_eval_trigger=4,1,0)) AS level4, "
					+ " SUM(IF(preowned_eval_trigger=5,1,0)) AS level5, "
					+ " COUNT(preowned_id) AS preownedcount "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned ON preowned_branch_id = branch_id ";
			if (!team_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id ";
			}
			StrSql += " AND preowned_preownedstatus_id = 1"
					+ " AND preowned_eval_trigger > 0  "
					+ " WHERE 1 = 1 "
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id = 2"
					+ StrSearch
					+ BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY branch_id ";
			// SOP("StrSql-----Ecal Esc Dash------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// if (crs.isBeforeFirst()) {
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-toggle=\"true\">#</th>\n");
			Str.append("<th >Branch</th>\n");
			Str.append("<th>Level 1</th>\n");
			Str.append("<th data-hide=\"phone\">Level 2</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Level 3</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Level 4</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Level 5</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
			Str.append("</tr>");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			while (crs.next()) {
				count++;
				preownedcount = preownedcount + crs.getInt("preownedcount");
				level1 = level1 + crs.getInt("level1");
				level2 = level2 + crs.getInt("level2");
				level3 = level3 + crs.getInt("level3");
				level4 = level4 + crs.getInt("level4");
				level5 = level5 + crs.getInt("level5");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>").append(count).append("</b></td>\n");
				Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level1")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level2")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level3")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level4")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("level5")).append("</td>\n");
				Str.append("<td align=center>").append(crs.getString("preownedcount")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("<tr align=center>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td align=center><b>").append(level1).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level2).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level3).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level4).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level5).append("</b></td>\n");
			Str.append("<td align=center><b>").append(preownedcount).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</tbody>\n");
			Str.append("</table>");
			Str.append("</div>");
			Str.append(getEvaluationPriority());
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getEvaluationPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority "
					+ " ORDER BY prioritypreowned_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				// Str.append("<tr><th colspan=9>Priority</th></tr>");
				// Str.append("<tr align=center>\n");
				Str.append("<br><div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr><center><b>Priority</b></center></tr>");
				Str.append("<td data-toggle=\"true\"><b>#</b></th>\n");
				Str.append("<th><b>Priority</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Description</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Due Hours</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 1</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 2</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 3</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 4</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Level 5</b></th>\n");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("prioritypreowned_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("prioritypreowned_desc")).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("prioritypreowned_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
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
