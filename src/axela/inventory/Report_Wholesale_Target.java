package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import cloudify.connect.Connect;

public class Report_Wholesale_Target extends Connect {

	public String WholesaleHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String starttime = "", start_time = "";
	public String invoicestartdate = "", invoiceenddate = "";
	public String endtime = "", end_time = "";
	public String StrSql = "";
	public String BranchAccess = "", ExeAccess = "", emp_all_exe = "";
	public String msg = "";
	public String[] brand_ids, region_ids, branch_ids, model_ids, item_ids, fueltype_ids;
	public String brand_id = "", region_id = "", branch_id = "", model_id = "", item_id, fueltype_id = "";
	public String go = "";
	public String dr_branch_id = "0";
	public String vehstock_branch_id = "";
	public String delstatus_id = "";
	public int grandtotal = 0;
	public String chart_data = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access, emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			go = PadQuotes(request.getParameter("submit_button"));
			GetValues(request, response);
			CheckForm();
			if (go.equals("Go")) {

				StrSearch = BranchAccess.replace("branch_id", "wstarget_branch_id"); // + " " + ExeAccess;
				if (!brand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + region_id + ")";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND wstarget_branch_id IN (" + branch_id + ")";
				}
				if (!model_id.equals("")) {
					StrSearch += " AND wstarget_model_id IN (" + model_id + ")";
				}
				if (!item_id.equals("")) {
					StrSearch += " AND item_id IN (" + item_id + ")";
				}
				if (!fueltype_id.equals(""))
				{
					StrSearch += " AND wstarget_fueltype_id IN (" + fueltype_id + ")";
				}
				if (!starttime.equals("")) {
					StrSearch += " and substr(wstarget_month, 1,6) >= SUBSTR('" + starttime + "', 1,6)";
				}
				if (!endtime.equals("")) {
					StrSearch += " AND SUBSTR(wstarget_month, 1,6) <= SUBSTR('" + endtime + "', 1,6) ";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					WholesaleHTML = WholesaleTargetDetails(comp_id);
				}
			}
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
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		item_id = RetrunSelectArrVal(request, "dr_variant");
		item_ids = request.getParameterValues("dr_variant");
		fueltype_id = RetrunSelectArrVal(request, "dr_fueltype_id");
		fueltype_ids = request.getParameterValues("dr_fueltype_id");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String WholesaleTargetDetails(String comp_id) {
		int stockcount = 0;
		int targetcount = 0;
		int stockpurchased = 0;
		int balance = 0;
		int grandtotalbalance = 0;
		int opensocount = 0;
		int TATcount = 0;
		SOP("=======" + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)));
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT model_id, model_name, fueltype_name, "
					+ " wstarget_id, wstarget_branch_id, wstarget_month,"
					+ " SUM(wstarget_count) AS 'targetcount' ,"
					+ " COALESCE(stockcount,0) AS 'stockcount',"
					+ " COALESCE(stockpurchased,0) AS 'stockpurchased',"
					+ " COALESCE(opensocount,0) AS 'opensocount',"
					+ " COALESCE(TAT,0) AS 'TAT'"

					+ " FROM " + compdb(comp_id) + "axela_sales_target_wholesale"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = wstarget_model_id";
			if (!item_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = wstarget_fueltype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = wstarget_branch_id"
					+ " LEFT JOIN ( SELECT SUM(IF(so_retail_date IS NULL OR so_retail_date = '',1,0)) AS stockcount,"
					+ " SUM(IF(SUBSTR(vehstock_ordered_date, 1, 8) >= SUBSTR(" + starttime + ", 1, 8) "
					+ " AND SUBSTR(vehstock_ordered_date, 1, 8) <= SUBSTR(" + endtime + ", 1, 8) ,1 ,0)) AS stockpurchased,"
					+ " SUM(IF (so_retail_date = '', 1,	0)) AS opensocount,"
					+ " SUM(IF(vehstock_invoice_date != '' AND so_retail_date >" + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0))
					+ " , DATEDIFF(so_retail_date,vehstock_invoice_date),0))"
					+ " / SUM(IF(vehstock_invoice_date != '' AND so_retail_date >" + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)) + "  , 1,0)) AS TAT,"
					+ " item_model_id, item_fueltype_id "
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1"
					+ " WHERE 1 = 1"
					+ " GROUP BY item_model_id, item_fueltype_id )"
					+ " AS tblstock ON tblstock.item_model_id = wstarget_model_id"
					+ " AND tblstock.item_fueltype_id = wstarget_fueltype_id "

					+ " WHERE model_active = '1' "
					+ " AND model_sales = '1'"
					+ StrSearch
					+ " GROUP BY wstarget_model_id, wstarget_fueltype_id"
					// + ", wstarget_id"
					+ " ORDER BY model_name, fueltype_name";

			// SOPInfo("whole Sale Target--------------------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-hover table-bordered\">");
				Str.append("<thead>");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th>Fuel</th>\n");
				Str.append("<th>Target</th>\n");
				Str.append("<th>Purchased</th>\n");
				Str.append("<th>Purchased %</th>\n");
				Str.append("<th>Balance</th>\n");
				Str.append("<th>Fund Required</th>\n");
				Str.append("<th>Stock Count</th>\n");
				Str.append("<th>Open Sales Orders</th>\n");
				Str.append("<th>TAT</th>\n");

				Str.append("</tr></thead><tbody>\n");
				crs.last();
				int count = 0;
				crs.beforeFirst();
				while (crs.next()) {
					stockcount += crs.getInt("stockcount");
					targetcount += crs.getInt("targetcount");
					stockpurchased += crs.getInt("stockpurchased");
					opensocount += crs.getInt("opensocount");
					balance = crs.getInt("targetcount") - crs.getInt("stockpurchased");
					TATcount += crs.getDouble("TAT");
					grandtotalbalance = targetcount - stockpurchased;

					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("model_name") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("fueltype_name") + "</td>");
					Str.append("<td valign=top align=right><a href=../inventory/target-ws-update.jsp?update=yes&target_id=" + crs.getString("wstarget_id")
							+ "&dr_branch_id=" + crs.getString("wstarget_branch_id") + "&year=" + SplitYear(crs.getString("wstarget_month")) + "&month=" + SplitMonth(crs.getString("wstarget_month"))
							+ ">" + crs.getString("targetcount")
							+ "</a></td>");
					Str.append("<td valign=top align=right>" + crs.getString("stockpurchased") + "</td>");
					Str.append("<td valign=top align=right>" + getPercentage(crs.getInt("stockpurchased"), crs.getInt("targetcount")) + " %"
							+ "</td>");

					Str.append("<td valign=top align=right>" + balance + "</td>");
					Str.append("<td valign=top align=right>" + 0 + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("stockcount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("opensocount") + "</td>");
					Str.append("<td valign=top align=right>" + deci.format(crs.getDouble("TAT")) + "</td>");

					Str.append("</tr>\n");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td valign=top align=right colspan=3><b>Total :</b></td>");
				Str.append("<td valign=top align=right><b>" + targetcount + "</b></td>");
				Str.append("<td valign=top align=right><b>" + stockpurchased + "</b></td>");
				Str.append("<td valign=top align=right><b>" + getPercentage(stockpurchased, targetcount) + " %" + "</b></td>");
				Str.append("<td valign=top align=right><b>" + grandtotalbalance + "</b></td>");
				Str.append("<td valign=top align=right><b>" + 0 + "</b></td>");
				Str.append("<td valign=top align=right><b>" + stockcount + "</b></td>");
				Str.append("<td valign=top align=right><b>" + opensocount + "</b></td>");
				Str.append("<td valign=top align=right><b>" + TATcount / count + " %" + "</b></td>");
				SOP("stockpurchased===" + TATcount);
				SOP("count===" + count);

				Str.append("</tr>\n");
				Str.append("</tbody></table>\n");
			} else {
				Str.append("<br><br><b><font color=red>No Wholesale Details Found!</font></b><br><br>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateFuelType(String comp_id, HttpServletRequest request) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype" + " where 1=1"
					+ " order by fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_fueltype_id id=dr_fueltype_id class='form-control multiselect-dropdown' multiple=\"multiple\" style=\"padding:10px\" size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				// Str.append(StrSelectdrop(crs.getString("fueltype_id"),
				// fueltype_id));
				Str.append(ArrSelectdrop(crs.getInt("fueltype_id"), fueltype_ids));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
