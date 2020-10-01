package axela.service;

/**
 * @author Gurumurthy TS 30 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobCardStage1 extends Connect {

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managejobcardstage.jsp?all=yes>List Job Card Stage</a>:";
    public String LinkExportPage = "";
    public String LinkAddPage = "<a href=managejobcardstage-update.jsp?add=yes>Add Job Card Stage...</a>";
    public String ExportPerm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String StrHTML = "";
    public String msg = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String CountSql = "";
    public String SqlJoin = "";
    public String PageURL = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public String Up = "";
    public String Down = "";
    public String stage_id = "0";
    public String all = "";
    public Smart SmartSearch = new Smart();
    public String advSearch = "";
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Stage ID", "numeric", "jcstage_id"},
        {"Stage Name", "text", "jcstage_name"},
        {"Stage Rank", "text", "jcstage_rank"},
        {"Entry By", "text", "jcstage_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
        {"Entry Date", "date", "jcstage_entry_date"},
        {"Modified By", "text", "jcstage_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
        {"Modified Date", "date", "stage_modified_date"}
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
            msg = PadQuotes(request.getParameter("msg"));
            stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
            Up = PadQuotes(request.getParameter("Up"));
            Down = PadQuotes(request.getParameter("Down"));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND stage_id = 0";
            } else if ("yes".equals(all)) {
                msg = msg + "<br>Results for All Job Card Stage(s)!";
                StrSearch = StrSearch + " and stage_id > 0";
            }
            if (Up.equals("yes")) {
                moveup();
                response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Moved Up Successfully!"));
            }
            if (Down.equals("yes")) {
                movedown();
                response.sendRedirect(response.encodeRedirectURL("managejobcardstage.jsp?msg=Stage Moved Down Successfully!"));
            }
            if (!(stage_id.equals("0"))) {
                msg = msg + "<br>Results for Job Card Stage ID = " + stage_id + "!";
                StrSearch = StrSearch + " and stage_id = " + stage_id + "";
            } else if (advSearch.equals("Search")) // for keyword search
            {
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                if (StrSearch.equals("")) {
                    msg = "Enter search text!";
                    StrSearch = StrSearch + " AND stage_id = 0";
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

            StrSql = "Select jcstage_id, jcstage_name, jcstage_rank";
            CountSql = "SELECT Count(distinct jcstage_id)";
            SqlJoin = " from " + compdb(comp_id) + "axela_service_jc_stage"
                    + " where 1=1";

            StrSql = StrSql + SqlJoin;
            CountSql = CountSql + SqlJoin;

            if (!(StrSearch.equals(""))) {
                StrSql = StrSql + StrSearch;
                CountSql = CountSql + StrSearch;
            }
            TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
            if (TotalRecords != 0) {
                StrSql = StrSql + " order by jcstage_rank ";
                try {
                    CachedRowSet crs = processQuery(StrSql, 0);
                    int count = 0;
                    Str.append("<br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                    Str.append("<tr align=center>\n");
                    Str.append("<th width=5%>#</th>\n");
                    Str.append("<th>Job Card Stage Details</th>\n");
                    Str.append("<th>Order</th>\n");
                    Str.append("<th width=20%>Actions</th>\n");
                    Str.append("</tr>\n");
                    while (crs.next()) {
                        count = count + 1;
                        Str.append("<tr>");
                        Str.append("<td valign=top align=center>").append(count).append("</td>\n");
                        Str.append("<td valign=top>").append(crs.getString("stage_name")).append("</td>\n");
                        Str.append("<td valign=top align=center><a href=\"managejobcardstage.jsp?Up=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Up</a> - <a href=\"managejobcardstage.jsp?Down=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Down</a></td>\n");
                        Str.append("<td valign=top>");
                        if (!crs.getString("stage_id").equals("5") && !crs.getString("stage_id").equals("6")) {
                            Str.append("<a href=\"managejobcardstage-update.jsp?update=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Update Job Card Stage</a>");
                        } else {
                            Str.append("&nbsp;");
                        }
                        Str.append("</td></tr>\n");
                    }
                    Str.append("</table>\n");
                    crs.close();
                } catch (Exception ex) {
                    SOPError("Axelaauto== " + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    return "";
                }
            } else {
                Str.append("<br><br><br><br><font color=red><b>No Job Card Stage(s) Found!</b></font><br><br>");
            }
        }
        return Str.toString();
    }

    public void moveup() {
        int tempRank;
        int priorityenquiry_rank;
        try {
            priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_service_jc_stage where  stage_id=" + stage_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("Select min(stage_rank) as min1 from " + compdb(comp_id) + "axela_service_jc_stage where 1 = 1"));
//            SOP("priorityenquiry_rank=="+priorityenquiry_rank+" tempRank==="+tempRank);
            if (priorityenquiry_rank != tempRank) {
                tempRank = priorityenquiry_rank - 1;
                StrSql = "Update " + compdb(comp_id) + "axela_service_jc_stage set stage_rank = " + priorityenquiry_rank + " where stage_rank = " + tempRank + "";
                updateQuery(StrSql);
                StrSql = "Update " + compdb(comp_id) + "axela_service_jc_stage set stage_rank = " + tempRank + " where stage_rank = " + priorityenquiry_rank + " and stage_id = " + stage_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void movedown() {
        int tempRank;
        int priorityenquiry_rank;
        try {
            priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_service_jc_stage where stage_id = " + stage_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("Select max(stage_rank) as max1 from " + compdb(comp_id) + "axela_service_jc_stage where 1 = 1"));
            if (priorityenquiry_rank != tempRank) {
                tempRank = priorityenquiry_rank + 1;
                StrSql = "Update " + compdb(comp_id) + "axela_service_jc_stage set stage_rank = " + priorityenquiry_rank + " where stage_rank = " + tempRank + "";
                updateQuery(StrSql);
                StrSql = "Update " + compdb(comp_id) + "axela_service_jc_stage set stage_rank = " + tempRank + " where stage_rank = " + priorityenquiry_rank + " and stage_id = " + stage_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
