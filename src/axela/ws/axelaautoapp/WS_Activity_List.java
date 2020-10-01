/* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Activity_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String activitydate = "";
	public String month = "";
	public String StrSearch = "";
	public String emp_id = "0";
	public String activity_emp_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String pagecurrent = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String populatedata = "";
	public CachedRowSet crs = null;
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject ActivityList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Activity_List===" + input);
		}
		HttpSession session = request.getSession(true);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		if (ReturnPerm(comp_id, "emp_activity_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		if (!input.isNull("activitydate")) {
			activitydate = ConvertShortDateToStr((PadQuotes((String) input.get("activitydate"))));
		}
		if (!input.isNull("month")) {
			month = PadQuotes((String) input.get("month"));
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (!input.isNull("activity_emp_id")) {
			activity_emp_id = CNumeric(PadQuotes((String) input.get("activity_emp_id")));
		}
		if (activity_emp_id.equals("0")) {
			activity_emp_id = emp_id;
		}
		if (!input.isNull("populatedata")) {
			populatedata = PadQuotes((String) input.get("populatedata"));
		}
		if (populatedata.equals("yes")) {
			PopulateExecutive();
		}

		if (!emp_id.equals("0")) {
			try {
				StrSql = "(SELECT activity_start_datetime AS start_time," + " DATE_FORMAT(DATE_ADD(activity_end_datetime, INTERVAL 30 minute), '%Y%m%d%H%i%S') AS end_time,"
						+ " activity_title AS title,"
						+ " CONCAT(IF(activity_desc != '', CONCAT(activity_desc, '<br>'), ''),"
						+ " IF(type_name != '', CONCAT('Type: ', type_name, '<br>'), ''),"
						+ " IF(priority_name != '', CONCAT('Priority: ', priority_name, '<br>'), ''),"
						+ " IF(activity_contact_person != '', CONCAT('Contact: ', activity_contact_person, '<br>'), ''),"
						+ " IF(activity_phone1 != '', CONCAT(activity_phone1, '<br>'), ''),"
						+ " IF(activity_phone2 != '', CONCAT(activity_phone2, '<br>'), ''),"
						+ " IF(activity_feedback != '', CONCAT('Feedback: ', activity_feedback, '<br>'), ''),"
						+ " IF(activity_remarks != '', CONCAT('Remarks: ', activity_remarks), '')) AS description,"
						+ " activity_id, 'activity' AS type, '' AS feedback,"
						+ " COALESCE(contact_mobile1,'') AS contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_activity"
						+ " INNER JOIN " + compdb(comp_id) + "axela_activity_type " + " ON axela_activity.activity_type_id = axela_activity_type.type_id"
						// " LEFT JOIN axela_activity_status ON axela_activity.activity_status_id = axela_activity_status.status_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_activity_priority" + " ON priority_id = activity_priority_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON activity_contact_id = contact_id"
						+ " WHERE activity_emp_id = " + activity_emp_id + "";
				if (month.equals("yes")) {
					StrSql += " AND SUBSTR(activity_start_datetime, 1, 6) >= " + activitydate.substring(0, 6)
							+ " AND SUBSTR(activity_start_datetime, 1, 6) <= " + activitydate.substring(0, 6) + ")";
				} else {
					StrSql += " AND SUBSTR(activity_start_datetime, 1, 8) >= " + activitydate.substring(0, 8)
							+ " AND SUBSTR(activity_start_datetime, 1, 8) <= " + activitydate.substring(0, 8) + ")";
				}

				// For Oppr Followup
				StrSql += " UNION ALL (SELECT followup_followup_time AS start_time,"
						+ " DATE_FORMAT(DATE_ADD(followup_followup_time, INTERVAL 2 hour), '%Y%m%d%H%i%S') AS end_time,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS title,"
						+ " CONCAT(IF(followuptype_name != '', CONCAT('Type: ', followuptype_name, ' - Enquiry Follow-up', '<br>'), ''),"
						+ " IF(followup_desc != '', CONCAT('Feedback: ', followup_desc, '<br>'), ''),"
						+ " IF(CONCAT(contact_fname, ' ', contact_lname) != '',"
						+ " CONCAT('Contact: ', CONCAT(contact_fname, ' ', contact_lname, '<br>')), ''),"
						+ " IF(contact_mobile1 != '', CONCAT('Mobile: ', contact_mobile1), '')) AS description,"
						+ " enquiry_id AS activity_id, 'enquiryfollowup' AS type, followup_desc AS feedback,contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry " + " ON enquiry_id = followup_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type " + " ON followuptype_id = followup_followuptype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact " + " ON contact_id = enquiry_contact_id"
						+ " WHERE followup_emp_id = " + activity_emp_id + "";
				if (month.equals("yes")) {
					StrSql += " AND SUBSTR(followup_followup_time, 1, 6) = " + activitydate.substring(0, 6) + ")";
				} else {
					StrSql += " AND SUBSTR(followup_followup_time, 1, 8) = " + activitydate.substring(0, 8) + ")";
				}
				// test drive
				StrSql += " UNION ALL (SELECT testdrive_time_from AS start_time, testdrive_time_to AS end_time,"
						+ " 'Test Drive' AS title, CONCAT('Contact: ', contact_fname, ' ', contact_lname,"
						+ " '<br>Mobile: ', contact_mobile1, '<br>Location: ', location_name,"
						+ " IF(testdrive_fb_taken = 1, '<br>Status: Taken', IF(testdrive_fb_taken = 2, '<br>Status: Not Taken', ''))) AS description,"
						+ " testdrive_id AS activity_id, 'testdrive' AS type,"
						+ " IF(testdrive_fb_taken = 0, '', 'Feedback given') AS feedback, contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry " + " ON enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location " + " ON location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact " + " ON contact_id = enquiry_contact_id"
						+ " WHERE testdrive_emp_id = " + activity_emp_id + "";
				if (month.equals("yes")) {
					StrSql += " AND SUBSTR(testdrive_time_from, 1,6) >= " + activitydate.substring(0, 6)
							+ " AND SUBSTR(testdrive_time_to, 1, 6) <= " + activitydate.substring(0, 6) + ")";
				} else {
					StrSql += " AND SUBSTR(testdrive_time_from, 1, 8) >= " + activitydate.substring(0, 8)
							+ " AND SUBSTR(testdrive_time_to, 1, 8) <= " + activitydate.substring(0, 8) + ")";
				}
				StrSql += " ORDER BY start_time ";
				// + LimitRecords(TotalRecords, pagecurrent);
				// SOP("StrSql====================" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("start_time", strToLongDate(crs.getString("start_time")));
						map.put("end_time", strToLongDate(crs.getString("end_time")));
						map.put("title", crs.getString("title"));
						map.put("description", crs.getString("description"));
						map.put("activity_id", crs.getString("activity_id"));
						map.put("type", crs.getString("type"));
						map.put("feedback", crs.getString("feedback"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						list.add(gson.toJson(map));
					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output =========WS_Activity_list======== " + output.toString(1));
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}

	public void PopulateExecutive() {
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1' "
					+ ExeAccess;
			StrSql = StrSql + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populateexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
