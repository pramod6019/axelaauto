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
public class Report_Vehicle_Service_Due_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "", StrSmart = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0", vehcalltype_id = "0";
	public String dr_branch_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime = "";
	public String months = "", end_time = "", endtime = "";
	public String veh_emp_id = "";
	public String veh_id = "", filter = "", emp_id = "";
	public String msg = "";
	public Report_Check reportexe = new Report_Check();
	DecimalFormat df = new DecimalFormat("###.##");
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "", advisor_id = "", tech_id = "", jccat_id = "";

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

				if (filter.equals("yes")) {
					DueStatusDetails(request, response);
				}
				GetValues(request, response);
				CheckForm();
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
					if (!dr_branch_id.equals("")) {
						StrSearch += " AND veh_branch_id IN (" + dr_branch_id + ")";
					}

					if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
						StrSearch += " AND main.vehfollowup_emp_id = " + empcrm_id + "";
					}

					if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
						StrSearch += " AND main.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
					}

					StrSearch += " AND SUBSTR(main.vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(main.vehfollowup_followup_time, 1, 8)  <= SUBSTR('" + endtime + "', 1, 8) ";

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
		dr_branch_id = RetrunSelectArrVal(request, "dr_branch_id");
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
	//
	protected void CheckForm() {
		msg = "";
		if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
			msg = msg + "<br>Start Date should be less than End date!";
		}
	}

	public String ServiceDetails() {
		try {
			int count = 0;
			int totalvehicle = 0, totalcontacted = 0, totalfollowupcont = 0, totalbookingcount = 0;
			int totalnotcontacted = 0;
			int totalconversion = 0;
			int totalduecon = 0;

			StringBuilder Str = new StringBuilder();

			String Strcontacted = " LEFT JOIN (SELECT COUNT(vehtrans.veh_id) AS vehcount, vehtrans.veh_crmemp_id AS vehtransveh_crmemp_id "
					+ " FROM " + compdb(comp_id) + "axela_service_veh vehtrans "
					+ " WHERE 1=1";
			if (!dr_branch_id.equals("")) {
				Strcontacted += " AND vehtrans.veh_branch_id IN (" + dr_branch_id + ")";
			}

			Strcontacted += " AND vehtrans.veh_id IN (SELECT trans.vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup trans "
					+ " WHERE 1=1 "
					+ " AND trans.vehfollowup_followup_main = 1 ";
			if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
				Strcontacted += " AND trans.vehfollowup_emp_id = " + empcrm_id;
			}
			if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
				Strcontacted += " AND trans.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
			}

			Strcontacted += " AND veh_crmemp_id= vehfollowup_emp_id "
					+ " AND SUBSTR( trans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR( trans.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) ) ";

			Strcontacted += " AND vehtrans.veh_id IN (SELECT trans.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup trans WHERE trans.vehfollowup_contactable_id = 1 ";

			if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
				Strcontacted += " AND trans.vehfollowup_emp_id = " + empcrm_id;
			}
			if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
				Strcontacted += " AND trans.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
			}
			Strcontacted += " AND veh_crmemp_id= vehfollowup_emp_id "
					+ " AND SUBSTR( trans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ ")"
					+ " GROUP BY vehtrans.veh_crmemp_id ) "
					+ " AS tblcontacted ON tblcontacted.vehtransveh_crmemp_id = emp_id ";

			StrSql = " SELECT emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COALESCE (COUNT(DISTINCT CASE WHEN main.vehfollowup_followup_main = 1"
					+ " AND veh_crmemp_id = main.vehfollowup_emp_id THEN veh_id END)) AS vehicles,"
					+ " COALESCE (tblcontacted.vehcount, 0) AS contacted,"
					+ " COALESCE ((COUNT(DISTINCT CASE WHEN main.vehfollowup_followup_main = 1"
					+ " AND veh_crmemp_id = main.vehfollowup_emp_id THEN veh_id END)- tblcontacted.vehcount),0) AS notcontacted,"
					// + " COALESCE (COUNT(main.vehfollowup_id),0) AS followup,"

					+ " COALESCE (COUNT(DISTINCT CASE WHEN main.vehfollowup_followup_main = 1"
					+ " THEN main.vehfollowup_id END)) AS followup,"

					// bookingcount
					+ " COALESCE(tblbooking.bookingcount,0) AS bookingcount,"

					// for conversion
					+ "	COALESCE (tblconvercount.convercount,0) AS conversion,"

					+ " COALESCE (SUM(IF(main.vehfollowup_desc='',1,0)),0) AS opencount,"
					+ " COALESCE (SUM(IF(main.vehfollowup_desc!='',1,0)),0) AS closedcount,"
					+ " COALESCE (COUNT(main.vehfollowup_id),0) AS totalcount,"
					+ " COALESCE(dueconcount, 0) AS duecon"

					// main join
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup  main ON main.vehfollowup_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id  = vehfollowup_veh_id";
			if (!zone_id.equals("") || !brand_id.equals("") || !dr_branch_id.equals("") || !region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}

			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
			}
			if (!dr_branch_id.equals("")) {
				StrSearch += " AND veh_branch_id IN (" + dr_branch_id + ")";
			}

			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ Strcontacted
					// + Strnontcontacted

					// bookingcount
					+ " LEFT JOIN (SELECT COUNT(DISTINCT booking.vehfollowup_veh_id) as bookingcount, booking.vehfollowup_emp_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup booking "
					+ " WHERE booking.vehfollowup_vehaction_id = 1 "
					+ " AND booking.vehfollowup_booking_main = 1";
			if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
				StrSql += " AND booking.vehfollowup_vehcalltype_id = " + vehcalltype_id;
			}
			StrSql += " AND SUBSTR(booking.vehfollowup_entry_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND booking.vehfollowup_veh_id IN (SELECT mainveh.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup mainveh "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = mainveh.vehfollowup_veh_id";

			if (!zone_id.equals("") || !brand_id.equals("") || !dr_branch_id.equals("") || !region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}// added

			StrSql += " WHERE mainveh.vehfollowup_followup_main = 1 ";

			if (!dr_branch_id.equals("")) {
				StrSql += " AND veh_branch_id IN (" + dr_branch_id + ")";
			}// added

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}// added

			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}// added

			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_id + ") ";
			}// added

			StrSql += " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) )"
					+ " GROUP BY booking.vehfollowup_emp_id ) AS tblbooking ON tblbooking.vehfollowup_emp_id = emp_id"

					// conversioncount

					+ " LEFT JOIN (SELECT COUNT(DISTINCT conver.vehfollowup_veh_id) AS convercount, conver.vehfollowup_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup conver "
					+ " WHERE conver.vehfollowup_jc_id > 0"
					+ " AND SUBSTR(conver.vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND conver.vehfollowup_veh_id IN (SELECT booking.vehfollowup_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup booking"
					+ " WHERE 1=1"
					+ " AND booking.vehfollowup_vehaction_id = 1"
					+ " AND booking.vehfollowup_booking_main = 1"
					+ " AND SUBSTR(booking.vehfollowup_entry_time,1,8) >= SUBSTR('" + starttime + "', 1, 8))"
					+ " AND conver.vehfollowup_veh_id IN ( SELECT mainveh.vehfollowup_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup mainveh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = mainveh.vehfollowup_veh_id";

			if (!zone_id.equals("") || !brand_id.equals("") || !dr_branch_id.equals("") || !region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}// added

			StrSql += " WHERE 1=1 "
					+ " AND mainveh.vehfollowup_followup_main = 1";
			if (!dr_branch_id.equals("")) {
				StrSql += " AND veh_branch_id IN (" + dr_branch_id + ")";
			}// added

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}// added

			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}// added

			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_id + ") ";
			}// added
			StrSql += " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8))";

			StrSql += " GROUP BY vehfollowup_emp_id) AS tblconvercount ON tblconvercount.vehfollowup_emp_id = emp_id"

					// for duecon
					+ " LEFT JOIN ( SELECT COUNT(DISTINCT followuptrans.vehfollowup_veh_id) AS dueconcount, followuptrans.vehfollowup_emp_id AS duecon_emp_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup followuptrans "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh duevehtrans ON duevehtrans.veh_id = followuptrans.vehfollowup_veh_id ";

			if (!zone_id.equals("") || !brand_id.equals("") || !dr_branch_id.equals("") || !region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = duevehtrans.veh_branch_id";
			}// added

			StrSql += " WHERE 1 = 1 ";

			if (!dr_branch_id.equals("")) {
				StrSql += " AND duevehtrans.veh_branch_id IN (" + dr_branch_id + ")";
			}// added

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}// added

			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}// added

			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_id + ") ";
			}// added

			if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
				StrSql += " AND followuptrans.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
			}// added

			StrSql += " AND followuptrans.vehfollowup_followup_main = 1 "
					+ " AND SUBSTR( followuptrans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR( followuptrans.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) "

					+ " AND duevehtrans.veh_id IN (SELECT duecontacted.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup duecontacted "
					+ " WHERE 1 = 1 AND duecontacted.vehfollowup_contactable_id = 1 "
					+ " AND SUBSTR( duecontacted.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR( duecontacted.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) ) "

					+ " AND duevehtrans.veh_id IN (SELECT servicedtrans.vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup servicedtrans "
					+ " WHERE servicedtrans.vehfollowup_jc_id > 0 "
					+ " AND SUBSTR( servicedtrans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) ) "
					+ " GROUP BY duecon_emp_id ) AS tblduecon ON tblduecon.duecon_emp_id = emp_id"

					+ " WHERE 1 = 1 "
					+ " AND emp_active = 1 "
					+ " AND emp_crm = 1"
					+ StrSearch;
			if (!empcrm_id.equals("0")) {
				StrSql += " AND emp_id = " + empcrm_id;
			}
			StrSql += " AND emp_id != 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			SOP("StrSql--------" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-responsive table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>CRM Executives</th>\n");
				Str.append("<th align=center>Vehicles</th>\n");
				Str.append("<th align=center>Contacted</th>\n");
				Str.append("<th align=center>Contacted %</th>\n");
				Str.append("<th align=center>Unique Not Contacted</th>\n");
				Str.append("<th align=center>Total Follow-ups</th>\n");
				Str.append("<th align=center>Booking</th>\n");
				Str.append("<th align=center>Booking %</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking v/s Conversion</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Booking v/s Conversion %</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Due v/s Conversion </th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Due v/s Conversion  %</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs1.next()) {
					totalvehicle += crs1.getInt("vehicles");
					totalnotcontacted += crs1.getInt("notcontacted");
					totalcontacted += crs1.getInt("contacted");
					totalfollowupcont += crs1.getInt("followup");
					totalbookingcount += crs1.getInt("bookingcount");
					totalconversion += crs1.getInt("conversion");
					totalduecon += crs1.getInt("duecon");

					count++;

					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");

					// CRM Executives
					Str.append("<td align=left>");
					Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs1.getInt("emp_id")).append(">")
							.append(crs1.getString("emp_name")).append("</a>");
					Str.append("</td>\n");

					// Vehicles
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&vehicles=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("vehicles")).append("</a></b></td>");

					// Contacted
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&contacted=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(Integer.parseInt(crs1.getString("contacted"))).append("</a></b></td>");

					// Contacted %
					Str.append("<td align=right>")
							.append(getPercentage(crs1.getInt("contacted"), crs1.getInt("vehicles"))).append("%").append("</a></b></td>");

					// Unique Not Contacted
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&notcontacted=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("notcontacted")).append("</a></b></td>");

					// Total Follow-ups
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&followup=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("followup")).append("</a></b></td>");

					// Booking
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&bookingcount=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("bookingcount")).append("</a></b></td>");

					// Booking %
					Str.append("<td align=right>")
							.append(getPercentage(crs1.getInt("bookingcount"), crs1.getInt("vehicles"))).append("%").append("</a></b></td>");

					// Booking v/s Conversion
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&conversion=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("conversion")).append("</a></b></td>");

					// Booking v/s Conversion %
					Str.append("<td align=right>")
							.append(getPercentage(crs1.getInt("conversion"), crs1.getInt("bookingcount"))).append("%").append("</a></b></td>");

					// Due v/s Conversion
					Str.append("<td align=right><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&duecon=yes&emp_id=").append(crs1.getInt("emp_id"))
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("&vehcalltype_id=").append(vehcalltype_id)
							.append("&brand_id=").append(brand_id)
							.append("&region_id=").append(region_id)
							.append("&zone_id=").append(zone_id)
							.append("&branch_id=").append(dr_branch_id)
							.append(" target=_blank>")
							.append(crs1.getString("duecon")).append("</a></b></td>");

					// Due v/s Conversion %
					Str.append("<td align=right>").append(getPercentage(crs1.getInt("duecon"), crs1.getInt("vehicles"))).append("%").append("</a></b></td>");

					Str.append("</tr>");
				}
				// FOR TOTAL

				Str.append("<tr>\n");
				Str.append("<td></td>");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&vehicles=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalvehicle).append("</b></td>\n");

				Str.append("<td align=right><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&contacted=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalcontacted).append("</a></b></td>\n");

				Str.append("<td align=right><b>").append(getPercentage(totalcontacted, totalvehicle)).append("%").append("</b></td>\n");

				Str.append("<td align=right><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&notcontacted=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalnotcontacted).append("</a></b></td>\n");

				Str.append("<td align=right><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&followup=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalfollowupcont).append("</a></b></td>\n");

				Str.append("<td align=right colspan=\"\"><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&bookingcount=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalbookingcount).append("</a></b></td>\n");

				Str.append("<td align=right><b>").append(getPercentage(totalbookingcount, totalvehicle)).append("%").append("</b></td>\n");

				Str.append("<td align=right colspan=\"\"><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&conversion=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalconversion).append("</a></b></td>\n");
				Str.append("<td align=right><b>").append(getPercentage(totalconversion, totalbookingcount)).append("%").append("</b></td>\n");

				Str.append("<td align=right colspan=\"\"><b><a href=../service/report-vehicle-service-due-status.jsp?filter=yes&duecon=yes&emp_id=").append(empcrm_id)
						.append("&vehcalltype_id=").append(vehcalltype_id)
						.append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id)
						.append("&zone_id=").append(zone_id)
						.append("&branch_id=").append(dr_branch_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append(" target=_blank>")
						.append(totalduecon).append("</a></b></td>\n");

				Str.append("<td align=right><b>").append(getPercentage(totalduecon, totalvehicle)).append("%").append("</b></td>\n");

				Str.append("</tr>");

				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Service Due Status found!</b></font>");
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

	private void DueStatusDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			HttpSession session = request.getSession(true);
			String empid = CNumeric(PadQuotes(request.getParameter("emp_id")));
			String vehcalltype_id = CNumeric(PadQuotes(request.getParameter("vehcalltype_id")));
			String brand_id = RetrunSelectArrVal(request, "brand_id");
			String region_id = RetrunSelectArrVal(request, "region_id");
			String zone_id = RetrunSelectArrVal(request, "zone_id");
			String branch_id = RetrunSelectArrVal(request, "branch_id");
			String vehicles = PadQuotes(request.getParameter("vehicles"));
			String contacted = PadQuotes(request.getParameter("contacted"));
			String notcontacted = PadQuotes(request.getParameter("notcontacted"));
			String followup = PadQuotes(request.getParameter("followup"));
			String bookingcount = PadQuotes(request.getParameter("bookingcount"));
			String conversion = PadQuotes(request.getParameter("conversion"));
			String duecon = PadQuotes(request.getParameter("duecon"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String branchfilter = "";
			if (!brand_id.equals("")) {
				branchfilter += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				branchfilter += " AND branch_region_id IN (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				branchfilter += " AND branch_zone_id IN (" + zone_id + ") ";
			}
			if (!branch_id.equals("")) {
				branchfilter += " AND veh_branch_id IN (" + branch_id + ")";
			}

			if (vehicles.equals("yes")) {
				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_followup_main = 1 ";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " AND veh_crmemp_id = vehfollowup_emp_id "
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			}
			// SOP("StrSmart=123==" + StrSmart);
			if (contacted.equals("yes")) {

				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_followup_main = 1 ";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " AND veh_crmemp_id = vehfollowup_emp_id "
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
				// not in filter aggregate
				StrSmart += " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_contactable_id = 1";
				// + " AND vehfollowup_followup_main = 1 ";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}

				StrSmart += " AND veh_crmemp_id = vehfollowup_emp_id"
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ ")";

			}

			// uniquenotcontacted
			if (notcontacted.equals("yes")) {
				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_followup_main = 1 "
						+ " AND vehfollowup_emp_id = veh_crmemp_id";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " "
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
				// not in filter aggregate
				StrSmart += " AND veh_id NOT IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_contactable_id = 1 "
						+ " AND vehfollowup_emp_id = veh_crmemp_id"
						+ "";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " "
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						// + " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8)"
						+ ")";
			}
			// SOP("StrSmart===" + StrSmart);
			if (followup.equals("yes")) {
				// StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
				// + " WHERE 1=1 "
				// + " AND vehfollowup_id !=0"
				// + " AND vehfollowup_vehaction_id = 1";
				// if (!empid.equals("0")) {
				// StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				// }
				//
				// StrSmart += branchfilter;
				//
				// if (!vehcalltype_id.equals("0")) {
				// StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				// }
				//
				// StrSmart += " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
				// + " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
				// // SOP("StrSmart-----followup------" + StrSmart);

				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_followup_main = 1 ";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " AND veh_crmemp_id = vehfollowup_emp_id "
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			}

			if (bookingcount.equals("yes")) {
				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_vehaction_id = 1"
						+ " AND vehfollowup_booking_main = 1";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}
				StrSmart += " AND SUBSTR(vehfollowup_entry_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						// + " AND SUBSTR(vehfollowup_entry_time,1,8) <= SUBSTR('" + endtime + "',1,8)"
						+ " AND vehfollowup_veh_id IN (SELECT mainveh.vehfollowup_veh_id "
						+ " FROM " + compdb(comp_id) + "axela_service_followup mainveh "
						+ " WHERE mainveh.vehfollowup_followup_main = 1 "
						+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
						+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) ))";

			}

			if (conversion.equals("yes")) {
				StrSmart = " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_jc_id > 0";
				if (!empid.equals("0")) {
					StrSmart += " AND vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("0")) {
					StrSmart += "	AND vehfollowup_vehcalltype_id = " + vehcalltype_id;
				}

				StrSmart += " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
						+ " AND vehfollowup_veh_id IN (SELECT booking.vehfollowup_veh_id"
						+ " FROM " + compdb(comp_id) + "axela_service_followup booking"
						+ " WHERE 1=1"
						+ " AND booking.vehfollowup_vehaction_id = 1"
						+ " AND booking.vehfollowup_booking_main = 1"
						+ " AND SUBSTR(booking.vehfollowup_entry_time,1,8) >= SUBSTR('" + starttime + "', 1, 8))"
						+ " AND vehfollowup_veh_id IN ( SELECT mainveh.vehfollowup_veh_id"
						+ " FROM " + compdb(comp_id) + "axela_service_followup mainveh"
						+ " WHERE 1=1 "
						+ " AND vehfollowup_followup_main = 1"
						+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR( mainveh.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8)) )";
				// + " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";
			}

			if (duecon.equals("yes")) {
				StrSmart = " AND veh_id IN (SELECT DISTINCT followuptrans.vehfollowup_veh_id AS dueconcount"
						+ " FROM " + compdb(comp_id) + "axela_service_followup followuptrans "
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh duevehtrans ON duevehtrans.veh_id = followuptrans.vehfollowup_veh_id "
						+ " WHERE 1 = 1 ";

				if (!empid.equals("0")) {
					StrSmart += " AND followuptrans.vehfollowup_emp_id = " + empid + "";
				}

				StrSmart += branchfilter;

				if (!vehcalltype_id.equals("") && !vehcalltype_id.equals("0")) {
					StrSmart += " AND followuptrans.vehfollowup_vehcalltype_id = " + vehcalltype_id + "";
				}

				StrSmart += " AND followuptrans.vehfollowup_followup_main = 1 "
						+ " AND SUBSTR( followuptrans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
						+ " AND SUBSTR( followuptrans.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) "

						+ " AND duevehtrans.veh_id IN (SELECT duecontacted.vehfollowup_veh_id "
						+ " FROM " + compdb(comp_id) + "axela_service_followup duecontacted "
						+ " WHERE 1 = 1 AND duecontacted.vehfollowup_contactable_id = 1 "
						+ " AND SUBSTR( duecontacted.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8) "
						+ " AND SUBSTR( duecontacted.vehfollowup_followup_time, 1, 8 ) <= SUBSTR('" + endtime + "', 1, 8) ) "

						+ " AND duevehtrans.veh_id IN (SELECT servicedtrans.vehfollowup_veh_id "
						+ " FROM " + compdb(comp_id) + "axela_service_followup servicedtrans "
						+ " WHERE servicedtrans.vehfollowup_jc_id > 0 "
						+ " AND SUBSTR( servicedtrans.vehfollowup_followup_time, 1, 8 ) >= SUBSTR('" + starttime + "', 1, 8)) ) ";

			}
			// SOP("StrSmart=Due=" + StrSmart);
			SetSession("vehstrsql", StrSmart, request);
			response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
