package axela.accounting;
//shivaprasad 23 Sept 2015     

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

import cloudify.connect.Connect;

public class Adjustment_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0";
	public String so_id = "0", branch_id = "1";
	public String emp_role_id = "0";
	// voucher table
	public String vouchertype_id = "1";
	public String voucher_no = "";
	public String voucher_date = "";
	public String voucher_supplier_id = "0";
	public String voucher_contact_id = "0";
	public String voucher_grandtotal = "0.00";
	public String voucher_po_id = "0", voucher_git_id = "0", voucher_grn_id = "0";
	public String voucher_enquiry_id = "0", voucher_lead_id = "0";
	public String vouchertrans_location_id = "5";
	public String vouchertrans_item_id = "0";
	public String voucher_rateclass_id = "0";
	public String voucher_emp_id = "0";
	public String voucher_gatepass = "";
	public String voucher_lrno = "";
	public String voucher_driver_no = "";
	public String voucher_tempo_no = "";
	public String voucher_cashdiscount = "", voucher_turnoverdisc = "";
	public String voucher_payment_date = "", voucher_billing_add = "", voucher_consignee_add = "";
	public String voucher_special = "0";
	public String voucher_pending = "0";
	public String voucher_ref_no = "";
	public String voucher_active = "1";
	public String voucher_notes = "", voucher_authorize = "", vouchertype_terms = "";
	public String voucher_id = "", vouchertrans_from_location_id = "0";
	public String voucher_entry_id = "0", voucher_entry_date = "";
	public String voucher_amt = "", voucher_qty = "", serial_no = "", vouchertrans_price = "", id = "0", dup_serial_no = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				voucher_emp_id = emp_id;
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				// CheckPerm(comp_id, "emp_service_vehicle_edit", request,
				// response);
				voucher_entry_id = CNumeric(GetSession("emp_id", request));
				voucher_entry_date = ToLongDate(kknow());
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int count = 0;
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			// All Commented fields should be there use when data is provided...
			StrSql = "SELECT id, serial_no,"
					+ " COALESCE(qty, '') AS qty,"
					+ " COALESCE(rate, '') AS rate,"
					+ " COALESCE(value, '') AS value"
					+ " FROM " + compdb(comp_id) + "stock_pdi"
					+ " WHERE active = 0"
					+ " LIMIT 30";
			// SOP("StrSql = veh data===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					id = PadQuotes(crs.getString("id"));
					voucher_grandtotal = PadQuotes(crs.getString("value"));
					voucher_qty = PadQuotes(crs.getString("qty"));
					vouchertrans_price = PadQuotes(crs.getString("rate"));
					serial_no = PadQuotes(crs.getString("serial_no"));
					if (!serial_no.equals("")) {
						String srn[] = serial_no.split(" ");
						serial_no = srn[0];
						// if (serial_no.contains(" 1")) {
						// serial_no = serial_no.substring(0, serial_no.length() - 1);
						// SOP("serial_no==22=" + serial_no);
						// }
						dup_serial_no = ExecuteQuery("SELECT voucher_notes FROM " + compdb(comp_id) + "axela_acc_voucher"
								+ " WHERE voucher_notes = '" + serial_no + "'"
								+ " AND voucher_location_id = " + vouchertrans_location_id);
						if (!dup_serial_no.equals(serial_no)) {
							voucher_notes = serial_no;
							vouchertrans_item_id = CNumeric(ExecuteQuery("SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_code = '" + serial_no + "'"
									+ " LIMIT 1"));

							if (!vouchertrans_item_id.equals("0")) {
								AddVoucher();
							} else {
								voucher_grandtotal = "";
								voucher_qty = "";
								vouchertrans_price = "";
								serial_no = "";
								vouchertrans_item_id = "0";
								StrSql = "UPDATE " + compdb(comp_id) + "stock_pdi"
										+ " SET active = '2'"
										+ " WHERE id = " + id + "";
								updateQuery(StrSql);
							}
						} else {
							StrSql = "UPDATE " + compdb(comp_id) + "stock_pdi"
									+ " SET active = '1'"
									+ " WHERE id = " + id + "";
							updateQuery(StrSql);
						}
					}

				}
			} else {
				SOP("No Data Found!");
			}
			crs.close();
			conntx.commit();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		} finally {
			conntx.setAutoCommit(true);// Enables auto commit
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}

	}
	public void AddVoucher() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher"
					+ " ("
					+ " voucher_vouchertype_id,"
					+ " voucher_branch_id,"
					+ " voucher_location_id,"
					+ " voucher_no,"
					+ " voucher_date,"
					+ " voucher_amount,"
					+ " voucher_customer_id,"
					+ " voucher_contact_id,"
					+ " voucher_lead_id,"
					+ " voucher_enquiry_id,"
					+ " voucher_po_id,"
					+ " voucher_git_id,"
					+ " voucher_grn_id,"
					+ " voucher_rateclass_id,"
					+ " voucher_emp_id," + " voucher_payment_date,"
					+ " voucher_billing_add," + " voucher_consignee_add,"
					+ " voucher_gatepass," + " voucher_lrno,"
					+ " voucher_driver_no," + " voucher_tempo_no,"
					+ " voucher_cashdiscount," + " voucher_turnoverdisc,"
					+ " voucher_special,"
					+ " voucher_pending,"
					+ " voucher_ref_no,"
					+ " voucher_authorize,"
					+ " voucher_terms,"
					+ " voucher_active," + " voucher_notes,"
					+ " voucher_entry_id," + " voucher_entry_date)"
					+ " VALUES"
					+ " (" + vouchertype_id + ","
					+ " " + branch_id + ","
					+ " " + vouchertrans_location_id + ","
					+ " 0,"
					+ " " + ToLongDate(kknow()) + ","
					+ "" + Double.parseDouble(voucher_grandtotal) + ","
					+ " " + CNumeric(voucher_supplier_id) + ","
					+ " " + CNumeric(voucher_contact_id) + ","
					+ " " + voucher_lead_id + ","
					+ " " + voucher_enquiry_id + ","
					+ " " + voucher_po_id + ","
					+ " " + voucher_git_id + ","
					+ " " + voucher_grn_id + ","
					+ " " + voucher_rateclass_id + ","
					+ " " + voucher_emp_id + ","
					+ " '" + voucher_payment_date + "',"
					+ " '" + voucher_billing_add + "',"
					+ " '" + voucher_consignee_add + "',"
					+ " '" + voucher_gatepass + "',"
					+ " '" + voucher_lrno + "',"
					+ " '" + voucher_driver_no + "',"
					+ " '" + voucher_tempo_no + "',"
					+ " 0,"
					+ " 0,"
					+ " '" + voucher_special + "',"
					+ " '" + voucher_pending + "',"
					+ " '" + voucher_ref_no + "',"
					+ " '" + voucher_authorize + "',"
					+ " '" + vouchertype_terms + "',"
					+ " '" + voucher_active + "',"
					+ " '" + voucher_notes + "',"
					+ " " + emp_id + "," + " " + ToLongDate(kknow()) + ")";
			// SOP("StrSql===voucher==" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs1 = stmttx.getGeneratedKeys();
			while (rs1.next()) {
				voucher_id = rs1.getString(1);
			}
			rs1.close();

			if (!voucher_id.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher_trans"
						+ " (vouchertrans_voucher_id,"
						+ " vouchertrans_multivoucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_location_id,"
						+ " vouchertrans_from_location_id,"
						+ " vouchertrans_item_id," + " vouchertrans_discount,"
						+ " vouchertrans_discount_perc,"
						+ " vouchertrans_tax," + " vouchertrans_tax_id,"
						+ " vouchertrans_rowcount," + " vouchertrans_option_id,"
						+ " vouchertrans_price," + " vouchertrans_netprice,"
						+ " vouchertrans_delivery_date,"
						+ " vouchertrans_convfactor," + " vouchertrans_qty,"
						+ " vouchertrans_unit_cost," + " vouchertrans_amount,"
						+ " vouchertrans_alt_qty," + " vouchertrans_alt_uom_id,"
						+ " vouchertrans_time," + " vouchertrans_dc)" + " VALUES ("
						+ " " + voucher_id + ","
						+ " 0,"
						+ " " + CNumeric(voucher_supplier_id) + ","
						+ " " + vouchertrans_location_id + ","
						+ " 0,"
						+ " " + vouchertrans_item_id + "," + " 0, " + " 0, " + " 0, " + " 0, " + " 1, " + " 0, "
						+ " " + Double.parseDouble(vouchertrans_price) + ","
						+ " " + Double.parseDouble(voucher_grandtotal) + ","
						+ " '', " + " 0," + "" + voucher_qty + "," + " 0,"
						+ " " + Double.parseDouble(voucher_grandtotal) + ","
						+ "" + voucher_qty + "," + " 1," + "'" + ToLongDate(kknow()) + "', '1')";
				// SOP("StrSql===vouchertrans==" + StrSql);
				stmttx.execute(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "stock_pdi"
						+ " SET active = '1'"
						+ " WHERE id = " + id + "";
				stmttx.execute(StrSql);

				if (!vouchertrans_item_id.equals("0") && !vouchertrans_location_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_stock"
							+ " SET itemstock_current_qty = " + voucher_qty
							+ " WHERE itemstock_location_id = " + vouchertrans_location_id
							+ " AND itemstock_item_id = " + vouchertrans_item_id + "";
					SOP("StrSql=stock update==" + StrSql);
					stmttx.execute(StrSql);
				}
			}

			// CalCurrentStock(vouchertrans_item_id, vouchertrans_location_id, comp_id, vouchertype_id);

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

	}
	public void CalCurrentStock(String item_id, String location_id,
			String comp_id, String vouchertype_id) {
		try {
			String StrSql = "UPDATE  " + compdb(comp_id) + "axela_inventory_stock"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = itemstock_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_id = itemstock_location_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " SET";
			if (vouchertype_id.equals("1") || vouchertype_id.equals("3")
					|| vouchertype_id.equals("2") || vouchertype_id.equals("4")
					|| vouchertype_id.equals("7") || vouchertype_id.equals("9")) {
				StrSql += " itemstock_current_qty ="
						+ " @stockcount:= "
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
						+ " -vouchertrans_qty))"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN   " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE vouchertrans_item_id = itemstock_item_id"
						+ " AND vouchertrans_location_id = itemstock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND voucher_active = 1"
						+ " AND sub.branch_id = branch_id"
						// + " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_vouchertype_id != 10),0)";
			} else if (vouchertype_id.equals("10")) {
				// / for git quabtity (git-grn)
				StrSql += " stock_git_qty ="
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 1, vouchertrans_qty," + " -vouchertrans_qty))"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN   " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN   " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE voucher_vouchertype_id IN (10,1,117)" // for // git
						+ " AND vouchertrans_item_id = itemstock_item_id"
						+ " AND vouchertrans_location_id = itemstock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						// + " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_active = 1),0)";
			} else if (vouchertype_id.equals("108")) {
				// stock inorder
				StrSql += " itemstock_stockinorder ="
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 0, vouchertrans_qty," + " -vouchertrans_qty))"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN   " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = po.voucher_vouchertype_id"
						+ " INNER JOIN   " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = po.voucher_branch_id"
						+ " WHERE po.voucher_vouchertype_id IN (108,1,117)" // po
						+ " AND vouchertrans_item_id = itemstock_item_id"
						+ " AND vouchertrans_location_id = itemstock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						// + " AND sub.branch_company_id = branch_company_id"
						+ " AND po.voucher_special = 0"
						+ " AND po.voucher_pending = 1"
						+ " AND po.voucher_active = 1),0)";
			} else if (vouchertype_id.equals("4")) {
				// stock indemand
				StrSql += " itemstock_stockindemand ="
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 0, vouchertrans_qty," + " -vouchertrans_qty))"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN   " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN   " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE voucher_vouchertype_id IN(4,3,23)" // so
						+ " AND vouchertrans_item_id = itemstock_item_id"
						+ " AND vouchertrans_location_id = itemstock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						// + " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_active = 1),0)";
			}
			StrSql += " WHERE item_active = 1";
			// + " AND item_stock = 1"
			// + " AND branch_company_id = " + comp_id;
			if (!item_id.equals("")) {
				StrSql += item_id;
			}

			if (!CNumeric(location_id).equals("0")) {
				StrSql += " AND itemstock_location_id = " + location_id + "";
			}
			// SOP("StrSql===calcurrentstock===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("AxelaCRM===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

}
