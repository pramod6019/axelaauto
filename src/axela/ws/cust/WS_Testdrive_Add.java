package axela.ws.cust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Testdrive_Add extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String testdrive_user_id = "";
	public String testdrive_title_id = "";
	public String testdrive_fname = "";
	public String testdrive_lname = "";
	public String testdrive_mobile = "";
	public String testdrive_email = "";
	public String comp_id = "0";
	public String testdrive_testdrivetype_id = "";
	public String testdrive_model_id = "0";
	public String testdrive_item_id = "0";
	public String testdrive_comments = "";
	public String time = "", date = "";
	public String testdrive_time = "";
	public String user_id = "0";
	public String model_id = "0";
	public String validatetestdrivetime = "";
	Map<Integer, Object> sqlmap = new HashMap<Integer, Object>();
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject testdriveadd(JSONObject input) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOPError("testdrive car input = " + input);
			}

			if (!input.isNull("add")) {
				add = PadQuotes((String) input.get("add"));
			}
			if (!input.isNull("addB")) {
				addB = PadQuotes((String) input.get("addB"));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("testdrive_user_id")) {
				testdrive_user_id = CNumeric(PadQuotes((String) input.get("testdrive_user_id")));
			}
			if (!input.isNull("testdrive_mobile")) {
				testdrive_mobile = CNumeric(PadQuotes((String) input.get("testdrive_mobile")));
			}
			if (!input.isNull("model_id")) {
				model_id = CNumeric(PadQuotes((String) input.get("model_id")));
			}
			if (add.equals("yes") && addB.equals("")) {
				PopulateModel();
				PopulateItem();
				PopulateTitle();
			}

			// first time
			if (!testdrive_mobile.equals("")) {
				GetValues(input);
				CheckForm();
				if (IsValidMobileNo(testdrive_mobile)) {
					String regstatus = "";
					StrSql = "SELECT user_id "
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_mobile = ?";
					sqlmap.put(1, "91-" + testdrive_mobile);
					CachedRowSet crs = processPrepQuery(StrSql, sqlmap, 0);
					while (crs.next()) {
						user_id = crs.getString("user_id");
					}
					crs.close();
					sqlmap.clear();

					if (user_id.equals("0")) {
						regstatus = "add";
					} else {
						regstatus = "update";
					}
					// GetValues(input);
					// CheckForm();
					if (msg.equals("")) {
						if (regstatus.equals("add")) {
							AddFields();
						} else {
							UpdateUser();
						}
						AddTestDrive();
						output.put("user_id", user_id);
						String addmsg = "Test Drive request added successfully,"
								+ " our sales consultant will be in touch with you shortly.<br>"
								+ " Have a nice day!";
						output.put("msg", addmsg);
					} else {
						output.put("msg", msg);
					}
				} else {
					output.put("msg", "Invalid Mobile No!");
				}
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			input = null;
		}
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}

	protected void GetValues(JSONObject input) {
		try {
			if (!input.isNull("testdrive_title_id")) {
				testdrive_title_id = CNumeric(PadQuotes((String) input.get("testdrive_title_id")));
			}
			if (!input.isNull("testdrive_fname")) {
				testdrive_fname = PadQuotes((String) input.get("testdrive_fname"));
			}
			if (!input.isNull("testdrive_lname")) {
				testdrive_lname = PadQuotes((String) input.get("testdrive_lname"));
			}
			if (!input.isNull("testdrive_mobile")) {
				testdrive_mobile = PadQuotes((String) input.get("testdrive_mobile"));

			}
			if (!input.isNull("testdrive_email")) {
				testdrive_email = PadQuotes((String) input.get("testdrive_email"));
			}
			if (!input.isNull("testdrive_model_id")) {
				testdrive_model_id = CNumeric(PadQuotes((String) input.get("testdrive_model_id")));
			}
			if (!input.isNull("testdrive_item_id")) {
				testdrive_item_id = CNumeric(PadQuotes((String) input.get("testdrive_item_id")));
			}
			if (!input.isNull("testdrive_comments")) {
				testdrive_comments = PadQuotes((String) input.get("testdrive_comments"));
			}
			if (!input.isNull("testdrive_time")) {
				time = PadQuotes((String) input.get("testdrive_time"));
			}
			if (!input.isNull("testdrive_date")) {
				date = PadQuotes((String) input.get("testdrive_date"));
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void CheckForm() {
		if (testdrive_title_id.equals("0")) {
			msg = msg + "Select Title!<br>";
		}
		if (testdrive_fname.equals("")) {
			msg = msg + "Enter First Name!<br>";
		}
		if (testdrive_lname.equals("")) {
			msg = msg + "Enter Last Name!<br>";
		}
		if (testdrive_mobile.equals("")) {
			msg = msg + "Enter Mobile No.!<br>";
		}
		if (testdrive_email.equals("")) {
			msg = msg + "Enter Email!<br>";
		}
		if (testdrive_model_id.equals("0")) {
			msg = msg + "Select Model!<br>";
		}
		if (date.equals("")) {
			msg = msg + "Select Test Drive Date!<br>";
		}
		if (time.equals("")) {
			msg = msg + "Select Test Drive Time!<br>";
		}
		if (!time.equals("") && !date.equals("")) {
			testdrive_time = date + " " + time;
			testdrive_time = ConvertLongDateToStr(testdrive_time);
			if (Long.parseLong(testdrive_time) < Long.parseLong(ToLongDate(kknow()))) {
				msg = msg + "Test Drive Date cannot be less than " + strToLongDate(ToLongDate(kknow())) + "!";
			}
		}
	}
	public void AddTestDrive() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_testdrive"
					+ " (testdrive_user_id,"
					+ " testdrive_title_id,"
					+ " testdrive_fname,"
					+ " testdrive_lname,"
					+ " testdrive_mobile,"
					+ " testdrive_email,"
					+ " testdrive_testdrivetype_id,"
					+ " testdrive_model_id,"
					+ " testdrive_comments,"
					+ " testdrive_time,"
					+ " testdrive_entry_time)"
					+ " VALUES"
					+ " (" + user_id + ","
					+ " " + testdrive_title_id + ","
					+ " '" + testdrive_fname + "',"
					+ " '" + testdrive_lname + "',"
					+ " '" + testdrive_mobile + "',"
					+ " '" + testdrive_email + "',"
					+ " '1',"
					+ " '" + testdrive_model_id + "',"
					+ " '" + testdrive_comments + "',"
					+ " '" + testdrive_time + "',"
					+ " '" + ToLongDate(kknow()) + "')";
			SOPError("StrSql testdrive add = " + StrSql);
			updateQuery(StrSql);

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateTitle() {
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// map.put("title_id", "0");
				// map.put("title_desc", "Select");
				// list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("title_id", crs.getString("title_id"));
					map.put("title_desc", crs.getString("title_desc"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("title_id", "0");
				map.put("title_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatetitle", list);
			list.clear();
			// output.put("selectedtitle_id", list);
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_user"
					+ " (user_mobile,"
					+ " user_title_id,"
					+ " user_fname,"
					+ " user_lname,"
					+ " user_pass,"
					+ " user_otp_time)"
					+ " VALUES"
					+ " ('91-" + testdrive_mobile + "',"
					+ " " + testdrive_title_id + ","
					+ " '" + testdrive_fname + "',"
					+ " '" + testdrive_lname + "',"
					+ " '',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("strsql====" + StrSql);
			user_id = UpdateQueryReturnID(StrSql);

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateModel() {
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1 AND model_active = '1'"
					+ " ORDER BY model_name";
			// SOPError("PopulateModel SQL------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemodel", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id)
					+ "axela_inventory_item"
					+ " WHERE 1=1";
			if (!model_id.equals("0") && !model_id.equals(""))
			{
				StrSql += " AND item_model_id =" + model_id;
			}
			StrSql += " AND item_type_id = 1 AND item_active = '1'"
					+ " ORDER BY item_name";
			// SOP("PopulateItem -------------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateitem", list);
			list.clear();
			crs.close();
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
	public void UpdateUser() {
		try {
			StrSql = "UPDATE axela_app_user"
					+ " SET"
					+ " user_title_id = " + testdrive_title_id + ","
					+ " user_fname = '" + testdrive_fname + "',"
					+ " user_lname = '" + testdrive_lname + "',"
					+ " user_email = '" + testdrive_email + "'"
					+ " where user_id = " + user_id + "";
			// SOPError("StrSql = " + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
