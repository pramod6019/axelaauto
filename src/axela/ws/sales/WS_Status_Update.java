/* Annappa (29th April 2015) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Status_Update extends ConnectWS {

	public String update = "";
	public String updateB = "";
	public String delete = "";
	public String submitB = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_status = "";
	public String emp_uuid = "0";
	public String role_id = "";
	public String branch_id = "";
	// public String enquiry_decisiontime_id = "0";
	// ws
	Gson gson = new Gson();
	// JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	public String status = "";

	public JSONObject Status_Update(JSONObject input) {
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_CRMFollowUp input = " + input);
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
						emp_status = PadQuotes((String) input.get("emp_status"));
						// SOP("emp_status---------" + emp_status);
					}

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
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}

}
