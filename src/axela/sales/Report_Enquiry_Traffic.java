package axela.sales;

//Manjur 13/05/2015
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_Traffic extends Connect {

	public String StrSql = "";
	public String endtime = "", end_time = "";
	public String dr_month = "";
	public String dr_year = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "", soe_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids, soe_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String enquiry_hour = "";
	public String enquiry_day = "";
	public String enquiry_month = "";
	public int enquirycount = 0;
	public int salesordercount = 0;
	public int deliverycount = 0;
	public String dr_traffic = "1";
	public String[] x;
	public String enquiry_count = "";
	public String StrSearch = "";
	public String chart_data = "";
	public String Stremp = "";
	public String Strbranch = "";
	public String Strteam = "";
	public String Strmodel = "";
	public String StrSoe = "";
	public static int year;
	public static int month;
	public static int maxdays;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	String StrEnquiry = "";
	String StrSo = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				dr_traffic = CNumeric(PadQuotes(request.getParameter("dr_traffic")));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				if (dr_traffic.equals("0")) {
					dr_traffic = "1";
				}
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + ExeAccess;

					// if (!exe_id.equals("")) {
					// StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					// }
					// if (!brand_id.equals("")) {
					// StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					// }
					//
					// if (!branch_id.equals("")) {
					// StrSearch = StrSearch + " AND branch_id IN(" + branch_id + ")";
					// }
					// if (!team_id.equals("")) {
					// StrSearch = StrSearch
					// + " AND emp_id IN (SELECT teamtrans_emp_id "
					// + " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN ("
					// + team_id + "))";
					// }
					// if (!model_id.equals("")) {
					// StrSearch = StrSearch + " AND enquiry_model_id IN ("
					// + model_id + ")";
					// }
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
				}

				if (msg.equals("")) {
					if (dr_traffic.equals("1")) {
						ListEnquiryHourly();
					}
					if (dr_traffic.equals("2")) {
						ListEnquiryDaily();
					}
					if (dr_traffic.equals("3")) {
						ListEnquiryMonthly();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}
		month = (Integer.parseInt(dr_month));
		if (dr_year.equals("")) {
			dr_year = ToShortDate(kknow()).substring(0, 4);
		}
		year = (Integer.parseInt(dr_year));

		endtime = PadQuotes(request.getParameter("txt_endtime"));

		if (dr_traffic.equals("1")) {
			x = new String[24];
		} else if (dr_traffic.equals("2")) {
			x = new String[32];
		} else if (dr_traffic.equals("3")) {
			x = new String[13];
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (dr_traffic.equals("1")) {
			for (int i = 0; i < 24; i++) {
				x[i] = ToLongDate(AddHoursDate(
						StringToDate(ConvertShortDateToStr(endtime)), 0, i, 0));
			}
		}
		if (dr_traffic.equals("2")) {
			int temp = daysInMonth();
			for (int i = 1; i <= temp; i++) {
				if ((i + "").length() == 1) {
					x[i] = dr_year + dr_month + "0" + i + "000000";
				} else {
					x[i] = dr_year + dr_month + i + "000000";
				}
				// SOP("x[i]---------" + x[i]);
			}
		}
		if (dr_traffic.equals("3")) {
			for (int i = 1; i <= 12; i++) {
				if (i < 10) {
					x[i] = dr_year + "0" + i;
				} else {
					x[i] = dr_year + i;
				}
				// SOP("x[i]---------" + x[i]);
			}
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
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
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");

		if (!branch_id.equals("") || !BranchAccess.equals("") || !region_id.equals("") || !brand_id.equals("")) {
			mischeck.exe_branch_id = branch_id;
			StrEnquiry = StrEnquiry + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id ";
		}
		if (!team_id.equals("")) {
			mischeck.exe_branch_id = branch_id;
			mischeck.branch_id = branch_id;
			StrEnquiry = StrEnquiry + " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id";
		}
		if (!exe_id.equals("")) {
			StrEnquiry = StrEnquiry + " AND enquiry_emp_id IN (" + exe_id + ")";
		}
		if (!model_id.equals("") && !model_ids.equals("")) {
			StrEnquiry += " AND enquiry_model_id IN (" + model_id + ")";
		}

		// SOP("brand_id===" + brand_id);
		if (!brand_id.equals("")) {
			StrEnquiry = StrEnquiry + " AND branch_brand_id IN (" + brand_id + ")";
		}
		if (!region_id.equals("")) {
			StrEnquiry = StrEnquiry + " AND branch_region_id IN (" + region_id + ")";
		}
		if (!branch_id.equals("")) {
			StrEnquiry = StrEnquiry + " AND enquiry_branch_id IN (" + branch_id + ")" + BranchAccess;
		}
		if (!team_id.equals("")) {
			StrEnquiry = StrEnquiry + " AND teamtrans_team_id IN (" + team_id + ")";
		}

		if (!branch_id.equals("") || !BranchAccess.equals("") || !region_id.equals("") || !brand_id.equals("")) {
			StrSo = StrSo + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id ";
		}
		if (!team_id.equals("")) {
			StrSo = StrSo + " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id";
		}
		if (!exe_id.equals("")) {
			StrSo = StrSo + " AND so_emp_id IN (" + exe_id + ")";
		}
		if (!model_id.equals("") && !model_ids.equals(""))
		{
			StrSo += " AND so_item_id IN (" + model_id + ")";
		}

		if (!brand_id.equals("")) {
			StrSo = StrSo + " AND branch_brand_id IN (" + brand_id + ")";
		}
		if (!region_id.equals("")) {
			StrSo = StrSo + " AND branch_region_id IN (" + region_id + ")";
		}
		if (!branch_id.equals("")) {
			StrSo = StrSo + " AND branch_id IN (" + branch_id + ")" + BranchAccess;
		}

		if (!team_id.equals("")) {
			StrSo = StrSo + " AND teamtrans_team_id IN (" + team_id + ")";
		}
		if (!soe_id.equals("")) {
			StrSoe = " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id ";
		}

	}
	protected void CheckForm() {
		msg = "";
		if (dr_traffic.equals("1")) {
			if (!endtime.equals("")) {
				if (isValidDateFormatShort(endtime)) {
					endtime = ConvertShortDateToStr(endtime);
					end_time = strToShortDate(endtime);
					// SOP("end_time====" + end_time);
					// endtime = ToLongDate(end_time);
				} else {
					msg = msg + "<br>Enter Valid Date!";
					endtime = "";
				}
			}
			if (endtime.equals("")) {
				msg = msg + "<br>Select Date!<br>";
			}
			// SOP("endtime-------" + endtime);
		}
		if (dr_traffic.equals("2")) {
			if ((dr_month.equals("")) && (dr_year.equals(""))) {
				msg += "<br>Invalid Month Or Year!";
			}
			if ((dr_month.equals("-1")) && (!dr_year.equals("-1"))) {
				msg += "<br>Enter Month!";
			}
			if (msg.equals("")) {
				endtime = dr_year + dr_month + "00000000";
			}
		}
		if (dr_traffic.equals("3")) {
			if (dr_year.equals("")) {
				msg += "<br>Invalid Year!";
			}
			if (dr_year.equals("-1")) {
				msg += "<br>Enter Month!";
			}
			if (msg.equals("")) {
				endtime = dr_year + "0000000000";
			}
		}
		if ((dr_traffic.equals("2") || dr_traffic.equals("3")) && !endtime.equals("")) {
			end_time = DateToShortDate(kknow());
		}
	}

	public void ListEnquiryHourly() {
		try {
			CachedRowSet crs = null;
			Map<Integer, String> enquirymaphourly = new LinkedHashMap<Integer, String>();
			Map<Integer, String> salesordermaphourly = new LinkedHashMap<Integer, String>();
			Map<Integer, String> deliveriesmaphourly = new LinkedHashMap<Integer, String>();

			// start of hourly enquirys

			StrSql = " SELECT CONCAT(HOUR(calhour)) AS time, "
					+ " COALESCE(enqcount,0) AS enquirys,"
					+ " COALESCE(socount,0) AS salesorders, "
					+ " COALESCE(delcount,0) AS deliveries "
					+ " FROM (";
			for (int i = 0; i < x.length; i++) {
				StrSql = StrSql + " SELECT " + (x[i]) + " AS calhour ";
				if (i != x.length - 1) {
					StrSql = StrSql + " UNION ";
				}
			}
			StrEnquiry = StrEnquiry.replace("enquiry_date", "CONCAT( SUBSTR(enquiry_date, 1, 8), SUBSTR(enquiry_entry_date, 9, 2) )");
			StrEnquiry = StrEnquiry.replace("cal.calday", "SUBSTR(cal.calhour, 1, 10)");
			StrSql = StrSql + " ) AS cal"

					// enquiry join
					+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS enqcount, CONCAT(SUBSTR(enquiry_date, 1, 8), "
					+ " SUBSTR(enquiry_entry_date, 9, 2) ) AS enqdate "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ StrEnquiry
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 8) = SUBSTR(" + endtime + ", 1, 8)";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY enqdate ) AS tblenq ON tblenq.enqdate = SUBSTR(cal.calhour, 1, 10)"

					// So join
					+ " LEFT JOIN (SELECT COUNT(so_id) AS socount, CONCAT(SUBSTR(so_date, 1, 8), "
					+ " SUBSTR(so_entry_date, 9, 2) ) AS sodate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "CONCAT( SUBSTR(so_date, 1, 8), SUBSTR(so_entry_date, 9, 2) )")
							.replace("cal.calday", " SUBSTR(cal.calhour, 1, 10) ")
					+ " WHERE 1 = 1 "
					+ " AND so_active = 1"
					+ " AND SUBSTR(so_date, 1, 8) = SUBSTR(" + endtime + ", 1, 8)";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY sodate ) AS tblso ON tblso.sodate = SUBSTR(cal.calhour, 1, 10)"

					// deliveries join
					+ " LEFT JOIN ( SELECT COUNT(so_id) AS delcount, CONCAT( SUBSTR(so_delivered_date, 1, 8), "
					+ " SUBSTR(so_delivered_date, 9, 2) ) AS deldate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "CONCAT( SUBSTR(so_delivered_date, 1, 8), SUBSTR(so_delivered_date, 9, 2) )")
							.replace("cal.calday", "SUBSTR(cal.calhour, 1, 10)")
					+ " WHERE 1 = 1 "
					+ " AND so_active = 1"
					+ " AND SUBSTR(so_delivered_date, 1, 8) = SUBSTR(" + endtime + ", 1, 8)";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY deldate ) AS tbldel ON tbldel.deldate = SUBSTR(cal.calhour, 1, 10)"
					+ " WHERE 1 = 1"
					+ " GROUP BY calhour"
					+ " ORDER BY calhour";
			// if (emp_id.equals("37") && comp_id.equals("1011")) {
			SOP("StrSql==Hourly= enquirys=" + StrSql);
			// }

			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				enquirymaphourly.put(crs.getInt("time"), crs.getString("enquirys"));
				salesordermaphourly.put(crs.getInt("time"), crs.getString("salesorders"));
				deliveriesmaphourly.put(crs.getInt("time"), crs.getString("deliveries"));
			}

			chart_data = "[";
			for (Map.Entry<Integer, String> entry : deliveriesmaphourly.entrySet()) {
				enquirycount += Integer.parseInt(enquirymaphourly.get(entry.getKey()));
				salesordercount += Integer.parseInt(salesordermaphourly.get(entry.getKey()));
				deliverycount += Integer.parseInt(deliveriesmaphourly.get(entry.getKey()));
				chart_data = chart_data + "{'hour': '" + entry.getKey() + "'"
						+ ", 'column-1':'" + Integer.parseInt(enquirymaphourly.get(entry.getKey())) + "'"
						+ ", 'column-2':'" + Integer.parseInt(salesordermaphourly.get(entry.getKey())) + "'"
						+ ", 'column-3':'" + Integer.parseInt(deliveriesmaphourly.get(entry.getKey())) + "'},";
				// SOP("entry.getKey()" + entry.getKey());
			}
			chart_data = chart_data.substring(0, chart_data.lastIndexOf(","));
			chart_data = chart_data + "]";
			// SOPInfo("chart_data==for hourly-----------" + chart_data);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	/**
	 * 
	 */
	public void ListEnquiryDaily() {
		try {
			Map<Integer, String> enquirymapdaily = new LinkedHashMap<Integer, String>();
			Map<Integer, String> salesordermapdaily = new LinkedHashMap<Integer, String>();
			Map<Integer, String> deliveriesmapdaily = new LinkedHashMap<Integer, String>();
			// endtime = ConvertShortDateToStr(endtime);
			// SOP("endtime=====" + endtime);
			int temp = daysInMonth();

			// Stremp = " AND enquiry_emp_id IN (" + exe_id + ")";
			// Strmodel = " AND enquiry_model_id IN (" + model_id + ")";
			// Strbranch = " AND branch_id IN(" + branch_id + ")";
			// Strteam = " AND emp_id IN (SELECT teamtrans_emp_id "
			// + " FROM " + compdb(comp_id)
			// + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";

			// start of daily enquiry

			StrSql = "SELECT concat(DAY(calday)) AS DAY, "
					+ " COALESCE(enqcount,0) AS enquirys,"
					+ " COALESCE(socount,0) AS salesorders, "
					+ " COALESCE(delcount,0) AS deliveries "
					+ " FROM (";

			for (int i = 1; i <= temp; i++) {
				StrSql = StrSql + " SELECT " + x[i] + " AS calday ";
				if (i != temp) {
					StrSql = StrSql + " UNION ";
				}
			}
			StrSql = StrSql + ") AS cal";

			// StrEnquiry = StrEnquiry.replace("enquiry_date", "CONCAT( SUBSTR(enquiry_date, 1, 8), SUBSTR(enquiry_entry_date, 9, 2) )");

			// enquiry join
			StrSql += " LEFT JOIN ( SELECT COUNT(enquiry_id) AS enqcount, SUBSTR(enquiry_date, 1, 8) AS enqdate "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ StrEnquiry
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(enquiry_date, 1, 6) = SUBSTR(" + endtime + ", 1, 6)";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY enqdate ) AS tblenq ON tblenq.enqdate = SUBSTR(cal.calday, 1, 8)"

					// So join
					+ " LEFT JOIN ( SELECT COUNT(so_id) AS socount, SUBSTR(so_date, 1, 8) AS sodate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "SUBSTR(so_date, 1, 8)")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(so_date, 1, 6) = SUBSTR(" + endtime + ", 1, 6)"
					+ " AND so_active = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY sodate ) AS tblso ON tblso.sodate = SUBSTR(cal.calday, 1, 8)"

					// deliveries join
					+ " LEFT JOIN ( SELECT COUNT(so_id) AS delcount, SUBSTR(so_delivered_date, 1, 8) AS deldate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "SUBSTR(so_delivered_date, 1, 8)")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(so_delivered_date, 1, 6) = SUBSTR(" + endtime + ", 1, 6)"
					+ " AND so_active = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY deldate ) AS tbldel ON tbldel.deldate = SUBSTR(cal.calday, 1, 8)"

					+ " WHERE 1 = 1"
					+ " GROUP BY calday"
					+ " ORDER BY calday";

			SOP("StrSql=for ListEnquiryDaily====" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				enquirymapdaily.put(crs1.getInt("DAY"), crs1.getString("enquirys"));
				salesordermapdaily.put(crs1.getInt("DAY"), crs1.getString("salesorders"));
				deliveriesmapdaily.put(crs1.getInt("DAY"), crs1.getString("deliveries"));
			}

			// end of daily deliveries

			chart_data = "[";
			for (Map.Entry<Integer, String> entry : enquirymapdaily.entrySet()) {

				enquirycount += Integer.parseInt(enquirymapdaily.get(entry.getKey()));
				salesordercount += Integer.parseInt(salesordermapdaily.get(entry.getKey()));
				deliverycount += Integer.parseInt(deliveriesmapdaily.get(entry.getKey()));
				chart_data = chart_data + "{'hour': '" + entry.getKey() + "'"
						+ ", 'column-1':'" + Integer.parseInt(enquirymapdaily.get(entry.getKey())) + "'"
						+ ", 'column-2':'" + Integer.parseInt(salesordermapdaily.get(entry.getKey())) + "'"
						+ ", 'column-3':'" + Integer.parseInt(deliveriesmapdaily.get(entry.getKey())) + "'},";
			}
			// SOPInfo("chart_data=for monthly---------" + chart_data);
			chart_data = chart_data.substring(0, chart_data.lastIndexOf(","));
			chart_data = chart_data + "]";

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ListEnquiryMonthly() {
		try {
			CachedRowSet crs = null;
			Map<String, String> enquirymonthlymap = new LinkedHashMap<String, String>();
			Map<String, String> salesordersmonthlymap = new LinkedHashMap<String, String>();
			Map<String, String> deliveriesmonthlymap = new LinkedHashMap<String, String>();
			int temp = daysInMonth();
			// endtime = ConvertShortDateToStr(endtime);
			// SOP("endtime====" + endtime);
			// Staring of monthly enquiry

			StrSql = "SELECT MONTHNAME(concat(calmonth, '00')) AS MONTH, "
					+ " COALESCE(enqcount,0) AS enquirys,"
					+ " COALESCE(socount,0) AS salesorders, "
					+ " COALESCE(delcount,0) AS deliveries "
					+ " FROM (";

			for (int i = 1; i <= 12; i++) {
				StrSql = StrSql + " SELECT " + x[i] + " AS calmonth ";
				if (i != 12) {
					StrSql = StrSql + " UNION ";
				}
			}
			StrEnquiry = StrEnquiry.replace("enquiry_date", "SUBSTR(enquiry_date, 1, 6)");
			StrEnquiry = StrEnquiry.replace("cal.calday", "cal.calmonth");
			StrSql = StrSql + " ) AS cal"

					// enquiry join
					+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS enqcount, SUBSTR(enquiry_date, 1, 6) AS enqdate "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ StrEnquiry
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(enquiry_date, 1, 4) = SUBSTR(" + endtime + ", 1, 4)";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY enqdate ) AS tblenq ON tblenq.enqdate = SUBSTR(cal.calmonth, 1, 6)"

					// So join
					+ " LEFT JOIN (SELECT COUNT(so_id) AS socount, SUBSTR(so_date, 1, 6) AS sodate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "CONCAT( SUBSTR(so_date, 1, 8), SUBSTR(so_entry_date, 9, 2) )")
							.replace("cal.calday", " SUBSTR(cal.calmonth, 1, 10) ")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(so_date, 1, 4) = SUBSTR(" + endtime + ", 1, 4)"
					+ " AND so_active = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY sodate ) AS tblso ON tblso.sodate = SUBSTR(cal.calmonth, 1, 6)"

					// deliveries join
					+ " LEFT JOIN ( SELECT COUNT(so_id) AS delcount, SUBSTR(so_delivered_date, 1, 6) AS deldate "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ StrSoe
					+ StrSo.replace("so_date", "CONCAT( SUBSTR(so_delivered_date, 1, 8), SUBSTR(so_delivered_date, 9, 2) )")
							.replace("cal.calday", "SUBSTR(cal.calmonth, 1, 10)")
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(so_delivered_date, 1, 4) = SUBSTR(" + endtime + ", 1, 4)"
					+ " AND so_active = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND enquiry_soe_id IN (" + soe_id + ") ";
			}
			StrSql += ExeAccess.replace("emp_id", "so_emp_id")
					+ " GROUP BY deldate ) AS tbldel ON tbldel.deldate = SUBSTR(cal.calmonth, 1, 6)"
					+ " WHERE 1 = 1"
					+ " GROUP BY calmonth"
					+ " ORDER BY calmonth";
			SOP("StrSql==monthly===" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				enquirymonthlymap.put(crs.getString("MONTH"), crs.getString("enquirys"));
				salesordersmonthlymap.put(crs.getString("MONTH"), crs.getString("salesorders"));
				deliveriesmonthlymap.put(crs.getString("MONTH"), crs.getString("deliveries"));
			}

			// end of monthly deliveries

			chart_data = "[";
			for (Map.Entry<String, String> entry : enquirymonthlymap.entrySet()) {

				enquirycount += Integer.parseInt(enquirymonthlymap.get(entry.getKey()));
				salesordercount += Integer.parseInt(salesordersmonthlymap.get(entry.getKey()));
				deliverycount += Integer.parseInt(deliveriesmonthlymap.get(entry.getKey()));
				chart_data = chart_data + "{'hour': '" + entry.getKey() + "'"
						+ ", 'column-1':'" + Integer.parseInt(enquirymonthlymap.get(entry.getKey())) + "'"
						+ ", 'column-2':'" + Integer.parseInt(salesordersmonthlymap.get(entry.getKey())) + "'"
						+ ", 'column-3':'" + Integer.parseInt(deliveriesmonthlymap.get(entry.getKey())) + "'},";
			}
			chart_data = chart_data.substring(0, chart_data.lastIndexOf(","));
			chart_data = chart_data + "]";
			// SOP("chart_data--Monthly----------" + chart_data);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateTraffic() {
		StringBuilder sb = new StringBuilder();
		// sb.append("<SELECT>");
		// sb.append("<option value=0>--</option>\n");
		sb.append("<option value=1" + StrSelectdrop("1", dr_traffic) + ">Hourly</option>\n");
		sb.append("<option value=2" + StrSelectdrop("2", dr_traffic) + ">Daily</option>\n");
		sb.append("<option value=3" + StrSelectdrop("3", dr_traffic) + ">Monthly</option>\n");
		// sb.append("</SELECT>");
		return sb.toString();
	}

	public String PopulateYears() {

		String year = ToShortDate(kknow()).substring(0, 4);
		StringBuilder years = new StringBuilder();
		for (int i = Integer.parseInt(year); i > Integer.parseInt(year) - 3; i--) {
			years.append("<option value = " + doublenum(i) + ""
					+ StrSelectdrop(doublenum(i), dr_year) + ">" + i + "</option>\n");
		}
		return years.toString();
	}

	public String PopulateMonths() {
		String months = "";
		months += "<option value=01" + StrSelectdrop(doublenum(1), dr_month) + ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dr_month) + ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dr_month) + ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dr_month) + ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dr_month) + ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dr_month) + ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dr_month) + ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dr_month) + ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dr_month) + ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dr_month) + ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dr_month) + ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dr_month) + ">December</option>\n";
		return months;
	}

	public static int daysInMonth() {
		int date = 0;
		GregorianCalendar Calendar = new GregorianCalendar(year, month, year);
		Calendar.set(year, month, date);
		maxdays = Calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxdays;
	}

	public String PopulateSOE() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " WHERE 1 = 1 "
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
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
