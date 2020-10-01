// Ved (11 Feb, 29 March 2013)
package axela.portal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Activity_Events extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String comp_id = "0";
	public long start = 0;
	public long end = 0;
	public Date startdatevalue;
	public Date enddatevalue;
	public String startdate = "";
	public String enddate = "";
	public String startyear = "";
	public String startmonth = "";
	public String startday = "";
	public String starthour = "";
	public String startmin = "";
	public String startsec = "";
	public String startDatetime = "";
	public String endDatetime = "";
	public String endyear = "";
	public String endmonth = "";
	public String endday = "";
	public String endhour = "";
	public String endmin = "";
	public String endsec = "";
	public String activity_emp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			try {
				CheckPerm(comp_id, "emp_activity_access", request, response);
				activity_emp_id = CNumeric(GetSession("activity_emp_id", request) + "");
				start = Long.parseLong(CNumeric(PadQuotes(request.getParameter("start"))));
				end = Long.parseLong(CNumeric(PadQuotes(request.getParameter("end"))));
				if (start > 0 && end > 0) {
					startdatevalue = new java.util.Date((long) start * 1000);
					enddatevalue = new java.util.Date((long) end * 1000);
					startdate = ToLongDate(startdatevalue);
					enddate = ToLongDate(enddatevalue);
					// SOP("s-------"+startdate);
					// SOP("e-------"+enddate);
				}
				StrSql = "(SELECT activity_start_datetime AS start_time,"
						+ " DATE_FORMAT(DATE_ADD(activity_end_datetime, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time,"
						+ " activity_title AS title, CONCAT(IF(type_name != '', CONCAT('Type: ', type_name, '<br>'), ''),"
						+ " IF(activity_desc != '', CONCAT('Description: ' , activity_desc, '<br>'), ''),"
						+ " IF(priority_name != '', CONCAT('Priority: ', priority_name, '<br>'), ''),"
						+ " IF(activity_contact_person != '', CONCAT('Contact: ', activity_contact_person, '<br>'), ''),"
						+ " IF(activity_phone1 != '', CONCAT(activity_phone1, '<br>'), ''),"
						+ " IF(activity_phone2 != '', CONCAT(activity_phone2, '<br>'), ''),"
						+ " IF(activity_feedback != '', CONCAT('Feedback: ', activity_feedback, '<br>'), ''),"
						+ " IF(activity_remarks != '', CONCAT('Remarks: ', activity_remarks, '<br>'), '')) AS description,"
						+ " activity_id, 'activity' as type, '' AS feedback, 0 AS status"
						+ " FROM " + compdb(comp_id) + "axela_activity"
						+ " INNER JOIN " + compdb(comp_id) + "axela_activity_type ON " + compdb(comp_id) + "axela_activity.activity_type_id = " + compdb(comp_id) + "axela_activity_type.type_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_activity_status ON " + compdb(comp_id) + "axela_activity.activity_status_id = " + compdb(comp_id)
						+ "axela_activity_status.status_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_activity_priority ON priority_id = activity_priority_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON activity_contact_id = contact_id"
						+ " WHERE activity_emp_id = " + activity_emp_id + ""
						+ " AND (activity_start_datetime >= " + startdate + ""
						+ " AND activity_start_datetime < " + enddate + ""
						+ " OR (activity_start_datetime < " + startdate + ""
						+ " AND activity_end_datetime > " + enddate + ")"
						+ " OR (activity_end_datetime > " + startdate + ""
						+ " AND activity_end_datetime <= " + enddate + ")))"
						// Enquiry-followup
						+ " UNION ALL (SELECT followup_followup_time AS start_time,"
						+ " DATE_FORMAT(DATE_ADD(followup_followup_time, INTERVAL 2 hour), '%Y%m%d%H%i%S') AS end_time,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS title,"
						+ " CONCAT('Type: ', followuptype_name, ' - Enquiry Follow-up', '<br>', IF(followup_desc != '',"
						+ " CONCAT('Feedback: ', followup_desc, '<br>'), ''), CONCAT('Contact: ', contact_fname, ' ', contact_lname, '<br>'),"
						+ " 'Mobile: ', contact_mobile1) AS description, enquiry_id AS activity_id,"
						+ " 'enquiryfollowup' AS type, followup_desc AS feedback, 0 AS status"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " WHERE followup_emp_id = " + activity_emp_id + ""
						+ " AND (followup_followup_time >= " + startdate + ""
						+ " OR followup_followup_time < " + enddate + ""
						+ " AND (followup_followup_time >= " + startdate + ""
						+ " AND followup_followup_time < " + enddate + ""
						+ " OR (followup_followup_time < " + startdate + ""
						+ " AND followup_followup_time > " + enddate + ")"
						+ " OR (followup_followup_time > " + startdate + ""
						+ " AND followup_followup_time <= " + enddate + "))))"

						// For JCPSF FollowUp
						+ " UNION ALL(SELECT jcpsf_nextfollowuptime start_time,"
						+ " DATE_FORMAT(DATE_ADD(jcpsf_nextfollowuptime, INTERVAL 2 hour), '%Y%m%d%H%i%S') AS end_time,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS title,"
						+ " CONCAT('Type: ', psffeedbacktype_name, ' - JCPSF Next Follow-up', '<br>', IF(jcpsf_desc !='',"
						+ " CONCAT('Feedback:',jcpsf_desc, '<br>'), ''),CONCAT('Contact: ',contact_fname, ' ', contact_lname),  '<br>',"
						+ " 'Mobile: ', contact_mobile1) AS description,"
						+ " jcpsf_id AS activity_id, 'psffollowup' AS type, jcpsf_desc AS feedback, 0 AS status"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
						+ " INNER JOIN axela_service_psf_feedbacktype ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
						+ " WHERE jcpsf_emp_id = " + activity_emp_id + ""
						+ " AND (jcpsf_nextfollowuptime >= " + startdate + ""
						+ " OR jcpsf_nextfollowuptime < " + enddate + ""
						+ " AND (jcpsf_nextfollowuptime >= " + startdate + ""
						+ " AND jcpsf_nextfollowuptime < " + enddate + ""
						+ " OR (jcpsf_nextfollowuptime < " + startdate + ""
						+ " AND jcpsf_nextfollowuptime > " + enddate + ")"
						+ " OR (jcpsf_nextfollowuptime > " + startdate + ""
						+ " AND jcpsf_nextfollowuptime <= " + enddate + "))))"

						// Test Drive
						+ " UNION ALL (SELECT testdrive_time_from AS start_time, testdrive_time_to AS end_time,"
						+ " 'Test Drive' AS title, CONCAT('Contact: ', contact_fname, ' ', contact_lname,"
						+ " '<br>Mobile: ', contact_mobile1, '<br>Location: ',location_name,"
						+ " IF(testdrive_fb_taken = 1, '<br>Status: Taken', '<br>Status: Not Taken')) AS description,"
						+ " testdrive_id AS activity_id, 'Test Drive' AS type, if(testdrive_fb_taken = 0, '', 'taken') AS feedback,"
						+ " testdrive_fb_taken AS status"
						+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
						+ " WHERE testdrive_emp_id = " + activity_emp_id + ""
						+ " AND (testdrive_time_from >= " + startdate + ""
						+ " OR testdrive_time_to < " + enddate + ""
						+ " AND (testdrive_time_from >= " + startdate + ""
						+ " AND testdrive_time_to < " + enddate + ")))"
						+ " ORDER BY start_time";
				// SOP("StrSql-----------" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);

				JsonObject obj = new JsonObject();
				ArrayList<String> getList = new ArrayList<String>();
				Gson gson = new Gson();
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						// SOP("type====" + crs.getString("type"));
						startDatetime = crs.getString("start_time");
						if (!startDatetime.equals("")) {
							startDatetime = crs.getString("start_time");
							startyear = startDatetime.substring(0, 4);
							startmonth = startDatetime.substring(4, 6);
							startday = startDatetime.substring(6, 8);
							starthour = startDatetime.substring(8, 10);
							startmin = startDatetime.substring(10, 12);
							startsec = startDatetime.substring(12, 14);
						}
						// Activity
						if (crs.getString("type").equals("activity")) {
							if (!crs.getString("end_time").equals("")) {
								endDatetime = crs.getString("end_time");
								endyear = endDatetime.substring(0, 4);
								endmonth = endDatetime.substring(4, 6);
								endday = endDatetime.substring(6, 8);
								endhour = endDatetime.substring(8, 10);
								endmin = endDatetime.substring(10, 12);
								endsec = endDatetime.substring(12, 14);
							}
							obj.addProperty("title", crs.getString("title"));
							obj.addProperty("start", startyear + "-" + startmonth + "-" + startday + "T" + starthour + ":" + startmin + ":" + startsec);
							obj.addProperty("end", endyear + "-" + endmonth + "-" + endday + "T" + endhour + ":" + endmin + ":" + endsec);
							if (Long.parseLong(crs.getString("end_time"))
									- Long.parseLong(crs.getString("start_time")) > (3600 * 24)) {
								obj.addProperty("allDay", true);
							} else {
								obj.addProperty("allDay", false);
							}
							obj.addProperty("description", crs.getString("description"));
							obj.addProperty("color", "blue");
							obj.addProperty("textColor", "white");
							obj.addProperty("url", "../portal/activity-list.jsp?update=yes&contact_id=0&activity_id=" + crs.getString("activity_id"));
						}
						// Enquiry Follow-up
						if (crs.getString("type").equals("enquiryfollowup")) {
							if (!crs.getString("end_time").equals("")) {
								endDatetime = crs.getString("end_time");
								endyear = endDatetime.substring(0, 4);
								endmonth = endDatetime.substring(4, 6);
								endday = endDatetime.substring(6, 8);
								endhour = endDatetime.substring(8, 10);
								endmin = endDatetime.substring(10, 12);
								endsec = endDatetime.substring(12, 14);
							}
							if (!crs.getString("title").equals("")) {
								obj.addProperty("title", crs.getString("title"));
							}
							obj.addProperty("start", startyear + "-" + startmonth + "-" + startday + "T" + starthour + ":" + startmin + ":" + startsec);
							obj.addProperty("end", endyear + "-" + endmonth + "-" + endday + "T" + endhour + ":" + endmin + ":" + endsec);
							obj.addProperty("allDay", false);
							obj.addProperty("description", crs.getString("description"));
							if (!crs.getString("feedback").equals("")) {
								obj.addProperty("color", "#CACAFF");
								obj.addProperty("textColor", "black");
							} else {
								obj.addProperty("color", "#FFCEFF");
								obj.addProperty("textColor", "black");
							}
							obj.addProperty("url", "../sales/enquiry-dash.jsp?enquiry_id=" + crs.getString("activity_id") + "#tabs-2");
						}

						// For JCPSF FollowUp
						if (crs.getString("type").equals("psffollowup")) {
							if (!crs.getString("end_time").equals("")) {
								endDatetime = crs.getString("end_time");
								endyear = endDatetime.substring(0, 4);
								endmonth = endDatetime.substring(4, 6);
								endday = endDatetime.substring(6, 8);
								endhour = endDatetime.substring(8, 10);
								endmin = endDatetime.substring(10, 12);
								endsec = endDatetime.substring(12, 14);
							}
							if (!crs.getString("title").equals("")) {
								obj.addProperty("title", crs.getString("title"));
							}
							obj.addProperty("start", startyear + "-" + startmonth + "-" + startday + "T" + starthour + ":" + startmin + ":" + startsec);
							obj.addProperty("end", endyear + "-" + endmonth + "-" + endday + "T" + endhour + ":" + endmin + ":" + endsec);
							obj.addProperty("allDay", false);
							obj.addProperty("description", crs.getString("description"));
							if (!crs.getString("feedback").equals("")) {
								obj.addProperty("color", "#CACAFF");
								obj.addProperty("textColor", "black");
							} else {
								obj.addProperty("color", "#FFCEFF");
								obj.addProperty("textColor", "black");
							}
							obj.addProperty("url", "../service/jobcard-psf-update.jsp?jcpsf_id=" + crs.getString("activity_id"));
						}

						// Test Drive
						if (crs.getString("type").equals("Test Drive")) {
							if (!startDatetime.equals("")) {
								endDatetime = crs.getString("end_time");
								endyear = endDatetime.substring(0, 4);
								endmonth = endDatetime.substring(4, 6);
								endday = endDatetime.substring(6, 8);
								endhour = endDatetime.substring(8, 10);
								endmin = endDatetime.substring(10, 12);
								endsec = endDatetime.substring(12, 14);
							}
							if (!crs.getString("title").equals("")) {
								obj.addProperty("title", crs.getString("title"));
							}
							obj.addProperty("start", startyear + "-" + startmonth + "-" + startday + "T" + starthour + ":" + startmin + ":" + startsec);
							obj.addProperty("end", endyear + "-" + endmonth + "-" + endday + "T" + endhour + ":" + endmin + ":" + endsec);
							obj.addProperty("allDay", false);
							obj.addProperty("description", crs.getString("description"));
							if (!crs.getString("status").equals("0")) {
								obj.addProperty("color", "#FF7F50");
								obj.addProperty("textColor", "black");
							} else {
								obj.addProperty("color", "#FF8080");
								obj.addProperty("textColor", "black");
							}
							obj.addProperty("url", "../sales/testdrive-list.jsp?testdrive_id=" + crs.getString("activity_id"));
						}
						getList.add(gson.toJson(obj));
					}
					StrHTML = getList.toString();
				} else {
					StrHTML = "[]";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
