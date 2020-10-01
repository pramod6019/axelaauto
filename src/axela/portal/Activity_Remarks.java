// Ved (11 Feb 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Activity_Remarks extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String updateB = "";
    public String d = "";
    public static String msg = "";
    public String activity_title = "";
    public String activity_remarks = "";
    public String employee_id = "0";
    public String activity_id = "0";
    public String activity_remarks_entry_id = "0";
    public String activity_remarks_entry_datetime = "";
    public String StrSql = "";
    public String modified_by = "";
    public String modified_date = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_activity_access", request, response);
            emp_id = CNumeric(GetSession("emp_id", request));
            updateB = PadQuotes(request.getParameter("update_button"));
            activity_id = PadQuotes(request.getParameter("activity_id"));
            employee_id = CNumeric(PadQuotes(request.getParameter("employee_id")));
            msg = PadQuotes(request.getParameter("msg"));
            d = PadQuotes(request.getParameter("d"));
            PopulateFields(response);

            if (activity_id.equals("0")) {
                response.sendRedirect(response.encodeRedirectURL("../portal/home.jsp"));
            }
            if (!activity_id.equals("")) {
                StrSql = "SELECT activity_id, activity_title"
                        + " from " + compdb(comp_id) + "axela_activity"
                        + " where activity_id = " + CNumeric(activity_id) + "";
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    while (crs.next()) {
                        if (!crs.getString("activity_title").equals("")) {
                            activity_title = activity_title + "<a href='activity-list.jsp?activity_id=" + crs.getString("activity_id") + "'>" + crs.getString("activity_title") + "</a>";
                        }
                    }
                } else {
                    response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity!"));
                }
                crs.close();
            }
            if ("Update".equals(updateB)) {
                GetValues(request, response);
                CheckForm();
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                } else {
                    activity_remarks_entry_id = emp_id;
                    activity_remarks_entry_datetime = ToLongDate(kknow());
                    StrSql = "UPDATE " + compdb(comp_id) + "axela_activity"
                            + " SET"
                            + " activity_remarks = '" + activity_remarks + "',"
                            + " activity_remarks_entry_id = " + activity_remarks_entry_id + ","
                            + " activity_remarks_entry_datetime = '" + activity_remarks_entry_datetime + "'"
                            + " where activity_id = " + CNumeric(activity_id) + "";
                    updateQuery(StrSql);
                    response.sendRedirect(response.encodeRedirectURL("activity-list.jsp?activity_id=" + activity_id + "&msg=Remarks Updated Successfully!"));
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
        activity_remarks = PadQuotes(request.getParameter("txt_activity_remarks"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (activity_remarks.equals("")) {
            msg = msg + "<br>Enter Remarks!";
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT activity_id, activity_remarks, activity_remarks_entry_id,"
                    + " activity_remarks_entry_datetime"
                    + " from " + compdb(comp_id) + "axela_activity"
                    + " where activity_id = " + CNumeric(activity_id) + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    activity_id = crs.getString("activity_id");
                    activity_remarks = crs.getString("activity_remarks");
                    activity_remarks_entry_id = crs.getString("activity_remarks_entry_id");
                    if (!activity_remarks_entry_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(activity_remarks_entry_id));
                        modified_date = strToLongDate(crs.getString("activity_remarks_entry_datetime"));
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
}
