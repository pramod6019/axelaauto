package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Followup_Trigger_Status extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "", StrSql = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String dr_branch_id = "";
	public String msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				if (!branch_id.equals("0")) {
					StrSearch = " AND branch_id = " + branch_id;
					StrSearch = StrSearch + BranchAccess;
					StrHTML = FollowupTriggerStatus();
				}

				if (!dr_branch_id.equals("0") && !dr_branch_id.equals("")) {
					StrSearch = " AND branch_id = " + dr_branch_id;
					StrSearch = StrSearch + BranchAccess;
					StrHTML = FollowupTriggerStatus();
				}
				if (dr_branch_id.equals("0") || dr_branch_id.equals("")) {
					msg = "Select Branch!";
				}
			}
			// SOP("StrSearch--" + StrSearch);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String FollowupTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, concat(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE 1=1"
					+ " AND branch_active='1' "
					+ StrSearch
					+ " ORDER BY branch_name";
			// SOP("StrSql center --" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs1.next()) {
					Str.append("<tr align=center><td colspan=5 align=center><br><b>" + crs1.getString("branch_name")
							+ "</b></td></tr>");
					Str.append("<tr align=center>\n");
					Str.append("<td align=center><b> Level 1 </b></td>\n");
					Str.append("<td align=center><b> Level 2 </b></td>\n");
					Str.append("<td align=center><b> Level 3 </b></td>\n");
					Str.append("<td align=center><b> Level 4</b></td>\n");
					Str.append("<td align=center><b> Level 5</b></td>\n");
					Str.append("</tr>");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = "SELECT student_id, student_pid, student_name, student_no,"
								+ " CONCAT(branch_name,' (',student_no,')') AS studentno "
								+ " FROM " + compdb(comp_id) + "axela_followup "
								+ " INNER JOIN " + compdb(comp_id) + "axela_student ON student_id = followup_student_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=student_branch_id "
								+ " WHERE student_active='1'"
								+ " AND student_enqfollowup_active='1' "
								+ " AND followup_desc = ''"
								+ " AND followup_trigger=" + i + " "
								+ " AND student_branch_id=" + crs1.getString("branch_id");
						// SOP("FollowupTriggerStatus strsql===" + StrSql);
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td  valign=top align=left>");
							while (crs2.next()) {
								Str.append("<a href=../stud/student-pop.jsp?Update=yes&student_id=" + crs2.getString("student_id") + " target=_blank>"
										+ crs2.getString("student_name") + " (" + crs2.getString("student_pid") + ")</a><br><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td  valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</table>");
				Str.append(getPriorityStatus());
			} else {
				Str.append("<font color=red><b>No Follow-up Escalation found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getPriorityStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_priority"
					+ " WHERE 1=1"
					+ " ORDER BY priorityenqfollowup_rank";
			// SOP("StrSql getPriorityStatus --" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br><b>Priority Legend</b><br>");
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center><b> # </b></td>\n");
				Str.append("<td align=center><b> Name </b></td>\n");
				Str.append("<td align=center><b> Description </b></td>\n");
				Str.append("<td align=center><b> Due Hours </b></td>\n");
				Str.append("<td align=center><b> Level 1</b></td>\n");
				Str.append("<td align=center><b> Level 2</b></td>\n");
				Str.append("<td align=center><b> Level 3</b></td>\n");
				Str.append("<td align=center><b> Level 4</b></td>\n");
				Str.append("<td align=center><b> Level 5</b></td>\n");
				Str.append("</tr>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>" + count + ".</td>\n");
					Str.append("<td align=left>" + crs.getString("priorityenqfollowup_name") + "</td>\n");
					Str.append("<td align=left>" + crs.getString("priorityenqfollowup_desc") + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_duehrs")) + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_trigger1_hrs")) + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_trigger2_hrs")) + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_trigger3_hrs")) + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_trigger4_hrs")) + "</td>\n");
					Str.append("<td align=center>" + ConvertHoursToDaysHrsMins(crs.getDouble("priorityenqfollowup_trigger5_hrs")) + "</td>\n");
					Str.append("</tr>");
				}
				Str.append("</table>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT branch_id, branch_name, branch_code "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE 1=1"
					+ " AND branch_active='1' " + BranchAccess
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("branch_id") + "");
				Str.append(StrSelectdrop(crs.getString("branch_id"), dr_branch_id));
				Str.append(">" + crs.getString("branch_name") + " (" + crs.getString("branch_code") + ")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
