package axela.portal;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Contact_Salesorder extends Connect {

    public String customer_id = "";
    public String enquiry_id = "";
    public String Strsearch = "";
    public String branch_id = "";
    public String BranchAccess = "";
    public String emp_idsession = "";
    public String StrHTML = "";
    public String comp_id = "0";
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
                Strsearch = " and so_enquiry_id=" + enquiry_id;
                CheckPerm(comp_id, "emp_enquiry_access", request, response);
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
        String StrSql = "select so_id, so_branch_id,concat(branch_code, so_no) as so_no, so_date, customer_name, "
                + " so_netamt, so_discamt, so_servicetax, so_cst,so_vat,so_netamt, so_grandtotal, so_mga_amount, "
                + " branch_name, so_entry_id, so_entry_date,concat(emp_name,' (', emp_ref_no, ')') as emp_name, so_emp_id "
                + " from " + compdb(comp_id) + "axela_sales_so "
                + " inner join " + compdb(comp_id) + "axela_customer on so_customer_id=customer_id "
                + " inner join " + compdb(comp_id) + "axela_branch on so_branch_id=branch_id "
                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id "
                + " where so_active='1' and so_customer_id=" + customer_id + ""
                + " " + BranchAccess + Strsearch + " order by so_id desc";
//         SOP("ssssssssssss"+StrSqlBreaker(StrSql));
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                String discPercent = "";
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>SO No.</th>\n");
                Str.append("<th>Date</th>\n");
                Str.append("<th>Product</th>\n");
                Str.append("<th>Net Total</th>\n");
                Str.append("<th>Discount</th>\n");
                Str.append("<th>Disc. %age</th>\n");
                Str.append("<th>ST</th>\n");
                Str.append("<th>CST</th>\n");
                Str.append("<th>VAT</th>\n");
                Str.append("<th>Grand Total</th>\n");
                Str.append("<th>MGA Amount</th>\n");
                Str.append("<th>Action</th>\n");
                Str.append("</tr>\n");

                while (crs.next()) {
                    count++;
                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=center >").append(count).append("</td>");
                    Str.append("<td valign=top align=left ><a href=invoice-list.jsp?so_id=").append(crs.getString("so_id")).append(" target=_parent>").append(crs.getString("so_no")).append("</a>" + "<br>Executive: <a href=javascript:WinPop('executive-summary.jsp?emp_id=").append(crs.getInt("so_emp_id")).append("','exec','50','50','500','500')>").append(crs.getString("emp_name")).append("</a></td>");
                    Str.append("<td valign=top align=center >").append(strToShortDate(crs.getString("so_date"))).append("</td>");
                    Str.append("<td valign=top align=left >");
                    Str.append(ProductList(crs.getString("so_id")));
                    Str.append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_netamt"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_discamt"))).append("</td>");
                    if (crs.getString("so_netamt").equals("0")) {
                        Str.append("<td valign=top align=right >100%</td>");
                    } else {
                        discPercent = deci.format((crs.getDouble("so_discamt") * 100) / crs.getDouble("so_netamt"));
                        Str.append("<td valign=top align=right >").append(discPercent).append("%</td>");
                    }

                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_servicetax"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_cst"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_vat"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_grandtotal"))).append("</td>");
                    Str.append("<td valign=top align=right >").append(IndFormat(crs.getString("so_mga_amount"))).append("</td>");
                    Str.append("<td valign=top align=left nowrap><a href=\"receipt-update.jsp?Add=yes&so_id=").append(crs.getString("so_id")).append("&customer_id=").append(customer_id).append(" \" target=_parent> Add Receipt</a>");
                    Str.append("<br><a href=\"salesorder-installtrack.jsp?so_id=").append(crs.getString("so_id")).append("\" target=_parent>Balance Track</a></td>");
                    Str.append("</tr>" + "\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<br><br><font color=red><b>No Sales Order(s) found!</b></font>");
            }

            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    String ProductList(String so_id) {
        StringBuilder Str = new StringBuilder();
        try {
            String StrSql = " select prod_id, concat(prod_name,' (',(case when prod_code='' then prod_id else prod_code end),')') as prod_name ,prod_code from " + compdb(comp_id) + "axela_sales_so_prod "
                    + " inner join " + compdb(comp_id) + "axela_prod on prod_id = soprod_prod_id "
                    + " where soprod_so_id = " + so_id;
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
