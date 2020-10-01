package axela.ws.axelaautoapp;
//Divya & Sangita

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Quote_List_Filter extends Connect {

	public String StrSql = "";
	public String branch_id = "0", region_id = "0";
	public String enquiry_id = "0";
	public String StrHTML = "";
	public String emp_id = "0";
	public String brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0", enquiry_region_id = "0";
	public String emp_uuid = "0";
	public String access = "";
	public String emp_all_exe = "";
	public int enquirystatusid = 0;
	public Connection conntx = null;
	public Statement stmttx = null;
	public CachedRowSet crs = null;
	public String branchcount = "0";
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Gson gson = new Gson();
	public JSONObject QuoteListFilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			if (AppRun().equals("0")) {

				// SOP("input quotelistfilter for testing ======== " + input);
			}
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			SOP("emp_id-----111111--" + emp_id);
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));

			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			branch_id = CNumeric((session.getAttribute("emp_branch_id")) + "");
			region_id = CNumeric((session.getAttribute("emp_branch_id")) + "");
			BranchAccess = GetSession("BranchAccess", request);
			// SOP("BranchAccess-----------" + BranchAccess);
			ExeAccess = GetSession("ExeAccess", request);
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));

			}
			if (!emp_id.equals("0")) {
				if (!input.isNull("brand_id")) {
					brand_id = (PadQuotes((String) input.get("brand_id")));
				}
				if (!input.isNull("region_id")) {
					region_id = (PadQuotes((String) input.get("region_id")));
				}
				if (!input.isNull("branch_id")) {
					branch_id = (PadQuotes((String) input.get("branch_id")));
				}
				if (brand_id.equals("0") && region_id.equals("0") && branch_id.equals("0")) {
					new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
				}
			}
			StrSql = "SELECT COUNT(branch_id) AS branchcount,"
					+ " IF(COUNT(branch_id) = 1, branch_id, 0) AS branch_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess;
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				branchcount = CNumeric(crs.getString("branchcount"));
				branch_id = CNumeric(crs.getString("branch_id"));
			}
			crs.close();
			output.put("branchcount", branchcount);
			if (Integer.parseInt(branchcount) > 1) {
				PopulateBrand(comp_id);
				PopulateRegion(brand_id, comp_id);
				Populatebranch1(brand_id, region_id, comp_id);
			}

			PopulateModel(brand_id, comp_id);
			PopulateBranchName(branch_id, request);
			PopulateActive();
			// PopulateStatus(comp_id);
			// PopulateStage(comp_id);
			// PopulateOpprPriority(comp_id);
			PopulateExecutive(branch_id, comp_id);
			// PopulateSOE(comp_id);

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("output---------------" + output);
		return output;
	}
	public JSONObject PopulateBrand(String comp_id) {
		CachedRowSet crs = null;
		String enquiry_item_id = "0";
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// + " ORDER BY brand_name"
			// if (!brand_id.equals("0")) {
			// StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			// }
			// StrSql += " GROUP BY region_id"
			// + " ORDER BY region_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					map.put("brand_id", crs.getString("brand_id"));
					map.put("brand_name", unescapehtml(crs.getString("brand_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebrand", list);
			// output.put("brand_id", brand_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateRegion(String brand_id, String comp_id) {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ BranchAccess;
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
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
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject Populatebranch1(String brand_id, String region_id, String comp_id) {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ BranchAccess;

			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("0")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " AND branch_branchtype_id = 1"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			crs = processQuery(StrSql, 0);
			// while (crs.next()) {
			// rs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					// count++;
					// if (count == 1) {
					// enquiry_region_id = "0";
					// }
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
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
			// }

			// crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateExecutive(String branch_id, String comp_id) {
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ " AND emp_sales='1'"
					+ " AND emp_active='1'";
			if (!branch_id.equals("0")) {
				StrSql += " AND emp_branch_id IN (" + branch_id + ")";
			}
			// StrSql += BranchAccess;
			StrSql += BranchAccess.replace("branch_id", "emp_branch_id")
					+ ExeAccess;
			StrSql += " GROUP BY emp_id ";
			StrSql += " ORDER BY emp_name ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					map.put("executive_id", crs.getString("emp_id"));
					map.put("executive_name", unescapehtml(crs.getString("emp_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexecutive", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	// public JSONObject PopulateModel(String brand_id, String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT model_id, model_name"
	// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=model_brand_id"
	// + " AND item_type_id = 1"
	// + " WHERE model_active = '1'"
	// + " AND model_sales = '1'";
	//
	// if (!brand_id.equals("0")) {
	// StrSql += " AND model_brand_id in (" + brand_id + ") ";
	// }
	// SOP("StrSql-------model----" + StrSql);
	// rs = processQuery(StrSql, 0);
	// if (crs.isBeforeFirst()) {
	// map.put("model_id", "0");
	// map.put("model_name", "Select");
	// list.add(gson.toJson(map));
	// int count = 0;
	// while (crs.next()) {
	// map.put("model_id", crs.getString("model_id"));
	// // map.put("model_name", unescapehtml(crs.getString("model_name")));
	// map.put("model_name", crs.getString("model_name"));
	// list.add(gson.toJson(map)); // Converting String to Json
	// }
	// } else {
	// map.put("model_id", "0");
	// map.put("model_name", "Select");
	// list.add(gson.toJson(map));
	// }
	// map.clear();
	// output.put("populatemodel", list);
	// // output.put("enquiry_region_id", enquiry_region_id);
	// list.clear();
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return output;
	// }
	// return output;
	// }

	public JSONObject PopulateModel(String brand_id, String comp_id) {
		CachedRowSet crs = null;
		String enquiry_region_id = "0";
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=model_brand_id"
					+ " AND item_type_id = 1"
					+ " WHERE model_active = '1'"
					+ " AND model_sales = '1'";

			if (!brand_id.equals("0")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}

			StrSql += " GROUP BY model_id"
					+ " ORDER BY model_brand_id, model_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_region_id = "0";
					}
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", unescapehtml(crs.getString("model_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemodel", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranchName(String branch_id, HttpServletRequest request) {
		CachedRowSet crs = null;
		// String BranchAccess = "";
		// StringBuilder stringval = new StringBuilder();
		// HttpSession session = request.getSession(true);
		// BranchAccess = GetSession("BranchAccess", request);
		// String comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String SqlStr = "Select branch_id, branch_name, branch_code"
					+ " from " + compdb(comp_id) + "axela_branch"
					+ " where branch_active='1' " + BranchAccess + ""
					+ " order by branch_name";
			// SOP("SqlStr===" + SqlStr);
			crs = processQuery(SqlStr, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("branch_id", crs.getString("branch_id"));
					map.put("branch_name", unescapehtml(crs.getString("branch_name")));
					map.put("branch_code", unescapehtml(crs.getString("branch_code")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebranchname", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateActive()
	{
		try {
			map.put("active_id", "");
			map.put("active_name", "Select");
			list.add(gson.toJson(map));
			map.put("active_id", "0");
			map.put("active_name", "No");
			list.add(gson.toJson(map));
			map.put("active_id", "1");
			map.put("active_name", "Yes");
			list.add(gson.toJson(map));
			map.clear();
			output.put("populateactive", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

}
