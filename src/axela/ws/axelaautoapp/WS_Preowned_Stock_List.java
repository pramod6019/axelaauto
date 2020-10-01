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

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Preowned_Stock_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String StrSearch = "", strSearch2 = "", StrSearch1 = "", Subquery = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String preowned_id = "0";
	public String stock_id = "0";
	public String so_id = "0";
	public String comp_id = "0";
	public String searchvalue = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public CachedRowSet crs = null;
	public String all = "";
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject PreownedStockList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==preownedstocklist===" + input);
		}
		HttpSession session = request.getSession(true);
		// SOP("emp_uuid==enq==" + emp_uuid);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		if (!input.isNull("all")) {
			all = PadQuotes((String) input.get("all"));
		}
		if (!input.isNull("preowned_id")) {
			preowned_id = PadQuotes((String) input.get("preowned_id"));
			StrSearch = StrSearch + " AND preowned_id = " + preowned_id + "";
		}
		if (!input.isNull("stock_id")) {
			stock_id = PadQuotes((String) input.get("stock_id"));
			StrSearch = StrSearch + " AND preownedstock_id = " + stock_id + "";
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
		if (ReturnPerm(comp_id, "emp_preowned_stock_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		emp_all_exe = GetSession("emp_all_exe", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		if (!emp_id.equals("0")) {
			if (!input.isNull("pagecurrent")) {
				pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
			}
			if (pagecurrent.equals("1")) {
				new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			}
			try {
				StrSql = "SELECT preownedstock_id,"
						+ " COALESCE(preownedstock_preowned_id,0) AS preownedstock_preowned_id,"
						+ " COALESCE(preownedstock_so_id,0) AS preownedstock_so_id,"
						+ " preownedmodel_id, preownedmodel_name, variant_id,"
						+ " preownedstock_date, preownedstatus_name, preownedtype_name, preowned_id,"
						+ " CONCAT(preownedmodel_name, ' (', variant_name, ')') AS item_name,"
						+ " preowned_extcolour_id, preowned_intcolour_id,"
						+ " preownedstock_engine_no, preownedstock_chassis_no,"
						+ " COALESCE(stock.emp_id,0) AS stockemp_id,"
						+ " coalesce(sales.emp_id,0) AS salesempid, COALESCE(concat(sales.emp_name,' (', sales.emp_ref_no, ')'),'') as salesemp_name,"
						+ " preowned_title,"
						+ " COALESCE(extcolour_id, 0) AS extcolour_id, COALESCE(extcolour_name, '') AS extcolour_name,COALESCE(intcolour_id,0) AS intcolour_id,  COALESCE(intcolour_name, '') As intcolour_name, preowned_kms,"
						+ " COALESCE(preowned_options,'') preowned_options, COALESCE(preowned_manufyear,'') preowned_manufyear,"
						+ " COALESCE(preowned_regdyear,'') preowned_regdyear, preowned_regno,"
						+ " preowned_fcamt, COALESCE(preowned_insur_date,'') preowned_insur_date, COALESCE(preowned_sub_variant,'') preowned_sub_variant,"
						+ " preowned_invoicevalue, preowned_expectedprice, preowned_quotedprice,"
						+ " CONCAT(stock.emp_name, ' (', stock.emp_ref_no, ')') AS stockemp_name, preowned_branch_id,"
						+ " CONCAT(customer_name, ' (', customer_id, ')') AS Customer, customer_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_id, COALESCE(contact_mobile1,'') contact_mobile1, COALESCE(contact_mobile2,'') contact_mobile2,"
						+ " COALESCE(contact_email1,'') contact_email1, COALESCE(contact_email2,'') contact_email2,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " COALESCE((SELECT eval_id FROM " + compdb(comp_id) + "axela_preowned_eval"
						+ " WHERE eval_preowned_id=preowned_id limit 1), 0)AS eval_id, "
						+ " COALESCE((SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_preownedstock_id = preownedstock_id limit 1), 0)AS so_preownedstock_id ";
				CountSql = "SELECT COUNT(DISTINCT preownedstock_id)";
				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock_status ON preownedstatus_id = preownedstock_status_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_type ON preownedtype_id = preownedstock_preownedtype_id"
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_fueltype ON fueltype_id = preowned_fueltype_id"
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_preowned_ownership ON ownership_id = preowned_ownership_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp stock ON stock.emp_id = preownedstock_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales on sales.emp_id = preowned_sales_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = preowned_campaign_id"
						+ " LEFT JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
						+ " LEFT JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
						+ " WHERE 1 = 1";
				StrSql += SqlJoin;
				CountSql += SqlJoin;
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY preownedstock_id"
							+ " ORDER BY preownedstock_id DESC";
				}
				// SOP("ExeAccess = " + ExeAccess);
				CountSql += StrSearch;
				StrSearch += BranchAccess.replace("branch_id", "preowned_branch_id");
				StrSearch += ExeAccess.replace("emp_id", "preownedstock_emp_id");
				if (all.equals("yes")) {
					StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned_stock\\b", "FROM " + compdb(comp_id) + "axela_preowned_stock "
							+ " INNER JOIN (select preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
							+ " WHERE 1=1 " + StrSearch
							+ " GROUP BY preownedstock_id ORDER BY preownedstock_id desc) AS myresults using (preownedstock_id)");

					StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
							+ " ORDER BY preownedstock_id DESC";

				}
				CountSql += LimitRecords(TotalRecords, pagecurrent);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// SOP("StrSql===========" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						i++;
						map.put("preownedstock_id", crs.getString("preownedstock_id"));
						map.put("preownedstock_preowned_id", crs.getString("preownedstock_preowned_id"));
						map.put("eval_id", crs.getString("eval_id"));
						map.put("so_preownedstock_id", crs.getString("so_preownedstock_id"));
						map.put("item_name", unescapehtml(crs.getString("item_name")));
						map.put("preownedstock_date", crs.getString("preownedstock_date"));
						map.put("extcolour_name", crs.getString("extcolour_name"));
						map.put("intcolour_name", crs.getString("intcolour_name"));
						map.put("preownedstock_date", crs.getString("preownedstock_date"));
						map.put("preownedstatus_name", crs.getString("preownedstatus_name"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("salesemp_name", crs.getString("stockemp_name"));
						list.add(gson.toJson(map));

					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output ======preownedstocklist========== " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}
}
