package axela.ws.runner;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import cloudify.connect.Connect;

public class WS_Executive_Update_Password extends Connect {

	public String emp_id = "0";
	public String emp_upass = "";
	public String StrSql = "";
	public String comp_id = "0";

	public String Executive_Update_Password(JSONObject input, HttpServletRequest request) {
		try {
			if (AppRun().equals("0")) {
				SOP("input==WS_Exe_update===" + input);
			}
			if (!input.isNull("comp_id")) {
				comp_id = (String) input.get("comp_id");
			}
			if (!comp_id.equals("0")) {
				if (!input.isNull("emp_id")) {
					emp_id = (String) input.get("emp_id");
				}
				if (!input.isNull("emp_upass")) {
					emp_upass = (String) input.get("emp_upass");
				}
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp "
						+ " SET emp_upass = '" + emp_upass + "'"
						+ " WHERE  emp_id =" + emp_id + "";
				// SOP("StrSql=======" + StrSql);
				updateQuery(StrSql);
				new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
		return null;
	}

}
