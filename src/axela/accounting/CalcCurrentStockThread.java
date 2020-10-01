package axela.accounting;

import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CalcCurrentStockThread extends Connect implements Runnable {

	private String comp_id, voucher_branch_id, vouchertrans_location_id, vouchertype_id, jc;

	// 1. Pass voucher_branch_id, if you want to calculate the stock Branch wise.
	// 2. Pass vouchertrans_location_id, if you want to calculate the stock Location wise.
	// 3. Pass vouchertype_id, if you want to calculate the stock Voucher Type wise or pass vouchertype_id = 0 to calculate the stock for all Voucher Type.
	// 4. Pass jc = yes, if you want to calculate the stock for Job Card and Vouchers or else pass jc = "" for calculating stock for only Vouchers

	public CalcCurrentStockThread(String voucher_branch_id, String vouchertrans_location_id, String comp_id, String vouchertype_id, String jc) {
		this.comp_id = comp_id;
		this.voucher_branch_id = voucher_branch_id;
		this.vouchertrans_location_id = vouchertrans_location_id;
		this.vouchertype_id = vouchertype_id;
		this.jc = jc;
	}

	public void run() {
		if (jc.equals("")) {
			CalCurrentStockVoucher();
		} else if (jc.equals("yes")) {
			CalCurrentStockJobcard();
		}

	}

	public void CalCurrentStockVoucher() {
		String item_id = "";
		String location_id = "0";
		int count = 0;
		String StrSql = "SELECT DISTINCT vouchertrans_location_id, vouchertrans_item_id"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id  = vouchertrans_voucher_id"
				+ " WHERE 1=1";
		if (!voucher_branch_id.equals("0") && !voucher_branch_id.equals("")) {
			StrSql += " AND voucher_branch_id = " + voucher_branch_id;
		} else {
			StrSql += "	AND vouchertrans_location_id = " + vouchertrans_location_id;
		}
		StrSql += " AND vouchertrans_discount = 0"
				+ " AND vouchertrans_tax = 0"
				+ " AND vouchertrans_rowcount != 0"
				+ " AND vouchertrans_option_id = 0";

		// SOP("StrSql===CalCurrentStockVoucher==" + StrSqlBreaker(StrSql));

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				item_id += " AND stock_item_id IN ( ";
				while (crs.next()) {
					location_id = crs.getString("vouchertrans_location_id");
					item_id += crs.getString("vouchertrans_item_id") + ",";
				}
				item_id = item_id.substring(0, item_id.length() - 1);
				item_id += " )";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		CalCurrentStock(item_id, location_id, comp_id, vouchertype_id);
	}

	// To calculate inventory current stock or our current stock
	public void CalCurrentStock(String item_id, String location_id, String comp_id, String vouchertype_id) {
		try {
			item_id = "";
			String StrSql = "SELECT stock_item_id FROM " + compdb(comp_id) + "axela_inventory_stock WHERE stock_location_id = " + location_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				item_id += crs.getString("stock_item_id") + ", ";
			}
			crs.close();
			item_id = item_id.substring(0, item_id.length() - 2);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = stock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " SET";
			if (vouchertype_id.equals("12")) {
				// stock inorder
				// if (vouchertype_id.equals("10")) {
				// StrSql += ", ";
				// }
				StrSql += " stock_current_qty = "
						+ " (SELECT"
						+ " SUM(IF( voucher_vouchertype_id = 12, vouchertrans_qty, - vouchertrans_qty ) ) AS qty"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = po.voucher_vouchertype_id"
						+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = po.voucher_branch_id"
						+ " WHERE po.voucher_vouchertype_id IN (12)"
						+ " AND po.voucher_special = 0"
						+ " AND po.voucher_active = 1)";
				// + " GROUP BY"
				// + " voucher_location_id, vouchertrans_item_id";
			}
			if (vouchertype_id.equals("1") || vouchertype_id.equals("6") || vouchertype_id.equals("7") || vouchertype_id.equals("20") ||
					vouchertype_id.equals("23") || vouchertype_id.equals("24") || vouchertype_id.equals("25") || vouchertype_id.equals("0")) {
				StrSql += " stock_current_qty = @stockcount:= "
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 1, vouchertrans_qty, -vouchertrans_qty))"
						+ " FROM " + compdb(comp_id) + " axela_acc_voucher_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE vouchertrans_item_id = stock_item_id"
						+ " AND vouchertrans_location_id = stock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND voucher_active = 1";

				StrSql += " AND sub.branch_id = branch_id),0)";
			}
			// } else if (vouchertype_id.equals("10")) {
			// // / for git quabtity (git-grn)
			// StrSql += " stock_git_qty ="
			// + " COALESCE((select sum(if(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
			// + " -vouchertrans_qty))"
			// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
			// + " INNER JOIN " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
			// + " WHERE voucher_vouchertype_id IN (10,1,117)" // for
			// // git
			// + " AND vouchertrans_item_id = stock_item_id"
			// + " AND vouchertrans_location_id = stock_location_id"
			// + " AND vouchertype_affects_inventory = 1"
			// + " AND sub.branch_id = branch_id"
			// // + " AND sub.branch_company_id = branch_company_id"
			// + " AND voucher_active = 1),0)";
			// } else if (vouchertype_id.equals("108")) {
			// // stock inorder
			// StrSql += " stock_stockinorder ="
			// + " COALESCE((select sum(if(voucherclass_inventory_traffic = 0, vouchertrans_qty,"
			// + " -vouchertrans_qty))"
			// + " FROM " + compdb(comp_id) + " axela_acc_voucher_trans"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = vouchertrans_voucher_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = po.voucher_vouchertype_id"
			// + " INNER JOIN " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = po.voucher_branch_id"
			// + " WHERE po.voucher_vouchertype_id IN (108,1,117)" // po
			// + " AND vouchertrans_item_id = stock_item_id"
			// + " AND vouchertrans_location_id = stock_location_id"
			// + " AND vouchertype_affects_inventory = 1"
			// + " AND sub.branch_id = branch_id"
			// // + " AND sub.branch_company_id = branch_company_id"
			// + " AND po.voucher_special = 0"
			// + " AND po.voucher_pending = 1"
			// + " AND po.voucher_active = 1),0)";
			// }
			else if (vouchertype_id.equals("4")) {
				// stock indemand
				StrSql += " stock_stockindemand ="
						+ " COALESCE((SELECT SUM(IF(voucherclass_inventory_traffic = 0, vouchertrans_qty, -vouchertrans_qty))"
						+ " FROM axela_acc_voucher_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE voucher_vouchertype_id IN(4,25,23)" // so
						+ " AND vouchertrans_item_id = stock_item_id"
						+ " AND vouchertrans_location_id = stock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						// + " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_active = 1),0)";
			}
			StrSql += " WHERE item_active = 1";
			// + " AND item_stock = 1"
			// + " AND branch_company_id = " + comp_id;

			if (!item_id.equals("")) {
				StrSql += " AND item_id IN (" + item_id + ")";
			}
			if (!CNumeric(voucher_branch_id).equals("0")) {
				StrSql += " AND location_branch_id  = " + voucher_branch_id + "";
			} else if (!CNumeric(location_id).equals("0")) {
				StrSql += " AND stock_location_id = " + location_id + "";
			}
			SOP("StrSql===calcurrentstock===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CalCurrentStockJobcard() {
		String item_id = "";
		String location_id = "0";
		int count = 0;
		String StrSql = "SELECT DISTINCT jctrans_location_id, jctrans_item_id"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id  = jctrans_jc_id"
				+ " WHERE jctrans_location_id = " + vouchertrans_location_id
				+ " AND jctrans_discount = 0"
				+ " AND jctrans_tax = 0"
				+ " AND jctrans_rowcount != 0"
				+ " AND jctrans_option_id = 0";

		// SOP("StrSql===CalCurrentStockJobcard==" + StrSqlBreaker(StrSql));

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				item_id += " AND stock_item_id IN ( ";
				while (crs.next()) {
					location_id = crs.getString("jctrans_location_id");
					item_id += crs.getString("jctrans_item_id") + ",";
				}
				item_id = item_id.substring(0, item_id.length() - 1);
				item_id += " )";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		CalCurrentJobcardStock(item_id, location_id, comp_id, jc);
	}

	// To calculate inventory current stock
	public void CalCurrentJobcardStock(String item_id, String location_id, String comp_id, String jc) {
		try {
			item_id = "";
			String StrSql = "SELECT stock_item_id FROM " + compdb(comp_id) + "axela_inventory_stock WHERE stock_location_id = " + location_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				item_id += crs.getString("stock_item_id") + ", ";
			}
			crs.close();
			item_id = item_id.substring(0, item_id.length() - 2);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = stock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " SET";
			StrSql += " stock_current_qty =  @stockcount :="
					+ " COALESCE ( ( SELECT SUM( IF ( voucherclass_inventory_traffic = 1, vouchertrans_qty, - vouchertrans_qty ) )"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
					+ " WHERE vouchertrans_item_id = stock_item_id"
					+ " AND vouchertrans_location_id = stock_location_id"
					+ " AND vouchertype_affects_inventory = 1"
					+ " AND voucher_active = 1"
					+ " AND sub.branch_id = branch_id ), 0 )"
					+ " - @stockcount := COALESCE ( ( SELECT SUM(jctrans_qty)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jctrans_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = jc_branch_id"
					+ " WHERE jctrans_item_id = stock_item_id"
					+ " AND jctrans_location_id = stock_location_id"
					+ " AND jc_active = 1"
					+ " AND sub.branch_id = branch_id ), 0 )";
			StrSql += " WHERE item_active = 1";

			if (!item_id.equals("")) {
				StrSql += " AND item_id IN (" + item_id + ")";
			}

			if (!CNumeric(location_id).equals("0")) {
				StrSql += " AND stock_location_id = " + location_id + "";
			}
			// SOP("StrSql==CalCurrentJobcardStock===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
