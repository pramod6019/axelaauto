package axela.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class City_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String city = "", id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				id = CNumeric(PadQuotes(request.getParameter("cityid")));
				city = PadQuotes(request.getParameter("city"));
				SOP("city===" + city);
				// SOP("id===" + id);
				if (city.length() > 1) {
					StrHTML = PopulateCities("0", comp_id);
				}
				// if (!id.equals("0")) {
				//
				// StrHTML = PopulateCities("0");
				// }
			}
		} catch (Exception ex) {
			SOP("DC===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCities(String cityid, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StringBuilder Str = new StringBuilder();
			if (!cityid.equals("0")) {

				// Get's executed while populating the city

				StrSql = "SELECT city_id, COALESCE(CONCAT(city_name,' - ',state_name), '') as city_name"
						+ " FROM " + compdb(comp_id) + "axela_city"
						+ " INNER JOIN " + compdb(comp_id)
						+ "axela_state ON state_id = city_state_id"
						+ " WHERE city_id = " + cityid
						+ " GROUP BY city_id"
						+ " ORDER BY city_name";

				// SOP("StrSql===" + StrSql);
				CachedRowSet crs =processQuery(StrSql, 0);

				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("city_id")).append(">");
					Str.append(crs.getString("city_name")).append("</option>\n");
				}
				crs.close();
				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!city.equals("")) {

					Str.append("<option value=0>Select City</option>");

					StrSql = "SELECT city_id, COALESCE(CONCAT(city_name,' - ',state_name), '') as city_name"
							+ " FROM " + compdb(comp_id) + "axela_city"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id";
					if (!city.equals("")) {
						StrSql += " WHERE city_name LIKE '%" + city + "%'";
					} else if (!id.equals("0")) {
						StrSql += " WHERE city_id = " + id + "";
					}
					StrSql += " GROUP BY city_id"
							+ " ORDER BY city_name";
					if (!city.equals("")) {
						StrSql += " LIMIT 10";
					}
					// SOP("city check=====" + StrSql);
					CachedRowSet crs =processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!city.equals("")) {
								map.put("id", crs.getString("city_id"));
								map.put("text", crs.getString("city_name"));
								list.add(gson.toJson(map));
							}
							if (!id.equals("0")) {
								output.put("text", crs.getString("city_name"));
							}
						}
						if (!city.equals("")) {
							map.clear();
							output.put("cities", list);
							list.clear();
						}
					} else {
						if (!city.equals("")) {
							output.put("cities", "");
						}
						if (!id.equals("0")) {
							output.put("text", "");
						}
					}
					crs.close();
				}
				return JSONPadQuotes(output.toString());
			}

		} catch (Exception ex) {
			SOP("DC===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
