package axela.portal;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Followup_Update extends Connect {

    public String submitB = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String StrSql = "";
    public static String msg = "";
    public String student_id = "";
    public String student_pid = "";
    public String student_name = "";
    public String branch_id = "";
    public String student_branch_id = "";
    public String student_enqfollowup_active = "";
    public String followup_counselor_id = "";
    public String current_followup_time = "";
    public String followup_followup_time = "";
    public String followup_time = "";
    public String followup_entry_id = "";
    public String followup_entry_time = "";
    public String followup_desc = "";
    public String studpop = "";
    public String StartHour;
    public String StartMin;
    public String DurHour;
    public String DurMin;
    public String comp_actleadtime;
    public String currenttime;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_enquiry_add", request, response);
            CheckPerm(comp_id, "emp_act_add", request, response);
            emp_id = CNumeric(GetSession("emp_id", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            submitB = PadQuotes(request.getParameter("submit_button"));
            student_id = PadQuotes(request.getParameter("student_id"));
            studpop = PadQuotes(request.getParameter("studpop"));
            msg = PadQuotes(request.getParameter("msg"));

            if (student_id.equals("") || !isNumeric(student_id)) {
                student_id = "0";
            }
            if (submitB == null) {
                submitB = "";
            }
            if (msg == null) {
                msg = "";
            }
            PopulateFields();
            if (!branch_id.equals("0") && !student_branch_id.equals(branch_id)) {
                response.sendRedirect(response.encodeRedirectURL("home.jsp?All=&msg=Access denied!"));
            }
            if (!current_followup_time.equals("") && (Long.parseLong(current_followup_time) > Long.parseLong(ToLongDate(kknow())))) {
//                if(studpop.equals("yes"))
//                    response.sendRedirect(response.encodeRedirectURL("student-followup.jsp?student_id=" + student_id + "&msg=Follow-up time is greater than System time!"));
//                else
//                    response.sendRedirect(response.encodeRedirectURL("student-pop.jsp?student_id=" + student_id + "&msg=Follow-up time is greater than System time!"));
            }
            if ("Submit".equals(submitB)) {
                GetValues(request);
                CheckForm();
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                }
                if (msg.equals("")) {
                    followup_entry_id = emp_id;
                    followup_entry_time = ToLongDate(kknow());
                    UpdateFields();
                    if (studpop.equals("yes")) {
                        response.sendRedirect(response.encodeRedirectURL("../stud/student-pop-followup.jsp?student_id=" + student_id + "&msg=Follow-up updated successfully!"));
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("../stud/student-pop.jsp?student_id=" + student_id + "&msg=Follow-up updated successfully!"));
                    }
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

    protected void GetValues(HttpServletRequest request) {
        followup_desc = PadQuotes(request.getParameter("txt_followup_desc"));
        followup_followup_time = PadQuotes(request.getParameter("txt_followup_time"));
        followup_time = PadQuotes(request.getParameter("txt_followup_time"));
        StartHour = PadQuotes(request.getParameter("drop_StartHour"));
        StartMin = PadQuotes(request.getParameter("drop_StartMin"));
    }

    protected void CheckForm() {
        msg = "";
        comp_actleadtime = ExecuteQuery("Select comp_actleadtime from " + compdb(comp_id) + "axela_comp");
        Date date = AddHoursDate(kknow(), 0, Double.parseDouble(comp_actleadtime), 0);
        currenttime = ToLongDate(date);
        if (followup_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (student_enqfollowup_active.equals("1")) {
            if (followup_followup_time.equals("")) {
                msg = msg + "<br>Select Next Follow-up Date!";
            } else if (StartHour.equals("00") && StartMin.equals("00")) {
                msg = msg + "<br>Select Valid Next Follow-up Time!";
            }
            if (!followup_time.equals("") && !isValidDateFormatShort(followup_time)) {
                msg = msg + "<br>Enter Valid Next Follow-up Time!";
            }
            if (!followup_time.equals("") && isValidDateFormatShort(followup_time)) {
                if (!followup_followup_time.equals("") && !StartHour.equals("00")) {
                    followup_followup_time = followup_time + " " + StartHour + ":" + StartMin + ":00";
                    followup_followup_time = ConvertLongDateToStr(followup_followup_time);
                }
                if (Long.parseLong(followup_followup_time) <= Long.parseLong(current_followup_time)) {
//                    SOP("followup_followup_time--" + Long.parseLong(followup_followup_time));
//                    SOP("current_followup_time---" + Long.parseLong(current_followup_time));
                    msg = msg + "<br>Follow-up time must be greater than " + strToLongDate(current_followup_time) + "!";
                }
            }
        }
    }

    protected void UpdateFields() {
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + compdb(comp_id) + "axela_followup"
                        + " SET "
                        + " followup_desc = '" + followup_desc + "',"
                        + " followup_entry_time = '" + followup_entry_time + "', "
                        + " followup_entry_id = " + followup_entry_id + " "
                        + " where followup_desc='' and followup_student_id = " + student_id;
//                SOP("StrSql in UpdateFields: " + StrSql);
                updateQuery(StrSql);
                if (student_enqfollowup_active.equals("1")) {
                    AddFields();
                }
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    public void AddFields() {
        try {
            StrSql = " insert into " + compdb(comp_id) + "axela_followup "
                    + " ( "
                    + " followup_comp_id, "
                    + " followup_followuptype_id, "
                    + " followup_student_id, "
                    + " followup_counselor_id, "
                    + " followup_followup_time, "
                    + " followup_desc, "
                    + " followup_entry_id, "
                    + " followup_entry_time, "
                    + " followup_trigger) "
                    + " values "
                    + "("
                    + "1,"
                    + "'" + student_id + "',"
                    + "" + followup_counselor_id + ", "
                    + " '" + followup_followup_time + "', "
                    + " '', "
                    + " 0, "
                    + " '', "
                    + "0 "
                    + ")";
            updateQuery(StrSql);

        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateStartHour() {

        String year = "";
        if (StartHour == null || StartHour.equals("")) {
            StartHour = "10";
        }

        for (int i = 0; i <= 23; i++) {
            year = year + "<option value = " + doublenum(i) + "" + Selectdrop(i, StartHour) + ">" + doublenum(i) + "</option>\n";
        }
        return year;
    }

    public String PopulateStartMin() {
        String stringval = "";
        for (int i = 0; i <= 59; i++) {
            stringval = stringval + "<option value = " + doublenum(i) + "" + Selectdrop(i, StartMin) + ">" + doublenum(i) + "</option>\n";
        }
        return stringval;
    }

    public void PopulateFields() {
        try {
            StrSql = "select student_name, student_pid, student_branch_id,student_counsel_emp_id,student_enqfollowup_active, "
                    + " coalesce((select followup_followup_time from " + compdb(comp_id) + "axela_followup "
                    + " where followup_desc='' and followup_student_id=" + student_id + "),'') as followup_followup_time"
                    + " from " + compdb(comp_id) + "axela_student where student_id = " + student_id;
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                student_name = crs.getString("student_name");
                student_pid = crs.getString("student_pid");
                student_branch_id = crs.getString("student_branch_id");
                student_enqfollowup_active = crs.getString("student_enqfollowup_active");
                followup_counselor_id = crs.getString("student_counsel_emp_id");
                current_followup_time = crs.getString("followup_followup_time");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
