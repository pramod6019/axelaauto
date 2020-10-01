package axela.accounting;

import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CalcCurrentBalanceThread extends Connect implements Runnable {

	private String comp_id, voucher_id, customer_id = "0", vouchertype_id = "0";

	CalcCurrentBalanceThread(String voucher_id, String comp_id, String customer_id, String vouchertype_id) {
		this.comp_id = comp_id;
		this.voucher_id = voucher_id;
		this.customer_id = customer_id;
		this.vouchertype_id = vouchertype_id;
		// SOP("========CalcCurrentBalanceThread========");
	}
	public void run() {
		CalcuateCurrentVoucherBal();
	}
	public void CalcuateCurrentVoucherBal() {
		String StrSql = "";
		double currbalance = 0.00;
		String customer_id = "";

		try {
			StrSql = "SELECT DISTINCT vouchertrans_customer_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE 1=1"
					+ " AND vouchertrans_voucher_id = " + voucher_id
					+ " AND vouchertrans_customer_id !=0";
			// SOP("StrSql==1==" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				customer_id += crs1.getString("vouchertrans_customer_id") + ",";
			}
			crs1.close();
			// SOP("customer_id====" + customer_id);
			customer_id = customer_id.substring(0, customer_id.length() - 1);
			// SOP("customer_id====" + customer_id);
			LedgerCurrBalance(comp_id, customer_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public void LedgerCurrBalance(String comp_id, String customer_id) {
		String StrSql = "";
		StrSql = "UPDATE  " + compdb(comp_id) + "axela_customer"
				+ " SET customer_curr_bal = "
				+ " COALESCE("
				+ " (customer_open_bal +"
				+ " (SELECT"
				+ " COALESCE(sum(if(vouchertrans_dc = 1, vouchertrans_amount, -vouchertrans_amount)), 0)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE 1 = 1"
				+ " AND vouchertype_affects_accounts = 1"
				+ " AND voucher_active = 1"
				+ " AND vouchertrans_customer_id = customer_id"
				+ " AND substr(voucher_date ,1,8) <= substr(" + ToShortDate(kknow()) + ",1,8)"
				+ " GROUP BY vouchertrans_customer_id)), 0)";// as currbal
		if (!customer_id.equals("")) {
			StrSql += " WHERE customer_id IN (" + customer_id + ")";
		}
		// SOP("StrSql===LedgerCurrBalance==" + StrSqlBreaker(StrSql));
		updateQuery(StrSql);
	}

}
