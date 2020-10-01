package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Esc_Dashboard extends Connect {

    public String StrHTML = "";
    public String StrSql = "";
//    public String BranchAccess = "";
    public String ExeAccess = "";
    public String go = "";
    public String comp_id = "0";
    public String ticket_dept_id = "", exe_id = "";
    public String StrSearch = "";
    public String[] ticket_ticket_dept_id, ticket_emp_id;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
//            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_ticket_access", request, response);
            go = PadQuotes(request.getParameter("submit_button"));
            GetValues(request, response);

            if (go.equals("Go")) {
                if (!ticket_dept_id.equals("")) {
                    StrSearch = StrSearch + " and ticket_ticket_dept_id in (" + ticket_dept_id + ")";
                }
                if (!exe_id.equals("")) {
                    StrSearch = StrSearch + " and ticket_emp_id in (" + exe_id + ")";
                }
            }
            StrHTML = TicketEscDashboard();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        ticket_dept_id = RetrunSelectArrVal(request, "dr_ticket_dept");
        ticket_ticket_dept_id = request.getParameterValues("dr_ticket_dept");
        exe_id = RetrunSelectArrVal(request, "dr_emp");
        ticket_emp_id = request.getParameterValues("dr_emp");
    }

    public String TicketEscDashboard() {
        try {
            int count = 0, ticketcount = 0;
            int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
            StringBuilder Str = new StringBuilder();
            StrSql = "select branch_id, branch_name,"
                    + " sum(if(ticket_trigger=1,1,0)) as level1,"
                    + " sum(if(ticket_trigger=2,1,0)) as level2,"
                    + " sum(if(ticket_trigger=3,1,0)) as level3,"
                    + " sum(if(ticket_trigger=4,1,0)) as level4,"
                    + " sum(if(ticket_trigger=5,1,0)) as level5,"
                    + " count(ticket_id) as ticketcount "
                    + " from ((select 0 as branch_id, 'Head Office' as branch_name)"
                    + " UNION"
                    + " (select branch_id, concat(branch_name,' (',branch_code,')') as branch_name"
                    + " from " + compdb(comp_id) + "axela_branch "
                    + " where branch_active='1' " 
//                    + BranchAccess
                    + " order by branch_name)) as branch"
                    + " left join ((select 0 as customer_id, 0 as customer_branch_id)"
                    + " UNION"
                    + " (select customer_id, customer_branch_id"
                    + " from " + compdb(comp_id) + "axela_customer)) as customer on customer_branch_id = branch_id"
                    + " left join " + compdb(comp_id) + "axela_service_ticket on ticket_customer_id = customer_id"
                    + " and ticket_trigger > 0 and ticket_ticketstatus_id!=3"
                    + ExeAccess.replace("emp_id", "ticket_emp_id")
                    + " where 1=1 " + StrSearch
                    + " group by branch_id ";
//            SOP("StrSql==="+StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
            Str.append("<tr align=center>\n");
            Str.append("<th>#</th>\n");
            Str.append("<th>Branch</th>\n");
            Str.append("<th>Level 1</th>\n");
            Str.append("<th>Level 2</th>\n");
            Str.append("<th>Level 3</th>\n");
            Str.append("<th>Level 4</th>\n");
            Str.append("<th>Level 5</th>\n");
            Str.append("<th>Ticket Count</th>\n");
            Str.append("</tr>");
            while (crs.next()) {
                count++;
                ticketcount = ticketcount + crs.getInt("ticketcount");
                level1 = level1 + crs.getInt("level1");
                level2 = level2 + crs.getInt("level2");
                level3 = level3 + crs.getInt("level3");
                level4 = level4 + crs.getInt("level4");
                level5 = level5 + crs.getInt("level5");
                Str.append("<tr align=center>\n");
                Str.append("<td align=center>").append(count).append(".</b></td>\n");
                Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("level1")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("level2")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("level3")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("level4")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("level5")).append("</td>\n");
                Str.append("<td align=center>").append(crs.getString("ticketcount")).append("</td>\n");
                Str.append("</tr>");
            }
            Str.append("<tr align=center>\n");
            Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
            Str.append("<td align=center><b>").append(level1).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level2).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level3).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level4).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level5).append("</b></td>\n");
            Str.append("<td align=center><b>").append(ticketcount).append("</b></td>\n");
            Str.append("</tr>");
            Str.append("</table>");
            Str.append(getDepartment());
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String getDepartment() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "Select * "
                    + " from " + compdb(comp_id) + "axela_service_ticket_dept "
                    + " order by ticket_dept_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                int count = 0;
                Str.append("<br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr><th colspan=9>Department Priority</th></tr>");
                Str.append("<tr align=center>\n");
                Str.append("<td align=center><b>#</b></td>\n");
                Str.append("<td align=center><b>Department</b></td>\n");
                Str.append("<td align=center><b>Description</b></td>\n");
                Str.append("<td align=center><b>Due Hours</b></td>\n");
                Str.append("<td align=center><b>Level 1</b></td>\n");
                Str.append("<td align=center><b>Level 2</b></td>\n");
                Str.append("<td align=center><b>Level 3</b></td>\n");
                Str.append("<td align=center><b>Level 4</b></td>\n");
                Str.append("<td align=center><b>Level 5</b></td>\n");
                Str.append("</tr>");
                while (crs.next()) {
                    count++;
                    Str.append("<tr align=center>\n");
                    Str.append("<td align=center>").append(count).append("</td>\n");
                    Str.append("<td align=left>").append(crs.getString("ticket_dept_name")).append("</td>\n");
                    Str.append("<td align=left>").append(crs.getString("ticket_dept_desc")).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_duehrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger1_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger2_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger3_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger4_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger5_hrs"))).append("</td>\n");
                    Str.append("</tr>");
                }
                Str.append("</table>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }

    public String PopulateDepartment() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "select ticket_dept_id, ticket_dept_name "
                    + " from " + compdb(comp_id) + "axela_service_ticket_dept "
                    + " where 1 = 1 "
                    + " group by ticket_dept_id"
                    + " order by ticket_dept_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("ticket_dept_id"), ticket_ticket_dept_id));
                Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }

    public String PopulateAllExecutive() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "select concat(emp_name, ' (',emp_ref_no,')') as emp_name, " + compdb(comp_id) + "axela_emp.emp_id as emp_id "
                    + " from " + compdb(comp_id) + "axela_emp "
                    + " left join " + compdb(comp_id) + "axela_service_ticket on ticket_emp_id = " + compdb(comp_id) + "axela_emp.emp_id "
                    + " and ticket_ticketstatus_id!=3 "
                    + " where emp_active = '1' and emp_ticket_owner = '1'"
                    + " group by emp_id "
                    + " order by emp_name";
            CachedRowSet crs = processQuery(StrSql, 0);
//            SOP("StrSql-all-"+StrSql);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), ticket_emp_id));
                Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }
}
