package axela.sales;
//Saiman 13th Dec 2012
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Sales_Dash extends Connect {
	
	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String comp_id = "0";
	public String ExeAccess = "";
	public String so_month = "";
	public String[] x = new String[14];
	public String so_grandtotal = "";
	public String invoice_grandtotal = "";
	public String receipt_amount = "";
	public String StrSearch = "";
	public String emp_all_exe = "";
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
					StrSearch = BranchAccess + " " + ExeAccess;
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					
					StrSearch += ExeAccess;
					
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN(" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						ListDashboard();
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
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		for (int i = 0; i < 14; i++) {
			x[i] = AddDayMonthYear(endtime, 0, 0, -i, 0);
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}
	
	protected void CheckForm() {
		msg = "";
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				end_time = strToShortDate(endtime);
				endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}
	
	public void ListDashboard() {
		StrSql = " SELECT concat(YEAR(calmonth),'-',substr(MONTHNAME(calmonth),1,3)) AS yearmonth, "
				+ " COALESCE((SELECT sum(so_grandtotal) FROM " + compdb(comp_id) + "axela_sales_so "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch  on branch_id = so_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp  on emp_id=so_emp_id "
				+ " WHERE so_active=1 AND substr(so_date,1,6)=substr(cal.calmonth,1,6) " + StrSearch + "),0) AS sales, "
				
				+ " COALESCE((SELECT sum(voucher_amount) "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch  on branch_id = voucher_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp  on emp_id = voucher_emp_id  "
				+ " WHERE 1=1"
				+ " AND voucher_active = 1"
				+ " AND voucher_authorize = 1"
				+ " AND voucher_vouchertype_id= 6"
				+ " AND substr(voucher_date,1,6)=substr(cal.calmonth,1,6)" + StrSearch + "),0) AS invoices, "
				
				+ " COALESCE((SELECT sum(voucher_amount) "
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch  on branch_id = voucher_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp  on emp_id = voucher_emp_id  "
				+ " WHERE 1=1"
				+ " AND voucher_active = 1"
				+ " AND voucher_authorize = 1"
				+ " AND voucher_vouchertype_id= 9"
				+ " AND substr(voucher_date,1,6)=substr(cal.calmonth,1,6)" + StrSearch + "),0) AS receipts "
				
				+ " FROM (";
		for (int i = 0; i < 14; i++) {
			StrSql = StrSql + " SELECT " + ConvertShortDateToStr(x[i]) + " AS calmonth ";
			if (i != x.length - 1) {
				StrSql = StrSql + " UNION ";
			}
		}
		StrSql = StrSql + " ) AS cal "
				+ " GROUP BY calmonth"
				+ " ORDER BY calmonth";
		SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				so_grandtotal = "[";
				invoice_grandtotal = "[";
				receipt_amount = "[";
				so_month = "[";
				while (crs.next()) {
					so_grandtotal = so_grandtotal + crs.getString("sales") + "";
					invoice_grandtotal = invoice_grandtotal + crs.getString("invoices") + "";
					receipt_amount = receipt_amount + crs.getString("receipts") + "";
					so_month = so_month + "'" + crs.getString("yearmonth") + "'";
					if (!crs.isLast()) {
						so_grandtotal = so_grandtotal + ",";
						invoice_grandtotal = invoice_grandtotal + ",";
						receipt_amount = receipt_amount + ",";
						so_month = so_month + ",";
					}
				}
				so_grandtotal = so_grandtotal + "]";
				invoice_grandtotal = invoice_grandtotal + "]";
				receipt_amount = receipt_amount + "]";
				so_month = so_month + "]";
			} else {
				// NoSalesPipeline = "No Open Enquiry!";
			}
			crs.close();
			
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	// public String PopulateTeam() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT team_id, team_name "
	// + " FROM " + compdb(comp_id) + "axela_sales_team "
	// + " WHERE team_branch_id=" + dr_branch_id + " "
	// + " GROUP BY team_id "
	// + " ORDER BY team_name ";
	// // SOP("PopulateTeam query ==== "+StrSql);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("team_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
	// Str.append(">").append(crs.getString("team_name")).append("</option> \n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }
	
	// public String PopulateSalesExecutives() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// String exe = "";
	// StrSql =
	// "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_team_exe on teamtrans_emp_id=emp_id"
	// + " WHERE emp_active = '1' AND emp_sales='1' AND "
	// + " (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
	// + " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) +
	// "axela_emp_branch empbr"
	// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
	// + " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
	//
	// if (!team_id.equals("")) {
	// StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
	// }
	// StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<SELECT name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
	// Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
	// }
	// Str.append("</SELECT>");
	// crs.close();
	// return exe = Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }
}
