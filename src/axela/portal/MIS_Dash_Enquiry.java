package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Dash_Enquiry extends Connect {

    public String submitB = "";
    public static String msg = "";
    public static String StrSql = "";
    public String starttime = "", start_time = "";
    public String endtime = "", end_time = "";
    public String emp_id = "", branch_id = "";
    public String comp_id = "";
    public String[] group_ids, exe_ids, course_ids;
    public String group_id = "", exe_id = "", course_id = "";
    public String StrHTML = "", StrClosedHTML = "";
    public String BranchAccess = "", dr_branch_id = "";
    public String EnquirySearch = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
            CheckPerm(comp_id, "emp_mis_access", request, response);
            HttpSession session = request.getSession(true);
           if(!comp_id.equals("0"))
            {
            	  emp_id = CNumeric(GetSession("emp_id", request));
                  branch_id = CNumeric(GetSession("emp_branch_id", request));
                  BranchAccess = GetSession("BranchAccess", request);

                  GetValues(request, response);
                  CheckForm();
                  EnquirySearch = BranchAccess + "";

                  if (!starttime.equals("")) {
                      EnquirySearch = EnquirySearch + " and student_enquiry_date >= '" + starttime + "'";
                  }
                  if (!endtime.equals("")) {
                      EnquirySearch = EnquirySearch + " and student_enquiry_date <= '" + endtime + "'";
                  }
                  if (!exe_id.equals("")) {
                      EnquirySearch = EnquirySearch + " and student_counsel_emp_id in (" + exe_id + ")";
                  }
                  if (!dr_branch_id.equals("0")) {
                      EnquirySearch = EnquirySearch + " and student_branch_id =" + dr_branch_id;
                  }
                  if (!course_id.equals("")) {
                      EnquirySearch = EnquirySearch + " and (student_id in (select invoice_student_id from " + compdb(comp_id) + "axela_invoice "
                              + " inner join " + compdb(comp_id) + "axela_invoice_course on invcourse_invoice_id = invoice_id "
                              + " and invcourse_course_id in (" + course_id + ")))";
                  }
                  if (!ExecuteQuery("select emp_pid from " + compdb(comp_id) + "axela_emp where  emp_id=" + emp_id + "").equals("1")) {
                      EnquirySearch = EnquirySearch + " and (student_counsel_emp_id = " + emp_id
                              + " or student_counsel_emp_id in (select ex.empexe_id "
                              + " from " + compdb(comp_id) + "axela_emp_exe as ex where ex.empexe_emp_id=" + emp_id + "))";
                  }
                  if (!msg.equals("")) {
                      msg = "Error!" + msg;
                  }
                  if (msg.equals("")) {
                      StrHTML = EnquirySummary();
                  }

            }
          
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        starttime = PadQuotes(request.getParameter("txt_starttime"));
        endtime = PadQuotes(request.getParameter("txt_endtime"));
        if (starttime.equals("")) {
            starttime = ReportStartdate();
        }
        if (endtime.equals("")) {
            endtime = strToShortDate(ToShortDate(kknow()));
        }
        if (branch_id.equals("0")) {
            dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
            if (dr_branch_id.equals("")) {
                dr_branch_id = "0";
            }
        } else {
            dr_branch_id = branch_id;
        }
        exe_id = RetrunSelectArrVal(request, "dr_executive");
        exe_ids = request.getParameterValues("dr_executive");

        course_id = RetrunSelectArrVal(request, "dr_course");
        course_ids = request.getParameterValues("dr_course");
    }

    protected void CheckForm() {
        msg = "";
        if (starttime.equals("")) {
            msg = msg + "<br>Select Start Date!";
        }
        if (!starttime.equals("")) {
            if (isValidDateFormatShort(starttime)) {
                starttime = ConvertShortDateToStr(starttime);
                start_time = strToShortDate(starttime);
            } else {
                msg = msg + "<br>Enter Valid Start Date!";
                starttime = "";
            }
        }
        if (endtime.equals("")) {
            msg = msg + "<br>Select End Date!";
        }
        if (!endtime.equals("")) {
            if (isValidDateFormatShort(endtime)) {
                endtime = ConvertShortDateToStr(endtime);
                if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
                    msg = msg + "<br>Start Date should be less than End date!";
                }
                end_time = strToShortDate(endtime);
                endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
            } else {
                msg = msg + "<br>Enter Valid End Date!";
                endtime = "";
            }
        }

    }

    public String EnquirySummary() {
        String StrSql = "";
        int opencount = 0;
        int closedwon = 0;
        int closedlost = 0;
        int total = 0;
        int invoice = 0;
        int receipt = 0;
        StringBuilder Str = new StringBuilder();
        try {
//            StrSql = " SELECT soe_name, "
//                    + " (select count(student_id) from " + compdb(comp_id) + "axela_student where student_soe_id = soe_id and student_enqfollowup_active='1') as opencount,"
//                    + " (select count(student_id) from " + compdb(comp_id) + "axela_student where student_no!='' and student_soe_id = soe_id) as closedwon,"
//                    + " (select count(student_id) from " + compdb(comp_id) + "axela_student where student_soe_id = soe_id and student_enqfollowup_active='0') as closedlost, "
//                    + " count(student_id) as total"
//                    + " from " + compdb(comp_id) + "axela_soe "
//                    + " inner join " + compdb(comp_id) + "axela_student on student_soe_id = soe_id "
//                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id = student_branch_id "
//                    + " left  join " + compdb(comp_id) + "axela_invoice on invoice_student_id = student_id "
//                    + " left  join " + compdb(comp_id) + "axela_invoice_course on invcourse_invoice_id = invoice_id "
//                    + " where soe_comp_id = "+comp_id +EnquirySearch
//                    + " group by soe_id order by soe_name";
            StrSql = " SELECT soe_name,"
                    + "  (select count(student_id) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  where student_soe_id = soe_id and student_active='1') as total, "
                    + "  ( "
                    + "  select count(student_id) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  where student_soe_id = soe_id and student_no='' and student_active='1' and student_enqfollowup_active='0') as closedlost, "
                    + "  (select count(student_id) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  where student_soe_id = soe_id and student_enqfollowup_active='1' and student_no='' and student_active='1') as opencount, ( "
                    + "  select count(student_id) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  where student_no!='' and student_soe_id = soe_id and student_active='1') as closedwon,"
                    + "  coalesce((select sum(invoice_grandtotal) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  inner join " + compdb(comp_id) + "axela_invoice on invoice_student_id = student_id "
                    + "  where student_no!='' and student_soe_id = soe_id and student_active='1' and invoice_active='1'),0) as invoicevalue, "
                    + "  coalesce((select sum(receipt_total) "
                    + "  from " + compdb(comp_id) + "axela_student "
                    + "  inner join " + compdb(comp_id) + "axela_invoice on invoice_student_id = student_id "
                    + "  inner join " + compdb(comp_id) + "axela_invoice_receipt on receipt_invoice_id = invoice_id "
                    + "  where student_no!='' and student_soe_id = soe_id and student_active='1' and invoice_active='1' and receipt_active=1),0) as receiptvalue "
                    + "  from " + compdb(comp_id) + "axela_soe "
                    + "  inner join " + compdb(comp_id) + "axela_student on student_soe_id = soe_id "
                    + "  inner join " + compdb(comp_id) + "axela_branch on branch_id = student_branch_id "
                    + "  where soe_comp_id = " + comp_id + EnquirySearch
                    + "  group by soe_id "
                    + "  order by total, soe_name ";

//            SOP("strsql in LmsSummary---" + StrSqlBreaker(StrSql));
            CachedRowSet crs =processQuery(StrSql, 0);
            Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
            Str.append("<tr align=center>\n");
            Str.append("<th>Source</th>\n");
            Str.append("<th>Total</th>\n");
            Str.append("<th>Closed Lost</th>\n");
            Str.append("<th>Open</th>\n");
            Str.append("<th>Closed Won</th>\n");
            Str.append("<th>Invoice Amount</th>\n");
            Str.append("<th>Receipt Amount</th>\n");
            Str.append("</tr>\n");
            while (crs.next()) {
                total = total + crs.getInt("total");
                closedwon = closedwon + crs.getInt("closedwon");
                opencount = opencount + crs.getInt("opencount");
                closedlost = closedlost + crs.getInt("closedlost");
                invoice = invoice + crs.getInt("invoicevalue");
                receipt = receipt + crs.getInt("receiptvalue");

                Str.append("<tr>\n");
                Str.append("<td valign=top align=left>" + crs.getString("soe_name") + "</td>\n");
                Str.append("<td valign=top align=right>" + crs.getString("total") + "</td>");
                Str.append("<td valign=top align=right>" + crs.getString("closedlost") + "</td>");
                Str.append("<td valign=top align=right>" + crs.getString("opencount") + "</td>");
                Str.append("<td valign=top align=right>" + crs.getString("closedwon") + "</td>");
                Str.append("<td valign=top align=right>" + IndFormat(crs.getString("invoicevalue")) + "</td>");
                Str.append("<td valign=top align=right>" + IndFormat(crs.getString("receiptvalue")) + "</td>");
            }
            crs.close();
            Str.append("<tr>\n");
            Str.append("<td align=right><b>Total:</b></td>\n");
            Str.append("<td align=right><b>" + total + "</b></td>\n");
            Str.append("<td align=right><b>" + closedlost + "</b></td>\n");
            Str.append("<td align=right><b>" + opencount + "</b></td>\n");
            Str.append("<td align=right><b>" + closedwon + "</b></td>\n");
            Str.append("<td align=right><b>" + IndFormat(invoice + "") + "</b></td>\n");
            Str.append("<td align=right><b>" + IndFormat(receipt + "") + "</b></td>\n");
            Str.append("</tr>");
            Str.append("</table>");
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String PopulateExecutive() {
        try {
            String exe = "";
            StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                    + " from " + compdb(comp_id) + "axela_emp "
                    + " where 1=1 and  emp_active='1' ";//and emp_role_id=4 " +
            if (!dr_branch_id.equals("0")) {
                StrSql = StrSql + " and (emp_branch_id=0 or emp_branch_id=" + dr_branch_id + ")";
            }
            StrSql = StrSql + " group by emp_id order by emp_name";
//                SOP("SqlStr==== in PopulateExecutive" + StrSqlBreaker(SqlStr));
            CachedRowSet crs =processQuery(StrSql, 0);
            exe = "<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">";

            while (crs.next()) {
                exe = exe + "<option value=" + crs.getString("emp_id") + "";
                exe = exe + ArrSelectdrop(crs.getInt("emp_id"), exe_ids);
                exe = exe + ">" + (crs.getString("emp_name")) + "</option> \n";
            }
            exe = exe + "</select>";
            crs.close();
            return exe;
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String PopulateCourse() {
        String stringval = "";
        try {
            StrSql = "select course_id, course_name from " + compdb(comp_id) + "axela_course "
                    + " where  course_active=1 and course_cat_id=1"
                    + " group by course_id order by course_name";
            CachedRowSet crs =processQuery(StrSql, 0);
//             SOP("StrSql in PopulateCountry==========" + StrSql);
            while (crs.next()) {
                stringval = stringval + "<option value=" + crs.getString("course_id") + "";
                stringval = stringval + ArrSelectdrop(crs.getInt("course_id"), course_ids);
                stringval = stringval + ">" + crs.getString("course_name") + "</option>\n";
            }

            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return stringval;
    }

    public String RetrunSelectArrVal(HttpServletRequest request, String name) {
        String arr = "";
        String[] strArr = request.getParameterValues(name);
        try {
            if (strArr != null && strArr.length > 0) {
                for (int i = 0; i < strArr.length; i++) {
                    arr = arr + strArr[i] + ",";
                }
                arr = arr.substring(0, arr.length() - 1);
            }
            return arr;
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
