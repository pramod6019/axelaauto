package axela.jobs;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Jobs_Sostock_Deallocate extends Connect {

	public String StrHTML = "";
	public String comp_id = "0";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				UpdateSoStockDeAllocate();
			} else {
				comp_id = "1009"; // //DD Motors
				UpdateSoStockDeAllocate();
				// Thread.sleep(100);
				// comp_id = "1011"; // //Indel
				// UpdateSoStockDeAllocate();
				// Thread.sleep(100);
				// comp_id = "1014"; // //Jubiliant
				// UpdateSoStockDeAllocate();
				// Thread.sleep(100);
				// comp_id = "1015"; // //Big Boy toys
				// UpdateSoStockDeAllocate();
				// Thread.sleep(100);
				// comp_id = "1017"; // /Joshi
				// UpdateSoStockDeAllocate();
			}

			StrHTML = "Jobs SO Stock Deallocate Routine Run Successfully!";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}

	void UpdateSoStockDeAllocate() throws SQLException {
		try {
			StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config on brandconfig_brand_id = branch_brand_id"
					+ " SET so_vehstock_id = IF(DATEDIFF('" + ToLongDate(kknow()) + "', so_stockallocation_time) > brandconfig_deallocatestock_days "
					+ " AND COALESCE((SELECT SUM(voucher_amount) FROM " + compdb(comp_id) + "axela_acc_voucher "
					+ " WHERE voucher_active = 1 "
					+ " AND voucher_vouchertype_id = 9 "
					+ " AND voucher_so_id = so_id)"
					+ " / so_grandtotal * 100, 0) < brandconfig_deallocatestock_amountperc,"
					+ " 0 , so_vehstock_id)"
					+ " WHERE 1 = 1"
					+ " AND so_active = 1"
					+ " AND so_vehstock_id != 0"
					+ " AND so_retail_date = ''"
					+ " AND brandconfig_deallocatestock_enable = 1 ";
			SOP("StrSql=====Deallocate======" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
