package axela.service;
//aJIt 13th November, 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Booking_Dash extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String branch_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String submitB = "";
	public String delete = "";
	public String booking_id = "0";
	public String booking_branch_id = "0";
	public String booking_call_id = "0";
	public String booking_contact_id = "0";
	public String booking_time = "";
	public String apptappttime = "";
	public String item_display = "none";
	public String link_vehicle_name = "";
	public String link_item_name = "";
	public String booking_entry_id = "0";
	public String booking_entry_date = "";
	public String booking_entry_by = "";
	public String entry_date = "";
	public String emp_role_id = "0";
	public String booking_modified_id = "0";
	public String booking_modified_date = "";
	public String modified_date = "";
	public String booking_modified_by = "";
	public String veh_id = "0";
	public String veh_reg_no = "";
	public String item_id = "0";
	public String item_name = "";
	public String booking_service_emp_id = "0";
	public String booking_parking_id = "0";
	public String booking_active = "";
	public String customer_id = "0";
	// pickup Starts
	public String pickup_id = "0";
	public String pickup_active = "0";
	public String pickup_emp_id = "0";
	public String pickup_time_from = "0";
	public String pickup_time_to = "0";
	public String pickup_location_id = "0";
	public String pickup_pickuptype_id = "0";
	public String pickup_contact_name = "";
	public String pickup_mobile1 = "", pickup_mobile2 = "";
	public String pickup_add = "";
	public String pickup_landmark = "";
	public String pickup_instruction = "";
	// courtesy car
	public String courtesycar_id = "0";
	public String courtesy_active = "0";
	public String courtesycar_time_from = "", courtesycar_time_to = "";
	public String ccstartdate = "";
	public String ccenddate = "";
	public String courtesycar_contact_id = "0";
	public String courtesycar_contact_name = "";
	public String courtesycar_courtesyveh_id = "";
	public String courtesycar_mobile1 = "", courtesycar_mobile2 = "";
	public String courtesycar_add = "";
	public String courtesycar_landmark = "";
	public String courtesycar_notes = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
				if (!booking_id.equals("0")) {
					GetApptDetails();
				}

				if ("yes".equals(updateB)) {
					GetValues(request);
					booking_modified_id = emp_id;
					booking_modified_date = ToLongDate(kknow());
					UpdateFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						GetApptDetails();
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void GetApptDetails() {
		try {
			StrSql = "SELECT booking_branch_id, booking_contact_id, booking_veh_id, veh_reg_no, veh_id, item_id,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS itemname,"
					+ " booking_service_emp_id, booking_time, booking_parking_id, booking_active, "
					+ " COALESCE(pickup_pickuptype_id, 0) AS pickup_pickuptype_id,"
					+ " COALESCE(pickup_id, 0) AS pickup_id, contact_customer_id,"
					+ " COALESCE(pickup_emp_id, 0) AS pickup_emp_id,"
					+ " COALESCE(pickup_location_id, 0) AS pickup_location_id,"
					+ " COALESCE(pickup_contact_name, '') AS pickup_contact_name,"
					+ " COALESCE(pickup_mobile1, '') AS pickup_mobile1,"
					+ " COALESCE(pickup_mobile2, '') AS pickup_mobile2,"
					+ " COALESCE(pickup_add, '') AS pickup_add,"
					+ " COALESCE(pickup_landmark, '') AS pickup_landmark,"
					+ " COALESCE(pickup_instruction, '') AS pickup_instruction,"
					+ " COALESCE(courtesycar_id, 0) AS courtesycar_id,"
					+ " COALESCE(courtesycar_courtesyveh_id, 0) AS courtesycar_courtesyveh_id,"
					+ " COALESCE(courtesycar_time_from, '') AS courtesycar_time_from,"
					+ " COALESCE(courtesycar_time_to, '') AS courtesycar_time_to,"
					+ " COALESCE(courtesycar_contact_name, '') AS courtesycar_contact_name,"
					+ " COALESCE(courtesycar_mobile1, '') AS courtesycar_mobile1,"
					+ " COALESCE(courtesycar_mobile2, '') AS courtesycar_mobile2,"
					+ " COALESCE(courtesycar_add, '') AS courtesycar_add,"
					+ " COALESCE(courtesycar_landmark, '') AS courtesycar_landmark,"
					+ " COALESCE(courtesycar_notes, '') AS courtesycar_notes"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = booking_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_pickup ON pickup_booking_id = booking_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_courtesy_car ON courtesycar_booking_id = booking_id"
					+ " WHERE booking_id = " + booking_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				// appt details
				customer_id = crs.getString("contact_customer_id");
				booking_branch_id = crs.getString("booking_branch_id");
				booking_contact_id = crs.getString("booking_contact_id");
				veh_id = crs.getString("booking_veh_id");
				link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id")
						+ "\">" + SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
				link_item_name = "<a href=\"../inventory/inventory-item-list.jsp?item_id=" + crs.getString("item_id")
						+ "\">" + crs.getString("itemname") + "</a>";
				booking_service_emp_id = crs.getString("booking_service_emp_id");
				booking_time = strToLongDate(crs.getString("booking_time"));
				booking_parking_id = crs.getString("booking_parking_id");
				booking_active = crs.getString("booking_active");

				// pickup details
				pickup_id = crs.getString("pickup_id");
				if (!pickup_id.equals("0")) {
					pickup_active = "1";
				}
				pickup_pickuptype_id = crs.getString("pickup_pickuptype_id");
				pickup_emp_id = crs.getString("pickup_emp_id");
				pickup_location_id = crs.getString("pickup_location_id");
				pickup_contact_name = crs.getString("pickup_contact_name");
				pickup_mobile1 = crs.getString("pickup_mobile1");
				pickup_mobile2 = crs.getString("pickup_mobile2");
				pickup_add = crs.getString("pickup_add");
				pickup_landmark = crs.getString("pickup_landmark");
				pickup_instruction = crs.getString("pickup_instruction");

				// courtesy car details
				courtesycar_id = crs.getString("courtesycar_id");
				if (!courtesycar_id.equals("0")) {
					courtesy_active = "1";
				}
				courtesycar_courtesyveh_id = crs.getString("courtesycar_courtesyveh_id");
				courtesycar_time_from = strToLongDate(crs.getString("courtesycar_time_from"));
				courtesycar_time_to = strToLongDate(crs.getString("courtesycar_time_to"));
				courtesycar_contact_name = crs.getString("courtesycar_contact_name");
				courtesycar_mobile1 = crs.getString("courtesycar_mobile1");
				courtesycar_mobile2 = crs.getString("courtesycar_mobile2");
				courtesycar_add = crs.getString("courtesycar_add");
				courtesycar_landmark = crs.getString("courtesycar_landmark");
				courtesycar_notes = crs.getString("courtesycar_notes");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request) {
		booking_id = CNumeric(PadQuotes(request.getParameter("txt_booking_id")));
		customer_id = CNumeric(PadQuotes(request.getParameter("txt_customer_id")));
		booking_branch_id = CNumeric(PadQuotes(request.getParameter("dr_booking_branch_id")));
		booking_contact_id = CNumeric(PadQuotes(request.getParameter("dr_booking_contact_id")));
		// call_time = PadQuotes(request.getParameter("txt_call_time"));
		booking_service_emp_id = CNumeric(PadQuotes(request.getParameter("dr_booking_service_emp_id")));
		booking_parking_id = CNumeric(PadQuotes(request.getParameter("dr_parking_id")));
		booking_time = PadQuotes(request.getParameter("txt_booking_time"));
		booking_active = CheckBoxValue(PadQuotes(request.getParameter("chk_booking_active")));

		// PickUp
		pickup_id = CNumeric(PadQuotes(request.getParameter("txt_pickup_id")));
		pickup_active = CheckBoxValue(PadQuotes(request.getParameter("chk_pickup_active")));
		pickup_pickuptype_id = CNumeric(PadQuotes(request.getParameter("dr_pickup_pickuptype_id")));
		pickup_emp_id = PadQuotes(request.getParameter("dr_pickup_emp_id"));
		pickup_location_id = CNumeric(PadQuotes(request.getParameter("dr_pickup_location_id")));
		pickup_contact_name = PadQuotes(request.getParameter("txt_pickup_contact_name"));
		pickup_mobile1 = PadQuotes(request.getParameter("txt_pickup_mobile1"));
		pickup_mobile2 = PadQuotes(request.getParameter("txt_pickup_mobile2"));
		pickup_add = PadQuotes(request.getParameter("txt_pickup_add"));
		pickup_landmark = PadQuotes(request.getParameter("txt_pickup_landmark"));
		pickup_instruction = PadQuotes(request.getParameter("txt_pickup_instruction"));

		// Courtesy
		courtesycar_id = CNumeric(PadQuotes(request.getParameter("txt_courtesycar_id")));
		courtesy_active = CheckBoxValue(PadQuotes(request.getParameter("chk_courtesy_active")));
		courtesycar_courtesyveh_id = CNumeric(PadQuotes(request.getParameter("dr_vehicle")));
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

	public void CheckForm() {
		msg = "";
		try {
			if (booking_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (booking_contact_id.equals("0")) {
				msg += "<br>Select Booking Contact!";
			}

			if (booking_service_emp_id.equals("0")) {
				msg += "<br>Service Advisor!";
			}

			if (booking_time.equals("")) {
				msg += "<br>Enter Booking Time!";
			} else {
				if (isValidDateFormatLong(booking_time)) {
					apptappttime = ConvertLongDateToStr(booking_time);
					// checking whether parking is OCCUPIED or Not....
					if (!booking_parking_id.equals("0")) {
						String parking_occupied = "0";
						StrSql = "SELECT DATE_FORMAT(DATE_SUB(booking_time, INTERVAL 1 HOUR), '%d/%m/%Y %h:%i') AS min_time,"
								+ " DATE_FORMAT(DATE_ADD(booking_time, INTERVAL 3 HOUR), '%d/%m/%Y %h:%i') AS max_time"
								+ " FROM " + compdb(comp_id) + "axela_service_booking"
								+ " WHERE booking_id != " + booking_id + ""
								+ " AND booking_branch_id = " + booking_branch_id + ""
								+ " AND booking_parking_id = " + booking_parking_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							if (Long.parseLong(apptappttime) > Long.parseLong(ConvertLongDateToStr(crs.getString("min_time")))
									&& Long.parseLong(apptappttime) < Long.parseLong(ConvertLongDateToStr(crs.getString("max_time")))) {
								parking_occupied = "1";
							}
						}
						crs.close();

						if (parking_occupied.equals("1")) {
							msg += "<br>Parking is already Occupied!";
						}
					}
				} else {
					msg += "<br>Enter valid Booking Time!";
				}

				if (!booking_time.equals("")) {
					msg += "<br>Enter Booking Time!";
					// if (isValidDateFormatLong(booking_time)) {
					// if
					// (Long.parseLong(ConvertLongDateToStr(strToLongDate(call_time)))
					// > Long.parseLong(ConvertLongDateToStr(booking_time))) {
					// msg +=
					// "<br>Booking Time should be greater than Call time!";
					// }
					// }
				}

				if (booking_parking_id.equals("0")) {
					msg += "<br>Select Booking Parking Lot!";
				}
			}

			// Checking for PickUp
			if (pickup_active.equals("1")) {
				if (pickup_location_id.equals("0")) {
					msg += "<br>Select Pickup Location!";
				} else {
					StrSql = "SELECT location_leadtime, location_inspection_dur"
							+ " FROM " + compdb(comp_id) + "axela_service_location"
							+ " WHERE location_id = " + pickup_location_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							pickup_time_from = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(booking_time)), 0, 0, -crs.getDouble("location_leadtime")));
							pickup_time_to = ToLongDate(AddHoursDate(StringToDate(ConvertLongDateToStr(booking_time)), 0, 0, crs.getDouble("location_inspection_dur")));
						}

						StrSql = "SELECT pickup_time_from FROM " + compdb(comp_id) + "axela_service_pickup"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id = pickup_location_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_pickup_type ON pickuptype_id = pickup_pickuptype_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
								+ " WHERE pickup_emp_id = " + pickup_emp_id + ""
								+ " AND ((pickup_time_from > " + pickup_time_from + ""
								+ " AND pickup_time_from < " + pickup_time_to + ")"
								+ " OR (pickup_time_to > " + pickup_time_from + ""
								+ " AND pickup_time_to < " + pickup_time_to + "))";
						if (!ExecuteQuery(StrSql).equals("")) {
							msg += "<br>Driver is occupied for other Pickup!";
						}
					}
					crs.close();
				}

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

				if (pickup_landmark.equals("")) {
					msg += "<br>Enter Landmark for Pickup!";
				}
			}

			// Checking for Courtesy car
			if (courtesy_active.equals("1")) {
				if (courtesycar_courtesyveh_id.equals("0")) {
					msg += "<br>Select Courtesy vehicle!";
				}

				if (courtesycar_time_from.equals("")) {
					msg += "<br>Select Start Time for Courtesy!";
				} else if (!courtesycar_time_from.equals("")) {
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
				} else if (!courtesycar_time_to.equals("")) {
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

	protected void UpdateFields() throws SQLException {
		CheckForm();

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_booking"
						+ " SET"
						+ " booking_branch_id = " + booking_branch_id + ","
						+ " booking_service_emp_id = " + booking_service_emp_id + ","
						+ " booking_parking_id = " + booking_parking_id + ","
						+ " booking_active = " + booking_active + ","
						+ " booking_contact_id = " + booking_contact_id + ","
						+ " booking_time = '" + apptappttime + "',"
						+ " booking_modified_id = " + booking_modified_id + ","
						+ " booking_modified_date = '" + booking_modified_date + "'"
						+ " WHERE booking_id = " + booking_id + "";
				stmttx.addBatch(StrSql);

				if (pickup_active.equals("1")) {
					if (pickup_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_pickup"
								+ " (pickup_emp_id,"
								+ " pickup_branch_id,"
								+ " pickup_booking_id,"
								+ " pickup_customer_id,"
								+ " pickup_contact_id,"
								+ " pickup_pickuptype_id,"
								+ " pickup_location_id,"
								+ " pickup_contact_name,"
								+ " pickup_mobile1,"
								+ " pickup_mobile2,"
								+ " pickup_add, "
								+ " pickup_landmark, "
								+ " pickup_notes,"
								+ " pickup_instruction,"
								+ " pickup_entry_id,"
								+ " pickup_entry_date)"
								+ " VALUES"
								+ " (" + pickup_emp_id + ","
								+ " " + booking_branch_id + ","
								+ " " + booking_id + ","
								+ " " + customer_id + ","
								+ " " + booking_contact_id + ","
								+ " " + pickup_pickuptype_id + ","
								+ " " + pickup_location_id + ","
								+ " '" + pickup_contact_name + "',"
								+ " '" + pickup_mobile1 + "',"
								+ " '" + pickup_mobile2 + "',"
								+ " '" + pickup_add + "',"
								+ " '" + pickup_landmark + "',"
								+ " '',"
								+ " '" + pickup_instruction + "',"
								+ " '" + booking_modified_id + "',"
								+ " '" + booking_modified_date + "')";
						stmttx.addBatch(StrSql);
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_pickup"
								+ " SET"
								+ " pickup_emp_id = " + pickup_emp_id + ","
								+ " pickup_branch_id = " + booking_branch_id + ","
								+ " pickup_booking_id = " + booking_id + ","
								+ " pickup_customer_id = " + customer_id + ","
								+ " pickup_contact_id = " + booking_contact_id + ","
								+ " pickup_pickuptype_id = " + pickup_pickuptype_id + ","
								+ " pickup_location_id = " + pickup_location_id + ","
								+ " pickup_contact_name = '" + pickup_contact_name + "',"
								+ " pickup_mobile1 = '" + pickup_mobile1 + "',"
								+ " pickup_mobile2 = '" + pickup_mobile2 + "',"
								+ " pickup_add = '" + pickup_add + "',"
								+ " pickup_landmark = '" + pickup_landmark + "',"
								+ " pickup_instruction = '" + pickup_instruction + "',"
								+ " pickup_modified_id = " + booking_modified_id + ","
								+ " pickup_modified_date = '" + booking_modified_date + "'"
								+ " WHERE pickup_id = " + pickup_id + "";
						stmttx.addBatch(StrSql);
					}
				} else {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_pickup"
							+ " WHERE pickup_id =" + pickup_id + "";
					stmttx.addBatch(StrSql);
				}

				if (courtesy_active.equals("1")) {
					if (courtesycar_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_courtesy_car"
								+ " (courtesycar_customer_id,"
								+ " courtesycar_contact_id,"
								+ " courtesycar_booking_id,"
								+ " courtesycar_branch_id,"
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
								+ " (" + customer_id + ","
								+ " " + booking_contact_id + ","
								+ " " + booking_id + ","
								+ " " + booking_branch_id + ","
								+ " " + courtesycar_courtesyveh_id + ","
								+ " '" + ccstartdate + "',"
								+ " '" + ccenddate + "',"
								+ " '" + courtesycar_contact_name + "',"
								+ " '" + courtesycar_landmark + "',"
								+ " '" + courtesycar_notes + "',"
								+ " '" + courtesycar_mobile1 + "',"
								+ " '" + courtesycar_mobile2 + "',"
								+ " '" + courtesycar_add + "',"
								+ " '" + courtesy_active + "',"
								+ " " + booking_modified_id + ","
								+ " '" + booking_modified_date + "')";
						stmttx.addBatch(StrSql);
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_courtesy_car"
								+ " SET"
								+ " courtesycar_customer_id = " + customer_id + ","
								+ " courtesycar_contact_id = " + booking_contact_id + ","
								+ " courtesycar_booking_id = " + booking_id + ","
								+ " courtesycar_branch_id = " + booking_branch_id + ","
								+ " courtesycar_courtesyveh_id = " + courtesycar_courtesyveh_id + ","
								+ " courtesycar_time_from = '" + ccstartdate + "',"
								+ " courtesycar_time_to = '" + ccenddate + "',"
								+ " courtesycar_contact_name = '" + courtesycar_contact_name + "',"
								+ " courtesycar_notes = '" + courtesycar_notes + "',"
								+ " courtesycar_landmark = '" + courtesycar_landmark + "',"
								+ " courtesycar_mobile1 = '" + courtesycar_mobile1 + "',"
								+ " courtesycar_mobile2 = '" + courtesycar_mobile2 + "',"
								+ " courtesycar_add = '" + courtesycar_add + "',"
								+ " courtesycar_modified_id = " + booking_modified_id + ","
								+ " courtesycar_modified_date = '" + booking_modified_date + "'"
								+ " WHERE courtesycar_id = " + courtesycar_id + "";
						stmttx.addBatch(StrSql);
					}
				} else {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_courtesy_car"
							+ " WHERE courtesycar_id = " + courtesycar_id + "";
					stmttx.addBatch(StrSql);
				}
				stmttx.executeBatch();
				conntx.commit();
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
	}

	public String PopulatePickUpLocation(String branch_id) {
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
					+ " WHERE emp_pickup_driver = 1"
					+ " AND emp_branch_id = " + booking_branch_id + ""
					+ " AND emp_active = 1"
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

	public String PopulateParking(String parking_branch_id, String booking_time, String booking_id) {
		StringBuilder Str = new StringBuilder();
		// String booking_time = "";
		String befortime = "1", aftertime = "3";
		try {
			if (!booking_time.equals("")) {
				booking_time = ConvertLongDateToStr(booking_time);
				StrSql = "SELECT parking_id, parking_name, parking_rank"
						+ " FROM " + compdb(comp_id) + "axela_service_parking"
						+ " WHERE parking_branch_id = " + parking_branch_id + ""
						+ " AND parking_id NOT IN (SELECT booking_parking_id"
						+ " FROM " + compdb(comp_id) + "axela_service_booking"
						+ " WHERE ((DATE_FORMAT(DATE_SUB(booking_time, INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')"
						+ " <= DATE_FORMAT(DATE_SUB('" + booking_time + "', INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s'))"
						+ " AND (DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s')"
						+ " >= DATE_FORMAT(DATE_SUB('" + booking_time + "', INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')))"
						+ " OR ((DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + befortime + " hour), '%Y%m%d%h%i%s')"
						+ " <= DATE_FORMAT(DATE_ADD('" + booking_time + "', INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s'))"
						+ " AND (DATE_FORMAT(DATE_ADD(booking_time, INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s')"
						+ " >= DATE_FORMAT(DATE_ADD('" + booking_time + "', INTERVAL " + aftertime + " hour), '%Y%m%d%h%i%s')))"
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

	public String PopulateServiceExecutive(String booking_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS empname"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_branch_id = " + booking_branch_id + ""
					+ " AND emp_service = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
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

	public String PopulateCourtesyVehicle(String courtesyveh_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT courtesyveh_id, courtesyveh_name"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
					+ " WHERE courtesyveh_branch_id = " + courtesyveh_branch_id + ""
					+ " AND courtesyveh_active = 1"
					+ " ORDER BY courtesyveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("courtesyveh_id"));
				Str.append(StrSelectdrop(crs.getString("courtesyveh_id"), courtesycar_courtesyveh_id));
				Str.append(">").append(crs.getString("courtesyveh_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
