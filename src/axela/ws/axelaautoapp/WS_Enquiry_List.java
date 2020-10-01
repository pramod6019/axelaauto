/* Annappa May 20 2015 */
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

public class WS_Enquiry_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String enqstatus = "";
	public String StrSearch = "", strSearch2 = "", StrSearch1 = "", Subquery = "";
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
	public String contact_mobile1_temp = "";
	public String contact_mobile2_temp = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String model_id = "0";
	public String enquiry_id = "0";
	public String enquiry_startdate = "", enquiry_enddate = "";
	public String item_id = "0";
	public String status_id = "0";
	public String priority_id = "0";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchvalue = "";
	public String model_ids = "0";
	public String status_ids = "0";
	public String stage_ids = "0";
	public String priorityenquiry_ids = "0";
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
	public String executive_ids = "0";
	public String filterenquiry_id = "0";
	public String filterenquiry_dms_no = "0";
	public String filterenquiry_branch_id = "0";
	public String filterenquiry_customer_id = "0";
	public String filterenquiry_contact_id = "0";
	public String filterenquiry_customer_name = "";
	public String filterenquiry_contact_name = "";
	public String filterenquiry_contact_mobile = "";
	public String filterenquiry_contact_email = "";
	public String filterenquiry_branch_name = "";
	public String filterpreowned = "";
	public String filterquery = "";
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject EnquiryList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			// SOP("input==WS_Enquiry_List===" + input);
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
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		if (ReturnPerm(comp_id, "emp_enquiry_access", request).equals("0")) {
			output.put("errorpage", "Access Denied!");
			return output;
		}
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		if (!input.isNull("filter")) {
			filter = PadQuotes((String) input.get("filter"));
		}
		if (filter.equals("yes")) {
			strSearch2 = ProcessFilter(request, input);
		}
		if (!input.isNull("brand_id")) {
			brand_id = (((String) input.get("brand_id")));
		}
		if (!input.isNull("region_id")) {
			region_id = (((String) input.get("region_id")));
		}
		if (!input.isNull("branch_id")) {
			branch_id = (PadQuotes((String) input.get("branch_id")));
		}
		if (!input.isNull("team_id")) {
			team_id = (PadQuotes((String) input.get("team_id")));
		}
		if (!input.isNull("executive_id")) {
			executive_id = (PadQuotes((String) input.get("executive_id")));
		}
		if (!input.isNull("enquiry_id")) {
			enquiry_id = (PadQuotes((String) input.get("enquiry_id")));
		}
		if (!input.isNull("txt_enquiry_startdate")) {
			enquiry_startdate = (PadQuotes((String) input.get("txt_enquiry_startdate")));
		}
		if (!input.isNull("txt_enquiry_enddate")) {
			enquiry_enddate = (PadQuotes((String) input.get("txt_enquiry_enddate")));
		}
		if (!input.isNull("filterquery")) {
			filterquery = JSONPadQuotes((String) input.get("filterquery"));
			if (!filterquery.equals("")) {
				filterquery = new String(Base64.decodeBase64(filterquery.getBytes("ISO-8859-1")));
			}
		}
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!emp_id.equals("0")) {
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
			if (!enquiry_id.equals("0")) {
				StrSearch1 += " AND enquiry_id = " + enquiry_id + "";
			}
			if (!enquiry_startdate.equals("") && isValidDateFormatShort(enquiry_startdate)) {
				enquiry_startdate = ConvertShortDateToStr(enquiry_startdate);
				StrSearch += " AND SUBSTR(enquiry_date,1,8) >= SUBSTR(" + enquiry_startdate + ",1,8)";
			}
			if (!enquiry_enddate.equals("") && isValidDateFormatShort(enquiry_enddate)) {
				enquiry_enddate = ConvertShortDateToStr(enquiry_enddate);
				StrSearch += " AND SUBSTR(enquiry_date,1,8) <= SUBSTR(" + enquiry_enddate + ",1,8)";
			}

			if (!input.isNull("enqstatus")) {
				enqstatus = PadQuotes((String) input.get("enqstatus"));
				if (enqstatus.equals("monthenquiries")) {
					StrSearch = " AND substr(enquiry_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)";
				}
				if (enqstatus.equals("booking")) {
					Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
					StrSearch = " AND substr(so_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " AND so_active = 1";
				}
				if (enqstatus.equals("retail")) {
					Subquery = " INNER JOIN " + compdb(comp_id) + "axela_invoice ON invoice_enquiry_id = enquiry_id";
					StrSearch = " AND substr(invoice_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)"
							+ " AND invoice_active = 1";
				}
				if (enqstatus.equals("delivered")) {
					Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
					StrSearch = " AND substr(so_delivered_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " AND so_active = 1";
				}
				if (enqstatus.equals("cancellation")) {
					Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
					StrSearch = " AND substr(so_cancel_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " AND so_active = 0";
				}
				if (enqstatus.equals("todayenquiries")) {
					StrSearch = " AND substr(enquiry_entry_date,1,8) = substr(" + ToLongDate(kknow()) + ",1,8)";
				}
				if (enqstatus.equals("totalhotenquiries")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_priorityenquiry_id = 1 ";
				}
				if (enqstatus.equals("totalenquiries")) {
					StrSearch = " AND enquiry_status_id = 1 ";
				}
				if (enqstatus.equals("level1")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT followup_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_trigger = 1"
							+ " AND followup_desc = ''"
							+ " ORDER BY followup_id ) ";
				}
				if (enqstatus.equals("level2")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT followup_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_trigger = 2"
							+ " AND followup_desc = ''"
							+ " ORDER BY followup_id ) ";
				}
				if (enqstatus.equals("level3")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT followup_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_trigger = 3"
							+ " AND followup_desc = ''"
							+ " ORDER BY followup_id ) ";
				}
				if (enqstatus.equals("level4")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT followup_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_trigger = 4"
							+ " AND followup_desc = ''"
							+ " ORDER BY followup_id ) ";
				}
				if (enqstatus.equals("level5")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT followup_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_trigger = 5"
							+ " AND followup_desc = ''"
							+ " ORDER BY followup_id ) ";
				}
				if (enqstatus.equals("crmlevel1")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT crm_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " WHERE crm_trigger = 1"
							+ " AND crm_desc = ''"
							+ " ORDER BY crm_id ) ";
				}
				if (enqstatus.equals("crmlevel2")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT crm_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " WHERE crm_trigger = 2"
							+ " AND crm_desc = ''"
							+ " ORDER BY crm_id ) ";
				}
				if (enqstatus.equals("crmlevel3")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT crm_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " WHERE crm_trigger = 3"
							+ " AND crm_desc = ''"
							+ " ORDER BY crm_id ) ";
				}
				if (enqstatus.equals("crmlevel4")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT crm_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " WHERE crm_trigger = 4"
							+ " AND crm_desc = ''"
							+ " ORDER BY crm_id ) ";
				}
				if (enqstatus.equals("crmlevel5")) {
					StrSearch = " AND enquiry_status_id = 1 AND enquiry_id IN ( SELECT crm_enquiry_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " WHERE crm_trigger = 5"
							+ " AND crm_desc = ''"
							+ " ORDER BY crm_id ) ";
				}
				if (enqstatus.equals("todaytestdrives")) {
					StrSearch = " AND SUBSTR(testdrive_time,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)"
							+ " AND testdrive_fb_taken = 1";
				}
				if (enqstatus.equals("todayhomevisits")) {
					StrSearch = " AND SUBSTR(followup_followup_time,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)"
							+ " AND followup_feedbacktype_id = 9";
				}
			}

			try {
				StrSql = "SELECT customer_name, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, branch_brand_id,"
						+ " enquiry_id, enquiry_status_id, enquiry_date, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " contact_phone1, contact_phone2, enquiry_model_id,"
						+ " CONCAT(exeemp.emp_name, ' (', exeemp.emp_ref_no, ')') AS emp_name,"
						+ " COALESCE(item_name, '') item_name,"
						+ " stage_name, status_name, enquiry_priorityenquiry_id, enquiry_enquirytype_id";

				CountSql = "SELECT COUNT(DISTINCT enquiry_id)";
				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry";
				SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON  title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp exeemp ON exeemp.emp_id = enquiry_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id";
				if (!executive_id.equals("0") || !executive_ids.equals("0") || !team_id.equals("0")) {
					SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id";
					SqlJoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
				}
				if (enqstatus.equals("todaytestdrives")) {
					SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id";
				}
				if (enqstatus.equals("todayhomevisits")) {
					SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followup_followuptype_id = followuptype_id";
				}

				SqlJoin += Subquery
						+ " WHERE 1 = 1 "
						+ StrSearch
						+ StrSearch1
						// for monitoring board
						+ filterquery;

				StrSql = StrSql + SqlJoin;
				StrSql += strSearch2
						+ ExeAccess.replace("emp_id", "enquiry_emp_id");
				if (!BranchAccess.equals("")) {
					StrSql += BranchAccess;
				}
				StrSql = StrSql + " GROUP BY enquiry_id"
						+ " ORDER BY enquiry_id DESC "
						+ LimitRecords(TotalRecords, pagecurrent);
				SOP("StrSql====" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						i++;
						map.put("enquiry_id", crs.getString("enquiry_id"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
						map.put("item_name", unescapehtml(crs.getString("item_name")));
						map.put("stage_name", crs.getString("stage_name"));
						map.put("status_name", crs.getString("status_name"));
						map.put("customer_name", crs.getString("customer_name"));
						map.put("enquiry_status_id", crs.getString("enquiry_status_id"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("branch_brand_id", crs.getString("branch_brand_id"));
						map.put("enquiry_enquirytype_id", crs.getString("enquiry_enquirytype_id"));
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
					// SOP("output =========WS_Enquiry_list======== " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}
	private String ProcessFilter(HttpServletRequest request, JSONObject input) {
		try {
			if (!input.isNull("model_ids")) {
				model_ids = PadQuotes((String) input.get("model_ids"));
			}
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
			if (!input.isNull("stage_ids")) {
				stage_ids = PadQuotes((String) input.get("stage_ids"));
			}
			if (!input.isNull("priorityenquiry_ids")) {
				priorityenquiry_ids = PadQuotes((String) input.get("priorityenquiry_ids"));
			}
			if (!input.isNull("model_ids")) {
				model_ids = PadQuotes((String) input.get("model_ids"));
			}
			if (!input.isNull("soe_ids")) {
				soe_ids = PadQuotes((String) input.get("soe_ids"));
			}
			if (!input.isNull("executive_ids")) {
				executive_ids = PadQuotes((String) input.get("executive_ids"));
			}
			if (!input.isNull("txt_enquiry_id")) {
				filterenquiry_id = CNumeric(PadQuotes((String) input.get("txt_enquiry_id")));
			}
			// String filterenquiry_no =
			// CNumeric(PadQuotes((String) input.get("txt_enquiry_no")));
			if (!input.isNull("txt_dms_no")) {
				filterenquiry_dms_no = PadQuotes((String) input.get("txt_dms_no"));
			}
			// if (!input.isNull("dr_branch_id")) {
			// filterenquiry_branch_id = CNumeric(PadQuotes((String) input.get("dr_branch_id")));
			// }
			if (!input.isNull("dr_branch_name")) {
				filterenquiry_branch_name = PadQuotes((String) input.get("dr_branch_name"));
			}
			if (!input.isNull("txt_customer_id")) {
				filterenquiry_customer_id = CNumeric(PadQuotes((String) input.get("txt_customer_id")));
			}
			if (!input.isNull("txt_contact_id")) {
				filterenquiry_contact_id = CNumeric(PadQuotes((String) input.get("txt_contact_id")));
			}
			if (!input.isNull("txt_customer_name")) {
				filterenquiry_customer_name = PadQuotes((String) input.get("txt_customer_name"));
			}
			if (!input.isNull("txt_contact_name")) {
				filterenquiry_contact_name = PadQuotes((String) input.get("txt_contact_name"));
			}
			if (!input.isNull("txt_contact_mobile")) {
				filterenquiry_contact_mobile = (PadQuotes((String) input.get("txt_contact_mobile")));
			}
			if (!input.isNull("txt_contact_email")) {
				filterenquiry_contact_email = PadQuotes((String) input.get("txt_contact_email"));
			}
			if (!input.isNull("preowned")) {
				filterpreowned = PadQuotes((String) input.get("preowned"));
			}
			if (!model_ids.equals("0"))
			{
				strSearch2 = " AND enquiry_model_id IN(" + model_ids + ")";
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
				strSearch2 += " AND enquiry_branch_id IN(" + branch_ids + ")";
			}
			if (!executive_ids.equals("0"))
			{
				strSearch2 += " AND enquiry_emp_id IN(" + executive_ids + ")";
			}
			if (!status_ids.equals("0"))
			{
				strSearch2 += " AND enquiry_status_id IN(" + status_ids + ")";
			}

			if (!stage_ids.equals("0"))
			{
				strSearch2 += " AND enquiry_stage_id IN(" + stage_ids + ")";
			}

			if (!priorityenquiry_ids.equals("0"))
			{
				strSearch2 += " AND enquiry_priorityenquiry_id IN(" + priorityenquiry_ids + ")";
			}

			if (!soe_ids.equals("0"))
			{
				strSearch2 += " AND enquiry_soe_id IN(" + soe_ids + ")";
			}

			if (!filterenquiry_id.equals("0"))
			{
				strSearch2 += " AND enquiry_id=" + filterenquiry_id;
			}

			if (!filterenquiry_dms_no.equals("0"))
			{
				strSearch2 += " AND enquiry_dmsno='" + filterenquiry_dms_no + "'";
			}

			if (!filterenquiry_branch_name.equals(""))
			{
				strSearch2 += " AND branch_name='" + filterenquiry_branch_name + "'";
			}

			if (!filterenquiry_customer_id.equals("0"))
			{
				strSearch2 += " AND enquiry_customer_id=" + filterenquiry_customer_id;
			}

			if (!filterenquiry_contact_id.equals("0"))
			{
				strSearch2 += " AND enquiry_contact_id=" + filterenquiry_contact_id;
			}

			if (!filterenquiry_customer_name.equals(""))
			{
				strSearch2 += " AND customer_name LIKE '%" + filterenquiry_customer_name + "%'";
			}

			if (!filterenquiry_contact_name.equals(""))
			{
				strSearch2 += " AND (contact_fname LIKE '%" + filterenquiry_contact_name + "%' OR contact_lname LIKE '%" + filterenquiry_contact_name + "%')";
			}

			if (!filterenquiry_contact_mobile.contains("-") && !filterenquiry_contact_mobile.equals("")) {
				filterenquiry_contact_mobile = "91-" + filterenquiry_contact_mobile;
			}

			if (!filterenquiry_contact_mobile.equals(""))
			{
				strSearch2 += " AND (contact_mobile1='" + filterenquiry_contact_mobile + "'"
						+ " OR contact_mobile2='" + filterenquiry_contact_mobile + "'" + ")";
			}

			if (!filterenquiry_contact_email.equals(""))
			{
				strSearch2 += " AND (contact_email1='" + filterenquiry_contact_email + "'"
						+ " OR contact_email1='" + filterenquiry_contact_email + "'" + ")";
			}

			if (!filterpreowned.equals(""))
			{
				strSearch2 += " AND enquiry_enquirytype_id in (2)";
			}
			// SOP("strSearch2===========" + strSearch2);
			return strSearch2;

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return strSearch2;
	}
}
