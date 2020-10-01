package axela.service;
//aJIt 11th March, 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Vehicle_Service_Booking_Check extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0";
	public String dr_branch_id = "0", booking_branch_id = "0", vehfollowup_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime1 = "", starttime = "";
	public String months = "", end_time = "", endtime = "", endtime1 = "";
	public String veh_emp_id = "", addpostpone = "", postponed_time = "", postponed_desc = "", postponed_bookingtype_id = "0";
	public String postponed_driver_emp_id = "", postponed_pickupaddress = "";
	public String veh_id = "";
	public String calltype_id = "0", emp_id = "0";
	public String duplicatebooking = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				addpostpone = PadQuotes(request.getParameter("add_button"));
				postponed_time = PadQuotes(request.getParameter("postponed_time"));
				postponed_bookingtype_id = PadQuotes(request.getParameter("dr_postponed_bookingtype_id"));
				postponed_driver_emp_id = PadQuotes(request.getParameter("dr_postponed_driver_emp_id"));
				postponed_pickupaddress = PadQuotes(request.getParameter("txt_postponed_pickupaddress"));
				postponed_desc = PadQuotes(request.getParameter("txt_postponed_desc"));
				emp_id = CNumeric(GetSession("emp_id", request));
				vehfollowup_id = CNumeric(request.getParameter("vehfollowup_id"));

				if (emp_id.equals("1")) {
					SOPInfo("postponed_desc==123===" + postponed_desc);
				}

				if (!vehfollowup_id.equals("0")) {
					StrSql = "SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup"
							+ " WHERE vehfollowup_id = " + vehfollowup_id + "";
					veh_id = CNumeric(ExecuteQuery(StrSql));
				}

				StrSql = "SELECT vehfollowup_id FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE vehfollowup_veh_id = '" + veh_id + "'"
						+ " AND SUBSTR(vehfollowup_appt_time,1,8) = SUBSTR('" + ConvertLongDateToStr(postponed_time) + "',1,8)";

				duplicatebooking = CNumeric(ExecuteQuery(StrSql));

				if (!isValidDateFormatLong(postponed_time)) {
					StrHTML = "<font color=#ff0000>Enter Valid Booking Postpone Time!</font>";
				}

				if (!duplicatebooking.equals("0")) {
					StrHTML = "<font color=#ff0000>Booking Already Exists On same Date!</font>";
				}
				if (postponed_desc.equals("")) {
					StrHTML = "<font color=#ff0000>Description Cannot be empty!</font>";
				}

				if (!postponed_bookingtype_id.equals("2")) {
					postponed_driver_emp_id = "0";
					postponed_pickupaddress = "";
				}
				if (emp_id.equals("1")) {
					SOPInfo("StrHTML===123==" + StrHTML);
				}
				if (StrHTML.equals("")) {
					if (addpostpone.equals("yes")) {
						PostponedBooking();
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void PostponedBooking() {
		if (StrHTML.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
					+ " SET vehfollowup_postponed = 1"
					+ " WHERE 1=1 "
					+ " AND vehfollowup_id = " + vehfollowup_id + "";
			// SOP("StrSql====1==" + StrSql);
			updateQuery(StrSql);

			if (!vehfollowup_id.equals("0") && !veh_id.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup "
						+ "( vehfollowup_veh_id,"
						+ " vehfollowup_emp_id,"
						+ " vehfollowup_vehaction_id,"
						+ " vehfollowup_workshop_branch_id,"
						+ " vehfollowup_appt_time,"
						+ " vehfollowup_bookingtype_id,"
						+ "	vehfollowup_vehcalltype_id,"
						+ " vehfollowup_dueservice,"
						+ " vehfollowup_pickupdriver_emp_id,"
						+ " vehfollowup_pickuplocation,"
						+ " vehfollowup_desc,"
						+ " vehfollowup_kms,"
						+ " vehfollowup_postponed,"
						+ " vehfollowup_contactable_id,"
						+ " vehfollowup_notcontactable_id,"
						+ " vehfollowup_followup_time,"
						+ " vehfollowup_entry_id,"
						+ " vehfollowup_entry_time"
						+ " )("
						+ " SELECT"
						+ " vehfollowup_veh_id,"
						+ " vehfollowup_emp_id,"
						+ " vehfollowup_vehaction_id,"
						+ " vehfollowup_workshop_branch_id,"
						+ " '" + ConvertLongDateToStr(postponed_time) + "',"
						+ " '" + postponed_bookingtype_id + "',"
						+ "	COALESCE((SELECT calltype.vehfollowup_vehcalltype_id FROM " + compdb(comp_id) + "axela_service_followup calltype"
						+ " WHERE calltype.vehfollowup_veh_id = " + veh_id + " ORDER BY calltype.vehfollowup_id DESC LIMIT 1), 0 ),"
						+ "	COALESCE((SELECT dueservice.vehfollowup_dueservice FROM " + compdb(comp_id) + "axela_service_followup dueservice"
						+ " WHERE dueservice.vehfollowup_veh_id = " + veh_id + " ORDER BY dueservice.vehfollowup_id DESC LIMIT 1), 0 ),"
						+ " '" + postponed_driver_emp_id + "',"
						+ " '" + postponed_pickupaddress + "',"
						+ " '" + postponed_desc + "',"
						+ " vehfollowup_kms,"
						+ " '0'," // vehfollowup_postponed
						+ " vehfollowup_contactable_id,"
						+ " vehfollowup_notcontactable_id,"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "'"
						+ " FROM " + compdb(comp_id) + "axela_service_followup"
						+ " WHERE vehfollowup_id = " + vehfollowup_id + " LIMIT 1)";

				SOP("StrSql==PostponedBooking== " + StrSql);
				updateQuery(StrSql);
			}

			StrHTML = "<font color=#ff0000>Booking Time Postponed!</font>";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
