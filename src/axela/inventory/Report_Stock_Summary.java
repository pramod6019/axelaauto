package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_Stock_Summary extends Connect {

	public String SummaryHTML = "", no_Summarydata = "", StrHTML = "", list = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String StrSql = "";
	public String BranchAccess = "", ExeAccess = "", emp_all_exe = "";
	public String msg = "";
	public String[] brand_ids, region_ids, branch_ids, model_ids, location_ids, cat_ids;
	public String brand_id = "", region_id = "", branch_id = "", model_id = "", models = "", location_id = "", cat_id = "";
	public String dr_groupby = "0";
	public String go = "", search = "", itemsearch = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String dr_branch_id = "0";
	public String targetstarttime = "";
	public String all = "";
	// public String QueryString = "";
	public int recperpage = 100;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String targetendtime = "";
	public String starttime = "", start_time = "";
	public String PageNaviStr = "", PageNavi = "";
	public String endtime = "", end_time = "";
	public String voucher_branch_id = "", export = "", exportcount = "", dr_totalby = "";
	DecimalFormat deci = new DecimalFormat("##.##");

	public MIS_Check mischeck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			PageNavi = PadQuotes(request.getParameter("PageNavi"));
			SOP("PageCurrents--------" + PageCurrents);
			go = PadQuotes(request.getParameter("submit_button"));
			search = PadQuotes(request.getParameter("search"));
			list = PadQuotes(request.getParameter("list"));
			itemsearch = PadQuotes(request.getParameter("itemsearch"));
			export = PadQuotes(request.getParameter("export"));
			// SOP("export==" + export);
			all = PadQuotes(request.getParameter("all"));
			exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
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
				if (!search.equals("") && search.length() < 4) {
					msg = "Item Name should be more than 3 characters";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					if ((list.equals("yes") && (!search.equals("") || go.equals("Go"))) || PageNavi.equals("yes")) {
						StrHTML = TotalStockDetails();
					} else if (go.equals("Go") && !list.equals("yes")) {
						SummaryHTML = TotalStockDetails();
					}
					if (SummaryHTML.equals("") || (SummaryHTML.equals("") && list.equals("yes"))) {
						no_Summarydata = ("<center><b><font color=red>No Stock Summary Details Found!</font></b></center>");
					}
				}
				// SOP("SummaryHTML----------" + SummaryHTML);
			}
			if (export.equals("Export")) {
				GetValues(request, response);
				CheckForm();
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				} else if (msg.equals("") && ReturnPerm(comp_id, "emp_export_access", request).equals("1")) {
					StockSummaryExportDetails(request, response);
				}
				else {
					response.sendRedirect(AccessDenied());
				}
			}
		}

	}
	public void StockSummaryExportDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT COALESCE (item_id, '0') AS 'ID', COALESCE (item_name, '') AS 'ItemName',"
					+ " COALESCE (item_code, '') AS 'ItemCode',"
					+ " COALESCE (cat_name, '') AS 'CategoryName',"
					+ " COALESCE ( dtStock.OpeningBalanaceQty, 0 ) AS OpeningBalanaceQty,"
					+ " COALESCE ( dtStock.OpeningBalanaceAmt, 0 ) AS OpeningBalanaceAmt,"
					+ " COALESCE ( dtStock.InwardsQty, 0 ) AS InwardsQty,"
					+ " COALESCE ( dtStock.InwardsAmt, 0 ) AS InwardsAmt ,"
					+ " COALESCE ( dtStock.OutwardsQty, 0 ) AS OutwardsQty ,"
					+ " COALESCE ( dtStock.OutwardsAmt, 0 ) AS OutwardsAmt,"
					+ " COALESCE ( (dtStock.OpeningBalanaceQty + dtStock.InwardsQty - dtStock.OutwardsQty), 0 ) AS ClosingBalanaceQty,"
					+ " COALESCE (((dtStock.OpeningBalanaceQty + dtStock.InwardsQty - dtStock.OutwardsQty)* dtStock.price_amt), 0 ) AS ClosingBalanaceAmt"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id";

			StrSql += " INNER JOIN ( SELECT vouchertrans.vouchertrans_item_id,"
					+ "	COALESCE(mainpurchaseprice.price_amt, 0) AS price_amt,"
					+ " ( SUM( IF ( vouchertype_id = 1 AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8),"
					+ " vouchertrans.vouchertrans_qty, 0 ) )"// OpenStockAmount
					+ " + SUM( IF ( vouchertype_id IN (2, 3, 20, 23) AND voucherclass_inventory_traffic = 1 "// OpenINwardQTY
					+ " AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) )"
					+ " - SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"// OpenOutwardQTY
					+ " AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) ) AS OpeningBalanaceQty,"

					+ " ( SUM( IF ( vouchertype_id = 1 AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8),"
					+ " vouchertrans.vouchertrans_qty, 0 ) )"// OpenStockAmount
					+ " + SUM( IF ( vouchertype_id IN (2, 3, 20, 23) AND voucherclass_inventory_traffic = 1 "// OpenINwardQTY
					+ " AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) )"
					+ " - SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"// OpenOutwardQTY
					+ " AND SUBSTR(voucher_date, 1, 8) <  SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) )"
					+ "  )* mainpurchaseprice.price_amt AS OpeningBalanaceAmt,"// OpenOutwardAmount

					+ " SUM( IF ( vouchertype_id IN (1, 2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// INwardAmountQty
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS InwardsQty,"

					+ " SUM( IF ( vouchertype_id IN (1, 2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// INwardAmount
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),"
					+ " (vouchertrans.vouchertrans_qty * COALESCE(mainpurchaseprice.price_amt, 0)), 0 ) )AS InwardsAmt,"

					+ " SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"// OutwardQty
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS OutwardsQty,"

					+ " SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"// OutwardAmount
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),"
					+ " (vouchertrans.vouchertrans_qty * vouchertrans.vouchertrans_price)"
					+ " - COALESCE(vouchertrans.vouchertrans_discountamount, 0) , 0 ) ) AS OutwardsAmt"

					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans vouchertrans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans.vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "

					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price mainpurchaseprice ON mainpurchaseprice.price_item_id = vouchertrans.vouchertrans_item_id"

					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans.vouchertrans_location_id "
					+ " WHERE 1=1"
					+ "	AND voucher_active = '1'"
					+ " AND vouchertrans.vouchertrans_rowcount != 0"
					+ " AND vouchertrans.vouchertrans_option_id = 0"
					+ " AND mainpurchaseprice.price_id = ("
					+ " SELECT price_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE SUBSTR(price_effective_from, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " AND price_active = 1 "
					+ " AND price_rateclass_id = 6 "
					+ " AND price_item_id = vouchertrans.vouchertrans_item_id "
					+ " ORDER BY price_effective_from"
					+ " DESC LIMIT 1 )"

					+ " GROUP BY vouchertrans_item_id ) AS dtStock ON dtStock.vouchertrans_item_id = item_id";

			StrSql += " WHERE 1=1";
			if (!search.equals("")) {
				StrSql += " AND ( item_name LIKE ('%" + search + "%')"
						+ " OR item_id LIKE ('%" + search + "%')"
						+ " OR item_code LIKE ('%" + search + "%'))";
			}
			StrSql += StrSearch.replace("voucher_branch_id", "branch_id")
					+ " AND item_type_id NOT IN (1, 4)";
			if (dr_totalby.equals("1")) {
				StrSql += " GROUP BY item_id "
						+ " ORDER BY item_name";
			} else if (dr_totalby.equals("2")) {
				StrSql += " GROUP BY item_cat_id "
						+ " ORDER BY cat_name";
			} else {
				StrSql += " GROUP BY item_id "
						+ " ORDER BY item_name";
			}
			StrSql += " LIMIT 10000";
			SOP("StrSql==StockSummaryExportDetails==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				new ExportToXLSX().Export(request, response, crs, "StockSummaryDetails", comp_id);
			} else {
				msg = "No Records To Export!";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		SOP("brand_id===========" + brand_id);
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		model_id = RetrunSelectArrVal(request, "dr_model");
		models = model_id;
		model_ids = request.getParameterValues("dr_model");
		location_id = RetrunSelectArrVal(request, "dr_location");
		location_ids = request.getParameterValues("dr_location");
		cat_id = RetrunSelectArrVal(request, "dr_cat");
		cat_ids = request.getParameterValues("dr_cat");
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

	public String TotalStockDetails() {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		double currentstock = 0.0;
		double currentstockamt = 0.0;
		double inwardstock = 0.0;
		double inwardstockamt = 0.0;
		double outwardstock = 0.0;
		double outwardstockamt = 0.0;
		double closestock = 0.0;
		double closestockamt = 0.0;
		int count = 1;
		double totalcurrstock = 0.0;
		double totalcurrstockamt = 0.0;
		double totalincomingstock = 0.0;
		double totalincomingstockamt = 0.0;
		double totaloutgoingstock = 0.0;
		double totaloutgoingstockamt = 0.0;
		double totalclosingstock = 0.0;
		double totalclosingstockamt = 0.0;
		double currstock = 0.0;
		double currstockamt = 0.0;
		double incomingstock = 0.0;
		double incomingstockamt = 0.0;
		double outgoingstock = 0.0;
		double outgoingstockamt = 0.0;
		double closingstock = 0.0;
		double closingstockamt = 0.0;
		String catname = "";
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		int TotalRecords;
		String PageURL;
		SOP("StrSearch" + StrSearch);
		SOP("cat_id==" + cat_id.trim().length());
		try {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT COALESCE (item_id, '0') AS 'item_id',"
					+ " COALESCE (item_name, '') AS 'item_name',"
					+ " COALESCE (item_code, '') AS 'item_code',"
					+ " COALESCE (cat_name, '') AS 'cat_name',"
					+ "	COALESCE(mainpurchaseprice.price_amt, 0) AS price_amt,"

					+ " @openstock := SUM( IF ( vouchertype_id = 1 AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8),"
					+ " vouchertrans.vouchertrans_qty, 0 ) ) AS openstock,"// OpenstockQty

					+ " @openstockamt := SUM( IF ( vouchertype_id = 1 AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8),"
					+ " (vouchertrans.vouchertrans_qty * COALESCE(mainpurchaseprice.price_amt, 0)), 0 ) ) AS openstockamt,"// OpenstockAmount

					+ " @openinwardstock := SUM( IF ( vouchertype_id IN (2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// OpenINwardQty
					+ " AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS openinwardstock,"

					+ " @openinwardstockamt := SUM( IF ( vouchertype_id IN (2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// OpenInwardAmount
					+ " AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8), (vouchertrans.vouchertrans_qty * COALESCE(mainpurchaseprice.price_amt, 0)), 0 ) ) AS openinwardstockamt,"

					+ " @openoutwardstock := SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2 "// OpenOutwardQty
					+ "AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS openoutwardstock,"

					+ " @openoutwardstockamt := SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"
					+ " AND SUBSTR(voucher_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8),"
					+ " (vouchertrans.vouchertrans_qty * vouchertrans.vouchertrans_price) - COALESCE(vouchertrans.vouchertrans_discountamount, 0), 0 ) ) AS openoutwardstockamt,"// OpenOutwardAmount

					+ " @instock := SUM( IF ( vouchertype_id IN (1, 2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// INwardAmountQty
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS incomingstock,"

					+ " @instockamt := SUM( IF ( vouchertype_id IN (1, 2, 3, 20, 23) AND voucherclass_inventory_traffic = 1"// INwardAmount
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), (vouchertrans.vouchertrans_qty * COALESCE(mainpurchaseprice.price_amt, 0)), 0 ) ) AS incomingstockamt,"

					+ " @outstock := SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vouchertrans.vouchertrans_qty, 0 ) ) AS outgoingstock,"// OutwardQty

					+ " @outgoingstockamt := SUM( IF ( vouchertype_id IN (6, 7, 24) AND voucherclass_inventory_traffic = 2"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),"
					+ " (vouchertrans.vouchertrans_qty * vouchertrans.vouchertrans_price) - COALESCE(vouchertrans.vouchertrans_discountamount, 0) , 0 ) ) AS outgoingstockamt";// OutwardAmount
			if (dr_totalby.equals("2")) {
				CountSql = "SELECT COUNT(DISTINCT(item_cat_id))";
			} else {
				CountSql = "SELECT COUNT(DISTINCT(item_id))";

			}
			SqlJoin += " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans vouchertrans ON vouchertrans.vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans.vouchertrans_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price mainpurchaseprice ON mainpurchaseprice.price_item_id = item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans.vouchertrans_location_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_cat ON cat_id = item_cat_id ";

			SqlJoin += " WHERE 1 = 1";

			SqlJoin += " AND voucher_active = '1'"
					+ " AND vouchertrans.vouchertrans_rowcount != 0"
					+ " AND vouchertrans.vouchertrans_option_id = 0";

			if (!search.equals("")) {
				SqlJoin += " AND ( item_name LIKE ('%" + search + "%')"
						+ " OR item_id LIKE ('%" + search + "%')"
						+ " OR item_code LIKE ('%" + search + "%'))";
			}
			SqlJoin += " AND item_type_id NOT IN (1, 4)"
					+ "	AND item_active = '1'"
					+ " AND mainpurchaseprice.price_id = ("
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
				StrSql += StrSearch + " GROUP BY item_id"
						+ " ORDER BY item_name";
			} else if (dr_totalby.equals("2")) {
				StrSql += StrSearch + " GROUP BY item_id"
						+ " ORDER BY cat_name";
			} else {
				StrSql += StrSearch + " GROUP BY item_id"
						+ " ORDER BY item_name";
			}

			CountSql += StrSearch;

			SOP("CountSql====" + CountSql);
			TotalRecords = Integer.parseInt(CNumeric(PadQuotes(ExecuteQuery(CountSql))));
			SOP("TotalRecords=====" + TotalRecords);

			if (TotalRecords != 0) {

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
				if (!dr_totalby.equals("2")) {
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				}
				SOP("StrSql==TotalStockDetails==" + StrSql);
				crs = processQuery(StrSql, 0);
				count = StartRec;
				currentstock = 0.0;
				currentstockamt = 0.0;
				inwardstock = 0.0;
				inwardstockamt = 0.0;
				outwardstock = 0.0;
				outwardstockamt = 0.0;
				closestock = 0.0;
				closestockamt = 0.0;
				currstock = 0.0;
				currstockamt = 0.0;
				incomingstock = 0.0;
				incomingstockamt = 0.0;
				outgoingstock = 0.0;
				outgoingstockamt = 0.0;
				closingstock = 0.0;
				closingstockamt = 0.0;
				if (crs.isBeforeFirst()) {
					Str.append("<center><div>" + PageNaviStr + "</div></center>");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" style=\"border-collapse:unset\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th>#<br><br></th>\n");
					if (dr_totalby.equals("2")) {
						Str.append("<th>Category<br><br></th>\n");
					} else {
						Str.append("<th>Item<br><br></th>\n");
					}
					Str.append("<th data-hide=\"phone\"><b>Opening Balance</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Inwards</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Outwards</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
					Str.append("<th data-hide=\"phone\"><b>Closing Balance</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>\n");
					System.out.println("cat_id.length()==" + cat_id.length());
					if (dr_totalby.equals("2")) {
						count = 1;
						while (crs.next()) {

							if (catname.equals("")) {
								catname = crs.getString("cat_name");
							}
							if (catname.equals(crs.getString("cat_name"))) {
								currentstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock")
										- crs.getDouble("openoutwardstock"));
								currentstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock")
										- crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt"));
								inwardstock += (crs.getDouble("incomingstock"));
								inwardstockamt += (crs.getDouble("incomingstockamt"));
								outwardstock += (crs.getDouble("outgoingstock"));
								outwardstockamt += (crs.getDouble("outgoingstockamt"));
								closestock += (((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")));
								closestockamt += ((((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt")));
								currstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"));
								currstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt"));
								incomingstock += (crs.getDouble("incomingstock"));
								incomingstockamt += (crs.getDouble("incomingstockamt"));
								outgoingstock += (crs.getDouble("outgoingstock"));
								outgoingstockamt += (crs.getDouble("outgoingstockamt"));
								closingstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock"));
								closingstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt");
							}
							else {
								System.out.println("catname ==" + catname);
								Str.append("<tbody><tr>\n");
								Str.append("<td style='text-align:center'>");
								Str.append(count);
								Str.append("</td>\n");
								Str.append("<td>");
								Str.append(catname);
								Str.append("</td>\n");
								// opeingbal
								Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
								Str.append(currentstock);
								Str.append("</span><span style='float: right;'>\n");
								Str.append(IndDecimalFormat(deci.format(currentstockamt)));
								Str.append("</span></td>\n");
								// inwards
								Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
								Str.append(inwardstock);
								Str.append("</span><span style='float: right;'>\n");
								Str.append(IndDecimalFormat(deci.format(inwardstockamt)));
								Str.append("</span></td>\n");
								// outwards
								Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
								Str.append(outwardstock);
								Str.append("</span><span style='float: right;'>\n");
								Str.append(IndDecimalFormat(deci.format(outwardstockamt)));
								Str.append("</span></td>\n");
								// closingbal
								Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
								Str.append(closestock);
								Str.append("</span><span style='float: right;'>\n");
								Str.append(IndDecimalFormat(deci.format(closestockamt)));
								Str.append("</span></td>\n");
								Str.append("</tr>\n");
								totalcurrstock += currstock;
								totalcurrstockamt += currstockamt;
								totalincomingstock += incomingstock;
								totalincomingstockamt += incomingstockamt;
								totaloutgoingstock += outgoingstock;
								totaloutgoingstockamt += outgoingstockamt;
								totalclosingstock += closingstock;
								totalclosingstockamt += closingstockamt;
								catname = crs.getString("cat_name");
								count++;
								currentstock = 0.0;
								currentstockamt = 0.0;
								inwardstock = 0.0;
								inwardstockamt = 0.0;
								outwardstock = 0.0;
								outwardstockamt = 0.0;
								closestock = 0.0;
								closestockamt = 0.0;
								currstock = 0.0;
								currstockamt = 0.0;
								incomingstock = 0.0;
								incomingstockamt = 0.0;
								outgoingstock = 0.0;
								outgoingstockamt = 0.0;
								closingstock = 0.0;
								closingstockamt = 0.0;
								currentstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock")
										- crs.getDouble("openoutwardstock"));
								currentstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock")
										- crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt"));
								inwardstock += (crs.getDouble("incomingstock"));
								inwardstockamt += (crs.getDouble("incomingstockamt"));
								outwardstock += (crs.getDouble("outgoingstock"));
								outwardstockamt += (crs.getDouble("outgoingstockamt"));
								closestock += (((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")));
								closestockamt += ((((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt")));
								currstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"));
								currstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt"));
								incomingstock += (crs.getDouble("incomingstock"));
								incomingstockamt += (crs.getDouble("incomingstockamt"));
								outgoingstock += (crs.getDouble("outgoingstock"));
								outgoingstockamt += (crs.getDouble("outgoingstockamt"));
								closingstock += (crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock"));
								closingstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
										+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt");

							}
						}

					} else {
						while (crs.next()) {
							Str.append("<tbody><tr>\n");
							Str.append("<td style='text-align:center'>");
							Str.append(count++);
							Str.append("</td>\n");
							Str.append("<td>");
							if (dr_totalby.equals("2")) {
								Str.append(crs.getString("cat_name"));
							} else {
								Str.append(crs.getString("item_name"));
								if (!crs.getString("item_code").equals("")) {
									Str.append(" ( " + crs.getString("item_code") + " )");
								}
							}
							Str.append("</td>\n");
							// opeingbal
							Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
							Str.append(crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"));
							Str.append("</span><span style='float: right;'>\n");
							Str.append(IndDecimalFormat(deci.format((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt"))));
							Str.append("</span></td>\n");
							// inwards
							Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
							Str.append(crs.getString("incomingstock"));
							Str.append("</span><span style='float: right;'>\n");
							Str.append(IndDecimalFormat(deci.format(crs.getDouble("incomingstockamt"))));
							Str.append("</span></td>\n");
							// outwards
							Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
							Str.append(crs.getString("outgoingstock"));
							Str.append("</span><span style='float: right;'>\n");
							System.out.println("159===" + crs.getDouble("outgoingstockamt"));
							Str.append(IndDecimalFormat(deci.format(crs.getDouble("outgoingstockamt"))));
							Str.append("</span></td>\n");
							// closingbal
							Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
							Str.append((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
									+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock"));
							Str.append("</span><span style='float: right;'>\n");
							Str.append(IndDecimalFormat(deci.format(((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
									+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt"))));
							Str.append("</span></td>\n");
							Str.append("</tr>\n");
							// total calculation
							totalcurrstock += crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock");
							totalcurrstockamt += (crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")) * crs.getDouble("price_amt");
							totalincomingstock += crs.getDouble("incomingstock");
							totalincomingstockamt += crs.getDouble("incomingstockamt");
							totaloutgoingstock += crs.getDouble("outgoingstock");
							totaloutgoingstockamt += crs.getDouble("outgoingstockamt");
							totalclosingstock += crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock")
									+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock");
							totalclosingstockamt += ((crs.getDouble("openstock") + crs.getDouble("openinwardstock") - crs.getDouble("openoutwardstock"))
									+ crs.getDouble("incomingstock") - crs.getDouble("outgoingstock")) * crs.getDouble("price_amt");

						}
					}
					if (cat_id.length() <= 2 && dr_totalby.equals("2")) {

						Str.append("<tbody><tr>\n");
						Str.append("<td style='text-align:center'>");
						Str.append(count);
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append(catname);
						Str.append("</td>\n");
						// opeingbal
						Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
						Str.append(currentstock);
						Str.append("</span><span style='float: right;'>\n");
						Str.append(IndDecimalFormat(deci.format(currentstockamt)));
						Str.append("</span></td>\n");
						// inwards
						Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
						Str.append(inwardstock);
						Str.append("</span><span style='float: right;'>\n");
						Str.append(IndDecimalFormat(deci.format(inwardstockamt)));
						Str.append("</span></td>\n");
						// outwards
						Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
						Str.append(outwardstock);
						Str.append("</span><span style='float: right;'>\n");
						Str.append(IndDecimalFormat(deci.format(outwardstockamt)));
						Str.append("</span></td>\n");
						// closingbal
						Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
						Str.append(closestock);
						Str.append("</span><span style='float: right;'>\n");
						Str.append(IndDecimalFormat(deci.format(closestockamt)));
						Str.append("</span></td>\n");
						Str.append("</tr>\n");
						totalcurrstock += currstock;
						totalcurrstockamt += currstockamt;
						totalincomingstock += incomingstock;
						totalincomingstockamt += incomingstockamt;
						totaloutgoingstock += outgoingstock;
						totaloutgoingstockamt += outgoingstockamt;
						totalclosingstock += closingstock;
						totalclosingstockamt += closingstockamt;
					}
					// Total
					Str.append("<tr>");
					Str.append("<td></td>");
					Str.append("<td style='text-align: right;'><b>");
					Str.append(" Total:</b></td>");

					Str.append("<td><b><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(totalcurrstock);
					Str.append("</span><span style='float: right;'>\n");
					Str.append(IndDecimalFormat(deci.format(totalcurrstockamt)));
					Str.append("</span></b></td>\n");

					Str.append("<td><b><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(totalincomingstock);
					Str.append("</span><span style='float: right;'>\n");
					System.out.println("=158=" + deci.format(totalincomingstockamt));
					Str.append(IndDecimalFormat(deci.format(totalincomingstockamt)));
					Str.append("</span></b></td>\n");

					Str.append("<td><b><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(totaloutgoingstock);
					Str.append("</span><span style='float: right;'>\n");
					Str.append(IndDecimalFormat(deci.format(totaloutgoingstockamt)));
					Str.append("</span></b></td>\n");

					Str.append("<td><b><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(totalclosingstock);
					Str.append("</span><span style='float: right;'>\n");
					Str.append(IndDecimalFormat(deci.format(totalclosingstockamt)));
					Str.append("</span></b></td>\n");

					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table >");
					Str.append("<center><div>" + PageNaviStr + "</div></center>");
				}
				crs.close();
			} else {
				Str.append("<center><b><font color=red>No Stock Summary Data Found!</font></b></center>");
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

	public String PopulateInventoryLocation(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location";
			if (!branch_id.equals("")) {
				StrSql += " WHERE location_branch_id IN ( " + branch_id + ")";
			}
			StrSql += " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql==loc==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location\" id=\"dr_location\" multiple=multiple class='form-control multiselect-dropdown'>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("location_id"), location_ids));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Item</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Category</option>\n");
		return Str.toString();
	}
}
