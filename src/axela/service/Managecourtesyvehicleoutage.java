//Dilip Kumar 03 APR 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Managecourtesyvehicleoutage extends Connect {

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
            + " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
            + " &gt; <a href=managecourtesyvehicleoutage.jsp?all=yes>List Vehicle Outage</a><b>:</b>";
    public String LinkListPage = "managecourtesyvehiclecourtesyoutage.jsp";
    public String LinkExportPage = "democourtesyoutage.jsp?smart=yes&target=" + Math.random() + "";
    public String LinkAddPage = "<a href=managecourtesyvehicleoutage-update.jsp?add=yes>Add Vehicle Outage...</a>";
    public String ExportPerm = "";
    public String msg = "";
    public String all = "";
    public String smart = "";
    public String StrHTML = "";
    public String SqlJoin = "";
    public String CountSql = "";
    public String PageURL = "";
    public String StrSql = "";
    public String StrSearch = "";
    public Smart SmartSearch = new Smart();
    public String QueryString = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String courtesyoutage_id = "0";
    public String advSearch = "";
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Courtesy Outage ID", "numeric", "courtesyoutage_id"},
        {"Vehicle ID", "numeric", "courtesyoutage_courtesyveh_id"},
        {"From time", "date", "courtesyoutage_fromtime"},
        {"To Time", "date", "courtesyoutage_totime"},
        {"Description", "text", "courtesyoutage_desc"},
        {"Notes", "text", "courtesyoutage_notes"},
        {"Entry By", "text", "courtesyoutage_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Entry Date", "date", "courtesyoutage_entry_date"},
        {"Modified By", "text", "courtesyoutage_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Modified Date", "date", "courtesyoutage_modified_date"}
    };
    public String branch_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
            emp_id = CNumeric(GetSession("emp_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            all = PadQuotes(request.getParameter("all"));
            ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
            msg = PadQuotes(request.getParameter("msg"));
            smart = PadQuotes(request.getParameter("smart"));
            PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
            QueryString = PadQuotes(request.getQueryString());
            advSearch = PadQuotes(request.getParameter("advsearch_button"));
            courtesyoutage_id = CNumeric(PadQuotes(request.getParameter("courtesyoutage_id")));
            branch_id = CNumeric(GetSession("emp_branch_id", request));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND courtesyoutage_id = 0";
            } else if ("yes".equals(all)) //for all students to b displayed
            {
                msg = msg + "<br>Results for All Vehicle Outage!";
                StrSearch = StrSearch + " AND courtesyoutage_id > 0";
            } else if (advSearch.equals("Search")) // for keyword search
            {
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                if (StrSearch.equals("")) {
                    msg = "Enter Search Text!";
                    StrSearch = StrSearch + " AND courtesyoutage_id = 0";
                } else {
                    msg = "Results for Search!";
                }
            } else if (!courtesyoutage_id.equals("0")) {
                msg = msg + "<br>Results for Vehicle Outage ID = " + courtesyoutage_id + "!";
                StrSearch = StrSearch + " AND courtesyoutage_id = " + courtesyoutage_id + "";
            } else if ("yes".equals(smart)) {
                msg = msg + "<br>Results of Search!";
                StrSearch = StrSearch + GetSession("courtesyoutagestrsql", request);
            }
            if (!StrSearch.equals("")) {
                SetSession("courtesyoutagestrsql", StrSearch, request);
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
            if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
                PageCurrents = "1";
            }
            PageCurrent = Integer.parseInt(PageCurrents);
            // to know  no of records depending on search
            if (!msg.equals("")) {
                StrSql = "SELECT courtesyoutage_id, courtesyveh_name, branch_code,"
                        + " courtesyoutage_courtesyveh_id, branch_id,"
                        + " CONCAT(branch_name,' (',branch_code,')') as branchname, courtesyoutage_desc,"
                        + " courtesyoutage_fromtime, courtesyoutage_totime, courtesyoutage_desc,"
                        + " courtesyoutage_desc";

                CountSql = " SELECT COUNT(DISTINCT courtesyoutage_id)";

                SqlJoin = " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage"
                        + " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesyoutage_courtesyveh_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
                        + " WHERE 1 = 1 " + BranchAccess;
                StrSql = StrSql + SqlJoin;
//                SOP("StrSql----" + StrSqlBreaker(StrSql));
                CountSql = CountSql + SqlJoin;
                if (!(StrSearch.equals(""))) {
                    StrSql = StrSql + StrSearch;
                    CountSql = CountSql + StrSearch;
                }
//                SOP("countsql===="+CountSql);
                TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
                if (TotalRecords != 0) {
                    StartRec = ((PageCurrent - 1) * recperpage) + 1;
                    EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
                    if (EndRec > TotalRecords) {
                        EndRec = TotalRecords;
                    }
                    RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Vehicle Outage(s)";
                    if (QueryString.contains("PageCurrent") == true) {
                        QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
                    }
                    PageURL = "managecourtesyvehicleoutage.jsp?" + QueryString + "&PageCurrent=";
                    PageCount = (TotalRecords / recperpage);
                    if ((TotalRecords % recperpage) > 0) {
                        PageCount = PageCount + 1;
                    }
                    PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
                    if (all.equals("yes")) {
                        StrSql = StrSql + " GROUP BY courtesyoutage_id ORDER BY courtesyoutage_id DESC";
                    } else {
                        StrSql = StrSql + " GROUP BY courtesyoutage_id ORDER BY courtesyoutage_id";
                    }
                    StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
                    CachedRowSet crs = processQuery(StrSql, 0);
                    try {
                        int count = StartRec - 1;
                        Str.append("<div class=\" table-bordered table-hover \">\n");
        				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
                        //Str.append("<tr align=center>\n");
        				Str.append("<thead><tr>\n");
                        Str.append("<th data-toggle=\"true\">#</th>\n");
                        Str.append("<th>Vehicle Outage Details</th>\n");
                        Str.append("<th>Vehicle</th>\n");
                        if (branch_id.equals("0")) {
                            Str.append("<th data-hide=\"phone\">Branch</th>\n");
                        }
                        Str.append("<th data-hide=\"phone\">Actions</th>\n");
                        Str.append("</tr></thead><tbody>\n");
                        while (crs.next()) {
                            count = count + 1;
                            Str.append("<tr>\n");
                            Str.append("<td valign=top align=center>").append(count).append("</td>\n");
                            Str.append("<td valign=top>");
                            if (!crs.getString("courtesyoutage_fromtime").equals("")) {
                                Str.append("From: ").append(strToLongDate(crs.getString("courtesyoutage_fromtime"))).append("");
                            }
                            if (!crs.getString("courtesyoutage_totime").equals("")) {
                                Str.append("<br>To: ").append(strToLongDate(crs.getString("courtesyoutage_totime"))).append("");
                            }
                            if (!crs.getString("courtesyoutage_desc").equals("")) {
                                Str.append("<br>").append(crs.getString("courtesyoutage_desc")).append("");
                            }
                            Str.append("</td>\n");
                            Str.append("<td valign=top>").append("<b>").append(crs.getString("courtesyveh_name")).append("</b></td>");
                            if (branch_id.equals("0")) {
                                Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>");
                            }
                            Str.append("<td valign=top><a href=\"managecourtesyvehicleoutage-update.jsp?update=yes&courtesyoutage_id=").append(crs.getString("courtesyoutage_id")).append("\">Update Vehicle Outage</a>");
                            Str.append("</td>\n</tr>\n");
                        }
                        Str.append("</tr></tbody>\n");
        				Str.append("</table></div>\n");
                        crs.close();
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                        return "";
                    }
                } else {
                    Str.append("<br><br><br><br><font color=red><b>No Courtesy Vehicle Outage(s) Found!</b></font><br><br>");
                }
            }
        }
        return Str.toString();
    }
}
