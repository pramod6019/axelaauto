package axela.preowned;
//Bhagwan Singh 27th June 2013

//import axela.sales.Enquiry_Dash_Customer;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Preowned_Dash_Customer extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String msg = "";
    public String preowned_id = "0";
    public String preowned_title = "";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String StrSql = "";
    public String customer_name = "";
    public String customer_id = "0";
    public String preowned_customer_id = "0";
    public String comp_email_enable = "";
    public String config_email_enable = "";
    public String comp_sms_enable = "";
    public String config_sms_enable = "";
    public Enquiry_Dash_Customer preowneddash = new Enquiry_Dash_Customer();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                BranchAccess = GetSession("BranchAccess", request);
                ExeAccess = GetSession("ExeAccess", request);
                CheckPerm(comp_id, "emp_preowned_access", request, response);
                preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));

                StrSql = "SELECT preowned_customer_id, COALESCE(preowned_title, '') AS preowned_title"
                        + " FROM " + compdb(comp_id) + "axela_preowned"  
                        + " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id"
                        + " WHERE 1 = 1" + " AND preowned_id =" + preowned_id + BranchAccess + ExeAccess + ""
                        + " GROUP BY preowned_id"
                        + " ORDER BY preowned_id DESC";

                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    while (crs.next()) {
                        preowned_title = crs.getString("preowned_title");
                        customer_id = crs.getString("preowned_customer_id");
                    }
                } else {
                    response.sendRedirect("../portal/error.jsp?msg=Invalid Pre Owned!");
                }
                crs.close();
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

    protected void PopulateConfigDetails() {

        StrSql = "SELECT COALESCE(comp_email_enable,'') AS comp_email_enable,"
                + " COALESCE(comp_sms_enable,'') AS comp_sms_enable,"
                + " COALESCE(config_email_enable,'') AS config_email_enable,"
                + " COALESCE(config_sms_enable,'') AS config_sms_enable"
                + " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
                + " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + emp_id;
//        SOP(StrSqlBreaker(StrSql));
        try {
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                comp_email_enable = crs.getString("comp_email_enable");
                comp_sms_enable = crs.getString("comp_sms_enable");
                config_email_enable = crs.getString("config_email_enable");
                config_sms_enable = crs.getString("config_sms_enable");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
