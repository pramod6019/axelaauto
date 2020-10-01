// Ved Prakash (11 Feb 2013)
package axela.portal;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Branch_List extends Connect {

	public String LinkHeader = "<a href=home.jsp>Home</a>"
			+ " &gt; <a href=\"manager.jsp\">Business Manager</a>"
			+ " &gt; <a href=branch-list.jsp?all=yes>List Branches</a><b>:</b>";
	public String LinkExportPage = "branch.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=branch-update.jsp?add=yes>Add Branch...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String branch_id = "0";
	public String emp_branch_id = "";
	public String emp_idsession = "0";
	public String branch_name = "";
	public String franchisee_id = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Branch Code", "text", "branch_code"},
			{"Branch Vat", "text", "branch_vat"},
			{"Branch Cst", "text", "branch_cst"},
			{"Branch Pan", "text", "branch_pan"},
			{"Branch Type", "text", "branchtype_name"},
			{"Brand", "text", "brand_name"},
			{"Branch Phone1", "text", "branch_phone1"},
			{"Branch Phone2", "text", "branch_phone2"},
			{"Branch Mobile1", "text", "branch_mobile1"},
			{"Branch Mobile2", "text", "branch_mobile2"},
			{"Branch Email1", "text", "branch_email1"},
			{"Branch Email2", "text", "branch_email2"},
			{"Branch Address", "text", "branch_add"},
			{"Branch Pin", "text", "branch_pin"},
			{"Active", "boolean", "branch_active"},
			{"Branch Notes", "text", "branch_notes"},
			{"Entry By", "text", "branch_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "branch_entry_date"},
			{"Modified By", "text", "branch_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "branch_modified_date"}
	};
	// duetime variables
	String item[];

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_idsession = session.getAttribute("emp_id").toString();
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id, emp_executive_access", request, response);
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				franchisee_id = CNumeric(PadQuotes(request.getParameter("franchisee_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				// Date starttime = new ;
				// Calendar time1 = Calendar.getInstance();
				// getHoursBetween(12-03-2012, 12-03-2012 );
				// int workingHours = 9;
				int duehours = 24;
				// DueTime("20130316150000", 9, 18, duehours, "1", "0", "1",
				// "0", "0", "0", "0");
				// String smitha = CalDueTime("20130315210000", 9, 18, duehours,
				// "1", "1");
				// SOP("=====smitha===== "+smitha);
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND branch_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Branches!";
					StrSearch = StrSearch + " and branch_id > 0";
				} else if (!(branch_id.equals("0"))) {
					msg = msg + "<br>Results for Branch = " + branch_id + "!";
					StrSearch = StrSearch + " and branch_id =" + branch_id + "";
				} else if (!(franchisee_id.equals("0"))) {
					msg = msg + "<br>Results for Branch Code =" + franchisee_id + "!";
					StrSearch = StrSearch + " and branch_franchisee_id =" + franchisee_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND branch_id = 0";
					} else {
						msg = "Result for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("centerstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("centerstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("centerstrsql", StrSearch, request);
					SetSession("centerPrintSearchStr", StrSearch, request);
					SetSession("centerFilterStr", StrSearch, request);
				}
				StrHTML = Listdata();
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

	// public void DueTime(String starttime, double fromtime, double totime,
	// double duehours, String mon,
	// String tue, String wed, String thu, String fri, String sat, String sun) {
	// // from time
	// int days_a = 1;
	// int x =0;
	// Date mydate;
	// String str_fromtime = Double.toString(fromtime);
	// SOP("str_fromtime......" + str_fromtime);
	// String split_fromtime[] = str_fromtime.split("\\.");
	// SOP("split_fromtime[0]......" + split_fromtime[0]);
	// SOP("split_fromtime[1]......" + split_fromtime[1]);
	//
	// if (split_fromtime[0].length() < 2) {
	// split_fromtime[0] = "0" + split_fromtime[0];
	// SOP("0+split_fromtime[0]......" + split_fromtime[0] + "");
	// }
	// if (split_fromtime[1].length() < 2) {
	// split_fromtime[1] = split_fromtime[1] + "0";
	// SOP("split_totime[1]+0......" + split_fromtime[1] + "");
	// }
	// SOP("fromtime......" + fromtime);
	// String date_fromtime = "20131210" + split_fromtime[0] + split_fromtime[1]
	// + "00";
	// SOP("date_fromtime......" + date_fromtime);
	//
	// // to time
	// String str_totime = Double.toString(totime);
	// SOP("str_totime......" + str_totime);
	// String split_totime[] = str_totime.split("\\.");
	// SOP("split_totime[0]......" + split_totime[0]);
	// SOP("split_totime[1]......" + split_totime[1]);
	//
	// if (split_totime[0].length() < 2) {
	// split_totime[0] = "0" + split_totime[0];
	// SOP("0+split_totime[0]......" + split_totime[0] + "");
	// }
	// if (split_totime[1].length() < 2) {
	// split_totime[1] = split_totime[1] + "0";
	// SOP("split_totime[1]+0......" + split_totime[1] + "");
	// }
	// SOP("totime......" + totime);
	// String date_totime = "20131210" + split_totime[0] + split_totime[1] +
	// "00";
	// SOP("date_totime......" + date_totime);
	//
	// if (Double.parseDouble(starttime.substring(8, 10) + "." +
	// starttime.substring(10, 12)) < (Double.parseDouble(split_fromtime[0] +
	// "." + split_fromtime[1]))) {
	// starttime = starttime.substring(0, 8) + split_fromtime[0] +
	// split_fromtime[1] + "00";
	// SOP("for early time......" + starttime + "");
	// } else if (Double.parseDouble(starttime.substring(8, 10) + "." +
	// starttime.substring(10, 12))
	// > (Double.parseDouble(split_totime[0] + "." + split_totime[1]))) {
	//
	// /*Checking the Start time After The to time*/
	//
	// mydate = AddHoursDate(StringToDate(starttime), 1, 0, 0);
	// SOP("mydate......" + mydate + "");
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP("x......" + x + "");
	//
	// CheckWorkingDays( mydate, days_a, x, sun, mon, tue, wed, thu, fri, sat);
	//
	// SOP("days_a......" + days_a + "");
	// starttime = ToLongDate(mydate);
	// starttime = starttime.substring(0, 8) + split_fromtime[0] +
	// split_fromtime[1] + "00";
	// // starttime = ToLongDate(AddHoursDate(StringToDate(starttime), days_a,
	// 0, 0));
	// // starttime = starttime.substring(0, 8) + split_fromtime[0] +
	// split_fromtime[1] + "00";
	// SOP("for late time......" + starttime + "");
	// }
	//
	// double workingHours = getHoursBetween(StringToDate(date_fromtime),
	// StringToDate(date_totime));
	// SOP("workingHours......" + workingHours);
	// int min_wh = (int) (workingHours * 60);
	// int min_dh = (int) ((double) duehours * 60);
	// int days = min_dh / min_wh;
	// int days_add = days;
	// Date added_date = StringToDate(starttime);
	// for (int i = 1; i <= days_add; i++) {
	// SOP("''''''''xxxxxxxxxx''''''''................"+x);
	// // starttime = "20130317090000";
	// added_date = AddHoursDate(added_date, 1, 0, 0);
	// SOP("2nd block added_date......" + added_date + "");
	// int y = ReturnDayOfWeek(ToLongDate(added_date));
	// SOP("2nd block days_add......" + days_add + "");
	// // if(sun.equals(0) || mon.equals(0) || tue.equals(0) || wed.equals(0) ||
	// thu.equals(0)
	// // || fri.equals(0) || sat.equals(0)){
	//
	// if (y == 1) {
	// days_add = days_add + Integer.parseInt(sun);
	// SOP("111111111111111111......" + days_add + "");
	// } else if (y == 2) {
	// days_add = days_add + Integer.parseInt(mon);
	// SOP("22222222222222222......" + days_add + "");
	// } else if (y == 3) {
	// days_add = days_add + Integer.parseInt(tue);
	// SOP("333333333333333333......" + days_add + "");
	// } else if (y == 4) {
	// days_add = days_add + Integer.parseInt(wed);
	// SOP("44444444444444444......" + days_add + "");
	// } else if (y == 5) {
	// days_add = days_add + Integer.parseInt(thu);
	// SOP("55555555555555555......" + days_add + "");
	// } else if (y == 6) {
	// days_add = days_add + Integer.parseInt(fri);
	// SOP("66666666666666666......" + days_add + "");
	// } else if (y == 7) {
	// days_add = days_add + Integer.parseInt(sat);
	// SOP("77777777777777777......" + days_add + "");
	// }
	// // }
	// }
	// int minutes = min_dh % min_wh;
	// SOP("days......" + days);
	// SOP("minutes......" + minutes);
	// SOP("workingHours......" + workingHours);
	// SOP("starttime......" + starttime);
	// // Date new_date = AddHoursDate(StringToDate(starttime), days_add, 0,
	// minutes);
	// Date new_date = AddHoursDate(StringToDate(starttime), days_add, 0, 0);
	// String hourtime = ToLongDate(new_date);
	// hourtime = hourtime.substring(0, 8) + split_totime[0] + split_totime[1] +
	// "00";
	// double add_hrs = getHoursBetween(new_date, StringToDate(hourtime) );
	// add_hrs = add_hrs*60;
	// new_date = AddHoursDate(new_date, 0, 0, add_hrs);
	//
	// SOP("due_date is......" + new_date);
	// SOP("hourtime is......" + hourtime);
	// SOP("add_hrs is......" + add_hrs +"newly  smitha......." +new_date);
	// Date newd = CheckWorkingDays( new_date, days_a, x, sun, mon, tue, wed,
	// thu, fri, sat);
	// SOP("newd is......" + newd);
	//
	// }
	//
	// public Date CheckWorkingDays(Date mydate, int days_a, int x, String sun,
	// String mon, String tue, String wed,
	// String thu, String fri, String sat) {
	// int ddd = 1;
	//
	// if (x == 1) {
	// if (sun.equals(1)) {
	// SOP("inside x==1......" + x);
	// days_a = days_a + Integer.parseInt(sun);
	// if (days_a != ddd) {
	// if (mon.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 11111......" + x);
	// }
	// }
	// SOP("days_a for x==1......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate11111......" + mydate);
	// }
	// }
	// if (x == 2) {
	// if (mon.equals(1)) {
	// SOP("inside x==2......" + x);
	// days_a = days_a + Integer.parseInt(mon);
	// if (days_a != ddd) {
	// if (tue.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 22222......" + x);
	// }
	// }
	// SOP("days_a for x==2......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate2222222......" + mydate);
	// }
	// }
	// if (x == 3) {
	// if (tue.equals(1)) {
	// SOP("inside x==3......" + x);
	// days_a = days_a + Integer.parseInt(tue);
	// if (days_a != ddd) {
	// if (wed.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 3333......" + x);
	// }
	// }
	// SOP("days_a for x==3......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate333333......" + mydate);
	// }
	// }
	// if (x == 4) {
	// if (wed.equals(1)) {
	// SOP("inside x==4......" + x);
	// days_a = days_a + Integer.parseInt(wed);
	// if (days_a != ddd) {
	// if (thu.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 44444......" + x);
	// }
	// }
	// SOP("days_a for x==4......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate44444......" + mydate);
	// }
	// }
	// if (x == 5) {
	// if (thu.equals(1)) {
	// SOP("inside x==5......" + x);
	// days_a = days_a + Integer.parseInt(thu);
	// if (days_a != ddd) {
	// if (fri.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 5555......" + x);
	// }
	// }
	// SOP("days_a for x==5......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate555555......" + mydate);
	// }
	// }
	// if (x == 6) {
	// if (fri.equals(1)) {
	// SOP("inside x==6......" + x);
	// days_a = days_a + Integer.parseInt(fri);
	// if (days_a != ddd) {
	// if (sat.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  X calculated 6666......" + x);
	// }
	// }
	// SOP("days_a for x==6......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// SOP(";;;;;;;;;;;;;;;  mydate77777......" + mydate);
	// }
	// }
	// if (x == 7) {
	// // if (sat.equals(1)) {
	// SOP("inside x==7......" + x);
	// days_a = days_a + Integer.parseInt(sat);
	// if (days_a != ddd) {
	// if (sun.equals(1)) {
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// CheckHolidays(mydate, days_a, x, sun, mon, tue, wed, thu, fri);
	// SOP(";;;;;;;;;;;;;;;  X calculated 7777......" + x);
	// }
	// }
	// SOP("days_a for x==7......" + days_a);
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	//
	// SOP(";;;;;;;;;;;;;;;  mydate77777......" + mydate);
	// // }
	// }
	// return mydate;
	//
	// }
	//
	// public void CheckNext(int days_add, int x, String sun, String mon, String
	// tue, String wed, String thu, String fri) {
	// if (x == 1) {
	// days_add = days_add + Integer.parseInt(sun);
	// SOP("111111111111111111......" + days_add + "");
	// } else if (x == 2) {
	// days_add = days_add + Integer.parseInt(mon);
	// SOP("22222222222222222......" + days_add + "");
	// } else if (x == 3) {
	// days_add = days_add + Integer.parseInt(tue);
	// SOP("333333333333333333......" + days_add + "");
	// } else if (x == 4) {
	// days_add = days_add + Integer.parseInt(wed);
	// SOP("44444444444444444......" + days_add + "");
	// } else if (x == 5) {
	// days_add = days_add + Integer.parseInt(thu);
	// SOP("55555555555555555......" + days_add + "");
	// } else if (x == 6) {
	// days_add = days_add + Integer.parseInt(fri);
	// SOP("66666666666666666......" + days_add + "");
	// }
	// }
	//
	// public void CheckHolidays(Date mydate, int days_a, int x, String sun,
	// String mon, String tue, String wed, String thu, String fri) {
	//
	// if (x == 1) {
	// SOP("inside x==1......" + x);
	// // if(sun.equals(1)){
	// days_a = days_a + Integer.parseInt(sun);
	// SOP("days_a for x==1......" + days_a);
	// // x = ReturnDayOfWeek(ToLongDate(AddHoursDate(StringToDate(starttime),
	// 1, 0, 0)));
	// // SOP(";;;;;;;;;;;;;;;  X calculated next time......" + x);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate1111111......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated in 1111......" + x);
	// }
	// // x=2;
	// if (x == 2) {
	// // if(mon.equals(1)){
	// SOP("inside x==2......" + x);
	// days_a = days_a + Integer.parseInt(mon);
	// SOP("days_a for x==2......" + days_a);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate222222......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated 2222......" + x);
	// //
	// }
	// // x=3;
	// if (x == 3) {
	// // if(tue.equals(1)){
	// SOP("inside x==3......" + x);
	// days_a = days_a + Integer.parseInt(tue);
	// SOP("days_a for x==3......" + days_a);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate3333333......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated 3333......" + x);
	// }
	// // x=4;
	// if (x == 4) {
	// // if(wed.equals(1)){
	// SOP("inside x==4......" + x);
	// days_a = days_a + Integer.parseInt(wed);
	// SOP("days_a for x==4......" + days_a);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate444444......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated 4444......" + x);
	// }
	// // x=5;
	// if (x == 5) {
	// // if(thu.equals(1)){
	// SOP("inside x==5......" + x);
	// days_a = days_a + Integer.parseInt(thu);
	// SOP("days_a for x==5......" + days_a);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate55555......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated 55555......" + x);
	// }
	// //x=6;
	// if (x == 6) {
	// // if(fri.equals(1)){
	// SOP("inside x==6......" + x);
	// days_a = days_a + Integer.parseInt(fri);
	// SOP("days_a for x==6......" + days_a);
	// // }
	// mydate = AddHoursDate(mydate, 1, 0, 0);
	// x = ReturnDayOfWeek(ToLongDate(mydate));
	// SOP(";;;;;;;;;;;;;;;  mydate6666......" + mydate);
	// SOP(";;;;;;;;;;;;;;;  X calculated 66666......" + x);
	// }
	//
	// }
	//
	// public String DateCheck(String xi, String sat, String sun) {
	// int isHoliday = 0;
	// String starttime = xi;
	// int x = ReturnDayOfWeek(ToLongDate(AddHoursDate(StringToDate(xi), 1, 0,
	// 0)));
	// if (x == 1) {
	// xi = ToLongDate(AddHoursDate(StringToDate(xi), 1, 0, 0));
	// SOP("inside datecheck is.xi1....." + xi);
	//
	// DateCheck(xi, "1", "1");
	// // isHoliday = isHoliday;
	// } else if (x == 7) {
	// xi = ToLongDate(AddHoursDate(StringToDate(xi), 1, 0, 0));
	// SOP("inside datecheck is.xi2....." + xi);
	// DateCheck(xi, "1", "1");
	// // isHoliday = isHoliday;
	// }
	// // if (x == 1 || x == 7) {
	// // DateCheck(xi,"1","1");
	// // }
	// SOP("inside datecheck is.xi23....." + xi);
	// return xi;
	// }
	public String Listdata() {
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		int PageListSize = 10;
		int StartRec = 0;
		String Img = "";
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			StrSql = "SELECT branch_id, branch_logo, branch_name, brand_name, branch_active, franchiseetype_id,"
					+ " branch_email1, branch_email2, branch_phone1, branch_phone2,"
					+ " branch_mobile1, branch_mobile2, rateclass_name, branch_add, branch_add,branchtype_name, "
					+ " city_name, branch_pin, franchisee_name, franchisee_id, branch_notes";

			StrJoin = " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN axela_brand on brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_franchisee on branch_franchisee_id = franchisee_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_franchisee_type on franchisee_franchiseetype_id = franchiseetype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = branch_city_id"
					+ " LEFT JOIN axela_branch_type on branch_branchtype_id = branchtype_id"
					+ " where 1=1";

			CountSql = "SELECT Count(distinct branch_id)";

			StrSql = StrSql + StrJoin;
			CountSql = CountSql + StrJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Branch(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "branch-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				if (all.equals("yes")) {
					StrSql = StrSql + " order by branch_id desc";
				} else {
					StrSql = StrSql + "  order by branch_id ";
				}
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
				// SOP("StrSql----------branch-lis-----------" +
				// StrSqlBreaker(StrSql));
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					String active = "";

					Str.append("<div class=\" table-bordered\">\n");
					Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\"> #</th>\n");
					Str.append("<th data-toggle=\"true\">Branch ID</th>\n");
					Str.append("<th>Branch Details</th>\n");
					Str.append("<th>Brand</th>\n");
					Str.append("<th data-hide=\"phone\">Contacts</th>\n");
					Str.append("<th data-hide=\"phone\">Address</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Notes</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						Date d = new Date();
						if (crs.getString("branch_logo").equals("")) {
							Img = "";
						} else {
							Img = "<img src=\"../Thumbnail.do?branchlogo=" + crs.getString("branch_logo")
									+ "&width=200&time=" + d.getTime() + "&target=" + Math.random()
									+ "&dummy=84456663 alt=" + crs.getString("branch_name") + "\"><br>";
						}
						count = count + 1;
						if (crs.getString("branch_active").equals("0")) {
							active = "<br><font color=red > [Inactive] </font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("branch_id")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(Img).append(crs.getString("branch_name"))
								.append(active)
								.append("<br>Type: ").append(crs.getString("branchtype_name"))
								.append("<br>Franchisee:  ")
								.append(" <a href=franchisee-list.jsp?franchisee_id=");
						Str.append(crs.getString("franchisee_id")).append(">").append(crs.getString("franchisee_name")).append(" (").append(crs.getString("franchisee_id")).append(")</a>");
						Str.append("<br>Class: ").append(crs.getString("rateclass_name"));
						Str.append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("brand_name")).append("</td>");
						Str.append("<td valign=top align=left nowrap>");

						Str.append(SplitPhoneNo(crs.getString("branch_phone1"), 4, "T")).append("<br>");

						if (!crs.getString("branch_phone2").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("branch_phone2"), 4, "T")).append("<br>");
						}

						Str.append(SplitPhoneNo(crs.getString("branch_mobile1"), 5, "M")).append("<br>");

						if (!crs.getString("branch_mobile2").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("branch_mobile2"), 5, "M")).append("<br>");
						}
						Str.append("<a href=mailto:").append(crs.getString("branch_email1")).append(">").append(crs.getString("branch_email1")).append("</a><br>");
						if (!crs.getString("branch_email2").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("branch_email2")).append(">").append(crs.getString("branch_email2")).append("</a>");
						}
						Str.append("</td><td valign=top align=left>");
						if (!crs.getString("branch_add").equals("")) {
							Str.append(crs.getString("branch_add")).append(", ");
						}
						Str.append("<br>").append(crs.getString("city_name")).append("");
						if (!crs.getString("branch_pin").equals("")) {
							Str.append(" - ").append(crs.getString("branch_pin")).append("");
						}
						// if (crs.getString("branch_pin").equals("")) {
						// Str.append(", ");
						// }
						// Str.append(crs.getString("city_name"));
						Str.append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append(crs.getString("branch_notes")).append("</td>\n");
						Str.append("<td valign=top nowrap>");
						if (emp_role_id.equals("1")) {
							Str.append("<a href=\"branch-update.jsp?update=yes&branch_id=").append(crs.getString("branch_id")).append(" \">Update Branch</a>");
							Str.append("<br><a href=\"../portal/branch-logo.jsp?branch_id=").append(crs.getString("branch_id")).append("\">Update Logo</a><br>");
						}
						Str.append("<a href=branch-email.jsp?branch_id=").append(crs.getString("branch_id"));
						Str.append(">Email Settings</a>\n");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					// SOP("branch_id is ------" + branch_id);
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Branch(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
