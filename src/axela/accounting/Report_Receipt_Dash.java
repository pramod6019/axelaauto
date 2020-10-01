package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Receipt_Dash extends Connect {

	public String submitB = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "";
	public String StrHTML = "", InvoiceStrHTML = "", Strhtml = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String emp_all_exe = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public Acc_Check acccheck = new Acc_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + ExeAccess;

					if (!starttime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(voucher_date,1,8) >= SUBSTR('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(voucher_date,1,8) <= SUBSTR('" + endtime + "',1,8)";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND voucher_emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch = StrSearch + " AND voucher_branch_id IN(" + branch_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ReceiptSummary();
						InvoiceStrHTML = InvoiceSummary();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String ReceiptSummary() {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		double receipt_total_amount = 0.00, authorize_amount = 0.00, unauthorize_amount = 0.00;
		int count_receipt_total = 0, count_authorize_total = 0, un_authorize_total = 0;
		String url_parameter = "../accounting/voucher-list.jsp?all=yes&voucherclass_id=9&vouchertype_id=9"
				+ "&starttime=" + starttime + "&endtime=" + endtime + "&exe_emp_id=" + exe_id
				+ "&brand_id=" + brand_id + "&region_id=" + region_id + "&branch_id=" + branch_id;

		try {

			StrSql = "SELECT paymode_name,paymode_id,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 THEN 1 END ), 0 ) AS receipttotal,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 THEN voucher_amount END ), 0 ) AS receiptamt,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 AND voucher_authorize = 1 THEN 1 END ), 0 ) AS authorize,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 AND voucher_authorize = 1 THEN voucher_amount END ), 0 ) AS authorizeamt,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 AND voucher_authorize != 1 THEN 1 END ), 0 ) AS unauthorize,"
					+ " COALESCE ( SUM( CASE WHEN voucher_vouchertype_id = 9 AND voucher_authorize != 1 THEN voucher_amount END ), 0 ) AS unauthorizeamt"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE 1 = 1"
					+ " AND voucher_vouchertype_id = 9"
					+ " AND voucher_active = 1"
					+ " AND branch_active = 1"
					+ " AND vouchertrans_dc = 1"
					+ " " + StrSearch
					+ " GROUP BY paymode_id"
					+ " ORDER BY paymode_id";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Payment Mode</th>\n");
				Str.append("<th>Receipt</th>\n");
				Str.append("<th>Total Amount</th>\n");
				Str.append("<th>Authorized</th>\n");
				Str.append("<th data-hide=\"phone\">Authorized Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Un-Authorized</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Un-Authorized Amount</th>\n");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {

					String url_parameter_paymode = url_parameter + "&paymode_id=" + crs.getString("paymode_id");

					receipt_total_amount += crs.getDouble("receiptamt");
					authorize_amount += crs.getDouble("authorizeamt");
					unauthorize_amount += crs.getDouble("unauthorizeamt");

					count_receipt_total += crs.getInt("receipttotal");
					count_authorize_total += crs.getInt("authorize");
					un_authorize_total += crs.getInt("unauthorize");

					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count++).append("</td>");
					Str.append("<td align=left>").append(crs.getString("paymode_name")).append("</td>");
					Str.append("<td align=right>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter_paymode + "&receiptdash=yes','salesdashreceipt','');remote.focus();\">" + crs.getString("receipttotal"))
							.append("</a>")
							.append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("receiptamt")))).append("</td>");
					Str.append("<td align=right>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter_paymode + "&voucher_authorize=1&receiptdash=yes','salesdashreceipt','');remote.focus();\">"
									+ crs.getString("authorize")
									+ "</a>").append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("authorizeamt")))).append("</td>");
					Str.append("<td align=right>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter_paymode + "&voucher_authorize=0&receiptdash=yes','salesdashreceipt','');remote.focus();\">"
									+ crs.getString("unauthorize") + "</a>").append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("unauthorizeamt")))).append("</td>");
					Str.append("</tr>");
				}

				Str.append("<tr align=center valign=top>");
				Str.append("<td align=right colspan='2'>").append("<b>Total:</b>").append("</td>");
				Str.append("<td align=right>").append("<b> <a href=\"javascript:remote=window.open('" + url_parameter + "&receiptdash=yes','salesdashreceipt','');remote.focus();\">"
						+ count_receipt_total + "</a> </b> </td>");
				Str.append("<td align=right>").append("<b>" + IndFormat(df.format(receipt_total_amount)) + "</b> </td>");
				Str.append("<td align=right>")
						.append("<b> <a href=\"javascript:remote=window.open('" + url_parameter + "&voucher_authorize=0&receiptdash=yes','salesdashreceipt','');remote.focus();\">"
								+ count_authorize_total + "</a> </b> </td>");
				Str.append("<td align=right >").append("<b>" + IndFormat(df.format(authorize_amount)) + "</b> </td>");
				Str.append("<td align=right >")
						.append("<b> <a href=\"javascript:remote=window.open('" + url_parameter + "&voucher_authorize=0&receiptdash=yes','salesdashreceipt','');remote.focus();\">"
								+ un_authorize_total + "</a> </b> </td>");
				Str.append("<td align=right > <b>" + IndFormat(df.format(unauthorize_amount)) + "</b> </td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<center><font color=red><b>No Receipts found!</b></font></center>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error ReceiptSummary: " + ex);
			return "";
		}
		return Str.toString();
	}
	public String InvoiceSummary() {

		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		String StrStage = "";
		String StrClose = "";

		String url_parameter = "../accounting/voucher-list.jsp?all=yes&voucherclass_id=6&vouchertype_id=6"
				+ "&starttime=" + starttime + "&endtime=" + endtime + "&exe_emp_id=" + exe_id
				+ "&brand_id=" + brand_id + "&region_id=" + region_id + "&branch_id=" + branch_id;

		try {
			int count = 0;
			StrSql = "SELECT"
					+ " branch_name, emp_name,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 THEN 1 END),0) AS invoicetotal,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 THEN voucher_amount END),0) AS invoiceamt,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 AND voucher_authorize = 1 THEN 1 END),0) AS authorize,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 AND voucher_authorize = 1 THEN voucher_amount END),0) AS authorizeamt,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 AND voucher_authorize != 1 THEN 1 END),0) AS unauthorize,"
					+ " COALESCE(SUM(CASE WHEN voucher_vouchertype_id = 6 AND voucher_authorize != 1 THEN voucher_amount END),0) AS unauthorizeamt"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE 1 = 1 "
					+ " AND voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND branch_active = 1" + StrSearch + "";

			// SOP("InvoiceSummary===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>Invoice</th>\n");
				Str.append("<th>Total Amount</th>\n");
				Str.append("<th>Authorized</th>\n");
				Str.append("<th data-hide=\"phone\">Authorized Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Un-Authorized</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Un-Authorized Amount</th>\n");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center valign=top>\n");

					Str.append("<td align=center>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter + "&receiptdash=yes','salesdashinvoice','');remote.focus();\">" + crs.getString("invoicetotal")
									+ "</a>")
							.append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("invoiceamt")))).append("</td>");
					Str.append("<td align=center>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter + "&voucher_authorize=1&receiptdash=yes','salesdashinvoice','');remote.focus();\">"
									+ crs.getString("authorize")
									+ "</a>").append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("authorizeamt")))).append("</td>");
					Str.append("<td align=center>")
							.append("<a href=\"javascript:remote=window.open('" + url_parameter + "&voucher_authorize=0&receiptdash=yes','salesdashinvoice','');remote.focus();\">"
									+ crs.getString("unauthorize") + "</a>").append("</td>");
					Str.append("<td align=right>").append(IndFormat(df.format(crs.getDouble("unauthorizeamt")))).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<center><font color=red><b>No Invoices found!</b></font></center>");

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error InvoiceSummary: " + ex);
			return "";
		}
		return Str.toString();
	}

}