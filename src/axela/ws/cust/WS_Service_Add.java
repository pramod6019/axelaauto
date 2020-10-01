package axela.ws.cust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Service_Add extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String user_id = "";
	public String comp_id = "0";
	public String service_title_id = "";
	public String service_fname = "";
	public String service_lname = "";
	public String service_mobile = "";
	public String service_email = "";
	public String service_reg_no = "";
	public String service_slot = "";
	public String service_time = "";
	public String service_entry_time = "";
	public String validateservicetime = "";

	Map<Integer, Object> sqlmap = new HashMap<Integer, Object>();
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject serviceadd(JSONObject input) throws Exception {
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
			if (!input.isNull("user_id")) {
				user_id = CNumeric(PadQuotes((String) input.get("user_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("service_mobile")) {
				service_mobile = CNumeric(PadQuotes((String) input.get("service_mobile")));
			}

			if (add.equals("yes") && addB.equals("")) {
				PopulateSlot();
				PopulateTitle();
			}

			if (!service_mobile.equals("")) {
				GetValues(input);
				CheckForm();
				if (IsValidMobileNo(service_mobile)) {
					String regstatus = "";
					StrSql = "SELECT user_id "
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_mobile = '91-" + service_mobile + "'";
					CachedRowSet crs =processQuery(StrSql, 0);
					while (crs.next()) {
						user_id = crs.getString("user_id");
					}
					crs.close();

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
						AddServiceFields();
						output.put("user_id", user_id);
						String addmsg = "Service request added successfully,"
								+ " our sales consultant will be in touch with you shortly.<br>"
								+ " Have a nice day!";
						output.put("msg", addmsg);
					} else {
						output.put("msg", msg);
					}
				}
				else {
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
					+ " ('91-" + service_mobile + "',"
					+ " " + service_title_id + ","
					+ " '" + service_fname + "',"
					+ " '" + service_lname + "',"
					+ " '',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("strsql====" + StrSql);
			user_id = UpdateQueryReturnID(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(JSONObject input) {
		try {
			if (!input.isNull("service_title_id")) {
				service_title_id = CNumeric(PadQuotes((String) input.get("service_title_id")));
			}
			if (!input.isNull("service_fname")) {
				service_fname = PadQuotes((String) input.get("service_fname"));
			}
			if (!input.isNull("service_lname")) {
				service_lname = PadQuotes((String) input.get("service_lname"));
			}
			if (!input.isNull("service_mobile")) {
				service_mobile = PadQuotes((String) input.get("service_mobile"));
			}
			if (!input.isNull("service_email")) {
				service_email = PadQuotes((String) input.get("service_email"));
			}
			if (!input.isNull("service_reg_no")) {
				service_reg_no = PadQuotes((String) input.get("service_reg_no"));
			}
			if (!input.isNull("service_time")) {
				service_time = PadQuotes((String) input.get("service_time"));
			}
			if (!input.isNull("service_slot")) {
				service_slot = PadQuotes((String) input.get("service_slot"));
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CheckForm() {
		if (service_title_id.equals("0")) {
			msg = msg + "Select Title!<br>";
		}
		if (service_fname.equals("")) {
			msg = msg + "Enter First Name!<br>";
		}
		if (service_lname.equals("")) {
			msg = msg + "Enter Last Name!<br>";
		}
		if (service_mobile.equals("")) {
			msg = msg + "Enter Mobile No.!<br>";
		}
		if (service_email.equals("")) {
			msg = msg + "Enter Email!<br>";
		}
		if (service_reg_no.equals("")) {
			msg = msg + "<br>Enter Reg No.!";
		}
		if (service_time.equals("")) {
			msg = msg + "Enter Date!<br>";
		}
		if (service_slot.equals("")) {
			msg = msg + "<br>Select Slot!";
		}
	}

	public void AddServiceFields() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_service"
					+ " (service_user_id,"
					+ " service_title_id,"
					+ " service_fname,"
					+ " service_lname,"
					+ " service_mobile,"
					+ " service_email,"
					+ " service_reg_no,"
					+ " service_time,"
					+ " service_slot,"
					+ " service_entry_time)"
					+ " VALUES"
					+ " (" + user_id + ","
					+ " " + service_title_id + ","
					+ " '" + service_fname + "',"
					+ " '" + service_lname + "',"
					+ " '" + service_mobile + "',"
					+ " '" + service_email + "',"
					+ " '" + service_reg_no + "',"
					+ " '" + ConvertShortDateToStr(service_time) + "',"
					+ " '" + service_slot + "',"
					+ " '" + ToLongDate(kknow()) + "')";
			// SOPError("StrSql testdrive add = " + StrSql);
			updateQuery(StrSql);

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateSlot() {
		try {
			map.put("slot_id", "1");
			map.put("slot_name", "8am - 1pm");
			list.add(gson.toJson(map));

			map.put("slot_id", "2");
			map.put("slot_name", "1pm - 3pm");
			list.add(gson.toJson(map));

			map.put("slot_id", "3");
			map.put("slot_name", "3am - 6pm");
			list.add(gson.toJson(map));

			map.clear();
			output.put("populateslot", list);
			list.clear();
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
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UpdateUser() {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_user"
					+ " SET"
					+ " user_title_id = " + service_title_id + ","
					+ " user_fname = '" + service_fname + "',"
					+ " user_lname = '" + service_lname + "',"
					+ " user_email = '" + service_email + "'"
					+ " where user_id = " + user_id + "";
			// SOPError("StrSql = " + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
