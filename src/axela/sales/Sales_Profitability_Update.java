package axela.sales;
//aJIt 16th July 2013

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

public class Sales_Profitability_Update extends Connect {

	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", so_id = "0";

	// class variables
	public int count = 0, connCount = 0;

	public Connection conntx = null;
	public Statement stmttx = null;
	public Veh_Salesorder_Update obj = new Veh_Salesorder_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			UpdateProfitabiltyFields(request, response);
			msg = count + " Sales Profitability Fields updated successfully!";
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
			String fromdate = ConvertLongDateToStr(AddDayMonthYearStr(ToShortDate(kknow()), 0, 0, -10, 0));
			// SOP("count============" + count);
			if (!so_id.equals("0")) {
				StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so WHERE so_active = '1' AND so_id = " + so_id;
			} else {
				StrSql = "SELECT so_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_active = '1'";
				// + " AND SUBSTR(so_date, 1, 6) >= SUBSTR(" + fromdate + ", 1, 6)";

				// StrSql = "SELECT so_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_stock_aftertax ON stock_vinno = vehstock_chassis_no"
				// + " WHERE stock_profitability_flag = 0"
				// + " AND stock_vinno IS NOT NULL";
				// + " AND SUBSTR(so_date, 1, 6) >= SUBSTR(" + fromdate + ", 1, 6)";
			}

			// SOPInfo("StrSql==UpdateProfitabiltyFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					so_id = crs.getString("so_id");
					obj.UpdateProfitability(so_id, comp_id);
					// updateQuery("UPDATE " + compdb(comp_id) + "axela_stock_aftertax"
					// + " SET stock_profitability_flag = 1");
					Thread.sleep(10);
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
