package axela.customer;
// saiman
// 

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Customer_Birthday extends Connect {

	public String StrSql = "";
	public String starttime = "";
	public String endtime = "";
	public String starttime_day = "", starttime_month;
	public String endtime_day = "", endtime_month;
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String go = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();

				if (go.equals("Go")) {
					// StrSearch = BranchAccess.replaceAll("branch_id", "customer_branch_id")
					// + ExeAccess.replaceAll("emp_id", "customer_emp_id");
					// SOP("msg----" + msg);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListBirthdays();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		starttime_day = PadQuotes(request.getParameter("dr_starttime_day"));
		starttime_month = PadQuotes(request.getParameter("dr_starttime_month"));

		endtime_day = PadQuotes(request.getParameter("dr_endtime_day"));
		endtime_month = PadQuotes(request.getParameter("dr_endtime_month"));

	}

	protected void CheckForm() {
		msg = "";

		if (starttime_day.equals("0")) {
			msg += "<br>Select Start Day";
		}
		if (starttime_month.equals("0")) {
			msg += "<br>Select Start Month";
		}

		if (endtime_day.equals("0")) {
			msg += "<br>Select End Day";
		}
		if (endtime_month.equals("0")) {
			msg += "<br>Select End Month";
		}

		// starttime = SplitYear(ToLongDate(kknow())) + starttime_month + starttime_day;
		//
		// if (!starttime.equals("")) {
		// SOP("strToShortDate=======" + isValidDateFormatStr(strToShortDate(starttime + "000000")));
		// if (!isValidDateFormatStr(strToShortDate(starttime + "000000"))) {
		// msg = msg + "<br>Enter Valid Start Date!";
		// }
		// }
		//
		// endtime = SplitYear(ToLongDate(kknow())) + endtime_month + endtime_day;
		//
		// if (!endtime.equals("")) {
		// SOP("strToShortDate=======" + isValidDateFormatStr(strToShortDate(endtime + "000000")));
		// if (!isValidDateFormatStr(strToShortDate(endtime + "000000"))) {
		// msg = msg + "<br>Enter Valid End Date!";
		// }
		// }
	}

	public String ListBirthdays() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT "
					+ " contact_id,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_dob"
					+ " FROM  " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE 1 = 1 "
					+ " AND SUBSTR(contact_dob, 5, 4) >= " + starttime_month + starttime_day + ""
					+ " AND SUBSTR(contact_dob, 5, 4) <= " + endtime_month + endtime_day + ""
					+ " " + StrSearch
					+ " GROUP BY contact_id"
					+ " ORDER BY contact_dob"
					+ " DESC LIMIT 1000";
			// SOP("ListBirthdays---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Contact</th>\n");
				Str.append("<th>D.O.B</th>\n");
				Str.append("<th>Mobile 1</th>\n");
				Str.append("<th>Mobile 2</th>\n");
				Str.append("<th>Email 1</th>\n");
				Str.append("<th>Email 2</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td><a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id"))
							.append(">").append(crs.getString("contact_name")).append("</a></td>\n");
					Str.append("<td align=center>").append(strToShortDate(crs.getString("contact_dob"))).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("contact_mobile1")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("contact_mobile2")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("contact_email1")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("contact_email2")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<center><br><br><font color=red><b>No Birthday's Found!</b></font><br><br></center>");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// This method is called for both start and end day only parameter will change while calling in JSP
	public String PopulateDay(String dr_day) {
		String day = "<option value=0>Days</option>\n";
		for (int i = 1; i <= 31; i++) {
			day += "<option value = " + doublenum(i) + "" + StrSelectdrop(doublenum(i), dr_day) + ">" + i + "</option>\n";
		}
		return day;
	}

	// This method is called for both start and end month only parameter will change while calling in JSP
	public String PopulateMonth(String dr_month) {
		String months = "<option value=0>Months</option>\n";
		months += "<option value=01" + StrSelectdrop(doublenum(1), dr_month) + ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dr_month) + ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dr_month) + ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dr_month) + ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dr_month) + ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dr_month) + ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dr_month) + ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dr_month) + ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dr_month) + ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dr_month) + ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dr_month) + ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dr_month) + ">December</option>\n";
		return months;
	}
}
