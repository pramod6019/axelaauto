package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class TicketDash_Check_Search extends Connect {

    public String StrSearch = "";
    public String StrHTML = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String starttime = "";
    public String endtime = "";
    public String ticketstatus_id = "";
    public String ticket_dept_id = "";
    public String tickettoday = "", closedtickettoday = "", openticket = "", ticket_dept = "";
    public String day = "", week = "", month = "", qtr = "";
    public String ExeAccess = "";
    public String BranchAccess = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            ExeAccess = GetSession("ExeAccess", request);
            BranchAccess = GetSession("BranchAccess", request);
            emp_id = (PadQuotes(request.getParameter("emp_id")));
            starttime = PadQuotes(request.getParameter("starttime"));
            endtime = PadQuotes(request.getParameter("endtime"));
            tickettoday = PadQuotes(request.getParameter("tickettoday"));
            closedtickettoday = PadQuotes(request.getParameter("closedtickettoday"));
            openticket = PadQuotes(request.getParameter("openticket"));
            ticket_dept = PadQuotes(request.getParameter("ticket_dept"));
            day = PadQuotes(request.getParameter("day"));
            week = PadQuotes(request.getParameter("week"));
            month = PadQuotes(request.getParameter("month"));
            qtr = PadQuotes(request.getParameter("qtr"));
            ticketstatus_id = CNumeric(PadQuotes(request.getParameter("ticketstatus_id")));
            ticket_dept_id = CNumeric(PadQuotes(request.getParameter("ticket_dept_id")));
// BranchAccess.replace("branch_id", "jc_branch_id") +
            StrSearch = ExeAccess.replace("emp_id", "ticket_emp_id");

            if (!emp_id.equals("")) {
                StrSearch = StrSearch + " and ticket_emp_id in (" + emp_id + ")";
            }
///////// tickets for today///////////
            if (!tickettoday.equals("")) {
                if (!starttime.equals("")) {
                    StrSearch += " and substring(ticket_report_time,1,8)= '" + starttime.substring(0, 8) + "'";
                }
                if (!ticketstatus_id.equals("0")) {
                    StrSearch += " and ticket_ticketstatus_id =" + ticketstatus_id;
                }
            }
///////// closed tickets for today///////////
            if (!closedtickettoday.equals("")) {
                if (!starttime.equals("")) {
                    StrSearch += " and substring(ticket_closed_time,1,8)= '" + starttime.substring(0, 8) + "'";
                }
                if (!ticketstatus_id.equals("0")) {
                    StrSearch += " and ticket_ticketstatus_id =" + ticketstatus_id;
                }
            }
///////// closed tickets///////////
            if (!openticket.equals("")) {
                StrSearch += " and ticketstatus_id !=3";
                if (!ticketstatus_id.equals("0")) {
                    StrSearch += " and ticket_ticketstatus_id =" + ticketstatus_id;
                }
            }
///////// Departments///////////
            if (!ticket_dept.equals("")) {
                StrSearch += " and ticketstatus_id !=3";
                if (!ticket_dept_id.equals("0")) {
                    StrSearch += " and ticket_ticket_dept_id =" + ticket_dept_id;
                }
            }

            if (day.equals("day")) {
                if (!starttime.equals("")) {
                    StrSearch += " and substring(ticket_report_time,1,8)= '" + starttime + "'";
                }
            }
//            SOP("StrSearch = " + StrSearch);

            if (week.equals("week")) {
                if (!starttime.equals("") && !endtime.equals("")) {
                    StrSearch += " and substring(ticket_report_time,1,8)>='" + endtime + "' and substring(ticket_report_time,1,8)<'" + starttime + "'";
                }
            }

            if (month.equals("month")) {
                if (!starttime.equals("") && !endtime.equals("")) {
                    StrSearch += " and substring(ticket_report_time,1,8)>='" + endtime + "' and substring(ticket_report_time,1,8)<'" + starttime + "'";
                }
            }
            if (qtr.equals("qtr")) {
                if (!starttime.equals("") && !endtime.equals("")) {
                    StrSearch += " and (substring(ticket_report_time,1,4)='" + starttime + "' and substring(ticket_report_time,5,2) " + endtime + ")";
                }
            }

            if (!StrSearch.equals("")) {  
                SetSession("ticketstrsql", StrSearch, request);
                response.sendRedirect(response.encodeRedirectURL("../service/ticket-list.jsp?smart=yes"));
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
}
