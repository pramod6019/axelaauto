package axela.service;
// 29 may 2014
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Update_DueTime extends Connect {

    public String StrSql = "";
    public String comp_id = "0";
    public String StrHTML = "";
    public int count = 0;
    public String ticket_id = "0";
    public String duetime = "";
    double duehrs = 0;
    public String ticket_trigger1_hrs = "";
    public String ticket_trigger2_hrs = "";
    public String ticket_trigger3_hrs = "";
    public String ticket_trigger4_hrs = "";
    public String ticket_trigger5_hrs = "";
    public String ticket_dept_trigger1_hrs = "";
    public String ticket_dept_trigger2_hrs = "";
    public String ticket_dept_trigger3_hrs = "";
    public String ticket_dept_trigger4_hrs = "";
    public String ticket_dept_trigger5_hrs = "";
    public String ticket_report_time = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            ticket_id = PadQuotes(request.getParameter("ticket_id"));
            if (ticket_id.equals("") || !isNumeric(ticket_id)) {
                ticket_id = "0";
            }
//            SOP("ticket_id = " + ticket_id);

            StrSql = "SELECT ticket_id, ticket_report_time, ticket_dept_starttime, ticket_dept_endtime,"
                    + " COALESCE(customer_branch_id, 0) as branch_id, ticket_dept_duehrs, ticket_dept_business_hrs,"
                    + " ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu, ticket_dept_fri, ticket_dept_sat,"
                    + " ticket_dept_trigger1_hrs, ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
                    + " ticket_dept_trigger5_hrs"
                    + " FROM " + compdb(comp_id) + "axela_service_ticket"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
                    + " left join " + compdb(comp_id) + "axela_customer_contact on contact_id =ticket_contact_id"
                    + " left join " + compdb(comp_id) + "axela_customer on customer_id =contact_customer_id"
                    + " left join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
                    + " WHERE 1=1"
                    //                    + " AND ticket_id = " + ticket_id + "";
                    //                    + " AND ticket_id > " + ticket_id + ""
                    + " ORDER BY ticket_id"
                    + " LIMIT 250";
//            SOP("StrSql = " + StrSql);
            CachedRowSet crs = processQuery(StrSql, 0);

            StringBuilder Str = new StringBuilder();
            while (crs.next()) {
                ticket_report_time = crs.getString("ticket_report_time");
                ticket_dept_trigger1_hrs = crs.getString("ticket_dept_trigger1_hrs");
                ticket_dept_trigger2_hrs = crs.getString("ticket_dept_trigger2_hrs");
                ticket_dept_trigger3_hrs = crs.getString("ticket_dept_trigger3_hrs");
                ticket_dept_trigger4_hrs = crs.getString("ticket_dept_trigger4_hrs");
                ticket_dept_trigger5_hrs = crs.getString("ticket_dept_trigger5_hrs");

                if (crs.getString("ticket_dept_business_hrs").equals("1")) {
//                    SOP("case 1****");

                    ArrayList public_holidate = publicHolidays(comp_id, crs.getString("ticket_report_time"), crs.getString("branch_id"));

                    if (!crs.getString("ticket_dept_duehrs").equals("0")) {
                        duetime = DueTime(ticket_report_time, crs.getString("ticket_dept_duehrs"),
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        duetime = ticket_report_time;
                    }

                    //*** start- ticket_trigger_hrs
                    //---------------1--------------------
                    if (!ticket_dept_trigger1_hrs.equals("0")) {
                        ticket_trigger1_hrs = DueTime(ticket_report_time, ticket_dept_trigger1_hrs,
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        ticket_trigger1_hrs = "";
                    }

                    //---------------2--------------------
                    if (!ticket_dept_trigger2_hrs.equals("0")) {
                        ticket_trigger2_hrs = DueTime(ticket_report_time, ticket_dept_trigger2_hrs,
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        ticket_trigger2_hrs = "";
                    }

                    //---------------3--------------------
                    if (!ticket_dept_trigger3_hrs.equals("0")) {
                        ticket_trigger3_hrs = DueTime(ticket_report_time, ticket_dept_trigger3_hrs,
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        ticket_trigger3_hrs = "";
                    }

                    //---------------4--------------------
                    if (!ticket_dept_trigger4_hrs.equals("0")) {
                        ticket_trigger4_hrs = DueTime(ticket_report_time, ticket_dept_trigger4_hrs,
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        ticket_trigger4_hrs = "";
                    }

                    //---------------5--------------------
                    if (!ticket_dept_trigger5_hrs.equals("0")) {
                        ticket_trigger5_hrs = DueTime(ticket_report_time, ticket_dept_trigger5_hrs,
                                crs.getDouble("ticket_dept_starttime"), crs.getDouble("ticket_dept_endtime"),
                                crs.getString("ticket_dept_sun"), crs.getString("ticket_dept_mon"),
                                crs.getString("ticket_dept_tue"), crs.getString("ticket_dept_wed"),
                                crs.getString("ticket_dept_thu"), crs.getString("ticket_dept_fri"),
                                crs.getString("ticket_dept_sat"), public_holidate);
                    } else {
                        ticket_trigger5_hrs = "";
                    }
                    //*** eof- ticket_trigger_hrs

                } else {
//                    SOP("case 2****");
                    if (!crs.getString("ticket_dept_duehrs").equals("0")) {
                        duetime = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, crs.getDouble("ticket_dept_duehrs"), 0));
                    } else {
                        duetime = ticket_report_time;
                    }
                    //---------------1--------------------
                    if (!ticket_dept_trigger1_hrs.equals("0")) {
                        ticket_trigger1_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Integer.parseInt(ticket_dept_trigger1_hrs), 0));
                    } else {
                        ticket_trigger1_hrs = "";
                    }
                    //---------------2--------------------
                    if (!ticket_dept_trigger2_hrs.equals("0")) {
                        ticket_trigger2_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Integer.parseInt(ticket_dept_trigger2_hrs), 0));
                    } else {
                        ticket_trigger2_hrs = "";
                    }
                    //---------------3--------------------
                    if (!ticket_dept_trigger3_hrs.equals("0")) {
                        ticket_trigger3_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Integer.parseInt(ticket_dept_trigger3_hrs), 0));
                    } else {
                        ticket_trigger3_hrs = "";
                    }
                    //---------------4--------------------
                    if (!ticket_dept_trigger4_hrs.equals("0")) {
                        ticket_trigger4_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Integer.parseInt(ticket_dept_trigger4_hrs), 0));
                    } else {
                        ticket_trigger4_hrs = "";
                    }
                    //---------------5--------------------
                    if (!ticket_dept_trigger5_hrs.equals("0")) {
                        ticket_trigger5_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Integer.parseInt(ticket_dept_trigger5_hrs), 0));
                    } else {
                        ticket_trigger5_hrs = "";
                    }

                }
//                SOP("duetime = " + duetime);
                StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket"
                        + " SET"
                        + " ticket_due_time = '" + duetime + "',"
                        + " ticket_trigger1_hrs = '" + ticket_trigger1_hrs + "',"
                        + " ticket_trigger2_hrs = '" + ticket_trigger2_hrs + "',"
                        + " ticket_trigger3_hrs = '" + ticket_trigger3_hrs + "',"
                        + " ticket_trigger4_hrs = '" + ticket_trigger4_hrs + "',"
                        + " ticket_trigger5_hrs = '" + ticket_trigger5_hrs + "'"
                        + " WHERE ticket_id = " + crs.getString("ticket_id");
//                SOP("StrSql = " + StrSqlBreaker(StrSql));
                updateQuery(StrSql);
                Str.append("<br>Ticket ID ").append(crs.getString("ticket_id")).append(" updated!");
            }
            crs.close();
            StrHTML = Str.toString();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
