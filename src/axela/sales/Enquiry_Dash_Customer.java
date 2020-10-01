package axela.sales;
//Saiman 11th Feb 2013
//sangita 15th july 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Dash_Customer extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_enquirytype_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "";
	public String customer_name = "";
	public String customer_id = "0";
	public String customer_communication = "";
	public String customer_address = "";
	public String customer_landmark = "", emp_name = "", emp_ref_no = "0";
	public String customer_notes = "";
	public String customer_active = "";
	public String customer_exe = "";
	public String group = "";
	public String config_sales_enquiry = "";
	public String comp_email_enable = "";
	public String config_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sms_enable = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				group = PadQuotes(request.getParameter("group"));
				StrSql = "SELECT enquiry_title, enquiry_enquirytype_id, enquiry_customer_id,"
						+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
						+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY enquiry_id"
						+ " ORDER BY enquiry_id DESC";
				// SOP("");
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						enquiry_title = crs.getString("enquiry_title");
						enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
						customer_id = crs.getString("enquiry_customer_id");
					}
					// CustomerDetails(response,enquiry_customer_id);
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Opportunity!");
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

	public String CustomerDetails(HttpServletResponse response, String customer_id, String modal, String comp_id) {
		// SOP("comp_id=====" + comp_id);
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		if (!comp_id.equals("0"))
		{
			try {
				StrSql = "SELECT COALESCE (customer_name,'') AS customer_name,"
						+ " COALESCE (customer_address,'') AS customer_address,"
						+ " COALESCE (customer_landmark,'') AS customer_landmark,"
						+ " COALESCE (customer_phone1,'') AS customer_phone1,"
						+ " COALESCE (customer_phone2,'') AS customer_phone2 ,"
						+ " COALESCE (customer_phone3,'') AS customer_phone3 ,"
						+ " COALESCE (customer_phone4,'') AS customer_phone4 ,"
						+ " COALESCE (customer_mobile1,'') AS customer_mobile1,"
						+ " COALESCE (customer_mobile2,'') AS customer_mobile2,"
						+ " COALESCE (customer_fax1,'') AS customer_fax1,"
						+ " COALESCE (customer_fax2,'') AS customer_fax2,"
						+ " COALESCE (customer_email1,'') AS customer_email1,"
						+ " COALESCE (customer_email2,'') AS customer_email2,"
						+ " COALESCE (customer_emp_id,'') AS customer_emp_id,"
						+ " COALESCE (customer_website1,'') AS customer_website1,"
						+ " COALESCE (customer_website2,'') AS customer_website2,"
						+ " COALESCE (customer_pin,'') AS customer_pin,"
						+ " COALESCE (customer_notes,'') AS customer_notes,"
						+ " COALESCE (customer_active,'') AS customer_active,"
						+ " COALESCE (emp_name,'') AS emp_name ,"
						+ " COALESCE (emp_ref_no,'') AS emp_ref_no ,"
						+ " COALESCE(city_name, '') AS city_name"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id = customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
						+ " WHERE customer_id = " + customer_id + ""
						+ " GROUP BY customer_id";
				crs = processQuery(StrSql, 0);
				// SOP("StrSql===cust===" + StrSql);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						customer_name = crs.getString("customer_name");
						if (!crs.getString("customer_phone1").equals("")) {
							customer_communication = SplitPhoneNo(crs.getString("customer_phone1"), 4, "T") + ClickToCall(crs.getString("customer_phone1"), comp_id) + "";
						}

						if (!crs.getString("customer_phone2").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone2"), 4, "T")
										+ ClickToCall(crs.getString("customer_phone2"), comp_id) + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_phone2"), 4, "T") + ClickToCall(crs.getString("customer_phone2"), comp_id) + "";
							}
						}

						if (!crs.getString("customer_phone3").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone3"), 4, "T")
										+ ClickToCall(crs.getString("customer_phone3"), comp_id) + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_phone3"), 4, "T") + ClickToCall(crs.getString("customer_phone3"), comp_id) + "";
							}
						}

						if (!crs.getString("customer_phone4").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone4"), 4, "T")
										+ ClickToCall(crs.getString("customer_phone4"), comp_id) + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_phone4"), 4, "T") + ClickToCall(crs.getString("customer_phone4"), comp_id) + "";
							}
						}

						if (!crs.getString("customer_mobile1").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_mobile1"), 5, "M")
										+ ClickToCall(crs.getString("customer_mobile1"), comp_id) + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_mobile1"), 4, "T") + ClickToCall(crs.getString("customer_mobile1"), comp_id) + "";
							}
						}

						if (!crs.getString("customer_mobile2").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_mobile2"), 5, "M")
										+ ClickToCall(crs.getString("customer_mobile2"), comp_id) + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_mobile2"), 4, "T") + ClickToCall(crs.getString("customer_mobile2"), comp_id) + "";
							}
						}

						if (!crs.getString("customer_fax1").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_fax1"), 4, "F") + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_fax1"), 4, "T") + "";
							}
						}

						if (!crs.getString("customer_fax2").equals("")) {
							if (!customer_communication.equals("")) {
								customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_fax2"), 4, "F") + "";
							} else {
								customer_communication = SplitPhoneNo(crs.getString("customer_fax2"), 4, "T") + "";
							}
						}

						if (!crs.getString("customer_email1").equals("")) {
							customer_communication = customer_communication + "<br><a href=mailto:" + crs.getString("customer_email1") + ">" + crs.getString("customer_email1") + "</a>";
						}

						if (!crs.getString("customer_email2").equals("")) {
							customer_communication = customer_communication + "<br><a href=mailto:" + crs.getString("customer_email2") + ">" + crs.getString("customer_email2") + "</a>";
						}

						if (!crs.getString("customer_website1").equals("")) {
							customer_communication = customer_communication + "<br><a href=http://" + crs.getString("customer_website1") + " target=_blank>" + crs.getString("customer_website1")
									+ "</a>";
						}

						if (!crs.getString("customer_website2").equals("")) {
							customer_communication = customer_communication + "<br><a href=http://" + crs.getString("customer_website2") + " target=_blank>" + crs.getString("customer_website2")
									+ "</a>";
						}

						if (!crs.getString("customer_address").equals("")) {
							customer_address = crs.getString("customer_address");
							if (!crs.getString("city_name").equals("")) {
								customer_address = customer_address + ", " + crs.getString("city_name");
							}
							if (!crs.getString("customer_pin").equals("")) {
								customer_address = customer_address + " - " + crs.getString("customer_pin");
							}
						}

						if (!crs.getString("customer_landmark").equals("")) {
							customer_landmark = crs.getString("customer_landmark");
						}

						if (!crs.getString("emp_name").equals("")) {
							emp_name = crs.getString("emp_name");
						}
						if (!crs.getString("emp_ref_no").equals("0")) {
							emp_ref_no = crs.getString("emp_ref_no");
						}
						if (crs.getString("customer_active").equals("0")) {
							customer_active = "<font color=red><b>Inactive</b></font>";
						} else {
							customer_active = "Active";
						}
						customer_notes = crs.getString("customer_notes");
					}
					if (modal.equals("yes")) {
						Str.append("<a href=\"javascript:window.parent.AddNewContact(").append(customer_id).append(");\" id=\"new_contact_link\" style=\"float:right\">Add New Contact...</a>");
						Str.append("<div id=\"dialog-modal-contact\"></div>");
					} else {
						Str.append("<a href=\"../customer/customer-contact-update.jsp?Add=yes&customer_id=").append(customer_id).append("\" style=\"float:right\">Add New Contact...</a>\n");
					}
					Str.append("<br><div class=\"container-fluid portlet box\">");
					Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
					Str.append("<div class=\"caption\" style=\"float: none\">Customer</div></div>");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=\"\">");
					// Str.append("<div class=\"portlet-body portlet-empty\">");
					// Str.append(" <div class=\"tab-pane\" id=\"\">");
					Str.append("<div class=\"table-responsive \">\n");
					Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<tbody>\n");
					/*
					 * Str.append("<tr>\n"); Str.append("<td colspan=\"2\" align=\"right\">\n");
					 * 
					 * Str.append("</td>\n"); Str.append("</tr>\n");
					 */
					/*
					 * Str.append("<tr>\n"); // Str.append("<th colspan=\"2\">Customer</th>\n"); Str.append("</tr>\n");
					 */
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" colspan=\"2\"><a href=\"../customer/customer-list.jsp?customer_id=").append(customer_id).append("\"> <b>").append(customer_name).append(" (")
							.append(customer_id).append(")</b></a></td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" width=\"100px\">Communication:</td>\n");
					Str.append("<td>").append(customer_communication).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\">Address:</td>\n");
					Str.append("<td>").append(customer_address).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\">Landmark:</td>\n");
					Str.append("<td>").append(customer_landmark).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\">Sales Consultant:</td>\n");
					Str.append("<td>").append(emp_name).append(" (").append(emp_ref_no).append(")").append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\">Active:</td>\n");
					Str.append("<td>").append(customer_active).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\">Notes:</td>\n");
					Str.append("<td>").append(customer_notes).append("</td>\n");
					Str.append("</tr>\n");

					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<tr>\n");
					Str.append("<td colspan=\"2\" align=\"center\">").append(ListContact(customer_id, comp_id)).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");

				} else {
					msg = "<br><br><br><br><center><font color=red><b>No Customer found!</b></font></center><br><br><br><br>";
					Str.append(msg);
					// response.sendRedirect(response.encodeRedirectURL("enquiry-dash-customer.jsp?enquiry_id="+enquiry_id+"&msg=No Customer found!"));
				}

				crs.close();

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
	public String ListContact(String enquiry_customer_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		String active = "";
		String address = "", img = "";
		try {
			StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,"
					+ " contact_jobtitle, contact_phone1,"
					+ " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
					+ " contact_email1, contact_email2, contact_yahoo, contact_msn, contact_aol,"
					+ " contact_address, contact_pin, contact_landmark, contact_dob, contact_active,"
					+ " COALESCE(city_name,'') AS city_name, customer_id, customer_name, coalesce(branch_name,'')as branch,"
					+ " COALESCE(branch_code,'')AS branch_code"
					// +
					// "contact_photo,  contact_company, contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " WHERE contact_customer_id = " + enquiry_customer_id + BranchAccess;
			// SOP("StrSql--"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<thead><tr>");
			Str.append("<th data-toggle=\"true\">#</th>\n");
			Str.append("<th>ID</th>\n");
			Str.append("<th>Contact Person</th>\n");
			Str.append("<th data-hide=\"phone\">Communication</th>\n");
			Str.append("<th data-hide=\"phone\">Address</th>\n");
			// Str.append("<th>Type</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			while (crs.next()) {
				count = count + 1;
				if (crs.getString("contact_active").equals("0")) {
					active = "<font color=red><b>&nbsp;[Inactive]</b></font>";
				} else {
					active = "";
				}
				// if (crs.getString("contact_photo").equals("")) {
				// img = "";
				// } else {
				// img = "<a href=../Thumbnail.do?contactphoto=" +
				// crs.getString("contact_photo") +
				// " target=_blank><img src=../Thumbnail.do?contactphoto=" +
				// crs.getString("contact_photo")
				// + "&width=200 alt=" + crs.getString("contact_name") +
				// "></a><br>";
				// }
				Str.append("<tr>\n");
				Str.append("<td valign=top align=center >").append(count).append("</td>\n");
				Str.append("<td valign=top align=center nowrap><a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append(">");
				Str.append(crs.getString("contact_id")).append("</a>");
				Str.append("</td>");
				Str.append("<td valign=top align=left>");

				// if (!crs.getString("contact_photo").equals("")){
				// Str.append(crs.getString("contact_photo"));
				// }
				Str.append(img).append(crs.getString("contact_name")).append(active);

				if (!crs.getString("contact_jobtitle").equals("")) {
					Str.append("<br>").append(crs.getString("contact_jobtitle"));
				}

				// if (!crs.getString("contact_company").equals("")) {
				// Str.append("<br>").append(crs.getString("contact_company"));
				// }
				Str.append("</td><td valign=top align=left nowrap>");
				if (!crs.getString("contact_phone1").equals("")) {
					Str.append("").append(crs.getString("contact_phone1")).append(ClickToCall(crs.getString("contact_phone1"), comp_id)).append("<br>");
				}

				if (!crs.getString("contact_phone2").equals("")) {
					Str.append("").append(crs.getString("contact_phone2")).append(ClickToCall(crs.getString("contact_phone2"), comp_id)).append("<br>");
				}

				if (!crs.getString("contact_mobile1").equals("")) {
					Str.append("").append(crs.getString("contact_mobile1")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("<br>");
				}

				if (!crs.getString("contact_mobile2").equals("")) {
					Str.append("").append(crs.getString("contact_mobile2")).append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("<br>");
				}

				if (!crs.getString("contact_email1").equals("")) {
					Str.append("" + "<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a><br>");
				}

				if (!crs.getString("contact_email2").equals("")) {
					Str.append("" + "<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a><br>");
				}

				if (!crs.getString("contact_yahoo").equals("")) {
					Str.append("" + "<a href=mailto:").append(crs.getString("contact_yahoo")).append(">").append(crs.getString("contact_yahoo")).append("</a><br>");
				}

				if (!crs.getString("contact_msn").equals("")) {
					Str.append("" + "<a href=mailto:").append(crs.getString("contact_msn")).append(">").append(crs.getString("contact_msn")).append("</a><br>");
				}

				if (!crs.getString("contact_aol").equals("")) {
					Str.append("" + "<a href=mailto:").append(crs.getString("contact_aol")).append(">").append(crs.getString("contact_aol")).append("</a><br>");
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
				// Str.append("<td valign=top align=left>").append(crs.getString("contacttype_name")).append("</td>\n");

				Str.append("<td valign=top align=left nowrap>");
				Str.append("<a href=\"../customer/customer-contact-update.jsp?update=yes&customer_id=").append(crs.getString("customer_id")).append("&contact_id=").append(crs.getString("contact_id"))
						.append(" \">Update Contact Person</a><br>");
				if (config_sales_enquiry.equals("1")) {
					Str.append("<a href=\"../sales/enquiry-quickadd.jsp?Add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Opportunity</a><br>");
				}
				// Str.append("<a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Ticket</a><br>");
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")) {
					Str.append("<a href=../portal/email-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send Email</a><br>");
				}
				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
					Str.append("<a href=../portal/sms-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send SMS</a><br>");
				}
				Str.append(" <br><br>\n");
				Str.append("</td>");
				Str.append("</tr>");
			}
			Str.append("</tbody>\n");
			// Str.append("</table>\n");
			// Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void PopulateConfigDetails() {

		StrSql = "SELECT COALESCE(comp_email_enable,'') AS comp_email_enable,"
				+ " COALESCE(comp_sms_enable,'') AS comp_sms_enable,"
				+ " COALESCE(config_email_enable,'') AS config_email_enable,"
				+ " COALESCE(config_sms_enable,'') AS config_sms_enable,"
				+ " COALESCE(config_sales_enquiry,'') AS config_sales_enquiry,"
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + emp_id;
		// SOP(StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_enquiry = crs.getString("config_sales_enquiry");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
