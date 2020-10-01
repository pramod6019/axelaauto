package axela.invoice;
//aJIt 1st December, 2012

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Quote_Update extends Connect {

	public String emp_id = "0", emp_branch_id = "";
	public String add = "";
	public String update = "";
	public String comp_id = "0";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String msgChk = "";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0", session_id = "";
	public String branch_name = "";
	public String rateclass_id = "0";
	/* Quote Variables */
	public String quote_id = "0";
	public String quote_date = "";
	public String quotedate = "";
	public String quote_netqty = "";
	public String quote_netamt = "";
	public String quote_discamt = "";
	public String quote_grandtotal = "";
	public String quote_enquiry_id = "0", lead_id = "0";
	public String quote_totaltax = "";
	public String quote_emp_id = "0";
	public String quote_refno = "";
	public String quote_active = "";
	public String quote_notes = "";
	public String quote_auth = "", quote_auth_id = "";
	public String quote_auth_date = "", quote_authdate = "";
	public String quote_bill_address = "";
	public String quote_bill_city = "";
	public String quote_bill_state = "";
	public String quote_bill_pin = "";
	public String quote_ship_address = "";
	public String quote_ship_city = "";
	public String quote_ship_state = "";
	public String quote_ship_pin = "";
	public String quote_desc = "", quote_terms = "";
	public String quote_entry_id = "0";
	public String quote_entry_by = "";
	public String quote_entry_date = "";
	public String quote_modified_id = "0";
	public String quote_modified_by = "";
	public String quote_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	/* End Of Quote Variables */
	/* Config Variables */
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String branch_quote_email_enable = "";
	public String branch_quote_email_format = "";
	public String branch_quote_email_sub = "";
	public String branch_quote_sms_enable = "";
	public String branch_quote_sms_format = "";
	public String branch_quote_email_exe_sub = "";
	public String branch_quote_email_exe_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";
	public String config_sales_quote_refno = "";
	public String emp_quote_priceupdate = "";
	public String emp_quote_discountupdate = "";
	public String emp_role_id = "";
	/* End of Config Variables */
	DecimalFormat deci = new DecimalFormat("#.##");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String contact_id = "0";
	public String quote_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String contact_city_id = "";
	public String state_id = "";
	public String quote_customer_id = "0";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public int i = 0;
	public String[] mode_active = new String[4];
	public String year_range = "";
	public String year_option = "";
	public String readOnly = "";
	public String display = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				// SOP("11111111111");
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_quote_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				quote_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				session_id = PadQuotes(request.getParameter("txt_session_id"));
				if (session_id.equals("") || session_id == null) {
					String key = "", possible = "0123456789";
					for (int i = 0; i < 9; i++) {
						key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
					}
					session_id = key;
				}
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				if (!quote_enquiry_id.equals("0") && add.equals("yes")) {
					GetEnquiryDetails(response);
				}

				empEditperm = ReturnPerm(comp_id, "emp_quote_edit", request);
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}

				if (add.equals("yes") && quote_enquiry_id.equals("0")) {
					branch_id = CNumeric((GetSession("quote_branch_id", request)) + "");
					rateclass_id = CNumeric((GetSession("quote_rateclass_id", request)) + "");
					SOP("rateclass_id===" + rateclass_id);
					if (rateclass_id.equals("0") || branch_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../invoice/invoice-branch.jsp?para=quote"));
					}
					branch_name = ExecuteQuery("SELECT concat(branch_name, ' (', branch_code,')') as branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " where branch_id = " + branch_id);
				}
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				year_option = "" + (Integer.parseInt(SplitYear(ToLongDate(kknow()))) - 2) + "";
				year_range = "[" + (Integer.parseInt(SplitYear(ToLongDate(kknow()))) - 1) + "," + (Integer.parseInt(SplitYear(ToLongDate(kknow()))) + 1) + "]";
				if (!emp_id.equals("0") || !emp_id.equals("")) {
					quote_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
				} else {
					quote_emp_id = emp_id;
				}

				PopulateContactDetails();

				PopulateConfigDetails();

				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					display = "none";
					if (!"yes".equals(addB)) {
						StrSql = "Select branch_quote_desc, branch_quote_terms, city_id, state_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = branch_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
								+ " where branch_id = " + branch_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);
						try {
							while (crs.next()) {
								contact_city_id = crs.getString("city_id");
								state_id = crs.getString("state_id");
								quote_desc = crs.getString("branch_quote_desc");
								quote_terms = crs.getString("branch_quote_terms");
							}
							crs.close();
						} catch (Exception ex) {
							SOPError("Axelaauto===" + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						}
						quote_date = ToLongDate(kknow());
						quotedate = strToShortDate(quote_date);
						quote_auth = "0";
						quote_netqty = "0";
						quote_netamt = "0";
						quote_grandtotal = "0";
						contact_title_id = "0";
						quote_totaltax = "0";
						quote_emp_id = emp_id;
						quote_active = "1";
						contact_mobile1 = "91-";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_quote_add", request).equals("1")) {
							if (!branch_id.equals("0")) {
								try {
									StrSql = "SELECT branch_city_id, branch_pin"
											+ " FROM " + compdb(comp_id) + "axela_branch"
											+ " WHERE branch_id = " + branch_id + "";
									CachedRowSet crs = processQuery(StrSql, 0);
									while (crs.next()) {
										branch_city_id = crs.getString("branch_city_id");
										branch_pin = crs.getString("branch_pin");
									}
									crs.close();
								} catch (Exception ex) {
									SOPError("Axelaauto===" + this.getClass().getName());
									SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
								}
							}
							quote_entry_id = emp_id;
							quote_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("quote-list.jsp?quote_id=" + quote_id + "&msg=Quote added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Quote".equals(deleteB)) {
						PopulateFields(request, response);
						contact_id = quote_contact_id;
						CopyToCart(request);
					} else if ("yes".equals(updateB) && !"Delete Quote".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_quote_edit", request).equals("1")) {
							quote_modified_id = emp_id;
							quote_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("quote-list.jsp?quote_id=" + quote_id + "&msg=Quote updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Quote".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_quote_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("quote-list.jsp?msg=Quote deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_id = PadQuotes(request.getParameter("txt_branch_id"));
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_contact_city_id")));
		state_id = CNumeric(PadQuotes(request.getParameter("dr_contact_state_id")));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		quote_auth = PadQuotes(request.getParameter("chk_quote_auth"));
		if (quote_auth.equals("on")) {
			quote_auth = "1";
		} else {
			quote_auth = "0";
		}
		quote_bill_address = PadQuotes(request.getParameter("txt_quote_bill_address"));
		quote_bill_city = PadQuotes(request.getParameter("txt_quote_bill_city"));
		quote_bill_pin = PadQuotes(request.getParameter("txt_quote_bill_pin"));
		quote_bill_state = PadQuotes(request.getParameter("txt_quote_bill_state"));
		quote_ship_address = PadQuotes(request.getParameter("txt_quote_ship_address"));
		quote_ship_city = PadQuotes(request.getParameter("txt_quote_ship_city"));
		quote_ship_pin = PadQuotes(request.getParameter("txt_quote_ship_pin"));
		quote_ship_state = PadQuotes(request.getParameter("txt_quote_ship_state"));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quotedate = PadQuotes(request.getParameter("txt_quote_date"));
		quote_netqty = CNumeric(PadQuotes(request.getParameter("txt_quote_qty")));
		quote_netamt = CNumeric(PadQuotes(request.getParameter("txt_quote_netamt")));
		quote_discamt = CNumeric(PadQuotes(request.getParameter("txt_quote_discamt")));
		quote_totaltax = CNumeric(PadQuotes(request.getParameter("txt_quote_totaltax")));
		quote_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_quote_grandtotal")));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
		quote_active = PadQuotes(request.getParameter("chk_quote_active"));
		if (quote_active.equals("on")) {
			quote_active = "1";
		} else {
			quote_active = "0";
		}
		// quote_emp_id =
		// CNumeric(PadQuotes(request.getParameter("dr_executive")));
		quote_notes = PadQuotes(request.getParameter("txt_quote_notes"));
		quote_entry_by = PadQuotes(request.getParameter("entry_by"));
		quote_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		// if (customer_id.equals("0") && quote_enquiry_id.equals("0") &&
		// status.equals("Add")) {
		// msg = msg + "<br>Select Customer!";
		// }

		if (quotedate.equals("")) {
			msg = msg + "<br>Enter Quote Date!";
		}
		if (!quotedate.equals("")) {
			if (isValidDateFormatShort(quotedate)) {
				quote_date = ConvertShortDateToStr(quotedate);
			} else {
				msg = msg + "<br>Enter Valid Quote Date!";
			}
		}
		if (status.equals("Add") && contact_id.equals("0")) {
			if (contact_title_id.equals("0")) {
				msg = msg + "<br>Select Title!";
			}
			if (contact_fname.equals("")) {
				msg = msg + "<br>Enter the First Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}
			if (!contact_lname.equals("")) {
				contact_lname = toTitleCase(contact_lname);
			}

			if (customer_name.equals("")) {
				customer_name = contact_fname + " " + contact_lname;
			} else {
				customer_name = toTitleCase(customer_name);
			}
			if (!config_customer_dupnames.equals("1") && !customer_name.equals("")) {
				StrSql = "Select coalesce(customer_name,'') FROM " + compdb(comp_id) + "axela_customer"
						+ " where customer_name = '" + customer_name + "'";
				if (!ExecuteQuery(StrSql).equals("") && ExecuteQuery(StrSql) != null) {
					msg = msg + "<br>Customer already exist!";
				}
			}
			if (contact_mobile1.equals("91-")) {
				contact_mobile1 = "";
			}
			if (contact_mobile1.equals("")) {
				msg = msg + "<br>Enter Contact Mobile!";
			} else {
				if (!IsValidMobileNo11(contact_mobile1)) {
					msg = msg + "<br>Enter Valid Contact Mobile!";
				}
			}
			if (!contact_phone1.equals("")) {
				if (!IsValidPhoneNo11(contact_phone1)) {
					msg = msg + "<br>Enter Valid Contact Phone!";
				}
			}
			if (!contact_email1.equals("")) {
				if (!IsValidEmail(contact_email1)) {
					msg = msg + "<br>Enter Valid Contact Email!";
				} else {
					contact_email1 = contact_email1.toLowerCase();
				}
			}
			if (contact_address.equals("")) {
				msg = msg + "<br>Enter Contact Address!";
			}

			if (contact_city_id.equals("0")) {
				msg = msg + "<br>Select Contact City!";
			}
			if (state_id.equals("0")) {
				msg = msg + "<br>Select Contact State!";
			}
			if (contact_pin.equals("")) {
				msg = msg + "<br>Enter Contact Pin!";
			} else if (!isNumeric(contact_pin)) {
				msg = msg + "<br>Contact Pin: Enter Numeric!";
			}
		}
		if (quote_bill_address.equals("")) {
			msg = msg + "<br>Enter Billing Address!";
		}
		if (quote_bill_city.equals("")) {
			msg = msg + "<br>Enter Billing City!";
		}
		if (quote_bill_pin.equals("")) {
			msg = msg + "<br>Enter Billing Pin!";
		}
		if (quote_bill_state.equals("")) {
			msg = msg + "<br>Enter Billing State!";
		}
		if (quote_ship_address.equals("")) {
			msg = msg + "<br>Enter Shipping Address!";
		}
		if (quote_ship_city.equals("")) {
			msg = msg + "<br>Enter Shipping City!";
		}
		if (quote_ship_pin.equals("")) {
			msg = msg + "<br>Enter Shipping Pin!";
		}
		if (quote_ship_state.equals("")) {
			msg = msg + "<br>Enter Shipping State!";
		}
		if (config_sales_quote_refno.equals("1")) {
			if (quote_refno.equals("")) {
				msg = msg + "<br>Enter Quote Reference No.!";
			} else {
				if (quote_refno.length() < 2) {
					msg = msg + "<br>Quote Reference No. Should be Atleast Two Digits! ";
				}
				if (!branch_id.equals("0")) {
					StrSql = "Select quote_refno FROM " + compdb(comp_id) + "axela_sales_quote"
							+ " where quote_branch_id = " + branch_id + ""
							+ " and quote_refno = '" + quote_refno + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " and quote_id != " + quote_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Quote Reference No. found!";
					}
				}
			}
		}

		if (!isNumeric(quote_totaltax)) {
			quote_totaltax = "0.0";
		}
		if (quote_grandtotal.equals("0.00")) {
			msg = msg + "<br>Quote Amount cannot be equal to zero!";
		}
		if (quote_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		msg = msgChk + msg;
	}

	public void GetEnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "Select enquiry_emp_id, lead_id, customer_id, customer_name, contact_id, rateclass_id,"
					+ " concat(title_desc,' ',contact_fname,' ',contact_lname) contactname,"
					+ " contact_mobile1, contact_email1, state_name, city_name, customer_address,"
					+ " customer_pin, enquiry_branch_id, concat(branch_name,' (',branch_code,')') as branchname"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead on lead_id = enquiry_lead_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	"
					+ " where enquiry_id = " + quote_enquiry_id + ""
					+ " group BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP(StrSqlBreaker(StrSql)+" StrSql");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					quote_emp_id = crs.getString("enquiry_emp_id");
					customer_id = crs.getString("customer_id");
					lead_id = crs.getString("lead_id");
					customer_name = crs.getString("customer_name");
					link_customer_name = "<a href=../customer/customer-list.jsp?customer_id=" + customer_id + ">" + customer_name + "</a>";

					contact_id = crs.getString("contact_id");
					contact_name = crs.getString("contactname");
					link_contact_name = "<a href=../customer/customer-contact-list.jsp?contact_id=" + contact_id + ">" + contact_name + "</a>";
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_email1 = crs.getString("contact_email1");
					branch_id = CNumeric(crs.getString("enquiry_branch_id"));
					rateclass_id = CNumeric(crs.getString("rateclass_id"));
					branch_name = crs.getString("branchname");

					if (!addB.equals("Add Quote") && add.equals("yes")) {
						quote_bill_state = crs.getString("state_name");
						quote_bill_city = crs.getString("city_name");
						quote_bill_address = crs.getString("customer_address");
						quote_bill_pin = crs.getString("customer_pin");
						quote_ship_state = quote_bill_state;
						quote_ship_city = quote_bill_city;
						quote_ship_address = quote_bill_address;
						quote_ship_pin = quote_bill_pin;
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (contact_id.equals("0")) {
					if (!contact_mobile1.equals("") || !contact_phone1.equals("") || !contact_email1.equals("")) {
						StrSql = "SELECT contact_customer_id, contact_id"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " where";
						if (!contact_mobile1.equals("")) {
							StrSql = StrSql + " contact_mobile1 = '" + contact_mobile1 + "'";
						}
						if ((!contact_mobile1.equals("") && !contact_phone1.equals("")) || (!contact_mobile1.equals("") && !contact_email1.equals(""))) {
							StrSql = StrSql + " or";
						}
						if (!contact_phone1.equals("")) {
							StrSql = StrSql + " contact_phone1 = '" + contact_phone1 + "'";
						}
						if (!contact_phone1.equals("") && !contact_email1.equals("")) {
							StrSql = StrSql + " or";
						}
						if (!contact_email1.equals("")) {
							StrSql = StrSql + " contact_email1 = '" + contact_email1 + "'";
						}
						CachedRowSet crs = processQuery(StrSql, 0);
						try {
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									quote_customer_id = crs.getString("contact_customer_id");
									quote_contact_id = crs.getString("contact_id");
								}
							} else {
								quote_customer_id = "0";
								quote_contact_id = "0";
							}
							crs.close();
						} catch (Exception ex) {
							SOPError("Axelaauto===" + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						}
					} else {
						quote_customer_id = "0";
						quote_contact_id = "0";
					}
				}

				if (quote_contact_id.equals("0") && quote_customer_id.equals("0")) {
					AddCustomer();
					AddContact(quote_customer_id);
				}

				StrSql = StrSql = "Insert into " + compdb(comp_id) + "axela_sales_quote"
						+ " (quote_branch_id,"
						+ " quote_no,"
						+ " quote_date,"
						+ " quote_customer_id,"
						+ " quote_contact_id,";
				if (!quote_enquiry_id.equals("0")) {
					StrSql = StrSql + " quote_lead_id,"
							+ " quote_enquiry_id,";
				}
				StrSql = StrSql + " quote_netamt,"
						+ " quote_discamt,"
						+ " quote_totaltax,"
						+ " quote_grandtotal,"
						+ " quote_bill_address,"
						+ " quote_bill_city,"
						+ " quote_bill_pin,"
						+ " quote_bill_state,"
						+ " quote_ship_address,"
						+ " quote_ship_city,"
						+ " quote_ship_pin,"
						+ " quote_ship_state,"
						+ " quote_desc,"
						+ " quote_terms,"
						+ " quote_emp_id,";
				if (config_sales_quote_refno.equals("1")) {
					StrSql = StrSql + " quote_refno,";
				}
				StrSql = StrSql + " quote_auth,"
						+ " quote_active,"
						+ " quote_notes,"
						+ " quote_entry_id,"
						+ " quote_entry_date)"
						+ " values"
						+ " (" + branch_id + ","
						+ " (Select coalesce(max(quote.quote_no),0)+1 "
						+ " FROM " + compdb(comp_id) + "axela_sales_quote as quote"
						+ " where quote.quote_branch_id = " + branch_id + "),"
						+ " '" + quote_date + "',"
						+ " " + quote_customer_id + ","
						+ " " + quote_contact_id + ",";
				if (!quote_enquiry_id.equals("0")) {
					StrSql = StrSql + " " + lead_id + ","
							+ " " + quote_enquiry_id + ",";
				}
				StrSql = StrSql + " '" + quote_netamt + "',"
						+ " '" + quote_discamt + "',"
						+ " '" + quote_totaltax + "',"
						+ " '" + Math.ceil(Double.parseDouble(quote_grandtotal)) + "',"
						+ " '" + quote_bill_address + "',"
						+ " '" + quote_bill_city + "',"
						+ " '" + quote_bill_pin + "',"
						+ " '" + quote_bill_state + "',"
						+ " '" + quote_ship_address + "',"
						+ " '" + quote_ship_city + "',"
						+ " '" + quote_ship_pin + "',"
						+ " '" + quote_ship_state + "',"
						+ " '" + quote_desc + "',"
						+ " '" + quote_terms + "',"
						+ " " + quote_emp_id + ",";
				if (config_sales_quote_refno.equals("1")) {
					StrSql = StrSql + " '" + quote_refno + "',";
				}
				StrSql = StrSql + " '" + quote_auth + "',"
						+ " '" + quote_active + "',"
						+ " '" + quote_notes + "',"
						+ " " + quote_entry_id + ","
						+ " '" + quote_entry_date + "')";

				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					quote_id = rs1.getString(1);
				}
				contact_id = quote_contact_id;

				PopulateContactDetails();

				// if (!quote_id.equals("0")) {
				// if (quote_active.equals("1")) {
				// if (comp_email_enable.equals("1")
				// && config_email_enable.equals("1")
				// && !config_admin_email.equals("")
				// && branch_quote_email_enable.equals("1")) {
				// if (!contact_email1.equals("")
				// && !branch_quote_email_format.equals("")
				// && !branch_quote_email_sub.equals("")) {
				// SendEmail();
				// }
				// }
				// if (comp_sms_enable.equals("1") &&
				// config_sms_enable.equals("1") &&
				// branch_quote_sms_enable.equals("1")) {
				// if (!branch_quote_sms_format.equals("") &&
				// !contact_mobile1.equals("")) {
				// SendSMS();
				// }
				// }
				// }
				// }

				// Copy items FROM quote_cart to quote_item
				AddItemFields(request);

				// Delete Old cart items
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_cart"
						+ " WHERE cart_time < " + ToShortDate(kknow()) + "";
				stmttx.execute(StrSql);

				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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

	public void AddCustomer() throws SQLException {
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_phone1,"
					+ " customer_email1,"
					+ " customer_address,"
					+ " customer_city_id,"
					+ " customer_pin,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " values"
					+ " (" + branch_id + ","
					+ " '" + customer_name + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_address + "',"
					+ " " + contact_city_id + ","
					+ " '" + contact_pin + "',"
					+ " '1',"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				quote_customer_id = rs.getString(1);
			}
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void AddContact(String customer_id) throws SQLException {
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_mobile1,"
					+ " contact_phone1,"
					+ " contact_email1,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_notes,"
					+ " contact_active,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " values"
					+ " (" + customer_id + ","
					+ " " + contact_title_id + ","
					+ " '" + contact_fname + "',"
					+ " '" + contact_lname + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_address + "',"
					+ " " + contact_city_id + ","
					+ " '" + contact_pin + "',"
					+ " '',"
					+ " '1',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				quote_contact_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// In update mode, copy all the items for that particular quote-
	// -to the cart table BY creating a new session
	protected void CopyToCart(HttpServletRequest request) {
		try {
			StrSql = "SELECT"
					+ " " + emp_id + ","
					+ " " + session_id + ","
					+ " quoteitem_id,"
					+ " quoteitem_rowcount,"
					+ " quoteitem_item_id,"
					+ " quoteitem_option_id,"
					+ " quoteitem_option_group,"
					+ " quoteitem_item_serial,"
					+ " quoteitem_price,"
					+ " quoteitem_qty,"
					+ " quoteitem_disc,"
					+ " quoteitem_tax,"
					+ " quoteitem_tax_id,"
					+ " quoteitem_tax_rate,"
					+ " quoteitem_total,"
					+ " " + ToLongDate(kknow()) + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " where quoteitem_quote_id = " + quote_id + "";

			StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_quote_cart"
					+ " (cart_emp_id,"
					+ " cart_session_id,"
					+ " cart_quoteitem_id,"
					+ " cart_rowcount,"
					+ " cart_item_id,"
					+ " cart_option_id,"
					+ " cart_option_group,"
					+ " cart_item_serial,"
					+ " cart_price,"
					+ " cart_qty,"
					+ " cart_discount,"
					+ " cart_tax,"
					+ " cart_tax_id,"
					+ " cart_tax_rate,"
					+ " cart_total,"
					+ " cart_time"
					+ ")"
					+ " " + StrSql + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddItemFields(HttpServletRequest request) throws SQLException {
		try {
			StrSql = "SELECT"
					+ " " + quote_id + ","
					+ " cart_rowcount,"
					+ " cart_item_id,"
					+ " cart_option_id,"
					+ " cart_option_group,"
					+ " cart_item_serial,"
					+ " cart_qty,"
					+ " cart_price,"
					+ " cart_discount,"
					+ " cart_tax,"
					+ " cart_tax_id,"
					+ " cart_tax_rate,"
					+ " cart_total"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_cart"
					+ " where cart_quoteitem_id=0 and cart_session_id = " + session_id + "";

			StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_quote_item"
					+ " (quoteitem_quote_id,"
					+ " quoteitem_rowcount,"
					+ " quoteitem_item_id,"
					+ " quoteitem_option_id,"
					+ " quoteitem_option_group,"
					+ " quoteitem_item_serial,"
					+ " quoteitem_qty,"
					+ " quoteitem_price,"
					+ " quoteitem_disc,"
					+ " quoteitem_tax,"
					+ " quoteitem_tax_id,"
					+ " quoteitem_tax_rate,"
					+ " quoteitem_total)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// protected void SendEmail() throws SQLException {
	// String msg, sub;
	// msg = branch_quote_email_format;
	// sub = branch_quote_email_sub;
	//
	// sub = "replace('" + sub + "','[BILLID]',quote_id)";
	// sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
	// sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
	// sub = "replace(" + sub +
	// ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
	// sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
	// sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
	// sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
	// sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
	// sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
	//
	// msg = "replace('" + msg + "','[BILLID]',quote_id)";
	// msg = "replace(" + msg + ",'[CUSTOMERID]',customer_id)";
	// msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
	// msg = "replace(" + msg +
	// ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// msg = "replace(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// msg = "replace(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
	// msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
	// msg = "replace(" + msg + ",'[EXENAME]',emp_name)";
	// msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
	// msg = "replace(" + msg + ",'[EXEPHONE1]',emp_phone1)";
	// msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
	//
	// try {
	// String StrSql = "SELECT"
	// + " '" + quote_contact_id + "',"
	// + " '" + contact_fname + " " + contact_lname + "',"
	// + " '" + config_admin_email + "',"
	// + " '" + contact_email1 + "',"
	// + " " + sub + ","
	// + " " + msg + ","
	// + " '" + ToLongDate(kknow()) + "',"
	// + " " + emp_id + ","
	// + " 0"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_enquiry on enquiry_id = quote_enquiry_id "
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_customer on customer_id = quote_customer_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_customer_contact on contact_id = quote_contact_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_emp on quote_entry_id = emp_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_title on contact_title_id = title_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_jobtitle on emp_jobtitle_id = jobtitle_id"
	// + " where quote_id = " + quote_id + "";
	//
	// StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
	// + " (email_contact_id,"
	// + " email_contact,"
	// + " email_FROM,"
	// + " email_to,"
	// + " email_subject,"
	// + " email_msg,"
	// + " email_date,"
	// + " email_entry_id,"
	// + " email_sent)"
	// + " " + StrSql + "";
	// stmttx.execute(StrSql);
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// msg = "<br>Transaction Error!";
	// SOPError("conn is closed.....");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// msg = "<br>Transaction Error!";
	// SOPError("connection rollback...");
	// }
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + e);
	// }
	// }
	//
	// protected void SendSMS() throws SQLException {
	// String msg = branch_quote_sms_format;
	//
	// msg = "replace('" + msg + "','[BILLID]',quote_id)";
	// msg = "replace(" + msg + ",'[CUSTOMERID]',customer_id)";
	// msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
	// msg = "replace(" + msg +
	// ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
	// msg = "replace(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
	// msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
	// msg = "replace(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
	// msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
	// msg = "replace(" + msg + ",'[EXENAME]',emp_name)";
	// msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
	// msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
	// msg = "replace(" + msg + ",'[EXEPHONE1]',emp_phone1)";
	// msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
	// try {
	// String StrSql = "SELECT"
	// + " " + quote_contact_id + ","
	// + " '" + contact_fname + " " + contact_lname + "',"
	// + " '" + contact_mobile1 + "',"
	// + " " + msg + ","
	// + " '" + ToLongDate(kknow()) + "',"
	// + " 0,"
	// + " " + emp_id + ""
	// + " FROM " + compdb(comp_id) + "axela_sales_quote"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_enquiry on enquiry_id = quote_enquiry_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_customer on customer_id = quote_customer_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_customer_contact on contact_id = quote_contact_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_emp on quote_entry_id = emp_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_title on contact_title_id = title_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_jobtitle on emp_jobtitle_id = jobtitle_id"
	// + " where quote_id = " + quote_id + "";
	//
	// StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
	// + " (sms_contact_id,"
	// + " sms_contact,"
	// + " sms_mobileno,"
	// + " sms_msg,"
	// + " sms_date,"
	// + " sms_sent,"
	// + " sms_entry_id)"
	// + " " + StrSql + "";
	// stmttx.execute(StrSql);
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// msg = "<br>Transaction Error!";
	// SOPError("conn is closed.....");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// msg = "<br>Transaction Error!";
	// SOPError("connection rollback...");
	// }
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + e);
	// }
	// }
	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_sales_quote.*, concat(branch_name, ' (', branch_code,')') as branch_name,"
					+ " rateclass_id, contact_lname, contact_fname, title_id, title_desc,"
					+ " contact_mobile1, contact_phone1, contact_email1, customer_name, customer_id, contact_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class on rateclass_id = branch_rateclass_id	"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = quote_emp_id"
					+ " where quote_id = " + quote_id + "" + BranchAccess;

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					quote_id = crs.getString("quote_id");
					branch_id = crs.getString("quote_branch_id");
					branch_name = crs.getString("branch_name");
					quote_contact_id = crs.getString("quote_contact_id");
					contact_title_id = crs.getString("title_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_phone1 = crs.getString("contact_phone1");
					contact_email1 = crs.getString("contact_email1");
					rateclass_id = crs.getString("rateclass_id");
					quote_date = crs.getString("quote_date");
					quotedate = strToShortDate(quote_date);
					quote_bill_address = crs.getString("quote_bill_address");
					quote_bill_city = crs.getString("quote_bill_city");
					quote_bill_pin = crs.getString("quote_bill_pin");
					quote_bill_state = crs.getString("quote_bill_state");
					branch_name = crs.getString("branch_name");
					quote_refno = crs.getString("quote_refno");
					quote_auth = crs.getString("quote_auth");

					quote_ship_address = crs.getString("quote_ship_address");
					quote_ship_city = crs.getString("quote_ship_city");
					quote_ship_pin = crs.getString("quote_ship_pin");
					quote_ship_state = crs.getString("quote_ship_state");
					quote_desc = crs.getString("quote_desc");
					quote_terms = crs.getString("quote_terms");
					quote_netamt = crs.getString("quote_netamt");
					quote_totaltax = crs.getString("quote_totaltax");
					quote_grandtotal = crs.getString("quote_grandtotal");
					quote_enquiry_id = crs.getString("quote_enquiry_id");
					quote_emp_id = crs.getString("quote_emp_id");
					quote_active = crs.getString("quote_active");
					quote_notes = crs.getString("quote_notes");

					quote_entry_id = crs.getString("quote_entry_id");
					if (!quote_entry_id.equals("")) {
						quote_entry_by = Exename(comp_id, Integer.parseInt(quote_entry_id));
					}
					entry_date = strToLongDate(crs.getString("quote_entry_date"));
					quote_modified_id = crs.getString("quote_modified_id");
					if (!quote_modified_id.equals("")) {
						quote_modified_by = Exename(comp_id, Integer.parseInt(quote_modified_id));
					}
					modified_date = strToLongDate(crs.getString("quote_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			CheckPageperm(response);
		}
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_quote"
						+ " SET"
						+ " quote_branch_id = " + branch_id + ","
						+ " quote_customer_id = " + quote_customer_id + ","
						+ " quote_contact_id = " + quote_contact_id + ","
						+ " quote_date = '" + quote_date + "',"
						+ " quote_netamt = '" + quote_netamt + "',"
						+ " quote_discamt = '" + quote_discamt + "',"
						+ " quote_totaltax = '" + quote_totaltax + "',"
						+ " quote_grandtotal = '" + Math.ceil(Double.parseDouble(quote_grandtotal)) + "',"
						+ " quote_bill_address = '" + quote_bill_address + "',"
						+ " quote_bill_city = '" + quote_bill_city + "',"
						+ " quote_bill_pin = '" + quote_bill_pin + "',"
						+ " quote_bill_state = '" + quote_bill_state + "',"
						+ " quote_ship_address = '" + quote_ship_address + "',"
						+ " quote_ship_city = '" + quote_ship_city + "',"
						+ " quote_ship_pin = '" + quote_ship_pin + "',"
						+ " quote_ship_state = '" + quote_ship_state + "',"
						+ " quote_desc = '" + quote_desc + "',"
						+ " quote_terms = '" + quote_terms + "',"
						+ " quote_emp_id  = " + quote_emp_id + ",";
				if (config_sales_quote_refno.equals("1")) {
					StrSql = StrSql + "quote_refno ='" + quote_refno + "',";
				}
				StrSql = StrSql + "quote_auth  ='" + quote_auth + "',"
						+ "quote_active  ='" + quote_active + "',"
						+ "quote_notes  ='" + quote_notes + "',"
						+ "quote_modified_id =" + quote_modified_id + ","
						+ "quote_modified_date  ='" + quote_modified_date + "' "
						+ "where quote_id = " + quote_id + " ";
				// SOP("Strsql in UpdateFields---" + StrSql);
				stmttx.execute(StrSql);
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " where quoteitem_quote_id = " + quote_id + ""
						+ " and quoteitem_id not in (Select cart_quoteitem_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_cart"
						+ " where cart_session_id = " + session_id + ")";
				stmttx.execute(StrSql);

				StrSql = "Update " + compdb(comp_id) + "axela_sales_quote_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_cart on cart_quoteitem_id = quoteitem_id"
						+ " SET"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_rowcount = " + compdb(comp_id) + "axela_sales_quote_cart.cart_rowcount,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_item_id = " + compdb(comp_id) + "axela_sales_quote_cart.cart_item_id,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_option_id = " + compdb(comp_id) + "axela_sales_quote_cart.cart_option_id,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_option_group = " + compdb(comp_id) + "axela_sales_quote_cart.cart_option_group,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_item_serial = " + compdb(comp_id) + "axela_sales_quote_cart.cart_item_serial,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_qty = " + compdb(comp_id) + "axela_sales_quote_cart.cart_qty,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_price = " + compdb(comp_id) + "axela_sales_quote_cart.cart_price,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_disc = " + compdb(comp_id) + "axela_sales_quote_cart.cart_discount,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_tax = " + compdb(comp_id) + "axela_sales_quote_cart.cart_tax,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_tax_id = " + compdb(comp_id) + "axela_sales_quote_cart.cart_tax_id,"
						+ " " + compdb(comp_id) + "axela_sales_quote_item.quoteitem_total = " + compdb(comp_id) + "axela_sales_quote_cart.cart_total"
						+ " where cart_session_id = " + session_id + ""
						+ " and quoteitem_quote_id = " + quote_id + "";
				stmttx.execute(StrSql);

				// Copy items FROM quote_cart to quote_item
				AddItemFields(request);

				// Delete Old cart items
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_sales_quote_cart"
						+ " where cart_time < " + ToShortDate(kknow()) + "";
				stmttx.execute(StrSql);

				StrSql = "Delete FROM " + compdb(comp_id) + "axela_sales_quote_cart"
						+ " where cart_session_id = " + session_id + "";
				stmttx.execute(StrSql);
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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

	protected void DeleteFields(HttpServletResponse response) throws Exception {
		CheckPageperm(null);
		StrSql = "Select so_id FROM " + compdb(comp_id) + "axela_sales_so"
				+ " where so_quote_id = " + quote_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Quote is associated with Sales ORDER!";
		}
		StrSql = "Select invoice_id FROM " + compdb(comp_id) + "axela_invoice"
				+ " where invoice_quote_id = " + quote_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Quote is associated with Invoice!";
		}
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " where quoteitem_quote_id = " + quote_id + "";
				stmttx.execute(StrSql);
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_sales_quote"
						+ " where quote_id = " + quote_id + "";
				stmttx.execute(StrSql);
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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

	void CheckPageperm(HttpServletResponse response) {
		try {
			String res = "";
			StrSql = "SELECT quote_id FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = quote_branch_id"
					+ " where quote_id = " + quote_id + BranchAccess;
			res = ExecuteQuery(StrSql);
			if (res.equals("")) {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Access denied!"));
				return;
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {

		StrSql = "SELECT "
				+ " COALESCE(brandconfig_quote_email_enable,'') as brandconfig_quote_email_enable,"
				+ " COALESCE(brandconfig_quote_sms_format,'') as brandconfig_quote_sms_format,"
				+ " COALESCE(brandconfig_quote_sms_enable,'') as brandconfig_quote_sms_enable,"
				+ " COALESCE(brandconfig_quote_email_sub,'') as brandconfig_quote_email_sub,"
				+ " COALESCE(brandconfig_quote_email_format,'') as brandconfig_quote_email_format,"
				+ " config_admin_email, config_email_enable, config_sms_enable,"
				+ " config_sales_quote_refno, config_customer_dupnames,"
				+ " comp_email_enable, comp_sms_enable, "
				+ " coalesce(emp.emp_quote_priceupdate,0) as emp_quote_priceupdate,"
				+ " coalesce(emp.emp_quote_discountupdate,0) as emp_quote_discountupdate"
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + ""
				+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
				+ " where admin.emp_id = " + emp_id + "";
		// SOP(StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				branch_quote_email_enable = crs.getString("branch_quote_email_enable");
				branch_quote_email_format = crs.getString("branch_quote_email_format");
				branch_quote_sms_enable = crs.getString("branch_quote_sms_enable");
				branch_quote_sms_format = crs.getString("branch_quote_sms_format");
				branch_quote_email_sub = crs.getString("branch_quote_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				config_sales_quote_refno = crs.getString("config_sales_quote_refno");
				emp_quote_priceupdate = crs.getString("emp_quote_priceupdate");
				emp_quote_discountupdate = crs.getString("emp_quote_discountupdate");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateState(String state_id, String span_id, String dr_state_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("state_id==" + state_id);
			StrSql = "SELECT state_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=").append(dr_state_id).append(" id=").append(dr_state_id)
					.append(" class=form-control onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_contact_city_id','").append(span_id).append("'); \">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id")).append("");
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCity(String state_id, String city_id, String dr_city_id) {
		StringBuilder Str = new StringBuilder();
		try {
			if (state_id.equals("")) {
				state_id = "0";
			}
			StrSql = "SELECT city_id, city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " where city_state_id = " + state_id + ""
					+ " ORDER BY city_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=").append(dr_city_id).append(" id=").append(dr_city_id).append("  class=form-control>");
			Str.append("<option value = 0>Select</option>");
			if (!state_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("city_id")).append("");
					Str.append(StrSelectdrop(crs.getString("city_id"), city_id));
					Str.append(">").append(crs.getString("city_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !quote_contact_id.equals("0")) {
				StrSql = "Select customer_id, contact_id, customer_name, contact_fname, contact_lname,"
						+ " contact_email1, contact_mobile1, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id";
				if (!contact_id.equals("0")) {
					StrSql = StrSql + " where contact_id = " + contact_id + "";
				} else if (!quote_contact_id.equals("0")) {
					StrSql = StrSql + " where contact_id = " + quote_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					quote_customer_id = crs.getString("customer_id");
					quote_contact_id = crs.getString("contact_id");
					contact_email1 = crs.getString("contact_email1");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contact_fname") + " "
							+ crs.getString("contact_lname") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value = 0>Select Executive</option>");

			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1' and emp_sales='1' and (emp_branch_id = " + branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + branch_id + "))"
					+ " group BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), quote_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
