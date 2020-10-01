package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Faqexecutive_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String faq_id = "0";
    public String faq_cat_id = "0";
    public String faq_question = "";
    public String faq_answer = "";
    public String faq_active = "1";
    public String faq_rank = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String faq_entry_id = "0";
    public String entry_by = "";
    public String faq_entry_date = "";
    public String entry_date = "";
    public String faq_modified_id = "0";
    public String modified_by = "";
    public String faq_modified_date = "";
    public String modified_date = "";
    public String QueryString = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_faq_access", request, response);
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            add = PadQuotes(request.getParameter("Add"));
            update = PadQuotes(request.getParameter("Update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            msg = PadQuotes(request.getParameter("msg"));
            faq_id = CNumeric(PadQuotes(request.getParameter("faq_id")));
            QueryString = PadQuotes(request.getQueryString());

            if (add.equals("yes")) {
                status = "Add";
            } else if (update.equals("yes")) {
                status = "Update";
            }
            if ("yes".equals(add)) {
                if (!"Add FAQ".equals(addB)) {
                } else {
                    CheckPerm(comp_id, "emp_faq_add", request, response);
                    GetValues(request, response);
                    faq_entry_id = emp_id;
                    faq_entry_date = ToLongDate(kknow());
                    AddFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("faqexecutive-list.jsp?faq_cat_id=" + faq_cat_id + "&faq_id=" + faq_id + "&msg=FAQ added successfully!"));
                    }
                }
            }
            if ("yes".equals(update)) {
                if (!"Update FAQ".equals(updateB) && !"Delete FAQ".equals(deleteB)) {
                    PopulateFields();
                } else if ("Update FAQ".equals(updateB) && !"Delete FAQ".equals(deleteB)) {
                    CheckPerm(comp_id, "emp_faq_edit", request, response);
                    GetValues(request, response);
                    faq_modified_id = emp_id;
                    faq_modified_date = ToLongDate(kknow());
                    UpdateFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("faqexecutive-list.jsp?faq_cat_id=" + faq_cat_id + "&faq_id=" + faq_id + "&msg=FAQ updated successfully!" + msg + ""));
                    }
                } else if ("Delete FAQ".equals(deleteB)) {
                    CheckPerm(comp_id, "emp_faq_delete", request, response);
                    GetValues(request, response);
                    DeleteFields();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        response.sendRedirect(response.encodeRedirectURL("faqexecutive-list.jsp?msg=FAQ deleted successfully!"));
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
        faq_cat_id = PadQuotes(request.getParameter("dr_faq_cat_id"));
        faq_id = PadQuotes(request.getParameter("faq_id"));
        faq_question = PadQuotes(request.getParameter("txt_faq_question"));
        faq_answer = PadQuotes(request.getParameter("txt_faq_answer"));
        faq_active = PadQuotes(request.getParameter("chk_active"));
        if (faq_active.equals("on")) {
            faq_active = "1";
        } else {
            faq_active = "0";
        }
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (faq_cat_id.equals("0")) {
            msg = "<br>Select Category!";
        }
        if (faq_question.equals("")) {
            msg = msg + "<br>Enter Question!";
        }
        try {
            if (!faq_question.equals("")) {
                if (update.equals("yes")) {
                    StrSql = "select faq_question from " + compdb(comp_id) + "axela_faq where faq_question = '" + faq_question + "' and faq_id!=" + faq_id + "";
                } else if (add.equals("yes") && !faq_question.equals("")) {
                    StrSql = "select faq_question from " + compdb(comp_id) + "axela_faq where  faq_question = '" + faq_question + "'";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Question found!";
                }
                crs.close();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        if (faq_answer.equals("")) {
            msg = msg + "<br>Enter Answer!";
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "insert into " + compdb(comp_id) + "axela_faq"
                        + "(faq_id,"
                        + "faq_cat_id,"
                        + "faq_question,"
                        + "faq_answer,"
                        + "faq_rank,"
                        + "faq_active,"
                        + "faq_entry_id,"
                        + "faq_entry_date,"
                        + "faq_modified_id,"
                        + "faq_modified_date "
                        + ")"
                        + "values	"
                        + "((Select (coalesce(max(faq_id),0)+1) from " + compdb(comp_id) + "axela_faq as faq_id ),"
                        + "" + faq_cat_id + ","
                        + "'" + faq_question + "',"
                        + "'" + faq_answer + "',"
                        + "(Select (coalesce(max(faq_rank),0)+1) from " + compdb(comp_id) + "axela_faq as ID where 1=1 ),"
                        + "'" + faq_active + "',"
                        + "'" + faq_entry_id + "',"
                        + "'" + faq_entry_date + "',"
                        + "'0',"
                        + "'')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields() {
        try {
            StrSql = "select * from " + compdb(comp_id) + "axela_faq where  faq_id=" + faq_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                faq_id = crs.getString("faq_id");
                faq_cat_id = crs.getString("faq_cat_id");
                faq_question = crs.getString("faq_question");
                faq_answer = crs.getString("faq_answer");
                faq_active = crs.getString("faq_active");
                faq_entry_id = crs.getString("faq_entry_id");
                faq_entry_date = crs.getString("faq_entry_date");
                faq_modified_id = crs.getString("faq_modified_id");
                faq_modified_date = crs.getString("faq_modified_date");
                if (!faq_entry_id.equals("")) {
                    entry_by = Exename(comp_id, Integer.parseInt(faq_entry_id));
                    entry_date = strToLongDate(crs.getString("faq_entry_date"));
                }
                if (!faq_modified_id.equals("0")) {
                    modified_by = Exename(comp_id, Integer.parseInt(faq_modified_id));
                    modified_date = strToLongDate(crs.getString("faq_modified_date"));
                }
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_faq"
                        + " SET "
                        + "faq_cat_id = '" + faq_cat_id + "',"
                        + "faq_question = '" + faq_question + "',"
                        + "faq_answer = '" + faq_answer + "',"
                        + "faq_active = '" + faq_active + "',"
                        + "faq_modified_id  ='" + faq_modified_id + "',"
                        + "faq_modified_date  ='" + faq_modified_date + "' "
                        + "where  faq_id = " + faq_id + " ";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_faq where  faq_id =" + faq_id + "";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    public String PopulateCategory() {
        try {
            StringBuilder Str = new StringBuilder();
            StrSql = "SELECT cat_id, cat_name from " + compdb(comp_id) + "axela_faq_cat "
                    + " where 1 =1 order by cat_name ";
            CachedRowSet crs =processQuery(StrSql, 0);
            Str.append("<option value=0 >Select</option>");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("cat_id")).append("");
                Str.append(StrSelectdrop(crs.getString("cat_id"), faq_cat_id));
                Str.append(">").append(crs.getString("cat_name")).append("</option> \n");
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
