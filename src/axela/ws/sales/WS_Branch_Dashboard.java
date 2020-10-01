package axela.ws.sales;

//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Branch_Dashboard extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String dash = "";
	public String emp_uuid = "0";
	public String ExeAccess = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list1 = new ArrayList<String>();
	Map<String, String> map1 = new HashMap<String, String>();

	public JSONObject dashboard(JSONObject input) throws Exception {
		String confirmed = "";
		if (AppRun().equals("0")) {
			SOP("input = " + input);
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
				if (!input.isNull("dash")) {
					dash = PadQuotes((String) input.get("dash"));
				}
				if (!dash.equals("yes")) {
					PopulateExecutives();
				}
				ListDashboard();

			}
		}
		SOP("output = " + output);
		return output;
	}

	public JSONObject PopulateExecutives() {
		CachedRowSet crs = null;
		try {
			// ExeAccess = WSCheckExeAccess(emp_id);
			StrSql = "SELECT emp_id, emp_name" + " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1 AND emp_sales = '1' AND emp_active = '1'"
					+ " " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("PopulateSalesExecutives SQL---" + StrSql);
			crs = processQuery(StrSql, 0);
			map.put("emp_id", "0");
			map.put("emp_name", "Select");
			list.add(gson.toJson(map));
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatesalesexecutive", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public void ListDashboard() {
		String date = ToLongDate(kknow()).substring(0, 6);
		double salesratio = 0, testdriveratio = 0;
		try {
			StrSql = "select emp_name,"
					// ----------enquirycount---------
					+ " (select coalesce(count(distinct enquiry_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where 1=1"
					+ " and substr(enquiry_date,1,6) >= " + date + ""
					+ " and enquiry_emp_id=emp_id) as enquirycount,"
					// ----------enquiryopen---------
					+ " (select coalesce(count(distinct enquiry_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where 1=1"
					+ " and enquiry_status_id = 1"
					+ " and substr(enquiry_date,1,6) >= " + date + ""
					+ " and enquiry_emp_id = emp_id) as enquiryopen,"
					// ----------enquiryclosed---------
					+ " (select coalesce(count(distinct enquiry_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where 1=1"
					+ " and substr(enquiry_date,1,6) >= " + date + ""
					+ " and enquiry_status_id > 2"
					+ " and enquiry_emp_id = emp_id) as enquiryclosed,"
					// ----------enquiryhot---------
					+ " (select coalesce(count(distinct enquiry_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where 1=1 "
					+ " and (enquiry_priorityenquiry_id = 1)"
					+ " and enquiry_status_id=1"
					+ " and enquiry_emp_id = emp_id) as enquiryhot,"
					// ----------meetingplanned---------
					+ " (select coalesce(count(distinct followup_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on enquiry_id = followup_enquiry_id "
					+ " where 1=1"
					+ " and substr(followup_followup_time,1,6) >= " + date + ""
					+ " and followup_followuptype_id = 2"
					+ " and followup_emp_id = emp_id) as meetingplanned,"
					// ----------meetingcompleted---------
					+ " (select coalesce(count(distinct followup_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on enquiry_id= followup_enquiry_id"
					+ " where 1=1"
					+ " and substr(followup_followup_time,1,6) >= " + date + ""
					+ " and followup_feedbacktype_id = 2"
					+ " and followup_emp_id=emp_id) as meetingcompleted,"
					// ----------enquiryfollowup---------
					+ " (select coalesce(count(distinct followup_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on enquiry_id = followup_enquiry_id "
					+ " where 1=1"
					+ " and substr(followup_followup_time,1,6) >= " + date + ""
					+ " and followup_emp_id = emp_id) as enquiryfollowup, "
					// ----------followupesc---------
					+ " (select coalesce(count(distinct followup_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on enquiry_id = followup_enquiry_id "
					+ " where 1=1"
					+ " and substr(followup_followup_time,1,6) >= "
					+ date
					+ ""
					+ " and followup_trigger = 5"
					+ " and followup_emp_id = emp_id) as followupesc, "
					// ----------enquiry_testdrivesplanned---------
					+ " (select coalesce(count(testdrive_id),0) "
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_testdrive on enquiry_id = testdrive_enquiry_id"
					+ " where 1=1"
					+ " and substr(testdrive_time,1,6) >= "
					+ date
					+ ""
					+ " and testdrive_emp_id = emp_id) as enquiry_testdrivesplanned, "
					// ----------enquiry_testdrivescompleted---------
					+ " (select coalesce(count(distinct testdrive_id),0)"
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_testdrive on enquiry_id= testdrive_enquiry_id"
					+ " where 1=1"
					+ " and substr(testdrive_time,1,6) >= "
					+ date
					+ ""
					+ " and testdrive_fb_taken = 1 and testdrive_fb_entry_date != ''"
					+ " and testdrive_emp_id = emp_id) as enquiry_testdrivescompleted, "
					// ----------enquiry_KPItestdrives---------
					+ " (select coalesce(count(distinct enquiry_id),0) "
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_testdrive on enquiry_id= testdrive_enquiry_id "
					+ " where 1=1"
					+ " and substr(testdrive_time,1,6) >= "
					+ date
					+ ""
					+ " and testdrive_type = 1 AND testdrive_fb_taken = 1"
					+ " and testdrive_fb_entry_date != ''"
					+ " and testdrive_emp_id=emp_id) as enquiry_KPItestdrives, "
					// ----------enquirycustomertestdrivefeedback---------
					+ " (select coalesce(count(distinct testdrive_id),0) "
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_testdrive on enquiry_id= testdrive_enquiry_id "
					+ " where 1=1"
					+ " and substr(testdrive_client_fb_entry_date,1,6) >= "
					+ date
					+ ""
					+ " and testdrive_fb_taken = 1"
					+ " and testdrive_fb_entry_date != ''"
					+ " and testdrive_emp_id=emp_id) as enquirycustomertestdrivefeedback,"
					// ----------quotecount---------
					+ " (select coalesce(count(distinct quote_id),0)"
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_quote"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_quote_item on quoteitem_quote_id=quote_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item on quoteitem_item_id=item_id"
					+ " where 1=1"
					+ " and substr(quote_date,1,6) >= "
					+ date
					+ ""
					+ " and quote_active = '1'  "
					+ " and quote_emp_id = emp_id) as quotecount, "
					// ----------quotekpicount---------
					+ " (select coalesce(count(distinct quote_enquiry_id),0)"
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_quote "
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_quote_item on quoteitem_quote_id=quote_id"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item on quoteitem_item_id=item_id"
					+ " where 1=1"
					+ " and substr(quote_date,1,6) >= "
					+ date
					+ ""
					+ " and quote_active = '1' "
					+ " and quote_emp_id = emp_id) as quotekpicount,"
					// ----------socount---------
					+ " (select coalesce(count(distinct so_id))"
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_so"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_so_item on soitem_so_id=so_id and soitem_rowcount > 0"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item on soitem_item_id=item_id"
					+ " where 1=1"
					+ " and substr(so_date,1,6) >= "
					+ date
					+ ""
					+ " and so_active = '1'"
					+ " and so_emp_id = emp_id) as socount,  "
					// ----------soamount---------
					+ " (select coalesce(sum(so_grandtotal),0)"
					+ " from "
					+ compdb(comp_id)
					+ "axela_sales_so"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount > 0"
					+ " inner join "
					+ compdb(comp_id)
					+ "axela_inventory_item on soitem_item_id = item_id"
					+ " where 1=1"
					+ " and substr(so_date,1,6) >= "
					+ date
					+ ""
					+ " and so_active = '1' and so_emp_id=emp_id) as soamount, "
					// ----------receiptcount---------
					// + " (select coalesce(count(distinct receipt_id))"
					// + " from "
					// + compdb(comp_id)
					// + "axela_invoice_receipt"
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_invoice on invoice_id = receipt_invoice_id"
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_sales_so on so_id = invoice_so_id "
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount > 0 "
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_inventory_item on soitem_item_id = item_id"
					// + " where 1=1"
					// + " and substr(receipt_date,1,6) >= "
					// + date
					// + ""
					// + " and receipt_active = '1'"
					// + " and receipt_emp_id = emp_id) as receiptcount,"
					+ " 0  as receiptcount,"
					// ----------receiptamount---------
					// + " (select coalesce(sum(receipt_amount),0)"
					// + " from "
					// + compdb(comp_id)
					// + "axela_invoice_receipt"
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_invoice on invoice_id = receipt_invoice_id"
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_sales_so on so_id = invoice_so_id "
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount > 0 "
					// + " inner join "
					// + compdb(comp_id)
					// + "axela_inventory_item on soitem_item_id = item_id"
					// + " where 1=1"
					// + " and substr(receipt_date,1,6) >= "
					// + date
					// + ""
					// + " and receipt_active = '1'"
					// + " and receipt_emp_id = emp_id) as receiptamount "
					+ " 0  as receiptamount"
					// --------------------------------
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_emp"
					+ " left join "
					+ compdb(comp_id)
					+ "axela_branch on branch_id = emp_branch_id"
					+ " where emp_active = '1'"
					+ " and emp_sales = '1'"
					+ " AND emp_id = "
					+ emp_id
					+ ""
					+ " and (emp_branch_id = branch_id"
					+ " or emp_id in (select empbr.emp_id"
					+ " from "
					+ compdb(comp_id)
					+ "axela_emp_branch empbr where "
					+ compdb(comp_id)
					+ "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = branch_id))"
					+ " group by emp_name, emp_id " + " order by emp_name";
			// SOP("STRSQL=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				// }
				while (crs.next()) {
					if (!crs.getString("enquiry_KPItestdrives").equals("0")
							&& !crs.getString("enquirycount").equals("0")) {
						testdriveratio = (crs.getDouble("enquiry_KPItestdrives") / crs
								.getDouble("enquirycount")) * 100;
						testdriveratio = Math.round(testdriveratio * 100.0) / 100.0;
					}
					if (!crs.getString("socount").equals("0")
							&& !crs.getString("enquirycount").equals("0")) {
						salesratio = (crs.getDouble("socount") / crs
								.getDouble("enquirycount")) * 100;
						salesratio = Math.round(salesratio * 100.0) / 100.0;
					}
					// map.put("emp_name", crs.getString("emp_name"));
					map.put("enquirycount", crs.getString("enquirycount"));
					map.put("enquiryopen", crs.getString("enquiryopen"));
					map.put("enquiryclosed", crs.getString("enquiryclosed"));
					map.put("enquiryhot", crs.getString("enquiryhot"));
					map.put("meetingplanned", crs.getString("meetingplanned"));
					map.put("meetingcompleted",
							crs.getString("meetingcompleted"));
					map.put("enquiryfollowup", crs.getString("enquiryfollowup"));
					map.put("followupesc", crs.getString("followupesc"));
					map.put("enquiry_testdrivesplanned",
							crs.getString("enquiry_testdrivesplanned"));
					map.put("enquiry_testdrivescompleted",
							crs.getString("enquiry_testdrivescompleted"));
					map.put("enquiry_KPItestdrives",
							crs.getString("enquiry_KPItestdrives"));
					map.put("testdriveratio", testdriveratio + "");
					map.put("enquirycustomertestdrivefeedback",
							crs.getString("enquirycustomertestdrivefeedback"));
					map.put("quotecount", crs.getString("quotecount"));
					map.put("quotekpicount", crs.getString("quotekpicount"));
					map.put("socount", crs.getString("socount"));
					map.put("salesratio", salesratio + "");
					map.put("soamount", crs.getString("soamount"));
					map.put("receiptcount", crs.getString("receiptcount"));
					map.put("receiptamount", crs.getString("receiptamount"));
					list.add(gson.toJson(map));

					// new data
					map1.put("value", crs.getString("enquirycount"));
					map1.put("name", "Enquiry:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiryopen"));
					map1.put("name", "Open Enquiry:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiryclosed"));
					map1.put("name", "Closed Enquiry:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiryhot"));
					map1.put("name", "Hot Enquiry:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("meetingplanned"));
					map1.put("name", "Meetings Planned:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("meetingcompleted"));
					map1.put("name", "Meetings Completed:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiryfollowup"));
					map1.put("name", "Follow-Up:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("followupesc"));
					map1.put("name", "Escalation:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiry_testdrivesplanned"));
					map1.put("name", "TD Planned:");
					list1.add(gson.toJson(map1));
					map1.put("value",
							crs.getString("enquiry_testdrivescompleted"));
					map1.put("name", "TD Completed:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("enquiry_KPItestdrives"));
					map1.put("name", "KPI TD:");
					list1.add(gson.toJson(map1));
					map1.put("value", testdriveratio + "");
					map1.put("name", "TD Ratio:");
					list1.add(gson.toJson(map1));
					map1.put("value",
							crs.getString("enquirycustomertestdrivefeedback"));
					map1.put("name", "Customer TD Feedback:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("quotecount"));
					map1.put("name", "Quotes:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("quotekpicount"));
					map1.put("name", "KPI Quotes:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("socount"));
					map1.put("name", "Sales Orders:");
					list1.add(gson.toJson(map1));
					map1.put("value", salesratio + "");
					map1.put("name", "Sales Ration:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("soamount"));
					map1.put("name", "Sales Amount:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("receiptcount"));
					map1.put("name", "Receipts:");
					list1.add(gson.toJson(map1));
					map1.put("value", crs.getString("receiptamount"));
					map1.put("name", "Receipts Amount:");
					list1.add(gson.toJson(map1));

				}

				map.clear();
				output.put("listdashboard", list);
				map1.clear();
				output.put("listbranchdashboard", list1);
				list.clear();
			} else {
				output.put("msg", "No Details Found!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

	}
}
