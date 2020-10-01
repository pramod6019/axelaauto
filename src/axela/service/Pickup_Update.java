package axela.service;
//Sangita 04,05 APRIL 2013

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Pickup_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String entry_by = "", modified_by = "";
	public String BranchAccess = "", branch_id = "0";
	public String QueryString = "";
	public String pickup_customer_id = "0";
	public String pickup_contact_id = "0";
	public String pickup_contact_name = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String pickup_id = "0";
	public String pickup_branch_id = "0";
	public String pickup_veh_id = "0";
	public String pickup_driver_id = "0";
	public String contact_id = "0";
	public String customer_id = "0";
	public String pickup_type = "";
	public String pickup_time = "";
	public String pickup_time_from = "", pickup_time_to = "";
	public String pickup_location_id = "0";
	public String pickup_add = "", pickup_landmark = "";
	public String pickup_mobile1 = "", pickup_mobile2 = "";
	public String pickup_active = "0";
	public String pickup_notes = "";
	public String pickup_instruction = "";
	public String customer_display = "none";
	public String pickup_entry_id = "0";
	public String pickup_entry_date = "";
	public String pickup_modified_id = "0";
	public String pickup_modified_date = "";
	public String veh_id = "0";
	public String call_veh_id = "0";
	public String veh_reg_no = "";
	public String link_vehicle_name = "";
	public String strHTML = "";
	public String pickupdate = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_service_pickup_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				pickup_id = CNumeric(PadQuotes(request.getParameter("pickup_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_contact_id")));
				pickup_contact_id = CNumeric(PadQuotes(request.getParameter("pickup_contact_id")));
				pickup_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				if (!pickup_veh_id.equals("0")) {
					PopulateVehicleDetails();
				}
				if (!branch_id.equals("0")) {
					pickup_branch_id = branch_id;
				}
				PopulateContactDetails();

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						pickup_type = "1";
						if (!pickup_veh_id.equals("0")) {
							customer_display = "";
						} else {
							customer_display = "none";
						}
						pickup_active = "1";
						pickupdate = strToLongDate(ToLongDate(kknow()));
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_pickup_add", request).equals("1")) {
							pickup_entry_id = emp_id;
							pickup_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("pickup-list.jsp?pickup_id=" + pickup_id + "&msg=Pickup added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !deleteB.equals("Delete Pickup")) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !deleteB.equals("Delete Pickup")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_pickup_edit ", request).equals("1")) {
							pickup_modified_id = emp_id;
							pickup_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("pickup-list.jsp?pickup_id=" + pickup_id + "&msg=Pickup updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Pickup")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_pickup_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("pickup-list.jsp?msg=Pickup deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		pickup_contact_id = CNumeric(PadQuotes(request.getParameter("pickup_contact_id")));
		contact_id = CNumeric(PadQuotes(request.getParameter("span_contact_id")));
		pickup_customer_id = CNumeric(PadQuotes(request.getParameter("txt_pickup_customer_id")));
		customer_id = CNumeric(PadQuotes(request.getParameter("span_customer_id")));
		if (!contact_id.equals("0") && !pickup_contact_id.equals(contact_id)) {
			pickup_contact_id = contact_id;
		}

		if (!customer_id.equals("0") && !pickup_customer_id.equals(customer_id)) {
			pickup_customer_id = customer_id;
		}

		if (pickup_contact_id.equals("0")) {
			customer_display = "none";
		}

		veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
		if (!veh_id.equals("0")) {
			VehicleDetails();
		}

		pickup_veh_id = CNumeric(PadQuotes(request.getParameter("txt_veh_id")));
		if (!veh_id.equals("0") && !veh_id.equals(pickup_veh_id)) {
			pickup_veh_id = veh_id;
		}
		pickup_driver_id = PadQuotes(request.getParameter("dr_pickup_emp_id"));
		pickup_type = PadQuotes(request.getParameter("dr_pickuptype"));
		pickup_location_id = PadQuotes(request.getParameter("dr_location"));
		pickupdate = PadQuotes(request.getParameter("txt_pickup_date"));
		pickup_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		pickup_add = PadQuotes(request.getParameter("txt_pickup_add"));
		pickup_landmark = PadQuotes(request.getParameter("txt_pickup_landmark"));
		pickup_mobile1 = PadQuotes(request.getParameter("txt_pickup_mobile1"));
		pickup_mobile2 = PadQuotes(request.getParameter("txt_pickup_mobile2"));
		pickup_active = CheckBoxValue(PadQuotes(request.getParameter("chk_pickup_active")));
		pickup_notes = PadQuotes(request.getParameter("txt_pickup_notes"));
		pickup_instruction = PadQuotes(request.getParameter("txt_pickup_instruction"));
		pickup_contact_name = PadQuotes(request.getParameter("txt_pickup_contact_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		pickup_entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		pickup_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (pickup_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (pickup_contact_id.equals("0")) {
			msg += "<br>Select Contact!";
		}

		if (pickup_type.equals("0")) {
			msg += "<br>Select Pickup Type!";
		}

		if (pickup_driver_id.equals("0")) {
			msg += "<br>Select Driver!";
		}

		if (pickup_location_id.equals("0")) {
			msg += "<br>Select Location!";
		}

		if (pickup_contact_name.equals("")) {
			msg += "<br>Enter Contact Person Name!";
		}

		if (pickup_mobile1.equals("")) {
			msg += "<br>Enter Mobile 1!";
		}

		if (!pickup_mobile1.equals("") && !IsValidMobileNo11(pickup_mobile1)) {
			msg += "<br>Enter valid Mobile 1!";
		}

		if (!pickup_mobile2.equals("") && !IsValidMobileNo11(pickup_mobile2)) {
			msg += "<br>Enter valid Mobile 2!";
		}

		if (pickup_add.equals("")) {
			msg += "<br>Enter Address!";
		}

		pickup_time = pickupdate + ":00";
		pickup_time = ConvertLongDateToStr(pickup_time);

		StrSql = "SELECT location_leadtime, location_inspection_dur"
				+ " FROM " + compdb(comp_id) + "axela_service_location"
				+ " WHERE location_id = " + pickup_location_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Date start = AddHoursDate(StringToDate(pickup_time), 0, 0, -crs.getDouble("location_leadtime"));
				pickup_time_from = ToLongDate(start);
				Date end = AddHoursDate(StringToDate(pickup_time), 0, 0, crs.getDouble("location_inspection_dur"));
				pickup_time_to = ToLongDate(end);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		if (pickupdate.equals("")) {
			msg += "<br>Select Date!";
		} else {
			if (isValidDateFormatLong(pickupdate)) {
				if (!pickup_time_from.equals("") && !pickup_time_to.equals("")) {
					StrSql = "SELECT pickup_id FROM " + compdb(comp_id) + "axela_service_pickup"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id= pickup_location_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
							+ " WHERE ((pickup_time_from >= " + pickup_time_from + " AND pickup_time_from < " + pickup_time_to + ")"
							+ " OR (pickup_time_to > " + pickup_time_from + " AND pickup_time_to <= " + pickup_time_to + ") "
							+ " OR (pickup_time_from >= " + pickup_time_from + " AND pickup_time_to <= " + pickup_time_to + ") "
							+ " OR (pickup_time_from <= " + pickup_time_from + " AND pickup_time_to >= " + pickup_time_to + "))"
							+ " AND pickup_emp_id = " + pickup_driver_id + "";
					if (!update.equals("")) {
						StrSql += " AND pickup_id != " + pickup_id;
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Vehicle is occupied by other Pickup!";
					}
				}
			} else {
				msg += "<br>Enter valid Pickup Date!";
			}
		}

		if (pickup_landmark.equals("")) {
			msg = msg + "<br>Enter Landmark!";
		}

	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_pickup"
					+ " (pickup_branch_id,"
					+ " pickup_veh_id,"
					+ " pickup_customer_id,"
					+ " pickup_contact_id,"
					+ " pickup_emp_id,"
					+ " pickup_pickuptype_id,"
					+ " pickup_time,"
					+ " pickup_time_from,"
					+ " pickup_time_to,"
					+ " pickup_location_id,"
					+ " pickup_add, "
					+ " pickup_landmark, "
					+ " pickup_contact_name,"
					+ " pickup_mobile1,"
					+ " pickup_mobile2,"
					+ " pickup_active,"
					+ " pickup_notes,"
					+ " pickup_instruction,"
					+ " pickup_entry_id,"
					+ " pickup_entry_date)"
					+ " VALUES"
					+ " ('" + pickup_branch_id + "',"
					+ " " + pickup_veh_id + ","
					+ " '" + pickup_customer_id + "',"
					+ " " + pickup_contact_id + ","
					+ " " + pickup_driver_id + ","
					+ " '" + pickup_type + "',"
					+ " '" + pickup_time + "',"
					+ " '" + pickup_time_from + "',"
					+ " '" + pickup_time_to + "',"
					+ " '" + pickup_location_id + "',"
					+ " '" + pickup_add + "',"
					+ " '" + pickup_landmark + "',"
					+ " '" + pickup_contact_name + "',"
					+ " '" + pickup_mobile1 + "',"
					+ " '" + pickup_mobile2 + "',"
					+ " '" + pickup_active + "',"
					+ " '" + pickup_notes + "',"
					+ " '" + pickup_instruction + "',"
					+ " '" + pickup_entry_id + "',"
					+ " '" + pickup_entry_date + "')";
			pickup_id = UpdateQueryReturnID(StrSql);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_pickup"
					+ " SET"
					+ " pickup_branch_id = " + pickup_branch_id + ","
					+ " pickup_veh_id = " + pickup_veh_id + ","
					+ " pickup_customer_id = " + pickup_customer_id + ","
					+ " pickup_contact_id = " + pickup_contact_id + ","
					+ " pickup_emp_id = " + pickup_driver_id + ","
					+ " pickup_pickuptype_id = " + pickup_type + ","
					+ " pickup_time = '" + pickup_time + "',"
					+ " pickup_time_from = '" + pickup_time_from + "',"
					+ " pickup_time_to = '" + pickup_time_to + "',"
					+ " pickup_location_id = " + pickup_location_id + ","
					+ " pickup_add = '" + pickup_add + "',"
					+ " pickup_landmark = '" + pickup_landmark + "',"
					+ " pickup_contact_name = '" + pickup_contact_name + "',"
					+ " pickup_mobile1 = '" + pickup_mobile1 + "',"
					+ " pickup_mobile2 = '" + pickup_mobile2 + "',"
					+ " pickup_active = '" + pickup_active + "',"
					+ " pickup_notes = '" + pickup_notes + "',"
					+ " pickup_instruction = '" + pickup_instruction + "',"
					+ " pickup_modified_id = " + pickup_modified_id + ","
					+ " pickup_modified_date = '" + pickup_modified_date + "'"
					+ " WHERE pickup_id = " + pickup_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_pickup"
					+ " WHERE pickup_id = " + pickup_id;
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT pickup_branch_id, pickup_contact_id, pickup_emp_id, pickup_pickuptype_id,"
					+ " pickup_veh_id, pickup_time, pickup_time_from, pickup_time_to, pickup_location_id,"
					+ " pickup_add, pickup_landmark, pickup_contact_name, pickup_mobile1, pickup_mobile2,"
					+ " pickup_active, pickup_notes, pickup_instruction, title_desc, pickup_entry_id,"
					+ " pickup_entry_date, pickup_modified_id, pickup_modified_date, contact_id, customer_id,"
					+ " customer_name, COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no,"
					+ " contact_fname, contact_lname"
					+ " FROM " + compdb(comp_id) + "axela_service_pickup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id = pickup_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_pickup_type ON pickuptype_id = pickup_pickuptype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = pickup_veh_id"
					+ " WHERE pickup_id = " + pickup_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_display = "";
					pickup_veh_id = crs.getString("pickup_veh_id");
					pickup_contact_id = crs.getString("pickup_contact_id");
					pickup_customer_id = crs.getString("customer_id");
					pickup_driver_id = crs.getString("pickup_emp_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("pickup_contact_id") + "\">" + crs.getString("title_desc") + " " + crs.getString("contact_fname")
							+ " " + crs.getString("contact_lname") + "</a>";
					veh_id = crs.getString("veh_id");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id="
							+ crs.getString("veh_id") + "\">" + SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
					pickup_time = crs.getString("pickup_time");
					pickup_time_from = crs.getString("pickup_time_from");
					pickup_time_to = crs.getString("pickup_time_to");
					pickupdate = SplitDate(pickup_time) + "/" + SplitMonth(pickup_time) + "/" + SplitYear(pickup_time) + " " + SplitHourMin(pickup_time);
					pickup_location_id = crs.getString("pickup_location_id");
					pickup_type = crs.getString("pickup_pickuptype_id");
					pickup_add = crs.getString("pickup_add");
					pickup_landmark = crs.getString("pickup_landmark");
					pickup_contact_name = crs.getString("pickup_contact_name");
					pickup_mobile1 = crs.getString("pickup_mobile1");
					pickup_mobile2 = crs.getString("pickup_mobile2");
					pickup_branch_id = crs.getString("pickup_branch_id");
					pickup_active = crs.getString("pickup_active");
					pickup_notes = crs.getString("pickup_notes");
					pickup_instruction = crs.getString("pickup_instruction");
					pickup_entry_id = crs.getString("pickup_entry_id");
					entry_by = Exename(comp_id, crs.getInt("pickup_entry_id"));
					pickup_entry_date = strToLongDate(crs.getString("pickup_entry_date"));
					pickup_modified_id = crs.getString("pickup_modified_id");
					if (!pickup_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(pickup_modified_id));
						pickup_modified_date = strToLongDate(crs.getString("pickup_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid pickup"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDriver() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value = 0>Select Driver</option>");
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_pickup_driver"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_pickup_driver = '1'"
					+ " AND emp_active = '1'"
					+ " AND emp_branch_id != 0"
					+ " AND emp_branch_id = " + pickup_branch_id + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), pickup_driver_id));
				Str.append(">").append(crs.getString("emp_pickup_driver")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_service_location"
					+ " WHERE location_active = '1'"
					+ " ORDER BY location_name ";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(Selectdrop(crs.getInt("location_id"), pickup_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
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
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT pickuptype_id, pickuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_pickup_type"
					+ " ORDER BY pickuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("pickuptype_id")).append("");
				Str.append(Selectdrop(crs.getInt("pickuptype_id"), pickup_type));
				Str.append(">").append(crs.getString("pickuptype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !pickup_contact_id.equals("0")) {
				StrSql = "SELECT contact_id, contact_fname, contact_lname, title_desc,"
						+ " customer_name, customer_id"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " WHERE 1 = 1";
				if (!contact_id.equals("0")) {
					StrSql = StrSql + " AND contact_id = " + contact_id + "";
				} else if (!pickup_contact_id.equals("0")) {
					StrSql = StrSql + " AND contact_id = " + pickup_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					customer_display = "";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
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

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id"
					+ " AND emp_pickup_driver = '1'"
					+ " WHERE 1 = 1 "
					+ " AND branch_branchtype_id IN (1,3)";
			if (add.equals("yes")) {
				StrSql = StrSql + " AND branch_active = 1"
						+ " AND emp_active = 1";
			}
			StrSql = StrSql + " GROUP BY branch_name"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			 SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value = 0>Select Branch</option>");
			while (crs.next()) {
				Str.append("<option value = ").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), pickup_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
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
			if (!veh_id.equals("0") || !pickup_veh_id.equals("0")) {
				StrSql = "SELECT veh_reg_no, veh_id, contact_id, title_desc,"
						+ " customer_name, customer_id, contact_fname, contact_lname,"
						+ " contact_mobile1, contact_mobile2,"
						+ " contact_address, contact_landmark,"
						+ " CONCAT(contact_fname,' ', contact_lname) AS contactname"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = veh_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " WHERE 1 = 1";
				if (!veh_id.equals("0")) {
					StrSql += " AND veh_id = " + veh_id + "";
				} else if (!pickup_veh_id.equals("0")) {
					StrSql += " AND veh_id = " + pickup_veh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">"
							+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";

					customer_display = "";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					pickup_contact_name = crs.getString("contactname");
					pickup_mobile1 = crs.getString("contact_mobile1");
					pickup_mobile2 = crs.getString("contact_mobile2");
					pickup_add = crs.getString("contact_address");
					pickup_landmark = crs.getString("contact_landmark");
					pickup_contact_id = crs.getString("contact_id");
					pickup_customer_id = crs.getString("customer_id");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void VehicleDetails() {
		try {
			if (!veh_id.equals("0") || !pickup_veh_id.equals("0")) {
				StrSql = "SELECT veh_reg_no, veh_id"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE 1 = 1";
				if (!veh_id.equals("0")) {
					StrSql += " AND veh_id = " + veh_id + "";
				} else if (!pickup_veh_id.equals("0")) {
					StrSql += " AND veh_id = " + pickup_veh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					link_vehicle_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">"
							+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
