package axela.axelaauto_app;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class App_Ticket_Dash_Details extends Connect {

	public String emp_id = "0";
	public String emp_uuid = "";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String client = "";
	public String client1 = "";
	public String ticket_ticketcat_id = "0";
	public String ticket_tickettype_id = "0";
	public String ticket_id = "0";
	public String ticket_parent_id = "0";
	public String ticket_customer_id = "0";
	public String customer_name = "";
	public String jc_id = "";
	public String veh_id = "";
	public String veh_reg_no = "";
	public String ticket_ticketstatus_id = "0";
	public String ticket_subject = "";
	public String ticket_desc = "";
	public String ticket_closed_time = "";
	public String closed_date = "";
	public String ticket_closed_comments = "";
	public String ticket_read = "";
	public String ticket_ticketsource_id = "0";
	public String ticket_priorityticket_id = "0";
	public String ticket_ticket_dept_id = "0";
	public String ticket_emp_id = "0";
	public String ticket_due_time = "";
	public String ticket_cc = "";
	public String duedate = "";
	public String ticket_report_time = "";
	public String ticket_report_date = "";
	public String reporttime = strToLongDate(ToLongDate(kknow()));
	public String ticket_crm_id = "";
	public String ticket_so_id = "0";
	public String ticket_jc_id = "0";
	public String ticket_jcpsf_id = "0";
	public String ticket_entry_id = "0";
	public String ticket_entry_date = "";
	public String entry_id = "0";
	public String entry_date = "";
	public String msg = "";
	public String StrSql = "";
	public String childTickets = "";
	public String ticketParentId = "0";
	public String ticket_contact_id = "";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String customer_branch_id = "0";
	// /followup/////

	public String StrHTML = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String tickettrans_id = "0";
	public String tickettrans_followup = "";
	public String tickettrans_nextfollowup_time = "";
	public String tickettrans_private = "";
	public String tickettrans_entry_id = "0";
	public String tickettrans_entry_date = "";
	public String tickettrans_modified_id = "0";
	public String tickettrans_modified_date = "";
	public String tickettrans_contact_entry_id = "0";
	public String ticket_reopened_emp_id = "0";
	public String priorityticket_name = "";
	public String contact_entry_by = "";
	public String checkperm = "";
	public String entry_by = "";
	public String modified_by = "";
	public String modified_date = "";
	public String config_ticket_followup_email_enable = "";
	public String config_ticket_followup_email_sub = "";
	public String config_ticket_followup_email_format = "";
	public String config_ticket_followup_email_exe_sub = "";
	public String config_ticket_followup_email_exe_format = "";
	public String config_ticket_followup_sms_enable = "";
	public String config_ticket_followup_sms_format = "";
	// public String config_ticket_followup_sms_exe_format = "";
	public String config_admin_email = "";
	public String config_sms_enable = "";
	public String comp_sms_enable = "";
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_email_formail = "";
	public String entryemp_email = "";
	public String emp_email = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String contact_id = "0";
	public String contact_name = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String send_contact_email = "";
	public String branch_name = "";
	// public String ticket_dept_name = "";
	// public String ticket_status = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String docmsg = "";

	public String enquiry_id = "0";
	public String crm_followup_time = "";
	public String crm_crmdays_id = "0";
	public String crm_emp_id = "0";
	public String crmdays_daycount = "0";
	public String crmemp_name = "", salesexe_name = "";
	public String so_date = "";
	public String jcpsf_followup_time = "";
	public String psfemp_name = "";
	public String psfdays_daycount = "0";
	public String jc_veh_id = "0";
	public String jc_reg_no = "0";

	public Enquiry_Dash_Customer Customer_dash = new Enquiry_Dash_Customer();

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
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				if (ticket_id != null && !ticket_id.equals("") && !ticket_id.equals("0")) {
					ListChildTickets();
				}
				PopulateFields(response);
				PopulateConfigDetails();
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

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT "
					+ " COALESCE(enquiry_id,'0') AS enquiry_id,"
					+ " COALESCE(crm_followup_time,'') AS crm_followup_time,"
					+ " COALESCE(crm_crmdays_id,'0') AS crm_crmdays_id,"
					+ " COALESCE(crm_emp_id,'0') AS crm_emp_id,"
					+ " COALESCE (enquiry_emp_id, '0') AS enquiry_emp_id,"
					+ " COALESCE(so_date,'') AS so_date,"
					+ " COALESCE(jcpsf_followup_time,'') AS jcpsf_followup_time,"
					+ " COALESCE(CONCAT(psf.emp_name,' (', psf.emp_ref_no,')'),'') AS 'psfemp_name', "
					+ " COALESCE(CONCAT(crmdays_daycount,crmdays_desc,''),'') AS 'crmdays_daycount',"
					+ " COALESCE(CONCAT(crm.emp_name,' (', crm.emp_ref_no,')'),'') AS 'crmemp_name',"
					+ " COALESCE(CONCAT(salesexe.emp_name,' (', salesexe.emp_ref_no,')'),'') AS 'salesexe_name',"
					+ " COALESCE(CONCAT( psfdays_daycount, ' (', psfdays_desc,')'),'') AS 'psfdays_daycount',"
					+ " COALESCE(jc_veh_id,'0') AS jc_veh_id,"
					+ " COALESCE(jc_reg_no,'0') AS jc_reg_no, " + compdb(comp_id) + "axela_service_ticket.*,"
					+ " COALESCE(contact_id, 0) contact_id, "
					+ " COALESCE(concat(title_desc,' ',contact_fname,' ',contact_lname),'') AS contact_name, "
					+ " COALESCE(contact_email1, '') contact_email1, "
					+ " COALESCE(contact_email2, '') contact_email2,"
					+ " COALESCE(contact_mobile1, '') contact_mobile1, "
					+ " COALESCE(contact_mobile2, '') contact_mobile2, "
					+ " COALESCE(customer_name,'') AS customer_name, "
					+ " COALESCE(customer_branch_id, 0) AS customer_branch_id, branch_name,"
					+ compdb(comp_id) + "axela_emp.emp_id emp_id,"
					+ " COALESCE(e.emp_id,'') AS entryemp_id, "
					+ " CONCAT(e.emp_name, ' (',e.emp_ref_no,')') AS entryemp_name,"
					+ " COALESCE(veh_reg_no, '') AS veh_reg_no "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON " + compdb(comp_id) + "axela_emp.emp_id = ticket_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = ticket_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = crm_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = ticket_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp psf ON psf.emp_id = jcpsf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"

					// + " inner join " + compdb(comp_id) + " "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					// + BranchAccess
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " WHERE ticket_id = " + ticket_id
					+ BranchAccess.replace("branch_id", "ticket_branch_id")
					+ ExeAccess.replace("emp_id", "" + compdb(comp_id) + "axela_emp.emp_id");
			// SOP("StrSql---PopulateFields---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_id = crs.getString("ticket_id");
					customer_branch_id = crs.getString("customer_branch_id");
					branch_name = crs.getString("branch_name");
					ticket_contact_id = crs.getString("ticket_contact_id");
					ticket_customer_id = crs.getString("ticket_customer_id");
					ticket_parent_id = crs.getString("ticket_parent_id");
					ticket_ticketstatus_id = crs.getString("ticket_ticketstatus_id");
					ticket_ticketcat_id = crs.getString("ticket_ticketcat_id");
					ticket_tickettype_id = crs.getString("ticket_tickettype_id");
					ticket_subject = crs.getString("ticket_subject");
					ticket_desc = crs.getString("ticket_desc");
					ticket_closed_time = crs.getString("ticket_closed_time");
					closed_date = strToLongDate(ticket_closed_time);
					ticket_closed_comments = crs.getString("ticket_closed_comments");
					ticket_emp_id = crs.getString("emp_id");

					ticket_crm_id = crs.getString("ticket_crm_id");
					ticket_so_id = crs.getString("ticket_so_id");
					ticket_jc_id = crs.getString("ticket_jc_id");
					ticket_jcpsf_id = crs.getString("ticket_jcpsf_id");
					ticket_read = crs.getString("ticket_read");
					if (ticket_read.equals("0") && emp_id.equals(ticket_emp_id)) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket SET ticket_read = '1' WHERE ticket_id=" + ticket_id + "";
						updateQuery(StrSql);
					}
					ticket_ticketsource_id = crs.getString("ticket_ticketsource_id");

					ticket_report_time = crs.getString("ticket_report_time");
					reporttime = strToLongDate(ticket_report_time);
					ticket_report_date = reporttime.substring(0, 10);
					ticket_report_time = reporttime.substring(10, 16);
					ticket_ticket_dept_id = crs.getString("ticket_ticket_dept_id");
					ticket_priorityticket_id = crs.getString("ticket_priorityticket_id");
					ticket_due_time = crs.getString("ticket_due_time");
					duedate = strToLongDate(ticket_due_time);
					ticket_cc = crs.getString("ticket_cc");
					entry_id = crs.getString("entryemp_name");

					ticket_entry_id = crs.getString("ticket_entry_id");
					entry_date = strToLongDate(crs.getString("ticket_entry_date"));
					// client = "<b> " + crs.getString("contact_name") + "</a></b>";
					// client1 = "<b><a href=\"../customer/customer-list.jsp?customer_id=" + ticket_customer_id + "\"> " + crs.getString("customer_name") + "</a></b>";
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_name = crs.getString("contact_name");
					contact_id = crs.getString("contact_id");
					customer_name = crs.getString("customer_name");
					jc_id = crs.getString("ticket_jc_id");
					veh_id = crs.getString("ticket_veh_id");
					if (!crs.getString("veh_reg_no").equals("")) {
						veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 4);
					}

					enquiry_id = crs.getString("enquiry_id");
					crm_followup_time = strToLongDate(crs.getString("crm_followup_time"));
					crm_crmdays_id = crs.getString("crm_crmdays_id");
					crm_emp_id = crs.getString("crm_emp_id");
					crmdays_daycount = crs.getString("crmdays_daycount");
					crmemp_name = crs.getString("crmemp_name");
					salesexe_name = crs.getString("salesexe_name");
					so_date = strToShortDate(crs.getString("so_date"));
					jcpsf_followup_time = strToShortDate(crs.getString("jcpsf_followup_time"));
					psfemp_name = crs.getString("psfemp_name");
					psfdays_daycount = crs.getString("psfdays_daycount");
					jc_veh_id = crs.getString("jc_veh_id");
					jc_reg_no = crs.getString("jc_reg_no");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateSourceType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketsource_id, ticketsource_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_source "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticketsource_id"
					+ " ORDER BY ticketsource_name";
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

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketstatus_id, ticketstatus_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_status "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticketstatus_id"
					+ " ORDER BY ticketstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketstatus_id"), ticket_ticketstatus_id));
				Str.append(">").append(crs.getString("ticketstatus_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketPrioirty() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY priorityticket_id"
					+ " ORDER BY priorityticket_name";
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
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticket_dept_id"
					+ " ORDER BY ticket_dept_name";
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
			StrSql = "SELECT emp_name, emp_ref_no, " + compdb(comp_id) + "axela_emp.emp_id AS emp_id "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					// + " left join " + compdb(comp_id) +
					// "axela_service_ticket on ticket_emp_id = " +
					// compdb(comp_id) + "axela_emp.emp_id "
					// + " and ticket_ticketstatus_id!=3 "
					+ " WHERE 1=1 "
					+ " AND emp_ticket_owner = '1'"
					// + ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql-all-" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
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
			StrSql += " group by ticketcat_id "
					+ " order by ticketcat_name";

			// SOP("StrSqll== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_ticket_ticketcat_id\" class=\"form-control\" onChange=\"SecurityCheck('dr_ticket_ticketcat_id',this,'hint_dr_ticket_ticketcat_id');\">");
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
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type  "
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

	protected String ListChildTickets() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT ticket_id, coalesce(customer_id,'') AS customer_id "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " WHERE ticket_parent_id =" + ticket_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("customer_id").equals("")) {
						Str.append("<b><u><a href=callurlapp-ticket-dash-details.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">").append(crs.getString("ticket_id"))
								.append("</a></u></b>&nbsp &nbsp &nbsp");
					} else {
						Str.append("<b><u><a href=callurlapp-ticket-dash-details.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">").append(crs.getString("ticket_id"))
								.append("</a></u></b>&nbsp &nbsp &nbsp");
					}
				}
			} else {
				Str.append("");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		childTickets = Str.toString();
		return childTickets;
	}

	public void PopulateConfigDetails() {
		StrSql = " SELECT config_service_ticket_cat, config_service_ticket_type, admin.emp_role_id AS emp_role_id,"
				+ " config_email_enable, config_admin_email, config_sms_enable, comp_email_enable, comp_sms_enable,"
				+ " config_ticket_followup_email_enable, config_ticket_followup_email_sub, config_ticket_followup_email_format, "
				+ " config_ticket_followup_email_exe_sub, config_ticket_followup_email_exe_format, "
				+ " config_ticket_followup_sms_enable, config_ticket_followup_sms_format,"
				// + " config_ticket_followup_sms_exe_format,"
				+ " COALESCE(ticketemp.emp_email1,'') AS emp_email1, COALESCE(ticketemp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(ticketemp.emp_name,'') AS emp_name,"
				+ " COALESCE(ticketemp.emp_mobile1,'') AS emp_mobile1 , COALESCE(ticketemp.emp_mobile2,'') AS emp_mobile2, "
				+ " COALESCE(ticketentry.emp_email1,'') AS entryemp_email, coalesce(admin.emp_email1,'') AS emp_email "
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketemp ON ticketemp.emp_id = " + ticket_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketentry ON ticketentry.emp_id = " + ticket_entry_id + ""
				+ " WHERE 1 = 1 "
				+ " AND admin.emp_id = " + emp_id;
		// SOP("StrSql config= " + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
				config_email_enable = crs.getString("config_email_enable");
				config_admin_email = crs.getString("config_admin_email");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				config_ticket_followup_email_enable = crs.getString("config_ticket_followup_email_enable");
				config_ticket_followup_email_format = crs.getString("config_ticket_followup_email_format");
				config_ticket_followup_email_sub = crs.getString("config_ticket_followup_email_sub");
				config_ticket_followup_email_exe_format = crs.getString("config_ticket_followup_email_exe_format");
				config_ticket_followup_email_exe_sub = crs.getString("config_ticket_followup_email_exe_sub");
				config_ticket_followup_sms_enable = crs.getString("config_ticket_followup_sms_enable");
				config_ticket_followup_sms_format = crs.getString("config_ticket_followup_sms_format");
				// config_ticket_followup_sms_exe_format =
				// crs.getString("config_ticket_followup_sms_exe_format");
				entryemp_email = crs.getString("entryemp_email");
				// SOP("entryemp_email = " + entryemp_email);
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
				emp_role_id = crs.getString("emp_role_id");
				emp_email = crs.getString("emp_email");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
