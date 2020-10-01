package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
///
public class Report_Vehicle_Service_Booking_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "", StrSmart = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0", vehcalltype_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime = "";
	public String months = "", end_time = "", endtime = "";
	public String veh_emp_id = "";
	public String veh_id = "", filter = "", emp_id = "";
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "", advisor_id = "", tech_id = "", jccat_id = "";
	public String msg = "";
	public Report_Check reportexe = new Report_Check();
	DecimalFormat df = new DecimalFormat("###.##");;
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				emp_id = PadQuotes(request.getParameter("emp_id"));
				// SOP("filter===" + filter);
				// SOP("emp_id========" + emp_id);

				if (filter.equals("yes"))
				{
					BookingStatusDetails(request, response);
				}
				GetValues(request, response);
				// CheckForm();
				if (go.equals("Go")) {
					StrSearch = ExeAccess; // .replace("emp_id", "crm.emp_id");

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND veh_branch_id IN (" + branch_id + ")";
					}

					if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
						StrSearch += " AND main.vehfollowup_emp_id = " + empcrm_id + "";
					}

					if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
						StrSearch += " AND main.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
					}

					// StrSearch += " AND SUBSTR(main.vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					// + " AND SUBSTR(main.vehfollowup_followup_time, 1, 8)  <= SUBSTR('" + endtime + "', 1, 8) ";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ServiceDetails();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");

		empcrm_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
		vehcalltype_id = CNumeric(PadQuotes(request.getParameter("dr_vehcalltype_id")));
		start_time = PadQuotes(request.getParameter("txt_start_time"));
		end_time = PadQuotes(request.getParameter("txt_end_time"));
		if (start_time.equals("")) {
			start_time = strToShortDate(ToShortDate(kknow()));
		}

		if (end_time.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
		}

		starttime = ConvertShortDateToStr(start_time);
		endtime = ConvertShortDateToStr(end_time);

		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}
	}

	protected void CheckForm() {
		msg = "";
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}
	public String ServiceDetails() {
		try {
			int count = 0;
			String grandtotalcontacted = "", grandtotal = "", grandtotalconversion = "";
			int vehicletotal = 0, contactedcount = 0;
			int bookingcount = 0;
			int conversioncount = 0;
			String bookingpercent = "";
			String conversionpercent = "";
			int booking = 0;
			int vehicles = 0;
			int conversion = 0;
			int mmscount = 0, totalmmscount = 0;
			int pndcount = 0, totalpndcount = 0;
			int svcount = 0, vehjobcards = 0, jobcards = 0, jc_emp_id = 0;
			int totalsvcount = 0, totalvehjobcards = 0, totaljobcards = 0;
			StringBuilder Str = new StringBuilder();

			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"

					+ "  COUNT(DISTINCT CASE WHEN SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('"
					+ starttime
					+ "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('"
					+ endtime
					+ "', 1, 8)"
					// + " AND main.vehfollowup_booking_main = 1"
					+ " THEN (vehfollowup_veh_id) END) as vehfollowup,"
					+ ""
					+ " COUNT(DISTINCT CASE WHEN SUBSTR(vehfollowup_modified_time, 1, 8) >= SUBSTR('"
					+ starttime
					+ "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_modified_time, 1, 8) <= SUBSTR('"
					+ endtime
					+ "', 1, 8)"
					// + " AND main.vehfollowup_booking_main = 1"
					+ " AND main.vehfollowup_vehaction_id = 1"
					+ " AND main.vehfollowup_booking_main = 1"
					// + " AND main.vehfollowup_enquiry_time =''"
					+ " THEN (vehfollowup_veh_id) END) as bookingcount,"
					// Self Visit
					+ " COUNT(DISTINCT CASE WHEN SUBSTR(vehfollowup_modified_time, 1, 8) >= SUBSTR('"
					+ starttime
					+ "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_modified_time, 1, 8) <= SUBSTR('"
					+ endtime
					+ "', 1, 8)"
					+ " AND vehfollowup_vehaction_id = 1"
					+ " AND vehfollowup_bookingtype_id = 1"
					+ " AND vehfollowup_booking_main = 1"
					+ " THEN (vehfollowup_veh_id) END) as svcount,"

					+ " COUNT(DISTINCT CASE WHEN "
					+ " SUBSTR(vehfollowup_modified_time, 1, 8) >= SUBSTR('"
					+ starttime
					+ "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_modified_time, 1, 8) <= SUBSTR('"
					+ endtime
					+ "', 1, 8)"
					+ " AND vehfollowup_vehaction_id = 1"
					+ " AND vehfollowup_bookingtype_id = 2"
					+ " AND vehfollowup_booking_main = 1"
					+ " THEN (vehfollowup_veh_id) END) as mmscount,"

					+ " COUNT(DISTINCT CASE WHEN SUBSTR(vehfollowup_modified_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_modified_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND vehfollowup_vehaction_id = 1"
					+ " AND vehfollowup_bookingtype_id = 3"
					+ " AND vehfollowup_booking_main = 1"
					+ " THEN (vehfollowup_veh_id) END) as pndcount,"

					+ " COALESCE (tblconvercount.convercount,0) AS conversion,"
					+ " COALESCE (tbljc.vehjobcards,0) AS vehjobcards,"
					+ " COALESCE (tbljc.jobcards,0) AS jobcards,"
					+ " COALESCE (tbljc.jc_emp_id, 0) AS jc_emp_id"

					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup main ON main.vehfollowup_emp_id  = emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id  = main.vehfollowup_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = main.vehfollowup_jc_id";

			if (!brand_id.equals("") || !region_id.equals("") || !zone_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}

			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = main.vehfollowup_vehcalltype_id "

					+ " LEFT JOIN ( SELECT COUNT( DISTINCT conver.vehfollowup_veh_id ) AS convercount, "
					+ " conver.vehfollowup_emp_id FROM " + compdb(comp_id) + "axela_service_followup conver "
					+ " WHERE conver.vehfollowup_jc_id > 0 "
					+ " AND SUBSTR( conver.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR( conver.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) "
					+ " AND conver.vehfollowup_veh_id IN "
					+ " ( SELECT booking.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup booking "
					+ " WHERE 1 = 1 AND booking.vehfollowup_vehaction_id = 1 "
					+ " AND booking.vehfollowup_booking_main = 1 "
					+ " AND SUBSTR( booking.vehfollowup_appt_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) ) "
					+ " GROUP BY vehfollowup_emp_id ) AS tblconvercount ON tblconvercount.vehfollowup_emp_id = emp_id "

					+ " LEFT JOIN (SELECT COUNT(DISTINCT jc_veh_id) AS vehjobcards, "
					+ " COUNT(jc_id) AS jobcards, veh_crmemp_id, jc_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " WHERE 1=1"
					+ " AND SUBSTR(jc_time_in,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " GROUP BY veh_crmemp_id"
					+ " ) AS tbljc ON tbljc.veh_crmemp_id = emp_id"

					+ " WHERE 1 = 1 "
					// + " AND main.vehfollowup_booking_main = 1"
					// + " AND emp_id != 1"
					+ " AND emp_active = 1 "
					+ " AND emp_crm = 1"
					+ StrSearch;
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// if (emp_id.equals("1")) {
			// SOP("service details========>" + StrSql);
			// }

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-responsive table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>CRM Executives</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Vehicle Follow-up</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking %</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Self Visit </th>\n");
				Str.append("<th data-hide=\"phone\" align=center>MMS </th>\n");
				Str.append("<th data-hide=\"phone\" align=center>P&D </th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking v/s Conversion</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking v/s Conversion %</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Vehicle Job Cards </th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Job Cards </th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					vehicletotal += crs1.getInt("vehfollowup");
					// totalfollowupcont += crs1.getInt("followup");
					bookingcount += crs1.getInt("bookingcount");
					booking = crs1.getInt("bookingcount");
					vehicles = crs1.getInt("vehfollowup");
					conversion = crs1.getInt("conversion");
					conversioncount += crs1.getInt("conversion");
					bookingpercent = getPercentage(booking, vehicles);
					conversionpercent = getPercentage(conversion, booking);
					svcount = crs1.getInt("svcount");
					mmscount = crs1.getInt("mmscount");
					pndcount = crs1.getInt("pndcount");
					vehjobcards = crs1.getInt("vehjobcards");
					jobcards = crs1.getInt("jobcards");
					jc_emp_id = crs1.getInt("jc_emp_id");
					totalsvcount += svcount;
					totalmmscount += mmscount;
					totalpndcount += pndcount;
					totalvehjobcards += vehjobcards;
					totaljobcards += jobcards;

					grandtotalcontacted = getPercentage(contactedcount, vehicletotal);
					grandtotal = getPercentage(bookingcount, vehicletotal);
					grandtotalconversion = getPercentage(conversioncount, bookingcount);
					count++;

					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left>");
					Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getInt("emp_id")).append(">")
							.append(crs1.getString("emp_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&vehicles=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=")
							.append(branch_id).append(" target=_blank>")
							.append(crs1.getString("vehfollowup")).append("</a></b></td>");

					Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&bookingcount=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id).append(" target=_blank>")
							.append(crs1.getString("bookingcount")).append("</a></b></td>");
					Str.append("<td align=right>")
							.append(bookingpercent).append("%").append("</a></b></td>");

					Str.append("<td align=right>")
							.append(svcount).append("</b></td>");

					Str.append("<td align=right>")
							.append(mmscount).append("</b></td>");
					Str.append("<td align=right>")
							.append(pndcount).append("</b></td>");

					Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&conversion=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id).append(" target=_blank>")
							.append(crs1.getString("conversion")).append("</a></b></td>");
					Str.append("<td align=right>")
							.append(conversionpercent).append("%").append("</b></td>");

					Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&vehjccount=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id).append(" target=_blank>")
							.append(vehjobcards).append("</a></td>");
					Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&jccount=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append(" target=_blank>")
							.append(jobcards).append("</a></td>");

					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td></td>");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&vehicles=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=").append(branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(vehicletotal).append("</b></td>\n");

				Str.append("<td align=right colspan=\"\"><b><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&bookingcount=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=").append(branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(bookingcount).append("</a></b></td>\n");
				Str.append("<td align=right><b>").append((grandtotal)).append("%").append("</b></td>\n");
				Str.append("<td align=right><b>").append((totalsvcount)).append("</b></td>\n");
				Str.append("<td align=right><b>").append((totalmmscount)).append("</b></td>\n");
				Str.append("<td align=right><b>").append((totalpndcount)).append("</b></td>\n");

				Str.append("<td align=right colspan=\"\"><b><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&conversion=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=").append(branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(conversioncount).append("</a></b></td>\n");
				Str.append("<td align=right><b>").append((grandtotalconversion)).append("%").append("</b></td>\n");

				Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&vehjccount=yes")
						.append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=").append(branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append("<b>" + (totalvehjobcards)).append("</b></a></td>\n");
				Str.append("<td align=right><a href=../service/report-vehicle-service-booking-status.jsp?filter=yes&jccount=yes")
						.append("&vehcalltype_id=").append(vehcalltype_id).append("&veh_branch_id=").append(branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append("<b>" + (totaljobcards)).append("</b></a></td>\n");

				Str.append("</tr>");

				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Booking Status found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateCRMExecutives(String crmemp_id, String comp_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_crm = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), crmemp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCallTypeDays() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehcalltype_id, vehcalltype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_calltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY vehcalltype_id"
					+ " ORDER BY vehcalltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehcalltype_id"));
				Str.append(Selectdrop(crs.getInt("vehcalltype_id"), vehcalltype_id));
				Str.append(">").append(crs.getString("vehcalltype_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("StrSql---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	private void BookingStatusDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			HttpSession session = request.getSession(true);
			String empid = CNumeric(PadQuotes(request.getParameter("emp_id")));
			String vehcalltype_id = CNumeric(PadQuotes(request.getParameter("vehcalltype_id")));
			String veh_branch_id = CNumeric(PadQuotes(request.getParameter("veh_branch_id")));
			String vehicles = PadQuotes(request.getParameter("vehicles"));
			String dmsallocate = PadQuotes(request.getParameter("dmsallocated"));
			String contacted = PadQuotes(request.getParameter("contacted"));
			String notcontacted = PadQuotes(request.getParameter("notcontacted"));
			String vehnotcontacted = PadQuotes(request.getParameter("vehnotcontacted"));
			// String notattempted = PadQuotes(request.getParameter("notattempted"));
			String followup = PadQuotes(request.getParameter("followup"));
			String bookingcount = PadQuotes(request.getParameter("bookingcount"));
			String bookingpercent = PadQuotes(request.getParameter("bookingpercent"));
			String conversion = PadQuotes(request.getParameter("conversion"));
			String conversionpercent = PadQuotes(request.getParameter("conversionpercent"));
			String duecon = PadQuotes(request.getParameter("duecon"));
			String dueconpercent = PadQuotes(request.getParameter("dueconpercent"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String vehjccount = PadQuotes(request.getParameter("vehjccount"));
			String jccount = PadQuotes(request.getParameter("jccount"));

			if (jccount.equals("yes")) {
				StrSmart += " AND SUBSTR(jc_time_in, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(jc_time_in, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";

				if (!empid.equals("0")) {
					StrSmart += " AND veh_crmemp_id =" + empid;
				}
				if (!veh_branch_id.equals("0")) {
					StrSmart += " AND veh_branch_id =" + veh_branch_id;
				}
				SetSession("jcstrsql", StrSmart, request);
				response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
			} else {
				if (vehjccount.equals("yes")) {
					StrSmart = " AND veh_id IN ("
							+ " SELECT DISTINCT (jc_veh_id)"
							+ " FROM " + compdb(comp_id) + "axela_service_jc"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
							+ " WHERE 1 = 1"
							+ " AND SUBSTR(jc_time_in, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(jc_time_in, 1, 8) <= SUBSTR('" + endtime + "', 1, 8))";

					if (!empid.equals("0")) {
						StrSmart += " AND veh_crmemp_id =" + empid;
					}

					if (!veh_branch_id.equals("0")) {
						StrSmart += " AND veh_branch_id =" + veh_branch_id;
					}
				}

				if (vehicles.equals("yes")) {

					StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
							+ " WHERE 1=1 ";
					// + " AND vehfollowup_booking_main = 1 ";
					if (!empid.equals("0")) {
						StrSmart += " AND vehfollowup_emp_id = " + empid + "";
					}
					if (!veh_branch_id.equals("0")) {
						StrSmart += " AND veh_branch_id = " + veh_branch_id + "";
					}
					if (!vehcalltype_id.equals("0")) {
						StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
					}
					StrSmart += " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
					// SOP("StrSmart-----------" + StrSmart);
				}

				// uniquenotcontacted

				if (bookingcount.equals("yes")) {
					StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
							+ " WHERE 1=1 "
							+ " AND vehfollowup_vehaction_id = 1"
							// + " AND vehfollowup_postponed !=1 ";
							// + " AND vehfollowup_enquiry_time =''";
							+ " AND vehfollowup_booking_main = 1";
					if (!empid.equals("0")) {
						StrSmart += " AND vehfollowup_emp_id = " + empid + "";
					}
					if (!veh_branch_id.equals("0")) {
						StrSmart += " AND veh_branch_id = " + veh_branch_id + "";
					}
					if (!vehcalltype_id.equals("0")) {
						StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
					}
					StrSmart += " AND SUBSTR(vehfollowup_modified_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(vehfollowup_modified_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
					// SOP("booking===" + StrSmart);
				}

				if (conversion.equals("yes")) {
					StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
							+ " WHERE 1=1 "
							+ " AND vehfollowup_jc_id > 0";
					if (!empid.equals("0")) {
						StrSmart += " AND vehfollowup_emp_id = " + empid + "";
					}
					if (!veh_branch_id.equals("0")) {
						StrSmart += " AND veh_branch_id = " + veh_branch_id + "";
					}
					if (!vehcalltype_id.equals("0")) {
						StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
					}

					StrSmart += " AND vehfollowup_veh_id IN (SELECT booking.vehfollowup_veh_id"
							+ " FROM " + compdb(comp_id) + "axela_service_followup booking"
							+ " WHERE 1=1"
							+ " AND booking.vehfollowup_vehaction_id = 1"
							+ " AND booking.vehfollowup_booking_main = 1"
							+ " AND SUBSTR(booking.vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(booking.vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "', 1, 8)"
							+ " AND vehfollowup_veh_id IN ( SELECT mainveh.vehfollowup_veh_id"
							+ " FROM " + compdb(comp_id) + "axela_service_followup mainveh"
							+ " WHERE 1=1 "
							+ " AND vehfollowup_followup_main = 1"
							+ " AND vehfollowup_vehaction_id = 1"
							+ " AND vehfollowup_booking_main = 1"
							+ " AND SUBSTR( mainveh.vehfollowup_appt_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8))))";
					// + " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
				}
				SetSession("vehstrsql", StrSmart, request);
				response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
