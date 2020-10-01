package axela.axelaauto_app;
// modified - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.tomcat.util.codec.binary.Base64;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Report_Monitoring_Board extends Connect {

	public String StrSql = "";
	public String start_time = "";
	public String end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_uuid = "";
	public String emp_id = "0", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids, soe_ids;
	public String team_id = "0", exe_id = "0";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String ExeAccess = "";
	public String targetenquirytodate = "";
	public String targetenquiryfromdate = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrTeam = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	// public String SearchURL = "app-report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String include_inactive_exe = "0", include_preowned = "";

	public int total_retailtarget = 0;
	public int total_enquirytarget = 0;
	public int total_enquiryopen = 0;
	public int total_enquiryfresh = 0;
	public int total_enquirylost = 0;
	public int total_soretail = 0;
	public int total_sodelivered = 0;
	public String total_soretail_perc = "0";
	public int total_pendingenquiry = 0;
	public int total_pendingbooking = 0;
	public String total_pendingbooking_perc = "0";
	public int total_pendingdelivery = 0;
	public int total_cancellation = 0;
	public int total_testdrives = 0;
	public String total_testdrives_perc = "0";
	public int total_kpitestdrives = 0;
	public int total_homevisit = 0;
	public String total_homevisit_perc = "0";
	public int total_kpihomevisit = 0;
	public int total_mga_sales = 0;
	public int total_maruti_insurance = 0;
	public int total_extwarranty = 0;
	public int total_fincases = 0;
	public int total_preowned = 0;
	public String total_preowned_perc = "0";
	public int total_exchange = 0;
	public String total_exchange_perc = "0";
	public int total_evaluation = 0;
	public String total_evaluation_perc = "0";
	public String access = "", emp_all_exe = "";
	public String fromdate = "", starttime = "";
	public String todate = "", endtime = "";
	public String brand_id = "0";
	public String region_id = "0";
	public String executive_id = "0";
	public String SearchURL = "app-report-monitoring-board.jsp?filter=yes&";
	public String filter = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			filter = PadQuotes(request.getParameter("filter"));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				// emp_all_exe = GetSession("emp_all_exe", request);
				access = ReturnPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request);
				if (access.equals("0")) {
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				GetValues(request, response);
				if (todate.equals("") || fromdate.equals("")) {

					todate = ToLongDate(kknow());
					int month = Integer.parseInt(SplitMonth(todate));
					int year = Integer.parseInt(SplitYear(todate));
					int days = DaysInMonth(year, month);
					todate = DateToShortDate(kknow());
					fromdate = "01" + todate.substring(2, 10);
					todate = days + todate.substring(2, 10);
					starttime = fromdate;
					starttime = ConvertShortDateToStr(fromdate);
					endtime = ConvertShortDateToStr(todate);
				}
				else
				{
					starttime = ConvertShortDateToStr(fromdate);
					endtime = ConvertShortDateToStr(todate);
				}
				// if (!exe_id.equals("0")) {
				// StrSearch = " AND emp_id IN (" + exe_id + ")";
				// }
				StrSearch = BranchAccess.replace("branch_id", "team_branch_id");

				if (!brand_id.equals("0") && branch_id.equals("0")) {
					branch_id = ReturnBranchids(brand_id, comp_id);
				}
				if (!region_id.equals("0")) {
					StrSearch += " AND branch_region_id IN (" + region_id + ") ";
				}
				if (!branch_id.equals("0")) {
					StrSearch = StrSearch + " AND  emp_branch_id IN (" + branch_id + ")";
				}
				if (!team_id.equals("0")) {
					StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					// + " AND team_branch_id IN (" + branch_id + ")";
				}
				if (!executive_id.equals("0")) {
					StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
				}
				StrSearch += ExeAccess;
				// if (!filter.equals("yes")) {
				StrHTML = ListMonitorBoard(request, response, comp_id);
				// } else {
				// AppendQueryFilter(request, response);
				// AppendQueryFilter(request, response);
				// }

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===App==" + this.getClass().getName());
			SOPError("Axelaauto===App== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		todate = PadQuotes(request.getParameter("txt_to_date"));
		fromdate = PadQuotes(request.getParameter("txt_from_date"));
		brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
		region_id = CNumeric(PadQuotes(request.getParameter("dr_region")));
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		executive_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		team_id = CNumeric(PadQuotes(request.getParameter("dr_team")));

		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		include_preowned = PadQuotes(request.getParameter("chk_include_preowned"));

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

	public String ListMonitorBoard(HttpServletRequest request, HttpServletResponse response, String comp_id) {

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

		// team fields End

		StringBuilder Str = new StringBuilder();
		String tblTargetGroupby = "", tblEnqGroupby = "", tblEnqFollowupGroupby = "", tblSoGroupby = "", tblTestDriveGroupby = "", tblPreOnwedGroupby = "";
		String mainGroupBy = "", mainOrderBy = "", tblFilterJoin = "";
		String tbljoin = "";
		String empactivefilter = "";
		if (!team_id.equals("0") || include_inactive_exe.equals("0")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe dupteamexe ON dupteamexe.teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team dupteam ON dupteam.team_id = dupteamexe.teamtrans_team_id"
					+ " AND dupteam.team_active =1 "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		} else if ((!branch_id.equals("0") || !brand_id.equals("0") || !region_id.equals("0"))) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		} else if (!BranchAccess.equals("0")) {
			tbljoin = " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch dupbranch ON dupbranch.branch_id = emp_branch_id";
		}

		if (include_inactive_exe.equals("0")) {
			empactivefilter += " AND emp_active = 1";
		}

		tblTargetGroupby = " target_emp_id";
		tblEnqGroupby = " enquiry_emp_id";
		tblEnqFollowupGroupby = " enquiry_emp_id";
		tblSoGroupby = " so_emp_id";
		tblTestDriveGroupby = " enquiry_emp_id";
		tblPreOnwedGroupby = " preowned_sales_emp_id";
		mainGroupBy = " mainemp.emp_id";
		mainOrderBy = " mainemp.emp_name";
		tblFilterJoin = " mainemp.emp_id";

		StrSql = " SELECT  COALESCE (mainemp.emp_id,'') AS emp_id, COALESCE (mainemp.emp_name,'') AS emp_name,"
				+ " COALESCE(mainemp.emp_ref_no,'') AS emp_ref_no, mainemp.emp_active,";
		if ((!team_id.equals("0")) || include_inactive_exe.equals("0")) {
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
		if ((!team_id.equals("0")) || include_inactive_exe.equals("0")) {
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

		if (!team_id.equals("0")) {
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
				+ StrModel.replace("model_id", "enquiry_model_id")
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
				+ StrModel.replace("model_id", "item_model_id")
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
				+ StrModel.replace("model_id", "enquiry_model_id")
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
				+ StrModel.replace("model_id", "enquiry_model_id")
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

		if (include_inactive_exe.equals("0")) {
			StrSql += " AND team_active = '1'";
		}

		StrSql += " AND branch_active = '1'";
		if (!include_inactive_exe.equals("1")) {
			StrSql += " AND emp_active = 1 ";
		}

		StrSql += StrSearch
				.replace("emp_id", "mainemp.emp_id")
				.replace("team_id", "mainteam.team_id")
				.replace("emp_branch_id", "mainbranch.branch_id")
				.replace("branch_region_id", "mainregion.region_id")
				.replace("branch_brand_id", "mainbrand.brand_id")
				+ " GROUP BY " + mainGroupBy
				+ " ";
		StrSql += " ORDER BY " + mainOrderBy + "";
		SOP("Strsql==mainquery==" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					count++;
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

				}

				Str.append("<table class=\"table table-hover table-light\">\n")
						.append("<br><br>\n")
						.append("<tbody>")

						.append("<tr>")
						.append("<td><b>\n").append("RT :").append("</b></td>\n")
						.append("<td align=\"right\"><b>").append(total_retailtarget).append("</b></td>\n")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("ET :").append("</b></td>\n")
						.append("<td align=\"right\"><b>").append(total_enquirytarget).append("</b></td>\n")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Open Enquiry  :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "openenquiry") + "'>")
						.append(total_enquiryopen + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Fresh Enquiry :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "freshenquiry") + "'>")
						.append(total_enquiryfresh + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Lost Enquiry :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "lostenquiry") + "'>")
						.append(total_enquirylost + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Pending Enquiry :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "pendingenquiry") + "'>")
						.append(total_pendingenquiry + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Retail :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesordersoretail") + "'>")
						.append(total_soretail + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Retail % :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("")
						.append(total_soretail_perc + "")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Del :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesordersodelivered") + "'>")
						.append(total_sodelivered + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Booking :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderbooking") + "'>")
						.append(total_pendingbooking + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Booking % :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("")
						.append(total_pendingbooking_perc + "")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Pending Booking :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderpendingbooking") + "'>")
						.append(total_pendingdelivery + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Cal :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesordercancellation") + "'>")
						.append(total_cancellation + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("TD :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "testdrives") + "'>")
						.append(total_testdrives + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("KPI TD :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "kpitestdrives") + "'>")
						.append(total_kpitestdrives + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("TD% :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("")
						.append(total_testdrives_perc + "")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Home Visit :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "enquiryhomevisit") + "'>")
						.append(total_homevisit + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("KPI Home Visit :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "enquirykpihomevisit") + "'>")
						.append(total_kpihomevisit + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Home Visit % :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("")
						.append(total_homevisit_perc + "")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Access :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderaccessamt") + "'>")
						.append(total_mga_sales + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Ins :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderinsurcount") + "'>")
						.append(total_maruti_insurance + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("EW :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderewcount") + "'>")
						.append(total_extwarranty + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Fin Cases :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderfincasecount") + "'>")
						.append(total_fincases + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Exe :").append("</b></td>\n")
						.append("<td align=\"right\"><b>")
						.append("<a href='" + AppendQueryFilter(request, response, "salesorderexchangecount") + "'>")
						.append(total_exchange + "</a>")
						.append("</b><td>")
						.append("</tr>\n");

				Str.append("<tr>")
						.append("<td><b>\n").append("Exe % :").append("</b></td>\n")
						.append("<td align=\"right\"><b>").append(total_exchange_perc).append("</b></td>\n")
						.append("</tr>\n");

				if (include_preowned.equals("1")) {
					Str.append("<tr>")
							.append("<td><b>\n").append("PE :").append("</b></td>\n")
							.append("<td align=\"right\"><b>")
							.append("<a href='" + AppendQueryFilter(request, response, "enquirypreowned") + "'>")
							.append(total_preowned + "</a>")
							.append("</b><td>")
							.append("</tr>\n");

					Str.append("<tr>")
							.append("<td><b>\n").append("PE % :").append("</b></td>\n")
							.append("<td align=\"right\"><b>").append(total_preowned_perc).append("</b></td>\n")
							.append("</tr>\n");

					Str.append("<tr>")
							.append("<td><b>\n").append("Eval :").append("</b></td>\n")
							.append("<td align=\"right\"><b>")
							.append("<a href='" + AppendQueryFilter(request, response, "enquiryevaluation") + "'>")
							.append(total_evaluation + "</a>")
							.append("</b><td>")
							.append("</tr>\n");

					Str.append("<tr>")
							.append("<td><b>\n").append("Eval % :").append("</b></td>\n")
							.append("<td align=\"right\"><b>").append(total_evaluation_perc).append("</b></td>\n")
							.append("</tr>\n");

				}

				Str.append("</tbody>")
						.append("</table>");

			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Axelaauto-App== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales='1'"
					+ ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("PopulateSalesExecutives-----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Axelaauto-App==" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrincipal(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id = 1"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRegion(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ BranchAccess;
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("region_id")).append("");
				Str.append(StrSelectdrop(crs.getString("region_id"), region_id));
				Str.append(">").append(crs.getString("region_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;

			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("0")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTeam(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE 1=1 ";
			if (!branch_id.equals("0")) {
				StrSql += " AND team_branch_id  IN (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "team_branch_id")
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("StrSQl===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = '1' "
					+ " AND emp_sales = '1'"
					+ ExeAccess;
			if (!branch_id.equals("0")) {
				StrSql = StrSql + " and emp_branch_id IN (" + branch_id + ")";
			}

			if (!team_id.equals("0")) {
				StrSql = StrSql + " and teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String AppendQueryFilter(HttpServletRequest request, HttpServletResponse response, String filtertargetvalue) {

		try {
			String searchdate = "", filtersodate = "";
			// comp_id = CNumeric(GetSession("comp_id", request));
			StrSearch = "";
			if (!comp_id.equals("0")) {
				String filterstarttime = starttime;
				String filterendtime = endtime;
				String filterbrand_id = brand_id;
				String filterregion_id = region_id;
				String filterbranch_id = branch_id;
				String filterteam_id = team_id;
				String filteremp_id = exe_id;
				String filterinactiveexe = include_inactive_exe;
				// *** searchdate
				if (!filterstarttime.equals("") && !filterendtime.equals("")) {
					searchdate = " AND SUBSTR(searchdatestart, 1, 8) >= SUBSTR(" + filterstarttime + ", 1, 8) AND SUBSTR(searchdateend, 1, 8) <= SUBSTR(" + filterendtime + ", 1, 8) ";
					// StrSearch = "  SUBSTR(so_date, 1, 8) >= SUBSTR('" + filterstarttime + "', 1, 8) AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + filterendtime + "', 1, 8) ";
				}

				// MAIN FILTER START
				if (filterinactiveexe.equals("0")) {
					StrSearch += " AND emp_active = 1";
				}
				if (!filterbrand_id.equals("0")) {
					StrSearch += " AND branch_brand_id IN (" + filterbrand_id + ")";
				}
				if (!filterregion_id.equals("0")) {
					StrSearch += " AND branch_region_id IN (" + filterregion_id + ")";
				}
				if (!filterbranch_id.equals("0")) {
					StrSearch += " AND branch_id IN (" + filterbranch_id + ")";
				}
				if (!filterteam_id.equals("0")) {
					StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_team_id IN (" + filterteam_id + "))";
				}
				if (!filteremp_id.equals("0")) {
					StrSearch += " AND emp_id IN (" + filteremp_id + ")";
				}
				if (filterinactiveexe.equals("0")) {
					StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE 1=1)";
				}

				// MAIN FILTER END

				// *** /enquiry Strat
				if (filtertargetvalue.contains("enquiry")) {

					if (filtertargetvalue.equals("openenquiry")) {
						StrSearch = StrSearch + " AND enquiry_status_id = 1"
								+ " AND SUBSTR(enquiry_date,1,8) < SUBSTR('" + filterstarttime + "',1,8) ";
					}
					// enquiryfresh
					else if (filtertargetvalue.equals("freshenquiry")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch;
					}
					// enquirylost
					else if (filtertargetvalue.equals("lostenquiry")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date") + StrSearch
								+ " AND (enquiry_status_id=3 OR enquiry_status_id=4)";
					}
					// pendingenquiry
					else if (filtertargetvalue.equals("pendingenquiry")) {
						StrSearch = StrSearch + " AND enquiry_status_id = 1";

					}
					else if (filtertargetvalue.equals("enquiryhomevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT followup_enquiry_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1 = 1 "
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time")
										.replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					// kpihomevisit
					else if (filtertargetvalue.equals("enquirykpihomevisit")) {
						StrSearch = StrSearch + " AND enquiry_id IN (SELECT DISTINCT(followup_enquiry_id) "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
								+ " WHERE 1=1 "
								+ searchdate.replaceAll("searchdatestart", "followup_followup_time").replaceAll("searchdateend", "followup_followup_time")
								+ " AND followup_feedbacktype_id = 9 )";
					}
					// enquiryevaluation
					else if (filtertargetvalue.equals("enquiryevaluation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ StrSearch + " AND enquiry_id IN (SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry" + ")"
								+ " AND enquiry_evaluation = 1 ";
					}
					// enquirypreowned
					else if (filtertargetvalue.equals("enquirypreowned")) {
						StrSearch += " AND enquiry_id IN (SELECT preowned_enquiry_id FROM " + compdb(comp_id) + "axela_preowned"
								+ " WHERE 1=1"
								+ " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
						if (!emp_id.equals("")) {
							StrSearch += " AND preowned_sales_emp_id IN (" + emp_id + ")";
						}
						StrSearch += " )";
					}

					StrSearch += " AND emp_branch_id = enquiry_branch_id";

				}
				// Enquiry end

				// *** testdrive Start
				if (filtertargetvalue.contains("testdrives")) {

					if (filtertargetvalue.equals("testdrives")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "testdrive_time")
								.replaceAll("searchdateend", "testdrive_time")
								+ StrSearch + " AND testdrive_fb_taken = 1";
					}

					if (filtertargetvalue.equals("kpitestdrives")) {
						// StrSearch = StrSearch + " AND testdrive_fb_taken = 1";
						// StrSearch = StrSearch + searchdate.replaceAll("searchdatestart", "testdrive_time")
						// .replaceAll("searchdateend", "testdrive_time");

						StrSearch += " AND enquiry_id IN ( SELECT DISTINCT testdrive_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
								+ " WHERE 1 = 1"
								+ " AND testdrive_fb_taken = 1"
								+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
								+ " GROUP BY testdrive_enquiry_id )";

					}
					// enquiryevaluation
					else if (filtertargetvalue.equals("enquiryevaluation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "enquiry_date").replaceAll("searchdateend", "enquiry_date")
								+ StrSearch + " AND enquiry_id IN (SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry" + ")"
								+ " AND enquiry_evaluation = 1 ";
					}
					// enquirypreowned
					else if (filtertargetvalue.equals("enquirypreowned")) {
						StrSearch += " AND enquiry_id IN (SELECT preowned_enquiry_id FROM " + compdb(comp_id) + "axela_preowned"
								+ " WHERE 1=1"
								+ " AND SUBSTR(enquiry_date,1,8)>=SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(enquiry_date,1,8)<=SUBSTR('" + endtime + "',1,8)";
						if (!emp_id.equals("")) {
							StrSearch += " AND preowned_sales_emp_id IN (" + emp_id + ")";
						}
						StrSearch += " )";
					}

					StrSearch += " AND emp_branch_id = enquiry_branch_id";

				}

				// *** testdrive End

				// *** salesorder
				if (filtertargetvalue.contains("salesorder")) {

					// soretail
					if (filtertargetvalue.equals("salesordersoretail")) {
						StrSearch = StrSearch + " AND so_active='1' "
								+ " AND (SUBSTR(so_retail_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_retail_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// sodelivered
					if (filtertargetvalue.equals("salesordersodelivered")) {
						StrSearch = StrSearch + " AND so_active='1' "
								+ " AND (SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8) "
								+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8))";
					}
					// pendingbooking
					else if (filtertargetvalue.equals("salesorderbooking")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_date").replaceAll("searchdateend", "so_date")
								+ StrSearch + " AND so_active='1'";
					}

					// pendingdelivery
					else if (filtertargetvalue.equals("salesorderpendingbooking")) {
						StrSearch = StrSearch + " AND so_active = 1 AND so_delivered_date = '' AND so_retail_date = '' ";
					}

					// cancellation
					else if (filtertargetvalue.equals("salesordercancellation")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_cancel_date").replaceAll("searchdateend", "so_cancel_date")
								+ StrSearch + " AND so_active='0'";
						// + " AND so_delivered_date='' ";
					}
					// mgaamount
					else if (filtertargetvalue.equals("salesorderaccessamt")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_mga_amount!=0";
					}
					// marutiinsur
					else if (filtertargetvalue.equals("salesorderinsurcount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_insur_amount > 0";
					}
					// extendedwarranty
					else if (filtertargetvalue.equals("salesorderewcount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_ew_amount > 0 ";
					}
					// fincases
					else if (filtertargetvalue.equals("salesorderfincasecount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active = '1' AND so_finance_amt > 0  AND so_fintype_id = 1 ";
					}
					// exchange
					else if (filtertargetvalue.equals("salesorderexchangecount")) {
						StrSearch = searchdate.replaceAll("searchdatestart", "so_retail_date").replaceAll("searchdateend", "so_retail_date")
								+ StrSearch + " AND so_active='1' AND so_exchange_amount > 0";
					}
					StrSearch += " AND emp_branch_id = so_branch_id";

				}
				// // :- targetvalue
				if (filtertargetvalue.contains("enquiry") || filtertargetvalue.equals("kpitestdrives")) {
					StrSearch = "callurlapp-enquiry-list.jsp?filterquery=" + Base64Encoder(StrSearch.toString());
				} else if (filtertargetvalue.contains("testdrives")) {
					StrSearch = "callurlapp-testdrive-list.jsp?filterquery=" + Base64Encoder(StrSearch.toString());
				} else if (filtertargetvalue.contains("salesorder")) {
					StrSearch = "callurlapp-veh-salesorder-list.jsp?filterquery=" + Base64Encoder(StrSearch.toString());
				}
				SOP("StrSearch====" + StrSearch);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return StrSearch.toString();

	}
	public String Base64Encoder(String password) throws IOException {
		return new String(Base64.encodeBase64(password.getBytes("ISO-8859-1")));
	}
}
