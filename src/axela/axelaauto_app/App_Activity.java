// Ved (11 Feb, 29 March 2013)
package axela.axelaauto_app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Activity extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String activity_emp_id = "0";
	public long start = 0;
	public long end = 0;
	public Date startdatevalue;
	public Date enddatevalue;
	public String startdate = "";
	public String enddate = "";
	public String startDatetime = "";
	public String endDatetime = "";
	public String startyear = "";
	public String startmonth = "";
	public String startday = "";
	public String starthour = "";
	public String startmin = "";
	public String startsec = "";
	public String endyear = "";
	public String endmonth = "";
	public String endday = "";
	public String endhour = "";
	public String endmin = "";
	public String endsec = "";
	public String comp_id = "0", StrSearch = "";
	public String emp_uuid = "";
	public String access = "", emp_all_exe = "";
	public String ExeAccess = "", emp_id = "0", exec = "0", day = "", month = "", year = "", startdate1 = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		if (!emp_id.equals("0")) {
			exec = CNumeric(PadQuotes(request.getParameter("dr_executive")));
			startdate1 = PadQuotes(request.getParameter("txt_date"));
			if (exec.equals("0")) {
				exec = emp_id;
			}
			if (startdate1.equals("")) {
				startdate1 = DateToShortDate(kknow());
			}
			if (!startdate1.equals("")) {
				// startdate = startdate1.replace('-', '/');
				day = startdate1.substring(0, 2);
				month = startdate1.substring(3, 5);
				year = startdate1.substring(6, 10);
				startdate = day + "/" + month + "/" + year;
				startdate = startdate + " " + "00" + ":" + "00";
				// SOP("startdate======111111111111====" + startdate);
				// isValidDateFormatLong(startdate);
				// startdate = ConvertLongDateToStr(startdate);
				startdate = ConvertShortDateToStr(startdate);
				enddate = day + "/" + month + "/" + year;
				enddate = enddate + " " + "23" + ":" + "59";
				isValidDateFormatLong(enddate);
				enddate = ConvertLongDateToStr(enddate);
			}
			try {
				// CheckPerm(comp_id, "emp_activity_access", request, response);
				access = ReturnPerm(comp_id, "emp_activity_access", request);

				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				ExeAccess = GetSession("ExeAccess", request);
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }

				StrSql = "(SELECT activity_start_datetime AS start_time," + " DATE_FORMAT(DATE_ADD(activity_end_datetime, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time,"
						+ " activity_title AS title, CONCAT(IF(activity_desc != '', CONCAT(activity_desc, '<br>'), '')," + " IF(type_name != '', CONCAT('Type: ', type_name, '<br>'), ''),"
						+ " IF(priority_name != '', CONCAT('Priority: ', priority_name, '<br>'), ''),"
						+ " IF(activity_contact_person != '', CONCAT('Contact: ', activity_contact_person, '<br>'), '')," + " IF(activity_phone1 != '', CONCAT(activity_phone1, '<br>'), ''),"
						+ " IF(activity_phone2 != '', CONCAT(activity_phone2, '<br>'), '')," + " IF(activity_feedback != '', CONCAT('Feedback: ', activity_feedback, '<br>'), ''),"
						+ " IF(activity_remarks != '', CONCAT('Remarks: ', activity_remarks), '')) AS description,"
						+ " activity_id, 'activity' AS type, '' AS feedback, COALESCE(contact_mobile1,'') AS contact_mobile1"
						+ " FROM "
						+ compdb(comp_id) + "axela_activity"
						+ " INNER JOIN " + compdb(comp_id) + "axela_activity_type " + " ON axela_activity.activity_type_id = axela_activity_type.type_id"
						// " LEFT JOIN axela_activity_status ON axela_activity.activity_status_id = axela_activity_status.status_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_activity_priority" + " ON priority_id = activity_priority_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON activity_contact_id = contact_id"
						+ " WHERE activity_emp_id = " + exec + ""
						+ " AND (activity_start_datetime >= " + startdate + ")"
						+ " AND (activity_end_datetime <= " + enddate + "))"

						// + " AND (activity_start_datetime >= " + startdate +
						// ""
						// + " AND activity_start_datetime < " + enddate + ""
						// + " OR (activity_start_datetime < " + startdate + ""
						// + " AND activity_end_datetime > " + enddate + ")"
						// + " OR (activity_end_datetime > " + startdate + ""
						// + " AND activity_end_datetime <= " + enddate + ")))"

						// For Oppr Followup
						+ " UNION ALL (SELECT followup_followup_time AS start_time,"
						+ " DATE_FORMAT(DATE_ADD(followup_followup_time, INTERVAL 2 hour), '%Y%m%d%H%i%S') AS end_time,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS title,"
						+ " CONCAT(IF(followuptype_name != '', CONCAT('Type: ', followuptype_name, ' - Enquiry Follow-up', '<br>'), ''),"
						+ " IF(followup_desc != '', CONCAT('Feedback: ', followup_desc, '<br>'), ''),"
						+ " IF(CONCAT(contact_fname, ' ', contact_lname) != '',"
						+ " CONCAT('Contact: ', CONCAT(contact_fname, ' ', contact_lname, '<br>')), ''),"
						+ " IF(contact_mobile1 != '', CONCAT('Mobile: ', contact_mobile1), '')) AS description,"
						+ " enquiry_id AS activity_id, 'opprfollowup' AS type, followup_desc AS feedback,contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry " + " ON enquiry_id = followup_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type " + " ON followuptype_id = followup_followuptype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact " + " ON contact_id = enquiry_contact_id"
						+ " WHERE followup_emp_id = " + exec + ""
						+ " AND (followup_followup_time >= " + startdate + ")"
						+ " AND (followup_followup_time <= " + enddate + "))"

						// + " AND (followup_followup_time >= " + startdate + ""
						// + " AND followup_followup_time < " + enddate + ""
						// + " OR (followup_followup_time < " + startdate + ""
						// + " AND followup_followup_time > " + enddate + ")"
						// + " OR (followup_followup_time > " + startdate + ""
						// + " AND followup_followup_time <= " + enddate +
						// "))))"

						// Demo
						+ " UNION ALL (SELECT testdrive_time_from AS start_time, testdrive_time_to AS end_time,"
						+ " 'Test Drive' AS title, CONCAT('Contact: ', contact_fname, ' ', contact_lname,"
						+ " '<br>Mobile: ', contact_mobile1, '<br>Location: ', location_name,"
						+ " IF(testdrive_fb_taken = 1, '<br>Status: Taken', IF(testdrive_fb_taken = 2, '<br>Status: Not Taken', ''))) AS description,"
						+ " testdrive_id AS activity_id, 'Demo' AS type,"
						+ " IF(testdrive_fb_taken = 0, '', 'Feedback given') AS feedback, contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry " + " ON enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location " + " ON location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact " + " ON contact_id = enquiry_contact_id"
						+ " WHERE testdrive_emp_id = " + exec + ""
						+ " AND (testdrive_time_from >= " + startdate + ")"
						+ " AND testdrive_time_to <= " + enddate + ")"
						// + " AND (testdrive_time_from >= " + startdate + ""
						// + " AND testdrive_time_to < " + enddate + ")))"

						// For Call Follow-up
						/*
						 * + " UNION ALL (SELECT call_followup_time AS start_time," + " DATE_FORMAT(DATE_ADD(call_followup_time, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time," +
						 * " calltype_name AS title, CONCAT(IF(CONCAT(contact_fname, ' ', contact_lname) != ''," + " CONCAT('Contact: ', CONCAT(contact_fname, ' ', contact_lname), '<br>'), '')," +
						 * " IF(contact_mobile1 != '', CONCAT('Mobile: ', contact_mobile1, '<br>'), '')," +
						 * " IF(COALESCE(veh_reg_no, '') != '', CONCAT('Reg. No.: ', COALESCE(veh_reg_no, ''), '<br>'), '')," +
						 * " IF(call_customer_voice != '', CONCAT(call_customer_voice), '')) AS description," + " call_id AS activity_id, 'call' AS type, '' AS feedback" + " FROM " + compdb(comp_id) +
						 * " axela_service_call" + " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id" + " INNER JOIN " + compdb(comp_id) +
						 * " axela_customer_contact ON contact_id = call_contact_id" + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id" + " WHERE call_followup_time != ''"
						 * + " AND call_emp_id = " + exec + "" + " AND (call_followup_time >= " + startdate + "" + " OR call_followup_time < " + enddate + "" + " AND (call_followup_time >= " +
						 * startdate + "" + " AND call_followup_time < " + enddate + "" + " OR (call_followup_time < " + startdate + "" + " AND call_followup_time > " + enddate + ")" +
						 * " OR (call_followup_time > " + startdate + "" + " AND call_followup_time <= " + enddate + "))))" // // For Call Appointments // +
						 * " UNION ALL (SELECT call_time AS start_time," // + // " DATE_FORMAT(DATE_ADD(call_time, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time," // + //
						 * " calltype_name AS title, CONCAT('Contact: ', contact_fname, ' ' , contact_lname, '<br>'," // + //
						 * " IF(contact_mobile1 != '', CONCAT('Mobile: ', contact_mobile1, '<br>'), '')," // + //
						 * " IF(COALESCE(veh_reg_no, '') != '', CONCAT('Reg. No.: ', COALESCE(veh_reg_no, ''), '<br>'), '')," // + //
						 * " IF(call_customer_voice != '', CONCAT(call_customer_voice), '')) AS description," // + // " call_id AS activity_id, 'call' AS type, '' AS feedback" // +
						 * " FROM axela_service_call" // + // " INNER JOIN axela_service_call_type ON calltype_id = call_type_id" // + //
						 * " INNER JOIN axela_customer_contact ON contact_id = call_contact_id" // + // " LEFT JOIN axela_service_veh ON veh_id = call_veh_id" // + " WHERE 1= 1" // For Call
						 * Appointments + " UNION ALL (SELECT appt_appt_time AS start_time," + " DATE_FORMAT(DATE_ADD(appt_appt_time, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time," +
						 * " calltype_name AS title, CONCAT('Contact: ', contact_fname, ' ' , contact_lname, '<br>'," + " IF(contact_mobile1 != '', CONCAT('Mobile: ', contact_mobile1, '<br>'), '')," +
						 * " IF(COALESCE(veh_reg_no, '') != '', CONCAT('Reg. No.: ', COALESCE(veh_reg_no, ''), '<br>'), '')," +
						 * " IF(call_customer_voice != '', CONCAT(call_customer_voice), '')) AS description," + " call_id AS activity_id, 'call' AS type, '' AS feedback" + " FROM " + compdb(comp_id) +
						 * " axela_service_call" + " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id" + " INNER JOIN " + compdb(comp_id) +
						 * " axela_customer_contact ON contact_id = call_contact_id" + " INNER JOIN " + compdb(comp_id) + "axela_service_call_appt ON appt_call_id = call_id" + " LEFT JOIN " +
						 * compdb(comp_id) + " axela_service_veh ON veh_id = call_veh_id" + " WHERE 1= 1" // + " call_appt_time != ''" + " AND call_emp_id = " + exec + " " + " AND (appt_appt_time >= "
						 * + startdate + "" + " OR appt_appt_time < " + enddate + "" + " AND (appt_appt_time >= " + startdate + "" + " AND appt_appt_time < " + enddate + "" + " OR (appt_appt_time < "
						 * + startdate + "" + " AND appt_appt_time > " + enddate + ")" + " OR (appt_appt_time > " + startdate + "" + " AND appt_appt_time <= " + enddate + "))))"
						 */

						// For Pickup
						// + " UNION ALL (SELECT pickup_time AS start_time,"
						// +
						// " DATE_FORMAT(DATE_ADD(pickup_time, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time,"
						// +
						// " pickuptype_name AS title, pickup_notes AS description, pickup_id AS activity_id,"
						// + " 'pickup' AS type, '' AS feedback" + " FROM " +
						// compdb(comp_id) + "axela_service_pickup"
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_service_pickup_type " +
						// " ON pickuptype_id = pickup_pickuptype_id"
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_service_location AS locationname " +
						// " ON locationname.location_id = pickup_location_id"
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_customer_contact " +
						// " ON contact_id = pickup_contact_id"
						// + " WHERE pickup_emp_id = " + exec + ""
						// + " AND (pickup_time >= " + startdate + ""
						// + " OR pickup_time < " + enddate + ""
						// + " AND (pickup_time >= " + startdate + ""
						// + " AND pickup_time < " + enddate + ""
						// + " OR (pickup_time < " + startdate + ""
						// + " AND pickup_time > " + enddate + ")"
						// + " OR (pickup_time > " + startdate + ""
						// + " AND pickup_time <= " + enddate + "))))"
						//
						// // birthday
						+ " UNION ALL (SELECT " + startdate + " AS start_time,"
						+ " DATE_FORMAT(DATE_ADD(contact_dob, INTERVAL 1000 minute), '%Y%m%d%H%i%S') AS end_time,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS title,"
						+ " CONCAT('Status: Birthday<br>Type: ',contacttype_name) AS description, contact_id AS activity_id,"
						+ " 'birthday' AS type, '' AS feedback, contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer " + " ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title " + " ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact_type " + " ON contacttype_id = contact_contacttype_id"
						+ " WHERE 1=1 "
						+ " AND contact_dob!=''"
						+ " AND SUBSTR(contact_dob, 5, 4) = SUBSTR('" + startdate + "',5,4) "
						+ " AND customer_emp_id=" + exec;
				// if (emp_all_exe.equals("0"))
				// {
				// StrSql += ExeAccess.replace("emp_id", "customer_emp_id");
				// }
				StrSql += " ORDER BY start_time )";
				SOP("StrSql========activity======" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				StringBuilder Str = new StringBuilder();
				String suffix = "";
				int i = 0;
				int j = 0;
				if (crs.isBeforeFirst()) {
					for (i = 0; i <= 23; i++) {

						if (i >= 0 && i <= 11) {
							suffix = "am";
						} else {
							suffix = "pm";
						}
						Str.append("<tr>").append("<td class=\"active\" style=\"width: 50px;\">").append(i).append(suffix).append("</td>")
								.append("<td class=\"warning\">");
						// .append(" <div class=\"portlet solid red\">")
						// .append("<div class=\"portlet-body\">")
						// .append("<div class=\"scroller\">");
						// / Loop appts. for for each hour;
						crs.beforeFirst();
						while (crs.next()) {
							if (crs.getString("start_time").substring(8, 10).equals(doublenum(i))) {
								SOP("type=========================" + crs.getString("type"));
								if (crs.getString("type").equals("birthday")) {
									if (!crs.getString("activity_id").equals("0")) {
										Str.append("<div class=\"portlet solid red\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n");
										Str.append("<a href='callurlapp-enquiry-list.jsp?contact_id=").append(crs.getString("activity_id"))
												.append("'").append(">").append("<br>");
										Str.append("<div class=\"col-md-10 col-xs-10\">\n");
										// Str.append(crs.getString("start_time"));
										// Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-");
										// SOP("88888");
										// SOP("111-----" +
										// crs.getString("end_time"));
										// Str.append(strToLongDate(crs.getString("end_time")).substring(11))
										// .append("<br>");
										// SOP("99999");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n")
												.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									} else {
										Str.append("<a href='callurlapp-enquiry-list.jsp?contact_id=").append(crs.getString("activity_id")).append("'").append(">")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
									}

								} else if (crs.getString("type").equals("activity")) {
									if (!crs.getString("activity_id").equals("0")) {
										Str.append("<div class=\"portlet solid green\">\n").append("<div class=\"portlet-body\">\n")
												.append("<div class=\"scroller\">\n");
										Str.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-activity.jsp?enquiry_id=").append(crs.getString("activity_id"))
												.append("'").append(">").append("<br>")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n")
												.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									} else {
										Str.append("<a href='callurlapp-activity.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">");
										Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11)).append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
									}
								} else if (crs.getString("type").equals("opprfollowup")) {
									if (!crs.getString("activity_id").equals("0") && !crs.getString("feedback").equals("")) {
										Str.append("<div class=\"portlet solid blue\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n")
												.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">").append("<br>");
										Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									}
									else if (!crs.getString("activity_id").equals("0") && crs.getString("feedback").equals("")) {
										Str.append("<div class=\"portlet solid\" style=\"background-color:#E43A45\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n")
												.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">").append("<br>");
										Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									}
									else {
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">");
										Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
									}
								} else if (crs.getString("type").equals("psf")) {
									if (!crs.getString("activity_id").equals("0")) {
										Str.append("<div class=\"portlet solid Yellow\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n")
												.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">").append("<br>")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									} else {
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
									}
								} else if (crs.getString("type").equals("Demo")) {
									if (!crs.getString("activity_id").equals("0")) {
										Str.append("<div class=\"portlet solid purple\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n")
												.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-testdrive-list.jsp?testdrive_id=").append(crs.getString("activity_id")).append("'").append(">").append("<br>")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
										Str.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									} else {
										Str.append("<a href='callurlapp-testdrive-list.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>").append(crs.getString("description")).append("</a>");
									}
								} else if (crs.getString("type").equals("pickup")) {
									if (!crs.getString("activity_id").equals("0")) {
										Str.append("<div class=\"portlet solid pink\">\n").append("<div class=\"portlet-body\">\n").append("<div class=\"scroller\">\n")
												.append("<div class=\"col-md-10 col-xs-10\">\n");
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">").append("<br>")
												.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11))
												.append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>")
												.append("</div>\n")
												.append("<div class=\"col-md-2 col-xs-2\"  onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\" style=\"position: relative; top: 15px\">")
												.append("<img src=\"ifx/icon-call.png\">\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
										Str.append("</div>\n");
									} else {
										Str.append("<a href='callurlapp-enquiry-dash.jsp?enquiry_id=").append(crs.getString("activity_id")).append("'").append(">");
										Str.append(strToLongDate(crs.getString("start_time")).substring(11)).append("-")
												.append(strToLongDate(crs.getString("end_time")).substring(11)).append("<br>");
										Str.append(crs.getString("title") + "<br>");
										Str.append(crs.getString("description"));
										Str.append("</a>");
									}

								}
							}
						}
						Str.append("</td>");
						Str.append("</tr>");
					}
				} else {
					Str.append("<div class=\"container\" align=\"center\" style=\"color:red\"><b>").append("No Activities Found!")
							.append("</b></div>\n");
				}
				StrHTML = Str.toString();
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateExe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1' "
					+ ExeAccess;
			StrSql = StrSql + " ORDER BY emp_name";
			// SOP("StrSql-executive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), exec));
				Str.append(">").append(crs.getString("emp_name")).append(" (");
				Str.append(crs.getString("emp_ref_no")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
