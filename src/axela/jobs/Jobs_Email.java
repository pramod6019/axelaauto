package axela.jobs;

// 5 march 2013
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Email;

public class Jobs_Email extends Connect {
	
	public String StrHTML = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String ConfigEmailEnable = "";
	public String BatchEmailCount = "";
	public String msg = "";
	
	public Jobs_Email() {
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1009";
				Execute();
			} else {
				comp_id = "1000";
				Execute();
				Thread.sleep(100);
				comp_id = "1009";
				Execute();
				Thread.sleep(100);
				comp_id = "1011";
				Execute();
				Thread.sleep(100);
				comp_id = "1015";
				Execute();
				Thread.sleep(100);
				comp_id = "1017";
				Execute();
				Thread.sleep(100);
				comp_id = "1019";
				Execute();
				Thread.sleep(100);
				comp_id = "1023";
				Execute();
				
			}
			StrHTML = "Email Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}
	
	public void Execute() throws SQLException {
		// SOPError("Starting AxelaAuto-DDMotors Job_Email Scheduled Job!");
		CachedRowSet crs = null;
		try {
			StrSql = "Select config_email_enable, config_email_batchcount"
					+ " from " + compdb(comp_id) + "axela_config";
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				ConfigEmailEnable = crs.getString("config_email_enable");
				BatchEmailCount = crs.getString("config_email_batchcount");
			}
			
			if (ConfigEmailEnable.equals("1")) {
				SendEmail();
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			crs.close();
		}
	}
	
	public void SendEmail() throws SQLException {
		// String email_msg = "";
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT  email_id, email_from, email_to, email_cc, email_bcc,"
					+ " email_subject, email_msg, email_attach1,"
					+ "	COALESCE(branch_id, 0) AS branch_id,"
					+ "	COALESCE(branch_email_server, '') AS branch_email_server,"
					+ "	COALESCE(branch_email_username, '') AS branch_email_username,"
					+ "	COALESCE(branch_email_password, '') AS branch_email_password,"
					+ "	COALESCE(branch_email_port, '0') AS branch_email_port,"
					+ "	COALESCE(branch_email_ssl, '0') AS branch_email_ssl,"
					+ "	COALESCE(branch_email_tls, '0') AS branch_email_tls"
					+ " FROM " + compdb(comp_id) + "axela_email"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch  ON branch_id = email_branch_id"
					+ " WHERE email_sent = '0' ORDER BY email_id LIMIT " + BatchEmailCount;
			// SOP("StrSql ===============" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				updateQuery("Update " + compdb(comp_id) + "axela_email set "
						+ " email_sent = '1', "
						+ " email_date = '" + ToLongDate(kknow()) + "'"
						+ " where email_id = " + crs.getString("email_id") + " ");
				
				if (!crs.getString("branch_email_server").equals("") && !crs.getString("branch_email_username").equals("")
						&& !crs.getString("branch_email_password").equals("") && !CNumeric(crs.getString("branch_email_port")).equals("0")) {
					new Email().postMailCustom(crs.getString("branch_id"),
							crs.getString("branch_email_server"),
							crs.getString("branch_email_username"),
							crs.getString("branch_email_password"),
							crs.getString("branch_email_port"),
							crs.getString("branch_email_ssl"),
							crs.getString("branch_email_tls"),
							crs.getString("email_to"),
							crs.getString("email_cc"),
							crs.getString("email_bcc"),
							crs.getString("email_from"),
							unescapehtml(crs.getString("email_subject")),
							unescapehtml(crs.getString("email_msg")),
							crs.getString("email_attach1"), comp_id);
				} else {
					postMail(crs.getString("email_to"), crs.getString("email_cc"),
							crs.getString("email_bcc"), crs.getString("email_from"),
							unescapehtml(crs.getString("email_subject")),
							unescapehtml(crs.getString("email_msg")),
							crs.getString("email_attach1"), comp_id);
				}
			}
			
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(comp_id + "--To--" + crs.getString("email_to"));
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			crs.close();
		}
	}
}
