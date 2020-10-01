package axela.service;
//sangita

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Status extends Connect {

    public static String msg = "";
    public String starttime = "", start_time = "";
    public String endtime = "", end_time = "";
    public String emp_id = "",comp_id = "0", branch_id = "0";
    public String[] team_ids, exe_ids, technicianexe_ids, advisorexe_ids, model_ids, jcpsfdays_ids;
    public String exe_id = "", technicianexe_id = "", advisorexe_id = "", model_id = "";
    public String StrHTML = "", StrClosedHTML = "";
    public String BranchAccess = "", dr_branch_id = "0";
    public String PSFSearch = "";
    public String ExeAccess = "";
    public String chart_data = "";
    public String chart_data_q1 = "";
    public String chart_data_q2 = "";
    public String chart_data_q3 = "";
    public String chart_data_q11 = "";
    public String chart_data_q12 = "";
    public String chart_data_q13 = "";
    public String chart_data_q14 = "";
    public String chart_data_q15 = "";
    public String chart_data_q16 = "";
    public String chart_data_q17 = "";
    public String chart_data_concern = "";
    public String go = "";
    public String NoChart = "";
    public String NoChartQ1 = "";
    public String NoChartQ2 = "";
    public String NoChartQ3 = "";
    public String NoChartQ11 = "";
    public String NoChartQ12 = "";
    public String NoChartQ13 = "";
    public String NoChartQ14 = "";
    public String NoChartQ15 = "";
    public String NoChartQ16 = "";
    public String NoChartQ17 = "";
    public String NoChartConcern = "";
    public int chart_data_total = 0;
    public String chart_data_q1_total = "";
    public String chart_data_q2_total = "";
    public String chart_data_q3_total = "";
    public String chart_data_q11_total = "";
    public String chart_data_q12_total = "";
    public String chart_data_q13_total = "";
    public String chart_data_q14_total = "";
    public String chart_data_q15_total = "";
    public String chart_data_q16_total = "";
    public String chart_data_q17_total = "";
    public String chart_data_concern_total = "";
    public int TotalRecords = 0;
    public String StrSql = "";
    public String ModelJoin = "";
    public String psf_psfdays_id = "";
    public Report_Check reportexe = new Report_Check();
    public String[] chartq = null;
    public String uniquesplit = "!@#";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
             emp_id = CNumeric(GetSession("emp_id", request));
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
            go = PadQuotes(request.getParameter("submit_button"));
//            SOP("ExeAccess status= " + ExeAccess);
            GetValues(request, response);
            CheckForm();
            if (go.equals("Go")) {
                PSFSearch = ExeAccess + BranchAccess.replace("branch_id", "jc_branch_id") + "";

                if (!starttime.equals("")) {
                    PSFSearch = PSFSearch + " and SUBSTR(jcpsf_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
                }
                if (!endtime.equals("")) {
                    PSFSearch = PSFSearch + " and SUBSTR(jcpsf_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
                }
                if (!exe_id.equals("")) {
                    PSFSearch = PSFSearch + " and jcpsf_emp_id in (" + exe_id + ")";
                }
                if (!technicianexe_id.equals("")) {
                    PSFSearch = PSFSearch + " and jc_technician_emp_id in (" + technicianexe_id + ")";
                }
                if (!advisorexe_id.equals("")) {
                    PSFSearch = PSFSearch + " and jc_emp_id in (" + advisorexe_id + ")";
                }
                if (!dr_branch_id.equals("0")) {
                    PSFSearch = PSFSearch + " and jc_branch_id =" + dr_branch_id;
                }
                if (!model_id.equals("")) {
                    ModelJoin = " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
                            + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                            + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
//                    " INNER JOIN " + compdb(comp_id) + "axela_service_jc_item ON jcitem_jc_id = jc_id"
                    PSFSearch = PSFSearch + " and item_model_id in (" + model_id + ")";
                }
                if (!psf_psfdays_id.equals("")) {
                    PSFSearch = PSFSearch + " and jcpsf_jcpsfdays_id in (" + psf_psfdays_id + ")";
                }
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                }
                if (msg.equals("")) {
                    PreparePieChart();
                    SOP("noChart = " + NoChart);
                    /////3rd day q1////
                    chartq = PreparePieChartRatingQ("jcpsf_q1_rating").split(uniquesplit);
                    chart_data_q1 = chartq[0];
                    chart_data_q1_total = chartq[1];
                    if (chart_data_q1.equals("")) {
                        NoChartQ1 = "<font color=red><b>No PSF Rating Found For 3rd Day Q1!</b></font>";
                    }
                    /////3rd day q2////  
                    chartq = PreparePieChartRatingQ("jcpsf_q2_rating").split(uniquesplit);
                    chart_data_q2 = chartq[0];
                    chart_data_q2_total = chartq[1];
                    if (chart_data_q2.equals("")) {
                        NoChartQ2 = "<font color=red><b>No PSF Rating Found For 3rd Day Q2!</b></font>";
                    }
//                    /////3rd day q3////
                    chartq = PreparePieChartRatingQ("jcpsf_q1_rating").split(uniquesplit);
                    chart_data_q3 = chartq[0];
                    chart_data_q3_total = chartq[1];
                    if (chart_data_q3.equals("")) {
                        NoChartQ3 = "<font color=red><b>No PSF Rating Found For 3rd Day Q3!</b></font>";
                    }
//                    /////7th day q1////
                    chartq = PreparePieChartRatingQ("jcpsf_q11_rating").split(uniquesplit);
                    chart_data_q11 = chartq[0];
                    chart_data_q11_total = chartq[1];
                    if (chart_data_q11.equals("")) {
                        NoChartQ11 = "<font color=red><b>No PSF Rating Found For 7th Day Q1!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q12_rating").split(uniquesplit);
                    chart_data_q12 = chartq[0];
                    chart_data_q12_total = chartq[1];
                    if (chart_data_q12.equals("")) {
                        NoChartQ12 = "<font color=red><b>No PSF Rating Found For 7th Day Q2!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q13_rating").split(uniquesplit);
                    chart_data_q13 = chartq[0];
                    chart_data_q13_total = chartq[1];
                    if (chart_data_q13.equals("")) {
                        NoChartQ13 = "<font color=red><b>No PSF Rating Found For 7th Day Q3!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q14_rating").split(uniquesplit);
                    chart_data_q14 = chartq[0];
                    chart_data_q14_total = chartq[1];
                    if (chart_data_q14.equals("")) {
                        NoChartQ14 = "<font color=red><b>No PSF Rating Found For 7th Day Q4!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q15_rating").split(uniquesplit);
                    chart_data_q15 = chartq[0];
                    chart_data_q15_total = chartq[1];
                    if (chart_data_q15.equals("")) {
                        NoChartQ15 = "<font color=red><b>No PSF Rating Found For 7th Day Q5!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q16_rating").split(uniquesplit);
                    chart_data_q16 = chartq[0];
                    chart_data_q16_total = chartq[1];  
                    if (chart_data_q16.equals("")) {
                        NoChartQ16 = "<font color=red><b>No PSF Rating Found For 7th Day Q6!</b></font>";
                    }
                    chartq = PreparePieChartRatingQ("jcpsf_q17_rating").split(uniquesplit);
                    chart_data_q17 = chartq[0];
                    chart_data_q17_total = chartq[1];
                    if (chart_data_q17.equals("")) {
                        NoChartQ17 = "<font color=red><b>No PSF Rating Found For 7th Day Q7!</b></font>";
                    }
                    PreparePieChartConcern();
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
        starttime = PadQuotes(request.getParameter("txt_starttime"));
        endtime = PadQuotes(request.getParameter("txt_endtime"));
        if (starttime.equals("")) {
            starttime = ReportStartdate();
        }
        if (endtime.equals("")) {
            endtime = strToShortDate(ToShortDate(kknow()));
        }
        if (branch_id.equals("0")) {
            dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
        } else {
            dr_branch_id = branch_id;
        }
        exe_id = RetrunSelectArrVal(request, "dr_executive");
        exe_ids = request.getParameterValues("dr_executive");
        technicianexe_id = RetrunSelectArrVal(request, "dr_technician");
        technicianexe_ids = request.getParameterValues("dr_technician");
        advisorexe_id = RetrunSelectArrVal(request, "dr_advisor");
        advisorexe_ids = request.getParameterValues("dr_advisor");

        model_id = RetrunSelectArrVal(request, "dr_model");
        model_ids = request.getParameterValues("dr_model");
        psf_psfdays_id = RetrunSelectArrVal(request, "dr_jcpsfdays");
        jcpsfdays_ids = request.getParameterValues("dr_jcpsfdays");
    }

    protected void CheckForm() {
        msg = "";
        if (dr_branch_id.equals("0")) {
            msg = msg + "<br>Select Branch!";
        }

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
//                endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
            } else {
                msg = msg + "<br>Enter Valid End Date!";
                endtime = "";
            }
        }

    }

    public String PreparePieChart() {
        StrSql = " SELECT jcpsffeedbacktype_id, jcpsffeedbacktype_name, count(jcpsf_id) as Total ";
        String CountSql = " SELECT Count(distinct jcpsffeedbacktype_id)";
        String StrJoin = " FROM " + compdb(comp_id) + "axela_service_jc_psffeedbacktype"
                + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jcpsffeedbacktype_id = jcpsffeedbacktype_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = jcpsf_jc_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
                + ModelJoin
                + " WHERE 1 = 1  and jc_active = 1 " + PSFSearch + "";
        CountSql = CountSql + StrJoin;
//        SOP("PreparePieChart= " +   CountSql );
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
        StrJoin = StrJoin + " GROUP BY jcpsffeedbacktype_id ORDER BY Total desc";
        StrSql = StrSql + StrJoin;
//        SOP("str--" + StrSqlBreaker(StrSql));
        int count = 0;
        CachedRowSet crs = processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                chart_data = "[";
                while (crs.next()) {
                    count++;
//                    chart_data = chart_data + "['" + crs.getString("jcpsffeedbacktype_name") + " (" + crs.getString("Total") + ")'," + crs.getString("Total") + "]";
                    chart_data = chart_data + "{'type': '" + crs.getString("jcpsffeedbacktype_name") + "', 'total':" + crs.getString("Total") + "}";
                    chart_data_total = chart_data_total + crs.getInt("Total");
                    if (count < TotalRecords) {
                        chart_data = chart_data + ",";
                    } else {
                    }
                }
                chart_data = chart_data + "]";
            } else {
                NoChart = "<font color=red><b>No PSF Follow-up Found!</b></font>";
            }
//            SOP("chart_data = " + chart_data);
            crs.close();
        } catch (SQLException ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }

        return "";
    }

    public String PreparePieChartRatingQ(String qfield) {
        StringBuilder str = new StringBuilder();
        int qtotal = 0;
        StrSql = " SELECT " + qfield + ", count(jcpsf_id) as Total ";
        String CountSql = " SELECT Count(distinct jcpsf_id)";
        String StrJoin = " FROM " + compdb(comp_id) + "axela_service_jc_psf"
                + " INNER JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = jcpsf_jc_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
                + ModelJoin
                + " WHERE  " + qfield + " != 0 and  jc_active = 1" + PSFSearch + "";
        CountSql = CountSql + StrJoin;
        StrJoin = StrJoin + " GROUP BY " + qfield + " ORDER BY " + qfield + " ";
        StrSql = StrSql + StrJoin;
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));  
//        SOP("str--" + StrSqlBreaker(StrSql));
        int count = 0;
        CachedRowSet crs = processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                str.append("[");
                while (crs.next()) {
                    count++;
                    str.append("{'type': '").append(crs.getString(qfield)).append("', 'total':").append(crs.getString("Total")).append("}");
                    qtotal = qtotal + crs.getInt("Total");
                    if (count < TotalRecords) {
                        str.append(",");
                    } else {
                    }
                }
                str.append("]");
            }
            crs.close();
        } catch (SQLException ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return str.toString() + uniquesplit + qtotal;
    }

    public String PreparePieChartConcern() {
        StrSql = " SELECT jcpsfconcern_id, jcpsfconcern_desc, count(jcpsf_id) as Total ";
        String CountSql = " SELECT Count(distinct jcpsfconcern_id)";
        String StrJoin = " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
                + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jcpsfconcern_id = jcpsfconcern_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = jcpsf_jc_id"
                + " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
                + ModelJoin
                + " WHERE 1 = 1  and jc_active = 1" + PSFSearch + "";
        CountSql = CountSql + StrJoin;
        StrJoin = StrJoin + " GROUP BY jcpsfconcern_id ORDER BY Total desc";
        StrSql = StrSql + StrJoin;
        TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
//        SOP("str--" + StrSqlBreaker(StrSql));
        int count = 0;
        CachedRowSet crs = processQuery(StrSql, 0);
        try {
            if (crs.isBeforeFirst()) {
                chart_data_concern = "[";
                while (crs.next()) {
                    count++;
//                    chart_data_concern = chart_data_concern + "['" + crs.getString("jcpsfconcern_desc") + " (" + crs.getString("Total") + ")'," + crs.getString("Total") + "]";
                    chart_data_concern = chart_data_concern + "{'type': '" + crs.getString("jcpsfconcern_desc") + "', 'total':" + crs.getString("Total") + "}";
                    chart_data_concern_total = chart_data_concern_total + crs.getInt("Total");
                    if (count < TotalRecords) {
                        chart_data_concern = chart_data_concern + ",";
                    } else {
                    }
                }
                chart_data_concern = chart_data_concern + "]";
            } else {
                NoChartConcern = "<font color=red><b>No PSF Concern Found !</b></font>";
            }
            crs.close();
        } catch (SQLException ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }

        return "";
    }

    public String PopulatePSFExecutives() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE emp_active = '1' and (emp_service_psf=1 OR emp_service_psf_iacs=1 OR emp_crm=1) and"
                    + " (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
                    + " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                    + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                    + " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + ""
                    + " group by emp_id order by emp_name";
//            SOP("StrSql==" + StrSqlBreaker(StrSql));
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

    public String PopulateModel() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT model_id, model_name"
                    + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
                    + " GROUP BY model_id"
                    + " ORDER BY model_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("model_id"));
                Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
                Str.append(">").append(crs.getString("model_name")).append("</option>\n");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
