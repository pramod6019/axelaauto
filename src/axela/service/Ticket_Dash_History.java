package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Dash_History extends Connect {

    public String ticket_id = "0";
    public String StrHTMLHistory = "";
    public String StrSql = "";
    public String comp_id = "0";
    public String CountSql = "";
    public String StrJoin = "";
    public String ExeAccess = "";
    public String ticket_subject = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            //CheckPerm(comp_id, "emp_ticket_access", request, response);
            ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
            StrSql = "select ticket_subject "
                    + " from " + compdb(comp_id) + "axela_service_ticket "
                    + " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
                    + " where 1=1 and ticket_id =" + ticket_id + ExeAccess + ""
                    + " group by ticket_id "
                    + " order by ticket_id desc ";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    ticket_subject = crs.getString("ticket_subject");
                }
                StrHTMLHistory = ListHistoryData();
            } else {
                response.sendRedirect("../portal/error.jsp?msg=Invalid Ticket!");
            }
            crs.close();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String ListHistoryData() {
        StringBuilder Str = new StringBuilder();
        int TotalRecords = 0;
        StrSql = "select * ";

        CountSql = "select count(history_id) ";

        StrJoin = " from " + compdb(comp_id) + "axela_service_ticket_history "
                + " where history_ticket_id='" + ticket_id + "'";

        StrSql = StrSql + StrJoin;
        CountSql = CountSql + StrJoin;

        StrSql = StrSql + " order by history_id desc ";

        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
//         SOP("=="+StrSql);
        if (TotalRecords != 0) {
            CachedRowSet crs = processQuery(StrSql, 0);
            try {
                int j = 0;
                String col = "white";
                String altcol = "#FFFFCC";
                String bgcol = col;
                Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=listtable>");
                Str.append("<tr align=center>\n");
                Str.append("<th>Date</th>");
                Str.append("<th>Action By</th>");
                Str.append("<th>Type of Action</th>");
                Str.append("<th>Old Value</th>");
                Str.append("<th>New Value</th>");
                Str.append("</tr>\n");

                while (crs.next()) {
                    if (j == 0) {
                        j = 1;
                        bgcol = col;
                    } else {
                        j = 0;
                        bgcol = altcol;
                    }
                    Str.append("<tr onmouseover=\"this.style.background='#eeeeee';\" onmouseout=\"this.style.background='").append(bgcol).append("';\" bgcolor='").append(bgcol).append("'>\n");
                    Str.append("<td valign=top align=center >").append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
                    Str.append("<td valign=top align=left >").append(Exename(comp_id, crs.getInt("history_emp_id"))).append("</td>");
                    Str.append("<td valign=top align=left >").append(crs.getString("history_actiontype")).append(" </td>");
                    Str.append("<td valign=top align=left >").append(crs.getString("history_oldvalue")).append("</td>");
                    Str.append("<td valign=top align=left >").append(crs.getString("history_newvalue")).append("</td>");
                    Str.append("</tr>" + "\n");
                }
                Str.append("</table>\n");
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
        return Str.toString();
    }
}
