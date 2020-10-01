package axela.service;

/**
 * @author Gurumurthy TS 17 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJobCardBay_Update extends Connect {

    public String add = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String bay_id = "0";
    public String bay_name = "";
    public String bay_open = "0";
    public String bay_active = "0";
    public String bay_notes = "";
    public String bay_entry_id = "0";
    public String bay_entry_by = "";
    public String bay_entry_date = "";
    public String bay_modified_id = "0";
    public String bay_modified_by = "";
    public String bay_modified_date = "";
    public String entry_date = "";
    public String modified_date = "";
    public String entry_by = "", modified_by = "";
    public String QueryString = "";
    public String bay_branch_id = "";
    public String branch_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            msg = PadQuotes(request.getParameter("msg"));
            bay_id = CNumeric(PadQuotes(request.getParameter("bay_id")));
            QueryString = PadQuotes(request.getQueryString());

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }

            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                    bay_active = "1";
                    bay_open = "1";
                } else {
                    GetValues(request, response);
                    bay_entry_id = emp_id;
                    bay_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardbay.jsp?msg=Job Card Bay Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Job Card Bay".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Job Card Bay".equals(deleteB)) {
                    GetValues(request, response);
                    bay_modified_id = emp_id;
                    bay_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardbay.jsp?msg=Job Card Bay Updated Successfully!"));
                    }
                }
                if ("Delete Job Card Bay".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardbay.jsp?msg=Job Card Bay Deleted Successfully!"));
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
        bay_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
        bay_name = PadQuotes(request.getParameter("txt_bay_name"));
        bay_open = PadQuotes(request.getParameter("chk_bay_open"));
        if ((bay_open.equals("on"))) {
            bay_open = "1";
        }
        bay_active = PadQuotes(request.getParameter("chk_bay_active"));
        if ((bay_active.equals("on"))) {
            bay_active = "1";
        } else {
            bay_active = "0";
        }
        bay_notes = PadQuotes(request.getParameter("txt_bay_notes"));
        bay_entry_by = PadQuotes(request.getParameter("entry_by"));
        bay_modified_by = PadQuotes(request.getParameter("modified_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (bay_branch_id.equals("0")) {
            msg = "<br>Select Job Card Bay Branch!";
        }
        if (bay_name.equals("")) {
            msg = msg + "<br>Enter Job Card Bay Name!";
        }
        try {  
            if (!bay_name.equals("")) {
                StrSql = "Select bay_name from " + compdb(comp_id) + "axela_service_jc_bay"
                        + " where bay_branch_id = " + bay_branch_id + ""
                        + " and bay_name = '" + bay_name + "'";
//                bay_name1 = ExecuteQuery("Select bay_name from " + compdb(comp_id) + "axela_service_jc_bay"
//                        + " where bay_branch_id = " + bay_branch_id + ""
//                        + " and bay_name = " + bay_name);

//                StrSql = "select bay_name from " + compdb(comp_id) + "axela_service_jc_bay where bay_name = '" + bay_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and bay_id != " + bay_id + "";
                }
                if (bay_name.equals(ExecuteQuery(StrSql))) {
                    msg = msg + "<br>Similar Job Card Bay Found! ";
                }
//                CachedRowSet crs = processQuery(StrSql, 0);
//                if (crs.isBeforeFirst()) {
//                    msg = msg + "<br>Similar Job Card Bay Found! ";
//                }
//                crs.close();
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
                bay_id = ExecuteQuery("Select coalesce(max(bay_id),0)+1 from " + compdb(comp_id) + "axela_service_jc_bay");
                StrSql = "INSERT into " + compdb(comp_id) + "axela_service_jc_bay"
                        + " (bay_id,"
                        + " bay_name,"
                        + " bay_branch_id,"
                        + " bay_open,"
                        + " bay_active,"
                        + " bay_notes,"
                        + " bay_rank,"
                        + " bay_entry_id,"
                        + " bay_entry_date)"
                        + " values"
                        + " (" + bay_id + ","
                        + " '" + bay_name + "',"
                        + " " + bay_branch_id + ","
                        + " '" + bay_open + "',"
                        + " '" + bay_active + "',"
                        + " '" + bay_notes + "',"
                        + " (Select (coalesce(max(bay_rank),0)+1) from " + compdb(comp_id) + "axela_service_jc_bay as Rank),"
                        + " " + bay_entry_id + ","
                        + " '" + bay_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_service_jc_bay"
                    + " where bay_id = " + bay_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    bay_branch_id = crs.getString("bay_branch_id");
                    SOP("bay_branch_id=="+bay_branch_id);
                    bay_name = crs.getString("bay_name");
                    bay_open = crs.getString("bay_open");
                    bay_active = crs.getString("bay_active");
                    bay_notes = crs.getString("bay_notes");
                    bay_entry_id = crs.getString("bay_entry_id");
                    if (!bay_entry_id.equals("0")) {
                        bay_entry_by = Exename(comp_id, Integer.parseInt(bay_entry_id));
                    }
                    entry_date = strToLongDate(crs.getString("bay_entry_date"));
                    bay_modified_id = crs.getString("bay_modified_id");
                    if (!bay_modified_id.equals("0")) {
                        bay_modified_by = Exename(comp_id, Integer.parseInt(bay_modified_id));
                    }
                    modified_date = strToLongDate(crs.getString("bay_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Bay!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_bay"
                        + " SET"
                        + " bay_branch_id= '" + bay_branch_id + "',"
                        + " bay_name = '" + bay_name + "',"
                        + " bay_open = '" + bay_open + "',"
                        + " bay_active = '" + bay_active + "',"
                        + " bay_notes = '" + bay_notes + "',"
                        + " bay_modified_id = " + bay_modified_id + ","
                        + " bay_modified_date = '" + bay_modified_date + "'"
                        + " where bay_id = " + bay_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT jc_bay_id FROM " + compdb(comp_id) + "axela_service_jc WHERE jc_bay_id = " + bay_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg += "<br>Bay is associated with Job Card!";
        }

        if (msg.equals("")) {
            StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_bay WHERE bay_id = " + bay_id + "";
            updateQuery(StrSql);
        }
    }
}
