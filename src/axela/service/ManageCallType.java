package axela.service;

/**
 * @author Sangita 02 APRIL 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageCallType extends Connect {

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managecalltype.jsp?all=yes>List Call Type</a><b>:</b>";
    public String LinkExportPage = "";
    public String LinkAddPage = "<a href=managecalltype-update.jsp?add=yes>Add Call Type...</a>";
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
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String type_id = "0";
    public String all = "";
    public Smart SmartSearch = new Smart();
    public String advSearch = "";
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Type ID", "numeric", "calltype_id"},
        {"Type Name", "text", "calltype_name"},
        {"Entry By", "text", "calltype_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
        {"Entry Date", "date", "calltype_entry_date"},
        {"Modified By", "text", "calltype_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
        {"Modified Date", "date", "calltype_modified_date"}
    };

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
            PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
            QueryString = PadQuotes(request.getQueryString());
            msg = PadQuotes(request.getParameter("msg"));
            type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND calltype_id = 0";
            } else if ("yes".equals(all)) {
                msg = msg + "<br>Results for All Call Type(s)!";
                StrSearch = StrSearch + " and calltype_id > 0";
            } else if (!(type_id.equals("0"))) {
                msg = msg + "<br>Results for Call Type ID = " + type_id + "!";
                StrSearch = StrSearch + " and calltype_id = " + type_id + "";
            } else if (advSearch.equals("Search")) // for keyword search
            {
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                if (StrSearch.equals("")) {
                    msg = "Enter Search Text!";
                    StrSearch = StrSearch + " and calltype_id = 0";
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
        int PageListSize = 10;
        int StartRec = 0;
        int EndRec = 0;
        int TotalRecords = 0;
        StringBuilder Str = new StringBuilder();
        if (!msg.equals("")) {
            //Check PageCurrent is valid for parse int
            if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
                PageCurrents = "1";
            }
            PageCurrent = Integer.parseInt(PageCurrents);
            // to know no of records depending on search
            StrSql = "Select calltype_id, calltype_name";
            CountSql = "SELECT Count(distinct calltype_id)";
            SqlJoin = " from " + compdb(comp_id) + "axela_service_call_type"
                    + " where 1 = 1";

            StrSql = StrSql + SqlJoin;
            CountSql = CountSql + SqlJoin;

            if (!(StrSearch.equals(""))) {
                StrSql = StrSql + StrSearch;
                CountSql = CountSql + StrSearch;
            }
            TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
            if (TotalRecords != 0) {
                StartRec = ((PageCurrent - 1) * recperpage) + 1;
                EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
                //if limit ie. 10 > totalrecord
                if (EndRec > TotalRecords) {
                    EndRec = TotalRecords;
                }
                RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Call Type(s)";
                if (QueryString.contains("PageCurrent") == true) {
                    QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
                }
                //PageURL = "managecontractservice.jsp?" + QueryString + "&PageCurrent=";
                PageURL = "managecalltype.jsp?" + QueryString + "&PageCurrent=";
                PageCount = (TotalRecords / recperpage);
                if ((TotalRecords % recperpage) > 0) {
                    PageCount = PageCount + 1;
                }
                // display on jsp page
                PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
                StrSql = StrSql + " order by calltype_id desc";
                StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

                try {
                    CachedRowSet crs = processQuery(StrSql, 0);
                    int count = StartRec - 1, j = 0;
                    Str.append("\n<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                    Str.append("<tr align=center>\n");
                    Str.append("<th width=5%>#</th>\n");
                    Str.append("<th>Call Type Details</th>\n");
                    Str.append("<th width=20%>Actions</th>\n");
                    Str.append("</tr>\n");
                    while (crs.next()) {
                        count = count + 1;
                        Str.append("<tr>");
                        Str.append("<td valign=top align=center >").append(count).append("</td>\n");
                        Str.append("<td valign=top>").append(crs.getString("calltype_name")).append("</td>\n");
                        Str.append("<td valign=top><a href=\"managecalltype-update.jsp?update=yes&type_id=").append(crs.getString("calltype_id")).append(" \">Update Call Type</a></td>\n");
                        Str.append("</tr>\n");
                    }
                    Str.append("</table>\n");
                    crs.close();
                } catch (Exception ex) {
                    SOPError("Axelaauto== " + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    return "";
                }
            } else {
                Str.append("<br><br><br><br><font color=red><b>No Call Type(s) Found!</b></font><br><br>");
            }
        }
        return Str.toString();
    }
}
