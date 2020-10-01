package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageState_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String state_id = "0";
    public String state_name = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String QueryString = "";
    public String state_entry_id = "0";
    public String state_entry_date = "";
    public String state_modified_id = "0";
    public String state_modified_date = "";
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
                add = PadQuotes(request.getParameter("Add"));
                update = PadQuotes(request.getParameter("Update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                state_id = CNumeric(PadQuotes(request.getParameter("state_id")));
                QueryString = PadQuotes(request.getQueryString());

                if (add.equals("yes")) {
                    status = "Add";
                } else if (update.equals("yes")) {
                    status = "Update";
                }

                if ("yes".equals(add)) {
                    if (!"yes".equals(addB)) {
                        state_name = "";
                    } else {
                        GetValues(request, response);
                        state_entry_id = emp_id;
                        state_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managestate.jsp?state_id=" + state_id + "&msg=State Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete State".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete State".equals(deleteB)) {

                        GetValues(request, response);
                        state_modified_id = emp_id;
                        state_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managestate.jsp?state_id=" + state_id + "&msg=State Updated Successfully!"));
                        }
                    } else if ("Delete State".equals(deleteB)) {

                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managestate.jsp?msg=State Deleted Successfully!"));
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
        state_name = PadQuotes(request.getParameter("txt_state_name"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (state_name.equals("")) {
            msg = msg + "<br>Enter State!";
        }
        try {
            if (!state_name.equals("")) {
                if (update.equals("yes")) {
                    StrSql = "Select state_name from " + compdb(comp_id) + "axela_state where state_name = '" + state_name + "' and state_id != " + state_id + "";
                } else if (add.equals("yes")) {
                    StrSql = "Select state_name from " + compdb(comp_id) + "axela_state where state_name = '" + state_name + "'";
                }
                if (!ExecuteQuery(StrSql).equals("")) {
                    msg = msg + "<br>Similar State Name Found!";
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
                StrSql = "Insert into " + compdb(comp_id) + "axela_state"
                        + " (state_name,"
                        + " state_entry_id,"
                        + " state_entry_date)"
                        + " values"
                        + " ('" + state_name + "',"
                        + " " + state_entry_id + ","
                        + " '" + state_entry_date + "')";
                state_id = UpdateQueryReturnID(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            int state_idi = Integer.parseInt(state_id);
            StrSql = "Select * from " + compdb(comp_id) + "axela_state where state_id = " + state_idi + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    state_id = crs.getString("state_id");
                    state_name = crs.getString("state_name");
                    state_entry_id = crs.getString("state_entry_id");
                    if (!state_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(state_entry_id));
                        entry_date = strToLongDate(crs.getString("state_entry_date"));
                    }
                    state_modified_id = crs.getString("state_modified_id");
                    if (!state_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(state_modified_id));
                        modified_date = strToLongDate(crs.getString("state_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid State!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_state"
                        + " SET"
                        + " state_name = '" + state_name + "',"
                        + " state_modified_id = " + state_modified_id + ","
                        + " state_modified_date = '" + state_modified_date + "'"
                        + " where  state_id = " + state_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT city_state_id FROM " + compdb(comp_id) + "axela_city where city_state_id = " + state_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>State is Associated with a City!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_state where state_id = " + state_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
