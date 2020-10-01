package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import cloudify.connect.Connect;

public class Report_Vehicle_Preowned_Stock_Ageing extends Connect {

	public String AgeingHTML = "", no_Ageingdata = "", WholesaleHTML = "", ThismonthsaleHTML = "", PrevmonthsaleHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String StrSql = "";
	public String BranchAccess = "", ExeAccess = "", emp_all_exe = "";
	public String msg = "";
	public String[] brand_ids, region_ids, branch_ids, preownedmodel_ids, carmanuf_ids;
	public String brand_id = "", region_id = "", branch_id = "", preownedmodel_id = "", models = "", carmanuf_id;
	public String dr_groupby = "0";
	public String go = "";
	public String dr_branch_id = "0";
	public String targetstarttime = "";
	public String targetendtime = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String preowned_branch_id = "";
	public String delstatus_id = "";
	public int grandtotal = 0;
	public String chart_data = "", filter = "";
	DecimalFormat deci = new DecimalFormat("##.##");

	public MIS_Check1 mischeck = new MIS_Check1();
	public String SearchURL = "report-vehicle-preowned-stock-ageing.jsp?";

	LinkedHashMap<String, Integer> stockageingdays = new LinkedHashMap<String, Integer>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			filter = PadQuotes(request.getParameter("filter"));
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			go = PadQuotes(request.getParameter("submit_button"));
			dr_groupby = PadQuotes(request.getParameter("dr_groupby"));
			if (filter.equals("yes")) {
				StockAgeingDetails(request, response);
			}
			GetValues(request, response);
			CheckForm();

			if (go.equals("")) {
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				msg = "";
			}

			if (go.equals("Go")) {

				StrSearch = BranchAccess.replace("branch_id", "preowned_branch_id");
				if (!brand_id.equals("")) {
					StrSearch += " AND brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND preowned_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_region_id IN (" + region_id + "))";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND preowned_branch_id IN (" + branch_id + ")";
				}
				if (!preownedmodel_id.equals("")) {
					StrSearch += " AND preownedmodel_id IN (" + preownedmodel_id + ")";
				}
				if (!carmanuf_id.equals("")) {
					StrSearch += " AND carmanuf_id IN (" + carmanuf_id + ")";
				}

				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}

				if (msg.equals("")) {
					AgeingHTML = StockAgeing(comp_id);
					if (AgeingHTML.equals("")) {
						no_Ageingdata = ("<center><b><font color=red>No Ageing Details Found!</font></b></center>");
					}
				}
				// // SOP("AgeingHTML----------" + AgeingHTML);
			}
		}

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		preownedmodel_id = RetrunSelectArrVal(request, "dr_model");
		models = preownedmodel_id;
		preownedmodel_ids = request.getParameterValues("dr_model");
		carmanuf_id = RetrunSelectArrVal(request, "dr_manufacturer");
		carmanuf_ids = request.getParameterValues("dr_manufacturer");
		dr_groupby = PadQuotes(request.getParameter("dr_groupby"));
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				end_time = strToShortDate(endtime);
				if (!starttime.equals("") && !endtime.equals("")
						&& Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			}
		}
		// targetendtime = endtime.substring(0, 6) + "31000000";
		// end_time = strToShortDate(endtime);
		// // endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
		// // 0, 0));
		//
		// } else {
		// msg = msg + "<br>Enter Valid End Date!";
		// endtime = "";
		// }
		// }
	}
	public String StockAgeing(String comp_id) {
		int total_0_15days = 0;
		int total_16_30dys = 0;
		int total_31_60days = 0;
		int total_61_90days = 0;
		int total_91days = 0;
		double total_tat = 0;
		double total_invamt_0_15days = 0;
		double total_invamt_16_30dys = 0;
		double total_invamt_31_60days = 0;
		double total_invamt_61_90days = 0;
		double total_invamt_91days = 0;
		StringBuilder Str = new StringBuilder();

		try {
			String now = ToLongDate(kknow()).substring(0, 8) + "000000";
			String strqwert1 = " DATEDIFF('" + starttime + "', preownedstock_date) >= qwerty1";
			String strqwert2 = " AND DATEDIFF('" + starttime + "', preownedstock_date) <= qwerty2";
			String strqwert3 = " AND sold.so_preownedstock_id IS NULL";
			String strstockdays = " COALESCE(COUNT(DISTINCT CASE WHEN "
					+ strqwert1 + strqwert2 + strqwert3 + " THEN preownedstock_id END),0)";
			String strinvoiceamt = " COALESCE(SUM(CASE WHEN "
					+ strqwert1 + strqwert2 + strqwert3 + " THEN (preownedstock_purchase_amt+preownedstock_refurbish_amt) END) ,0)";
			// SOP("strinvoiceamt-----------" + strinvoiceamt);

			StrSql = "SELECT "
					+ " carmanuf_id,"
					+ " preownedstock_id,"
					+ " carmanuf_name,"
					+ " preownedmodel_id,"
					+ " preownedmodel_name,"
					+ " brand_name,"
					+ " brand_id,";

			// 0-15days
			StrSql += strstockdays.replace("qwerty1", "0").replace("qwerty2", "15") + " AS '0-15days',"
					// 16-30days
					+ strstockdays.replace("qwerty1", "16").replace("qwerty2", "30") + " AS '16-30days',"
					// 31-60days
					+ strstockdays.replace("qwerty1", "31").replace("qwerty2", "60") + " AS '31-60days',"
					// 61-90days
					+ strstockdays.replace("qwerty1", "60").replace("qwerty2", "90") + " AS '61-90days',"
					// 91days
					+ strstockdays.replace("qwerty1", "91").replace(strqwert2, "")
					+ " AS '91days',"
					// Stock_invoice_amt
					+ " COALESCE ( SUM( CASE WHEN preownedstock_date >= '" + starttime + "' THEN (preownedstock_purchase_amt+preownedstock_refurbish_amt) END ), 0 ) AS 'purchasedamt',"
					// 0-15days
					+ strinvoiceamt.replace("qwerty1", "0").replace("qwerty2", "15") + " AS '0-15daysamt',"
					// 16-30days
					+ strinvoiceamt.replace("qwerty1", "16").replace("qwerty2", "30") + " AS '16-30daysamt',"
					// 31-60days
					+ strinvoiceamt.replace("qwerty1", "31").replace("qwerty2", "60") + " AS '31-60daysamt',"
					// 61-90days
					+ strinvoiceamt.replace("qwerty1", "60").replace("qwerty2", "90") + " AS '61-90daysamt',"
					// 91days
					+ strinvoiceamt.replace("qwerty1", "91").replace(strqwert2, "") + " AS '91daysamt', "
					// For OpenStock
					+ " COALESCE (SUM(IF(soopen.so_retail_date = ''"
					+ " AND soopen.so_retail_date <= " + starttime + ", 1, 0)),0) AS allocated, "
					// + " COALESCE (SUM(IF(soopen.so_retail_date != ''"
					// + " AND soopen.so_retail_date <= " + starttime + ", 1, 0)),0) AS notallocated, "
					+ " COALESCE (SUM(IF (preownedstock_date != '' "
					+ " AND soopen.so_retail_date >  " + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)) + ","
					+ " DATEDIFF(soopen.so_retail_date,	preownedstock_date	), 0)) / SUM(IF(preownedstock_date != ''"
					+ " AND soopen.so_retail_date > " + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)) + ", 1, 0)),	0) AS TAT"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";

			// so left join
			StrSql += " LEFT JOIN (SELECT so_preownedstock_id, so_retail_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE 1=1"
					+ " AND so_retail_date != ''"
					+ " AND so_retail_date <= " + starttime
					+ " AND so_active = 1 ) AS sold ON sold.so_preownedstock_id = preownedstock_id"
					// left join for tat, allocated and notallocated
					+ " LEFT JOIN (SELECT so_preownedstock_id, so_retail_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE 1=1"
					+ " AND so_preownedstock_id != 0"
					+ " AND so_active = 1 ) AS soopen ON soopen.so_preownedstock_id = preownedstock_id";

			StrSql += " WHERE 1 = 1 "
					+ StrSearch;

			StrSql += " GROUP BY " + dr_groupby;

			StrSql += " ORDER BY";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " brand_name";
			} else if (dr_groupby.equals("carmanuf_id")) {
				StrSql += " carmanuf_name";
			} else if (dr_groupby.equals("preownedmodel_id")) {
				StrSql += " preownedmodel_name";
			}

			// SOPInfo("preownedageing---------/----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			StringBuilder singleSelect = null;
			StringBuilder multiSelect = new StringBuilder();
			multiSelect.append("&starttime=").append(starttime)
					.append("&endtime=").append(endtime)
					.append("&brand_ids=").append(brand_id)
					.append("&region_ids=").append(region_id)
					.append("&branch_ids=").append(branch_id)
					.append("&model_ids=").append(preownedmodel_id)
					.append("&carmanuf_ids=").append(carmanuf_id)
					.append(" target=_blank");
			if (crs.isBeforeFirst()) {

				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				if (dr_groupby.equals("preownedmodel_id")) {
					Str.append("<th data-toggle=\"true\">Model</th>\n");
				}
				if (dr_groupby.equals("brand_id")) {
					Str.append("<th data-toggle=\"true\">Brand</th>\n");
				}
				if (dr_groupby.equals("carmanuf_id")) {
					Str.append("<th data-toggle=\"true\">Manufacturer</th>\n");
				}
				Str.append("<th>0-15 Days</th>\n");
				Str.append("<th data-hide=\"phone\">16-30 Days</th>\n");
				Str.append("<th data-hide=\"phone\">30-60 Days</th>\n");
				Str.append("<th data-hide=\"phone\">61-90 Days</th>\n");
				Str.append("<th data-hide=\"phone\">91 - > Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
				Str.append("<th data-hide=\"phone\">TAT</th>\n");
				Str.append("</tr></thead>\n");
				crs.last();
				int rowcount = crs.getRow();
				double counttotalamt = 0;
				int count = 0, total = 0;

				crs.beforeFirst();
				while (crs.next()) {
					singleSelect = new StringBuilder();
					total_0_15days += crs.getInt("0-15days");
					total_16_30dys += crs.getInt("16-30days");
					total_31_60days += crs.getInt("31-60days");
					total_61_90days += crs.getInt("61-90days");
					total_91days += crs.getInt("91days");
					total_tat += crs.getDouble("TAT");

					total_invamt_0_15days += crs.getDouble("0-15daysamt");
					total_invamt_16_30dys += crs.getDouble("16-30daysamt");
					total_invamt_31_60days += crs.getDouble("31-60daysamt");
					total_invamt_61_90days += crs.getDouble("61-90daysamt");
					total_invamt_91days += crs.getDouble("91daysamt");

					total = crs.getInt("0-15days") + crs.getInt("16-30days") + crs.getInt("31-60days")
							+ crs.getInt("61-90days") + crs.getInt("91days");
					// // SOP("total=====" + total);
					count++;
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>");
					if (dr_groupby.equals("preownedmodel_id")) {
						Str.append(crs.getString("preownedmodel_name"));
					}
					if (dr_groupby.equals("brand_id")) {
						Str.append(crs.getString("brand_name"));
					}
					if (dr_groupby.equals("carmanuf_id")) {
						Str.append(crs.getString("carmanuf_name"));
					}

					Str.append("</td>");

					// Group BY Models
					if (dr_groupby.equals("preownedmodel_id")) {

						singleSelect.append("&preownedmodel_id=" + crs.getString("preownedmodel_id"));

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days15=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("0-15days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days30=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("16-30days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days60=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("31-60days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days90=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("61-90days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days91=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("91days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&preownedmodel=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(total).append("</td>");

						Str.append("<td valign=top align=right>").append(crs.getString("TAT")).append("</td>");

						Str.append("</tr>\n");
						grandtotal += total;
					}

					// Group BY Manuf
					if (dr_groupby.equals("carmanuf_id")) {

						singleSelect.append("carmanuf_id=" + crs.getString("carmanuf_id"));

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&days15=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("0-15days")).append("</a></td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&days30=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("16-30days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&days60=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("31-60days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&days90=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("61-90days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&days91=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("91days")).append("</td>");

						Str.append("<td valign=top align=right><a href=");
						Str.append(SearchURL).append("filter=yes&carmanuf=yes&").append(singleSelect).append(multiSelect + ">");
						Str.append(total).append("</td>");

						Str.append("<td valign=top align=right>").append(crs.getString("TAT")).append("</td>");

						Str.append("</tr>\n");
						grandtotal += total;
					}

					// Group BY Brands
					if (dr_groupby.equals("brand_id")) {

						singleSelect.append("brand_id=" + crs.getString("brand_id"));

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days15=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("0-15days")).append("</td>");

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days30=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("16-30days")).append("</td>");

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days60=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("31-60days")).append("</td>");

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days90=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("61-90days")).append("</td>");

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days91=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("91days")).append("</td>");

						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(total).append("</td>");

						Str.append("<td valign=top align=right>").append(crs.getString("TAT")).append("</td>");

						Str.append("</tr>\n");
						grandtotal += total;
					}
				}
				counttotalamt = total_invamt_0_15days + total_invamt_16_30dys + total_invamt_31_60days
						+ total_invamt_61_90days + total_invamt_91days;
				Str.append("<tr align=center>\n");

				// For Total
				// Group BY Models
				if (dr_groupby.equals("preownedmodel_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days15=yes&").append(multiSelect + ">");
					Str.append(total_0_15days).append("</td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days30=yes&").append(multiSelect + ">");
					Str.append(total_16_30dys).append("</td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days60=yes&").append(multiSelect + ">");
					Str.append(total_31_60days).append("</td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days90=yes&").append(multiSelect + ">");
					Str.append(total_61_90days).append("</td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&days91=yes&").append(multiSelect + ">");
					Str.append(total_91days).append("</td>");

					Str.append("<td valign=top align=right><a href=");
					Str.append(SearchURL).append("filter=yes&preownedmodel=yes&").append(multiSelect + ">");
					Str.append(grandtotal).append("</td>");

					Str.append("<td valign=top align=right></td>");

					// Str.append("<td valign=top align=right>").append(unescapehtml(IndDecimalFormat(deci.format((total_tat / count)))))
					// .append("</td>");

				}

				// Group BY Manufacturer
				if (dr_groupby.equals("carmanuf_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&days15=yes&").append(multiSelect + ">");
					Str.append(total_0_15days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&days30=yes&").append(multiSelect + ">");
					Str.append(total_16_30dys).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&days60=yes&").append(multiSelect + ">");
					Str.append(total_31_60days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&days90=yes&").append(multiSelect + ">");
					Str.append(total_61_90days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&days91=yes&").append(multiSelect + ">");
					Str.append(total_91days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&carmanuf=yes&").append(multiSelect + ">");
					Str.append(grandtotal).append("</td>");

					Str.append("<td valign=top align=right></td>");

					// Str.append("<td valign=top align=right>").append(unescapehtml(IndDecimalFormat(deci.format((total_tat / count)))))
					// .append("</td>");
				}

				if (dr_groupby.equals("brand_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&days15=yes&").append(multiSelect + ">");
					Str.append(total_0_15days).append("</a></td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&days30=yes&").append(multiSelect + ">");
					Str.append(total_16_30dys).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&days60=yes&").append(multiSelect + ">");
					Str.append(total_31_60days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&days90=yes&").append(multiSelect + ">");
					Str.append(total_61_90days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&days91=yes&").append(multiSelect + ">");
					Str.append(total_91days).append("</td>");

					Str.append("<td valign=top align=right><a href=").append(SearchURL);
					Str.append("filter=yes&brand=yes&").append(multiSelect + ">");
					Str.append(grandtotal).append("</td>");

					Str.append("<td valign=top align=right></td>");

				}

				Str.append("</tr>\n");
				Str.append("<tr align=center>\n");

				// Group BY Models
				if (dr_groupby.equals("preownedmodel_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total Value : </b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_0_15days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_16_30dys)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_31_60days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_61_90days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_91days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>" + unescapehtml(IndDecimalFormat(deci.format((counttotalamt)))) + "</b></td>");
					Str.append("<td valign=top align=right></td>");
				}

				// Group BY Brands
				if (dr_groupby.equals("brand_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total Value : </b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_0_15days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_16_30dys)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_31_60days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_61_90days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_91days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>" + unescapehtml(IndDecimalFormat(deci.format((counttotalamt)))) + "</b></td>");
					Str.append("<td valign=top align=right></td>");

				}

				if (dr_groupby.equals("carmanuf_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total Value : </b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_0_15days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_16_30dys)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_31_60days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_61_90days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_91days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>" + unescapehtml(IndDecimalFormat(deci.format((counttotalamt)))) + "</b></td>");
					Str.append("<td valign=top align=right></td>");
				}

				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				stockageingdays.put("0-15 Days", total_0_15days);
				stockageingdays.put("16-30 Days", total_16_30dys);
				stockageingdays.put("31-60 Days", total_31_60days);
				stockageingdays.put("60-90 Days", total_61_90days);
				stockageingdays.put("91 Days", total_91days);
				PrepareStockAgeingChart();
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

	public String PrepareStockAgeingChart() {
		// Set set = stockageingdays.entrySet();
		// Iterator i = set.iterator();
		// chart_data = "[";
		// while (i.hasNext()) {
		// Map.Entry m = (Map.Entry) i.next();
		// chart_data = chart_data + "{'type': '" + m + "', 'total':" + stockageingdays.get(m) + "}";
		// chart_data = chart_data + ",";
		// }
		// // SOP("chart_data-----------" + chart_data);
		// chart_data = chart_data + "]";
		// return "";

		// 3rd one is for transit
		String color[] = {"#b0de0f", "#fcd202", "#ff9e01", "#ff6600", "#ff0f00"};

		chart_data = "[";
		int i = 0;
		for (String str : stockageingdays.keySet()) {

			chart_data = chart_data + "{'type': '" + str + "','color':'" + color[i] + "', 'total':" + stockageingdays.get(str) + "}";
			// chart_data_total = chart_data_total + stockageingdays.get(str);
			i++;
			chart_data = chart_data + ",";

		}
		chart_data = chart_data + "]";
		// // SOP("chart_data-----------" + chart_data);
		return "";

	}

	public String PopulateGroupBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=brand_id").append(StrSelectdrop("brand_id", dr_groupby)).append(">Brands</option>\n");
		Str.append("<option value=carmanuf_id").append(StrSelectdrop("carmanuf_id", dr_groupby)).append(">Manufacturer</option>\n");
		Str.append("<option value=preownedmodel_id").append(StrSelectdrop("preownedmodel_id", dr_groupby)).append(">Models</option>\n");
		return Str.toString();
	}

	private void StockAgeingDetails(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String carmanuf = PadQuotes(request.getParameter("carmanuf"));
			String preownedmodel = PadQuotes(request.getParameter("preownedmodel"));
			String brand = PadQuotes(request.getParameter("brand"));
			String brand_id = CNumeric(PadQuotes(RetrunSelectArrVal(request, "brand_id")));
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_ids"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_ids"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_ids"));
			String preownedmodel_id = CNumeric(PadQuotes(request.getParameter("preownedmodel_id")));
			String preownedmodel_ids = PadQuotes(RetrunSelectArrVal(request, "preownedmodel_ids"));
			String carmanuf_id = CNumeric(PadQuotes(request.getParameter("carmanuf_id")));
			String carmanuf_ids = PadQuotes(RetrunSelectArrVal(request, "carmanuf_ids"));
			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String days15 = PadQuotes(request.getParameter("days15"));
			String days30 = PadQuotes(request.getParameter("days30"));
			String days60 = PadQuotes(request.getParameter("days60"));
			String days90 = PadQuotes(request.getParameter("days90"));
			String days91 = PadQuotes(request.getParameter("days91"));
			String total = PadQuotes(request.getParameter("total"));
			String grandtotal = PadQuotes(request.getParameter("grandtotal"));
			String curr_Date = ToLongDate(kknow());

			// SOP("brand_ids===" + brand_ids);
			// SOP("region_ids===" + region_ids);
			// SOP("branch_ids===" + branch_ids);
			// SOP("preownedmodel_id===" + preownedmodel_id);
			// SOP("preownedmodel_ids===" + preownedmodel_ids);
			// SOP("carmanuf_id===" + carmanuf_id);
			// SOP("carmanuf_ids===" + carmanuf_ids);
			// SOP("starttime===" + starttime);
			// SOP("endtime===" + endtime);

			StrSearch = BranchAccess.replace("branch_id", "preowned_branch_id"); // + " " + ExeAccess;
			// For multi-select
			// Brands
			if (!brand_id.equals("0")) {
				StrSearch += " AND branch_brand_id = " + brand_id + " ";
			}
			if (!brand_ids.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrSearch += " AND preowned_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_region_id IN (" + region_ids + "))";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrSearch += " AND preowned_branch_id IN (" + branch_ids + ")";
			}
			// Models
			if (!preownedmodel_id.equals("0")) {
				StrSearch += " AND preownedmodel_id =" + preownedmodel_id;
			}
			if (!preownedmodel_ids.equals("")) {
				StrSearch += " AND preownedmodel_id IN (" + preownedmodel_ids + ")";
			}
			// Manufacturer
			if (!carmanuf_id.equals("0")) {
				StrSearch += " AND carmanuf_id =" + carmanuf_id;
			}
			if (!carmanuf_ids.equals("")) {
				StrSearch += " AND carmanuf_id IN (" + carmanuf_ids + ")";
			}

			// Group BY Models
			if (preownedmodel.equals("yes")) {
				StrFilter += StrSearch;
				if (days15.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 0"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 15 ";

				}
				if (days30.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 16"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 30 ";
				}
				if (days60.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 31"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 60 ";
				}
				if (days90.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 61"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 90 ";
				}
				if (days91.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 91 ";
				}

				StrFilter += " AND (so_preownedstock_id IS NULL OR so_retail_date = '')";

			}

			// Group By Manufacturer
			if (carmanuf.equals("yes")) {
				StrFilter += StrSearch;
				if (days15.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 0"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 15 ";
				}
				if (days30.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 16"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 30 ";
				}
				if (days60.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 31"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 60 ";
				}
				if (days90.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 61"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 90 ";
				}
				if (days91.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 91 ";
				}
				StrFilter += " AND (so_preownedstock_id IS NULL OR so_retail_date = '')";
			}

			// Group BY Brands
			if (brand.equals("yes")) {
				StrFilter += StrSearch;
				if (!brand_id.equals("0")) {
					StrFilter += " AND branch_brand_id = " + brand_id;
				}
				if (days15.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 0"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 15 ";
				}
				if (days30.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 15"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 30 ";
				}
				if (days60.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 31"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 60 ";
				}
				if (days90.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 61"
							+ " AND DATEDIFF(" + starttime + ",preownedstock_date) <= 90 ";
				}
				if (days91.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",preownedstock_date) >= 91 ";
				}
				StrFilter += " AND (so_preownedstock_id IS NULL OR so_retail_date = '')";
			}

			// String retailFilter = " AND (so_retail_date = ''"
			// + " OR so_retail_date > " + starttime + ")"
			// + " AND preownedstock_date <= " + starttime;

			// SOP("StrFilter===" + StrFilter);

			// SetSession("retailFilter", retailFilter, request);

			SetSession("preownedstockstrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../preowned/preowned-stock-list.jsp?smart=yes"));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
