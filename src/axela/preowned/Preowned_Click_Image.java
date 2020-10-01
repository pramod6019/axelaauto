/* Ved Prakash & $at!sh (22th July 2013 )*/
package axela.preowned;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Preowned_Click_Image extends Connect {

    public String StrSql = "";
    public String LinkHeader = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String preowned_id = "0";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                CheckPerm(comp_id, "emp_preowned_access", request, response);
                preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
                LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../preowned/index.jsp>Pre-owned</a> &gt; <a href=../preowned/preowned.jsp>Pre-owned</a> &gt; <a href=../preowned/preowned-list.jsp?all=yes>List Pre-owned</a> &gt; <a href=../preowned/preowned-list.jsp?preowned_id=" + preowned_id + "> Pre-owned ID: " + preowned_id + "</a> &gt; <a href=../preowned/preowned-dash-image.jsp?preowned_id=" + preowned_id + ">List Images</a>:";
            }
            
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
