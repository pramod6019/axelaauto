package axela.service;
// dilip kumar

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_JobCard_PSF_Followup_Details extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String start_time = "";
	public String todate = "", end_time = "";
	public String endtime = "", starttime = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String dr_branch_id = "0";
	public String msg = "", ExeAccess = "";
	public String psfdays_id = "";
	public String psfdays_crmtype_id = "0";
	public String satisfied = "";
	public String emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// for psf branch to band
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				exportB = PadQuotes(request.getParameter("btn_export"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				satisfied = PadQuotes(request.getParameter("satisfied"));
				// SOP("satisfied===" + satisfied);

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
						if (satisfied.equals("yes")) {
							PSFDetails(request, response);
						} else {
							PSFDissatisfiedDetails(request, response);
						}
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
		start_time = PadQuotes(request.getParameter("txt_from_date"));
		end_time = PadQuotes(request.getParameter("txt_to_date"));

		if (start_time.equals("")) {
			start_time = strToShortDate(ToShortDate(kknow()));
		}

		if (end_time.equals("")) {
			end_time = strToShortDate(ToShortDate(kknow()));
		}

		starttime = ConvertShortDateToStr(start_time);
		endtime = ConvertShortDateToStr(end_time);
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		psfdays_id = CNumeric(PadQuotes(request.getParameter("dr_psfdays")));
		psfdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_psfdays_crmtype_id")));

		StrSearch += " AND SUBSTR(jcpsf_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(jcpsf_followup_time, 1, 8)  <= SUBSTR('" + endtime + "', 1, 8) ";
	}

	protected void CheckForm() {
		msg = "";

		if (!dr_branch_id.equals("0")) {
			StrSearch += " AND jc_branch_id = " + dr_branch_id + "";
		} else {
			msg = msg + "<br>Select Branch!";
		}
		if (satisfied.equals("yes")) {
			if (!psfdays_id.equals("0")) {
				StrSearch += " AND psfdays_id = " + psfdays_id + "";
			} else {
				msg = msg + "<br>Select Follow-up Day!";
			}
		}
	}

	public void PSFDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String strcf = "";
			StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, cftype_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsfcf_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id "
					+ " INNER JOIN axelaauto.axela_cf_type ON cftype_id = jcpsfcf_cftype_id "
					+ " WHERE 1=1 "
					+ " AND jcpsfcf_active = 1"
					+ " AND psfdays_active = 1"
					+ " AND branch_id = " + dr_branch_id
					+ " AND psfdays_id = " + psfdays_id
					// + " AND crmcftrans_crm_id = " + + ""
					+ " GROUP BY jcpsfcf_id"
					+ " ORDER BY psfdays_daycount, jcpsfcf_rank";
			// SOP("details query====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getString("cftype_id").equals("6")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y %h:%i'), '') AS 'jcpsfcftrans_value'";
					} else if (crs.getString("cftype_id").equals("5")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y'), '') AS 'jcpsfcftrans_value'";
					} else if (crs.getString("cftype_id").equals("3")) {
						strcf += " (SELECT COALESCE(IF(jcpsfcftrans_value=1, 'YES', 'NO'), '') AS 'jcpsfcftrans_value'";
					}
					else {
						strcf += "(SELECT COALESCE(jcpsfcftrans_value,'') AS 'jcpsfcftrans_value' ";
					}
					strcf += " FROM " + compdb(comp_id) + "axela_service_jc_psf_trans "
							+ " WHERE jcpsfcftrans_jcpsf_id = jcpsf_id"
							+ " AND jcpsfcftrans_jcpsfcf_id = " + crs.getString("jcpsfcf_id") + " Limit 1) as '" + crs.getString("jcpsfcf_title") + "',";

				}
			}
			// SOP("strcf===custom fields==" + strcf);
			crs.close();

			StrSql = "SELECT jc_id AS 'Job Card ID',"
					+ " jc_ro_no AS 'RO NO.',"
					+ " branch_name AS 'Branch',"
					+ " COALESCE(variant_name, '') AS 'Variant',"
					+ " COALESCE(DATE_FORMAT(jc_time_in, '%d/%m/%Y %h:%i'), '')  AS 'Time In',"
					+ " COALESCE(DATE_FORMAT(jc_time_out, '%d/%m/%Y %h:%i'), '')  AS 'Time Out',"
					+ " COALESCE(jc_kms, 0) As 'Kms',"
					// + " jctype_name AS 'Type',"
					// + " jcstage_name AS 'Stage',"
					+ " veh_reg_no AS 'Reg. No.',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y %h:%i'), '') AS 'Date of Sale',"
					+ " IF(substr(veh_sale_date, 1, 8) < (SELECT DATE_FORMAT(SUBDATE(NOW(), INTERVAL 6 month), '%Y%m%d')), 'Old','New') AS 'Type',"
					+ " concat(title_desc,' ',contact_fname, ' ', contact_lname	) AS 'Customer Name',"
					+ " COALESCE(contact_mobile1, '') AS 'Mobile 1',"
					+ " COALESCE(contact_mobile2, '') AS 'Mobile 2',"
					+ " COALESCE(contact_email1, '') AS 'Email 1',"
					+ " COALESCE(contact_email2, '') AS 'Email 2',"
					+ " COALESCE(contact_phone1, '') AS 'Phone 1',"
					+ " COALESCE(contact_phone2, '') AS 'Phone 2',"
					+ " COALESCE(contact_address, '') AS 'Address',"
					+ " COALESCE(jcpsf_desc, '') AS 'Description', "
					+ " CONCAT(psfdays_daycount, psfdays_desc) AS 'PSF Days',"
					+ " COALESCE(psffeedbacktype_name, '') AS 'Feedback Name',"
					+ " IF(jcpsf_satisfied = 1,'Yes',IF(jcpsf_satisfied = 2, 'NO', '')) AS 'Satisfied',"
					+ " COALESCE (entryemp.emp_name, '') AS 'Executive Name',"
					+ " COALESCE(service.emp_name, '') AS 'Service Adviser',"
					+ " COALESCE(DATE_FORMAT(jcpsf_entry_time, '%d/%m/%Y %h:%i'), '') AS 'JobCard Entry Time',";
			if (!strcf.equals("")) {
				StrSql += strcf;
			}
			StrSql += " COALESCE(jcpsfconcern_desc, '') AS 'Concern'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp service ON service.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp SESSION ON SESSION.emp_id = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " LEFT JOIN axela_service_psf_feedbacktype  ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " WHERE jc_active = 1"
					+ StrSearch;
			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");
			}
			StrSql += " GROUP BY jcpsf_id "
					+ " ORDER BY jcpsf_followup_time, jc_id";

			// SOP("StrSql===Report PSF Details=======" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs, "PSFDetails", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PSFDissatisfiedDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String strcf = "";
			StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, cftype_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsfcf_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id "
					+ " INNER JOIN axelaauto.axela_cf_type ON cftype_id = jcpsfcf_cftype_id "
					+ " WHERE 1=1 "
					+ " AND jcpsfcf_active = 1"
					+ " AND psfdays_active = 1"
					+ " AND branch_id = " + dr_branch_id
					+ " AND psfdays_id = " + psfdays_id
					// + " AND crmcftrans_crm_id = " + + ""
					+ " GROUP BY jcpsfcf_id"
					+ " ORDER BY psfdays_daycount, jcpsfcf_rank";
			// SOP("details query====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getString("cftype_id").equals("6")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y %h:%i'), '') AS 'jcpsfcftrans_value'";
					} else if (crs.getString("cftype_id").equals("5")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y'), '') AS 'jcpsfcftrans_value'";
					} else if (crs.getString("cftype_id").equals("3")) {
						strcf += " (SELECT COALESCE(IF(jcpsfcftrans_value=1, 'YES', 'NO'), '') AS 'jcpsfcftrans_value'";
					}
					else {
						strcf += "(SELECT COALESCE(jcpsfcftrans_value,'') AS 'jcpsfcftrans_value' ";
					}
					strcf += " FROM " + compdb(comp_id) + "axela_service_jc_psf_trans "
							+ " WHERE jcpsfcftrans_jcpsf_id = jcpsf_id"
							+ " AND jcpsfcftrans_jcpsfcf_id = " + crs.getString("jcpsfcf_id") + " Limit 1) as '" + crs.getString("jcpsfcf_title") + "',";

				}
			}
			// SOP("strcf===custom fields==" + strcf);
			crs.close();

			StrSql = "SELECT CONCAT(CONCAT(title_desc,' ',contact_fname, ' ', contact_lname),'\n',"
					+ " COALESCE(contact_address, '')) AS 'CUSTOMER DETAILS\nCUSTOMER NAME & ADDERSS',"
					// +
					// " concat(title_desc,' ',contact_fname, ' ', contact_lname	) AS 'CUSTOMER NAME',"
					// + " COALESCE(contact_address, '') AS 'ADDERSS',"
					+ " CONCAT(COALESCE(contact_mobile1, ''),"
					+ " '        ',COALESCE(veh_reg_no, ''), '               ', COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y %h:%i'), ''))"
					+ " AS 'VEHICLE DETAILS\nPHONE NO.     VEHI.MODEL/REGN.NO.     DATE',"
					// +
					// " IF(substr(veh_sale_date, 1, 8) < (SELECT DATE_FORMAT(SUBDATE(NOW(), INTERVAL 6 month), '%Y%m%d')), 'Old','New') AS 'Type',"
					+ " CONCAT( 'GROUP:\nS.A.: ', COALESCE (serviceadvisoremp.emp_name, ''), "
					+ " '\nSUP:\nT.A:\nTEC: ',COALESCE (technicianemp.emp_name, ''),'\nF.I: ') AS 'Service Details',"
					+ " CONCAT('NEW: ', COALESCE(jcpsfconcern_desc, ''),'\nOLD :') AS 'DETAILS OF PSF COMPLAINTS\nProblem/Defects mentioned by the Customer',"
					+ " '' AS 'ACTION TAKEN\nAPPOINTMENT DATE  ATTENDED ON  ACTION TAKEN TO RECTIFY THE PROBLEMS/DEFECTS',"
					+ " '' AS 'D2S ANALYSIS\nREASON FOR GENERATION OF COMPLAINTS',"
					+ " CONCAT('BY:HV/W/E','\n','DAYS: ') AS 'COMPLAINED RESOLVED DETAILS',"
					+ " CONCAT('DATE: \n','STATUS: \n','RATING: \n','SING: \n','PROB: ') AS 'REPEAT FOLLOW UPS\n------------1ST PSF S.A(3RD DAY)     2ND DAY PSF CCM(6TH DAY)     3RD DAY PSF MSIL TSM(15 DAY)     4TH DAY PSF CCE (30TH DAY)'"
					+ " FROM "
					+ compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp SESSION ON SESSION.emp_id = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp serviceadvisoremp ON serviceadvisoremp.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp technicianemp ON technicianemp.emp_id = jc_technician_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp jcexe on jcexe.emp_id = jcpsf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " LEFT JOIN axela_service_psf_feedbacktype  ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " WHERE 1=1"
					+ " AND jcpsf_satisfied = 2 "
					+ " AND jc_active = 1" + StrSearch + ""
					+ " GROUP BY jcpsf_id "
					+ " ORDER BY jcpsf_followup_time, jc_id";

			// SOP("StrSql===Report PSF Dissatisfied Details===" +
			// StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs, "PSFDissatisfiedDetails", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulatePSFDays() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT psfdays_id, concat(psfdays_daycount, psfdays_desc) as psfdays_desc"
					+ " from " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id "
					+ " where branch_id = " + dr_branch_id + ""
					+ " GROUP BY psfdays_id"
					+ " ORDER BY psfdays_daycount";
			// SOP("strsql===psf=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_psfdays id=dr_psfdays class=form-control >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psfdays_id")).append("");
				Str.append(StrSelectdrop(crs.getString("psfdays_id"), psfdays_id));
				Str.append(">").append(crs.getString("psfdays_desc")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
