package axela.ws.cust;
//Divya

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

public class WS_AppVersion extends ConnectWS {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "";
	JSONObject output = new JSONObject();

	public JSONObject appversion(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = CNumeric(PadQuotes((String) input.get("user_id")));
		}

		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		StrSql = "SELECT appconfig_app_ver" + " FROM " + compdb(comp_id) + "axela_app_config";
		CachedRowSet crs =processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			while (crs.next()) {
				output.put("appconfig_app_ver", crs.getString("appconfig_app_ver"));
			}
		} else {
			output.put("msg", "Invalid Request!");
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}

}
