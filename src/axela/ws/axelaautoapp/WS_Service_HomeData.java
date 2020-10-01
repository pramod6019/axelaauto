package axela.ws.axelaautoapp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Service_HomeData extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String emp_uuid = "0";
	public String StrJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrLibrary = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String smart = "";
	public String ticketchart_data = "";
	public String callchart_data = "";
	public String jcprioritychart_data = "";
	public String servicefollowupescchart_data = "";
	public String psfchart_data = "";
	public String insurancechart_data = "";
	public int TotalRecords = 0;

	public String datetype = "", StrHTML = "";
	public String curr_date = ToShortDate(kknow());
	public String back_date = "";
	public String curr_month = "", back_month = "";
	public String startquarter = "", endquarter = "";
	public String back_startquarter = "", back_endquarter = "";

	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] jc_branch_ids;

	// / Today
	public String veh_countday = "0";
	public String jc_countday = "0";
	public String ticket_countday = "0";
	public String service_booking_countday = "0";
	public String parts_amtday = "0";
	public String labour_amtday = "0";
	// Month
	public String veh_countmonth = "0";
	public String jc_countmonth = "0";
	public String ticket_countmonth = "0";
	public String service_booking_countmonth = "0";
	public String parts_amtmonth = "0";
	public String labour_amtmonth = "0";
	// Quarter
	public String veh_countquarter = "0";
	public String jc_countquarter = "0";
	public String ticket_countquarter = "0";
	public String service_booking_countquarter = "0";
	public String parts_amtquarter = "0";
	public String labour_amtquarter = "0";

	// Today
	public String veh_countday_old = "0";
	public String jc_countday_old = "0";
	public String ticket_countday_old = "0";
	public String service_booking_countday_old = "0";
	public String parts_amtday_old = "0";
	public String labour_amtday_old = "0";
	// Month
	public String veh_countmonth_old = "0";
	public String jc_countmonth_old = "0";
	public String ticket_countmonth_old = "0";
	public String service_booking_countmonth_old = "0";
	public String parts_amtmonth_old = "0";
	public String labour_amtmonth_old = "0";
	// Quarter
	public String veh_countquarter_old = "0";
	public String jc_countquarter_old = "0";
	public String ticket_countquarter_old = "0";
	public String service_booking_countquarter_old = "0";
	public String parts_amtquarter_old = "0";
	public String labour_amtquarter_old = "0";
	public String branch_id = "0";
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String StrSearch = "";
	public String team_emp_id = "0";
	public String executive_id = "0";
	// refresh All AJAX
	public String refreshAll = "", cards = "";
	// for redirect
	public String filter = "";
	public String timefilter = "";
	public String opt = "";
	String period = "";
	public String emp_branch_id = "0";
	public String ticketescstatus = "";
	public String jcpriorityescstatus = "";
	public String servicefollowupescstatus = "";
	public String jcpsfescstatus = "";
	public String openjobcards = "";
	public String opentickets = "";
	public String branch_ids = "";
	public String zone_id = "";
	public String region_ids = "";
	public String brand_ids = "";
	public String zone_ids = "";
	public String startdate = "";
	public String enddate = "";
	public String branchcount = "0";
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Executive_Univ_Check update = new Executive_Univ_Check();
	Gson gson = new Gson();
	DecimalFormat df = new DecimalFormat("0.00");

	public JSONObject servicehome(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			emp_uuid = PadQuotes((String) input.get("emp_uuid"));
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			SetSession("comp_id", comp_id, request);
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
			if (AppRun().equals("0")) {
				SOP("input homedata for testing ======== " + input);
			}
			if (!input.isNull("startdate")) {
				startdate = ConvertShortDateToStr(PadQuotes(((String) input.get("startdate"))));
			}
			if (!input.isNull("enddate")) {
				enddate = ConvertShortDateToStr(PadQuotes(((String) input.get("enddate"))));
			}
			if (!input.isNull("brand_ids")) {
				brand_ids = PadQuotes((String) input.get("brand_ids"));
			}
			if (!input.isNull("region_ids")) {
				region_ids = PadQuotes((String) input.get("region_ids"));
			}
			if (!input.isNull("branch_ids")) {
				branch_ids = PadQuotes((String) input.get("branch_ids"));
			}
			if (!input.isNull("zone_ids")) {
				zone_ids = PadQuotes((String) input.get("zone_ids"));
			}
			StrSql = "SELECT COUNT(branch_id) AS branchcount,"
					+ " IF(COUNT(branch_id) = 1, branch_id, 0) AS branch_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			// SOP("StrSql======COUNT=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				branchcount = CNumeric(crs.getString("branchcount"));
				branch_id = CNumeric(crs.getString("branch_id"));
			}
			output.put("branchcount", branchcount);
			crs.close();
			if (Integer.parseInt(branchcount) != 1) {
				PopulateBrand();
				Populateregion();
				PopulateBranches();
				PopulateZone();
			}
			if (!brand_ids.equals("")) {
				Populateregion();
				PopulateZone();
			}
			if (!region_ids.equals("") || !zone_ids.equals("")) {
				PopulateBranches();
			}
			ServiceVehDetails();
			if (!(emp_id).equals("0")) {
				// Start filter
				if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("0") || !zone_ids.equals("")) {
					if (!brand_ids.equals("")) {
						StrSearch += " AND branch_brand_id = " + brand_ids + "";
					}
					if (!region_ids.equals("")) {
						StrSearch += " AND branch_region_id = " + region_ids + "";
					}
					if (!branch_ids.equals("0")) {
						StrSearch = StrSearch + " AND branch_id in (" + branch_ids + ")";
					}
					if (!zone_ids.equals("0")) {
						StrSearch = StrSearch + " AND branch_zone_id in (" + zone_ids + ")";
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axela-App ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (AppRun().equals("0")) {
			SOP("output ======== " + output);
		}
		return output;
	}
	public void ServiceVehDetails() {
		String new_value = "", old_value = "";
		try {
			CachedRowSet crs = null;

			StrSql = "SELECT"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 8) >= SUBSTR(" + startdate + ", 1, 8) AND SUBSTR(veh_entry_date, 1, 8) <= SUBSTR(" + enddate
					+ ", 1, 8) , 1, 0)),0) AS vehcountquarter,"
					+ " "
					+ " COALESCE(service_jc.jccountquarter,0) AS jccountquarter,"

					+ " COALESCE(service_jc.partsquater,0) AS partsamtquarter,"
					+ " "
					+ " COALESCE(service_jc.labouramtquarter,0) AS labouramtquarter,"
					+ " "
					+ " COALESCE(tblticket.ticketcountquarter,0) AS ticketcountquarter,"
					+ " "
					+ " COALESCE(service_booking.servicebookingquarter,0) AS servicecountquarter"
					+ " "
					+ " FROM " + compdb(comp_id) + "axela_service_veh";
			if (!brand_ids.equals("") || !region_ids.equals("") || !branch_ids.equals("") || !zone_ids.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id";
			}

			StrSql += " ,("
					+ " SELECT"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) >= SUBSTR("
					+ startdate
					+ ", 1, 8) AND SUBSTR(jc_bill_cash_date, 1, 8) <= SUBSTR("
					+ enddate
					+ ", 1, 8), 1, 0)) AS jccountquarter,"
					+ " "

					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) >= SUBSTR("
					+ startdate
					+ ", 1, 8) AND SUBSTR(jc_bill_cash_date, 1, 8) <=SUBSTR("
					+ enddate
					+ ", 1, 8),(jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0)) "
					+ " AS partsquater,"

					+ " "
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) >= SUBSTR("
					+ startdate
					+ ", 1, 8) AND SUBSTR(jc_bill_cash_date, 1, 8) <= SUBSTR("
					+ enddate
					+ ", 1, 8), "
					+ " jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) "
					+ " AS labouramtquarter"
					+ " "
					+ " FROM " + compdb(comp_id) + "axela_service_jc";
			if (!brand_ids.equals("") || !region_ids.equals("") || !branch_ids.equals("") || !zone_ids.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "jc_emp_id")
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_ids + ")";
			}
			if (!region_ids.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_ids + ")";
			}
			if (!zone_ids.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_ids + ")";
			}
			if (!branch_ids.equals("")) {
				StrSql += " AND branch_id IN (" + branch_ids + ")";
			}
			StrSql += " ) AS service_jc,"
					+ " ("
					+ " SELECT"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 8) >= SUBSTR(" + startdate + ", 1, 8) AND SUBSTR(ticket_entry_date, 1, 8) <=  SUBSTR(" + enddate + ", 1, 8) , 1, 0)) AS ticketcountquarter"
					+ " "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket";
			if (!brand_ids.equals("") || !region_ids.equals("") || !branch_ids.equals("") || !zone_ids.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticket_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "ticket_emp_id")
					+ BranchAccess.replace("branch_id", "ticket_branch_id");
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_ids + ")";
			}
			if (!region_ids.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_ids + ")";
			}
			if (!zone_ids.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_ids + ")";
			}
			if (!branch_ids.equals("")) {
				StrSql += " AND branch_id IN (" + branch_ids + ")";
			}
			StrSql += " ) AS tblticket,"
					+ " ("
					+ " SELECT"
					+ " SUM(IF (vehfollowup_vehaction_id = 1 AND (SUBSTR(vehfollowup_entry_time, 1, 8) >= SUBSTR(" + startdate + ", 1, 8) AND SUBSTR(vehfollowup_entry_time, 1, 8) <=  SUBSTR("
					+ enddate + ", 1, 8)), 1, 0)) AS servicebookingquarter"
					+ " "
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id";
			if (!brand_ids.equals("") || !region_ids.equals("") || !branch_ids.equals("") || !zone_ids.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}
			StrSql += " WHERE 1=1"
					+ ExeAccess.replace("emp_id", "veh_emp_id")
					+ BranchAccess.replace("branch_id", "veh_branch_id");
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_ids + ")";
			}
			if (!region_ids.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_ids + ")";
			}
			if (!zone_ids.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_ids + ")";
			}
			if (!branch_ids.equals("")) {
				StrSql += " AND branch_id IN (" + branch_ids + ")";
			}
			StrSql += " ) AS service_booking"
					+ " where 1=1"
					+ ExeAccess.replace("emp_id", "veh_emp_id")
					+ BranchAccess.replace("branch_id", "veh_branch_id");
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_ids + ")";
			}
			if (!region_ids.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_ids + ")";
			}
			if (!zone_ids.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_ids + ")";
			}
			if (!branch_ids.equals("")) {
				StrSql += " AND branch_id IN (" + branch_ids + ")";
			}
			StrSql += StrSearch;
			// SOP("StrSql===========servicehome==========" + StrSql);

			crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					veh_countquarter = crs.getString("vehcountquarter");
					map.put("veh_countmonth", veh_countquarter);
					jc_countquarter = crs.getString("jccountquarter");
					map.put("jc_countmonth", jc_countquarter);
					ticket_countquarter = crs.getString("ticketcountquarter");
					map.put("ticket_countmonth", ticket_countquarter);
					service_booking_countquarter = crs.getString("servicecountquarter");
					map.put("service_booking_countmonth", service_booking_countquarter);
					parts_amtquarter = (int) crs.getDouble("partsamtquarter") + "";
					map.put("parts_amtmonth", parts_amtquarter);
					labour_amtquarter = (int) crs.getDouble("labouramtquarter") + "";
					map.put("labour_amtmonth", labour_amtquarter);
					list.add(gson.toJson(map));
					map = null;
				}
			} else {
				map.put("veh_countmonth", "0");
				map.put("jc_countmonth", "0");
				map.put("ticket_countmonth", "0");
				map.put("service_booking_countmonth", "0");
				map.put("parts_amtmonth", "0");
				map.put("labour_amtmonth", "0");
				list.add(gson.toJson(map));
			}
			output.put("servicedata", list);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axela-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public JSONObject PopulateZone() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT zone_id, zone_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_zone"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_zone_id = zone_id"
					+ " WHERE 1=1 "
					+ " AND branch_Active = 1"
					+ " AND branch_branchtype_id = 3";
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_ids + ") ";
			}
			StrSql += " GROUP BY zone_id "
					+ " ORDER BY zone_name ";
			SOP("PopulateZone query =====" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("zone_id", "0");
				map.put("zone_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					map.put("zone_id", crs.getString("zone_id"));
					map.put("zone_name", unescapehtml(crs.getString("zone_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("zone_id", "0");
				map.put("zone_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatezone", list);
			// output.put("enquiry_branch_id", enquiry_branch_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject Populateregion() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1=1 AND branch_active = 1  "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_ids.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_ids + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					map.put("region_id", crs.getString("region_id"));
					map.put("region_name", unescapehtml(crs.getString("region_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateregion", list);
			// output.put("enquiry_branch_id", enquiry_branch_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBrand() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query =====" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					map.put("brand_id", crs.getString("brand_id"));
					map.put("brand_name", unescapehtml(crs.getString("brand_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebrand", list);
			// output.put("enquiry_branch_id", enquiry_branch_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranches() {
		CachedRowSet crs = null;

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!zone_ids.equals("")) {
				StrSql += " AND branch_zone_id in (" + zone_ids + ") ";
			}
			if (!region_ids.equals("")) {
				StrSql += " AND branch_region_id in (" + region_ids + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {

					map.put("branch_id", crs.getString("branch_id"));
					map.put("branch_name", unescapehtml(crs.getString("branch_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebranch", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return output;
		}
		return output;
	}
}
