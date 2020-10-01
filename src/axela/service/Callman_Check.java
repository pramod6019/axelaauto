package axela.service;
//aJIt 11th March, 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Callman_Check extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String branch_id = "0";
    public String txt_search = "";
    public String StrSql = "";
    public String StrHTML = "";
    public String StrSearch = "";
    public String dr_emp_id = "0";
    public String dr_branch_id = "0";
    public String veh_id = "";
    public String veh_details = "";
    public String todays_appt = "";
    public String calls_overdue = "";
    public String service_due = "";
    public String ticket_due = "";
    public String para = "";
    public String search_param = "";
    public String branch_emp = "";
    DecimalFormat df = new DecimalFormat("0.00");

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CheckSession(request, response);
        HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
         emp_id = CNumeric(GetSession("emp_id", request));
        branch_id = CNumeric(GetSession("emp_branch_id", request));
        txt_search = PadQuotes(request.getParameter("txt_search"));
        dr_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
        dr_branch_id = PadQuotes(request.getParameter("branch_id"));
        veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
        veh_details = PadQuotes(request.getParameter("veh_details"));
        para = PadQuotes(request.getParameter("para"));
        branch_emp = PadQuotes(request.getParameter("branch_emp"));
        search_param = CNumeric(PadQuotes(request.getParameter("search_param")));

        if (txt_search.contains("-")) {
            txt_search = txt_search.replace("-", "");
        }

        if (txt_search.contains(" ")) {
            txt_search = txt_search.replaceAll("\\s+", "");
        }

        if (!veh_id.equals("0") && veh_details.equals("yes")) {
            StrHTML = "<font color=\"red\"><b>Vehicle Selected Successfully!</b></font>";
        } else if (branch_emp.equals("yes")) {
            StrHTML = new Callman().PopulateExecutive(dr_branch_id, search_param);
        } else if (!dr_branch_id.equals("0") && txt_search.equals("") && para.equals("overdue")) {
            if (search_param.equals("1")) {
                StrHTML = GetCalls(para);
            } else if (search_param.equals("2")) {
                StrHTML = GetCallFollowups(para);
            } else if (search_param.equals("3")) {
                StrHTML = GetAppts(para);
            } else if (search_param.equals("4")) {
                StrHTML = GetJobCards(para);
            } else if (search_param.equals("5")) {
                StrHTML = GetTickets(para);
            }
        } else if (!dr_branch_id.equals("0") && !txt_search.equals("") && para.equals("date")) {
            if (isValidDateFormatShort(txt_search)) {
                if (search_param.equals("1")) {//Calls
                    StrHTML = GetCalls(para);
                } else if (search_param.equals("2")) {//Call Follow-ups
                    StrHTML = GetCallFollowups(para);
                } else if (search_param.equals("3")) {//Bookings
                    StrHTML = GetAppts(para);
                } else if (search_param.equals("4")) {//Job Cards
                    StrHTML = GetJobCards(para);
                } else if (search_param.equals("5")) {//Tickets
                    StrHTML = GetTickets(para);
                } else if (search_param.equals("6")) {//Pickups
                    StrHTML = GetPickups(para);
                } else if (search_param.equals("7")) {//Courtesy Cars
                    StrHTML = GetCourtesyCars(para);
                }
            } else {
                StrHTML = "<br/><br/><br/><br/><br/><font color=\"red\"><b>Enter valid date!</b></font>";
            }
        } else if (!txt_search.equals("") && para.equals("vehicle")) {
            if (txt_search.length() < 3) {
                StrHTML = "<br/><br/><br/><br/><br/><font color=\"red\"><b>Enter at least 3 characters to search!</b></font>";
            } else {
                StrHTML = SearchVehicle();
            }
        } else if (!dr_branch_id.equals("0") && para.equals("service_due")) {
            StrHTML = GetDueService();
        } else if (dr_branch_id.equals("0") && !para.equals("vehicle") && !para.equals("contact")) {
            StrHTML = "<br/><br/><br/><br/><br/><font color=\"red\"><b>Select Branch!</b></font>";
        }
    }
    }

    public String GetCalls(String search) {
        StringBuilder Str = new StringBuilder();
        try {
            int count = 0;
            String bgcolor = "";
            if (search.equals("date")) {
                StrSearch = " AND SUBSTR(call_time, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND call_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND call_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT calltype_name, call_id, call_customer_voice, call_followup_time,"
                    + " contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contact_name,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2, call_time,"
                    + " customer_name, customer_id,"
                    + " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_chassis_no, '') AS veh_chassis_no,"
                    + " COALESCE(veh_engine_no, '') AS veh_engine_no, call_call_id, call_emp_id,"
                    + " COALESCE(veh_reg_no, '') AS veh_reg_no, COALESCE(veh_id, 0) AS veh_id,"
                    + " COALESCE(CONCAT(call_emp.emp_name, ' (', call_emp.emp_ref_no, ')'), '') AS call_emp_name,"
                    + " title_desc, COALESCE(driver.emp_id, 0) AS driver_id,"
                    + " COALESCE(booking_time, '') AS booking_time,"
                    + " COALESCE(CONCAT(driver.emp_name, ' (', driver.emp_ref_no, ')'), '') AS driver_emp_name,"
                    + " COALESCE(pickup_id, 0) AS pickup_id, COALESCE(pickup_time_from, '') AS pickup_time_from,"
                    + " COALESCE(pickup_time_to, '') AS pickup_time_to,"
                    + " COALESCE(courtesycar_id, 0) AS courtesycar_id, COALESCE(courtesycar_time_from, '') AS courtesycar_time_from,"
                    + " COALESCE(courtesycar_time_to, '') AS courtesycar_time_to, COALESCE(courtesyveh_name, '') AS courtesy_car"
                    + " FROM " + compdb(comp_id) + "axela_service_call"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp call_emp ON call_emp.emp_id = call_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_booking ON booking_call_id = call_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_pickup ON pickup_booking_id = booking_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp driver ON driver.emp_id = pickup_emp_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_courtesy_car ON courtesycar_booking_id = booking_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY call_id"
                    + " ORDER BY call_time ASC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"11\">Calls</th>\n</tr>\n");
            if (crs.isBeforeFirst() && !search.equals("overdue")) {
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Call Type</th>\n");
                Str.append("<th>Customer Voice</th>\n");
                Str.append("<th>Call Time</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Appt.</th>\n");   
                Str.append("<th>Pickup Driver</th>\n");
                Str.append("<th>Courtesy Vehicle</th>\n");
                Str.append("<th>Executive</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    if (crs.getString("call_call_id").equals("0")
                            && crs.getLong("call_followup_time") < Long.parseLong(ToLongDate(kknow()))) {
                        bgcolor = " bgcolor=\"pink\"";
                    } else {
                        bgcolor = " bgcolor=\"white\"";
                    }

                    Str.append("<tr>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(count).append("</td>\n");
                    Str.append("<td valign=\"top\" align=\"center\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../service/call-list.jsp?call_id=").append(crs.getString("call_id")).append("\">").append(crs.getString("call_id")).append("</a></td>");
                    Str.append("<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
                    Str.append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a>");
                    if (!crs.getString("contact_name").equals("")) {
                        Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=");
                        Str.append(crs.getString("contact_id")).append("\">");
                        Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contact_name")).append("</a>");
                    }

                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">").append(crs.getString("calltype_name"));
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append(">").append(crs.getString("call_customer_voice"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">").append(strToLongDate(crs.getString("call_time")));
                    if (!crs.getString("call_followup_time").equals("")) {
                        Str.append("<br/>Follow-up: ").append(strToLongDate(crs.getString("call_followup_time")));
                    }

                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append("><a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");

                    Str.append("</td>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">");
                    if (!crs.getString("booking_time").equals("")) {
                        Str.append(strToLongDate(crs.getString("booking_time")));
                    }
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    if (!crs.getString("driver_id").equals("0")) {
                        Str.append("<a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\">");
                        Str.append(crs.getString("driver_emp_name")).append("</a>");
                        Str.append("<br/>").append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "1"));
                    }

                    Str.append("&nbsp;</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../service/courtesy-list.jsp?courtesycar_id=").append(crs.getString("courtesycar_id")).append("\">");
                    Str.append(crs.getString("courtesy_car")).append("</a>").append("<br/>");
                    if (!crs.getString("courtesycar_time_from").equals("") && !crs.getString("courtesycar_time_to").equals("")) {
                        Str.append(strToLongDate(crs.getString("courtesycar_time_from"))).append(" - ").append(strToLongDate(crs.getString("courtesycar_time_to")));
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append("><a href=\"../portal/executive-summary.jsp?emp_id=");
                    Str.append(crs.getString("call_emp_id")).append("\">").append(crs.getString("call_emp_name")).append("</a></td>\n</tr>");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Calls found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetPickups(String search) {
        StringBuilder Str = new StringBuilder();
        try {
            int count = 0;
            String bgcolor = "";
            if (search.equals("date")) {
                StrSearch = " AND SUBSTR(pickup_time, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND pickup_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND pickup_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contact_name,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
                    + " customer_name, customer_id, title_desc, driver.emp_id AS driver_id,"
                    + " CONCAT(driver.emp_name, ' (', driver.emp_ref_no, ')') AS driver_emp_name,"
                    + " pickup_id, pickup_time, pickup_time_from, pickup_time_to, pickuptype_name,"
                    + " pickup_location_id, pickup_add, pickup_landmark,"
                    + " CONCAT(location_name, ' (' , location_code, ')') AS locationname,"
                    + " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no"
                    + " FROM " + compdb(comp_id) + "axela_service_pickup"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_pickup_type ON pickuptype_id = pickup_pickuptype_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp driver ON driver.emp_id = pickup_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = pickup_location_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_booking ON booking_id = pickup_booking_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_call ON call_id = booking_call_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
                    + " WHERE 1 = 1 " + StrSearch + ""
                    + " GROUP BY pickup_id"
                    + " ORDER BY pickup_time ASC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"10\">Pickup</th>\n</tr>\n");
            if (crs.isBeforeFirst() && !search.equals("overdue")) {
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Pickup Time</th>\n");
                Str.append("<th>Pickup Type</th>\n");
                Str.append("<th>Vehicle</th>\n");
                Str.append("<th>Location</th>\n");
                Str.append("<th>Pickup Driver</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    bgcolor = " bgcolor=\"white\"";

                    Str.append("<tr>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(count).append("</td>\n");
                    Str.append("<td valign=\"top\" align=\"center\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\">");
                    Str.append(crs.getString("pickup_id")).append("</a></td>\n");
                    Str.append("<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
                    Str.append(crs.getString("customer_id")).append("\">").append(crs.getString("customer_name")).append("</a>");
                    if (!crs.getString("contact_name").equals("")) {
                        Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=");
                        Str.append(crs.getString("contact_id")).append("\">");
                        Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contact_name")).append("</a>");
                    }

                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">");
                        Str.append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">");
                        Str.append(crs.getString("contact_email2")).append("</a>");
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">Pickup Time: ");
                    Str.append(strToLongDate(crs.getString("pickup_time"))).append("<br/>");
                    Str.append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "1"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">").append(crs.getString("pickuptype_name"));
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    if (!crs.getString("veh_id").equals("0")) {
                        Str.append("Vehicle ID: ").append(crs.getString("veh_id")).append("<br/>Reg. No.: ");
                        Str.append("<a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                        Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
                    }
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">Location: ").append(crs.getString("locationname"));
                    Str.append("<br/>Address: ").append(crs.getString("pickup_add"));
                    Str.append("<br/>Landmark: ").append(crs.getString("pickup_landmark"));
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\">");
                    Str.append(crs.getString("driver_emp_name")).append("</a></td>\n");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Pickup found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetCourtesyCars(String search) {
        StringBuilder Str = new StringBuilder();
        try {
            int count = 0;
            if (search.equals("date")) {
                StrSearch = " AND SUBSTR(courtesycar_time_from, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND courtesycar_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contactname,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2, customer_name,"
                    + " customer_id, title_desc, courtesycar_id, courtesycar_time_from, courtesycar_time_to,"
                    + " courtesycar_landmark, courtesycar_add, courtesyveh_name"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = courtesycar_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesycar_branch_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY courtesycar_id"
                    + " ORDER BY courtesycar_time_from ASC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"10\">Courtesy Car</th>\n</tr>\n");
            if (crs.isBeforeFirst() && !search.equals("overdue")) {
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>From</th>\n");
                Str.append("<th>To</th>\n");
                Str.append("<th>Location</th>\n");
                Str.append("<th>Courtesy Car</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    Str.append("<tr>\n<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
                    Str.append("<td valign=\"top\" align=\"center\">");
                    Str.append("<a href=\"../service/courtesy-list.jsp?courtesycar_id=").append(crs.getString("courtesycar_id")).append("\">");
                    Str.append(crs.getString("courtesycar_id")).append("</a></td>\n");
                    Str.append("<td valign=\"top\" align=\"left\">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=");
                    Str.append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a>");
                    if (!crs.getString("contactname").equals("")) {
                        Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=");
                        Str.append(crs.getString("contact_id")).append("\">");
                        Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contactname")).append("</a>");
                    }

                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("courtesycar_time_from")));
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("courtesycar_time_to")));
                    Str.append("</td>\n<td valign=\"top\">Address: ").append(crs.getString("courtesycar_add"));
                    Str.append("<br/>Landmark: ").append(crs.getString("courtesycar_landmark")).append("</td>\n");
                    Str.append("<td valign=\"top\">");
                    Str.append("<a href=\"../service/courtesy-list.jsp?courtesycar_id=").append(crs.getString("courtesycar_id")).append("\">");
                    Str.append(crs.getString("courtesyveh_name")).append("</a>");
                    Str.append("</td>\n</tr>\n");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Courtesy Car found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetCallFollowups(String search) {
        StringBuilder Str = new StringBuilder();
        int count = 0;
        String bgcolor = "";
        try {
            if (search.equals("overdue")) {
                StrSearch = " AND call_followup_time != ''"
                        + " AND call_call_id = 0"
                        + " AND call_followup_time < " + ToLongDate(kknow());
            } else if (search.equals("date")) {
                StrSearch = " AND SUBSTR(call_followup_time, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND call_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND call_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT call_contact_id, customer_id, customer_name, contact_fname,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
                    + " contact_lname, title_desc, call_followup_time, COALESCE(veh_id, 0) AS veh_id,"
                    + " COALESCE(veh_reg_no, '') AS veh_reg_no, call_trigger, call_customer_voice,"
                    + " call_id, call_call_id, call_emp_id, emp_name, emp_ref_no,"
                    + " COALESCE(model_name, '') AS model_name, COALESCE(item_name, '') AS item_name,"
                    + " COALESCE(item_id, 0) AS item_id, COALESCE(model_id, 0) AS model_id"
                    + " FROM " + compdb(comp_id) + "axela_service_call"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY call_id"
                    + " ORDER BY call_followup_time ASC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"11\">Call Follow-up</th>\n</tr>\n");
            if (crs.isBeforeFirst()) {
                Str.append("<tr>\n<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Customer Voice</th>\n");
                Str.append("<th>Follow-up Time</th>\n");
                Str.append("<th>Level</th>\n");
                Str.append("<th>Model</th>\n");
                Str.append("<th>Item</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Executive</th>\n");
                Str.append("<th>Action</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    if (crs.getString("call_call_id").equals("0")
                            && crs.getLong("call_followup_time") < Long.parseLong(ToLongDate(kknow()))) {
                        bgcolor = " bgcolor=\"pink\"";
                    } else {
                        bgcolor = " bgcolor=\"white\"";
                    }

                    Str.append("<tr>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(count);
                    Str.append("</td>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(crs.getString("call_id"));
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a><br/>");
                    Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("call_contact_id")).append("\">");
                    Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contact_fname")).append(" ").append(crs.getString("contact_lname")).append("</a>");
                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                    }
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append(">").append(crs.getString("call_customer_voice"));
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append("><a href=\"../service/call-list.jsp?call_id=").append(crs.getString("call_id")).append("\">");
                    Str.append(strToLongDate(crs.getString("call_followup_time"))).append("</a>");
                    Str.append("</td>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(crs.getString("call_trigger"));
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append("><a href=\"../inventory/item-model.jsp?model_id=").append(crs.getString("model_id")).append("\">");
                    Str.append(crs.getString("model_name"));
                    Str.append("</a></td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append("><a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
                    Str.append(crs.getString("item_name"));
                    Str.append("</a></td>\n<td valign=\"top\"").append(bgcolor).append("><a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("call_emp_id")).append("\" target=\"_blank\">");
                    Str.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")</a>");
                    Str.append("</td>\n<td valign=\"top\"").append(bgcolor).append(">");
                    if (crs.getString("call_call_id").equals("0")) {
                        Str.append("<a href=\"../service/call-update.jsp?add=yes&call_id=").append(crs.getString("call_id")).append("#tabs-7\" target=\"_blank\">Call Now</a>");
                    }
                    Str.append("</td>\n</tr>\n");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Call Follow-up found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetAppts(String search) {
        StringBuilder Str = new StringBuilder();
        try {
            int count = 0;
            if (search.equals("date")) {
                StrSearch = " AND SUBSTR(booking_time, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND booking_service_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND booking_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT call_contact_id, customer_id, customer_name,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
                    + " contact_lname, title_desc, COALESCE(veh_id, 0) AS veh_id,"
                    + " COALESCE(veh_reg_no, '') AS veh_reg_no, customer_id,"
                    + " call_id, booking_service_emp_id, exe.emp_name AS emp_name,"
                    + " exe.emp_ref_no AS emp_ref_no, booking_time,"
                    + " COALESCE(model_name, '') AS model_name,"
                    + " COALESCE(item_name, '') AS item_name,"
                    + " COALESCE(item_id, 0) AS item_id, contact_fname,"
                    + " COALESCE(model_id, 0) AS model_id, booking_id,"
                    + " COALESCE(driver.emp_name, '') AS driver_name,"
                    + " COALESCE(driver.emp_id, 0) AS driver_id,"
                    + " COALESCE(courtesyveh_name, '') AS courtesy_car,"
                    + " COALESCE(courtesyveh_id, '') AS courtesyveh_id,"
                    + " COALESCE(driver.emp_ref_no, '') AS driver_ref_no,"
                    + " COALESCE(pickup_id, 0) AS pickup_id,"
                    + " COALESCE(pickup_time_from, '') AS pickup_time_from,"
                    + " COALESCE(pickup_time_to, '') AS pickup_time_to,"
                    + " COALESCE(courtesycar_id, 0) AS courtesycar_id,"
                    + " COALESCE(courtesycar_time_from, '') AS courtesycar_time_from,"
                    + " COALESCE(courtesycar_time_to, '') AS courtesycar_time_to"
                    + " FROM " + compdb(comp_id) + "axela_service_booking"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = booking_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_call ON call_id = booking_call_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = booking_service_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_pickup ON pickup_booking_id = booking_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp driver ON driver.emp_id = pickup_emp_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_courtesy_car ON courtesycar_booking_id = booking_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY booking_id"
                    + " ORDER BY booking_time ASC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"11\">Bookings</th>\n</tr>\n");
            if (crs.isBeforeFirst() && !search.equals("overdue")) {
                Str.append("<tr>\n<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Appt. Time</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Model</th>\n");
                Str.append("<th>Item</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Pickup</th>\n");
                Str.append("<th>Courtesy Car</th>\n");
                Str.append("<th>Service Advisor</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    Str.append("<tr>\n<td valign=\"top\" align=\"center\">").append(count);
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"><a id=\"x\" href=\"javascript:PopulateApptDash(").append(crs.getString("booking_id")).append(");\">");
                    Str.append(crs.getString("booking_id")).append("</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">");
                    Str.append(strToLongDate(crs.getString("booking_time")));
                    Str.append("</td>\n<td valign=\"top\"><a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a><br/>");
                    Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("call_contact_id")).append("\">");
                    Str.append(crs.getString("title_desc")).append(" ");
                    Str.append(crs.getString("contact_fname")).append(" ").append(crs.getString("contact_lname")).append("</a>");
                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                    }

                    Str.append("</td>\n<td align=\"left\" valign=\"top\"><a href=\"../inventory/item-model.jsp?model_id=").append(crs.getString("model_id")).append("\">");
                    Str.append(crs.getString("model_name"));
                    Str.append("</a></td>\n<td align=\"left\" valign=\"top\"><a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
                    Str.append(crs.getString("item_name"));
                    Str.append("</a></td>\n<td valign=\"top\"><a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
                    Str.append("</td>\n<td valign=\"top\">");
                    if (!crs.getString("driver_id").equals("0")) {
                        Str.append("<a href=\"../service/pickup-list.jsp?pickup_id=").append(crs.getString("pickup_id")).append("\">");
                        Str.append(crs.getString("driver_name")).append(" (").append(crs.getString("driver_ref_no")).append(")</a>");
                        Str.append("<br/>").append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "1"));
                    }

                    Str.append("&nbsp;</td>\n<td valign=\"top\">");
                    Str.append("<a href=\"../service/courtesy-list.jsp?courtesycar_id=").append(crs.getString("courtesycar_id")).append("\">");
                    Str.append(crs.getString("courtesy_car")).append("</a>").append("<br/>");
                    if (!crs.getString("courtesycar_time_from").equals("") && !crs.getString("courtesycar_time_to").equals("")) {
                        Str.append(strToLongDate(crs.getString("courtesycar_time_from"))).append(" - ").append(strToLongDate(crs.getString("courtesycar_time_to")));
                    }

                    Str.append("</td>\n<td valign=\"top\">");
                    Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("booking_service_emp_id")).append("\" target=\"_blank\">");
                    Str.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
                    if (!crs.getString("veh_id").equals("0")) {
                        Str.append("<a href=\"../service/jobcard-list.jsp?jc_veh_id=").append(crs.getString("veh_id")).append("\">List Job Cards</a><br/>");
                    }

                    if (crs.getString("veh_id").equals("0")) {
                        Str.append("<a href=\"../service/jobcard-veh-search.jsp\">Add Job Card</a><br/>");
                    } else {
                        Str.append("<a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Job Card</a><br/>");
                    }

                    if (!crs.getString("veh_id").equals("0")) {
                        Str.append("<a href=\"../service/call-list.jsp?call_veh_id=").append(crs.getString("veh_id")).append("\">List Calls</a><br/>");
                    } else {
                        Str.append("<a href=\"../service/call-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">List Calls</a><br/>");
                    }
                    Str.append("<a href=\"../service/call-update.jsp?add=yes&call_id=").append(crs.getString("call_id")).append("\">Add Call</a>");
                    Str.append("</td>\n</tr>\n");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Booking(s) found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetJobCards(String search) {
        int count = 0;
        String bgcolor = "";
        StringBuilder Str = new StringBuilder();
        try {
            if (search.equals("overdue")) {
                StrSearch = " AND jc_time_promised < " + ToLongDate(kknow()) + ""
                        + " AND jc_time_out = ''";
            } else if (search.equals("date")) {
                StrSearch = " AND SUBSTR(jc_time_promised, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND jc_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND jc_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT jc_id, jc_branch_id, CONCAT('JC', branch_code, jc_no) AS jc_no, jc_time_in,"
                    + " jc_contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
                    + " item_id, model_id, jc_cust_voice, jc_netamt, jc_totaltax, jc_grandtotal, jc_ro_no,"
                    + " jc_auth, jc_active, jc_time_out, jc_time_promised, jcstage_name, model_name,"
                    + " item_name, veh_id, veh_reg_no, priorityjc_name, jccat_name, jctype_name, jc_title,"
                    + " customer_id, customer_name, contact_id, jc_time_ready, jc_priority_trigger,"
                    + " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
                    + " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
                    + " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id, jc_stage_trigger"
                    + " FROM " + compdb(comp_id) + "axela_service_jc"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY jc_id"
                    + " ORDER BY jc_id DESC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"17\">Job Cards</th></tr>\n");
            if (crs.isBeforeFirst()) {
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>No.</th>\n");
                Str.append("<th>Job Card</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Time</th>\n");
                Str.append("<th>Voice</th>\n");
                Str.append("<th>Item</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Stage</th>\n");
                Str.append("<th>Priority</th>\n");
                Str.append("<th>Type</th>\n");
                Str.append("<th>Category</th>\n");
                Str.append("<th>Amount</th>\n");
                Str.append("<th>Service Advisor</th>\n");
                Str.append("<th>Action</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    if (crs.getLong("jc_time_promised") < Long.parseLong(ToLongDate(kknow()))
                            && crs.getString("jc_time_out").equals("")) {
                        bgcolor = " bgcolor=\"pink\"";
                    } else {
                        bgcolor = " bgcolor=\"white\"";
                    }

                    Str.append("<tr>\n<td align=\"center\" valign=\"top\"").append(bgcolor).append(">").append(count);
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(">");
                    Str.append("<a href=\"jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("\">").append(crs.getString("jc_id")).append("</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(">").append(crs.getString("jc_no"));
                    if (!crs.getString("jc_ro_no").equals("")) {
                        Str.append("<br/>RO No: ").append(crs.getString("jc_ro_no"));
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a href=\"javascript:PopulateJCDash(").append(crs.getString("jc_id")).append(");\">").append(crs.getString("jc_title")).append("</a>");
                    if (!crs.getString("jc_ro_no").equals("")) {
                        Str.append("<br/>RO. No: ").append(crs.getString("jc_ro_no"));
                    }

                    if (crs.getString("jc_active").equals("0")) {
                        Str.append("<br/><font color=\"red\"><b>[Inactive]</b></font>");
                    }

                    if (crs.getString("jc_auth").equals("1")) {
                        Str.append("<br/><font color=\"red\">[Authorized]</font>");
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a>");
                    Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
                    Str.append(crs.getString("contact_name")).append("</a>");
                    if (!crs.getString("contact_mobile1").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                    }

                    if (!crs.getString("contact_mobile2").equals("")) {
                        Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                    }

                    if (!crs.getString("contact_email1").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                    }

                    if (!crs.getString("contact_email2").equals("")) {
                        Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>Time In: ").append(strToLongDate(crs.getString("jc_time_in")));
                    Str.append("<br/>Promised: ").append(strToLongDate(crs.getString("jc_time_promised")));
                    if (!crs.getString("jc_time_ready").equals("")) {
                        Str.append("<br/>Ready: ").append(strToLongDate(crs.getString("jc_time_ready")));
                    }

                    if (!crs.getString("jc_time_out").equals("")) {
                        Str.append("<br/>Time Out: ").append(strToLongDate(crs.getString("jc_time_out")));
                    }
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append(">").append(crs.getString("jc_cust_voice"));
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
                    Str.append(crs.getString("item_name"));
                    Str.append("</a></td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("jcstage_name"));
                    if (crs.getString("jc_stage_trigger").equals("5")) {
                        Str.append("<br/><font color=\"red\">Level: ").append(crs.getString("jc_stage_trigger")).append("</font>");
                    } else {
                        Str.append("<br/>Level: ").append(crs.getString("jc_stage_trigger"));
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("priorityjc_name"));
                    if (crs.getString("jc_priority_trigger").equals("5")) {
                        Str.append("<br/><font color=\"red\">Level: ").append(crs.getString("jc_priority_trigger")).append("</font>");
                    } else {
                        Str.append("<br/>Level: ").append(crs.getString("jc_priority_trigger"));
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">").append(crs.getString("jctype_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">").append(crs.getString("jccat_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"right\"").append(bgcolor).append(" nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_netamt"))));
                    if (!crs.getString("jc_totaltax").equals("0")) {
                        Str.append("<br/>Service Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_totaltax"))));
                    }
                    Str.append("<br/><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_grandtotal")))).append("</b>");
                    Str.append("<br/><br/></td>\n");
                    Str.append("<td valign=\"top\"").append(bgcolor).append(">");
                    Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
                    Str.append(crs.getString("emp_name")).append("</a></td>\n");
                    Str.append("<td valign=\"top\" align=\"left\" nowrap>");
                    Str.append("<a href=\"jobcard-update.jsp?update=yes&jc_id=").append(crs.getString("jc_id")).append("\">Update Job Card</a>");
                    Str.append("<br/><a href=\"jobcard-authorize.jsp?jc_id=").append(crs.getString("jc_id")).append("\">Authorize</a>");
                    Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Add Ticket</a>");
                    Str.append("<br/><a href=\"../invoice/invoice-update.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Add Invoice</a>");
                    if (!crs.getString("jc_time_out").equals("")) {
                        if (Long.parseLong(crs.getString("jc_time_out")) <= Long.parseLong(ToLongDate(kknow()))) {
                            Str.append("<br/><a href=\"../service/jobcard-cust-feedback.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Customer Feedback</a>");
                        }
                    }
                    Str.append("<br/><a href=\"jobcard-print-pdf.jsp?jc_id=").append(crs.getString("jc_id")).append("&target=").append(Math.random()).append("\" target=_blank>Print Job Card</a>");
                    Str.append("<br/><a href=\"jobcard-email.jsp?jc_id=").append(crs.getString("jc_id")).append("\">Email Job Card</a>");
                    Str.append("<br/><a href=\"gate-pass-print-pdf.jsp?jc_id=").append(crs.getString("jc_id")).append("&target=").append(Math.random()).append("\" target=_blank>Print Gate Pass</a>");
                    Str.append("</td>\n</tr>\n");
                }
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=\"red\"><b>No Job Card(s) found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetTickets(String search) {
        StringBuilder Str = new StringBuilder();
        String bgcolor = "";
        int count = 0;
        long closetime = 0;
        try {
            if (search.equals("overdue")) {
                StrSearch = " AND ticket_closed_time = ''";
            } else if (search.equals("date")) {
                StrSearch = " AND SUBSTR(ticket_due_time, 1, 8) = " + ConvertShortDateToStr(txt_search).substring(0, 8);
            }

            if (!dr_emp_id.equals("0")) {
                StrSearch += " AND ticket_emp_id = " + dr_emp_id + "";
            }

            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND customer_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT ticket_id, ticket_subject, ticket_desc, ticket_customer_id, ticket_contact_id,"
                    + " COALESCE(contact_mobile1, '') AS contact_mobile1, COALESCE(contact_mobile2, '') AS contact_mobile2,"
                    + " COALESCE(contact_email1, '') AS contact_email1, COALESCE(contact_email2, '') AS contact_email2,"
                    + " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
                    + " COALESCE(ticketcat_name, '') AS ticketcat_name, COALESCE(tickettype_name, '') AS tickettype_name,"
                    + " priorityticket_name, priorityticket_id, ticket_trigger,"
                    + " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
                    + " ticketstatus_id, ticketstatus_name, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
                    + " ticket_dept_name, ticket_report_time, ticket_due_time, ticket_closed_time"
                    + " FROM " + compdb(comp_id) + "axela_service_ticket"
                    + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id"
                    + " WHERE 1 = 1" + StrSearch + ""
                    + " GROUP BY ticket_id"
                    + " ORDER BY ticket_id DESC"
                    + " LIMIT 100";
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<table cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\" border=\"1\">\n");
            Str.append("<tr>\n<th colspan=\"13\">Tickets</th>\n</tr>\n");
            if (crs.isBeforeFirst()) {
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Ticket</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Report Time</th>\n");
                Str.append("<th>Due Time</th>\n");
                Str.append("<th>Status</th>\n");
                Str.append("<th>Priority</th>\n");
                Str.append("<th>Level</th>\n");
                Str.append("<th>Category</th>\n");
                Str.append("<th>Type</th>\n");
                Str.append("<th>Department</th>\n");
                Str.append("<th>Executive</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    if (crs.getString("ticket_closed_time").equals("")) {
                        bgcolor = " bgcolor=\"pink\"";
                    } else {
                        bgcolor = " bgcolor=\"white\"";
                    }

                    Str.append("<tr>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(">").append(count).append("</td>\n");
                    Str.append("<td valign=\"top\" align=\"center\"").append(bgcolor).append(">").append(crs.getString("ticket_id")).append("</td>\n");
                    Str.append("<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    Str.append("<a href=\"ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("\">");
                    Str.append(crs.getString("ticket_subject")).append("</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>");
                    if (!crs.getString("contact_name").equals("")) {
                        Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                        Str.append(crs.getString("customer_name")).append("</a><br/>");
                        Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("ticket_contact_id")).append("\">");
                        Str.append(crs.getString("contact_name")).append("</a>");
                        if (!crs.getString("contact_mobile1").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                        }

                        if (!crs.getString("contact_mobile2").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                        }

                        if (!crs.getString("contact_email1").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                        }

                        if (!crs.getString("contact_email2").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                        }
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(" nowrap>").append(strToLongDate(crs.getString("ticket_report_time")));
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(" nowrap>");
                    if (!crs.getString("ticket_closed_time").equals("")) {
                        closetime = crs.getLong("ticket_closed_time");
                    } else {
                        closetime = Long.parseLong(ToLongDate(kknow()));
                    }

                    if (closetime >= Long.parseLong(crs.getString("ticket_due_time"))) {
                        Str.append("<font color=\"#ff0000\">").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
                    } else {
                        Str.append("<font color=\"blue\">").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(">");
                    if (crs.getString("ticketstatus_id").equals("3")) {
                        Str.append("<font color=\"red\">").append(crs.getString("ticketstatus_name")).append("</font>");
                    } else {
                        Str.append(crs.getString("ticketstatus_name"));
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("priorityticket_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"center\"").append(bgcolor).append(" nowrap>").append(crs.getString("ticket_trigger"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("ticketcat_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("tickettype_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>").append(crs.getString("ticket_dept_name"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\"").append(bgcolor).append(" nowrap>");
                    Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">");
                    Str.append(crs.getString("emp_name")).append("</a>");
                    Str.append("</td>\n</tr>\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<tr>\n<td align=\"center\" height=\"100\"><font color=red><b>No Ticket(s) found!</b></font></td>\n</tr>\n");
            }
            crs.close();
            Str.append("</table>\n");
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String SearchVehicle() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT veh_id, veh_variant_id, veh_chassis_no, veh_engine_no, veh_reg_no, veh_sale_date,"
                    + " COALESCE(model_name, '') AS model_name, COALESCE(customer_id, 0) AS customer_id,"
                    + " IF(COALESCE(item_code, '') != '', CONCAT(item_name, ' (', item_code, ')'), COALESCE(item_name,'')) AS item_name,"
                    + " COALESCE(customer_name, '') AS customer_name, veh_lastservice,"
                    + " COALESCE(contact_id, 0) AS contact_id, COALESCE(call_id, 0) AS call_id,"
                    + " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
                    + " COALESCE(contact_mobile1, '') AS contact_mobile1, COALESCE(contact_mobile2, '') AS contact_mobile2,"
                    + " COALESCE(contact_email1, '') AS contact_email1, COALESCE(contact_email2, '') AS contact_email2,"
                    + " COALESCE(veh_kms, 0) AS veh_kms, COALESCE(veh_cal_kms, 0) AS veh_cal_kms,"
                    + " COALESCE(item_id, 0) AS item_id, COALESCE(model_id, 0) AS model_id, veh_iacs,"
                    + " veh_service_duedate, veh_service_duekms, COALESCE(emp_id, 0) AS veh_emp_id"
                    + " FROM " + compdb(comp_id) + "axela_service_veh"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_service_call ON call_veh_id = veh_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_emp_id"
                    + " WHERE veh_id LIKE '%" + txt_search + "%'"
                    + " OR veh_reg_no LIKE '%" + txt_search.replace(" ", "") + "%'"
                    + " OR veh_chassis_no LIKE '%" + txt_search + "%'"
                    + " OR veh_engine_no LIKE '%" + txt_search + "%'"
                    + " OR CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) LIKE '%" + txt_search + "%'"
                    + " OR contact_mobile1 LIKE '%" + txt_search + "%'"
                    + " OR contact_mobile2 LIKE '%" + txt_search + "%'"
                    + " OR REPLACE(contact_mobile1, '-', '') LIKE '%" + txt_search.replace(" ", "") + "%'"
                    + " OR REPLACE(contact_mobile2, '-', '') LIKE '%" + txt_search.replace(" ", "") + "%'"
                    + " OR contact_phone1 LIKE '%" + txt_search + "%'"
                    + " OR contact_phone2 LIKE '%" + txt_search + "%'"
                    + " OR REPLACE(contact_phone1, '-', '') LIKE '%" + txt_search.replace(" ", "") + "%'"
                    + " OR REPLACE(contact_phone2, '-', '') LIKE '%" + txt_search.replace(" ", "") + "%'"
                    + " OR contact_email1 LIKE '%" + txt_search + "%'"
                    + " OR contact_email2 LIKE '%" + txt_search + "%'"
                    + " OR customer_name LIKE '%" + txt_search + "%'"
                    + " OR customer_mobile1 LIKE '%" + txt_search + "%'"
                    + " OR customer_mobile2 LIKE '%" + txt_search + "%'"
                    + " OR customer_phone1 LIKE '%" + txt_search + "%'"
                    + " OR customer_phone2 LIKE '%" + txt_search + "%'"
                    + " OR customer_email1 LIKE '%" + txt_search + "%'"
                    + " OR customer_email2 LIKE '%" + txt_search + "%'"
                    + " GROUP BY veh_id"
                    + " ORDER BY veh_id"
                    + " LIMIT 25";
            CachedRowSet crs = processQuery(StrSql, 0);

            int count = 0;
            if (crs.isBeforeFirst()) {
                Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Item</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Chassis Number.</th>\n");
                Str.append("<th>Sale Date</th>\n");
                Str.append("<th>Kms</th>\n");
                Str.append("<th>Service</th>\n");
                Str.append("<th>Advisor</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    Str.append("<tr>\n<td align=\"center\" valign=\"top\">").append(count);
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">");
                    Str.append("<a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(crs.getString("veh_id")).append("</a>");
                    Str.append("</td>\n<td valign=\"top\" align=\"left\">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a>");
                    if (!crs.getString("contact_name").equals("")) {
                        Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
                        Str.append(crs.getString("contact_name")).append("</a>");
                        if (!crs.getString("contact_mobile1").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                        }

                        if (!crs.getString("contact_mobile2").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                        }

                        if (!crs.getString("contact_email1").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                        }

                        if (!crs.getString("contact_email2").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                        }
                    }
                    Str.append("</td>\n<td align=\"left\" valign=\"top\"><a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
                    Str.append(crs.getString("item_name"));
                    Str.append("</a></td>\n<td align=\"center\" valign=\"top\"><a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
                    Str.append("</td>\n<td align=\"center\" valign=\"top\">");
                    Str.append("<a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">").append(crs.getString("veh_chassis_no")).append("</a>");
                    Str.append("<br/>Engine No.: <a id=\"x\" href=\"javascript:PopulateVehicleDash(").append(crs.getString("veh_id")).append(");\">").append(crs.getString("veh_engine_no")).append("</a>");
                    if (crs.getString("veh_iacs").equals("1")) {
                        Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
                    }
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToShortDate(crs.getString("veh_sale_date")));
                    Str.append("</td>\n<td align=\"right\" valign=\"top\">Last: ").append(IndFormat(crs.getString("veh_kms")));
                    Str.append("<br/>Calculated: ").append(IndFormat(crs.getString("veh_cal_kms")));
                    Str.append("</td>\n<td align=\"right\" valign=\"top\" nowrap>Last Service: ");
                    if (!crs.getString("veh_lastservice").equals("")) {
                        Str.append(strToShortDate(crs.getString("veh_lastservice")));
                    }

                    Str.append("<br/>Next Service Date: ");
                    if (!crs.getString("veh_service_duedate").equals("")) {
                        Str.append(strToShortDate(crs.getString("veh_service_duedate")));
                    }

                    Str.append("<br/>Next Service Kms: ");
                    if (!crs.getString("veh_service_duedate").equals("")) {
                        Str.append(IndFormat(crs.getString("veh_service_duekms")));
                    }

                    Str.append("</td>\n<td valign=\"top\" align=\"center\">");
                    if (!crs.getString("veh_emp_id").equals("0")) {
                        Str.append(Exename(comp_id, crs.getInt("veh_emp_id")));
                    }

                    Str.append("&nbsp;</td>\n<td valign=\"top\" align=\"left\" nowrap>");
                    Str.append("<a href=\"vehicle-update.jsp?update=yes&veh_id=").append(crs.getString("veh_id")).append("\">Update Vehicle</a>");
                    Str.append("<br/><a href=\"../service/kms-list.jsp?vehkms_veh_id=").append(crs.getString("veh_id")).append("\">List Kms</a>");
                    Str.append("<br/><a href=\"../service/kms-update.jsp?add=yes&vehkms_veh_id=").append(crs.getString("veh_id")).append("\">Add Kms</a>");
                    Str.append("<br/><a href=\"../service/jobcard-list.jsp?jc_veh_id=").append(crs.getString("veh_id")).append("\">List Job Cards</a>");
                    Str.append("<br/><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Job Card</a>");
                    Str.append("<br/><a href=\"../service/jobcard-quickadd.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Quick Add Job Card</a>");
                    Str.append("<br/><a href=\"../customer/customer-contact-update.jsp?Add=yes&customer_id=").append(crs.getString("customer_id")).append("\">Add New Contact</a>");
                    Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Ticket</a>");
                    Str.append("<br/><a href=\"../service/call-list.jsp?call_veh_id=").append(crs.getString("veh_id")).append("\">List Calls</a>");
                    if (!crs.getString("call_id").equals("0")) {
                        Str.append("<br/><a href=\"../service/call-update.jsp?add=yes&call_id=").append(crs.getString("call_id")).append("\">Add Call</a>");
                    } else {
                        Str.append("<br/><a href=\"../service/call-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Call</a>");
                    }
                    Str.append("<br/><a href=\"../service/pickup-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Pickup</a>");
                    Str.append("</td>\n</tr>\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<b><font color=\"#ff0000\">No Vehicle Found!</font></b>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public String GetDueService() {
        StringBuilder Str = new StringBuilder();
        try {
            if (!dr_branch_id.equals("0")) {
                StrSearch += " AND customer_branch_id = " + dr_branch_id + "";
            }

            StrSql = "SELECT veh_id, veh_variant_id, model_name,"
            		+ " veh_chassis_no, veh_id, model_id,"
                    + " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
                    + " veh_engine_no, veh_reg_no, veh_sale_date, customer_id, customer_name,"
                    + " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
                    + " veh_so_id, COALESCE(branch_id, 0) AS branch_id, veh_kms, veh_lastservice,"
                    + " contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_id,"
                    + " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, item_id"
                    + " FROM " + compdb(comp_id) + "axela_service_veh"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
                    + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
                    + " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
                    + " WHERE veh_service_duekms <= veh_kms"
                    + " OR veh_service_duedate <= " + ToLongDate(kknow()) + StrSearch + ""
                    + " GROUP BY veh_id"
                    + " ORDER BY veh_lastservice DESC";
            CachedRowSet crs = processQuery(StrSql, 0);

            int count = 0;
            if (crs.isBeforeFirst()) {
                Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
                Str.append("<tr>\n<th colspan=\"14\">Due Services</th>\n</tr>\n");
                Str.append("<tr align=\"center\">\n");
                Str.append("<th>#</th>\n");
                Str.append("<th>ID</th>\n");
                Str.append("<th>Contact</th>\n");
                Str.append("<th>Model</th>\n");
                Str.append("<th>Item</th>\n");
                Str.append("<th>Reg. No.</th>\n");
                Str.append("<th>Chassis Number</th>\n");
                Str.append("<th>Engine No.</th>\n");
                Str.append("<th>Kms</th>\n");
                Str.append("<th>Last Service Date</th>\n");
                Str.append("<th>Actions</th>\n");
                Str.append("</tr>\n");
                while (crs.next()) {
                    count++;
                    Str.append("<tr>\n<td align=\"center\" valign=\"top\">").append(count);
                    Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("veh_id"));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\">");
                    Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
                    Str.append(crs.getString("customer_name")).append("</a>");
                    if (!crs.getString("contact_name").equals("")) {
                        Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
                        Str.append(crs.getString("contact_name")).append("</a>");
                        if (!crs.getString("contact_mobile1").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
                        }

                        if (!crs.getString("contact_mobile2").equals("")) {
                            Str.append("<br/>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
                        }

                        if (!crs.getString("contact_email1").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a>");
                        }

                        if (!crs.getString("contact_email2").equals("")) {
                            Str.append("<br/><a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a>");
                        }
                    }

                    Str.append("</td>\n<td align=\"left\" valign=\"top\">");
                    Str.append("<a href=\"../inventory/item-model.jsp?model_id=").append(crs.getString("model_id")).append("\">");
                    Str.append(crs.getString("model_name"));
                    Str.append("</a></td>\n<td align=\"left\" valign=\"top\">");
                    Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
                    Str.append(crs.getString("item_name"));
                    Str.append("</a></td>\n<td align=\"left\" valign=\"top\">");
                    Str.append("<a href=\"vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
                    Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");  
                    Str.append("</td>\n<td align=\"center\" valign=\"top\">").append(crs.getString("veh_chassis_no"));
                    Str.append("</td>\n<td align=\"center\" valign=\"top\">").append(crs.getString("veh_engine_no"));
                    Str.append("</td>\n<td align=\"right\" valign=\"top\">").append(IndFormat(crs.getString("veh_kms")));
                    Str.append("</td>\n<td align=\"right\" valign=\"top\">").append(strToShortDate(crs.getString("veh_lastservice")));
                    Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
                    Str.append("<a href=\"vehicle-update.jsp?update=yes&veh_id=").append(crs.getString("veh_id")).append("\">Update Vehicle</a>");
                    Str.append("<br/><a href=\"../service/kms-list.jsp?vehkms_veh_id=").append(crs.getString("veh_id")).append("\">List Kms</a>");
                    Str.append("<br/><a href=\"../service/kms-update.jsp?add=yes&vehkms_veh_id=").append(crs.getString("veh_id")).append("\">Add Kms</a>");
                    Str.append("<br/><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Job Card</a>");
                    Str.append("<br/><a href=\"../service/call-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Call</a>");
                    Str.append("<br/><a href=\"../service/pickup-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Pickup</a>");
                    Str.append("</td>\n</tr>\n");
                }
                Str.append("</table>\n");
            } else {
                Str.append("<b><font color=\"#ff0000\">No Vehicle found!</font></b>");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);

    }
    
}
