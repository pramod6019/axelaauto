package axela.customer;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Top_Customers_Overdue extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String msg = "";
	public String emp_id = "0", branch_id = "0";
	public String[] team_ids, exe_ids;
	public String team_id = "", exe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";
	public String comp_id = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id in (" + exe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND (emp_branch_id=" + dr_branch_id + " or emp_id= 1 or emp_id in (SELECT empbr.emp_id FROM "
								+ " " + compdb(comp_id) + "axela_emp_branch empbr WHERE " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id AND "
								+ " empbr.emp_branch_id = " + dr_branch_id + "))";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id in (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id in (" + team_id + "))";
					}
					StrHTML = TopCustomerOverdue();
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

	public String TopCustomerOverdue() {
		// SOP("TopCustomerOverdue===" );
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_id , customer_name, sum(customer_curr_bal)  AS customeramount "
					+ " FROM " + compdb(comp_id) + "axela_customer "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id= customer_emp_id "
					+ " WHERE customer_active=1  AND customer_curr_bal < 0 "
					+ " " + StrSearch + BranchAccess.replace("branch_id", "customer_branch_id") + " "
					+ ExeAccess.replace("emp_id", "customer_emp_id")
					+ " GROUP BY customer_id "
					+ " ORDER BY customeramount asc "
					+ " limit 20 ";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td align=right>").append(deci.format(crs.getDouble("customeramount"))).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Top Customers Overdue Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String comp_id) {
		String stringval = "";
		try {
			StrSql = "SELECT team_id, team_name FROM " + compdb(comp_id) + "axela_sales_team "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_team_id = team_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id= teamtrans_emp_id "
					+ " WHERE 1=1 AND emp_sales=1 AND emp_active=1" + ExeAccess + ""
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("SqlStr in PopulateCountry==========" + SqlStr);
			while (crs.next()) {
				stringval = stringval + "<option value=" + crs.getString("team_id") + "";
				stringval = stringval + ArrSelectdrop(crs.getInt("team_id"), team_ids);
				stringval = stringval + ">" + crs.getString("team_name") + "</option>\n";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

	public String PopulateSalesExecutives(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' AND emp_sales='1' AND (emp_branch_id = " + branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id + ")) " + ExeAccess + "";
			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";

			}
			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			// SOP("SqlStr==== in PopulateExecutive" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\"  >");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</SELECT>");
			crs.close();
			return Str.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
