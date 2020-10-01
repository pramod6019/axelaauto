package axela.preowned;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_TestDrive_Check extends Connect {

	public String model_id = "";
	public String branch_id = "";
	public String testdrive = "";
	public String testdrive_preownedstock_id = "";
	public String testdrivedate = "";
	public String starttime = "";
	public String endtime = "";
	public String msg = "";
	public String output = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_preowned_testdrive_edit = "";
	public String StrSql = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = PadQuotes(request.getParameter("branch_id"));
			model_id = PadQuotes(request.getParameter("model_id"));
			testdrive = PadQuotes(request.getParameter("testdrive"));
			testdrive_preownedstock_id = PadQuotes(request.getParameter("preownedstock_id"));
			testdrivedate = PadQuotes(request.getParameter("testdrivedate"));
			if (!testdrivedate.equals("") && isValidDateFormatLong(testdrivedate)) {
				starttime = ConvertLongDateToStr(testdrivedate);
				endtime = ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0));
			}
			testdrivedate = strToShortDate(ConvertShortDateToStr(testdrivedate));
			if (testdrive.equals("")) {
				StrHTML = PopulateVariant();
			} else {
				StrHTML = TestDriveCalendar();
			}
		}

	}

	public String PopulateVariant() {
		// SOP("====");
		String search = "";
		if (!model_id.equals("")) {
			model_id = model_id.substring(0, model_id.length() - 1);
			search = search + " and variant_preownedmodel_id in (" + model_id + ")";
		}
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT preownedstock_id, variant_name, preowned_regno "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preowned_id  = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " WHERE preowned_branch_id = " + branch_id + search + ""
					+ " ORDER BY variant_name";
			// SOP("StrSql check page variant =---------= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_preownedstock_id id=dr_preownedstock_id class=form-control multiple size=10 style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstock_id")).append(">");
				if (!crs.getString("preowned_regno").equals("")) {
					Str.append(crs.getString("variant_name")).append("-").append(crs.getString("preowned_regno"));
				} else {
					Str.append(crs.getString("variant_name"));
				}
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected String TestDriveCalendar() {
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		String search = "";
		if (testdrivedate.equals("")) {
			Str.append("<br><br><font color=red><b>Select Date!</b></font>");
			return Str.toString();
		}
		if (testdrive_preownedstock_id.equals("0")) {
			Str.append("<br><br><font color=red><b>Select Variant!</b></font>");
			return Str.toString();
		}
		try {
			StrSql = "SELECT testdrive_id, variant_name, preowned_regno, branch_code, customer_name,"
					+ " testdrive_time_to, testdrive_time_from, testdrive_confirmed, preowned_no, testdrive_doc_value,"
					+ " customer_id, preowned_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') AS emp_name,"
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms,"
					+ " enquiry_id, concat('OPR',branch_code,enquiry_no) as enquiry_no"
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					// + " INNER JOIN axela_preowned_model ON preownedmodel_id = preowned_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id"
					+ " WHERE 1=1 "
					+ " AND testdrive_preownedstock_id = " + testdrive_preownedstock_id + ""
					+ " AND preowned_branch_id = " + branch_id + ""
					+ " AND substr(testdrive_time,1,8) = substr('" + ConvertShortDateToStr(testdrivedate) + "',1,8)"
					// + " AND preowned_preownedmodel_id = " + model_id + ""
					+ " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_time_from";
			// SOP("StrSql----------===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>" + testdrivedate + "</b>");
				Str.append("<div class=\"table-responsive \">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td valign=top>");
					if (!crs.getString("testdrive_time_from").equals("")) {
						Str.append("<b>" + PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "2") + "</b>");
					} else {
						Str.append("<font color=red><b>Not Confirmed</b></font>");
					}
					Str.append("<br>Test Drive Time: " + SplitHourMin(crs.getString("testdrive_time")) + "");
					if (crs.getString("testdrive_confirmed").equals("0")) {
						Str.append("<br><font color=red>[Not Confirmed]</font>");
					}
					Str.append("<br>Test Drive ID: " + crs.getString("testdrive_id"));
					Str.append("<br>" + crs.getString("variant_name") + "");
					Str.append("<br>Reg No: " + crs.getString("preowned_regno") + "");
					Str.append("<br>" + crs.getString("location_name"));
					Str.append("<br><a href=../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + ">" + crs.getString("emp_name") + "</a>");
					Str.append("<br>"
							+ "<a href=../sales/enquiry-dash-opportunity.jsp?enquiry_id="
							+ crs.getString("enquiry_id") + "><b>"
							+ crs.getString("enquiry_no") + "</b>"
							+ "</a>");
					// Str.append("<br><a href=../preowned/preowned-dash.jsp?preowned_id="
					// + crs.getString("preowned_id") + "><b>" +
					// crs.getString("preowned_no") + "</b></a>");
					Str.append("<br><a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "><b>" + crs.getString("customer_name") + " (" + crs.getString("customer_id")
							+ ")</b></a>");
					if (!crs.getString("testdrive_out_time").equals("")) {
						Str.append("<br>Out Time: " + SplitHourMin(crs.getString("testdrive_out_time")) + "");
						if (!crs.getString("testdrive_in_time").equals("")) {
							Str.append("<br>In Time: " + SplitHourMin(crs.getString("testdrive_in_time")) + "");
						}
						if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
							String Hours = deci.format(getHoursBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							String Mins = deci.format(getMinBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							// SOP("Hours in Test DriveCalendar---" + Hours);
							Hours = doublenum(Integer.parseInt(Hours));
							Mins = doublenum(Integer.parseInt(Mins));
							Str.append("<br>Duration: " + Hours + ":" + Mins + "");
						}
						Str.append("<br>Out Kms: " + crs.getString("testdrive_out_kms") + " kms.");
						if (!crs.getString("testdrive_in_time").equals("")) {
							Str.append("<br>In Kms: " + crs.getString("testdrive_in_kms") + " kms.");
						}
					}
					if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
						if (!crs.getString("testdrive_in_kms").equals(crs.getString("testdrive_out_kms"))) {
							Str.append("<br>Mileage: " + (crs.getDouble("testdrive_in_kms") - crs.getDouble("testdrive_out_kms")) + " kms.");
						}
					}
					Str.append("<br></td>");
					Str.append("</tr>");

				}
			} else {
				Str.append("<br><br><font color=red><b>No Test Drive(s) found!</b></font>");
			}
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
