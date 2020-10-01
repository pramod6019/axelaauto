package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Check extends Connect {

    public String branch_id = "";
    public String call_branch_id = "";
    public String emp = "";
    public String comp_id = "0";
    public String po_branch_id = "";
    public String enquiry_branch_id = "";
    public String enquiry_emp_id = "";
    public String StrSql = "";
    public String sales = "";
    public String StrHTML = "";
    public String for_po = "";
    public String dr = "", dr1 = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")) {
            	enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("enquiry_branch_id")));
                branch_id = PadQuotes(request.getParameter("lead_branch_id"));
                call_branch_id = PadQuotes(request.getParameter("call_branch_id"));
                emp = PadQuotes(request.getParameter("emp"));
                po_branch_id = CNumeric(PadQuotes(request.getParameter("po_branch_id")));
                sales = PadQuotes(request.getParameter("emp_sales"));
                enquiry_emp_id = CNumeric(PadQuotes(request.getParameter("enquiry_emp_id")));
                dr = PadQuotes(request.getParameter("dr_emp_id"));
                for_po = PadQuotes(request.getParameter("for_po"));
                dr1 = PadQuotes(request.getParameter("dr1"));

                if (!po_branch_id.equals("0") && for_po.equals("yes")) {
                    String str = "";
                    StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                            + " FROM " + compdb(comp_id) + "axela_emp"
                            + " WHERE emp_active = '1' and (emp_branch_id = " + po_branch_id + " or emp_id = 1"
                            + " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                            + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                            + " and empbr.emp_branch_id = " + po_branch_id + "))"
                            + " group by emp_id"
                            + " order by emp_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " <option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("emp_id") + "";
                            str = str + ">" + crs.getString("emp_name") + " </option> \n";
                        }
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }
                if (!call_branch_id.equals("") && emp.equals("yes")) {
                    String str = "";
                    StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                            + " from " + compdb(comp_id) + "axela_emp"
                            + " where emp_active = '1' and (emp_branch_id = " + call_branch_id + ""
                            + " or emp_id in (select empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                            + " where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + call_branch_id + "))"
                            + " group by emp_id" 
                            + " order by emp_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " <select class=selectbox name=\"dr_call_emp_id\" id=\"dr_call_emp_id\"><option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("emp_id") + "";
                            str = str + ">" + crs.getString("emp_name") + " </option> \n";
                        }
                        str = str + "</select>";
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }
                if (!branch_id.equals("")) {
                    String str = "";
                    StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                            + " from " + compdb(comp_id) + "axela_emp"
                            + " where emp_sales = 1 and emp_active = '1' and (emp_branch_id = " + branch_id + ""
                            + " or emp_id in (select empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                            + " where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + branch_id + "))"
                            + " group by emp_id"
                            + " order by emp_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " ><option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("emp_id") + "";
                            str = str + ">" + crs.getString("emp_name") + " </option> \n";

                        }
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }

                if (!enquiry_branch_id.equals("0") && !dr1.equals("dr_enquiry_campaign_id")) {
                    String str = "";
                    StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                            + " from " + compdb(comp_id) + "axela_emp"
                            + " where emp_sales = 1 and emp_active = '1' and (emp_branch_id = " + enquiry_branch_id + ""
                            + " or emp_id in (select empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                            + " where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + enquiry_branch_id + "))"
                            + " group by emp_id"
                            + " order by emp_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " ><option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("emp_id") + "";
                            str = str + ">" + crs.getString("emp_name") + " </option> \n";
                        }
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }

                if (!enquiry_branch_id.equals("0") && dr1.equals("dr_enquiry_campaign_id")) {
                    String str = "";
                    StrSql = "select campaign_id, campaign_name "
                            + " from " + compdb(comp_id) + "axela_sales_campaign "
                            + " inner join " + compdb(comp_id) + "axela_sales_campaign_branch on campaign_id = camptrans_campaign_id "
                            + " where  camptrans_branch_id = " + enquiry_branch_id
                            + " order by campaign_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " <option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("campaign_id") + "";
                            str = str + ">" + crs.getString("campaign_name") + " </option> \n";
                        }
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }

                if (!enquiry_emp_id.equals("0")) {
                    String str = "";
                    StrSql = "SELECT team_id,team_name,emp_name, emp_id "
                            + " from " + compdb(comp_id) + "axela_sales_team "
                            + " inner join " + compdb(comp_id) + "axela_emp on team_emp_id=emp_id "
                            + " where  1=1  and team_emp_id=" + enquiry_emp_id + ""
                            + " group by team_name order by team_name";
                    try {
                        CachedRowSet crs =processQuery(StrSql, 0);
                        str = str + " ><option value = 0>Select</option>";
                        while (crs.next()) {
                            str = str + "<option value=" + crs.getString("team_id") + "";
                            str = str + ">" + crs.getString("team_name") + " </option> \n";
                        }
                        crs.close();
                        StrHTML = str;
                    } catch (Exception ex) {
                        SOPError("Axelaauto== " + this.getClass().getName());
                        SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                    }
                }
            }
        
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
