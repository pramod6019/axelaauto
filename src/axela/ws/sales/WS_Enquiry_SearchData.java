package axela.ws.sales;
//12 sept 2013
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_SearchData extends ConnectWS {

	public String keyword = "0";
	public String fromdate = "";
	public String enddate = "";
	public String enquiry_id = "";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String enquiry_stage_id = "";
	public String enquiry_status_id = "";
	public String enquiry_model_id = "";
	public String enquiry_item_id = "";
	public String StrSql = "";
	public String search = "";
	// ws
	JSONObject output = new JSONObject();
	Gson gson = new Gson();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_Search(JSONObject input) {
		try {
			if (AppRun().equals("0")) {
				SOP("input = " + input);
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("keyword")) {
				keyword = PadQuotes((String) input.get("keyword"));
				if (!keyword.equals("")) {
					search = search + " and (enquiry_id like '%" + keyword + "%' or enquiry_no like '%" + keyword + "%'"
							+ " or enquiry_title like '%" + keyword + "%' or enquiry_desc like '%" + keyword + "%'"
							+ " or enquiry_refno like '%" + keyword + "%' or enquiry_date like '%" + keyword + "%'"
							+ " or enquiry_close_date like '%" + keyword + "%' or enquiry_lead_id like '%" + keyword + "%'"
							+ " or customer_id like '%" + keyword + "%' or customer_name like '%" + keyword + "%'"
							+ " or contact_id like '%" + keyword + "%' or contact_fname like '%" + keyword + "%'"
							+ " or contact_lname like '%" + keyword + "%'"
							+ " or contact_mobile1 like '%" + keyword + "%' or contact_mobile2 like '%" + keyword + "%'"
							+ " or contact_email1 like '%" + keyword + "%' or soe_name like '%" + keyword + "%'"
							+ " or sob_name like '%" + keyword + "%' or campaign_id like '%" + keyword + "%'"
							+ " or campaign_name like '%" + keyword + "%'"
							+ " or emp_id like '%" + keyword + "%' or emp_name like '%" + keyword + "%'"
							+ " or stage_name like '%" + keyword + "%' or status_name like '%" + keyword + "%'"
							+ " or branch_id like '%" + keyword + "%' or branch_code like '%" + keyword + "%'"
							+ " or branch_name like '%" + keyword + "%' or followup_enquiry_id like '%" + keyword + "%'"
							+ " or item_name like '%" + keyword + "%' or enquiry_priorityenquiry_id like '%" + keyword + "%')";
				}
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
				if (!enquiry_id.equals("0")) {
					search = search + " and enquiry_id = " + enquiry_id + "";
				}
			}
			if (!input.isNull("fromdate")) {
				fromdate = PadQuotes((String) input.get("fromdate"));
				if (!fromdate.equals("")) {
					search = search + " and SUBSTR(enquiry_date,0, 8) > " + fromdate + "";
				}
			}
			if (!input.isNull("enddate")) {
				enddate = PadQuotes((String) input.get("enddate"));
				if (!enddate.equals("")) {
					search = search + " and SUBSTR(enquiry_date,0, 8) < " + enddate + "";
				}
			}
			if (!input.isNull("enquiry_stage_id")) {
				enquiry_stage_id = CNumeric(PadQuotes((String) input.get("enquiry_stage_id")));
				if (!enquiry_stage_id.equals("0")) {
					search = search + " and enquiry_date = " + enddate + "";
				}
			}
			if (!input.isNull("enquiry_status_id")) {
				enquiry_status_id = CNumeric(PadQuotes((String) input.get("enquiry_status_id")));
				if (!enquiry_status_id.equals("0")) {
					search = search + " and enquiry_status_id = " + enquiry_status_id + "";
				}
			}
			if (!input.isNull("enquiry_model_id")) {
				enquiry_model_id = CNumeric(PadQuotes((String) input.get("enquiry_model_id")));
				if (!enquiry_model_id.equals("0")) {
					search = search + " and enquiry_model_id = " + enquiry_model_id + "";
				}
			}
			if (!input.isNull("enquiry_item_id")) {
				enquiry_item_id = CNumeric(PadQuotes((String) input.get("enquiry_item_id")));
				if (!enquiry_item_id.equals("0")) {
					search = search + " and enquiry_item_id = " + enquiry_item_id + "";
				}
			}
			if (!keyword.equals("") || !enquiry_id.equals("0")
					|| !fromdate.equals("") || !enddate.equals("")
					|| !enquiry_stage_id.equals("0") || !enquiry_status_id.equals("0")
					|| !enquiry_model_id.equals("0") || !enquiry_item_id.equals("0")) {
				ListEnquiry();

			} else {
				output.put("searchenquiry", "Enter Atleast 1 Search Parameter!");
			}
			if (AppRun().equals("0")) {
				SOP("output = " + output);
			}
			return output;
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}

	}

	public JSONObject ListEnquiry() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT enquiry_id, CONCAT('Enquiry', branch_code, enquiry_no) AS enquiry_no, enquiry_title,"
					+ " enquiry_desc, enquiry_refno, enquiry_date, enquiry_close_date, enquiry_lead_id, customer_id,"
					+ " customer_name, contact_id, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, COALESCE(soe_name, '') AS soe_name,"
					+ " COALESCE(sob_name, '') AS sob_name, COALESCE(campaign_id, 0) AS campaign_id,"
					+ " COALESCE(campaign_name, ' ') AS campaign_name, emp_id,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, stage_name, status_name,"
					+ " branch_id, branch_code, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " COALESCE(followup_enquiry_id, 0) AS followup_enquiry_id, item_name, enquiry_priorityenquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON  title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id"
					+ " WHERE 1 = 1 " + search
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("enquiry_id", crs.getString("enquiry_id"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("searchenquiry", list);
				list.clear();
				crs.close();
			} else {
				output.put("searchenquiry", "No Enquiry Found!");
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
