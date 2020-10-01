/* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Select2 extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public CachedRowSet crs = null;
	public String filterpreowned = "";
	public String preowned_variant = "";
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject Select2(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Select2===" + input);
		}
		HttpSession session = request.getSession(true);
		// SOP("emp_uuid==enq==" + emp_uuid);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}

		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		emp_all_exe = GetSession("emp_all_exe", request);
		// emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));

		// if (!input.isNull("emp_uuid")) {
		// emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		// }
		if (!input.isNull("preowned_variant")) {
			preowned_variant = PadQuotes((String) input.get("preowned_variant"));
		}

		if (!preowned_variant.equals("")) {
			StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1";
			if (!preowned_variant.equals("")) {
				StrSql += " AND variant_name LIKE '%" + preowned_variant + "%'"
						+ " OR preownedmodel_name LIKE '%" + preowned_variant + "%'"
						+ " OR carmanuf_name LIKE '%" + preowned_variant + "%'";
				// } else if (!variantid.equals("0")) {
				// StrSql += " AND variant_id = " + variantid + "";
				// }
				StrSql = StrSql + " GROUP BY variant_id"
						+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";
				if (!preowned_variant.equals("")) {
					StrSql += " LIMIT 20";
				}
				SOP("StrSql PopulateVariant==2222=" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (!preowned_variant.equals("")) {
							map.put("variant_id", crs.getString("variant_id"));
							map.put("variant_name", unescapehtml(crs.getString("variant_name")));
							list.add(gson.toJson(map));
						}
						// if (!variantid.equals("0")) {
						// output.put("text", crs.getString("variant_name"));
						// }
					}
					if (!preowned_variant.equals("")) {
						map.clear();
						output.put("populatepreownedvariants", list);
						list.clear();
					}
				} else {
					if (!preowned_variant.equals("")) {
						output.put("variants", "");
					}

					// if (!variantid.equals("0")) {
					// output.put("text", "");
					// }
				}
				crs.close();
			}
		}

		SOP("output==========" + output);
		return output;
	}

}
