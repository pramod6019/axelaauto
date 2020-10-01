//* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.codec.binary.Base64;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Preowned_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String enqstatus = "";
	public String strSearch = "", Subquery = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String role_id = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String model_id = "0";
	public String enquiry_id = "0";
	public String filterpreowned_id = "0";
	public String item_id = "0";
	public String status_id = "0";
	public String priority_id = "0";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchvalue = "";
	public String model_ids = "0";
	public String status_ids = "0";
	public String stage_ids = "0";
	public String prioritypreowned_ids = "0";
	public String emp_ids = "0";
	public String soe_ids = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public CachedRowSet crs = null;
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String executive_id = "0";
	public String filter = "";
	public String strsearch = "";
	public String brand_ids = "0";
	public String region_ids = "0";
	public String branch_ids = "0";
	public String sales = "";
	public String executive_ids = "0";
	public String filterpreowned_preowned_id = "0";
	// public String filterenquiry_dms_no = "0";
	public String filterpreowned_branch_id = "0";
	public String filterpreowned_customer_id = "0";
	public String filterpreowned_contact_id = "0";
	public String filterpreowned_customer_name = "";
	public String filterpreowned_contact_name = "";
	public String filterpreowned_contact_mobile = "";
	public String filterpreowned_contact_email = "";
	public String filterpreowned_branch_name = "";
	public String filterpreowned_variant_id = "";
	public String carmanuf_ids = "0";
	public String preownedmodel_ids = "0";
	public String filterpreowned_enquiry_id = "0";
	public String variant_ids = "0";
	public String all = "";
	public String filterQuery = "";

	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject PreownedList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Preowned_List===" + input);
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
		if (ReturnPerm(comp_id, "emp_preowned_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		// emp_all_exe = GetSession("emp_all_exe", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		if (!input.isNull("filter")) {
			filter = PadQuotes((String) input.get("filter"));
		}
		if (filter.equals("yes")) {
			strSearch = ProcessFilter(request, input);
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!input.isNull("sales")) {
			sales = PadQuotes((String) input.get("sales"));
		}
		if (!input.isNull("all")) {
			all = PadQuotes((String) input.get("all"));
		}
		if (!input.isNull("filterquery")) {
			filterQuery = JSONPadQuotes((String) input.get("filterquery"));
			if (!filterQuery.equals("")) {
				filterQuery = new String(Base64.decodeBase64(filterQuery.getBytes("ISO-8859-1")));
			}
		}
		if (!emp_id.equals("0")) {
			try {
				StrSql = "SELECT preowned_id, CONCAT('PRE',branch_code,preowned_no) AS preowned_no,"
						+ " preowned_title, preowned_date, preowned_close_date,"
						+ " COALESCE(preowned_enquiry_id, 0) AS preowned_enquiry_id,"
						+ " preowned_sub_variant,"
						+ " preowned_options, preowned_manufyear, preowned_regdyear, preowned_regno, preowned_kms,"
						+ " customer_id, customer_name,"
						+ " contact_id, concat(title_desc,' ',contact_fname,' ', contact_lname) AS contact_name,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(soe_name,'') AS soe_name,"
						+ " COALESCE(sob_name,'') AS sob_name,"
						+ " COALESCE(campaign_id,'') AS campaign_id,"
						+ " COALESCE(campaign_name,'') AS campaign_name,"
						+ " COALESCE(preowned.emp_id,0) AS preownedempid,"
						+ " COALESCE(CONCAT(preowned.emp_name,' (', preowned.emp_ref_no, ')'),'') AS preownedemp_name, "
						+ " CONCAT(preownedmodel_name, ' ', variant_name) AS item_name, "
						+ " preownedstatus_name,"
						+ " COALESCE(sales.emp_id,0) AS salesempid,"
						+ " COALESCE(CONCAT(sales.emp_name,' (', sales.emp_ref_no, ')'),'') AS salesemp_name,"
						+ " branch_id, branch_code, concat(branch_name,' (', branch_code, ')') AS branchname,"
						+ " preowned_refno, preowned_desc, "
						+ " COALESCE((SELECT preownedstock_id"
						+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ " WHERE preownedstock_preowned_id=preowned_id limit 1), 0)AS stock_id,"
						+ " COALESCE((select eval_id FROM " + compdb(comp_id) + "axela_preowned_eval"
						+ " WHERE eval_preowned_id=preowned_id limit 1), 0)AS eval_id ";

				CountSql = " SELECT Count(distinct(preowned_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp preowned ON preowned.emp_id = preowned_emp_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_status ON preownedstatus_id = preowned_preownedstatus_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = preowned_campaign_id"
						+ " WHERE 1 = 1 ";
				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin + filterQuery;
				if (sales.equals("yes")) {
					if (!ExeAccess.equals("")) {
						strSearch += ExeAccess.replace("emp_id", "preowned_sales_emp_id");
					} else {
						strSearch += " AND preowned_sales_emp_id =" + emp_id;
					}
				} else {
					if (!ExeAccess.equals("")) {
						strSearch += ExeAccess.replace("emp_id", "preowned_emp_id");
					}
					if (!ExeAccess.equals("")) {
						strSearch += ExeAccess.replace("emp_id", "preowned_emp_id");
					}
				}
				if (all.equals("yes")) {
					StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned\\b", "FROM " + compdb(comp_id) + "axela_preowned "
							+ " INNER JOIN (SELECT preowned_id FROM " + compdb(comp_id) + "axela_preowned "
							+ " WHERE 1=1 " + strSearch + ""
							+ " GROUP BY preowned_id"
							+ " ORDER BY preowned_id desc) AS myresults USING (preowned_id)");
					StrSql = "SELECT * FROM (" + StrSql + ") AS datatable ";
				} else {
					StrSql += strSearch;
				}
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				StrSql = StrSql + " GROUP BY preowned_id"
						+ " ORDER BY preowned_id DESC "
						+ LimitRecords(TotalRecords, pagecurrent);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				SOP("StrSql=========" + CountSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						i++;
						map.put("preowned_id", crs.getString("preowned_id"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("item_name", crs.getString("item_name"));
						// map.put("variant_name", crs.getString("variant_name"));
						map.put("preowned_sub_variant", crs.getString("preowned_sub_variant"));
						map.put("preownedstatus_name", crs.getString("preownedstatus_name"));
						map.put("preownedemp_name", crs.getString("preownedemp_name"));
						map.put("salesemp_name", crs.getString("salesemp_name"));
						map.put("preowned_date", strToShortDate(crs.getString("preowned_date")));
						map.put("preowned_enquiry_id", crs.getString("preowned_enquiry_id"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("eval_id", crs.getString("eval_id"));
						map.put("stock_id", crs.getString("stock_id"));
						list.add(gson.toJson(map)); // Converting String to json
					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					// SOP("output = " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}

			// }
			// }

		}
		// SOP("output-------WS_preowned_list--------" + output);
		return output;
	}
	private String ProcessFilter(HttpServletRequest request, JSONObject input) {
		StringBuilder str = new StringBuilder();
		try {
			if (!input.isNull("brand_ids")) {
				brand_ids = PadQuotes((String) input.get("brand_ids"));
			}
			if (!input.isNull("region_ids")) {
				region_ids = PadQuotes((String) input.get("region_ids"));
			}
			if (!input.isNull("branch_ids")) {
				branch_ids = PadQuotes((String) input.get("branch_ids"));
			}
			if (!input.isNull("status_ids")) {
				status_ids = PadQuotes((String) input.get("status_ids"));
			}
			if (!input.isNull("carmanuf_ids")) {
				carmanuf_ids = PadQuotes((String) input.get("carmanuf_ids"));
			}
			if (!input.isNull("preownedmodel_ids")) {
				preownedmodel_ids = PadQuotes((String) input.get("preownedmodel_ids"));
			}
			if (!input.isNull("variant_ids")) {
				variant_ids = PadQuotes((String) input.get("variant_ids"));
			}
			if (!input.isNull("prioritypreowned_ids")) {
				prioritypreowned_ids = PadQuotes((String) input.get("prioritypreowned_ids"));
			}
			if (!input.isNull("txt_preowned_variant_id")) {
				filterpreowned_variant_id = PadQuotes((String) input.get("txt_preowned_variant_id"));
			}
			if (!input.isNull("soe_ids")) {
				soe_ids = PadQuotes((String) input.get("soe_ids"));
			}
			if (!input.isNull("executive_ids")) {
				executive_ids = PadQuotes((String) input.get("executive_ids"));
			}
			if (!input.isNull("txt_preowned_id")) {
				filterpreowned_id = CNumeric(PadQuotes((String) input.get("txt_preowned_id")));
			}
			if (!input.isNull("txt_enquiry_id")) {
				filterpreowned_enquiry_id = CNumeric(PadQuotes((String) input.get("txt_enquiry_id")));
			}
			if (!input.isNull("dr_branch_name")) {
				filterpreowned_branch_name = PadQuotes((String) input.get("dr_branch_name"));
			}
			if (!input.isNull("txt_customer_id")) {
				filterpreowned_customer_id = CNumeric(PadQuotes((String) input.get("txt_customer_id")));
			}
			if (!input.isNull("txt_contact_id")) {
				filterpreowned_contact_id = CNumeric(PadQuotes((String) input.get("txt_contact_id")));
			}
			if (!input.isNull("txt_customer_name")) {
				filterpreowned_customer_name = PadQuotes((String) input.get("txt_customer_name"));
			}
			if (!input.isNull("txt_contact_name")) {
				filterpreowned_contact_name = PadQuotes((String) input.get("txt_contact_name"));
			}
			if (!input.isNull("txt_contact_mobile")) {
				filterpreowned_contact_mobile = CNumeric(PadQuotes((String) input.get("txt_contact_mobile")));
			}
			if (!input.isNull("txt_contact_email")) {
				filterpreowned_contact_email = PadQuotes((String) input.get("txt_contact_email"));
			}
			if (!brand_ids.equals("0"))
			{
				strSearch += " AND branch_brand_id IN(" + brand_ids + ")";
			}
			if (!region_ids.equals("0"))
			{
				strSearch += " AND branch_region_id IN(" + region_ids + ")";
			}
			if (!branch_ids.equals("0"))
			{
				strSearch += " AND branch_id IN(" + branch_ids + ")";
			}
			if (!executive_ids.equals("0"))
			{
				strSearch += " AND preowned_emp_id IN(" + executive_ids + ")";
			}
			if (!status_ids.equals("0"))
			{
				strSearch += " AND preowned_preownedstatus_id IN(" + status_ids + ")";
			}
			if (!carmanuf_ids.equals("0"))
			{
				strSearch += " AND carmanuf_id IN(" + carmanuf_ids + ")";
			}
			if (!preownedmodel_ids.equals("0"))
			{
				strSearch += " AND preownedmodel_id IN(" + preownedmodel_ids + ")";
			}
			if (!variant_ids.equals("0"))
			{
				strSearch += " AND variant_id IN(" + variant_ids + ")";
			}

			// if (!stage_ids.equals("0"))
			// {
			// strSearch += " AND enquiry_stage_id IN(" + stage_ids + ")";
			// }

			if (!prioritypreowned_ids.equals("0"))
			{
				strSearch += " AND preowned_prioritypreowned_id IN(" + prioritypreowned_ids + ")";
			}
			if (!soe_ids.equals("0"))
			{
				strSearch += " AND preowned_soe_id IN(" + soe_ids + ")";
			}
			if (!filterpreowned_id.equals("0"))
			{
				strSearch += " AND preowned_id=" + filterpreowned_id;
			}
			if (!filterpreowned_enquiry_id.equals("0"))
			{
				strSearch += " AND preowned_enquiry_id=" + filterpreowned_enquiry_id;
			}
			// if (!filterenquiry_no.equals("0"))
			// {
			// strsearch += " AND enquiry_no=" + filterenquiry_no;
			// }

			// if (!filterenquiry_dms_no.equals("0"))
			// {
			// strSearch += " AND enquiry_dmsno='" + filterenquiry_dms_no + "'";
			// }

			// if (!filterenquiry_branch_id.equals("0") || filterenquiry_branch_id.equals(""))
			// {
			// strSearch2 += " AND branch_name='" + filterenquiry_branch_id + "'";
			// }

			if (!filterpreowned_branch_name.equals(""))
			{
				strSearch += " AND branch_name='" + filterpreowned_branch_name + "'";
			}
			if (!filterpreowned_customer_id.equals("0"))
			{
				strSearch += " AND preowned_customer_id=" + filterpreowned_customer_id;
			}
			if (!filterpreowned_contact_id.equals("0"))
			{
				strSearch += " AND preowned_contact_id=" + filterpreowned_contact_id;
			}
			if (!filterpreowned_customer_name.equals(""))
			{
				strSearch += " AND customer_name  LiKE '%" + filterpreowned_customer_name + "%'";
			}
			if (!filterpreowned_contact_name.equals(""))
			{
				// strSearch += " AND (contact_fname='" + filterpreowned_contact_name + "'" + "OR contact_fname='" + filterpreowned_contact_name + "')";
				strSearch += " AND (contact_fname  LiKE '%" + filterpreowned_contact_name + "%'"
						+ " OR contact_lname  LiKE '%" + filterpreowned_contact_name + "%')";
			}
			if (!filterpreowned_contact_mobile.equals(""))
			{
				strSearch += " AND (contact_mobile1='" + filterpreowned_contact_mobile + "'"
						+ " OR contact_mobile2='" + filterpreowned_contact_mobile + "'" + ")";
			}
			if (!filterpreowned_contact_email.equals(""))
			{
				strSearch += " AND (contact_email1='" + filterpreowned_contact_email + "'"
						+ " OR contact_email1='" + filterpreowned_contact_email + "'" + ")";
			}
			if (!filterpreowned_variant_id.equals(""))
			{
				strSearch += " AND preowned_variant_id='" + filterpreowned_variant_id + "'";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		SOP("strSearch=======================================" + strSearch);
		return strSearch;
	}
}
