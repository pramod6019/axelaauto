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

public class Preowned_Variant_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String city = "", variant = "", variantid = "", currentvehicle = "", currentvehiclejlr = "", othermodelsjlr = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("----------comp_id--------------");
			if (!comp_id.equals("0")) {
				city = PadQuotes(request.getParameter("city"));
				variantid = CNumeric(PadQuotes(request.getParameter("variantid")));
				variant = PadQuotes(request.getParameter("variant"));
				currentvehicle = PadQuotes(request.getParameter("currentvehicle"));
				currentvehiclejlr = PadQuotes(request.getParameter("currentvehiclejlr"));
				othermodelsjlr = PadQuotes(request.getParameter("othermodelsjlr"));
				// SOP("variantid-----" + variantid);
				// SOP("variant-----" + variant);
				// SOP("currentvehicle-----" + currentvehicle);
				// SOP("currentvehiclejlr-----" + currentvehiclejlr);
				// SOP("othermodelsjlr-----" + othermodelsjlr);
				if (variant.length() > 1 && !currentvehicle.equals("yes")) {
					StrHTML = PopulateVariant("0");
				}

				if (!variantid.equals("0")) {
					StrHTML = PopulateVariant("0");
				}
				if (currentvehicle.equals("yes")) {
					StrHTML = PopulatePorscheOtherVehicle("0", comp_id);
				}
				if (currentvehiclejlr.equals("yes")) {
					StrHTML = PopulateJLRCurrentCars("0", comp_id);
				}
				if (othermodelsjlr.equals("yes")) {
					StrHTML = PopulateJLROtherModel("0", comp_id);
				}
			}
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateVariant(String variant_id) {

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
						+ " WHERE 1 = 1 "
						+ " AND variant_id = " + variant_id
						+ " GROUP BY variant_id"
						+ " ORDER BY"
						+ " carmanuf_name,"
						+ " preownedmodel_name,"
						+ " variant_name";

				// SOP("StrSql PopulateVariant---------" + StrSql);
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

					StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE 1=1";
					if (!variant.equals("")) {
						StrSql += " AND variant_name LIKE '%" + variant + "%'"
								+ " OR preownedmodel_name LIKE '%" + variant + "%'"
								+ " OR carmanuf_name LIKE '%" + variant + "%'";
					} else if (!variantid.equals("0")) {
						StrSql += " AND variant_id = " + variantid + "";
					}
					StrSql = StrSql + " GROUP BY variant_id"
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

	public String PopulatePorscheOtherVehicle(String enquiry_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {

			if (!enquiry_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " variant_id,"
						+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) AS variant_name"
						+ " FROM axela_preowned_variant"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE 1 = 1 "
						+ " AND currentcars_enquiry_id = " + enquiry_id
						+ " GROUP BY variant_id"
						+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";

				// SOP("StrSql PopulateVariant---------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("variant_id")).append(" selected");
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

					StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE 1=1";
					if (!variant.equals("")) {
						StrSql += " AND variant_name LIKE '%" + variant + "%'"
								+ " OR preownedmodel_name LIKE '%" + variant + "%'"
								+ " OR carmanuf_name LIKE '%" + variant + "%'";
					} else if (!variantid.equals("0")) {
						StrSql += " AND variant_id = " + variantid + "";
					}
					StrSql = StrSql + " GROUP BY variant_id"
							+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";
					if (!variant.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("PopulateVariant===" + StrSql);
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

	public String PopulateJLRCurrentCars(String enquiry_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {

			if (!enquiry_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " variant_id,"
						+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) AS variant_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " INNER JOIN axela_preowned_variant ON variant_id = currentcars_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE 1 = 1 "
						+ " AND currentcars_enquiry_id = " + enquiry_id
						+ " GROUP BY variant_id"
						+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";

				// SOP("StrSql PopulateVariant---------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("variant_id")).append(" selected");
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

					StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE 1=1";
					if (!variant.equals("")) {
						StrSql += " AND variant_name LIKE '%" + variant + "%'"
								+ " OR preownedmodel_name LIKE '%" + variant + "%'"
								+ " OR carmanuf_name LIKE '%" + variant + "%'";
					} else if (!variantid.equals("0")) {
						StrSql += " AND variant_id = " + variantid + "";
					}
					StrSql = StrSql + " GROUP BY variant_id"
							+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";
					if (!variant.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("PopulateVariant===" + StrSql);
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

	public String PopulateJLROtherModel(String enquiry_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();

		try {

			if (!enquiry_id.equals("0")) {

				// Get's executed while populating the Models

				StrSql = "SELECT"
						+ " preownedmodel_id,"
						+ " CONCAT(carmanuf_name,' - ',preownedmodel_name) as preownedmodel_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_othermodels"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = othermodels_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE 1 = 1 "
						+ " AND othermodels_enquiry_id = " + enquiry_id
						+ " GROUP BY preownedmodel_id"
						+ " ORDER BY preownedmodel_name";

				// SOP("StrSql PopulateModel---------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("preownedmodel_id")).append(" selected");
						Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
					}
				}
				crs.close();

				return Str.toString();
			} else {

				// Gets executed when request comes through ajax call
				// Create JSon object and set the values for the select2
				if (!variant.equals("")) {

					Str.append("<option value=0 >").append("Select Option").append("</option>\n");

					StrSql = "SELECT preownedmodel_id, CONCAT(carmanuf_name,' - ',preownedmodel_name) AS preownedmodel_name"
							+ " FROM axela_preowned_model"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE 1 = 1";
					if (!variant.equals("")) {
						StrSql += " AND preownedmodel_name LIKE '%" + variant + "%'"
								+ " OR carmanuf_name LIKE '%" + variant + "%'";
					} else if (!variantid.equals("0")) {
						StrSql += " AND preownedmodel_id = " + variantid + "";
					}
					StrSql = StrSql + " GROUP BY preownedmodel_id"
							+ " ORDER BY carmanuf_name, preownedmodel_name";
					if (!variant.equals("")) {
						StrSql += " LIMIT 20";
					}
					// SOP("PopulateVariant===" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!variant.equals("")) {
								map.put("id", crs.getString("preownedmodel_id"));
								map.put("text", unescapehtml(crs.getString("preownedmodel_name")));
								list.add(gson.toJson(map));
							}
							if (!variantid.equals("0")) {
								output.put("text", crs.getString("preownedmodel_name"));
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
