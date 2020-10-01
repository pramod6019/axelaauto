package axela.portal;

/**
 * @Gurumurthy TS 11 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageActivityType_Update extends Connect {

    public String add = "";
    public String addB = "";
    public String update = "";
    public String updateB = "";
    public String deleteB = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String QueryString = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String type_id = "0";
    public String type_name = "";
    public String type_desc = "";
    public String type_duehrs = "";
    public String type_rank = "";
    public String type_trigger1_hrs = "0";
    public String type_trigger2_hrs = "0";
    public String type_trigger3_hrs = "0";
    public String type_trigger4_hrs = "0";
    public String type_trigger5_hrs = "0";
    public String type_entry_id = "0";
    public String type_entry_date = "";
    public String type_modified_id = "0";
    public String type_modified_date = "";
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
                 add = PadQuotes(request.getParameter("add"));
                 update = PadQuotes(request.getParameter("update"));
                 addB = PadQuotes(request.getParameter("add_button"));
                 updateB = PadQuotes(request.getParameter("update_button"));
                 deleteB = PadQuotes(request.getParameter("delete_button"));
                 msg = PadQuotes(request.getParameter("msg"));
                 type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
                 QueryString = PadQuotes(request.getQueryString());
                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }
                 if ("yes".equals(add)) {
                     if (!"yes".equals(addB)) {
                         type_name = "";
                         type_desc = "";
                         type_trigger1_hrs = "";
                         type_trigger2_hrs = "";
                         type_trigger3_hrs = "";
                         type_trigger4_hrs = "";
                         type_trigger5_hrs = "";
                         type_duehrs = "";
                     } else {
                         GetValues(request, response);
                         type_entry_id = emp_id;
                         type_entry_date = ToLongDate(kknow());
                         AddFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageactivitytype.jsp?type_id=" + type_id + "&msg=Activity Type Added Successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"yes".equals(updateB) && !"Delete Activity Type".equals(deleteB)) {
                         PopulateFields(response);
                     } else if ("yes".equals(updateB) && !"Delete Activity Type".equals(deleteB)) {
                         GetValues(request, response);
                         type_modified_id = emp_id;
                         type_modified_date = ToLongDate(kknow());
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageactivitytype.jsp?type_id=" + type_id + "&msg=Activity Type Updated Successfully!"));
                         }
                     } else if ("Delete Activity Type".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageactivitytype.jsp?msg=Activity Type Deleted Successfully!"));
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
        type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
        type_name = PadQuotes(request.getParameter("txt_type_name"));
        type_desc = PadQuotes(request.getParameter("txt_type_desc"));
        type_duehrs = PadQuotes(request.getParameter("txt_type_duehrs"));
        type_trigger1_hrs = PadQuotes(request.getParameter("txt_type_trigger1_hrs"));
        type_trigger2_hrs = PadQuotes(request.getParameter("txt_type_trigger2_hrs"));
        type_trigger3_hrs = PadQuotes(request.getParameter("txt_type_trigger3_hrs"));
        type_trigger4_hrs = PadQuotes(request.getParameter("txt_type_trigger4_hrs"));
        type_trigger5_hrs = PadQuotes(request.getParameter("txt_type_trigger5_hrs"));
        if (type_trigger1_hrs.equals("")) {
            type_trigger1_hrs = "0";
        }
        if (type_trigger2_hrs.equals("")) {
            type_trigger2_hrs = "0";
        }
        if (type_trigger3_hrs.equals("")) {
            type_trigger3_hrs = "0";
        }
        if (type_trigger4_hrs.equals("")) {
            type_trigger4_hrs = "0";
        }
        if (type_trigger5_hrs.equals("")) {
            type_trigger5_hrs = "0";
        }
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (type_name.equals("")) {
            msg = "<br>Enter Type Name!";
        } else {
            try {
                if (!type_name.equals("")) {
                    StrSql = "select type_name from " + compdb(comp_id) + "axela_activity_type where type_name = '" + type_name + "'";
                    if (update.equals("yes")) {
                        StrSql = StrSql + " and type_id != " + type_id + "";
                    }
                    CachedRowSet crs =processQuery(StrSql, 0);
                    if (crs.isBeforeFirst()) {
                        msg = msg + "<br>Similar Type Found! ";
                    }
                    crs.close();
                }
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
        if (type_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (type_duehrs.equals("")) {
            msg = msg + "<br>Enter Due Hours!";
        } else if (!isNumeric(type_duehrs)) {
            msg = msg + "<br>Due Hours must be Numeric!";
        }
        if ((!type_trigger5_hrs.equals("0"))) {
            if (Double.parseDouble(type_trigger5_hrs) < Double.parseDouble(type_trigger4_hrs) || Double.parseDouble(type_trigger5_hrs) < Double.parseDouble(type_trigger3_hrs) || Double.parseDouble(type_trigger5_hrs) < Double.parseDouble(type_trigger2_hrs) || Double.parseDouble(type_trigger5_hrs) < Double.parseDouble(type_trigger1_hrs)) {
                msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
            }
        }
        if (!type_trigger4_hrs.equals("0")) {
            if (Double.parseDouble(type_trigger4_hrs) < Double.parseDouble(type_trigger3_hrs) || Double.parseDouble(type_trigger4_hrs) < Double.parseDouble(type_trigger2_hrs) || Double.parseDouble(type_trigger4_hrs) < Double.parseDouble(type_trigger1_hrs)) {
                msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
            }
        }
        if (!type_trigger3_hrs.equals("0")) {
            if (Double.parseDouble(type_trigger3_hrs) < Double.parseDouble(type_trigger2_hrs) || Double.parseDouble(type_trigger3_hrs) < Double.parseDouble(type_trigger1_hrs)) {
                msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
            }
        }
        if (!type_trigger2_hrs.equals("0")) {
            if (Double.parseDouble(type_trigger2_hrs) < Double.parseDouble(type_trigger1_hrs)) {
                msg = msg + "<br> Level-2 hours should be more than previous Level hours";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                type_id = ExecuteQuery("Select (coalesce(max(type_id),0)+1) from " + compdb(comp_id) + "axela_activity_type");

                StrSql = "Insert into " + compdb(comp_id) + "axela_activity_type"
                        + " (type_id,"
                        + " type_name,"
                        + " type_desc,"
                        + " type_duehrs,"
                        + " type_rank,"
                        + " type_trigger1_hrs,"
                        + " type_trigger2_hrs,"
                        + " type_trigger3_hrs,"
                        + " type_trigger4_hrs,"
                        + " type_trigger5_hrs,"
                        + " type_entry_id,"
                        + " type_entry_date)"
                        + " values"
                        + " (" + type_id + ","
                        + " '" + type_name + "',"
                        + " '" + type_desc + "',"
                        + " '" + type_duehrs + "',"
                        + " (Select (coalesce(max(type_rank),0)+1) from " + compdb(comp_id) + "axela_activity_type as Rank where 1=1 ),"
                        + " '" + type_trigger1_hrs + "',"
                        + " '" + type_trigger2_hrs + "',"
                        + " '" + type_trigger3_hrs + "',"
                        + " '" + type_trigger4_hrs + "',"
                        + " '" + type_trigger5_hrs + "',"
                        + " " + type_entry_id + ","
                        + " '" + type_entry_date + "')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select *"
                    + " from " + compdb(comp_id) + "axela_activity_type"
                    + " where type_id=" + type_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    type_name = crs.getString("type_name");
                    type_desc = crs.getString("type_desc");
                    type_duehrs = crs.getString("type_duehrs");
                    type_trigger1_hrs = crs.getString("type_trigger1_hrs");
                    type_trigger2_hrs = crs.getString("type_trigger2_hrs");
                    type_trigger3_hrs = crs.getString("type_trigger3_hrs");
                    type_trigger4_hrs = crs.getString("type_trigger4_hrs");
                    type_trigger5_hrs = crs.getString("type_trigger5_hrs");
                    type_entry_id = crs.getString("type_entry_id");
                    if (!type_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(type_entry_id));
                        entry_date = strToLongDate(crs.getString("type_entry_date"));
                    }
                    type_modified_id = crs.getString("type_modified_id");
                    if (!type_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(type_modified_id));
                        modified_date = strToLongDate(crs.getString("type_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity Type!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_activity_type"
                        + " SET"
                        + " type_name = '" + type_name + "',"
                        + " type_desc = '" + type_desc + "',"
                        + " type_duehrs = '" + type_duehrs + "',"
                        + " type_trigger1_hrs = '" + type_trigger1_hrs + "',"
                        + " type_trigger2_hrs = '" + type_trigger2_hrs + "',"
                        + " type_trigger3_hrs = '" + type_trigger3_hrs + "',"
                        + " type_trigger4_hrs = '" + type_trigger4_hrs + "',"
                        + " type_trigger5_hrs = '" + type_trigger5_hrs + "',"
                        + " type_modified_id = " + type_modified_id + ","
                        + " type_modified_date = '" + type_modified_date + "'"
                        + " where type_id = " + type_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT activity_type_id FROM " + compdb(comp_id) + "axela_activity where activity_type_id = " + type_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Type is Associated with Activity!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_activity_type where type_id = " + type_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
