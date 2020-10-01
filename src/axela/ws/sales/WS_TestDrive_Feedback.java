package axela.ws.sales;
//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_TestDrive_Feedback extends ConnectWS {

	public String update = "";
	public String updateB = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String testdrive_id = "";
	public String testdrive_fb_entry_id = "";
	public String testdrive_fb_notes = "";
	public String testdrive_fb_taken = "";
	public String testdrive_time = "";
	public String testdrive_fb_status_id = "0";
	public String testdrive_fb_status_comments = "";
	public String executive_name = "";
	public String contact_id = "";
	public String contact_name = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String config_sms_enable = "";
	// ws
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject TestDrive_Feedback(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}

		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}
		if (!input.isNull("testdrive_id")) {
			testdrive_id = CNumeric(PadQuotes((String) input.get("testdrive_id")));
		}
		if (!input.isNull("update")) {
			update = PadQuotes((String) input.get("update"));
		}
		if (!input.isNull("updateB")) {
			updateB = PadQuotes((String) input.get("updateB"));
		}
		if (!emp_id.equals("0") && !testdrive_id.equals("0")) {
			if ("yes".equals(update) && !updateB.equals("yes")) {
				PopulateTestDriveTaken();
				PopulateStatus();
				PopulateFields();
			}
			if (updateB.equals("yes")) {
				GetValues(input);
				CheckForm();
				if (msg.equals("")) {
					UpdateFields();
				} else {
					output.put("msg", "Error!" + msg);
				}
			}
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	protected void GetValues(JSONObject input) throws JSONException {
		if (!input.isNull("testdrive_fb_taken")) {
			testdrive_fb_taken = CNumeric(PadQuotes((String) input.get("testdrive_fb_taken")));
		}
		if (!input.isNull("testdrive_fb_notes")) {
			testdrive_fb_notes = PadQuotes((String) input.get("testdrive_fb_notes"));
		}
		if (!input.isNull("testdrive_time")) {
			testdrive_time = PadQuotes((String) input.get("testdrive_time"));
		}
		if (!input.isNull("testdrive_fb_entry_id")) {
			testdrive_fb_entry_id = CNumeric(PadQuotes((String) input.get("testdrive_fb_entry_id")));
		}
		if (!input.isNull("testdrive_fb_status_id")) {
			testdrive_fb_status_id = CNumeric(PadQuotes((String) input.get("testdrive_fb_status_id")));
		}
		if (!input.isNull("testdrive_fb_status_comments")) {
			testdrive_fb_status_comments = PadQuotes((String) input.get("testdrive_fb_status_comments"));
		}
	}

	protected void CheckForm() {
		msg = "";
		if (Long.parseLong(testdrive_time) > Long.parseLong(ToLongDate(kknow()))) {
			msg = msg + "<br>Test Drive feedback can be given after " + strToLongDate(testdrive_time) + "!";
		}
		if (testdrive_fb_taken.equals("0")) {
			msg = msg + "<br>Select Test Drive Taken or Not!";
		}
		if (testdrive_fb_taken.equals("1")) {
			if (testdrive_fb_status_id.equals("0")) {
				msg = msg + "<br>Select Status!";
			}
			if (testdrive_fb_status_comments.equals("")) {
				msg = msg + "<br>Enter Comments!";
			}

		}
		if (testdrive_fb_taken.equals("2")) {
			if (testdrive_fb_notes.equals("")) {
				msg = msg + "<br>Enter reason for testdrive not taken in notes!";
			}
		}
		if (testdrive_fb_notes.length() > 1000) {
			testdrive_fb_notes = testdrive_fb_notes.substring(0, 999);
		}
	}

	protected void UpdateFields() {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
					+ " SET "
					+ "testdrive_fb_taken = " + testdrive_fb_taken + ", "
					+ "testdrive_fb_status_id = " + testdrive_fb_status_id + ", "
					+ "testdrive_fb_status_comments = '" + testdrive_fb_status_comments + "', "
					+ "testdrive_fb_notes = '" + testdrive_fb_notes + "', ";
			if (testdrive_fb_entry_id.equals("0")) {
				StrSql = StrSql + "testdrive_fb_entry_id = '" + emp_id + "', "
						+ "testdrive_fb_entry_date = '" + ToLongDate(kknow()) + "' ";
			} else {
				StrSql = StrSql + "testdrive_fb_modified_id = " + emp_id + ", "
						+ "testdrive_fb_modified_date = '" + ToLongDate(kknow()) + "' ";
			}
			StrSql = StrSql + "where testdrive_id = " + testdrive_id + " ";

			// SOP("StrSql ---" + StrSql);
			updateQuery(StrSql);
			output.put("msg", "Feedback updated successfully!");

			if (comp_id.equals("1009")) {
				if (testdrive_fb_taken.equals("1")) {
					GetConfigDetails();
					if (config_sms_enable.equals("1")) {
						if (!contact_mobile1.equals("")) {
							SendSMS(contact_mobile1);
						}
						if (!contact_mobile2.equals("")) {
							SendSMS(contact_mobile2);
						}
					}
				}
			}
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void SendSMS(String contact_mobile) {
		msg = "Greetings from DD Motors! Dear " + contact_name + " Hope you had a wonderful Maruti Drive Experience, For any further assistance please contact Sales Manager " + executive_name
				+ " .\nWarm Regards,\nDD Motocrs.";
		try {
			String Sql = "insert into " + compdb(comp_id) + "axela_sms"
					+ "(sms_mobileno,"
					+ "sms_msg,"
					+ "sms_date ,"
					+ "sms_sent ,"
					+ "sms_contact_id, "
					+ "sms_entry_id)"
					+ "values"
					+ "('" + contact_mobile + "',"
					+ "'" + msg + "',"
					+ "'" + ToLongDate(kknow()) + "',"
					+ "'0',"
					+ "'" + contact_id + "',"
					+ "'" + emp_id + "')";
			updateQuery(Sql);
			msg = "";
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateTestDriveTaken() {
		try {
			map.put("testdrive_type_id", "0");
			map.put("testdrive_type_name", "Select");
			list.add(gson.toJson(map));
			map.put("testdrive_type_id", "1");
			map.put("testdrive_type_name", "Taken");
			list.add(gson.toJson(map));
			map.put("testdrive_type_id", "2");
			map.put("testdrive_type_name", "Not Taken");
			list.add(gson.toJson(map));
			map.clear();
			output.put("populatetestdrivetaken", list);
			list.clear();
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "Select testdrive_fb_taken, testdrive_fb_status_id, testdrive_fb_notes, testdrive_time,testdrive_fb_status_comments,"
					+ " testdrive_fb_entry_id"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id = testdrive_fb_status_id"
					+ " where testdrive_id = " + testdrive_id + "";
			// + BranchAccess + "";
			// SOP("strsql==111======="+StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_time = crs.getString("testdrive_time");
					output.put("testdrive_fb_taken", crs.getString("testdrive_fb_taken"));
					output.put("testdrive_fb_status_id", crs.getString("testdrive_fb_status_id"));
					output.put("testdrive_fb_status_comments", crs.getString("testdrive_fb_status_comments"));
					output.put("testdrive_fb_notes", crs.getString("testdrive_fb_notes"));
					output.put("testdrive_time", crs.getString("testdrive_time"));
					output.put("testdrive_fb_entry_id", crs.getString("testdrive_fb_entry_id"));
				}
			} else {
				output.put("msg", "Invalid Test Drive!");
			}
			crs.close();
		} catch (Exception ex) {
			SOP("AxelaAuto === " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateStatus() {
		try {
			StrSql = "select status_id, status_name from " + compdb(comp_id) + "axela_sales_testdrive_status order by status_name";
			// + BranchAccess + "";
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name", crs.getString("status_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatestatus", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetConfigDetails() {
		try {
			StrSql = "Select contact_id, concat(title_desc, ' ', contact_fname,' ', contact_lname) as contact_name,"
					+ " contact_mobile1, contact_mobile2, concat(emp_name,' ',emp_mobile1) as emp_name,"
					+ " config_sms_enable "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id"
					+ " , " + compdb(comp_id) + "axela_config"
					+ " where testdrive_id = " + testdrive_id + "";
			// + BranchAccess + "";
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					contact_id = crs.getString("contact_id");
					contact_name = crs.getString("contact_name");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					executive_name = crs.getString("emp_name");
					config_sms_enable = crs.getString("config_sms_enable");
				}
				map.clear();
				output.put("populatefields", list);
				list.clear();
			} else {
				output.put("msg", "Invalid Test Drive!");
			}
			crs.close();
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
