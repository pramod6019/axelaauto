package axela.invoice;
//Saiman 13th Dec 2012

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_TotalBalance extends Connect {

	public String StrSql = "";
	// public String starttime = "", start_time = "";
	// public String endtime = "", end_time = "";
	public String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "";
	public String[] team_ids, exe_ids, itemgroup_ids, brand_ids, branch_ids;
	public String team_id = "", exe_id = "", itemgroup_id = "";
	public String BranchAccess = "";
	public String dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String so_month = "";
	public String voucher_amount = "";
	public String receipt_amount = "";
	public String StrSearch = "";

	public String StrHTML = "";
	public double total_invoice = 0.00;
	public double total_receipts = 0.00;
	public double total_balances = 0.00;
	DecimalFormat deci = new DecimalFormat("0.00");
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "1", request, response);
			emp_id = CNumeric(GetSession("emp_id", request)) + "";
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			// ExeAccess = GetSession("ExeAccess", request);
			go = PadQuotes(request.getParameter("submit_button"));
			GetValues(request, response);
			CheckForm();
			if (go.equals("Go")) {
				StrSearch = BranchAccess + " " + ExeAccess;
				if (!exe_id.equals("")) {
					StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
				}
				if (!dr_branch_id.equals("0")) {
					StrSearch = StrSearch + " AND voucher_branch_id = " + dr_branch_id + "";
				}
				if (!team_id.equals("")) {
					StrSearch += " AND emp_id IN (SELECT teamtrans_emp_id"
							+ " FROM  " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_team_id IN (" + team_id + "))";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					StrHTML = ListTarget();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
	}

	public String ListTarget() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		// invoicetotal-receipttotal=balance
		StrSql = "SELECT emp_id, emp_name, emp_ref_no, SUM(voucher_amount) AS invoicetotal,"
				+ " COALESCE((SELECT SUM(voucherbal_amount) FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher inv ON inv.voucher_id = voucherbal_trans_id"
				+ " WHERE voucher_active = 1"
				+ " AND inv.voucher_active = 1"
				+ " AND inv.voucher_emp_id = emp_id), 0) AS receipttotal,"
				+ " (SUM(voucher_amount)- COALESCE((SELECT SUM(voucherbal_amount)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher inv ON inv.voucher_id = voucherbal_trans_id"
				+ " WHERE voucher_active = 1"
				+ " AND inv.voucher_active = 1"
				+ " AND inv.voucher_emp_id = emp_id), 0)) AS balance"
				+ " FROM  " + compdb(comp_id) + "axela_emp"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_emp_id=emp_id"
				+ " WHERE voucher_active = 1" + StrSearch + " "
				+ " GROUP BY emp_id"
				+ " HAVING balance > 0"
				+ " ORDER BY emp_name";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Invoices</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Receipts</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Balances</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					total_invoice = total_invoice + crs.getDouble("invoicetotal");
					total_receipts = total_receipts + crs.getDouble("receipttotal");
					total_balances = total_balances + crs.getDouble("balance");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append(" (")
							.append(crs.getString("emp_ref_no")).append(")</a></td>");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("invoicetotal")))).append("</td>");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("receipttotal")))).append("</td>");
					Str.append("<td valign=top align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("balance")))).append("</td>");
					Str.append("</tr>");
				}
				if (count > 1) {
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right colspan=2><b>Total:</b></td>");
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(deci.format(total_invoice))).append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(deci.format(total_receipts))).append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(IndDecimalFormat(deci.format(total_balances)).replace("-,", "-")).append("</b></td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

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
		String stringval = "";
		try {
			StrSql = "SELECT team_id, team_name"
					+ " FROM  " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + dr_branch_id + ""
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
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
		return stringval = Str.toString();
	}

	public String PopulateSalesExecutives(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			String exe = "";
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'"
					+ " AND (emp_branch_id = " + dr_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM  " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + "))" + ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql += " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_executive id=dr_executive class=form-control multiple=\"multiple\" size=10 >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return exe = Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
