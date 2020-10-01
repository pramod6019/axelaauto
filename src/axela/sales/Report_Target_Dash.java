package axela.sales;
// smitha nag 2, 3 march 2013, 8 march 2013(edited)
//divya 9th may
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Target_Dash extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String drop_year = "";
	public String StrSearch = "";
	public String startdate = "", enddate = "";
	public String BranchSearch = "";
	public String TargetSearch = "";
	public String ModelSearch = "";
	public String DateSearch = "";
	public String ExeSearch = "";
	public String TeamSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	static DecimalFormat deci = new DecimalFormat("#.###");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));

				GetValues(request, response);
				if (go.equals("Go")) {

					TargetSearch = ExeAccess.replace("emp_id", "target_emp_id") + "";

					TargetSearch = TargetSearch + " AND target_startdate >= '" + startdate + "' AND target_startdate < '" + enddate + "'";
					DateSearch = " AND startdate >= '" + startdate + "' AND startdate < '" + enddate + "'";

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						TargetSearch = TargetSearch + " AND target_emp_id IN (" + exe_id + ")";
						ExeSearch = " AND emp_id IN (" + exe_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						TargetSearch = TargetSearch + " AND teamtrans_team_id IN (" + team_id + ")";
						TeamSearch = " AND teamtrans_team_id IN (" + team_id + ")";
						TeamSql = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id ";
					}
					if (!model_id.equals("")) {
						ModelSearch = ModelSearch + " AND model_id IN (" + model_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListTarget();

					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		startdate = PadQuotes(request.getParameter("dr_startmonth"));
		enddate = PadQuotes(request.getParameter("dr_endmonth"));
		if (!startdate.equals("") && !enddate.equals("")) {
			if (Double.parseDouble(startdate) > Double.parseDouble(enddate)) {
				msg = "<br>From date should be less than end date!";
			}
		}

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
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}

	public String ListTarget() {
		StringBuilder Str = new StringBuilder();
		int enquiry_count_t = 0, enquiry_count_a = 0, enquiry_count_perc = 0;
		int enquiry_calls_t = 0, enquiry_calls_a = 0, enquiry_calls_perc = 0;
		int enquiry_meetings_t = 0, enquiry_meetings_a = 0, enquiry_meetings_perc = 0;
		int enquiry_testdrives_t = 0, enquiry_testdrives_a = 0, enquiry_testdrives_perc = 0;
		int enquiry_hot_t = 0, enquiry_hot_a = 0, enquiry_hot_perc = 0;
		int enquiry_so_t = 0, enquiry_so_a = 0, enquiry_so_perc = 0;
		int enquiry_so_min_t = 0, enquiry_so_min_a = 0, enquiry_so_min_perc = 0;
		int enquiry_cw = 0, enquiry_cl = 0, enquiry_co = 0;
		int hot = 0;
		try {
			StrSql = "SELECT "
					+ " model_id, "
					+ " model_name, "
					+ " COALESCE (enquiry_count_t, 0) AS enquiry_count_t, "
					+ " COALESCE (enquiry_count_a, 0) AS enquiry_count_a, "
					+ " COALESCE ((100 * enquiry_count_a /enquiry_count_t), 0) AS enquiry_count_perc, "

					+ " COALESCE (enquiry_calls_t, 0) AS enquiry_calls_t, "
					+ " COALESCE (enquiry_calls_a, 0) AS enquiry_calls_a, "
					+ " COALESCE ((100 * enquiry_calls_a /enquiry_calls_t),	0) AS enquiry_calls_perc, "

					+ " COALESCE (enquiry_meetings_t, 0) AS enquiry_meetings_t, "
					+ " COALESCE (enquiry_meetings_a, 0) AS enquiry_meetings_a, "
					+ " COALESCE ((100 * enquiry_meetings_a /enquiry_meetings_t),	0) AS enquiry_meetings_perc, "

					+ " COALESCE (enquiry_testdrives_t, 0) AS enquiry_testdrives_t, "
					+ " COALESCE (enquiry_testdrives_a, 0) AS enquiry_testdrives_a, "
					+ " COALESCE ((100 * enquiry_testdrives_a /enquiry_testdrives_t),	0) AS enquiry_testdrives_perc, "

					+ " COALESCE (enquiry_hot_t, 0) AS enquiry_hot_t, "
					+ " COALESCE (enquiry_hot_a, 0) AS enquiry_hot_a, "
					+ " COALESCE ((100 * enquiry_hot_a /enquiry_hot_t),	0) AS enquiry_hot_perc, "

					+ " COALESCE (enquiry_so_t, 0) AS enquiry_so_t, "
					+ " COALESCE (enquiry_so_a, 0) AS enquiry_so_a, "
					+ " COALESCE ((100 * enquiry_so_a /enquiry_so_t),	0) AS enquiry_so_perc, "

					+ " COALESCE (enquiry_so_min_t, 0) AS enquiry_so_min_t, "
					+ " COALESCE (enquiry_so_a, 0) AS enquiry_so_min_a, "
					+ " COALESCE ((100 * enquiry_so_a /enquiry_so_min_t),	0) AS enquiry_so_amt_perc, "

					+ " COALESCE (enquiry_cw, 0) AS enquiry_cw, "
					+ " COALESCE (enquiry_cl, 0) AS enquiry_cl, "
					+ " COALESCE (enquiry_co, 0) AS enquiry_co "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = model_brand_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " SUM(modeltarget_enquiry_count) AS enquiry_count_t, "
					+ " SUM(modeltarget_enquiry_calls_count) AS enquiry_calls_t, "
					+ " SUM(modeltarget_enquiry_meetings_count) AS enquiry_meetings_t, "
					+ " SUM(modeltarget_enquiry_testdrives_count) AS enquiry_testdrives_t, "
					+ " SUM(modeltarget_enquiry_hot_count) AS enquiry_hot_t, "
					+ " SUM(modeltarget_so_count) AS enquiry_so_t, "
					+ " SUM(modeltarget_so_min) AS enquiry_so_min_t, "
					+ " modeltarget_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_target_model "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id "
					+ TeamSql
					+ " WHERE 1 = 1 "
					+ TargetSearch
					+ " GROUP BY modeltarget_model_id ) AS tblmodelenquirytargetcount ON tblmodelenquirytargetcount.modeltarget_model_id = model_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(DISTINCT enquiry_id) AS enquiry_count_a, "
					+ " COUNT(DISTINCT CASE	WHEN enquiry_priorityenquiry_id = 1 THEN enquiry_id	END) AS enquiry_hot_a, "
					+ " SUM(IF(enquiry_status_id = 2, 1, 0)) AS enquiry_cw, "
					+ " SUM(IF(enquiry_status_id = 3, 1, 0)) AS enquiry_cl, "
					+ " SUM(IF(enquiry_status_id = 4, 1, 0)) AS enquiry_co, "
					+ " enquiry_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = enquiry_branch_id "
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id "
					+ TeamSql.replace("target_emp_id", "enquiry_emp_id")
					+ " WHERE 1 = 1 "
					+ BranchSearch.replace("branch_id", "enquiry_branch_id") + DateSearch.replace("startdate", "enquiry_date")
					+ ExeSearch.replace("emp_id", "enquiry_emp_id") + TeamSearch
					+ " GROUP BY enquiry_model_id ) AS tblmodelenquirycount ON tblmodelenquirycount.enquiry_model_id = model_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(DISTINCT CASE	WHEN followup_followuptype_id = 1 THEN followup_id END) AS enquiry_calls_a, "
					+ " COUNT(DISTINCT CASE	WHEN followup_followuptype_id = 2 THEN enquiry_id END) AS enquiry_meetings_a, "
					+ " enquiry_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = enquiry_branch_id "
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id "
					+ TeamSql.replace("target_emp_id", "enquiry_emp_id")
					+ " WHERE 1 = 1"
					+ BranchSearch.replace("branch_id", "enquiry_branch_id") + DateSearch.replace("startdate", "enquiry_date")
					+ ExeSearch.replace("emp_id", "enquiry_emp_id") + TeamSearch
					+ " GROUP BY enquiry_model_id ) AS tblmodelenquiryfollowupcount ON tblmodelenquiryfollowupcount.enquiry_model_id = model_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(DISTINCT testdrive_id) AS enquiry_testdrives_a, "
					+ " enquiry_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = enquiry_branch_id "
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id "
					+ TeamSql.replace("target_emp_id", "enquiry_emp_id")
					+ " WHERE 1 = 1 "
					+ BranchSearch.replace("branch_id", "enquiry_branch_id") + DateSearch.replace("startdate", "enquiry_date")
					+ ExeSearch.replace("emp_id", "enquiry_emp_id") + TeamSearch
					+ " AND testdrive_fb_taken = 1 "
					+ " GROUP BY enquiry_model_id ) AS tblmodelenquirytdcount ON tblmodelenquirytdcount.enquiry_model_id = model_id "
					+ " LEFT JOIN ( "
					+ " SELECT "
					+ " COUNT(so_id) AS enquiry_so_a, "
					+ " enquiry_model_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = enquiry_branch_id "
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id "
					+ TeamSql.replace("target_emp_id", "enquiry_emp_id")
					+ " WHERE 1 = 1 "
					+ BranchSearch.replace("branch_id", "enquiry_branch_id") + DateSearch.replace("startdate", "enquiry_date")
					+ ExeSearch.replace("emp_id", "enquiry_emp_id") + TeamSearch
					+ " AND so_active = '1' "
					+ " GROUP BY enquiry_model_id ) AS tblmodelsocount ON tblmodelsocount.enquiry_model_id = model_id "
					+ " WHERE 1 = 1"
					+ " AND model_active = '1' "
					+ StrSearch
					+ BranchAccess
					+ ModelSearch
					+ " GROUP BY model_name "
					+ " ORDER BY brand_name, model_name ";
			// SOP("query list target--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\">\n");
				Str.append("<thead><tr>\n");
				// Str.append("<th>#</th>\n");
				Str.append("<th data-toggle=\"true\">Model</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=3>Enquiry Count</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=3>Enquiry Calls</th>\n");
				Str.append("<th data-hide=\"phone\" colspan=3>Enquiry Meeting</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Enquiry Test Drives</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Enquiry Hot</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>SO Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>SO Min</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">CW</th>\n");
				Str.append("<th v>CL</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">CO</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">HR</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">BE</th>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">T</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">A</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">%</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">&nbsp;</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					// count++;
					enquiry_count_t = enquiry_count_t + crs.getInt("enquiry_count_t");
					enquiry_count_a = enquiry_count_a + crs.getInt("enquiry_count_a");
					// SOP("enquiry_count_perc--------" + enquiry_count_perc);
					enquiry_count_perc = (int) (enquiry_count_perc + crs.getDouble("enquiry_count_perc"));
					enquiry_calls_t = enquiry_calls_t + crs.getInt("enquiry_calls_t");
					enquiry_calls_a = enquiry_calls_a + crs.getInt("enquiry_calls_a");
					// SOP("enquiry_calls_perc--------" + crs.getInt("enquiry_calls_perc"));
					enquiry_calls_perc = (int) (enquiry_calls_perc + crs.getDouble("enquiry_calls_perc"));
					enquiry_meetings_t = enquiry_meetings_t + crs.getInt("enquiry_meetings_t");
					enquiry_meetings_a = enquiry_meetings_a + crs.getInt("enquiry_meetings_a");
					enquiry_meetings_perc = (int) (enquiry_meetings_perc + crs.getDouble("enquiry_meetings_perc"));
					enquiry_testdrives_t = enquiry_testdrives_t + crs.getInt("enquiry_testdrives_t");
					enquiry_testdrives_a = enquiry_testdrives_a + crs.getInt("enquiry_testdrives_a");
					enquiry_testdrives_perc = (int) (enquiry_testdrives_perc + crs.getDouble("enquiry_testdrives_perc"));
					enquiry_hot_t = enquiry_hot_t + crs.getInt("enquiry_hot_t");
					enquiry_hot_a = enquiry_hot_a + crs.getInt("enquiry_hot_a");
					enquiry_hot_perc = (int) (enquiry_hot_perc + crs.getDouble("enquiry_hot_perc"));
					enquiry_so_t = enquiry_so_t + crs.getInt("enquiry_so_t");
					enquiry_so_a = enquiry_so_a + crs.getInt("enquiry_so_a");
					enquiry_so_perc = (int) (enquiry_so_perc + crs.getDouble("enquiry_so_perc"));
					enquiry_so_min_t = (int) (enquiry_so_min_t + crs.getDouble("enquiry_so_min_t"));
					enquiry_so_min_a = (int) (enquiry_so_min_a + crs.getDouble("enquiry_so_min_a"));
					enquiry_so_min_perc = (int) (enquiry_so_min_perc + crs.getDouble("enquiry_so_amt_perc"));
					enquiry_cw = enquiry_cw + crs.getInt("enquiry_cw");
					enquiry_cl = enquiry_cl + crs.getInt("enquiry_cl");
					enquiry_co = enquiry_co + crs.getInt("enquiry_co");

					Str.append("<tr>\n");
					// Str.append("<td valign=top align=center >").append(count).append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("model_name")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_count_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_count_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_count_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_calls_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_calls_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_calls_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_meetings_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_meetings_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_meetings_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_testdrives_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_testdrives_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_testdrives_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_hot_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_hot_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_hot_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_t")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_so_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_so_min_t"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("enquiry_so_min_a")).append("</td>");
					Str.append("<td valign=top align=right>").append((int) (crs.getDouble("enquiry_so_amt_perc"))).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_cw")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_cl")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_co")).append("</td>");
					Str.append("<td valign=top align=right>").append(getPercentage(crs.getInt("enquiry_so_a"), crs.getInt("enquiry_count_a"))).append("</td>");
					if (crs.getInt("enquiry_hot_a") / 2 <= crs.getInt("enquiry_so_a"))
						hot = crs.getInt("enquiry_so_a");
					else
						hot = crs.getInt("enquiry_hot_a") / 2;
					Str.append("<td valign=top align=right>").append(hot).append("</td>");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td valign=top align=right><b>Total:</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_count_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_count_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_count_a, enquiry_count_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_calls_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_calls_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_calls_a, enquiry_calls_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_meetings_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_meetings_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_meetings_a, enquiry_meetings_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_testdrives_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_testdrives_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_testdrives_a, enquiry_testdrives_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_hot_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_hot_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_hot_a, enquiry_hot_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_so_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_so_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_so_a, enquiry_so_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_so_min_t).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_so_min_a).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_so_min_a, enquiry_so_min_t)).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_cw).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_cl).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(enquiry_co).append("</b></td>");
				Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_so_a, enquiry_count_a)).append("</b></td>");
				if (enquiry_hot_a / 2 <= enquiry_so_a)
					hot = enquiry_so_a;
				else
					hot = enquiry_hot_a / 2;
				Str.append("<td valign=top align=right><b>").append(hot).append("</b></td>");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("<br>CW = Closed Won");
				Str.append("<br>CL = Closed Lost");
				Str.append("<br>CO = Closed Others");
				Str.append("<br>HR = Hit Ratio");
				Str.append("<br>BE = Bulls Eye");
			} else {
				Str.append("<br><br><br><br><font color=red>No Target(s) found!</font><br><br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateStartMonth() {
		if (startdate.equals("")) {
			startdate = SplitYear(ToLongDate(kknow())) + SplitMonth(ToLongDate(kknow())) + "01000000";
		}
		StringBuilder year = new StringBuilder();
		int curryear = Integer.parseInt(SplitYear(ToLongDate(kknow())));
		for (int i = curryear - 1; i <= curryear + 1; i++) {
			for (int j = 1; j <= 12; j++) {
				year.append("<option value=").append(i).append(doublenum(j)).append("01000000 ").append(StrSelectdrop(i + doublenum(j) + "01000000", startdate)).append(">")
						.append(StrShorttoMonthYear("01/" + doublenum(j) + "/" + i)).append("</option>\n");
			}
		}
		return year.toString();
	}

	public String PopulateEndMonth() {
		if (enddate.equals("")) {
			enddate = SplitYear(ToLongDate(kknow())) + SplitMonth(ToLongDate(kknow())) + "01000000";
		}
		StringBuilder year = new StringBuilder();
		int curryear = Integer.parseInt(SplitYear(ToLongDate(kknow())));
		for (int i = curryear - 1; i <= curryear + 1; i++) {
			for (int j = 1; j <= 12; j++) {
				year.append("<option value=").append(i).append(doublenum(j)).append("01000000 ").append(StrSelectdrop(i + doublenum(j) + "01000000", enddate)).append(">")
						.append(StrShorttoMonthYear("01/" + doublenum(j) + "/" + i)).append("</option>\n");
			}
		}
		return year.toString();
	}

	public String PopulateYear() {
		String year = "";
		int curryear = Integer.parseInt(SplitYear(ToLongDate(kknow())));
		for (int i = curryear - 1; i <= curryear + 1; i++) {
			year = year + "<option value = " + i + "" + Selectdrop(i, drop_year) + ">" + i + "</option>\n";
		}
		return year;
	}
}
