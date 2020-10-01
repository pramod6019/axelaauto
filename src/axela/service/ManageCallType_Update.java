package axela.service;

/**
 * @author Sangita 02 APRIL 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCallType_Update extends Connect {

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
    public String type_id = "0";
    public String type_name = "";
    public String type_entry_id = "0";
    public String type_entry_by = "";
    public String type_entry_date = "";
    public String type_modified_id = "0";
    public String type_modified_by = "";
    public String type_modified_date = "";
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
            type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
            QueryString = PadQuotes(request.getQueryString());

            if (update.equals("yes")) {
                if (type_id.equals("")) {
                    response.sendRedirect(response.encodeRedirectURL("index.jsp"));
                } else if (type_id.equals("0")) {
                    response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Call Type!"));
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
                    type_entry_id = emp_id;
                    type_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managecalltype.jsp?type_id=" + type_id + "&msg=Call Type added successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Call Type".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Call Type".equals(deleteB)) {
                    GetValues(request, response);
                    type_modified_id = emp_id;
                    type_modified_date = ToLongDate(kknow());

                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managecalltype.jsp?type_id=" + type_id + "&msg=Call Type updated successfully!"));
                    }
                }
                if ("Delete Call Type".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managecalltype.jsp?msg=Call Type deleted successfully!"));
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
        type_name = PadQuotes(request.getParameter("txt_type_name"));
        type_entry_by = PadQuotes(request.getParameter("entry_by"));
        type_modified_by = PadQuotes(request.getParameter("modified_by"));
        type_entry_date = PadQuotes(request.getParameter("entry_date"));
        type_modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (type_name.equals("")) {
            msg = "<br>Enter Call Type Name!";
        }
        try {
            if (!type_name.equals("")) {
                StrSql = "Select calltype_name from " + compdb(comp_id) + "axela_service_call_type"
                        + " where calltype_name = '" + type_name + "'"
                        + " and calltype_id != " + type_id + "";
                CachedRowSet crs = processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Call Type Found!";
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
                type_id = ExecuteQuery("Select coalesce(max(calltype_id),0)+1 as calltype_id from " + compdb(comp_id) + "axela_service_call_type");

                StrSql = "INSERT into " + compdb(comp_id) + "axela_service_call_type"
                        + " (calltype_id,"
                        + " calltype_name,"
                        + " calltype_entry_id,"
                        + " calltype_entry_date)"
                        + " values"
                        + " (" + type_id + ","
                        + " '" + type_name + "',"
                        + " " + type_entry_id + ","
                        + " '" + type_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_service_call_type"
                    + " where calltype_id = " + type_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    type_name = crs.getString("calltype_name");
                    type_entry_id = crs.getString("calltype_entry_id");
                    if (!type_entry_id.equals("0")) {
                        type_entry_by = Exename(comp_id, Integer.parseInt(type_entry_id));
                    }
                    type_entry_date = strToLongDate(crs.getString("calltype_entry_date"));
                    type_modified_id = crs.getString("calltype_modified_id");
                    if (!type_modified_id.equals("0")) {
                        type_modified_by = Exename(comp_id, Integer.parseInt(type_modified_id));
                    }
                    type_modified_date = strToLongDate(crs.getString("calltype_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Call Type!"));
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
                StrSql = "UPDATE  " + compdb(comp_id) + "axela_service_call_type"
                        + " SET"
                        + " calltype_name = '" + type_name + "',"
                        + " calltype_modified_id = " + type_modified_id + ","
                        + " calltype_modified_date = '" + type_modified_date + "'"
                        + " where calltype_id = " + type_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        if (msg.equals("")) {
            try {
                StrSql = "Select call_type_id"
                        + " from " + compdb(comp_id) + "axela_service_call"
                        + " where call_type_id = " + type_id + "";
                if (ExecuteQuery(StrSql).equals(type_id)) {
                    msg = msg + "<br>Call Type is associated with Call!";
                }
                if (msg.equals("")) {
                    StrSql = "Delete from " + compdb(comp_id) + "axela_service_call_type"
                            + " where calltype_id = " + type_id + "";
                    updateQuery(StrSql);
                }
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
