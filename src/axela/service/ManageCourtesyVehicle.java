// Sangita 03 APRIL 2013 
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageCourtesyVehicle extends Connect {

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
            + " &gt; <a href=../portal/manager.jsp>Business Master</a>"
            + " &gt; <a href=managecourtesyvehicle.jsp?all=yes>List Courtesy Vehicles</a><b>:</b>";
    public String LinkExportPage = "vehicle-export.jsp?smart=yes&target=" + Math.random() + "";
    public String LinkAddPage = "<a href=managecourtesyvehicle-update.jsp?add=yes>Add New Courtesy Vehicle...</a>";
    public String LinkPrintPage = "";
    public String ExportPerm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String courtesyveh_id = "0";
    public String type_id = "0";
    public String StrHTML = "";
    public String all = "";
    public String msg = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String CountSql = "";
    public String SqlJoin = "";
    public int PageSize = 0;
    public int PageCount = 10;
    public int PageCurrent = 0;
    public String PageURL = "";
    public String PageNaviStr = "";
    public int recperpage = 0;
    public String RecCountDisplay = "";
    public String smart = "";
    public String PageCurrents = "";
    public String QueryString = "";
    public Smart SmartSearch = new Smart();
    public String advSearch = "";
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Vehicle ID", "numeric", "courtesyveh_id"},
        {"Name", "text", "courtesyveh_name"},
        {"Branch ID", "numeric", "courtesyveh_branch_id"},
        {"Entry By", "text", "courtesyveh_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Entry Date", "date", "courtesyveh_entry_date"},
        {"Modified By", "text", "courtesyveh_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
        {"Modified Date", "date", "courtesyveh_modified_date"}
    };

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
            CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
            HttpSession session = request.getSession(true);
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            all = PadQuotes(request.getParameter("all"));
            smart = PadQuotes(request.getParameter("smart"));
            msg = PadQuotes(request.getParameter("msg"));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));
            PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
            QueryString = PadQuotes(request.getQueryString());
            courtesyveh_id = CNumeric(PadQuotes(request.getParameter("courtesyveh_id")));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND courtesyveh_id = 0";
            } else if ("yes".equals(all)) {
                msg = msg + "<br>Results for All Courtesy Vehicles!";
                StrSearch = StrSearch + " AND courtesyveh_id > 0";
            } else if (!(courtesyveh_id.equals("0"))) {
                msg = msg + "<br>Results for Vehicle ID = " + courtesyveh_id + "!";
                StrSearch = StrSearch + " AND courtesyveh_id = " + courtesyveh_id + "";
            } else if (advSearch.equals("Search")) {
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
                if (StrSearch.equals("")) {
                    msg = "Enter Search Text!";
                    StrSearch = StrSearch + " AND courtesyveh_id = 0";
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
            if (PageCurrents.equals("0")) {
                PageCurrents = "1";
            }
            PageCurrent = Integer.parseInt(PageCurrents);
            StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_branch_id,"
                    + " courtesyveh_service_start_date, courtesyveh_service_end_date, courtesyveh_regno,"
                    + " courtesyveh_active, CONCAT(branch_name, '(',branch_code,')') AS branchname, branch_id";

            CountSql = "SELECT COUNT(DISTINCT courtesyveh_id)";

            SqlJoin = " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch on  branch_id = courtesyveh_branch_id"
                    + " WHERE 1 = 1";

            StrSql = StrSql + SqlJoin;
            CountSql = CountSql + SqlJoin;
            //SOP("StrSql" + StrSql);

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
                RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Courtesy Vehicle(s)";
                if (QueryString.contains("PageCurrent") == true) {
                    QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
                }
                PageURL = "managedemovehicle.jsp?" + QueryString + "&PageCurrent=";
                PageCount = (TotalRecords / recperpage);
                if ((TotalRecords % recperpage) > 0) {
                    PageCount = PageCount + 1;
                }
                PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
                StrSql = StrSql + " order by courtesyveh_id desc";
                StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
                try {
                    CachedRowSet crs = processQuery(StrSql, 0);
                    int count = StartRec - 1, j = 0;
                    Str.append("<div class=\" table-bordered table-hover \">\n");
    				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
                    //Str.append("<tr align=center>\n");
    				Str.append("<thead><tr>\n");
                    Str.append("<th data-toggle=\"true\">#</th>\n");
                    Str.append("<th>Vehicle Details</th>\n");
                    Str.append("<th>Service Start Date</th>\n");
                    Str.append("<th>Service End Date</th>\n");
                    Str.append("<th data-hide=\"phone\">Branch</th>\n");
                    Str.append("<th data-hide=\"phone\">Actions</th>\n");
                    Str.append("</tr></thead><tbody>\n");

                    while (crs.next()) {  
                        count = count + 1;
                        Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
                        Str.append("<td valign=top>").append(crs.getString("courtesyveh_name"));
                        if (crs.getString("courtesyveh_active").equals("0")) {
                            Str.append("<font color=red><b> [Inactive]</b></font>");
                        }
                        Str.append("<br>Reg No: ").append(crs.getString("courtesyveh_regno"));
                        Str.append("</td>\n");
                        Str.append("<td valign=top align=center>");
                        Str.append(strToShortDate(crs.getString("courtesyveh_service_start_date"))).append("</td>\n");
                        Str.append("<td valign=top align=center>");
                        Str.append(strToShortDate(crs.getString("courtesyveh_service_end_date"))).append("</td>\n");
                        Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?branch_id=");
                        Str.append(crs.getInt("branch_id")).append("\">");
                        Str.append(crs.getString("branchname")).append("</a></td>");
                        Str.append("<td valign=top><a href=\"managecourtesyvehicle-update.jsp?update=yes&courtesyveh_id=");
                        Str.append(crs.getString("courtesyveh_id")).append("\">Update Courtesy Vehicle</a></td>\n");
                        Str.append("</tr>\n");
                    }
                    Str.append("</tr></tbody>\n");
    				Str.append("</table></div>\n");
                    crs.close();
                } catch (Exception ex) {
                    SOPError("Axelaauto== " + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
                    return "";
                }
            } else {
                Str.append("<br><br><br><br><font color=red><b>No Coutesy Vehicle(s) Found!</b></font><br><br>");
            }
        }
        return Str.toString();
    }
}
