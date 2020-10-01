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

public class ManageEvalSubHead_Update extends Connect {

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
    public String evalsubhead_id = "0";
    public String evalhead_id = "0";
    public String evalsubhead_name = "";
    public String evalsubhead_rank = "";
    public String evalsubhead_entry_id = "0";
    public String evalsubhead_entry_by = "";
    public String evalsubhead_entry_date = "";
    public String evalsubhead_modified_id = "0";
    public String evalsubhead_modified_by = "";
    public String evalsubhead_modified_date = "";
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
                evalsubhead_id = CNumeric(PadQuotes(request.getParameter("evalsubhead_id")));
                evalhead_id = PadQuotes(request.getParameter("evalhead_id"));
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
                        evalsubhead_entry_id = emp_id;
                        evalsubhead_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalsubhead.jsp?msg=Evaluation Sub Head Added Successfully!"));
                        }
                    }
                }
                if ("yes".equals(update)) {
                    if (!"yes".equals(updateB) && !"Delete Evaluation Sub Head".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Evaluation Sub Head".equals(deleteB)) {
                        GetValues(request, response);

                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalsubhead.jsp?msg=Evaluation Sub Head Updated Successfully!"));
                        }
                    }

                    if ("Delete Evaluation Sub Head".equals(deleteB)) {
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("manageevalsubhead.jsp?msg=Evaluation Sub Head Deleted Successfully!"));
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
        evalhead_id = PadQuotes(request.getParameter("dr_evalhead_id"));
        evalsubhead_name = PadQuotes(request.getParameter("txt_evalsubhead_name"));
        evalsubhead_entry_by = PadQuotes(request.getParameter("entry_by"));
        evalsubhead_modified_by = PadQuotes(request.getParameter("modified_by"));
        evalsubhead_entry_date = PadQuotes(request.getParameter("entry_date"));
        evalsubhead_modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (evalhead_id.equals("0")) {
            msg = "<br>Select Evaluation Head!";
        }
        if (evalsubhead_name.equals("")) {
            msg = msg + "<br>Enter Name!";
        }

        try {
            if (!evalsubhead_name.equals("")) {
                StrSql = "SELECT evalsubhead_name"
                        + " FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
                        + " WHERE evalsubhead_name = '" + evalsubhead_name + "'"
                        + " AND evalsubhead_evalhead_id =" + evalhead_id + ""
                        + " AND evalsubhead_id != " + evalsubhead_id + "";

                if (!ExecuteQuery(StrSql).equals("")) {
                    msg += "<br>Similar Name Found!";
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
                StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_eval_subhead"
                        + "(evalsubhead_evalhead_id,"
                        + " evalsubhead_name,"
                        + " evalsubhead_rank,"
                        + " evalsubhead_entry_id,"
                        + " evalsubhead_entry_date)"
                        + " values("
                        + " " + evalhead_id + ","
                        + " '" + evalsubhead_name + "',"
                        + " (SELECT (COALESCE(MAX(evalsubhead_rank), '0')+1) FROM " + compdb(comp_id) + "axela_preowned_eval_subhead AS Rank WHERE evalsubhead_evalhead_id = " + evalhead_id + " ),"
                        + " " + evalsubhead_entry_id + ","
                        + " '" + evalsubhead_entry_date + "')";

                evalsubhead_id = UpdateQueryReturnID(StrSql);
                //updateQuery(StrSql);    
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT evalhead_id, evalsubhead_name, evalsubhead_entry_id, evalsubhead_entry_date,"
                    + " evalsubhead_modified_id, evalsubhead_modified_date"
                    + " FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
                    + " INNER JOIN " + compdb(comp_id) + "axela_preowned_eval_head ON evalhead_id = evalsubhead_evalhead_id"
                    + " WHERE evalsubhead_id = " + evalsubhead_id + "";

            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    evalhead_id = crs.getString("evalhead_id");
                    evalsubhead_name = crs.getString("evalsubhead_name");
                    evalsubhead_entry_id = crs.getString("evalsubhead_entry_id");
                    if (!evalsubhead_entry_id.equals("")) {
                        evalsubhead_entry_by = Exename(comp_id, Integer.parseInt(evalsubhead_entry_id));
                    }
                    evalsubhead_entry_date = strToLongDate(crs.getString("evalsubhead_entry_date"));
                    evalsubhead_modified_id = crs.getString("evalsubhead_modified_id");
                    if (!evalsubhead_modified_id.equals("")) {
                        evalsubhead_modified_by = Exename(comp_id, Integer.parseInt(evalsubhead_modified_id));
                    }
                    evalsubhead_modified_date = strToLongDate(crs.getString("evalsubhead_modified_date"));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Evaluation Sub Head!"));
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
                evalsubhead_modified_id = emp_id;
                evalsubhead_modified_date = ToLongDate(kknow());
                StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_eval_subhead"
                        + " SET"
                        + " evalsubhead_evalhead_id = " + evalhead_id + ","
                        + " evalsubhead_name = '" + evalsubhead_name + "',"
                        + " evalsubhead_modified_id = " + evalsubhead_modified_id + ","
                        + " evalsubhead_modified_date = '" + evalsubhead_modified_date + "'"
                        + " WHERE evalsubhead_id = " + evalsubhead_id + "";
                //SOP("11=="+StrSql);
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {

        StrSql = "SELECT evaldetails_evalsubhead_id"
                + " FROM " + compdb(comp_id) + "axela_preowned_eval_details"
                + " WHERE evaldetails_evalsubhead_id = " + evalsubhead_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Evaluation Sub Head is associated with Details!";
        }

        if (msg.equals("")) {
            try {
                StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_eval_subhead"
                        + " WHERE evalsubhead_id = " + evalsubhead_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    public String PopulateEvalHead() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT evalhead_id, evalhead_name"
                    + " FROM " + compdb(comp_id) + "axela_preowned_eval_head"
                    + " ORDER BY evalhead_name";
            CachedRowSet crs =processQuery(StrSql, 0);
            Str.append("<option value=0>Select</option>");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("evalhead_id"));
                Str.append(StrSelectdrop(crs.getString("evalhead_id"), evalhead_id));
                Str.append(">").append(crs.getString("evalhead_name")).append("</option>\n");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
