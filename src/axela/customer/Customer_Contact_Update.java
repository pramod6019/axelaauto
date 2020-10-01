//@Bhagwan Singh 11 feb 2013
package axela.customer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Customer_Contact_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String contact_id = "0";
	public String customer_id = "0", contact_contacttype_id = "0";
	public String customer_name = "", customer_branch_id = "0";
	public String branch_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_jobtitle = "";
	public String contact_location = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_mobile3 = "";
	public String contact_mobile4 = "";
	public String contact_mobile5 = "";
	public String contact_mobile6 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_yahoo = "", contact_msn = "";
	public String contact_aol = "", contact_skype = "";
	public String contact_address = "";
	public String contact_city_id = "0";
	public String customer_pan_no = "";
	public String contact_pin = "";
	public String contact_landmark = "";
	public String customer_since = "", customersince = "";
	public String contact_dob = "", dob = "";
	public String contact_anniversary = "", anniversary = "";
	public String dropDueDay = "", dropDueMonth = "";
	public String dropDueDays = "", dropDueMonths = "", dropDueYears = "2012";
	public String dropSinceDays = "", dropSinceMonths = "", dropSinceYears = "";
	public String customer_empcount_id = "0";
	public String customer_type = "0";
	public String contact_active = "0";
	public String contact_notes = "";
	public String contact_entry_id = "0";
	public String contact_entry_date = "";
	public String contact_modified_id = "0";
	public String contact_modified_date = "";
	public String emp_role_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String BranchAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String QueryString = "";
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_contact_access", request, response);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (branch_id.equals("") || branch_id.equals("0")) {
					customer_branch_id = PadQuotes(request.getParameter("dr_customer_branch_id"));
				} else {
					customer_branch_id = branch_id;
				}

				if (add.equals("yes")) {
				} else if (update.equals("yes")) {
				}
				if (!customer_id.equals("0")) {
					StrSql = "SELECT concat(customer_name,' (',customer_id,')') as customer_name"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_id = " + CNumeric(customer_id) + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							customer_name = crs.getString("customer_name");
						}
					} else {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Customer!"));
					}
					crs.close();
				}
				if ("yes".equals(add)) {
					status = "Add";
					if (!("yes").equals(addB)) {
						contact_fname = "";
						contact_lname = "";
						contact_jobtitle = "";
						contact_location = "";
						contact_phone1 = "";
						contact_phone2 = "";
						contact_mobile1 = "91-";
						contact_mobile2 = "";
						contact_mobile3 = "";
						contact_mobile4 = "";
						contact_mobile5 = "";
						contact_mobile6 = "";
						contact_email1 = "";
						contact_email2 = "";
						contact_yahoo = "";
						contact_msn = "";
						contact_aol = "";
						contact_skype = "";
						contact_address = "";
						contact_pin = "";
						contact_landmark = "";
						customer_pan_no = "";
						customer_since = "";
						dob = "";
						anniversary = "";
						contact_active = "1";
						contact_notes = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_contact_add", request).equals("1")) {
							contact_entry_id = CNumeric(GetSession("emp_id", request));
							contact_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("customer-contact-list.jsp?customer_id=" + customer_id + "&contact_id=" + contact_id
										+ "&msg=Contact Person added successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!("yes").equals(updateB) && !(deleteB).equals("Delete Contact Person")) {
						if (!customer_name.equals("")) {
							PopulateFields(response);
						}
					} else if (("yes").equals(updateB) && !(deleteB).equals("Delete Contact Person")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_contact_edit", request).equals("1")) {
							contact_modified_id = CNumeric(GetSession("emp_id", request));
							contact_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("customer-contact-list.jsp?contact_id=" + contact_id + "&msg=Contact Person details updated successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Contact Person")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_contact_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("customer-contact-list.jsp?msg=Contact Person details deleted successfully."));
							}
						} else {
							response.sendRedirect(AccessDenied());
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		contact_id = PadQuotes(request.getParameter("contact_id"));
		contact_contacttype_id = CNumeric(PadQuotes(request.getParameter("dr_contact_contacttype_id")));
		contact_title_id = PadQuotes(request.getParameter("dr_title"));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_jobtitle = PadQuotes(request.getParameter("txt_contact_jobtitle"));
		contact_location = PadQuotes(request.getParameter("txt_contact_location"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
		contact_mobile3 = PadQuotes(request.getParameter("txt_contact_mobile3"));
		contact_mobile4 = PadQuotes(request.getParameter("txt_contact_mobile4"));
		contact_mobile5 = PadQuotes(request.getParameter("txt_contact_mobile5"));
		contact_mobile6 = PadQuotes(request.getParameter("txt_contact_mobile6"));
		contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
		contact_phone2 = PadQuotes(request.getParameter("txt_contact_phone2"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_email2 = PadQuotes(request.getParameter("txt_contact_email2"));
		contact_yahoo = PadQuotes(request.getParameter("txt_contact_yahoo"));
		contact_msn = PadQuotes(request.getParameter("txt_contact_msn"));
		contact_aol = PadQuotes(request.getParameter("txt_contact_aol"));
		contact_skype = PadQuotes(request.getParameter("txt_contact_skype"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = PadQuotes(request.getParameter("maincity"));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		contact_landmark = PadQuotes(request.getParameter("txt_contact_landmark"));
		customer_pan_no = PadQuotes(request.getParameter("txt_customer_pan_no"));
		customersince = PadQuotes(request.getParameter("txt_customer_since"));
		customer_type = CNumeric(PadQuotes(request.getParameter("dr_customer_type")));
		customer_empcount_id = CNumeric(PadQuotes(request.getParameter("drop_customer_empcount_id")));
		dropDueDay = PadQuotes(request.getParameter("drop_bday"));
		dropDueMonth = PadQuotes(request.getParameter("drop_bmonth"));
		contact_dob = dropDueYears + dropDueMonth + dropDueDay + "000000";
		dropDueDays = PadQuotes(request.getParameter("drop_aday"));
		dropDueMonths = PadQuotes(request.getParameter("drop_amonth"));
		contact_anniversary = dropDueYears + dropDueMonths + dropDueDays + "000000";
		contact_active = CheckBoxValue(PadQuotes(request.getParameter("chk_contact_active")));
		contact_notes = PadQuotes(request.getParameter("txt_contact_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (customer_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		}
		if ((dropDueDay.equals("-1")) || (dropDueMonth.equals("-1"))) {
			contact_dob = "";
		}
		if ((dropDueDays.equals("-1")) || (dropDueMonths.equals("-1"))) {
			contact_anniversary = "";
		}
	}

	protected void CheckForm() {
		msg = "";
		if (contact_contacttype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
		if (customer_name.equals("")) {
			msg += "<br>Enter the Customer Name!";
		} else {
			customer_name = toTitleCase(customer_name);
		}

		if (contact_title_id.equals("0")) {
			msg += "<br>Select Title!";
		}

		if (contact_fname.equals("")) {
			msg += "<br>Enter the Contact Person First Name!";
		} else {
			contact_fname = toTitleCase(contact_fname);
		}

		if (!contact_lname.equals("")) {
			contact_lname = toTitleCase(contact_lname);
		}

		if (contact_mobile1.equals("91-")) {
			contact_mobile1 = "";
		}

		if (contact_mobile1.equals("")) {
			msg += "<br>Enter Contact Mobile 1!";
		} else {
			if (!IsValidMobileNo11(contact_mobile1)) {
				msg += "<br>Enter Valid Mobile 1!";
			}
		}

		if (!contact_mobile2.equals("") && !IsValidMobileNo11(contact_mobile2)) {
			msg += "<br>Enter Valid Mobile 2!";
		}

		if (!contact_mobile3.equals("") && !IsValidMobileNo11(contact_mobile3)) {
			msg += "<br>Enter Valid Mobile 3!";
		}

		if (!contact_mobile4.equals("") && !IsValidMobileNo11(contact_mobile4)) {
			msg += "<br>Enter Valid Mobile 4!";
		}

		if (!contact_mobile5.equals("") && !IsValidMobileNo11(contact_mobile5)) {
			msg += "<br>Enter Valid Mobile 5!";
		}

		if (!contact_mobile6.equals("") && !IsValidMobileNo11(contact_mobile6)) {
			msg += "<br>Enter Valid Mobile 6!";
		}

		if (!contact_phone1.equals("")) {
			if (!IsValidPhoneNo11(contact_phone1)) {
				msg += "<br>Enter Valid Phone 1!";
			}
		}

		if (!contact_phone2.equals("")) {
			if (!IsValidPhoneNo11(contact_phone2)) {
				msg += "<br>Enter Valid Phone 2!";
			}
		}

		if (!contact_email1.equals("") && IsValidEmail(contact_email1) != true) {
			msg += "<br>Enter valid Email 1!";
		} else {
			contact_email1 = contact_email1.toLowerCase();
		}

		if (!contact_email2.equals("") && IsValidEmail(contact_email2) != true) {
			msg += "<br>Enter valid Email 2!";
		} else {
			contact_email2 = contact_email2.toLowerCase();
		}

		if (!contact_email1.equals("")
				&& !contact_email2.equals("")
				&& contact_email2.equals(contact_email1)) {
			msg += "<br>Email2 is same as Email1!";
		}

		if (contact_address.equals("")) {
			msg += "<br>Enter Address!";
		}

		if (contact_city_id.equals("")) {
			msg += "<br>Select City!";
		}

		if (contact_pin.equals("")) {
			msg += "<br>Enter Pin Code!";
		} else if (!contact_pin.equals("") && !isNumeric(contact_pin)) {
			msg += "<br>Enter Numeric Pin Code!";
		}

		if (((dropDueDay.equals("-1")) && (!dropDueMonth.equals("-1"))) || ((!dropDueDay.equals("-1")) && (dropDueMonth.equals("-1")))) {
			msg += "<br>Invalid DOB!";
		}

		if (((dropDueDays.equals("-1")) && (!dropDueMonths.equals("-1"))) || ((!dropDueDays.equals("-1")) && (dropDueMonths.equals("-1")))) {
			msg += "<br>Invalid Anniversary Date!";
		}

		if ((!dropDueDay.equals("-1")) && (!dropDueMonth.equals("-1"))) {
			if (!isValidDateFormatShort(dropDueDay + "/" + dropDueMonth + "/" + dropDueYears)) {
				msg += "<br>Invalid DOB!";
			}
		}

		if ((!dropDueDays.equals("-1")) && (!dropDueMonths.equals("-1"))) {
			if (!isValidDateFormatShort(dropDueDays + "/" + dropDueMonths + "/" + dropDueYears)) {
				msg += "<br>Invalid Anniversary Date!";
			}
		}

		if (!contact_email1.equals("")) {
			contact_email1 = contact_email1.toLowerCase();
		}

		if (!contact_email2.equals("")) {
			contact_email2 = contact_email2.toLowerCase();
		}
		if ("yes".equals(add)) {
			if (customer_id.equals("0")) {
				if (customer_type.equals("0")) {
					msg += "<br>Select Customer Type!";
				}

				if (customersince.equals("")) {
					msg += "<br>Select Customer Since Date!";
				}
			}
			else if (!customersince.equals("")) {
				if (isValidDateFormatShort(customersince)) {
					customer_since = ConvertShortDateToStr(customersince);
				} else {
					msg = msg + "<br>Enter Valid Customer Since Date!";
				}
			}
		}
	}

	protected void AddFields(HttpServletRequest request) {

		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			if (customer_id.equals("0")) {

				if (!customer_name.equals("")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
							+ " (customer_name,"
							+ " customer_city_id,"
							+ " customer_branch_id,"
							+ " customer_notes,"
							+ " customer_pan_no,"
							+ " customer_since,"
							+ " customer_type,"
							+ " customer_accgroup_id,"
							+ " customer_empcount_id,"
							+ " customer_entry_id,"
							+ " customer_entry_date)"
							+ " VALUES"
							+ " ("
							+ " '" + customer_name + "',"
							+ " " + contact_city_id + ","
							+ " " + customer_branch_id + ","
							+ " '',"
							+ " '" + customer_pan_no + "',"
							+ " '" + customer_since + "',"
							+ " " + customer_type + ",";
					if (customer_type.equals("1")) {
						StrSql += " 32,";
					} else if (customer_type.equals("2")) {
						StrSql += " 31,";
					}
					StrSql += " " + customer_empcount_id + ","
							+ " " + contact_entry_id + ","
							+ " '" + contact_entry_date + "')";
					SOP("StrSql==========" + StrSql);
					customer_id = UpdateQueryReturnID(StrSql);
				}
			}

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle, "
					+ " contact_location,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_mobile3,"
					+ " contact_mobile4,"
					+ " contact_mobile5,"
					+ " contact_mobile6,"
					+ " contact_phone1,"
					+ " contact_phone2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_yahoo,"
					+ " contact_msn,"
					+ " contact_aol,"
					+ " contact_skype,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_landmark,"
					+ " contact_dob,"
					+ " contact_anniversary,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " VALUES"
					+ " (" + customer_id + ","
					+ " " + contact_contacttype_id + ","
					+ " " + contact_title_id + ","
					+ " '" + contact_fname + "',"
					+ " '" + contact_lname + "',"
					+ " '" + contact_jobtitle + "',"
					+ " '" + contact_location + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_mobile2 + "',"
					+ " '" + contact_mobile3 + "',"
					+ " '" + contact_mobile4 + "',"
					+ " '" + contact_mobile5 + "',"
					+ " '" + contact_mobile6 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + contact_phone2 + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_email2 + "',"
					+ " '" + contact_yahoo + "',"
					+ " '" + contact_msn + "',"
					+ " '" + contact_aol + "',"
					+ " '" + contact_skype + "',"
					+ " '" + contact_address + "',"
					+ " " + contact_city_id + ","
					+ " '" + contact_pin + "',"
					+ " '" + contact_landmark + "',"
					+ " '" + contact_dob + "',"
					+ " '" + contact_anniversary + "',"
					+ " '" + contact_active + "',"
					+ " '" + contact_notes + "',"
					+ " " + contact_entry_id + ","
					+ " '" + contact_entry_date + "')";
			// SOP("StrSql=====IN==" + StrSqlBreaker(StrSql));
			contact_id = UpdateQueryReturnID(StrSql);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
					+ " SET"
					+ " contact_contacttype_id = " + contact_contacttype_id + ","
					+ " contact_title_id = " + contact_title_id + ","
					+ " contact_fname = '" + contact_fname + "',"
					+ " contact_lname = '" + contact_lname + "',"
					+ " contact_jobtitle = '" + contact_jobtitle + "',"
					+ " contact_location = '" + contact_location + "',"
					+ " contact_mobile1 = '" + contact_mobile1 + "',"
					+ " contact_mobile2 = '" + contact_mobile2 + "',"
					+ " contact_mobile3 = '" + contact_mobile3 + "',"
					+ " contact_mobile4 = '" + contact_mobile4 + "',"
					+ " contact_mobile5 = '" + contact_mobile5 + "',"
					+ " contact_mobile6 = '" + contact_mobile6 + "',"
					+ " contact_phone1 = '" + contact_phone1 + "',"
					+ " contact_phone2 = '" + contact_phone2 + "',"
					+ " contact_email1 = '" + contact_email1 + "',"
					+ " contact_email2 = '" + contact_email2 + "',"
					+ " contact_yahoo = '" + contact_yahoo + "',"
					+ " contact_msn = '" + contact_msn + "',"
					+ " contact_aol = '" + contact_aol + "',"
					+ " contact_skype = '" + contact_skype + "',"
					+ " contact_address = '" + contact_address + "',"
					+ " contact_city_id = " + contact_city_id + ","
					+ " contact_pin = '" + contact_pin + "',"
					+ " contact_landmark = '" + contact_landmark + "',"
					+ " contact_dob = '" + contact_dob + "',"
					+ " contact_anniversary = '" + contact_anniversary + "',"
					+ " contact_active = '" + contact_active + "',"
					+ " contact_notes = '" + contact_notes + "',"
					+ " contact_modified_id = " + contact_modified_id + ","
					+ " contact_modified_date = '" + contact_modified_date + "'"
					+ " WHERE contact_id = " + contact_id + " ";
			updateQuery(StrSql);
		}
	}
	protected void DeleteFields() {
		msg = "";
		// Association with Activity
		StrSql = "SELECT activity_contact_id FROM " + compdb(comp_id) + "axela_activity"
				+ " WHERE activity_contact_id = " + contact_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Activity!";
		}

		// Association with enquiry
		StrSql = "SELECT enquiry_contact_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_contact_id = " + contact_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Enquiry!";
		}

		// Association with Quote
		StrSql = "SELECT quote_contact_id FROM " + compdb(comp_id) + "axela_sales_quote"
				+ " WHERE quote_contact_id = " + contact_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Quote!";
		}

		// Association with SO
		StrSql = "SELECT so_contact_id  FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_contact_id = " + contact_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Sales Order!";
		}

		// Association with Invoice
		StrSql = "SELECT voucher_contact_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_contact_id = " + contact_id + ""
				+ " AND voucher_vouchertype_id = 6";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Invoice!";
		}

		// Association with Receipt
		StrSql = "SELECT voucher_contact_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_contact_id = " + contact_id + ""
				+ " AND voucher_vouchertype_id = 9";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Contact is associated with Receipt!";
		}

		if (msg.equals("") && chkpermmsg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " where contact_id = " + contact_id + "";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT axela_customer.*, axela_customer_contact.*, customer_name,"
					+ " contact_id, customer_city_id, customer_since "
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE  contact_id = " + contact_id + BranchAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("contact_customer_id");
					contact_contacttype_id = crs.getString("contact_contacttype_id");
					contact_title_id = crs.getString("contact_title_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_jobtitle = crs.getString("contact_jobtitle");
					contact_location = crs.getString("contact_location");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_mobile3 = crs.getString("contact_mobile3");
					contact_mobile4 = crs.getString("contact_mobile4");
					contact_mobile5 = crs.getString("contact_mobile5");
					contact_mobile6 = crs.getString("contact_mobile6");
					contact_phone1 = crs.getString("contact_phone1");
					contact_phone2 = crs.getString("contact_phone2");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_yahoo = crs.getString("contact_yahoo");
					contact_msn = crs.getString("contact_msn");
					contact_aol = crs.getString("contact_aol");
					contact_skype = crs.getString("contact_skype");
					contact_address = crs.getString("contact_address");
					contact_city_id = crs.getString("contact_city_id");
					contact_pin = crs.getString("contact_pin");
					contact_landmark = crs.getString("contact_landmark");
					customer_pan_no = crs.getString("customer_pan_no");
					customersince = strToShortDate(crs.getString("customer_since"));
					customer_type = crs.getString("customer_type");
					customer_empcount_id = crs.getString("customer_empcount_id");
					contact_dob = crs.getString("contact_dob");
					dropDueDay = SplitDate(contact_dob);
					dropDueMonth = SplitMonth(contact_dob);
					contact_anniversary = crs.getString("contact_anniversary");
					dropDueDays = SplitDate(contact_anniversary);
					dropDueMonths = SplitMonth(contact_anniversary);
					contact_active = crs.getString("contact_active");
					contact_notes = crs.getString("contact_notes");
					contact_entry_id = crs.getString("contact_entry_id");
					entry_by = Exename(comp_id, crs.getInt("contact_entry_id"));
					entry_date = strToLongDate(crs.getString("contact_entry_date"));
					contact_modified_id = crs.getString("contact_modified_id");
					if (!contact_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(contact_modified_id));
						modified_date = strToLongDate(crs.getString("contact_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateMonth() {
		String month = "<option value=-1>Months</option>\n";
		month += "<option value=01" + StrSelectdrop(doublenum(1), dropDueMonth) + ">January</option>\n";
		month += "<option value=02" + StrSelectdrop(doublenum(2), dropDueMonth) + ">February</option>\n";
		month += "<option value=03" + StrSelectdrop(doublenum(3), dropDueMonth) + ">March</option>\n";
		month += "<option value=04" + StrSelectdrop(doublenum(4), dropDueMonth) + ">April</option>\n";
		month += "<option value=05" + StrSelectdrop(doublenum(5), dropDueMonth) + ">May</option>\n";
		month += "<option value=06" + StrSelectdrop(doublenum(6), dropDueMonth) + ">June</option>\n";
		month += "<option value=07" + StrSelectdrop(doublenum(7), dropDueMonth) + ">July</option>\n";
		month += "<option value=08" + StrSelectdrop(doublenum(8), dropDueMonth) + ">August</option>\n";
		month += "<option value=09" + StrSelectdrop(doublenum(9), dropDueMonth) + ">September</option>\n";
		month += "<option value=10" + StrSelectdrop(doublenum(10), dropDueMonth) + ">October</option>\n";
		month += "<option value=11" + StrSelectdrop(doublenum(11), dropDueMonth) + ">November</option>\n";
		month += "<option value=12" + StrSelectdrop(doublenum(12), dropDueMonth) + ">December</option>\n";
		return month;
	}

	public String PopulateDay() {
		String day = "<option value=-1>Days</option>\n";
		for (int i = 1; i <= 31; i++) {
			day += "<option value = " + doublenum(i) + "" + StrSelectdrop(doublenum(i), dropDueDay) + ">" + i + "</option>\n";
		}
		return day;
	}

	public String PopulateMonths() {
		String months = "<option value=-1>Months</option>\n";
		months += "<option value=01" + StrSelectdrop(doublenum(1), dropDueMonths) + ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dropDueMonths) + ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dropDueMonths) + ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dropDueMonths) + ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dropDueMonths) + ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dropDueMonths) + ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dropDueMonths) + ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dropDueMonths) + ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dropDueMonths) + ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dropDueMonths) + ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dropDueMonths) + ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dropDueMonths) + ">December</option>\n";
		return months;
	}

	public String PopulateDays() {
		String days = "<option value=\"-1\">Days</option>\n";
		for (int i = 1; i <= 31; i++) {
			days += "<option value = " + doublenum(i) + "" + StrSelectdrop(doublenum(i), dropDueDays) + ">" + i + "</option>\n";
		}
		return days;
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");

		Str.append("<option value=1").append(StrSelectdrop("1", customer_type))
				.append(">").append("Customer");
		Str.append("</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", customer_type))
				.append(">").append("Supplier");
		Str.append("</option>\n");

		return Str.toString();
	}

	public String PopulateContactType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "SELECT contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY contacttype_id"
					+ " ORDER BY contacttype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contacttype_id"));
				Str.append(StrSelectdrop(crs.getString("contacttype_id"), contact_contacttype_id));
				Str.append(">").append(crs.getString("contacttype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
