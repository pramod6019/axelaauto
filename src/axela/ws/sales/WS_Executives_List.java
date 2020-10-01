/* Annappa (29th April 2015) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Executives_List extends ConnectWS {

	public String update = "";
	public String updateB = "";
	public String delete = "";
	public String submitB = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String add = "";
	public String addB = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_status = "";
	public String emp_uuid = "0";
	public String role_id = "";
	public String branch_id = "";
	public String emp_photo = "";
	public String keyword = "";
	public String keyword_search = "";
	public String status = "";
	public String pagecurrent = "";
	public int TotalRecords = 0;
	public String SqlJoin = "";
	public String CountSql = "";
	// public String enquiry_decisiontime_id = "0";
	// ws
	Gson gson = new Gson();
	// JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();

	public JSONObject ExecutivesList(JSONObject input) {
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
					if (!input.isNull("emp_status")) {
						emp_status = PadQuotes((String) input.get("emp_status"));
						// SOP("emp_status---------" + emp_status);
					}
					if (!input.isNull("keyword")) {
						keyword = PadQuotes((String) input.get("keyword"));
						// SOP("emp_status---------" + emp_status);
					}
					if (!input.isNull("pagecurrent")) {
						pagecurrent = PadQuotes((Integer) input.get("pagecurrent") + "");
					}

					if (!input.isNull("keyword")) {
						keyword = PadQuotes((String) input.get("keyword"));
						if (!keyword.equals("")) {
							keyword_search = " and (emp_id like '%" + keyword + "%' "
									+ " or emp_name like '%" + keyword + "%'"
									+ " or emp_mobile1 like '%" + keyword + "%'"
									+ " or emp_mobile2 like '%" + keyword + "%'"
									+ " or emp_email1 like '%" + keyword + "%'"
									+ " or emp_email2 like '%" + keyword + "%')";
						} else {
							keyword_search = "";
						}

					}

					StrSql = " SELECT emp_id, emp_name, emp_mobile1, emp_email1, emp_photo, emp_status,"
							+ " jobtitle_id, jobtitle_desc,"
							+ " case when emp_id = " + emp_id + " then 1"
							+ " when emp_photo != '' then 2"
							+ " else 3 end as emporder";

					// CountSql = "SELECT COUNT(DISTINCT emp_id)";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_emp"
							+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
							+ " WHERE emp_active = 1 " + keyword_search;
					// + " ORDER BY emp_name"
					// + LimitRecords(TotalRecords, pagecurrent);
					// StrSql += SqlJoin;
					StrSql = StrSql + SqlJoin;
					StrSql = StrSql + " ORDER BY emporder, emp_name ";
					// + LimitRecords(TotalRecords, pagecurrent);
					// CountSql += SqlJoin;
					// TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
					// SOP("strsql======" + StrSql);
					CachedRowSet crs =processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("emp_id", crs.getString("emp_id"));
							map.put("emp_name", crs.getString("emp_name"));
							map.put("emp_mobile1", crs.getString("emp_mobile1"));
							map.put("emp_email1", crs.getString("emp_email1"));
							map.put("emp_status", crs.getString("emp_status"));
							map.put("emp_photo", crs.getString("emp_photo"));
							map.put("jobtitle_desc", crs.getString("jobtitle_desc"));
							list.add(gson.toJson(map));
						}
						// SOP("TotalRecords=====" + TotalRecords);
						map.clear();
						// output.put("totalrecords", TotalRecords);
						output.put("executivelist", list);

						list.clear();

					} else {
						output.put("msg", "No Records Found");
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOPError("output = " + output);
		}
		return output;
	}
}
