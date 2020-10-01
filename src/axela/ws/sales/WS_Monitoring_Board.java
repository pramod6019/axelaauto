package axela.ws.sales;

//divya 26th march 2014

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Monitoring_Board extends ConnectWS {

	public String StrSql = "";
	public String emp_id = "";
	public String soe_id = "";
	public String emp_uuid = "0";
	public String comp_id = "0";
	public String dash = "";
	public String dashemp_id = "", chk_team_lead = "0";
	public String ExeAccess = "";
	public String StrSoe = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list1 = new ArrayList<String>();
	Map<String, String> map1 = new HashMap<String, String>();

	public JSONObject monitoringboard(JSONObject input) throws Exception {
		String confirmed = "";
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			// SOP("emp_id---1----" + emp_id);
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!emp_id.equals("0")) {
				// SOP("emp_id---2-" + emp_id);
				if (!input.isNull("dash")) {
					dash = PadQuotes((String) input.get("dash"));
				}
				if (!dash.equals("yes")) {
					PopulateExecutives();
				}
				if (!input.isNull("dashemp_id")) {
					// SOP("emp_id---3----" + emp_id);
					dashemp_id = CNumeric(PadQuotes((String) input
							.get("dashemp_id")));
					// SOP("dashemp_id-----" + dashemp_id);
				}
				ListMonitoringBoard();
			}
			if (!input.isNull("soe_id")) {
				soe_id = CNumeric(PadQuotes((String) input.get("soe_id")));
			}

		}
		// SOP("output = " + output);
		return output;
	}

	public JSONObject PopulateExecutives() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT empexe.emp_id, empexe.emp_name, empexe.emp_all_exe"
					+ " FROM "
					+ compdb(comp_id) + "axela_emp empexe,"
					+ compdb(comp_id) + "axela_emp emp"
					+ " WHERE 1 = 1 "
					+ " AND emp.emp_id = " + emp_id
					+ " AND if(emp.emp_all_exe='0',(empexe.emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe "
					+ " WHERE empexe_emp_id =" + emp_id + ") OR empexe.emp_id =" + emp_id + "),1=1)"
					+ " AND empexe.emp_sales = '1' AND empexe.emp_active = '1'"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("PopulateSalesExecutives SQL---" + StrSql);
			crs = processQuery(StrSql, 0);
			map.put("emp_id", "0");
			map.put("emp_name", "All Executives");
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
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public void ListMonitoringBoard() {
		String check_team_id = "", check_teamttl_id = "";
		int total_retailtarget = 0;
		int total_enquirytarget = 0;
		int total_enquiryopen = 0;
		int total_enquiryfresh = 0;
		int total_enquirylost = 0;
		int total_soretail = 0;
		String total_soretail_perc = "0";
		int total_pendingenquiry = 0;
		int total_cancellation = 0;
		int total_pendingbooking = 0;
		String total_pendingbooking_perc = "0";
		int total_pendingdelivery = 0;
		int total_eventplanned = 0;
		int total_eventactual = 0;
		int total_testdrives = 0;
		int total_enquiryrecvd = 0;
		String total_testdrives_perc = "0";
		int total_kpitestdrives = 0;
		int total_homevisit = 0;
		String total_homevisit_perc = "0";
		int total_kpihomevisit = 0;
		int total_mga_sales = 0;
		int total_maruti_insurance = 0;
		int total_extwarranty = 0;
		int total_fincases = 0;
		int total_exchange = 0;
		int total_evaluation = 0;
		int team_retailtarget = 0;
		int team_enquirytarget = 0;
		int team_enquiryopen = 0;
		int team_enquiryfresh = 0;
		int team_enquirylost = 0;
		int team_soretail = 0;
		String team_soretail_perc = "0";
		int team_pendingenquiry = 0;
		int team_pendingbooking = 0;
		String team_pendingbooking_perc = "0";
		int team_pendingdelivery = 0;
		int team_testdrives = 0;
		String team_testdrives_perc = "0";
		int team_kpitestdrives = 0;
		int team_homevisit = 0;
		String team_homevisit_perc = "0";
		int team_kpihomevisit = 0;
		int team_mga_sales = 0;
		int team_maruti_insurance = 0;
		int team_extwarranty = 0;
		int team_fincases = 0;
		int team_exchange = 0;
		int team_evaluation = 0;

		String date = ToLongDate(kknow()).substring(0, 6);
		// SOP("date----" + date);
		double salesratio = 0, testdriveratio = 0;
		String empfilter = "";
		if (!dashemp_id.equals("0")) {
			ExeAccess = " AND emp_id = " + dashemp_id + "";
		} else {
			ExeAccess = " AND IF((SELECT emp_all_exe from "
					+ compdb(comp_id) + "axela_emp "
					+ " WHERE emp_id=" + emp_id
					// + " AND emp_uuid='"+ emp_uuid + "'"
					+ ")=0, (emp_id in (SELECT empexe_id from "
					+ compdb(comp_id)
					+ "axela_emp_exe WHERE empexe_emp_id =" + emp_id + ") OR emp_id ="
					+ emp_id + "),emp_id > 0)";
		}
		try {
			StrSql = " SELECT emp_id, emp_name, emp_ref_no,";
			StrSql += " @open:=(SELECT COALESCE(count(enquiry_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE 1=1 "
					+ " AND enquiry_emp_id = emp_id"
					+ " AND enquiry_status_id=1 "
					+ " AND substr(enquiry_date,1,6) < " + date
					+ " ) as enquiryopen,"

					// retailtarget
					+ " COALESCE((SELECT sum(modeltarget_so_count)"
					+ " from " + compdb(comp_id) + "axela_sales_target_model "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target on target_id = modeltarget_target_id "
					+ " WHERE 1=1 "
					+ " AND target_emp_id = emp_id"
					+ " AND substring(target_startdate,1,6) >= " + date + " "
					+ " AND substring(target_enddate,1,6) <= " + date
					+ " ),'0') as retailtarget,"

					// enquirytarget
					+ " COALESCE((SELECT sum(modeltarget_enquiry_count)"
					+ " from " + compdb(comp_id) + "axela_sales_target_model  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target  on target_id =modeltarget_target_id"
					+ " WHERE 1=1 "
					+ " AND target_emp_id = emp_id"
					+ " AND substring(target_startdate,1,6) >= " + date + " "
					+ " AND substring(target_enddate,1,6) <= " + date
					+ " ),'0') as enquirytarget,"
					// enquiryopen

					// // enquiryfresh
					+ " @fresh:=(SELECT COALESCE(count(enquiry_id),0)"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry "
					+ " WHERE 1=1 "
					+ " AND enquiry_emp_id = emp_id"
					+ " AND substr(enquiry_date,1,6) >= " + date + " "
					+ " AND substr(enquiry_date,1,6) <= " + date
					+ ") as enquiryfresh,"
					//
					// // enquirylost
					+ " @lost:=(SELECT COALESCE(count(enquiry_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry "
					+ " WHERE 1=1 "
					+ " AND enquiry_emp_id = emp_id"
					+ " AND substr(enquiry_date,1,6) >= " + date + " "
					+ " AND substr(enquiry_date,1,6) <= " + date + " "
					+ " AND (enquiry_status_id=3 or enquiry_status_id=4)) as enquirylost,"
					//
					// // soretail
					+ " @retail:=(SELECT COALESCE(count(distinct so_id),0) from "
					+ compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id = emp_id"
					+ " AND so_active='1' "
					+ " AND (substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date + ") "
					+ ") as soretail,"

					// // pendingenquiry
					+ " (@open+@fresh-@lost-@retail) as pendingenquiry,"

					// // pendingbooking
					+ "(SELECT COALESCE(count(so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id = emp_id"
					+ " AND so_active='1' "
					// + " AND so_delivered_date='' "
					+ " AND substr(so_date,1,6) >= " + date + " "
					+ " AND substr(so_date,1,6) <= " + date
					+ " ) as pendingbooking,"

					// // pendingdelivery
					+ " @delivery:=(SELECT COALESCE(count(so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND so_emp_id = emp_id "
					+ " AND so_active='1' "
					+ " AND so_delivered_date = ''"
					// + " AND substr(so_date,1,6) >= " + date + " "
					// + " AND substr(so_date,1,6) <= " + date
					+ " ) as pendingdelivery,"
					// Cancellation
					+ " @cancellation:=(select coalesce(count(so_id),0) from " + compdb(comp_id) + "axela_sales_so"
					+ " inner join " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " where so_emp_id=emp_id and so_active='0' "
					// + " and so_delivered_date='' "
					+ " and substr(so_date,1,6) >= " + date + ""
					+ " and substr(so_date,1,6) <= " + date
					+ " ) as cancellation,"
					// eventplanned
					// + " (SELECT COALESCE(count(distinct campaign_id),0) from "
					// + compdb(comp_id) + "axela_sales_campaign"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch on camptrans_campaign_id=campaign_id"
					// + " WHERE 1=1"
					// + ExeAccess
					// + " AND campaign_active='1'"
					// + " AND ((substr(campaign_startdate,1,6) >= " + date
					// + " AND substr(campaign_startdate,1,6) < " + date
					// + ")" + " or (substr(campaign_enddate,1,6) > " + date
					// + " AND substr(campaign_enddate,1,6) <= " + date + ")"
					// + " or (substr(campaign_startdate,1,6) < " + date
					// + "  AND substr(campaign_enddate,1,6) > " + date
					// + "))" + " ) as eventplanned,"
					// SOP("emp_id---11----" + emp_id);

					// eventactual
					// + " (SELECT"
					// + " COALESCE(count(distinct campaign_id),0)"
					// + " from " + compdb(comp_id) + "axela_sales_campaign"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_campaign_id=campaign_id"
					// // + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					// + " WHERE 1=1"
					// + ExeAccess
					// + " AND campaign_active='1'"
					// + " AND campaign_id!='1'"
					// + " AND substr(enquiry_date,1,6) = " + date
					// + " ) as eventactual,"

					// // enquiryrecvd
					// + " (SELECT COALESCE(count(enquiry_id),0)"
					// + " from " + compdb(comp_id) + "axela_sales_enquiry"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign on campaign_id=enquiry_campaign_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					// + " WHERE 1=1"
					// + ExeAccess
					// + " AND substr(enquiry_date,1,6) = " + date + ""
					// + " AND campaign_id!='1'"
					// + " AND campaign_active='1' ) as 'enquiryrecvd',"

					// testdrives from Test Drive Table

					// + " @testdrives:=(SELECT COALESCE(count(testdrive_id),0) "
					// + " from " + compdb(comp_id) + "axela_sales_testdrive "
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id=testdrive_enquiry_id "
					// + " WHERE testdrive_emp_id = emp_id "
					// + " AND substr(testdrive_time,1,6) >= " + date + " "
					// + " AND substr(testdrive_time,1,6) <= " + date + " "
					// + "" + ") as 'testdrives',"
					// testdrives from Enquiry Followup Table

					+ " @testdrives:=(SELECT COALESCE(count(testdrive_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id=testdrive_enquiry_id "
					+ " WHERE 1=1 "
					+ " AND enquiry_emp_id = emp_id"
					// + " AND followup_desc LIKE 'Test Drive Taken%'"
					+ " AND testdrive_fb_taken = 1"
					+ " AND substr(testdrive_time,1,6) >= " + date
					+ " AND substr(testdrive_time,1,6) <= " + date
					+ " ) as 'testdrives',"
					// // testdrives_perc
					// + " (@testdrives/@fresh*100) as 'testdrives_perc',"

					// KPI Test Drives
					+ " (SELECT COALESCE(count(DISTINCT enquiry_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " WHERE 1=1"
					+ " AND enquiry_emp_id = emp_id"
					+ " AND testdrive_fb_taken = 1"
					+ " AND substr(testdrive_time,1,6) >= " + date + " "
					+ " AND substr(testdrive_time,1,6) <= " + date
					+ " ) as 'kpitestdrives',"

					// // home visit
					+ " @homevisit:=(SELECT COALESCE(count(followup_id),0) "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = followup_enquiry_id "
					+ " WHERE 1=1"
					+ " AND enquiry_emp_id = emp_id"
					+ " AND followup_desc LIKE 'Home Visit Done%'"
					+ " AND substr(followup_followup_time,1,6) >= " + date + ""
					+ " AND substr(followup_followup_time,1,6) <= " + date
					+ ") as 'homevisit',"
					// SOP("emp_id---13----" + emp_id);
					// KPI home visit
					+ "(SELECT COALESCE(count(DISTINCT enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id=followup_enquiry_id "
					+ " WHERE 1=1"
					+ " AND enquiry_emp_id=emp_id"
					+ " AND followup_desc LIKE 'Home Visit Done%'"
					+ " AND substr(followup_followup_time,1,6) >= " + date + " "
					+ " AND substr(followup_followup_time,1,6) <= " + date
					+ " ) as 'kpihomevisit',"

					// // + " (@homevisit/@fresh*100) as 'homevisit_perc',"
					// // mga amount

					+ "(SELECT COALESCE(sum(so_mga_amount),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " where so_emp_id=emp_id AND so_active='1' "
					+ " AND substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date
					+ "  ) as mga_amount,"

					// // maruti insurance
					+ "(SELECT COALESCE(count(DISTINCT so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id = emp_id"
					+ " AND so_active='1' AND so_insur_amount > 0"
					+ " AND substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date
					+ " ) as maruti_insur,"

					// // extended warranty

					+ "(SELECT COALESCE(count(DISTINCT so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id=emp_id"
					+ " AND so_active='1' AND so_ew_amount > 0"
					+ " AND substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date
					+ " )  as 'extended_warranty',"

					// // fin cases
					+ "(SELECT COALESCE(count(DISTINCT so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id=emp_id"
					+ " AND so_active = '1' "
					+ " AND so_finance_amt > 0"
					+ " AND so_fintype_id = 1"
					+ " AND substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date
					+ "  ) as 'fin_cases',"

					// exchange
					+ "(SELECT COALESCE(count(DISTINCT so_id),0) from " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " WHERE 1=1 "
					+ " AND so_emp_id = emp_id"
					+ " AND so_active = '1' AND so_exchange_amount > 0"
					+ " AND substr(so_delivered_date,1,6) >= " + date + " "
					+ " AND substr(so_delivered_date,1,6) <= " + date
					+ " ) as 'exchange',"

					+ " (select coalesce(count(enquiry_id),0) from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where enquiry_emp_id=emp_id "
					+ " and enquiry_evaluation = 1 "
					+ " and substr(enquiry_date,1,6) >= " + date + ""
					+ " AND substr(enquiry_date,1,6) <= " + date + ""
					+ " )  as 'evaluation' "
					// + " and substr(enquiry_date,1,6) < substr('" + date + "',1,8) as evaluation "

					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE emp_sales = 1"
					+ " AND emp_active = 1"
					+ ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql------monitoring board---- " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int no = 0;
				while (crs.next()) {
					// SOP("n is====" + no++);

					total_retailtarget = total_retailtarget + crs.getInt("retailtarget");
					total_enquirytarget = total_enquirytarget + crs.getInt("enquirytarget");
					total_enquiryopen = total_enquiryopen + crs.getInt("enquiryopen");
					total_enquiryfresh = total_enquiryfresh + crs.getInt("enquiryfresh");
					total_enquirylost = total_enquirylost + crs.getInt("enquirylost");
					total_soretail = total_soretail + crs.getInt("soretail");
					total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
					total_pendingenquiry = total_pendingenquiry + crs.getInt("pendingenquiry");
					total_cancellation = total_cancellation + crs.getInt("cancellation");
					total_pendingbooking = total_pendingbooking + crs.getInt("pendingbooking");
					total_pendingbooking_perc = getPercentage((double) total_pendingbooking, (double) total_enquiryfresh);
					total_pendingdelivery = total_pendingdelivery + crs.getInt("pendingdelivery");
					// total_eventplanned = total_eventplanned + crs.getInt("eventplanned");
					// total_eventactual = total_eventactual + crs.getInt("eventactual");
					// total_enquiryrecvd = total_enquiryrecvd + crs.getInt("enquiryrecvd");
					total_testdrives = total_testdrives + crs.getInt("testdrives");
					total_kpitestdrives = total_kpitestdrives + crs.getInt("kpitestdrives");
					total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
					total_homevisit = total_homevisit + crs.getInt("homevisit");
					total_kpihomevisit = total_kpihomevisit + crs.getInt("kpihomevisit");
					// total_homevisit = total_homevisit + crs.getInt("homevisit");
					// total_kpihomevisit = total_kpihomevisit + crs.getInt("kpihomevisit");
					total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
					total_mga_sales = total_mga_sales + crs.getInt("mga_amount");
					total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
					total_extwarranty = total_extwarranty + crs.getInt("extended_warranty");
					total_fincases = total_fincases + crs.getInt("fin_cases");
					total_exchange = total_exchange + crs.getInt("exchange");
					total_evaluation = total_evaluation + crs.getInt("evaluation");
					// team_evaluation = team_evaluation + crs.getInt("exchange");
					// map.put("retailtarget", crs.getString("retailtarget"));
					// map.put("enquirytarget", crs.getString("enquirytarget"));
					// map.put("enquiryopen", crs.getString("enquiryopen"));
					// map.put("enquiryfresh", crs.getString("enquiryfresh"));
					// map.put("enquirylost", crs.getString("enquirylost"));
					// map.put("soretail", crs.getString("soretail"));
					// map.put("pendingenquiry", crs.getString("pendingenquiry"));
					// map.put("pendingbooking", crs.getString("pendingbooking"));
					// // map.put("eventplanned", crs.getString("eventplanned"));
					// // map.put("eventactual", crs.getString("eventactual"));
					// // map.put("enquiryrecvd", crs.getString("enquiryrecvd"));
					// // map.put("testdrives", crs.getString("testdrives"));
					// map.put("homevisit", crs.getString("homevisit"));
					// map.put("mga_amount", crs.getString("mga_amount"));
					// map.put("maruti_insur", crs.getString("maruti_insur"));
					// map.put("extended_warranty", crs.getString("extended_warranty"));
					// map.put("fin_cases", crs.getString("fin_cases"));
					// map.put("exchange", crs.getString("exchange"));
					// list.add(gson.toJson(map));

					// map1.put("value", total_retailtarget+ "");
					// map1.put("name", "Retail Target:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("enquirytarget"));
					// map1.put("name", "Enquiry Target:");
					// list1.add(gson.toJson(map1));

					// map1.put("value", crs.getString("enquirytarget"));
					// map1.put("name", "Enquiry Target:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("enquiryopen"));
					// map1.put("name", "Open Enquiry:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("enquiryfresh"));
					// map1.put("name", "Fresh Enquiry:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("enquirylost"));
					// map1.put("name", "Lost Enquiry:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("soretail"));
					// map1.put("name", "Retail:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", total_soretail_perc);
					// map1.put("name", "Retail %:");
					// list1.add(gson.toJson(map1));

					// map1.put("value", crs.getString("team_homevisit_perc"));
					// map1.put("name", "Team Homevisit Percentage:");
					// list1.add(gson.toJson(map1));
					//
					// map1.put("value", crs.getString("total_soretail_perc"));
					// map1.put("name", "Total So Retail Percentage:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("pendingenquiry"));
					// // map1.put("value", crs.getString("team_pendingbooking_perc"));
					// // map1.put("name", "Team Pending Booking Percentage:");
					// // list1.add(gson.toJson(map1));
					// map1.put("name", "Total Pending Enquiry:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("pendingbooking"));
					// map1.put("name", "Total Pending Booking:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", total_pendingbooking_perc);
					// map1.put("name", "Total Pending Booking Percentage %:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("pendingdelivery"));
					// map1.put("name", "Total Pending Delivery:");
					// list1.add(gson.toJson(map1));
					//
					// map1.put("value", crs.getString("eventplanned"));
					// map1.put("name", "Event Planned:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("eventactual"));
					// map1.put("name", "Event Actual:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("enquiryrecvd"));
					// map1.put("name", "Enquiry Received:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("testdrives"));
					// map1.put("name", "Test Drive(s):");
					// list1.add(gson.toJson(map1));
					// map1.put("value", total_testdrives_perc);
					// map1.put("name", "Team Test Drive Percentage %:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("kpitestdrives"));
					// map1.put("name", "Kpi Test Drive(s):");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("homevisit"));
					// map1.put("name", "Home Visit:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("kpihomevisit"));
					// map1.put("name", "Kpi Home Visit:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("mga_amount"));
					// map1.put("name", "Accessories Amount:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("maruti_insur"));
					// map1.put("name", "Insurance:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("extended_warranty"));
					// map1.put("name", "Extended Warranty:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("fin_cases"));
					// map1.put("name", "Fin Cases:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("exchange"));
					// map1.put("name", "Exchange:");
					// list1.add(gson.toJson(map1));
					// map1.put("value", crs.getString("team_evaluation"));
					// map1.put("name", "Evaluation:");
					// list1.add(gson.toJson(map1));
				}
				map1.put("value", total_retailtarget + "");
				map1.put("name", "Retail Target:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_enquirytarget + "");
				map1.put("name", "Enquiry Target:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_enquiryopen + "");
				map1.put("name", "Open Enquiry:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_enquiryfresh + "");
				map1.put("name", "Fresh Enquiry:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_enquirylost + "");
				map1.put("name", "Lost Enquiry:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_soretail + "");
				map1.put("name", "Retail:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_soretail_perc + "");
				map1.put("name", "Retail %:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_pendingenquiry + "");
				map1.put("name", "Pending Enquiry:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_pendingbooking + "");
				map1.put("name", "Booking:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_pendingbooking_perc + "");
				map1.put("name", "Booking %:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_pendingdelivery + "");
				map1.put("name", "Pending Delivery:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_cancellation + "");
				map1.put("name", "Cancellation:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_testdrives + "");
				map1.put("name", "Test Drives:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_kpitestdrives + "");
				map1.put("name", "KPI Test Drives:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_testdrives_perc + "");
				map1.put("name", "Test Drives %:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_homevisit + "");
				map1.put("name", "Home Visit:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_kpihomevisit + "");
				map1.put("name", "KPI Home Visit:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_homevisit_perc + "");
				map1.put("name", "KPI Home Visit %:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_mga_sales + "");
				map1.put("name", "Accessories Amount:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_maruti_insurance + "");
				map1.put("name", "Insurance:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_extwarranty + "");
				map1.put("name", "Extended Warranty:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_fincases + "");
				map1.put("name", "Finance Cases:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_exchange + "");
				map1.put("name", "Exchange:");
				list1.add(gson.toJson(map1));
				map1.put("value", total_evaluation + "");
				map1.put("name", "Evaluation:");
				list1.add(gson.toJson(map1));
				// map.clear();
				// list.clear();
				// output.put("listdashboard", list);

				map1.clear();
				output.put("monitoringboard", list1);
				list1.clear();
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
