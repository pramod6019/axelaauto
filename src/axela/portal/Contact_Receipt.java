package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Contact_Receipt extends Connect {

    public String customer_id = "";
    public String BranchAccess;
    public String comp_id = "0";
    public String branch_id = "";
    public String emp_idsession = "";
    public String StrHTML = "";
    public String enquiry_id = "";
    public String Strsearch = "";

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
        String SqlJoin = "";
        int TotalRecords = 0;

        String StrSql = " select receipt_id, receipt_amt, receipt_st, receipt_cst, receipt_vat, "
                + " receipt_total, concat(branch_code,receipt_no) as receipt_no,"
                + " group_concat(receipttrans_balancetrack_id separator ', ') as part,"
                + " so_id, so_active, coalesce(concat(branch_code,so_no),'') as so_no, receipt_date, "
                + " receipt_total,receipt_paymode_id "
                + " from " + compdb(comp_id) + "axela_invoice_receipt ";

        String CountSql = "select count(receipt_id) from " + compdb(comp_id) + "axela_invoice_receipt ";

        SqlJoin = " inner join " + compdb(comp_id) + "axela_invoice_receipt_trans on receipttrans_receipt_id=receipt_id"
                + " inner join " + compdb(comp_id) + "axela_sales_so on so_id=receipt_so_id "
                + " inner join " + compdb(comp_id) + "axela_branch on branch_id = receipt_branch_id "
                + " inner join " + compdb(comp_id) + "axela_customer on customer_id = receipt_customer_id"
                + " where 1=1 and receipt_active='1' and receipt_customer_id=" + customer_id + ""
                + " " + BranchAccess + Strsearch + " order by receipt_date desc";

        StrSql = StrSql + SqlJoin;
        CountSql = CountSql + SqlJoin;
//                SOP("StrSql...from receipt details......"+StrSqlBreaker(CountSql));
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
        if (TotalRecords != 0) {
            CachedRowSet crs =processQuery(StrSql, 0);
            try {
                int count = 0;
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Receipt No.</th>\n");
                Str.append("<th>SO No.</th>\n");
                Str.append("<th>Particulars</th>\n");
                Str.append("<th>Date</th>\n");
                Str.append("<th>Net Total</th>\n");
                Str.append("<th>ST</th>\n");
                Str.append("<th>CST</th>\n");
                Str.append("<th>VAT</th>\n");
                Str.append("<th>Grand Total</th>\n");
                Str.append("<th>Payment</th>\n");
                Str.append("</tr>\n");

                while (crs.next()) {
                    if (!((crs.getString("so_id") != null
                            && !crs.getString("so_id").equals("")
                            && crs.getString("so_active").equals("0")))) {
                        count++;
                        Str.append("<tr>\n");
                        Str.append("<td valign=top align=center >").append(count).append("</td> ");
                        Str.append("<td valign=top align=center ><a href=receipt-list.jsp?receipt_id=").append(crs.getString("receipt_id")).append(" target=_parent>").append(crs.getString("receipt_no")).append("</a></td> ");
                        Str.append("<td valign=top align=center >");
                        if (crs.getString("so_id") != null) {
                            Str.append("<a href=invoice-list.jsp?so_id=").append(crs.getString("so_id")).append(" target=_parent>").append(crs.getString("so_no")).append("</a>");
                        }
                        Str.append("&nbsp;</td> ");
                        Str.append("<td valign=top align=left nowrap>" + /*ReturnParticluar(crs.getString("part"))*/ "" + "</td>");
                        Str.append("<td valign=top align=center >").append(strToShortDate(crs.getString("receipt_date"))).append("</td>");
                        Str.append("<td valign=top align=right >").append(IndDecimalFormat(crs.getString("receipt_amt"))).append("</td>");
                        Str.append("<td valign=top align=right >").append(IndDecimalFormat(crs.getString("receipt_st"))).append("</td>");
                        Str.append("<td valign=top align=right >").append(IndDecimalFormat(crs.getString("receipt_cst"))).append("</td>");
                        Str.append("<td valign=top align=right >").append(IndDecimalFormat(crs.getString("receipt_vat"))).append("</td>");
                        Str.append("<td valign=top align=right >").append(IndDecimalFormat(crs.getString("receipt_total"))).append("</td>");
                        Str.append("<td valign=top align=center >By ").append(PaymentMode(crs.getInt("receipt_paymode_id"))).append("</td> ");
                        Str.append("</tr>" + "\n");
                    }
                }
                Str.append("</table>\n");
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        } else {
            Str.append("<br><br><font color=red><b>No Receipts(s) found!</b></font>");
        }
        return Str.toString();
    }
// public String ReturnParticluar(String part) {
//        if (part.equals("0")) {
//            return "Booking Amount";
//        } else if (part.contains(", 0")) {
//            return "Booking Amount,<br>"+Instlbl+"";
//        } else {
//            return ""+Instlbl+"";
//        }
//    }
}
