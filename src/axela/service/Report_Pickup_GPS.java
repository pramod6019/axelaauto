//09th may 2014
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Pickup_GPS extends Connect {

    public String StrSql = "", map = "", gps = "", data = "";
    public static String msg = "";
    public String emp_id = "", comp_id = "0", branch_id = "", gps_data = "";
    public String exe_id = "";
    public String StrHTML = "", google_api_key = "";
    public String BranchAccess = "", dr_branch_id = "0";
    public String StrSearch = "", gps_marker = "";
    public String go = "", gps_time = "", gpstime = "";
    public String[] exe_ids;
    public String ExeGpsHtml = "";
    public String ExeAccess = "";
    public String marker = "", marker_content = "", no_data = "", no_data_msg = "";
    public int count = 0;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
             emp_id = CNumeric(GetSession("emp_id", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            BranchAccess = CheckNull(GetSession("ExeAccess", request));
            CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
            go = PadQuotes(request.getParameter("submit_button"));
            GetValues(request, response);
//            CheckForm();
            if (go.equals("Go")) {
                GetValues(request, response);
                CheckForm();
                StrSearch = BranchAccess.replaceAll("branch_id", "emp_branch_id").replace(")", "  or emp_branch_id=0)") + " " + ExeAccess;

                if (!exe_id.equals("")) {
                    StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
//                    SOP("TargetSearch" + StrSearch);
                }
                if (!dr_branch_id.equals("0")) {
                    StrSearch = StrSearch + " and  (emp_branch_id=" + dr_branch_id + " or emp_id= 1 or emp_id in (select empbr.emp_id from "
                            + " " + compdb(comp_id) + "axela_emp_branch empbr where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and "
                            + " empbr.emp_branch_id = " + dr_branch_id + "))";
                }
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                }
                if (msg.equals("")) {
                    google_api_key = ExecuteQuery("SELECT config_google_api_key"
                            + " FROM " + compdb(comp_id) + "axela_config ");
                    ListExecutiveGPS();
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
        gpstime = PadQuotes(request.getParameter("txt_gps_time"));
        exe_id = RetrunSelectArrVal(request, "dr_executive");
        exe_ids = request.getParameterValues("dr_executive");
    }

    protected void CheckForm() {
        msg = "";
        if (dr_branch_id.equals("0")) {
            msg += "<br>Select Branch!";
        }
        gps_time = gpstime;
//        SOP("gpstime = " + gpstime);
    }

    public void ListExecutiveGPS() {
        StrSql = "SELECT emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name,"
                + " coalesce((select concat(gps_latitude,', ',gps_longitude,'-',gps_time)"
                + " FROM " + compdb(comp_id) + "axela_emp_gps"
                + " where gps_emp_id = emp_id"
                + " and gps_latitude!=''"
                + " and gps_longitude!=''";
        if (!gpstime.equals("")) {
            StrSql = StrSql + " and substr(gps_time,1,8) <= " + ConvertLongDateToStr(gpstime).substring(0, 8) + "";
        }
        StrSql = StrSql + " order by gps_id desc limit 1), 0) as gps"
                + " from " + compdb(comp_id) + "axela_emp"
                + " WHERE emp_active = 1 and emp_pickup_driver='1'"
                + StrSearch + ""
                + " GROUP BY emp_id"
                + " ORDER BY emp_name";
//        SOP("ListExecutiveGPS StrSql = " + StrSql);
        try {
            int totalcount = 0, nodatacount = 0;

            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                String[] parts = null;
                while (crs.next()) {
//                    SOP("parts = " );
                    totalcount++;
                    map = "yes";
                    parts = crs.getString("gps").split("-");
                    if (!parts[0].equals("0")) {
                        gps = "yes";
                        marker = marker + "[" + parts[0] + "],\n";
                        marker_content = marker_content
                                + "['<div style=line-height:1.35;overflow:hidden;white-space:nowrap;><b>"
                                + crs.getString("emp_name") + "<br>" + strToLongDate(parts[1]) + "</b><div>'],\n";
                        gps_data = gps_data + crs.getString("emp_name") + ",";
                        gps_marker = gps_marker + parts[0] + ";";
                    } else {
                        data = "no";
                        nodatacount++;
                        no_data = no_data + crs.getString("emp_name") + ", ";
                    }
                }
                count = totalcount - nodatacount;
                if (gps.equals("yes")) {

                    marker = marker.substring(0, marker.length() - 2);
                    marker_content = marker_content.substring(0, marker_content.length() - 2);
                }
                if (data.equals("no")) {
                    no_data = no_data.substring(0, no_data.length() - 2);
                    no_data_msg = "No co-ordinates found for " + no_data;
                }
            } else {
                map = "no";
            }
//            SOP("no_data = " + no_data);
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateServiceExecutives() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE emp_active = '1' and emp_pickup_driver='1' and (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
                    + " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                    + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                    + " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess
                    + " group by emp_id order by emp_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
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

    public String GpsExecutives(String gps_data, String gps_marker, String google_api_key) {
//        SOP("gps_marker = " + gps_marker);
//        SOP("gps_data = " + gps_data);
        String[] token = gps_data.split(",");
        String[] markertoken = gps_marker.split(";");
        StringBuilder Str = new StringBuilder();
        Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
        for (int i = 1; i <= token.length; i++) {
            Str.append("<tr align=left>");
            Str.append("<td id=\"gps_" + i + "\"  name=\"gps_" + i + "\"  "
                    + " onClick=\"populatemap('" + markertoken[i - 1] + "', '" + google_api_key + "');\" "
                    //                + "onClick=\"javascript:populatemap("+marker+", "+marker_content+");\""
                    + ">" + token[i - 1] + "</td>\n");
            Str.append("</tr>");
        }
        Str.append("</table>");
//        SOP("Str = " + Str);
        return Str.toString();
    }
}
