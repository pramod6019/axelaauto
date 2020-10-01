package axela.service;
//aJIt 28th oct 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Parking_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String comp_id = "0";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String emp_id = "0", emp_role_id = "0";
    public String parking_id = "0";
    public String parking_branch_id = "0";
    public String parking_name = "";
    public String parking_notes = "";
    public String parking_rank = "";
    public String parking_active = "";
    public String parking_entry_id = "0";
    public String parking_entry_date = "";
    public String parking_modified_id = "0";
    public String parking_modified_date = "";
    public String entry_by = "";
    public String entry_date = "";
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
            emp_role_id = CNumeric(GetSession("emp_role_id", request));
            CheckPerm(comp_id, "emp_service_booking_access", request, response);
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            msg = PadQuotes(request.getParameter("msg"));
            QueryString = PadQuotes(request.getQueryString());

            if ("yes".equals(add)) {
                status = "Add";
                if (!"yes".equals(addB)) {
                    parking_active = "1";
                } else {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_booking_add", request).equals("1")) {
                        parking_entry_id = emp_id;
                        parking_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("parking-list.jsp?parking_id=" + parking_id + "&msg=Parking added successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
                    }
                }
            } else if ("yes".equals(update)) {
                status = "Update";
                if (!"yes".equals(updateB) && !"Delete Parking".equals(deleteB)) {
                    parking_id = CNumeric(PadQuotes(request.getParameter("parking_id")));
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Parking".equals(deleteB)) {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_booking_edit", request).equals("1")) {
                        parking_modified_id = emp_id;
                        parking_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("parking-list.jsp?parking_id=" + parking_id + "&msg=Parking updated successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
                    }
                } else if ("Delete Parking".equals(deleteB)) {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_booking_delete", request).equals("1")) {
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("parking-list.jsp?msg=Parking deleted successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
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
        parking_id = CNumeric(PadQuotes(request.getParameter("parking_id")));
        parking_branch_id = CNumeric(PadQuotes(request.getParameter("dr_parking_branch_id")));
        parking_name = PadQuotes(request.getParameter("txt_parking_name"));
        parking_notes = PadQuotes(request.getParameter("txt_parking_notes"));
        parking_active = CheckBoxValue(PadQuotes(request.getParameter("chk_parking_active")));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {

        if (parking_branch_id.equals("0")) {
            msg = "<br>Select Branch!";
        }

        if (parking_name.equals("")) {
            msg += "<br>Enter Parking Name!";
        } else {
            StrSql = "SELECT parking_name FROM " + compdb(comp_id) + "axela_service_parking"
                    + " WHERE parking_name = '" + parking_name + "'"
                    + " AND parking_branch_id = " + parking_branch_id + ""
                    + " AND parking_id != " + parking_id + "";
            if (!ExecuteQuery(StrSql).equals("")) {
                msg += "<br>Similar Parking Name Found!";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            parking_id = ExecuteQuery("SELECT COALESCE(MAX(parking_id), 0) + 1 FROM " + compdb(comp_id) + "axela_service_parking");

            StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_parking"
                    + " (parking_id,"
                    + " parking_branch_id,"
                    + " parking_name,"
                    + " parking_rank,"
                    + " parking_active,"
                    + " parking_notes,"
                    + " parking_entry_id,"
                    + " parking_entry_date)"
                    + " VALUES"
                    + " (" + parking_id + ","
                    + " " + parking_branch_id + ","
                    + " '" + parking_name + "',"
                    + " (SELECT COALESCE(MAX(parking_rank), 0) + 1 FROM " + compdb(comp_id) + "axela_service_parking AS Rank"
                    + " WHERE parking_branch_id = " + parking_branch_id + "),"
                    + " '" + parking_active + "',"
                    + " '" + parking_notes + "',"
                    + " " + parking_entry_id + ","
                    + " '" + parking_entry_date + "')";
            updateQuery(StrSql);
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_service_parking"
                    + " WHERE parking_id = " + parking_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);

            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    parking_name = crs.getString("parking_name");
                    parking_branch_id = crs.getString("parking_branch_id");
                    parking_notes = crs.getString("parking_notes");
                    parking_active = crs.getString("parking_active");
                    parking_entry_id = crs.getString("parking_entry_id");
                    if (!parking_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(parking_entry_id));
                        entry_date = strToLongDate(crs.getString("parking_entry_date"));
                    }
                    parking_modified_id = crs.getString("parking_modified_id");
                    if (!parking_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(parking_modified_id));
                        modified_date = strToLongDate(crs.getString("parking_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Parking!"));
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
            StrSql = "UPDATE " + compdb(comp_id) + "axela_service_parking"
                    + " SET"
                    + " parking_branch_id = " + parking_branch_id + ","
                    + " parking_name = '" + parking_name + "',"
                    + " parking_notes = '" + parking_notes + "',"
                    + " parking_active = '" + parking_active + "',"
                    + " parking_modified_id = " + parking_modified_id + ","
                    + " parking_modified_date = '" + parking_modified_date + "'"
                    + " WHERE parking_id = " + parking_id + "";
            updateQuery(StrSql);
        }
    }

    protected void DeleteFields() {
        if (parking_id.equals("1")) {
            msg = "<br>First Record cannot be Deleted!";
            return;
        }

        StrSql = "SELECT booking_parking_id FROM " + compdb(comp_id) + "axela_service_booking"
                + " WHERE booking_parking_id = " + parking_id + "";
        if (ExecuteQuery(StrSql).equals("")) {
            msg += "<br>Parking is associated with Booking!";
        }

        if (msg.equals("")) {
            StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_parking"
                    + " WHERE parking_id = " + parking_id + "";
            updateQuery(StrSql);
        }
    }
}
