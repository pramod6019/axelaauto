package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInstallPriority extends Connect {

    public String LinkHeader = "Home &gt; <a href=manager.jsp>Business Manager</a> &gt; List Balance Type:";
    public String LinkListPage = "manageinstallpriority.jsp";
    public String LinkExportPage = "";
    public String LinkFilterPage = "";
    public String LinkAddPage = "<a href=manageinstallpriority-update.jsp?Add=yes>Add Balance Type...</a>";
    public String ExportPerm = "";
    public String emp_idsession = "";
    public String StrHTML = "";
    public String comp_id = "0";
    public String search = "";
    public String msg = "";
    public String Up = "";
    public String Down = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    int PageCount = 10;
    int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String txt_search = "";
    public String drop_search = "";
    public String all = "";
    public String alpha = "";
    public String others = "";
    public String prioritybalance_id = "";
    public String prioritybalance_pid = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
                    emp_idsession = CNumeric(GetSession("emp_id", request));
                }
                CheckPerm(comp_id, "emp_role_id", request, response);
                all = PadQuotes(request.getParameter("all"));
                alpha = PadQuotes(request.getParameter("Alpha"));
                others = PadQuotes(request.getParameter("Others"));
                PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
                QueryString = PadQuotes(request.getQueryString());
                search = PadQuotes(request.getParameter("search"));
                msg = PadQuotes(request.getParameter("msg"));
                Up = PadQuotes(request.getParameter("Up"));
                Down = PadQuotes(request.getParameter("Down"));
                prioritybalance_id = PadQuotes(request.getParameter("prioritybalance_id"));
                prioritybalance_pid = PadQuotes(request.getParameter("prioritybalance_pid"));

                if (!prioritybalance_id.equals("0") && !isNumeric(prioritybalance_id)) {
                    prioritybalance_id = "0";
                }
                if (!prioritybalance_pid.equals("0") && !isNumeric(prioritybalance_pid)) {
                    prioritybalance_pid = "0";
                }
                if (search == null) {
                    search = "";
                }
                if (msg == null) {
                    msg = "";
                }
                if (Up == null) {
                    Up = "";
                }
                if (Down == null) {
                    Down = "";
                }

                if (msg.toLowerCase().contains("delete")) {
                    StrSearch = " AND prioritybalance_id = 0";
                } else if ("yes".equals(all)) {
                    msg = "Results for all Balance Types!";
                    StrSearch = StrSearch + " and prioritybalance_id > 0";
                } else if (!"".equals(alpha)) {
                    msg = "Result for Balance Types starting from = " + alpha + "";
                    StrSearch = StrSearch + " and  prioritybalance_name like '" + alpha + "%'";
                } else if ("yes".equals(others)) {
                    msg = "Results for Balance Types starting as others!";
                    for (int i = 65; i < 90; i++) {
                        StrSearch = StrSearch + " and  prioritybalance_name not like '" + (char) (i) + "%'";
                    }
                }
                if (Up.equals("yes")) {
                    moveup();
                    response.sendRedirect(response.encodeRedirectURL("manageinstallpriority.jsp?msg=Priority moved up successfully!"));
                }
                if (Down.equals("yes")) {
                    movedown();
                    response.sendRedirect(response.encodeRedirectURL("manageinstallpriority.jsp?msg=Priority moved down successfully!"));
                }
                if ("yes".equals(search)) // for keyword search
                {
                    GetValues(request, response);
                    if (drop_search.equals("-1")) {
                        StrSearch = StrSearch + " and (prioritybalance_name like '%" + txt_search + "%'";
                        if (isNumeric(txt_search)) {
                            StrSearch = StrSearch + " or prioritybalance_pid = '" + txt_search + "'";
                        }
                        StrSearch = StrSearch + " )";
                    } else if (drop_search.equals("1")) {
                        if (isNumeric(txt_search)) {
                            msg = "Result for Search =" + txt_search + "";
                            StrSearch = StrSearch + " and prioritybalance_pid = " + txt_search + " ";
                        }
                    } else if (drop_search.equals("2")) {
                        msg = "Result for Search =" + txt_search + "";
                        StrSearch = StrSearch + " and prioritybalance_name like '%" + txt_search + "%'";
                    }
                }
                if (!(prioritybalance_id.equals(""))) {
                    msg = msg + "<br>Results for Balance Type";
                    StrSearch = StrSearch + " and prioritybalance_id =" + prioritybalance_id + "";
                }
                StrHTML = Listdata();
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

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            txt_search = PadQuotes(request.getParameter("txt_search"));
            drop_search = PadQuotes(request.getParameter("dr_search"));
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String Listdata() {

        int TotalRecords = 0;
        String CountSql = "";
        String PageURL = "";
        StringBuilder Str = new StringBuilder();
        StrSql = "Select prioritybalance_id, prioritybalance_name, prioritybalance_desc "
                + " from " + compdb(comp_id) + "axela_priority_balance where 1 = 1 ";
        CountSql = "SELECT Count(distinct prioritybalance_id) from " + compdb(comp_id) + "axela_priority_balance where 1=1 ";
        {
            StrSql = StrSql + StrSearch;
            CountSql = CountSql + StrSearch;
        }
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
        if (TotalRecords != 0) {
            StrSql = StrSql + " order by prioritybalance_rank";
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = 0;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Priority Details</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("<th>Order</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center>").append(count).append("</td>\n");
                    Str.append("<td valign=top align=left>").append(crs.getString("prioritybalance_name")).append("</font></td>\n");
                    Str.append("<td valign=top align=left nowrap><a href=\"manageinstallpriority-update.jsp?update=yes&prioritybalance_id=").append(crs.getString("prioritybalance_id")).append(" \"> Update  Priority</a></td>\n");
                    Str.append("<td valign=top align=center><a href=\"manageinstallpriority.jsp?Up=yes&prioritybalance_id=").append(crs.getString("prioritybalance_id")).append(" \">Up</a> - <a href=\"manageinstallpriority.jsp?Down=yes&prioritybalance_id=").append(crs.getString("prioritybalance_id")).append(" \">Down</a></td>\n");
                }
                Str.append("</tr>\n");
                Str.append("</table>\n");
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                return "";
            }
        } else {
            RecCountDisplay = "<br><br><font color=red><b>No Priority found!</b></font>";
        }

        return Str.toString();
    }

    public void moveup() {
        int tempRank;
        int prioritybalance_rank;
        try {
            prioritybalance_rank = Integer.parseInt(ExecuteQuery("SELECT prioritybalance_rank from " + compdb(comp_id) + "axela_priority_balance where  prioritybalance_id=" + prioritybalance_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select min(prioritybalance_rank) as min1 from " + compdb(comp_id) + "axela_priority_balance where 1=1 "));
            if (prioritybalance_rank != tempRank) {
                tempRank = prioritybalance_rank - 1;
                StrSql = "update " + compdb(comp_id) + "axela_priority_balance set prioritybalance_rank=" + prioritybalance_rank + " where prioritybalance_rank=" + tempRank + " ";
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_priority_balance set prioritybalance_rank=" + tempRank + " where  prioritybalance_rank=" + prioritybalance_rank + " and prioritybalance_id=" + prioritybalance_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void movedown() {
        int tempRank;
        int prioritybalance_rank;
        try {
            prioritybalance_rank = Integer.parseInt(ExecuteQuery("SELECT prioritybalance_rank from " + compdb(comp_id) + "axela_priority_balance where  prioritybalance_id=" + prioritybalance_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select max(prioritybalance_rank) as max1 from " + compdb(comp_id) + "axela_priority_balance where 1=1"));
            if (prioritybalance_rank != tempRank) {
                tempRank = prioritybalance_rank + 1;
                StrSql = "update " + compdb(comp_id) + "axela_priority_balance set prioritybalance_rank=" + prioritybalance_rank + " where  prioritybalance_rank=" + tempRank + " ";
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_priority_balance set prioritybalance_rank=" + tempRank + " where  prioritybalance_rank=" + prioritybalance_rank + " and prioritybalance_id=" + prioritybalance_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateSearch() {
        search = "<option value = -1" + Selectdrop(-1, drop_search) + ">Keyword</option>";
        search = search + "<option value = 1" + Selectdrop(1, drop_search) + ">Balance Type ID</option>\n";
        search = search + "<option value = 2" + Selectdrop(2, drop_search) + ">Balance Type</option>\n";
        return search;
    }

    public String AtoZ() {
        String atoz = "";
        for (int i = 65; i <= 90; i++) {
            atoz = atoz + " <a href=manageinstallpriority.jsp?Alpha=" + (char) i + ">" + (char) i + "</a>&nbsp;";
        }
        return atoz;
    }
}
