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

public class WS_FeatureDescription extends ConnectWS {

	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String pagecurrent = "";
	public String user_id = "";
	public String comp_id = "";
	public String feature_id = "";
	public String model_id = "";
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

	public JSONObject featuredescdata(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOPError("input = " + input);
		}
		if (!input.isNull("user_id")) {
			user_id = CNumeric(PadQuotes((String) input.get("user_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("feature_id")) {
			feature_id = CNumeric(PadQuotes((String) input.get("feature_id")));
		}
		if (!feature_id.equals("0")) {
			StrSql = "Select feature_name, feature_desc, coalesce(group_concat(img_value),'') as 'images'"
					+ " from " + compdb(comp_id) + "axela_app_model_feature"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_app_model_feature_img ON img_feature_id = feature_id"
					+ " where 1=1"
					+ " and feature_active = '1'"
					+ " and feature_id=?"
					+ " ORDER BY feature_rank";
			// SOPError("StrSql = " + StrSql);
			sqlmap.put(1, feature_id);
			CachedRowSet crs = processPrepQuery(StrSql, sqlmap, 0);
			String img[] = null;
			if (crs.isBeforeFirst()) {
				String desc = "";
				while (crs.next()) {
					desc = crs.getString("feature_desc");
					desc = desc.replaceAll("\\r\\n\\r\\n", "");
					output.put("feature_name", crs.getString("feature_name"));
					output.put("feature_desc", unescapehtml("<font color=#000000>" + desc + "</font>"));
					// list.add(gson.toJson(map)); // Converting String to Json
					if (!crs.getString("images").equals("")) {
						img = crs.getString("images").split(",");
						for (int i = 0; i < img.length; i++) {
							map1.put("url_image", ct.WSUrl() + "thumbnail?comp_id=" + comp_id + "&image=" + img[i] + "&path=featureimg&width=500");
							list1.add(gson.toJson(map1));
						}
						output.put("url", list1);
					}
				}
			} else {
				output.put("msg", "No Feature Description!");
			}
			crs.close();
			sqlmap.clear();
		} else {
			output.put("msg", "Invalid Request!");
		}
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}

}
