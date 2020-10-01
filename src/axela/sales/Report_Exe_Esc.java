package axela.sales;

//Uma K 03/09/2016
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Exe_Esc extends Connect {

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
	public String enquiry_count = "";
	public String StrSearch = "";
	public String emp_all_exe = "";
	public static int year;
	public static int month;
	public static int maxdays;
	public MIS_Check1 mischeck = new MIS_Check1();

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
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {
					CheckForm();
					StrSearch = BranchAccess + ExeAccess;

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
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
						StrSearch = StrSearch
								+ " AND emp_id IN (SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN ("
								+ team_id + "))";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_model_id IN ("
								+ model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
						dr_month = PadQuotes(request.getParameter("dr_month"));
						dr_year = PadQuotes(request.getParameter("dr_year"));
						StrHTML = ExeFollowupEscalationStatus();
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
		if (dr_year.equals("")) {
			dr_year = ToShortDate(kknow()).substring(0, 4);
		}
		year = (Integer.parseInt(dr_year));
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
	}

	protected void CheckForm() {
		msg = "";
		if (brand_id.equals("")) {
			msg = msg + "<br>Select Brand!<br>";
		}
	}

	public String ExeFollowupEscalationStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			String StrSql = "";
			int count = 0, followuptotal = 0;
			StrSql = "  SELECT emp_id, emp_name, emp_ref_no,"
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 0 THEN 1 END),0) AS level0, "
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 1 THEN 1 end),0) AS level1,"
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 2 THEN 1 end),0) AS level2,"
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 3 THEN 1 end),0) AS level3,"
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 4 THEN 1 end),0) AS level4,"
					+ " COALESCE(SUM(CASE WHEN followup_trigger = 5 THEN 1 end),0) AS level5"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = followup_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = followup_emp_id"
					+ " WHERE SUBSTR(followup_followup_time, 1, 6) = SUBSTR(" + year + dr_month + ", 1,6)"
					+ StrSearch + BranchAccess + ExeAccess.replace("emp_id", "enquiry_emp_id") + ""
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered table-hover\">\n");
				Str.append("<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
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
					count++;
					followuptotal = crs.getInt("level0") + crs.getInt("level1") + crs.getInt("level2")
							+ crs.getInt("level3") + crs.getInt("level4") + crs.getInt("level5");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append(" (");
					Str.append(crs.getString("emp_ref_no")).append(")</a></td>");
					Str.append("<td align=right>").append(crs.getInt("level0")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level1")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level2")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level3")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level4")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getInt("level5")).append("</td>\n");
					Str.append("<td align=right>").append(followuptotal).append("</td>\n");
					Str.append("</tr>");
				}
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
}
