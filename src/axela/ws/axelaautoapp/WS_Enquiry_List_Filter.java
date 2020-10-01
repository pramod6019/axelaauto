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

public class WS_Enquiry_List_Filter extends Connect {

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
	public JSONObject EnquiryListFilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOP("input enquirylistfilter for testing ======== " + input);
			}
			HttpSession session = request.getSession(true);
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));
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
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));

			}
			if (!emp_id.equals("0")) {
				branch_id = CNumeric((session.getAttribute("emp_branch_id")) + "");
				region_id = CNumeric((session.getAttribute("emp_branch_id")) + "");
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				// if (access.equals("0"))
				// {
				// response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				// }
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
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
				PopulateStatus(comp_id);
				PopulateStage(comp_id);
				PopulateOpprPriority(comp_id);
				PopulateExecutive(branch_id, comp_id);
				PopulateSOE(comp_id);
				PopulateBranchName(branch_id, "", request);
				PopulateActive();
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("output---------------" + output);
		return output;
	}
	public JSONObject PopulateBrand(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
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
			SOPError("Axelaauto-App ==" + this.getClass().getName());
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
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject Populatebranch1(String brand_id, String region_id, String comp_id) {
		CachedRowSet crs = null;
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active='1' "
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
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateStatus(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT status_id, status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status " + " WHERE 1 = 1 "
					+ " ORDER BY status_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_region_id = "0";
					}
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name", unescapehtml(crs.getString("status_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatestatus", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateStage(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT stage_id, stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage "
					+ " ORDER BY stage_rank";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("stage_id", "0");
				map.put("stage_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("stage_id", crs.getString("stage_id"));
					map.put("stage_name", unescapehtml(crs.getString("stage_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("stage_id", "0");
				map.put("stage_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatestage", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateOpprPriority(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_name, priorityenquiry_duehrs "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY priorityenquiry_id"
					+ " ORDER BY priorityenquiry_rank";
			// SOP("StrSql---" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("priorityenquiry_id", "0");
				map.put("priorityenquiry_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("priorityenquiry_id", crs.getString("priorityenquiry_id"));
					map.put("priorityenquiry_name", unescapehtml(crs.getString("priorityenquiry_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("priorityenquiry_id", "0");
				map.put("priorityenquiry_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateopprpriority", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App==="
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;

		}
		return output;
	}

	public JSONObject PopulateExecutive(String branch_id, String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ " AND emp_sales='1'"
					+ " AND emp_active='1'";
			if (!branch_id.equals("0")) {
				StrSql += " AND emp_branch_id IN (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "emp_branch_id")
					+ ExeAccess;
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
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
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
	public JSONObject PopulateSOE(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("soe_id", "0");
				map.put("soe_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("soe_id", crs.getString("soe_id"));
					map.put("soe_name", unescapehtml(crs.getString("soe_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("soe_id", "0");
				map.put("soe_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatesoe", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("AxelaAuto-App==="
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return output;
	}

	public JSONObject PopulateModel(String brand_id, String comp_id) {
		CachedRowSet crs = null;
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
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranchName(String branch_id, String param, HttpServletRequest request) {
		try {
			String SqlStr = "Select branch_id, branch_name, branch_code"
					+ " from " + compdb(comp_id) + "axela_branch"
					+ " where branch_active='1' " + BranchAccess + ""
					+ " order by branch_brand_id, branch_branchtype_id, branch_name";
			crs = processQuery(SqlStr, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("branch_id", crs.getString("branch_id"));
					map.put("branch_name", crs.getString("branch_name"));
					map.put("branch_code", crs.getString("branch_code"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebranchname", list);
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
			list.clear();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

}
