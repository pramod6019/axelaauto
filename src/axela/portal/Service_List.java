package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_List extends Connect {

    /////// List page links
    public String LinkHeader = "Home &gt; <a href=service.jsp>Services</a> &gt; List Services:";
    public String LinkListPage = "service-list.jsp";
    public String LinkExportPage = "service.jsp?smart=yes&target=" + Math.random() + "";
    public String LinkFilterPage = "service-filter.jsp";
    public String LinkAddPage = "<a href=service-update.jsp?Add=yes>Add New Service...</a>";
    public String ExportPerm = "";
    public String emp_id = "", branch_id = "";
    public String StrHTML = "";
    public String search = "";
    public String comp_id = "0";
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
    public String drop_search;
    public String ServiceTax = "";
    public String all = "";
    public String group = "";
    public String alpha = "";
    public String others = "";
    public String go = "";
    public String smart = "";
    public String keyword = "";
    public String course_id = "", course_name = "";
    public String subject_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            CheckPerm(comp_id, "emp_service_access", request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
                branch_id = CNumeric(GetSession("emp_branch_id", request));
                smart = PadQuotes(request.getParameter("smart"));
                PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
                QueryString = PadQuotes(request.getQueryString());
                search = PadQuotes(request.getParameter("search"));
                go = PadQuotes(request.getParameter("go_button"));
                msg = PadQuotes(request.getParameter("msg"));
                course_id = PadQuotes(request.getParameter("course_id"));
                all = PadQuotes(request.getParameter("all"));
                alpha = PadQuotes(request.getParameter("Alpha"));
                others = PadQuotes(request.getParameter("Others"));
                subject_id = PadQuotes(request.getParameter("subject_id"));
                group = PadQuotes(request.getParameter("group"));

                if (!course_id.equals("0") && !isNumeric(course_id)) {
                    course_id = "0";
                }
                if (!subject_id.equals("0") && !isNumeric(subject_id)) {
                    subject_id = "0";
                }
                if (!branch_id.equals("0")) {
                    LinkAddPage = "";
                }

                if (msg.toLowerCase().contains("delete")) {
                    StrSearch = " AND course_id = 0";
                } else if ("yes".equals(all)) {
                    if (msg.equals("")) {
                        msg = "Results for all Service!";
                    }
                    StrSearch = StrSearch + " and course_id > 0";
                } else if (!"".equals(alpha)) {
                    msg = "Result for Service starting from = " + alpha + "";
                    StrSearch = StrSearch + " and course_name like '" + alpha + "%'";
                } else if ("yes".equals(others)) {
                    msg = "Results for Service starting as others!";
                    for (int i = 65; i < 90; i++) {
                        StrSearch = StrSearch + " and course_name not like '" + (char) (i) + "%'";
                    }
                } else if ("yes".equals(smart)) {
                    msg = msg + "<br>Results of Search!";
                    if (!GetSession("coursestrsql", request).equals("")) {
                        StrSearch = StrSearch + GetSession("coursestrsql", request);
                    }
                } else if (!(course_id.equals(""))) {
                    msg = msg + "<br>Results for Service Code";
                    StrSearch = StrSearch + " and course_id =" + course_id + "";
                } else if (!(subject_id.equals(""))) {
                    msg = msg + "<br>Results for Subject ID ";
                    StrSearch = StrSearch + " and course_id in (select course_course_id from " + compdb(comp_id) + "axela_course_subject where course_subject_id =" + subject_id + ")";
                } else if ("yes".equals(search)) // for keyword search
                {
                    GetValues(request, response);
                    if (drop_search.equals("-1")) {
                        msg = "Result for Search =" + txt_search + "";
                        StrSearch = StrSearch + " and(course_name like '%" + txt_search + "%'";
                        if (isNumeric(txt_search)) {
                            StrSearch = StrSearch + " or course_code like '%" + txt_search + "%'";
                        }
                        StrSearch = StrSearch + " or course_name like '%" + txt_search + "%'";
                        StrSearch = StrSearch + " or course_desc like '%" + txt_search + "%'";
                        StrSearch = StrSearch + ") ";
                    } else if (drop_search.equals("1")) {
                        if (isNumeric(txt_search)) {
                            msg = "Result for Service ID = " + txt_search + "";
                            // StrSearch = StrSearch + " and (course_pid = "+ txt_search +")";
                        }
                    } else if (drop_search.equals("2")) {
                        msg = " Result for Service Code = " + txt_search + "";
                        StrSearch = StrSearch + " and (course_code like '%" + txt_search + "%')";
                    } else if (drop_search.equals("3")) {
                        msg = " Result for Service Name = " + txt_search + "";
                        StrSearch = StrSearch + " and (course_name like '%" + txt_search + "%')";
                    } else if (drop_search.equals("4")) {
                        msg = " Result for Service Description = " + txt_search + "";
                        StrSearch = StrSearch + " and (course_desc like '%" + txt_search + "%')";
                    }
                }
                if (!StrSearch.equals("")) {
//                        SetSession("coursePrintSearchStr", StrSearch, request);
//                        SetSession("courseFilterStr", StrSearch, request);
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
            keyword = PadQuotes(request.getParameter("keyword"));
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
        int PageListSize = 10;
        int StartRec = 0;
        int EndRec = 0;

        if (!msg.equals("")) {
            if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
                PageCurrents = "1";
            }
            PageCurrent = Integer.parseInt(PageCurrents);
            // to know  no of records depending on search
            ServiceTax = ExecuteQuery("Select comp_servicetax from " + compdb(comp_id) + "axela_comp where 1=1");

            StrSql = " Select course_id,course_name,course_code,course_active from " + compdb(comp_id) + "axela_course "
                    + " where  course_cat_id=2";

            CountSql = " SELECT Count(distinct course_id) from " + compdb(comp_id) + "axela_course "
                    + " where course_cat_id=2 ";

            if (!(StrSearch.equals(""))) {
                StrSql = StrSql + StrSearch;
                CountSql = CountSql + StrSearch;
            }
//                    SOP("StrSql---"+StrSql);
            TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));


            if (TotalRecords != 0) {

                StartRec = ((PageCurrent - 1) * recperpage) + 1;
                EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
                //    if limit ie. 10 > totalrecord
                if (EndRec > TotalRecords) {
                    EndRec = TotalRecords;
                }
                RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Service(s)";
                if (QueryString.contains("PageCurrent") == true) {
                    QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
                }
                PageURL = "service-list.jsp?" + QueryString + "&PageCurrent=";
                PageCount = (TotalRecords / recperpage);
                if ((TotalRecords % recperpage) > 0) {
                    PageCount = PageCount + 1;
                }
                //display on jsp page
                PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
                if (all.equals("yes")) {
                    StrSql = StrSql + " order by course_name";
                } else {
                    StrSql = StrSql + "  order by course_id ";
                }
                StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
                //   SOP("StrSql..in lisy course.."+StrSql);

                try {
                    CachedRowSet crs =processQuery(StrSql, 0);
                    int count = StartRec - 1, j = 0;
                    String col = "white", active = "";
                    String altcol = "white";
                    String bgcol = col;
                    Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                    Str.append("<tr align=center>\n");
                    Str.append("<th>#</th>\n");
                    Str.append("<th>Service Details</th>\n");
                    if (branch_id.equals("0")) {
                        Str.append("<th>Actions</th>\n");
                    }
                    Str.append("</tr>\n");

                    while (crs.next()) {
                        if (j == 0) {
                            j = 1;
                            bgcol = col;
                        } else {
                            j = 0;
                            bgcol = altcol;
                        }
                        count = count + 1;
                        if (crs.getString("course_active").equals("0")) {
                            active = "<font color=red > [Inactive] </font>";
                        } else {
                            active = "";
                        }
                        Str.append("<tr>\n");
                        Str.append("<td valign=top align=center ><b>" + count + ".</b></td>\n");
                        Str.append("<td valign=top align=left ><b><a href=service-summary.jsp?course_id=" + crs.getString("course_id") + " target=_blank>" + crs.getString("course_name") + ") </a>" + active + "</b>"
                                + "<br>Code: " + crs.getString("course_code") + "\n</td>");
                        if (branch_id.equals("0")) {
                            Str.append("<td valign=top align=left ><a href=\"service-update.jsp?Update=yes&course_id=" + crs.getString("course_id") + " \">Update Service</a>");
                            Str.append("<br><a href=\"course-fee-list.jsp?cat_id=2&course_id=" + crs.getString("course_id") + "\">Update Fee</a>");
                            Str.append("<br><br></td>\n");
                            Str.append("</tr>\n");

                        }
                        Str.append("</tr>\n");
                    }
                    Str.append("</table>\n");

                    crs.close();


                } catch (Exception ex) {
                    SOPError("Axelaauto===" + this.getClass().getName());
                    SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    return "";
                }
            } else {
                RecCountDisplay = "<br><br><br><br><font color=red>No Service(s) found!</font><br><br>";
            }
        }
        return Str.toString();
    }

    public String PopulateSearch() {
        search = "<option value = -1" + Selectdrop(-1, drop_search) + ">Keyword</option>";
        search = search + "<option value = 1" + Selectdrop(1, drop_search) + ">Service ID</option>\n";
        search = search + "<option value = 2" + Selectdrop(2, drop_search) + ">Service Code</option>\n";
        search = search + "<option value = 3" + Selectdrop(3, drop_search) + ">Service Name</option>\n";
        search = search + "<option value = 4" + Selectdrop(4, drop_search) + ">Description</option>\n";
        return search;
    }

    public String AtoZ() {
        String atoz = "";
        for (int i = 65; i <= 90; i++) {
            atoz = atoz + " <a href=service-list.jsp?Alpha=" + (char) i + ">" + (char) i + "</a>&nbsp;";
        }
        return atoz;
    }
}
// Created by Dhanesh on 12-05-08

