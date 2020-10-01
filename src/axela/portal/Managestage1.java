package axela.portal;
//Murali 21st jun
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managestage1 extends Connect {

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=manager.jsp>Business Manager</a> &gt; <a href=managestage.jsp>List Stage</a>:";
    public String LinkListPage = "managestage.jsp";
    public String LinkExportPage = "";
    public String LinkFilterPage = "";
    public String LinkAddPage = "<a href=managestage-update.jsp?Add=yes>Add Stage...</a>";
    public String ExportPerm = "";
    public String emp_id = "", branch_id = "";
    public String StrHTML = "";
    //public String search ="";
    public String msg = "";
    public String StrSql = "";
    public String comp_id = "0";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    //  public String txt_search ="";
    //  public String drop_search;
    public String all = "";
    // public String alpha ="";
    //  public String others ="";
    public String stage_id = "";
    public String stage_rank = "";
    public String Up = "";
    public String Down = "";
    public String advSearch = "";
    public Smart SmartSearch = new Smart();
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Stage ID", "numeric", "stage_id"},
        {"Stage Name", "text", "stage_name"},
        {"Stage Probability", "numeric", "stage_probability"},
        {"Stage Rank", "numeric", "stage_rank"}
    };

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
                CheckPerm(comp_id, "emp_role_id", request, response);
                branch_id = CNumeric(GetSession("emp_branch_id", request));
                all = PadQuotes(request.getParameter("all"));
                PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
                QueryString = PadQuotes(request.getQueryString());
                msg = PadQuotes(request.getParameter("msg"));
                Up = PadQuotes(request.getParameter("Up"));
                Down = PadQuotes(request.getParameter("Down"));
                stage_id = CNumeric(PadQuotes(request.getParameter("stage_id")));
                stage_rank = PadQuotes(request.getParameter("stage_rank"));
                advSearch = PadQuotes(request.getParameter("advsearch_button"));
                if (msg.toLowerCase().contains("delete")) {
                    StrSearch = " AND stage_id = 0";
                } else if ("yes".equals(all)) {   
                    msg = "Results for all Stage!";
                    StrSearch = StrSearch + " and stage_id > 0";
                }

                if (Up.equals("yes")) {
                    moveup();
                    response.sendRedirect(response.encodeRedirectURL("managestage.jsp?msg=Stage moved up successfully!"));
                }
                if (Down.equals("yes")) {
                    movedown();
                    response.sendRedirect(response.encodeRedirectURL("managestage.jsp?msg=Stage moved down successfully!"));
                }

                if (!(stage_id.equals("0"))) {
                    msg = msg + "<br>Results for Stage";
                    StrSearch = StrSearch + " and stage_id =" + stage_id + "";
                } else if (advSearch.equals("Search")) // for keyword search
                {
                    msg = "Result for Search";
                    StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                }
                if (!StrSearch.equals("")) {
                    SetSession("stagestrsql", StrSearch, request);
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

    public String Listdata() {
        int TotalRecords = 0;
        String CountSql = "";
        StringBuilder Str = new StringBuilder();
        StrSql = "Select stage_id,stage_name,stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1 ";

        CountSql = "SELECT Count(distinct stage_id) from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1 ";


        if (!(StrSearch.equals(""))) {
            StrSql = StrSql + StrSearch;
            CountSql = CountSql + StrSearch;
        }
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));


        if (TotalRecords != 0) {
            StrSql = StrSql + " order by stage_rank";
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = 0;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Stage Details</th>\n");
                Str.append("<th>Order</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");

                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center >").append(count).append("</td>\n");
                    Str.append("<td valign=top align=left >").append(crs.getString("stage_name")).append("</td>\n");
                    Str.append("<td valign=top align=center><a href=\"managestage.jsp?Up=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Up</a> - <a href=\"managestage.jsp?Down=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Down</a></td>\n");
                    Str.append("<td valign=top align=left ><a href=\"managestage-update.jsp?Update=yes&stage_id=").append(crs.getString("stage_id")).append(" \"> Update Stage</a></td>\n");
                }
                crs.close();
                Str.append("</tr>\n");
                Str.append("</table>\n");
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                return "";
            }
        } else {
            RecCountDisplay = "<br><br><br><br><font color=red>No Stage(s) found!</font><br><br>";
        }
        return Str.toString();
    }

//    public String Listdata() {
//
//        int TotalRecords = 0;
//        String CountSql = "";
//        String PageURL = "";
//        StringBuilder Str = new StringBuilder();
//        int PageListSize = 10;
//        int StartRec = 0;
//        int EndRec = 0;
//        String StrJoin = "";
//
//        if (!msg.equals("")) {
//            if (PageCurrents.equals("0")) {
//                PageCurrents = "1";
//            }
//            PageCurrent = Integer.parseInt(PageCurrents);
//            // to know  no of records depending on search
//            if (!msg.equals("Select a search parameter!")) {
//                StrSql = "Select stage_id,stage_name,stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1 " ;
//
//               CountSql = "SELECT Count(distinct stage_id) from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1 " ;
//
//                if (!(StrSearch.equals(""))) {
//                    StrSql = StrSql + StrSearch;
//                    CountSql = CountSql + StrSearch;
//                }
//                TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
//               
//                if (TotalRecords != 0) {
//                    StartRec = ((PageCurrent - 1) * recperpage) + 1;
//                    EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
//                    //    if limit ie. 10 > totalrecord
//                    if (EndRec > TotalRecords) {
//                        EndRec = TotalRecords;
//                    }
//                    RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Franchisee(s)";
//                    if (QueryString.contains("PageCurrent") == true) {
//                        QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
//                    }
//                    PageURL = "managestage.jsp?" + QueryString + "&PageCurrent=";
//                    PageCount = (TotalRecords / recperpage);
//                    if ((TotalRecords % recperpage) > 0) {
//                        PageCount = PageCount + 1;
//                    }
//                    //display on jsp page
//
//                    PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
//                    //StrSql = StrSql + StrSearch;
//                    StrSql = StrSql + " order by stage_rank";
//
//                    StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
//
//                   try
//            {
//            CachedRowSet crs =processQuery(StrSql, 0);
//                int count = 0;
//                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
//               Str.append("<tr align=center>\n");
//                Str.append("<th width=5%>#</th>\n");
//                Str.append("<th>Stage Details</th>\n");
//                Str.append("<th>Order</th>\n");
//                Str.append("<th>Actions</th>\n");
//                Str.append("</tr>\n");
//
//                while(crs.next())
//                {
//                    count = count+1;
//                    Str.append("<tr>\n");
//                    Str.append("<td valign=top align=center >").append(count).append("</td>\n");
//                    Str.append("<td valign=top align=left >").append(crs.getString("stage_name")).append("</td>\n");
//                    Str.append("<td valign=top align=center><a href=\"managestage.jsp?Up=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Up</a> - <a href=\"managestage.jsp?Down=yes&stage_id=").append(crs.getString("stage_id")).append(" \">Down</a></td>\n");
//                    Str.append("<td valign=top align=left ><a href=\"managestage-update.jsp?Update=yes&stage_id=").append(crs.getString("stage_id")).append(" \"> Update Stage</a></td>\n");
//                }
//                    Str.append("</tr>\n");
//                    Str.append("</table>\n");
//                } catch (Exception ex) {
//                        SOPError("Axelaauto===" + this.getClass().getName());
//                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//                        return "";
//                    }
//                } else {
//                    RecCountDisplay = "<br><br><br><br><font color=red>No Stage(s) found!</font><br><br>";
//                }
//            }
//        }
//        return Str.toString();
//    }
    public void moveup() {
        int tempRank;
        int stage_rank;
        try {
            stage_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where  stage_id=" + stage_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select min(stage_rank) as min1 from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1"));
            if (stage_rank != tempRank) {
                tempRank = stage_rank - 1;
                StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank=" + stage_rank + " where stage_rank=" + tempRank + " ";
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank=" + tempRank + " where stage_rank=" + stage_rank + " and stage_id=" + stage_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void movedown() {
        int tempRank;
        int stage_rank;
        try {
            stage_rank = Integer.parseInt(ExecuteQuery("SELECT stage_rank from " + compdb(comp_id) + "axela_sales_enquiry_stage where stage_id=" + stage_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select max(stage_rank) as max1 from " + compdb(comp_id) + "axela_sales_enquiry_stage where 1=1"));
            if (stage_rank != tempRank) {
                tempRank = stage_rank + 1;
                StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank=" + stage_rank + " where  stage_rank=" + tempRank + " ";
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry_stage set stage_rank=" + tempRank + " where  stage_rank=" + stage_rank + " and stage_id=" + stage_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
