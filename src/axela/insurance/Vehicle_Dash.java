////////////Divya 4th april 2013
package axela.insurance;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.customer.Customer_Tags_Check;
import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Vehicle_Dash extends Connect {

	public String StrSql = "";
	public String StrCustomerDetails = "";
	public String msg = "", contact_msg = "", list_insurfollowup_msg = "", insur_msg = "";
	public String StrHTML = "";
	public String SqlJoin = "";
	public String BranchAccess = "";
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
	public String veh_id = "0";
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
	public String StrSearch = "";
	// Insurance Follow-Up
	public String insurfollowup_contactable_id = "0";
	public String disposition_id = "0", dr_inspection_id = "0", dr_inspection_result_id = "0", dr_appoint_verification_id = "0";
	public String txt_nextfollowup_time = "", dr_nextfollowup_type = "0", dr_offer_id = "0", dr_field_exe_id = "0", txt_remarks = "";
	public String addInsurFollowup = "", listfollowup_info = "", veh_insurtype_id = "", veh_insur_notinterested_id = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public String veh_enquiry_id = "0";
	public Vehicle_Check vehicle = new Vehicle_Check();

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();
	public MIS_Check mischeck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				// ExeAccess = GetSession("ExeAccess", request);
				// customer_edit_perm = ReturnPerm(comp_id, "emp_customer_edit", request);
				msg = PadQuotes(request.getParameter("msg"));
				list_insurfollowup_msg = PadQuotes(request.getParameter("insurfollowup_msg"));
				disposition_id = PadQuotes(request.getParameter("dr_disposition_id"));
				insurfollowup_contactable_id = PadQuotes(request.getParameter("dr_insurfollowup_contactable_id"));
				// SOP("list_insurfollowup_msg----do-" + list_insurfollowup_msg);
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				modal = PadQuotes(request.getParameter("modal"));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				// SOP("veh_id====due=====" + veh_id);
				insurfollowup_id = PadQuotes(request.getParameter("insurfollowup_id"));
				vehfollowup_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				addContactB = PadQuotes(request.getParameter("add_contact_button"));
				currenttimevalidate = strToLongDate(ToLongDate(kknow()));
				currenttimevalidate = currenttimevalidate.replaceAll("/", "").replace(":", "").replace(" ", "");
				addInsurFollowup = PadQuotes(request.getParameter("add_insurfollowup_button"));
				delete = PadQuotes(request.getParameter("Delete"));

				if (loaddata.equals("yes")) {
					PopulateVehDetails(veh_id);
					StrCustomerDetails = CustomerDetails(veh_customer_id, customer_edit_perm, comp_id);
					contactdetails = new Enquiry_Dash_Customer().ListContact(veh_customer_id, comp_id);
					customer_info = new Enquiry_Dash_Customer().CustomerDetails(response, veh_customer_id, "yes", comp_id);
					ownership_info = ListOwnershipData(comp_id, veh_id);
					customerdetail = ListCustomerDetails(comp_id, veh_id, "Service");
					insurance_info = ListInsurance(comp_id, veh_id);
					history = ListHistory(comp_id, veh_id);
					option = VehicleOption(veh_id);
				}
				if (addContactB.equals("Add Contact")) {
					new_contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_new_contact_title_id")));
					new_contact_fname = PadQuotes(request.getParameter("txt_new_contact_fname"));
					new_contact_lname = PadQuotes(request.getParameter("txt_new_contact_lname"));
					new_contact_mobile1 = PadQuotes(request.getParameter("txt_new_contact_mobile1"));
					new_contact_mobile2 = PadQuotes(request.getParameter("txt_new_contact_mobile2"));
					new_contact_mobile3 = PadQuotes(request.getParameter("txt_new_contact_mobile3"));
					new_contact_mobile4 = PadQuotes(request.getParameter("txt_new_contact_mobile4"));
					new_contact_mobile5 = PadQuotes(request.getParameter("txt_new_contact_mobile5"));
					new_contact_mobile6 = PadQuotes(request.getParameter("txt_new_contact_mobile6"));
					new_contact_email1 = PadQuotes(request.getParameter("txt_new_contact_email1"));
					new_contact_contacttype_id = CNumeric(PadQuotes(request.getParameter("dr_new_contact_contacttype_id")));
					contact_mobile1_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id1")));
					contact_mobile2_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id2")));
					contact_mobile3_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id3")));
					contact_mobile4_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id4")));
					contact_mobile5_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id5")));
					contact_mobile6_phonetype_id = CNumeric(PadQuotes(request.getParameter("dr_new_phonetype_id6")));
					new_contact_notes = PadQuotes(request.getParameter("txt_new_contact_notes"));
					ContactCheckForm();
					if (contact_msg.equals("")) {
						AddNewContact();
						contact_msg = "Contact added successfully!";
					} else {
						contact_msg = "Error!" + contact_msg;
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

	public String CustomerDetails(String customer_id, String customer_edit_perm, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_mobile3, customer_mobile4,"
					+ " customer_mobile5, customer_mobile6,"
					+ " customer_phone1, customer_phone2, customer_email1,"
					+ " customer_email2, customer_address, city_name, customer_pin"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " WHERE customer_id = " + customer_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("Testing...");
				// Str.append("<div class=\"portlet box  \"> \n");
				// Str.append("<div class=\"portlet-title\" style=\"text-align: center\">\n");
				// Str.append("<div class=\"caption\" style=\"float: none\">Customer Details</div></div>\n");
				// Str.append("<div class=\"portlet-body portlet-empty\"\n>");
				// Str.append("<div class=\"tab-pane\" id=\"tabs-2\">");
				// Str.append("<center>");
				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<div class=\"table-bordered table-hover\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tbody>\n");
				// Str.append("Testing...2222");
				// Str.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"1\" class=\"listtable\">");

				// if (customer_edit_perm.equals("1")) { Str.append("<tr>\n"); Str .append("<td colspan=\"4\" align=\"right\" valign=\"top\">\n" ); Str.append(
				// "<a href=\"javascript:UpdateCustomer();\">Update Customer</a></td>\n" ); Str.append("</tr>\n"); }

				// Str.append("<tr>\n<th colspan=\"4\" align=\"center\" valign=\"top\">Customer Details</th>\n</tr>\n");
				while (crs.next()) {
					// Str.append("Hello.....");
					Str.append("<tr>");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">Customer:</td>\n");
					Str.append("<td colspan=\"3\" align=\"left\" valign=\"top\">").append(crs.getString("customer_name")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr valign=\"center\">\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 1:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile1")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 2:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile2")).append("</td>\n");
					Str.append("</tr>\n");
					if (!crs.getString("customer_mobile3").equals("")) {
						Str.append("<tr valign=\"center\">\n");
						Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 3:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile3")).append("</td>\n");
					}
					if (!crs.getString("customer_mobile4").equals("")) {

						Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 4:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile4")).append("</td>\n");
						Str.append("</tr>\n");
					}
					if (!crs.getString("customer_mobile5").equals("")) {
						Str.append("<tr valign=\"center\">\n");
						Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 5:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile5")).append("</td>\n");
					}
					if (!crs.getString("customer_mobile6").equals("")) {
						Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Mobile 6:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_mobile6")).append("</td>\n");
						Str.append("</tr>\n");
					}

					Str.append("<tr valign=\"center\">\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Phone 1:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_phone1")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\"> Phone 2:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_phone2")).append("</td>\n");
					Str.append("</tr> \n");
					Str.append("<tr valign=\"center\">\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">Email 1:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_email1")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">Email 2:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_email2")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">Address:</td>\n");
					Str.append("<td  align=\"left\" valign=\"top\">").append(crs.getString("customer_address")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">City:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("city_name")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr valign=\"center\">\n");
					Str.append("<td align=\"right\" valign=\"top\" id=\"cust_details\">Pin/Zip:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("customer_pin")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">&nbsp;</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">&nbsp;</td>\n");
					Str.append("</td>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("</tbody>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
				// Str.append("</div>");
				// Str.append("</center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String VehicleOption(String veh_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_name, option_code"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
					+ " WHERE (option_optiontype_id != 1"
					+ " AND option_optiontype_id != 2)"
					+ " AND vehtrans_veh_id = " + veh_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append(crs.getString("option_name")).append(" (").append(crs.getString("option_code")).append(")<br>");
				}
				Str.append("<a href=\"vehicle-options.jsp?veh_id=").append(veh_id).append("\">Configure Options</a>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateVehDetails(String veh_id) {
		try {
			StrSql = "SELECT COALESCE(customer_id, 0) AS customer_id,"
					+ " COALESCE(contact_id, 0) AS contact_id,"
					+ " COALESCE(so_enquiry_id, 0) AS so_enquiry_id,"
					+ " COALESCE(title_desc, '') AS title_desc,"
					+ " COALESCE(customer_id, '') AS customer_id,"
					+ " COALESCE(customer_name, '') AS customer_name, "
					+ " COALESCE(contact_title_id, 0) AS contact_title_id,"
					+ " COALESCE(contact_fname, '') AS contact_fname,"
					+ " COALESCE(contact_lname, '') AS contact_lname,"
					+ " COALESCE(city_id, 0) AS city_id, veh_warranty_expirydate,"
					+ " COALESCE(veh_branch_id, '') AS veh_branch_id,"
					+ " COALESCE(model_id, 0) AS model_id,"
					+ " COALESCE(item_id, 0) AS item_id,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE(contact_mobile1_phonetype_id, '') AS contact_mobile1_phonetype_id,"
					+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
					+ " COALESCE(contact_mobile2_phonetype_id, '') AS contact_mobile2_phonetype_id,"
					+ " COALESCE(contact_mobile3, '') AS contact_mobile3,"
					+ " COALESCE(contact_mobile3_phonetype_id, '') AS contact_mobile3_phonetype_id,"
					+ " COALESCE(contact_mobile4, '') AS contact_mobile4,"
					+ " COALESCE(contact_mobile4_phonetype_id, '') AS contact_mobile4_phonetype_id,"
					+ " COALESCE(contact_mobile5, '') AS contact_mobile5,"
					+ " COALESCE(contact_mobile5_phonetype_id, '') AS contact_mobile5_phonetype_id,"
					+ " COALESCE(contact_mobile6, '') AS contact_mobile6,"
					+ " COALESCE(contact_mobile6_phonetype_id, '') AS contact_mobile6_phonetype_id,"
					+ " COALESCE(contact_phone1, '') AS contact_phone1,"
					+ " COALESCE(contact_phone2, '') AS contact_phone2,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2,"
					+ " COALESCE(contact_pin, '') AS contact_pin,"
					+ " COALESCE(contact_address, '') AS contact_address,"
					+ " CONCAT(contact_address,', ',city_name,', ',state_name,', ',contact_pin,', ',contact_landmark) AS pickuplocation,"
					+ " branch_name, branch_code,"
					+ " veh_modelyear, veh_chassis_no, veh_engine_no, veh_reg_no,"
					+ " veh_sale_date, veh_sale_amount, COALESCE(veh_vehsource_id, 0) AS veh_vehsource_id, veh_notes, veh_service_duekms,"
					+ " IF(veh_service_duedate=0,'',veh_service_duedate) AS veh_service_duedate, veh_lastservice, veh_lastservice_kms, veh_emp_id, veh_crmemp_id,"
					+ " COALESCE(veh_insuremp_id,0) AS veh_insuremp_id, veh_insursource_id, veh_renewal_date, veh_insur_date, veh_insurtype_id,"
					+ " COALESCE((SELECT vehtrans_option_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND vehtrans_veh_id = veh_id LIMIT 1), 0) AS ext_id,"
					+ " COALESCE((SELECT vehtrans_option_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
					+ " WHERE option_optiontype_id = 2"
					+ " AND vehtrans_veh_id = veh_id LIMIT 1), 0) AS int_id,"
					+ " veh_entry_id, veh_entry_date, veh_modified_id, veh_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh_option_trans ext ON ext.vehtrans_veh_id = veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
					+ " WHERE 1=1"
					+ " AND veh_id = " + veh_id
					+ BranchAccess.replace("branch_id", "veh_branch_id");
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("Populate Veh Details=======" + StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					veh_branch_id = crs.getString("veh_branch_id");
					veh_enquiry_id = CNumeric(PadQuotes(crs.getString("so_enquiry_id")));
					branch_brand_id = CNumeric(ExecuteQuery("Select branch_brand_id from " + compdb(comp_id) + "axela_branch "
							+ " WHERE branch_id = " + veh_branch_id));
					branch_name = crs.getString("branch_name");
					item_model_id = crs.getString("model_id");
					veh_item_id = crs.getString("item_id");
					veh_modelyear = crs.getString("veh_modelyear");
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_engine_no = crs.getString("veh_engine_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					veh_sale_date = strToShortDate(crs.getString("veh_sale_date"));
					veh_sale_amount = crs.getString("veh_sale_amount");
					veh_vehsource_id = crs.getString("veh_vehsource_id");
					vehwarrantyexpirydate = strToShortDate(crs.getString("veh_warranty_expirydate"));
					veh_notes = crs.getString("veh_notes");
					veh_service_duekms = crs.getString("veh_service_duekms");
					veh_service_duedate = strToShortDate(crs.getString("veh_service_duedate"));
					veh_lastservice = strToShortDate(crs.getString("veh_lastservice"));
					veh_lastservice_kms = crs.getString("veh_lastservice_kms");
					veh_emp_id = crs.getString("veh_emp_id");
					veh_crmemp_id = crs.getString("veh_crmemp_id");
					veh_insuremp_id = crs.getString("veh_insuremp_id");
					veh_insursource_id = crs.getString("veh_insursource_id");
					veh_insur_date = strToShortDate(crs.getString("veh_insur_date"));
					veh_renewal_date = strToShortDate(crs.getString("veh_renewal_date"));
					veh_insurtype_id = crs.getString("veh_insurtype_id");
					veh_customer_id = crs.getString("customer_id");
					veh_customer_name = crs.getString("customer_name");
					veh_contact_id = crs.getString("contact_id");
					contact_title_id = crs.getString("contact_title_id");
					contact_city_id = crs.getString("city_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_name = crs.getString("title_desc") + contact_fname + contact_lname;
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile1_phonetype_id = crs.getString("contact_mobile1_phonetype_id");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_mobile2_phonetype_id = crs.getString("contact_mobile2_phonetype_id");
					contact_mobile3 = crs.getString("contact_mobile3");
					contact_mobile3_phonetype_id = crs.getString("contact_mobile3_phonetype_id");
					contact_mobile4 = crs.getString("contact_mobile4");
					contact_mobile4_phonetype_id = crs.getString("contact_mobile4_phonetype_id");
					contact_mobile5 = crs.getString("contact_mobile5");
					contact_mobile5_phonetype_id = crs.getString("contact_mobile5_phonetype_id");
					contact_mobile6 = crs.getString("contact_mobile6");
					contact_mobile6_phonetype_id = crs.getString("contact_mobile6_phonetype_id");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_phone1 = crs.getString("contact_phone1");
					contact_phone2 = crs.getString("contact_phone2");
					contact_pin = crs.getString("contact_pin");
					contact_address = crs.getString("contact_address");
					ext_id = crs.getString("ext_id");
					int_id = crs.getString("int_id");
					veh_entry_id = crs.getString("veh_entry_id");
					if (!veh_entry_id.equals("")) {
						veh_entry_by = Exename(comp_id, Integer.parseInt(veh_entry_id));
					}
					veh_entry_date = strToLongDate(crs.getString("veh_entry_date"));
					veh_modified_id = crs.getString("veh_modified_id");
					if (!veh_modified_id.equals("0")) {
						veh_modified_by = Exename(comp_id, Integer.parseInt(veh_modified_id));
					}
					veh_modified_date = strToLongDate(crs.getString("veh_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String VehicleInfo(String comp_id, String veh_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT customer_id, contact_id, customer_name, title_desc, contact_title_id, model_id,"
					+ " contact_lname, model_name, item_id, contact_fname, veh_cal_kms, veh_modified_date,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " COALESCE(so_id, 0) AS so_id, veh_id, veh_item_id, veh_lastservice,"
					+ " veh_modelyear, veh_chassis_no, veh_engine_no, veh_reg_no,veh_sale_amount, veh_kms,"
					+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " COALESCE(CONCAT('SO', branch_code, COALESCE(so_no, '')), '') AS so_no,"
					+ " veh_sale_date, veh_insursource_id, veh_renewal_date, veh_notes, veh_entry_id, veh_entry_date, veh_modified_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " WHERE veh_id = " + veh_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!crs.getString("so_id").equals("0")) {
						link_so_name = "<a href=\"../sales/veh-salesorder-list.jsp?so_id="
								+ crs.getString("so_id") + "\">" + crs.getString("so_no") + "</a>";
					}
					// Str.append("<table width=\"100%\" border=1 cellspacing=0 cellpadding=0 class=\"listtable\" height=\"200\">\n");
					// Str.append("<tr align=\"center\">\n");
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th colspan=\"4\">Vehicle Info</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Branch:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap colspan=\"3\">");
					Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">");
					Str.append(crs.getString("branch_name")).append("</a></td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Customer:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Contact:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("title_desc")).append(crs.getString("contact_fname")).append(" ").append(crs.getString("contact_lname")).append("</a>&nbsp;");
					Str.append("<div class=\"admin-master\"><a href=\"../customer/customer-contact-update.jsp?add=yes&customer_id=");
					Str.append(crs.getString("customer_id")).append("\" title=\"Add Contact\"></a></div>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Model:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("model_name")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Item:</td>");
					Str.append("<td align=\"left\" valign=\"top\"><a href=\"../inventory/inventory-item-list.jsp?item_id=");
					Str.append(crs.getString("item_id")).append("\">").append(crs.getString("item_name")).append("</a></td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Reg. No.:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Sale Date:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap>").append(strToShortDate(crs.getString("veh_sale_date"))).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Sale Amount:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap colspan=\"3\">").append(crs.getString("veh_sale_amount")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Model Year:</td>\n");
					Str.append("<td colspan=\"3\" align=\"left\" valign=\"top\" nowrap>").append(crs.getString("veh_modelyear")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Chassis Number:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap>").append(crs.getString("veh_chassis_no")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Engine Number:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" nowrap>").append(crs.getString("veh_engine_no")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Last Kms:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("veh_kms")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Calculated Kms:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("veh_cal_kms")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Last Service Date</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(strToShortDate(crs.getString("veh_lastservice"))).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Insurance Source:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("veh_insursource_id")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">Renewal Date</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(strToShortDate(crs.getString("veh_renewal_date"))).append("</td>\n");
					// Str.append("<td align=\"left\" valign=\"top\">Insurance Type:</td>\n");
					// Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("veh_insurtype_id")).append("</td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Sales Order:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(link_so_name);
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"left\" valign=\"top\">Notes:</td>\n");
					Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(crs.getString("veh_notes")).append("</td>\n");
					Str.append("</tr>\n");
					if (!crs.getString("veh_entry_id").equals("0")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\" valign=\"top\">Entry by:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(Exename(comp_id, Integer.parseInt(crs.getString("veh_entry_id")))).append("</td>\n");
						Str.append("</tr>\n");
						Str.append("<tr>\n");
						Str.append("<td align=\"left\" valign=\"top\">Entry date:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(strToLongDate(crs.getString("veh_entry_date"))).append("</td>\n");
						Str.append("</tr>\n");
					}

					if (!crs.getString("veh_modified_id").equals("0")) {
						Str.append("<tr>\n");
						Str.append("<td align=\"left\" valign=\"top\">Modified by:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(Exename(comp_id, Integer.parseInt(crs.getString("veh_modified_id")))).append("</td>\n");
						Str.append("</tr>\n");
						Str.append("<tr>\n");
						Str.append("<td align=\"left\" valign=\"top\">Modified Date:</td>\n");
						Str.append("<td align=\"left\" valign=\"top\" colspan=\"3\">").append(strToLongDate(crs.getString("veh_modified_date"))).append("</td>\n");
						Str.append("</tr>\n");
						Str.append("</tbody>\n");
					}
					Str.append("</table>\n");
					Str.append("</div>\n");
				}
			} else {
				Str.append("<br><br><br><br><center><font color=\"red\"><b>No Vehicle Selected!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListOwnershipData(String comp_id, String veh_id) {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT ownership_id, veh_id, ownership_name, veh_reg_no, item_name,"
				+ " ownership_from, ownership_to, ownership_address, ownership_notes,"
				+ " ownership_veh_id, ownership_mobile1, ownership_email1"
				+ " FROM " + compdb(comp_id) + "axela_service_veh_ownership"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ownership_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
				+ " WHERE veh_id = " + veh_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			Str.append("<div style=\"float:right\" >  ");
			Str.append("<a href=\"veh-ownership-update.jsp?add=yes&veh_id=").append(veh_id).append("\" target=\"_blank\">Add New Ownership...</a>");
			Str.append("</div>");
			Str.append("<table class=\"table table-responsive table-hover\">");
			// Str.append("<tr>\n");
			// Str.append("<td  colspan=\"11\" style=\"float:right\">");
			// Str.append("<a href=\"veh-ownership-update.jsp?add=yes&veh_id=").append(veh_id).append("\" target=\"_blank\">Add New Ownership...</a>");
			// Str.append("<br></td>\n");
			// Str.append("</tr>\n");
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<thead>");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th align=\"center\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Ownership Name</th>\n");
				Str.append("<th>Address</th>\n");
				Str.append("<th>Term</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr></thead>\n");

				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(crs.getString("ownership_id")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("ownership_name"));
					Str.append("<br>").append(crs.getString("ownership_notes")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("ownership_address"));
					Str.append("<br>").append(crs.getString("ownership_mobile1"));
					Str.append("<br>").append(crs.getString("ownership_email1"));
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>");
					Str.append(strToShortDate(crs.getString("ownership_from"))).append(" - ").append(strToShortDate(crs.getString("ownership_to")));
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"veh-ownership-update.jsp?update=yes&ownership_id=").append(crs.getString("ownership_id")).append("&veh_id=").append(veh_id)
							.append("\"> Update Ownership</a>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
			} else {
				Str.append("\n<div align=\"center\"><br><br><br><br><font color=red><b>No Ownership found!</b></font><br><br><br><br></div>\n");
				// Str.append("<tr>\n<td align=\"center\"><br><br><br><br><font color=red><b>No Ownership found!</b></font><br><br><br><br></td>\n</tr>\n");
			}
			Str.append("</table>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListDocs(String veh_id, String PageCurrents, String QueryString, int recperpage, String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
			StrSql = " SELECT doc_id, doc_value, doc_title, doc_remarks "
					+ " FROM " + compdb(comp_id) + "axela_service_veh_docs "
					+ " WHERE 1=1 "
					+ " and doc_veh_id=" + veh_id + "";
		// SOP("StrSql----------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {

			Str.append("<div class=\"container-fluid \">\n");
			Str.append("<a href=\"../portal/docs-update.jsp?add=yes&veh_id=").append(veh_id).append("\" style=\"float:right\">Add New Document...</a>");
			Str.append("</div>");
			// Str.append("<div class=\"container-fluid portlet box\">");
			// Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
			// Str.append("<div class=\"caption\" style=\"float: none\">Documents</div></div>");
			Str.append("<div class=\"portlet-body portlet-empty\">");
			Str.append("<div class=\"tab-pane\" id=\"\">");
			Str.append("<div class=\"table-responsive \">\n");
			Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead>\n");
			Str.append("<tr>\n");
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center ><b>").append(count).append("</b></td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(VehicleDocPath(comp_id)).exists()) {
							new File(VehicleDocPath(comp_id)).mkdirs();
						}
						File f = new File(VehicleDocPath(comp_id)
								+ crs.getString("doc_value"));
						Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "veh_doc_id=")
								.append(crs.getString("doc_id")).append("><b>")
								.append(crs.getString("doc_title")).append(" (")
								.append(crs.getString("doc_id"))
								.append(")</a></b> (")
								.append(ConvertFileSizeToBytes(FileSize(f)))
								.append(")<br> ")
								.append(crs.getString("doc_remarks"))
								.append("</td>\n");
					} else {
						Str.append("<td valign=top align=left ><b>")
								.append(crs.getString("doc_title")).append(" (")
								.append(crs.getString("doc_id"))
								.append(") (0 Bytes)</b><br> ")
								.append(crs.getString("doc_remarks"))
								.append("</td>\n");
					}
					// if (!veh_id.equals("0")) {
					Str.append("<td valign=top align=left ><a href=\"../portal/docs-update.jsp?update=yes&veh_id=")
							.append(veh_id).append("&doc_id=")
							.append(crs.getString("doc_id"))
							.append("\">Update Document</a></td>\n");
					// }
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
			} else {
				docmsg = "";
				Str.append("\n<div align=\"center\"><br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br><br><br></div>\n");
			}
			Str.append("</table>\n");
			Str.append("</div>\n");
			// Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();

		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// Ticket List //
	public String ListTicketData(String comp_id, String veh_id) {
		// SOP("Coming inside ListTickets");
		StringBuilder Str = new StringBuilder();
		int count = 0;
		String ticketstatus_id = "";
		long closetime = 0;

		StrSql = "SELECT ticket_id, ticket_subject, ticket_desc, ticket_customer_id,"
				+ " ticket_contact_id, customer_id, customer_name, item_name,"
				+ " COALESCE(ticketcat_name, '') AS ticketcat_name,"
				+ " COALESCE(tickettype_name, '') AS tickettype_name,"
				+ " priorityticket_name, priorityticket_id,"
				+ " ticket_trigger, ticketstatus_id, ticketstatus_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
				+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
				+ " emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, ticket_dept_name,"
				+ " ticket_report_time, ticket_due_time, ticket_closed_time, veh_id, veh_reg_no"
				+ " FROM " + compdb(comp_id) + "axela_service_ticket"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id" + BranchAccess + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id"
				+ " WHERE veh_id = " + veh_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("Ticket SQL=======" + StrSql);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-reponsive table-bordered \" >\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th width=\"3%\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Ticket</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Vehicle</th>\n");
				Str.append("<th>Report Time</th>\n");
				Str.append("<th>Due Time</th>\n");
				Str.append("<th>Status</th>\n");
				Str.append("<th>Priority</th>\n");
				Str.append("<th>Level</th>\n");
				Str.append("<th>Category</th>\n");
				Str.append("<th>Type</th>\n");
				Str.append("<th>Department</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					ticketstatus_id = crs.getString("ticketstatus_id");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("ticket_id")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"javascript:remote=window.open('ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("','ticketdash','');remote.focus();\">");
					Str.append(crs.getString("ticket_subject")).append("</a></td>");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					if (!crs.getString("contact_id").equals("0")) {
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">");
						Str.append(crs.getString("customer_name")).append("</a><br>");
						Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("ticket_contact_id")).append(" \">");
						Str.append(crs.getString("contact_name")).append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a>");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
						}
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					if (!crs.getString("item_name").equals("")) {
						Str.append(crs.getString("item_name"));
						Str.append("<br>").append(SplitRegNo(crs.getString("veh_reg_no"), 4));
						Str.append("<br>ID: <a href=\"vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">").append(crs.getString("veh_id")).append("</a>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>");
					if (!crs.getString("ticket_closed_time").equals("")) {
						closetime = crs.getLong("ticket_closed_time");
					} else {
						closetime = Long.parseLong(ToLongDate(kknow()));
					}
					if (!crs.getString("ticket_due_time").equals("")) {
						if (closetime >= Long.parseLong(crs.getString("ticket_due_time"))) {
							Str.append("<font color=\"#ff0000\">").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
						} else {
							Str.append("<font color=\"blue\">").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
						}
					}
					if (ticketstatus_id.equals("3")) {
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><font color=\"red\">").append(crs.getString("ticketstatus_name")).append("</font></td>\n");
					} else {
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("status_name")).append("</td>\n");
					}
					Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("priorityticket_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(crs.getString("ticket_trigger")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("ticketcat_name")).append("</td>\n");
					Str.append("<td valign=\"top\" calign=\"left\" nowrap>").append(crs.getString("tickettype_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("ticket_dept_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
				crs.close();
			} else {
				Str.append("<br><br><br><br><center><font color=\"red\"><b>No Ticket(s) found!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// Insurance

	protected void CheckForm() {
		list_insurfollowup_msg = "";

		if (insurfollowup_insuraction_id.equals("0")) {
			list_insurfollowup_msg += "<br>Select Follow-up Action!";
		}

		if (insurfollowup_desc.equals("")) {
			list_insurfollowup_msg += "<br>Enter Feedback!";
		}

		if (insurfollowup_psffeedbacktype_id.equals("0")) {
			list_insurfollowup_msg += "<br>Select Contactable!";
		}
		if (insurlostcase1_id.equals("0")) {
			if (insurfollowup_time.equals("")) {
				list_insurfollowup_msg += "<br>Select Next Follow-up Time!";
			} else {
				if (!isValidDateFormatLong(insurfollowup_time)) {
					list_insurfollowup_msg += "<br>Enter valid Follow-up Time!";
				} else {
					if (Long.parseLong(ConvertLongDateToStr(insurfollowup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
						list_insurfollowup_msg += "<br>Next Follow-up Time must be greater than Current Time!";
					}

					if (Integer.parseInt(CNumeric(getMonthsBetween(ToLongDate(kknow()), ConvertLongDateToStr(insurfollowup_time)))) > 11) {
						list_insurfollowup_msg += "<br>Next Follow-up can't exceed 11 months!";
					}

					if ((Integer.parseInt(ConvertLongDateToStr(insurfollowup_time).substring(8, 10)) > 21) || (Integer.parseInt(ConvertLongDateToStr(insurfollowup_time).substring(8, 10)) < 8)) {
						list_insurfollowup_msg += "<br>Next Follow-up Time should be greater than 8 AM and less than 9 PM!";
					}
				}
			}

			if (insurfollowup_followuptype_id.equals("0")) {
				list_insurfollowup_msg += "<br>Select Next Follow-up Type!";
			}

			if (insurfollowup_priorityinsurfollowup_id.equals("0")) {
				list_insurfollowup_msg += "<br>Select Next Follow-up Priority!";
			}

		}

		// if (veh_insuremp_id.equals("0")) {
		// msg += "<br>Select Next Follow-up Executive!";
		// }
	}

	protected void ContactCheckForm() {
		contact_msg = "";

		if (new_contact_title_id.equals("0")) {
			contact_msg += "<br>Select Contact Title for New Contact!";
		}

		if (new_contact_fname.equals("")) {
			contact_msg += "<br>Enter Contact First Name for New Contact!";
		}

		if (new_contact_mobile1.equals("")) {
			contact_msg += "<br>Enter Contact Mobile 1 for New Contact!";
		} else if (!IsValidMobileNo11(new_contact_mobile1)) {
			contact_msg += "<br>Enter valid Contact Mobile 1 for New Contact!";
		} else {
			StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " WHERE contact_mobile1 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile2 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile3 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile4 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile5 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile6 = '" + new_contact_mobile1 + "'";
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				contact_msg += "<br>Similar Contact Mobile 1 found for New Contact!";
			}
			if (contact_mobile1_phonetype_id.equals("0")) {
				contact_msg += "<br>Select Phone Type for Mobile 1!";
			}
		}

		if (!new_contact_mobile2.equals("")) {

			if (!IsValidMobileNo11(new_contact_mobile2)) {
				contact_msg += "<br>Enter valid Contact Mobile 2 for New Contact!";
			} else {
				StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " WHERE contact_mobile1 = '" + new_contact_mobile2 + "'"
						+ " OR contact_mobile2 = '" + new_contact_mobile2 + "'"
						+ " OR contact_mobile3 = '" + new_contact_mobile2 + "'"
						+ " OR contact_mobile4 = '" + new_contact_mobile2 + "'"
						+ " OR contact_mobile5 = '" + new_contact_mobile2 + "'"
						+ " OR contact_mobile6 = '" + new_contact_mobile2 + "'";
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					contact_msg += "<br>Similar Contact Mobile 2 found for New Contact!";
				}
				if (contact_mobile2_phonetype_id.equals("0")) {
					contact_msg += "<br>Select Phone Type for Mobile 2!";
				}
			}
		}

		if (!new_contact_mobile3.equals("")) {

			if (!IsValidMobileNo11(new_contact_mobile3)) {
				contact_msg += "<br>Enter valid Contact Mobile 3 for New Contact!";
			} else {
				StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " WHERE contact_mobile1 = '" + new_contact_mobile3 + "'"
						+ " OR contact_mobile2 = '" + new_contact_mobile3 + "'"
						+ " OR contact_mobile3 = '" + new_contact_mobile3 + "'"
						+ " OR contact_mobile4 = '" + new_contact_mobile3 + "'"
						+ " OR contact_mobile5 = '" + new_contact_mobile3 + "'"
						+ " OR contact_mobile6 = '" + new_contact_mobile3 + "'";
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					contact_msg += "<br>Similar Contact Mobile 3 found for New Contact!";
				}
				if (contact_mobile3_phonetype_id.equals("0")) {
					contact_msg += "<br>Select Phone Type for Mobile 3!";
				}
			}
		}

		if (!new_contact_mobile4.equals("")) {

			if (!IsValidMobileNo11(new_contact_mobile4)) {
				contact_msg += "<br>Enter valid Contact Mobile 4 for New Contact!";
			} else {
				StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " WHERE contact_mobile1 = '" + new_contact_mobile4 + "'"
						+ " OR contact_mobile2 = '" + new_contact_mobile4 + "'"
						+ " OR contact_mobile3 = '" + new_contact_mobile4 + "'"
						+ " OR contact_mobile4 = '" + new_contact_mobile4 + "'"
						+ " OR contact_mobile5 = '" + new_contact_mobile4 + "'"
						+ " OR contact_mobile6 = '" + new_contact_mobile4 + "'";
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					contact_msg += "<br>Similar Contact Mobile 4 found for New Contact!";
				}
				if (contact_mobile4_phonetype_id.equals("0")) {
					contact_msg += "<br>Select Phone Type for Mobile 4!";
				}
			}
		}

		if (!new_contact_mobile5.equals("")) {

			if (!IsValidMobileNo11(new_contact_mobile5)) {
				contact_msg += "<br>Enter valid Contact Mobile 5 for New Contact!";
			} else {
				StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " WHERE contact_mobile1 = '" + new_contact_mobile5 + "'"
						+ " OR contact_mobile2 = '" + new_contact_mobile5 + "'"
						+ " OR contact_mobile3 = '" + new_contact_mobile5 + "'"
						+ " OR contact_mobile4 = '" + new_contact_mobile5 + "'"
						+ " OR contact_mobile5 = '" + new_contact_mobile5 + "'"
						+ " OR contact_mobile6 = '" + new_contact_mobile5 + "'";
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					contact_msg += "<br>Similar Contact Mobile 5 found for New Contact!";
				}
				if (contact_mobile5_phonetype_id.equals("0")) {
					contact_msg += "<br>Select Phone Type for Mobile 5!";
				}
			}
		}

		if (!new_contact_mobile6.equals("")) {

			if (!IsValidMobileNo11(new_contact_mobile6)) {
				contact_msg += "<br>Enter valid Contact Mobile 6 for New Contact!";
			} else {
				StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " WHERE contact_mobile1 = '" + new_contact_mobile6 + "'"
						+ " OR contact_mobile2 = '" + new_contact_mobile6 + "'"
						+ " OR contact_mobile3 = '" + new_contact_mobile6 + "'"
						+ " OR contact_mobile4 = '" + new_contact_mobile6 + "'"
						+ " OR contact_mobile5 = '" + new_contact_mobile6 + "'"
						+ " OR contact_mobile6 = '" + new_contact_mobile6 + "'";
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					contact_msg += "<br>Similar Contact Mobile 6 found for New Contact!";
				}
				if (contact_mobile6_phonetype_id.equals("0")) {
					contact_msg += "<br>Select Phone Type for Mobile 6!";
				}
			}
		}

		// if (new_contact_email1.equals("")) {
		// contact_msg += "<br>Enter Contact Email 1 for New Contact!";
		// }
		if (!new_contact_email1.equals("") && !IsValidEmail(new_contact_email1)) {
			contact_msg += "<br>Enter valid Contact Email 1 for New Contact!";
		}

		if (new_contact_contacttype_id.equals("0")) {
			contact_msg += "<br>Select Contact Type for New Contact!";
		}
	}

	public void AddNewContact() {
		veh_customer_id = ExecuteQuery("SELECT veh_customer_id FROM " + compdb(comp_id) + "axela_service_veh"
				+ " WHERE veh_id = " + veh_id);
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_mobile1_phonetype_id,"
				+ " contact_mobile2,"
				+ " contact_mobile2_phonetype_id,"
				+ " contact_mobile3,"
				+ " contact_mobile3_phonetype_id,"
				+ " contact_mobile4,"
				+ " contact_mobile4_phonetype_id,"
				+ " contact_mobile5,"
				+ " contact_mobile5_phonetype_id,"
				+ " contact_mobile6,"
				+ " contact_mobile6_phonetype_id,"
				+ " contact_email1,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_active,"
				+ " contact_notes,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + veh_customer_id + ","
				+ " " + new_contact_contacttype_id + ","
				+ " " + new_contact_title_id + ","
				+ " '" + new_contact_fname + "',"
				+ " '" + new_contact_lname + "',"
				+ " '" + new_contact_mobile1 + "',"
				+ " '" + contact_mobile1_phonetype_id + "',"
				+ " '" + new_contact_mobile2 + "',"
				+ " '" + contact_mobile2_phonetype_id + "',"
				+ " '" + new_contact_mobile3 + "',"
				+ " '" + contact_mobile3_phonetype_id + "',"
				+ " '" + new_contact_mobile4 + "',"
				+ " '" + contact_mobile4_phonetype_id + "',"
				+ " '" + new_contact_mobile5 + "',"
				+ " '" + contact_mobile5_phonetype_id + "',"
				+ " '" + new_contact_mobile6 + "',"
				+ " '" + contact_mobile6_phonetype_id + "',"
				+ " '" + new_contact_email1 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " 1,"
				+ " '" + new_contact_notes + "',"
				+ " " + emp_id + ","
				+ " '" + ToLongDate(kknow()) + "')";
		updateQuery(StrSql);
	}

	public void PopulateContactDetails() {

		StrSql = " SELECT  veh_branch_id, veh_insuremp_id, customer_id, customer_name, contact_id, "
				+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, "
				+ " contact_mobile1, contact_mobile2, contact_mobile3, contact_mobile4, contact_mobile5, contact_mobile6,"
				+ " contact_email1, contact_email2 "
				+ " FROM " + compdb(comp_id) + "axela_service_veh "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= veh_customer_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " where veh_id = " + veh_id
				+ " group by veh_id";
		// SOP("StrSql--------PopulateContactDetails--------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				veh_branch_id = crs.getString("veh_branch_id");
				veh_contact_id = crs.getString("contact_id");
				veh_insuremp_id = crs.getString("veh_insuremp_id");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_mobile2 = crs.getString("contact_mobile2");
				contact_mobile3 = crs.getString("contact_mobile3");
				contact_mobile4 = crs.getString("contact_mobile4");
				contact_mobile5 = crs.getString("contact_mobile5");
				contact_mobile6 = crs.getString("contact_mobile6");
				contact_email1 = crs.getString("contact_email1");
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_sob , config_sales_enquiry_refno,"
				+ " config_email_enable, config_sms_enable, config_sales_lead_for_enquiry, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign, config_customer_dupnames, COALESCE(jobtitle_desc, '') AS jobtitle_desc,"
				+ " coalesce(branch_email1,'') AS branch_email1, "
				+ " coalesce(branch_insur_lost_email_enable,'') AS branch_insur_lost_email_enable,"
				+ " coalesce(branch_insur_lost_email_sub,'') AS branch_insur_lost_email_sub,"
				+ " coalesce(branch_insur_lost_email_format,'') AS branch_insur_lost_email_format,"
				+ " coalesce(branch_insur_lost_sms_enable,'') AS branch_insur_lost_sms_enable, "
				+ " coalesce(branch_insur_lost_sms_format,'') AS branch_insur_lost_sms_format, "
				+ " coalesce(emp.emp_email1,'') AS emp_email1, coalesce(emp.emp_email2,'') AS emp_email2,"
				+ " coalesce(emp.emp_name,'') AS emp_name,"
				+ " coalesce(emp.emp_mobile1,'') AS emp_mobile1, coalesce(emp.emp_mobile2,'') AS emp_mobile2"
				+ " FROM " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp,"
				+ compdb(comp_id) + "axela_emp admin"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = 1"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + veh_insuremp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp.emp_jobtitle_id"
				+ " where 1 = 1 and admin.emp_id = " + emp_id;

		// SOP("config======" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
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
	protected void SendEmailLostInsurance() throws SQLException {
		String emailmsg, sub;
		// SOP("emp_name--------------" + emp_name);
		// SOP("jobtitle_desc--------------" + jobtitle_desc);
		// SOP("emp_mobile1--------------" + emp_mobile1);
		// SOP("emp_email1--------------" + emp_email1);
		// SOP("emp_phone1--------------" + emp_phone1);

		StrSql = "SELECT veh_id, model_name, item_name, veh_insuremp_id, COALESCE(jobtitle_desc, '') AS jobtitle_desc "
				+ " FROM " + compdb(comp_id) + "axela_service_veh "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_insuremp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
				+ " WHERE veh_id = " + veh_id + ""
				+ " GROUP BY veh_id";
		// SOP("StrSql-----------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			while (crs.next()) {
				model_name = crs.getString("model_name");
				item_name = crs.getString("item_name");
				// jobtitle_desc = crs.getString("jobtitle_desc");

			}
			crs.close();

		}
		sub = "replace(branch_insur_lost_email_sub, '[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[INSUREXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[INSUREXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[INSUREXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		emailmsg = "replace(branch_insur_lost_email_format, '[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEPHONE1]',emp_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";
		// SOP("emailmsg--------" + emailmsg);
		try {
			StrSql = "SELECT"
					+ " '" + veh_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + emp_email1 + "',"
					+ " '" + contact_email1 + "',"
					+ " " + (sub) + ","
					+ " " + (emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_insuremp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " WHERE veh_id = " + veh_id;
			// SOP("StrSql-------email--2-------" + StrSqlBreaker(StrSql));
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_contact_id,"
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

	protected void SendSMSLostInsurance(String contact_mobile) throws SQLException {
		String smsmsg;

		smsmsg = "replace(branch_insur_lost_sms_format, '[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEPHONE1]',emp_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";
		// SOP("smsmsg-------sms-----" + smsmsg);
		try {
			StrSql = "SELECT"
					+ " " + veh_contact_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + contact_mobile + "',"
					+ " " + (smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_insuremp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " WHERE veh_id = " + veh_id;
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql--------sms-insert-------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListInsurance(String comp_id, String veh_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, insurpolicy_veh_id, veh_id, veh_reg_no, insurpolicy_contact_id,"
				+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
				+ " insurpolicy_date, policytype_name, insurpolicy_policy_no, inscomp_name,"
				+ " insurpolicy_premium_amt, insurpolicy_idv_amt, insurpolicy_od_amt, insurpolicy_od_discount, insurpolicy_payout,"
				+ " insurtype_name, insurpolicy_customer_id, insurpolicy_entry_date, insurpolicy_start_date, insurpolicy_end_date, customer_name,"
				+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
				+ " contact_id, contact_mobile1, contact_mobile2, model_name, insurpolicy_active, item_name"
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
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
				+ " WHERE 1=1"
				+ " AND insurpolicy_veh_id = " + veh_id
				+ BranchAccess.replace("branch_id", "insurpolicy_branch_id")
				+ " GROUP BY insurpolicy_id"
				+ " ORDER BY insurpolicy_id DESC";
		SOP("ListInsurance-----" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			Str.append("<div class=\"container-fluid \">\n");
			Str.append("<a href=\"insurance-update.jsp?add=yes&insurpolicy_veh_id=").append(veh_id).append("\" target=\"_blank\"\" style=\"float:right\">Add New Insurance...</a>");
			Str.append("</div><div class=\"container-fluid portlet box\">");
			Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
			Str.append("<div class=\"caption\" style=\"float: none\">Insurance</div></div>");
			Str.append("<div class=\"portlet-body portlet-empty\">");
			Str.append("<div class=\"tab-pane\" id=\"\">");
			Str.append("<div class=\"table-responsive \">\n");
			Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
			/*
			 * Str.append("<tr>\n"); Str.append( "<td align=\"right\" colspan=\"12\"  style=\"text-align:right\" >" ); Str.append("<a href=\"insurance-update.jsp?add=yes&veh_id=").append
			 * (veh_id).append("\" target=\"_blank\">Add New Insurance...</a>"); Str.append("<br></td>\n"); Str.append("</tr>\n");
			 */
			Str.append("<thead>\n");
			Str.append("<tr>\n");
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Vehicle ID</th>\n");
				Str.append("<th data-hide=\"phone\">Insurance</th>\n");
				Str.append("<th data-hide=\"phone\">Vehicle</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Term</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(crs.getString("insurpolicy_id")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>");
					Str.append("<a href=\"../insurance/vehicle-list.jsp?veh_id=").append(crs.getString("insurpolicy_veh_id")).append("\">");
					Str.append(crs.getString("insurpolicy_veh_id")).append("</a>").append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap><b>Company:</b> ").append(crs.getString("inscomp_name"));
					Str.append("<br><b>Policy:</b> ").append(crs.getString("policytype_name"));
					Str.append("<br><b>Policy No.:</b> ").append(crs.getString("insurpolicy_policy_no"));
					if (crs.getString("insurpolicy_active").equals("0")) {
						Str.append("<br><font color=\"red\">[Inactive]</font>");
					}

					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../insurance/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
					Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
					Str.append("<br>").append(crs.getString("item_name"));
					if (crs.getString("insurpolicy_active").equals("0")) {
						Str.append("<br><font color=\"red\">[Inactive]</font>");
					}

					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("insurpolicy_customer_id")).append(" \">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M")).append("</a>");
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M")).append("</a>");
					}

					Str.append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("insurtype_name")).append("</td>\n");

					if (!crs.getString("insurpolicy_entry_date").equals("")) {
						Str.append("<td align=\"left\" valign=\"top\">").append(strToShortDate(crs.getString("insurpolicy_entry_date"))).append("</td>\n");
					}

					Str.append("<td valign=\"top\" align=\"center\" nowrap>");
					if (!crs.getString("insurpolicy_start_date").equals("")) {
						Str.append(strToShortDate(crs.getString("insurpolicy_start_date"))).append("-").append(strToShortDate(crs.getString("insurpolicy_end_date"))).append(" ");
					}

					String startdate = crs.getString("insurpolicy_start_date").substring(0, 8);
					String enddate = crs.getString("insurpolicy_end_date").substring(0, 8);

					if (Long.parseLong(enddate) < Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=\"red\">[Expired]</font>");
					} else if (Long.parseLong(startdate) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=\"blue\">[Future Insurance]</font>");
					}
					Str.append("<td valign=\"top\" align=\"right\" nowrap>");
					if (!crs.getString("insurpolicy_premium_amt").equals("0")) {
						Str.append("Premium Amount: ").append(crs.getString("insurpolicy_premium_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_idv_amt").equals("0")) {
						Str.append("IDV Amount: ").append(crs.getString("insurpolicy_idv_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_od_amt").equals("0")) {
						Str.append("OD Amount: ").append(crs.getString("insurpolicy_od_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_od_discount").equals("0")) {
						Str.append("OD Discount: ").append(crs.getString("insurpolicy_od_discount")).append("<br>");
					}

					if (!crs.getString("insurpolicy_payout").equals("0")) {
						Str.append("Payout Amount: ").append(crs.getString("insurpolicy_payout"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">");
					Str.append(crs.getString("branch_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap>");
					Str.append("<a href=\"insurance-update.jsp?update=yes&insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">Update Insurance</a>");
					Str.append("<br/><a href=\"insurance-docs-list.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append(" \">List Documents</a>");
					Str.append("<br/><a href=\"insurance-service.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append(" \">Manage services</a>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
			}
			else {
				Str.append("\n<div align=\"center\"><br><br><br><br><font color=red><b>No Insurance found!</b></font><br><br><br><br></div>\n");
			}

			Str.append("</table>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListHistory(String comp_id, String veh_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_veh_history.*, emp_id,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_history"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = history_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " WHERE history_veh_id = " + veh_id + ""
					+ " ORDER BY history_id DESC";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-responsive\" data-filter=\"#filter\" >");
				Str.append("<thead>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th >Action By</th>\n");
				Str.append("<th data-hide=\"phone\">Type of Action</th>\n");
				Str.append("<th data-hide=\"phone\">Old Value</th>\n");
				Str.append("<th data-hide=\"phone\">New Value</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead><tbody>\n");
				while (crs.next()) {
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("history_datetime"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">");
					Str.append(crs.getString("emp_name")).append("</a>").append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_actiontype")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_oldvalue")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_newvalue")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table><div>\n");
			} else {
				Str.append("<br><br><br><br><center><font color=\"red\"><b>No History(s) found!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();

	}

	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), item_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String comp_id, String item_model_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS itemname"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE item_model_id = " + item_model_id + ""
					+ " AND item_type_id = 1"
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\" onChange=\"ClearMsg();SecurityCheck('dr_item_id', this, 'hint_dr_item_id')\">");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), veh_item_id));
				Str.append(">").append(crs.getString("itemname")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExterior(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_optiontype_id = 1"
					+ " AND option_brand_id = " + branch_brand_id;
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id"));
				Str.append(StrSelectdrop(crs.getString("option_id"), ext_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInterior(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_optiontype_id = 2"
					+ " AND option_brand_id = " + branch_brand_id;
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id"));
				Str.append(StrSelectdrop(crs.getString("option_id"), int_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTitle(String comp_id, String contact_title_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
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

	public String PopulateContactType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact_type"
					+ " GROUP BY contacttype_id"
					+ " ORDER BY contacttype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contacttype_id"));
				Str.append(StrSelectdrop(crs.getString("contacttype_id"), new_contact_contacttype_id));
				Str.append(">").append(crs.getString("contacttype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCity(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT city_id, COALESCE(city_name, '') AS city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("city_id"));
				Str.append(StrSelectdrop(crs.getString("city_id"), contact_city_id));
				Str.append(">").append(crs.getString("city_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFollowupPriority(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT priorityinsurfollowup_id, priorityinsurfollowup_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " ORDER BY priorityinsurfollowup_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityinsurfollowup_id"));
				Str.append(StrSelectdrop(crs.getString("priorityinsurfollowup_id"), insurfollowup_priorityinsurfollowup_id));
				Str.append(">").append(crs.getString("priorityinsurfollowup_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowupAction(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT insuraction_id, insuraction_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_action"
					+ " ORDER BY insuraction_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insuraction_id"));
				Str.append(StrSelectdrop(crs.getString("insuraction_id"), insurfollowup_insuraction_id));
				Str.append(">").append(crs.getString("insuraction_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), veh_insuremp_id));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCRMExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS crmemp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_crm = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
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

	public String PopulateServiceExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), veh_emp_id));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurLostCase(String comp_id, String lostcase_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurlostcase1_id, insurlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_lostcase1"
					+ " ORDER BY insurlostcase1_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurlostcase1_id"));
				Str.append(StrSelectdrop(crs.getString("insurlostcase1_id"), insurlostcase1_id));
				Str.append(">").append(crs.getString("insurlostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String PopulateInsurancePsfFeedback(String comp_id, String insurfollowup_psffeedbacktype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT psffeedbacktype_id, psffeedbacktype_name"
					+ " FROM axela_service_psf_feedbacktype"
					+ " WHERE 1=1"
					+ " ORDER BY psffeedbacktype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psffeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("psffeedbacktype_id"), insurfollowup_psffeedbacktype_id));
				Str.append(">").append(crs.getString("psffeedbacktype_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInsurSource(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insursource_id, insursource_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_source"
					+ " WHERE 1=1"
					+ " ORDER BY insursource_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insursource_id"));
				Str.append(StrSelectdrop(crs.getString("insursource_id"), veh_insursource_id));
				Str.append(">").append(crs.getString("insursource_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVehSource(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehsource_id, vehsource_name"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_source"
					+ " WHERE 1=1"
					+ " ORDER BY vehsource_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehsource_id"));
				Str.append(StrSelectdrop(crs.getString("vehsource_id"), veh_vehsource_id));
				Str.append(">").append(crs.getString("vehsource_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListCustomerDetails(String comp_id, String veh_id, String type)
	{
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT customer_id, customer_name, contact_id, veh_contactable_id, "
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, "
					+ " contact_mobile1, contact_mobile2, contact_mobile3, contact_mobile4, contact_mobile5, contact_mobile6, "
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile1_phonetype_id ), '') AS phonetypemobile1,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile2_phonetype_id ), '') AS phonetypemobile2,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile3_phonetype_id ), '') AS phonetypemobile3,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile4_phonetype_id ), '') AS phonetypemobile4,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile5_phonetype_id ), '') AS phonetypemobile5,"
					+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype WHERE phonetype_id = contact_mobile6_phonetype_id ), '') AS phonetypemobile6,"
					+ " item_name, veh_kms, veh_cal_kms, veh_lastservice, veh_lastservice_kms, veh_sale_date,veh_reg_no,"
					+ " IF(veh_service_duedate=0,'',veh_service_duedate) AS veh_service_duedate "
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= veh_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE veh_id = " + veh_id
					+ " GROUP BY veh_id";
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
					Str.append("<td>Variant: ").append(crs.getString("item_name")).append("</td>\n");

					if (type.equals("Service")) {
						Str.append("<td>Sale Date: ").append(strToShortDate(crs.getString("veh_sale_date"))).append("</td>\n");
						Str.append("<td>Registration No: ").append(crs.getString("veh_reg_no")).append("</td>\n");
						Str.append("<td>Next Service Date: ");
						Str.append(strToShortDate(crs.getString("veh_service_duedate")));
						Str.append("</td></tr>\n");
					}

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<tr align=center><td>Mobile 1: ").append(crs.getString("contact_mobile1") + "(" + crs.getString("phonetypemobile1") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile1"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<td>Mobile 2: ").append(crs.getString("contact_mobile2") + "(" + crs.getString("phonetypemobile2") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile2"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (!crs.getString("contact_mobile3").equals("")) {
						Str.append("<td>Mobile 3: ").append(crs.getString("contact_mobile3") + "(" + crs.getString("phonetypemobile3") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile3"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (!crs.getString("contact_mobile4").equals("")) {
						Str.append("<td>Mobile 4: ").append(crs.getString("contact_mobile4") + "(" + crs.getString("phonetypemobile4") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile4"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (!crs.getString("contact_mobile5").equals("")) {
						Str.append("<td>Mobile 5: ").append(crs.getString("contact_mobile5") + "(" + crs.getString("phonetypemobile5") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile5"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp;</td>");
					}
					if (!crs.getString("contact_mobile6").equals("")) {
						Str.append("<td>Mobile 6: ").append(crs.getString("contact_mobile6") + "(" + crs.getString("phonetypemobile6") + ")");
						Str.append(ClickToCall(crs.getString("contact_mobile6"), comp_id)).append("</td>");
					} else {
						Str.append("<td>&nbsp&nbsp;</td>");
					}
					// Str.append("</td>");
					Str.append("</tr>\n");

					if (type.equals("Service")) {
						Str.append("<tr align=center>\n");
						Str.append("<td>Kms: ").append(IndFormat(crs.getString("veh_kms"))).append("</td>\n");
						Str.append("<td>Cal. Kms: ").append(IndFormat(crs.getString("veh_cal_kms"))).append("</td>\n");
						Str.append("<td>Last Service Date: ").append(strToShortDate(crs.getString("veh_lastservice"))).append("</td>\n");
						Str.append("<td>Last Service Kms: ").append(IndFormat(crs.getString("veh_lastservice_kms"))).append("</td>\n");
						// .append("<td colspan=\"3\">&nbsp;</td>\n");
						Str.append("<td>Contactable: ");
						if (crs.getString("veh_contactable_id").equals("1")) {
							Str.append("Yes").append("</td>\n");
						} else if (crs.getString("veh_contactable_id").equals("2")) {
							Str.append("No").append("</td>\n");
						} else if (crs.getString("veh_contactable_id").equals("0")) {
							Str.append("").append("</td>\n");
						}
						Str.append("</td>\n");
						Str.append("<td></td>");
						Str.append("<td></td>");
						Str.append("</tr>");
					}
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

	public String PopulatePsfFeedback1(String comp_id, String vehpsffeedback_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT psffeedbacktype_id, psffeedbacktype_name"
					+ " FROM axela_service_psf_feedbacktype"
					+ " WHERE 1=1"
					+ " ORDER BY psffeedbacktype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psffeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("psffeedbacktype_id"), vehpsffeedback_id));
				Str.append(">").append(crs.getString("psffeedbacktype_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void populateConfigDetails(String comp_id) {
		try {
			StrSql = "SELECT branch_email1,"
					+ " brandconfig_vehfollowup_notcontactable_email_enable,"
					+ " brandconfig_vehfollowup_notcontactable_email_sub,"
					+ " brandconfig_vehfollowup_notcontactable_email_format,"
					+ " brandconfig_vehfollowup_notcontactable_sms_enable,"
					+ " brandconfig_vehfollowup_notcontactable_sms_format,"
					+ " brandconfig_vehfollowup_booking_email_enable, "
					+ " brandconfig_vehfollowup_booking_email_sub,"
					+ " brandconfig_vehfollowup_booking_email_format, "
					+ " brandconfig_vehfollowup_booking_sms_enable, "
					+ " brandconfig_vehfollowup_booking_sms_format"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " WHERE veh_id =" + veh_id
					+ " LIMIT 1";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("strsql===" + StrSql);
			crs.beforeFirst();
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");
				brandconfig_vehfollowup_notcontactable_email_enable = crs.getString("brandconfig_vehfollowup_notcontactable_email_enable");
				brandconfig_vehfollowup_notcontactable_email_sub = crs.getString("brandconfig_vehfollowup_notcontactable_email_sub");
				brandconfig_vehfollowup_notcontactable_email_format = crs.getString("brandconfig_vehfollowup_notcontactable_email_format");
				brandconfig_vehfollowup_notcontactable_sms_enable = crs.getString("brandconfig_vehfollowup_notcontactable_sms_enable");
				brandconfig_vehfollowup_notcontactable_sms_format = crs.getString("brandconfig_vehfollowup_notcontactable_sms_format");
				brandconfig_vehfollowup_booking_email_enable = crs.getString("brandconfig_vehfollowup_booking_email_enable");
				brandconfig_vehfollowup_booking_email_sub = crs.getString("brandconfig_vehfollowup_booking_email_sub");
				brandconfig_vehfollowup_booking_email_format = crs.getString("brandconfig_vehfollowup_booking_email_format");
				brandconfig_vehfollowup_booking_sms_enable = crs.getString("brandconfig_vehfollowup_booking_sms_enable");
				brandconfig_vehfollowup_booking_sms_format = crs.getString("brandconfig_vehfollowup_booking_sms_format");
				// contact_email1 = crs.getString("contact_email1");
				// contact_mobile1 = crs.getString("contact_mobile1");
				// SOP("brandconfig_vehfollowup_notcontactable_enable==" + brandconfig_vehfollowup_notcontactable_enable);
				// SOP("brandconfig_vehfollowup_notcontactable_email_sub==" + brandconfig_vehfollowup_notcontactable_email_sub);
				// SOP("brandconfig_vehfollowup_notcontactable_email_format==" + brandconfig_vehfollowup_notcontactable_email_format);
				// SOP("brandconfig_vehfollowup_notcontactable_sms_format==" + brandconfig_vehfollowup_notcontactable_sms_format);
				// SOP("brandconfig_vehfollowup_booking_enable==" + brandconfig_vehfollowup_booking_enable);
				// SOP("brandconfig_vehfollowup_booking_email_sub==" + brandconfig_vehfollowup_booking_email_sub);
				// SOP("brandconfig_vehfollowup_booking_email_format==" + brandconfig_vehfollowup_booking_email_format);
				// SOP("brandconfig_vehfollowup_booking_sms_format==" + brandconfig_vehfollowup_booking_sms_format);

			}
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulatePhoneType(String comp_id, String contact_mobile_phonetype_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT phonetype_id, phonetype_name"
					+ " FROM axela_phonetype"
					+ " GROUP BY phonetype_id"
					+ " ORDER BY phonetype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("phonetype_id"));
				Str.append(StrSelectdrop(crs.getString("phonetype_id"), contact_mobile_phonetype_id));
				Str.append(">").append(crs.getString("phonetype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateInsuranceType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(StrSelectdrop(crs.getString("insurtype_id"), veh_insurtype_id));
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

}