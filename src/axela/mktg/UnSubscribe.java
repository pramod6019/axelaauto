package axela.mktg;
//sangita

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class UnSubscribe extends Connect {

    public String StrSql = "";
    public String email_id = "";
    public String unsubcribe = "";
    Encrypt encrypt = new Encrypt();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            email_id = PadQuotes(request.getParameter("email_id"));
            unsubcribe = PadQuotes(request.getParameter("unsubcribe"));
            email_id = CNumeric(encrypt.decrypt(email_id));
            if (unsubcribe.equals("yes") && !email_id.equals("0")) {
                StrSql = "UPDATE axela_mktg_lead"
                        + " INNER JOIN axela_email on email_lead_id = lead_id"
                        + " SET"
                        + " lead_unsubscribed = '1'"
                        + " where email_id = " + email_id;
//                SOP("StrSql--" + StrSql);
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError(ClientName+"===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
