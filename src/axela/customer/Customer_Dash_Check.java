package axela.customer;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Customer_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String customer_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String name = "";
	public String value = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String customer_name = "";
	public String contact_customer_id = "0";
	public String customer_communication = "";
	public String customer_address = "";
	public String customer_landmark = "";
	public String customer_notes = "";
	public String customer_active = "";
	public String customer_exe = "";
	public String enquiry_count = "";
	public String quote_count = "";
	public String so_count = "";
	public String invoice_count = "";
	public String receipt_count = "";
	public String payment_count = "";
	public String jc_count = "";
	public String enquiry_total = "";
	public String quote_total = "";
	public String so_total = "";
	public String invoice_total = "";
	public String receipt_total = "";
	public String payment_total = "";
	public String jc_total = "";
	public DecimalFormat deci = new DecimalFormat("0.00");

	// for Tabs
	public String contacts = "";
	public String enquries = "";
	public String salesorder = "";
	public String vehicles = "";
	public String jobcards = "";
	// public String insurance = "";
	public String tickets = "";
	public String coupon = "";
	public String invoice = "";
	public String receipts = "";
	public String history = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				name = PadQuotes(request.getParameter("name"));
				value = PadQuotes(request.getParameter("value"));
				customer_id = PadQuotes(request.getParameter("customer_id"));
				customer_address = PadQuotes(request.getParameter("customer_address"));
				customer_landmark = PadQuotes(request.getParameter("customer_landmark"));
				customer_notes = PadQuotes(request.getParameter("customer_notes"));
				customer_active = PadQuotes(request.getParameter("customer_active"));
				customer_exe = PadQuotes(request.getParameter("customer_exe"));
				// for Tabs
				contacts = PadQuotes(request.getParameter("contacts"));
				enquries = PadQuotes(request.getParameter("enquiries"));
				salesorder = PadQuotes(request.getParameter("salesorder"));
				vehicles = PadQuotes(request.getParameter("vehicles"));
				jobcards = PadQuotes(request.getParameter("jobcards"));
				// insurance = PadQuotes(request.getParameter("insurance"));
				tickets = PadQuotes(request.getParameter("tickets"));
				coupon = PadQuotes(request.getParameter("coupon"));
				invoice = PadQuotes(request.getParameter("invoice"));
				receipts = PadQuotes(request.getParameter("receipts"));
				history = PadQuotes(request.getParameter("history"));
				try {
					if (!customer_id.equals("0")) {
						if (contacts.equals("yes")) {
							StrHTML = new Customer_Dash().ListContact(comp_id, customer_id);
						} else if (enquries.equals("yes")) {
							StrHTML = new Customer_Dash().EnquiryDetails(comp_id, customer_id);
						}
						else if (salesorder.equals("yes")) {
							StrHTML = new Customer_Dash().SODetails(comp_id, customer_id);
						}
						else if (vehicles.equals("yes")) {
							StrHTML = new Customer_Dash().VehicleDetails(comp_id, customer_id);
						}
						else if (jobcards.equals("yes")) {
							StrHTML = new Customer_Dash().JobCardDetails(comp_id, customer_id);
						}
						// else if (insurance.equals("yes")) {
						// StrHTML = new Customer_Dash().InsuranceDetails(comp_id, customer_id);
						// }
						else if (history.equals("yes")) {
							StrHTML = new Customer_Dash().HistoryDetails(comp_id, customer_id);
						}
						else if (tickets.equals("yes")) {
							StrHTML = new Customer_Dash().TicketDetails(comp_id, customer_id);
						}
						else if (coupon.equals("yes")) {
							StrHTML = new Customer_Dash().CouponDetails(comp_id, customer_id);
						}
						else if (invoice.equals("yes")) {
							StrHTML = new Customer_Dash().InvoiceDetails(customer_id, BranchAccess, ExeAccess, comp_id);
						}
						else if (receipts.equals("yes")) {
							StrHTML = new Customer_Dash().ReceiptDetails(customer_id, BranchAccess, ExeAccess, comp_id);
						}

						if (name.equals("txt_customer_name")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_name"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_name = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Name";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Name updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Name</font>";
							}
						}
						if (name.equals("txt_customer_alias")) {
							if (value != "") {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_alias"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_alias = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Alias";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Alias updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Alias</font>";
							}
						}

						if (name.equals("txt_customer_code")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT customer_code"
									+ " FROM " + compdb(comp_id) + "axela_customer"
									+ " WHERE customer_id=" + customer_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
									+ " SET"
									+ " customer_code = '" + value + "'"
									+ " WHERE customer_id = " + customer_id + "";
							updateQuery(StrSql);
							SOP(StrSql);
							String history_actiontype = "Customer Code";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
									+ " (history_customer_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + customer_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							// SOP(StrSql);
							StrHTML = "Customer Code updated!";
						}
						if (name.equals("dr_customer_branch_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String oldBranchId = ExecuteQuery("SELECT customer_branch_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id= " + customer_id + " ");
								String history_oldvalue = ExecuteQuery("SELECT  branch_name"
										+ " FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE branch_id= " + oldBranchId + " ");
								String branch_name = ExecuteQuery("SELECT  branch_name"
										+ " FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE branch_id= " + value + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_branch_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								SOP("Sql up" + StrSql);
								updateQuery(StrSql);
								String history_actiontype = "Branch";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + branch_name + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Branch Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select Branch!</font>";
							}
						}
						if (name.equals("txt_customer_mobile1")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile1 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile No Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile1 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile1 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_1";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Customer Mobile 1 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile No!</font>";
							}
						}
						if (name.equals("txt_customer_mobile2")) {
							if (IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile2 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile2 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile2 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_2";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Customer Mobile 2 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 2!</font>";
							}
						}
						if (name.equals("txt_customer_mobile3")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile3 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile3 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile3 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_3";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Customer Mobile 3 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 3!</font>";
							}
						}
						if (name.equals("txt_customer_mobile4")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile4 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile4 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile4 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_4";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Mobile 4 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 4!</font>";
							}
						}
						if (name.equals("txt_customer_mobile5")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile5 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile5 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile5 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_5";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Mobile 5 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 5!</font>";
							}
						}
						if (name.equals("txt_customer_mobile6")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_mobile6 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_mobile1 = '" + value + "' OR customer_mobile2 = '" + value + "'OR customer_mobile3 = '" + value + "'OR customer_mobile4 = '" + value
										+ "'OR customer_mobile5 = '" + value + "'OR customer_mobile6 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									SOP("Execute Query:" + StrSql);
									StrHTML = "Similar Mobile Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_mobile6 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_mobile6 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Mobile_6";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Mobile 6 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 6!</font>";
							}
						}
						if (name.equals("txt_customer_phone1")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_phone1 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_phone1 = '" + value + "' OR customer_phone2 = '" + value + "' OR customer_phone3 = '" + value + "' OR customer_phone4 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Phone Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_phone1 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_phone1 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Phone_1";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Phone 1 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Phone 1!</font>";
							}
						}
						if (name.equals("txt_customer_phone2")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_phone2 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_phone1 = '" + value + "' OR customer_phone2 = '" + value + "' OR customer_phone3 = '" + value + "' OR customer_phone4 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Phone Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_phone2 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_phone2 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Phone_2";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "customer Phone 2 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Phone 2!</font>";
							}
						}
						if (name.equals("txt_customer_phone3")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_phone3 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_phone1 = '" + value + "' OR customer_phone2 = '" + value + "' OR customer_phone3 = '" + value + "' OR customer_phone4 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Phone Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_phone3 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_phone3 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Phone_3";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "customer Phone 3 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Phone 3!</font>";
							}
						}
						if (name.equals("txt_customer_phone4")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT customer_phone4 "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = '" + customer_id + "' "
										+ " AND (customer_phone1 = '" + value + "' OR customer_phone2 = '" + value + "' OR customer_phone3 = '" + value + "' OR customer_phone4 = '" + value + "')";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Phone Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT customer_phone4 "
											+ " FROM " + compdb(comp_id) + "axela_customer "
											+ " WHERE customer_id=" + customer_id);
									StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
											+ " SET"
											+ " customer_phone4 = '" + value + "'"
											+ " WHERE customer_id = " + customer_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Customer_Phone_4";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
											+ " (history_customer_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + customer_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "customer Phone 4 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Phone 4!</font>";
							}
						}
						if (name.equals("txt_customer_fax1")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_fax1"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_fax1 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Fax 1";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Fax 1 Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Fax 1!</font>";
							}
						}
						if (name.equals("txt_customer_fax2")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_fax2"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_fax2 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Fax 2";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Fax 2 Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Fax 2!</font>";
							}
						}
						if (name.equals("txt_customer_email1")) {
							if (!value.equals("") && IsValidEmail(value)) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_email1"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_email1 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Email 1";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Email 1 Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid Email 1!</font>";
							}
						}
						if (name.equals("txt_customer_email2")) {
							if (!value.equals("") && IsValidEmail(value)) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_email2"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_email2 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Email 2";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Email 2 Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid Email 2!</font>";
							}
						}
						if (name.equals("txt_customer_website1")) {
							if (!value.equals("") && WebValidate(value) != "") {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_website1"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_website1 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "WebSite 1";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "WebSite 1 Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid WebSite 1!</font>";
							}
						}
						if (name.equals("txt_customer_website2") && WebValidate(value) != "") {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_website2"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_website2 = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "WebSite 2";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "WebSite 2 Updated!";
							}
							else
							{
								StrHTML = "<font color=\"red\">Enter Valid Website 2!</font>";
							}
						}
						if (name.equals("txt_customer_gst_no")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_gst_no"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_gst_no = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "GSTN";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "GSTN Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter GSTN!</font>";
							}
						}
						if (name.equals("txt_customer_gst_regdate")) {
							if (!value.equals("") && ConvertShortDateToStr(value) != "") {
								String history_oldvalue = ExecuteQuery("SELECT customer_gst_regdate"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_gst_regdate = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								SOP("RegDate" + StrSql);
								updateQuery(StrSql);
								String history_actiontype = "GSTIN Date";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "GSTIN Date Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid GSTIN Date!</font>";
							}
						}
						if (name.equals("txt_customer_arn_no")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_arn_no"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_arn_no = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "ARN";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "ARN Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Valid ARN!</font>";
							}
						}
						if (name.equals("dr_customer_itstatus_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String itoldStutusId = ExecuteQuery("SELECT 	customer_itstatus_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id= '" + customer_id + "'");
								String history_oldvalue = ExecuteQuery("SELECT 	itstatus_name"
										+ " FROM " + compdb(comp_id) + "axela_customer_itstatus"
										+ " WHERE itstatus_id= '" + itoldStutusId + "'");
								String status = ExecuteQuery("SELECT 	itstatus_name"
										+ " FROM " + compdb(comp_id) + "axela_customer_itstatus"
										+ " WHERE itstatus_id= '" + value + "'");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_itstatus_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Status";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + status + "')";
								updateQuery(StrSql);
								StrHTML = "Status Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select Status!</font>";
							}
						}
						if (name.equals("txt_customer_address")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_address"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_address = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Address";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Address Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Address!</font>";
							}
						}
						if (name.equals("maincity")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String oldCityId = ExecuteQuery("SELECT 	customer_city_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id= '" + customer_id + "'");
								String history_oldvalue = ExecuteQuery("SELECT 	city_name"
										+ " FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_id= '" + oldCityId + "'");
								String new_city_name = ExecuteQuery("SELECT  city_name"
										+ " FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_id= '" + value + "'");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_city_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "City";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + new_city_name + "')";
								updateQuery(StrSql);
								StrHTML = "City Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select City!</font>";
							}
						}
						if (name.equals("txt_customer_pin")) {
							if (!value.equals("") && value.length() == 6) {
								String history_oldvalue = ExecuteQuery("SELECT customer_pin"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_pin = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "PIN/ZIP";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "PIN/ZIP Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid PIN/ZIP!</font>";
							}
						}
						if (name.equals("chk_customer_tds")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_tds"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								if (history_oldvalue.equals("0")) {
									history_oldvalue = "TDS Inactive";
								}
								else {
									history_oldvalue = "TDS Active";
								}
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_tds = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								String historyNewValue = "";
								if (value.equals("0")) {
									historyNewValue = "TDS Inactive";
								}
								else {
									historyNewValue = "TDS Active";
								}
								updateQuery(StrSql);
								String history_actiontype = "Customer TDS";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + historyNewValue + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "TDS Updated!";
							}
						}
						if (name.equals("txt_customer_pan_no")) {

							if (!value.equals("") && value.length() == 10) {
								String history_oldvalue = ExecuteQuery("SELECT customer_pan_no"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_pan_no = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "PAN";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "PAN Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid PAN!</font>";
							}
						}
						if (name.equals("dr_customer_type")) {
							if (!value.equals("")) {
								String history_oldvalue = "";
								String customer_type = ExecuteQuery("SELECT customer_type"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								if (customer_type.equals("1")) {
									history_oldvalue = "Customer";
								}
								else {
									history_oldvalue = "Supplier";
								}
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_type = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String newValue = "";
								if (value.equals("1")) {
									newValue = "Customer";
								}
								else {
									newValue = "Supplier";
								}
								String history_actiontype = "Customer Type";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + newValue + "')";
								updateQuery(StrSql);
								StrHTML = "Customer Type Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid Customer Type!</font>";
							}
						}
						if (name.equals("txt_customer_since")) {
							if (!value.equals("") && ConvertShortDateToStr(value) != "") {
								String history_oldvalue = ExecuteQuery("SELECT customer_since"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_since = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Customer Since";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Customer Since Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Valid Customer Since!</font>";
							}
						}
						if (name.equals("txt_customer_landmark")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_landmark"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_landmark = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "LandMark";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Landmark Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter LandMark!</font>";
							}
						}
						if (name.equals("drop_customer_soe_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String oldSoeId = ExecuteQuery("SELECT customer_soe_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id= " + customer_id + " ");
								String history_oldvalue = ExecuteQuery("SELECT  soe_name"
										+ " FROM " + compdb(comp_id) + "axela_soe"
										+ " WHERE soe_id= " + oldSoeId + " ");
								String soe_name = ExecuteQuery("SELECT  soe_name"
										+ " FROM " + compdb(comp_id) + "axela_soe"
										+ " WHERE soe_id= " + value + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_soe_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "SOE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + soe_name + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "SOE Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select SOE!</font>";
							}
						}
						if (name.equals("drop_customer_sob_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String oldSobId = ExecuteQuery("SELECT customer_sob_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id= " + customer_id + " ");
								String history_oldvalue = ExecuteQuery("SELECT  sob_name"
										+ " FROM " + compdb(comp_id) + "axela_sob"
										+ " WHERE sob_id= " + oldSobId + " ");
								String sob_name = ExecuteQuery("SELECT  sob_name"
										+ " FROM " + compdb(comp_id) + "axela_sob"
										+ " WHERE sob_id= " + value + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_sob_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "SOB";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + sob_name + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "SOB Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select SOB!</font>";
							}
						}
						if (name.equals("dr_customer_emp_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT 	emp_name"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_id="
										+ "(SELECT customer_emp_id "
										+ " FROM " + compdb(comp_id) + "axela_customer where customer_id=" + customer_id + ")");
								String emp_Name = ExecuteQuery("SELECT emp_name"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_id=" + value + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_emp_id = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Executive";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + emp_Name + "')";
								updateQuery(StrSql);
								StrHTML = "Executive Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Select Executive!</font>";
							}
						}
						if (name.equals("txt_customer_notes")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_notes"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_notes = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Notes";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Notes Updated!";
							}
							else {
								StrHTML = "<font color=\"red\">Enter Notes!</font>";
							}
						}
						if (name.equals("chk_customer_active")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT customer_active"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id=" + customer_id + " ");
								if (history_oldvalue.equals("0")) {
									history_oldvalue = "InActive";
								}
								else {
									history_oldvalue = "Active";
								}
								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET"
										+ " customer_active = '" + value + "'"
										+ " WHERE customer_id = " + customer_id + "";
								String historyNewValue = "";
								if (value.equals("0")) {
									historyNewValue = "Inactive";
								}
								else {
									historyNewValue = "Active";
								}
								updateQuery(StrSql);
								String history_actiontype = "Active";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_history"
										+ " (history_customer_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + customer_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + historyNewValue + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Customer Active Updated!";
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {

		}
	}
}