package axela.sales;
// dilip kumar

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_CRM_Details extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String from_date = "";
	public String todate = "", to_date = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String dr_branch_id = "0";
	public String msg = "", ExeAccess = "";
	public String crmfollowupdays_id = "0";
	public String crmdays_crmtype_id = "0";
	public Report_CRMFollowup rep = new Report_CRMFollowup();
	public MIS_Check1 mis = new MIS_Check1();
	public String emp_all_exe = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				exportB = PadQuotes(request.getParameter("btn_export"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				crmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
				// SOP("crmfollowupdays_id==doGet==" + crmfollowupdays_id);
				crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
				GetValues(request, response);
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}

				if (exportB.equals("Export")) {
					// GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						CRMDetails(request, response);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		from_date = PadQuotes(request.getParameter("txt_starttime"));
		to_date = PadQuotes(request.getParameter("txt_endtime"));
		dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		if (from_date.equals("")) {
			from_date = strToShortDate(ToShortDate(kknow()));
		}
		if (to_date.equals("")) {
			to_date = strToShortDate(ToShortDate(kknow()));
		}
		crmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
		crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (crmdays_crmtype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
		if (from_date.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatShort(from_date)) {
				// from_date = ConvertShortDateToStr(from_date);
			} else {
				msg = msg + "<br>Enter Valid From Date!";
				from_date = "";
			}
		}
		if (to_date.equals("")) {
			msg = msg + "<br>Select Date!";
		} else {
			if (isValidDateFormatShort(to_date)) {
				// to_date = ConvertShortDateToStr(to_date);
			} else {
				msg = msg + "<br>Enter Valid To Date!";
				to_date = "";
			}
		}

		if (!from_date.equals("") && !to_date.equals("")) {
			if (Long.parseLong(ConvertShortDateToStr(from_date)) > Long.parseLong(ConvertShortDateToStr(to_date))) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
			if (getDaysBetween(ConvertShortDateToStr(from_date), ConvertShortDateToStr(to_date)) > 31) {
				msg = msg + "<br>Date duration cannot exceed 31 Days!";
			}
		}

		// if (!dr_branch_id.equals("0")) {
		// StrSearch += " AND enquiry_branch_id = " + dr_branch_id + "";
		// }
		// else {
		// msg = msg + "<br>Select Branch!";
		// }
		if (crmfollowupdays_id.equals("0")) {
			msg = msg + "<br>Select Follow-up Day!";
		}

	}

	public void CRMDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String strcf = "";
			String Strcftitle = "";
			StrSql = "SELECT crmcf_id, crmcf_title, cftype_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crmcf_crmdays_id"
					+ " INNER JOIN axela_cf_type ON cftype_id = crmcf_cftype_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id "
					+ " WHERE 1=1 "
					+ " AND crmcf_active = 1"
					+ " AND crmdays_active = 1";
			if (!dr_branch_id.equals("0")) {
				StrSql += " AND branch_id = " + dr_branch_id;
			}
			StrSql += " AND crmdays_id = " + crmfollowupdays_id
					// + " AND crmcftrans_crm_id = " + + ""
					+ " GROUP BY crmcf_id"
					+ " ORDER BY crmdays_daycount, crmcf_rank";
			// SOP("StrSql=cf=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					strcf += "";
					if (crs.getString("cftype_id").equals("6")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(crmcftrans_value, '%d/%m/%Y %h:%i'), '') AS 'crmcftrans_value'";
					} else if (crs.getString("cftype_id").equals("5")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(crmcftrans_value, '%d/%m/%Y'), '') AS 'crmcftrans_value'";
					} else {
						strcf += "(SELECT COALESCE(crmcftrans_value,'') AS 'crmcftrans_value'";
					}
					strcf += " FROM " + compdb(comp_id) + "axela_sales_crm_trans "
							+ " WHERE 1 = 1"
							+ " AND crmcftrans_crm_id = crm_id"
							+ " AND crmcftrans_crmcf_id = " + crs.getString("crmcf_id")
							+ " GROUP BY crmcftrans_crmcf_id ) AS '" + crs.getString("crmcf_title") + "',";

					// strcf += " ) AS tbltrans" + count + " ON " + "tbltrans" + count + ".crmcftrans_crm_id = crm_id";
					// Strcftitle += " COALESCE (tbltrans" + count + ".crmcftrans_value,'') AS '" + crs.getString("crmcf_title") + "',";
					count++;
				}
				// strcf = strcf.substring(0, strcf.length() - 1);
			}

			// SOP("strcf==" + strcf);
			crs.close();
			StrSql = " SELECT "
					+ " enquiry_id AS 'Enquiry ID',"
					+ " enquiry_no AS 'Enquiry No.',"
					+ " COALESCE(soe_name,'') AS 'Source Of Enquiry',"
					+ " COALESCE(sob_name,'') AS 'Source of Business',"
					+ " DATE_FORMAT(enquiry_date, '%d/%m/%Y') AS 'Enquiry Date',"
					+ " enquiry_dmsno AS 'DMS No.',"
					+ " COALESCE(vehstock_engine_no, '') AS 'Engine No.',"
					+ " COALESCE(vehstock_chassis_no, '') AS 'Chassis No.',"
					+ " customer_name AS 'Customer Name',"
					+ " concat(title_desc,' ', contact_fname,' ', contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS 'Contact Mobile1',"
					+ " contact_email1 AS 'Contact Email1',"
					+ " contact_mobile2 AS 'Contact Mobile2',"
					+ " contact_email2 AS 'Contact Email2',"
					+ " contact_phone1 AS 'Contact Phone1',"
					+ " contact_phone2 AS 'Contact Phone2',"
					+ " item_name AS 'Model/Variant',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Colour',"
					+ " team.emp_name AS 'Team Leader',"
					+ " enquiryexe.emp_name AS 'Sales Consultant',"
					// + " COALESCE (occ_name, '') AS 'Occupation',"
					+ " COALESCE((SELECT occ_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ "
					+ " WHERE occ_id = enquiry_occ_id),'') AS 'Occupation',"
					+ " COALESCE (so_id, 0) AS 'SO ID',"
					+ " COALESCE (so_no, '') AS 'SO No.',"
					+ " COALESCE (DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'Date of Booking',"
					+ " COALESCE(so_reg_perm_reg_no, '') AS 'Reg No.',"
					+ " COALESCE(so_allot_no, '') AS 'Allotment No',"
					// + " COALESCE (DATE_FORMAT(crm_followup_time, '%d/%m/%Y'), '') AS 'Contact Date',"
					+ " COALESCE (DATE_FORMAT(IF(crm_modified_id = '',crm_entry_time,crm_modified_time),'%d/%m/%Y'),'') AS 'Contact Date',"
					+ " COALESCE (so_booking_amount, '') AS 'Booking Amount',"
					+ " COALESCE (DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date',"
					+ " COALESCE (DATE_FORMAT(so_promise_date, '%d/%m/%Y'), '') AS 'Tentative Delivery Date',"
					+ " COALESCE (DATE_FORMAT(so_payment_date, '%d/%m/%Y'), '') AS 'SO Payment Date',"
					+ " COALESCE (DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), '') AS 'SO Deliverey Date',"
					+ " DATE_FORMAT(crmdays_modified_date, '%d/%m/%Y') AS 'Customer Contact Date',"
					+ strcf
					+ " crmtype_name AS 'Type',"
					+ " COALESCE(CONCAT(crmdays_daycount, crmdays_desc),'') AS 'CRM Days',"
					+ " COALESCE((SELECT cancelreason_name FROM " + compdb(comp_id) + "axela_sales_so_cancelreason WHERE cancelreason_id = so_cancelreason_id),'') AS 'Cancel Reason',"
					+ " COALESCE(crm_desc,'') AS 'Feedback',"
					+ " crmfeedbacktype_name AS 'Feedback Type',"
					+ " IF(crm_satisfied = 1,'Satisfied',IF(crm_satisfied = 2,'Dis-Satisfied',\"\")) AS 'Satisfied',"
					+ " COALESCE(entryby.emp_name,'') AS 'Entry By'"

					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp enquiryexe on enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp team ON team.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryby ON entryby.emp_id = crm_entry_id"

					+ " WHERE 1 = 1 "
					+ " AND crm_emp_id != 0";
			if (!dr_branch_id.equals("0")) {
				StrSql += " AND enquiry_branch_id = " + dr_branch_id;
			}
			StrSql += " AND SUBSTR(crm_followup_time,1,8) >= " + ConvertShortDateToStr(from_date).substring(0, 8)
					+ " AND SUBSTR(crm_followup_time,1,8) <= " + ConvertShortDateToStr(to_date).substring(0, 8);

			if (!crmfollowupdays_id.equals("0")) {
				StrSql += " and crm_crmdays_id = " + crmfollowupdays_id;
			}

			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " and crmdays_crmtype_id = " + crmdays_crmtype_id;
			}

			StrSql += ExeAccess.replace("emp_id", "crm_emp_id");
			StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id");
			StrSql += " GROUP BY crm_id "
					+ " ORDER BY crm_followup_time"

					+ " LIMIT " + exportcount;
			// SOPInfo("StrSql==Report CRM Details--------------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				new ExportToXLSX().Export(request, response, crs, "CRMDetails", comp_id);
			} else {
				msg = "No records to export!";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateCRMType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select crmtype_id, crmtype_name"
					+ " from axela_sales_crm_type order by crmtype_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"),
						crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
}
