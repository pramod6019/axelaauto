//09th may 2014
package axela.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Report_Pickup_GPS_Check extends Connect {

    public String StrSql = "", map = "", gps = "", data = "";
    public static String msg = "";
    public String emp_id = "", branch_id = "", gps_data = "";
    public String comp_id = "", google_api_key = "";
    public String marker = "", marker_content = "", selected = "";
    public int count = 0;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
             emp_id = CNumeric(GetSession("emp_id", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            marker = PadQuotes(request.getParameter("marker"));
            marker_content = PadQuotes(request.getParameter("marker_content"));
            selected = PadQuotes(request.getParameter("selected"));
            google_api_key = PadQuotes(request.getParameter("google_api_key"));
//            SOP("marker = " + marker);
//            SOP("marker_content = " + marker_content);
//            SOP("selected = " + selected);
//            SOP("google_api_key = " + google_api_key);
            if (!marker.equals("") || !marker_content.equals("")
                    || !selected.equals("") || !google_api_key.equals("")) {
                map = "yes";
            }
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
