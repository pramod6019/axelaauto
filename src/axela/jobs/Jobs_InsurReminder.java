package axela.jobs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Jobs_InsurReminder extends Connect {
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0";
	public String veh_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	CachedRowSet crs = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			// veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
			// SOP("veh_id-------" + veh_id);
			if (AppRun().equals("0")) {
				comp_id = "1000";
			} else {
				comp_id = "1009";
				// InsurReminder();
				// Thread.sleep(100);
				// comp_id = "1011";
				// InsurReminder();
				// Thread.sleep(100);
				// comp_id = "1017";
				// InsurReminder();
				Thread.sleep(100);
				comp_id = "1022";
				InsurReminder();
			}

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			InsurReminder();
			conntx.commit();
			StrHTML = "Insurance Reminder Job Run Successfully!";
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("Conn is closed....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOP("Connection rollback....");
			}
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void InsurReminder() throws SQLException {

		try {
			StrSql = "SELECT insurreminder_id, insurreminder_days, insurreminder_email_enable,"
					+ " insurreminder_email_sub, insurreminder_email_format,"
					+ " insurreminder_sms_enable, insurreminder_sms_format"
					+ " FROM " + compdb(comp_id) + "axela_insurance_reminder"
					+ " WHERE 1=1"
					+ " AND insurreminder_email_enable = 1"
					+ " AND insurreminder_sms_enable = 1"
					+ " ORDER BY insurreminder_days";
			// SOP("InsurReminder-----------" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (crs.getString("insurreminder_email_enable").equals("1")) {
					SendEmail(crs.getString("insurreminder_days"), crs.getString("insurreminder_email_sub"), crs.getString("insurreminder_email_format"));
				}
				if (crs.getString("insurreminder_sms_enable").equals("1")) {
					SendSms(crs.getString("insurreminder_days"), crs.getString("insurreminder_sms_format"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("Conn is closed....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOP("Connection rollback....");
			}
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void SendEmail(String insurreminder_days, String insurreminder_email_sub, String insurreminder_email_format) throws SQLException {
		String emailmsg, sub;
		emailmsg = "'" + insurreminder_email_format + "'";
		sub = "'" + insurreminder_email_sub + "'";

		sub = "replace(" + sub + ",'[VEHICLEID]',veh_id)";
		sub = "replace(" + sub + ",'[VEHICLEREGISTRATIONNO]',veh_reg_no)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[VARIANTNAME]',item_name)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[INSUREXENAME]',COALESCE(emp_name, ''))";
		sub = "replace(" + sub + ",'[INSUREXEJOBTITLE]',COALESCE(jobtitle_desc, ''))";
		sub = "replace(" + sub + ",'[INSUREXEMOBILE1]',COALESCE(emp_mobile1, ''))";
		sub = "replace(" + sub + ",'[INSUREXEPHONE1]',COALESCE(emp_phone1, ''))";
		sub = "replace(" + sub + ",'[INSUREXEEMAIL1]',COALESCE(emp_email1, ''))";

		emailmsg = "replace(" + emailmsg + ",'[VEHICLEID]',veh_id)";
		emailmsg = "replace(" + emailmsg + ",'[VEHICLEREGISTRATIONNO]',veh_reg_no)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[VARIANTNAME]',item_name)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXENAME]',COALESCE(emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEJOBTITLE]',COALESCE(jobtitle_desc, ''))";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEMOBILE1]',COALESCE(emp_mobile1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEPHONE1]',COALESCE(emp_phone1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[INSUREXEEMAIL1]',COALESCE(emp_email1, ''))";

		try {
			StrSql = "SELECT veh_branch_id,"
					+ " veh_contact_id,"
					+ " veh_insuremp_id,"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " branch_email1,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " '',"
					+ " " + (sub) + ","
					+ " " + (emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " 1"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = veh_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " LEFT join " + compdb(comp_id) + "axela_emp on emp_id = veh_insuremp_id"
					+ " LEFT join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id  "
					+ " WHERE 1=1"
					+ " AND contact_email1 !=''"
					+ " AND veh_renewal_date !=''"
					+ " AND SUBSTR(veh_renewal_date, 5, 4) = "
					+ " SUBSTR('" + ToLongDate(AddHoursDate(kknow(), Integer.parseInt(insurreminder_days), 0, 0)) + "', 5, 4)"
					+ " GROUP BY veh_id"
					+ " ORDER BY veh_id";
			// SOP("StrSql---------email 1----------" + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_contact_id, "
					+ " email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_bcc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			// SOP("InsurReminder---email 2--------" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("Conn is closed....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOP("Connection rollback....");
			}
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void SendSms(String insurreminder_days, String insurreminder_sms_format) throws SQLException {

		String smsmsg = "'" + insurreminder_sms_format + "'";

		smsmsg = "replace(" + smsmsg + ",'[VEHICLEID]',veh_id)";
		smsmsg = "replace(" + smsmsg + ",'[VEHICLEREGISTRATIONNO]',veh_reg_no)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[VARIANTNAME]',item_name)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXENAME]',COALESCE(emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEJOBTITLE]',COALESCE(jobtitle_desc, ''))";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEMOBILE1]',COALESCE(emp_mobile1, ''))";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEPHONE1]',COALESCE(emp_phone1, ''))";
		smsmsg = "replace(" + smsmsg + ",'[INSUREXEEMAIL1]',COALESCE(emp_email1, ''))";
		try {
			// ////// stmttx = conntx.createStatement();
			StrSql = "SELECT veh_branch_id,"
					+ " veh_insuremp_id,"
					+ " veh_contact_id,"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " contact_mobile1,"
					+ " " + (smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " 1"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = veh_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " LEFT join " + compdb(comp_id) + "axela_emp on emp_id = veh_insuremp_id"
					+ " LEFT join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id,  "
					+ compdb(comp_id) + " axela_config"
					+ " WHERE 1=1"
					+ " AND contact_mobile1 !=''"
					+ " AND veh_renewal_date !=''"
					+ " AND SUBSTR(veh_renewal_date, 5, 4) = "
					+ " SUBSTR('" + ToLongDate(AddHoursDate(kknow(), Integer.parseInt(insurreminder_days), 0, 0)) + "', 5, 4)"
					+ " GROUP BY veh_id"
					+ " ORDER BY veh_id";
			// SOP("StrSql------------sms-------" + StrSqlBreaker(StrSql));

			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_emp_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql-------sms---------" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("Conn is closed....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOP("Connection rollback....");
			}
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}

}
