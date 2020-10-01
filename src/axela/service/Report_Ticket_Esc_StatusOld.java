package axela.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Esc_StatusOld extends Connect {

    public String StrHTML = "";
    public String StrSearch = "";
    public String StrSql = "";
    public String comp_id = "0";
    public String dr_branch_id = "0", branch_id = "";
//    public String BranchAccess = "";
    public String ExeAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
//            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_ticket_access", request, response);
            branch_id = CNumeric(GetSession("emp_branch_id", request));

            if (branch_id.equals("0")) {
                dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
            } else {
                dr_branch_id = branch_id;
            }
//            dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));

            if ((!dr_branch_id.equals("0"))) {
                StrSearch = " and branch_id = " + dr_branch_id;
                StrHTML = EnquiryTriggerStatus();
            }
            if (dr_branch_id.equals("0")) {
                StrHTML = EnquiryTriggerStatus();
            }
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String EnquiryTriggerStatus() {
        try {
            StringBuilder Str = new StringBuilder();
            StrSql = "select * from ((select 0 as branch_id, 'Head Office' as branch_name) "
                    + " UNION "
                    + " (select branch_id, concat(branch_name,' (',branch_code,')') as branch_name "
                    + " from " + compdb(comp_id) + "axela_branch  "
                    + " where branch_active='1' "
                    + " order by branch_name)) as branch"
                    + " where branch_id = " + dr_branch_id;
//            SOP("StrSql=="+StrSql);
            CachedRowSet crs1 = processQuery(StrSql, 0);
            if (crs1.isBeforeFirst()) {
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                while (crs1.next()) {
                    Str.append("<tr align=center><th colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
                    Str.append("<tr align=center>\n");
                    Str.append("<td align=center><b>Level 1</b></td>\n");
                    Str.append("<td align=center><b>Level 2</b></td>\n");
                    Str.append("<td align=center><b>Level 3</b></td>\n");
                    Str.append("<td align=center><b>Level 4</b></td>\n");
                    Str.append("<td align=center><b>Level 5</b></td>\n");
                    Str.append("</tr>");
                    Str.append("<tr align=center>\n");
                    for (int i = 1; i <= 5; i++) {
                        StrSql = " SELECT ticket_id, concat(emp_name,' (',emp_ref_no,')') as emp_name "
                                + " from " + compdb(comp_id) + "axela_service_ticket  "
                                + " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id"
                                + " inner join (( "
                                + " select 0 as customer_id, 0 as customer_branch_id)  UNION  ("
                                + " select customer_id, customer_branch_id  "
                                + " from " + compdb(comp_id) + "axela_customer)) as customer on customer_id = ticket_customer_id and customer_branch_id = " + dr_branch_id
                                + " inner join (("
                                + " select 0 as branch_id, 'Head Office' as branch_name)  UNION  ("
                                + " select branch_id, concat(branch_name,' (',branch_code,')') as branch_name "
                                + " from " + compdb(comp_id) + "axela_branch"
                                + " where branch_active='1' "+ StrSearch
//                                + BranchAccess
                                + " order by branch_name)) as branch on branch_id = customer_branch_id "
                                + " where 1=1 "
                                + " and ticket_trigger=" + i + " and ticket_ticketstatus_id!=3"
                                  + ExeAccess
                                + " group by ticket_id order by emp_name";
//                        SOP("StrSql==="+StrSqlBreaker(StrSql)); 
                        CachedRowSet crs2 =processQuery(StrSql, 0);
                        if (crs2.isBeforeFirst()) {
                            Str.append("<td valign=top align=left >");
                            while (crs2.next()) {
                                Str.append("<a href=ticket-list.jsp?ticket_id=").append(crs2.getString("ticket_id")).append(">").append(crs2.getString("ticket_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
                            }
                            Str.append("</td>");
                        } else {
                            Str.append("<td valign=top align=center>--<br><br></td>");
                        }
                        crs2.close();
                    }
                    Str.append("</tr>");
                }
                Str.append("</table>");
                Str.append(getDepartment());
            } else {
                Str.append("<font color=red><b>No branches found!</b></font>");
            }
            crs1.close();
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
}
