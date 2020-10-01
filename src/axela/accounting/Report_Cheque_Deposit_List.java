package axela.accounting;
//@shivaprasad 16 Aug 2014

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Cheque_Deposit_List extends Connect {

	public String StrSql = "";
	public String startdate = "", start_date = "";
	public String enddate = "", end_date = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_id = "";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String go = "";
	public String export = "", StrSearch = "";
	public String vouchertype_id = "0", voucher_customer_id = "0", vouchertrans_customer_id = "0";
	public String ledger_idarr[] = null;
	public String voucher_type = "";
	public String customer_id = "0";
	// public String formatdate_datepicker = "";
	double credit = 0.00, debit = 0.00;
	double drtotal = 0.00, crtotal = 0.00;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// comp_id="2017";
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));

				// startdate = ReportStartdate();
				enddate = DateToShortDate(kknow());
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
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		voucher_type = PadQuotes(request.getParameter("dr_voucher_type"));
		ledger_idarr = request.getParameterValues("dr_ledger_type");

		if (ledger_idarr != null) {
			StrSearch += " AND (";
			for (int i = 0; i < ledger_idarr.length; i++) {
				if (i == 0) {
					StrSearch += " customer_id = "
							+ ledger_idarr[i] + "";
				} else {
					StrSearch += " OR customer_id = "
							+ ledger_idarr[i] + "";
				}
			}
			StrSearch += " )";
		}

		if (voucher_type.equals("1")) {
			StrSearch += " AND voucher_vouchertype_id = 9";
		}
		if (voucher_type.equals("2")) {
			StrSearch += " AND voucher_vouchertype_id = 15";
		}
	}

	protected void CheckForm() {
		msg = "";

		if (enddate.equals("")) {
			msg = msg + "<br>Select Date!";
		}

		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (Long.parseLong(ConvertShortDateToStr(enddate)) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Date can't be greater than Today's Date!";
				}
			} else {
				msg = msg + "<br>Enter Valid Date!";
			}
		}
	}

	public String Listdata(HttpServletRequest request, HttpServletResponse response) {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT customer_id, customer_name, vouchertrans_cheque_no, vouchertrans_cheque_date, vouchertrans_cheque_date, vouchertrans_amount "
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
				+ " WHERE 1 = 1" + StrSearch
				+ " AND customer_ledgertype = 2"
				+ " AND vouchertrans_paymode_id = 2"
				+ " AND SUBSTR(vouchertrans_cheque_date,1,8)<=SUBSTR(" + end_date + ",1,8)"
				+ " GROUP BY customer_id"
				+ " ORDER BY customer_id DESC";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			CachedRowSet rscopy = crs.createCopy();
			int count = 0, total = 0, amount = 0;

			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th ><span class=\"footable-toggle\"></span>Cheque No.</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Cheque Date</th>\n");
				Str.append("<th>Party</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Amount</th>\n");
				Str.append("</tr></thead>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tbody><tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("vouchertrans_cheque_no")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("vouchertrans_cheque_date"))).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("customer_name")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("vouchertrans_amount")).append("</td>\n");
					Str.append("</tr></tbody>\n");

					amount = (int) Double.parseDouble(crs.getString("vouchertrans_amount"));
					total += amount;
				}
				Str.append("<tbody><tr>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td align='right'>").append("&nbsp;").append("</td>\n");
				Str.append("<td valign=top align=right><b>").append("Total:").append("</b></td>\n");
				Str.append("<td valign=top align=right><b>").append(df.format(total)).append("</b></td>\n");
				Str.append("</tr></tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				rscopy.close();
				crs.close();

			}

			else {
				Str.append("<br><br><br><br><center><font color=red><b>No Records found!</b></font><center><br><br><br><br><br>");
			}
		}

		catch (Exception ex) {
			SOPError("Report Cheque Deposit List===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBankLedger() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT customer_id, customer_name, customer_code"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " WHERE 1=1"
					+ " AND customer_ledgertype = 2"
					+ " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("customer_id"));
				Str.append(StrSelectdrop(crs.getString("customer_id"),
						customer_id));
				Str.append(">").append(crs.getString("customer_name"))
						.append("</option>\n");
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

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value='-1'" + StrSelectdrop("1", voucher_type) + ">Select</option>");
			Str.append("<option value='1'" + StrSelectdrop("1", voucher_type) + ">Reciept</option>");
			Str.append("<option value='2'" + StrSelectdrop("2", voucher_type) + ">Payment</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}
}
