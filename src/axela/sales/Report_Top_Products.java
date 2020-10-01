package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Top_Products extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, brand_ids, branch_ids, region_ids;
	public String comp_id = "0";
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", enquiry_Model = "", item_Model = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";
	public String ExeAccess = "";
	public String NoDetails = "";
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
					StrSearch = BranchAccess + ExeAccess;

					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id in (" + exe_id + ")";
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
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id in (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id in (" + team_id + "))";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListTarget();
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

	public String ListTarget() {
		int qty = 0, price = 0, disc = 0, amount = 0;
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
			Str.append("<table class=\"table table-responsive table-bordered\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<tr align=center>\n");
			StrSql = "SELECT  item_id, item_name, item_code, SUM(soitem_qty) AS qty ,"
					+ " SUM(soitem_price * soitem_qty) AS price, SUM(soitem_disc * soitem_qty) AS disc,"
					+ " SUM(soitem_price * soitem_qty-soitem_disc * soitem_qty) AS total"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_item_id=item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so on so_id=soitem_so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id= so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id= so_emp_id"
					+ " WHERE 1=1 AND so_active=1 AND "
					+ " SUBSTR(so_date,1,8)>=SUBSTR('" + starttime + "',1,8) AND SUBSTR(so_date,1,8)<=SUBSTR('" + endtime + "',1,8) "
					+ " " + StrSearch
					+ " GROUP BY item_id"
					+ " ORDER BY total"
					+ " DESC LIMIT  25";
			// SOP("StrSql in list=====4444=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Item ID</th>\n");
				Str.append("<th data-toggle=\"true\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Qunatity</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Price</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Discount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					qty = qty + (int) crs.getDouble("qty");
					price = price + (int) crs.getDouble("price");
					disc = disc + (int) crs.getDouble("disc");
					amount = amount + (int) crs.getDouble("total");
					Str.append("<tr>\n");
					Str.append("<td align=center>" + count + "</td>\n");
					Str.append("<td align=center>" + crs.getString("item_id") + "</td>\n");
					Str.append("<td><a href=../inventory/inventory-item-list.jsp?item_id=" + crs.getString("item_id") + ">" + item_name + "</a></td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("qty")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("price")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("disc")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("total")) + "</td>\n");
					Str.append("</tr>\n");

				}
				Str.append("<tr align=center>\n");
				Str.append("<td  colspan=3 align=right><b>Total:</b></td>\n");
				Str.append("<td  align=right>" + deci.format(qty) + "</td>\n");
				Str.append("<td  align=right>" + deci.format(price) + "</td>\n");
				Str.append("<td  align=right>" + deci.format(disc) + "</td>\n");
				Str.append("<td  align=right>" + deci.format(amount) + "</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				// NoDetails = "0";
			} else {
				Str.append("<font color=red><b>No Records Found!</b></font>");
			}
			crs.close();
			// invoice block
			// StrSql =
			// "SELECT  item_id, item_name, item_code, SUM(invoiceitem_qty) AS invqty ,"
			// +
			// " SUM(invoiceitem_price * invoiceitem_qty) AS invprice, SUM(invoiceitem_disc * invoiceitem_qty) AS invdisc,"
			// +
			// " SUM(invoiceitem_price * invoiceitem_qty-invoiceitem_disc * invoiceitem_qty) AS invtotal"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item "
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_invoice_item on invoiceitem_item_id=item_id"
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_invoice on invoice_id=invoiceitem_invoice_id"
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_branch on branch_id= invoice_branch_id"
			// + " INNER JOIN " + compdb(comp_id) +
			// "axela_emp on emp_id= invoice_emp_id"
			// + " WHERE 1=1 AND invoice_active=1 AND "
			// + " SUBSTR(invoice_date,1,8) >= SUBSTR('" + starttime +
			// "',1,8) AND SUBSTR(invoice_date,1,8) <= SUBSTR('" + endtime +
			// "',1,8) "
			// + "" + StrSearch
			// + " GROUP BY item_id"
			// + " ORDER BY invtotal"
			// + " DESC;";
			// // SOP("StrSql in list==========" + StrSql);
			// rs = processQuery(StrSql, 0);
			// if (crs.isBeforeFirst()) {
			// int count = 0;
			// Str.append("<td valign=top>");
			// Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			// Str.append("<tr align=center>\n");
			// Str.append("<th>#</th>\n");
			// Str.append("<th>Item ID</th>\n");
			// Str.append("<th>Item</th>\n");
			// Str.append("<th>Qunatity</th>\n");
			// Str.append("<th>Price</th>\n");
			// Str.append("<th>Discount</th>\n");
			// Str.append("<th>Invoice Amount</th>\n");
			// Str.append("</tr>\n");
			// while (crs.next()) {
			// count++;
			// String item_name = crs.getString("item_name");
			// if (!crs.getString("item_code").equals("")) {
			// item_name = item_name + " (" + crs.getString("item_code") + ")";
			// }
			// qty = qty + crs.getInt("invqty");
			// price = price + crs.getInt("invprice");
			// disc = disc + crs.getInt("invdisc");
			// amount = amount + crs.getInt("invtotal");
			// Str.append("<tr>\n");
			// Str.append("<td align=center>" + count + "</td>\n");
			// Str.append("<td align=center>" + crs.getString("item_id") +
			// "</td>\n");
			// Str.append("<td><a href=../inventory/inventory-item-list.jsp?item_id="
			// + crs.getString("item_id") + ">" + item_name + "</a></td>\n");
			// Str.append("<td align=right>" +
			// deci.format(crs.getDouble("invqty")) + "</td>\n");
			// Str.append("<td align=right>" +
			// deci.format(crs.getDouble("invprice")) + "</td>\n");
			// Str.append("<td align=right>" +
			// deci.format(crs.getDouble("invdisc")) + "</td>\n");
			// Str.append("<td align=right>" +
			// deci.format(crs.getDouble("invtotal")) + "</td>\n");
			// Str.append("</tr>\n");
			//
			// }
			// Str.append("<tr align=center>\n");
			// Str.append("<td  colspan=3 align=right><b>Total:</b></td>\n");
			// Str.append("<td  align=right>" + deci.format(qty) + "</td>\n");
			// Str.append("<td  align=right>" + deci.format(price) + "</td>\n");
			// Str.append("<td  align=right>" + deci.format(disc) + "</td>\n");
			// Str.append("<td  align=right>" + deci.format(amount) +
			// "</td>\n");
			// Str.append("</tr>\n");
			// Str.append("</table>\n");
			// Str.append("</td>\n");
			// // NoDetails = "0";
			// } else {
			// NoDetails = "1";
			// }
			// if (NoDetails.equals("1")) {
			// Str.append("<font color=red><b>No Items Found!</b></font>");
			// }
			// crs.close();
			Str.append("</tr>\n");
			Str.append("</table>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateTeam() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT team_id, team_name "
	// + " FROM " + compdb(comp_id) + "axela_sales_team "
	// + " WHERE team_branch_id=" + dr_branch_id + " "
	// + " group by team_id "
	// + " order by team_name ";
	// // SOP("PopulateTeam query ==== " + StrSql);
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
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_team_exe on teamtrans_emp_id=emp_id"
	// + " WHERE emp_active = '1' AND emp_sales='1' AND (emp_branch_id = " +
	// dr_branch_id + " or emp_id = 1"
	// + " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) +
	// "axela_emp_branch empbr"
	// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
	// + " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";
	// if (!team_id.equals("")) {
	// StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
	//
	// }
	// StrSql = StrSql + " group by emp_id order by emp_name";
	// // SOP("StrSql==== in PopulateExecutive" + StrSqlBreaker(StrSql));
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
