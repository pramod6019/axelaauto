//smitha nag 13 feb 2013
package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Check extends Connect {
	
	public String model_id = "";
	public String branch_id = "";
	public String testdrive = "", vehicle = "";
	public String testdrive_testdriveveh_id = "";
	public String testdrivedate = "";
	public String starttime = "";
	public String endtime = "";
	public String msg = "";
	public String output = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_testdrive_edit = "";
	public String StrSql = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	public String StrHTML = "";
	public String model = "", item_model_id = "";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = PadQuotes(request.getParameter("branch_id"));
			model_id = PadQuotes(request.getParameter("model_id"));
			item_model_id = PadQuotes(request.getParameter("item_model_id"));
			
			model = PadQuotes(request.getParameter("model"));
			// SOP(""model==" + model + " model_id==" + model_id);
			// SOP("model_id is...." + model_id);
			testdrive = PadQuotes(request.getParameter("testdrive"));
			vehicle = PadQuotes(request.getParameter("vehicle"));
			testdrive_testdriveveh_id = PadQuotes(request.getParameter("testdriveveh_id"));
			testdrivedate = PadQuotes(request.getParameter("testdrivedate"));
			// SOP("testdrivedate = " + testdrivedate);
			if (!testdrivedate.equals("") && isValidDateFormatLong(testdrivedate)) {
				starttime = ConvertLongDateToStr(testdrivedate);
				endtime = ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0));
			}
			testdrivedate = strToShortDate(ConvertShortDateToStr(testdrivedate));
			
			// SOP("testdrivedate =22= " + testdrivedate);
			if (testdrive.equals("")) {
				StrHTML = PopulateVehicle();
			} else {
				// emp_testdrive_edit = ExecuteQuery("select emp_testdrive_edit from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id);
				StrHTML = TestDriveCalendar();
			}
			if (model.equals("yes")) {
				// SOP(""model_id==" + item_model_id);
				StrHTML = new TestDrive_Update().PopulateVehicle(comp_id, item_model_id, branch_id);
			}
			if (vehicle.equals("yes")) {
				StrHTML = new TestDrive_Cal().PopulateVehicle(comp_id, model_id);
			}
		}
	}
	
	public String PopulateVehicle() {
		StringBuilder Str = new StringBuilder();
		String search = "";
		if (!branch_id.equals("") && !branch_id.equals("0")) {
			search = " and testdriveveh_branch_id=" + branch_id;
		}
		if (!model_id.equals("")) {
			model_id = model_id.substring(0, model_id.length() - 1);
			search = search + " and item_model_id in (" + model_id + ")";
		}
		try {
			StrSql = "SELECT testdriveveh_id, testdriveveh_name "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id "
					+ " where 1=1 " + search
					+ " group by testdriveveh_id order by testdriveveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("Vehicle==" + StrSql);
			Str.append("<select name=dr_vehicle id=dr_vehicle class=form-control multiple size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("testdriveveh_id")).append(">").append(crs.getString("testdriveveh_name")).append("");
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
		if (testdrive_testdriveveh_id.equals("0")) {
			Str.append("<br><br><font color=red><b>Select Vehicle!</b></font>");
			return Str.toString();
		}
		if (!testdrive_testdriveveh_id.equals("0") && !testdrivedate.equals("")) {
			StrSql = "select salesgatepass_fromtime, salesgatepass_totime, salesgatepass_notes "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ "  where salesgatepass_testdriveveh_id = " + testdrive_testdriveveh_id + " and "
					+ " ((salesgatepass_fromtime >= " + starttime + " and salesgatepass_fromtime < " + endtime + ") "
					+ " or (salesgatepass_totime > " + starttime + " and salesgatepass_totime <= " + endtime + ") "
					+ " or (salesgatepass_fromtime < " + starttime + " and salesgatepass_totime > " + endtime + "))"
					+ " order by salesgatepass_fromtime";
			// SOP(""StrSql==" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					Str.append("<font color=red><b>Vehicle Outage : </b></font><br>");
				}
				while (crs.next()) {
					Str.append("<font color=red><b>" + strToLongDate(crs.getString("salesgatepass_fromtime")) + " - " + strToLongDate(crs.getString("salesgatepass_totime")) + "</b></font><br>");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		if (!testdrive_testdriveveh_id.equals("0")) {
			search = " and testdrive_testdriveveh_id=" + testdrive_testdriveveh_id + "";
		}
		search = search + " and enquiry_branch_id = " + branch_id + "";
		if (!testdrivedate.equals("")) {
			
			// SOP("testdrivedate =33- " + testdrivedate);
			search = search + " and date_format(testdrive_time,'%d/%m/%Y') = '" + testdrivedate + "'";
		}
		try {
			StrSql = " select testdrive_id, testdriveveh_name, testdriveveh_regno, branch_code, customer_name, testdrive_time_to, testdrive_time_from, testdrive_confirmed,"
					+ " concat('ENQ',branch_code,enquiry_no) as enquiry_no, testdrive_doc_value, customer_id, enquiry_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, "
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive "
					+ " inner Join " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " inner Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_testdriveveh_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
					+ " where 1=1  ";
			StrSql = StrSql + search
					+ " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_time_from";
			// SOP(""strqq..outage" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				
				// SOP("testdrivedate =44- " + testdrivedate);
				Str.append("<b>" + testdrivedate + "</b>");
				// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<center><table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<tbody>\n");
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
					Str.append("<br>" + crs.getString("testdriveveh_name") + "");
					Str.append("<br>Reg No: " + crs.getString("testdriveveh_regno") + "");
					Str.append("<br>" + crs.getString("location_name"));
					// if (crs.getString("testdrive_fb_taken").equals("1")) {
					// Str.append("<br>" + "Test Drive Taken!");
					// }
					// if(emp_testdrive_edit.equals("1"))
					// {
					Str.append("<br><a href=../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + ">" + crs.getString("emp_name") + "</a>");
					Str.append("<br><a href=../sales/enquiry-dash.jsp?enquiry_id=" + crs.getString("enquiry_id") + "><b>" + crs.getString("enquiry_no") + "</b></a>");
					Str.append("<br><a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "><b>" + crs.getString("customer_name") + " (" + crs.getString("customer_id")
							+ ")</b></a>");
					// }
					if (!crs.getString("testdrive_out_time").equals("")) {
						Str.append("<br>Out Time: " + SplitHourMin(crs.getString("testdrive_out_time")) + "");
						if (!crs.getString("testdrive_in_time").equals("")) {
							Str.append("<br>In Time: " + SplitHourMin(crs.getString("testdrive_in_time")) + "");
						}
						if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
							String Hours = deci.format(getHoursBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							String Mins = deci.format(getMinBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
							// SOP("Hours in TestDriveCalendar---" + Hours);
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
					Str.append("</tr>");
				}
				Str.append("<tbody></table></center><br>");
			} else {
				Str.append("<br><br><font color=red><b>No Test Drive(s) found!</b></font>");
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
