package axela.service;

/**
 * @author Gurumurthy TS 17 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJobCardType_Update extends Connect {

    public String add = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "", jctype_workhour = "";
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
    public String entry_date = "";
    public String modified_date = "";
    public String entry_by = "";
    public String modified_by = "";
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
                if (type_id.equals("0")) {
                    response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Type!"));
                }
            }

            if ("yes".equals(add)) {
                status = "Add";
                if ("yes".equals(addB)) {
                    GetValues(request, response);
                    type_entry_id = emp_id;
                    type_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardtype.jsp?type_id=" + type_id + "&msg=Job Card Type Added Successfully!"));
                    }
                }
            } else if ("yes".equals(update)) {
                status = "Update";
                if (!"yes".equals(updateB) && !"Delete Job Card Type".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Job Card Type".equals(deleteB)) {
                    GetValues(request, response);
                    type_modified_id = emp_id;
                    type_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardtype.jsp?type_id=" + type_id + "&msg=Job Card Type Updated Successfully!"));
                    }
                } else if ("Delete Job Card Type".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardtype.jsp?msg=Job Card Type Deleted Successfully!"));
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
        jctype_workhour = PadQuotes(request.getParameter("txt_jctype_workhour"));
        jctype_workhour = jctype_workhour.replaceAll(":", "\\.");
        type_entry_by = PadQuotes(request.getParameter("entry_by"));
        type_modified_by = PadQuotes(request.getParameter("modified_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (type_name.equals("")) {
            msg = "<br>Enter Job Card Type Name!";
        }
        try {
            if (!type_name.equals("")) {
                StrSql = "Select jctype_name from " + compdb(comp_id) + "axela_service_jc_type"
                        + " where jctype_name = '" + type_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and jctype_id != " + type_id + "";
                }
                CachedRowSet crs = processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Job Card Type Found!";
                }
                crs.close();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        if (jctype_workhour.equals("")) {
            msg = msg + "<br>Enter Work Hour!";
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                type_id = ExecuteQuery("Select coalesce(max(jctype_id), 0)+1 as jctype_id from " + compdb(comp_id) + "axela_service_jc_type");

                StrSql = "INSERT into " + compdb(comp_id) + "axela_service_jc_type"
                        + " (jctype_id,"
                        + " jctype_name,"
                        + " jctype_workhour,"
                        + " jctype_entry_id,"
                        + " jctype_entry_date)"
                        + " values"
                        + " (" + type_id + ","
                        + " '" + type_name + "',"
                        + " '" + jctype_workhour + "',"
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
            StrSql = "Select * from " + compdb(comp_id) + "axela_service_jc_type"
                    + " where jctype_id = " + type_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    type_name = crs.getString("jctype_name");
                    jctype_workhour = crs.getString("jctype_workhour");
                    if (!jctype_workhour.contains(".")) {
                        jctype_workhour = jctype_workhour + ".00";
                    }
                    String jctype_workhours[] = jctype_workhour.split("\\.");
                    if (jctype_workhours[0].length() < 2) {
                        jctype_workhours[0] = "0" + jctype_workhours[0];
                    }
                    if (jctype_workhours[1].length() < 2) {
                        jctype_workhours[1] = jctype_workhours[1] + "0";
                    }
                    jctype_workhour = jctype_workhours[0] + ":" + jctype_workhours[1];

                    type_entry_id = crs.getString("jctype_entry_id");
                    if (!type_entry_id.equals("0")) {
                        type_entry_by = Exename(comp_id, Integer.parseInt(type_entry_id));
                    }
                    entry_date = strToLongDate(crs.getString("jctype_entry_date"));
                    type_modified_id = crs.getString("jctype_modified_id");
                    if (!type_modified_id.equals("0")) {
                        type_modified_by = Exename(comp_id, Integer.parseInt(type_modified_id));
                    }
                    modified_date = strToLongDate(crs.getString("jctype_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Type!"));
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
                StrSql = "UPDATE  " + compdb(comp_id) + "axela_service_jc_type"
                        + " SET"
                        + " jctype_name = '" + type_name + "',"
                        + " jctype_workhour = '" + jctype_workhour + "',"
                        + " jctype_modified_id = " + type_modified_id + ","
                        + " jctype_modified_date = '" + type_modified_date + "'"
                        + " where jctype_id = " + type_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
//        StrSql = "SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc where jc_id = " + type_id + "";
//        if (!ExecuteQuery(StrSql).equals("")) {
//            msg = msg + "<br>Type is associated with Job Card!";
//        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_type"
                        + " where jctype_id = " + type_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
