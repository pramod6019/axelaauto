// smitha nag 1 march 2013
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageAccess_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String access_module_id = "0";
    public String msg = "";
    public String access_id = "0";
    public String access_name = "";
    public String access_rank = "";
    public String QueryString = "";
    public String access_entry_id = "0", access_entry_date = "";
    public String access_modified_id = "0", access_modified_date = "";
    public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
    public String module_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	//user_id = CNumeric(PadQuotes(GetSession("user_id", request) + ""));
                emp_id = CNumeric(GetSession("emp_id", request));
                add = PadQuotes(request.getParameter("add"));
                update = PadQuotes(request.getParameter("update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                QueryString = PadQuotes(request.getQueryString());
//                access_id = CNumeric(PadQuotes(request.getParameter("access_id")));
                access_id = CNumeric(PadQuotes(request.getParameter("access_id")));
                module_id = CNumeric(PadQuotes(request.getParameter("module_id")));
                if (access_module_id.equals("0")) {
                    access_module_id = module_id;
                }

                if (add.equals("yes")) {
                    status = "Add";
                } else if (update.equals("yes")) {
                    status = "Update";
                }
                if ("yes".equals(add)) {
                    if (!"yes".equals(addB)) {
                        access_name = "";
                    } else {
                        access_entry_id = emp_id;
                        if (!emp_id.equals("0")) {
                            access_entry_date = ToLongDate(kknow());
                        }
                        GetValues(request, response);
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageaccess.jsp?dr_module_id=" + access_module_id + "&msg=Access added successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Access".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Access".equals(deleteB)) {
                        access_modified_id = emp_id;
                        if (!emp_id.equals("0")) {
                            access_modified_date = ToLongDate(kknow());
                        }
                        GetValues(request, response);
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageaccess.jsp?dr_module_id=" + access_module_id + "&msg=Access updated successfully!"));
                        }
                    } else if ("Delete Access".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageaccess.jsp?dr_module_id=" + access_module_id + "&msg=Access deleted successfully!"));
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
        access_module_id = CNumeric(PadQuotes(request.getParameter("dr_module")));
        access_name = PadQuotes(request.getParameter("txt_access_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (access_module_id.equals("0")) {
            msg = msg + "<br>Select Module!";
        }
        if (access_name.equals("")) {
            msg = msg + "<br>Enter Access!";
        }
        if (!access_name.equals("")) {
            try {
                if (update.equals("yes") && !access_name.equals("")) {
                    StrSql = "Select access_name from " + maindb() + "module_access"
                            + " where access_name = '" + access_name + "' and access_id!=" + access_id + "";
                } else if (add.equals("yes") && !access_name.equals("")) {
                    StrSql = "Select access_name from " + maindb() + "module_access"
                            + " where access_name = '" + access_name + "'";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Access found!";
                }
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                access_rank = CNumeric(PadQuotes(ExecuteQuery("SELECT  max(access_rank) as ID FROM " + maindb() + "module_access where access_module_id=" + access_module_id)));
                int access_ranki = Integer.parseInt(access_rank) + 1;
                access_rank = "" + access_ranki;
                StrSql = "insert into " + maindb() + "module_access"
                        + " (access_module_id,"
                        + " access_id,"
                        + " access_name,"
                        + " access_rank,"
                        + " access_entry_id,"
                        + " access_entry_date,"
                        + " access_modified_id,"
                        + " access_modified_date)"
                        + " values"
                        + " ('" + access_module_id + "',"
                        + " (Select coalesce(max(access_id),'0')+1 from " + maindb() + "module_access as access_id),"
                        + " '" + access_name + "',"
                        + " " + access_rank + ","
                        + " " + access_entry_id + ","
                        + " '" + access_entry_date + "',"
                        + " '0',"
                        + " '')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + maindb() + "module_access"
                    + " where access_id = " + access_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    access_module_id = crs.getString("access_module_id");
                    access_id = crs.getString("access_id");
                    access_name = crs.getString("access_name");
                    access_entry_id = crs.getString("access_entry_id");
                    if (!access_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, crs.getInt("access_entry_id"));
                        entry_date = strToLongDate(crs.getString("access_entry_date"));
                    }
                    access_modified_id = crs.getString("access_modified_id");
                    if (!access_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(access_modified_id));
                        modified_date = strToLongDate(crs.getString("access_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Access!"));
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
                StrSql = "UPDATE " + maindb() + "module_access"
                        + " SET"
                        + " access_module_id = '" + access_module_id + "',"
                        + " access_name = '" + access_name + "',"
                        + " access_modified_id = " + access_modified_id + ","
                        + " access_modified_date = '" + access_modified_date + "'"
                        + " where access_id = " + access_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        try {
            StrSql = "Delete from " + maindb() + "module_access"
                    + " where access_id = " + access_id + "";
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateModule() {
        StringBuilder exe = new StringBuilder();
        try {
            StrSql = "SELECT module_id, module_name"
                    + " from " + maindb() + "module"
                    + " order by module_name ";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                exe.append("<option value=").append(crs.getString("module_id")).append("");
                exe.append(Selectdrop(crs.getInt("module_id"), access_module_id));
                exe.append(">").append(crs.getString("module_name")).append("</option> \n");
            }
            crs.close();
            return exe.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
