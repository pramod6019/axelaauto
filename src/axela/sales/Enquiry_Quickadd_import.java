package axela.sales;

//Saiman 12th Feb 2013
// divya
// modified by sn 6, 7 may 2013

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

import axela.portal.City_Check;
import axela.preowned.Preowned_Quickadd;
import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Enquiry_Quickadd_import extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String msg1 = "";
	public String emp_enquiry_edit = "";
	public String contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_jobtitle = "";
	public String enquiry_id = "0";
	public String enquiry_branch_id = "0";
	public String enquiry_customer_id = "0";
	public String enquiry_contact_id = "0";
	public String enquiry_title = "";
	public String enquiry_desc = "";
	// p//ublic String enquirydate = "";
	public String enquiry_date = "";
	public String enquiry_close_date = "", close_date = "";
	public String enquiry_value_syscal = "";
	public String enquiry_avpresent = "";
	public String enquiry_manager_assist = "";
	public String enquiry_value = "";
	public String enquiry_emp_id = "0";
	public String enquiry_campaign_id = "0", followup_feedbacktype_id = "0";
	public String enquiry_status_id = "0";
	public String enquiry_status_date = "", enquiry_status_desc = "";
	public String enquiry_stage_id = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_sob_id = "0";
	public String enquiry_qcsno = "";
	public String enquiry_notes = "";
	public String enquiry_entry_id = "0", enquiry_entry_date = "";
	public String enquiry_priorityenquiry_id = "1";
	public String branch_id = "0";
	public String branch_name = "", branch_brand_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_email_formail = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String title_desc = "";
	public String contact_name = "";
	public String contact_info = "";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_email2 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String contact_city_id = "0";
	public String contact_pin = "", branch_pin = "";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	// public String BranchAccess = "";
	public String enquiry_team_id = "0";
	public String brandconfig_enquiry_email_enable = "";
	public String brandconfig_enquiry_email_sub = "";
	public String brandconfig_enquiry_email_format = "";
	public String brandconfig_enquiry_email_exe_enable = "";
	public String brandconfig_enquiry_email_exe_format = "";
	public String brandconfig_enquiry_sms_enable = "";
	public String brandconfig_enquiry_sms_exe_format = "";
	public String brandconfig_enquiry_sms_format = "";
	// public String config_admin_email = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sales_enquiry_refno = "";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String emp_role_id = "";
	public String config_sales_sob = "";
	public String enquiry_model_id = "0";
	public String model_name = "";
	public String enquiry_item_id = "0";
	// Brochure variables
	public String attachment = "";
	public String brandconfig_enquiry_brochure_email_enable = "";
	public String brandconfig_enquiry_brochure_email_format = "";
	public String brandconfig_enquiry_brochure_email_sub = "";
	// End of Brochure variables
	public String send_contact_email = "";
	public String crmfollowupdays_daycount = "";
	public String crmfollowup_followup_time = "";
	public String enquiry_dmsno = "", enquiry_loanfinancer = "",
			// enquiry_buyertype_id = "0",
			enquiry_buyertype_id = "0", enquiry_lead_id = "0";
	public String enquiry_enquirycat_id = "0";
	public String enquiry_custtype_id = "2";
	public String enquiry_enquirytype_id = "1";
	public String enquiry_refemp_id = "0";
	// public String enquiry_preownedmodel_id = "0";
	public String enquiry_preownedvariant_id = "0";
	public String enquiry_fueltype_id = "0";
	public String enquiry_prefreg_id = "0";
	public String enquiry_presentcar = "";
	public String enquiry_finance = "0";
	public String enquiry_budget = "";
	public String team_emp_email = "";
	public String branchtype_id = "0";
	public String team_preownedbranch_id = "0", team_preownedemp_id = "0";
	public String enquiry_tradein_preownedvariant_id = "0";
	public String soe_crm_enable = "";
	public String soe_active = "";
	public Preowned_Variant_Check variantcheck = new Preowned_Variant_Check();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				if (enquiry_branch_id.equals("0")) {
					enquiry_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (enquiry_branch_id.equals("0")) {
						enquiry_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1"
								+ " AND branch_branchtype_id"
								+ " IN (1, 2) LIMIT 1");
					}
				}

				// BranchAccess = GetSession("BranchAccess", request);
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				enquiry_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
				enquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_emp_id")));
				emp_enquiry_edit = ReturnPerm(comp_id, "emp_enquiry_edit", request);
				branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
				// SOP("branchtype_id-----------" + branchtype_id);
				if (!enquiry_branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code,')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + enquiry_branch_id);
				}
				PopulateConfigDetails();

				if (!enquiry_contact_id.equals("0")) {
					PopulateContactCustomerDetails(response);
				}
				if (!"yes".equals(addB)) {
					enquiry_emp_id = emp_id;
					enquiry_team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_emp_id = " + emp_id));
					// SOP("enquiry_emp_id---------" + enquiry_emp_id);
					// if (contact_mobile1.equals("")) {
					// contact_mobile1 = "91-";
					// }
					enquiry_date = strToShortDate(ToShortDate(kknow()));
					enquiry_close_date = strToShortDate(ToShortDate(kknow()));

					if (!enquiry_branch_id.equals("0")) {
						StrSql = "SELECT city_id, city_state_id"
								+ " FROM " + compdb(comp_id) + "axela_branch "
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " WHERE branch_id = " + enquiry_branch_id;

						// SOP("StrSql======++++" + StrSql);

						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							contact_city_id = crs.getString("city_id");
						}
						crs.close();
					}

				} else {
					GetValues(request, response);
					// if (ReturnPerm(comp_id, "emp_enquiry_add", request).equals("1")) {
					enquiry_entry_id = emp_id;
					enquiry_entry_date = ToLongDate(kknow());
					AddEnquiryFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (!enquiry_tradein_preownedvariant_id.equals("0") && !team_preownedbranch_id.equals("0") && !team_preownedemp_id.equals("0")) {

							AddPreOwnedEnquiry();
						}

						if (ReturnPerm(comp_id, "emp_enquiry_access", request).equals("0")) {
							response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Enquiry added successfully!"));
						}
						else {
							response.sendRedirect(response.encodeRedirectURL("enquiry-list.jsp?enquiry_id=" + enquiry_id + "&msg=Enquiry added successfully!"));
						}
					}
					// } else {
					// response.sendRedirect(AccessDenied());
					// }
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
		if (enquiry_contact_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_jobtitle = PadQuotes(request.getParameter("txt_jobtitle"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
			contact_email2 = PadQuotes(request.getParameter("txt_contact_email2"));
			contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
			contact_phone2 = PadQuotes(request.getParameter("txt_contact_phone2"));
			contact_address = PadQuotes(request.getParameter("txt_contact_address"));
			contact_city_id = CNumeric(PadQuotes(request.getParameter("maincity")));
			contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		}
		if (emp_enquiry_edit.equals("1")) {
			enquiry_date = PadQuotes(request.getParameter("txt_enquiry_date"));
		} else {
			enquiry_date = strToShortDate(ToShortDate(kknow()));
		}
		enquiry_close_date = PadQuotes(request.getParameter("txt_enquiry_close_date"));
		close_date = enquiry_close_date;
		enquiry_desc = PadQuotes(request.getParameter("txt_enquiry_desc"));
		enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
		enquiry_item_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_item_id")));
		enquiry_team_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_team")));
		enquiry_soe_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_soe_id")));
		enquiry_sob_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_sob_id")));
		enquiry_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_campaign_id")));
		enquiry_buyertype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_buyertype_id")));
		enquiry_enquirycat_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_enquirycat_id")));
		enquiry_notes = PadQuotes(request.getParameter("txt_enquiry_notes"));
		enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_enquirytype_id")));
		// enquiry_preownedmodel_id = CNumeric(PadQuotes(request
		// .getParameter("dr_enquiry_preownedmodel_id")));
		enquiry_preownedvariant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		enquiry_tradein_preownedvariant_id = CNumeric(PadQuotes(request.getParameter("enquiry_tradein_preownedvariant_id")));
		enquiry_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_fueltype_id")));
		enquiry_prefreg_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_prefreg_id")));
		enquiry_presentcar = PadQuotes(request.getParameter("txt_enquiry_presentcar"));
		enquiry_finance = CNumeric(PadQuotes(request.getParameter("dr_enquiry_finance")));
		enquiry_budget = CNumeric(PadQuotes(request.getParameter("txt_enquiry_budget")));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		String customername = "";
		if (branchtype_id.equals("1")) {
			enquiry_enquirytype_id = "1";
		}
		if (branchtype_id.equals("2")) {
			enquiry_enquirytype_id = "2";
		}
		if (enquiry_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		if (enquiry_contact_id.equals("0")) {
			if (contact_title_id.equals("0")) {
				msg = msg + "<br>Select Title!";
			}
			if (contact_fname.equals("")) {
				msg = msg + "<br>Enter the Contact Person Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}
			if (!contact_lname.equals("")) {
				contact_lname = toTitleCase(contact_lname);
			}
			if (customer_name.equals("")) {
				customername = (contact_fname + " " + contact_lname).trim();
				customer_name = toTitleCase(customername);
			} else {
				customername = toTitleCase(customer_name);
				customer_name = customername;
			}

			if (contact_mobile1.equals("91-"))
			{
				contact_mobile1 = "";
			}
			if (contact_phone1.equals("91-"))
			{
				contact_phone1 = "";
			}
			// if (contact_address.equals("")) {
			// msg = msg + "<br>Enter Contact Address!";
			// }
			if (contact_city_id.equals("")) {
				msg = msg + "<br>Select Contact City!";
			}
			// if (contact_pin.equals("")) {
			// msg = msg + "<br>Enter Contact Pin!";
			// }
			// else if (!isNumeric(contact_pin)) {
			// msg = msg + "<br>Contact Pin: Enter Numeric!";
			// }
		}

		if (contact_mobile1.equals("") && contact_phone1.equals("")) {
			msg = msg + "<br>Enter Either Contact Mobile 1 or Phone 1!";
		} else if (!contact_mobile1.equals("")) {
			if (!IsValidMobileNo11(contact_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			} else {

				StrSql = "SELECT emp_mobile1, emp_mobile2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_mobile1.equals(crs.getString("emp_mobile1"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
					if (contact_mobile1.equals(crs.getString("emp_mobile2"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
				}
				crs.close();

				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_status_id = 1 "
						+ " AND (contact_mobile1 = '" + contact_mobile1 + "'"
						+ " OR contact_mobile2 = '" + contact_mobile1 + "') "
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				// SOP("StrSql--------------" + StrSqlBreaker(StrSql));
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Mobile 1 Found!";
				}
			}
		}
		if (!contact_mobile2.equals("")) {
			if (!IsValidMobileNo11(contact_mobile2)) {
				msg = msg + "<br>Enter Valid Contact Mobile 2!";
			}
			else {
				StrSql = "SELECT emp_mobile1, emp_mobile2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_mobile2.equals(crs.getString("emp_mobile1"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
					if (contact_mobile2.equals(crs.getString("emp_mobile2"))) {
						msg = msg + "<br>" + emp_name + "'s Mobile No. can't be used!";
					}
				}
				crs.close();

				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_status_id = 1 "
						+ " AND (contact_mobile1 = '" + contact_mobile2 + "'"
						+ " OR contact_mobile2 = '" + contact_mobile2 + "') "
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Mobile 2 Found!";
				}
			}
		}
		if (!contact_email1.equals("")) {
			if (!IsValidEmail(contact_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1!";
			}
			else {
				StrSql = "SELECT emp_email1, emp_email2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_email1.equals(crs.getString("emp_email1"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
					if (contact_email1.equals(crs.getString("emp_email2"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
				}
				crs.close();

				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_status_id = 1 AND contact_email1 = '" + contact_email1 + "'"
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Email 1 Found!!";
				}
				contact_email1 = contact_email1.toLowerCase();

			}
		}
		if (!contact_email2.equals("")) {
			if (!IsValidEmail(contact_email2)) {
				msg = msg + "<br>Enter valid Contact Email 2!";
			}
			else {
				StrSql = "SELECT emp_email1, emp_email2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_email2.equals(crs.getString("emp_email1"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
					if (contact_email2.equals(crs.getString("emp_email2"))) {
						msg = msg + "<br>" + emp_name + "'s Email can't be used!";
					}
				}
				crs.close();
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_status_id = 1 AND contact_email2 = '" + contact_email2 + "'"
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Email 2 Found!!";
				}
				contact_email2 = contact_email2.toLowerCase();
			}
		}
		if (!contact_phone1.equals("")) {
			if (!IsValidPhoneNo11(contact_phone1)) {
				msg = msg + "<br>Enter Valid Contact Phone!";
			}
			else {
				StrSql = "SELECT emp_phone1, emp_phone2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_phone1.equals(crs.getString("emp_phone1"))) {
						msg = msg + "<br>" + emp_name + "'s Phone No. can't be used!";
					}
					if (contact_phone1.equals(crs.getString("emp_phone2"))) {
						msg = msg + "<br>" + emp_name + "'s Phone No. can't be used!";
					}
				}
				crs.close();

				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_status_id = 1"
						+ " AND (contact_phone1 = '" + contact_phone1 + "'"
						+ " OR contact_phone2 = '" + contact_phone1 + "')"
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Phone 1 Found!";
				}
			}
		}
		if (!contact_phone2.equals("")) {
			if (!IsValidPhoneNo11(contact_phone2)) {
				msg = msg + "<br>Enter Valid Contact Phone 2!";
			}
			else {
				StrSql = "SELECT emp_phone1, emp_phone2, emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.next()) {
					emp_name = crs.getString("emp_name");
					if (contact_phone2.equals(crs.getString("emp_phone1"))) {
						msg = msg + "<br>" + emp_name + "'s Phone No. can't be used!";
					}
					if (contact_phone2.equals(crs.getString("emp_phone2"))) {
						msg = msg + "<br>" + emp_name + "'s Phone No. can't be used!";
					}
				}
				crs.close();
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " where enquiry_status_id = 1"
						+ " AND (contact_phone1 = '" + contact_phone2 + "'"
						+ " OR contact_phone2 = '" + contact_phone2 + "')"
						+ " AND enquiry_branch_id = " + enquiry_branch_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Phone 2 Found!";
				}
			}
		}

		if (emp_enquiry_edit.equals("1")) {
			if (enquiry_date.equals("")) {
				msg = msg + "<br>Enter Date!";
			} else {
				if (!isValidDateFormatShort(enquiry_date)) {
					msg = msg + "<br>Enter Valid Date!";
				}
				if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(enquiry_date))) {
					msg = msg + " <br>Date can't be greater than Current Date!";
				}
			}
		}

		if (enquiry_close_date.equals("")) {
			msg = msg + "<br>Enter Closed Date!";
		} else {
			if (isValidDateFormatShort(enquiry_close_date)) {
				close_date = ConvertShortDateToStr(enquiry_close_date);
				if (Long.parseLong(ConvertShortDateToStr(enquiry_close_date)) < Long.parseLong(ToShortDate(kknow()))) {
					// msg = msg + " <br>Close Date cannot be less than Current Date!";
				}
			} else {
				msg = msg + "<br>Enter valid Closed Date!";
			}
		}
		if (enquiry_enquirytype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}

		if (enquiry_title.equals("")) {
			enquiry_title = ExecuteQuery("SELECT brand_title"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
					+ " WHERE branch_id = " + enquiry_branch_id);
		}

		if (enquiry_enquirytype_id.equals("1")) {
			if (enquiry_model_id.equals("0")) {
				msg = msg + "<br>Select Model!";
			}
			if (enquiry_item_id.equals("0")) {
				msg = msg + "<br>Select Variant!";
			}
		}
		if (enquiry_enquirytype_id.equals("2")) {
			if (enquiry_preownedvariant_id.equals("0")) {
				msg = msg + "<br>Select Pre Owned Model!";
			}
			// else {
			// model_name = ExecuteQuery("select preownedmodel_name from "
			// + compdb(comp_id)
			// + "axela_preowned_model where preownedmodel_id = "
			// + enquiry_preownedmodel_id);
			// enquiry_title = "Pre Owned " + model_name;
			// }
			// if (enquiry_preownedvariant_id.equals("0")) {
			// msg = msg + "<br>Select Pre Owned Variant!";
			// }
			// if (enquiry_budget.equals("0")) {
			// msg = msg + "<br>Enter Budget!";
			// }
			// if (enquiry_fueltype_id.equals("0")) {
			// msg = msg + "<br>Select Fuel Type!";
			// }
			// if (enquiry_finance.equals("0")) {
			// msg = msg + "<br>Select Finance!";
			// }
		}

		if (enquiry_buyertype_id.equals("0") && !branch_brand_id.equals("1")) {
			msg = msg + "<br>Select Type of Buyer!";
		}
		if (enquiry_enquirycat_id.equals("0") && (branch_brand_id.equals("55"))) {
			msg = msg + "<br>Select Category!";
		}
		// if (enquiry_team_id.equals("0")) {
		// msg = msg + "<br>Select Team!";
		// }

		if (enquiry_emp_id.equals("0")) {
			msg = msg + "<br>Select Sales Consultant!";
		}
		if (config_sales_soe.equals("1")) {
			if (enquiry_soe_id.equals("0")) {
				msg = msg + "<br>Select Source of Enquiry!";
			}
		}
		if (config_sales_sob.equals("1")) {
			if (enquiry_sob_id.equals("0")) {
				msg = msg + "<br>Select Source of Bussiness!";
			}
		}
		if (config_sales_campaign.equals("1")) {
			if (enquiry_campaign_id.equals("0")) {
				msg = msg + "<br>Select Campaign!";
			}
		}
		if (!enquiry_team_id.equals("0") && !enquiry_tradein_preownedvariant_id.equals("0")) {
			StrSql = "SELECT team_preownedbranch_id, team_preownedemp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_id = " + enquiry_team_id;
			// SOP("StrSql-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				team_preownedbranch_id = crs.getString("team_preownedbranch_id");
				team_preownedemp_id = crs.getString("team_preownedemp_id");
			}
			if (team_preownedbranch_id.equals("0")) {
				msg = msg + "<br>Pre-Owned Branch is not configured for this Team!";
			}
			if (team_preownedemp_id.equals("0")) {
				msg = msg + "<br>Pre-Owned Consultant is not configured for this Team!";
			}
			crs.close();
		}
		if (!enquiry_branch_id.equals("0")) {
			if (!enquiry_emp_id.equals("0")) {
				StrSql = "SELECT COALESCE(team_crm_emp_id, 0) AS team_crm_emp_id,"
						+ " COALESCE(emp_email1, '') AS team_emp_email"
						+ " FROM " + compdb(comp_id) + "axela_sales_team"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = team_emp_id"
						+ " WHERE team_branch_id = " + enquiry_branch_id + ""
						+ " AND teamtrans_emp_id=" + enquiry_emp_id + " limit 1";
				// SOP("StrSql------c8---" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					// crmfollowup_crm_emp_id = crs.getString("team_crm_emp_id");
					team_emp_email = crs.getString("team_emp_email");
				}
				crs.close();
			}
		}

		if (!enquiry_emp_id.equals(emp_id)) {
			enquiry_refemp_id = emp_id;
		} else {
			enquiry_refemp_id = "0";
		}
	}
	public void AddEnquiryFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			if (contact_pin.equals("")) {
				contact_pin = branch_pin;
			}
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (enquiry_customer_id.equals("0") || (!enquiry_customer_id.equals("0") && config_customer_dupnames.equals("1"))) {
					AddCustomerFields();
				}
				if (enquiry_contact_id.equals("0")) {
					AddContactFields();
				}
				// SOP("emp_id===" + emp_id);
				// SOP("enquiry_enquirytype_id==222==" + enquiry_enquirytype_id);

				// SOP("enquiry_model_id======2============" + enquiry_model_id);

				if (!enquiry_customer_id.equals("0") && !enquiry_contact_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry"
							+ " (" + " enquiry_no,"
							+ " enquiry_branch_id,"
							+ " enquiry_customer_id,"
							+ " enquiry_contact_id,"
							+ " enquiry_enquirytype_id,"
							+ " enquiry_refemp_id,"
							+ " enquiry_title,"
							+ " enquiry_desc,"
							+ " enquiry_date,"
							+ " enquiry_model_id,"
							+ " enquiry_item_id,"
							+ " enquiry_close_date,"
							+ " enquiry_value_syscal,"
							+ " enquiry_avpresent,"
							+ " enquiry_manager_assist,"
							// + " enquiry_preownedmodel_id,"
							+ " enquiry_preownedvariant_id,"
							+ " enquiry_tradein_preownedvariant_id,"
							+ " enquiry_fueltype_id,"
							+ " enquiry_prefreg_id,"
							+ " enquiry_presentcar,"
							+ " enquiry_finance,"
							+ " enquiry_value,"
							+ " enquiry_emp_id,"
							+ " enquiry_team_id,"
							+ " enquiry_stage_id, ";
					if (config_sales_soe.equals("1")) {
						StrSql = StrSql + " enquiry_soe_id,";
					}
					if (config_sales_sob.equals("1")) {
						StrSql = StrSql + " enquiry_sob_id,";
					}
					if (config_sales_campaign.equals("1")) {
						StrSql = StrSql + " enquiry_campaign_id, ";
					}

					StrSql = StrSql + " enquiry_status_id,"
							+ " enquiry_status_date,"
							+ " enquiry_status_desc,"
							+ " enquiry_priorityenquiry_id,"
							+ " enquiry_lead_id,"
							+ " enquiry_notes,";

					if (config_sales_enquiry_refno.equals("1")) {
						StrSql = StrSql + "enquiry_qcsno,";
					}
					StrSql = StrSql
							+ "enquiry_dmsno,"
							+ " enquiry_buyertype_id,"
							+ " enquiry_enquirycat_id,"
							+ " enquiry_custtype_id,"
							// + " enquiry_buyertype_id,"
							+ " enquiry_entry_id,"
							+ " enquiry_entry_date,"
							+ " enquiry_modified_id,"
							+ " enquiry_modified_date)"
							+ " values "
							+ "((SELECT COALESCE(MAX(enquiry.enquiry_no),0)+1 "
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry AS enquiry "
							+ " WHERE enquiry.enquiry_branch_id  = " + enquiry_branch_id + "),"
							+ " " + enquiry_branch_id + ","
							+ " " + enquiry_customer_id + ","
							+ " " + enquiry_contact_id + ","
							+ " " + enquiry_enquirytype_id + ","
							+ enquiry_refemp_id + "," // for enquiry_refemp_id taken from session
							+ " '" + enquiry_title + "',"
							+ " '" + enquiry_desc + "',"
							+ " '" + ConvertShortDateToStr(enquiry_date) + "',"
							+ " ";
					if (enquiry_enquirytype_id.equals("1")) {
						StrSql = StrSql + " " + enquiry_model_id + ","
								+ " " + enquiry_item_id + ",";
					} else if (enquiry_enquirytype_id.equals("2")) {
						StrSql = StrSql + "(SELECT model_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_name  = 'Pre Owned'),"
								+ " (SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_name = 'Pre Owned'),";
					}
					StrSql = StrSql + " '" + close_date + "'," + " '1',";
					StrSql = StrSql + " '0'," + " '0'," + ""
							// + " " + enquiry_preownedmodel_id + ","
							+ " " + enquiry_preownedvariant_id + ","
							+ " " + enquiry_tradein_preownedvariant_id + "," // enquiry_tradein_preownedvariant_id
							+ " " + enquiry_fueltype_id + ","
							+ " " + enquiry_prefreg_id + ","
							+ " '" + enquiry_presentcar + "',"
							+ " " + enquiry_finance + ","
							+ " " + enquiry_budget + ","
							+ " " + enquiry_emp_id + ","
							+ " " + enquiry_team_id + ", 1,";

					// SOP("emp_id===" + emp_id);
					if (config_sales_soe.equals("1")) {
						StrSql = StrSql + " " + enquiry_soe_id + ",";
					}
					if (config_sales_sob.equals("1")) {
						StrSql = StrSql + " " + enquiry_sob_id + ",";
					}
					if (config_sales_campaign.equals("1")) {
						StrSql = StrSql + " " + enquiry_campaign_id + ",";
					}
					StrSql = StrSql + "4," + " ''," + " '',"// status
							+ " " + enquiry_priorityenquiry_id + ","
							+ " " + enquiry_lead_id + ","
							// + " 1,"
							+ " '" + enquiry_notes + "',";
					if (config_sales_enquiry_refno.equals("1")) {
						StrSql = StrSql + "'" + enquiry_qcsno + "',";
					}
					StrSql = StrSql + "'" + enquiry_dmsno + "'," + " "
							+ enquiry_buyertype_id + ","
							+ enquiry_enquirycat_id + ","
							// + " '" + enquiry_loanfinancer + "',"
							+ " " + 2 + ","
							+ " " + enquiry_entry_id + ","
							+ " '" + enquiry_entry_date + "',"
							+ " 0,"
							+ " '')";
					// SOP("StrSql---------enquiry---quick--add-----" + StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						enquiry_id = rs.getString(1);
					}
					rs.close();

					if (!enquiry_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history "
								+ "(history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ", "
								+ emp_id + ", "
								+ "'" + ToLongDate(kknow()) + "', "
								+ "'NEW_ENQUIRY', '', 'New Enquiry added')";
						// SOP("hist==" + StrSql);
						stmttx.execute(StrSql);

						if (!contact_email2.equals("") && !contact_email1.equals("")) {
							send_contact_email = contact_email1 + "," + contact_email2;
						} else if (!contact_email1.equals("")) {
							send_contact_email = contact_email1;
						}

						if (comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								&& !branch_email1.equals("")
								// && !emp_email_formail.equals("")
								&& brandconfig_enquiry_email_enable.equals("1")) {
							if (!contact_email1.equals("")
									&& !brandconfig_enquiry_email_format.equals("")
									&& !brandconfig_enquiry_email_sub.equals("")) {
								// SendEmail();
							}

							if (!emp_email_formail.equals("")
									&& !branch_email1.equals("")
									&& !brandconfig_enquiry_email_exe_format
											.equals("")
									&& !brandconfig_enquiry_email_exe_enable.equals("")) {
								// SendEmailToExecutive();
							}
						}

						if (comp_sms_enable.equals("1")
								&& config_sms_enable.equals("1")
								&& brandconfig_enquiry_sms_enable.equals("1")) {
							if (!brandconfig_enquiry_sms_format.equals("")) {
								if (!contact_mobile1.equals("")) {
									// SendSMS(contact_mobile1);
								}
								if (!contact_mobile2.equals("")) {
									// SendSMS(contact_mobile2);
								}
							}
							if (!brandconfig_enquiry_sms_exe_format.equals("")) {
								if (!emp_mobile1.equals("")) {
									// SendSMSToExecutive(emp_mobile1);
								}
								if (!emp_mobile2.equals("")) {
									// SendSMSToExecutive(emp_mobile2);
								}
							}
						}

						if (!enquiry_emp_id.equals("0")) {
							SOP("123");
							// AddFollowupFields();
							SOP("456");
						}

						if (comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								// && !branch_email1.equals("")
								&& !emp_email_formail.equals("")
								&& brandconfig_enquiry_brochure_email_enable.equals("1")
								&& !contact_email1.equals("")
								&& !brandconfig_enquiry_brochure_email_format.equals("")
								&& !brandconfig_enquiry_brochure_email_sub.equals("")) {
							// BrochureAttachment();
							if (!attachment.equals("")) {
								// SendBrochureEmail();
							}
						}
					}
				}
				conntx.commit();
				String date = ConvertShortDateToStr(enquiry_date).substring(0, 8) + enquiry_entry_date.substring(8, 14);
				// checking condition wheather soe_active and
				// soe_crm_enable are active
				StrSql = "SELECT soe_crm_enable, soe_active"
						+ " FROM " + compdb(comp_id) + "axela_soe "
						+ " WHERE soe_id = " + enquiry_soe_id;
				// SOP("StrSql=====crmcustom=++++" + StrSql);

				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					soe_crm_enable = crs.getString("soe_crm_enable");
					soe_active = crs.getString("soe_active");
				}
				crs.close();
				if (soe_crm_enable.equals("1") && soe_active.equals("1")) {
					// AddCustomCRMFields(enquiry_id, date, "new", comp_id);
				}

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					SOP("Transaction Error==");
					conntx.rollback();
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
				msg = "<br>Transaction Error!";
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
					+ " customer_mobile2,"
					+ " customer_city_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_soe_id,"
					+ " customer_sob_id," + " customer_emp_id,"
					+ " customer_since," + " customer_address,"
					+ " customer_pin," + " customer_email1,"
					+ " customer_email2," + " customer_phone1,"
					+ " customer_phone2," + " customer_active,"
					+ " customer_notes," + " customer_entry_id,"
					+ " customer_entry_date," + " customer_modified_id,"
					+ " customer_modified_date)"
					+ " VALUES "
					+ " ('" + enquiry_branch_id + "'," // enquiry_branch_id
					+ " '" + customer_name + "'," // customer_name
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " '" + contact_mobile2 + "'," // contact_mobile2
					+ " '" + contact_city_id + "'," // contact_city_id
					+ " 32," // customer_accgroup_id
					+ " 1," // customer_type
					+ " '" + enquiry_soe_id + "'," // enquiry_soe_id
					+ " '" + enquiry_sob_id + "'," // enquiry_sob_id
					+ " " + enquiry_emp_id + "," // enquiry_emp_id
					+ " '" + ToShortDate(kknow()) + "'," // customer_since
					+ " '" + contact_address + "'," // contact_address
					+ " '" + contact_pin + "'," // contact_pin
					+ " '" + contact_email1 + "'," // contact_email1
					+ " '" + contact_email2 + "'," // contact_email2
					+ " '" + contact_phone1 + "'," // contact_phone1
					+ " '" + contact_phone2 + "'," // contact_phone2
					+ " '1'," // customer_active
					+ " ''," // customer_notes
					+ " " + emp_id + "," // customer_entry_id
					+ " '" + ToLongDate(kknow()) + "'," // customer_entry_date
					+ " 0," // customer_modified_id
					+ " '')"; // customer_modified_date
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				enquiry_customer_id = rs.getString(1);
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

	protected void AddContactFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_phone1,"
					+ " contact_phone2,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " VALUES "
					+ " (" + enquiry_customer_id + "," // contact_customer_id
					+ " 1," // contact_contacttype_id
					+ " " + contact_title_id + "," // contact_title_id
					+ " '" + contact_fname + "'," // contact_fname
					+ " '" + contact_lname + "'," // contact_lname
					+ " '" + contact_jobtitle + "'," // contact_jobtitle
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " '" + contact_mobile2 + "'," // contact_mobile2
					+ " '" + contact_email1 + "'," // contact_email1
					+ " '" + contact_email2 + "'," // contact_email2
					+ " '" + contact_phone1 + "'," // contact_phone1
					+ " '" + contact_phone2 + "'," // contact_phone2
					+ " '" + contact_address + "'," // contact_address
					+ " '" + contact_city_id + "'," // contact_city_id
					+ " '" + contact_pin + "'," // contact_pin
					+ " '1'," // contact_active
					+ " ''," // contact_notes
					+ " " + emp_id + "," // contact_entry_id
					+ " '" + ToLongDate(kknow()) + "')"; // contact_entry_date
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				enquiry_contact_id = rs.getString(1);
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

	public void AddFollowupFields() throws SQLException {
		try {
			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " (followup_enquiry_id,"
					+ " followup_emp_id,"
					+ " followup_followup_time,"
					+ " followup_followuptype_id,"
					+ " followup_desc,"
					+ " followup_feedbacktype_id,"
					+ " followup_entry_id,"
					+ " followup_entry_time,"
					+ " followup_trigger)"
					+ " VALUES"
					+ " (" + " '" + enquiry_id + "'," // followup_enquiry_id
					+ " " + enquiry_emp_id + "," // followup_emp_id
					+ " '" + ToLongDate(kknow()) + "'," // followup_followup_time
					+ " 1," // followup_followuptype_id
					+ " ''," // followup_desc
					+ " " + followup_feedbacktype_id + ", "
					+ " " + emp_id + "," // followup_entry_id
					+ " '" + ToLongDate(kknow()) + "'," // followup_entry_time
					+ " 0)"; // followup_trigger
			// SOP("AddFollowupFields==StrSql==" + StrSql);
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

	public void AddCustomCRMFields(String enquiry_id, String date, String crmtype, String comp_id) throws SQLException {
		try {
			// SOP("enquiry_id===" + enquiry_id);
			// SOP("comp_id===" + comp_id);
			enquiry_entry_date = ToLongDate(kknow());
			StrSql = "SELECT " + enquiry_id + ", "
					+ "COALESCE(("
					+ " SELECT"
					+ " CASE WHEN crmdays_exe_type = 1 THEN team_crm_emp_id"
					+ " WHEN crmdays_exe_type = 2 THEN enquiry_emp_id"
					+ " WHEN crmdays_exe_type = 3 THEN team_emp_id"
					+ " END" + " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
					+ " WHERE team_branch_id = enquiry_branch_id"
					+ " AND teamtrans_emp_id = enquiry_emp_id"
					+ " LIMIT 1), enquiry_emp_id) AS crmempid, "
					+ " crmdays_id, "
					+ " DATE_FORMAT(DATE_ADD(" + date + ","
					+ " INTERVAL (crmdays_daycount-1) DAY), '%Y%m%d%H%i%s')"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_brand_id = branch_brand_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			if (crmtype.equals("new")) {
				StrSql += " AND crmdays_lostfollowup=0 AND crmdays_testdrivefollowup=0 AND crmdays_homevisitfollowup=0";
			} else if (crmtype.equals("lost")) {
				StrSql += " AND crmdays_lostfollowup=1 AND crmdays_testdrivefollowup=0 AND crmdays_homevisitfollowup=0";
			} else if (crmtype.equals("testdrive")) {
				StrSql += " AND crmdays_lostfollowup=0 AND crmdays_testdrivefollowup=1 AND crmdays_homevisitfollowup=0";
			} else if (crmtype.equals("homevisit")) {
				StrSql += " AND crmdays_lostfollowup=0 AND crmdays_testdrivefollowup=0 AND crmdays_homevisitfollowup=1";
			}

			StrSql += " AND crmdays_active = 1 " + " AND crmdays_crmtype_id=1";
			// + " AND concat(enquiry_id, '-', crmdays_id) NOT IN "
			// + " (SELECT concat(crm_enquiry_id, '-', crm_crmdays_id) FROM " + compdb(comp_id) + "axela_sales_crm)";

			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_sales_crm"
					+ " (" + " crm_enquiry_id,"
					+ " crm_emp_id,"
					+ " crm_crmdays_id,"
					+ " crm_followup_time)" + StrSql;
			//
			SOP("StrSql------AddCustomCRMFields---------" + StrSql);
			updateQuery(StrSql);

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
	// For getting the attachments of brochure
	protected void BrochureAttachment() throws ServletException, IOException,
			SQLException {
		StrSql = "SELECT brochure_value, brochure_title  "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + enquiry_branch_id
				+ " WHERE brochure_rateclass_id = branch_rateclass_id "
				+ " AND brochure_brand_id = branch_brand_id "
				+ " AND (brochure_item_id=0 or brochure_item_id= " + enquiry_item_id + ")";
		// SOP("StrSql-----" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			attachment = "";
			while (crs.next()) {
				attachment = attachment + EnquiryBrochurePath(comp_id) + crs.getString("brochure_value") + ","
						+ crs.getString("brochure_title") + fileext(crs.getString("brochure_value"));
				attachment = attachment + ";";
			}
			attachment = attachment.substring(0, attachment.lastIndexOf(";"));
		}
		// SOP("attachment------" + attachment);
		crs.close();
	}

	protected void SendBrochureEmail() throws SQLException {
		String msg = "", sub = "";
		msg = brandconfig_enquiry_brochure_email_format;
		sub = brandconfig_enquiry_brochure_email_sub;

		sub = "replace('" + sub + "','[ENQUIRYID]', enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[BRANCHNAME]',branch_name)";
		sub = "replace(" + sub + ",'[BRANCHEMAIL1]',branch_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		msg = "replace('" + msg + "','[ENQUIRYID]',enquiry_id)";
		msg = "replace(" + msg + ",'[ENQUIRYNAME]',enquiry_title)";
		msg = "replace(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "replace(" + msg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		msg = "replace(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "replace(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "replace(" + msg + ",'[EXENAME]',emp_name)";
		msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "replace(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "replace(" + msg + ",'[BRANCHNAME]',branch_name)";
		msg = "replace(" + msg + ",'[BRANCHEMAIL1]',branch_email1)";
		msg = "replace(" + msg + ",'[MODELNAME]',model_name)";
		msg = "replace(" + msg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " enquiry_branch_id,"
					+ " '" + enquiry_contact_id + "',"
					+ " '" + contact_name + "',"
					+ " branch_email1,"
					+ " '" + send_contact_email + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id + "";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_attach1,"
					+ " email_date,"
					+ " email_entry_id," + " email_sent)" + " " + StrSql + "";
			// SOP("StrSql------SendBrochureEmail---" + StrSql);
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

	/* Eof code for Sending Brochure as attachment */

	protected void SendEmail() throws SQLException {
		String emailmsg, sub;
		emailmsg = brandconfig_enquiry_email_format;
		sub = brandconfig_enquiry_email_sub;

		sub = "replace('" + sub + "','[ENQUIRYID]',enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[BRANCHEMAIL1]',branch_email1)";
		sub = "replace(" + sub + ",'[BRANCHNAME]',branch_name)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		emailmsg = "replace('" + emailmsg + "','[ENQUIRYID]',enquiry_id)";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]',branch_name)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHEMAIL1]',branch_email1)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " enquiry_branch_id ,"
					+ " '" + enquiry_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + branch_email1 + "',"
					+ " '" + send_contact_email + "',"
					+ " '" + team_emp_email + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " "
					+ StrSql + "";
			// SOP("SendEmail-----------" + StrSql);
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

	protected void SendSMS(String contact_mobile) throws SQLException {
		String smsmsg = (brandconfig_enquiry_sms_format);

		smsmsg = "replace('" + smsmsg + "','[ENQUIRYID]',enquiry_id)";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',branch_email1)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";
		try {
			StrSql = "SELECT"
					+ " " + enquiry_branch_id + ","
					+ " " + enquiry_contact_id + ","
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + contact_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id;

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			SOP("-------Sendsms-----------" + StrSql);
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

	protected void SendEmailToExecutive() throws SQLException {
		String emailmsg, sub;
		emailmsg = brandconfig_enquiry_email_exe_format;
		sub = brandconfig_enquiry_email_exe_enable;

		sub = "replace('" + sub + "','[ENQUIRYID]',enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[BRANCHNAME]',branch_name)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		emailmsg = "replace('" + emailmsg + "','[ENQUIRYID]',enquiry_id)";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]',branch_name)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";
		try {

			StrSql = "SELECT"
					+ " enquiry_branch_id,"
					+ " " + enquiry_emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + emp_email_formail + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			// SOP("SendEmailToExecutive---" + StrSql);
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

	protected void SendSMSToExecutive(String emp_mobile) throws SQLException {
		String smsmsg = (brandconfig_enquiry_sms_exe_format);

		smsmsg = "replace('" + smsmsg + "','[ENQUIRYID]',enquiry_id)";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";
		smsmsg = "replace(" + smsmsg + ",'[SOE]',COALESCE(soe_name, ''))";
		try {
			StrSql = "SELECT"
					+ " " + enquiry_branch_id + ","
					+ " " + enquiry_emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + emp_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_emp_id,"
					+ " sms_emp,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("-------SendSMSToExecutive-----------" + StrSqlBreaker(StrSql));
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

	public void AddPreOwnedEnquiry() {
		try {
			Preowned_Quickadd preowned = new Preowned_Quickadd();
			preowned.comp_id = comp_id;
			preowned.emp_id = emp_id;
			preowned.preowned_branch_id = team_preownedbranch_id;
			preowned.preowned_customer_id = enquiry_customer_id;
			preowned.preowned_contact_id = enquiry_contact_id;
			preowned.preowned_title = "New Pre-Owned";
			preowned.preowned_sub_variant = "";
			preowned.preowned_extcolour_id = "0";
			preowned.preowned_intcolour_id = "0";
			preowned.preowned_options = "";
			preowned.preowned_date = strToShortDate(ToShortDate(kknow()));
			preowned.preowned_variant_id = enquiry_tradein_preownedvariant_id;
			preowned.preowned_fcamt = "0";
			preowned.preowned_noc = "";
			preowned.preowned_funding_bank = "";
			preowned.preowned_loan_no = "";
			preowned.preowned_insur_date = "";
			preowned.preowned_insurance_id = "0";
			preowned.preowned_ownership_id = "0";
			preowned.preowned_regdyear = "";
			preowned.preowned_manufyear = "";
			preowned.preowned_invoicevalue = "0";
			preowned.preowned_kms = "0";
			preowned.preowned_regno = "";
			preowned.preowned_expectedprice = "0";
			preowned.preowned_quotedprice = "0";
			preowned.preowned_fueltype_id = "0";
			preowned.preowned_close_date = strToShortDate(ToLongDate(kknow()));
			preowned.preowned_emp_id = team_preownedemp_id;
			preowned.preownedteam_id = CNumeric(ExecuteQuery("SELECT preownedteamtrans_team_id FROM " + compdb(comp_id) + "axela_preowned_team_exe WHERE preownedteamtrans_emp_id ="
					+ team_preownedemp_id));
			preowned.preowned_sales_emp_id = enquiry_emp_id;
			preowned.preowned_enquiry_id = enquiry_id;
			preowned.preowned_preownedstatus_id = "1";
			preowned.preowned_preownedstatus_date = "";
			preowned.preowned_preownedstatus_desc = "";
			preowned.preowned_prioritypreowned_id = "1";
			preowned.preowned_notes = "";
			preowned.preowned_desc = "";
			preowned.preowned_entry_id = emp_id;
			preowned.preowned_entry_date = ToLongDate(kknow());
			preowned.PopulateConfigDetails();
			preowned.PopulateContactCustomerDetails();
			preowned.AddPreownedFields();
			preowned.preowned_id = preowned.preowned_id;
			// SOP("preowned.preowned_id----------" + preowned.preowned_id);
			if (!preowned.preowned_id.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history "
						+ "(history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " values"
						+ " (" + enquiry_id + ", "
						+ emp_id + ", "
						+ "'" + ToLongDate(kknow()) + "', "
						+ "'ADD EVALUATION', "
						+ "'', "
						+ "CONCAT('Pre-Owned Enquiry added For Enquiry ID:'," + enquiry_id + "))";
				// SOP("StrSql--------history--------" + StrSql);
				updateQuery(StrSql);
			}
			// msg += preowned.msg;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob, config_sales_enquiry_refno,"
				+ " config_email_enable, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign, "
				+ " COALESCE(branch_branchtype_id,0) As branch_branchtype_id, "
				+ " COALESCE(branch_email1,'') AS branch_email1, branch_pin,"
				+ " COALESCE(brandconfig_enquiry_email_enable,'') AS brandconfig_enquiry_email_enable,"
				+ " COALESCE(brandconfig_enquiry_email_format,'') AS brandconfig_enquiry_email_format,"
				+ " COALESCE(brandconfig_enquiry_email_sub,'') AS brandconfig_enquiry_email_sub,"
				+ " COALESCE(brandconfig_enquiry_email_exe_enable,'') AS brandconfig_enquiry_email_exe_enable,"
				+ " COALESCE(brandconfig_enquiry_email_exe_format,'') AS brandconfig_enquiry_email_exe_format,"
				+ " COALESCE(brandconfig_enquiry_sms_enable,'') AS brandconfig_enquiry_sms_enable,"
				+ " COALESCE(brandconfig_enquiry_sms_format,'') AS brandconfig_enquiry_sms_format,"
				+ " COALESCE(brandconfig_enquiry_sms_exe_format,'') AS brandconfig_enquiry_sms_exe_format,"
				+ " COALESCE(brandconfig_enquiry_brochure_email_enable,'') AS brandconfig_enquiry_brochure_email_enable, "
				+ " COALESCE(brandconfig_enquiry_brochure_email_format,'') AS brandconfig_enquiry_brochure_email_format, "
				+ " COALESCE(brandconfig_enquiry_brochure_email_sub,'') AS brandconfig_enquiry_brochure_email_sub, "
				+ " COALESCE(emp.emp_email1,'') AS emp_email1, COALESCE(emp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(emp.emp_name,'') AS emp_name, "
				+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1 , COALESCE(emp.emp_mobile2,'') AS emp_mobile2, "
				// +
				// " COALESCE(if(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2),'') AS  emp_mobile, ""SELECT config_sales_soe, config_sales_sob, config_sales_enquiry_refno,"
				+ " config_email_enable, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign,"
				+ " COALESCE(branch_brand_id,0) AS branch_brand_id,"
				+ " COALESCE(branch_email1,'') AS branch_email1, branch_pin,"
				+ " COALESCE(brandconfig_enquiry_email_enable,'') AS brandconfig_enquiry_email_enable,"
				+ " COALESCE(brandconfig_enquiry_email_format,'') AS brandconfig_enquiry_email_format,"
				+ " COALESCE(brandconfig_enquiry_email_sub,'') AS brandconfig_enquiry_email_sub,"
				+ " COALESCE(brandconfig_enquiry_email_exe_enable,'') AS brandconfig_enquiry_email_exe_enable,"
				+ " COALESCE(brandconfig_enquiry_email_exe_format,'') AS brandconfig_enquiry_email_exe_format,"
				+ " COALESCE(brandconfig_enquiry_sms_enable,'') AS brandconfig_enquiry_sms_enable,"
				+ " COALESCE(brandconfig_enquiry_sms_format,'') AS brandconfig_enquiry_sms_format,"
				+ " COALESCE(brandconfig_enquiry_sms_exe_format,'') AS brandconfig_enquiry_sms_exe_format,"
				+ " COALESCE(brandconfig_enquiry_brochure_email_enable,'') AS brandconfig_enquiry_brochure_email_enable, "
				+ " COALESCE(brandconfig_enquiry_brochure_email_format,'') AS brandconfig_enquiry_brochure_email_format, "
				+ " COALESCE(brandconfig_enquiry_brochure_email_sub,'') AS brandconfig_enquiry_brochure_email_sub, "
				+ " COALESCE(emp.emp_email1,'') AS emp_email1, COALESCE(emp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(emp.emp_name,'') AS emp_name, "
				+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1 , COALESCE(emp.emp_mobile2,'') AS emp_mobile2, "
				// +
				// " COALESCE(if(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2),'') AS  emp_mobile, "
				+ " config_customer_dupnames, "
				+ " COALESCE(team_preownedbranch_id, 0) AS team_preownedbranch_id,"
				+ " COALESCE(team_preownedemp_id, 0) AS team_preownedemp_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + enquiry_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id =" + enquiry_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id,"
				+ compdb(comp_id) + "axela_config,"
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1"
				+ " AND branch_id = " + enquiry_branch_id;
		// SOP("StrSql---PopulateConfigDetails-------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				// config_admin_email = crs.getString("config_admin_email");
				branchtype_id = crs.getString("branch_branchtype_id");
				branch_brand_id = crs.getString("branch_brand_id");
				branch_email1 = crs.getString("branch_email1");
				branch_pin = crs.getString("branch_pin");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_enquiry_email_enable = crs.getString("brandconfig_enquiry_email_enable");
				// brandconfig_enquiry_email_enable);
				brandconfig_enquiry_email_format = crs.getString("brandconfig_enquiry_email_format");
				brandconfig_enquiry_email_sub = crs.getString("brandconfig_enquiry_email_sub");
				brandconfig_enquiry_email_exe_enable = crs.getString("brandconfig_enquiry_email_exe_enable");
				brandconfig_enquiry_email_exe_format = crs.getString("brandconfig_enquiry_email_exe_format");
				brandconfig_enquiry_sms_enable = crs.getString("brandconfig_enquiry_sms_enable");
				brandconfig_enquiry_sms_format = crs.getString("brandconfig_enquiry_sms_format");
				brandconfig_enquiry_sms_exe_format = crs.getString("brandconfig_enquiry_sms_exe_format");
				brandconfig_enquiry_brochure_email_enable = crs.getString("brandconfig_enquiry_brochure_email_enable");
				brandconfig_enquiry_brochure_email_format = crs.getString("brandconfig_enquiry_brochure_email_format");
				brandconfig_enquiry_brochure_email_sub = crs.getString("brandconfig_enquiry_brochure_email_sub");
				team_preownedbranch_id = crs.getString("team_preownedbranch_id");
				team_preownedemp_id = crs.getString("team_preownedemp_id");
				emp_name = crs.getString("emp_name");
				// emp_email = crs.getString("emp_email");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateContactCustomerDetails(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, customer_name, customer_emp_id, contact_title_id, "
					+ " CONCAT(title_desc,' ', contact_fname,' ', contact_lname) AS contact_name, "
					+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_phone1, contact_phone2, contact_address, "
					+ " customer_branch_id, CONCAT(branch_name, ' (',branch_code,')') branchname, contact_pin, "
					+ " contactcity.city_id AS contactcity,"
					+ " contactstate.state_id AS contactstate "
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_city contactcity ON contactcity.city_id = contact_city_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_state contactstate ON contactstate.state_id = contactcity.city_state_id "
					+ " WHERE contact_id = " + enquiry_contact_id
					+ " GROUP BY contact_id"
					+ " ORDER BY contact_fname";
			// SOP("StrSql------CustomerDetails------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_name = crs.getString("customer_name");
					customer_info = "<a href=../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + " target=_blank><b>" + customer_name + " ("
							+ crs.getString("customer_id") + ")</b></a>";
					contact_name = crs.getString("contact_name");
					contact_info = "<a href=../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id") + " target=_blank><b>"
							+ contact_name + " (" + crs.getString("contact_id") + ")</b></a>";
					if (!crs.getString("contact_mobile1").equals("")) {
						contact_info = contact_info + "<br>" + crs.getString("contact_mobile1");
						contact_mobile1 = crs.getString("contact_mobile1");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						contact_info = contact_info + "<br>" + crs.getString("contact_mobile2");
						contact_mobile2 = crs.getString("contact_mobile2");
					}
					enquiry_customer_id = crs.getString("customer_id");
					enquiry_contact_id = crs.getString("contact_id");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_phone1 = crs.getString("contact_phone1");
					contact_phone2 = crs.getString("contact_phone2");
				}
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
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
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id IN (1, 2)";
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

	public String PopulateCampaign(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1"
					+ " AND camptrans_branch_id = " + enquiry_branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// SOP("PopulateCampaign-------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), enquiry_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
						.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
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

	public String PopulateCity(String state_id, String city_id,
			String dr_city_id) {
		StringBuilder Str = new StringBuilder();
		try {
			if (state_id.equals("")) {
				state_id = "0";
			}
			StrSql = "SELECT city_id, city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE city_state_id = " + state_id + ""
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=").append(dr_city_id).append(" class=selectbox>");
			Str.append("<select name=").append(dr_city_id).append(" id=")
					.append(dr_city_id).append("  class=selectbox>");
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateState(String state_id, String span_id,
			String dr_state_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT state_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=").append(dr_state_id).append(" id=").append(dr_state_id);
			Str.append(" class=selectbox onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_city_id_contact','");
			Str.append(span_id).append("'); \">");
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String enquiry_model_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1=1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = 1 "
					+ " AND model_sales = 1";
			// if (!CNumeric(enquiry_model_id).equals("0")) {
			// StrSql += " AND model_id = " + enquiry_model_id + "";
			// }
			StrSql += " ORDER BY model_name";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1"
					+ " AND item_type_id = 1 ";
			// if (!update.equals("yes")) {
			StrSql = StrSql + " and item_active = '1'";
			// }
			StrSql = StrSql + " and item_model_id = " + enquiry_model_id
					+ " order by item_name";
			// SOP(" PopulateItem-----addd------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), enquiry_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBuyerType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT buyertype_id, buyertype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " WHERE 1=1"
					+ " ORDER BY buyertype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("buyertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("buyertype_id"), enquiry_buyertype_id));
				Str.append(">").append(crs.getString("buyertype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirycat_id, enquirycat_name"
					+ " FROM axela_sales_enquiry_cat"
					+ " GROUP BY enquirycat_id"
					+ " ORDER BY enquirycat_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirycat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirycat_id"), enquiry_enquirycat_id));
				Str.append(">").append(crs.getString("enquirycat_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSalesExecutives(String branch_id, String team_id, String enquiry_emp_id, String active, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales = '1'"
					+ " AND emp_branch_id = " + branch_id;
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id IN (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id =" + team_id + ")";
			}
			if (active.equals("1")) {
				StrSql = StrSql + " AND emp_active = '1' ";
			}
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow())
					+ " AND leave_active = 1 )";

			// Only for super admin
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id,"
						+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id = 1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";

			// SOP("StrSql=12===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_emp_id\" id=\"dr_enquiry_emp_id\" onchange=\"PopulateSOE();\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), enquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateTeam(String branch_id, String enquiry_team_id, String comp_id) {
		// SOP("team_id---" + enquiry_team_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + branch_id
					+ " AND team_active = 1 "
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			// SOP("StrSql------PopulateTeam-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_team\" id=\"dr_enquiry_team\" class=\"dropdown form-control\" onchange=\"PopulateExecutive();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), enquiry_team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + enquiry_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_sob_id\" id=\"dr_enquiry_sob_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), enquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			Str.append("<select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe(String comp_id, String emp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1 = 1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id = " + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			// SOP("SOE" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_soe_id\" id=\"dr_enquiry_soe_id\" onchange=\"populateSob();\"  class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), enquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirytype_id, enquirytype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_type"
					+ " GROUP BY enquirytype_id" + " ORDER BY enquirytype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirytype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirytype_id"), enquiry_enquirytype_id));
				Str.append(">").append(crs.getString("enquirytype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulatePreownedModel(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT preownedmodel_id, preownedmodel_name"
	// + " FROM axela_preowned_model"
	// + " WHERE 1=1"
	// + " ORDER BY preownedmodel_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), enquiry_preownedmodel_id));
	// Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	public String PopulatePreownedVariant(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1=1";
			// if (!preowned_preownedmodel_id.equals("0")) {
			// StrSql = StrSql + " and variant_preownedmodel_id = "
			// + enquiry_preownedmodel_id;
			// }
			StrSql = StrSql + " ORDER BY variant_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(StrSelectdrop(crs.getString("variant_id"), enquiry_preownedvariant_id));
				Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), enquiry_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrefReg(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prefreg_id, prefreg_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg"
					+ " WHERE 1 = 1"
					+ " ORDER BY prefreg_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prefreg_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prefreg_id"), enquiry_prefreg_id));
				Str.append(">").append(crs.getString("prefreg_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFinance(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", enquiry_finance)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_finance)).append(">No</option>\n");
		return Str.toString();
	}
}
