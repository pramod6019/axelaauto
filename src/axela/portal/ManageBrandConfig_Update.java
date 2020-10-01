// Ved Prakash (11, 12 Feb 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.Ledger_Check;
import cloudify.connect.Connect;

public class ManageBrandConfig_Update extends Connect {

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
	public String brand_id = "0";
	public String QueryString = "";
	public String brandconfig_id = "0";
	public String brand_name = "";
	public String comp_id = "0", emp_id = "0";

	public String brandconfig_sms_enable = "";
	public String brandconfig_sms_url = "";

	public String brandconfig_comp_id = "0";
	public String brandconfig_brand_id = "0";
	public String brandconfig_principal_id = "0";
	public String brandconfig_notes = "";
	public String brandconfig_deallocatestock_enable = "0";
	public String brandconfig_deallocatestock_days = "0";
	public String brandconfig_deallocatestock_amountperc = "0";
	public String brandconfig_vehfollowup_days = "0";
	public String brandconfig_close_enquiry_after_days = "0";
	public String brandconfig_allocatestock_fifo = "";
	public String brandconfig_exestockview = "";

	public String brandconfig_vehfollowup_notcontactable_email_enable = "";
	public String brandconfig_vehfollowup_notcontactable_email_sub = "";
	public String brandconfig_vehfollowup_notcontactable_email_format = "";
	public String brandconfig_vehfollowup_notcontactable_sms_enable = "";
	public String brandconfig_vehfollowup_notcontactable_sms_format = "";

	public String brandconfig_vehfollowup_dailydue_email_enable = "";
	public String brandconfig_vehfollowup_dailydue_email_sub = "";
	public String brandconfig_vehfollowup_dailydue_email_format = "";
	public String brandconfig_vehfollowup_dailydue_sms_enable = "";
	public String brandconfig_vehfollowup_dailydue_sms_format = "";

	public String brandconfig_vehfollowup_booking_email_enable = "";
	public String brandconfig_vehfollowup_booking_email_sub = "";
	public String brandconfig_vehfollowup_booking_email_format = "";
	public String brandconfig_vehfollowup_booking_exe_email_sub = "";
	public String brandconfig_vehfollowup_booking_exe_email_format = "";
	public String brandconfig_vehfollowup_booking_sms_enable = "";
	public String brandconfig_vehfollowup_booking_sms_format = "";
	public String brandconfig_vehfollowup_booking_exe_sms_format = "";

	public String brandconfig_vehfollowup_serviced_email_enable = "";
	public String brandconfig_vehfollowup_serviced_email_sub = "";
	public String brandconfig_vehfollowup_serviced_email_format = "";
	public String brandconfig_vehfollowup_serviced_sms_enable = "";
	public String brandconfig_vehfollowup_serviced_sms_format = "";

	public String brandconfig_coupon_email_enable = "";
	public String brandconfig_coupon_email_sub = "";
	public String brandconfig_coupon_email_format = "";
	public String brandconfig_coupon_sms_enable = "";
	public String brandconfig_coupon_sms_format = "";

	public String brandconfig_insur_enquiry_email_enable = "";
	public String brandconfig_insur_enquiry_email_sub = "";
	public String brandconfig_insur_enquiry_email_format = "";
	public String brandconfig_insur_enquiry_exe_email_sub = "";
	public String brandconfig_insur_enquiry_exe_email_format = "";
	public String brandconfig_insur_enquiry_sms_enable = "";
	public String brandconfig_insur_enquiry_sms_format = "";
	public String brandconfig_insur_enquiry_exe_sms_format = "";

	public String brandconfig_din_email_enable = "";
	public String brandconfig_din_email_sub = "";
	public String brandconfig_din_email_format = "";
	public String brandconfig_din_sms_enable = "";
	public String brandconfig_din_sms_format = "";

	public String brandconfig_socin_email_enable = "";
	public String brandconfig_socin_email_sub = "";
	public String brandconfig_socin_email_format = "";
	public String brandconfig_socin_sms_enable = "";
	public String brandconfig_socin_sms_format = "";

	public String brandconfig_socin_exe_email_enable = "";
	public String brandconfig_socin_exe_email_sub = "";
	public String brandconfig_socin_exe_email_format = "";
	public String brandconfig_socin_exe_sms_enable = "";
	public String brandconfig_socin_exe_sms_format = "";

	public String brandconfig_receipt_authorize_email_enable = "";
	public String brandconfig_receipt_authorize_email_sub = "";
	public String brandconfig_receipt_authorize_email_format = "";
	public String brandconfig_receipt_authorize_sms_enable = "";
	public String brandconfig_receipt_authorize_sms_format = "";

	public String brandconfig_receipt_email_enable = "";
	public String brandconfig_receipt_email_sub = "";
	public String brandconfig_receipt_email_format = "";
	public String brandconfig_receipt_sms_enable = "";
	public String brandconfig_receipt_sms_format = "";

	public String brandconfig_entry_id = "0";
	public String brandconfig_entry_date = "";
	public String brandconfig_modified_id = "0";
	public String brandconfig_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String brandconfig_entry_by = "";
	public String brandconfig_modified_by = "";

	public String brandconfig_noshow_enable = "0";
	public String brandconfig_incentive = "0";
	public String brandconfig_discountauthorize = "0";
	public String brandconfig_noshow_days = "0";
	public String brandconfig_noshow_future_days = "0";

	// New Fields
	public String brandconfig_enquiry_email_enable = "";
	public String brandconfig_enquiry_email_sub = "";
	public String brandconfig_enquiry_email_format = "";
	public String brandconfig_enquiry_email_exe_enable = "";
	public String brandconfig_enquiry_email_exe_sub = "";
	public String brandconfig_enquiry_email_exe_format = "";
	public String brandconfig_enquiry_sms_enable = "";
	public String brandconfig_enquiry_sms_format = "";
	public String brandconfig_enquiry_sms_exe_enable = "";
	public String brandconfig_enquiry_sms_exe_format = "";

	public String brandconfig_enquiry_brochure_email_enable = "";
	public String brandconfig_enquiry_brochure_email_sub = "";
	public String brandconfig_enquiry_brochure_email_format = "";

	public String brandconfig_testdrive_email_enable = "";
	public String brandconfig_testdrive_email_sub = "";
	public String brandconfig_testdrive_email_format = "";
	public String brandconfig_testdrive_email_exe_enable = "";
	public String brandconfig_testdrive_email_exe_sub = "";
	public String brandconfig_testdrive_email_exe_format = "";
	public String brandconfig_testdrive_sms_enable = "";
	public String brandconfig_testdrive_sms_format = "";
	public String brandconfig_testdrive_sms_exe_enable = "";
	public String brandconfig_testdrive_sms_exe_format = "";

	public String brandconfig_testdrive_feedback_email_enable = "";
	public String brandconfig_testdrive_feedback_email_sub = "";
	public String brandconfig_testdrive_feedback_email_format = "";
	public String brandconfig_testdrive_feedback_email_exe_enable = "";
	public String brandconfig_testdrive_feedback_email_exe_sub = "";
	public String brandconfig_testdrive_feedback_email_exe_format = "";
	public String brandconfig_testdrive_feedback_sms_enable = "";
	public String brandconfig_testdrive_feedback_sms_format = "";
	public String brandconfig_testdrive_feedback_sms_exe_enable = "";
	public String brandconfig_testdrive_feedback_sms_exe_format = "";

	public String brandconfig_quote_email_enable = "";
	public String brandconfig_quote_email_sub = "";
	public String brandconfig_quote_email_format = "";
	public String brandconfig_quote_email_exe_enable = "";
	public String brandconfig_quote_email_exe_sub = "";
	public String brandconfig_quote_email_exe_format = "";
	public String brandconfig_quote_sms_enable = "";
	public String brandconfig_quote_sms_format = "";
	public String brandconfig_quote_sms_exe_enable = "";
	public String brandconfig_quote_sms_exe_format = "";
	public String brandconfig_quote_discount_authorize_email_enable = "";
	public String brandconfig_quote_discount_authorize_email_sub = "";
	public String brandconfig_quote_discount_authorize_email_format = "";
	public String brandconfig_quote_discount_authorize_sms_enable = "";
	public String brandconfig_quote_discount_authorize_sms_format = "";

	public String brandconfig_so_email_enable = "";
	public String brandconfig_so_email_sub = "";
	public String brandconfig_so_email_format = "";
	public String brandconfig_so_email_exe_enable = "";
	public String brandconfig_so_email_exe_sub = "";
	public String brandconfig_so_email_exe_format = "";
	public String brandconfig_so_sms_enable = "";
	public String brandconfig_so_sms_format = "";
	public String brandconfig_so_sms_exe_enable = "";
	public String brandconfig_so_sms_exe_format = "";

	public String brandconfig_so_delivered_email_enable = "";
	public String brandconfig_so_delivered_email_sub = "";
	public String brandconfig_so_delivered_email_format = "";
	public String brandconfig_so_delivered_sms_enable = "";
	public String brandconfig_so_delivered_sms_format = "";

	public String brandconfig_preowned_email_enable = "";
	public String brandconfig_preowned_email_sub = "";
	public String brandconfig_preowned_email_format = "";
	public String brandconfig_preowned_email_exe_enable = "";
	public String brandconfig_preowned_email_exe_sub = "";
	public String brandconfig_preowned_email_exe_format = "";
	public String brandconfig_preowned_sms_enable = "";
	public String brandconfig_preowned_sms_format = "";
	public String brandconfig_preowned_sms_exe_enable = "";
	public String brandconfig_preowned_sms_exe_format = "";

	public String brandconfig_jc_new_email_enable = "";
	public String brandconfig_jc_new_email_sub = "";
	public String brandconfig_jc_new_email_format = "";
	public String brandconfig_jc_new_sms_enable = "";
	public String brandconfig_jc_new_sms_format = "";

	public String brandconfig_jc_ready_email_enable = "";
	public String brandconfig_jc_ready_email_sub = "";
	public String brandconfig_jc_ready_email_format = "";
	public String brandconfig_jc_ready_sms_enable = "";
	public String brandconfig_jc_ready_sms_format = "";

	public String brandconfig_jc_delivered_email_enable = "";
	public String brandconfig_jc_delivered_email_sub = "";
	public String brandconfig_jc_delivered_email_format = "";
	public String brandconfig_jc_delivered_sms_enable = "";
	public String brandconfig_jc_delivered_sms_format = "";

	public String brandconfig_jc_estimate_email_enable = "";
	public String brandconfig_jc_estimate_email_sub = "";
	public String brandconfig_jc_estimate_email_format = "";
	public String brandconfig_jc_estimate_sms_enable = "";
	public String brandconfig_jc_estimate_sms_format = "";

	public String brandconfig_jc_feedback_email_enable = "";
	public String brandconfig_jc_feedback_email_sub = "";
	public String brandconfig_jc_feedback_email_format = "";
	public String brandconfig_jc_feedback_sms_enable = "";
	public String brandconfig_jc_feedback_sms_format = "";

	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{

				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				brandconfig_id = CNumeric(PadQuotes(request.getParameter("brandconfig_id")));
				brandconfig_principal_id = CNumeric(PadQuotes(request.getParameter("accountingcustomer")));
				brand_id = CNumeric(PadQuotes(request.getParameter("hidden_brandconfig_brand_id")));

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							brandconfig_entry_id = CNumeric(GetSession("emp_id", request));
							brandconfig_entry_date = ToLongDate(kknow());
							brandconfig_modified_date = "";
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managebrandconfig-list.jsp?brandconfig_id=" + brandconfig_id + "&msg=Brand Config Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Brand Config".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Brand Config".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							brandconfig_modified_id = CNumeric(GetSession("emp_id", request));
							brandconfig_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managebrandconfig-list.jsp?brandconfig_id=" + brandconfig_id + "&msg=Brand Config Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Brand Config".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managebrandconfig-list.jsp?msg=Brand Config Deleted Successfully!"));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		brandconfig_brand_id = PadQuotes(request.getParameter("dr_brand_id"));
		brandconfig_principal_id = CNumeric(PadQuotes(request.getParameter("accountingcustomer")));
		brand_id = CNumeric(PadQuotes(request.getParameter("hidden_brandconfig_brand_id")));
		brandconfig_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_sms_enable"));
		if (brandconfig_sms_enable.equals("on")) {
			brandconfig_sms_enable = "1";
		} else {
			brandconfig_sms_enable = "0";
		}
		if (brandconfig_sms_enable.equals("1")) {
			brandconfig_sms_url = PadQuotes(request.getParameter("txt_brandconfig_sms_url"));
		}
		brandconfig_close_enquiry_after_days = PadQuotes(request.getParameter("txt_close_enquiry_after_days"));
		brandconfig_allocatestock_fifo = PadQuotes(request.getParameter("dr_stockfifo_id"));
		brandconfig_exestockview = PadQuotes(request.getParameter("dr_exestockview_id"));
		brandconfig_deallocatestock_enable = PadQuotes(request.getParameter("chk_brandconfig_deallocatestock_enable"));
		brandconfig_deallocatestock_days = PadQuotes(request.getParameter("txt_brandconfig_deallocatestock_days"));
		brandconfig_deallocatestock_amountperc = PadQuotes(request.getParameter("txt_brandconfig_deallocatestock_amountperc"));
		brandconfig_vehfollowup_days = PadQuotes(request.getParameter("txt_brandconfig_vehfollowup_days"));
		brandconfig_notes = PadQuotes(request.getParameter("txt_brandconfig_notes"));

		brandconfig_vehfollowup_notcontactable_email_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_notcontactable_email_enable"));
		brandconfig_vehfollowup_notcontactable_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_notcontactable_sms_enable"));

		brandconfig_vehfollowup_dailydue_email_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_dailydue_email_enable"));
		brandconfig_vehfollowup_dailydue_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_dailydue_sms_enable"));

		brandconfig_vehfollowup_booking_email_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_booking_email_enable"));
		brandconfig_vehfollowup_booking_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_booking_sms_enable"));

		brandconfig_vehfollowup_serviced_email_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_serviced_email_enable"));
		brandconfig_vehfollowup_serviced_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_vehfollowup_serviced_sms_enable"));

		brandconfig_coupon_email_enable = PadQuotes(request.getParameter("chk_brandconfig_coupon_email_enable"));
		brandconfig_coupon_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_coupon_sms_enable"));

		brandconfig_insur_enquiry_email_enable = PadQuotes(request.getParameter("chk_brandconfig_insur_enquiry_email_enable"));
		brandconfig_insur_enquiry_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_insur_enquiry_sms_enable"));

		brandconfig_socin_email_enable = PadQuotes(request.getParameter("chk_brandconfig_socin_email_enable"));
		brandconfig_socin_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_socin_sms_enable"));

		brandconfig_socin_exe_email_enable = PadQuotes(request.getParameter("chk_brandconfig_socin_exe_email_enable"));
		brandconfig_socin_exe_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_socin_exe_sms_enable"));

		brandconfig_din_email_enable = PadQuotes(request.getParameter("chk_brandconfig_din_email_enable"));
		brandconfig_din_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_din_sms_enable"));

		brandconfig_receipt_email_enable = PadQuotes(request.getParameter("chk_brandconfig_receipt_email_enable"));
		brandconfig_receipt_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_receipt_sms_enable"));

		brandconfig_receipt_authorize_email_enable = PadQuotes(request.getParameter("chk_brandconfig_receipt_authorize_email_enable"));
		brandconfig_receipt_authorize_sms_enable = PadQuotes(request.getParameter("chk_brandconfig_receipt_authorize_sms_enable"));

		brandconfig_noshow_enable = PadQuotes(request.getParameter("chk_brandconfig_noshow_enable"));
		brandconfig_noshow_days = PadQuotes(request.getParameter("txt_brandconfig_noshow_days"));
		brandconfig_noshow_future_days = PadQuotes(request.getParameter("txt_brandconfig_noshow_future_days"));
		brandconfig_incentive = PadQuotes(request.getParameter("chk_brandconfig_incentive"));
		brandconfig_discountauthorize = PadQuotes(request.getParameter("chk_brandconfig_discountauthorize"));

		brandconfig_enquiry_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_enquiry_email_enable")));
		brandconfig_enquiry_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_enquiry_sms_enable")));
		brandconfig_enquiry_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_enquiry_email_exe_enable")));
		brandconfig_enquiry_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_enquiry_sms_exe_enable")));

		brandconfig_enquiry_brochure_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_enquiry_brochure_email_enable")));

		brandconfig_testdrive_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_email_enable")));
		brandconfig_testdrive_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_sms_enable")));
		brandconfig_testdrive_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_email_exe_enable")));
		brandconfig_testdrive_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_sms_exe_enable")));

		brandconfig_testdrive_feedback_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_feedback_email_enable")));
		brandconfig_testdrive_feedback_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_feedback_sms_enable")));
		brandconfig_testdrive_feedback_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_feedback_email_exe_enable")));
		brandconfig_testdrive_feedback_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_testdrive_feedback_sms_exe_enable")));

		brandconfig_quote_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_email_enable")));
		brandconfig_quote_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_sms_enable")));
		brandconfig_quote_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_email_exe_enable")));
		brandconfig_quote_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_sms_exe_enable")));
		brandconfig_quote_discount_authorize_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_discount_authorize_email_enable")));
		brandconfig_quote_discount_authorize_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_quote_discount_authorize_sms_enable")));

		brandconfig_so_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_email_enable")));
		brandconfig_so_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_sms_enable")));
		brandconfig_so_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_email_exe_enable")));
		brandconfig_so_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_sms_exe_enable")));

		brandconfig_so_delivered_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_delivered_email_enable")));
		brandconfig_so_delivered_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_so_delivered_sms_enable")));

		brandconfig_preowned_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_preowned_email_enable")));
		brandconfig_preowned_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_preowned_sms_enable")));
		brandconfig_preowned_email_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_preowned_email_exe_enable")));
		brandconfig_preowned_sms_exe_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_preowned_sms_exe_enable")));

		brandconfig_jc_new_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_new_email_enable")));
		brandconfig_jc_new_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_new_sms_enable")));

		brandconfig_jc_ready_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_ready_email_enable")));
		brandconfig_jc_ready_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_ready_sms_enable")));

		brandconfig_jc_delivered_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_delivered_email_enable")));
		brandconfig_jc_delivered_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_delivered_sms_enable")));

		brandconfig_jc_estimate_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_estimate_email_enable")));
		brandconfig_jc_estimate_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_estimate_sms_enable")));

		brandconfig_jc_feedback_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_feedback_email_enable")));
		brandconfig_jc_feedback_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_feedback_sms_enable")));

		brandconfig_jc_estimate_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_estimate_email_enable")));
		brandconfig_jc_estimate_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_estimate_sms_enable")));

		brandconfig_jc_feedback_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_feedback_email_enable")));
		brandconfig_jc_feedback_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_brandconfig_jc_feedback_sms_enable")));

		if (brandconfig_incentive.equals("on")) {
			brandconfig_incentive = "1";
		} else {
			brandconfig_incentive = "0";
		}

		if (brandconfig_discountauthorize.equals("on")) {
			brandconfig_discountauthorize = "1";
		} else {
			brandconfig_discountauthorize = "0";
		}

		if (brandconfig_noshow_enable.equals("on")) {
			brandconfig_noshow_enable = "1";
		} else {
			brandconfig_noshow_enable = "0";
		}

		if (brandconfig_deallocatestock_enable.equals("on")) {
			brandconfig_deallocatestock_enable = "1";
		} else {
			brandconfig_deallocatestock_enable = "0";
		}
		// brandconfig_receipt_authorize_enable start
		if (brandconfig_receipt_authorize_email_enable.equals("on")) {
			brandconfig_receipt_authorize_email_enable = "1";
		} else {
			brandconfig_receipt_authorize_email_enable = "0";
		}
		if (brandconfig_receipt_authorize_sms_enable.equals("on")) {
			brandconfig_receipt_authorize_sms_enable = "1";
		} else {
			brandconfig_receipt_authorize_sms_enable = "0";
		}
		// brandconfig_receipt_authorize_enable end

		// brandconfig_receipt_notcontactable_enable start
		if (brandconfig_vehfollowup_notcontactable_email_enable.equals("on")) {
			brandconfig_vehfollowup_notcontactable_email_enable = "1";
		} else {
			brandconfig_vehfollowup_notcontactable_email_enable = "0";
		}
		if (brandconfig_vehfollowup_notcontactable_sms_enable.equals("on")) {
			brandconfig_vehfollowup_notcontactable_sms_enable = "1";
		} else {
			brandconfig_vehfollowup_notcontactable_sms_enable = "0";
		}
		// brandconfig_receipt_notcontactable_enable end

		// brandconfig_receipt_dailydue_enable start
		if (brandconfig_vehfollowup_dailydue_email_enable.equals("on")) {
			brandconfig_vehfollowup_dailydue_email_enable = "1";
		} else {
			brandconfig_vehfollowup_dailydue_email_enable = "0";
		}
		if (brandconfig_vehfollowup_dailydue_sms_enable.equals("on")) {
			brandconfig_vehfollowup_dailydue_sms_enable = "1";
		} else {
			brandconfig_vehfollowup_dailydue_sms_enable = "0";
		}
		// brandconfig_receipt_dailydue_enable end

		// brandconfig_receipt_bookinge_enable start
		if (brandconfig_vehfollowup_booking_email_enable.equals("on")) {
			brandconfig_vehfollowup_booking_email_enable = "1";
		} else {
			brandconfig_vehfollowup_booking_email_enable = "0";
		}
		if (brandconfig_vehfollowup_booking_sms_enable.equals("on")) {
			brandconfig_vehfollowup_booking_sms_enable = "1";
		} else {
			brandconfig_vehfollowup_booking_sms_enable = "0";
		}
		// brandconfig_receipt_booking_enable end

		// brandconfig_receipt_serviced_enable start
		if (brandconfig_vehfollowup_serviced_email_enable.equals("on")) {
			brandconfig_vehfollowup_serviced_email_enable = "1";
		} else {
			brandconfig_vehfollowup_serviced_email_enable = "0";
		}
		if (brandconfig_vehfollowup_serviced_sms_enable.equals("on")) {
			brandconfig_vehfollowup_serviced_sms_enable = "1";
		} else {
			brandconfig_vehfollowup_serviced_sms_enable = "0";
		}

		// brandconfig_coupon_email_enable
		if (brandconfig_coupon_email_enable.equals("on")) {
			brandconfig_coupon_email_enable = "1";
		} else {
			brandconfig_coupon_email_enable = "0";
		}

		// brandconfig_coupon_sms_enable
		if (brandconfig_coupon_sms_enable.equals("on")) {
			brandconfig_coupon_sms_enable = "1";
		} else {
			brandconfig_coupon_sms_enable = "0";
		}

		// brandconfig_receipt_serviced_enable end

		// brandconfig_receipt_insurance_enable start
		if (brandconfig_insur_enquiry_email_enable.equals("on")) {
			brandconfig_insur_enquiry_email_enable = "1";
		} else {
			brandconfig_insur_enquiry_email_enable = "0";
		}
		if (brandconfig_insur_enquiry_sms_enable.equals("on")) {
			brandconfig_insur_enquiry_sms_enable = "1";
		} else {
			brandconfig_insur_enquiry_sms_enable = "0";
		}

		// brandconfig_SOCIN_enable start
		if (brandconfig_socin_email_enable.equals("on")) {
			brandconfig_socin_email_enable = "1";
		} else {
			brandconfig_socin_email_enable = "0";
		}
		if (brandconfig_socin_sms_enable.equals("on")) {
			brandconfig_socin_sms_enable = "1";
		} else {
			brandconfig_socin_sms_enable = "0";
		}

		// brandconfig_SOCIN_exe_enable start
		if (brandconfig_socin_exe_email_enable.equals("on")) {
			brandconfig_socin_exe_email_enable = "1";
		} else {
			brandconfig_socin_exe_email_enable = "0";
		}
		if (brandconfig_socin_exe_sms_enable.equals("on")) {
			brandconfig_socin_exe_sms_enable = "1";
		} else {
			brandconfig_socin_exe_sms_enable = "0";
		}

		// brandconfig_receipt_insurance_enable end

		// brandconfig_receipt_din_enable start
		if (brandconfig_din_email_enable.equals("on")) {
			brandconfig_din_email_enable = "1";
		} else {
			brandconfig_din_email_enable = "0";
		}
		if (brandconfig_din_sms_enable.equals("on")) {
			brandconfig_din_sms_enable = "1";
		} else {
			brandconfig_din_sms_enable = "0";
		}
		// brandconfig_receipt_din_enable end

		// brandconfig_receipt_enable start
		if (brandconfig_receipt_email_enable.equals("on")) {
			brandconfig_receipt_email_enable = "1";
		} else {
			brandconfig_receipt_email_enable = "0";
		}
		if (brandconfig_receipt_sms_enable.equals("on")) {
			brandconfig_receipt_sms_enable = "1";
		} else {
			brandconfig_receipt_sms_enable = "0";
		}
		// brandconfig_receipt_enable end

		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		brandconfig_entry_by = PadQuotes(request.getParameter("brandconfig_entry_by"));
		brandconfig_modified_by = PadQuotes(request.getParameter("brandconfig_modified_by"));

	}

	protected void CheckForm() {
		msg = "";
		Msg1 = "";
		try {
			if (brandconfig_sms_enable.equals("1")) {
				if (brandconfig_sms_url.equals("")) {
					msg += "<br>Enter SMS URL!";
				}
			}

			// if (brandconfig_deallocatestock_days.equals("")) {
			// msg = msg + "<br>Enter De-Allocate Stock Days!";
			// }

			if (brandconfig_deallocatestock_enable.equals("1")) {
				if (Integer.parseInt(CNumeric(brandconfig_deallocatestock_days)) < 1) {
					msg = msg + "<br>De-Allocate Stock Days should be greater than 0!";
				}
			}

			if (brandconfig_noshow_enable.equals("1")) {
				if (Integer.parseInt(CNumeric(brandconfig_noshow_days)) < 1) {
					msg = msg + "<br>No Show Days should be greater than 0!";
				}
				if (Integer.parseInt(CNumeric(brandconfig_noshow_future_days)) < 1) {
					msg = msg + "<br>No Show Future Days should be greater than 0!";
				}
			}
			// if (brandconfig_deallocatestock_amountperc.equals("")) {
			// msg = msg + "<br>Enter Amount Percentage!";
			// }

			if (brandconfig_deallocatestock_enable.equals("1")) {
				if (Integer.parseInt(CNumeric(brandconfig_deallocatestock_amountperc)) < 1) {
					msg = msg + "<br>Amount Percentage should be greater than 0!";
				}
			}

			if (brandconfig_brand_id.equals("0")) {
				msg = msg + "<br>Select Brand!";
			} else {
				StrSql = "SELECT brandconfig_brand_id "
						+ " FROM " + compdb(comp_id) + "axela_brand_config"
						+ " WHERE brandconfig_brand_id =" + brandconfig_brand_id;
				if (update.equals("yes")) {
					StrSql += " AND brandconfig_id != " + brandconfig_id + "";
				}

				if (ExecuteQuery(StrSql).equals(brandconfig_brand_id)) {
					msg = msg + "<br>Similar Brand Name found!";
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {

				brandconfig_id = ExecuteQuery("SELECT COALESCE(max(brandconfig_id), 0)+1 as brandconfig_id FROM " + compdb(comp_id) + "axela_brand_config");

				// brandconfig_id = "" +
				// CheckCurrentId(Integer.parseInt(brandconfig_id));
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_brand_config"
						+ " (brandconfig_id,"
						+ " brandconfig_comp_id,"
						+ " brandconfig_brand_id,"
						+ " brandconfig_principal_id,"
						+ "	brandconfig_sms_enable,"
						+ "	brandconfig_sms_url,"
						+ " brandconfig_closeenqafterdays,"
						+ " brandconfig_exestockview,"
						+ " brandconfig_allocatestock_fifo,"
						+ " brandconfig_deallocatestock_enable,"
						+ " brandconfig_deallocatestock_days,"
						+ " brandconfig_deallocatestock_amountperc,"

						+ " brandconfig_vehfollowup_days,"

						+ " brandconfig_vehfollowup_noshow,"
						+ " brandconfig_vehfollowup_noshow_days,"
						+ " brandconfig_vehfollowup_noshow_futuredays,"

						// new fields
						+ " brandconfig_enquiry_email_sub, "
						+ " brandconfig_enquiry_email_format, "
						+ " brandconfig_enquiry_email_exe_sub, "
						+ " brandconfig_enquiry_email_exe_format,"
						+ " brandconfig_enquiry_sms_format,"
						+ " brandconfig_enquiry_sms_exe_format, "

						+ " brandconfig_enquiry_brochure_email_sub, "
						+ " brandconfig_enquiry_brochure_email_format, "

						+ " brandconfig_testdrive_email_sub, "
						+ " brandconfig_testdrive_email_format, "
						+ " brandconfig_testdrive_email_exe_sub, "
						+ " brandconfig_testdrive_email_exe_format,"
						+ " brandconfig_testdrive_sms_format,"
						+ " brandconfig_testdrive_sms_exe_format, "

						+ " brandconfig_testdrive_feedback_email_sub, "
						+ " brandconfig_testdrive_feedback_email_format, "
						+ " brandconfig_testdrive_feedback_email_exe_sub, "
						+ " brandconfig_testdrive_feedback_email_exe_format,"
						+ " brandconfig_testdrive_feedback_sms_format,"
						+ " brandconfig_testdrive_feedback_sms_exe_format, "

						+ " brandconfig_quote_email_sub, "
						+ " brandconfig_quote_email_format, "
						+ " brandconfig_quote_email_exe_sub, "
						+ " brandconfig_quote_email_exe_format,"
						+ " brandconfig_quote_sms_format,"
						+ " brandconfig_quote_sms_exe_format,"
						+ " brandconfig_quote_discount_authorize_email_sub,"
						+ " brandconfig_quote_discount_authorize_email_format,"
						+ " brandconfig_quote_discount_authorize_sms_format,"

						+ " brandconfig_so_email_sub, "
						+ " brandconfig_so_email_format, "
						+ " brandconfig_so_email_exe_sub, "
						+ " brandconfig_so_email_exe_format, "
						+ " brandconfig_so_sms_format, "
						+ " brandconfig_so_sms_exe_format, "

						+ " brandconfig_so_delivered_email_sub, "
						+ " brandconfig_so_delivered_email_format, "
						+ " brandconfig_so_delivered_sms_format, "

						+ " brandconfig_preowned_email_sub, "
						+ " brandconfig_preowned_email_format, "
						+ " brandconfig_preowned_email_exe_sub, "
						+ " brandconfig_preowned_email_exe_format,"
						+ " brandconfig_preowned_sms_format, "
						+ " brandconfig_preowned_sms_exe_format, "

						+ " brandconfig_jc_new_email_sub, "
						+ " brandconfig_jc_new_email_format, "
						+ " brandconfig_jc_new_sms_format, "

						+ " brandconfig_jc_ready_email_sub, "
						+ " brandconfig_jc_ready_email_format, "
						+ " brandconfig_jc_ready_sms_format, "

						+ " brandconfig_jc_delivered_email_sub, "
						+ " brandconfig_jc_delivered_email_format, "
						+ " brandconfig_jc_delivered_sms_format, "

						+ " brandconfig_jc_estimate_email_sub, "
						+ " brandconfig_jc_estimate_email_format, "
						+ " brandconfig_jc_estimate_sms_format, "

						+ " brandconfig_jc_feedback_email_sub, "
						+ " brandconfig_jc_feedback_email_format,"
						+ " brandconfig_jc_feedback_sms_format,"
						// end of new fields

						+ " brandconfig_incentive,"
						+ " brandconfig_discountauthorize,"

						+ " brandconfig_vehfollowup_notcontactable_email_sub,"
						+ " brandconfig_vehfollowup_notcontactable_email_format,"
						+ " brandconfig_vehfollowup_notcontactable_sms_format,"

						+ " brandconfig_vehfollowup_dailydue_email_sub,"
						+ " brandconfig_vehfollowup_dailydue_email_format,"
						+ " brandconfig_vehfollowup_dailydue_sms_format,"

						+ " brandconfig_vehfollowup_booking_email_sub,"
						+ " brandconfig_vehfollowup_booking_email_format,"
						+ " brandconfig_vehfollowup_booking_exe_email_sub,"
						+ " brandconfig_vehfollowup_booking_exe_email_format,"
						+ " brandconfig_vehfollowup_booking_sms_format,"
						+ " brandconfig_vehfollowup_booking_exe_sms_format,"

						+ " brandconfig_vehfollowup_serviced_email_sub,"
						+ " brandconfig_vehfollowup_serviced_email_format,"
						+ " brandconfig_vehfollowup_serviced_sms_format,"

						+ " brandconfig_coupon_email_enable,"
						+ " brandconfig_coupon_email_sub,"
						+ " brandconfig_coupon_email_format,"
						+ " brandconfig_coupon_sms_enable,"
						+ " brandconfig_coupon_sms_format,"

						+ " brandconfig_insur_enquiry_email_sub,"
						+ " brandconfig_insur_enquiry_email_format,"
						+ " brandconfig_insur_enquiry_exe_email_sub,"
						+ " brandconfig_insur_enquiry_exe_email_format,"
						+ " brandconfig_insur_enquiry_sms_format,"
						+ " brandconfig_insur_enquiry_exe_sms_format,"

						+ " brandconfig_socin_email_sub,"
						+ " brandconfig_socin_email_format,"
						+ " brandconfig_socin_sms_format,"

						+ " brandconfig_socin_exe_email_sub,"
						+ " brandconfig_socin_exe_email_format,"
						+ " brandconfig_socin_exe_sms_format,"

						+ " brandconfig_din_email_sub,"
						+ " brandconfig_din_email_format,"
						+ " brandconfig_din_sms_format,"

						+ " brandconfig_receipt_email_sub,"
						+ " brandconfig_receipt_email_format,"
						+ " brandconfig_receipt_sms_format,"

						+ " brandconfig_receipt_authorize_email_sub,"
						+ " brandconfig_receipt_authorize_email_format,"
						+ " brandconfig_receipt_authorize_sms_format,"

						+ " brandconfig_notes,"
						+ " brandconfig_entry_id,"
						+ " brandconfig_entry_date)"
						+ " VALUES"
						+ " (" + brandconfig_id + ","
						+ " " + comp_id + ","
						+ " " + brandconfig_brand_id + ","
						+ " " + brandconfig_principal_id + ","
						+ " " + brandconfig_sms_enable + ","
						+ " '" + brandconfig_sms_url + "',"

						+ " " + brandconfig_close_enquiry_after_days + ","
						+ " " + brandconfig_exestockview + ","
						+ " " + brandconfig_allocatestock_fifo + ","
						+ " " + brandconfig_deallocatestock_enable + ","
						+ " " + brandconfig_deallocatestock_days + ","
						+ " " + brandconfig_deallocatestock_amountperc + ","
						+ " " + brandconfig_vehfollowup_days + ","

						+ " " + brandconfig_noshow_enable + ","
						+ " " + brandconfig_noshow_days + ","
						+ " " + brandconfig_noshow_future_days + ","

						// new Fields
						+ " '" + brandconfig_enquiry_email_sub + "',"
						+ " '" + brandconfig_enquiry_email_format + "',"
						+ " '" + brandconfig_enquiry_email_exe_sub + "',"
						+ " '" + brandconfig_enquiry_email_exe_format + "',"
						+ " '" + brandconfig_enquiry_sms_format + "',"
						+ " '" + brandconfig_enquiry_sms_exe_format + "',"

						+ " '" + brandconfig_enquiry_brochure_email_sub + "',"
						+ " '" + brandconfig_enquiry_brochure_email_format + "',"

						+ " '" + brandconfig_testdrive_email_sub + "',"
						+ " '" + brandconfig_testdrive_email_format + "',"
						+ " '" + brandconfig_testdrive_email_exe_sub + "',"
						+ " '" + brandconfig_testdrive_email_exe_format + "',"
						+ " '" + brandconfig_testdrive_sms_format + "',"
						+ " '" + brandconfig_testdrive_sms_exe_format + "',"

						+ " '" + brandconfig_testdrive_feedback_email_sub + "',"
						+ " '" + brandconfig_testdrive_feedback_email_format + "',"
						+ " '" + brandconfig_testdrive_feedback_email_exe_sub + "',"
						+ " '" + brandconfig_testdrive_feedback_email_exe_format + "',"
						+ " '" + brandconfig_testdrive_feedback_sms_format + "',"
						+ " '" + brandconfig_testdrive_feedback_sms_exe_format + "',"

						+ " '" + brandconfig_quote_email_sub + "',"
						+ " '" + brandconfig_quote_email_format + "',"
						+ " '" + brandconfig_quote_email_exe_sub + "',"
						+ " '" + brandconfig_quote_email_exe_format + "',"
						+ " '" + brandconfig_quote_sms_format + "',"
						+ " '" + brandconfig_quote_sms_exe_format + "',"
						+ " '" + brandconfig_quote_discount_authorize_email_sub + "',"
						+ " '" + brandconfig_quote_discount_authorize_email_format + "',"
						+ " '" + brandconfig_quote_discount_authorize_sms_format + "',"

						+ " '" + brandconfig_so_email_sub + "',"
						+ " '" + brandconfig_so_email_format + "',"
						+ " '" + brandconfig_so_email_exe_sub + "',"
						+ " '" + brandconfig_so_email_exe_format + "',"

						+ " '" + brandconfig_so_delivered_email_sub + "',"
						+ " '" + brandconfig_so_delivered_email_format + "',"
						+ " '" + brandconfig_so_delivered_sms_format + "',"

						+ " '" + brandconfig_so_sms_format + "',"
						+ " '" + brandconfig_so_sms_exe_format + "',"

						+ " '" + brandconfig_preowned_email_sub + "',"
						+ " '" + brandconfig_preowned_email_format + "',"
						+ " '" + brandconfig_preowned_email_exe_sub + "',"
						+ " '" + brandconfig_preowned_email_exe_format + "',"
						+ " '" + brandconfig_preowned_sms_format + "',"
						+ " '" + brandconfig_preowned_sms_exe_format + "',"

						+ " '" + brandconfig_jc_new_email_sub + "',"
						+ " '" + brandconfig_jc_new_email_format + "',"
						+ " '" + brandconfig_jc_new_sms_format + "',"

						+ " '" + brandconfig_jc_ready_email_sub + "',"
						+ " '" + brandconfig_jc_ready_email_format + "',"
						+ " '" + brandconfig_jc_ready_sms_format + "',"

						+ " '" + brandconfig_jc_delivered_email_sub + "',"
						+ " '" + brandconfig_jc_delivered_email_format + "',"
						+ " '" + brandconfig_jc_delivered_sms_format + "',"

						+ " '" + brandconfig_jc_estimate_email_sub + "',"
						+ " '" + brandconfig_jc_estimate_email_format + "',"
						+ " '" + brandconfig_jc_estimate_sms_format + "',"

						+ " '" + brandconfig_jc_feedback_email_sub + "',"
						+ " '" + brandconfig_jc_feedback_email_format + "',"
						+ " '" + brandconfig_jc_feedback_sms_format + "',"
						// end of new fields

						+ " " + brandconfig_incentive + ","
						+ " " + brandconfig_discountauthorize + ","

						+ " '" + brandconfig_vehfollowup_notcontactable_email_sub + "',"
						+ " '" + brandconfig_vehfollowup_notcontactable_email_format + "',"
						+ " '" + brandconfig_vehfollowup_notcontactable_sms_format + "',"

						+ " '" + brandconfig_vehfollowup_dailydue_email_sub + "',"
						+ " '" + brandconfig_vehfollowup_dailydue_email_format + "',"
						+ " '" + brandconfig_vehfollowup_dailydue_sms_format + "',"

						+ " '" + brandconfig_vehfollowup_booking_email_sub + "',"
						+ " '" + brandconfig_vehfollowup_booking_email_format + "',"
						+ " '" + brandconfig_vehfollowup_booking_exe_email_sub + "',"
						+ " '" + brandconfig_vehfollowup_booking_exe_email_format + "',"
						+ " '" + brandconfig_vehfollowup_booking_sms_format + "',"
						+ " '" + brandconfig_vehfollowup_booking_exe_sms_format + "',"

						+ " '" + brandconfig_vehfollowup_serviced_email_sub + "',"
						+ " '" + brandconfig_vehfollowup_serviced_email_format + "',"
						+ " '" + brandconfig_vehfollowup_serviced_sms_format + "',"

						+ " '" + brandconfig_coupon_email_enable + "',"
						+ " '" + brandconfig_coupon_email_sub + "',"
						+ " '" + brandconfig_coupon_email_format + "',"
						+ " '" + brandconfig_coupon_sms_enable + "',"
						+ " '" + brandconfig_coupon_sms_format + "',"

						+ " '" + brandconfig_insur_enquiry_email_sub + "',"
						+ " '" + brandconfig_insur_enquiry_email_format + "',"
						+ " '" + brandconfig_insur_enquiry_exe_email_sub + "',"
						+ " '" + brandconfig_insur_enquiry_exe_email_format + "',"
						+ " '" + brandconfig_insur_enquiry_sms_format + "',"
						+ " '" + brandconfig_insur_enquiry_exe_sms_format + "',"

						+ " '" + brandconfig_socin_email_sub + "',"
						+ " '" + brandconfig_socin_email_format + "',"
						+ " '" + brandconfig_socin_sms_format + "',"

						+ " '" + brandconfig_socin_exe_email_sub + "',"
						+ " '" + brandconfig_socin_exe_email_format + "',"
						+ " '" + brandconfig_socin_exe_sms_format + "',"

						+ " '" + brandconfig_din_email_sub + "',"
						+ " '" + brandconfig_din_email_format + "',"
						+ " '" + brandconfig_din_sms_format + "',"

						+ " '" + brandconfig_receipt_email_sub + "',"
						+ " '" + brandconfig_receipt_email_format + "',"
						+ " '" + brandconfig_receipt_sms_format + "',"

						+ " '" + brandconfig_receipt_authorize_email_sub + "',"
						+ " '" + brandconfig_receipt_authorize_email_format + "',"
						+ " '" + brandconfig_receipt_authorize_sms_format + "',"

						+ " '" + brandconfig_notes + "',"
						+ " " + brandconfig_entry_id + ","
						+ " '" + ToLongDate(kknow()) + "')";
				SOP("strsql==========Add Brand Config====" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = " SELECT brandconfig_id, brand_name, brand_id, "
					+ " brandconfig_comp_id, brandconfig_brand_id, brandconfig_principal_id,"
					+ "	brandconfig_sms_enable, brandconfig_sms_url,"
					+ " brandconfig_closeenqafterdays,"
					+ " brandconfig_exestockview,"
					+ " brandconfig_allocatestock_fifo,"
					+ " brandconfig_deallocatestock_enable,"
					+ " brandconfig_deallocatestock_days, "
					+ " brandconfig_deallocatestock_amountperc,"
					+ " brandconfig_vehfollowup_days,"
					// For No Show
					+ " brandconfig_vehfollowup_noshow,"
					+ " brandconfig_vehfollowup_noshow_days,"
					+ " brandconfig_vehfollowup_noshow_futuredays,"

					+ " brandconfig_incentive,"
					+ " brandconfig_discountauthorize,"

					+ " brandconfig_vehfollowup_notcontactable_email_enable,"
					+ " brandconfig_vehfollowup_notcontactable_email_sub,"
					+ " brandconfig_vehfollowup_notcontactable_email_format,"
					+ " brandconfig_vehfollowup_notcontactable_sms_enable,"
					+ " brandconfig_vehfollowup_notcontactable_sms_format,"

					+ " brandconfig_vehfollowup_dailydue_email_enable,"
					+ " brandconfig_vehfollowup_dailydue_email_sub,"
					+ " brandconfig_vehfollowup_dailydue_email_format,"
					+ " brandconfig_vehfollowup_dailydue_sms_enable,"
					+ " brandconfig_vehfollowup_dailydue_sms_format,"

					+ " brandconfig_vehfollowup_booking_email_enable,"
					+ " brandconfig_vehfollowup_booking_email_sub,"
					+ " brandconfig_vehfollowup_booking_email_format,"
					+ " brandconfig_vehfollowup_booking_exe_email_sub,"
					+ " brandconfig_vehfollowup_booking_exe_email_format,"
					+ " brandconfig_vehfollowup_booking_sms_enable,"
					+ " brandconfig_vehfollowup_booking_sms_format,"
					+ " brandconfig_vehfollowup_booking_exe_sms_format,"

					+ " brandconfig_vehfollowup_serviced_email_enable,"
					+ " brandconfig_vehfollowup_serviced_email_sub,"
					+ " brandconfig_vehfollowup_serviced_email_format,"
					+ " brandconfig_vehfollowup_serviced_sms_enable,"
					+ " brandconfig_vehfollowup_serviced_sms_format,"

					// new fields
					+ " brandconfig_enquiry_email_enable,"
					+ " brandconfig_enquiry_email_sub,"
					+ " brandconfig_enquiry_email_format,"
					+ " brandconfig_enquiry_email_exe_enable,"
					+ " brandconfig_enquiry_email_exe_sub,"
					+ " brandconfig_enquiry_email_exe_format,"
					+ " brandconfig_enquiry_sms_enable,"
					+ " brandconfig_enquiry_sms_format,"
					+ " brandconfig_enquiry_sms_exe_enable,"
					+ " brandconfig_enquiry_sms_exe_format,"

					+ " brandconfig_enquiry_brochure_email_enable,"
					+ " brandconfig_enquiry_brochure_email_sub,"
					+ " brandconfig_enquiry_brochure_email_format,"

					+ " brandconfig_testdrive_email_enable,"
					+ " brandconfig_testdrive_email_sub,"
					+ " brandconfig_testdrive_email_format,"
					+ " brandconfig_testdrive_email_exe_enable,"
					+ " brandconfig_testdrive_email_exe_sub,"
					+ " brandconfig_testdrive_email_exe_format,"
					+ " brandconfig_testdrive_sms_enable,"
					+ " brandconfig_testdrive_sms_format,"
					+ " brandconfig_testdrive_sms_exe_enable,"
					+ " brandconfig_testdrive_sms_exe_format,"

					+ " brandconfig_testdrive_feedback_email_enable,"
					+ " brandconfig_testdrive_feedback_email_sub,"
					+ " brandconfig_testdrive_feedback_email_format,"
					+ " brandconfig_testdrive_feedback_email_exe_enable,"
					+ " brandconfig_testdrive_feedback_email_exe_sub,"
					+ " brandconfig_testdrive_feedback_email_exe_format,"
					+ " brandconfig_testdrive_feedback_sms_enable,"
					+ " brandconfig_testdrive_feedback_sms_format,"
					+ " brandconfig_testdrive_feedback_sms_exe_enable,"
					+ " brandconfig_testdrive_feedback_sms_exe_format,"

					+ " brandconfig_quote_email_enable,"
					+ " brandconfig_quote_email_sub,"
					+ " brandconfig_quote_email_format,"
					+ " brandconfig_quote_email_exe_enable,"
					+ " brandconfig_quote_email_exe_sub,"
					+ " brandconfig_quote_email_exe_format,"
					+ " brandconfig_quote_sms_enable,"
					+ " brandconfig_quote_sms_format,"
					+ " brandconfig_quote_sms_exe_enable,"
					+ " brandconfig_quote_sms_exe_format,"
					+ " brandconfig_quote_discount_authorize_email_enable,"
					+ " brandconfig_quote_discount_authorize_email_sub,"
					+ " brandconfig_quote_discount_authorize_email_format,"
					+ " brandconfig_quote_discount_authorize_sms_enable,"
					+ " brandconfig_quote_discount_authorize_sms_format,"

					+ " brandconfig_so_email_enable,"
					+ " brandconfig_so_email_sub,"
					+ " brandconfig_so_email_format,"
					+ " brandconfig_so_email_exe_enable,"
					+ " brandconfig_so_email_exe_sub,"
					+ " brandconfig_so_email_exe_format,"
					+ " brandconfig_so_sms_enable,"
					+ " brandconfig_so_sms_format,"
					+ " brandconfig_so_sms_exe_enable,"
					+ " brandconfig_so_sms_exe_format,"

					+ " brandconfig_so_delivered_email_enable,"
					+ " brandconfig_so_delivered_email_sub,"
					+ " brandconfig_so_delivered_email_format,"
					+ " brandconfig_so_delivered_sms_enable,"
					+ " brandconfig_so_delivered_sms_format,"

					+ " brandconfig_preowned_email_enable,"
					+ " brandconfig_preowned_email_sub,"
					+ " brandconfig_preowned_email_format,"
					+ " brandconfig_preowned_email_exe_enable,"
					+ " brandconfig_preowned_email_exe_sub,"
					+ " brandconfig_preowned_email_exe_format,"
					+ " brandconfig_preowned_sms_enable,"
					+ " brandconfig_preowned_sms_format,"
					+ " brandconfig_preowned_sms_exe_enable,"
					+ " brandconfig_preowned_sms_exe_format,"

					+ " brandconfig_jc_new_email_enable,"
					+ " brandconfig_jc_new_email_sub,"
					+ " brandconfig_jc_new_email_format,"
					+ " brandconfig_jc_new_sms_enable,"
					+ " brandconfig_jc_new_sms_format,"

					+ " brandconfig_jc_ready_email_enable,"
					+ " brandconfig_jc_ready_email_sub,"
					+ " brandconfig_jc_ready_email_format,"
					+ " brandconfig_jc_ready_sms_enable,"
					+ " brandconfig_jc_ready_sms_format,"

					+ " brandconfig_jc_delivered_email_enable,"
					+ " brandconfig_jc_delivered_email_sub,"
					+ " brandconfig_jc_delivered_email_format,"
					+ " brandconfig_jc_delivered_sms_enable,"
					+ " brandconfig_jc_delivered_sms_format,"

					+ " brandconfig_jc_estimate_email_enable,"
					+ " brandconfig_jc_estimate_email_sub,"
					+ " brandconfig_jc_estimate_email_format,"
					+ " brandconfig_jc_estimate_sms_enable,"
					+ " brandconfig_jc_estimate_sms_format,"

					+ " brandconfig_jc_feedback_email_enable,"
					+ " brandconfig_jc_feedback_email_sub,"
					+ " brandconfig_jc_feedback_email_format,"
					+ " brandconfig_jc_feedback_sms_enable,"
					+ " brandconfig_jc_feedback_sms_format,"

					// end of new fields

					// for brandconfig_coupon_email_enable and brandconfig_coupon_sms_enable
					+ " brandconfig_coupon_email_enable,"
					+ " brandconfig_coupon_sms_enable,"

					+ " brandconfig_insur_enquiry_email_enable,"
					+ " brandconfig_insur_enquiry_email_sub,"
					+ " brandconfig_insur_enquiry_email_format,"
					+ " brandconfig_insur_enquiry_exe_email_sub,"
					+ " brandconfig_insur_enquiry_exe_email_format,"
					+ " brandconfig_insur_enquiry_sms_enable,"
					+ " brandconfig_insur_enquiry_sms_format,"
					+ " brandconfig_insur_enquiry_exe_sms_format,"

					+ " brandconfig_socin_email_enable, "
					+ " brandconfig_socin_email_sub,"
					+ " brandconfig_socin_email_format, "
					+ " brandconfig_socin_sms_enable, "
					+ " brandconfig_socin_sms_format,"

					+ " brandconfig_socin_exe_email_enable, "
					+ " brandconfig_socin_exe_email_sub,"
					+ " brandconfig_socin_exe_email_format, "
					+ " brandconfig_socin_exe_sms_enable, "
					+ " brandconfig_socin_exe_sms_format,"

					+ " brandconfig_din_email_enable, "
					+ " brandconfig_din_email_sub,"
					+ " brandconfig_din_email_format, "
					+ " brandconfig_din_sms_enable, "
					+ " brandconfig_din_sms_format,"

					+ " brandconfig_receipt_email_enable, "
					+ " brandconfig_receipt_email_sub,"
					+ " brandconfig_receipt_email_format, "
					+ " brandconfig_receipt_sms_enable, "
					+ " brandconfig_receipt_sms_format,"

					+ " brandconfig_receipt_authorize_email_enable, "
					+ " brandconfig_receipt_authorize_email_sub,"
					+ " brandconfig_receipt_authorize_email_format, "
					+ " brandconfig_receipt_authorize_sms_enable, "
					+ " brandconfig_receipt_authorize_sms_format,"

					+ " brandconfig_notes,"
					+ " brandconfig_entry_id,"
					+ " brandconfig_entry_date,"
					+ " brandconfig_modified_id, "
					+ " brandconfig_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_brand_config"
					+ " INNER JOIN axela_brand ON brand_id = brandconfig_brand_id "
					+ " WHERE brandconfig_id = " + brandconfig_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql---PopulateFields-------" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					brandconfig_id = crs.getString("brandconfig_id");
					brandconfig_comp_id = crs.getString("brandconfig_comp_id");
					brandconfig_brand_id = crs.getString("brandconfig_brand_id");
					brandconfig_principal_id = CNumeric(crs.getString("brandconfig_principal_id"));
					brandconfig_sms_enable = crs.getString("brandconfig_sms_enable");
					brandconfig_sms_url = crs.getString("brandconfig_sms_url");
					brandconfig_close_enquiry_after_days = crs.getString("brandconfig_closeenqafterdays");
					brandconfig_exestockview = crs.getString("brandconfig_exestockview");
					brandconfig_allocatestock_fifo = crs.getString("brandconfig_allocatestock_fifo");
					brandconfig_deallocatestock_enable = crs.getString("brandconfig_deallocatestock_enable");
					brandconfig_deallocatestock_days = crs.getString("brandconfig_deallocatestock_days");
					brandconfig_deallocatestock_amountperc = crs.getString("brandconfig_deallocatestock_amountperc");
					brandconfig_vehfollowup_days = crs.getString("brandconfig_vehfollowup_days");

					brandconfig_noshow_enable = crs.getString("brandconfig_vehfollowup_noshow");
					brandconfig_noshow_days = crs.getString("brandconfig_vehfollowup_noshow_days");
					brandconfig_noshow_future_days = crs.getString("brandconfig_vehfollowup_noshow_futuredays");

					brandconfig_incentive = crs.getString("brandconfig_incentive");
					brandconfig_discountauthorize = crs.getString("brandconfig_discountauthorize");

					brandconfig_vehfollowup_notcontactable_email_enable = crs.getString("brandconfig_vehfollowup_notcontactable_email_enable");
					brandconfig_vehfollowup_notcontactable_email_sub = crs.getString("brandconfig_vehfollowup_notcontactable_email_sub");
					brandconfig_vehfollowup_notcontactable_email_format = crs.getString("brandconfig_vehfollowup_notcontactable_email_format");
					brandconfig_vehfollowup_notcontactable_sms_enable = crs.getString("brandconfig_vehfollowup_notcontactable_sms_enable");
					brandconfig_vehfollowup_notcontactable_sms_format = crs.getString("brandconfig_vehfollowup_notcontactable_sms_format");

					brandconfig_vehfollowup_dailydue_email_enable = crs.getString("brandconfig_vehfollowup_dailydue_email_enable");
					brandconfig_vehfollowup_dailydue_email_sub = crs.getString("brandconfig_vehfollowup_dailydue_email_sub");
					brandconfig_vehfollowup_dailydue_email_format = crs.getString("brandconfig_vehfollowup_dailydue_email_format");
					brandconfig_vehfollowup_dailydue_sms_enable = crs.getString("brandconfig_vehfollowup_dailydue_sms_enable");
					brandconfig_vehfollowup_dailydue_sms_format = crs.getString("brandconfig_vehfollowup_dailydue_sms_format");

					brandconfig_vehfollowup_booking_email_enable = crs.getString("brandconfig_vehfollowup_booking_email_enable");
					brandconfig_vehfollowup_booking_email_sub = crs.getString("brandconfig_vehfollowup_booking_email_sub");
					brandconfig_vehfollowup_booking_email_format = crs.getString("brandconfig_vehfollowup_booking_email_format");
					brandconfig_vehfollowup_booking_exe_email_sub = crs.getString("brandconfig_vehfollowup_booking_exe_email_sub");
					brandconfig_vehfollowup_booking_exe_email_format = crs.getString("brandconfig_vehfollowup_booking_exe_email_format");
					brandconfig_vehfollowup_booking_sms_enable = crs.getString("brandconfig_vehfollowup_booking_sms_enable");
					brandconfig_vehfollowup_booking_sms_format = crs.getString("brandconfig_vehfollowup_booking_sms_format");
					brandconfig_vehfollowup_booking_exe_sms_format = crs.getString("brandconfig_vehfollowup_booking_exe_sms_format");

					brandconfig_vehfollowup_serviced_email_enable = crs.getString("brandconfig_vehfollowup_serviced_email_enable");
					brandconfig_vehfollowup_serviced_email_sub = crs.getString("brandconfig_vehfollowup_serviced_email_sub");
					brandconfig_vehfollowup_serviced_email_format = crs.getString("brandconfig_vehfollowup_serviced_email_format");
					brandconfig_vehfollowup_serviced_sms_enable = crs.getString("brandconfig_vehfollowup_serviced_sms_enable");
					brandconfig_vehfollowup_serviced_sms_format = crs.getString("brandconfig_vehfollowup_serviced_sms_format");

					brandconfig_coupon_email_enable = crs.getString("brandconfig_coupon_email_enable");
					brandconfig_coupon_sms_enable = crs.getString("brandconfig_coupon_sms_enable");

					// new Fields
					brandconfig_enquiry_email_enable = crs.getString("brandconfig_enquiry_email_enable");
					brandconfig_enquiry_email_exe_enable = crs.getString("brandconfig_enquiry_email_exe_enable");
					brandconfig_enquiry_email_sub = crs.getString("brandconfig_enquiry_email_sub");
					brandconfig_enquiry_email_format = crs.getString("brandconfig_enquiry_email_format");
					brandconfig_enquiry_email_exe_sub = crs.getString("brandconfig_enquiry_email_exe_sub");
					brandconfig_enquiry_email_exe_format = crs.getString("brandconfig_enquiry_email_exe_format");
					brandconfig_enquiry_sms_enable = crs.getString("brandconfig_enquiry_sms_enable");
					brandconfig_enquiry_sms_format = crs.getString("brandconfig_enquiry_sms_format");
					brandconfig_enquiry_sms_exe_enable = crs.getString("brandconfig_enquiry_sms_exe_enable");
					brandconfig_enquiry_sms_exe_format = crs.getString("brandconfig_enquiry_sms_exe_format");

					brandconfig_enquiry_brochure_email_enable = crs.getString("brandconfig_enquiry_brochure_email_enable");
					brandconfig_enquiry_brochure_email_sub = crs.getString("brandconfig_enquiry_brochure_email_sub");
					brandconfig_enquiry_brochure_email_format = crs.getString("brandconfig_enquiry_brochure_email_format");

					brandconfig_testdrive_email_enable = crs.getString("brandconfig_testdrive_email_enable");
					brandconfig_testdrive_email_sub = crs.getString("brandconfig_testdrive_email_sub");
					brandconfig_testdrive_email_format = crs.getString("brandconfig_testdrive_email_format");
					brandconfig_testdrive_email_exe_enable = crs.getString("brandconfig_testdrive_email_exe_enable");
					brandconfig_testdrive_email_exe_sub = crs.getString("brandconfig_testdrive_email_exe_sub");
					brandconfig_testdrive_email_exe_format = crs.getString("brandconfig_testdrive_email_exe_format");
					brandconfig_testdrive_sms_enable = crs.getString("brandconfig_testdrive_sms_enable");
					brandconfig_testdrive_sms_format = crs.getString("brandconfig_testdrive_sms_format");
					brandconfig_testdrive_sms_exe_enable = crs.getString("brandconfig_testdrive_sms_exe_enable");
					brandconfig_testdrive_sms_exe_format = crs.getString("brandconfig_testdrive_sms_exe_format");

					brandconfig_testdrive_feedback_email_enable = crs.getString("brandconfig_testdrive_feedback_email_enable");
					brandconfig_testdrive_feedback_email_sub = crs.getString("brandconfig_testdrive_feedback_email_sub");
					brandconfig_testdrive_feedback_email_format = crs.getString("brandconfig_testdrive_feedback_email_format");
					brandconfig_testdrive_feedback_email_exe_enable = crs.getString("brandconfig_testdrive_feedback_email_exe_enable");
					brandconfig_testdrive_feedback_email_exe_sub = crs.getString("brandconfig_testdrive_feedback_email_exe_sub");
					brandconfig_testdrive_feedback_email_exe_format = crs.getString("brandconfig_testdrive_feedback_email_exe_format");
					brandconfig_testdrive_feedback_sms_enable = crs.getString("brandconfig_testdrive_feedback_sms_enable");
					brandconfig_testdrive_feedback_sms_format = crs.getString("brandconfig_testdrive_feedback_sms_format");
					brandconfig_testdrive_feedback_sms_exe_enable = crs.getString("brandconfig_testdrive_feedback_sms_exe_enable");
					brandconfig_testdrive_feedback_sms_exe_format = crs.getString("brandconfig_testdrive_feedback_sms_exe_format");

					brandconfig_quote_email_enable = crs.getString("brandconfig_quote_email_enable");
					brandconfig_quote_email_sub = crs.getString("brandconfig_quote_email_sub");
					brandconfig_quote_email_format = crs.getString("brandconfig_quote_email_format");
					brandconfig_quote_email_exe_enable = crs.getString("brandconfig_quote_email_exe_enable");
					brandconfig_quote_email_exe_sub = crs.getString("brandconfig_quote_email_exe_sub");
					brandconfig_quote_email_exe_format = crs.getString("brandconfig_quote_email_exe_format");
					brandconfig_quote_sms_enable = crs.getString("brandconfig_quote_sms_enable");
					brandconfig_quote_sms_format = crs.getString("brandconfig_quote_sms_format");
					brandconfig_quote_sms_exe_enable = crs.getString("brandconfig_quote_sms_exe_enable");
					brandconfig_quote_sms_exe_format = crs.getString("brandconfig_quote_sms_exe_format");
					brandconfig_quote_discount_authorize_email_enable = crs.getString("brandconfig_quote_discount_authorize_email_enable");
					brandconfig_quote_discount_authorize_email_sub = crs.getString("brandconfig_quote_discount_authorize_email_sub");
					brandconfig_quote_discount_authorize_email_format = crs.getString("brandconfig_quote_discount_authorize_email_format");
					brandconfig_quote_discount_authorize_sms_enable = crs.getString("brandconfig_quote_discount_authorize_sms_enable");
					brandconfig_quote_discount_authorize_sms_format = crs.getString("brandconfig_quote_discount_authorize_sms_format");

					brandconfig_so_email_enable = crs.getString("brandconfig_so_email_enable");
					brandconfig_so_email_sub = crs.getString("brandconfig_so_email_sub");
					brandconfig_so_email_format = crs.getString("brandconfig_so_email_format");
					brandconfig_so_email_exe_enable = crs.getString("brandconfig_so_email_exe_enable");
					brandconfig_so_email_exe_sub = crs.getString("brandconfig_so_email_exe_sub");
					brandconfig_so_email_exe_format = crs.getString("brandconfig_so_email_exe_format");
					brandconfig_so_sms_enable = crs.getString("brandconfig_so_sms_enable");
					brandconfig_so_sms_format = crs.getString("brandconfig_so_sms_format");
					brandconfig_so_sms_exe_enable = crs.getString("brandconfig_so_sms_exe_enable");
					brandconfig_so_sms_exe_format = crs.getString("brandconfig_so_sms_exe_format");

					brandconfig_so_delivered_email_enable = crs.getString("brandconfig_so_delivered_email_enable");
					brandconfig_so_delivered_email_sub = crs.getString("brandconfig_so_delivered_email_sub");
					brandconfig_so_delivered_email_format = crs.getString("brandconfig_so_delivered_email_format");
					brandconfig_so_delivered_sms_enable = crs.getString("brandconfig_so_delivered_sms_enable");
					brandconfig_so_delivered_sms_format = crs.getString("brandconfig_so_delivered_sms_format");

					brandconfig_preowned_email_enable = crs.getString("brandconfig_preowned_email_enable");
					brandconfig_preowned_email_sub = crs.getString("brandconfig_preowned_email_sub");
					brandconfig_preowned_email_format = crs.getString("brandconfig_preowned_email_format");
					brandconfig_preowned_email_exe_enable = crs.getString("brandconfig_preowned_email_exe_enable");
					brandconfig_preowned_email_exe_sub = crs.getString("brandconfig_preowned_email_exe_sub");
					brandconfig_preowned_email_exe_format = crs.getString("brandconfig_preowned_email_exe_format");
					brandconfig_preowned_sms_enable = crs.getString("brandconfig_preowned_sms_enable");
					brandconfig_preowned_sms_format = crs.getString("brandconfig_preowned_sms_format");
					brandconfig_preowned_sms_exe_enable = crs.getString("brandconfig_preowned_sms_exe_enable");
					brandconfig_preowned_sms_exe_format = crs.getString("brandconfig_preowned_sms_exe_format");

					brandconfig_jc_new_email_enable = crs.getString("brandconfig_jc_new_email_enable");
					brandconfig_jc_new_email_sub = crs.getString("brandconfig_jc_new_email_sub");
					brandconfig_jc_new_email_format = crs.getString("brandconfig_jc_new_email_format");
					brandconfig_jc_new_sms_enable = crs.getString("brandconfig_jc_new_sms_enable");
					brandconfig_jc_new_sms_format = crs.getString("brandconfig_jc_new_sms_format");

					brandconfig_jc_ready_email_enable = crs.getString("brandconfig_jc_ready_email_enable");
					brandconfig_jc_ready_email_sub = crs.getString("brandconfig_jc_ready_email_sub");
					brandconfig_jc_ready_email_format = crs.getString("brandconfig_jc_ready_email_format");
					brandconfig_jc_ready_sms_enable = crs.getString("brandconfig_jc_ready_sms_enable");
					brandconfig_jc_ready_sms_format = crs.getString("brandconfig_jc_ready_sms_format");

					brandconfig_jc_delivered_email_enable = crs.getString("brandconfig_jc_delivered_email_enable");
					brandconfig_jc_delivered_email_sub = crs.getString("brandconfig_jc_delivered_email_sub");
					brandconfig_jc_delivered_email_format = crs.getString("brandconfig_jc_delivered_email_format");
					brandconfig_jc_delivered_sms_enable = crs.getString("brandconfig_jc_delivered_sms_enable");
					brandconfig_jc_delivered_sms_format = crs.getString("brandconfig_jc_delivered_sms_format");

					brandconfig_jc_estimate_email_enable = crs.getString("brandconfig_jc_estimate_email_enable");
					brandconfig_jc_estimate_email_sub = crs.getString("brandconfig_jc_estimate_email_sub");
					brandconfig_jc_estimate_email_format = crs.getString("brandconfig_jc_estimate_email_format");
					brandconfig_jc_estimate_sms_enable = crs.getString("brandconfig_jc_estimate_sms_enable");
					brandconfig_jc_estimate_sms_format = crs.getString("brandconfig_jc_estimate_sms_format");

					brandconfig_jc_feedback_email_enable = crs.getString("brandconfig_jc_feedback_email_enable");
					brandconfig_jc_feedback_email_sub = crs.getString("brandconfig_jc_feedback_email_sub");
					brandconfig_jc_feedback_email_format = crs.getString("brandconfig_jc_feedback_email_format");
					brandconfig_jc_feedback_sms_enable = crs.getString("brandconfig_jc_feedback_sms_enable");
					brandconfig_jc_feedback_sms_format = crs.getString("brandconfig_jc_feedback_sms_format");
					// end of new fields

					brandconfig_insur_enquiry_email_enable = crs.getString("brandconfig_insur_enquiry_email_enable");
					brandconfig_insur_enquiry_email_sub = crs.getString("brandconfig_insur_enquiry_email_sub");
					brandconfig_insur_enquiry_email_format = crs.getString("brandconfig_insur_enquiry_email_format");
					brandconfig_insur_enquiry_exe_email_sub = crs.getString("brandconfig_insur_enquiry_exe_email_sub");
					brandconfig_insur_enquiry_exe_email_format = crs.getString("brandconfig_insur_enquiry_exe_email_format");
					brandconfig_insur_enquiry_sms_enable = crs.getString("brandconfig_insur_enquiry_sms_enable");
					brandconfig_insur_enquiry_exe_sms_format = crs.getString("brandconfig_insur_enquiry_exe_sms_format");

					brandconfig_socin_email_enable = crs.getString("brandconfig_socin_email_enable");
					brandconfig_socin_email_sub = crs.getString("brandconfig_socin_email_sub");
					brandconfig_socin_email_format = crs.getString("brandconfig_socin_email_format");
					brandconfig_socin_sms_enable = crs.getString("brandconfig_socin_sms_enable");
					brandconfig_socin_sms_format = crs.getString("brandconfig_socin_sms_format");

					brandconfig_socin_exe_email_enable = crs.getString("brandconfig_socin_exe_email_enable");
					brandconfig_socin_exe_email_sub = crs.getString("brandconfig_socin_exe_email_sub");
					brandconfig_socin_exe_email_format = crs.getString("brandconfig_socin_exe_email_format");
					brandconfig_socin_exe_sms_enable = crs.getString("brandconfig_socin_exe_sms_enable");
					brandconfig_socin_exe_sms_format = crs.getString("brandconfig_socin_exe_sms_format");

					brandconfig_din_email_enable = crs.getString("brandconfig_din_email_enable");
					brandconfig_din_email_sub = crs.getString("brandconfig_din_email_sub");
					brandconfig_din_email_format = crs.getString("brandconfig_din_email_format");
					brandconfig_din_sms_enable = crs.getString("brandconfig_din_sms_enable");
					brandconfig_din_sms_format = crs.getString("brandconfig_din_sms_format");

					brandconfig_receipt_email_enable = crs.getString("brandconfig_receipt_email_enable");
					brandconfig_receipt_email_sub = crs.getString("brandconfig_receipt_email_sub");
					brandconfig_receipt_email_format = crs.getString("brandconfig_receipt_email_format");
					brandconfig_receipt_sms_enable = crs.getString("brandconfig_receipt_sms_enable");
					brandconfig_receipt_sms_format = crs.getString("brandconfig_receipt_sms_format");

					brandconfig_receipt_authorize_email_enable = crs.getString("brandconfig_receipt_authorize_email_enable");
					brandconfig_receipt_authorize_email_sub = crs.getString("brandconfig_receipt_authorize_email_sub");
					brandconfig_receipt_authorize_email_format = crs.getString("brandconfig_receipt_authorize_email_format");
					brandconfig_receipt_authorize_sms_enable = crs.getString("brandconfig_receipt_authorize_sms_enable");
					brandconfig_receipt_authorize_sms_format = crs.getString("brandconfig_receipt_authorize_sms_format");

					brandconfig_notes = crs.getString("brandconfig_notes");
					brand_name = crs.getString("brand_name");
					brand_id = crs.getString("brand_id");
					brandconfig_entry_id = crs.getString("brandconfig_entry_id");
					if (!brandconfig_entry_id.equals("")) {
						brandconfig_entry_by = Exename(comp_id, Integer.parseInt(brandconfig_entry_id));
					}
					entry_date = strToLongDate(crs.getString("brandconfig_entry_date"));
					brandconfig_modified_id = crs.getString("brandconfig_modified_id");
					if (!brandconfig_modified_id.equals("0")) {
						brandconfig_modified_by = Exename(comp_id, Integer.parseInt(brandconfig_modified_id));
						modified_date = strToLongDate(crs.getString("brandconfig_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Brand Config!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		// Check
		CheckForm();
		if (msg.equals("")) {
			try {

				StrSql = "UPDATE " + compdb(comp_id) + "axela_brand_config"
						+ " SET"
						+ " brandconfig_comp_id = " + comp_id + ","
						+ " brandconfig_brand_id = " + brandconfig_brand_id + ","
						+ " brandconfig_principal_id = " + brandconfig_principal_id + ","
						+ " brandconfig_sms_enable = '" + brandconfig_sms_enable + "',"
						+ " brandconfig_sms_url = '" + brandconfig_sms_url + "',"

						+ " brandconfig_closeenqafterdays = " + brandconfig_close_enquiry_after_days + ","
						+ "	brandconfig_exestockview = " + brandconfig_exestockview + ","
						+ " brandconfig_allocatestock_fifo = '" + brandconfig_allocatestock_fifo + "',"
						+ " brandconfig_deallocatestock_enable = " + brandconfig_deallocatestock_enable + ","
						+ " brandconfig_deallocatestock_days =" + brandconfig_deallocatestock_days + ","
						+ " brandconfig_deallocatestock_amountperc = " + brandconfig_deallocatestock_amountperc + ","
						+ " brandconfig_vehfollowup_days = " + brandconfig_vehfollowup_days + ","

						// For No Show
						+ " brandconfig_vehfollowup_noshow = " + brandconfig_noshow_enable + ","
						+ " brandconfig_vehfollowup_noshow_days =" + brandconfig_noshow_days + ","
						+ " brandconfig_vehfollowup_noshow_futuredays = " + brandconfig_noshow_future_days + ","

						// New Fields
						+ " brandconfig_enquiry_email_enable = " + brandconfig_enquiry_email_enable + ","
						+ " brandconfig_enquiry_sms_enable = " + brandconfig_enquiry_sms_enable + ","
						+ " brandconfig_enquiry_email_exe_enable = " + brandconfig_enquiry_email_enable + ","
						+ " brandconfig_enquiry_sms_exe_enable = " + brandconfig_enquiry_sms_enable + ","

						+ " brandconfig_enquiry_brochure_email_enable = " + brandconfig_enquiry_brochure_email_enable + ","

						+ " brandconfig_testdrive_email_enable = " + brandconfig_testdrive_email_enable + ","
						+ " brandconfig_testdrive_sms_enable = " + brandconfig_testdrive_sms_enable + ","
						+ " brandconfig_testdrive_email_exe_enable = " + brandconfig_testdrive_email_enable + ","
						+ " brandconfig_testdrive_sms_exe_enable = " + brandconfig_testdrive_sms_enable + ","

						+ " brandconfig_testdrive_feedback_email_enable = " + brandconfig_testdrive_feedback_email_enable + ","
						+ " brandconfig_testdrive_feedback_sms_enable = " + brandconfig_testdrive_feedback_sms_enable + ","
						+ " brandconfig_testdrive_feedback_email_exe_enable = " + brandconfig_testdrive_feedback_email_enable + ","
						+ " brandconfig_testdrive_feedback_sms_exe_enable = " + brandconfig_testdrive_feedback_sms_enable + ","

						+ " brandconfig_quote_email_enable = " + brandconfig_quote_email_enable + ","
						+ " brandconfig_quote_sms_enable = " + brandconfig_quote_sms_enable + ","
						+ " brandconfig_quote_email_exe_enable = " + brandconfig_quote_email_enable + ","
						+ " brandconfig_quote_sms_exe_enable = " + brandconfig_quote_sms_enable + ","
						+ " brandconfig_quote_discount_authorize_email_enable = " + brandconfig_quote_discount_authorize_email_enable + ","
						+ " brandconfig_quote_discount_authorize_sms_enable = " + brandconfig_quote_discount_authorize_sms_enable + ","

						+ " brandconfig_so_email_enable = " + brandconfig_so_email_enable + ","
						+ " brandconfig_so_sms_enable = " + brandconfig_so_sms_enable + ","
						+ " brandconfig_so_email_exe_enable = " + brandconfig_so_email_enable + ","
						+ " brandconfig_so_sms_exe_enable = " + brandconfig_so_sms_enable + ","

						+ " brandconfig_so_delivered_email_enable = " + brandconfig_so_delivered_email_enable + ","
						+ " brandconfig_so_delivered_sms_enable = " + brandconfig_so_delivered_sms_enable + ","

						+ " brandconfig_preowned_email_enable = " + brandconfig_preowned_email_enable + ","
						+ " brandconfig_preowned_sms_enable = " + brandconfig_preowned_sms_enable + ","
						+ " brandconfig_preowned_email_exe_enable = " + brandconfig_preowned_email_enable + ","
						+ " brandconfig_preowned_sms_exe_enable = " + brandconfig_preowned_sms_enable + ","

						+ " brandconfig_jc_new_email_enable = " + brandconfig_jc_new_email_enable + ","
						+ " brandconfig_jc_new_sms_enable = " + brandconfig_jc_new_sms_enable + ","

						+ " brandconfig_jc_ready_email_enable = " + brandconfig_jc_ready_email_enable + ","
						+ " brandconfig_jc_ready_sms_enable = " + brandconfig_jc_ready_sms_enable + ","

						+ " brandconfig_jc_delivered_email_enable = " + brandconfig_jc_delivered_email_enable + ","
						+ " brandconfig_jc_delivered_sms_enable = " + brandconfig_jc_delivered_sms_enable + ","

						+ " brandconfig_jc_estimate_email_enable = " + brandconfig_jc_estimate_email_enable + ","
						+ " brandconfig_jc_estimate_sms_enable = " + brandconfig_jc_estimate_sms_enable + ","

						+ " brandconfig_jc_feedback_email_enable = " + brandconfig_jc_feedback_email_enable + ","
						+ " brandconfig_jc_feedback_sms_enable = " + brandconfig_jc_feedback_sms_enable + ","
						// End of new fields

						+ " brandconfig_incentive = " + brandconfig_incentive + ","
						+ " brandconfig_discountauthorize = " + brandconfig_discountauthorize + ","

						+ " brandconfig_vehfollowup_notcontactable_email_enable = " + brandconfig_vehfollowup_notcontactable_email_enable + ","
						+ " brandconfig_vehfollowup_notcontactable_sms_enable = " + brandconfig_vehfollowup_notcontactable_sms_enable + ","

						+ " brandconfig_vehfollowup_dailydue_email_enable = " + brandconfig_vehfollowup_dailydue_email_enable + ","
						+ " brandconfig_vehfollowup_dailydue_sms_enable = " + brandconfig_vehfollowup_dailydue_sms_enable + ","

						+ " brandconfig_vehfollowup_booking_email_enable =" + brandconfig_vehfollowup_booking_email_enable + ","
						+ " brandconfig_vehfollowup_booking_sms_enable =" + brandconfig_vehfollowup_booking_sms_enable + ","

						+ " brandconfig_vehfollowup_serviced_email_enable = " + brandconfig_vehfollowup_serviced_email_enable + ","
						+ " brandconfig_vehfollowup_serviced_sms_enable = " + brandconfig_vehfollowup_serviced_sms_enable + ","

						+ " brandconfig_coupon_email_enable = " + brandconfig_coupon_email_enable + ","
						+ " brandconfig_coupon_sms_enable = " + brandconfig_coupon_sms_enable + ","

						+ " brandconfig_insur_enquiry_email_enable = " + brandconfig_insur_enquiry_email_enable + ","
						+ " brandconfig_insur_enquiry_sms_enable = " + brandconfig_insur_enquiry_sms_enable + ","

						+ " brandconfig_socin_email_enable = " + brandconfig_socin_email_enable + ","
						+ " brandconfig_socin_sms_enable = " + brandconfig_socin_sms_enable + ","

						+ " brandconfig_socin_exe_email_enable = " + brandconfig_socin_exe_email_enable + ","
						+ " brandconfig_socin_exe_sms_enable = " + brandconfig_socin_exe_sms_enable + ","

						+ " brandconfig_din_email_enable = " + brandconfig_din_email_enable + ","
						+ " brandconfig_din_sms_enable = " + brandconfig_din_sms_enable + ","

						+ " brandconfig_receipt_email_enable = " + brandconfig_receipt_email_enable + ","
						+ " brandconfig_receipt_sms_enable = " + brandconfig_receipt_sms_enable + ","

						+ " brandconfig_receipt_authorize_email_enable = " + brandconfig_receipt_authorize_email_enable + ","
						+ " brandconfig_receipt_authorize_sms_enable = " + brandconfig_receipt_authorize_sms_enable + ","

						+ " brandconfig_notes = '" + brandconfig_notes + "',"
						+ " brandconfig_modified_id = " + brandconfig_modified_id + ","
						+ " brandconfig_modified_date = '" + brandconfig_modified_date + "'"
						+ " WHERE brandconfig_id = " + brandconfig_id + "";
				// SOP("StrSql----------update-------" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			if (msg.equals("")) {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_brand_config"
						+ " WHERE brandconfig_id = " + brandconfig_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), brandconfig_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStockPreference(String stockpreference_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", stockpreference_id)).append(">Random</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", stockpreference_id)).append(">FIFO</option>");

		return Str.toString();
	}

	public String PopulateExeStockView(String exestockview_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", exestockview_id)).append(">By Region</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", exestockview_id)).append(">By All Regions</option>");

		return Str.toString();
	}
}
