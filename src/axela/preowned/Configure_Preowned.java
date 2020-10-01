package axela.preowned;
// sangita
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Configure_Preowned extends Connect {

    public String updateB = "";
    public String StrSql = "";
    public String comp_id = "0";
    public String msg = "";
    public String name = "";
    public String config_preowned_campaign = "";
    public String config_preowned_refno = "";
    public String config_preowned_soe = "";
    public String config_preowned_sob = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	CheckPerm(comp_id, "emp_role_id", request, response);
                updateB = PadQuotes(request.getParameter("update_button"));
                msg = PadQuotes(request.getParameter("msg"));
                PopulateConfigDetails();
                PopulateFields();
                if ("yes".equals(updateB)) {
                    GetValues(request, response);

                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    }
                    if (msg.equals("")) {
                        UpdateFields(request);
                        response.sendRedirect(response.encodeRedirectURL("configure-preowned.jsp?msg=Configuration details updated successfully for Pre Owned!"));
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

        config_preowned_campaign = PadQuotes(request.getParameter("chk_config_preowned_campaign"));
        config_preowned_refno = PadQuotes(request.getParameter("chk_config_preowned_refno"));
        config_preowned_soe = PadQuotes(request.getParameter("chk_config_preowned_soe"));
        config_preowned_sob = PadQuotes(request.getParameter("chk_config_preowned_sob"));

        if (config_preowned_campaign.equals("on")) {
            config_preowned_campaign = "1";
        } else {
            config_preowned_campaign = "0";
        }
        if (config_preowned_refno.equals("on")) {
            config_preowned_refno = "1";
        } else {
            config_preowned_refno = "0";
        }
        if (config_preowned_soe.equals("on")) {
            config_preowned_soe = "1";
        } else {
            config_preowned_soe = "0";
        }
        if (config_preowned_sob.equals("on")) {
            config_preowned_sob = "1";
        } else {
            config_preowned_sob = "0";
        }
    }

    protected void PopulateFields() {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_config";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                config_preowned_campaign = crs.getString("config_preowned_campaign");
                config_preowned_refno = crs.getString("config_preowned_refno");
                config_preowned_soe = crs.getString("config_preowned_soe");
                config_preowned_sob = crs.getString("config_preowned_sob");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields(HttpServletRequest request) {
        try {
            StrSql = "UPDATE " + compdb(comp_id) + "axela_config"
                    + " SET"
                    + " config_preowned_campaign = '" + config_preowned_campaign + "',"
                    + " config_preowned_refno = '" + config_preowned_refno + "',"
                    + " config_preowned_soe = '" + config_preowned_soe + "',"
                    + " config_preowned_sob = '" + config_preowned_sob + "'";
//            SOP("StrSql=="+StrSql);
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void PopulateConfigDetails() {
        StrSql = "select  config_preowned_soe, config_preowned_sob"
                + " from " + compdb(comp_id) + "axela_config";
        CachedRowSet crs =processQuery(StrSql, 0);
        try {
            while (crs.next()) {
                config_preowned_soe = crs.getString("config_preowned_soe");
                config_preowned_sob = crs.getString("config_preowned_sob");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
