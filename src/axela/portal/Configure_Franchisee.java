package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Configure_Franchisee extends Connect {

    public String updateB = "";
    public String StrSql = "";
    public String msg = "";
    public String comp_id = "0";
    public String name = "";
    public String config_sales_leads = "";
    public String config_sales_enquiry = "";
    public String config_sales_quote = "";
    public String config_sales_salesorder = "";
    public String config_invoice_invoice = "";
    public String config_sales_balancepayments = "";
    public String config_sales_campaign = "";
    public String config_sales_target = "";
    public String config_sales_teams = "";
    public String config_sales_lead_for_enquiry = "";
    public String config_sales_enquiry_for_quote = "";
    public String config_sales_quote_for_so = "";
//    public String config_itemrow_count = "";
//    public String confipublicg_servicetax = "";
    public String config_sales_soe = "";
    public String config_sales_sob = "";
//    public String config_balancerow_count = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_role_id", request, response);
            updateB = PadQuotes(request.getParameter("update_button"));
            msg = PadQuotes(request.getParameter("msg"));
            PopulateConfigDetails();
            PopulateFields();
            if ("Update".equals(updateB)) {
                GetValues(request, response);

                if (PadQuotes(request.getParameter("chk_config_sales_leads")).equals("")
                        && PadQuotes(request.getParameter("chk_config_sales_enquiry")).equals("")
                        && PadQuotes(request.getParameter("chk_config_sales_quote")).equals("")
                        && PadQuotes(request.getParameter("chk_config_sales_salesorder")).equals("")) {
                    msg = " Error! <br> Select atleast one among Leads Enquiry Quotes and Sales Order!";
                }
                if (msg.equals("")) {
                    UpdateFields(request);
                    response.sendRedirect(response.encodeRedirectURL("configure-sales.jsp?&msg=Configuration details updated successfully for Sales!"));
                }
            }
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

        config_sales_leads = PadQuotes(request.getParameter("chk_config_sales_leads"));
        config_sales_enquiry = PadQuotes(request.getParameter("chk_config_sales_enquiry"));
        config_sales_quote = PadQuotes(request.getParameter("chk_config_sales_quote"));
        config_sales_salesorder = PadQuotes(request.getParameter("chk_config_sales_salesorder"));
        config_invoice_invoice = PadQuotes(request.getParameter("chk_config_invoice_invoice"));
        config_sales_balancepayments = PadQuotes(request.getParameter("chk_config_sales_balancepayments"));
        config_sales_campaign = PadQuotes(request.getParameter("chk_config_sales_campaign"));
        config_sales_target = PadQuotes(request.getParameter("chk_config_sales_target"));
        config_sales_teams = PadQuotes(request.getParameter("chk_config_sales_teams"));
        config_sales_soe = PadQuotes(request.getParameter("chk_config_sales_soe"));
        config_sales_sob = PadQuotes(request.getParameter("chk_config_sales_sob"));
        config_sales_lead_for_enquiry = PadQuotes(request.getParameter("chk_config_sales_lead_for_enquiry"));
        config_sales_enquiry_for_quote = PadQuotes(request.getParameter("chk_config_sales_enquiry_for_quote"));
        config_sales_quote_for_so = PadQuotes(request.getParameter("chk_config_sales_quote_for_so"));

        if (config_sales_leads.equals("on")) {
            config_sales_leads = "1";
        } else {
            config_sales_leads = "0";
        }
        if (config_sales_enquiry.equals("on")) {
            config_sales_enquiry = "1";
        } else {
            config_sales_enquiry = "0";
        }

        if (config_sales_quote.equals("on")) {
            config_sales_quote = "1";
        } else {
            config_sales_quote = "0";
        }
        if (config_sales_salesorder.equals("on")) {
            config_sales_salesorder = "1";
        } else {
            config_sales_salesorder = "0";
        }

        if (config_invoice_invoice.equals("on")) {
            config_invoice_invoice = "1";
        } else {
            config_invoice_invoice = "0";
        }

        if (config_sales_balancepayments.equals("on")) {
            config_sales_balancepayments = "1";
        } else {
            config_sales_balancepayments = "0";
        }
        if (config_sales_campaign.equals("on")) {
            config_sales_campaign = "1";
        } else {
            config_sales_campaign = "0";
        }

        if (config_sales_target.equals("on")) {
            config_sales_target = "1";
        } else {
            config_sales_target = "0";
        }
        if (config_sales_teams.equals("on")) {
            config_sales_teams = "1";
        } else {
            config_sales_teams = "0";
        }
        if (config_sales_soe.equals("on")) {
            config_sales_soe = "1";
        } else {
            config_sales_soe = "0";
        }
        if (config_sales_sob.equals("on")) {
            config_sales_sob = "1";
        } else {
            config_sales_sob = "0";
        }

        if (config_sales_lead_for_enquiry.equals("on")) {
            config_sales_lead_for_enquiry = "1";
        } else {
            config_sales_lead_for_enquiry = "0";
        }
        if (config_sales_enquiry_for_quote.equals("on")) {
            config_sales_enquiry_for_quote = "1";
        } else {
            config_sales_enquiry_for_quote = "0";
        }
        if (config_sales_quote_for_so.equals("on")) {
            config_sales_quote_for_so = "1";
        } else {
            config_sales_quote_for_so = "0";
        }
    }

    protected void PopulateFields() {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_config ";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                config_sales_leads = crs.getString("config_sales_leads");
                config_sales_enquiry = crs.getString("config_sales_enquiry");
                config_sales_quote = crs.getString("config_sales_quote");
                config_sales_salesorder = crs.getString("config_sales_salesorder");
                config_invoice_invoice = crs.getString("config_invoice_invoice");
                config_sales_balancepayments = crs.getString("config_sales_balancepayments");
                config_sales_campaign = crs.getString("config_sales_campaign");
                config_sales_target = crs.getString("config_sales_target");
                config_sales_teams = crs.getString("config_sales_teams");
                config_sales_soe = crs.getString("config_sales_soe");
                config_sales_sob = crs.getString("config_sales_sob");
                config_sales_lead_for_enquiry = crs.getString("config_sales_lead_for_enquiry");
                config_sales_enquiry_for_quote = crs.getString("config_sales_enquiry_for_quote");
                config_sales_quote_for_so = crs.getString("config_sales_quote_for_so");
                
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields(HttpServletRequest request) {
        try {
            StrSql = "UPDATE " + compdb(comp_id) + "axela_config Set "
                    + "config_sales_leads = '" + config_sales_leads + "',"
                    + "config_sales_enquiry = '" + config_sales_enquiry + "',"
                    + "config_sales_quote = '" + config_sales_quote + "',"
                    + "config_sales_salesorder = '" + config_sales_salesorder + "',"
                    + "config_invoice_invoice = '" + config_invoice_invoice + "',"
                    + "config_sales_balancepayments = '" + config_sales_balancepayments + "',"
                    + "config_sales_campaign = '" + config_sales_campaign + "',"
                    + "config_sales_target = '" + config_sales_target + "',"
                    + "config_sales_teams = '" + config_sales_teams + "',"
                    + "config_sales_soe = '" + config_sales_soe + "',"
                    + "config_sales_sob = '" + config_sales_sob + "',"
                    + "config_sales_lead_for_enquiry = '" + config_sales_lead_for_enquiry + "',"
                    + "config_sales_enquiry_for_quote = '" + config_sales_enquiry_for_quote + "',"
                    + "config_sales_quote_for_so = '" + config_sales_quote_for_so + "'";
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void PopulateConfigDetails() {
        StrSql = "select  config_sales_soe, config_sales_sob from " + compdb(comp_id) + "axela_config";
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            while (crs.next()) {
                config_sales_soe = crs.getString("config_sales_soe");
                config_sales_sob = crs.getString("config_sales_sob");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
