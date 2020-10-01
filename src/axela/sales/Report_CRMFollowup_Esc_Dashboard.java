package axela.sales;
// Sangita 28th may 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_CRMFollowup_Esc_Dashboard extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String team_id = "", exe_id = "", brand_id = "", region_id = "";
	public String StrSearch = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String dr_branch_id = "0";
	public String branch_id = "";
	public String comp_id = "0";
	public MIS_Check1 mischeck = new MIS_Check1();
	public String msg = "";
	public String emp_all_exe = "";
	public String crmdays_crmtype_id = "0";
	public String crmdays_id = "0";
	public Report_CRMFollowup rep = new Report_CRMFollowup();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
				GetValues(request, response);
				// SOP("branch_id===" + branch_id);
				if (go.equals("Go")) {
					if (!brand_id.equals("")) {

						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {

						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!crmdays_crmtype_id.equals("0")) {
						StrSearch = StrSearch + " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
					}
					if (!crmdays_id.equals("0")) {
						StrSearch = StrSearch + " AND crm_crmdays_id = " + crmdays_id;
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_emp_id IN (select teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_emp_id IN (" + exe_id + ")";
					}
				}
				StrHTML = CRMFollowupEscDashboard();
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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		// SOP("branch id===----==" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
	}

	public String CRMFollowupEscDashboard() {
		try {
			int count = 0, enquirycount = 0;
			int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, branch_name, "
					+ " SUM(IF(crm_trigger=1,1,0)) AS level1, "
					+ " SUM(IF(crm_trigger=2,1,0)) AS level2, "
					+ " SUM(IF(crm_trigger=3,1,0)) AS level3, "
					+ " SUM(IF(crm_trigger=4,1,0)) AS level4, "
					+ " SUM(IF(crm_trigger=5,1,0)) AS level5, "
					+ " count(crm_id) AS enquirycount "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_branch_id = branch_id AND enquiry_status_id=1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm on crm_enquiry_id = enquiry_id "
					+ " AND crm_trigger > 0 AND crm_desc = ''"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " WHERE 1=1 AND branch_active='1' " + StrSearch
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");

			StrSql += ExeAccess.replace("emp_id", "crm_emp_id");

			StrSql += " GROUP BY branch_id ";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-hide=\"phone\">#</th>\n");
			Str.append("<th data-toggle=\"true\">Branch</th>\n");
			Str.append("<th data-hide=\"phone\">Level 1</th>\n");
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
				enquirycount = enquirycount + crs.getInt("enquirycount");
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
				Str.append("<td align=center>").append(crs.getString("enquirycount")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("<tr align=center>\n");
			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			Str.append("<td align=center><b>").append(level1).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level2).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level3).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level4).append("</b></td>\n");
			Str.append("<td align=center><b>").append(level5).append("</b></td>\n");
			Str.append("<td align=center><b>").append(enquirycount).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			Str.append(getFollowupPriority());
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
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " ORDER BY priorityenquiry_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<br><div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th style=text-align:center colspan=9>Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>#</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Priority</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Description</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Due Hours</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Level 1</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Level 2</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Level 3</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Level 4</b></td>\n");
				Str.append("<td data-toggle=\"true\" align=center><b>Level 5</b></td>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_desc")).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToMins(crs.getString("priorityenquiry_trigger5_hrs"))).append("</td>\n");
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

	public String PopulateCRMType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmtype_id, crmtype_name"
					+ " FROM axela_sales_crm_type ORDER BY crmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"), crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
