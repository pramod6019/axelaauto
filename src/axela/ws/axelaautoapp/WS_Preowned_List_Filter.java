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

public class WS_Preowned_List_Filter extends Connect {

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
	public String preownedmodel_id = "0", carmanuf_id = "0", variant_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public CachedRowSet crs = null;
	public String branchcount = "0";
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Gson gson = new Gson();
	public JSONObject PreownedListFilter(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			if (AppRun().equals("0")) {

				SOP("input preownedlistfilter for testing ======== " + input);
			}
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));
			}
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			// emp_id = CNumeric(session.getAttribute("emp_id") + "");
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
				if (!input.isNull("carmanuf_id")) {
					carmanuf_id = (PadQuotes((String) input.get("carmanuf_id")));
				}
				if (!input.isNull("preownedmodel_id")) {
					preownedmodel_id = (PadQuotes((String) input.get("preownedmodel_id")));
				}
				if (!input.isNull("variant_id")) {
					variant_id = (PadQuotes((String) input.get("variant_id")));
				}
				StrSql = "SELECT COUNT(branch_id) AS branchcount,"
						+ " IF(COUNT(branch_id) = 1, branch_id, 0) AS branch_id"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_active = 1"
						+ " AND branch_branchtype_id = 2"
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
				PopulateStatus(comp_id);
				PopulatePreownedPriority(comp_id);
				PopulateExecutive(branch_id, comp_id);
				PopulateSOE(comp_id);
				PopulateBranchName(branch_id, "", request);
				PopulatePreownedManufacturer(comp_id);
				if (!carmanuf_id.equals("0")) {
					PopulatePreownedModel(comp_id, carmanuf_id);
				}
				if (!preownedmodel_id.equals("0") && !carmanuf_id.equals("0")) {
					PopulatePreownedVariant(comp_id, carmanuf_id, preownedmodel_id);
				}
				if (brand_id.equals("0") && region_id.equals("0") && branch_id.equals("0") && preownedmodel_id.equals("0") && carmanuf_id.equals("0") && variant_id.equals("0")) {
					new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("output------preowned-filter---------" + output);
		return output;
	}
	public JSONObject PopulateBrand(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
					+ BranchAccess
					+ " AND branch_branchtype_id=2"
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
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ BranchAccess;
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " AND branch_branchtype_id=2";
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
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
			StrSql += " AND branch_branchtype_id = 2";
			StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
	public JSONObject PopulateStatus(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT preownedstatus_id, preownedstatus_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_status "
					+ " WHERE 1 = 1 "
					+ " ORDER BY preownedstatus_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("status_id", crs.getString("preownedstatus_id"));
					map.put("status_name", unescapehtml(crs.getString("preownedstatus_name")));
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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulatePreownedPriority(String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT prioritypreowned_id, prioritypreowned_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY prioritypreowned_id"
					+ " ORDER BY prioritypreowned_name";
			// SOP("StrSql---" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("prioritypreowned_id", "0");
				map.put("prioritypreowned_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("prioritypreowned_id", crs.getString("prioritypreowned_id"));
					map.put("prioritypreowned_name", unescapehtml(crs.getString("prioritypreowned_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("prioritypreowned_id", "0");
				map.put("prioritypreowned_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatepreownedpriority", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App==="
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
			StrSql += " GROUP BY emp_id ";
			StrSql += " ORDER BY emp_name ";
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
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App==="
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return output;
	}

	public JSONObject PopulateBranchName(String branch_id, String param, HttpServletRequest request) {
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active='1'"
					+ " AND branch_branchtype_id=2"
					+ BranchAccess + ""
					+ " ORDER BY branch_name";
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
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulatePreownedManufacturer(String comp_id) {
		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1"
					+ " GROUP BY carmanuf_id"
					+ " ORDER BY carmanuf_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("carmanuf_id", "0");
				map.put("carmanuf_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("carmanuf_id", crs.getString("carmanuf_id"));
					map.put("carmanuf_name", crs.getString("carmanuf_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("carmanuf_id", "0");
				map.put("carmanuf_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatepreownedmanufacturer", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulatePreownedModel(String comp_id, String carmanuf_id) {
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1";
			if (!carmanuf_id.equals("0")) {
				StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			}
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("preownedmodel_id", "0");
				map.put("preownedmodel_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("preownedmodel_id", crs.getString("preownedmodel_id"));
					map.put("preownedmodel_name", crs.getString("preownedmodel_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("preownedmodel_id", "0");
				map.put("preownedmodel_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatepreownedmodel", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulatePreownedVariant(String comp_id, String carmanuf_id, String preownedmodel_id) {
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1";
			if (!carmanuf_id.equals("0")) {
				StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			}
			if (!preownedmodel_id.equals("0")) {
				StrSql += " AND preownedmodel_id IN(" + preownedmodel_id + ")";
			}
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("variant_id", "0");
				map.put("variant_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("variant_id", crs.getString("variant_id"));
					map.put("variant_name", unescapehtml(crs.getString("variant_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("variant_id", "0");
				map.put("variant_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatepreownedvariant", list);
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
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
