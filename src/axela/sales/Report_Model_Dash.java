package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Model_Dash extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, exe_ids, model_ids, soe_ids, fueltype_ids;
	public String brand_id = "", region_id = "", exe_id = "", model_id = "", soe_id = "", fueltype_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_model_lead = "0";
	public String dr_totalby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String modelSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				SOP("header...." + header);
				// SOP("===="+getPercentage(100, 10));
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				// SOP("branch_id---------" + branch_id);
				if (!header.equals("no")) {
					GetValues(request, response);
					CheckForm();

					if (go.equals("Go")) {

						StrSearch = BranchAccess.replace("branch_id", "model_branch_id"); // + " " + ExeAccess;
						
							StrSearch += ExeAccess;
						
						// if (!exe_id.equals("")) {
						// StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
						// }
						if (!brand_id.equals("") && branch_id.equals("")) {
							branch_id = ReturnBranchids(brand_id, comp_id);
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id in (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							// StrSearch = StrSearch + " and  (emp_branch_id in (" + branch_id + ") or emp_id= 1 or emp_id in (select empbr.emp_id from "
							// + " " + compdb(comp_id) + "axela_emp_branch empbr where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and "
							// + " empbr.emp_branch_id in (" + branch_id + ")))";
							StrSearch = StrSearch + " and  branch_id in (" + branch_id + ")";

							// StrBranch = " and branch_id=" + dr_branch_id;
						}
						// if (!fueltype_id.equals(""))
						// {
						// StrSearch = StrSearch + " and  fueltype_id in (" + fueltype_id + ")";
						// SOP("fueltype_id====" + fueltype_id);
						// }
						// SOP("model_id===" + model_id);
						if (!model_id.equals("")) {
							StrModel = " and model_id in (" + model_id + ")";
						}
						if (!soe_id.equals(""))
						{
							StrSoe = " and enquiry_soe_id in (" + soe_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						}
						// SOP("StrSearch----------" + StrSearch);
					}
				} else {
					// dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
					starttime = PadQuotes(request.getParameter("txt_starttime"));
					endtime = PadQuotes(request.getParameter("txt_endtime"));
					// SOP("starttime--------2--------" + starttime);
					// SOP("endtime--------2--------" + endtime);

					targetstarttime = starttime;
					targetendtime = endtime;
					chk_model_lead = PadQuotes(request.getParameter("chk_tmodellead"));
					StrHTML = ListMonitorBoard(dr_branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
				}

				// SOP("StrSearch_----------" + StrSearch);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
			// SOP("starttime--------1--------" + starttime);
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
			// SOP("endtime--------1--------" + endtime);
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		// exe_id = RetrunSelectArrVal(request, "dr_executive");
		// exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		// SOP("region_ids-----------" + region_ids);
		// SOP("region_id-----------" + region_id);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		// SOP("branch_id-----------" + branch_id);
		// SOP("branch_ids-----------" + branch_ids);
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		fueltype_id = RetrunSelectArrVal(request, "fueltype_id");
		fueltype_ids = request.getParameterValues("fueltype_id");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");

		// chk_model_lead = PadQuotes(request.getParameter("chk_model_lead"));
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		// if (chk_model_lead.equals("on")) {
		// chk_model_lead = "1";
		// } else {
		// chk_model_lead = "0";
		// }
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListMonitorBoard(String dr_branch_id, String starttime, String endtime, String targetstarttime, String targetendtime, String dr_totalby, String comp_id) {
		int total_retailtarget = 0;
		int total_enquirytarget = 0;
		int total_enquiryopen = 0;
		int total_enquiryfresh = 0;
		int total_enquirylost = 0;
		int total_soretail = 0;
		int total_sodelivered = 0;
		String total_soretail_perc = "0";
		int total_pendingenquiry = 0;
		int total_pendingbooking = 0;
		String total_pendingbooking_perc = "0";
		int total_pendingdelivery = 0;
		int total_cancellation = 0;
		int total_testdrives = 0;
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
		String total_exchange_perc = "0";
		int total_evaluation = 0;
		String total_evaluation_perc = "0";

		// ====model count variables===//
		int model_retailtarget = 0;
		int model_enquirytarget = 0;
		int model_enquiryopen = 0;
		int model_enquiryfresh = 0;
		int model_enquirylost = 0;
		int model_soretail = 0;
		int model_sodelivered = 0;
		String model_soretail_perc = "0";
		int model_pendingenquiry = 0;
		int model_pendingbooking = 0;
		String model_pendingbooking_perc = "0";
		int model_pendingdelivery = 0;
		int model_cancellation = 0;
		int model_testdrives = 0;
		String model_testdrives_perc = "0";
		int model_kpitestdrives = 0;
		int model_homevisit = 0;
		String model_homevisit_perc = "0";
		int model_kpihomevisit = 0;
		int model_mga_sales = 0;
		int model_maruti_insurance = 0;
		int model_extwarranty = 0;
		int model_fincases = 0;
		int model_exchange = 0;
		String model_exchange_perc = "0";
		int model_evaluation = 0;
		String model_evaluation_perc = "0";
		String check_model_id = "", check_modelttl_id = "";
		// ====branch count variables===//
		int branch_retailtarget = 0;
		int branch_enquirytarget = 0;
		int branch_enquiryopen = 0;
		int branch_enquiryfresh = 0;
		int branch_enquirylost = 0;
		int branch_soretail = 0;
		int branch_sodelivered = 0;
		String branch_soretail_perc = "0";
		int branch_pendingenquiry = 0;
		int branch_pendingbooking = 0;
		String branch_pendingbooking_perc = "0";
		int branch_pendingdelivery = 0;
		int branch_cancellation = 0;
		int branch_testdrives = 0;
		String branch_testdrives_perc = "0";
		int branch_kpitestdrives = 0;
		int branch_homevisit = 0;
		String branch_homevisit_perc = "0";
		int branch_kpihomevisit = 0;
		int branch_mga_sales = 0;
		int branch_maruti_insurance = 0;
		int branch_extwarranty = 0;
		int branch_fincases = 0;
		int branch_exchange = 0;
		String branch_exchange_perc = "0";
		int branch_evaluation = 0;
		String branch_evaluation_perc = "0";
		String check_branch_id = "", check_branchttl_id = "";
		// ====principal count variables===//
		int principal_retailtarget = 0;
		int principal_enquirytarget = 0;
		int principal_enquiryopen = 0;
		int principal_enquiryfresh = 0;
		int principal_enquirylost = 0;
		int principal_soretail = 0;
		int principal_sodelivered = 0;
		String principal_soretail_perc = "0";
		int principal_pendingenquiry = 0;
		int principal_pendingbooking = 0;
		String principal_pendingbooking_perc = "0";
		int principal_pendingdelivery = 0;
		int principal_cancellation = 0;
		int principal_testdrives = 0;
		String principal_testdrives_perc = "0";
		int principal_kpitestdrives = 0;
		int principal_homevisit = 0;
		String principal_homevisit_perc = "0";
		int principal_kpihomevisit = 0;
		int principal_mga_sales = 0;
		int principal_maruti_insurance = 0;
		int principal_extwarranty = 0;
		int principal_fincases = 0;
		int principal_exchange = 0;
		String principal_exchange_perc = "0";
		int principal_evaluation = 0;
		String principal_evaluation_perc = "0";

		// -----------Region count Variables----------
		int region_retailtarget = 0;
		int region_enquirytarget = 0;
		int region_enquiryopen = 0;
		int region_enquiryfresh = 0;
		int region_enquirylost = 0;
		int region_soretail = 0;
		int region_sodelivered = 0;
		String region_soretail_perc = "0";
		int region_pendingenquiry = 0;
		int region_pendingbooking = 0;
		String region_pendingbooking_perc = "0";
		int region_pendingdelivery = 0;
		int region_cancellation = 0;
		int region_testdrives = 0;
		String region_testdrives_perc = "0";
		int region_kpitestdrives = 0;
		int region_homevisit = 0;
		String region_homevisit_perc = "0";
		int region_kpihomevisit = 0;
		int region_mga_sales = 0;
		int region_maruti_insurance = 0;
		int region_extwarranty = 0;
		int region_fincases = 0;
		int region_exchange = 0;
		String region_exchange_perc = "0";
		int region_evaluation = 0;
		String region_evaluation_perc = "0";
		String check_brand_id = "", check_principalttl_id = "";
		String check_region_id = "", check_regionttl_id = "";

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();

		StrSql = "SELECT model_id, brand_id, region_id, branch_id, brand_name, region_name, branch_name, model_name,"
				// retail target

				+ " CAST( COALESCE ( ( SELECT SUM(modeltarget_so_count) "
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id "
				+ " WHERE 1=1 "
				+ " AND modeltarget_model_id = model_id"
				+ " AND SUBSTRING(target_startdate,1,6) >= SUBSTR('" + targetstarttime + "',1,6) "
				+ " AND SUBSTRING(target_enddate,1,6) <= SUBSTR('" + targetendtime + "',1,6) "
				// + " " + StrModel.replace("model_id", "modeltarget_model_id")
				+ "  ), '0' ) AS CHAR ) AS retailtarget, "

				// enquiry target

				+ " CAST( COALESCE ( ( SELECT CAST( SUM(modeltarget_enquiry_count) AS CHAR ) "
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id "
				+ " WHERE 1=1 "
				+ " AND modeltarget_model_id = model_id "
				+ " AND SUBSTRING(target_startdate,1,6) >= SUBSTR('" + targetstarttime + "',1,6) "
				+ " AND SUBSTRING(target_enddate,1,6) <= SUBSTR('" + targetendtime + "',1,6) "
				// + StrModel.replace("model_id", "modeltarget_model_id")
				+ " ), '0' )"
				+ " AS CHAR )   AS enquirytarget,"

				// enquiry open

				+ " @OPEN := CAST( ( SELECT COALESCE (count(enquiry_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " WHERE 1 = 1 "
				+ " AND enquiry_model_id = model_id "
				+ " AND enquiry_status_id = 1 "
				+ " AND SUBSTR(enquiry_date,1,8) < SUBSTR('" + starttime + "',1,8)"
				+ " " + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS enquiryopen,"

				// enquiryfresh

				+ " @fresh := CAST( ( SELECT COALESCE (COUNT(enquiry_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " WHERE 1 = 1 "
				+ " AND enquiry_model_id = model_id "
				+ " AND SUBSTR(enquiry_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(enquiry_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
				+ "" + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS enquiryfresh,"

				// enquiry lost

				+ " @lost := CAST( ( SELECT COALESCE (COUNT(enquiry_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " WHERE 1 = 1 "
				+ " AND enquiry_model_id = model_id "
				+ " AND SUBSTR(enquiry_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(enquiry_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " AND ( enquiry_status_id = 3 OR enquiry_status_id = 4 ) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS enquirylost,"

				// so retail

				+ " @retail := CAST( ( SELECT COALESCE (COUNT(DISTINCT so_id), 0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
		}

		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1"
				+ " AND soitem_item_id = item_id "
				+ " AND so_active = '1' "
				+ " AND SUBSTR(so_retail_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_retail_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " )AS CHAR ) AS soretail,"

				// pending enquiry

				+ " CAST( (@OPEN +@fresh -@lost -@retail) AS CHAR ) AS pendingenquiry,"

				// so delivered

				+ " @retail := CAST( ( SELECT COALESCE (COUNT(DISTINCT so_id), 0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN   " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " so_item_id = item_id " // should be commented
				+ " AND so_active = '1' "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS sodelivered,"

				// pending booking
				+ " @booking:=CAST((SELECT COALESCE(count(so_id),0) from " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '1' "
				+ " AND SUBSTR(so_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS pendingbooking,"

				// pending delivery

				+ " @delivery:=CAST((SELECT COALESCE(COUNT(so_id),0)   FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '1' "
				// + " AND so_delivered_date = '' "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS pendingdelivery,"

				// Cancellation

				+ " @cancellation:=CAST((SELECT COALESCE(COUNT(so_id),0)  from " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}

		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '0' "
				+ " AND SUBSTR(so_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS cancellation, "

				// test drives

				+ "	@testdrives := CAST( ( SELECT COALESCE (COUNT(testdrive_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
				+ " WHERE 1 = 1 "
				// + " AND enquiry_emp_id = emp_id "
				+ " AND testdrive_fb_taken = 1 "
				+ " AND SUBSTR(testdrive_time,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(testdrive_time,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ "" + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS 'testdrives',"

				// KPI Test drives

				+ " CAST( ( SELECT COALESCE ( COUNT(DISTINCT enquiry_id), 0 ) "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
				+ " WHERE 1 = 1 "
				// + " AND enquiry_emp_id = emp_id "
				+ " AND testdrive_fb_taken = 1 "
				+ " AND SUBSTR(testdrive_time,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(testdrive_time,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ "" + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS 'kpitestdrives',"

				// home visit

				+ "	@homevisit := CAST( ( SELECT COALESCE (count(followup_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id "
				+ " WHERE 1=1 "
				// + " AND enquiry_emp_id = emp_id "
				+ " AND followup_desc LIKE 'Home Visit Done%' "
				+ " AND SUBSTR(followup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(followup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS 'homevisit',"

				// KPI home visit

				+ " CAST( ( SELECT COALESCE ( COUNT(DISTINCT enquiry_id), 0 ) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id "
				+ " WHERE 1 = 1 "
				// + " AND enquiry_emp_id = emp_id "
				+ " AND followup_desc LIKE 'Home Visit Done%' "
				+ " AND SUBSTR(followup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(followup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS 'kpihomevisit',"

				// MGA Amount

				+ " CAST((SELECT COALESCE(SUM(so_mga_amount),0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active='1' "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR) as mga_amount,"

				// maruti insurance

				+ "CAST((SELECT COALESCE(COUNT(DISTINCT so_id),0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '1' "
				+ " AND so_insur_amount > 0 "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS 'maruti_insur',"

				// extended warranty

				+ " CAST((SELECT COALESCE(count(DISTINCT so_id),0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '1' "
				+ " AND so_ew_amount > 0 "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS 'extended_warranty', "

				// fin cases

				+ " CAST((SELECT COALESCE(COUNT(DISTINCT so_id),0) from " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND so_emp_id = emp_id "
				+ " AND so_active = '1' "
				+ " AND so_finance_amt > 0 "
				+ " AND so_fintype_id = 1 "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS 'fin_cases',"

				// exchange

				+ " CAST((SELECT COALESCE(COUNT(DISTINCT so_id),0) FROM " + compdb(comp_id) + "axela_sales_so";
		if (!StrSoe.equals("")) {
			StrSql += " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
				+ " AND soitem_rowcount != 0 "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id "
				+ " WHERE 1 = 1 "
				// + " AND	so_emp_id = emp_id "
				+ " AND so_active = '1' "
				+ " AND so_exchange_amount > 0 "
				+ " AND SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "item_model_id")
				+ " ) AS CHAR ) AS 'exchange',"

				// evaluation

				+ " CAST( ( SELECT COALESCE (count(enquiry_id), 0) "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " WHERE 1 = 1 "
				+ " AND enquiry_evaluation = 1 "
				+ " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8) "
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8) "
				+ " " + StrSoe
				// + StrModel.replace("model_id", "enquiry_model_id")
				+ " ) AS CHAR ) AS evaluation"

				// main table joins

				+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
				+ " INNER JOIN axela_brand ON brand_id = model_brand_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id ";
		if (!fueltype_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id "
					+ " AND  item_fueltype_id in (" + fueltype_id + ")";
		}
		StrSql += " WHERE 1 = 1 "
				+ StrModel
				+ StrSearch
				+ " AND model_active = 1 "
				+ " AND model_sales = 1 "
				+ " GROUP BY brand_id, region_id, branch_id, model_id "
				+ " ORDER BY brand_name, region_name, branch_name, model_name";
		SOP("strsql==model dash=" + StrSql);
		// CachedRowSet crs1 = processQuery(StrSql, 0);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Build model Headers

				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				if (dr_totalby.equals("1")) {
					StrHead.append("<th data-toggle=\"true\">Model</th>\n");
				}
				if (dr_totalby.equals("2")) {
					StrHead.append("<th data-toggle=\"true\">Branch</th>\n");
				}
				if (dr_totalby.equals("3")) {
					StrHead.append("<th data-toggle=\"true\">Brand</th>\n");
				}
				if (dr_totalby.equals("4")) {
					StrHead.append("<th data-toggle=\"true\">Region</th>\n");
				}
				StrHead.append("<th title=\"Retail Target\">RT</th>\n");
				StrHead.append("<th data-hide=\"phone\" title=\"Enquiry Target\">ET</th>\n");
				StrHead.append("<th data-hide=\"phone\">Open Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone\">Fresh Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Lost Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Pending Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Retail</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Retail %</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Delivered\">Del</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Booking</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Booking %</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Pending Delivery</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Cancellation\">Cal</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">TD</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">KPI TD</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">TD%</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Home Visit</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">KPI Home Visit</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Home Visit %</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Accessories\">Access</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Insurance\">Ins</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Extended Warranty\">EW</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" >Fin Cases</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Exchange\">Exe </th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Exchange\">Exe %</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Evaluation\">Eval </th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Evaluation\">Eval %</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				if (!header.equals("no")) {
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table   table-hover table-bordered\" data-filter=\"#filter\">\n");
				} else {
					Str.append("<div class=\"  table-bordered\">\n");
					// Str.append("<table border=1 data-filter=\"#filter\" style=\"border-collapse:collapse;border-color:#726a7a;padding:3px;\" width=\"100%\">\n");
					Str.append("<table class=\"table   table-hover table-bordered\" data-filter=\"#filter\">\n");
				}
				Str.append(StrHead.toString());
				// crs.last();
				int count = 0, modelcount = 0, branchcount = 0, principalcount = 0, regioncount = 0;
				String modelname = "", branchname = "", principalname = "", regionname = "";
				while (crs.next()) {
					count++;
					// ==================================================================================================================
					total_retailtarget = total_retailtarget + crs.getInt("retailtarget");
					total_enquirytarget = total_enquirytarget + crs.getInt("enquirytarget");
					total_enquiryopen = total_enquiryopen + crs.getInt("enquiryopen");
					total_enquiryfresh = total_enquiryfresh + crs.getInt("enquiryfresh");
					total_enquirylost = total_enquirylost + crs.getInt("enquirylost");
					total_soretail = total_soretail + crs.getInt("soretail");
					total_sodelivered = total_sodelivered + crs.getInt("sodelivered");
					total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
					total_pendingenquiry = total_pendingenquiry + crs.getInt("pendingenquiry");
					total_pendingbooking = total_pendingbooking + crs.getInt("pendingbooking");
					total_pendingbooking_perc = getPercentage((double) total_pendingbooking, (double) total_enquiryfresh);
					total_pendingdelivery = total_pendingdelivery + crs.getInt("pendingdelivery");
					total_cancellation = total_cancellation + crs.getInt("cancellation");
					total_testdrives = total_testdrives + crs.getInt("testdrives");
					total_kpitestdrives = total_kpitestdrives + crs.getInt("kpitestdrives");
					total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
					total_homevisit = total_homevisit + crs.getInt("homevisit");
					total_kpihomevisit = total_kpihomevisit + crs.getInt("kpihomevisit");
					total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
					total_mga_sales = total_mga_sales + crs.getInt("mga_amount");
					total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
					total_extwarranty = total_extwarranty + crs.getInt("extended_warranty");
					total_fincases = total_fincases + crs.getInt("fin_cases");
					total_exchange = total_exchange + crs.getInt("exchange");
					total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
					total_evaluation = total_evaluation + crs.getInt("evaluation");
					total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);

					// ==================================================================================================================

					if (check_branchttl_id.equals("")) {
						check_branchttl_id = crs.getString("branch_id");
						branchname = crs.getString("branch_name");
					}

					if (check_principalttl_id.equals("")) {
						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");
					}
					if (check_regionttl_id.equals("")) {
						check_regionttl_id = crs.getString("region_id");
						regionname = crs.getString("region_name");
					}
					// SOP("check_modelttl_id===" + check_modelttl_id);
					// SOP("crs.getString(model_id)==222=" + crs.getString("model_id"));

					if (!check_modelttl_id.equals(crs.getString("model_id")) && dr_totalby.equals("1")) {

						Str.append("<tr>");
						Str.append("<td>");
						if (dr_totalby.equals("1")) {
							Str.append("" + modelcount + "");
						} else {
							Str.append("&nbsp");
						}
						Str.append("</td>");
						Str.append("<td nowrap><b>");
						if (dr_totalby.equals("1")) {
							Str.append(modelname);
						} else {
							Str.append("Model Total:");
						}
						Str.append("</b></td>");
						// model_retailtarget
						Str.append("<td><b>");
						Str.append(model_retailtarget);
						Str.append("</b></td>");
						// model_enquirytarget
						Str.append("<td><b>");
						Str.append(model_enquirytarget);
						Str.append("</b></td>");
						// model_enquiryopen
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_enquiryopen);
						Str.append("</a></b></td>");
						SOP("model_enquiryopen==" + model_enquiryopen);
						// model_enquiryfresh
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_enquiryfresh);
						Str.append("</a></b></td>");
						// model_enquirylost
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_enquirylost);
						Str.append("</a></b></td>");
						// model_pendingenquiry
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_pendingenquiry);
						// SOP("model_pendingenquiry------------" + model_pendingenquiry);
						Str.append("</a></b></td>");
						// model_soretail
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_soretail);
						Str.append("</a></b></td>");
						// model_soretail_perc
						model_soretail_perc = getPercentage((double) model_soretail, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_soretail_perc).append("%");
						Str.append("</b></td>");
						// model_sodelivered
						Str.append("<td  ><b><a href=").append(SearchURL).append("?solesorder=sodelivered&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_sodelivered);
						Str.append("</a></b></td>");
						// model_pendingbooking
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_pendingbooking);
						Str.append("</a></b></td>");
						// model_pendingbooking_perc
						model_pendingbooking_perc = getPercentage((double) model_pendingbooking, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_pendingbooking_perc).append("%");
						Str.append("</b></td>");
						// model_pendingdelivery
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_pendingdelivery);
						Str.append("</a></b></td>");
						// model_cancellation
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_cancellation);
						Str.append("</a></b></td>");
						// model_testdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_testdrives);
						Str.append("</a></b></td>");
						// model_kpitestdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_kpitestdrives);
						Str.append("</a></b></td>");
						// model_testdrives_perc
						model_testdrives_perc = getPercentage((double) model_kpitestdrives, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_testdrives_perc).append("%");
						Str.append("</b></td>");
						// model_homevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_homevisit);
						Str.append("</a></b></td>");
						// model_kpihomevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_kpihomevisit);
						Str.append("</a></b></td>");
						// model_homevisit_perc
						model_homevisit_perc = getPercentage((double) model_kpihomevisit, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_homevisit_perc).append("%");
						Str.append("</b></td>");
						// model_mga_sales
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_mga_sales);
						Str.append("</a></b></td>");
						// model_maruti_insurance
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_maruti_insurance);
						Str.append("</a></b></td>");
						// model_extwarranty
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_extwarranty);
						Str.append("</a></b></td>");
						// model_fincases
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_fincases);
						Str.append("</a></b></td>");
						// model_exchange
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_exchange);
						Str.append("</a></b></td>");
						// model_exchange_perc
						model_exchange_perc = getPercentage((double) model_exchange, (double) model_pendingbooking);
						Str.append("<td><b>");
						Str.append(model_exchange_perc).append("%");
						Str.append("</b></td>");

						// model_evaluation
						Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
						Str.append(model_evaluation);
						Str.append("</a></b></td>");
						// model_evaluation_perc
						model_evaluation_perc = getPercentage((double) model_evaluation, (double) model_enquiryfresh);
						Str.append("<td><b>");
						Str.append(model_evaluation_perc).append("%");
						Str.append("</b></td>");
						Str.append("</tr>");

						check_modelttl_id = crs.getString("model_id");

						model_retailtarget = 0;
						model_enquirytarget = 0;
						model_enquiryopen = 0;
						model_enquiryfresh = 0;
						model_enquirylost = 0;
						model_soretail = 0;
						model_soretail_perc = "0";
						model_sodelivered = 0;
						model_pendingenquiry = 0;
						model_pendingbooking = 0;
						model_pendingbooking_perc = "0";
						model_pendingdelivery = 0;
						model_cancellation = 0;
						model_testdrives = 0;
						model_testdrives_perc = "0";
						model_kpitestdrives = 0;
						model_homevisit = 0;
						model_homevisit_perc = "0";
						model_kpihomevisit = 0;
						model_mga_sales = 0;
						model_maruti_insurance = 0;
						model_extwarranty = 0;
						model_fincases = 0;
						model_exchange = 0;
						model_exchange_perc = "0";
						model_evaluation = 0;
						model_evaluation_perc = "0";
					}
					// ==================================================================================================================
					model_retailtarget = model_retailtarget + crs.getInt("retailtarget");
					model_enquirytarget = model_enquirytarget + crs.getInt("enquirytarget");
					model_enquiryopen = model_enquiryopen + crs.getInt("enquiryopen");
					model_enquiryfresh = model_enquiryfresh + crs.getInt("enquiryfresh");
					model_enquirylost = model_enquirylost + crs.getInt("enquirylost");
					model_pendingenquiry = model_pendingenquiry + crs.getInt("pendingenquiry");
					model_soretail = model_soretail + crs.getInt("soretail");
					model_sodelivered = model_sodelivered + crs.getInt("sodelivered");
					model_pendingbooking = model_pendingbooking + crs.getInt("pendingbooking");
					model_pendingdelivery = model_pendingdelivery + crs.getInt("pendingdelivery");
					model_cancellation = model_cancellation + crs.getInt("cancellation");
					model_testdrives = model_testdrives + crs.getInt("testdrives");
					model_kpitestdrives = model_kpitestdrives + crs.getInt("kpitestdrives");
					model_homevisit = model_homevisit + crs.getInt("homevisit");
					model_kpihomevisit = model_kpihomevisit + crs.getInt("kpihomevisit");
					model_mga_sales = model_mga_sales + crs.getInt("mga_amount");
					model_maruti_insurance = model_maruti_insurance + crs.getInt("maruti_insur");
					model_extwarranty = model_extwarranty + crs.getInt("extended_warranty");
					model_fincases = model_fincases + crs.getInt("fin_cases");
					model_exchange = model_exchange + crs.getInt("exchange");
					model_evaluation = model_evaluation + crs.getInt("evaluation");

					if (check_branchttl_id.equals(crs.getString("branch_id")) && dr_totalby.equals("2")) {
						branch_retailtarget = branch_retailtarget + crs.getInt("retailtarget");
						branch_enquirytarget = branch_enquirytarget + crs.getInt("enquirytarget");
						branch_enquiryopen = branch_enquiryopen + crs.getInt("enquiryopen");
						branch_enquiryfresh = branch_enquiryfresh + crs.getInt("enquiryfresh");
						branch_enquirylost = branch_enquirylost + crs.getInt("enquirylost");
						branch_pendingenquiry = branch_pendingenquiry + crs.getInt("pendingenquiry");
						branch_soretail = branch_soretail + crs.getInt("soretail");
						branch_sodelivered = branch_sodelivered + crs.getInt("sodelivered");
						branch_pendingbooking = branch_pendingbooking + crs.getInt("pendingbooking");
						branch_pendingdelivery = branch_pendingdelivery + crs.getInt("pendingdelivery");
						branch_cancellation = branch_cancellation + crs.getInt("cancellation");
						branch_testdrives = branch_testdrives + crs.getInt("testdrives");
						branch_kpitestdrives = branch_kpitestdrives + crs.getInt("kpitestdrives");
						branch_homevisit = branch_homevisit + crs.getInt("homevisit");
						branch_kpihomevisit = branch_kpihomevisit + crs.getInt("kpihomevisit");
						branch_mga_sales = branch_mga_sales + crs.getInt("mga_amount");
						branch_maruti_insurance = branch_maruti_insurance + crs.getInt("maruti_insur");
						branch_extwarranty = branch_extwarranty + crs.getInt("extended_warranty");
						branch_fincases = branch_fincases + crs.getInt("fin_cases");
						branch_exchange = branch_exchange + crs.getInt("exchange");
						branch_evaluation = branch_evaluation + crs.getInt("evaluation");
					}

					if (check_principalttl_id.equals(crs.getString("brand_id")) && dr_totalby.equals("3")) {
						principal_retailtarget = principal_retailtarget + crs.getInt("retailtarget");
						principal_enquirytarget = principal_enquirytarget + crs.getInt("enquirytarget");
						principal_enquiryopen = principal_enquiryopen + crs.getInt("enquiryopen");
						principal_enquiryfresh = principal_enquiryfresh + crs.getInt("enquiryfresh");
						principal_enquirylost = principal_enquirylost + crs.getInt("enquirylost");
						principal_pendingenquiry = principal_pendingenquiry + crs.getInt("pendingenquiry");
						principal_soretail = principal_soretail + crs.getInt("soretail");
						principal_sodelivered = principal_sodelivered + crs.getInt("sodelivered");
						principal_pendingbooking = principal_pendingbooking + crs.getInt("pendingbooking");
						principal_pendingdelivery = principal_pendingdelivery + crs.getInt("pendingdelivery");
						principal_cancellation = principal_cancellation + crs.getInt("cancellation");
						principal_testdrives = principal_testdrives + crs.getInt("testdrives");
						principal_kpitestdrives = principal_kpitestdrives + crs.getInt("kpitestdrives");
						principal_homevisit = principal_homevisit + crs.getInt("homevisit");
						principal_kpihomevisit = principal_kpihomevisit + crs.getInt("kpihomevisit");
						principal_mga_sales = principal_mga_sales + crs.getInt("mga_amount");
						principal_maruti_insurance = principal_maruti_insurance + crs.getInt("maruti_insur");
						principal_extwarranty = principal_extwarranty + crs.getInt("extended_warranty");
						principal_fincases = principal_fincases + crs.getInt("fin_cases");
						principal_exchange = principal_exchange + crs.getInt("exchange");
						principal_evaluation = principal_evaluation + crs.getInt("evaluation");
					}

					if (check_regionttl_id.equals(crs.getString("region_id")) && dr_totalby.equals("4")) {
						region_retailtarget = region_retailtarget + crs.getInt("retailtarget");
						region_enquirytarget = region_enquirytarget + crs.getInt("enquirytarget");
						region_enquiryopen = region_enquiryopen + crs.getInt("enquiryopen");
						region_enquiryfresh = region_enquiryfresh + crs.getInt("enquiryfresh");
						region_enquirylost = region_enquirylost + crs.getInt("enquirylost");
						region_pendingenquiry = region_pendingenquiry + crs.getInt("pendingenquiry");
						region_soretail = region_soretail + crs.getInt("soretail");
						region_sodelivered = region_sodelivered + crs.getInt("sodelivered");
						region_pendingbooking = region_pendingbooking + crs.getInt("pendingbooking");
						region_pendingdelivery = region_pendingdelivery + crs.getInt("pendingdelivery");
						region_cancellation = region_cancellation + crs.getInt("cancellation");
						region_testdrives = region_testdrives + crs.getInt("testdrives");
						region_kpitestdrives = region_kpitestdrives + crs.getInt("kpitestdrives");
						region_homevisit = region_homevisit + crs.getInt("homevisit");
						region_kpihomevisit = region_kpihomevisit + crs.getInt("kpihomevisit");
						region_mga_sales = region_mga_sales + crs.getInt("mga_amount");
						region_maruti_insurance = region_maruti_insurance + crs.getInt("maruti_insur");
						region_extwarranty = region_extwarranty + crs.getInt("extended_warranty");
						region_fincases = region_fincases + crs.getInt("fin_cases");
						region_exchange = region_exchange + crs.getInt("exchange");
						region_evaluation = region_evaluation + crs.getInt("evaluation");
					}

					// ==================================================================================================================
					if (!check_model_id.equals(crs.getString("model_id"))) {
						modelcount++;
						modelname = crs.getString("model_name");
						check_model_id = crs.getString("model_id");
					}
					// ==================================================================================================================

					if (!check_branchttl_id.equals(crs.getString("branch_id")) && dr_totalby.equals("2")) {

						// SOP("333");
						branchcount++;
						check_branch_id = crs.getString("branch_id");
						Str.append("<tr>");
						Str.append("<td align=center valign=top>");
						Str.append("" + branchcount + "");
						Str.append("</td>");
						Str.append("<td   nowrap><b>");
						Str.append(branchname);
						Str.append("</b></td>");
						// branch_retailtarget
						Str.append("<td  ><b>");
						Str.append(branch_retailtarget);
						Str.append("</b></td>");
						// branch_enquirytarget
						Str.append("<td  ><b>");
						Str.append(branch_enquirytarget);
						Str.append("</b></td>");
						// branch_enquiryopen
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_enquiryopen);
						Str.append("</a></b></td>");
						// branch_enquiryfresh
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_enquiryfresh);
						Str.append("</a></b></td>");
						// branch_enquirylost
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_enquirylost);
						Str.append("</a></b></td>");
						// branch_pendingenquiry
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_pendingenquiry);
						Str.append("</a></b></td>");
						// branch_soretail
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_soretail);
						Str.append("</a></b></td>");
						// branch_soretail_perc
						branch_soretail_perc = getPercentage((double) branch_soretail, (double) branch_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(branch_soretail_perc).append("%");
						Str.append("</b></td>");
						// branch_sodelivered
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_sodelivered);
						Str.append("</a></b></td>");

						// branch_pendingbooking
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_pendingbooking);
						Str.append("</a></b></td>");
						// branch_pendingbooking_perc
						branch_pendingbooking_perc = getPercentage((double) branch_pendingbooking, (double) branch_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(branch_pendingbooking_perc).append("%");
						Str.append("</b></td>");
						// branch_pendingdelivery
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_pendingdelivery);
						Str.append("</a></b></td>");
						// branch_cancellation
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_cancellation);
						Str.append("</a></b></td>");
						// branch_testdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_testdrives);
						Str.append("</a></b></td>");
						// branch_kpitestdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_kpitestdrives);
						Str.append("</a></b></td>");
						// branch_testdrives_perc
						branch_testdrives_perc = getPercentage((double) branch_kpitestdrives, (double) branch_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(branch_testdrives_perc).append("%");
						Str.append("</b></td>");
						// branch_homevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_homevisit);
						Str.append("</a></b></td>");
						// branch_kpihomevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append(check_branch_id)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_kpihomevisit);
						Str.append("</a></b></td>");
						// branch_homevisit_perc
						branch_homevisit_perc = getPercentage((double) branch_kpihomevisit, (double) branch_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(branch_homevisit_perc).append("%");
						Str.append("</b></td>");
						// branch_mga_sales
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_mga_sales);
						Str.append("</a></b></td>");
						// branch_maruti_insurance
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_maruti_insurance);
						Str.append("</a></b></td>");
						// branch_extwarranty
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_extwarranty);
						Str.append("</a></b></td>");
						// branch_fincases
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_fincases);
						Str.append("</a></b></td>");
						// branch_exchange
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_exchange);
						Str.append("</a></b></td>");
						// branch_exchange_perc
						branch_exchange_perc = getPercentage((double) branch_exchange, (double) branch_pendingbooking);
						Str.append("<td><b>");
						Str.append(branch_exchange_perc).append("%");
						Str.append("</b></td>");
						// branch_evaluation
						Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("");
						Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
						Str.append(branch_evaluation);
						Str.append("</a></b></td>");
						// branch_evaluation_perc
						branch_evaluation_perc = getPercentage((double) branch_evaluation, (double) branch_enquiryfresh);
						Str.append("<td><b>");
						Str.append(branch_evaluation_perc).append("%");
						Str.append("</b></td>");
						Str.append("</tr>");

						check_branchttl_id = crs.getString("branch_id");
						branchname = crs.getString("branch_name");

						total_retailtarget = total_retailtarget - crs.getInt("retailtarget");
						total_enquirytarget = total_enquirytarget - crs.getInt("enquirytarget");
						total_enquiryopen = total_enquiryopen - crs.getInt("enquiryopen");
						total_enquiryfresh = total_enquiryfresh - crs.getInt("enquiryfresh");
						total_enquirylost = total_enquirylost - crs.getInt("enquirylost");
						total_soretail = total_soretail - crs.getInt("soretail");
						total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
						total_pendingenquiry = total_pendingenquiry - crs.getInt("pendingenquiry");
						total_pendingbooking = total_pendingbooking - crs.getInt("pendingbooking");
						total_pendingbooking_perc = getPercentage((double) total_pendingbooking, (double) total_enquiryfresh);
						total_pendingdelivery = total_pendingdelivery - crs.getInt("pendingdelivery");
						total_cancellation = total_cancellation - crs.getInt("cancellation");
						total_testdrives = total_testdrives - crs.getInt("testdrives");
						total_kpitestdrives = total_kpitestdrives - crs.getInt("kpitestdrives");
						total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
						total_homevisit = total_homevisit - crs.getInt("homevisit");
						total_kpihomevisit = total_kpihomevisit - crs.getInt("kpihomevisit");
						total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
						total_mga_sales = total_mga_sales - crs.getInt("mga_amount");
						total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
						total_extwarranty = total_extwarranty - crs.getInt("extended_warranty");
						total_fincases = total_fincases - crs.getInt("fin_cases");
						total_exchange = total_exchange - crs.getInt("exchange");
						total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);

						branch_retailtarget = 0;
						branch_enquirytarget = 0;
						branch_enquiryopen = 0;
						branch_enquiryfresh = 0;
						branch_enquirylost = 0;
						branch_soretail = 0;
						branch_soretail_perc = "0";
						branch_sodelivered = 0;
						branch_pendingenquiry = 0;
						branch_pendingbooking = 0;
						branch_pendingbooking_perc = "0";
						branch_pendingdelivery = 0;
						branch_cancellation = 0;
						branch_testdrives = 0;
						branch_testdrives_perc = "0";
						branch_kpitestdrives = 0;
						branch_homevisit = 0;
						branch_homevisit_perc = "0";
						branch_kpihomevisit = 0;
						branch_mga_sales = 0;
						branch_maruti_insurance = 0;
						branch_extwarranty = 0;
						branch_fincases = 0;
						branch_exchange = 0;
						branch_exchange_perc = "0";
						branch_evaluation = 0;
						branch_evaluation_perc = "0";
						crs.previous();
					}
					if (!check_principalttl_id.equals(crs.getString("brand_id")) && dr_totalby.equals("3")) {
						// SOP("4444");
						principalcount++;
						check_brand_id = crs.getString("brand_id");
						Str.append("<tr>");
						Str.append("<td align=center valign=top>");
						Str.append("" + principalcount + "");
						Str.append("</td>");
						Str.append("<td   nowrap><b>");
						Str.append(principalname);
						Str.append("</b></td>");
						// principal_retailtarget
						Str.append("<td  ><b>");
						Str.append(principal_retailtarget);
						Str.append("</b></td>");
						// principal_enquirytarget
						Str.append("<td  ><b>");
						Str.append(principal_enquirytarget);
						Str.append("</b></td>");
						// principal_enquiryopen
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&brand_id=")
								.append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_enquiryopen);
						Str.append("</a></b></td>");
						// principal_enquiryfresh
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_enquiryfresh);
						Str.append("</a></b></td>");
						// principal_enquirylost
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_enquirylost);
						Str.append("</a></b></td>");
						// principal_pendingenquiry
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_pendingenquiry);
						Str.append("</a></b></td>");
						// principal_soretail
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_soretail);
						Str.append("</a></b></td>");
						// principal_soretail_perc
						principal_soretail_perc = getPercentage((double) principal_soretail, (double) principal_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(principal_soretail_perc).append("%");
						Str.append("</b></td>");
						// principal_sodelivered
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_sodelivered);
						Str.append("</a></b></td>");
						// principal_pendingbooking
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_pendingbooking);
						Str.append("</a></b></td>");
						// principal_pendingbooking_perc
						principal_pendingbooking_perc = getPercentage((double) principal_pendingbooking, (double) principal_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(principal_pendingbooking_perc).append("%");
						Str.append("</b></td>");
						// principal_pendingdelivery
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_pendingdelivery);
						Str.append("</a></b></td>");
						// principal_cancellation
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_cancellation);
						Str.append("</a></b></td>");
						// principal_testdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=").append(check_principalttl_id).append(" target=_blank>");
						Str.append(principal_testdrives);
						Str.append("</a></b></td>");
						// principal_kpitestdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_kpitestdrives);
						Str.append("</a></b></td>");
						// principal_testdrives_perc
						principal_testdrives_perc = getPercentage((double) principal_kpitestdrives, (double) principal_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(principal_testdrives_perc).append("%");
						Str.append("</b></td>");
						// principal_homevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_homevisit);
						Str.append("</a></b></td>");
						// principal_kpihomevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_kpihomevisit);
						Str.append("</a></b></td>");
						// principal_homevisit_perc
						principal_homevisit_perc = getPercentage((double) principal_kpihomevisit, (double) principal_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(principal_homevisit_perc).append("%");
						Str.append("</b></td>");
						// principal_mga_sales
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_mga_sales);
						Str.append("</a></b></td>");
						// principal_maruti_insurance
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_maruti_insurance);
						Str.append("</a></b></td>");
						// principal_extwarranty
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_extwarranty);
						Str.append("</a></b></td>");
						// principal_fincases
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_fincases);
						Str.append("</a></b></td>");
						// principal_exchange
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_exchange);
						Str.append("</a></b></td>");
						// principal_exchange_perc
						principal_exchange_perc = getPercentage((double) principal_exchange, (double) principal_pendingbooking);
						Str.append("<td><b>");
						Str.append(principal_exchange_perc).append("%");
						Str.append("</b></td>");
						// principal_evaluation
						Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&brand_id=")
								.append(check_principalttl_id)
								.append(" target=_blank>");
						Str.append(principal_evaluation);
						Str.append("</a></b></td>");
						// principal_evaluation_perc
						principal_evaluation_perc = getPercentage((double) principal_evaluation, (double) principal_enquiryfresh);
						Str.append("<td><b>");
						Str.append(principal_evaluation_perc).append("%");
						Str.append("</b></td>");
						Str.append("</tr>");

						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");

						total_retailtarget = total_retailtarget - crs.getInt("retailtarget");
						total_enquirytarget = total_enquirytarget - crs.getInt("enquirytarget");
						total_enquiryopen = total_enquiryopen - crs.getInt("enquiryopen");
						total_enquiryfresh = total_enquiryfresh - crs.getInt("enquiryfresh");
						total_enquirylost = total_enquirylost - crs.getInt("enquirylost");
						total_soretail = total_soretail - crs.getInt("soretail");
						total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
						total_pendingenquiry = total_pendingenquiry - crs.getInt("pendingenquiry");
						total_pendingbooking = total_pendingbooking - crs.getInt("pendingbooking");
						total_pendingbooking_perc = getPercentage((double) total_pendingbooking, (double) total_enquiryfresh);
						total_pendingdelivery = total_pendingdelivery - crs.getInt("pendingdelivery");
						total_cancellation = total_cancellation - crs.getInt("cancellation");
						total_testdrives = total_testdrives - crs.getInt("testdrives");
						total_kpitestdrives = total_kpitestdrives - crs.getInt("kpitestdrives");
						total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
						total_homevisit = total_homevisit - crs.getInt("homevisit");
						total_kpihomevisit = total_kpihomevisit - crs.getInt("kpihomevisit");
						total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
						total_mga_sales = total_mga_sales - crs.getInt("mga_amount");
						total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
						total_extwarranty = total_extwarranty - crs.getInt("extended_warranty");
						total_fincases = total_fincases - crs.getInt("fin_cases");
						total_exchange = total_exchange - crs.getInt("exchange");
						total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);

						principal_retailtarget = 0;
						principal_enquirytarget = 0;
						principal_enquiryopen = 0;
						principal_enquiryfresh = 0;
						principal_enquirylost = 0;
						principal_soretail = 0;
						principal_sodelivered = 0;
						principal_soretail_perc = "0";
						principal_pendingenquiry = 0;
						principal_pendingbooking = 0;
						principal_pendingbooking_perc = "0";
						principal_pendingdelivery = 0;
						principal_cancellation = 0;
						principal_testdrives = 0;
						principal_testdrives_perc = "0";
						principal_kpitestdrives = 0;
						principal_homevisit = 0;
						principal_homevisit_perc = "0";
						principal_kpihomevisit = 0;
						principal_mga_sales = 0;
						principal_maruti_insurance = 0;
						principal_extwarranty = 0;
						principal_fincases = 0;
						principal_exchange = 0;
						principal_exchange_perc = "0";
						principal_evaluation = 0;
						principal_evaluation_perc = "0";
						crs.previous();
					}
					// //////////// region///////////

					if (!check_regionttl_id.equals(crs.getString("region_id")) && dr_totalby.equals("4")) {
						// SOP("5555");
						regioncount++;
						check_region_id = crs.getString("region_id");
						Str.append("<tr>");
						Str.append("<td align=center valign=top>");
						Str.append("" + regioncount + "");
						Str.append("</td>");
						Str.append("<td   nowrap><b>");
						Str.append(regionname);
						Str.append("</b></td>");
						// region_retailtarget
						Str.append("<td  ><b>");
						Str.append(region_retailtarget);
						Str.append("</b></td>");
						// region_enquirytarget
						Str.append("<td  ><b>");
						Str.append(region_enquirytarget);
						Str.append("</b></td>");
						// region_enquiryopen
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&region_id=")
								.append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_enquiryopen);
						Str.append("</a></b></td>");
						// region_enquiryfresh
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_enquiryfresh);
						Str.append("</a></b></td>");
						// region_enquirylost
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_enquirylost);
						Str.append("</a></b></td>");
						// region_pendingenquiry
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_pendingenquiry);
						Str.append("</a></b></td>");
						// region_soretail
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_soretail);
						Str.append("</a></b></td>");
						// region_soretail_perc
						region_soretail_perc = getPercentage((double) region_soretail, (double) region_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(region_soretail_perc).append("%");
						Str.append("</b></td>");
						// region_sodelivered
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_sodelivered);
						Str.append("</a></b></td>");
						// region_pendingbooking
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_pendingbooking);
						Str.append("</a></b></td>");
						// region_pendingbooking_perc
						region_pendingbooking_perc = getPercentage((double) region_pendingbooking, (double) region_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(region_pendingbooking_perc).append("%");
						Str.append("</b></td>");
						// region_pendingdelivery
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_pendingdelivery);
						Str.append("</a></b></td>");
						// region_cancellation
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_cancellation);
						Str.append("</a></b></td>");
						// region_testdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=").append(check_regionttl_id).append(" target=_blank>");
						Str.append(region_testdrives);
						Str.append("</a></b></td>");
						// region_kpitestdrives
						Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_kpitestdrives);
						Str.append("</a></b></td>");
						// region_testdrives_perc
						region_testdrives_perc = getPercentage((double) region_kpitestdrives, (double) region_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(region_testdrives_perc).append("%");
						Str.append("</b></td>");
						// region_homevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_homevisit);
						Str.append("</a></b></td>");
						// region_kpihomevisit
						Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_kpihomevisit);
						Str.append("</a></b></td>");
						// region_homevisit_perc
						region_homevisit_perc = getPercentage((double) region_kpihomevisit, (double) region_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(region_homevisit_perc).append("%");
						Str.append("</b></td>");
						// region_mga_sales
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_mga_sales);
						Str.append("</a></b></td>");
						// region_maruti_insurance
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_maruti_insurance);
						Str.append("</a></b></td>");
						// region_extwarranty
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_extwarranty);
						Str.append("</a></b></td>");
						// region_fincases
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_fincases);
						Str.append("</a></b></td>");
						// region_exchange
						Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_exchange);
						Str.append("</a></b></td>");
						// region_exchange_perc
						region_exchange_perc = getPercentage((double) region_exchange, (double) region_pendingbooking);
						Str.append("<td><b>");
						Str.append(region_exchange_perc).append("%");
						Str.append("</b></td>");
						// region_evaluation
						Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
						Str.append(check_model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&region_id=")
								.append(check_regionttl_id)
								.append(" target=_blank>");
						Str.append(region_evaluation);
						Str.append("</a></b></td>");
						// region_evaluation_perc
						region_evaluation_perc = getPercentage((double) region_evaluation, (double) region_enquiryfresh);
						Str.append("<td><b>");
						Str.append(region_evaluation_perc).append("%");
						Str.append("</b></td>");
						Str.append("</tr>");

						check_regionttl_id = crs.getString("region_id");
						regionname = crs.getString("region_name");

						total_retailtarget = total_retailtarget - crs.getInt("retailtarget");
						total_enquirytarget = total_enquirytarget - crs.getInt("enquirytarget");
						total_enquiryopen = total_enquiryopen - crs.getInt("enquiryopen");
						total_enquiryfresh = total_enquiryfresh - crs.getInt("enquiryfresh");
						total_enquirylost = total_enquirylost - crs.getInt("enquirylost");
						total_soretail = total_soretail - crs.getInt("soretail");
						total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
						total_pendingenquiry = total_pendingenquiry - crs.getInt("pendingenquiry");
						total_pendingbooking = total_pendingbooking - crs.getInt("pendingbooking");
						total_pendingbooking_perc = getPercentage((double) total_pendingbooking, (double) total_enquiryfresh);
						total_pendingdelivery = total_pendingdelivery - crs.getInt("pendingdelivery");
						total_cancellation = total_cancellation - crs.getInt("cancellation");
						total_testdrives = total_testdrives - crs.getInt("testdrives");
						total_kpitestdrives = total_kpitestdrives - crs.getInt("kpitestdrives");
						total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
						total_homevisit = total_homevisit - crs.getInt("homevisit");
						total_kpihomevisit = total_kpihomevisit - crs.getInt("kpihomevisit");
						total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
						total_mga_sales = total_mga_sales - crs.getInt("mga_amount");
						total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
						total_extwarranty = total_extwarranty - crs.getInt("extended_warranty");
						total_fincases = total_fincases - crs.getInt("fin_cases");
						total_exchange = total_exchange - crs.getInt("exchange");
						total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);

						region_retailtarget = 0;
						region_enquirytarget = 0;
						region_enquiryopen = 0;
						region_enquiryfresh = 0;
						region_enquirylost = 0;
						region_soretail = 0;
						region_soretail_perc = "0";
						region_sodelivered = 0;
						region_pendingenquiry = 0;
						region_pendingbooking = 0;
						region_pendingbooking_perc = "0";
						region_pendingdelivery = 0;
						region_cancellation = 0;
						region_testdrives = 0;
						region_testdrives_perc = "0";
						region_kpitestdrives = 0;
						region_homevisit = 0;
						region_homevisit_perc = "0";
						region_kpihomevisit = 0;
						region_mga_sales = 0;
						region_maruti_insurance = 0;
						region_extwarranty = 0;
						region_fincases = 0;
						region_exchange = 0;
						region_exchange_perc = "0";
						region_evaluation = 0;
						region_evaluation_perc = "0";
						crs.previous();
					}

					// End While Loop
				}

				// ==================================================================================================================
				// Print model Totals
				if (dr_totalby.equals("1")) {
					Str.append("<tr><td align=center valign=top>");
					if (dr_totalby.equals("1")) {
						Str.append("" + modelcount + "");
					} else {
						Str.append("&nbsp");
					}
					Str.append("</td><td   nowrap><b>");
					if (dr_totalby.equals("1")) {
						Str.append(modelname);
					} else {
						Str.append("Model Total:");
					}
					Str.append("</b></td>");

					// model_retailtarget
					Str.append("<td  ><b>");
					Str.append(model_retailtarget);
					Str.append("</b></td>");
					// model_enquirytarget
					Str.append("<td  ><b>");
					Str.append(model_enquirytarget);
					Str.append("</b></td>");
					// model_enquiryopen
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_enquiryopen);
					Str.append("</a></b></td>");

					// model_enquiryfresh
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_enquiryfresh);
					Str.append("</a></b></td>");
					// model_enquirylost
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_enquirylost);
					Str.append("</a></b></td>");
					// model_pendingenquiry
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					SOP("model_pendingenquiry-------2---------" + model_pendingenquiry);
					Str.append(model_pendingenquiry);
					Str.append("</a></b></td>");
					// model_soretail
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_soretail);
					Str.append("</a></b></td>");
					// model_soretail_perc
					model_soretail_perc = getPercentage((double) model_soretail, (double) model_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(model_soretail_perc).append("%");
					Str.append("</b></td>");
					// model_sodelivered
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_sodelivered);
					Str.append("</a></b></td>");
					// model_pendingbooking
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_pendingbooking);
					Str.append("</a></b></td>");
					// model_pendingbooking_perc
					model_pendingbooking_perc = getPercentage((double) model_pendingbooking, (double) model_enquiryfresh);
					// SOP("model_testdrives_perc==" + count + "====" + ((double) model_testdrives) / ((double) model_enquiryfresh));
					Str.append("<td  ><b>");
					Str.append(model_pendingbooking_perc).append("%");
					Str.append("</b></td>");
					// model_pendingdelivery
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_pendingdelivery);
					Str.append("</a></b></td>");
					// model_cancellation
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_cancellation);
					Str.append("</a></b></td>");
					// model_testdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_testdrives);
					Str.append("</a></b></td>");
					// model_kpitestdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_kpitestdrives);
					Str.append("</a></b></td>");
					// model_testdrives_perc
					model_testdrives_perc = getPercentage((double) model_kpitestdrives, (double) model_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(model_testdrives_perc).append("%");
					Str.append("</b></td>");
					// model_homevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_homevisit);
					Str.append("</a></b></td>");
					// model_kpihomevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&soe_id=").append(soe_id)
							.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_kpihomevisit);
					Str.append("</a></b></td>");
					// model_homevisit_perc
					model_homevisit_perc = getPercentage((double) model_kpihomevisit, (double) model_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(model_homevisit_perc).append("%");
					Str.append("</b></td>");
					// model_mga_sales
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_mga_sales);
					Str.append("</a></b></td>");
					// model_maruti_insurance
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_maruti_insurance);
					Str.append("</a></b></td>");
					// model_extwarranty
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=").append(check_model_id).append("&region_id=")
							.append(region_id)
							.append("&starttime=")
							.append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_extwarranty);
					Str.append("</a></b></td>");
					// model_fincases
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_fincases);
					Str.append("</a></b></td>");
					// model_exchange
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_exchange);
					Str.append("</a></b></td>");
					// model_exchange_perc
					model_exchange_perc = getPercentage((double) model_exchange, (double) model_pendingbooking);
					Str.append("<td><b>");
					Str.append(model_exchange_perc).append("%");
					Str.append("</b></td>");
					// model_evaluation
					Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=").append(check_model_id).append("&region_id=").append(region_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
					Str.append(model_evaluation);
					Str.append("</a></b></td>");
					// model_evaluation_perc
					model_evaluation_perc = getPercentage((double) model_evaluation, (double) model_enquiryfresh);
					Str.append("<td><b>");
					Str.append(model_evaluation_perc).append("%");
					Str.append("</b></td>");
					Str.append("</tr>\n");
					// ==================================================================================================================
				}
				if (dr_totalby.equals("2")) {

					branchcount++;
					Str.append("<tr>");
					Str.append("<td align=center valign=top>");
					Str.append("" + branchcount + "");
					Str.append("</td>");
					Str.append("<td   nowrap><b>");
					Str.append(branchname);
					Str.append("</b></td>");
					// branch_retailtarget
					Str.append("<td  ><b>");
					Str.append(branch_retailtarget);
					Str.append("</b></td>");
					// branch_enquirytarget
					Str.append("<td  ><b>");
					Str.append(branch_enquirytarget);
					Str.append("</b></td>");
					// branch_enquiryopen
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_enquiryopen);
					Str.append("</a></b></td>");
					// branch_enquiryfresh
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_enquiryfresh);
					Str.append("</a></b></td>");
					// branch_enquirylost
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_enquirylost);
					Str.append("</a></b></td>");
					// branch_pendingenquiry
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_pendingenquiry);
					Str.append("</a></b></td>");
					// branch_soretail
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_soretail);
					Str.append("</a></b></td>");
					// branch_soretail_perc
					branch_soretail_perc = getPercentage((double) branch_soretail, (double) branch_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(branch_soretail_perc).append("%");
					Str.append("</b></td>");
					// branch_sodelivered
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_sodelivered);
					Str.append("</a></b></td>");
					// branch_pendingbooking
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_pendingbooking);
					Str.append("</a></b></td>");
					// branch_pendingbooking_perc
					branch_pendingbooking_perc = getPercentage((double) branch_pendingbooking, (double) branch_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(branch_pendingbooking_perc).append("%");
					Str.append("</b></td>");
					// branch_pendingdelivery
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_pendingdelivery);
					Str.append("</a></b></td>");
					// branch_cancellation
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_cancellation);
					Str.append("</a></b></td>");
					// branch_testdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_testdrives);
					Str.append("</a></b></td>");
					// branch_kpitestdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_kpitestdrives);
					Str.append("</a></b></td>");
					// branch_testdrives_perc
					branch_testdrives_perc = getPercentage((double) branch_kpitestdrives, (double) branch_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(branch_testdrives_perc).append("%");
					Str.append("</b></td>");
					// branch_homevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_homevisit);
					Str.append("</a></b></td>");
					// branch_kpihomevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&branch_id=")
							.append(check_branch_id)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_kpihomevisit);
					Str.append("</a></b></td>");
					// branch_homevisit_perc
					branch_homevisit_perc = getPercentage((double) branch_kpihomevisit, (double) branch_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(branch_homevisit_perc).append("%");
					Str.append("</b></td>");
					// branch_mga_sales
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_mga_sales);
					Str.append("</a></b></td>");
					// branch_maruti_insurance
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_maruti_insurance);
					Str.append("</a></b></td>");
					// branch_extwarranty
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_extwarranty);
					Str.append("</a></b></td>");
					// branch_fincases
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_fincases);
					Str.append("</a></b></td>");
					// branch_exchange
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_exchange);
					Str.append("</a></b></td>");
					// branch_exchange_perc
					branch_exchange_perc = getPercentage((double) branch_exchange, (double) branch_pendingbooking);
					Str.append("<td><b>");
					Str.append(branch_exchange_perc).append("%");
					Str.append("</b></td>");
					// branch_evaluation
					Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("");
					Str.append("&dr_branch_id=").append(check_branchttl_id).append(" target=_blank>");
					Str.append(branch_evaluation);
					Str.append("</a></b></td>");
					// branch_evaluation_perc
					branch_evaluation_perc = getPercentage((double) branch_evaluation, (double) branch_enquiryfresh);
					Str.append("<td><b>");
					Str.append(branch_evaluation_perc).append("%");
					Str.append("</b></td>");
					Str.append("</tr>");

					branch_retailtarget = 0;
					branch_enquirytarget = 0;
					branch_enquiryopen = 0;
					branch_enquiryfresh = 0;
					branch_enquirylost = 0;
					branch_soretail = 0;
					branch_sodelivered = 0;
					branch_soretail_perc = "0";
					branch_pendingenquiry = 0;
					branch_pendingbooking = 0;
					branch_pendingbooking_perc = "0";
					branch_pendingdelivery = 0;
					branch_cancellation = 0;
					branch_testdrives = 0;
					branch_testdrives_perc = "0";
					branch_kpitestdrives = 0;
					branch_homevisit = 0;
					branch_homevisit_perc = "0";
					branch_kpihomevisit = 0;
					branch_mga_sales = 0;
					branch_maruti_insurance = 0;
					branch_extwarranty = 0;
					branch_fincases = 0;
					branch_exchange = 0;
					branch_exchange_perc = "0";
					branch_evaluation = 0;
					branch_evaluation_perc = "0";
				}

				if (dr_totalby.equals("3")) {

					principalcount++;
					SOP("principalcount===2222===" + principalcount);
					Str.append("<tr>");
					Str.append("<td align=center valign=top>");
					Str.append("" + principalcount + "");
					Str.append("</td>");
					Str.append("<td   nowrap><b>");
					Str.append(principalname);
					Str.append("</b></td>");
					// principal_retailtarget
					Str.append("<td  ><b>");
					Str.append(principal_retailtarget);
					Str.append("</b></td>");
					// principal_enquirytarget
					Str.append("<td  ><b>");
					Str.append(principal_enquirytarget);
					Str.append("</b></td>");
					// principal_enquiryopen
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_enquiryopen);
					Str.append("</a></b></td>");
					// principal_enquiryfresh
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_enquiryfresh);
					Str.append("</a></b></td>");
					// principal_enquirylost
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_enquirylost);
					Str.append("</a></b></td>");
					// principal_pendingenquiry
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_pendingenquiry);
					Str.append("</a></b></td>");
					// principal_soretail
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_soretail);
					Str.append("</a></b></td>");
					// principal_soretail_perc
					principal_soretail_perc = getPercentage((double) principal_soretail, (double) principal_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(principal_soretail_perc).append("%");
					Str.append("</b></td>");
					// principal_sodelivered
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_sodelivered);
					Str.append("</a></b></td>");
					// principal_pendingbooking
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_pendingbooking);
					Str.append("</a></b></td>");
					// principal_pendingbooking_perc
					principal_pendingbooking_perc = getPercentage((double) principal_pendingbooking, (double) principal_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(principal_pendingbooking_perc).append("%");
					Str.append("</b></td>");
					// principal_pendingdelivery
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_pendingdelivery);
					Str.append("</a></b></td>");
					// principal_cancellation
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_cancellation);
					Str.append("</a></b></td>");
					// principal_testdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_testdrives);
					Str.append("</a></b></td>");
					// principal_kpitestdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_kpitestdrives);
					Str.append("</a></b></td>");
					// principal_testdrives_perc
					principal_testdrives_perc = getPercentage((double) principal_kpitestdrives, (double) principal_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(principal_testdrives_perc).append("%");
					Str.append("</b></td>");
					// principal_homevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_homevisit);
					Str.append("</a></b></td>");
					// principal_kpihomevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id)
							.append(" target=_blank>");
					Str.append(principal_kpihomevisit);
					Str.append("</a></b></td>");
					// principal_homevisit_perc
					principal_homevisit_perc = getPercentage((double) principal_kpihomevisit, (double) principal_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(principal_homevisit_perc).append("%");
					Str.append("</b></td>");
					// principal_mga_sales
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_mga_sales);
					Str.append("</a></b></td>");
					// principal_maruti_insurance
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_maruti_insurance);
					Str.append("</a></b></td>");
					// principal_extwarranty
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_extwarranty);
					Str.append("</a></b></td>");
					// principal_fincases
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_fincases);
					Str.append("</a></b></td>");
					// principal_exchange
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_exchange);
					Str.append("</a></b></td>");
					// principal_exchange_perc
					principal_exchange_perc = getPercentage((double) principal_exchange, (double) principal_pendingbooking);
					Str.append("<td><b>");
					Str.append(principal_exchange_perc).append("%");
					Str.append("</b></td>");
					// principal_evaluation
					Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&brand_id=")
							.append(check_principalttl_id).append(" target=_blank>");
					Str.append(principal_evaluation);
					Str.append("</a></b></td>");
					// principal_evaluation_perc
					principal_evaluation_perc = getPercentage((double) principal_evaluation, (double) principal_enquiryfresh);
					Str.append("<td><b>");
					Str.append(principal_evaluation_perc).append("%");
					Str.append("</b></td>");
					Str.append("</tr>");

					principal_retailtarget = 0;
					principal_enquirytarget = 0;
					principal_enquiryopen = 0;
					principal_enquiryfresh = 0;
					principal_enquirylost = 0;
					principal_soretail = 0;
					principal_soretail_perc = "0";
					principal_sodelivered = 0;
					principal_pendingenquiry = 0;
					principal_pendingbooking = 0;
					principal_pendingbooking_perc = "0";
					principal_pendingdelivery = 0;
					principal_cancellation = 0;
					principal_testdrives = 0;
					principal_testdrives_perc = "0";
					principal_kpitestdrives = 0;
					principal_homevisit = 0;
					principal_homevisit_perc = "0";
					principal_kpihomevisit = 0;
					principal_mga_sales = 0;
					principal_maruti_insurance = 0;
					principal_extwarranty = 0;
					principal_fincases = 0;
					principal_exchange = 0;
					principal_exchange_perc = "0";
					principal_evaluation = 0;
					principal_evaluation_perc = "0";
				}

				if (dr_totalby.equals("4")) {

					regioncount++;
					Str.append("<tr>");
					Str.append("<td>");
					Str.append("" + regioncount + "");
					Str.append("</td>");
					Str.append("<td   nowrap><b>");
					Str.append(regionname);
					Str.append("</b></td>");
					// region_retailtarget
					Str.append("<td  ><b>");
					Str.append(region_retailtarget);
					Str.append("</b></td>");
					// region_enquirytarget
					Str.append("<td  ><b>");
					Str.append(region_enquirytarget);
					Str.append("</b></td>");
					// region_enquiryopen
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_enquiryopen);
					Str.append("</a></b></td>");
					// region_enquiryfresh
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_enquiryfresh);
					Str.append("</a></b></td>");
					// region_enquirylost
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_enquirylost);
					Str.append("</a></b></td>");
					// region_pendingenquiry
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_pendingenquiry);
					Str.append("</a></b></td>");
					// region_soretail
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_soretail);
					Str.append("</a></b></td>");
					// region_soretail_perc
					region_soretail_perc = getPercentage((double) region_soretail, (double) region_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(region_soretail_perc).append("%");
					Str.append("</b></td>");
					// region_sodelivered
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_sodelivered);
					Str.append("</a></b></td>");
					// region_pendingbooking
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_pendingbooking);
					Str.append("</a></b></td>");
					// region_pendingbooking_perc
					region_pendingbooking_perc = getPercentage((double) region_pendingbooking, (double) region_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(region_pendingbooking_perc).append("%");
					Str.append("</b></td>");
					// region_pendingdelivery
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_pendingdelivery);
					Str.append("</a></b></td>");
					// region_cancellation
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_cancellation);
					Str.append("</a></b></td>");
					// region_testdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_testdrives);
					Str.append("</a></b></td>");
					// region_kpitestdrives
					Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_kpitestdrives);
					Str.append("</a></b></td>");
					// region_testdrives_perc
					region_testdrives_perc = getPercentage((double) region_kpitestdrives, (double) region_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(region_testdrives_perc).append("%");
					Str.append("</b></td>");
					// region_homevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_homevisit);
					Str.append("</a></b></td>");
					// region_kpihomevisit
					Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&soe_id=").append(soe_id).append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id)
							.append(" target=_blank>");
					Str.append(region_kpihomevisit);
					Str.append("</a></b></td>");
					// region_homevisit_perc
					region_homevisit_perc = getPercentage((double) region_kpihomevisit, (double) region_enquiryfresh);
					Str.append("<td  ><b>");
					Str.append(region_homevisit_perc).append("%");
					Str.append("</b></td>");
					// region_mga_sales
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_mga_sales);
					Str.append("</a></b></td>");
					// region_maruti_insurance
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_maruti_insurance);
					Str.append("</a></b></td>");
					// region_extwarranty
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_extwarranty);
					Str.append("</a></b></td>");
					// region_fincases
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_fincases);
					Str.append("</a></b></td>");
					// region_exchange
					Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_exchange);
					Str.append("</a></b></td>");
					// region_exchange_perc
					region_exchange_perc = getPercentage((double) region_exchange, (double) region_pendingbooking);
					Str.append("<td  ><b>");
					Str.append(region_exchange_perc).append("%");
					Str.append("</b></td>");
					// region_evaluation
					Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
					Str.append(model_id).append("&region_id=").append(region_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
							.append("&region_id=")
							.append(check_regionttl_id).append(" target=_blank>");
					Str.append(region_evaluation);
					Str.append("</a></b></td>");
					// region_evaluation_perc
					region_evaluation_perc = getPercentage((double) region_evaluation, (double) region_enquiryfresh);
					Str.append("<td><b>");
					Str.append(region_evaluation_perc).append("%");
					Str.append("</b></td>");
					Str.append("</tr>");

					region_retailtarget = 0;
					region_enquirytarget = 0;
					region_enquiryopen = 0;
					region_enquiryfresh = 0;
					region_enquirylost = 0;
					region_soretail = 0;
					region_soretail_perc = "0";
					region_sodelivered = 0;
					region_pendingenquiry = 0;
					region_pendingbooking = 0;
					region_pendingbooking_perc = "0";
					region_pendingdelivery = 0;
					region_cancellation = 0;
					region_testdrives = 0;
					region_testdrives_perc = "0";
					region_kpitestdrives = 0;
					region_homevisit = 0;
					region_homevisit_perc = "0";
					region_kpihomevisit = 0;
					region_mga_sales = 0;
					region_maruti_insurance = 0;
					region_extwarranty = 0;
					region_fincases = 0;
					region_exchange = 0;
					region_exchange_perc = "0";
					region_evaluation = 0;
					region_evaluation_perc = "0";
				}

				// ///// Display Grand Total
				Str.append("<tr>\n");
				Str.append("<td colspan=2><b>Total:</b></td>");
				// * total_retailtarget
				Str.append("<td  ><b>");
				Str.append(total_retailtarget).append("</b></td>");
				// * total_enquirytarget
				Str.append("<td  ><b>");
				// final total
				Str.append(total_enquirytarget).append("</b></td>");
				// * total_enquiryopen
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryopen&model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				// Str.append("&emp_id=enquiry_emp_id")
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_enquiryopen).append("</a></b></td>");
				// * total_enquiryfresh
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=").append(model_id).append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_enquiryfresh).append("</a></b></td>");
				// * total_enquirylost
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=").append(model_id).append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_enquirylost).append("</a></b></td>");
				// * total_pendingenquiry
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				SOP("total_pendingenquiry---3---------" + total_pendingenquiry);
				Str.append(total_pendingenquiry).append("</a></b></td>");
				// * total_soretail
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=soretail&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_soretail).append("</a></b></td>");
				// * total_soretail_perc
				Str.append("<td  ><b>");
				Str.append(total_soretail_perc).append("%").append("</b></td>");
				// * total_sodelivered
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_sodelivered).append("</a></b></td>");
				// * total_pendingbooking
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_pendingbooking).append("</a></b></td>");
				// * total_pendingbooking_perc
				Str.append("<td  ><b>");
				Str.append(total_pendingbooking_perc).append("%").append("</b></td>");
				// * total_pendingdelivery
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=pendingdelivery&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=")
						.append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_pendingdelivery).append("</a></b></td>");
				// * total_cancellation
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=cancellation&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_cancellation).append("</a></b></td>");
				// * total_testdrives
				Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_testdrives).append("</a></b></td>");
				// * total_kpitestdrives
				Str.append("<td  ><b><a href=").append(SearchURL).append("?testdrive=testdrives&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_kpitestdrives).append("</a></b></td>");
				// * total_testdrives_perc
				Str.append("<td  ><b>");
				Str.append(total_testdrives_perc).append("%").append("</b></td>");
				// * total_homevisit
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=homevisit&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_homevisit).append("</a></b></td>");
				// * total_kpihomevisit
				Str.append("<td  ><b><a href=").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_kpihomevisit).append("</a></b></td>");
				// * total_homevisit_perc
				Str.append("<td  ><b>");
				Str.append(total_homevisit_perc).append("%").append("</b></td>");
				// * total_mga_sales
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_mga_sales).append("</a></b></td>");
				// * total_maruti_insurance
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_maruti_insurance).append("</a></b></td>");
				// * total_extwarranty
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=")
						.append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_extwarranty).append("</a></b></td>");
				// * total_fincases
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=fincases&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_fincases).append("</a></b></td>");
				// * total_exchange
				Str.append("<td  ><b><a href=").append(SearchURL).append("?salesorder=exchange&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_exchange).append("</a></b></td>");
				// * total_exchange_perc
				Str.append("<td><b>");
				Str.append(total_exchange_perc).append("%").append("</b></td>");
				// * total_evaluation
				Str.append("<td><b><a href=").append(SearchURL).append("?enquiry=evaluation&" + "model_id=").append(model_id).append("&region_id=").append(region_id)

						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append(" target=_blank>");
				Str.append(total_evaluation).append("</a></b></td>");
				// * total_evaluation_perc
				Str.append("<td><b>");
				Str.append(total_evaluation_perc).append("%").append("</b></td>");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><br><br><br><b>No Details Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateSoe()
	{
		String sb = "";
		try
		{
			StrSql = " select soe_id, soe_name "
					+ " from " + compdb(comp_id) + "axela_soe "
					// + " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id"
					+ " order by soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("soe_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("soe_id"), soe_ids);
				sb = sb + ">" + crs.getString("soe_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb;
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Models</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");

		return Str.toString();
	}

	public String PopulateFuelTypes(HttpServletRequest request) {

		// String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";

		try {
			StrSql = " SELECT fueltype_id, fueltype_name "
					+ " FROM " + compdb(comp_id) + "axela_fueltype "
					+ " GROUP BY fueltype_id "
					+ " ORDER BY fueltype_name, fueltype_id";
			SOP("StrSql-------Populatefuels------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=fueltype_id size=10 multiple=multiple class=form-control id=fueltype_id style=\"padding:10px\">\n>";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("fueltype_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("fueltype_id"), fueltype_ids);
					stringval = stringval + ">" + crs.getString("fueltype_name") + "</option>\n";
				}
			}
			stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

}
