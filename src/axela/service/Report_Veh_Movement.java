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

public class Report_Veh_Movement extends Connect {

	public String jc_id = "0";
	public String emp_id = "0";
	public String comp_id = "0", jc_branch_id = "";
	public String msg = "";
	public String StrSql = "", StrSearch = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String JCsToday = "", JCsToday1 = "";
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
	public String brand_id = "", region_id = "", zone_id = "";
	public String[] brand_ids, region_ids, jc_branch_ids, zone_ids;
	public String dr_branch_id = "0", go = "";
	public String[] technicianexe_ids, advisorexe_ids;
	public String technicianexe_id = "", advisorexe_id = "";
	public String movementreport = "";
	public Report_Check reportexe = new Report_Check();
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
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
				if (!movementreport.equals("")) {
					VehicleFilter(request, response);
				}
				if (go.equals("Go")) {
					StrSearch += BranchAccess.replace("branch_id", "jc_branch_id");

					if (!brand_id.equals("") && jc_branch_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!jc_branch_id.equals("")) {
						StrSearch += " AND vehmove_branch_id IN (" + jc_branch_id + ")";
					}
					// /StrSeach = ExeAccess + BranchAccess.replace("branch_id",
					// "jc_branch_id") + "";

					// if (!technicianexe_id.equals("")) {
					// StrSearch = StrSearch + " AND jc_technician_emp_id in ("
					// + technicianexe_id + ")";
					// }
					// if (!advisorexe_id.equals("")) {
					// StrSearch = StrSearch + " AND jc_emp_id in (" +
					// advisorexe_id + ")";
					// }
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND vehmove_branch_id =" + dr_branch_id;
					}
					// if (!msg.equals("")) {
					// msg = "Error!" + msg;
					// }
				}
				Stats();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void VehicleFilter(HttpServletRequest request, HttpServletResponse response) {
		String Strfilter = "";
		String branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
		String vehmove_timein = PadQuotes(request.getParameter("vehmove_timein"));
		String starttime = PadQuotes(request.getParameter("starttime"));
		String endtime = PadQuotes(request.getParameter("endtime"));

		HttpSession session1 = request.getSession(true);
		try {
			if (movementreport.equals("IN")) {
				Strfilter = " AND SUBSTRING(vehmove_timein,1,8)= SUBSTRING('" + vehmove_timein + "',1,8)";
				// + " AND vehmove_timeout=''";
			}
			if (movementreport.equals("OUT")) {
				Strfilter += " AND SUBSTRING(vehmove_timeout,1,8)=SUBSTRING('" + vehmove_timein + "',1,8)";
			}
			if (movementreport.equals("INOUT")) {
				Strfilter += " AND SUBSTRING(vehmove_timein,1,8)=SUBSTRING('" + vehmove_timein + "',1,8)";
			}
			if (movementreport.equals("carryover")) {
				Strfilter += " AND vehmove_timeout=''";
			}
			if (movementreport.equals("daytraffic")) {
				Strfilter += " AND SUBSTRING(vehmove_timein,1,8)='" + vehmove_timein + "'";
			}
			if (movementreport.equals("weektraffic")) {
				Strfilter += " AND SUBSTRING(vehmove_timein,1,8)>='" + endtime + "'"
						+ " AND SUBSTRING(vehmove_timein,1,8)<'" + starttime + "'";
			}
			if (movementreport.equals("monthtraffic")) {
				Strfilter += " AND SUBSTRING(vehmove_timein,1,8)>='" + endtime + "'"
						+ " AND SUBSTRING(vehmove_timein,1,8)<'" + starttime + "'";
			}
			if (movementreport.equals("quartertraffic")) {
				Strfilter += " AND (SUBSTRING(vehmove_timein,1,4)='" + starttime + "' "
						+ " AND SUBSTRING(vehmove_timein,5,2) " + endtime + ")";
			}
			if (!branch_id.equals("0"))
			{
				Strfilter += " AND vehmove_branch_id=" + branch_id;
			}
			session1.setAttribute("vehmovestrsql", Strfilter);
			response.sendRedirect(response.encodeRedirectURL("movement-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		// SOP("region_id----" + region_id);
		// SOP("region_ids========" + region_ids);
		jc_branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		jc_branch_ids = request.getParameterValues("dr_branch_id");
		movementreport = PadQuotes(request.getParameter("movementreport"));
		// technicianexe_id = RetrunSelectArrVal(request, "dr_technician");
		// technicianexe_ids = request.getParameterValues("dr_technician");
		// advisorexe_id = RetrunSelectArrVal(request, "dr_advisor");
		// advisorexe_ids = request.getParameterValues("dr_advisor");

	}

	public void Stats() throws UnsupportedEncodingException {

		String startWeek, endWeek, startMonth, endMonth;
		StringBuilder StrJCsToday = new StringBuilder();
		StringBuilder StrJCsToday1 = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(cal.MONTH);
		int currYear = cal.get(cal.YEAR);
		int currDate = cal.get(cal.DATE);

		// -----------------------------Logged
		// Today-----------------------------------------------------------------------------
		try {
			StrSql = "SELECT COUNT(vehmove_id)  AS vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
					// + " left join " + compdb(comp_id) +
					// "axela_service_jc on jc_jcstage_id = jcstage_id "
					// + " AND jc_active=1"
					+ " WHERE 1=1"
					+ " AND SUBSTRING(vehmove_timein,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "vehmove_branch_id")
					+ ExeAccess
					+ StrSearch;
			// + " GROUP BY vehmove_id ";
			// SOP("in------" + StrSql);
			int count = 0;
			int totalcount = 0, carryover = 0;
			CachedRowSet crs = processQuery(StrSql, 0);
			StrJCsToday.append("<div class=\"table-responsive table-bordered\">\n");
			StrJCsToday.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
			StrJCsToday.append("<tbody>");
			StrJCsToday.append("<tr align=center>");
			StrJCsToday.append("<td align=left>").append("IN").append("</td>");
			while (crs.next()) {
				count = count + crs.getInt("vehcount");
				totalcount = totalcount + crs.getInt("vehcount");

			}
			StrJCsToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=").append(dr_branch_id)
					.append("&vehmove_timein=").append(ToLongDate(kknow()))
					.append("&movementreport=IN").append("','jobcardlist','');remote.focus();\">").append(totalcount).append("</a></td>");
			StrJCsToday.append("</tr>");
			crs.close();

			StrSql = "SELECT count(vehmove_id)  AS vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
					// + " left join " + compdb(comp_id) +
					// "axela_service_jc on jc_jcstage_id = jcstage_id "
					// + " AND jc_active=1"
					+ " WHERE 1=1"
					+ " AND SUBSTRING(vehmove_timeout,1,8)= '" + (ToLongDate(kknow()).substring(0, 8)) + "'"
					+ BranchAccess.replace("branch_id", "vehmove_branch_id")
					+ ExeAccess
					+ StrSearch;
			// + " GROUP BY vehmove_id ";
			// SOP("out-----" + StrSql);
			int count1 = 0;
			int totalcount1 = 0;
			CachedRowSet crs1 = processQuery(StrSql, 0);
			StrJCsToday.append("<tr align=center>");
			StrJCsToday.append("<td align=left>").append("OUT").append("</td>");
			while (crs1.next()) {
				count1 = count1 + crs1.getInt("vehcount");
				totalcount1 = totalcount1 + crs1.getInt("vehcount");
				// SOP("crs1======" + crs1.getInt("vehcount"));
			}
			// SOP("count--------" + count1);
			// SOP("totalcount---------out------" + totalcount1);
			StrJCsToday.append("<td align=right width=20%><a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=").append(dr_branch_id)
					.append("&vehmove_timein=").append(ToLongDate(kknow()))
					.append("&movementreport=OUT").append("','jobcardlist','');remote.focus();\">").append(totalcount1).append("</a></td>");
			StrJCsToday.append("</tr>");
			crs1.close();

			StrJCsToday.append("</tr>");
			StrJCsToday.append("</tbody>\n");
			StrJCsToday.append("</table>\n");
			StrJCsToday.append("</div>\n");
			JCsToday = StrJCsToday.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		try {
			int count = 0;
			int totalcount = 0, carryover = 0;

			StrSql = "SELECT count(vehmove_id)  AS vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
					+ " WHERE 1=1"
					+ " AND vehmove_timeout=''"
					+ BranchAccess.replace("branch_id", "vehmove_branch_id")
					+ ExeAccess
					+ StrSearch
					+ " GROUP BY vehmove_id ";
			// SOP("carry over-----" + StrSql);
			CachedRowSet crs2 = processQuery(StrSql, 0);
			StrJCsToday1.append("<div class=\"table-responsive table-bordered\">\n");
			StrJCsToday1.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
			StrJCsToday1.append("<tbody>");
			StrJCsToday1.append("<tr align=center>");
			StrJCsToday1.append("<td align=left>").append("Carry Over").append("</td>");
			while (crs2.next()) {
				count = count + crs2.getInt("vehcount");
				carryover = carryover + crs2.getInt("vehcount");
			}
			StrJCsToday1.append("<td align=right width=20%><a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=").append(dr_branch_id)
					.append("&vehmove_timein=").append(ToLongDate(kknow()))
					.append("&movementreport=carryover").append("','jobcardlist','');remote.focus();\">").append(carryover).append("</a></td>");
			StrJCsToday1.append("</tr>");
			crs2.close();
			StrJCsToday1.append("</tbody>\n");
			StrJCsToday1.append("</table>\n");
			StrJCsToday1.append("</div>\n");
			JCsToday1 = StrJCsToday1.toString();
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
		// SOP("currDate----1----" + currDate);
		day1name = "" + currDate + " " + TextMonth(currMonth);
		// SOP("currMonth---1-----" + currMonth);
		// SOP("day1name---------" + day1name);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("day1name==" + StrSql);
		day1 = ExecuteQuery(StrSql);
		day1 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day1 + "</a>";
		// SOP("===="+ currYear + doublenum(currMonth + 1) +
		// doublenum(currDate));
		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		// SOP("currMonth---2----" + currMonth);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		// SOP("currYear----" + currYear);
		// SOP("currDate--------" + currDate);
		day2name = "" + currDate + " " + TextMonth(currMonth);
		// SOP("day2name-------" + day2name);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " where 1=1 AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("day2name==" + StrSql);
		day2 = ExecuteQuery(StrSql);
		day2 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day2 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day3name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		day3 = ExecuteQuery(StrSql);
		day3 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day3 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day4name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		day4 = ExecuteQuery(StrSql);
		day4 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day4 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		// SOP("currDate-----" + currDate);
		// SOP("currMonth----------" + currMonth);
		day5name = "" + currDate + " " + TextMonth(currMonth);
		// SOP("day5name========" + day5name);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		day5 = ExecuteQuery(StrSql);
		day5 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day5 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day6name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("day6name-----" + StrSql);
		day6 = ExecuteQuery(StrSql);
		day6 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day6 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day7name = "" + currDate + " " + TextMonth(currMonth);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("day7name==" + StrSql);
		day7 = ExecuteQuery(StrSql);
		day7 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day7 + "</a>";

		cal.add(cal.DATE, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		cal.set(currYear, currMonth, currDate);
		day8name = "" + currDate + " " + TextMonth(currMonth);
		// SOP("day8name-------" + day8name);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND SUBSTRING(vehmove_timein,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + ""
				+ "" + BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("day8-----" + StrSql);
		day8 = ExecuteQuery(StrSql);
		day8 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=daytraffic&vehmove_timein=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "','jobcardlist','');remote.focus();\">" + day8 + "</a>";

		// -----------------------------Logged
		// Weeks-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		// SOP("currMonth-------" + currMonth);
		// SOP("currDate--------" + currDate);
		// SOP("cal.DAY_OF_WEEK==========" + cal.DAY_OF_WEEK);
		startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate + 7 - cal.get(cal.DAY_OF_WEEK));
		week1 = cal.get(cal.WEEK_OF_YEAR);
		// SOP("week1--------" + week1);
		cal.add(cal.DATE, -cal.get(cal.DAY_OF_WEEK) + 2);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		currDate = cal.get(cal.DATE);
		endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
		cal.set(currYear, currMonth, currDate);

		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endWeek + "' AND substring(vehmove_timein,1,8)<'" + startWeek + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logWeek1 = ExecuteQuery(StrSql);

		logWeek1 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=weektraffic&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek1 + "</a>";

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

		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endWeek + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startWeek + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logWeek2 = ExecuteQuery(StrSql);
		logWeek2 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=weektraffic&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek2 + "</a>";

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

		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND(SUBSTRING(vehmove_timein,1,8)>='" + endWeek + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startWeek + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("week3-------" + StrSql);
		logWeek3 = ExecuteQuery(StrSql);
		logWeek3 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=weektraffic&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek3 + "</a>";

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

		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endWeek + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startWeek + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("week4---" + StrSql);
		logWeek4 = ExecuteQuery(StrSql);
		logWeek4 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=weektraffic&starttime=" + startWeek + "&endtime=" + endWeek + "','jobcardlist','');remote.focus();\">" + logWeek4 + "</a>";

		// -----------------------------Logged
		// Months-----------------------------------------------------------------------------
		cal = Calendar.getInstance();
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month1 = TextMonth(currMonth) + " " + currYear;
		// SOP("month1--------" + month1);
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endMonth + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startMonth + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logMonth1 = ExecuteQuery(StrSql);
		logMonth1 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=monthtraffic&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth1 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month2 = TextMonth(currMonth) + " " + currYear;
		// SOP("month2---------" + month2);
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endMonth + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startMonth + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logMonth2 = ExecuteQuery(StrSql);
		logMonth2 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=monthtraffic&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth2 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month3 = TextMonth(currMonth) + " " + currYear;
		// SOP("month3----------" + month3);
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endMonth + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startMonth + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logMonth3 = ExecuteQuery(StrSql);
		logMonth3 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=monthtraffic&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth3 + "</a>";

		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
		month4 = TextMonth(currMonth) + " " + currYear;
		cal.add(cal.MONTH, -1);
		currMonth = cal.get(cal.MONTH);
		currYear = cal.get(cal.YEAR);
		endMonth = currYear + doublenum(currMonth + 1) + doublenum(1);
		cal.set(currYear, currMonth, 1);
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,8)>='" + endMonth + "' AND SUBSTRING(vehmove_timein,1,8)<'" + startMonth + "')"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess.replace("emp_id", "vehmove_branch_id")
				+ StrSearch;
		logMonth4 = ExecuteQuery(StrSql);
		logMonth4 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=monthtraffic&starttime=" + startMonth + "&endtime=" + endMonth + "','jobcardlist','');remote.focus();\">" + logMonth4 + "</a>";

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
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,4)='" + currYear + "' AND SUBSTRING(vehmove_timein,5,2) " + quarter + ")"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("quarter1----" + StrSql);
		logQur1 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur1 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id
				+ "&movementreport=quartertraffic&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur1 + "</a>";

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
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1 "
				+ " AND (SUBSTRING(vehmove_timein,1,4)='" + currYear + "' AND SUBSTRING(vehmove_timein,5,2) " + quarter + ")"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		// SOP("StrSql----qur2-----" + StrSql);
		logQur2 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur2 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&movementreport=quartertraffic&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur2 + "</a>";
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
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND (SUBSTRING(vehmove_timein,1,4)='" + currYear + "' AND SUBSTRING(vehmove_timein,5,2) " + quarter + ")"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logQur3 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur3 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&movementreport=quartertraffic&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur3 + "</a>";

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
		StrSql = "SELECT COUNT(vehmove_id) "
				+ " FROM " + compdb(comp_id) + "axela_service_veh_move "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id=vehmove_branch_id"
				// + " inner join " + compdb(comp_id) +
				// "axela_emp on emp_id = jc_emp_id "
				+ " WHERE 1=1"
				+ " AND (SUBSTRING(vehmove_timein,1,4)='" + currYear + "' AND SUBSTRING(vehmove_timein,5,2) " + quarter + ")"
				+ BranchAccess.replace("branch_id", "vehmove_branch_id")
				+ ExeAccess
				+ StrSearch;
		logQur4 = ExecuteQuery(StrSql);
		quarter = URLEncoder.encode(quarter, "UTF-8");
		logQur4 = "<a href=\"javascript:remote=window.open('report-veh-movement.jsp?branch_id=" + dr_branch_id + "&technician=" + technicianexe_id + "&advisor=" + advisorexe_id
				+ "&movementreport=quartertraffic&starttime=" + currYear + "&endtime=" + quarter + "','jobcardlist','');remote.focus();\">" + logQur4 + "</a>";

	}

	public String PopulateTechnician(String dr_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = '1' AND emp_active='1'"
					+ " AND (emp_branch_id = " + dr_branch_id + " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + "))"
					// + " " + ExeAccess + ""
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
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
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = '1' AND emp_active='1'"
					+ " AND (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + "))"
					// + " " + ExeAccess + ""
					+ " GROUP BY emp_id order by emp_name";
			// / SOP("StrSql=aaa=" + StrSqlBreaker(StrSql));
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
