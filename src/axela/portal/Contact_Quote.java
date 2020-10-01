package axela.portal;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Contact_Quote extends Connect {

    public String customer_id = "";
    public String comp_id = "0";
    public String enquiry_id = "";
    public String Strsearch = "";
    public String branch_id = "";
    public String BranchAccess = "";
    public String emp_idsession = "";
    public String StrHTML = "";
    DecimalFormat deci = new DecimalFormat("#.##");

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            customer_id = PadQuotes(request.getParameter("customer_id"));
            enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
            if (!enquiry_id.equals("")) {
                CheckPerm(comp_id, "emp_enquiry_access", request, response);
                Strsearch = " and quote_enquiry_id=" + enquiry_id;
            }
            if (!customer_id.equals("")) {
                CheckPerm(comp_id, "emp_customer_access", request, response);
                StrHTML = ListData();
            }
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String ListData() {
        StringBuilder Str = new StringBuilder();
        int count = 0;
        String StrSql = "select quote_id, quote_branch_id,concat(branch_code, quote_no) as quote_no, quote_date,quote_cst,quote_vat, customer_name, "
                + " quote_netamt, quote_servicetax, quote_grandtotal, concat(emp_name,' (', emp_ref_no, ')') as emp_name, quote_emp_id,"
                + " branch_name, quote_entry_id, quote_entry_date, quote_auth"
                + " from " + compdb(comp_id) + "axela_sales_quote "
                + " inner join " + compdb(comp_id) + "axela_customer on quote_customer_id=customer_id "
                + " inner join " + compdb(comp_id) + "axela_branch on quote_branch_id=branch_id "
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = quote_emp_id "
                + " where quote_active='1' and quote_auth='1' and quote_customer_id=" + customer_id + ""
                + " " + BranchAccess + Strsearch + " order by quote_id desc";
//         SOP("ssssssssssss"+StrSql);
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                String discPercent = "";
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Quote No.</th>\n");
                Str.append("<th>Date</th>\n");
                Str.append("<th>Product</th>\n");
                Str.append("<th>Net Total</th>\n");
                Str.append("<th>ST</th>\n");
                Str.append("<th>CST</th>\n");
                Str.append("<th>VAT</th>\n");
                Str.append("<th>Grand Total</th>\n");
                Str.append("<th>Action</th>\n");
                Str.append("</tr>\n");

                while (crs.next()) {
                    count++;

                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center >").append(count).append("</td>");
                    Str.append("<td valign=top align=left ><a href=quote-list.jsp?quote_id=").append(crs.getString("quote_id")).append(" target=_parent>").append(crs.getString("quote_no")).append("</a>" + "<br>Executive: <a href=javascript:WinPop('executive-summary.jsp?emp_id=").append(crs.getInt("quote_emp_id")).append("','exec','50','50','500','500')>").append(crs.getString("emp_name")).append("</a></td>");
                    Str.append("<td valign=top align=center >").append(strToShortDate(crs.getString("quote_date"))).append("</td>");
                    Str.append("<td valign=top align=left >");
                    Str.append(ProductList(crs.getString("quote_id")));
                    Str.append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("quote_netamt"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("quote_servicetax"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("quote_cst"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("quote_vat"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("quote_grandtotal"))).append("</td>");
                    Str.append("<td valign=top align=left nowrap><a href=\"quote-update.jsp?Update=yes&quote_id=").append(crs.getString("quote_id")).append("&customer_id=").append(customer_id).append(" \" target=_parent>Update Quote</a>" + "<br><a href=\"salesorder-update.jsp?Add=yes&quote_id=").append(crs.getString("quote_id")).append(" \"> Add Sales Order</a>");
                    if (crs.getString("quote_auth").equals("1")) {
                        Str.append("<br><a href=\"quote-email.jsp?quote_id=").append(crs.getString("quote_id")).append(" \">Email Quote</a><br>" + "<a href=\"../Quote_Report.do?exporttype=pdf&report=quotePrint1&quote_id=").append(crs.getString("quote_id")).append("&target=").append(Math.random()).append("\" target=_blank>Print Quote 1</a>"
                                + //    "<br><a href=\"../Quote_Report.do?exporttype=pdf&report=quotePrint2&quote_id=" + crs.getString("quote_id") + "&target=" + Math.random() + "\" target=_blank>Print Quote 2</a>"+
                                "");
                    }
                    Str.append("</td></tr>\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<br><br><font color=red><b>No Quote(s) found!</b></font>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    String ProductList(String quote_id) {
        StringBuilder Str = new StringBuilder();
        try {
            String StrSql = " select prod_id, concat(prod_name, ' (',(case when prod_code='' then prod_id else prod_name end),')') as prod_name, prod_code from " + compdb(comp_id) + "axela_sales_quote_item "
                    + " inner join " + compdb(comp_id) + "axela_prod on prod_id = quoteprod_prod_id "
                    + " where quoteprod_quote_id = " + quote_id;
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<a href=\"javascript:WinPop('course-details.jsp?prod_id=").append(crs.getString("prod_id")).append("','coursedetails','50','50','800','600');\">").append(crs.getString("prod_name")).append("</a><br>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }
}
