package axela.ws.runner;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executives_Dash_Check;
import cloudify.connect.Connect;

public class WS_Ecover_Executive_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String exe_id = "0";
	public String branch_brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String name = "";
	public String value = "";
	public String StrSql = "";
	public String StrHTML = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String labelname = "";
	// for Tabs
	public String history = "", updatefields = "";
	public String accessright = "", accessId = "";

	public String ExecutiveDashCheck(JSONObject input, HttpServletRequest request) {
		try {
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric((String) input.get("comp_id"));
			}
			if (!comp_id.equals("0")) {
				GetValues(input);
				Executives_Dash_Check enqdash = new Executives_Dash_Check();
				enqdash.BranchAccess = BranchAccess;
				enqdash.comp_id = comp_id;
				enqdash.emp_id = emp_id;
				enqdash.name = name;
				enqdash.value = value;
				enqdash.exe_id = exe_id;
				enqdash.labelname = labelname;
				// enqdash.accessright = accessright;
				enqdash.accessId = accessId;
				enqdash.history = history;
				if (!exe_id.equals("0")) {
					// if (accessright.equals("yes")) {
					// enqdash.AccessRightsUpdate();
					// }
					updatefields = "yes";
					if (updatefields.equals("yes")) {
						enqdash.UpdateFields(name, value);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Ecover===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return "";
	}

	public void GetValues(JSONObject input) {
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric((String) input.get("emp_id"));
			}
			if (!input.isNull("name")) {
				name = PadQuotes((String) input.get("name"));
			}
			if (!input.isNull("value")) {
				value = PadQuotes((String) input.get("value"));
			}
			if (!input.isNull("exe_id")) {
				exe_id = CNumeric((String) input.get("exe_id"));
			}
			if (!input.isNull("labelname")) {
				labelname = CNumeric((String) input.get("labelname"));
			}
			if (!input.isNull("accessright")) {
				accessright = PadQuotes((String) input.get("accessright"));
			}
			if (!input.isNull("accessId")) {
				accessId = CNumeric((String) input.get("accessId"));
			}
			if (!input.isNull("history")) {
				history = PadQuotes((String) input.get("history"));
			}
			if (!input.isNull("updatefields")) {
				updatefields = PadQuotes((String) input.get("updatefields"));
			}
		} catch (Exception ex) {
			SOPError("Ecover===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
}