package axela.inventory;
//aJIt 16th July 2013
// Suzuki Stock Import

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.CalcCurrentStockThread;
import cloudify.connect.Connect;

public class Inventory_StockAdjustment_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", jc_service = "", jc_grandtotal = "0.0", month = "", day = "", year = "";
	public String emp_role_id = "0";
	public String prime_id = "0";

	// voucher fields
	public String voucher_id = "0", voucher_vouchertype_id = "0", voucher_customer_id = "0";
	public String voucher_branch_id = "0", voucher_location_id = "0";
	public String voucher_no = "", voucher_date = "", voucher_amount = "0", voucher_rateclass_id = "0", voucher_emp_id = "0";
	public String voucher_entry_id = "0", voucher_entry_date = "";

	// Voucher trans fields
	public String vouchertrans_customer_id = "0", vouchertrans_item_id = "0", vouchertrans_qty = "0";
	public String vouchertrans_price = "0", vouchertrans_netprice = "0", vouchertrans_amount = "0";

	// class variables
	public int count = 0, connCount = 0;
	public String recursion = "";
	public String item_code = "", cat_id = "0", model_id = "0", deleteVouchertrans = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));
			voucher_entry_id = CNumeric(session.getAttribute("emp_id") + "");
			voucher_entry_date = ToLongDate(kknow());
			if (ReturnPerm(comp_id, "emp_service_vehicle_edit", request).equals("1")) {
				AddStockAdjustmentFields(request, response);
			}
			msg = count + " Inventory Stock Adjustment imported successfully!";
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

	public void AddStockAdjustmentFields(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			connCount++;
			SOP("Conn count===========" + connCount);
			StrSql = "SELECT imp_item_code, imp_price, imp_quantity, imp_updatestatus"
					+ " FROM " + compdb(comp_id) + "yamaha_spare_parts_import"
					+ " WHERE imp_updatestatus = '0'"
					+ " LIMIT 40";
			SOP("StrSql==select==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst()) {
				voucher_id = "0";
				voucher_amount = "0";
				recursion = "yes";
				while (crs.next()) {
					// Re-intialize all variables
					item_code = PadQuotes(crs.getString("imp_item_code"));
					prime_id = PadQuotes(crs.getString("imp_item_code"));
					// To be changed based on brand.
					model_id = "74"; // yamaha Parts
					voucher_vouchertype_id = "1"; // Stock Adjustment
					voucher_branch_id = "117"; // yamaha Service
					voucher_location_id = "84"; // Kalamassery Kochi
					voucher_rateclass_id = "6"; // Kochi Purchase
					//

					voucher_customer_id = "5";
					voucher_no = "";
					voucher_date = ToLongDate(kknow());
					deleteVouchertrans = "no";
					vouchertrans_item_id = "0";
					vouchertrans_qty = CNumeric(crs.getString("imp_quantity"));
					vouchertrans_price = CNumeric(crs.getString("imp_price"));
					vouchertrans_amount = String.valueOf(Double.parseDouble(vouchertrans_price) * Double.parseDouble(vouchertrans_qty));
					voucher_amount = String.valueOf(Double.parseDouble(voucher_amount) + Double.parseDouble(vouchertrans_amount));
					if (!item_code.equals("")) {
						StrSql = "SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ "	WHERE item_code = '" + item_code + "'"
								+ "	AND item_model_id = " + model_id
								+ " LIMIT 1";
						SOP("StrSql==item==" + StrSql);
						CachedRowSet crs1 = processQuery(StrSql, 0);
						while (crs1.next()) {
							vouchertrans_item_id = CNumeric(PadQuotes(crs1.getString("item_id")));
						}
						if (vouchertrans_item_id.equals("0")) {
							StrSql = "UPDATE " + compdb(comp_id) + "yamaha_spare_parts_import"
									+ " SET"
									+ " imp_updatestatus = 3" // Item not present
									+ " WHERE imp_item_code = '" + prime_id + "'";
							SOP("StrSql==status-3==" + StrSql);
							stmttx.execute(StrSql);
						}
						crs1.close();

						if (!CNumeric(vouchertrans_item_id).equals("0") && !vouchertrans_qty.equals("0")) {
							if (voucher_id.equals("0")) {
								AddVoucher();
								deleteVouchertrans = "yes";
							}
							if (!voucher_id.equals("0")) {
								AddVouchertrans();
							}
							if (!voucher_id.equals("0")) {
								count++;
								StrSql = "UPDATE " + compdb(comp_id) + "yamaha_spare_parts_import"
										+ " SET"
										+ " imp_updatestatus = 1" // Stock imported successfully
										+ " WHERE imp_item_code = '" + prime_id + "'";
								stmttx.execute(StrSql);
							}
						}
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "yamaha_spare_parts_import"
								+ " SET"
								+ " imp_updatestatus = 5" // Item Code not present
								+ " WHERE imp_item_code = '" + prime_id + "'";
						stmttx.execute(StrSql);
					}
					conntx.commit();
				}
				CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", voucher_location_id, comp_id, voucher_vouchertype_id, "");
				Thread thread = new Thread(calccurrentstockthread);
				thread.start();
			} else {
				recursion = "no";
			}
			SOP("recursion==" + recursion);
			crs.close();
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
			Thread.sleep(5000);
			if (recursion.equals("yes")) {
				AddStockAdjustmentFields(request, response);
			}

		} catch (Exception e) {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
			// This logic is to recall fucntionality even if exception errors while importing..
			if (recursion.equals("yes")) {
				StrSql = "UPDATE " + compdb(comp_id) + "yamaha_spare_parts_import"
						+ " SET"
						+ " imp_updatestatus = 4" // Exception occured due to In-valid data
						+ " WHERE imp_item_code = '" + prime_id + "'";
				SOP("Exception occured due to In-valid data===========");
				updateQuery(StrSql);
				try {
					Thread.sleep(5000);
					if (recursion.equals("yes")) {
						AddStockAdjustmentFields(request, response);
					}
				} catch (InterruptedException ie) {
					SOPError("InterruptedException==");
				}
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
	public void AddVoucher() throws SQLException {
		StrSql = "SELECT COALESCE(MAX(voucher_no), 0) + 1"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_branch_id = " + voucher_branch_id
				+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id;

		voucher_no = CNumeric(ExecuteQuery(StrSql));
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
				+ " ( voucher_vouchertype_id,"
				+ " voucher_branch_id,"
				+ " voucher_location_id,"
				+ " voucher_no,"
				+ " voucher_date,"
				+ " voucher_rateclass_id,"
				+ " voucher_emp_id,"
				+ " voucher_terms,"
				+ " voucher_active,"
				+ " voucher_notes,"
				+ " voucher_entry_id,"
				+ " voucher_entry_date)"
				+ " VALUES"
				+ " (" + voucher_vouchertype_id + ","
				+ " " + voucher_branch_id + ","
				+ " " + voucher_location_id + ","
				+ " " + voucher_no + ","
				+ " " + voucher_date + ","
				+ " " + voucher_rateclass_id + ","
				+ " " + 1 + "," // voucher_emp_id
				+ " ''," // voucher_terms
				+ " '" + 1 + "'," // voucher_active
				+ " ''," // voucher_notes
				+ " " + voucher_entry_id + ","
				+ " '" + voucher_entry_date + "')";
		// SOP("StrSql===Voucher==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			voucher_id = CNumeric(rs.getString(1));
		}
		rs.close();
	}

	public void AddVouchertrans() throws SQLException {

		if (deleteVouchertrans.equals("yes")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + " axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
			stmttx.addBatch(StrSql);
		}
		StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans ("
				+ " vouchertrans_voucher_id,"
				+ " vouchertrans_customer_id,"
				+ " vouchertrans_location_id,"
				+ " vouchertrans_item_id,"
				+ " vouchertrans_rowcount,"
				+ " vouchertrans_qty,"
				+ " vouchertrans_price,"
				+ " vouchertrans_netprice,"
				+ " vouchertrans_convfactor,"
				+ " vouchertrans_unit_cost,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_alt_qty,"
				+ " vouchertrans_alt_uom_id,"
				+ " vouchertrans_time,"
				+ " vouchertrans_dc)"
				+ " VALUES ("
				+ " " + voucher_id + ","
				+ "	" + voucher_customer_id + ","
				+ "	" + voucher_location_id + ","
				+ "	" + vouchertrans_item_id + ","
				+ "	" + 1 + "," // vouchertrans_rowcount
				+ "	" + vouchertrans_qty + ","
				+ "	" + vouchertrans_price + ","
				+ "	" + vouchertrans_amount + "," // vouchertrans_netprice
				+ "	" + 1 + "," // vouchertrans_convfactor
				+ "	" + vouchertrans_price + "," // vouchertrans_unit_cost
				+ "	" + vouchertrans_amount + ","
				+ "	" + vouchertrans_qty + "," // vouchertrans_alt_qty
				+ "	" + 1 + "," // vouchertrans_alt_uom_id
				+ "'" + ToLongDate(kknow()) + "'," // vouchertrans_time
				+ "	'0'" // vouchertrans_dc
				+ ")";
		// SOP("StrSql==debit== " + StrSqlBreaker(StrSql));
		stmttx.addBatch(StrSql);
		// credit the supplier
		StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans ("
				+ "	vouchertrans_voucher_id,"
				+ " vouchertrans_location_id,"
				+ " vouchertrans_netprice,"
				+ " vouchertrans_amount,"
				+ " vouchertrans_time,"
				+ " vouchertrans_dc)"
				+ " VALUES ("
				+ " " + voucher_id + ","
				+ " " + voucher_location_id + ","
				+ " " + vouchertrans_amount + "," // vouchertrans_netprice
				+ " " + vouchertrans_amount + "," // vouchertrans_amount
				+ "'" + ToLongDate(kknow()) + "'," // vouchertrans_time
				+ " '1'" // vouchertrans_dc
				+ " )";
		// SOP("StrSql==credit==" + StrSqlBreaker(StrSql));
		stmttx.addBatch(StrSql);
		StrSql = "UPDATE " + compdb(comp_id) + " axela_acc_voucher"
				+ "	SET voucher_amount = " + voucher_amount
				+ "	WHERE voucher_id = " + voucher_id;
		stmttx.addBatch(StrSql);
		stmttx.executeBatch();
	}
}
