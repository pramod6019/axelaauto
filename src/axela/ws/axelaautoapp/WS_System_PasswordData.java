package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_System_PasswordData extends ConnectWS {

	public String updateB = "";
	public String emp_id = "", branch_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_uuid = "0";
	public String emp_upass;
	public String current_pwd = "";
	public String new_pwd = "";
	public String confirmnew_pwd = "";
	// ws
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject changepassword(JSONObject input, @Context HttpServletRequest request) {
		try {

			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!input.isNull("comp_id")) {
					comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				}
				new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
				if (!input.isNull("emp_uuid")) {
					emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
				}
				if (!emp_id.equals("0")) {
					if (!input.isNull("updateB")) {
						updateB = PadQuotes((String) input.get("updateB"));
					}
					if (updateB == null) {
						updateB = "";
					}

					if (!"Update Password".equals(updateB)) {
						current_pwd = " ";
						new_pwd = " ";
						confirmnew_pwd = " ";
					} else {
						GetValues(input);
						GetUpass();
						CheckForm();
						if (!msg.equals("")) {
							output.put("pwdmsg", "Error" + msg);
						} else {
							UpdateFields();
						}
					}

				}
			}
			return output;
		} catch (Exception ex) {
			SOPError("Axelaauto-App ==" + this.getClass().getName());
			SOPError("Axelaauto-App == " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return output;
		}
	}

	protected void GetValues(JSONObject input) {
		try {
			if (!input.isNull("current_pwd")) {
				current_pwd = PadQuotes((String) input.get("current_pwd"));
			}
			if (!input.isNull("new_pwd")) {
				new_pwd = PadQuotes((String) input.get("new_pwd"));
			}
			if (!input.isNull("confirmnew_pwd")) {
				confirmnew_pwd = PadQuotes((String) input.get("confirmnew_pwd"));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App ==" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				int emp_idintval = Integer.parseInt(emp_id);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp Set emp_upass = '" + confirmnew_pwd + "' WHERE  emp_id =" + emp_idintval + "";
				updateQuery(StrSql);
				// msg = "Password update successfully!";
				StrSql = "UPDATE axela_uni_emp Set emp_upass = '" + confirmnew_pwd + "' WHERE  emp_id =" + emp_idintval + "";
				updateQuery(StrSql);
				output.put("pwdmsg", "Password updated successfully!");

			} catch (Exception ex) {
				SOPError("Axelaauto-App=====" + this.getClass().getName());
				SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			}
		}
	}

	protected void GetUpass() {
		try {
			StrSql = "select emp_upass from " + compdb(comp_id) + "axela_emp where  emp_id =" + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_upass = crs.getString("emp_upass");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (current_pwd.equals("")) {
			msg = "<br>Your current password cannot be blank!";
		}
		if (!current_pwd.equals("") && !current_pwd.equals(emp_upass)) {
			msg = msg + "<br>Please check your current password!";
		}
		if (new_pwd.equals("")) {
			msg = msg + "<br>Your new password cannot be blank!";
		}
		if (!new_pwd.equals("") && new_pwd.length() < 8) {
			msg = msg + "<br>Your new password cannot be less than 8 Characters!";
		}
		if (confirmnew_pwd.equals("")) {
			msg = msg + "<br>Please check your password confirmation!";
		}
		if (!confirmnew_pwd.equals("") && !confirmnew_pwd.equals(new_pwd)) {
			msg = msg + "<br>Your new passwords does not match!";
		}
	}
}
