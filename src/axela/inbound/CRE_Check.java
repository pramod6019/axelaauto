package axela.inbound;

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

public class CRE_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String creids = "", assigninsurfollowup_cre_id = "";
	public String exe_ids = "";
	// public String id = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				// id = PadQuotes(request.getParameter("cityid"));
				creids = PadQuotes(request.getParameter("creids"));
				if (creids.length() > 1) {
					StrHTML = PopulateCreids(comp_id, "0");
				}
				exe_ids = PadQuotes(request.getParameter("exeids"));
				if (exe_ids.length() > 1) {
					StrHTML = PopulateAllExecutives(comp_id, "0");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCreids(String comp_id, String assigninsurfollowup_cre_id) {
		String[] assigninsurfollowup_cre_ids = assigninsurfollowup_cre_id.split(",");
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StringBuilder Str = new StringBuilder();
			if (!assigninsurfollowup_cre_id.equals("0")) {

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1";
				if (!assigninsurfollowup_cre_id.equals("0") && !assigninsurfollowup_cre_id.equals("")) {
					StrSql += " AND emp_id IN (" + assigninsurfollowup_cre_id + ")";
				}
				StrSql += " AND emp_active = 1"
						+ " AND emp_insur = 1";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {

					Str.append("<option value=").append(crs.getString("emp_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("emp_id"), assigninsurfollowup_cre_ids));
					Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
				}
				crs.close();
				return Str.toString();
			} else {

				Str.append("<option value=0>Select CRE</option>");

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 "
						+ " AND emp_active = 1"
						+ " AND emp_insur = 1";

				if (!creids.equals("")) {
					StrSql += " AND emp_name LIKE '%" + creids + "%'";
				}
				// if (!id.equals("")) {
				// StrSql += " AND emp_id IN( " + id + ")";
				// }
				StrSql += " GROUP BY emp_id "
						+ " ORDER BY emp_name";
				if (!creids.equals("")) {
					StrSql += " LIMIT 10";
				}

				// SOP("StrSql-ELSE====" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("id", crs.getString("emp_id"));
						map.put("text", crs.getString("emp_name"));
						list.add(gson.toJson(map));
					}
					map.clear();
					output.put("creids", list);
					list.clear();
				} else {
					if (!creids.equals("")) {
						output.put("creids", "");
					}
					// if (!id.equals("0")) {
					// output.put("text", "");
					// }
				}

				crs.close();
				return JSONPadQuotes(output.toString());
			}

		} catch (Exception ex) {
			SOPError("Axelaauto==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateAllExecutives(String comp_id, String exe_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StringBuilder Str = new StringBuilder();
			if (!exe_id.equals("0")) {

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1";
				if (!exe_id.equals("0") && !exe_id.equals("")) {
					StrSql += " AND emp_id IN (" + exe_id + ")";
				}
				StrSql += " AND emp_active = 1";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {

					Str.append("<option value=").append(crs.getString("emp_id")).append("");
					Str.append(StrSelectdrop(crs.getString("emp_id"), exe_ids));
					Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
				}
				crs.close();
				return Str.toString();
			} else {

				Str.append("<option value=0>Select Executive</option>");

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 "
						+ " AND emp_active = 1";

				if (!exe_ids.equals("")) {
					StrSql += " AND emp_name LIKE '%" + exe_ids + "%'";
				}
				StrSql += " GROUP BY emp_id "
						+ " ORDER BY emp_name";
				if (!exe_ids.equals("")) {
					StrSql += " LIMIT 10";
				}

				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("id", crs.getString("emp_id"));
						map.put("text", crs.getString("emp_name"));
						list.add(gson.toJson(map));
					}
					map.clear();
					output.put("exeids", list);
					list.clear();
				} else {
					if (!exe_ids.equals("")) {
						output.put("exeids", "");
					}
				}

				crs.close();
				return JSONPadQuotes(output.toString());
			}

		} catch (Exception ex) {
			SOPError("Axelaauto==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
