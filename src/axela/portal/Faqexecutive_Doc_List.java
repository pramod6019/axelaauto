package axela.portal;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Faqexecutive_Doc_List extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String PageCurrents = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public String filename = "";
    public String filePath = "";
    public int recperpage = 0;
    public int PageCurrent = 0;
    public int PageCount = 10;
    public String QueryString = "";
    public String msg = "";
    public String doc_id = "0";
    public String doc_faq_id = "0";
    public String all = "";
    public String StrSearch = "";
    public String StrHTML = "";
    public String StrSql = "";
    public String faq_id = "0";
    public String faq_question = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            CheckPerm(comp_id, "emp_faq_access", request, response);
            PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
            QueryString = PadQuotes(request.getQueryString());
            msg = PadQuotes(request.getParameter("msg"));
            faq_id = CNumeric(PadQuotes(request.getParameter("faq_id")));
            doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
            all = PadQuotes(request.getParameter("all"));
            faq_question = ExecuteQuery("Select faq_question from " + compdb(comp_id) + "axela_faq where faq_id=" + faq_id);
            if (msg.toLowerCase().contains("delete")) {
                StrSearch = " AND doc_id = 0";
            } else if ("yes".equals(all) && RecCountDisplay.equals("")) {
                msg = "Results for all Documents.";
                StrSearch = StrSearch + " and doc_id > 0";
            } else if (!(doc_id.equals("0"))) {
                StrSearch = StrSearch + " and doc_id =" + doc_id + "";
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
        StringBuilder Str = new StringBuilder();
        String CountSql = "";
        String Sqljoin = "";
        int TotalRecords = 0;
        int StartRec = 0;
        int EndRec = 0;
        String PageURL = "";
        int PageListSize = 10;

        if ((PageCurrents.equals("0"))) {
            PageCurrents = "1";
        }
        PageCurrent = Integer.parseInt(PageCurrents);
        // to know  no of records depending on search
        StrSql = "Select doc_id, doc_value, doc_title, doc_remarks ";
        CountSql = "SELECT Count(distinct doc_id)";
        Sqljoin = " from " + compdb(comp_id) + "axela_faq_docs "
                + " join  " + compdb(comp_id) + "axela_faq on doc_faq_id=faq_id "
                + " where 1=1 and doc_faq_id=" + faq_id + "";
        StrSql = StrSql + Sqljoin;
        CountSql = CountSql + Sqljoin;
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
        if (!(StrSearch.equals(""))) {
            StrSql = StrSql + StrSearch;
            CountSql = CountSql + StrSearch;
        }
        if (TotalRecords != 0) {
            StartRec = ((PageCurrent - 1) * recperpage) + 1;
            EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

            if (EndRec > TotalRecords) {
                EndRec = TotalRecords;
            }
            RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
            if (QueryString.contains("PageCurrent") == true) {
                QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
            }
            PageURL = "faqexecutive-doc-list.jsp?" + QueryString + "&PageCurrent=";
            PageCount = (TotalRecords / recperpage);
            if ((TotalRecords % recperpage) > 0) {
                PageCount = PageCount + 1;
            }
            PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
            StrSql = StrSql + " order by doc_id desc";
            StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            try {
                int count = StartRec - 1;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Document Details</th>\n");
                Str.append("<th width=20%>Actions</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td height=20 valign=top align=center ><b>").append(count).append(".</b></td>\n");
                    if (!crs.getString("doc_value").equals("")) {
                        if (!new File(FaqExePath(comp_id)).exists()) {
                            new File(FaqExePath(comp_id)).mkdirs();
                        }
                        File f = new File(FaqExePath(comp_id) + crs.getString("doc_value"));
                        Str.append("<td height=20 valign=top align=left ><a href=../Fetchdocs.do?").append(QueryString).append("&faq_doc_id=").append(crs.getString("doc_id")).append("><b>").append(crs.getString("doc_title")).append(" (").append(crs.getString("doc_id")).append(") (").append(ConvertFileSizeToBytes(FileSize(f))).append(")</b></a><br> Remarks: ").append(crs.getString("doc_remarks")).append("</td>\n");// <a href=../Fetchdocs.do?+QueryString+&emp_doc_id="+ crs.getString("emp_doc_id") +" \"target=_blank>
                    } else {
                        Str.append("<td height=20 valign=top align=left ><b>").append(crs.getString("doc_title")).append(" (").append(crs.getString("doc_id")).append(") (0 Bytes)</b><br> Remarks: ").append(crs.getString("doc_remarks")).append("</td>\n");// <a href=../Fetchdocs.do?+QueryString+&emp_doc_id="+ crs.getString("emp_doc_id") +" \"target=_blank>
                    }
                    if (!faq_id.equals("0")) {
                        Str.append("<td height=20 valign=top align=left ><a href=\"../portal/docs-update.jsp?update=yes&faq_id=").append(faq_id).append("&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a></td>\n");
                    }
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
            msg = "";
            Str.append("<br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br>");
        }
        return Str.toString();
    }
}
