package axela.ws.cust;
//Divya

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_ServiceCenter_List extends ConnectWS {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "0";

	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();

	public JSONObject listservicecentres(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = PadQuotes((String) input.get("user_id"));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		StrSql = "SELECT CAST(servicecenter_name AS CHAR) AS servicecenter_name,"
				+ " CAST(servicecenter_address AS CHAR) AS servicecenter_address,"
				+ " CAST(state_name AS CHAR) AS state_name,"
				+ " CAST(servicecenter_pin AS CHAR) AS servicecenter_pin,"
				+ " CAST(city_name AS CHAR) AS city_name,"
				+ " servicecenter_mobile1, servicecenter_mobile2, servicecenter_phone1, servicecenter_phone2,"
				+ " servicecenter_email1, servicecenter_email2, servicecenter_website1, servicecenter_website2, servicecenter_latitude AS latitude,servicecenter_longitude AS longitude"
				+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = servicecenter_city_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
				+ " WHERE 1 = 1"
				+ " AND servicecenter_active = 1"
				+ " GROUP BY servicecenter_id"
				+ " ORDER BY servicecenter_id ";

		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			StringBuilder address = new StringBuilder();
			while (crs.next()) {
				if (!crs.getString("servicecenter_address").equals("")) {
					address.append(crs.getString("servicecenter_address"));
				}
				if (!crs.getString("city_name").equals("")) {
					address.append("<br/>" + crs.getString("city_name"));
				}
				if (!crs.getString("servicecenter_pin").equals("")) {
					address.append("<br/>" + crs.getString("servicecenter_pin"));
				}
				if (!crs.getString("state_name").equals("")) {
					address.append("<br/>" + crs.getString("state_name"));
				}
				map.put("servicecenter_name", crs.getString("servicecenter_name"));
				map.put("servicecenter_address", address.toString());
				map.put("servicecenter_mobile1", crs.getString("servicecenter_mobile1"));
				map.put("servicecenter_mobile2", crs.getString("servicecenter_mobile2"));
				map.put("servicecenter_phone1", crs.getString("servicecenter_phone1"));
				map.put("servicecenter_phone2", crs.getString("servicecenter_phone2"));
				map.put("servicecenter_email1", crs.getString("servicecenter_email1"));
				map.put("servicecenter_email2", crs.getString("servicecenter_email2"));
				map.put("servicecenter_website1", crs.getString("servicecenter_website1"));
				map.put("servicecenter_website2", crs.getString("servicecenter_website2"));
				map.put("servicecenter_latitude", crs.getString("latitude"));
				map.put("servicecenter_longitude", crs.getString("longitude"));

				list.add(gson.toJson(map));
				address.setLength(0);
			}
			map.clear();
			output.put("listdata", list);
		} else {
			output.put("msg", "No Service Centres found!");
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}
}
