package axela.insurance;
//Murali 21st jun
//divya
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrLibrary = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ticketchart_data = "";
	public String callchart_data = "";
	public String jcprioritychart_data = "";
	public String servicefollowupescchart_data = "";
	public String psfchart_data = "";
	public String insurancechart_data = "";
	public int TotalRecords = 0;

	// public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	// public String[] brand_ids, region_ids, branch_ids;
	// public String brand_id = "", region_id = "";

	// days
	public String insuenq = "0";
	public String fieldapptcount = "0";
	public String policycount = "0";
	public String policypremiumamtcount = "0";
	// months
	public String insuenqmonth = "0";
	public String fieldapptcountmonth = "0";
	public String policycountmonth = "0";
	public String policypremiumamtmonth = "0";
	// years
	public String insuenqquarter = "0";
	public String fieldapptcountquarter = "0";
	public String policycountquarter = "0";
	public String policypremiumamtquarter = "0";

	// days
	public String insuenq_old = "0";
	public String fieldapptcount_old = "0";
	public String policycount_old = "0";
	public String policypremiumamtcount_old = "0";
	// months
	public String insuenqmonth_old = "0";
	public String fieldapptcountmonth_old = "0";
	public String policycountmonth_old = "0";
	public String policypremiumamtmonth_old = "0";
	// years
	public String insuenqquarter_old = "0";
	public String fieldapptcountquarter_old = "0";
	public String policycountquarter_old = "0";
	public String policypremiumamtquarter_old = "0";

	public String datetype = "", StrHTML = "";
	public String curr_date = ToShortDate(kknow());
	public String back_date = "";
	public String curr_month = "", back_month = "";
	public String startquarter = "", endquarter = "";
	public String back_startquarter = "", back_endquarter = "";
	String period = "";

	// refresh All AJAX
	public String refreshAll = "", cards = "";
	public String insurfollowupescstatus = "";

	public String filter = "";
	public String opt = "";
	public MIS_Check misCheck = new MIS_Check();

	// public MIS_Check misCheck = new MIS_Check();
	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String brand_id = "", region_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_enquiry_access", request, response);

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				filter = PadQuotes(request.getParameter("filter"));

				if (smart == null) {
					smart = "";
				}

				refreshAll = PadQuotes(request.getParameter("refreshAll"));
				cards = PadQuotes(request.getParameter("cards"));

				// psfstatus = PadQuotes(request.getParameter("psfstatus"));
				insurfollowupescstatus = PadQuotes(request.getParameter("insurfollowupescstatus"));

				filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
				filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
				filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));

				if (filter.equals("yes")) {
					opt = PadQuotes(request.getParameter("opt"));
					populateconfigdetails();
					CheckRedirect(opt, request, response);
				}

				// ServiceVehDetails(comp_id);
				if (msg.equals("") && refreshAll.equals("")) {

					if (smart.equals("yes")) {
					} else {
						SetSession("ticketstrsql", StrSearch, request);
					}
				} else if (msg.equals("")) {
					if (refreshAll.equals("yes") && insurfollowupescstatus.equals("yes")) {
						StrHTML = InsuranceFollowupEscStatus();
					}
					if (refreshAll.equals("yes") && cards.equals("yes")) {
						populateconfigdetails();
						StrHTML = ServiceVehDetails(comp_id);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String InsuranceFollowupEscStatus() {
		try {
			int totalinsurcount = 0;
			StrSql = " SELECT gr.group_id AS group_id, "
					+ " COALESCE(triggercount,0) AS triggercount"
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
					+ " LEFT JOIN (SELECT COUNT(insurenquiryfollowup_id) AS triggercount, insurenquiryfollowup_trigger"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id";
			}
			StrSql += " WHERE insurenquiryfollowup_disp1 = '' "
					+ " AND insurenquiryfollowup_followup_time <= '" + ToLongDate(kknow()) + "'";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += ExeAccess.replace("emp_id", "insurenquiry_emp_id")
					+ " GROUP BY insurenquiryfollowup_trigger) AS tr ON tr.insurenquiryfollowup_trigger = gr.group_id"
					+ " WHERE 1=1 "
					+ " GROUP BY group_id "
					+ " ORDER BY group_id desc";
			// SOP("StrSql=====InsuranceFollowupEscStatus====" + StrSql);
			TotalRecords = 5;
			int count = 0, level = 5;
			String color = "";
			CachedRowSet crs = processQuery(StrSql, 0);
			insurancechart_data = "[";
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					if (level == 5) {
						color = "#FF0033";
					} else if (level == 4) {
						color = "#FF8030";
					} else if (level == 3) {
						color = "#7ba5de";
					} else if (level == 2) {
						color = "#F8FF01";
					} else if (level == 1) {
						color = "#c3a7e2";
					}
					count++;
					totalinsurcount = totalinsurcount + crs.getInt("triggercount");
					insurancechart_data = insurancechart_data + "{\"level\": \"Level " + level + "\", \"value\":\"" + crs.getString("triggercount") + "\", \"color\":\"" + color
							+ "\", \"url\":\"report-insurance-esc-status.jsp\"}";
					level--;
					if (count < TotalRecords) {
						insurancechart_data = insurancechart_data + ",";
					}
				}
				insurancechart_data = insurancechart_data + "]";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return insurancechart_data.toString();
	}

	public String InsuranceFollowup() {
		try {
			int totalinsurcount = 0;
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COUNT(insurenquiryfollowup_id) AS insurcount "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id"
					+ " WHERE 1 = 1"
					+ ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql=InsuranceFollowup======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"  \">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				while (crs.next()) {
					totalinsurcount = totalinsurcount + crs.getInt("insurcount");
					Str.append("<tr align=center>\n");
					Str.append("<td align=left><a href=../insurance/report-insurance-followup.jsp>").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td align=right width=10%>").append(crs.getString("insurcount")).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(totalinsurcount).append("</b></td>\n");
				Str.append("</tr>");
			} else {
				Str.append("<tr><td colspan=2 align=center><br><br>No Open Insurance Follow-up!<br><br><br><br><br><br></td></tr>");
			}
			Str.append("</table>\n");
			// Str.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 13 "
				+ " AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		// SOP("StrSql===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"  \">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ServiceVehDetails(String comp_id) {

		String new_value = "", old_value = "";
		try {
			CachedRowSet crs = null;

			StrSql = "SELECT"
					+ " COALESCE (tblinsurenquiry.insurenqcount, 0) AS insurenq,"
					+ " COALESCE (tblinsurenquiry.insurenqcountmonth, 0) AS insuenqmonth,"
					+ " COALESCE (tblinsurenquiry.insurenqcountquarter, 0) AS insuenqquarter,"
					+ " COALESCE (tblinsurenquiry.insurenqcountold, 0) AS insurenqold,"
					+ " COALESCE (tblinsurenquiry.insurenqcountmonthold, 0) AS insuenqmonthold,"
					+ " COALESCE (tblinsurenquiry.insurenqcountquarterold, 0) AS insuenqquarterold,"

					+ " COALESCE (tblpolicy.policycount, 0) AS policycount,"
					+ " COALESCE (tblpolicy.policycountmonth, 0) AS policycountmonth,"
					+ " COALESCE (tblpolicy.policycountquarter, 0) AS policycountquarter,"
					+ " COALESCE (tblpolicy.policycountold, 0) AS policycountold,"
					+ " COALESCE (tblpolicy.policycountmonthold, 0) AS policycountmonthold,"
					+ " COALESCE (tblpolicy.policycountquarterold, 0) AS policycountquarterold,"

					+ " COALESCE (tblfieldappt.fieldapptcount, 0) AS fieldapptcount,"
					+ " COALESCE (tblfieldappt.fieldapptcountmonth, 0) AS fieldapptcountmonth,"
					+ " COALESCE (tblfieldappt.fieldapptcountquarter, 0) AS fieldapptcountquarter,"
					+ " COALESCE (tblfieldappt.fieldapptcountold, 0) AS fieldapptcountold,"
					+ " COALESCE (tblfieldappt.fieldapptcountmonthold, 0) AS fieldapptcountmonthold,"
					+ " COALESCE (tblfieldappt.fieldapptcountquarterold, 0) AS fieldapptcountquarterold,"

					+ " COALESCE (tblpolicy.policypremiumamtcount, 0) AS policypremiumamtcount,"
					+ " COALESCE (tblpolicy.policypremiumamtmonth, 0) AS policypremiumamtmonth,"
					+ " COALESCE (tblpolicy.policypremiumamtquarter, 0) AS policypremiumamtquarter,"
					+ " COALESCE (tblpolicy.policypremiumamtold, 0) AS policypremiumamtold,"
					+ " COALESCE (tblpolicy.policypremiumamtmonthold, 0) AS policypremiumamtmonthold,"
					+ " COALESCE (tblpolicy.policypremiumamtquarterold, 0) AS policypremiumamtquarterold"
					+ " FROM ";

			// insurance_enq
			StrSql += "	("
					+ " SELECT"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 8) = " + curr_date + ", 1, 0)) AS insurenqcount,"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 6) = " + curr_month + ", 1, 0)) AS insurenqcountmonth,"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(insurenquiry_date, 1, 6) <= " + endquarter + ", 1, 0)) AS insurenqcountquarter,"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 8) = " + back_date + ", 1, 0)) AS insurenqcountold,"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 6) = " + back_month + ", 1, 0)) AS insurenqcountmonthold,"
					+ " SUM(IF (SUBSTR(insurenquiry_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(insurenquiry_date, 1, 6) <= " + back_endquarter + ", 1, 0)) AS insurenqcountquarterold"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id";
			}
			StrSql += " WHERE 1 = 1" +
					ExeAccess.replace("emp_id", "insurenquiry_emp_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS tblinsurenquiry,";

			// Policy
			StrSql += "("
					+ "SELECT"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 8) = " + curr_date + ", 1, 0)) AS policycount,"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 6) = " + curr_month + ", 1, 0)) AS policycountmonth,"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 6) >= " + startquarter
					+ "	AND SUBSTR(insurpolicy_date, 1, 6) <= " + endquarter + ", 1, 0)) AS policycountquarter,"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 8) = " + back_date + ", 1, 0)) AS policycountold,"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 6) = " + back_month + ", 1, 0)) AS policycountmonthold,"
					+ " SUM(IF (SUBSTR(insurpolicy_date, 1, 6) >= " + back_startquarter
					+ "	AND SUBSTR(insurpolicy_date, 1, 6) <= " + back_endquarter + ", 1, 0)) AS policycountquarterold,"

					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 8) = " + curr_date + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtcount,"
					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 6) = " + curr_month + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtmonth,"
					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(insurpolicy_date, 1, 6) <= " + endquarter + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtquarter,"
					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 8) = " + back_date + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtold,"
					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 6) = " + back_month + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtmonthold,"
					+ "	SUM(IF (SUBSTR(insurpolicy_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(insurpolicy_date, 1, 6) <= " + back_endquarter + ", insurpolicy_premium_amt, 0)	) AS policypremiumamtquarterold"

					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "insurpolicy_emp_id")
					+ BranchAccess.replace("branch_id", "insurpolicy_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS tblpolicy,";

			StrSql += "("
					+ " SELECT"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 8) = " + curr_date + ", 1, 0)	) AS fieldapptcount,"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 6) = " + curr_month + ", 1, 0)	) AS fieldapptcountmonth,"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 6) >= " + startquarter
					+ " AND SUBSTR(fieldappt_appttime, 1, 6) <= " + endquarter + ", 1, 0)	) AS fieldapptcountquarter,"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 8) = " + back_date + ", 1, 0)	) AS fieldapptcountold,"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 6) = " + back_month + ", 1, 0)	) AS fieldapptcountmonthold,"
					+ " SUM(IF (SUBSTR(fieldappt_appttime, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(fieldappt_appttime, 1, 6) <= " + back_endquarter + ", 1, 0)	) AS fieldapptcountquarterold"
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = fieldappt_emp_id"
					+ " WHERE 1 = 1"
					+ " ) AS tblfieldappt";

			// SOP("StrSql====ServiceVehDetails===" + StrSql);

			crs = processQuery(StrSql);
			while (crs.next()) {

				insuenq = crs.getString("insurenq");
				insuenqmonth = crs.getString("insuenqmonth");
				insuenqquarter = crs.getString("insuenqquarter");

				fieldapptcount = crs.getString("fieldapptcount");
				fieldapptcountmonth = crs.getString("fieldapptcountmonth");
				fieldapptcountquarter = crs.getString("fieldapptcountquarter");

				policycount = crs.getString("policycount");
				policycountmonth = crs.getString("policycountmonth");
				policycountquarter = crs.getString("policycountquarter");

				policypremiumamtcount = crs.getString("policypremiumamtcount");
				policypremiumamtmonth = crs.getString("policypremiumamtmonth");
				policypremiumamtquarter = crs.getString("policypremiumamtquarter");

				new_value = insuenq + "," + insuenqmonth + "," + insuenqquarter + ","
						+ fieldapptcount + "," + fieldapptcountmonth + "," + fieldapptcountquarter + ","
						+ policycount + "," + policycountmonth + "," + policycountquarter + ","
						+ policypremiumamtcount + "," + policypremiumamtmonth + "," + policypremiumamtquarter;

				// ==========================================Old Values===========================================

				insuenq_old = crs.getString("insurenqold");
				insuenqmonth_old = crs.getString("insuenqmonthold");
				insuenqquarter_old = crs.getString("insuenqquarterold");

				fieldapptcount_old = crs.getString("fieldapptcountold");
				fieldapptcountmonth_old = crs.getString("fieldapptcountmonthold");
				fieldapptcountquarter_old = crs.getString("fieldapptcountquarterold");

				policycount_old = crs.getString("policycountold");
				policycountmonth_old = crs.getString("policycountmonthold");
				policycountquarter_old = crs.getString("policycountquarterold");

				policypremiumamtcount_old = crs.getString("policypremiumamtold");
				policypremiumamtmonth_old = crs.getString("policypremiumamtmonthold");
				policypremiumamtquarter_old = crs.getString("policypremiumamtquarterold");

				old_value = insuenq_old + "," + insuenqmonth_old + "," + insuenqquarter_old + ","
						+ fieldapptcount_old + "," + fieldapptcountmonth_old + "," + fieldapptcountquarter_old + ","
						+ policycount_old + "," + policycountmonth_old + "," + policycountquarter_old + ","
						+ policypremiumamtcount_old + "," + policypremiumamtmonth_old + "," + policypremiumamtquarter_old;

			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return new_value + ":" + old_value;
	}
	public void populateconfigdetails() {

		curr_date = curr_date.substring(0, 8);
		curr_month = ToShortDate(kknow()).substring(0, 6);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		back_month = ToLongDate(cal.getTime()).substring(0, 6);
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		back_date = ToLongDate(cal.getTime()).substring(0, 8);

		if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 1 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 3) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "01";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "03";

			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "10";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "12";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 4 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 6) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "04";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "06";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "01";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "03";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 7 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 9) {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "07";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "09";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "04";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "06";
		} else {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "10";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "12";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "07";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "09";
		}
	}

	protected void CheckRedirect(String opt, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		period = PadQuotes(request.getParameter("period"));
		filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
		filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
		filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));

		if (!filter_brand_id.equals("")) {
			StrSearch = " AND branch_brand_id IN (" + filter_brand_id + ")";
		}
		if (!filter_region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + filter_region_id + ")";
		}
		if (!filter_branch_id.equals("")) {
			StrSearch += " AND branch_id IN (" + filter_branch_id + ")";
		}

		// insurenquiry
		if (opt.equals("insurenquiry") && period.equals("today")) {
			StrSearch += " AND SUBSTR(insurenquiry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("insurenquiry") && period.equals("month")) {
			StrSearch += " AND SUBSTR(insurenquiry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("insurenquiry") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(insurenquiry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(insurenquiry_date, 1, 6) <= " + endquarter;
		}

		// Field Appt
		if (opt.equals("fieldappt") && period.equals("today")) {
			StrSearch += " AND SUBSTR(fieldappt_appttime, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("fieldappt") && period.equals("month")) {
			StrSearch += " AND SUBSTR(fieldappt_appttime, 1, 6) = " + curr_month;
		} else if (opt.equals("fieldappt") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(fieldappt_appttime, 1, 6) >= " + startquarter
					+ " AND SUBSTR(fieldappt_appttime, 1, 6) <= " + endquarter;
		}

		// SOP("opt-----" + opt);
		// Policy
		if ((opt.equals("policy") || opt.equals("premiumamt")) && period.equals("today")) {
			StrSearch += " AND SUBSTR(insurpolicy_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if ((opt.equals("policy") || opt.equals("premiumamt")) && period.equals("month")) {
			StrSearch += " AND SUBSTR(insurpolicy_date, 1, 6) = " + curr_month;
		} else if ((opt.equals("policy") || opt.equals("premiumamt")) && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(insurpolicy_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(insurpolicy_date, 1, 6) <= " + endquarter;
		}

		if (opt.equals("insurenquiry")) {
			SetSession("insurstrsql", StrSearch, request);
			response.sendRedirect("../insurance/insurance-enquiry-list.jsp?smart=yes");
			return;
		}

		if (opt.equals("fieldappt")) {
			SetSession("fieldaptstrsql", StrSearch, request);
			response.sendRedirect("../insurance/fieldappt-list.jsp?smart=yes");
			return;
		}

		if (opt.equals("policy") || opt.equals("premiumamt")) {
			// SOP("Coming-----" + opt);
			// SOP("StrSearch-----" + StrSearch);
			SetSession("insurancestrsql", StrSearch, request);
			response.sendRedirect("../insurance/insurance-list.jsp?smart=yes");
			return;
		}

	}

}
