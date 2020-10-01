package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Trail_Balance extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String startdate = "", start_date = "";
	public String enddate = "", end_date = "";
	public String msg = "";
	public String emp_id = "";

	public String brand_id = "", branch_id = "0", region_id = "0";
	public String[] brand_ids, branch_ids;

	public String StrHTML = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";
	public String show_export = "";
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access, emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				startdate = ReportStartdate();
				enddate = DateToShortDate(kknow());

				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = TrailBalance();
					} else {
						msg = "Error" + msg;
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
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");

	}

	protected void CheckForm() {
		msg = "";
		if (brand_id.equals("")) {
			msg += "<br>Select Brand!";
		}
		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		}
		if (!startdate.equals("")) {
			if (Long.parseLong(ConvertShortDateToStr(startdate)) > Long
					.parseLong(ToShortDate(kknow()))) {
				msg += "<br>Start Date can't be greater than Today's Date!";
			} else if (!isValidDateFormatShort(startdate)) {
				msg += "<br>Enter Valid Start Date!";
			} else {
				start_date = ConvertShortDateToStr(startdate);
			}
		}
		if (enddate.equals("")) {
			msg += "<br>Select End Date!";
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
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter Valid End Date!";
			}
		}
	}

	public String TrailBalance() {
		StringBuilder Str = new StringBuilder();
		double totalDR = 0, totalCR = 0;

		try {
			StrSql = "SELECT"
					+ " accgroup_id, accgroup_name,"
					+ " CONCAT(customer_name,' (',vouchertrans_customer_id,')') AS customer_name,"
					+ " vouchertrans_customer_id, accgroup_alie,"
					+ " GROUP_CONCAT(voucher_id) AS voucher_id,"
					+ " COALESCE ("
					+ " ( SUM( IF ( vouchertrans_dc = 1, vouchertrans_amount, 0 ) )"
					+ " - SUM( IF ( vouchertrans_dc = 0, vouchertrans_amount, 0 ) )"
					+ " ), 0 ) AS amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id AND voucherclass_acc = 1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE 1 = 1"
					+ " AND voucher_active = 1"
					+ " AND vouchertype_affects_accounts = 1"
					+ " AND vouchertrans_amount != 0"
					+ " AND ( ( vouchertype_authorize = 1 AND voucher_authorize = 1 ) OR (vouchertype_authorize = 0) )"
					+ " AND substr(voucher_date, 1, 8) >= substr(" + start_date + ", 1, 8)"
					+ " AND substr(voucher_date, 1, 8) <= substr(" + end_date + ", 1, 8)";
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " GROUP BY customer_accgroup_id"
					// + " GROUP BY customer_accgroup_id"
					+ " ORDER BY customer_name"
					// + " LIMIT 10000"
					+ "";

			// SOP("StrSql==trail balance=" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class='table-responsive table-bordered'>");

			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(start_date);
			String startDate = new SimpleDateFormat("dd-MMM-yyyy").format(date);
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse(end_date);
			String endDate = new SimpleDateFormat("dd-MMM-yyyy").format(date);

			Str.append("<div><h6><b>Trial Balance<br>" + startDate + " To " + endDate + "</b></h6></div>");
			Str.append("<table class='table table-bordered table-hover table-responsive' data-filter='#filter' id='table'>");
			Str.append("<thead><tr>");
			Str.append("<th>Particulars</th>");
			Str.append("<th>Debit</th>");
			Str.append("<th>Credit</th>");
			Str.append("</tr></thead>");;
			Str.append("<tbody>");
			while (crs.next()) {
				Str.append("<tr>");
				Str.append("<td>")
						.append("<a href='../accounting/report-ledgerstatement.jsp?all=yes&trial_bal=yes&trial_bal_principal=" + brand_id + "&trial_bal_branch=" + branch_id + "&ledger="
								+ crs.getString("vouchertrans_customer_id") + "&txt_startdate=" + startdate
								+ "&txt_enddate=" + enddate + "' target='_blank'>"
								+ crs.getString("customer_name") + "</a>")
						.append("</td>");
				if (Double.parseDouble(CNumeric(PadQuotes(crs.getString("amount")))) > 0) {
					totalDR += Double.parseDouble(CNumeric(PadQuotes(crs.getString("amount"))));
					Str.append("<td class='text-right'>").append(IndFormat(deci.format(Double.parseDouble(CNumeric(PadQuotes(crs.getString("amount"))))))).append("</td>");
					Str.append("<td>").append("").append("</td>");
				} else {
					totalCR += Double.parseDouble(CNumeric(PadQuotes(crs.getString("amount")))) * (-1);
					Str.append("<td>").append("").append("</td>");
					Str.append("<td class='text-right'>").append(IndFormat(deci.format(Double.parseDouble(CNumeric(PadQuotes(crs.getString("amount")))) * (-1)))).append("</td>");
				}
				Str.append("</tr>");
			}
			Str.append("<tr>");
			Str.append("<td class='text-right'><b>").append("Total:").append("</b></td>");
			Str.append("<td class='text-right'><b>").append(IndFormat(deci.format(totalDR))).append("</b></td>");
			Str.append("<td class='text-right'><b>").append(IndFormat(deci.format(totalCR))).append("</b></td>");
			Str.append("</tr>");
			Str.append("</tbody>");
			Str.append("</table>");
			Str.append("</div>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
