/*/// Ved Prakash (11th Sept 2013) */
package axela.ws.axelaautoapp;

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
public class WS_HomeData extends Connect {
	public String StrSql = "";
	public String emp_id = "0";
	public String emp_uuid = "0";
	public String emp_active = "";
	public String ExeAccess = "";
	public String emp_enquiry_access = "";
	public String emp_device_id = "";
	public String emp_device_fcmtoken = "";
	public String emp_device_os = "";
	public CachedRowSet crs = null;
	public String level1 = "0";
	public String level2 = "0";
	public String level3 = "0";
	public String level4 = "0";
	public String level5 = "0";
	public String comp_logo = "";
	public String followupchart_data = "";
	public String BranchAccess = "";
	public String monthenquires = "0";
	public String monthbooking = "0";
	public String monthretails = "0";
	public String monthcancellations = "0";
	public String todayenquires = "0";
	public String todaybooking = "0";
	public String todayretails = "0";
	public String totalenquires = "0";
	public String totalhotenquires = "0";
	public String totalbooking = "0";
	public String todayinvoices = "0";
	public String todaytestdrives = "0";
	public String todayhomevisits = "0";
	public String todayreceipts = "0";
	public String monthlyinvoices = "0";
	public String monthlyreceipts = "0";
	public String totaldue = "0";
	public String enquirytarget1 = "0", sotarget1 = "0";
	public int enquirytarget = 0;
	public int sotarget = 0;
	public String date = "";
	public String fromdate = "";
	public String todate = "";
	public String month = "", msg = "";
	public int TotalRecords = 0;
	public String emp_name = "";
	public String branch_name = "";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";
	public String branch_logo = "";
	public String branch_id = "0";
	public String region_id = "0";
	public String brand_id = "0";
	public String team_id = "0";
	public String StrSearch = "";
	public String team_emp_id = "0";
	public String executive_id = "0";
	JSONObject output = new JSONObject();
	Map<String, String> map = new HashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Executive_Univ_Check update = new Executive_Univ_Check();
	Gson gson = new Gson();
	public String branchcount = "";
	// private String monthretails;
	// private String todayretails;

	public JSONObject home(JSONObject input, @Context HttpServletRequest request) throws Exception {
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
		if (!(emp_id).equals("0")) {

			if (!input.isNull("brand_id")) {
				brand_id = CNumeric(PadQuotes((String) input.get("brand_id")));
			}
			if (!input.isNull("region_id")) {
				region_id = CNumeric(PadQuotes((String) input.get("region_id")));
			}
			if (!input.isNull("branch_id")) {
				branch_id = CNumeric(PadQuotes((String) input.get("branch_id")));
			}
			if (!input.isNull("team_id")) {
				team_id = CNumeric(PadQuotes((String) input.get("team_id")));
			}
			if (!input.isNull("executive_id")) {
				executive_id = CNumeric(PadQuotes((String) input.get("executive_id")));
			}
			if (!brand_id.equals("0")) {
				PopulateRegion(input, brand_id);
				PopulateBranch1(input, brand_id, region_id, comp_id);
			}
			if (!region_id.equals("0")) {
				PopulateBranch1(input, brand_id, region_id, comp_id);
			}
			if (!branch_id.equals("0")) {
				PopulateTeam(input, branch_id, comp_id);
			}
			if (!team_id.equals("0")) {
				PopulateExecutive(input, team_id, comp_id);
			}
			if (!input.isNull("emp_branch_id")) {
				emp_branch_id = CNumeric(PadQuotes((String) input.get("emp_branch_id")));
			}
			if (!input.isNull("emp_device_id")) {
				emp_device_id = PadQuotes((String) input.get("emp_device_id"));
			}
			if (!input.isNull("emp_device_fcmtoken")) {
				emp_device_fcmtoken = PadQuotes((String) input.get("emp_device_fcmtoken"));
			}
			if (!input.isNull("emp_device_os")) {
				emp_device_os = PadQuotes((String) input.get("emp_device_os"));
			}
			// Start filter
			if (!brand_id.equals("0") || !region_id.equals("0") || !branch_id.equals("0") || !team_id.equals("0") || !executive_id.equals("0")) {
				if (!brand_id.equals("0")) {
					StrSearch += " AND branch_brand_id = " + brand_id + "";
				}
				if (!region_id.equals("0")) {
					StrSearch += " AND branch_region_id = " + region_id + "";
				}
				if (!branch_id.equals("0")) {
					StrSearch = StrSearch + " AND branch_id in (" + branch_id + ")";
				}
				if (!team_id.equals("0")) {
					StrSearch += " AND teamtrans_team_id = " + team_id + "";
				}
				if (!executive_id.equals("0")) {
					StrSearch += " AND teamtrans_emp_id = " + executive_id + "";
				}
			}
			// End filter

			// Start Branch Count
			StrSql = "SELECT COUNT(branch_id) AS branchcount,"
					+ " IF(COUNT(branch_id) = 1, branch_id, 0) AS branch_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess;
			// SOP("StrSql======COUNT=======" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				branchcount = CNumeric(crs.getString("branchcount"));
				branch_id = CNumeric(crs.getString("branch_id"));
			}
			crs.close();
			if (Integer.parseInt(branchcount) == 1) {
				if (!branch_id.equals("0")) {
					PopulateTeam(input, branch_id, comp_id);
				}
				PopulateBrand(input, comp_id);
			} else if (Integer.parseInt(branchcount) > 1) {
				PopulateBrand(input, comp_id);
			}
			map.put("branchcount", branchcount);
			// End Branch Count

			// Start emp_active check
			emp_active = CNumeric(ExecuteQuery("SELECT emp_active"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + emp_id));
			map.put("emp_active", emp_active);
			// End emp_active check
			emp_enquiry_access = CNumeric(ReturnPerm(comp_id, "emp_enquiry_access", request));
			map.put("emp_enquiry_access", emp_enquiry_access);
			map.put("appversion", CNumeric(ExecuteQuery("SELECT config_app_ver FROM " + compdb(comp_id) + "axela_config")));
			// End check emp_active

			// Start check emp_device_id
			if (!emp_id.equals("1")) {
				if (!emp_device_id.equals("")) {
					StrSql = "SELECT emp_id"
							+ " FROM axela_uni_emp" +
							" WHERE emp_id = " + emp_id + ""
							+ " AND emp_device_id = " + "'" +
							emp_device_id + "'";
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						map.put("emp_device_id", "yes");
					} else {
						map.put("emp_device_id", "no");
					}
					crs.close();
				}
			} else {
				map.put("emp_device_id", "yes");
			}
			// End check emp_device_id

			// Start FCM
			if (!emp_device_fcmtoken.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET emp_device_fcmtoken = '" + emp_device_fcmtoken + "',"
						+ " emp_device_os = '" + emp_device_os + "'"
						+ " WHERE emp_id =" + emp_id;
				// SOP("StrSql==========" + StrSql);
				updateQuery(StrSql);
				update.UpdateUniversalEmp(emp_id, comp_id);
			}

			emp_device_fcmtoken = PadQuotes(ExecuteQuery("SELECT emp_device_fcmtoken"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + emp_id));
			emp_device_fcmtoken = map.put("emp_device_fcmtoken", emp_device_fcmtoken);
			// End FCM

			date = ToShortDate(kknow()).substring(4, 6);
			month = TextMonth(Integer.parseInt(date) - 1);
			String StrSql2 = "SELECT emp_name, emp_active, COALESCE(branch_name,'') AS branch_name, COALESCE(branch_logo,'') AS branch_logo, emp_branch_id, comp_name, comp_logo"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id, "
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE emp_id = " + emp_id;
			crs = processQuery(StrSql2, 0);
			if (emp_active.equals("1")) {
				try {
					// / Enquiry Counts
					StrSql = "SELECT COALESCE(SUM(IF(SUBSTR(enquiry_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6),1,0)),0) AS monthenquires,"
							+ " COALESCE(SUM(IF(SUBSTR(enquiry_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8),1,0)),0) AS todayenquires,"
							+ " COALESCE(SUM(IF(enquiry_status_id=1,1,0)),0) AS totalenquires,"
							+ " COALESCE(SUM(IF(enquiry_status_id=1 AND enquiry_priorityenquiry_id=1,1,0)),0) AS totalhotenquires"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry";

					if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
					}
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE 1=1";
					StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id")
							+ ExeAccess.replace("emp_id", "enquiry_emp_id");
					StrSql += StrSearch;
					// }
					// SOP("StrSql----------------me-----------" + StrSql);
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							monthenquires = map.put("monthenquires", crs.getString("monthenquires"));
							todayenquires = map.put("todayenquires", crs.getString("todayenquires"));
							totalenquires = map.put("totalenquires", crs.getString("totalenquires"));
							totalhotenquires = map.put("totalhotenquires", crs.getString("totalhotenquires"));
						}
					}
					else {
						monthenquires = map.put("monthenquires", "0");
						todayenquires = map.put("todayenquires", "0");
						totalenquires = map.put("totalenquires", "0");
						totalhotenquires = map.put("totalhotenquires", "0");
					}
					crs.close();

					// / Sales Counts
					StrSql = "SELECT COALESCE(SUM(IF(so_active = '1' AND SUBSTR(so_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6) AND so_active!='0',1,0)),0) AS monthbooking,"
							+ " COALESCE(SUM(IF(so_active = '1' AND SUBSTR(so_retail_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6),1,0)),0) AS monthretails,"
							+ " COALESCE(SUM(IF(so_active = '0' AND so_delivered_date = '' AND so_retail_date = '' AND SUBSTR(so_cancel_date,1,6) = SUBSTR(" + ToLongDate(kknow())
							+ ",1,6),1,0)),0) AS monthcancellations,"
							+ " COALESCE(SUM(IF(so_active = '1' AND SUBSTR(so_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8) AND so_active!='0',1,0)),0) AS todaybooking,"
							+ " COALESCE(SUM(IF(so_active = '1' AND SUBSTR(so_retail_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8),1,0)),0) AS todayretails,"
							+ " COALESCE(SUM(IF(so_active = '1' AND so_delivered_date='' AND so_cancel_date='' AND so_active!='0',1,0)),0) AS totalbooking"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id";
					if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
					}
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE 1=1"
							// + " AND so_active = 1";
							+ " AND emp_sales = '1'";
					StrSql += BranchAccess.replace("branch_id", "so_branch_id")
							+ ExeAccess.replace("emp_id", "so_emp_id");
					StrSql += StrSearch.replace("enquiry_emp_id", "so_emp_id");
					// SOP("StrSql----------month---------" + StrSql);
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							monthbooking = map.put("monthbooking", crs.getString("monthbooking"));
							monthretails = map.put("monthretails", crs.getString("monthretails"));
							monthcancellations = map.put("monthcancellations", crs.getString("monthcancellations"));
							todaybooking = map.put("todaybooking", crs.getString("todaybooking"));
							todayretails = map.put("todayretails", crs.getString("todayretails"));
							totalbooking = map.put("totalbooking", crs.getString("totalbooking"));
						}

					}
					else {
						monthbooking = map.put("monthbooking", "0");
						monthretails = map.put("monthretails", "0");
						monthcancellations = map.put("monthcancellations", "0");
						todaybooking = map.put("todaybooking", "0");
						todayretails = map.put("todayretails", "0");
						totalbooking = map.put("totalbooking", "0");
					}
					crs.close();
					todate = ToLongDate(kknow());
					int month = Integer.parseInt(SplitMonth(todate));
					int year = Integer.parseInt(SplitYear(todate));
					int days = DaysInMonth(year, month);
					int day = Integer.parseInt(SplitDate(todate));
					todate = DateToShortDate(kknow());
					fromdate = "01" + todate.substring(2, 10);
					todate = days + todate.substring(2, 10);
					fromdate = ConvertShortDateToStr(fromdate);
					todate = ConvertShortDateToStr(todate);
					StrSql = "SELECT COALESCE (SUM(target_enquiry_count), 0 ) AS enquirytarget,"
							+ " COALESCE (SUM(target_so_count), 0 ) AS sotarget"
							+ " FROM " + compdb(comp_id) + "axela_sales_target"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
							+ " WHERE emp_sales=1";
					StrSql += StrSearch + ExeAccess + BranchAccess;
					StrSql += " AND substring(target_startdate, 1, 6) >=  substr('" + fromdate + "', 1, 6)";
					SOP("StrSql----target-------" + StrSql);
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							enquirytarget = enquirytarget + crs.getInt("enquirytarget");
							sotarget = sotarget + crs.getInt("sotarget");
						}
					} else {
						map.put("enquirytarget", "0");
						map.put("sotarget", "0");
					}
					crs.close();
					// today enquiry count
					Integer i = new Integer(enquirytarget);
					Double enquirytargetint = i.doubleValue();
					Double todayenquirytarget1 = (enquirytargetint * day) / days;
					String todayenquirytarget = Integer.parseInt(Math.round(todayenquirytarget1) + "") + "";
					// today so count
					Integer j = new Integer(sotarget);
					Double sotargetint = j.doubleValue();
					Double todaysotarget1 = (sotargetint * day) / days;
					String todaysotarget = Integer.parseInt(Math.round(todaysotarget1) + "") + "";
					enquirytarget1 = Integer.toString(enquirytarget);
					sotarget1 = Integer.toString(sotarget);
					map.put("enquirytarget", enquirytarget1);
					map.put("sotarget", sotarget1);
					map.put("todayenquirytarget", todayenquirytarget);
					map.put("todaysotarget", todaysotarget);

					// / Start Today Test Drives
					StrSql = "SELECT COALESCE ( SUM( IF ( SUBSTR(testdrive_time, 1, 8) = SUBSTR(" + ToLongDate(kknow()) + ", 1, 8), 1, 0 ) ), 0 ) AS todaytestdrives"
							+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry" + " ON enquiry_id = testdrive_enquiry_id ";
					// if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
					// }
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE 1=1"
							+ " AND testdrive_fb_taken = 1";
					if (!StrSearch.equals("")) {
						StrSql += StrSearch;
					}
					StrSql += ExeAccess.replace("emp_id", "testdrive_emp_id")
							+ BranchAccess;
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							todaytestdrives = map.put("todaytestdrives", crs.getString("todaytestdrives"));
						}
					}
					crs.close();
					// End Today Test Drives

					// Start Today Home Visit
					StrSql = "SELECT COALESCE ( SUM( IF ( SUBSTR(followup_followup_time, 1, 8) = SUBSTR(" + ToLongDate(kknow()) + ", 1, 8), 1, 0 ) ), 0 ) AS todayhomevisits"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry" + " ON enquiry_id = followup_enquiry_id ";
					// if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
					// }
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE 1=1"
							+ " AND followup_feedbacktype_id = 9";
					if (!StrSearch.equals("")) {
						StrSql += StrSearch;
					}
					StrSql += ExeAccess.replace("emp_id", "followup_emp_id")
							+ BranchAccess;
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							todayhomevisits = map.put("todayhomevisits", crs.getString("todayhomevisits"));
						}
					}
					crs.close();
					// End Home visit

					// Start Total Incentives.
					StrSql = "SELECT COALESCE(incentive_total, '0') AS incentive_total"
							+ " FROM " + compdb(comp_id) + "axela_sales_incentive"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = incentive_emp_id"
							+ " WHERE 1=1"
							+ " AND incentive_emp_id = " + emp_id
							+ " AND SUBSTR(incentive_startdate, 1, 6) = SUBSTR(" + ToLongDate(kknow()) + ", 1, 6)"
							+ " AND emp_active =  1";
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("incentive_total", IndDecimalFormat(crs.getString("incentive_total")));
						}
					} else {
						map.put("incentive_total", "0.00");
					}
					crs.close();
					// End Total Incentives.

					// Start Financial calculation
					StrSql = "SELECT"
							+ " COALESCE(SUM( CASE WHEN SUBSTR(voucher_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)"
							+ " AND voucher_vouchertype_id = 6 THEN voucher_amount END),0) AS todayinvoices,"
							+ " COALESCE(SUM( CASE WHEN SUBSTR(voucher_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)"
							+ " AND voucher_vouchertype_id = 9 THEN voucher_amount END),0) AS todayreceipts,"
							+ " COALESCE(SUM( CASE WHEN SUBSTR(voucher_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)"
							+ " AND voucher_vouchertype_id = 6 THEN voucher_amount END),0) AS monthlyinvoices,"
							+ " COALESCE(SUM( CASE WHEN SUBSTR(voucher_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)"
							+ " AND voucher_vouchertype_id = 9 THEN voucher_amount END),0) AS monthlyreceipts"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id";
					// if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id";
					// }
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = voucher_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE voucher_vouchertype_id IN (6, 9)"
							+ " AND branch_active = 1"
							+ " AND voucher_active = 1"
							+ " AND SUBSTR(voucher_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
					if (!StrSearch.equals("")) {
						StrSql += StrSearch;
					}
					if (!ExeAccess.equals("")) {
						StrSql += ExeAccess.replace("emp_id", "voucher_emp_id");
					}
					// SOP("StrSql=========Financial calculation=========" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							map.put("todayinvoices", String.valueOf(Math.round((Double.parseDouble(crs.getString("todayinvoices"))))));
							map.put("todayreceipts", String.valueOf(Math.round((Double.parseDouble(crs.getString("todayreceipts"))))));
							map.put("monthlyinvoices", String.valueOf(Math.round((Double.parseDouble(crs.getString("monthlyinvoices"))))));
							map.put("monthlyreceipts", String.valueOf(Math.round((Double.parseDouble(crs.getString("monthlyreceipts"))))));
						}
					}
					crs.close();

					// total due
					StrSql = "SELECT @todayso := SUM(so_grandtotal),"
							+ " @todayreceipt := SUM(voucher_amount),"
							+ " COALESCE(@totaldue := @todayso - @todayreceipt,0) AS totaldue"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id";
					if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
						StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id";
					}
					if (!executive_id.equals("0") || !team_id.equals("0")) {
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = voucher_emp_id";
						StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
					}
					StrSql += " WHERE 1=1"
							+ " AND SUBSTR(voucher_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
					if (!StrSearch.equals("")) {
						StrSql += StrSearch;
					}
					if (!ExeAccess.equals("")) {
						StrSql += ExeAccess.replace("emp_id", "voucher_emp_id");
					}
					// SOP("StrSql==================" + StrSql);
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							todayhomevisits = map.put("totaldue", "0");
						}
					}
					crs.close();
					// End Financial calculation
					FollowupEscStatus();
					CRMFollowupEscStatus();
				} catch (Exception e) {
					SOPError("Axelaauto-APP===" + this.getClass().getName());
					SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
			}

			// End home page data
			list.add(gson.toJson(map)); // Converting String to Json
			output.put("homedata", list);

		}
		SOP("output---------------" + output);
		return output;
	}
	public void FollowupEscStatus() {
		try {
			StrSql = " SELECT gr.group_id AS group_id,"
					+ " COALESCE(triggercount,0) AS triggercount "
					+ " FROM ( " + " SELECT 1 AS group_id "
					+ " UNION " + " SELECT 2 AS group_id "
					+ " UNION " + " SELECT 3 AS group_id "
					+ " UNION " + " SELECT 4 AS group_id "
					+ " UNION " + " SELECT 5 AS group_id " + " )" + " AS gr "
					+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS triggercount, followup_trigger"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id";

			if (!branch_id.equals("0") || !region_id.equals("0") || !brand_id.equals("0")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}

			if (!executive_id.equals("0") || !team_id.equals("0")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id";
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
			}
			StrSql += " WHERE followup_desc = '' "
					+ " AND followup_trigger > 0"
					+ " AND enquiry_status_id = 1"
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id");
			if (!StrSearch.equals("")) {
				StrSql += StrSearch;
			}
			StrSql += " GROUP BY followup_trigger) AS tr ON tr.followup_trigger = gr.group_id"
					+ " GROUP BY group_id"
					+ " ORDER BY group_id";
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			followupchart_data = "[";
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					if (crs1.getString("group_id").equals("1")) {
						level1 = crs1.getString("triggercount");
						map.put("level1", level1);
					}

					if (crs1.getString("group_id").equals("2")) {
						level2 = crs1.getString("triggercount");
						map.put("level2", level2);
					}
					if (crs1.getString("group_id").equals("3")) {
						level3 = crs1.getString("triggercount");
						map.put("level3", level3);
					}
					if (crs1.getString("group_id").equals("4")) {
						level4 = crs1.getString("triggercount");
						map.put("level4", level4);
					}
					if (crs1.getString("group_id").equals("5")) {
						level5 = crs1.getString("triggercount");
						map.put("level5", level5);
					}
					if (level1.equals("0")) {
						map.put("level1", "0");
					}
					if (level2.equals("0")) {
						map.put("level2", "0");
					}
					if (level3.equals("0")) {
						map.put("level3", "0");
					}
					if (level4.equals("0")) {
						map.put("level4", "0");
					}
					if (level5.equals("0")) {
						map.put("level5", "0");
					}
				}
			}
			else {
				map.put("level1", "0");
				map.put("level2", "0");
				map.put("level3", "0");
				map.put("level4", "0");
				map.put("level5", "0");
			}
			crs1.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP===" + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CRMFollowupEscStatus() {
		try {
			StrSql = " SELECT gr.group_id AS group_id, COALESCE(triggercount, 0) AS triggercount "
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
			SOP("StrSql=CRMFollowupEscStatus=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getString("group_id").equals("1")) {
						level1 = crs.getString("triggercount");
						map.put("crmlevel1", level1);
					}

					if (crs.getString("group_id").equals("2")) {
						level2 = crs.getString("triggercount");
						map.put("crmlevel2", level2);
					}
					if (crs.getString("group_id").equals("3")) {
						level3 = crs.getString("triggercount");
						map.put("crmlevel3", level3);
					}
					if (crs.getString("group_id").equals("4")) {
						level4 = crs.getString("triggercount");
						map.put("crmlevel4", level4);
					}
					if (crs.getString("group_id").equals("5")) {
						level5 = crs.getString("triggercount");
						map.put("crmlevel5", level5);
					}
				}
			} else {
				map.put("crmlevel1", "0");
				map.put("crmlevel2", "0");
				map.put("crmlevel3", "0");
				map.put("crmlevel4", "0");
				map.put("crmlevel5", "0");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public JSONObject PopulateBrand(JSONObject input, String comp_id) {
		CachedRowSet crs = null;
		String enquiry_item_id = "0";
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// + " ORDER BY brand_name"
			// if (!brand_id.equals("0")) {
			// StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			// }
			// StrSql += " GROUP BY region_id"
			// + " ORDER BY region_name";
			// SOP("StrSql=========app=====" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_item_id = crs.getString("brand_id");
					}
					map.put("brand_id", crs.getString("brand_id"));
					map.put("brand_name", unescapehtml(crs.getString("brand_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("brand_id", "0");
				map.put("brand_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebrand", list);
			// output.put("brand_id", brand_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateRegion(JSONObject input, String brand_id) {
		CachedRowSet crs = null;
		String enquiry_region_id = "0";
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id = " + brand_id;
			}
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			// SOP("StrSql=========app=====" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("region_id", "0");
				map.put("region_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_region_id = "0";
					}
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
			// output.put("enquiry_region_id", enquiry_region_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranch1(JSONObject input, String brand_id, String region_id, String comp_id) {
		CachedRowSet crs = null;
		String enquiry_branch_id = "0";
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active='1'"
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess;

			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id = " + brand_id;
			}
			if (!region_id.equals("0")) {
				StrSql += " AND branch_region_id = " + region_id;
			}
			StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("branch_id", "0");
				map.put("branch_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					count++;
					if (count == 1) {
						enquiry_branch_id = crs.getString("branch_id");
					}
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
			// output.put("enquiry_branch_id", enquiry_branch_id);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateTeam(JSONObject input, String branch_id, String comp_id) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE 1=1 ";
			if (!branch_id.equals("0")) {
				StrSql += " AND team_branch_id = " + branch_id;
			}
			StrSql += BranchAccess.replace("branch_id", "team_branch_id")
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";

			// if (!brand_id.equals("0")) {
			// StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			// }
			// if (!region_id.equals("0")) {
			// StrSql += " AND branch_region_id IN (" + region_id + ") ";
			// }
			// StrSql += " ORDER BY branch_name";
			// SOP("StrSql==team=" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("team_id", "0");
				map.put("team_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					// count++;
					// if (count == 1) {
					// enquiry_branch_id = crs.getString("branch_id");
					// }
					map.put("team_id", crs.getString("team_id"));
					map.put("team_name", unescapehtml(crs.getString("team_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("team_id", "0");
				map.put("team_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateteam", list);
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

	public JSONObject PopulateExecutive(JSONObject input, String team_id, String comp_id) {
		CachedRowSet crs = null;
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = 1 "
					+ " AND emp_sales = 1"
					+ ExeAccess;
			if (!branch_id.equals("0")) {
				StrSql = StrSql + " and emp_branch_id = " + branch_id;
			}

			if (!team_id.equals("0")) {
				StrSql = StrSql + " and teamtrans_team_id = " + team_id;
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			SOP("StrSql======PopulateExecutive===========" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
				int count = 0;
				while (crs.next()) {
					// count++;
					// if (count == 1) {
					// enquiry_branch_id = crs.getString("branch_id");
					// }
					map.put("executive_id", crs.getString("emp_id"));
					map.put("executive_name", unescapehtml(crs.getString("emp_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("executive_id", "0");
				map.put("executive_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexecutive", list);
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
}
