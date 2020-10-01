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

public class Report_TotalBalance22 extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String so_month = "";
	public String[] x = new String[14];
	public String invoice_grandtotal = "";
	public String receipt_amount = "";
	public String StrSearch = "";
	public String StrHTML = "";
	DecimalFormat deci = new DecimalFormat("0.00");

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
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "invoice_branch_id") + " " + ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and invoice_branch_id =" + dr_branch_id;
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (select teamtrans_emp_id "
								+ " from " + compdb(comp_id) + "axela_sales_team_exe where teamtrans_team_id in (" + team_id + "))";
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		StrSql = "SELECT emp_id, emp_name, emp_ref_no, sum(invoice_grandtotal) as invoicetotal,"
				+ " COALESCE((SELECT sum(receipt_amount) "
				+ " FROM  " + compdb(comp_id) + "axela_invoice_receipt "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_invoice inv on inv.invoice_id=receipt_invoice_id"
				+ " where receipt_active=1 and inv.invoice_active=1 and inv.invoice_emp_id=emp_id  ),0) as receipttotal,"
				+ " (sum(invoice_grandtotal)- (COALESCE((SELECT sum(receipt_amount) "
				+ " FROM  " + compdb(comp_id) + "axela_invoice_receipt "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_invoice inv on inv.invoice_id=receipt_invoice_id"
				+ " where receipt_active=1 and inv.invoice_active=1 and inv.invoice_emp_id=emp_id  ),0))) as balance"
				+ " from " + compdb(comp_id) + "axela_emp "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_invoice on invoice_emp_id=emp_id"
				+ " where invoice_active=1" + StrSearch + " "
				+ " GROUP BY emp_id"
				+ " having balance > 0"
				+ " ORDER BY emp_name ";
		try {
			// SOP("ListDashboard StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Sales Consultant</th>\n");
				Str.append("<th>Invoices</th>\n");
				Str.append("<th>Receipts</th>\n");
				Str.append("<th>Balances</th>\n");
				Str.append("</tr>\n");
				int count = 0;
				// crs.isFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append(".</td>");
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
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " group by team_id "
					+ " order by team_name ";
			// SOP("PopulateTeam query ==== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
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
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' and emp_sales='1' and (emp_branch_id = " + dr_branch_id + " "
					+ " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id order by emp_name";
			// SOP("PopulateSalesExecutives ========"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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
}
