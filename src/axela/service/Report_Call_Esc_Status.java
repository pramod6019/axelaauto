package axela.service;

/**
 * @divya 5 APR 2013
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Call_Esc_Status extends Connect {

    public String StrHTML = "";
    public String comp_id = "0";
    public String StrSearch = "";
    public String StrSql = "";
    public String msg = "";
    public String dr_branch_id = "0", branch_id = "";
    public String BranchAccess = "";
    public String ExeAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_call_access", request, response);
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            
            if (branch_id.equals("0")) {
                dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
            } else {
                dr_branch_id = branch_id;
            }

            if ((!dr_branch_id.equals("0"))) {
                StrSearch = " and branch_id = " + dr_branch_id;
                StrHTML = EnquiryTriggerStatus(request);
            }
            StrHTML += getDepartment();
            if (dr_branch_id.equals("0")) {
                msg = msg + "<br>Select Branch!";
            }
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String EnquiryTriggerStatus(HttpServletRequest request) {
        try {
            StringBuilder Str = new StringBuilder();
            Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
            Str.append("<tr align=center>\n");
            Str.append("<th align=center><b>Level 1</b></th>\n");
            Str.append("<th align=center><b>Level 2</b></th>\n");
            Str.append("<th align=center><b>Level 3</b></th>\n");
            Str.append("<th align=center><b>Level 4</b></th>\n");
            Str.append("<th align=center><b>Level 5</b></th>\n");
            Str.append("</tr>");
            Str.append("<tr align=center>\n");
            for (int i = 1; i <= 5; i++) {
                StrSql = " SELECT call_id, concat(emp_name,' (',emp_ref_no,')') as emp_name "
                        + " from " + compdb(comp_id) + "axela_service_call  "
                        + " inner join " + compdb(comp_id) + "axela_emp on emp_id = call_emp_id"
                        + " inner join " + compdb(comp_id) + "axela_branch on branch_id = call_branch_id "
                        + " where 1=1 "
                        + " and call_trigger=" + i + " and call_followup_time !='' and call_call_id=0 and call_followup_time < " + ToLongDate(kknow())
                        + StrSearch + BranchAccess + ExeAccess
                        + " group by call_id order by emp_name";
//                        SOP("StrSql===" + StrSqlBreaker(StrSql));
                CachedRowSet crs2 =processQuery(StrSql, 0);
                if (crs2.isBeforeFirst()) {
                    Str.append("<td valign=top align=left >");
                    while (crs2.next()) {
                        Str.append("<a href=call-list.jsp?call_id=").append(crs2.getString("call_id")).append(">").append(crs2.getString("call_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
                    }
                    Str.append("</td>");
                } else {
                    Str.append("<td valign=top align=center>--<br><br></td>");
                }
                crs2.close();
            }
            Str.append("</tr>");
            Str.append("</table><br>");
//            Str.append(getDepartment());
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
                    + " from " + compdb(comp_id) + "axela_service_call_priority"
                    + " order by prioritycall_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                int count = 0;
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr><th colspan=9>Job Card Priority</th></tr>");
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
}
