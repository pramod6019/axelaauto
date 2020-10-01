/* Ved Prakash (12th Sept 2013) */
package axela.ws.cust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_Model_ItemData extends ConnectWS {

	public String msg = "";
	public String StrSql = "";
	public String update = "";
	public String branch_id = "";
	public String comp_id = "0";
	public String model_id = "";
	public String state_id = "";
	public String emp_uuid = "0";
	public Object team_id = "";
	public String search = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_Model_Item(JSONObject input) {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("model_id")) {
				model_id = CNumeric(PadQuotes((String) input.get("model_id")));
				if (!model_id.equals("0")) {
					PopulateModelItem(input);
				} else {
					output.put("msg", "Select Model!");
				}
			}
			if (!input.isNull("branch_id") || !input.isNull("team_id")) {
				branch_id = CNumeric(PadQuotes((String) input.get("branch_id")));
				team_id = CNumeric(PadQuotes((String) input.get("team_id")));
				if (!branch_id.equals("0") || !team_id.equals("0")) {
					PopulateExecutive(input);
				}
			}

			if (!input.isNull("search")) {
				search = PadQuotes((String) input.get("search"));
				if (search.equals("no")) {
					PopulateModel();
					PopulateItem();
					PopulateStatus();
					PopulatePriority();
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

	public JSONObject PopulateModelItem(JSONObject input) {
		CachedRowSet crs = null;
		String enquiry_item_id = "0";
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1'"
					+ " AND item_model_id = " + model_id;
			StrSql += " ORDER BY item_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_item_id = crs.getString("item_id");
					}
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateitem", list);
			output.put("enquiry_item_id", enquiry_item_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateExecutive(JSONObject input) {
		CachedRowSet crs = null;
		try {
			if (!input.isNull("branch_id")) {
				branch_id = CNumeric(PadQuotes((String) input.get("branch_id")));
			}
			if (!input.isNull("team_id")) {
				team_id = CNumeric(PadQuotes((String) input.get("team_id")));
			}
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1 AND emp_sales = '1' AND emp_active = '1' AND (emp_branch_id = " + branch_id + " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id = " + branch_id + "))";
			if (!team_id.equals("0")) {
				StrSql += " AND emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id = " + team_id + ")";
			}
			StrSql += " ORDER BY emp_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateModel() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " WHERE 1 = 1"
					+ " AND model_active = '1'"
					+ " AND item_type_id = 1"
					+ " AND item_active = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("PopulateModel SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", unescapehtml(crs.getString("model_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
			}
			crs.close();
			map.clear();
			output.put("populatemodel", list);
			list.clear();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1'";
			StrSql += " GROUP BY item_id"
					+ " ORDER BY item_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
			}
			crs.close();
			map.clear();
			output.put("populateitem", list);
			list.clear();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateStatus() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
					+ " WHERE 1 = 1 "
					+ " GROUP BY status_id"
					+ " ORDER BY status_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name", unescapehtml(crs.getString("status_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			crs.close();
			output.put("populatestatus", list);
			list.clear();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulatePriority() {
		CachedRowSet crs = null;
		try {
			StrSql = "select priorityenquiry_id, priorityenquiry_desc, priorityenquiry_duehrs "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " where 1 = 1 "
					+ " group by priorityenquiry_id"
					+ " order by priorityenquiry_rank";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("priority_id", "0");
				map.put("priority_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("priority_id", crs.getString("priorityenquiry_id"));
					map.put("priority_desc", unescapehtml(crs.getString("priorityenquiry_desc")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("priority_id", "0");
				map.put("priority_desc", "Select");
				list.add(gson.toJson(map));
			}
			crs.close();
			map.clear();
			output.put("populatepriority", list);
			list.clear();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
