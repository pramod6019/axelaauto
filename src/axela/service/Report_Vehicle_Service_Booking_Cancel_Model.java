package axela.service;
//24 December, 2016

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Service_Booking_Cancel_Model extends Connect {

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
	public String veh_emp_id = "", cancel = "";
	public String veh_id = "0";
	public String lostcase_id = "0", competitor_id = "0";
	public String veh_followup_desc = "";
	public String vehlostcase1_id = "0";
	public String calltype_id = "0", emp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("comp_id---------" + comp_id);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				cancel = PadQuotes(request.getParameter("add_button"));
				lostcase_id = CNumeric(PadQuotes(request.getParameter("lostcase_id")));
				veh_followup_desc = PadQuotes(request.getParameter("veh_followup_desc"));
				emp_id = CNumeric(GetSession("emp_id", request));
				vehfollowup_id = CNumeric(request.getParameter("vehfollowup_id"));
				competitor_id = CNumeric(PadQuotes(request.getParameter("dr_competitor_id")));
				if (!vehfollowup_id.equals("0")) {
					veh_id = ExecuteQuery("SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup WHERE vehfollowup_id=" + vehfollowup_id);
				}
				SOP("dr_competitor_id====" + veh_id);
				if (!lostcase_id.equals("1")) {
					competitor_id = "0";
				}
				// if (lostcase_id.equals("0")) {
				// StrHTML = "<font color=#ff0000>Select Lost Case!</font>";
				// }
				// else if (veh_followup_desc.equals("")) {
				// StrHTML += "<font color=#ff0000><br>Enter Feedback!</font>";
				// }
				// else {
				SOP("cancel====" + cancel);
				if (cancel.equals("yes") && !lostcase_id.equals("0") && !veh_followup_desc.equals("")) {
					CancelledBooking(comp_id, vehfollowup_id);
				}
				// }

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	private void CancelledBooking(String comp_id, String vehfollowup_id) {
		if (StrHTML.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
					+ " SET vehfollowup_postponed = 1"
					// + " vehfollowup_vehlostcase1_id = " + lostcase_id + ""
					// + " vehfollowup_desc = '" + veh_followup_desc + "'"
					+ " WHERE vehfollowup_id = " + vehfollowup_id + "";
			// SOP("StrSql====update==" + StrSql);
			updateQuery(StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup "
					+ "( vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_vehaction_id,"
					+ " vehfollowup_vehlostcase1_id,"
					+ " vehfollowup_competitor_id,"
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
					+ " 3," // vehfollowup_vehaction_id
					+ " " + lostcase_id + ","
					+ " " + competitor_id + ","
					+ " vehfollowup_workshop_branch_id,"
					+ " '',"
					+ " vehfollowup_bookingtype_id,"
					+ "	COALESCE((SELECT calltype.vehfollowup_vehcalltype_id FROM " + compdb(comp_id) + "axela_service_followup calltype"
					+ " WHERE calltype.vehfollowup_veh_id = " + veh_id + " ORDER BY calltype.vehfollowup_id DESC LIMIT 1), 0 ),"
					+ "	COALESCE((SELECT dueservice.vehfollowup_dueservice FROM " + compdb(comp_id) + "axela_service_followup dueservice"
					+ " WHERE dueservice.vehfollowup_veh_id = " + veh_id + " ORDER BY dueservice.vehfollowup_id DESC LIMIT 1), 0 ),"
					+ " vehfollowup_pickupdriver_emp_id,"
					+ " vehfollowup_pickuplocation,"
					+ " '" + veh_followup_desc + "',"
					+ " vehfollowup_kms,"
					+ " '0'," // vehfollowup_postponed
					+ " vehfollowup_contactable_id,"
					+ " vehfollowup_notcontactable_id,"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_id = " + vehfollowup_id + " )";
			SOP("StrSql----------cancel--insert---------- " + StrSql);
			updateQuery(StrSql);
			StrHTML = "<b><font color=#ff0000>Booking Cancelled!</font></b>";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateVehLostcase1(String comp_id, String vehlostcase1_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehlostcase1_id, vehlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY vehlostcase1_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehlostcase1_id"));
				Str.append(StrSelectdrop(crs.getString("vehlostcase1_id"), vehlostcase1_id));
				Str.append(">").append(crs.getString("vehlostcase1_name")).append("</option>\n");
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

	public String PopulateCompetitor(String comp_id, String competitor_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT competitor_id, competitor_name"
					+ " FROM " + compdb(comp_id) + "axela_service_competitor"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = competitor_brand_id"
					+ " WHERE 1=1"
					+ " AND branch_id =" + branch_id
					+ " GROUP BY competitor_id"
					+ " ORDER BY competitor_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("StrSql==" + StrSql);
			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("competitor_id"));
				Str.append(StrSelectdrop(crs.getString("competitor_id"), competitor_id));
				Str.append(">").append(crs.getString("competitor_name")).append("</option>\n");
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
}
