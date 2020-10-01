/* Annappa (29th April 2015) */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Status_Update extends Connect {

	public String update = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_status = "";
	public String emp_uuid = "0";
	public String branch_id = "";
	// public String enquiry_decisiontime_id = "0";
	// ws
	Gson gson = new Gson();
	// JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	public String status = "";

	public JSONObject Status_Update(JSONObject input, HttpServletRequest request) {
		if (AppRun().equals("0")) {
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!emp_id.equals("0")) {
					if (!input.isNull("comp_id")) {
						comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
					}
					if (!input.isNull("emp_uuid")) {
						emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
					}
					if (!input.isNull("emp_status")) {
						emp_status = PadQuotes(StringEscapeUtils.escapeHtml4((String) input.get("emp_status")));
					}
					new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_status = '" + emp_status + "'"
							+ " WHERE emp_id = " + emp_id;
					// SOP("strsql======="+StrSql);
					updateQuery(StrSql);
					output.put("msg", "Status Updated Successfully!");

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App ======" + this.getClass().getName());
			SOPError("Axelaauto-App ====== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

}
