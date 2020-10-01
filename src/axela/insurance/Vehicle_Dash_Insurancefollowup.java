////////////Divya 4th april 2013
package axela.insurance;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.customer.Customer_Tags_Check;
import cloudify.connect.Connect;

public class Vehicle_Dash_Insurancefollowup extends Connect {

	public String StrSql = "";
	public String StrCustomerDetails = "";
	public String msg = "", contact_msg = "", list_insurfollowup_msg = "", insur_msg = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	// public String BranchAccess = "";
	// public String ExeAccess = "";
	public String branch_id = "0";
	public String branch_brand_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_name = "";
	public String contact_title_id = "0", new_contact_title_id = "1";
	public String item_model_id = "0";
	public String veh_branch_id = "0";
	public String model_name = "";
	public String contactdetails = "";
	public String modal = "";
	public String item_name = "";
	public String customer_edit_perm = "0";
	public String veh_customer_id = "0";
	public String contact_id = "0";
	public String veh_customer_name = "";
	public String link_contact_name = "";
	public String contact_name = "";
	public String contact_fname = "", new_contact_fname = "";
	public String new_contact_lname = "", contact_jobtitle = "";
	public String contact_mobile1 = "", new_contact_mobile1 = "";
	public String new_contact_contacttype_id = "7";
	// public String new_phonetype_id = "0";
	public String contact_mobile2 = "", new_contact_mobile2 = "", contact_mobile3 = "", new_contact_mobile3 = "";
	public String contact_mobile4 = "", new_contact_mobile4 = "", contact_mobile5 = "", new_contact_mobile5 = "", contact_mobile6 = "", new_contact_mobile6 = "";
	public String contact_mobile1_phonetype_id = "0";
	public String contact_mobile2_phonetype_id = "0";
	public String contact_mobile3_phonetype_id = "0";
	public String contact_mobile4_phonetype_id = "0";
	public String contact_mobile5_phonetype_id = "0";
	public String contact_mobile6_phonetype_id = "0";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_email1 = "", contact_email2 = "", new_contact_email1 = "";
	public String contact_address = "";
	public String new_contact_notes = "";
	public String contact_city_id = "0";
	public String contact_pin = "";
	public String insurenquiry_id = "0";
	public String veh_name = "0";
	public String customer_info = "";
	public String ownership_info = "";
	public String customerdetail = "";
	public String insurcustomerdetail = "";
	public String call_info = "";
	public String jobcard_info = "";
	public String invoice_info = "";
	public String receipt_info = "";
	public String insurance_info = "";
	public String followup_info = "";
	public String history = "";
	public String option = "", vehfollowup_time = "";
	public String veh_engine_no = "";
	public String veh_reg_no = "";
	public String veh_sale_amount = "";
	public String veh_chassis_no = "";
	public String link_so_name = "";
	public String veh_item_id = "0";
	public String veh_modelyear = "";
	public String veh_kms = "";
	public String lostcase_reason = "0";
	public String veh_sale_date = "", veh_insursource_id = "0";
	public String veh_warranty_expirydate = "";
	public String vehwarrantyexpirydate = "";
	public String veh_notes = "";
	public String veh_entry_id = "0";
	public String veh_entry_date = "";
	public String veh_entry_by = "";
	public String veh_modified_id = "0";
	public String veh_modified_by = "";
	public String veh_modified_date = "";
	public String veh_service_duekms = "";
	public String veh_service_duedate = "";
	public String veh_lastservice = "";
	public String veh_lastservice_kms = "0";
	public String veh_insur_date = "";
	public String veh_renewal_date = "";
	public String location_id = "0";
	public String ext_id = "0";
	public String int_id = "0";
	public String insurlostcase1_id = "0";
	public String insurfollowup_desc = "";
	public String currentfollowuptime = "";
	public String insurfollowup_time = "";
	public String veh_emp_id = "0";
	public String veh_crmemp_id = "0";
	public String veh_insuremp_id = "0", veh_vehsource_id = "0";
	public String insurfollowup_followuptype_id = "0", insurpolicy_field_emp_id = "0";
	public String insurfollowup_priorityinsurfollowup_id = "0";
	public String insurfollowup_insuraction_id = "0";
	public String insurfollowup_entry_id = "0";
	public String insurfollowup_entry_time = "0";
	public String currenttimevalidate = "";
	public String insurfollowup_id = "0";
	public String vehfollowup_id = "0";
	public String vehfollowup_pickupdriver_emp_id = "0", vehfollowupaddress1 = "Jayamahal";
	public String brandconfig_vehfollowup_notcontactable_email_enable = "0";
	public String brandconfig_vehfollowup_notcontactable_email_sub = "";
	public String brandconfig_vehfollowup_notcontactable_email_format = "";
	public String brandconfig_vehfollowup_notcontactable_sms_enable = "0";
	public String brandconfig_vehfollowup_notcontactable_sms_format = "";
	public String brandconfig_vehfollowup_booking_email_enable = "0";
	public String brandconfig_vehfollowup_booking_email_sub = "";
	public String brandconfig_vehfollowup_booking_email_format = "";
	public String brandconfig_vehfollowup_booking_sms_enable = "0";
	public String brandconfig_vehfollowup_booking_sms_format = "";

	public String insurfollowup_psffeedbacktype_id = "0";
	public String insurfollowup_insur_field_emp_id = "0";
	public String vehfollowup_emp_id = "0";
	public String vehfollowup_entry_id = "0";
	public String vehfollowup_entry_time = "0";
	public String status = "";
	public String submitB = "";
	public String addContactB = "";
	public String delete = "";
	public String loaddata = "yes";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String comp_sms_enable = "";
	public String comp_email_enable = "";
	public String branch_email1 = "";
	public String branch_insur_lost_sms_format = "";
	public String contact_lname = "", customer_display = "", title_desc = "";
	public String customer_id = "0";
	public String customer_name = "", link_customer_name = "";
	public String insurpolicy_contact_id = "0";
	public String emp_email1 = "";
	public String branch_insur_new_email_enable = "0";
	public String branch_insur_new_email_format = "";
	public String branch_insur_new_email_sub = "";
	public String branch_insur_new_sms_enable = "0";

	public String branch_insur_lost_email_enable = "0";
	public String branch_insur_lost_email_format = "";
	public String branch_insur_lost_email_sub = "";
	public String branch_insur_lost_sms_enable = "0";
	public String emp_name = "", emp_email2 = "", emp_email_formail = "", emp_mobile1 = "", emp_mobile2 = "";
	public String jobtitle_desc = "", field_emp_name = "", emp_phone1;
	public String veh_contact_id = "0";
	public String vehfollowup_email_to = "", vehfollowup_sms_to = "";
	public int PageCurrent = 0;
	public String RecCountDisplay = "";
	public int PageCount = 10, recperpage = 0;
	public String PageNaviStr = "";
	public String docmsg = "";
	public String StrSearch = "", service = "", insurance = "";
	// Insurance Follow-Up
	public String insurfollowup_contactable_id = "0";
	public String disposition_id = "0", dr_inspection_id = "0", dr_inspection_result_id = "0", dr_appoint_verification_id = "0";
	public String txt_nextfollowup_time = "", dr_nextfollowup_type = "0", dr_field_exe_id = "0", txt_remarks = "";
	public String addInsurFollowup = "", listfollowup_info = "", veh_insurtype_id = "", veh_insur_notinterested_id = "";
	public String[] dr_offer_id;
	String insur_offers = "";
	public String veh_insur_previouscompname = "";
	public String veh_insur_previousyearidv = "";
	public String veh_insur_previousgrosspremium = "";
	public String veh_insur_previousplanname = "";
	public String veh_insur_policyexpirydate = "";
	public String veh_insur_currentidv = "";
	public String veh_insur_premium = "";
	public String veh_insur_premiumwithzerodept = "";
	public String veh_insur_compoffered = "";
	public String veh_insur_plansuggested = "", veh_insur_name = "", veh_insur_variant = "", veh_insur_reg_no = "", veh_insur_chassis_no = "";
	public String veh_insur_ncb = "", veh_insur_address = "";

	DecimalFormat df = new DecimalFormat("0.00");

	public String veh_enquiry_id = "0";
	public Vehicle_Check vehicle = new Vehicle_Check();

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();
	public MIS_Check mischeck = new MIS_Check();
	public Vehicle_Dash vehDash = new Vehicle_Dash();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_insurance_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				service = PadQuotes(request.getParameter("service"));
				insurance = PadQuotes(request.getParameter("insurance"));
				list_insurfollowup_msg = PadQuotes(request.getParameter("insurfollowup_msg"));
				disposition_id = PadQuotes(request.getParameter("dr_disposition_id"));
				insurfollowup_contactable_id = PadQuotes(request.getParameter("dr_insurfollowup_contactable_id"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				modal = PadQuotes(request.getParameter("modal"));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				SOP("insurenquiry_id===" + insurenquiry_id);
				insurfollowup_id = PadQuotes(request.getParameter("insurfollowup_id"));
				vehfollowup_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				addContactB = PadQuotes(request.getParameter("add_contact_button"));
				currenttimevalidate = ToLongDate(kknow());
				addInsurFollowup = PadQuotes(request.getParameter("add_insurfollowup_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				insurcustomerdetail = ListCustomerDetails(comp_id, insurenquiry_id, "");
				GetInsurDetails(insurenquiry_id, comp_id);
				followup_info = ListInsuranceFollowup(comp_id, insurenquiry_id);
				if (delete.equals("yes") && !insurfollowup_id.equals("0") && emp_id.equals("1")) {
					DeleteInsurFollowupFields();
					loaddata = "";
					if (service.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
					}
				} else if (addInsurFollowup.equals("Add")) {
					insurfollowup_contactable_id = CNumeric(PadQuotes(request.getParameter("dr_insurfollowup_contactable_id")));
					disposition_id = CNumeric(PadQuotes(request.getParameter("dr_disposition_id")));
					veh_insur_notinterested_id = CNumeric(PadQuotes(request.getParameter("dr_reason_id")));
					dr_inspection_id = CNumeric(PadQuotes(request.getParameter("dr_inspection_id")));
					dr_inspection_result_id = CNumeric(PadQuotes(request.getParameter("dr_inspection_result_id")));
					// dr_offer_id = PadQuotes(request.getParameter("dr_offer_id"));
					dr_offer_id = request.getParameterValues("dr_offer_id");
					if (dr_offer_id != null) {
						for (int i = 0; i < dr_offer_id.length; i++) {
							SOP("i===" + i);
							insur_offers += dr_offer_id[i] + ",";
						}
						insur_offers = insur_offers.substring(0, insur_offers.length() - 1);
					}

					// SOP("dr_offer_id-----" + insur_offers);

					dr_appoint_verification_id = CNumeric(PadQuotes(request.getParameter("dr_appoint_verification_id")));
					txt_nextfollowup_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));
					dr_nextfollowup_type = PadQuotes(request.getParameter("dr_insurfollowup_type"));
					insurpolicy_field_emp_id = CNumeric(PadQuotes(request.getParameter("dr_field_emp_id")));
					txt_remarks = PadQuotes(request.getParameter("txt_insurfollowup_desc"));
					txt_remarks = txt_remarks.trim();
					SOP("txt_nextfollowup_time===" + txt_nextfollowup_time);
					InsurCheckForm();
					SOP("txt_nextfollowup_time===" + txt_nextfollowup_time);
					if (insur_msg.equals("")) {
						insurfollowup_entry_id = emp_id;
						insurfollowup_entry_time = ToLongDate(kknow());
						// SOP("insurfollowup_entry_time====" + insurfollowup_entry_time);
						AddInsurFollowup();
						loaddata = "";
						if (service.equals("yes")) {
							response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
						} else {
							response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
						}
						// list_insurfollowup_msg = "Insurance Followup Added Successfully!";
					} else {
						insur_msg = "Error!" + insur_msg;
						// response.sendRedirect(response.encodeRedirectURL("../insurance/vehicle-dash.jsp?insurenquiry_id=" + insurenquiry_id + "&insur_msg=Insurance Followup Added Successfully!"
						// + "#tabs-10"));
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

	public String GetInsurDetails(String insurenquiry_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT  COALESCE(insurenquiry_previouscompname,'') AS insurenquiry_previouscompname,"
				+ " COALESCE(insurenquiry_previousyearidv,'') AS insurenquiry_previousyearidv,"
				+ " COALESCE(insurenquiry_previousgrosspremium,'') AS insurenquiry_previousgrosspremium,"
				+ " COALESCE(insurenquiry_previousplanname,'') AS insurenquiry_previousplanname,"
				+ " COALESCE(insurenquiry_policyexpirydate,'') AS insurenquiry_policyexpirydate,"
				+ " COALESCE(insurenquiry_currentidv,'') AS insurenquiry_currentidv,"
				+ " COALESCE(insurenquiry_premium,'') AS insurenquiry_premium,"
				+ " COALESCE(insurenquiry_premiumwithzerodept,'') AS insurenquiry_premiumwithzerodept,"
				+ " COALESCE(insurenquiry_compoffered,'') AS insurenquiry_compoffered,"
				+ " COALESCE(insurenquiry_plansuggested,'') AS insurenquiry_plansuggested,"
				+ " CONCAT(	title_desc,' ', contact_fname,' ',contact_lname	) AS veh_contactname,"
				+ " COALESCE(insurenquiry_variant,'') AS insurenquiry_variant,"
				+ " COALESCE(insurenquiry_ncb,'') AS insurenquiry_ncb,"
				+ " COALESCE(insurenquiry_reg_no,'') AS insurenquiry_reg_no,"
				+ " COALESCE(insurenquiry_chassis_no,'') AS insurenquiry_chassis_no,"
				+ " IF(insurenquiry_address='',CONCAT(contact_address,', ',city_name,'-',contact_pin),insurenquiry_address) AS insurenquiry_address"
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id "
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
				+ " WHERE 1 = 1"
				+ " AND insurenquiry_id = " + insurenquiry_id;
		SOP("StrSql==GetInsurDetails==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				veh_insur_previouscompname = crs.getString("insurenquiry_previouscompname");
				veh_insur_previousyearidv = crs.getString("insurenquiry_previousyearidv");
				veh_insur_previousgrosspremium = crs.getString("insurenquiry_previousgrosspremium");
				veh_insur_previousplanname = crs.getString("insurenquiry_previousplanname");
				veh_insur_policyexpirydate = strToShortDate(crs.getString("insurenquiry_policyexpirydate"));
				veh_insur_currentidv = crs.getString("insurenquiry_currentidv");
				veh_insur_premium = crs.getString("insurenquiry_premium");
				veh_insur_premiumwithzerodept = crs.getString("insurenquiry_premiumwithzerodept");
				veh_insur_compoffered = crs.getString("insurenquiry_compoffered");
				veh_insur_plansuggested = crs.getString("insurenquiry_plansuggested");
				veh_insur_name = crs.getString("veh_contactname");
				veh_insur_variant = crs.getString("insurenquiry_variant");
				veh_insur_ncb = crs.getString("insurenquiry_ncb");
				veh_insur_reg_no = crs.getString("insurenquiry_reg_no");
				veh_insur_chassis_no = crs.getString("insurenquiry_chassis_no");
				veh_insur_address = crs.getString("insurenquiry_address");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// Insurance

	public String ListInsuranceFollowup(String comp_id, String insurenquiry_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT insurfollowup_id,"
				+ " insurenquiry_id,"
				+ " insurfollowup_followup_time,"
				+ " insurfollowup_nextfollowup_time,"
				+ " insurfollowup_contactable_id,"
				+ " insurfollowup_desc, insurfollowup_entry_id,"
				+ " insurfollowup_entry_time,"
				+ " COALESCE(insurdisposition_name,'') AS insurdisposition_name, "
				+ " COALESCE(notinterestedreason_name,'') AS notinterestedreason_name, "
				+ " COALESCE(insurinspection_name, '') AS insurinspection_name,"
				+ " COALESCE(insurinspectionresult_name, '') AS insurinspectionresult_name,"
				+ " COALESCE((SELECT GROUP_CONCAT(' ',insuroffer_name) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_offer "
				+ " WHERE FIND_IN_SET(insuroffer_id,insurfollowup_offer_id)), '') AS insuroffer_name,"
				+ " COALESCE(insurapptvarification_name, '') AS insurapptvarification_name,"
				+ " COALESCE(followuptype_name,'') AS followuptype_name,"
				+ " COALESCE(e.emp_id,0)AS emp_id1,"
				+ " COALESCE(CONCAT(e.emp_name, ' (', e.emp_ref_no, ')'),'') AS emp_name,"
				+ " COALESCE(field.emp_id,0) AS emp_id2,"
				+ " COALESCE(CONCAT(field.emp_name, ' (', field.emp_ref_no, ')'),'') AS field_emp_name,"
				+ " COALESCE(insurfollowup_modified_id,0) AS insurfollowup_modified_id,"
				+ " COALESCE(insurfollowup_modified_time,0) AS insurfollowup_modified_time"
				+ " FROM " + compdb(comp_id) + "axela_insurance_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurfollowup_insurenquiry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = insurenquiry_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_followup_type ON followuptype_id = insurfollowup_followuptype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp field ON field.emp_id = insurfollowup_insur_field_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_disposition ON insurdisposition_id = insurfollowup_disposition_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_notinterestedreason ON notinterestedreason_id = insurfollowup_notinterestedreason_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_inspection ON insurinspection_id = insurfollowup_inspection_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_inspection_result ON insurinspectionresult_id = insurfollowup_inspectionresult_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_appt_verification ON insurapptvarification_id = insurfollowup_appointmentverification_id"
				+ " WHERE insurenquiry_id = " + insurenquiry_id
				+ " ORDER BY insurfollowup_id DESC";
		SOP("Str====ListInsuranceFollowup==== " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("<th data-hide=\"phone\">Contactable</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition</th>\n");
				Str.append("<th data-hide=\"phone\">Reason</th>\n");
				Str.append("<th data-hide=\"phone\">Inspection</th>\n");
				Str.append("<th data-hide=\"phone\">Inspection Result</th>\n");
				Str.append("<th data-hide=\"phone\">Offer/Commitment </th>\n");
				Str.append("<th data-hide=\"phone\">Appointment Verification</th>\n");
				Str.append("<th data-hide=\"phone\">Follow-up Description</th>\n");
				Str.append("<th data-hide=\"phone\">Follow-up Type</th>\n");
				Str.append("<th data-hide=\"phone\">Next Follow-up Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Field Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Feedback By</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Entry By</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("insurfollowup_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br/><a href=\"../insurance/vehicle-dash-insurancefollowup.jsp?");
						if (service.equals("yes")) {
							Str.append("service=yes&");
						}
						Str.append("Delete=yes&insurenquiry_id=").append(crs.getString("insurenquiry_id")).append("&insurfollowup_id=");
						Str.append(crs.getString("insurfollowup_id")).append("\">Delete Follow-up</a>");
					}
					Str.append("</td>\n");

					Str.append("<td valign=\"top\" align=\"left\">");
					if (crs.getString("insurfollowup_contactable_id").equals("1")) {
						Str.append("Contactable");
					}
					else if (crs.getString("insurfollowup_contactable_id").equals("2")) {
						Str.append("Not Contactable");
					}
					Str.append("</td>\n");

					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurdisposition_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("notinterestedreason_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurinspection_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurinspectionresult_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insuroffer_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurapptvarification_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurfollowup_desc")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("followuptype_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(strToLongDate(crs.getString("insurfollowup_nextfollowup_time"))).append("</td>\n");
					Str.append("<td valign=\"top\"><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id1")).append("\">");
					Str.append(crs.getString("emp_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td valign=\"top\"><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id2")).append("\">");
					Str.append(crs.getString("field_emp_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					if (!crs.getString("insurfollowup_modified_id").equals("0")) {
						Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurfollowup_modified_id"))));
						Str.append("<br>").append(strToLongDate(crs.getString("insurfollowup_modified_time")));
					}
					Str.append("&nbsp;</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					if (!crs.getString("insurfollowup_entry_id").equals("0")) {
						Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurfollowup_entry_id"))));
						Str.append("<br>").append(strToLongDate(crs.getString("insurfollowup_entry_time")));
					}
					Str.append("&nbsp;</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				// Str.append("<br><br><br><br><center><font color=\"red\"><b>No Follow-up found!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListCustomerDetails(String comp_id, String insurenquiry_id, String type)
	{
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT customer_id, customer_name, contact_id, "
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, "
					+ " contact_mobile1, contact_mobile2, "
					+ " variant_name,"
					+ " insurenquiry_sale_date, insurenquiry_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE insurenquiry_id = " + insurenquiry_id
					+ " GROUP BY insurenquiry_id";
			// SOP("StrSql-----3111-- " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<center>");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");

				// Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs.next()) {
					Str.append("<tr align=center>\n");
					Str.append("<td >Customer: <a href=\"../customer/customer-list.jsp?customer_id=")
							.append(crs.getString("customer_id")).append(" \">")
							.append(crs.getString("customer_name")).append(" (")
							.append(crs.getString("customer_id")).append(")</td>\n");
					Str.append("<td>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=")
							.append(crs.getString("contact_id")).append(" \">")
							.append(crs.getString("contacts")).append("</a></td>\n");
					// Str.append("<td>Mobile: ").append(crs.getString("contact_mobile1"))
					// .append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("</td>\n");
					Str.append("<td>Variant: ").append(crs.getString("variant_name")).append("</td>\n");

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<tr align=center><td>Mobile 1: ").append(crs.getString("contact_mobile1"));
						Str.append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<td>Mobile 2: ").append(crs.getString("contact_mobile2"));
						Str.append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}

					// Str.append("</td>");
					Str.append("</tr>\n");

				}
				Str.append("</table></div></center>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void DeleteInsurFollowupFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
					+ " WHERE insurfollowup_id = " + insurfollowup_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateFieldExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS fieldemp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_fieldinsur = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), insurpolicy_field_emp_id));
				Str.append(">").append(crs.getString("fieldemp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurContactable(String comp_id, String insurfollowup_contactable_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"1\" " + Selectdrop(1, insurfollowup_contactable_id) + ">Yes</option>");
			Str.append("<option value=\"2\" " + Selectdrop(2, insurfollowup_contactable_id) + ">No</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInspection(String comp_id, String dr_inspection_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurinspection_id, insurinspection_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_inspection"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurinspection_id"
					+ " ORDER BY insurinspection_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurinspection_id"));
				Str.append(StrSelectdrop(crs.getString("insurinspection_id"), dr_inspection_id));
				Str.append(">").append(crs.getString("insurinspection_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInspectionResult(String comp_id, String dr_inspection_result_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurinspectionresult_id,	insurinspectionresult_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_inspection_result"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurinspectionresult_id"
					+ " ORDER BY insurinspectionresult_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurinspectionresult_id"));
				Str.append(StrSelectdrop(crs.getString("insurinspectionresult_id"), dr_inspection_result_id));
				Str.append(">").append(crs.getString("insurinspectionresult_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateOffer(String comp_id, String[] dr_offer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insuroffer_id, insuroffer_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_offer"
					+ " WHERE	1 = 1"
					+ " GROUP BY insuroffer_id"
					+ " ORDER BY insuroffer_name";
			SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<input type=\"checkbox\" id=\"dr_offer_id\" name=\"dr_offer_id\" value='").append(crs.getString("insuroffer_id")).append("'");
				Str.append(ArrSelectCheck(crs.getInt("insuroffer_id"), dr_offer_id));
				Str.append(">").append(crs.getString("insuroffer_name")).append("&nbsp;&nbsp;");
			}
			SOP("Str===" + Str.toString());

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAppointVerification(String comp_id, String dr_appoint_verification_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurapptvarification_id, insurapptvarification_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_appt_verification"
					+ " WHERE 1 = 1"
					+ " GROUP BY insurapptvarification_id"
					+ " ORDER BY insurapptvarification_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurapptvarification_id"));
				Str.append(StrSelectdrop(crs.getString("insurapptvarification_id"), dr_appoint_verification_id));
				Str.append(">").append(crs.getString("insurapptvarification_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void InsurCheckForm() {
		insur_msg = "";

		if (insurfollowup_contactable_id.equals("0")) {
			insur_msg += "<br>Select Contactable!";
		}
		if (disposition_id.equals("0")) {
			insur_msg += "<br>Select Disposition!";
		}
		if (disposition_id.equals("4") && dr_inspection_id.equals("0")) {
			insur_msg += "<br>Select Inspection!";
		}
		if (!disposition_id.equals("5") && !disposition_id.equals("11") && txt_nextfollowup_time.equals("")) {
			insur_msg += "<br>Select Next Follow-up Time!";
		}
		else if (!txt_nextfollowup_time.equals("")) {
			if (Long.parseLong(ConvertLongDateToStr(txt_nextfollowup_time).substring(0, 12)) <= Long.parseLong(((ToLongDate(kknow()).substring(0, 12))))) {
				insur_msg += "<br>Next Follow-up Time should be greater than Today's Date and Time!";
			}
		}
		if (!disposition_id.equals("5") && !disposition_id.equals("11") && dr_nextfollowup_type.equals("0")) {
			insur_msg += "<br>Select Next Follow-up Type!";
		}
		if (!disposition_id.equals("4")) {
			dr_inspection_id = "0";
			dr_inspection_result_id = "0";
			dr_appoint_verification_id = "0";
		}

		if (disposition_id.equals("11")) {
			dr_inspection_id = "0";
			dr_inspection_result_id = "0";
			// dr_offer_id = ;
			dr_appoint_verification_id = "0";
			txt_nextfollowup_time = "";
			dr_nextfollowup_type = "0";
			insurpolicy_field_emp_id = "0";
		}

		if (txt_remarks.equals("")) {
			insur_msg += "<br>Remarks cannot be empty!";
		}
	}

	public void AddInsurFollowup() throws Exception {
		SOP("coming.....");
		if (insur_msg.equals("")) {
			StrSql = "SELECT insurfollowup_id FROM " + compdb(comp_id) + " axela_insurance_followup"
					+ " WHERE insurfollowup_desc = '' "
					+ " AND insurfollowup_insurenquiry_id = " + insurenquiry_id;

			if (!ExecuteQuery(StrSql).equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_followup"
						+ " SET"
						+ " insurfollowup_contactable_id = " + insurfollowup_contactable_id + ","
						+ " insurfollowup_disposition_id = " + disposition_id + ","
						+ "	insurfollowup_inspection_id = " + dr_inspection_id + ","
						+ " insurfollowup_inspectionresult_id = " + dr_inspection_result_id + ","
						+ " insurfollowup_offer_id = '" + insur_offers + "',"
						+ " insurfollowup_appointmentverification_id = " + dr_appoint_verification_id + ","
						// + " insurfollowup_insur_field_emp_id = " + insurpolicy_field_emp_id + ","
						+ " insurfollowup_desc = '" + txt_remarks + "',"
						+ " insurfollowup_modified_id = " + emp_id + ","
						+ " insurfollowup_modified_time = '" + ToLongDate(kknow()) + "'"
						+ " WHERE 1=1 "
						+ " AND insurfollowup_desc = ''"
						+ " AND insurfollowup_insurenquiry_id = " + insurenquiry_id + "";
				SOP("StrSql==update followup==" + StrSql);
				updateQuery(StrSql);
			}
			else {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
						+ " (insurfollowup_insurenquiry_id,"
						// + " insurfollowup_emp_id,"
						+ " insurfollowup_followup_time,"
						+ " insurfollowup_nextfollowup_time,"
						+ " insurfollowup_contactable_id, "
						// + " insurfollowup_followuptype_id,"
						+ " insurfollowup_disposition_id ,"
						+ " insurfollowup_notinterestedreason_id ,"
						+ " insurfollowup_inspection_id, "
						+ " insurfollowup_inspectionresult_id, "
						+ " insurfollowup_offer_id, "
						+ " insurfollowup_appointmentverification_id,"
						// + " insurfollowup_insur_field_emp_id, "
						+ " insurfollowup_desc,"
						+ " insurfollowup_entry_id,"
						+ " insurfollowup_entry_time"
						+ ")"
						+ " VALUES"
						+ " (" + insurenquiry_id + ","
						// + " (SELECT IF(veh_insuremp_id = 0, " + emp_id + ", veh_insuremp_id) from " + compdb(comp_id) + "axela_service_veh "
						// + " WHERE insurenquiry_id = " + insurenquiry_id + "),"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + ConvertLongDateToStr(txt_nextfollowup_time) + "',"
						+ " '" + insurfollowup_contactable_id + "',"
						// + " '" + dr_nextfollowup_type + "',"// insurfollowup_followuptype_id
						+ " '" + disposition_id + "'," // disposition_id
						+ " '" + veh_insur_notinterested_id + "'," // reason_id
						+ " '" + dr_inspection_id + "'," // dr_inspection_id
						+ " '" + dr_inspection_result_id + "'," // dr_inspection_result_id
						+ " '" + insur_offers + "'," // dr_offer_id
						+ " '" + dr_appoint_verification_id + "'," // dr_appoint_verification_id
						// / + " '" + insurpolicy_field_emp_id + "'," // insurpolicy_field_emp_id
						+ " '" + txt_remarks + "'," // txt_remarks
						+ " '" + emp_id + "',"
						+ " '" + insurfollowup_entry_time + "')";
				SOP("Insert insur==111==" + StrSql);
				updateQuery(StrSql);
			}

			if (!txt_nextfollowup_time.equals("")) {

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
						+ " (insurfollowup_insurenquiry_id,"
						// + " insurfollowup_emp_id,"
						+ " insurfollowup_followup_time,"
						// + " insurfollowup_nextfollowup_time,"
						+ " insurfollowup_followuptype_id,"
						// + " insurfollowup_disposition_id , "
						+ " insurfollowup_insur_field_emp_id, "
						+ " insurfollowup_entry_id,"
						+ " insurfollowup_entry_time"
						+ ")"
						+ " VALUES"// dr_nextfollowup_type
						+ " (" + insurenquiry_id + ","
						// + " (SELECT IF(veh_insuremp_id = 0, " + emp_id + ", veh_insuremp_id) from " + compdb(comp_id) + "axela_service_veh "
						// + " WHERE insurenquiry_id = " + insurenquiry_id + "),"
						+ " '" + ConvertLongDateToStr(txt_nextfollowup_time) + "',"
						// + " '" + ConvertLongDateToStr(txt_nextfollowup_time) + "',"
						+ " '" + dr_nextfollowup_type + "',"// insurfollowup_followuptype_id
						// + " '" + disposition_id + "'," // disposition_id
						+ " '" + insurpolicy_field_emp_id + "'," // insurpolicy_field_emp_id
						+ " '" + emp_id + "',"
						+ " '" + insurfollowup_entry_time + "')";
				SOP("Insert insur==222==" + StrSql);
				updateQuery(StrSql);
			}

		}
	}

	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_type"
					+ " ORDER BY followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("followuptype_id"));
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateReason(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT notinterestedreason_id, notinterestedreason_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_notinterestedreason"
					+ " ORDER BY notinterestedreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("notinterestedreason_id"));
				Str.append(StrSelectdrop(crs.getString("notinterestedreason_id"), veh_insur_notinterested_id));
				Str.append(">").append(crs.getString("notinterestedreason_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFeedbackType(String comp_id, String dr_feedbacktype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT feedbacktype_id, feedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurnace_enquiry_followup_feedback_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurinspection_id"
					+ " ORDER BY insurinspection_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("feedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("feedbacktype_id"), dr_feedbacktype_id));
				Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateNextFollowupType(String comp_id, String dr_followuptype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurfollowuptype_id, insurfollowuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurinspection_id"
					+ " ORDER BY insurinspection_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfollowuptype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfollowuptype_id"), dr_followuptype_id));
				Str.append(">").append(crs.getString("insurfollowuptype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
