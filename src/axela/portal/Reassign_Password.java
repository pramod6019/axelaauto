package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Reassign_Password extends Connect {

    public String submitB = "";
    public String cancelB = "";
    public String msg = "";
    public String StrSql = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String emp_email = "";
    public String new_password = "";
    public String confirm_password = "";
    public String emp_name = "";
//    public String desc = "", from_address = "", to_address = "", from = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	submitB = PadQuotes(request.getParameter("submit_button"));
                cancelB = PadQuotes(request.getParameter("cancel_button"));
                new_password = PadQuotes(request.getParameter("txt_new_password"));
                confirm_password = PadQuotes(request.getParameter("txt_confirm_password"));
                emp_id = CNumeric(GetSession("emp_id", request));
                emp_email = ExecuteQuery("SELECT emp_email1 FROM " + compdb(comp_id) + "axela_emp"
                        + " WHERE emp_id = " + emp_id + "");
                
                if (submitB == null) {
                    submitB = "";
                }
                if ("Continue".equals(submitB)) {
                    CheckFields();
                    if (msg.equals("")) {
                        StrSql = "UPDATE " + compdb(comp_id) + "axela_emp SET emp_upass = '" + new_password + "'"
                                + " WHERE emp_id = " + emp_id + "";
                        updateQuery(StrSql);
                        response.sendRedirect(response.encodeRedirectURL("../portal/home.jsp"));
                    }
                }
                if ("Cancel".equals(cancelB)) {
                    response.sendRedirect(response.encodeRedirectURL("../portal/signout.jsp"));
                }
            }
            
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void CheckFields() {
        msg = "";

        if (new_password.equals("")) {
            msg = msg + "<br>Your new password cannot be blank!";
        }
        if (!new_password.equals("") && new_password.length() < 8) {
            msg = msg + "<br>Your new password cannot be less than 8 Characters!";
        }
        if (confirm_password.equals("")) {
            msg = msg + "<br>Please check your password confirmation!";
        }
        if (!confirm_password.equals("") && !confirm_password.equals(new_password)) {
            msg = msg + "<br>Your new passwords does not match!";
        }
//        if (!new_password.equals("") && !confirm_password.equals("")) {
//            if (!new_password.equals(confirm_password)) {
//                msg = "You must enter same password twice in order to confirm it!";
//            }
//        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doPost(request, response);
    }
}
