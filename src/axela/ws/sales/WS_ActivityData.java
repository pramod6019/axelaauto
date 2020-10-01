package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_ActivityData extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String startdate = "";
	public String emp_uuid = "0";
	public String enddate = "";
	public String populate = "";
	public String startDatetime = "";
	public String endDatetime = "";
	public String ExeAccess = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject ActivityList(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("populate")) {
				populate = PadQuotes((String) input.get("populate"));
			}
			if (populate.equals("yes")) {
				PopulateExecutives();
			}

			if (!emp_id.equals("0")) {
				if (!input.isNull("startdate")) {
					startdate = PadQuotes((String) input.get("startdate"));
					startdate = ConvertShortDateToStr(startdate);
				}
				if (!input.isNull("enddate")) {
					enddate = PadQuotes((String) input.get("enddate"));
					enddate = strToShortDate(ToShortDate(AddHoursDate(
							StringToDate(ConvertShortDateToStr(enddate)), 1, 0,
							0)));
					enddate = ConvertShortDateToStr(enddate);
				}
				try {
					// StrSql = "(SELECT "
					// + " activity_start_datetime as  start_time,"
					// + " date_format(date_add(activity_end_datetime, interval 30 minute), '%Y%m%d%H%i%s') as end_time, "
					// + " 'Activity' AS title, CONCAT("
					// + " IF(activity_desc != '', CONCAT('Description: ' , activity_desc, '<br>'), ''),"
					// + " IF(priority_name != '', CONCAT('Priority: ', priority_name, '<br>'), ''),"
					// + " IF(activity_contact_person != '',"
					// + " CONCAT('Contact: ', title_desc, ' ', activity_contact_person, '<br>'), ''),"
					// + " IF(activity_phone1 != '', CONCAT(activity_phone1, '<br>'), ''),"
					// + " IF(activity_phone2 != '', CONCAT(activity_phone2, '<br>'), ''),"
					// + " IF(activity_feedback != '', CONCAT('Feedback: ', activity_feedback, '<br>'), ''),"
					// + " IF(activity_remarks != '', CONCAT('Remarks: ', activity_remarks, '<br>'), '')) AS description,"
					// + " substring(activity_start_datetime ,1,4) AS 'startyear',"
					// + " substring(activity_start_datetime ,5,2) AS 'startmonth',"
					// + " substring(activity_start_datetime ,7,2) AS 'startdate',"
					// + " substring(activity_start_datetime ,9,2) AS 'starthour',"
					// + " substring(activity_start_datetime ,11,2) AS 'startmin',"
					// + " substring(activity_end_datetime ,9,2) AS 'endhour',"
					// + " substring(activity_end_datetime ,11,2) AS 'endmin',"
					// + " '' AS contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, activity_id, "
					// + compdb(comp_id) + "axela_activity_type.type_name as type,"
					// + " '' AS feedback, 0 AS status"
					// + " FROM " + compdb(comp_id)+ "axela_activity"
					// + " INNER JOIN " + compdb(comp_id) + "axela_activity_type ON " + compdb(comp_id) + "axela_activity.activity_type_id = " + compdb(comp_id)+ "axela_activity_type.type_id"
					// + " LEFT JOIN "+ compdb(comp_id)+ "axela_activity_status ON "+ compdb(comp_id)+ "axela_activity.activity_status_id = " + compdb(comp_id) + "axela_activity_status.status_id"
					// + " LEFT JOIN "+ compdb(comp_id)+ "axela_activity_priority ON priority_id = activity_priority_id"
					// + " LEFT JOIN "+ compdb(comp_id)+ "axela_customer_contact ON activity_contact_id = contact_id"
					// + " INNER JOIN " + compdb(comp_id)+ "axela_title ON title_id = contact_title_id "
					// + " INNER JOIN " + compdb(comp_id)+ "axela_emp emp ON emp_id = activity_emp_id"
					// + " WHERE 1=1"
					// + " and substring(activity_start_datetime ,1,8) = substring("+ startdate + " ,1,8)"
					// + " AND emp.emp_id = " + emp_id
					// + " AND IF (emp.emp_exeaccess != '', FIND_IN_SET(activity_emp_id, emp.emp_exeaccess), 1=1)"
					// + ")"
					// + " UNION ALL ("
					// ENquiry Follow up
					StrSql = " ( SELECT  "
							+ " followup_followup_time as  start_time,"
							+ " date_format(date_add(followup_followup_time, interval 30 minute), '%Y%m%d%H%i%s') as end_time, "
							+ " 'enquiryfollowup' AS title,"
							+ " CONCAT(IF(followup_desc != '',"
							+ " CONCAT('Feedback: ', followup_desc, '<br>'), ''),"
							+ " CONCAT('Contact: ', title_desc, ' ', contact_fname, ' ', contact_lname)"
							+ " ) AS description,"
							+ " substring(followup_followup_time ,1,4) AS 'startyear',"
							+ " substring(followup_followup_time ,5,2) AS 'startmonth',"
							+ " substring(followup_followup_time ,7,2) AS 'startdate',"
							+ " substring(followup_followup_time ,9,2) AS 'starthour',"
							+ " substring(followup_followup_time ,11,2) AS 'startmin',"
							+ " date_format(date_add(followup_followup_time,INTERVAL 30 MINUTE),'%H') AS endhour,"
							+ " date_format(date_add(followup_followup_time,INTERVAL 30 MINUTE),'%i') AS endmin,"
							+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, enquiry_id AS activity_id,"
							+ " followuptype_name AS type, followup_desc AS feedback, 0 AS status"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp  ON emp_id = followup_emp_id"
							+ " WHERE 1=1"
							+ " and substring(followup_followup_time ,1,8) = substring(" + startdate + " ,1,8)"
							+ " AND emp_id = " + emp_id
							+ " AND IF (emp_exeaccess != '', FIND_IN_SET(followup_emp_id, emp_exeaccess), 1=1)"
							+ ")"
							// CRM Follow-up
							+ " UNION ALL (SELECT "
							+ " crm_followup_time as  start_time,"
							+ " date_format(date_add(crm_followup_time, interval 30 minute), '%Y%m%d%H%i%s') as end_time, "
							+ " 'CRM Followup' AS title, "
							+ " CONCAT(IF(crm_desc != '',"
							+ " CONCAT('Feedback: ', crm_desc, '<br>'), ''),"
							+ " CONCAT('Contact: ', title_desc, ' ', contact_fname, ' ', contact_lname)"
							+ " ) AS description,"
							+ " substring(crm_followup_time ,1,4) AS 'startyear',"
							+ " substring(crm_followup_time ,5,2) AS 'startmonth',"
							+ " substring(crm_followup_time ,7,2) AS 'startdate',"
							+ " substring(crm_followup_time ,9,2) AS 'starthour',"
							+ " substring(crm_followup_time ,11,2) AS 'startmin',"
							+ " date_format(date_add(crm_followup_time,INTERVAL 30 MINUTE),'%H') AS endhour,"
							+ " date_format(date_add(crm_followup_time,INTERVAL 30 MINUTE),'%i') AS endmin,"
							+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, crm_enquiry_id AS activity_id,"
							+ " crmfeedbacktype_name AS type, crm_desc AS feedback, 0 AS status"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = crm_emp_id"
							+ " INNER JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
							+ " WHERE 1=1"
							+ " and substring(crm_followup_time ,1,8) = substring(" + startdate + " ,1,8)"
							+ " AND emp_id = " + emp_id
							+ " AND IF (emp_exeaccess != '', FIND_IN_SET(crm_emp_id, emp_exeaccess), 1=1)"
							+ ")"
							// Test Drive
							+ " UNION ALL (SELECT "
							+ " testdrive_time_from as  start_time,"
							+ " testdrive_time_to as end_time, "
							+ " 'Test Drive' AS title, CONCAT('Contact: ', title_desc, ' ', contact_fname, ' ', contact_lname,"
							+ " '<br>Location: ',location_name,"
							+ " IF(testdrive_fb_taken = 1, '<br>Status: Taken', '<br>Status: Not Taken')) AS description, "
							+ " substring(testdrive_time_from ,1,4) AS 'startyear',"
							+ " substring(testdrive_time_from ,5,2) AS 'startmonth',"
							+ " substring(testdrive_time_from ,7,2) AS 'startdate',"
							+ " substring(testdrive_time_from ,9,2) AS 'starthour',"
							+ " substring(testdrive_time_from ,11,2) AS 'startmin',"
							+ " substring(testdrive_time_to ,9,2) AS 'endhour',"
							+ " substring(testdrive_time_to ,11,2) AS 'endmin',"
							+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, "
							+ " testdrive_id AS activity_id, IF(testdrive_type=1,'Main Test Drive','Alternate Test Drive') AS type, if(testdrive_fb_taken = 0, '', 'taken') AS feedback,"
							+ " testdrive_fb_taken AS status"
							+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id = testdrive_location_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp  ON emp_id = testdrive_emp_id"
							+ " WHERE 1=1"
							+ " and substring(testdrive_time_from ,1,8) = substring(" + startdate + " ,1,8)"
							+ " AND emp_id = " + emp_id
							+ " AND IF (emp_exeaccess != '', FIND_IN_SET(testdrive_emp_id, emp_exeaccess), 1=1)"
							+ ")" + " ORDER BY start_time";
					SOP("list activity  query === " + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							// SOP("start_time=====" + crs.getString("start_time"));
							// SOP("end time=====" + crs.getString("end_time"));
							// SOP("starthour=====" + crs.getString("starthour"));
							// SOP("endhour=====" + crs.getString("endhour"));
							// startDatetime = crs.getString("start_time");
							// Activity
							// if (crs.getString("title").equals("activity")) {
							// map.put("actiontype", "3");
							// map.put("type", crs.getString("type"));
							// map.put("title", crs.getString("title"));
							// map.put("start", crs.getString("start_time"));
							// map.put("end", crs.getString("end_time"));
							// map.put("startyear", crs.getString("startyear"));
							// map.put("startmonth",
							// crs.getString("startmonth"));
							// map.put("startdate", crs.getString("startdate"));
							// map.put("starthour", crs.getString("starthour"));
							// map.put("startmin", crs.getString("startmin"));
							// map.put("endhour", crs.getString("endhour"));
							// map.put("endmin", crs.getString("endmin"));
							// map.put("description",unescapehtml(crs.getString("description")).replace("\"", ""));
							// map.put("feedback",unescapehtml(crs.getString("feedback")).replace("\"", ""));
							// map.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
							// map.put("contact_mobile2", unescapehtml(crs.getString("contact_mobile2")));
							// map.put("contact_phone1", unescapehtml(crs.getString("contact_phone1")));
							// map.put("contact_phone2", unescapehtml(crs.getString("contact_phone2")));
							// map.put("activity_id",crs.getString("activity_id"));
							// }
							// Enquiry Follow-up
							if (crs.getString("title").equals("enquiryfollowup")) {
								map.put("actiontype", "1");
								map.put("type", crs.getString("type"));
								// if (!crs.getString("title").equals("")) {
								map.put("title", "Enquiry Follow-up");
								// }
								map.put("start", crs.getString("start_time"));
								map.put("end", crs.getString("end_time"));
								map.put("startyear", crs.getString("startyear"));
								map.put("startmonth",
										crs.getString("startmonth"));
								map.put("startdate", crs.getString("startdate"));
								map.put("starthour", crs.getString("starthour"));
								map.put("startmin", crs.getString("startmin"));
								map.put("endhour", crs.getString("endhour"));
								map.put("endmin", crs.getString("endmin"));
								map.put("description", unescapehtml(crs.getString("description")));
								map.put("feedback", unescapehtml(crs.getString("feedback")));
								map.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
								map.put("contact_mobile2", unescapehtml(crs.getString("contact_mobile2")));
								map.put("contact_phone1", unescapehtml(crs.getString("contact_phone1")));
								map.put("contact_phone2", unescapehtml(crs.getString("contact_phone2")));
								map.put("activity_id", crs.getString("activity_id"));
							}

							// CRM Followup
							// SOP("title====" + crs.getString("title"));
							if (crs.getString("title").equals("CRM Followup")) {
								map.put("actiontype", "3");
								map.put("type", crs.getString("type"));
								map.put("title", "CRM Follow-up");
								map.put("start", crs.getString("start_time"));
								map.put("end", crs.getString("end_time"));
								map.put("startyear", crs.getString("startyear"));
								map.put("startmonth", crs.getString("startmonth"));
								map.put("startdate", crs.getString("startdate"));
								map.put("starthour", crs.getString("starthour"));
								map.put("startmin", crs.getString("startmin"));
								map.put("endhour", crs.getString("endhour"));
								map.put("endmin", crs.getString("endmin"));
								map.put("description", unescapehtml(crs.getString("description")));
								map.put("feedback", unescapehtml(crs.getString("feedback")));
								map.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
								map.put("contact_mobile2", unescapehtml(crs.getString("contact_mobile2")));
								map.put("contact_phone1", unescapehtml(crs.getString("contact_phone1")));
								map.put("contact_phone2", unescapehtml(crs.getString("contact_phone2")));
								map.put("activity_id", crs.getString("activity_id"));
							}

							// Test Drive
							if (crs.getString("title").equals("Test Drive")) {
								map.put("actiontype", "2");
								map.put("type", crs.getString("type"));
								// if (!crs.getString("title").equals("")) {
								map.put("title", crs.getString("title"));
								// }
								map.put("start", crs.getString("start_time"));
								map.put("end", crs.getString("end_time"));
								map.put("startyear", crs.getString("startyear"));
								map.put("startmonth", crs.getString("startmonth"));
								map.put("startdate", crs.getString("startdate"));
								map.put("starthour", crs.getString("starthour"));
								map.put("startmin", crs.getString("startmin"));
								map.put("endhour", crs.getString("endhour"));
								map.put("endmin", crs.getString("endmin"));
								map.put("description", crs.getString("description"));
								map.put("feedback", unescapehtml(crs.getString("feedback")));
								map.put("activity_id", crs.getString("activity_id"));
								map.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
								map.put("contact_mobile2", unescapehtml(crs.getString("contact_mobile2")));
								map.put("contact_phone1", unescapehtml(crs.getString("contact_phone1")));
								map.put("contact_phone2", unescapehtml(crs.getString("contact_phone2")));
							}
							map.put("msg", "");
							list.add(gson.toJson(map)); // Converting String to
														// Json
						}
						map.clear();
						output.put("listdata", list);
						list.clear();
					} else {
						output.put("msg", "No Records Found!");
					}
					if (AppRun().equals("0")) {
						SOP("output = " + output);
					}
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto =="
							+ this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + ex);
					return output;
				}
			}
		}
		return output;
	}

	public JSONObject PopulateExecutives() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT empexe.emp_id, empexe.emp_name, empexe.emp_all_exe"
					+ " FROM " + compdb(comp_id) + "axela_emp empexe,"
					+ compdb(comp_id) + "axela_emp emp"
					+ " WHERE 1 = 1 "
					+ " AND emp.emp_id = " + emp_id
					+ " AND if(emp.emp_all_exe='0',(empexe.emp_id in (SELECT empexe_id from "
					+ compdb(comp_id) + "axela_emp_exe "
					+ " WHERE empexe_emp_id =" + emp_id
					+ ") OR empexe.emp_id =" + emp_id + "),1=1)"
					+ " AND empexe.emp_sales = '1' AND empexe.emp_active = '1'"
					+ " GROUP BY emp_id" + " ORDER BY emp_name";
			// SOP("PopulateSalesExecutives SQL---" + StrSql);
			crs = processQuery(StrSql, 0);
			// map.put("emp_id", "0");
			// map.put("emp_name", "Select");
			// list.add(gson.toJson(map));
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatesalesexecutive", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		} finally {

		}
		return output;
	}
}
