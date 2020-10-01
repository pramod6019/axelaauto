package axela.service;

/**
 * @author Dilip Kumar P
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobcardPSFConcern extends Connect {  

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
            + " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
            + " &gt; <a href=managejobcardpsfconcern.jsp?all=yes>List PSF Concern</a>:";
    public String LinkExportPage = "";
    public String LinkAddPage = "<a href=managejobcardpsfconcern-update.jsp?add=yes>Add PSF Concern...</a>";
    public String ExportPerm = "";
    public String StrHTML = "";
    public String msg = "";
    public String StrSql = "";
    public String CountSql = "";
    public String SqlJoin = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int PageCount = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String all = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String jcpsfconcern_id = "0";
    public Smart SmartSearch = new Smart();
    public String advSearch = "";
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"JCPSF Concern ID", "numeric", "jcpsfconcern_id"},
        {"Desc", "text", "jcpsfconcern_desc"},
        {"Entry By", "text", "jcpsfconcern_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Entry Date", "date", "jcpsfconcern_entry_date"},
        {"Modified By", "text", "jcpsfconcern_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Modified Date", "date", "jcpsfconcern_modified_date"}
    };

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            all = PadQuotes(request.getParameter("all"));
            PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
            QueryString = PadQuotes(request.getQueryString());
            msg = PadQuotes(request.getParameter("msg"));
            jcpsfconcern_id = CNumeric(PadQuotes(request.getParameter("jcpsfconcern_id")));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND jcpsfconcern_id = 0";
            } else if ("yes".equals(all)) {
                msg = msg + "<br>Results for All PSF Concern!";
                StrSearch = StrSearch + " AND jcpsfconcern_id > 0";
            }

            if (!jcpsfconcern_id.equals("0")) {
                msg = msg + "<br>Results for PSF Concern ID = " + jcpsfconcern_id + "!";
                StrSearch = StrSearch + " AND jcpsfconcern_id = " + jcpsfconcern_id + "";
            } else if (advSearch.equals("Search")) // for keyword search
            {
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                if (StrSearch.equals("")) {
                    msg = "Enter Search Text!";
                    StrSearch = StrSearch + " AND jcpsfconcern_id = 0";
                } else {
                    msg = "Results for Search!";
                }
            }
            StrHTML = Listdata();
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
        StringBuilder Str = new StringBuilder();
        if (!msg.equals("")) {
            StrSql = "SELECT jcpsfconcern_id, jcpsfconcern_desc";

            CountSql = "SELECT COUNT(DISTINCT jcpsfconcern_id)";

            SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
                    + " WHERE 1 = 1";

            StrSql = StrSql + SqlJoin;
            CountSql = CountSql + SqlJoin;

            if (!(StrSearch.equals(""))) {
                StrSql = StrSql + StrSearch;
                CountSql = CountSql + StrSearch;
            }
            TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
            if (TotalRecords != 0) {
                StrSql = StrSql + " ORDER BY jcpsfconcern_id DESC";
                try {
                    CachedRowSet crs = processQuery(StrSql, 0);
                    int count = 0;
                    Str.append("<br><br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
                    Str.append("<tr align=center>\n");
                    Str.append("<th width=5%>#</th>\n");
                    Str.append("<th>PSF Concern Details</th>\n");
                    Str.append("<th width=20%>Actions</th>\n");
                    Str.append("</tr>\n");
                    while (crs.next()) {
                        count = count + 1;
                        Str.append("<tr>\n");
                        Str.append("<td valign=top align=center>").append(count).append("</td>\n");
                        Str.append("<td valign=top align=center>").append(crs.getString("jcpsfconcern_desc")).append("</td>\n");
                        Str.append("<td valign=top nowrap>");
                        Str.append("<a href=\"managejobcardpsfconcern-update.jsp?update=yes&jcpsfconcern_id=").append(crs.getString("jcpsfconcern_id")).append(" \">Update PSF Concern</a>");
//                            Str.append("&nbsp;");
                        Str.append("</td>\n</tr>\n");
                    }
                    Str.append("</table>\n");
                    crs.close();
                } catch (Exception ex) {
                    SOPError("Axelaauto== " + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    return "";
                }
            } else {
                RecCountDisplay = "<br><br><font color=red><b>No PSF Concern Found!</b></font>";
            }
        }
        return Str.toString();
    }
}
