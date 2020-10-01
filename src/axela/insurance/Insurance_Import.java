//Shivaprasad

package axela.insurance;

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

public class Insurance_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", importmsg = "";
	public String INSTABLE = "import_vehicle_insurance";
	public int insurcount = 0;
	public String branch_id = "0";
	public String comp_id = "0";
	public String insurpolicy_id = "0";
	public String followup_time = "";
	public String entrydate = ToLongDate(kknow());
	// Veh Data Membecrs....
	public String veh_id = "0";
	public String veh_branch_id = "0";
	public String veh_customer_id = "0", veh_contact_id = "0";
	public String veh_item_id = "0", veh_modelyear = "";
	public String veh_comm_no = "", veh_chassis_no = "", veh_engine_no = "", veh_reg_no = "";
	public String veh_emp_id = "0", veh_emp_name = "";
	public String veh_sale_date = "", veh_vehsource_id = "0", vehsource_name = "", veh_kms = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0",
			veh_service_duedate = "", veh_service_duekms = "0", veh_insur_date = "";
	public String veh_crmemp_id = "0", veh_insuremp_id = "0", veh_renewal_date = "";
	public String soe_id = "0", sob_id = "0", soe_name = "", sob_name = "", veh_service_advisor_name = "";
	public String veh_notes = "";
	public String veh_followup = "0";
	public String interior = "", exterior = "", item_name = "";
	public String veh_model_id = "0", model_brand_id = "0";
	public String model_name = "";
	public String option_id = "0", item_service_code = "", veh_insur_emp_name = "";
	public String veh_entry_id = "";
	public String veh_entry_date = "";

	// Customer and Contact Data Membecrs....
	public String customer_name = "", contact_name = "", contact_title_id = "0", contact_fname = "", contact_lname = "", contact_fname_lname;
	public String contact_mobile1 = "", contact_mobile2 = "", contact_email1 = "", contact_email2 = "", contact_address = "", city_name = "", contact_city_id = "0", contact_pin = "";
	public String contact_dob = "", contact_anniversary = "", contact_email = "", contact_mobile = "";
	// General Data Membecrs....
	public String error_msg = "", imp_id = "0";
	public String veh_error_msg = "", month = "", day = "", year = "", servicedueyear = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("comp_id===" + comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
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

			insurpolicy_id = "0";

			StrSql = "SELECT imp_id,"
					+ " COALESCE(imp_customer, '') AS imp_customer,"
					+ " COALESCE(imp_contact, '') AS imp_contact,"
					+ " COALESCE(imp_mobile1, '') AS imp_mobile1,"
					+ " COALESCE(imp_mobile2, '') AS imp_mobile2,"
					+ " COALESCE(imp_email1, '') AS imp_email1,"
					+ " COALESCE(imp_email2, '') AS imp_email2,"
					+ " COALESCE(imp_phone1, '') AS imp_phone1,"
					+ " COALESCE(imp_phone2, '') AS imp_phone2,"
					+ " COALESCE(imp_address, '') AS imp_address,"
					+ " COALESCE(imp_city, '') AS imp_city,"
					+ " COALESCE(imp_pincode, '') AS imp_pincode,"
					+ " COALESCE(imp_sale_date, '') AS imp_sale_date,"
					+ " COALESCE(imp_model, '') AS imp_model,"
					+ " COALESCE(imp_item, '') AS imp_item,"
					+ " COALESCE(imp_interior, '') AS imp_interior,"
					+ " COALESCE(imp_exterior, '') AS imp_exterior,"
					+ " COALESCE(imp_chassis_no, '') AS imp_chassis_no,"
					+ " COALESCE(imp_engine_no, '') AS imp_engine_no,"
					+ " COALESCE(imp_modelyear, '') AS imp_modelyear,"
					+ " COALESCE(imp_insur_date, '') AS imp_insur_date,"
					+ " COALESCE(imp_reg_no, '') AS imp_reg_no,"
					+ " COALESCE(imp_service_date, '') AS imp_service_date,"
					+ " COALESCE(imp_kms, '') AS imp_kms,"
					+ " COALESCE(imp_service_advisor, '') AS imp_service_advisor,"
					+ " COALESCE(imp_soe, '') AS imp_soe,"
					+ " COALESCE(imp_sob, '') AS imp_sob,"
					+ " COALESCE(imp_telecaller_insur_emp_id, '') AS imp_telecaller_insur_emp_id,"
					+ " COALESCE(imp_vehcrm_emp, '') AS imp_vehcrm_emp,"
					+ " COALESCE(imp_lastservice, '') AS imp_lastservice,"
					+ " COALESCE(imp_branch_id, '') AS imp_branch_id,"
					+ " COALESCE(imp_notes, '') AS imp_notes"
					+ " FROM " + compdb(comp_id) + "" + INSTABLE
					+ " WHERE imp_active = 0"
					// +
					// " AND (imp_reg_no = 'DL8CAG0303' OR imp_reg_no = 'DL8CQ5100' OR imp_reg_no = 'DL9CQS4883')"
					+ " LIMIT 60";
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// Branch-------------------------------
					// StrSql = "SELECT branch_id"
					// + " FROM " + compdb(comp_id) + "axela_branch"
					// + " WHERE branch_name LIKE '" + crs.getString("Location")
					// + "'"
					// + " LIMIT 1";
					// branch_id = CNumeric(ExecuteQuery(StrSql));
					// if (branch_id.equals("0")) {
					// branch_id = "1";
					// }

					// CustomerName-------------------------------
					// 9810592473/9717122143
					imp_id = crs.getString("imp_id");
					// SOP("imp_id-------" + imp_id);

					veh_branch_id = crs.getString("imp_branch_id");
					// SOP("veh_branch_id-------" + veh_branch_id);

					customer_name = crs.getString("imp_customer");
					// SOP("customer_name-------" + customer_name);

					contact_fname = crs.getString("imp_contact");
					if ((contact_fname.equals("") || contact_fname.equals("0")) && !customer_name.equals(""))
					{
						contact_fname = customer_name;
					}
					// SOP("contact_fname-----11--" + contact_fname);
					if (!contact_fname.equals("")) {
						// SOP("contact_fname---22----" + contact_fname);
						if (contact_fname.contains(".") && !contact_fname.endsWith(".")) {
							// SOP("contact_fname---33----" + contact_fname);
							contact_title_id = contact_fname.split("\\.")[0];
							// SOP("contact_title_id---11----" +
							// contact_title_id);

							contact_title_id = CNumeric(ExecuteQuery("SELECT title_id FROM " + compdb(comp_id) + "axela_title"
									+ " WHERE title_desc = '" + contact_title_id + ".'"));

							contact_fname = contact_fname.split("\\.")[1].trim();
						} else {
							contact_title_id = "1";
							contact_fname = contact_fname;
						}
					}
					// SOP("contact_title_id--22-" + contact_title_id);

					// SOP("contact_fname-final--" + contact_fname);
					// Mobile-------------------------------
					contact_mobile = crs.getString("imp_mobile1");
					contact_mobile1 = "";
					contact_mobile2 = "";
					// SOP("contact_mobile-------" + contact_mobile);
					if (!crs.getString("imp_mobile2").equals("") && crs.getString("imp_mobile2").length() == 10) {
						contact_mobile2 = crs.getString("imp_mobile2");
						// SOP("contact_mobile2--1-----" + contact_mobile2);
					}

					if (contact_mobile.contains("/")) {
						if (contact_mobile.split("/")[0].length() == 10) {
							contact_mobile1 = contact_mobile.split("/")[0];
							// SOP("contact_mobile1--2-----" + contact_mobile1);
						}

						if (contact_mobile2.equals("")) {
							if (contact_mobile.split("/")[1].length() == 10) {
								contact_mobile2 = contact_mobile.split("/")[1];
								// SOP("contact_mobile2--3-----" +
								// contact_mobile2);
							}
						}
					} else {
						contact_mobile1 = contact_mobile;
					}

					// Email-------------------------------
					contact_email = crs.getString("imp_email1");
					SOP("contact_email-----" + contact_email);
					if (!contact_email.equals("") && contact_email.contains("@") && contact_email.contains(".")) {
						if (contact_email.contains(",")) {
							contact_email1 = contact_email.split(",")[0];
							contact_email2 = contact_email.split(",")[1];
							// SOP("contact_email-----" + contact_email);
							// SOP("contact_email2-----" + contact_email2);
						} else {
							contact_email1 = contact_email;
						}
					}

					// Address-------------------------------
					contact_address = crs.getString("imp_address");
					// SOP("contact_address-----" + contact_address);
					// City-------------------------------
					StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
							+ " WHERE city_name = '" + crs.getString("imp_city") + "'"
							+ " LIMIT 1";
					contact_city_id = CNumeric(ExecuteQuery(StrSql));
					// SOP("contact_city_id-----" + contact_city_id);
					if (contact_city_id.equals("0")) {
						contact_city_id = "1";
					}

					// Pincode---------
					contact_pin = crs.getString("imp_pincode");
					// SOP("contact_pin-----" + contact_pin);
					// Sale Date-------------
					veh_sale_date = crs.getString("imp_sale_date");
					// SOP("veh_sale_date-----" + veh_sale_date);
					day = "";
					month = "";
					year = "";
					veh_modelyear = "";
					// SOP("veh_sale_date===" + veh_sale_date);
					if (veh_sale_date.equals("null")) {
						veh_sale_date = "";
					}
					if (!veh_sale_date.equals("")) {
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
								// day = veh_sale_date.split("/")[0];
								// if (day.length() == 1) {
								// day = "0" + day;
								// }
								//
								// month = veh_sale_date.split("/")[1];
								// if (month.length() == 1) {
								// month = "0" + month;
								// }
								//
								// year = veh_sale_date.split("/")[2];
								//
								// if (isValidDateFormatShort(day + "/" + month
								// + "/" + year)) {
								// // SOP("inside daymonthyear");
								// veh_sale_date = year + month + day +
								// "000000";
								// veh_modelyear = year;
								// // SOP("veh_modelyear=in1==" +
								// veh_modelyear);
								// } else {
								veh_sale_date = "20140101000000";
								veh_modelyear = "2014";
								// }
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

						// SOP("veh_modelyear=in4==" + veh_modelyear);
					}
					// SOP("veh_sale_date=new==" + veh_sale_date);
					// Model-------------
					model_name = crs.getString("imp_model");
					// SOP("model_name-----" + model_name);
					// Item-----------------------------------------------
					item_name = crs.getString("imp_item");
					// SOP("item_name-----" + item_name);

					if (item_name.contains("Seater")) {
						item_name = item_name.replace("Seater", "STR");
					}
					if (item_name.contains("'")) {
						// SOP("item_name==='before==" + item_name);
						item_name = item_name.replace("'", "").trim();

						if (item_name.contains("D Zire") && item_name.split(" ").length == 2) {
							item_name = item_name.split(" ")[0] + item_name.split(" ")[1];
						}
						if (item_name.contains("D Zire") && item_name.split(" ").length == 3) {
							item_name = item_name.split(" ")[0] + item_name.split(" ")[1] + " " + item_name.split(" ")[2];
						}
						if (item_name.contains("D Zire") && item_name.split(" ").length == 4) {
							item_name = item_name.split(" ")[0] + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3];
						}
						if (item_name.contains("D Zire") && item_name.split(" ").length == 5) {
							item_name = item_name.split(" ")[0] + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3] + " " + item_name.split(" ")[3];
						}
						// SOP("item_name==='after==" + item_name);
					}

					if (item_name.contains("WagonR")) {

						if (item_name.contains("WagonR") && item_name.split(" ").length == 2) {
							item_name = "Wagon R" + " " + item_name.split(" ")[1];
						}
						if (item_name.contains("WagonR") && item_name.split(" ").length == 3) {
							item_name = "Wagon R" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2];
						}
						if (item_name.contains("WagonR") && item_name.split(" ").length == 4) {
							item_name = "Wagon R" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3];
						}
						if (item_name.contains("WagonR") && item_name.split(" ").length == 5) {
							item_name = "Wagon R" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3] + " " + item_name.split(" ")[3];
						}
						// SOP("item_name==='after==" + item_name);

					}

					if (item_name.contains("Alto800")) {

						if (item_name.contains("Alto800") && item_name.split(" ").length == 2) {
							item_name = "Alto 800" + " " + item_name.split(" ")[1];
						}
						if (item_name.contains("Alto800") && item_name.split(" ").length == 3) {
							item_name = "Alto 800" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2];
						}
						if (item_name.contains("Alto800") && item_name.split(" ").length == 4) {
							item_name = "Alto 800" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3];
						}
						if (item_name.contains("Alto800") && item_name.split(" ").length == 5) {
							item_name = "Alto 800" + " " + item_name.split(" ")[1] + " " + item_name.split(" ")[2] + " " + item_name.split(" ")[3] + " " + item_name.split(" ")[3];
						}
						// SOP("item_name==='after==" + item_name);

					}

					if (item_name.contains("Versa-Standard") || item_name.contains("SX4-AT") || item_name.contains("SX4-Zxi") || item_name.contains("SX4-ZDI") || item_name.contains("SX4-VDI")
							|| item_name.contains("SX4-Vxi") || item_name.contains("Wagon-R")) {

						if (item_name.contains("SX4-Zxi") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("SX4-VDI") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("SX4-Vxi") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("Wagon-R") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("SX4-ZDI") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("Versa-Standard") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						} else if (item_name.contains("SX4-AT") && item_name.split("-").length == 2) {
							item_name = item_name.split("-")[0] + " " + item_name.split("-")[1];
						}
						// SOP("item_name==='after==" + item_name);

					}
					if (item_name.equals("Wagon-R (Limited Edition - Premia)")) {
						item_name = "Wagon R";
					}

					StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE 1 =1"
							// +
							// " AND REPLACE(REPLACE(item_name,'&#40;','('),'&#41;',')') = '"
							// + crs.getString("VariantDESC") + "'"
							+ " AND item_name like '%" + item_name + "%'"
							+ " LIMIT 1";
					veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// SOP("StrSql=item main.==" + StrSql);
					// SOP("item_id =----main---- " + veh_item_id);

					// String[] model_name_arr = null;
					// String model_name1 = "";
					String[] item_name_arr = null;
					String item_name1 = "";
					String item_name2 = "";
					String item_name3 = "";
					String item_name4 = "";
					String item_name5 = "";
					String item_name6 = "", item_name7 = "";

					if (veh_item_id.equals("0")) {
						// SOP("1111111111=====" + item_name.split(" ").length);
						if (item_name.split(" ").length == 2) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 1.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name2 + " " + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 2.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 2.11.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}
						} else if (item_name.split(" ").length == 3) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							item_name3 = item_name.split(" ")[2];

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 3.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id "
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 4.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name2 + " " + item_name1 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 4.1.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 5.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

						} else if (item_name.split(" ").length == 4) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							item_name3 = item_name.split(" ")[2];
							item_name4 = item_name.split(" ")[3];

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 6.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 7.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 8.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name4 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 9.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name4 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 10.111==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 10.222==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 11.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}
						} else if (item_name.split(" ").length == 5) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							item_name3 = item_name.split(" ")[2];
							item_name4 = item_name.split(" ")[3];
							item_name5 = item_name.split(" ")[4];

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + " " + item_name5 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 12.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name3 + " " + item_name2 + " " + item_name1 + " " + item_name4 + " " + item_name5 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 13.11==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name3 + " " + item_name2 + " " + item_name1 + " " + item_name4 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 13.22==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 13.33==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 14.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 15.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name4 + " " + item_name5 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 16.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name5 + " " + item_name4 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 17.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 18.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}
						} else if (item_name.split(" ").length == 6) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							item_name3 = item_name.split(" ")[2];
							item_name4 = item_name.split(" ")[3];
							item_name5 = item_name.split(" ")[4];
							item_name6 = item_name.split(" ")[5];

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + " " + item_name5 + " " + item_name6 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 22.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 23.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 24.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 25.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name4 + " " + item_name5 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 26.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id "
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name5 + " " + item_name4 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 27.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 28.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

						} else if (item_name.split(" ").length == 7) {
							item_name1 = item_name.split(" ")[0];
							item_name2 = item_name.split(" ")[1];
							item_name3 = item_name.split(" ")[2];
							item_name4 = item_name.split(" ")[3];
							item_name5 = item_name.split(" ")[4];
							item_name6 = item_name.split(" ")[5];
							item_name7 = item_name.split(" ")[6];

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + " " + item_name5 + " " + item_name6 + " " + item_name7 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 31.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + " " + item_name5 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 32.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + " " + item_name4 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 33.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name2 + " " + item_name3 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 34.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 35.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name3 + " " + item_name4 + " " + item_name5 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 36.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + " " + item_name5 + " " + item_name4 + " " + item_name3 + " " + item_name2 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 37.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}

							StrSql = "SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_type_id = 1"
									+ " AND item_name like '%" + item_name1 + " " + item_name2 + "-" + item_name3 + " " + item_name7 + "%'"
									+ " LIMIT 1";
							// SOP("StrSql=item 38.==" + StrSql);
							veh_item_id = CNumeric(ExecuteQuery(StrSql));

							if (veh_item_id.equals("0")) {
								StrSql = "SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_type_id = 1"
										+ " AND item_name like '%" + item_name1 + "%'"
										+ " LIMIT 1";
								// SOP("StrSql=item 40.==" + StrSql);
								veh_item_id = CNumeric(ExecuteQuery(StrSql));
							}
						}
						// SOP("veh_item_id =----yup got it---- " +
						// veh_item_id);
					}

					// NOTE : Use any one case to get item_id
					// If Required Alert the query accordingly to satisfy given
					// condition and get item_id

					// CASE 1 : **********To be used when item_code or
					// item_service_code is given to get item**************
					// ---------------Replace in query i.e. in where condition
					// -->item_code when item_code given or item_serive_code
					// when item_serive_code given------------------------
					// veh_item_id = "0";
					// item_service_code = "";
					// item_service_code = crs.getString("imp_item");
					// // SOP("model_service_code===" + model_service_code);
					// if (item_service_code.equals("null") ||
					// item_service_code.equals("0")) {
					// item_service_code = "";
					// }
					// if (!item_service_code.equals("")) {
					// StrSql = "SELECT item_id, item_model_id" + " FROM " +
					// compdb(comp_id) + "axela_inventory_item"
					// + " WHERE item_service_code = '" + item_service_code +
					// "'"
					// + " LIMIT 1";
					// // SOP("StrSql===item by 1.service code==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					//
					// if (veh_item_id.equals("0")) {
					// StrSql = "SELECT item_id" + " FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " WHERE item_service_code like '" + item_service_code +
					// "%'"
					// + " LIMIT 1";
					// // SOP("StrSql===item by 2. service code==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// }
					//
					// if (!veh_item_id.equals("0")) {
					// model_brand_id = "0";
					// StrSql = "SELECT model_brand_id FROM " +
					// compdb(comp_id) + "axela_inventory_item_model"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item ON item_model_id = model_id"
					// + " WHERE item_id = " + veh_item_id;
					// // SOP("StrSql===model_brand_id==" + StrSql);
					// model_brand_id = CNumeric(ExecuteQuery(StrSql));
					// // SOP("model_brand_id=====" + model_brand_id);
					// }
					//
					// }

					// CASE 2 : **********To be used when item_code or
					// item_service_code is not given to get item but Only Model
					// is given and no Item given**************
					// So in this case we get one item for that model i,e
					// item_id !=0 and use it.-----------
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
					// } else if (model_name.contains("SWIFT NEW / DZ") ||
					// model_name.contains("SWIFT NEW/  DZ")) {
					// model_name = "SWIFT";
					// } else if (model_name.contains("ERTIGA DIESEL") ||
					// model_name.contains("ERTIGA CNG")) {
					// model_name = "Ertiga";
					// } else if (model_name.contains("ALTO") ||
					// model_name.contains("ALTO 800 CNG") ||
					// model_name.contains("ALTO K10 CNG")) {
					// model_name = "ALTO 800";
					// } else if (model_name.contains("CELERIO DIESEL") ||
					// model_name.contains("CELERIO CNG")) {
					// model_name = "CELERIO";
					// } else if (model_name.contains("CIAZ DIESEL") ||
					// model_name.contains("CIAZ PETROL")) {
					// model_name = "CIAZ";
					// } else if (model_name.contains("VITARA")) {
					// model_name = "GRAND VITARA";
					// }

					// if (veh_item_id.equals("0")) {
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " WHERE item_type_id = 1 AND item_name like '" +
					// model_name + "%'"
					// + " LIMIT 1";
					// // SOP("StrSql=item model-1==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// if (!model_name.equals("") && veh_item_id.equals("0")) {
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item_model ON model_id = item_model_id"
					// + " WHERE item_type_id = 1 AND '" + model_name +
					// "' like CONCAT(model_name, '%')"
					// + " LIMIT 1";
					// // SOP("StrSql=item model-2==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// }
					// if (!model_name.equals("") && veh_item_id.equals("0")) {
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item_model ON model_id = item_model_id"
					// + " WHERE item_type_id = 1 AND '" + model_name +
					// "' like CONCAT('%',model_name, '%')"
					// + " LIMIT 1";
					// // SOP("StrSql=item model-3==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// }
					// if (!model_name.equals("") && veh_item_id.equals("0")) {
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item_model ON model_id = item_model_id"
					// + " WHERE item_type_id = 1 AND '" + model_name +
					// "' like CONCAT('%',model_desc, '%')"
					// + " LIMIT 1";
					// // SOP("StrSql=item model-4==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// }
					// }
					// if (veh_item_id.equals("0")) {
					// if (!model_name1.equals("")) {
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " WHERE item_type_id = 1 AND item_name like '%" +
					// model_name1 + "%'"
					// + " LIMIT 1";
					// // SOP("StrSql=item model5==" + StrSql);
					// veh_item_id = CNumeric(ExecuteQuery(StrSql));
					// }
					// }
					//
					// if (veh_item_id.equals("0")) {
					// if (!model_name.equals("") || !item_name.equals("")) {
					// veh_item_id =
					// CNumeric(ExecuteQuery("SELECT item_id FROM " +
					// compdb(comp_id) + "axela_inventory_item"
					// + " WHERE item_type_id = 1 AND item_name ='Others'"));
					// // SOP("StrSql=item model-6 other==" + item_id);
					// }
					// }
					//
					//
					//
					// if (veh_item_id.equals("0")) {
					// error_msg += " Model/Item not present!<br>";
					// }
					// *********************************************************************************************

					interior = crs.getString("imp_interior");
					// SOP("veh_interior =-------- " + interior);

					exterior = crs.getString("imp_exterior");
					// SOP("veh_exterior =-------- " + exterior);

					veh_chassis_no = crs.getString("imp_chassis_no");
					// SOP("veh_chassis_no =-------- " + veh_chassis_no);

					veh_engine_no = crs.getString("imp_engine_no");
					// SOP("veh_engine_no =-------- " + veh_engine_no);

					if (!CNumeric(crs.getString("imp_modelyear")).equals("0")) {
						veh_modelyear = crs.getString("imp_modelyear");
					}
					// SOP("veh_modelyear =-------- " + veh_modelyear);

					veh_insur_date = crs.getString("imp_insur_date");
					// SOP("veh_insur_date =----data---- " + veh_insur_date);

					day = "";
					month = "";
					year = "";
					// SOP("veh_lastservice==111=" + veh_lastservice);
					if (!veh_insur_date.equals("")) {
						day = "";
						month = "";
						year = "";
						if (isValidDateFormatShort(veh_insur_date)) {
							veh_insur_date = ConvertShortDateToStr(veh_insur_date);
							// SOP("veh_modelyear=direct==" + veh_modelyear);
						} else if (veh_insur_date.split("/").length == 3) {
							month = veh_insur_date.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = veh_insur_date.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = veh_insur_date.split("/")[2];

							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								// SOP("inside daymonthyear");
								veh_insur_date = year + month + day + "000000";
								// SOP("veh_modelyear=in1==" + veh_modelyear);
							}
						}
					}
					// SOP("veh_insur_date==converted data=" + veh_insur_date);

					veh_reg_no = crs.getString("imp_reg_no");
					// SOP("veh_reg_no =-------- " + veh_reg_no);

					veh_lastservice = crs.getString("imp_service_date");
					// SOP("veh_lastservice =-------- " + veh_lastservice);

					day = "";
					month = "";
					year = "";
					servicedueyear = "";
					veh_service_duekms = "0";
					veh_service_duedate = "";
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
							// else {
							// day = veh_lastservice.split("/")[0];
							// if (day.length() == 1) {
							// day = "0" + day;
							// }
							// month = veh_lastservice.split("/")[1];
							// if (month.length() == 1) {
							// month = "0" + month;
							// }
							// year = veh_lastservice.split("/")[2];
							// if (isValidDateFormatShort(day + "/" + month +
							// "/" + year)) {
							// // SOP("inside daymonthyear");
							// veh_lastservice = year + month + day + "000000";
							// // SOP("veh_modelyear=in1==" + veh_modelyear);
							// }
							// }
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
						// SOP("veh_service_duedate==new=" +
						// veh_service_duedate);
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
							// SOP("servicedueyear=22==" + servicedueyear);
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
						// SOP("veh_service_duedate=new=" +
						// veh_service_duedate);
						// SOP("veh_service_duekms=new=" + veh_service_duekms);
					}

					veh_kms = crs.getString("imp_kms");
					// SOP("veh_kms =-------- " + veh_kms);

					soe_name = crs.getString("imp_soe");
					// SOP("soe_name =-------- " + soe_name);
					if (!soe_name.equals("")) {
						soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
								+ " FROM " + compdb(comp_id) + "axela_soe WHERE soe_name='" + soe_name + "'"));
						// SOP("soe_id =-------- " + soe_id);
					}

					sob_name = crs.getString("imp_sob");
					// SOP("sob_name =-------- " + sob_name);
					if (!sob_name.equals("")) {
						sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
								+ " FROM " + compdb(comp_id) + "axela_sob WHERE sob_name='" + sob_name + "'"));
						// SOP("sob_id =-------- " + sob_id);
					}

					veh_insuremp_id = crs.getString("imp_telecaller_insur_emp_id");

					// veh_service_advisor_name =
					// crs.getString("imp_service_advisor");// When given take
					veh_emp_id = "1";
					// SOP("veh_service_advisor =-------- " +
					// veh_service_advisor_name);
					crs.close();
					veh_id = "0";
					veh_customer_id = "0";
					veh_contact_id = "0";

					// **********Starts***************//
					if (!veh_reg_no.equals("") && !veh_item_id.equals("0")) { // Manditory
																				// fields
																				// to
																				// be
																				// checked
																				// if
																				// not
																				// satisfying
																				// no
																				// import
																				// for
																				// that
																				// record
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();

							StrSql = "SELECT veh_id, veh_customer_id, veh_contact_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_reg_no = '" + veh_reg_no + "'";
							// SOP("StrSql=present==" + StrSql);
							CachedRowSet crs1 = processQuery(StrSql, 0);
							// CachedRowSet crs = processQuery(StrSql, 0);
							if (crs1.isBeforeFirst()) {
								while (crs1.next()) {
									veh_id = crs1.getString("veh_id");
									veh_customer_id = crs1.getString("veh_customer_id");
									veh_contact_id = crs1.getString("veh_contact_id");
								}
							}
							crs1.close();
							// if (!veh_id.equals("0")) {
							// // SOP("veh_id ==already present = " + veh_id);
							// }

							if (veh_id.equals("0")) {
								// SOP("item_id===" + veh_item_id);
								if (veh_contact_id.equals("0")) {
									if (CNumeric(veh_customer_id).equals("0")) {
										veh_customer_id = AddCustomer();
									}
									if (CNumeric(veh_contact_id).equals("0")) {
										veh_contact_id = AddContact();
									}
								}
								// SOP("veh_contact_id==" + veh_contact_id +
								// " veh_customer_id==" + veh_customer_id);

								if (!CNumeric(veh_contact_id).equals("0") && !CNumeric(veh_customer_id).equals("0")) {
									AddVehicle();
								}
							} else {
								// For updating Customer Details for existing
								// vehicles...
								StrSql = " UPDATE " + compdb(comp_id) + "axela_customer"
										+ " SET ";
								// if (!veh_branch_id.equals("0")) {
								StrSql += " customer_branch_id = " + veh_branch_id + ",";
								// }
								// if (!customer_name.equals("")) {
								StrSql += " customer_name = '" + customer_name + "',";
								// }
								// if (!contact_mobile1.equals("")) {
								StrSql += " customer_mobile1 = '" + contact_mobile1 + "',";
								// }
								// if (!contact_mobile2.equals("")) {
								StrSql += " customer_mobile2 = '" + contact_mobile2 + "',";
								// }
								// if (!contact_email1.equals("")) {
								StrSql += " customer_email1 = '" + contact_email1 + "',";
								// }
								// if (!contact_email2.equals("")) {
								StrSql += " customer_email2 = '" + contact_email2 + "',";
								// }
								// if (!contact_address.equals("")) {
								StrSql += " customer_address = '" + contact_address + "',";
								// }
								// if (!contact_city_id.equals("0")) {
								StrSql += " customer_city_id = " + contact_city_id + ",";
								// }
								// if (!contact_pin.equals("")) {
								StrSql += " customer_pin = '" + contact_pin + "',";
								// }
								// if (!soe_id.equals("0")) {
								StrSql += " customer_soe_id = " + soe_id + ",";
								// }
								// if (!sob_id.equals("0")) {
								StrSql += " customer_sob_id = " + sob_id + ",";
								// }
								StrSql += " customer_active = '1',"
										+ " customer_modified_id = " + veh_entry_id + ","
										+ " customer_modified_date = '" + veh_entry_date + "'"
										+ " WHERE 1=1"
										// + " AND veh_vehsource_id = 2 "
										+ " AND customer_id = " + veh_customer_id;
								// SOP("StrSql=veh Customer==" + StrSql);
								stmttx.execute(StrSql);

								// For updating Contact Details for existing
								// vehicles...
								StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " SET ";
								// if (!contact_fname.equals("")) {
								StrSql += " contact_fname = '" + contact_fname + "',";
								// }
								if (!contact_lname.equals("")) {
									StrSql += " contact_lname = '" + contact_lname + "',";
								}
								// if (!contact_mobile1.equals("")) {
								StrSql += " contact_mobile1 = '" + contact_mobile1 + "',";
								// }
								// if (!contact_mobile2.equals("")) {
								StrSql += " contact_mobile2 = '" + contact_mobile2 + "',";
								// }
								// if (!contact_email1.equals("")) {
								StrSql += " contact_email1 = '" + contact_email1 + "',";
								// }
								// if (!contact_email2.equals("")) {
								StrSql += " contact_email2 = '" + contact_email2 + "',";
								// }
								// if (!contact_address.equals("")) {
								StrSql += " contact_address = '" + contact_address + "',";
								// }
								// if (!contact_pin.equals("")) {
								StrSql += " contact_pin = '" + contact_pin + "',";
								// }
								// if (!contact_dob.equals("")) {
								StrSql += " contact_dob = '" + contact_dob + "',";
								// }
								// if (!contact_anniversary.equals("")) {
								StrSql += " contact_anniversary = '" + contact_anniversary + "',";
								// }
								StrSql += " contact_active = '1',"
										+ " contact_modified_id = " + veh_entry_id + ","
										+ " contact_modified_date = '" + veh_entry_date + "'"
										+ " WHERE 1=1"
										// + " AND veh_vehsource_id = 2 "
										+ " AND contact_id = " + veh_contact_id;
								// SOP("StrSql=veh Contact==" + StrSql);
								stmttx.execute(StrSql);

								// For updating vehicle info
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET ";
								if (!veh_customer_id.equals("0")) {
									StrSql += " veh_customer_id = " + veh_customer_id + ",";
								}
								if (!veh_contact_id.equals("0")) {
									StrSql += " veh_contact_id = " + veh_contact_id + ",";
								}
								if (!veh_chassis_no.equals("")) {
									StrSql += " veh_chassis_no = '" + veh_chassis_no + "',";
								}
								if (!veh_engine_no.equals("")) {
									StrSql += " veh_engine_no = '" + veh_engine_no + "',";
								}
								if (!veh_kms.equals("0")) {
									StrSql += " veh_kms = " + CNumeric(veh_kms) + ",";
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
								if (!veh_insur_date.equals("")) {
									StrSql += " veh_insur_date = " + veh_insur_date + ",";
								}
								if (!veh_emp_id.equals("0")) {
									StrSql += " veh_emp_id = " + veh_emp_id + ",";
								}
								StrSql = StrSql.substring(0, StrSql.length() - 1);
								StrSql += " WHERE veh_id =" + veh_id;
								// SOP("strsql=====vehicle=veh===" + StrSql);
								stmttx.execute(StrSql);

								if (!veh_insuremp_id.equals("0")) {
									AddInsurFollowupFields(veh_id, veh_insur_date, veh_insuremp_id);
								}

								// For inserting the recent kms of the vehicle
								// into veh_kms table
								if (!veh_kms.equals("0") || veh_kms.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
											+ " (vehkms_veh_id,"
											+ " vehkms_kms,"
											+ " vehkms_entry_id,"
											+ " vehkms_entry_date)"
											+ " VALUES"
											+ " (" + veh_id + ","
											+ " " + CNumeric(veh_kms) + ","
											+ " " + veh_entry_id + ","
											+ " '" + veh_lastservice + "')";
									// SOP("strsql===if===veh kms===" +
									// StrSqlBreaker(StrSql));
									stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
									ResultSet rskms = stmttx.getGeneratedKeys();
									String vehkms_id = "0";
									while (rskms.next()) {
										vehkms_id = rskms.getString(1);
									}
									rskms.close();
									vehkms_id = CNumeric(vehkms_id);

									if (!vehkms_id.equals("0")) {
										// propcount++;
									}

								}

								StrSql = " DELETE FROM " + compdb(comp_id) + "axela_service_followup"
										+ " WHERE vehfollowup_desc = ''"
										+ " AND vehfollowup_veh_id = " + veh_id;
								// SOP("StrSql===del===" + StrSql);
								stmttx.execute(StrSql);

								String default_mileage = "30";

								StrSql = "UPDATE "
										+ compdb(comp_id)
										+ "axela_service_veh v1"
										+ " INNER JOIN (SELECT veh_id, calkms"
										+ " FROM (SELECT veh_id, @default_mileage:="
										+ default_mileage
										+ ","
										+ " @kmscount:=COALESCE((SELECT COUNT(vehkms_id)"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_service_veh_kms"
										+ " WHERE vehkms_veh_id = veh_id), '0') AS kmscount,"
										+ " @date1:=COALESCE((SELECT vehkms_entry_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_service_veh_kms"
										+ " WHERE vehkms_veh_id = veh_id"
										+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS date1,"
										+ " @kms1:=COALESCE((SELECT vehkms_kms"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_service_veh_kms"
										+ " WHERE vehkms_veh_id = veh_id"
										+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS kms1,"
										+ " @date2:=COALESCE((SELECT vehkms_entry_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_service_veh_kms"
										+ " WHERE vehkms_veh_id = veh_id"
										+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS date2,"
										+ " @kms2:=COALESCE((SELECT vehkms_kms"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_service_veh_kms"
										+ " WHERE vehkms_veh_id = veh_id"
										+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS kms2,"
										+ " IF(@kmscount>1, COALESCE((veh_kms + ((@kms1 - @kms2)/DATEDIFF(@date1, @date2))*DATEDIFF('" + ToLongDate(kknow())
										+ "', @date1)),(veh_kms + (@kms1-@kms2))),"
										+ " (IF(@kmscount=1, COALESCE((veh_kms + @default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms+@default_mileage)),"
										+ " COALESCE(@default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', veh_sale_date),@default_mileage))))"
										+ " AS calkms,"
										+ " IF (@kmscount > 1,"
										+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / ((@kms1 - @kms2) / DATEDIFF(@date1, @date2)) ,0),"
										+ "	(IF(@kmscount = 1,"
										+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0),"
										+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0)))"
										+ " ) AS calservicedate,"
										+ " IF(@kmscount > 1, COALESCE(@date1, ''), (IF(@kmscount = 1, COALESCE(@date1, ''), veh_sale_date )))"
										+ " AS lastservice_date"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE 1=1";
								// + " AND veh_vehsource_id = 2";
								if (!veh_id.equals("0"))
									StrSql += " AND veh_id = " + veh_id;
								StrSql += " GROUP BY veh_id"
										+ " ORDER BY veh_id) Sat) v2"
										+ " SET"
										+ " v1.veh_cal_kms = v2.calkms,"
										+ " v1.veh_calservicedate = DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL IF(@calserviceday< 365, @calserviceday, 365) DAY),'%Y%m%d%h%i%s')"
										+ " WHERE v1.veh_id = v2.veh_id";
								// SOP("StrSql====vehicle kms update==" +
								// StrSqlBreaker(StrSql));
								stmttx.execute(StrSql);

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
										+ " (vehfollowup_veh_id,"
										+ " vehfollowup_emp_id,"
										+ " vehfollowup_followup_time)"
										+ " VALUES (" + veh_id + ", " + veh_crmemp_id + ","
										+ " '20151027110000')";
								// SOP("StrSql===veh==" + StrSql);
								stmttx.execute(StrSql);

								StrSql = "UPDATE " + compdb(comp_id) + "" + INSTABLE
										+ " SET imp_active = '1'"
										+ " WHERE imp_id = " + imp_id + "";
								// SOP("StrSql===instable===" + StrSql);
								stmttx.execute(StrSql);

								conntx.commit();

								insurcount++;

							}

							importmsg = insurcount++ + " Vehicle Imported Succesfully";
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
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "" + INSTABLE
								+ " SET imp_active = '2'"
								+ " WHERE imp_id = " + imp_id + "";
						// SOP("StrSql===instable=2==" + StrSql);
						updateQuery(StrSql);
					}

					// if (!veh_customer_id.equals("0") &&
					// !veh_contact_id.equals("0") && !veh_id.equals("0") &&
					// !veh_item_id.equals("0")) {
					// StrSql = "UPDATE " + compdb(comp_id) + "" + INSTABLE
					// + " SET imp_active = '1'"
					// + " WHERE imp_id = " + imp_id + "";
					// updateQuery(StrSql);
					// } else {
					// StrSql = "UPDATE " + compdb(comp_id) + "" + INSTABLE
					// + " SET imp_active = '3'"
					// + " WHERE imp_id = " + imp_id + "";
					// updateQuery(StrSql);
					// }s
				}

				// Thread.sleep(2000);
				// AddData(request, response);
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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
				+ " (" + veh_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '" + contact_mobile2 + "',"
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
			veh_customer_id = rs.getString(1);
		}
		rs.close();
		return veh_customer_id;
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
				+ " contact_email1,"
				+ " contact_email2,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_dob,"
				+ " contact_anniversary,"
				+ " contact_notes,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + veh_customer_id + ","
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
				+ " '" + contact_dob + "',"
				+ " '" + contact_anniversary + "',"
				+ " '',"
				+ " '1',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		// SOP("StrSql==contact=" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_contact_id = rs.getString(1);
		}
		rs.close();
		return veh_contact_id;
	}

	public void AddVehicle() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
				+ " (veh_branch_id,"
				+ " veh_customer_id,"
				+ " veh_contact_id,"
				+ " veh_so_id,"
				+ " veh_item_id,"
				+ " veh_modelyear,"
				// + " veh_comm_no,"
				+ " veh_chassis_no,"
				+ " veh_engine_no,"
				+ " veh_reg_no,"
				+ " veh_sale_date,"
				+ " veh_emp_id,"
				// + " veh_vehsource_id,"
				+ " veh_kms,"
				+ " veh_lastservice,"
				+ " veh_lastservice_kms,"
				+ " veh_service_duedate,"
				+ " veh_service_duekms,"
				+ " veh_iacs,"
				+ " veh_crmemp_id,"
				+ " veh_insuremp_id,"
				+ " veh_insur_date,"
				// + " veh_renewal_date,"
				+ " veh_notes,"
				+ " veh_entry_id,"
				+ " veh_entry_date)"
				+ " VALUES"
				+ " (" + veh_branch_id + ","
				+ " " + veh_customer_id + ","
				+ " " + veh_contact_id + ","
				+ " 0,"
				+ " " + veh_item_id + ","
				+ " '" + veh_modelyear + "',"
				// + " " + CNumeric(veh_comm_no) + ","
				+ " '" + veh_chassis_no + "',"
				+ " '" + veh_engine_no + "',"
				+ " '" + veh_reg_no + "',"
				+ " '" + veh_sale_date + "',"
				+ " " + veh_emp_id + ","
				// + " " + veh_vehsource_id + ","
				+ " " + CNumeric(veh_kms) + ","
				+ " '" + veh_lastservice + "',"
				+ " " + CNumeric(veh_lastservice_kms) + ","
				+ " '" + veh_service_duedate + "',"
				+ " " + CNumeric(veh_service_duekms) + ","
				+ " 0,"
				+ " " + ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 AND emp_crm = 1 and emp_active = 1 "
						+ " ORDER BY RAND() LIMIT 1") + ","
				+ " " + veh_insuremp_id + ","
				+ " '" + veh_insur_date + "',"
				// + " '" + veh_renewal_date + "',"
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
			// propcount++;
			// SOP("propcount===" + propcount);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
					+ " (vehkms_veh_id,"
					+ " vehkms_kms,"
					+ " vehkms_entry_id,"
					+ " vehkms_entry_date)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + CNumeric(veh_kms) + ","
					+ " " + veh_entry_id + ","
					+ " '" + veh_lastservice + "')";
			// SOP("StrSql===veh_lastservice==" + StrSql);
			stmttx.execute(StrSql);

			if (!veh_insuremp_id.equals("0")) {
				// SOP("veh_insur_date===" + veh_insur_date);
				AddInsurFollowupFields(veh_id, veh_insur_date, veh_insuremp_id);
			}

			if (veh_followup.equals("1")) {
				String default_mileage = "30";

				StrSql = "UPDATE "
						+ compdb(comp_id)
						+ "axela_service_veh v1"
						+ " INNER JOIN (SELECT veh_id, calkms"
						+ " FROM (SELECT veh_id, @default_mileage:="
						+ default_mileage
						+ ","
						+ " @kmscount:=COALESCE((SELECT COUNT(vehkms_id)"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id), '0') AS kmscount,"
						+ " @date1:=COALESCE((SELECT vehkms_entry_date"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS date1,"
						+ " @kms1:=COALESCE((SELECT vehkms_kms"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS kms1,"
						+ " @date2:=COALESCE((SELECT vehkms_entry_date"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS date2,"
						+ " @kms2:=COALESCE((SELECT vehkms_kms"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS kms2,"
						+ " IF(@kmscount>1, COALESCE((veh_kms + ((@kms1 - @kms2)/DATEDIFF(@date1, @date2))*DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms + (@kms1-@kms2))),"
						+ " (IF(@kmscount=1, COALESCE((veh_kms + @default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms+@default_mileage)),"
						+ " COALESCE(@default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', veh_sale_date),@default_mileage))))"
						+ " AS calkms,"
						+ " IF (@kmscount > 1,"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / ((@kms1 - @kms2) / DATEDIFF(@date1, @date2)) ,0),"
						+ "	(IF(@kmscount = 1,"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0),"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0)))"
						+ " ) AS calservicedate,"
						+ " IF(@kmscount > 1, COALESCE(@date1, ''), (IF(@kmscount = 1, COALESCE(@date1, ''), veh_sale_date )))"
						+ " AS lastservice_date"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE 1=1";
				// + " AND veh_vehsource_id = 2";
				StrSql += " AND veh_id = " + veh_id;
				StrSql += " GROUP BY veh_id"
						+ " ORDER BY veh_id) Sat) v2"
						+ " SET"
						+ " v1.veh_cal_kms = v2.calkms,"
						+ " v1.veh_calservicedate = DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL IF(@calserviceday< 365, @calserviceday, 365) DAY),'%Y%m%d%h%i%s')"
						+ " WHERE v1.veh_id = v2.veh_id";
				// SOP("StrSql====vehicle kms update==" +
				// StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
						+ " (vehfollowup_veh_id,"
						+ " vehfollowup_emp_id,"
						+ " vehfollowup_followup_time)"
						+ " VALUES (" + veh_id + ", " + veh_crmemp_id + ","
						+ " '20151027110000')";

				// SOP("strsql====add veh followup=" + StrSql);
				stmttx.execute(StrSql);
			}
		}

		// SOP("StrSql===veh_id==" + veh_id);

		if (!veh_id.equals("0")) {
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
					// SOP("StrSql===exterior===" + StrSql);
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
		StrSql = "UPDATE " + compdb(comp_id) + "" + INSTABLE
				+ " SET imp_active = '1'"
				+ " WHERE imp_id = " + imp_id + "";
		// SOP("StrSql===instable=1==" + StrSql);
		stmttx.execute(StrSql);

		conntx.commit();

		insurcount++;

	}
	public void AddInsurFollowupFields(String veh_id, String veh_insur_date,
			String veh_insuremp_id) throws SQLException {
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
					+ " " + veh_insuremp_id + ","
					// For insurance date Confirm with sir about this condition
					// before importing
					+ " DATE_FORMAT(DATE_SUB(" + veh_insur_date + ", INTERVAL 60 DAY), '%Y%m%d%h%i%s'),"
					// + " DATE_FORMAT(DATE_ADD(" + veh_sale_date +
					// ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s'),"
					+ " 1,"
					+ " 1," + " ''," + " " + veh_entry_id + "," + " '" + veh_entry_date
					+ "'," + " 0)";
			// SOP("StrSql===insur followup==" + StrSql);
			stmttx.execute(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
					+ " veh_insuremp_id = " + veh_insuremp_id + ""
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
