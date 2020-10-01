package axela.jobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Jobs_PBF_WaitingPeriod extends Connect {

	public String StrHTML = "";
	public String comp_id = "0";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				UpdatePBFFollowUp();
			} else {
				comp_id = "1009";
				UpdatePBFFollowUp();
				Thread.sleep(100);
				comp_id = "1011";
				UpdatePBFFollowUp();
			}

			StrHTML = "Jobs PBF Waiting Period Escalation Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	void UpdatePBFFollowUp() throws SQLException {

		StrSql = "SELECT so_enquiry_id, so_id, "
				+ " COALESCE ( ( SELECT CASE WHEN crmdays_exe_type = 1 "
				+ " THEN ( CASE WHEN crmdays_crmtype_id = 1 "
				+ " THEN team_crm_emp_id WHEN crmdays_crmtype_id = 2 "
				+ " THEN team_pbf_emp_id WHEN crmdays_crmtype_id = 3 "
				+ " THEN team_psf_emp_id END ) WHEN crmdays_exe_type = 2 "
				+ " THEN so_emp_id WHEN crmdays_exe_type = 3 "
				+ " THEN team_emp_id END "
				+ " FROM " + compdb(comp_id) + "axela_sales_team "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id "
				+ " WHERE team_branch_id = so_branch_id "
				+ " AND teamtrans_emp_id = so_emp_id LIMIT 1 ), so_emp_id ) AS crmempid,"
				+ " crmdays_id, "
				+ " DATE_FORMAT( DATE_ADD( concat( substr(so_date, 1, 8), '110000' ), "
				+ " INTERVAL (crmdays_daycount - 1) DAY ), '%Y%m%d%H%i%s' )"
				+ " FROM " + compdb(comp_id) + "axela_sales_so "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_brand_id = branch_brand_id "
				+ " WHERE 1 = 1 "
				+ " AND DATEDIFF(" + ToLongDate(kknow()) + ", so_date) = (crmdays_daycount - 1) "
				+ " AND crmdays_so_inactive = 0 "
				+ " AND crmdays_active = 1 "
				+ " AND crmdays_crmtype_id = 2 "
				+ " AND crmdays_waitingperiod = 1 "
				+ " AND so_active = 1 "
				+ " AND so_retail_date = '' "
				+ " AND concat( so_enquiry_id, '-', so_id, '-', crmdays_id ) "
				+ " NOT IN ( SELECT concat( crm_enquiry_id, '-', crm_so_id, '-', crm_crmdays_id ) "
				+ " FROM " + compdb(comp_id) + "axela_sales_crm )";
		// SOP("StrSql==1=" + StrSql);
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crm "
				+ "( crm_enquiry_id, "
				+ " crm_so_id, "
				+ " crm_emp_id, "
				+ " crm_crmdays_id, "
				+ " crm_followup_time ) "
				+ StrSql;
		// SOP("StrSql==2=" + StrSql);
		updateQuery(StrSql);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
