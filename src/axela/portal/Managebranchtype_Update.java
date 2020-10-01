package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managebranchtype_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String chkPermMsg = "";
    public String branchtype_id = "0";
    public String branchtype_name = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String branch_id = "";
    public String QueryString = "";
    public String branchtype_entry_id = "0";
    public String branchtype_entry_date = "";
    public String branchtype_modified_id = "0";
    public String branchtype_modified_date = "";
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
            	 emp_id = CNumeric(GetSession("emp_id", request));
                 CheckPerm(comp_id, "emp_role_id", request, response);
                 branch_id = CNumeric(GetSession("emp_branch_id", request));
                 add = PadQuotes(request.getParameter("add"));
                 update = PadQuotes(request.getParameter("update"));
                 addB = PadQuotes(request.getParameter("add_button"));
                 updateB = PadQuotes(request.getParameter("update_button"));
                 deleteB = PadQuotes(request.getParameter("delete_button"));
                 msg = PadQuotes(request.getParameter("msg"));
                 branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
                 QueryString = PadQuotes(request.getQueryString());

                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }

                 if ("yes".equals(add)) {
                     if (!"yes".equals(addB)) {
                         branchtype_name = "";
                     } else {
                         GetValues(request, response);
                         branchtype_entry_id = emp_id;
                         branchtype_entry_date = ToLongDate(kknow());
                         AddFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managebranchtype.jsp?branchtype_id=" + branchtype_id + "&msg=Branch Type Added Successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"yes".equals(updateB) && !"Delete Branch Type".equals(deleteB)) {
                         PopulateFields(response);
                     } else if ("yes".equals(updateB) && !"Delete Branch Type".equals(deleteB)) {
                         GetValues(request, response);
                         branchtype_modified_id = emp_id;
                         branchtype_modified_date = ToLongDate(kknow());
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managebranchtype.jsp?branchtype_id=" + branchtype_id + "&msg=Branch Type Updated Successfully!"));
                         }
                     } else if ("Delete Branch Type".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managebranchtype.jsp?msg=Branch Type Deleted Successfully!"));
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
        branchtype_name = PadQuotes(request.getParameter("txt_branchtype_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (branchtype_name.equals("")) {
            msg = msg + "<br>Enter Branch Type!";
        }

        try {
            if (!branchtype_name.equals("")) {
                if (update.equals("yes") && !branchtype_name.equals("")) {
                    StrSql = "Select branchtype_name from axela_branch_type where branchtype_name = '" + branchtype_name + "' and branchtype_id != " + branchtype_id + "";
                }
                if (add.equals("yes") && !branchtype_name.equals("")) {
                    StrSql = "Select branchtype_name from axela_branch_type where branchtype_name = '" + branchtype_name + "'";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Branch Type Found!";
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
                branchtype_id = ExecuteQuery("Select (coalesce(max(branchtype_id),0)+1) from axela_branch_type");
                StrSql = "Insert into axela_branch_type"
                        + " (branchtype_id,"
                        + "branchtype_name,"
                        + " branchtype_entry_id,"
                        + " branchtype_entry_date)"
                        + " values"
                        + " (" + branchtype_id + ","
                        + " '" + branchtype_name + "',"
                        + " " + branchtype_entry_id + ","
                        + " '" + branchtype_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from axela_branch_type where  branchtype_id = " + branchtype_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    branchtype_id = crs.getString("branchtype_id");
                    branchtype_name = crs.getString("branchtype_name");
                    branchtype_entry_id = crs.getString("branchtype_entry_id");
                    if (!branchtype_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(branchtype_entry_id));
                        entry_date = strToLongDate(crs.getString("branchtype_entry_date"));
                    }
                    branchtype_modified_id = crs.getString("branchtype_modified_id");
                    if (!branchtype_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(branchtype_modified_id));
                        modified_date = strToLongDate(crs.getString("branchtype_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Branch Type!"));
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
                StrSql = "UPDATE axela_branch_type"
                        + " SET"
                        + " branchtype_name = '" + branchtype_name + "',"
                        + " branchtype_modified_id = " + branchtype_modified_id + ","
                        + " branchtype_modified_date = '" + branchtype_modified_date + "'"
                        + " where branchtype_id = " + branchtype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT branch_branchtype_id from " + compdb(comp_id) + "axela_branch where branch_branchtype_id = " + branchtype_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Branch Type is Associated with a Branch!";
        }
        if (branchtype_id.equals("1")) {
            msg = msg + "<br>Cannot Delete First Record!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "Delete from axela_branch_type where branchtype_id = " + branchtype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
