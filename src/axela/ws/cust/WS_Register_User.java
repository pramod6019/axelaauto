package axela.ws.cust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Register_User extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String register = "";
	public String user_id = "0";
	public String user_title_id = "";
	public String user_fname = "";
	public String user_lname = "";
	public String user_mobile = "";
	public String user_pass = "";
	public String first = "";
	public String comp_id = "0";
	public String user_otp_time = "";
	public String user_reg_time = "";
	public String user_os = "";
	public String user_version = "";
	JSONObject output = new JSONObject();
	Map<Integer, Object> sqlmap = new HashMap<Integer, Object>();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject registeruserdata(JSONObject input) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOPError("register car input = " + input);
			}
			if (!input.isNull("user_mobile")) {
				user_mobile = PadQuotes((String) input.get("user_mobile"));
			}
			if (!input.isNull("first")) {
				first = PadQuotes((String) input.get("first"));
			}
			if (!input.isNull("user_id")) {
				user_id = CNumeric(PadQuotes((String) input.get("user_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("register")) {
				register = PadQuotes((String) input.get("register"));
			}
			if (register.equals("yes")) {
				PopulateTitle();
			}
			// first time
			if (first.equals("yes") && !user_mobile.equals("")) {
				if (IsValidMobileNo(user_mobile)) {
					String regstatus = "";
					StrSql = "SELECT user_id "
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_mobile = ?";
					sqlmap.put(1, "91-" + user_mobile);
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
					GetValues(input);
					CheckForm();
					if (msg.equals("")) {
						user_pass = GenOTP(4);
						AddFields(regstatus);
						SendSMS();
						output.put("user_id", user_id);
						output.put("user_pass", user_pass);
						output.put("msg", "SMS sent successfully!");
					} else {
						output.put("msg", msg);
					}
				} else {
					output.put("msg", "Invalid Mobile No!");
				}
			} // second time
			else if (first.equals("no") && !user_id.equals("0")) {
				String pass = "";
				if (!input.isNull("user_pass")) {
					user_pass = PadQuotes((String) input.get("user_pass"));
				}
				if (!user_pass.equals("")) {
					StrSql = "SELECT user_pass, user_otp_time"
							+ " FROM " + compdb(comp_id) + "axela_app_user"
							+ " WHERE user_id = ?";
					sqlmap.put(1, user_id);
					CachedRowSet crs = processPrepQuery(StrSql, sqlmap, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							pass = crs.getString("user_pass");
							user_otp_time = crs.getString("user_otp_time");
						}
					} else {
						output.put("msg", "Invalid request!");
					}
					crs.close();
					sqlmap.clear();

					if (getHoursBetween(StringToDate(user_otp_time), kknow()) <= 24.00) {
						if (pass.equals(user_pass)) {

							if (!input.isNull("user_os")) {
								user_os = PadQuotes((String) input.get("user_os"));
							}
							if (!input.isNull("user_version")) {
								user_version = PadQuotes((String) input.get("user_version"));
							}

							if (user_os.equals("") || user_version.equals("")) {
								msg = msg + "<br>Invalid Details!";
							}

							if (msg.equals("")) {
								UpdateFields();
								output.put("msg", "You have successfully registered!");
							} else {
								output.put("msg", msg);
							}
						} else {
							output.put("msg", "Please enter correct password!");
						}
					} else {
						output.put("msg", "Time expired for your One Time Password. Please register once again!");
					}

				} else {
					output.put("msg", "Please enter authentication password!");
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

	public void GetValues(JSONObject input) {
		try {
			if (!input.isNull("user_title_id")) {
				user_title_id = PadQuotes((String) input.get("user_title_id"));
			}
			if (!input.isNull("user_fname")) {
				user_fname = PadQuotes((String) input.get("user_fname"));
			}
			if (!input.isNull("user_lname")) {
				user_lname = PadQuotes((String) input.get("user_lname"));
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CheckForm() {
		msg = "";
		if (user_title_id.equals("0")) {
			msg = msg + "<br>Select Title!";
		}
		if (user_fname.equals("")) {
			msg = msg + "<br>Enter First Name!";
		}
		if (user_lname.equals("")) {
			msg = msg + "<br>Enter Last Name!";
		}
	}

	public void AddFields(String regstatus) {
		try {
			if (regstatus.equals("add")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_user"
						+ " (user_mobile,"
						+ " user_title_id,"
						+ " user_fname,"
						+ " user_lname,"
						+ " user_pass,"
						+ " user_otp_time)"
						+ " VALUES"
						+ " ('91-" + user_mobile + "',"
						+ " " + user_title_id + ","
						+ " '" + user_fname + "',"
						+ " '" + user_lname + "',"
						+ " '" + user_pass + "',"
						+ " '" + ToLongDate(kknow()) + "')";
				user_id = UpdateQueryReturnID(StrSql);
			} else if (regstatus.equals("update")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_app_user"
						+ " SET"
						+ " user_pass = '" + user_pass + "',"
						+ " user_otp_time = '" + ToLongDate(kknow()) + "'"
						+ " where user_id = " + user_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UpdateFields() {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_user"
					+ " SET"
					+ " user_reg_time = '" + ToLongDate(kknow()) + "',"
					+ " user_os = '" + user_os + "',"
					+ " user_version = '" + user_version + "',"
					+ " user_active = 1"
					+ " where user_id = " + user_id + "";
			// SOPError("StrSql = " + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS() {
		String msg = "Welcome, your One Time Password is " + user_pass + ". Please use it to authenticate your mobile number.";
		StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
				+ " (sms_mobileno,"
				+ " sms_msg,"
				+ " sms_date,"
				+ " sms_sent,"
				+ " sms_entry_id)"
				+ " values ("
				+ " " + user_mobile + ","
				+ " '" + msg + "',"
				+ " " + ToLongDate(kknow()) + ","
				+ " 0,"
				+ " 1)";
		// SOPError("StrSql = " + StrSql);
		updateQuery(StrSql);
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
			// output.put("selectedtitle_id", list);
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
