package axela.portal;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Access_Log extends Connect {

    public String LinkHeader = "Home &gt; <a href=executives.jsp>Executive</a> &gt; Executives Access:";
    public String LinkListPage = "executives-access-log.jsp";
    public String LinkExportPage = "";
    public String LinkFilterPage = "";
    public String LinkAddPage = "";
    public String ExportPerm = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String StrHTML = "";
    public String search = "";
    public String alpha = "";
    public String others = "";
    public String drop_search;
    public String msg = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String txt_search = "";
    public String all = "";
    public String log_id = "";
    public String dr_atoz;
    public String txt_keyword;
    DecimalFormat deci = new DecimalFormat("#");

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
 	       if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            all = PadQuotes(request.getParameter("all"));
            alpha = PadQuotes(request.getParameter("alpha"));
            others = PadQuotes(request.getParameter("Others"));
            PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
            QueryString = PadQuotes(request.getQueryString());
            search = PadQuotes(request.getParameter("search_button"));
            msg = PadQuotes(request.getParameter("msg"));

            dr_atoz = PadQuotes(request.getParameter("dr_atoz"));
            txt_keyword = PadQuotes(request.getParameter("txt_keyword"));


            if ("yes".equals(all)) {
                msg = "Results for all Executive(s)!";
                StrSearch = StrSearch + " and log_id > 0";
            } else if (!"".equals(alpha))//Contacts to b displayed based on clicking a specific alphabet
            {
                msg = "Result for Executive starting from = " + alpha + "!";
                StrSearch = StrSearch + " and emp_name like '" + alpha + "%'";
            } else if ("yes".equals(others))//Contacts after alphabet 'Z' will b displayed
            {
                msg = "Results for Executive starting as others!";
                for (int i = 65; i < 91; i++) {
                    StrSearch = StrSearch + " and emp_name not like '" + (char) (i) + "%'";
                }
            }
//            else if (!emp_id.equals("")) {
//                StrSearch = StrSearch + " and emp_id=" + emp_id + "";
//            }

            if (!log_id.equals("")) {
                StrSearch = StrSearch + " and log_id=" + log_id + "";
            } else if ("Search".equals(search)) // for keyword search
            {
                GetValues(request, response);
                if (txt_search.equals("")) {
                    msg = "Enter search text!";
                }
                if (!txt_search.equals("") && drop_search.equals("-1")) {
                    msg = "Result for Search =" + txt_search + "!";
                    StrSearch = StrSearch + " and (emp_name like '%" + txt_search + "%'";

                    StrSearch = StrSearch + " or log_remote_host like '%" + txt_search + "%'";
                    StrSearch = StrSearch + " or log_remote_agent like '%" + txt_search + "%'";
                    StrSearch = StrSearch + ") ";
                } else if (drop_search.equals("1")) {
                    msg = "Result for Search =" + txt_search + "!";
                    //StrSearch = StrSearch + " and (emp_pid =" + CNumeric(txt_search) + ")";
                } else if (drop_search.equals("2")) {
                    msg = "Result for Search =" + txt_search + "!";
                    StrSearch = StrSearch + " and (emp_name like '%" + txt_search + "%')";
                } else if (drop_search.equals("3")) {
                    msg = "Result for Search =" + txt_search + "!";
                    StrSearch = StrSearch + " and (log_remote_host like '%" + txt_search + "%')";
                } else if (drop_search.equals("4")) {
                    msg = "Result for Search =" + txt_search + "!";
                    StrSearch = StrSearch + " and (log_remote_agent like '%" + txt_search + "%')";
                }
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
            drop_search = PadQuotes(request.getParameter("dr_search"));
            txt_search = PadQuotes(request.getParameter("txt_search"));
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String Listdata() {
        int PageListSize = 10;
        int StartRec = 0;
        int EndRec = 0;
        int TotalRecords = 0;
        String StrJoin = "";
        String CountSql = "";
        String PageURL = "";
        StringBuilder Str = new StringBuilder();
        //Check PageCurrent is valid for parse int
        if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
            PageCurrents = "1";
        }
        PageCurrent = Integer.parseInt(PageCurrents);

        // to know  no of records depending on search
        StrSql = " Select " + compdb(comp_id) + "axela_emp_log.*, emp_name, emp_id, comp_name ";

        StrJoin = " from " + compdb(comp_id) + "axela_emp_log "
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id=log_emp_id "
                + " where 1=1 ";

        CountSql = " SELECT Count(distinct log_id) ";

        StrSql = StrSql + StrJoin;
        CountSql = CountSql + StrJoin;
        //SOP("StrSql====in listdata==="+StrSql);
        if (!(StrSearch.equals(""))) {
            StrSql = StrSql + StrSearch;
            CountSql = CountSql + StrSearch;
//            SOP(StrSql);
        }
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

        if (TotalRecords != 0) {
            StartRec = ((PageCurrent - 1) * recperpage) + 1;
            EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
            //if limit ie. 10 > totalrecord
            if (EndRec > TotalRecords) {
                EndRec = TotalRecords;
            }

            RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Executive(s)";
            if (QueryString.contains("PageCurrent") == true) {
                QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
            }
            PageURL = "executives-access-log.jsp?" + QueryString + "&PageCurrent=";
            PageCount = (TotalRecords / recperpage);
            if ((TotalRecords % recperpage) > 0) {
                PageCount = PageCount + 1;
            }
            // display on jsp page

            PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
            StrSql = StrSql + " order by log_id desc";
            StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
//            SOP("StrSql====in listdata==="+StrSql);
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = StartRec - 1;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Executive</th>\n");
                Str.append("<th>Remote Host</th>\n");
                Str.append("<th>Remote Agent</th>\n");
                Str.append("<th>Sign In Time</th>\n");
                Str.append("<th>Sign Out Time</th>\n");
                Str.append("<th>Duration</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center ><b>").append(count).append(".</b></td>\n");
                    StringBuilder append = Str.append("<td valign=top align=left><a href=javascript:WinPop('executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("','exe','50','50','500','500')>").append(crs.getString("emp_name")).append(")</a></td>\n");
                    StringBuilder append1 = Str.append("<td valign=top align=left>").append(crs.getString("log_remote_host")).append("</td>\n");
                    StringBuilder append2 = Str.append("<td valign=top align=left>").append(crs.getString("log_remote_agent")).append("</td>\n");
                    Str.append("<td valign=top align=left>").append(strToLongDate(crs.getString("log_signin_time"))).append("</td>\n");
                    Str.append("<td valign=top align=left>").append(strToLongDate(crs.getString("log_signout_time"))).append("</td>\n");
                    if (!crs.getString("log_signout_time").equals("")) {
                        // String Hours=deci.format(getHoursBetween(StringToDate(crs.getString("log_signin_time")), StringToDate(crs.getString("log_signout_time"))));
                        int Hours = +(int) getHoursBetween(StringToDate(crs.getString("log_signin_time")), StringToDate(crs.getString("log_signout_time")));
                        // SOP("Hours=="+StringToDate(crs.getString("log_signin_time")));
                        //  SOP("end===="+StringToDate(crs.getString("log_signout_time")));
                        String Mins = deci.format(getMinBetween(StringToDate(crs.getString("log_signin_time")), StringToDate(crs.getString("log_signout_time"))));
                        Str.append("<td valign=top align=center>").append(Hours).append(":").append(Mins).append("</td>\n");
                    } else {
                        Str.append("<td valign=top align=center>--</td>\n");
                    }
                    Str.append("</tr>\n");
                }
                Str.append("</table>");
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                return "";
            }
        } else {
            Str.append("<br><br><br><br><b><font color=red>No Executive(s) found!</font></b><br><br>");
        }
        return Str.toString();
    }

    public String PopulateSearch() {
        search = "<option value = -1" + Selectdrop(-1, drop_search) + ">Keyword</option>";
        search = search + "<option value = 1" + Selectdrop(1, drop_search) + ">Executive ID</option>\n";
        search = search + "<option value = 2" + Selectdrop(2, drop_search) + ">Executive Name</option>\n";
        search = search + "<option value = 3" + Selectdrop(3, drop_search) + ">Remote Host</option>\n";
        search = search + "<option value = 4" + Selectdrop(4, drop_search) + ">Remote Agent</option>\n";
        return search;
    }

    public String AtoZ() {
        String atoz = "";
        for (int i = 65; i <= 90; i++) {
            atoz = atoz + " <a href=executives-access-log.jsp?alpha=" + (char) i + ">" + (char) i + "</a>&nbsp;";
        }
        return atoz;
    }
}
