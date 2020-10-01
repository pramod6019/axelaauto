package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class FAQ extends Connect {

    public static String msg = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String start_time = "";
    public String end_time = "";
    public String ListLink = "";
    public String EnableSearch = "0";
    public String smart = "";
    public String StrHTML = "";
    public String ExportPerm = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_faq_access", request, response);
            StrHTML = FAQSummary(request);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String FAQSummary(HttpServletRequest request) {
        String count = "";
        StringBuilder Str = new StringBuilder();
        try {
            Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
			Str.append("<div class=\"caption\" style=\"float: none\">FAQ Status</div></div><div class=\"portlet-body portlet-empty\">");
			Str.append("<table class=\"table table-bordered\">\n");
            Str.append("<tr>\n");
            Str.append("<th>&nbsp;</th>\n");
            Str.append("<th>Total</th>\n");
            Str.append("<th>Active</th>\n");
            Str.append("<th>Inactive</th>\n");
            Str.append("</tr>");
            Str.append("<tr>\n");
            Str.append("<td>Executive FAQ</td>\n");
            count = ExecuteQuery("select count(faq_id) from " + compdb(comp_id) + "axela_faq where 1=1");
            Str.append("<td><b>").append(count).append("</b></td>\n");
            count = ExecuteQuery("select count(faq_id) from " + compdb(comp_id) + "axela_faq where faq_active = '1'");
            Str.append("<td><b>").append(count).append("</b></td>\n");
            count = ExecuteQuery("select count(faq_id) from " + compdb(comp_id) + "axela_faq where  faq_active = '0'");
            Str.append("<td><b>").append(count).append("</b></td>\n");
            Str.append("</tr>");
            Str.append("</table></div></div>");
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
