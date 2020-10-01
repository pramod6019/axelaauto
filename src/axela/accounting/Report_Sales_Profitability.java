package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import cloudify.connect.Connect;

public class Report_Sales_Profitability extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", include_inactive_exe = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, fueltype_ids, item_ids;
	public String brand_id = "", region_id = "", team_id = "", item_id = "", fueltype_id = "", exe_id = "", model_id = "", soe_id = "";
	public String dr_datafield = "";
	public String[] dr_datafields;

	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";

	public String vehstock_invoice_date = "";
	public String so_retail_date = "";
	public int daydiff = 0;
	public int socount = 0;
	public int saleprice = 0;
	public int purchasevalue = 0;
	public int grossmargin = 0;
	public int servicemargin = 0;
	public int netmargin = 0;
	public int principalsupport = 0;
	public int financepayout = 0;
	public int insurancepayout = 0;
	public int accessories = 0;
	public int totalincome = 0;
	public int offergiven = 0;
	// public int focamount = 0;
	// public int totaldiscount = 0;
	// public int custoffer = 0;
	public int intrestOnInventory = 0;
	public int totalexpense = 0;
	public int profitorloss = 0;
	public String filter = "", StrFilter = "";

	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();
	public String emp_copy_access = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_sales_order_access, emp_voucher_access", request, response);
			emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				filter = PadQuotes(request.getParameter("filter"));

				if (filter.equals("yes")) {
					SalesOrderSendRedirect(request, response);
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

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
					if (!model_id.equals("")) {
						StrSearch += " AND model_id IN (" + model_id + ")";
					}
					if (!item_id.equals("")) {
						StrSearch += " AND so_item_id IN (" + item_id + ")";
					}
					if (!fueltype_id.equals("")) {
						StrSearch += " AND item_fueltype_id IN (" + fueltype_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + ")";
					}

					if (!starttime.equals("")) {
						StrSearch += " AND SUBSTR(so_retail_date,1,8) >= SUBSTR(" + starttime + ",1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(so_retail_date,1,8) <= SUBSTR(" + endtime + ",1,8) ";
					}
					StrSearch += BranchAccess.replace("branch_id", "so_branch_id");
					// SOP("strsear===" + StrSearch);

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListSalesProfit();
					}
				}

				targetstarttime = starttime;
				targetendtime = endtime;

				// SOP("StrSearch_----------" + StrSearch);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
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
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		dr_orderby = request.getParameter("dr_orderby");
		dr_datafields = request.getParameterValues("dr_datafields");
		dr_datafield = PadQuotes(RetrunSelectArrVal(request, "dr_datafields"));

		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		if (include_inactive_exe.equals("on")) {
			include_inactive_exe = "1";
		}
		else {
			include_inactive_exe = "0";
		}

	}

	protected void CheckForm() {
		msg = "";

		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
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
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

		if (dr_totalby.equals("7")) {
			if (Integer.parseInt(DiffBetweenDates(starttime, endtime).split(",")[0]) > 0) {
				msg = msg + "<br>Date duration cannot exceed 365 Days!";
			}
			if (brand_id.equals("")) {
				msg = msg + "<br>Select Brand!";
			} else if (brand_id.contains(",")) {
				msg = msg + "<br>Select One Brand!";
			}
		}

	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		Str.append("<option value=7").append(StrSelectdrop("7", dr_totalby)).append(">VIN</option>\n");
		return Str.toString();
	}

	public String PopulateFuelType(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1 = 1"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_fueltype_id id=dr_fueltype_id class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"padding:10px\">");
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

	public String ListSalesProfit() {

		String orderby = "";
		String SearchURL = "<a target=_blank href=../accounting/report-sales-profitability.jsp?filter=yes&";
		String type_id = "", type_name = "";
		int total_saleprice = 0;
		int total_purchasevalue = 0;
		int total_grossmargin = 0;
		int total_servicemargin = 0;
		int total_netmargin = 0;
		int total_principalsupport = 0;
		int total_financepayout = 0;
		int total_insurancepayout = 0;
		int total_accessories = 0;
		int total_totalincome = 0;
		int total_offergiven = 0;
		int total_intrestOnInventory = 0;
		int total_totalexpense = 0;
		int total_profitorloss = 0;
		int total_socount = 0;
		int total_additionaldiscount = 0;
		int temp_profit_loss = 0;

		StringBuilder stock_id = new StringBuilder();

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();

		try {

			StrSql = "SELECT so_id, emp_id, emp_active, emp_name, COALESCE (team_id,'0') AS team_id,"
					+ " COALESCE (team_name,'') AS team_name,"
					+ " branch_id, branch_name, region_id, region_name,"
					+ " branch_brand_id, brand_id, brand_name, model_id, model_name,"
					+ " so_vehstock_id, vehstock_chassis_no,"
					+ " COALESCE (vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE (so_retail_date, '') AS so_retail_date,"

					+ " COUNT(DISTINCT so_id) AS socount,"
					// + " GROUP_CONCAT(so_vehstock_id) AS so_vehstock_id,"
					+ " COALESCE (SUM(so_profitability_salesprice), 0) AS saleprice,"
					+ " COALESCE (SUM(so_profitability_grossinvoiceamount), 0) AS purchasevalue,"
					+ " COALESCE (SUM(so_profitability_grossmargin), 0) AS grossmargin,"
					+ " COALESCE (SUM(so_profitability_servicemargin), 0) AS servicemargin,"
					+ " COALESCE (SUM(so_profitability_netmargin), 0) AS netmargin,"
					+ " COALESCE (SUM(so_profitability_principalsupport), 0) AS principalsupport,"
					+ " COALESCE (SUM(so_finance_net), 0) AS financepayout,"
					+ " COALESCE (SUM(so_insur_net), 0) AS insurancepayout,"
					+ " COALESCE (SUM(so_profitability_paidaccessories), 0) AS accessories,"
					+ " COALESCE (SUM(so_profitability_totalincome), 0) AS totalincome,"
					+ " COALESCE (SUM(so_profitability_offercost), 0) AS offergiven,"
					+ " COALESCE (SUM(so_profitability_inventoryinterest), 0) AS intrestOnInventory,"
					+ " COALESCE (SUM(so_profitability_totalexpense), 0) AS totalexpense,"
					+ " COALESCE (SUM(so_profitability_profit), 0) AS profitorloss,"

					+ " COALESCE(SUM((SELECT soitem_disc"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND soitem_so_id = so_id"
					+ " AND soitem_option_group_id = 1"
					+ " AND item_name = 'Additional Discount'"
					+ " GROUP BY so_id )),0)"
					+ " AS 'additionaldiscount'"

					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE 1 = 1 ";
			if (include_inactive_exe.equals("0")) {
				StrSql += " AND emp_active = 1";
			}

			StrSql += " AND emp_sales = 1 "
					+ " AND so_retail_date != '' "
					+ " AND so_active = '1' "
					+ StrSearch
					+ " GROUP BY ";
			orderby = " ORDER BY ";

			switch (dr_totalby) {
				case "1" :
					StrSql += " emp_id";
					orderby += " emp_name";
					break;
				case "2" :
					StrSql += " team_id";
					orderby += " team_name";
					break;
				case "3" :
					StrSql += " branch_id";
					orderby += " branch_name";
					break;
				case "4" :
					StrSql += " branch_region_id";
					orderby += " region_name";
					break;
				case "5" :
					StrSql += " branch_brand_id";
					orderby += " brand_name";
					break;
				case "6" :
					StrSql += " model_id";
					orderby += " model_name";
					break;
				case "7" :
					StrSql += " so_id";
					orderby += " so_date DESC"
							+ " LIMIT 2000";
					break;
			}

			StrSql += orderby;
			// + " so_id = " + so_id;

			// SOPInfo("StrSql==profitability report====" + StrSql);
			// /SOP("StrSql=1111------------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				SearchURL += "brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&branch_id=" + branch_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&inactive_exe=" + include_inactive_exe
						+ "&model_id=" + model_id
						+ "&item_id=" + item_id
						+ "&fueltype_id=" + fueltype_id
						+ "&starttime=" + starttime
						+ "&endtime=" + endtime;

				StrHead.append("<div class=\"table-responsive table-bordered\">\n");
				StrHead.append("<table id='table' class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead><tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");

				switch (dr_totalby) {
					case "1" :
						StrHead.append("<th>Sales Consultant</th>\n");
						type_name = "emp";
						break;
					case "2" :
						StrHead.append("<th>Team</th>\n");
						type_name = "team";
						break;
					case "3" :
						StrHead.append("<th>Branch</th>\n");
						type_name = "branch";
						break;
					case "4" :
						StrHead.append("<th>Region</th>\n");
						type_name = "region";
						break;
					case "5" :
						StrHead.append("<th>Brand</th>\n");
						type_name = "brand";
						break;
					case "6" :
						StrHead.append("<th>Model</th>\n");
						type_name = "model";
						break;
					case "7" :
						StrHead.append("<th>SO ID</th>\n");
						type_name = "so";
						break;
				}

				if (!dr_totalby.equals("7")) {
					StrHead.append("<th>SO Count</th>\n");
				} else {
					StrHead.append("<th>VIN No.</th>\n");
				}

				StrHead.append("<th>Sale Price</th>\n");
				StrHead.append("<th>Purchase Value</th>\n");
				StrHead.append("<th>Gross Margin</th>\n");
				StrHead.append("<th>Serivce Margin</th>\n");
				StrHead.append("<th>Net Margin</th>\n");
				if (!dr_datafield.contains("PS")) {
					StrHead.append("<th>Principal Support</th>\n");
				}
				StrHead.append("<th>Finance Payout</th>\n");
				StrHead.append("<th>Insurance Payout</th>\n");
				StrHead.append("<th>Paid Accessory @ 20%</th>\n");
				StrHead.append("<th>Total Income</th>\n");
				StrHead.append("<th>Offer given including accessory at cost</th>\n");
				StrHead.append("<th>Additional Discount</th>\n");
				if (!dr_datafield.contains("IOI")) {
					StrHead.append("<th>Interest on Inventory</th>\n");
				}
				StrHead.append("<th>Total Expense</th>\n");
				StrHead.append("<th>Profit</th>\n");
				StrHead.append("</tr></thead>\n");
				StrHead.append("<tbody>\n");
				Str.append(StrHead);
				// crs.last();
				// int rowcount = crs.getRow();
				int count = 0;

				// crs.beforeFirst();
				while (crs.next()) {
					count++;
					switch (dr_totalby) {
						case "1" :
							type_id = crs.getString("emp_id");
							break;
						case "2" :
							type_id = crs.getString("team_id");
							break;
						case "3" :
							type_id = crs.getString("branch_id");
							break;
						case "4" :
							type_id = crs.getString("region_id");
							break;
						case "5" :
							type_id = crs.getString("brand_id");
							break;
						case "6" :
							type_id = crs.getString("model_id");
							break;
						case "7" :
							type_id = crs.getString("so_id");
							break;
					}

					total_socount += crs.getInt("socount");
					total_saleprice += (int) crs.getDouble("saleprice");
					total_purchasevalue += (int) crs.getDouble("purchasevalue");
					total_grossmargin += (int) crs.getDouble("grossmargin");
					total_servicemargin += (int) crs.getDouble("servicemargin");
					total_netmargin += (int) crs.getDouble("netmargin");
					total_principalsupport += (int) crs.getDouble("principalsupport");
					total_financepayout += (int) crs.getDouble("financepayout");
					total_insurancepayout += (int) crs.getDouble("insurancepayout");
					total_accessories += (int) crs.getDouble("accessories");
					total_totalincome += (int) crs.getDouble("totalincome");
					total_offergiven += (int) crs.getDouble("offergiven");
					total_intrestOnInventory += (int) crs.getDouble("intrestOnInventory");
					total_totalexpense += (int) crs.getDouble("totalexpense");

					// additional discount comes from soitem table to just show it as it is already calculated for Profit
					// here no calculation is done only shown it
					total_additionaldiscount += (int) crs.getDouble("additionaldiscount");

					// temporary calculation to subtract principal support and add interest of
					// inventory with total Profit if the data field drop down is selected

					temp_profit_loss = (int) crs.getDouble("profitorloss");

					if (dr_datafield.contains("IOI")) {
						temp_profit_loss = (temp_profit_loss + (int) crs.getDouble("intrestOnInventory"));
					}
					if (dr_datafield.contains("PS")) {
						temp_profit_loss = (temp_profit_loss - (int) crs.getDouble("principalsupport"));
					}

					total_profitorloss += temp_profit_loss;

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>");
					switch (dr_totalby) {
						case "1" :
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append(">")
									.append(crs.getString("emp_name")).append("</a>");
							if (crs.getString("emp_active").equals("0")) {
								Str.append("</br><font color=red > [Inactive] </font>");
							}
							break;
						case "2" :
							Str.append("<a href=../sales/team-list.jsp?team_id=").append(crs.getString("team_id"));
							Str.append("&dr_branch=").append(crs.getString("branch_id"));
							Str.append(" target=_blank>").append(crs.getString("team_name")).append("</b></a>");
							break;
						case "3" :
							Str.append("<a href=../portal/branch-list.jsp?branch_id=").append(crs.getString("branch_id"));
							Str.append(" target=_blank>").append(crs.getString("branch_name")).append("</b></a>");
							break;
						case "4" :
							Str.append("<a href=../portal/region-list.jsp?region_id=").append(crs.getString("region_id"));
							Str.append(" target=_blank>").append(crs.getString("region_name")).append("</b></a>");
							break;
						case "5" :
							Str.append(crs.getString("brand_name"));
							break;
						case "6" :
							Str.append(crs.getString("model_name"));
							break;
						case "7" :
							Str.append("<a href=../sales/veh-salesorder-list.jsp?so_id=").append(crs.getInt("so_id")).append(">")
									.append(crs.getString("so_id")).append("</a>");
							break;
					}

					Str.append("</td>");
					if (dr_totalby.equals("7")) {
						Str.append("<td valign=top align=right>");
						Str.append("<a href=../inventory/stock-list.jsp?" + "&vehstock_id=" + crs.getInt("so_vehstock_id")
								+ " traget=_blank >" + crs.getString("vehstock_chassis_no") + "</a>")
								.append("</td>");
					} else {
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&type_name=" + type_name + "&type_id=" + type_id + " traget=_blank >" + crs.getInt("socount") + "</a>").append("</td>");
					}

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("saleprice")) + "")).append("</td>");

					if (dr_totalby.equals("7")) {
						Str.append("<td valign=top align=right>");
						Str.append("<a href=../inventory/stock-list.jsp?" + "&vehstock_id=" + crs.getInt("so_vehstock_id") + " traget=_blank >");
						Str.append(IndFormat(deci.format(crs.getDouble("purchasevalue"))) + "</a>").append("</td>");
					} else {
						Str.append("<td valign=top align=right>");
						Str.append(SearchURL + "&stock=yes&type_name=" + type_name + "&type_id=" + type_id + " traget=_blank >");
						Str.append(IndFormat(deci.format(crs.getDouble("purchasevalue"))) + "</a>").append("</td>");
					}

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("grossmargin")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("servicemargin")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("netmargin")) + "")).append("</td>");

					if (!dr_datafield.contains("PS")) {
						Str.append("<td valign=top align=right>");
						Str.append(IndFormat(deci.format(crs.getDouble("principalsupport")) + "")).append("</td>");
					}

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("financepayout")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("insurancepayout")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("accessories")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("totalincome")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("offergiven")) + "")).append("</td>");

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("additionaldiscount")) + "")).append("</td>");

					if (!dr_datafield.contains("IOI")) {
						Str.append("<td valign=top align=right>");
						Str.append(IndFormat(deci.format(crs.getDouble("intrestOnInventory")) + "")).append("</td>");
					}

					Str.append("<td valign=top align=right>");
					Str.append(IndFormat(deci.format(crs.getDouble("totalexpense")) + ""));
					Str.append("</td>");

					if (temp_profit_loss > 0) {
						Str.append("<td valign=top style=\"text-align: right;\"><b><font color='blue'> " + IndFormat(deci.format(temp_profit_loss) + "") + "</font></b></td>");
					} else {
						Str.append("<td valign=top style=\"text-align: right;\"><b><font color='red'> " + IndFormat(deci.format(temp_profit_loss) + "") + "</font></b></td>");
					}
					Str.append("</tr>\n");

				}
				Str.append("<tr align=center>\n");
				if (dr_totalby.equals("7")) {
					Str.append("<td valign=top align=right colspan='3'><b>");
				} else {
					Str.append("<td valign=top align=right colspan='2'><b>");
				}

				Str.append("Total").append("</b></td>");

				if (!dr_totalby.equals("7")) {
					Str.append("<td valign=top align=right><b>");
					Str.append(SearchURL + " traget=_blank >" + total_socount + "</a>").append("</td>");
				}

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_saleprice) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(SearchURL + "&stock=yes traget=_blank >");
				Str.append(IndFormat(deci.format(total_purchasevalue)) + "</a></b>").append("</td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_grossmargin) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_servicemargin) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_netmargin) + "")).append("</b></td>");

				if (!dr_datafield.contains("PS")) {
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(deci.format(total_principalsupport) + "")).append("</b></td>");
				}

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_financepayout) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_insurancepayout) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_accessories) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_totalincome) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_offergiven) + "")).append("</b></td>");

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_additionaldiscount) + "")).append("</b></td>");

				if (!dr_datafield.contains("IOI")) {
					Str.append("<td valign=top align=right><b>");
					Str.append(IndFormat(deci.format(total_intrestOnInventory) + "")).append("</b></td>");
				}

				Str.append("<td valign=top align=right><b>");
				Str.append(IndFormat(deci.format(total_totalexpense) + "")).append("</b></td>");

				if (total_profitorloss > 0) {
					Str.append("<td valign=top style=\"text-align: right;\"><b><font color='blue'> " + IndFormat(deci.format(total_profitorloss) + "") + "</font></b></td>");
				} else {
					Str.append("<td valign=top style=\"text-align: right;\"><b><font color='red'> " + IndFormat(deci.format(total_profitorloss) + "") + "</font></b></td>");
				}

			}
			else {
				Str.append("<div class=\"portlet box  \"> <div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">");
				Str.append("</div></div><div class=\"portlet-body portlet-empty\"> <div class=\"tab-pane\" id=\"\">");
				Str.append("<br><br><br><br><b><font color=red>No Data found!</font></b><br><br>\n");
				Str.append("</div></div></div>");
			}

			Str.append("</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");

			crs.close();
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	private void SalesOrderSendRedirect(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_ids = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String item_ids = PadQuotes(RetrunSelectArrVal(request, "item_id"));
			String fueltype_id = PadQuotes(RetrunSelectArrVal(request, "fueltype_id"));
			String team_ids = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String exe_ids = PadQuotes(RetrunSelectArrVal(request, "exe_id"));
			String starttime = PadQuotes(RetrunSelectArrVal(request, "starttime"));
			String endtime = PadQuotes(RetrunSelectArrVal(request, "endtime"));
			String type_name = PadQuotes(RetrunSelectArrVal(request, "type_name"));
			String type_id = PadQuotes(RetrunSelectArrVal(request, "type_id"));
			String inactive_exe = PadQuotes(RetrunSelectArrVal(request, "inactive_exe"));
			String stock = PadQuotes(request.getParameter("stock"));
			// Brand
			if (!brand_ids.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_ids + ") ";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrFilter += " AND so_branch_id IN (" + branch_ids + ") ";
			}
			// Models
			if (!model_ids.equals("")) {
				StrFilter += " AND item_model_id IN (" + model_ids + ") ";
			}
			// Item
			if (!item_ids.equals("")) {
				StrFilter += " AND so_item_id IN (" + item_ids + ") ";
			}
			// Fuel Type
			if (!fueltype_id.equals("")) {
				StrFilter += " AND item_fueltype_id IN (" + fueltype_id + ") ";
			}
			// Teams
			if (!team_ids.equals("")) {
				StrFilter += " AND emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id IN (" + team_ids + " )) ";
			}
			// Sales Consultant
			if (!exe_ids.equals("")) {
				StrFilter += " AND emp_id IN (" + exe_ids + ") ";
			}
			if (inactive_exe.equals("0")) {
				StrFilter += " AND emp_active = 1 ";
			}

			if (type_name.equals("emp")) {
				StrFilter += " AND emp_id = " + type_id;
			}
			else if (type_name.equals("brand")) {
				StrFilter += " AND branch_brand_id = " + type_id + " ";
			}
			else if (type_name.equals("region")) {
				StrFilter += " AND branch_region_id = " + type_id + " ";
			}
			else if (type_name.equals("branch")) {
				StrFilter += " AND so_branch_id = " + type_id + " ";
			}
			else if (type_name.equals("team")) {
				StrFilter += " AND emp_id IN (SELECT teamtrans_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id = " + type_id + " ) ";
			} else if (type_name.equals("model")) {
				StrFilter += " AND item_model_id = " + type_id + " ";
			}
			if (!starttime.equals("")) {
				StrFilter += " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR(" + starttime + ", 1, 8) ";
			}
			if (!endtime.equals("")) {
				StrFilter += " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR(" + endtime + ", 1, 8) ";
			}

			StrFilter += " AND so_active = 1"
					+ " AND so_retail_date != '' ";

			// SOP("StrSearch====" + StrFilter);
			if (stock.equals("yes")) {

				StrFilter = " AND vehstock_id IN ("
						+ " SELECT vehstock_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
						+ " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
						+ " WHERE 1 = 1	"
						+ StrFilter + ")";

				// SOP("StrFilter==" + StrFilter);

				SetSession("stockstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../inventory/stock-list.jsp?smart=yes"));
			} else {
				SetSession("sostrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDataFields(String[] dr_datafields) {
		// SOP("dr_field_ids===" + dr_field_ids.toString());
		StringBuilder Str = new StringBuilder();
		Str.append("<select name='dr_datafields' id='dr_datafields' class='form-control multiselect-dropdown' multiple=multiple size=10'>");
		Str.append("<option value='PS' ").append(StrArrSelectdrop("PS", dr_datafields)).append(">Principal Support</option>\n");
		Str.append("<option value='IOI' ").append(StrArrSelectdrop("IOI", dr_datafields)).append(">Interest On Inventory</option>\n");
		Str.append("</select>");

		return Str.toString();
	}
}
