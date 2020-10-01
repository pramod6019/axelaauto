package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;
///
public class Report_Vehicle_Service_Due_Details extends Connect {

	public String StrHTML = "";
	public String exporttype = "";
	public String exportB = "";
	public String exportcount = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", empcrm_id = "0";
	public String dr_branch_id = "0", veh_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String pending_followup = "";
	public String go = "";
	public String veh_kms = "", start_time = "", starttime = "";
	public String months = "", end_time = "", endtime = "";
	public String veh_emp_id = "";
	public String veh_id = "";
	public String msg = "";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportB = PadQuotes(request.getParameter("btn_export"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				GetValues(request, response);
				// CheckForm();

				if (exportB.equals("Export")) {
					// GetValues(request, response);
					// CheckForm();
					StrSearch = ExeAccess.replace("emp_id", "crm.emp_id");

					if (!branch_id.equals("0")) {
						StrSearch += " AND veh_branch_id = " + branch_id + "";
					}

					if (!empcrm_id.equals("") && !empcrm_id.equals("0")) {
						StrSearch += " AND vehfollowup_emp_id = " + empcrm_id + "";
					}

					StrSearch += " AND SUBSTR(vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTR(vehfollowup_followup_time, 1, 8)  <= SUBSTR('" + endtime + "', 1, 8) ";

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ServiceDetails(request, response);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		empcrm_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		start_time = PadQuotes(request.getParameter("txt_start_time"));
		end_time = PadQuotes(request.getParameter("txt_end_time"));
		if (start_time.equals("")) {
			start_time = strToShortDate(ToShortDate(kknow()));
		}

		if (end_time.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
		}

		starttime = ConvertShortDateToStr(start_time);
		endtime = ConvertShortDateToStr(end_time);

	}

	// protected void CheckForm() {
	// msg = "";
	// if (branch_id.equals("0")) {
	// msg = msg + "<br>Select Branch!";
	// }
	// if (empcrm_id.equals("0")) {
	// msg = msg + "<br>Service Advisor!";
	// }
	// if (starttime.equals("")) {
	// msg = msg + "<br>Select Start Date!";
	// }
	// if (!starttime.equals("")) {
	// if (isValidDateFormatShort(starttime)) {
	// starttime = ConvertShortDateToStr(starttime);
	// start_time = strToShortDate(starttime);
	// } else {
	// msg = msg + "<br>Enter Valid Start Date!";
	// starttime = "";
	// }
	// }
	// if (endtime.equals("")) {
	// msg = msg + "<br>Select End Date!";
	// }
	// if (!endtime.equals("")) {
	// if (isValidDateFormatShort(endtime)) {
	// endtime = ConvertShortDateToStr(endtime);
	// if (!starttime.equals("") && !endtime.equals("") &&
	// Long.parseLong(starttime) > Long.parseLong(endtime)) {
	// msg = msg + "<br>Start Date should be less than End date!";
	// }
	// end_time = strToShortDate(endtime);
	// // endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
	// } else {
	// msg = msg + "<br>Enter Valid End Date!";
	// endtime = "";
	// }
	// }

	// }
	public void ServiceDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT "
					+ " veh_id AS 'Veh ID',"
					+ " veh_reg_no AS 'Veh Reg.No',"
					+ " branch_name  AS 'Branch Name', "
					+ " veh_chassis_no  AS 'Veh Chassis. No',"
					+ " veh_engine_no AS 'Veh Engine. No',"
					+ " veh_iacs AS 'Veh. IACS',"
					+ " veh_modelyear AS 'Model Year',"
					+ " item_name AS 'Variant',"
					+ " COALESCE(veh_kms, 0) AS 'Veh Kms',"
					+ " COALESCE(veh_cal_kms, 0) AS 'Veh Cal Kms', "
					+ " COALESCE(DATE_FORMAT(veh_lastservice, '%d/%m/%Y'), '') AS 'Last Service Date',"
					+ " COALESCE(veh_lastservice_kms, 0) AS 'Veh Lastservice Kms',"
					+ " COALESCE(DATE_FORMAT(veh_calservicedate, '%d/%m/%Y'), '') AS 'Cal.Service Date',"
					+ " COALESCE(veh_service_duekms, 0) AS 'Duekms',"
					+ " COALESCE(DATE_FORMAT(veh_service_duedate, '%d/%m/%Y'), '') AS 'Service Due Date',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y'), '') AS 'Veh Sale Date',"
					+ " COALESCE(DATE_FORMAT(veh_renewal_date, '%d/%m/%Y'), '') AS 'Veh ReNewal Date',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS 'Customer Name',"
					+ " contact_mobile1 AS 'Mobile 1',"
					+ " contact_mobile2 AS 'Mobile 2',"
					+ " contact_email1 AS 'Email 1',"
					+ " contact_email2 AS 'Email 2',"
					+ " COALESCE(concat(ser.emp_name,' (', ser.emp_ref_no, ')'), '')  AS 'Service Advisor', "
					+ " COALESCE(concat(crm.emp_name,' (', crm.emp_ref_no, ')'), '') AS 'CRM Executive', "
					+ " COALESCE(DATE_FORMAT(vehfollowup_followup_time, '%d/%m/%Y'), '') AS 'Veh.Followup Time',"
					+ " COALESCE(vehfollowup_desc, '') AS 'Vehfollowup Desc',"
					+ " COALESCE(vehfollowup_kms, 0) AS 'Veh Followup Kms',"
					+ " COALESCE(vehcalltype_name, '') AS 'Call Type Name'"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ser ON ser.emp_id = veh_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm ON crm.emp_id = vehfollowup_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup_calltype ON vehcalltype_id = vehfollowup_vehcalltype_id "
					+ " WHERE 1=1"
					+ " " + StrSearch
					+ " GROUP BY vehfollowup_id"
					+ " ORDER BY vehfollowup_id DESC"
					+ " limit " + exportcount;

			// SOP("StrSql====service due details========" +
			// StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs1, "ServiceDetails", comp_id);
			crs1.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateCRMExecutives(String comp_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_crm = 1"
					+ " AND emp_branch_id = " + branch_id
					// + " " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql==crm exe===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), empcrm_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_branch_id\" id=\"dr_branch_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
