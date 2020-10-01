package axela.mktg;
//sangita

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;
import cloudify.connect.Encrypt;

public class Campaign_Preview extends Connect {

    public String StrSql = "";
    public String campaign_id = "";
    public String campaign_msg = "";
    public String email_id = "";
    Encrypt encrypt = new Encrypt();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            campaign_id = PadQuotes(request.getParameter("campaign_id"));
            email_id = PadQuotes(request.getParameter("email_id"));
            campaign_id = CNumeric(encrypt.decrypt(campaign_id));
            email_id = CNumeric(encrypt.decrypt(email_id));
//            SOP("email_id--" + email_id);
//            SOP("campaign_id--" + campaign_id);
            StrSql = "SELECT campaign_msg"
                    + " FROM axela_mktg_campaign"
                    + " WHERE campaign_id = " + campaign_id;
            campaign_msg = unescapehtml(ExecuteQuery(StrSql));

            if (!email_id.equals("0")) {
                StrSql = "INSERT INTO axela_mktg_campaign_views "
                        + "(view_email_id, view_time, view_remote_host, view_remote_agent)"
                        + " values"
                        + " (" + email_id + ", "
                        + "'" + ToLongDate(kknow()) + "',"
                        + "'" + request.getRemoteHost() + "',"
                        + "'" + request.getHeader("User-Agent") + "')";
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
