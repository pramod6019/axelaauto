package axela.service;
//@Bhagwan Singh 12 March 2013

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

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Call_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String empEditperm = "";
	public String msg = "", add_msg = "";
	public String BranchAccess = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_email1 = "", contact_email2 = "", contact_mobile1 = "";
	public String branch_id = "0";
	public String session_id = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String call_id = "0";
	public String call_branch_id = "0";
	public String call_contact_id = "0";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String veh_id = "0";
	public String call_veh_id = "0";
	public String veh_reg_no = "";
	public String link_vehicle_name = "";
	public String item_name = "";
	public String item_id = "0";
	public String call_type_id = "3";
	public String call_prioritycall_id = "0";
	public String call_time = "";
	public String calltime = "";
	public String call_customer_voice = "";
	public String call_followup_time = "";
	public String callfollowuptime = "";
	public String booking_id = "0";
	public String callappttime = "";
	public String customer_display = "none";
	public String item_display = "none";
	public String contact_id = "0";
	public String call_entry_id = "0";
	public String call_entry_date = "";
	public String call_entry_by = "";
	public String entry_date = "";
	public String emp_role_id = "0";
	public String call_modified_id = "0";
	public String call_modified_date = "";
	public String modified_date = "";
	public String call_modified_by = "";
	public String QueryString = "";
	public String call_emp_id = "0";
	public String call_booking_time = "";
	public String booking_instruction = "";
	public String booking_mandatory = "0";
	public String inventory_location_id = "0";
	public String booking_service_emp_id = "0", booking_contact_id = "0";
	public String booking_parking_id = "0";
	public String parent_call_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String StartHour = "", StartMin = "";
	public String DurHour = "", DurMin = "";
	// PickUp Variables..
	public String pickup_pickuptype_id = "0";
	public String pickup_customer_id = "0";
	public String pickup_location_id = "0";
	public String pickup_time_from = "", pickup_time_to = "";
	public String pickup_instruction = "";
	public String pickup_contact_name = "";
	public String pickup_mobile1 = "";
	public String pickup_mobile2 = "";
	public String pickup_emp_id = "0";
	public String pickup_add = "";
	public String pickup_landmark = "";
	// Courtesy Car Variables..
	public String courtesycar_customer_id = "0";
	public String courtesycar_contact_id = "0";
	public String courtesycar_courtesyveh_id = "0";
	public String courtesycar_time_from = "", courtesycar_time_to = "";
	public String ccstartdate = "";
	public String ccenddate = "";
	public String courtesycar_contact_name = "";
	public String courtesycar_mobile1 = "";
	public String courtesycar_mobile2 = "";
	public String courtesycar_add = "";
	public String courtesycar_landmark = "";
	public String courtesycar_notes = "";
	public String veh_info = "";
	public String contact_info = "";
	public String jobcard_info = "";
	public String insurance_info = "";
	public String insurance_followup_info = "";
	public String previous_calls_info = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String customer_id = "0";
	public String branch_service_appointment_email_enable = "0";
	public String branch_service_appointment_email_format = "";
	public String branch_service_appointment_email_sub = "";
	public String branch_service_appointment_sms_enable = "0";
	public String branch_service_appointment_sms_format = "";
	public String branch_service_appointment_email_exe_sub = "";
	public String branch_service_appointment_email_exe_format = "";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public Enquiry_Dash_Customer Customer_dash = new Enquiry_Dash_Customer();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_call_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				call_id = CNumeric(PadQuotes(request.getParameter("call_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
				call_veh_id = CNumeric(PadQuotes(request.getParameter("txt_veh_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_contact_id")));
				call_contact_id = CNumeric(PadQuotes(request.getParameter("call_contact_id")));
				if (!branch_id.equals("0") && add.equals("yes")) {
					call_branch_id = branch_id;
				}

				session_id = PadQuotes(request.getParameter("txt_session_id"));
				if (session_id.equals("") || session_id == null) {
					String key = "", possible = "0123456789";
					for (int i = 0; i < 9; i++) {
						key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
					}
					session_id = key;
				}

				// if (!call_veh_id.equals("0") && add.equals("yes")) {
				// StrSql = "SELECT contact_customer_id"
				// + " FROM " + compdb(comp_id) + "axela_service_call"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_customer_contact ON contact_customer_id = call_contact_id"
				// + " WHERE call_id = " + call_id + "";
				// // + " INNER JOIN " + compdb(comp_id) +
				// "axela_service_veh ON veh_customer_id" ;
				// CachedRowSet crs =processQuery(StrSql, 0);
				// while (crs.next()) {
				// customer_id = crs.getString("contact_customer_id");
				// }
				//
				// crs.close();
				if (!call_id.equals("0") && add.equals("yes")) {
					PopulateCallDetails(response);
					previous_calls_info = PopulatePreviousCalls();
					parent_call_id = call_id;

				}

				if (!veh_id.equals("0") && !contact_id.equals("0")) {
					PopulateVehicleDetails();
				}

				if (call_veh_id.equals("0")) {
					call_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				}

				if (add.equals("yes") && veh_id.equals("0")) {
					veh_info = new Vehicle_Dash().VehicleInfo(comp_id, veh_id);
					jobcard_info = new Vehicle_Dash().ListJobCardData(comp_id, veh_id);
					insurance_info = new Vehicle_Dash().ListInsurance(comp_id, veh_id);
					// insurance_followup_info = new Vehicle_Dash().ListInsuranceFollowup(veh_id, comp_id);
					previous_calls_info = PopulatePreviousCalls();
				}

				if (!call_veh_id.equals("0") && add.equals("yes") && veh_id.equals("0")) {
					PopulateVehicleDetails();
					veh_info = new Vehicle_Dash().VehicleInfo(comp_id, call_veh_id);
					jobcard_info = new Vehicle_Dash().ListJobCardData(comp_id, call_veh_id);
					insurance_info = new Vehicle_Dash().ListInsurance(comp_id, call_veh_id);
					// insurance_followup_info = new Vehicle_Dash().ListInsuranceFollowup(call_veh_id, comp_id);
					previous_calls_info = PopulatePreviousCalls();
				}

				if (!call_veh_id.equals("0") && add.equals("yes")) {
					try {
						StrSql = "SELECT booking_service_emp_id, booking_branch_id"
								+ " FROM " + compdb(comp_id) + "axela_service_booking"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_call ON call_id = booking_call_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
								+ " WHERE call_veh_id = " + call_veh_id + ""
								+ " ORDER BY call_id DESC LIMIT 1";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							booking_service_emp_id = crs.getString("booking_service_emp_id");
							call_branch_id = crs.getString("booking_branch_id");
						}
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
				}

				empEditperm = ReturnPerm(comp_id, "emp_service_call_edit", request);
				PopulateContactDetails();

				if (add.equals("yes")) {
					status = "Add";
					if (!"yes".equals(addB)) {
						call_prioritycall_id = "1";
						call_emp_id = emp_id;
						call_time = strToLongDate(ToLongDate(kknow()));
					} else {
						GetValues(request);
						if (ReturnPerm(comp_id, "emp_service_call_add", request).equals("1")) {
							call_entry_id = emp_id;
							call_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("call-list.jsp?call_id=" + call_id + "&msg=Call added successfully!" + add_msg));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Call")) {
						PopulateFields(request, response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Call")) {
						GetValues(request);
						if (ReturnPerm(comp_id, "emp_service_call_edit", request).equals("1")) {
							call_modified_id = emp_id;
							call_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("call-list.jsp?call_id=" + call_id + "&msg=Call updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					} else if (deleteB.equals("Delete Call")) {
						GetValues(request);
						if (ReturnPerm(comp_id, "emp_service_call_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("call-list.jsp?msg=Call deleted successfully!"));
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

	protected void GetValues(HttpServletRequest request) {
		call_branch_id = PadQuotes(request.getParameter("dr_call_branch_id"));
		call_emp_id = CNumeric(PadQuotes(request.getParameter("dr_call_emp_id")));
		booking_id = CNumeric(PadQuotes(request.getParameter("txt_booking_id")));
		booking_contact_id = CNumeric(PadQuotes(request.getParameter("dr_booking_contact_id")));
		booking_service_emp_id = CNumeric(PadQuotes(request.getParameter("dr_booking_service_emp_id")));
		booking_parking_id = CNumeric(PadQuotes(request.getParameter("dr_booking_parking_id")));
		call_booking_time = PadQuotes(request.getParameter("txt_call_booking_time"));
		booking_instruction = PadQuotes(request.getParameter("txt_booking_instruction"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
		call_veh_id = CNumeric(PadQuotes(request.getParameter("txt_veh_id")));
		contact_id = CNumeric(PadQuotes(request.getParameter("span_contact_id")));
		call_contact_id = CNumeric(PadQuotes(request.getParameter("call_contact_id")));
		if (!veh_id.equals("0") && !call_veh_id.equals(veh_id)) {
			call_veh_id = veh_id;
		}

		if (call_veh_id.equals("0")) {
			item_display = "none";
		}

		if (!contact_id.equals("0") && !call_contact_id.equals(contact_id)) {
			call_contact_id = contact_id;
		}

		if (call_contact_id.equals("0")) {
			customer_display = "none";
		}

		inventory_location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
		call_type_id = CNumeric(PadQuotes(request.getParameter("dr_call_type_id")));
		call_prioritycall_id = CNumeric(PadQuotes(request.getParameter("dr_call_prioritycall_id")));
		call_time = PadQuotes(request.getParameter("txt_call_time"));
		call_customer_voice = PadQuotes(request.getParameter("call_customer_voice"));
		if (call_customer_voice.length() > 2000) {
			call_customer_voice = call_customer_voice.substring(0, 1999);
		}

		call_followup_time = PadQuotes(request.getParameter("txt_call_followup_time"));
		call_entry_by = PadQuotes(request.getParameter("call_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		call_modified_by = PadQuotes(request.getParameter("call_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		// PickUp
		pickup_pickuptype_id = CNumeric(PadQuotes(request.getParameter("dr_pickup_pickuptype_id")));
		pickup_customer_id = CNumeric(PadQuotes(request.getParameter("pickup_customer_id")));
		pickup_emp_id = CNumeric(PadQuotes(request.getParameter("dr_pickup_emp_id")));
		pickup_location_id = CNumeric(PadQuotes(request.getParameter("dr_pickup_location_id")));
		pickup_contact_name = PadQuotes(request.getParameter("txt_pickup_contact_name"));
		pickup_mobile1 = PadQuotes(request.getParameter("txt_pickup_mobile1"));
		pickup_mobile2 = PadQuotes(request.getParameter("txt_pickup_mobile2"));
		pickup_add = PadQuotes(request.getParameter("txt_pickup_add"));
		pickup_landmark = PadQuotes(request.getParameter("txt_pickup_landmark"));
		pickup_instruction = PadQuotes(request.getParameter("txt_pickup_instruction"));
		// Courtesy
		courtesycar_courtesyveh_id = CNumeric(PadQuotes(request.getParameter("dr_vehicle")));
		if (!courtesycar_courtesyveh_id.equals("0") && add.equals("yes")) {
			courtesycar_customer_id = CNumeric(PadQuotes(request.getParameter("courtesycar_customer_id")));
			courtesycar_time_from = PadQuotes(request.getParameter("txt_courtesycar_startdate"));
			ccstartdate = ConvertLongDateToStr(courtesycar_time_from);
			courtesycar_time_to = PadQuotes(request.getParameter("txt_courtesycar_enddate"));
			ccenddate = ConvertLongDateToStr(courtesycar_time_to);
			courtesycar_contact_name = PadQuotes(request.getParameter("txt_courtesycar_contact_name"));
			courtesycar_mobile1 = PadQuotes(request.getParameter("txt_courtesycar_mobile1"));
			courtesycar_mobile2 = PadQuotes(request.getParameter("txt_courtesycar_mobile2"));
			courtesycar_add = PadQuotes(request.getParameter("txt_courtesycar_add"));
			courtesycar_landmark = PadQuotes(request.getParameter("txt_courtesycar_landmark"));
			courtesycar_notes = PadQuotes(request.getParameter("txt_courtesycar_notes"));
		}

		// For Particular Vehicle Showing Info..
		veh_info = new Vehicle_Dash().VehicleInfo(comp_id, call_veh_id);
		jobcard_info = new Vehicle_Dash_Jobcard().ListData(comp_id, call_veh_id);
		insurance_info = new Vehicle_Dash_Insurance().ListInsurance(call_veh_id, comp_id);
		insurance_followup_info = new Vehicle_Dash_Insurance_Followup().ListInsuranceFollowup(call_veh_id, comp_id);
		previous_calls_info = PopulatePreviousCalls();
		PopulateConfigDetails();
	}

	public void CheckForm() {
		msg = "";
		try {
			if (!call_booking_time.equals("")) {
				booking_mandatory = "1";
			}

			if (call_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (call_emp_id.equals("0")) {
				msg += "<br>Service Advisor!";
			}

			if (call_contact_id.equals("0") && add.equals("yes")) {
				msg += "<br>Select Contact!";
			}

			if (call_time.equals("")) {
				msg += "<br>Enter Call Time!";
			} else {
				if (isValidDateFormatLong(call_time)) {
					calltime = ConvertLongDateToStr(call_time);
				} else {
					msg += "<br>Enter valid Call Time!";
				}
			}

			if (call_type_id.equals("0")) {
				msg += "<br>Select Call Type!";
			} else if (call_type_id.equals("1") && call_followup_time.equals("")) {
				msg += "<br>Enter Follow-up Time!";
			}

			if (call_prioritycall_id.equals("0")) {
				msg += "<br>Select Priority!";
			}

			if (call_customer_voice.equals("")) {
				msg += "<br>Enter Customer Voice!";
			}

			if (!call_followup_time.equals("")) {
				if (isValidDateFormatLong(call_followup_time)) {
					callfollowuptime = ConvertLongDateToStr(call_followup_time);
				} else {
					msg += "<br>Enter valid Follow-up Time!";
				}

				if (!call_type_id.equals("1")) {
					msg += "<br>Call Type should be Reminder!";
				}
			}

			if (!call_followup_time.equals("") && !call_time.equals("")) {
				if (isValidDateFormatLong(call_followup_time) && isValidDateFormatLong(call_time)) {
					if (Long.parseLong(ConvertLongDateToStr(call_time)) > Long.parseLong(ConvertLongDateToStr(call_followup_time))) {
						msg += "<br>Follow-up Time should be greater than Call time!";
					}
				}
			}

			// ------- Booking deatils Validation...
			if (booking_mandatory.equals("1")) {
				if (call_veh_id.equals("0")) {
					msg += "<br>Select Vehicle for Booking!";
				}

				if (booking_contact_id.equals("0")) {
					msg += "<br>Select Booking Contact!";
				}

				if (booking_service_emp_id.equals("0")) {
					msg += "<br>Select Service Advisor!";
				}

				if (call_booking_time.equals("")) {
					msg += "<br>Enter Booking Time!";
				} else {
					if (isValidDateFormatLong(call_booking_time)) {
						callappttime = ConvertLongDateToStr(call_booking_time);
						// checking whether parking is OCCUPIED or Not....
						if (!booking_parking_id.equals("0")) {
							String parking_occupied = "0";
							StrSql = "SELECT DATE_FORMAT(DATE_SUB(booking_time, INTERVAL 1 HOUR), '%d/%m/%Y %h:%i') AS min_time,"
									+ " DATE_FORMAT(DATE_ADD(booking_time, INTERVAL 3 HOUR), '%d/%m/%Y %h:%i') AS max_time"
									+ " FROM " + compdb(comp_id) + "axela_service_booking"
									+ " WHERE booking_id != " + booking_id + ""
									+ " AND booking_branch_id = " + call_branch_id + ""
									+ " AND booking_parking_id = " + booking_parking_id + "";
							CachedRowSet crs = processQuery(StrSql, 0);

							while (crs.next()) {
								if (Long.parseLong(callappttime) > Long.parseLong(ConvertLongDateToStr(crs.getString("min_time")))
										&& Long.parseLong(callappttime) < Long.parseLong(ConvertLongDateToStr(crs.getString("max_time")))) {
									parking_occupied = "1";
								}
							}
							crs.close();

							if (parking_occupied.equals("1")) {
								msg += "<br>Parking is already Occupied!";
							}
						}
						// -------
					} else {
						msg += "<br>Enter valid Booking Time!";
					}
				}

				if (!call_booking_time.equals("") && !call_time.equals("")) {
					if (isValidDateFormatLong(call_booking_time) && isValidDateFormatLong(call_time)) {
						if (Long.parseLong(ConvertLongDateToStr(call_time)) > Long.parseLong(ConvertLongDateToStr(call_booking_time))) {
							msg += "<br>Booking Time should be greater than Call time!";
						}
					}
				}

				if (booking_parking_id.equals("0")) {
					msg += "<br>Select Booking Parking Lot!";
				}

				if (booking_instruction.equals("")) {
					msg += "<br>Enter Booking Instruction!";
				}
			}
			// --------------

			// Checking for PickUp
			if (!pickup_location_id.equals("0") && add.equals("yes")) {
				// StrSql = "SELECT location_leadtime, location_inspection_dur"
				// + " FROM " + compdb(comp_id) + "axela_service_location"
				// + " WHERE location_id = " + pickup_location_id + "";
				// CachedRowSet crs =processQuery(StrSql, 0);
				//
				// if (crs.isBeforeFirst()) {
				// while (crs.next()) {
				// pickup_time_from =
				// ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(call_booking_time)),
				// 0, 0, -crs.getDouble("location_leadtime")));
				// pickup_time_to =
				// ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(call_booking_time)),
				// 0, 0, crs.getDouble("location_inspection_dur")));
				// }
				//
				// StrSql = "SELECT pickup_time_from FROM " + compdb(comp_id) +
				// "axela_service_pickup"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_service_location ON location_id = pickup_location_id"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_service_pickup_type ON pickuptype_id = pickup_pickuptype_id"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_customer ON customer_id = pickup_customer_id"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_customer_contact ON contact_id = pickup_contact_id"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_branch ON branch_id = pickup_branch_id"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_emp ON emp_id = pickup_emp_id"
				// + " WHERE pickup_emp_id = " + pickup_emp_id + ""
				// + " AND ((pickup_time_from > " + pickup_time_from + ""
				// + " AND pickup_time_from < " + pickup_time_to + ")"
				// + " OR (pickup_time_to > " + pickup_time_from + ""
				// + " AND pickup_time_to < " + pickup_time_to + "))";
				// if (!ExecuteQuery(StrSql).equals("")) {
				// msg += "<br>Driver is occupied for other Pickup!";
				// }
				// }
				// crs.close();

				if (pickup_mobile1.equals("")) {
					msg += "<br>Enter Mobile1 for Pickup!";
				}

				if (!pickup_mobile1.equals("") && !IsValidMobileNo11(pickup_mobile1)) {
					msg += "<br>Mobile1 is invalid for Pickup!";
				}

				if (!pickup_mobile2.equals("") && !IsValidMobileNo11(pickup_mobile2)) {
					msg += "<br>Mobile2 is invalid for Pickup!";
				}

				if (pickup_add.equals("")) {
					msg += "<br>Enter Address for Pickup!";
				}

				// if (pickup_landmark.equals("")) {
				// msg += "<br>Enter Landmark for Pickup!";
				// }
			}

			// Checking for Courtesy car
			if (!courtesycar_courtesyveh_id.equals("0")) {
				if (courtesycar_time_from.equals("")) {
					msg += "<br>Select Start Time for Courtesy!";
				} else {
					if (isValidDateFormatLong(courtesycar_time_from)) {
						ccstartdate = ConvertLongDateToStr(courtesycar_time_from);
					} else {
						msg += "<br>Enter valid Start Time for Courtesy!";
					}
				}

				if (!courtesycar_time_from.equals("") && !courtesycar_time_to.equals("")) {
					if (isValidDateFormatLong(courtesycar_time_to) && isValidDateFormatLong(courtesycar_time_to)) {
						if (Long.parseLong(ConvertLongDateToStr(courtesycar_time_to)) < Long.parseLong(ConvertLongDateToStr(courtesycar_time_from))) {
							msg += "<br>Courtesy End Time should be greater than Courtesy Start Time!";
						}
					}
				}

				if (courtesycar_time_to.equals("")) {
					msg += "<br>Select End Time for Courtesy!";
				} else {
					if (isValidDateFormatLong(courtesycar_time_to)) {
						ccenddate = ConvertLongDateToStr(courtesycar_time_to);
					} else {
						msg += "<br>Enter valid End Time for Courtesy!";
					}
				}

				if (!courtesycar_time_from.equals("") && !courtesycar_time_to.equals("")) {
					if (isValidDateFormatLong(courtesycar_time_from) && isValidDateFormatLong(courtesycar_time_to)) {
						StrSql = "SELECT courtesycar_time_from FROM " + compdb(comp_id) + "axela_service_courtesy_car"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = courtesycar_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
								+ " WHERE courtesycar_courtesyveh_id = " + courtesycar_courtesyveh_id + ""
								+ " AND ((courtesycar_time_from > " + ConvertLongDateToStr(courtesycar_time_from) + ""
								+ " AND courtesycar_time_from < " + ConvertLongDateToStr(courtesycar_time_to) + ")"
								+ " OR (courtesycar_time_to > " + ConvertLongDateToStr(courtesycar_time_from) + ""
								+ " AND courtesycar_time_to < " + ConvertLongDateToStr(courtesycar_time_to) + "))";
						if (!ExecuteQuery(StrSql).equals("")) {
							msg += "<br>Courtesy Car is occupied!";
						}
					}
				}

				if (courtesycar_mobile1.equals("")) {
					msg += "<br>Enter Mobile1 for Courtesy!";
				} else if (!IsValidMobileNo11(courtesycar_mobile1)) {
					msg += "<br>Mobile1 is invalid for Courtesy!";
				}

				if (!courtesycar_mobile2.equals("") && !IsValidMobileNo11(courtesycar_mobile2)) {
					msg += "<br>Mobile2 is invalid for Courtesy!";
				}

				if (courtesycar_add.equals("")) {
					msg += "<br>Enter Address for Courtesy!";
				}

				if (courtesycar_landmark.equals("")) {
					msg += "<br>Enter Landmark for Courtesy!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_call"
						+ " (call_veh_id,"
						+ " call_branch_id,"
						+ " call_emp_id,"
						+ " call_contact_id,"
						+ " call_type_id,"
						+ " call_time,"
						+ " call_customer_voice,"
						+ " call_followup_time,"
						+ " call_prioritycall_id,"
						+ " call_entry_id,"
						+ " call_entry_date)"
						+ " VALUES"
						+ " (" + call_veh_id + ","
						+ " " + call_branch_id + ","
						+ " " + call_emp_id + ","
						+ " " + call_contact_id + ","
						+ " " + call_type_id + ","
						+ " '" + calltime + "',"
						+ " '" + call_customer_voice + "',"
						+ " '" + callfollowuptime + "',"
						+ " " + call_prioritycall_id + ","
						+ " " + call_entry_id + ","
						+ " '" + call_entry_date + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					call_id = rs.getString(1);
				}

				if (!parent_call_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call"
							+ " SET"
							+ " call_call_id = " + call_id + ""
							+ " WHERE call_id = " + parent_call_id + "";
					stmttx.addBatch(StrSql);
				}

				// Add the appointment if the appointment time is given for the
				// call
				if (booking_mandatory.equals("1")) {
					AddAppt();

					// Add the Pickup details if the location is selected for
					// the pickup
					if (!pickup_location_id.equals("0") && !CNumeric(booking_id).equals("0")) {
						AddPickups();
					}
				}

				// Add the Courtesy details if the courtesy vehicle is selected
				if (!courtesycar_courtesyveh_id.equals("0")) {
					AddCourtesyCars();
				}
				stmttx.executeBatch();

				// if (!call_id.equals("0")) {
				// if (!call_booking_time.equals("")) {
				// if (comp_email_enable.equals("1")
				// && config_email_enable.equals("1")
				// && !config_admin_email.equals("")
				// && branch_service_appointment_email_enable.equals("1")) {
				// if (!contact_email1.equals("")
				// && !branch_service_appointment_email_format.equals("")
				// && !branch_service_appointment_email_sub.equals("")) {
				// SendEmail();
				// }
				// }
				// if (comp_sms_enable.equals("1") &&
				// config_sms_enable.equals("1") &&
				// branch_service_appointment_sms_enable.equals("1")) {
				// if (!branch_service_appointment_sms_format.equals("") &&
				// !contact_mobile1.equals("")) {
				// SendSMS();
				// }
				// }
				// }
				// }
				// Delete Old cart items
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_booking_cart"
						+ " WHERE bookingcart_session_id = " + session_id + "";
				stmttx.execute(StrSql);

				conntx.commit();

				if (msg.equals("")) {
					// for blocking the appt items, update the stock table
					// UpdateStock(comp_id, "0", "0");
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
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
	}

	public void AddAppt() throws Exception {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_booking"
					+ " (booking_branch_id,"
					+ " booking_call_id,"
					+ " booking_contact_id,"
					+ " booking_time,"
					+ " booking_instruction,"
					+ " booking_veh_id,"
					+ " booking_service_emp_id,"
					+ " booking_parking_id,"
					+ " booking_active,"
					+ " booking_entry_id,"
					+ " booking_entry_date)"
					+ " VALUES"
					+ " (" + call_branch_id + ","
					+ " " + call_id + ","
					+ " " + booking_contact_id + ","
					+ " '" + callappttime + "',"
					+ " '" + booking_instruction + "',"
					+ " " + call_veh_id + ","
					+ " " + booking_service_emp_id + ","
					+ " " + booking_parking_id + ","
					+ " '1',"
					+ " " + call_entry_id + ","
					+ " '" + call_entry_date + "')";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs1 = stmttx.getGeneratedKeys();

			while (rs1.next()) {
				booking_id = rs1.getString(1);
			}

			/*
			 * After adding the appointment, block all the items for that appointment by adding into the apptitem table and update the Stock
			 */
			if (!booking_id.equals("0") && add.equals("yes")) {
				AddItemFields();
			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddPickups() throws Exception {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_pickup"
					+ " (pickup_branch_id,"
					+ " pickup_booking_id,"
					+ " pickup_veh_id,"
					+ " pickup_customer_id,"
					+ " pickup_contact_id,"
					+ " pickup_emp_id,"
					+ " pickup_pickuptype_id,"
					+ " pickup_time,"
					+ " pickup_time_from,"
					+ " pickup_time_to,"
					+ " pickup_location_id,"
					+ " pickup_add,"
					+ " pickup_landmark,"
					+ " pickup_contact_name,"
					+ " pickup_mobile1,"
					+ " pickup_mobile2,"
					+ " pickup_instruction,"
					+ " pickup_active,"
					+ " pickup_notes,"
					+ " pickup_entry_id,"
					+ " pickup_entry_date)"
					+ " VALUES"
					+ " (" + call_branch_id + ","
					+ " " + booking_id + ","
					+ " " + call_veh_id + ","
					+ " " + pickup_customer_id + ","
					+ " " + call_contact_id + ","
					+ " " + pickup_emp_id + ","
					+ " " + pickup_pickuptype_id + ","
					+ " '" + callappttime + "',"
					+ " '" + pickup_time_from + "',"
					+ " '" + pickup_time_to + "',"
					+ " " + pickup_location_id + ","
					+ " '" + pickup_add + "',"
					+ " '" + pickup_landmark + "',"
					+ " '" + pickup_contact_name + "',"
					+ " '" + pickup_mobile1 + "',"
					+ " '" + pickup_mobile2 + "',"
					+ " '" + pickup_instruction + "',"
					+ " '1',"// pickup_active
					+ " '',"// pickup_notes
					+ " " + call_entry_id + ","
					+ " '" + call_entry_date + "')";
			stmttx.addBatch(StrSql);

			add_msg = "<br>Pickup added successfully!";
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddCourtesyCars() throws Exception {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " (courtesycar_customer_id,"
					+ " courtesycar_branch_id,"
					+ " courtesycar_booking_id,"
					+ " courtesycar_contact_id,"
					+ " courtesycar_courtesyveh_id,"
					+ " courtesycar_time_from,"
					+ " courtesycar_time_to,"
					+ " courtesycar_contact_name,"
					+ " courtesycar_landmark,"
					+ " courtesycar_notes,"
					+ " courtesycar_mobile1,"
					+ " courtesycar_mobile2,"
					+ " courtesycar_add,"
					+ " courtesycar_active,"
					+ " courtesycar_entry_id,"
					+ " courtesycar_entry_date)"
					+ " VALUES"
					+ " (" + courtesycar_customer_id + ","
					+ " " + call_branch_id + ","
					+ " " + booking_id + ","
					+ " " + call_contact_id + ","
					+ " " + courtesycar_courtesyveh_id + ","
					+ " '" + ccstartdate + "',"
					+ " '" + ccenddate + "',"
					+ " '" + courtesycar_contact_name + "',"
					+ " '" + courtesycar_landmark + "',"
					+ " '" + courtesycar_notes + "',"
					+ " '" + courtesycar_mobile1 + "',"
					+ " '" + courtesycar_mobile2 + "',"
					+ " '" + courtesycar_add + "',"
					+ " '1',"// courtesycar_active
					+ " " + call_entry_id + ","
					+ " '" + call_entry_date + "')";
			stmttx.addBatch(StrSql);

			add_msg += "<br>Courtesy Car added successfully!";
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddItemFields() throws SQLException {
		try {
			StrSql = "SELECT"
					+ " " + booking_id + ","
					+ " bookingcart_item_id,"
					+ " bookingcart_location_id,"
					+ " bookingcart_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_booking_cart"
					+ " WHERE bookingcart_bookingitem_id = 0"
					+ " AND bookingcart_session_id = " + session_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_booking_item"
					+ " (bookingitem_booking_id,"
					+ " bookingitem_item_id,"
					+ " bookingitem_location_id,"
					+ " bookingitem_qty)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
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
		}
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT call_emp_id, contact_id, customer_id, customer_name, call_contact_id, contact_email1,"
					+ " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no, contact_email2,"
					+ " call_veh_id, call_type_id, contact_lname, call_branch_id, call_call_id, title_desc,"
					+ " COALESCE(booking_id, 0) AS booking_id, COALESCE(booking_time, '') AS booking_time,"
					+ " COALESCE(booking_contact_id, 0) AS  booking_contact_id, contact_fname, contact_title_id,"
					+ " COALESCE(booking_instruction, '') AS booking_instruction, call_entry_date, call_modified_date,"
					+ " COALESCE(booking_service_emp_id, 0) AS  booking_service_emp_id, call_prioritycall_id,"
					+ " COALESCE(booking_parking_id, 0) AS  booking_parking_id, call_entry_id, call_modified_id,"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
					+ " COALESCE(item_id, 0) AS item_id, call_customer_voice, call_time, call_followup_time"
					+ " FROM " + compdb(comp_id) + "axela_service_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_booking ON booking_call_id = call_id"
					+ " WHERE call_id = " + call_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_display = "";
					call_branch_id = crs.getString("call_branch_id");
					call_emp_id = crs.getString("call_emp_id");
					contact_id = crs.getString("contact_id");
					customer_id = crs.getString("customer_id");
					call_contact_id = crs.getString("call_contact_id");
					call_veh_id = crs.getString("call_veh_id");
					call_type_id = crs.getString("call_type_id");
					call_time = strToLongDate(crs.getString("call_time"));

					call_customer_voice = crs.getString("call_customer_voice");
					call_prioritycall_id = crs.getString("call_prioritycall_id");
					call_followup_time = strToLongDate(crs.getString("call_followup_time"));
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");

					// Appt Details
					booking_id = crs.getString("booking_id");
					booking_instruction = crs.getString("booking_instruction");
					call_booking_time = strToLongDate(crs.getString("booking_time"));
					booking_contact_id = crs.getString("booking_contact_id");
					booking_service_emp_id = crs.getString("booking_service_emp_id");
					booking_parking_id = crs.getString("booking_parking_id");

					veh_id = crs.getString("veh_id");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					SOP("link_customer_name===" + link_customer_name);
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id")
							+ "\">" + crs.getString("title_desc") + " " + crs.getString("contact_fname")
							+ " " + crs.getString("contact_lname") + "</a>";
					SOP("link_contact_name===" + link_contact_name);
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id="
							+ crs.getString("veh_id") + "\">" + SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
					SOP("link_vehicle_name===" + link_vehicle_name);
					item_id = crs.getString("item_id");
					item_name = crs.getString("item_name");
					if (!item_name.equals("")) {
						item_display = "";
					}

					call_entry_id = crs.getString("call_entry_id");
					if (!call_entry_id.equals("")) {
						call_entry_by = Exename(comp_id, Integer.parseInt(call_entry_id));
					}

					entry_date = strToLongDate(crs.getString("call_entry_date"));
					call_modified_id = crs.getString("call_modified_id");
					if (!call_modified_id.equals("0")) {
						call_modified_by = Exename(comp_id, Integer.parseInt(call_modified_id));
						modified_date = strToLongDate(crs.getString("call_modified_date"));
					}
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Call!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !call_contact_id.equals("0")) {
				StrSql = "SELECT contact_id, contact_email1, contact_email2, contact_fname,"
						+ " contact_lname, title_desc, customer_name, contact_mobile1, customer_id"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " WHERE 1 = 1";

				if (!contact_id.equals("0")) {
					StrSql += " AND contact_id = " + contact_id + "";
				} else if (!call_contact_id.equals("0")) {
					StrSql += " AND contact_id = " + call_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					customer_display = "";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateExecutive(String call_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Service Advisor</option>\n");

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1";
			if (add.equals("yes")) {
				StrSql += " AND emp_active = 1";
			}
			StrSql += " AND (emp_service = 1"
					+ " OR emp_service_psf = 1"
					+ " OR emp_crm = 1)"
					+ " AND (emp_branch_id = " + call_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + call_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), call_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call"
						+ " SET"
						+ " call_branch_id = " + call_branch_id + ","
						+ " call_emp_id = " + call_emp_id + ","
						+ " call_veh_id = " + call_veh_id + ","
						+ " call_contact_id = " + call_contact_id + ","
						+ " call_type_id = " + call_type_id + ",";
				if (empEditperm.equals("1")) {
					StrSql += " call_time = '" + calltime + "',";
				}

				StrSql += " call_followup_time = '" + callfollowuptime + "',"
						+ " call_prioritycall_id = " + call_prioritycall_id + ","
						+ " call_customer_voice = '" + call_customer_voice + "',"
						+ " call_modified_id = " + call_modified_id + ","
						+ " call_modified_date = '" + call_modified_date + "'"
						+ " WHERE call_id = " + call_id + "";
				stmttx.execute(StrSql);

				if (booking_id.equals("0")) {
					AddAppt();
				} else if (!booking_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_booking"
							+ " SET"
							+ " booking_branch_id = " + call_branch_id + ","
							+ " booking_call_id = " + call_id + ","
							+ " booking_contact_id = " + booking_contact_id + ","
							+ " booking_time = '" + callappttime + "',"
							+ " booking_instruction = '" + booking_instruction + "',"
							+ " booking_service_emp_id = " + booking_service_emp_id + ","
							+ " booking_parking_id = " + booking_parking_id + ","
							+ " booking_modified_id = " + call_modified_id + ","
							+ " booking_modified_date = '" + call_modified_date + "'"
							+ " WHERE booking_call_id = " + call_id + "";
					stmttx.execute(StrSql);
				}
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
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
	}

	protected void SendEmail() throws SQLException {
		// emailmsg = (branch_service_appointment_email_format);
		// emailsub = (branch_service_appointment_email_sub);

		emailsub = "REPLACE(branch_service_appointment_email_sub, '[CALLID]', call_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]', customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]', customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]', contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]', contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]', contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL2]', contact_email2)";
		emailsub = "REPLACE(" + emailsub + ", '[EXENAME]', emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEJOBTITLE]', jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEMOBILE1]', emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEPHONE1]', emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEEMAIL1]', emp_email1)";

		emailmsg = "REPLACE(branch_service_appointment_email_format, '[CALLID]', call_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]', customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]', customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]', contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]', contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL2]', contact_email2)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXENAME]', emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEMOBILE1]', emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEPHONE1]', emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEEMAIL1]', emp_email1)";
		try {
			String email_to = contact_email1;
			if (!contact_email2.equals("")) {
				email_to += "," + contact_email2;
			}

			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + call_contact_id + "',"
					+ " '" + contact_fname + " " + contact_lname + "',"
					+ " '" + config_admin_email + "',"
					+ " '" + email_to + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_service_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " WHERE call_id = " + call_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS() throws SQLException {
		// smsmsg = (branch_service_appointment_sms_format);

		smsmsg = "REPLACE(branch_service_appointment_sms_format, '[CALLID]', call_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]', customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]', customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]', contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]', contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL2]', contact_email2)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXENAME]', emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEMOBILE1]', emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEPHONE1]', emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEEMAIL1]', emp_email1)";
		try {
			StrSql = "SELECT "
					+ " " + branch_id + ","
					+ " " + call_contact_id + ","
					+ " '" + contact_fname + " " + contact_lname + "',"
					+ " '" + contact_mobile1 + "',"
					+ " " + smsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " WHERE call_id = " + call_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void DeleteFields() throws Exception {
		StrSql = "SELECT booking_id FROM " + compdb(comp_id) + "axela_service_booking"
				+ " WHERE booking_call_id = " + call_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Call is associated with Booking!";
		}

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// Set the child call_id as '0' for parent calls before deleting
				StrSql = "SELECT call_call_id FROM " + compdb(comp_id) + "axela_service_call"
						+ " WHERE call_call_id = " + call_id + "";
				if (ExecuteQuery(StrSql).equals(call_id)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call"
							+ " SET"
							+ " call_call_id = 0"
							+ " WHERE call_call_id = " + call_id + "";
					stmttx.execute(StrSql);
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_call"
						+ " WHERE call_id = " + call_id + "";
				stmttx.execute(StrSql);

				conntx.commit();
			} catch (Exception ex) {
				if (conntx.isClosed()) {
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public String PopulateCallType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT calltype_id, calltype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_call_type"
					+ " ORDER BY calltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("calltype_id"));
				Str.append(StrSelectdrop(crs.getString("calltype_id"), call_type_id));
				Str.append(">").append(crs.getString("calltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCallPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prioritycall_id, prioritycall_name"
					+ " FROM " + compdb(comp_id) + "axela_service_call_priority"
					+ " GROUP BY prioritycall_id"
					+ " ORDER BY prioritycall_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prioritycall_id"));
				Str.append(StrSelectdrop(crs.getString("prioritycall_id"), call_prioritycall_id));
				Str.append(">").append(crs.getString("prioritycall_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void PopulateVehicleDetails() {
		try {
			if (!veh_id.equals("0") || !call_veh_id.equals("0")) {
				StrSql = "SELECT veh_branch_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name, item_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
						+ " contact_mobile1, contact_mobile2, contact_address, contact_landmark, customer_id,"
						+ " COALESCE(veh_variant_id, 0) AS veh_variant_id, veh_reg_no, veh_id, contact_id"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " WHERE 1 = 1";
				if (!veh_id.equals("0")) {
					StrSql += " AND veh_id = " + veh_id + "";
				} else if (!call_veh_id.equals("0")) {
					StrSql += " AND veh_id = " + call_veh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_display = "";
					item_display = "";
					call_branch_id = crs.getString("veh_branch_id");
					pickup_customer_id = crs.getString("customer_id");
					courtesycar_customer_id = crs.getString("customer_id");
					call_contact_id = crs.getString("contact_id");
					item_name = crs.getString("item_name");
					item_id = crs.getString("item_id");
					pickup_contact_name = crs.getString("contactname");
					courtesycar_contact_name = crs.getString("contactname");
					pickup_mobile1 = crs.getString("contact_mobile1");
					courtesycar_mobile1 = crs.getString("contact_mobile1");
					pickup_mobile2 = crs.getString("contact_mobile2");
					courtesycar_mobile2 = crs.getString("contact_mobile2");
					pickup_add = crs.getString("contact_address");
					courtesycar_add = crs.getString("contact_address");
					pickup_landmark = crs.getString("contact_landmark");
					courtesycar_landmark = crs.getString("contact_landmark");
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">"
							+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id") + "\">" + crs.getString("contactname") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePreviousCalls() {
		String StrSearch = "";
		StringBuilder Str = new StringBuilder();
		if (!call_veh_id.equals("0")) {
			try {
				StrSearch = " AND call_veh_id = " + call_veh_id + "";

				StrSql = "SELECT call_time, call_customer_voice, call_entry_id,"
						+ " call_emp_id, emp_name, emp_id, call_entry_date"
						+ " FROM " + compdb(comp_id) + "axela_service_call"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE 1 = 1" + StrSearch + ""
						+ " ORDER BY call_time DESC"
						+ " LIMIT 10";
				CachedRowSet crs = processQuery(StrSql, 0);

				int count = 0;
				if (crs.isBeforeFirst()) {
					Str.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\" class=\"listtable\">\n");
					Str.append("<tr>\n<th>").append("#");
					Str.append("</th>\n<th>").append("Call Time");
					Str.append("</th>\n<th>").append("Customer Voice");
					Str.append("</th>\n<th>").append("Service Advisor");
					Str.append("</th>\n<th>").append("Entry By");
					Str.append("</th>\n</tr>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n<td valign=\"top\" align=\"center\">").append(count);
						Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("call_time")));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("call_customer_voice"));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(Exename(comp_id, Integer.parseInt(crs.getString("call_emp_id"))));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(Exename(comp_id, Integer.parseInt(crs.getString("call_entry_id"))));
						Str.append("<br>").append(strToLongDate(crs.getString("call_entry_date")));
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
				} else {
					Str.append("<br><br><br><br><b><font color=\"red\">No Previous Calls found!</font></b><br><br><br><br>");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			Str.append("<br><br><br><br><b><font color=\"red\">No Previous Calls found!</font></b><br><br><br><br>");
		}
		return Str.toString();
	}

	public void PopulateCallDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT call_id, call_emp_id, contact_id, call_contact_id, COALESCE(veh_id, 0) AS veh_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, customer_id,"
					+ " contact_mobile1, contact_mobile2, contact_address, contact_pin, contact_landmark,"
					+ " call_veh_id, call_type_id, call_branch_id, call_customer_voice, call_time,"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
					+ " call_prioritycall_id, COALESCE(veh_reg_no, '') AS veh_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_service_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " WHERE call_id = " + call_id + BranchAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_display = "";
					call_branch_id = crs.getString("call_branch_id");
					call_emp_id = crs.getString("call_emp_id");
					contact_id = crs.getString("contact_id");
					pickup_customer_id = crs.getString("customer_id");
					courtesycar_customer_id = crs.getString("customer_id");
					call_contact_id = crs.getString("call_contact_id");
					pickup_contact_name = crs.getString("contactname");
					courtesycar_contact_name = crs.getString("contactname");
					pickup_mobile1 = crs.getString("contact_mobile1");
					courtesycar_mobile1 = crs.getString("contact_mobile1");
					pickup_mobile2 = crs.getString("contact_mobile2");
					courtesycar_mobile2 = crs.getString("contact_mobile2");
					pickup_add = crs.getString("contact_address");
					courtesycar_add = crs.getString("contact_address");
					pickup_landmark = crs.getString("contact_landmark");
					courtesycar_landmark = crs.getString("contact_landmark");
					call_veh_id = crs.getString("call_veh_id");
					veh_reg_no = crs.getString("veh_reg_no");
					call_time = strToLongDate(crs.getString("call_time"));
					item_name = crs.getString("item_name");
					if (!item_name.equals("")) {
						item_display = "";
					}

					call_prioritycall_id = crs.getString("call_prioritycall_id");
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">" + SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id")
							+ "\">" + crs.getString("contactname") + "</a>";
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Call!"));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateServiceExecutive(String booking_branch_id, String booking_time, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String StrSearch = "";
		int day = 0;
		if (!booking_time.equals("") && isValidDateFormatLong(booking_time)) {
			day = ReturnDayOfWeek(ConvertLongDateToStr(booking_time));
			// StrSearch = WorkDaysStr(day);
		}
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS empname"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE (emp_active = 1"
					+ " AND emp_branch_id = " + booking_branch_id + ""
					+ " AND emp_service = 1" + StrSearch + ")"
					+ " OR emp_id = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), booking_service_emp_id));
				Str.append(">").append(crs.getString("empname")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	//

	public String WorkDaysStr(int day) {
		String WorkDayStr = "";
		switch (day) {
			case 1 :
				WorkDayStr = " AND emp_workday_sunday = 1";
				break;
			case 2 :
				WorkDayStr = " AND emp_workday_monday = 1";
				break;
			case 3 :
				WorkDayStr = " AND emp_workday_tuesday = 1";
				break;
			case 4 :
				WorkDayStr = " AND emp_workday_wednesday = 1";
				break;
			case 5 :
				WorkDayStr = " AND emp_workday_thursday = 1";
				break;
			case 6 :
				WorkDayStr = " AND emp_workday_friday = 1";
				break;
			case 7 :
				WorkDayStr = " AND emp_workday_satday = 1";
				break;
		}
		return WorkDayStr;
	}

	public String PopulatePickUpLocation(String branch_id, String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_service_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(Selectdrop(crs.getInt("location_id"), pickup_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePickupType() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT pickuptype_id, pickuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_pickup_type"
					+ " GROUP BY pickuptype_id"
					+ " ORDER BY pickuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("pickuptype_id"));
				Str.append(Selectdrop(crs.getInt("pickuptype_id"), pickup_pickuptype_id));
				Str.append(">").append(crs.getString("pickuptype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDriver() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_pickup_driver"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_pickup_driver = '1'"
					+ " AND emp_branch_id = " + call_branch_id + ""
					+ " AND emp_active = '1'"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Driver</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), pickup_emp_id));
				Str.append(">").append(crs.getString("emp_pickup_driver")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateParking(String parking_branch_id, String call_booking_time, String booking_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		String booking_time = "";
		String befortime = "1", aftertime = "3";
		try {
			if (!call_booking_time.equals("")) {
				booking_time = ConvertLongDateToStr(call_booking_time);
				StrSql = "SELECT parking_id, parking_name, parking_rank"
						+ " FROM " + compdb(comp_id) + "axela_service_parking"
						+ " WHERE parking_branch_id = " + parking_branch_id + ""
						+ " AND parking_id NOT IN (SELECT booking_parking_id"
						+ " FROM " + compdb(comp_id) + "axela_service_booking"
						+ " WHERE (((DATE_FORMAT(DATE_SUB(booking_time, INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')"
						+ " <= DATE_FORMAT(DATE_SUB('" + booking_time + "', INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s'))"
						+ " AND (DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s')"
						+ " >= DATE_FORMAT(DATE_SUB('" + booking_time + "', INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')))"
						+ " OR ((DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')"
						+ " <= DATE_FORMAT(DATE_ADD('" + booking_time + "', INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s'))"
						+ " AND (DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s')"
						+ " >= DATE_FORMAT(DATE_ADD('" + booking_time + "', INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s'))))"
						+ " AND booking_id != " + booking_id + ")"
						+ " AND parking_active = 1"
						+ " GROUP BY parking_id"
						+ " ORDER BY parking_rank";
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						Str.append("<option value=").append(crs.getString("parking_id"));
						Str.append(Selectdrop(crs.getInt("parking_id"), booking_parking_id));
						Str.append(">").append(crs.getString("parking_name")).append("</option>\n");
					}
				} else {
					Str.append("<option value=\"0\">Select Parking Lot</option>\n");
				}
				crs.close();
			} else {
				Str.append("<option value=\"0\">Select Parking Lot</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(branch_service_appointment_email_enable, '') AS branch_service_appointment_email_enable,"
					+ " COALESCE(branch_service_appointment_sms_format, '') AS branch_service_appointment_sms_format,"
					+ " COALESCE(branch_service_appointment_sms_enable, '') AS branch_service_appointment_sms_enable,"
					+ " COALESCE(branch_service_appointment_email_sub, '') AS branch_service_appointment_email_sub,"
					+ " COALESCE(branch_service_appointment_email_format, '') AS branch_service_appointment_email_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " config_customer_dupnames, comp_email_enable, comp_sms_enable"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + call_branch_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				branch_service_appointment_email_enable = crs.getString("branch_service_appointment_email_enable");
				branch_service_appointment_email_format = crs.getString("branch_service_appointment_email_format");
				branch_service_appointment_sms_enable = crs.getString("branch_service_appointment_sms_enable");
				branch_service_appointment_sms_format = crs.getString("branch_service_appointment_sms_format");
				branch_service_appointment_email_sub = crs.getString("branch_service_appointment_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCourtesyVehicle(String courtesyveh_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
					+ " WHERE courtesyveh_branch_id = " + courtesyveh_branch_id + ""
					+ " AND courtesyveh_active = 1"
					+ " ORDER BY courtesyveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("courtesyveh_id"));
				Str.append(StrSelectdrop(crs.getString("courtesyveh_id"), courtesycar_courtesyveh_id));
				Str.append(">").append(crs.getString("courtesyveh_name")).append(" - ");
				Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	/* Using in Vehicle Dash Page for StockStatus Tab */
	public String PopulateLocation(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name, location_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Location</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"), inventory_location_id));
				Str.append(">").append(crs.getString("location_name")).append(" (");
				Str.append(crs.getString("location_code")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBookingContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT contact_id, contacttype_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " WHERE customer_id = " + customer_id + ""
					+ " GROUP BY contact_id"
					+ " ORDER BY contacttype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("contact_id"));
					Str.append(StrSelectdrop(crs.getString("contact_id"), booking_contact_id));
					Str.append(">").append(crs.getString("contactname")).append(" (");
					Str.append(crs.getString("contacttype_name")).append(")</option>\n");
				}
			} else {
				Str.append("<option value=\"0\">Select Contact</option>\n");
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
