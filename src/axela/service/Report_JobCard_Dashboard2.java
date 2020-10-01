package axela.service;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_Dashboard2 extends Connect {

	public String jc_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrJCsToday = "";
	public String StrClosedToday = "";
	public String StrOpenJCs = "";
	public String StrDepartments = "";
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

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				Stats();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void Stats() {

		String startWeek, endWeek, startMonth, endMonth;
		StringBuilder Str = new StringBuilder();
		StringBuilder Str1 = new StringBuilder();
		StringBuilder Str2 = new StringBuilder();
		StringBuilder Str3 = new StringBuilder();

		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(cal.MONTH);
		int currYear = cal.get(cal.YEAR);
		int currDate = cal.get(cal.DATE);

		// -----------------------------Logged
		// Today-----------------------------------------------------------------------------
		try {
			StrSql = "Select jcstage_name, count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " and substring(jc_time_in,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
					+ " where 1=1 " + ExeAccess + BranchAccess.replace("branch_id", "jc_branch_id")
					+ " group by jcstage_id "
					+ " order by jccount desc, jcstage_name";
			// SOP("StrSql=="+StrSql);
			int count = 0;
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				Str.append("<tr align=center>");
				Str.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				Str.append("<td align=right>").append(crs.getString("jccount")).append("</td>");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr align=center>");
			Str.append("<td align=right><b>Total: </b></td>");
			Str.append("<td align=right><b>").append(count).append("</b></td>");
			Str.append("</tr>");
			StrJCsToday = Str.toString();

			StrSql = "Select jcstage_name, count(jc_id)  as jccount"
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " and substring(jc_time_ready,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
					+ " where 1=1 " + ExeAccess + BranchAccess.replace("branch_id", "jc_branch_id")
					+ " group by jcstage_id "
					+ " order by jccount desc, jcstage_name";
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				Str1.append("<tr align=center>");
				Str1.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				Str1.append("<td align=right>").append(crs.getString("jccount")).append("</td>");
				Str1.append("</tr>");
			}
			crs.close();
			Str1.append("<tr align=center>");
			Str1.append("<td align=right><b>Total: </b></td>");
			Str1.append("<td align=right><b>").append(count).append("</b></td>");
			Str1.append("</tr>");
			StrClosedToday = Str1.toString();

			StrSql = "Select jcstage_name, count(jc_id)  as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_stage "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_jcstage_id = jcstage_id "
					+ " and jc_time_ready='' "
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
					+ " where 1=1 "
					+ ExeAccess + BranchAccess.replace("branch_id", "jc_branch_id")
					+ " group by jcstage_id "
					+ " order by jccount desc, jcstage_name ";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				Str2.append("<tr align=center>");
				Str2.append("<td align=left>").append(crs.getString("jcstage_name")).append("</td>");
				Str2.append("<td align=right>").append(crs.getString("jccount")).append("</td>");
				Str2.append("</tr>");

			}
			crs.close();
			Str2.append("<tr align=center>");
			Str2.append("<td align=right><b>Total: </b></td>");
			Str2.append("<td align=right><b>").append(count).append("</b></td>");
			Str2.append("</tr>");
			StrOpenJCs = Str2.toString();

			StrSql = "Select priorityjc_name, count(jc_id)  as jccount "
					+ " from " + compdb(comp_id) + "axela_service_jc_priority  "
					+ " left join " + compdb(comp_id) + "axela_service_jc on jc_priorityjc_id = priorityjc_id "
					+ " and jc_time_ready='' and jc_time_promised < " + ToLongDate(kknow())
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
					+ " where 1=1 "
					+ ExeAccess + BranchAccess.replace("branch_id", "jc_branch_id")
					+ " group by priorityjc_id"
					+ " order by jccount desc, priorityjc_name";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("jccount");
				Str3.append("<tr align=center>");
				Str3.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>");
				Str3.append("<td align=right>").append(crs.getString("jccount")).append("</td>");
				Str3.append("</tr>");
			}
			crs.close();
			Str3.append("<tr align=center>");
			Str3.append("<td align=right><b>Total: </b></td>");
			Str3.append("<td align=right><b>").append(count).append("</b></td>");
			Str3.append("</tr>");
			StrDepartments = Str3.toString();

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
				+ " where substring(jc_time_in,1,8)=" + (ToLongDate(kknow()).substring(0, 8)) + ""
				+ "" + ExeAccess;
		day1 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day2name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -1, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day2 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day3name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -2, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day3 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day4name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -3, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day4 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day5name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -4, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day5 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day6name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -5, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day6 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day7name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -6, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day7 = ExecuteQuery(StrSql);

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day8name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and substring(jc_time_in,1,8)=" + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -7, 0, 0)).substring(0, 8) + ""
				+ "" + ExeAccess;
		day8 = ExecuteQuery(StrSql);

		// -----------------------------Logged
		// Weeks-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = Integer.parseInt(ToLongDate(kknow()).substring(4, 6));
		// SOP("currMonth=="+currMonth);
		currYear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
		// SOP("currYear=="+currYear);
		currDate = Integer.parseInt(ToLongDate(kknow()).substring(6, 8));
		// SOP("cal.get(cal.DAY_OF_WEEK)=="+ReturnDayOfWeek(ToLongDate(kknow())));

		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate + 7 - ReturnDayOfWeek(ToLongDate(kknow())));
		// SOP("startWeek-aaaaa-"+AddDayMonthYear(strToShortDate(ToShortDate(kknow())),
		// 0, -1, 0, 0));
		// SOP("startWeek--"+startWeek);
		// week1 = cal.get(cal.WEEK_OF_YEAR);
		week1 = ReturnWeekOfYear(ToLongDate(kknow()));
		// SOP("week1=="+week1);
		SOP("-cal.get(cal.DAY_OF_WEEK)==" + cal.get(cal.DAY_OF_WEEK));

		// cal.add(cal.DATE, -cal.get(cal.DAY_OF_WEEK) + 2);
		SOP("-cal.get(cal.DAY_OF_WEEK)==" + cal.DATE);
		// AddDayMonthYear(ToLongDate(kknow()), week1, week1, empcount, week1)
		// AddHoursDate(null, week1, week1, ToLongDate(null))
		// currMonth = cal.get(cal.MONTH);
		// currYear = cal.get(cal.YEAR);
		// currDate = cal.get(cal.DATE);
		currMonth = Integer.parseInt(ToLongDate(kknow()).substring(4, 6)) - 1;
		// SOP("currMonth=="+currMonth);
		currYear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
		// SOP("currYear=="+currYear);
		currDate = 1;

		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		SOP("endWeek--" + endWeek);
		cal.set(currYear, currMonth, currDate);

		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endWeek + "' and substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + ExeAccess;
		// SOP("StrSqld5=="+StrSql);
		logWeek1 = ExecuteQuery(StrSql);

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		SOP("currDate==" + currDate + " currYear==" + currYear + " currMonth==" + currMonth);
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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endWeek + "' and substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + ExeAccess;
		logWeek2 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and(substring(jc_time_in,1,8)>='" + endWeek + "' and substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + ExeAccess;
		logWeek3 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endWeek + "' and substring(jc_time_in,1,8)<'" + startWeek + "')"
				+ "" + ExeAccess;
		logWeek4 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endMonth + "' and substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + ExeAccess;
		logMonth1 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endMonth + "' and substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + ExeAccess;
		logMonth2 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endMonth + "' and substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + ExeAccess;
		logMonth3 = ExecuteQuery(StrSql);

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
				+ " where 1=1 and (substring(jc_time_in,1,8)>='" + endMonth + "' and substring(jc_time_in,1,8)<'" + startMonth + "')"
				+ "" + ExeAccess;
		logMonth4 = ExecuteQuery(StrSql);

		// -----------------------------Logged
		// Quarter-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		cal.set(currYear, currMonth, 1);
		if (currMonth >= 0 && currMonth <= 2) {
			qur1 = currYear + " Qtr 1 ";
			quarter = "between 1 and 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur1 = currYear + " Qtr 2 ";
			quarter = "between 4 and 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur1 = currYear + " Qtr 3 ";
			quarter = "between 7 and 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur1 = currYear + " Qtr 4 ";
			quarter = "between 10 and 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and (substring(jc_time_in,1,4)='" + currYear + "' and substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + ExeAccess;
		logQur1 = ExecuteQuery(StrSql);

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
			quarter = "between 1 and 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur2 = currYear + " Qtr 2 ";
			quarter = "between 4 and 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur2 = currYear + " Qtr 3 ";
			quarter = "between 7 and 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur2 = currYear + " Qtr 4 ";
			quarter = "between 10 and 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and (substring(jc_time_in,1,4)='" + currYear + "' and substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + ExeAccess;
		logQur2 = ExecuteQuery(StrSql);

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
			quarter = "between 1 and 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur3 = currYear + " Qtr 2 ";
			quarter = "between 4 and 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur3 = currYear + " Qtr 3 ";
			quarter = "between 7 and 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur3 = currYear + " Qtr 4 ";
			quarter = "between 10 and 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and (substring(jc_time_in,1,4)='" + currYear + "' and substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + ExeAccess;
		logQur3 = ExecuteQuery(StrSql);

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
			quarter = "between 1 and 3";
		}
		if (currMonth >= 3 && currMonth <= 5) {
			qur4 = currYear + " Qtr 2 ";
			quarter = "between 4 and 6";
		}
		if (currMonth >= 6 && currMonth <= 8) {
			qur4 = currYear + " Qtr 3 ";
			quarter = "between 7 and 9";
		}
		if (currMonth >= 9 && currMonth <= 11) {
			qur4 = currYear + " Qtr 4 ";
			quarter = "between 10 and 12";
		}
		StrSql = "select count(jc_id) "
				+ " from " + compdb(comp_id) + "axela_service_jc "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 and (substring(jc_time_in,1,4)='" + currYear + "' and substring(jc_time_in,5,2) " + quarter + ")"
				+ "" + ExeAccess;
		logQur4 = ExecuteQuery(StrSql);

	}
}
