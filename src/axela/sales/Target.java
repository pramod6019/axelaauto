// Smitha Nag 26, 28 march 2013
package axela.sales;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target extends Connect {
	
	public String StrHTML = "";
	public String StrSql = "";
	public String year = "";
	public int curryear = 0;
	public String[] x = new String[13];
	public String comp_id = "0";
	public String QueryString = "";
	public int total_enquiry_count = 0, total_enquiry_calls_count = 0;
	public int total_enquiry_meetings_count = 0, total_enquiry_testdrives_count = 0;
	public int total_enquiry_hot_count = 0, total_so_count = 0;
	public int total_so_min = 0;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_target_access", request, response);
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
		StrSql = "select concat(YEAR(calmonth),'-',substr(MONTHNAME(calmonth),1,3)) as yearmonth,  "
				+ " coalesce((select sum(target_enquiry_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) as target_enquiry_count , "
				+ " coalesce((select sum(target_enquiry_calls_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) as target_enquiry_calls_count,  "
				+ " coalesce((select sum(target_enquiry_meetings_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) as target_enquiry_meetings_count,  "
				+ " coalesce((select sum(target_enquiry_testdrives_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ " (SUBSTR(calmonth, 1, 8)+31)), 0) as target_enquiry_testdrives_count,  "
				+ " coalesce((select sum(target_enquiry_hot_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ "  (SUBSTR(calmonth, 1, 8)+31)), 0) as target_enquiry_hot_count, "
				
				+ " coalesce((select sum(target_so_count) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ "  (SUBSTR(calmonth, 1, 8)+31)), 0) as target_so_count, "
				
				+ " coalesce((select sum(target_so_min) from " + compdb(comp_id) + "axela_sales_target  "
				+ " where SUBSTR(target_startdate, 1, 8) >= SUBSTR(calmonth, 1, 8) and SUBSTR(target_enddate, 1, 8)<= "
				+ "  (SUBSTR(calmonth, 1, 8)+31)), 0) as target_so_min "
				+ " from (  ";
		for (int i = 0; i <= 11; i++) {
			StrSql = StrSql + " Select " + ConvertShortDateToStr(x[i]) + " as calmonth";
			// SOP("x length is.... " + x.length);
			if (i != x.length - 2) {
				StrSql = StrSql + " UNION";
			}
		}
		StrSql = StrSql + " ) as cal"
				+ " group by calmonth"
				+ " order by calmonth";
		// SOP(StrSqlBreaker(StrSql));
		crs = processQuery(StrSql, 0);
		try {
			Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead>\n");
			Str.append("<tr>\n");
			Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
			Str.append("<th data-toggle=\"true\">Month</th>\n");
			Str.append("<th>Enquiry Count</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry Calls</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry Meeting</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry Test Drives</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry Hot</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry SO Count</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Enquiry SO Minimum</th>\n");
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
				Str.append("<td valign=top align=right>").append(crs.getString("target_enquiry_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_enquiry_calls_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_enquiry_meetings_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_enquiry_testdrives_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_enquiry_hot_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_so_count")).append("</td>\n");
				Str.append("<td valign=top align=right>").append(crs.getString("target_so_min")).append("</td>\n");
				Str.append("</tr>\n");
				total_enquiry_count = (int) (total_enquiry_count + crs.getDouble("target_enquiry_count"));
				total_enquiry_calls_count = (int) (total_enquiry_calls_count + crs.getDouble("target_enquiry_calls_count"));
				total_enquiry_meetings_count = (int) (total_enquiry_meetings_count + crs.getDouble("target_enquiry_meetings_count"));
				total_enquiry_testdrives_count = (int) (total_enquiry_testdrives_count + crs.getDouble("target_enquiry_testdrives_count"));
				total_enquiry_hot_count = (int) (total_enquiry_hot_count + crs.getDouble("target_enquiry_hot_count"));
				total_so_count = (int) (total_so_count + crs.getDouble("target_so_count"));
				total_so_min = (int) (total_so_min + crs.getDouble("target_so_min"));
			}
			Str.append("<tr>");
			Str.append("<td colspan='2' align = 'right'><b>Total:</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_enquiry_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_enquiry_calls_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_enquiry_meetings_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_enquiry_testdrives_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_enquiry_hot_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_so_count).append("</b></td>\n");
			Str.append("<td align = 'right'><b>").append(total_so_min).append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table>");
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
