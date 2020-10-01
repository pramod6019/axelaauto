package axela.insurance;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Import1 extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public int insurcount = 0;
	public String comp_id = "0";
	public String branch_id = "0";
	public String insurpolicy_id = "0";
	public String followup_time = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = "1";
				AddData(request, response);
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

	public void AddData(HttpServletRequest request, HttpServletResponse response) {
		try {

			String contact_id = "0";
			String customer_id = "0";
			String contact_title_id = "0";
			String contact_fname = "";
			String contact_lname = "";
			String customer_name = "";
			String contact_name = "";
			String contact_mobile1 = "", contact_mobile2 = "";
			String contact_phone1 = "", contact_phone2 = "", contact_phone3 = "";
			String contact_email = "", contact_email1 = "", contact_email2 = "";
			String contact_city_id = "0", contact_pin = "";
			String contact_address = "", contact_address1 = "", contact_address2 = "", contact_address3 = "";

			String veh_renewal_date = "";
			String veh_id = "0";
			String item_id = "0";
			String veh_modelyear = "";
			String veh_reg_no = "";
			String veh_chassis_no = "";
			String veh_engine_no = "";
			String veh_sale_date = "";
			String veh_sale_amount = "";
			String veh_insursource_id = "0";
			String veh_kms = "0";
			String veh_service_duekms = "0";
			String veh_service_duedate = "";
			String veh_warranty_expirydate = "";
			String veh_iacs = "";
			String veh_notes = "";

			String insurpolicy_branch_id = "0";
			String insurpolicy_contact_id = "0";
			String insurpolicy_veh_id = "0";
			String insurpolicy_date = "";
			String insurpolicy_start_date = "";
			String insurpolicy_end_date = "";
			String insurfollowup_followup_time = "";
			String insur_type_id = "0";
			String insurpolicy_inscomp_id = "0";
			String insurpolicy_policytype_id = "0";
			String insurpolicy_policy_no = "";
			String insurpolicy_covernote_no = "";
			String insurpolicy_premium_amt = "";
			String insurpolicy_paymode_id = "0";
			String insurpolicy_cheque_no = "";
			String insurpolicy_cheque_date = "";
			String insurpolicy_cheque_bank_id = "0";
			String insurpolicy_idv_amt = "";
			String insurpolicy_od_amt = "";
			String insurpolicy_od_discount = "";
			String insurpolicy_payout = "";
			String insurpolicy_desc = "";
			String insurpolicy_terms = "";
			String insurpolicy_emp_id = "0";
			String insurpolicy_field_emp_id = "0";
			String insurpolicy_active = "";
			String insurpolicy_notes = "";
			String insurpolicy_entry_id = "0";
			String insurpolicy_entry_date = "";

			insurpolicy_id = "0";

			StrSql = "SELECT slno,"
					// + " COALESCE(Module, '') AS SOB,"
					+ " COALESCE(RefNo, '') AS RefNo,"
					+ " COALESCE(Salutation, '') AS Salutation,"
					+ " COALESCE(FName, '') AS FName,"
					+ " COALESCE(LName, '') AS LName,"
					+ " COALESCE(Address1, '') AS Address1,"
					+ " COALESCE(Address2, '') AS Address2,"
					+ " COALESCE(Address3, '') AS Address3,"
					+ " COALESCE(Country, '') AS Country,"
					+ " COALESCE(State, '') AS State,"
					+ " COALESCE(City, '') AS City,"
					+ " COALESCE(Block, '') AS Block,"
					+ " COALESCE(Pincode, '') AS Pincode,"
					+ " COALESCE(Phone1, '') AS Phone1,"
					+ " COALESCE(Phone2, '') AS Phone2,"
					+ " COALESCE(Mobile, '') AS Mobile,"
					+ " COALESCE(AltPhone, '') AS AltPhone,"
					+ " COALESCE(AltMobile, '') AS AltMobile,"
					+ " COALESCE(Email, '') AS Email,"
					+ " COALESCE(ContactSource, '') AS ContactSource,"
					+ " COALESCE(OpportunityDate, '') AS OpportunityDate,"
					+ " COALESCE(OpportunitySource, '') AS OpportunitySource,"
					// + " COALESCE(ProductMake, '') AS ProductMake,"
					+ " COALESCE(Product, '') AS Product,"
					+ " COALESCE(MfgYear, '') AS MfgYear,"
					+ " COALESCE(SalesExecutive, '') AS SalesExecutive,"
					+ " COALESCE(Color, '') AS Color,"
					+ " COALESCE(ChassisNo, '') AS ChassisNo,"
					+ " COALESCE(EngineNo, '') AS EngineNo,"
					+ " COALESCE(RegistrationNo, '') AS RegistrationNo,"
					+ " COALESCE(Dealer, '') AS Dealer,"
					+ " COALESCE(SalesDate, '') AS SalesDate,"
					+ " COALESCE(SalesAmount, '') AS SalesAmount,"
					+ " COALESCE(Outlet, '') AS Outlet,"
					+ " COALESCE(Financier, '') AS Financier,"
					+ " COALESCE(AssignedTo, '') AS AssignedTo,"
					+ " COALESCE(AssignedDate, '') AS AssignedDate,"
					+ " COALESCE(CurrentStatus, '') AS CurrentStatus,"
					+ " COALESCE(Renewaldate, '') AS Renewaldate,"
					+ " COALESCE(RenewalStatus, '') AS RenewalStatus,"
					+ " COALESCE(PolicyNo, '') AS PolicyNo,"
					+ " COALESCE(PolicyIssueDate, '') AS PolicyIssueDate,"
					+ " COALESCE(InsuranceCompany, '') AS InsuranceCompany,"
					+ " COALESCE(InsuranceType, '') AS InsuranceType,"
					+ " COALESCE(PolicyExpirydate, '') AS PolicyExpirydate,"
					+ " COALESCE(Premium, '') AS Premium,"
					+ " COALESCE(CurrentRenewalStatus, '') AS CurrentRenewalStatus,"
					+ " COALESCE(FreshOrRenewal, '') AS FreshOrRenewal,"
					+ " COALESCE(PolicyDoneBy, '') AS PolicyDoneBy,"
					+ " COALESCE(PolicyDoneAt, '') AS PolicyDoneAt,"
					+ " COALESCE(DealerExecutive, '') AS DealerExecutive,"
					+ " COALESCE(LastAction, '') AS LastAction,"
					+ " COALESCE(ActionDate, '') AS ActionDate,"
					+ " COALESCE(ActionExecutive, '') AS ActionExecutive,"
					+ " COALESCE(Remark, '') AS Remark,"
					+ " COALESCE(Feedback, '') AS Feedback,"
					+ " COALESCE(FirstAction, '') AS FirstAction,"
					+ " COALESCE(FirstActionDate, '') AS FirstActionDate,"
					+ " COALESCE(Ageing, '') AS Ageing,"
					+ " COALESCE(F60, '') AS F60"
					+ " FROM insur"
					+ " WHERE Active = 0"
					+ " LIMIT 15";
			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("StrSql = " + StrSql);
			if (crs.isBeforeFirst()) {
				StringBuilder StrNotes = new StringBuilder();
				while (crs.next()) {
					// Branch-------------------------------
					StrSql = "SELECT branch_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_name LIKE '" + crs.getString("Outlet") + "'"
							+ " LIMIT 1";
					branch_id = CNumeric(ExecuteQuery(StrSql));
					if (branch_id.equals("0")) {
						branch_id = "1";
					}

					// Mobile-------------------------------
					contact_mobile1 = crs.getString("Mobile");
					if (contact_mobile1.contains(".")) {
						contact_mobile1 = contact_mobile1.replace(".", "");
					}
					if (contact_mobile1.length() >= 10) {
						contact_mobile1 = contact_mobile1.substring(0, 10);
					}
					contact_mobile2 = crs.getString("AltMobile");
					if (contact_mobile2.contains(".")) {
						contact_mobile2 = contact_mobile2.replace(".", "");
					}
					if (contact_mobile2.length() >= 10) {
						contact_mobile2 = contact_mobile2.substring(0, 10);
					}

					// CustomerName-------------------------------
					customer_name = crs.getString("FName") + crs.getString("LName");

					// Contact Name-------------------------------
					StrSql = "SELECT title_id"
							+ " FROM " + compdb(comp_id) + "axela_title"
							+ " WHERE title_desc = '" + crs.getString("Salutation") + ".'";
					contact_title_id = CNumeric(ExecuteQuery(StrSql));
					if (contact_title_id.equals("0")) {
						contact_title_id = "1";
					}
					contact_fname = crs.getString("FName");
					contact_lname = crs.getString("LName");

					// Address-------------------------------
					contact_address1 = crs.getString("Address1");
					contact_address2 = crs.getString("Address2");
					contact_address3 = crs.getString("Address3");
					contact_address = crs.getString("Address1");
					if (!contact_address2.equals("")) {
						contact_address += "\n" + contact_address2;
					}
					if (!contact_address3.equals("")) {
						contact_address += "\n" + contact_address3;
					}

					// City-------------------------------
					StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
							+ " WHERE city_name = '" + crs.getString("City") + "'"
							+ " LIMIT 1";
					contact_city_id = CNumeric(ExecuteQuery(StrSql));
					if (contact_city_id.equals("0")) {
						contact_city_id = "1";
					}

					// Pin-------------------------------
					contact_pin = crs.getString("Pincode");

					// Phone------------------------------
					contact_phone1 = crs.getString("Phone1");
					if (contact_phone1.contains(".")) {
						contact_phone1 = contact_phone1.replace(".", "");
					}
					if (contact_phone1.length() >= 12) {
						contact_phone1 = contact_phone1.substring(0, 12);
					}
					contact_phone2 = crs.getString("Phone2");
					if (contact_phone2.contains(".")) {
						contact_phone2 = contact_phone2.replace(".", "");
					}
					if (contact_phone2.length() >= 12) {
						contact_phone2 = contact_phone2.substring(0, 12);
					}
					contact_phone3 = crs.getString("AltPhone");
					if (contact_phone3.contains(".")) {
						contact_phone3 = contact_phone3.replace(".", "");
					}
					if (contact_phone3.length() >= 12) {
						contact_phone3 = contact_phone3.substring(0, 12);
					}

					// Email-------------------------------
					contact_email = crs.getString("Email");

					if (!contact_email.equals("")) {
						if (contact_email.contains(",")) {
							contact_email1 = contact_email.split(",")[0];
							if (contact_email.split(",").length > 1) {
								contact_email2 = contact_email.split(",")[1];
							}
						} else {
							contact_email1 = contact_email;
						}
					}

					// Item-----------------------------------------------
					StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE 1 =1"
							+ " AND item_name LIKE '" + crs.getString("Product") + "%'"
							+ " LIMIT 1";
					item_id = CNumeric(ExecuteQuery(StrSql));

					if (item_id.equals("0")) {
						item_id = "1";
					}

					StrSql = "SELECT insursource_id FROM " + compdb(comp_id) + "axela_insurance_source"
							+ " WHERE 1 =1"
							+ " AND insursource_name LIKE '" + crs.getString("ContactSource") + "%'"
							+ " LIMIT 1";
					veh_id = "0";
					customer_id = "0";
					contact_id = "0";
					veh_insursource_id = CNumeric(ExecuteQuery(StrSql));
					veh_renewal_date = crs.getString("Renewaldate");
					veh_reg_no = crs.getString("RegistrationNo");
					veh_chassis_no = crs.getString("ChassisNo");
					veh_engine_no = crs.getString("EngineNo");

					// Insurance Details
					insurpolicy_date = crs.getString("PolicyIssueDate");
					insurpolicy_start_date = crs.getString("PolicyIssueDate");
					insurpolicy_end_date = crs.getString("PolicyExpirydate");
					StrSql = "SELECT insurtype_id FROM " + compdb(comp_id) + "axela_insurance_type"
							+ " WHERE 1 =1"
							+ " AND insurtype_name LIKE '" + crs.getString("FreshOrRenewal") + "%'"
							+ " LIMIT 1";
					insur_type_id = CNumeric(ExecuteQuery(StrSql));
					StrSql = "SELECT inscomp_id FROM " + compdb(comp_id) + "axela_insurance_comp"
							+ " WHERE 1 =1"
							+ " AND inscomp_name LIKE '" + crs.getString("InsuranceCompany") + "%'"
							+ " LIMIT 1";
					insurpolicy_inscomp_id = CNumeric(ExecuteQuery(StrSql));
					insurpolicy_policytype_id = "0";
					insurpolicy_policy_no = crs.getString("PolicyNo");
					insurpolicy_covernote_no = "";
					insurpolicy_premium_amt = crs.getString("Premium");
					if (insurpolicy_premium_amt.equals("")) {
						insurpolicy_premium_amt = "0";
					}
					insurpolicy_paymode_id = "0";
					insurpolicy_cheque_no = "0";
					insurpolicy_cheque_date = "";
					insurpolicy_cheque_bank_id = "0";
					insurpolicy_idv_amt = "0";
					insurpolicy_od_amt = "0";
					insurpolicy_od_discount = "0";
					insurpolicy_payout = "0";
					// / insurpolicy_desc = crs.getString("PolicyNo");
					// insurpolicy_terms = crs.getString("PolicyNo");
					// insurpolicy_emp_id = crs.getString("SalesExecutive");
					insurpolicy_emp_id = ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE 1 =1"
							+ " AND emp_name LIKE '%" + crs.getString("SalesExecutive") + "'"
							+ " LIMIT 1");
					if (CNumeric(insurpolicy_emp_id).equals("0")) {
						insurpolicy_emp_id = "1";
					}
					SOP("insurpolicy_emp_id ================= " + insurpolicy_emp_id);
					// insurpolicy_field_emp_id = crs.getString("PolicyNo");
					// / insurpolicy_notes = crs.getString("PolicyNo");
					SOP("insurpolicy_date = " + insurpolicy_date);
					// insurfollowup_followup_time =
					// ConvertShortDateToStr(AddMonth(insurpolicy_date, -3));
					// SOP("insurfollowup_followup_time = " +
					// insurfollowup_followup_time);
					// insurfollowup_followup_time = concat(substr('" +
					// insurfollowup_followup_time + "', 1, 8), substr('" +
					// enquiry_entry_date + "', 9, 14));

					// SOP("RegistrationNo = " +
					// crs.getString("RegistrationNo"));
					// SOP("ChassisNo = " + crs.getString("ChassisNo"));
					// SOP("EngineNo = " + crs.getString("EngineNo"));
					// if (!veh_reg_no.equals("")) {
					// StrSql = "SELECT veh_id, veh_customer_id, veh_contact_id"
					// + " FROM " + compdb(comp_id) + "axela_service_veh"
					// + " WHERE veh_reg_no = " +
					// crs.getString("RegistrationNo") + "";
					// ResultSet rsreg = processQuery(StrSql, 0);
					// while (crs.next()) {
					// veh_id = rsreg.getString("veh_id");
					// customer_id = rsreg.getString("veh_customer_id");
					// contact_id = rsreg.getString("veh_contact_id");
					// }
					// rsreg.close();
					// } else {
					// checking if vehicle already exist
					StrSql = "SELECT COALESCE(veh_id,0) veh_id, COALESCE(veh_customer_id,0) veh_customer_id,"
							+ " COALESCE(veh_contact_id,0) veh_contact_id"
							+ " FROM " + compdb(comp_id) + "axela_service_veh"
							+ " WHERE (veh_chassis_no = '" + veh_chassis_no + "' OR veh_engine_no = '" + veh_engine_no + "'";
					if (!crs.getString("RegistrationNo").equals("")) {
						StrSql += " OR veh_reg_no = '" + crs.getString("RegistrationNo") + "'";
					}
					StrSql += ")";
					// SOP("StrSql =veh== " + StrSqlBreaker(StrSql));
					ResultSet rsveh = processQuery(StrSql, 0);
					while (rsveh.next()) {
						veh_id = rsveh.getInt("veh_id") + "";
						customer_id = rsveh.getInt("veh_customer_id") + "";
						contact_id = rsveh.getInt("veh_contact_id") + "";
					}
					rsveh.close();
					// }

					SOP("veh_id  here = " + veh_id);

					if (veh_id.equals("0")) {
						// ------------------------------------------------------
						if (!contact_mobile1.equals("")) {
							StrSql = "SELECT contact_id, contact_customer_id"
									+ " FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " WHERE contact_mobile1 = '" + contact_mobile1 + "'"
									+ " OR contact_mobile2 = '" + contact_mobile1 + "'";
							CachedRowSet crs1 = processQuery(StrSql, 0);
							// SOP("StrSql mob====== " + StrSql);
							while (crs1.next()) {
								contact_id = crs1.getString("contact_id");
								customer_id = crs1.getString("contact_customer_id");
							}
							crs1.close();
						}

						if (contact_id.equals("0") && !contact_fname.equals("")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
									+ " (customer_branch_id,"
									+ " customer_name,"
									+ " customer_mobile1,"
									+ " customer_mobile2,"
									+ " customer_phone1,"
									+ " customer_phone2,"
									+ " customer_phone3,"
									+ " customer_email1,"
									+ " customer_email2,"
									+ " customer_address,"
									+ " customer_city_id,"
									+ " customer_since,"
									+ " customer_active,"
									+ " customer_notes,"
									+ " customer_entry_id,"
									+ " customer_entry_date)"
									+ " VALUES"
									+ " (" + branch_id + ","
									+ " '" + customer_name + "',"
									+ " '" + contact_mobile1 + "',"
									+ " '" + contact_mobile2 + "',"
									+ " '" + contact_phone1 + "',"
									+ " '" + contact_phone2 + "',"
									+ " '" + contact_phone3 + "',"
									+ " '" + contact_email1 + "',"
									+ " '" + contact_email2 + "',"
									+ " '" + contact_address1 + "',"
									+ " " + CNumeric(contact_city_id) + ","
									+ " '" + ToShortDate(kknow()) + "',"
									+ " 1,"
									+ " '',"
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "')";
							// SOP("customer StrSql = " + StrSql);
							customer_id = UpdateQueryReturnID(StrSql);

							if (!customer_id.equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
										+ " (contact_customer_id,"
										+ " contact_contacttype_id,"
										+ " contact_title_id,"
										+ " contact_fname,"
										+ " contact_lname,"
										+ " contact_mobile1,"
										+ " contact_mobile2,"
										+ " contact_phone1,"
										+ " contact_phone2,"
										+ " contact_email1,"
										+ " contact_email2,"
										+ " contact_address,"
										+ " contact_pin,"
										+ " contact_city_id,"
										+ " contact_active,"
										+ " contact_notes,"
										+ " contact_entry_id,"
										+ " contact_entry_date)"
										+ " VALUES"
										+ " (" + customer_id + ","
										+ " 1,"
										+ " " + CNumeric(contact_title_id) + ","
										+ " '" + contact_fname + "',"
										+ " '" + contact_lname + "',"
										+ " '" + contact_mobile1 + "',"
										+ " '" + contact_mobile2 + "',"
										+ " '" + contact_phone1 + "',"
										+ " '" + contact_phone2 + "',"
										+ " '" + contact_email1 + "',"
										+ " '" + contact_email2 + "',"
										+ " '" + contact_address1.replace("'", "&#39;") + "',"
										+ " '" + contact_pin + "',"
										+ " " + contact_city_id + ","
										+ " 1,"
										+ " '',"
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "')";
								// SOP("contact StrSql = " + StrSql);
								contact_id = UpdateQueryReturnID(StrSql);
							}
						}

						veh_modelyear = crs.getString("MfgYear");
						veh_sale_date = crs.getString("SalesDate");
						Date dttemp;
						if (veh_sale_date != null && !veh_sale_date.equals("")) {
							dttemp = new SimpleDateFormat("yyyy-MM-dd").parse(veh_sale_date);
							veh_sale_date = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
						}
						SOP("veh_sale_date ============= " + veh_sale_date);

						veh_sale_amount = crs.getString("SalesAmount");
						veh_notes = "Color: " + crs.getString("Color") + "<br/>Dealer Executive: " + crs.getString("DealerExecutive") + "";

						SOP("customer_id = " + customer_id);
						SOP("contact_id = " + contact_id);
						// ///////////// Insert the record in to vehicle table
						// //////////////
						if (!customer_id.equals("0") && !contact_id.equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
									+ " (veh_customer_id,"
									+ " veh_contact_id,"
									+ " veh_so_id,"
									+ " veh_item_id,"
									+ " veh_modelyear,"
									+ " veh_chassis_no,"
									+ " veh_engine_no,"
									+ " veh_reg_no,"
									+ " veh_sale_date,"
									+ " veh_sale_amount,"
									+ " veh_kms,"
									+ " veh_service_duekms,"
									+ " veh_service_duedate,"
									+ " veh_insursource_id,"
									+ " veh_renewal_date,"
									+ " veh_warranty_expirydate,"
									+ " veh_iacs,"
									+ " veh_notes,"
									+ " veh_entry_id,"
									+ " veh_entry_date)"
									+ " VALUES"
									+ " (" + customer_id + ","
									+ " " + contact_id + ","
									+ " 0,"// /////////sales order
									+ " " + item_id + ","
									+ " '" + veh_modelyear + "',"
									+ " " + veh_chassis_no + ","
									+ " " + veh_engine_no + ","
									+ " '" + veh_reg_no.toUpperCase().replaceAll(" ", "") + "',"
									+ " '" + veh_sale_date + "',"
									+ " " + veh_sale_amount + ","
									+ " 0,"
									+ " 0,"
									+ " '',"
									+ " " + veh_insursource_id + ","
									+ " '',"
									+ " '',"
									+ " 0,"
									+ " '" + veh_notes + "',"
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "')";
							veh_id = UpdateQueryReturnID(StrSql);
							// SOP("StrSql veh= " + StrSqlBreaker(StrSql));
							SOP("veh_id = " + veh_id);
						}
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
								+ " SET"
								+ " veh_renewal_date = '" + veh_renewal_date + "',"
								+ " veh_modified_id = " + emp_id + ","
								+ " veh_modified_date = '" + ToLongDate(kknow()) + "'"
								+ " WHERE veh_id = " + veh_id + "";
						updateQuery(StrSql);
					}

					if (!customer_id.equals("0") && !contact_id.equals("0") && !veh_id.equals("0")) {
						insurcount++;

						// StrSql = "INSERT INTO " + compdb(comp_id) +
						// "axela_insurance_policy"
						// + " (insurpolicy_branch_id,"
						// + " insurpolicy_customer_id,"
						// + " insurpolicy_contact_id,"
						// + " insurpolicy_veh_id,"
						// + " insurpolicy_date,"
						// + " insurpolicy_start_date,"
						// + " insurpolicy_end_date,"
						// + " insurpolicy_insurtype_id,"
						// + " insurpolicy_inscomp_id,"
						// + " insurpolicy_policytype_id,"
						// + " insurpolicy_policy_no,"
						// + " insurpolicy_covernote_no,"
						// + " insurpolicy_premium_amt,"
						// + " insurpolicy_paymode_id,"
						// + " insurpolicy_cheque_no,"
						// + " insurpolicy_cheque_date,"
						// + " insurpolicy_cheque_bank_id,"
						// + " insurpolicy_idv_amt,"
						// + " insurpolicy_od_amt,"
						// + " insurpolicy_od_discount,"
						// + " insurpolicy_payout,"
						// + " insurpolicy_desc,"
						// + " insurpolicy_terms,"
						// + " insurpolicy_emp_id,"
						// + " insurpolicy_field_emp_id,"
						// + " insurpolicy_active,"
						// + " insurpolicy_notes,"
						// + " insurpolicy_entry_id,"
						// + " insurpolicy_entry_date)"
						// + " VALUES"
						// + " (" + branch_id + ","
						// + " " + customer_id + ","
						// + " " + contact_id + ","
						// + " " + veh_id + ","
						// + " '" + ConvertShortDateToStr(insurpolicy_date) + "',"
						// + " '" + ConvertShortDateToStr(insurpolicy_start_date) +
						// "',"
						// + " '" + ConvertShortDateToStr(insurpolicy_end_date) + "',"
						// + " " + insur_type_id + ","
						// + " '" + insurpolicy_inscomp_id + "',"
						// + " '" + insurpolicy_policytype_id + "',"
						// + " '" + insurpolicy_policy_no + "',"
						// + " '" + insurpolicy_covernote_no + "',"
						// + " '" + insurpolicy_premium_amt + "',"
						// + " '" + insurpolicy_paymode_id + "',"
						// + " '" + insurpolicy_cheque_no + "',"
						// + " '" + insurpolicy_cheque_date + "',"
						// + " '" + insurpolicy_cheque_bank_id + "',"
						// + " '" + insurpolicy_idv_amt + "',"
						// + " '" + insurpolicy_od_amt + "',"
						// + " '" + insurpolicy_od_discount + "',"
						// + " '" + insurpolicy_payout + "',"
						// + " '" + insurpolicy_desc + "',"
						// + " '" + insurpolicy_terms + "',"
						// + " " + insurpolicy_emp_id + ","
						// + " " + insurpolicy_field_emp_id + ","
						// + " '" + insurpolicy_active + "',"
						// + " '" + insurpolicy_notes + "',"
						// + " " + emp_id + ","
						// + " '" + ToLongDate(kknow()) + "')";
						// SOP("rsveh insur= " + StrSqlBreaker(StrSql));
						// insurpolicy_id = UpdateQueryReturnID(StrSql);
						SOP("insurpolicy_id === " + insurpolicy_id);

						followup_time = "";
						followup_time = AddMonth(insurpolicy_date, -3);

						// SOP("insurfollowup_followup_time = 11==" +
						// insurfollowup_followup_time);
						// insurance followup
						// StrSql = " INSERT INTO " + compdb(comp_id) +
						// "axela_insurance_followup"
						// + " (insurfollowup_veh_id, "
						// + " insurfollowup_emp_id, "
						// + " insurfollowup_followuptype_id,"
						// + " insurfollowup_priorityinsurfollowup_id,"
						// + " insurfollowup_followup_time, "
						// + " insurfollowup_desc, "
						// + " insurfollowup_entry_id, "
						// + " insurfollowup_entry_time, "
						// + " insurfollowup_trigger) "
						// + " VALUES"
						// + " ('" + veh_id + "',"
						// + " " + insurpolicy_emp_id + ","
						// + " 1,"
						// + " 2,"
						// + " concat(substr('" + insurfollowup_followup_time +
						// "', 1, 8), substr('" + ToLongDate(kknow()) +
						// "', 9, 14)),"
						// + " '',"
						// + "" + emp_id + ","
						// + " '" + ToLongDate(kknow()) + "',"
						// + " 0)";
						// SOP("StrSql ===fol=== " + StrSqlBreaker(StrSql));
						// updateQuery(StrSql);
						// eof insur followup
						StrSql = "UPDATE insur"
								+ " SET"
								+ " Active = 1"
								+ " WHERE slno = " + crs.getString("slno");
						updateQuery(StrSql);
						// SOP("enquiry_id = " + insurpolicy_id);

					} else {
						StrSql = "UPDATE insur"
								+ " SET"
								+ " Active = 2"
								+ " WHERE slno = " + crs.getString("slno");
						updateQuery(StrSql);
					}
				}

				Thread.sleep(3000);
				AddData(request, response);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
