package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Branch_Dash extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids, soe_ids, sob_ids;
	public String team_id = "", exe_id = "", model_id = "", brand_id = "", region_id = "", soe_id = "", sob_id = "";
	public String StrHTML = "";
	public String dr_totalby = "";
	// public String StrBranch = "";
	public String comp_id = "0";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	public String StrBranch = "", StrRegion = "", StrModel = "";
	public String StrSoe = "", StrSob = "";
	DecimalFormat deci = new DecimalFormat("#");
	public String go = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "emp_branch_id").replace(")", "  OR emp_branch_id=0)");

					StrSearch += ExeAccess;

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id in (" + team_id + "))";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!region_id.equals("")) {
						StrRegion += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrBranch = " AND branch_id IN (" + branch_id + ") ";
						mischeck.exe_branch_id = branch_id;
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
						StrHTML = ListDashboard();
					}
				}
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
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		// SOP("region_id---------/---RBD-------" + region_id);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		SOP("branch_id==//==" + branch_id);
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		// SOP("team id==//==" + team_id);
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));
		// SOP("dr_totalby===== " + dr_totalby);
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListDashboard() {
		int empid = 0, empcount = 0;
		int total_enquiry = 0;

		int total_enquiry_followup_planned = 0;
		int total_enquiry_followup_completed = 0;

		int total_homevisit_planned = 0;
		int total_homevisit_completed = 0;

		int total_enquiry_closed = 0;
		int total_meeting_completed = 0, total_meeting_planned = 0;
		int total_enquiry_testdrivesplanned = 0, total_enquiry_testdrivescompleted = 0;
		int total_enquiry_hot = 0, total_enquiry_open = 0, total_quote_count = 0, total_quote_kpi_count = 0;
		int total_enquiry_KPItestdrives = 0;
		int total_so_count = 0, total_so_value = 0;
		int total_followup_esc = 0;

		int total_booking_expected = 0;
		int total_so_deliveries_planned = 0;
		int total_so_actual_deliveries = 0;

		int total_receipt_count = 0, total_receipt_value = 0;
		double salesratio = 0, testdriveratio = 0;
		double total_salesratio = 0, total_testdriveratio = 0;
		StringBuilder Str = new StringBuilder();

		String StrRegionJoin = "";
		String groupBy = "";

		if (!region_id.equals("") || dr_totalby.equals("Regions") || dr_totalby.equals("Brands")) {
			StrRegionJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = replace_branch_id ";
		}

		switch (dr_totalby) {
			case "Consultants" :
				groupBy = " enquiry_emp_id";
				break;

			case "Teams" :
				groupBy = " enquiry_team_id";
				break;

			case "Branches" :
				groupBy = " enquiry_branch_id";
				break;

			case "Regions" :
				groupBy = " branch_region_id";
				break;

			case "Brands" :
				groupBy = " branch_brand_id";
				break;

		}

		StrSql = "SELECT"
				+ " emp_id, emp_name, emp_ref_no,"
				+ " COALESCE (branch_region_id, 0) AS region_id,"
				+ " COALESCE (enquirycount, 0) AS enquirycount,"
				+ " COALESCE (enquiryopen, 0) AS enquiryopen,"
				+ " COALESCE (enquiryclosed, 0) AS enquiryclosed,"
				+ " COALESCE (enquiryhot, 0) AS enquiryhot,"

				+ " COALESCE (meetingplanned, 0) AS meetingplanned,"
				+ " COALESCE (meetingcompleted, 0) AS meetingcompleted,"
				+ " COALESCE (enquiryfollowupplanned, 0) AS enquiryfollowupplanned,"
				+ " COALESCE (enquiryfollowupcompleted, 0) AS enquiryfollowupcompleted,"
				+ " COALESCE (homevisitplanned, 0) AS homevisitplanned,"
				+ " COALESCE (homevisitcompleted, 0) AS homevisitcompleted,"

				+ " COALESCE (followupesc, 0) AS followupesc,"
				+ " COALESCE (enquiry_testdrivesplanned,0) AS enquiry_testdrivesplanned,"
				+ " COALESCE (enquiry_testdrivescompleted,0) AS enquiry_testdrivescompleted,"
				+ " COALESCE (enquiry_KPItestdrives, 0) AS enquiry_KPItestdrives,"
				+ " COALESCE (quotecount, 0) AS quotecount,"
				+ " COALESCE (quotekpicount, 0) AS quotekpicount,"
				+ " COALESCE (socount, 0) AS socount,"
				+ " branch_id, branch_name, ";

		if (dr_totalby.equals("Teams")) {
			StrSql += " team_id, team_name, ";
		} else if (dr_totalby.equals("Regions")) {
			StrSql += " region_id, region_name, ";
		}
		else if (dr_totalby.equals("Brands")) {
			StrSql += " brand_id, brand_name, ";
		}

		// bookingexpected
		StrSql += " 0 AS bookingexpected,"

				// deliveriesplanned"
				+ " 0 AS deliveriesplanned,"

				// actualdeliveries"
				+ " 0 AS actualdeliveries,"

				+ " 0 AS receiptcount"

				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id ";

		if (dr_totalby.equals("Teams")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		} else if (dr_totalby.equals("Regions")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
		}
		else if (dr_totalby.equals("Brands")) {
			StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
		}

		// tblenquiry
		StrSql += " LEFT JOIN ( "
				+ " SELECT COUNT(DISTINCT enquiry_id) AS enquirycount,"
				+ " SUM(IF(enquiry_status_id = 1 ,1,0)) AS enquiryopen,"
				+ " SUM(IF(enquiry_status_id > 2 ,1,0)) AS enquiryclosed,"
				+ " SUM(IF(enquiry_priorityenquiry_id = 1"
				+ "	AND enquiry_status_id = 1 ,1,0)) AS enquiryhot, enquiry_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry ";
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "enquiry_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "enquiry_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "enquiry_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy
				+ " ) AS tblenquiry ON tblenquiry.enquiry_emp_id = emp_id "

				// tblmeeting
				+ " LEFT JOIN ( "
				+ " SELECT SUM(IF(followup_followuptype_id = 2,1,0)) AS meetingplanned,"
				+ " SUM(IF (followup_feedbacktype_id = 5 AND followup_desc != '',	1,0)) AS meetingcompleted,"
				+ " count(DISTINCT followup_id) AS enquiryfollowupplanned,"
				+ " SUM(IF(followup_desc != '', 1, 0)) AS enquiryfollowupcompleted,"
				+ " SUM(IF (followup_followuptype_id = 3,	1,0)) AS homevisitplanned,"
				+ " SUM(IF (followup_feedbacktype_id = 9 AND followup_desc != '',	1,0	)) AS homevisitcompleted,"
				+ " followup_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id ";
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "enquiry_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(followup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(followup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "enquiry_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "enquiry_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy
				+ " ) AS tblmeeting ON tblmeeting.followup_emp_id = emp_id "

				// tblfollowupesc
				+ " LEFT JOIN ( "
				+ " SELECT count(DISTINCT followup_id) AS followupesc, followup_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON enquiry_id = followup_enquiry_id ";
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "enquiry_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(followup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(followup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
				+ " AND followup_trigger = 5 ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "enquiry_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "enquiry_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy
				+ " ) AS tblfollowupesc ON tblfollowupesc.followup_emp_id = emp_id "

				// tblenquiry_testdrives
				+ " LEFT JOIN ( "
				+ " SELECT count(DISTINCT testdrive_id) AS enquiry_testdrivesplanned,"
				+ " SUM(IF(testdrive_fb_taken = 1 AND testdrive_fb_entry_date != '' ,1,0)) AS enquiry_testdrivescompleted,"
				+ " SUM(IF(testdrive_type = 1 AND testdrive_fb_taken = 1 AND testdrive_fb_entry_date != '',1,0)) AS enquiry_KPItestdrives,"
				+ " testdrive_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive ON enquiry_id = testdrive_enquiry_id ";
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "enquiry_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "enquiry_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "enquiry_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy
				+ " ) AS tblenquiry_testdrives ON tblenquiry_testdrives.testdrive_emp_id = emp_id "

				// tblquotecount
				+ " LEFT JOIN ( "
				+ " SELECT CAST(count(DISTINCT quote_id) AS CHAR) AS quotecount,"
				+ " CAST(count(DISTINCT quote_enquiry_id) AS CHAR) AS quotekpicount,"
				+ " quote_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_quote ";
		if (!StrModel.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON quoteitem_item_id = item_id ";
		}
		if (!soe_id.equals("") || !sob_id.equals("") || dr_totalby.equals("Teams")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id ";
		}
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "quote_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(quote_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(quote_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
				+ " AND quote_active = '1' ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "quote_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "item_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy.replace("enquiry_emp_id", "quote_emp_id")
				.replace("enquiry_branch_id", "quote_branch_id")
				+ " ) AS tblquotecount ON tblquotecount.quote_emp_id = emp_id "

				// tblsocount
				+ " LEFT JOIN ( "
				+ " SELECT count(DISTINCT so_id) AS socount, so_emp_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_so ";
		if (!StrModel.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id "
					+ " AND soitem_rowcount > 0 "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON soitem_item_id = item_id ";
		}
		if (!soe_id.equals("") || !sob_id.equals("") || dr_totalby.equals("Teams")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
		}
		if (!StrRegion.equals("") || !dr_totalby.equals("")) {
			StrSql += StrRegionJoin.replace("replace_branch_id", "so_branch_id");
		}
		StrSql += " WHERE 1 = 1 "
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
				+ " AND so_active = '1' ";
		if (!branch_id.equals("")) {
			StrSql += StrBranch.replace("branch_id", "so_branch_id");
		}
		if (!region_id.equals("")) {
			StrSql += StrRegion;
		}
		if (!region_id.equals("")) {
			StrSql += StrModel.replace("model_id", "item_model_id");
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		if (!sob_id.equals("")) {
			StrSql += StrSob;
		}
		StrSql += " GROUP BY " + groupBy.replace("enquiry_emp_id", "so_emp_id")
				.replace("enquiry_branch_id", "so_branch_id")
				+ " ) AS tblsocount ON tblsocount.so_emp_id = emp_id "

				+ " WHERE emp_active = '1' "
				+ " AND emp_sales = '1' "
				+ StrSearch + StrRegion + StrBranch + ""
				+ " GROUP BY " + groupBy.replace("enquiry_", "")
				+ " ORDER BY branch_name, emp_name ";

		SOP("ListDashboard StrSql--------------" + StrSqlBreaker(StrSql));

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered table-hover\">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");

				switch (dr_totalby) {
					case "Consultants" :
						Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
						break;

					case "Teams" :
						Str.append("<th data-toggle=\"true\">Team</th>\n");
						break;

					case "Branches" :
						Str.append("<th data-toggle=\"true\">Banch</th>\n");
						break;

					case "Regions" :
						Str.append("<th data-toggle=\"true\">Region</th>\n");
						break;

					case "Brands" :
						Str.append("<th data-toggle=\"true\">Brand</th>\n");
						break;

				}

				Str.append("<th >Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\">Open Enquiry</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Closed Enquiry</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Hot Enquiry</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Meetings Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Meetings Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Follow-Up Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Follow-Up Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Home Visit Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Home Visit Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Escalation</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Test Drives Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Test Drives Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">KPI Test Drives</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Test Drive Ratio</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Quotes</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">KPI Quotes</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Booking Expected</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Orders</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Ratio</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Deliveries Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actual Deliveries</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Receipts</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					if (empid != crs.getInt("emp_id")) {
						empcount++;
						empid = crs.getInt("emp_id");
					}
					total_enquiry = total_enquiry + crs.getInt("enquirycount");
					total_enquiry_open = total_enquiry_open + crs.getInt("enquiryopen");
					total_enquiry_closed = total_enquiry_closed + crs.getInt("enquiryclosed");
					total_enquiry_hot = total_enquiry_hot + crs.getInt("enquiryhot");
					total_meeting_planned = total_meeting_planned + crs.getInt("meetingplanned");
					total_meeting_completed = total_meeting_completed + crs.getInt("meetingcompleted");

					total_enquiry_followup_planned = total_enquiry_followup_planned + crs.getInt("enquiryfollowupplanned");
					total_enquiry_followup_completed = total_enquiry_followup_completed + crs.getInt("enquiryfollowupcompleted");

					total_homevisit_planned = total_homevisit_planned + crs.getInt("homevisitplanned");
					total_homevisit_completed = total_homevisit_completed + crs.getInt("homevisitcompleted");

					total_followup_esc = total_followup_esc + crs.getInt("followupesc");
					total_enquiry_testdrivesplanned = total_enquiry_testdrivesplanned + crs.getInt("enquiry_testdrivesplanned");
					total_enquiry_testdrivescompleted = total_enquiry_testdrivescompleted + crs.getInt("enquiry_testdrivescompleted");

					total_enquiry_KPItestdrives = total_enquiry_KPItestdrives + crs.getInt("enquiry_KPItestdrives");

					total_quote_count = total_quote_count + crs.getInt("quotecount");
					total_quote_kpi_count = total_quote_kpi_count + crs.getInt("quotekpicount");

					total_so_count = total_so_count + crs.getInt("socount");

					total_booking_expected = total_booking_expected + crs.getInt("bookingexpected");

					total_so_deliveries_planned = total_so_deliveries_planned + crs.getInt("deliveriesplanned");
					total_so_actual_deliveries = total_so_actual_deliveries + crs.getInt("actualdeliveries");

					total_receipt_count = total_receipt_count + crs.getInt("receiptcount");

					testdriveratio = 0;
					salesratio = 0;

					if (!crs.getString("enquiry_KPItestdrives").equals("0") && !crs.getString("enquirycount").equals("0")) {
						testdriveratio = (crs.getDouble("enquiry_KPItestdrives") / crs.getDouble("enquirycount")) * 100;
						testdriveratio = Math.round(testdriveratio * 100.0) / 100.0;
					}
					if (total_enquiry != 0) {
						// SOP("total_enquiry_KPItestdrives==" +
						// total_enquiry_KPItestdrives);
						// SOP("total_enquiry==" + total_enquiry);
						// if (total_enquiry != 0) {
						total_testdriveratio = (Double.parseDouble(Integer.toString(total_enquiry_KPItestdrives)) / Double.parseDouble(Integer.toString(total_enquiry))) * 100;
						// SOP("total_testdriveratio==" + total_testdriveratio);
						total_testdriveratio = Math.round(total_testdriveratio * 100.0) / 100.0;
						// SOP("total_testdriveratio=aaaaaaa=" +
						// total_testdriveratio);
						// }
					}
					if (!crs.getString("socount").equals("0") && !crs.getString("enquirycount").equals("0")) {
						salesratio = (crs.getDouble("socount") / crs.getDouble("enquirycount")) * 100;
						salesratio = Math.round(salesratio * 100.0) / 100.0;
					}
					if (total_enquiry != 0) {
						total_salesratio = (Double.parseDouble(Integer.toString(total_so_count)) / Double.parseDouble(Integer.toString(total_enquiry))) * 100;
						total_salesratio = Math.round(total_salesratio * 100.0) / 100.0;
					}

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + empcount + ".</td>");

					switch (dr_totalby) {
						case "Consultants" :
							Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " ("
									+ crs.getString("emp_ref_no") + ")</a></td>");
							break;

						case "Teams" :
							Str.append("<td valign=top align=left><a href=../sales/team-list.jsp?team_id=" + crs.getString("team_id") + ">" + crs.getString("team_name") + ")</a></td>");
							break;

						case "Branches" :
							Str.append("<td valign=top align=left><a href=../portal/branch-list.jsp?branch_id=" + crs.getString("branch_id") + ">" + crs.getString("branch_name") + "</a></td>");
							break;

						case "Regions" :
							Str.append("<td valign=top align=left>" + crs.getString("region_name") + "</td>");
							break;

						case "Brands" :
							Str.append("<td valign=top align=left>" + crs.getString("brand_name") + "</td>");
							break;

					}

					// / Enquiry
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquirycount")
							+ "</a></td>");
					// / Open Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=open"
							+ "&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryopen")
							+ "</a></td>");
					// / Closed Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=closed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryclosed")
							+ "</a></td>");
					// / Hot Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=hot&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryhot")
							+ "</a></td>");

					// / Meeting Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=meetingsplanned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("meetingplanned")
							+ "</a></td>");
					// / Meeting Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=meetingscompleted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("meetingcompleted") + "</a></td>");

					// / Follow-planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=followupplanned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryfollowupplanned") + "</a></td>");
					// Str.append("<td valign=top align=right>").append(crs.getString("enquiryfollowupplanned")).append("</a></td>");

					// / Follow-completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=followupcompleted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiryfollowupcompleted") + "</a></td>");

					// Str.append("<td valign=top align=right>").append(crs.getString("enquiryfollowupcompleted")).append("</a></td>");

					// / Homevisitplanned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=homevisitplanned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("homevisitplanned")
							+ "</a></td>");

					// / Homevisitcompleted
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=homevisitcompleted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("homevisitcompleted")
							+ "</a></td>");
					// Str.append("<td valign=top align=right>").append(crs.getString("homevisitcompleted")).append("</a></td>");

					// / Escalation
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=escalation&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("followupesc")
							+ "</a></td>");

					// / TestDrives Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=planned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiry_testdrivesplanned") + "</a></td>");
					// / TestDrives Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=completed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiry_testdrivescompleted") + "</a></td>");
					// / KPI TestDrives
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=kpi&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("enquiry_KPItestdrives") + "</a></td>");
					// /Test Drive Ratio
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=kpi&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ testdriveratio
							+ "</a></td>");

					// / Quotes
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?quote=quote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("quotecount")
							+ "</a></td>");
					// / KPI Quotes
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("quotekpicount")
							+ "</a></td>");

					// Booking Expected
					Str.append("<td valign=top align=right>").append(crs.getString("bookingexpected")).append("</td>");

					// Sales Order Count
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("socount")
							+ "</a></td>");

					// /sales Ratio
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ salesratio + "</a></td>");

					// Deliveries Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("deliveriesplanned")
							+ "</a></td>");

					// Str.append("<td valign=top align=right>").append(crs.getString("deliveriesplanned")).append("</td>");

					// Actual Deliveries
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("actualdeliveries")
							+ "</a></td>");

					// Str.append("<td valign=top align=right>").append(crs.getString("actualdeliveries")).append("</td>");

					// Receipt Count
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&emp_id=" + crs.getString("emp_id") + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ crs.getInt("receiptcount")
							+ "</a></td>");

				}
				if (empcount > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
					Str.append("<td valign=top align=right><b>Total:</b></td>");

					// / Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=enquiry&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry + "</b></a></td>");

					// / Open Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=open"
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry_open + "</b></a></td>");

					// / Closed Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=closed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry_closed + "</b></a></td>");

					// / Hot Opportunity
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=hot&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry_hot + "</b></a></td>");

					// / Meeting Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=meetingsplanned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_meeting_planned + "</a></td>");

					// / Meeting Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=meetingscompleted&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_meeting_completed + "</a></td>");

					// / Follow-up Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=followup&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry_followup_planned + "</b></a></td>");

					// / Follow-up Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=followup&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_enquiry_followup_completed + "</b></a></td>");

					// / Homevisit Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=homevisit&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_homevisit_planned + "</b></a></td>");

					// / Homevisit Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=homevisit&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_homevisit_completed + "</b></a></td>");

					// / Escalation
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?enquiry=escalation&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_followup_esc + "</b></a></td>");

					// / TestDrives Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=planned&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_enquiry_testdrivesplanned
							+ "</a></td>");

					// / TestDrives Completed
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=completed&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_enquiry_testdrivescompleted
							+ "</a></td>");

					// / KPI TestDrives
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=kpi&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_enquiry_KPItestdrives + "</a></td>");

					// /Test Drive Ratio
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?testdrive=kpi&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank>"
							+ total_testdriveratio + "</a></td>");

					// / Quotes Count
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?quote=quote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_quote_count + "</b></a></td>");

					// / KPI Quotes
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?quote=kpiquote&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_quote_kpi_count + "</b></a></td>");

					// Booking Expected
					Str.append("<td valign=top align=right>").append(total_booking_expected).append("</td>");

					// Sales Order Count
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_so_count + "</b></a></td>");

					// Sales Ratio
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_salesratio + "</b></a></td>");

					// Deliveries Planned
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_so_deliveries_planned + "</b></a></td>");
					// Str.append("<td valign=top align=right>").append(total_so_deliveries_planned).append("</td>");

					// Actual Deliveries
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?salesorder=salesorder&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_so_actual_deliveries + "</b></a></td>");
					// Str.append("<td valign=top align=right>").append(total_so_actual_deliveries).append("</td>");

					// Receipt Count
					Str.append("<td valign=top align=right><a href=../sales/enquiry-check-search.jsp?receipt=receipt&starttime=" + starttime + "&endtime=" + endtime + ""
							+ "&model_id=" + model_id + "&region_id=" + region_id + "&exe_id=" + exe_id + "&team_id=" + team_id + "&dr_branch_id=" + dr_branch_id + " target=_blank><b>"
							+ total_receipt_count + "</b></a></td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// public String PopulateTeam() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT team_id, team_name "
	// + " FROM " + compdb(comp_id) + "axela_sales_team "
	// + " WHERE team_branch_id=" + dr_branch_id + " "
	// + " GROUP BY team_id "
	// + " ORDER BY team_name ";
	// // SOP("PopulateTeam query ==== " + StrSql);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("team_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
	// Str.append(">").append(crs.getString("team_name")).append("</option> \n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateSalesExecutives() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql =
	// "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_team_exe on teamtrans_emp_id=emp_id"
	// + " WHERE emp_active = '1' AND emp_sales='1' AND "
	// + " (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
	// + " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) +
	// "axela_emp_branch empbr"
	// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
	// + " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
	//
	// if (!team_id.equals("")) {
	// StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
	// }
	// StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
	// // SOP("hjag..." + StrSql);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<SELECT name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
	// Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
	// }
	// Str.append("</SELECT>");
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateModel() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// String SqlStr = "SELECT model_id, model_name "
	// + " FROM " + compdb(comp_id) + "axela_inventory_item_model "
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item on item_model_id = model_id"
	// + " GROUP BY model_id ORDER BY model_name";
	// CachedRowSet crs =processQuery(SqlStr);
	// // SOP("SqlStr in PopulateCountry==========" + SqlStr);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("model_id"));
	// Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
	// Str.append(">").append(crs.getString("model_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	// public String PopulateItem() {
	// String stringval = "";
	// try {
	// StrSql = "SELECT model_id, model_name FROM " + compdb(comp_id) +
	// "axela_inventory_item_model "
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item on item_model_id=model_id"
	// + " GROUP BY model_id ORDER BY model_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// // SOP("StrSql in PopulateCountry==========" + StrSql);
	// while (crs.next()) {
	// stringval = stringval + "<option value=" + crs.getString("model_id") + "";
	// stringval = stringval + ArrSelectdrop(crs.getInt("model_id"), model_ids);
	// stringval = stringval + ">" + crs.getString("model_name") + "</option>\n";
	// }
	//
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return stringval;
	// }

	// public String PopulateTotalBy(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// // Str.append("<option value=0>Select</option>");
	// Str.append("<option value=1").append(StrSelectdrop("1",
	// dr_totalby)).append(">Consultants</option>\n");
	// Str.append("<option value=2").append(StrSelectdrop("2",
	// dr_totalby)).append(">Teams</option>\n");
	// Str.append("<option value=3").append(StrSelectdrop("3",
	// dr_totalby)).append(">Branches</option>\n");
	// Str.append("<option value=4").append(StrSelectdrop("4",
	// dr_totalby)).append(">Brands</option>\n");
	// return Str.toString();
	// }

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
		Str.append("<option value=\"Consultants\" ").append(StrSelectdrop("Consultants", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=\"Teams\" ").append(StrSelectdrop("Teams", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=\"Branches\" ").append(StrSelectdrop("Branches", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=\"Regions\" ").append(StrSelectdrop("Regions", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=\"Brands\" ").append(StrSelectdrop("Brands", dr_totalby)).append(">Brands</option>\n");
		return Str.toString();
	}

}
