package axela.service;

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

import cloudify.connect.Connect;

public class Booking_Enquiry extends Connect {

	public String StrSql = "";
	public String msg = "", msg1 = "";
	public String booking_customer_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String booking_veh_variant_id = "0";
	// public String booking_veh_model_id = "0";
	public String booking_veh_reg_no = "";
	public String booking_veh_id = "0";
	public String booking_time = "";
	public String followup_nextfollowup_time = "";
	public String booking_type = "0";
	public String vehfollowup_voc = "";
	public String booking_driver = "";
	public String booking_other_address = "";
	public String booking_veh_branch_id = "0";
	public String booking_contact_id = "0";
	public String booking_veh_crm_id = "";
	public String booking_campaign_id = "0";
	public String booking_soe_id = "0";
	public String booking_sob_id = "0";
	public String booking_entry_id = "0", booking_entry_date = "";
	public String branch_city_id = "0";
	public String vehfollowup_id = "0";
	public String vehfollowup_booking_main = "", vehfollowup_vehaction_id = "";
	public String submitB = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String booking_refemp_id = "0";
	public String contact_name = "";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";
	public String customer_name = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public String branch_email1 = "";
	public String crm_emp_email1 = "", crm_emp_email2 = "";
	public String crm_emp_mobile1 = "", crm_emp_mobile2 = "";
	public String branch_servicebooking_email = "";

	public String brandconfig_vehfollowup_booking_email_enable = "";
	public String brandconfig_vehfollowup_booking_email_sub = "";
	public String brandconfig_vehfollowup_booking_email_format = "";
	public String brandconfig_vehfollowup_booking_exe_email_sub = "";
	public String brandconfig_vehfollowup_booking_exe_email_format = "";
	public String brandconfig_vehfollowup_booking_sms_enable = "";
	public String brandconfig_vehfollowup_booking_sms_format = "";
	public String brandconfig_vehfollowup_booking_exe_sms_format = "";
	public Service_Variant_Check variantcheck = new Service_Variant_Check();
	public String ddwebsite_check = "";
	// inbound package fields
	public String inbound_check = "";
	public String vehfollowup_call_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				booking_time = strToLongDate(ToShortDate(kknow()));
				followup_nextfollowup_time = strToLongDate(ToShortDate(kknow()));
				submitB = PadQuotes(request.getParameter("addbutton"));
				booking_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				inbound_check = PadQuotes(request.getParameter("inbound"));
				vehfollowup_call_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_call_id")));

				if (booking_veh_branch_id.equals("0")) {
					booking_veh_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (booking_veh_branch_id.equals("0")) {
						booking_veh_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 "
								+ " AND branch_branchtype_id IN (3) LIMIT 1");
					}
				}
				if (!booking_veh_branch_id.equals("0")) {
					branch_city_id = ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch where branch_id =" + booking_veh_branch_id);
				}

				if ("Add Enquiry".equals(submitB)) {
					GetValues(request, response);
					booking_entry_id = emp_id;
					booking_entry_date = ToLongDate(kknow());
					AddVehFields(response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						if (ReturnPerm(comp_id, "emp_service_vehicle_access", request).equals("0")) {
							response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=" + msg1));
						}
						else {
							if (inbound_check.equals("yes")) {
								msg = "Booking Enquiry added successfully!";
							} else {
								response.sendRedirect(response.encodeRedirectURL("vehicle-list.jsp?veh_id=" + booking_veh_id + "&msg=" + msg1));
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			if (conntx != null) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed()) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
			}

			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		booking_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		// Customer Details
		customer_name = PadQuotes(request.getParameter("txt_booking_customer_name"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		// Vechile Details
		// ddwebsite_check = PadQuotes(request.getParameter("ddwebsite_check"));
		booking_veh_variant_id = CNumeric(PadQuotes(request.getParameter("servicepreownedvariant")));
		booking_veh_reg_no = PadQuotes(request.getParameter("txt_booking_veh_reg_no"));
		// booking_veh_chassis_no = PadQuotes(request.getParameter("txt_booking_veh_chassis_no"));
		// booking_veh_engine_no = PadQuotes(request.getParameter("txt_booking_veh_engine_no"));

		// followup details
		booking_veh_crm_id = PadQuotes(request.getParameter("dr_booking_veh_executive"));
		booking_time = PadQuotes(request.getParameter("txt_booking_time"));
		followup_nextfollowup_time = PadQuotes(request.getParameter("txt_nextfollowup_time"));
		booking_type = PadQuotes(request.getParameter("dr_booking_type"));
		vehfollowup_voc = PadQuotes(request.getParameter("txt_vehfollowup_voc"));
		// booking_driver = PadQuotes(request.getParameter("dr_vehfollowup_pickupdriver_emp_id"));
		// booking_other_address = PadQuotes(request.getParameter("txt_vehfollowup_pickuplocation"));
		booking_soe_id = CNumeric(PadQuotes(request.getParameter("dr_booking_soe_id")));
		booking_sob_id = CNumeric(PadQuotes(request.getParameter("dr_booking_sob_id")));
		booking_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_booking_campaign_id")));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		String customername = "";
		if (booking_veh_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
		if (contact_title_id.equals("0")) {
			msg = msg + "<br>Select Title!";
		}
		if (contact_fname.equals("")) {
			msg = msg + "<br>Enter the Contact Person Name!";
		} else {
			contact_fname = toTitleCase(contact_fname);
		}
		if (!contact_lname.equals("")) {
			contact_lname = toTitleCase(contact_lname);
		}
		if (customer_name.equals("")) {
			customername = (contact_fname + " " + contact_lname).trim();
			customer_name = toTitleCase(customername);
		} else {
			customername = toTitleCase(customer_name);
			customer_name = customername;
		}

		if (contact_mobile1.equals("91-")) {
			contact_mobile1 = "";
		}
		if (contact_mobile1.equals("")) {
			msg = msg + "<br>Enter Contact Mobile 1!";
		} else if (!contact_mobile1.equals("")) {
			if (!IsValidMobileNo11(contact_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			}
			else {
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
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_mobile1 = '" + contact_mobile1 + "'"
						+ " OR contact_mobile2 = '" + contact_mobile1 + "')"
						+ " AND contact_contacttype_id = 9"
						+ " AND customer_branch_id = " + booking_veh_branch_id;
				booking_contact_id = CNumeric(ExecuteQuery(StrSql));
			}
		}
		if (!contact_email1.equals("")) {
			if (!IsValidEmail(contact_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1!";
			} else {
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
			}
		}

		if (booking_veh_variant_id.equals("0") && !ddwebsite_check.equals("yes")) {
			msg = msg + "<br>Select Variant!";
		}

		if (!booking_type.equals("0")) {
			if (booking_time.equals("")) {
				msg += "<br>Enter Booking Time!";
			}
			else {
				if (!isValidDateFormatLong(booking_time)) {
					msg = msg + "<br>Enter Valid Booking Time!";
				}
				else if (Long.parseLong(ConvertLongDateToStr(booking_time)) < (Long.parseLong((ToLongDate(kknow()))))) {
					msg += "<br>Booking Time must be greater than Current Time!";
				}
			}
		}
		if (booking_veh_crm_id.equals("0")) {
			msg += "<br>Select CRM Executive!";
		}

		// if (booking_type.equals("0")) {
		// msg = msg + "<br>Select Booking Type!";
		// }
		// if (booking_type.equals("2")) {
		// if (booking_driver.equals("0")) {
		// msg = msg + "<br>Select Driver!";
		// }
		// if (booking_other_address.equals("")) {
		// msg = msg + "<br>Enter Location!";
		// }
		// }
		if (!booking_type.equals("2")) {
			booking_driver = "0";
			booking_other_address = "";
		}
		if (booking_type.equals("0")) {
			booking_time = "";
			vehfollowup_booking_main = "0";
			vehfollowup_vehaction_id = "0";
		} else {
			followup_nextfollowup_time = "";
			vehfollowup_booking_main = "1";
			vehfollowup_vehaction_id = "1";
		}
		if (!followup_nextfollowup_time.equals("")) {
			if (!isValidDateFormatLong(followup_nextfollowup_time)) {
				msg = msg + "<br>Enter Valid Follow-up Time!";
			}
			else if (Long.parseLong(ConvertLongDateToStr(followup_nextfollowup_time)) < (Long.parseLong((ToLongDate(kknow()))))) {
				msg += "<br>Follow-up Time must be greater than Current Time!";
			}
		}
		if (!booking_type.equals("0")) {
			followup_nextfollowup_time = strToLongDate(ToLongDate(kknow()));
		}

		if (booking_soe_id.equals("0")) {
			msg = msg + "<br>Select Source of Enquiry!";
		}
		if (booking_sob_id.equals("0")) {
			msg = msg + "<br>Select Source of Business!";
		}

		if (booking_campaign_id.equals("0")) {
			msg = msg + "<br>Select Campaign!";
		}
		// }
		if (!booking_veh_crm_id.equals(emp_id)) {
			booking_refemp_id = emp_id;
		} else {
			booking_refemp_id = "0";
		}

		if (booking_veh_reg_no.equals("")) {
			msg = msg + "<br>Enter Registration Number!";
		} else {
			StrSql = "SELECT veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " WHERE veh_reg_no = '" + booking_veh_reg_no + "'";

			booking_veh_id = CNumeric(ExecuteQuery(StrSql));

			StrSql = "SELECT vehfollowup_id FROM " + compdb(comp_id) + "axela_service_followup main"
					+ " WHERE 1=1 "
					+ " AND main.vehfollowup_id > COALESCE((SELECT MAX(vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup alreadyserviced"
					+ " WHERE 1=1"
					+ " AND alreadyserviced.vehfollowup_desc LIKE 'Already serviced at%' "
					+ " AND alreadyserviced.vehfollowup_veh_id =" + booking_veh_id
					+ " ),0)"
					+ " AND main.vehfollowup_id > COALESCE((SELECT MAX(vehfollowup_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_followup lost"
					+ " WHERE 1=1"
					+ " AND lost.vehfollowup_vehlostcase1_id != 0 "
					+ " AND lost.vehfollowup_veh_id =" + booking_veh_id
					+ " ),0)"
					+ " AND main.vehfollowup_vehaction_id = 1"
					+ " AND main.vehfollowup_veh_id =" + booking_veh_id;
			// SOPInfo("StrSql====booking-enquiry==" + StrSql);

			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				booking_veh_crm_id = CNumeric(ExecuteQuery("SELECT veh_crmemp_id " + " FROM " + compdb(comp_id) + "axela_service_veh "
						+ " WHERE veh_id = '" + booking_veh_id + "' "));

				vehfollowup_vehaction_id = "0";
				booking_type = "0";
				booking_time = "";
				vehfollowup_booking_main = "0";

			}
			// SOP("booking_veh_crm_id==" + booking_veh_crm_id);
		}

	}
	// Adding customer details
	public void AddCustomerFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_mobile1_phonetype_id,"
					+ " customer_email1,"
					+ " customer_notes,"
					+ " customer_city_id,"
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_entry_id,"
					+ " customer_entry_date,"
					+ " customer_modified_id,"
					+ " customer_modified_date) "
					+ " VALUES "
					+ " ('" + booking_veh_branch_id + " ',"// customer_branch_id
					+ " '" + customer_name + " ',"// customer_name
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " 3," // contact_mobile1_phonetype_id //for service
					+ " '" + contact_email1 + "'," // contact_email1
					+ "'',"// customer notes
					+ " '" + branch_city_id + "',"// cust_city_id
					+ " '" + booking_soe_id + "'," // enquiry_soe_id
					+ " '" + booking_sob_id + "'," // enquiry_sob_id
					+ " 32,"// customer_accgroup_id
					+ " 1,"// customer_type
					+ "'" + emp_id + "'," // entry_id
					+ "'" + ToLongDate(kknow()) + "'," // entry date
					+ " 0," // customer_modified_id
					+ " '')"; // customer_modified_date

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				booking_customer_id = rs.getString(1);
			}
			// SOP("booking_customer_id===after inserting ustomer==" + booking_customer_id);
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// Adding contact details
	public void AddContactFields() throws SQLException {
		try {
			if (booking_customer_id.equals("0")) {
				booking_customer_id = CNumeric(ExecuteQuery("SELECT veh_customer_id FROM " + compdb(comp_id) + "axela_service_veh "
						+ " WHERE veh_reg_no = '" + booking_veh_reg_no + "'"));
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle,"
					+ " contact_mobile1,"
					+ " contact_mobile1_phonetype_id,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_phone1,"
					+ " contact_phone2,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date"
					+ ")"
					+ " VALUES "
					+ " (" + booking_customer_id + "," // contact_customer_id
					+ " 9," // contact_contacttype_id
					+ " " + contact_title_id + "," // contact_title_id
					+ " '" + contact_fname + "'," // contact_fname
					+ " '" + contact_lname + "'," // contact_lname
					+ " ''," // contact_jobtitle
					+ " '" + contact_mobile1 + "'," // contact_mobile1
					+ " 3,"// contact_mobile1_phonetype_id //service
					+ " ''," // contact_mobile2
					+ " '" + contact_email1 + "'," // contact_email1
					+ " ''," // contact_email2
					+ " ''," // contact_phone1
					+ " ''," // contact_phone2
					+ " ''," // contact_address
					+ " '" + branch_city_id + "',"// contact_city_id
					+ " ''," // contact_pin
					+ " '1'," // contact_active
					+ " ''," // contact_notes
					+ " '" + emp_id + "'," // contact_entry_id
					+ " '" + ToLongDate(kknow()) + "')"; // contact_entry_date
			// SOP("StrSql=====contact===" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				booking_contact_id = rs.getString(1);
			}
			rs.close();
			// //SOP("booking_contact_id=after inserting contact==" + booking_contact_id);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// Adding vech details
	public void AddVehFields(HttpServletResponse response) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (!booking_veh_id.equals("0")) {
					UpdateVehFields(response, comp_id, booking_veh_id);
				} else if (booking_veh_id.equals("0")) {
					AddCustomerFields();
					AddContactFields();
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
							+ " (veh_branch_id,"
							+ " veh_customer_id,"
							+ " veh_contact_id,"
							+ " veh_so_id,"
							+ " veh_variant_id,"
							+ " veh_modelyear,"
							+ " veh_comm_no,"
							// + " veh_chassis_no,"
							// + " veh_engine_no,"
							+ " veh_reg_no,"
							+ " veh_sale_date,"
							+ " veh_sale_amount,"
							+ " veh_emp_id,"
							+ " veh_vehsource_id,"
							+ " veh_kms,"
							+ " veh_calservicedate,"
							+ " veh_cal_kms,"
							+ " veh_lastservice,"
							+ " veh_lastservice_kms,"
							+ " veh_warranty_expirydate,"
							+ " veh_iacs,"
							+ " veh_contactable_id,"
							+ " veh_priorityinsurfollowup_id,"
							+ " veh_service_duekms,"
							+ " veh_service_duedate,"
							+ " veh_crmemp_id,"
							+ " veh_insuremp_id,"
							+ " veh_insursource_id,"
							+ " veh_insur_date,"
							+ " veh_renewal_date,"
							+ " veh_ref_no,"
							+ " veh_imp_no,"
							+ " veh_notes,"
							+ " veh_entry_id,"
							+ " veh_entry_date,"
							+ " veh_modified_id,"
							+ " veh_modified_date "
							+ ")"
							+ " VALUES "
							+ " ('" + booking_veh_branch_id + "'," // booking_veh_branch_id
							+ " '" + booking_customer_id + "'," // customer_id
							+ " '" + booking_contact_id + "',"// contact_id
							+ " '0'," // veh_so_id
							+ " '" + booking_veh_variant_id + "'," // veh_variant_id
							+ " ''," // veh_modelyear
							+ " '0'," // veh_comm_no
							// + " '" + booking_veh_chassis_no + "'," // chassis_no
							// + " '" + booking_veh_engine_no + "'," // engine_no
							+ " '" + booking_veh_reg_no + "'," // reg. no
							+ " ''," // veh_sale_date
							+ " '0'," // veh_sale_amount
							+ " '1'," // veh_emp_id
							+ " '0'," // veh_vehsource_id
							+ " '0'," // veh_kms
							+ " ''," // veh_calservicedate
							+ " '0'," // veh_cal_kms
							+ " ''," // veh_lastservice
							+ " '0'," // veh_lastservice_kms
							+ " ''," // veh_warranty_expirydate
							+ " '0'," // veh_iacs
							+ " '1'," // veh_contactable_id
							+ " '0'," // veh_priorityinsurfollowup_id
							+ " '0'," // veh_service_duekms
							+ " ''," // veh_service_duedate
							+ " '" + booking_veh_crm_id + "'," // veh_crmemp_id
							+ " '0'," // veh_insuremp_id
							+ " '0'," // veh_insursource_id
							+ " ''," // veh_insur_date
							+ " ''," // veh_renewal_date
							+ " '0'," // veh_ref_no
							+ " '0'," // veh_imp_no
							+ " ''," // veh_notes
							+ " '" + booking_entry_id + "'," // entry_emp_id
							+ " '" + booking_entry_date + "'," // veh_entry_date
							+ " '0'," // modified_id
							+ " '')"; // modified_date

					// SOP("StrSql===insert veh===" + StrSql);
					stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						booking_veh_id = rs.getString(1);
					}
					rs.close();
					if (!booking_veh_id.equals("0")) {
						msg1 += "Booking Enquiry added successfully!";
					}
				}
				// SOP("booking_veh_id==after add veh==" + booking_veh_id);
				if (!booking_veh_id.equals("0")) {
					AddServiceFollowupFields(booking_veh_id);
				}

				conntx.commit();

				if (!vehfollowup_id.equals("0")) {
					populateConfigDetails(comp_id);

					if (brandconfig_vehfollowup_booking_email_enable.equals("1")) {
						if (!brandconfig_vehfollowup_booking_email_sub.equals("")
								&& !brandconfig_vehfollowup_booking_email_format.equals("")) {
							if (!branch_email1.equals("") && !contact_email1.equals(""))
								SendFollowUpEmail(comp_id, "customer", contact_email1);
						}
					}

					if (brandconfig_vehfollowup_booking_email_enable.equals("1")) {
						if (!brandconfig_vehfollowup_booking_exe_email_sub.equals("")
								&& !brandconfig_vehfollowup_booking_exe_email_format.equals("")) {
							if (!branch_email1.equals("") && !crm_emp_email1.equals(""))
								SendFollowUpEmail(comp_id, "executive", crm_emp_email1);
						}
					}

					// //SOP("brandconfig_vehfollowup_booking_sms_enable====" + brandconfig_vehfollowup_booking_sms_enable);
					// //SOP("brandconfig_vehfollowup_booking_sms_format====" + brandconfig_vehfollowup_booking_sms_format);
					// //SOP("contact_mobile1====" + contact_mobile1);
					if (brandconfig_vehfollowup_booking_sms_enable.equals("1")
							&& !brandconfig_vehfollowup_booking_sms_format.equals("")) {
						if (!contact_mobile1.equals("")) {
							SendFollowupSMS(comp_id, "customer", contact_mobile1);
						}
					}

					if (brandconfig_vehfollowup_booking_sms_enable.equals("1")
							&& !brandconfig_vehfollowup_booking_exe_sms_format.equals("")) {
						if (!crm_emp_mobile1.equals("")) {
							SendFollowupSMS(comp_id, "executive", crm_emp_mobile1);
						}
						if (!crm_emp_mobile2.equals("")) {
							SendFollowupSMS(comp_id, "executive", crm_emp_mobile2);
						}
					}
					// SOP("commit emaiil and sms transactions");
					conntx.commit();
				}

			} catch (Exception ex) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

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
	// for service followup
	public void UpdateVehFields(HttpServletResponse response, String comp_id, String booking_veh_id) throws SQLException {
		try {
			if (booking_contact_id.equals("0")) {
				AddContactFields();
			}

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh SET "
					+ " veh_branch_id = " + booking_veh_branch_id + ","
					+ " veh_contact_id =" + booking_contact_id + ","
					+ " veh_contactable_id = '1',"
					+ " veh_modified_id=" + emp_id + ","
					+ " veh_modified_date=" + ToLongDate(kknow())
					+ " WHERE veh_id =" + booking_veh_id;
			// SOP("StrSql==after update veh==" + StrSql);
			stmttx.executeUpdate(StrSql);
			// SOP("booking_veh_id==after update veh==" + booking_veh_id);
			if (!booking_veh_id.equals("0")) {
				msg1 += "Vehicle Updated successfully!";

			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void AddServiceFollowupFields(String booking_veh_id) throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
					+ " (vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_vehcalltype_id,"
					+ " vehfollowup_followup_time,"
					+ " vehfollowup_contactable_id,"
					+ " vehfollowup_vehaction_id,"
					+ " vehfollowup_kms,"
					+ " vehfollowup_workshop_branch_id,"
					+ " vehfollowup_appt_time,"
					+ " vehfollowup_bookingtype_id,"
					+ " vehfollowup_booking_main,"
					+ " vehfollowup_pickupdriver_emp_id,"
					+ " vehfollowup_pickuplocation,"
					+ " vehfollowup_jc_id,"
					+ " vehfollowup_nextcalltime,"
					+ " vehfollowup_vehlostcase1_id,"
					+ " vehfollowup_competitor_id,"
					+ " vehfollowup_notcontactable_id,"
					+ " vehfollowup_desc,"
					+ " vehfollowup_voc,"
					+ " vehfollowup_postponed,"
					+ " vehfollowup_followup_main,"
					+ " vehfollowup_soe_id,"
					+ " vehfollowup_sob_id,"
					+ " vehfollowup_campaign_id,";
			if (inbound_check.equals("yes") && !vehfollowup_call_id.equals("0")) {
				StrSql += " vehfollowup_call_id, ";
			}
			StrSql += " vehfollowup_refemp_id,"
					+ " vehfollowup_enquiry_time,"
					+ " vehfollowup_entry_id,"
					+ " vehfollowup_entry_time)"
					+ " VALUES"
					+ " (" + booking_veh_id + ","
					+ " '" + booking_veh_crm_id + "',"
					+ "'0',"// vehfollowup_vehcalltype_id
					+ "'" + ConvertLongDateToStr(followup_nextfollowup_time) + "',"// vehfollowup_followup_time
					+ "'1',"// vehfollowup_contactable_id
					+ "'" + vehfollowup_vehaction_id + "',"// vehfollowup_vehaction_id
					+ "'0',"// vehfollowup_kms
					+ "'" + booking_veh_branch_id + "',"
					+ "'" + ConvertLongDateToStr(booking_time) + "',"// vehfollowup_appt_time
					+ "'" + booking_type + "',"
					+ "'" + vehfollowup_booking_main + "',"// vehfollowup_booking_main
					+ "0," // vehfollowup_pickupdriver_emp_id
					+ "'',"// vehfollowup_pickuplocation
					+ "'0',"// vehfollowup_jc_id
					+ "'',"// vehfollowup_nextcalltime
					+ "'0',"// vehfollowup_vehlostcase1_id
					+ "'0',"// vehfollowup_competitor_id
					+ "'0',"// vehfollowup_notcontactable_id
					+ "'',"// vehfollowup_desc
					+ " '" + vehfollowup_voc + "',"
					+ "'0',"// vehfollowup_postponed
					+ "'1'," // vehfollowup_followup_main
					+ " '" + booking_soe_id + "',"
					+ " '" + booking_sob_id + "',"
					+ " '" + booking_campaign_id + "',";
			if (inbound_check.equals("yes") && !vehfollowup_call_id.equals("0")) {
				StrSql += " " + vehfollowup_call_id + ","; // vehfollowup_call_id
			}
			StrSql += " '" + booking_refemp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"// vehfollowup_enquiry_time
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";// vehfollowup_entry_time
			// SOP("StrSql====followup=" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				vehfollowup_id = rs.getString(1);
			}
			rs.close();
			// //SOP("vehfollowup_id===123===" + vehfollowup_id);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// For Booking type
	public String PopulateServiceBookingType(String comp_id, String vehfollowup_bookingtype_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT bookingtype_id, bookingtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_bookingtype"
					+ " WHERE 1 = 1"
					// + " AND bookingtype_id = 1"
					+ " GROUP BY bookingtype_id"
					+ " ORDER BY bookingtype_id ";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bookingtype_id"));
				Str.append(StrSelectdrop(crs.getString("bookingtype_id"), vehfollowup_bookingtype_id));
				Str.append(">").append(crs.getString("bookingtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// For Service Pickup and check vehfollowup_pickupdriver_emp_id
	public String PopulateServicePickUp(String comp_id, String booking_driver) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ " AND emp_pickup_driver = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), booking_driver));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String comp_id, String booking_veh_branch_id) {
		// //SOP("booking_veh_branch_id====" + booking_veh_branch_id);
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " AND branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// //SOP("StrSql == " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), booking_veh_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			// //SOP("StrSql == " + booking_veh_branch_id);
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " WHERE 1 =  1";
			if (!CNumeric(contact_title_id).equals("0")) {
				StrSql += " AND title_id = " + contact_title_id + "";
			}
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
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

	// public String PopulateModel(String booking_veh_branch_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT model_id, preownedmodel_name"
	// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
	// + " INNER JOIN axela_brand ON brand_id = model_brand_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
	// + " WHERE branch_id =" + booking_veh_branch_id;
	// // if (!customer_branch_id.equals("0")) {
	// // StrSql += " AND branch_id =" + customer_branch_id;
	// // }
	// StrSql += " GROUP BY model_id"
	// + " ORDER BY brand_name, preownedmodel_name";
	// // //SOP("StrSql----PopulateModel----------" + StrSql);
	// CachedRowSet crs = processQuery(StrSql, 0);
	// Str.append("<select name=\"dr_item_model_id\" id=\"dr_item_model_id\" class=\"form-control\" onchange=\"PopulateItem(this.value);\">\n");
	// Str.append("<option value=0>Select</option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("model_id"));
	// Str.append(StrSelectdrop(crs.getString("model_id"), booking_veh_model_id));
	// Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
	// }
	// crs.close();
	// Str.append("</select>\n");
	// // //SOP("Str------" + Str);
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
	//
	// public String PopulateItem(String veh_model_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT item_id, IF(item_code != '', CONCAT(variant_name, ' (', item_code, ')'), variant_name) AS variant_name"
	// + " FROM " + compdb(comp_id) + "axela_inventory_item"
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
	// + " WHERE item_model_id = " + veh_model_id + ""
	// + " AND item_type_id = 1"
	// + " ORDER BY variant_name";
	//
	// // //SOP("StrSql----PopulateItem----------" + StrSqlBreaker(StrSql));
	//
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\">\n");
	// Str.append("<option value=0>Select</option>\n");
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("item_id"));
	// Str.append(StrSelectdrop(crs.getString("item_id"), booking_veh_variant_id));
	// Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
	// }
	//
	// crs.close();
	// Str.append("</select>\n");
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	public String PopulateCRMExecutive(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_crm = 1"
					+ " AND emp_active = 1";
			// + " AND emp_branch_id =" + booking_veh_branch_id;
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + ""
					+ " AND leave_active = 1)";

			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id = 1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// //SOP("StrSql=12=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_booking_veh_executive\" id=\"dr_booking_veh_executive\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Service Advisor</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), booking_veh_crm_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1 = 1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id = " + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), booking_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB(String booking_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1";
			if (!booking_soe_id.equals("0")) {
				StrSql += " AND soetrans_soe_id = " + booking_soe_id + "";
			}

			StrSql += " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// //SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_booking_sob_id\" id=\"dr_booking_sob_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), booking_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCampaign(String booking_veh_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP("booking_time123" + booking_time);
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1 "
					+ " AND camptrans_branch_id = " + booking_veh_branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ToLongDate((kknow())) + "',1,8) "
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ToLongDate(kknow()) + "',1,8) "
					// + " AND branch_id =" + booking_veh_branch_id
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// //SOP("PopulateCampaign-------" + StrSqlBreaker(StrSql));
			// SOP("PopulateCampaign-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_booking_campaign_id\" id=\"dr_booking_campaign_id\" class=\" form-dropdown form-control\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id")).append("");;
				Str.append(Selectdrop(crs.getInt("campaign_id"), booking_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
						.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			// Str.append("<option value=").append(crs.getString("emp_id")).append("");
			// Str.append(Selectdrop(crs.getInt("emp_id"), booking_veh_crm_id));
			// Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			// }
			crs.close();
			Str.append("</select>\n");
			// //SOP("str" + Str);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axelaauto.axela_preowned_model"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1"
					+ " AND carmanuf_id IN (2,10)"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void populateConfigDetails(String comp_id) {
		try {
			StrSql = "SELECT branch_email1,"
					+ " emp_email1, emp_email2,"
					+ " emp_mobile1, emp_mobile2,"
					+ " branch_servicebooking_email,"
					+ " brandconfig_vehfollowup_booking_email_enable, "
					+ " brandconfig_vehfollowup_booking_email_sub,"
					+ " brandconfig_vehfollowup_booking_email_format, "
					+ " brandconfig_vehfollowup_booking_exe_email_sub,"
					+ " brandconfig_vehfollowup_booking_exe_email_format, "
					+ " brandconfig_vehfollowup_booking_sms_enable, "
					+ " brandconfig_vehfollowup_booking_sms_format,"
					+ " brandconfig_vehfollowup_booking_exe_sms_format"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + booking_veh_branch_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + booking_veh_crm_id
					+ " WHERE veh_id = " + booking_veh_id;

			// SOP("strsql==pop=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("strsql===" + StrSql);
			// crs.beforeFirst();
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");
				crm_emp_email1 = crs.getString("emp_email1");
				crm_emp_email2 = crs.getString("emp_email2");
				crm_emp_mobile1 = crs.getString("emp_mobile1");
				crm_emp_mobile2 = crs.getString("emp_mobile2");
				branch_servicebooking_email = crs.getString("branch_servicebooking_email");
				brandconfig_vehfollowup_booking_email_enable = crs.getString("brandconfig_vehfollowup_booking_email_enable");
				brandconfig_vehfollowup_booking_email_sub = crs.getString("brandconfig_vehfollowup_booking_email_sub");
				brandconfig_vehfollowup_booking_email_format = crs.getString("brandconfig_vehfollowup_booking_email_format");
				brandconfig_vehfollowup_booking_exe_email_sub = crs.getString("brandconfig_vehfollowup_booking_exe_email_sub");
				brandconfig_vehfollowup_booking_exe_email_format = crs.getString("brandconfig_vehfollowup_booking_exe_email_format");
				brandconfig_vehfollowup_booking_sms_enable = crs.getString("brandconfig_vehfollowup_booking_sms_enable");
				brandconfig_vehfollowup_booking_sms_format = crs.getString("brandconfig_vehfollowup_booking_sms_format");
				brandconfig_vehfollowup_booking_exe_sms_format = crs.getString("brandconfig_vehfollowup_booking_exe_sms_format");

				if (!crm_emp_email2.equals("")) {
					crm_emp_email1 = crm_emp_email1 + "," + crm_emp_email2;
				}

				// SOP("branch_servicebooking_email=1=" + branch_servicebooking_email);
				// SOP("brandconfig_vehfollowup_booking_email_sub=1=" + brandconfig_vehfollowup_booking_email_sub);
				// SOP("brandconfig_vehfollowup_booking_email_format=1=" + brandconfig_vehfollowup_booking_email_format);
				// SOP("brandconfig_vehfollowup_booking_sms_enable=1=" + brandconfig_vehfollowup_booking_sms_enable);
				// SOP("brandconfig_vehfollowup_booking_sms_format=1=" + brandconfig_vehfollowup_booking_sms_format);

			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	protected void SendFollowUpEmail(String comp_id, String type, String email_to) throws SQLException {
		String emailmsg = "", sub = "", email_contact_id = "", email_contact_name = "", email_cc = "";
		if (type.equals("customer")) {
			email_contact_id = booking_contact_id;
			email_contact_name = contact_fname + " " + contact_lname;
			sub = brandconfig_vehfollowup_booking_email_sub;
			emailmsg = brandconfig_vehfollowup_booking_email_format;
		}
		else if (type.equals("executive")) {
			email_contact_id = "0";
			email_contact_name = "";
			sub = brandconfig_vehfollowup_booking_exe_email_sub;
			emailmsg = brandconfig_vehfollowup_booking_exe_email_format;
			// checking if request is coming from DDmotors website
			if (ddwebsite_check.equals("yes")) {
				email_cc = branch_servicebooking_email;
			}
		}

		sub = "replace('" + sub + "','[VEHID]', veh_id)";
		sub = "replace(" + sub + ",'[REGNO]',veh_reg_no)";
		sub = "replace(" + sub + ",'[MODEL]', preownedmodel_name)";
		sub = "replace(" + sub + ",'[VARIANT]',variant_name)";
		sub = "replace(" + sub + ",'[SERVICETYPE]',COALESCE (vehcalltype_name,''))";
		sub = "replace(" + sub + ",'[SERVICEDUEDATE]', IF(veh_service_duedate = '','',COALESCE (DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'),'')))";
		sub = "replace(" + sub + ",'[BOOKINGTIME]', IF(vehfollowup_appt_time = '','',COALESCE (DATE_FORMAT(vehfollowup_appt_time,'%d/%m/%Y %h:%m:%s'),'')))";
		sub = "replace(" + sub + ",'[CRMEXE]',emp_name)";
		sub = "replace(" + sub + ",'[CRMEXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[CRMMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[CRMEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(contact_fname,' ',contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[BRANCHNAME]',branch_name)";
		sub = "replace(" + sub + ",'[BRANCHPHONE]', branch_phone1)";
		sub = "replace(" + sub + ",'[BRANCHMOBILE1]',branch_mobile1)";
		sub = "replace(" + sub + ",'[BRANCHEMAIL1]',branch_email1)";
		sub = "replace(" + sub + ",'[VEHFOLLOWUPID]',vehfollowup_id)";

		emailmsg = "replace('" + emailmsg + "','[VEHID]', veh_id)";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',veh_reg_no)";
		emailmsg = "replace(" + emailmsg + ",'[MODEL]', preownedmodel_name)";
		emailmsg = "replace(" + emailmsg + ",'[VARIANT]',variant_name)";
		emailmsg = "replace(" + emailmsg + ",'[SERVICETYPE]',COALESCE (vehcalltype_name,''))";
		emailmsg = "replace(" + emailmsg + ",'[SERVICEDUEDATE]', IF(veh_service_duedate = '','',COALESCE (DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[BOOKINGTIME]', IF(vehfollowup_appt_time = '','',COALESCE (DATE_FORMAT(vehfollowup_appt_time,'%d/%m/%Y %h:%m:%s'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXE]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[CRMMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CRMEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(contact_fname,' ',contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]',branch_name)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHPHONE]', branch_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHMOBILE1]',branch_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHEMAIL1]',branch_email1)";
		emailmsg = "replace(" + emailmsg + ",'[VEHFOLLOWUPID]',vehfollowup_id)";

		try {
			StrSql = "SELECT "
					+ "	branch_id,"
					+ " " + email_contact_id + ", "
					+ " '" + email_contact_name + "', "
					+ " '" + branch_email1 + "', "
					+ " '" + email_to + "', "
					+ " '" + email_cc + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE vehfollowup_id =" + vehfollowup_id;

			// SOP("StrSql--service-------mail--" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql--service-------mail--" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendFollowupSMS(String comp_id, String type, String sms_to) throws SQLException {
		String smsmsg = "", sms_contact_id = "", sms_contact_name = "";
		if (type.equals("customer")) {
			sms_contact_id = booking_contact_id;
			sms_contact_name = contact_fname + " " + contact_lname;
			smsmsg = brandconfig_vehfollowup_booking_sms_format;

		}
		else if (type.equals("executive")) {
			sms_contact_id = "0";
			sms_contact_name = "";
			smsmsg = brandconfig_vehfollowup_booking_exe_sms_format;

		}

		smsmsg = "replace('" + smsmsg + "','[VEHID]', veh_id)";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',veh_reg_no)";
		smsmsg = "replace(" + smsmsg + ",'[MODEL]', preownedmodel_name)";
		smsmsg = "replace(" + smsmsg + ",'[VARIANT]',variant_name)";
		smsmsg = "replace(" + smsmsg + ",'[SERVICETYPE]',COALESCE(vehcalltype_name,''))";
		smsmsg = "replace(" + smsmsg + ",'[SERVICEDUEDATE]', IF(veh_service_duedate = '','',COALESCE (DATE_FORMAT(veh_service_duedate,'%d/%m/%Y'),'')))";
		smsmsg = "replace(" + smsmsg + ",'[BOOKINGTIME]', IF(vehfollowup_appt_time = '','',COALESCE (DATE_FORMAT(vehfollowup_appt_time,'%d/%m/%Y %h:%m:%s'),'')))";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXE]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[CRMMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CRMEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(contact_fname,' ',contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',branch_name)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHPHONE]', branch_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHMOBILE1]',branch_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',branch_email1)";
		smsmsg = "replace(" + smsmsg + ",'[VEHFOLLOWUPID]',vehfollowup_id)";

		try {
			StrSql = "SELECT"
					+ " branch_id ,"
					+ " " + sms_contact_id + ","
					+ " '" + sms_contact_name + "',"
					+ " '" + sms_to + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '" + emp_id + "',"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE vehfollowup_id =" + vehfollowup_id;

			// SOP("StrSql--------SMS--" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_emp_id,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOPInfo("StrSql--------SMS--" + StrSql);
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			// //SOP("com9ing....sms done..");
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			// //SOP("coming..13.");
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
