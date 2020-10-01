package axela.service;
//divya 30th may 2013
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Service_Booking extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", branch_id = "0";
	public String[] exe_ids, branch_ids, crm_emp_ids;
	public String team_id = "", crm_emp_id = "";
	public String StrHTML = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String BookingSearch = "", StrSearch = "";
	public String BookingSearchStr = "", FollowupSearchStr = "";
	public String EmpSearch = "";
	public String ExeAccess = "";
	public String go = "";
	public String bookingvehcount = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public String TeamJoin = "";
	// public String crmfollowupdays_id = "";
	public String crmdays_id = "";
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				bookingvehcount = PadQuotes(request.getParameter("bookingvehcount"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					BookingSearch = BranchAccess.replace("branch_id", "booking_branch_id") + "";

					if (!starttime.equals("")) {
						BookingSearch = BookingSearch + " and SUBSTR(booking_time, 1, 8) >= SUBSTR('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						BookingSearch = BookingSearch + " and SUBSTR(booking_time, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
					}
					if (!crm_emp_id.equals("")) {
						EmpSearch = " AND emp_id in (" + crm_emp_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = FollowupDetails();
					}
				} else if (bookingvehcount.equals("yes")) {
					BookingSearchStr = PadQuotes(request.getParameter("BookingSearchStr"));
					FollowupSearchStr = PadQuotes(request.getParameter("FollowupSearchStr"));
					if (!BookingSearchStr.equals("")) {
						// SOP("BookingSearchStr===" +
						// URLDecoder.decode(unescapehtml(BookingSearchStr),
						// "UTF-8"));
						StrSearch = " AND  veh_id IN (SELECT booking_veh_id"
								+ " FROM " + compdb(comp_id) + "axela_service_booking"
								+ " WHERE 1=1 "
								+ " " + URLDecoder.decode(unescapehtml(BookingSearchStr), "UTF-8")
								+ " )";
					} else {
						StrSearch = " AND  veh_id IN (SELECT vehfollowup_veh_id"
								+ " FROM " + compdb(comp_id) + "axela_service_followup"
								+ " WHERE 1=1 "
								+ " "
								+ URLDecoder.decode(unescapehtml(FollowupSearchStr.replace("booking_time", "vehfollowup_followup_time").replace("booking_crm_emp_id", "vehfollowup_emp_id")), "UTF-8")
								+ " )";
					}

					SetSession("vehstrsql", StrSearch, request);
					response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		// SOP("starttime-------" + starttime);
		// SOP("endtime-------" + endtime);
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");

		crm_emp_id = RetrunSelectArrVal(request, "dr_emp_id");
		crm_emp_ids = request.getParameterValues("dr_emp_id");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String FollowupDetails() {
		try {
			int count = 0;
			int bookingtotal = 0;
			int bookingconverstion = 0;
			String bookinggrandconverstion = "";
			int bookingvehcount = 0;
			int bookingopencount = 0;
			int bookingsercount = 0;
			int bookingpostcount = 0;
			int followupvehcount = 0;
			int openfollowupcount = 0;
			int closedfollowupcount = 0;
			int followupcount = 0;
			int bookingcount = 0;
			int bookinggrandtotal = 0;
			int totalbookingvehcount = 0;
			int totalbookingopencount = 0;
			int totalbookingsercount = 0;
			int totalbookingpostcount = 0;
			int totalfollowupvehcount = 0;
			int totalopenfollowupcount = 0;
			int totalclosedfollowupcount = 0;
			int totalfollowupcount = 0;
			int totalbookingcount = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COALESCE((SELECT COUNT(DISTINCT booking_veh_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " WHERE booking_crm_emp_id = emp_id"
					+ " " + BookingSearch
					+ " ),0) AS bookingvehcount,"

					+ " @bookingopencount:= COALESCE((SELECT count(booking_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " WHERE booking_crm_emp_id = emp_id"
					+ " AND booking_bookingstatus_id = 1"
					+ " " + BookingSearch
					+ " ),0) AS bookingopencount,"

					+ " @bookingsercount:= COALESCE((SELECT count(booking_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " WHERE booking_crm_emp_id = emp_id"
					+ " AND booking_bookingstatus_id = 2"
					+ " " + BookingSearch
					+ " ),0) AS bookingsercount,"

					+ " @bookingpostcount:= COALESCE((SELECT count(booking_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " WHERE booking_crm_emp_id = emp_id"
					+ " AND booking_bookingstatus_id = 3"
					+ " " + BookingSearch
					+ " ),0) AS bookingpostcount,"

					+ " @bookingtotal:= (@bookingopencount + @bookingsercount + @bookingpostcount) as bookingtotal,"
					+ " @bookingconverstion:= (@bookingsercount / @bookingtotal * 100) as bookingconverstion,"

					+ " COALESCE((SELECT COUNT(DISTINCT vehfollowup_veh_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_emp_id = emp_id"
					+ " " + BookingSearch.replace("booking_time", "vehfollowup_followup_time").replace("booking_branch_id", "veh_branch_id")
					+ " ),0) AS followupvehcount,"

					+ " COALESCE((SELECT count(vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_emp_id = emp_id"
					+ " AND vehfollowup_desc = ''"
					+ " " + BookingSearch.replace("booking_time", "vehfollowup_followup_time").replace("booking_branch_id", "veh_branch_id")
					+ " ),0) AS openfollowupcount,"

					+ " COALESCE((SELECT count(vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_emp_id = emp_id"
					+ " AND vehfollowup_desc != ''"
					+ " " + BookingSearch.replace("booking_time", "vehfollowup_followup_time").replace("booking_branch_id", "veh_branch_id")
					+ " ),0) AS closedfollowupcount,"

					+ " COALESCE((SELECT count(vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_emp_id = emp_id"
					+ " " + BookingSearch.replace("booking_time", "vehfollowup_followup_time").replace("booking_branch_id", "veh_branch_id")
					+ " ),0) AS totalfollowupcount,"

					+ " COALESCE((SELECT count(booking_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_booking ON vehfollowup_veh_id = booking_veh_id"
					+ " WHERE 1=1"
					+ " AND vehfollowup_emp_id = emp_id"
					+ " AND (booking_bookingstatus_id = 1 OR booking_bookingstatus_id = 2)"
					+ " " + BookingSearch.replace("booking_time", "vehfollowup_followup_time")
					+ " ),0) AS totalbookingcount"

					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND emp_active = 1"
					+ " AND emp_crm = 1"
					+ " " + EmpSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("BookingSearch===" + BookingSearch);
			// + " LIMIT 1000";

			// SOP("StrSql====BookingDetails=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>CRM</th>\n");
				Str.append("<th align=center>Booking Vehicle Count</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Open Booking</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Serviced Booking</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Postponed Booking</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Total Booking</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Converstion %</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Follow-up Vehicle Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Open Follow-up</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Closed Follow-up</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Total Follow-up</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Total Booking</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					bookingvehcount = crs.getInt("bookingvehcount");
					bookingopencount = crs.getInt("bookingopencount");
					bookingsercount = crs.getInt("bookingsercount");
					bookingpostcount = crs.getInt("bookingpostcount");
					followupvehcount = crs.getInt("followupvehcount");
					openfollowupcount = crs.getInt("openfollowupcount");
					closedfollowupcount = crs.getInt("closedfollowupcount");
					followupcount = crs.getInt("totalfollowupcount");
					bookingcount = crs.getInt("totalbookingcount");

					totalbookingvehcount += bookingvehcount;
					totalbookingopencount += bookingopencount;
					totalbookingsercount += bookingsercount;
					totalbookingpostcount += bookingpostcount;
					totalfollowupvehcount += followupvehcount;
					totalopenfollowupcount += openfollowupcount;
					totalclosedfollowupcount += closedfollowupcount;
					totalfollowupcount += followupcount;
					totalbookingcount += bookingcount;
					bookingtotal = (int) crs.getDouble("bookingtotal");
					bookingconverstion = (int) crs.getDouble("bookingconverstion");
					bookinggrandtotal += bookingtotal;

					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=left>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td align=right><a href=\"../service/report-service-booking.jsp?bookingvehcount=yes&BookingSearchStr=")
							.append(URLEncoder.encode(BookingSearch + " AND booking_crm_emp_id=" + crs.getString("emp_id"), "UTF-8")).append("\">")
							.append(bookingvehcount).append("</a></td>\n");
					// Str.append("<td align=right>").append(crs.getString("bookingvehcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("bookingopencount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("bookingsercount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("bookingpostcount")).append("</td>\n");
					Str.append("<td align=right>").append(bookingtotal).append("</td>\n");
					Str.append("<td align=right>").append(bookingconverstion).append("%").append("</td>\n");
					Str.append("<td align=right><a href=\"../service/report-service-booking.jsp?bookingvehcount=yes&FollowupSearchStr=")
							.append(URLEncoder.encode(BookingSearch + " AND booking_crm_emp_id=" + crs.getString("emp_id"), "UTF-8")).append("\">")
							.append(followupvehcount).append("</a></td>\n");
					// Str.append("<td align=right>").append(crs.getString("followupvehcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("openfollowupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("closedfollowupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("totalfollowupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("totalbookingcount")).append("</td>\n");
					Str.append("</tr>");

				}
				bookinggrandconverstion = getPercentage((double) totalbookingsercount, (double) bookinggrandtotal);

				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><a href=\"../service/report-service-booking.jsp?bookingvehcount=yes&BookingSearchStr=").append(URLEncoder.encode(BookingSearch, "UTF-8")).append("\">")
						.append(totalbookingvehcount).append("</a></td>\n");
				// emp_id=").append(crs.getString("emp_id")).append("&
				// Str.append("<td align=right><b>").append(totalbookingvehcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingopencount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingsercount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingpostcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(bookinggrandtotal).append("</b></td>\n");
				Str.append("<td align=right><b>").append(bookinggrandconverstion).append("%").append("</b></td>\n");
				Str.append("<td align=right><a href=\"../service/report-service-booking.jsp?bookingvehcount=yes&FollowupSearchStr=").append(URLEncoder.encode(BookingSearch, "UTF-8")).append("\">")
						.append(totalfollowupvehcount).append("</a></td>\n");
				// Str.append("<td align=right><b>").append(totalfollowupvehcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalopenfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalclosedfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Service Booking found!</b></font>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateCRM() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_crm = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql--PopulateCRM----" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), crm_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
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
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("StrSql------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
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
