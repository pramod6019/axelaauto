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

public class Insurance_Import5 extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public String INSTABLE = "insurpg";
	public int insurcount = 0;
	public String branch_id = "0";
	public String comp_id = "0";
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
			String entrydate = ToLongDate(kknow());
			String contact_id = "0";
			String customer_id = "0";
			String contact_title_id = "0";
			String contact_fname = "";
			String customer_name = "";
			String contact_mobile1 = "", contact_mobile2 = "";
			String contact_phone1 = "", contact_phone2 = "", contact_phone3 = "";
			String contact_email = "", contact_email1 = "", contact_email2 = "";
			String contact_city_id = "0", contact_pin = "";
			String contact_address = "", contact_address1 = "", contact_address2 = "", contact_address3 = "";

			String veh_id = "0";
			String item_id = "0";
			String veh_modelyear = "";
			String veh_reg_no = "";
			String veh_chassis_no = "";
			String veh_engine_no = "";
			String veh_sale_date = "";
			String veh_sale_amount = "";
			String veh_insursource_id = "0";
			String insurpolicy_emp_id = "";
			String veh_notes = "";

			insurpolicy_id = "0";

			StrSql = "SELECT slno,"
					+ " COALESCE(Salutation, '') AS Salutation,"
					+ " COALESCE(CustomerName, '') AS CustomerName,"
					+ " COALESCE(ADD1, '') AS ADD1,"
					+ " COALESCE(ADD2, '') AS ADD2,"
					+ " COALESCE(ADD3, '') AS ADD3,"
					+ " COALESCE(City, '') AS City,"
					+ " COALESCE(Pincode, '') AS Pincode,"
					+ " COALESCE(Mobile1, '') AS Mobile1,"
					+ " COALESCE(Mobile2, '') AS Mobile2,"
					+ " COALESCE(EmailId, '') AS EmailId,"
					+ " COALESCE(VariantDESC, '') AS VariantDESC,"// item_name
					+ " COALESCE(Variantcd, '') AS Variantcd,"// item_name
					+ " COALESCE(Model, '') AS Model,"// item_name
					+ " COALESCE(TeleCaller, '') AS TeleCaller,"
					+ " COALESCE(ClrDESC, '') AS ClrDESC,"
					+ " COALESCE(Chassis, '') AS Chassis,"
					+ " COALESCE(Engine, '') AS Engine,"
					+ " COALESCE(RegNo, '') AS RegNo,"
					+ " COALESCE(Invdt, '') AS Invdt,"
					+ " COALESCE(InvAmt, '') AS InvAmt,"
					+ " COALESCE(Location, '') AS Location"
					+ " FROM " + INSTABLE
					+ " WHERE Active = 2"
					+ " LIMIT 10";
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// Branch-------------------------------
					StrSql = "SELECT branch_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_name LIKE '" + crs.getString("Location") + "'"
							+ " LIMIT 1";
					branch_id = CNumeric(ExecuteQuery(StrSql));
					if (branch_id.equals("0")) {
						branch_id = "1";
					}

					// Mobile-------------------------------
					contact_mobile1 = crs.getString("Mobile1");
					if (contact_mobile1.contains(".")) {
						contact_mobile1 = contact_mobile1.replace(".", "");
					}
					if (contact_mobile1.length() >= 10) {
						contact_mobile1 = contact_mobile1.substring(0, 10);
					}
					contact_mobile2 = crs.getString("Mobile2");
					if (contact_mobile2.contains(".")) {
						contact_mobile2 = contact_mobile2.replace(".", "");
					}
					if (contact_mobile2.length() >= 10) {
						contact_mobile2 = contact_mobile2.substring(0, 10);
					}

					// CustomerName-------------------------------
					customer_name = crs.getString("CustomerName");

					// Contact Name-------------------------------
					StrSql = "SELECT title_id"
							+ " FROM " + compdb(comp_id) + "axela_title"
							+ " WHERE title_desc = '" + crs.getString("Salutation") + ".'";
					contact_title_id = CNumeric(ExecuteQuery(StrSql));
					if (contact_title_id.equals("0")) {
						contact_title_id = "1";
					}
					contact_fname = crs.getString("CustomerName");

					// Address-------------------------------
					contact_address1 = crs.getString("ADD1");
					contact_address2 = crs.getString("ADD2");
					contact_address3 = crs.getString("ADD3");
					contact_address = crs.getString("ADD1");
					if (!contact_address2.equals("")) {
						contact_address += "\n" + contact_address2;
					}
					if (!contact_address3.equals("")) {
						contact_address += "\n" + contact_address3;
					}

					// City-------------------------------
					String contact_city = "";
					// if (crs.getString("City").contains("-")) {
					// String city[] = crs.getString("City").split("-");
					// contact_city = city[0];
					// contact_pin = city[1];
					// SOP("contact_pin===" + contact_pin.length());
					// if (contact_pin.length() > 6) {
					// SOP("contact_pin.length()>6===" + (contact_pin.length() >
					// 6));
					// contact_pin = "";
					// contact_city = contact_city + " " + contact_pin;
					// SOP("contact_city===" + contact_city);
					// }
					// } else {
					// contact_city = crs.getString("City");
					// contact_pin = crs.getString("Pincode");
					// if (contact_pin.length() > 6) {
					// contact_pin = "";
					// }
					// }
					SOP("1111111");
					contact_city = crs.getString("City");
					SOP("contact_city==" + contact_city);
					contact_pin = crs.getString("Pincode");
					SOP("contact_pin==" + contact_pin);
					StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
							// + " WHERE city_name = '" + crs.getString("City")
							// + "'"
							+ " WHERE city_name like '%" + contact_city + "%'"
							+ " LIMIT 1";
					contact_city_id = CNumeric(ExecuteQuery(StrSql));
					if (contact_city_id.equals("0")) {
						contact_city_id = "1";
					}

					// contact_pin = crs.getString("Pincode");
					// Email-------------------------------
					contact_email = crs.getString("EmailId");

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
					// StrSql = "SELECT item_id FROM " + compdb(comp_id) +
					// "axela_inventory_item"
					// + " WHERE 1 =1"
					// + " AND item_name LIKE '" + crs.getString("VariantDESC")
					// + "%'"
					// + " OR item_code = '" + crs.getString("Variantcd") + "'"
					// + " LIMIT 1";
					item_id = crs.getString("VariantDESC");
					SOP("item_id =-------- " + item_id);

					veh_id = "0";
					customer_id = "0";
					contact_id = "0";
					veh_reg_no = crs.getString("RegNo");
					veh_chassis_no = crs.getString("Chassis");
					veh_engine_no = crs.getString("Engine");
					insurpolicy_emp_id = crs.getString("telecaller");
					// checking if vehicle already exist
					if (!crs.getString("RegNo").equals("")) {
						StrSql = "SELECT COALESCE(veh_id, 0) veh_id, COALESCE(veh_customer_id, 0) veh_customer_id,"
								+ " COALESCE(veh_contact_id, 0) veh_contact_id"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE 1=1";
						// + " WHERE (veh_chassis_no = '" + veh_chassis_no +
						// "' OR veh_engine_no = '" + veh_engine_no + "'";
						// if (!crs.getString("RegNo").equals("")) {
						StrSql += " AND veh_reg_no = '" + crs.getString("RegNo") + "'";
						// }
						// StrSql += ")";
						// SOP("StrSql =veh== " + StrSqlBreaker(StrSql));
						CachedRowSet crs1 = processQuery(StrSql, 0);
						while (crs1.next()) {
							veh_id = crs1.getInt("veh_id") + "";
							// customer_id = rsveh.getInt("veh_customer_id") +
							// "";
							// contact_id = rsveh.getInt("veh_contact_id") + "";
						}
						crs1.close();
					}

					SOP("veh_chassis_no  here ==================== " + veh_chassis_no);
					SOP("veh_engine_no  here ==================== " + veh_engine_no);
					SOP("crs.getString(\"RegNo\")  here ==================== " + crs.getString("RegNo"));
					SOP("veh_id  here ==================== " + veh_id);
					if (!item_id.equals("0")) {
						if (veh_id.equals("0")) {
							// ------------------------------------------------------
							if (!contact_mobile1.equals("") || !contact_mobile2.equals("")) {
								StrSql = "SELECT contact_id, contact_customer_id"
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " WHERE 1=1 ";
								if (!contact_mobile1.equals("") && !contact_mobile2.equals("")) {
									StrSql += " AND (contact_mobile1 = '" + contact_mobile1 + "'"
											+ " OR contact_mobile2 = '" + contact_mobile1 + "'";
									StrSql += " OR contact_mobile1 = '" + contact_mobile2 + "'"
											+ " OR contact_mobile2 = '" + contact_mobile2 + "')";
								} else if (!contact_mobile1.equals("")) {
									StrSql += " AND (contact_mobile1 = '" + contact_mobile1 + "'"
											+ " OR contact_mobile2 = '" + contact_mobile1 + "')";
								} else if (!contact_mobile2.equals("")) {
									StrSql += " AND (contact_mobile1 = '" + contact_mobile2 + "'"
											+ " OR contact_mobile2 = '" + contact_mobile2 + "')";
								}
								SOP("StrSql cont===== " + StrSqlBreaker(StrSql));
								CachedRowSet crs1 = processQuery(StrSql, 0);
								// SOP("StrSql mob====== " + StrSql);
								while (crs1.next()) {
									contact_id = crs1.getString("contact_id");
									customer_id = crs1.getString("contact_customer_id");
									SOP("contact_id ====mobmatch==== " + contact_id);
								}
								crs1.close();
							}

							SOP("contact_fname ============== " + contact_fname);
							SOP("contact_id ============== " + contact_id);
							if (contact_id.equals("0") && !contact_fname.equals("")) {
								SOP("contact_fname ============== " + contact_fname);
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
										+ " '" + entrydate + "')";
								SOP("customer StrSql ====== " + StrSqlBreaker(StrSql));
								customer_id = UpdateQueryReturnID(StrSql);
								SOP("customer_id ================ " + customer_id);

								if (!customer_id.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
											+ " (contact_customer_id,"
											+ " contact_contacttype_id,"
											+ " contact_title_id,"
											+ " contact_fname,"
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
											+ " '" + entrydate + "')";
									SOP("contact StrSql = " + StrSql);
									contact_id = UpdateQueryReturnID(StrSql);
								}
							}
							veh_sale_date = crs.getString("Invdt");
							Date dttemp;
							if (veh_sale_date != null && !veh_sale_date.equals("")) {
								dttemp = new SimpleDateFormat("MM/dd/yyyy").parse(veh_sale_date);
								veh_sale_date = new SimpleDateFormat("yyyyMMddHHmmss").format(dttemp);
							}

							veh_modelyear = veh_sale_date.substring(0, 4);
							veh_sale_amount = crs.getString("InvAmt");
							veh_sale_amount = CNumeric(veh_sale_amount);
							veh_notes = "Color: " + crs.getString("ClrDESC") + "";

							// ///////////// Insert the record in to vehicle
							// table //////////////
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
										+ " veh_insuremp_id,"
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
										+ " " + insurpolicy_emp_id + ","
										+ " '" + veh_notes + "',"
										+ " " + emp_id + ","
										+ " '" + entrydate + "')";
								// SOP("StrSql veh= " + StrSqlBreaker(StrSql));
								veh_id = UpdateQueryReturnID(StrSql);
								SOP("veh_id = " + veh_id);
							}
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									// + " veh_renewal_date = '" +
									// veh_renewal_date + "',"
									+ " veh_modified_id = " + emp_id + ","
									+ " veh_modified_date = '" + entrydate + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
						}
					}
					if (!customer_id.equals("0") && !contact_id.equals("0") && !veh_id.equals("0") && !item_id.equals("0")) {
						StrSql = "UPDATE " + INSTABLE
								+ " SET"
								+ " Active = 1"
								+ " WHERE slno = " + crs.getString("slno");
						updateQuery(StrSql);
					} else {
						StrSql = "UPDATE " + INSTABLE
								+ " SET"
								+ " Active = 3"
								+ " WHERE slno = " + crs.getString("slno");
						updateQuery(StrSql);
					}
				}

				Thread.sleep(2000);
				AddData(request, response);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
