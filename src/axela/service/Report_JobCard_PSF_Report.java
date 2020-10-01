//sangita
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_JobCard_PSF_Report extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "0", dr_branch_id = "0", executive_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String comp_id = "0";
	public String historydate = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String jc_emp_id = "";
	public String jc_id = "";
	public String pending_followup = "";
	public String msg = "";
	public String jcpsfdays_id = "";
	public String jcpsf_jcpsffeedbacktype_id = "";
	public String exportcount = "";
	public String export = "";
	public String startdate = "";
	StringBuilder strpendingso = new StringBuilder();
	public String emp_all_exe = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				go = PadQuotes(request.getParameter("submit_button"));
				export = PadQuotes(request.getParameter("export"));
				GetValues(request, response);
				SOP("startdate = " + startdate);
				if (go.equals("Go")) {
					CheckForm();
					if (emp_all_exe.equals("0"))
					{
						StrSearch = ExeAccess.replace("emp_id", "psf.emp_id") + BranchAccess.replace("branch_id", "jc_branch_id") + "";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + "  and jc_branch_id = " + dr_branch_id;
					}
					StrSearch = StrSearch + " and SUBSTR(jcpsf_followup_time, 1, 6) >= SUBSTR('" + startdate + "', 1, 6)";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						JCPSFDetails(request, response);
					}
				}
				// if (go.equals("Go")) {
				// if (!GetSession("jcpsfstrsql", request).equals("")) {
				// StrSearch = GetSession("jcpsfstrsql", request);
				// }
				// }
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		startdate = PadQuotes(request.getParameter("dr_startmonth"));
		// if (!startdate.equals("") ) {
		// if (Double.parseDouble(startdate) > Double.parseDouble(enddate)) {
		// msg = "<br>From date should be less than end date!";
		// }
		// }
	}

	public String PopulateStartMonth() {
		if (startdate.equals("")) {
			startdate = SplitYear(ToLongDate(kknow())) + SplitMonth(ToLongDate(kknow())) + "01000000";
		}
		StringBuilder year = new StringBuilder();
		int curryear = Integer.parseInt(SplitYear(ToLongDate(kknow())));
		for (int i = curryear - 1; i <= curryear + 1; i++) {
			for (int j = 1; j <= 12; j++) {
				year.append("<option value=").append(i).append(doublenum(j)).append("01000000 ").append(StrSelectdrop(i + doublenum(j) + "01000000", startdate)).append(">")
						.append(StrShorttoMonthYear("01/" + doublenum(j) + "/" + i)).append("</option>\n");
			}
		}
		return year.toString();
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		}

	}

	public void JCPSFDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			// ////////////Over all experience with workshop
			StrSql = " select jc_id AS 'DATE OF CALL',"
					+ " coalesce(DATE_FORMAT(jc_time_in, '%d/%m/%Y %h:%i'),'')  AS 'RO (MTD--3 days)',"
					+ " coalesce(DATE_FORMAT(jc_time_out, '%d/%m/%Y %h:%i'),'')  AS 'PSF done count',"
					+ " item_name AS 'Contact %Age',"
					+ " jcstage_name AS 'ES', "
					+ " veh_reg_no AS 'VS',"
					+ " jc_ro_no AS '%age satisfied',"
					+ " coalesce(DATE_FORMAT(veh_sale_date, '%d/%m/%Y %h:%i'),'') AS 'FS',"
					// +
					// " if(substr(veh_sale_date,1,8) < (select DATE_FORMAT(SUBDATE(NOW(),INTERVAL 6 month), '%Y%m%d')), 'Old','New') AS 'Type',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS '%age Neutral',"
					+ " COALESCE(contact_mobile1,'') AS 'NVS',"
					+ " COALESCE(contact_mobile2,'') AS 'NVSA',"
					+ " COALESCE(contact_email1,'') AS '%age Dissatisfied',"
					// ////////Repeat Repair Count
					+ " COALESCE(contact_email2,'') AS 'Yes Q2',"
					+ " concat(e.emp_name,' (', e.emp_ref_no, ')') as 'Yes Q3',"
					+ " concat(psf.emp_name,' (', psf.emp_ref_no, ')') as '%age',"
					// //////Delievered on promise time
					+ " coalesce(DATE_FORMAT(jcpsf_followup_time, '%d/%m/%Y %h:%i'),'') AS 'YES',"
					+ " COALESCE(CONCAT(jcpsfdays_daycount,' ',jcpsfdays_desc),'') AS 'NO',"
					+ " (select jcpsfrating_desc from " + compdb(comp_id) + "axela_service_jc_psf_rating where jcpsfrating_id=jcpsf_q1_rating) AS '%Age of yes',"
					// //////Explanation of invoice
					+ " if(jcpsf_q2_rating=1,'YES',if(jcpsf_q2_rating=2,'NO','')) AS 'ES',"
					+ " if(jcpsf_q3_rating=1,'YES',if(jcpsf_q3_rating=2,'NO',''))  AS 'VS',"
					+ " (select jcpsfrating_desc from " + compdb(comp_id) + "axela_service_jc_psf_rating where jcpsfrating_id=jcpsf_q11_rating) AS '%Age',"
					+ " (select jcpsfrating_desc from " + compdb(comp_id) + "axela_service_jc_psf_rating where jcpsfrating_id=jcpsf_q12_rating) AS 'FS',"
					+ " (select jcpsfconcern_desc from " + compdb(comp_id) + "axela_service_jc_psf_concern where jcpsfconcern_id=jcpsf_jcpsfconcern_id) AS '%age',"
					+ " jcpsf_desc AS 'NVS',"
					+ " coalesce(jcpsffeedbacktype_name,'') as 'NVSA'"
					+ " coalesce(jcpsffeedbacktype_name,'') as '%age'"
					// //////Not contactable
					+ " coalesce(jcpsffeedbacktype_name,'') as '%Age'"
					+ " from " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " inner join " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jc_id = jc_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " inner join " + compdb(comp_id) + "axela_service_veh on veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays on jcpsfdays_id = jcpsf_jcpsfdays_id"
					+ " inner join " + compdb(comp_id) + "axela_emp e on e.emp_id = jc_emp_id"
					+ " inner join " + compdb(comp_id) + "axela_emp psf on psf.emp_id = jcpsf_emp_id"
					+ " left join " + compdb(comp_id) + "axela_service_jc_psffeedbacktype on jcpsffeedbacktype_id = jcpsf_jcpsffeedbacktype_id"
					+ " where 1=1 and jc_active = 1"
					+ StrSearch
					+ " order by jc_id";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs, "PSFReport", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
