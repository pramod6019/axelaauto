package axela.ws.axelaautoapp;

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

public class WS_Preowned_Testdrive_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String enquiry_id = "0";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String StrSearch = "";
	public String testdrive_id = "0";
	public String testdrive_date1 = "";
	public String testdrive_date = "";
	public String testdrivedate = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public String branch_id = "0";
	public String strsearch = "";
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String executive_id = "0";
	public String strSearch2 = "";
	public String brand_ids = "0";
	public String region_ids = "0";
	public String branch_ids = "0";
	public String executive_ids = "0";
	public String model_ids = "0";

	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject PreownedTestdriveList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input =====PreownedTestdrivelist=== " + input);
		}
		HttpSession session = request.getSession(true);
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		CheckAppSession(emp_uuid, comp_id, request);
		if (ReturnPerm(comp_id, "emp_preowned_testdrive_access", request).equals("0")) {
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
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (pagecurrent.equals("1")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}
		if (!input.isNull("so_id")) {
			testdrive_id = CNumeric(PadQuotes((String) input.get("so_id")));
		}
		if (!input.isNull("enquiry_id")) {
			enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
		}
		if (!input.isNull("testdrive_id")) {
			testdrive_id = CNumeric((String) input.get("testdrive_id"));
		}
		if (!input.isNull("testdrive_date")) {
			testdrive_date = PadQuotes((String) input.get("testdrive_date"));
		}
		if (!enquiry_id.equals("0")) {
			StrSearch += " AND testdrive_enquiry_id =" + enquiry_id + "";
		}
		if (testdrive_date.equals("")) {
			testdrive_date = DateToShortDate(kknow());
			testdrive_date1 = ConvertShortDateToStr(testdrive_date);
		}
		else {
			testdrive_date1 = ConvertShortDateToStr(testdrive_date);
		}
		if (!testdrive_id.equals("0")) {
			StrSearch = StrSearch + " AND testdrive_id =" + testdrive_id + "";
			testdrivedate = "";
		} else {
			testdrivedate = " AND SUBSTRING(testdrive_time,1,8) =SUBSTRING(" + testdrive_date1 + ",1,8)";
		}
		if (!BranchAccess.equals("")) {
			StrSearch += BranchAccess;
		}
		if (!ExeAccess.equals("")) {
			StrSearch += ExeAccess;
		}
		if (!emp_id.equals("0")) {
			try {
				StrSql = "SELECT testdrive_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS contact_name,"
						+ " COALESCE(preownedmodel_name, '') AS  preownedmodel_name, enquiry_id,"
						+ " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name, testdrive_time, location_name";

				CountSql = "SELECT COUNT(DISTINCT testdrive_id) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " INNER join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
						+ " INNER join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
						+ " INNER Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
						+ " INNER join " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id"
						// + " left join " + compdb(comp_id) +
						// "axela_sales_testdrive_status on status_id= testdrive_fb_status_id"
						+ " WHERE 1 = 1"
						+ " AND enquiry_enquirytype_id = 2 ";
				SqlJoin += testdrivedate;
				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				StrSql += StrSearch + " GROUP BY testdrive_id"
						+ " ORDER BY testdrive_id DESC"
						+ LimitRecords(TotalRecords, pagecurrent);
				CountSql += StrSearch;
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// SOP("StrSql===preownedtestdrive List===" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						i++;
						map.put("testdrive_id", crs.getString("testdrive_id"));
						map.put("contact_name", crs.getString("contact_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("enquiry_id", crs.getString("enquiry_id"));
						map.put("emp_name", crs.getString("emp_name"));
						map.put("preownedmodel_name", crs.getString("preownedmodel_name"));
						map.put("testdrive_time", strToShortDate(crs.getString("testdrive_time")));
						map.put("location_name", crs.getString("location_name"));
						list.add(gson.toJson(map));
					}
					map.clear();
					output.put("totalrecords", TotalRecords);
					output.put("listdata", list);
					list.clear();
				} else {
					output.put("msg", "No Records Found!");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto =="
						+ this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0]
								.getMethodName() + ": " + ex);
			}
		}
		if (AppRun().equals("0")) {
			SOP("output ===testdrive list==== " + output);
		}
		return output;
	}
}
