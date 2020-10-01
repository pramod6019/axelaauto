package axela.service;
//aJIt 16th July 2013

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

public class JobCard_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", jc_service = "", jc_grandtotal = "0.0", month = "", day = "", year = "";
	public String emp_role_id = "0";
	public String prime_id = "0";

	// customer fields
	public String customer_id = "0", customer_name = "", customer_mobile1 = "", customer_phone1 = "", customer_email1 = "";
	public String customer_address = "", customer_city_id = "0", customer_pin = "", customer_emp_id = "0";

	// customer contact fields
	public String contact_id = "0", contact_title = "", contact_fname = "", contact_lname = "", contact_dob = "", contact_anniversary = "";
	public String contact_mobile1 = "";

	// Jobcard fields
	public String jc_id = "0", jc_branch_id = "0", jc_no = "", jc_time_in = "", jc_customer_id = "0", jc_bill_date = "";
	public String jc_contact_id = "0", jc_veh_id = "0", jc_reg_no = "", jc_jctype_id = "0";
	public String jc_jccat_id = "0", jc_kms = "", jc_emp_id = "0", jc_technician_emp_id = "0", jc_ro_no = "";
	public String jc_bill_cash_no = "", jc_jcstage_id = "0", jc_priorityjc_id = "0", jc_location_id = "0";
	public String jc_bill_cash_customername = "", jc_bill_cash_date = "", jc_open = "", jc_critical = "";
	public String jc_auth = "", jc_active = "", jc_notes = "", jc_entry_id = "0", jc_entry_date = "";

	// Vehicle fields
	public String veh_id = "0", veh_chassis_no = "", veh_engine_no = "", veh_sale_date = "", veh_variant_id = "0";

	public String city_name = "", state_name = "";
	// class variables
	public String modelName = "", fuelType = "", categoryName = "", serviceAdvisor = "";
	public String technician = "", carUserName = "", PreferedDayofFollowupTime = "", serviceType = "";
	public String model_name = "", preownedmodel_id = "0";
	public String variant_name = "", variant_id = "0";
	public String jcbill_date = "", sale_date = "", bill_date = "", contact_name = "";
	public int count = 0, connCount = 0;
	public String recursion = "";
	public String vehid = "0";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			jc_entry_id = CNumeric(session.getAttribute("emp_id") + "");
			jc_entry_date = ToLongDate(kknow());
			if (ReturnPerm(comp_id, "emp_service_vehicle_edit", request).equals("1")) {
				AddJobCardFields(request, response);
			}
			msg = count + " Job Card imported successfully!";
			SOP("Total Conn count==" + connCount);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void AddJobCardFields(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			connCount++;
			SOP("Conn count============" + connCount);
			StrSql = "SELECT Branch, FollowupType, FollowupNo, FollowupDueDate, FollowupActualDate, JobCardNo,"
					+ " JobCardOpenDate, BillNumber, RegNum, ChassisNo, ModelName, FuelType, Mileage, SaleDate,"
					+ " CustomerCategory, ServiceAdvisor, Technician, CustomerName, Address, TelephoneNo, MobileNo,"
					+ " EmailID, CarUserName, PreferedDayofFollowupTime, DateofBirth, DateOfAnniversary, ServiceType,"
					+ " Engine, Imp_Status"
					+ " FROM " + compdb(comp_id) + "jobcard_imp"
					+ " WHERE Imp_Status = 0"
					+ " LIMIT 20";
			// SOP("StrSql==select==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst()) {
				recursion = "yes";
				while (crs.next()) {
					// Re-intialize all variables
					customer_id = "0";
					customer_city_id = "0";
					customer_emp_id = "0";
					contact_id = "0";
					jc_id = "0";
					jc_branch_id = "0";
					jc_customer_id = "0";
					jc_contact_id = "0";
					jc_veh_id = "0";
					jc_jctype_id = "0";
					jc_jccat_id = "0";
					jc_emp_id = "0";
					jc_technician_emp_id = "0";
					jc_ro_no = "";
					jc_jcstage_id = "0";
					jc_priorityjc_id = "0";
					jc_location_id = "0";
					veh_id = "0";
					vehid = "0";
					veh_variant_id = "0";
					preownedmodel_id = "0";
					variant_id = "0";
					jc_id = "0";
					jc_branch_id = crs.getString("branch");
					jc_no = crs.getString("JobCardNo");
					jc_ro_no = crs.getString("JobCardNo");
					jc_time_in = crs.getString("JobCardOpenDate");
					bill_date = crs.getString("JobCardOpenDate");
					jc_bill_cash_no = crs.getString("BillNumber");
					jc_reg_no = crs.getString("RegNum");
					veh_chassis_no = crs.getString("ChassisNo");
					variant_name = PadQuotes(crs.getString("ModelName"));
					fuelType = PadQuotes(crs.getString("FuelType"));
					jc_kms = crs.getString("Mileage");
					veh_sale_date = crs.getString("SaleDate").replaceAll("-", "/");
					categoryName = PadQuotes(crs.getString("CustomerCategory"));
					serviceAdvisor = PadQuotes(crs.getString("ServiceAdvisor"));
					technician = PadQuotes(crs.getString("Technician"));
					customer_name = PadQuotes(crs.getString("CustomerName"));
					customer_address = PadQuotes(crs.getString("Address"));
					customer_phone1 = crs.getString("TelephoneNo");
					customer_mobile1 = crs.getString("MobileNo");
					customer_email1 = crs.getString("EmailID");
					carUserName = PadQuotes(crs.getString("CarUserName"));
					PreferedDayofFollowupTime = crs.getString("PreferedDayofFollowupTime");
					contact_dob = crs.getString("DateofBirth");
					contact_anniversary = crs.getString("DateOfAnniversary");
					serviceType = PadQuotes(crs.getString("ServiceType"));
					veh_engine_no = CNumeric(crs.getString("Engine"));

					prime_id = crs.getString("JobCardNo");
					if (!jc_time_in.equals("")) {
						if (isValidDateFormatLong(jc_time_in)) {
							jc_time_in = ConvertLongDateToStr(jc_time_in);
							// SOP("jc_time_in==" + jc_time_in);
						}
					}

					StrSql = "SELECT city_id, city_name, state_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE branch_id = " + jc_branch_id;
					// SOP("StrSql==city===" + StrSql);
					CachedRowSet crs1 = processQuery(StrSql);
					while (crs1.next()) {
						customer_city_id = crs1.getString("city_id");
						city_name = crs1.getString("city_name");
						state_name = crs1.getString("state_name");
					}

					crs1.close();
					if (!customer_name.equals("")) {
						contact_name = customer_name;
						if (!contact_name.equals("")) {
							contact_title = "0";
							if (contact_name.contains(" ")) {
								contact_title = contact_name.split(" ")[0];
								StrSql = "SELECT title_id"
										+ " FROM " + compdb(comp_id) + "axela_title"
										+ " WHERE title_desc LIKE '%" + contact_title + "%'";
								contact_title = CNumeric(ExecuteQuery(StrSql));
								if (contact_title.equals("0")) {
									contact_title = "1";
									contact_fname = contact_name;
								} else {
									if (contact_name.split(" ").length > 1) {
										contact_fname = contact_name;
									}
								}
							} else {
								contact_title = "1";
								contact_fname = contact_name;
							}
							contact_name = contact_fname;
						}
					}

					if (!customer_mobile1.equals("") && !customer_mobile1.equals("0")) {

						if (customer_mobile1.contains(",")) {
							customer_mobile1 = customer_mobile1.split(",")[0];
						} else if (customer_mobile1.substring(0, 3).equals("+91")) {
							customer_mobile1 = customer_mobile1.replace("+91", "");
						} else if (customer_mobile1.substring(0, 2).equals("91") && customer_mobile1.length() > 10) {
							customer_mobile1 = customer_mobile1.replaceFirst("91", "");
						}
						else if (customer_mobile1.substring(0, 1).equals("0") && customer_mobile1.length() > 10) {
							customer_mobile1 = customer_mobile1.replaceFirst("0", "");
						}
						customer_mobile1 = customer_mobile1.replaceAll("[^0-9]+", "");
						if (!customer_mobile1.contains("91-")) {
							customer_mobile1 = "91-" + customer_mobile1;
						}
						if (!IsValidMobileNo11(contact_mobile1)) {
							contact_mobile1 = "";
						}
					}
					SOP("customer_mobile1==" + customer_mobile1);

					if (!customer_phone1.equals("") && !customer_phone1.equals("0")) {
						customer_phone1 = customer_phone1.replaceAll("[^-0-9]+", "");
						if (!customer_phone1.contains("91-")) {
							customer_phone1 = "91-" + customer_phone1;
						}
						if (!IsValidPhoneNo11(customer_phone1)) {
							customer_phone1 += "";
						}
					}

					if (!IsValidEmail(customer_email1)) {
						customer_email1 = "";
					}

					// contact_dob valdation
					if (!contact_dob.equals("")) {
						if (isValidDateFormatShort(contact_dob)) {
							contact_dob = ConvertShortDateToStr(contact_dob);
						} else if (contact_dob.split("/").length == 3) {
							month = contact_dob.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = contact_dob.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = contact_dob.split("/")[2];
							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								contact_dob = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							} else {
								contact_dob += "";
							}
							day = "";
							month = "";
							year = "";
						} else {
							contact_dob += "";
						}
					}

					// contact_anniversary valdation
					if (!contact_anniversary.equals("")) {
						if (isValidDateFormatShort(contact_anniversary)) {
							contact_anniversary = ConvertShortDateToStr(contact_anniversary);
						} else if (contact_anniversary.split("/").length == 3) {
							month = contact_anniversary.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = contact_anniversary.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = contact_anniversary.split("/")[2];
							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								contact_anniversary = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							} else {
								contact_anniversary += "";
							}
							day = "";
							month = "";
							year = "";
						} else {
							contact_anniversary += "";
						}
					}

					if (!bill_date.equals("") && !bill_date.equals("0")) {
						if (bill_date.split("/").length == 3)
							month = bill_date.split("/")[0];
						if (month.length() == 1) {
							month = "0" + month;
						}
						day = bill_date.split("/")[1];
						if (day.length() == 1) {
							day = "0" + day;
						}
						year = bill_date.split("/")[2];

						jcbill_date = day + "/" + month + "/" + year;

						jc_bill_date = ConvertShortDateToStr(jcbill_date);
					}

					if (!veh_sale_date.equals("")) {
						if (isValidDateFormatShort(veh_sale_date)) {
							veh_sale_date = ConvertShortDateToStr(veh_sale_date);
							// SOP("veh_sale_date==" + veh_sale_date);
						}
					}
					preownedmodel_id = "0";
					variant_id = "0";
					veh_id = "0";
					vehid = "0";
					if (!veh_chassis_no.equals("") && !jc_reg_no.equals("")) {
						StrSql = "SELECT veh_id"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE veh_reg_no = '" + jc_reg_no + "'";
						crs1 = processQuery(StrSql);

						while (crs1.next()) {
							vehid = crs1.getString("veh_id");
						}
						crs1.close();
						if (!vehid.equals("0")) {
							StrSql = "SELECT veh_id, customer_id,"
									+ " COALESCE(contact_id, 0) AS contact_id,"
									+ " COALESCE(city_name, 0) AS city_name,"
									+ " COALESCE(customer_pin, 0) AS customer_pin,"
									+ " COALESCE(state_name, 0) AS state_name,"
									+ " customer_address,"
									+ " veh_variant_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
									+ "	WHERE 1=1";
							if (!veh_chassis_no.equals("") && !veh_engine_no.equals("0") && !veh_engine_no.equals("")) {
								StrSql += " AND CONCAT(veh_chassis_no, '-', veh_engine_no) = '" + veh_chassis_no + "-" + veh_engine_no + "'";
							}
							StrSql += " AND veh_id = " + vehid;
							SOP("StrSql===vehicle details==" + StrSql);
							crs1 = processQuery(StrSql);
							if (crs1.isBeforeFirst()) {
								while (crs1.next()) {
									veh_id = crs1.getString("veh_id");
									veh_variant_id = crs1.getString("veh_variant_id");
									customer_id = crs1.getString("customer_id");
									contact_id = crs1.getString("contact_id");
									customer_address = crs1.getString("customer_address");
									// customer_address = customer_address.replace("'", " ");
									city_name = crs1.getString("city_name");
									customer_pin = crs1.getString("customer_pin");
									state_name = crs1.getString("state_name");
									// SOP("customer_id==111==" + customer_id);
									// SOP("contact_id==111==" + contact_id);
								}
							} else {
								veh_id = vehid;
							}
							crs1.close();
						}

						if (customer_id.equals("0") && !customer_mobile1.equals("")) {
							StrSql = "SELECT customer_id"
									+ " FROM " + compdb(comp_id) + "axela_customer"
									+ " WHERE (customer_mobile1 = '" + customer_mobile1 + "'"
									+ " OR customer_mobile2 = '" + customer_mobile1 + "')";
							// SOP("check mobile1==" + StrSql);
							customer_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("customer_id==222==" + customer_id);
						}
						if (!customer_id.equals("0")) {
							// Check Service type contact.
							StrSql = "SELECT contact_id"
									+ " FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
									+ " WHERE contact_contacttype_id = 9"
									+ " AND customer_id = " + customer_id;
							// SOP("check Service type==" + StrSql);
							contact_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("contact_id==222==" + contact_id);
						}

						if (!categoryName.equals("")) {
							StrSql = "SELECT jccat_id FROM " + compdb(comp_id) + "axela_service_jc_cat"
									+ " WHERE jccat_name LIKE '%" + categoryName + "%'";
							jc_jccat_id = CNumeric(ExecuteQuery(StrSql));
							if (jc_jccat_id.equals("0")) {
								jc_jccat_id = "3";
							}
						}

						if (!serviceType.equals("")) {
							StrSql = "SELECT jctype_id FROM " + compdb(comp_id) + "axela_service_jc_type"
									+ " WHERE jctype_name LIKE '%" + serviceType + "%'";
							jc_jctype_id = CNumeric(ExecuteQuery(StrSql));
						}

						if (!serviceAdvisor.equals("")) {
							StrSql = "SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_name = '" + serviceAdvisor + "'"
									+ " AND emp_service = 1";
							jc_emp_id = CNumeric(ExecuteQuery(StrSql));
						}

						if (!technician.equals("")) {
							technician = technician.replace("(", "&#40;").replace(")", "&#41;");
							StrSql = "SELECT emp_id"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_name = '" + technician + "'"
									+ " AND emp_technician = 1"
									+ " LIMIT 1";
							jc_technician_emp_id = CNumeric(ExecuteQuery(StrSql));

						}

						if (!variant_name.equals("")) {
							if (variant_name.contains("(")) {
								variant_name = variant_name.replace("(", "&#40;");
							}
							if (variant_name.contains(")")) {
								variant_name = variant_name.replace(")", "&#41;");
							}

							StrSql = "SELECT variant_id, variant_preownedmodel_id"
									+ " FROM axelaauto.axela_preowned_variant"
									+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
									+ " WHERE variant_service_code = '" + variant_name + "'"
									// + "	OR variant_code = '" + variant_name + "'"
									+ " OR variant_name = '" + variant_name + "'"
									// + "	AND variant_type_id = 1"
									+ " LIMIT 1";
							// SOP("StrSql==variant==111==" + StrSql);
							crs1 = processQuery(StrSql, 0);
							while (crs1.next()) {
								variant_id = CNumeric(crs1.getString("variant_id"));
								preownedmodel_id = CNumeric(crs1.getString("variant_preownedmodel_id"));
							}
							crs1.close();
						}
						if (!veh_id.equals("0")) {
							StrSql = "SELECT jc_id"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " WHERE jc_ro_no = '" + jc_ro_no + "'"
									+ " AND jc_branch_id = " + jc_branch_id;
							SOP("StrSql==jc_id==" + StrSql);
							jc_id = CNumeric(ExecuteQuery(StrSql));
						}

						if (veh_id.equals("0")) {
							// Add Vehicle
							if (customer_id.equals("0")) {
								customer_id = AddCustomer();
								// SOP("customer_id==333==" + customer_id);
							}
							if (contact_id.equals("0")) {
								contact_id = AddContact();
								// SOP("contact_id==333==" + contact_id);
							}
							if (!customer_id.equals("0") && !contact_id.equals("0")) {
								AddVehicle();
							}
						}
						SOP("veh_id==" + veh_id);
						SOP("jc_id==" + jc_id);
						SOP("customer_id==" + customer_id);
						SOP("contact_id==" + contact_id);

						if (!CNumeric(veh_id).equals("0") && jc_id.equals("0") && !customer_id.equals("0") && !contact_id.equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc"
									+ " (jc_branch_id,"
									+ " jc_time_in,"
									+ " jc_customer_id,"
									+ " jc_contact_id,"
									+ " jc_veh_id,"
									+ " jc_reg_no,"
									+ " jc_jctype_id,"
									+ " jc_jccat_id,"
									+ " jc_kms,"
									+ " jc_bill_address,"
									+ " jc_bill_pin,"
									+ " jc_bill_state,"
									+ " jc_del_address,"
									+ " jc_del_city,"
									+ " jc_del_pin,"
									+ " jc_del_state,"
									+ " jc_title,"
									+ " jc_bill_cash_customername,"
									+ " jc_grandtotal,"
									+ " jc_bill_cash_date,"
									+ " jc_cust_voice,"
									+ " jc_advice,"
									+ " jc_instructions,"
									+ " jc_terms,"
									+ " jc_inventory,"
									+ " jc_notes,"
									+ " jc_active,"
									+ " jc_emp_id,"
									+ " jc_technician_emp_id,"
									+ " jc_ro_no,"
									+ " jc_time_promised,"
									+ " jc_time_ready,"
									+ " jc_time_out,"
									+ " jc_jcstage_id,"
									+ " jc_entry_id,"
									+ " jc_entry_date)"
									+ " VALUES"
									+ " (" + jc_branch_id + "," // jc_branch_id
									+ " '" + jc_time_in + "',"
									+ " " + customer_id + ","
									+ " " + contact_id + ","
									+ " " + veh_id + ","
									+ " '" + jc_reg_no + "',"
									+ " " + jc_jctype_id + ","
									+ " " + jc_jccat_id + ","
									+ " " + jc_kms + "," // jc_kms
									+ " '" + customer_address + "'," // jc_bill_address
									+ " '" + customer_pin + "'," // jc_bill_pin
									+ " '" + state_name + "'," // jc_bill_state
									+ " '" + customer_address + "'," // jc_del_address
									+ " '" + city_name + "'," // jc_del_city
									+ " '" + customer_pin + "'," // jc_del_pin
									+ " '" + state_name + "'," // jc_del_state
									+ " 'Job Card',"
									+ " '" + customer_name + "'," // jc_customername
									+ " " + jc_grandtotal + "," // jc_grandtotal
									+ " '" + jc_bill_date + "'," // jc_bill_date
									+ " ''," // jc_cust_voice
									+ " ''," // jc_advice
									+ " ''," // jc_instructions
									+ " ''," // jc_terms
									+ " ''," // jc_inventory
									+ " ''," // jc_notes
									+ " '1'," // jc_active
									+ " " + jc_emp_id + ","
									+ " " + jc_technician_emp_id + ","
									+ " '" + jc_ro_no + "',"
									+ " '" + jc_time_in + "'," // jc_time_promised
									+ " '" + jc_time_in + "'," // jc_time_ready
									+ " '" + jc_time_in + "'," // jc_time_out
									+ " 6," // jc_jcstage_id
									+ " " + jc_entry_id + ","
									+ " '" + jc_entry_date + "')";
							// SOP("StrSql==INSERT axela_service_jc==" + StrSqlBreaker(StrSql));
							stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
							ResultSet rs = stmttx.getGeneratedKeys();

							while (rs.next()) {
								jc_id = CNumeric(rs.getString(1));
							}
							rs.close();
							if (!jc_id.equals("0")) {
								count++;
								SOP("Row updated=========" + count);
								StrSql = "UPDATE " + compdb(comp_id) + "jobcard_imp"
										+ " SET"
										+ " Imp_Status = 1" // Jobcard inserted
										+ " WHERE JobCardNo = '" + prime_id + "'";
								stmttx.execute(StrSql);
							}
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "jobcard_imp"
									+ " SET"
									+ " Imp_Status = 2" // Jobcard already present
									+ " WHERE JobCardNo = '" + prime_id + "'";
							stmttx.execute(StrSql);
						}
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "jobcard_imp"
								+ " SET"
								+ " Imp_Status = 3" // Chassis No. not present
								+ " WHERE JobCardNo = '" + prime_id + "'";
						stmttx.execute(StrSql);
					}
					conntx.commit();
				}
			} else {
				recursion = "no";
			}
			crs.close();
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
			Thread.sleep(1000);
			if (recursion.equals("yes")) {
				AddJobCardFields(request, response);
			}

		} catch (Exception e) {
			// This logic is to recall fucntionality even if exception errors while importing..
			if (recursion.equals("yes")) {
				StrSql = "UPDATE " + compdb(comp_id) + "jobcard_imp"
						+ " SET"
						+ " Imp_Status = 4" // Exception occured due to In-valid data
						+ " WHERE JobCardNo = '" + prime_id + "'";
				SOP("Exception occured due to In-valid data===========");
				updateQuery(StrSql);
				try {
					Thread.sleep(1000);
					if (recursion.equals("yes")) {
						AddJobCardFields(request, response);
					}
				} catch (InterruptedException ie) {
					SOPError("InterruptedException==");
				}
			}

			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				SOP("Transaction Error==");
				conntx.rollback();
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
			msg = "<br>Transaction Error!";
		} finally {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
		}
	}
	public String AddCustomer() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_phone1,"
				+ " customer_email1,"
				+ " customer_address,"
				+ " customer_city_id,"
				+ " customer_pin,"
				+ "	customer_emp_id,"
				+ "	customer_type,"
				+ "	customer_accgroup_id,"
				// + " customer_soe_id,"
				// + " customer_sob_id,"
				+ " customer_since,"
				+ " customer_active,"
				+ " customer_notes,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + jc_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + customer_mobile1 + "',"
				+ " '" + customer_phone1 + "',"
				+ " '" + customer_email1 + "',"
				+ " '" + customer_address + "',"
				+ " " + customer_city_id + ","
				+ " '" + customer_pin + "',"
				+ " " + jc_entry_id + "," // customer_emp_id
				+ "	'1'," // customer_type
				+ " 32," // customer_accgroup_id
				// + " " + soe_id + ","
				// + " " + sob_id + ","
				+ " '" + ToShortDate(kknow()) + "',"
				+ " '1',"
				+ " '',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql==cust==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			customer_id = rs.getString(1);
		}
		rs.close();
		return customer_id;
	}

	public String AddContact() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_mobile1_phonetype_id,"
				+ " contact_phone1,"
				+ " contact_email1,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_notes,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + customer_id + ","
				+ " 9,"
				+ " " + 1 + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + customer_mobile1 + "',"
				+ " '3'," // Service contact_mobile1_phonetype_id
				+ " '" + customer_phone1 + "',"
				+ " '" + customer_email1 + "',"
				+ " '" + customer_address + "',"
				+ " " + customer_city_id + ","
				+ " '" + customer_pin + "',"
				+ " '',"
				+ " '1',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql==contact=" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			contact_id = rs.getString(1);
		}
		rs.close();
		return contact_id;
	}

	public void AddVehicle() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
				+ " (veh_branch_id,"
				+ " veh_customer_id,"
				+ " veh_contact_id,"
				// + " veh_so_id,"
				+ " veh_variant_id,"
				// + " veh_modelyear,"
				+ " veh_chassis_no,"
				+ " veh_engine_no,"
				+ " veh_reg_no,"
				+ " veh_sale_date,"
				+ " veh_emp_id,"
				+ " veh_kms,"
				// + " veh_lastservice,"
				+ " veh_crmemp_id,"
				+ "	veh_notes,"
				+ " veh_entry_id,"
				+ " veh_entry_date)"
				+ " VALUES"
				+ " (" + jc_branch_id + ","
				+ " " + customer_id + ","
				+ " " + contact_id + ","
				// + " " + so_id + ","
				+ " " + variant_id + ","
				// + " '" + veh_modelyear + "',"
				+ " '" + veh_chassis_no + "',"
				+ " '" + veh_engine_no + "',"
				+ " '" + jc_reg_no + "',"
				+ " '" + veh_sale_date + "',"
				+ " " + jc_emp_id + ","
				+ " " + CNumeric(jc_kms) + ","
				// + " '" + veh_lastservice + "',"
				+ " " + ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 AND emp_crm = 1 and emp_active = 1 "
						+ " ORDER BY RAND() LIMIT 1") + ","
				+ "	'',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql===veh==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_id = rs.getString(1);
		}
		rs.close();
		veh_id = CNumeric(veh_id);

		if (!veh_id.equals("0")) {
			StrSql = "SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1"
					+ " ORDER BY RAND()"
					+ " LIMIT 1";
			String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));

			if (insurpolicy_emp_id.equals("0")) {
				insurpolicy_emp_id = CNumeric(jc_emp_id);
			}

		}

		// SOP("StrSql===veh_id==" + veh_id);
		if (!CNumeric(jc_kms).equals("0") && !veh_id.equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
					+ " (vehkms_veh_id,"
					+ " vehkms_kms,"
					+ " vehkms_entry_id,"
					+ " vehkms_entry_date)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + jc_kms + ","
					+ " " + jc_entry_id + ","
					+ " '" + jc_entry_date + "')";
			stmttx.execute(StrSql);
		}

		// SOP("interior==" + interior + " exterior==" + exterior);
	}
}
