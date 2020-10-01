package axela.service;

/**
 * @divya 5 APR 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Call_Esc_Dashboard extends Connect {

    public String StrHTML = "";
    public String comp_id = "0";
    public String StrSql = "";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String go = "";
    public String prioritycall_id = "", exe_id = "";
    public String StrSearch = "";
    public String[] call_prioritycall_id, call_emp_id;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_call_access", request, response);
            go = PadQuotes(request.getParameter("submit_button"));
            GetValues(request, response);

            if (go.equals("Go")) {
                if (!prioritycall_id.equals("")) {
                    StrSearch = StrSearch + " and call_prioritycall_id in (" + prioritycall_id + ")";
                }
                if (!exe_id.equals("")) {
                    StrSearch = StrSearch + " and call_emp_id in (" + exe_id + ")";
                }
            }
            StrHTML = CallEscDashboard();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        prioritycall_id = RetrunSelectArrVal(request, "dr_priority");
        call_prioritycall_id = request.getParameterValues("dr_priority");
        exe_id = RetrunSelectArrVal(request, "dr_emp");
        call_emp_id = request.getParameterValues("dr_emp");
    }

    public String CallEscDashboard() {
        try {
            int count = 0, jobcardcount = 0;
            int level1 = 0, level2 = 0, level3 = 0, level4 = 0, level5 = 0;
            StringBuilder Str = new StringBuilder();
            StrSql = "select branch_id, branch_name, "
                    + " sum(if(call_trigger=1,1,0)) as level1, "
                    + " sum(if(call_trigger=2,1,0)) as level2, "
                    + " sum(if(call_trigger=3,1,0)) as level3, "
                    + " sum(if(call_trigger=4,1,0)) as level4, "
                    + " sum(if(call_trigger=5,1,0)) as level5, "
                    + " count(call_id) as callcount "
                    + " from " + compdb(comp_id) + "axela_branch "
                    + " left join " + compdb(comp_id) + "axela_service_call on call_branch_id = branch_id "
                    + " and call_trigger > 0 and call_followup_time='' and call_followup_time < " + ToLongDate(kknow())
                    + ExeAccess.replace("emp_id", "call_emp_id") + BranchAccess.replace("branch_id", "call_branch_id")
                    + " where 1=1 " + StrSearch
                    + " group by branch_id ";
//            SOP("StrSql===" + StrSqlBreaker(StrSql));
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
            Str.append("<th>Call Count</th>\n");
            Str.append("</tr>");
            while (crs.next()) {
                count++;
                jobcardcount = jobcardcount + crs.getInt("callcount");
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
                Str.append("<td align=center>").append(crs.getString("callcount")).append("</td>\n");
                Str.append("</tr>");
            }
            Str.append("<tr align=center>\n");
            Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
            Str.append("<td align=center><b>").append(level1).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level2).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level3).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level4).append("</b></td>\n");
            Str.append("<td align=center><b>").append(level5).append("</b></td>\n");
            Str.append("<td align=center><b>").append(jobcardcount).append("</b></td>\n");
            Str.append("</tr>");
            Str.append("</table>");
            Str.append(getPriority());
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String getPriority() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "Select * "
                    + " from " + compdb(comp_id) + "axela_service_call_priority "
                    + " order by prioritycall_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                int count = 0;
                Str.append("<br><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr><th colspan=9>Priority</th></tr>");
                Str.append("<tr align=center>\n");
                Str.append("<td align=center><b>#</b></td>\n");
                Str.append("<td align=center><b>Priority</b></td>\n");
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
                    Str.append("<td align=left>").append(crs.getString("prioritycall_name")).append("</td>\n");
                    Str.append("<td align=left>").append(crs.getString("prioritycall_desc")).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_duehrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_trigger1_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_trigger2_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_trigger3_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_trigger4_hrs"))).append("</td>\n");
                    Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("prioritycall_trigger5_hrs"))).append("</td>\n");
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

    public String PopulatePriority() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "select prioritycall_id, prioritycall_name "
                    + " from " + compdb(comp_id) + "axela_service_call_priority "
                    + " where 1 = 1 "
                    + " group by prioritycall_id"
                    + " order by prioritycall_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("prioritycall_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("prioritycall_id"), call_prioritycall_id));
                Str.append(">").append(crs.getString("prioritycall_name")).append("</option> \n");
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
            StrSql = "Select concat(emp_name, ' (',emp_ref_no,')') as emp_name, emp_id "
                    + " from " + compdb(comp_id) + "axela_emp "
                    + " left join " + compdb(comp_id) + "axela_service_call on call_emp_id = emp_id "
                    + " and call_followup_time='' and call_followup_time < " + ToLongDate(kknow())
                    + " where 1=1 and emp_active = '1' "
                    + " group by emp_id "
                    + " order by emp_name";
            CachedRowSet crs = processQuery(StrSql, 0);
//            SOP("StrSql-all-"+StrSql);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), call_emp_id));
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
