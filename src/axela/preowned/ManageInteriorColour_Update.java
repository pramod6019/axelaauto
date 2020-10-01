// Bhagwan Singh (10 july 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInteriorColour_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String intcolour_id = "0";
    public String intcolour_name = "";
    public String checkperm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String QueryString = "";
    public String intcolour_entry_id = "0";
    public String intcolour_entry_date = "";
    public String intcolour_modified_id = "";
    public String intcolour_modified_date = "";
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
                CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
                add = PadQuotes(request.getParameter("add"));
                update = PadQuotes(request.getParameter("update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                intcolour_id = CNumeric(PadQuotes(request.getParameter("intcolour_id")));
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
                        intcolour_entry_id = emp_id;
                        intcolour_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageinteriorcolour.jsp?intcolour_id=" + intcolour_id + "&msg=Interior Colour Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
                        GetValues(request, response);
                        intcolour_modified_id = emp_id;
                        intcolour_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageinteriorcolour.jsp?intcolour_id=" + intcolour_id + "&msg=Interior Colour Updated Successfully!"));
                        }
                    } else if ("Delete Colour".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageinteriorcolour.jsp?msg=Interior Colour Deleted Successfully!"));
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
        intcolour_id = PadQuotes(request.getParameter("intcolour_id"));
        intcolour_name = PadQuotes(request.getParameter("txt_intcolour_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {

        if (intcolour_name.equals("")) {
            msg = msg + "<br>Enter Colour!";
        }
        try {
            if (!intcolour_name.equals("")) {
                StrSql = "SELECT intcolour_name"
                        + " FROM axela_preowned_intcolour"
                        + " WHERE intcolour_name = '" + intcolour_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " AND intcolour_id != " + intcolour_id + "";
                }
                if (!ExecuteQuery(StrSql).equals("")) {
                    msg = msg + "<br>Similar Colour Found";
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
                intcolour_id = ExecuteQuery("SELECT (COALESCE(MAX(intcolour_id), 0) +1) FROM axela_preowned_intcolour");
                StrSql = "INSERT INTO axela_preowned_intcolour"
                        + " (intcolour_id,"
                        + " intcolour_name,"
                        + " intcolour_entry_id,"
                        + " intcolour_entry_date)"
                        + " values"
                        + " (" + intcolour_id + ","
                        + " '" + intcolour_name + "',"
                        + " " + intcolour_entry_id + ","
                        + " '" + intcolour_entry_date + "')";
                updateQuery(StrSql);
//                SOP(StrSqlBreaker(StrSql));
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT intcolour_id, intcolour_name,"
                    + " intcolour_entry_id, intcolour_entry_date, intcolour_modified_id, intcolour_modified_date"
                    + " FROM axela_preowned_intcolour"
                    + " WHERE intcolour_id = " + intcolour_id + "";
//            SOP(StrSqlBreaker(StrSql));
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    intcolour_name = crs.getString("intcolour_name");
                    intcolour_entry_id = crs.getString("intcolour_entry_id");
                    entry_by = Exename(comp_id, crs.getInt("intcolour_entry_id"));
                    entry_date = strToLongDate(crs.getString("intcolour_entry_date"));
                    intcolour_modified_id = crs.getString("intcolour_modified_id");
                    if (!intcolour_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(intcolour_modified_id));
                        modified_date = strToLongDate(crs.getString("intcolour_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?&msg=Invalid Colour!"));
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
                StrSql = "UPDATE axela_preowned_intcolour"
                        + " SET"
                        + " intcolour_name = '" + intcolour_name + "',"
                        + " intcolour_modified_id = " + intcolour_modified_id + ","
                        + " intcolour_modified_date = '" + intcolour_modified_date + "'"
                        + " where intcolour_id = " + intcolour_id + "";
//                SOP("StrSql--" + StrSql);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT preowned_intcolour_id"
                + " FROM " + compdb(comp_id) + "axela_preowned"
                + " WHERE preowned_intcolour_id = " + intcolour_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Colour is Associated with Pre Owned!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "DELETE FROM axela_preowned_intcolour"
                        + " WHERE intcolour_id = " + intcolour_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
