package axela.accounting;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_BalanceSheet extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String RecCountDisplay = "";
	public String comp_id = "0";
	public String emp_id = "0";
	public String finyear_id = "", start_date = "", end_date = "";
	public String go = "";
	public String customer_id = "0";
	public String subgroupid = "0";
	public String brand_id = "", region_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String groupid = "0";
	public Double grouptotal = 0.00;
	public double closingstock = 0.00;
	DecimalFormat df = new DecimalFormat("0.00");

	public double total_assets = 0;
	public String[] groups = null;
	public String[] groups2 = null;
	public int i = 0;
	StringBuilder Str = new StringBuilder();
	CachedRowSet crs = null;

	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));

				go = PadQuotes(request.getParameter("submit_button"));

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = Listdata();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		finyear_id = CNumeric(PadQuotes(request.getParameter("dr_finyear_id")));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

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

	public void CheckForm() {
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
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
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

	public String Listdata() {

		StrSql = "SELECT accgroup_id, accgroup_name,"
				+ " CONCAT(accgrouppop_name) AS groupname,"
				+ " customer_id, customer_name, accgroup_alie,"
				+ " IF ( accgroup_alie = 1, 'Assets',"
				+ " IF ( accgroup_alie = 2, 'Liabilities',"
				+ " IF ( accgroup_alie = 3, 'Income',"
				+ " IF ( accgroup_alie = 4, 'Expense',"
				+ " IF ( accgroup_alie = 5, 'Owners Equity',"
				+ " '' ) ) ) ) ) AS accgroup_alie_name,"
				+ " customer_open_bal, ( customer_open_bal +"
				+ " IF ( accgroup_alie = 2, SUM("
				+ " IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) - SUM("
				+ " IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ), SUM("
				+ " IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ) - SUM("
				+ " IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) ) ) AS subgrpsum"
				+ " FROM " + compdb(comp_id) + "axela_acc_group"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_accgroup_id = accgroup_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = accgroup_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE 1 = 1"
				+ " AND (accgroup_alie = 1 OR accgroup_alie = 2 OR accgroup_alie = 5)"
				+ " AND voucher_active = 1"
				+ " AND vouchertype_affects_accounts = 1"
				+ " AND ((vouchertype_authorize = 1 AND voucher_authorize = 1) OR (vouchertype_authorize = 0 ))"
				+ " AND vouchertrans_amount != 0"
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

		StrSql += " GROUP BY accgroup_id"
				+ " ORDER BY accgroup_alie, accgrouppop_name";

		// SOP("StrSql===balance sheet===" + StrSqlBreaker(StrSql));

		crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class='col-md-6 col-sm-12'>");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th width=70%>A S S E T S</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("</tr></thead><tbody>\n");
				// method
				Str.append(populateAssetsLiabilities("1", crs));
				Str.append("<tr><td><b>Total APPLICATION OF FUNDS</b></td><td></td><td align='right'><b>" + IndFormat(df.format(total_assets)) + "</b></td></tr>\n");
				total_assets = 0;
				Str.append("</tbody></table>\n");
				Str.append("</div>\n");

				Str.append("<div class='col-md-6 col-sm-12'>");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th width=70%>L I A B I L I T I E S</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("</tr></thead><tbody>\n");
				// method
				Str.append(populateAssetsLiabilities("2", crs));
				Double totalliblities = total_assets;
				Str.append("<tr><td><b>Total Liabilities</b></td><td></td><td align='right'><b>" + IndFormat(df.format(totalliblities)) + "</b></td></tr>\n");
				Str.append("</tbody><thead><tr align=center>\n");
				total_assets = 0;

				String profitloss = getRetainedEarnings();
				// SOP("profitloss===" + profitloss);

				// if (!profitloss.equals("0") && !profitloss.equals("")) {
				Str.append("<th width=70%>OWNER'S EQUITY</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("</tr></thead><tbody>\n");
				Str.append(populateAssetsLiabilities("5", crs));
				total_assets += Double.parseDouble(profitloss);
				Str.append("<tr><td><b>"
						+ "<a href='../accounting/report-profit-loss.jsp?balsheet=yes&submit_button=Go&dr_principal=" + brand_id + "&dr_branch=" + branch_id + "&dr_region=" + region_id
						+ "&dr_finyear_id=" + finyear_id + "' target='_blank'>Retained Earnings</a></b></td><td align='right'><b>"
						+ IndFormat(df.format(Double.parseDouble(profitloss))) + "</b></td><td></td></tr>\n");
				Str.append("<tr><td><b>Total Owner's Equity</b></td><td></td><td align='right'><b>" + IndFormat(df.format(total_assets)) + "</b></td></tr>\n");
				// }

				Str.append("<tr><td><b>Total SOURCES OF FUNDS</b></td><td></td><td align='right'><b>" + IndFormat(df.format(total_assets + totalliblities)) + "</b></td></tr>\n");
				total_assets = 0;
				Str.append("</tbody></table>\n");
				Str.append("</div>\n");
				crs.close();
			} else {
				Str.append("<br><br><br><br><strong><center><font color=red><b>No Voucher(s) Found!</b></font></center></strong><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	public String populateAssetsLiabilities(String alie, CachedRowSet crs) {

		try {
			StringBuilder Str = new StringBuilder();
			crs.beforeFirst();
			if (crs.next()) {

				groups = crs.getString("groupname").split(" > ");
				if (crs.getString("accgroup_alie").equals(alie)) {
					for (i = 0; i < groups.length - 1; i++) {
						Str.append("<tr><td>" + addSpace(i + 1) + "<b>" + groups[i] + "</b>" + "</td><td></td><td></td></tr>\n");
					}
					Str.append("<tr><td>" + addSpace(i + 1) + "<span><a href='report-ledgerstatement.jsp?bal_sheat=yes&txt_startdate=" + strToShortDate(start_date)
							+ "&txt_enddate=" + strToShortDate(end_date) + "&dr_group=" + crs.getString("accgroup_id")
							+ " ' target='_blank'>" + crs.getString("accgroup_name") + "</a></span></td>\n");
					Str.append("<td align='right'>" + IndFormat(df.format(crs.getDouble("subgrpsum"))) + "</td><td></td></tr>\n");

					total_assets += crs.getDouble("subgrpsum");
				}
			}

			while (crs.next()) {
				if (crs.getString("accgroup_alie").equals(alie)) {
					groups2 = crs.getString("groupname").split(" > ");
					for (i = 0; i < groups2.length - 1; i++) {

						if (i < groups.length && !groups[i].equals(groups2[i])) {
							Str.append("<tr><td>" + addSpace(i + 1) + "<b>" + groups2[i] + "</b>" + "</td><td></td><td></td></tr>\n");
						} else if (i >= groups.length) {
							Str.append("<tr><td>" + addSpace(i + 1) + "<b>" + groups2[i] + "</b>" + "</td><td></td><td></td></tr>\n");
						}
					}
					Str.append("<tr><td>" + addSpace(i + 1) + "<span><a href='report-ledgerstatement.jsp?bal_sheat=yes&txt_startdate=" + strToShortDate(start_date)
							+ "&txt_enddate=" + strToShortDate(end_date) + "&dr_group=" + crs.getString("accgroup_id")
							+ " ' target='_blank'>" + crs.getString("accgroup_name") + "</a></span></td>\n");
					Str.append("<td align='right'>" + IndFormat(df.format(crs.getDouble("subgrpsum"))) + "</td><td></td></tr>\n");
					total_assets += crs.getDouble("subgrpsum");
					groups = groups2;
				}
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public String getRetainedEarnings() {

		StrSql = "SELECT ("
				// + " customer_open_bal +"
				+ " IF ( accgroup_alie = 3, SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) - SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ),0)"
				+ " - IF ( accgroup_alie = 4, SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) ) - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) )  ,0)"
				+ " ) AS subgrpsum"
				+ " FROM " + compdb(comp_id) + "axela_acc_group"
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
		// SOP("OWners equity=====" + StrSqlBreaker(StrSql));

		return PadQuotes(ExecuteQuery(StrSql));

	}

	public String addSpace(int i) {
		String str = "";
		if (i != 0) {
			for (int x = 1; x < i; x++) {
				str += "&emsp;&emsp;&emsp;&emsp;&emsp;";
			}
		}
		return str;
	}
}