package axela.preowned;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Pre_Owned_Monitoring_Board extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String BranchAccess = "", dr_branch_id = "0";
	public String include_inactive_exe = "";
	public String StrSearch = "", StrPreOwnedFilter = "", StrPreOwnedModelFilter = "", StrSearchMain = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", carmanuf_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, carmanuf_ids;
	public String StrJoin = "";
	public String dr_totalby = "0", dr_orderby = "0";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";
	public String ExeAccess = "";
	public String SearchURL = "<a href=../preowned/preowned-check-search.jsp?", idname = "", SearchURLType = "";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_preowned_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {
					CheckForm();
					// StrSearch = BranchAccess.replaceAll("branch_id", "emp_branch_id").replace(")", "  or emp_branch_id=0)")
					// + " " + ExeAccess;
					// StrSearch = ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch = StrSearch + " AND branch_brand_id IN (" + brand_id + ")";
					}
					if (!region_id.equals("")) {
						StrSearch = StrSearch + " AND branch_region_id IN (" + region_id + ")";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					// SOP("team_id----" + team_id);
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						// StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preowned_team_id ";
						StrPreOwnedFilter += " AND preownedteamtrans_team_id IN (" + team_id + ") ";
					}
					// SOP("exe_id----" + exe_id);
					if (!exe_id.equals("")) {
						StrPreOwnedFilter += " AND preowned_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrPreOwnedModelFilter += " AND preownedmodel_id IN (" + model_id + ")";
					}

					if (!carmanuf_id.equals("")) {
						StrPreOwnedModelFilter += " AND carmanuf_id IN (" + carmanuf_id + ")";
					}
					if (!team_id.equals("") && !team_id.equals("0")) {
						StrSearchMain += " AND preownedteamtrans_team_id IN (" + team_id + ")";
					}
					if (!exe_id.equals("") && !exe_id.equals("0")) {
						StrSearchMain += " AND emp_id IN (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						StrHTML = ListPreOwnedMonitoringBoard();
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
			start_time = ReportStartdate();
		}
		if (endtime.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
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
		team_id = RetrunSelectArrVal(request, "dr_preownedteam");
		team_ids = request.getParameterValues("dr_preownedteam");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		carmanuf_id = RetrunSelectArrVal(request, "dr_manufacturer");
		carmanuf_ids = request.getParameterValues("dr_manufacturer");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");

		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));
		dr_orderby = PadQuotes(request.getParameter("dr_orderby"));

		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		if (include_inactive_exe.equals("on")) {
			include_inactive_exe = "1";
		}
		else {
			include_inactive_exe = "0";
		}
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg += "<br>Select Branch!";
		// }

		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}

		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}

		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListPreOwnedMonitoringBoard() {
		int empid = 0, empcount = 0;
		int total_sales_enq = 0, total_enquiry_closed = 0, total_enquiry_hot = 0, total_enquiry_open = 0;
		int total_meeting_completed = 0, total_meeting_planned = 0, total_KPImeetings = 0;
		int total_enquiry_followup = 0, total_followup_esc = 0;
		int total_enquiry_testdrivesplanned = 0, total_enquiry_testdrivescompleted = 0, total_enquiry_KPItestdrives = 0, total_enquirycustomertestdrivefeedback = 0;
		int total_preowned_count = 0, total_eval_count = 0, total_purchased_count = 0;
		int total_preowned_target_enquiry = 0, total_preowned_target_eval = 0, total_preowned_target_purchase = 0;
		Double total_tat_count = 0.0;
		// int total_quote_count = 0, total_quote_kpi_count = 0;
		// int total_so_count = 0, total_so_value = 0;
		// int total_receipt_count = 0, total_receipt_value = 0;
		// double salesratio = 0, total_salesratio = 0;
		double testdriveratio = 0, total_testdriveratio = 0;

		StringBuilder Str = new StringBuilder();

		StrSql = " SELECT emp_id, ";
		if (dr_totalby.equals("emp_id")) {
			StrSql += " emp_name, emp_ref_no, emp_active,";
		}
		if (dr_totalby.equals("emp_branch_id")) {
			StrSql += " emp_branch_id, branch_name, ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id")) {
			StrSql += " preownedteamtrans_team_id, preownedteam_name, ";
		}
		if (dr_totalby.equals("branch_region_id")) {
			StrSql += " branch_region_id, region_name, ";
		}
		StrSql += " COALESCE (SUM(preownedtarget_enquiry), 0) AS preownedtarget_enquiry, "
				+ " COALESCE (SUM(preownedtarget_enquiry_eval), 0) AS preownedtarget_enquiry_eval,"
				+ " COALESCE (SUM(preownedtarget_purchase), 0) AS preownedtarget_purchase,"
				+ " COALESCE (SUM(preownedcount), 0) AS preownedcount,"
				+ " COALESCE (SUM(saleenqcount), 0) AS saleenqcount,"
				+ " COALESCE (SUM(enquiryopen), 0) AS enquiryopen,"
				+ " COALESCE (SUM(enquiryclosed), 0) AS enquiryclosed,"
				+ " COALESCE (SUM(enquiryhot), 0) AS enquiryhot,"
				+ " COALESCE (SUM(evalcount), 0) AS evalcount,"
				+ " COALESCE (SUM(tatcount), 0) AS tatcount,"
				+ " COALESCE (SUM(meetingplanned), 0) AS meetingplanned,"
				+ " COALESCE (SUM(meetingcompleted), 0) AS meetingcompleted,"
				+ " COALESCE (SUM(kpimeetings), 0) AS kpimeetings,"
				+ " COALESCE (SUM(enquiryfollowup), 0) AS enquiryfollowup,"
				+ " COALESCE (SUM(followupesc), 0) AS followupesc,"
				+ " COALESCE (SUM(enquiry_testdrivesplanned), 0) AS enquiry_testdrivesplanned,"
				+ " COALESCE (SUM(enquiry_testdrivescompleted), 0) AS enquiry_testdrivescompleted,"
				+ " COALESCE (SUM(enquiry_KPItestdrives), 0) AS enquiry_KPItestdrives,"
				// + " COALESCE (SUM(enquirycustomertestdrivefeedback), 0) AS enquirycustomertestdrivefeedback,"
				+ " COALESCE (SUM(purchased), 0) AS purchased";
		// + " COALESCE (SUM(quotecount), 0) AS quotecount,"
		// + " COALESCE (SUM(quotekpicount), 0) AS quotekpicount,"
		// + " COALESCE (SUM(socount), 0) AS socount,"
		// + " COALESCE (SUM(soamount), 0) AS soamount,"
		// + " COALESCE (SUM(receiptcount), 0) AS receiptcount,"
		// + " COALESCE (SUM(receiptamount), 0) AS receiptamount ";

		// main table
		StrSql += " FROM " + compdb(comp_id) + "axela_emp";

		if (dr_totalby.equals("emp_branch_id") || dr_totalby.equals("branch_region_id") || !brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}

		StrSql += " LEFT JOIN ( "
				+ " SELECT "
				+ "	SUM(preownedtarget_enquiry_count) AS preownedtarget_enquiry, "
				+ "	SUM(preownedtarget_enquiry_eval_count) AS preownedtarget_enquiry_eval, "
				+ "	SUM(preownedtarget_purchase_count) AS preownedtarget_purchase, "
				+ "	preownedtarget_emp_id "
				+ "	FROM " + compdb(comp_id) + "axela_preowned_target ";
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preownedtarget_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}

		StrSql += " WHERE 1 = 1"
				+ " AND SUBSTR(preownedtarget_startdate, 1, 6) >= SUBSTR('" + starttime + "', 1, 6) "
				+ " AND SUBSTR(preownedtarget_enddate, 1, 6) <= SUBSTR('" + endtime + "', 1, 6) "
				+ ExeAccess.replace("emp_id", "preownedtarget_emp_id")
				+ StrPreOwnedFilter.replace("preowned_emp_id", "preownedtarget_emp_id");

		StrSql += " GROUP BY preownedtarget_emp_id "
				+ " ) AS tbltarget ON tbltarget.preownedtarget_emp_id = emp_id ";

		StrSql += " LEFT JOIN ( SELECT "
				// preownedcount
				+ " COUNT( DISTINCT CASE WHEN preowned_enquiry_id = 0"
				+ " AND SUBSTR(preowned_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(preowned_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " THEN preowned_id END ) AS preownedcount,"

				// saleenqcount
				+ " COUNT( DISTINCT CASE WHEN preowned_enquiry_id != 0"
				+ " AND SUBSTR(preowned_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(preowned_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " THEN preowned_id END ) AS saleenqcount,"

				// enquiryopen
				+ " COUNT( DISTINCT CASE"
				+ " WHEN preowned_preownedstatus_id = 1"
				+ " AND preowned_enquiry_id != 0"
				+ " AND SUBSTR(preowned_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " THEN preowned_id END ) AS enquiryopen,"

				// enquiryclosed
				+ " COUNT( DISTINCT CASE"
				+ " WHEN SUBSTR(preowned_preownedstatus_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
				+ " AND SUBSTR(	preowned_preownedstatus_date,1,8) <= SUBSTR('" + endtime + "',1, 8)"
				+ " AND preowned_enquiry_id != 0"
				+ " AND preowned_preownedstatus_id >= 2"
				+ " THEN preowned_id END ) AS enquiryclosed,"

				// enquiryhot
				+ " COUNT( DISTINCT CASE WHEN preowned_prioritypreowned_id = 1 AND preowned_preownedstatus_id = 1"
				+ " AND SUBSTR(preowned_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND preowned_enquiry_id != 0"
				+ " THEN preowned_id END ) AS enquiryhot,"

				+ " preowned_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned";
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}

		if (!model_id.equals("") || !carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		}
		if (!carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		}
		StrSql += " WHERE 1 = 1"
				+ BranchAccess.replace("branch_id", "preowned_branch_id")
				+ ExeAccess.replace("emp_id", "preowned_emp_id")
				+ StrSearch
				+ StrPreOwnedFilter
				+ StrPreOwnedModelFilter;

		StrSql += " GROUP BY preowned_emp_id"
				+ " ) AS tblpreowned ON tblpreowned.preowned_emp_id = emp_id"

				+ " LEFT JOIN (SELECT"
				// evalcount
				+ " COUNT(DISTINCT eval_id) AS evalcount,"

				+ " eval_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_eval"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = eval_preowned_id";
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}
		if (!model_id.equals("") || !carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		}
		if (!carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		}
		StrSql += " WHERE 1 = 1"
				+ BranchAccess.replace("branch_id", "preowned_branch_id")
				+ ExeAccess.replace("emp_id", "preowned_emp_id")
				+ StrSearch
				+ StrPreOwnedFilter
				+ StrPreOwnedModelFilter;

		StrSql += " AND SUBSTR(eval_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(eval_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " GROUP BY eval_emp_id"
				+ " ) AS tbleval ON tbleval.eval_emp_id = emp_id"

				+ " LEFT JOIN ( SELECT"
				// tatcount
				+ " COALESCE(FORMAT(DATEDIFF(CONCAT(SUBSTR(IF (eval_date != '',eval_date," + ToLongDate(kknow()) + "), 1, 8),"
				+ " SUBSTR(IF(eval_entry_date != '',eval_entry_date, " + ToLongDate(kknow()) + "), 9, 6)),"
				+ " CONCAT(SUBSTR(preowned_date, 1, 8),SUBSTR(preowned_entry_date, 9, 6))) / COUNT(DISTINCT eval_id), 2), 0) AS tatcount,"

				// meetingplanned
				+ " SUM(IF(preownedfollowup_preownedfollowuptype_id IN ( 2, 4), 1, 0)) AS meetingplanned,"

				// meetingcompleted
				+ " SUM(IF(preownedfollowup_preownedfeedbacktype_id IN ( 2, 5), 1, 0)) AS meetingcompleted,"

				// kpimeetings
				+ " COUNT(DISTINCT CASE WHEN preownedfollowup_preownedfeedbacktype_id IN ( 2, 5) THEN preownedfollowup_id END) AS kpimeetings,"

				// enquiryfollowup
				+ " COUNT(DISTINCT preownedfollowup_id) AS enquiryfollowup,"

				// followupesc
				+ " COUNT(DISTINCT CASE WHEN preownedfollowup_trigger = 5"
				+ " THEN preownedfollowup_id END) AS followupesc,"

				+ " preowned_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_followup ON preownedfollowup_preowned_id = preowned_id";
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}
		if (!model_id.equals("") || !carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		}
		if (!carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		}
		StrSql += " WHERE 1 = 1"
				+ BranchAccess.replace("branch_id", "preowned_branch_id")
				+ ExeAccess.replace("emp_id", "preowned_emp_id")
				+ StrSearch
				+ StrPreOwnedFilter
				+ StrPreOwnedModelFilter;
		StrSql += " AND SUBSTR(preownedfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(preownedfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"

				+ " GROUP BY preowned_emp_id"
				+ " ) AS tbltat ON tbltat.preowned_emp_id = emp_id"

				+ " LEFT JOIN ( SELECT"
				// enquiry_testdrivesplanned
				+ " COUNT(testdrive_id) AS enquiry_testdrivesplanned,"

				// enquiry_testdrivescompleted
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1 AND testdrive_fb_entry_date != ''"
				+ " THEN testdrive_id END) AS enquiry_testdrivescompleted," // for checking TD completed

				// enquiry_KPItestdrives
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND testdrive_fb_entry_date != ''"
				+ " THEN testdrive_enquiry_id END) AS enquiry_KPItestdrives," // for checking TD KPI completed

				// enquirycustomertestdrivefeedback
				+ " COUNT(DISTINCT CASE WHEN testdrive_fb_taken = 1"
				+ " AND testdrive_fb_entry_date != ''"
				+ " THEN testdrive_id END) AS enquirycustomertestdrivefeedback," // for checking TD total

				+ " testdrive_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_testdrive ON testdrive_enquiry_id = enquiry_id";
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = enquiry_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}
		// if (!model_id.equals("") || !carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = enquiry_preownedvariant_id";
		// }

		// if (!carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
		// + " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		// }
		StrSql += " WHERE 1 = 1"
				+ " AND enquiry_enquirytype_id = 2"
				+ " AND SUBSTR(testdrive_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(testdrive_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ BranchAccess.replace("branch_id", "enquiry_branch_id")
				+ ExeAccess.replace("emp_id", "enquiry_emp_id")
				+ StrSearch
				+ StrPreOwnedFilter.replace("preowned_emp_id", "enquiry_emp_id");
		StrSql += " GROUP BY testdrive_emp_id"
				+ " ) AS tbltestdrive ON tbltestdrive.testdrive_emp_id = emp_id"

				+ " LEFT JOIN (SELECT"
				// purchased
				+ " COUNT(DISTINCT preownedstock_id) AS purchased,"

				+ " preownedstock_emp_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id";
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id ";
		}
		if (dr_totalby.equals("preownedteamtrans_team_id") || !team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = preowned_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_preowned_team ON preownedteam_id = preownedteamtrans_team_id";
		}
		if (!model_id.equals("") || !carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		}

		if (!carmanuf_id.equals("")) {
			StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		}
		StrSql += " WHERE 1 = 1"
				+ BranchAccess.replace("branch_id", "preowned_branch_id")
				+ ExeAccess.replace("emp_id", "preowned_emp_id")
				+ StrSearch
				+ StrPreOwnedFilter
				+ StrPreOwnedModelFilter;
		StrSql += " AND SUBSTR(preownedstock_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(preownedstock_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ " GROUP BY preownedstock_emp_id"
				+ " ) AS tblstock ON tblstock.preownedstock_emp_id = emp_id";

		// sales part joins

		// + " LEFT JOIN(SELECT"
		// // quotecount
		// + " COUNT(quote_id) AS quotecount,"
		// + " COUNT(DISTINCT quote_id) AS quotekpicount,"
		// + " quote_emp_id"
		// + " FROM " + compdb(comp_id) + "axela_preowned_stock"
		// + " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
		// + " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_preownedstock_id = preownedstock_id";
		// if (!model_id.equals("") || !carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		// }
		//
		// if (!carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
		// + " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		// }
		//
		// StrSql += " WHERE 1 = 1";
		// // + StrPreOwnedFilter;
		// StrSql += " AND SUBSTR(quote_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
		// + " AND SUBSTR(quote_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
		// + " AND quote_active = '1'";
		//
		// StrSql += " GROUP BY quote_emp_id"
		// + " ) AS tblquote ON tblquote.quote_emp_id = emp_id"

		// + " LEFT JOIN(SELECT"
		// // socount
		// + " COUNT(DISTINCT so_id) AS socount,"
		//
		// // soamount
		// + " COALESCE (sum(so_grandtotal), 0) AS soamount,"
		//
		// // receiptcount
		// + " 0 AS receiptcount,"
		//
		// // receiptamount
		// + " 0 AS receiptamount,"
		//
		// + " so_emp_id"
		// + " FROM " + compdb(comp_id) + "axela_preowned_stock"
		// + " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
		// + " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_preownedstock_id = preownedstock_id";
		// if (!model_id.equals("") || !carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id";
		// }
		//
		// if (!carmanuf_id.equals("")) {
		// StrSql += " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
		// + " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
		// }
		//
		// StrSql += " WHERE 1 = 1";
		// // + StrPreOwnedFilter;
		// StrSql += " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
		// + " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
		// + " AND so_active = '1'";
		//
		// StrSql += " GROUP BY so_emp_id"
		// + " ) AS tblso ON tblso.so_emp_id = emp_id";

		StrSql += " WHERE 1 = 1"
				+ " AND emp_preowned = 1";
		if (!include_inactive_exe.equals("1")) {
			StrSql += " AND emp_active = 1 ";
		}

		if (dr_totalby.equals("emp_branch_id")) {
			StrSql += " AND branch_branchtype_id = 2 ";
		}
		StrSql += StrSearch
				+ StrSearchMain
				+ BranchAccess.replace("branch_id", "emp_branch_id")
				+ ExeAccess;

		StrSql += " GROUP BY " + dr_totalby;

		StrSql += " ORDER BY ";
		if (dr_orderby.equals("0") || dr_orderby.equals("")) {
			StrSql += " emp_name ";
		} else {
			StrSql += dr_orderby + " DESC";
		}

		SOP("Preowned Monitoring Board------" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				SearchURL += "&starttime=" + starttime + "&endtime=" + endtime
						+ "&brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&dr_branch_id=" + branch_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&carmanuf_id=" + carmanuf_id
						+ "&model_id=" + model_id
						+ "&include_inactive_exe=" + include_inactive_exe;

				if (dr_totalby.equals("emp_id")) {
					SearchURLType += "&type=emp";
					idname = "emp_id";
				} else if (dr_totalby.equals("preownedteamtrans_team_id")) {
					SearchURLType += "&type=team";
					idname = "preownedteamtrans_team_id";
				} else if (dr_totalby.equals("emp_branch_id")) {
					SearchURLType += "&type=branch";
					idname = "emp_branch_id";
				} else if (dr_totalby.equals("branch_region_id")) {
					SearchURLType += "&type=region";
					idname = "branch_region_id";
				}

				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				// Str.append("<tr align=center>\n");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");

				if (dr_totalby.equals("emp_id")) {
					Str.append("<th>Pre-Owned Consultant</th>\n");
				}
				if (dr_totalby.equals("emp_branch_id")) {
					Str.append("<th>Branch</th>\n");
				}
				if (dr_totalby.equals("preownedteamtrans_team_id")) {
					Str.append("<th>Team</th>\n");
				}
				if (dr_totalby.equals("branch_region_id")) {
					Str.append("<th>Region</th>\n");
				}

				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Target</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned</th>\n");
				Str.append("<th data-hide=\"phone\">New Car Pre-Owned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Evaluation Target</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Evaluation</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">TAT</th>\n");
				Str.append("<th data-hide=\"phone\">Open Pre-Owned</th>\n");
				Str.append("<th data-hide=\"phone\">Closed Pre-Owned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Hot Pre-Owned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Purchase Target</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Purchased</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Meetings Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Meetings Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">KPI Meetings</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Follow-Up</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Escalation</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned TD Planned</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned TD Completed</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">KPI Pre-Owned TD</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Ratio</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Customer Pre-Owned Feedback</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Quotes</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">KPI Quotes</th>\n");
				// Str.append("<th>Quotes Amount</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Sales Orders</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Sales Ratio</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Sales Amount</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Receipts</th>\n");
				// Str.append("<th data-hide=\"phone, tablet\">Receipt Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				// int rowcount = crs.getRow();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					if (empid != crs.getInt("emp_id")) {
						empcount++;
						empid = crs.getInt("emp_id");
					}
					// total_lead = total_lead + crs.getInt("leadcount");
					// total_enquiry = total_enquiry + crs.getInt("enquirycount");
					total_preowned_count = total_preowned_count + crs.getInt("preownedcount");
					total_sales_enq = total_sales_enq + crs.getInt("saleenqcount");
					total_enquiry_open = total_enquiry_open + crs.getInt("enquiryopen");
					total_enquiry_closed = total_enquiry_closed + crs.getInt("enquiryclosed");
					total_enquiry_hot = total_enquiry_hot + crs.getInt("enquiryhot");
					total_meeting_planned = total_meeting_planned + crs.getInt("meetingplanned");
					total_meeting_completed = total_meeting_completed + crs.getInt("meetingcompleted");
					total_KPImeetings = total_KPImeetings + crs.getInt("kpimeetings");

					total_enquiry_followup = total_enquiry_followup + crs.getInt("enquiryfollowup");
					total_followup_esc = total_followup_esc + crs.getInt("followupesc");

					total_enquiry_testdrivesplanned = total_enquiry_testdrivesplanned + crs.getInt("enquiry_testdrivesplanned");
					total_enquiry_testdrivescompleted = total_enquiry_testdrivescompleted + crs.getInt("enquiry_testdrivescompleted");
					total_enquiry_KPItestdrives = total_enquiry_KPItestdrives + crs.getInt("enquiry_KPItestdrives");
					// total_enquirycustomertestdrivefeedback = total_enquirycustomertestdrivefeedback + crs.getInt("enquirycustomertestdrivefeedback");
					total_eval_count = total_eval_count + crs.getInt("evalcount");
					total_tat_count = (Double) (total_tat_count + crs.getDouble("tatcount"));
					total_purchased_count = total_purchased_count + crs.getInt("purchased");

					total_preowned_target_enquiry += crs.getInt("preownedtarget_enquiry");
					total_preowned_target_eval += crs.getInt("preownedtarget_enquiry_eval");
					total_preowned_target_purchase += crs.getInt("preownedtarget_purchase");

					// total_quote_count = total_quote_count + crs.getInt("quotecount");
					// total_quote_kpi_count = total_quote_kpi_count + crs.getInt("quotekpicount");
					// total_quote_value = total_quote_value + crs.getInt("quoteamount");

					// total_so_count = total_so_count + crs.getInt("socount");
					// total_so_value = total_so_value + (int) crs.getDouble("soamount");

					// total_invoice_count = total_invoice_count + crs.getInt("invoicecount");
					// total_invoice_value = total_invoice_value + crs.getInt("invoiceamount");

					// total_receipt_count = total_receipt_count + crs.getInt("receiptcount");
					// total_receipt_value = total_receipt_value + crs.getInt("receiptamount");
					testdriveratio = 0;
					// salesratio = 0;

					if (!crs.getString("enquiry_KPItestdrives").equals("0") && !crs.getString("preownedcount").equals("0")) {
						testdriveratio = (crs.getDouble("enquiry_KPItestdrives") / crs.getDouble("preownedcount")) * 100;
						testdriveratio = Math.round(testdriveratio * 100.0) / 100.0;
					}
					if (total_preowned_count != 0) {
						// total_enquiry_KPItestdrives);
						// if (total_enquiry != 0) {
						total_testdriveratio = (Double.parseDouble(Integer.toString(total_enquiry_KPItestdrives)) / Double.parseDouble(Integer.toString(total_preowned_count))) * 100;
						total_testdriveratio = Math.round(total_testdriveratio * 100.0) / 100.0;
						// total_testdriveratio);
						// }
					}
					// if (!crs.getString("socount").equals("0") && !crs.getString("preownedcount").equals("0")) {
					// salesratio = (crs.getDouble("socount") / crs.getDouble("preownedcount")) * 100;
					// salesratio = Math.round(salesratio * 100.0) / 100.0;
					// }
					// if (total_preowned_count != 0) {
					// total_salesratio = (Double.parseDouble(Integer.toString(total_so_count)) / Double.parseDouble(Integer.toString(total_preowned_count))) * 100;
					// total_salesratio = Math.round(total_salesratio * 100.0) / 100.0;
					// }

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + empcount + "</td>");

					if (dr_totalby.equals("emp_id")) {
						Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id="
								+ crs.getString("emp_id") + ">" + crs.getString("emp_name") + " ("
								+ crs.getString("emp_ref_no") + ")</a>");
						if (crs.getString("emp_active").equals("0")) {
							Str.append(" <font color=red><b>[Inactive]</b></font>");
						}
						Str.append("</td>");
					}
					if (dr_totalby.equals("emp_branch_id")) {
						Str.append("<td valign=top align=left><a href=../portal/branch-summary.jsp?branch_id="
								+ crs.getString("emp_branch_id") + ">" + crs.getString("branch_name") + "</a></td>");
					}
					if (dr_totalby.equals("preownedteamtrans_team_id")) {
						Str.append("<td valign=top align=left>" + crs.getString("preownedteam_name") + "</a></td>");
					}
					if (dr_totalby.equals("branch_region_id")) {
						Str.append("<td valign=top align=left>" + crs.getString("region_name") + "</a></td>");
					}

					// / preowned Target
					if (dr_totalby.equals("emp_id")) {
						Str.append("<td valign=top align=right><a href=../preowned/target-list.jsp?dr_executives=").append(crs.getString("emp_id"))
								.append("&dr_year=").append(SplitYear(starttime)).append(" target=_blank>");
						Str.append(crs.getInt("preownedtarget_enquiry")).append("</a></td>");
					} else {
						Str.append("<td valign=top align=right>").append(crs.getInt("preownedtarget_enquiry")).append("</td>");
					}

					// / preowned
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=preowned")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("preownedcount")).append("</a></td>");

					// / Opportunity from sales
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=salesenquires")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("saleenqcount") + "</a></td>");

					// / Eval Target
					if (dr_totalby.equals("emp_id")) {
						Str.append("<td valign=top align=right><a href=../preowned/target-list.jsp?dr_executives=").append(crs.getString("emp_id"))
								.append("&dr_year=").append(SplitYear(starttime)).append(" target=_blank>");
						Str.append(crs.getInt("preownedtarget_enquiry_eval")).append("</a></td>");
					} else {
						Str.append("<td valign=top align=right>").append(crs.getInt("preownedtarget_enquiry_eval")).append("</td>");
					}

					// / Eval
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=eval")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("evalcount") + "</a></td>");
					// /tat
					Str.append("<td valign=top align=right>" + deci.format(crs.getDouble("tatcount")) + "</td>");

					// / Open Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=open")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("enquiryopen") + "</a></td>");

					// / Closed Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=closed")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("enquiryclosed") + "</a></td>");

					// / Hot Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=hot")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("enquiryhot") + "</a></td>");

					// / Purchase Target
					if (dr_totalby.equals("emp_id")) {
						Str.append("<td valign=top align=right><a href=../preowned/target-list.jsp?dr_executives=").append(crs.getString("emp_id"))
								.append("&dr_year=").append(SplitYear(starttime)).append(" target=_blank>");
						Str.append(crs.getInt("preownedtarget_purchase")).append("</a></td>");
					} else {
						Str.append("<td valign=top align=right>");
						Str.append(crs.getInt("preownedtarget_purchase")).append("</td>");
					}

					// / Purchased
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=stock")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
									+ crs.getInt("purchased") + "</a></td>");

					// / Meeting Planned
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=meetingsplanned")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("meetingplanned") + "</a></td>");

					// / Meeting Completed
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=meetingscompleted")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("meetingcompleted") + "</a></td>");

					// / KPI Meetings
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=kpimeetings")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("kpimeetings") + "</a></td>");

					// / Follow-up
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=followup")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>")
							.append(crs.getInt("enquiryfollowup") + "</a></td>");

					// / Escalation
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&preowned=escalation")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
									+ crs.getInt("followupesc") + "</a></td>");

					// / Pre Owneds Planned
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&testdrive=planned")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
									+ crs.getInt("enquiry_testdrivesplanned") + "</a></td>");

					// / Pre Owneds Completed
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&testdrive=completed")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
									+ crs.getInt("enquiry_testdrivescompleted") + "</a></td>");

					// / KPI Pre Owneds
					Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&testdrive=kpi")
							.append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
									+ crs.getInt("enquiry_KPItestdrives") + "</a></td>");

					// /Pre Owned Ratio
					Str.append("<td valign=top align=right>" + testdriveratio + "</td>");

					// // / Customer Pre Owned Feedback
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&testdrive=feedback")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + crs.getInt("enquirycustomertestdrivefeedback") + "</a></td>");

					// // / Quotes
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&quote=quote")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + crs.getInt("quotecount") + "</a></td>");
					//
					// // / KPI Quotes
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&quote=kpiquote")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + crs.getInt("quotekpicount") + "</a></td>");
					// Quote Amount
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&quote=kpiquote")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>" +
					// IndFormat(crs.getString("quoteamount")) + "</td>");

					// // Sales Order Count
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&salesorder=salesorder")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + crs.getInt("socount") + "</a></td>");
					//
					// // /sales Ratio
					// Str.append("<td valign=top align=right>" + salesratio + "</td>");
					//
					// // Sales Amount
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&salesorder=salesorder")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + IndFormat(crs.getString("soamount")) + "</a></td>");
					// //
					// // Receipt Countt
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&receipt=receipt")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + crs.getInt("receiptcount") + "</a></td>"); //
					//
					// // Receipt Amount
					// Str.append("<td valign=top align=right>").append(SearchURL).append(SearchURLType).append("&receipt=receipt")
					// .append("&typeid=" + crs.getString(idname)).append(" target=_blank>"
					// + IndFormat(crs.getString("receiptamount")) + "</a></td>");

					Str.append("</tr>\n");
				}
				if (empcount > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
					Str.append("<td valign=top align=right><b>Total:</b></td>");

					// /Total preowned Target
					Str.append("<td valign=top align=right>").append(total_preowned_target_enquiry + "</a></td>");

					// /Total preowned
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=preowned")
							.append(" target=_blank>" + total_preowned_count + "</a></td>");

					// Total from sales_oppr
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=salesenquires")
							.append(" target=_blank><b>" + total_sales_enq + "</b></a></td>");

					// /Total eval Target
					Str.append("<td valign=top align=right>").append(total_preowned_target_eval + "</a></td>");

					// /Total eval
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=eval")
							.append(" target=_blank>" + total_eval_count + "</a></td>");

					// Total Tat
					Str.append("<td valign=top align=right>" + deci.format(total_tat_count) + "</td>");

					// /Total Open Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=open")
							.append(" target=_blank><b>" + total_enquiry_open + "</b></a></td>");

					// /Total Closed Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=closed")
							.append(" target=_blank><b>" + total_enquiry_closed + "</b></a></td>");

					// /Total Hot Opportunity
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=hot")
							.append(" target=_blank><b>" + total_enquiry_hot + "</b></a></td>");

					// /Total Purchase Target
					Str.append("<td valign=top align=right>").append(total_preowned_target_purchase + "</a></td>");

					// /Total Purchased
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=stock")
							.append(" target=_blank>" + total_purchased_count + "</a></td>");

					// /Total Meeting Planned
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=meetingsplanned")
							.append(" target=_blank>" + total_meeting_planned + "</a></td>");

					// /Total Meeting Completed
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=meetingscompleted")
							.append(" target=_blank>" + total_meeting_completed + "</a></td>");

					// /Total KPI Meeting
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=kpimeetings")
							.append(" target=_blank>" + total_KPImeetings + "</a></td>");

					// /Total Follow-up
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=followup")
							.append(" target=_blank><b>" + total_enquiry_followup + "</b></a></td>");

					// /Total Escalation
					Str.append("<td valign=top align=right>").append(SearchURL).append("&preowned=escalation")
							.append(" target=_blank><b>" + total_followup_esc + "</b></a></td>");

					// /Total Pre Owneds Planned
					Str.append("<td valign=top align=right>").append(SearchURL).append("&testdrive=planned")
							.append(" target=_blank>" + total_enquiry_testdrivesplanned + "</a></td>");

					// /Total Pre Owneds Completed
					Str.append("<td valign=top align=right>").append(SearchURL).append("&testdrive=completed")
							.append(" target=_blank>" + total_enquiry_testdrivescompleted + "</a></td>");

					// /Total KPI Pre Owneds
					Str.append("<td valign=top align=right>").append(SearchURL).append("&testdrive=kpi")
							.append(" target=_blank>" + total_enquiry_KPItestdrives + "</a></td>");

					// /Total Pre Owned Ratio
					Str.append("<td valign=top align=right>" + total_testdriveratio + "</td>");

					// // / Customer Pre Owned Feedback
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&testdrive=feedback")
					// .append(" target=_blank>" + total_enquirycustomertestdrivefeedback + "</a></td>");

					// // / Quotes Count
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&quote=quote")
					// .append(" target=_blank><b>" + total_quote_count + "</b></a></td>");
					//
					// // / KPI Quotes
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&quote=kpiquote")
					// .append(" target=_blank><b>" + total_quote_kpi_count + "</b></a></td>");
					//
					// // Quote Amount
					// // Str.append("<td valign=top align=right>").append(SearchURL).append("&quote=kpiquote")
					// // .append(" target=_blank><b>" + IndFormat(total_quote_value + "")
					// // + "</b></a></td>");
					//
					// // Sales Order Count
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&salesorder=salesorder")
					// .append(" target=_blank><b>" + total_so_count + "</b></a></td>");
					//
					// // Sales Ratio
					// Str.append("<td valign=top align=right>" + total_salesratio + "</td>");
					//
					// // sales Order Amount
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&salesorder=salesorder")
					// .append(" target=_blank><b>" + IndFormat(total_so_value + "") + "</b></a></td>");
					//
					// // Receipt Count
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&receipt=receipt")
					// .append(" target=_blank><b>" + total_receipt_count + "</b></a></td>");
					//
					// // Receipt Amount
					// Str.append("<td valign=top align=right>").append(SearchURL).append("&receipt=receipt")
					// .append(" target=_blank><b>" + IndFormat(total_receipt_value + "") + "</b></a></td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<center><font color=red><b>No Pre-Owned Details Found!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateOrderBy(String comp_id) {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=preownedcount").append(StrSelectdrop("preownedcount", dr_orderby)).append(">Pre-Owned</option>\n");
		Str.append("<option value=saleenqcount").append(StrSelectdrop("saleenqcount", dr_orderby)).append(">New Car Pre-Owned</option>\n");
		Str.append("<option value=evalcount").append(StrSelectdrop("evalcount", dr_orderby)).append(">Evaluation</option>\n");
		Str.append("<option value=tatcount").append(StrSelectdrop("tatcount", dr_orderby)).append(">TAT</option>\n");
		Str.append("<option value=enquiryopen").append(StrSelectdrop("enquiryopen", dr_orderby)).append(">Open Pre-Owned</option>\n");
		Str.append("<option value=enquiryclosed").append(StrSelectdrop("enquiryclosed", dr_orderby)).append(">Closed Pre-Owned</option>\n");
		Str.append("<option value=enquiryhot").append(StrSelectdrop("enquiryhot", dr_orderby)).append(">Hot Pre-Owned</option>\n");
		Str.append("<option value=meetingplanned").append(StrSelectdrop("meetingplanned", dr_orderby)).append(">Meetings Planned</option>\n");
		Str.append("<option value=meetingcompleted").append(StrSelectdrop("meetingcompleted", dr_orderby)).append(">Meetings Completed</option>\n");
		Str.append("<option value=kpimeetings").append(StrSelectdrop("kpimeetings", dr_orderby)).append(">KPI Meetings</option>\n");
		Str.append("<option value=enquiryfollowup").append(StrSelectdrop("enquiryfollowup", dr_orderby)).append(">Follow-Up</option>\n");
		Str.append("<option value=followupesc").append(StrSelectdrop("followupesc", dr_orderby)).append(">Escalation</option>\n");
		Str.append("<option value=enquiry_testdrivesplanned").append(StrSelectdrop("enquiry_testdrivesplanned", dr_orderby)).append(">Pre-Owneds TD Planned</option>\n");
		Str.append("<option value=enquiry_testdrivescompleted").append(StrSelectdrop("enquiry_testdrivescompleted", dr_orderby)).append(">Pre-Owneds TD Completed</option>\n");
		Str.append("<option value=enquiry_KPItestdrives").append(StrSelectdrop("enquiry_KPItestdrives", dr_orderby)).append(">KPI Pre-Owneds TD</option>\n");
		// Str.append("<option value=enquirycustomertestdrivefeedback").append(StrSelectdrop("enquirycustomertestdrivefeedback", dr_orderby)).append(">Customer Pre-Owneds Feedback</option>\n");
		Str.append("<option value=purchased").append(StrSelectdrop("purchased", dr_orderby)).append(">Purchased</option>\n");
		// Str.append("<option value=quotecount").append(StrSelectdrop("quotecount", dr_orderby)).append(">Quotes</option>\n");
		// Str.append("<option value=quotekpicount").append(StrSelectdrop("quotekpicount", dr_orderby)).append(">KPI Quotes</option>\n");
		// Str.append("<option value=socount").append(StrSelectdrop("socount", dr_orderby)).append(">Sales Orders</option>\n");
		// Str.append("<option value=soamount").append(StrSelectdrop("soamount", dr_orderby)).append(">Sales Amount</option>\n");
		// Str.append("<option value=receiptcount").append(StrSelectdrop("receiptcount", dr_orderby)).append(">Receipts</option>\n");
		// Str.append("<option value=receiptamount").append(StrSelectdrop("receiptamount", dr_orderby)).append(">Receipt Amount</option>\n");

		return Str.toString();
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=emp_id").append(StrSelectdrop("emp_id", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=preownedteamtrans_team_id").append(StrSelectdrop("preownedteamtrans_team_id", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=emp_branch_id").append(StrSelectdrop("emp_branch_id", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		// Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Brands</option>\n");
		// Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		return Str.toString();
	}

	public String PopulateModels(String carmanuf_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		// String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("carmanuf_ids===" + carmanuf_id);
		if (carmanuf_id.equals("")) {
			carmanuf_id = "0";
		}

		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1 ";
			// if (!carmanuf_id.equals("0")) {
			StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			// }
			// StrSql += BranchAccess;
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_model size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_model style=\"padding:10px\">\n>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("preownedmodel_id"));
					Str.append(ArrSelectdrop(crs.getInt("preownedmodel_id"), model_ids));
					Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}