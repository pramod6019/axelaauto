//sangita
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_ManHours extends Connect {

    public String StrHTML = "";
    public String StrSearch = "";
    public String StrSql = "";
    public String branch_id = "0", dr_branch_id = "0";
    public String BranchAccess = "";
    public String comp_id = "0";
    public String ExeAccess = "";
    public String go = "";
    public String starttime = "", start_time = "";
    public String endtime = "", end_time = "";
    public String msg = "";
    public String[] technicianexe_ids;
    public String technicianexe_id = "";
    public Report_Check reportexe = new Report_Check();

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            go = PadQuotes(request.getParameter("submit_button"));
            GetValues(request, response);
            CheckForm();
            if (go.equals("Go")) {
                StrSearch = BranchAccess.replace("branch_id", "emp_branch_id") + ExeAccess + "";

                if (!dr_branch_id.equals("0")) {
                    StrSearch += " AND emp_branch_id = " + dr_branch_id;
                }
//                SOP("technicianexe_id = " + technicianexe_id);
                if (!technicianexe_id.equals("")) {
                    StrSearch = StrSearch + " and emp_id in (" + technicianexe_id + ")";
                }
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                }

                if (msg.equals("")) {
                    StrHTML = ListJCManHours();
                }
            }
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        if (branch_id.equals("0")) {
            dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
        } else {
            dr_branch_id = branch_id;
        }
        starttime = PadQuotes(request.getParameter("txt_starttime"));
        endtime = PadQuotes(request.getParameter("txt_endtime"));
        if (starttime.equals("")) {
            starttime = ReportStartdate();
        }

        if (endtime.equals("")) {
            endtime = strToShortDate(ToShortDate(kknow()));
        }
        technicianexe_id = RetrunSelectArrVal(request, "dr_technician");
        technicianexe_ids = request.getParameterValues("dr_technician");
    }

    public String ListJCManHours() {
        try {
            int count = 0;
            StringBuilder Str = new StringBuilder();
            int manhours = 0;
            int grandtotal = 0;

            StrSql = " SELECT emp_id,emp_branch_id,"
                    + " CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name,"
                    + " COALESCE((SELECT SUM(TIME_TO_SEC(timediff(baytrans_end_time,baytrans_start_time))/60)"
                    + " FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
                    + " WHERE baytrans_emp_id = emp_id"
                    + " AND baytrans_end_time != ''"
                    + " AND SUBSTR(baytrans_start_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
                    + " AND SUBSTR(baytrans_end_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)),'0') as manhours"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_emp_id = emp_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay ON bay_id = baytrans_bay_id"
                    + " WHERE emp_technician = '1' AND emp_active='1'" + StrSearch + ""
                    + " GROUP BY emp_name, emp_id"
                    + " ORDER BY emp_name";
//            SOP("StrSql = " + StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);

            if (crs.isBeforeFirst()) {
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
                Str.append("<tr align=\"center\">\n");
                Str.append("<th align=\"center\">#</th>\n");
                Str.append("<th align=\"center\">Technician</th>\n");
                Str.append("<th width=20% align=\"right\">Man Hours</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    manhours = crs.getInt("manhours");
                    grandtotal = grandtotal + manhours;
                    count++;
                    Str.append("<tr align=\"center\" valign=\"top\">\n");
                    Str.append("<td valign=top align=center>").append(count).append("</td>");
                    Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>");
                    Str.append("<td align=\"right\">").append(ConvertMintoHrsMins(manhours)).append("</td>");
                    Str.append("</tr>\n");
                }
                Str.append("<tr>\n<td colspan=\"2\" align=\"right\"><b>Total:</b></td>\n");
                Str.append("<td valign=\"top\" align=\"right\"><b>").append(ConvertMintoHrsMins(grandtotal)).append("</b></td>\n");
                Str.append("</tr>\n");
                Str.append("</table>\n");
            } else {
                Str.append("<font color=\"red\"><b>No Man Hours found!</b></font>");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String PopulateTechnician(String dr_branch_id) {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE emp_technician = '1' and emp_active='1'"
                    + " and (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
                    + " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                    + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                    + " and empbr.emp_branch_id = " + dr_branch_id + "))"
                    + " " + ExeAccess + ""
                    + " group by emp_id order by emp_name";
//            SOP("StrSql=tt=" + StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<select name=dr_technician id=dr_technician class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), technicianexe_ids));
                Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
            }
            Str.append("</select>");
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    protected void CheckForm() {
        msg = "";
        if (dr_branch_id.equals("0")) {
            msg = msg + "<br>Select Branch!";
        }

        if (starttime.equals("")) {
            msg += "<br>Select Start Date!";
        } else {
            if (isValidDateFormatShort(starttime)) {
                starttime = ConvertShortDateToStr(starttime);
                start_time = strToShortDate(starttime);
            } else {
                msg += "<br>Enter Valid Start Date!";
                starttime = "";
            }
        }

        if (endtime.equals("")) {
            msg += "<br>Select End Date!<br>";
        } else {
            if (isValidDateFormatShort(endtime)) {
                endtime = ConvertShortDateToStr(endtime);
                if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
                    msg += "<br>Start Date should be less than End date!";
                }
                end_time = strToShortDate(endtime);
            } else {
                msg += "<br>Enter Valid End Date!";
                endtime = "";
            }
        }

    }
}
