package axela.portal;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_List_1 extends Connect {

    public String emp_idsession = "";
    public String StrHTML = "";
    public String all = "";
    public String msg = "";
    public String Up = "";
    public String Down = "";
    public String StrSql = "";
    public String SqlJoin = "";
    public String StrSearch = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    int PageCount = 10;
    int PageSpan = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String txt_search = "";
    public String drop_search;
    public String target_id = "";
    public String startdate = "";
    public String enddate = "";
//    public String franchisee_id ="";
//    public String subgroup_id ="";
//    public String prevsubgroup_id ="";
//    public String group_id ="";
    public String emp_id = "";
    public String comp_id = "0";
    public String year = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	 emp_id = CNumeric(GetSession("emp_id", request));
                 CheckPerm(comp_id, "emp_target_access", request, response);
                 PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
                 QueryString = PadQuotes(request.getQueryString());
                 all = PadQuotes(request.getParameter("all"));
                 msg = PadQuotes(request.getParameter("msg"));
                 target_id = PadQuotes(request.getParameter("target_id"));
                 startdate = PadQuotes(request.getParameter("target_startdate"));
                 enddate = PadQuotes(request.getParameter("target_enddate"));
                 emp_id = PadQuotes(request.getParameter("dr_executives"));
                 year = PadQuotes(request.getParameter("dr_year"));
                 // SOP("target_id---" + target_id);
//                 if (msg.toLowerCase().contains("delete")) {
//                     StrSearch = " AND "
//                 }
                 if ((emp_id.equals("0"))) {
                     msg = "Results for Employee Id =" + emp_id + "!";
                     StrSearch = StrSearch + " and target_emp_id =" + emp_id + "";

                 }
                 StrHTML = Listdata();
            }
           
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            drop_search = PadQuotes(request.getParameter("dr_search"));
            txt_search = PadQuotes(request.getParameter("txt_search"));
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String Listdata() {
        int TotalRecords = 0;
        String CountSql = "";
        StringBuilder Str = new StringBuilder();
        StrSql = "select month, startdate, enddate, amt from (";
        for (int i = 1; i <= 12; i++) {
            if (i != 1) {
                StrSql = StrSql + "UNION";
            }
            StrSql = StrSql + " (select '" + year + "' as month, 'target_startdate' as startdate, 'target_enddate' as enddate, coalesce((select target_so_amount from " + compdb(comp_id) + "axela_sales_target where  target_startdate='20120101000000' and target_enddate='20120131000000' and target_emp_id=2),'') as amt)  ";
        }
        StrSql = StrSql + ")t";
        //SOP("StrSql======" + StrSql);

//        }

        TotalRecords = Integer.parseInt(ExecuteQuery(StrSql));

        if (TotalRecords != 0) {
            RecCountDisplay = "<br><br>";
            // StrSql = StrSql + " order by target_id";
            try {
                CachedRowSet crs =processQuery(StrSql, 0);
                int count = 0, j = 0;
                String col = "white";
                String altcol = "#FFFFCC";
                String bgcol = col;
                Str.append("<table border=1 cellpadding=0 cellspacing=0 class=listtable>\n");
                Str.append("<tr align=center>\n");
                Str.append("<th width=5%>#</th>\n");
                Str.append("<th>Executives</th>\n");
                Str.append("<th>Months</th>\n");
                Str.append("<th>Amount</th>\n");
                // Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");
                SimpleDateFormat sdf = new SimpleDateFormat("MMM");

                while (crs.next()) {
                    if (j == 0) {
                        j = 1;
                        bgcol = col;
                    } else {
                        j = 0;
                        bgcol = altcol;
                    }
                    String months[] = {"JAN", "FEB", "MARCH", "APR", "MAY", "JUN", "JULY", "AUG", "SEP", "OCT", "NOV", "DEC"};

                    for (int i = 0; i < 12; i++) {
                        count = count + 1;
                        Str.append("<tr onmouseover=\"this.style.background='#eeeeee';\" onmouseout=\"this.style.background='" + bgcol + "';\" bgcolor='" + bgcol + "'>\n");
                        Str.append("<td valign=top align=center ><b>" + count + ".</b></td>\n");
                        Str.append("<td valign=top align=left ></td>");
                        Str.append("<td valign=top align=left >" + months[i] + "-" + year + "</td>\n");
                        Str.append("<td valign=top align=right >" + crs.getString("amt") + "</td>\n");
                        // Str.append("<td valign=top align=left ><a href=\"target-update.jsp?update=yes&target_id=" + crs.getString("target_id") + " \"> Update Target</a></td>");
                        Str.append("</tr>\n");
                    }
                    Str.append("</table>");
                }
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
                return "";
            }
        } else {
            Str.append("<br><br><font color=red><b>No Target(s) found!</b></font>");
        }

        return Str.toString();
    }

    public String PopulateEmp() {

        StringBuilder Str = new StringBuilder();
        Str.append("<option value = 0> Select </option>");
        try {

            StrSql = "SELECT emp_name, emp_id "
                    + " from " + compdb(comp_id) + "axela_emp "
                    + " where emp_sales =1 "
                    + " group by emp_id order by emp_name";

            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=" + crs.getString("emp_id") + "");
                Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
                Str.append(">" + crs.getString("emp_name") + "</option>\n");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

        }
        return Str.toString();
    }

    public String PopulateYear() {
        String year = "<option value = -1" + Selectdrop(-1, drop_search) + ">Select Year</option>";
        year = year + "<option value = 1" + Selectdrop(1, drop_search) + ">2008</option>\n";
        year = year + "<option value = 2" + Selectdrop(2, drop_search) + ">2009</option>\n";
        year = year + "<option value = 3" + Selectdrop(3, drop_search) + ">2010</option>\n";
        year = year + "<option value = 4" + Selectdrop(4, drop_search) + "> 2011</option>\n";
        year = year + "<option selected=\"selected\" value = 2012" + Selectdrop(5, drop_search) + ">2012</option>\n";
        year = year + "<option value = 6" + Selectdrop(6, drop_search) + ">2013</option>\n";
        year = year + "<option value = 7" + Selectdrop(7, drop_search) + ">2014</option>\n";
        year = year + "<option value = 8" + Selectdrop(8, drop_search) + ">2015</option>\n";
        year = year + "<option value = 9" + Selectdrop(9, drop_search) + ">2016</option>\n";
        year = year + "<option value = 10" + Selectdrop(10, drop_search) + ">2017</option>\n";
        return year;
    }
}
