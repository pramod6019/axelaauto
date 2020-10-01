package axela.insurance;
//Dilip Kumar 19 APR 2013
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Insurance_Dashboard1 extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String ExeAccess = "";
    public String msg = "";
    public String StrSql = "";
    public String day1name = "", day1 = "", day2name = "", day2 = "", day3name = "", day3 = "", day4name = "", day4 = "", day5name = "", day5 = "", day6name = "", day6 = "", day7name = "", day7 = "", day8name = "", day8 = "";
    public String cday1name = "", cday1 = "", cday2name = "", cday2 = "", cday3name = "", cday3 = "", cday4name = "", cday4 = "", cday5name = "", cday5 = "", cday6name = "", cday6 = "", cday7name = "", cday7 = "";
    public int week1, week2, week3, week4;
    public String logWeek1, logWeek2, logWeek3, logWeek4;
    public String closeWeek1, closeWeek2, closeWeek3, closeWeek4;
    public String month1, month2, month3, month4;
    public String logMonth1, logMonth2, logMonth3, logMonth4;
    public String closeMonth1, closeMonth2, closeMonth3, closeMonth4;
    public String qur1, qur2, qur3, qur4, quarter;
    public String logQur1, logQur2, logQur3, logQur4;
    public String closeQur1, closeQur2, closeQur3, closeQur4;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
            Stats();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void Stats() {

        String startWeek, endWeek, startMonth, endMonth;
        Calendar cal = Calendar.getInstance();
        int currMonth = cal.get(cal.MONTH);
        int currYear = cal.get(cal.YEAR);
        int currDate = cal.get(cal.DATE);

//        -----------------------------Logged Days-----------------------------------------------------------------------------

        day1name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//                SOP("StrSql--day1--"+StrSql);
        day1 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day2name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day2--"+StrSql);
        day2 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day3name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day3--"+StrSql);
        day3 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day4name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day4--"+StrSql);
        day4 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day5name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day5--"+StrSql);
        day5 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day6name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day6--"+StrSql);
        day6 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day7name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day7--"+StrSql);
        day7 = ExecuteQuery(StrSql);

        cal.add(cal.DATE, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        cal.set(currYear, currMonth, currDate);
        day8name = "" + currDate + " " + TextMonth(currMonth);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and substring(insurfollowup_followup_time,1,8)=" + currYear + doublenum(currMonth + 1) + doublenum(currDate) + "";
//SOP("StrSql--day8--"+StrSql);
        day8 = ExecuteQuery(StrSql);

//        -----------------------------Logged Weeks-----------------------------------------------------------------------------
        cal = Calendar.getInstance();
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);

        startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate + 7 - cal.get(cal.DAY_OF_WEEK));
//        SOP("startWeek==="+startWeek);
        week1 = cal.get(cal.WEEK_OF_YEAR);

        cal.add(cal.DATE, -cal.get(cal.DAY_OF_WEEK) + 2);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
//        SOP("endWeek===="+endWeek);
        cal.set(currYear, currMonth, currDate);

        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endWeek + "'"
                + " and substring(insurfollowup_followup_time,1,8)<='" + startWeek + "')";
//SOP("StrSql--week1--"+StrSql);
        logWeek1 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

        cal.add(cal.DATE, -7);
        week2 = cal.get(cal.WEEK_OF_YEAR);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
        cal.set(currYear, currMonth, currDate);

        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endWeek + "'"
                + " and substring(insurfollowup_followup_time,1,8)<='" + startWeek + "')";
//SOP("StrSql--week2--"+StrSql);
        logWeek2 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

        cal.add(cal.DATE, -7);
        week3 = cal.get(cal.WEEK_OF_YEAR);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
        cal.set(currYear, currMonth, currDate);

        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endWeek + "'"
                + " and substring(insurfollowup_followup_time,1,8)<='" + startWeek + "')";
//SOP("StrSql--week3--"+StrSql);
        logWeek3 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        startWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);

        cal.add(cal.DATE, -7);
        week4 = cal.get(cal.WEEK_OF_YEAR);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        currDate = cal.get(cal.DATE);
        endWeek = currYear + doublenum(currMonth + 1) + doublenum(currDate);
        cal.set(currYear, currMonth, currDate);

        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endWeek + "'"
                + " and substring(insurfollowup_followup_time,1,8)<='" + startWeek + "')";
//SOP("StrSql--week4--"+StrSql);
        logWeek4 = ExecuteQuery(StrSql);

//        -----------------------------Logged Months-----------------------------------------------------------------------------
        cal = Calendar.getInstance();
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        month1 = TextMonth(currMonth) + " " + currYear;
        cal.add(cal.MONTH, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        cal.set(currYear, currMonth, 1);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endMonth + "'"
                + " and substring(insurfollowup_followup_time,1,8)<'" + startMonth + "')";
//SOP("StrSql--month1--"+StrSql);
        logMonth1 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        month2 = TextMonth(currMonth) + " " + currYear;
        cal.add(cal.MONTH, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        cal.set(currYear, currMonth, 1);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endMonth + "'"
                + " and substring(insurfollowup_followup_time,1,8)<'" + startMonth + "')";

        logMonth2 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        month3 = TextMonth(currMonth) + " " + currYear;
        cal.add(cal.MONTH, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        endMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        cal.set(currYear, currMonth, 1);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endMonth + "'"
                + " and substring(insurfollowup_followup_time,1,8)<'" + startMonth + "')";

        logMonth3 = ExecuteQuery(StrSql);

        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        startMonth = currYear + doublenum(currMonth + 2) + doublenum(1);
        month4 = TextMonth(currMonth) + " " + currYear;
        cal.add(cal.MONTH, -1);
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        endMonth = currYear + doublenum(currMonth + 1) + doublenum(1);
        cal.set(currYear, currMonth, 1);
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1 " + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,8)>='" + endMonth + "'"
                + " and substring(insurfollowup_followup_time,1,8)<'" + startMonth + "')";

        logMonth4 = ExecuteQuery(StrSql);

//        -----------------------------Logged Quarter-----------------------------------------------------------------------------
        cal = Calendar.getInstance();
        currMonth = cal.get(cal.MONTH);
        currYear = cal.get(cal.YEAR);
        cal.set(currYear, currMonth, 1);
        if (currMonth >= 0 && currMonth <= 2) {
            qur1 = currYear + " Qtr 1 ";
            quarter = "between 1 and 3";
        }
        if (currMonth >= 3 && currMonth <= 5) {
            qur1 = currYear + " Qtr 2 ";
            quarter = "between 4 and 6";
        }
        if (currMonth >= 6 && currMonth <= 8) {
            qur1 = currYear + " Qtr 3 ";
            quarter = "between 7 and 9";
        }
        if (currMonth >= 9 && currMonth <= 11) {
            qur1 = currYear + " Qtr 4 ";
            quarter = "between 10 and 12";
        }
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,4)='" + currYear + "'"
                + " and substring(insurfollowup_followup_time,5,2) " + quarter + ")";

        logQur1 = ExecuteQuery(StrSql);

        cal.add(cal.MONTH, -3);
        currMonth = cal.get(cal.MONTH);
        if (currMonth == -1) {
            currMonth = 11;
            currYear = currYear - 1;
        } else {
            currYear = cal.get(cal.YEAR);
        }
        cal.set(currYear, currMonth, 1);

        if (currMonth >= 0 && currMonth <= 2) {
            qur2 = currYear + " Qtr 1 ";
            quarter = "between 1 and 3";
        }
        if (currMonth >= 3 && currMonth <= 5) {
            qur2 = currYear + " Qtr 2 ";
            quarter = "between 4 and 6";
        }
        if (currMonth >= 6 && currMonth <= 8) {
            qur2 = currYear + " Qtr 3 ";
            quarter = "between 7 and 9";
        }
        if (currMonth >= 9 && currMonth <= 11) {
            qur2 = currYear + " Qtr 4 ";
            quarter = "between 10 and 12";
        }
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,4)='" + currYear + "'"
                + " and substring(insurfollowup_followup_time,5,2) " + quarter + ")";
        logQur2 = ExecuteQuery(StrSql);

        cal.add(cal.MONTH, -3);
        currMonth = cal.get(cal.MONTH);
        if (currMonth == -1) {
            currMonth = 11;
            currYear = currYear - 1;
        } else {
            currYear = cal.get(cal.YEAR);
        }
        cal.set(currYear, currMonth, 1);

        if (currMonth >= 0 && currMonth <= 2) {
            qur3 = currYear + " Qtr 1 ";
            quarter = "between 1 and 3";
        }
        if (currMonth >= 3 && currMonth <= 5) {
            qur3 = currYear + " Qtr 2 ";
            quarter = "between 4 and 6";
        }
        if (currMonth >= 6 && currMonth <= 8) {
            qur3 = currYear + " Qtr 3 ";
            quarter = "between 7 and 9";
        }
        if (currMonth >= 9 && currMonth <= 11) {
            qur3 = currYear + " Qtr 4 ";
            quarter = "between 10 and 12";
        }
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,4)='" + currYear + "'"
                + " and substring(insurfollowup_followup_time,5,2) " + quarter + ")";
        logQur3 = ExecuteQuery(StrSql);

        cal.add(cal.MONTH, -3);
        currMonth = cal.get(cal.MONTH);
        if (currMonth == -1) {
            currMonth = 11;
            currYear = currYear - 1;
        } else {
            currYear = cal.get(cal.YEAR);
        }
        cal.set(currYear, currMonth, 1);

        if (currMonth >= 0 && currMonth <= 2) {
            qur4 = currYear + " Qtr 1 ";
            quarter = "between 1 and 3";
        }
        if (currMonth >= 3 && currMonth <= 5) {
            qur4 = currYear + " Qtr 2 ";
            quarter = "between 4 and 6";
        }
        if (currMonth >= 6 && currMonth <= 8) {
            qur4 = currYear + " Qtr 3 ";
            quarter = "between 7 and 9";
        }
        if (currMonth >= 9 && currMonth <= 11) {
            qur4 = currYear + " Qtr 4 ";
            quarter = "between 10 and 12";
        }
        StrSql = "Select count(insurfollowup_id)"
                + " from " + compdb(comp_id) + "axela_insurance_followup"
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
                + " where 1=1" + ExeAccess
                + " and (substring(insurfollowup_followup_time,1,4)='" + currYear + "'"
                + " and substring(insurfollowup_followup_time,5,2) " + quarter + ")";
        logQur4 = ExecuteQuery(StrSql);

    }
}
