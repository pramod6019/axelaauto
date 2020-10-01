// Smitha Nag 26, 28 march 2013
package axela.insurance;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Target extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String year = "";
	public int curryear = 0;
	public String[] x = new String[13];
	public String comp_id = "0";
	public String QueryString = "";
	public long insurance_target_enquiry_count = 0, insurance_target_policy_count = 0;

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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(HttpServletRequest request) throws SQLException {

		StringBuilder Str = new StringBuilder();
		CachedRowSet crs;
		String date = "01/01/" + year;

		for (int j = 0; j <= 11; j++) {
			x[j] = AddDayMonthYear(date, 0, 0, j, 0);
		}
		StrSql = "SELECT CONCAT(YEAR(calmonth),'-',SUBSTR(MONTHNAME(calmonth),1,3)) AS yearmonth,"
				+ " COALESCE((SELECT"
				+ " SUM(insurance_target_enquiry_count)"
				+ " FROM " + compdb(comp_id) + "axela_insurance_target"
				+ " WHERE SUBSTR(insurance_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8)"
				+ " AND SUBSTR(insurance_target_enddate, 1, 8) <= (SUBSTR(calmonth, 1, 8) + 31)), 0)"
				+ " AS insurance_target_enquiry_count,"
				+ " COALESCE((SELECT "
				+ " SUM(insurance_target_policy_count)"
				+ " FROM " + compdb(comp_id) + "axela_insurance_target"
				+ " WHERE SUBSTR(insurance_target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8)"
				+ " AND SUBSTR(insurance_target_enddate, 1, 8) <= (SUBSTR(calmonth, 1, 8) + 31)), 0)"
				+ " AS insurance_target_policy_count"
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
		// SOP("StrSql----" + StrSql);
		crs = processQuery(StrSql, 0);
		try {
			Str.append("<div class=\" table-bordered\">\n");
			Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			Str.append("<thead>\n");
			Str.append("<tr>\n");
			Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
			Str.append("<th data-toggle=\"true\">Month</th>\n");
			Str.append("<th>Insurance Enquiry</th>\n");
			Str.append("<th data-hide=\"phone\">Insurance Policy</th>\n");
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
				Str.append("<td valign=top align=right>").append(crs.getString("insurance_target_enquiry_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("insurance_target_policy_count")).append("</td>\n");
				Str.append("</tr>\n");
				insurance_target_enquiry_count = insurance_target_enquiry_count + Long.parseLong(crs.getString("insurance_target_enquiry_count"));
				insurance_target_policy_count = insurance_target_policy_count + Long.parseLong(crs.getString("insurance_target_policy_count"));
			}
			// SOP("service_target_oil_amount====" + service_target_oil_amount);
			Str.append("<tr>");
			Str.append("<td colspan='2' align = 'right'><b>Total:</b></td>\n");
			Str.append("<td align='right'><b>").append(insurance_target_enquiry_count).append("</b></td>\n");
			Str.append("<td align='right'><b>").append(insurance_target_policy_count).append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table>");
			Str.append("</div>\n");
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
