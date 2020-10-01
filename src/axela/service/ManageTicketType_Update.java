// Ved Prakash (8 Jan 2013)
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTicketType_Update extends Connect {

    public String add = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String tickettype_id = "0";
    public String tickettype_name = "";
    public String tickettype_entry_id = "0";
    public String tickettype_entry_by = "";
    public String tickettype_entry_date = "";
    public String tickettype_modified_id = "0";
    public String tickettype_modified_by = "";
    public String tickettype_modified_date = "";
    public String entry_date = "";
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
            msg = PadQuotes(request.getParameter("msg"));
            tickettype_id = CNumeric(PadQuotes(request.getParameter("tickettype_id")));
            QueryString = PadQuotes(request.getQueryString());

            if (update.equals("yes")) {
                if (tickettype_id.equals("")) {
                    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
                } else if (tickettype_id.equals("0")) {
                    response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket Type!"));
                }
            }

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }

            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                } else {
                    GetValues(request, response);
                    tickettype_entry_id = emp_id;
                    tickettype_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managetickettype.jsp?tickettype_id=" + tickettype_id + "&msg=Ticket Type Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Ticket Type".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Ticket Type".equals(deleteB)) {
                    GetValues(request, response);
                    tickettype_modified_id = emp_id;
                    tickettype_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managetickettype.jsp?tickettype_id=" + tickettype_id + "&msg=Ticket Type Updated Successfully!"));
                    }
                }
                if ("Delete Ticket Type".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managetickettype.jsp?msg=Ticket Type Deleted Successfully!"));
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
        tickettype_name = PadQuotes(request.getParameter("txt_tickettype_name"));
        tickettype_entry_by = PadQuotes(request.getParameter("entry_by"));
        tickettype_modified_by = PadQuotes(request.getParameter("modified_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (tickettype_name.equals("")) {
            msg = "<br>Enter Ticket Type Name!";
        }
        try {
            if (!tickettype_name.equals("")) {
                StrSql = "Select tickettype_name from " + compdb(comp_id) + "axela_service_ticket_type where tickettype_name = '" + tickettype_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and tickettype_id != " + tickettype_id + "";
                }
                CachedRowSet crs = processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Ticket Type Found! ";
                }
                crs.close();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                tickettype_id = ExecuteQuery("Select max(tickettype_id) as tickettype_id from " + compdb(comp_id) + "axela_service_ticket_type");
                if (tickettype_id == null || tickettype_id.equals("")) {
                    tickettype_id = "0";
                }
                int tickettype_idi = Integer.parseInt(tickettype_id) + 1;
                tickettype_id = "" + tickettype_idi;

                StrSql = "Insert into " + compdb(comp_id) + "axela_service_ticket_type"
                        + " (tickettype_id,"
                        + " tickettype_name,"
                        + " tickettype_entry_id,"
                        + " tickettype_entry_date)"
                        + " values"
                        + " (" + tickettype_id + ","
                        + " '" + tickettype_name + "',"
                        + " " + tickettype_entry_id + ","
                        + " '" + tickettype_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select *"
                    + " FROM " + compdb(comp_id) + "axela_service_ticket_type"
                    + " where tickettype_id = " + tickettype_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    tickettype_name = crs.getString("tickettype_name");
                    tickettype_entry_id = crs.getString("tickettype_entry_id");
                    if (!tickettype_entry_id.equals("")) {
                        tickettype_entry_by = Exename(comp_id, Integer.parseInt(tickettype_entry_id));
                    }
                    entry_date = strToLongDate(crs.getString("tickettype_entry_date"));
                    tickettype_modified_id = crs.getString("tickettype_modified_id");
                    if (!tickettype_modified_id.equals("")) {
                        tickettype_modified_by = Exename(comp_id, Integer.parseInt(tickettype_modified_id));
                    }
                    modified_date = strToLongDate(crs.getString("tickettype_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket Type!"));
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
                StrSql = "UPDATE  " + compdb(comp_id) + "axela_service_ticket_type"
                        + " SET"
                        + " tickettype_name = '" + tickettype_name + "',"
                        + " tickettype_modified_id = " + tickettype_modified_id + ","
                        + " tickettype_modified_date  = '" + tickettype_modified_date + "'"
                        + " where tickettype_id = " + tickettype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT ticket_tickettype_id FROM " + compdb(comp_id) + "axela_service_ticket where ticket_tickettype_id = " + tickettype_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Type is Associated with Ticket!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_type where tickettype_id = " + tickettype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
