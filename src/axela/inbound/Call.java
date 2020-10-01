package axela.inbound;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import axela.customer.Customer_Tags_Check;
import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Call extends Connect {
	public String comp_id = "0";
	public String call_id = "0", call_callid = "0";
	public String call_no = "";
	public String emp_id = "0";
	public String call_search_no = "";
	public String singlecontact = "", singlecontact_id = "0";
	// =================================
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";

	public String customer_id = "0";
	public String customer_name = "";
	public String customer_mobile1 = "";
	public String customer_mobile2 = "";
	public String customer_email1 = "";
	public String customer_email2 = "";
	public String customer_address = "";
	public String contact_id = "0";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_phone1 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_address = "";
	public String customer_class = "";
	public String customer_accountmanager = "";

	public String customer_enquiry_count = "0";
	public String customer_so_count = "0";
	public String customer_servicebooking_count = "0";
	public String customer_insurance_count = "0";
	public String customer_jc_count = "0";
	public String customer_ticket_count = "0";
	public String customer_preowned_count = "0";
	public String Tag = "";
	public String customer_tags = "";
	public String search_button = "";
	public String add_button = "";
	public String contact_add = "";
	public String inbound = "";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_city_id = "0";
	public String contact_pin = "";

	public String customer_branch_id = "0";
	public String contact_count = "0";
	public String StrHTML = "";
	public String take_call = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = getcompsession(request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
			if (emp_id.equals("0")) {
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				AssignSession(comp_id, emp_id, "0", "0", "1", "", "", "", "", "10", 100, request);
			}
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			search_button = PadQuotes(request.getParameter("search_button"));
			inbound = PadQuotes(request.getParameter("inbound"));
			add_button = PadQuotes(request.getParameter("add_button"));
			take_call = PadQuotes(request.getParameter("take_call"));
			call_callid = CNumeric(PadQuotes(request.getParameter("call_callid")));
			call_id = CNumeric(PadQuotes(request.getParameter("call_id")));
			call_no = PadQuotes(request.getParameter("call_no"));
			contact_add = PadQuotes(request.getParameter("contact_add"));
			singlecontact = PadQuotes(request.getParameter("singlecontact"));
			singlecontact_id = CNumeric(PadQuotes(request.getParameter("singlecontact_id")));
			contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
			customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
			if (!comp_id.equals("0")) {
				if (search_button.equals("Search")) {
					call_search_no = PadQuotes(request.getParameter("call_no_search"));
					if (!call_search_no.contains("91-")) {
						call_search_no = "91-" + call_search_no;
					}
					if (!IsValidMobileNo11(call_search_no)) {
						msg = "Enter Valid Mobile!<br>";
						return;
					} else {
						AddCall();
						contact_count = SearchContactByNumber(call_search_no);
						if (contact_count.equals("0")) {
							response.sendRedirect("../inbound/call.jsp?contact_add=yes&call_callid=" + call_callid + "&call_no=" + call_search_no);
						} else if (contact_count.equals("1")) {
							response.sendRedirect("../inbound/call.jsp?singlecontact=single&singlecontact_id=" + contact_id + "&call_callid=" + call_callid + "&call_no=" + call_search_no);
						} else {
							response.sendRedirect("../inbound/call.jsp?singlecontact=multiple&call_callid=" + call_callid + "&call_no=" + call_search_no);
						}
						return;
					}
				}
				if (inbound.equals("yes")) {
					call_search_no = PadQuotes(request.getParameter("call_no"));
					if (!call_search_no.contains("91-")) {
						call_search_no = "91-" + call_search_no;
					}
					AddCall();
					contact_count = SearchContactByNumber(call_search_no);
					if (contact_count.equals("0")) {
						response.sendRedirect("../inbound/call.jsp?contact_add=yes&call_callid=" + call_callid + "&call_no=" + call_search_no);
					} else if (contact_count.equals("1")) {
						// Main Fraim
						response.sendRedirect("../inbound/call.jsp?"
								+ "singlecontact=single"
								+ "&singlecontact_id=" + contact_id
								+ "&call_id=" + call_id
								+ "&call_callid=" + call_callid
								+ "&call_no=" + call_search_no
								+ "&customer_id=" + customer_id
								+ "&contact_id=" + contact_id);
					} else {
						response.sendRedirect("../inbound/call.jsp?singlecontact=multiple&call_callid=" + call_callid + "&call_no=" + call_search_no);
					}
					return;

				}
				if (contact_add.equals("yes") && add_button.equals("Add Contact")) {
					GetValues(request, response);
					Addfields();
					if (msg.equals("")) {
						// Main Fraim
						response.sendRedirect("../inbound/call.jsp?"
								+ "singlecontact=single"
								+ "&singlecontact_id=" + contact_id
								+ "&contact_id=" + contact_id
								+ "&customer_id=" + customer_id
								+ "&call_callid=" + call_callid
								+ "&call_id=" + call_callid
								+ "&call_no=" + call_no);
					} else {
						msg = "Error!</br>" + msg;
					}

				}
				if (singlecontact.equals("single")) {
					SearchContactById(singlecontact_id);
				} else if (singlecontact.equals("multiple")) {
					StrHTML = SearchMultipleContact(call_no);
				}

				if (take_call.equals("yes") && !singlecontact_id.equals("0")) {
					// Main Fraim
					response.sendRedirect("../inbound/call.jsp?singlecontact=single"
							+ "&singlecontact_id=" + singlecontact_id
							+ "&call_id=" + call_callid
							+ "&call_callid=" + call_callid
							+ "&customer_id=" + customer_id
							+ "&contact_id=" + singlecontact_id
							+ "&call_no=" + call_no);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddCall() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_call"
					+ "(call_emp_id,"
					+ " call_callid,"
					+ " call_phone_number,"
					+ " call_entry_time)"
					+ " VALUES"
					+ " ("
					+ "" + emp_id + ","
					+ " " + call_callid + ","
					+ " '" + call_no + "',"
					+ " '" + ToLongDate(kknow()) + "'"
					+ ")";
			// SOP("StrSql------call----------" + StrSql);
			call_callid = UpdateQueryReturnID(StrSql);
			call_id = call_callid;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Addfields() throws SQLException {
		CheckForm();
		SOP("coming...to add filelds");
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (customer_id.equals("0")) {
					AddCustomerFields();
				}
				if (contact_id.equals("0")) {
					AddContactFields();
				}
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

			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void AddCustomerFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_city_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_emp_id,"
					+ " customer_since,"
					+ " customer_email1,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " VALUES "
					+ " ('" + customer_branch_id + "'," // enquiry_branch_id
					+ " '" + customer_name + "'," // customer_name
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " '" + contact_city_id + "'," // contact_city_id
					+ " 32," // customer_accgroup_id
					+ " 1," // customer_type
					+ " '0'," // enquiry_soe_id
					+ " '0'," // enquiry_sob_id
					+ " " + emp_id + "," // enquiry_emp_id
					+ " '" + ToShortDate(kknow()) + "'," // customer_since
					+ " '" + contact_email1 + "'," // contact_email1
					+ " '1'," // customer_active
					+ " ''," // customer_notes
					+ " " + emp_id + "," // customer_entry_id
					+ " '" + ToLongDate(kknow()) + "'" // customer_entry_date
					+ " )"; // customer_modified_date
			// SOP("Customer==" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				customer_id = rs.getString(1);
			}
			rs.close();
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
	public void AddContactFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_mobile1,"
					+ " contact_email1,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " VALUES "
					+ " (" + customer_id + "," // contact_customer_id
					+ " 1," // contact_contacttype_id
					+ " " + contact_title_id + "," // contact_title_id
					+ " '" + contact_fname + "'," // contact_fname
					+ " '" + contact_lname + "'," // contact_lname
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " '" + contact_email1 + "'," // contact_email1
					+ " '" + contact_address + "'," // contact_address
					+ " '" + contact_city_id + "'," // contact_city_id
					+ " '1'," // contact_active
					+ " ''," // contact_notes
					+ " " + emp_id + "," // contact_entry_id
					+ " '" + ToLongDate(kknow()) + "')"; // contact_entry_date
			// SOP("Contact==" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				contact_id = rs.getString(1);
			}
			rs.close();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = CNumeric(PadQuotes(request.getParameter("txt_customer_city")));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));

	}

	public void CheckForm() throws SQLException {
		if (customer_branch_id.equals("0")) {
			msg = "Select Branch!<br>";
		}
		if (contact_title_id.equals("0")) {
			msg += "Select Title!<br>";
		}
		if (contact_fname.equals("")) {
			msg += "Enter First Name!<br>";
		} else {
			contact_fname = toTitleCase(contact_fname);
		}
		if (contact_lname.equals("")) {
			msg = msg + "Enter the Contact Person Last Name!<br>";
		} else {
			contact_lname = toTitleCase(contact_lname);
		}
		if (customer_name.equals("")) {
			customer_name = (contact_fname + " " + contact_lname).trim();
			customer_name = toTitleCase(customer_name);
		}

		if (contact_mobile1.equals("") && contact_phone1.equals("")) {
			msg = msg + "Enter Either Contact Mobile 1 or Phone 1!<br>";
		} else {
			if (contact_mobile1.equals("")) {
				msg = msg + "Enter Either Contact Mobile 1 or Phone 1!<br>";
			} else if (!contact_mobile1.equals("")) {
				if (!IsValidMobileNo11(contact_mobile1)) {
					msg = msg + "Enter Valid Contact Mobile 1!<br>";

				}
			}
			if (!contact_phone1.equals("")) {
				if (!IsValidPhoneNo11(contact_phone1)) {
					msg = msg + "Enter Valid Contact Phone!<br>";
				}
			}

		}

		if (!contact_email1.equals("")) {
			if (!IsValidEmail(contact_email1)) {
				msg = msg + "Enter Valid Contact Email 1!<br>";
			}
		}

		StrSql = "SELECT branch_city_id, branch_pin "
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_id = " + customer_branch_id;
		SOP("StrSql===" + StrSql);
		CachedRowSet crs = processQuery(StrSql);
		if (crs.isBeforeFirst()) {
			while (crs.next()) {
				if (contact_city_id.equals("0")) {
					contact_city_id = crs.getString("branch_city_id");
				}
				if (contact_pin.equals("")) {
					contact_pin = crs.getString("branch_pin");
				}

			}

		}
		// SOP("msg===" + msg);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String SearchContactByNumber(String call_search_no) throws SQLException {
		StrSql = "SELECT"
				+ " COUNT(contact_id) AS contactcount, contact_id ,contact_customer_id"
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
				+ " WHERE 1 = 1 "
				+ " AND (contact_mobile1 = '" + call_search_no + "'"
				+ " OR contact_mobile2 = '" + call_search_no + "'"
				+ " OR contact_phone1 = '" + call_search_no + "'"
				+ " OR contact_phone2 = '" + call_search_no + "'"
				+ " OR customer_mobile1 = '" + call_search_no + "'"
				+ " OR customer_mobile2 = '" + call_search_no + "'"
				+ " OR customer_phone1 = '" + call_search_no + "'"
				+ " OR customer_phone2 = '" + call_search_no + "')";
		// SOP("SearchContactByNumber===" + StrSql);
		CachedRowSet crs = processQuery(StrSql);
		while (crs.next()) {
			contact_count = crs.getString("contactcount");
			contact_id = crs.getString("contact_id");
			customer_id = crs.getString("contact_customer_id");
		}

		if (!contact_count.equals("1")) {
			contact_id = "0";
			customer_id = "0";
		}

		return contact_count;
	}

	public void SearchContactById(String contact_id) {
		try {
			StrSql = "SELECT "
					+ " customer_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_mobile2,"
					+ " customer_email1,"
					+ " customer_email2,"
					+ " customer_address,"
					+ " contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ',contact_lname) AS contact_name,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_address,"
					+ " COALESCE(class_desc,'') AS customer_class,"
					+ " COALESCE(emp_name,'') AS emp_name,"

					+ " COALESCE ( (SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_contact_id = contact_id ),0) AS 'enquirycount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT so_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_contact_id = contact_id"
					+ " AND so_active = 1 ),0) AS 'socount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = veh_id"
					+ " WHERE vehfollowup_bookingtype_id = 1"
					+ " AND veh_contact_id = contact_id ),0) AS 'bookingcount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT jc_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc WHERE"
					+ " jc_contact_id = contact_id ),0) AS 'jccount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT preowned_id)"
					+ " FROM " + compdb(comp_id) + "axela_preowned WHERE"
					+ " preowned_contact_id = contact_id ),0) AS 'preownedcount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT ticket_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " WHERE ticket_contact_id = contact_id ),0) AS 'ticketcount'"

					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_class ON class_id = customer_class_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = customer_emp_id"
					+ " WHERE contact_id = " + contact_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql==2222==" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					customer_name = crs.getString("customer_name");
					customer_mobile1 = crs.getString("customer_mobile1");
					customer_mobile2 = crs.getString("customer_mobile2");
					customer_email1 = crs.getString("customer_email1");
					customer_email2 = crs.getString("customer_email2");
					customer_address = crs.getString("customer_address");
					contact_id = crs.getString("contact_id");
					contact_name = crs.getString("contact_name");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_address = crs.getString("contact_address");
					customer_class = crs.getString("customer_class");
					customer_accountmanager = crs.getString("emp_name");

					customer_enquiry_count = CNumeric(crs.getString("enquirycount"));
					customer_so_count = CNumeric(crs.getString("socount"));
					customer_servicebooking_count = CNumeric(crs.getString("bookingcount"));
					customer_preowned_count = CNumeric(crs.getString("preownedcount"));
					customer_jc_count = CNumeric(crs.getString("jccount"));
					customer_ticket_count = CNumeric(crs.getString("ticketcount"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String SearchMultipleContact(String search_call_no) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {

			StrSql = "SELECT "
					+ " customer_id, contact_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ',contact_lname) AS contact_name,"
					+ " contact_mobile1,"
					+ " branch_name,"
					+ " COALESCE(emp_name,'') AS emp_name,"

					+ " COALESCE ( (SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE"
					+ " enquiry_contact_id = contact_id "
					+ " GROUP BY so_contact_id),0) AS 'enquirycount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT so_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so WHERE"
					+ " so_contact_id = contact_id "
					+ " GROUP BY so_contact_id),0) AS 'socount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = veh_id"
					+ " WHERE vehfollowup_bookingtype_id = 1"
					+ " AND veh_contact_id = contact_id"
					+ " GROUP BY veh_contact_id ),0) AS 'bookingcount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT jc_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc WHERE"
					+ " jc_contact_id = contact_id ),0) AS 'jccount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT preowned_id)"
					+ " FROM " + compdb(comp_id) + "axela_preowned WHERE"
					+ " preowned_contact_id = contact_id"
					+ " GROUP BY preowned_contact_id ),0) AS 'preownedcount',"

					+ " COALESCE ( (SELECT COUNT(DISTINCT ticket_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " WHERE ticket_contact_id = contact_id "
					+ " GROUP BY ticket_contact_id),0) AS 'ticketcount'"

					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = customer_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = veh_id"
					+ " AND vehfollowup_bookingtype_id = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_contact_id = contact_id"
					+ " AND so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_contact_id = contact_id"
					+ " AND jc_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_preowned ON preowned_contact_id = contact_id"

					+ " LEFT JOIN " + compdb(comp_id) + " axela_service_ticket ON ticket_contact_id = contact_id "
					+ " WHERE 1 = 1 "
					+ " AND (contact_mobile1 = '" + call_no + "'"
					+ " OR contact_mobile2 = '" + call_no + "'"
					+ " OR contact_phone1 = '" + call_no + "'"
					+ " OR contact_phone2 = '" + call_no + "'"
					+ " OR customer_mobile1 = '" + call_no + "'"
					+ " OR customer_mobile2 = '" + call_no + "'"
					+ " OR customer_phone1 = '" + call_no + "'"
					+ " OR customer_phone2 = '" + call_no + "')"
					+ " GROUP BY contact_id"
					+ " ORDER BY contact_id DESC ";

			// SOP("StrSql==33==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<tr align=\"center\">\n");
			Str.append("<th data-toggle=\"true\" colspan=1>#</th>\n");
			Str.append("<th data-hide=\"phone, tablet\" colspan=2>Name</th>\n");
			Str.append("<th data-hide=\"phone, tablet\" colspan=2>Mobile</th>\n");
			Str.append("<th data-hide=\"phone, tablet\" colspan=2>Branch</th>\n");
			Str.append("<th data-hide=\"phone, tablet\" colspan=1>Action</th>\n");

			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<tr align=\"center\">\n");
					Str.append("<td colspan=1 data-toggle=\"true\">" + ++count + "</td>\n");
					Str.append("<td colspan=2 data-hide=\"phone, tablet\"><b>" + crs.getString("contact_name") + "</b></td>\n");
					Str.append("<td colspan=2 data-hide=\"phone, tablet\"><b>" + crs.getString("contact_mobile1") + "</b></td>\n");
					Str.append("<td colspan=2 data-hide=\"phone, tablet\"><b>" + crs.getString("branch_name") + "</b></td>\n");
					Str.append("<td colspan=2 data-hide=\"phone, tablet\">").append("<a href='../inbound/call.jsp?take_call=yes")
							.append("&singlecontact_id=" + crs.getString("contact_id"))
							.append("&customer_id=" + crs.getString("customer_id"))
							// .append("&call_id=" + call_id)
							.append("&call_callid=" + call_callid + "&call_no=" + call_no)
							.append("'>").append("Take Call").append("</td>\n");
					Str.append("</tr>\n");

					Str.append("<tr>\n");
					Str.append("<td valign=top align=left colspan=1>").append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("Enquiry: ").append(crs.getString("enquirycount")).append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("Pre-Owned: ").append(crs.getString("preownedcount")).append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("SO: ").append(crs.getString("socount")).append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("Service Booking: ").append(crs.getString("bookingcount")).append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("JC: ").append(crs.getString("jccount")).append("</td>\n");
					Str.append("<td valign=top align=left colspan=1>").append("Tickets: ").append(crs.getString("ticketcount")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr> <td valign=top align=left colspan=8>\n");
					Str.append("</td></tr>");
					Str.append("</tr>\n");
					Str.append("</tr>");
				}
			}
			crs.close();

			Str.append("</tbody>\n");
			Str.append("</table>\n");

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateTags(String contact_customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT REPLACE(COALESCE(("
					+ " SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
					+ " WHERE tagtrans_customer_id = " + contact_customer_id + " ), '' ),',','') AS tag";
			// SOP("tags-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Tag = crs.getString("tag");
				Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='border: 1px solid aliceblue;top:-16px; background:");
				Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
				Tag = ReplaceStr(Tag, "StartName", "");
				Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
				Str.append(Tag);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title" + " WHERE 1 =  1";
			if (!CNumeric(contact_title_id).equals("0")) {
				StrSql += " AND title_id = " + contact_title_id + "";
			}
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
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

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 ";
			// + " AND branch_branchtype_id IN (1, 2)";
			SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

}
