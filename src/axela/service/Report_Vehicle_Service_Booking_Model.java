package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Service_Booking_Model extends Connect {

	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0";
	public String dr_branch_id = "0", booking_branch_id = "0", post_veh_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime1 = "", starttime = "";
	public String months = "", end_time = "", endtime = "", endtime1 = "";
	public String veh_emp_id = "", addpostpone = "", postponed_time = "", txt_postponed_desc = "";
	public String veh_id = "";
	public String calltype_id = "0", emp_post_id = "0";
	public String msg = "", status = "Add";
	public Report_Check reportexe = new Report_Check();
	public Report_Vehicle_Service_Booking_Followup servicelist = new Report_Vehicle_Service_Booking_Followup();
	public String vehfollowup_id = "0";
	public String vehfollowup_bookingtype_id = "0", vehfollowup_pickupdriver_emp_id = "0", vehfollowup_pickuplocation = "";
	String postponemodal = "";
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
				postponemodal = PadQuotes(request.getParameter("postponemodal"));
				addpostpone = PadQuotes(request.getParameter("add_button"));
				postponed_time = PadQuotes(request.getParameter("txt_postponed_time"));
				txt_postponed_desc = PadQuotes(request.getParameter("txt_postponed_desc"));
				emp_post_id = CNumeric(GetSession("emp_id", request));
				post_veh_id = CNumeric(PadQuotes(request.getParameter("post_veh_id")));
				vehfollowup_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_id")));

				if (postponemodal.equals("yes")) {
					getServiceBookingDetails();
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

	private void getServiceBookingDetails() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT "
					+ "  bookingtype_id , COALESCE(emp_id,0) AS pickup_emp_id,"
					+ " CONCAT(contact_address,', ',city_name,', ',state_name,', ',contact_pin,', ',contact_landmark) AS pickuplocation,"
					+ " COALESCE((SELECT vehfollowup_pickuplocation"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_vehaction_id = 1 "
					+ " AND vehfollowup_bookingtype_id =2"
					+ " AND vehfollowup_veh_id = veh_id "
					+ " ORDER BY vehfollowup_id DESC LIMIT 1 "
					+ " ),'') AS vehfollowup_pickuplocation"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_bookingtype ON bookingtype_id = vehfollowup_bookingtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_pickupdriver_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE vehfollowup_id=" + vehfollowup_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.next()) {
				vehfollowup_bookingtype_id = crs.getString("bookingtype_id");
				vehfollowup_pickupdriver_emp_id = crs.getString("pickup_emp_id");
				vehfollowup_pickuplocation = crs.getString("vehfollowup_pickuplocation");
				if (vehfollowup_pickuplocation.equals("")) {
					vehfollowup_pickuplocation = crs.getString("pickuplocation");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String PopulateServiceBookingType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bookingtype_id, bookingtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_bookingtype"
					+ " WHERE 1=1"
					+ " GROUP BY bookingtype_id"
					+ " ORDER BY bookingtype_id ";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bookingtype_id"));
				Str.append(StrSelectdrop(crs.getString("bookingtype_id"), vehfollowup_bookingtype_id));
				Str.append(">").append(crs.getString("bookingtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateServicePickUp(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND emp_active=1"
					+ " AND emp_pickup_driver = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), vehfollowup_pickupdriver_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
