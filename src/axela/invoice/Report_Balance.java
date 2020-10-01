package axela.invoice;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Balance extends Connect {

	public String StrSql = "";
	public String start_time = "", starttime = "";
	public String end_time = "", endtime = "";
	public static String msg = "";
	public String emp_id = "0", branch_id = "0", brand_id = "";
	public String[] team_ids, exe_ids, itemgroup_ids, brand_ids, branch_ids;
	public String team_id = "0", exe_id = "0", itemgroup_id = "0";
	public String BranchAccess = "";
	public String go = "";
	public String ExeAccess = "";
	public String so_month = "";
	public String voucher_amount = "";
	public String voucherbal_amount = "";
	public String StrSearch = "";
	public String StrHTML = "";

	DecimalFormat deci = new DecimalFormat("0.00");
	public int curryear = 0;
	public int currmonth = 0;
	StringBuilder SearchSql = new StringBuilder();
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			// ExeAccess = GetSession("ExeAccess", request);
			go = PadQuotes(request.getParameter("submit_button"));
			curryear = Integer.parseInt(CNumeric(ToShortDate(kknow())).substring(0, 4));
			if (go.equals("")) {
				starttime = strToShortDate(ToShortDate(kknow()));
				endtime = strToShortDate(ToShortDate(kknow()));
				msg = "";
			}
			if (go.equals("Go")) {
				GetValues(request, response);
				CheckForm();
				StrSearch = BranchAccess + " " + ExeAccess;
				StrSearch = StrSearch + "AND  SUBSTR(voucher_payment_date, 1, 8) >= SUBSTR('" + start_time + "', 1, 8)"
						+ " AND SUBSTR(voucher_payment_date, 1, 8) < SUBSTR('" + end_time + "', 1, 8)";

				if (!exe_id.equals("")) {
					StrSearch += " AND emp_id IN (" + exe_id + ")";
				}
				if (!CNumeric(branch_id).equals("0")) {
					StrSearch += " AND voucher_branch_id = " + branch_id + "";
				}
				if (!team_id.equals("")) {
					StrSearch += " AND emp_id in (SELECT teamtrans_emp_id"
							+ " FROM  " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_team_id IN (" + team_id + "))";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					StrSearch += " AND voucher_active = 1";
					StrHTML = ListTarget();
					if (!SearchSql.toString().equals("")) {
						SetSession("invoicestrsql", " AND (" + SearchSql.toString() + ")", request);
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		SOP("branch id===----==" + branch_id);
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				start_time = ConvertShortDateToStr(starttime);
			} else {
				msg += "<br>Enter valid Start Date!";
			}
		}
		if (endtime.equals("")) {
			msg += "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				end_time = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("")
						&& isValidDateFormatShort(starttime)
						&& Long.parseLong(start_time) > Long.parseLong(end_time)) {
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter valid End Date!";
			}
		}
		if (!endtime.equals("") && !starttime.equals("") && isValidDateFormatShort(starttime) && isValidDateFormatShort(endtime)) {
			if (Long.parseLong(end_time) > Long.parseLong(ConvertShortDateToStr(AddDayMonthYear(strToShortDate(start_time), 0, 0, 1, 0)))) {
				msg = msg + "<br>Difference of Start Date and End Date should not be greater than 1 month!";
			}
		}
	}

	public String ListTarget() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		double total_invoice_value = 0.00, total_receipt_value = 0.00, total_balance = 0.00;

		// select emp_id, emp_name, emp_ref_no, voucher_id, voucher_date,
		// voucher_payment_date, voucher_amount as invoicetotal,
		// @receipttotal:= coalesce((select sum(voucherbal_amount)
		// from " + compdb(comp_id) + "axela_acc_voucher_bal
		// inner join " + compdb(comp_id) + "axela_acc_voucher inv on inv.voucher_id = voucherbal_trans_id
		// where voucher_active = 1 and inv.voucher_active = 1
		// and inv.voucher_emp_id = emp_id), 0) as receipttotal,
		// (voucher_amount-@receipttotal) as balance
		// from " + compdb(comp_id) + "axela_acc_voucher
		// inner join " + compdb(comp_id) + "axela_emp on emp_id = voucher_emp_id
		// where 1 = 1
		// -- and (branch_id = 2)
		// and substr(voucher_payment_date, 1, 8) >= substr('20140410000000', 1, 8)
		// and substr(voucher_payment_date, 1, 8) < substr('20150410000000', 1, 8)
		// and voucher_branch_id = 2 and voucher_active = 1
		// group by voucher_id having balance > 0
		// order by voucher_payment_date

		StrSql = "SELECT emp_id, emp_name, emp_ref_no, voucher_id, voucher_date, voucher_payment_date, voucher_amount AS invoicetotal,"
				+ " @receipttotal:= COALESCE((SELECT SUM(voucherbal_amount) "
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher inv ON inv.voucher_id = voucherbal_trans_id"
				+ " WHERE voucher_active = 1"
				+ " AND inv.voucher_active = 1"
				+ " AND inv.voucher_emp_id = emp_id), 0) AS receipttotal,"
				+ " (voucher_amount-@receipttotal) AS balance"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE 1 = 1"
				+ StrSearch + ""
				+ " GROUP BY voucher_id"
				+ " HAVING balance > 0"
				+ " ORDER BY voucher_payment_date";
		// SOP("StrSql==ListTarget=" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Invoice ID</th>\n");
				Str.append("<th>Invoice Date</th>\n");
				Str.append("<th>Payment Date</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Invoices</th>\n");
				Str.append("<th>Receipts</th>\n");
				Str.append("<th>Balances</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					if (count == 1) {
						SearchSql.append("voucher_id = ").append(crs.getString("voucher_id"));
					} else {
						SearchSql.append(" OR voucher_id = ").append(crs.getString("voucher_id"));
					}
					total_receipt_value += crs.getDouble("receipttotal");
					total_invoice_value += crs.getDouble("invoicetotal");
					total_balance += crs.getDouble("balance");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center>");
					Str.append("<a href=../accounting/voucher-list.jsp?voucher_id=").append(crs.getString("voucher_id")).append(">");
					Str.append(crs.getInt("voucher_id")).append("</a></td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_date"))).append("</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_payment_date"))).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">");
					Str.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")</a></td>\n");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("invoicetotal")))).append("</td>\n");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("receipttotal")))).append("</td>\n");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("balance")).replace("-,", "-"))).append("</td>\n");
					Str.append("</tr>\n");
				}
				if (count > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"right\" colspan=\"5\"><b>Total:</b></td>");
					Str.append("<td valign=\"top\" align=\"right\"><b>").append(IndDecimalFormat(deci.format(total_invoice_value))).append("</b></td>\n");
					Str.append("<td valign=\"top\" align=\"right\"><b>").append(IndDecimalFormat(deci.format(total_receipt_value))).append("</b></td>\n");
					Str.append("<td valign=\"top\" align=\"right\"><b>").append(IndDecimalFormat(deci.format(total_balance).replace("-,", "-"))).append("</b></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");

			} else {
				Str.append("<font color=\"red\"><b>No Balance Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name"
					+ " FROM  " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE 1 = 1";
			if (!CNumeric(branch_id).equals("0")) {
				StrSql += " AND team_branch_id = " + branch_id + "";
			}
			StrSql += " GROUP BY team_id"
					+ " ORDER BY team_name";
			SOP("StrSql=PopulateTeam==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id"));
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option>\n)");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateSalesExecutives(String comp_id) {
		// SOP("team_id===" + team_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'"
					+ " AND (";
			if (!CNumeric(branch_id).equals("0")) {
				StrSql += " emp_branch_id = " + branch_id + " OR ";
			}
			StrSql += " emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM  " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id";
			if (!CNumeric(branch_id).equals("0")) {
				StrSql += " AND empbr.emp_branch_id = " + branch_id + "";
			}
			StrSql += " ))" + ExeAccess + "";
			if (!CNumeric(team_id).equals("0")) {
				StrSql += " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("PopulateSalesExecutives====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_executive id=dr_executive class=form-control multiple=\"multiple\" size=10>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("emp_id"));
					Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
					Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
				}
			} else {
				// Str.append("<option value = 0>").append("No Sales Executive</option>");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
