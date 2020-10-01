package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Ticket_Dash_Customer extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String msg = "";      
    public String branch_id = "0";
    public String ticket_id = "0";
    public String ticket_subject = "";
//    public String BranchAccess = "";
    public String ExeAccess = "";
    public String StrSql = "";
    public String customer_id = "0";
    public Enquiry_Dash_Customer ticketdash = new Enquiry_Dash_Customer();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
//            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            //CheckPerm(comp_id, "emp_ticket_access", request, response);
            ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
//            group = PadQuotes(request.getParameter("group"));
//            SOP("ticket_id--" + ticket_id);
            StrSql = "SELECT ticket_subject, ticket_customer_id"
                    + " FROM " + compdb(comp_id) + "axela_service_ticket"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id"
                    + " WHERE 1 = 1 AND ticket_id = " + ticket_id + ExeAccess + ""
                    + " GROUP BY ticket_id"
                    + " ORDER BY ticket_id DESC";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    ticket_subject = crs.getString("ticket_subject");
                    customer_id = crs.getString("ticket_customer_id");
                }
            } else {
                response.sendRedirect("../portal/error.jsp?msg=Invalid Ticket!");
            }
            crs.close();
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
}
