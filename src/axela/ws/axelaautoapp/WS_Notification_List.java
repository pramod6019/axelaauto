//* Sanjay May 20 2017 */
package axela.ws.axelaautoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Notification_List extends Connect {
	public String StrSql = "";
	public int TotalRecords = 0;
	public String strSearch = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public CachedRowSet crs = null;
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject NotificationList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input==WS_Notification_List===" + input);
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
		if (!input.isNull("pagecurrent")) {
			pagecurrent = CNumeric(PadQuotes((String) input.get("pagecurrent")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!emp_id.equals("0")) {
			try {
				StrSql = "SELECT (SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = notification_entry_id) AS entry_name,"
						+ " notification_title, notification_msg, notification_date"
						+ " FROM " + compdb(comp_id) + "axela_notification"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = notification_emp_id"
						+ " WHERE 1=1 "
						+ " AND emp_id = " + emp_id
						+ " GROUP BY notification_id"
						+ " ORDER BY notification_id DESC "
						+ LimitRecords(0, pagecurrent);
				SOP("StrSql==============WS_Notification_List====================" + StrSql);
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						map.put("entry_name", crs.getString("entry_name"));
						map.put("notification_title", crs.getString("notification_title"));
						map.put("notification_msg", crs.getString("notification_msg"));
						map.put("notification_date", strToLongDate(crs.getString("notification_date")));
						list.add(gson.toJson(map));
					}
					map = null;
					output.put("listdata", list);
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
				if (AppRun().equals("0")) {
					SOP("output =========WS_Notification_List============= " + output);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App======" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return output;
	}
}
