package axela.service;

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

public class Vehicle_In_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String vehmove_reg_no = "", id = "", variant = "", variantid = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			variant = PadQuotes(request.getParameter("variant"));
			if (variant.length() > 1) {
				StrHTML = PopulateVariant("0", comp_id);
			}

			vehmove_reg_no = PadQuotes(request.getParameter("vehmove_reg_no"));
			if (!vehmove_reg_no.equals("")) {
				StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE 1=1"
						+ " AND veh_reg_no = '" + vehmove_reg_no + "'";
				StrHTML = CNumeric(ExecuteQuery(StrSql));
			}

		} catch (Exception ex) {
			SOP("DC===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}

	public String PopulateVariant(String variant_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {

			if (!variant_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " item_id,"
						+ " item_name"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE 1 = 1 "
						+ " AND item_type_id = 1"
						+ " AND item_id = " + variant_id
						+ " GROUP BY item_id"
						+ " ORDER BY item_name";

				// SOP("StrSql PopulateVariant---------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					// Str.append("<option value=0>Select Variant</option>");
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("item_id")).append("");
						Str.append(">").append(crs.getString("item_name")).append("</option>\n");
					}
				}
				crs.close();

				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!variant.equals("")) {

					// Str.append("<option value=0 >").append("Select Variant").append("</option>\n");

					StrSql = "SELECT item_id, item_name"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " WHERE 1=1"
							+ " AND item_type_id = 1";
					if (!variant.equals("")) {
						StrSql += " AND item_name LIKE '%" + variant + "%'";
					}
					StrSql = StrSql + " GROUP BY item_id"
							+ " ORDER BY item_name";
					if (!variant.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("StrSql Variant===" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!variant.equals("")) {
								map.put("id", crs.getString("item_id"));
								map.put("text", unescapehtml(crs.getString("item_name")));
								list.add(gson.toJson(map));
							}
							if (!variantid.equals("0")) {
								output.put("text", crs.getString("item_name"));
							}
						}
						if (!variant.equals("")) {
							map.clear();
							output.put("variants", list);
							list.clear();
						}
					} else {
						if (!variant.equals("")) {
							output.put("variants", "");
						}

						if (!variantid.equals("0")) {
							output.put("text", "");
						}
					}
					crs.close();
				}
				return JSONPadQuotes(output.toString());
			}
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
