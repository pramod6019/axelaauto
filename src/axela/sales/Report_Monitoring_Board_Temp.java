package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Monitoring_Board_Temp extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids;
	public String preowned_model = "", include_inactive_exe = "0", include_preowned = "0";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "";
	public String dr_totalby = "0", dr_orderby = "";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";

	public String SearchURL = "<a href=../sales/report-monitoring-board-search-temp.jsp?filter=yes&";

	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public String marital_status = "", emp_active = "", sex = "", address = "", Img = "";
	public String currexp[];
	public String emp_prevexp[];
	public int years = 0;
	public int months = 0;
	public int days = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);

				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess;
					StrSearch += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrModel = " AND model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSoe = " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		dr_orderby = request.getParameter("dr_orderby");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");

		preowned_model = PadQuotes(request.getParameter("chk_preowned_model"));
		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		include_preowned = PadQuotes(request.getParameter("chk_include_preowned"));

		if (preowned_model.equals("on")) {
			preowned_model = "1";
		} else {
			preowned_model = "0";
		}

		if (include_inactive_exe.equals("on")) {
			include_inactive_exe = "1";
		} else {
			include_inactive_exe = "0";
		}

		if (include_preowned.equals("on")) {
			include_preowned = "1";
		} else {
			include_preowned = "0";
		}

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
		int total_booking = 0;
		String total_booking_perc = "0";
		int total_pendingbooking = 0;
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
		int total_preowned = 0;
		String total_preowned_perc = "0";
		int total_evaluation = 0;
		String total_evaluation_perc = "0";
		int total_exchange = 0;
		String total_exchange_perc = "0";

		// team fields Start

		int total_team_retailtarget = 0;
		double total_team_retailtarget_tilldate = 0;
		int total_team_enquirytarget = 0;
		double total_team_enquirytarget_tilldate = 0;
		int total_team_enquiryopen = 0;
		int total_team_enquiryfresh = 0;
		int total_team_enquirylost = 0;
		int total_team_soretail = 0;
		int total_team_sodelivered = 0;
		String total_team_soretail_perc = "0";
		int total_team_pendingenquiry = 0;
		int total_team_booking = 0;
		String total_team_booking_perc = "0";
		int total_team_pendingbooking = 0;
		int total_team_cancellation = 0;
		int total_team_testdrives = 0;
		String total_team_testdrives_perc = "0";
		int total_team_kpitestdrives = 0;
		int total_team_homevisit = 0;
		String total_team_homevisit_perc = "0";
		int total_team_kpihomevisit = 0;
		int total_team_accessamt = 0;
		int total_team_insurance = 0;
		int total_team_extwarranty = 0;
		int total_team_fincases = 0;
		int total_team_preowned = 0;
		String total_team_preowned_perc = "0";
		int total_team_evaluation = 0;
		String total_team_evaluation_perc = "0";
		int total_team_exchange = 0;
		String total_team_exchange_perc = "0";

		// team fields End

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();
		StringBuilder StrTeamHeader = new StringBuilder();
		StringBuilder StrTeamFooter = new StringBuilder();
		String filtervalue = "", teamfiltervalue = "";
		String tblTargetGroupby = "", tblEnqGroupby = "", tblEnqFollowupGroupby = "", tblSoGroupby = "", tblTestDriveGroupby = "", tblPreOnwedGroupby = "";
		String mainGroupBy = "", mainOrderBy = "", tblFilterJoin = "";
		String tbljoin = "";
		String empactivefilter = "";
		String totalby = "", teamtotalby = "";
		String chk_team_header_name = "";
		String chk_team_footer_name = "";
		String chk_team_footer_name1 = "";

		if (((dr_totalby.equals("2") || !team_id.equals("")) || include_inactive_exe.equals("0")) && !dr_totalby.equals("6")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe dupteamexe ON dupteamexe.teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team dupteam ON dupteam.team_id = dupteamexe.teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = enquiry_branch_id";
		} else if ((!branch_id.equals("") || !brand_id.equals("") || !region_id.equals("")) || (dr_totalby.equals("3") || dr_totalby.equals("4") || dr_totalby.equals("5"))) {
			tbljoin = " LEFT JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = enquiry_branch_id";
		} else if (dr_totalby.equals("6")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_inventory_item dupitem ON dupitem.item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model dupmodel ON dupmodel.model_id = dupitem.item_model_id";
		}

		if (include_inactive_exe.equals("0")) {
			empactivefilter = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " AND emp_active = 1";
		}

		switch (dr_totalby) {
			case "1" :
				tblTargetGroupby = " target_emp_id";
				tblEnqGroupby = " enquiry_emp_id";
				tblEnqFollowupGroupby = " enquiry_emp_id";
				tblSoGroupby = " so_emp_id";
				tblTestDriveGroupby = " enquiry_emp_id";
				tblPreOnwedGroupby = " preowned_sales_emp_id";
				mainGroupBy = " mainemp.emp_id";
				mainOrderBy = " mainemp.emp_name";
				tblFilterJoin = " mainemp.emp_id";
				break;

			case "2" :
				tblTargetGroupby = " target_emp_id";
				tblEnqGroupby = " team_id";
				tblEnqFollowupGroupby = " team_id";
				tblSoGroupby = " team_id";
				tblTestDriveGroupby = " team_id";
				tblPreOnwedGroupby = " team_id";
				mainGroupBy = " mainteam.team_id";
				mainOrderBy = " mainteam.team_name";
				tblFilterJoin = " mainteam.team_id";
				break;

			case "3" :
				tblTargetGroupby = " branch_id";
				tblEnqGroupby = " enquiry_branch_id";
				tblEnqFollowupGroupby = " enquiry_branch_id";
				tblSoGroupby = " so_branch_id";
				tblTestDriveGroupby = " enquiry_branch_id";
				tblPreOnwedGroupby = " preowned_branch_id";
				mainGroupBy = " mainbranch.branch_id";
				mainOrderBy = " mainbranch.branch_name";
				tblFilterJoin = " mainbranch.branch_id";
				break;

			case "4" :
				tblTargetGroupby = " branch_region_id";
				tblEnqGroupby = " branch_region_id";
				tblEnqFollowupGroupby = " branch_region_id";
				tblSoGroupby = " branch_region_id";
				tblTestDriveGroupby = " branch_region_id";
				tblPreOnwedGroupby = " branch_region_id";
				mainGroupBy = " mainregion.region_id";
				mainOrderBy = " mainregion.region_name";
				tblFilterJoin = " mainregion.region_id";
				break;

			case "5" :
				tblTargetGroupby = " branch_brand_id";
				tblEnqGroupby = " branch_brand_id";
				tblEnqFollowupGroupby = " branch_brand_id";
				tblSoGroupby = " branch_brand_id";
				tblTestDriveGroupby = " branch_brand_id";
				tblPreOnwedGroupby = " branch_brand_id";
				mainGroupBy = " mainbrand.brand_id";
				mainOrderBy = " mainbrand.brand_name";
				tblFilterJoin = " mainbrand.brand_id";
				break;

			case "6" :
				tblEnqGroupby = " model_id";
				tblEnqFollowupGroupby = " model_id";
				tblSoGroupby = " model_id";
				tblTestDriveGroupby = " model_id";
				tblPreOnwedGroupby = " model_id";
				mainGroupBy = " mainmodel.model_id";
				mainOrderBy = " mainmodel.model_name";
				tblFilterJoin = " mainmodel.model_id";
				break;
		}

		// QUERY START

		StrSql = " SELECT  COALESCE (mainemp.emp_id,'') AS emp_id, COALESCE (mainemp.emp_name,'') AS emp_name,"
				+ " mainemp.emp_ref_no, mainemp.emp_active,";
		if ((dr_totalby.equals("2") || !team_id.equals("")) || include_inactive_exe.equals("0")) {
			StrSql += "  COALESCE(mainteam.team_id,'') AS team_id, COALESCE (mainteam.team_name,'') AS team_name,";
		}
		StrSql += " COALESCE(mainbranch.branch_id,'') AS branch_id, COALESCE(mainbranch.branch_name,'') AS branch_name,"
				+ " COALESCE(mainregion.region_id,'') AS region_id, COALESCE(mainregion.region_name,'') AS region_name, "
				+ " COALESCE(mainbrand.brand_id,'') AS brand_id, COALESCE(mainbrand.brand_name,'') AS brand_name, ";
		if (dr_totalby.equals("6")) {
			StrSql += "  COALESCE(mainmodel.model_id,'') AS model_id, COALESCE (mainmodel.model_name,'') AS model_name,";
		}

		StrSql += " COALESCE (retailtarget, 0) AS retailtarget,"
				+ " COALESCE (enquirytarget, 0) AS enquirytarget,"
				+ " COALESCE (enquiryopen, 0) AS enquiryopen,"
				+ " COALESCE (enquiryfresh, 0) AS enquiryfresh,"
				+ " COALESCE (enquirylost, 0) AS enquirylost,"
				+ " COALESCE (pendingenquiry, 0) AS pendingenquiry,";
		if (include_preowned.equals("1")) {
			StrSql += " COALESCE (evaluation, 0) AS evaluation,";
		}

		StrSql += " COALESCE (soretail, 0) AS soretail," // soretail
				+ " COALESCE (sodelivered, 0) AS sodelivered," // sodelivered
				+ " COALESCE (booking, 0) AS booking," // booking
				+ " COALESCE (pendingbooking, 0) AS pendingbooking," // pendingbooking
				+ " COALESCE (cancellation, 0) AS cancellation,"
				+ " COALESCE (accessamt, 0) AS accessamt,"
				+ " COALESCE (insurcount, 0) AS insurcount,"
				+ " COALESCE (ewcount, 0) AS ewcount,"
				+ " COALESCE (fincasecount, 0) AS fincasecount,"
				+ " COALESCE (exchangecount, 0) AS exchangecount,";
		StrSql += " COALESCE (testdrives, 0) AS testdrives,"
				+ " COALESCE (kpitestdrives, 0) AS kpitestdrives,"
				+ " COALESCE (homevisit, 0) AS homevisit,"
				+ " COALESCE (kpihomevisit, 0) AS kpihomevisit ";
		if (include_preowned.equals("1")) {
			StrSql += ", COALESCE (preownedenquiry,	0) AS preownedenquiry";
		}

		// main join
		StrSql += " FROM " + compdb(comp_id) + "axela_emp mainemp"
				+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = mainemp.emp_jobtitle_id";
		if ((dr_totalby.equals("2") || !team_id.equals("")) || include_inactive_exe.equals("0")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe mainteamexe ON mainteamexe.teamtrans_emp_id = mainemp.emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team mainteam ON mainteam.team_id = mainteamexe.teamtrans_team_id";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch mainbranch ON mainbranch.branch_id = mainemp.emp_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region mainregion ON mainregion.region_id = mainbranch.branch_region_id"
				+ " INNER JOIN axelaauto.axela_brand mainbrand ON  mainbrand.brand_id = mainbranch.branch_brand_id";

		if (dr_totalby.equals("6")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry mainenquiry ON mainenquiry.enquiry_emp_id = mainemp.emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item mainitem ON mainitem.item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model mainmodel ON mainmodel.model_id = mainitem.item_model_id";
		}

		// target join
		StrSql += " LEFT JOIN ( SELECT SUM(modeltarget_so_count) AS retailtarget,"
				+ " SUM(modeltarget_enquiry_count) AS enquirytarget,"
				+ tblTargetGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id";

		if (!dr_totalby.equals("1") && !dr_totalby.equals("2")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp targetemp ON targetemp.emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe targetteamexe ON targetteamexe.teamtrans_emp_id = targetemp.emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team targetteam ON targetteam.team_id = targetteamexe.teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch targetbranch ON targetbranch.branch_id = targetemp.emp_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region targetregion ON targetregion.region_id = targetbranch.branch_region_id"
					+ " INNER JOIN axelaauto.axela_brand targetbrand ON targetbrand.brand_id = targetbranch.branch_brand_id";
		}

		StrSql += " WHERE 1=1"
				+ " AND substring(target_startdate, 1, 6) >= substr('" + starttime + "', 1, 6)"
				+ " AND substring(target_enddate, 1, 6) <= substr('" + endtime + "', 1, 6)"
				+ " GROUP BY " + tblTargetGroupby + " ) AS tbltarget ON tbltarget." + tblTargetGroupby + " = " + tblFilterJoin;

		// enquiry join
		StrSql += " LEFT JOIN (SELECT"

				+ " COUNT(DISTINCT CASE WHEN"
				+ " SUBSTR(enquiry_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
				+ " AND enquiry_status_id = 1 THEN enquiry_id END ) AS enquiryopen,"

				+ " COUNT(DISTINCT CASE WHEN"
				+ " SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " THEN enquiry_id END ) AS enquiryfresh,"

				+ " COUNT(DISTINCT CASE WHEN"
				+ " SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND ( enquiry_status_id = 3 OR enquiry_status_id = 4 )"
				+ " THEN enquiry_id END ) AS enquirylost,"

				+ " COUNT(DISTINCT CASE WHEN"
				+ " enquiry_status_id = 1"
				+ " THEN enquiry_id END ) AS pendingenquiry,"

				+ " COUNT(CASE WHEN"
				+ " testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " THEN testdrive_id END ) AS testdrives,"

				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " THEN enquiry_id END ) AS kpitestdrives,"

				+ " COUNT(CASE WHEN"
				+ " followup_feedbacktype_id = 9"
				+ " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
				+ " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
				+ " THEN followup_id END ) AS homevisit,"

				+ " COUNT(DISTINCT CASE WHEN"
				+ " followup_feedbacktype_id = 9"
				+ " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
				+ " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
				+ " THEN enquiry_id END ) AS kpihomevisit,";

		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND enquiry_evaluation = 1 , 1, 0)) AS evaluation,";
		}
		StrSql += " " + tblEnqGroupby + " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id";

		StrSql += tbljoin
				+ empactivefilter
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "enquiry_emp_id")
				// join for filter
				+ StrSoe + StrModel.replace("model_id", "enquiry_model_id")
				+ " GROUP BY " + tblEnqGroupby
				+ " ) AS tblenquiry ON tblenquiry." + tblEnqGroupby + " = " + tblFilterJoin;

		// so join
		StrSql += " LEFT JOIN (SELECT"
				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS soretail," // soretail

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_delivered_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_delivered_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS sodelivered," // sodelivered

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS booking," // booking

				+ " SUM(IF(so_active = '1' "
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = '' , 1,0)) AS pendingbooking," // pendingbooking

				+ " SUM(IF(so_active = '0'"
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = ''"
				+ " AND SUBSTR(so_cancel_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_cancel_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS cancellation,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,so_mga_amount,0)) AS accessamt,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_insur_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS insurcount,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_ew_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS ewcount,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_finance_amt > 0"
				+ "	AND so_fintype_id = 1"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS fincasecount, "

				+ " SUM(IF(so_active = '1'"
				+ " AND so_exchange_amount > 0"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " ,1,0)) AS exchangecount,";

		StrSql += " " + tblSoGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
		StrSql += tbljoin.replace("enquiry_emp_id", "so_emp_id")
				.replace("enquiry_branch_id", "so_branch_id")
				+ empactivefilter.replace("enquiry_emp_id", "so_emp_id")
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "so_emp_id")
				+ StrSoe + StrModel.replace("model_id", "item_model_id")
				+ " GROUP BY " + tblSoGroupby + " ) AS tblso ON tblso." + tblSoGroupby + " = " + tblFilterJoin;

		// // testdrive join
		// StrSql += " LEFT JOIN (SELECT "
		// + " SUM(IF(testdrive_fb_taken = 1"
		// + " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
		// + " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), 1, 0)) AS testdrives,"
		// + " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
		// + " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
		// + " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) THEN enquiry_id END) AS  kpitestdrives,"
		// + " " + tblTestDriveGroupby
		// + " FROM " + compdb(comp_id) + "axela_sales_testdrive"
		// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
		// + tbljoin
		// + empactivefilter
		// + " WHERE 1 = 1"
		// + StrSearch.replace("emp_id", "enquiry_emp_id")
		// + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
		// + " GROUP BY " + tblTestDriveGroupby + " ) AS tbltestdrives ON tbltestdrives." + tblTestDriveGroupby + " = " + tblFilterJoin;
		//
		// // followup join
		// StrSql += " LEFT JOIN ( SELECT"
		// + " SUM(IF(followup_feedbacktype_id = 9 , 1, 0)) AS homevisit,"
		// + " COUNT(DISTINCT CASE WHEN followup_feedbacktype_id = 9 THEN enquiry_id END) AS  kpihomevisit,"
		// + " " + tblEnqFollowupGroupby
		// + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
		// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
		// + tbljoin
		// + empactivefilter
		// + " WHERE 1 = 1"
		// + StrSearch.replace("emp_id", "enquiry_emp_id")
		// + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
		// + " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
		// + " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
		// + " GROUP BY " + tblEnqFollowupGroupby + " ) AS tblhomevisit ON tblhomevisit." + tblEnqFollowupGroupby + " = " + tblFilterJoin;

		if (include_preowned.equals("1")) {
			// preowned join
			StrSql += " LEFT JOIN ( SELECT"
					+ " COUNT(DISTINCT preowned_id) AS preownedenquiry,"
					+ tblPreOnwedGroupby
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = preowned_enquiry_id"
					+ tbljoin
					+ empactivefilter.replace("enquiry_emp_id", "preowned_sales_emp_id")
					+ " WHERE 1 = 1"
					+ StrSearch.replace("emp_id", "preowned_emp_id")
					+ " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND preowned_sales_emp_id != 0"
					+ " GROUP BY " + tblPreOnwedGroupby + " ) AS tblpreownedenquiry ON tblpreownedenquiry." + tblPreOnwedGroupby + " = " + tblFilterJoin;
		}

		StrSql += " WHERE emp_sales = '1' ";

		if (dr_totalby.equals("2")) {
			StrSql += " AND team_active = '1'";
		}

		StrSql += " AND branch_active = '1'";
		if (!include_inactive_exe.equals("1")) {
			StrSql += " AND emp_active = 1 ";
		}

		StrSql += StrSearch
				.replace("emp_id", "mainemp.emp_id")
				.replace("team_id", "mainteam.team_id")
				.replace("branch_id", "mainbranch.branch_id")
				.replace("branch_region_id", "mainregion.region_id")
				.replace("branch_brand_id", "mainbrand.brand_id")
				+ " GROUP BY " + mainGroupBy
				+ " ORDER BY ";

		if (!dr_orderby.equals("")) {
			StrSql += dr_orderby + " DESC";
		} else if (dr_totalby.equals("1") && !include_inactive_exe.equals("1")) {
			StrSql += " team_name ";
		} else {
			StrSql += mainOrderBy + " DESC ";
		}

		// QUERY END

		SOPInfo("monitor board query-------ListMonitorBoard-----------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				SearchURL += "brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&branch_id=" + branch_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&include_inactive_exe=" + include_inactive_exe
						+ "&model_id=" + model_id
						+ "&starttime=" + starttime
						+ "&endtime=" + endtime;

				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");

				switch (dr_totalby) {
					case "1" :
						StrHead.append("<th>Sales Consultant</th>\n");
						totalby = "emp";
						break;
					case "2" :
						StrHead.append("<th>Team</th>\n");
						totalby = "team";
						break;
					case "3" :
						StrHead.append("<th>Branch</th>\n");
						totalby = "branch";
						break;
					case "4" :
						StrHead.append("<th>Region</th>\n");
						totalby = "region";
						break;
					case "5" :
						StrHead.append("<th>Brand</th>\n");
						totalby = "brand";
						break;
				}

				StrHead.append("<th title=\"Retail Target\">RT</th>\n");
				StrHead.append("<th data-hide=\"phone\" title=\"Enquiry Target\">ET</th>\n");
				StrHead.append("<th data-hide=\"phone\">Open Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone\">Fresh Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Lost Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Pending Enquiry</th>\n");
				StrHead.append("<th data-hide=\"phone, tablet\">Retail</th>\n"); // soretail
				StrHead.append("<th data-hide=\"phone, tablet\">Retail %</th>\n"); // soretail_perc
				StrHead.append("<th data-hide=\"phone, tablet\" title=\"Delivered\">Del</th>\n"); // sodelivered
				StrHead.append("<th data-hide=\"phone, tablet\">Booking</th>\n"); // booking
				StrHead.append("<th data-hide=\"phone, tablet\">Booking %</th>\n"); // pendingbooking_perc
				StrHead.append("<th data-hide=\"phone, tablet\">Pending Booking</th>\n"); // pendingbooking
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
				if (include_preowned.equals("1")) {
					StrHead.append("<th data-hide=\"phone, tablet\" title=\"Preowned Enquiry\">PE </th>\n");
					StrHead.append("<th data-hide=\"phone, tablet\" title=\"Preowned Enquiry %\">PE %</th>\n");
					StrHead.append("<th data-hide=\"phone, tablet\" title=\"Evaluation\">Eval </th>\n");
					StrHead.append("<th data-hide=\"phone, tablet\" title=\"Evaluation\">Eval %</th>\n");
				}

				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\" id='table'>\n");
				// }
				Str.append(StrHead.toString());
				int count = 0;

				double total_retailtarget_tilldate = 0, total_enquirytarget_tilldate = 0;
				double day = 0;
				double daycount = 0;
				day = getDaysBetween(starttime, endtime);
				daycount = getDaysBetween(starttime, endtime)
						+ ((DaysInMonth(Integer.parseInt(SplitYear(endtime)), Integer.parseInt(SplitMonth(endtime))) - Integer.parseInt(SplitDate(endtime))))
						+ (Integer.parseInt(SplitDate(starttime)) - 1);
				while (crs.next()) {
					count++;

					switch (dr_totalby) {
						case "1" :
							filtervalue = crs.getString("emp_id");
							break;
						case "2" :
							filtervalue = crs.getString("team_id");
							break;
						case "3" :
							filtervalue = crs.getString("branch_id");
							break;
						case "4" :
							filtervalue = crs.getString("region_id");
							break;
						case "5" :
							filtervalue = crs.getString("brand_id");
							break;
					}

					total_retailtarget = total_retailtarget + crs.getInt("retailtarget");
					total_enquirytarget = total_enquirytarget + crs.getInt("enquirytarget");
					total_enquiryopen = total_enquiryopen + crs.getInt("enquiryopen");
					total_enquiryfresh = total_enquiryfresh + crs.getInt("enquiryfresh");
					total_enquirylost = total_enquirylost + crs.getInt("enquirylost");
					total_pendingenquiry = total_pendingenquiry + crs.getInt("pendingenquiry");
					total_soretail = total_soretail + crs.getInt("soretail");
					total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
					total_sodelivered = total_sodelivered + crs.getInt("sodelivered");
					total_booking = total_booking + crs.getInt("booking");
					total_booking_perc = getPercentage((double) total_booking, (double) total_enquiryfresh);
					total_pendingbooking = total_pendingbooking + crs.getInt("pendingbooking");
					total_cancellation = total_cancellation + crs.getInt("cancellation");
					total_testdrives = total_testdrives + crs.getInt("testdrives");
					total_kpitestdrives = total_kpitestdrives + crs.getInt("kpitestdrives");
					total_testdrives_perc = getPercentage((double) total_kpitestdrives, (double) total_enquiryfresh);
					total_homevisit = total_homevisit + crs.getInt("homevisit");
					total_kpihomevisit = total_kpihomevisit + crs.getInt("kpihomevisit");
					total_homevisit_perc = getPercentage((double) total_kpihomevisit, (double) total_enquiryfresh);
					total_mga_sales = total_mga_sales + crs.getInt("accessamt");
					total_maruti_insurance = total_maruti_insurance + crs.getInt("insurcount");
					total_extwarranty = total_extwarranty + crs.getInt("ewcount");
					total_fincases = total_fincases + crs.getInt("fincasecount");
					total_exchange = total_exchange + crs.getInt("exchangecount");
					total_exchange_perc = getPercentage((double) total_exchange, (double) total_booking);

					if (include_preowned.equals("1")) {
						total_preowned = total_preowned + crs.getInt("preownedenquiry");
						total_preowned_perc = getPercentage((double) total_preowned, (double) total_enquiryfresh);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);
					}

					// ---------------------BUILDING TEAM HEAD START--------------------------
					if (include_inactive_exe.equals("0") && dr_totalby.equals("1")) {

						if (!chk_team_header_name.equals(crs.getString("team_name"))) {
							chk_team_header_name = crs.getString("team_name");
							StrTeamHeader.append("<tr>");
							Str.append("<td valign=top align=left colspan=30>").append("<b>");
							Str.append("<a href=../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
							Str.append("&dr_branch=").append(crs.getString("branch_id"));
							Str.append(">").append(crs.getString("team_name")).append("</b></a></td>");
							StrTeamHeader.append("</tr>");

						}
						Str.append(StrTeamHeader);
						StrTeamHeader.setLength(0);

						// ---------------------BUILDING TEAM HEAD END--------------------------

						// ---------------------BUILDING TEAM FOOTER START--------------------------
						teamtotalby = "team";
						teamfiltervalue = crs.getString("team_id");

						if (chk_team_footer_name.equals("")) {
							chk_team_footer_name = crs.getString("team_name");
						}

						total_team_retailtarget = total_team_retailtarget + crs.getInt("retailtarget");
						total_team_enquirytarget = total_team_enquirytarget + crs.getInt("enquirytarget");
						total_team_enquiryopen = total_team_enquiryopen + crs.getInt("enquiryopen");
						total_team_enquiryfresh = total_team_enquiryfresh + crs.getInt("enquiryfresh");
						total_team_enquirylost = total_team_enquirylost + crs.getInt("enquirylost");
						total_team_pendingenquiry = total_team_pendingenquiry + crs.getInt("pendingenquiry");
						total_team_soretail = total_team_soretail + crs.getInt("soretail");
						total_team_soretail_perc = getPercentage((double) total_team_soretail, (double) total_team_enquiryfresh);
						total_team_sodelivered = total_team_sodelivered + crs.getInt("sodelivered");
						total_team_booking = total_team_booking + crs.getInt("booking");
						total_team_booking_perc = getPercentage((double) total_team_booking, (double) total_team_enquiryfresh);
						total_team_pendingbooking = total_team_pendingbooking + crs.getInt("pendingbooking");
						total_team_cancellation = total_team_cancellation + crs.getInt("cancellation");
						total_team_testdrives = total_team_testdrives + crs.getInt("testdrives");
						total_team_kpitestdrives = total_team_kpitestdrives + crs.getInt("kpitestdrives");
						total_team_testdrives_perc = getPercentage((double) total_team_kpitestdrives, (double) total_team_enquiryfresh);
						total_team_homevisit = total_team_homevisit + crs.getInt("homevisit");
						total_team_kpihomevisit = total_team_kpihomevisit + crs.getInt("kpihomevisit");
						total_team_homevisit_perc = getPercentage((double) total_team_kpihomevisit, (double) total_team_enquiryfresh);
						total_team_accessamt = total_team_accessamt + crs.getInt("accessamt");
						total_team_insurance = total_team_insurance + crs.getInt("insurcount");
						total_team_extwarranty = total_team_extwarranty + crs.getInt("ewcount");
						total_team_fincases = total_team_fincases + crs.getInt("fincasecount");
						total_team_exchange = total_team_exchange + crs.getInt("exchangecount");
						total_team_exchange_perc = getPercentage((double) total_team_exchange, (double) total_team_booking);

						if (include_preowned.equals("1")) {
							total_team_preowned = total_team_preowned + crs.getInt("preownedenquiry");
							total_team_preowned_perc = getPercentage((double) total_team_preowned, (double) total_team_enquiryfresh);
							total_team_evaluation = total_team_evaluation + crs.getInt("evaluation");
							total_team_evaluation_perc = getPercentage((double) total_team_evaluation, (double) total_team_enquiryfresh);
						}

						if (!crs.isLast()) {
							if (chk_team_footer_name.equals(crs.getString("team_name"))) {
								crs.next();
								chk_team_footer_name1 = crs.getString("team_name");
								crs.previous();
							}
						}

						if (!chk_team_footer_name.equals(crs.getString("team_name"))
								|| !chk_team_footer_name.equals(chk_team_footer_name1)
								|| crs.isLast()) {

							chk_team_footer_name = chk_team_footer_name1;

							// --------------------------------------------------TARGET START-----------------------------------------------------------------------
							StrTeamFooter.append("<tr>");
							StrTeamFooter.append("<td colspan=2 align=right><b> Team Total:</b>");
							StrTeamFooter.append("</td>");

							// Retail Target
							total_team_retailtarget_tilldate = (total_team_retailtarget / daycount) * day;
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(Math.round(total_team_retailtarget_tilldate) + "/" + total_team_retailtarget + "</a>");
							StrTeamFooter.append("</td>");

							// Enquiry Target
							total_team_enquirytarget_tilldate = (total_team_enquirytarget / daycount) * day;
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(Math.round(total_team_enquirytarget_tilldate) + "/" + total_team_enquirytarget + "</a>");
							StrTeamFooter.append("</td>");

							// --------------------------------------------------TARGET END-----------------------------------------------------------------------

							// --------------------------------------------------ENQUIRY STRAT-----------------------------------------------------------------------
							// enquiryopen
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=openenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_enquiryopen + "</a>").append("</td>");
							// enquiryfresh
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=freshenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_enquiryfresh + "</a>").append("</td>");
							// enquirylost
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=lostenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_enquirylost + "</a>").append("</td>");
							// pendingenquiry
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=pendingenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_pendingenquiry + "</a>").append("</td>");
							// --------------------------------------------------ENQUIRY END-----------------------------------------------------------------------

							// --------------------------------------------------SO STRAT-----------------------------------------------------------------------
							// soretail(active and retailed in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordersoretail"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_soretail + "</a>").append("</td>");

							// soretail percentage(active and retailed in between two date/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_soretail_perc + "%" + "</td>");

							// sodelivered (active and delivered in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordersodelivered"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_sodelivered + "</a>").append("</td>");

							// booking (active and so raised in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderbooking"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_booking + "</a>").append("</td>");

							// booking percentage(active and so raised in between two date/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_booking_perc + "%" + "</td>");

							// pendingbooking(active and nither retailed nor delivered nodate filter)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderpendingbooking"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_pendingbooking + "</a>").append("</td>");

							// cancellation (inactive and nither retailed nor delivered and cancel date in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordercancellation"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_cancellation + "</a>").append("</td>");

							// --------------------------------------------------SO END-----------------------------------------------------------------------

							// --------------------------------------------------FOLLOW-UP START-----------------------------------------------------------------------
							// testdrives (testdrive_fb_taken in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=testdrives"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_testdrives + "</a>").append("</td>");

							// kpitestdrives (Distinct(Unique Enquiry) testdrive_fb_taken in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=kpitestdrives"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_kpitestdrives + "</a>").append("</td>");

							// testdrives percentage(testdrive_fb_taken in between two dates/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_testdrives_perc + "%" + "</td>");

							// homevisit (homevisit done(followup_feedbacktype_id = 9) in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=enquiryhomevisit"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_homevisit + "</a>").append("</td>");

							// kpihomevisit (Distinct(Unique Enquiry) homevisit done(followup_feedbacktype_id = 9) in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=enquirykpihomevisit"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_kpihomevisit + "</a>").append("</td>");

							// homevisit percentage(homevisit done(followup_feedbacktype_id = 9) in between two dates/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_homevisit_perc + "%" + "</td>");

							// --------------------------------------------------FOLLOW-UP END-----------------------------------------------------------------------

							// --------------------------------------------------SO START-----------------------------------------------------------------------

							// accessamt (active SO AND Accessories amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderaccessamt"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_accessamt + "</a>").append("</td>");

							// insurcount (active SO AND Insurance amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderinsurcount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_insurance + "</a>").append("</td>");

							// ewcount (active SO AND Extended Warrenty amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderewcount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_extwarranty + "</a>").append("</td>");

							// fincasecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderfincasecount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_fincases + "</a>").append("</td>");

							// --------------------------------------------------SO END-----------------------------------------------------------------------

							// --------------------------has to be at last------------------------SO START-----------------------------------------------------------------------

							// exchangecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderexchangecount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
									+ total_team_exchange + "</a>").append("</td>");

							// exchange percentage( exchangecount / pendingbooking )
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_exchange_perc + "%" + "</td>");
							// StrTeamFooter.append(crs.getInt("exchangecount") / crs.getInt("booking") ;

							// --------------------------------------------------SO END-----------------------------------------------------------------------

							// --------------------------------------------------PREOWNED START-----------------------------------------------------------------------
							if (include_preowned.equals("1")) {
								// preownedenquiry (total preowned enquiry from sales side between two dates (enquiry_date filter))
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(SearchURL + "&targetvalue=enquirypreowned"
										+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
										+ total_team_preowned + "</a>").append("</td>");

								// preownedenquiry percentage( preownedenquiry / freshenquiry )
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(total_team_preowned_perc + "%" + "</td>");

								// evaluation (total preowned enquiry evaluated from sales side between two dates (enquiry_date filter))
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(SearchURL + "&targetvalue=enquiryevaluation"
										+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget=_blank >"
										+ total_team_evaluation + "</a>").append("</td>");

								// evaluation percentage( evaluation / freshenquiry )
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(total_team_evaluation_perc + "%" + "</td>");
							}

							// --------------------------------------------------PREOWNED END-----------------------------------------------------------------------

							StrTeamFooter.append("</tr>\n");

							total_team_retailtarget = 0;
							total_team_enquirytarget = 0;
							total_team_enquiryopen = 0;
							total_team_enquiryfresh = 0;
							total_team_enquirylost = 0;
							total_team_pendingenquiry = 0;
							total_team_soretail = 0;
							total_team_soretail_perc = "0";
							total_team_sodelivered = 0;
							total_team_booking = 0;
							total_team_booking_perc = "0";
							total_team_pendingbooking = 0;
							total_team_cancellation = 0;
							total_team_testdrives = 0;
							total_team_kpitestdrives = 0;
							total_team_testdrives_perc = "0";
							total_team_homevisit = 0;
							total_team_kpihomevisit = 0;
							total_team_homevisit_perc = "0";
							total_team_accessamt = 0;
							total_team_insurance = 0;
							total_team_extwarranty = 0;
							total_team_fincases = 0;
							total_team_exchange = 0;
							total_team_exchange_perc = "0";
							if (include_preowned.equals("1")) {
								total_team_preowned = 0;
								total_team_preowned_perc = "0";
								total_team_evaluation = 0;
								total_team_evaluation_perc = "0";
							}

						}
					}

					// ---------------------BUILDING TEAM FOOTER END--------------------------

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>");
					switch (dr_totalby) {
						case "1" :
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">")
									.append(crs.getString("emp_name")).append("</a>");
							if (crs.getString("emp_active").equals("0")) {
								Str.append("</br><font color=red > [Inactive] </font>");
							}
							break;

						case "2" :
							Str.append("<a href=../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
							Str.append("&dr_branch=").append(crs.getString("branch_id"));
							Str.append(" target=_blank>").append(crs.getString("team_name")).append("</b></a>");
							break;

						case "3" :
							Str.append("<a href=../portal/branch-list.jsp?branch_id=").append(crs.getString("branch_id"));
							Str.append(" target=_blank>").append(crs.getString("branch_name")).append("</b></a>");
							break;

						case "4" :
							Str.append("<a href=../portal/region-list.jsp?region_id=").append(crs.getString("region_id"));
							Str.append(" target=_blank>").append(crs.getString("region_name")).append("</b></a>");
							break;

						case "5" :
							Str.append(crs.getString("brand_name"));
							break;
					}

					Str.append("</td>");

					// --------------------------------------------------TARGET START-----------------------------------------------------------------------
					// Retail Target
					total_retailtarget_tilldate = (crs.getInt("retailtarget") / daycount) * day;

					Str.append("<td valign=top align=right>");
					if (dr_totalby.equals("1")) {
						Str.append(SearchURL + "&targetvalue=target"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
								+ Math.round(total_retailtarget_tilldate) + "/" + crs.getInt("retailtarget") + "</a>");
					} else {
						Str.append(crs.getInt("retailtarget") + "</a>");
					}
					Str.append("</td>");

					// Enquiry Target
					total_enquirytarget_tilldate = (crs.getInt("enquirytarget") / daycount) * day;
					Str.append("<td valign=top align=right>");
					if (dr_totalby.equals("1")) {
						Str.append(SearchURL + "&targetvalue=target"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
								+ +Math.round(total_enquirytarget_tilldate) + "/" + crs.getInt("enquirytarget") + "</a>");
					} else {
						Str.append(crs.getInt("enquirytarget") + "</a>");
					}
					Str.append("</td>");

					// --------------------------------------------------TARGET END-----------------------------------------------------------------------

					// --------------------------------------------------ENQUIRY STRAT-----------------------------------------------------------------------
					// enquiryopen
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=openenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("enquiryopen") + "</a>").append("</td>");
					// enquiryfresh
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=freshenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("enquiryfresh") + "</a>").append("</td>");
					// enquirylost
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=lostenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("enquirylost") + "</a>").append("</td>");
					// pendingenquiry
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=pendingenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("pendingenquiry") + "</a>").append("</td>");
					// --------------------------------------------------ENQUIRY END-----------------------------------------------------------------------

					// --------------------------------------------------SO STRAT-----------------------------------------------------------------------
					// soretail(active and retailed in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordersoretail"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("soretail") + "</a>").append("</td>");

					// soretail percentage(active and retailed in between two date/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("soretail"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// sodelivered (active and delivered in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordersodelivered"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("sodelivered") + "</a>").append("</td>");

					// booking (active and so raised in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderbooking"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("booking") + "</a>").append("</td>");

					// booking percentage(active and so raised in between two date/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("booking"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// pendingbooking(active and nither retailed nor delivered nodate filter)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderpendingbooking"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("pendingbooking") + "</a>").append("</td>");

					// cancellation (inactive and nither retailed nor delivered and cancel date in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordercancellation"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("cancellation") + "</a>").append("</td>");

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// --------------------------------------------------FOLLOW-UP START-----------------------------------------------------------------------
					// testdrives (testdrive_fb_taken in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=testdrives"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("testdrives") + "</a>").append("</td>");

					// kpitestdrives (Distinct(Unique Enquiry) testdrive_fb_taken in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=kpitestdrives"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("kpitestdrives") + "</a>").append("</td>");

					// testdrives percentage(testdrive_fb_taken in between two dates/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("testdrives"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// homevisit (homevisit done(followup_feedbacktype_id = 9) in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=enquiryhomevisit"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("homevisit") + "</a>").append("</td>");

					// kpihomevisit (Distinct(Unique Enquiry) homevisit done(followup_feedbacktype_id = 9) in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=enquirykpihomevisit"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("kpihomevisit") + "</a>").append("</td>");

					// homevisit percentage(homevisit done(followup_feedbacktype_id = 9) in between two dates/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("homevisit"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// --------------------------------------------------FOLLOW-UP END-----------------------------------------------------------------------

					// --------------------------------------------------SO START-----------------------------------------------------------------------

					// accessamt (active SO AND Accessories amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderaccessamt"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("accessamt") + "</a>").append("</td>");

					// insurcount (active SO AND Insurance amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderinsurcount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("insurcount") + "</a>").append("</td>");

					// ewcount (active SO AND Extended Warrenty amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderewcount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("ewcount") + "</a>").append("</td>");

					// fincasecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderfincasecount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("fincasecount") + "</a>").append("</td>");

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// --------------------------has to be at last------------------------SO START-----------------------------------------------------------------------

					// exchangecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderexchangecount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
							+ crs.getInt("exchangecount") + "</a>").append("</td>");

					// exchange percentage( exchangecount / pendingbooking )
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("exchangecount"), (double) crs.getInt("booking")) + "%" + "</td>");
					// Str.append(crs.getInt("exchangecount") / crs.getInt("booking") ;

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// --------------------------------------------------PREOWNED START-----------------------------------------------------------------------
					if (include_preowned.equals("1")) {
						// preownedenquiry (total preowned enquiry from sales side between two dates (enquiry_date filter))
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&targetvalue=enquirypreowned"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
								+ crs.getInt("preownedenquiry") + "</a>").append("</td>");

						// preownedenquiry percentage( preownedenquiry / freshenquiry )
						Str.append("<td valign=top align=right>");
						Str.append(getPercentage((double) crs.getInt("preownedenquiry"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

						// evaluation (total preowned enquiry evaluated from sales side between two dates (enquiry_date filter))
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&targetvalue=enquiryevaluation"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget=_blank >"
								+ crs.getInt("evaluation") + "</a>").append("</td>");

						// evaluation percentage( evaluation / freshenquiry )
						Str.append("<td valign=top align=right>");
						Str.append(getPercentage((double) crs.getInt("evaluation"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");
					}

					// --------------------------------------------------PREOWNED END-----------------------------------------------------------------------

					Str.append("</tr>\n");

					// appending team total dats
					if (include_inactive_exe.equals("0")
							&& dr_totalby.equals("1")
							&& (!chk_team_footer_name.equals(crs.getString("team_name"))
							|| crs.isLast())) {

						Str.append(StrTeamFooter);
						StrTeamFooter.setLength(0);

					}

				}
				// ------------------------------Grand Total Start--------------------------------------
				Str.append("<tr>\n");
				Str.append("<td valign=top align=left colspan=2><b> Total: </b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(total_retailtarget + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(total_enquirytarget + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=openenquiry"
						+ " traget=_blank >" + total_enquiryopen + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=freshenquiry"
						+ " traget=_blank >" + total_enquiryfresh + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=lostenquiry"
						+ " traget=_blank >" + total_enquirylost + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=pendingenquiry"
						+ " traget=_blank >" + total_pendingenquiry + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesordersoretail"
						+ " traget=_blank >" + total_soretail + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_soretail_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesordersodelivered"
						+ " traget=_blank >" + total_sodelivered + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderbooking"
						+ " traget=_blank >" + total_booking + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_booking_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderpendingbooking"
						+ " traget=_blank >" + total_pendingbooking + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesordercancellation"
						+ " traget=_blank >" + total_cancellation + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=testdrives"
						+ " traget=_blank >" + total_testdrives + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=kpitestdrives"
						+ " traget=_blank >" + total_kpitestdrives + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_testdrives_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=enquiryhomevisit"
						+ " traget=_blank >" + total_homevisit + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=enquirykpihomevisit"
						+ " traget=_blank >" + total_kpihomevisit + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_homevisit_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderaccessamt"
						+ " traget=_blank >" + total_mga_sales + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderinsurcount"
						+ " traget=_blank >" + total_maruti_insurance + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderewcount"
						+ " traget=_blank >" + total_extwarranty + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderfincasecount"
						+ " traget=_blank >" + total_fincases + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&targetvalue=salesorderexchangecount"
						+ " traget=_blank >" + total_exchange + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_exchange_perc)) + "%" + "</b></td>");

				if (include_preowned.equals("1")) {
					Str.append("<td valign=top align=right><b>");
					Str.append(SearchURL + "&targetvalue=preownedenquiry"
							+ " traget=_blank >" + total_preowned + "</b></a>");
					Str.append("</td>");

					Str.append("<td valign=top align=right><b>");
					Str.append(total_preowned_perc + "</b></a>");
					Str.append("</td>");

					Str.append("<td valign=top align=right><b>");
					Str.append(SearchURL + "&targetvalue=preownedevaluation"
							+ " traget=_blank >" + total_evaluation + "</b></a>");
					Str.append("</td>");

					Str.append("<td valign=top align=right><b>");
					Str.append(deci.format(Double.parseDouble(total_evaluation_perc)) + "%" + "</b></td>");

				}

				Str.append("<tr>\n");
				// ------------------------------GRAND TOTAL END--------------------------------------
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
	// MODEL DASH QUERY

	// Pre owned MODEL DASH QUERY

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSoe() {
		String sb = "";
		try
		{
			StrSql = " SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " ORDER BY soe_name ";
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
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		return Str.toString();
	}

	public String PopulateOrderBy(String comp_id, String include_preowned) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_orderby\" class=\"form-control\" id=\"dr_orderby\">");
		Str.append("<option value=''>Select</option>");
		Str.append("<option value=retailtarget").append(StrSelectdrop("retailtarget", dr_orderby)).append(">Retail Target</option>\n");
		Str.append("<option value=enquirytarget").append(StrSelectdrop("enquirytarget", dr_orderby)).append(">Enquiry Target</option>\n");
		Str.append("<option value=enquiryopen").append(StrSelectdrop("enquiryopen", dr_orderby)).append(">Open Enquiry</option>\n");
		Str.append("<option value=enquiryfresh").append(StrSelectdrop("enquiryfresh", dr_orderby)).append(">Fresh Enquiry</option>\n");
		Str.append("<option value=enquirylost").append(StrSelectdrop("enquirylost", dr_orderby)).append(">Lost Enquiry</option>\n");
		Str.append("<option value=pendingenquiry").append(StrSelectdrop("pendingenquiry", dr_orderby)).append(">Pending Enquiry</option>\n");
		Str.append("<option value=soretail").append(StrSelectdrop("soretail", dr_orderby)).append(">Retail</option>\n");
		Str.append("<option value=sodelivered").append(StrSelectdrop("sodelivered", dr_orderby)).append(">SO Delivered</option>\n");
		Str.append("<option value=booking").append(StrSelectdrop("booking", dr_orderby)).append(">Booking</option>\n");
		Str.append("<option value=pendingbooking").append(StrSelectdrop("pendingbooking", dr_orderby)).append(">Pending Booking</option>\n");
		Str.append("<option value=cancellation").append(StrSelectdrop("cancellation", dr_orderby)).append(">Cancellation</option>\n");
		Str.append("<option value=testdrives").append(StrSelectdrop("testdrives", dr_orderby)).append(">Test Drives</option>\n");
		Str.append("<option value=kpitestdrives").append(StrSelectdrop("kpitestdrives", dr_orderby)).append(">KPI Test Drives</option>\n");
		Str.append("<option value=homevisit").append(StrSelectdrop("homevisit", dr_orderby)).append(">Home Visit</option>\n");
		Str.append("<option value=kpihomevisit").append(StrSelectdrop("kpihomevisit", dr_orderby)).append(">KPI Home Visit</option>\n");
		Str.append("<option value=accessamt").append(StrSelectdrop("accessamt", dr_orderby)).append(">Access</option>\n");
		Str.append("<option value=insurcount").append(StrSelectdrop("insurcount", dr_orderby)).append(">Insurance</option>\n");
		Str.append("<option value=ewcount").append(StrSelectdrop("ewcount", dr_orderby)).append(">Extended Warranty</option>\n");
		Str.append("<option value=fincasecount").append(StrSelectdrop("fincasecount", dr_orderby)).append(">FIN Cases</option>\n");
		if (include_preowned.equals("1")) {
			Str.append("<option value=exchangecount").append(StrSelectdrop("exchangecount", dr_orderby)).append(">Exchange</option>\n");
			Str.append("<option value=evaluation").append(StrSelectdrop("evaluation", dr_orderby)).append(">Evaluation</option>\n");
		}
		Str.append("</select>");
		return Str.toString();
	}
	//

}
