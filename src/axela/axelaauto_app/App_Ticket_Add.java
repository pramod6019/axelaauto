package axela.axelaauto_app;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.service.Ticket_Add;
import cloudify.connect.Connect;

public class App_Ticket_Add extends Connect {

	public String emp_role_id = "0";
	public String ticket_ticketcat_id = "0";
	public String ticket_tickettype_id = "0";
	public String ticket_id = "0";
	public String ticket_branch_id = "0", ticket_parent_id = "0";
	public String ticket_customer_id = "0";
	public String customer_name = "";
	public String ticket_ticketstatus_id = "0";
	public String ticket_subject = "";
	public String ticket_desc = "";
	public String ticket_read = "";
	public String ticket_notes = "";
	public String ticket_ticketsource_id = "0";
	public String ticket_priorityticket_id = "0";
	public String ticket_ticket_dept_id = "0";
	public String ticket_cc = "";
	public String ticket_emp_id = "0";
	public String ticket_due_time = "";

	public String ticket_enquiry_id = "0";
	public String ticket_preowned_id = "0";
	public String ticket_so_id = "0";
	public String ticket_crm_id = "0";
	public String ticket_preowned_crm_id = "0";
	public String ticket_veh_id = "0";
	public String ticket_jc_id = "0";
	public String ticket_jcpsf_id = "0";
	// /
	public String ticket_report_time = "";
	public String ticket_trigger = "";
	public String ticket_entry_id = "0";
	public String ticket_entry_date = "";
	public String ticket_modified_id = "0";
	public String ticket_modified_date = "";
	public String branch_brand_id = "0";
	public String add = "";
	public String addB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String SqlCount = "";
	public String StrHTML = "";
	public String customer_branch_id = "0";
	public String ticket_contact_id = "0";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String config_email_enable = "";
	public String config_admin_email = "";
	public String config_ticket_new_email_enable = "";
	public String config_ticket_new_email_format = "";
	public String config_ticket_new_email_sub = "";
	public String config_ticket_new_email_exe_enable = "";
	public String config_ticket_new_email_exe_format = "";
	public String config_ticket_new_email_exe_sub = "";
	public String config_ticket_new_sms_enable = "";
	public String config_ticket_new_sms_format = "";
	public String config_ticket_new_sms_exe_format = "";
	public String comp_sms_enable = "";
	public String comp_email_enable = "";
	public String config_sms_enable = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String send_contact_email = "";
	public String emp_id = "0";
	public String emp_uuid = "";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String branch_ticket_email = "";
	public String emp_email_formail = "";
	public String entryemp_email = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String branch_id = "", param = "", branch_type = "";

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
			SetSession("comp_id", comp_id, request);
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ticket_emp_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_emp_id")));
				ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
				if (!ticket_parent_id.equals("0")) {
					PopulateFields(response);
				}
				PopulateConfigDetails(comp_id);
				if (!"yes".equals(addB)) {
					ticket_emp_id = emp_id;
					ticket_trigger = "0";
					ticket_report_time = ToLongDate(kknow());
				} else {
					GetValues(request, response);
					ticket_entry_id = emp_id;
					ticket_entry_date = ToLongDate(kknow());
					ticket_report_time = ToLongDate(kknow());
					PopulateConfigDetails(comp_id);
					AddFields();
					if (!msg.equals("")) {
						msg = "Error! " + msg;
					} else {
						response.sendRedirect(response.encodeRedirectURL("callurlapp-ticket-list.jsp?ticket_id=" + ticket_id + "&msg=Ticket added successfully!"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			ticket_branch_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_branch_id")));;
			ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
			ticket_subject = PadQuotes(request.getParameter("txt_ticket_subject"));
			ticket_desc = PadQuotes(request.getParameter("txt_ticket_desc"));
			ticket_ticketcat_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketcat_id")));
			ticket_tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_tickettype_id")));
			ticket_ticketsource_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketsource_id")));
			ticket_priorityticket_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_priorityticket_id")));
			ticket_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept_id")));
			if (emp_id.equals(ticket_emp_id)) {
				ticket_read = "1";
			} else {
				ticket_read = "0";
			}
			ticket_trigger = "0";
			ticket_cc = (PadQuotes(request.getParameter("txt_ticket_cc")));
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields() throws Exception {
		Ticket_Add ticket = new Ticket_Add();
		ticket.emp_id = emp_id;
		ticket.comp_id = comp_id;
		ticket.emp_role_id = emp_role_id;
		// config details
		ticket.branch_ticket_email = branch_ticket_email;
		ticket.config_service_ticket_cat = config_service_ticket_cat;
		ticket.config_service_ticket_type = config_service_ticket_type;
		ticket.config_email_enable = config_email_enable;
		ticket.config_admin_email = config_admin_email;
		ticket.config_sms_enable = config_sms_enable;
		ticket.comp_sms_enable = comp_sms_enable;
		ticket.comp_email_enable = comp_email_enable;
		ticket.config_ticket_new_email_enable = config_ticket_new_email_enable;
		ticket.config_ticket_new_email_format = config_ticket_new_email_format;
		ticket.config_ticket_new_email_sub = config_ticket_new_email_sub;
		ticket.config_ticket_new_email_exe_format = config_ticket_new_email_exe_format;
		ticket.config_ticket_new_email_exe_sub = config_ticket_new_email_exe_sub;
		ticket.config_ticket_new_sms_enable = config_ticket_new_sms_enable;
		ticket.config_ticket_new_sms_format = config_ticket_new_sms_format;
		ticket.config_ticket_new_sms_exe_format = config_ticket_new_sms_exe_format;
		ticket.emp_name = emp_name;
		ticket.emp_email1 = emp_email1;
		ticket.emp_email2 = emp_email2;
		ticket.entryemp_email = entryemp_email;
		ticket.emp_email_formail = emp_email_formail;
		ticket.branch_ticket_email = branch_ticket_email;
		ticket.branch_ticket_email = branch_ticket_email;
		ticket.emp_mobile1 = emp_mobile1;
		ticket.emp_mobile2 = emp_mobile2;

		// Ticket details
		ticket.ticket_parent_id = ticket_parent_id;
		ticket.ticket_branch_id = ticket_branch_id;
		ticket.ticket_customer_id = ticket_customer_id;
		ticket.ticket_contact_id = ticket_contact_id;
		ticket.ticket_enquiry_id = ticket_enquiry_id;
		ticket.ticket_preowned_id = ticket_preowned_id;
		ticket.ticket_so_id = ticket_so_id;
		ticket.ticket_crm_id = ticket_crm_id;
		ticket.ticket_preowned_crm_id = ticket_preowned_crm_id;
		ticket.ticket_veh_id = ticket_veh_id;
		ticket.ticket_jc_id = ticket_jc_id;
		ticket.ticket_jcpsf_id = ticket_jcpsf_id;
		ticket.ticket_ticketsource_id = ticket_ticketsource_id;
		ticket.ticket_report_time = ticket_report_time;
		ticket.ticket_ticketstatus_id = "1";
		ticket.ticket_ticketcat_id = ticket_ticketcat_id;
		ticket.ticket_tickettype_id = ticket_tickettype_id;
		ticket.ticket_priorityticket_id = ticket_priorityticket_id;
		ticket.ticket_cc = ticket_cc;
		ticket.ticket_ticket_dept_id = ticket_ticket_dept_id;
		ticket.ticket_subject = ticket_desc;
		ticket.ticket_desc = ticket_desc;
		ticket.ticket_emp_id = ticket_emp_id;
		ticket.ticket_due_time = ticket_due_time;
		ticket.ticket_read = ticket_read;
		ticket.ticket_entry_id = ticket_entry_id;
		ticket.ticket_entry_date = ticket_entry_date;
		ticket.ticket_report_time = ticket_report_time;
		ticket.AddFields(comp_id);
		ticket_id = ticket.ticket_id;

	}

	public String PopulateSourceType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketsource_id, ticketsource_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_source "
					+ " where 1 = 1 "
					+ " group by ticketsource_id"
					+ " order by ticketsource_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketsource_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketsource_id"), ticket_ticketsource_id));
				Str.append(">").append(crs.getString("ticketsource_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " where 1 = 1 "
					+ " group by priorityticket_id"
					+ " order by priorityticket_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityticket_id"), ticket_priorityticket_id));
				Str.append(">").append(crs.getString("priorityticket_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " where 1 = 1 "
					+ " group by ticket_dept_id"
					+ " order by ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_ticket_dept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAllExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_service_ticket ON ticket_emp_id = " +
					// compdb(comp_id) + "axela_emp.emp_id"
					// + " AND ticket_ticketstatus_id != 3"
					+ " WHERE emp_active = '1'"
					+ " AND emp_ticket_owner = '1'"
					// + ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql PopulateAllExecutive= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketCategory(String depertment_id, String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT ticketcat_id, ticketcat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " WHERE 1 = 1 ";
			if (!depertment_id.equals("0")) {
				StrSql += " AND ticketcat_ticketdept_id = " + depertment_id;
			}
			StrSql += " GROUP BY ticketcat_id "
					+ " ORDER BY ticketcat_name";

			// SOP("StrSqll== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_ticket_ticketcat_id\" class=\"form-control\">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketcat_id"), ticket_ticketcat_id));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			Str.append("</select>");

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " WHERE 1 = 1 "
					+ " GROUP BY tickettype_id "
					+ " ORDER BY tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), ticket_tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_ticket.*, "
					+ " COALESCE(concat(title_desc,' ',contact_fname,' ',contact_lname),'') AS contact_name, "
					+ " COALESCE(customer_name,'') AS customer_name,"
					+ " COALESCE(customer_branch_id, 0) AS customer_branch_id "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id " + BranchAccess
					+ " LEFT join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " WHERE ticket_id=" + ticket_parent_id + ExeAccess;
			// SOP("StrSql==populate=="+StrSqlBreaker(StrSql));
			CachedRowSet rs = processQuery(StrSql, 0);
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					ticket_id = rs.getString("ticket_id");
					customer_branch_id = rs.getString("customer_branch_id");
					ticket_contact_id = rs.getString("ticket_contact_id");
					ticket_customer_id = rs.getString("ticket_customer_id");
					ticket_parent_id = rs.getString("ticket_parent_id");
					customer_name = rs.getString("customer_name");
					contact_name = rs.getString("contact_name");
					ticket_ticketstatus_id = rs.getString("ticket_ticketstatus_id");
					ticket_ticketcat_id = rs.getString("ticket_ticketcat_id");
					ticket_tickettype_id = rs.getString("ticket_tickettype_id");
					ticket_subject = rs.getString("ticket_subject");
					ticket_desc = rs.getString("ticket_desc");
					ticket_emp_id = rs.getString("ticket_emp_id");
					ticket_read = rs.getString("ticket_read");
					if (ticket_read.equals("0") && emp_id.equals(ticket_emp_id)) {
						StrSql = "Update " + compdb(comp_id) + "axela_service_ticket set ticket_read='1' where ticket_id=" + ticket_id + "";
						updateQuery(StrSql);
					}
					ticket_notes = rs.getString("ticket_notes");
					ticket_ticketsource_id = rs.getString("ticket_ticketsource_id");
					ticket_priorityticket_id = rs.getString("ticket_priorityticket_id");
					ticket_due_time = rs.getString("ticket_due_time");
				}
			} else {
				msg = "<br><br>No Ticket found!";
			}
			rs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails(String comp_id) {
		StrSql = " SELECT config_service_ticket_cat, config_service_ticket_type,"
				+ " config_email_enable, config_admin_email, config_sms_enable, comp_email_enable, comp_sms_enable,"
				+ " config_ticket_new_email_enable, config_ticket_new_email_sub, config_ticket_new_email_format, "
				+ " config_ticket_new_email_exe_sub, config_ticket_new_email_exe_format, "
				+ " config_ticket_new_sms_enable, config_ticket_new_sms_format, config_ticket_new_sms_exe_format,"
				+ " COALESCE(ticketemp.emp_email1,'') AS emp_email1, COALESCE(ticketemp.emp_email2,'') as emp_email2,"
				+ " COALESCE(ticketemp.emp_name,'') AS emp_name,"
				+ " COALESCE(ticketemp.emp_mobile1,'') AS emp_mobile1 , COALESCE(ticketemp.emp_mobile2,'') as emp_mobile2, "
				+ " COALESCE(ticketentry.emp_email1,'') AS entryemp_email,"
				+ " COALESCE(branch_ticket_email,'')branch_ticket_email"
				+ " FROM " + compdb(comp_id) + " axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketemp ON ticketemp.emp_id = " + ticket_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketentry ON ticketentry.emp_id = " + emp_id + ","
				+ compdb(comp_id) + "axela_comp,"
				+ compdb(comp_id) + " axela_config";
		if (!ticket_branch_id.equals("0")) {
			StrSql += " WHERE branch_id = " + ticket_branch_id;
		}

		// SOP("StrSql---" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_ticket_email = crs.getString("branch_ticket_email");
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
				config_email_enable = crs.getString("config_email_enable");
				config_admin_email = crs.getString("config_admin_email");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				config_ticket_new_email_enable = crs.getString("config_ticket_new_email_enable");
				config_ticket_new_email_format = crs.getString("config_ticket_new_email_format");
				config_ticket_new_email_sub = crs.getString("config_ticket_new_email_sub");
				config_ticket_new_email_exe_format = crs.getString("config_ticket_new_email_exe_format");
				config_ticket_new_email_exe_sub = crs.getString("config_ticket_new_email_exe_sub");
				config_ticket_new_sms_enable = crs.getString("config_ticket_new_sms_enable");
				config_ticket_new_sms_format = crs.getString("config_ticket_new_sms_format");
				config_ticket_new_sms_exe_format = crs.getString("config_ticket_new_sms_exe_format");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				entryemp_email = crs.getString("entryemp_email");
				// SOP("entryemp_email = " + entryemp_email);
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				if (!branch_ticket_email.equals("") && !emp_email_formail.equals("")) {
					emp_email_formail = emp_email_formail + "," + branch_ticket_email;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
