package axela.sales;
// saiman
// 

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Top_Customers extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";
	public int sales = autosales;
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public String NoDetails = "";
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
					StrSearch = BranchAccess + ExeAccess;

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id in (SELECT teamtrans_emp_id FROM  " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id in (" + team_id + "))";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		// SOP("branch id===----==" + branch_id);
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
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
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListDashboard() {
		StringBuilder Str = new StringBuilder();
		try {
			// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			// Str.append("<tr align=center>\n");
			StrSql = "SELECT customer_id , customer_name, sum(so_grandtotal) AS sales"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_customer_id=customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id= so_emp_id"
					+ " WHERE customer_active=1  AND "
					+ " SUBSTR(so_date,1,8) >= SUBSTR('" + starttime + "',1,8) AND SUBSTR(so_date,1,8) <= SUBSTR('" + endtime + "',1,8) "
					+ " " + StrSearch
					+ " GROUP BY customer_id"
					+ " ORDER BY sales "
					+ " DESC LIMIT 100";
			// SOP("Sales table----------------- " + StrSql);
			Str.append("<div class=\"col-md-4 col-sm-4\">\n");
			Str.append("<div class=\"portlet box\">"
					+ "<div class=\"portlet-title\" style=\"text-align:center\"><div class=\"caption\" style=\"float: none;\"><b>Sales</b></div></div>"
					+ "<div class=\"portlet-body portlet-empty\">"
					+ "<div class=\"tab-pane\" id=\"\">");
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<td valign=top>");
				// Start of sales order Segment
				// Str.append("<label class=\"control-label\">Sales</label>");

				// Str.append("<div class=\"col-md-4 col-sm-4\">");
				// Str.append("<b>Sales</b></div>");
				Str.append("<div class=\"table\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Sales</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td align=right>").append(deci.format(crs.getDouble("sales"))).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				// Str.append("</td>\n");
			} else {
				Str.append("<center><br><br><font color=red><b>No Sales Details Found!</b></font><br><br></center>");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
			}

			crs.close();
			// End of sales order Segment
			// Start of INvoice Segment
			StrSql = "SELECT customer_id , customer_name, sum(voucher_amount) AS invoices"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id= voucher_emp_id"
					+ " WHERE customer_active=1 AND "
					+ " SUBSTR(voucher_date,1,8) >= SUBSTR('" + starttime + "',1,8) AND SUBSTR(voucher_date,1,8) < SUBSTR('" + endtime + "',1,8) "
					+ " AND vouchertype_id = 6 AND vouchertype_voucherclass_id = 6 "
					+ " " + StrSearch
					+ " GROUP BY customer_id"
					+ " ORDER BY invoices"
					+ " DESC";

			// SOP("Invoice table ===" + StrSql);
			crs = processQuery(StrSql, 0);
			Str.append("<div class=\"col-md-4 col-sm-4\">\n");
			Str.append("<div class=\"portlet box\">"
					+ "<div class=\"portlet-title\" style=\"text-align:center\"><div class=\"caption\" style=\"float: none;\"><b>Invoices</b></div></div>"
					+ "<div class=\"portlet-body portlet-empty\">"
					+ "<div class=\"tab-pane\" id=\"\">");
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<td valign=top>");
				// Str.append("<div class=\"col-md-4 col-sm-4\">");
				// Str.append("<b>Invoices</b></div>");

				Str.append("<div class=\"table\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Invoices</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>" + count + "</td>\n");
					Str.append("<td><a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + ">" + crs.getString("customer_name") + "</a></td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("invoices")) + "</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<center><br><br><font color=red><b>No Invoice Details Found!</b></font><br><br></center>");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				// Str.append(NoDetails);
			}

			crs.close();
			// End of Invoice Segment

			// Start of Receipt Segment
			StrSql = "SELECT customer_id, customer_name, sum(voucher_amount) AS receipts"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id= voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id= voucher_emp_id"
					+ " WHERE customer_active=1 AND "
					+ " SUBSTR(voucher_date,1,8) >= SUBSTR('" + starttime + "',1,8) AND SUBSTR(voucher_date,1,8) < SUBSTR('" + endtime + "',1,8) "
					+ " AND vouchertype_id = 9 AND vouchertype_voucherclass_id = 9 "
					+ " " + StrSearch
					+ " GROUP BY customer_id"
					+ " ORDER BY receipts "
					+ " DESC";
			// SOP("Receipt table = " + StrSql);
			crs = processQuery(StrSql, 0);
			Str.append("<div class=\"col-md-4 col-sm-4\">\n");
			Str.append("<div class=\"portlet box\">"
					+ "<div class=\"portlet-title\" style=\"text-align:center\"><div class=\"caption\" style=\"float: none;\"><b>Receipt</b></div></div>"
					+ "<div class=\"portlet-body portlet-empty\">"
					+ "<div class=\"tab-pane\" id=\"\">");
			if (crs.isBeforeFirst()) {
				int count = 0;
				// Str.append("<td valign=top>");
				// Str.append("<div class=\"col-md-4 col-sm-4\">");
				// Str.append("<b>Receipts</b></div>");

				Str.append("<div class=\"table\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Receipts</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>" + count + "</td>\n");
					Str.append("<td><a href=../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + ">" + crs.getString("customer_name") + "</a></td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("receipts")) + "</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				// Str.append("</td>\n");

			} else {
				Str.append("<center><br><br><font color=red><b>No Receipt Details Found!</b></font><br><br></center>");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
			}

			// crs.close();
			// if (NoDetails.equals("1")) {
			// Str.append("<center><br><br><br><br><font color=red><b>No Details Found!</b></font><br><br></center>");
			// }
			// End of Receipt Segment
			Str.append("</tr>\n");
			Str.append("</table>\n");
		} catch (Exception ex) {
			SOPError("Terracle== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateTeam() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT team_id, team_name "
	// + " INNER " + compdb(comp_id) + "axela_sales_team "
	// + " WHERE team_branch_id=" + dr_branch_id + " "
	// + " group by team_id "
	// + " order by team_name ";
	// // SOP("PopulateTeam query ==== "+StrSql);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=" + crs.getString("team_id") + "");
	// Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
	// Str.append(">" + (crs.getString("team_name")) + "</option> \n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateSalesExecutives() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql =
	// "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
	// + " FROM  " + compdb(comp_id) + "axela_emp"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
	// + " WHERE emp_active = '1' AND emp_sales='1' AND (emp_branch_id = " +
	// dr_branch_id + " or emp_id = 1"
	// + " or emp_id in (SELECT empbr.emp_id INNER " + compdb(comp_id) +
	// "axela_emp_branch empbr"
	// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
	// + " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
	// if (!team_id.equals("")) {
	// StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
	//
	// }
	// StrSql = StrSql + " group by emp_id order by emp_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<SELECT name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
	// while (crs.next()) {
	// Str.append("<option value=" + crs.getString("emp_id") + "");
	// Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
	// Str.append(">" + (crs.getString("emp_name")) + "</option> \n");
	// }
	// Str.append("</SELECT>");
	// crs.close();
	// return Str.toString();
	//
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }
}
