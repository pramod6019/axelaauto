package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_CRMFollowup_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String comp_id = "0";
	public String team_id = "", exe_id = "";
	public String branch_id = "", brand_id = "", region_id = "";
	public String crmdays_id = "0";
	public String crmdays_crmtype_id = "0";
	public String msg = "";
	public String emp_all_exe = "";
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();
	public Report_CRMFollowup rep = new Report_CRMFollowup();

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
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
				crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
				// SOP("crmdays_id--------2--------" + crmdays_id);
				GetValues(request, response);

				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if ((!branch_id.equals(""))) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					if (!crmdays_crmtype_id.equals("0")) {
						StrSearch = StrSearch + " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
					}
					if (!crmdays_id.equals("0")) {
						StrSearch = StrSearch + " AND crm_crmdays_id = " + crmdays_id;
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_team_id IN (" + team_id + " )";
					}
					// SOP("StrSearch-----" + StrSearch);
					if (msg.equals("")) {
						StrHTML = CRMFollowupTriggerStatus();
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
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
		crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (crmdays_crmtype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
	}

	public String CRMFollowupTriggerStatus() {
		try {
			String branch_id1 = "";
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, CONCAT(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1' ";
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
			// SOP("StrSql----123-------" + StrSql);
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
						StrSql = " SELECT enquiry_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crm_crmdays_id = crmdays_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
								+ " AND crm_desc = '' "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = crm_emp_id"
								+ " WHERE 1=1 "
								+ " AND crm_trigger=" + i + " AND enquiry_status_id = 1 "
								+ StrSearch
								+ BranchAccess.replace("branch_id", "enquiry_branch_id");
						// if (!branch_id.equals("")) {
						// StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id");
						// }
						if (!branch_id1.equals("")) {
							StrSql += " AND enquiry_branch_id=" + branch_id1;
						}

						StrSql += ExeAccess.replace("emp_id", "crm_emp_id");

						StrSql += " GROUP BY crm_id order by emp_name";
						// SOP("StrSql------ee------" + StrSql);
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								// Str.append("<a href=enquiry-list.jsp?enquiry_id=").append(crs2.getString("enquiry_id")).append(">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
								Str.append("<a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs2.getString("enquiry_id") + "#tabs-3")
										.append("','reportsales','');remote.focus();\">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
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

	public String getFollowupPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select * "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " order by priorityenquiry_name";
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
}
