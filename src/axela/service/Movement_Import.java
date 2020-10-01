package axela.service;
//aJIt 2nd June 2014

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Movement_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0";
	public String so_id = "0";
	public String emp_role_id = "0";
	public String veh_entry_id = "0";
	public String veh_entry_date = "";
	public String veh_id = "0";
	public String option_id = "0";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_id = "0";
	public String customer_id = "0";
	public String contact_fname = "", contact_lname = "";
	public String contact_title = "";
	public String contact_title_id = "0";
	public String contact_mobile = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_phone1 = "";
	public String contact_email2 = "";
	public String contact_dob = "", contact_anniversery = "";
	public String contact_address = "", contact_city = "", contact_city_id = "0";
	public String contact_pin = "", contact_state = "", contact_state_id = "0";
	public String model_name = "", model_id = "0";
	public String item_name = "", item_id = "0";
	public String veh_modelyear = "";
	public String veh_reg_no = "";
	public String veh_chassis_no = "";
	public String veh_engine_no = "";
	public String veh_sale_date = "";
	public String veh_kms = "";
	public String interior = "", exterior = "";
	public String vehstock_id = "0";
	public int count = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_service_vehicle_edit", request, response);
				veh_entry_id = CNumeric(GetSession("emp_id", request));
				veh_entry_date = ToLongDate(kknow());
				Addfile(request, response);
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			StrSql = "SELECT COALESCE(BillToName, '') AS BillToName,"
					+ " COALESCE(FirsName, '') AS FirsName,"
					+ " COALESCE(LastName, '') AS LastName,"
					+ " COALESCE(MobilePhoneNumber, '') AS MobilePhoneNumber,"
					+ " COALESCE(EmailID, '') AS EmailID,"
					+ " COALESCE(AlternateEmail, '') AS AlternateEmail,"
					+ " COALESCE(LandlinePhoneNumber, '') AS LandlinePhoneNumber,"
					+ " COALESCE(RegisteredAddress, '') AS RegisteredAddress,"
					+ " COALESCE(CITY, '') AS CITY,"
					+ " COALESCE(PinCode, '') AS PinCode,"
					+ " COALESCE(Model, '') AS Model,"
					+ " COALESCE(Varient, '') AS Varient,"
					+ " COALESCE(ColourExterior, '') AS ColourExterior,"
					+ " COALESCE(ColourInterior, '') AS ColourInterior,"
					+ " COALESCE(Chassis, '') AS Chassis,"
					+ " COALESCE(EngineNumber, '') AS EngineNumber,"
					+ " COALESCE(VehicleRegistrationNumber, '') AS VehicleRegistrationNumber,"
					+ " COALESCE(OdometerReading, '') AS OdometerReading"
					+ " FROM vehicle"
					+ " WHERE Vehicle_Active = '0'"
					// + " ORDER BY vehicle_id DESC"
					+ " LIMIT 10";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					customer_name = crs.getString("BillToName");
					contact_name = crs.getString("FirsName");
					contact_lname = crs.getString("LastName");
					contact_mobile = crs.getString("MobilePhoneNumber");
					contact_email1 = crs.getString("EmailID");
					contact_email2 = crs.getString("AlternateEmail");
					contact_phone1 = crs.getString("LandlinePhoneNumber");
					contact_address = crs.getString("RegisteredAddress");
					contact_city = crs.getString("CITY");
					contact_pin = crs.getString("PinCode");
					model_name = crs.getString("Model");
					veh_chassis_no = crs.getString("Chassis");
					SOP("customer_name==" + customer_name);
					veh_engine_no = crs.getString("EngineNumber");
					veh_reg_no = crs.getString("VehicleRegistrationNumber");
					veh_kms = crs.getString("OdometerReading");
					item_name = model_name + " " + crs.getString("Varient");
					interior = crs.getString("ColourInterior");
					exterior = crs.getString("ColourExterior");

					so_id = "0";
					veh_sale_date = "";
					contact_title = "";
					veh_modelyear = "";
					contact_id = "0";
					customer_id = "0";
					model_id = "0";
					item_id = "0";
					veh_id = "0";
					contact_fname = "";
					contact_mobile1 = "";
					contact_mobile2 = "";

					vehstock_id = "0";

					SOP("veh_chassis_no==" + veh_chassis_no);
					if (!veh_chassis_no.equals("")) {
						StrSql = "SELECT COALESCE(so_customer_id, 0) AS so_customer_id,"
								+ " COALESCE(so_contact_id, 0) AS so_contact_id,"
								+ " COALESCE(so_id, 0) AS so_id,"
								+ " COALESCE(vehstock_id, 0) AS vehstock_id,"
								+ " COALESCE(stock_item_id, 0) AS vehstock_item_id,"
								+ " COALESCE(stock_modelyear, '') AS vehstock_modelyear"
								+ " FROM " + compdb(comp_id) + "axela_vehstock"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
								+ " WHERE vehstock_chassis_no = '" + veh_chassis_no + "'";
						CachedRowSet crs1 = processQuery(StrSql, 0);
						if (crs1.isBeforeFirst()) {
							while (crs1.next()) {
								so_id = crs1.getString("so_id");
								vehstock_id = crs1.getString("vehstock_id");
								customer_id = crs1.getString("so_customer_id");
								contact_id = crs1.getString("so_contact_id");
								item_id = crs1.getString("vehstock_item_id");
								veh_modelyear = crs1.getString("vehstock_modelyear");
							}
						}
						crs1.close();
						// SOP("contact_name==" + contact_name);
						if (contact_id.equals("0")) {
							if (contact_name.split(" ").length > 1) {
								contact_title = contact_name.split(" ")[0];
								contact_fname = contact_name.split(" ")[1];
							} else if (!contact_name.contains(" ")) {
								contact_title_id = "1";
								contact_fname = contact_name;
							}
							if (contact_mobile.split("/").length > 1) {
								contact_mobile1 = contact_mobile.split("/")[0];
								contact_mobile2 = contact_mobile.split("/")[1];
							} else if (contact_mobile.split("/").length == 1) {
								contact_mobile1 = contact_mobile.split("/")[0];
							}
						}

						if (!contact_city.equals("0")) {
							StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
									+ " WHERE city_name = '" + contact_city + "'";
							contact_city_id = ExecuteQuery(StrSql);
							if (CNumeric(contact_city_id).equals("0")) {
								contact_city_id = "2";
							}
						}

						if (!model_name.equals("")) {
							StrSql = "SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
									+ " WHERE model_name = '" + model_name + "'";
							model_id = CNumeric(ExecuteQuery(StrSql));
						}

						if (!item_name.equals("") && item_id.equals("0")) {
							StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_name = '" + item_name + "'";
							item_id = CNumeric(ExecuteQuery(StrSql));
						}

						SOP("item_id==" + item_id);
						if (veh_kms.contains("kms")) {
							veh_kms = veh_kms.split("kms")[0];
						} else if (veh_kms.contains("km")) {
							veh_kms = veh_kms.split("km")[0];
						} else if (veh_kms.contains("KM")) {
							veh_kms = veh_kms.split("KM")[0];
						} else if (CNumeric(veh_kms).equals("0")) {
							veh_kms = "0";
						}

						// if (!CNumeric(model_id).equals("0") && !CNumeric(item_id).equals("0")) {

						if (contact_id.equals("0")) {
							if (!contact_mobile1.equals("")) {
								StrSql = "SELECT contact_id, contact_customer_id FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " WHERE contact_mobile1 = '" + contact_mobile1 + "'";
								CachedRowSet crs2 = processQuery(StrSql, 0);
								while (crs2.next()) {
									contact_id = crs2.getString("contact_id");
									customer_id = crs2.getString("contact_customer_id");
								}
								crs2.close();
							}

							if (!contact_mobile2.equals("") && CNumeric(contact_id).equals("0")) {
								StrSql = "SELECT contact_id, contact_customer_id FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " WHERE contact_mobile1 = '" + contact_mobile2 + "'";
								CachedRowSet crs2 = processQuery(StrSql, 0);
								while (crs2.next()) {
									contact_id = crs2.getString("contact_id");
									customer_id = crs2.getString("contact_customer_id");
								}
								crs2.close();
							}

							if (customer_id.equals("0")) {
								StrSql = "SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_name = '" + customer_name + "'";
								customer_id = ExecuteQuery(StrSql);

								if (CNumeric(customer_id).equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
											+ " (customer_branch_id,"
											+ " customer_name,"
											+ " customer_mobile1,"
											+ " customer_mobile2,"
											+ " customer_email1,"
											+ " customer_email2,"
											+ " customer_address,"
											+ " customer_city_id,"
											+ " customer_pin,"
											+ " customer_since,"
											+ " customer_active,"
											+ " customer_notes,"
											+ " customer_entry_id,"
											+ " customer_entry_date)"
											+ " VALUES"
											+ " ('5',"
											+ " '" + customer_name + "',"
											+ " '" + contact_mobile1 + "',"
											+ " '" + contact_mobile2 + "',"
											+ " '" + contact_email1 + "',"
											+ " '" + contact_email2 + "',"
											+ " '" + contact_address + "',"
											+ " " + contact_city_id + ","
											+ " '" + contact_pin + "',"
											+ " '" + ToShortDate(kknow()) + "',"
											+ " '1',"
											+ " '',"
											+ " " + veh_entry_id + ","
											+ " '" + veh_entry_date + "')";

									customer_id = UpdateQueryReturnID(StrSql);
								}
							}

							if (CNumeric(contact_id).equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
										+ " (contact_customer_id,"
										+ " contact_contacttype_id,"
										+ " contact_title_id,"
										+ " contact_fname,"
										+ " contact_lname,"
										+ " contact_mobile1,"
										+ " contact_mobile2,"
										+ " contact_email1,"
										+ " contact_email2,"
										+ " contact_address,"
										+ " contact_city_id,"
										+ " contact_pin,"
										+ " contact_notes,"
										+ " contact_active,"
										+ " contact_entry_id,"
										+ " contact_entry_date)"
										+ " VALUES"
										+ " (" + customer_id + ","
										+ " 1,"
										+ " " + contact_title_id + ","
										+ " '" + contact_fname + "',"
										+ " '" + contact_lname + "',"
										+ " '" + contact_mobile1 + "',"
										+ " '" + contact_mobile2 + "',"
										+ " '" + contact_email1 + "',"
										+ " '" + contact_email2 + "',"
										+ " '" + contact_address + "',"
										+ " " + contact_city_id + ","
										+ " '" + contact_pin + "',"
										+ " '',"
										+ " '1',"
										+ " " + veh_entry_id + ","
										+ " '" + veh_entry_date + "')";

								contact_id = UpdateQueryReturnID(StrSql);
							}
						}
						SOP("contact_id==" + contact_id + " customer_id==" + customer_id);

						if (!CNumeric(contact_id).equals("0") && !CNumeric(customer_id).equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
									+ " (veh_customer_id,"
									+ " veh_contact_id,"
									+ " veh_so_id,"
									+ " veh_variant_id,"
									+ " veh_modelyear,"
									+ " veh_chassis_no,"
									+ " veh_engine_no,"
									+ " veh_reg_no,"
									+ " veh_sale_date,"
									+ " veh_kms,"
									+ " veh_notes,"
									+ " veh_entry_id,"
									+ " veh_entry_date)"
									+ " VALUES"
									+ " ('" + customer_id + "',"
									+ " '" + contact_id + "',"
									+ " '" + so_id + "',"
									+ " '" + CNumeric(item_id) + "',"
									+ " '" + veh_modelyear + "',"
									+ " '" + veh_chassis_no + "',"
									+ " '" + veh_engine_no + "',"
									+ " '" + veh_reg_no + "',"
									+ " '" + veh_sale_date + "',"
									+ " '" + veh_kms + "',"
									+ " '',"
									+ " '" + veh_entry_id + "',"
									+ " '" + veh_entry_date + "')";
							veh_id = UpdateQueryReturnID(StrSql);

							if (!veh_id.equals("0") && !vehstock_id.equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
										+ " (vehtrans_option_id,"
										+ " vehtrans_veh_id)"
										+ " SELECT option_id,"
										+ " " + veh_id + ""
										+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
							} else if (!veh_id.equals("0")) {
								option_id = "0";
								// StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
								// + " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
								// + " WHERE option_name = '" + interior + "'";
								// option_id = ExecuteQuery(StrSql);

								if (!CNumeric(option_id).equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
											+ " (vehtrans_option_id,"
											+ " vehtrans_veh_id)"
											+ " VALUES"
											+ " ('" + option_id + "',"
											+ " '" + veh_id + "')";
									// SOP("StrSql=interior=" + StrSql);
									updateQuery(StrSql);
								}

								SOP("interior==" + interior + " exterior==" + exterior);
								if (!interior.equals(exterior)) {
									option_id = "0";
									// StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									// + " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
									// + " WHERE option_name = '" + exterior + "'";
									// option_id = ExecuteQuery(StrSql);

									if (!CNumeric(option_id).equals("0")) {
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
												+ " (vehtrans_option_id,"
												+ " vehtrans_veh_id)"
												+ " VALUES"
												+ " ('" + option_id + "',"
												+ " '" + veh_id + "')";
										// SOP("StrSql=exterior=" + StrSql);
										updateQuery(StrSql);
									}
								}
							}

							StrSql = "UPDATE vehicle"
									+ " SET Vehicle_Active = '1'"
									+ " WHERE Chassis = '" + veh_chassis_no + "'";
							updateQuery(StrSql);
						} else {
							StrSql = "UPDATE vehicle"
									+ " SET Vehicle_Active = '2'"
									+ " WHERE Chassis = '" + veh_chassis_no + "'";
							updateQuery(StrSql);
						}
					} else {
						StrSql = "UPDATE vehicle"
								+ " SET Vehicle_Active = '2'"
								+ " WHERE VehicleRegistrationNumber = '" + veh_reg_no + "'";
						updateQuery(StrSql);
					}
					SOP("count==" + count);
				}
				msg = count + " Vehicles imported successfully!";
				Thread.sleep(5000);
				Addfile(request, response);
			}
			crs.close();
		} catch (Exception fe) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
		}
	}
}
