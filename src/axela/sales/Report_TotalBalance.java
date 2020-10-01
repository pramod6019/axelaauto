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

public class Report_TotalBalance extends Connect {
	
	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String so_month = "";
	public String comp_id = "0";
	public String[] x = new String[14];
	public String invoice_grandtotal = "";
	public String receipt_amount = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String emp_all_exe = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();
	
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
					StrSearch = BranchAccess.replace("branch_id", "voucher_branch_id");
					
					StrSearch += ExeAccess;
					
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch += " AND voucher_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_brand_id IN (" + brand_id + ")) ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND voucher_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_region_id IN (" + region_id + ")) ";
					}
					
					if (!branch_id.equals("")) {
						StrSearch = StrSearch + " AND voucher_branch_id IN(" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (select teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListDashboard();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			// if (dr_branch_id.equals("")) {
			// dr_branch_id = "0";
			// }
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		// SOP("branch id===----==" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
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
		StrSql = "SELECT emp_id, emp_name, emp_ref_no, SUM(IF(voucher_vouchertype_id = 6,voucher_amount,0)) AS invoicetotal,"
				+ " COALESCE((SELECT SUM(voucher_amount) "
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher "
				+ " WHERE 1=1"
				+ " AND voucher_vouchertype_id = 9"
				+ " AND voucher_active = 1"
				+ " AND voucher_emp_id = emp_id  ),0) AS receipttotal,"
				
				+ " (SUM(voucher_amount)- (COALESCE((SELECT SUM(voucher_amount) "
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher "
				+ " WHERE 1=1"
				+ " AND voucher_vouchertype_id = 9 "
				+ " AND voucher_active = 1"
				+ " AND voucher_emp_id = emp_id  ),0))) AS balance"
				
				+ " FROM " + compdb(comp_id) + "axela_emp "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_emp_id = emp_id"
				+ " WHERE voucher_active = 1"
				+ "	AND voucher_vouchertype_id = 6"
				+ StrSearch + " "
				+ " GROUP BY emp_id"
				+ " HAVING balance > 0"
				+ " ORDER BY emp_name ";
		try {
			SOP("ListDashboard StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
				Str.append("<th>Invoices</th>\n");
				Str.append("<th>Receipts</th>\n");
				Str.append("<th data-hide=\"phone\">Balances</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				// crs.isFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"));
					Str.append(" (").append(crs.getString("emp_ref_no")).append(")</a></td>");
					Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("invoicetotal")));
					Str.append("</td>");
					Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("receipttotal")));
					Str.append("</td>");
					Str.append("<td valign=top align=right>").append(deci.format(crs.getDouble("balance")));
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Balance Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
