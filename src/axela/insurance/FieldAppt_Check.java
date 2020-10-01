//smitha nag 13 feb 2013
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class FieldAppt_Check extends Connect {

	public String branch_id = "";
	public String appt = "";
	public String fieldapptdrivedate = "";
	public String fieldapptemp_id = "";
	public String starttime = "";
	public String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = PadQuotes(request.getParameter("branch_id"));
			appt = PadQuotes(request.getParameter("appt"));
			fieldapptdrivedate = PadQuotes(request.getParameter("fieldapptdrivedate"));
			fieldapptemp_id = PadQuotes(request.getParameter("fieldapptemp_id"));

			if (!fieldapptdrivedate.equals("") && isValidDateFormatLong(fieldapptdrivedate)) {
				starttime = ConvertLongDateToStr(fieldapptdrivedate);
			}
			fieldapptdrivedate = strToShortDate(ConvertShortDateToStr(fieldapptdrivedate));

			if (appt.equals("yes")) {
				StrHTML = FieldApptCalender();
			}
		}
	}
	protected String FieldApptCalender() {

		StringBuilder Str = new StringBuilder();
		if (fieldapptdrivedate.equals("")) {
			Str.append("<br><br><font color=red><b>Select Date!</b></font>");
			return Str.toString();
		}
		if (fieldapptemp_id.equals("0")) {
			Str.append("<br><br><font color=red><b>Select Executive!</b></font>");
			return Str.toString();
		}
		try {
			StrSql = " SELECT fieldappt_id, branch_code, customer_name, fieldappt_totime,"
					+ " fieldappt_fromtime, fieldappt_insurenquiry_id, customer_id, emp_id,"
					+ " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name, fieldappt_appttime"
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = fieldappt_insurenquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = fieldappt_emp_id "
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(fieldappt_appttime, 1, 8) = " + "SUBSTR(" + starttime + ",1,8)"
					+ " AND fieldappt_emp_id = " + fieldapptemp_id
					+ " GROUP BY fieldappt_id"
					+ " ORDER BY fieldappt_fromtime";
			// SOP("StrSql----appt----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<b>" + fieldapptdrivedate + "</b>");
				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td valign=top>");
					Str.append("<b>" + PeriodTime(crs.getString("fieldappt_fromtime"), crs.getString("fieldappt_totime"), "2") + "</b>");
					Str.append("<br>Field Appointment Time: " + SplitHourMin(crs.getString("fieldappt_appttime")) + "");
					Str.append("<br>Field Appointment ID: " + crs.getString("fieldappt_id"));
					Str.append("<br>Field Executive: <a href=../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + ">" + crs.getString("emp_name") + "</a>");
					Str.append("<br>Customer Name: <a href=../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "><b>" + crs.getString("customer_name")
							+ " (" + crs.getString("customer_id") + ")</b></a>");
					Str.append("<br>Insurance Enquiry ID :<a href=../insurance/insurance-enquiry-dash.jsp?"
							+ "insurenquiry_id=" + crs.getString("fieldappt_insurenquiry_id") + ">"
							+ crs.getString("fieldappt_insurenquiry_id") + "</a>");

					Str.append("</td></tr>");

				}
				Str.append("<tbody></table>");
				// Str.append("<div><br>");
			} else {
				Str.append("<br><br><font color=red><b>No Field Appointment(s) found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
