package axela.ws.cust;
//Divya

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Offers_List extends ConnectWS {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "";

	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();

	public JSONObject listoffers(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = PadQuotes((String) input.get("user_id"));
		}

		if (!input.isNull("comp_id")) {
			comp_id = PadQuotes((String) input.get("comp_id"));
		}
		StrSql = "SELECT offers_topic, offers_desc"
				+ " FROM " + compdb(comp_id) + "axela_app_offers"
				+ " WHERE offers_active = 1"
				+ " GROUP BY offers_id"
				+ " ORDER BY offers_id DESC";

		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			while (crs.next()) {
				map.put("offers_topic", crs.getString("offers_topic"));
				map.put("offers_desc", "<font color=#FFFFFF >" + unescapehtml(crs.getString("offers_desc")) + "</font>");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("listdata", list);
		} else {
			output.put("msg", "No Offers found!");
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}
}
