//Bhagwan Singh 10/01/2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTicketStatus_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String ticketstatus_id = "0";
    public String ticketstatus_name = "";
    public String ticketstatus_entry_id = "0";
    public String ticketstatus_entry_date = "";
    public String ticketstatus_modified_id = "0";
    public String ticketstatus_modified_date = "";
    public String entry_by = "", entry_date = "";
    public String modified_by = "";
    public String modified_date = "";
    public String QueryString = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            ticketstatus_id = CNumeric(PadQuotes(request.getParameter("ticketstatus_id")));
            msg = PadQuotes(request.getParameter("msg"));
            QueryString = PadQuotes(request.getQueryString());

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }
            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                    ticketstatus_name = "";
                } else {
                    GetValues(request, response);
                    ticketstatus_entry_id = emp_id;
                    ticketstatus_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("manageticketstatus.jsp?msg=Ticket Status Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Ticket Status".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Ticket Status".equals(deleteB)) {
                    GetValues(request, response);
                    ticketstatus_modified_id = emp_id;
                    ticketstatus_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("manageticketstatus.jsp?msg=Ticket Status Updated Successfully!"));
                    }
                }
                if ("Delete Ticket Status".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("manageticketstatus.jsp?msg=Ticket Status Deleted Successfully!"));
                    }
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
        ticketstatus_name = PadQuotes(request.getParameter("txt_ticketstatus_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (ticketstatus_name.equals("")) {
            msg = "<br>Enter Ticket Status!";
        } else {
            try {
                if (!ticketstatus_name.equals("")) {
                    StrSql = "Select ticketstatus_name from " + compdb(comp_id) + "axela_service_ticket_status where ticketstatus_name = '" + ticketstatus_name + "'";
                    if (update.equals("yes")) {
                        StrSql = StrSql + " and ticketstatus_id != " + ticketstatus_id + "";
                    }
                    CachedRowSet crs = processQuery(StrSql, 0);
                    if (crs.isBeforeFirst()) {
                        msg = msg + "<br>Similar Ticket Status Found! ";
                    }
                    crs.close();
                }
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                ticketstatus_id = ExecuteQuery("Select (coalesce(max(ticketstatus_id),0)+1) from " + compdb(comp_id) + "axela_service_ticket_status");
                StrSql = "Insert into " + compdb(comp_id) + "axela_service_ticket_status"
                        + " (ticketstatus_id,"
                        + " ticketstatus_name,"
                        + " ticketstatus_rank,"
                        + " ticketstatus_entry_id,"
                        + " ticketstatus_entry_date)"
                        + " values"
                        + " (" + ticketstatus_id + ","
                        + " '" + ticketstatus_name + "',"
                        + " (Select (coalesce(max(ticketstatus_rank),0)+1) from " + compdb(comp_id) + "axela_service_ticket_status as Rank where 1=1 ),"
                        + " " + ticketstatus_entry_id + ","
                        + " '" + ticketstatus_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * "
                    + " from " + compdb(comp_id) + "axela_service_ticket_status"
                    + " where ticketstatus_id = " + ticketstatus_id + ""
                    + " and ticketstatus_id != 3";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    ticketstatus_name = crs.getString("ticketstatus_name");
                    ticketstatus_entry_id = crs.getString("ticketstatus_entry_id");
                    if (!ticketstatus_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(ticketstatus_entry_id));
                        entry_date = strToLongDate(crs.getString("ticketstatus_entry_date"));
                    }
                    ticketstatus_modified_id = crs.getString("ticketstatus_modified_id");
                    if (!ticketstatus_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(ticketstatus_modified_id));
                        modified_date = strToLongDate(crs.getString("ticketstatus_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket Status!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_status"
                        + " SET"
                        + " ticketstatus_name = '" + ticketstatus_name + "',"
                        + " ticketstatus_modified_id = " + ticketstatus_modified_id + ","
                        + " ticketstatus_modified_date = '" + ticketstatus_modified_date + "' "
                        + " where ticketstatus_id = " + ticketstatus_id + " ";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {

        StrSql = "SELECT ticket_ticketstatus_id FROM " + compdb(comp_id) + "axela_service_ticket where ticket_ticketstatus_id = " + ticketstatus_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Status is Associated with Ticket!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_status where ticketstatus_id = " + ticketstatus_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
