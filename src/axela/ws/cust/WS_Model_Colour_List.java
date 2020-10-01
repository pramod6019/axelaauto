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

public class WS_Model_Colour_List extends ConnectWS {

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

	public JSONObject listcolourdata(JSONObject input) throws Exception {
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
			SOP("model_id---------" + model_id);
		}
		if (!model_id.equals("0")) {
			StrSql = "Select model_name, item_id, colours_id, colours_title, colours_colour, colours_value, "
					+ " COALESCE(price_amt-price_disc) As exshowroomprice "
					+ " from " + compdb(comp_id) + "axela_app_model_colours"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = colours_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " where colours_model_id = " + model_id
					+ " GROUP BY colours_id "
					+ " ORDER BY colours_title ";
			SOPError("StrSql--------" + StrSql);

			CachedRowSet crs =processQuery(StrSql, 0);
			String img[] = null;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("model_name", crs.getString("model_name"));
					map.put("exshowroomprice", IndFormat(crs.getString("exshowroomprice")));
					map.put("colours_id", crs.getString("colours_id"));
					map.put("colours_title", crs.getString("colours_title"));
					map.put("colours_colour", crs.getString("colours_colour"));
					map.put("colours_value", ct.WSUrl() + "thumbnail?comp_id=" + comp_id + "&image=" + crs.getString("colours_value") + "&path=modelcoloursimg&width=500");
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("listdata", list);
			} else {
				output.put("msg", "No Records Found!");
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
