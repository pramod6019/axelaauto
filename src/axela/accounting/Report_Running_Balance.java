// smitha nag june 6 2013
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

public class Report_Running_Balance extends Connect {

	public String StrHTML = "";
	public String LinkExportPage = "voucher-export.jsp";
	public String msg = "";
	public String StrSql = "";
	public String period_id = "1";
	public String comp_id = "0";
	public String customer_id = "";
	public double running_bal = 0.00;
	public double trans_total = 0.00;
	public Double trans_total1 = 0.00;
	public String customer_accgroup_id = "";
	public String customer_op_balance_dc = "";
	public String startdate = "";
	public String enddate = "";
	public String start_date = "";
	public String end_date = "";
	public String formatdate_datepicker = "";

	public double closing_bal = 0.00;
	public String go = "";
	public String month = "";
	public String show_export = "";
	DecimalFormat df = new DecimalFormat("0.00");
	double runningbal = 0.00, oppbal = 0.00;
	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				show_export = PadQuotes(request.getParameter("ExportButton"));
				go = PadQuotes(request.getParameter("submit_button"));
				month = PadQuotes(request.getParameter("submit_button_monthly"));

				startdate = ReportStartdate();
				enddate = DateToShortDate(kknow());

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if ((msg.equals(""))) {
						LedgerInfo(customer_id);
						StrHTML = Listdata();
					}
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (customer_id.equals("")) {
			msg += "<br>Select Ledger!";
		}
		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		}
		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter Valid Start Date!";
			}
		}

		if (enddate.equals("")) {
			msg += "<br>Select End Date!";
		}
		if (period_id.equals("0")) {
			msg += "<br>Select Period!";
		}
		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (Long.parseLong(ConvertShortDateToStr(enddate)) > Long
						.parseLong(ToShortDate(kknow()))) {
					msg += "<br>End Date can't be greater than Today's Date!";
				} else if (!startdate.equals("")
						&& !enddate.equals("")
						&& Long.parseLong(start_date) > Long
								.parseLong(end_date)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_id = PadQuotes(request.getParameter("ledger"));
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		period_id = CNumeric(PadQuotes(request.getParameter("dr_period")));
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		Integer no_days = (int) getDaysBetween(start_date, end_date);
		Integer no_month = Integer.parseInt(getMonthsBetween(start_date,
				end_date));
		Str.append("<div class=\"table-responsive table-bordered\">\n");
		Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");

		Str.append("<thead><tr>\n");
		Str.append("<th data-toggle=\"true\">Date</th>\n");
		Str.append("<th data-hide=\"phone\">Debit</th>\n");
		Str.append("<th data-hide=\"phone\">Credit</th>\n");
		Str.append("<th>Running Balance</th>\n");
		Str.append("</tr></thead>\n");
		if (period_id.equals("1")) {
			Str.append(RunningBalanceDaily());
		} else {
			Str.append(RunningBalanceMonthly());
		}
		Str.append("</table>\n");
		Str.append("</div>\n");
		return Str.toString();
	}
	public void LedgerInfo(String customer_id) throws SQLException {
		StrSql = "SELECT"
				+ " (customer_open_bal +  "
				+ " (SELECT COALESCE(sum(IF(vouchertrans_dc=1,vouchertrans_amount, -vouchertrans_amount)),0)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " WHERE voucher_active = 1 and vouchertrans_customer_id = customer_id"
				+ " AND substr(voucher_date ,1,8) <= substr(" + start_date + ",1,8) )) as ledgeropbal"
				+ " FROM  " + compdb(comp_id) + "axela_customer"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
				+ " WHERE customer_id = " + customer_id + ""
				+ " GROUP BY customer_id";
		// SOP("StrSql=ledger Info==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			runningbal = Double.parseDouble(crs.getString("ledgeropbal"));
		}
		crs.close();
	}

	public String RunningBalanceDaily() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT voucher_date AS voucher_date,"
				+ " @debit:=sum(if(vouchertrans_dc = 1 , coalesce(vouchertrans_amount,0.00),0.00)) as debit, "
				+ " @credit:=sum(if(vouchertrans_dc = 0 , coalesce(vouchertrans_amount,0.00),0.00)) as credit,"
				+ " COALESCE(SUM(if(vouchertrans_dc = 1,vouchertrans_amount, -vouchertrans_amount)),0) AS runningbal "
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				+ " WHERE 1 = 1 "
				+ " AND substr(voucher_date,1,8) >= substr(" + start_date + ",1,8)"
				+ " AND substr(voucher_date,1,8) <= substr(" + end_date + ",1,8)"
				+ " AND customer_id = " + customer_id
				+ " GROUP BY substr(voucher_date,1,8)"
				+ " ORDER BY substr(voucher_date,1,8)";
		// SOP("StrSql=Closing==" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<tbody><tr>\n");
			Str.append("<td align=right colspan='3'>\n<b>").append("Oppening Balance")
					.append("</b></td>\n");
			Str.append("<td align=right>\n<b>");
			if (runningbal < 0) {
				Str.append(df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR");
			} else {
				Str.append(df.format(runningbal) + " DR");
			}
			Str.append("</b></td>\n");
			while (crs.next()) {
				Str.append("<tr>");
				Str.append("<td align='center'>" + strToShortDate(crs.getString("voucher_date")) + "</td>");
				Str.append("<td align='right'>" + df.format(crs.getDouble("debit")) + "</td>");
				Str.append("<td align='right'>" + df.format(crs.getDouble("credit")) + "</td>");
				runningbal = (runningbal + crs.getDouble("debit") - crs.getDouble("credit"));
				if (runningbal < 0) {
					Str.append("<td align='right'>" + df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR</td>");
				} else {
					Str.append("<td align='right'>" + df.format(runningbal) + " DR</td>");
				}
				Str.append("</tr>");
			}
			Str.append("<tr>");
			Str.append("<td align='right' colspan='3'><b>Closing Balance</b></td>");
			if (runningbal < 0) {
				Str.append("<td align=right>\n<b>")
						.append("" + df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR")
						.append("</b></td>\n");
			} else {
				Str.append("<td align=right>\n<b>")
						.append("" + df.format(runningbal) + " DR")
						.append("</b></td>\n");
			}

			Str.append("</tr></tbody>\n");
			crs.close();
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();

	}

	public String RunningBalanceMonthly() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT voucher_date AS voucher_date,"
				+ " @debit:=sum(if(vouchertrans_dc = 1 , coalesce(vouchertrans_amount,0.00),0.00)) as debit, "
				+ " @credit:=sum(if(vouchertrans_dc = 0 , coalesce(vouchertrans_amount,0.00),0.00)) as credit,"
				+ " COALESCE(SUM(if(vouchertrans_dc = 1,vouchertrans_amount, -vouchertrans_amount)),0) AS runningbal "
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				// + " INNER JOIN  "+compdb(comp_id)+"axela_branch on branch_id = voucher_branch_id"
				+ " WHERE 1 = 1 "
				+ " AND substr(voucher_date,1,6) >= substr(" + start_date + ",1,6)"
				+ " AND substr(voucher_date,1,6) <= substr(" + end_date + ",1,6)"
				+ " AND customer_id = " + customer_id
				+ " GROUP BY substr(voucher_date,1,6)"
				+ " ORDER BY substr(voucher_date,1,6)";
		// SOP("StrSql=Closing=1=" + StrSqlBreaker(StrSql));

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<tbody><tr>\n");
			Str.append("<td align=right colspan='3'>\n<b>").append("Oppening Balance")
					.append("</b></td>\n");
			Str.append("<td align=right>\n<b>");
			if (runningbal < 0) {
				Str.append(df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR");
			} else {
				Str.append(df.format(runningbal) + " DR");
			}
			Str.append("</b></td>\n");
			while (crs.next()) {
				Str.append("<tr>");
				Str.append("<td align='center'>" + FormatDate(crs.getString("voucher_date"), "MMM yyyy") + "</td>");
				Str.append("<td align='right'>" + df.format(crs.getDouble("debit")) + "</td>");
				Str.append("<td align='right'>" + df.format(crs.getDouble("credit")) + "</td>");
				runningbal = (runningbal + crs.getDouble("debit") - crs.getDouble("credit"));
				if (runningbal < 0) {
					Str.append("<td align='right'>" + df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR</td>");
				} else {
					Str.append("<td align='right'>" + df.format(runningbal) + " DR</td>");
				}
				Str.append("</tr>");
			}
			Str.append("<tr>");
			Str.append("<td align='right' colspan='3'><b>Closing Balance</b></td>");
			if (runningbal < 0) {
				Str.append("<td align=right>\n<b>")
						.append("" + df.format(Double.parseDouble(String.valueOf(runningbal).substring(1))) + " CR")
						.append("</b></td>\n");
			} else {
				Str.append("<td align=right>\n<b>")
						.append("" + df.format(runningbal) + " DR")
						.append("</b></td>\n");
			}

			Str.append("</tr></tbody>\n");
			crs.close();
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();

	}
	public String PopulatePeriod() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value='1'").append(Selectdrop(1, period_id))
				.append(">Daily</option>");
		Str.append("<option value='2'").append(Selectdrop(2, period_id))
				.append(">Monthly</option>");
		return Str.toString();
	}
}
