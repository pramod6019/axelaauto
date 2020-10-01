package axela.insurance;

//Shivaprasad 12/7/2015   
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Tracker extends Connect {

	public String StrSql = "";
	public String endtime = "", end_time = "";

	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	// public String[] team_ids, exe_ids, model_ids;
	public String BranchAccess = "", dr_branch_id = "";
	// public String[] soe_ids;
	public String StrHTML = "";
	public String go = "";
	public String ExeAccess = "";
	public String dr_year = "";
	public String dr_month = "";
	public String[] x;
	public String[] row;
	public String[] column;
	public String StrSearch = "";
	public static int year;
	public static int month;
	public String[] exe_ids;
	public String exe_id = "";
	public String comp_id = "0";
	// public MIS_Check1 mischeck = new MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				dr_month = PadQuotes(request.getParameter("dr_month"));
				if (dr_month.equals("")) {
					dr_month = ToShortDate(kknow()).substring(4, 6);
				}
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					StrSearch = StrSearch + " and SUBSTR(insurfollowup_followup_time, 1, 6) = " + dr_year + dr_month + "";
					if (!exe_id.equals("0")) {
						StrSearch += " AND emp_id in (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						StrHTML = InsuranceTracker();

					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		year = (Integer.parseInt(dr_year));
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}
		month = (Integer.parseInt(dr_month));
		if (!dr_year.equals("")) {
			x = new String[32];
		}
		if (!dr_year.equals("") && !dr_month.equals("")) {
			int temp = DaysInMonth(year, month);
			for (int i = 1; i <= temp; i++) {
				if ((i + "").length() == 1) {
					x[i] = dr_year + dr_month + "0" + i + "000000";
				} else {
					x[i] = dr_year + dr_month + i + "000000";
				}
			}
		}
		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));

	}

	protected void CheckForm() {
		msg = "";
		if (dr_year.equals("")) {
			msg = msg + "<br>SELECT Year!<br>";
		}
		if (dr_month.equals("")) {
			msg += "<br>SELECT Month!";
		}
	}

	public String PopulateYears(String comp_id) {

		String year = ToShortDate(kknow()).substring(0, 4);
		StringBuilder years = new StringBuilder();
		for (int i = Integer.parseInt(year); i > Integer.parseInt(year) - 3; i--) {
			years.append("<option value = " + doublenum(i) + ""
					+ StrSelectdrop(doublenum(i), dr_year) + ">" + i
					+ "</option>\n");
		}
		return years.toString();
	}

	public String PopulateMonths(String comp_id, String dr_month) {
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

	public String InsuranceTracker() {
		int insur_row_total = 0;
		int grandtotal = 0;
		int count = 0;
		int rowcount = 0;
		StringBuilder Str = new StringBuilder();
		int monthdays = DaysInMonth(year, month);
		String date = "";
		String StrSqlprior = "";
		String StrSqlN60 = "";
		String priorityinsurfollowup_name = "";
		String psffeedbacktype_name = "";
		String StrSqlContactable = "";
		String followuptype_name = "";
		String StrSqlfollowuptype = "";

		// Followup Due
		StrSql = " SELECT ";
		for (int i = 1; i <= monthdays; i++) {
			if (("" + i + "").length() == 1) {
				date = "0" + i;
				StrSql = StrSql + " COALESCE((SUM(IF(SUBSTR(insurfollowup_followup_time, 1, 8) = " + dr_year + doublenum(month) + date + ",1,0))), 0) AS followupdue_" + i + ",";
			} else {
				StrSql = StrSql + " COALESCE((SUM(IF(SUBSTR(insurfollowup_followup_time, 1, 8) = " + dr_year + doublenum(month) + i + ",1,0))), 0) AS followupdue_" + i + ",";
			}
		}
		StrSql += " 'test' FROM " + compdb(comp_id) + "axela_insurance_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurfollowup_insurenquiry_id";
		if (!exe_id.equals("0")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id  = insurenquiry_emp_id";
		}
		StrSql += " WHERE 1=1"
				+ StrSearch;

		// (Veh Sale Date - 60)
		StrSqlN60 = " SELECT";
		for (int i = 1; i <= monthdays; i++) {
			if (("" + i + "").length() == 1) {
				date = "0" + i;
				StrSqlN60 = StrSqlN60 + " COALESCE((SUM(IF(CONCAT(" + dr_year + ",DATE_FORMAT(DATE_SUB(veh_sale_date, INTERVAL 60 DAY),'%m%d')) = " + dr_year + doublenum(month) + date
						+ ",1,0))), 0) AS veh_sale_date_" + i + ",";
			} else {
				StrSqlN60 = StrSqlN60 + " COALESCE((SUM(IF(CONCAT(" + dr_year + ",DATE_FORMAT(DATE_SUB(veh_sale_date, INTERVAL 60 DAY),'%m%d')) = " + dr_year + doublenum(month) + i
						+ ",1,0))), 0) AS veh_sale_date_" + i + ",";
			}
		}
		StrSqlN60 += " 'test'"
				+ " FROM " + compdb(comp_id) + "axela_service_veh";
		if (!exe_id.equals("0")) {
			StrSqlN60 += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id  = veh_insuremp_id";
		}
		StrSqlN60 += " WHERE 1=1";

		// Priority Insurfollowup
		StrSqlprior = " SELECT priorityinsurfollowup_name,";
		for (int i = 1; i <= monthdays; i++) {
			if (("" + i + "").length() == 1) {
				date = "0" + i;
				StrSqlprior = StrSqlprior + " COALESCE((SUM(IF(SUBSTR(insurfollowup_followup_time, 1, 8) = " + dr_year + doublenum(month) + date + ",1,0))), 0) AS vehpriority_" + i + ",";
			} else {
				StrSqlprior = StrSqlprior + " COALESCE((SUM(IF(SUBSTR(insurfollowup_followup_time, 1, 8) = " + dr_year + doublenum(month) + i + ",1,0))), 0) AS vehpriority_" + i + ",";
			}
		}
		StrSqlprior += " 'test'"
				+ " FROM " + compdb(comp_id) + "axela_insurance_followup";
		if (!exe_id.equals("0")) {
			StrSqlprior += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id  = insurenquiry_emp_id";
		}
		StrSqlprior += " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurfollowup_insurenquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_followup_priority ON priorityinsurfollowup_id = insurfollowup_priorityinsurfollowup_id"
				+ " WHERE 1=1"
				+ StrSearch
				+ " GROUP BY priorityinsurfollowup_id";

		// Contactable
		StrSqlContactable = " SELECT psffeedbacktype_name,";
		for (int i = 1; i <= monthdays; i++) {
			if (("" + i + "").length() == 1) {
				date = "0" + i;
				StrSqlContactable = StrSqlContactable + " COALESCE((SUM(IF(SUBSTR(insurfollowup_entry_time, 1, 8) = " + dr_year + doublenum(month) + date + ",1,0))), 0) AS psffeedbacktype_" + i + ",";
			} else {
				StrSqlContactable = StrSqlContactable + " COALESCE((SUM(IF(SUBSTR(insurfollowup_entry_time, 1, 8) = " + dr_year + doublenum(month) + i + ",1,0))), 0) AS psffeedbacktype_" + i + ",";
			}
		}
		StrSqlContactable += " 'test' FROM " + compdb(comp_id) + "axela_insurance_followup";
		if (!exe_id.equals("0")) {
			StrSqlContactable += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id  = insurenquiry_emp_id";
		}
		StrSqlContactable += " INNER JOIN " + maindb() + "service_psf_feedbacktype on psffeedbacktype_id  = insurfollowup_psffeedbacktype_id"
				+ " WHERE 1=1"
				+ " and SUBSTR(insurfollowup_entry_time, 1, 6) = " + dr_year + dr_month + ""
				+ " GROUP BY psffeedbacktype_id";

		// Followuptype
		StrSqlfollowuptype = " SELECT followuptype_name,";
		for (int i = 1; i <= monthdays; i++) {
			if (("" + i + "").length() == 1) {
				date = "0" + i;
				StrSqlfollowuptype = StrSqlfollowuptype + " COALESCE((SUM(IF(SUBSTR(insurfollowup_entry_time, 1, 8) = " + dr_year + doublenum(month) + date + ",1,0))), 0) AS followuptype_" + i + ",";
			} else {
				StrSqlfollowuptype = StrSqlfollowuptype + " COALESCE((SUM(IF(SUBSTR(insurfollowup_entry_time, 1, 8) = " + dr_year + doublenum(month) + i + ",1,0))), 0) AS followuptype_" + i + ",";
			}
		}
		StrSqlfollowuptype += " 'test' FROM " + compdb(comp_id) + "axela_insurance_followup";
		if (!exe_id.equals("0")) {
			StrSqlfollowuptype += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id  = insurenquiry_emp_id";
		}
		StrSqlfollowuptype += " INNER JOIN " + compdb(comp_id) + "axela_insurance_followup_type on followuptype_id = insurfollowup_followuptype_id"
				+ " WHERE 1=1"
				+ " and SUBSTR(insurfollowup_entry_time, 1, 6) = " + dr_year + dr_month + ""
				+ " GROUP BY followuptype_id";

		// SOP("StrSql===" + StrSql);

		try {

			CachedRowSet crs = null;
			// SOP("StrSql==follow up due===" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);

			CachedRowSet crsN60 = null;
			// SOP("rsN60===" + StrSqlBreaker(StrSqlN60));
			crsN60 = processQuery(StrSqlN60, 0);

			CachedRowSet crsPriority = null;
			// SOP("StrSqlprior===" + StrSqlBreaker(StrSqlprior));
			crsPriority = processQuery(StrSqlprior, 0);

			CachedRowSet crsContactable = null;
			// SOP("Contactable===" + StrSqlBreaker(StrSqlContactable));
			crsContactable = processQuery(StrSqlContactable, 0);

			CachedRowSet crsfollowuptype = null;
			// SOP("rsfollowuptype===" + StrSqlBreaker(StrSqlfollowuptype));
			crsfollowuptype = processQuery(StrSqlfollowuptype, 0);

			// if (crs.isBeforeFirst()) {
			Str.append("<div class=\"table-bordered\">\n");
			Str.append("<table class=\"table  table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead>");
			Str.append("<th colspan=\"" + (monthdays + 2) + "\" align=\"center\" valign=\"top\">Daily Report</th>");
			Str.append("<tr align=center>\n");
			Str.append("<th data-toggle=\"true\">Date</th>");
			int days = DaysInMonth(year, month);
			// SOP("days===" + days);
			for (int i = 1; i <= days; i++) {
				count++;
				Str.append("<th data-hide=\"phone\">" + i + "</th>");
			}
			Str.append("<th data-hide=\"phone\">Total</th>");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			HashMap<String, Integer> day_column_total = new HashMap<>();
			for (int i = 1; i <= days; i++) {
				day_column_total.put(i + "", 0);
			}

			while (crs.next()) {
				Str.append("<tr>");
				Str.append("<td>Follow-ups Due</td>");
				for (int i = 1; i <= days; i++) {
					insur_row_total += crs.getInt("followupdue_" + i);
					// SOP("insur_row_total===" + insur_row_total);
					Str.append("<td align=right>" + crs.getString("followupdue_" + i) + "</td>");
					day_column_total.put(i + "", day_column_total.get(i + "") + crs.getInt("followupdue_" + i));
				}
				grandtotal += insur_row_total;
				Str.append("<td align=right><b>" + insur_row_total + "</b></td>");
				Str.append("</tr>");
				insur_row_total = 0;
			}

			while (crsN60.next()) {
				Str.append("<tr>");
				Str.append("<td>N-60</td>");
				for (int i = 1; i <= days; i++) {
					insur_row_total += crsN60.getInt("veh_sale_date_" + i);
					Str.append("<td align=right>" + crsN60.getString("veh_sale_date_" + i) + "</td>");
					day_column_total.put(i + "", day_column_total.get(i + "") + crsN60.getInt("veh_sale_date_" + i));
				}
				grandtotal += insur_row_total;
				Str.append("<td align=right><b>" + insur_row_total + "</b></td>");
				Str.append("</tr>");
				insur_row_total = 0;
			}

			while (crsPriority.next()) {
				priorityinsurfollowup_name = "";
				Str.append("<tr>");
				if (!crsPriority.getString("priorityinsurfollowup_name").equals(priorityinsurfollowup_name)) {
					Str.append("<td>" + crsPriority.getString("priorityinsurfollowup_name") + "</td>");
					for (int i = 1; i <= days; i++) {
						insur_row_total += crsPriority.getInt("vehpriority_" + i);
						Str.append("<td align=right>" + crsPriority.getString("vehpriority_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crsPriority.getInt("vehpriority_" + i));
					}
					priorityinsurfollowup_name = crsPriority.getString("priorityinsurfollowup_name");
				}
				grandtotal += insur_row_total;
				Str.append("<td align=right><b>" + insur_row_total + "</b></td>");
				Str.append("</tr>");
				insur_row_total = 0;
			}

			while (crsContactable.next()) {
				psffeedbacktype_name = "";
				Str.append("<tr>");
				if (!crsContactable.getString("psffeedbacktype_name").equals(psffeedbacktype_name)) {
					Str.append("<td>" + crsContactable.getString("psffeedbacktype_name") + "</td>");
					for (int i = 1; i <= days; i++) {
						insur_row_total += crsContactable.getInt("psffeedbacktype_" + i);
						Str.append("<td align=right>" + crsContactable.getString("psffeedbacktype_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crsContactable.getInt("psffeedbacktype_" + i));
					}
					psffeedbacktype_name = crsContactable.getString("psffeedbacktype_name");
				}
				grandtotal += insur_row_total;
				Str.append("<td align=right><b>" + insur_row_total + "</b></td>");
				Str.append("</tr>");
				insur_row_total = 0;
			}

			while (crsfollowuptype.next()) {
				followuptype_name = "";
				Str.append("<tr>");
				if (!crsfollowuptype.getString("followuptype_name").equals(followuptype_name)) {
					Str.append("<td>" + crsfollowuptype.getString("followuptype_name") + "</td>");
					for (int i = 1; i <= days; i++) {
						insur_row_total += crsfollowuptype.getInt("followuptype_" + i);
						Str.append("<td align=right>" + crsfollowuptype.getString("followuptype_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crsfollowuptype.getInt("followuptype_" + i));
					}
					followuptype_name = crsfollowuptype.getString("followuptype_name");
				}
				grandtotal += insur_row_total;
				Str.append("<td align=right><b>" + insur_row_total + "</b></td>");
				Str.append("</tr>");
				insur_row_total = 0;
			}

			// Str.append("<tr>");
			// Str.append("<td align=right><b>Total: </b></td>");
			// for (int i = 1; i <= days; i++) {
			// Str.append("<td align=right><b>" + day_column_total.get(i + "") +
			// "</b></td>");
			// }
			// Str.append("<td align=right><b>" + grandtotal + "</b></td>");
			// Str.append("</tr>");
			// SOP("grandtotal=====" + grandtotal);
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			Str.append("</br></br></br></br></br>");

			// }
			// else {
			// Str.append("</br></br></br></br></br><font color=red><b>No Records Found!</b></font>");
			// }
			crs.close();
			crsN60.close();
			crsPriority.close();
			crsContactable.close();
			crsfollowuptype.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}
	// /////////INSURANCE EXECUTIVE////////
	public String PopulateListInsuranceExecutives(String comp_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"form-control\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
