package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_ServiceFollowup_Esc_Status extends Connect {
	
	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String[] zone_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String comp_id = "0";
	public String zone_id = "", exe_id = "";
	public String branch_id = "", brand_id = "", region_id = "";
	public String msg = "";
	public String emp_all_exe = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id==" + branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				
				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					
					if ((!branch_id.equals(""))) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					// SOP("StrSearch-----" + StrSearch);
					if (msg.equals("")) {
						StrHTML = FollowupTriggerStatus();
					} else {
						msg = "Error!" + msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		exe_id = request.getParameter("dr_emp_id");
		// exe_ids = request.getParameterValues("dr_emp_id");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
	}
	
	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		// if (crmdays_crmtype_id.equals("0")) {
		// msg = msg + "<br>Select Type!";
		// }
	}
	
	public String FollowupTriggerStatus() {
		try {
			String branch_id1 = "";
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, CONCAT(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1' "
					+ " AND branch_esc_serviceveh_followup = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ")";
			}
			StrSql += BranchAccess;
			StrSql += " ORDER BY branch_name ";
			// SOPInfo("Vehfollwupesc----123-------" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				while (crs1.next()) {
					branch_id1 = crs1.getString("branch_id");
					Str.append("<thead>\n");
					Str.append("<tr><th style=\"text-align:center\" data-toggle=\"true\" colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
					Str.append("<tr align=center>\n");
					Str.append("<td data-hide=\"phone\" align=center><b>Level 1</b></td>\n");
					Str.append("<td data-hide=\"phone\" align=center><b>Level 2</b></td>\n");
					Str.append("<td data-hide=\"phone\" align=center><b>Level 3</b></td>\n");
					Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 4</b></td>\n");
					Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 5</b></td>\n");
					Str.append("</tr>");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT veh_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
								+ " FROM " + compdb(comp_id) + "axela_service_veh "
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = veh_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
								+ " AND vehfollowup_desc = '' "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_emp_id"
								+ " WHERE 1=1 "
								+ " AND vehfollowup_trigger=" + i + ""
								+ StrSearch
								+ BranchAccess.replace("branch_id", "veh_branch_id");
						
						// if (!branch_id1.equals("")) {
						// StrSql += " AND veh_branch_id=" + branch_id1;
						// }
						
						StrSql += ExeAccess.replace("emp_id", "vehfollowup_emp_id");
						
						StrSql += " GROUP BY veh_id order by emp_name";
						// SOPInfo("Vehfollwupesc------------" + StrSql);
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								Str.append("<a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs2.getString("veh_id") + "#tabs-4")
										.append("','reportservice','');remote.focus();\">").append(crs2.getString("veh_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
				Str.append(getFollowupPriority());
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
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
					+ " FROM " + compdb(comp_id) + "axela_service_vehfollowup_priority"
					+ " ORDER BY priorityvehfollwup_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br><div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th style=\"text-align:center\" data-toggle=\"true\" colspan=9>Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>#</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>Priority</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Description</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Due Hours</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 1</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 2</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 3</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 4</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\" align=center><b>Level 5</b></td>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityvehfollwup_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityvehfollwup_desc")).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityvehfollwup_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	
	public String PopulatePSFExecutive(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					// + " ("
					+ " AND emp_service_psf = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND (emp_branch_id IN ( " + branch_id + " )"
						+ " OR emp_id = 1"
						+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
						+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id";
				
				StrSql += " AND empbr.emp_branch_id IN ( " + branch_id + " )";
				StrSql += " )) ";
			}
			
			StrSql += ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			
			// SOP("StrSql----" + StrSql);
			
			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
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
}
