// smitha nag 5 july 2013
package axela.preowned;

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

public class Preowned_TestDrive_Cal extends Connect {

	public String add = "", StrHTML = "", emp_id = "";
	public String submitB = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";
	public String model_id = "", testdrive_preownedstock_id = "";
	public String[] model_ids, testdrive_emp_ids, testdrive_preownedstock_ids;
	public String testdrive_time_from = strToShortDate(ToShortDate(kknow())), starttime;
	public String testdrive_time_to = strToShortDate(ToShortDate(kknow())), endtime;
	public String branch_id = "";
	public String enquiry_branch_id = "0";
	public String testdrive_emp_id = "";
	public int colspan = 0;
	public String QueryString = "";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_preowned_testdrive_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (branch_id.equals("0")) {
					enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				} else {
					enquiry_branch_id = branch_id;
				}
				submitB = PadQuotes(request.getParameter("submit_button"));
				if (submitB.equals("Submit")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = TestDriveCalendar();
					} else {
						msg = "Error!" + msg;
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		testdrive_preownedstock_id = RetrunSelectArrVal(request, "dr_preownedstock_id");
		// SOP("testdrive_preownedstock_id-------" + testdrive_preownedstock_id);
		testdrive_preownedstock_ids = request.getParameterValues("dr_preownedstock_id");
		testdrive_emp_id = RetrunSelectArrVal(request, "dr_executive");
		// SOP("testdrive_emp_id----------" + testdrive_emp_id);
		testdrive_emp_ids = request.getParameterValues("dr_executive");
		// SOP("testdrive_emp_ids----------" + testdrive_emp_ids);
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
		if (getDaysBetween(ConvertShortDateToStr(testdrive_time_from), ConvertShortDateToStr(testdrive_time_to)) > 31) {
			msg = msg + "<br>Test Drive Duration should be less than 1 month!";
		}
	}

	public String TestDriveCalendar() {

		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		Date date;
		String today;
		String StrSearch = "", search = "";
		if (!model_id.equals("")) {
			search += " AND variant_preownedmodel_id in (" + model_id + ")";
		}
		if (!testdrive_preownedstock_id.equals("")) {
			search += " AND testdrive_preownedstock_id IN (" + testdrive_preownedstock_id + ")";
		}
		if (!testdrive_emp_id.equals("")) {
			search += " AND testdrive_emp_id IN (" + testdrive_emp_id + ")";
		}
		if (!enquiry_branch_id.equals("")) {
			search += " AND enquiry_branch_id IN (" + enquiry_branch_id + ")";
		}
		// SOP("search---------" + search);
		try {
			Str.append("<div class=\"table-bordered\">\n");
			Str.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
			Str.append("<tr>\n");
			Str.append("<td height=20><b>Days</b></td>\n");
			Str.append("<td><b>Test Drive Details</b></td>\n");
			Str.append("</tr>");
			double days = getDaysBetween(starttime, endtime);
			for (int i = 0; i <= days; i++) {
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + ""), 0.0, 0.0);
				StrSearch = " and testdrive_time_from >= " + ToLongDate(date) + "";
				if (isSunday(ToLongDate(date))) {
					Str.append("<tr align=left bgcolor=pink>\n");
				} else {
					Str.append("<tr align=left>\n");
				}
				today = ConvertLongDate(ToLongDate(date));
				String date_str = ToLongDate(date);
				date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + 1 + ""), 0.0, 0.0);
				StrSearch = StrSearch + " and testdrive_time_from < " + ToLongDate(date) + "";
				StrSql = "select testdrive_id, variant_name, preowned_regno, contact_fname, branch_code,"
						+ " contact_id, customer_name, testdrive_time, testdrive_time_to, testdrive_time_from, enquiry_no,"
						+ " testdrive_confirmed, testdrive_notes, testdrive_doc_value, customer_id, enquiry_id, emp_id,"
						+ " concat(emp_name,' (', emp_ref_no, ')') as emp_name,  location_name,"
						+ " testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms"
						+ " from " + compdb(comp_id) + "axela_preowned_testdrive"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id"
						+ " WHERE 1 = 1 AND enquiry_enquirytype_id = 2 " + BranchAccess;
				StrSql = StrSql + search + StrSearch;
				StrSql = StrSql + " GROUP BY testdrive_id"
						+ " ORDER BY testdrive_time_from";
				// SOP("StrSql ----------- " + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<td valign=top width=15%>").append(today).append("<br>");
				Str.append("</td>\n");
				if (crs.isBeforeFirst()) {
					Str.append("<td valign=top><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">"
							+ "<tr>");
					while (crs.next()) {
						Str.append("<td valign=top><b>" + PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "2") + "</b>");
						Str.append("<br><a href=\"preowned-testdrive-list.jsp?testdrive_id=" + crs.getString("testdrive_id") + "\" target=_blank>Test Drive ID: " + crs.getString("testdrive_id")
								+ "</a>");
						if (crs.getString("testdrive_confirmed").equals("0")) {
							Str.append("<br><font color=red>[Not Confirmed]</font>");
						}
						Str.append("<br>Time: ").append(SplitHourMin(crs.getString("testdrive_time")));
						Str.append("<br>" + crs.getString("variant_name") + "");
						if (!crs.getString("preowned_regno").equals("")) {
							Str.append(" - ").append(crs.getString("preowned_regno")).append("");
						}

						Str.append("<br>").append(crs.getString("location_name"));
						Str.append("<br><a href=../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + ">" + crs.getString("emp_name") + "</a>");
						Str.append("<br><a href=../sales/enquiry-list.jsp?enquiry_id=" + crs.getString("enquiry_id") + ">Enquiry ID: " + crs.getString("enquiry_id") + "</a>");
						Str.append("<br><a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + ">" + crs.getString("customer_name") + " (" + crs.getString("customer_id")
								+ ")</a>");
						// Str.append("<br><a href=\"contact-list.jsp?Update=yes&contact_id=" + crs.getString("contact_id") + "\" target=_blank>" + crs.getString("contact_fname") + " (" +
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
					Str.append("</tr></table></div></td>");
				} else {
					Str.append("<td valign=top align=center>---</td></tr>");
				}
				crs.close();
			}
			Str.append("</table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">" + crs.getString("model_name") + "</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateVariant() {
		String search = "";
		if (!model_id.equals("")) {
			search = search + " AND variant_preownedmodel_id IN (" + model_id + ")";
		}
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<select name=dr_preownedstock_id id=dr_preownedstock_id class='form-control multiselect-dropdown' multiple=multiple size=10 >");
			StrSql = "SELECT preownedstock_id, variant_name, preowned_regno "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preowned_id  = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " WHERE preowned_branch_id = " + enquiry_branch_id + search
					+ " ORDER BY variant_name";
			// SOP("StrSql ------PopulateVariant-----= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstock_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("preownedstock_id"), testdrive_preownedstock_ids)).append(">");
				if (!crs.getString("preowned_regno").equals("")) {
					Str.append(crs.getString("variant_name")).append("-").append(crs.getString("preowned_regno"));
				} else {
					Str.append(crs.getString("variant_name"));
				}

				Str.append("</option> \n");
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

	public String PopulateExecutive(String exe_branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ " AND emp_preowned = 1"
					+ " AND (emp_branch_id=0 OR emp_branch_id = " + exe_branch_id + ") "
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			// SOP("StrSql----------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\"  >");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), testdrive_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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
}
