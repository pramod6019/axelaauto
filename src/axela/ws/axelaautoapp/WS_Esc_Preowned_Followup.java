/* Ved Prakash (2nd May 2013) */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Esc_Preowned_Followup extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String branch_id = "";
	public CachedRowSet crs = null;
	JSONObject output = new JSONObject();

	public JSONObject PreownedEscalation(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input preowned esce for testing ======== " + input);
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
					StrSql = "SELECT preowned_id, preownedfollowup_id, preownedfollowup_trigger, preownedfollowup_followup_time, preownedfollowup_notify, "
							+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name, "
							+ " contact_mobile1, contact_email1 "
							+ " FROM " + compdb(comp_id) + "axela_preowned_followup  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedfollowup_preowned_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id  "
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
							// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = preowned_item_id"
							// + " INNER JOIN " + compdb(comp_id) + "axela_sales_preowned_stage ON stage_id = preowned_stage_id"
							// + " WHERE preowned_status_id = 1  "
							+ " WHERE 1=1"
							+ " AND preownedfollowup_desc = ''  "
							+ " AND preownedfollowup_trigger != 0 "
							+ " AND preownedfollowup_notify = 0 "
							+ " AND preowned_emp_id = " + emp_id
							+ " GROUP BY preowned_id "
							+ " ORDER BY preownedfollowup_followup_time"
							+ " LIMIT 3";
					// SOP("StrSql========preownedesc========" + StrSql);
					crs = processQuery(StrSql, 0);
					Gson gson = new Gson();
					JSONObject obj = new JSONObject();
					ArrayList<String> list = new ArrayList<String>();
					Map<String, String> map = new HashMap<String, String>();
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("preowned_id", crs.getString("preowned_id"));
							map.put("preownedfollowup_trigger", crs.getString("preownedfollowup_trigger"));
							map.put("preownedfollowup_id", crs.getString("preownedfollowup_id"));
							map.put("preownedfollowup_trigger", crs.getString("preownedfollowup_trigger"));
							map.put("preownedfollowup_followup_time", crs.getString("preownedfollowup_followup_time"));
							map.put("preownedfollowup_notify", crs.getString("preownedfollowup_notify"));
							map.put("contact_name", crs.getString("contact_name"));
							if (!crs.getString("contact_mobile1").equals("")) {
								map.put("contact_mobile1", crs.getString("contact_mobile1"));
							}
							list.add(gson.toJson(map)); // Converting String to Json
							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_followup "
									+ " SET preownedfollowup_notify= 1"
									+ " WHERE preownedfollowup_id=" + crs.getString("preownedfollowup_id") + " ";
							SOP("StrSql===update===" + StrSql);
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
		if (AppRun().equals("0")) {
			SOP("output====preowned-esc===" + output);
		}
		return output;
	}
}
