package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Access extends Connect {

//    public String StrSql = "";
//    public String service_mobile = "", service_email = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
//            String servername = PadQuotes(request.getServerName() + "");
//            if (servername.equals("localhost")) {
//                servername = "localhost.localhost";
//            }
//            StrSql = "select comp_id"
//                    + " from axela_comp"
//                    + " INNER JOIN axela_channel on channel_id=comp_channel_id"
//                    + " where 1 = 1 and channel_active = 1"
//                    + " and CONCAT(comp_subdomain,'.',channel_domain) = '" + servername + "'"; 
//            CachedRowSet crs =processQuery(StrSql, 0);
//            while (crs.next()) {
//                service_mobile = "+" + crs.getString("channel_mobile_support");
//                service_email = crs.getString("channel_email_support");
//                service_email = "<a href=mailto:" + service_email + ">" + service_email + "</a>";
//            }
//            crs.close();

//            if (service_mobile.equals("")) { 
//                StrSql = "select channel_mobile_support, channel_email_support"
//                        + " from axela_channel"  
//                        + " where channel_id=1"; 
////                SOP("StrSql=="+StrSql);
//                CachedRowSet crs1 =processQuery(StrSql, 0);
//                while (crs1.next()) {
//                    service_mobile = "+" + crs1.getString("channel_mobile_support");
//                    service_email = crs1.getString("channel_email_support");
//                    service_email = "<a href=mailto:" + service_email + ">" + service_email + "</a>";
//                }
//                crs1.close(); 
//            }
        } catch (Exception ex) {
            SOPError("AxelaAuto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        doPost(request, response);
    }
}
