package axela.accounting;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Profit_Loss extends Connect {

	public String StrSql = "";
	public String comp_id = "0";

	public String finyear_id = "", start_date = "", end_date = "";
	public String msg = "";
	public String balsheet = "";
	public String brand_id = "", region_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String emp_id = "";
	public String StrHTML = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String accgroup_id = "0";
	public String go = "";

	public double closingstock = 0.00;
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	public axela.accounting.Report_BalanceSheet balancesheet = new axela.accounting.Report_BalanceSheet();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				go = PadQuotes(request.getParameter("submit_button"));
				balsheet = PadQuotes(request.getParameter("balsheet"));
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ProfitLoss();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		finyear_id = CNumeric(PadQuotes(request.getParameter("dr_finyear_id")));
		accgroup_id = CNumeric(PadQuotes(request.getParameter("dr_group")));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		region_id = RetrunSelectArrVal(request, "dr_region");
		if (balsheet.equals("yes")) {
			SOP("aaa");
			brand_ids = brand_id.split(",");
			branch_ids = branch_id.split(",");
			region_ids = region_id.split(",");
		} else {
			brand_ids = request.getParameterValues("dr_principal");
			branch_ids = request.getParameterValues("dr_branch");
			region_ids = request.getParameterValues("dr_region");
		}

		if (!finyear_id.equals("0")) {
			StrSql = "SELECT finyear_startdate, finyear_enddate FROM " + compdb(comp_id) + "axela_acc_finyear WHERE finyear_id = " + finyear_id;
			CachedRowSet crs = processQuery(StrSql);
			try {
				while (crs.next()) {
					start_date = PadQuotes(crs.getString("finyear_startdate"));
					end_date = PadQuotes(crs.getString("finyear_enddate"));
				}
				crs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		if (finyear_id.equals("0")) {
			msg = msg + "<br>Select Financial Year!";
		}
	}

	public String PopulareYear(String finyear_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT finyear_id, finyear_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_finyear"
					+ " ORDER BY finyear_startdate DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("finyear_id"));
				Str.append(StrSelectdrop(crs.getString("finyear_id"), finyear_id));
				Str.append(">").append(crs.getString("finyear_name")).append("</option>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ProfitLoss() {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT"
					+ " accgroup_id, CONCAT(accgrouppop_name) AS groupname, accgroup_name,"
					+ " ( customer_open_bal +"
					+ " IF ( accgroup_alie = 3, SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) - SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ),"
					+ " SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ) - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) )  )"
					+ " ) AS subgrpsum,"
					+ " accgroup_alie"
					+ " FROM " + compdb(comp_id) + "axela_acc_group"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = accgroup_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_accgroup_id = accgroup_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE 1 = 1"
					+ " AND ( accgroup_alie = 3 OR accgroup_alie = 4 )"
					+ " AND voucher_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND ((vouchertype_authorize = 1 AND voucher_authorize = 1) OR (vouchertype_authorize = 0 ))"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR( " + start_date + ", 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR( " + end_date + ", 1, 8)";
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ")";
			}
			if (!accgroup_id.equals("0")) {
				StrSql += " AND accgroup_id = " + accgroup_id;
			}

			StrSql += " GROUP BY accgroup_id"
					+ " ORDER BY accgroup_alie, accgroup_id";

			// SOP("StrSql profit loss ======== " + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			balancesheet.start_date = start_date;
			balancesheet.end_date = end_date;

			if (crs.isBeforeFirst()) {
				Str.append("<div class='col-md-6 col-sm-12'>");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th width=70%>INCOME</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("</tr></thead><tbody>\n");
				// method
				Str.append(balancesheet.populateAssetsLiabilities("3", crs));
				Str.append("<tr><td><b>Total INCOME</b></td><td></td><td align='right'><b>" + IndFormat(deci.format(balancesheet.total_assets)) + "</b></td></tr>\n");
				balancesheet.total_assets = 0;
				Str.append("</tbody></table>\n");
				Str.append("</div>\n");

				Str.append("<div class='col-md-6 col-sm-12'>");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th width=70%>EXPENSE</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("</tr></thead><tbody>\n");
				// method
				Str.append(balancesheet.populateAssetsLiabilities("4", crs));

				Str.append("<tr><td><b>Total EXPENSE</b></td><td></td><td align='right'><b>" + IndFormat(deci.format(balancesheet.total_assets)) + "</b></td></tr>\n");
				balancesheet.total_assets = 0;

				Str.append("</tbody></table>\n");
				Str.append("</div>\n");
				crs.close();
			} else {
				Str.append("<br><br><br><br><strong><center><font color=red><b>No Voucher(s) Found!</b></font></center></strong><br><br>");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}

}