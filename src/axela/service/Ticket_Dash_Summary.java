package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Dash_Summary extends Connect {

	public String StrHTML1 = "";
	public String StrHTML2 = "";
	public String StrHTML3 = "";
	public String StrSql = "";
	public String ticket_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String ExeAccess = "";
	public String msg = "";
	public String ticket_subject = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));

				ExeAccess = GetSession("ExeAccess", request);
				// CheckPerm(comp_id, "emp_ticket_access", request, response);
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				msg = PadQuotes(request.getParameter("msg"));
				StrSql = "select ticket_subject "
						+ " from " + compdb(comp_id) + "axela_service_ticket "
						+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id "
						+ " where 1=1 and ticket_id =" + ticket_id + ExeAccess + ""
						+ " group by ticket_id "
						+ " order by ticket_id desc ";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						ticket_subject = crs.getString("ticket_subject");
					}
					TicketSummary();
					CustomerSummary();
					ContactSummary();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Ticket!");
				}
				crs.close();
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

	public void TicketSummary() {
		StringBuilder Str1 = new StringBuilder();
		StrSql = "select ticket_id, ticket_read, ticket_closed_time, ticket_due_time, ticket_trigger, ticket_desc, ticket_report_time,  "
				+ " ticket_closed_emp_id, ticket_closed_time, ticket_closed_comments, concat(c.emp_name, '(', c.emp_ref_no, ')') as closed_by, ticket_notes, "
				+ " ticket_reopened_emp_id, concat(o.emp_name, '(', o.emp_ref_no, ')') as reopen_by, ticket_reopened_comments, ticket_reopened_time, "
				+ " ticket_priorityticket_id, ticket_subject, ticket_emp_id, ticket_entry_id, ticket_entry_date, ticket_modified_id, ticket_modified_date, "
				+ " ticket_dept_name, coalesce(priorityticket_name,'-') as priorityticket_name, coalesce(ticketsource_name,'-') as ticketsource_name, ticketstatus_name, "
				+ " coalesce(tickettype_name,'-') as tickettype_name"
				+ " from " + compdb(comp_id) + "axela_service_ticket "
				+ " inner join " + compdb(comp_id) + "axela_service_ticket_priority on ticket_priorityticket_id = priorityticket_id "
				+ " inner join " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
				+ " inner join " + compdb(comp_id) + "axela_service_ticket_status on ticket_ticketstatus_id = ticketstatus_id "
				+ " inner join " + compdb(comp_id) + "axela_service_ticket_source on ticketsource_id=ticket_ticketsource_id "
				+ " left join " + compdb(comp_id) + "axela_emp as c on c.emp_id=ticket_closed_emp_id"
				+ " left join " + compdb(comp_id) + "axela_emp as o on o.emp_id=ticket_reopened_emp_id"
				+ " left join " + compdb(comp_id) + "axela_service_ticket_type on ticket_tickettype_id = tickettype_id "
				+ " where ticket_id = " + ticket_id + ExeAccess.replace("emp_id", "ticket_emp_id");
		// SOP("StrSql--"+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {

				Str1.append("<table width=100% border=1 cellpadding=0 cellspacing=0 class=listtable>\n");
				Str1.append("<tr><th colspan=2>Ticket Summary</th>\n</tr>");
				Str1.append("<tr><td width=25% align=left valign=top ><b> Ticket ID: </b></td><td width=75% valign=top align=left ><b>").append(crs.getString("ticket_id")).append("</b> </td></tr>\n");
				Str1.append("<tr><td  align=left valign=top ><b>Subject: </b></td><td align=left valign=top ><b>").append(crs.getString("ticket_subject")).append("</b></td></tr>\n");
				Str1.append("<tr><td  align=left valign=top>Description: </td><td align=left valign=top >").append(crs.getString("ticket_desc")).append("</td></tr>\n");
				Str1.append("<tr><td  align=left valign=top>Status: </td><td align=left  valign=top >").append(crs.getString("ticketstatus_name")).append("</td></tr>\n");

				if (crs.getString("ticket_dept_name") != null) {
					Str1.append("<tr><td  align=left valign=top>Department: </td><td align=left  valign=top >").append(crs.getString("ticket_dept_name")).append("</td></tr>\n");
				}
				Str1.append("<tr><td  align=left valign=top>Level: </td><td align=left  valign=top >").append(crs.getString("ticket_trigger")).append("</td></tr>\n");
				if (crs.getString("priorityticket_name") != null) {
					Str1.append("<tr><td  align=left valign=top>Priority: </td><td align=left  valign=top >").append(crs.getString("priorityticket_name")).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_report_time").equals("")) {
					Str1.append("<tr><td  align=left valign=top>Report Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_due_time").equals("")) {
					Str1.append("<tr><td  align=left valign=top>Due Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_due_time"))).append("</td></tr>\n");
				}
				Str1.append("<tr><td  align=left valign=top>Execuitve: </td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_emp_id"))).append("</td></tr>\n");
				if (crs.getString("tickettype_name") != null) {
					Str1.append("<tr><td  align=left valign=top>Type: </td><td align=left valign=top >").append(crs.getString("tickettype_name")).append("</td></tr>\n");
				}

				if (crs.getString("ticketsource_name") != null) {
					Str1.append("<tr><td  align=left valign=top>Source</td><td align=left valign=top >").append(crs.getString("ticketsource_name")).append("</td></tr>\n");
				}

				if (!crs.getString("ticket_reopened_emp_id").equals("0")) {
					Str1.append("<tr><td  align=left valign=top >Reopened Comments: </td><td align=left valign=top >").append(crs.getString("ticket_reopened_comments")).append("</td></tr>\n");
					Str1.append("<tr><td  align=left valign=top >Reopened By: </td><td align=left valign=top >").append(crs.getString("reopen_by")).append("</td></tr>\n");
					Str1.append("<tr><td  align=left valign=top >Reopened Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_reopened_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_closed_emp_id").equals("0")) {
					Str1.append("<tr><td  align=left valign=top >Closed Comments: </td><td align=left valign=top >").append(crs.getString("ticket_closed_comments")).append("</td></tr>\n");
					Str1.append("<tr><td  align=left valign=top >Closed By: </td><td align=left valign=top >").append(crs.getString("closed_by")).append("</td></tr>\n");
					Str1.append("<tr><td  align=left valign=top >Closed Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_closed_time"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_notes").equals("")) {
					Str1.append("<tr><td  align=left valign=top>Notes: </td><td align=left valign=top >").append(crs.getString("ticket_notes")).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_entry_id").equals("0")) {
					Str1.append("<tr><td  align=left valign=top>Entry By: </td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_entry_id"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_entry_date").equals("")) {
					Str1.append("<tr><td  align=left valign=top>Entry Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_entry_date"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_modified_id").equals("0")) {
					Str1.append("<tr><td  align=left valign=top>Modified By: </td><td align=left valign=top >").append(Exename(comp_id, crs.getInt("ticket_modified_id"))).append("</td></tr>\n");
				}
				if (!crs.getString("ticket_modified_date").equals("")) {
					Str1.append("<tr><td  align=left valign=top>Modified Time: </td><td align=left valign=top >").append(strToLongDate(crs.getString("ticket_modified_date"))).append("</td></tr>\n");
				}

				if (crs.getString("ticket_read").equals("0") && crs.getString("ticket_emp_id").equals(emp_id)) {
					StrSql = "update " + compdb(comp_id) + "axela_service_ticket set ticket_read='1' where ticket_id=" + ticket_id;
					updateQuery(StrSql);
				}
				Str1.append("</table>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		StrHTML1 = Str1.toString();
	}

	public void CustomerSummary() {
		StringBuilder Str2 = new StringBuilder();
		StrSql = "select ticket_customer_id, customer_name, customer_active, customer_mobile1, customer_mobile2, customer_phone1, customer_phone2, customer_email1, customer_email2, customer_address, city_name, customer_pin "
				+ " from " + compdb(comp_id) + "axela_service_ticket "
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id=ticket_customer_id "
				+ " left join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
				+ " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id "
				+ " where ticket_id = " + ticket_id + "";

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str2.append("<table width=100% border=1 cellpadding=0 cellspacing=0 class=listtable> \n");
				Str2.append("<tr><th colspan=2>Customer Summary</font></th></tr>\n");
				Str2.append(
						"<tr><td width=25%  align=left valign=top>Customer Name: </td><td valign=top width=75% align=left ><a href=\"javascript:remote=window.open('../customer/customer-list.jsp?customer_id=")
						.append(crs.getString("ticket_customer_id")).append("','studentpop','');remote.focus();\">").append(crs.getString("customer_name")).append(" (")
						.append(crs.getString("ticket_customer_id")).append(")</a></td></tr>\n");
				if (crs.getString("customer_address") != null && !crs.getString("customer_address").equals("")) {
					String address = crs.getString("customer_address");
					address = crs.getString("customer_address");
					address = address + ", " + crs.getString("city_name");
					address = address + " - " + crs.getString("customer_pin") + ".";
					Str2.append("<tr><td  align=left valign=top >Address: </td><td align=left valign=top >").append(address).append("</td></tr>\n");
				}
				if (!crs.getString("customer_mobile1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Mobile: </td><td align=left valign=top >").append(SplitPhoneNo(crs.getString("customer_mobile1"), 5, "M")).append("");
					if (!crs.getString("customer_mobile2").equals("")) {
						Str2.append("; ").append(SplitPhoneNo(crs.getString("customer_mobile2"), 5, "M"));
					}
					Str2.append("</td></tr>\n");
				}
				if (!crs.getString("customer_phone1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Phone: </td><td align=left valign=top >").append(SplitPhoneNo(crs.getString("customer_phone1"), 4, "T")).append("");
					if (!crs.getString("customer_phone2").equals("")) {
						Str2.append("; ").append(SplitPhoneNo(crs.getString("customer_phone2"), 4, "T"));
					}
					Str2.append("</td></tr>\n");
				}
				if (!crs.getString("customer_email1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Email: </td><td align=left valign=top >" + "<a href=mailto:").append(crs.getString("customer_email1")).append(">")
							.append(crs.getString("customer_email1")).append("</a>");
					if (!crs.getString("customer_email2").equals("")) {
						Str2.append("; <a href=mailto:").append(crs.getString("customer_email2")).append(">").append(crs.getString("customer_email2")).append("</a>");
					}
					Str2.append("</td></tr>\n");
				}
			}
			Str2.append("</table>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		StrHTML2 = Str2.toString();
	}

	public void ContactSummary() {
		StringBuilder Str2 = new StringBuilder();
		StrSql = "select ticket_contact_id, concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name, contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, contact_email1, contact_email2, contact_address, city_name, contact_pin "
				+ " from " + compdb(comp_id) + "axela_service_ticket "
				+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id=ticket_contact_id "
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
				+ " left join " + compdb(comp_id) + "axela_city on city_id = contact_city_id "
				+ " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id "
				+ " where ticket_id = " + ticket_id + "";

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str2.append("<table width=100% border=1 cellpadding=0 cellspacing=0 class=listtable> \n");
				Str2.append("<tr><th colspan=2>Contact Summary</font></th></tr>\n");
				Str2.append(
						"<tr><td width=25%  align=left valign=top>Contact Name: </td><td valign=top width=75% align=left ><a href=\"javascript:remote=window.open('../customer/customer-contact-list.jsp?contact_id=")
						.append(crs.getString("ticket_contact_id")).append("','studentpop','');remote.focus();\">").append(crs.getString("contact_name")).append(" (")
						.append(crs.getString("ticket_contact_id")).append(")</a></td></tr>\n");
				if (crs.getString("contact_address") != null && !crs.getString("contact_address").equals("")) {
					String address = crs.getString("contact_address");
					address = crs.getString("contact_address");
					address = address + ", " + crs.getString("city_name");
					address = address + " - " + crs.getString("contact_pin") + ".";
					Str2.append("<tr><td  align=left valign=top >Address: </td><td align=left valign=top >").append(address).append("</td></tr>\n");
				}
				if (!crs.getString("contact_mobile1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Mobile: </td><td align=left valign=top >").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M")).append("");
					if (!crs.getString("contact_mobile2").equals("")) {
						Str2.append("; ").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
					}
					Str2.append("</td></tr>\n");
				}
				if (!crs.getString("contact_phone1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Phone: </td><td align=left valign=top >").append(SplitPhoneNo(crs.getString("contact_phone1"), 4, "T")).append("");
					if (!crs.getString("contact_phone2").equals("")) {
						Str2.append("; ").append(SplitPhoneNo(crs.getString("contact_phone2"), 4, "T"));
					}
					Str2.append("</td></tr>\n");
				}
				if (!crs.getString("contact_email1").equals("")) {
					Str2.append("<tr><td  align=left valign=top >Email: </td><td align=left valign=top >" + "<a href=mailto:").append(crs.getString("contact_email1")).append(">")
							.append(crs.getString("contact_email1")).append("</a>");
					if (!crs.getString("contact_email2").equals("")) {
						Str2.append("; <a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
					}
					Str2.append("</td></tr>\n");
				}
			}
			Str2.append("</table>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		StrHTML3 = Str2.toString();
	}
	// public String Follow-up() {
	// StringBuilder Str = new StringBuilder();
	// int TotalRecords = 0;
	// StrSql = "select tickettrans_id, tickettrans_followup, tickettrans_private, (case when tickettrans_private='1' then 'Yes' else 'No' end) as private, "
	// + " tickettrans_entry_id, concat(e.emp_name, '(', e.emp_ref_no, ')') as entry_by, tickettrans_entry_date, "
	// + " tickettrans_modified_id, concat(e.emp_name, '(', e.emp_ref_no, ')') as modified_by, tickettrans_modified_date "
	// + " from " + compdb(comp_id) + "axela_service_ticket_trans "
	// + " left join " + compdb(comp_id) + "axela_emp as e on e.emp_id=tickettrans_entry_id"
	// + " left join " + compdb(comp_id) + "axela_emp as m on m.emp_id=tickettrans_modified_id"
	// + " where tickettrans_ticket_id='" + ticket_id + "' "
	// + " order by tickettrans_id";
	// String CountSql = "select count(tickettrans_id) "
	// + "from " + compdb(comp_id) + "axela_service_ticket_trans "
	// + "where tickettrans_ticket_id='" + ticket_id + "'" ;
	// TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
	// if (TotalRecords != 0) {
	// //SOP("StrSql-----"+StrSql);
	// CachedRowSet crs = processQuery(StrSql, 0);
	// try {
	// Str.append("<table border=1 cellspacing=0 cellpadding=0 width=100%>");
	// while (crs.next()) {
	// String border = "";
	// if (!crs.getString("tickettrans_entry_id").equals("0") && crs.getString("tickettrans_private").equals("0")) {
	// border = "#809FFE"; //blue
	// } else if (!crs.getString("tickettrans_entry_id").equals("0") && crs.getString("tickettrans_private").equals("1")) {
	// border = "#FF0000"; //red
	// }
	// Str.append("<tr><td style=\"border:solid ").append(border).append("\">").append(crs.getString("tickettrans_followup")).append("");
	// Str.append("<br>Private: ").append(crs.getString("private")).append("");
	// if (!crs.getString("tickettrans_entry_id").equals("0")) {
	// Str.append("<br>Entry By: ").append(crs.getString("entry_by")).append("");
	// Str.append("<br>Entry Time: ").append(strToLongDate(crs.getString("tickettrans_entry_date"))).append("");
	// }
	// if (!crs.getString("tickettrans_modified_id").equals("0")) {
	// Str.append("<br>Modified By: ").append(crs.getString("modified_by")).append("");
	// Str.append("<br>Modified Time: ").append(strToLongDate(crs.getString("tickettrans_modified_date"))).append("");
	// }
	// Str.append("<br><a href=ticket-dash-followup-update.jsp?update=yes&tickettrans_id=").append(crs.getString("tickettrans_id")).append(">Update Follow-up</a>");
	// Str.append("</td></tr>\n");
	// }
	// Str.append("</table>\n");
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
	// return Str.toString();
	// }
}
