// Sri Venkatesh 06 apr 2013
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

public class Pickup_Cal extends Connect {

    public String add = "", StrHTML = "", emp_id = "0";
    public String submitB = "";
    public String StrSql = "";
    public String comp_id = "0";
    public static String msg = "";
    public String[] pickup_emp_ids;
    public String pickup_time_from = strToShortDate(ToShortDate(kknow())), starttime;
    public String pickup_time_to = strToShortDate(ToShortDate(kknow())), endtime;
    public String branch_id = "";
    public String[] item_model_ids;
    public String pickup_branch_id = "0";  
    public String item_model_id = "0";
    public String pickup_emp_id = "0";
    public int colspan = 0;
    public String QueryString = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            CheckPerm(comp_id, "emp_service_pickup_access", request, response);
            emp_id = CNumeric(GetSession("emp_id", request));
            branch_id= CNumeric(GetSession("emp_branch_id", request));
            if (branch_id.equals("0")) {
                colspan = 1;
            } else {
                colspan = 3;
                pickup_branch_id = branch_id;
            }
            submitB = PadQuotes(request.getParameter("submit_button"));
            if (submitB.equals("Submit")) {
                GetValues(request, response);
                CheckForm();
                if (msg.equals("")) {
                    StrHTML = PickupCalendar();
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
        item_model_id = RetrunSelectArrVal(request, "dr_model");
        item_model_ids = request.getParameterValues("dr_model");
        pickup_branch_id = PadQuotes(request.getParameter("dr_branch"));
        pickup_emp_id = RetrunSelectArrVal(request, "dr_pickup_emp_id");
        pickup_emp_ids = request.getParameterValues("dr_pickup_emp_id");
        pickup_time_from = PadQuotes(request.getParameter("txt_pickup_time_from"));
        pickup_time_to = PadQuotes(request.getParameter("txt_pickup_time_to"));
        if (pickup_time_from.equals("")) {
            pickup_time_from = strToShortDate(ToShortDate(kknow()));
        }
        if (pickup_time_to.equals("")) {
            pickup_time_to = strToShortDate(ToShortDate(kknow()));
        }
    }

    protected void CheckForm() {
        msg = "";
        if (pickup_branch_id.equals("0")) {
            msg = "<br>Select Branch!";
        }
        if (pickup_time_from.equals("") || !isValidDateFormatShort(pickup_time_from)) {
            msg = msg + "<br>Select Start Date!";
        }
        if (pickup_time_to.equals("") || !isValidDateFormatShort(pickup_time_to)) {
            msg = msg + "<br>Enter valid End date!";  
        }
        if (!pickup_time_from.equals("")
                && isValidDateFormatShort(pickup_time_from)
                && !pickup_time_to.equals("")
                && isValidDateFormatShort(pickup_time_to)) {
            starttime = ConvertShortDateToStr(pickup_time_from);
            endtime = ConvertShortDateToStr(pickup_time_to);
            if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
                msg = msg + "<br>Start Date should be less than End date!";
            }
        }
    }

    public String PopulateDriver() {
        try {
            StringBuilder Str = new StringBuilder();
            StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
                    + " FROM " + compdb(comp_id) + "axela_emp"
                    + " WHERE 1 = 1 AND emp_active=1 AND emp_pickup_driver = 1"
                    + " AND (emp_branch_id=0 OR emp_branch_id = " + pickup_branch_id + ")"
                    + " GROUP BY emp_id ORDER BY emp_name";
//            SOP("hello" + StrSql);
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<select name=dr_pickup_emp_id id=dr_pickup_emp_id class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"width:250px\">");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("emp_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("emp_id"), pickup_emp_ids));
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

    public String PickupCalendar() {
        StringBuilder Str = new StringBuilder();
        Date date;
        String today;
        String StrSearch = "", search = "";
        if (!pickup_emp_id.equals("")) {
            search += " AND pickup_emp_id IN (" + pickup_emp_id + ")";
        }
        if (!pickup_branch_id.equals("") && !pickup_branch_id.equals("0")) {
            search += " AND pickup_branch_id = " + pickup_branch_id + "";
        }
        if (!item_model_id.equals("")) {  
            search += " AND item_model_id IN (" + item_model_id + ")";
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
			Str.append("<th>Pickup Details</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			Str.append("</div>");
            double days = getDaysBetween(starttime, endtime);
            for (int i = 0; i <= days; i++) {
                date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + ""), 0.0, 0.0);
                StrSearch = " AND pickup_time_from >= " + ToLongDate(date) + "";
                if (isSunday(ToLongDate(date))) {
                    Str.append("<tr align=left bgcolor=pink>\n");
                } else {
                    Str.append("<tr align=left>\n");
                }
                today = ConvertLongDate(ToLongDate(date));
                date = AddHoursDate(new SimpleDateFormat("dd/MM/yyyy").parse(FormatDate(starttime, "dd/MM/yyyy")), Double.parseDouble(i + 1 + ""), 0.0, 0.0);
                StrSearch = StrSearch + " AND pickup_time_from < " + ToLongDate(date) + "";
                StrSql = "SELECT pickup_id, contact_fname, branch_code, contact_id, pickup_time,"
                        + " pickup_time_to, pickup_time_from, pickup_notes, emp_id, contact_mobile1,"
                        + " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name, location_name,"
                        + " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact,"
                        + " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no"
                        + " FROM " + compdb(comp_id) + "axela_service_pickup"
                        + " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id = pickup_location_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
                        + " INNER Join " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
                        + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
                        + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = pickup_veh_id"
                        + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                        + " WHERE 1 = 1 AND pickup_active = 1";
                StrSql = StrSql + search + StrSearch;

                StrSql = StrSql + " GROUP BY pickup_id"
                        + " ORDER BY pickup_time_from";
//                SOP(StrSqlBreaker(StrSql));
                CachedRowSet crs = processQuery(StrSql, 0);
                Str.append("<td valign=top height=20 width=20%>").append(today).append("<br></td>\n");
                if (crs.isBeforeFirst()) {
                    Str.append("<td valign=top><table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");

                    while (crs.next()) {
                        Str.append("<tr><td valign=top><b>").append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "2")).append("</b>");
                        Str.append("<br>Pickup Time: <a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\" target=\"_blank\">");
                        Str.append(SplitHourMin(crs.getString("pickup_time"))).append("</a>");
                        Str.append("<br>Place: ").append(crs.getString("location_name"));
                        Str.append("<br>Driver/Technician: <a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
                        if (!crs.getString("veh_id").equals("0")) {
                            Str.append("<br>Vehicle: <a href=../service/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("><b>").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</b></a>");
                        }
                        Str.append("<br>Contact: <a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("><b>").append(crs.getString("contact")).append("</b></a>");
                        Str.append("<br>Mobile: ").append(crs.getString("contact_mobile1"));
                        Str.append("<br></td>\n</tr>\n");
                    }
                    Str.append("</table></td></div>");
                } else {
                    Str.append("<td valign=top align=center></td></tr>");
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

    public String PopulateModel() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT model_id, model_name"
                    + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
                    + " ORDER BY model_name";
            CachedRowSet crs = processQuery(StrSql, 0);
            
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("model_id")).append("");
                Str.append(ArrSelectdrop(crs.getInt("model_id"), item_model_ids));
                Str.append(">").append(crs.getString("model_name")).append("</option>\n");
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
