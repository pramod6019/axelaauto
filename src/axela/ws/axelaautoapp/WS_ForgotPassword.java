package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_ForgotPassword extends ConnectWS {

	// public String submitB = "";
	public String msg = "";
	public String password = "";
	public String emp_uuid = "0";
	public String StrSql = "";
	public String email_id = "";
	public String signinid = "", emp_name = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String desc = "", from_address = "", to_address = "", from = "";
	axela.portal.Executives_Update update = new axela.portal.Executives_Update();
	// / ws
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	int count = 0;

	public JSONObject ForgotPassword(JSONObject input) {
		try {
			// if (!input.isNull("comp_id")) {
			// comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			// }
			// if (!input.isNull("emp_uuid")) {
			// emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			// }
			if (!input.isNull("email_id")) {
				email_id = PadQuotes((String) input.get("email_id"));
			}
			if (!email_id.equals("")) {

				CheckFields();
				if (msg.equals("")) {
					StrSql = "select emp_email1 from axelaauto.axela_uni_emp where 1=1";
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						from = crs1.getString("emp_email1");
					}
					crs1.close();
					from_address = "Axela " + "<" + from + ">";

					StrSql = "select emp_id,emp_email1, emp_upass, emp_name, comp_id"
							+ " from axelaauto.axela_uni_emp "
							+ " where emp_email1!='' and emp_active='1'  "
							+ " and emp_email1='" + email_id + "'";
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql==========" + StrSql);
					try {
						while (crs.next()) {
							count = count + 1;
							password = crs.getString("emp_upass");
							emp_id = crs.getString("emp_id");
							comp_id = crs.getString("comp_id");
							signinid = crs.getString("emp_email1");
							emp_name = crs.getString("emp_name");
							to_address = emp_name + " <" + email_id + ">";

							desc = desc + "Hi ";
							if (!emp_name.equals("")) {
								desc = desc + emp_name + ",<br><br>";
							}
							desc = desc + "The following is the Sign-In ID and password for Axela access: ";
							if (!signinid.equals("")) {
								desc = desc + "<br>Sign In ID: " + signinid;
							}
							if (!password.equals("")) {
								desc = desc + "<br>Password: " + password;
							}
							desc = desc + "<br><br>Best Regards,<br>Team Axela";
							desc = "<font face=Arial size=2>" + desc + "</font>";
							// updateQuery("Update axelaauto.axela_uni_emp set emp_upass = '" + password + "' where  emp_email1='" + email_id + "'");
							// update.UpdateUniversalEmp(emp_id, comp_id);
							postMail(to_address, "", "", from_address, "Axela Password", desc, "", comp_id);
						}
						crs.close();

						if (count == 0) {
							output.put("msg", "Email Not found!");
						} else {
							output.put("msg", "Password has been sent to your Email-Id!");
						}
					} catch (Exception ex) {// }
						SOPError("Axelaauto ==" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
				} else {
					output.put("msg", msg);
				}
			}
			return output;
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
	}

	protected void CheckFields() {
		msg = "";
		try {
			if (email_id.equals("")) {
				msg = "Enter Email ID!<br>";
			} else if (IsValidEmail(email_id) != true) {
				msg = "Enter Valid Email ID!";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
