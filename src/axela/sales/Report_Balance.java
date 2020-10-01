package axela.sales;
//Saiman 13th Dec 2012
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Balance extends Connect {
	
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String so_month = "";
	public String[] x = new String[14];
	public String invoice_grandtotal = "";
	public String receipt_amount = "";
	public String StrSearch = "";
	public String StrHTML = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String dropYear = "0";
	public String dropMonth = "0";
	public String str_date = "";
	public int curryear = 0;
	public String emp_all_exe = "";
	public int currmonth = 0;
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();
	
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
				go = PadQuotes(request.getParameter("submit_button"));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				dropMonth = CNumeric(PadQuotes(request.getParameter("drop_month")));
				dropYear = CNumeric(PadQuotes(request.getParameter("drop_year")));
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				if (branch_id.equals("0")) {
					dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				} else {
					dr_branch_id = branch_id;
				}
				CheckForm();
				if (go.equals("Go")) {
					GetValues(request, response);
					// StrSearch = BranchAccess + " " + ExeAccess;
					StrSearch = BranchAccess.replace("branch_id", "invoice_branch_id");
					StrSearch += ExeAccess;
					
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " and invoice_branch_id =" + dr_branch_id;
					}
					if (dr_branch_id.equals("0")) {
						mischeck.exe_branch_id = "";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " and emp_id IN (SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = "()";
						StrHTML = ListDashboard();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = "01/" + dropMonth + "/" + dropYear;
		
		starttime = ConvertShortDateToStr(starttime);
		endtime = ConvertShortDateToStr(AddDayMonthYear(strToShortDate(starttime), 0, 0, 1, 0));
		
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}
	
	protected void CheckForm() {
		msg = "";
	}
	
	public String ListDashboard() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		double total_invoice_value = 0.00, total_receipt_value = 0.00, total_balance = 0.00;
		// StrSql = "SELECT emp_id, emp_name, emp_ref_no, invoice_id, invoice_date, invoice_payment_date, sum(invoice_grandtotal) as invoicetotal,"
		// + " @x:=COALESCE((SELECT sum(receipt_amount) FROM  " + compdb(comp_id) + "axela_invoice_receipt "
		// + " INNER JOIN  " + compdb(comp_id) + "axela_invoice inv on inv.invoice_id=receipt_invoice_id"
		// + " where 1=1 and receipt_active=1 and inv.invoice_active=1 and inv.invoice_emp_id=emp_id  ),0) as receipttotal,"
		// + " (sum(invoice_grandtotal)- @x) as balance"
		// + " from " + compdb(comp_id) + "axela_invoice"
		// + " INNER JOIN  " + compdb(comp_id) + "axela_emp on emp_id=invoice_emp_id"
		// + " where invoice_active=1 and  substr(invoice_payment_date,1,8)>=substr('" + starttime + "',1,8)"
		// + " and substr(invoice_payment_date,1,8)<substr('" + endtime + "',1,8)" + StrSearch + " "
		// + " GROUP BY invoice_id"
		// + " having balance > 0"
		// + " ORDER BY invoice_payment_date ";
		
		StrSql = "SELECT emp_id, emp_name, emp_ref_no,"
				+ " SUM(IF(voucher_vouchertype_id = 6,voucher_amount,0)) AS invoicetotal,"
				+ " COALESCE((SELECT SUM(voucher_amount) "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher "
				+ " WHERE 1 = 1"
				+ " AND voucher_vouchertype_id = 9"
				+ " AND voucher_active = 1"
				+ " AND voucher_emp_id = emp_id  ),0) AS receipttotal,"
				+ " (SUM(voucher_amount)-(COALESCE((SELECT SUM(voucher_amount) FROM  " + compdb(comp_id) + "axela_acc_voucher "
				+ " WHERE 1 = 1"
				+ " AND voucher_vouchertype_id = 9 "
				+ " AND voucher_active = 1"
				+ " AND voucher_emp_id = emp_id  ),0))) AS balance"
				+ " FROM " + compdb(comp_id) + "axela_emp "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_emp_id = emp_id"
				+ " WHERE voucher_active = 1"
				+ " AND  SUBSTR(voucher_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
				+ " AND SUBSTR(voucher_date,1,8) < SUBSTR('" + endtime + "',1,8)"
				+ StrSearch + ""
				+ " GROUP BY emp_id"
				+ " HAVING balance > 0"
				+ " ORDER BY emp_name ";
		
		// SOP("StrSql===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone\">Invoices</th>\n");
				Str.append("<th data-hide=\"phone\">Receipts</th>\n");
				Str.append("<th data-hide=\"phone\">Balances</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					
					total_receipt_value = total_receipt_value + crs.getDouble("receipttotal");
					total_invoice_value = total_invoice_value + crs.getDouble("invoicetotal");
					total_balance = total_balance + crs.getDouble("balance");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left>" + ++count + "</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id") + ">"
							+ crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</a></td>");
					Str.append("<td valign=top align=right>" + IndFormat(deci.format(crs.getDouble("invoicetotal"))) + "</td>");
					Str.append("<td valign=top align=right>" + IndFormat(deci.format(crs.getDouble("receipttotal"))) + "</td>");
					Str.append("<td valign=top align=right>" + IndFormat(deci.format(crs.getDouble("balance"))) + "</td>");
					Str.append("</tr>");
				}
				if (count > 1) {
					Str.append("<tr>\n");
					// Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
					Str.append("<td valign=top align=right colspan=2><b>Total:</b></td>");
					Str.append("<td valign=top align=right><b>" + IndFormat(deci.format(total_invoice_value)) + "</b></td>");
					Str.append("<td valign=top align=right><b>" + IndFormat(deci.format(total_receipt_value)) + "</b></td>");
					Str.append("<td valign=top align=right><b>" + IndFormat(deci.format(total_balance)) + "</b></td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				
			} else {
				Str.append("<font color=red><b>No Balance Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id
					+ " AND team_active = 1 "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("team_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">" + (crs.getString("team_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			String exe = "";
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'  and emp_sales = '1' AND (emp_branch_id = " + dr_branch_id + " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + ")) "
					+ ExeAccess + "";
			
			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=form-control multiple=\"multiple\" size=10 style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("emp_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">" + (crs.getString("emp_name")) + "</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return exe = Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String PopulateMonth() {
		StringBuilder Str = new StringBuilder();
		for (int i = 1, j = 0; i <= 12; i++, j++) {
			Str.append("<option value = " + doublenum(i) + "" + StrSelectdrop(doublenum(i), doublenum(Integer.parseInt(dropMonth))) + ">" + TextMonth(j) + "</option>\n");
		}
		return Str.toString();
	}
	
	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		for (int i = curryear; i <= curryear + 4; i++) {
			Str.append("<option value=" + i + "");
			Str.append(StrSelectdrop(Integer.toString(i), dropYear));
			Str.append(">" + i + "</option>\n");
		}
		return Str.toString();
	}
}
