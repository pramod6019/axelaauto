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

public class ManageJobCardPriority_Update extends Connect {

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
    public String priorityjc_id = "0";
    public String priorityjc_name = "";
    public String priorityjc_desc = "";
    public String priorityjc_duehrs = "";
    public String priorityjc_rank = "";
    public String priorityjc_trigger1_hrs = "0";
    public String priorityjc_trigger2_hrs = "0";
    public String priorityjc_trigger3_hrs = "0";
    public String priorityjc_trigger4_hrs = "0";
    public String priorityjc_trigger5_hrs = "0";
    public String priorityjc_entry_id = "0";
    public String priorityjc_entry_date = "";
    public String priorityjc_modified_id = "0";
    public String priorityjc_modified_date = "";
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

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }

            if ("yes".equals(add)) {
                if (!"yes".equals(addB)) {
                    priorityjc_name = "";
                    priorityjc_desc = "";
                    priorityjc_trigger1_hrs = "";
                    priorityjc_trigger2_hrs = "";
                    priorityjc_trigger3_hrs = "";
                    priorityjc_trigger4_hrs = "";
                    priorityjc_trigger5_hrs = "";
                    priorityjc_duehrs = "";
                } else {
                    GetValues(request, response);
                    priorityjc_entry_id = emp_id;
                    priorityjc_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardpriority.jsp?msg=Priority Added Successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"yes".equals(updateB) && !"Delete Job Card Priority".equals(deleteB)) {
                    priorityjc_id = CNumeric(PadQuotes(request.getParameter("priorityjc_id")));
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !"Delete Job Card Priority".equals(deleteB)) {
                    GetValues(request, response);
                    priorityjc_modified_id = emp_id;
                    priorityjc_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardpriority.jsp?msg= Priority Updated Successfully!"));
                    }
                }
                if ("Delete Job Card Priority".equals(deleteB)) {
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("managejobcardpriority.jsp?msg=Priority Deleted Successfully!"));
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
        priorityjc_id = CNumeric(PadQuotes(request.getParameter("priorityjc_id")));
        priorityjc_name = PadQuotes(request.getParameter("txt_priorityjc_name"));
        priorityjc_desc = PadQuotes(request.getParameter("txt_priorityjc_desc"));
        priorityjc_duehrs = PadQuotes(request.getParameter("txt_priorityjc_duehrs"));
        priorityjc_trigger1_hrs = PadQuotes(request.getParameter("txt_priorityjc_trigger1_hrs"));
        priorityjc_trigger2_hrs = PadQuotes(request.getParameter("txt_priorityjc_trigger2_hrs"));
        priorityjc_trigger3_hrs = PadQuotes(request.getParameter("txt_priorityjc_trigger3_hrs"));
        priorityjc_trigger4_hrs = PadQuotes(request.getParameter("txt_priorityjc_trigger4_hrs"));
        priorityjc_trigger5_hrs = PadQuotes(request.getParameter("txt_priorityjc_trigger5_hrs"));
        if (priorityjc_trigger1_hrs.equals("")) {
            priorityjc_trigger1_hrs = "0";
        }
        if (priorityjc_trigger2_hrs.equals("")) {
            priorityjc_trigger2_hrs = "0";
        }
        if (priorityjc_trigger3_hrs.equals("")) {
            priorityjc_trigger3_hrs = "0";
        }
        if (priorityjc_trigger4_hrs.equals("")) {
            priorityjc_trigger4_hrs = "0";
        }
        if (priorityjc_trigger5_hrs.equals("")) {
            priorityjc_trigger5_hrs = "0";
        }
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (priorityjc_name.equals("")) {
            msg = "<br>Enter Priority!";
        } else {
            try {
                if (!priorityjc_name.equals("")) {
                    StrSql = "Select priorityjc_name from " + compdb(comp_id) + "axela_service_jc_priority where priorityjc_name = '" + priorityjc_name + "'";
                    if (update.equals("yes")) {
                        StrSql = StrSql + " and priorityjc_id != " + priorityjc_id + "";
                    }
                    CachedRowSet crs = processQuery(StrSql, 0);
                    if (crs.isBeforeFirst()) {
                        msg = msg + "<br>Similar Priority Found!";
                    }
                    crs.close();
                }
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
        if (priorityjc_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (priorityjc_duehrs.equals("")) {
            msg = msg + "<br>Enter Due Hours!";
        } else if (!isNumeric(priorityjc_duehrs)) {
            msg = msg + "<br>Due Hours must be Numeric!";
        }
        if ((!priorityjc_trigger5_hrs.equals("0"))) {
            if (Double.parseDouble(priorityjc_trigger5_hrs) < Double.parseDouble(priorityjc_trigger4_hrs) || Double.parseDouble(priorityjc_trigger5_hrs) < Double.parseDouble(priorityjc_trigger3_hrs) || Double.parseDouble(priorityjc_trigger5_hrs) < Double.parseDouble(priorityjc_trigger2_hrs) || Double.parseDouble(priorityjc_trigger5_hrs) < Double.parseDouble(priorityjc_trigger1_hrs)) {
                msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
            }
        }
        if (!priorityjc_trigger4_hrs.equals("0")) {
            if (Double.parseDouble(priorityjc_trigger4_hrs) < Double.parseDouble(priorityjc_trigger3_hrs) || Double.parseDouble(priorityjc_trigger4_hrs) < Double.parseDouble(priorityjc_trigger2_hrs) || Double.parseDouble(priorityjc_trigger4_hrs) < Double.parseDouble(priorityjc_trigger1_hrs)) {
                msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
            }
        }
        if (!priorityjc_trigger3_hrs.equals("0")) {
            if (Double.parseDouble(priorityjc_trigger3_hrs) < Double.parseDouble(priorityjc_trigger2_hrs) || Double.parseDouble(priorityjc_trigger3_hrs) < Double.parseDouble(priorityjc_trigger1_hrs)) {
                msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
            }
        }
        if (!priorityjc_trigger2_hrs.equals("0")) {
            if (Double.parseDouble(priorityjc_trigger2_hrs) < Double.parseDouble(priorityjc_trigger1_hrs)) {
                msg = msg + "<br> Level-2 hours should be more than previous Level hours";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                priorityjc_id = ExecuteQuery("Select (coalesce(max(priorityjc_id),0)+1) from " + compdb(comp_id) + "axela_service_jc_priority");
                StrSql = "Insert into " + compdb(comp_id) + "axela_service_jc_priority"
                        + " (priorityjc_id,"
                        + " priorityjc_name,"
                        + " priorityjc_desc,"
                        + " priorityjc_duehrs,"
                        + " priorityjc_rank,"
                        + " priorityjc_trigger1_hrs,"
                        + " priorityjc_trigger2_hrs,"
                        + " priorityjc_trigger3_hrs,"
                        + " priorityjc_trigger4_hrs,"
                        + " priorityjc_trigger5_hrs,"
                        + " priorityjc_entry_id,"
                        + " priorityjc_entry_date)"
                        + " values"
                        + " (" + priorityjc_id + ","
                        + " '" + priorityjc_name + "',"
                        + " '" + priorityjc_desc + "',"
                        + " '" + priorityjc_duehrs + "',"
                        + " (Select (coalesce(max(priorityjc_rank),0)+1) from " + compdb(comp_id) + "axela_service_jc_priority as Rank where 1=1 ),"
                        + " '" + priorityjc_trigger1_hrs + "',"
                        + " '" + priorityjc_trigger2_hrs + "',"
                        + " '" + priorityjc_trigger3_hrs + "',"
                        + " '" + priorityjc_trigger4_hrs + "',"
                        + " '" + priorityjc_trigger5_hrs + "',"
                        + " " + priorityjc_entry_id + ","
                        + " '" + priorityjc_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select *"
                    + " from " + compdb(comp_id) + "axela_service_jc_priority"
                    + " where priorityjc_id = " + priorityjc_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    priorityjc_name = crs.getString("priorityjc_name");
                    priorityjc_desc = crs.getString("priorityjc_desc");
                    priorityjc_duehrs = crs.getString("priorityjc_duehrs");
                    priorityjc_trigger1_hrs = crs.getString("priorityjc_trigger1_hrs");
                    priorityjc_trigger2_hrs = crs.getString("priorityjc_trigger2_hrs");
                    priorityjc_trigger3_hrs = crs.getString("priorityjc_trigger3_hrs");
                    priorityjc_trigger4_hrs = crs.getString("priorityjc_trigger4_hrs");
                    priorityjc_trigger5_hrs = crs.getString("priorityjc_trigger5_hrs");
                    priorityjc_entry_id = crs.getString("priorityjc_entry_id");
                    if (!priorityjc_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(priorityjc_entry_id));
                        entry_date = strToLongDate(crs.getString("priorityjc_entry_date"));
                    }
                    priorityjc_modified_id = crs.getString("priorityjc_modified_id");
                    if (!priorityjc_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(priorityjc_modified_id));
                        modified_date = strToLongDate(crs.getString("priorityjc_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card Priority!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_priority"
                        + " SET"
                        + " priorityjc_name = '" + priorityjc_name + "',"
                        + " priorityjc_desc = '" + priorityjc_desc + "',"
                        + " priorityjc_duehrs = '" + priorityjc_duehrs + "',"
                        + " priorityjc_trigger1_hrs = '" + priorityjc_trigger1_hrs + "',"
                        + " priorityjc_trigger2_hrs = '" + priorityjc_trigger2_hrs + "',"
                        + " priorityjc_trigger3_hrs = '" + priorityjc_trigger3_hrs + "',"
                        + " priorityjc_trigger4_hrs = '" + priorityjc_trigger4_hrs + "',"
                        + " priorityjc_trigger5_hrs = '" + priorityjc_trigger5_hrs + "',"
                        + " priorityjc_modified_id = " + priorityjc_modified_id + ","
                        + " priorityjc_modified_date = '" + priorityjc_modified_date + "'"
                        + " where priorityjc_id = " + priorityjc_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        if (priorityjc_id.equals("1")) {
            msg = "<br>First Record cannot be Deleted ";
            return;
        }
        StrSql = "SELECT jc_priorityjc_id FROM " + compdb(comp_id) + "axela_service_jc where jc_priorityjc_id = " + priorityjc_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Priority is Associated with Job Card!";
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_priority where priorityjc_id = " + priorityjc_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
