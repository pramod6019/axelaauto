package axela.insurance;
//@Bhagwan Singh 1 March 2013

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

import axela.service.Manage_Veh_Kms;
import cloudify.connect.Connect;

public class Insurance_Veh_Add extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String customer_id = "0", customer_branch_id = "0", veh_sale_amount = "0", veh_vehsource_id = "0";
	public String customer_name = "";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String contact_city_id = "0";
	public String state_id = "0";
	public String so_id = "0";
	public String veh_so_id = "0";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String link_branch_name = "";
	public String link_so_name = "";
	public String item_model_id = "0";
	public String veh_id = "0";
	public String veh_branch_id = "0";
	public String vehkms_id = "0";
	public String veh_customer_id = "0";
	public String contact_id = "0";
	public String branch_id = "0";
	public String prev_contact_id = "0";
	public String veh_contact_id = "0";
	public String veh_item_id = "0";
	public String veh_modelyear = "";
	public String veh_chassis_no = "";
	public String veh_engine_no = "";
	public String veh_reg_no = "";
	public String veh_lastservice = "";
	public String veh_lastservice_kms = "0";
	public String veh_sale_date = "", vehsaledate = "", veh_insuremp_id = "0", veh_insursource_id = "0", veh_renewal_date = "";
	public String soe_id = "0", sob_id = "0";
	public String veh_kms = "", renewal_date = "";
	public String veh_service_duekms = "0", veh_service_duedate = "", service_duedate = "";
	public String veh_warranty_expirydate = "";
	public String veh_iacs = "0";
	public String vehwarrantyexpirydate = "";
	public String vehstock_delstatus_id = "0";
	public String veh_notes = "";
	public String veh_entry_id = "0";
	public String veh_entry_date = "";
	public String veh_entry_by = "";
	public String entry_date = "";
	public String emp_role_id = "0";
	public String veh_modified_id = "0";
	public String veh_modified_date = "";
	public String modified_date = "";
	public String veh_modified_by = "";
	public String QueryString = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String vehicle_so_id = "0";
	public String vehicle_so_name = "";
	public String veh_crmemp_id = "0";
	public String veh_emp_id = "0";
	public String imp_veh_followup = "0";
	public String BranchAccess = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			// SOP("comp_id===" + comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				customer_branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				SOP("veh_id-------------" + veh_id);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				// updateB = PadQuotes(request.getParameter("update_button"));
				// deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				so_id = CNumeric(PadQuotes(request.getParameter("span_so_id")));
				veh_so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				veh_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				// branch_id =
				// CNumeric(PadQuotes(request.getParameter("span_branch_id")));
				veh_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				veh_reg_no = PadQuotes(request.getParameter("veh_reg_no"));

				PopulateContactDetails();
				PopulateSODetails();

				if (add.equals("yes")) {
					status = "Add";
					if (!"yes".equals(addB)) {
						contact_mobile1 = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_add", request).equals("1")) {
							veh_entry_id = emp_id;
							veh_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("vehicle-list.jsp?veh_id=" + veh_id + "&msg=Vehicle added successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					}
				}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		customer_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		if (!customer_branch_id.equals("0"))
		{
			veh_branch_id = customer_branch_id;
		}
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_email2 = PadQuotes(request.getParameter("txt_contact_email2"));
		contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_contact_city_id")));
		state_id = CNumeric(PadQuotes(request.getParameter("dr_contact_state_id")));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		so_id = CNumeric(PadQuotes(request.getParameter("span_so_id")));
		veh_so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
		customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
		veh_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
		veh_sale_amount = CNumeric(PadQuotes(request.getParameter("txt_veh_sale_amount")));
		contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
		veh_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		prev_contact_id = CNumeric(PadQuotes(request.getParameter("txt_prev_contact_id")));

		if (!veh_so_id.equals(so_id)) {
			veh_so_id = so_id;
		}

		if (!customer_id.equals("0") && !veh_customer_id.equals(customer_id)) {
			veh_customer_id = customer_id;
		}

		if (!contact_id.equals("0") && !veh_contact_id.equals(contact_id)) {
			veh_contact_id = contact_id;
		}

		item_model_id = CNumeric(PadQuotes(request.getParameter("dr_item_model_id")));
		veh_item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
		// veh_crmemp_id =
		// CNumeric(PadQuotes(request.getParameter("dr_veh_crmemp_id")));
		// veh_emp_id =
		// CNumeric(PadQuotes(request.getParameter("dr_veh_emp_id")));
		veh_insuremp_id = CNumeric(PadQuotes(request.getParameter("dr_veh_insuremp_id")));
		veh_insursource_id = CNumeric(PadQuotes(request.getParameter("dr_veh_insursource_id")));
		renewal_date = PadQuotes(request.getParameter("txt_veh_renewal_date"));
		veh_modelyear = PadQuotes(request.getParameter("txt_veh_modelyear"));
		veh_chassis_no = PadQuotes(request.getParameter("txt_veh_chassis_no"));
		veh_engine_no = PadQuotes(request.getParameter("txt_veh_engine_no"));
		veh_reg_no = PadQuotes(request.getParameter("txt_veh_reg_no"));
		vehsaledate = PadQuotes(request.getParameter("txt_veh_sale_date"));
		veh_vehsource_id = PadQuotes(request.getParameter("dr_veh_vehsource_id"));
		veh_kms = CNumeric(PadQuotes(request.getParameter("txt_veh_kms")));
		// veh_service_duekms =
		// CNumeric(PadQuotes(request.getParameter("txt_veh_service_duekms")));
		// vehwarrantyexpirydate =
		// PadQuotes(request.getParameter("txt_veh_warranty_expirydate"));
		// veh_iacs =
		// CheckBoxValue(PadQuotes(request.getParameter("chk_veh_iacs")));
		// service_duedate =
		// PadQuotes(request.getParameter("txt_veh_service_duedate"));
		// veh_lastservice =
		// PadQuotes(request.getParameter("txt_veh_lastservice"));
		// veh_lastservice_kms =
		// CNumeric(PadQuotes(request.getParameter("txt_veh_lastservice_kms")));
		if (veh_lastservice.equals(""))
		{
			veh_lastservice = vehsaledate;
		}
		veh_notes = PadQuotes(request.getParameter("txt_veh_notes"));
		veh_entry_by = PadQuotes(request.getParameter("veh_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		veh_modified_by = PadQuotes(request.getParameter("veh_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	public void CheckForm() {
		msg = "";
		if (status.equals("Add") && contact_id.equals("0")) {
			if (customer_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (contact_title_id.equals("0")) {
				msg += "<br>Select Title!";
			}

			if (contact_fname.equals("")) {
				msg += "<br>Enter the First Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}

			if (!contact_lname.equals("")) {
				contact_lname = toTitleCase(contact_lname);
			}

			if (customer_name.equals("")) {
				customer_name = contact_fname + " " + contact_lname;
			} else {
				customer_name = toTitleCase(customer_name);
			}

			// if (!config_customers_dupnames.equals("1") &&
			// !customer_name.equals("")) {
			// StrSql = "Select coalesce(customer_name,'') from " +
			// compdb(comp_id) + "axela_customer"
			// + " where customer_name = '" + customer_name + "'";
			//
			// if (!ExecuteQuery(StrSql).equals("") && ExecuteQuery(StrSql) !=
			// null) {
			// msg = msg + "<br>Customer already exist!";
			// }
			// }
			if (!contact_email1.equals("")) {
				if (!IsValidEmail(contact_email1)) {
					msg += "<br>Email1 is invalid!";
				} else {
					contact_email1 = contact_email1.toLowerCase();
				}
			}

			if (!contact_email2.equals("")) {
				if (!IsValidEmail(contact_email2)) {
					msg += "<br>Email2 is invalid!";
				} else {
					contact_email2 = contact_email2.toLowerCase();
				}
			}

			if (contact_mobile1.equals("")) {
				msg += "<br>Enter Contact Mobile1!";
			} else if (!IsValidMobileNo11(contact_mobile1)) {
				msg += "<br>Enter valid Contact Mobile1!";
			}

			if (!contact_mobile2.equals("") && (!IsValidMobileNo11(contact_mobile2))) {
				msg += "<br>Enter valid Contact Mobile2!";
			}

			if (contact_address.equals("")) {
				msg += "<br>Enter Contact Address!";
			}

			if (contact_city_id.equals("0")) {
				msg += "<br>Select Contact City!";
			}

			if (contact_pin.equals("")) {
				msg += "<br>Enter Contact Pin!";
			} else if (!isNumeric(contact_pin)) {
				msg += "<br>Contact Pin: Enter Numeric!";
			}

			if (state_id.equals("0")) {
				msg += "<br>Select Contact State!";
			}
		}

		if (item_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}

		if (veh_item_id.equals("0")) {
			msg += "<br>Select Variant!";
		}

		if (veh_modelyear.equals("")) {
			msg += "<br>Enter Model Year!";
		}

		if (!veh_modelyear.equals("") && !veh_entry_date.equals("")) {
			if (Integer.parseInt(veh_modelyear) > ((Integer.parseInt(SplitYear(veh_entry_date))) + 1)) {
				msg += "<br>Model Year should be less than current Year!";
			}
		}
		// uncommented later

		if (veh_chassis_no.equals("")) {
			msg += "<br>Enter Chassis Number!";
		} else {
			StrSql = "SELECT veh_chassis_no FROM " + compdb(comp_id) +
					"axela_service_veh"
					+ " WHERE veh_chassis_no = '" + veh_chassis_no + "'"
					+ " and veh_id != " + veh_id + "";

			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Chassis Number found!";
			} else {
				if (!veh_so_id.equals("0")) {
					StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_vehstock"
							+ " INNER JOIN " + compdb(comp_id) +
							"axela_sales_so ON so_vehstock_id = vehstock_id"
							+ " AND so_active = 1"
							+ " WHERE vehstock_chassis_no = '" + veh_chassis_no + "'";
					if (!ExecuteQuery(StrSql).equals(veh_so_id)) {
						msg += "<br>Sales Order Stock and Vehicle Stock is not matching!";
					}
				} else {
					StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_vehstock"
							+ " INNER JOIN " + compdb(comp_id) +
							"axela_sales_so ON so_vehstock_id = vehstock_id"
							+ " AND so_active = 1"
							+ " WHERE vehstock_chassis_no = '" + veh_chassis_no + "'";
					veh_so_id = CNumeric(ExecuteQuery(StrSql));
				}
			}
		}
		// uncommented later
		if (veh_engine_no.equals("")) {
			msg += "<br>Enter Engine Number!";
		} else {
			StrSql = "SELECT veh_engine_no FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE veh_engine_no = '" + veh_engine_no + "'"
					+ " AND veh_id != " + veh_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Engine Number found!";
			}
		}

		if (veh_reg_no.equals("")) {
			msg += "<br>Enter Registration Number!";
		} else {
			veh_reg_no = veh_reg_no.replaceAll(" ", "");
			veh_reg_no = veh_reg_no.replaceAll("-", "");
			if (!IsValidRegNo(veh_reg_no)) {
				msg += "<br>Enter valid Registration No.!";
			}

			StrSql = "SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE veh_reg_no = '" + veh_reg_no + "'"
					+ " AND veh_id != " + veh_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Similar Vehicle Registration Number found!";
			}
		}

		if (vehsaledate.equals("")) {
			msg += "<br>Enter Sale Date!";
		} else {
			if (isValidDateFormatShort(vehsaledate)) {
				veh_sale_date = ConvertShortDateToStr(vehsaledate);
			} else {
				msg += "<br>Enter valid Sale Date!";
			}
		}

		if (!renewal_date.equals("")) {
			if (isValidDateFormatShort(renewal_date)) {
				veh_renewal_date = ConvertShortDateToStr(renewal_date);
			} else {
				msg += "<br>Enter valid Insurance Renewal Date!";
			}
		}

		if (!vehsaledate.equals("") && !service_duedate.equals("")) {
			if (isValidDateFormatShort(service_duedate) && isValidDateFormatShort(vehsaledate)) {
				if (Long.parseLong(ConvertShortDateToStr(service_duedate)) < Long.parseLong(ConvertShortDateToStr(vehsaledate))) {
					msg += "<br>Service Due Date must be greater than or equal to the Vehicle Sale Date!";
				}
			}
		}

		if (veh_kms.equals("0")) {
			msg += "<br>Enter Vehicle Kms!";
		}
		if (veh_insuremp_id.equals("0")) {
			msg += "<br>Select Insurance Executive!";
		}
	}
	public void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (contact_id.equals("0")) {
					if (!contact_mobile1.equals("") || !contact_email1.equals("")) {
						StrSql = "SELECT contact_customer_id, contact_id"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " WHERE";
						if (!contact_mobile1.equals("")) {
							StrSql += " contact_mobile1 = '" + contact_mobile1 + "'";
						}

						if (!contact_mobile1.equals("") && !contact_email1.equals("")) {
							StrSql += " OR";
						}

						if (!contact_email1.equals("")) {
							StrSql += " contact_email1 = '" + contact_email1 + "'";
						}
						CachedRowSet crs = processQuery(StrSql, 0);

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								veh_customer_id = crs.getString("contact_customer_id");
								veh_contact_id = crs.getString("contact_id");
							}
						} else {
							veh_customer_id = "0";
							veh_contact_id = "0";
						}
						crs.close();
					} else {
						veh_customer_id = "0";
						veh_contact_id = "0";
					}
				}

				if (veh_contact_id.equals("0")) {
					AddCustomer();
					AddContact();
				}
				// SOP("veh_branch_id-2---" + veh_branch_id);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
						+ " (veh_branch_id,"
						+ " veh_customer_id,"
						+ " veh_contact_id,"
						+ " veh_so_id,"
						+ " veh_item_id,"
						+ " veh_modelyear,"
						+ " veh_chassis_no,"
						+ " veh_engine_no,"
						+ " veh_reg_no,"
						+ " veh_sale_date,"
						// + " veh_sale_amount,"
						+ " veh_vehsource_id,"
						+ " veh_kms,"
						// + " veh_service_duekms,"
						// + " veh_service_duedate,"
						// + " veh_lastservice,"
						// + " veh_lastservice_kms,"
						// + " veh_crmemp_id,"
						// + " veh_emp_id,"
						+ " veh_insuremp_id,"
						+ " veh_insursource_id,"
						+ " veh_renewal_date,"
						// + " veh_warranty_expirydate,"
						// + " veh_iacs,"
						+ " veh_notes,"
						+ " veh_entry_id,"
						+ " veh_entry_date)"
						+ " VALUES"
						+ " ('" + veh_branch_id + "',"
						+ " " + veh_customer_id + ","
						+ " " + veh_contact_id + ","
						+ " " + veh_so_id + ","
						+ " " + veh_item_id + ","
						+ " '" + veh_modelyear + "',"
						+ " '" + veh_chassis_no + "',"
						+ " '" + veh_engine_no + "',"
						+ " '" + veh_reg_no.toUpperCase().replaceAll(" ", "") + "',"
						+ " '" + veh_sale_date + "',"
						// + " " + veh_sale_amount + ","
						+ " " + veh_vehsource_id + ","
						+ " '" + CNumeric(veh_kms) + "',"
						// + " " + veh_service_duekms + ","
						// + " '" + veh_service_duedate + "',"
						// + " '" + ConvertShortDateToStr(veh_lastservice) +
						// "',"
						// + " " + veh_lastservice_kms + ","
						// + " " + veh_crmemp_id + ","
						// + " " + veh_emp_id + ","
						+ " " + veh_insuremp_id + ","
						+ " " + veh_insursource_id + ","
						+ " '" + veh_renewal_date + "',"
						// + " '" + veh_warranty_expirydate + "',"
						// + " '" + veh_iacs + "',"
						+ " '" + veh_notes + "',"
						+ " " + veh_entry_id + ","
						+ " '" + veh_entry_date + "')";
				// SOP("strsql-----Veh-AddFields------" +
				// StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					veh_id = rs.getString(1);
				}
				rs.close();
				// if (!veh_id.equals("0")) {
				// SOP("inside");
				// new Manage_Veh_Kms().VehKmsUpdate(veh_id, comp_id);
				// }
				// for inserting the recent kms of the vehicle into veh_kms
				// table
				if ((!CNumeric(veh_kms).equals("0") && !CNumeric(veh_id).equals("0")) || (CNumeric(veh_kms).equals("0") && !CNumeric(veh_id).equals("0"))) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
							+ " (vehkms_veh_id,"
							+ " vehkms_kms,"
							+ " vehkms_entry_id,"
							+ " vehkms_entry_date)"
							+ " VALUES"
							+ " (" + veh_id + ","
							+ " " + CNumeric(veh_kms) + ","
							+ " " + veh_entry_id + ","
							+ " '" + ConvertShortDateToStr(veh_lastservice) + "')";
					stmttx.execute(StrSql);
				}

				if (!veh_id.equals("0")) {
					StrSql = "SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_active = 1"
							+ " AND emp_insur = 1"
							+ " ORDER BY RAND()"
							+ " LIMIT 1";
					String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));

					if (veh_insuremp_id.equals("0")) {
						veh_insuremp_id = insurpolicy_emp_id;
						if (veh_insuremp_id.equals("0")) {
							veh_insuremp_id = emp_id;
						}
					}
					// SOP("veh_insuremp_id===" + veh_insuremp_id);
					if (!veh_insuremp_id.equals("0")) {
						AddInsurFollowupFields(veh_insuremp_id);
					}
				}
				conntx.commit();
				if (!veh_id.equals("0")) {
					new Manage_Veh_Kms().VehKmsUpdate(veh_id, comp_id);
				}

				// To Update the jobcard veh Details
				// UpdateJobCardVehicle(veh_id, veh_reg_no);
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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
	public void AddInsurFollowupFields(String insurpolicy_emp_id) throws SQLException {
		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
					+ " (insurfollowup_veh_id,"
					+ " insurfollowup_emp_id,"
					+ " insurfollowup_followup_time,"
					+ " insurfollowup_followuptype_id,"
					+ " insurfollowup_priorityinsurfollowup_id,"
					+ " insurfollowup_desc,"
					// + " insurfollowup_entry_id,"
					// + " insurfollowup_entry_time,"
					+ " insurfollowup_trigger)"
					+ " VALUES"
					+ " ('" + veh_id + "',"
					+ " " + insurpolicy_emp_id + ","
					+ " IF(CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5, 8)) <"
					+ " SUBSTR('" + ToLongDate(kknow()) + "', 1, 8), "
					+ " CONCAT(SUBSTR(DATE_ADD('" + ToLongDate(kknow()) + "', INTERVAL 1 YEAR), 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date
					+ ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5)),"
					+ " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5))),"
					// + " DATE_FORMAT(DATE_ADD(" + veh_sale_date +
					// ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s'),"
					+ " 1,"
					+ " 1,"
					+ " '',"
					// + " " + emp_id + ","
					// + " '" + ToLongDate(kknow()) + "',"
					+ " 0)";
			// SOP("StrSql----AddInsurFollowupFields--" +
			// StrSqlBreaker(StrSql));
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !veh_contact_id.equals("0")) {
				StrSql = "SELECT customer_id, customer_branch_id, contact_id, customer_name, contact_fname, contact_lname,"
						+ " title_desc, contact_email1, contact_email2, contact_mobile1, contact_mobile2"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE 1 = 1";
				if (!contact_id.equals("0")) {
					StrSql += " AND contact_id = " + contact_id + "";
				} else if (!veh_contact_id.equals("0")) {
					StrSql += " AND contact_id = " + veh_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_name = crs.getString("customer_name");
					veh_branch_id = crs.getString("customer_branch_id");
					veh_customer_id = crs.getString("customer_id");
					veh_contact_id = crs.getString("contact_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateSODetails() {
		try {
			if (!so_id.equals("0") || !veh_so_id.equals("0")) {
				StrSql = "SELECT so_id, CONCAT('SO', branch_code, so_no) AS so_no"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " WHERE 1 = 1";
				if (!so_id.equals("0")) {
					StrSql += " AND so_id = " + so_id + "";
				} else if (!veh_so_id.equals("0")) {
					StrSql += " AND so_id = " + veh_so_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					link_so_name = "<a href=\"../sales/veh-salesorder-list.jsp?so_id=" + crs.getString("so_id") + "\">"
							+ crs.getString("so_no") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddCustomer() throws SQLException {
		try {
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
					+ " ('" + customer_branch_id + "',"
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
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("StrSql-------Customer-----" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();

			while (rs.next()) {
				veh_customer_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddContact() throws SQLException {
		try {
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
					+ " '',"
					+ " '1',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				veh_contact_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateState(String state_id, String span_id, String dr_state_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT state_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_state_id).append(" id=").append(dr_state_id);
			Str.append(" class=\"selectbox\" onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_contact_city_id','");
			Str.append(span_id).append("'); \">\n");
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id"));
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCity(String state_id, String city_id, String dr_city_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			if (state_id.equals("")) {
				state_id = "0";
			}

			StrSql = "SELECT city_id, city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE city_state_id = " + state_id + ""
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_city_id).append(" id=").append(dr_city_id).append(" class=selectbox>\n");
			Str.append("<option value=\"0\">Select</option>\n");
			if (!state_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("city_id"));
					Str.append(StrSelectdrop(crs.getString("city_id"), city_id));
					Str.append(">").append(crs.getString("city_name")).append("</option>\n");
				}
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTitle(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
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

	public String PopulateModel(String comp_id, String customer_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE branch_id =" + customer_branch_id;
			// if (!customer_branch_id.equals("0")) {
			// StrSql += " AND branch_id =" + customer_branch_id;
			// }
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql----PopulateModel----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_model_id\" id=\"dr_item_model_id\" class=\"selectbox\" onchange=\"PopulateItem(this.value);\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), item_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			// SOP("Str------" + Str);
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateItem(String item_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE item_model_id = " + item_model_id + ""
					+ " ORDER BY model_name, item_name";
			// SOP("StrSql----PopulateItem----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), veh_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInsuranceSource(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insursource_id, insursource_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_source"
					+ " WHERE 1=1"
					+ " ORDER BY insursource_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insursource_id"));
				Str.append(StrSelectdrop(crs.getString("insursource_id"), veh_insursource_id));
				Str.append(">").append(crs.getString("insursource_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateVehSource(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehsource_id, vehsource_name"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_source"
					+ " WHERE 1=1"
					+ " ORDER BY vehsource_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehsource_id"));
				Str.append(StrSelectdrop(crs.getString("vehsource_id"), veh_vehsource_id));
				Str.append(">").append(crs.getString("vehsource_name")).append("</option>\n");
			}
			crs.close();
			// Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInsurExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), veh_insuremp_id));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
