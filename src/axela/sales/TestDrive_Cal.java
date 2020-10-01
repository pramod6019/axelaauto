// smitha nag 18 feb 2013
package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Cal extends Connect {
	
	public String add = "", StrHTML = "", emp_id = "";
	public String submitB = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";
	public String item_model_id = "", testdrive_testdriveveh_id = "";
	public String[] item_model_ids, testdrive_testdriveveh_ids, testdrive_emp_ids;
	public String testdrive_time_from = strToShortDate(ToShortDate(kknow())), starttime;
	public String testdrive_time_to = strToShortDate(ToShortDate(kknow())), endtime;
	public String branch_id = "";
	public String enquiry_branch_id = "0";
	public String testdrive_emp_id = "0";
	public int colspan = 0;
	public String QueryString = "";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// SOP(emp_id);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (branch_id.equals("0")) {
					colspan = 1;
				} else {
					colspan = 3;
					enquiry_branch_id = branch_id;
				}
				submitB = PadQuotes(request.getParameter("submit_button"));
				// SOP("submitB===========" + submitB);
				if (submitB.equals("Submit")) {
					GetValues(request, response);
					// SOP("1111");
					CheckForm();
					// SOP("22222");
					// SOP("msg=======" + msg);
					if (msg.equals("")) {
						// SOP("aaaaaaa");
						StrHTML = TestDriveCalendar();
						// SOP("3333333");
					} else {
						msg = "Error!" + msg;
					}
				}
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
	
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		enquiry_branch_id = PadQuotes(request.getParameter("dr_branch"));
		item_model_id = RetrunSelectArrVal(request, "dr_model");
		item_model_ids = request.getParameterValues("dr_model");
		testdrive_testdriveveh_id = RetrunSelectArrVal(request, "dr_vehicle");
		testdrive_testdriveveh_ids = request.getParameterValues("dr_vehicle");
		testdrive_emp_id = RetrunSelectArrVal(request, "dr_executive");
		testdrive_emp_ids = request.getParameterValues("dr_executive");
		testdrive_time_from = PadQuotes(request.getParameter("txt_testdrive_time_from"));
		testdrive_time_to = PadQuotes(request.getParameter("txt_testdrive_time_to"));
		if (testdrive_time_from.equals("")) {
			testdrive_time_from = strToShortDate(ToShortDate(kknow()));
		}
		if (testdrive_time_to.equals("")) {
			testdrive_time_to = strToShortDate(ToShortDate(kknow()));
		}
	}
	
	protected void CheckForm() {
		msg = "";
		if (enquiry_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
		if (testdrive_time_from.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!testdrive_time_from.equals("") && !isValidDateFormatShort(testdrive_time_from)) {
			msg = msg + "<br>Enter valid Start date!";
		}
		if (testdrive_time_to.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!testdrive_time_to.equals("") && !isValidDateFormatShort(testdrive_time_to)) {
			msg = msg + "<br>Enter valid End date!";
		}
		if (!testdrive_time_from.equals("")
				&& isValidDateFormatShort(testdrive_time_from)
				&& !testdrive_time_to.equals("")
				&& isValidDateFormatShort(testdrive_time_to)) {
			starttime = ConvertShortDateToStr(testdrive_time_from);
			endtime = ConvertShortDateToStr(testdrive_time_to);
			if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
		// start- to check < 30 days //
		if (getDaysBetween(ConvertShortDateToStr(testdrive_time_from), ConvertShortDateToStr(testdrive_time_to)) > 31) {
			msg = msg + "<br>Test Drive Duration should be less than 1 month!";
		}
		// eof- to check < 30 days //
	}
	
	public String TestDriveCalendar() {
		
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		Date date;
		String today;
		String StrSearch = "", search = "";
		// search = search + ExeAccess.replaceAll("emp_id", "enquiry_emp_id");
		if (!item_model_id.equals("")) {
			search = " and item_model_id in (" + item_model_id + ")";
		}
		if (!testdrive_testdriveveh_id.equals("")) {
			search = " and testdrive_testdriveveh_id in (" + testdrive_testdriveveh_id + ")";
		}
		if (!testdrive_emp_id.equals("")) {
			search = " and testdrive_emp_id in (" + testdrive_emp_id + ")";
		}
		if (!enquiry_branch_id.equals("") && !enquiry_branch_id.equals("0")) {
			search = search + " and enquiry_branch_id = " + enquiry_branch_id + "";
		}
		try {
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<td height=20><b>Days</b></td>\n");
			Str.append("<td><b>Test Drive Details</b></td>\n");
			Str.append("</tr></thead>");
			double days = getDaysBetween(starttime, endtime);
			for (int i = 0; i <= days; i++) {
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(CalDateFormat(starttime, "dd/MM/yyyy")), Double.parseDouble(i + ""), 0.0, 0.0);
				// SOP("date=====" + date);
				StrSearch = " and testdrive_time_from >= " + ToLongDate(date) + "";
				if (isSunday(ToLongDate(date))) {
					Str.append("<tr align=left bgcolor=pink>\n");
				} else {
					Str.append("<tr align=left>\n");
				}
				today = ConvertLongDate(ToLongDate(date));
				String date_str = ToLongDate(date);
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(CalDateFormat(starttime, "dd/MM/yyyy")), Double.parseDouble(i + 1 + ""), 0.0, 0.0);
				StrSearch = StrSearch + " and testdrive_time_from < " + ToLongDate(date) + "";
				StrSql = " SELECT testdrive_id, testdriveveh_name, testdriveveh_regno, contact_fname, branch_code, contact_id, customer_name, testdrive_time, testdrive_time_to, testdrive_time_from,"
						+ " enquiry_no, testdrive_confirmed, testdrive_notes, testdrive_doc_value, customer_id, enquiry_id, emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, "
						+ " location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms"
						+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id= enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_testdriveveh_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = testdriveveh_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
						+ " WHERE 1 = 1  "
						+ BranchAccess
						+ ExeAccess.replaceAll("emp_id", "enquiry_emp_id")
						+ "";
				StrSql = StrSql + search + StrSearch;
				StrSql = StrSql + " GROUP BY testdrive_id ORDER BY testdrive_time_from";
				// SOP("StrSql----------" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				// Str.append("<td valign=top height=20 width=20%>" + today +
				// "<br></td>\n");
				Str.append("<td valign=top width=15%>").append(today).append("<br>");
				
				if (!testdrive_testdriveveh_id.equals("") && isNumeric(testdrive_testdriveveh_id)) {
					Str.append(TestDriveOutage(testdrive_testdriveveh_id, date_str));
				}
				
				Str.append("</td>\n");
				if (crs.isBeforeFirst()) {
					Str.append("<td valign=top><table class=\"table-hover table-responsive\" data-filter=\"#filter\">"
							+ "<tr>");
					while (crs.next()) {
						Str.append("<td valign=top><b>" + PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "2") + "</b>");
						Str.append("<br><a href=\"testdrive-list.jsp?testdrive_id=" + crs.getString("testdrive_id") + "\" target=_blank>Test Drive ID: " + crs.getString("testdrive_id") + "</a>");
						if (crs.getString("testdrive_confirmed").equals("0")) {
							Str.append("<br><font color=red>[Not Confirmed]</font>");
						}
						Str.append("<br>Time: " + SplitHourMin(crs.getString("testdrive_time")));
						Str.append("<br>" + crs.getString("testdriveveh_name") + "");
						Str.append("<br>" + crs.getString("testdriveveh_regno") + "");
						Str.append("<br>" + crs.getString("location_name"));
						Str.append("<br><a href='../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + "'>" + crs.getString("emp_name") + "</a>");
						Str.append("<br><a href='../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "'\"><b>"
								+ crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")</b></a>");
						// Str.append("<br><a href=\"contact-list.jsp?Update=yes&contact_id="
						// + crs.getString("contact_id") + "\" target=_blank>" +
						// crs.getString("contact_fname") + " (" +
						// crs.getString("contact_id") + ")</a>");
						if (!crs.getString("testdrive_out_time").equals("")) {
							Str.append("<br>Out Time: " + SplitHourMin(crs.getString("testdrive_out_time")) + "");
							if (!crs.getString("testdrive_in_time").equals("")) {
								Str.append("<br>In Time: " + SplitHourMin(crs.getString("testdrive_in_time")) + "");
							}
							if (!crs.getString("testdrive_out_time").equals("") && !crs.getString("testdrive_in_time").equals("")) {
								String Hours = deci.format(getHoursBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
								String Mins = deci.format(getMinBetween(StringToDate(crs.getString("testdrive_out_time")), StringToDate(crs.getString("testdrive_in_time"))));
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
						Str.append("<br><a href=\"testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id") + "\" target=_blank>Update Mileage</a>");
						if (!crs.getString("testdrive_notes").equals("")) {
							Str.append("<br>" + crs.getString("testdrive_notes") + "");
						}
						Str.append("<br></td>");
					}
					Str.append("</tr></table></td>");
				} else {
					Str.append("<td valign=top align=center>---</td></tr>");
				}
				crs.close();
			}
			Str.append("</table></div>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name FROM " + compdb(comp_id) + "axela_inventory_item_model ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("model_id"), item_model_ids));
				Str.append(">" + crs.getString("model_name") + "</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	
	public String PopulateVehicle(String comp_id, String item_model_id) {
		String search = "";
		if (!enquiry_branch_id.equals("") && !enquiry_branch_id.equals("0")) {
			search = " and testdriveveh_branch_id=" + enquiry_branch_id;
		}
		if (!item_model_id.equals("")) {
			search = search + " and item_model_id in (" + item_model_id + ")";
		}
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<select name='dr_vehicle' id='dr_vehicle' multiple=\"multiple\" class='form-control multiselect-dropdown' size='10'>");
			
			StrSql = " SELECT testdriveveh_id, testdriveveh_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " WHERE 1 = 1"
					+ " AND testdriveveh_active = 1 "
					+ search
					+ " GROUP BY testdriveveh_id"
					+ " ORDER BY testdriveveh_name ";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("testdriveveh_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("testdriveveh_id"), testdrive_testdriveveh_ids));
				Str.append(">" + crs.getString("testdriveveh_name") + " " + "</option> \n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateExecutive() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_active = '1' "
					+ " AND (emp_branch_id = 0 OR emp_branch_id = " + enquiry_branch_id + ") "
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			// SOP("hello111"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("emp_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), testdrive_emp_ids));
				Str.append(">" + (crs.getString("emp_name")) + "</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String TestDriveOutage(String testdrive_testdriveveh_id, String date_str) {
		try {
			String outage_msg = "";
			StrSql = "SELECT salesgatepass_testdriveveh_id, salesgatepass_fromtime, salesgatepass_totime"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " WHERE salesgatepass_testdriveveh_id = " + testdrive_testdriveveh_id + ""
					+ " AND (substr(salesgatepass_fromtime,1,8) = " + date_str.substring(0, 8) + ""
					+ " OR substr(salesgatepass_totime,1,8) = " + date_str.substring(0, 8) + " "
					+ " OR (substr(salesgatepass_fromtime,1,8) <= " + date_str.substring(0, 8) + ""
					+ " AND substr(salesgatepass_totime,1,8) >= " + date_str.substring(0, 8) + "))";
			// SOP("TestDriveOutage = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					outage_msg = "<b><font color='#ff0000'>Vehicle Outage:<br>" + PeriodTime(crs.getString("salesgatepass_fromtime"), crs.getString("salesgatepass_totime"), "4") + "</font></b>";
				}
			}
			crs.close();
			return outage_msg;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id IN (1, 2)";
			SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	
}
