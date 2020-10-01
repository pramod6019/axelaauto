package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managefranchtype_Update extends Connect {

    public String add = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String franchiseetype_id = "0";
    public String franchiseetype_name = "";
    public String QueryString = "";
    public String franchiseetype_entry_id = "0";
    public String franchiseetype_entry_date = "";
    public String franchiseetype_modified_id = "0";
    public String franchiseetype_modified_date = "";
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
                 franchiseetype_id = CNumeric(PadQuotes(request.getParameter("franchiseetype_id")));
                 QueryString = PadQuotes(request.getQueryString());

                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }

                 if ("yes".equals(add)) {
                     if (!"yes".equals(addB)) {
                         franchiseetype_name = "";
                     } else {
                         GetValues(request, response);
                         franchiseetype_entry_id = emp_id;
                         franchiseetype_entry_date = ToLongDate(kknow());
                         AddFields();
                         if (!msg.equals("")) {

                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managefranchtype.jsp?franchiseetype_id=" + franchiseetype_id + "&msg=Franchisee Type Added Successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"yes".equals(updateB) && !"Delete Franchisee".equals(deleteB)) {
                         PopulateFields(response);
                     } else if ("yes".equals(updateB) && !"Delete Franchisee Type".equals(deleteB)) {
                         GetValues(request, response);
                         franchiseetype_modified_id = emp_id;
                         franchiseetype_modified_date = ToLongDate(kknow());
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managefranchtype.jsp?franchiseetype_id=" + franchiseetype_id + "&msg=Franchisee Type Updated Successfully!"));
                         }
                     } else if ("Delete Franchisee Type".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();

                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managefranchtype.jsp?msg=Franchisee Type Deleted Successfully!"));
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
        franchiseetype_name = PadQuotes(request.getParameter("txt_franchiseetype_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));

    }

    protected void CheckForm() {
        msg = "";
        if (franchiseetype_name.equals("")) {
            msg = msg + "<br>Enter Franchisee Type!";
        }
        try {
            if (!franchiseetype_name.equals("")) {
                if (update.equals("yes") && !franchiseetype_name.equals("")) {
                    StrSql = "Select franchiseetype_name from " + compdb(comp_id) + "axela_franchisee_type where franchiseetype_name = '" + franchiseetype_name + "' and franchiseetype_id != " + franchiseetype_id + "";
                }
                if (add.equals("yes") && !franchiseetype_name.equals("")) {
                    StrSql = "Select franchiseetype_name from " + compdb(comp_id) + "axela_franchisee_type where franchiseetype_name = '" + franchiseetype_name + "'";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Franchisee Type Found!";
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
                franchiseetype_id = ExecuteQuery("Select (coalesce(max(franchiseetype_id),0)+1) from " + compdb(comp_id) + "axela_franchisee_type");
                StrSql = "Insert into " + compdb(comp_id) + "axela_franchisee_type"
                        + " (franchiseetype_id,"
                        + " franchiseetype_name,"
                        + " franchiseetype_entry_id,"
                        + " franchiseetype_entry_date)"
                        + " values"
                        + " (" + franchiseetype_id + ","
                        + " '" + franchiseetype_name + "',"
                        + " " + franchiseetype_entry_id + ","
                        + " '" + franchiseetype_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_franchisee_type where franchiseetype_id = " + franchiseetype_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    franchiseetype_id = crs.getString("franchiseetype_id");
                    franchiseetype_name = crs.getString("franchiseetype_name");
                    franchiseetype_entry_id = crs.getString("franchiseetype_entry_id");
                    if (!franchiseetype_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(franchiseetype_entry_id));
                        entry_date = strToLongDate(crs.getString("franchiseetype_entry_date"));
                    }
                    franchiseetype_modified_id = crs.getString("franchiseetype_modified_id");
                    if (!franchiseetype_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(franchiseetype_modified_id));
                        modified_date = strToLongDate(crs.getString("franchiseetype_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Franchisee Type!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_franchisee_type"
                        + " SET"
                        + " franchiseetype_name = '" + franchiseetype_name + "',"
                        + " franchiseetype_modified_id = " + franchiseetype_modified_id + ","
                        + " franchiseetype_modified_date = '" + franchiseetype_modified_date + "'"
                        + " where franchiseetype_id = " + franchiseetype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT franchisee_franchiseetype_id from " + compdb(comp_id) + "axela_franchisee where franchisee_franchiseetype_id = " + franchiseetype_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Franchisee Type is Associated with a Franchisee!";
        }
        if (franchiseetype_id.equals("1")) {
            msg = msg + "<br>Cannot Delete First Record!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_franchisee_type where franchiseetype_id = " + franchiseetype_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
