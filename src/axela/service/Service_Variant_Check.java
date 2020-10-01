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

public class Service_Variant_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String variant = "", variantid = "";
	String branch_id = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("----------comp_id--------------");
			if (!comp_id.equals("0")) {
				variantid = CNumeric(PadQuotes(request.getParameter("variantid")));
				variant = PadQuotes(request.getParameter("variant"));
				branch_id = CNumeric(PadQuotes(request.getParameter("variantbranch")));
				//
				// SOP("variantid-----" + variantid);
				// SOP("variant-----" + variant);
				if (variant.length() > 1) {
					StrHTML = PopulateVariant("0", comp_id);
				}

				if (!variantid.equals("0")) {
					StrHTML = PopulateVariant("0", comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
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
						+ " variant_id,"
						+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
						+ " FROM axela_preowned_variant"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = carmanuf_id"
						+ " WHERE 1 = 1 "
						+ " AND variant_id = " + variant_id;
				// if (!ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + branch_id).equals("1")) {
				// StrSql += " AND branch_id = " + branch_id;
				// }
				StrSql += " GROUP BY variant_id"
						+ " ORDER BY"
						+ " carmanuf_name,"
						+ " preownedmodel_name,"
						+ " variant_name";

				SOP("StrSql PopulateVariant---------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("variant_id")).append("");
						Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
					}
				}
				crs.close();

				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!variant.equals("")) {

					Str.append("<option value=0 >").append("Select Option").append("</option>\n");

					StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) AS variant_name"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = carmanuf_id"
							+ " WHERE 1 = 1";
					if (!ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + branch_id).equals("1")) {
						StrSql += " AND branch_id = " + branch_id;
					}
					if (!variant.equals("")) {
						StrSql += " AND (variant_name LIKE '%" + variant + "%'"
								+ " OR preownedmodel_name LIKE '%" + variant + "%'"
								+ " OR carmanuf_name LIKE '%" + variant + "%')";
					} else if (!variantid.equals("0")) {
						StrSql += " AND variant_id = " + variantid + "";
					}
					StrSql += " GROUP BY variant_id"
							+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";
					if (!variant.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("StrSql PopulateVariant===" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!variant.equals("")) {
								map.put("id", crs.getString("variant_id"));
								map.put("text", unescapehtml(crs.getString("variant_name")));
								list.add(gson.toJson(map));
							}
							if (!variantid.equals("0")) {
								output.put("text", crs.getString("variant_name"));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		doPost(request, response);
	}
}
