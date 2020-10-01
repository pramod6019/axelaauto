package axela.sales;
// $at!sh

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder_Din_Update extends Connect {

	public String add = "", emp_id = "0";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";

	public String so_id = "0";
	public String so_din_del_location = "0";
	public String so_din_reg = "";
	// public String so_din_accessories_free = "";
	// public String so_din_accessories_paid = "";
	public String so_din_promised_time = "", promisedDeliveryTime = "";
	// public String so_din_packages = "";
	public String so_din_special_remarks = "";

	public String so_din_rto_pincode = "";
	public String so_din_perm_rto_name = "";
	public String so_din_rto_code = "";
	public String so_din_rto = "";
	public String so_din_taxi_permit = "";
	public String so_din_registration_type = "";
	public String so_din_spouse_name = "";
	public String so_din_registration_addr_perm = "";
	public String so_din_registration_addr_temp = "";

	public String so_din_insurance_details = "";
	public String so_din_insurance_type = "";
	public String so_din_insurance_noclaimbonus = "";
	public String so_din_insurance_nominee_age = "";
	public String so_din_insurance_nominee_name = "";
	public String so_din_insurance_nominee_relation = "";

	public String so_din_entry_id = "0", entry_by = "";
	public String so_din_entry_date = "";
	public String so_din_modified_id = "0", modified_by = "";
	public String so_din_modified_date = "";
	public String brandconfig_din_email_enable = "";
	public String brandconfig_din_email_sub = "";
	public String brandconfig_din_email_format = "";
	public String brandconfig_din_sms_format = "";
	public String brandconfig_din_sms_enable = "";
	public String branch_email1 = "", branch_mobile1 = "";
	public String branch_din_email = "", branch_din_mobile = "";
	public String din_mobile[] = null;
	public String emailmsg = "", emailsub = "";
	public String entry_date = "";
	public String modified_date = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String din_so_id = "0";
	public String QueryString = "";
	public String status = "";
	public String so_invoice_request = "0", requestinvoice = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			QueryString = PadQuotes(request.getQueryString());
			update = PadQuotes(request.getParameter("Update"));
			addB = PadQuotes(request.getParameter("add_button"));
			updateB = PadQuotes(request.getParameter("update_button"));
			msg = PadQuotes(request.getParameter("msg"));
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			PopulateConfigDetails();
			if (!so_id.equals("0")) {
				StrSql = "SELECT so_din_entry_id FROM " + compdb(comp_id) + "axela_sales_so" + " WHERE so_id = " + so_id + "";
				so_din_entry_id = CNumeric(ExecuteQuery(StrSql));
				if (!so_din_entry_id.equals("0")) {
					status = "Update";
				} else {
					status = "Add";
				}
			}

			if (!so_id.equals("0")) {
				if (status.equals("Add")) {
					if (!"yes".equals(addB)) {
						PopulateFields(response);
					} else if ("yes".equals(addB)) {
						// CheckPerm("emp_sales_order_add", request, response);
						so_din_entry_id = session.getAttribute("emp_id").toString();
						so_din_entry_date = ToLongDate(kknow());
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Din added successfully!" + msg + ""));
						}
					}
				} else if (status.equals("Update")) {
					if (!"yes".equals(updateB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB)) {
						// CheckPerm("emp_sales_order_add", request, response);
						so_din_modified_id = session.getAttribute("emp_id").toString();
						so_din_modified_date = ToLongDate(kknow());
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Din updated successfully!" + msg + ""));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		so_din_del_location = PadQuotes(request.getParameter("dr_din_deliveryloctype_id"));
		so_din_reg = PadQuotes(request.getParameter("dr_so_din_reg"));
		promisedDeliveryTime = PadQuotes(request.getParameter("txt_so_din_promised_time"));
		// so_din_packages = PadQuotes(request.getParameter("txt_so_din_packages"));
		so_din_special_remarks = PadQuotes(request.getParameter("txt_so_din_special_remarks"));
		// so_din_accessories_free = PadQuotes(request.getParameter("txt_so_din_accessories_free"));
		// so_din_accessories_paid = PadQuotes(request.getParameter("txt_so_din_accessories_paid"));

		so_din_spouse_name = PadQuotes(request.getParameter("txt_so_din_spouse_name"));
		so_din_insurance_details = PadQuotes(request.getParameter("dr_so_din_insurance_details"));
		so_din_insurance_type = PadQuotes(request.getParameter("txt_so_din_insurance_type"));
		so_din_insurance_noclaimbonus = PadQuotes(request.getParameter("txt_so_din_insurance_noclaimbonus"));
		so_din_insurance_nominee_age = PadQuotes(request.getParameter("txt_so_din_insurance_nominee_age"));
		so_din_insurance_nominee_name = PadQuotes(request.getParameter("txt_so_din_insurance_nominee_name"));
		so_din_insurance_nominee_relation = PadQuotes(request.getParameter("txt_so_din_insurance_nominee_relation"));
		so_din_perm_rto_name = PadQuotes(request.getParameter("txt_so_din_perm_rto_name"));

		so_din_rto_code = PadQuotes(request.getParameter("txt_so_din_rto_code"));
		so_din_rto_pincode = PadQuotes(request.getParameter("txt_so_din_rto_pincode"));
		so_din_rto = PadQuotes(request.getParameter("txt_so_din_rto"));
		so_din_taxi_permit = PadQuotes(request.getParameter("dr_so_din_taxi_permit"));
		so_din_registration_type = PadQuotes(request.getParameter("txt_so_din_registration_type"));
		so_din_registration_addr_perm = PadQuotes(request.getParameter("txt_so_din_registration_addr_perm"));
		so_din_registration_addr_temp = PadQuotes(request.getParameter("txt_so_din_registration_addr_temp"));

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		requestinvoice = PadQuotes(request.getParameter("requestinvoice"));
	}

	protected void CheckForm() {
		msg = "";

		if (so_din_del_location.equals("0")) {
			msg += "<br>Select Delivery Location!";
		}

		if (!promisedDeliveryTime.equals("")) {
			if (isValidDateFormatShort(promisedDeliveryTime)) {
				so_din_promised_time = ConvertShortDateToStr(promisedDeliveryTime);
			} else {
				msg += "<br>Enter Valid Promised Delivery Date!";
			}
		} else {
			msg += "<br>Enter Promised Delivery Date!";
		}

		if (so_din_reg.equals("0")) {
			msg += "<br>Select Registration Mode!";
		}

	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT "
					+ " so_promise_date,"
					+ " so_din_promised_time,"
					+ " so_din_del_location, "
					+ " so_din_reg, "
					+ " so_din_packages , "
					+ " so_din_special_remarks , "
					+ " so_din_accessories_free , "
					+ " so_din_accessories_paid , "
					+ " so_din_spouse_name, "
					+ " so_din_registration_addr_perm, "
					+ " so_din_registration_addr_temp, "
					+ " so_din_rto_pincode, "
					+ " so_din_perm_rto_name, "
					+ " so_din_rto_code, "
					+ " so_din_rto, "
					+ " so_din_taxi_permit, "
					+ " so_din_registration_type, "
					+ " so_din_insurance_details, "
					+ " so_din_insurance_type, "
					+ " so_din_insurance_noclaimbonus, "
					+ " so_din_insurance_nominee_age, "
					+ " so_din_insurance_nominee_name, "
					+ " so_din_insurance_nominee_relation, "
					+ " so_din_entry_id, "
					+ " so_din_entry_date, "
					+ " so_din_modified_date, "
					+ " so_din_modified_id, "
					+ " so_invoice_request,"
					+ " COALESCE(CONCAT(customer_address, ', ', state_name, ', ', city_name, '-', customer_pin), '') AS customer_address "
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE so_id = " + so_id + "";
			// SOP("PopulateFields----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				so_din_del_location = crs.getString("so_din_del_location");
				so_din_reg = crs.getString("so_din_reg");

				// so_din_packages = crs.getString("so_din_packages");
				so_din_special_remarks = crs.getString("so_din_special_remarks");
				// so_din_accessories_free = crs.getString("so_din_accessories_free");
				// so_din_accessories_paid = crs.getString("so_din_accessories_paid");

				// Get the tentative delivery date when status is ADD and store in so_din_promised_time
				// Get the customer address from customer on ADD status and store in so_din_registration_addr_perm
				if (status.equals("Add")) {
					so_din_promised_time = crs.getString("so_promise_date");
					so_din_registration_addr_perm = crs.getString("customer_address");
				} else {
					so_din_promised_time = crs.getString("so_din_promised_time");
					so_din_registration_addr_perm = crs.getString("so_din_registration_addr_perm");
				}
				promisedDeliveryTime = strToShortDate(so_din_promised_time);
				so_din_spouse_name = crs.getString("so_din_spouse_name");
				so_din_registration_addr_temp = crs.getString("so_din_registration_addr_temp");
				so_din_rto_pincode = crs.getString("so_din_rto_pincode");
				so_din_perm_rto_name = crs.getString("so_din_perm_rto_name");
				so_din_rto_code = crs.getString("so_din_rto_code");
				so_din_rto = crs.getString("so_din_rto");
				so_din_taxi_permit = crs.getString("so_din_taxi_permit");
				so_din_registration_type = crs.getString("so_din_registration_type");
				so_din_insurance_details = crs.getString("so_din_insurance_details");
				so_din_insurance_type = crs.getString("so_din_insurance_type");
				so_din_insurance_noclaimbonus = crs.getString("so_din_insurance_noclaimbonus");
				so_din_insurance_nominee_age = crs.getString("so_din_insurance_nominee_age");
				so_din_insurance_nominee_name = crs.getString("so_din_insurance_nominee_name");
				so_din_insurance_nominee_relation = crs.getString("so_din_insurance_nominee_relation");
				so_invoice_request = crs.getString("so_invoice_request");
				so_din_entry_id = crs.getString("so_din_entry_id");
				if (!so_din_entry_id.equals("")) {
					entry_by = Exename(comp_id, Integer.parseInt(so_din_entry_id));
				}
				entry_date = strToLongDate(crs.getString("so_din_entry_date"));
				so_din_modified_id = crs.getString("so_din_modified_id");
				if (!so_din_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(so_din_modified_id));
					modified_date = strToLongDate(crs.getString("so_din_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
					+ " SET"
					+ " so_din_del_location = " + so_din_del_location + ","
					+ " so_din_reg = '" + so_din_reg + "',"
					+ " so_din_promised_time = '" + so_din_promised_time + "',"
					// + " so_din_packages = '" + so_din_packages + "',"
					+ " so_din_special_remarks = '" + so_din_special_remarks + "',"
					// + " so_din_accessories_free = '" + so_din_accessories_free + "',"
					// + " so_din_accessories_paid = '" + so_din_accessories_paid + "',"
					+ " so_din_spouse_name = '" + so_din_spouse_name + "',"
					+ " so_din_registration_addr_perm = '" + so_din_registration_addr_perm + "',"
					+ " so_din_registration_addr_temp = '" + so_din_registration_addr_temp + "',"
					+ " so_din_rto_pincode = '" + so_din_rto_pincode + "',"
					+ " so_din_perm_rto_name = '" + so_din_perm_rto_name + "',"
					+ " so_din_rto_code = '" + so_din_rto_code + "',"
					+ " so_din_rto = '" + so_din_rto + "',"
					+ " so_din_taxi_permit = '" + so_din_taxi_permit + "',"
					+ " so_din_registration_type = '" + so_din_registration_type + "',"
					+ " so_din_insurance_details = '" + so_din_insurance_details + "',"
					+ " so_din_insurance_type = '" + so_din_insurance_type + "',"
					+ " so_din_insurance_noclaimbonus = '" + so_din_insurance_noclaimbonus + "',"
					+ " so_din_insurance_nominee_age = '" + so_din_insurance_nominee_age + "',"
					+ " so_din_insurance_nominee_name = '" + so_din_insurance_nominee_name + "',"
					+ " so_din_insurance_nominee_relation = '" + so_din_insurance_nominee_relation + "',";

			if (requestinvoice.equals("invoice")) {
				StrSql += " so_invoice_request = '1',"
						+ " so_invoice_request_time = '" + ToLongDate(kknow()) + "',"
						+ " so_invoice_request_emp_id = '" + emp_id + "',";
			}

			// SOP("status==" + status);
			if (status.equals("Add")) {
				StrSql += " so_din_entry_id = '" + so_din_entry_id + "',"
						+ " so_din_entry_date = '" + so_din_entry_date + "'";
			} else if (status.equals("Update")) {
				StrSql += " so_din_modified_id = '" + so_din_modified_id + "',"
						+ " so_din_modified_date = '" + so_din_modified_date + "'";
			}
			StrSql += " WHERE so_id = " + so_id + "";
			// SOP("StrSql==Update DIN==" + StrSql);
			updateQuery(StrSql);

			if (status.equals("Add")) {
				if (brandconfig_din_email_enable.equals("1") && !brandconfig_din_email_sub.equals("")
						&& !brandconfig_din_email_format.equals("") && !branch_din_email.equals("")) {
					SendEmail();
				}
				// SOP("brandconfig_din_sms_enable-----------------" + brandconfig_din_sms_enable);
				// SOP("brandconfig_din_sms_format-----------------" + brandconfig_din_sms_format);
				if (brandconfig_din_sms_enable.equals("1") && !brandconfig_din_sms_format.equals("")
						&& !branch_din_mobile.equals("")) {
					din_mobile = branch_din_mobile.split(",");
					for (int i = 0; i < din_mobile.length; i++) {
						din_mobile[i] = din_mobile[i].replace(" ", "");
						// SOP("din_mobile[i]-----------------" + din_mobile[i]);
						SendSMS(din_mobile[i]);
					}

				}
			}
		}
	}
	protected void SendEmail() {
		emailmsg = (brandconfig_din_email_format);
		emailsub = (brandconfig_din_email_sub);

		emailsub = "REPLACE('" + emailsub + "', '[SOID]',so_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]',customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]',customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERDOB]',IF(contact_dob = '', '', COALESCE(DATE_FORMAT(contact_dob,'%d/%m/%Y'), '')))";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERANNIVERSARY]', IF(contact_anniversary = '', '',COALESCE(DATE_FORMAT(contact_anniversary, '%d/%m/%Y'), '')))";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHNAME]',branch_name)";
		emailsub = "REPLACE(" + emailsub + ", '[MODELNAME]',model_name)";
		emailsub = "REPLACE(" + emailsub + ", '[VARIANTNAME]',item_name)";
		emailsub = "REPLACE(" + emailsub + ", '[COLOR]',COALESCE(option_name, ''))";
		emailsub = "REPLACE(" + emailsub + ", '[DELIVERYLOCATION]',so_din_del_location)";
		emailsub = "REPLACE(" + emailsub + ", '[CHASSISNUMBER]',stock_chassis_no)";
		emailsub = "REPLACE(" + emailsub + ", '[ENGINENUMBER]',stock_engine_no)";
		emailsub = "REPLACE(" + emailsub + ", '[FREE]',so_din_accessories_free)";
		emailsub = "REPLACE(" + emailsub + ", '[PAID]',so_din_accessories_paid)";
		emailsub = "REPLACE(" + emailsub + ", '[STARPACKAGES]',so_din_packages)";
		emailsub = "REPLACE(" + emailsub + ", '[PROMISEDTIME]',so_din_promised_time)";
		emailsub = "REPLACE(" + emailsub + ", '[SALESPERSON]',salesexe.emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[SPECIALOCCASION]',so_din_special_remarks)";
		emailsub = "REPLACE(" + emailsub + ", '[REGISTRATIONMODE]',so_din_reg)";
		emailsub = "REPLACE(" + emailsub + ", '[REGISTRATIONNUMBER]',so_din_reg_reg_no)";
		emailsub = "REPLACE(" + emailsub + ", '[PROMISEDDELIVERYDATE]',IF(so_promise_date = '', '', COALESCE(DATE_FORMAT(so_promise_date, '%d/%m/%Y'), '')))";

		emailmsg = "REPLACE('" + emailmsg + "', '[SOID]',so_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERDOB]',IF(contact_dob = '', '', COALESCE(DATE_FORMAT(contact_dob, '%d/%m/%Y'), '')))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERANNIVERSARY]',IF(contact_anniversary = '', '',COALESCE(DATE_FORMAT(contact_anniversary, '%d/%m/%Y'), '')))";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHNAME]', branch_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[MODELNAME]', model_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[VARIANTNAME]', item_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[COLOR]',COALESCE(option_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ", '[DELIVERYLOCATION]',so_din_del_location)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CHASSISNUMBER]',stock_chassis_no)";
		emailmsg = "REPLACE(" + emailmsg + ", '[ENGINENUMBER]',stock_engine_no)";
		emailmsg = "REPLACE(" + emailmsg + ", '[FREE]',so_din_accessories_free)";
		emailmsg = "REPLACE(" + emailmsg + ", '[PAID]',so_din_accessories_paid)";
		emailmsg = "REPLACE(" + emailmsg + ", '[STARPACKAGES]',so_din_packages)";
		emailmsg = "REPLACE(" + emailmsg + ", '[PROMISEDTIME]',so_din_promised_time)";
		emailmsg = "REPLACE(" + emailmsg + ", '[SALESPERSON]',salesexe.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[SPECIALOCCASION]',so_din_special_remarks)";
		emailmsg = "REPLACE(" + emailmsg + ", '[REGISTRATIONMODE]',so_din_reg)";
		emailmsg = "REPLACE(" + emailmsg + ", '[REGISTRATIONNUMBER]',so_din_reg_reg_no)";
		emailmsg = "REPLACE(" + emailmsg + ", '[PROMISEDDELIVERYDATE]',IF(so_promise_date = '','',COALESCE(DATE_FORMAT(so_promise_date,'%d/%m/%Y'),'')))";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " so_contact_id ,"
					+ " " + emp_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " branch_email1 ,"
					+ " branch_din_email,"
					+ " CONCAT(COALESCE(salesexe.emp_email1, ''), ',', COALESCE(manager.emp_email1, ''), ',', COALESCE(psfexe.emp_email1, '')),"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp salesexe ON salesexe.emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS psfexe ON psfexe.emp_id = team_psf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans on trans_vehstock_id =vehstock_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option on option_id =trans_option_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id,"
					+ compdb(comp_id) + " axela_comp"
					+ " WHERE so_id = " + so_id // + " " + BranchAccess + ""
					+ " LIMIT 1";
			// SOP("StrSql--Email-----1-----" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_emp_id, "
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc," // Sales Consultant/Manager/Psf Executives
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql---Email----2-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	protected void SendSMS(String din_mobile) {
		String smsmsg = (brandconfig_din_sms_format);

		smsmsg = "REPLACE('" + smsmsg + "', '[SOID]',so_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]',customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]',customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERDOB]',IF(contact_dob = '','',COALESCE(DATE_FORMAT(contact_dob,'%d/%m/%Y'),'')))";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERANNIVERSARY]',IF(contact_anniversary = '','',COALESCE(DATE_FORMAT(contact_anniversary,'%d/%m/%Y'),'')))";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHNAME]',branch_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[MODELNAME]',model_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[VARIANTNAME]',item_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[COLOR]',COALESCE(option_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ", '[DELIVERYLOCATION]',so_din_del_location)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CHASSISNUMBER]',stock_chassis_no)";
		smsmsg = "REPLACE(" + smsmsg + ", '[ENGINENUMBER]',stock_engine_no)";
		smsmsg = "REPLACE(" + smsmsg + ", '[FREE]',so_din_accessories_free)";
		smsmsg = "REPLACE(" + smsmsg + ", '[PAID]',so_din_accessories_paid)";
		smsmsg = "REPLACE(" + smsmsg + ", '[STARPACKAGES]',so_din_packages)";
		smsmsg = "REPLACE(" + smsmsg + ", '[PROMISEDTIME]',so_din_promised_time)";
		smsmsg = "REPLACE(" + smsmsg + ", '[SALESPERSON]',emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[SPECIALOCCASION]',so_din_special_remarks)";
		smsmsg = "REPLACE(" + smsmsg + ", '[REGISTRATIONMODE]',so_din_reg)";
		smsmsg = "REPLACE(" + smsmsg + ", '[REGISTRATIONNUMBER]',so_din_reg_reg_no)";
		smsmsg = "REPLACE(" + smsmsg + ", '[PROMISEDDELIVERYDATE]',IF(so_promise_date = '','',COALESCE(DATE_FORMAT(so_promise_date,'%d/%m/%Y'),'')))";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " so_contact_id ,"
					+ " " + emp_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ "  '" + din_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans on trans_vehstock_id =vehstock_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option on option_id =trans_option_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id,"
					+ compdb(comp_id) + " axela_comp"
					+ " WHERE so_id = " + so_id + " " + BranchAccess + ""
					+ " LIMIT 1";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_emp_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("-------Sendsms-----------" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE(brandconfig_din_email_enable, '') AS brandconfig_din_email_enable,"
					+ " COALESCE(brandconfig_din_email_sub, '') AS brandconfig_din_email_sub,"
					+ " COALESCE(brandconfig_din_email_format, '') AS brandconfig_din_email_format,"
					+ " COALESCE(brandconfig_din_sms_enable,'') AS brandconfig_din_sms_enable,"
					+ " COALESCE(brandconfig_din_sms_format, '') AS brandconfig_din_sms_format,"
					+ " branch_email1, branch_mobile1,"
					+ " branch_din_email, branch_din_mobile"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
					+ " LEFT JOIN axela_brand on brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config on brandconfig_brand_id = brand_id,"
					+ compdb(comp_id) + "axela_config, "
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE"
					+ " so_id = " + so_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql----PopulateConfigDetails------------" + StrSqlBreaker(StrSql));
			while (crs.next()) {
				brandconfig_din_email_enable = crs.getString("brandconfig_din_email_enable");
				brandconfig_din_email_sub = crs.getString("brandconfig_din_email_sub");
				brandconfig_din_email_format = crs.getString("brandconfig_din_email_format");
				brandconfig_din_sms_enable = crs.getString("brandconfig_din_sms_enable");
				brandconfig_din_sms_format = crs.getString("brandconfig_din_sms_format");
				branch_email1 = crs.getString("branch_email1");
				branch_mobile1 = crs.getString("branch_mobile1");
				branch_din_email = crs.getString("branch_din_email");
				branch_din_mobile = crs.getString("branch_din_mobile");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public String PopulateRegistrationType() {
	// String mode = "<option value = 0>Select</option>\n";
	// mode += "<option value = \"Self\"" + StrSelectdrop("Self", so_din_reg) + ">Self</option>\n";
	// mode += "<option value = \"Company\"" + StrSelectdrop("2", so_din_reg) + ">Company</option>\n";
	// return mode;
	// }

	public String PopulateDeliveryLocationType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>\n");
		Str.append("<option value = \"1\"").append(StrSelectdrop("1", so_din_del_location)).append(">Customer Place</option>\n");
		Str.append("<option value = \"2\"").append(StrSelectdrop("2", so_din_del_location)).append(">Showroom</option>\n");
		return Str.toString();
	}

	public String PopulateTaxiPermit() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>\n");
		Str.append("<option value = \"1\"").append(StrSelectdrop("1", so_din_taxi_permit)).append(">Yes</option>\n");
		Str.append("<option value = \"2\"").append(StrSelectdrop("2", so_din_taxi_permit)).append(">No</option>\n");
		return Str.toString();
	}

	public String PopulateInsuranceDetails() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>\n");
		Str.append("<option value = \"Dealer\"").append(StrSelectdrop("Dealer", so_din_insurance_details)).append(">Dealer</option>\n");
		Str.append("<option value = \"Customer\"").append(StrSelectdrop("Customer", so_din_insurance_details)).append(">Customer</option>\n");
		return Str.toString();
	}

}
