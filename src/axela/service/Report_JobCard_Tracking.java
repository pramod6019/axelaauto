//sangita
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Report_JobCard_Tracking extends Connect {

    public String StrHTML = "";
    public String StrSearch = "";
    public String StrSql = "";
    public String branch_id = "0", dr_branch_id = "0";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String comp_id = "0";
    public String starttime = "", start_time = "";
    public String endtime = "", end_time = "";
    public String msg = "", ModelJoin = "";
    public String[] advisorexe_ids, technicianexe_ids, model_ids;
    public String advisorexe_id = "", technicianexe_id = "", model_id = "";
    public String exportcount = "";
    public String export = "";
    Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
    Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
    Font normal_font = FontFactory.getFont("Helvetica", 10);
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
            exportcount = ExecuteQuery("SELECT comp_export_count from " + compdb(comp_id) + "axela_comp");
            export = PadQuotes(request.getParameter("submit_button"));
            GetValues(request, response);
            CheckForm();
            if (export.equals("Print")) {
                StrSearch = BranchAccess + ExeAccess.replace("emp_id", "jc_emp_id") + "";

                if (!dr_branch_id.equals("0")) {
                    StrSearch += " AND jc_branch_id = " + dr_branch_id;
                }

                if (!starttime.equals("")) {
                    StrSearch = StrSearch + " and SUBSTR(jc_time_in, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
                }
                if (!endtime.equals("")) {
                    StrSearch = StrSearch + " and SUBSTR(jc_time_in, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
                }

                if (!advisorexe_id.equals("")) {
                    StrSearch = StrSearch + " and jc_emp_id in (" + advisorexe_id + ")";
                }

                if (!technicianexe_id.equals("")) {
                    StrSearch = StrSearch + " and jc_technician_emp_id in (" + technicianexe_id + ")";
                }
                if (!model_id.equals("")) {
//                    ModelJoin = " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = jc_variant_id"
//                            + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
//                    " INNER JOIN " + compdb(comp_id) + "axela_service_jc_item ON jcitem_jc_id = jc_id"
                    StrSearch = StrSearch + " and item_model_id in (" + model_id + ")";
                }
                if (!msg.equals("")) {
                    msg = "Error!" + msg;
                }

                if (msg.equals("")) {
                    ListJCManHours(response);
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
        advisorexe_id = RetrunSelectArrVal(request, "dr_advisor");
        advisorexe_ids = request.getParameterValues("dr_advisor");
        model_id = RetrunSelectArrVal(request, "dr_model");
        model_ids = request.getParameterValues("dr_model");
    }

    public void ListJCManHours(HttpServletResponse response) {
        try {
            StrSql = " SELECT jc_ro_no,"
                    + " COALESCE(veh_reg_no, '') AS 'veh_reg_no',"
                    + " IF(veh_iacs=1,'IACS','Walk in') AS 'veh_iacs',"
                    + " COALESCE(item_name,'') AS 'item_name',"
                    + " COALESCE(jc_instructions, '') AS 'jc_instructions',"
                    + " COALESCE(CONCAT(sa.emp_name, ' (', sa.emp_ref_no, ')'), '') AS 'advisor',"
                    + " COALESCE(DATE_FORMAT(jc_time_in, '%d/%m/%Y %h:%i'), '') AS 'jc_time_in',"
                    + " COALESCE(DATE_FORMAT(jc_time_ready, '%d/%m/%Y %h:%i'), '') AS 'jc_time_ready',"
                    + " COALESCE(DATE_FORMAT(jc_time_promised, '%d/%m/%Y %h:%i'), '') AS 'jc_time_promised',"
                    + " COALESCE(CONCAT(tech.emp_name, ' (', tech.emp_ref_no, ')'), '') AS 'technician',"
                    + " coalesce(GROUP_CONCAT(date_format(baytrans_start_time, '%d/%m/%y %h:%i') SEPARATOR '\n'),'') as 'starttime',"
                    + " coalesce(GROUP_CONCAT(date_format(baytrans_end_time, '%d/%m/%y %h:%i') SEPARATOR '\n'),'') as 'endtime',"
                    + " COALESCE(sum(time_to_sec(timediff(baytrans_end_time,baytrans_start_time))/60), '') AS 'manhours',"
                    + " COALESCE(jcstage_name, '') AS 'jcstage_name',branch_logo"
                    + " FROM " + compdb(comp_id) + "axela_service_jc"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp sa ON sa.emp_id = jc_emp_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_jc_id = jc_id"
                    + " WHERE jc_active='1'" + StrSearch + ""
                    + " GROUP BY jc_id"
                    + " ORDER BY jc_id"
                    + " LIMIT " + exportcount + "";
//            SOP("StrSql = " + StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                int count = 0;
                Document document = new Document(PageSize.A4.rotate());
                response.setContentType("application/pdf");
                PdfWriter.getInstance(document, response.getOutputStream());
                document.open();

                crs.last();

                PdfPTable top_table = new PdfPTable(2);
                top_table.setWidthPercentage(100);
                PdfPCell cell;

                cell = new PdfPCell();
                if (!crs.getString("branch_logo").equals("")) {
                    Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
                    cell.addElement(new Chunk(branch_logo, 0, 0));
                    cell.setFixedHeight(branch_logo.getHeight());
                } else {
                    cell.addElement(new Phrase(""));
                }
                cell.setBorderWidth(0);
                cell.setPaddingLeft(0);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                top_table.addCell(cell);

                crs.beforeFirst();

                cell = new PdfPCell(new Phrase("", header_font));
                cell.setBorderWidth(0);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                top_table.addCell(cell);

                document.add(top_table);

                PdfPTable table = new PdfPTable(15);
                table.setWidthPercentage(100);

                cell = new PdfPCell(new Phrase("#", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("GGN No", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Regn No.", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Appmnt/\nIACS/\nWalk in", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Model", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Job Description", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("SA Name", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Veh. In time", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Del.Time By SA", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Exp.Completion Time", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Tech Name", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Start Time", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Stop Time", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Clock Hours", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Status", bold_font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                while (crs.next()) {
                    count++;

                    cell = new PdfPCell(new Phrase(count + "", normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jc_ro_no")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("veh_reg_no")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("veh_iacs")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("item_name")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jc_instructions")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("advisor")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jc_time_in")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jc_time_ready")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jc_time_promised")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("technician")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("starttime")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("endtime")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(ConvertMintoHrsMins(crs.getInt("manhours")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase((crs.getString("jcstage_name")), normal_font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell); 
                } 
                document.add(table);
                document.close();
            } else {
                response.sendRedirect("../portal/error.jsp?msg=No Job Card Found!");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateAdvisor(String dr_branch_id) {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE emp_service = '1' and emp_active='1'"
                    + " and (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
                    + " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
                    + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
                    + " and empbr.emp_branch_id = " + dr_branch_id + "))"
                    + " " + ExeAccess + ""
                    + " group by emp_id order by emp_name";
//            SOP("StrSql=aaa=" + StrSqlBreaker(StrSql));
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<select name=dr_advisor id=dr_advisor class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), advisorexe_ids));
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
