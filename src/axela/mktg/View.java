package axela.mktg;
//sangita

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class View extends Connect {

    public String email_id = "";
    public String StrSql = "";
    public String view_time = "";
    public String view_remote_host = "";
    public String view_remote_agent = "";
    Encrypt encrypt = new Encrypt();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            email_id = PadQuotes(request.getParameter("email_id"));
            email_id = CNumeric(encrypt.decrypt(email_id));

            StrSql = "INSERT INTO axela_email_campaign_views "
                    + "(view_email_id, view_time, view_remote_host, view_remote_agent)"
                    + " values"
                    + " (" + email_id + ", "
                    + "'" + ToLongDate(kknow()) + "',"
                    + "'" + request.getRemoteHost() + "',"
                    + "'" + request.getHeader("User-Agent") + "')";
            updateQuery(StrSql);

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
