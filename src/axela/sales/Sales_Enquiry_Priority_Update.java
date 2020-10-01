package axela.sales;
//Gopal 4th March 2017

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sales_Enquiry_Priority_Update extends Connect {
	
	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", enquiry_id = "0";
	
	// class variables
	public int count = 0, connCount = 0;
	
	public Connection conntx = null;
	public Statement stmttx = null;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
			UpdateProfitabiltyFields(request, response);
			msg = count + " Enquiry Priority updated successfully!";
			SOP("Total Conn count==" + connCount);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void UpdateProfitabiltyFields(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			connCount++;
			// String fromdate = ConvertLongDateToStr(AddDayMonthYearStr(ToShortDate(kknow()), 0, 0, -5, 0));
			// SOP("count============" + count);
			if (!enquiry_id.equals("0")) {
				StrSql = "SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_status_id = '1'"
						+ " AND enquiry_id = " + enquiry_id;
			} else {
				StrSql = "SELECT enquiry_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_status_id = '1'";
				// + " AND SUBSTR(enquiry_date, 1, 6) > SUBSTR(" + fromdate + ", 1, 6)";
			}
			
			// SOPInfo("StrSql==UpdateProfitabiltyFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql);
			
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					enquiry_id = crs.getString("enquiry_id");
					EnquiryPriorityUpdate(comp_id, enquiry_id);
					Thread.sleep(1);
				}
			}
			crs.close();
		} catch (Exception e) {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				SOP("Transaction Error==");
				conntx.rollback();
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
			msg = "<br>Transaction Error!";
		} finally {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
		}
	}
}