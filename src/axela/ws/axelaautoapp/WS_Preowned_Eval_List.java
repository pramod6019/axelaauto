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

public class WS_Preowned_Eval_List extends Connect {
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
	public String eval_id = "0";
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

	public JSONObject PreownedEvalList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Eval_List===" + input);
		}
		HttpSession session = request.getSession(true);
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

		if (!input.isNull("eval_id")) {
			eval_id = PadQuotes((String) input.get("eval_id"));
			StrSearch += " AND eval_id = " + eval_id + "";
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
		if (ReturnPerm(comp_id, "emp_preowned_eval_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		// emp_all_exe = GetSession("emp_all_exe", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		if (!emp_id.equals("0")) {
			if (!input.isNull("pagecurrent")) {
				pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
			}
			if (pagecurrent.equals("1")) {
				new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			}
			try {
				StrSql = "SELECT eval_id, eval_preowned_id, preownedmodel_id, preownedmodel_name, preowned_no, variant_id,"
						+ " variant_name, eval_date, preowned_id, preowned_title, emp_id,"
						// +
						// " coalesce(preowned_preownedmodel_id, 0) as preowned_preownedmodel_id, "
						+ " CONCAT(preownedmodel_name, ' (', variant_name, ')') AS item_name,"
						+ " CONCAT(preowned_enquiry_id,'') AS preowned_enquiry_id,"
						+ " preowned_sub_variant,"
						+ " fueltype_id, fueltype_name, extcolour_id, extcolour_name, intcolour_id, intcolour_name,"
						+ " preowned_options, preowned_manufyear, preowned_regdyear, preowned_regno, preowned_kms,"
						+ " preowned_fcamt, preowned_insur_date, insurance_id, insurance_name, ownership_id, ownership_name,"
						+ " preowned_invoicevalue, preowned_expectedprice, preowned_quotedprice,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
						+ " CONCAT(customer_name, ' (', customer_id, ')') AS Customer, customer_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2, preowned_branch_id,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name";

				CountSql = "SELECT COUNT(DISTINCT eval_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_eval"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
						+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype on fueltype_id = preowned_fueltype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership on ownership_id = preowned_ownership_id"

						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = eval_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_insurance on insurance_id = preowned_insurance_id"
						+ " LEFT JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"
						+ " LEFT JOIN axela_preowned_extcolour on extcolour_id = preowned_extcolour_id"
						+ " LEFT JOIN axela_preowned_intcolour on intcolour_id = preowned_intcolour_id"
						+ " WHERE 1 = 1 ";

				StrSql += SqlJoin;
				CountSql += SqlJoin;
				StrSearch += BranchAccess.replace("branch_id", "preowned_branch_id");
				StrSearch += ExeAccess.replace("emp_id", "eval_emp_id");
				if (!(StrSearch.equals(""))) {
					StrSql += StrSearch + " GROUP BY eval_id"
							+ " ORDER BY eval_id DESC";
				}
				CountSql += StrSearch;
				if (all.equals("yes")) {
					StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned_eval\\b", "FROM " + compdb(comp_id) + "axela_preowned_eval"
							+ " INNER JOIN (select eval_id from " + compdb(comp_id) + "axela_preowned_eval "
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id"
							+ " WHERE 1=1 " + StrSearch
							+ " GROUP BY eval_id order by eval_id desc) AS myresults using (eval_id)");
					// + " LIMIT " + (StartRec - 1) + ", " + recperpage + ") as myresults using (eval_id)");

					StrSql = "SELECT * from (" + StrSql + ") AS datatable"
							+ " ORDER BY eval_id desc ";
				}
				CountSql += StrSearch;
				CountSql += LimitRecords(TotalRecords, pagecurrent);
				// SOP("StrSql-----evallist----------" + StrSqlBreaker(StrSql));
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("eval_id", crs.getString("eval_id"));
						map.put("eval_preowned_id", crs.getString("eval_preowned_id"));
						map.put("preowned_enquiry_id", crs.getString("preowned_enquiry_id"));
						map.put("preowned_title", crs.getString("preowned_title"));
						map.put("preownedmodel_name", crs.getString("preownedmodel_name"));
						map.put("item_name", unescapehtml(crs.getString("item_name")));
						if (!crs.getString("preowned_sub_variant").equals("")) {
							map.put("preowned_sub_variant", crs.getString("preowned_sub_variant"));
						}
						map.put("eval_date", strToShortDate(crs.getString("eval_date")));
						map.put("customer", crs.getString("customer"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("emp_name", crs.getString("emp_name"));
						list.add(gson.toJson(map));
					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output = " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}

			// }
			// }

		}
		// SOP("output-------enquiry_list--------" + output);
		return output;
	}
}
