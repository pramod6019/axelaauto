package axela.accounting;

import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CalcCurrentBalance extends Connect {

	private String comp_id, voucher_id;

	public synchronized void setCompany_id(String comp_id) {
		this.comp_id = comp_id;
	}

	public synchronized void setVoucher_id(String voucher_id) {
		this.voucher_id = voucher_id;
	}

	public synchronized void CalcuateCurrentVoucherBal() {
		String StrSql = "";
		double currbalance = 0.00;
		String customer_id = "";

		try {
			StrSql = "SELECT DISTINCT vouchertrans_customer_id"
					+ " FROM axela_acc_voucher_trans" + " WHERE 1=1"
					+ " AND vouchertrans_tax = 0"
					+ " AND vouchertrans_discount = 0"
					+ " AND vouchertrans_voucher_id = " + voucher_id;
			SOP("StrSql==1==" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				customer_id += crs1.getString("vouchertrans_customer_id") + ",";
			}
			crs1.close();
			customer_id = customer_id.substring(0, customer_id.length() - 1);
			// SOP("customer_id====" + customer_id);
			LedgerCurrBalance(comp_id, customer_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}

	public void LedgerCurrBalance(String comp_id, String customer_id) {
		String StrSql = "";
		String currbalance = "";
		StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_currentbal"
				+ " SET currentbal_amount = "
				+ " COALESCE("
				+ " (currentbal_opp_amount +"
				+ " (SELECT"
				+ " COALESCE(sum(if(vouchertrans_dc = 1, vouchertrans_amount, -vouchertrans_amount)), 0)"
				+ " FROM axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE voucher_active = 1 "
				+ " AND branch_company_id = currentbal_company_id"
				+ " AND vouchertrans_customer_id = currentbal_customer_id "
				+ " AND substr(voucher_date ,1,8) < substr("
				+ ToShortDate(kknow()) + ",1,8) "
				+ " GROUP BY vouchertrans_customer_id)), 0)"// as currbal
				+ " WHERE currentbal_customer_id IN (" + customer_id + ")"
				+ " AND currentbal_company_id = " + comp_id
				+ "";
		// SOP("StrSql===LedgerCurrBalance==" + StrSqlBreaker(StrSql));
		updateQuery(StrSql);
	}

}
