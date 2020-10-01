package axela.ws.cust;
//Divya

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Showroom_List extends ConnectWS {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "0";

	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();

	public JSONObject listshowrooms(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = PadQuotes((String) input.get("user_id"));
		}
		if (!input.isNull("comp_id")) {
			comp_id = PadQuotes((String) input.get("comp_id"));
		}

		// if (!user_id.equals("0")) {
		StrSql = "SELECT CAST(showroom_name AS CHAR) AS showroom_name,"
				+ " CAST(showroom_address AS CHAR) AS showroom_address,"
				+ " CAST(state_name AS CHAR) AS state_name,"
				+ " CAST(showroom_pin AS CHAR) AS showroom_pin,"
				+ " CAST(city_name AS CHAR) AS city_name,"
				+ " showroom_mobile1, showroom_mobile2, showroom_phone1, showroom_phone2,"
				+ " showroom_email1, showroom_email2, showroom_website1, showroom_website2, showroom_latitude AS latitude,showroom_longitude AS longitude"
				+ " FROM " + compdb(comp_id) + "axela_app_showroom"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = showroom_city_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
				+ " WHERE 1 = 1"
				+ " AND showroom_active = 1"
				+ " GROUP BY showroom_id"
				+ " ORDER BY showroom_id";
		// SOP("StrSql====" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			StringBuilder address = new StringBuilder();
			while (crs.next()) {
				if (!crs.getString("showroom_address").equals("")) {
					address.append(crs.getString("showroom_address"));
				}
				if (!crs.getString("city_name").equals("")) {
					address.append("<br/>" + crs.getString("city_name"));
				}
				if (!crs.getString("showroom_pin").equals("")) {
					address.append("<br/>" + crs.getString("showroom_pin"));
				}
				if (!crs.getString("state_name").equals("")) {
					address.append("<br/>" + crs.getString("state_name"));
				}
				map.put("showroom_name", crs.getString("showroom_name"));
				map.put("showroom_address", address.toString());
				map.put("showroom_mobile1", crs.getString("showroom_mobile1"));
				map.put("showroom_mobile2", crs.getString("showroom_mobile2"));
				map.put("showroom_phone1", crs.getString("showroom_phone1"));
				map.put("showroom_phone2", crs.getString("showroom_phone2"));
				map.put("showroom_email1", crs.getString("showroom_email1"));
				map.put("showroom_email2", crs.getString("showroom_email2"));
				map.put("showroom_website1", crs.getString("showroom_website1"));
				map.put("showroom_website2", crs.getString("showroom_website2"));
				map.put("showroom_latitude", crs.getString("latitude"));
				map.put("showroom_longitude", crs.getString("longitude"));
				list.add(gson.toJson(map));
				address.setLength(0);
			}
			map.clear();
			output.put("listdata", list);
		} else {
			output.put("msg", "No Showrooms found!");
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}
}
