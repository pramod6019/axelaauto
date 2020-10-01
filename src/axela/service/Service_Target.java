// Smitha Nag 26, 28 march 2013
package axela.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Target extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String year = "";
	public int curryear = 0;
	public String[] x = new String[13];
	public String comp_id = "0";
	public String QueryString = "";
	public double service_target_labour_amount = 0, service_target_parts_amount = 0;
	public double service_target_oil_amount = 0, service_target_tyre_amount = 0;
	public double service_target_accessories_amount = 0, service_target_vas_amount = 0;
	DecimalFormat deci = new DecimalFormat("0.00");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "service_target_access", request, response);
			if (!comp_id.equals("0")) {
				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}
				StrHTML = Listdata(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(HttpServletRequest request) throws SQLException {

		StringBuilder Str = new StringBuilder();
		CachedRowSet crs;
		String date = "01/01/" + year;

		for (int j = 0; j <= 11; j++) {
			x[j] = AddDayMonthYear(date, 0, 0, j, 0);
		}
		StrSql = "SELECT CONCAT(YEAR(calmonth),'-',SUBSTR(MONTHNAME(calmonth),1,3)) AS yearmonth,  "
				+ " COALESCE((SELECT SUM(service_target_labour_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) as service_target_labour_amount , "
				+ " coalesce((select SUM(service_target_parts_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) AS service_target_parts_amount,  "
				+ " coalesce((select SUM(service_target_oil_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) AS service_target_oil_amount,  "
				+ " COALESCE((SELECT SUM(service_target_tyre_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) AS service_target_tyre_amount,  "
				+ " COALESCE((SELECT SUM(service_target_accessories_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ "  (SUBSTR(calmonth, 1, 8)+31)), 0) AS service_target_accessories_amount, "
				+ " COALESCE((select sum(service_target_vas_amount) FROM " + compdb(comp_id) + "axela_service_target  "
				+ " WHERE SUBSTR(service_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) AND SUBSTR(service_target_enddate, 1, 8)<= "
				+ "  (SUBSTR(calmonth, 1, 8)+31)), 0) AS service_target_vas_amount "
				+ " FROM (  ";
		for (int i = 0; i <= 11; i++) {
			StrSql = StrSql + " SELECT " + ConvertShortDateToStr(x[i]) + " AS calmonth";
			// SOP("x length is.... " + x.length);
			if (i != x.length - 2) {
				StrSql = StrSql + " UNION";
			}
		}
		StrSql = StrSql + " ) AS cal"
				+ " GROUP BY calmonth"
				+ " ORDER BY calmonth";
		// SOP(StrSqlBreaker(StrSql));
		crs = processQuery(StrSql, 0);
		try {
			Str.append("<div class=\"  table-bordered\"><table class=\"table  \" data-filter=\"#filter\">\n");
			Str.append("<thead>\n");
			Str.append("<tr>\n");
			Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
			Str.append("<th data-toggle=\"true\">Month</th>\n");
			Str.append("<th>Labour Amount</th>\n");
			Str.append("<th data-hide=\"phone\">Parts Amount</th>\n");
			Str.append("<th data-hide=\"phone\">Oil Amount</th>\n");
			Str.append("<th data-hide=\"phone\">Tyre Amount</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Accessories Amount</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">VAS Amount</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			int count = 0;
			while (crs.next()) {
				count++;
				Str.append("<tr><td valign=top align=center>").append(count).append("</td>\n");
				Str.append("<td valign=top>");
				Str.append(TextMonth(count - 1)).append("-").append(year);
				// Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(target_id).append(">");
				Str.append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_labour_amount"))).append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_parts_amount"))).append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_oil_amount"))).append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_tyre_amount"))).append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_accessories_amount"))).append("</td>\n");
				Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("service_target_vas_amount"))).append("</td>\n");
				Str.append("</tr>\n");
				service_target_labour_amount = (service_target_labour_amount + crs.getDouble("service_target_labour_amount"));
				service_target_parts_amount = (service_target_parts_amount + crs.getDouble("service_target_parts_amount"));
				service_target_oil_amount = (service_target_oil_amount + crs.getDouble("service_target_oil_amount"));
				service_target_tyre_amount = (service_target_tyre_amount + crs.getDouble("service_target_tyre_amount"));
				service_target_accessories_amount = (service_target_accessories_amount + crs.getDouble("service_target_accessories_amount"));
				service_target_vas_amount = (service_target_vas_amount + crs.getDouble("service_target_vas_amount"));
			}
			// SOP("service_target_oil_amount====" + service_target_oil_amount);
			Str.append("<tr>");
			Str.append("<td colspan='2' align = 'right'><b>Total:</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_labour_amount)).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_parts_amount)).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_oil_amount)).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_tyre_amount)).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_accessories_amount)).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(deci.format(service_target_vas_amount)).append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table></div>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
