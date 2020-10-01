package axela.ws.axelaautoapp;

//divya 26th march 2014

import java.text.DecimalFormat;
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

public class WS_Veh_Quote_List extends Connect {
	public int i = 0;
	public static int total = 0;
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String strSearch2 = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String quote_id = "0";
	public String enquiry_id = "0";
	public String config_sales_enquiry_refno = "";
	public String populate = "";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchvalue = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public String StrSearch1 = "";
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String executive_id = "0";
	public String branch_id = "0";
	public String filterquote_branch_name = "";
	public String strsearch = "";
	public String brand_ids = "0";
	public String region_ids = "0";
	public String branch_ids = "0";
	public String executive_ids = "0";
	public String model_ids = "0";
	// String filterenquiry_no =
	// CNumeric(PadQuotes((String) input.get("txt_enquiry_no")));

	public String filter = "";
	public String filterquote_id = "0";
	public String filterquote_no = "0";
	public String filterquote_from_date = "";
	public String filterquote_to_date = "";
	public String filterquote_autorized_from_date = "";
	public String filterquote_autorized_to_date = "";
	public String filterquote_branch_id = "0";
	public String filterquote_customer_name = "";
	public String filterquote_customer_id = "0";
	public String filterquote_contact_id = "0";
	public String filterquote_contact_name = "";
	public String filterquote_contact_mobile = "";
	public String filterquote_contact_email = "";
	public String filterquote_enquiry_id = "0";
	public String filterquote_active = "0";

	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject QuoteList(JSONObject input, @Context HttpServletRequest request) throws Exception {

		if (AppRun().equals("0")) {
			SOP("input ====WS_Veh_Quote_List======= " + input);
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		HttpSession session = request.getSession(true);
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
		SOP("ReturnPerm===" + ReturnPerm(comp_id, "emp_quote_access", request));
		if (ReturnPerm(comp_id, "emp_sales_quote_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!emp_id.equals("0")) {
			if (!input.isNull("filter")) {
				filter = PadQuotes((String) input.get("filter"));
			}
			if (filter.equals("yes")) {
				strSearch2 = ProcessFilter(request, input);
			}
			if (!input.isNull("quote_id")) {
				quote_id = CNumeric(PadQuotes((String) input.get("quote_id")));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!brand_id.equals("0")) {
				StrSearch1 += " AND branch_brand_id = " + brand_id + "";
			}
			if (!region_id.equals("0")) {
				StrSearch1 += " AND branch_region_id = " + region_id + "";
			}
			if (!branch_id.equals("0")) {
				StrSearch1 += " AND branch_id = " + branch_id + "";
			}
			if (!team_id.equals("0")) {
				StrSearch1 += " AND teamtrans_team_id = " + team_id + "";
			}
			if (!executive_id.equals("0")) {
				StrSearch1 += " AND teamtrans_emp_id = " + executive_id + "";
			}
			if (!BranchAccess.equals("")) {
				StrSearch1 += BranchAccess;
			}
			if (!emp_id.equals("0")) {
				PopulateConfigDetails();
				output.put("config_sales_enquiry_refno", config_sales_enquiry_refno);
				// if (!populate.equals("yes")) {
				try {
					StrSql = "SELECT quote_id, quote_branch_id, quote_no, so_no,"
							+ " quote_netamt, quote_totaltax, quote_grandtotal, quote_refno, quote_auth, quote_active,"
							+ " customer_id, customer_name, contact_id,"
							+ " COALESCE(so_id, 0) AS so_id,"
							+ " COALESCE(so_active, '0') AS  so_active,"
							+ " quote_enquiry_id,"
							+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
							+ " CONCAT(contact_mobile1, ' ')AS mobile1,"
							+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, quote_discamt,"
							+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, quote_date,"
							+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";

					CountSql = "SELECT COUNT(DISTINCT(quote_id))";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_quote"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_quote_id = quote_id AND so_active = '1'"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item on quoteitem_quote_id = quote_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id";
					if (!executive_id.equals("0") || !team_id.equals("0") || !executive_ids.equals("0")) {
						SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = quote_emp_id";
						SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					SqlJoin += " WHERE quote_enquiry_id != 0";
					if (!enquiry_id.equals("0")) {
						SqlJoin += " AND quote_enquiry_id = " + enquiry_id;
					}
					StrSql += SqlJoin;
					StrSql += StrSearch1;
					StrSql += strSearch2
							+ ExeAccess.replace("emp_id", "quote_emp_id");
					StrSql = StrSql + " GROUP BY quote_id"
							+ " ORDER BY quote_id DESC "
							+ LimitRecords(TotalRecords, pagecurrent);
					CountSql += SqlJoin;
					SOP("StrSql---ws_quotelist----" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
					output.put("totalrecords", TotalRecords);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("quote_id", crs.getString("quote_id"));
							map.put("quote_no", crs.getString("quote_no"));
							if (config_sales_enquiry_refno.equals("1")) {
								map.put("quote_refno", crs.getString("quote_refno"));
							} else {
								map.put("quote_refno", "");
							}
							map.put("quote_enquiry_id", crs.getString("quote_enquiry_id"));
							map.put("quote_active", crs.getString("quote_active"));
							map.put("so_id", crs.getString("so_id"));
							map.put("so_active", crs.getString("so_active"));
							map.put("customer_name", crs.getString("customer_name"));
							map.put("contact_name", crs.getString("contact_name"));
							map.put("contact_mobile1", crs.getString("contact_mobile1"));
							map.put("contact_mobile2", crs.getString("contact_mobile2"));
							map.put("contact_email1", crs.getString("contact_email1"));
							map.put("contact_email2", crs.getString("contact_email2"));
							map.put("quote_date", strToShortDate(crs.getString("quote_date")));
							map.put("quote_netamt", IndDecimalFormat(df.format(crs.getDouble("quote_netamt"))));
							map.put("emp_name", crs.getString("emp_name"));
							list.add(gson.toJson(map));
						}
						map.clear();
						output.put("listdata", list);
						list.clear();
					} else {
						output.put("msg", "No Records Found!");
					}
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto-App ==" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
		if (AppRun().equals("0")) {
			SOP("output ===========vehquotelist============ " + output.toString(1));
		}
		return output;
	}

	private String ProcessFilter(HttpServletRequest request, JSONObject input) {
		StringBuilder str = new StringBuilder();
		try {
			// if (!input.isNull("model_ids")) {
			// // model_ids = CNumeric(PadQuotes((String) input.get("model_ids")));
			// SOP("model_ids-----------" + model_ids);;
			// }
			if (!input.isNull("brand_ids")) {
				brand_ids = CNumeric(PadQuotes((String) input.get("brand_ids")));
			}
			if (!input.isNull("region_ids")) {
				region_ids = CNumeric(PadQuotes((String) input.get("region_ids")));
			}
			if (!input.isNull("branch_ids")) {
				branch_ids = CNumeric(PadQuotes((String) input.get("branch_ids")));
			}
			if (!input.isNull("model_ids")) {
				model_ids = CNumeric(PadQuotes((String) input.get("model_ids")));
			}
			if (!input.isNull("executive_ids")) {
				executive_ids = CNumeric(PadQuotes((String) input.get("executive_ids")));
			}
			// if (!input.isNull("txt_quote_id")) {
			// filterenquiry_id = CNumeric(PadQuotes((String) input.get("txt_quote_id")));
			// }
			// // String filterenquiry_no =
			// // CNumeric(PadQuotes((String) input.get("txt_enquiry_no")));
			// if (!input.isNull("dr_branch_id")) {
			// filterenquiry_branch_id = CNumeric(PadQuotes((String) input.get("dr_branch_id")));
			// }
			// // String filterenquiry_branch_name =
			// // PadQuotes(request.getParameter("txt_branch_name"));
			// if (!input.isNull("txt_customer_id")) {
			// filterenquiry_customer_id = CNumeric(PadQuotes((String) input.get("txt_customer_id")));
			// }
			// if (!input.isNull("txt_contact_id")) {
			// filterenquiry_contact_id = CNumeric(PadQuotes((String) input.get("txt_contact_id")));
			// }
			// if (!input.isNull("txt_customer_name")) {
			// filterenquiry_customer_name = PadQuotes((String) input.get("txt_customer_name"));
			// }
			// if (!input.isNull("txt_contact_name")) {
			// filterenquiry_contact_name = PadQuotes((String) input.get("txt_contact_name"));
			// }
			// if (!input.isNull("txt_contact_mobile")) {
			// filterenquiry_contact_mobile = CNumeric(PadQuotes((String) input.get("txt_contact_mobile")));
			// }
			// if (!input.isNull("txt_contact_email")) {
			// filterenquiry_contact_email = PadQuotes((String) input.get("txt_contact_email"));
			// }
			//
			if (!model_ids.equals("0"))
			{
				strSearch2 = " AND item_model_id IN(" + model_ids + ")";
			}

			if (!brand_ids.equals("0"))
			{
				strSearch2 += " AND branch_brand_id IN(" + brand_ids + ")";
			}
			if (!region_ids.equals("0"))
			{
				strSearch2 += " AND branch_region_id IN(" + region_ids + ")";
			}

			if (!branch_ids.equals("0"))
			{
				strSearch2 += " AND quote_branch_id IN(" + branch_ids + ")";
			}
			if (!executive_ids.equals("0"))
			{
				strSearch2 += " AND quote_emp_id IN(" + executive_ids + ")";
			}

			if (!filterquote_enquiry_id.equals("0"))
			{
				strSearch2 += " AND quote_enquiry_id=" + filterquote_enquiry_id;
			}

			// if (!filterenquiry_no.equals("0"))
			// {
			// strsearch += " AND enquiry_no=" + filterenquiry_no;
			// }
			if (!input.isNull("txt_quote_id")) {
				filterquote_id = CNumeric(PadQuotes((String) input.get("txt_quote_id")));
			}
			if (!input.isNull("txt_quote_no")) {
				filterquote_no = CNumeric(PadQuotes((String) input.get("txt_quote_no")));
			}
			if (!input.isNull("txt_from_date")) {
				filterquote_from_date = PadQuotes((String) input.get("txt_from_date"));
			}
			if (!input.isNull("txt_to_date")) {
				filterquote_to_date = PadQuotes((String) input.get("txt_to_date"));
			}

			if (!filterquote_from_date.equals(""))
			{
				// filterquote_from_date = FormatDateStr(filterquote_from_date);
				filterquote_from_date = ConvertShortDateToStr(filterquote_from_date);
			}
			if (!filterquote_to_date.equals(""))
			{
				// filterquote_to_date = FormatDateStr(filterquote_to_date);
				filterquote_to_date = ConvertShortDateToStr(filterquote_to_date);

			}
			// if (!input.isNull("dr_branch_name")) {
			// filterquote_branch_name = PadQuotes((String) input.get("dr_branch_name"));
			// }
			if (!input.isNull("txt_customer_id")) {

				filterquote_customer_id = CNumeric(PadQuotes((String) input.get("txt_customer_id")));
			}
			if (!input.isNull("txt_customer_name")) {
				filterquote_customer_name = PadQuotes((String) input.get("txt_customer_name"));
			}
			if (!input.isNull("txt_contact_id")) {
				filterquote_contact_id = CNumeric(PadQuotes((String) input.get("txt_contact_id")));
			}
			if (!input.isNull("txt_contact_name")) {
				filterquote_contact_name = PadQuotes((String) input.get("txt_contact_name"));
			}
			if (!input.isNull("txt_contact_mobile")) {
				filterquote_contact_mobile = PadQuotes((String) input.get("txt_contact_mobile"));
			}
			if (!input.isNull("txt_contact_email")) {
				filterquote_contact_email = PadQuotes((String) input.get("txt_contact_email"));
			}
			if (!input.isNull("txt_enquiry_id")) {
				filterquote_enquiry_id = CNumeric(PadQuotes((String) input.get("txt_enquiry_id")));
			}
			// String filterquote_autorized_by =
			// CNumeric(PadQuotes(request.getParameter("txt_authorized_id")));
			if (!input.isNull("txt_from_authorized_date")) {
				filterquote_autorized_from_date = PadQuotes((String) input.get("txt_from_authorized_date"));
			}
			if (!input.isNull("txt_to_authorized_date")) {
				filterquote_autorized_to_date = PadQuotes((String) input.get("txt_to_authorized_date"));
			}

			if (!filterquote_autorized_from_date.equals(""))
			{
				// filterquote_autorized_from_date = FormatDateStr(filterquote_autorized_from_date);
				filterquote_autorized_from_date = ConvertShortDateToStr(filterquote_autorized_from_date);
			}
			if (!filterquote_autorized_to_date.equals(""))
			{
				// filterquote_autorized_to_date = FormatDateStr(filterquote_autorized_to_date);
				filterquote_autorized_to_date = ConvertShortDateToStr(filterquote_autorized_to_date);

			}
			if (!input.isNull("dr_action")) {
				filterquote_active = PadQuotes((String) input.get("dr_action"));
			}

			if (!filterquote_id.equals("0"))
			{
				strSearch2 += " AND quote_id=" + filterquote_id;
			}

			if (!filterquote_no.equals("0"))
			{
				strSearch2 += " AND quote_no=" + filterquote_no;
			}

			if (!filterquote_from_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(quote_date,1,8) >=SUBSTR('" + filterquote_from_date + "',1,8)";

			}

			if (!filterquote_to_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(quote_date,1,8) <=SUBSTR('" + filterquote_to_date + "',1,8)";

			}

			// if (!filterquote_branch_name.equals(""))
			// {
			// strSearch2 += " AND branch_name='" + filterquote_branch_name + "'";
			// }

			if (!filterquote_customer_id.equals("0"))
			{

				strSearch2 += " AND quote_customer_id=" + filterquote_customer_id;
			}

			if (!filterquote_customer_name.equals(""))
			{
				strSearch2 += " AND customer_name='" + filterquote_customer_name + "'";
			}

			if (!filterquote_contact_id.equals("0"))
			{
				strSearch2 += " AND quote_contact_id=" + filterquote_contact_id;
			}

			if (!filterquote_contact_name.equals(""))
			{
				strSearch2 += " AND (contact_fname LIKE" + filterquote_contact_name + "OR contact_lname LIKE " + filterquote_contact_name + "";
			}
			if (!filterquote_contact_mobile.contains("-") && !filterquote_contact_mobile.equals("")) {
				filterquote_contact_mobile = "91-" + filterquote_contact_mobile;
			}

			if (!filterquote_contact_mobile.equals(""))
			{
				strSearch2 += " AND (contact_mobile1='" + filterquote_contact_mobile + "'"
						+ " OR contact_mobile2='" + filterquote_contact_mobile + "'" + ")";
			}

			if (!filterquote_contact_email.equals(""))
			{
				strSearch2 += " AND (contact_email1='" + filterquote_contact_email + "'"
						+ " OR contact_email1='" + filterquote_contact_email + "'" + ")";
			}

			if (!filterquote_enquiry_id.equals("0"))
			{
				strSearch2 += " AND quote_enquiry_id=" + filterquote_enquiry_id;
			}

			if (!filterquote_autorized_from_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(quote_auth_date,1,8) >=SUBSTR('" + filterquote_autorized_from_date + "',1,8)";
			}

			if (!filterquote_autorized_to_date.equals(""))
			{
				strSearch2 += " AND SUBSTR(quote_auth_date,1,8)<=SUBSTR('" + filterquote_autorized_to_date + "',1,8)";
			}

			if (!filterquote_active.equals("0"))
			{
				strSearch2 += " AND quote_active=" + filterquote_active;
			}

			// return strsearch;

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("strSearch2----------in method------" + strSearch2);
		return strSearch2;
	}
	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_sales_campaign, config_sales_enquiry_refno"
				+ " from " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_emp" + " where emp_id = " + emp_id
				+ "";
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("strsql==config=="+StrSql);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
