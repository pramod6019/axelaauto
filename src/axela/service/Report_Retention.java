package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Retention extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	DecimalFormat deci = new DecimalFormat("#");
	public String go = "";
	public String ExeAccess = "";
	public String dr_month = "";
	public String dr_year = "";
	public String[] x;
	public static int year;
	public static int month;
	public static int maxdays;
	public String[] brand_ids, region_ids, branch_ids;
	public String brand_id = "", region_id = "";
	public String SearchURL = "report-retention-search.jsp";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_opportunity_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							StrSearch += " AND branch_id IN (" + branch_id + ")";
						}
						StrHTML = ListRetention();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}
		month = (Integer.parseInt(dr_month));
		if (dr_year.equals("")) {
			dr_year = ToShortDate(kknow()).substring(0, 4);
		}
		year = (Integer.parseInt(dr_year));

		x = new String[13];
		for (int i = 1; i <= 12; i++) {
			if (i < 10) {
				x[i] = dr_year + "0" + i;
			} else {
				x[i] = dr_year + i;
			}
		}
	}

	public String PopulateYears() {

		String year = ToShortDate(kknow()).substring(0, 4);
		StringBuilder years = new StringBuilder();
		for (int i = Integer.parseInt(year) - 2; i <= Integer.parseInt(year) + 1; i++) {
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

	public String ListRetention() {

		int total_vehsold = 0;
		int total_servicein = 0;
		int total_appt = 0;
		String doublemonth = "";
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = " SELECT SUBSTR(calmonth, 5, 2) as doublemonth,"
					+ " MONTHNAME(concat(calmonth,'00')) as 'month',"

					+ " COALESCE((SELECT count(veh_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE SUBSTR(veh_sale_date, 5, 2) = " + dr_month + "), 0) AS vehsold,"

					+ " COALESCE((SELECT count(distinct veh_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ "	INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id=veh_branch_id "
					+ " WHERE SUBSTR(jc_time_in,1,6) = SUBSTR(calmonth,1,6)"
					+ " AND SUBSTR(veh_sale_date, 5, 2) = " + dr_month
					+ StrSearch + " ) ,  0) AS jctime,"

					+ " COALESCE((SELECT count(distinct veh_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
					+ "	INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id=veh_branch_id "
					+ " WHERE SUBSTR(booking_time,1,6) = SUBSTR(calmonth,1,6)"
					+ " AND SUBSTR(veh_sale_date, 5, 2) = " + dr_month
					+ StrSearch + " ) ,  0)AS appttime"
					+ " FROM (";

			for (int i = 1; i <= 12; i++) {
				StrSql = StrSql + " SELECT " + x[i] + " as calmonth ";
				if (i != 12) {
					StrSql = StrSql + " UNION ";
				}
			}
			StrSql = StrSql + " ) AS cal" + " GROUP BY calmonth"
					+ " ORDER BY calmonth";
			SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive \">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th  data-toggle=\"true\">Month</th>\n");
				Str.append("<th>Vehicles Sold</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Service In</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Bookings</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				while (crs.next()) {
					total_servicein += crs.getInt("jctime");
					total_appt += crs.getInt("appttime");
					doublemonth = crs.getString("doublemonth");
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("month") + "</td>");
					Str.append("<td valign=top align=right>");
					if (crs.getString("doublemonth").equals(dr_month)) {
						total_vehsold += crs.getInt("vehsold");
						Str.append("<a href=").append(SearchURL).append("?sold=yes")
								.append("&salesmonth=").append(dr_month).append("&serviceyear=").append(dr_year)
								.append("&currentmonth=").append(doublemonth).append(" target=_blank>");
						Str.append("" + crs.getString("vehsold") + "</a>");
					} else {
						Str.append("" + 0 + "");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("?servicein=yes")
							.append("&salesmonth=").append(dr_month).append("&serviceyear=").append(dr_year)
							.append("&currentmonth=").append(doublemonth).append(" target=_blank>");
					Str.append(crs.getString("jctime")).append("</a></td>");
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("?appt=yes")
							.append("&salesmonth=").append(dr_month).append("&serviceyear=").append(dr_year)
							.append("&currentmonth=").append(doublemonth).append(" target=_blank>");
					Str.append(crs.getString("appttime")).append("</a></td>");
					Str.append("</tr>\n");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td valign=top align=right colspan=2><b>Total:</b></td>");
				Str.append("<td valign=top align=right>");
				Str.append("<a href=").append(SearchURL).append("?sold=yes")
						.append("&salesmonth=").append(dr_year).append(" target=_blank>");
				Str.append("<b>" + total_vehsold + "</b></a></td>");
				Str.append("<td valign=top align=right><b>" + total_servicein + "</b></td>");
				Str.append("<td valign=top align=right><b>" + total_appt + "</b></td>");
				Str.append("</tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<td valign=top align=right colspan=3><b>Difference:</b></td>");
				Str.append("<td valign=top align=right><b>" + (total_vehsold - total_servicein) + "</b></td>");
				Str.append("<td valign=top align=right>&nbsp;</td>");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><b><font color=red>No Retention Details Found!</font></b><br><br>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
