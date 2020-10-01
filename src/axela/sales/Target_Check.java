package axela.sales;
/*Smitha Nag 2, 3 april 2013 */
//divya 9th may

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Check extends Connect {

    public String exe_id = "";
    public String StrSql = "";
    public String StrHTML = "";
    public String startdate = "";
    public String enddate = "";
    public String comp_id = "0";
    public String ExeAccess = "";
    public String TargetSearch = "";
    public String DateSearch = "";
    public String ExeSearch = "";
    public String BranchAccess = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        comp_id = CNumeric(GetSession("comp_id", request));
        if(!comp_id.equals("0")) {
        	startdate = PadQuotes(request.getParameter("month_id"));
//          SOP("startdate" + startdate);
          enddate = AddMonth(startdate, 1);
          enddate = ConvertShortDateToStr(enddate);
          exe_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
          ExeAccess = GetSession("ExeAccess", request);
          BranchAccess = GetSession("BranchAccess", request);

          TargetSearch = ExeAccess.replace("emp_id", "target_emp_id") + "";
          TargetSearch = TargetSearch + " and target_startdate>= '" + ConvertShortDateToStr(startdate) + "' and target_startdate< '" + enddate + "'";
          DateSearch = " and startdate>= '" + ConvertShortDateToStr(startdate) + "' and startdate< '" + enddate + "'";
          ExeSearch = ExeAccess;
          if (!exe_id.equals("0")) {
              TargetSearch = TargetSearch + " and target_emp_id in (" + exe_id + ")";
              ExeSearch = " and emp_id in (" + exe_id + ")";
          }

          StrHTML = ListTarget();
        }
    }

    public String ListTarget() {
        StringBuilder Str = new StringBuilder();
        int enquiry_count_t = 0, enquiry_count_a = 0, enquiry_count_perc = 0;
        int enquiry_calls_t = 0, enquiry_calls_a = 0, enquiry_calls_perc = 0;
        int enquiry_meetings_t = 0, enquiry_meetings_a = 0, enquiry_meetings_perc = 0;
        int enquiry_testdrives_t = 0, enquiry_testdrives_a = 0, enquiry_testdrives_perc = 0;
        int enquiry_hot_t = 0, enquiry_hot_a = 0, enquiry_hot_perc = 0;
        int enquiry_so_t = 0, enquiry_so_a = 0, enquiry_so_perc = 0;
        int enquiry_so_amt_t = 0, enquiry_so_amt_a = 0, enquiry_so_amt_perc = 0;

        try {
            StrSql = "SELECT model_id, model_name,  "
                    //enquiry_count_t
                    + " @enquiry_count_t:=coalesce((select sum(modeltarget_enquiry_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_count_t ,"
                    //enquiry_count_a
                    + " @enquiry_count_a:=(select coalesce(count(enquiry_id),0)  "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " where enquiry_model_id = model_id "
                    + DateSearch.replace("startdate", "enquiry_date")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    //                    + ExeAccess.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_count_a, "
                    //enquiry_count_perc
                    + " coalesce((100*@enquiry_count_a/@enquiry_count_t),0) as enquiry_count_perc , "
                    //enquiry_calls_t
                    + " @enquiry_calls_t:=coalesce((select sum(modeltarget_enquiry_calls_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_calls_t,  "
                    //enquiry_calls_a
                    + " @enquiry_calls_a:=(select coalesce(count(followup_id),0)  "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on followup_enquiry_id=enquiry_id "
                    + " where enquiry_model_id = model_id and followup_followuptype_id=1"
                    + DateSearch.replace("startdate", "followup_followup_time")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_calls_a, "
                    //enquiry_calls_perc
                    + " coalesce((100*@enquiry_calls_a/@enquiry_calls_t),0) as enquiry_calls_perc , "
                    //enquiry_meetings_t
                    + " @enquiry_meetings_t:=coalesce((select sum(modeltarget_enquiry_meetings_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_meetings_t ,  "
                    //enquiry_meetings_a
                    + " @enquiry_meetings_a:=(select coalesce(count(enquiry_id),0)  "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup on followup_enquiry_id=enquiry_id "
                    + " where enquiry_model_id = model_id and followup_followuptype_id=2"
                    + DateSearch.replace("startdate", "followup_followup_time")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_meetings_a, "
                    //enquiry_meetings_perc
                    + " coalesce((100*@enquiry_meetings_a/@enquiry_meetings_t),0) as enquiry_meetings_perc , "
                    //enquiry_testdrives_t
                    + " @enquiry_testdrives_t:=coalesce((select sum(modeltarget_enquiry_testdrives_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_testdrives_t, "
                    //enquiry_testdrives_a
                    + " @enquiry_testdrives_a:=(select coalesce(count(testdrive_id),0)  "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_sales_testdrive on testdrive_enquiry_id=enquiry_id "
                    + " where enquiry_model_id = model_id and testdrive_fb_taken=1"
                    + DateSearch.replace("startdate", "testdrive_time")
                    + ExeSearch.replace("emp_id", "testdrive_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_testdrives_a, "
                    //enquiry_testdrives_perc
                    + " coalesce((100*@enquiry_testdrives_a/@enquiry_testdrives_t),0) as enquiry_testdrives_perc , "
                    //enquiry_hot_t
                    + " @enquiry_hot_t:=coalesce((select sum(modeltarget_enquiry_hot_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_hot_t, "
                    //enquiry_hot_a
                    + " @enquiry_hot_a:=(select coalesce(count(enquiry_id),0) "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " where enquiry_model_id = model_id and enquiry_priorityenquiry_id=1"
                    + DateSearch.replace("startdate", "enquiry_date")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_hot_a, "
                    //enquiry_hot_perc
                    + " coalesce((100*@enquiry_hot_a/@enquiry_hot_t),0) as enquiry_hot_perc , "
                    //enquiry_so_t
                    + " @enquiry_so_t:=coalesce((select sum(modeltarget_so_count) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_so_t ,  "
                    //enquiry_so_a
                    + " @enquiry_so_a:=(select coalesce(count(so_id),0) "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_sales_so on so_enquiry_id = enquiry_id  "
                    + " where enquiry_model_id = model_id and so_active='1'"
                    + DateSearch.replace("startdate", "so_date")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_so_a, "
                    //enquiry_so_perc
                    + " coalesce((100*@enquiry_so_a/@enquiry_so_t),0) as enquiry_so_perc , "
                    //enquiry_so_amt_t
                    + " @enquiry_so_amt_t:=coalesce((select sum(modeltarget_so_amount) "
                    + " from " + compdb(comp_id) + "axela_sales_target_model "
                    + " inner join " + compdb(comp_id) + "axela_sales_target on target_id=modeltarget_target_id "
                    + " where modeltarget_model_id=model_id "
                    + " " + TargetSearch + " ),0) as enquiry_so_amt_t, "
                    //enquiry_so_amt_a
                    + " @enquiry_so_amt_a:=(select coalesce(sum(so_grandtotal),0) "
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_sales_so on so_enquiry_id = enquiry_id  "
                    + " where enquiry_model_id = model_id and so_active='1'"
                    + DateSearch.replace("startdate", "so_date")
                    + ExeSearch.replace("emp_id", "enquiry_emp_id")
                    + BranchAccess.replace("branch_id", "enquiry_branch_id") + ") as enquiry_so_amt_a,  "
                    //enquiry_so_amt_perc
                    + " coalesce((100*@enquiry_so_amt_a/@enquiry_so_amt_t),0) as enquiry_so_amt_perc "
                    // ====== done =====
                    + " FROM " + compdb(comp_id) + "axela_inventory_item_model  "
                    + " where 1=1 and model_name!='Pre Owned'"
                    + " group by model_name"
                    + " order by model_name";

//            SOP("query list target..." + StrSqlBreaker(StrSql));
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
                Str.append("<tr align=center>\n");
                Str.append("<th>Model</th>\n");
                Str.append("<th colspan=3>Enquiry Count</th>\n");
                Str.append("<th colspan=3>Enquiry Calls</th>\n");
                Str.append("<th colspan=3>Enquiry Meeting</th>\n");
                Str.append("<th colspan=3>Enquiry Test Drives</th>\n");
                Str.append("<th colspan=3>Enquiry Hot</th>\n");
                Str.append("<th colspan=3>SO Count</th>\n");
                Str.append("<th colspan=3>SO Amount</th>\n");
                Str.append("</tr>\n");
                Str.append("<tr>\n");
                Str.append("<th>&nbsp;</th>");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("<th>T</th>\n");
                Str.append("<th>A</th>\n");
                Str.append("<th>%</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    enquiry_count_t = enquiry_count_t + crs.getInt("enquiry_count_t");
                    enquiry_count_a = enquiry_count_a + crs.getInt("enquiry_count_a");
                    enquiry_count_perc = enquiry_count_perc + crs.getInt("enquiry_count_perc");
                    enquiry_calls_t = enquiry_calls_t + crs.getInt("enquiry_calls_t");
                    enquiry_calls_a = enquiry_calls_a + crs.getInt("enquiry_calls_a");
                    enquiry_calls_perc = enquiry_calls_perc + crs.getInt("enquiry_calls_perc");
                    enquiry_meetings_t = enquiry_meetings_t + crs.getInt("enquiry_meetings_t");
                    enquiry_meetings_a = enquiry_meetings_a + crs.getInt("enquiry_meetings_a");
                    enquiry_meetings_perc = enquiry_meetings_perc + crs.getInt("enquiry_meetings_perc");
                    enquiry_testdrives_t = enquiry_testdrives_t + crs.getInt("enquiry_testdrives_t");
                    enquiry_testdrives_a = enquiry_testdrives_a + crs.getInt("enquiry_testdrives_a");
                    enquiry_testdrives_perc = enquiry_testdrives_perc + crs.getInt("enquiry_testdrives_perc");
                    enquiry_hot_t = enquiry_hot_t + crs.getInt("enquiry_hot_t");
                    enquiry_hot_a = enquiry_hot_a + crs.getInt("enquiry_hot_a");
                    enquiry_hot_perc = enquiry_hot_perc + crs.getInt("enquiry_hot_perc");
                    enquiry_so_t = enquiry_so_t + crs.getInt("enquiry_so_t");
                    enquiry_so_a = enquiry_so_a + crs.getInt("enquiry_so_a");
                    enquiry_so_perc = enquiry_so_perc + crs.getInt("enquiry_so_perc");
                    enquiry_so_amt_t = enquiry_so_amt_t + crs.getInt("enquiry_so_amt_t");
                    enquiry_so_amt_a = enquiry_so_amt_a + crs.getInt("enquiry_so_amt_a");
                    enquiry_so_amt_perc = enquiry_so_amt_perc + crs.getInt("enquiry_so_amt_perc");

                    Str.append("<tr>\n");
                    Str.append("<td valign=top align=left>").append(crs.getString("model_name")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_count_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_count_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_count_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_calls_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_calls_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_calls_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_meetings_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_meetings_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_meetings_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_testdrives_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_testdrives_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_testdrives_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_hot_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_hot_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_hot_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_perc")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_amt_t")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_amt_a")).append("</td>");
                    Str.append("<td valign=top align=right>").append(crs.getInt("enquiry_so_amt_perc")).append("</td>");
                    Str.append("</tr>\n");
                }
                Str.append("<tr>\n");
                Str.append("<td valign=top align=right><b>Total:</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_count_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_count_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_count_a, enquiry_count_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_calls_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_calls_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_calls_a, enquiry_calls_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_meetings_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_meetings_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_meetings_a, enquiry_meetings_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_testdrives_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_testdrives_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_testdrives_a, enquiry_testdrives_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_hot_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_hot_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_hot_a, enquiry_hot_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_so_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_so_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_so_a, enquiry_so_t)).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_so_amt_t).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(enquiry_so_amt_a).append("</b></td>");
                Str.append("<td valign=top align=right><b>").append(getPercentage(enquiry_so_amt_a, enquiry_so_amt_t)).append("</b></td>");
                Str.append("</tr>\n");
                Str.append("</table>\n");
            } else {
                Str.append("<br><br><br><br><font color=red>No Target(s) found!</font><br><br>");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
