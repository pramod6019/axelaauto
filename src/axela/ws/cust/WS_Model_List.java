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

public class WS_Model_List extends ConnectWS {

	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String pagecurrent = "";
	public String user_id = "";
	public String comp_id = "";
	public boolean flag = false;
	public int TotalRecords = 0;

	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	ArrayList<String> list1 = new ArrayList<String>();
	Map<String, String> map1 = new HashMap<String, String>();
	Connect ct = new Connect();

	public JSONObject modeldata(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}

		if (!input.isNull("user_id")) {
			user_id = PadQuotes((String) input.get("user_id"));
		}
		if (!input.isNull("comp_id")) {
			comp_id = PadQuotes((String) input.get("comp_id"));
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = input.get("pagecurrent") + "";
		}
		CountSql = "SELECT COUNT(DISTINCT model_id)";
		StrSql = "Select model_id, model_name, model_desc, model_img_value";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				+ " WHERE 1=1 "
				+ " and model_active = '1'"
				+ " and model_sales = '1'";

		StrSql = StrSql + SqlJoin;
		CountSql += SqlJoin;

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		StrSql = StrSql + " GROUP BY model_id"
				+ " ORDER BY model_name"
				+ LimitRecords(TotalRecords, pagecurrent);
		SOP("StrSql = " + StrSql);

		CachedRowSet crs =processQuery(StrSql, 0);
		String img[] = null;
		int count = 0;
		if (crs.isBeforeFirst()) {
			String desc = "";
			while (crs.next()) {
				if (!crs.getString("model_img_value").equals("")) {
					count++;
					flag = true;
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					map.put("model_desc", crs.getString("model_desc"));
					map.put("url_image", ct.WSUrl() + "thumbnail?comp_id=" + comp_id + "&image=" + crs.getString("model_img_value") + "&path=modelphoto&width=500");
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			if (count == 0) {
				output.put("msg", "No Models Found!");
			} else {
				output.put("listdata", list);
				output.put("totalrecords", TotalRecords);
			}
		} else {
			if (pagecurrent.equals("1")) {
				output.put("msg", "No Records Found!");
			} else {
				output.put("msg", "No Models Found!");
			}
		}
		crs.close();
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}
}
