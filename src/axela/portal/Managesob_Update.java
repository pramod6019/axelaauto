package axela.portal;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managesob_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String sob_id = "0";
    public String sob_name = "";
    public String QueryString = "";
    public String sob_entry_id = "0";
    public String sob_entry_date = "";
    public String sob_modified_id = "0";
    public String sob_modified_date = "";
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
                 branch_id = CNumeric(GetSession("emp_branch_id", request));
                 CheckPerm(comp_id, "emp_role_id", request, response);
                 add = PadQuotes(request.getParameter("add"));
                 update = PadQuotes(request.getParameter("update"));
                 addB = PadQuotes(request.getParameter("add_button"));
                 updateB = PadQuotes(request.getParameter("update_button"));
                 deleteB = PadQuotes(request.getParameter("delete_button"));
                 msg = PadQuotes(request.getParameter("msg"));
                 sob_id = CNumeric(PadQuotes(request.getParameter("sob_id")));
                 QueryString = PadQuotes(request.getQueryString());

                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }

                 if ("yes".equals(add)) {
                     if (!"yes".equals(addB)) {
                         sob_name = "";
                     } else {
                         GetValues(request, response);
                         sob_entry_id = emp_id;
                         sob_entry_date = ToLongDate(kknow());
                         AddFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managesob.jsp?sob_id=" + sob_id + "&msg=SOB Added Successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"yes".equals(updateB) && !"Delete SOB".equals(deleteB)) {
                         PopulateFields(response);
                     } else if ("yes".equals(updateB) && !"Delete SOB".equals(deleteB)) {
                         GetValues(request, response);
                         sob_modified_id = emp_id;
                         sob_modified_date = ToLongDate(kknow());
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managesob.jsp?sob_id=" + sob_id + "&msg=SOB Updated Successfully!"));
                         }
                     } else if ("Delete SOB".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managesob.jsp?msg=SOB Deleted Successfully!"));
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
        sob_name = PadQuotes(request.getParameter("txt_sob_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (sob_name.equals("")) {
            msg = msg + "<br>Enter Sob!";
        }
        try {
            if (!sob_name.equals("")) {
                StrSql = "Select sob_name from " + compdb(comp_id) + "axela_sob where sob_name = '" + sob_name + "'";
                if (update.equals("yes")) {
                    StrSql += " and sob_id != " + sob_id + "";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar SOB Found! ";
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
                sob_id = ExecuteQuery("Select (coalesce(max(sob_id),0)+1) from " + compdb(comp_id) + "axela_sob");
                StrSql = "Insert into " + compdb(comp_id) + "axela_sob"
                        + " (sob_id,"
                        + " sob_name ,"
                        + " sob_entry_id,"
                        + " sob_entry_date)"
                        + " values"
                        + " (" + sob_id + ","
                        + " '" + sob_name + "',"
                        + " " + sob_entry_id + ","
                        + " '" + sob_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_sob where sob_id=" + sob_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    sob_name = crs.getString("sob_name");
                    sob_entry_id = crs.getString("sob_entry_id");
                    if (!sob_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(sob_entry_id));
                        entry_date = strToLongDate(crs.getString("sob_entry_date"));
                    }
                    sob_modified_id = crs.getString("sob_modified_id");
                    if (!sob_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(sob_modified_id));
                        modified_date = strToLongDate(crs.getString("sob_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid SOB!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_sob"
                        + " SET"
                        + " sob_name = '" + sob_name + "',"
                        + " sob_modified_id = " + sob_modified_id + ","
                        + " sob_modified_date = '" + sob_modified_date + "'"
                        + " where sob_id = " + sob_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT enquiry_sob_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_sob_id = " + sob_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>SOB is Associated with Enquiry!";
        }
        StrSql = "SELECT lead_sob_id FROM " + compdb(comp_id) + "axela_sales_lead where lead_sob_id = " + sob_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>SOB is Associated with Lead!";
        }
        StrSql = "SELECT customer_sob_id FROM " + compdb(comp_id) + "axela_customer where customer_sob_id = " + sob_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>SOB is Associated with Customer!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_sob where sob_id = " + sob_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
