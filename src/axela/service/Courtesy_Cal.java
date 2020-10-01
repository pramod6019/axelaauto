// Dilip Kumar 04 APR 2013
package axela.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Courtesy_Cal extends Connect {

    public String add = "";
    public String submitB = "";
    public String StrHTML = "";
    public String StrSql = "";
    public String comp_id = "0";
    public static String msg = "";
    public String QueryString = "";
    public String branch_id = "";
    public String courtesyveh_branch_id = "0";
    public String courtesyveh_id = "";
    public String[] courtesyveh_ids;
    public String courtesycar_time_from = strToShortDate(ToShortDate(kknow())), starttime;
    public String courtesycar_time_to = strToShortDate(ToShortDate(kknow())), endtime;
    public int colspan = 0;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            branch_id = CNumeric(GetSession("emp_branch_id", request));
            if (branch_id.equals("0")) {
                colspan = 1;
            } else {
                colspan = 3;
                courtesyveh_branch_id = branch_id;
            }
            submitB = PadQuotes(request.getParameter("submit_button"));
            if (submitB.equals("Submit")) {
                GetValues(request, response);
                CheckForm();
                if (msg.equals("")) {
                    StrHTML = CourtesyCalendar();
                } else {
                    msg = "Error!" + msg;
                }
            }
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        courtesyveh_branch_id = PadQuotes(request.getParameter("dr_branch"));
        courtesyveh_id = RetrunSelectArrVal(request, "dr_vehicle");
        courtesyveh_ids = request.getParameterValues("dr_vehicle");
        courtesycar_time_from = PadQuotes(request.getParameter("txt_courtesycar_time_from"));
        courtesycar_time_to = PadQuotes(request.getParameter("txt_courtesycar_time_to"));
        if (courtesycar_time_from.equals("")) {
            courtesycar_time_from = strToShortDate(ToShortDate(kknow()));
        }
        if (courtesycar_time_to.equals("")) {
            courtesycar_time_to = strToShortDate(ToShortDate(kknow()));
        }
    }

    protected void CheckForm() {
        msg = "";
        if (courtesyveh_branch_id.equals("0")) {
            msg = "<br>Select Branch!";
        }
        if (courtesycar_time_from.equals("")) {
            msg = msg + "<br>Select Start Date!";
        }
        if (!courtesycar_time_from.equals("") && !isValidDateFormatShort(courtesycar_time_from)) {
            msg = msg + "<br>Enter valid Start date!";
        }
        if (courtesycar_time_to.equals("")) {
            msg = msg + "<br>Select End Date!";
        }
        if (!courtesycar_time_to.equals("") && !isValidDateFormatShort(courtesycar_time_to)) {
            msg = msg + "<br>Enter valid End date!";
        }
        if (!courtesycar_time_from.equals("")
                && isValidDateFormatShort(courtesycar_time_from)
                && !courtesycar_time_to.equals("")
                && isValidDateFormatShort(courtesycar_time_to)) {
            starttime = ConvertShortDateToStr(courtesycar_time_from);
            endtime = ConvertShortDateToStr(courtesycar_time_to);
            if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
                msg = msg + "<br>End Date should be greater than Start Date!";
            }
        }
    }

    public String CourtesyCalendar() {
        StringBuilder Str = new StringBuilder();
        Date date;
        String today;
        String StrSearch = "";
        if (!courtesyveh_id.equals("")) {
            StrSearch = " AND courtesyveh_id in (" + courtesyveh_id + ")";
        }
        if (!courtesyveh_branch_id.equals("") && !courtesyveh_branch_id.equals("0")) {
            StrSearch = StrSearch + " AND courtesyveh_branch_id = " + courtesyveh_branch_id + "";
        }
        try {
			Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");	
			Str.append("<div class=\"container-fluid\">\n");
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<tr>\n");
			Str.append("<th data-toggle=\"true\">#</th>\n");
			Str.append("<th>Days</th>\n");
			Str.append("<th>Courtesy Details</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			Str.append("</div>");
            double days = getDaysBetween(starttime, endtime);
            for (int i = 0; i <= days; i++) {
                date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + ""), 0.0, 0.0);
                StrSearch = " and courtesycar_time_from >= " + ToLongDate(date) + "";
                if (isSunday(ToLongDate(date))) {
                    Str.append("<tr bgcolor=pink>\n");
                } else {
                    Str.append("<tr>\n");
                }
                today = ConvertLongDate(ToLongDate(date));
                date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + 1 + ""), 0.0, 0.0);
                StrSearch = StrSearch + " AND courtesycar_time_from < " + ToLongDate(date) + "";
                StrSql = "SELECT courtesycar_id, courtesyveh_name, courtesyveh_regno,"
                        + " courtesycar_contact_name, courtesycar_time_from, courtesycar_time_to"
                        + " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
                        + " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle on courtesyveh_id = courtesycar_courtesyveh_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = courtesycar_customer_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
                        + " WHERE 1 = 1 AND courtesyveh_branch_id = " + courtesyveh_branch_id + "";

                StrSql = StrSql + StrSearch;
                StrSql = StrSql + " GROUP BY courtesycar_id ORDER BY courtesycar_time_from";
//                SOP(StrSql);
                CachedRowSet crs = processQuery(StrSql, 0);
                Str.append("<td valign=top>").append(today).append("<br></td>\n");
                if (crs.isBeforeFirst()) {
                    Str.append("<td valign=top><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">"
                            + "<tr>");
                    while (crs.next()) {
                        Str.append("<td valign=top><b>");
                        if (!crs.getString("courtesycar_time_from").equals("")) {
                            Str.append(strToLongDate(crs.getString("courtesycar_time_from")));
                        }
                        Str.append(" - ");
                        if (!crs.getString("courtesycar_time_to").equals("")) {
                            Str.append(strToLongDate(crs.getString("courtesycar_time_to")));
                        }
                        Str.append("</b><br>");
                        Str.append("<a href=\"courtesy-list.jsp?courtesycar_id=");
                        Str.append(crs.getString("courtesycar_id")).append("\" target=_blank><b>Courtesy ID</b> : ");
                        Str.append(crs.getString("courtesycar_id")).append("</a><br>");
                        if (!crs.getString("courtesyveh_name").equals("")) {
                            Str.append("<b>Vehicle</b> : ").append(crs.getString("courtesyveh_name")).append("<br>");
                        }
                        if (!crs.getString("courtesyveh_regno").equals("")) {
                            Str.append("<b>Reg. No.</b> : ").append(crs.getString("courtesyveh_regno")).append("<br>");
                        }
                        if (!crs.getString("courtesycar_contact_name").equals("")) {
                            Str.append("<b>Contact Name</b> : ").append(crs.getString("courtesycar_contact_name"));
                        }
                        Str.append("</td>");
                    }
                    Str.append("</tr></table></td></div>");
                } else {
                    Str.append("<td valign=top align=center>---</td></tr>");
                }
                crs.close();
            }
            Str.append("</table>");
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String PopulateVehicle(String courtesyveh_branch_id) {
        StringBuilder Str = new StringBuilder();
        try {
            Str.append("<Select name=dr_vehicle id=dr_vehicle class='form-control multiselect-dropdown' multiple size=10 style=\"width:250px\">");
            StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " WHERE courtesyveh_branch_id != 0"
                    + " AND courtesyveh_branch_id = " + courtesyveh_branch_id + ""
                    + " AND courtesyveh_active = '1'"
                    + " GROUP BY courtesyveh_id"
                    + " ORDER BY courtesyveh_name";
            CachedRowSet crs = processQuery(StrSql, 0);

            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("courtesyveh_id"));
                Str.append(ArrSelectdrop(crs.getInt("courtesyveh_id"), courtesyveh_ids));
                Str.append(">").append(crs.getString("courtesyveh_name")).append(" - ");
                Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
            }
            crs.close();
            Str.append("</select>");
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
