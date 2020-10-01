package axela.sales;
//Saiman 13th Dec 2012
import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_Daily extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String dropDueMon = "";
	public String dropDueYear = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String comp_id = "0";
	public String ExeAccess = "";
	public String enquiry_hour = "";
	public String[] x = new String[32];
	public String m = "";
	public String enquiry_count = "";
	public String invoice_grandtotal = "";
	public String receipt_amount = "";
	public String StrSearch = "";
	public String chart_data = "";
	public int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static int year;
	public static int month;
	public static int maxdays;
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
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + " " + ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and branch_id =" + dr_branch_id;
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (select teamtrans_emp_id "
								+ " from " + compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id in (" + team_id + "))";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " and enquiry_model_id in (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
				}

				if (msg.equals("")) {
					ListTarget();
				}
			}
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SOP("inside get values");
		dropDueMon = PadQuotes(request.getParameter("drop_month"));
		dropDueYear = PadQuotes(request.getParameter("drop_year"));
		if (dropDueMon.equals("")) {
			dropDueMon = ToShortDate(kknow()).substring(4, 6);
		}
		month = (Integer.parseInt(dropDueMon));
		SOP("Month----------" + month);
		if (dropDueYear.equals("")) {
			dropDueYear = ToShortDate(kknow()).substring(0, 4);
		}
		year = (Integer.parseInt(dropDueYear));
		SOP("Year--------" + year);
		// if (dropDueMon.equals("-1")) {
		// dropDueMon = TextMonth(Integer.parseInt(SplitMonth(strToShortDate(ToShortDate(kknow())))));
		// SOP("dropDueMon---------"+dropDueMon);
		//
		// }
		// if (dropDueYear.equals("-1")) {
		// dropDueYear = SplitYear(strToShortDate(ToShortDate(kknow())));
		// SOP("dropDueYear---------"+dropDueYear+ "---------"+SplitYear(strToShortDate(ToShortDate(kknow()))));
		//
		// }

		// for (int i = 0; i < 24; i++) {
		// x[i] =ToLongDate(AddHoursDate(StringToDate(ConvertShortDateToStr(endtime)), 0, i, 0));
		//
		// }
		//

		for (int i = 1; i <= daysInMonth(); i++)
		{
			if ((i + "").length() == 1) {
				x[i] = dropDueYear + dropDueMon + "0" + i + "000000";
			} else {
				x[i] = dropDueYear + dropDueMon + i + "000000";
			}
			SOP("x[i]---------" + x[i]);
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		SOP("dr_branch_id===" + dr_branch_id);
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}

	// public void CheckMonth()
	// {
	// int [] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	// for(int i=0; i<arr[Integer.parseInt(dropDueMon)-1]; i++)
	// {
	// m = ToLongDate(AddHoursDate(StringToDate(ConvertShortDateToStr(dropDueMon)), 0, i, 0));
	// }
	// }
	protected void CheckForm() {
		msg = "";
		SOP("inside ckeckform----------");
		SOP("dropDueYear---" + dropDueYear);
		if (dropDueMon.equals("-1")) {
			SOP("dropDueMon------" + dropDueMon);
			// dropDueMon = dropDueYear+dropDueMon+"0"+"000000";
			SOP("dropDueMon---------" + dropDueMon);

		}
		if ((dropDueMon.equals("")) && (dropDueYear.equals("")))
		{
			msg += "<br>Invalid Month Or Year!";
		}
		if ((dropDueMon.equals("-1")) && (!dropDueYear.equals("-1")))
		{
			msg += "<br>Enter Month!";
		}
		// if ((dropDueMon.equals("-1")) && (dropDueYear.equals("-1")))
		// {
		// msg += "<br>Enter Year!";
		// }
		//
		// if (endtime.equals("")) {
		// msg = msg + "<br>Select Date!<br>";
		// }
		// if (!endtime.equals("")) {
		// if (isValidDateFormatShort(endtime)) {
		// endtime = ConvertShortDateToStr(endtime);
		// end_time = strToShortDate(endtime);
		// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
		// } else {
		// msg = msg + "<br>Enter Valid Date!";
		// endtime = "";
		// }
		// }
	}

	public void ListTarget() {
		SOP("inside ListTarget");
		StrSql = " select "
				+ " concat(DAY(calday)) as day, "
				+ " COALESCE((select count(enquiry_id)"
				+ " from " + compdb(comp_id) + "axela_sales_enquiry"
				+ " inner JOIN " + compdb(comp_id) + "axela_branch  on branch_id = enquiry_branch_id"
				+ " inner JOIN " + compdb(comp_id) + "axela_emp  on emp_id=enquiry_emp_id "
				// + " where CONCAT(substr(enquiry_date,1,8),substr(enquiry_entry_date,9,2))=substr(cal.calday,1,10) "+StrSearch+" ),0) as enquiry"
				+ " where enquiry_date=cal.calday), 0) as enquiry"
				+ " from (";
		for (int i = 1; i <= daysInMonth(); i++) {
			StrSql = StrSql + " select " + x[i] + " as calday ";
			if (i != x.length - 1) {
				StrSql = StrSql + " UNION ";
			}
		}
		StrSql = StrSql + " ) as cal"
				+ " group by calday"
				+ " order by calday";

		SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				chart_data = "[";
				while (crs.next()) {
					enquiry_count = crs.getString("enquiry") + "";
					enquiry_hour = "'" + crs.getString("time") + "'";
					chart_data = chart_data + "{'hour': " + enquiry_hour + ", 'column-1':'" + enquiry_count + "'}";
					if (!crs.isLast()) {
						chart_data = chart_data + ",";
					}
				}
				chart_data = chart_data + "]";
			} else {
				// NoChart = "<font color=red><b>No Data Found!</b></font>";
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " group by team_id "
					+ " order by team_name ";
			// SOP("PopulateTeam query ==== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("team_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">" + (crs.getString("team_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public static int daysInMonth()
	{
		int date = 0;
		GregorianCalendar Calendar = new GregorianCalendar(year, month, year);
		Calendar.set(year, month, date);
		// Calendar.set(year, year);
		maxdays = Calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxdays;
		// int [] daysinmonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		// daysinmonth[1] += c.isLeapYear(c.get(GregorianCalendar.YEAR)) ? 1:0;
		// maxdays = daysinmonth(c.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
	}

	// public String PopulatesNumberOfDays(int month1, int year2, GregorianCalendar c)
	// {
	// SOP("Month----------"+month);
	// SOP("Year----------"+year);
	// String days = "";
	// PopulateLeapYear(month,year);
	// for(int i=1; i<=arr[Integer.parseInt(dropDueMon)-1]; i++)
	// {
	// if((i+"").length() == 1){
	// x[i] = dropDueYear+dropDueMon+"0"+i+"000000";
	// } else {
	// x[i] = dropDueYear+dropDueMon+i+"000000";
	// }
	// SOP(x[i]);
	// }
	// return dropDueMon;
	// }
	//
	// public boolean PopulateLeapYear(int month, int year)
	// {
	// int numberOfDays;
	// if(month==4 || month==6 || month==9 || month==11)
	// {
	// numberOfDays=30;
	// }
	// else if(month==2)
	// {
	// if((year %4==0 || year %400==0) && (year % 100!=0))
	// {
	// numberOfDays = 29;
	// }
	// else
	// numberOfDays = 28;
	// }
	// else
	// numberOfDays=31;
	// return true;
	// }

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			String exe = "";
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' and emp_sales='1' and "
					+ " (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id order by emp_name";
			// SOP("StrSql===="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=selectbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return exe = Str.toString();
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			String SqlStr = "select model_id, model_name "
					+ " from " + compdb(comp_id) + "axela_inventory_item_model "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id = model_id"
					+ " group by model_id order by model_name";
			CachedRowSet crs = processQuery(SqlStr, 0);
			// SOP("SqlStr in PopulateCountry==========" + SqlStr);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateYears() {

		String days = "<option value=\"-1\">--</option>\n";
		for (int i = year - 2; i <= year; i++) {
			days += "<option value = " + doublenum(i) + "" + StrSelectdrop(doublenum(i), dropDueYear) + ">" + i + "</option>\n";

		}
		return days;
	}
	public String PopulateMonths() {
		String months = "<option value=-1>--</option>\n";
		months += "<option value=01" + StrSelectdrop(doublenum(1), dropDueMon) + ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dropDueMon) + ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dropDueMon) + ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dropDueMon) + ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dropDueMon) + ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dropDueMon) + ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dropDueMon) + ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dropDueMon) + ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dropDueMon) + ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dropDueMon) + ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dropDueMon) + ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dropDueMon) + ">December</option>\n";
		return months;
	}
}
