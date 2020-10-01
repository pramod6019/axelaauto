package axela.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Dashboard1 extends Connect {

	public String ticket_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String TicketsToday = "";
	public String ClosedTicketsToday = "";
	public String OpenStatus = "";
	public String OpenDepartments = "";
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
	public String go = "", StrSearch = "";
	public String[] ticketexe_ids;
	public String ticketexe_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_ticket_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));

				GetValues(request, response);
				// SOP("ticketexe_id = " + ticketexe_id);
				if (go.equals("Go")) {
					// StrSeach = ExeAccess + BranchAccess.replace("branch_id",
					// "jc_branch_id") + "";
					if (!ticketexe_id.equals("")) {
						StrSearch = StrSearch + " and ticket_emp_id in (" + ticketexe_id + ")";
					}
				}
				Stats();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ticketexe_id = RetrunSelectArrVal(request, "dr_emp");
		ticketexe_ids = request.getParameterValues("dr_emp");
	}

	public void Stats() {

		String startWeek, endWeek, startMonth, endMonth;
		StringBuilder StrTicketsToday = new StringBuilder();
		StringBuilder StrClosedTicketsToday = new StringBuilder();
		StringBuilder StrOpenStatus = new StringBuilder();
		StringBuilder StrOpenDepartments = new StringBuilder();

		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(cal.MONTH);
		int currYear = cal.get(cal.YEAR);
		int currDate = cal.get(cal.DATE);

		// -----------------------------Logged
		// Today-----------------------------------------------------------------------------
		try {
			StrSql = "Select ticketstatus_id, ticketstatus_name, count(ticket_id)  as ticketcount"
					+ " from " + compdb(comp_id) + "axela_service_ticket_status "
					+ " left join " + compdb(comp_id) + "axela_service_ticket on ticket_ticketstatus_id = ticketstatus_id "
					+ " and substring(ticket_report_time,1,8)= '" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "'"
					// + " left join " + compdb(comp_id) +
					// "axela_emp on emp_id = ticket_emp_id "
					+ ExeAccess.replace("emp_id", "ticket_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by ticketstatus_id "
					+ " order by ticketcount desc, ticketstatus_name";
			int count = 0;
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("ticketcount");
				StrTicketsToday.append("<tr align=center>");
				StrTicketsToday.append("<td align=left>").append(crs.getString("ticketstatus_name")).append("</td>");
				StrTicketsToday.append("<td align=right><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?tickettoday=yes&emp_id=").append(ticketexe_id).append("&ticketstatus_id=")
						.append(crs.getString("ticketstatus_id")).append("&starttime=").append(currYear + doublenum(currMonth + 1) + doublenum(currDate))
						.append("','ticketlist','');remote.focus();\">").append(crs.getString("ticketcount")).append("</a></td>");
				StrTicketsToday.append("</tr>");
			}
			crs.close();
			StrTicketsToday.append("<tr align=center>");
			StrTicketsToday.append("<td align=right><b>Total: </b></td>");
			StrTicketsToday.append("<td align=right><b><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?tickettoday=yes").append("&emp_id=").append(ticketexe_id)
					.append("&starttime=").append(currYear + doublenum(currMonth + 1) + doublenum(currDate)).append("','ticketlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrTicketsToday.append("</tr>");
			TicketsToday = StrTicketsToday.toString();

			StrSql = "Select ticketstatus_id, ticketstatus_name, count(ticket_id)  as ticketcount"
					+ " from " + compdb(comp_id) + "axela_service_ticket_status "
					+ " left join " + compdb(comp_id) + "axela_service_ticket on ticket_ticketstatus_id = ticketstatus_id "
					+ " and substring(ticket_closed_time,1,8)= '" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "'"
					// + " left join " + compdb(comp_id) +
					// "axela_emp on emp_id = ticket_emp_id "
					+ ExeAccess.replace("emp_id", "ticket_emp_id") + StrSearch
					+ " where 1=1 "
					+ " group by ticketstatus_id "
					+ " order by ticketcount desc, ticketstatus_name";
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("ticketcount");
				StrClosedTicketsToday.append("<tr align=center>");
				StrClosedTicketsToday.append("<td align=left>").append(crs.getString("ticketstatus_name")).append("</td>");
				StrClosedTicketsToday.append("<td align=right><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?closedtickettoday=yes&emp_id=").append(ticketexe_id)
						.append("&ticketstatus_id=").append(crs.getString("ticketstatus_id")).append("&starttime=").append(currYear + doublenum(currMonth + 1) + doublenum(currDate))
						.append("','ticketlist','');remote.focus();\">").append(crs.getString("ticketcount")).append("</a></td>");
				StrClosedTicketsToday.append("</tr>");
			}
			crs.close();
			StrClosedTicketsToday.append("<tr align=center>");
			StrClosedTicketsToday.append("<td align=right><b>Total: </b></td>");
			StrClosedTicketsToday.append("<td align=right><b><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?closedtickettoday=yes").append("&emp_id=").append(ticketexe_id)
					.append("&starttime=").append(currYear + doublenum(currMonth + 1) + doublenum(currDate)).append("','ticketlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrClosedTicketsToday.append("</tr>");
			ClosedTicketsToday = StrClosedTicketsToday.toString();

			StrSql = "Select ticketstatus_id, ticketstatus_name, count(ticket_id)  as ticketcount "
					+ " from " + compdb(comp_id) + "axela_service_ticket_status "
					+ " left join " + compdb(comp_id) + "axela_service_ticket on ticket_ticketstatus_id = ticketstatus_id"
					+ " and ticketstatus_id !=3 "
					// + " left join " + compdb(comp_id) +
					// "axela_emp on emp_id = ticket_emp_id "
					+ ExeAccess.replace("emp_id", "ticket_emp_id") + StrSearch
					+ " where 1=1"
					+ " group by ticketstatus_id "
					+ " order by ticketcount desc, ticketstatus_name ";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("ticketcount");
				StrOpenStatus.append("<tr align=center>");
				StrOpenStatus.append("<td align=left>").append(crs.getString("ticketstatus_name")).append("</td>");
				StrOpenStatus.append("<td align=right><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?openticket=yes&emp_id=").append(ticketexe_id).append("&ticketstatus_id=")
						.append(crs.getString("ticketstatus_id")).append("','ticketlist','');remote.focus();\">").append(crs.getString("ticketcount")).append("</td>");
				StrOpenStatus.append("</tr>");

			}
			crs.close();
			StrOpenStatus.append("<tr align=center>");
			StrOpenStatus.append("<td align=right><b>Total: </b></td>");
			StrOpenStatus.append("<td align=right><b><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?openticket=yes").append("&emp_id=").append(ticketexe_id)
					.append("','ticketlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOpenStatus.append("</tr>");
			OpenStatus = StrOpenStatus.toString();

			StrSql = "Select ticket_dept_id, ticket_dept_name, count(ticket_id)  as ticketcount "
					+ " from " + compdb(comp_id) + "axela_service_ticket_dept  "
					+ " left join " + compdb(comp_id) + "axela_service_ticket on ticket_ticket_dept_id = ticket_dept_id"
					+ "  and ticket_ticketstatus_id !=3"
					// + " left join " + compdb(comp_id) +
					// "axela_emp on emp_id = ticket_emp_id "
					+ ExeAccess.replace("emp_id", "ticket_emp_id") + StrSearch
					+ " where 1=1"
					+ " group by ticket_dept_id"
					+ " order by ticketcount desc, ticket_dept_name";
			// SOP("StrSql==="+StrSql);
			count = 0;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + crs.getInt("ticketcount");
				StrOpenDepartments.append("<tr align=center>");
				StrOpenDepartments.append("<td align=left>").append(crs.getString("ticket_dept_name")).append("</td>");
				StrOpenDepartments.append("<td align=right><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?ticket_dept=yes&emp_id=").append(ticketexe_id).append("&ticket_dept_id=")
						.append(crs.getString("ticket_dept_id")).append("','ticketlist','');remote.focus();\">").append(crs.getString("ticketcount")).append("</td>");
				StrOpenDepartments.append("</tr>");
			}
			crs.close();
			StrOpenDepartments.append("<tr align=center>");
			StrOpenDepartments.append("<td align=right><b>Total: </b></td>");
			StrOpenDepartments.append("<td align=right><b><a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?ticket_dept=yes").append("&emp_id=").append(ticketexe_id)
					.append("','ticketlist','');remote.focus();\">").append(count).append("</a></b></td>");
			StrOpenDepartments.append("</tr>");
			OpenDepartments = StrOpenDepartments.toString();

			// } catch (Exception ex) {
			// SOPError("Axelaauto== " + this.getClass().getName());
			// SOPError("Error in " + new
			// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// }

			// -----------------------------Logged
			// Days-----------------------------------------------------------------------------

			cal = Calendar.getInstance();
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);

			day1name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ ExeAccess + StrSearch
					+ " where substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "";
			day1 = ExecuteQuery(StrSql);
			day1 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day1 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day2name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day2 = ExecuteQuery(StrSql);
			day2 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day2 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day3name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day3 = ExecuteQuery(StrSql);
			day3 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day3 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day4name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day4 = ExecuteQuery(StrSql);
			day4 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day4 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day5name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day5 = ExecuteQuery(StrSql);
			day5 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day5 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day6name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day6 = ExecuteQuery(StrSql);
			day6 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day6 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day7name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day7 = ExecuteQuery(StrSql);
			day7 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day7 + "</a>";

			cal.add(cal.DATE, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			currDate = cal.get(cal.DATE);
			cal.set(currYear, currMonth, currDate);
			day8name = "" + currDate + " " + TextMonth(currMonth);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and substring(ticket_report_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
					+ "" + ExeAccess + StrSearch;
			day8 = ExecuteQuery(StrSql);
			day8 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?day=day&emp_id=" + ticketexe_id + "&starttime=" + currYear + doublenum(currMonth + 1) + doublenum(currDate)
					+ "','ticketlist','');remote.focus();\">" + day8 + "</a>";

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

			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endWeek + "' and substring(ticket_report_time,1,8)<'" + startWeek + "')"
					+ "" + ExeAccess + StrSearch;
			logWeek1 = ExecuteQuery(StrSql);
			logWeek1 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?week=week&emp_id=" + ticketexe_id + "&starttime=" + startWeek + "&endtime=" + endWeek
					+ "','ticketlist','');remote.focus();\">" + logWeek1 + "</a>";

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

			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endWeek + "' and substring(ticket_report_time,1,8)<'" + startWeek + "')"
					+ "" + ExeAccess + StrSearch;
			logWeek2 = ExecuteQuery(StrSql);
			logWeek2 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?week=week&emp_id=" + ticketexe_id + "&starttime=" + startWeek + "&endtime=" + endWeek
					+ "','ticketlist','');remote.focus();\">" + logWeek2 + "</a>";

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

			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and(substring(ticket_report_time,1,8)>='" + endWeek + "' and substring(ticket_report_time,1,8)<'" + startWeek + "')"
					+ "" + ExeAccess + StrSearch;
			logWeek3 = ExecuteQuery(StrSql);
			logWeek3 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?week=week&emp_id=" + ticketexe_id + "&starttime=" + startWeek + "&endtime=" + endWeek
					+ "','ticketlist','');remote.focus();\">" + logWeek3 + "</a>";

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

			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endWeek + "' and substring(ticket_report_time,1,8)<'" + startWeek + "')"
					+ "" + ExeAccess + StrSearch;
			logWeek4 = ExecuteQuery(StrSql);
			logWeek4 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?week=week&emp_id=" + ticketexe_id + "&starttime=" + startWeek + "&endtime=" + endWeek
					+ "','ticketlist','');remote.focus();\">" + logWeek4 + "</a>";

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
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endMonth + "' and substring(ticket_report_time,1,8)<'" + startMonth + "')"
					+ "" + ExeAccess + StrSearch;
			logMonth1 = ExecuteQuery(StrSql);
			logMonth1 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?month=month&emp_id=" + ticketexe_id + "&starttime=" + startMonth + "&endtime=" + endMonth
					+ "','ticketlist','');remote.focus();\">" + logMonth1 + "</a>";

			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
			month2 = TextMonth(currMonth) + " " + currYear;
			cal.add(cal.MONTH, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
			cal.set(currYear, currMonth, 1);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endMonth + "' and substring(ticket_report_time,1,8)<'" + startMonth + "')"
					+ "" + ExeAccess + StrSearch;
			logMonth2 = ExecuteQuery(StrSql);
			logMonth2 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?month=month&emp_id=" + ticketexe_id + "&starttime=" + startMonth + "&endtime=" + endMonth
					+ "','ticketlist','');remote.focus();\">" + logMonth2 + "</a>";

			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
			month3 = TextMonth(currMonth) + " " + currYear;
			cal.add(cal.MONTH, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
			cal.set(currYear, currMonth, 1);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endMonth + "' and substring(ticket_report_time,1,8)<'" + startMonth + "')"
					+ "" + ExeAccess + StrSearch;
			logMonth3 = ExecuteQuery(StrSql);
			logMonth3 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?month=month&emp_id=" + ticketexe_id + "&starttime=" + startMonth + "&endtime=" + endMonth
					+ "','ticketlist','');remote.focus();\">" + logMonth3 + "</a>";

			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
			month4 = TextMonth(currMonth) + " " + currYear;
			cal.add(cal.MONTH, -1);
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			endMonth = currYear + doublenum(currMonth + 1) + doublenum(1);
			cal.set(currYear, currMonth, 1);
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,8)>='" + endMonth + "' and substring(ticket_report_time,1,8)<'" + startMonth + "')"
					+ "" + ExeAccess + StrSearch;
			logMonth4 = ExecuteQuery(StrSql);
			logMonth4 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?month=month&emp_id=" + ticketexe_id + "&starttime=" + startMonth + "&endtime=" + endMonth
					+ "','ticketlist','');remote.focus();\">" + logMonth4 + "</a>";

			// -----------------------------Logged
			// Quarter-----------------------------------------------------------------------------
			cal = Calendar.getInstance();
			currMonth = cal.get(cal.MONTH);
			currYear = cal.get(cal.YEAR);
			cal.set(currYear, currMonth, 1);
			// SOP("cal = " + cal);
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
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,4)='" + currYear + "' and substring(ticket_report_time,5,2) " + quarter + ")"
					+ "" + ExeAccess + StrSearch;
			logQur1 = ExecuteQuery(StrSql);
			quarter = URLEncoder.encode(quarter, "UTF-8");
			logQur1 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?qtr=qtr&emp_id=" + ticketexe_id + "&starttime=" + currYear + "&endtime=" + quarter
					+ "','ticketlist','');remote.focus();\">" + logQur1 + "</a>";

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
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,4)='" + currYear + "' and substring(ticket_report_time,5,2) " + quarter + ")"
					+ "" + ExeAccess + StrSearch;
			logQur2 = ExecuteQuery(StrSql);
			quarter = URLEncoder.encode(quarter, "UTF-8");
			logQur2 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?qtr=qtr&emp_id=" + ticketexe_id + "&starttime=" + currYear + "&endtime=" + quarter
					+ "','ticketlist','');remote.focus();\">" + logQur2 + "</a>";

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
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,4)='" + currYear + "' and substring(ticket_report_time,5,2) " + quarter + ")"
					+ "" + ExeAccess + StrSearch;
			logQur3 = ExecuteQuery(StrSql);
			quarter = URLEncoder.encode(quarter, "UTF-8");
			logQur3 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?qtr=qtr&emp_id=" + ticketexe_id + "&starttime=" + currYear + "&endtime=" + quarter
					+ "','ticketlist','');remote.focus();\">" + logQur3 + "</a>";

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
			StrSql = "select count(ticket_id) "
					+ " from " + compdb(comp_id) + "axela_service_ticket "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
					+ " where 1=1 and (substring(ticket_report_time,1,4)='" + currYear + "' and substring(ticket_report_time,5,2) " + quarter + ")"
					+ "" + ExeAccess + StrSearch;
			logQur4 = ExecuteQuery(StrSql);
			quarter = URLEncoder.encode(quarter, "UTF-8");
			logQur4 = "<a href=\"javascript:remote=window.open('ticketdash-check-search.jsp?qtr=qtr&emp_id=" + ticketexe_id + "&starttime=" + currYear + "&endtime=" + quarter
					+ "','ticketlist','');remote.focus();\">" + logQur4 + "</a>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active='1' AND emp_ticket_owner='1'"
					// + " " + ExeAccess + ""
					+ " group by emp_id order by emp_name";
			// SOP("StrSql=aaa=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_emp id=dr_emp class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), ticketexe_ids));
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
