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

public class Vehicle_Import_V_V_IMP extends Connect {

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
	public String item_name = "", item_id = "0", item_service_code = "", model_brand_id = "0";
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
	public String model_name1 = "", veh_notes = "", day = "", month = "", year = "";
	public String model_name2 = "", branch_name = "", veh_iacs = "", vehkms_id = "0", imp_id = "0", veh_insuremp_id = "0", veh_crmemp_id = "0";
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
			StrSql = "SELECT imp_id,"
					+ " COALESCE(imp_branch_id, '') AS imp_branch_id," + " COALESCE(imp_customer, '') AS imp_customer," + " COALESCE(imp_contact, '') AS imp_contact,"
					+ " COALESCE(imp_mobile1, '') AS imp_mobile1," + " COALESCE(imp_mobile2, '') AS imp_mobile2,"
					+ " COALESCE(imp_email1, '') AS imp_email1," + " COALESCE(imp_email2, '') AS imp_email2,"
					+ " COALESCE(imp_phone1, '') AS imp_phone1," + " COALESCE(imp_phone2, '') AS imp_phone2,"
					+ " COALESCE(imp_address, '') AS imp_address," + " COALESCE(imp_so_id, '') AS imp_so_id,"
					+ " COALESCE(imp_model, '') AS imp_model," + " COALESCE(imp_item, '') AS imp_item,"
					+ " COALESCE(imp_reg_no, '') AS imp_reg_no,"
					// + " COALESCE(imp_chassis_no, '') AS imp_chassis_no,"
					+ " COALESCE(imp_engine_no, '') AS imp_engine_no," + " COALESCE(imp_iacs, '') AS imp_iacs,"
					+ " COALESCE(imp_modelyear, '') AS imp_modelyear,"
					+ " COALESCE(imp_kms, '') AS imp_kms,"// For both veh_kms,
															// veh_lastservice_kms
															// update same //
															// ServiceMileage =
															// kms
					// + " COALESCE(imp_city, '') AS imp_city," +
					// " COALESCE(Pincode, '') AS Pincode,"
					+ " COALESCE(imp_sale_date, '') AS imp_sale_date,"
					// +
					// " COALESCE(imp_interior_color, '') AS imp_interior_color,"
					// +
					// " COALESCE(imp_exterior_color, '') AS imp_exterior_color,"
					// + " COALESCE(imp_comm_no, 0) AS imp_comm_no,"
					// + " COALESCE(imp_insur_date, '') AS imp_insur_date,"
					+ " COALESCE(imp_lastservice, '') AS imp_lastservice,"
					+ " COALESCE(imp_service_advisor, '') AS imp_service_advisor,"
					+ " COALESCE(imp_vehcrm_emp_id, '') AS imp_vehcrm_emp_id,"
					+ " COALESCE(imp_notes, '') AS imp_notes"
					// + " COALESCE(imp_soe, '') AS imp_soe," +
					// " COALESCE(imp_sob, '') AS imp_sob,"
					// + " COALESCE(imp_ref_no, '') AS imp_ref_no"
					+ " FROM " + compdb(comp_id) + "import_vehicle"
					+ " WHERE imp_active = '0'"
					// + " ORDER BY vehicle_id DESC"
					+ " order by imp_id"
					+ " LIMIT 100";
			SOP("StrSql = veh data===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_id = "0";
					vehcrm_emp_id = "0";
					item_name = "";
					veh_reg_no = "";
					veh_sale_date = "";
					imp_id = CNumeric(PadQuotes(crs.getString("imp_id")));
					SOP("imp_id===" + imp_id);
					contact_mobile1 = PadQuotes(crs.getString("imp_mobile1"));
					contact_mobile2 = PadQuotes(crs.getString("imp_mobile2"));
					contact_email1 = PadQuotes(crs.getString("imp_email1"));
					contact_email2 = PadQuotes(crs.getString("imp_email2"));
					contact_phone1 = PadQuotes(crs.getString("imp_phone1"));
					contact_phone2 = PadQuotes(crs.getString("imp_phone2"));
					contact_address = PadQuotes(crs.getString("imp_address"));
					// contact_city = PadQuotes(crs.getString("imp_city"));
					// contact_pin = PadQuotes(crs.getString("imp_pincode"));
					branch_id = PadQuotes(crs.getString("imp_branch_id"));
					SOP("branch_id===" + branch_id);
					vehcrm_emp_id = PadQuotes(crs.getString("imp_vehcrm_emp_id"));
					SOP("vehcrm_emp_id===" + vehcrm_emp_id);
					// branch_id =
					// CNumeric(PadQuotes(crs.getString("imp_branch")));
					customer_name = PadQuotes(crs.getString("imp_customer"));
					contact_name = PadQuotes(crs.getString("imp_contact"));
					if (contact_name.equals("")) {
						contact_fname = customer_name; // i.e when contact is
														// blank
					}
					so_id = CNumeric(PadQuotes(crs.getString("imp_so_id")));
					model_name = PadQuotes(crs.getString("imp_model"));
					item_name = PadQuotes(crs.getString("imp_item"));
					veh_modelyear = CNumeric(PadQuotes(crs.getString("imp_modelyear")));
					SOP("veh_modelyear==111=" + veh_modelyear);
					// interior =
					// PadQuotes(crs.getString("imp_interior_color"));
					// exterior =
					// PadQuotes(crs.getString("imp_exterior_color"));
					// veh_comm_no = PadQuotes(crs.getString("imp_comm_no"));
					// veh_chassis_no =
					// PadQuotes(crs.getString("imp_chassis_no"));
					veh_engine_no = PadQuotes(crs.getString("imp_engine_no"));
					veh_reg_no = PadQuotes(crs.getString("imp_reg_no"));
					// if (veh_reg_no.contains("#")) {
					// veh_reg_no = veh_reg_no.substring(1);
					// SOP("veh_reg_no==#===" + veh_reg_no);
					// }

					// SOP("veh_reg_no==5===" + veh_reg_no);
					veh_sale_date = PadQuotes(crs.getString("imp_sale_date"));// Check
																				// the
																				// date
																				// format
					// SOP("veh_sale_date==111=" + veh_sale_date);
					if (!veh_sale_date.equals("")) {
						day = "";
						month = "";
						year = "";
						if (isValidDateFormatShort(veh_sale_date)) {
							veh_sale_date = ConvertShortDateToStr(veh_sale_date);
							veh_modelyear = veh_sale_date.substring(0, 4);
							// SOP("veh_modelyear=direct==" + veh_modelyear);
						} else if (veh_sale_date.split("/").length == 3) {
							month = veh_sale_date.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = veh_sale_date.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = veh_sale_date.split("/")[2];

							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								// SOP("inside daymonthyear");
								veh_sale_date = year + month + day + "000000";
								veh_modelyear = year;
								// SOP("veh_modelyear=in1==" + veh_modelyear);
							} else {
								veh_sale_date = "20140101000000";
								veh_modelyear = "2014";
								// SOP("veh_modelyear=in2==" + veh_modelyear);
							}
						} else {
							veh_sale_date = "20140101000000";
							veh_modelyear = "2014";
							// SOP("veh_modelyear=in3==" + veh_modelyear);
						}
						// SOP("veh_sale_date=222==" + veh_sale_date);
					} else {
						veh_sale_date = "20140101000000";
						veh_modelyear = "2014";

						// SOP("veh_sale_date=in4==" + veh_sale_date);
					}

					vehsource_name = "Service Data";// To be confirmed with
													// sujay sir ..
					veh_emp_name = PadQuotes(crs.getString("imp_service_advisor"));
					veh_emp_id = "1";// To be confirmed with sujay sir ..
					veh_kms = CNumeric(PadQuotes(crs.getString("imp_kms")));
					// SOP("veh_kms==111=" + veh_kms);
					veh_lastservice_kms = "";
					if (!veh_kms.equals("0")) {
						veh_lastservice_kms = veh_kms;
					} else {
						veh_lastservice_kms = "0";
					}
					veh_lastservice = PadQuotes(crs.getString("imp_lastservice")); // Check
																					// the
																					// date
																					// format
					// SOP("veh_lastservice==111=" + veh_lastservice);
					if (!veh_lastservice.equals("")) {
						day = "";
						month = "";
						year = "";
						if (isValidDateFormatShort(veh_lastservice)) {
							veh_lastservice = ConvertShortDateToStr(veh_lastservice);
							// SOP("veh_modelyear=direct==" + veh_modelyear);
						} else if (veh_lastservice.split("/").length == 3) {
							month = veh_lastservice.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = veh_lastservice.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = veh_lastservice.split("/")[2];

							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								// SOP("inside daymonthyear");
								veh_lastservice = year + month + day + "000000";
								// SOP("veh_modelyear=in1==" + veh_modelyear);
							}
						}
						// SOP("veh_sale_date=222==" + veh_sale_date);
					}
					if (!veh_lastservice.equals("")) {
						// if (isValidDateFormatShort(veh_lastservice)) {
						// veh_lastservice =
						// ConvertShortDateToStr(veh_lastservice);
						// }
						if (isValidDateFormatLong(veh_lastservice)) {
							veh_lastservice = ConvertLongDateToStr(veh_lastservice);
							// SOP("veh_lastservice=1111==" + veh_lastservice);
						}
					} else {
						veh_lastservice = veh_sale_date;
					}
					// SOP("veh_lastservice==new=" + veh_lastservice);
					if (!veh_lastservice.equals("")) {
						int duekms = 0;
						int duecount = 0;
						veh_service_duedate = strToLongDate(veh_lastservice);
						// SOP("veh_service_duedate==new=" + veh_service_duedate);
						day = veh_service_duedate.split("/")[0];
						month = veh_service_duedate.split("/")[1];
						year = veh_service_duedate.split("/")[2].substring(0, 4);
						// SOP("year===" + year);
						int i = Integer.parseInt(year);
						if (Long.parseLong(veh_lastservice.substring(1, 8)) > Long.parseLong(ToLongDate(kknow()).substring(1, 8))) {
							servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
						} else {
							servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
							// SOP("servicedueyear=11==" + servicedueyear);
							servicedueyear = (Integer.parseInt(servicedueyear) + 1) + "";
							SOP("servicedueyear=22==" + servicedueyear);
						}
						while (i < Integer.parseInt(servicedueyear)) {
							if (duecount == 0) {
								duekms += Integer.parseInt(veh_lastservice_kms) + 10000;
							} else {
								duekms += 10000;
							}
							i++;
							duecount++;
						}
						if (duekms == 0) {
							duekms = Integer.parseInt(veh_lastservice_kms);
						}
						veh_service_duekms = duekms + "";
						veh_service_duedate = servicedueyear + month + day + "000000";
						SOP("veh_service_duedate=new=" + veh_service_duedate);
						SOP("veh_service_duekms=new=" + veh_service_duekms);
					}

					veh_iacs = PadQuotes(crs.getString("imp_iacs"));
					// veh_renewal_date =
					// PadQuotes(crs.getString("imp_insur_date")); // For time
					// being insurpolicy_date is taken as renewal_date
					// if (!veh_renewal_date.equals("")) {
					// if (isValidDateFormatShort(veh_renewal_date)) {
					// veh_renewal_date =
					// ConvertShortDateToStr(veh_renewal_date);
					// }
					// if (isValidDateFormatLong(veh_renewal_date)) {
					// veh_renewal_date =
					// ConvertLongDateToStr(veh_renewal_date);
					// }
					// }
					// veh_insuremp_id = "0"; // To be confirmed with sujay sir
					// ..
					// veh_crmemp_id = "0";
					// soe_name = PadQuotes(crs.getString("imp_soe"));
					// sob_name = PadQuotes(crs.getString("imp_sob"));
					// veh_ref_no = PadQuotes(crs.getString("imp_ref_no"));
					veh_notes = PadQuotes(crs.getString("imp_notes"));

					vehstock_id = "0";
					veh_id = "0";
					customer_id = "0";
					contact_id = "0";
					item_id = "0";
					model_id = "0";
					contact_city_id = "0";
					// branch_id = "0";

					SOP("veh_reg_no==" + veh_reg_no);
					if (!veh_reg_no.equals("")) {
						StrSql = "SELECT COALESCE(veh_id, 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE veh_reg_no = '" + veh_reg_no + "'";
						// SOP("StrSql 2 = " + StrSql);
						veh_id = CNumeric(ExecuteQuery(StrSql));
						if (!veh_id.equals("0")) {
							SOP("veh_id ==already present = " + veh_id);
						}

						if (veh_id.equals("0")) {
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

							if (!contact_city.equals("0")) {
								StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_name = '" + contact_city + "'";
								contact_city_id = ExecuteQuery(StrSql);
								if (CNumeric(contact_city_id).equals("0")) {
									contact_city_id = "6";
								}
							}

							item_id = "0";
							// if (item_id.equals("0")) {
							// if (!item_name.equals("")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " WHERE item_type_id = 1 AND item_name = '" +
							// item_name + "'"
							// + " LIMIT 1";
							// // SOP("StrSql=item=="+StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// }
							// if (model_name.equals("New Swift/ New D")) {
							// if (model_name.contains("/")) {
							// model_name_arr = model_name.split("/");
							// model_name = model_name_arr[0];
							// model_name1 = model_name_arr[1];
							// }
							// } else if (model_name.equals("NEW WAGON-R") ||
							// model_name.equals("WAGON-R NEW")) {
							// model_name = "Wagon R";
							// } else if (model_name.contains("M800") ||
							// model_name.contains("M 800")) {
							// model_name = "Maruti 800";
							// // SOP("model_name==m800==" + model_name);
							// } else if (model_name.contains("SWIFT NEW / DZ")
							// || model_name.contains("SWIFT NEW/  DZ")) {
							// model_name = "SWIFT";
							// } else if (model_name.contains("ERTIGA DIESEL")
							// || model_name.contains("ERTIGA CNG")) {
							// model_name = "Ertiga";
							// } else if (model_name.contains("ALTO") ||
							// model_name.contains("ALTO 800 CNG") ||
							// model_name.contains("ALTO K10 CNG")) {
							// model_name = "ALTO 800";
							// } else if (model_name.contains("CELERIO DIESEL")
							// || model_name.contains("CELERIO CNG")) {
							// model_name = "CELERIO";
							// } else if (model_name.contains("CIAZ DIESEL") ||
							// model_name.contains("CIAZ PETROL")) {
							// model_name = "CIAZ";
							// } else if (model_name.contains("VITARA")) {
							// model_name = "GRAND VITARA";
							// }
							//
							// if (item_id.equals("0")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " WHERE item_type_id = 1 AND item_name like '"
							// + model_name + "%'"
							// + " LIMIT 1";
							// // SOP("StrSql=item model-1==" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// if (!model_name.equals("") &&
							// item_id.equals("0")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_inventory_item_model ON model_id = item_model_id"
							// + " WHERE item_type_id = 1 AND '" + model_name +
							// "' like CONCAT(model_name, '%')"
							// + " LIMIT 1";
							// // SOP("StrSql=item model-2==" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// if (!model_name.equals("") &&
							// item_id.equals("0")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_inventory_item_model ON model_id = item_model_id"
							// + " WHERE item_type_id = 1 AND '" + model_name +
							// "' like CONCAT('%',model_name, '%')"
							// + " LIMIT 1";
							// // SOP("StrSql=item model-3==" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// if (!model_name.equals("") &&
							// item_id.equals("0")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_inventory_item_model ON model_id = item_model_id"
							// + " WHERE item_type_id = 1 AND '" + model_name +
							// "' like CONCAT('%',model_desc, '%')"
							// + " LIMIT 1";
							// // SOP("StrSql=item model-4==" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// }
							// if (item_id.equals("0")) {
							// if (!model_name1.equals("")) {
							// StrSql = "SELECT item_id FROM " + compdb(comp_id)
							// + "axela_inventory_item"
							// + " WHERE item_type_id = 1 AND item_name like '%"
							// + model_name1 + "%'"
							// + " LIMIT 1";
							// // SOP("StrSql=item model5==" + StrSql);
							// item_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// }
							//
							// if (item_id.equals("0")) {
							// if (!model_name.equals("") ||
							// !item_name.equals("")) {
							// item_id =
							// CNumeric(ExecuteQuery("SELECT item_id FROM " +
							// compdb(comp_id) + "axela_inventory_item"
							// +
							// " WHERE item_type_id = 1 AND item_name ='Others'"));
							// // SOP("StrSql=item model-6 other==" + item_id);
							// }
							// }

							if (!item_name.equals("")) {

								item_id = "0";
								StrSql = "SELECT item_id" + " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_service_code = '" + item_name + "'"
										+ " LIMIT 1";
								// SOP("StrSql===item by 1.service code==" +
								// StrSql);
								item_id = CNumeric(ExecuteQuery(StrSql));
								if (item_id.equals("0")) {
									StrSql = "SELECT item_id" + " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_service_code like '" + item_name + "%'"
											+ " LIMIT 1";
									// SOP("StrSql===item by 2. service code=="
									// + StrSql);
									item_id = CNumeric(ExecuteQuery(StrSql));
								}
								if (!item_id.equals("0")) {
									model_brand_id = "0";
									StrSql = "SELECT model_brand_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
											+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
											+ " WHERE item_id = " + item_id;
									// SOP("StrSql===model_brand_id==" +
									// StrSql);
									model_brand_id = CNumeric(ExecuteQuery(StrSql));
									// SOP("model_brand_id=====" +
									// model_brand_id);
								}
							}
							SOP("item_id==new=" + item_id);

							if (veh_notes.equals("")) {
								veh_notes = item_name;
							}
							soe_name = "Service Data";
							if (!soe_name.equals("")) {
								soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
										+ " FROM " + compdb(comp_id) + "axela_soe WHERE soe_name='" + soe_name + "'"));
							}

							sob_name = "Service Data";
							if (!sob_name.equals("")) {
								sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
										+ " FROM " + compdb(comp_id) + "axela_sob WHERE sob_name='" + sob_name + "'"));
							}

							if (!vehsource_name.equals("")) {
								veh_vehsource_id = CNumeric(ExecuteQuery("SELECT vehsource_id"
										+ " FROM " + compdb(comp_id) + "axela_service_veh_source WHERE vehsource_name='" + vehsource_name + "'"));
							}
							// SOP("item_id===" + item_id);
							if (!item_id.equals("0")) {
								if (contact_id.equals("0")) {
									if (CNumeric(customer_id).equals("0")) {
										customer_id = AddCustomer();
									}

									if (CNumeric(contact_id).equals("0")) {
										contact_id = AddContact();
									}
								}
								SOP("contact_id==" + contact_id + " customer_id==" + customer_id);

								if (!CNumeric(contact_id).equals("0") && !CNumeric(customer_id).equals("0")) {
									AddVehicle();
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
											+ " SET imp_active = '2'"
											+ " WHERE imp_id = " + imp_id + "";
									updateQuery(StrSql);
								}
							} // End Scope of item_id !=0
							else {
								StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
										+ " SET imp_active = '2'"
										+ " WHERE imp_id = " + imp_id + "";
								updateQuery(StrSql);
							}
						} else {

							StrSql = " DELETE FROM " + compdb(comp_id) + "axela_service_followup"
									+ " WHERE vehfollowup_desc = ''"
									+ " AND vehfollowup_veh_id = " + veh_id;
							SOP("StrSql===del===" + StrSql);
							updateQuery(StrSql);

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
									+ " (vehfollowup_veh_id,"
									+ " vehfollowup_emp_id,"
									+ " vehfollowup_followup_time)"
									+ " VALUES (" + veh_id + ", " + vehcrm_emp_id + ","
									+ " '20151027110000')";

							SOP("strsql====add veh followup=" + StrSql);
							updateQuery(StrSql);

							// // For updating veh followup time for existing
							// vehicles...
							// StrSql = " UPDATE " + compdb(comp_id) +
							// "axela_service_followup"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_service_veh ON veh_id = vehfollowup_veh_id"
							// + " SET vehfollowup_followup_time = " +
							// " CONCAT(substr(" + ToShortDate(kknow()) +
							// ",1,8), 100000)"
							// + " WHERE 1=1"
							// +
							// " AND veh_calservicedate <= DATE_FORMAT(DATE_ADD('"
							// + ToLongDate(kknow()) +
							// "', INTERVAL 1 MONTH),'%Y%m%d%h%i%s') "
							// +
							// " AND veh_id NOT IN (select vehfollowup_veh_id from "
							// + compdb(comp_id) + "axela_service_followup "
							// + " where 1=1"
							// +
							// " AND vehfollowup_veh_id = veh_id AND vehfollowup_desc = '')"
							// +
							// " AND veh_id NOT IN (SELECT booking_veh_id FROM "
							// + compdb(comp_id) + "axela_service_booking"
							// + " WHERE 1 = 1"
							// +
							// " AND booking_veh_id = veh_id AND booking_bookingstatus_id = 1 AND booking_time > DATE_FORMAT(DATE_SUB('"
							// + ToLongDate(kknow())
							// + "', INTERVAL 15 DAY),'%Y%m%d%h%i%s'))";
							// // SOP("StrSql=veh present==" + StrSql);
							// stmttx.execute(StrSql);

							// For inserting the recent kms of the vehicle into
							// veh_kms table
							if (!veh_kms.equals("0") || veh_kms.equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
										+ " (vehkms_veh_id,"
										+ " vehkms_kms,"
										+ " vehkms_entry_id,"
										+ " vehkms_entry_date)"
										+ " VALUES"
										+ " (" + veh_id + ","
										+ " " + veh_kms + ","
										+ " " + veh_entry_id + ","
										+ " '" + veh_lastservice + "')";
								// SOP("strsql===if===veh kms===" +
								// StrSqlBreaker(StrSql));
								stmttx.execute(StrSql);

								// For updating the recent kms of the vehicle
								// into veh table
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET ";
								if (!customer_id.equals("0")) {
									StrSql += " veh_customer_id = " + customer_id + ",";
								}
								if (!contact_id.equals("0")) {
									StrSql += " veh_contact_id = " + contact_id + ",";
								}
								if (!veh_chassis_no.equals("")) {
									StrSql += " veh_chassis_no = '" + veh_chassis_no + "',";
								}
								if (!veh_engine_no.equals("")) {
									StrSql += " veh_engine_no = '" + veh_engine_no + "',";
								}
								if (!veh_kms.equals("0")) {
									StrSql += " veh_kms = " + veh_kms + ",";
								}
								if (!veh_lastservice.equals("")) {
									StrSql += " veh_lastservice = '" + veh_lastservice + "',";
								}
								if (!veh_lastservice_kms.equals("0")) {
									StrSql += " veh_lastservice_kms = " + veh_lastservice_kms + ",";
								}
								if (!veh_service_duedate.equals("")) {
									StrSql += " veh_service_duedate = '" + veh_service_duedate + "',";
								}
								if (!veh_service_duekms.equals("0")) {
									StrSql += " veh_service_duekms = " + veh_service_duekms + ",";
								}
								if (!veh_emp_id.equals("0")) {
									StrSql += " veh_emp_id = " + veh_emp_id + ",";
								}
								StrSql = StrSql.substring(0, StrSql.length() - 1);
								StrSql += " WHERE veh_id =" + veh_id;
								// SOP("strsql===if==111=veh===" + StrSql);
								stmttx.execute(StrSql);
							}

							StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
									+ " SET imp_active = '1'"
									+ " WHERE imp_id = " + imp_id + "";
							updateQuery(StrSql);
						}
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
								+ " SET imp_active = '2'"
								+ " WHERE imp_id = " + imp_id + "";
						updateQuery(StrSql);
					}
				}
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
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public String AddCustomer() throws SQLException {

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
				+ " veh_so_id,"
				+ " veh_variant_id,"
				+ " veh_modelyear,"
				+ " veh_comm_no,"
				+ " veh_chassis_no,"
				+ " veh_engine_no,"
				+ " veh_reg_no,"
				+ " veh_sale_date,"
				+ " veh_emp_id,"
				+ " veh_vehsource_id,"
				+ " veh_kms,"
				+ " veh_lastservice,"
				+ " veh_lastservice_kms,"
				+ " veh_service_duedate,"
				+ " veh_service_duekms,"
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
				+ " '" + veh_sale_date + "',"
				+ " " + veh_emp_id + ","
				+ " " + veh_vehsource_id + ","
				+ " " + CNumeric(veh_kms) + ","
				+ " '" + veh_lastservice + "',"
				+ " " + CNumeric(veh_lastservice_kms) + ","
				+ " '" + veh_service_duedate + "',"
				+ " " + CNumeric(veh_service_duekms) + ","
				+ " '" + veh_iacs + "',"
				+ " " + ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 AND emp_crm = 1 and emp_active = 1 "
						+ " ORDER BY RAND() LIMIT 1") + ","
				+ " " + veh_insuremp_id + ","
				+ " '" + veh_ref_no + "',"
				+ " '" + veh_renewal_date + "',"
				+ " " + imp_id + ","
				+ " '" + veh_notes + "',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		// SOP("StrSql===veh==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_id = rs.getString(1);
		}
		rs.close();
		veh_id = CNumeric(veh_id);
		if (!veh_id.equals("0")) {
			count++;
			SOP("count===veh==" + count);
		}

		if (!veh_id.equals("0")) {
			StrSql = "SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1"
					+ " ORDER BY RAND()"
					+ " LIMIT 1";
			String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));

			if (insurpolicy_emp_id.equals("0")) {
				insurpolicy_emp_id = CNumeric(veh_emp_id);
			}

			if (!insurpolicy_emp_id.equals("0")) {
				AddInsurFollowupFields(veh_id, veh_sale_date, insurpolicy_emp_id);
			}
		}

		// SOP("StrSql===veh_id==" + veh_id);
		if (!CNumeric(veh_kms).equals("0") && !veh_id.equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
					+ " (vehkms_veh_id,"
					+ " vehkms_kms,"
					+ " vehkms_entry_id,"
					+ " vehkms_entry_date)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + veh_kms + ","
					+ " " + veh_entry_id + ","
					+ " '" + veh_lastservice + "')";
			stmttx.execute(StrSql);
		}
		if (!veh_id.equals("0") && !vehstock_id.equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
					+ " (vehtrans_option_id,"
					+ " vehtrans_veh_id)"
					+ " SELECT trans_option_id,"
					+ " " + veh_id + ""
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
					+ " WHERE trans_vehstock_id = " + vehstock_id + "";
			stmttx.execute(StrSql);
		} else if (!veh_id.equals("0")) {
			option_id = "0";
			if (!model_brand_id.equals("0") && !interior.equals("")) {
				StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
						+ " WHERE option_name = '" + interior + "'"
						+ " AND option_brand_id = " + model_brand_id;
				option_id = CNumeric(ExecuteQuery(StrSql));
			}

			if (!option_id.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
						+ " (vehtrans_option_id,"
						+ " vehtrans_veh_id)"
						+ " VALUES"
						+ " (" + option_id + ","
						+ " " + veh_id + ")";
				// SOP("StrSql=interior=" + StrSql);
				stmttx.execute(StrSql);
			}

			// SOP("interior==" + interior + " exterior==" + exterior);
			if (!interior.equals(exterior)) {
				option_id = "0";
				if (!model_brand_id.equals("0") && !exterior.equals("")) {
					StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
							+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
							+ " WHERE option_name = '" + exterior + "'"
							+ " AND option_brand_id = " + model_brand_id;
					option_id = CNumeric(ExecuteQuery(StrSql));
				}

				if (!option_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
							+ " (vehtrans_option_id,"
							+ " vehtrans_veh_id)"
							+ " VALUES"
							+ " (" + option_id + ","
							+ " " + veh_id + ")";
					// SOP("StrSql=exterior=" + StrSql);
					stmttx.execute(StrSql);
				}
			}
		}
		conntx.commit();
		// new Manage_Veh_Kms().VehKmsUpdate(veh_id, vehcrm_emp_id, comp_id); //
		// Do uncomment according to requirement/make changes.... before import

		StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
				+ " SET imp_active = '1'"
				+ " WHERE imp_id = " + imp_id + "";
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
