package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Monitoring_Board extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids, sob_ids;
	public String preowned_model = "", include_inactive_exe = "", include_preowned = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "", sob_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "", StrSob = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
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
					// SOP("StrSearch===in mb=" + StrSearch);
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
					if (!sob_id.equals("")) {
						StrSob = " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						if (preowned_model.equals("1")) {
							StrHTML = ListPreownedModelMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						} else if (dr_totalby.equals("6")) {
							StrHTML = ListModelMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						} else {
							StrHTML = ListMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						}
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
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");

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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListMonitorBoard(String dr_branch_id, String starttime, String endtime, String targetstarttime, String targetendtime, String dr_totalby, String comp_id) {

		SearchURL = "<a href=../sales/report-monitoring-board-search-temp.jsp?filter=yes&";

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

		if ((dr_totalby.equals("2") || !team_id.equals("")) || include_inactive_exe.equals("0")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe dupteamexe ON dupteamexe.teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team dupteam ON dupteam.team_id = dupteamexe.teamtrans_team_id"
					+ " AND dupteam.team_active =1 "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		} else if ((!branch_id.equals("") || !brand_id.equals("") || !region_id.equals("")) || (dr_totalby.equals("3") || dr_totalby.equals("4") || dr_totalby.equals("5"))) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		} else if (!BranchAccess.equals("")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		}

		if (include_inactive_exe.equals("0")) {
			empactivefilter += " AND emp_active = 1";
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
				tblTargetGroupby = " team_id";
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
				tblEnqGroupby = " emp_branch_id";
				tblEnqFollowupGroupby = " emp_branch_id";
				tblSoGroupby = " emp_branch_id";
				tblTestDriveGroupby = " emp_branch_id";
				tblPreOnwedGroupby = " emp_branch_id";
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

		}
		// QUERY START

		StrSql = " SELECT  COALESCE (mainemp.emp_id,'') AS emp_id, COALESCE (mainemp.emp_name,'') AS emp_name,"
				+ " COALESCE(mainemp.emp_ref_no,'') AS emp_ref_no, mainemp.emp_active,";
		if ((dr_totalby.equals("2") || !team_id.equals("")) || include_inactive_exe.equals("0")) {
			StrSql += "  COALESCE(mainteam.team_id,'') AS team_id, COALESCE (mainteam.team_name,'') AS team_name,";
		}
		StrSql += " COALESCE(mainbranch.branch_id,'') AS branch_id, COALESCE(mainbranch.branch_name,'') AS branch_name,"
				+ " COALESCE(mainregion.region_id,'') AS region_id, COALESCE(mainregion.region_name,'') AS region_name, "
				+ " COALESCE(mainbrand.brand_id,'') AS brand_id, COALESCE(mainbrand.brand_name,'') AS brand_name, ";

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
		StrSql += " COALESCE (tbltestdrives.testdrives, 0) AS testdrives,"
				+ " COALESCE (tbltestdrives.kpitestdrives, 0) AS kpitestdrives,"
				+ " COALESCE (tblhomevisit.homevisit, 0) AS homevisit,"
				+ " COALESCE (tblhomevisit.kpihomevisit, 0) AS kpihomevisit ";
		if (include_preowned.equals("1")) {
			StrSql += ", COALESCE (tblpreownedenquiry.preownedenquiry,	0) AS preownedenquiry";
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

		// target join
		StrSql += " LEFT JOIN ( SELECT SUM(modeltarget_so_count) AS retailtarget,"
				+ " SUM(modeltarget_enquiry_count) AS enquirytarget,"
				+ tblTargetGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp targetemp ON targetemp.emp_id = target_emp_id";

		if (dr_totalby.equals("2") || !team_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe targetteamexe ON targetteamexe.teamtrans_emp_id = targetemp.emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team targetteam ON targetteam.team_id = targetteamexe.teamtrans_team_id"
					+ " AND team_active = 1";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch targetbranch ON targetbranch.branch_id = targetemp.emp_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region targetregion ON targetregion.region_id = targetbranch.branch_region_id"
				+ " INNER JOIN axelaauto.axela_brand targetbrand ON targetbrand.brand_id = targetbranch.branch_brand_id";

		StrSql += " WHERE 1=1"
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (include_inactive_exe.equals("0")) {
			StrSql += " AND targetemp.emp_active = 1";
		}
		StrSql += " AND substring(target_startdate, 1, 6) >= substr('" + starttime + "', 1, 6)"
				+ " AND substring(target_enddate, 1, 6) <= substr('" + endtime + "', 1, 6)"
				+ " GROUP BY " + tblTargetGroupby + " ) AS tbltarget ON tbltarget." + tblTargetGroupby + " = " + tblFilterJoin;

		// enquiry join
		StrSql += " LEFT JOIN (SELECT"
				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
				+ " AND enquiry_status_id = 1, 1, 0)) AS enquiryopen,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) , 1, 0)) AS enquiryfresh,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND (enquiry_status_id = 3 OR enquiry_status_id = 4	), 1, 0)) AS enquirylost,"
				+ " SUM(IF(enquiry_status_id = 1, 1, 0)) AS pendingenquiry,";

		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND enquiry_evaluation = 1 , 1, 0)) AS evaluation,";
		}
		StrSql += " " + tblEnqGroupby + " FROM " + compdb(comp_id) + "axela_sales_enquiry";

		StrSql += tbljoin
				+ empactivefilter
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "enquiry_emp_id")
				// join for filter
				+ StrSoe + StrSob + StrModel.replace("model_id", "enquiry_model_id")
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
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " ,1,0)) AS exchangecount,";

		StrSql += " " + tblSoGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		StrSql += tbljoin.replace("enquiry_emp_id", "so_emp_id")
				.replace("enquiry_branch_id", "so_branch_id")
				.replace("emp_branch_id", "so_branch_id")
				+ empactivefilter.replace("enquiry_emp_id", "so_emp_id")
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "so_emp_id")
				+ StrSoe + StrSob + StrModel.replace("model_id", "item_model_id")
				+ " GROUP BY " + tblSoGroupby + " ) AS tblso ON tblso." + tblSoGroupby + " = " + tblFilterJoin;

		// / testdrive join
		StrSql += " LEFT JOIN (SELECT "
				+ " SUM(IF(testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), 1, 0)) AS testdrives,"
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) THEN enquiry_id END) AS  kpitestdrives,"
				+ " " + tblTestDriveGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ tbljoin
				+ empactivefilter
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "enquiry_emp_id")
				+ StrSoe + StrSob + StrModel.replace("model_id", "enquiry_model_id")
				+ " GROUP BY " + tblTestDriveGroupby + " ) AS tbltestdrives ON tbltestdrives." + tblTestDriveGroupby + " = " + tblFilterJoin;

		// followup join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(followup_feedbacktype_id = 9 , 1, 0)) AS homevisit,"
				+ " COUNT(DISTINCT CASE WHEN followup_feedbacktype_id = 9 THEN enquiry_id END) AS  kpihomevisit,"
				+ " " + tblEnqFollowupGroupby
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
				+ tbljoin
				+ empactivefilter
				+ " WHERE 1 = 1"
				+ StrSearch.replace("emp_id", "enquiry_emp_id")
				+ StrSoe + StrSob + StrModel.replace("model_id", "enquiry_model_id")
				+ " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
				+ " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
				+ " GROUP BY " + tblEnqFollowupGroupby + " ) AS tblhomevisit ON tblhomevisit." + tblEnqFollowupGroupby + " = " + tblFilterJoin;

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
					+ StrSearch.replace("emp_id", "preowned_sales_emp_id")
					+ " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND preowned_sales_emp_id != 0"
					+ " GROUP BY " + tblPreOnwedGroupby + " ) AS tblpreownedenquiry ON tblpreownedenquiry." + tblPreOnwedGroupby + " = " + tblFilterJoin;
		}

		StrSql += " WHERE emp_sales = '1' ";

		if ((dr_totalby.equals("1") || dr_totalby.equals("2")) && include_inactive_exe.equals("0")) {
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
				+ " ";

		if (dr_totalby.equals("1") && dr_orderby.equals("") && include_inactive_exe.equals("0")) {
			StrSql += " ORDER BY team_name";
		} else if (dr_totalby.equals("1") && !dr_orderby.equals("")) {
			StrSql += " ORDER BY " + dr_orderby + " DESC";
		} else if (!dr_totalby.equals("1") && !dr_orderby.equals("")) {
			StrSql += " ORDER BY " + dr_orderby + " DESC";
		} else {
			StrSql += " ORDER BY " + mainOrderBy + "";
		}

		// if (emp_id.equals("1") && comp_id.equals("1011")) {
		// SOPInfo("StrSql==123==sales===mb====" + StrSql);
		// }

		// QUERY END

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				SearchURL += "brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&branch_id=" + branch_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&include_inactive_exe=" + include_inactive_exe
						+ "&soe_id=" + soe_id
						+ "&sob_id=" + sob_id
						+ "&model_id=" + model_id
						+ "&starttime=" + starttime
						+ "&endtime=" + endtime;

				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide='phone'>#</th>\n");

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

				StrHead.append("<th title='Retail Target'>RT</th>\n");
				StrHead.append("<th data-hide='phone' title='Enquiry Target'>ET</th>\n");
				StrHead.append("<th data-hide='phone'>Open Enquiry</th>\n");
				StrHead.append("<th data-hide='phone'>Fresh Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Lost Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Pending Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Retail</th>\n"); // soretail
				StrHead.append("<th data-hide='phone, tablet'>Retail %</th>\n"); // soretail_perc
				StrHead.append("<th data-hide='phone, tablet' title='Delivered'>Del</th>\n"); // sodelivered
				StrHead.append("<th data-hide='phone, tablet'>Booking</th>\n"); // booking
				StrHead.append("<th data-hide='phone, tablet'>Booking %</th>\n"); // pendingbooking_perc
				StrHead.append("<th data-hide='phone, tablet'>Pending Booking</th>\n"); // pendingbooking
				StrHead.append("<th data-hide='phone, tablet' title='Cancellation'>Cal</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD%</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit %</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Accessories'>Access</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Insurance'>Ins</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Extended Warranty'>EW</th>\n");
				StrHead.append("<th data-hide='phone, tablet' >Fin Cases</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe </th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe %</th>\n");
				if (include_preowned.equals("1")) {
					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry'>PE </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry %'>PE %</th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval %</th>\n");
				}

				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Str.append("<div class='table-bordered'>\n");
				Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
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
					if (include_inactive_exe.equals("0") && dr_totalby.equals("1") && dr_orderby.equals("")) {

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
							StrTeamFooter.append("<td colspan=2 align=right><b> Team Total:</b></td>");

							// Retail Target
							total_team_retailtarget_tilldate = (total_team_retailtarget / daycount) * day;
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(Math.round(total_team_retailtarget_tilldate) + "/" + total_team_retailtarget);
							StrTeamFooter.append("</td>");

							// Enquiry Target
							total_team_enquirytarget_tilldate = (total_team_enquirytarget / daycount) * day;
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(Math.round(total_team_enquirytarget_tilldate) + "/" + total_team_enquirytarget);
							StrTeamFooter.append("</td>");

							// --------------------------------------------------TARGET END-----------------------------------------------------------------------

							// --------------------------------------------------ENQUIRY STRAT-----------------------------------------------------------------------
							// enquiryopen
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=openenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_enquiryopen + "</a>").append("</td>");
							// enquiryfresh
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=freshenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_enquiryfresh + "</a>").append("</td>");
							// enquirylost
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=lostenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_enquirylost + "</a>").append("</td>");
							// pendingenquiry
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=pendingenquiry"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_pendingenquiry + "</a>").append("</td>");
							// --------------------------------------------------ENQUIRY END-----------------------------------------------------------------------

							// --------------------------------------------------SO STRAT-----------------------------------------------------------------------
							// soretail(active and retailed in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordersoretail"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_soretail + "</a>").append("</td>");

							// soretail percentage(active and retailed in between two date/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_soretail_perc + "%" + "</td>");

							// sodelivered (active and delivered in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordersodelivered"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_sodelivered + "</a>").append("</td>");

							// booking (active and so raised in between two date)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderbooking"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_booking + "</a>").append("</td>");

							// booking percentage(active and so raised in between two date/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_booking_perc + "%" + "</td>");

							// pendingbooking(active and nither retailed nor delivered nodate filter)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderpendingbooking"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_pendingbooking + "</a>").append("</td>");

							// cancellation (inactive and nither retailed nor delivered and cancel date in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesordercancellation"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_cancellation + "</a>").append("</td>");

							// --------------------------------------------------SO END-----------------------------------------------------------------------

							// --------------------------------------------------FOLLOW-UP START-----------------------------------------------------------------------
							// testdrives (testdrive_fb_taken in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=testdrives"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_testdrives + "</a>").append("</td>");

							// kpitestdrives (Distinct(Unique Enquiry) testdrive_fb_taken in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=kpitestdrives"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_kpitestdrives + "</a>").append("</td>");

							// testdrives percentage(testdrive_fb_taken in between two dates/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_testdrives_perc + "%" + "</td>");

							// homevisit (homevisit done(followup_feedbacktype_id = 9) in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=enquiryhomevisit"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_homevisit + "</a>").append("</td>");

							// kpihomevisit (Distinct(Unique Enquiry) homevisit done(followup_feedbacktype_id = 9) in between two dates)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=enquirykpihomevisit"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_kpihomevisit + "</a>").append("</td>");

							// homevisit percentage(homevisit done(followup_feedbacktype_id = 9) in between two dates/enquiry fresh)
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(total_team_homevisit_perc + "%" + "</td>");

							// --------------------------------------------------FOLLOW-UP END-----------------------------------------------------------------------

							// --------------------------------------------------SO START-----------------------------------------------------------------------

							// accessamt (active SO AND Accessories amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderaccessamt"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_accessamt + "</a>").append("</td>");

							// insurcount (active SO AND Insurance amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderinsurcount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_insurance + "</a>").append("</td>");

							// ewcount (active SO AND Extended Warrenty amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderewcount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_extwarranty + "</a>").append("</td>");

							// fincasecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderfincasecount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
									+ total_team_fincases + "</a>").append("</td>");

							// --------------------------------------------------SO END-----------------------------------------------------------------------

							// --------------------------has to be at last------------------------SO START-----------------------------------------------------------------------

							// exchangecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
							StrTeamFooter.append("<td valign=top align=right>");
							StrTeamFooter.append(SearchURL + "&targetvalue=salesorderexchangecount"
									+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
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
										+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
										+ total_team_preowned + "</a>").append("</td>");

								// preownedenquiry percentage( preownedenquiry / freshenquiry )
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(total_team_preowned_perc + "%" + "</td>");

								// evaluation (total preowned enquiry evaluated from sales side between two dates (enquiry_date filter))
								StrTeamFooter.append("<td valign=top align=right>");
								StrTeamFooter.append(SearchURL + "&targetvalue=enquiryevaluation"
										+ "&total_by=" + teamtotalby + "&value=" + teamfiltervalue + " traget='_blank' >"
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
							Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), crs.getString("emp_ref_no")));
							if (crs.getString("emp_active").equals("0")) {
								Str.append("</br><font color=red > [Inactive] </font>");
							}
							break;

						case "2" :
							Str.append("<a href=../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
							Str.append("&dr_branch=").append(crs.getString("branch_id"));
							Str.append("' target='_blank'><b>").append(crs.getString("team_name")).append("</b></a>");
							break;

						case "3" :
							Str.append("<a href=../portal/branch-list.jsp?branch_id=").append(crs.getString("branch_id"));
							Str.append("' target='_blank'><b>").append(crs.getString("branch_name")).append("</b></a>");
							break;

						case "4" :
							Str.append("<a href=../portal/region-list.jsp?region_id=").append(crs.getString("region_id"));
							Str.append("' target='_blank'><b>").append(crs.getString("region_name")).append("</b></a>");
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
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
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
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
								+ +Math.round(total_enquirytarget_tilldate) + "/" + crs.getInt("enquirytarget") + "</a>");
					} else {
						Str.append(crs.getInt("enquirytarget"));
					}
					Str.append("</td>");

					// --------------------------------------------------TARGET END-----------------------------------------------------------------------

					// --------------------------------------------------ENQUIRY STRAT-----------------------------------------------------------------------
					// enquiryopen
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=openenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("enquiryopen") + "</a>").append("</td>");
					// enquiryfresh
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=freshenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("enquiryfresh") + "</a>").append("</td>");
					// enquirylost
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=lostenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("enquirylost") + "</a>").append("</td>");
					// pendingenquiry
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=pendingenquiry"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("pendingenquiry") + "</a>").append("</td>");
					// --------------------------------------------------ENQUIRY END-----------------------------------------------------------------------

					// --------------------------------------------------SO STRAT-----------------------------------------------------------------------
					// soretail(active and retailed in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordersoretail"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("soretail") + "</a>").append("</td>");

					// soretail percentage(active and retailed in between two date/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("soretail"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// sodelivered (active and delivered in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordersodelivered"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("sodelivered") + "</a>").append("</td>");

					// booking (active and so raised in between two date)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderbooking"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("booking") + "</a>").append("</td>");

					// booking percentage(active and so raised in between two date/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("booking"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// pendingbooking(active and nither retailed nor delivered nodate filter)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderpendingbooking"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("pendingbooking") + "</a>").append("</td>");

					// cancellation (inactive and nither retailed nor delivered and cancel date in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesordercancellation"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("cancellation") + "</a>").append("</td>");

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// --------------------------------------------------FOLLOW-UP START-----------------------------------------------------------------------
					// testdrives (testdrive_fb_taken in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=testdrives"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("testdrives") + "</a>").append("</td>");

					// kpitestdrives (Distinct(Unique Enquiry) testdrive_fb_taken in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=kpitestdrives"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("kpitestdrives") + "</a>").append("</td>");

					// testdrives percentage(testdrive_fb_taken in between two dates/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("testdrives"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// homevisit (homevisit done(followup_feedbacktype_id = 9) in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=enquiryhomevisit"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("homevisit") + "</a>").append("</td>");

					// kpihomevisit (Distinct(Unique Enquiry) homevisit done(followup_feedbacktype_id = 9) in between two dates)
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=enquirykpihomevisit"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("kpihomevisit") + "</a>").append("</td>");

					// homevisit percentage(homevisit done(followup_feedbacktype_id = 9) in between two dates/enquiry fresh)
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("homevisit"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

					// --------------------------------------------------FOLLOW-UP END-----------------------------------------------------------------------

					// --------------------------------------------------SO START-----------------------------------------------------------------------

					// accessamt (active SO AND Accessories amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderaccessamt"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("accessamt") + "</a>").append("</td>");

					// insurcount (active SO AND Insurance amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderinsurcount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("insurcount") + "</a>").append("</td>");

					// ewcount (active SO AND Extended Warrenty amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderewcount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("ewcount") + "</a>").append("</td>");

					// fincasecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderfincasecount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("fincasecount") + "</a>").append("</td>");

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// --------------------------has to be at last------------------------SO START-----------------------------------------------------------------------

					// exchangecount (active SO AND Finance Cases amount collected for SO in between two dates(so_retail_date filter))
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&targetvalue=salesorderexchangecount"
							+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
							+ crs.getInt("exchangecount") + "</a>").append("</td>");

					// exchange percentage( exchangecount / pendingbooking )
					Str.append("<td valign=top align=right>");
					Str.append(getPercentage((double) crs.getInt("exchangecount"), (double) crs.getInt("booking")) + "%" + "</td>");
					// Str.append(crs.getInt("exchangecount") / crs.getInt("booking") ;

					// --------------------------------------------------SO END-----------------------------------------------------------------------

					// / --------------------------------------------------PREOWNED START-----------------------------------------------------------------------
					if (include_preowned.equals("1")) {
						// preownedenquiry (total preowned enquiry from sales side between two dates (enquiry_date filter))
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&targetvalue=enquirypreowned"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
								+ crs.getInt("preownedenquiry") + "</a>").append("</td>");

						// preownedenquiry percentage( preownedenquiry / freshenquiry )
						Str.append("<td valign=top align=right>");
						Str.append(getPercentage((double) crs.getInt("preownedenquiry"), (double) crs.getInt("enquiryfresh")) + "%" + "</td>");

						// evaluation (total preowned enquiry evaluated from sales side between two dates (enquiry_date filter))
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&targetvalue=enquiryevaluation"
								+ "&total_by=" + totalby + "&value=" + filtervalue + " traget='_blank' >"
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
				// -----------------------------GRAND TOTAL START-----------------------------
				Str.append("<tr>\n");
				Str.append("<td valign=top align=left colspan=2><b> Total: </b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(total_retailtarget + "</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(total_enquirytarget + "</b></td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=openenquiry"
						+ " traget='_blank' ><b>" + total_enquiryopen + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=freshenquiry"
						+ " traget='_blank' ><b>" + total_enquiryfresh + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=lostenquiry"
						+ " traget='_blank' ><b>" + total_enquirylost + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=pendingenquiry"
						+ " traget='_blank' ><b>" + total_pendingenquiry + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesordersoretail"
						+ " traget='_blank' ><b>" + total_soretail + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_soretail_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesordersodelivered"
						+ " traget='_blank' ><b>" + total_sodelivered + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderbooking"
						+ " traget='_blank' ><b>" + total_booking + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_booking_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderpendingbooking"
						+ " traget='_blank' ><b>" + total_pendingbooking + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesordercancellation"
						+ " traget='_blank' ><b>" + total_cancellation + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=testdrives"
						+ " traget='_blank' ><b>" + total_testdrives + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=kpitestdrives"
						+ " traget='_blank' ><b>" + total_kpitestdrives + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_testdrives_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=enquiryhomevisit"
						+ " traget='_blank' ><b>" + total_homevisit + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=enquirykpihomevisit"
						+ " traget='_blank' ><b>" + total_kpihomevisit + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_homevisit_perc)) + "%" + "</b></td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderaccessamt"
						+ " traget='_blank' ><b>" + total_mga_sales + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderinsurcount"
						+ " traget='_blank' ><b>" + total_maruti_insurance + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderewcount"
						+ " traget='_blank' ><b>" + total_extwarranty + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderfincasecount"
						+ " traget='_blank' ><b>" + total_fincases + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right>");
				Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=salesorderexchangecount"
						+ " traget='_blank' ><b>" + total_exchange + "</b></a>");
				Str.append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(deci.format(Double.parseDouble(total_exchange_perc)) + "%" + "</b></td>");

				if (include_preowned.equals("1")) {
					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=enquirypreowned"
							+ " traget='_blank' ><b>" + total_preowned + "</b></a>");
					Str.append("</td>");

					Str.append("<td valign=top align=right><b>");
					Str.append(total_preowned_perc + "</b>");
					Str.append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(SearchURL + "&total_by=" + totalby + "&targetvalue=enquiryevaluation"
							+ " traget='_blank' ><b>" + total_evaluation + "</b></a>");
					Str.append("</td>");

					Str.append("<td valign=top align=right><b>");
					Str.append(deci.format(Double.parseDouble(total_evaluation_perc)) + "%" + "</b></td>");

				}

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
	// MODEL DASH QUERY
	public String ListModelMonitorBoard(String dr_branch_id, String starttime, String endtime, String targetstarttime, String targetendtime, String dr_totalby, String comp_id) {
		String Strteamjoin = "", Strteam = "", Stremp = "";
		String BranchJoin = "";
		StrSearch = "";
		String Strbrand = "";
		if (!brand_id.equals("")) {
			Strbrand = " AND model_brand_id IN (" + brand_id + ") ";
		}
		if (!region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + region_id + ") ";
		}
		if (!branch_id.equals("")) {
			StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
		}
		if (!brand_id.equals("") || !branch_id.equals("") || !region_id.equals("")) {
			BranchJoin = " INNER JOIN " + compdb(comp_id) + "axela_branch branch ON branch_id = enquiry_branch_id";
		}

		if (!model_id.equals("")) {
			StrModel = " AND model_id IN (" + model_id + ")";
		}
		if (!soe_id.equals("")) {
			StrSoe = " AND enquiry_soe_id IN (" + soe_id + ")";
		}
		if (!sob_id.equals("")) {
			StrSob = " AND enquiry_soe_id IN (" + sob_id + ")";
		}
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			Strteamjoin = " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id";
		}
		if (!team_id.equals("")) {
			Strteam = " AND teamtrans_team_id IN (" + team_id + ")"
					+ " AND team_active = 1";
		}
		if (!exe_id.equals("")) {
			Stremp = " AND emp_id IN (" + exe_id + ")";
		}
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
		int total_evaluation = 0, total_preownedenquiry = 0;
		String total_evaluation_perc = "0";
		String total_preownedenquiry_perc = "0";
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
		int model_preownedenquiry = 0;
		String model_preownedenquiry_perc = "0";

		String model_evaluation_perc = "0";
		String check_model_id = "", check_modelttl_id = "";

		String check_principalttl_id = "";
		String check_regionttl_id = "";

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();

		StrSql = "SELECT model_id, brand_id, branch_region_id,"
				// + " ,region_id,"
				+ " branch_id, "
				+ " brand_name,"
				// + ", region_name,"
				+ " branch_name, model_name,";

		StrSql += " COALESCE (  tbltarget.enquirytarget,       0) AS enquirytarget,"
				+ " COALESCE (  tbltarget.retailtarget,		   0) AS retailtarget,"
				+ " COALESCE (  tblenquiry.enquiryopen,	   0) AS enquiryopen,"
				+ " COALESCE (	tblenquiry.enquiryfresh,   0) AS enquiryfresh,"
				+ " COALESCE (	tblenquiry.enquirylost,    0) AS enquirylost,"
				+ " COALESCE (	tblenquiry.pendingenquiry, 0) AS pendingenquiry,";
		if (include_preowned.equals("1")) {
			StrSql += " COALESCE (  tblenquiry.evaluation,     0) AS evaluation,";
		}
		StrSql += " COALESCE (	tblso.soretail,          0) AS soretail,"
				+ " COALESCE (	tblso.sodelivered,	   0) AS sodelivered,"
				+ " COALESCE (	tblso.pendingbooking,	   0) AS pendingbooking,"
				+ " COALESCE (	tblso.pendingdelivery,   0) AS pendingdelivery,"
				+ " COALESCE (	tblso.cancellation,	   0) AS cancellation,"
				+ " COALESCE (	tblso.so_mga_amount,	   0) AS mga_amount,"
				+ " COALESCE (	tblso.maruti_insur,	   0) AS maruti_insur,"
				+ " COALESCE (	tblso.extended_warranty, 0) AS extended_warranty,"
				+ " COALESCE (  tblso.fin_cases,         0) AS fin_cases,";
		if (include_preowned.equals("1")) {
			StrSql += " COALESCE (  tblso.exchange,          0) AS exchange,";
		}
		StrSql += " COALESCE (  tbltestdrives.testdrives,      0) AS testdrives,"
				+ " COALESCE (	tbltestdrives.kpitestdrives,   0) AS kpitestdrives,"
				+ " COALESCE (  tblhomevisit.homevisit,        0) AS homevisit,"
				+ " COALESCE (	tblhomevisit.kpihomevisit,	   0) AS kpihomevisit,"
				+ " COALESCE (	tblpreownedenquiry.preownedenquiry,	0) AS preownedenquiry";

		// main join
		StrSql += " FROM " + compdb(comp_id) + "axela_inventory_item_model "
				+ " INNER JOIN axela_brand ON brand_id = model_brand_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id ";

		// target join
		StrSql += " LEFT JOIN ( SELECT SUM(modeltarget_so_count) AS retailtarget,"
				+ " SUM(modeltarget_enquiry_count) AS enquirytarget,"
				+ " modeltarget_model_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id"
				+ Strteamjoin
				+ " WHERE 1 = 1";
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "target_emp_id")
					+ ExeAccess.replace("emp_id", "target_emp_id")
					+ BranchAccess.replace("branch_id", "team_branch_id");
		}
		StrSql += " AND substring(target_startdate, 1, 6) >= substr('" + starttime + "', 1, 6)"
				+ " AND substring(target_enddate, 1, 6) <= substr('" + endtime + "', 1, 6)"
				+ " GROUP BY modeltarget_model_id ) AS tbltarget ON tbltarget.modeltarget_model_id = model_id";

		// enquiry join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
				+ " AND enquiry_status_id = 1,1,0)) AS enquiryopen,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS enquiryfresh,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND (enquiry_status_id = 3 OR enquiry_status_id = 4	),1,0)) AS enquirylost,"

				+ " SUM(IF(enquiry_status_id = 1,1,0)) AS pendingenquiry,";
		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND enquiry_evaluation = 1 ,1,0)) AS evaluation,";
		}

		StrSql += " enquiry_model_id"

				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin
				+ " WHERE 1 = 1 "
				+ StrSearch.replace("branch_id", "enquiry_branch_id");

		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += StrSoe + StrSob
				+ " GROUP BY enquiry_model_id ) AS tblenquiry ON tblenquiry.enquiry_model_id = model_id";

		// so join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS soretail,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_delivered_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_delivered_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS sodelivered,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS pendingbooking,"

				+ " SUM(IF(so_active = '1' "
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = '' ,1,0)) AS pendingdelivery,"

				+ " SUM(IF(so_active = '0'"
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = ''"
				+ " AND SUBSTR(so_cancel_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_cancel_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS cancellation,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,so_mga_amount,0)) AS so_mga_amount,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_insur_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS maruti_insur,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_ew_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS extended_warranty,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_finance_amt > 0"
				+ "	AND so_fintype_id = 1"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS fin_cases,";
		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(so_active = '1'"
					+ " AND so_exchange_amount > 0"
					// + " AND so_exchange = 1"
					+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS exchange,";
		}
		StrSql += " item_model_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
				+ Strteamjoin.replace("target_emp_id", "so_emp_id")
				+ BranchJoin.replace("enquiry_branch_id", "so_branch_id");
		if (!StrSoe.equals("") || !StrSob.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id ";
		}

		StrSql += " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "so_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "so_emp_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id");
		}
		StrSql += StrSoe + StrSob
				+ " GROUP BY item_model_id ) AS tblso ON tblso.item_model_id = model_id";

		// testdrive join
		StrSql += " LEFT JOIN ( SELECT "
				+ " SUM(IF(testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),1,0)) AS testdrives,"
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) THEN enquiry_id END) AS  kpitestdrives,"
				+ " enquiry_model_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin;
		StrSql += " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "enquiry_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += StrSoe + StrSob
				+ " GROUP BY enquiry_model_id ) AS tbltestdrives ON tbltestdrives.enquiry_model_id = model_id";

		// followup join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(followup_feedbacktype_id = 9 ,1,0)) AS homevisit,"
				+ " COUNT(DISTINCT CASE WHEN followup_feedbacktype_id = 9 THEN enquiry_id END) AS  kpihomevisit,"
				+ " enquiry_model_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin
				+ " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "enquiry_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
				+ " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
				+ " GROUP BY enquiry_model_id ) AS tblhomevisit ON tblhomevisit.enquiry_model_id = model_id";

		// preowned join
		StrSql += " LEFT JOIN ( SELECT"
				+ " COUNT(DISTINCT preowned_id) AS preownedenquiry,"
				+ " enquiry_model_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = preowned_enquiry_id"
				+ Strteamjoin.replace("target_emp_id", "preowned_emp_id")
				+ BranchJoin.replace("enquiry_branch_id", "preowned_branch_id")
				+ " WHERE 1 = 1";
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "preowned_emp_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ BranchAccess.replace("branch_id", "preowned_branch_id");
		}
		StrSql += " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND preowned_sales_emp_id != 0"
				+ " GROUP BY enquiry_model_id ) AS tblpreownedenquiry ON tblpreownedenquiry.enquiry_model_id = model_id";

		StrSql += " WHERE 1 = 1 "
				+ StrModel
				+ Strbrand
				+ BranchAccess
				+ " AND model_active = 1 "
				+ " AND model_sales = 1 "
				+ " AND branch_active = 1 "

				+ " GROUP BY "
				+ " model_id ";
		if (dr_orderby.equals("")) {
			StrSql += " ORDER BY model_name";
		}
		else {
			StrSql += " ORDER BY "
					+ dr_orderby + " DESC";
		}
		// SOPInfo("strsql==bymodel=" + StrSql);

		// SOP("strsql==model dash=" + StrSql);

		// CachedRowSet crs1 = processQuery(StrSql, 0);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Build model Headers

				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide='phone'>#</th>\n");
				if (dr_totalby.equals("6")) {
					StrHead.append("<th data-toggle='true'>Model</th>\n");
				}

				StrHead.append("<th title='Retail Target'>RT</th>\n");
				StrHead.append("<th data-hide='phone' title='Enquiry Target'>ET</th>\n");
				StrHead.append("<th data-hide='phone'>Open Enquiry</th>\n");
				StrHead.append("<th data-hide='phone'>Fresh Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Lost Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Pending Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Retail</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Retail %</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Delivered'>Del</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Booking</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Booking %</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Pending Booking</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Cancellation'>Cal</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD%</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit %</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Accessories'>Access</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Insurance'>Ins</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Extended Warranty'>EW</th>\n");
				StrHead.append("<th data-hide='phone, tablet' >Fin Cases</th>\n");

				if (include_preowned.equals("1")) {

					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry '>PE </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry '>PE % </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval %</th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe %</th>\n");
				}

				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				// if (!header.equals("no")) {
				// Str.append("<div class='table-bordered'>\n");
				// Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
				// } else {
				Str.append("<div class='table-bordered'>\n");
				// Str.append("<table border=1 data-filter='#filter' style='border-collapse:collapse;border-color:#726a7a;padding:3px;' width='100%'>\n");
				Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
				// }
				Str.append(StrHead.toString());
				int count = 0, modelcount = 0, branchcount = 0, principalcount = 0, regioncount = 0;
				String modelname = "", branchname = "", principalname = "";
				// String regionname = "";
				crs.beforeFirst();

				double total_target_tilldate = 0;
				double day = 0;
				double daycount = 0;
				day = getDaysBetween(starttime, endtime);
				daycount = getDaysBetween(starttime, endtime)
						+ ((DaysInMonth(Integer.parseInt(SplitYear(endtime)), Integer.parseInt(SplitMonth(endtime))) - Integer.parseInt(SplitDate(endtime))))
						+ (Integer.parseInt(SplitDate(starttime)) - 1);

				while (crs.next()) {
					count++;
					modelname = crs.getString("model_name");
					model_retailtarget = model_retailtarget + crs.getInt("retailtarget");
					model_enquirytarget = model_enquirytarget + crs.getInt("enquirytarget");
					model_enquiryopen = model_enquiryopen + crs.getInt("enquiryopen");
					model_enquiryfresh = model_enquiryfresh + crs.getInt("enquiryfresh");
					model_enquirylost = model_enquirylost + crs.getInt("enquirylost");
					model_pendingenquiry = model_pendingenquiry + (int) crs.getDouble("pendingenquiry");
					model_soretail = model_soretail + (int) crs.getDouble("soretail");
					model_sodelivered = model_sodelivered + crs.getInt("sodelivered");
					model_pendingbooking = model_pendingbooking + crs.getInt("pendingbooking");
					model_pendingdelivery = model_pendingdelivery + crs.getInt("pendingdelivery");
					model_cancellation = model_cancellation + crs.getInt("cancellation");
					model_testdrives = model_testdrives + crs.getInt("testdrives");
					model_kpitestdrives = model_kpitestdrives + crs.getInt("kpitestdrives");
					model_homevisit = model_homevisit + crs.getInt("homevisit");
					model_kpihomevisit = model_kpihomevisit + crs.getInt("kpihomevisit");
					model_mga_sales = model_mga_sales + (int) crs.getDouble("mga_amount");
					model_maruti_insurance = model_maruti_insurance + crs.getInt("maruti_insur");
					model_extwarranty = model_extwarranty + crs.getInt("extended_warranty");
					model_fincases = model_fincases + crs.getInt("fin_cases");
					if (include_preowned.equals("1")) {
						model_preownedenquiry = model_preownedenquiry + crs.getInt("preownedenquiry");
						model_evaluation = model_evaluation + crs.getInt("evaluation");
						model_exchange = model_exchange + crs.getInt("exchange");
					}
					// SOP("model name====" + crs.getString("model_name"));
					// ==================================================================================================================
					modelname = crs.getString("model_name");
					model_id = crs.getString("model_id");
					total_retailtarget = total_retailtarget + crs.getInt("retailtarget");
					total_enquirytarget = total_enquirytarget + crs.getInt("enquirytarget");
					total_enquiryopen = total_enquiryopen + crs.getInt("enquiryopen");
					total_enquiryfresh = total_enquiryfresh + crs.getInt("enquiryfresh");
					total_enquirylost = total_enquirylost + crs.getInt("enquirylost");
					total_soretail = total_soretail + (int) crs.getDouble("soretail");
					total_sodelivered = total_sodelivered + crs.getInt("sodelivered");
					total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
					total_pendingenquiry = total_pendingenquiry + (int) crs.getDouble("pendingenquiry");
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
					total_mga_sales = total_mga_sales + (int) crs.getDouble("mga_amount");
					total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
					total_extwarranty = total_extwarranty + crs.getInt("extended_warranty");
					total_fincases = total_fincases + crs.getInt("fin_cases");
					if (include_preowned.equals("1")) {
						total_preownedenquiry = total_preownedenquiry + crs.getInt("preownedenquiry");
						total_preownedenquiry_perc = getPercentage((double) total_preownedenquiry, (double) total_enquiryfresh);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);
						total_exchange = total_exchange + crs.getInt("exchange");
						total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
					}

					// ==================================================================================================================
					// if (check_branchttl_id.equals("")) {
					// check_branchttl_id = crs.getString("branch_id");
					// branchname = crs.getString("branch_name");
					// }
					if (check_principalttl_id.equals("")) {
						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");
					}
					if (check_regionttl_id.equals("")) {
						// check_regionttl_id = crs.getString("branch_region_id");
						// regionname = crs.getString("region_name");
					}
					// SOP("check_modelttl_id===" + check_modelttl_id);
					// SOP("crs.getString(model_id)==222=" + crs.getString("model_id"));

					if (dr_totalby.equals("6")) {
						Str.append("<tr>");
						Str.append("<td>");
						if (dr_totalby.equals("6")) {
							Str.append("" + count + "");
						} else {
							Str.append("&nbsp");
						}
						Str.append("</td>");
						Str.append("<td nowrap><b>");
						if (dr_totalby.equals("6")) {
							Str.append(modelname);
						} else {
							Str.append("Model Total:");
						}
						Str.append("</b></td>");
						// model_retailtarget
						Str.append("<td><b>");

						total_target_tilldate = (model_retailtarget / daycount) * day;

						Str.append(Math.round(total_target_tilldate) + "/" + model_retailtarget);
						Str.append("</b></td>");
						// model_enquirytarget
						Str.append("<td><b>");

						total_target_tilldate = (model_enquirytarget / daycount) * day;

						Str.append(Math.round(total_target_tilldate) + "/" + model_enquirytarget);
						Str.append("</b></td>");
						// model_enquiryopen
						// SOP("model_id=====" + model_id);
						Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=enquiryopen&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquiryopen);
						Str.append("</a></b></td>");

						// model_enquiryfresh
						Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=enquiryfresh&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquiryfresh);
						Str.append("</a></b></td>");

						// model_enquirylost
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=enquirylost&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquirylost);
						Str.append("</a></b></td>");

						// model_pendingenquiry
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=pendingenquiry&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingenquiry);
						Str.append("</a></b></td>");

						// model_soretail
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=soretail&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_soretail);
						Str.append("</a></b></td>");

						// model_soretail_perc
						model_soretail_perc = getPercentage((double) model_soretail, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_soretail_perc).append("%");
						Str.append("</b></td>");
						// model_sodelivered
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=sodelivered&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_sodelivered);
						Str.append("</a></b></td>");
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=pendingbooking&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingbooking);
						Str.append("</a></b></td>");

						// model_pendingbooking_perc
						model_pendingbooking_perc = getPercentage((double) model_pendingbooking, (double) model_enquiryfresh);
						Str.append("<td ><b>");
						Str.append(model_pendingbooking_perc).append("%");
						Str.append("</b></td>");

						// model_pendingdelivery
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=pendingdelivery&model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingdelivery);
						Str.append("</a></b></td>");

						// model_cancellation
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=cancellation&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_cancellation);
						Str.append("</a></b></td>");

						// model_testdrives
						Str.append("<td  ><b><a href='").append(SearchURL).append("?testdrive=testdrives&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_testdrives);
						Str.append("</a></b></td>");

						// model_kpitestdrives
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=kpitestdrives&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_kpitestdrives);
						Str.append("</a></b></td>");

						// model_testdrives_perc
						model_testdrives_perc = getPercentage((double) model_kpitestdrives, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_testdrives_perc).append("%");
						Str.append("</b></td>");

						// model_homevisit
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=homevisit&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_homevisit);
						Str.append("</a></b></td>");

						// model_kpihomevisit
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=kpihomevisit&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_kpihomevisit);
						Str.append("</a></b></td>");

						// model_homevisit_perc
						model_homevisit_perc = getPercentage((double) model_kpihomevisit, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_homevisit_perc).append("%");
						Str.append("</b></td>");

						// model_mga_sales
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=mgaamount&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_mga_sales);
						Str.append("</a></b></td>");

						// model_maruti_insurance
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=marutiinsur&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_maruti_insurance);
						Str.append("</a></b></td>");

						// model_extwarranty
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=extendedwarranty&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_extwarranty);
						Str.append("</a></b></td>");

						// model_fincases
						Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=fincases&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_fincases);
						Str.append("</a></b></td>");

						if (include_preowned.equals("1")) {
							// Preowned_enquiry
							Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=preowned&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_preownedenquiry);
							Str.append("</a></b></td>");
							// Preowned_enquiry percentage
							model_preownedenquiry_perc = getPercentage((double) model_preownedenquiry, (double) model_enquiryfresh);
							Str.append("<td><b>");
							Str.append(model_preownedenquiry_perc).append("%");
							Str.append("</b></td>");

							// model_evaluation
							Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=evaluation&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_evaluation);
							Str.append("</a></b></td>");
							// model_evaluation_perc
							model_evaluation_perc = getPercentage((double) model_evaluation, (double) model_enquiryfresh);
							Str.append("<td><b>");
							Str.append(model_evaluation_perc).append("%");
							Str.append("</b></td>");

							// model_exchange
							Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=exchange&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_exchange);
							Str.append("</a></b></td>");

							// model_exchange_perc
							model_exchange_perc = getPercentage((double) model_exchange, (double) model_pendingbooking);
							Str.append("<td><b>");
							Str.append(model_exchange_perc).append("%");
							Str.append("</b></td>");

						}

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
						model_preownedenquiry = 0;
						model_preownedenquiry_perc = "0";
						model_evaluation = 0;
						model_evaluation_perc = "0";
						model_exchange = 0;
						model_exchange_perc = "0";

					}
					// ==================================================================================================================

					// if (check_branchttl_id.equals(crs.getString("branch_id")) && dr_totalby.equals("2")) {}

					// if (check_principalttl_id.equals(crs.getString("brand_id")) && dr_totalby.equals("3")) {}

					// if (check_regionttl_id.equals(crs.getString("region_id")) && dr_totalby.equals("4")) {}

					// ==================================================================================================================
					if (!check_model_id.equals(crs.getString("model_id"))) {
						modelcount++;
						modelname = crs.getString("model_name");
						check_model_id = crs.getString("model_id");
					}

					// End While Loop
				}

				// ///// Display Grand Total links for model
				Str.append("<tr>\n");
				Str.append("<td colspan=2><b>Total:</b></td>");
				// * total_model_retailtarget
				Str.append("<td  ><b>");

				total_target_tilldate = (total_enquirytarget / daycount) * day;

				Str.append(Math.round(total_target_tilldate) + "/" + total_retailtarget).append("</b></td>");
				// *total_model_enquirytarget
				Str.append("<td  ><b>");
				// final total

				total_target_tilldate = (total_enquirytarget / daycount) * day;

				Str.append(Math.round(total_target_tilldate) + "/" + total_enquirytarget).append("</b></td>");
				// *total_model_enquiryopen
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=enquiryopen")
						.append("&region_id=").append(region_id).append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquiryopen).append("</a></b></td>");
				// *total_model_enquiryfresh
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=enquiryfresh").append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=")
						.append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquiryfresh).append("</a></b></td>");
				// *total_model_enquirylost
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=enquirylost").append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=")
						.append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquirylost).append("</a></b></td>");
				// *total_model_pendingenquiry
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=pendingenquiry").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&starttime=").append(starttime).append("&endtime=")
						.append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				// SOP("total_pendingenquiry---3---------" +total_model_pendingenquiry);
				Str.append(total_pendingenquiry).append("</a></b></td>");
				// *total_model_soretail
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=soretail").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_soretail).append("</a></b></td>");
				// *total_model_soretail_perc
				Str.append("<td  ><b>");
				Str.append(total_soretail_perc).append("%").append("</b></td>");
				// *total_model_sodelivered
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=sodelivered").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_sodelivered).append("</a></b></td>");
				// *total_model_pendingbooking
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=pendingbooking").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_pendingbooking).append("</a></b></td>");
				// *total_model_pendingbooking_perc
				Str.append("<td  ><b>");
				Str.append(total_pendingbooking_perc).append("%").append("</b></td>");
				// *total_model_pendingdelivery
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=pendingdelivery").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_pendingdelivery).append("</a></b></td>");
				// *total_model_cancellation
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=cancellation").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_cancellation).append("</a></b></td>");
				// *total_model_testdrives
				Str.append("<td  ><b><a href='").append(SearchURL).append("?testdrive=testdrives").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_testdrives).append("</a></b></td>");
				// *total_model_kpitestdrives
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=kpitestdrives").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_kpitestdrives).append("</a></b></td>");
				// *total_model_testdrives_perc
				Str.append("<td  ><b>");
				Str.append(total_testdrives_perc).append("%").append("</b></td>");
				// *total_model_homevisit
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=homevisit").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_homevisit).append("</a></b></td>");
				// *total_model_kpihomevisit
				Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry=kpihomevisit").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_kpihomevisit).append("</a></b></td>");
				// *total_model_homevisit_perc
				Str.append("<td  ><b>");
				Str.append(total_homevisit_perc).append("%").append("</b></td>");
				// *total_model_mga_sales
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=mgaamount").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_mga_sales).append("</a></b></td>");
				// *total_model_maruti_insurance
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=marutiinsur").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_maruti_insurance).append("</a></b></td>");
				// *total_model_extwarranty
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=extendedwarranty").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=")
						.append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_extwarranty).append("</a></b></td>");
				// *total_model_fincases
				Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=fincases").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_fincases).append("</a></b></td>");

				if (include_preowned.equals("1")) {

					// *total_model_preowned
					Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=preowned").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_preownedenquiry).append("</a></b></td>");
					// *total_model_preowned_perc
					Str.append("<td><b>");
					Str.append(total_preownedenquiry_perc).append("%").append("</b></td>");

					// *total_model_evaluation
					Str.append("<td><b><a href='").append(SearchURL).append("?enquiry=evaluation").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_evaluation).append("</a></b></td>");
					// *total_model_evaluation_perc
					Str.append("<td><b>");
					Str.append(total_evaluation_perc).append("%").append("</b></td>");
					// *total_model_exchange
					Str.append("<td  ><b><a href='").append(SearchURL).append("?salesorder=exchange").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_exchange).append("</a></b></td>");
					// *total_model_exchange_perc
					Str.append("<td><b>");
					Str.append(total_exchange_perc).append("%").append("</b></td>");

				}

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

	// Pre owned MODEL DASH QUERY
	public String ListPreownedModelMonitorBoard(String dr_branch_id, String starttime, String endtime, String targetstarttime, String targetendtime, String dr_totalby, String comp_id) {
		String Strteamjoin = "", Strteam = "", Stremp = "";
		String BranchJoin = "";
		StrSearch = "";
		String Strbrand = "";
		if (!brand_id.equals("")) {
			Strbrand = " AND model_brand_id IN (" + brand_id + ") ";
		}
		if (!region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + region_id + ") ";
		}
		if (!branch_id.equals("")) {
			StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
		}
		if (!brand_id.equals("") || !branch_id.equals("") || !region_id.equals("")) {
			BranchJoin = " INNER JOIN " + compdb(comp_id) + "axela_branch branch ON branch_id = enquiry_branch_id";
		}

		if (!model_id.equals("")) {
			StrModel = " AND model_id IN (" + model_id + ")";
		}
		if (!soe_id.equals("")) {
			StrSoe = " AND enquiry_soe_id IN (" + soe_id + ")";
		}
		if (!sob_id.equals("")) {
			StrSob = " AND enquiry_sob_id IN (" + sob_id + ")";
		}
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			Strteamjoin = " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id";
		}
		if (!team_id.equals("")) {
			Strteam = " AND teamtrans_team_id IN (" + team_id + ")"
					+ " AND team_active = 1";
		}
		if (!exe_id.equals("")) {
			Stremp = " AND emp_id IN (" + exe_id + ")";
		}
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
		int total_evaluation = 0, total_preownedenquiry = 0;
		String total_evaluation_perc = "0";
		String total_preownedenquiry_perc = "0";
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
		int model_preownedenquiry = 0;
		String model_preownedenquiry_perc = "0";

		String model_evaluation_perc = "0";
		String check_model_id = "", check_modelttl_id = "";

		String check_principalttl_id = "";
		String check_regionttl_id = "";

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();

		StrSql = "SELECT "
				+ " preownedmodel_id,"
				+ " brand_id,"
				+ " branch_region_id,"
				+ " branch_id, "
				+ " brand_name,"
				+ " branch_name,"
				+ " preownedmodel_name,"
				+ " carmanuf_name,";

		StrSql += " COALESCE (  tbltarget.enquirytarget,       0) AS enquirytarget,"
				+ " COALESCE (  tbltarget.retailtarget,		   0) AS retailtarget,"
				+ " COALESCE (  tblenquiry.enquiryopen,	   0) AS enquiryopen,"
				+ " COALESCE (	tblenquiry.enquiryfresh,   0) AS enquiryfresh,"
				+ " COALESCE (	tblenquiry.enquirylost,    0) AS enquirylost,"
				+ " COALESCE (	tblenquiry.pendingenquiry, 0) AS pendingenquiry,";
		if (include_preowned.equals("1")) {
			StrSql += " COALESCE (  tblenquiry.evaluation,     0) AS evaluation,";
		}
		StrSql += " COALESCE (	tblso.soretail,            0) AS soretail,"
				+ " COALESCE (	tblso.sodelivered,	       0) AS sodelivered,"
				+ " COALESCE (	tblso.pendingbooking,	   0) AS pendingbooking,"
				+ " COALESCE (	tblso.pendingdelivery,     0) AS pendingdelivery,"
				+ " COALESCE (	tblso.cancellation,	       0) AS cancellation,"
				+ " COALESCE (	tblso.so_mga_amount,	   0) AS mga_amount,"
				+ " COALESCE (	tblso.maruti_insur,	       0) AS maruti_insur,"
				+ " COALESCE (	tblso.extended_warranty,   0) AS extended_warranty,"
				+ " COALESCE (  tblso.fin_cases,           0) AS fin_cases,";
		if (include_preowned.equals("1")) {
			StrSql += " COALESCE (  tblso.exchange, 0) AS exchange,"
					+ " COALESCE (	tblpreownedenquiry.preownedenquiry,	0) AS preownedenquiry,";
		}
		StrSql += " COALESCE (  tbltestdrives.testdrives,  0) AS testdrives,"
				+ " COALESCE (	tbltestdrives.kpitestdrives,0) AS kpitestdrives,"
				+ " COALESCE (  tblhomevisit.homevisit,    0) AS homevisit,"
				+ " COALESCE (	tblhomevisit.kpihomevisit, 0) AS kpihomevisit";

		// main join
		StrSql += " FROM axelaauto.axela_preowned_variant"
				+ " INNER JOIN axelaauto.axela_preowned_model ON variant_id = variant_preownedmodel_id"
				+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";

		// target join
		StrSql += " LEFT JOIN ( SELECT SUM(modeltarget_so_count) AS retailtarget,"
				+ " SUM(modeltarget_enquiry_count) AS enquirytarget,"
				+ " modeltarget_model_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id"
				+ Strteamjoin
				+ " WHERE 1=1";
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "target_emp_id")
					+ ExeAccess.replace("emp_id", "target_emp_id")
					+ BranchAccess.replace("branch_id", "team_branch_id");
		}
		StrSql += " AND substring(target_startdate, 1, 6) >= substr('" + starttime + "', 1, 6)"
				+ " AND substring(target_enddate, 1, 6) <= substr('" + endtime + "', 1, 6)"
				+ " GROUP BY modeltarget_model_id ) AS tbltarget ON tbltarget.modeltarget_model_id = preownedmodel_id";

		// enquiry join
		StrSql += " INNER JOIN ( SELECT"
				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
				+ " AND enquiry_status_id = 1,1,0)) AS enquiryopen,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS enquiryfresh,"

				+ " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " AND (enquiry_status_id = 3 OR enquiry_status_id = 4	),1,0)) AS enquirylost,"

				+ " SUM(IF(enquiry_status_id = 1,1,0)) AS pendingenquiry,";

		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND enquiry_evaluation = 1 ,1,0)) AS evaluation,";
		}
		StrSql += " variant_preownedmodel_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin
				+ " WHERE 1 = 1 "
				+ StrSearch.replace("branch_id", "enquiry_branch_id");

		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += StrSoe + StrSob
				+ " GROUP BY variant_preownedmodel_id ) AS tblenquiry ON tblenquiry.variant_preownedmodel_id = preownedmodel_id";

		// so join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS soretail,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_delivered_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_delivered_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS sodelivered,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS pendingbooking,"

				+ " SUM(IF(so_active = '1' "
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = '' ,1,0)) AS pendingdelivery,"

				+ " SUM(IF(so_active = '0'"
				+ " AND so_delivered_date = ''"
				+ "	AND so_retail_date = ''"
				+ " AND SUBSTR(so_cancel_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_cancel_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS cancellation,"

				+ " SUM(IF(so_active = '1'"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,so_mga_amount,0)) AS so_mga_amount,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_insur_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS maruti_insur,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_ew_amount > 0"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS extended_warranty,"

				+ " SUM(IF(so_active = '1'"
				+ " AND so_finance_amt > 0"
				+ "	AND so_fintype_id = 1"
				+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS fin_cases,";

		if (include_preowned.equals("1")) {
			StrSql += " SUM(IF(so_active = '1'"
					+ " AND so_exchange_amount > 0"
					// + " AND so_exchange = 1"
					+ " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ,1,0)) AS exchange,";
		}
		StrSql += " variant_preownedmodel_id"

				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
				+ Strteamjoin.replace("target_emp_id", "so_emp_id")
				+ BranchJoin.replace("enquiry_branch_id", "so_branch_id");

		StrSql += " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "so_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "so_emp_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id");
		}
		StrSql += StrSoe + StrSob
				+ " GROUP BY variant_preownedmodel_id ) AS tblso ON tblso.variant_preownedmodel_id = preownedmodel_id";

		// testdrive join
		StrSql += " LEFT JOIN ( SELECT "
				+ " SUM(IF(testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),1,0)) AS testdrives,"
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) THEN enquiry_id END) AS  kpitestdrives,"
				+ " variant_preownedmodel_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin
				+ " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "enquiry_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += " GROUP BY variant_preownedmodel_id ) AS tbltestdrives ON tbltestdrives.variant_preownedmodel_id = preownedmodel_id";

		// followup join
		StrSql += " LEFT JOIN ( SELECT"
				+ " SUM(IF(followup_feedbacktype_id = 9 ,1,0)) AS homevisit,"
				+ " COUNT(DISTINCT CASE WHEN followup_feedbacktype_id = 9 THEN enquiry_id END) AS  kpihomevisit,"
				+ " variant_preownedmodel_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
				+ Strteamjoin.replace("target_emp_id", "enquiry_emp_id")
				+ BranchJoin
				+ " WHERE 1 = 1"
				+ StrSearch.replace("branch_id", "enquiry_branch_id");
		if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
			StrSql += Strteam + Stremp.replace("emp_id", "enquiry_emp_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
		}
		StrSql += " AND substr(followup_followup_time, 1, 8) >= substr('" + starttime + "', 1, 8)"
				+ " AND substr(followup_followup_time, 1, 8) <= substr('" + endtime + "', 1, 8)"
				+ " GROUP BY variant_preownedmodel_id ) AS tblhomevisit ON tblhomevisit.variant_preownedmodel_id = preownedmodel_id";

		if (include_preowned.equals("1")) {
			// preowned join
			StrSql += " LEFT JOIN ( SELECT"
					+ " COUNT(DISTINCT preowned_id) AS preownedenquiry,"
					+ " variant_preownedmodel_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = preowned_enquiry_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
					+ Strteamjoin.replace("target_emp_id", "preowned_emp_id")
					+ BranchJoin.replace("enquiry_branch_id", "preowned_branch_id")
					+ " WHERE 1 = 1";
			if (!ExeAccess.equals("") || !BranchAccess.equals("") || !team_id.equals("") || !exe_id.equals("")) {
				StrSql += Strteam + Stremp.replace("emp_id", "preowned_emp_id")
						+ ExeAccess.replace("emp_id", "preowned_emp_id")
						+ BranchAccess.replace("branch_id", "preowned_branch_id");
			}
			StrSql += " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND preowned_sales_emp_id != 0"
					+ " GROUP BY variant_preownedmodel_id ) AS tblpreownedenquiry ON tblpreownedenquiry.variant_preownedmodel_id = preownedmodel_id";
		}

		StrSql += " ," + compdb(comp_id) + "axela_emp"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
				+ " INNER JOIN axela_brand ON brand_id = branch_brand_id";

		StrSql += " WHERE 1 = 1 "
				+ BranchAccess
				+ " AND branch_active = 1 "
				+ " GROUP BY "
				+ " preownedmodel_id ";
		if (dr_orderby.equals("")) {
			StrSql += " ORDER BY preownedmodel_name";
		} else {
			StrSql += " ORDER BY "
					+ dr_orderby + " DESC";
		}
		// if (emp_id.equals("1")) {
		// SOP("strsql==model mb------" + StrSql);
		// }

		// CachedRowSet crs1 = processQuery(StrSql, 0);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Build model Headers

				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide='phone'>#</th>\n");
				if (dr_totalby.equals("6")) {
					StrHead.append("<th data-toggle='true'>Model</th>\n");
				}

				StrHead.append("<th title='Retail Target'>RT</th>\n");
				StrHead.append("<th data-hide='phone' title='Enquiry Target'>ET</th>\n");
				StrHead.append("<th data-hide='phone'>Open Enquiry</th>\n");
				StrHead.append("<th data-hide='phone'>Fresh Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Lost Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Pending Enquiry</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Retail</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Retail %</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Delivered'>Del</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Booking</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Booking %</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Pending Booking</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Cancellation'>Cal</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI TD</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>TD%</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>KPI Home Visit</th>\n");
				StrHead.append("<th data-hide='phone, tablet'>Home Visit %</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Accessories'>Access</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Insurance'>Ins</th>\n");
				StrHead.append("<th data-hide='phone, tablet' title='Extended Warranty'>EW</th>\n");
				StrHead.append("<th data-hide='phone, tablet' >Fin Cases</th>\n");

				if (include_preowned.equals("1")) {

					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry '>PE </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Preowned Enquiry '>PE % </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Evaluation'>Eval %</th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe </th>\n");
					StrHead.append("<th data-hide='phone, tablet' title='Exchange'>Exe %</th>\n");
				}

				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				// if (!header.equals("no")) {
				// Str.append("<div class='table-bordered'>\n");
				// Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
				// } else {
				Str.append("<div class='table-bordered'>\n");
				// Str.append("<table border=1 data-filter='#filter' style='border-collapse:collapse;border-color:#726a7a;padding:3px;' width='100%'>\n");
				Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
				// }
				Str.append(StrHead.toString());
				int count = 0, modelcount = 0, branchcount = 0, principalcount = 0, regioncount = 0;
				String modelname = "", branchname = "", principalname = "";
				// String regionname = "";
				crs.beforeFirst();

				double total_target_tilldate = 0;
				double day = 0;
				double daycount = 0;
				day = getDaysBetween(starttime, endtime);
				daycount = getDaysBetween(starttime, endtime)
						+ ((DaysInMonth(Integer.parseInt(SplitYear(endtime)), Integer.parseInt(SplitMonth(endtime))) - Integer.parseInt(SplitDate(endtime))))
						+ (Integer.parseInt(SplitDate(starttime)) - 1);

				while (crs.next()) {
					count++;
					modelname = crs.getString("preownedmodel_name");
					model_retailtarget = model_retailtarget + crs.getInt("retailtarget");
					model_enquirytarget = model_enquirytarget + crs.getInt("enquirytarget");
					model_enquiryopen = model_enquiryopen + crs.getInt("enquiryopen");
					model_enquiryfresh = model_enquiryfresh + crs.getInt("enquiryfresh");
					model_enquirylost = model_enquirylost + crs.getInt("enquirylost");
					model_pendingenquiry = model_pendingenquiry + (int) crs.getDouble("pendingenquiry");
					model_soretail = model_soretail + (int) crs.getDouble("soretail");
					model_sodelivered = model_sodelivered + crs.getInt("sodelivered");
					model_pendingbooking = model_pendingbooking + crs.getInt("pendingbooking");
					model_pendingdelivery = model_pendingdelivery + crs.getInt("pendingdelivery");
					model_cancellation = model_cancellation + crs.getInt("cancellation");
					model_testdrives = model_testdrives + crs.getInt("testdrives");
					model_kpitestdrives = model_kpitestdrives + crs.getInt("kpitestdrives");
					model_homevisit = model_homevisit + crs.getInt("homevisit");
					model_kpihomevisit = model_kpihomevisit + crs.getInt("kpihomevisit");
					model_mga_sales = model_mga_sales + (int) crs.getDouble("mga_amount");
					model_maruti_insurance = model_maruti_insurance + crs.getInt("maruti_insur");
					model_extwarranty = model_extwarranty + crs.getInt("extended_warranty");
					model_fincases = model_fincases + crs.getInt("fin_cases");
					if (include_preowned.equals("1")) {
						model_preownedenquiry = model_preownedenquiry + crs.getInt("preownedenquiry");
						model_evaluation = model_evaluation + crs.getInt("evaluation");
						model_exchange = model_exchange + crs.getInt("exchange");
					}

					// SOP("model name====" + crs.getString("preownedmodel_name"));
					// ==================================================================================================================
					modelname = crs.getString("carmanuf_name") + " - " + crs.getString("preownedmodel_name");
					model_id = crs.getString("preownedmodel_id");
					total_retailtarget = total_retailtarget + crs.getInt("retailtarget");
					total_enquirytarget = total_enquirytarget + crs.getInt("enquirytarget");
					total_enquiryopen = total_enquiryopen + crs.getInt("enquiryopen");
					total_enquiryfresh = total_enquiryfresh + crs.getInt("enquiryfresh");
					total_enquirylost = total_enquirylost + crs.getInt("enquirylost");
					total_soretail = total_soretail + (int) crs.getDouble("soretail");
					total_sodelivered = total_sodelivered + crs.getInt("sodelivered");
					total_soretail_perc = getPercentage((double) total_soretail, (double) total_enquiryfresh);
					total_pendingenquiry = total_pendingenquiry + (int) crs.getDouble("pendingenquiry");
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
					total_mga_sales = total_mga_sales + (int) crs.getDouble("mga_amount");
					total_maruti_insurance = total_maruti_insurance + crs.getInt("maruti_insur");
					total_extwarranty = total_extwarranty + crs.getInt("extended_warranty");
					total_fincases = total_fincases + crs.getInt("fin_cases");
					if (include_preowned.equals("1")) {
						total_preownedenquiry = total_preownedenquiry + crs.getInt("preownedenquiry");
						total_preownedenquiry_perc = getPercentage((double) total_preownedenquiry, (double) total_enquiryfresh);
						total_evaluation = total_evaluation + crs.getInt("evaluation");
						total_evaluation_perc = getPercentage((double) total_evaluation, (double) total_enquiryfresh);
						total_exchange = total_exchange + crs.getInt("exchange");
						total_exchange_perc = getPercentage((double) total_exchange, (double) total_pendingbooking);
					}

					// ==================================================================================================================
					// if (check_branchttl_id.equals("")) {
					// check_branchttl_id = crs.getString("branch_id");
					// branchname = crs.getString("branch_name");
					// }
					if (check_principalttl_id.equals("")) {
						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");
					}
					if (check_regionttl_id.equals("")) {
						// check_regionttl_id = crs.getString("branch_region_id");
						// regionname = crs.getString("region_name");
					}
					// SOP("check_modelttl_id===" + check_modelttl_id);
					// SOP("crs.getString(model_id)==222=" + crs.getString("model_id"));

					if (dr_totalby.equals("6")) {
						Str.append("<tr>");
						Str.append("<td>");
						if (dr_totalby.equals("6")) {
							Str.append("" + count + "");
						} else {
							Str.append("&nbsp");
						}
						Str.append("</td>");
						Str.append("<td nowrap><b>");
						if (dr_totalby.equals("6")) {
							Str.append(modelname);
						} else {
							Str.append("Model Total:");
						}
						Str.append("</b></td>");
						// model_retailtarget
						Str.append("<td><b>");

						total_target_tilldate = (model_retailtarget / daycount) * day;

						Str.append(Math.round(total_target_tilldate) + "/" + model_retailtarget);
						Str.append("</b></td>");
						// model_enquirytarget
						Str.append("<td><b>");

						total_target_tilldate = (model_enquirytarget / daycount) * day;

						Str.append(Math.round(total_target_tilldate) + "/" + model_enquirytarget);
						Str.append("</b></td>");
						// model_enquiryopen
						Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_open&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquiryopen);
						Str.append("</a></b></td>");

						// model_enquiryfresh
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_fresh&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquiryfresh);
						Str.append("</a></b></td>");

						// model_enquirylost
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_lost&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_enquirylost);
						Str.append("</a></b></td>");

						// model_pendingenquiry
						Str.append("<td  ><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_pending&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=")
								.append(endtime).append("&team_id=").append(team_id).append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingenquiry);
						Str.append("</a></b></td>");

						// model_soretail
						Str.append("<td  ><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_soretail&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_soretail);
						Str.append("</a></b></td>");

						// model_soretail_perc
						model_soretail_perc = getPercentage((double) model_soretail, (double) model_enquiryfresh);
						Str.append("<td  ><b>");
						Str.append(model_soretail_perc).append("%");
						Str.append("</b></td>");
						// model_sodelivered
						Str.append("<td  ><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_sodelivered&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_sodelivered);
						Str.append("</a></b></td>");
						Str.append("<td  ><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_pendingbooking&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingbooking);
						Str.append("</a></b></td>");

						// model_pendingbooking_perc
						model_pendingbooking_perc = getPercentage((double) model_pendingbooking, (double) model_enquiryfresh);
						Str.append("<td ><b>");
						Str.append(model_pendingbooking_perc).append("%");
						Str.append("</b></td>");

						// model_pendingdelivery
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_pendingdelivery&model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_pendingdelivery);
						Str.append("</a></b></td>");

						// model_cancellation
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_cancellation&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_cancellation);
						Str.append("</a></b></td>");

						// model_testdrives
						Str.append("<td><b><a href='").append(SearchURL).append("?td_preowned_model=td_preowned_model_testdrives&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_testdrives);
						Str.append("</a></b></td>");

						// model_kpitestdrives
						Str.append("<td><b><a href='").append(SearchURL).append("?td_preowned_model=td_preowned_model_kpitestdrives&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_kpitestdrives);
						Str.append("</a></b></td>");

						// model_testdrives_perc
						model_testdrives_perc = getPercentage((double) model_kpitestdrives, (double) model_enquiryfresh);
						Str.append("<td><b>");
						Str.append(model_testdrives_perc).append("%");
						Str.append("</b></td>");

						// model_homevisit
						Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_homevisit&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_homevisit);
						Str.append("</a></b></td>");

						// model_kpihomevisit
						Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_kpihomevisit&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id)
								.append("&sob_id=").append(sob_id).append("&starttime=").append(starttime)
								.append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_kpihomevisit);
						Str.append("</a></b></td>");

						// model_homevisit_perc
						model_homevisit_perc = getPercentage((double) model_kpihomevisit, (double) model_enquiryfresh);
						Str.append("<td><b>");
						Str.append(model_homevisit_perc).append("%");
						Str.append("</b></td>");

						// model_mga_sales
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_mgaamount&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_mga_sales);
						Str.append("</a></b></td>");

						// model_maruti_insurance
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_marutiinsur&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_maruti_insurance);
						Str.append("</a></b></td>");

						// model_extwarranty
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_extendedwarranty&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_extwarranty);
						Str.append("</a></b></td>");

						// model_fincases
						Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_fincases&" + "model_id=");
						Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
								.append("&team_id=").append(team_id)
								.append("");
						Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
						Str.append(model_fincases);
						Str.append("</a></b></td>");

						if (include_preowned.equals("1")) {

							// Preowned_enquiry
							Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquiry_preowned_model_preowned&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_preownedenquiry);
							Str.append("</a></b></td>");
							// Preowned_enquiry percentage
							model_preownedenquiry_perc = getPercentage((double) model_preownedenquiry, (double) model_enquiryfresh);
							Str.append("<td><b>");
							Str.append(model_preownedenquiry_perc).append("%");
							Str.append("</b></td>");

							// model_evaluation
							Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquiry_preowned_model_evaluation&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_evaluation);
							Str.append("</a></b></td>");
							// model_evaluation_perc
							model_evaluation_perc = getPercentage((double) model_evaluation, (double) model_enquiryfresh);
							Str.append("<td><b>");
							Str.append(model_evaluation_perc).append("%");
							Str.append("</b></td>");

							// model_exchange
							Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_exchange&" + "model_id=");
							Str.append(model_id).append("&region_id=").append(region_id).append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime)
									.append("&team_id=").append(team_id)
									.append("");
							Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
							Str.append(model_exchange);
							Str.append("</a></b></td>");

							// model_exchange_perc
							model_exchange_perc = getPercentage((double) model_exchange, (double) model_pendingbooking);
							Str.append("<td><b>");
							Str.append(model_exchange_perc).append("%");
							Str.append("</b></td>");
						}

						Str.append("</tr>");
						check_modelttl_id = crs.getString("preownedmodel_id");

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

						if (include_preowned.equals("1")) {
							model_preownedenquiry = 0;
							model_preownedenquiry_perc = "0";
							model_evaluation = 0;
							model_evaluation_perc = "0";
							model_exchange = 0;
							model_exchange_perc = "0";
						}

					}
					// ==================================================================================================================

					// if (check_branchttl_id.equals(crs.getString("branch_id")) && dr_totalby.equals("2")) {}

					// if (check_principalttl_id.equals(crs.getString("brand_id")) && dr_totalby.equals("3")) {}

					// if (check_regionttl_id.equals(crs.getString("region_id")) && dr_totalby.equals("4")) {}

					// ==================================================================================================================
					if (!check_model_id.equals(crs.getString("preownedmodel_id"))) {
						modelcount++;
						modelname = crs.getString("preownedmodel_name");
						check_model_id = crs.getString("preownedmodel_id");
					}

					// End While Loop
				}

				// ///// Display Grand Total links for model
				Str.append("<tr>\n");
				Str.append("<td colspan=2><b>Total:</b></td>");
				// * total_model_retailtarget
				Str.append("<td><b>");

				total_target_tilldate = (total_retailtarget / daycount) * day;

				Str.append(Math.round(total_target_tilldate) + "/" + total_retailtarget).append("</b></td>");
				// *total_model_enquirytarget
				Str.append("<td><b>");
				// final total

				total_target_tilldate = (total_enquirytarget / daycount) * day;

				Str.append(Math.round(total_target_tilldate) + "/" + total_enquirytarget).append("</b></td>");
				// *total_model_enquiryopen
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_open")
						.append("&region_id=").append(region_id).append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&region_id=").append(region_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquiryopen).append("</a></b></td>");
				// *total_model_enquiryfresh
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_fresh").append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=")
						.append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquiryfresh).append("</a></b></td>");
				// *total_model_enquirylost
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_lost").append("&region_id=").append(region_id)
						.append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=")
						.append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_enquirylost).append("</a></b></td>");
				// *total_model_pendingenquiry
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_pending").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&starttime=").append(starttime).append("&endtime=")
						.append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				// SOP("total_pendingenquiry---3---------" +total_model_pendingenquiry);
				Str.append(total_pendingenquiry).append("</a></b></td>");
				// *total_model_soretail
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_soretail").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_soretail).append("</a></b></td>");
				// *total_model_soretail_perc
				Str.append("<td><b>");
				Str.append(total_soretail_perc).append("%").append("</b></td>");
				// *total_model_sodelivered
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_sodelivered").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_sodelivered).append("</a></b></td>");
				// *total_model_pendingbooking
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_pendingbooking").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_pendingbooking).append("</a></b></td>");
				// *total_model_pendingbooking_perc
				Str.append("<td><b>");
				Str.append(total_pendingbooking_perc).append("%").append("</b></td>");
				// *total_model_pendingdelivery
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_pendingdelivery").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime);
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_pendingdelivery).append("</a></b></td>");
				// *total_model_cancellation
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_cancellation").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime).append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_cancellation).append("</a></b></td>");
				// *total_model_testdrives
				Str.append("<td><b><a href='").append(SearchURL).append("?td_preowned_model=td_preowned_model_testdrives").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_testdrives).append("</a></b></td>");
				// *total_model_kpitestdrives
				Str.append("<td><b><a href='").append(SearchURL).append("?td_preowned_model=td_preowned_model_kpitestdrives").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_kpitestdrives).append("</a></b></td>");
				// *total_model_testdrives_perc
				Str.append("<td><b>");
				Str.append(total_testdrives_perc).append("%").append("</b></td>");
				// *total_model_homevisit
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_homevisit").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_homevisit).append("</a></b></td>");
				// *total_model_kpihomevisit
				Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquirypreownedmodel_kpihomevisit").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id).append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&soe_id=").append(soe_id).append("&sob_id=").append(sob_id).append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id)
						.append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_kpihomevisit).append("</a></b></td>");
				// *total_model_homevisit_perc
				Str.append("<td><b>");
				Str.append(total_homevisit_perc).append("%").append("</b></td>");
				// *total_model_mga_sales
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_mgaamount").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_mga_sales).append("</a></b></td>");
				// *total_model_maruti_insurance
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_marutiinsur").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_maruti_insurance).append("</a></b></td>");
				// *total_model_extwarranty
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_extendedwarranty").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=")
						.append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_extwarranty).append("</a></b></td>");
				// *total_model_fincases
				Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_fincases").append("&region_id=").append(region_id)
						.append("&emp_id=").append(exe_id)
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime).append("");
				Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
				Str.append(total_fincases).append("</a></b></td>");

				if (include_preowned.equals("1")) {

					// *total_model_preowned
					Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquiry_preowned_model_preowned").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_preownedenquiry).append("</a></b></td>");
					// *total_model_preowned_perc
					Str.append("<td><b>");
					Str.append(total_preownedenquiry_perc).append("%").append("</b></td>");

					// *total_model_evaluation
					Str.append("<td><b><a href='").append(SearchURL).append("?enquiry_preowned_model=enquiry_preowned_model_evaluation").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_evaluation).append("</a></b></td>");
					// *total_model_evaluation_perc
					Str.append("<td><b>");
					Str.append(total_evaluation_perc).append("%").append("</b></td>");
					// *total_model_exchange
					Str.append("<td><b><a href='").append(SearchURL).append("?so_preowned_model=so_preowned_model_exchange").append("&region_id=").append(region_id)
							.append("&emp_id=").append(exe_id)
							.append("&starttime=").append(starttime)
							.append("&endtime=").append(endtime).append("");
					Str.append("&dr_branch_id=").append(dr_branch_id).append("&brand_id=").append(brand_id).append("&include_inactive_exe=" + include_inactive_exe + "' target='_blank'>");
					Str.append(total_exchange).append("</a></b></td>");
					// *total_model_exchange_perc
					Str.append("<td><b>");
					Str.append(total_exchange_perc).append("%").append("</b></td>");
				}
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
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id=model_id"
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

	public String PopulateSob(String soe_id, String comp_id, HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try
		{
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND soetrans_soe_id IN (" + soe_id + ")";
			}
			StrSql += " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("SOB===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			sb.append("<select name='dr_sob' id='dr_sob' multiple='multiple' class='form-control multiselect-dropdown'>");
			while (crs.next()) {
				sb.append("<option value=").append(crs.getString("sob_id")).append("");
				sb.append(ArrSelectdrop(crs.getInt("sob_id"), sob_ids));
				sb.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			sb.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb.toString();
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
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
		Str.append("<select name='dr_orderby' class='form-control' id='dr_orderby'>");
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
		Str.append("<option value=exchange").append(StrSelectdrop("exchange", dr_orderby)).append(">Exchange</option>\n");
		if (include_preowned.equals("1")) {
			Str.append("<option value=preownedenquiry").append(StrSelectdrop("preownedenquiry", dr_orderby)).append(">Pre-Owned Enquiry</option>\n");
			Str.append("<option value=evaluation").append(StrSelectdrop("evaluation", dr_orderby)).append(">Evaluation</option>\n");
		}
		Str.append("</select>");
		return Str.toString();
	}
	//

}
