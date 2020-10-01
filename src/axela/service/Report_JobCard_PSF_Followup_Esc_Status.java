package axela.service;
// Sangita 28th may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Followup_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String[] team_ids, exe_ids;
	public String comp_id = "0";
	public String team_id = "", exe_id = "";
	public String branch_id = "0";
	public String msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if ((!dr_branch_id.equals("0"))) {
						StrSearch = " and jc_branch_id = " + dr_branch_id;
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
					}
					if (msg.equals("")) {
						StrHTML = PSFFollowupTriggerStatus();
					} else {
						msg = "Error!" + msg;
					}
				}
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
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
	}

	public String PSFFollowupTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, concat(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active='1' ";
			if (branch_id.equals("0")) {
				StrSql += " AND branch_branchtype_id = 3";
			} else {
				StrSql += " AND branch_id = " + dr_branch_id + "";
			}
			StrSql += " ORDER BY branch_name ";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"  table-hover table-bordered\">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				while (crs1.next()) {
					Str.append("<tr align=center><th style=\"text-align:center\" colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\"><b>Level 1</b></th>\n");
					Str.append("<th  data-hide=\"phone,tablet\"><b>Level 2</b></th>\n");
					Str.append("<th  data-hide=\"phone,tablet\"><b>Level 3</b></th>\n");
					Str.append("<th  data-hide=\"phone,tablet\"><b>Level 4</b></th>\n");
					Str.append("<th  data-hide=\"phone,tablet\"><b>Level 5</b></th>\n");
					Str.append("</tr>");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT jc_id, concat(emp_name,' (',emp_ref_no,')') as emp_name "
								+ " from " + compdb(comp_id) + "axela_service_jc  "
								+ " inner join " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jc_id = jc_id"
								+ " and jcpsf_desc = '' "
								+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
								+ " where 1=1 "
								+ " and jcpsf_trigger=" + i
								// + " and enquiry_status_id = 1 "
								+ StrSearch + BranchAccess.replace("branch_id", "jc_branch_id") + ExeAccess.replace("emp_id", "jc_emp_id")
								+ " group by jc_id order by emp_name";
						// SOP("StrSql=11=" + StrSqlBreaker(StrSql));
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								// Str.append("<a href=enquiry-list.jsp?enquiry_id=").append(crs2.getString("enquiry_id")).append(">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
								Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs2.getString("jc_id") + "#tabs-3")
										.append("','reportservice','');remote.focus();\">").append(crs2.getString("jc_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=left>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append(getFollowupPriority());
			} else {
				Str.append("<center><font color=red><b>No Details Found!</b></font></center>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateServiceExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND (emp_service_psf = 1"
					// + " OR emp_service_psf_iacs = 1"
					+ " OR emp_service = 1)"
					+ " AND (emp_branch_id = " + dr_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			// if (!team_id.equals("")) {
			// StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			// }
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=10 style=\"width:250px\">");
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
			StrSql = "Select * "
					+ " from " + compdb(comp_id) + "axela_service_jc_priority "
					+ " order by priorityjc_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br>");
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table   table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>");
				Str.append("<th style=\"text-align:center\" colspan=9>Priority</th></tr>");
				Str.append("<tr>\n");
				Str.append("<th style=\"text-align:center\" data-hide=\"phone\"><b>#</b></th>\n");
				Str.append("<th data-toggle=\"true\"><b>Priority</b></th>\n");
				Str.append("<th><b>Description</b></th>\n");
				Str.append("<th><b>Due Hours</b></th>\n");
				Str.append("<th data-hide=\"phone,tablet\"><b>Level 1</b></th>\n");
				Str.append("<th data-hide=\"phone,tablet\"><b>Level 2</b></th>\n");
				Str.append("<th data-hide=\"phone,tablet\"><b>Level 3</b></th>\n");
				Str.append("<th data-hide=\"phone,tablet\"><b>Level 4</b></th>\n");
				Str.append("<th data-hide=\"phone,tablet\"><b>Level 5</b></th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_desc")).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_duehrs"))).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=left>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger5_hrs"))).append("</td>\n");
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
