package axela.insurance;
// dilip kumar

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_Insurance_Details extends Connect {

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
	public String insurfollowup_followuptype_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				exportB = PadQuotes(request.getParameter("btn_export"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				// SOP("branch_id----from session--"+branch_id);
				GetValues(request, response);
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}

				if (exportB.equals("Export")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						InsuranceDetails(request, response);
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
		from_date = PadQuotes(request.getParameter("txt_from_date"));
		to_date = PadQuotes(request.getParameter("txt_to_date"));

		if (from_date.equals("")) {
			from_date = strToShortDate(ToShortDate(kknow()));
		}
		if (to_date.equals("")) {
			to_date = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		insurfollowup_followuptype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
	}

	protected void CheckForm() {
		msg = "";
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
		if (!dr_branch_id.equals("0")) {
			StrSearch += " AND enquiry_branch_id = " + dr_branch_id + "";
		}
		// else {
		// msg = msg + "<br>Select Branch!";
		// }
	}

	public void InsuranceDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = " SELECT "
					+ " insurenquiry_id AS 'Insurance Enquiry.Id.', "
					+ " variant_name AS 'Variant Name', "
					+ " CONCAT(branch_name, '') AS 'Branch Name', "
					+ " customer_id AS 'Customer Id.', "
					+ " customer_name AS 'Customer Name', "
					+ " contact_id AS 'Contact Id.', "
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS 'Contact Name', "
					+ " COALESCE (contact_mobile1, '') AS 'Mobile1', "
					+ " COALESCE (contact_mobile2, '') AS 'Mobile2', "
					+ " COALESCE (contact_email1, '') AS 'Email1', "
					+ " COALESCE (contact_email2, '') AS 'Email2', "
					+ " insurenquiryfollowup_id AS 'Insur Follow-up Id.', "
					+ " COALESCE (DATE_FORMAT(insurenquiryfollowup_followup_time,'%d/%m/%Y'),'') AS 'Insur Follow-up Time', "
					+ " COALESCE (insurenquiryfollowup_desc, '') AS 'Insur Follow-up Desc.', "
					+ " COALESCE (emp_id, '') AS 'Insur Follow-up Emp Id.', "
					+ " COALESCE (concat(emp_name,' (',emp_ref_no,')'),'') AS 'Insur Follow-up Emp Name', "
					+ " insurenquiry_reg_no AS 'Insurenquiry.Reg.No.', "
					+ " COALESCE (insurfollowuptype_name, '') AS 'Type' "
					+ " FROM  " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_insurance_enquiry_followup ON insurenquiryfollowup_insurenquiry_id = insurenquiry_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_insurance_enquiry_followup_type ON insurfollowuptype_id = insurenquiryfollowup_followuptype_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id "
					+ " WHERE 1 = 1 ";

			if (!dr_branch_id.equals("0")) {
				StrSql += " and branch_id = " + dr_branch_id + "";
			}
			StrSql += " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) >= " + ConvertShortDateToStr(from_date).substring(0, 8)
					+ " AND SUBSTR(insurenquiryfollowup_followup_time,1,8) <= " + ConvertShortDateToStr(to_date).substring(0, 8);

			if (!insurfollowup_followuptype_id.equals("0")) {
				StrSql += " and insurfollowuptype_id = " + insurfollowup_followuptype_id;
			}
			StrSql += " GROUP BY insurenquiryfollowup_id "
					+ " ORDER BY insurenquiryfollowup_followup_time"
					+ " LIMIT " + exportcount;
			SOP("StrSql===Report Insurance Details===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs, "InsuranceDetails", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurfollowuptype_id, insurfollowuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup_type"
					+ " ORDER BY insurfollowuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfollowuptype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfollowuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("insurfollowuptype_name")).append("</option>\n");
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
