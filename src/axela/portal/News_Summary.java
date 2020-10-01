package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class News_Summary extends Connect {

    public String StrHTML = "", emp_id = "", branch_id = "";
    public String BranchAccess = "";
    public String comp_id = "0";
    public String StrSql = "";
    public String news_id = "0", news_topic = "";
    public String honews_id = "0";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	 emp_id = CNumeric(GetSession("emp_id", request));
                 branch_id = CNumeric(GetSession("emp_branch_id", request));
                 BranchAccess = GetSession("BranchAccess", request);
                 news_id = CNumeric(PadQuotes(request.getParameter("news_id")));
                 honews_id = CNumeric(PadQuotes(request.getParameter("honews_id")));
                 if (!news_id.equals("0")) {
                     StrSql = "Select news_id, news_topic, news_desc, news_date"
                             + " from " + compdb(comp_id) + "axela_news_branch"
                             + " where news_active = '1'"
                             + " and news_id = " + news_id + ""
                             + " group by news_id";
                 } else if (!honews_id.equals("0")) {
                     StrSql = "Select news_id, news_topic, news_desc, news_date"
                             + " from " + compdb(comp_id) + "axela_news_ho"
                             + " where news_active = '1'"
                             + " and news_id = " + honews_id + ""
                             + " group by news_id";
                 }
//                 SOP("StrSql==" + StrSqlBreaker(StrSql));
                 if (!news_id.equals("0") || !honews_id.equals("0")) {
                     StrHTML = HoNewsDetails();
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

    public String HoNewsDetails() {
        StringBuilder Str = new StringBuilder();
        Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
        try {
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    if (!crs.getString("news_topic").equals("")) {
                        Str.append("<tr><th>").append(crs.getString("news_topic")).append(" (").append(strToShortDate(crs.getString("news_date"))).append(") " + "</th></tr>\n");
                    }
                    if (!crs.getString("news_desc").equals("")) {
                        Str.append("<tr><td width=70% height=200 align=left valign=top>").append(crs.getString("news_desc")).append("</td></tr>\n");
                    }
                }
                Str.append("</table>\n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }
}
