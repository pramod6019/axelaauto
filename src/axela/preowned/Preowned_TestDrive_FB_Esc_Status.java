package axela.preowned;
//
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive_FB_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String branch_id = "0";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();
	public static String msg = "";
	public String comp_id = "0";

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
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_opportunity_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if (!brand_id.equals("")) {
						StrSearch = " AND branch_brand_id in (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
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
				StrHTML += getFollowupPriority();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// if (branch_id.equals("0")) {
		// dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
		// } else {
		// dr_branch_id = branch_id;
		// }

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		// branch_id = PadQuotes(request.getParameter("dr_branch"));
		// branch_ids = PadQuotes(request.getParameter("dr_branch"));
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		SOP("branch_id==" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";
		/*
		 * if (dr_branch_id.equals("0")) { msg = msg + "<br>Select Branch!"; }
		 */
	}

	public String EnquiryTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, CONCAT(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1'"
					+ " AND branch_branchtype_id = '2' ";
			if (!dr_branch_id.equals("0")) {
				StrSql += " AND branch_id IN( " + dr_branch_id + ")";
			}
			StrSql += " ORDER BY branch_name ";
			SOP("StrSql==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");

				while (crs1.next()) {
					Str.append("<thead><tr>\n");
					Str.append("<th style=\"text-align:center\" colspan=5>").append(crs1.getString("branch_name")).append("</b></th>").append("</tr>");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\"><b>Level 1</b></th>\n");
					Str.append("<th><b>Level 2</b></th>\n");
					Str.append("<th><b>Level 3</b></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Level 4</b></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Level 5</b></th>\n");
					Str.append("</tr>");
					Str.append("</thead>");
					Str.append("</tbody>");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT testdrive_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
								+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive  "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id";
						if (!team_id.equals("")) {
							StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = emp_id ";
						}
						StrSql += " WHERE 1 = 1"
								+ " AND testdrive_fb_taken = 0"
								+ " AND testdrive_mileage_entry_id !=0"
								+ " AND testdrive_trigger=" + i
								+ StrSearch
								+ BranchAccess.replace("branch_id", "enquiry_branch_id") + ExeAccess
								+ " GROUP BY testdrive_id"
								+ " ORDER BY emp_name";
						SOP("StrSql==" + StrSql);
						CachedRowSet crs2 = processQuery(StrSql, 0);

						crs2.beforeFirst();
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								Str.append("<a href=\"javascript:remote=window.open('testdrive-list.jsp?testdrive_id=").append(crs2.getString("testdrive_id"))
										.append("','reportsales','');remote.focus();\">").append(crs2.getString("testdrive_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
								// +
								// "<a href=testdrive-list.jsp?testdrive_id=").append(crs2.getString("testdrive_id")).append(" target=_blank>").append(crs2.getString("testdrive_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
					Str.append("</tbody>");
				}
				Str.append("</table><br>");
				Str.append("</div>");
				// Str.append(getFollowupPriority());
			} else {
				Str.append("<font color=red><center><b>No Details Found!</b></center></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " group by team_id "
					+ " order by team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
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
			StrSql = "Select * "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " order by priorityenquiry_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr><center><b>Priority</b></center></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<th data-toggle=\"true\"><b>#</b></th>\n");
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
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_desc")).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append((crs.getString("priorityenquiry_trigger5_hrs"))).append("</td>\n");
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
