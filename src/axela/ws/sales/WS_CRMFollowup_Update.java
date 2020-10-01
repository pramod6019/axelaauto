/* Annappa (29th April 2015) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_CRMFollowup_Update extends ConnectWS {

	public String update = "";
	public String updateB = "";
	public String delete = "";
	public String submitB = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String crm = "";
	public String psf_id = "0";
	public String psf_desc = "";
	public String psf_psffeedbacktype_id = "0";
	public String so_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String enquiry_id = "";
	public String enquiry_status_id = "";
	public String crmfeedbacktype_id = "0";
	public String crm_crmfeedbacktype_id = "0";
	public String crm_desc = "";
	public String followup = "";
	public String crm_id = "0";
	public String followup_desc = "";
	public String followup_emp_id = "0";
	public String followupdesc_id = "";
	public String followupdesc_name = "";
	public String followup_followuptype_id = "";
	public String followup_followup_time = "";
	public String crmfollowup_days_desc = "";
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

	public JSONObject CRMFollowUp_Update(JSONObject input) {
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_CRMFollowUp input = " + input);
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!emp_id.equals("0")) {
					if (!input.isNull("comp_id")) {
						comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
					}
					if (!input.isNull("emp_uuid")) {
						emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
					}
					if (!input.isNull("enquiry_id")) {
						enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
					}
					if (!input.isNull("crm_id")) {
						crm_id = CNumeric(PadQuotes((String) input.get("crm_id")));
					}
					if (!input.isNull("update")) {
						update = PadQuotes((String) input.get("update"));
					}
					if (!input.isNull("updateB")) {
						updateB = PadQuotes((String) input.get("updateB"));
					}
					if (!input.isNull("delete")) {
						delete = PadQuotes((String) input.get("delete"));
					}
					if (!input.isNull("submitB")) {
						submitB = PadQuotes((String) input.get("submitB"));
					}
					if (!input.isNull("crm")) {
						crm = PadQuotes((String) input.get("crm"));
					}
					if (!update.equals("yes")) {
						ListCustomerDetails();
					}
					ListCRMFeedbackType();
					if (!crm_id.equals("0")) {
						PopulateCRMfields();
					}

					if (updateB.equals("yes")) {
						GetValues(input);
						followup_entry_id = emp_id;
						followup_entry_time = ToLongDate(kknow());
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm"
								+ " SET"
								+ " crm_desc = '" + crm_desc + "',"
								+ " crm_crmfeedbacktype_id = " + crm_crmfeedbacktype_id + ","
								+ " crm_modified_id = " + emp_id + ","
								+ " crm_modified_time = '" + ToLongDate(kknow()) + "'"
								+ " WHERE crm_id = " + crm_id
								+ " and crm_enquiry_id = " + enquiry_id;
						// SOP("strsql======="+StrSql);
						updateQuery(StrSql);
						output.put("msg", "CRM FollowUp Updated Successfully!");
					}
					// if (delete.equals("yes") && !crmfollowup_id.equals("0") && emp_id.equals("1")) {
					// // DeleteFields();
					// }
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

		if (!input.isNull("crm_crmfeedbacktype_id")) {
			crm_crmfeedbacktype_id = CNumeric(PadQuotes((String) input.get("crm_crmfeedbacktype_id")));
		}
		if (!input.isNull("crm_desc")) {
			crm_desc = PadQuotes((String) input.get("crm_desc"));
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

	public JSONObject ListCRMFeedbackType() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT crmfeedbacktype_id, crmfeedbacktype_name"
					+ " FROM axela_sales_crm_feedbacktype"
					+ " WHERE 1=1"
					+ " ORDER BY crmfeedbacktype_name";
			// SOP("PopulateTeam SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					map.put("crmfeedbacktype_id", crs.getString("crmfeedbacktype_id"));
					map.put("crmfeedbacktype_name", crs.getString("crmfeedbacktype_name"));

					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("listcrmfeedbacktype", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateCRMfields() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT "
					+ " crm_desc, "
					+ " crm_crmfeedbacktype_id, "
					+ " COALESCE(crm_entry_time,'') AS crm_entry_time,"
					+ " COALESCE(crm_modified_time,'') AS crm_modified_time "
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_crm"
					+ " WHERE crm_id = " + crm_id + "" + " GROUP BY crm_id";
			// SOP("StrSql--CRM-----" + StrSql);
			SOP("PopulateTeam SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					output.put("crm_crmfeedbacktype_id", crs.getString("crm_crmfeedbacktype_id"));
					output.put("crm_desc", crs.getString("crm_desc"));

					// list.add(gson.toJson(map));
				}
			}
			// map.clear();
			// output.put("populatefields", list);
			// list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

}
