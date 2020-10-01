package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_Configure_Format extends Connect {

    public String updateB = "";
    public String comp_id = "0";
    public String StrSql = "";
    public String msg = "";
    public String email = "";
    public String sms = "";
    public String FormatName = "";
    public String subjectName = "";
    public String subject = "";
    public String status = "";
    public String title = "";
    public String format_desc = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_role_id", request, response);
            updateB = PadQuotes(request.getParameter("update_button"));
            msg = PadQuotes(request.getParameter("msg"));
            status = PadQuotes(request.getParameter("status"));
            email = PadQuotes(request.getParameter("email"));
            sms = PadQuotes(request.getParameter("sms"));
            FormatName = PadQuotes(request.getParameter("opt"));
            SOP(FormatName);
            if (updateB == null) {
                updateB = "";
            }
            if (msg == null) {
                msg = "";
            }
            if (email == null) {
                email = "";
            }
            if (sms == null) {
                sms = "";
            }
            if (FormatName == null) {
                FormatName = "";
            }
            if (format_desc == null) {
                format_desc = "";
            }
            if (email.equals("yes")) {
                subjectName = FormatName.substring(0, FormatName.lastIndexOf("_")) + "_sub";
            }
            if (email.equals("yes")) {
                title = status + " Email";
            }
            if (sms.equals("yes")) {
                title = status + " SMS";
            }
            if (!"Update".equals(updateB)) {
                PopulateFields();
            }
            if ("Update".equals(updateB)) {
                GetValues(request, response);
                CheckForm();
                if (msg.equals("")) {
                    UpdateFields();
                    response.sendRedirect(response.encodeRedirectURL("manage-configure.jsp?&msg=Format details for " + status + " updated successfully!"));
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
        subject = PadQuotes(request.getParameter("txt_subject"));
        format_desc = PadQuotes(request.getParameter("txt_format"));
    }

    protected void CheckForm() {
        if (sms.equals("yes") && format_desc.length() > 160) {
            format_desc = format_desc.substring(0, 159);
        }
        if (email.equals("yes") && subject.equals("")) {
            msg = "<br>Enter Subject!";
        }
        if (format_desc.equals("")) {
            msg = msg + "<br>Enter Description!";
        }
        if (!msg.equals("")) {
            msg = "Error!" + msg;
        }
    }

    protected void PopulateFields() {

        StrSql = "select " + FormatName;
        if (email.equals("yes")) {
            StrSql = StrSql + ", " + subjectName;
        }
        StrSql = StrSql + " from " + compdb(comp_id) + "axela_comp";
        SOP("StrSql" + StrSql);
        try {
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                format_desc = crs.getString(FormatName);
                if (email.equals("yes")) {
                    subject = crs.getString(subjectName);
                }
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields() {

        try {
            StrSql = "UPDATE " + compdb(comp_id) + "axela_comp Set "
                    + "" + FormatName + " = '" + format_desc + "' ";
            if (email.equals("yes")) {
                StrSql = StrSql + "," + subjectName + " = '" + subject + "'";
            }
            //  SOP("SqlStr=="+StrSql);
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }

    }
}
