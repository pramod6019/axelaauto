package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Stock_Gross_Margin extends Connect {

	public String SummaryHTML = "", no_Summarydata = "", StrHTML = "", list = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String StrSql = "";
	public String BranchAccess = "", ExeAccess = "", emp_all_exe = "";
	public String msg = "";
	public String[] brand_ids, region_ids, branch_ids, model_ids, location_ids, cat_ids, billtype_ids;
	public String brand_id = "", region_id = "", branch_id = "", model_id = "", models = "", location_id = "", cat_id = "", billtype_id = "";
	public String dr_groupby = "0", dr_totalby = "";
	public String go = "", search = "", itemsearch = "";
	public String dr_branch_id = "0";
	public String targetstarttime = "";
	public String targetendtime = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String voucher_branch_id = "", export = "", exportcount = "";
	DecimalFormat deci = new DecimalFormat("##.##");
	public String SqlJoin = "";
	public String CountSql = "";
	public int recperpage = 100;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String PageNaviStr = "", PageNavi = "";

	public MIS_Check mischeck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			SOP("Report_Stock_Gross_Margin==");
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			go = PadQuotes(request.getParameter("submit_button"));
			search = PadQuotes(request.getParameter("search"));
			list = PadQuotes(request.getParameter("list"));
			itemsearch = PadQuotes(request.getParameter("itemsearch"));
			export = PadQuotes(request.getParameter("export"));
			SOP("export==" + export);
			exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			PageNavi = PadQuotes(request.getParameter("PageNavi"));
			SOP("PageCurrents--------" + PageCurrents);
			GetValues(request, response);
			CheckForm();
			if (go.equals("")) {
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				msg = "";
			}
			if (go.equals("Go") || itemsearch.equals("itemsearch") || export.equals("Export") || PageNavi.equals("yes")) {
				StrSearch = BranchAccess.replace("branch_id", "voucher_branch_id"); // + " " + ExeAccess;
				if (!brand_id.equals("")) {
					StrSearch += " AND model_brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND voucher_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_region_id IN (" + region_id + "))";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
				}
				if (!model_id.equals("")) {
					StrSearch += " AND model_id IN (" + model_id + ")";
				}
				if (!location_id.equals("")) {
					StrSearch += " AND location_id IN (" + location_id + ")";
				}
				if (!cat_id.equals("")) {
					StrSearch += " AND item_cat_id IN (" + cat_id + ")";
				}
				SOP("search==" + search);
				if (!search.equals("") && search.length() < 4) {
					msg = "Item Name should be more than 3 characters";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}

				if (msg.equals("")) {

					// SOP("SummaryHTML==" + SummaryHTML);
					SOP("list==" + list);
					SOP("search==" + search);
					SOP("go==" + go);
					if ((list.equals("yes") && (!search.equals("") || go.equals("Go"))) || PageNavi.equals("yes")) {
						StrHTML = TotalStockDetails(request, response);
					} else if (go.equals("Go") && !list.equals("yes")) {
						SummaryHTML = TotalStockDetails(request, response);
					}
					if (SummaryHTML.equals("") || (SummaryHTML.equals("") && list.equals("yes"))) {
						no_Summarydata = ("<center><b><font color=red>No Stock Gross Margin Found!</font></b></center>");
					}
				}

			}
			if (export.equals("Export")) {
				System.out.println("coming");
				GetValues(request, response);
				CheckForm();
				System.out.println("msg===" + msg);
				if (!msg.equals("")) {
					SOP("222");
					msg = "Error!" + msg;
				} else if (msg.equals("") && ReturnPerm(comp_id, "emp_export_access", request).equals("1")) {
					SOP("125");
					SOP("StrSearch==" + StrSearch);
					TotalStockDetails(request, response);
				}
				else {
					SOP("666");
					response.sendRedirect(AccessDenied());
				}
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
		cat_id = RetrunSelectArrVal(request, "dr_cat");
		cat_ids = request.getParameterValues("dr_cat");
		billtype_id = RetrunSelectArrVal(request, "dr_jctrans_billtype");
		billtype_ids = request.getParameterValues("dr_jctrans_billtype");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		model_id = RetrunSelectArrVal(request, "dr_model");
		models = model_id;
		model_ids = request.getParameterValues("dr_model");
		location_id = RetrunSelectArrVal(request, "dr_location");
		location_ids = request.getParameterValues("dr_location");
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));

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
	}

	public String TotalStockDetails(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		int count = 1;
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		int TotalRecords;
		String PageURL;

		double totalunits = 0;
		double totalcostprice = 0;
		double totalsalesprice = 0;
		double totalincostofsales = 0;
		double totalnetvalue = 0;
		double totalgrossmargin = 0;
		double totalgrossperc = 0;
		double netsalevalue = 0;
		double Costofsale = 0;

		String totalname = "";
		String totalbyquery = "";
		String uom = "";
		int itemcount = 0;

		double units = 0;
		double costprice = 0;
		double salesprice = 0;
		double grossmargin = 0;
		double grossperc = 0;

		double totalbyunits = 0;
		double totalbycostprice = 0;
		double totalbysalesprice = 0;
		double totalbyincostofsales = 0;
		double totalbynetvalue = 0;
		double totalbygrossmargin = 0;
		double totalbygrossperc = 0;

		try {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT COALESCE (item_id, '0') AS 'ID', COALESCE (item_name, '') AS 'ItemName',"
					+ " COALESCE (item_code, '') AS 'ItemCode',"
					+ " COALESCE (cat_name, '') AS 'CategoryName',"
					+ " COALESCE(uom_name, '') AS uom_name,";
			if (dr_totalby.equals("4")) {
				StrSql += " COALESCE(branch_id, '0') AS branchId,"
						+ " COALESCE (branch_name, '') AS 'BranchName',";
			}
			if (dr_totalby.equals("2")) {
				StrSql += " COALESCE(cat_id, '0') AS catId,";
			}
			if (dr_totalby.equals("3")) {
				StrSql += " COALESCE(billtype_id, '0') AS billtype_id,";
			}
			StrSql += " COALESCE(SUM(vouchertrans.vouchertrans_qty), 0) AS 'units',"
					+ " COALESCE(mainpurchaseprice.price_amt,0) AS 'CostPrice',"
					+ " COALESCE( vouchertrans.vouchertrans_price - COALESCE(vouchertrans.vouchertrans_discountamount, 0) ,0) AS 'SalesPrice'";

			if (export.equals("Export")) {
				StrSql += " , COALESCE (FORMAT(gross.CostOfSales,2), 0) AS 'CostOfSales',"
						+ " COALESCE (FORMAT(gross.NetSalesValue,2), 0) AS 'NetSalesValue',"
						+ " COALESCE (FORMAT(gross.grossmargin,2), 0) AS 'GrossMargin',"
						+ " COALESCE (FORMAT((gross.grossmargin / gross.NetSalesValue)*100,2),0) AS 'GrossMarginPerc'";
			}
			if (dr_totalby.equals("2")) {
				CountSql = "SELECT COUNT(DISTINCT(item_cat_id))";
			} else {
				CountSql = "SELECT COUNT(DISTINCT(item_id))";

			}
			SqlJoin += " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price mainpurchaseprice ON mainpurchaseprice.price_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans vouchertrans ON vouchertrans.vouchertrans_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans.vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_branch_id = voucher_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id ";

			if (dr_totalby.equals("4")) {
				SqlJoin += " INNER JOIN axelaauto.axela_brand ON brand_id = model_brand_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id";

			}
			SqlJoin += " WHERE item_type_id != 1"
					+ " AND voucher_vouchertype_id IN(6, 7)"
					+ "	AND voucher_active = '1'"
					+ " AND vouchertrans.vouchertrans_rowcount != 0"
					+ " AND vouchertrans.vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!search.equals("")) {
				SqlJoin += " AND ( item_name LIKE ('%" + search + "%')"
						+ " OR item_id LIKE ('%" + search + "%')"
						+ " OR item_code LIKE ('%" + search + "%'))";
			}
			SqlJoin += " AND mainpurchaseprice.price_id = ("
					+ " SELECT price_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE SUBSTR(price_effective_from, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND price_active = 1 "
					+ " AND price_rateclass_id = 6 "
					+ " AND price_item_id = vouchertrans.vouchertrans_item_id "
					+ " ORDER BY price_effective_from"
					+ " DESC LIMIT 1 )";
			StrSql += SqlJoin;
			CountSql += SqlJoin;
			if (dr_totalby.equals("1")) {
				StrSql += StrSearch + " GROUP BY item_id "
						+ " ORDER BY item_name";
			} else if (dr_totalby.equals("2")) {
				StrSql += StrSearch + " GROUP BY item_id "
						+ " ORDER BY cat_name";
			} else if (dr_totalby.equals("3")) {
				StrSql += StrSearch + " GROUP BY item_id "
						+ " ORDER BY billtype_name";
			} else if (dr_totalby.equals("4")) {
				StrSql += StrSearch + " GROUP BY item_id "
						+ " ORDER BY branch_name";
			} else {
				StrSql += StrSearch + " GROUP BY item_id "
						+ " ORDER BY item_name";
			}

			CountSql += StrSearch;
			SOP("CountSql====" + CountSql);
			TotalRecords = Integer.parseInt(CNumeric(PadQuotes(ExecuteQuery(CountSql))));
			SOP("TotalRecords=====" + TotalRecords);

			if (TotalRecords != 0) {
				System.out.println("PageCurrent===" + PageCurrent);
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount++;
				}
				// display on jsp page
				PageNaviStr = PageNaviJS("loadTable", PageCurrent, PageCount, PageListSize);
				if (dr_totalby.equals("1")) {
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				}
				SOP("StrSql==Totalgross margin==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					Str.append("<center><div>" + PageNaviStr + "</div></center>");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" style=\"border-collapse:unset\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th>#<br><br></th>\n");
					if (dr_totalby.equals("2")) {
						Str.append("<th>Category<br><br></th>\n");
					} else if (dr_totalby.equals("3")) {
						Str.append("<th>Type<br><br></th>\n");
					} else if (dr_totalby.equals("4")) {
						Str.append("<th>Branch<br><br></th>\n");
					} else {
						Str.append("<th>Item<br><br></th>\n");
					}
					Str.append("<th data-hide=\"phone\"><b>UOM</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Quantity</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Cost Price</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Sales Price</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Cost Of Sales</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Net Sales Value</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Gross Margin</b><br><br></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Gross Margin %</b><br><br></th>\n");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>\n");

					if (!dr_totalby.equals("1")) {
						count = 1;
						itemcount = 1;
						while (crs.next()) {
							if (totalname.equals("")) {
								if (dr_totalby.equals("2")) {
									totalname = crs.getString("CategoryName");
								}
								if (dr_totalby.equals("3")) {
									totalname = crs.getString("billtype_name");
								}
								if (dr_totalby.equals("4")) {
									totalname = crs.getString("BranchName");
								}
							}
							uom = crs.getString("uom_name");
							if (dr_totalby.equals("2")) {
								totalbyquery = crs.getString("CategoryName");
							}
							if (dr_totalby.equals("3")) {
								totalbyquery = crs.getString("billtype_name");
							}
							if (dr_totalby.equals("4")) {
								totalbyquery = crs.getString("BranchName");
							}
							if (totalname.equals(totalbyquery)) {
								units += (crs.getDouble("units"));
								costprice += (crs.getDouble("CostPrice"));
								salesprice += (crs.getDouble("salesprice"));
								netsalevalue += (crs.getDouble("units") * crs.getDouble("salesprice"));
								Costofsale += (crs.getDouble("units") * crs.getDouble("CostPrice"));
								grossmargin += ((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice")));
								grossperc += ((((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice"))) /
										(crs.getDouble("units") * crs.getDouble("salesprice"))) * 100 / itemcount);
								totalbyunits += (crs.getDouble("units"));
								totalbycostprice += (crs.getDouble("CostPrice"));
								totalbysalesprice += (crs.getDouble("salesprice"));
								totalbyincostofsales += (crs.getDouble("units") * crs.getDouble("CostPrice"));;
								totalbynetvalue += (crs.getDouble("units") * crs.getDouble("salesprice"));
								totalbygrossmargin += ((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice")));
								totalbygrossperc += ((((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice"))) /
										(crs.getDouble("units") * crs.getDouble("salesprice"))) * 100 / itemcount);
								itemcount++;
								System.out.println("grossperc==" + grossperc);
							}

							else {
								Str.append("<tr>\n");
								Str.append("<td style='text-align:center'>");
								Str.append(count);
								Str.append("</td>\n");
								// total by name
								Str.append("<td align='left'>");
								Str.append(totalname);
								Str.append("</td>\n");

								// uom_name
								Str.append("<td align='center'>");
								Str.append(crs.getString("uom_name"));
								Str.append("</td>\n");

								// units
								Str.append("<td align='center'>");
								Str.append(IndDecimalFormat(deci.format(units)));
								Str.append("</td>\n");

								// CostPrice
								Str.append("<td align='right'>");
								Str.append(IndDecimalFormat(deci.format(costprice)));
								Str.append("</td>\n");

								// SalesPrice
								Str.append("<td align='right'>");
								Str.append(IndDecimalFormat(deci.format(salesprice)));
								Str.append("</td>\n");

								// CostOfSales
								Str.append("<td align='right'>");
								Str.append(IndDecimalFormat(deci.format(Costofsale)));
								Str.append("</td>\n");

								// NetSalesValue
								Str.append("<td align='right'>");
								Str.append(IndDecimalFormat(deci.format(netsalevalue)));
								Str.append("</td>\n");

								// GrossMargin
								Str.append("<td align='right'>");
								Str.append(IndDecimalFormat(deci.format(grossmargin)));
								Str.append("</td>\n");
								// GrossMarginPerc
								Str.append("<td align='right'>");
								if (netsalevalue != 0) {
									Str.append(IndDecimalFormat(deci.format(grossperc / itemcount)));
								}
								Str.append("</td>\n");
								// total calculation
								totalunits += totalbyunits;
								totalcostprice += totalbycostprice;
								totalsalesprice += totalbysalesprice;
								totalincostofsales += totalbyincostofsales;
								totalnetvalue += totalbynetvalue;
								totalgrossmargin += totalbygrossmargin;
								totalgrossperc += grossperc / itemcount;
								if (dr_totalby.equals("2")) {
									totalname = crs.getString("CategoryName");
								}
								if (dr_totalby.equals("3")) {
									totalname = crs.getString("billtype_name");
								}
								if (dr_totalby.equals("4")) {
									totalname = crs.getString("BranchName");
								}
								count++;
								itemcount = 1;
								units = 0.0;
								costprice = 0.0;
								salesprice = 0.0;
								netsalevalue = 0.0;
								Costofsale = 0.0;
								grossmargin = 0.0;
								grossperc = 0.0;
								totalbyunits = 0.0;
								totalbycostprice = 0.0;
								totalbysalesprice = 0.0;
								totalbyincostofsales = 0.0;
								totalbynetvalue = 0.0;
								totalbygrossmargin = 0.0;
								totalbygrossperc = 0.0;
								units += (crs.getDouble("units"));
								costprice += (crs.getDouble("CostPrice"));
								salesprice += (crs.getDouble("salesprice"));
								netsalevalue += (crs.getDouble("units") * crs.getDouble("salesprice"));
								Costofsale += (crs.getDouble("units") * crs.getDouble("CostPrice"));
								grossmargin += ((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice")));
								grossperc += ((((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice"))) /
										(crs.getDouble("units") * crs.getDouble("salesprice")) * 100) / itemcount);
								totalbyunits += (crs.getDouble("units"));
								totalbycostprice += (crs.getDouble("CostPrice"));
								totalbysalesprice += (crs.getDouble("salesprice"));
								totalbyincostofsales += (crs.getDouble("units") * crs.getDouble("CostPrice"));;
								totalbynetvalue += (crs.getDouble("units") * crs.getDouble("salesprice"));
								totalbygrossmargin += ((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice")));
								totalbygrossperc += (((((crs.getDouble("units") * crs.getDouble("salesprice"))
										- (crs.getDouble("units") * crs.getDouble("CostPrice"))) /
										(crs.getDouble("units") * crs.getDouble("salesprice"))) * 100) / itemcount);

							}
						}
					} else {
						while (crs.next()) {
							Str.append("<tr>\n");
							Str.append("<td style='text-align:center'>");
							Str.append(count++);
							Str.append("</td>\n");
							// item_code
							Str.append("<td align='left'>");
							Str.append(crs.getString("ItemName"));
							if (!crs.getString("ItemCode").equals("")) {
								Str.append(" ( " + crs.getString("ItemCode") + " )");
							}
							Str.append("</td>\n");
							netsalevalue = Double.parseDouble(crs.getString("units")) * Double.parseDouble(crs.getString("SalesPrice"));
							Costofsale = Double.parseDouble(crs.getString("units")) * Double.parseDouble(crs.getString("CostPrice"));
							// uom_name
							Str.append("<td align='center'>");
							Str.append(crs.getString("uom_name"));
							Str.append("</td>\n");

							// units
							Str.append("<td align='center'>");
							Str.append(IndDecimalFormat(deci.format(Double.parseDouble(crs.getString("units")))));
							Str.append("</td>\n");

							// CostPrice
							Str.append("<td align='right'>");
							Str.append(IndDecimalFormat(deci.format(Double.parseDouble(crs.getString("CostPrice")))));
							Str.append("</td>\n");

							// SalesPrice
							Str.append("<td align='right'>");
							Str.append(IndDecimalFormat(deci.format(Double.parseDouble(crs.getString("SalesPrice")))));
							Str.append("</td>\n");

							// CostOfSales
							Str.append("<td align='right'>");
							Str.append(IndDecimalFormat(deci.format(Costofsale)));
							Str.append("</td>\n");

							// NetSalesValue
							Str.append("<td align='right'>");
							Str.append(IndDecimalFormat(deci.format(netsalevalue)));
							Str.append("</td>\n");

							// GrossMargin
							Str.append("<td align='right'>");
							Str.append(IndDecimalFormat(deci.format(netsalevalue - Costofsale)));
							Str.append("</td>\n");
							// GrossMarginPerc
							Str.append("<td align='right'>");
							if (netsalevalue != 0) {
								Str.append(deci.format(((netsalevalue - Costofsale) / netsalevalue) * 100));
							}
							Str.append("</td>\n");
							// total calculation
							totalunits += Double.parseDouble(crs.getString("units"));
							totalcostprice += Double.parseDouble(crs.getString("CostPrice"));
							totalsalesprice += Double.parseDouble(crs.getString("SalesPrice"));
							totalincostofsales += Costofsale;
							totalnetvalue += netsalevalue;
							totalgrossmargin += (netsalevalue - Costofsale);
							totalgrossperc += ((netsalevalue - Costofsale) / netsalevalue) * 100;
						}
					}
					if (!dr_totalby.equals("1") && ((!cat_id.contains(",") && cat_id.length() != 0)
							|| (!branch_id.contains(",") && branch_id.length() != 0) || count == 1))
					{
						System.out.println("grossmargin==" + grossmargin);
						System.out.println("1589");
						Str.append("<tr>\n");
						Str.append("<td style='text-align:center'>");
						Str.append(count);
						Str.append("</td>\n");

						// total by name
						Str.append("<td align='left'>");
						Str.append(totalname);
						Str.append("</td>\n");

						// uom_name
						Str.append("<td align='center'>");
						Str.append(uom);
						Str.append("</td>\n");

						// units
						Str.append("<td align='center'>");
						Str.append(IndDecimalFormat(deci.format(units)));
						Str.append("</td>\n");

						// CostPrice
						Str.append("<td align='right'>");
						Str.append(IndDecimalFormat(deci.format(costprice)));
						Str.append("</td>\n");

						// SalesPrice
						Str.append("<td align='right'>");
						Str.append(IndDecimalFormat(deci.format(salesprice)));
						Str.append("</td>\n");

						// CostOfSales
						Str.append("<td align='right'>");
						Str.append(IndDecimalFormat(deci.format(Costofsale)));
						Str.append("</td>\n");

						// NetSalesValue
						Str.append("<td align='right'>");
						Str.append(IndDecimalFormat(deci.format(netsalevalue)));
						Str.append("</td>\n");

						// GrossMargin
						Str.append("<td align='right'>");
						Str.append(IndDecimalFormat(deci.format(grossmargin)));
						Str.append("</td>\n");
						// GrossMarginPerc
						Str.append("<td align='right'>");
						if (netsalevalue != 0) {
							Str.append(deci.format(grossperc / itemcount));
						}
						Str.append("</td>\n");
						// total calculation
						totalunits += totalbyunits;
						totalcostprice += totalbycostprice;
						totalsalesprice += totalbysalesprice;
						totalincostofsales += totalbyincostofsales;
						totalnetvalue += totalbynetvalue;
						totalgrossmargin += totalbygrossmargin;
						totalgrossperc += totalbygrossperc / itemcount;
					}
					// Total
					Str.append("<tr>");
					Str.append("<td></td>\n");
					Str.append("<td style='text-align: right;'><b>");
					Str.append(" Total:</b></td>");

					Str.append("<td></td>\n");
					Str.append("<td align='center'><b>");
					System.out.println("4");
					Str.append(IndDecimalFormat(deci.format(totalunits)));
					System.out.println("4");
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					System.out.println("4");
					Str.append(IndDecimalFormat(deci.format(totalcostprice)));
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					Str.append(IndDecimalFormat(deci.format(totalsalesprice)));
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					Str.append(IndDecimalFormat(deci.format(totalincostofsales)));
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					Str.append(IndDecimalFormat(deci.format(totalnetvalue)));
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					Str.append(IndDecimalFormat(deci.format(totalgrossmargin)));
					Str.append("</b></td>\n");

					Str.append("<td align='right'><b>");
					if (count != 1) {
						Str.append(deci.format(totalgrossperc / (count - 1)));
					} else {
						Str.append(deci.format(totalgrossperc / (count)));

					}
					Str.append("</b></td>\n");

					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table >");
				}
				crs.close();
			} else {
				Str.append("<center><b><font color=red>No Stock Gross Margin Data Found!</font></b></center>");
			}
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

	public String PopulateGroupBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=cat_id").append(StrSelectdrop("cat_id", dr_groupby)).append(">Category</option>\n");
		Str.append("<option value=brand_id").append(StrSelectdrop("brand_id", dr_groupby)).append(">Brands</option>\n");
		return Str.toString();
	}
	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Item</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Category</option>\n");
		// Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Type</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Branch</option>\n");
		return Str.toString();
	}
}
