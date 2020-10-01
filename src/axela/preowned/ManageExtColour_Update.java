// Bhagwan Singh (10 july 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageExtColour_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String extcolour_id = "0";
    public String extcolour_name = "";
    public String checkperm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String QueryString = "";
    public String extcolour_entry_id = "0";
    public String extcolour_entry_date = "";
    public String extcolour_modified_id = "";
    public String extcolour_modified_date = "";
    //public String colour_item_id = "0";
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
                extcolour_id = CNumeric(PadQuotes(request.getParameter("extcolour_id")));
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
                        extcolour_entry_id = emp_id;
                        extcolour_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageextcolour.jsp?extcolour_id=" + extcolour_id + "&msg=Exeterior Colour Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
                        GetValues(request, response);
                        extcolour_modified_id = emp_id;
                        extcolour_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageextcolour.jsp?extcolour_id=" + extcolour_id + "&msg=Exeterior Colour Updated Successfully!"));
                        }
                    } else if ("Delete Colour".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageextcolour.jsp?msg=Exeterior Colour Deleted Successfully!"));
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
        extcolour_id = PadQuotes(request.getParameter("extcolour_id"));
        extcolour_name = PadQuotes(request.getParameter("txt_extcolour_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {

        if (extcolour_name.equals("")) {
            msg = msg + "<br>Enter Colour!";
        }
        try {
            if (!extcolour_name.equals("")) {
                StrSql = "SELECT extcolour_name"
                        + " FROM axela_preowned_extcolour"
                        + " WHERE extcolour_name = '" + extcolour_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " AND extcolour_id != " + extcolour_id + "";
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
                extcolour_id = ExecuteQuery("SELECT (COALESCE(MAX(extcolour_id), 0) +1) FROM axela_preowned_extcolour");
                StrSql = "INSERT INTO axela_preowned_extcolour"
                        + " (extcolour_id,"
                        + " extcolour_name,"
                        + " extcolour_entry_id,"
                        + " extcolour_entry_date)"
                        + " values"
                        + " (" + extcolour_id + ","
                        + " '" + extcolour_name + "',"
                        + " " + extcolour_entry_id + ","
                        + " '" + extcolour_entry_date + "')";
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
            StrSql = "SELECT extcolour_id, extcolour_name,"
                    + " extcolour_entry_id, extcolour_entry_date, extcolour_modified_id, extcolour_modified_date"
                    + " FROM axela_preowned_extcolour"
                    + " WHERE extcolour_id = " + extcolour_id + "";
//            SOP(StrSqlBreaker(StrSql));
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    extcolour_name = crs.getString("extcolour_name");
                    extcolour_entry_id = crs.getString("extcolour_entry_id");
                    entry_by = Exename(comp_id, crs.getInt("extcolour_entry_id"));
                    entry_date = strToLongDate(crs.getString("extcolour_entry_date"));
                    extcolour_modified_id = crs.getString("extcolour_modified_id");
                    if (!extcolour_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(extcolour_modified_id));
                        modified_date = strToLongDate(crs.getString("extcolour_modified_date"));
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
                StrSql = "UPDATE axela_preowned_extcolour"
                        + " SET"
                        + " extcolour_name = '" + extcolour_name + "',"
                        + " extcolour_modified_id = " + extcolour_modified_id + ","
                        + " extcolour_modified_date = '" + extcolour_modified_date + "'"
                        + " where extcolour_id = " + extcolour_id + "";
//                SOP("StrSql--" + StrSql);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT preowned_extcolour_id"
                + " FROM " + compdb(comp_id) + "axela_preowned"
                + " WHERE preowned_extcolour_id = " + extcolour_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Colour is Associated with Pre Owned!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "DELETE FROM axela_preowned_extcolour "
                        + " WHERE extcolour_id = " + extcolour_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
