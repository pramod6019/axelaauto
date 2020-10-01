package axela.preowned;

// sangita

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
import cloudify.connect.Connect;

public class Preowned_Quickadd extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_preowned_edit = "";
	public String lead_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_jobtitle = "";
	public String preowned_id = "0";
	public String preowned_no = "";
	public String preowned_enquiry_id = "0";
	public String preowned_preownedstatus_date = "";
	public String preowned_preownedstatus_desc = "";
	public String preowned_prioritypreowned_id = "0";
	public String preowned_branch_id = "0";
	public String preowned_customer_id = "0";
	public String preowned_contact_id = "0";
	public String preowned_title = "New Pre-Owned";
	public String preowned_desc = "";
	public String preowned_sub_variant = "";
	public String preowned_extcolour_id = "0";
	public String preowned_intcolour_id = "0";
	public String preowned_options = "";
	public String preowned_date = "";
	public String preowned_close_date = "", close_date = "";
	public String preowned_emp_id = "0";
	public String preowned_sales_emp_id = "0";
	public String preowned_campaign_id = "0";
	public String preowned_soe_id = "0";
	public String preowned_sob_id = "0";
	public String preowned_refno = "";
	public String preowned_notes = "";
	public String preowned_preownedstatus_id = "0";
	public String preownedstatus_name = "";
	public String preowned_entry_id = "0", preowned_entry_date = "";
	public String entry_by = "", entry_date = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String city_id = "0";
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
	public String contact_address = "";
	public String contact_city_id = "";
	public String contact_pin = "";
	public String state_id = "0";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String preownedteam_id = "0";
	public String brandconfig_preowned_email_enable = "0";
	public String brandconfig_preowned_email_exe_enable = "0";
	public String brandconfig_preowned_email_sub = "";
	public String brandconfig_preowned_email_format = "";
	public String brandconfig_preowned_email_exe_sub = "";
	public String brandconfig_preowned_email_exe_format = "";
	public String brandconfig_preowned_sms_enable = "0";
	public String brandconfig_preowned_sms_exe_enable = "0";
	public String brandconfig_preowned_sms_exe_format = "";
	public String brandconfig_preowned_sms_format = "";
	public String branch_email1 = "";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_customer_dupnames = "";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String config_preowned_refno = "";
	public String config_preowned_campaign = "";
	public String config_preowned_soe = "";
	public String emp_role_id = "0";
	public String config_preowned_sob = "";
	// public String preowned_preownedmodel_id = "0";
	public String preownedmodel_name = "";
	public String preowned_variant_id = "0";
	public String preowned_fcamt = "";
	public String preowned_noc = "";
	public String preowned_funding_bank = "";
	public String preowned_loan_no = "";
	public String preowned_insur_date = "";
	public String preowned_insurance_id = "0";
	public String preowned_ownership_id = "0";
	public String preowned_regdyear = "", preowned_manufyear = "";
	public String preowned_invoicevalue = "", preowned_kms = "";
	public String preowned_regno = "";
	public String preowned_expectedprice = "";
	public String preowned_quotedprice = "";
	public String preowned_fueltype_id = "0";
	// public String preowned_team_id = "0";
	public String ExeAccess = "";
	public String send_contact_email = "";
	public String addcontinue = "";
	// public String enquiry_id = "0";
	public String precrmfollowup_crm_emp_id = "0";
	public String precrmfollowupdays_daycount = "";
	public String precrmfollowup_followup_time = "";
	public String preowned_soe_crm_enable = "";
	public String preowned_soe_active = "";
	// inbound package variables
	public String inbound_check = "";
	public String preowned_call_id = "0";

	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				// CheckPerm(comp_id, "emp_preowned_access", request, response);
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				preowned_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				preowned_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				// preowned_preownedmodel_id =
				// CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedmodel_id")));
				preowned_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_emp_id")));
				emp_preowned_edit = ReturnPerm(comp_id, "emp_preowned_edit", request);
				addcontinue = PadQuotes(request.getParameter("chk_add_continue"));
				inbound_check = PadQuotes(request.getParameter("inbound"));
				preowned_call_id = CNumeric(PadQuotes(request.getParameter("preowned_call_id")));
				// enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				preownedstatus_name = ExecuteQuery("SELECT preownedstatus_name"
						+ " FROM " + compdb(comp_id) + "axela_preowned_status WHERE preownedstatus_id = 1");

				if (preowned_branch_id.equals("0")) {
					preowned_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (preowned_branch_id.equals("0")) {
						preowned_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 AND branch_branchtype_id = 2 LIMIT 1");
					}
				}
				if (!preowned_branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + preowned_branch_id);
				}
				PopulateConfigDetails();
				if (!preowned_contact_id.equals("0")) {
					PopulateContactCustomerDetails();
				}
				if (!addB.equals("yes")) {
					preowned_emp_id = emp_id;
					// contact_mobile1 = "91-";
					preowned_date = strToShortDate(ToShortDate(kknow()));
					preowned_close_date = strToShortDate(ToShortDate(kknow()));
					StrSql = "SELECT preownedteamtrans_team_id"
							+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
							+ " WHERE preownedteamtrans_emp_id = " + emp_id;
					// SOP("StrSql===" + StrSql);
					preownedteam_id = CNumeric(ExecuteQuery(StrSql));
					// SOP("preownedteam_id===" + preownedteam_id);
					// if (!preowned_id.equals("0")) {
					// StrSql = "SELECT enquiry_soe_id, enquiry_sob_id, enquiry_campaign_id"
					// + " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					// + " WHERE enquiry_id = " + enquiry_id;
					// CachedRowSet crs = processQuery(StrSql, 0);
					//
					// while (crs.next()) {
					// preowned_soe_id = crs.getString("enquiry_soe_id");
					// preowned_sob_id = crs.getString("enquiry_sob_id");
					// preowned_campaign_id = crs.getString("enquiry_campaign_id");
					// }
					// crs.close();
					// }
					if (!preowned_branch_id.equals("0")) {
						StrSql = "SELECT city_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " WHERE branch_id = " + preowned_branch_id + "";
						contact_city_id = ExecuteQuery(StrSql);
					}
				} else {
					GetValues(request, response);
					// if (ReturnPerm(comp_id, "emp_preowned_add",
					// request).equals("1")) {
					preowned_entry_date = ToLongDate(kknow());
					AddPreownedFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (addcontinue.equals("1")) {
							msg = "Pre Owned added successfully!";
							preowned_customer_id = "0";
							preowned_contact_id = "0";
							customer_name = "";
							contact_fname = "";
							contact_lname = "";
							contact_jobtitle = "";
							contact_mobile1 = "";
							contact_email1 = "";
							contact_mobile2 = "";
							contact_email2 = "";
							contact_phone1 = "";
							contact_address = "";
							contact_pin = "";
							preowned_desc = "";
							preowned_refno = "";
							preowned_notes = "";
							preowned_sub_variant = "";
							preowned_options = "";
						} else {
							if (ReturnPerm(comp_id, "emp_preowned_access", request).equals("0")) {
								response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Pre-Owned added successfully!"));
							}
							else {
								if (inbound_check.equals("yes")) {
									msg = "Pre-Owned added successfully!";
								} else {
									response.sendRedirect(response.encodeRedirectURL("preowned-list.jsp?preowned_id=" + preowned_id + "&msg=Pre-Owned added successfully!"));
								}
							}

						}

					}
					// } else {
					// response.sendRedirect(AccessDenied());
					// }
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (preowned_contact_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_jobtitle = PadQuotes(request.getParameter("txt_contact_jobtitle"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			SOP("contact_mobile1==" + contact_mobile1);
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
			contact_email2 = PadQuotes(request.getParameter("txt_contact_email2"));
			contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
			contact_address = PadQuotes(request.getParameter("txt_contact_address"));
			state_id = CNumeric(PadQuotes(request.getParameter("dr_state_id")));
			contact_city_id = CNumeric(PadQuotes(request.getParameter("maincity")));
			contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		}
		if (emp_preowned_edit.equals("1")) {
			preowned_date = PadQuotes(request.getParameter("txt_preowned_date"));
		} else {
			preowned_date = strToShortDate(ToShortDate(kknow()));
		}
		preowned_close_date = PadQuotes(request.getParameter("txt_preowned_close_date"));
		close_date = preowned_close_date;
		preowned_desc = PadQuotes(request.getParameter("txt_preowned_desc"));
		preowned_sub_variant = PadQuotes(request.getParameter("txt_preowned_sub_variant"));
		preowned_extcolour_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_extcolour_id")));
		preowned_intcolour_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_intcolour_id")));
		preowned_options = PadQuotes(request.getParameter("txt_preowned_options"));
		preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		preowned_fcamt = CNumeric(PadQuotes(request.getParameter("txt_preowned_fcamt")));
		preowned_noc = CNumeric(PadQuotes(request.getParameter("dr_preowned_noc")));
		preowned_funding_bank = (PadQuotes(request.getParameter("txt_preowned_funding_bank")));
		preowned_loan_no = (PadQuotes(request.getParameter("txt_preowned_loan_no")));
		preowned_insur_date = (PadQuotes(request.getParameter("txt_preowned_insur_date")));
		preowned_insurance_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_insurance_id")));
		preowned_ownership_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_ownership_id")));
		preowned_regdyear = (PadQuotes(request.getParameter("txt_preowned_regdyear")));
		preowned_manufyear = (PadQuotes(request.getParameter("txt_preowned_manufyear")));
		preowned_invoicevalue = CNumeric(PadQuotes(request.getParameter("txt_preowned_invoicevalue")));
		preowned_kms = CNumeric(PadQuotes(request.getParameter("txt_preowned_kms")));
		preowned_regno = (PadQuotes(request.getParameter("txt_preowned_regno")));
		preowned_expectedprice = CNumeric(PadQuotes(request.getParameter("txt_preowned_expectedprice")));
		preowned_quotedprice = CNumeric(PadQuotes(request.getParameter("txt_preowned_quotedprice")));
		preowned_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_fueltype_id")));
		preowned_preownedstatus_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedstatus_id")));
		preowned_soe_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_soe_id")));
		preowned_sob_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sob_id")));
		preowned_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_campaign_id")));
		preowned_sales_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sales_emp_id")));
		preownedteam_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_team")));
		preowned_refno = PadQuotes(request.getParameter("txt_preowned_refno"));
		preowned_notes = PadQuotes(request.getParameter("txt_preowned_notes"));
		if (addcontinue.equals("on")) {
			addcontinue = "1";
		} else {
			addcontinue = "0";
		}
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		String customername = "";
		if (customer_name.equals("")) {
			customername = (contact_fname + " " + contact_lname).trim();
			customer_name = toTitleCase(customername);
		} else {
			customername = toTitleCase(customer_name);
			customer_name = customername;
		}
		if (preowned_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		if (preowned_contact_id.equals("0")) {
			if (contact_title_id.equals("0")) {
				msg += "<br>Select Contact Title!";
			}
			if (contact_fname.equals("")) {
				msg += "<br>Enter the Contact First Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}

			// if (contact_lname.equals("")) {
			// msg += "<br>Enter the Contact Last Name!";
			// } else {
			// contact_lname = toTitleCase(contact_lname);
			// }
			if (contact_mobile1.equals("91-")) {
				contact_mobile1 = "";
			}
			if (contact_phone1.equals("91-")) {
				contact_phone1 = "";
			}
			if (contact_mobile1.equals("") && contact_phone1.equals("")) {
				msg += "<br>Enter Either Contact Mobile 1 or Phone!";
			} else if (!contact_mobile1.equals("")) {
				if (!IsValidMobileNo11(contact_mobile1)) {
					msg += "<br>Enter Valid Contact Mobile 1!";
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
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
							+ " WHERE preowned_preownedstatus_id = 1 "
							+ " AND (contact_mobile1 = '" + contact_mobile1
							+ "' OR contact_mobile2 = '" + contact_mobile1 + "')";
					// + " and preowned_branch_id = " + preowned_branch_id;
					// SOP("StrSql==11111===" + StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Mobile 1 Found!";
					}
				}
			}

			if (!contact_mobile2.equals("")) {
				if (!IsValidMobileNo11(contact_mobile2)) {
					msg += "<br>Enter Valid Contact Mobile 2!";
				} else {

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
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
							+ " WHERE preowned_preownedstatus_id = 1"
							+ " AND (contact_mobile1 = '" + contact_mobile2
							+ "' OR contact_mobile2 = '" + contact_mobile2 + "')"
							+ " AND preowned_branch_id = " + preowned_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Mobile 2 Found!";
					}
				}
			}
			if (!contact_email1.equals("")) {
				if (!IsValidEmail(contact_email1)) {
					msg += "<br>Enter Valid Contact Email 1!";
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
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
							+ " WHERE preowned_preownedstatus_id = 1"
							+ " AND contact_email1 = '" + contact_email1 + "'"
							+ " AND preowned_branch_id = " + preowned_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Email 1 Found!!";
					}
					contact_email1 = contact_email1.toLowerCase();
				}
			}
			if (!contact_email2.equals("")) {
				if (!IsValidEmail(contact_email2)) {
					msg += "<br>Enter valid Contact Email 2!";
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

					StrSql = "SELECT contact_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
							+ " WHERE preowned_preownedstatus_id = 1"
							+ " AND contact_email2 = '" + contact_email2 + "'"
							+ " AND preowned_branch_id = " + preowned_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Email 2 Found!!";
					}
					contact_email2 = contact_email2.toLowerCase();
				}
			}
			if (!contact_phone1.equals("")) {
				if (!IsValidPhoneNo11(contact_phone1)) {
					msg += "<br>Enter Valid Contact Phone!";
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
					StrSql = "SELECT contact_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
							+ " WHERE preowned_preownedstatus_id = 1"
							+ " AND (contact_phone1 = '" + contact_phone1
							+ "' OR contact_phone2 = '" + contact_phone1 + "')"
							+ " AND preowned_branch_id = " + preowned_branch_id;
					// SOP("StrSql--"+StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Phone Found!";
					}
				}
			}
			if (contact_city_id.equals("")) {
				msg += "<br>Select Contact City!";
			}
			// if (contact_state_id.equals("0")) {
			// msg += "<br>Select Contact State!";
			// }
			if (contact_pin.equals("")) {
				contact_pin = ExecuteQuery("SELECT branch_pin"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_id = " + preowned_branch_id);
			} else if (!isNumeric(contact_pin)) {
				msg += "<br>Contact Pin: Enter Numeric!";
			}
		}

		if (emp_preowned_edit.equals("1")) {
			if (preowned_date.equals("")) {
				msg += "<br>Enter Date!";
			} else {
				if (!isValidDateFormatShort(preowned_date)) {
					msg += "<br>Enter Valid Date!";
				}
				if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(preowned_date))) {
					msg += " <br>Date can't be greater than Current Date!";
				}
			}
		}

		if (preowned_close_date.equals("")) {
			msg += "<br>Enter Closed Date!";
		} else {
			if (isValidDateFormatShort(preowned_close_date)) {
				close_date = ConvertShortDateToStr(preowned_close_date);
				if (Long.parseLong(ConvertShortDateToStr(preowned_close_date)) < Long.parseLong(ToShortDate(kknow()))) {
					msg += " <br>Close Date cannot be less than Current Date!";
				}
			} else {
				msg += "<br>Enter valid Closed Date!";
			}
		}

		// if (!preowned_preownedmodel_id.equals("0")) {
		// preownedmodel_name =
		// ExecuteQuery("SELECT preownedmodel_name FROM axela_preowned_model WHERE preownedmodel_id = "
		// + preowned_preownedmodel_id);
		// preowned_title = "Pre Owned " + preownedmodel_name;
		// }
		// if (preowned_preownedmodel_id.equals("0")) {
		// msg += "<br>Select Model!";
		// }
		if (preowned_variant_id.equals("0")) {
			msg += "<br>Select Model!";
		}
		// if (preowned_fueltype_id.equals("0")) {
		// msg += "<br>Select Fuel Type!";
		// }
		// if (preowned_extcolour_id.equals("0")) {
		// msg += "<br>Select Exterior!";
		// }
		// if (preowned_intcolour_id.equals("0")) {
		// msg += "<br>Select Interior!";
		// }
		// if (preowned_manufyear.equals("")) {
		// msg += "<br>Enter Manufacture Year!";
		// }
		// if (preowned_ownership_id.equals("0")) {
		// msg += "<br>Select Ownership!";
		// }
		// if (preowned_regno.equals("")) {
		// msg += "<br>Enter Registration No.!";
		// } else {
		// preowned_regno = preowned_regno.replaceAll(" ", "");
		// preowned_regno = preowned_regno.replaceAll("-", "");
		// if (!IsValidUsername(preowned_regno).equals("")) {
		// msg += "<br>Enter valid Registration No.!";
		// }
		// StrSql = "SELECT preowned_regno FROM " + compdb(comp_id) +
		// "axela_preowned WHERE preowned_regno = '" + preowned_regno + "'";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Similar Registration No. found!";
		// }

		// }

		// if (preowned_insur_date.equals("")) {
		// msg += "<br>Enter Insurance Date!";
		// } else {
		// if (!isValidDateFormatShort(preowned_insur_date)) {
		// msg += "<br>Enter valid Insurance Date!";
		// }
		// }

		if (Integer.parseInt(preowned_fcamt) > 0) {
			if (preowned_funding_bank.equals("")) {
				msg += "<br>Enter Funding Bank!";
			}
			if (preowned_loan_no.equals("")) {
				msg += "<br>Enter Loan No.!";
			}
		} else if (!preowned_funding_bank.equals("")) {
			if (preowned_fcamt.equals("")
					|| Integer.parseInt(preowned_fcamt) <= 0) {
				msg += "<br>Enter Foreclosure Amt!";
			}
			if (preowned_loan_no.equals("")) {
				msg += "<br>Enter Loan No.!";
			}
		} else if (!preowned_loan_no.equals("")) {
			if (preowned_fcamt.equals("") || Integer.parseInt(preowned_fcamt) <= 0) {
				msg += "<br>Enter Foreclosure Amt!";
			}
			if (preowned_funding_bank.equals("")) {
				msg += "<br>Enter Funding Bank!";
			}
		}
		if (preownedteam_id.equals("0")) {
			msg += "<br>Select Team!";
		}
		if (preowned_emp_id.equals("0")) {
			msg += "<br>Select Pre-Owned Consultant!";
		}
		// if (preowned_preownedstatus_id.equals("0")) {
		// msg = msg + "<br>Select Status!";
		// }
		if (config_preowned_soe.equals("1")) {
			if (preowned_soe_id.equals("0")) {
				msg += "<br>Select Source of Enquiry!";
			}
		}
		if (config_preowned_sob.equals("1")) {
			if (preowned_sob_id.equals("0")) {
				msg += "<br>Select Source of Bussiness!";
			}
		}
		if (config_preowned_campaign.equals("1")) {
			if (preowned_campaign_id.equals("0")) {
				msg += "<br>Select Campaign!";
			}
		}
		// if (config_preowned_refno.equals("1")) {
		// if (preowned_refno.equals("")) {
		// msg += "<br>Enter Pre Owned Reference No.!";
		// } else {
		// if (preowned_refno.length() < 2) {
		// msg += "<br>Pre Owned Reference No. Should be Atleast Two Digits!";
		// }
		// if (!preowned_branch_id.equals("0")) {
		// StrSql = "SELECT preowned_refno"
		// + " FROM " + compdb(comp_id) + "axela_preowned"
		// + " WHERE preowned_branch_id = " + preowned_branch_id + ""
		// + " and preowned_refno = '" + preowned_refno + "'";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Similar Pre Owned Reference No. found!";
		// }
		// }
		// }
		// }
		// SOP("preowned_branch_id===" + preowned_branch_id);
		// SOP("preowned_emp_id===" + preowned_emp_id);
		if (!preowned_branch_id.equals("0")) {
			if (!preowned_emp_id.equals("0")) {
				StrSql = "SELECT COALESCE((SELECT preownedteam_crm_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_preowned_team"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_team_id = preownedteam_id"
						+ " WHERE preownedteam_branch_id = " + preowned_branch_id + ""
						+ " AND preownedteamtrans_emp_id = " + preowned_emp_id
						+ " AND  preownedteam_crm_emp_id != 0"
						+ " LIMIT 1) ,0)";
				// SOP("StrSql--------------" + StrSql);
				precrmfollowup_crm_emp_id = ExecuteQuery(StrSql);
				// precrmfollowup_crm_emp_id =
				// SOP("precrmfollowup_crm_emp_id--------" + precrmfollowup_crm_emp_id);
			}
		}
	}
	public void AddPreownedFields() throws SQLException {
		CheckForm();
		// preowned_preownedmodel_id =
		// ExecuteQuery("SELECT variant_preownedmodel_id FROM axela_preowned_variant"
		// + " WHERE variant_id = " + preowned_variant_id);
		// SOP("msg =------------------------ " + msg);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				// SOPInfo("preowned_customer_id=======" + preowned_customer_id);
				// SOPInfo("config_customer_dupnames=======" + config_customer_dupnames);
				// SOPInfo("preowned_contact_id=======" + preowned_contact_id);
				// SOPInfo("======" + (preowned_customer_id.equals("0") || (!preowned_customer_id.equals("0") && config_customer_dupnames.equals("1"))));
				if (preowned_customer_id.equals("0") || (!preowned_customer_id.equals("0") && config_customer_dupnames.equals("1"))) {
					AddCustomerFields();
				}
				if (preowned_contact_id.equals("0")) {
					AddContactFields();
				}

				if (!preowned_customer_id.equals("0") && !preowned_contact_id.equals("0")) {

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned"
							+ " (preowned_no,"
							+ " preowned_branch_id,"
							+ " preowned_customer_id,"
							+ " preowned_contact_id,"
							+ " preowned_title,"
							+ " preowned_sub_variant,"
							+ " preowned_extcolour_id,"
							+ " preowned_intcolour_id,"
							+ " preowned_options,"
							+ " preowned_date,"
							// + " preowned_preownedmodel_id,"
							+ " preowned_variant_id,"
							+ " preowned_fcamt,"
							+ " preowned_noc,"
							+ " preowned_funding_bank,"
							+ " preowned_loan_no,"
							+ " preowned_insur_date,"
							+ " preowned_insurance_id,"
							+ " preowned_ownership_id,"
							+ " preowned_regdyear,"
							+ " preowned_manufyear,"
							+ " preowned_invoicevalue,"
							+ " preowned_kms,"
							+ " preowned_regno,"
							+ " preowned_expectedprice,"
							+ " preowned_quotedprice,"
							+ " preowned_fueltype_id,"
							+ " preowned_close_date,"
							+ " preowned_team_id,"
							+ " preowned_emp_id,"
							+ " preowned_sales_emp_id,"
							+ " preowned_enquiry_id, ";
					// + " preowned_preownedstage_id, ";

					if (config_preowned_soe.equals("1")) {
						StrSql += " preowned_soe_id,";
					}
					if (config_preowned_sob.equals("1")) {
						StrSql += " preowned_sob_id,";
					}
					if (config_preowned_campaign.equals("1")) {
						StrSql += " preowned_campaign_id, ";
					}
					if (inbound_check.equals("yes") && !preowned_call_id.equals("0")) {
						StrSql += " preowned_call_id, ";
					}

					StrSql += " preowned_preownedstatus_id,"
							+ " preowned_preownedstatus_date,"
							+ " preowned_preownedstatus_desc,"
							+ " preowned_prioritypreowned_id,"
							+ " preowned_notes," + " preowned_desc,";

					if (config_preowned_refno.equals("1")) {
						StrSql += "preowned_refno,";
					}
					StrSql += " preowned_entry_id,"
							+ " preowned_entry_date,"
							+ " preowned_modified_id,"
							+ " preowned_modified_date)"
							+ " values "
							+ "("
							+ " (SELECT COALESCE(MAX(preowned.preowned_no), 0) + 1 "
							+ " FROM " + compdb(comp_id) + "axela_preowned AS preowned "
							+ " WHERE preowned.preowned_branch_id  = " + preowned_branch_id + "),"
							+ " " + preowned_branch_id + ","
							+ " " + preowned_customer_id + ","
							+ " " + preowned_contact_id + ","
							+ " '" + preowned_title + "',"
							+ " '" + preowned_sub_variant + "',"
							+ " " + preowned_extcolour_id + ","
							+ " " + preowned_intcolour_id + ","
							+ " '" + preowned_options + "',"
							+ " '" + ConvertShortDateToStr(preowned_date) + "',"
							// + " " + preowned_preownedmodel_id + ","
							+ " " + preowned_variant_id + ","
							+ " '" + preowned_fcamt + "',"
							+ " '" + preowned_noc + "',"
							+ " '" + preowned_funding_bank + "',"
							+ " '" + preowned_loan_no + "',"
							+ " '" + ConvertShortDateToStr(preowned_insur_date) + "',"
							+ " " + preowned_insurance_id + ","
							+ " " + preowned_ownership_id + ","
							+ " '" + preowned_regdyear + "',"
							+ " '" + preowned_manufyear + "',"
							+ " " + preowned_invoicevalue + ","
							+ " " + preowned_kms + ","
							+ " '" + preowned_regno.replaceAll(" ", "") + "',"
							+ " " + preowned_expectedprice + ","
							+ " " + preowned_quotedprice + ","
							+ " " + preowned_fueltype_id + ","
							+ " '" + close_date + "',"
							+ " " + preownedteam_id + ","
							+ " " + preowned_emp_id + ","
							+ " " + preowned_sales_emp_id + " ,"
							+ " " + preowned_enquiry_id + " ,";
					// + " " + 2 + ",";
					if (config_preowned_soe.equals("1")) {
						StrSql += " " + preowned_soe_id + ",";
					}
					if (config_preowned_sob.equals("1")) {
						StrSql += " " + preowned_sob_id + ",";
					}
					if (config_preowned_campaign.equals("1")) {
						StrSql += " " + preowned_campaign_id + ",";
					}
					if (inbound_check.equals("yes") && !preowned_call_id.equals("0")) {
						StrSql += " " + preowned_call_id + ",";
					}
					StrSql += " 1," + " ''," + " ''," + " 1," + " '"
							+ preowned_notes + "'," + " '" + preowned_desc
							+ "',";
					if (config_preowned_refno.equals("1")) {
						StrSql = StrSql + "'" + preowned_refno + "',";
					}
					StrSql = StrSql + " " + emp_id + "," + " '"
							+ ToLongDate(kknow()) + "'," + " 0," + " '')";
					SOP("StrSql-preowned-quicka add----" + StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						preowned_id = rs.getString(1);
					}
					rs.close();
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES"
							+ " (" + preowned_id + ","
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 'NEW_PRE-OWNED'," + " '',"
							+ " 'New Pre Owned added')";
					stmttx.execute(StrSql);
					if (!preowned_id.equals("0")) {
						// SOP("contact_email1-------------" + contact_email1);
						// SOP("contact_email2-------------" + contact_email2);

						if (!contact_email2.equals("") && !contact_email1.equals("")) {
							send_contact_email = contact_email1 + "," + contact_email2;
						} else if (!contact_email1.equals("")) {
							send_contact_email = contact_email1;
						}
						// SOP("comp_email_enable-------------" + comp_email_enable);
						// SOP("emp_email_formail-------------" + emp_email_formail);
						// SOP("brandconfig_preowned_email_exe_enable-------------" + brandconfig_preowned_email_exe_enable);
						// SOP("branch_email1-------------" + branch_email1);
						// SOP("brandconfig_preowned_email_exe_format-------------" + brandconfig_preowned_email_exe_format);
						// SOP("brandconfig_preowned_email_exe_sub-------------" + brandconfig_preowned_email_exe_sub);

						if (comp_email_enable.equals("1") && config_email_enable.equals("1") && !branch_email1.equals("")) {
							if (!send_contact_email.equals("") && brandconfig_preowned_email_enable.equals("1") && !brandconfig_preowned_email_format.equals("")
									&& !brandconfig_preowned_email_sub.equals("")) {
								SendEmail();
							}
							if (brandconfig_preowned_email_exe_enable.equals("1") && !brandconfig_preowned_email_exe_format.equals("") && !brandconfig_preowned_email_exe_sub.equals("")
									&& !branch_email1.equals("") && !emp_email_formail.equals("")) {
								SendEmailToExecutive();
							}
						}

						// SOP("comp_sms_enable-------------" +
						// comp_sms_enable);
						// SOP("config_sms_enable-------------" +
						// config_sms_enable);
						// SOP("branch_preowned_sms_enable-------------" +
						// branch_preowned_sms_enable);
						// SOP("contact_mobile1-------------" +
						// contact_mobile1);
						// SOP("contact_mobile2-------------" +
						// contact_mobile2);
						// SOP("branch_preowned_sms_exe_format-------------" +
						// branch_preowned_sms_exe_format);

						if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
							if (brandconfig_preowned_sms_enable.equals("1") && !brandconfig_preowned_sms_format.equals("")) {
								if (!contact_mobile1.equals("")) {
									SendSMS(contact_mobile1);
								}
								if (!contact_mobile2.equals("")) {
									SendSMS(contact_mobile2);
								}
							}
							if (brandconfig_preowned_sms_exe_enable.equals("1") && !brandconfig_preowned_sms_exe_format.equals("")) {
								if (!emp_mobile1.equals("")) {
									SendSMSToExecutive(emp_mobile1);
								}
								if (!emp_mobile2.equals("")) {
									SendSMSToExecutive(emp_mobile2);
								}
							}
						}
						if (!preowned_emp_id.equals("0")) {
							AddFollowupFields();
						}
						// SOP("precrmfollowup_crm_emp_id=====" + precrmfollowup_crm_emp_id);
						conntx.commit();
						StrSql = "SELECT soe_crm_enable, soe_active"
								+ " FROM " + compdb(comp_id) + "axela_soe "
								+ " WHERE soe_id = " + preowned_soe_id;
						// SOP("StrSql=====crmcustom====" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							preowned_soe_crm_enable = crs.getString("soe_crm_enable");
							preowned_soe_active = crs.getString("soe_active");
						}
						crs.close();
						if (!precrmfollowup_crm_emp_id.equals("0") && preowned_soe_crm_enable.equals("1") && preowned_soe_active.equals("1")) {
							preowned_date = ConvertShortDateToStr(preowned_date);
							AddPreownedCRMFollowupFields(preowned_id, preowned_date, "new", "1", comp_id);
						}
					}
				}

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_emp_id,"
					+ " customer_address,"
					+ " customer_pin,"
					+ " customer_email1,"
					+ " customer_email2,"
					+ " customer_phone1,"
					+ " customer_since,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " VALUES"
					+ " ('" + preowned_branch_id + "',"
					+ " '" + customer_name + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_mobile2 + "',"
					+ " '" + contact_city_id + "',"
					+ " '" + preowned_soe_id + "',"
					+ " '" + preowned_sob_id + "',"
					+ " " + preowned_emp_id + ","
					+ " '" + contact_address + "',"
					+ " '" + contact_pin + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_email2 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + ToShortDate(kknow()) + "',"
					+ " 32,"// customer_accgroup_id
					+ " 1,"// customer_type
					+ " '1',"// active
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOPInfo("customer====StrSql===" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				preowned_customer_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date,"
					+ " contact_modified_id,"
					+ " contact_modified_date)"
					+ " VALUES"
					+ " (" + preowned_customer_id + ","
					+ " 1,"
					+ " " + contact_title_id + ","
					+ " '" + contact_fname + "',"
					+ " '" + contact_lname + "',"
					+ " '" + contact_jobtitle + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_mobile2 + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_email2 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + contact_address + "',"
					+ " '" + contact_city_id + "',"
					+ " '" + contact_pin + "',"
					+ " '1',"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '')";
			SOP("contact====StrSql===" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				preowned_contact_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFollowupFields() throws SQLException {
		try {
			StrSql = " INSERT INTO " + compdb(comp_id)
					+ "axela_preowned_followup"
					+ " (preownedfollowup_preowned_id,"
					+ " preownedfollowup_emp_id,"
					+ " preownedfollowup_followup_time,"
					+ " preownedfollowup_preownedfollowuptype_id,"
					+ " preownedfollowup_desc," + " preownedfollowup_entry_id,"
					+ " preownedfollowup_entry_time,"
					+ " preownedfollowup_trigger)" + " VALUES" + " ('"
					+ preowned_id + "'," + " " + preowned_emp_id + "," + " '"
					+ ToLongDate(kknow()) + "'," + " 1," + " ''," + " "
					+ emp_id + "," + " '" + ToLongDate(kknow()) + "'," + " 0)";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void AddPreownedCRMFollowupFields(String preowned_id, String date, String crmtype, String crmdaystype, String comp_id) throws SQLException {

		// SOP("date====" + date);
		// SOP("date convert======" + ConvertShortDateToStr(date));
		try {
			StrSql = " Select "
					+ preowned_id + ", "
					+ "COALESCE(("
					+ " SELECT"
					+ " CASE WHEN precrmfollowupdays_exe_type = 1 THEN preownedteam_crm_emp_id"
					+ " WHEN precrmfollowupdays_exe_type = 2 THEN preowned_emp_id"
					+ " WHEN precrmfollowupdays_exe_type = 3 THEN preownedteam_emp_id"
					+ " END"
					+ " FROM " + compdb(comp_id) + "axela_preowned_team"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_team_id = preownedteam_id"
					+ " WHERE preownedteam_branch_id = preowned_branch_id"
					+ " AND preownedteamtrans_emp_id = preowned_emp_id"
					+ " LIMIT 1), preowned_emp_id) AS preownedcrmempid, "
					+ " precrmfollowupdays_id, "
					+ " DATE_FORMAT(DATE_ADD(" + date + ","
					+ " INTERVAL (precrmfollowupdays_daycount-1) DAY), '%Y%m%d%H%i%s')"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_brand_id = branch_brand_id"
					+ " WHERE preowned_id = " + preowned_id;
			if (crmtype.equals("new")) {
				StrSql += " AND precrmfollowupdays_lostfollowup=0 AND precrmfollowupdays_testdrivefollowup=0 AND precrmfollowupdays_homevisitfollowup=0";
			} else if (crmtype.equals("lost")) {
				StrSql += " AND precrmfollowupdays_lostfollowup=1 AND precrmfollowupdays_testdrivefollowup=0 AND precrmfollowupdays_homevisitfollowup=0";
			} else if (crmtype.equals("testdrive")) {
				StrSql += " AND precrmfollowupdays_lostfollowup=0 AND precrmfollowupdays_testdrivefollowup=1 AND precrmfollowupdays_homevisitfollowup=0";
			} else if (crmtype.equals("homevisit")) {
				StrSql += " AND precrmfollowupdays_lostfollowup=0 AND precrmfollowupdays_testdrivefollowup=0 AND precrmfollowupdays_homevisitfollowup=1";
			}
			StrSql += " AND precrmfollowupdays_active = 1 " + " AND precrmfollowupdays_precrmtype_id = " + crmdaystype;
			StrSql += " AND concat(preowned_id, '-', precrmfollowupdays_id) NOT IN "
					+ " (SELECT concat(precrmfollowup_preowned_id, '-', precrmfollowup_precrmfollowupdays_id) FROM " + compdb(comp_id) + "axela_preowned_crmfollowup)";

			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_preowned_crmfollowup"
					+ " ( precrmfollowup_preowned_id,"
					+ " precrmfollowup_crm_emp_id,"
					+ " precrmfollowup_precrmfollowupdays_id,"
					+ " precrmfollowup_followup_time)" + StrSql;
			// SOP("StrSql crm followup----insert-----------" + StrSql);
			updateQuery(StrSql);

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("Connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("Connection rollback...");
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmail() throws SQLException {
		// SOP("SendEmail==");
		String emailmsg, sub;

		emailmsg = brandconfig_preowned_email_format;
		sub = brandconfig_preowned_email_sub;

		sub = "replace('" + sub + "', '[PREOWNEDID]',preowned_id)";
		sub = "replace(" + sub + ",'[PREOWNEDNAME]',preowned_title)";
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
		sub = "replace(" + sub + ",'[MODELNAME]',preownedmodel_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',variant_name)";
		sub = "REPLACE(" + sub + ", '[BRANCHADDRESS]',branch_add)";

		emailmsg = "replace('" + emailmsg + "', '[PREOWNEDID]',preowned_id)";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDNAME]',preowned_title)";
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
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',preownedmodel_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',variant_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT" + " '"
					+ preowned_contact_id
					+ "',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '"
					+ branch_email1
					+ "',"
					+ " '"
					+ send_contact_email
					+ "',"
					+ " "
					+ unescapehtml(sub)
					+ ","
					+ " "
					+ unescapehtml(emailmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "',"
					+ " "
					+ emp_id
					+ ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
					+ " WHERE preowned_id = " + preowned_id
					+ " LIMIT 1";
			// SOP("StrSql==SendEmail==" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_contact_id," + " email_contact,"
					+ " email_from," + " email_to," + " email_subject,"
					+ " email_msg," + " email_date," + " email_entry_id,"
					+ " email_sent)" + " " + StrSql + "";
			// SOP("StrSql==email==" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("Connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String contact_mobile) throws SQLException {
		// SOP("SendSMS==");
		String smsmsg = brandconfig_preowned_sms_format;

		smsmsg = "replace('" + smsmsg + "', '[PREOWNEDID]',preowned_id)";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDNAME]',preowned_title)";
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
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',preownedmodel_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',variant_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT" + " "
					+ preowned_branch_id
					+ ","
					+ " "
					+ preowned_contact_id
					+ ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '"
					+ contact_mobile
					+ "',"
					+ " "
					+ unescapehtml(smsmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "',"
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
					+ " WHERE preowned_id = " + preowned_id + "" + " LIMIT 1 ";
			// SOP("SELECT StrSql-sms-"+StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id," + " sms_contact_id," + " sms_contact,"
					+ " sms_mobileno," + " sms_msg," + " sms_date,"
					+ " sms_sent," + " sms_entry_id)" + " " + StrSql + "";
			// SOP("StrSql==SendSMS==" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmailToExecutive() throws SQLException {
		// SOP("SendEmailToExecutive==");
		String emailmsg, sub;
		emailmsg = (brandconfig_preowned_email_exe_format);
		sub = (brandconfig_preowned_email_exe_sub);

		sub = "replace('" + sub + "', '[PREOWNEDID]',preowned_id)";
		sub = "replace(" + sub + ",'[PREOWNEDNAME]',preowned_title)";
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
		sub = "replace(" + sub + ",'[MODELNAME]',preownedmodel_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',variant_name)";
		sub = "replace(" + sub + ",'[SOE]',soe_name)";
		sub = "replace(" + sub + ",'[SOB]',sob_name)";
		sub = "REPLACE(" + sub + ", '[BRANCHADDRESS]',branch_add)";

		emailmsg = "replace('" + emailmsg + "', '[PREOWNEDID]',preowned_id)";
		emailmsg = "replace(" + emailmsg + ",'[PREOWNEDNAME]',preowned_title)";
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
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',preownedmodel_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',variant_name)";
		emailmsg = "replace(" + emailmsg + ",'[SOE]',soe_name)";
		emailmsg = "replace(" + emailmsg + ",'[SOB]',sob_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT" + " "
					+ preowned_emp_id
					+ ","
					+ " '"
					+ emp_name
					+ "',"
					+ " '"
					+ branch_email1
					+ "',"
					+ " '"
					+ emp_email_formail
					+ "',"
					+ " "
					+ unescapehtml(sub)
					+ ","
					+ " "
					+ unescapehtml(emailmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "',"
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
					+ " WHERE preowned_id = " + preowned_id + " LIMIT 1";

			// SOP("StrSql-emaile-"+StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql==SendEmailToExecutive==" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMSToExecutive(String emp_mobile) throws SQLException {
		// SOP("SendSMSToExecutive==");
		String smsmsg = (brandconfig_preowned_sms_exe_format);

		smsmsg = "replace('" + smsmsg + "', '[PREOWNEDID]',preowned_id)";
		smsmsg = "replace(" + smsmsg + ",'[PREOWNEDNAME]',preowned_title)";
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
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',preownedmodel_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',variant_name)";
		smsmsg = "replace(" + smsmsg + ",'[SOE]',COALESCE(soe_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[SOB]',COALESCE(sob_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT" + " "
					+ preowned_branch_id
					+ ","
					+ " "
					+ preowned_emp_id
					+ ","
					+ " '"
					+ emp_name
					+ "',"
					+ " '"
					+ emp_mobile
					+ "',"
					+ " "
					+ unescapehtml(smsmsg)
					+ ","
					+ " '"
					+ ToLongDate(kknow())
					+ "',"
					+ " 0,"
					+ " "
					+ emp_id
					+ ""
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id  "
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
					+ " WHERE preowned_id = " + preowned_id
					+ " LIMIT 1";
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
			// SOP("StrSql==SendSMSToExecutive==" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_preowned_soe, config_preowned_sob, config_preowned_refno,"
				+ " config_email_enable, config_sms_enable, comp_email_enable,"
				+ " config_preowned_campaign, config_customer_dupnames, comp_sms_enable,"
				+ " COALESCE(branch_email1, '') AS branch_email1,"
				+ " COALESCE (brandconfig_preowned_email_enable,'') AS brandconfig_preowned_email_enable,"
				+ " COALESCE (brandconfig_preowned_email_sub,'') AS brandconfig_preowned_email_sub,"
				+ " COALESCE (brandconfig_preowned_email_format,'') AS brandconfig_preowned_email_format,"
				+ " COALESCE (brandconfig_preowned_email_exe_enable,'') AS brandconfig_preowned_email_exe_enable,"
				+ " COALESCE (brandconfig_preowned_email_exe_sub,'') AS brandconfig_preowned_email_exe_sub,"
				+ " COALESCE (brandconfig_preowned_email_exe_format,'') AS brandconfig_preowned_email_exe_format,"
				+ " COALESCE (brandconfig_preowned_sms_enable,'') AS brandconfig_preowned_sms_enable,"
				+ " COALESCE (brandconfig_preowned_sms_format,'') AS brandconfig_preowned_sms_format,"
				+ " COALESCE (brandconfig_preowned_sms_exe_enable,'') AS brandconfig_preowned_sms_exe_enable,"
				+ " COALESCE (brandconfig_preowned_sms_exe_format,'') AS brandconfig_preowned_sms_exe_format,"
				+ " COALESCE(emp.emp_email1, '') AS emp_email1,"
				+ " COALESCE(emp.emp_email2, '') AS emp_email2,"
				+ " COALESCE(emp.emp_name, '') AS emp_name,"
				+ " COALESCE(emp.emp_mobile1, '') AS emp_mobile1,"
				+ " COALESCE(emp.emp_mobile2, '') AS emp_mobile2,"
				+ " COALESCE(branch_id, 0) AS branch_id"
				+ " FROM " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp, "
				+ compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + preowned_branch_id + ""
				+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + preowned_emp_id + ""
				+ " WHERE admin.emp_id = " + emp_id + "";
		// SOP("StrSql ---------/-----" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_preowned_refno = crs.getString("config_preowned_refno");
				config_preowned_campaign = crs.getString("config_preowned_campaign");
				config_preowned_soe = crs.getString("config_preowned_soe");
				config_preowned_sob = crs.getString("config_preowned_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_preowned_email_enable = crs.getString("brandconfig_preowned_email_enable");
				brandconfig_preowned_email_format = crs.getString("brandconfig_preowned_email_format");
				brandconfig_preowned_email_sub = crs.getString("brandconfig_preowned_email_sub");
				brandconfig_preowned_email_exe_enable = crs.getString("brandconfig_preowned_email_exe_enable");
				brandconfig_preowned_email_exe_sub = crs.getString("brandconfig_preowned_email_exe_sub");
				brandconfig_preowned_email_exe_format = crs.getString("brandconfig_preowned_email_exe_format");
				brandconfig_preowned_sms_enable = crs.getString("brandconfig_preowned_sms_enable");
				brandconfig_preowned_sms_format = crs.getString("brandconfig_preowned_sms_format");
				brandconfig_preowned_sms_exe_enable = crs.getString("brandconfig_preowned_sms_exe_enable");
				brandconfig_preowned_sms_exe_format = crs.getString("brandconfig_preowned_sms_exe_format");
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
				preowned_branch_id = crs.getString("branch_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateContactCustomerDetails() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, customer_name, customer_emp_id, contact_title_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_id, contact_mobile1, contact_mobile2, contact_email1,"
					+ " contact_email2, contact_phone1, contact_address, customer_branch_id,"
					+ " CONCAT(branch_name, ' (',branch_code, ')') AS branchname, contact_pin"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					+ " WHERE contact_id = " + preowned_contact_id
					+ " GROUP by contact_id"
					+ " ORDER by contact_fname";
			// SOPInfo("PopulateContactCustomerDetails------------" + StrSql);
			// StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				customer_name = crs.getString("customer_name");
				contact_name = crs.getString("contact_name");
				if (inbound_check.equals("yes")) {
					customer_info = "<b>" + customer_name + " (" + crs.getString("customer_id") + ")</b>";
					contact_info = "<b>" + contact_name + " (" + crs.getString("contact_id") + ")</b>";
				} else {
					customer_info = "<a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + " target=_blank><b>"
							+ customer_name + " (" + crs.getString("customer_id") + ")</b></a>";
					contact_info = "<a href=../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + " target=_blank><b>"
							+ contact_name + " (" + crs.getString("contact_id") + ")</b></a>";
				}

				if (!crs.getString("contact_mobile1").equals("")) {
					contact_info = contact_info + "<br>" + crs.getString("contact_mobile1");
					contact_mobile1 = crs.getString("contact_mobile1");
				}
				if (!crs.getString("contact_mobile2").equals("")) {
					contact_info = contact_info + "<br>" + crs.getString("contact_mobile2");
					contact_mobile2 = crs.getString("contact_mobile2");
				}
				preowned_customer_id = crs.getString("customer_id");
				preowned_contact_id = crs.getString("contact_id");
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				contact_phone1 = crs.getString("contact_phone1");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " AND branch_branchtype_id = 2"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr------PopulateBranches--------------" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateCampaign() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch On campaign_id = camptrans_campaign_id"
					+ " WHERE camptrans_branch_id = " + preowned_branch_id + ""
					+ " AND campaign_active = '1'"
					+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('" + ConvertShortDateToStr(preowned_date) + "', 1, 8)"
					+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('" + ConvertShortDateToStr(preowned_date) + "', 1, 8)"
					+ " GROUP BY campaign_id"
					+ " ORDER by campaign_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), preowned_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ");
				Str.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
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

	public String PopulateCity(String state_id, String city_id, String dr_city_id) {
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

			Str.append("<SELECT name=").append(dr_city_id).append(" id=").append(dr_city_id).append("  class=selectbox>\n");
			Str.append("<option value = 0>Select</option>\n");
			if (!state_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("city_id"));
					Str.append(StrSelectdrop(crs.getString("city_id"), city_id));
					Str.append(">").append(crs.getString("city_name")).append("</option>\n");
				}
			}
			Str.append("</SELECT>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " ORDER by state_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<SELECT name=").append(dr_state_id).append(" id=").append(dr_state_id);
			Str.append(" class=selectbox onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_city_id_contact','");
			Str.append(span_id).append("'); \">\n");
			Str.append("<option value = 0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id"));
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name")).append("</option>\n");
			}
			Str.append("</SELECT>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutives(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ "	AND emp_sales='1'"
					+ " AND emp_active='1' "
					+ " AND ( emp_branch_id IN (SELECT branch_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 AND branch_brand_id IN (SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team ON preownedteam_branch_id = branch_id"
					+ " WHERE preownedteam_branch_id = " + preowned_branch_id + " ))";
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + " "
					+ " AND leave_active = 1 )";
			if (emp_id.equals("1")) {
				StrSql += " OR emp_id = 1 ";
			}
			StrSql += ")";
			StrSql = StrSql + " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql------SalesExecutives---" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), preowned_sales_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String preowned_branch_id, String preownedteam_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedteam_id, preownedteam_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_team"
					+ " WHERE preownedteam_branch_id = " + preowned_branch_id
					+ " GROUP BY preownedteam_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=\"dr_preowned_team\" id=\"dr_preowned_team\" class=\"dropdown form-control\" onchange=\"PopulateExecutive();\">");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedteam_id"));
				Str.append(StrSelectdrop(crs.getString("preownedteam_id"), preownedteam_id));
				Str.append(">").append(crs.getString("preownedteam_name")).append("</option>\n");
			}
			Str.append("</SELECT>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedExecutives(String branch_id, String team_id, String preowned_emp_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		// SOP("preowned_emp_id==111=" + preowned_emp_id);
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_preowned = 1"
					+ " AND emp_active = 1"
					+ " AND emp_branch_id = " + branch_id;
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id IN (SELECT preownedteamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe "
						+ " WHERE preownedteamtrans_team_id =" + team_id + ")";
			}

			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + " "
					+ " AND leave_active = 1 )";

			// SOP("emp_id===" + emp_id);
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id,"
						+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id = 1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("PopulatePreOwnedExecutives ==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_emp_id\" id=\"dr_preowned_emp_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), preowned_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " WHERE sob_name != 'Others'"
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), preowned_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), preowned_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	// public String PopulateStatus() {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "Select preownedstatus_id, preownedstatus_name"
	// + " FROM " + compdb(comp_id) + "axela_preowned_status"
	// + " WHERE 1=1"
	// + " ORDER BY preownedstatus_name";
	// // SOP("StrSql--"+StrSql);
	// ResultSet rs = processQuery(StrSql, 0);
	// Str.append("<option value=0>Select</option>");
	// while (rs.next()) {
	// Str.append("<option value=").append(rs.getString("preownedstatus_id")).append("");
	// Str.append(StrSelectdrop(rs.getString("preownedstatus_id"),
	// preowned_preownedstatus_id));
	// Str.append(">").append(rs.getString("preownedstatus_name")).append("</option>\n");
	// }
	// rsClose(rs);
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }
	public String PopulateFuel() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), preowned_fueltype_id));
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

	public String PopulateOwnership() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ownership_id, ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " ORDER BY ownership_id";
			Str.append("<option value=0>Select</option>");
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), preowned_ownership_id));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInsuranceType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurance_id, insurance_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_insurance"
					+ " ORDER BY insurance_name";
			Str.append("<option value=0>Select</option>");
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurance_id")).append("");
				Str.append(StrSelectdrop(crs.getString("insurance_id"), preowned_insurance_id));
				Str.append(">").append(crs.getString("insurance_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateNoc() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", preowned_noc)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", preowned_noc)).append(">No</option>\n");
		return Str.toString();
	}

	public String PopulateExterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT extcolour_id, extcolour_name"
					+ " FROM axela_preowned_extcolour"
					+ " GROUP BY extcolour_id" + " ORDER BY extcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("extcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("extcolour_id"), preowned_extcolour_id));
				Str.append(">").append(crs.getString("extcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT intcolour_id, intcolour_name"
					+ " FROM axela_preowned_intcolour"
					+ " GROUP BY intcolour_id" + " ORDER BY intcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("intcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("intcolour_id"), preowned_intcolour_id));
				Str.append(">").append(crs.getString("intcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
