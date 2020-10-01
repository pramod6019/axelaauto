package axela.sales;

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

public class Executive_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String exeid = "";
	public String exe_ids = "";
	// public String id = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				// id = PadQuotes(request.getParameter("cityid"));
				exeid = PadQuotes(request.getParameter("exeid"));
				if (exeid.length() > 1) {
					StrHTML = PopulateDriver(comp_id, "0");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDriver(String comp_id, String driver_exe) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StringBuilder Str = new StringBuilder();
			if (!driver_exe.equals("0")) {

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1";
				if (!driver_exe.equals("0") && !driver_exe.equals("")) {
					StrSql += " AND emp_id IN (" + driver_exe + ")";
				}
				StrSql += " AND emp_active = 1";
				// SOP("Driver==11==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {

					Str.append("<option value=").append(crs.getString("emp_id")).append("");
					Str.append(Selectdrop(crs.getInt("emp_id"), driver_exe));
					Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
				}
				crs.close();
				return Str.toString();
			} else {

				Str.append("<option value=0>Select Driver</option>");

				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 "
						+ " AND emp_active = 1";

				if (!exeid.equals("")) {
					StrSql += " AND emp_name LIKE '%" + exeid + "%'";
				}
				// if (!id.equals("")) {
				// StrSql += " AND emp_id IN( " + id + ")";
				// }
				StrSql += " GROUP BY emp_id "
						+ " ORDER BY emp_name";
				if (!exeid.equals("")) {
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
					output.put("exeid", list);
					list.clear();
				} else {
					if (!exeid.equals("")) {
						output.put("exeid", "");
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
