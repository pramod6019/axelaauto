// Ved Prakash (11, 12 Feb 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Branch_Update extends Connect {

	public String updateB = "";
	public String update = "";
	public String StrSql = "";
	public String add = "";
	public String deleteB = "";
	public String addB = "";
	public static String status = "";
	public String msg = "";
	public static String Msg1 = "";
	public String branch_id = "";
	public String branch_name = "";
	public String branch_invoice_name = "";
	public String branch_region_id = "0", branch_zone_id = "0";
	public String branch_branchtype_id = "0";
	// public String franchisee_id = "";
	public String branch_phone1 = "";
	public String branch_phone2 = "";
	public String branch_mobile1 = "";
	public String branch_mobile2 = "";
	public String branch_email1 = "";
	public String branch_email2 = "";
	public String branch_sales_email = "";
	public String[] sales_email = null;
	public String[] sales_mobile = null;
	public String[] ticket_email = null;
	public String[] crm_email = null;
	public String[] socin_email = null;
	public String[] socin_mobile = null;
	public String[] din_email = null;
	public String[] din_mobile = null;
	public String[] jcpsf_email = null;
	public String[] service_email = null;
	public String branch_sales_mobile = "";
	public String branch_servicebooking_email = "";
	public String branch_ticket_email = "";
	public String branch_crm_email = "";
	public String branch_jcpsf_email = "";
	public String branch_crm_mobile = "";
	public String branch_socin_email = "";
	public String branch_socin_mobile = "";
	public String branch_din_email = "";
	public String branch_din_mobile = "";
	public String branch_jcpsf_mobile = "";
	public String branch_add = "";
	public String QueryString = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String branch_notes = "";
	public String branch_entry_date = "";
	public String branch_modified_date = "";
	public String branch_franchisee_id = "0";
	public String branch_brand_id = "0";
	public String branch_active = "1";
	int active_branchcount = 0;
	// Get the branchcount FROM Connect class
	public int branch_count = branchcount;
	public String branch_entry_id = "";
	public String entry_date = "";
	public String branch_modified_id = "0";
	public String modified_date = "";
	// public String state_id = "0";
	public String branch_rateclass_id = "0";
	public String branch_code = "";
	public String branch_tin = "";
	public String branch_cin = "";
	public String branch_gst = "";
	public String branch_vat = "";
	public String branch_cst = "";
	public String branch_pan = "";
	public String branch_quote_prefix = "";
	public String branch_so_prefix = "";
	public String branch_invoice_prefix = "";
	public String branch_receipt_prefix = "";
	public String branch_bill_prefix = "";
	public String branch_jc_prefix = "";
	public String branch_modified_by = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_entry_by = "";
	public String quote_desc = "";
	public String quote_terms = "";
	public String so_desc = "", branch_invoice_terms = "", branch_bill_terms = "";
	public String so_terms = "";
	public String invoice_desc = "";
	public String invoice_terms = "";
	public String receipt_desc = "";
	public String branch_enquiry_email_enable = "";
	public String branch_enquiry_email_format = "";
	public String branch_enquiry_email_exe_format = "";
	public String branch_enquiry_sms_enable = "";
	public String branch_enquiry_brochure_email_enable = "";

	public String branch_testdrive_email_enable = "";
	public String branch_testdrive_email_format = "";
	public String branch_testdrive_email_exe_format = "";
	public String branch_testdrive_sms_enable = "";

	public String branch_quote_email_enable = "";
	public String branch_quote_email_format = "";
	public String branch_quote_email_exe_format = "";
	public String branch_quote_sms_enable = "";
	public String branch_so_email_enable = "";
	public String branch_so_email_format = "";
	public String branch_so_sms_enable = "";
	public String branch_so_email_exe_format = "";

	public String branch_so_delivered_email_enable = "";
	public String branch_so_delivered_sms_enable = "";

	public String branch_service_due_email_enable = "0";
	public String branch_service_due_sms_enable = "0";

	public String branch_service_appointment_email_enable = "0";
	public String branch_service_appointment_email_format = "";
	public String branch_service_appointment_sms_enable = "0";

	public String branch_preowned_email_enable = "";
	public String branch_preowned_sms_enable = "";

	public String branch_jc_new_email_enable = "0";
	public String branch_jc_new_email_format = "";
	public String branch_jc_new_sms_enable = "0";

	public String branch_jc_ready_email_enable = "0";
	public String branch_jc_ready_email_format = "";
	public String branch_jc_ready_sms_enable = "0";

	public String branch_jc_delivered_email_enable = "0";
	public String branch_jc_delivered_sms_enable = "0";

	public String branch_jc_estimate_email_enable = "0";
	public String branch_jc_estimate_sms_enable = "0";

	public String branch_jc_feedback_email_enable = "0";
	public String branch_jc_feedback_sms_enable = "0";

	public String branch_insur_new_email_enable = "0";
	public String branch_insur_new_email_format = "";
	public String branch_insur_new_email_sub = "";
	public String branch_insur_new_sms_enable = "0";

	public String branch_insur_lost_email_enable = "0";
	public String branch_insur_lost_email_format = "";
	public String branch_insur_lost_email_sub = "";
	public String branch_insur_lost_sms_enable = "0";

	// public String branch_invoice_email_enable = "0";
	// public String branch_invoice_email_sub = "";
	// public String branch_invoice_email_format = "";
	// public String branch_invoice_sms_enable = "0";

	// public String branch_receipt_email_enable = "";
	// public String branch_receipt_email_sub = "";
	// public String branch_receipt_email_format = "";
	// public String branch_receipt_sms_enable = "";

	// public String branch_sms_enable = "";
	// public String branch_sms_url = "";

	public String branch_esc_enquiry = "";
	public String branch_esc_enquiry_followup = "";
	public String branch_esc_preowned_followup = "";
	public String branch_esc_preowned_eval_followup = "";

	public String branch_esc_crm_followup = "";
	public String branch_esc_servicepsf_followup = "";
	public String branch_esc_serviceveh_followup = "";

	// public String branch_receipt_sms_format = "";
	// public String branch_bill_email_enable = "";
	// public String branch_bill_email_sub = "";
	// public String branch_bill_email_format = "";
	// public String branch_bill_sms_enable = "";
	// public String branch_bill_sms_format = "";
	// public String branch_balance_due_email_enable = "";
	// public String branch_balance_due_email_format = "";
	// public String branch_balance_due_sms_enable = "";
	// public String branch_balance_overdue_email_enable = "";
	// public String branch_balance_overdue_email_format = "";
	// public String branch_balance_overdue_sms_enable = "";
	// public int franchisee_count = 0;
	// public int active_franchiseecount = 0;
	// Email
	public String active = "";
	public String veh_crmemp_id = "0";
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						branch_name = "";
						branch_code = "";
						branch_invoice_name = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							branch_entry_id = CNumeric(GetSession("emp_id", request));
							branch_entry_date = ToLongDate(kknow());
							branch_modified_date = "";
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("branch-list.jsp?branch_id=" + branch_id + "&msg=Branch Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}

				if ("yes".equals(update)) {
					active = ExecuteQuery("SELECT branch_active FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + branch_id + "");
					if (!"yes".equals(updateB) && !"Delete Branch".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Branch".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							// branch_entry_id = CNumeric(GetSession("emp_id",
							// request));
							// branch_entry_date = ToLongDate(kknow());
							branch_modified_id = CNumeric(GetSession("emp_id", request));
							branch_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("branch-list.jsp?branch_id=" + branch_id + "&msg=Branch Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Branch".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("branch-list.jsp?msg=Branch Deleted Successfully!"));
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
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		branch_invoice_name = PadQuotes(request.getParameter("txt_branch_invoice_name"));
		branch_code = PadQuotes(request.getParameter("txt_branch_code"));
		branch_tin = PadQuotes(request.getParameter("txt_branch_tin"));
		branch_cin = PadQuotes(request.getParameter("txt_branch_cin"));
		branch_gst = PadQuotes(request.getParameter("txt_branch_gst"));
		branch_vat = PadQuotes(request.getParameter("txt_branch_vat"));
		branch_cst = PadQuotes(request.getParameter("txt_branch_cst"));
		branch_pan = PadQuotes(request.getParameter("txt_branch_pan"));
		branch_quote_prefix = PadQuotes(request.getParameter("txt_branch_quote_prefix"));
		branch_so_prefix = PadQuotes(request.getParameter("txt_branch_so_prefix"));
		branch_invoice_prefix = PadQuotes(request.getParameter("txt_branch_invoice_prefix"));
		branch_receipt_prefix = PadQuotes(request.getParameter("txt_branch_receipt_prefix"));
		branch_bill_prefix = PadQuotes(request.getParameter("txt_branch_bill_prefix"));
		branch_jc_prefix = PadQuotes(request.getParameter("txt_branch_jc_prefix"));
		branch_region_id = CNumeric(PadQuotes(request.getParameter("dr_branch_region_id")));
		branch_zone_id = CNumeric(PadQuotes(request.getParameter("dr_branch_zone_id")));
		branch_branchtype_id = CNumeric(PadQuotes(request.getParameter("dr_branch_branchtype_id")));
		branch_franchisee_id = CNumeric(PadQuotes(request.getParameter("dr_franchisee_id")));
		branch_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
		branch_rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_branch_rateclass_id")));
		branch_phone1 = PadQuotes(request.getParameter("txt_branch_phone1"));
		branch_phone2 = PadQuotes(request.getParameter("txt_branch_phone2"));
		branch_mobile1 = PadQuotes(request.getParameter("txt_branch_mobile1"));
		branch_mobile2 = PadQuotes(request.getParameter("txt_branch_mobile2"));
		branch_email1 = PadQuotes(request.getParameter("txt_branch_email1"));
		branch_email2 = PadQuotes(request.getParameter("txt_branch_email2"));
		branch_sales_email = PadQuotes(request.getParameter("txt_branch_sales_email"));

		branch_servicebooking_email = PadQuotes(request.getParameter("txt_branch_servicebooking_email"));
		branch_ticket_email = PadQuotes(request.getParameter("txt_branch_ticket_email"));
		branch_crm_email = PadQuotes(request.getParameter("txt_branch_crm_email"));
		branch_socin_email = PadQuotes(request.getParameter("txt_branch_socin_email"));
		branch_din_email = PadQuotes(request.getParameter("txt_branch_din_email"));
		branch_jcpsf_email = PadQuotes(request.getParameter("txt_branch_jcpsf_email"));
		branch_sales_mobile = PadQuotes(request.getParameter("txt_branch_sales_mobile"));
		branch_crm_mobile = PadQuotes(request.getParameter("txt_branch_crm_mobile"));
		branch_socin_mobile = PadQuotes(request.getParameter("txt_branch_socin_mobile"));
		branch_din_mobile = PadQuotes(request.getParameter("txt_branch_din_mobile"));
		branch_jcpsf_mobile = PadQuotes(request.getParameter("txt_branch_jcpsf_mobile"));
		branch_add = PadQuotes(request.getParameter("txt_branch_add"));
		// state_id = PadQuotes(request.getParameter("dr_state_id"));

		branch_city_id = CNumeric(PadQuotes(request.getParameter("maincity")));

		branch_pin = PadQuotes(request.getParameter("txt_branch_pin"));
		branch_notes = PadQuotes(request.getParameter("txt_branch_notes"));
		branch_active = PadQuotes(request.getParameter("ch_branch_active"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		branch_entry_by = PadQuotes(request.getParameter("branch_entry_by"));
		branch_modified_by = PadQuotes(request.getParameter("branch_modified_by"));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		so_desc = PadQuotes(request.getParameter("txt_so_desc"));
		branch_invoice_terms = PadQuotes(request.getParameter("branch_invoice_terms"));
		branch_bill_terms = PadQuotes(request.getParameter("branch_bill_terms"));
		so_terms = PadQuotes(request.getParameter("txt_so_terms"));
		invoice_desc = PadQuotes(request.getParameter("txt_invoice_desc"));
		invoice_terms = PadQuotes(request.getParameter("txt_invoice_terms"));
		receipt_desc = PadQuotes(request.getParameter("txt_receipt_desc"));
		branch_enquiry_email_enable = PadQuotes(request.getParameter("chk_branch_enquiry_email_enable"));
		branch_enquiry_sms_enable = PadQuotes(request.getParameter("chk_branch_enquiry_sms_enable"));

		branch_enquiry_brochure_email_enable = PadQuotes(request.getParameter("chk_branch_enquiry_brochure_email_enable"));
		// branch_enquiry_brochure_email_format =
		// PadQuotes(request.getParameter("chk_branch_lead_sms_enable"));
		branch_testdrive_email_enable = PadQuotes(request.getParameter("chk_branch_testdrive_email_enable"));
		branch_testdrive_sms_enable = PadQuotes(request.getParameter("chk_branch_testdrive_sms_enable"));
		branch_quote_email_enable = PadQuotes(request.getParameter("chk_branch_quote_email_enable"));
		branch_quote_sms_enable = PadQuotes(request.getParameter("chk_branch_quote_sms_enable"));
		branch_so_email_enable = PadQuotes(request.getParameter("chk_branch_so_email_enable"));
		branch_so_sms_enable = PadQuotes(request.getParameter("chk_branch_so_sms_enable"));

		branch_so_delivered_email_enable = PadQuotes(request.getParameter("chk_branch_so_delivered_email_enable"));
		branch_so_delivered_sms_enable = PadQuotes(request.getParameter("chk_branch_so_delivered_sms_enable"));

		branch_service_due_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_service_due_email_enable")));
		branch_service_due_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_service_due_sms_enable")));

		branch_service_appointment_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_service_appointment_email_enable")));
		branch_service_appointment_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_service_appointment_sms_enable")));

		branch_preowned_email_enable = PadQuotes(request.getParameter("chk_branch_preowned_email_enable"));
		branch_preowned_sms_enable = PadQuotes(request.getParameter("chk_branch_preowned_sms_enable"));

		branch_jc_new_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_new_email_enable")));
		branch_jc_new_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_new_sms_enable")));

		branch_jc_ready_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_ready_email_enable")));
		branch_jc_ready_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_ready_sms_enable")));

		branch_jc_delivered_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_delivered_email_enable")));
		branch_jc_delivered_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_delivered_sms_enable")));

		branch_jc_estimate_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_estimate_email_enable")));
		branch_jc_estimate_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_estimate_sms_enable")));

		branch_jc_feedback_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_feedback_email_enable")));
		branch_jc_feedback_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_jc_feedback_sms_enable")));

		branch_insur_new_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_insur_new_email_enable")));
		branch_insur_new_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_insur_new_sms_enable")));
		branch_insur_lost_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_insur_lost_email_enable")));
		branch_insur_lost_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_insur_lost_sms_enable")));

		// branch_invoice_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_invoice_email_enable")));
		// branch_invoice_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_invoice_sms_enable")));

		// branch_receipt_email_enable = PadQuotes(request.getParameter("chk_branch_receipt_email_enable"));
		// branch_receipt_sms_enable = PadQuotes(request.getParameter("chk_branch_receipt_sms_enable"));

		// branch_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_branch_sms_enable")));
		// branch_sms_url = PadQuotes(request.getParameter("txt_branch_sms_url"));

		branch_esc_enquiry = PadQuotes(request.getParameter("ch_branch_esc_enquiry"));
		branch_esc_enquiry_followup = PadQuotes(request.getParameter("ch_branch_esc_enquiry_followup"));
		branch_esc_preowned_followup = PadQuotes(request.getParameter("ch_branch_esc_preowned_followup"));
		branch_esc_preowned_eval_followup = PadQuotes(request.getParameter("ch_branch_esc_preowned_eval_followup"));
		branch_esc_crm_followup = PadQuotes(request.getParameter("ch_branch_esc_crm_followup"));
		branch_esc_servicepsf_followup = CheckBoxValue(PadQuotes(request.getParameter("ch_branch_esc_servicepsf_followup")));
		branch_esc_serviceveh_followup = CheckBoxValue(PadQuotes(request.getParameter("ch_branch_esc_serviceveh_followup")));
		// branch_bill_email_enable =
		// PadQuotes(request.getParameter("chk_branch_bill_email_enable"));
		// branch_bill_sms_enable =
		// PadQuotes(request.getParameter("chk_branch_bill_sms_enable"));
		// branch_balance_due_email_enable =
		// PadQuotes(request.getParameter("chk_branch_balance_due_email_enable"));
		// branch_balance_due_sms_enable =
		// PadQuotes(request.getParameter("chk_branch_balance_due_sms_enable"));
		// branch_balance_overdue_email_enable =
		// PadQuotes(request.getParameter("chk_branch_balance_overdue_email_enable"));
		// branch_balance_overdue_sms_enable =
		// PadQuotes(request.getParameter("chk_branch_balance_overdue_sms_enable"));
		// SOP("branch_esc_enquiry--------" + branch_esc_enquiry);
		// SOP("branch_esc_enquiry_followup---------" +
		// branch_esc_enquiry_followup);
		// SOP("branch_esc_crm_followup--------------" +
		// branch_esc_crm_followup);
		if (branch_active.equals("on")) {
			branch_active = "1";
		} else {
			branch_active = "0";
		}

		if (branch_enquiry_email_enable.equals("on")) {
			branch_enquiry_email_enable = "1";
		} else {
			branch_enquiry_email_enable = "0";
		}
		if (branch_enquiry_sms_enable.equals("on")) {
			branch_enquiry_sms_enable = "1";
		} else {
			branch_enquiry_sms_enable = "0";
		}
		if (branch_enquiry_brochure_email_enable.equals("on")) {
			branch_enquiry_brochure_email_enable = "1";
		} else {
			branch_enquiry_brochure_email_enable = "0";
		}

		if (branch_testdrive_email_enable.equals("on")) {
			branch_testdrive_email_enable = "1";
		} else {
			branch_testdrive_email_enable = "0";
		}
		if (branch_testdrive_sms_enable.equals("on")) {
			branch_testdrive_sms_enable = "1";
		} else {
			branch_testdrive_sms_enable = "0";
		}

		if (branch_quote_email_enable.equals("on")) {
			branch_quote_email_enable = "1";
		} else {
			branch_quote_email_enable = "0";
		}
		if (branch_quote_sms_enable.equals("on")) {
			branch_quote_sms_enable = "1";
		} else {
			branch_quote_sms_enable = "0";
		}

		if (branch_so_email_enable.equals("on")) {
			branch_so_email_enable = "1";
		} else {
			branch_so_email_enable = "0";
		}
		if (branch_so_delivered_email_enable.equals("on")) {
			branch_so_delivered_email_enable = "1";
		} else {
			branch_so_delivered_email_enable = "0";
		}
		if (branch_so_sms_enable.equals("on")) {
			branch_so_sms_enable = "1";
		} else {
			branch_so_sms_enable = "0";
		}
		if (branch_so_delivered_sms_enable.equals("on")) {
			branch_so_delivered_sms_enable = "1";
		} else {
			branch_so_delivered_sms_enable = "0";
		}
		if (branch_preowned_email_enable.equals("on")) {
			branch_preowned_email_enable = "1";
		} else {
			branch_preowned_email_enable = "0";
		}

		if (branch_preowned_sms_enable.equals("on")) {
			branch_preowned_sms_enable = "1";
		} else {
			branch_preowned_sms_enable = "0";
		}
		// if (branch_receipt_email_enable.equals("on")) {
		// branch_receipt_email_enable = "1";
		// } else {
		// branch_receipt_email_enable = "0";
		// }
		// if (branch_receipt_sms_enable.equals("on")) {
		// branch_receipt_sms_enable = "1";
		// } else {
		// branch_receipt_sms_enable = "0";
		// }
		if (branch_esc_enquiry.equals("on"))
		{
			branch_esc_enquiry = "1";
		} else {
			branch_esc_enquiry = "0";
		}
		if (branch_esc_enquiry_followup.equals("on"))
		{
			branch_esc_enquiry_followup = "1";
		} else {
			branch_esc_enquiry_followup = "0";
		}

		if (branch_esc_preowned_followup.equals("on")) {
			branch_esc_preowned_followup = "1";
		} else {
			branch_esc_preowned_followup = "0";
		}
		if (branch_esc_preowned_eval_followup.equals("on")) {
			branch_esc_preowned_eval_followup = "1";
		} else {
			branch_esc_preowned_eval_followup = "0";
		}

		if (branch_esc_crm_followup.equals("on"))
		{
			branch_esc_crm_followup = "1";
		} else {
			branch_esc_crm_followup = "0";
		}

		veh_crmemp_id = CNumeric(PadQuotes(request.getParameter("dr_vehfollowup_crmexe_id")));

		// if(branch_esc_servicepsf_followup.equals("on"))
		// {
		// branch_esc_servicepsf_followup = "1";
		// } else {
		// branch_esc_servicepsf_followup = "0";
		// }
		// if (branch_bill_email_enable.equals("on")) {
		// branch_bill_email_enable = "1";
		// } else {
		// branch_bill_email_enable = "0";
		// }
		// if (branch_bill_sms_enable.equals("on")) {
		// branch_bill_sms_enable = "1";
		// } else {
		// branch_bill_sms_enable = "0";
		// }
		// if (branch_balance_due_email_enable.equals("on")) {
		// branch_balance_due_email_enable = "1";
		// } else {
		// branch_balance_due_email_enable = "0";
		// }
		// if (branch_balance_due_sms_enable.equals("on")) {
		// branch_balance_due_sms_enable = "1";
		// } else {
		// branch_balance_due_sms_enable = "0";
		// }
		// if (branch_balance_overdue_email_enable.equals("on")) {
		// branch_balance_overdue_email_enable = "1";
		// } else {
		// branch_balance_overdue_email_enable = "0";
		// }
		// if (branch_balance_overdue_sms_enable.equals("on")) {
		// branch_balance_overdue_sms_enable = "1";
		// } else {
		// branch_balance_overdue_sms_enable = "0";
		// }

	}

	protected void CheckForm() {
		msg = "";
		Msg1 = "";
		try {

			PopulateConfigDetails();
			if ("Add Branch".equals(addB)) {
				if (active_branchcount >= branch_count) {
					msg = msg + "<br>Maximum Branch Count Reached!";
				}
			}
			if ("Update Branch".equals(updateB)) {
				if (active.equals("0") && branch_active.equals("1") && branch_count <= active_branchcount) {
					msg = msg + "<br>Maximum Branch Count Reached!";
				}
			}
			if (branch_name.equals("")) {
				msg = msg + "<br>Enter Branch Name!";
			} else// similar Name found
			{
				StrSql = "SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_name = '" + branch_name + "'"
						+ " AND branch_id != " + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Name Found! ";
				}
			}

			if (!branch_name.equals("") && branch_name.length() < 3) {
				msg = msg + "<br>Enter Minimum of 3 Characters for Branch Name!";
			}
			if (branch_invoice_name.equals("")) {
				msg = msg + "<br>Enter Name on Invoice!";
			}
			if (branch_code.equals("")) {
				msg = msg + "<br>Enter Branch Code!";
			} else// similar Name found
			{
				StrSql = "SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_code = '" + branch_code + "'"
						+ " AND branch_id != " + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Code Found! ";
				}
			}
			if (!branch_cin.equals("") && !branch_cin.matches("[A-Z]{1}\\d{5}[A-Z]{2}\\d{4}[A-Z]{3}\\d{6}")) {
				msg = msg + "<br>Enter Valid CIN!";
			}
			if (!branch_gst.equals("") && !branch_gst.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
				msg = msg + "<br>Enter Valid GSTIN!";
			}

			// if (branch_vat.equals("")) {
			// msg = msg + "<br>Enter Vat!";
			// }
			if (branch_cst.equals("")) {
				msg = msg + "<br>Enter Cst!";
			}
			if (branch_pan.equals("")) {
				msg = msg + "<br>Enter PAN!";
			}
			else {
				if (branch_pan.length() < 10) {
					msg += "<br>PAN should be of 10 digits!";
				}
			}

			if (branch_quote_prefix.equals("")) {
				msg = msg + "<br>Enter Quote Prefix!";
			} else {
				StrSql = "SELECT branch_quote_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_quote_prefix = '" + branch_quote_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Quote Prefix Found! ";
				}
			}

			if (branch_so_prefix.equals("")) {
				msg = msg + "<br>Enter SO Prefix!";
			} else {
				StrSql = "SELECT branch_so_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_so_prefix = '" + branch_so_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch SO Prefix Found! ";
				}
			}

			if (branch_invoice_prefix.equals("")) {
				msg = msg + "<br>Enter Invoice Prefix!";
			} else {
				StrSql = "SELECT branch_invoice_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_invoice_prefix = '" + branch_invoice_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Invoice Prefix Found! ";
				}
			}

			if (branch_receipt_prefix.equals("")) {
				msg = msg + "<br>Enter Receipt Prefix!";
			} else {
				StrSql = "SELECT branch_receipt_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_receipt_prefix = '" + branch_receipt_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Receipt Prefix Found! ";
				}
			}

			if (branch_bill_prefix.equals("")) {
				msg = msg + "<br>Enter Bill Prefix!";
			} else {
				StrSql = "SELECT branch_bill_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_bill_prefix = '" + branch_bill_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Bill Prefix Found! ";
				}
			}

			if (branch_jc_prefix.equals("")) {
				msg = msg + "<br>Enter Job Card Prefix!";
			} else {
				StrSql = "SELECT branch_jc_prefix FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_jc_prefix = '" + branch_jc_prefix + "'"
						+ " AND branch_id!=" + branch_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Branch Job Card Prefix Found! ";
				}
			}

			msg = msg + Msg1;
			if (branch_region_id.equals("0")) {
				msg = msg + "<br>Select Region!";
			}
			if (branch_zone_id.equals("0")) {
				msg = msg + "<br>Select Zone!";
			}
			if (branch_branchtype_id.equals("0")) {
				msg = msg + "<br>Select Type!";
			}
			if (branch_franchisee_id.equals("0")) {
				msg = msg + "<br>Select Franchisee!";
			}
			if (branch_brand_id.equals("0"))
			{
				msg = msg + "<br>Select Brand!";
			}
			if (branch_rateclass_id.equals("0")) {
				msg = msg + "<br>Select Class!";
			}

			if (branch_mobile1.equals("") && branch_phone1.equals("")) {
				msg = msg + "<br>Enter Either Branch Mobile 1 or Phone 1!";
			} else if (!branch_mobile1.equals("")) {
				if (!IsValidMobileNo11(branch_mobile1)) {
					msg = msg + "<br>Enter Valid Branch Mobile 1!";
				}
			}

			if (!branch_mobile2.equals("")) {
				if (!IsValidMobileNo11(branch_mobile2)) {
					msg = msg + "<br>Enter Valid Branch Mobile 2!";
				}
			}
			if (!branch_phone1.equals("")) {
				if (!IsValidPhoneNo11(branch_phone1)) {
					msg = msg + "<br>Enter Valid Branch Phone!";
				}
			}
			if (!branch_phone2.equals("")) {
				if (!IsValidPhoneNo11(branch_phone2)) {
					msg = msg + "<br>Enter Valid Branch Phone 2!";
				}
			}
			if (branch_email1.equals("")) {
				msg = msg + "<br>Enter Email1!";
			}
			if (!branch_email1.equals("") && !IsValidEmail(branch_email1)) {
				msg = msg + "<br>Enter Valid Email 1!";
			}
			if (!branch_email2.equals("") && !IsValidEmail(branch_email2)) {
				msg = msg + "<br>Enter Valid Email 2!";
			}
			// if (branch_sales_email.equals("")) {
			// msg = msg + "<br>Enter Sales Email!";
			// } else if (!IsValidEmail(branch_sales_email)) {
			// msg = msg + "<br>Enter Valid Sales Email!";
			// }

			String ccmsg = "";
			if (!branch_servicebooking_email.equals("")) {
				ccmsg = "";
				service_email = branch_servicebooking_email.split(",");
				for (int i = 0; i < service_email.length; i++) {
					service_email[i] = service_email[i].replace(" ", "");
					if (!IsValidEmail(service_email[i])) {
						ccmsg = "<br>Enter Valid Service Email!";
					}
				}
				msg += CheckDuplicateEmail(service_email, "Service");
				msg = msg + ccmsg;
			}

			if (!branch_sales_email.equals("")) {
				ccmsg = "";
				sales_email = branch_sales_email.split(",");
				for (int i = 0; i < sales_email.length; i++) {
					sales_email[i] = sales_email[i].replace(" ", "");
					if (!IsValidEmail(sales_email[i])) {
						ccmsg = "<br>Enter Valid Sales Email!";
					}
				}
				msg += CheckDuplicateEmail(sales_email, "Sales");

				msg = msg + ccmsg;
			}
			// if (!branch_sales_mobile.equals("") && !IsValidMobileNo11(branch_sales_mobile)) {
			// msg = msg + "<br>Sales Mobile is Invalid!";
			// }
			if (!branch_sales_mobile.equals("")) {
				branch_sales_mobile = branch_sales_mobile.substring(0, 499);
				ccmsg = "";
				// SOP("ccmsg==22===" + ccmsg);
				sales_mobile = branch_sales_mobile.split(",");
				for (int i = 0; i < sales_mobile.length; i++) {
					sales_mobile[i] = sales_mobile[i].replace(" ", "");
					// SOP("sales_mobile---------" + sales_mobile[i]);
					if (!IsValidMobileNo11(sales_mobile[i])) {
						// SOP("coming...." + sales_mobile[i]);
						ccmsg = "<br>Sales Mobile is Invalid!";
					}
				}
				msg = msg + ccmsg;
				// SOP("ccmsg==11===" + ccmsg);
				// SOP("msg==11===" + msg);
			}
			// SOP("msg--------final-----------" + msg);
			// if (!branch_din_mobile.equals("")) {
			// din_mobile = branch_din_mobile.split(",");
			// for (int i = 0; i < din_mobile.length; i++) {
			// din_mobile[i] = din_mobile[i].replace(" ", "");
			// if (!IsValidMobileNo11(din_mobile[i])) {
			// ccmsg = "<br>DIN Mobile is Invalid!";
			// }
			// }
			// msg = msg + ccmsg;
			// }

			if (!branch_ticket_email.equals("")) {
				ccmsg = "";
				ticket_email = branch_ticket_email.split(",");
				for (int i = 0; i < ticket_email.length; i++) {
					ticket_email[i] = ticket_email[i].replace(" ", "");
					if (!IsValidEmail(ticket_email[i])) {
						ccmsg = "<br>Enter Valid Ticket Email!";
					}
				}
				msg += CheckDuplicateEmail(ticket_email, "Ticket");

				msg = msg + ccmsg;
			}
			if (!branch_crm_email.equals("")) {
				ccmsg = "";
				crm_email = branch_crm_email.split(",");
				for (int i = 0; i < crm_email.length; i++) {
					crm_email[i] = crm_email[i].replace(" ", "");
					if (!IsValidEmail(crm_email[i])) {
						ccmsg = "<br>Enter Valid CRM Email!";
					}
				}
				msg += CheckDuplicateEmail(crm_email, "CRM");

				msg = msg + ccmsg;
			}

			if (!branch_crm_mobile.equals("") && !IsValidMobileNo11(branch_crm_mobile)) {
				msg = msg + "<br>CRM Mobile is Invalid!";
			}

			if (!branch_socin_email.equals("")) {
				ccmsg = "";
				socin_email = branch_socin_email.split(",");
				for (int i = 0; i < socin_email.length; i++) {
					socin_email[i] = socin_email[i].replace(" ", "");
					if (!IsValidEmail(socin_email[i])) {
						ccmsg = "<br>Enter Valid CIN Email!";
					}
				}
				msg += CheckDuplicateEmail(socin_email, "SO CIN");

				msg = msg + ccmsg;
			}

			if (!branch_socin_mobile.equals("")) {
				ccmsg = "";
				socin_mobile = branch_socin_mobile.split(",");
				for (int i = 0; i < socin_mobile.length; i++) {
					socin_mobile[i] = socin_mobile[i].replace(" ", "");
					if (!IsValidMobileNo11(socin_mobile[i])) {
						ccmsg = "<br>CIN Mobile is Invalid!";
					}
				}
				msg = msg + ccmsg;
			}

			if (!branch_din_email.equals("")) {
				ccmsg = "";
				din_email = branch_din_email.split(",");
				for (int i = 0; i < din_email.length; i++) {
					din_email[i] = din_email[i].replace(" ", "");
					if (!IsValidEmail(din_email[i])) {
						ccmsg = "<br>Enter Valid DIN Email!";
					}
				}
				msg += CheckDuplicateEmail(din_email, "DIN");

				msg = msg + ccmsg;
			}

			if (!branch_din_mobile.equals("")) {
				ccmsg = "";
				din_mobile = branch_din_mobile.split(",");
				for (int i = 0; i < din_mobile.length; i++) {
					din_mobile[i] = din_mobile[i].replace(" ", "");
					if (!IsValidMobileNo11(din_mobile[i])) {
						ccmsg = "<br>DIN Mobile is Invalid!";
					}
				}
				msg = msg + ccmsg;
			}
			if (!branch_jcpsf_email.equals("")) {
				ccmsg = "";
				jcpsf_email = branch_jcpsf_email.split(",");
				for (int i = 0; i < jcpsf_email.length; i++) {
					jcpsf_email[i] = jcpsf_email[i].replace(" ", "");
					if (!IsValidEmail(jcpsf_email[i])) {
						ccmsg = "<br>Enter Valid JCPSF Email!";
					}
				}
				msg += CheckDuplicateEmail(jcpsf_email, "JCPSF");

				msg = msg + ccmsg;
			}

			if (!branch_jcpsf_mobile.equals("") && !IsValidMobileNo11(branch_jcpsf_mobile)) {
				msg = msg + "<br>JCPSF Mobile is Invalid!";
			}
			// if (!branch_sms_url.equals(""))
			// {
			// branch_sms_url = branch_sms_url.substring(0, 999);
			// }
			// if (branch_sms_url.length() > 1000) {
			// branch_sms_url = branch_sms_url.substring(0, 999);
			// }

			if (branch_add.equals("")) {
				msg = msg + "<br>Enter Address!";
			}
			if (branch_city_id.equals("0")) {
				msg = msg + "<br>Select City!";
			}
			if (branch_pin.equals("")) {
				msg = msg + "<br>Enter Pin Code!";
			} else if (!branch_pin.equals("") && !isNumeric(branch_pin)) {
				msg = msg + "<br>Pin Code: Enter Numeric!";
			}
			// SOP("msg--------final-----1------" + msg);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {

				branch_id = ExecuteQuery("SELECT COALESCE(MAX(branch_id), 0)+1 AS branch_id FROM " + compdb(comp_id) + "axela_branch");

				branch_id = "" + CheckCurrentId(Integer.parseInt(branch_id));
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_branch"
						+ " (branch_id,"
						+ " branch_name,"
						+ " branch_invoice_name,"
						+ " branch_cin,"
						+ " branch_code,"
						+ " branch_tin,"
						+ " branch_gst,"
						+ " branch_vat,"
						+ " branch_cst,"
						+ " branch_pan,"
						+ " branch_quote_prefix,"
						+ " branch_so_prefix,"
						+ " branch_invoice_prefix,"
						+ " branch_receipt_prefix,"
						+ "	branch_bill_prefix,"
						+ "	branch_jc_prefix,"
						+ " branch_region_id,"
						+ " branch_zone_id,"
						+ " branch_branchtype_id,"
						+ " branch_franchisee_id,"
						+ " branch_brand_id,"
						+ " branch_rateclass_id,"
						+ " branch_veh_crm_emp_id,"
						+ " branch_phone1,"
						+ " branch_phone2,"
						+ " branch_mobile1,"
						+ " branch_mobile2,"
						+ " branch_email1,"
						+ " branch_email2,"
						+ " branch_sales_email,"
						+ " branch_sales_mobile,"
						+ " branch_servicebooking_email,"
						+ " branch_ticket_email,"
						+ " branch_crm_email,"
						+ " branch_crm_mobile,"
						+ " branch_socin_email,"
						+ " branch_socin_mobile,"
						+ " branch_din_email,"
						+ " branch_din_mobile,"
						+ " branch_jcpsf_email,"
						+ " branch_jcpsf_mobile,"
						+ " branch_add,"
						+ " branch_city_id,"
						+ " branch_pin,"
						+ " branch_quote_desc,"
						+ " branch_quote_terms,"
						+ " branch_so_desc,"
						+ " branch_invoice_terms,"
						+ " branch_bill_terms,"
						+ " branch_so_terms,"
						+ " branch_enquiry_email_sub,"
						+ " branch_enquiry_email_exe_sub,"
						+ " branch_enquiry_email_format,"
						+ " branch_enquiry_email_exe_format,"
						+ " branch_enquiry_sms_format,"
						+ " branch_enquiry_sms_exe_format,"
						+ " branch_enquiry_brochure_email_sub,"
						+ " branch_enquiry_brochure_email_format,"
						+ " branch_testdrive_email_sub,"
						+ " branch_testdrive_email_exe_sub,"
						+ " branch_testdrive_email_format,"
						+ " branch_testdrive_email_exe_format,"
						+ " branch_testdrive_sms_format,"
						+ " branch_quote_email_sub,"
						+ " branch_quote_email_exe_sub,"
						+ " branch_quote_email_format,"
						+ " branch_quote_email_exe_format,"
						+ " branch_quote_sms_format,"
						+ " branch_so_email_format,"
						+ " branch_so_email_exe_format,"
						+ " branch_so_delivered_email_format,"
						+ " branch_preowned_email_format,"
						+ " branch_preowned_email_exe_format,"
						+ " branch_service_due_email_format,"
						+ " branch_service_appointment_email_format,"
						+ " branch_jc_new_email_format,"
						+ " branch_jc_ready_email_format,"
						+ " branch_jc_delivered_email_format,"
						+ " branch_jc_estimate_email_format,"
						+ " branch_jc_feedback_email_format,"
						+ " branch_insur_new_email_format,"
						+ " branch_insur_new_email_sub,"
						+ " branch_insur_lost_email_format,"
						+ " branch_insur_lost_email_sub,"
						// + " branch_invoice_email_format,"
						// + " branch_invoice_email_sub,"
						// + " branch_receipt_email_format,"
						// + " branch_receipt_email_sub,"
						// + " branch_bill_email_format,"
						// + " branch_bill_email_sub,"
						// + " branch_balance_due_email_format,"
						// + " branch_balance_overdue_email_format,"
						+ " branch_notes,"
						+ " branch_active,"
						// + " branch_sms_enable,"
						// + " branch_sms_url,"
						+ " branch_esc_enquiry,"
						+ " branch_esc_enquiry_followup,"
						+ " branch_esc_preowned_followup,"
						+ " branch_esc_preowned_eval_followup,"
						+ " branch_esc_crm_followup,"
						+ " branch_esc_servicepsf_followup,"
						+ " branch_esc_serviceveh_followup,"
						+ " branch_entry_id,"
						+ " branch_so_email_sub,"
						+ " branch_so_delivered_email_sub,"
						+ " branch_so_sms_format,"
						+ " branch_so_delivered_sms_format,"
						+ " branch_so_sms_exe_format,"
						+ " branch_service_due_email_sub,"
						+ " branch_service_due_sms_format,"
						+ " branch_service_appointment_email_sub,"
						+ " branch_service_appointment_sms_format,"
						+ " branch_jc_new_email_sub,"
						+ " branch_jc_new_sms_format,"
						+ " branch_jc_ready_email_sub,"
						+ " branch_jc_ready_sms_format,"
						+ " branch_jc_delivered_email_sub,"
						+ " branch_jc_delivered_sms_format,"
						+ " branch_jc_estimate_email_sub,"
						+ " branch_jc_estimate_sms_format,"
						+ " branch_jc_feedback_email_sub,"
						+ " branch_jc_feedback_sms_format,"
						// + " branch_invoice_sms_format,"
						// + " branch_receipt_sms_format,"
						+ " branch_entry_date)"
						+ " VALUES"
						+ " (" + branch_id + ","
						+ " '" + branch_name + "',"
						+ " '" + branch_invoice_name + "',"
						+ " '" + branch_code.toUpperCase() + "',"
						+ " '" + branch_tin + "',"
						+ " '" + branch_cin + "',"
						+ " '" + branch_gst + "',"
						+ " '" + branch_vat + "',"
						+ " '" + branch_cst + " ',"
						+ " '" + branch_pan + "',"
						+ " '" + branch_quote_prefix + "',"
						+ " '" + branch_so_prefix + "',"
						+ " '" + branch_invoice_prefix + "',"
						+ " '" + branch_receipt_prefix + "',"
						+ " '" + branch_bill_prefix + "',"
						+ " '" + branch_jc_prefix + "',"
						+ " " + branch_region_id + ","
						+ " " + branch_zone_id + ","
						+ " " + branch_branchtype_id + ","
						+ " " + branch_franchisee_id + ","
						+ " " + branch_brand_id + ","
						+ " " + branch_rateclass_id + ","
						+ " " + veh_crmemp_id + ","
						+ " '" + branch_phone1 + "',"
						+ " '" + branch_phone2 + "',"
						+ " '" + branch_mobile1 + "',"
						+ " '" + branch_mobile2 + "',"
						+ " '" + branch_email1 + "',"
						+ " '" + branch_email2 + "',"
						+ " '" + branch_sales_email + "',"
						+ " '" + branch_sales_mobile + "',"
						+ " '" + branch_servicebooking_email + "',"
						+ " '" + branch_ticket_email + "',"
						+ " '" + branch_crm_email + "',"
						+ " '" + branch_crm_mobile + "',"
						+ " '" + branch_socin_email + "',"
						+ " '" + branch_socin_mobile + "',"
						+ " '" + branch_din_email + "',"
						+ " '" + branch_din_mobile + "',"
						+ " '" + branch_jcpsf_email + "',"
						+ " '" + branch_jcpsf_mobile + "',"
						+ " '" + branch_add + "',"
						+ " " + branch_city_id + ","
						+ " '" + branch_pin + "',"
						+ " '" + quote_desc + "',"
						+ " '" + quote_terms + "',"
						// + " '',"
						+ " '" + so_desc + "',"
						+ " '" + branch_invoice_terms + "',"
						+ " '" + branch_bill_terms + "',"
						+ " '" + so_terms + "',"
						+ " '',"
						+ " '',"
						+ " '" + branch_enquiry_email_format + "',"
						+ " '" + branch_enquiry_email_exe_format + "',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '" + branch_testdrive_email_format + "',"
						+ " '" + branch_testdrive_email_exe_format + "',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '" + branch_quote_email_format + "',"
						+ " '" + branch_quote_email_exe_format + "',"
						+ " '',"
						+ " '" + branch_so_email_format + "',"
						+ " '" + branch_so_email_exe_format + "',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '" + branch_insur_new_email_format + "',"
						+ " '" + branch_insur_new_email_sub + "',"
						+ " '" + branch_insur_lost_email_format + "',"
						+ " '" + branch_insur_lost_email_sub + "',"
						// + " '" + branch_invoice_email_format + "',"
						// + " '" + branch_invoice_email_sub + "',"
						// + " '" + branch_receipt_email_format + "',"
						// + " '" + branch_receipt_email_sub + "',"
						// + " '" + branch_bill_email_format + "',"
						// + " '" + branch_bill_email_sub + "',"
						// + " '" + branch_balance_due_email_format + "',"
						// + " '" + branch_balance_overdue_email_format + "',"
						+ " '" + branch_notes + "',"
						+ " '" + branch_active + "',"
						// + " " + branch_sms_enable + ","
						// + " '" + branch_sms_url + "',"
						+ " " + branch_esc_enquiry + ","
						+ " " + branch_esc_enquiry_followup + ","
						+ " " + branch_esc_preowned_followup + ","
						+ " " + branch_esc_preowned_eval_followup + ","
						+ " " + branch_esc_crm_followup + ","
						+ " " + branch_esc_servicepsf_followup + ","
						+ " " + branch_esc_serviceveh_followup + ","
						+ " " + branch_entry_id + ","
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						+ " '',"
						// + " '',"
						// + " '',"
						+ " '" + ToLongDate(kknow()) + "')";
				// SOP("strsql==========Add Branch====" +
				// StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT branch_id, branch_name, branch_invoice_name, branch_code, branch_tin,"
					+ " branch_cin, branch_gst, branch_vat, branch_cst, branch_pan, branch_brand_id,"
					+ " branch_quote_prefix, branch_so_prefix, branch_invoice_prefix, branch_receipt_prefix,"
					+ "	branch_bill_prefix, branch_jc_prefix,"
					+ " branch_region_id, branch_branchtype_id,"
					+ " branch_zone_id, branch_franchisee_id, branch_rateclass_id, branch_veh_crm_emp_id, branch_phone1, branch_phone2, branch_mobile1,"
					+ " branch_mobile2, branch_email1, branch_email2, branch_sales_email, branch_sales_mobile, branch_servicebooking_email, branch_ticket_email,"
					+ " branch_crm_email, branch_crm_mobile,"
					+ " branch_socin_email, branch_socin_mobile,"
					+ " branch_din_email, branch_din_mobile,"
					+ " branch_jcpsf_email, branch_jcpsf_mobile,"
					+ " branch_add, state_id, city_id,"
					+ " branch_pin, branch_quote_desc, branch_quote_terms, branch_so_desc, branch_invoice_terms, branch_bill_terms, branch_so_terms,"
					+ " branch_notes, branch_active, branch_esc_enquiry, branch_esc_enquiry_followup, "
					+ " branch_esc_preowned_followup, branch_esc_preowned_eval_followup,"
					+ " branch_esc_crm_followup, branch_esc_servicepsf_followup, branch_esc_serviceveh_followup,"
					+ " branch_enquiry_email_enable, branch_enquiry_sms_enable, branch_enquiry_brochure_email_enable,"
					+ " branch_testdrive_email_enable, branch_testdrive_sms_enable,"
					+ " branch_quote_email_enable,branch_preowned_email_enable, branch_preowned_sms_enable,"
					+ " branch_quote_sms_enable,branch_so_email_enable,branch_so_sms_enable,"
					+ " branch_so_delivered_email_enable, branch_so_delivered_sms_enable,"
					+ " branch_service_due_email_enable, branch_service_due_sms_enable,"
					+ " branch_service_appointment_email_enable, branch_service_appointment_sms_enable,"
					+ " branch_jc_new_email_enable, branch_jc_new_sms_enable,"
					+ " branch_jc_ready_email_enable, branch_jc_ready_sms_enable,"
					+ " branch_jc_delivered_email_enable, branch_jc_delivered_sms_enable,"
					+ " branch_jc_estimate_email_enable, branch_jc_estimate_sms_enable,"
					+ " branch_jc_feedback_email_enable, branch_jc_feedback_sms_enable,"
					+ " branch_insur_new_email_enable, branch_insur_new_sms_enable,"
					+ " branch_insur_lost_email_enable, branch_insur_lost_sms_enable,"
					// + " branch_invoice_email_enable, branch_invoice_sms_enable,"
					// + " branch_receipt_email_enable, branch_receipt_sms_enable,"
					// + " branch_balance_due_email_enable,"
					// + "branch_balance_due_sms_enable,"
					// + " branch_balance_overdue_email_enable,"
					// + "branch_balance_overdue_sms_enable,"
					+ " branch_entry_date,"
					+ " branch_entry_id, branch_modified_id, branch_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN axela_brand ON brand_id = branch_brand_id "
					+ " WHERE branch_id = " + branch_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---PopulateFields-------" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_id = crs.getString("branch_id");
					branch_name = crs.getString("branch_name");
					branch_invoice_name = crs.getString("branch_invoice_name");
					branch_code = crs.getString("branch_code");
					branch_tin = crs.getString("branch_tin");
					branch_cin = crs.getString("branch_cin");
					branch_gst = crs.getString("branch_gst");
					branch_vat = crs.getString("branch_vat");
					branch_cst = crs.getString("branch_cst");
					branch_pan = crs.getString("branch_pan");
					branch_quote_prefix = crs.getString("branch_quote_prefix");
					branch_so_prefix = crs.getString("branch_so_prefix");
					branch_invoice_prefix = crs.getString("branch_invoice_prefix");
					branch_receipt_prefix = crs.getString("branch_receipt_prefix");
					branch_bill_prefix = crs.getString("branch_bill_prefix");
					branch_jc_prefix = crs.getString("branch_jc_prefix");
					branch_region_id = crs.getString("branch_region_id");
					branch_zone_id = crs.getString("branch_zone_id");
					branch_branchtype_id = crs.getString("branch_branchtype_id");
					branch_franchisee_id = crs.getString("branch_franchisee_id");
					branch_brand_id = crs.getString("branch_brand_id");
					branch_rateclass_id = crs.getString("branch_rateclass_id");
					veh_crmemp_id = crs.getString("branch_veh_crm_emp_id");
					branch_phone1 = crs.getString("branch_phone1");
					branch_phone2 = crs.getString("branch_phone2");
					branch_mobile1 = crs.getString("branch_mobile1");
					branch_mobile2 = crs.getString("branch_mobile2");
					branch_email1 = crs.getString("branch_email1");
					branch_email2 = crs.getString("branch_email2");
					branch_sales_email = crs.getString("branch_sales_email");
					branch_sales_mobile = crs.getString("branch_sales_mobile");
					branch_servicebooking_email = crs.getString("branch_servicebooking_email");
					branch_ticket_email = crs.getString("branch_ticket_email");
					branch_crm_email = crs.getString("branch_crm_email");
					branch_crm_mobile = crs.getString("branch_crm_mobile");
					branch_socin_email = crs.getString("branch_socin_email");
					branch_socin_mobile = crs.getString("branch_socin_mobile");
					branch_din_email = crs.getString("branch_din_email");
					branch_din_mobile = crs.getString("branch_din_mobile");
					branch_jcpsf_email = crs.getString("branch_jcpsf_email");
					branch_jcpsf_mobile = crs.getString("branch_jcpsf_mobile");
					branch_add = crs.getString("branch_add");
					// state_id = crs.getString("state_id");
					branch_city_id = crs.getString("city_id");
					// if (branch_city_id == null) {
					// branch_city_id = "0";
					// }
					branch_pin = crs.getString("branch_pin");
					quote_desc = crs.getString("branch_quote_desc");
					quote_terms = crs.getString("branch_quote_terms");
					so_desc = crs.getString("branch_so_desc");
					branch_invoice_terms = crs.getString("branch_invoice_terms");
					branch_bill_terms = crs.getString("branch_bill_terms");
					so_terms = crs.getString("branch_so_terms");
					branch_notes = crs.getString("branch_notes");
					branch_active = crs.getString("branch_active");
					// branch_sms_enable = crs.getString("branch_sms_enable");
					// branch_sms_url = crs.getString("branch_sms_url");
					branch_esc_enquiry = crs.getString("branch_esc_enquiry");
					branch_esc_enquiry_followup = crs.getString("branch_esc_enquiry_followup");
					branch_esc_preowned_followup = crs.getString("branch_esc_preowned_followup");
					branch_esc_preowned_eval_followup = crs.getString("branch_esc_preowned_eval_followup");

					branch_esc_crm_followup = crs.getString("branch_esc_crm_followup");
					branch_esc_servicepsf_followup = crs.getString("branch_esc_servicepsf_followup");
					branch_esc_serviceveh_followup = crs.getString("branch_esc_serviceveh_followup");
					branch_enquiry_email_enable = crs.getString("branch_enquiry_email_enable");
					branch_enquiry_sms_enable = crs.getString("branch_enquiry_sms_enable");
					branch_enquiry_brochure_email_enable = crs.getString("branch_enquiry_brochure_email_enable");
					branch_testdrive_email_enable = crs.getString("branch_testdrive_email_enable");
					branch_testdrive_sms_enable = crs.getString("branch_testdrive_sms_enable");
					branch_quote_email_enable = crs.getString("branch_quote_email_enable");
					branch_quote_sms_enable = crs.getString("branch_quote_sms_enable");
					branch_so_email_enable = crs.getString("branch_so_email_enable");
					branch_so_sms_enable = crs.getString("branch_so_sms_enable");
					branch_preowned_email_enable = crs.getString("branch_preowned_email_enable");
					branch_preowned_sms_enable = crs.getString("branch_preowned_sms_enable");
					branch_so_delivered_email_enable = crs.getString("branch_so_delivered_email_enable");
					branch_so_delivered_sms_enable = crs.getString("branch_so_delivered_sms_enable");
					branch_service_due_email_enable = crs.getString("branch_service_due_email_enable");
					branch_service_due_sms_enable = crs.getString("branch_service_due_sms_enable");
					branch_service_appointment_email_enable = crs.getString("branch_service_appointment_email_enable");
					branch_service_appointment_sms_enable = crs.getString("branch_service_appointment_sms_enable");
					branch_jc_new_email_enable = crs.getString("branch_jc_new_email_enable");
					branch_jc_new_sms_enable = crs.getString("branch_jc_new_sms_enable");
					branch_jc_ready_email_enable = crs.getString("branch_jc_ready_email_enable");
					branch_jc_ready_sms_enable = crs.getString("branch_jc_ready_sms_enable");
					branch_jc_delivered_email_enable = crs.getString("branch_jc_delivered_email_enable");
					branch_jc_delivered_sms_enable = crs.getString("branch_jc_delivered_sms_enable");
					branch_jc_estimate_email_enable = crs.getString("branch_jc_estimate_email_enable");
					branch_jc_estimate_sms_enable = crs.getString("branch_jc_estimate_sms_enable");
					branch_jc_feedback_email_enable = crs.getString("branch_jc_feedback_email_enable");
					branch_jc_feedback_sms_enable = crs.getString("branch_jc_feedback_sms_enable");
					branch_insur_new_email_enable = crs.getString("branch_insur_new_email_enable");
					branch_insur_new_sms_enable = crs.getString("branch_insur_new_sms_enable");
					branch_insur_lost_email_enable = crs.getString("branch_insur_lost_email_enable");
					branch_insur_lost_sms_enable = crs.getString("branch_insur_lost_sms_enable");
					// branch_invoice_email_enable = crs.getString("branch_invoice_email_enable");
					// branch_invoice_sms_enable = crs.getString("branch_invoice_sms_enable");
					// branch_receipt_email_enable = crs.getString("branch_receipt_email_enable");
					// branch_receipt_sms_enable = crs.getString("branch_receipt_sms_enable");
					// branch_bill_email_enable =
					// crs.getString("branch_bill_email_enable");
					// branch_bill_sms_enable =
					// crs.getString("branch_bill_sms_enable");
					// branch_balance_due_email_enable =
					// crs.getString("branch_balance_due_email_enable");
					// branch_balance_due_sms_enable =
					// crs.getString("branch_balance_due_sms_enable");
					// branch_balance_overdue_email_enable =
					// crs.getString("branch_balance_overdue_email_enable");
					// branch_balance_overdue_sms_enable =
					// crs.getString("branch_balance_overdue_sms_enable");
					branch_entry_id = crs.getString("branch_entry_id");
					if (!branch_entry_id.equals("")) {
						branch_entry_by = Exename(comp_id, Integer.parseInt(branch_entry_id));
					}
					entry_date = strToLongDate(crs.getString("branch_entry_date"));
					branch_modified_id = crs.getString("branch_modified_id");
					if (!branch_modified_id.equals("0")) {
						branch_modified_by = Exename(comp_id, Integer.parseInt(branch_modified_id));
						modified_date = strToLongDate(crs.getString("branch_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Branch!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {

				StrSql = "UPDATE " + compdb(comp_id) + "axela_branch"
						+ " SET"
						+ " branch_name = '" + branch_name + "',"
						+ " branch_invoice_name = '" + branch_invoice_name + "',"
						+ " branch_tin = '" + branch_tin + "',"
						+ " branch_code = '" + branch_code + "',"
						+ " branch_cin = '" + branch_cin + "',"
						+ " branch_gst = '" + branch_gst + "',"
						+ " branch_vat = '" + branch_vat + "',"
						+ " branch_cst = '" + branch_cst + "',"
						+ " branch_pan = '" + branch_pan + "',"
						+ " branch_quote_prefix = '" + branch_quote_prefix + "',"
						+ " branch_so_prefix = '" + branch_so_prefix + "',"
						+ " branch_invoice_prefix = '" + branch_invoice_prefix + "',"
						+ " branch_receipt_prefix = '" + branch_receipt_prefix + "',"
						+ " branch_bill_prefix = '" + branch_bill_prefix + "',"
						+ " branch_jc_prefix = '" + branch_jc_prefix + "',"
						+ " branch_region_id = " + branch_region_id + ","
						+ " branch_zone_id = " + branch_zone_id + ","
						+ " branch_branchtype_id = " + branch_branchtype_id + ","
						+ " branch_franchisee_id = " + branch_franchisee_id + ","
						+ " branch_brand_id = " + branch_brand_id + ","
						+ " branch_rateclass_id	= " + branch_rateclass_id + ","
						+ " branch_veh_crm_emp_id	= " + veh_crmemp_id + ","
						+ " branch_phone1 = '" + branch_phone1 + "',"
						+ " branch_phone2 = '" + branch_phone2 + "',"
						+ " branch_mobile1 = '" + branch_mobile1 + "',"
						+ " branch_mobile2 = '" + branch_mobile2 + "',"
						+ " branch_email1 = '" + branch_email1 + "',"
						+ " branch_email2 = '" + branch_email2 + "',"
						+ " branch_sales_email = '" + branch_sales_email + "',"
						+ " branch_sales_mobile = '" + branch_sales_mobile + "',"
						+ " branch_servicebooking_email = '" + branch_servicebooking_email + "',"
						+ " branch_ticket_email = '" + branch_ticket_email + "',"
						+ " branch_crm_email = '" + branch_crm_email + "',"
						+ " branch_crm_mobile = '" + branch_crm_mobile + "',"
						+ " branch_socin_email = '" + branch_socin_email + "',"
						+ " branch_socin_mobile = '" + branch_socin_mobile + "',"
						+ " branch_din_email = '" + branch_din_email + "',"
						+ " branch_din_mobile = '" + branch_din_mobile + "',"
						+ " branch_jcpsf_email = '" + branch_jcpsf_email + "',"
						+ " branch_jcpsf_mobile = '" + branch_jcpsf_mobile + "',"
						+ " branch_add = '" + branch_add + "',"
						+ " branch_city_id = " + branch_city_id + ","
						+ " branch_pin = '" + branch_pin + "',"
						+ " branch_quote_desc = '" + quote_desc + "',"
						+ " branch_quote_terms = '" + quote_terms + "',"
						+ " branch_so_desc = '" + so_desc + "',"
						+ " branch_invoice_terms = '" + branch_invoice_terms + "',"
						+ " branch_bill_terms = '" + branch_bill_terms + "',"
						+ " branch_so_terms = '" + so_terms + "',"
						+ " branch_enquiry_email_enable = '" + branch_enquiry_email_enable + "',"
						+ " branch_enquiry_sms_enable = '" + branch_enquiry_sms_enable + "',"
						+ " branch_enquiry_brochure_email_enable = '" + branch_enquiry_brochure_email_enable + "',"
						+ " branch_testdrive_email_enable = '" + branch_testdrive_email_enable + "',"
						+ " branch_testdrive_sms_enable = '" + branch_testdrive_sms_enable + "',"
						+ " branch_quote_email_enable = '" + branch_quote_email_enable + "',"
						+ " branch_quote_sms_enable = '" + branch_quote_sms_enable + "',"
						+ " branch_so_email_enable = '" + branch_so_email_enable + "',"
						+ " branch_so_sms_enable = '" + branch_so_sms_enable + "',"
						+ " branch_so_delivered_email_enable = '" + branch_so_delivered_email_enable + "',"
						+ " branch_so_delivered_sms_enable = '" + branch_so_delivered_sms_enable + "',"
						+ " branch_preowned_email_enable = '" + branch_preowned_email_enable + "',"
						+ " branch_preowned_sms_enable = '" + branch_preowned_sms_enable + "',"
						+ " branch_service_due_email_enable = '" + branch_service_due_email_enable + "',"
						+ " branch_service_due_sms_enable = '" + branch_service_due_sms_enable + "',"
						+ " branch_service_appointment_email_enable = '" + branch_service_appointment_email_enable + "',"
						+ " branch_service_appointment_sms_enable = '" + branch_service_appointment_sms_enable + "',"
						+ " branch_jc_new_email_enable = '" + branch_jc_new_email_enable + "',"
						+ " branch_jc_new_sms_enable = '" + branch_jc_new_sms_enable + "',"
						+ " branch_jc_ready_email_enable = '" + branch_jc_ready_email_enable + "',"
						+ " branch_jc_ready_sms_enable = '" + branch_jc_ready_sms_enable + "',"
						+ " branch_jc_delivered_email_enable = '" + branch_jc_delivered_email_enable + "',"
						+ " branch_jc_delivered_sms_enable = '" + branch_jc_delivered_sms_enable + "',"
						+ " branch_jc_estimate_email_enable = '" + branch_jc_estimate_email_enable + "',"
						+ " branch_jc_estimate_sms_enable = '" + branch_jc_estimate_sms_enable + "',"
						+ " branch_jc_feedback_email_enable = '" + branch_jc_feedback_email_enable + "',"
						+ " branch_jc_feedback_sms_enable = '" + branch_jc_feedback_sms_enable + "',"
						+ " branch_insur_new_email_enable = '" + branch_insur_new_email_enable + "',"
						+ " branch_insur_new_sms_enable = '" + branch_insur_new_sms_enable + "',"
						+ " branch_insur_lost_email_enable = '" + branch_insur_lost_email_enable + "',"
						+ " branch_insur_lost_sms_enable = '" + branch_insur_lost_sms_enable + "',"
						// + " branch_invoice_email_enable = '" + branch_invoice_email_enable + "',"
						// + " branch_invoice_sms_enable = '" + branch_invoice_sms_enable + "',"
						// + " branch_receipt_email_enable = '" + branch_receipt_email_enable + "',"
						// + " branch_receipt_sms_enable = '" + branch_receipt_sms_enable + "',"
						// + " branch_bill_email_enable = '" + ///
						// branch_bill_email_enable + "',"
						// + " branch_bill_sms_enable = '" +
						// branch_bill_sms_enable + "',"
						// + " branch_balance_due_email_enable = '" +
						// branch_balance_due_email_enable + "',"
						// + " branch_balance_due_sms_enable = '" +
						// branch_balance_due_sms_enable + "',"
						// + " branch_balance_due_sms_format  ='" +
						// branch_balance_due_sms_format + "',"
						// + " branch_balance_overdue_email_enable = '" +
						// branch_balance_overdue_email_enable + "',"
						// + " branch_balance_overdue_sms_enable = '" +
						// branch_balance_overdue_sms_enable + "',"
						+ " branch_notes = '" + branch_notes + "',"
						+ " branch_active = '" + branch_active + "',"
						// + " branch_sms_enable = '" + branch_sms_enable + "',"
						// + " branch_sms_url = '" + branch_sms_url + "',"
						+ " branch_esc_enquiry = '" + branch_esc_enquiry + "',"
						+ " branch_esc_enquiry_followup = '" + branch_esc_enquiry_followup + "',"
						+ " branch_esc_preowned_followup = '" + branch_esc_preowned_followup + "',"
						+ " branch_esc_preowned_eval_followup = '" + branch_esc_preowned_eval_followup + "',"
						+ " branch_esc_crm_followup = '" + branch_esc_crm_followup + "',"
						+ " branch_esc_servicepsf_followup = '" + branch_esc_servicepsf_followup + "',"
						+ " branch_esc_serviceveh_followup = '" + branch_esc_serviceveh_followup + "',"
						+ " branch_modified_id = " + branch_modified_id + ","
						+ " branch_modified_date = '" + branch_modified_date + "'"
						+ " where branch_id = " + branch_id + "";
				// SOP("StrSql---------update-------" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "SELECT customer_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Customer!";
			}

			StrSql = "SELECT lead_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_lead"
					+ " WHERE lead_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Lead!";
			}
			StrSql = "SELECT enquiry_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Enquiry!";
			}

			// association with testdrive
			StrSql = "SELECT veh_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " WHERE veh_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Test Drive!";
			}

			StrSql = "SELECT quote_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " WHERE quote_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Quote!";
			}
			StrSql = "SELECT so_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Sales Order!";
			}

			// association with PO
			StrSql = "SELECT voucher_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 12"
					+ " AND voucher_branch_id = " + branch_id + "";

			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Purchase Order!";
			}

			// association with Stock
			StrSql = "SELECT vehstock_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " WHERE vehstock_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Stock!";
			}

			StrSql = "SELECT emp_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with Executives!";
			}

			StrSql = "SELECT news_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_news_branch"
					+ " WHERE news_branch_id = " + branch_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Branch is associated with News!";
			}
			if (msg.equals("")) {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_id = " + branch_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateRegion() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " ORDER BY region_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("region_id"));
				Str.append(StrSelectdrop(crs.getString("region_id"), branch_region_id));
				Str.append(">").append(crs.getString("region_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulateBranchType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branchtype_id, branchtype_name"
					+ " FROM axela_branch_type"
					+ " ORDER BY branchtype_name";
			// SOP("PopulateBranchType(===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branchtype_id"));
				Str.append(StrSelectdrop(crs.getString("branchtype_id"), branch_branchtype_id));
				Str.append(">").append(crs.getString("branchtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFranchisee() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT franchisee_id, franchisee_name"
					+ " FROM " + compdb(comp_id) + "axela_franchisee"
					+ " ORDER BY franchisee_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("franchisee_id"));
				Str.append(StrSelectdrop(crs.getString("franchisee_id"), branch_franchisee_id));
				Str.append(">").append(crs.getString("franchisee_name")).append(" (");
				Str.append(crs.getString("franchisee_id")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
	public String PopulatePrincipal()
	{

		StringBuilder sb = new StringBuilder();
		try
		{
			StrSql = " SELECT brand_id, brand_name"
					+ " FROM axela_brand "
					+ " ORDER BY brand_name ";

			CachedRowSet crs = processQuery(StrSql, 0);
			sb.append("<option value=0>Select</option>");
			while (crs.next())
			{
				sb.append("<option value=").append(crs.getString("brand_id"));
				sb.append(StrSelectdrop(crs.getString("brand_id"), branch_brand_id));
				sb.append(">").append(crs.getString("brand_name")).append(" ");
				sb.append("</option>\n");
			}
			crs.close();
			return sb.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String PopulateState() {
	// try {
	// StrSql = "SELECT state_id, state_name"
	// + " FROM " + compdb(comp_id) + "axela_state"
	// + " order by state_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// String group =
	// "<SELECT name=dr_state_id id=dr_state_id class=selectbox onchange=\"showHint('../portal/location.jsp?state_id='+GetReplace(this.value)+'&dr_city_id=dr_city_id','city_id');\">\n";
	// group = group + "<option value = 0> Select </option>";
	// while (crs.next()) {
	// group = group + "<option value=" + crs.getString("state_id") + " ";
	// group = group + StrSelectdrop(crs.getString("state_id"), state_id);
	// group = group + ">" + crs.getString("state_name") + "</option>\n";
	// }
	// group = group + "</SELECT>\n";
	// crs.close();
	// return group;
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
	// public String PopulateCity() {
	// try {
	// if (state_id.equals("")) {
	// state_id = "0";
	// }
	// StrSql = "SELECT city_id, city_name"
	// + " FROM " + compdb(comp_id) + "axela_city"
	// + " left join " + compdb(comp_id) +
	// "axela_state on state_id = city_state_id"
	// + " where city_state_id = " + state_id + ""
	// + " order by city_name";
	//
	// CachedRowSet crs =processQuery(StrSql, 0);
	// String group =
	// "<select name=dr_city_id class=selectbox><option value=0>Select</option>";
	// if (!state_id.equals("0")) {
	// while (crs.next()) {
	// group = group + "<option value=" + crs.getString("city_id") + " ";
	// group = group + StrSelectdrop(crs.getString("city_id"), branch_city_id);
	// group = group + ">" + crs.getString("city_name") + "</option>\n";
	// }
	// }
	// group = group + "</select>";
	// crs.close();
	// return group;
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
	public String PopulateRateClass() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " ORDER BY rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), branch_rateclass_id));
				Str.append(">").append(crs.getString("rateclass_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateConfigDetails() {
		StrSql = "SELECT count(branch_id) as activebranchcount"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_active = '1'";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				active_branchcount = crs.getInt("activebranchcount");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateZone() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT zone_id, zone_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_zone"
					+ " ORDER BY zone_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("zone_id"));
				Str.append(StrSelectdrop(crs.getString("zone_id"), branch_zone_id));
				Str.append(">").append(crs.getString("zone_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulateCRMExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS crmemp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_crm = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), veh_crmemp_id));
				Str.append(">").append(crs.getString("crmemp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
