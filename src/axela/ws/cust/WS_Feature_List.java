package axela.ws.cust;
//Divya

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Feature_List extends ConnectWS {

	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String pagecurrent = "";
	public String user_id = "";
	public String model_id = "";
	public String comp_id = "0";
	public int TotalRecords = 0;
	Map<Integer, Object> sqlmap = new HashMap<Integer, Object>();
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	ArrayList<String> list1 = new ArrayList<String>();
	Map<String, String> map1 = new HashMap<String, String>();
	Connect ct = new Connect();

	public JSONObject featuredata(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input ===== " + input);
		}
		if (!input.isNull("user_id")) {
			user_id = CNumeric(PadQuotes((String) input.get("user_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("model_id")) {
			model_id = CNumeric(PadQuotes((String) input.get("model_id")));
		}
		if (!model_id.equals("0")) {
			StrSql = "Select feature_id, feature_name, coalesce(group_concat(img_value),'') as 'images'"
					+ " from " + compdb(comp_id) + "axela_app_model_feature"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = feature_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_app_model_feature_img ON img_feature_id = feature_id"
					+ " where 1=1"
					+ " and feature_active = '1'"
					+ " and feature_model_id= " + model_id
					+ " GROUP BY feature_id"
					+ " ORDER BY feature_name";
			SOPError("StrSql = " + StrSql);

			CachedRowSet crs =processQuery(StrSql, 0);
			String img[] = null;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("feature_id", crs.getString("feature_id"));
					map.put("feature_name", crs.getString("feature_name"));

					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("listdata", list);
			} else {
				output.put("msg", "No Feature Found!");
			}
			crs.close();
		} else {
			output.put("msg", "Invalid Request!");
		}
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}

}
