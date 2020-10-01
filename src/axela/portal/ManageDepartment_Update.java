package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageDepartment_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String department_id = "0";
    public String QueryString = "";
    public String department_name;
    public String emp_id = "0";
    public String comp_id = "0";
    public String department_entry_id = "0";
    public String department_entry_date = "";
    public String department_modified_id = "0";
    public String department_modified_date = "";
    public String entry_by = "", entry_date = "";
    public String modified_by = "";
    public String modified_date = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                CheckPerm(comp_id, "emp_role_id", request, response);
                add = PadQuotes(request.getParameter("add"));
                update = PadQuotes(request.getParameter("update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                department_id = CNumeric(PadQuotes(request.getParameter("department_id")));
                QueryString = PadQuotes(request.getQueryString());

                if (add.equals("yes")) {
                    status = "Add";
                } else if (update.equals("yes")) {
                    status = "Update";
                }
                if ("yes".equals(add)) {
                    if (!"yes".equals(addB)) {
                        department_name = "";
                    } else {
                        GetValues(request, response);
                        department_entry_id = emp_id;
                        department_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managedepartment.jsp?department_id=" + department_id + "&msg=Department Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Department".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Department".equals(deleteB)) {

                        GetValues(request, response);
                        department_modified_id = emp_id;
                        department_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managedepartment.jsp?department_id=" + department_id + "&msg=Department Updated Successfully!"));
                        }
                    } else if ("Delete Department".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managedepartment.jsp?msg=Department Deleted Successfully!"));
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

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        department_name = PadQuotes(request.getParameter("txt_department_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (department_name.equals("")) {
            msg = "<br>Enter Department!";
        }
        try {
            if (!department_name.equals("")) {
                if (update.equals("yes")) {
                    StrSql = "Select department_name from " + compdb(comp_id) + "axela_emp_department where department_name = '" + department_name + "' and department_id != " + department_id + "";
                } else if (add.equals("yes")) {
                    StrSql = "Select department_name from " + compdb(comp_id) + "axela_emp_department where department_name = '" + department_name + "'";
                }
                if (!ExecuteQuery(StrSql).equals("")) {
                    msg = msg + "<br>Similar Department Found!";
                }
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                department_id = ExecuteQuery("Select (coalesce(max(department_id),0)+1) from " + compdb(comp_id) + "axela_emp_department");
                StrSql = "Insert into " + compdb(comp_id) + "axela_emp_department"
                        + " (department_id,"
                        + " department_name,"
                        + " department_entry_id,"
                        + " department_entry_date)"
                        + " values"
                        + " (" + department_id + ","
                        + " '" + department_name + "',"
                        + " " + department_entry_id + ","
                        + " '" + department_entry_date + "')";
//                SOP("SqlStr" + SqlStr);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_emp_department where department_id = " + department_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    department_id = crs.getString("department_id");
                    department_name = crs.getString("department_name");
                    department_entry_id = crs.getString("department_entry_id");
                    if (!department_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(department_entry_id));
                        entry_date = strToLongDate(crs.getString("department_entry_date"));
                    }
                    department_modified_id = crs.getString("department_modified_id");
                    if (!department_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(department_modified_id));
                        modified_date = strToLongDate(crs.getString("department_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Department!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + compdb(comp_id) + "axela_emp_department"
                        + " SET"
                        + " department_name = '" + department_name + "',"
                        + " department_modified_id = " + department_modified_id + ","
                        + " department_modified_date = '" + department_modified_date + "'"
                        + " where department_id = " + department_id + "";
//                SOP("SqlStr" + SqlStr);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT emp_department_id FROM " + compdb(comp_id) + "axela_emp where emp_department_id = " + department_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Department is Associated with Executive!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_emp_department where department_id = " + department_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
