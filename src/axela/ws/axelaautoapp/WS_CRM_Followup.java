/* Annappa May 20 2015 */
package axela.ws.axelaautoapp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_CRM_Followup extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public CachedRowSet crs = null;
	public String strsearch = "";
	public String crmlevel1 = "0";
	public String crmlevel2 = "0";
	public String crmlevel3 = "0";
	public String crmlevel4 = "0";
	public String crmlevel5 = "0";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject CRMFollowup(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_CRMFOllowup===" + input);
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
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		if (!emp_id.equals("0")) {
			try {
				StrSql = " SELECT gr.group_id AS group_id,"
						+ " COALESCE(triggercount, 0) AS triggercount "
						+ " FROM ( "
						+ " SELECT 1 AS group_id "
						+ " UNION "
						+ " SELECT 2 AS group_id "
						+ " UNION "
						+ " SELECT 3 AS group_id "
						+ " UNION "
						+ " SELECT 4 AS group_id "
						+ " UNION "
						+ " SELECT 5 AS group_id "
						+ " ) AS gr "
						+ " LEFT JOIN (SELECT COUNT(crm_enquiry_id) AS triggercount, crm_trigger"
						+ " FROM " + compdb(comp_id) + "axela_sales_crm"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
						+ " WHERE crm_desc = '' "
						+ " AND crm_trigger > 0"
						+ " AND enquiry_status_id = 1"
						+ BranchAccess.replace("branch_id", "enquiry_branch_id")
						+ ExeAccess.replace("emp_id", "crm_emp_id")
						+ " GROUP BY crm_trigger) AS tr ON tr.crm_trigger = gr.group_id"
						+ " WHERE 1=1 "
						+ " GROUP BY group_id "
						+ " ORDER BY group_id DESC";
				SOP("StrSql==========enquiry==========" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (crs.getString("group_id").equals("1")) {
							crmlevel1 = crs.getString("triggercount");
						}
						if (crs.getString("group_id").equals("2")) {
							crmlevel2 = crs.getString("triggercount");
						}
						if (crs.getString("group_id").equals("3")) {
							crmlevel3 = crs.getString("triggercount");
						}
						if (crs.getString("group_id").equals("4")) {
							crmlevel4 = crs.getString("triggercount");
						}
						if (crs.getString("group_id").equals("5")) {
							crmlevel5 = crs.getString("triggercount");
						}
					}
				}
				output.put("crmlevel1", crmlevel1);
				output.put("crmlevel2", crmlevel2);
				output.put("crmlevel3", crmlevel3);
				output.put("crmlevel4", crmlevel4);
				output.put("crmlevel5", crmlevel5);
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output =========WS_Followup======== " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return output;
			}
		}
		return output;
	}
}
