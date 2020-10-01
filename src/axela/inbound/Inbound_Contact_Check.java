/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.inbound;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inbound_Contact_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String contact = "", branch = "";
	public String type_id = "0";
	public String executive_sendsms = "";
	public String exe_id, contact_name, contact_mobile = "", contact_id = "", customer_id = "", call_id = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			contact = PadQuotes(request.getParameter("contact"));
			branch = PadQuotes(request.getParameter("branch"));
			type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
			executive_sendsms = PadQuotes(request.getParameter("executivesendsms"));
			if (branch.contains("-")) {
				branch = branch.replace("-", "");
			}
			if (branch.contains(" ")) {
				branch = branch.replaceAll("\\s+", "");
			}
			if (!contact.equals("")) {
				StrHTML = SearchContact();
			}
			else if (type_id.equals("1")) {
				StrHTML = SearchEmployee();
			} else if (type_id.equals("2")) {
				StrHTML = SearchBranch();
			} else {
				StrHTML = SearchEmployee();
			}

			if (executive_sendsms.equals("yes")) {
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				call_id = CNumeric(PadQuotes(request.getParameter("call_id")));
				contact_name = PadQuotes(request.getParameter("contact_name"));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				contact_mobile = PadQuotes(request.getParameter("contact_mobile"));
				StrHTML = new Executive_Send_Sms().PopulateconFigDetails(request, comp_id, exe_id, call_id, customer_id, contact_id, contact_name, contact_mobile);
			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String SearchContact() {
		StringBuilder Str = new StringBuilder();
		String address = "";
		String active = "";
		String Img = "";
		StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc, ' ', contact_fname, ' ',contact_lname) AS contact_name,"
				+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2,"
				+ " contact_jobtitle,"
				// + " contact_company, "
				+ " contact_phone1, contact_phone2,"
				// + " contact_photo,"
				+ " contact_notes, customer_id, customer_name, contact_msn,"
				+ " contact_active, contact_dob, contact_anniversary, contact_yahoo,"
				+ " contact_aol, contact_address, contact_pin, contact_landmark,"
				+ " COALESCE(city_name, '') AS city_name, "
				+ " (SELECT count(ticket_id) FROM " + compdb(comp_id) + "axela_service_ticket"
				+ " WHERE ticket_customer_id = customer_id AND ticket_ticketstatus_id != 3) AS ticketopen "
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
				+ " WHERE 1 = 1 "
				+ " AND (contact_mobile1 = '" + contact + "'"
				+ " OR contact_mobile2 = '" + contact + "'"
				+ " OR contact_phone1 = '" + contact + "'"
				+ " OR contact_phone2 = '" + contact + "')"
				+ " GROUP BY contact_id"
				+ " ORDER BY contact_id DESC "
				+ " LIMIT 10";
		// // SOP("StrSql----------------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Contact Person</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Communication</th>\n");
				Str.append("<th data-hide=\"phone\">Address</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Open Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("contact_active").equals("0")) {
						active = " <font color=red><b>[Inactive]</b></font>";
					} else {
						active = "";
					}
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("contact_id") + ")'"
							+ " onmouseout='HideCustomerInfo(" + crs.getString("contact_id") + ")' style='height:200px'>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");

					Str.append("<td valign=top align=left>");
					Str.append(Img).append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">")
							.append(crs.getString("contact_name")).append("</a> ").append(active).append("");
					if (!crs.getString("contact_jobtitle").equals("")) {
						Str.append("<br>").append(crs.getString("contact_jobtitle"));
					}
					if (!crs.getString("contact_dob").equals("")) {
						Str.append("<br>DOB: ").append(strToShortDate(crs.getString("contact_dob")));
					}
					if (!crs.getString("contact_anniversary").equals("")) {
						Str.append("<br>Anniversary: ").append(strToShortDate(crs.getString("contact_anniversary")));
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left><a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">")
							.append(crs.getString("customer_name")).append("</a></td>");
					Str.append("<td valign=top align=left nowrap>");
					if (!crs.getString("contact_phone1").equals("")) {
						Str.append(SplitPhoneNoSpan(crs.getString("contact_phone1"), 10, "T", crs.getString("contact_id")))
								.append(ClickToCall(crs.getString("contact_phone1"), comp_id)).append("<br />");
					}
					if (!crs.getString("contact_phone2").equals("")) {
						Str.append(SplitPhoneNoSpan(crs.getString("contact_phone2"), 10, "T", crs.getString("contact_id")))
								.append(ClickToCall(crs.getString("contact_phone2"), comp_id)).append("<br />");
					}
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("contact_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("<br />");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("contact_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("<br />");
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<span class='customer_info customer_" + crs.getString("contact_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>").append("<br />");
					}
					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<span class='customer_info customer_" + crs.getString("contact_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>").append("<br />");
					}
					if (!crs.getString("contact_yahoo").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("contact_yahoo")).append(">").append(crs.getString("contact_yahoo")).append("</a><br>");
					}
					if (!crs.getString("contact_msn").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("contact_msn")).append(">").append(crs.getString("contact_msn")).append("</a><br>");
					}
					if (!crs.getString("contact_aol").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("contact_aol")).append(">").append(crs.getString("contact_aol")).append("</a><br>");
					}
					Str.append("</td><td valign=top align=left>");
					address = crs.getString("contact_address");
					if (!address.equals("")) {
						address = crs.getString("contact_address");
						if (!crs.getString("city_name").equals("")) {
							address = address + ", " + crs.getString("city_name");
						}
						address = address + " - " + crs.getString("contact_pin");
						if (!crs.getString("contact_landmark").equals("")) {
							address = address + "<br>Landmark: " + crs.getString("contact_landmark");
						}
					}
					Str.append(address);
					Str.append("</td>\n");
					Str.append("<td valign=top align=left nowrap><a href=../service/ticket-list.jsp?customer_id=" + crs.getString("customer_id") + " >Tickets (" + crs.getString("ticketopen")
							+ ")</a>" + "</td>");
					Str.append("<td valign=top align=left nowrap>");
					Str.append("<a href=\"../sales/enquiry-quickadd.jsp?contact_id=").append(crs.getString("contact_id")).append("\">Add Enquiry</a>");
					Str.append("<br><a href=\"../sales/enquiry-list.jsp?enquiry_customer_id=" + crs.getString("customer_id")).append("\">List Enquiry</a>");
					Str.append("<br><a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Ticket</a>"
							+ "<br><a href=\"../service/ticket-list.jsp?ticket_customer_id=" + crs.getString("customer_id") + "\">List Tickets</a>"
							+ "");
					Str.append("<br><a href=../service/executive-send-sms.jsp?exe_id=&contact_id=")
							.append(crs.getString("contact_id"))
							.append("&contact_name=").append(crs.getString("contact_name").replace(" ", "*") + "")
							.append("&contact_mobile=").append(crs.getString("contact_mobile1"))
							.append(" data-target=\"#Hintclicktocall\" data-toggle=\"modal\" style=\"margin-top: 1px;\"")
							.append("\">Send SMS</a></td></tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				// SOP("===" + contact);
				Str.append("<div class=\"form-element2\"></div>");
				Str.append("<div class=\"form-element8\"><center><b><font color=\"#ff0000\">No Contacts Found!</font></b></center></div>");

				if (!contact.equals("")) {

					// SOP("contact===" + contact);
					if (IsValidMobileNo11(contact)) {
						Str.append("<div class=\"form-element2\" style=\"text-align: right;\">");
						Str.append("<a href=\"../service/ticket-add.jsp?add=yes&contact=yes&contact_mobile=").append(contact).append("\" target=_blank>Add Ticket</a>");
						Str.append("</div>");
					}
					if (IsValidPhoneNo11(contact) && !IsValidMobileNo11(contact)) {
						Str.append("<div class=\"form-element2\" style=\"text-align: right;\">");
						Str.append("<a href=\"../service/ticket-add.jsp?add=yes&contact=yes&contact_phno=").append(contact).append("\" target=_blank>Add Ticket</a>");
						Str.append("</div>");
					}
				}
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String SearchBranch() {
		StringBuilder Str = new StringBuilder();
		String address = "";
		String active = "";
		String Img = "";
		StrSql = "SELECT branch_id, branch_name, branch_add, branch_pin, branch_phone1, branch_phone2,"
				+ " branch_mobile1, branch_mobile2, branch_email1, branch_email2, branch_active, city_name, state_name"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
				+ " WHERE (branch_id LIKE '%" + branch + "%'"
				+ " OR branch_name LIKE '%" + branch + "%' "
				+ " OR branch_invoice_name LIKE '%" + branch + "%'"
				+ " OR branch_code LIKE '%" + branch + "%'"
				+ " OR branch_add LIKE '%" + branch + "%'"
				+ " OR branch_phone1 LIKE '%" + branch + "%'"
				+ " OR branch_phone2 LIKE '%" + branch + "%'"
				+ " OR branch_mobile1 LIKE '%" + branch + "%'"
				+ " OR branch_mobile2 LIKE '%" + branch + "%'"
				+ " OR branch_email1 LIKE '%" + branch + "%'"
				+ " OR city_name LIKE '%" + branch + "%'"
				+ " OR branch_pin LIKE '%" + branch + "%'"
				+ " OR branch_email2 LIKE '%" + branch + "%'"
				+ " OR branch_sales_email LIKE '%" + branch + "%'"
				+ " OR branch_vat LIKE '%" + branch + "%'"
				+ " OR branch_cst LIKE '%" + branch + "%'"
				+ " OR branch_pan LIKE '%" + branch + "%'"
				+ " OR branch_quote_prefix LIKE '%" + branch + "%'"
				+ " OR city_name LIKE '%" + branch + "%')"
				+ " GROUP BY branch_id"
				+ " ORDER BY branch_id desc "
				+ " LIMIT 10";
		// // SOP("StrSql-----------123-----" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Branch Address</th>\n");
				// Str.append("<th>Contact Person</th>\n");
				Str.append("<th data-hide=\"phone\">Mobile</th>\n");
				Str.append("<th data-hide=\"phone\">Email</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("branch_active").equals("0")) {
						active = " <font color=red><b>[Inactive]</b></font>";
					} else {
						active = "";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("branch_name"));
					if (!crs.getString("branch_add").equals("")) {
						Str.append("<br>").append(crs.getString("branch_add")).append(", ");
					}
					Str.append("<br>").append(crs.getString("city_name")).append("");
					if (!crs.getString("branch_pin").equals("")) {
						Str.append(" - ").append(crs.getString("branch_pin")).append("");
					}
					Str.append("</br>" + active);
					Str.append("</td>\n");

					Str.append("<td valign=top align=left>");
					if (!crs.getString("branch_phone1").equals("")) {
						Str.append(SplitPhoneNo(crs.getString("branch_phone1"), 4, "T")).append("<br>");
					}
					if (!crs.getString("branch_phone2").equals("")) {
						Str.append(SplitPhoneNo(crs.getString("branch_phone2"), 4, "T")).append("<br>");
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						Str.append(SplitPhoneNo(crs.getString("branch_mobile1"), 5, "M")).append("<br>");
					}
					if (!crs.getString("branch_mobile2").equals("")) {
						Str.append(SplitPhoneNo(crs.getString("branch_mobile2"), 5, "M")).append("<br>");
					}
					Str.append("</td>");

					Str.append("<td valign=top align=left nowrap>");
					if (!crs.getString("branch_email1").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("branch_email1")).append(">").append(crs.getString("branch_email1")).append("</a><br>");
					}
					if (!crs.getString("branch_email2").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("branch_email2")).append(">").append(crs.getString("branch_email2")).append("</a><br>");
					}
					Str.append("</td>");

					Str.append("<td valign=top align=left nowrap>");
					Str.append("<a href=\"../portal/branch-update.jsp?update=yes&branch_id=").append(crs.getString("branch_id")).append("\">Update Branch</a>");
					Str.append("</td></tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=\"#ff0000\">No Branches Found!</font></b>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String SearchEmployee() {
		StringBuilder Str = new StringBuilder();
		String address = "";
		String active = "";
		String Img = "";
		StrSql = "Select emp_id, emp_name, emp_ref_no, emp_phone1,emp_phone2, emp_mobile1, emp_mobile2, "
				+ "emp_email1, emp_email2, emp_address, emp_city, emp_state, emp_pin, jobtitle_desc, emp_active "
				+ " from " + compdb(comp_id) + "axela_emp"
				+ " INNER join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
				+ " where (emp_id like '%" + branch + "%' OR emp_name like '%" + branch + "%'"
				+ " OR emp_ref_no like '%" + branch + "%' OR emp_phone1 like '%" + branch + "%'"
				+ " OR emp_phone2 like '%" + branch + "%' OR emp_mobile1 like '%" + branch + "%'"
				+ " OR emp_mobile2 like '%" + branch + "%' OR emp_email1 like '%" + branch + "%' "
				+ " OR emp_email2 like '%" + branch + "%' OR emp_address like '%" + branch + "%'"
				+ " OR emp_city like '%" + branch + "%' OR emp_state like '%" + branch + "%' "
				+ " OR emp_email2 like '%" + branch + "%' OR emp_pin like '%" + branch + "%'  "
				+ " OR jobtitle_desc LIKE '%" + branch + "%'"
				+ ")"
				+ " GROUP BY emp_id"
				+ " order by emp_name "
				+ " limit 10";
		// // SOP("StrSql-------employee---------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th data-hide=\"phone\">Mobile</th>\n");
				Str.append("<th data-hide=\"phone\">Phone</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Email</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Address</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("emp_active").equals("0")) {
						active = " <font color=red><b>[Inactive]</b></font>";
					} else {
						active = "";
					}
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");

					Str.append("<td valign=top align=left>");
					Str.append(Img).append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append("\">")
							.append(crs.getString("emp_name")).append("</a> ").append(active).append("");
					if (!crs.getString("jobtitle_desc").equals("")) {
						Str.append("<br>").append(crs.getString("jobtitle_desc"));
					}
					Str.append("</td>\n");

					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_ref_no")).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_mobile1"));
					if (!crs.getString("emp_mobile2").equals("")) {
						Str.append("<br>").append(crs.getString("emp_mobile2"));
					}
					Str.append("</td>\n");

					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_phone1"));
					if (!crs.getString("emp_phone2").equals("")) {
						Str.append("<br>").append(crs.getString("emp_phone2"));
					}
					Str.append("</td>\n");

					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_email1"));
					if (!crs.getString("emp_email2").equals("")) {
						Str.append("<br>").append(crs.getString("emp_email2"));
					}
					Str.append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_address"));
					if (!crs.getString("emp_state").equals("")) {
						Str.append("<br>").append(crs.getString("emp_state"));
					}
					if (!crs.getString("emp_city").equals("")) {
						Str.append("<br>").append(crs.getString("emp_city"));
					}
					if (!crs.getString("emp_pin").equals("")) {
						Str.append("<br>").append(crs.getString("emp_pin"));
					}
					Str.append("</td>\n");

					Str.append("<td valign=top align=center nowrap>");
					Str.append("<input name=\"button\" type=\"button\" class=\"btn btn-success\" id=\"button\" value=\"Send SMS\" onclick=\"smsPage(" + crs.getString("emp_id") + ");\"/>"
							+ "</td>\n");

					// Str.append("<br><a href=\"../sales/enquiry-list.jsp?enquiry_customer_id=" + crs.getString("customer_id")).append("\">List Enquiry</a>");
					// Str.append("<br><a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Ticket</a>"
					// + "<br><a href=\"../service/ticket-list.jsp?ticket_customer_id=" + crs.getString("customer_id") + "\">List Tickets</a>"
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=\"#ff0000\">No Employees Found!</font></b>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
