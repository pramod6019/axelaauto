package axela.sales;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_DayWise_BookingTracker extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "", BranchAccess = "";
	public String branch_name = "";
	public String msg = "";
	public String so_id = "";
	public String go = "";
	public String model_id = "", brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String order_by = "";
	public String vehstock_allocate = "0";
	public String fueltype_id = "", soe_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, fueltype_ids, soe_ids;
	public String SoQueryJoin = "";
	public String EnqQueryJoin = "";
	public String dr_branch_id = "0", executive_id = "0";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public int curryear = 0;
	public String year = "", month = "", dr_from_year = "";
	String cfbooking = "", cfsoretail = "", cfselffinance = "", cfnofinance = "", cfallocated = "";
	String cflogin = "", cfwaitingforapproval = "", cfdoreceived = "", cfdocpending = "", cfdelivered = "", cfcancel = "";
	public int cftotal = 0;
	public String filter = "", StrFilter = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			BranchAccess = GetSession("BranchAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
			filter = PadQuotes(request.getParameter("filter"));

			if (branch_id.equals("0")) {
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			} else {
				dr_branch_id = branch_id;
			}
			if (filter.equals("yes")) {
				SalesOrderDetails(request, response);
			}
			GetValues(request, response);
			if (!team_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				mischeck.branch_id = branch_id;
				StrSearch = StrSearch + " AND team_id IN (" + team_id + ")";

				SoQueryJoin = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
			}
			if (!exe_id.equals("")) {
				StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + " )";
			}
			if (!model_id.equals("")) {
				StrSearch = StrSearch + " AND item_model_id IN(" + model_id + ")";

			}
			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ") ";
			}
			if (!branch_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
			}
			if (!branch_id.equals("") || !region_id.equals("") || !brand_id.equals("")) {

				SoQueryJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id ";
			}
			if (!fueltype_id.equals("")) {
				StrSearch += " AND item_fueltype_id IN (" + fueltype_id + ")";
			}

			if (!soe_id.equals("")) {
				StrSearch += " AND enquiry_soe_id IN (" + soe_id + ")";
			}

			if (!fueltype_id.equals("") || !model_id.equals("")) {
				SoQueryJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
			}

			go = PadQuotes(request.getParameter("submit_button"));

			try {
				if (go.equals("Go")) {
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListDayWiseBookingTracker();
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");

		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");

		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");

		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");

		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");

		fueltype_id = RetrunSelectArrVal(request, "dr_fuel_id");
		fueltype_ids = request.getParameterValues("dr_fuel_id");

		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");

		year = PadQuotes(request.getParameter("dr_year"));
		month = PadQuotes(request.getParameter("dr_month"));
		if (year.equals("")) {
			year = ToLongDate(kknow()).substring(0, 4);
		}
		if (month.equals("")) {
			month = ToLongDate(kknow()).substring(4, 6);
		}

	}
	protected void CheckForm() {
		if (year.equals("0")) {
			msg = msg + "<br>Select From Year!";
		}
		if (month.equals("00")) {
			msg = msg + "<br>Select From Month!";
		}
	}

	public String ListDayWiseBookingTracker() {

		StringBuilder Str = new StringBuilder();
		int enquiry_total = 0;
		int tetdrivedone_total = 0;
		int tetdrivenotdone_total = 0;
		int homevisitdonecount_total = 0;
		int homevisitnotdonecount_total = 0;
		int meetingdonecount_total = 0;
		int meetingnotdonecount_total = 0;
		int salesorders_total = 0;
		int cancelled_total = 0;
		int delivered_total = 0;
		int soretail_total = 0;
		int total_inhand = 0;
		int allocated_total = 0;
		int docpending_total = 0;
		int sleffinance_total = 0;
		int nofinance_total = 0;
		int login_total = 0;
		int doreceived_total = 0;
		int waitingforapproval_total = 0;
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			calendar.set(Calendar.YEAR, Integer.parseInt(year));
			int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

			String predaysStrSql = "";

			for (int i = 01; i <= days; i++) {
				if (i == 1) {
					predaysStrSql = " SELECT " + year + month + "0" + i + " AS calday ";
				} else if (i < 10) {
					predaysStrSql += " UNION SELECT " + year + month + "0" + i + " AS calday ";
				} else {
					predaysStrSql += " UNION SELECT " + year + month + i + " AS calday ";
				}
			}
			// SOP("predaysStrSql==" + predaysStrSql);

			StrSql = " SELECT calday,"
					+ " COALESCE (enquirycount, 0) AS enquirycount,"
					+ " COALESCE (testdrivedonecount, 0) AS testdrivedonecount,"
					+ " COALESCE (testdrivenotdonecount, 0) AS testdrivenotdonecount,"
					+ " COALESCE (homevisitdonecount, 0) AS homevisitdonecount,"
					+ " COALESCE (homevisitnotdonecount, 0) AS homevisitnotdonecount,"
					+ " COALESCE (meetingdonecount, 0) AS meetingdonecount,"
					+ " COALESCE (meetingnotdonecount, 0) AS meetingnotdonecount,"
					+ " COALESCE (cfbooking, 0) AS cfbooking,"
					+ " COALESCE (cfsoretail, 0) AS cfsoretail,"
					+ " COALESCE (cfallocated, 0) AS cfallocated,"
					+ " COALESCE (cfselffinance, 0) AS cfselffinance,"
					+ " COALESCE (cfnofinance, 0) AS cfnofinance,"
					+ " COALESCE (cfdocpending, 0) AS cfdocpending,"
					+ " COALESCE (cflogin, 0) AS cflogin,"
					+ " COALESCE (cfwaitingforapproval, 0) AS cfwaitingforapproval,"
					+ " COALESCE (cfdoreceived, 0) AS cfdoreceived,"
					+ " COALESCE (cfdelivered, 0) AS cfdelivered,"
					+ " COALESCE (cfcancel, 0) AS cfcancel,"
					+ " COALESCE (socount, 0) AS salesorders,"
					+ " COALESCE (soretail, 0) AS soretail, "
					+ " COALESCE (allocated, 0) AS allocated, "
					+ " COALESCE (selffinance, 0) AS selffinance, "
					+ " COALESCE (nofinance, 0) AS nofinance, "
					+ " COALESCE (docpending, 0) AS docpending, "
					+ " COALESCE (login, 0) AS login, "
					+ " COALESCE (waitingforapproval, 0) AS waitingforapproval, "
					+ " COALESCE (doreceived, 0) AS doreceived, "
					+ " COALESCE (cancelled, 0) AS cancelled,"
					+ " COALESCE (delivered, 0) AS delivered"
					+ " FROM ( "
					+ predaysStrSql
					+ " ) AS cal "
					+ " LEFT JOIN ( "
					+ " SELECT"
					+ " COUNT(so_id) AS socount, "
					+ " SUM(IF(so_active = 1 AND so_retail_date != '', 1, 0)) AS soretail,"
					+ " SUM(IF(so_active = 1 AND so_vehstock_id !=0 AND so_stockallocation_time != '' AND so_delivered_date = '' AND so_retail_date = '', 1 ,0)) AS allocated,"
					+ " SUM(IF(so_finstatus_id = 9 AND so_retail_date = '', 1, 0)) AS selffinance,"
					+ " SUM(IF(so_finstatus_id = 6 AND so_retail_date = '', 1, 0)) AS nofinance,"
					+ " SUM(IF(so_finstatus_id = 10 AND so_retail_date = '', 1, 0)) AS docpending,"
					+ " SUM(IF(so_finstatus_id = 1 AND so_retail_date = '', 1, 0)) AS login,"
					+ " SUM(IF(so_finstatus_id = 2 AND so_retail_date = '', 1, 0)) AS waitingforapproval,"
					+ " SUM(IF(so_finstatus_id = 12 AND so_retail_date = '', 1, 0)) AS doreceived,"
					+ " SUM(IF(so_active = 0, 1, 0)) AS cancelled,"
					+ " SUM(IF(so_active = 1 AND so_delivered_date != '', 1, 0)) AS delivered,"
					+ " so_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ SoQueryJoin; // for filter joins;
			if (!soe_id.equals("")) {
				StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id";
			}
			StrSql += " WHERE 1 = 1 "
					+ " AND SUBSTR(so_date, 1, 6) = "
					+ year
					+ month
					+ StrSearch // for filters
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY SUBSTR(so_date, 1, 8) "
					+ " ) AS tblso ON SUBSTR(tblso.so_date, 1, 8) = cal.calday "

					// enq count
					+ " LEFT JOIN ( "
					+ " SELECT"
					+ " COUNT(enquiry_id) AS enquirycount, "
					+ " enquiry_date "
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry "
					+ SoQueryJoin.replace("so_emp_id", "enquiry_emp_id").replace("so_item_id", "enquiry_item_id")
							.replace("so_branch_id", "enquiry_branch_id")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 6) = "
					+ year
					+ month
					+ StrSearch.replace("so_emp_id", "enquiry_emp_id") // for filters
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY SUBSTR(enquiry_date, 1, 8) "
					+ " ) AS tblenq ON SUBSTR(tblenq.enquiry_date, 1, 8) = cal.calday "

					// testdrive join
					+ " LEFT JOIN ( "
					+ " SELECT"
					// testdrivedonecount
					+ " COUNT( DISTINCT (CASE WHEN testdrive_id IS NOT NULL"
					+ " AND testdrive_fb_taken = 1 THEN enquiry_id END ))"
					+ " AS testdrivedonecount,"

					// testdrivenotdonecount
					+ " COUNT( DISTINCT (CASE WHEN testdrive_id IS NULL"
					+ " THEN enquiry_id END ))"
					+ " AS testdrivenotdonecount,"

					+ " enquiry_date"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_sales_testdrive  ON testdrive_enquiry_id = enquiry_id "
					+ SoQueryJoin.replace("so_emp_id", "enquiry_emp_id").replace("so_item_id", "enquiry_item_id")
							.replace("so_branch_id", "enquiry_branch_id")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 6) = "
					+ year
					+ month
					+ " AND enquiry_status_id != 2"
					+ StrSearch.replace("so_emp_id", "enquiry_emp_id") // for filters
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY  SUBSTR(enquiry_date, 1, 8) "
					+ " ) AS tbltestdrive ON SUBSTR(tbltestdrive.enquiry_date, 1, 8) = cal.calday"

					// homevisit join
					+ " LEFT JOIN ( "
					+ " SELECT"

					// homevisitdonecount
					+ " COUNT( DISTINCT (CASE WHEN followup_id IS NOT NULL THEN enquiry_id END ))"
					+ " AS homevisitdonecount,"

					// homevisitnotdonecount
					+ " COUNT( DISTINCT (CASE WHEN followup_id IS NULL"
					+ " THEN enquiry_id END ))"
					+ " AS homevisitnotdonecount,"

					+ " enquiry_date, followup_followup_time "
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
					+ " AND followup_feedbacktype_id = 9"
					+ SoQueryJoin.replace("so_emp_id", "enquiry_emp_id").replace("so_item_id", "enquiry_item_id")
							.replace("so_branch_id", "enquiry_branch_id")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 6) = "
					+ year
					+ month
					+ " AND enquiry_status_id != 2"
					+ StrSearch.replace("so_emp_id", "enquiry_emp_id") // for filters
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY SUBSTR(enquiry_date, 1, 8) "
					+ " ) AS tblhomevisit ON SUBSTR(tblhomevisit.enquiry_date, 1, 8) = cal.calday"

					// Meeting join

					+ " LEFT JOIN ( "
					+ " SELECT"
					// AS meetingdonecountdonecount
					+ " COUNT( DISTINCT (CASE WHEN followup_id IS NOT NULL THEN enquiry_id END ))"
					+ " AS meetingdonecount,"

					// AS meetingdonecount
					+ " COUNT( DISTINCT (CASE WHEN followup_id IS NULL"
					+ " THEN enquiry_id END ))"
					+ " AS meetingnotdonecount,"

					+ " enquiry_date, followup_followup_time "
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry"
					+ " LEFT JOIN "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
					+ " AND followup_feedbacktype_id = 5"
					+ SoQueryJoin.replace("so_emp_id", "enquiry_emp_id").replace("so_item_id", "enquiry_item_id")
							.replace("so_branch_id", "enquiry_branch_id")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 6) = "
					+ year
					+ month
					+ " AND enquiry_status_id != 2"
					+ StrSearch.replace("so_emp_id", "enquiry_emp_id") // for filters
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY SUBSTR(enquiry_date, 1, 8) "
					+ " ) AS tblmeeting ON SUBSTR(tblmeeting.enquiry_date, 1, 8) = cal.calday"

					// sojoin
					+ " LEFT JOIN( SELECT"
					+ " SUM(IF(so_active = 1, 1, 0)) + SUM(IF(SUBSTR(so_cancel_date,1,8) >= '"
					+ year
					+ month
					+ "01"
					+ "' AND so_active = 0, 1, 0)) AS cfbooking,"
					+ " SUM(IF(so_retail_date != '' AND so_active = 1, 1, 0)) AS cfsoretail,"
					+ " SUM(IF(so_vehstock_id !=0 AND so_active = 1 AND so_vehstock_id !=0 AND so_stockallocation_time != '' AND so_delivered_date = '' AND so_retail_date = '' , 1, 0)) AS cfallocated,"
					+ " SUM(IF(so_finstatus_id = 9 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cfselffinance,"
					+ " SUM(IF(so_finstatus_id = 6 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cfnofinance,"
					+ " SUM(IF(so_finstatus_id = 10 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cfdocpending,"
					+ " SUM(IF(so_finstatus_id = 1 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cflogin,"
					+ " SUM(IF(so_finstatus_id = 2 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cfwaitingforapproval,"
					+ " SUM(IF(so_finstatus_id = 12 AND so_active = 1 AND so_retail_date = '', 1, 0)) AS cfdoreceived,"
					+ " SUM(IF(SUBSTR(so_cancel_date,1,8) >= '" + year + month + "01" + "' AND so_active = 0, 1, 0)) AS cfcancel,"
					+ " SUM(IF(so_delivered_date != '' AND so_active = 1, 1, 0)) AS cfdelivered"

					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ SoQueryJoin; // for filter joins;
			if (!soe_id.equals("")) {
				StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id";
			}
			StrSql += " WHERE 1 = 1 "
					+ " AND SUBSTR(so_date, 1, 8) < '" + year + month + "01" + "'"
					+ " AND (SUBSTR(so_delivered_date,1,8) > '" + year + month + "01" + "'"
					+ " OR so_delivered_date= '')"
					+ StrSearch
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ " ) As tblcfbooking ON 1 = 1"
					+ " WHERE 1 = 1 "
					+ " GROUP BY calday "
					+ " ORDER BY calday ";

			// SOP("StrSql----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered table-responsive \">\n");
				Str.append("<table class=\"table table-hover table-bordered table-responsive \" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<th >").append("Booking").append("</th>\n");
				Str.append("<th >").append("CF").append("</th>\n");
				while (crs.next()) {
					Str.append("<th data-hide=\"phone\">").append(crs.getString("calday").substring(6, 8)).append("</th>\n");
				}
				Str.append("<th>").append("Total").append("</th>\n");
				Str.append("</thead>\n");
				crs.beforeFirst();

				while (crs.next()) {
					cfbooking = crs.getString("cfbooking");
					cfsoretail = crs.getString("cfsoretail");
					cfallocated = crs.getString("cfallocated");
					cfselffinance = crs.getString("cfselffinance");
					cfnofinance = crs.getString("cfnofinance");
					cfdocpending = crs.getString("cfdocpending");
					cflogin = crs.getString("cflogin");
					cfwaitingforapproval = crs.getString("cfwaitingforapproval");
					cfdoreceived = crs.getString("cfdoreceived");
					cfcancel = crs.getString("cfcancel");
					cfdelivered = crs.getString("cfdelivered");

					cftotal = crs.getInt("cfbooking") - crs.getInt("cfsoretail") - crs.getInt("cfcancel");
				}

				// Enquiry Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Enquiry Count: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("enquirycount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&enquiry=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday"))
								.append(">").append(crs.getString("enquirycount"))
								.append("</a></td>\n");
					}
					enquiry_total += Integer.parseInt(crs.getString("enquirycount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(enquiry_total)
						.append("</b></td>\n");

				Str.append("</tr>\n");

				// Test Drive Done Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Test Drive Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("testdrivedonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&testdrivedone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday"))
								.append(">").append(crs.getString("testdrivedonecount"))
								.append("</a></td>\n");
					}
					tetdrivedone_total += Integer.parseInt(crs.getString("testdrivedonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(tetdrivedone_total)
						.append("</b></td>\n");

				Str.append("</tr>\n");

				// Test Drive Not Done Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Test Drive Not Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("testdrivenotdonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&testdrivenotdone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday"))
								.append(">").append(crs.getString("testdrivenotdonecount"))
								.append("</a></td>\n");
					}
					tetdrivenotdone_total += Integer.parseInt(crs.getString("testdrivenotdonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(tetdrivenotdone_total)
						.append("</b></td>\n");

				Str.append("</tr>\n");

				// HomevisitDone Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Home Visit Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("homevisitdonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&homevisitdone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("homevisitdonecount"))
								.append("</a></td>\n");
					}
					homevisitdonecount_total += Integer.parseInt(crs.getString("homevisitdonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(homevisitdonecount_total)
						.append("</b></td>\n");

				Str.append("</tr>\n");

				// Home Visit Not Done Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Home Visit Not Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("homevisitnotdonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&homevisitnotdone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("homevisitnotdonecount"))
								.append("</a></td>\n");
					}
					homevisitnotdonecount_total += Integer.parseInt(crs.getString("homevisitnotdonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(homevisitnotdonecount_total)
						.append("</b></td>\n");

				Str.append("</tr>\n");

				// Meeting Done Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Meeting Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("meetingdonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&meetingdone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("meetingdonecount"))
								.append("</a></td>\n");
					}
					meetingdonecount_total += Integer.parseInt(crs.getString("meetingdonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(meetingdonecount_total)
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// Meeting Not Done Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Meeting Not Done: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\" >").append("").append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("meetingnotdonecount").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&meetingnotdone=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("enqteam=").append("yes").append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("meetingnotdonecount"))
								.append("</a></td>\n");
					}
					meetingnotdonecount_total += Integer.parseInt(crs.getString("meetingnotdonecount"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(meetingnotdonecount_total)
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Count
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\"><font color=green><b>").append("SO Count: ").append("</b></font></td>\n");
				if (cfbooking.equals("0")) {
					cfbooking = "";
				}
				Str.append("<td style=\"text-align:right;\" ><font color=green><b>");
				if (cfbooking.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfbooking);
				}
				Str.append("</b></font></td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("salesorders").equals("0")) {
						Str.append("<td style=\"text-align:right;\"><b>").append("&nbsp").append("</b></td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><font color=green><b><a style =\"color: green\"  href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&salesorder=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("salesorders"))
								.append("</a></b></font></td>\n");
					}
					salesorders_total += Integer.parseInt(crs.getString("salesorders"));
				}
				Str.append("<td style=\"text-align:right;\"><font color=green><b>")
						.append(salesorders_total + Integer.parseInt(CNumeric(cfbooking)))
						.append("</font></b></td>\n");

				Str.append("</tr></font></b>\n");

				// SO Retail
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\"> <font color=red><b>").append("Retail: ").append("</b></font></td>\n");
				Str.append("<td style=\"text-align:right;\"> <font color=red><b>");
				if (cfsoretail.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfsoretail);
				}
				Str.append("</b></font></td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("soretail").equals("0")) {
						Str.append("<td style=\"text-align: right;\"><b>").append("&nbsp").append("</b></td>\n");
					} else {
						Str.append("<td style=\"text-align: right;\"><font color=red><b><a style =\"color: red\"  href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&soretail=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("soretail"))
								.append("</a></b></font></font></td>\n");
					}
					soretail_total += Integer.parseInt(crs.getString("soretail"));
				}
				Str.append("<td style=\"text-align: right;\"><b><font color=red><b>")
						.append(soretail_total + Integer.parseInt(CNumeric(cfsoretail)))
						.append("</b></font></font></b></td>\n");
				Str.append("</font></tr>\n");

				// Total Pending
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\"> ").append("Total Pending: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\"> ");
				total_inhand = cftotal;
				if (cftotal == 0) {
					Str.append("");
				}
				else {
					Str.append(cftotal);
				}
				Str.append("</b></font></td>\n");
				crs.beforeFirst();
				while (crs.next()) {

					if ((crs.getInt("salesorders") - crs.getInt("soretail") - crs.getInt("cancelled")) == 0) {
						Str.append("<td style=\"text-align: right;\"><b>").append("&nbsp").append("</b></td>\n");
					} else {
						Str.append("<td style=\"text-align: right;\"><a  href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&bookinginhand=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getInt("salesorders") - crs.getInt("soretail") - crs.getInt("cancelled"))
								.append("</a></td>\n");
					}
					total_inhand += (crs.getInt("salesorders") - crs.getInt("soretail") - crs.getInt("cancelled"));
				}
				Str.append("<td style=\"text-align: right;\"><b>")
						.append(total_inhand)
						.append("</b></td>\n");
				Str.append("</font></tr>\n");

				// Stock allocated
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Allocated: ").append("</td>\n");
				Str.append("<td style=\"text-align:right;\">");
				if (cfallocated.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfallocated);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("allocated").equals("0")) {
						Str.append("<td style=\"text-align: right;\"><b>").append("&nbsp").append("</b></td>\n");
					} else {
						Str.append("<td style=\"text-align: right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&allocated=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("allocated"))
								.append("</a></td>\n");
					}
					allocated_total += Integer.parseInt(crs.getString("allocated"));
				}
				Str.append("<td style=\"text-align: right;\"><b>")
						.append(allocated_total + Integer.parseInt(CNumeric(cfallocated)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// Self finance
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align: left;\">").append("Self Finance: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfselffinance.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfselffinance);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("selffinance").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&selffinance=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("selffinance"))
								.append("</a></td>\n");
					}
					sleffinance_total += Integer.parseInt(crs.getString("selffinance"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(sleffinance_total + Integer.parseInt(CNumeric(cfselffinance)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// No finance
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align: left;\">").append("No Finance: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfnofinance.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfnofinance);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("nofinance").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&nofinance=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("nofinance"))
								.append("</a></td>\n");
					}
					nofinance_total += Integer.parseInt(crs.getString("nofinance"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(nofinance_total + Integer.parseInt(CNumeric(cfnofinance)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Document pending
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Document pending: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfdocpending.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfdocpending);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("docpending").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&docpending=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("docpending"))
								.append("</a></td>\n");
					}
					docpending_total += Integer.parseInt(crs.getString("docpending"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(docpending_total + Integer.parseInt(CNumeric(cfdocpending)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Log in
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Log in: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cflogin.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cflogin);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("login").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&login=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("login"))
								.append("</a></td>\n");
					}
					login_total += Integer.parseInt(crs.getString("login"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(login_total + Integer.parseInt(CNumeric(cflogin)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Waiting for approval
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Waiting for approval: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfwaitingforapproval.equals("0")) {
					Str.append("");
				}
				else {
					Str.append(cfwaitingforapproval);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("waitingforapproval").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&waitingforapproval=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("waitingforapproval"))
								.append("</a></td>\n");
					}
					waitingforapproval_total += Integer.parseInt(crs.getString("waitingforapproval"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(waitingforapproval_total + Integer.parseInt(CNumeric(cfwaitingforapproval)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Do received
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("DO received: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfdoreceived.equals("0"))
				{
					Str.append("");
				}
				else {
					Str.append(cfdoreceived);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("doreceived").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&doreceived=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("doreceived"))
								.append("</a></td>\n");
					}
					doreceived_total += Integer.parseInt(crs.getString("doreceived"));
				}
				Str.append("<td style=\"text-align:right;\"><b>")
						.append(doreceived_total + Integer.parseInt(CNumeric(cfdoreceived)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO Cancelled
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\">").append("Cancelled: ").append("</td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfcancel.equals("0")) {
					Str.append("");
				} else {
					Str.append(cfcancel);
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("cancelled").equals("0")) {
						Str.append("<td style=\"text-align:right;\">").append("&nbsp").append("</td>\n");
					} else {
						Str.append("<td style=\"text-align:right;\"><a href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&cancelled=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("cancelled"))
								.append("</a></td>\n");
					}
					cancelled_total += Integer.parseInt(crs.getString("cancelled"));
				}
				Str.append("<td style=\"text-align:right; \"><b>")
						.append(cancelled_total + Integer.parseInt(CNumeric(cfcancel)))
						.append("</b></td>\n");
				Str.append("</tr>\n");

				// SO delivered
				Str.append("<tr>\n");
				Str.append("<td style=\"text-align:left;\"> <font color=blue><b>").append("Delivered: ").append("</b></td>\n");
				Str.append("<td style=\"text-align: right;\">");
				if (cfdelivered.equals("0")) {
					Str.append("");
				} else {
					Str.append("<font color=blue><b>" + cfdelivered + "</b></font>");
				}
				Str.append("</td>\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("delivered").equals("0")) {
						Str.append("<td style=\"text-align:right; \"><b>").append("&nbsp").append("</b></td>\n");
					} else {
						Str.append("<td style=\"text-align:right; \"><font color=blue><b><a style =\"color: blue\" href=../sales/report-daywise-bookingtracker.jsp?filter=yes")
								.append("&delivered=").append("yes").append("&")
								.append("brand_id=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id).append("&")
								.append("exe_id=").append(exe_id).append("&")
								.append("fueltype_id=").append(fueltype_id).append("&")
								.append("soe_id=").append(soe_id).append("&")
								.append("starttime=").append(crs.getString("calday")).append("&")
								.append(">").append(crs.getString("delivered"))
								.append("</a></b></font></td>\n");
					}
					delivered_total += Integer.parseInt(crs.getString("delivered"));
				}
				Str.append("<td style=\"text-align:right; \"><font color=blue><b>")
						.append(delivered_total + Integer.parseInt(CNumeric(cfdelivered)))
						.append("</b></font></td>\n");
				Str.append("</tr>\n");

				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateSOE() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name  FROM " + compdb(comp_id) + "axela_soe "
					+ " WHERE 1=1 "
					+ " GROUP BY soe_id ";
			// SOP("PopulateTeam query ==== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("soe_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("soe_id"), soe_ids));
				Str.append(">" + (crs.getString("soe_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
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

	public String PopulateExecutive() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'";

			if (!emp_id.equals("1")) {
				StrSql += " " + ExeAccess + "";
			}
			StrSql = StrSql + " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append(" (")
						.append(crs.getString("emp_ref_no")).append(" )").append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String[] fueltype_ids, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(ArrSelectdrop(crs.getInt("fueltype_id"), fueltype_ids));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		for (int i = curryear - 3; i <= curryear; i++) {
			Str.append("<option value=").append(i);
			Str.append(StrSelectdrop(Integer.toString(i), year));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}
	public String PopulateMonth() {
		StringBuilder Str = new StringBuilder();

		Str.append("<option value=00").append(StrSelectdrop("00", month)).append(">Select</option>");
		Str.append("<option value=01").append(StrSelectdrop("01", month)).append(">January</option>");
		Str.append("<option value=02").append(StrSelectdrop("02", month)).append(">February</option>");
		Str.append("<option value=03").append(StrSelectdrop("03", month)).append(">March</option>");
		Str.append("<option value=04").append(StrSelectdrop("04", month)).append(">April</option>");
		Str.append("<option value=05").append(StrSelectdrop("05", month)).append(">May</option>");
		Str.append("<option value=06").append(StrSelectdrop("06", month)).append(">June</option>");
		Str.append("<option value=07").append(StrSelectdrop("07", month)).append(">July</option>");
		Str.append("<option value=08").append(StrSelectdrop("08", month)).append(">August</option>");
		Str.append("<option value=09").append(StrSelectdrop("09", month)).append(">September</option>");
		Str.append("<option value=10").append(StrSelectdrop("10", month)).append(">October</option>");
		Str.append("<option value=11").append(StrSelectdrop("11", month)).append(">November</option>");
		Str.append("<option value=12").append(StrSelectdrop("12", month)).append(">December</option>");

		return Str.toString();
	}

	private void SalesOrderDetails(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_ids = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String team_ids = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String exe_ids = PadQuotes(RetrunSelectArrVal(request, "exe_id"));
			String fueltype_id = PadQuotes(RetrunSelectArrVal(request, "fueltype_id"));
			String soe_id = PadQuotes(request.getParameter("soe_id"));
			String starttime = PadQuotes(RetrunSelectArrVal(request, "starttime"));
			String tilldate = PadQuotes(request.getParameter("tilldate"));

			String enquiry = PadQuotes(RetrunSelectArrVal(request, "enquiry"));
			String testdrivedone = PadQuotes(RetrunSelectArrVal(request, "testdrivedone"));
			String testdrivenotdone = PadQuotes(RetrunSelectArrVal(request, "testdrivenotdone"));
			String homevisitdone = PadQuotes(RetrunSelectArrVal(request, "homevisitdone"));
			String homevisitnotdone = PadQuotes(RetrunSelectArrVal(request, "homevisitnotdone"));
			String meetingdone = PadQuotes(RetrunSelectArrVal(request, "meetingdone"));
			String meetingnotdone = PadQuotes(RetrunSelectArrVal(request, "meetingnotdone"));
			String salesorder = PadQuotes(RetrunSelectArrVal(request, "salesorder"));
			String soretail = PadQuotes(RetrunSelectArrVal(request, "soretail"));
			String bookinginhand = PadQuotes(RetrunSelectArrVal(request, "bookinginhand"));
			String allocated = PadQuotes(RetrunSelectArrVal(request, "allocated"));
			String selffinance = PadQuotes(RetrunSelectArrVal(request, "selffinance"));
			String nofinance = PadQuotes(RetrunSelectArrVal(request, "nofinance"));
			String docpending = PadQuotes(RetrunSelectArrVal(request, "docpending"));
			String login = PadQuotes(RetrunSelectArrVal(request, "login"));
			String waitingforapproval = PadQuotes(RetrunSelectArrVal(request, "waitingforapproval"));
			String doreceived = PadQuotes(RetrunSelectArrVal(request, "doreceived"));
			String cancelled = PadQuotes(RetrunSelectArrVal(request, "cancelled"));
			String delivered = PadQuotes(RetrunSelectArrVal(request, "delivered"));
			String enqteam = PadQuotes(RetrunSelectArrVal(request, "enqteam"));
			// Brand
			if (!brand_ids.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_ids + ") ";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrFilter += " AND so_branch_id IN (" + branch_ids + ") ";
			}
			// Models
			if (!model_ids.equals("")) {
				StrFilter += " AND item_model_id IN (" + model_ids + ") ";
			}

			// Teams
			if (!team_ids.equals("") && !enqteam.equals("yes")) {
				StrFilter += " AND so_emp_id IN (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id IN (" + team_ids + "))";
			}
			else if (!team_ids.equals("")) {
				StrFilter += " AND enquiry_team_id IN (" + team_ids + ") ";
			}

			// Sales Consultant
			if (!exe_ids.equals("")) {
				StrFilter += " AND emp_id IN (" + exe_ids + ") ";
			}
			// Sales fueltype_id
			if (!fueltype_id.equals("")) {
				StrFilter += " AND item_fueltype_id IN (" + fueltype_id + ") ";
			}
			// SOE
			//
			// Date Filter
			if (enquiry.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "' ";
			}

			// TestdriveDone count
			if (testdrivedone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id IN "
						+ " (SELECT DISTINCT testdrive_enquiry_id FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " WHERE 1 = 1"
						+ " AND testdrive_fb_taken = 1 )";
			}

			// Testdrive Not Done count
			if (testdrivenotdone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id NOT IN "
						+ " (SELECT testdrive_enquiry_id FROM " + compdb(comp_id) + "axela_sales_testdrive"
						+ " WHERE 1 = 1 )";
			}

			// homevisitdone count
			if (homevisitdone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id IN "
						+ "(SELECT DISTINCT followup_enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " WHERE 1=1"
						+ " AND followup_feedbacktype_id = 9 )";
			}

			// homevisitnotdone count
			if (homevisitnotdone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id NOT IN "
						+ "(SELECT DISTINCT followup_enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " WHERE 1=1"
						+ " AND followup_feedbacktype_id = 9)";
			}

			// meetingdone count
			if (meetingdone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id IN "
						+ "(SELECT DISTINCT followup_enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " WHERE 1=1"
						+ " AND followup_feedbacktype_id = 5 )";
			}

			// meetingdonenotdone count
			if (meetingnotdone.equals("yes") && !starttime.equals("")) {
				StrFilter = StrFilter.replace("so_branch_id", "enquiry_branch_id");
				StrFilter += " AND SUBSTR(enquiry_date,1,8) = '" + starttime + "'"
						+ " AND enquiry_status_id != 2"
						+ " AND enquiry_id NOT IN "
						+ "(SELECT followup_enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " WHERE 1=1"
						+ " AND followup_feedbacktype_id = 5)";
			}

			// Sales Order Count
			if (salesorder.equals("yes") && !starttime.equals("")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' ";
			}
			// Sales Order Count
			if (salesorder.equals("yes") && !tilldate.equals("")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' ";
			}
			// Sales Order Retial
			if (!salesorder.equals("yes") && !starttime.equals("") && soretail.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_active = 1 AND so_retail_date != '' ";
			}
			// Sales Order Retial
			if (!salesorder.equals("yes") && !tilldate.equals("") && soretail.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_active = 1 AND so_retail_date != '' ";
			}

			// Sales Order inhand
			if (!salesorder.equals("yes") && !starttime.equals("") && bookinginhand.equals("yes")) {
				StrFilter += " AND so_active = 1 AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_retail_date = ''";
			}
			// Sales Order inhand
			if (!salesorder.equals("yes") && !tilldate.equals("") && bookinginhand.equals("yes")) {
				StrFilter += " AND so_active = 1 AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_retail_date = ''";
			}

			// Sales Order allocated
			if (!salesorder.equals("yes") && !starttime.equals("") && allocated.equals("yes")) {
				StrFilter += " AND so_active = 1 AND SUBSTR(so_date,1,8) = '" + starttime
						+ "' AND so_vehstock_id !=0 AND so_stockallocation_time != '' AND so_delivered_date = '' AND so_retail_date = ''";
			}
			// Sales Order allocated
			if (!salesorder.equals("yes") && !tilldate.equals("") && allocated.equals("yes")) {
				StrFilter += " AND so_active = 1 AND SUBSTR(so_date,1,6) ='" + tilldate
						+ "' AND so_vehstock_id !=0 AND so_stockallocation_time != '' AND so_delivered_date = '' AND so_retail_date = '' ";
			}

			// Sales Order sleffinance
			if (!salesorder.equals("yes") && !starttime.equals("") && selffinance.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 9 AND so_retail_date = '' ";
			}
			// Sales Order sleffinance
			if (!salesorder.equals("yes") && !tilldate.equals("") && selffinance.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 9 AND so_retail_date = '' ";
			}
			// Sales Order nofinance
			if (!salesorder.equals("yes") && !starttime.equals("") && nofinance.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 6 AND so_retail_date = '' ";
			}
			// Sales Order nofinance
			if (!salesorder.equals("yes") && !tilldate.equals("") && nofinance.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 6 AND so_retail_date = '' ";
			}

			// Sales Order docpending
			if (!salesorder.equals("yes") && !starttime.equals("") && docpending.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 10 AND so_retail_date = '' ";
			}
			// Sales Order docpending
			if (!salesorder.equals("yes") && !tilldate.equals("") && docpending.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 10 AND so_retail_date = '' ";
			}

			// Sales Order login
			if (!salesorder.equals("yes") && !starttime.equals("") && login.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 1 AND so_retail_date = '' ";
			}
			// Sales Order login
			if (!salesorder.equals("yes") && !tilldate.equals("") && login.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 1 AND so_retail_date = '' ";
			}

			// Sales Order waitingforapproval
			if (!salesorder.equals("yes") && !starttime.equals("") && waitingforapproval.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 2 AND so_retail_date = '' ";
			}
			// Sales Order waitingforapproval
			if (!salesorder.equals("yes") && !tilldate.equals("") && waitingforapproval.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 2 AND so_retail_date = '' ";
			}

			// Sales Order doreceived
			if (!salesorder.equals("yes") && !starttime.equals("") && doreceived.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_finstatus_id = 12 AND so_retail_date = '' ";
			}
			// Sales Order doreceived
			if (!salesorder.equals("yes") && !tilldate.equals("") && doreceived.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_finstatus_id = 12 AND so_retail_date = '' ";
			}

			// Sales Order cancelled
			if (!salesorder.equals("yes") && !starttime.equals("") && cancelled.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_active = 0 ";
			}
			// Sales Order cancelled
			if (!salesorder.equals("yes") && !tilldate.equals("") && cancelled.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_active = 0 ";
			}
			// Sales Order delivered
			if (!salesorder.equals("yes") && !starttime.equals("") && delivered.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,8) = '" + starttime + "' AND so_active = 1 AND so_delivered_date != '' ";
			}
			// Sales Order delivered
			if (!salesorder.equals("yes") && !tilldate.equals("") && delivered.equals("yes")) {
				StrFilter += " AND SUBSTR(so_date,1,6) ='" + tilldate + "' AND so_active = 1 AND so_delivered_date != '' ";
			}

			// StrFilter += " AND so_active = 1";
			// SOP("StrSearch====" + StrFilter);
			if (enquiry.equals("yes")
					|| testdrivedone.equals("yes")
					|| testdrivenotdone.equals("yes")
					|| homevisitdone.equals("yes")
					|| homevisitnotdone.equals("yes")
					|| meetingdone.equals("yes")
					|| meetingnotdone.equals("yes")) {
				if (!soe_id.equals("")) {
					StrFilter += " AND enquiry_soe_id IN (" + soe_id + ") ";
				}
				SetSession("enquirystrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
			}

			if (salesorder.equals("yes") || soretail.equals("yes") || bookinginhand.equals("yes") || allocated.equals("yes") || selffinance.equals("yes") || nofinance.equals("yes")
					|| docpending.equals("yes") || login.equals("yes")
					|| waitingforapproval.equals("yes") || doreceived.equals("yes")
					|| cancelled.equals("yes") || delivered.equals("yes")) {
				if (!soe_id.equals("")) {
					StrFilter += " AND so_enquiry_id IN (SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_soe_id IN ( " + soe_id + "))";
				}
				SetSession("sostrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
