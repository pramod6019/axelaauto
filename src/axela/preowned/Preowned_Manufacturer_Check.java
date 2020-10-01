package axela.preowned;

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

public class Preowned_Manufacturer_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String carmanufid = "", manufacturer = "", manufacturermodel = "", preownedmodelid = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("----------comp_id--------------");
			if (!comp_id.equals("0")) {
				carmanufid = CNumeric(PadQuotes(request.getParameter("carmanufid")));
				manufacturer = PadQuotes(request.getParameter("manufacturer"));
				preownedmodelid = CNumeric(PadQuotes(request.getParameter("preownedmodelid")));
				manufacturermodel = PadQuotes(request.getParameter("manufacturermodel"));
				// SOP("carmanufid-----" + carmanufid);
				// SOP("manufacturer-----" + manufacturer);
				// SOP("manufacturermodel-----" + manufacturermodel);
				if (manufacturer.length() > 1) {
					StrHTML = PopulateManufacturer("0");
				}

				if (!carmanufid.equals("0")) {
					StrHTML = PopulateManufacturer("0");
				}
				if (manufacturermodel.length() > 1) {
					StrHTML = PopulateManufacturerModel("0");
				}
				if (!preownedmodelid.equals("0")) {
					StrHTML = PopulateManufacturerModel("0");
				}
			}
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateManufacturer(String manufacturer_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (!manufacturer_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " carmanuf_id,"
						+ " COALESCE(carmanuf_name,'') as carmanuf_name"
						+ " FROM axela_preowned_manuf"
						+ " WHERE 1=1 "
						+ " AND carmanuf_id = " + manufacturer_id
						+ " GROUP BY carmanuf_id"
						+ " ORDER BY"
						+ " carmanuf_name";

				// SOP("StrSql PopulateManufacturer---11111------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("carmanuf_id")).append("");
						Str.append(">").append(crs.getString("carmanuf_name")).append("</option>\n");
					}
				}
				crs.close();

				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!manufacturer.equals("")) {

					Str.append("<option value=0 >").append("Select Option").append("</option>\n");

					StrSql = "SELECT carmanuf_id,"
							+ " COALESCE(carmanuf_name, '') as carmanuf_name"
							+ " FROM axela_preowned_manuf"
							+ " WHERE 1=1";
					if (!manufacturer.equals("")) {
						StrSql += " AND carmanuf_name LIKE '%" + manufacturer + "%'";

					} else if (!carmanufid.equals("0")) {
						StrSql += " AND carmanuf_id = " + carmanufid + "";
					}
					StrSql = StrSql + " GROUP BY carmanuf_id"
							+ " ORDER BY carmanuf_name";
					if (!manufacturer.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("StrSql PopulateManufacturer=22222==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!manufacturer.equals("")) {
								map.put("id", crs.getString("carmanuf_id"));
								map.put("text", crs.getString("carmanuf_name"));
								list.add(gson.toJson(map));
							}
							if (!carmanufid.equals("0")) {
								output.put("text", crs.getString("carmanuf_name"));
							}
						}
						if (!manufacturer.equals("")) {
							map.clear();
							output.put("manufacturers", list);
							list.clear();
						}
					} else {
						if (!manufacturer.equals("")) {
							output.put("manufacturers", "");
						}

						if (!carmanufid.equals("0")) {
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
	public String PopulateManufacturerModel(String preownedmodel_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {

			if (!preownedmodel_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " preownedmodel_id,"
						+ " COALESCE(CONCAT(carmanuf_name,' - ',preownedmodel_name),'') as preownedmodel_name"
						+ " FROM axela_preowned_model"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE 1=1 "
						+ " AND preownedmodel_id = " + preownedmodel_id
						+ " GROUP BY preownedmodel_id"
						+ " ORDER BY"
						+ " preownedmodel_name,"
						+ " carmanuf_name";

				// SOP("StrSql PopulateManufacturerModel-----11111----" +
				// StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
						Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
					}
				}
				crs.close();

				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!manufacturermodel.equals("")) {

					Str.append("<option value=0 >").append("Select Option").append("</option>\n");

					StrSql = "SELECT preownedmodel_id,"
							+ " COALESCE(CONCAT(carmanuf_name,' - ',preownedmodel_name),'') AS preownedmodel_name"
							+ " FROM axela_preowned_model"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE 1=1";
					if (!manufacturermodel.equals("")) {
						StrSql += " AND preownedmodel_name LIKE '%" + manufacturermodel + "%'"
								+ " OR carmanuf_name LIKE '%" + manufacturermodel + "%'";

					} else if (!preownedmodelid.equals("0")) {
						StrSql += " AND preownedmodel_id = " + preownedmodelid + "";
					}
					StrSql = StrSql + " GROUP BY preownedmodel_id"
							+ " ORDER BY preownedmodel_name,"
							+ " carmanuf_name";
					if (!manufacturermodel.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("StrSql PopulateManufacturerModel==22222=" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!manufacturermodel.equals("")) {
								map.put("id", crs.getString("preownedmodel_id"));
								map.put("text", crs.getString("preownedmodel_name"));
								list.add(gson.toJson(map));
							}
							if (!preownedmodelid.equals("0")) {
								output.put("text", crs.getString("preownedmodel_name"));
							}
						}
						if (!manufacturermodel.equals("")) {
							map.clear();
							output.put("manufacturermodels", list);
							list.clear();
						}
					} else {
						if (!manufacturermodel.equals("")) {
							output.put("manufacturermodels", "");
						}

						if (!preownedmodelid.equals("0")) {
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

	public String PopulateFuelType(String variant_fueltype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM axela_fueltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY"
					+ " fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0 >").append("Select Option").append("</option>\n");

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("fueltype_id"));
					Str.append(StrSelectdrop(crs.getString("fueltype_id"), variant_fueltype_id));
					Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
				}
			}
			crs.close();
			return Str.toString();

		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
