package axela.accounting;

import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CalcCurrentStock extends Connect {

	private String comp_id, voucher_id, vouchertype_id;

	// @Override
	// public void run() {
	// CalCurrentStockVoucher(voucher_id, comp_id, vouchertype_id);
	// }

	public synchronized void setCompany_id(String comp_id) {
		this.comp_id = comp_id;
	}

	public synchronized void setVoucher_id(String voucher_id) {
		this.voucher_id = voucher_id;
	}

	public synchronized void setVouchertype_id(String vouchertype_id) {
		this.vouchertype_id = vouchertype_id;
	}

	public synchronized void CalCurrentStockVoucher() {
		String item_id = "";
		String location_id = "0";
		int count = 0;
		String StrSql = "SELECT DISTINCT vouchertrans_location_id, vouchertrans_item_id"
				+ " FROM axela_acc_voucher_trans"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id  = vouchertrans_voucher_id"
				+ " WHERE vouchertrans_voucher_id = "
				+ voucher_id
				+ " AND vouchertrans_discount = 0"
				+ " AND vouchertrans_tax = 0"
				+ " AND vouchertrans_rowcount != 0"
				+ " AND vouchertrans_option_id = 0";
		// SOP("StrSql===CalCurrentStockVoucher=="+StrSqlBreaker(StrSql));
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
			SOPError("AxelaCRM===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

		CalCurrentStock(item_id, location_id, comp_id, vouchertype_id);
	}

	// To calculate inventory current stock or our current stock
	public void CalCurrentStock(String item_id, String location_id,
			String comp_id, String vouchertype_id) {
		try {
			String StrSql = "UPDATE  " + compdb(comp_id) + "axela_inventory_stock"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = stock_item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " SET"
					+ " stock_current_qty ="
					+ " @stockcount:= "
					+ " COALESCE((select sum(if(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
					+ " -vouchertrans_qty))"
					+ " FROM axela_acc_voucher_trans"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
					+ " WHERE vouchertrans_item_id = stock_item_id"
					+ " AND vouchertrans_location_id = stock_location_id"
					+ " AND vouchertype_affects_inventory = 1"
					+ " AND voucher_active = 1"
					+ " AND sub.branch_id = branch_id"
					+ " AND sub.branch_company_id = branch_company_id"
					+ " AND voucher_vouchertype_id != 10),0)";
			if (vouchertype_id.equals("10")) {
				// / for git quabtity (git-grn)
				StrSql += " , stock_git_qty ="
						+ " COALESCE((select sum(if(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
						+ " -vouchertrans_qty))"
						+ " FROM axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE voucher_vouchertype_id IN (10,1,117)" // for
																		// git
						+ " AND vouchertrans_item_id = stock_item_id"
						+ " AND vouchertrans_location_id = stock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						+ " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_active = 1),0)";
			} else if (vouchertype_id.equals("108")) {
				// stock inorder
				StrSql += ", stock_stockinorder ="
						+ " COALESCE((select sum(if(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
						+ " -vouchertrans_qty))"
						+ " FROM axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = po.voucher_vouchertype_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = po.voucher_branch_id"
						+ " WHERE po.voucher_vouchertype_id IN (108,1,117)" // po
						+ " AND vouchertrans_item_id = stock_item_id"
						+ " AND vouchertrans_location_id = stock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						+ " AND sub.branch_company_id = branch_company_id"
						+ " AND po.voucher_special = 0"
						+ " AND po.voucher_pending = 0"
						+ " AND po.voucher_active = 1),0)";
			} else if (vouchertype_id.equals("4")) {
				// stock indemand
				StrSql += ", stock_stockindemand ="
						+ " COALESCE((select sum(if(voucherclass_inventory_traffic = 1, vouchertrans_qty,"
						+ " -vouchertrans_qty))"
						+ " FROM axela_acc_voucher_trans"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch sub ON sub.branch_id = voucher_branch_id"
						+ " WHERE voucher_vouchertype_id IN(4,25,23)" // so
						+ " AND vouchertrans_item_id = stock_item_id"
						+ " AND vouchertrans_location_id = stock_location_id"
						+ " AND vouchertype_affects_inventory = 1"
						+ " AND sub.branch_id = branch_id"
						+ " AND sub.branch_company_id = branch_company_id"
						+ " AND voucher_active = 1),0)";
			}
			StrSql += " WHERE item_active = 1" + " AND item_stock = 1"
					+ " AND branch_company_id = " + comp_id;
			if (!item_id.equals("")) {
				StrSql += item_id;
			}

			if (!CNumeric(location_id).equals("0")) {
				StrSql += " AND stock_location_id = " + location_id + "";
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
