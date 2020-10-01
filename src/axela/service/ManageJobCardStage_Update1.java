package axela.service;

/**
 * @author Gurumurthy TS 30 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJobCardStage_Update1 extends Connect {

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
    public String stage_id = "0";
    public String stage_name = "";
    public String stage_entry_id = "0";
    public String stage_entry_by = "";
    public String stage_entry_date = "";
    public String stage_modified_id = "0";
    public String stage_modified_by = "";
    public String stage_modified_date = "";
    public String entry_date = "";
    public String modified_date = "";
    public String entry_by = "";
    public String modified_by = "";
    public String QueryString = "";

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
            stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
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
                    stage_entry_id = emp_id;
                    stage_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?stage_id=" + stage_id + "&msg=Job Card Stage Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Job Card Stage".equals(deleteB)) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Job Card Stage".equals(deleteB)) {
                    GetValues(request, response);
                    stage_modified_id = emp_id;
                    stage_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?stage_id=" + stage_id + "&msg=Job Card Stage Updated Successfully!"));
                    }
                }
                if ("Delete Job Card Stage".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Job Card Stage Deleted Successfully!"));
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
        stage_name = PadQuotes(request.getParameter("txt_stage_name"));
        stage_entry_by = PadQuotes(request.getParameter("entry_by"));
        stage_modified_by = PadQuotes(request.getParameter("modified_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (stage_name.equals("")) {
            msg = "<br>Enter Job Card Stage Name!";
        }
        try {
            if (!stage_name.equals("")) {
                StrSql = "Select stage_name from " + compdb(comp_id) + "axela_service_jc_stage where stage_name = '" + stage_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " and stage_id != " + stage_id + "";
                }
                CachedRowSet crs = processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Job Card Stage Found!";
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
                stage_id = ExecuteQuery("Select coalesce(max(stage_id),0)+1 as stage_id from " + compdb(comp_id) + "axela_service_jc_stage");

                StrSql = "INSERT into " + compdb(comp_id) + "axela_service_jc_stage"
                        + "(stage_id,"
                        + " stage_name,"
                        + " stage_rank,"
                        + " stage_entry_id,"
                        + " stage_entry_date)"
                        + " values"
                        + " (" + stage_id + ","
                        + " '" + stage_name + "',"
                        + "(Select (coalesce(max(stage_rank),0)+1) from " + compdb(comp_id) + "axela_service_jc_stage as Rank where 1=1 ),"
                        + " " + stage_entry_id + ","
                        + " '" + stage_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select *"
                    + " from " + compdb(comp_id) + "axela_service_jc_stage"
                    + " where stage_id = " + stage_id + ""
                    + " and stage_id != 5"
                    + " and stage_id != 6";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    stage_name = crs.getString("stage_name");
                    stage_entry_id = crs.getString("stage_entry_id");
                    if (!stage_entry_id.equals("0")) {
                        stage_entry_by = Exename(comp_id, Integer.parseInt(stage_entry_id));
                    }
                    entry_date = strToLongDate(crs.getString("stage_entry_date"));
                    stage_modified_id = crs.getString("stage_modified_id");
                    if (!stage_modified_id.equals("0")) {
                        stage_modified_by = Exename(comp_id, Integer.parseInt(stage_modified_id));
                    }
                    modified_date = strToLongDate(crs.getString("stage_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Stage!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
                        + " SET"
                        + " stage_name = '" + stage_name + "',"
                        + " stage_modified_id = " + stage_modified_id + ","
                        + " stage_modified_date = '" + stage_modified_date + "'"
                        + " where stage_id = " + stage_id + ""
                        + " and stage_id != 5"
                        + " and stage_id != 6";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT jc_stage_id FROM " + compdb(comp_id) + "axela_service_jc where jc_stage_id = " + stage_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Stage is Associated with Job Card!";
        }
        SOP(stage_id);
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_stage where stage_id = " + stage_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
