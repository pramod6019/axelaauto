package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageActivitystatus_Update extends Connect {

    public String add = "";
    public String addB = "";
    public String update = "";
    public String updateB = "";
    public String deleteB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String QueryString = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String status_id = "0";
    public String status_desc = "";
    public String status_entry_id = "0";
    public String status_entry_date = "";
    public String status_modified_id = "0";
    public String status_modified_date = "";
    public String entry_by = "";
    public String entry_date = "";
    public String modified_by = "";
    public String modified_date = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	CheckPerm(comp_id, "emp_role_id", request, response);
                emp_id = CNumeric(GetSession("emp_id", request));
                add = PadQuotes(request.getParameter("add"));
                update = PadQuotes(request.getParameter("update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                status_id = CNumeric(PadQuotes(request.getParameter("status_id")));
                QueryString = PadQuotes(request.getQueryString());

                if (add.equals("yes")) {
                    status = "Add";
                } else if (update.equals("yes")) {
                    status = "Update";
                }
                if ("yes".equals(add)) {
                    if (!"yes".equals(addB)) {
                        status_desc = "";
                    } else {
                        GetValues(request, response);
                        status_entry_id = emp_id;
                        status_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageactivitystatus.jsp?status_id=" + status_id + "&msg=Activity Status Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Activity Status".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Activity Status".equals(deleteB)) {
                        GetValues(request, response);
                        status_modified_id = emp_id;
                        status_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageactivitystatus.jsp?status_id=" + status_id + "&msg=Activity Status Updated Successfully!"));
                        }
                    } else if ("Delete Activity Status".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageactivitystatus.jsp?msg=Activity Status Deleted Successfully!"));
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
        status_desc = PadQuotes(request.getParameter("txt_status_desc"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (status_desc.equals("")) {
            msg = "<br>Enter Activity Status!";
        }
        try {
            if (!status_desc.equals("")) {
                StrSql = "select status_desc "
                        + " from " + compdb(comp_id) + "axela_activity_status "
                        + " where status_desc = '" + status_desc + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and status_id!=" + status_id + "";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Activity Status Found! ";
                }
                crs.close();
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
                status_id = ExecuteQuery("Select coalesce(max(status_id),0) + 1 as status_id from " + compdb(comp_id) + "axela_activity_status");
                StrSql = "Insert into " + compdb(comp_id) + "axela_activity_status"
                        + " (status_id,"
                        + " status_desc,"
                        + " status_entry_id,"
                        + " status_entry_date)"
                        + " values"
                        + " (" + status_id + ","
                        + " '" + status_desc + "',"
                        + " " + status_entry_id + ","
                        + " '" + status_entry_date + "')";

                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select * from " + compdb(comp_id) + "axela_activity_status "
                    + " where status_id=" + status_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    status_desc = crs.getString("status_desc");
                    status_entry_id = crs.getString("status_entry_id");
                    if (!status_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(status_entry_id));
                        entry_date = strToLongDate(crs.getString("status_entry_date"));
                    }
                    status_modified_id = crs.getString("status_modified_id");
                    if (!status_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(status_modified_id));
                        modified_date = strToLongDate(crs.getString("status_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity Status!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_activity_status"
                        + " SET"
                        + " status_desc = '" + status_desc + "',"
                        + " status_modified_id = " + status_modified_id + ","
                        + " status_modified_date = '" + status_modified_date + "'"
                        + " where status_id = " + status_id + "";
//                SOP(StrSqlBreaker(SqlStr));
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT activity_status_id from " + compdb(comp_id) + "axela_activity where activity_status_id = " + status_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Activity Status is associated with an Activity!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_activity_status where status_id =" + status_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
