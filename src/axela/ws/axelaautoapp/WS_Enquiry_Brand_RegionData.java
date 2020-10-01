/* Ved Prakash (12th Sept 2013) */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_Brand_RegionData extends ConnectWS {

	public String msg = "";
	public String StrSql = "";
	public String update = "";
	public String branch_id = "";
	public String region_id = "";
	public String comp_id = "0";
	public String brand_id = "";
	public String model_id = "";
	public String state_id = "";
	public String emp_uuid = "0";
	public Object team_id = "";
	public String search = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject enquiry_Brand_Region(JSONObject input, HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		if (emp_all_exe.equals("1"))
		{
			ExeAccess = "";
		}
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				// SOP("comp_id---" + comp_id);
			}

			if (!input.isNull("emp_uuid")) {
				emp_uuid = (PadQuotes((String) input.get("emp_uuid")));
				// SOP("emp_uuid---" + emp_uuid);
			}
			CheckAppSession(emp_uuid, comp_id, request);
			BranchAccess = GetSession("BranchAccess", request);
			// SOP("BranchAccess-------" + BranchAccess);
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = GetSession("emp_all_exe", request);
			emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));

			if (!input.isNull("brand_id")) {
				brand_id = CNumeric(PadQuotes((String) input.get("brand_id")));
				// SOP("brand_id==========" + brand_id);
				if (!brand_id.equals("0")) {
					PopulateRegion(input, brand_id);
					PopulateBranch1(input, brand_id, region_id, comp_id);
				}
			}

			if (!input.isNull("region_id")) {
				region_id = CNumeric(PadQuotes((String) input.get("brand_id")));
				if (!region_id.equals("0")) {
					PopulateBranch1(input, brand_id, region_id, comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	public JSONObject PopulateRegion(JSONObject input, String brand_id) {
		CachedRowSet crs = null;
		String enquiry_item_id = "0";
		try {
			if (!input.isNull("region_id")) {
				region_id = CNumeric(PadQuotes((String) input.get("region_id")));
			}
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			// SOP("StrSql=======PopulateRegion======" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_item_id = crs.getString("region_id");
					}
					map.put("region_id", crs.getString("region_id"));
					map.put("region_name", unescapehtml(crs.getString("region_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateregion", list);
			output.put("region_id", region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranch1(JSONObject input, String brand_id, String region_id, String comp_id) {
		CachedRowSet crs = null;
		String enquiry_item_id = "0";
		try {
			if (!input.isNull("region_id")) {
				region_id = CNumeric(PadQuotes((String) input.get("region_id")));
			}
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active='1' "
					+ BranchAccess;

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("StrSql=======PopulateBranch1======" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_item_id = crs.getString("branch_id");
					}
					map.put("branch_id", crs.getString("branch_id"));
					map.put("branch_name", unescapehtml(crs.getString("branch_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebranch", list);
			output.put("branch_id", branch_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
