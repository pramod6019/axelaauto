/* Annappa (29th April 2015) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_DashCRMFollowup extends ConnectWS {

	public String update = "";
	public String delete = "";
	public String submitB = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String emp_uuid = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "";
	public String enquiry_status_id = "";
	public String followup = "";
	public String followup_id = "0";
	public String followup_desc = "";
	public String followup_emp_id = "0";
	public String followupdesc_id = "";
	public String followupdesc_name = "";
	public String followup_followuptype_id = "";
	public String followup_followup_time = "";
	public String followuptime = "";
	public String followup_time = "";
	public String followup_entry_id = "0";
	public String followup_entry_time = "";
	public String role_id = "";
	public String branch_id = "";
	// public String enquiry_decisiontime_id = "0";
	public String enquiry_custtype_id = "0";
	public String enquiry_tradeinmake_id = "0";
	public String enquiry_tradeinmodel_id = "0";
	public String enquiry_tradein_year = "";
	public String enquiry_tradein_fueltype_id = "0";
	public String enquiry_variantcolour_id = "0";
	// ws
	Gson gson = new Gson();
	// JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	public String status = "";

	public JSONObject Enquiry_Dash_CRMFollowUp(JSONObject input) {
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_CRMFollowUp input = " + input);
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!emp_id.equals("0")) {
					if (!input.isNull("enquiry_id")) {
						enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
						SOP("enquiry_id==========" + enquiry_id);
					}
					if (!input.isNull("comp_id")) {
						comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
					}
					if (!input.isNull("emp_uuid")) {
						emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
					}
					if (!input.isNull("followup_id")) {
						followup_id = CNumeric(PadQuotes((String) input.get("followup_id")));
					}
					if (!input.isNull("add")) {
						add = PadQuotes((String) input.get("add"));
					}
					if (!input.isNull("addB")) {
						addB = PadQuotes((String) input.get("addB"));
					}
					if (!input.isNull("delete")) {
						delete = PadQuotes((String) input.get("delete"));
					}
					if (!input.isNull("submitB")) {
						submitB = PadQuotes((String) input.get("submitB"));
					}
					// ExeAccess = WSCheckExeAccess(emp_id);
					if (!enquiry_id.equals("0") && !delete.equals("yes")) {
						String StrSql1 = "SELECT emp_branch_id, emp_role_id"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_id = " + emp_id;
						CachedRowSet crs1 = processQuery(StrSql1, 0);
						while (crs1.next()) {
							role_id = crs1.getString("emp_role_id");
							branch_id = crs1.getString("emp_branch_id");
						}
						crs1.close();
						if (!enquiry_id.equals("0")) {
							ListCustomerDetails();
							ListCRMFollowup();
							// ListPBF();
							// ListPSF();

						}
					}
					if (delete.equals("yes") && !followup_id.equals("0") && emp_id.equals("1")) {
						// DeleteFields();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_FollowUp output = " + output);
		}
		return output;
	}

	public JSONObject GetValues(JSONObject input) throws Exception {
		JSONObject output = new JSONObject();
		if (!input.isNull("followupdesc_id")) {
			followupdesc_id = CNumeric(PadQuotes((String) input.get("followupdesc_id")));
			// SOP("followupdesc_id = " + followupdesc_id);
		}
		if (!input.isNull("followup_desc")) {
			followup_desc = PadQuotes((String) input.get("followup_desc"));
			// SOP("followup_desc = " + followup_desc);
		}
		if (!input.isNull("followup_time")) {
			followup_followup_time = PadQuotes((String) input.get("followup_time"));
		}
		if (!input.isNull("followup_followuptype_id")) {
			followup_followuptype_id = CNumeric(PadQuotes((String) input.get("followup_followuptype_id")));
			// SOP("followup_followuptype_id = " + followup_followuptype_id);
		}
		return output;
	}

	public JSONObject ListCustomerDetails() {
		CachedRowSet crs = null;
		try {
			StrSql = " select customer_id, customer_name, contact_id,"
					+ " concat(title_desc,' ', contact_fname, ' ', contact_lname) as contacts, contact_mobile1, item_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crmfollowup_enquiry_id"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " where followup_enquiry_id = " + enquiry_id
					+ " group by customer_id";
			// SOP("PopulateTeam SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					map.put("customer_id", crs.getString("customer_id"));
					map.put("customer_name", crs.getString("customer_name"));
					map.put("contact_id", crs.getString("contact_id"));
					map.put("contacts", crs.getString("contacts"));
					map.put("contact_mobile1", crs.getString("contact_mobile1"));
					map.put("item_name", crs.getString("item_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("listcustomerdetails", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject ListCRMFollowup() {
		CachedRowSet crs = null;
		try {
			StrSql = " select crm_id, crm_emp_id, crm_enquiry_id, crm_so_id, crm_followup_time, crm_desc, "
					+ " crm_entry_time, crm_entry_id, crm_modified_id,"
					+ " crm_modified_time,"
					+ " COALESCE(crmfeedbacktype_name, '') AS crmfeedbacktype_name,"
					+ " crm_crmdays_id, crmdays_daycount, crmdays_desc, crmtype_id, crmtype_name,"
					+ " crm.emp_id as crmemp_id,"
					+ " coalesce(CONCAT(crm.emp_name,' (',crm.emp_ref_no,')'),'') as crmemp_name,"
					+ " coalesce(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') as entry_by, "
					+ " coalesce(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') as modified_by "
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_crm"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " INNER JOIN axela_sales_crm_type on crmtype_id = crmdays_crmtype_id"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_emp as crm on crm.emp_id = crm_emp_id"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_emp as e on e.emp_id = crm_entry_id"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_emp as m on m.emp_id = crm_modified_id"
					+ " LEFT JOIN axela_sales_crm_feedbacktype on crmfeedbacktype_id = crm_crmfeedbacktype_id"
					+ " where 1 = 1 "
					// + " and crmdays_crmtype_id='1'"
					+ " and crm_enquiry_id = " + enquiry_id
					+ " GROUP BY crm_id"
					+ " ORDER BY crmdays_crmtype_id, crm_followup_time ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					map.put("crmfollowup_id", crs.getString("crm_id"));
					map.put("crmfollowup_enquiry_id", crs.getString("crm_enquiry_id"));
					map.put("crmtype_id", crs.getString("crmtype_id"));
					map.put("crmtype_name", crs.getString("crmtype_name"));
					map.put("crm_so_id", crs.getString("crm_so_id"));
					map.put("crmfollowup_followup_time", strToLongDate(crs.getString("crm_followup_time")));
					map.put("crmfollowup_desc", crs.getString("crm_desc"));
					map.put("crmfollowup_entry_time", strToLongDate(crs.getString("crm_entry_time")));
					map.put("crmfollowup_entry_id", crs.getString("crm_entry_id"));
					map.put("crmfollowup_modified_id", crs.getString("crm_modified_id"));
					map.put("crmfollowup_modified_time", strToLongDate(crs.getString("crm_modified_time")));
					map.put("crmfeedbacktype_name", crs.getString("crmfeedbacktype_name"));
					map.put("crmfollowupdays_daycount", crs.getString("crmdays_daycount"));
					map.put("crmfollowupdays_desc", crs.getString("crmdays_desc"));
					map.put("crmemp_id", crs.getString("crm_emp_id"));
					map.put("crmemp_name", crs.getString("crmemp_name"));
					map.put("entry_by", crs.getString("entry_by"));
					map.put("modified_by", crs.getString("modified_by"));

					//
					map.put("crm_id", crs.getString("crm_id"));
					map.put("crm_enquiry_id", crs.getString("crm_enquiry_id"));
					map.put("crmtype_id", crs.getString("crmtype_id"));
					map.put("crmtype_name", crs.getString("crmtype_name"));
					map.put("crm_so_id", crs.getString("crm_so_id"));
					map.put("crm_followup_time", strToLongDate(crs.getString("crm_followup_time")));
					map.put("crm_desc", crs.getString("crm_desc"));
					map.put("crm_entry_time", strToLongDate(crs.getString("crm_entry_time")));
					map.put("crm_entry_id", crs.getString("crm_entry_id"));
					map.put("crm_modified_id", crs.getString("crm_modified_id"));
					map.put("crm_modified_time", strToLongDate(crs.getString("crm_modified_time")));
					map.put("crmfeedbacktype_name", crs.getString("crmfeedbacktype_name"));
					map.put("crmdays_daycount", crs.getString("crmdays_daycount"));
					map.put("crmdays_desc", crs.getString("crmdays_desc"));
					map.put("crm_emp_id", crs.getString("crm_emp_id"));
					map.put("crmemp_name", crs.getString("crmemp_name"));
					map.put("entry_by", crs.getString("entry_by"));
					map.put("modified_by", crs.getString("modified_by"));
					map.put("update", "yes");

					//
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("listcrmfollowup", list);
				list.clear();
				crs.close();
			} else {
				output.put("msg", "No CRM FollowUP Found");
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
