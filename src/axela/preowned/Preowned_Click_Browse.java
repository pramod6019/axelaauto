/* Ved Prakash & $at!sh (22th July 2013 )*/
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Click_Browse extends Connect {

    public String StrSql = "";
    public String StrHTML = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
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

                if (!preowned_id.equals("0")) {
                    StrHTML = ListImages();
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

    public String ListImages() {
        StringBuilder Str = new StringBuilder();
        StrSql = "SELECT img_id, img_title, img_value"
                + " FROM " + compdb(comp_id) + "axela_preowned_img"
                + " WHERE img_preowned_id = " + preowned_id + "";
//        SOP("StrSql----" + StrSql);
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            int count = 0;
            if (crs.isBeforeFirst()) {
                Str.append("\n <table border=0 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                while (crs.next()) {
                    if (count >= 5) {
                        Str.append("</tr>\n<tr align=center>\n");
                        count = 0;
                    }
                    count = count + 1;
                    Str.append("<td valign=top align=center width=\"20%\">\n");
                    if (!crs.getString("img_value").equals("")) {
                        Str.append("<a href=\"../Thumbnail.do?preownedimg=");
                        Str.append(crs.getString("img_value")).append("\"");
                        Str.append(" target=\"_blank\">");
                        Str.append("<img border=\"0\" src=\"../Thumbnail.do?preownedimg=").append(crs.getString("img_value"));
                        Str.append("&width=200\"/>").append("</a>").append("&nbsp;");
                        Str.append("<br>");
                    } else {
                        Str.append("<b>").append(crs.getString("img_title")).append(" (").append(crs.getString("img_id")).append(") (0 Bytes)</b><br>");
                    }
                    Str.append("</td>\n");
                }
                if (count < 5) {
                    for (int i = count; i < 6; i++) {
                        Str.append("<td width=\"20%\">\n&nbsp;</td>\n");
                    }
                }
                Str.append("</tr></table>\n");
            } else {
                Str.append("<font color=red><b>No Image found!</b></font>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }
}
