package axela.portal;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Contact_Invoice extends Connect {

    public String customer_id = "";
    public String enquiry_id = "";
    public String Strsearch = "";
    public String branch_id = "";
    public String BranchAccess = "";
    public String emp_idsession = "";
    public String comp_id = "0";
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
            enquiry_id = PadQuotes(request.getParameter("enquiry_id"));
            customer_id = PadQuotes(request.getParameter("customer_id"));
            if (!enquiry_id.equals("")) {
                CheckPerm(comp_id, "emp_enquiry_access", request, response);
                Strsearch = " and invoice_enquiry_id=" + enquiry_id;
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
        String StrSql = "select invoice_id, invoice_branch_id,concat(branch_code, invoice_no) as invoice_no, invoice_date, customer_name, "
                + " invoice_netamt, invoice_discamt, invoice_servicetax, invoice_cst,invoice_vat,invoice_netamt, invoice_grandtotal,  "
                + " branch_name, invoice_entry_id, invoice_entry_date,concat(emp_name,' (', emp_ref_no, ')') as emp_name,invoice_emp_id "
                + " from " + compdb(comp_id) + "axela_invoice "
                + " inner join " + compdb(comp_id) + "axela_customer on invoice_customer_id=customer_id "
                + " inner join " + compdb(comp_id) + "axela_branch on invoice_branch_id=branch_id "
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = invoice_emp_id "
                + " where invoice_active='1' and invoice_customer_id=" + customer_id
                + " " + BranchAccess + Strsearch + " order by invoice_id desc";
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                String discPercent = "";
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Invoice No.</th>\n");
                Str.append("<th>Date</th>\n");
                Str.append("<th>Product</th>\n");
                Str.append("<th>Net Total</th>\n");
                Str.append("<th>Discount</th>\n");
                Str.append("<th>Disc. %age</th>\n");
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
                    Str.append("<td valign=top align=left ><a href=invoice-list.jsp?invoice_id=").append(crs.getString("invoice_id")).append(" target=_parent>").append(crs.getString("invoice_no")).append("</a>" + "<br>Executive: <a href=javascript:WinPop('executives-summary.jsp?emp_id=").append(crs.getInt("invoice_emp_id")).append("','exec','50','50','500','500')>").append(crs.getString("emp_name")).append("</a></td>");
                    Str.append("<td valign=top align=center >").append(strToShortDate(crs.getString("invoice_date"))).append("</td>");
                    Str.append("<td valign=top align=left >");
                    Str.append(ProductList(crs.getString("invoice_id")));
                    Str.append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_netamt"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_discamt"))).append("</td>");
                    if (crs.getString("invoice_netamt").equals("0")) {
                        Str.append("<td valign=top align=right >100%</td>");
                    } else {
                        discPercent = deci.format((crs.getDouble("invoice_discamt") * 100) / crs.getDouble("invoice_netamt"));
                        Str.append("<td valign=top align=right >").append(discPercent).append("%</td>");
                    }

                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_servicetax"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_cst"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_vat"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("invoice_grandtotal"))).append("</td>");
                    Str.append("<td valign=top align=left nowrap><a href=\"receipt-update.jsp?Add=yes&receipt_invoice_id=").append(crs.getString("invoice_id")).append("&customer_id=").append(customer_id).append(" \" target=_parent> Add Receipt</a></td>");
                    Str.append("</tr>" + "\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<br><br><font color=red><b>No Invoice(s) found!</b></font>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    String ProductList(String invoice_id) {
        StringBuilder Str = new StringBuilder();
        try {
            String StrSql = " select prod_id, concat(prod_name, ' (',(case when prod_code='' then prod_id else prod_name end),')') as prod_name from " + compdb(comp_id) + "axela_invoice_prod "
                    + " inner join " + compdb(comp_id) + "axela_prod on prod_id = invprod_prod_id "
                    + " where invprod_invoice_id = " + invoice_id;
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
