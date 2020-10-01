package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Ticket_Dash extends Connect {

	public String emp_id = "0";
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
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// CheckPerm(comp_id, "emp_ticket_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				tickettrans_id = CNumeric(PadQuotes(request.getParameter("tickettrans_id")));
				docmsg = PadQuotes(request.getParameter("docmsg"));

				StrSql = "SELECT ticket_parent_id FROM " + compdb(comp_id) + "axela_service_ticket "
						+ " WHERE 1=1 ";
				if (!ticket_id.equals("0")) {
					StrSql = StrSql + " and ticket_id = " + ticket_id;
				} else if (!ticket_parent_id.equals("0")) {
					StrSql = StrSql + " and ticket_id = " + ticket_parent_id;
				}
				ticketParentId = ExecuteQuery(StrSql);
				// SOP("ticketParentId==="+ticketParentId);

				if (ticket_id != null && !ticket_id.equals("") && !ticket_id.equals("0")) {
					ListChildTickets();
				}

				PopulateFields(response);

				PopulateConfigDetails();

				// //////////////////Followup start/////////////
				StrHTML = ListFollowupData();

				if (add.equals("yes")) {
					status = "Add";
					if (ticket_id.equals("0") || !isNumeric(ticket_id)) {
						response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access Denied!"));
					}
				} else if (update.equals("yes")) {
					status = "Update";
				} else {
					status = "";
				}
				// SOP("status = " + status);
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						CheckPerm(comp_id, "emp_ticket_add", request, response);
						GetValues(request, response);
						tickettrans_entry_id = emp_id;
						tickettrans_entry_date = ToLongDate(kknow());
						if (ticket_ticketstatus_id.equals("3")) {
							msg = msg + "<br>Cannot add Follow Up!";
						} else {
							AddFields();
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("ticket-dash.jsp?ticket_id=" + ticket_id + "&msg=Follow-up added successfully!#tabs-2"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Follow-up".equals(deleteB)) {
						tickettrans_id = CNumeric(PadQuotes(request.getParameter("tickettrans_id")));
						PopulateFollowupFields(response);
					} else if ("yes".equals(updateB) && !"Delete Follow-up".equals(deleteB)) {
						CheckPerm(comp_id, "emp_ticket_edit", request, response);
						GetValues(request, response);
						tickettrans_modified_id = emp_id;
						tickettrans_modified_date = ToLongDate(kknow());
						if (ticket_ticketstatus_id.equals("3")) {
							msg = msg + "<br>Cannot Update Follow-Up!";
						} else {
							UpdateFields(request);
						}
						if (!msg.equals("")) {
							msg = "Error! " + msg;
						} else {
							msg = "Follow-up updated successfully!";
							response.sendRedirect(response.encodeRedirectURL("ticket-dash.jsp?ticket_id=" + ticket_id + "&msg=" + msg + "#tabs-2"));
						}
					} else if ("Delete Follow-up".equals(deleteB)) {
						GetValues(request, response);
						CheckPerm(comp_id, "emp_ticket_delete", request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error! " + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("ticket-dash.jsp?ticket_id=" + ticket_id + "&msg=Follow-up deleted successfully!#tabs-2"));
						}
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

	// public void ContactInfo() {
	// StrSql =
	// "Select contact_id, concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name, "
	// + " contact_email1, contact_email2, contact_mobile1, contact_mobile2"
	// + " from " + compdb(comp_id) + "axela_customer_contact "
	// + " inner join " + compdb(comp_id) +
	// "axela_service_ticket on ticket_contact_id = contact_id "
	// + " inner join " + compdb(comp_id) +
	// "axela_title on title_id = contact_title_id "
	// + " where ticket_id = " + ticket_id;
	// try {
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// contact_email1 = crs.getString("contact_email1");
	// contact_email2 = crs.getString("contact_email2");
	// contact_mobile1 = crs.getString("contact_mobile1");
	// contact_mobile2 = crs.getString("contact_mobile2");
	// contact_name = crs.getString("contact_name");
	// contact_id = crs.getString("contact_id");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// }
	// }
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
					+ " COALESCE(customer_branch_id, 0) AS customer_branch_id,"
					+ compdb(comp_id) + "axela_emp.emp_id emp_id, coalesce(e.emp_id,'') AS entryemp_id, "
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
			SOP("StrSql---populate---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_id = crs.getString("ticket_id");
					customer_branch_id = crs.getString("customer_branch_id");
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
					ticket_ticket_dept_id = crs.getString("ticket_ticket_dept_id");
					ticket_priorityticket_id = crs.getString("ticket_priorityticket_id");
					ticket_due_time = crs.getString("ticket_due_time");
					duedate = strToLongDate(ticket_due_time);
					ticket_cc = crs.getString("ticket_cc");
					entry_id = "<a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("entryemp_id") + ">" + crs.getString("entryemp_name") + "</a>";

					ticket_entry_id = crs.getString("ticket_entry_id");
					entry_date = strToLongDate(crs.getString("ticket_entry_date"));
					client = "<b><a href=\"../customer/customer-contact-list.jsp?contact_id=" + ticket_contact_id + "\"> " + crs.getString("contact_name") + "</a></b>";
					client1 = "<b><a href=\"../customer/customer-list.jsp?customer_id=" + ticket_customer_id + "\"> " + crs.getString("customer_name") + "</a></b>";
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_name = crs.getString("contact_name");
					contact_id = crs.getString("contact_id");
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
					crmemp_name = "<a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("crm_emp_id") + ">" + crs.getString("crmemp_name") + "</a>";
					salesexe_name = "<a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("enquiry_emp_id") + ">" + crs.getString("salesexe_name") + "</a>";
					so_date = strToShortDate(crs.getString("so_date"));
					jcpsf_followup_time = strToShortDate(crs.getString("jcpsf_followup_time"));
					psfemp_name = crs.getString("psfemp_name");
					psfdays_daycount = crs.getString("psfdays_daycount");
					jc_veh_id = crs.getString("jc_veh_id");
					jc_reg_no = crs.getString("jc_reg_no");
					// SOP("jc_id = " + jc_id);
					// SOP("veh_id = " + veh_id);
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket!"));
			}
			// SOP ("customer_branch_id = " + customer_branch_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String PopulateExecutive() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0> Select </option>");
	// try {
	// StrSql = "select emp_name, emp_ref_no, " + compdb(comp_id) +
	// "axela_emp.emp_id as emp_id "
	// + " from " + compdb(comp_id) + "axela_emp "
	// + " left join " + compdb(comp_id) + "axela_emp_branch on " +
	// compdb(comp_id) + "axela_emp.emp_id = " + compdb(comp_id) +
	// "axela_emp_branch.emp_id "
	// + " left join " + compdb(comp_id) +
	// "axela_service_ticket on ticket_emp_id = " + compdb(comp_id) +
	// "axela_emp.emp_id "
	// + " and ticket_ticketstatus_id!=3 "
	// + " where emp_active = '1' and " + compdb(comp_id) +
	// "axela_emp.emp_id!=1 "
	// + " and  (" + compdb(comp_id) + "axela_emp_branch.emp_branch_id=" +
	// customer_branch_id + " or " + compdb(comp_id) +
	// "axela_emp.emp_branch_id = " + customer_branch_id + ") "
	// + " group by " + compdb(comp_id) + "axela_emp.emp_id  "
	// + " order by emp_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// // SOP("StrSql-eeell-"+StrSql);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
	// Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
						Str.append("<b><a href=ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">").append(crs.getString("ticket_id")).append("</a></b><br>");
					} else {
						Str.append("<b><a href=ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">").append(crs.getString("ticket_id")).append("</a></b><br>");
					}
				}
			} else {
				Str.append("");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// / List Followup/////////////
	public String ListFollowupData() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;

		StrSql = "SELECT ticket_id, tickettrans_id, ticket_customer_id, tickettrans_followup, tickettrans_nextfollowup_time, tickettrans_private, "
				+ " (CASE WHEN tickettrans_private='1' THEN 'Yes' ELSE 'No' END) as private, "
				+ " tickettrans_entry_id, "
				+ " COALESCE(CONCAT(e.emp_name, ' (', e.emp_ref_no, ')'),'') AS entry_by, "
				+ " tickettrans_entry_date, "
				+ " tickettrans_modified_id, "
				+ " COALESCE(CONCAT(m.emp_name, ' (', m.emp_ref_no, ')'),'') AS modified_by, "
				+ " tickettrans_modified_date, "
				+ " COALESCE(customer_name,'') AS customer_name, tickettrans_contact_entry_id, customer_id ";

		CountSql = "SELECT COUNT(tickettrans_id) ";

		SqlJoin = "FROM " + compdb(comp_id) + "axela_service_ticket_trans "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket on ticket_id=tickettrans_ticket_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as e ON e.emp_id=tickettrans_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as m ON m.emp_id=tickettrans_modified_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id=ticket_customer_id "
				+ " WHERE tickettrans_ticket_id='" + ticket_id + "'";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

		StrSql = StrSql + " ORDER BY tickettrans_id DESC";
		// SOP("StrSql-----"+StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords != 0) {
			// SOP("CountSql-----"+TotalRecords);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5% data-toggle=\"true\">#</th>\n");
					Str.append("<th data-hide=\"phone\">Follow-up Description</th>\n");
					Str.append("<th data-hide=\"phone\">Next Follow-up Time</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Entry by</th>\n");
					// Str.append("<th data-hide=\"phone, tablet\">Modified by</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						String colour = "";
						if (!crs.getString("tickettrans_contact_entry_id").equals("0")) {
							colour = "blue"; // blue
						} else if (crs.getString("tickettrans_private").equals("1")) {
							colour = "#FF0000"; // red
						} else if (crs.getString("tickettrans_private").equals("0")) {
							colour = "black"; // red
						}

						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>");
						Str.append("<td width=50% valign=top>");
						Str.append("<font color=").append(colour).append(">").append(crs.getString("tickettrans_followup")).append("</font></td>");
						Str.append("<td valign=top>");
						Str.append("<font color=").append(colour).append(">").append(strToLongDate(crs.getString("tickettrans_nextfollowup_time"))).append("</font></td>");
						Str.append("<td width=30% valign=top>\n");
						if (crs.getString("private").equals("Yes")) {
							Str.append("<font color=red><b>PRIVATE</b></font><br>");
						}
						if (!crs.getString("tickettrans_entry_id").equals("0")) {
							Str.append("Entry By: ").append(crs.getString("entry_by")).append("");
							Str.append("<br>Entry Time: ").append(strToLongDate(crs.getString("tickettrans_entry_date"))).append("");
						} else if (!crs.getString("tickettrans_contact_entry_id").equals("0")) {
							Str.append("<br>Customer: <a href=\"customer-pop.jsp?customer_id=").append(crs.getString("customer_id")).append("\">").append(crs.getString("customer_name"))
									.append("</a>");
						}
						if (!crs.getString("tickettrans_modified_id").equals("0")) {
							Str.append("<br>Modified By: ").append(crs.getString("modified_by")).append("");
							Str.append("<br>Modified Time: ").append(strToLongDate(crs.getString("tickettrans_modified_date"))).append("");
						}
						Str.append("<br><a href=ticket-dash.jsp?update=yes&ticket_id=").append(crs.getString("ticket_id")).append("&tickettrans_id=").append(crs.getString("tickettrans_id"))
								.append("#tabs-2").append(">Update Follow-up</a>");
						Str.append("</td></tr>\n");
						Str.append("</td></tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			if (msg.equals("")) {
				Str.append("<br><br><br><br><font color=red><b>No follow-up found!</b></font>");
			}
		}
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		tickettrans_followup = PadQuotes(request.getParameter("txt_tickettrans_followup"));
		tickettrans_nextfollowup_time = PadQuotes(request.getParameter("txt_tickettrans_nextfollowup_time"));
		// SOP("tickettrans_nextfollowup_time----------" + tickettrans_nextfollowup_time);
		tickettrans_private = CheckBoxValue(PadQuotes(request.getParameter("chk_private")));
		contact_entry_by = PadQuotes(request.getParameter("contact_entry_by"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

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
		CheckForm();
		if (msg.equals("")) {
			try {
				tickettrans_id = ExecuteQuery("Select max(tickettrans_id) as tickettrans_id from " + compdb(comp_id) + "axela_service_ticket_trans");
				if (tickettrans_id == null || tickettrans_id.equals("")) {
					tickettrans_id = "0";
				}
				int tickettrans_idi = Integer.parseInt(tickettrans_id) + 1;
				tickettrans_id = "" + tickettrans_idi;

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_ticket_trans"
						+ "(tickettrans_id,"
						+ "tickettrans_ticket_id,"
						+ "tickettrans_followup,"
						+ "tickettrans_nextfollowup_time,"
						+ "tickettrans_private,"
						+ "tickettrans_entry_id ,"
						+ "tickettrans_entry_date ,"
						+ "tickettrans_modified_id ,"
						+ "tickettrans_modified_date) "
						+ "VALUES	"
						+ "(" + tickettrans_id + ","
						+ "'" + ticket_id + "',"
						+ "'" + tickettrans_followup + "',"
						+ " '" + ConvertLongDateToStr(tickettrans_nextfollowup_time) + "',"
						+ "'" + tickettrans_private + "',"
						+ "" + tickettrans_entry_id + ", "
						+ "" + tickettrans_entry_date + ", "
						+ "0 ,"
						+ "''"
						+ ")";
				// SOP("StrSql-----------" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					tickettrans_id = rs.getString(1);
				}
				rs.close();
				if (!tickettrans_id.equals("") && !tickettrans_id.equals("0")) {
					// ContactInfo();
					if (!contact_email2.equals("") && !contact_email1.equals("")) {
						send_contact_email = contact_email1 + "," + contact_email2;
					} else if (!contact_email1.equals("")) {
						send_contact_email = contact_email1;
					}
					// SOP("comp_email_enable = " + comp_email_enable);
					// SOP("config_email_enable = " + config_email_enable);
					// SOP("config_admin_email = " + config_admin_email);
					// SOP("config_ticket_followup_email_enable = " +
					// config_ticket_followup_email_enable);
					// SOP("contact_email1 = " + contact_email1);
					// SOP("config_ticket_followup_email_format = " +
					// config_ticket_followup_email_format);
					// SOP("config_ticket_followup_email_sub = " +
					// config_ticket_followup_email_sub);

					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !config_admin_email.equals("")
							&& config_ticket_followup_email_enable.equals("1")) {
						if (!send_contact_email.equals("")
								&& !config_ticket_followup_email_format.equals("")
								&& !config_ticket_followup_email_sub.equals("")) {
							// SOP("contact exe-----------");
							// FollowupSendEmail();
						}
						// SOP("config_ticket_followup_email_exe_format = " +
						// config_ticket_followup_email_exe_format);
						// SOP("config_ticket_followup_email_exe_sub = " +
						// config_ticket_followup_email_exe_sub);
						// SOP("emp_email_formail = " + emp_email_formail);
						if (!config_ticket_followup_email_exe_format.equals("")
								&& !config_ticket_followup_email_exe_sub.equals("") && !emp_email_formail.equals("")) {
							// SOP("ticket_emp_id---------------"+ticket_emp_id);
							// SOP("ticket_entry_id---------------"+ticket_entry_id);
							// SOP("emp_email_formail---------------"+emp_email_formail);
							if (!ticket_emp_id.equals(ticket_entry_id) && !entryemp_email.equals("")) {
								emp_email_formail = emp_email_formail + "," + entryemp_email;
							}
							// SOP("emp_email_formail = " + emp_email_formail);
							// FollowupSendEmailToExecutive();
						}
					}

					if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && config_ticket_followup_sms_enable.equals("1")) {
						if (!config_ticket_followup_sms_format.equals("")) {
							// SOP("cont mobi---------------");
							// if (!contact_mobile1.equals("")) {
							// SendSMS(contact_mobile1);
							// }
							// if (!contact_mobile2.equals("")) {
							// SendSMS(contact_mobile2);
							// }
						}
						// if
						// (!config_ticket_followup_sms_exe_format.equals("")) {
						// // SOP("exeeeeeee mob-----------");
						// if (!emp_mobile1.equals("")) {
						// SendSMSToExecutive(emp_mobile1);
						// }
						// if (!emp_mobile2.equals("")) {
						// SendSMSToExecutive(emp_mobile2);
						// }
						// }
					}
				}
				conntx.commit();
				// SOP("Transaction commit...");
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
				}
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_trans"
						+ " SET"
						+ " tickettrans_followup = '" + tickettrans_followup + "',"
						+ " tickettrans_nextfollowup_time = '" + ConvertLongDateToStr(tickettrans_nextfollowup_time) + "',"
						+ " tickettrans_private = '" + tickettrans_private + "',"
						+ " tickettrans_modified_id = " + tickettrans_modified_id + ","
						+ " tickettrans_modified_date = " + tickettrans_modified_date + ""
						+ " WHERE tickettrans_id = " + tickettrans_id + "";
				// SOP("StrSql--------------" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_ticket_trans WHERE tickettrans_id =" + tickettrans_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFollowupFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name from " + compdb(comp_id) + "axela_service_ticket_trans "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id=tickettrans_contact_entry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id=contact_title_id "
					+ " WHERE tickettrans_id=" + tickettrans_id + "";
			// SOP(StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				tickettrans_id = crs.getString("tickettrans_id");
				ticket_id = crs.getString("tickettrans_ticket_id");
				tickettrans_followup = crs.getString("tickettrans_followup");
				tickettrans_nextfollowup_time = strToShortDate(crs.getString("tickettrans_nextfollowup_time"));
				tickettrans_private = crs.getString("tickettrans_private");
				tickettrans_entry_id = crs.getString("tickettrans_entry_id");
				tickettrans_entry_date = crs.getString("tickettrans_entry_date");
				tickettrans_modified_id = crs.getString("tickettrans_modified_id");
				if (!crs.getString("tickettrans_entry_id").equals("0")) {
					entry_by = Exename(comp_id, crs.getInt("tickettrans_entry_id"));
					entry_date = strToLongDate(crs.getString("tickettrans_entry_date"));
				} else if (!crs.getString("tickettrans_contact_entry_id").equals("0")) {
					contact_entry_by = crs.getString("contact_name") + "";
					// entry_date =
					// strToLongDate(crs.getString("tickettrans_student_entry_date"));
				}
				if (!tickettrans_modified_id.equals("0")) {
					modified_by = Exename(comp_id, crs.getInt("tickettrans_modified_id"));
					modified_date = strToLongDate(crs.getString("tickettrans_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void FollowupSendEmail() throws SQLException {
		String sub = (config_ticket_followup_email_sub);
		String emailmsg = (config_ticket_followup_email_format);

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETDESCRIPTION]',ticket_desc)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',ticket_report_time)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',ticket_due_time)";

		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";

		// emailmsg = "replace(" + emailmsg + ",'[EXECUTIVENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',coalesce(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[TICKETDESCRIPTION]',ticket_desc)";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',ticket_report_time)";
		sub = "replace(" + sub + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',ticket_due_time)";
		sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		// sub = "replace(" + sub + ",'[EXECUTIVENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		sub = "replace(" + sub + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		sub = "replace(" + sub + ",'[MODELNAME]',coalesce(model_name, ''))";
		sub = "replace(" + sub + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		sub = "replace(" + sub + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT "
					+ "	branch_id,"
					+ " " + contact_id + ", "
					+ " '" + contact_name + "', "
					+ " '" + emp_email1 + "', "
					+ " '" + send_contact_email + "', "
					+ " '" + ticket_cc + "', "
					+ " " + (sub) + ", "
					+ " " + (emailmsg) + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ticketexe ON ticketexe.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = ticketexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id "
					// + " left join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_trans ON ticket_id = tickettrans_ticket_id "
					+ " WHERE tickettrans_id=" + tickettrans_id
					+ " GROUP BY tickettrans_id"
					+ " LIMIT 1";
			// SOP("StrSql---"+StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql--iiii-"+StrSql);
			// updateQuery(StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void FollowupSendEmailToExecutive() throws SQLException {
		String emailmsg, sub;
		emailmsg = (config_ticket_followup_email_exe_format);
		sub = (config_ticket_followup_email_exe_sub);

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETDESCRIPTION]',ticket_desc)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',ticket_report_time)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',ticket_due_time)";

		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";

		emailmsg = "replace(" + emailmsg + ",'[TICKETOWNER]',owner.emp_name)";
		// emailmsg = "replace(" + emailmsg +
		// ",'[EXECUTIVENAME]',sesn.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',sesn.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',sesn.emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',sesn.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',sesn.emp_email2)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',coalesce(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[TICKETDESCRIPTION]',ticket_desc)";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',ticket_report_time)";
		sub = "replace(" + sub + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',ticket_due_time)";
		sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[TICKETOWNER]',owner.emp_name)";
		sub = "replace(" + sub + ",'[EXECUTIVENAME]',sesn.emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',sesn.emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEMOBILE2]',sesn.emp_mobile2)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',sesn.emp_email1)";
		sub = "replace(" + sub + ",'[EXEEMAIL2]',sesn.emp_email2)";
		sub = "replace(" + sub + ",'[MODELNAME]',coalesce(model_name, ''))";
		sub = "replace(" + sub + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		sub = "replace(" + sub + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " " + emp_id + ","
					+ " sesn.emp_name,"
					+ " '" + emp_email + "',"
					+ " '" + emp_email_formail + "',"
					+ " '" + ticket_cc + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "', "
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner ON owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesn ON sesn.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = sesn.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id "
					// + " left join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_trans ON ticket_id = tickettrans_ticket_id "
					+ " WHERE tickettrans_id=" + tickettrans_id
					+ " GROUP BY tickettrans_id"
					+ " LIMIT 1";
			// SOP("StrSqlselect-emaile-"+StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSqlinsert-emaile-" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String contact_mobile) throws SQLException {
		String smsmsg = config_ticket_followup_sms_format;

		smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETDESCRIPTION]',ticket_desc)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',ticket_report_time)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DUETIME]',ticket_due_time)";

		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";

		// smsmsg = "replace(" + smsmsg + ",'[EXECUTIVENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',coalesce(model_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		smsmsg = "replace(" + smsmsg + ",'[PRIORITY]',priorityticket_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',coalesce(concat(contact_fname,' ', contact_lname),''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',coalesce(customer_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";

		try {
			StrSql = "SELECT "
					+ "" + contact_id + ", "
					+ " '" + contact_name + "', "
					+ " '" + contact_mobile + "', "
					+ " " + (smsmsg) + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " 0, "
					+ " " + emp_id + " "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ticketexe ON ticketexe.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = ticketexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id "
					// + " left join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = ticket_so_id "
					+ " left join " + compdb(comp_id) + "axela_sales_crm on crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_trans ON ticket_id = tickettrans_ticket_id "
					+ " WHERE tickettrans_id=" + tickettrans_id
					+ " GROUP BY tickettrans_id"
					+ " LIMIT 1";
			// + " where tickettrans_ticket_id=" + tickettrans_ticket_id;
			// SOP("StrSql---"+StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ "("
					+ "sms_contact_id,"
					+ "sms_contact,"
					+ "sms_mobileno,"
					+ "sms_msg,"
					+ "sms_date ,"
					+ "sms_sent ,"
					+ "sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql AddFields==: " + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	/*
	 * protected void SendSMSToExecutive(String emp_mobile) throws SQLException { String smsmsg = config_ticket_followup_sms_exe_format;
	 * 
	 * smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)"; smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)"; smsmsg = "replace(" + smsmsg +
	 * ",'[TICKETDESCRIPTION]',ticket_desc)"; smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)"; smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',ticket_report_time)"; smsmsg =
	 * "replace(" + smsmsg + ",'[TICKETCAT]',coalesce(ticketcat_name, ''))"; smsmsg = "replace(" + smsmsg + ",'[TICKETTYPE]',coalesce(tickettype_name, ''))"; smsmsg = "replace(" + smsmsg +
	 * ",'[DUETIME]',ticket_due_time)";
	 * 
	 * smsmsg = "replace(" + smsmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))"; smsmsg = "replace(" + smsmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))"; smsmsg =
	 * "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))"; smsmsg = "replace(" + smsmsg +
	 * ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))" ; smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))"; smsmsg = "replace(" + smsmsg +
	 * ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))"; smsmsg = "replace(" + smsmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
	 * 
	 * smsmsg = "replace(" + smsmsg + ",'[TICKETOWNER]',owner.emp_name)"; smsmsg = "replace(" + smsmsg + ",'[EXECUTIVENAME]',sesn.emp_name)"; smsmsg = "replace(" + smsmsg +
	 * ",'[EXEJOBTITLE]',jobtitle_desc)"; smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',sesn.emp_mobile1)"; smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',sesn.emp_mobile2)"; smsmsg = "replace(" +
	 * smsmsg + ",'[EXEEMAIL1]',sesn.emp_email1)"; smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',sesn.emp_email2)"; smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',coalesce(model_name, ''))"; smsmsg =
	 * "replace(" + smsmsg + ",'[REGNO]',coalesce(veh_reg_no, ''))"; smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)"; smsmsg = "replace(" + smsmsg +
	 * ",'[PRIORITY]',priorityticket_name)"; smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)"; smsmsg = "replace(" + smsmsg +
	 * ",'[CONTACTNAME]',coalesce(concat(title_desc, ' ', contact_fname,' ', contact_lname),''))" ; smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',coalesce(contact_mobile1,''))"; smsmsg =
	 * "replace(" + smsmsg + ",'[CONTACTEMAIL1]',coalesce(contact_email1, ''))"; smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)"; smsmsg = "replace(" + smsmsg +
	 * ",'[CUSTOMERNAME]',coalesce(customer_name, ''))"; smsmsg = "replace(" + smsmsg + ",'[FOLLOWUPDESC]',coalesce(tickettrans_followup, ''))";
	 * 
	 * try { StrSql = "SELECT" + " " + emp_id + "," + " s.emp_name ," + " '" + emp_mobile + "'," + " " + (smsmsg) + "," + " '" + ToLongDate(kknow()) + "'," + " 0," + " " + emp_id + "" + " from " +
	 * compdb(comp_id) + "axela_service_ticket" + " inner join " + compdb(comp_id) + "axela_emp owner on owner.emp_id = ticket_emp_id " + " inner join " + compdb(comp_id) +
	 * "axela_emp sesn on sesn.emp_id = " + emp_id + "" + " inner join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = s.emp_jobtitle_id" + " inner join " + compdb(comp_id) +
	 * "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id " + " inner join " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
	 * + " inner join " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id" + " left join " + compdb(comp_id) +
	 * "axela_customer on customer_id = ticket_customer_id " + " left join " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id " + " left join " + compdb(comp_id) +
	 * "axela_title on title_id = contact_title_id " + " left join " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id " + " left join " + compdb(comp_id) +
	 * "axela_service_ticket_type on tickettype_id = ticket_tickettype_id " + " left join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = ticket_enquiry_id " + " left join " +
	 * compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id " + " left join " + compdb(comp_id) + "axela_sales_crm on crm_id = ticket_crm_id "
	 * 
	 * + " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id" + " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
	 * + " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id" + " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id" + " LEFT JOIN " +
	 * compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
	 * 
	 * + " left join " + compdb(comp_id) + "axela_service_veh on veh_id = ticket_veh_id" + " left join " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id " + " left join " +
	 * compdb(comp_id) + "axela_service_jc_psf on jcpsf_id = ticket_jcpsf_id " + " left join " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id" + " left join " + compdb(comp_id)
	 * + "axela_inventory_item_model on model_id = item_model_id" + " left join " + compdb(comp_id) + "axela_service_ticket_trans on ticket_id = tickettrans_ticket_id " + " where tickettrans_id=" +
	 * tickettrans_id + " GROUP BY tickettrans_id" + " limit 1"; // SOP("StrSql-smse-"+StrSql); StrSql = "INSERT into " + compdb(comp_id) + "axela_sms" + " (sms_emp_id," + " sms_emp," +
	 * " sms_mobileno," + " sms_msg," + " sms_date," + " sms_sent," + " sms_entry_id)" + " " + StrSql + ""; // SOP("StrSql-smse-"+StrSql); stmttx.execute(StrSql); } catch (Exception ex) { if
	 * (conntx.isClosed()) { msg = "<br>Transaction Error!"; SOPError("conn is closed....."); } if (!conntx.isClosed() && conntx != null) { conntx.rollback(); msg = "<br>Transaction Error!";
	 * SOPError("connemsgction rollback..."); } SOPError("Axelaauto== " + this.getClass().getName()); SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex); } }
	 */
	// //////////////followup end///////
	public String ListDocs() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;

		StrSql = "SELECT * ";

		CountSql = "SELECT COUNT(doc_id) ";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket_docs "
				+ " WHERE doc_ticket_id='" + ticket_id + "'";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;
		StrSql = StrSql + " order by doc_id desc";
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		// Str.append("<TABLE width=\"100%\" border=\"0\" align=\"center\" cellPadding=\"0\" cellSpacing=\"0\">");
		// Str.append("<TR>");
		// Str.append("<TD align=\"center\" vAlign=\"top\">");
		// Str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >");
		// Str.append("<TBODY>");
		// Str.append("<tr>");
		// Str.append("<td width=\"30%\" align=\"right\"><a href=\"../portal/docs-update.jsp?add=yes&ticket_id=").append(ticket_id).append("\" >Add New Document...</a>");
		// Str.append("</td>");
		// Str.append("</tr>");
		// Str.append("<tr><td>&nbsp;</td></tr>");
		if (TotalRecords != 0) {
			// Str.append("<tr>");
			// Str.append("<td align=\"center\"><font color=\"#ff0000\" ><b> ").append(docmsg).append("</b></font></td>");
			// Str.append("</tr>");
			// Str.append("<tr>");
			// Str.append("<td height=\"200\" valign=\"top\" align=\"center\">");
			// Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<br><div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead>\n");
			Str.append("<tr align=center>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>Document Details</th>\n");
			Str.append("<th>Actions</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			CachedRowSet crs = processQuery(StrSql, 0);

			try {
				int count = 0;
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center nowrap><b>").append(count).append("</b></td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(TicketDocPath(comp_id)).exists()) {
							new File(TicketDocPath(comp_id)).mkdirs();
						}
						File f = new File(TicketDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "doc_id=").append(crs.getString("doc_id")).append("><b>").append(crs.getString("doc_title")).append(" (")
								.append(crs.getString("doc_id")).append(")</a></b> (").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br> ").append(crs.getString("doc_remarks"))
								.append("</td>\n");
					} else {
						Str.append("<td valign=top align=left ><b>").append(crs.getString("doc_title")).append(" (").append(crs.getString("doc_id")).append(") (0 Bytes)</b><br> ")
								.append(crs.getString("doc_remarks")).append("</td>\n");
					}
					if (!ticket_id.equals("0")) {
						Str.append("<td valign=top align=left ><a href=\"../portal/docs-update.jsp?update=yes&ticket_id=").append(ticket_id).append("&doc_id=").append(crs.getString("doc_id"))
								.append("\">Update Document</a>"
										+ "<br><a href=\"../portal/docs-update-title.jsp?update=yes&title=yes&ticket_id=").append(ticket_id).append("&doc_id=").append(crs.getString("doc_id"))
								.append("\">Update Title</a>"
										+ "</td>\n");
					}
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
			// Str.append("</td>");
			// Str.append("</tr>");
		} else {
			docmsg = "";
			Str.append("<br><br><br><br><center><font color=red><b>No Document(s) found!</b></font></center>");
		}
		// Str.append("</TBODY>");
		// Str.append("</table>");
		// Str.append("</TD>");
		// Str.append("</TR>");
		// Str.append("</TABLE>");
		return Str.toString();
	}

	public String ListHistoryData() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		StrSql = "SELECT * ";

		CountSql = "SELECT COUNT(history_id) ";

		SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket_history "
				+ " WHERE history_ticket_id='" + ticket_id + "'";

		StrSql = StrSql + SqlJoin;
		CountSql = CountSql + SqlJoin;

		StrSql = StrSql + " ORDER BY history_id desc ";

		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		// SOP("=="+StrSqlBreaker(StrSql));
		if (TotalRecords != 0) {
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				// Str.append("<TABLE cellSpacing=\"0\" cellPadding=\"0\" width=\"100%\" border=\"0\" align=\"center\">");
				// Str.append("<tr>");
				// Str.append("<td width=\"30%\" align=\"right\">&nbsp;");
				// Str.append("</td>");
				// Str.append("</tr>");
				// Str.append("<TR>");
				// Str.append("<TD height=\"200\" align=\"center\" vAlign=\"top\" >");
				if (crs.isBeforeFirst()) {
					int j = 0;
					String col = "white";
					String altcol = "#FFFFCC";
					String bgcol = col;

					Str.append("<br><div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Date</th>");
					Str.append("<th>Action By</th>");
					Str.append("<th data-hide=\"phone\">Type of Action</th>");
					Str.append("<th data-hide=\"phone\">New Value</th>");
					Str.append("<th data-hide=\"phone\">Old Value</th>");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>");

					while (crs.next()) {
						if (j == 0) {
							j = 1;
							bgcol = col;
						} else {
							j = 0;
							bgcol = altcol;
						}
						// Str.append("<tr onmouseover=\"this.style.background='#eeeeee';\" onmouseout=\"this.style.background='").append(bgcol).append("';\" bgcolor='").append(bgcol).append("'>\n");
						Str.append("<tr><td valign=top align=center >").append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
						Str.append("<td valign=top align=left >").append(Exename(comp_id, crs.getInt("history_emp_id"))).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_actiontype")).append(" </td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_newvalue")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_oldvalue")).append("</td>");
						Str.append("</tr>" + "\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} else {
					Str.append("<br><br><center><font color=red><b>No History(s) found!</b></font></center>");
				}
				Str.append("</td>\n</tr>\n");
				Str.append("</table>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String TicketSummary() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT ticket_id,"
				+ " ticket_read,"
				+ " ticket_closed_time,"
				+ " ticket_due_time,"
				+ " ticket_trigger,"
				+ " ticket_desc,"
				+ " ticket_report_time,"
				+ " ticket_closed_emp_id,"
				+ " ticket_closed_time,"
				+ " ticket_closed_comments,"
				+ " CONCAT(c.emp_name, ' (', c.emp_ref_no, ')') AS closed_by, ticket_notes,"
				+ " ticket_reopened_emp_id, "
				+ " CONCAT(o.emp_name, ' (', o.emp_ref_no, ')') AS reopen_by,"
				+ " ticket_reopened_comments,"
				+ " ticket_reopened_time, "
				+ " ticket_priorityticket_id,"
				+ " ticket_subject,"
				+ " ticket_emp_id,"
				+ " ticket_entry_id,"
				+ " ticket_entry_date,"
				+ " ticket_modified_id,"
				+ " ticket_modified_date, "
				+ " ticket_dept_name,"
				+ " COALESCE(priorityticket_name,'-') AS priorityticket_name,"
				+ " COALESCE(ticketsource_name,'-') AS ticketsource_name,"
				+ " ticketstatus_name, "
				+ " COALESCE(ticketcat_name, '') AS ticketcat_name,"
				+ " COALESCE(tickettype_name, '') AS tickettype_name,"
				+ " COALESCE(customer_name, '') AS customer_name, "
				+ " COALESCE(concat(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
				+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
				+ " COALESCE(contact_mobile2,'') AS contact_mobile2,"
				+ " COALESCE(contact_email1, '') AS contact_email1,"
				+ " COALESCE(contact_email2, '') AS contact_email2,"
				+ " COALESCE(veh_id, 0) AS veh_id,"
				+ " COALESCE(veh_reg_no, '') AS veh_reg_no,"
				+ " COALESCE(veh_chassis_no,'') AS veh_chassis_no, "
				+ " COALESCE(variant_name, '') AS variant_name,"
				+ " COALESCE(jc_id, 0) AS jc_id "
				+ " FROM " + compdb(comp_id) + "axela_service_ticket "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON ticket_priorityticket_id = priorityticket_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticket_ticketstatus_id = ticketstatus_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as c ON c.emp_id = ticket_closed_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp as o ON o.emp_id = ticket_reopened_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
				// + BranchAccess + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
				+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
				+ " WHERE ticket_id = " + ticket_id
				+ ExeAccess.replace("emp_id", "ticket_emp_id");
		// SOP("StrSql--"+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {

				// Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				// Str.append("<tr><td colspan=2 align=right><a href=\"ticket-summary-print.jsp?ticket_id=").append(crs.getInt("ticket_id")).append(" \">Print Ticket</a></td>\n</tr>");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr><th colspan=2 align=center valign=top>Ticket Summary</th>\n</tr>");
				Str.append("</thead><tbody>\n");
				Str.append("<tr><td width=25% align=left valign=top ><b> Ticket ID: </b></td><td width=75% valign=top align=left ><b>").append(crs.getString("ticket_id")).append("</b> </td></tr>\n");
				Str.append("<tr><td  align=left valign=top ><b>Subject: </b></td><td align=left valign=top ><b>").append(crs.getString("ticket_subject")).append("</b></td></tr>\n");
				Str.append("<tr><td  align=left valign=top>Description: </td><td align=left valign=top >").append(crs.getString("ticket_desc")).append("</td></tr>\n");
				if (!crs.getString("customer_name").equals("")) {
					Str.append("<tr><td  align=left valign=top>Customer: </td><td align=left valign=top >").append(crs.getString("customer_name")).append("</td></tr>\n");
				}
				if (!crs.getString("contact_name").equals("")) {
					Str.append("<tr><td  align=left valign=top>Contact: </td><td align=left valign=top >").append(crs.getString("contact_name")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top>Mobile1: </td><td align=left valign=top >").append(crs.getString("contact_mobile1")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top>Mobile2: </td><td align=left valign=top >").append(crs.getString("contact_mobile2")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top>Email1: </td><td align=left valign=top >").append(crs.getString("contact_email1")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top>Email2: </td><td align=left valign=top >").append(crs.getString("contact_email2")).append("</td></tr>\n");
				}
				if (!crs.getString("variant_name").equals("")) {
					Str.append("<tr><td  align=left valign=top>Item:&nbsp </td><td align=left valign=top >").append(crs.getString("variant_name")).append("</td></tr>\n");
					if (!crs.getString("veh_reg_no").equals("")) {
						Str.append("<tr><td  align=left valign=top>Reg. No.: &nbsp</td><td align=left valign=top >").append(SplitRegNo(crs.getString("veh_reg_no"), 4)).append("</td></tr>\n");
					} else {
						Str.append("<tr><td  align=left valign=top>Chassis No.: &nbsp</td><td align=left valign=top >").append(crs.getString("veh_chassis_no")).append("</td></tr>\n");
					}
					Str.append("<tr><td  align=left valign=top>Vehicle ID:&nbsp </td><td align=left valign=top >").append(crs.getString("veh_id")).append("</td></tr>\n");
				}
				if (!crs.getString("jc_id").equals("0")) {
					Str.append("<tr><td  align=left valign=top>JC ID:&nbsp </td><td align=left valign=top >").append(crs.getString("jc_id")).append("</td></tr>\n");
				}
				Str.append("<tr><td  align=left valign=top>Status: &nbsp</td><td align=left  valign=top >").append(crs.getString("ticketstatus_name")).append("</td></tr>\n");
				Str.append("<tr><td  align=left valign=top>Department:&nbsp </td><td align=left  valign=top >").append(crs.getString("ticket_dept_name")).append("</td></tr>\n");
				Str.append("<tr><td  align=left valign=top>Level:&nbsp </td><td align=left  valign=top >").append(crs.getString("ticket_trigger")).append("</td></tr>\n");
				Str.append("<tr><td  align=left valign=top>Priority:&nbsp </td><td align=left  valign=top >").append(crs.getString("priorityticket_name")).append("</td></tr>\n");
				if (!crs.getString("ticket_report_time").equals("")) {
					Str.append("<tr><td  align=left valign=top>Report Time:&nbsp </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_due_time").equals("")) {
					Str.append("<tr><td  align=left valign=top>Due Time: &nbsp</td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_due_time"))).append("</td></tr>\n");
				}
				Str.append("<tr><td  align=left valign=top>Execuitve:&nbsp </td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_emp_id"))).append("</td></tr>\n");
				if (!crs.getString("ticketcat_name").equals("")) {
					Str.append("<tr><td  align=left valign=top>Category: &nbsp</td><td align=left valign=top >").append(crs.getString("ticketcat_name")).append("</td></tr>\n");
				}
				if (!crs.getString("tickettype_name").equals("")) {
					Str.append("<tr><td  align=left valign=top>Type:&nbsp </td><td align=left valign=top >").append(crs.getString("tickettype_name")).append("</td></tr>\n");
				}
				Str.append("<tr><td  align=left valign=top>Source</td><td align=left valign=top >").append(crs.getString("ticketsource_name")).append("</td></tr>\n");
				if (!crs.getString("ticket_reopened_emp_id").equals("0")) {
					Str.append("<tr><td  align=left valign=top >Reopened Comments: </td><td align=left valign=top >").append(crs.getString("ticket_reopened_comments")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top >Reopened By: </td><td align=left valign=top >").append(crs.getString("reopen_by")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top >Reopened Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_reopened_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_closed_emp_id").equals("0")) {
					Str.append("<tr><td  align=left valign=top >Closed Comments: </td><td align=left valign=top >").append(crs.getString("ticket_closed_comments")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top >Closed By: </td><td align=left valign=top >").append(crs.getString("closed_by")).append("</td></tr>\n");
					Str.append("<tr><td  align=left valign=top >Closed Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_closed_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_notes").equals("")) {
					Str.append("<tr><td  align=left valign=top>Notes: &nbsp</td><td align=left valign=top >").append(crs.getString("ticket_notes")).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_entry_id").equals("0")) {
					Str.append("<tr><td  align=left valign=top>Entry By:&nbsp </td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_entry_id"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_entry_date").equals("")) {
					Str.append("<tr><td  align=left valign=top>Entry Time:&nbsp</td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_entry_date"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_modified_id").equals("0")) {
					Str.append("<tr><td  align=left valign=top>Modified By: &nbsp</td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_modified_id"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_modified_date").equals("")) {
					Str.append("<tr><td  align=left valign=top>Modified Time:&nbsp </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_modified_date")))
							.append("</td></tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				}

				StrSql = "SELECT tickettrans_followup, tickettrans_entry_date"
						+ " FROM " + compdb(comp_id) + "axela_service_ticket_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket on ticket_id=tickettrans_ticket_id"
						+ " WHERE tickettrans_ticket_id='" + ticket_id + "'"
						+ " ORDER BY tickettrans_id DESC";
				// SOP("StrSql = " + StrSql);
				CachedRowSet crs1 = processQuery(StrSql, 0);
				if (crs1.isBeforeFirst()) {
					// Str.append("<tr><td colspan=2>");
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr><th colspan=2 align=center valign=top>Ticket Follow-up</th></tr></thead><tbody>\n");
					while (crs1.next()) {
						Str.append("<tr><td width=25% align=left valign=top>Time: ").append(strToLongDate(crs1.getString("tickettrans_entry_date")))
								.append("</td><td width=75% align=left valign=top >").append(unescapehtml(crs1.getString("tickettrans_followup"))).append("</td></tr>\n");
					}
					Str.append("</tbody></table></div>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs1.close();

				// Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
