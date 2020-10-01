package axela.jobs;

// 5 march 2013
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import axela.sales.Report_Monitoring_Board;
import cloudify.connect.Connect;

public class Jobs_Email_Sales_Monitoring_Board extends Connect {
	public String StrHTML = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "";
	public String endtime = "";
	public String date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			Report_Monitoring_Board report = new Report_Monitoring_Board();
			// ListMonitorBoard(String dr_branch_id, String starttime, String endtime, String targetstarttime, String targetendtime, String dr_totalby, String comp_id)
			report.dr_orderby = "mainemp.emp_id";
			comp_id = "1023";

			endtime = ToLongDate(kknow());
			date = ToLongDate(kknow()).substring(6, 8);
			starttime = ToLongDate(AddHoursDate(kknow(), -(Integer.parseInt(date) - 1), 0, 0));

			SOP("starttime======== " + starttime);
			SOP("endtime======== " + endtime);
			SOP("date======== " + date);

			StrHTML = report.ListMonitorBoard("", starttime, endtime, "", "", "3", comp_id).replaceAll("'", "\"");
			if (!StrHTML.equals("")) {
				SendEmail();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	protected void SendEmail() throws SQLException {
		String emailmsg = "", sub;
		emailmsg = StrHTML;
		sub = "Sales Monitoring Board";
		try {
			// StrSql = "SELECT"
			// + " enquiry_branch_id ,"
			// + " 'admin@emax.in',"
			// + " 'ank.khan7@gmail.com',"
			// + " 'team@emax.in',"
			// + " " + unescapehtml(sub) + ","
			// + " " + unescapehtml(emailmsg) + ","
			// + " '" + ToLongDate(kknow()) + "',"
			// + " " + 1 + ","
			// + " 0"
			// + " FROM " + compdb(comp_id) + "axela_sales_enquiry";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " (email_branch_id,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " VALUES "
					+ " (" + 1 + ","
					+ " 'admin@emax.in',"
					+ " 'demo123@gmail.com',"
					+ " 'team@emax.in',"
					+ " '" + unescapehtml(sub) + "',"
					+ " '" + unescapehtml(emailmsg) + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + 1 + ","
					+ " 0"
					+ ")";
			// SOP("SendEmail--------------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
