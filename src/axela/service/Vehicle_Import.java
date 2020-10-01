package axela.service;
//shivaprasad 23 Sept 2015     

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

public class Vehicle_Import extends Connect {

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
	public String contact_email1 = "", contact_phone1 = "", contact_phone2 = "";
	public String contact_email2 = "";
	public String contact_dob = "", contact_anniversery = "";
	public String contact_address = "", contact_city = "", contact_city_id = "0";
	public String contact_pin = "", contact_state = "", contact_state_id = "0";
	public String model_name = "", model_id = "0";
	public String item_name = "", item_id = "0", item_service_code = "";
	public String veh_modelyear = "";
	public String veh_reg_no = "";
	public String veh_chassis_no = "";
	public String veh_engine_no = "";
	public String veh_sale_date = "", veh_comm_no = "";
	public String veh_kms = "0", vehcrm_emp_id = "0";
	public String interior = "", exterior = "", veh_lastservice = "", soe_name = "", soe_id = "0", sob_name = "", sob_id = "0", veh_vehsource_id = "0", vehsource_name = "";
	public String vehstock_id = "0", veh_emp_name = "", veh_emp_id = "0", branch_id = "0", veh_ref_no = "0", veh_renewal_date = "";
	public int count = 0;
	public String[] model_name_arr = null;
	public String model_name1 = "", veh_notes = "", day = "", month = "", year = "", imp_insur_date = "", veh_telecaller = "", veh_crm_emp_id = "";
	public String model_name2 = "", branch_name = "", veh_iacs = "", vehkms_id = "0", vehcol_id = "0", veh_insuremp_id = "0", veh_crmemp_id = "0";
	String hrs = "", min = "", veh_lastservice_kms = "", servicedueyear = "", veh_service_duekms = "", veh_service_duedate = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				// CheckPerm(comp_id, "emp_service_vehicle_edit", request,
				// response);
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
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			// All Commented fields should be there use when data is provided...

			StrSql = "SELECT"
					+ " COALESCE(vehcol_id, '') AS vehcol_id,"
					+ " COALESCE(branch, '') AS branch,"
					+ " COALESCE(CustomerName, '') AS CustomerName,"
					+ " COALESCE(Mobile1, '') AS Mobile1,"
					+ " COALESCE(Model, '') AS Model,"
					+ " COALESCE(ChassisNumber, '') AS ChassisNumber,"
					+ " COALESCE(EngineNumber, '') AS EngineNumber,"
					+ " COALESCE(ModelYear, '') AS ModelYear,"
					+ " COALESCE(InsuranceDate, '') AS InsuranceDate,"
					+ " COALESCE(RegistrationNumber, '') AS RegistrationNumber,"
					+ " COALESCE(TELECALLER, '') AS TELECALLER,"
					+ " COALESCE(ID, '') AS ID"
					+ " FROM " + compdb(comp_id) + "vehmpdata"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_service_veh ON veh_reg_no = RegistrationNumber"
					+ " WHERE active = '0'"
					// +
					// " AND RegistrationNumber NOT IN (SELECT veh_reg_no FROM "
					// + compdb(comp_id) + "axela_service_veh)"
					+ " AND RegistrationNumber = ''"
					+ " AND EngineNumber != ''"
					// + " ORDER BY vehicle_id DESC"
					+ " order by vehcol_id"
					+ " LIMIT 100";
			SOP("veh data------------get---------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// branch_id = "";
					vehcol_id = CNumeric(crs.getString("vehcol_id"));
					// SOP("vehcol_id=1==" + vehcol_id);
					contact_mobile1 = PadQuotes(crs.getString("Mobile1"));
					branch_id = PadQuotes(crs.getString("branch"));
					SOP("branch_id------DB-----------" + branch_id);
					customer_name = PadQuotes(crs.getString("CustomerName"));
					model_name = PadQuotes(crs.getString("Model"));
					// SOP("model_name----4---/----------------------" +
					// model_name);
					// item_name = PadQuotes(crs.getString("imp_item"));
					veh_modelyear = CNumeric(PadQuotes(crs.getString("ModelYear")));
					// SOP("veh_modelyear==5--------=" + veh_modelyear);

					veh_renewal_date = PadQuotes(crs.getString("InsuranceDate"));

					// SOP("veh_renewal_date---6----" + veh_renewal_date);

					veh_chassis_no = PadQuotes(crs.getString("ChassisNumber"));
					veh_engine_no = PadQuotes(crs.getString("EngineNumber"));
					veh_reg_no = PadQuotes(crs.getString("RegistrationNumber"));
					// veh_sale_date =
					// PadQuotes(crs.getString("imp_sale_date"));// Check
					// the
					// date
					// format
					veh_telecaller = PadQuotes(crs.getString("TELECALLER"));
					veh_crm_emp_id = PadQuotes(crs.getString("ID"));
					SOP("veh_telecaller----10---" + veh_telecaller);

					if (!veh_renewal_date.equals("")) {
						if (isValidDateFormatShort(veh_renewal_date)) {
							veh_renewal_date = ConvertShortDateToStr(veh_renewal_date);
							veh_modelyear = veh_renewal_date.substring(0, 4);
							// SOP("veh_modelyear------" + veh_modelyear);
						} else if (veh_renewal_date.split("/").length == 3) {
							month = veh_renewal_date.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							// SOP("month---------" + month);
							day = veh_renewal_date.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							// SOP("day---------" + day);
							year = veh_renewal_date.split("/")[2];

							// SOP("year---------" + year);
							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								// SOP("inside daymonthyear");
								veh_renewal_date = year + month + day + "000000";
								veh_modelyear = year;
								// SOP("veh_modelyear=in1==" + veh_modelyear);
							} else {
								veh_renewal_date = "20140101000000";
								veh_modelyear = "2014";
								// SOP("veh_modelyear=in2==" + veh_modelyear);
							}
						} else {
							veh_renewal_date = "20140101000000";
							veh_modelyear = "2014";
							// SOP("veh_modelyear=in3==" + veh_modelyear);
						}
						// SOP("veh_renewal_date========222==" +
						// veh_renewal_date);
					} else {
						veh_renewal_date = "20140101000000";
						veh_modelyear = "2014";

						// SOP("veh_renewal_date=in4==" + veh_renewal_date);
					}

					vehsource_name = "Service Data";
					// To be confirmed with
					// sujay sir ..
					// veh_emp_name =
					// PadQuotes(crs.getString("imp_service_advisor"));
					veh_emp_id = "1";// To be
					// confirmed with sujay sir
					// ..

					vehstock_id = "0";
					veh_id = "0";
					customer_id = "0";
					contact_id = "0";
					item_id = "0";
					model_id = "0";
					contact_city_id = "0";
					// branch_id = "1";

					// SOP("veh_reg_no-------" + veh_reg_no);
					if (veh_reg_no.equals("") && !veh_engine_no.equals("")) {
						StrSql = "SELECT COALESCE(veh_id, 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE veh_engine_no = '" + veh_engine_no + "'";
						SOP("StrSql 2 = " + StrSql);
						veh_id = CNumeric(ExecuteQuery(StrSql));
						if (!veh_id.equals("0")) {
							SOP("veh_id ==already present = " + veh_id);
						}
						// SOP("veh_id------------" + veh_id);

						if (veh_id.equals("0")) {

							String veh_reg_no_check = "";
							String veh_engine_no_check = "";
							String veh_chassis_no_check = "";
							veh_reg_no_check = PadQuotes(ExecuteQuery("SELECT veh_reg_no  FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_reg_no = '" + veh_reg_no + "' "));
							veh_engine_no_check = PadQuotes(ExecuteQuery("SELECT veh_engine_no  FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_engine_no = '" + veh_engine_no + "' "));
							veh_chassis_no_check = PadQuotes(ExecuteQuery("SELECT veh_chassis_no  FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_engine_no = '" + veh_chassis_no + "' "));

							SOP("veh_reg_no_check--------" + veh_reg_no_check);
							SOP("veh_engine_no_check=----------" + veh_engine_no_check);
							SOP("veh_chassis_no_check------------" + veh_chassis_no_check);
							//
							// if (!branch_name.equals("")) {
							// StrSql = "SELECT branch_id FROM " +
							// compdb(comp_id) + "axela_branch"
							// + " WHERE branch_name = '" + branch_name + "'";
							// branch_id = CNumeric(ExecuteQuery(StrSql));
							//
							// }
							// SOP("contact_name==" + contact_name);
							if (contact_id.equals("0")) {
								if (contact_title_id.equals("0")) {
									contact_title_id = "1";
								}
							}

							// if (!contact_city.equals("")) {
							// StrSql = "SELECT city_id FROM " + compdb(comp_id)
							// + "axela_city"
							// + " WHERE city_name = '" + contact_city + "'";
							// contact_city_id = ExecuteQuery(StrSql);
							// if (CNumeric(contact_city_id).equals("0")) {
							// contact_city_id = "6";
							// }
							// }
							// SOP("contact_city_id--------" + contact_city_id);

							item_id = "0";
							// if (item_id.equals("0")) {
							// StrSql = "SELECT item_name from " +
							// compdb(comp_id) + "axela_inventory_item "
							// + " WHERE item_name= '" + model_name + "'"
							// + " LIMIT 1";
							// item_name = PadQuotes((ExecuteQuery(StrSql)));
							// if (!item_name.equals("")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " WHERE item_type_id = 1 AND item_name = '" +
							// item_name + "'"
							// + " LIMIT 1";
							// SOP("StrSql=-----item----" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// }

							// SOP("item_id----first time----" + item_id);

							if (model_name.equals("New Swift/ New D")) {
								if (model_name.contains("/")) {
									model_name_arr = model_name.split("/");
									model_name = model_name_arr[0];
									model_name1 = model_name_arr[1];
								}
							} else if (model_name.equals("NEW WAGON-R") || model_name.equals("WAGON-R NEW")) {
								model_name = "Wagon R";
							} else if (model_name.contains("M800") || model_name.contains("M 800")) {
								model_name = "Maruti 800";
								// SOP("model_name==m800==" + model_name);
							} else if (model_name.contains("SWIFT NEW / DZ") || model_name.contains("SWIFT NEW/  DZ")) {
								model_name = "SWIFT";
							} else if (model_name.contains("ERTIGA DIESEL") || model_name.contains("ERTIGA CNG")) {
								model_name = "Ertiga";
							} else if (model_name.contains("ALTO") || model_name.contains("ALTO 800 CNG") || model_name.contains("ALTO K10 CNG")) {
								model_name = "ALTO 800";
							} else if (model_name.contains("CELERIO DIESEL") || model_name.contains("CELERIO CNG")) {
								model_name = "CELERIO";
							} else if (model_name.contains("CIAZ DIESEL") || model_name.contains("CIAZ PETROL")) {
								model_name = "CIAZ";
							} else if (model_name.contains("VITARA")) {
								model_name = "GRAND VITARA";
							}// -----Start model conditions------------
							else if (model_name.contains("D' Zire Vdi") || model_name.contains("Zire")) {
								model_name = "zire";
							} else if (model_name.contains("EECO Ambulance") || model_name.contains("EECO")) {
								model_name = "Eeco";
							} else if (model_name.contains("Omni 5 Seater") || model_name.contains("Omni")) {
								model_name = "Omni";
							} else if (model_name.contains("Ritz VDI") || model_name.contains("Ritz")) {
								model_name = "Ritz";
							} else if (model_name.contains("SX4-VDI") || model_name.contains("SX4")) {
								model_name = "SX4";
							} else if (model_name.contains("Baleno Lxi") || model_name.contains("Baleno")) {
								model_name = "Baleno";
							} else if (model_name.contains("Zen Estilo Lxi") || model_name.contains("Estilo")) {
								model_name = "Estilo";
							}
							// -----End model conditions------------
							if (item_id.equals("0")) {
								StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1 AND item_name like '" + model_name + "%'"
										+ " LIMIT 1";
								SOP("StrSql-----item_id1-----" + StrSql);
								item_id = CNumeric(ExecuteQuery(StrSql));
								SOP("item_id----1-----" + item_id);

								if (!model_name.equals("") && item_id.equals("0")) {
									StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE item_type_id = 1 AND '" + model_name + "' like CONCAT(model_name, '%')"
											+ " LIMIT 1";
									SOP("StrSql=---------item_id2==========" + StrSql);
									item_id = CNumeric(ExecuteQuery(StrSql));
									SOP("item_id-----22-----" + item_id);
								}
								if (!model_name.equals("") && item_id.equals("0")) {
									StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE item_type_id = 1 AND '" + model_name + "' like CONCAT('%',model_name, '%')"
											+ " LIMIT 1";
									SOP("StrSql======item_id3========" + StrSql);
									item_id = CNumeric(ExecuteQuery(StrSql));
									SOP("item_id----2-----" + item_id);
								}
								if (!model_name.equals("") && item_id.equals("0")) {
									StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE item_type_id = 1 AND '" + model_name + "' like CONCAT('%',model_desc, '%')"
											+ " LIMIT 1";
									SOP("StrSql============item_id4==" + StrSql);
									item_id = CNumeric(ExecuteQuery(StrSql));
									SOP("item_id-----3-----" + item_id);
								}
							}
							if (item_id.equals("0")) {
								if (!model_name1.equals("")) {
									StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_type_id = 1 AND item_name like '%" + model_name1 + "%'"
											+ " LIMIT 1";
									SOP("StrSql========item_id5==" + StrSql);
									item_id = CNumeric(ExecuteQuery(StrSql));
									SOP("item_id-----4-----" + item_id);
								}
							}

							if (item_id.equals("0")) {
								if (!model_name.equals("") || !item_name.equals("")) {
									item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_type_id = 1 AND item_name ='Others'"));
									SOP("item_id-----6-----" + item_id);

								}
							}

							// if (!item_service_code.equals("")) {
							//
							// item_id = "0";
							// StrSql = "SELECT item_id" + " FROM " +
							// compdb(comp_id) + "axela_inventory_item"
							// + " WHERE item_service_code = '" +
							// item_service_code + "'"
							// + " LIMIT 1";
							// SOP("StrSql===item by 1.service code==" +
							// StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// if (item_id.equals("0")) {
							// StrSql = "SELECT item_id" + " FROM " +
							// compdb(comp_id) + "axela_inventory_item"
							// + " WHERE item_service_code like '" +
							// item_service_code + "%'"
							// + " LIMIT 1";
							// SOP("StrSql===item by 2. service code==" +
							// StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// }

							// if (veh_notes.equals("")) {
							// veh_notes = item_name;
							// }

							// veh_reg_no_check = crs1.getString("veh_reg_no");
							// veh_engine_no_check =
							// crs1.getString("veh_engine_no");
							// veh_chassis_no_check =
							// crs1.getString("veh_chassis_no");

							SOP("item_id-----final----------" + item_id);
							if (!item_id.equals("0") && !veh_chassis_no_check.equals(veh_chassis_no) && !veh_engine_no_check.equals(veh_engine_no) && !veh_reg_no_check.equals(veh_reg_no)) {
								if (contact_id.equals("0")) {
									if (CNumeric(customer_id).equals("0")) {
										customer_id = AddCustomer();
										SOP("after adding --------------------customer id--------" + customer_id);
									}

									if (CNumeric(contact_id).equals("0")) {
										contact_id = AddContact();
										SOP("after adding --------------------contact_id --------" + contact_id);
									}
								}
								SOP("contact_id-----after-----" + contact_id + " customer_id--after---" + customer_id);

								if (!CNumeric(contact_id).equals("0") && !CNumeric(customer_id).equals("0")) {
									AddVehicle();
									SOP("after adding --------------------contact_id --------" + contact_id);
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "vehmpdata"
											+ " SET active = '2'"
											+ " WHERE vehcol_id = " + vehcol_id + "";
									updateQuery(StrSql);
								}
							} // End Scope of item_id !=0
							else {
								StrSql = "UPDATE " + compdb(comp_id) + "vehmpdata"
										+ " SET active = '2'"
										+ " WHERE vehcol_id = " + vehcol_id + "";
								updateQuery(StrSql);
							}
						}
						else {
							// // For updating veh followup time for existing
							// // vehicles...
							// StrSql = " UPDATE " + compdb(comp_id) +
							// "axela_service_followup"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_service_veh ON veh_id = vehfollowup_veh_id"
							// + " SET vehfollowup_followup_time = " +
							// " CONCAT(substr(" + ToShortDate(kknow()) +
							// ",1,8), 100000)"
							// + " WHERE 1=1"
							// // + " AND veh_vehsource_id = 2 "
							// +
							// " AND IF(veh_calservicedate!='', DATE_FORMAT(DATE_SUB(veh_calservicedate,INTERVAL 1 MONTH),'%Y%m%d%h%i%s') <= '"
							// + ToLongDate(kknow()) + "', '')"
							// // +
							// //
							// " AND coalesce(DATE_FORMAT(DATE_SUB(veh_calservicedate, INTERVAL 1 MONTH),'%Y%m%d%h%i%s') <= '"
							// // + ToLongDate(kknow()) + "','') "
							// + " AND vehfollowup_veh_id = " + veh_id
							// + " AND vehfollowup_desc = ''";
							// SOP("StrSql----veh present--------" + StrSql);
							// stmttx.execute(StrSql);
							//
							// // For inserting the recent kms of the vehicle
							// into
							// // veh_kms table
							// if (!veh_kms.equals("0") || veh_kms.equals("0"))
							// {
							// StrSql = "INSERT INTO " + compdb(comp_id) +
							// "axela_service_veh_kms"
							// + " (vehkms_veh_id,"
							// + " vehkms_kms,"
							// + " vehkms_entry_id,"
							// + " vehkms_entry_date)"
							// + " VALUES"
							// + " (" + veh_id + ","
							// + " " + veh_kms + ","
							// + " " + veh_entry_id + ","
							// + " '" + veh_lastservice + "')";
							// SOP("Insert-----if------service_veh_kmss------" +
							// StrSqlBreaker(StrSql));
							// stmttx.execute(StrSql);
							//
							// // For updating the recent kms of the vehicle
							// // into veh table
							// StrSql = "UPDATE " + compdb(comp_id) +
							// "axela_service_veh"
							// + " SET ";
							// if (!customer_id.equals("0")) {
							// StrSql += " veh_customer_id = " + customer_id +
							// ",";
							// }
							// if (!contact_id.equals("0")) {
							// StrSql += " veh_contact_id = " + contact_id +
							// ",";
							// }
							// if (!veh_chassis_no.equals("")) {
							// StrSql += " veh_chassis_no = '" + veh_chassis_no
							// + "',";
							// }
							// if (!veh_engine_no.equals("")) {
							// StrSql += " veh_engine_no = '" + veh_engine_no +
							// "',";
							// }
							// if (!veh_kms.equals("0")) {
							// StrSql += " veh_kms = " + veh_kms + ",";
							// }
							// if (!veh_lastservice.equals("")) {
							// StrSql += " veh_lastservice = '" +
							// veh_lastservice + "',";
							// }
							// // if (!veh_lastservice_kms.equals("0")) {
							// // StrSql += " veh_lastservice_kms = " +
							// // veh_lastservice_kms + ",";
							// // }
							// // if (!veh_service_duedate.equals("")) {
							// // StrSql += " veh_service_duedate = '" +
							// // veh_service_duedate + "',";
							// // }
							// // if (!veh_service_duekms.equals("0")) {
							// // StrSql += " veh_service_duekms = " +
							// // veh_service_duekms + ",";
							// // }
							// if (!veh_emp_id.equals("0")) {
							// StrSql += " veh_emp_id = " + veh_emp_id + ",";
							// }
							// StrSql = StrSql.substring(0, StrSql.length() -
							// 1);
							// StrSql += " WHERE veh_id =" + veh_id;
							// SOP("strsql-------if-----veh----" + StrSql);
							// stmttx.execute(StrSql);
							// }
							//
							SOP("inside ------------ else block");
							StrSql = "UPDATE " + compdb(comp_id) +
									"vehmpdata "
									+ " SET active = '1'"
									+ " WHERE vehcol_id = " + vehcol_id + "";
							updateQuery(StrSql);
						}
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "vehmpdata"
								+ " SET active = '2'"
								+ " WHERE vehcol_id = " + vehcol_id + "";
						updateQuery(StrSql);
					}
				}
				SOP("count------1-----" + count);
				msg = count + " Vehicles imported successfully!";
				// Thread.sleep(5000);
				// Addfile(request, response);
			}
			crs.close();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== "
					+ this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0]
							.getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			// if (rs != null && !crs.isClosed()) {
			// crs.close();
			// }
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public String AddCustomer() throws SQLException {
		SOP("branch_id------AddCustomer-----------" + branch_id);
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_mobile2,"
				+ " customer_phone1,"
				+ " customer_phone2,"
				+ " customer_email1,"
				+ " customer_email2,"
				+ " customer_address,"
				+ " customer_city_id,"
				+ " customer_pin,"
				+ " customer_soe_id,"
				+ " customer_sob_id,"
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
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " " + soe_id + ","
				+ " " + sob_id + ","
				+ " '" + ToShortDate(kknow()) + "',"
				+ " '1',"
				+ " '',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("Insert ---------axela_customer--------" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx
				.getGeneratedKeys();

		while (rs.next()) {
			customer_id = rs.getString(1);
			SOP("customer_id-------after inserted-----" + customer_id);
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
				+ " contact_mobile2,"
				+ " contact_phone1,"
				+ " contact_phone2,"
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
				+ " '" + contact_phone1 + "',"
				+ " '" + contact_phone2 + "',"
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '',"
				+ " '1',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("Insert ---------axela_customer_contact--------" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx
				.getGeneratedKeys();

		while (rs.next()) {
			contact_id = rs.getString(1);
			SOP("contact_id-------after inserted-----" + contact_id);
		}
		rs.close();
		return contact_id;
	}

	public void AddVehicle() throws SQLException {
		SOP("branch_id-------AddVehicle----" + branch_id);
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
				+ " (veh_branch_id,"
				+ " veh_customer_id,"
				+ " veh_contact_id,"
				+ " veh_so_id,"
				+ " veh_variant_id,"
				+ " veh_modelyear,"
				+ " veh_comm_no,"
				+ " veh_chassis_no,"
				+ " veh_engine_no,"
				+ " veh_reg_no,"
				+ " veh_insur_date,"
				+ " veh_emp_id,"
				+ " veh_vehsource_id,"
				+ " veh_kms,"
				+ " veh_lastservice,"
				// + " veh_lastservice_kms,"
				+ " veh_service_duedate,"
				// + " veh_service_duekms,"
				+ " veh_iacs,"
				+ " veh_crmemp_id,"
				+ " veh_insuremp_id,"
				+ " veh_ref_no,"
				+ " veh_renewal_date,"
				+ " veh_imp_no,"
				+ " veh_notes,"
				+ " veh_entry_id,"
				+ " veh_entry_date)"
				+ " VALUES"
				+ " (" + branch_id + ","
				+ " " + customer_id + ","
				+ " " + contact_id + ","
				+ " " + so_id + ","
				+ " " + item_id + ","
				+ " '" + veh_modelyear + "',"
				+ " " + CNumeric(veh_comm_no) + ","
				+ " '" + veh_chassis_no + "',"
				+ " '" + veh_engine_no + "',"
				+ " '" + veh_reg_no + "',"
				+ " '" + imp_insur_date + "',"
				+ " " + veh_emp_id + ","
				+ " " + veh_vehsource_id + ","
				+ " " + CNumeric(veh_kms) + ","
				+ " '" + veh_lastservice + "',"
				// + " " + CNumeric(veh_lastservice_kms) + ","
				+ " '" + veh_service_duedate + "',"
				// + " " + CNumeric(veh_service_duekms) + ","
				+ " '" + veh_iacs + "',"
				+ " " + 0 + ", "
				// + " " + ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id)
				// + "axela_emp"
				// + " WHERE 1=1 AND emp_crm = 1 and emp_active = 1 "
				// + " ORDER BY RAND() LIMIT 1") + ","
				+ " " + veh_crm_emp_id + ","
				+ " '" + veh_ref_no + "',"
				+ " '" + veh_renewal_date + "',"
				+ " " + vehcol_id + ","
				+ " 'Ins import data',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("Insert ---------axela_service_veh--------" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx
				.getGeneratedKeys();

		while (rs.next()) {
			veh_id = rs.getString(1);
			SOP("veh_id-------after inserted-----" + veh_id);
		}
		rs.close();
		veh_id = CNumeric(veh_id);
		if (!veh_id.equals("0")) {
			count++;
			SOP("count-----------veh-----------" + count);
		}
		//
		// if (!veh_id.equals("0")) {
		// StrSql = "SELECT emp_id"
		// + " FROM " + compdb(comp_id) + "axela_emp"
		// + " WHERE emp_active = 1"
		// + " AND emp_insur = 1"
		// + " ORDER BY RAND()"
		// + " LIMIT 1";
		// String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));
		//
		// if (insurpolicy_emp_id.equals("0")) {
		// insurpolicy_emp_id = CNumeric(veh_emp_id);
		// }
		//
		// if (!insurpolicy_emp_id.equals("0")) {
		// // AddInsurFollowupFields(veh_id, veh_sale_date, insurpolicy_emp_id);
		// }
		// }
		//
		// // SOP("StrSql===veh_id==" + veh_id);
		// if (!CNumeric(veh_kms).equals("0") && !veh_id.equals("0")) {
		// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
		// + " (vehkms_veh_id,"
		// + " vehkms_kms,"
		// + " vehkms_entry_id,"
		// + " vehkms_entry_date)"
		// + " VALUES"
		// + " (" + veh_id + ","
		// + " " + veh_kms + ","
		// + " " + veh_entry_id + ","
		// + " '" + veh_lastservice + "')";
		// stmttx.execute(StrSql);
		// }
		// if (!veh_id.equals("0") && !vehstock_id.equals("0")) {
		// StrSql = "INSERT INTO " + compdb(comp_id) +
		// "axela_service_veh_option_trans"
		// + " (vehtrans_option_id,"
		// + " vehtrans_veh_id)"
		// + " SELECT trans_option_id,"
		// + " " + veh_id + ""
		// + " FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
		// + " WHERE trans_vehstock_id = " + vehstock_id + "";
		// stmttx.execute(StrSql);
		// } else if (!veh_id.equals("0")) {
		// option_id = "0";
		// StrSql = "SELECT option_id FROM " + compdb(comp_id) +
		// "axela_vehstock_option"
		// + " INNER JOIN " + compdb(comp_id) +
		// "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
		// + " WHERE option_name = '" + interior + "'";
		// option_id = CNumeric(ExecuteQuery(StrSql));
		//
		// if (!option_id.equals("0")) {
		// StrSql = "INSERT INTO " + compdb(comp_id) +
		// "axela_service_veh_option_trans"
		// + " (vehtrans_option_id,"
		// + " vehtrans_veh_id)"
		// + " VALUES"
		// + " (" + option_id + ","
		// + " " + veh_id + ")";
		// // SOP("StrSql=interior=" + StrSql);
		// stmttx.execute(StrSql);
		// }
		//
		// // SOP("interior==" + interior + " exterior==" + exterior);
		// if (!interior.equals(exterior)) {
		// option_id = "0";
		// StrSql = "SELECT option_id FROM " + compdb(comp_id) +
		// "axela_vehstock_option"
		// + " INNER JOIN " + compdb(comp_id) +
		// "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
		// + " WHERE option_name = '" + exterior + "'";
		// option_id = CNumeric(ExecuteQuery(StrSql));
		//
		// if (!option_id.equals("0")) {
		// StrSql = "INSERT INTO " + compdb(comp_id) +
		// "axela_service_veh_option_trans"
		// + " (vehtrans_option_id,"
		// + " vehtrans_veh_id)"
		// + " VALUES"
		// + " (" + option_id + ","
		// + " " + veh_id + ")";
		// // SOP("StrSql=exterior=" + StrSql);
		// stmttx.execute(StrSql);
		// }
		// }
		// }
		conntx.commit();
		// new Manage_Veh_Kms().VehKmsUpdate(veh_id, comp_id);

		StrSql = "UPDATE " + compdb(comp_id) + "vehmpdata"
				+ " SET active = '1'"
				+ " WHERE vehcol_id = " + vehcol_id + "";
		updateQuery(StrSql);

	}

	public void AddInsurFollowupFields(String veh_id, String veh_sale_date,
			String insurpolicy_emp_id) throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_insurance_followup"
					+ " (insurfollowup_veh_id," + " insurfollowup_emp_id,"
					+ " insurfollowup_followup_time,"
					+ " insurfollowup_followuptype_id,"
					+ " insurfollowup_priorityinsurfollowup_id,"
					+ " insurfollowup_desc," + " insurfollowup_entry_id,"
					+ " insurfollowup_entry_time," + " insurfollowup_trigger)"
					+ " VALUES" + " (" + veh_id + ","
					+ " " + insurpolicy_emp_id + ","
					+ " IF(CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5, 8)) <"
					+ " SUBSTR('" + ToLongDate(kknow()) + "', 1, 8), "
					+ " CONCAT(SUBSTR(DATE_ADD('" + ToLongDate(kknow()) + "', INTERVAL 1 YEAR), 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date
					+ ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5)),"
					+ " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5))),"
					// + " DATE_FORMAT(DATE_ADD(" + veh_sale_date +
					// ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s'),"
					+ " 1,"
					+ " 1," + " ''," + " " + veh_entry_id + "," + " '" + veh_entry_date
					+ "'," + " 0)";
			SOP("StrSql===insur followup==" + StrSql);
			stmttx.execute(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
					+ " veh_insuremp_id = " + insurpolicy_emp_id + ""
					+ " WHERE veh_id = " + veh_id + "";
			// SOP("strsql==update=veh=insur==" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
