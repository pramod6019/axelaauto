package axela.service;

/**
 * @Bhagwan
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageJobCardStage_Update extends Connect {

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
    public String stage_id = "0";
    public String stage_name = "";
    public String stage_desc = "";
    public String stage_duehrs = "";
    public String stage_rank = "";
    public String stage_trigger1_hrs = "0";
    public String stage_trigger2_hrs = "0";
    public String stage_trigger3_hrs = "0";
    public String stage_trigger4_hrs = "0";
    public String stage_trigger5_hrs = "0";
    public String stage_entry_id = "0";
    public String stage_entry_date = "";
    public String stage_modified_id = "0";
    public String stage_modified_date = "";
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
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            msg = PadQuotes(request.getParameter("msg"));
            QueryString = PadQuotes(request.getQueryString());
            stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }

            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                    stage_name = "";
                    stage_desc = "";
                    stage_trigger1_hrs = "";
                    stage_trigger2_hrs = "";
                    stage_trigger3_hrs = "";
                    stage_trigger4_hrs = "";
                    stage_trigger5_hrs = "";
                    stage_duehrs = "";
                } else {
                    GetValues(request, response);
                    stage_entry_id = emp_id;
                    stage_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Added Successfully!"));
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
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Updated Successfully!"));
                    }
                }
                if ("Delete Job Card Stage".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Deleted Successfully!"));
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
        stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
        stage_name = PadQuotes(request.getParameter("txt_stage_name"));
        stage_desc = PadQuotes(request.getParameter("txt_stage_desc"));
        stage_duehrs = PadQuotes(request.getParameter("txt_stage_duehrs"));
        stage_trigger1_hrs = CNumeric(PadQuotes(request.getParameter("txt_stage_trigger1_hrs")));
        stage_trigger2_hrs = CNumeric(PadQuotes(request.getParameter("txt_stage_trigger2_hrs")));
        stage_trigger3_hrs = CNumeric(PadQuotes(request.getParameter("txt_stage_trigger3_hrs")));
        stage_trigger4_hrs = CNumeric(PadQuotes(request.getParameter("txt_stage_trigger4_hrs")));
        stage_trigger5_hrs = CNumeric(PadQuotes(request.getParameter("txt_stage_trigger5_hrs")));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (stage_name.equals("")) {
            msg = "<br>Enter Stage!";
        } else {
            if (!stage_name.equals("")) {
                StrSql = "SELECT jcstage_name "
                        + " FROM " + compdb(comp_id) + "axela_service_jc_stage"
                        + " WHERE jcstage_name = '" + stage_name + "'"
                        + " AND jcstage_id !=" + stage_id + "";
                if (ExecuteQuery(StrSql).equals(stage_name)) {
                    msg += "<br>Similar Stage Found!";
                }
            }
        }
        if (stage_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (stage_duehrs.equals("")) {
            msg = msg + "<br>Enter Due Hours!";
        } else if (!isNumeric(stage_duehrs)) {
            msg = msg + "<br>Due Hours must be Numeric!";
        }
        if ((!stage_trigger5_hrs.equals("0"))) {
            if (Double.parseDouble(stage_trigger5_hrs) < Double.parseDouble(stage_trigger4_hrs) || Double.parseDouble(stage_trigger5_hrs) < Double.parseDouble(stage_trigger3_hrs) || Double.parseDouble(stage_trigger5_hrs) < Double.parseDouble(stage_trigger2_hrs) || Double.parseDouble(stage_trigger5_hrs) < Double.parseDouble(stage_trigger1_hrs)) {
                msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
            }
        }
        if (!stage_trigger4_hrs.equals("0")) {
            if (Double.parseDouble(stage_trigger4_hrs) < Double.parseDouble(stage_trigger3_hrs) || Double.parseDouble(stage_trigger4_hrs) < Double.parseDouble(stage_trigger2_hrs) || Double.parseDouble(stage_trigger4_hrs) < Double.parseDouble(stage_trigger1_hrs)) {
                msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
            }
        }
        if (!stage_trigger3_hrs.equals("0")) {
            if (Double.parseDouble(stage_trigger3_hrs) < Double.parseDouble(stage_trigger2_hrs) || Double.parseDouble(stage_trigger3_hrs) < Double.parseDouble(stage_trigger1_hrs)) {
                msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
            }
        }
        if (!stage_trigger2_hrs.equals("0")) {
            if (Double.parseDouble(stage_trigger2_hrs) < Double.parseDouble(stage_trigger1_hrs)) {
                msg = msg + "<br> Level-2 hours should be more than previous Level hours";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                stage_id = ExecuteQuery("SELECT (COALESCE(MAX(jcstage_id), 0) + 1) FROM " + compdb(comp_id) + "axela_service_jc_stage");
                StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_stage"
                        + " (jcstage_id,"
                        + " jcstage_name,"
                        + " jcstage_desc,"
                        + " jcstage_duehrs,"
                        + " jcstage_rank,"
                        + " jcstage_trigger1_hrs,"
                        + " jcstage_trigger2_hrs,"
                        + " jcstage_trigger3_hrs,"
                        + " jcstage_trigger4_hrs,"
                        + " jcstage_trigger5_hrs,"
                        + " jcstage_entry_id,"
                        + " jcstage_entry_date)"
                        + " VALUES"
                        + " (" + stage_id + ","
                        + " '" + stage_name + "',"
                        + " '" + stage_desc + "',"
                        + " '" + stage_duehrs + "',"
                        + " (SELECT (COALESCE(MAX(jcstage_rank), 0) + 1) FROM " + compdb(comp_id) + "axela_service_jc_stage AS Rank),"
                        + " '" + stage_trigger1_hrs + "',"
                        + " '" + stage_trigger2_hrs + "',"
                        + " '" + stage_trigger3_hrs + "',"
                        + " '" + stage_trigger4_hrs + "',"
                        + " '" + stage_trigger5_hrs + "',"
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
            StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_service_jc_stage"
                    + " WHERE jcstage_id = " + stage_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);

            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    stage_name = crs.getString("jcstage_name");
                    stage_desc = crs.getString("jcstage_desc");
                    stage_duehrs = crs.getString("jcstage_duehrs");
                    stage_trigger1_hrs = crs.getString("jcstage_trigger1_hrs");
                    stage_trigger2_hrs = crs.getString("jcstage_trigger2_hrs");
                    stage_trigger3_hrs = crs.getString("jcstage_trigger3_hrs");
                    stage_trigger4_hrs = crs.getString("jcstage_trigger4_hrs");
                    stage_trigger5_hrs = crs.getString("jcstage_trigger5_hrs");
                    stage_entry_id = crs.getString("jcstage_entry_id");
                    if (!stage_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(stage_entry_id));
                        entry_date = strToLongDate(crs.getString("jcstage_entry_date"));
                    }
                    stage_modified_id = crs.getString("jcstage_modified_id");
                    if (!stage_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(stage_modified_id));
                        modified_date = strToLongDate(crs.getString("jcstage_modified_date"));
                    }
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
            StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_stage"
                    + " SET"
                    + " jcstage_name = '" + stage_name + "',"
                    + " jcstage_desc = '" + stage_desc + "',"
                    + " jcstage_duehrs = '" + stage_duehrs + "',"
                    + " jcstage_trigger1_hrs = '" + stage_trigger1_hrs + "',"
                    + " jcstage_trigger2_hrs = '" + stage_trigger2_hrs + "',"
                    + " jcstage_trigger3_hrs = '" + stage_trigger3_hrs + "',"
                    + " jcstage_trigger4_hrs = '" + stage_trigger4_hrs + "',"
                    + " jcstage_trigger5_hrs = '" + stage_trigger5_hrs + "',"
                    + " jcstage_modified_id = " + stage_modified_id + ","
                    + " jcstage_modified_date = '" + stage_modified_date + "'"
                    + " WHERE jcstage_id = " + stage_id + "";
            updateQuery(StrSql);
        }
    }

    protected void DeleteFields() {
        if (stage_id.equals("1")) {
            msg = "<br>First Record cannot be Deleted ";
            return;
        }

        if (msg.equals("")) {
            StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_stage"
                    + " WHERE jcstage_id = " + stage_id + "";
            updateQuery(StrSql);
        }
    }   
}
