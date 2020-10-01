package axela.sales;
//Bhagwan Singh 20th july 2013  

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.customer.Customer_Tags_Check;
import cloudify.connect.Connect;

public class SalesOrder_Dash extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String so_desc = "";
	public String StrHTML = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String so_id = "0";
	public String so_date = "";
	public String so_customer_id = "0";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String link_vehicle_name = "";
	public String quote_link = "";
	public String enquiry_link = "";
	public String sodate = "", enquiry_custtype_id = "0", enquiry_enquirytype_id = "0";
	public String so_enquiry_id = "";

	public String emp_close_enquiry = "0";
	// Accounts
	public String so_mga_amount = "0";
	public String so_mga_paid = "0";
	public String so_mga_foc_amount = "0";
	public String so_ew_amount = "0";
	public String so_ew_payout = "0";
	public String so_ew_type_id = "0";
	public String so_insur_amount = "";
	public String so_insur_gross = "0";
	public String so_insur_net = "0";
	public String so_insur_type_id = "0";
	public String so_exchange_amount = "0";
	public String so_exchange = "";

	// Offers
	public String so_offer_consumer = "";
	public String so_offer_exchange_bonus = "";
	public String so_offer_corporate = "";
	public String so_offer_spcl_scheme = "";
	public String so_offer_loyalty_bonus = "";
	public String so_offer_govtempscheme = "";
	// public String so_offer_consumer = "";

	// fin status
	public String so_fintype_id = "0";
	public String so_finstatus_id = "0";
	public String so_finstatus_date = "";
	public String so_finstatus_emp_id = "0";
	public String so_finstatus_loan_amt = "";
	public String so_finstatus_emi_value = "";
	public String so_finstatus_advance = "";
	public String so_finstatus_disbursed_date = "", so_finstatus_disburseddate = "";
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
	// vehicle so
	public String invoice_id = "0";
	public String invoice_date = "";
	public String so_reg_rc_delivery = "";
	public String so_reg_hsrp_received_date = "", hsrp_received_date = "";
	public String so_reg_hsrp_install_date = "", hsrp_install_date = "";
	public String so_reg_perm_reg_no = "";
	public String so_reg_rc_received_date = "", rc_received_date = "";
	public String so_reg_rc_handover_date = "", rc_handover_date = "";
	public String so_preownedstock_id = "0";

	// Insurance
	public String customer_display = "none";
	public String reg_display = "none";
	public String contact_id = "0";
	public String customer_id = "0";
	public String customer_name = "";
	public String insurpolicy_contact_id = "0";
	public String insurpolicy_veh_id = "0";
	public String insurpolicy_start_date = "";
	public String insur_type_id = "0";
	public String insurpolicy_branch_id = "0";
	public String insurstartdate = "";
	public String insurpolicy_end_date = "";
	public String insurenddate = "";
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
	public String insurpolicy_emp_id = "0";
	public String inscomp_id = "0";
	public String insurpolicy_field_emp_id = "0";
	public String insurpolicy_active = "0";
	public String insurpolicy_notes = "";
	public String policytype_entry_id = "0";
	public String policytype_entry_date = "";
	public String policytype_modified_id = "0";
	public String policytype_modified_date = "";

	// new 5 fields
	// public String so_rcdel_fastfox_handover_time = "", rcdel_fastfox_handover_time = "";
	public String so_rcdel_person_received = "";
	public String so_rcdel_person_contact_no = "";
	public String so_rcdel_person_relation = "";
	public String so_rcdel_delivery_time = "", rcdel_delivery_time = "";
	// public String so_reg_salesfile_received_tat = "0";
	public String so_finstatus_disbursed_tat = "";
	// public String so_reg_rto_file_tat = "0";
	// public String so_reg_temp_reg_tat = "0";
	public String so_reg_hsrp_received_tat = "0";
	public String so_reg_hsrp_install_tat = "0";
	// public String so_reg_perm_reg_tat = "0";
	public String so_reg_rc_received_tat = "0";
	public String so_reg_rc_handover_tat = "0";
	// new 5 fields
	public String so_rcdel_fastfox_handover_tat = "0";
	public String so_rcdel_delivery_tat = "0";
	// public String so_rcdel_person_received_tat = "0";

	public String so_reg_hsrp = "0";
	//
	public String so_fincomp_id = "0";
	public String so_finance_amt = "";
	public String so_finance_gross = "0";
	public String so_finance_net = "0";

	public String so_booking_amount = "";
	public String so_refund_amount = "";
	public String so_bankrefund_amount = "";
	public String so_payment_date = "", so_paymentdate = "";
	public String so_promise_date = "", so_promisedate = "";
	public String so_cancel_date = "", so_canceldate = "";
	public String so_open = "0";
	public String so_refno = "";
	public String so_active = "0";
	public String so_notes = "";
	public String cinstatus_name = "", cancelreason_name = "";
	public String so_reg_notes = "";
	public String so_entry_id = "0";
	public String so_entry_by = "";
	public String so_entry_date = "";
	public String so_modified_id = "0";
	public String so_modified_by = "";
	public String so_modified_date = "";
	public String so_retail_date = "", so_retaildate = "";
	public String so_delivered_date = "", so_delivereddate = "";
	public String so_reg_no = "0";
	public String so_reg_date = "", so_regdate = "", so_dob = "", so_pan = "";
	public String item_id = "0";
	public String variant_id = "0";
	public String so_cancelreason_id = "0";
	public String so_cinstatus_id = "0";
	public String so_vehstock_id = "0", so_vehstock_fastag = "";
	public String so_option_id = "0", so_saletype_id = "0";
	public String so_allot_no = "", emp_role_id = "0";
	public String emp_all_exe = "";
	public String veh_id = "0";
	// public String insurpolicy_id = "0";
	public String soactive = "";
	public String so_authorize_pdi = "0";
	public String so_authorize_accessories = "0";
	public String so_authorize_accounts = "0";
	public String so_authorize_insurance = "0";
	public String so_authorize_registration = "0";
	public String so_authorize_deliverycoordinator = "0";
	public DecimalFormat df = new DecimalFormat("0");

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				msg = PadQuotes(request.getParameter("msg"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));

				emp_close_enquiry = CNumeric(ExecuteQuery("SELECT emp_close_enquiry"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id + ""));

				StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess.replace("emp_id", "so_emp_id") + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
					}
					PopulateFields(request, response);
					PopulateSOFinStatusDetails();
					PopulateSORegistrationDetails();
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();
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

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT so_id, so_date, so_branch_id, so_payment_date, so_promise_date,"
					+ " so_open, so_refno, so_fincomp_id, so_finance_amt, so_finance_gross, so_finance_net,  so_cancel_date,"
					+ " so_po, so_desc, so_emp_id, so_active, so_notes, so_entry_id, so_dob, so_pan,"
					+ " so_authorize_pdi, so_authorize_accessories, so_authorize_accounts, so_authorize_insurance,"
					+ " so_authorize_registration, so_authorize_deliverycoordinator,"
					+ "	COALESCE((SELECT cancelreason_name FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " WHERE cancelreason_id = so_cancelreason_id), '' )  AS cancelreason_name,"
					+ " COALESCE(( SELECT cinstatus_name FROM " + compdb(comp_id) + "axela_sales_so_cin_status"
					+ " WHERE cinstatus_id = so_cinstatus_id ), '')  AS cinstatus_name,"
					+ " so_entry_date, so_modified_id, so_modified_date, so_active, so_cancelreason_id,"
					+ " so_cinstatus_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name,"
					+ " rateclass_id, soitem_item_id, customer_name, customer_id, contact_id,"
					+ " COALESCE(so_quote_id, 0) AS so_quote_id, so_grandtotal, so_retail_date,"
					+ " so_discamt, so_netamt, so_exprice, so_totaltax, so_vehstock_id, so_option_id, so_allot_no,"
					+ " so_delivered_date, so_reg_no, so_reg_date,"
					+ " so_enquiry_id, so_booking_amount, so_refund_amount , so_bankrefund_amount, so_saletype_id,"
					+ " so_mga_amount, so_mga_paid, so_mga_foc_amount, so_ew_amount, so_ew_payout, so_ew_type_id,"
					+ " so_insur_amount, so_insur_gross, so_insur_net, so_insur_type_id,  so_exchange, so_exchange_amount,"
					+ " so_offer_consumer, so_offer_exchange_bonus, so_offer_spcl_scheme, so_offer_corporate,"
					+ " so_offer_loyalty_bonus, so_offer_govtempscheme, so_customer_id, so_reg_hsrp,"
					+ "	so_preownedstock_id, COALESCE ( ( SELECT variant_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE preownedstock_id = so_preownedstock_id ), '' ) AS variant_id,"
					+ " so_reg_notes, so_rcdel_person_received, so_rcdel_person_contact_no,"
					+ " so_rcdel_person_relation, so_rcdel_delivery_time, enquiry_enquirytype_id,"
					+ " COALESCE (veh_id, 0) AS veh_id,"
					// + " COALESCE (insurpolicy_id, 0) AS insurpolicy_id,"
					+ " COALESCE (vehstock_fastag,'') AS vehstock_fastag"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_so_id = so_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_insurance_policy ON insurpolicy_so_id = so_id"
					+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess.replace("emp_id", "so_emp_id");
			// if (emp_id.equals("1")) {
			// SOPInfo("StrSql===PopulateFields==" + StrSqlBreaker(StrSql));
			// }
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					so_id = crs.getString("so_id");
					so_date = crs.getString("so_date");
					sodate = strToShortDate(so_date);
					so_customer_id = crs.getString("so_customer_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("contact_name") + "</a>";
					enquiry_link = "<a href=\"../sales/enquiry-list.jsp?enquiry_id=" + crs.getString("so_enquiry_id") + "\">"
							+ crs.getString("so_enquiry_id") + "</a>";
					quote_link = "<a href=\"../sales/veh-quote-list.jsp?quote_id=" + crs.getString("so_quote_id") + "\">"
							+ crs.getString("so_quote_id") + "</a>";
					so_enquiry_id = crs.getString("so_enquiry_id");
					so_payment_date = crs.getString("so_payment_date");
					so_paymentdate = strToShortDate(so_payment_date);
					so_promise_date = crs.getString("so_promise_date");
					so_promisedate = strToShortDate(so_promise_date);
					so_retail_date = crs.getString("so_retail_date");
					so_retaildate = strToLongDate(so_retail_date);
					so_delivered_date = crs.getString("so_delivered_date");
					so_delivereddate = strToLongDate(so_delivered_date);
					so_reg_no = crs.getString("so_reg_no");
					so_reg_date = crs.getString("so_reg_date");
					so_regdate = strToShortDate(so_reg_date);
					so_dob = strToShortDate(crs.getString("so_dob"));
					so_pan = crs.getString("so_pan");
					so_cancelreason_id = crs.getString("so_cancelreason_id");
					so_cinstatus_id = crs.getString("so_cinstatus_id");
					so_vehstock_id = crs.getString("so_vehstock_id");
					so_vehstock_fastag = crs.getString("vehstock_fastag");
					so_preownedstock_id = crs.getString("so_preownedstock_id");
					so_option_id = crs.getString("so_option_id");
					so_allot_no = crs.getString("so_allot_no");

					if (enquiry_enquirytype_id.equals("1")) {
						item_id = crs.getString("soitem_item_id");
					} else if (enquiry_enquirytype_id.equals("2")) {
						item_id = crs.getString("variant_id");
					}

					so_open = crs.getString("so_open");
					so_refno = crs.getString("so_refno");
					so_active = crs.getString("so_active");
					soactive = so_active.equals("1") ? "Active" : "In-Active";

					so_fincomp_id = crs.getString("so_fincomp_id");
					so_finance_amt = df.format(crs.getDouble("so_finance_amt"));
					so_finance_gross = df.format(crs.getDouble("so_finance_gross"));
					so_finance_net = df.format(crs.getDouble("so_finance_net"));
					so_booking_amount = crs.getString("so_booking_amount");
					so_refund_amount = crs.getString("so_refund_amount");
					so_bankrefund_amount = crs.getString("so_bankrefund_amount");
					// accounts
					so_mga_amount = crs.getString("so_mga_amount");
					so_mga_paid = df.format(crs.getDouble("so_mga_paid"));
					so_mga_foc_amount = df.format(crs.getDouble("so_mga_foc_amount"));
					so_ew_amount = crs.getString("so_ew_amount");
					so_ew_payout = df.format(crs.getDouble("so_ew_payout"));
					so_ew_type_id = crs.getString("so_ew_type_id");
					so_insur_amount = crs.getString("so_insur_amount");
					so_insur_gross = df.format(crs.getDouble("so_insur_gross"));
					so_insur_net = df.format(crs.getDouble("so_insur_net"));
					so_insur_type_id = crs.getString("so_insur_type_id");
					so_exchange = crs.getString("so_exchange");
					so_exchange_amount = crs.getString("so_exchange_amount");
					so_offer_consumer = df.format(crs.getDouble("so_offer_consumer"));
					so_offer_exchange_bonus = df.format(crs.getDouble("so_offer_exchange_bonus"));
					so_offer_spcl_scheme = df.format(crs.getDouble("so_offer_spcl_scheme"));
					so_offer_loyalty_bonus = df.format(crs.getDouble("so_offer_loyalty_bonus"));
					so_offer_govtempscheme = df.format(crs.getDouble("so_offer_govtempscheme"));
					so_offer_corporate = df.format(crs.getDouble("so_offer_corporate"));
					so_saletype_id = crs.getString("so_saletype_id");
					so_desc = crs.getString("so_desc");
					so_cancel_date = crs.getString("so_cancel_date");
					so_canceldate = strToShortDate(so_cancel_date);
					so_notes = crs.getString("so_notes");
					cancelreason_name = crs.getString("cancelreason_name");
					cinstatus_name = crs.getString("cinstatus_name");
					so_reg_notes = crs.getString("so_reg_notes");
					so_reg_hsrp = crs.getString("so_reg_hsrp");

					// authorize
					so_authorize_pdi = crs.getString("so_authorize_pdi");
					so_authorize_accessories = crs.getString("so_authorize_accessories");
					so_authorize_accounts = crs.getString("so_authorize_accounts");
					so_authorize_insurance = crs.getString("so_authorize_insurance");
					so_authorize_registration = crs.getString("so_authorize_registration");
					so_authorize_deliverycoordinator = crs.getString("so_authorize_deliverycoordinator");
					// so_rcdel_fastfox_handover_time = crs.getString("so_rcdel_fastfox_handover_time");
					// rcdel_fastfox_handover_time = strToShortDate(so_rcdel_fastfox_handover_time);
					so_rcdel_person_received = crs.getString("so_rcdel_person_received");
					so_rcdel_person_contact_no = crs.getString("so_rcdel_person_contact_no");
					so_rcdel_person_relation = crs.getString("so_rcdel_person_relation");

					so_rcdel_delivery_time = crs.getString("so_rcdel_delivery_time");
					rcdel_delivery_time = strToShortDate(so_rcdel_delivery_time);
					veh_id = crs.getString("veh_id");
					// insurpolicy_id = crs.getString("insurpolicy_id");

					so_entry_id = crs.getString("so_entry_id");
					if (!so_entry_id.equals("")) {
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
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public String PopulateCancelReason(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"0\">Select</option>\n");
	// try {
	// StrSql = "SELECT cancelreason_id, cancelreason_name"
	// + " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
	// + " ORDER BY cancelreason_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("cancelreason_id"));
	// Str.append(StrSelectdrop(crs.getString("cancelreason_id"), so_cancelreason_id));
	// Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }

	public String PopulateCancelReason(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_cancelreason_id = cancelreason_id"
					+ "	WHERE so_id = " + so_id;
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append(crs.getString("cancelreason_name"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateCINStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT cinstatus_id, cinstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cin_status"
					+ " ORDER BY cinstatus_rank ASC";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cinstatus_id"));
				Str.append(StrSelectdrop(crs.getString("cinstatus_id"), so_cinstatus_id));
				Str.append(">").append(crs.getString("cinstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id"));
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), so_fincomp_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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

	public String PopulateTypeOfSale(String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT saletype_id, saletype_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_so_saletype"
				+ " WHERE 1 = 1"
				+ " GROUP BY saletype_id"
				+ " ORDER BY saletype_id";
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value='0'>Select</option>");
		try {
			while (crs.next()) {
				Str.append("<option value='" + crs.getString("saletype_id") + "' ");
				Str.append(Selectdrop(crs.getInt("saletype_id"), so_saletype_id)).append(">");
				Str.append(crs.getString("saletype_name")).append("</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTypeOfInsur(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(so_insur_type_id, "1") + ">FOC</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(so_insur_type_id, "2") + ">PAID</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTypeOfExtWarranty(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(so_ew_type_id, "1") + ">FOC</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(so_ew_type_id, "2") + ">PAID</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public void PopulateSOFinStatusDetails() {
		try {
			StrSql = "SELECT so_date, customer_id, customer_name, contact_id, so_enquiry_id, so_quote_id,"
					+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name,"
					+ " enquiry_enquirytype_id, so_fintype_id, so_finstatus_id, so_desc, so_finstatus_date,"
					+ " so_finstatus_emp_id, so_finstatus_desc, so_finstatus_loan_amt, so_finstatus_emi_value,"
					+ " so_finstatus_advance, so_finstatus_disbursed_date, so_finstatus_disbursed_amt, so_finstatus_gross_payout,"
					+ " so_finstatus_sex, so_finstatus_income_on_payment, so_finstatus_bank_rack_rate,"
					+ " so_finstatus_tenure, so_finstatus_bank_id, so_finstatus_scheme, so_finstatus_process_fee,"
					+ " so_finstatus_subvention, so_finstatus_customer_roi, so_finstatus_occupation,"
					+ " so_finstatus_industry, so_finstatus_income_asperdoc, so_finstatus_dob, so_promise_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess.replace("emp_id", "so_emp_id") + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("PopulateSOFinStatusDetails------" + StrSql);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_promisedate = strToShortDate(crs.getString("so_promise_date"));
					sodate = strToShortDate(crs.getString("so_date"));
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contact_name") + "</a>";
					enquiry_link = "<a href=\"../sales/enquiry-list.jsp?enquiry_id=" + crs.getString("so_enquiry_id") + "\">" + crs.getString("so_enquiry_id") + "</a>";
					quote_link = "<a href=\"../sales/veh-quote-list.jsp?quote_id=" + crs.getString("so_quote_id") + "\">" + crs.getString("so_quote_id") + "</a>";
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					so_desc = crs.getString("so_desc");
					so_fintype_id = crs.getString("so_fintype_id");
					so_finstatus_id = crs.getString("so_finstatus_id");
					so_finstatus_date = crs.getString("so_finstatus_date");
					so_finstatus_emp_id = crs.getString("so_finstatus_emp_id");
					so_finstatus_desc = crs.getString("so_finstatus_desc");
					so_finstatus_loan_amt = crs.getString("so_finstatus_loan_amt");
					so_finstatus_emi_value = crs.getString("so_finstatus_emi_value");
					so_finstatus_advance = crs.getString("so_finstatus_advance");
					so_finstatus_disburseddate = strToShortDate(crs.getString("so_finstatus_disbursed_date"));
					so_finstatus_disbursed_amt = crs.getString("so_finstatus_disbursed_amt");
					so_finstatus_gross_payout = crs.getString("so_finstatus_gross_payout");
					so_finstatus_sex = crs.getString("so_finstatus_sex");
					so_finstatus_income_on_payment = crs.getString("so_finstatus_income_on_payment");
					so_finstatus_bank_rack_rate = crs.getString("so_finstatus_bank_rack_rate");
					so_finstatus_customer_roi = crs.getString("so_finstatus_customer_roi");
					so_finstatus_occupation = crs.getString("so_finstatus_occupation");
					so_finstatus_industry = crs.getString("so_finstatus_industry");
					so_finstatus_income_asperdoc = crs.getString("so_finstatus_income_asperdoc");
					so_finstatus_dob = crs.getString("so_finstatus_dob");
					if (!so_finstatus_dob.equals("")) {
						so_finstatus_dob = strToShortDate(so_finstatus_dob);
					}
					so_finstatus_tenure = crs.getString("so_finstatus_tenure");
					so_finstatus_bank_id = crs.getString("so_finstatus_bank_id");
					so_finstatus_scheme = crs.getString("so_finstatus_scheme");
					so_finstatus_process_fee = crs.getString("so_finstatus_process_fee");
					so_finstatus_subvention = crs.getString("so_finstatus_subvention");
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateSORegistrationDetails() {
		try {
			StrSql = "SELECT so_date, customer_id, customer_name, contact_id, "
					+ " CONCAT(title_desc, ' ', contact_fname , ' ' , contact_lname) AS contact_name,"
					+ " so_enquiry_id, so_quote_id, enquiry_custtype_id,"
					+ " so_desc, so_reg_rc_delivery,"
					+ " so_reg_hsrp_received_date, so_reg_hsrp_install_date, "
					+ " so_reg_perm_reg_no, so_reg_rc_received_date, so_reg_rc_handover_date,"
					// + " COALESCE(invoice_id, '') AS invoice_id, COALESCE(invoice_date, '') AS invoice_date,"
					+ " IF(so_delivered_date != '', IF(so_reg_hsrp_received_date != '',"
					+ " COALESCE(TIMESTAMPDIFF(DAY, CONCAT(SUBSTR(so_delivered_date, 1, 4), '-',"
					+ " SUBSTR(so_delivered_date,5,2), '-', SUBSTR(so_delivered_date, 7, 2)),"
					+ " CONCAT(SUBSTR(so_reg_hsrp_received_date, 1, 4), '-',"
					+ " SUBSTR(so_reg_hsrp_received_date, 5, 2), '-',"
					+ " SUBSTR(so_reg_hsrp_received_date, 7, 2))), ''), ''), 0) AS 'HSRPReceivedTAT',"
					+ " IF(so_delivered_date != '', IF(so_reg_hsrp_install_date != '',"
					+ " COALESCE(TIMESTAMPDIFF(DAY, CONCAT(SUBSTR(so_delivered_date, 1, 4), '-',"
					+ " SUBSTR(so_delivered_date, 5, 2), '-', SUBSTR(so_delivered_date, 7, 2)),"
					+ " CONCAT(SUBSTR(so_reg_hsrp_install_date, 1, 4), '-',"
					+ " SUBSTR(so_reg_hsrp_install_date, 5, 2), '-',"
					+ " SUBSTR(so_reg_hsrp_install_date, 7, 2))), ''), ''), 0) AS 'HSRPInstallationTAT',"
					+ " IF(so_delivered_date != '', IF(so_reg_rc_received_date != '',"
					+ " COALESCE(TIMESTAMPDIFF(DAY, CONCAT(SUBSTR(so_delivered_date, 1, 4), '-',"
					+ " SUBSTR(so_delivered_date, 5, 2), '-', SUBSTR(so_delivered_date, 7, 2)),"
					+ " CONCAT(SUBSTR(so_reg_rc_received_date,1,4), '-',"
					+ " SUBSTR(so_reg_rc_received_date, 5, 2), '-',"
					+ " SUBSTR(so_reg_rc_received_date, 7, 2))), ''), ''), 0) AS 'RCReceivedTAT',"
					+ " IF(so_delivered_date != '', IF(so_reg_rc_handover_date != '',"
					+ " COALESCE(TIMESTAMPDIFF(DAY, CONCAT(SUBSTR(so_delivered_date, 1, 4), '-',"
					+ " SUBSTR(so_delivered_date, 5, 2), '-', SUBSTR(so_delivered_date, 7, 2)),"
					+ " CONCAT(SUBSTR(so_reg_rc_handover_date, 1, 4), '-',"
					+ " SUBSTR(so_reg_rc_handover_date, 5, 2), '-',"
					+ " SUBSTR(so_reg_rc_handover_date, 7, 2))), ''), ''), 0) AS 'RCHandoverTAT'"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_invoice ON invoice_so_id = so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " WHERE so_id = " + so_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "so_emp_id") + "";
			// SOP("StrSql==rop=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_date = crs.getString("so_date");
					sodate = strToShortDate(so_date);
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contact_name") + "</a>";
					enquiry_link = "<a href=\"../sales/enquiry-list.jsp?enquiry_id=" + crs.getString("so_enquiry_id") + "\">" + crs.getString("so_enquiry_id") + "</a>";
					quote_link = "<a href=\"../sales/veh-quote-list.jsp?quote_id=" + crs.getString("so_quote_id") + "\">" + crs.getString("so_quote_id") + "</a>";
					enquiry_custtype_id = crs.getString("enquiry_custtype_id");
					// invoice_id = "<a href=\"../invoice/invoice-list.jsp?invoice_id=" + crs.getString("invoice_id") + "\">" + crs.getString("invoice_id") + "</a>";
					// invoice_date = strToShortDate(crs.getString("invoice_date"));
					so_desc = crs.getString("so_desc");
					so_reg_rc_delivery = crs.getString("so_reg_rc_delivery");

					so_reg_hsrp_received_date = crs.getString("so_reg_hsrp_received_date");
					hsrp_received_date = strToShortDate(so_reg_hsrp_received_date);
					so_reg_hsrp_received_tat = crs.getString("HSRPReceivedTAT");

					so_reg_hsrp_install_date = crs.getString("so_reg_hsrp_install_date");
					hsrp_install_date = strToShortDate(so_reg_hsrp_install_date);
					so_reg_hsrp_install_tat = crs.getString("HSRPInstallationTAT");

					so_reg_perm_reg_no = crs.getString("so_reg_perm_reg_no");
					so_reg_perm_reg_no = SplitRegNo(so_reg_perm_reg_no, 2);

					so_reg_rc_received_date = crs.getString("so_reg_rc_received_date");
					rc_received_date = strToShortDate(so_reg_rc_received_date);
					so_reg_rc_received_tat = crs.getString("RCReceivedTAT");

					so_reg_rc_handover_date = crs.getString("so_reg_rc_handover_date");
					rc_handover_date = strToShortDate(so_reg_rc_handover_date);
					so_reg_rc_handover_tat = crs.getString("RCHandoverTAT");
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFinanceType(String comp_id) {
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
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT finstatus_id, finstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_status"
					+ " WHERE finstatus_active = 1"
					+ " GROUP BY finstatus_id"
					+ " ORDER BY finstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("finstatus_id"));
				Str.append(StrSelectdrop(crs.getString("finstatus_id"), so_finstatus_id));
				Str.append(">").append(crs.getString("finstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateFitmentPlace() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=0>Select</option>\n");
	// Str.append("<option value=1").append(StrSelectdrop("1", so_reg_hsrp_fitment_place)).append(">Showroom</option>\n");
	// Str.append("<option value=2").append(StrSelectdrop("2", so_reg_hsrp_fitment_place)).append(">Customer Place</option>\n");
	// return Str.toString();
	// }
	public String PopulateRcDelivary() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", so_reg_rc_delivery)).append(">Home</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", so_reg_rc_delivery)).append(">Showroom</option>\n");
		return Str.toString();
	}
	public String PopulateSOBank(String comp_id) {
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
				Str.append(StrSelectdrop(crs.getString("bank_id"), so_finstatus_bank_id));
				Str.append(">").append(crs.getString("bank_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateGender() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		Str.append("<option value=\"1\"").append(StrSelectdrop("1", so_finstatus_sex)).append(">Male</option>\n");
		Str.append("<option value=\"2\"").append(StrSelectdrop("2", so_finstatus_sex)).append(">Female</option>\n");
		return Str.toString();
	}
	public String PopulateAdvance() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		Str.append("<option value=\"1\"").append(StrSelectdrop("1", so_finstatus_advance)).append(">Advance</option>\n");
		Str.append("<option value=\"2\"").append(StrSelectdrop("2", so_finstatus_advance)).append(">Arrears</option>\n");
		return Str.toString();
	}

}
