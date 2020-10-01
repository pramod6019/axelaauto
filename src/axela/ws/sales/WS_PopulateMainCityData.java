package axela.ws.sales;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_PopulateMainCityData extends Connect {

    public String StrSql = "";
    public String city = "";
    public String comp_id = "";
    public String emp_uuid = "0";
    public String msg = "";
    Gson gson = new Gson();
    JSONObject output = new JSONObject();
    ArrayList<String> list = new ArrayList<String>();
    Map<String, String> map = new HashMap<String, String>();

    public JSONObject citiesdata(JSONObject input) throws Exception {
        if (AppRun().equals("0")) {
            SOP("input = " + input);
        }
        Connection conn1 = null;
        try {
            if (!input.isNull("comp_id")) {
                comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
            }
            
            if (!input.isNull("emp_uuid")) {
            	emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
            }
            if (!input.isNull("city")) {
                city = PadQuotes((String) input.get("city"));
            }
            if (!comp_id.equals("0") && !city.equals("")) {
                StrSql = "SELECT city_id, COALESCE(CONCAT(city_name, ' - ', state_name), '') as city_name"
                        + " FROM " + compdb(comp_id) + "axela_city"
                        + " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
                        + " WHERE 1 = 1"
                        + " AND city_name like '%" + city + "%'"
                        + " LIMIT 10";
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    while (crs.next()) {
                        map.put("city_id", crs.getString("city_id"));
                        map.put("city_name", crs.getString("city_name"));
                        list.add(gson.toJson(map));
                    }
                    map.clear();
                    output.put("cities", list);
                    list.clear();
                } else {
                    msg = "No Records Found!";
                    output.put("cities", msg);
                }
                crs.close();
            } else {
                output.put("cities", "");
            }
            /*End of Signin Block*/

        } catch (Exception ex) {
            SOPError("Axelaauto ==" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        } finally {
            input = null;
        }
        if (AppRun().equals("0")) {
            SOP("output = " + output);
        }
        return output;
    }
}
