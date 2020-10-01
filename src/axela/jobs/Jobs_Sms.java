package axela.jobs;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.SMSAPI;

public class Jobs_Sms extends Connect {
	
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String smsCount = "";
	
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
			StrHTML = "SMS Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}
	
	public void Execute() throws SQLException {
		CachedRowSet crs = null;
		try {
			StrSql = "Select config_sms_batchcount"
					+ " from " + compdb(comp_id) + "axela_config"
					+ " WHERE config_sms_enable = 1";
			// SOP("StrSql-------------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				smsCount = crs.getString("config_sms_batchcount");
				SendSms();
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		} finally {
			crs.close();
		}
	}
	
	public void SendSms() throws SQLException {
		CachedRowSet crs = null;
		String mobileno = "";
		String sms_msg = "";
		SMSAPI vd = new SMSAPI();
		try {
			StrSql = "Select sms_id, sms_mobileno, sms_msg, brandconfig_sms_url"
					+ " from " + compdb(comp_id) + "axela_sms"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = sms_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
					+ " WHERE sms_sent = '0' AND brandconfig_sms_enable = 1 AND brandconfig_sms_url != ''"
					+ " ORDER BY sms_id LIMIT " + smsCount;
			// SOP("StrSql -------" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sms_msg = unescapehtml(crs.getString("sms_msg"));
				mobileno = crs.getString("sms_mobileno").replaceAll("91-", "");
				if (AppRun().equals("1")) {
					vd.sendSMS(crs.getString("brandconfig_sms_url"), mobileno, sms_msg);
				}
				updateQuery("Update " + compdb(comp_id) + "axela_sms "
						+ " set "
						+ " sms_credits = " + ReturnSmsCredit(sms_msg.length()) + ", "
						+ " sms_sent = '1', "
						+ " sms_date = '" + ToLongDate(kknow()) + "'"
						+ " where sms_id = " + crs.getString("sms_id") + "");
				// SOPInfo("Axelaauto SMS => " + comp_id + " => " + mobileno);
			}
			
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		} finally {
			crs.close();
		}
	}
}
