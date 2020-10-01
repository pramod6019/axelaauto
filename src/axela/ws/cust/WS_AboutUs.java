package axela.ws.cust;
//Divya

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

public class WS_AboutUs extends ConnectWS {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "0";
	JSONObject output = new JSONObject();

	public JSONObject aboutusdata(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = CNumeric(PadQuotes((String) input.get("user_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		StrSql = "Select appconfig_aboutus" + " from " + compdb(comp_id) + "axela_app_config";
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			while (crs.next()) {
				output.put("appconfig_aboutus", "<font color=#FFFFFF >" + unescapehtml(PadQuotes(crs.getString("appconfig_aboutus")) + "</font>"));
			}
		} else {
			output.put("msg", "No Data Found!");
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}
}
