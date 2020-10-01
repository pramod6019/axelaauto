package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managedocs extends Connect {

    public String emp_id = "", branch_id = "";
    public String StrHTML = "";
    public String search = "";
    public String all = "";
    public String comp_id = "0";
    public String msg = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public String filename = "", filePath = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String txt_search = "";
    public String client_id = "";
    public String module_id = "";
    public String proj_id = "";
    public String name = "";
    public String doc_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            HttpSession session = request.getSession(true);
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
                branch_id = CNumeric(GetSession("emp_branch_id", request));
                PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
                QueryString = PadQuotes(request.getQueryString());
                search = PadQuotes(request.getParameter("search_button"));
                msg = PadQuotes(request.getParameter("msg"));
                client_id = PadQuotes(request.getParameter("client_id"));
                module_id = PadQuotes(request.getParameter("module_id"));
                proj_id = PadQuotes(request.getParameter("proj_id"));
                doc_id = PadQuotes(request.getParameter("doc_id"));
                all = PadQuotes(request.getParameter("All"));

                if (!client_id.equals("0") && !isNumeric(client_id)) {
                    client_id = "0";
                }
                if (!module_id.equals("0") && !isNumeric(module_id)) {
                    module_id = "0";
                }
                if (!proj_id.equals("0") && !isNumeric(proj_id)) {
                    proj_id = "0";
                }
                if (!doc_id.equals("0") && !isNumeric(doc_id)) {
                    doc_id = "0";
                }
                if (search == null) {
                    search = "";
                }
                if (msg == null) {
                    msg = "";
                }
                if ("Search".equals(search)) // for keyword search
                {
                    txt_search = PadQuotes(request.getParameter("txt_search"));
                    StrSearch = StrSearch + " and (doc_name like '%" + txt_search + "%')";
                }
                if (client_id != null && !client_id.equals("")) // for keyword search
                {
                    name = ExecuteQuery("select concat(client_firm_name,' (',client_id,')') from " + compdb(comp_id) + "axela_client where client_id=" + client_id + "");
                    StrSearch = StrSearch + " and client_id =" + client_id + "";
                }
                if (module_id != null && !module_id.equals("")) // for keyword search
                {
                    name = ExecuteQuery("select concat(module_name,' (',module_id,')') from " + maindb() + "module where module_id=" + module_id + "");
                    StrSearch = StrSearch + " and module_id =" + module_id + "";
                }
                if (proj_id != null && !proj_id.equals("")) // for keyword search
                {
                    name = ExecuteQuery("select concat(proj_name,' (',proj_id,')') from " + compdb(comp_id) + "axela_project where proj_id=" + proj_id + "");
                    StrSearch = StrSearch + " and proj_id =" + proj_id + "";
                }
                if (msg.toLowerCase().contains("delete")) {
                    StrSearch = " AND doc_id = 0";
                } else if ("yes".equals(all)) {
                    StrSearch = StrSearch + " and doc_id > 0 ";
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
        if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
            PageCurrents = "1";
        }
        PageCurrent = Integer.parseInt(PageCurrents);

        // to know  no of records depending on search
        if (client_id != null && !client_id.equals("")) {
            StrSql = "Select pm_doc.* from " + compdb(comp_id) + "axela_doc where 1 = 1 and doc_client_id=" + client_id + " ";
            CountSql = "SELECT Count(distinct doc_id) from " + compdb(comp_id) + "axela_doc where 1=1 and doc_client_id=" + client_id + "";
        }
        if (proj_id != null && !proj_id.equals("")) {
            StrSql = "Select pm_doc.* from " + compdb(comp_id) + "axela_doc join " + compdb(comp_id) + "axela_project on doc_proj_id=proj_id where 1 = 1 and proj_valid=1 and doc_proj_id=" + proj_id + " ";
            CountSql = "SELECT Count(distinct doc_id) from " + compdb(comp_id) + "axela_doc where 1=1 and doc_proj_id=" + proj_id + "";
        }
        if (module_id != null && !module_id.equals("")) {
            StrSql = "Select pm_doc.* from " + compdb(comp_id) + "axela_doc join " + maindb() + "module on doc_module_id=module_id where 1 = 1 and doc_module_id=" + module_id + "";
            CountSql = "SELECT Count(distinct doc_id) from " + compdb(comp_id) + "axela_doc join " + maindb() + "module on doc_module_id=module_id where 1 = 1 and doc_module_id=" + module_id + "";
        }
//			if(!(StrSearch.equals("")))
//				{
//					StrSql = StrSql + StrSearch;
//		            CountSql = CountSql + StrSearch;
//				}
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

        if (TotalRecords != 0) {
            StartRec = ((PageCurrent - 1) * recperpage) + 1;
            EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
            //if limit ie. 10 > totalrecord
            if (EndRec > TotalRecords) {
                EndRec = TotalRecords;
            }

            RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
            if (QueryString.contains("PageCurrent") == true) {
                QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
            }
            PageURL = "managedocs.jsp?" + QueryString + "&PageCurrent=";
            PageCount = (TotalRecords / recperpage);
            if ((TotalRecords % recperpage) > 0) {
                PageCount = PageCount + 1;
            }
            // display on jsp page

            PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
            StrSql = StrSql + " order by doc_id desc";
            StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = StartRec - 1;
                Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Document Details</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count = count + 1;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center ><b>").append(count).append(".</b></td>\n");
                    Str.append("<td valign=top align=left ><a href=../../propget/Fetchhomedocs?+QueryString+&doc_id=").append(crs.getString("doc_id")).append(" \"target=_blank>").append(crs.getString("doc_name")).append("</a><br> Remarks:").append(crs.getString("doc_remarks")).append("</td>\n");
                    if (client_id != null && !client_id.equals("")) {
                        Str.append("<td valign=top align=center ><a href=\"#\" onClick=\"WinPop('managedocs-update.jsp?Update=yes&client_id=").append(client_id).append("&doc_id=").append(crs.getString("doc_id")).append("','document','50','50','500','300')\">Update Document</a></td>\n");
                    }
                    if (module_id != null && !module_id.equals("")) {
                        Str.append("<td valign=top align=center ><a href=\"#\" onClick=\"WinPop('managedocs-update.jsp?Update=yes&module_id=").append(module_id).append("&doc_id=").append(crs.getString("doc_id")).append("','document','50','50','500','300')\">Update Document</a></td>\n");
                    }
                    if (proj_id != null && !proj_id.equals("")) {
                        Str.append("<td valign=top align=center ><a href=\"#\" onClick=\"WinPop('managedocs-update.jsp?Update=yes&proj_id=").append(proj_id).append("&doc_id=").append(crs.getString("doc_id")).append("','document','50','50','500','300')\">Update Document</a></td>\n");
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
            RecCountDisplay = "<br><br><br><br><font color=red>No Document(s) found!</font><br><br>";
        }


        return Str.toString();
    }
}
