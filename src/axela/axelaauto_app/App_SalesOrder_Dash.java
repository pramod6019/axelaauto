package axela.axelaauto_app;
//Bhagwan Singh 20th july 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_SalesOrder_Dash extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String msg = "";
	public String so_desc = "";
	public String StrHTML = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String so_id = "0";
	public String finnancetrans_desc = "";
	public String so_date = "", enquiry_enquirytype_id = "0";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String quote_link = "";
	public String enquiry_link = "";
	public String so_fintype_id = "0";
	public String so_finstatus_id = "0";
	public String so_finstatus_date = "";
	public String so_finstatus_emp_id = "0";
	public String so_finstatus_loan_amt = "";
	public String so_finstatus_emi_value = "";
	public String so_finstatus_advance = "";
	public String so_finstatus_disbursed_date = "";
	public String so_finstatus_disbursed_amt = "";
	public String so_finstatus_tenure = "";
	public String so_finstatus_bank_id = "0";
	public String so_finstatus_scheme = "";
	public String so_finstatus_desc = "";
	public String so_finstatus_process_fee = "";
	public String so_finstatus_subvention = "";
	public String so_finstatus_gross_payout = "";
	public String so_finstatus_income_on_payment = "";
	public String so_finstatus_bank_rack_rate = "";
	public String so_finstatus_customer_roi = "";
	public String so_finstatus_occupation = "";
	public String so_finstatus_industry = "";
	public String so_finstatus_income_asperdoc = "";
	public String so_finstatus_dob = "";
	public String so_finstatus_sex = "";
	public String so_promise_date = "";
	// vehicle so
	public String invoice_id = "0";
	public String invoice_date = "";
	public String so_reg_salesfile_received_date = "", salesfile_received_date = "";
	public String so_reg_reg_no_choice = "";
	public String so_reg_rto_file = "", rto_file = "";
	public String so_reg_temp_reg_date = "", temp_reg_date = "";
	public String so_reg_temp_reg_no = "";
	public String so_reg_hsrp_received_date = "", hsrp_received_date = "";
	public String so_reg_hsrp_install_date = "", hsrp_install_date = "";
	public String so_reg_hsrp_fitment_place = "";
	public String so_reg_hsrp_fitment_loc = "";
	public String so_reg_perm_reg_date = "", perm_reg_date = "";
	public String so_reg_perm_reg_no = "";
	public String so_reg_rc_received_date = "", rc_received_date = "";
	public String so_reg_rc_handover_date = "", rc_handover_date = "";
	public String so_din_reg = "";
	public String so_din_reg_state_id = "0";
	public String so_din_reg_state = "";
	public String so_reg_salesfile_received_tat = "0";
	public String so_finstatus_disbursed_tat = "";
	public String so_reg_rto_file_tat = "0";
	public String so_reg_temp_reg_tat = "0";
	public String so_reg_hsrp_received_tat = "0";
	public String so_reg_hsrp_install_tat = "0";
	public String so_reg_perm_reg_tat = "0";
	public String so_reg_rc_received_tat = "0";
	public String so_reg_rc_handover_tat = "0";
	public String sodate = "";
	public String so_option_id = "0";
	public String so_allot_no = "";
	public String so_booking_amount = "";
	public String so_reg_date = "", so_regdate = "", so_dob = "";
	public String so_pan = "";
	public String so_hypothecation = "";
	public String so_fincomp_id = "";
	public String so_finance_amt = "";
	public String so_inscomp_id = "";
	public String so_insurance_amt = "";
	public String so_file_status = "";
	public String so_downpayment = "";
	public String so_payment_date = "", so_paymentdate = "";
	public String so_promisedate = "", so_retaildate = "", so_retailtime = "";
	public String so_cancel_date = "", so_canceldate = "";
	public String so_open = "0";
	public String so_critical = "0";
	// public String so_hni = "0";
	public String so_reg_hsrp = "0";
	public String so_din_accessories_special_remarks = "";
	public String so_refno = "";
	public String so_active = "0";
	public String so_notes = "";
	public String cancelreason_name = "";
	public String cinstatus_name = "";
	public String so_entry_id = "0";
	public String so_entry_by = "";
	public String so_entry_date = "";
	public String so_modified_id = "0";
	public String so_modified_by = "";
	public String so_modified_date = "";
	public String so_delivered_date = "", so_delivereddate = "", so_deliveredtime = "";
	public String so_reg_no = "";
	public String item_id = "0";
	public String so_vehstock_id = "0";
	public String so_customer_id = "0";
	public String vehstock_comm_no = "";
	public String so_preownedstock_id = "0";
	public String emp_receipt_access = "0";
	public String so_cancelreason_id = "0";
	public String contact_dob = "";
	public String contact_anniversary = "";
	public String dropDueDay = "", dropDueMonth = "", dropDueYear = "2012";
	public String dropDueDays = "", dropDueMonths = "", dropDueYears = "";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String customer_name = "";
	public String customer_id = "0";
	public String customer_communication = "";
	public String customer_address = "";
	public String customer_landmark = "";
	public String customer_notes = "";
	public String customer_active = "";
	public String customer_exe = "";
	public String customer_since = "";
	public String contact_mobile1 = "";
	public String group = "";
	public String config_sales_enquiry = "";
	public String comp_email_enable = "";
	public String config_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sms_enable = "", access = "", emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			// emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_id = CNumeric(GetSession("emp_id", request));
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
				ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				access = ReturnPerm(comp_id, "emp_sales_order_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				msg = PadQuotes(request.getParameter("msg"));

				if (ReturnPerm(comp_id, "emp_receipt_access", request).equals("1")) {
					emp_receipt_access = "1";
				}

				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				StrSql = "SELECT so_id, emp_id, CONCAT(emp_name,' (', emp_ref_no, ')') AS customer_exe"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id
						+ BranchAccess
						+ ExeAccess;
				StrSql += " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				// SOP("StrSql=================" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
					}
					PopulateFields(request, response);
					// PopulateSOFinStatusDetails();
					// PopulateSORegistrationDetails();
				} else {
					// response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT so_id, so_date, so_branch_id, so_payment_date, so_dob, so_promise_date, so_delivered_date,"
					// + " so_critical,"
					+ " so_open, so_refno, so_pan, "
					// + " so_hypothecation, "
					+ " so_fincomp_id, so_finance_amt, so_option_id, so_allot_no, so_booking_amount, "
					+ " so_retail_date, so_reg_no, so_reg_date, "
					// + " so_insurance_amt, "
					// + " so_file_status, "
					// + " so_downpayment, "
					+ " so_po, so_desc, so_emp_id, so_active, so_notes,"
					+ "	COALESCE((SELECT cancelreason_name FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " WHERE cancelreason_id = so_cancelreason_id), '' )  AS cancelreason_name,"
					+ " COALESCE(( SELECT cinstatus_name FROM " + compdb(comp_id) + "axela_sales_so_cin_status"
					+ " WHERE cinstatus_id = so_cinstatus_id ), '')  AS cinstatus_name,"
					+ " so_entry_id, "
					// + " so_din_accessories_special_remarks, "
					+ " contact_anniversary, contact_dob, contact_mobile1,"
					+ " so_entry_date, so_modified_id, so_modified_date, so_active, so_cancel_date, so_cancelreason_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, so_promise_date,"
					+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name,"
					+ " rateclass_id, soitem_item_id, customer_name, customer_id, contact_id,"
					+ " COALESCE(so_quote_id, '0') AS so_quote_id, so_delivered_date,"
					+ " so_grandtotal, so_discamt, so_netamt, so_exprice, so_totaltax, so_vehstock_id,"
					+ " so_delivered_date, "
					+ " so_preownedstock_id, "
					+ " COALESCE(enquiry_enquirytype_id, 0)AS enquiry_enquirytype_id,"
					// + " so_hni, "
					+ " so_reg_hsrp,"
					// + " COALESCE(stock_comm_no, 0) AS vehstock_comm_no, "
					+ " so_enquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " WHERE so_id = " + so_id
					+ BranchAccess
					+ ExeAccess;
			// SOP("StrSql------PopulateFields------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_id = crs.getString("so_id");
					so_customer_id = crs.getString("customer_id");
					so_date = crs.getString("so_date");
					sodate = strToShortDate(so_date);
					contact_dob = crs.getString("contact_dob");
					contact_mobile1 = crs.getString("contact_mobile1");
					dropDueDay = SplitDate(contact_dob);
					dropDueMonth = SplitMonth(contact_dob);
					dropDueYear = SplitYear(contact_dob);
					contact_anniversary = crs.getString("contact_anniversary");
					dropDueDays = SplitDate(contact_anniversary);
					dropDueMonths = SplitMonth(contact_anniversary);
					dropDueYears = SplitYear(contact_anniversary);
					link_customer_name = crs.getString("customer_name");
					link_contact_name = crs.getString("contact_name");
					enquiry_link = crs.getString("so_enquiry_id");
					quote_link = crs.getString("so_quote_id");
					so_payment_date = crs.getString("so_payment_date");
					so_paymentdate = strToShortDate(so_payment_date);
					so_promise_date = crs.getString("so_promise_date");
					so_promisedate = strToShortDate(so_promise_date);
					so_retaildate = crs.getString("so_retail_date");
					if (!so_retaildate.equals("")) {
						so_retaildate = strToLongDate(so_retaildate);
						so_retailtime = so_retaildate.substring(11, 16);
						so_retaildate = so_retaildate.substring(0, 10);
					}
					// so_retaildate = strToShortDate(so_retaildate);
					so_delivereddate = crs.getString("so_delivered_date");
					if (!so_delivereddate.equals("")) {
						so_delivereddate = strToLongDate(so_delivereddate);
						so_deliveredtime = so_delivereddate.substring(11, 16);
						so_delivereddate = so_delivereddate.substring(0, 10);
					}
					so_vehstock_id = crs.getString("so_vehstock_id");
					so_preownedstock_id = crs.getString("so_preownedstock_id");
					item_id = crs.getString("soitem_item_id");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					// vehstock_comm_no = crs.getString("vehstock_comm_no");
					so_cancel_date = crs.getString("so_cancel_date");
					so_canceldate = strToShortDate(so_cancel_date);
					so_cancelreason_id = crs.getString("so_cancelreason_id");
					so_option_id = crs.getString("so_option_id");
					so_allot_no = crs.getString("so_allot_no");
					so_booking_amount = crs.getString("so_booking_amount");
					so_reg_no = crs.getString("so_reg_no");
					so_reg_date = strToShortDate(crs.getString("so_reg_date"));
					so_dob = strToShortDate(crs.getString("so_dob"));
					so_reg_hsrp = crs.getString("so_reg_hsrp");
					so_open = crs.getString("so_open");
					so_refno = crs.getString("so_refno");
					so_active = crs.getString("so_active");
					so_pan = crs.getString("so_pan");
					// so_hypothecation = crs.getString("so_hypothecation");
					so_fincomp_id = crs.getString("so_fincomp_id");
					so_finance_amt = crs.getString("so_finance_amt");
					// so_inscomp_id = crs.getString("so_inscomp_id");
					// so_insurance_amt = crs.getString("so_insurance_amt");
					// so_file_status = crs.getString("so_file_status");
					// so_downpayment = crs.getString("so_downpayment");
					so_desc = crs.getString("so_desc");
					so_notes = crs.getString("so_notes");
					cancelreason_name = crs.getString("cancelreason_name");
					cinstatus_name = crs.getString("cinstatus_name");
					// so_din_accessories_special_remarks =
					// crs.getString("so_din_accessories_special_remarks");
					so_entry_id = crs.getString("so_entry_id");
					if (!so_entry_id.equals("0")) {
						so_entry_by = Exename(comp_id, Integer.parseInt(so_entry_id));
					}
					so_entry_date = strToLongDate(crs.getString("so_entry_date"));
					so_modified_id = crs.getString("so_modified_id");
					if (!so_modified_id.equals("0")) {
						so_modified_by = Exename(comp_id, Integer.parseInt(so_modified_id));
					}
					so_modified_date = strToLongDate(crs.getString("so_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/app-error.jsp?msg=Invalid Sales Order!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFinanceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fintype_id, fintype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
					+ " GROUP BY fintype_id"
					+ " ORDER BY fintype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fintype_id"));
				Str.append(StrSelectdrop(crs.getString("fintype_id"), so_fintype_id));
				Str.append(">").append(crs.getString("fintype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateColour(String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT option_id, option_name"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " WHERE 1 = 1"
				+ " GROUP BY option_id"
				+ " ORDER BY option_id";
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value='0'>Select</option>");
		try {
			while (crs.next()) {
				Str.append("<option value='" + crs.getString("option_id") + "' ");
				Str.append(Selectdrop(crs.getInt("option_id"), so_option_id)).append(">");
				Str.append(crs.getString("option_name")).append("</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCancelReason() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id"));
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), so_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateState() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT state_id, state_name"
	// + " FROM " + compdb(comp_id) + "axela_state"
	// + " ORDER BY state_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<option value=\"0\"> Select </option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("state_id"));
	// Str.append(StrSelectdrop(crs.getString("state_id"), so_din_reg_state_id));
	// Str.append(">").append(crs.getString("state_name")).append("</option>\n");
	// }
	// Str.append("</select>\n");
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App====" + this.getClass().getName());
	// SOPError("Axelaauto-App====" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateRegistrationBy() {
	// String mode = "<option value=\"0\">Select</option>\n";
	// mode += "<option value=\"1\"" + StrSelectdrop("1", so_din_reg) +
	// ">Self</option>\n";
	// mode += "<option value=\"2\"" + StrSelectdrop("2", so_din_reg) + ">" +
	// ClientName + "</option>\n";
	// return mode;
	// }

	public String PopulateFitmentPlace() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", so_reg_hsrp_fitment_place)).append(">Showroom</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", so_reg_hsrp_fitment_place)).append(">Customer Place</option>\n");
		return Str.toString();
	}

	public String PopulateDay() {
		String day = "<option value=-1>--</option>\n";
		for (int i = 1; i <= 31; i++) {
			day += "<option value=" + doublenum(i) + "" + StrSelectdrop(doublenum(i), dropDueDay) + ">" + i + "</option>\n";
		}
		return day;
	}

	public String customerDetails(HttpServletResponse response, String customer_id, String modal) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_name, customer_address, customer_landmark, customer_since,"
					+ " customer_phone1, customer_phone2, customer_phone3, customer_phone4,"
					+ " customer_mobile1, customer_mobile2, customer_fax1, customer_fax2,"
					+ " customer_email1, customer_email2, customer_emp_id,"
					+ " customer_website1, customer_website2, customer_pin, customer_notes,"
					+ " customer_active, COALESCE(city_name, '') AS city_name,"
					+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'),'') AS customer_exe"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = customer_emp_id"
					+ " WHERE customer_id = " + customer_id + ""
					+ " GROUP BY customer_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				while (crs.next()) {

					customer_name = crs.getString("customer_name");
					if (!crs.getString("customer_phone1").equals("")) {
						customer_communication = SplitPhoneNo(crs.getString("customer_phone1"), 4, "T") + "";
					}

					if (!crs.getString("customer_phone2").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_phone2"), 4, "T") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_phone2"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_phone3").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_phone3"), 4, "T") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_phone3"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_phone4").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_phone4"), 4, "T") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_phone4"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_mobile1").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_mobile1"), 5, "M") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_mobile1"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_mobile2").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_mobile2"), 5, "M") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_mobile2"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_fax1").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_fax1"), 4, "F") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_fax1"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_fax2").equals("")) {
						if (!customer_communication.equals("")) {
							customer_communication += "<br>" + SplitPhoneNo(crs.getString("customer_fax2"), 4, "F") + "";
						} else {
							customer_communication = SplitPhoneNo(crs.getString("customer_fax2"), 4, "T") + "";
						}
					}

					if (!crs.getString("customer_email1").equals("")) {
						customer_communication += "<br><a href=mailto:" + crs.getString("customer_email1") + ">" + crs.getString("customer_email1") + "</a>";
					}

					if (!crs.getString("customer_email2").equals("")) {
						customer_communication += "<br><a href=mailto:" + crs.getString("customer_email2") + ">" + crs.getString("customer_email2") + "</a>";
					}

					if (!crs.getString("customer_website1").equals("")) {
						customer_communication += "<br><a href=http://" + crs.getString("customer_website1") + " target=_blank>" + crs.getString("customer_website1") + "</a>";
					}

					if (!crs.getString("customer_website2").equals("")) {
						customer_communication += "<br><a href=http://" + crs.getString("customer_website2") + " target=_blank>" + crs.getString("customer_website2") + "</a>";
					}

					if (!crs.getString("customer_address").equals("")) {
						customer_address = crs.getString("customer_address");
						if (!crs.getString("city_name").equals("")) {
							customer_address += ", " + crs.getString("city_name");
						}
						if (!crs.getString("customer_pin").equals("")) {
							customer_address += " - " + crs.getString("customer_pin");
						}
					}

					if (!crs.getString("customer_landmark").equals("")) {
						customer_landmark = crs.getString("customer_landmark");
					}

					if (!crs.getString("customer_exe").equals("")) {
						customer_exe = crs.getString("customer_exe");
					}

					if (!crs.getString("customer_since").equals("")) {
						customer_since = strToShortDate(crs.getString("customer_since"));
					}

					if (crs.getString("customer_active").equals("0")) {
						customer_active = "<font color=red><b>Inactive</b></font>";
					} else {
						customer_active = "Active";
					}
					customer_notes = crs.getString("customer_notes");

					Str.append("<b>\n<center>").append(customer_name).append(" (").append(customer_id).append(")</center></b>");
					Str.append("<div class=\"col-md-12 col-xs-12\" style=\"border: 2px solid #8E44AD; border-radius: 5px\">");
					if (!customer_communication.equals("")) {
						Str.append("<b>Communication:</b>\n")
								.append("<p>").append((customer_communication)).append("<br>\n")
								.append("</p>\n");
					}
					if (!crs.getString("customer_address").equals("")) {

						Str.append("<b>Address:</b>\n")
								.append("<p>").append((customer_address)).append("<br>\n")
								.append("</p>\n");
					}
					if (!crs.getString("customer_exe").equals("")) {
						Str.append("<b>Sales Consultant:</b>\n")
								.append("<p>").append(customer_exe).append("</p>\n");
					}

					Str.append("<b>Active:</b>")
							.append("<p>").append(customer_active).append("</p>\n")
							.append("</div>\n")
							.append("&nbsp;");
					Str.append(ListContact(customer_id));
				}
			} else {
				msg = "<table>\n<tr>\n<td align=\"center\"><br><br><br><br><font color=red><b>No customer details found!</b></font></td>\n</tr>\n</table>\n";
				Str.append(msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListContact(String enquiry_customer_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		String active = "";
		String address = "", img = "";
		try {
			StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
					+ " contact_jobtitle,"
					+ " contact_phone1,"
					+ " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
					+ " contact_email1, contact_email2,"
					// + " contact_photo,"
					+ " contact_yahoo, contact_msn, contact_aol,"
					+ " contact_address, contact_pin, contact_landmark, contact_dob, contact_active,"
					+ " COALESCE(city_name,'') AS city_name, customer_id, customer_name, coalesce(branch_name,'')as branch,"
					+ " COALESCE(branch_code,'')AS branch_code, contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " WHERE contact_customer_id = " + enquiry_customer_id + BranchAccess;
			// SOP("StrSql---List contacts----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count = count + 1;
				Str.append("<div class=\"col-md-12 col-xs-12\"><br>\n")
						.append("<b><center>Contact List</center></b><br>\n")
						.append("<div class=\"col-md-8 col-xs-8\">\n")
						.append("<b>Name: </b>").append(crs.getString("contact_name")).append(active).append("<br>\n")
						.append("<b>ID: </b>").append(crs.getString("contact_id")).append("<br>\n");
				if (!crs.getString("contact_mobile1").equals("")) {
					Str.append(crs.getString("contact_mobile1")).append("<br>");
				}
				if (!crs.getString("contact_mobile2").equals("")) {
					Str.append(crs.getString("contact_mobile2")).append("<br>");
				}
				if (!crs.getString("contact_phone1").equals("")) {
					Str.append(crs.getString("contact_phone1")).append("<br>");
				}
				if (!crs.getString("contact_phone2").equals("")) {
					Str.append(crs.getString("contact_phone2")).append("<br>");
				}

				if (!address.equals("")) {
					address = crs.getString("contact_address");
					if (!crs.getString("city_name").equals("")) {
						address = address + ", " + crs.getString("city_name");
					}
					address = address + " - " + crs.getString("contact_pin");
					if (!crs.getString("contact_landmark").equals("")) {
						address = address + "<br><b>Landmark:</b> " + crs.getString("contact_landmark");
					}
				}
				Str.append(address).append("<br>")
						.append("</div>")
						.append("</div>");

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}

	// public String PhoneDate(String date)
	// {
	// if (!date.equals("")) {
	// date = date.replace('/', '-');
	// day = date.substring(0, 2);
	// month = date.substring(3, 5);
	// year = date.substring(6);
	// date = year + "-" + month + "-" + day;
	// }
	// return date;
	// }

}
