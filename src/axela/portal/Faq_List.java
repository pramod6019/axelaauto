package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Faq_List extends Connect {
    /////// List page links

    public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/faq.jsp>FAQ</a> &gt; <a href=../portal/faqexecutive-list.jsp?all=yes>List Executive FAQs</a>:";
    public String LinkListPage = "faqexecutive-list.jsp";
    public String LinkExportPage = "";
    public String LinkFilterPage = "";
    public String LinkAddPage = "<a href=faqexecutive-update.jsp?Add=yes>Add New FAQ...</a>";
    public String ExportPerm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String StrHTML = "";
    public String Up = "";
    public String Down = "";
    public String msg = "";
    public String StrSql = "";
    public String StrJoin = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String faq_id = "0";
    public String all = "";
    public String faq_cat_id = "0";
    public String advSearch = "";
    public Smart SmartSearch = new Smart();
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Faq ID", "numeric", "faq_id"},
        {"Category", "text", "cat_name"},
        {"Question", "text", "faq_question"},
        {"Answer", "text", "faq_answer"},
        {"Active", "boolean", "faq_active"},
        {"Rank", "numeric", "faq_rank"},
        {"Entry Date", "date", "faq_entry_date"},
        {"Modified Date", "date", "faq_modified_date"}
    };

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
            QueryString = PadQuotes(request.getQueryString());
            msg = PadQuotes(request.getParameter("msg"));
            faq_id = CNumeric(PadQuotes(request.getParameter("faq_id")));
            faq_cat_id = CNumeric(PadQuotes(request.getParameter("faq_cat_id")));
            Down = PadQuotes(request.getParameter("Down"));
            Up = PadQuotes(request.getParameter("Up"));
            all = PadQuotes(request.getParameter("all"));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));

            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND faq_id = 0";
            } else if ("yes".equals(all)) {
                msg = "Results for all FAQ!";
                StrSearch = StrSearch + " and faq_id > 0";
            }
            if (Up.equals("yes")) {
                moveup();
                response.sendRedirect(response.encodeRedirectURL("faqexecutive-list.jsp?msg=Stage moved up successfully!"));
            }
            if (Down.equals("yes")) {
                movedown();
                response.sendRedirect(response.encodeRedirectURL("faqexecutive-list.jsp?msg=Stage moved down successfully!"));
            }
            if (!(faq_id.equals("0"))) {
                msg = msg + "<br>Results for FAQ";
                StrSearch = StrSearch + " and faq_id =" + faq_id + "";
            } else if (advSearch.equals("Search")) // for keyword search
            {
                msg = "Result for Search";
                StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
            }
            if (!StrSearch.equals("")) {
                SetSession("faqexestrsql", StrSearch, request);
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
        int PageListSize = 10;
        int StartRec = 0;
        int EndRec = 0;
        int TotalRecords = 0;
        String CountSql = "";
        String PageURL = "";
        StringBuilder Str = new StringBuilder();
        //Check PageCurrent is valid for parse int
        if (PageCurrents.equals("0")) {
            PageCurrents = "1";
        }
        PageCurrent = Integer.parseInt(PageCurrents);
        //to know  no of records depending on search
        StrSql = " Select faq_id, faq_cat_id, faq_question, faq_active, faq_rank, cat_name";
        CountSql = "SELECT Count(distinct faq_id)";
        StrJoin = " from " + compdb(comp_id) + "axela_faq"
                + " inner join " + compdb(comp_id) + "axela_faq_cat on cat_id = faq_cat_id"
                + " where 1=1";

        StrSql = StrSql + StrJoin;
        CountSql = CountSql + StrJoin;

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
            RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " FAQ(s)";
            if (QueryString.contains("PageCurrent") == true) {
                QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
            }
            PageURL = "faqexecutive-list.jsp?" + QueryString + "&PageCurrent=";
            PageCount = (TotalRecords / recperpage);
            if ((TotalRecords % recperpage) > 0) {
                PageCount = PageCount + 1;
            }
            PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
            StrSql = StrSql + " order by faq_rank";
            StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
//            SOP(StrSqlBreaker(StrSql));

            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = StartRec - 1;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>FAQ Details</th>\n");
                Str.append("<th>Category</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    String active = "";
                    count = count + 1;
                    if (crs.getString("faq_active").equals("0")) {
                        active = "<br><font color=red><b>[Inactive]</b></font>";
                    }
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center >").append(count).append("</td>\n");
                    Str.append("<td valign=top align=left >").append(crs.getString("faq_question")).append(active).append("</td>\n");
                    Str.append("<td valign=top align=left >").append(crs.getString("cat_name")).append("</td>\n");

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
            RecCountDisplay = "<br><br><br><br><font color=red>No FAQ(s) found!</font><br><br>";
        }
        return Str.toString();
    }

    public void moveup() {
        int tempRank;
        int faq_rank;
        try {
            faq_rank = Integer.parseInt(ExecuteQuery("SELECT faq_rank from " + compdb(comp_id) + "axela_faq where  faq_id=" + faq_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select min(faq_rank) as min1 from " + compdb(comp_id) + "axela_faq where 1=1"));
            if (faq_rank != tempRank) {
                tempRank = faq_rank - 1;
                StrSql = "update " + compdb(comp_id) + "axela_faq set faq_rank=" + faq_rank + " where  faq_rank=" + tempRank;
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_faq set faq_rank=" + tempRank + " where faq_rank=" + faq_rank + " and faq_id=" + faq_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void movedown() {
        int tempRank;
        int faq_rank;
        try {
            faq_rank = Integer.parseInt(ExecuteQuery("SELECT faq_rank from " + compdb(comp_id) + "axela_faq where faq_id=" + faq_id + ""));
            tempRank = Integer.parseInt(ExecuteQuery("select max(faq_rank) as max1 from " + compdb(comp_id) + "axela_faq where 1=1"));
            if (faq_rank != tempRank) {
                tempRank = faq_rank + 1;
                StrSql = "update " + compdb(comp_id) + "axela_faq set faq_rank=" + faq_rank + " where  faq_rank=" + tempRank + " ";
                updateQuery(StrSql);
                StrSql = "update " + compdb(comp_id) + "axela_faq set faq_rank=" + tempRank + " where  faq_rank=" + faq_rank + " and faq_id=" + faq_id + "";
                updateQuery(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateCategory() {
        try {
            String group = "";
            StrSql = "SELECT cat_id,cat_name from " + compdb(comp_id) + "axela_faq_cat"
                    + " where 1=1 order by cat_name ";
            CachedRowSet crs =processQuery(StrSql, 0);
            group = "<option value=0 >Select</option>";
            while (crs.next()) {
                group = group + "<option value=" + crs.getString("cat_id") + "";
                group = group + StrSelectdrop(crs.getString("cat_id"), faq_cat_id);
                group = group + ">" + crs.getString("cat_name") + "</option> \n";
            }
            crs.close();
            return group;
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
