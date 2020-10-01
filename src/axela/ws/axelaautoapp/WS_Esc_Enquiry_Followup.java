/* Ved Prakash (2nd May 2013) */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Esc_Enquiry_Followup extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String branch_id = "";
	public CachedRowSet crs = null;
	JSONObject output = new JSONObject();

	public JSONObject EscList(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {

			SOP("input esce for testing ======== " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!emp_id.equals("0")) {
				try {
					StrSql = "SELECT enquiry_id, followup_id, followup_trigger, followup_followup_time, followup_notify, "
							+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name, "
							+ " contact_mobile1, contact_email1 "
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
							+ " WHERE enquiry_status_id = 1  "
							+ " AND followup_desc=''  "
							+ " AND followup_trigger !=0 "
							+ " AND followup_notify = 0 "
							+ " AND enquiry_emp_id =" + emp_id
							+ " GROUP BY enquiry_id "
							+ " ORDER BY followup_followup_time"
							+ " LIMIT 3";
					// SOP("StrSql================" + StrSql);
					crs = processQuery(StrSql, 0);
					Gson gson = new Gson();
					JSONObject obj = new JSONObject();
					ArrayList<String> list = new ArrayList<String>();
					Map<String, String> map = new HashMap<String, String>();
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("enquiry_id", crs.getString("enquiry_id"));
							map.put("followup_id", crs.getString("followup_id"));
							map.put("followup_trigger", crs.getString("followup_trigger"));
							map.put("followup_followup_time", crs.getString("followup_followup_time"));
							map.put("followup_notify", crs.getString("followup_notify"));
							map.put("contact_name", crs.getString("contact_name"));
							if (!crs.getString("contact_mobile1").equals("")) {
								map.put("contact_mobile1", crs.getString("contact_mobile1"));
							}
							list.add(gson.toJson(map)); // Converting String to Json
							StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_followup "
									+ " set followup_notify= 1"
									+ " where followup_id=" + crs.getString("followup_id") + " ";
							updateQuery(StrSql);
						}
						map = null;
						output.put("listdata", list);
					} else {
						output.put("msg", "No Records Found!");
					}
					crs.close();

				} catch (Exception ex) {
					SOPError("Axelaauto-App =======" + this.getClass().getName());
					SOPError("Axelaauto-App ======= " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
		return output;
	}
}
