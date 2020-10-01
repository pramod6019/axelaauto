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

public class ManageJobCardCat_Update extends Connect {

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
    public String cat_id = "0";
    public String cat_name = "";
    public String cat_entry_id = "0";
    public String cat_entry_by = "";
    public String cat_entry_date = "";
    public String cat_modified_id = "0";
    public String cat_modified_by = "";
    public String cat_modified_date = "";
    public String entry_date = "";
    public String modified_date = "";
//    public String entry_by = "";
//    public String modified_by = "";
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
            cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
            QueryString = PadQuotes(request.getQueryString());

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }

            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                } else {
                    GetValues(request, response);
                    cat_entry_id = emp_id;
                    cat_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardcat.jsp?cat_id=" + cat_id + "&msg=Job Card Category Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Job Card Category".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Job Card Category".equals(deleteB)) {
                    GetValues(request, response);
                    cat_modified_id = emp_id;
                    cat_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardcat.jsp?cat_id=" + cat_id + "&msg=Job Card Category Updated Successfully!"));
                    }
                }
                if ("Delete Job Card Category".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardcat.jsp?msg=Job Card Category Deleted Successfully!"));
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
        cat_name = PadQuotes(request.getParameter("txt_cat_name"));
        cat_entry_by = PadQuotes(request.getParameter("entry_by"));
        cat_modified_by = PadQuotes(request.getParameter("modified_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (cat_name.equals("")) {
            msg = "<br>Enter Job Card Category Name!";
        }
        try {
            if (!cat_name.equals("")) {
                StrSql = "select jccat_name from " + compdb(comp_id) + "axela_service_jc_cat where jccat_name = '" + cat_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and jccat_id != " + cat_id + "";
                }
                CachedRowSet crs = processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Job Card Category Found!";
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
                cat_id = ExecuteQuery("Select coalesce(max(jccat_id),0)+1 as jccat_id from " + compdb(comp_id) + "axela_service_jc_cat");

                StrSql = "INSERT into " + compdb(comp_id) + "axela_service_jc_cat"
                        + "(jccat_id,"
                        + " jccat_name,"
                        + " jccat_entry_id,"
                        + " jccat_entry_date)"
                        + " values"
                        + " (" + cat_id + ","
                        + " '" + cat_name + "',"
                        + " " + cat_entry_id + ","
                        + " '" + cat_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select *"
                    + " from " + compdb(comp_id) + "axela_service_jc_cat"
                    + " where jccat_id = " + cat_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    cat_name = crs.getString("jccat_name");
                    cat_entry_id = crs.getString("jccat_entry_id");
                    if (!cat_entry_id.equals("0")) {
                        cat_entry_by = Exename(comp_id, Integer.parseInt(cat_entry_id));
                    }
                    entry_date = strToLongDate(crs.getString("jccat_entry_date"));
                    cat_modified_id = crs.getString("jccat_modified_id");
                    if (!cat_modified_id.equals("0")) {
                        cat_modified_by = Exename(comp_id, Integer.parseInt(cat_modified_id));
                    }
                    modified_date = strToLongDate(crs.getString("jccat_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Category!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_cat"
                        + " SET"
                        + " jccat_name = '" + cat_name + "',"
                        + " jccat_modified_id = " + cat_modified_id + ","
                        + " jccat_modified_date = '" + cat_modified_date + "'"
                        + " where jccat_id = " + cat_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
//        StrSql = "SELECT jc_jccat_id FROM " + compdb(comp_id) + "axela_service_jc where jc_jccat_id = " + cat_id + "";
//        if (!ExecuteQuery(StrSql).equals("")) {
//            msg = msg + "<br>Category is associated with Job Card!";
//        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_cat where jccat_id = " + cat_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
