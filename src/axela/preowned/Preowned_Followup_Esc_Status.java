package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Followup_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String comp_id = "0";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String branch_id = "0";
	public String msg = "";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id==" + branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_preowned_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("")) {
						StrSearch += " AND preowned_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_brand_id IN (" + brand_id + "))";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND preowned_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_region_id IN (" + region_id + "))";
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
					if (msg.equals("")) {
						StrHTML = EnquiryTriggerStatus();
					} else {
						msg = "Error!" + msg;
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		// SOP("region_ids-----------" + region_ids);
		// SOP("region_id-----------" + region_id);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		// SOP("exe_id-----------" + exe_id);
		// SOP("exe_ids-----------" + exe_ids);
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("")) {
			msg = msg + "<br>Select Branch!";
		}
	}

	public String EnquiryTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, concat(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1' ";
			if (!branch_id.equals("0")) {
				StrSql += "AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " ORDER BY branch_name ";
			// SOP("StrSql---------" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				while (crs1.next()) {
					Str.append("<thead><tr>\n");
					Str.append("<th style=\"text-align:center\" colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\"><b>Level 1</b></th>\n");
					Str.append("<th><b>Level 2</b></th>\n");
					Str.append("<th><b>Level 3</b></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Level 4</b></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Level 5</b></th>\n");
					Str.append("</tr>");
					Str.append("</thead>");
					Str.append("<tbody>");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT preowned_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
								+ " FROM " + compdb(comp_id) + "axela_preowned  "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup ON preownedfollowup_preowned_id = preowned_id"
								+ " AND preownedfollowup_desc = '' "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id";
						if (!team_id.equals("")) {
							StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id ";
						}
						StrSql += " WHERE 1 = 1 "
								+ " AND preownedfollowup_desc = ''"
								+ " AND preownedfollowup_trigger=" + i + ""
								+ " AND preowned_preownedstatus_id = 1 "
								+ StrSearch + BranchAccess.replace("branch_id", "preowned_branch_id") + ExeAccess
								+ " AND preowned_branch_id = " + crs1.getString("branch_id")
								+ " GROUP BY preowned_id"
								+ " ORDER BY emp_name";
						// SOP("StrSql========== " + i + " ==" + StrSql);
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								// Str.append("<a href=\"javascript:remote=window.open('enquiry-dash-followup.jsp?enquiry_id=").append(crs2.getString("enquiry_id")).append("','enquiryfollowup','');remote.focus();\">").append(": ").append(crs2.getString("emp_name")).
								// append("</a>");
								Str.append("<a href=\"javascript:remote=window.open('preowned-dash-followup.jsp?preowned_id=").append(crs2.getString("preowned_id"))
										.append("','reportsales','');remote.focus();\">").append(crs2.getString("preowned_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
								// Str.append("<a href=enquiry-dash-followup.jsp?enquiry_id=").append(crs2.getString("enquiry_id")).append(" target=_blank").append(">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
				Str.append(getFollowupPriority());
			} else {
				Str.append("<center><font color=red><b>No Details Found!</b></font></center>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getFollowupPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority "
					+ " ORDER BY prioritypreowned_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br><div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr><th colspan=9>Priority</th></tr>");
				Str.append("<tr>\n");
				Str.append("<th><b>#</b></th>\n");
				Str.append("<th><b>Priority</b></th>\n");
				Str.append("<th><b>Description</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Due Hours</b></th>\n");
				Str.append("<th data-hide=\"phone\"r><b>Level 1</b></th>\n");
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
