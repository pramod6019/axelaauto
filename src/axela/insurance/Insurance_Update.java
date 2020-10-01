package axela.insurance;
// Dilip Kumar 12th APR 2013

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Insurance_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String emp_role_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String insurpolicy_id = "0";
	public String branch_id = "0";
	public String insurpolicy_date = "";
	public String insurpolicydate = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_id = "0";
	public String insurpolicy_contact_id = "0";
	public String insurpolicy_start_date = "";
	public String insurstartdate = "";
	public String insurpolicy_end_date = "";
	public String insurenddate = "";
	public String insur_type_id = "0";
	public String insurpolicy_policytype_id = "0";
	public String insurpolicy_inscomp_id = "0";
	public String insurpolicy_policy_no = "";
	public String insurpolicy_covernote_no = "";
	public String insurpolicy_premium_amt = "0";
	public String insurpolicy_idv_amt = "0";
	public String insurpolicy_od_amt = "0";
	public String insurpolicy_od_discount = "0";
	public String insurpolicy_payout = "0";
	public String insurpolicy_paymode_id = "0";
	public String insurpolicy_cheque_no = "";
	public String insurpolicy_cheque_date = "";
	public String insurpolicy_cheque_bank_id = "0";
	public String insurpolicy_desc = "";
	public String insurpolicy_terms = "";
	public String insur_emp_id = "0";
	public String inscomp_id = "0";
	public String insurpolicy_field_emp_id = "0";
	public String insurpolicy_active = "0";
	public String insurpolicy_notes = "";
	public String insurpolicy_entry_id = "0";
	public String insurpolicy_entry_date = "";
	public String insurpolicy_modified_id = "0";
	public String insurpolicy_modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String history_old_value = "";

	public String link_customer_name = "";
	public String link_contact_name = "";
	public String link_insurenquiry_name = "";
	public String QueryString = "";
	public String comp_sms_enable = "";
	public String branch_email1 = "";
	public String contact_email1 = "", contact_mobile1 = "", contact_mobile2 = "", contact_jobtitle = "";
	public String comp_email_enable = "";
	public String title_desc = "";

	public String link_insurenquiry_reg_no = "";
	public String reg_no = "";
	public String insurenquiry_id = "0";
	public String variant_id = "0";

	public String config_customer_dupnames = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String emp_email1 = "";
	public String branch_insur_new_email_sub = "";
	public String branch_insur_new_email_format = "";
	public String branch_insur_new_sms_enable = "";
	public String branch_insur_new_sms_format = "";
	public String branch_insur_lost_email_enable = "";
	public String branch_insur_lost_email_sub = "";
	public String branch_insur_new_email_enable = "";
	public String branch_insur_lost_email_format = "";
	public String branch_insur_lost_sms_enable = "";
	public String branch_insur_lost_sms_format = "";
	public String emp_name = "", emp_email2 = "", emp_email_formail = "", emp_mobile1 = "", emp_mobile2 = "";
	public String jobtitle_desc = "", field_emp_name = "", emp_phone1;
	public String insurpolicy_so_id = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_policy_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				insurpolicy_id = CNumeric(PadQuotes(request.getParameter("insurpolicy_id")));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				SOP("update==" + insurenquiry_id);
				QueryString = PadQuotes(request.getQueryString());
				// PopulateFields(request, response);
				if (add.equals("yes")) {
					status = "Add";
					PopulateInsurEnquiryDetails();
					if (!addB.equals("yes")) {
						insurpolicydate = strToShortDate(ToShortDate(kknow()));
						insurpolicy_start_date = strToShortDate(ToShortDate(kknow()));
						insurstartdate = insurpolicy_start_date;
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.YEAR, 1);
						cal.add(Calendar.DATE, -1);
						insurpolicy_end_date = strToShortDate(ToShortDate(cal.getTime()));
						insurenddate = insurpolicy_end_date;
						insurpolicy_active = "1";
					} else {
						GetValues(request, response);
						PopulateConfigDetails();
						if (ReturnPerm(comp_id, "emp_insurance_policy_add", request).equals("1")) {

							insurpolicy_entry_id = emp_id;
							insurpolicy_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-list.jsp?insurpolicy_id=" + insurpolicy_id + "&msg=Insurance added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Insurance")) {
						SOP("coming..1");
						PopulateFields(request, response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Insurance")) {
						SOP("coming..2");
						GetValues(request, response);
						// PopulateConfigDetails();
						if (ReturnPerm(comp_id, "emp_insurance_policy_edit", request).equals("1")) {
							SOP("coming..3");
							UpdateFields(request, response);
							SOP("coming..4");
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-list.jsp?insurpolicy_id=" + insurpolicy_id + "&msg=Insurance updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Insurance")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_insurance_policy_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-list.jsp?msg=Insurance deleted successfully!"));
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
		customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
		customer_name = PadQuotes(request.getParameter("customer_name"));
		contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
		contact_name = PadQuotes(request.getParameter("contact_name"));
		reg_no = PadQuotes(request.getParameter("reg_no"));
		variant_id = CNumeric(PadQuotes(request.getParameter("variant_id")));
		insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
		link_customer_name = "<b><a href=\"../customer/customer-list.jsp?customer_id=" + customer_id + "\">" + customer_name + "</a></b>";
		link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + contact_id + "\">" + contact_name + "</a>";
		link_insurenquiry_name = "<a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + insurenquiry_id + "\">"
				+ insurenquiry_id + "</a>";
		link_insurenquiry_reg_no = "<b><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + insurenquiry_id
				+ "\">" + reg_no + "</a></b>";
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		insurpolicydate = PadQuotes(request.getParameter("txt_insur_date"));
		insurstartdate = PadQuotes(request.getParameter("txt_start_date"));
		insurenddate = PadQuotes(request.getParameter("txt_end_date"));
		insurpolicy_policytype_id = CNumeric(PadQuotes(request.getParameter("dr_insur_insurpolicy_id")));
		insurpolicy_inscomp_id = CNumeric(PadQuotes(request.getParameter("dr_insur_inscomp_id")));
		insurpolicy_policy_no = PadQuotes(request.getParameter("txt_insur_policy_no"));
		insur_type_id = CNumeric(PadQuotes(request.getParameter("dr_insur_type")));
		insurpolicy_covernote_no = PadQuotes(request.getParameter("txt_insur_covernote_no"));
		insurpolicy_premium_amt = CNumeric(PadQuotes(request.getParameter("txt_insur_premium_amt")));
		insurpolicy_idv_amt = CNumeric(PadQuotes(request.getParameter("txt_insur_idv_amt")));
		insurpolicy_od_amt = CNumeric(PadQuotes(request.getParameter("txt_insur_od_amt")));
		insurpolicy_od_discount = CNumeric(PadQuotes(request.getParameter("txt_insur_od_discount")));
		insurpolicy_payout = CNumeric(PadQuotes(request.getParameter("txt_insur_payout")));
		insurpolicy_paymode_id = CNumeric(PadQuotes(request.getParameter("dr_insur_paymode_id")));
		insurpolicy_cheque_no = PadQuotes(request.getParameter("txt_insur_cheque_no"));
		insurpolicy_cheque_date = PadQuotes(request.getParameter("txt_insur_cheque_date"));
		insurpolicy_cheque_bank_id = CNumeric(PadQuotes(request.getParameter("dr_insur_cheque_bank_id")));
		insurpolicy_desc = PadQuotes(request.getParameter("txt_insur_desc"));
		insurpolicy_terms = PadQuotes(request.getParameter("txt_insur_terms"));
		insur_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		insurpolicy_active = CheckBoxValue(PadQuotes(request.getParameter("chk_insur_active")));
		insurpolicy_notes = PadQuotes(request.getParameter("txt_insur_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		insurpolicy_entry_date = PadQuotes(request.getParameter("insurpolicy_entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		insurpolicy_modified_date = PadQuotes(request.getParameter("insurpolicy_modified_date"));

	}
	protected void CheckForm() {
		msg = "";

		if (insurpolicydate.equals("")) {
			msg += "<br>Select Issue Date!";
		} else {
			if (isValidDateFormatShort(insurpolicydate)) {
				insurpolicy_date = ConvertShortDateToStr(insurpolicydate);
			} else {
				msg += "<br>Enter valid Issue Date!";
			}
		}

		if (branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (insurstartdate.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(insurstartdate)) {
				insurpolicy_start_date = ConvertShortDateToStr(insurstartdate);
			} else {
				msg += "<br>Enter valid Start Date!";
			}
		}

		if (insurenddate.equals("")) {
			msg += "<br>Select Expiry Date!";
		} else {
			if (isValidDateFormatShort(insurenddate)) {
				insurpolicy_end_date = ConvertShortDateToStr(insurenddate);
			} else {
				msg += "<br>Enter valid Expiry Date!";
			}
		}

		if (!insurenddate.equals("") && !insurstartdate.equals("")
				&& isValidDateFormatShort(insurstartdate) && isValidDateFormatShort(insurenddate)) {
			if (Long.parseLong(ConvertShortDateToStr(insurenddate)) < Long.parseLong(ConvertShortDateToStr(insurstartdate))) {
				msg += "<br>Expiry Date should be greater than Start Date!";
			} else if (getDaysBetween(ConvertShortDateToStr(insurstartdate), ConvertShortDateToStr(insurenddate)) > 365) {
				msg += "<br>Expiry Date can't be greater than 1 Year!";
			}
		}

		if (insurpolicy_policytype_id.equals("0")) {
			msg += "<br>Select Insurance Policy!";
		}

		if (insurpolicy_inscomp_id.equals("0")) {
			msg += "<br>Select Company!";
		}

		if (insur_type_id.equals("0")) {
			msg += "<br>Select Type!";
		}

		if (insurpolicy_premium_amt.equals("0")) {
			msg += "<br>Enter Premium Amount!";
		} else if (!isFloat(insurpolicy_premium_amt)) {
			msg += "<br>Enter valid Premium Amount!";
		}

		if (insurpolicy_paymode_id.equals("0")) {
			msg += "<br>Select Payment Mode!";
		} else if (insurpolicy_paymode_id.equals("2")) {
			if (insurpolicy_cheque_no.equals("")) {
				msg += "<br>Enter Cheque No.!";
			}

			if (insurpolicy_cheque_date.equals("")) {
				msg += "<br>Enter Cheque Date!";
			} else if (!isValidDateFormatShort(insurpolicy_cheque_date)) {
				msg += "<br>Enter valid Cheque Date!";
			}

			if (insurpolicy_cheque_bank_id.equals("0")) {
				msg += "<br>Select Cheque Bank!";
			}
		} else {
			insurpolicy_cheque_no = "";
			insurpolicy_cheque_date = "";
			insurpolicy_cheque_bank_id = "0";
		}
		if (insur_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_policy"
					+ " (insurpolicy_branch_id,"
					+ " insurpolicy_customer_id,"
					+ " insurpolicy_contact_id,"
					+ " insurpolicy_insurenquiry_id,"
					+ " insurpolicy_date,"
					+ " insurpolicy_start_date,"
					+ " insurpolicy_end_date,"
					+ " insurpolicy_insurtype_id,"
					+ " insurpolicy_inscomp_id,"
					+ " insurpolicy_policytype_id,"
					+ " insurpolicy_policy_no,"
					+ " insurpolicy_covernote_no,"
					+ " insurpolicy_premium_amt,"
					+ " insurpolicy_paymode_id,"
					+ " insurpolicy_cheque_no,"
					+ " insurpolicy_cheque_date,"
					+ " insurpolicy_cheque_bank_id,"
					+ " insurpolicy_idv_amt,"
					+ " insurpolicy_od_amt,"
					+ " insurpolicy_od_discount,"
					+ " insurpolicy_payout,"
					+ " insurpolicy_desc,"
					+ " insurpolicy_terms,"
					+ " insurpolicy_emp_id,"
					+ " insurpolicy_active,"
					+ " insurpolicy_notes,"
					+ " insurpolicy_entry_id,"
					+ " insurpolicy_entry_date)"
					+ " VALUES"
					+ " (" + branch_id + ","
					+ " " + customer_id + ","
					+ " " + contact_id + ","
					+ " " + insurenquiry_id + ","
					+ " '" + insurpolicy_date + "',"
					+ " '" + insurpolicy_start_date + "',"
					+ " '" + insurpolicy_end_date + "',"
					+ " " + insur_type_id + ","
					+ " '" + insurpolicy_inscomp_id + "',"
					+ " '" + insurpolicy_policytype_id + "',"
					+ " '" + insurpolicy_policy_no + "',"
					+ " '" + insurpolicy_covernote_no + "',"
					+ " '" + insurpolicy_premium_amt + "',"
					+ " '" + insurpolicy_paymode_id + "',"
					+ " '" + insurpolicy_cheque_no + "',"
					+ " '" + insurpolicy_cheque_date + "',"
					+ " '" + insurpolicy_cheque_bank_id + "',"
					+ " '" + insurpolicy_idv_amt + "',"
					+ " '" + insurpolicy_od_amt + "',"
					+ " '" + insurpolicy_od_discount + "',"
					+ " '" + insurpolicy_payout + "',"
					+ " '" + insurpolicy_desc + "',"
					+ " '" + insurpolicy_terms + "',"
					+ " " + insur_emp_id + ","
					+ " '" + insurpolicy_active + "',"
					+ " '" + insurpolicy_notes + "',"
					+ " " + insurpolicy_entry_id + ","
					+ " " + insurpolicy_entry_date + ")";
			SOP("StrSql----Add-----" + StrSql);
			insurpolicy_id = UpdateQueryReturnID(StrSql);
			// SOP("contact_email1-------" + contact_email1);
			// SOP("branch_insur_new_email_format-------" + branch_insur_new_email_format);
			// SOP("branch_insur_new_email_sub-------" + branch_insur_new_email_sub);
			//
			// SOP("comp_sms_enable-------" + comp_sms_enable);
			// SOP("comp_sms_enable-------" + comp_sms_enable);
			// SOP("branch_insur_new_sms_enable-------" + branch_insur_new_sms_enable);

			if (insurpolicy_active.equals("1")) {
				if (!contact_email1.equals("")
						&& !branch_insur_new_email_format.equals("")
						&& !branch_insur_new_email_sub.equals("")) {
					SendEmail();
				}

				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && branch_insur_new_sms_enable.equals("1")) {
					if (!branch_insur_new_sms_format.equals("")) {
						if (!contact_mobile1.equals("")) {
							SendSMS(contact_mobile1);
						}
						if (!contact_mobile2.equals("")) {
							SendSMS(contact_mobile2);
						}
					}
				}
			}

			if (!insurpolicy_id.equals("0")) {
				// /// Delete Veh insurance follow-up
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup "
						+ " WHERE insurfollowup_insurenquiry_id = " + insurenquiry_id
						+ " AND insurfollowup_desc = ''";
				// SOP("StrSql-------delete--------" + StrSql);
				updateQuery(StrSql);

				// /// Insert Veh insurance follow-up
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
						+ " (insurfollowup_insurenquiry_id,"
						+ " insurfollowup_insurlostcase1_id,"
						+ " insurfollowup_followup_time,"
						+ " insurfollowup_entry_id,"
						+ " insurfollowup_entry_time,"
						+ " insurfollowup_trigger)"
						+ " VALUES"
						+ " (" + insurenquiry_id + ","
						+ " 0,"
						+ " DATE_FORMAT(DATE_ADD('" + insurpolicy_end_date + "',INTERVAL -2 MONTH), '%Y%m%d%H%i%s')," + ""
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 0)";
				updateQuery(StrSql);

				// for history and make it booked
				if (!CNumeric(insurenquiry_id).equals("0")) {
					StrSql = "SELECT insurstatus_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry_status ON insurstatus_id = insurenquiry_insurstatus_id"
							+ " WHERE 1=1"
							+ " AND insurenquiry_id = " + insurenquiry_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);

					while (crs.next()) {
						history_old_value = crs.getString("insurstatus_name");
					}
					crs.close();
				}

				StrSql = " UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET insurenquiry_insurstatus_id = 2,"
						+ " insurenquiry_insurstatus_date = " + ToLongDate(kknow())
						+ " WHERE 1=1"
						+ " AND insurenquiry_id = " + insurenquiry_id;
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue"
						+ ") VALUES ('"
						+ insurenquiry_id + "',"
						+ " '" + emp_id + "'," + " '" + ToLongDate(kknow()) + "',"
						+ " 'STATUS',"
						+ " '" + history_old_value + "',"
						+ " 'Policy Booked'"
						+ ")";
				updateQuery(StrSql);
			}

			// SOP("StrSql------INSERT--insur_followup--" + StrSqlBreaker(StrSql));
		}
	}
	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT *,"
					+ " insurenquiry_id,"
					+ " insurenquiry_variant_id,  COALESCE(variant_name,'') AS variant_name, insurenquiry_reg_no,"
					+ " COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurpolicy_insurenquiry_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp  ON emp_id = insurpolicy_emp_id"
					+ " WHERE insurpolicy_id = " + insurpolicy_id
					+ BranchAccess + "";
			SOP("StrSql---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					contact_id = crs.getString("insurpolicy_contact_id");
					contact_name = crs.getString("contact_name");
					customer_id = crs.getString("insurpolicy_customer_id");
					customer_name = crs.getString("customer_name");
					insurenquiry_id = crs.getString("insurenquiry_id");
					variant_id = crs.getString("insurenquiry_variant_id");

					link_customer_name = "<b><a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a></b>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("contact_name") + "</a>";
					link_insurenquiry_name = "<a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + crs.getString("insurenquiry_id") + "\">"
							+ crs.getString("insurenquiry_id") + "</a>";
					reg_no = crs.getString("insurenquiry_reg_no");
					link_insurenquiry_reg_no = "<b><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + crs.getString("insurenquiry_id") + "\">"
							+ crs.getString("insurenquiry_reg_no") + "</a></b>";

					branch_id = crs.getString("branch_id");
					branch_id = crs.getString("insurpolicy_branch_id");
					insur_type_id = crs.getString("insurpolicy_insurtype_id");
					insurpolicy_date = crs.getString("insurpolicy_date");
					insurpolicydate = strToShortDate(insurpolicy_date);
					insurpolicy_start_date = crs.getString("insurpolicy_start_date");
					insurstartdate = strToShortDate(insurpolicy_start_date);
					insurpolicy_end_date = crs.getString("insurpolicy_end_date");
					insurenddate = strToShortDate(insurpolicy_end_date);
					insurpolicy_policytype_id = crs.getString("insurpolicy_policytype_id");
					insurpolicy_inscomp_id = crs.getString("insurpolicy_inscomp_id");
					insurpolicy_policy_no = crs.getString("insurpolicy_policy_no");
					insurpolicy_covernote_no = crs.getString("insurpolicy_covernote_no");
					insurpolicy_premium_amt = df.format(Double.parseDouble(crs.getString("insurpolicy_premium_amt")));
					insurpolicy_paymode_id = crs.getString("insurpolicy_paymode_id");
					insurpolicy_cheque_no = crs.getString("insurpolicy_cheque_no");
					insurpolicy_cheque_date = crs.getString("insurpolicy_cheque_date");
					insurpolicy_cheque_bank_id = crs.getString("insurpolicy_cheque_bank_id");
					insurpolicy_idv_amt = crs.getString("insurpolicy_idv_amt");
					insurpolicy_od_amt = crs.getString("insurpolicy_od_amt");
					insurpolicy_od_discount = crs.getString("insurpolicy_od_discount");
					insurpolicy_payout = crs.getString("insurpolicy_payout");
					insurpolicy_premium_amt = df.format(Double.parseDouble(crs.getString("insurpolicy_premium_amt")));
					insurpolicy_desc = crs.getString("insurpolicy_desc");
					insurpolicy_terms = crs.getString("insurpolicy_terms");
					insur_emp_id = crs.getString("insurpolicy_emp_id");
					insurpolicy_active = crs.getString("insurpolicy_active");
					insurpolicy_notes = crs.getString("insurpolicy_notes");
					insurpolicy_entry_id = crs.getString("insurpolicy_entry_id");
					if (!insurpolicy_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(insurpolicy_entry_id));
					}
					insurpolicy_entry_date = strToLongDate(crs.getString("insurpolicy_entry_date"));
					insurpolicy_modified_id = crs.getString("insurpolicy_modified_id");
					if (!insurpolicy_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(insurpolicy_modified_id));
					}
					insurpolicy_modified_date = strToLongDate(crs.getString("insurpolicy_modified_date"));
				}
			} else {
				msg = "msg=Access denied. Please contact system administrator!";
				response.sendRedirect("../portal/error.jsp?" + msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			insurpolicy_modified_id = emp_id;
			insurpolicy_modified_date = ToLongDate(kknow());

			StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_policy"
					+ " SET"
					+ " insurpolicy_branch_id = " + branch_id + ","
					+ " insurpolicy_insurenquiry_id = " + insurenquiry_id + ","
					+ " insurpolicy_contact_id = " + contact_id + ","
					+ " insurpolicy_customer_id = " + customer_id + ","
					+ " insurpolicy_insurtype_id = " + insur_type_id + ","
					+ " insurpolicy_date = '" + insurpolicy_date + "',"
					+ " insurpolicy_start_date = '" + insurpolicy_start_date + "',"
					+ " insurpolicy_end_date = '" + insurpolicy_end_date + "',"
					+ " insurpolicy_policytype_id = '" + insurpolicy_policytype_id + "',"
					+ " insurpolicy_inscomp_id = '" + insurpolicy_inscomp_id + "',"
					+ " insurpolicy_policy_no = '" + insurpolicy_policy_no + "',"
					+ " insurpolicy_covernote_no = '" + insurpolicy_covernote_no + "',"
					+ " insurpolicy_premium_amt = '" + insurpolicy_premium_amt + "',"
					+ " insurpolicy_paymode_id = '" + insurpolicy_paymode_id + "',"
					+ " insurpolicy_cheque_no = '" + insurpolicy_cheque_no + "',"
					+ " insurpolicy_cheque_date = '" + insurpolicy_cheque_date + "',"
					+ " insurpolicy_cheque_bank_id = '" + insurpolicy_cheque_bank_id + "',"
					+ " insurpolicy_idv_amt = '" + insurpolicy_idv_amt + "',"
					+ " insurpolicy_od_amt = '" + insurpolicy_od_amt + "',"
					+ " insurpolicy_od_discount = '" + insurpolicy_od_discount + "',"
					+ " insurpolicy_payout = '" + insurpolicy_payout + "',"
					+ " insurpolicy_policy_no = '" + insurpolicy_policy_no + "',"
					+ " insurpolicy_desc = '" + insurpolicy_desc + "',"
					+ " insurpolicy_terms = '" + insurpolicy_terms + "',"
					+ " insurpolicy_emp_id = " + insur_emp_id + ","
					+ " insurpolicy_active = '" + insurpolicy_active + "',"
					+ " insurpolicy_notes = '" + insurpolicy_notes + "',"
					+ " insurpolicy_modified_id = " + insurpolicy_modified_id + ","
					+ " insurpolicy_modified_date = " + insurpolicy_modified_date + ""
					+ " WHERE insurpolicy_id = " + insurpolicy_id + "";
			updateQuery(StrSql);
			SOP("StrSql=====" + StrSql);
			if (insurpolicy_active.equals("0")) {
				// SendEmailToNewInsurance(insurpolicy_id);
			}
		}
	}

	protected String SendEmailToNewInsurance(String insurpolicy_id) {
		String email_msg = "", branch_id = "0";
		String subject = "";
		String branch_insur_email = "", branch_email1 = "";
		try {
			StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, veh_id, veh_reg_no, insurpolicy_contact_id,"
					+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " insurpolicy_date, policytype_name, insurpolicy_policy_no, inscomp_name,"
					+ " veh_chassis_no, veh_engine_no, branch_email1, branch_insur_email,"
					+ " insurpolicy_premium_amt, insurpolicy_idv_amt, insurpolicy_od_amt, insurpolicy_od_discount,"
					+ " insurtype_name, insurpolicy_customer_id, insurpolicy_start_date, insurpolicy_end_date, customer_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
					+ " model_name, insurpolicy_active, item_name, insurpolicy_emp_id, insurpolicy_field_emp_id,"
					+ " CONCAT(field.emp_name, ' (', field.emp_ref_no, ')') AS field_emp_name,"
					+ " CONCAT(insur.emp_name, ' (', insur.emp_ref_no, ')') AS insur_emp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = insurpolicy_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = insurpolicy_inscomp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp insur ON insur.emp_id = insurpolicy_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp field ON field.emp_id = insurpolicy_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
					+ " WHERE insurpolicy_id = " + insurpolicy_id + ""
					+ " GROUP BY insurpolicy_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_id = crs.getString("branch_id");
					if (insurpolicy_active.equals("1")) {
						subject = "Insurance Done";
						email_msg = " Dear All, <br><br>The following insurance done by our colleague " + crs.getString("insur_emp_name")
								+ ".<br><br>";
					} else if (insurpolicy_active.equals("0")) {
						subject = "Insurance Cancelled";
						email_msg = " Dear All, <br><br>The following insurance done by " + crs.getString("insur_emp_name")
								+ " is cancelled.<br><br>";
					}

					branch_insur_email = crs.getString("branch_insur_email");
					branch_email1 = crs.getString("branch_email1");

					email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n"
							+ "<tr height=\"30\">\n"
							+ "<td valign=\"top\">Insurance ID</td>\n"
							+ "<td valign=\"top\">Vehicle ID</td>\n"
							+ "<td valign=\"top\">Chassis No.</td>\n"
							+ "<td valign=\"top\">Engine No.</td>\n"
							+ "<td valign=\"top\">Reg. No.</td>\n"
							+ "<td valign=\"top\">Contact Name</td>\n"
							+ "<td valign=\"top\">Field Executive</td>\n"
							+ "<td valign=\"top\">Start Date</td>\n"
							+ "<td valign=\"top\">End Date</td>\n"
							+ "<td valign=\"top\">Car</td>\n"
							+ "</tr>\n"
							+ "<tr height=\"50\">\n"
							+ "<td valign=\"top\">" + crs.getString("insurpolicy_id") + "</td>\n"
							+ "<td valign=\"top\">" + crs.getString("veh_id") + "</td>\n"
							+ "<td valign=\"top\">" + crs.getString("veh_chassis_no") + "&nbsp;</td>\n"
							+ "<td valign=\"top\">" + crs.getString("veh_engine_no") + "&nbsp;</td>\n"
							+ "<td valign=\"top\">" + crs.getString("veh_reg_no") + "&nbsp;</td>\n"
							+ "<td valign=\"top\">" + crs.getString("contact_name") + "</td>\n"
							+ "<td valign=\"top\">" + crs.getString("field_emp_name") + "</td>\n"
							+ "<td valign=\"top\">" + strToShortDate(crs.getString("insurpolicy_start_date")) + " </td>\n"
							+ "<td valign=\"top\">" + strToShortDate(crs.getString("insurpolicy_end_date")) + "&nbsp;</td>\n"
							+ "<td valign=\"top\">" + crs.getString("item_name");
					if (!crs.getString("model_name").equals("")) {
						email_msg += "<br> Model: " + crs.getString("model_name");
					}

					email_msg += "&nbsp;</td>\n"
							+ "</tr>\n</table>\n";
				}
				email_msg += "<br><br>Regards,<br><br>";

				crs.beforeFirst();
				while (crs.next()) {
					email_msg += crs.getString("insur_emp_name")
							+ "<br>" + crs.getString("branch_name");
				}

				email_msg = "<html><body><basefont face=arial, verdana size=\"2\">" + email_msg + "</body></html>";

				if (!branch_email1.equals("") && !branch_insur_email.equals("")) {
					// insert into the email table as sent
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
							+ " ("
							+ "	email_branch_id,"
							+ "	email_contact_id,"
							+ " email_contact,"
							+ " email_from,"
							+ " email_to,"
							+ " email_subject,"
							+ " email_msg,"
							+ " email_date,"
							+ " email_entry_id,"
							+ " email_sent)"
							+ " VALUES"
							+ " ("
							+ " " + branch_id + ","
							+ "	0,"
							+ " '',"
							+ " '" + branch_email1 + "',"
							+ " '" + branch_insur_email + "',"
							+ " '" + subject + "',"
							+ " '" + email_msg + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + emp_id + ","
							+ " 0)";
					// SOP("StrSql=="+StrSql);
					updateQuery(StrSql);
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			email_msg = "";
		}
		return email_msg;
	}

	protected void DeleteFields(HttpServletResponse response) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_docs"
					+ " WHERE doc_insur_id = " + insurpolicy_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " WHERE insurpolicy_id = " + insurpolicy_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulatePaymentMode() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT paymode_id, paymode_name"
					+ " FROM axela_acc_paymode"
					+ " GROUP BY paymode_id"
					+ " ORDER BY paymode_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paymode_id"));
				Str.append(StrSelectdrop(crs.getString("paymode_id"), insurpolicy_paymode_id));
				Str.append(">").append(crs.getString("paymode_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateInsurEnquiryDetails() {
		try {
			if (!insurenquiry_id.equals("0")) {
				StrSql = "SELECT insurenquiry_id, insurenquiry_branch_id,"
						+ " insurenquiry_customer_id, insurenquiry_contact_id,"
						+ " insurenquiry_variant_id, insurenquiry_emp_id,"
						+ " insurenquiry_reg_no, customer_id,"
						+ " customer_name, contact_id,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " WHERE 1 = 1"
						+ " AND insurenquiry_id = " + insurenquiry_id + "";

				SOP("StrSql======" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_id = crs.getString("insurenquiry_customer_id");
					customer_name = crs.getString("customer_name");
					contact_id = crs.getString("insurenquiry_contact_id");
					contact_name = crs.getString("contact_name");
					variant_id = crs.getString("insurenquiry_variant_id");
					reg_no = crs.getString("insurenquiry_reg_no");

					link_insurenquiry_reg_no = "<b><a href=\"../insurance/insurance-enquiry-list.jsp?insurenquiry_id=" + crs.getString("insurenquiry_id") + "\">"
							+ crs.getString("insurenquiry_reg_no") + "</a></b>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("contact_name") + "</a>";
					link_customer_name = "<b><a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a></b>";

					branch_id = crs.getString("insurenquiry_branch_id");
					insur_emp_id = crs.getString("insurenquiry_emp_id");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_sob , config_sales_enquiry_refno,"
				+ " config_email_enable, config_sms_enable, config_sales_lead_for_enquiry, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign, config_customer_dupnames, COALESCE(jobtitle_desc,'') AS jobtitle_desc , "
				+ " COALESCE(branch_email1,'') AS branch_email1, "
				+ " COALESCE(branch_insur_new_email_enable,'') AS branch_insur_new_email_enable,"
				+ " COALESCE(branch_insur_new_email_sub,'') AS branch_insur_new_email_sub,"
				+ " COALESCE(branch_insur_new_email_format,'') AS branch_insur_new_email_format,"
				+ " COALESCE(branch_insur_new_sms_enable,'') AS branch_insur_new_sms_enable,"
				+ " COALESCE(branch_insur_new_sms_format,'') AS branch_insur_new_sms_format,"
				+ " COALESCE(branch_insur_lost_email_enable,'') AS branch_insur_lost_email_enable,"
				+ " COALESCE(branch_insur_lost_email_sub,'') AS branch_insur_lost_email_sub,"
				+ " COALESCE(branch_insur_lost_email_format,'') AS branch_insur_lost_email_format,"
				+ " COALESCE(branch_insur_lost_sms_enable,'') AS branch_insur_lost_sms_enable, "
				+ " COALESCE(branch_insur_lost_sms_format,'') AS branch_insur_lost_sms_format, "
				+ " COALESCE(emp.emp_email1,'') AS emp_email1, COALESCE(emp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(emp.emp_name,'') AS emp_name,"
				+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1, COALESCE(emp.emp_mobile2,'') AS emp_mobile2"
				+ " FROM " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp, "
				+ compdb(comp_id) + "axela_emp admin"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + ""
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + insur_emp_id + ""
				// + " INNER JOIN axela_emp insur ON insur.emp_id = insurpolicy_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp.emp_jobtitle_id"
				+ " WHERE 1 = 1 "
				+ " AND admin.emp_id = " + emp_id;

		// SOP("config======" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				branch_insur_new_email_sub = crs.getString("branch_insur_new_email_sub");
				branch_insur_new_email_format = crs.getString("branch_insur_new_email_format");
				branch_insur_new_email_enable = crs.getString("branch_insur_new_email_enable");
				branch_insur_new_sms_enable = crs.getString("branch_insur_new_sms_enable");
				branch_insur_new_sms_format = crs.getString("branch_insur_new_sms_format");
				branch_insur_lost_email_enable = crs.getString("branch_insur_lost_email_enable");
				branch_insur_lost_email_sub = crs.getString("branch_insur_lost_email_sub");
				branch_insur_lost_email_format = crs.getString("branch_insur_lost_email_format");
				branch_insur_lost_sms_enable = crs.getString("branch_insur_lost_sms_enable");
				branch_insur_lost_sms_format = crs.getString("branch_insur_lost_sms_format");
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
				jobtitle_desc = crs.getString("jobtitle_desc");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateInsurance() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT inscomp_name, inscomp_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_active = 1"
					+ " AND inscomp_value != ''"
					+ " GROUP BY inscomp_id"
					+ " ORDER BY inscomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inscomp_id"));
				Str.append(Selectdrop(crs.getInt("inscomp_id"), inscomp_id));
				Str.append(">").append(crs.getString("inscomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExecutive(String insur_emp_id) {
		StringBuilder Str = new StringBuilder();

		// SOP("insurpolicy_emp_id----Executive------" + insurpolicy_emp_id);
		try {
			StrSql = "SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), insur_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateFieldExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_fieldinsur = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), insurpolicy_field_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateChequeBank() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bank_id, bank_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_bank"
					+ " GROUP BY bank_id"
					+ " ORDER BY bank_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bank_id"));
				Str.append(StrSelectdrop(crs.getString("bank_id"), insurpolicy_cheque_bank_id));
				Str.append(">").append(crs.getString("bank_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateInsuranceCompany() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT inscomp_id, inscomp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_active = 1"
					+ " ORDER BY inscomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inscomp_id"));
				Str.append(StrSelectdrop(crs.getString("inscomp_id"), insurpolicy_inscomp_id));
				Str.append(">").append(crs.getString("inscomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsuranceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(StrSelectdrop(crs.getString("insurtype_id"), insur_type_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurancePolicy() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT policytype_id, policytype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " GROUP BY policytype_id"
					+ " ORDER BY policytype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("policytype_id"));
				Str.append(StrSelectdrop(crs.getString("policytype_id"), insurpolicy_policytype_id));
				Str.append(">").append(crs.getString("policytype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void SendEmail() throws SQLException {
		String emailmsg, sub;

		// SOP("emp_name--------------" + emp_name);
		// SOP("jobtitle_desc--------------" + jobtitle_desc);
		// SOP("emp_mobile1--------------" + emp_mobile1);
		// SOP("emp_email1--------------" + emp_email1);
		// SOP("emp_phone1--------------" + emp_phone1);

		sub = "REPLACE(branch_insur_new_email_sub, '[INSURID]',insurpolicy_id)";
		sub = "REPLACE(" + sub + ",'[INSURSTRDATE]',insurpolicy_start_date)";
		sub = "REPLACE(" + sub + ",'[INSURENDDATE]',insurpolicy_end_date)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[INSUREXENAME]',emp_name)";
		sub = "REPLACE(" + sub + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[INSUREXEPHONE1]',emp_phone1)";
		sub = "REPLACE(" + sub + ",'[INSUREXEEMAIL1]',emp_email1)";
		sub = "REPLACE(" + sub + ",'[MODELNAME]',model_name)";
		sub = "REPLACE(" + sub + ",'[ITEMNAME]',item_name)";
		// SOP("sub------------" + sub);

		emailmsg = "REPLACE(branch_insur_new_email_format, '[INSURID]',insurpolicy_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSURSTRDATE]',insurpolicy_start_date)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSURENDDATE]',insurpolicy_end_date)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXENAME]',emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEPHONE1]',emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[INSUREXEEMAIL1]',emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[ITEMNAME]',item_name)";
		// SOP("emailmsg------------" + emailmsg);
		try {
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + insurpolicy_contact_id + "',"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + emp_email1 + "',"
					+ " '" + contact_email1 + "',"
					+ " " + (sub) + ","
					+ " " + (emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = insurpolicy_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " where insurpolicy_id = " + insurpolicy_id;
			// SOP("StrSql-------email--2-------" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql--------email-insert-------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String contact_mobile) throws SQLException {
		String smsmsg, sub;
		smsmsg = "REPLACE(branch_insur_new_sms_format, '[INSURID]',insurpolicy_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSURSTRDATE]',insurpolicy_start_date)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSURENDDATE]',insurpolicy_end_date)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXENAME]',emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEPHONE1]',emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[INSUREXEEMAIL1]',emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[ITEMNAME]',item_name)";
		// SOP("contact_mobile-------sms-----" + contact_mobile);
		try {
			StrSql = "SELECT"
					+ " " + insurpolicy_contact_id + ","
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + contact_mobile + "',"
					+ " " + (smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_emp ON emp_id = insurpolicy_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = insurpolicy_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " where insurpolicy_id = " + insurpolicy_id;
			// SOP("StrSql-------sms 1-----" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql-------sms-----" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
