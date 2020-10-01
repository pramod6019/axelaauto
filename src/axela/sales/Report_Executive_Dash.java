package axela.sales;

//Manjur 13/05/2015
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Executive_Dash extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String dr_month = "";
	public String dr_year = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String StrSearch = "", StrModel = "";
	public String StrSearchSo = "";
	// public String StrHTML1 = "", StrClosedHTML = "", Strhtml = "";
	public String EnquirySearch = "";
	public String emp_all_exe = "";
	public String chart_data = "", chart_data_soe = "", chart_data_sob = "";
	public int chart_data_total = 0, chart_data_total_soe = 0, chart_data_total_sob = 0;
	public String chart_data_stage = "", chart_data_status = "", chart_data_priority = "";
	public int chart_data_total_stage = 0, chart_data_total_status = 0, chart_data_total_priority = 0;
	public String NoChart = "", NoChart_soe = "", NoChart_sob = "";
	public String NoChart_stage = "", NoChart_status = "", NoChart_priority = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public int TotalRecords = 0;
	public int enquirycount = 0;
	public int testdrives = 0;
	public int salesordercount = 0;
	public int deliverycount = 0;
	public String[] datearr = new String[13];
	String[] quarterarr = new String[5];
	public int startyear = 0;
	public static int year;
	public static int month;
	public static int maxdays;
	public MIS_Check1 mischeck = new MIS_Check1();
	public StringBuilder Str1 = new StringBuilder();
	public StringBuilder Str2 = new StringBuilder();
	public StringBuilder Str3 = new StringBuilder();
	public String monthAndYear = "";
	int quarter = 0;
	String quarterdates = "";
	DecimalFormat deci = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("") && branch_id.equals("")) {
						StrSearch += " AND branch_brand_id in (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN(" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ")";

					}
					if (!model_id.equals("")) {
						StrModel = " AND enquiry_model_id IN (" + model_id + ")";
					}

					EnquirySearch = BranchAccess + ExeAccess; // + ExeAccess;

					EnquirySearch = EnquirySearch + " AND SUBSTR(enquiry_date,1,6) >= SUBSTR('" + datearr[12] + "',1,6)";
					EnquirySearch = EnquirySearch + " AND SUBSTR(enquiry_date,1,6) <= SUBSTR('" + datearr[12] + "',1,6)";
					if (!exe_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("") && branch_id.equals("")) {
						branch_id = ReturnBranchids(brand_id, comp_id);
					}
					if (!region_id.equals("")) {
						EnquirySearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						EnquirySearch = EnquirySearch + " AND teamtrans_team_id IN (" + team_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
						dr_month = PadQuotes(request.getParameter("dr_month"));
						dr_year = PadQuotes(request.getParameter("dr_year"));
						StrHTML = FollowupEscalationStatus();
						ListEnquiryMonthly();
						ListEnquiryQuarterly();
						ListEnquiryYearly();
						PreparePieChartSOE();
						PreparePieChartSOB();
						PreparePieChartStage();
						PreparePieChartStatus();
						PreparePieChartPriority();
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error IN "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}

		month = (Integer.parseInt(dr_month));
		int startmonth = month;
		monthAndYear = TextMonth(month - 1).substring(0, 3);
		monthAndYear = monthAndYear + "-" + dr_year;
		double month1 = (double) month;
		String quarter1 = month1 / 3 <= 1 ? "1" : month1 / 3 <= 2 ? "2" : month1 / 3 <= 3 ? "3" : "4";

		quarter = Integer.parseInt(quarter1);
		// SOP("quarter1===" + quarter1);
		if (dr_year.equals("")) {
			dr_year = ToShortDate(kknow()).substring(0, 4);
		}
		year = (Integer.parseInt(dr_year));
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		// printStartEndQuarter(year, month);

		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");

		if (year != 0) {
			startyear = year - 1;
			int totalmonth = 13;
			int index = 0;
			for (int i = startmonth; i <= totalmonth; i++) {
				if (i < 10) {
					datearr[index] = startyear + "0" + i;
					// SOP("datearr[" + index + "]---------" + datearr[index]);
				}
				else {
					datearr[index] = startyear + "" + i;
					// SOP("datearr[" + index + "]---------" + datearr[index]);
				}

				if (i == 12) {
					totalmonth = startmonth;
					startmonth = 1;
					i = 0;
					startyear = startyear + 1;
				}
				index++;
				if (index == 13) {
					break;
				}
			}
			startyear = year - 1;
			int stratquarter = quarter;
			int totalquarter = 4;
			int quarterindex = 0;
			for (int i = stratquarter; i <= totalquarter; i++) {
				quarterarr[quarterindex] = startyear + "" + i;
				// SOP("quarterarr[" + quarterindex + "]---------" + quarterarr[quarterindex]);
				if (i == 4) {
					totalquarter = stratquarter;
					stratquarter = 1;
					i = 0;
					startyear = startyear + 1;
				}
				quarterindex++;
				if (quarterindex == 5) {
					break;
				}
			}
		}
	}
	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("")) {
		// msg = msg + "<br>SELECT Branch!<br>";
		// }
	}

	public String FollowupEscalationStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			String followupsql = "";
			String crmfollowupsql = "";
			int total = 0;
			followupsql = "  (SELECT 'Enquiry Follow-up' as type,"
					+ " SUM(CASE WHEN followup_trigger = 0 THEN 1 END) AS level0, "
					+ " SUM(CASE WHEN followup_trigger = 1 THEN 1 end) AS level1,"
					+ " SUM(CASE WHEN followup_trigger = 2 THEN 1 end) AS level2,"
					+ " SUM(CASE WHEN followup_trigger = 3 THEN 1 end) AS level3,"
					+ " SUM(CASE WHEN followup_trigger = 4 THEN 1 end) AS level4,"
					+ " SUM(CASE WHEN followup_trigger = 5 THEN 1 end) AS level5"

					+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = followup_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			if (!team_id.equals("") || team_ids != null) {
				followupsql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
			}
			// if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			// StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			// }
			followupsql += " WHERE SUBSTR(followup_followup_time, 1, 6) = SUBSTR(" + dr_year + dr_month + ", 1,6)"
					+ StrSearch.replace("emp_id", "followup_emp_id") + BranchAccess + ExeAccess.replace("emp_id", "enquiry_emp_id") +
					")";
			// SOP("Escalation status-------------------" + followupsql);
			crmfollowupsql = followupsql + ""
					+ " UNION "
					+ " (SELECT 'CRM Follow-up' as type,"
					+ " SUM(CASE WHEN crm_trigger = 0 THEN 1 END) AS level0, "
					+ " SUM(CASE WHEN crm_trigger = 1 THEN 1 end) AS level1,"
					+ " SUM(CASE WHEN crm_trigger = 2 THEN 1 end) AS level2,"
					+ " SUM(CASE WHEN crm_trigger = 3 THEN 1 end) AS level3,"
					+ " SUM(CASE WHEN crm_trigger = 4 THEN 1 end) AS level4,"
					+ " SUM(CASE WHEN crm_trigger = 5 THEN 1 end) AS level5"
					+ " from " + compdb(comp_id) + "axela_sales_crm "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			if (!team_id.equals("") || team_ids != null) {
				crmfollowupsql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
			}
			// if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			// StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			// }
			crmfollowupsql += " WHERE SUBSTR(crm_followup_time, 1, 6) = SUBSTR(" + dr_year + dr_month + ", 1,6)"
					+ StrSearch.replace("emp_id", "crm_emp_id") + BranchAccess + ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ ")";

			// SOP("crmfollowupsql-----------" + crmfollowupsql);
			CachedRowSet crs = processQuery(crmfollowupsql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered table-hover\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Escalation</th>\n");
				Str.append("<th data-hide=\"phone\" >Level 0</th>\n");
				Str.append("<th data-hide=\"phone\" >Level 1</th>\n");
				Str.append("<th data-hide=\"phone\" >Level 2</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 3</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 4</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 5</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					total = 0;
					total = crs.getInt("level0") + crs.getInt("level1") + crs.getInt("level2")
							+ crs.getInt("level3") + crs.getInt("level4") + crs.getInt("level5");
					Str.append("<tr align=center>\n");
					Str.append("<td align=left>").append(crs.getString("type")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level0")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level1")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level2")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level3")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level4")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level5")).append("</td>\n");
					Str.append("<td align=right>").append(total).append("</td>\n");
					Str.append("</tr>");

				}
				Str.append("</tbody>\n");
				Str.append("</table>");
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
	public String PopulateYears() {

		String year = ToShortDate(kknow()).substring(0, 4);
		StringBuilder years = new StringBuilder();
		for (int i = Integer.parseInt(year); i > Integer.parseInt(year) - 3; i--) {
			years.append("<option value = " + doublenum(i) + ""
					+ StrSelectdrop(doublenum(i), dr_year) + ">" + i
					+ "</option>\n");
		}
		return years.toString();
	}

	public String PopulateMonths() {
		String months = "";
		months += "<option value=01" + StrSelectdrop(doublenum(1), dr_month)
				+ ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dr_month)
				+ ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dr_month)
				+ ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dr_month)
				+ ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dr_month)
				+ ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dr_month)
				+ ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dr_month)
				+ ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dr_month)
				+ ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dr_month)
				+ ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dr_month)
				+ ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dr_month)
				+ ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dr_month)
				+ ">December</option>\n";
		return months;
	}

	public static int daysInMonth() {
		int date = 0;
		GregorianCalendar Calendar = new GregorianCalendar(year, month, year);
		Calendar.set(year, month, date);
		maxdays = Calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxdays;
	}

	public void ListEnquiryMonthly() {
		StrSql = " SELECT MONTHNAME(concat(calmonth,'00')) AS month,calmonth, "
				+ " COALESCE(enquirycount, 0) AS enquiry,";
		StrSql += " COALESCE(testdrivecount, 0) AS testdrives,";
		StrSql += " COALESCE(socount, 0) AS salesorders,";
		StrSql += " COALESCE(totalsoprofit, 0) AS totalsoprofit,";
		StrSql += " COALESCE(deliveriescount, 0) AS 'deliveries',";
		StrSql += " COALESCE(retailcount, 0) AS 'retails',";
		StrSql += " COALESCE(enquirytargetcount, 0) AS 'enquirytarget',";
		StrSql += " COALESCE(retailtargetcount, 0) AS 'retailtarget'"
				+ " FROM (";

		for (int i = 0; i < datearr.length; i++) {
			StrSql = StrSql + " SELECT " + datearr[i] + " AS calmonth  UNION";
		};
		// for replacing the last UNION
		StrSql = StrSql.substring(0, StrSql.trim().length() - 5);
		StrSql = StrSql + " ) AS cal"

				// Start Enquiry join
				+ " LEFT JOIN (SELECT COUNT(enquiry_id) as enquirycount, enquiry_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "enquiry_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "model_id");
		}
		StrSql += " GROUP BY SUBSTR(enquiry_date, 1, 6)"
				+ ") as tblenquiry ON SUBSTR(tblenquiry.enquiry_date, 1, 6) = cal.calmonth"

				// Start Test Drive join
				+ " LEFT JOIN (SELECT COUNT(testdrive_id) AS testdrivecount, testdrive_time"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive ";
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = testdrive_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		StrSql += " WHERE 1 = 1"
				+ " AND testdrive_fb_taken = 1 "
				+ StrSearch.replace("emp_id", "testdrive_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "model_id");
		}
		StrSql += " GROUP BY SUBSTR(testdrive_time,1,6)"
				+ ") as tbltestdrive ON SUBSTR(tbltestdrive.testdrive_time, 1, 6) = cal.calmonth"

				// Start Booking join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS socount, so_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active = 1 ";

		StrSql += StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_date,1,6)"
				+ ") AS tblsalesorder ON SUBSTR(tblsalesorder.so_date, 1, 6) = cal.calmonth"

				// Start SO Deliveries join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS deliveriescount, so_delivered_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_delivered_date,1,6)"
				+ ") AS tblsodelivery ON SUBSTR(tblsodelivery.so_delivered_date, 1, 6) = cal.calmonth"

				// Start SO Retail Date join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS retailcount, so_retail_date,"
				+ " COALESCE(SUM(so_profitability_profit), 0) AS totalsoprofit"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_retail_date,1,6)"
				+ ") AS tblsoretail ON SUBSTR(tblsoretail.so_retail_date, 1, 6) = cal.calmonth"

				// Start Enquiry Target join
				+ " LEFT JOIN (SELECT SUM(modeltarget_enquiry_count) AS enquirytargetcount, target_startdate"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}
		StrSql += " GROUP BY SUBSTR(target_startdate, 1, 6)"
				+ ") as tblenquirytarget ON SUBSTR(tblenquirytarget.target_startdate, 1, 6) = cal.calmonth"

				// Start Retail Target join
				+ " LEFT JOIN (SELECT SUM(modeltarget_so_count) AS retailtargetcount, target_startdate"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}
		StrSql += " GROUP BY SUBSTR(target_startdate, 1, 6)"
				+ ") as tblretailtarget ON SUBSTR(tblretailtarget.target_startdate, 1, 6) = cal.calmonth"

				+ " GROUP BY calmonth"
				+ " ORDER BY calmonth";
		// SOP("ListEnquiryMonthly-------------11-----" + StrSql);

		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// start of table
				// Str1.append("<div class=\"  table-bordered table-responsive \">\n");
				Str1.append("<table class=\"table table-hover table-bordered table-responsive \" data-filter=\"#filter\">\n");
				Str1.append("<thead><tr>\n");
				Str1.append("<th data-hide=\"phone\"></th>\n");
				while (crs.next()) {
					Str1.append("<th data-hide=\"phone\">").append(crs.getString("month").substring(0, 3) + "-" + crs.getString("calmonth").substring(2, 4)).append("</th>\n");
				}
				Str1.append("</thead>\n");
				crs.beforeFirst();
				Str1.append("<tbody>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Enquiries</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("enquiry")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Enquiry Target</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("enquirytarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Enquiry % Achieved</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("enquiry")), Integer.parseInt(crs.getString("enquirytarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Test Drives</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("testdrives")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Bookings</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("salesorders")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Retail</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("retails")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");

				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Retail Target</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("retailtarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");

				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Retail % Achieved</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("retails")), Integer.parseInt(crs.getString("retailtarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Deliveries</b></td>\n");
				while (crs.next()) {
					Str1.append("<td style=\"text-align:right;\">").append(crs.getString("deliveries")).append("</td>\n");
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");

				Str1.append("<tr>\n");
				Str1.append("<td data-toggle=\"true\"><b>Profit</b></td>\n");
				while (crs.next()) {
					if (crs.getDouble("totalsoprofit") >= 0) {
						Str1.append("<td style=\"text-align:right;\"><b><font color='blue'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					} else {
						Str1.append("<td style=\"text-align:right;\"><b><font color='red'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					}
				}
				crs.beforeFirst();
				Str1.append("</tr>\n");
				Str1.append("</tbody>\n");
				Str1.append("</table>\n");
				// Str1.append("</div>\n");
				// end of table
				crs.beforeFirst();

				chart_data = "[";
				while (crs.next()) {

					chart_data = chart_data + "{'month': '" + crs.getString("month").substring(0, 3) + "-" + crs.getString("calmonth").substring(2, 4) + "'"
							+ ", 'column-1':'" + crs.getString("enquiry") + "'"
							+ ", 'column-2':'" + crs.getString("testdrives") + "'"
							+ ", 'column-3':'" + crs.getString("salesorders") + "'"
							+ ", 'column-4':'" + crs.getString("deliveries") + "'}";
					// + ", 'column-4':'" + crs.getString("enquirytarget") + "'"
					// + ", 'column-5':'" + crs.getString("retailtarget") + "'}";

					if (!crs.isLast()) {
						chart_data = chart_data + ",";
					}
				}
				chart_data = chart_data + "]";
				crs.beforeFirst();

			} else {
				// NoChart = "<font color=red><b>No Data Found!</b></font>";
			}
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ListEnquiryYearly() {
		StrSql = " SELECT calyear, "
				+ " COALESCE(enquirycount, 0) AS enquiry,";
		StrSql += " COALESCE(testdrivecount, 0) AS testdrives,";
		StrSql += " COALESCE(socount, 0) AS salesorders,";
		StrSql += " COALESCE(totalsoprofit, 0) AS totalsoprofit,";
		StrSql += " COALESCE(deliveriescount, 0) AS 'deliveries',";
		StrSql += " COALESCE(retailscount, 0) AS 'retails',";
		StrSql += " COALESCE(enquirytargetcount, 0) AS 'enquirytarget',";
		StrSql += " COALESCE(retailtargetcount, 0) AS 'retailtarget'"
				+ " FROM (";
		for (int i = 0; i < 3; i++) {
			StrSql = StrSql + " SELECT " + startyear + " AS calyear  UNION";
			startyear--;
		}
		// for replacing the last UNION
		StrSql = StrSql.substring(0, StrSql.trim().length() - 5);
		StrSql = StrSql + " ) AS cal"

				// Start Yearly Enquiry join
				+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS enquirycount, enquiry_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}

		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		StrSql += " WHERE 1=1 " + StrSearch.replace("emp_id", "enquiry_emp_id")
				+ " GROUP BY SUBSTR(enquiry_date, 1, 4)"
				+ ") as monthlyenquiry ON SUBSTR(monthlyenquiry.enquiry_date, 1, 4) = cal.calyear"

				// Start Yearly Test Drive join
				+ " LEFT JOIN (SELECT COUNT(testdrive_id) AS testdrivecount, testdrive_time"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		StrSql += " WHERE 1 = 1"
				+ " AND testdrive_fb_taken = 1 "
				+ StrSearch.replace("emp_id", "testdrive_emp_id")
				+ " GROUP BY SUBSTR(testdrive_time, 1, 4)"
				+ ") AS monthlytestdrive ON SUBSTR(monthlytestdrive.testdrive_time, 1, 4) = cal.calyear"

				// Start Yearly Booking join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS socount, so_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active = 1 "
				+ StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_date, 1, 4)"
				+ ") AS monthlyso ON SUBSTR(monthlyso.so_date, 1, 4) = cal.calyear"

				// Start Yearly Deliveries join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS deliveriescount, so_delivered_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_delivered_date, 1, 4)"
				+ ") AS monthlydelivery ON SUBSTR(monthlydelivery.so_delivered_date, 1, 4) = cal.calyear"

				// Start Yearly Retail join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS retailscount,"
				+ " COALESCE(SUM(so_profitability_profit), 0) AS totalsoprofit,"
				+ " so_retail_date"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY SUBSTR(so_retail_date, 1, 4)"
				+ ") AS monthlyretails ON SUBSTR(monthlyretails.so_retail_date, 1, 4) = cal.calyear";

		// Start Enquiry Target join
		StrSql += " LEFT JOIN (SELECT SUM(modeltarget_enquiry_count) AS enquirytargetcount, target_startdate "
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}

		StrSql += " GROUP BY SUBSTR(target_startdate, 1, 4)"
				+ ") AS tblenquirytarget ON SUBSTR(tblenquirytarget.target_startdate, 1, 4) = cal.calyear"

				// Start Retail Target join
				+ " LEFT JOIN (SELECT SUM(modeltarget_so_count) AS retailtargetcount, target_startdate,"
				+ " CONCAT('Qtr ',QUARTER(target_startdate),'-',YEAR(target_startdate)) AS qtr"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 " + StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}

		StrSql += " GROUP BY SUBSTR(target_startdate, 1, 4)"
				+ ") as tblretailtarget ON SUBSTR(tblretailtarget.target_startdate, 1, 4) = cal.calyear";

		StrSql += " GROUP BY calyear"
				+ " ORDER BY calyear";
		// SOP("ListEnquiryYearly-----11-----" + StrSql);

		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// start of table
				// Str2.append("<div class=\"  table-bordered table-responsive \">\n");
				Str2.append("<table class=\"table table-hover table-bordered table-responsive \" data-filter=\"#filter\">\n");
				Str2.append("<thead><tr>\n");
				Str2.append("<th data-hide=\"phone\"></th>\n");
				while (crs.next()) {
					Str2.append("<th data-hide=\"phone\">").append(crs.getString("calyear")).append("</th>\n");
				}
				Str2.append("</thead>\n");
				crs.beforeFirst();
				Str2.append("<tbody>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Enquiries</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("enquiry")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Enquiry Target</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("enquirytarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Enquiry % Achieved</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("enquiry")), Integer.parseInt(crs.getString("enquirytarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Test Drives</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("testdrives")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Bookings</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("salesorders")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Retails</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("retails")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");

				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Retail Target</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("retailtarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");

				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Retail % Achieved</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("retails")), Integer.parseInt(crs.getString("retailtarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Deliveries</b></td>\n");
				while (crs.next()) {
					Str2.append("<td style=\"text-align:right;\">").append(crs.getString("deliveries")).append("</td>\n");
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");
				Str2.append("<tr>\n");

				Str2.append("<tr>\n");
				Str2.append("<td data-toggle=\"true\"><b>Profit</b></td>\n");
				while (crs.next()) {
					if (crs.getDouble("totalsoprofit") >= 0) {
						Str2.append("<td style=\"text-align:right;\"><b><font color='blue'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					} else {
						Str2.append("<td style=\"text-align:right;\"><b><font color='red'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					}
				}
				crs.beforeFirst();
				Str2.append("</tr>\n");

				Str2.append("</tbody>\n");
				Str2.append("</table>\n");
				// Str2.append("</div>\n");
				// end of table
				crs.beforeFirst();

			} else {
				// NoChart = "<font color=red><b>No Data Found!</b></font>";
			}
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

	}

	public void ListEnquiryQuarterly() {

		StrSql = " SELECT calqtr, "
				+ " COALESCE(enquirycount, 0) AS enquiry,";
		StrSql += " COALESCE(testdrivecount, 0) AS testdrives,";
		StrSql += " COALESCE(socount, 0) AS salesorders,";
		StrSql += " COALESCE(totalsoprofit, 0) AS totalsoprofit,";
		StrSql += " COALESCE(deliveries, 0) AS 'deliveries',";
		StrSql += " COALESCE(retails, 0) AS 'retails',";
		StrSql += " COALESCE(enquirytargetcount, 0) AS 'enquirytarget',";
		StrSql += " COALESCE(retailtargetcount, 0) AS 'retailtarget'"
				+ " FROM (";

		for (int i = 0; i < quarterarr.length; i++) {
			int quarteryear = Integer.parseInt(quarterarr[i].substring(0, 4));
			int quarter = Integer.parseInt(quarterarr[i].substring(4, 5));
			quarterdates = ReturnQuarterDates(quarteryear, quarter);
			int startdate = Integer.parseInt(quarterdates.substring(0, 6));
			int enddate = Integer.parseInt(quarterdates.substring(8, 14));
			String Strquarter = "";
			if (quarter == 1) {
				Strquarter = "'" + "Qtr 1" + "-" + quarteryear + "'";
			}
			if (quarter == 2) {
				Strquarter = "'" + "Qtr 2" + "-" + quarteryear + "'";
			}
			if (quarter == 3) {
				Strquarter = "'" + "Qtr 3" + "-" + quarteryear + "'";
			}
			if (quarter == 4) {
				Strquarter = "'" + "Qtr 4" + "-" + quarteryear + "'";
			}

			StrSql = StrSql + " SELECT " + Strquarter + " AS calqtr,'" + startdate
					+ "' AS calstartdate, '" + enddate + "' AS calenddate " + "UNION";
		}
		// for replacing the last UNION
		StrSql = StrSql.substring(0, StrSql.trim().length() - 5);
		StrSql = StrSql + " ) AS cal"
				// Start Quarterly Enquiry join
				+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS enquirycount, "
				+ " CONCAT('Qtr ',QUARTER(enquiry_date),'-',YEAR(enquiry_date)) AS qtr "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		StrSql += " WHERE 1=1" + StrSearch.replace("emp_id", "enquiry_emp_id")
				+ " GROUP BY qtr) AS tblenquiry ON tblenquiry.qtr = cal.calqtr"

				// Start Quarterly Test Drive join
				+ " LEFT JOIN (SELECT COUNT(testdrive_id) AS testdrivecount,"
				+ " CONCAT('Qtr ',QUARTER(testdrive_time),'-',YEAR(testdrive_time)) AS qtr "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}
		StrSql += " WHERE 1 = 1"
				+ " AND testdrive_fb_taken = 1 "
				+ StrSearch.replace("emp_id", "testdrive_emp_id")
				+ " GROUP BY qtr) AS tbltestdrive ON tbltestdrive.qtr = cal.calqtr"

				// Start Quarterly Booking join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS socount, "
				+ " CONCAT('Qtr ',QUARTER(so_date),'-',YEAR(so_date)) AS qtr"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}

		StrSql += StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}

		StrSql += " GROUP BY qtr) AS tblso ON tblso.qtr = cal.calqtr"

				// Start Quarterly Deliveries join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS deliveries,"
				+ " CONCAT('Qtr ',QUARTER(so_delivered_date),'-',YEAR(so_delivered_date)) AS qtr "
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}

		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY qtr) AS tbldelivery ON tbldelivery.qtr = cal.calqtr"
				// Start Quarterly Retails join
				+ " LEFT JOIN (SELECT COUNT(so_id) AS retails,"
				+ " CONCAT('Qtr ',QUARTER(so_retail_date),'-',YEAR(so_retail_date)) AS qtr,"
				+ " COALESCE(SUM(so_profitability_profit), 0) AS totalsoprofit"
				+ " FROM " + compdb(comp_id) + "axela_sales_so";
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}

		StrSql += " WHERE so_active=1 " + StrSearch.replace("emp_id", "so_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "item_model_id");
		}
		StrSql += " GROUP BY qtr) AS tblretails ON tblretails.qtr = cal.calqtr";

		// Start Enquiry Target join
		StrSql += " LEFT JOIN (SELECT SUM(modeltarget_enquiry_count) AS enquirytargetcount, target_startdate, "
				+ " CONCAT('Qtr ',QUARTER(target_startdate),'-',YEAR(target_startdate)) AS qtr"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}
		StrSql += " GROUP BY qtr"
				+ ") AS tblenquirytarget ON tblenquirytarget.qtr = cal.calqtr"

				// Start Retail Target join
				+ " LEFT JOIN (SELECT SUM(modeltarget_so_count) AS retailtargetcount, target_startdate,"
				+ " CONCAT('Qtr ',QUARTER(target_startdate),'-',YEAR(target_startdate)) AS qtr"
				+ " FROM " + compdb(comp_id) + "axela_sales_target"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
		// if (!model_id.equals("")) {
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_target_model ON modeltarget_target_id = target_id";
		// }
		if (!team_id.equals("") || team_ids != null) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = target_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";
		}
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		}
		StrSql += " WHERE 1=1 "
				+ StrSearch.replace("emp_id", "target_emp_id");
		if (!model_id.equals("")) {
			StrSql += StrModel.replace("enquiry_model_id", "modeltarget_model_id");
		}

		StrSql += " GROUP BY qtr"
				+ ") as tblretailtarget ON tblretailtarget.qtr = cal.calqtr";

		StrSql += " GROUP BY calqtr"
				+ " ORDER BY calstartdate";
		// SOP("StrSql==-------Quarterly----" + StrSql);
		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// start of table
				// Str3.append("<div class=\"table-bordered table-responsive \">\n");
				Str3.append("<table class=\"table table-hover table-bordered table-responsive \" data-filter=\"#filter\">\n");
				Str3.append("<thead><tr>\n");
				Str3.append("<th data-hide=\"phone\"></th>\n");
				while (crs.next()) {
					Str3.append("<th data-hide=\"phone\">").append(crs.getString("calqtr")).append("</th>\n");
				}
				Str3.append("</tr></thead>\n");
				crs.beforeFirst();
				Str3.append("<tbody>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Enquiries</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("enquiry")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Enquiry Target</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("enquirytarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Enquiry % Achieved</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("enquiry")), Integer.parseInt(crs.getString("enquirytarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Test Drives</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("testdrives")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Bookings</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("salesorders")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Retails</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("retails")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");

				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Retail Target</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("retailtarget")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");

				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Retail % Achieved</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(getPercentage(Integer.parseInt(crs.getString("retails")), Integer.parseInt(crs.getString("retailtarget"))))
							.append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");
				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Deliveries</b></td>\n");
				while (crs.next()) {
					Str3.append("<td style=\"text-align:right;\">").append(crs.getString("deliveries")).append("</td>\n");
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");

				Str3.append("<tr>\n");
				Str3.append("<td data-toggle=\"true\"><b>Profit</b></td>\n");
				while (crs.next()) {
					if (crs.getDouble("totalsoprofit") >= 0) {
						Str3.append("<td style=\"text-align:right;\"><b><font color='blue'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					} else {
						Str3.append("<td style=\"text-align:right;\"><b><font color='red'>").append(IndFormat(deci.format(crs.getDouble("totalsoprofit")))).append("</font></b></td>\n");
					}
				}
				crs.beforeFirst();
				Str3.append("</tr>\n");

				Str3.append("</tbody>\n");
				Str3.append("</table>\n");
				// Str3.append("</div>\n");
				// end of table
				crs.beforeFirst();

			} else {
				// NoChart = "<font color=red><b>No Data Found!</b></font>";
			}
		} catch (Exception ex) {
			System.out.println("Axelaauto===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PreparePieChartSOE() {
		StrSql = " SELECT soe_id, soe_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT Count(DISTINCT soe_id)";
		String StrJoin = " FROM " + compdb(comp_id) + "axela_soe "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_soe_id = soe_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id = enquiry_stage_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id = enquiry_status_id ";
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrJoin += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id ";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrJoin += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id ";
		}
		if (!model_id.equals("")) {
			StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		StrJoin += "  WHERE 1 = 1 " + EnquirySearch + "";
		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY soe_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("StrSql-----PreparePieChartSOE-------" + StrSql);
		try {
			if (crs.isBeforeFirst()) {
				chart_data_soe = "[";
				while (crs.next()) {
					count++;
					chart_data_soe = chart_data_soe + "{'type': '" + crs.getString("soe_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total_soe = chart_data_total_soe + crs.getInt("Total");
					// SOP("chart_data_total----------" + chart_data_total);
					if (count < TotalRecords) {
						chart_data_soe = chart_data_soe + ",";
					}
				}
				chart_data_soe = chart_data_soe + "]";
				// SOP("chart_data_soe----------" + chart_data_soe);
			} else {
				NoChart_soe = "No Enquiry Found!";
			}
			crs.close();
		} catch (SQLException ex) {
			Logger.getLogger(Report_SOE_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public String PreparePieChartSOB() {
		String StrSql = " SELECT sob_id, sob_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT COUNT(DISTINCT sob_id) ";
		String StrJoin = " FROM " + compdb(comp_id) + "axela_sob "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_sob_id = sob_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id";
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrJoin += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id ";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrJoin += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id ";
		}
		if (!model_id.equals("")) {
			StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		StrJoin += " WHERE 1 = 1 " + EnquirySearch + "";
		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY sob_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("PreparePieChartSOB------------------" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data_sob = "[";
				while (crs.next()) {
					count++;
					chart_data_sob = chart_data_sob + "{'type': '" + unescapehtml(crs.getString("sob_name")) + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total_sob = chart_data_total_sob + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data_sob = chart_data_sob + ",";
					}
				}
				chart_data_sob = chart_data_sob + "]";
			} else {
				NoChart_sob = "No Enquiry Found!";
			}
			crs.close();

		} catch (SQLException ex) {
			Logger.getLogger(Report_SOB_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}
	public String PreparePieChartStage() {
		String StrSql = " SELECT stage_id,stage_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT COUNT(DISTINCT stage_id) ";

		String StrJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_stage_id = stage_id";
		if (!branch_id.equals("") || branch_ids != null || !BranchAccess.equals("")) {
			StrJoin += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id ";
		}
		if (!team_id.equals("") || team_ids != null) {
			StrJoin += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id ";
		}
		if (!model_id.equals("")) {
			StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id";
		}
		StrJoin += "  WHERE 1 = 1" + EnquirySearch + "";

		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY stage_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("SqlS PreparePieChart===stage=" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data_stage = "[";
				while (crs.next()) {
					count++;
					chart_data_stage = chart_data_stage + "{'type': '" + crs.getString("stage_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total_stage = chart_data_total_stage + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data_stage = chart_data_stage + ",";
					}
				}
				chart_data_stage = chart_data_stage + "]";
				// SOP("chart_data_stage===" + chart_data_stage);

			} else {
				NoChart_stage = "No Enquiry Found!";
			}
			crs.close();

		} catch (SQLException ex) {
			Logger.getLogger(Report_SOB_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public String PreparePieChartStatus() {
		String StrSql = " SELECT status_id,status_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT COUNT(DISTINCT status_id) ";

		String StrJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_status_id = status_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
				+ " WHERE 1 = 1" + EnquirySearch + "";

		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY status_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("SqlS PreparePieChart===status=" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data_status = "[";
				while (crs.next()) {
					count++;
					chart_data_status = chart_data_status + "{'type': '" + crs.getString("status_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total_status = chart_data_total_status + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data_status = chart_data_status + ",";
					}
				}
				chart_data_status = chart_data_status + "]";
				// SOP("chart_data_status===" + chart_data_status);

			} else {
				NoChart_status = "No Enquiry Found!";
			}
			crs.close();

		} catch (SQLException ex) {
			Logger.getLogger(Report_SOB_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public String PreparePieChartPriority() {
		String StrSql = " SELECT priorityenquiry_id, priorityenquiry_name, COUNT(DISTINCT enquiry_id) AS Total ";
		String CountSql = " SELECT COUNT(DISTINCT priorityenquiry_id) ";

		String StrJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_priorityenquiry_id = priorityenquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
				+ " WHERE 1 = 1" + EnquirySearch + "";

		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY priorityenquiry_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		// SOP("SqlS PreparePieChart===priority=" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				chart_data_priority = "[";
				while (crs.next()) {
					count++;
					chart_data_priority = chart_data_priority + "{'type': '" + crs.getString("priorityenquiry_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total_priority = chart_data_total_priority + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data_priority = chart_data_priority + ",";
					}
				}
				chart_data_priority = chart_data_priority + "]";
				// SOP("chart_data_priority===" + chart_data_priority);

			} else {
				NoChart_priority = "No Enquiry Found!";
			}
			crs.close();

		} catch (SQLException ex) {
			Logger.getLogger(Report_SOB_Dash.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "";
	}

	public String ReturnQuarterDates(int year, int quarter) {
		int startmonth = 0;
		int endmonth = 0;
		SimpleDateFormat dateformat;
		Calendar calendarforstartdate, calendarforenddate;
		Date startdate, enddate;
		int quarterstartmonth = 0;
		quarterstartmonth = quarter == 1 ? 1 : quarter == 2 ? 4 : quarter == 3 ? 7 : 10;

		if (quarterstartmonth % 3 == 2) {
			quarterstartmonth = quarterstartmonth - 1;
		}
		else if (quarterstartmonth % 3 == 0) {
			quarterstartmonth = quarterstartmonth - 2;
		}
		startmonth = quarterstartmonth;
		endmonth = quarterstartmonth + 2;
		dateformat = new SimpleDateFormat("yyyyMMdd");
		// startmonth-1 to get exact month for startmonth in GregorianCalendar
		calendarforstartdate = new GregorianCalendar(year, startmonth - 1, Calendar.DAY_OF_MONTH);
		calendarforstartdate.set(Calendar.DAY_OF_MONTH, calendarforstartdate.getActualMinimum(Calendar.DAY_OF_MONTH));
		startdate = calendarforstartdate.getTime();
		quarterdates = dateformat.format(startdate).toString();
		// endmonth-1 to get exact month for endmonth in GregorianCalendar
		calendarforenddate = new GregorianCalendar(year, endmonth - 1, Calendar.DAY_OF_MONTH);
		calendarforenddate.set(Calendar.DAY_OF_MONTH, calendarforenddate.getActualMaximum(Calendar.DAY_OF_MONTH));
		enddate = calendarforenddate.getTime();
		quarterdates = quarterdates + dateformat.format(enddate).toString();
		return quarterdates;
	}
}