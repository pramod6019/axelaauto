package axela.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_Dashboard extends Connect {

	public String jc_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "", StrSearch = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String JCsToday = "";
	public String ReadyJCsToday = "";
	public String DeliveredJCsToday = "";
	public String OverdueStage = "";
	public String OverduePriority = "";
	public String OpenServiceStage = "";
	public String OpenPriority = "";
	public String OpenType = "";
	public String OpenTypeToday = "";
	public String OpenBodyShop = "";
	public String day1name = "", day1 = "", day2name = "", day2 = "", day3name = "", day3 = "", day4name = "", day4 = "", day5name = "", day5 = "", day6name = "", day6 = "", day7name = "", day7 = "",
			day8name = "", day8 = "";
	public String cday1name = "", cday1 = "", cday2name = "", cday2 = "", cday3name = "", cday3 = "", cday4name = "", cday4 = "", cday5name = "", cday5 = "", cday6name = "", cday6 = "",
			cday7name = "", cday7 = "";
	public int week1, week2, week3, week4;
	public String logWeek1, logWeek2, logWeek3, logWeek4;
	public String closeWeek1, closeWeek2, closeWeek3, closeWeek4;
	public String month1, month2, month3, month4;
	public String logMonth1, logMonth2, logMonth3, logMonth4;
	public String closeMonth1, closeMonth2, closeMonth3, closeMonth4;
	public String qur1, qur2, qur3, qur4, quarter;
	public String logQur1, logQur2, logQur3, logQur4;
	public String closeQur1, closeQur2, closeQur3, closeQur4;
	public String branch_id = "0";
	public String dr_branch_id = "0", go = "";
	public String[] technicianexe_ids, advisorexe_ids;
	public String ageingchart_data = "";
	public String technicianexe_id = "", advisorexe_id = "";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));

				GetValues(request, response);
				if (go.equals("Go")) {
					// /StrSeach = ExeAccess + BranchAccess.replace("branch_id",
					// "jc_branch_id") + "";

					if (!technicianexe_id.equals("")) {
						StrSearch = StrSearch + " AND jc_technician_emp_id in (" + technicianexe_id + ")";
					}
					if (!advisorexe_id.equals("")) {
						StrSearch = StrSearch + " AND jc_emp_id in (" + advisorexe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND jc_branch_id =" + dr_branch_id;
					}
					// if (!msg.equals("")) {
					// msg = "Error!" + msg;
					// }
				}
				Stats();
				AgeingStatus();
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
		technicianexe_id = RetrunSelectArrVal(request, "dr_technician");
		technicianexe_ids = request.getParameterValues("dr_technician");
		advisorexe_id = RetrunSelectArrVal(request, "dr_advisor");
		advisorexe_ids = request.getParameterValues("dr_advisor");
	}

	public void Stats() throws UnsupportedEncodingException {

		String startWeek, endWeek, startMonth, endMonth;
		StringBuilder StrJCsToday = new StringBuilder();
		StringBuilder StrOpenTypeToday = new StringBuilder();
		StringBuilder StrReadyJCsToday = new StringBuilder();
		StringBuilder StrDeliveredJCsToday = new StringBuilder();
		StringBuilder StrOverdueStage = new StringBuilder();
		StringBuilder StrOverduePriority = new StringBuilder();
		StringBuilder StrOpenStage = new StringBuilder();
		StringBuilder StrOpenPriority = new StringBuilder();
		StringBuilder StrOpenType = new StringBuilder();
		StringBuilder StrOpenBodyShop = new StringBuilder();

		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(cal.MONTH);
		int currYear = cal.get(cal.YEAR);
		int currDate = cal.get(cal.DATE);

		// -----------------------------Logged
		// Today-----------------------------------------------------------------------------
		try {
			StrSql = "Select jcstage_id, jcstage_name, count(jc_id)  AS jccount"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jcstage_id = jcstage_id "
					+ " AND jc_active=1"
					+ " AND substring(jc_time_in,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " WHERE 1=1"
					+ " GROUP BY jcstage_id "
					+ " ORDER BY jcstage_rank";
			// SOP("StrSql==" + StrSql);
			int count = 0;
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrJCsToday.append("<tr align=center>");
				StrJCsToday.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				StrJCsToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jctoday=yes&stage_id=").append(crs.getString("jcstage_id")).append("&starttime=")
						.append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrJCsToday.append("</tr>");
			}
			crs.close();
			StrJCsToday.append("<tr align=center>");
			StrJCsToday.append("<td align=right><b>Total: </b></td>");
			StrJCsToday.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jctoday=yes&starttime=").append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">")
					.append(count).append("</a></b></td>");
			StrJCsToday.append("</tr>");
			JCsToday = StrJCsToday.toString();

			// /////////Job Cards Type For Today///////
			StrSql = "SELECT jctype_id, jctype_name, count(jc_id)  AS jccount "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jctype_id = jctype_id "
					+ " WHERE 1=1 "
					+ " AND jc_active=1"
					+ " AND substring(jc_time_in,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id")
					+ StrSearch
					+ " GROUP BY jctype_id "
					+ " ORDER BY jctype_name ";
			SOP("StrSql===" + StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOpenTypeToday.append("<tr align=center>");
				StrOpenTypeToday.append("<td align=left>").append(crs.getString("jctype_name")).append("</td>");
				StrOpenTypeToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopentypetoday=yes&type_id=").append(crs.getString("jctype_id")).append("&starttime=")
						.append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOpenTypeToday.append("</tr>");

			}
			crs.close();
			StrOpenTypeToday.append("<tr align=center>");
			StrOpenTypeToday.append("<td align=right><b>Total: </b></td>");
			StrOpenTypeToday.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopentypetoday=yes").append("&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOpenTypeToday.append("</tr>");
			OpenTypeToday = StrOpenTypeToday.toString();

			StrSql = "Select count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc"
					+ " where jc_active=1 AND jc_jcstage_id=5"
					+ " AND substring(jc_time_ready,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;

			StrReadyJCsToday.append("<tr align=center>");
			StrReadyJCsToday.append("<td align=left>").append("Ready").append("</td>");
			StrReadyJCsToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcready=yes&stage_id=").append("5").append("&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(ExecuteQuery(StrSql)).append("</a></td>");
			StrReadyJCsToday.append("</tr>");
			ReadyJCsToday = StrReadyJCsToday.toString();

			StrSql = "Select count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc"
					+ " where jc_active=1 AND jc_jcstage_id=6"
					+ " AND substring(jc_time_out,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;

			StrDeliveredJCsToday.append("<tr align=center>");
			StrDeliveredJCsToday.append("<td align=left>").append("Delivered").append("</td>");
			StrDeliveredJCsToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcdelivered=yes&stage_id=").append("6").append("&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(ExecuteQuery(StrSql)).append("</a></td>");
			StrDeliveredJCsToday.append("</tr>");
			DeliveredJCsToday = StrDeliveredJCsToday.toString();

			StrSql = "Select jcstage_id, jcstage_name, count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " AND jc_active=1"
					+ " AND jc_time_ready=''"
					+ " AND substring(jc_time_promised,1,8) <= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1"
					+ " group by jcstage_id "
					+ " order by jcstage_rank";
			// SOP("StrSql=="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOverdueStage.append("<tr align=center>");
				StrOverdueStage.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				StrOverdueStage.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcoverduestage=yes&stage_id=").append(crs.getString("jcstage_id")).append("&starttime=")
						.append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOverdueStage.append("</tr>");
			}
			crs.close();
			StrOverdueStage.append("<tr align=center>");
			StrOverdueStage.append("<td align=right><b>Total: </b></td>");
			StrOverdueStage.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcoverduestage=yes&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOverdueStage.append("</tr>");
			OverdueStage = StrOverdueStage.toString();

			StrSql = "Select priorityjc_id, priorityjc_name, count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc_priority "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_priorityjc_id = priorityjc_id "
					+ " AND jc_active=1"
					+ " AND jc_time_ready=''"
					+ " AND substring(jc_time_promised,1,8) <= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1"
					+ " group by priorityjc_id "
					+ " order by priorityjc_rank";
			// SOP("StrSql=="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOverduePriority.append("<tr align=center>");
				StrOverduePriority.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>");
				StrOverduePriority.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcoverduepriority=yes&priority_id=").append(crs.getString("priorityjc_id")).append("&starttime=")
						.append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOverduePriority.append("</tr>");
			}
			crs.close();
			StrOverduePriority.append("<tr align=center>");
			StrOverduePriority.append("<td align=right><b>Total: </b></td>");
			StrOverduePriority.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcoverduepriority=yes&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOverduePriority.append("</tr>");
			OverduePriority = StrOverduePriority.toString();

			StrSql = "Select jcstage_id, jcstage_name, count(jc_id)  as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " AND jc_time_out='' AND jc_active=1 AND jc_jctype_id!=3"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by jcstage_id "
					+ " order by jcstage_rank ";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOpenStage.append("<tr align=center>");
				StrOpenStage.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				StrOpenStage.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopen=yes&stage_id=").append(crs.getString("jcstage_id"))
						.append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOpenStage.append("</tr>");

			}
			crs.close();
			StrOpenStage.append("<tr align=center>");
			StrOpenStage.append("<td align=right><b>Total: </b></td>");
			StrOpenStage.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopen=yes").append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOpenStage.append("</tr>");
			OpenServiceStage = StrOpenStage.toString();

			StrSql = "Select priorityjc_id, priorityjc_name, count(jc_id)  as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_priority  "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_priorityjc_id = priorityjc_id "
					+ " AND jc_time_out='' AND jc_active=1"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by priorityjc_id"
					+ " order by priorityjc_rank";
			// SOP("StrSql priority ==="+StrSqlBreaker(StrSql));
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOpenPriority.append("<tr align=center>");
				StrOpenPriority.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>");
				StrOpenPriority.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcpriority=yes&priority_id=").append(crs.getString("priorityjc_id")).append("&starttime=")
						.append(ToLongDate(kknow())).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOpenPriority.append("</tr>");
			}
			crs.close();
			StrOpenPriority.append("<tr align=center>");
			StrOpenPriority.append("<td align=right><b>Total: </b></td>");
			StrOpenPriority.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcpriority=yes").append("&starttime=").append(ToLongDate(kknow()))
					.append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOpenPriority.append("</tr>");
			OpenPriority = StrOpenPriority.toString();

			StrSql = "Select jctype_id, jctype_name, count(jc_id)  as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_type "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jctype_id = jctype_id "
					+ " AND jc_time_out='' AND jc_active=1"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by jctype_id "
					+ " order by jctype_name ";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOpenType.append("<tr align=center>");
				StrOpenType.append("<td align=left>").append(crs.getString("jctype_name")).append("</td>");
				StrOpenType.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopentype=yes&type_id=").append(crs.getString("jctype_id"))
						.append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOpenType.append("</tr>");

			}
			crs.close();
			StrOpenType.append("<tr align=center>");
			StrOpenType.append("<td align=right><b>Total: </b></td>");
			StrOpenType.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopentype=yes").append("','jobcardlist','');remote.focus();\">").append(count)
					.append("</a></b></td>");
			StrOpenType.append("</tr>");
			OpenType = StrOpenType.toString();

			// StrSql = "Select jccat_id, jccat_name, count(jc_id)  as jccount "
			// + " from " + compdb(comp_id) + "axela_service_jc_cat "
			// + " left join " + compdb(comp_id) +
			// "axela_service_jc on jc_jccat_id = jccat_id "
			// + " AND jc_time_out='' AND jc_active=1"
			// + BranchAccess.replace("branch_id", "jc_branch_id")
			// + ExeAccess.replace("emp_id", "jc_emp_id") + StrSeach
			// + " where 1=1 "
			// + " group by jccat_id "
			// + " order by jccat_name ";
			// // SOP("StrSql==="+StrSql);
			// count = 0;
			// rs = processQuery(StrSql, 0);
			// while (crs.next()) {
			// count = count + crs.getInt("jccount");
			// Str7.append("<tr align=center>");
			// Str7.append("<td align=left>").append(crs.getString("cat_name")).append("</td>");
			// Str7.append("<td align=right><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=").append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopencat=yes&cat_id=").append(crs.getString("cat_id")).append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
			// Str7.append("</tr>");
			//
			// }
			// crs.close();
			// Str7.append("<tr align=center>");
			// Str7.append("<td align=right><b>Total: </b></td>");
			// Str7.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=").append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopencat=yes").append("','jobcardlist','');remote.focus();\">").append(count).append("</a></b></td>");
			// Str7.append("</tr>");
			// StrOpenCat = Str7.toString();

			StrSql = "Select jcstage_id, jcstage_name, count(jc_id) as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " AND jc_time_out='' AND jc_active=1 AND jc_jctype_id=3"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by jcstage_id "
					+ " order by jcstage_rank ";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				StrOpenBodyShop.append("<tr align=center>");
				StrOpenBodyShop.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				StrOpenBodyShop.append("<td align=right width=20%><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
						.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopenbodyshop=yes&stage_id=").append(crs.getString("jcstage_id"))
						.append("','jobcardlist','');remote.focus();\">").append(crs.getString("jccount")).append("</a></td>");
				StrOpenBodyShop.append("</tr>");

			}
			crs.close();
			StrOpenBodyShop.append("<tr align=center>");
			StrOpenBodyShop.append("<td align=right><b>Total: </b></td>");
			StrOpenBodyShop.append("<td align=right><b><a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=").append(dr_branch_id).append("&technician=")
					.append(technicianexe_id).append("&advisor=").append(advisorexe_id).append("&jcopenbodyshop=yes").append("','jobcardlist','');remote.focus();\">").append(count)
					.append("</a></b></td>");
			StrOpenBodyShop.append("</tr>");
			OpenBodyShop = StrOpenBodyShop.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		// -----------------------------Logged
		// Days-----------------------------------------------------------------------------

		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);

		day1name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day1 = ExecuteQuery(StrSql);
		day1 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day1 + "</a>";
		// SOP("===="+ currYear + doublenum(currMonth + 1) +
		// doublenum(currDate));
		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day2name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day2 = ExecuteQuery(StrSql);
		day2 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day2 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day3name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day3 = ExecuteQuery(StrSql);
		day3 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day3 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day4name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day4 = ExecuteQuery(StrSql);
		day4 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day4 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day5name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day5 = ExecuteQuery(StrSql);
		day5 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day5 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day6name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day6 = ExecuteQuery(StrSql);
		day6 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day6 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day7name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day7 = ExecuteQuery(StrSql);
		day7 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day7 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day8name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND substring(jc_time_in,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		day8 = ExecuteQuery(StrSql);
		day8 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&day=day&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day8 + "</a>";

		// -----------------------------Logged
		// Weeks-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);

		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate + 7 - cal.get(cal.DAY_OF_WEEK));
		week1 = cal.get(cal.WEEK_OF_YEAR);

		cal.add(cal.DATE, -cal.get(cal.DAY_OF_WEEK) + 2);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		cal.set(currYear, currMonth, currDate);

		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endWeek + "' AND substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logWeek1 = ExecuteQuery(StrSql);
		logWeek1 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&week=week&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek1 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

		cal.add(cal.DATE, -7);
		week2 = cal.get(cal.WEEK_OF_YEAR);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		cal.set(currYear, currMonth, currDate);

		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endWeek + "' AND substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logWeek2 = ExecuteQuery(StrSql);
		logWeek2 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&week=week&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek2 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

		cal.add(cal.DATE, -7);
		week3 = cal.get(cal.WEEK_OF_YEAR);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		cal.set(currYear, currMonth, currDate);

		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND(substring(jc_time_in,1,8)>='" + endWeek + "' AND substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logWeek3 = ExecuteQuery(StrSql);
		logWeek3 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&week=week&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek3 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

		cal.add(cal.DATE, -7);
		week4 = cal.get(cal.WEEK_OF_YEAR);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		cal.set(currYear, currMonth, currDate);

		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endWeek + "' AND substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logWeek4 = ExecuteQuery(StrSql);
		logWeek4 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&week=week&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek4 + "</a>";

		// -----------------------------Logged
		// Months-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month1 = TextMonth(currMonth) + " " + currYear;
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endMonth + "' AND substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logMonth1 = ExecuteQuery(StrSql);
		logMonth1 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&month=month&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth1 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month2 = TextMonth(currMonth) + " " + currYear;
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endMonth + "' AND substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logMonth2 = ExecuteQuery(StrSql);
		logMonth2 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&month=month&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth2 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month3 = TextMonth(currMonth) + " " + currYear;
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endMonth + "' AND substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logMonth3 = ExecuteQuery(StrSql);
		logMonth3 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&month=month&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth3 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month4 = TextMonth(currMonth) + " " + currYear;
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 1) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,8)>='" + endMonth + "' AND substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logMonth4 = ExecuteQuery(StrSql);
		logMonth4 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&month=month&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth4 + "</a>";

		// -----------------------------Logged
		// Quarter-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		cal.set(currYear, currMonth, 1);
		if (currMonth >= 0 && currMonth <= 2) {
			qur1 = currYear + " Qtr 1 ";
			quarter = "between 1 AND 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur1 = currYear + " Qtr 2 ";
			quarter = "between 4 AND 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur1 = currYear + " Qtr 3 ";
			quarter = "between 7 AND 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur1 = currYear + " Qtr 4 ";
			quarter = "between 10 AND 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,4)='" + currYear + "' AND substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logQur1 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur1 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&qtr=qtr&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur1 + "</a>";

		cal.add(cal.MONTH, -3);
		currMonth = cal.get(cal.MONTH);
		if (currMonth == -1) {
			currMonth = 11;
			currYear = currYear - 1;
		} else {
			currYear = cal.get(cal.YEAR);
		}
		cal.set(currYear, currMonth, 1);

		if (currMonth >= 0 && currMonth <= 2) {
			qur2 = currYear + " Qtr 1 ";
			quarter = "between 1 AND 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur2 = currYear + " Qtr 2 ";
			quarter = "between 4 AND 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur2 = currYear + " Qtr 3 ";
			quarter = "between 7 AND 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur2 = currYear + " Qtr 4 ";
			quarter = "between 10 AND 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,4)='" + currYear + "' AND substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logQur2 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur2 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&qtr=qtr&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur2 + "</a>";
		cal.add(cal.MONTH, -3);
		currMonth = cal.get(cal.MONTH);
		if (currMonth == -1) {
			currMonth = 11;
			currYear = currYear - 1;
		} else {
			currYear = cal.get(cal.YEAR);
		}
		cal.set(currYear, currMonth, 1);

		if (currMonth >= 0 && currMonth <= 2) {
			qur3 = currYear + " Qtr 1 ";
			quarter = "between 1 AND 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur3 = currYear + " Qtr 2 ";
			quarter = "between 4 AND 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur3 = currYear + " Qtr 3 ";
			quarter = "between 7 AND 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur3 = currYear + " Qtr 4 ";
			quarter = "between 10 AND 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,4)='" + currYear + "' AND substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logQur3 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur3 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&qtr=qtr&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur3 + "</a>";

		cal.add(cal.MONTH, -3);
		currMonth = cal.get(cal.MONTH);
		if (currMonth == -1) {
			currMonth = 11;
			currYear = currYear - 1;
		} else {
			currYear = cal.get(cal.YEAR);
		}
		cal.set(currYear, currMonth, 1);

		if (currMonth >= 0 && currMonth <= 2) {
			qur4 = currYear + " Qtr 1 ";
			quarter = "between 1 AND 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur4 = currYear + " Qtr 2 ";
			quarter = "between 4 AND 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur4 = currYear + " Qtr 3 ";
			quarter = "between 7 AND 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur4 = currYear + " Qtr 4 ";
			quarter = "between 10 AND 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND (substring(jc_time_in,1,4)='" + currYear + "' AND substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + BranchAccess.replace("branch_id", "jc_branch_id")
				+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch;
		logQur4 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur4 = "<a href=\"javascript:remote=window.open('jobcarddash-check-search.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&qtr=qtr&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur4 + "</a>";

	}

	public String AgeingStatus() {
		try {
			int totalAgeingcount = 0;
			StrSql = "SELECT (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_jctype_id != 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -3, 0, 0, 0)) + "') as 'WS-0-3',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id != 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -8, 0, 0, 0)) + "' AND jc_time_in < '"
					+ ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -3, 0, 0, 0)) + "') as 'WS-4-8',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id != 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -15, 0, 0, 0)) + "' AND jc_time_in < '"
					+ ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -8, 0, 0, 0)) + "') as 'WS-9-15',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id != 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in < '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -15, 0, 0, 0)) + "') as 'WS->=16',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_jctype_id = 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -6, 0, 0, 0)) + "') as 'BS-0-6',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id = 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -12, 0, 0, 0)) + "' AND jc_time_in < '"
					+ ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -6, 0, 0, 0)) + "') as 'BS-7-12',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id = 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in > '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -17, 0, 0, 0)) + "' AND jc_time_in < '"
					+ ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -12, 0, 0, 0)) + "') as 'BS-13-17',"
					+ " (SELECT count(jc_id)"
					+ " FROM " + compdb(comp_id) + " axela_service_jc"
					+ " WHERE jc_jctype_id = 3"
					+ " AND jc_time_out = ''"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + StrSearch
					+ " AND jc_time_in < '" + ConvertShortDateToStr(AddDayMonthYear(DateToShortDate(kknow()), -17, 0, 0, 0)) + "') as 'BS->=18'";
			// SOP("StrSql====55555===chart" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			ageingchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ageingchart_data = ageingchart_data + "{'level': 'WS 1-3 days', 'value':" + crs.getString("WS-0-3") + ", 'color':'#04D215'},"
							+ "{'level': 'WS 4-8 days', 'value':" + crs.getString("WS-4-8") + ", 'color':'#c3a7e2'},"
							+ "{'level': 'WS 9-15 days', 'value':" + crs.getString("WS-9-15") + ", 'color':'#FFC200'},"
							+ "{'level': 'WS 16 days', 'value':" + crs.getString("WS->=16") + ", 'color':'#FF0033'},"
							// /BS
							+ "{'level': 'BS 1-6 days', 'value':" + crs.getString("BS-0-6") + ", 'color':'#04D215'},"
							+ "{'level': 'BS 7-12 days', 'value':" + crs.getString("BS-7-12") + ", 'color':'#c3a7e2'},"
							+ "{'level': 'BS 13-17 days', 'value':" + crs.getString("BS-13-17") + ", 'color':'#FFC200'},"
							+ "{'level': 'BS 18 days', 'value':" + crs.getString("BS->=18") + ", 'color':'#FF0033'}";
				}
				ageingchart_data = ageingchart_data + "]";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return "";
	}

	public String PopulateTechnician(String dr_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = '1' AND emp_active='1'"
					+ " AND (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + "))"
					// + " " + ExeAccess + ""
					+ " group by emp_id order by emp_name";
			// SOP("StrSql=tt=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_technician id=dr_technician class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), technicianexe_ids));
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

	public String PopulateAdvisor(String dr_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = '1' AND emp_active='1'"
					+ " AND (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + "))"
					// + " " + ExeAccess + ""
					+ " group by emp_id order by emp_name";
			// SOP("StrSql=aaa=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_advisor id=dr_advisor class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), advisorexe_ids));
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
}
