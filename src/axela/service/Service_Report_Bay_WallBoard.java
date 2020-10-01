package axela.service;

//Gurumurthy TS 6 JAN 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Report_Bay_WallBoard extends Connect {

    public String StrSql = "";
    public static String msg = "";
    public String emp_id = "", branch_id = "", bay_branch_id = "";
    public String StrHTML = "";
    public String BranchAccess = "";
    public String comp_id = "0";
    public String ExeAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));

            CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            GetValues(request, response);
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
//        bay_branch_id = CNumeric(PadQuotes(request.getParameter("dr_bay_branch_id")));
        if (branch_id.equals("0")) {
            bay_branch_id = CNumeric(PadQuotes(request.getParameter("dr_bay_branch_id")));
        } else {
            bay_branch_id = branch_id;
        }

        if (bay_branch_id.equals("0")) {
            StrHTML = "<br><br><br><font color=\"red\"><b>Select Branch!</b></font><br><br><br>";
        } else {
            StrSql = "Select branch_name from " + compdb(comp_id) + "axela_branch"
                    + " where branch_id = " + bay_branch_id + "";
            StrHTML = ListBay();
        }
    }

    public String ListBay() {
        StringBuilder Str = new StringBuilder();
        int i = 0;
        try {
            StrSql = "SELECT bay_id, bay_name,"
                    + " COALESCE(jc_id,'') AS jc_id,"
                    + " COALESCE(jc_reg_no, '') AS jc_reg_no,"
                    + " COALESCE(item_name, '') AS item_name, COALESCE(emp_name, '') AS emp_name"
                    + " FROM " + compdb(comp_id) + "axela_service_jc_bay"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_bay_id = bay_id AND jc_time_ready = ''"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_technician_emp_id"
                    + " WHERE bay_branch_id = " + bay_branch_id + " AND bay_active = 1"
                    + " GROUP BY bay_id"
                    + " ORDER BY bay_rank";
//            SOP("StrSql==" + StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                Str.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"5\" width=\"100%\">");
                Str.append("<tr><td colspan=5>&nbsp;</td></tr>");
                while (crs.next()) {
                    if (i == 0) {
                        Str.append("<tr>");
                    }
                    i++;

                    Str.append("<td align=\"center\" width=\"20%\">");
                    Str.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"200px\" align=\"center\">");
                    Str.append("<tr><td height=\"200px\" align=\"center\"");
                    if (!crs.getString("jc_id").equals("")) {
                        Str.append(" bgcolor=\"#B0DE09\"");
                    } else {
                        Str.append(" bgcolor=\"#FF6600\"");  
                    }
                    Str.append("><font size=2><b>");
                    Str.append("").append(crs.getString("bay_name")).append("");
                    if (crs.getString("jc_id").equals("")) {
                        Str.append("<br>FREE").append("");
                    } else {
//                        Str.append("<br>Job Card ID: ").append(crs.getString("jc_id"));
                        Str.append("<br>").append("<a href=\"jobcard-list.jsp?jc_id=").append(crs.getString("jc_id")).append("\">").append(crs.getString("jc_reg_no")).append("</a>");
                        Str.append("<br>").append(crs.getString("item_name"));
                        Str.append("<br>").append(crs.getString("emp_name"));
                    }

                    Str.append("</b></font></td></tr></table>");
                    Str.append("</td>");
                    if (i >= 5) {
                        Str.append("<tr><td colspan=5>&nbsp;</td></tr>");
                        i = 0;
                        Str.append("</tr>\n");
                    }
                }
                if (i < 5) {
                    for (int j = i; j < 5; j++) {
                        Str.append("<td>&nbsp;</td>");
                    }
                }
                Str.append("<tr><td colspan=5>&nbsp;</td></tr>");
                Str.append("</table>");
            } else {
                Str.append("<br><br><br><font color=\"red\"><b>No active Bays found!</b></font><br><br><br>");
            }

            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }
}
