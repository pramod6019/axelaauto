// Ved (31 Jan 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Activity_Feedback extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String emp_role_id = "0";
    public String d = "";
    public String updateB = "";
    public static String msg = "";
    public String activity_title = "";
    public String employee_id = "";
    public String activity_id = "0";
    public String activity_status_id = "0";
    public String activity_feedback_entry_id = "0";
    public String activity_feedback_entry_datetime = "";
    public String activity_start_datetime = "";
    public String activity_end_datetime = "";
    public String activity_feedback = "";
    public String StrSql = "";
    public String ExeAccess = "";
    public String modified_by = "";
    public String modified_date = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            emp_role_id = CNumeric(GetSession("emp_role_id", request));
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_activity_access", request, response);
            activity_id = CNumeric(PadQuotes(request.getParameter("activity_id")));
            employee_id = CNumeric(PadQuotes(request.getParameter("employee_id")));
            updateB = PadQuotes(request.getParameter("update_button"));
            msg = PadQuotes(request.getParameter("msg"));
            d = PadQuotes(request.getParameter("d"));
            PopulateFields(response);

            if (activity_id.equals("0")) {
                response.sendRedirect(response.encodeRedirectURL("home.jsp"));
            }
            if (!activity_id.equals("0")) {
                StrSql = "SELECT activity_id, activity_title, activity_desc, activity_start_datetime,"
                        + " activity_end_datetime, activity_priority_id, activity_contact_person,"
                        + " activity_Phone1, activity_Phone2, activity_venue, activity_entry_id,"
                        + " activity_entry_datetime, activity_modified_id, activity_modified_datetime"
                        + " from " + compdb(comp_id) + "axela_activity"
                        + " where activity_id = " + CNumeric(activity_id) + "";
                CachedRowSet crs =processQuery(StrSql, 0);
                while (crs.next()) {
                    if (!crs.getString("activity_title").equals("")) {
                        activity_title = activity_title + "<a href='activity-list.jsp?activity_id=" + crs.getString("activity_id") + "'>" + crs.getString("activity_title") + "</a>";
                        activity_end_datetime = crs.getString("activity_end_datetime");
                    }
                }
                crs.close();
            }

            if ("Update".equals(updateB)) {
                GetValues(request, response);
                CheckForm();
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                } else {
                    activity_feedback_entry_id = emp_id;
                    activity_feedback_entry_datetime = ToLongDate(kknow());
                    StrSql = "UPDATE " + compdb(comp_id) + "axela_activity"
                            + " SET"
                            + " activity_status_id = " + activity_status_id + ","
                            + " activity_feedback = '" + activity_feedback + "',"
                            + " activity_feedback_entry_id = " + activity_feedback_entry_id + ","
                            + " activity_feedback_entry_datetime = '" + activity_feedback_entry_datetime + "'"
                            + " where activity_id = " + CNumeric(activity_id) + "";
                    updateQuery(StrSql);
                    response.sendRedirect(response.encodeRedirectURL("activity-list.jsp?activity_id=" + activity_id + "&msg=Feedback Updated Successfully!"));
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
        activity_id = PadQuotes(request.getParameter("activity_id"));
        d = PadQuotes(request.getParameter("d"));
        activity_feedback = PadQuotes(request.getParameter("txt_activity_feedback"));
        activity_status_id = CNumeric(PadQuotes(request.getParameter("drop_activity_status_id")));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT activity_id, activity_feedback, activity_status_id,"
                    + " activity_feedback_entry_id, activity_feedback_entry_datetime"
                    + " from " + compdb(comp_id) + "axela_activity"
                    + " where activity_id = " + activity_id + ExeAccess;
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    activity_id = crs.getString("activity_id");
                    activity_feedback = crs.getString("activity_feedback");
                    activity_status_id = crs.getString("activity_status_id");
                    activity_feedback_entry_id = crs.getString("activity_feedback_entry_id");
                    if (!activity_feedback_entry_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(activity_feedback_entry_id));
                        modified_date = strToLongDate(crs.getString("activity_feedback_entry_datetime"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void CheckForm() {
        if (activity_status_id.equals("0")) {
            msg = "<br>Select Activity Status!";
        }
        if (activity_feedback.equals("")) {
            msg = msg + "<br>Enter Feedback!";
        }
        activity_start_datetime = ExecuteQuery("SELECT activity_start_datetime"
                + " from " + compdb(comp_id) + "axela_activity"
                + " where activity_id= " + activity_id + " ");
        if (!activity_start_datetime.equals("")) {
            if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(activity_start_datetime)) {
                msg = msg + "<br>Feedback time should be greater than activity time!";
            }
        }


        if (Long.parseLong(activity_end_datetime) > Long.parseLong(ToLongDate(kknow()))) {
            msg = msg + "<br>Can't add/update Feedback before End Time!";
        }
    }

    public String PopulateActivityStatus() {
        StringBuilder Str = new StringBuilder();
        try {
            Str.append("<option value = 0>Select</option>");
            StrSql = "SELECT status_id, status_desc"
                    + " from " + compdb(comp_id) + "axela_activity_status"
                    + " order by status_desc";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("status_id"));
                Str.append(StrSelectdrop(crs.getString("status_id"), activity_status_id));
                Str.append(">").append(crs.getString("status_desc")).append("</option> \n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }
}
