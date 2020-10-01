package axela.jobs;

// 5 march 2013
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_Sowaiting_Period_Days extends Connect {

	public String StrHTML = "";
	public String comp_id = "0", emp_id;
	public String StrSql = "";
	public String ConfigEmailEnable = "";
	public String BatchEmailCount = "";
	public String msg = "";
	public String email_from = "";
	public String email_to = "";
	public String email_sub = "", email_msg = "";
	public String so_id = "0", so_branch_id = "0", so_no = "", so_date = "";
	public String so_customer_id = "0", so_contact_id = "0", so_enquiry_id = "0", so_quote_id = "0";
	public Jobs_Sowaiting_Period_Days() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			emp_id = CNumeric(GetSession("emp_id", request));
			if (AppRun().equals("0")) {
				comp_id = "1000";
				Execute();
			} else {
				comp_id = "1009";
				Execute();
			}
			StrHTML = "Email Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	public void Execute() throws SQLException {
		SOPError("Starting AxelaAuto DDMotors Jobs SO Waiting Period Days Scheduled!");
		try {
			email_from = ExecuteQuery("select config_admin_email from " + compdb(comp_id) + "axela_config");

			email_sub = "replace('sowaitingperiod_email_sub','[SOID]',so_id)";
			email_sub = "replace(sowaitingperiod_email_sub,'[SODATE]',so_date)";
			// email_sub = "replace(" + email_sub + ",'[PROMISEDDATE]',so_promise_date)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTID]',contact_id)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTMOBILE2]',contact_mobile2)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTPHONE1]',contact_phone1)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTPHONE2]',contact_phone2)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTEMAIL1]',contact_email1)";
			// sowaitingperiod_email_sub = "replace(" + sowaitingperiod_email_sub + ",'[CONTACTEMAIL2]',contact_email2)";

			email_msg = "replace('sowaitingperiod_email_format','[SOID]',so_id)";
			email_msg = "replace(sowaitingperiod_email_format,'[SODATE]',so_date)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTNAME]',contact_fname)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTID]',contact_id)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTMOBILE1]',contact_mobile1)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTMOBILE2]',contact_mobile2)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTPHONE1]',contact_phone1)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTPHONE2]',contact_phone2)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTEMAIL1]',contact_email1)";
			// sowaitingperiod_email_format = "replace(" + sowaitingperiod_email_format + ",'[CONTACTEMAIL2]',contact_email2)";

			StrSql = " SELECT"
					+ " '" + email_from + "',"
					+ " contact_email1 ,"
					+ " " + email_sub + ","
					+ " " + email_msg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " contact_id,"
					+ " " + emp_id + ","
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_sowaitingperioddays ON sowaitingperiod_brand_id = brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " WHERE 1=1"
					+ " AND so_active = 1"
					+ " AND so_delivered_date = ''"
					+ " AND DATEDIFF('" + ToLongDate(kknow()) + "', so_date) = sowaitingperiod_days";
			SOP("StrSql---------------" + StrSql);
			SendEmail(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void SendEmail(String StrSql) throws SQLException {
		// String email_msg = "";
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " ("
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_contact_id,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			SOP("StrSql-------Email-------" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
