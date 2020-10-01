//smitha nag 13 feb 2013
package axela.axelaauto_app;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_TestDrive_Check extends Connect {

	public String model_id = "";
	public String branch_id = "";
	public String testdrive = "";
	public String testdrive_veh_id = "";
	public String testdrivedate = "";
	public String starttime = "";
	public String endtime = "";
	public String msg = "";
	public String output = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String emp_testdrive_edit = "";
	public String StrSql = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		comp_id = CNumeric(GetSession("comp_id", request));
		branch_id = PadQuotes(request.getParameter("branch_id"));
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!emp_id.equals("0")) {
			testdrive = PadQuotes(request.getParameter("testdrive"));
			testdrive_veh_id = PadQuotes(request.getParameter("veh_id"));
			testdrivedate = PadQuotes(request.getParameter("testdrivedate"));
			if (!testdrivedate.equals("") && isValidDateFormatLong(testdrivedate)) {
				starttime = ConvertLongDateToStr(testdrivedate);
				endtime = ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0));
			}
			testdrivedate = strToShortDate(ConvertShortDateToStr(testdrivedate));

			// SOP("testdrivedate =22= " + testdrivedate);
			// if (testdrive.equals("")) {
			// StrHTML = PopulateVehicle();
			// } else {
			// emp_testdrive_edit = ExecuteQuery("select emp_testdrive_edit from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id);
			StrHTML = TestDriveCalendar();
			// }
		}
	}

	protected String TestDriveCalendar() {
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		String search = "";
		if (testdrivedate.equals("")) {
			Str.append("<br><font color=red>Select Date!</font><br><br>");
			return Str.toString();
		}
		if (testdrive_veh_id.equals("0")) {
			Str.append("<br><font color=red>Select Vehicle!</font><br><br>");
			return Str.toString();
		}
		if (!testdrive_veh_id.equals("0") && !testdrivedate.equals("")) {
			StrSql = "select salesgatepass_fromtime, salesgatepass_totime, salesgatepass_notes "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ "  where salesgatepass_testdriveveh_id = " + testdrive_veh_id + " and "
					+ " ((salesgatepass_fromtime >= " + starttime + " and salesgatepass_fromtime < " + endtime + ") "
					+ " or (salesgatepass_totime > " + starttime + " and salesgatepass_totime <= " + endtime + ") "
					+ " or (salesgatepass_fromtime < " + starttime + " and salesgatepass_totime > " + endtime + "))"
					+ " order by salesgatepass_fromtime";
			SOP("StrSql==" + StrSql);
			try {
				SOP("StrSql----cal---" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"col-xs-12\" style=\"text-align:left\">");
					Str.append("<font color=red>Vehicle Outage: </font><br>");
					while (crs.next()) {
						Str.append("<font color=red>" + strToLongDate(crs.getString("outage_fromtime")) + " - " + strToLongDate(crs.getString("outage_totime")) + "</b></font>");
					}
					Str.append("</div><br><br>");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		if (!testdrive_veh_id.equals("0")) {
			search = " AND testdrive_testdriveveh_id=" + testdrive_veh_id + "";
		}
		search = search + " AND enquiry_branch_id = " + branch_id + "";
		if (!testdrivedate.equals("")) {
			// SOP("testdrivedate =33- " + testdrivedate);
			search = search + " AND date_format(testdrive_time,'%d/%m/%Y') = '" + testdrivedate + "'";
		}
		try {
			StrSql = " SELECT testdrive_id, testdriveveh_name, testdriveveh_regno, branch_code, customer_name, testdrive_time_to, testdrive_time_from, testdrive_confirmed,"
					+ " concat('ENQ',branch_code,enquiry_no) as enquiry_no, testdrive_doc_value, customer_id, enquiry_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, "
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_testdriveveh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
					+ " WHERE 1=1  ";
			StrSql = StrSql + search + " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_time_from";
			// SOP("strqq=======outage=========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<center><br><b>" + testdrivedate + "</b></center>");
				while (crs.next()) {
					Str.append("<div class=\"row\" style=\"border: 1px solid black; margin: 20px; text-align: left;\">");
					if (!crs.getString("testdrive_time_from").equals("")) {
						Str.append("<br><div class=\"col-xs-12\">");
						Str.append("<b>Test Drive Duration: </b> ");
						Str.append("" + PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "2") + "");
						Str.append("</div>");
					} else {
						Str.append("<font color=red>Not Confirmed</font>");
					}
					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Test Drive Time: </b>");
					Str.append("" + SplitHourMin(crs.getString("testdrive_time")));
					Str.append("</div>");
					if (crs.getString("testdrive_confirmed").equals("0")) {
						Str.append("<br><br><font color=red>[Not Confirmed]</font><br>");
					}
					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Test Drive ID: </b>");
					Str.append("" + crs.getString("testdrive_id"));
					Str.append("</div>");

					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Vehicle: </b>");
					Str.append("" + crs.getString("testdriveveh_name"));
					Str.append("</div>");

					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Reg. No.: </b>");
					Str.append("" + crs.getString("testdriveveh_regno"));
					Str.append("</div>");

					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Location: </b>");
					Str.append("" + crs.getString("location_name"));
					Str.append("</div>");

					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Sales Consultant: </b>");
					Str.append("" + crs.getString("emp_name"));
					Str.append("</div>");

					Str.append("<br><br><div class=\"col-xs-12\">");
					Str.append("<b>Customer: </b>");
					Str.append("" + crs.getString("customer_name") + " (" + crs.getString("customer_id")
							+ ")");
					Str.append("</div><br><br>\n");

					if (!crs.getString("testdrive_out_time").equals("")) {
						Str.append("<br><br><div class=\"col-xs-12\">");
						Str.append("<b>Out Time: </b>");
						Str.append("" + SplitHourMin(crs.getString("testdrive_out_time")) + ")");
						Str.append("</div>\n");
						if (!crs.getString("testdrive_in_time").equals("")) {
							Str.append("<div class=\"col-xs-12\">");
							Str.append("<b>In Time: </b>");
							Str.append("" + SplitHourMin(crs.getString("testdrive_in_time")) + ")");
							Str.append("</div><br>\n");
						}
						if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
							String Hours = deci.format(getHoursBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							String Mins = deci.format(getMinBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							Hours = doublenum(Integer.parseInt(Hours));
							Mins = doublenum(Integer.parseInt(Mins));

							Str.append("<br><br><div class=\"col-xs-12\">");
							Str.append("<b>Duration: </b>");
							Str.append("" + Hours + ":" + Mins + ")");
							Str.append("</div>\n");
						}

						Str.append("<br><br><div class=\"col-xs-12\">");
						Str.append("<b>Out Kms: </b>");
						Str.append("" + crs.getString("testdrive_out_kms") + "kms.");
						Str.append("</div>\n");

						if (!crs.getString("testdrive_in_time").equals("")) {

							Str.append("<br><br><div class=\"col-xs-12\">");
							Str.append("<b>In Kms: </b>");
							Str.append("" + crs.getString("testdrive_in_kms") + "kms.");
							Str.append("</div>\n");
						}
					}
					if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
						if (!crs.getString("testdrive_in_kms").equals(crs.getString("testdrive_out_kms"))) {

							Str.append("<br><br><div class=\"col-xs-12\">");
							Str.append("Mileage: </b>");
							Str.append("" + (crs.getDouble("testdrive_in_kms") - crs.getDouble("testdrive_out_kms")) + "kms.");
							Str.append("</div></b>\n");
						}
					}
					Str.append("</div><br>");
				}
			} else {
				Str.append("<br><center><font color=red>No Test Drive(s) found!</font></center>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
