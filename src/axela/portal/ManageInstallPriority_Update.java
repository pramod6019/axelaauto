package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInstallPriority_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String comp_id = "0";
    public static String status = "";
    public String StrSql = "";
    public String msg = "";
    public String prioritybalance_id = "";
    public String prioritybalance_name = "";
    public String prioritybalance_desc = "";
    public String prioritybalance_duehrs = "";
    public String prioritybalance_rank = "";
    public String prioritybalance_trigger1_hrs = "0";
    public String prioritybalance_trigger2_hrs = "0";
    public String prioritybalance_trigger3_hrs = "0";
    public String prioritybalance_trigger4_hrs = "0";
    public String prioritybalance_trigger5_hrs = "0";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	 CheckPerm(comp_id, "emp_role_id", request, response);
                 add = PadQuotes(request.getParameter("Add"));
                 update = PadQuotes(request.getParameter("update"));
                 addB = PadQuotes(request.getParameter("add_button"));
                 updateB = PadQuotes(request.getParameter("update_button"));
                 deleteB = PadQuotes(request.getParameter("delete_button"));
                 msg = PadQuotes(request.getParameter("msg"));

                 if (add == null) {
                     add = "";
                 }
                 if (update == null) {
                     update = "";
                 }
                 if (addB == null) {
                     addB = "";
                 }
                 if (updateB == null) {
                     updateB = "";
                 }
                 if (deleteB == null) {
                     deleteB = "";
                 }
                 if (msg == null) {
                     msg = "";
                 }

                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }

                 if ("yes".equals(add)) {
                     if (!"Add Priority".equals(addB)) {
                         prioritybalance_name = "";
                         prioritybalance_desc = "";
                         prioritybalance_trigger1_hrs = "";
                         prioritybalance_trigger2_hrs = "";
                         prioritybalance_trigger3_hrs = "";
                         prioritybalance_trigger4_hrs = "";
                         prioritybalance_trigger5_hrs = "";
                         prioritybalance_duehrs = "";
                     } else {
                         GetValues(request, response);
                         AddFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageinstallpriority.jsp?add=yes&msg= Priority added successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"Update Priority".equals(updateB) && !"Delete Priority".equals(deleteB)) {
                         prioritybalance_id = PadQuotes(request.getParameter("prioritybalance_id"));
                         if (prioritybalance_id.equals("0") || !isNumeric(prioritybalance_id)) {
                             prioritybalance_id = "0";
                         }
                         PopulateFields(response);
                     } else if ("Update Priority".equals(updateB) && !"Delete Priority".equals(deleteB)) {

                         GetValues(request, response);
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageinstallpriority.jsp?prioritybalance_id=" + prioritybalance_id + "&msg= Priority updated successfully!" + msg + ""));
                         }
                     } else if ("Delete Priority".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("manageinstallpriority.jsp?msg= Priority deleted successfully!"));
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
        prioritybalance_id = PadQuotes(request.getParameter("prioritybalance_id"));
        prioritybalance_name = PadQuotes(request.getParameter("txt_prioritybalance_name"));
        prioritybalance_desc = PadQuotes(request.getParameter("txt_prioritybalance_desc"));
        prioritybalance_duehrs = PadQuotes(request.getParameter("txt_prioritybalance_duehrs"));
        prioritybalance_trigger1_hrs = PadQuotes(request.getParameter("txt_prioritybalance_trigger1_hrs"));
        prioritybalance_trigger2_hrs = PadQuotes(request.getParameter("txt_prioritybalance_trigger2_hrs"));
        prioritybalance_trigger3_hrs = PadQuotes(request.getParameter("txt_prioritybalance_trigger3_hrs"));
        prioritybalance_trigger4_hrs = PadQuotes(request.getParameter("txt_prioritybalance_trigger4_hrs"));
        prioritybalance_trigger5_hrs = PadQuotes(request.getParameter("txt_prioritybalance_trigger5_hrs"));
        if (prioritybalance_trigger1_hrs.equals("")) {
            prioritybalance_trigger1_hrs = "0";
        }
        if (prioritybalance_trigger2_hrs.equals("")) {
            prioritybalance_trigger2_hrs = "0";
        }
        if (prioritybalance_trigger3_hrs.equals("")) {
            prioritybalance_trigger3_hrs = "0";
        }
        if (prioritybalance_trigger4_hrs.equals("")) {
            prioritybalance_trigger4_hrs = "0";
        }
        if (prioritybalance_trigger5_hrs.equals("")) {
            prioritybalance_trigger5_hrs = "0";
        }
    }

    protected void CheckForm() {
        // SOP("inside check form");
        msg = "";
        if (prioritybalance_name.equals("")) {
            msg = "<br>Enter Priority!";
        } else {
            try {
                if (update.equals("yes") && !prioritybalance_name.equals("")) {
                    StrSql = "select prioritybalance_name from " + compdb(comp_id) + "axela_priority_balance where prioritybalance_name = '" + prioritybalance_name + "' and prioritybalance_id!=" + prioritybalance_id + "";
                } else if (add.equals("yes") && !prioritybalance_name.equals("")) {
                    StrSql = "select prioritybalance_name from " + compdb(comp_id) + "axela_priority_balance where prioritybalance_name = '" + prioritybalance_name + "'";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar  Priority found! ";
                }
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
        if (prioritybalance_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (prioritybalance_duehrs.equals("")) {
            msg = msg + "<br>Enter Due Hours!";
        } else if (!isNumeric(prioritybalance_duehrs)) {
            msg = msg + "<br>Due Hours must be Numeric!";
        }
        if ((!prioritybalance_trigger5_hrs.equals("0"))) {
            if (Double.parseDouble(prioritybalance_trigger5_hrs) < Double.parseDouble(prioritybalance_trigger4_hrs) || Double.parseDouble(prioritybalance_trigger5_hrs) < Double.parseDouble(prioritybalance_trigger3_hrs) || Double.parseDouble(prioritybalance_trigger5_hrs) < Double.parseDouble(prioritybalance_trigger2_hrs) || Double.parseDouble(prioritybalance_trigger5_hrs) < Double.parseDouble(prioritybalance_trigger1_hrs)) {
                msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
            }
        }
        if (!prioritybalance_trigger4_hrs.equals("0")) {
            if (Double.parseDouble(prioritybalance_trigger4_hrs) < Double.parseDouble(prioritybalance_trigger3_hrs) || Double.parseDouble(prioritybalance_trigger4_hrs) < Double.parseDouble(prioritybalance_trigger2_hrs) || Double.parseDouble(prioritybalance_trigger4_hrs) < Double.parseDouble(prioritybalance_trigger1_hrs)) {
                msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
            }
        }
        if (!prioritybalance_trigger3_hrs.equals("0")) {
            if (Double.parseDouble(prioritybalance_trigger3_hrs) < Double.parseDouble(prioritybalance_trigger2_hrs) || Double.parseDouble(prioritybalance_trigger3_hrs) < Double.parseDouble(prioritybalance_trigger1_hrs)) {
                msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
            }
        }
        if (!prioritybalance_trigger2_hrs.equals("0")) {
            if (Double.parseDouble(prioritybalance_trigger2_hrs) < Double.parseDouble(prioritybalance_trigger1_hrs)) {
                msg = msg + "<br> Level-2 hours should be more than previous Level hours";
            }
        }

    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "insert into " + compdb(comp_id) + "axela_priority_balance"
                        + "(prioritybalance_id,"
                        + "prioritybalance_name,"
                        + "prioritybalance_desc,"
                        + "prioritybalance_duehrs,"
                        + "prioritybalance_rank,"
                        + "prioritybalance_trigger1_hrs, "
                        + "prioritybalance_trigger2_hrs, "
                        + "prioritybalance_trigger3_hrs, "
                        + "prioritybalance_trigger4_hrs, "
                        + "prioritybalance_trigger5_hrs "
                        + ")"
                        + "values	"
                        + "((Select (coalesce(max(prioritybalance_id),0)+1) from " + compdb(comp_id) + "axela_priority_balance as prioritybalance_id),"
                        + "'" + prioritybalance_name + "',"
                        + "'" + prioritybalance_desc + "',"
                        + "'" + prioritybalance_duehrs + "',"
                        + "(Select (coalesce(max(prioritybalance_rank),0)+1) from " + compdb(comp_id) + "axela_priority_balance as ID where 1=1),"
                        + "'" + prioritybalance_trigger1_hrs + "', "
                        + "'" + prioritybalance_trigger2_hrs + "', "
                        + "'" + prioritybalance_trigger3_hrs + "', "
                        + "'" + prioritybalance_trigger4_hrs + "', "
                        + "'" + prioritybalance_trigger5_hrs + "' "
                        + ")";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select * from " + compdb(comp_id) + "axela_priority_balance where  prioritybalance_id=" + prioritybalance_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    prioritybalance_id = crs.getString("prioritybalance_id");
                    prioritybalance_name = crs.getString("prioritybalance_name");
                    prioritybalance_desc = crs.getString("prioritybalance_desc");
                    prioritybalance_duehrs = crs.getString("prioritybalance_duehrs");
                    prioritybalance_trigger1_hrs = crs.getString("prioritybalance_trigger1_hrs");
                    prioritybalance_trigger2_hrs = crs.getString("prioritybalance_trigger2_hrs");
                    prioritybalance_trigger3_hrs = crs.getString("prioritybalance_trigger3_hrs");
                    prioritybalance_trigger4_hrs = crs.getString("prioritybalance_trigger4_hrs");
                    prioritybalance_trigger5_hrs = crs.getString("prioritybalance_trigger5_hrs");
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Install Priority!"));
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_priority_balance"
                        + " SET "
                        + "prioritybalance_name = '" + prioritybalance_name + "',"
                        + "prioritybalance_desc = '" + prioritybalance_desc + "',"
                        + "prioritybalance_duehrs = '" + prioritybalance_duehrs + "',"
                        + "prioritybalance_trigger1_hrs = '" + prioritybalance_trigger1_hrs + "', "
                        + "prioritybalance_trigger2_hrs = '" + prioritybalance_trigger2_hrs + "', "
                        + "prioritybalance_trigger3_hrs = '" + prioritybalance_trigger3_hrs + "', "
                        + "prioritybalance_trigger4_hrs = '" + prioritybalance_trigger4_hrs + "', "
                        + "prioritybalance_trigger5_hrs = '" + prioritybalance_trigger5_hrs + "' "
                        + " where  prioritybalance_id = " + prioritybalance_id + " ";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        if (prioritybalance_id.equals("1")) {
            msg = "<br>First record cannot deleted ";
            return;
        }
        StrSql = "SELECT student_priorityenq_id from " + compdb(comp_id) + "axela_student where student_priorityenq_id = " + prioritybalance_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = "<br>Priority is associated with a Student!";
            return;
        }
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_priority_balance where prioritybalance_id =" + prioritybalance_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
