package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Activeusers extends Connect {

    public String user_jsessionid = "";
    public String user_emp_id = "";
    public String user_cse = "";
    public String comp_id = "0";
    public String user_logintime = "";
    public String user_ip = "";
    public String user_agent = "";
    public String StrHTML = "";
    public String RecCountDisplay = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            	CheckPerm(comp_id, "emp_role_id", request, response);
                Listdata();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public String Listdata() {
        int TotalRecords = 0;
        int j = 0;
        int count = 0;
        String StrSql = "";
        String CountSql = "";
        StringBuilder Str = new StringBuilder();

        StrSql = "select * from " + compdb(comp_id) + "axela_emp_user where 1=1 order by user_logintime desc";
        CountSql = "select count(distinct user_jsessionid) from " + compdb(comp_id) + "axela_emp_user where 1=1";

        // SOP("StrSql in active-users$$$$$$$$"+StrSql);
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
        if (TotalRecords != 0) {
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Executive</th>\n");
                Str.append("<th>Remote Host</th>\n");
                Str.append("<th>Remote Agent</th>\n");
                Str.append("<th>SignIn Time</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center><b>" + count + ".</b></td>\n");
                    if (!Exename(comp_id, crs.getInt("user_emp_id")).equals("")) {
                        Str.append("<td valign=top align=center>" + Exename(comp_id, crs.getInt("user_emp_id")) + "</td>\n");
                    } else {
                        Str.append("<td valign=top align=center>&nbsp;</td>");
                    }
                    if (crs.getString("user_ip") != null) {
                        Str.append("<td valign=top align=center>" + crs.getString("user_ip") + "</td>\n");
                    } else {
                        Str.append("<td valign=top align=center>&nbsp;</td>");
                    }
                    if (crs.getString("user_agent") != null) {
                        Str.append("<td valign=top align=center>" + crs.getString("user_agent") + "</td>");
                    } else {
                        Str.append("<td valign=top align=center>&nbsp;</td>");
                    }
                    if (crs.getString("user_logintime") != null) {
                        Str.append("<td valign=top align=center>" + strToLongDate(crs.getString("user_logintime")) + "</td>");
                    } else {
                        Str.append("<td valign=top align=center>&nbsp;</td>");
                    }
                    Str.append("</tr>");
                }
                crs.close();
                Str.append("</table>\n");
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                return "";
            }
        } else {
            RecCountDisplay = "<br><br><br><br><font color=red>No Active User(s) found!</font><br><br>";
        }
        return Str.toString();
    }
}
