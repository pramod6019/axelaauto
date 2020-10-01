package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managecustomergroup_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String branch_id = "0";
    public String group_id = "0";
    public String group_desc = "";
    public String group_entry_id = "0";
    public String group_entry_date = "";
    public String group_modified_id = "0";
    public String group_modified_date = "";
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
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                CheckPerm(comp_id, "emp_role_id", request, response);
                branch_id = CNumeric(GetSession("emp_branch_id", request));
                add = PadQuotes(request.getParameter("add"));
                update = PadQuotes(request.getParameter("update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
                QueryString = PadQuotes(request.getQueryString());

                if (add.equals("yes")) {
                    status = "Add";
                } else if (update.equals("yes")) {
                    status = "Update";
                }

                if ("yes".equals(add)) {
                    if (!"yes".equals(addB)) {
                        group_desc = "";
                    } else {
                        GetValues(request, response);
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecustomergroup.jsp?group_id=" + group_id + "&msg=Group Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
                        GetValues(request, response);
                        group_modified_id = CNumeric(GetSession("emp_id", request));
                        group_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecustomergroup.jsp?group_id=" + group_id + "&msg=Group Updated Successfully!"));
                        }
                    } else if ("Delete Group".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecustomergroup.jsp?msg=Group Deleted Successfully!"));
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
        group_desc = PadQuotes(request.getParameter("txt_group_desc"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));

    }

    protected void CheckForm() {
        if (group_desc.equals("")) {
            msg = msg + "<br>Enter Group!";
        }
        if (!group_desc.equals("")) {

            StrSql = "SELECT group_desc"
                    + " FROM " + compdb(comp_id) + "axela_customer_group"
                    + " WHERE group_desc ='" + group_desc + "'";
            if (update.equals("yes")) {
                StrSql = StrSql + " AND group_id !=" + group_id + "";
            }
            if (!ExecuteQuery(StrSql).equals("")) {
                msg = msg + "<br>Similar Group found!";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                group_id = ExecuteQuery("Select (coalesce(max(group_id),0)+1) from " + compdb(comp_id) + "axela_customer_group");
                StrSql = "Insert into " + compdb(comp_id) + "axela_customer_group"
                        + " (group_id,"
                        + " group_desc,"
                        + " group_entry_id,"
                        + " group_entry_date)"
                        + " values"
                        + " (" + group_id + ","
                        + " '" + group_desc + "',"
                        + " '" + emp_id + "',"
                        + " '" + ToLongDate(kknow()) + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_customer_group where group_id=" + group_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    group_id = crs.getString("group_id");
                    group_desc = crs.getString("group_desc");
                    group_entry_id = crs.getString("group_entry_id");
                    entry_by = Exename(comp_id, crs.getInt("group_entry_id"));
                    entry_date = strToLongDate(crs.getString("group_entry_date"));
                    group_modified_id = crs.getString("group_modified_id");
                    if (!group_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(group_modified_id));
                        modified_date = strToLongDate(crs.getString("group_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Group!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_group"
                        + " SET"
                        + " group_desc = '" + group_desc + "',"
                        + " group_modified_id = " + group_modified_id + ","
                        + " group_modified_date = '" + group_modified_date + "'"
                        + " where group_id = " + group_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT trans_group_id FROM " + compdb(comp_id) + "axela_customer_group_trans where trans_group_id = " + group_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Group is Associated with Customer!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_customer_group where group_id = " + group_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
