package axela.sales;
//Saiman 13th Dec 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Product_Dash extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, cat_ids, item_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", cat_id = "", item_id = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String so_month = "";
	public String[] x = new String[14];
	public String so_amt = "";
	public String so_qty = "";
	public String StrSearch = "";
	public String emp_all_exe = "";
	public String amountchart_data = "", quantitychart_data = "";
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

					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
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
					if (!cat_id.equals("")) {
						StrSearch = StrSearch + " AND trans_salescat_id IN (" + cat_id + ")";
					}
					if (!item_id.equals("")) {
						StrSearch = StrSearch + " AND soitem_item_id  IN (" + item_id + ")";
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
						ListTarget();
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
		cat_id = RetrunSelectArrVal(request, "dr_salescat");
		cat_ids = request.getParameterValues("dr_salescat");
		item_id = RetrunSelectArrVal(request, "dr_item");
		item_ids = request.getParameterValues("dr_item");
	}

	protected void CheckForm() {
		msg = "";
		if (endtime.equals("")) {
			msg = msg + "<br>select End Date!<br>";
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

	public void ListTarget() {
		StrSql = " SELECT CONCAT(MONTHNAME(calmonth),'-',SUBSTR(calmonth,3,2)) AS yearmonth, "
				// StrSql =
				// " SELECT CONCAT(SUBSTR(calmonth,5,2)) AS yearmonth,  "
				+ " COALESCE((SELECT SUM(soitem_price * soitem_qty-soitem_disc * soitem_qty) "
				+ " FROM " + compdb(comp_id) + "axela_sales_so  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id=so_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id=soitem_item_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch  ON branch_id = so_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp  ON emp_id=so_emp_id  "
				+ " WHERE so_active=1 AND SUBSTR(so_date,1,6)=SUBSTR(cal.calmonth,1,6)   "
				+ StrSearch
				+ " AND (emp_id = 2  OR emp_id=11)),0) AS salesamt, "
				+ " COALESCE((SELECT SUM(soitem_qty) FROM " + compdb(comp_id) + "axela_sales_so  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id=so_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id=soitem_item_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch  ON branch_id = so_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp  ON emp_id=so_emp_id  "
				+ " WHERE so_active=1 AND SUBSTR(so_date,1,6)=SUBSTR(cal.calmonth,1,6)   "
				+ StrSearch
				+ " AND (emp_id = 2  OR emp_id=11)),0) AS salesqty  "
				+ " FROM ( ";
		for (int i = 0; i < 14; i++) {
			StrSql = StrSql + " SELECT " + ConvertShortDateToStr(x[i]) + " AS calmonth ";
			if (i != x.length - 1) {
				StrSql = StrSql + " UNION ";
			}
		}
		StrSql = StrSql + " ) AS cal"
				+ " GROUP BY calmonth"
				+ " ORDER BY calmonth";
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				amountchart_data = "[";
				quantitychart_data = "[";
				while (crs.next()) {
					so_amt = crs.getString("salesamt") + "";
					so_qty = crs.getString("salesqty") + "";
					so_month = "'" + crs.getString("yearmonth") + "'";
					amountchart_data = amountchart_data + "{'month': " + so_month + ", 'amount':'" + so_amt + "'}";
					quantitychart_data = quantitychart_data + "{'month': " + so_month + ", 'quantity':'" + so_qty + "'}";
					if (!crs.isLast()) {
						amountchart_data = amountchart_data + ",";
						quantitychart_data = quantitychart_data + ",";
					}
				}
				amountchart_data = amountchart_data + "]";
				quantitychart_data = quantitychart_data + "]";
			} else {
				// NoSalesPipeline = "No Open Enquiry!";
			}
			crs.close();
			// SOP(so_amt + "....so_amt");
			// SOP(so_qty + ".......so_qty");
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
	// Str.append("<option value=" + crs.getString("team_id") + "");
	// Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
	// Str.append(">" + (crs.getString("team_name")) + "</option> \n");
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
	// StrSql =
	// "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
	// + " WHERE emp_active = '1'  AND emp_sales='1' AND (emp_branch_id = " +
	// dr_branch_id + " or emp_id = 1"
	// + " or emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) +
	// "axela_emp_branch empbr"
	// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
	// + " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
	//
	// if (!team_id.equals("")) {
	// StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
	// }
	// StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
	// // SOP("StrSql==== IN PopulateExecutive" + StrSqlBreaker(StrSql));
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<SELECT name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id")).append("");
	// Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
	// Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
	// }
	// Str.append("</SELECT>");
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	public String PopulateSalesCat() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT salescat_id, salescat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_salescat"
					+ " WHERE 1=1";
			StrSql = StrSql + " GROUP BY salescat_id ORDER BY salescat_name";
			// SOP("StrSql==== IN PopulateExecutive" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=dr_salescat id=dr_salescat class='form-control multiselect-dropdown' multiple=\"multiple\" size=10  style=\"padding:10px\" onChange=\"ItemCheck();\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("salescat_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("salescat_id"), cat_ids));
				Str.append(">").append(crs.getString("salescat_name")).append("</option> \n");
			}
			Str.append("</SELECT>");
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, CONCAT(item_name,' (',item_code,')') AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id=item_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat ON salescat_id=trans_salescat_id "
					+ " WHERE item_active = '1'";
			if (!cat_id.equals("")) {
				StrSql = StrSql + " AND salescat_id IN (" + cat_id + ")";
			}
			StrSql = StrSql + " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<SELECT name=dr_item id=dr_item class='form-control multiselect-dropdown' multiple=\"multiple\" size=10  style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("item_id"), item_ids));
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			Str.append("</SELECT>");
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
