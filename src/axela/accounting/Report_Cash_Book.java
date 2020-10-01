package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Cash_Book extends Connect {

	public String StrSql = "";
	public String start_date = "";
	public String end_date = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_id = "";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String go = "";
	public String export = "", StrSearch = "";
	public String vouchertype_id = "0", voucher_customer_id = "0", vouchertrans_customer_id = "0";
	public double credit = 0.00, debit = 0.00;
	public double drtotal = 0.00, crtotal = 0.00;

	public String month = "0";
	public String type_id = "1";
	public String ledger_id = "0";

	public String brand_id = "", region_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;

	public String finyear_id = "0";

	double running_bal = 0.00;

	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	private String finyear_startdate = "", finyear_enddate = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = Listdata(request, response);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			brand_id = RetrunSelectArrVal(request, "dr_principal");
			brand_ids = request.getParameterValues("dr_principal");
			branch_id = RetrunSelectArrVal(request, "dr_branch");
			branch_ids = request.getParameterValues("dr_branch");
			region_id = RetrunSelectArrVal(request, "dr_region");
			region_ids = request.getParameterValues("dr_region");
			finyear_id = CNumeric(PadQuotes(request.getParameter("dr_finyear_id")));

			if (!finyear_id.equals("0")) {
				CachedRowSet crs = processQuery("SELECT finyear_startdate, finyear_enddate FROM " + compdb(comp_id) + "axela_acc_finyear where finyear_id=" + finyear_id, 0);
				if (crs.next()) {
					finyear_startdate = PadQuotes(crs.getString("finyear_startdate"));
					finyear_enddate = PadQuotes(crs.getString("finyear_enddate"));
				}
				crs.close();
			}

			type_id = request.getParameter("dr_type_id");
			month = CNumeric(PadQuotes(request.getParameter("dr_month")));
			ledger_id = CNumeric(PadQuotes(request.getParameter("dr_ledger_id")));

			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	protected void CheckForm() {
		msg = "";
		if (finyear_id.equals("0")) {
			msg += "<br>Select Financial Year!";
		}
		if (type_id.equals("1")) {
			findYearMonth(month, finyear_id);
		} else {
			start_date = finyear_startdate;
		}

		if (branch_id.equals("0") || branch_id.equals("")) {
			msg += "<br>Branch Not Selected!";
		}

		if (ledger_id.equals("0")) {
			msg += "<br>Select Ledger!";
		}

	}

	public String Listdata(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT"
					+ " voucher_date,"
					+ " COALESCE ( ( SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) )"
					+ " - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) ), 0 ) AS vouchertrans_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer  ON customer_id = vouchertrans_customer_id"
					+ " WHERE 1 = 1"
					+ " AND voucher_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND ( ( vouchertype_authorize = 1 AND voucher_authorize = 1 ) OR (vouchertype_authorize = 0))"
					+ " AND vouchertrans_amount != 0"
					+ " AND customer_id = " + ledger_id;
			if (type_id.equals("1")) {
				StrSql += " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR(" + start_date + ", 1, 8)"
						+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR(" + end_date + ", 1, 8)";
			}
			StrSql += " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR(" + finyear_startdate + ", 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR(" + finyear_enddate + ", 1, 8)"
					+ StrSearch;
			if (type_id.equals("1")) {
				StrSql += " GROUP BY SUBSTR(voucher_date, 1, 8)";
			} else {
				StrSql += " GROUP BY SUBSTR(voucher_date, 1, 6)";
			}
			StrSql += " ORDER BY SUBSTR(voucher_date, 1, 8)"
					+ " LIMIT 2000";

			// SOP("StrSql==Cash Book====" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			StrSql = "SELECT"
					+ " COALESCE ( ( SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) )"
					+ " - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) ) ), 0 ) AS openingbal"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " WHERE 1 = 1"
					+ " AND vouchertrans_customer_id = " + ledger_id
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND voucher_active = 1"
					+ " AND vouchertrans_item_id = 0"
					+ " AND vouchertrans_tax_id = 0"
					+ " AND voucher_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND ( ( vouchertype_authorize = 1 AND voucher_authorize = 1 ) OR (vouchertype_authorize = 0) )"
					+ " AND substr(voucher_date, 1, 8) < substr(" + start_date + ", 1, 8)"
					+ StrSearch;

			CachedRowSet crs1 = processQuery(StrSql, 0);
			int count = 0;

			if (crs.isBeforeFirst()) {

				export = "Export";

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");

				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				if (type_id.equals("1")) {
					Str.append("<th data-toggle=\"true\">Date</th>\n");
				} else {
					Str.append("<th data-toggle=\"true\">Month</th>\n");
				}

				Str.append("<th data-hide=\"phone, tablet\">Debit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Credit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Running Balance</th>\n");
				Str.append("</tr></thead>\n");
				crs.beforeFirst();

				while (crs.next()) {
					String openingbal = "";
					if (crs1.isBeforeFirst() && crs1.next()) {

						openingbal = crs1.getString("openingbal");
						Str.append("<tr>\n");
						Str.append("<td valign=top colspan='2' align=right>").append("<b>Opening Balance</b>").append("</td>\n");
						Str.append("<td valign=top colspan='3' align=right><b>").append(IndDecimalFormat(df.format(Double.parseDouble(openingbal))));

						if (Double.parseDouble(openingbal) > 0.0) {
							running_bal += Double.parseDouble(openingbal);
							Str.append(" DR");
						} else if (Double.parseDouble(openingbal) < 0.0) {
							running_bal -= Double.parseDouble(openingbal);
							Str.append(" CR");
						}
						Str.append("</b></td>\n");
						Str.append("</tr>");
					}
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					if (type_id.equals("1")) {
						Str.append("<td valign=top align=center>").append(crs.getString("voucher_date").subSequence(6, 8) + "-" + TextMonth(Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) - 1).substring(0, 3)).append("</td>\n");
					} else {
						Str.append("<td valign=top align=left>").append(TextMonth(Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) - 1)).append("</td>\n");
					}
					if (Double.parseDouble(crs.getString("vouchertrans_amount")) > 0.0) {
						running_bal += Double.parseDouble(crs.getString("vouchertrans_amount"));
						if (drtotal != 0.00) {
							drtotal += Double.parseDouble(crs.getString("vouchertrans_amount"));
						} else {
							drtotal = Double.parseDouble(crs.getString("vouchertrans_amount"));
						}
						Str.append("<td valign=top align=right>").append(IndDecimalFormat(df.format(crs.getDouble("vouchertrans_amount")))).append("</td>\n");
						Str.append("<td valign=top align=right>").append("</td>\n");
					} else if (Double.parseDouble(crs.getString("vouchertrans_amount")) < 0.0) {
						running_bal -= Double.parseDouble(crs.getString("vouchertrans_amount"));
						if (crtotal != 0.00) {
							crtotal += Double.parseDouble(crs.getString("vouchertrans_amount"));
						} else {
							crtotal = Double.parseDouble(crs.getString("vouchertrans_amount"));
						}
						Str.append("<td valign=top align=right>").append("</td>\n");
						Str.append("<td valign=top align=right>").append(IndDecimalFormat(df.format(crs.getDouble("vouchertrans_amount")))).append("</td>\n");
					}

					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(running_bal)));
					if (running_bal > 0.0) {
						Str.append(" DR");
					} else if (running_bal < 0.0) {
						Str.append(" CR");
					}
					Str.append("</b></td>\n");
					Str.append("</tr>\n");

				}

				Str.append("<tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>").append("Total:").append("</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(drtotal))).append("</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(crtotal))).append("</b></td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>");
				if (drtotal > crtotal) {
					Str.append("Total Debit:");
				} else {
					Str.append("Total Credit:");
				}
				Str.append("</b></td>\n");
				if (drtotal > crtotal) {
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(drtotal - crtotal))).append("</b></td>\n");
					Str.append("<td valign=top align=right><b>").append("0.00").append("</b></td>\n");
				} else {
					Str.append("<td valign=top align=right><b>").append("0.00").append("</b></td>\n");
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(df.format(crtotal - drtotal))).append("</b></td>\n");
				}
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>");
				Str.append("Closing Balance:");
				Str.append("</b></td>\n");

				if (running_bal >= 0) {
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(running_bal)) + " DR");
					Str.append("</b></td>\n");
					Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				} else {
					Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(df.format(Double.parseDouble(String.valueOf(running_bal).substring(1)))) + " CR");
					Str.append("</b></td>\n");
				}
				Str.append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<br><br><br><br><center><font color=red><b>No Records found!</b></font></center><br><br><br><br><br>");
			}
			crs1.close();
			crs.close();

		}

		catch (Exception ex) {
			SOPError("Report Day Book===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
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

	public String PopulateType(String type_id) {
		StringBuilder Str = new StringBuilder();
		try {
			String str[] = {"Day", "Month"};
			for (int i = 1; i <= 2; i++) {
				Str.append("<option value=").append(i + "");
				Str.append(StrSelectdrop(i + "", type_id));
				Str.append(">").append(str[i - 1]).append("</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePayment(String payment) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id, customer_name"
					+ " FROM " + compdb(comp_id) + " axela_customer"
					+ " WHERE customer_ledgertype !=0"
					+ " GROUP BY customer_id" + " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql);

			Str.append("<option value='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"), payment));
				Str.append(">").append(crs.getString("customer_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 04").append(Selectdrop(4, month)).append(">April</option>\n");
		Str.append("<option value = 05").append(Selectdrop(5, month)).append(">May</option>\n");
		Str.append("<option value = 06").append(Selectdrop(6, month)).append(">June</option>\n");
		Str.append("<option value = 07").append(Selectdrop(7, month)).append(">July</option>\n");
		Str.append("<option value = 08").append(Selectdrop(8, month)).append(">August</option>\n");
		Str.append("<option value = 09").append(Selectdrop(9, month)).append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month)).append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month)).append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month)).append(">December</option>\n");
		Str.append("<option value = 01").append(Selectdrop(1, month)).append(">January</option>\n");
		Str.append("<option value = 02").append(Selectdrop(2, month)).append(">February</option>\n");
		Str.append("<option value = 03").append(Selectdrop(3, month)).append(">March</option>\n");
		return Str.toString();
	}

	public void findYearMonth(String month, String finyear_id) {
		if (Integer.parseInt(CNumeric(month)) <= 3) {
			start_date = finyear_enddate.substring(0, 4) + month + "01000000";
			end_date = finyear_enddate.substring(0, 4) + month + "31000000";
		} else {
			start_date = finyear_startdate.substring(0, 4) + month + "01000000";
			end_date = finyear_startdate.substring(0, 4) + month + "31000000";
		}
	}

}
