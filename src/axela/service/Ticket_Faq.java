package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Ticket_Faq extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=../service/ticket-faq-list.jsp?smart=yes>Click here to List FAQs</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));

				CheckPerm(comp_id, "emp_service_faq_access", request, response);
				smart = PadQuotes(request.getParameter("smart"));
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access",
				// request, response);
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(ticketfaq_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(ticketfaq_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					// SOP("StrSearch=="+StrSearch);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("ticketfaqstrsql", StrSearch, request);
						StrHTML = TicketFaqSummary(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

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
	}

	protected void CheckForm() {
		msg = "";
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
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String TicketFaqSummary(HttpServletRequest request) {
		String count = "";
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<b>FAQ Summary</b><br>");
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
			Str.append("<thead>\n");
			Str.append("<tr align=center>\n");
			Str.append("<th>&nbsp;</th>\n");
			Str.append("<th>Total</th>\n");
			Str.append("<th>Active</th>\n");
			Str.append("<th>Inactive</th>\n");
			Str.append("</tr>");
			Str.append("</thead>");
			Str.append("<tbody>");
			Str.append("<tr align=center>\n");
			Str.append("<td align=center>FAQs</td>\n");
			count = ExecuteQuery("select count(ticketfaq_id) from " + compdb(comp_id) + "axela_service_ticket_faq where 1 = 1" + StrSearch);
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("select count(ticketfaq_id) from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_active = '1'" + StrSearch);
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			count = ExecuteQuery("select count(ticketfaq_id) from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_active = '0'" + StrSearch);
			Str.append("<td align=center><b>").append(count).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</tbody>");
			Str.append("</table>");
			Str.append("</div>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
