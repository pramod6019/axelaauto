package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.service.Ticket_Dash;
import cloudify.connect.Connect;

public class App_Ticket_Dash_Followup extends Connect {

	public String msg = "";
	public String msg1 = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String ticket_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String followupHTML = "";
	public String enquiry_status_id = "0";
	public String followup_emp_id = "0";
	public String followup_followupstatus_id = "0";
	public String followup_followuptype_id = "0";
	public String tickettrans_id = "0";
	public String tickettrans_followup = "";
	public String tickettrans_nextfollowup_date = "";
	public String tickettrans_nextfollowup_time = "";
	public String ticket_ticketstatus_id = "0";
	public String tickettrans_private = "";
	public String tickettrans_entry_id = "0";
	public String tickettrans_entry_date = "";
	public String tickettrans_modified_id = "0";
	public String tickettrans_modified_date = "";
	public String tickettrans_contact_entry_id = "0";
	public String ticket_reopened_emp_id = "0";
	public String add = "";
	public String addB = "";

	public String followup_id = "0";
	public String temp_time = "";
	public String status = "";
	public String day = "";
	public String month = "";
	public String year = "";
	public String comp_id = "0";
	public String emp_uuid = "", access = "";
	public String followupdesc_id = "0", emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_id = "1";
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				access = ReturnPerm(comp_id, "emp_ticket_add", request);
				if (access.equals("0")) {
					response.sendRedirect("callurlapp-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				msg = PadQuotes(request.getParameter("msg"));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				followup_id = CNumeric(PadQuotes(request.getParameter("followup_id")));

				followupHTML = ListFollowup();
				if ("yes".equals(add)) {
					status = "Add";
					ticket_ticketstatus_id = CNumeric(ExecuteQuery("SELECT ticket_ticketstatus_id"
							+ " FROM " + compdb(comp_id) + "axela_service_ticket"
							+ " WHERE 1 = 1"
							+ " AND ticket_id = " + ticket_id));
					if (ticket_id.equals("0") || !isNumeric(ticket_id)) {
						response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access Denied!"));
					}
					if (!"yes".equals(addB)) {
					} else {
						if (ReturnPerm(comp_id, "emp_ticket_add", request).equals("0")) {
							response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
						}
						GetValues(request);
						tickettrans_entry_id = emp_id;
						tickettrans_entry_date = ToLongDate(kknow());
						if (ticket_ticketstatus_id.equals("3")) {
							msg = msg + "<br>Cannot add Follow Up!";
						} else {
							AddFields();
						}
						if (msg.equals("")) {
							response.sendRedirect(response.encodeRedirectURL("callurlapp-ticket-dash-followup.jsp?ticket_id=" + ticket_id + "&msg=Follow-up added successfully!"));
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListFollowup() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT ticket_id, tickettrans_id, ticket_customer_id, tickettrans_followup, tickettrans_nextfollowup_time, tickettrans_private, "
				+ " (CASE WHEN tickettrans_private='1' THEN 'Yes' ELSE 'No' END) AS private, "
				+ " tickettrans_entry_id, "
				+ " COALESCE(CONCAT(e.emp_name, ' (', e.emp_ref_no, ')'),'') AS entry_by, "
				+ " tickettrans_entry_date, "
				+ " tickettrans_modified_id, "
				+ " COALESCE(CONCAT(m.emp_name, ' (', m.emp_ref_no, ')'),'') AS modified_by, "
				+ " tickettrans_modified_date, "
				+ " COALESCE(customer_name,'') AS customer_name, tickettrans_contact_entry_id, customer_id ";
		SqlJoin = "FROM " + compdb(comp_id) + "axela_service_ticket_trans "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket on ticket_id=tickettrans_ticket_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as e ON e.emp_id=tickettrans_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as m ON m.emp_id=tickettrans_modified_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id=ticket_customer_id "
				+ " WHERE tickettrans_ticket_id='" + ticket_id + "'";
		StrSql = StrSql + SqlJoin;
		StrSql = StrSql + " ORDER BY tickettrans_id DESC";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<div>").append("<div class=\"col-md-12 col-xs-12\" style=\"border: 1px solid #8E44AD; \">").append("<br>").append("<b>");
					Str.append("<b>").append("Follow-up Description:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("tickettrans_followup"))
							.append("</span><br><br>");
					Str.append("<b>").append("Next Follow-up Time:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(strToLongDate(crs.getString("tickettrans_nextfollowup_time")))
							.append("</span><br><br>");
					if (crs.getString("private").equals("Yes")) {
						Str.append("<font color=red>PRIVATE</font><br><br>");
					}
					Str.append("<b>").append("Entry By:").append("</b>&nbsp;").append("<span style=\"color: #000\">").append(crs.getString("entry_by")).append("</span><br><br>").append("<b>")
							.append("Entry Date:")
							.append("</b>&nbsp;").append("<span style=\"color: #000\">").append(strToLongDate(crs.getString("tickettrans_entry_date"))).append("</span><br><br>");
					Str.append("</div>").append("</div>");
				}
			} else {
				msg1 = "<br><br><font color=red><b>No follow-up found!</b></font>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		// SOP("str===" + Str.toString());
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request) {

		tickettrans_followup = PadQuotes(request.getParameter("txt_tickettrans_followup"));
		tickettrans_nextfollowup_date = PadQuotes(request.getParameter("txt_tickettrans_nextfollowup_date"));
		tickettrans_nextfollowup_time = PadQuotes(request.getParameter("txt_tickettrans_nextfollowup_time"));
		tickettrans_private = CheckBoxValue(PadQuotes(request.getParameter("chk_private")));
		temp_time = PadQuotes(request.getParameter("txt_tickettrans_nextfollowup_time"));
		if (!tickettrans_nextfollowup_date.equals("") && !temp_time.equals("")) {
			day = tickettrans_nextfollowup_date.substring(0, 2);
			month = tickettrans_nextfollowup_date.substring(3, 5);
			year = tickettrans_nextfollowup_date.substring(6, 10);
			tickettrans_nextfollowup_date = day + "/" + month + "/" + year;
			tickettrans_nextfollowup_time = tickettrans_nextfollowup_date + " " + temp_time;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (tickettrans_followup.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (tickettrans_nextfollowup_time.equals("")) {
			msg = msg + "<br>Next Follow-up Time!";
		} else {
			if (!isValidDateFormatLong(tickettrans_nextfollowup_time)) {
				msg = msg + "<br>Enter Valid Next Follow-up Time!";
			}
		}
	}

	public void AddFields() throws Exception {
		try {
			Ticket_Dash ticketdash = new Ticket_Dash();
			ticketdash.emp_id = emp_id;
			ticketdash.comp_id = comp_id;
			ticketdash.ticket_id = ticket_id;
			ticketdash.tickettrans_entry_id = emp_id;
			ticketdash.ticket_ticketstatus_id = ticket_ticketstatus_id;
			ticketdash.tickettrans_entry_date = ToLongDate(kknow());
			ticketdash.tickettrans_id = tickettrans_id;
			ticketdash.tickettrans_followup = tickettrans_followup;
			ticketdash.tickettrans_nextfollowup_time = tickettrans_nextfollowup_time;
			ticketdash.tickettrans_private = tickettrans_private;
			ticketdash.AddFields();
			msg = ticketdash.msg;
			ticket_id = ticketdash.ticket_id;

		} catch (Exception ex) {
			SOPError("Axelaauto-APP====" + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name " + "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_type " + "WHERE 1=1 " + "order by followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("followuptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), followup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP=== " + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowupDesc(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT feedbacktype_id, feedbacktype_name " + "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type " + "WHERE 1=1 " + "ORDER BY feedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("feedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("feedbacktype_id"), followupdesc_id));
				Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-APP=== " + this.getClass().getName());
			SOPError("Axelaauto-APP=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
