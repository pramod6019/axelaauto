package axela.portal;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email_Report_Details extends Connect {

    public String StrHTML1 = "";
    public String StrSearch = "";
    public String comp_id = "0";
    public String StrSql = "";
    public String BranchAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(true);
            CheckSession(request, response);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_enquiry_access", request, response);
            StrSearch = PadQuotes(GetSession("EnquiryStrSearch", request) + "");
            BranchAccess = GetSession("BranchAccess", request);
            StrHTML1 = EnquiryDetails(request);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String EnquiryDetails(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
        DecimalFormat fmt1 = new DecimalFormat("#.##");
        int count = 0;
        try {
            StrHTML1 = "";
            StrHTML1 = StrHTML1 + "<b>Email Details</b><br>";
            StrHTML1 = StrHTML1 + "<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">";
            StrHTML1 = StrHTML1 + "<tr align=center>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Sl. No. </font></b></td>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Date </font></b></td>\n";
            if (CNumeric(GetSession("emp_branch_id", request)).equals("0")) {
                StrHTML1 = StrHTML1 + "<td align=center><b><font size=2>Branch</font></b></td>\n";
            }
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Student Name </font></b></td>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> To Address </font></b></td>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Subject</font></b></td>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Status</font></b></td>\n";
            StrHTML1 = StrHTML1 + "<td align=center><b><font size=2> Sent By</font></b></td>\n";
            StrHTML1 = StrHTML1 + "</tr>";
            StrSql = " SELECT concat(substring(email_date,1,8),'000000') as emaildate, student_course_total,"
                    + " concat(branch_name,' (', branch_code, ')') as branchname, "
                    + " student_id, student_name, student_no, concat(branch_code, student_no) as studentno "
                    + " from " + compdb(comp_id) + "axela_email "
                    + " inner join " + compdb(comp_id) + "axela_student on student_id = email_student_id "
                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id= student_branch_id "
                    + " where  branch_active='1' " + StrSearch + BranchAccess + " ";
            StrSql = StrSql + " order by emaildate, branchname, student_name";
            CachedRowSet crs1 =processQuery(StrSql, 0);

            if (crs1.isBeforeFirst()) {
                while (crs1.next()) {
                    count++;
                    StrHTML1 = StrHTML1 + "<tr>\n";
                    StrHTML1 = StrHTML1 + "<td align= center valign=top>" + count + ".</td>";
                    StrHTML1 = StrHTML1 + "<td valign=top align=center >" + strToShortDate(crs1.getString("emaildate")) + "</td>\n";
                    if (CNumeric(GetSession("emp_branch_id", request)).equals("0")) {
                        StrHTML1 = StrHTML1 + "<td  valign=top align=left>" + crs1.getString("branchname") + "</td>";
                    }
                    StrHTML1 = StrHTML1 + "<td  valign=top align=left><a href=../stud/student-pop.jsp?student_id=" + crs1.getString("student_id") + " target=_blank>" + crs1.getString("student_name") + " (" + crs1.getString("student_id") + ")</a></td>";
                    StrHTML1 = StrHTML1 + "<td  valign=top align=center>";
                    if (!crs1.getString("student_no").equals("")) {
                        StrHTML1 = StrHTML1 + crs1.getString("studentno");
                    }
                    StrHTML1 = StrHTML1 + "&nbsp;</td>";
                    StrHTML1 = StrHTML1 + "<td align= right valign=top>  </td>";
                    StrHTML1 = StrHTML1 + "</tr>";
                }
            }
            crs1.close();
            StrHTML1 = StrHTML1 + "</table>";
            return StrHTML1;
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
