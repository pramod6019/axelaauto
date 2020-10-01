package axela.accounting;
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

public class VoucherAuthorize_Update extends Connect {

	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", voucher_id = "0", branch_id = "0", voucher_no = "0";

	// class variables
	public int totalCount = 0, updateCount = 0, connCount = 0;

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			voucher_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			voucherAuthorizeAllotVoucherNo(request, response);
			msg = totalCount + " Voucher Authorize updated successfully!";
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

	public void voucherAuthorizeAllotVoucherNo(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			connCount++;
			String tillDate = "20171031";
			SOP("count============" + totalCount);

			StrSql = "SELECT voucher_id, branch_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE SUBSTR(voucher_date, 1, 8) <= '" + tillDate + "'"
					+ " AND branch_brand_id = 6"
					+ " AND voucher_vouchertype_id = 6"
					+ " AND voucher_authorize != '1'"
					+ " AND voucher_no = 0"
					+ "	ORDER BY branch_id";
			SOP("StrSql==select==" + StrSql);
			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					totalCount++;
					voucher_id = crs.getString("voucher_id");
					branch_id = CNumeric(PadQuotes(crs.getString("branch_id")));
					voucher_no = CNumeric(ExecuteQuery("SELECT MAX(voucher_no) + 1 FROM  " + compdb(comp_id) + "axela_acc_voucher WHERE voucher_branch_id = " + branch_id));
					StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
							+ " SET voucher_authorize = '1',"
							+ " voucher_no = " + voucher_no
							+ " WHERE SUBSTR(voucher_date, 1, 8) <= '" + tillDate + "'"
							+ " AND voucher_id = " + voucher_id
							+ " AND branch_brand_id = 6"
							+ " AND voucher_vouchertype_id = 6"
							+ " AND voucher_authorize != '1'"
							+ " AND voucher_no = 0";
					stmttx.executeUpdate(StrSql);
					conntx.commit();
					Thread.sleep(1000);
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
