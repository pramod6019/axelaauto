package axela.preowned;
/*
 * @author Dilip Kumar (10th July 2013)
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageEvalHead_Update extends Connect {

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
    public String evalhead_id = "0";
    public String evalhead_name = "";
    public String evalhead_rank = "";
    public String evalhead_entry_id = "0";
    public String evalhead_entry_by = "";
    public String evalhead_entry_date = "";
    public String evalhead_modified_id = "0";
    public String evalhead_modified_by = "";
    public String evalhead_modified_date = "";
    public String entry_by = "", modified_by = "";
    public String QueryString = "";

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
                evalhead_id = CNumeric(PadQuotes(request.getParameter("evalhead_id")));
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
                        evalhead_entry_id = emp_id;
                        evalhead_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalhead.jsp?msg=Evaluation Head Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Evaluation Head".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Evaluation Head".equals(deleteB)) {
                        GetValues(request, response);

                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalhead.jsp?msg=Evaluation Head Updated Successfully!"));
                        }
                    }

                    if ("Delete Evaluation Head".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalhead.jsp?msg=Evaluation Head Deleted Successfully!"));
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

        evalhead_name = PadQuotes(request.getParameter("txt_evalhead_name"));
        evalhead_entry_by = PadQuotes(request.getParameter("entry_by"));
        evalhead_modified_by = PadQuotes(request.getParameter("modified_by"));
        evalhead_entry_date = PadQuotes(request.getParameter("entry_date"));
        evalhead_modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (evalhead_name.equals("")) {
            msg = "<br>Enter Name!";
        }
        try {
            if (!evalhead_name.equals("")) {
                StrSql = "SELECT evalhead_name"
                        + " FROM " + compdb(comp_id) + "axela_preowned_eval_head"
                        + " WHERE evalhead_name = '" + evalhead_name + "'";
                if (update.equals("yes")) {
                    StrSql = StrSql + " AND evalhead_id != " + evalhead_id + "";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Name Found!";
                }
                crs.close();
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
                evalhead_id = ExecuteQuery("SELECT COALESCE(MAX(evalhead_id),0)+1 AS evalhead_id FROM " + compdb(comp_id) + "axela_preowned_eval_head");

                StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_head"
                        + "(evalhead_id,"
                        + " evalhead_name,"
                        + " evalhead_rank,"
                        + " evalhead_entry_id,"
                        + " evalhead_entry_date)"
                        + " values"
                        + " (" + evalhead_id + ","
                        + " '" + evalhead_name + "',"
                        + " (SELECT (COALESCE(MAX(evalhead_rank), '0')+1) FROM " + compdb(comp_id) + "axela_preowned_eval_head AS Rank WHERE 1 = 1 ),"
                        + " " + evalhead_entry_id + ","
                        + " '" + evalhead_entry_date + "')";
                //SOP("StrSql==" + StrSql);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT evalhead_name, evalhead_entry_id, evalhead_entry_date, evalhead_modified_id,"
                    + " evalhead_modified_date"
                    + " FROM " + compdb(comp_id) + "axela_preowned_eval_head"
                    + " WHERE evalhead_id = " + evalhead_id + "";

            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    evalhead_name = crs.getString("evalhead_name");
                    evalhead_entry_id = crs.getString("evalhead_entry_id");
                    if (!evalhead_entry_id.equals("")) {
                        evalhead_entry_by = Exename(comp_id, Integer.parseInt(evalhead_entry_id));
                    }
                    evalhead_entry_date = strToLongDate(crs.getString("evalhead_entry_date"));
                    evalhead_modified_id = crs.getString("evalhead_modified_id");
                    if (!evalhead_modified_id.equals("")) {
                        evalhead_modified_by = Exename(comp_id, Integer.parseInt(evalhead_modified_id));
                    }
                    evalhead_modified_date = strToLongDate(crs.getString("evalhead_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Evaluation Head!"));
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
                evalhead_modified_id = emp_id;
                evalhead_modified_date = ToLongDate(kknow());
                StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_head"
                        + " SET"
                        + " evalhead_name = '" + evalhead_name + "',"
                        + " evalhead_modified_id = " + evalhead_modified_id + ","
                        + " evalhead_modified_date = '" + evalhead_modified_date + "'"
                        + " WHERE evalhead_id = " + evalhead_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT evalsubhead_evalhead_id"
                + " FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
                + " WHERE evalsubhead_evalhead_id = " + evalhead_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Evaluation Head is associated with Evaluation Sub Head!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval_head"
                        + " WHERE evalhead_id = " + evalhead_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }
}
