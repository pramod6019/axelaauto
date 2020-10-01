package axela.service;
//aJIt

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Callman extends Connect {

    public String StrHTML = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String booking_id = "0";
    public String StrSql = "";
    public String Search = "";
    public String branch_id = "0", BranchAccess = "";
    public String msg = "";
    public String ExeAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
        CheckPerm(comp_id, "emp_service_call_access", request, response);
        HttpSession session = request.getSession(true);
        if(!comp_id.equals("0")){
         emp_id = CNumeric(GetSession("emp_id", request));
        branch_id = CNumeric(GetSession("emp_branch_id", request));
        BranchAccess = GetSession("BranchAccess", request);
        ExeAccess = GetSession("ExeAccess", request);
        booking_id = CNumeric(PadQuotes(request.getParameter("txt_booking_id")));
        GetValues(request, response);
        CheckForm();
            }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Search = PadQuotes(request.getParameter("txt_serch"));
    }

    public void CheckForm() {
        msg = "";
        if (Search.equals("")) {
            StrHTML = "<br><br><br><br><br><font color=\"red\"><b>Enter Search Text!</b></font>";
        }

        if (branch_id.equals("0")) {
            //StrHTML = "<br><br><br><br><br><font color=\"red\"><b>Select Branch!</b></font>";
        }
    }

    public String PopulateExecutive(String branch_id, String search_param) {
        StringBuilder Str = new StringBuilder();
        String StrSearch = "";
        try {
            if (search_param.equals("1")) {//Calls
                StrSearch = " OR empaccess_access_id = '204'";
            } else if (search_param.equals("2")) {//Call Follow-up
                StrSearch = " OR empaccess_access_id = '204'";
            } else if (search_param.equals("3")) {//Appts
                StrSearch = " OR emp_service = '1'";
            } else if (search_param.equals("4")) {//Job Cards      
                StrSearch = " OR emp_service = '1'";
            } else if (search_param.equals("5")) {//Tickets        
                StrSearch = " OR emp_ticket_owner = '1'";
            } else if (search_param.equals("6")) {//Pickup
                StrSearch = " OR emp_pickup_driver = '1'";
            } else if (search_param.equals("7")) {//Courtesy Cars   
                StrSearch = " AND emp_service = '2'";
            }

            Str.append("<option value=\"0\">Service Advisor</option>\n");

            StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp_access ON empaccess_emp_id = emp_id"
                    + " WHERE emp_active = '1'"
                    + " AND (emp_service = '1'" + StrSearch + ")"
                    + " AND (emp_branch_id = " + branch_id + ""
                    + " OR emp_id = 1"
                    + " OR emp_id IN (SELECT empbr.emp_id"
                    + " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
                    + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                    + " AND empbr.emp_branch_id = " + branch_id + "))"
                    + " GROUP BY emp_id"
                    + " ORDER BY emp_name";
            CachedRowSet crs = processQuery(StrSql, 0);

            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id"));
                Str.append(Selectdrop(crs.getInt("emp_id"), emp_id));
                Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public String PopulateSearch() {
        StringBuilder Str = new StringBuilder();
        Str.append("<option value=\"1\">Overdue</option>\n");
        Str.append("<option value=\"2\">By Date</option>\n");
        Str.append("<option value=\"3\" selected=\"selected\">By Vehicle</option>\n");
        Str.append("<option value=\"4\">By Contact</option>\n");
        Str.append("<option value=\"5\">Service Due</option>\n");
        return Str.toString();
    }

    public String PopulateSearchParam() {
        StringBuilder Str = new StringBuilder();
        Str.append("<option value=\"1\">Calls</option>\n");
        Str.append("<option value=\"2\" selected=\"selected\">Call Follow-up</option>\n");
        Str.append("<option value=\"3\">Bookings</option>\n");
        Str.append("<option value=\"6\">Pickup</option>\n");
        Str.append("<option value=\"7\">Courtesy Car</option>\n");
        Str.append("<option value=\"4\">Job Cards</option>\n");
        Str.append("<option value=\"5\">Tickets</option>\n");
        return Str.toString();
    }
}
