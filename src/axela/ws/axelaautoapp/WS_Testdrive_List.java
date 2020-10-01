package axela.ws.axelaautoapp;

import java.text.DecimalFormat;
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

public class WS_Testdrive_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String enquiry_id = "0";
	public int TotalRecords = 0;
	public String pagecurrent = "";
	public String StrSearch = "";
	public String testdrive_id = "0";
	public String testdrive_fromdate = "", testdrivefromdate = "";
	public String testdrive_todate = "", testdrivetodate = "";
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
	public String filterquery = "";

	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray arr_keywords;

	public JSONObject TestdriveList(JSONObject input, @Context HttpServletRequest request) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input=======EnquiryDash========" + input.toString(1));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
		}
		HttpSession session = request.getSession(true);
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
		if (!input.isNull("testdrive_id")) {
			testdrive_id = CNumeric((String) input.get("testdrive_id"));
		}
		if (!input.isNull("enquiry_id")) {
			enquiry_id = CNumeric((String) input.get("enquiry_id"));
		}
		if (!input.isNull("testdrive_fromdate")) {
			testdrive_fromdate = PadQuotes((String) input.get("testdrive_fromdate"));
		}
		if (!input.isNull("testdrive_todate")) {
			testdrive_todate = PadQuotes((String) input.get("testdrive_todate"));
		}
		if (!testdrive_fromdate.equals("")) {
			testdrivefromdate = ConvertShortDateToStr(testdrive_fromdate);
		}

		if (!testdrive_todate.equals("")) {
			testdrivetodate = ConvertShortDateToStr(testdrive_todate);
		}
		if (!input.isNull("filterquery")) {
			filterquery = JSONPadQuotes((String) input.get("filterquery"));
			if (!filterquery.equals("")) {
				filterquery = new String(Base64.decodeBase64(filterquery.getBytes("ISO-8859-1")));
			}
		}

		if (!testdrive_id.equals("0")) {
			StrSearch = StrSearch + " AND testdrive_id =" + testdrive_id + "";
		} else if (!enquiry_id.equals("0")) {
			StrSearch = StrSearch + " AND testdrive_enquiry_id =" + enquiry_id + "";
		}
		if (!testdrivefromdate.equals("")) {
			StrSearch += " AND SUBSTRING(testdrive_time,1,8) >= SUBSTRING(" + testdrivefromdate + ",1,8)";
		}
		if (!testdrivetodate.equals("")) {
			StrSearch += " AND SUBSTRING(testdrive_time,1,8) <= SUBSTRING(" + testdrivetodate + ",1,8)";
		}

		if (!BranchAccess.equals("")) {
			StrSearch += BranchAccess;
		}
		if (!ExeAccess.equals("")) {
			StrSearch += ExeAccess;
		}
		if (!filterquery.equals("")) {
			StrSearch += filterquery;
		}
		if (!emp_id.equals("0")) {
			try {
				StrSql = "SELECT testdrive_id, enquiry_id, emp_name, customer_name, branch_brand_id,"
						+ " CONCAT(contact_fname, ' ', contact_lname) AS contact_name, contact_mobile1,"
						+ " contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(model_name, '') AS model_name,"
						+ " testdrive_time, location_name";

				CountSql = "SELECT COUNT(DISTINCT testdrive_id) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id= testdrive_fb_status_id"
						+ " WHERE 1 = 1";
				// if (enquiry_id.equals("0")) {
				// SqlJoin += testdrivefromdate + testdrivetodate;
				// }
				// + " AND SUBSTRING(testdrive_time,1,8) =SUBSTRING(" + startdate +
				// ",1,8)";
				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				StrSql += StrSearch + " GROUP BY testdrive_id"
						+ " ORDER BY testdrive_id DESC"
						+ LimitRecords(TotalRecords, pagecurrent);
				CountSql += StrSearch + filterquery;
				// SOP("CountSql===" + CountSql);
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
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
						map.put("branch_brand_id", crs.getString("branch_brand_id"));
						map.put("emp_name", crs.getString("emp_name"));
						map.put("model_name", unescapehtml(crs.getString("model_name")));
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
