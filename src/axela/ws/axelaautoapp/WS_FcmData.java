//* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_FcmData extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String strSearch = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String strsearch = "";
	public String msg = "";
	public String variant_ids = "0", emp_device_os = "", emp_device_fcmtoken = "";
	Executive_Univ_Check update = new Executive_Univ_Check();
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject FcmData(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Preowned_List===" + input);
		}
		HttpSession session = request.getSession(true);
		// SOP("emp_uuid==enq==" + emp_uuid);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		// emp_all_exe = GetSession("emp_all_exe", request);

		// emp_id = "1";

		if (!input.isNull("token")) {
			emp_device_fcmtoken = PadQuotes((String) input.get("token"));
		}
		if (!input.isNull("os_type")) {
			emp_device_os = PadQuotes((String) input.get("os_type"));
		}
		if (!emp_id.equals("0")) {
			try {
				if (!emp_device_fcmtoken.equals("") && !emp_device_os.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET emp_device_fcmtoken = '" + emp_device_fcmtoken + "',"
							+ " emp_device_os = '" + emp_device_os + "'"
							+ " WHERE emp_id =" + emp_id;
					updateQuery(StrSql);
					update.UpdateUniversalEmp(emp_id, comp_id);
					msg = "Device Token and Device OS updated!";
					output.put("msg", msg);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}
}
