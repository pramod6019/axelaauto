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

public class Report_Vehicle_Stock_Ageing extends Connect {
	
	public String AgeingHTML = "", no_Ageingdata = "", WholesaleHTML = "", ThismonthsaleHTML = "", PrevmonthsaleHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String StrSql = "";
	public String BranchAccess = "", ExeAccess = "", emp_all_exe = "";
	public String msg = "";
	public String[] brand_ids, region_ids, branch_ids, model_ids, item_ids, fueltype_ids, option_ids, pending_deliverys, status_ids;
	public String brand_id = "", region_id = "", branch_id = "", model_id = "", models = "", item_id, fueltype_id = ""
			, option_id = "", pending_delivery = "", status_id = "";
	public String dr_groupby = "0";
	public String go = "";
	public String dr_branch_id = "0";
	public String targetstarttime = "";
	public String targetendtime = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String vehstock_branch_id = "";
	public String delstatus_id = "";
	public int grandtotal = 0;
	public String chart_data = "", filter = "";
	DecimalFormat deci = new DecimalFormat("##.##");
	
	public MIS_Check1 mischeck = new MIS_Check1();
	public String SearchURL = "report-vehicle-stock-ageing.jsp?";
	
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
				// TotalStockDetails();
			}
			GetValues(request, response);
			CheckForm();
			// // SOP("go---------" + go);
			// if (!comp_id.equals("0"))
			// {
			// emp_id = CNumeric(GetSession("emp_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			// try {
			// AgeingHTML = StockAgeing();
			// } catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			// }
			// }
			// // SOP("Msg===" + msg);
			// // SOP("From date=====" + from_date);
			// // SOP("To date=====" + to_date);
			
			if (go.equals("")) {
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());
				msg = "";
			}
			
			if (go.equals("Go")) {
				
				StrSearch = BranchAccess.replace("branch_id", "vehstock_branch_id"); // + " " + ExeAccess;
				if (!brand_id.equals("")) {
					StrSearch += " AND model_brand_id IN (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND vehstock_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_region_id IN (" + region_id + "))";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND vehstock_branch_id IN (" + branch_id + ")";
				}
				if (!model_id.equals("")) {
					StrSearch += " AND model_id IN (" + model_id + ")";
				}
				if (!item_id.equals("")) {
					StrSearch += " AND item_id IN (" + item_id + ")";
				}
				if (!fueltype_id.equals("")) {
					StrSearch += " AND item_fueltype_id IN (" + fueltype_id + ")";
				}
				if (!option_id.equals("")) {
					StrSearch = StrSearch + " AND option_id IN ( " + option_id + ")";
				}
				if (!pending_delivery.contains("1,2")) {
					if (pending_delivery.contains("1")) {
						StrSearch = StrSearch + " AND vehstock_delstatus_id != 6";
					}
					if (pending_delivery.contains("2")) {
						StrSearch = StrSearch + " AND vehstock_delstatus_id = 6";
					}
				}
				
				if (!status_id.equals("")) {
					StrSearch += " AND vehstock_status_id IN (" + status_id + ")";
				}
				
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					AgeingHTML = StockAgeing(comp_id);
					TotalStockDetails();
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
		model_id = RetrunSelectArrVal(request, "dr_model");
		models = model_id;
		model_ids = request.getParameterValues("dr_model");
		item_id = RetrunSelectArrVal(request, "dr_variant");
		item_ids = request.getParameterValues("dr_variant");
		fueltype_id = RetrunSelectArrVal(request, "dr_fueltype_id");
		fueltype_ids = request.getParameterValues("dr_fueltype_id");
		option_id = RetrunSelectArrVal(request, "dr_color");
		option_ids = request.getParameterValues("dr_color");
		pending_delivery = RetrunSelectArrVal(request, "dr_pending_delivery_id");
		dr_groupby = PadQuotes(request.getParameter("dr_groupby"));
		status_id = RetrunSelectArrVal(request, "dr_status");
		status_ids = request.getParameterValues("dr_status");
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
	}
	
	public String StockAgeing(String comp_id) {
		int total_purchased = 0;
		int total_intransit = 0;
		int total_order_placed = 0;
		int total_opening_stock = 0;
		int total_closing_stock = 0;
		int total_0_30days = 0;
		int total_31_50dys = 0;
		int total_51_75days = 0;
		int total_76_100days = 0;
		int total_101days = 0;
		int total_allocated = 0;
		int total_non_allocated = 0;
		double total_tat = 0;
		double total_invamt_purchased = 0;
		double total_intransit_amt = 0;
		double total_invamt_orderplaced = 0;
		double total_openingstock_amt = 0;
		double total_closingstock_amt = 0;
		double total_invamt_0_30days = 0;
		double total_invamt_31_50dys = 0;
		double total_invamt_51_75days = 0;
		double total_invamt_76_100days = 0;
		double total_invamt_101days = 0;
		StringBuilder Str = new StringBuilder();
		
		try {
			String now = ToLongDate(kknow()).substring(0, 8) + "000000";
			String strqwert1 = " DATEDIFF('" + starttime + "', vehstock_invoice_date) >= qwerty1";
			String strqwert2 = " AND DATEDIFF('" + starttime + "', vehstock_invoice_date) <= qwerty2";
			String strqwert3 = " AND sold.so_vehstock_id IS NULL";
			String strstockdays = " COALESCE(COUNT(DISTINCT CASE WHEN "
					+ strqwert1 + strqwert2 + strqwert3 + " THEN vehstock_id END),0)";
			String strinvoiceamt = " COALESCE(SUM(CASE WHEN "
					+ strqwert1 + strqwert2 + strqwert3 + " THEN vehstock_invoice_amount END) ,0)";
			// // SOP("strinvoiceamt-----------" + strinvoiceamt);
			
			StrSql = "SELECT model_id, model_brand_id, vehstock_id, model_name, item_id, item_name, COALESCE(option_id,'') AS option_id, "
					+ "COALESCE(option_name,'') AS option_name, model_brand_id,";
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += " brand_name,";
			}
			StrSql += " '111' AS 'openingstock', "
					+ " '222' AS 'closingstock', ";
			
			StrSql += " COALESCE (orderplaced.orderplaced_count, 0) AS 'orderplaced',";
			// ===============================
			// Uncomment to revert back
			
			StrSql += " COALESCE (COUNT(DISTINCT CASE WHEN SUBSTR(vehstock_invoice_date, 1, 6) =SUBSTR('" + starttime + "', 1, 6) THEN vehstock_id END ), 0 ) AS 'purchased',";
			// StrSql += " 0 AS 'purchased',";
			// Intransit
			StrSql += " COALESCE (COUNT(DISTINCT CASE	WHEN vehstock_delstatus_id = 2 THEN	vehstock_id	END),0) AS 'intransit',";
			// ==================================
			// ///////0-30days//////////////
			StrSql += strstockdays.replace("qwerty1", "0").replace("qwerty2", "30")
					+ " AS '0-30days',"
					// ///////31-50days//////////////
					+ strstockdays.replace("qwerty1", "31").replace("qwerty2", "50")
					+ " AS '31-50days',"
					// ///////51-75days//////////////
					+ strstockdays.replace("qwerty1", "51").replace("qwerty2", "75")
					+ " AS '51-75days',"
					// ///////76-100days//////////////
					+ strstockdays.replace("qwerty1", "76").replace("qwerty2", "100")
					+ " AS '76-100days',"
					// ///////101days//////////////
					+ strstockdays.replace("qwerty1", "101").replace(strqwert2, "")
					+ " AS '101days',"
					// ////openingstockamt//////
					+ " '0.00' AS 'openingstockamt', "
					// ////closingstockamt//////
					+ " '0.00' AS 'closingstockamt', "
					// ////orderplacedamt//////
					// + " COALESCE ( SUM( CASE WHEN vehstock_invoice_date = '' THEN vehstock_invoice_amount END ), 0 ) AS 'orderplacedamt',"
					// ///////Stock_invoice_amt //////////////
					+ " COALESCE ( SUM( CASE WHEN SUBSTR(vehstock_invoice_date, 1, 6) =SUBSTR('"
					+ starttime
					+ "', 1, 6) THEN vehstock_invoice_amount END ), 0 ) AS 'purchasedamt',"
					+ " COALESCE (SUM(CASE WHEN vehstock_delstatus_id = 2 THEN	vehstock_invoice_amount	END),0) AS 'intransitamt',"
					// ///////0-30days ///////////
					+ strinvoiceamt.replace("qwerty1", "0").replace("qwerty2", "30")
					+ " AS '0-30daysamt',"
					// ///////31-50days//////////////
					+ strinvoiceamt.replace("qwerty1", "31").replace("qwerty2", "50")
					+ " AS '31-50daysamt',"
					// ///////51-75days//////////////
					+ strinvoiceamt.replace("qwerty1", "51").replace("qwerty2", "75")
					+ " AS '51-75daysamt',"
					// ///////76-100days//////////////
					+ strinvoiceamt.replace("qwerty1", "76").replace("qwerty2", "100")
					+ " AS '76-100daysamt',"
					// ///////101days//////////////
					+ strinvoiceamt.replace("qwerty1", "101").replace(strqwert2, "")
					+ " AS '101daysamt', "
					// For OpenStock
					+ " COALESCE (SUM(IF(soopen.so_retail_date = ''"
					+ " AND soopen.so_retail_date <= "
					+ starttime
					+ ", 1, 0)),0) AS allocated, "
					+ " COALESCE (notallocated,0) AS notallocated,"
					
					+ " COALESCE (SUM(IF (vehstock_invoice_date != '' "
					+ " AND soopen.so_retail_date >  " + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)) + ","
					+ " DATEDIFF(soopen.so_retail_date,	vehstock_invoice_date	), 0)) / SUM(IF(vehstock_invoice_date != ''"
					+ " AND soopen.so_retail_date > " + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 0, 0, -6, 0)) + ", 1, 0)),	0) AS TAT"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += " INNER JOIN axela_brand ON brand_id = model_brand_id";
			}
			
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id "
					+ " AND option_optiontype_id = 1"
					// so left join
					+ " LEFT JOIN (SELECT so_vehstock_id, so_retail_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE 1=1"
					+ " AND so_retail_date != ''"
					// + " AND so_retail_date <= " + starttime
					+ " AND so_active = 1 ) AS sold ON sold.so_vehstock_id = vehstock_id"
					// left join for tat, allocated and notallocated
					+ " LEFT JOIN (SELECT so_vehstock_id, so_retail_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE 1=1"
					+ " AND so_vehstock_id != 0"
					+ " AND so_active = 1 ) AS soopen ON soopen.so_vehstock_id = vehstock_id"
					
					// notallocated
					+ " LEFT JOIN (SELECT COUNT(so_id) AS notallocated,";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " model_brand_id AS modelbrnd";
			} else {
				StrSql += " model_id AS modelid";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE 1=1"
					+ " AND so_active = 1"
					+ " AND so_retail_date = ''"
					+ " AND so_vehstock_id = 0";
			// SOP("Exeacess==" + ExeAccess);
			if (!ExeAccess.equals("")) {
				StrSql += ExeAccess.replace("emp_id", "so_emp_id");
			}
			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ")";
			}
			if (!branch_id.equals("")) {
				StrSql += " AND so_branch_id IN (" + branch_id + ")";
			}
			if (!model_id.equals("")) {
				StrSql += " AND model_id IN (" + model_id + ")";
			}
			if (!item_id.equals("")) {
				StrSql += " AND item_id IN (" + item_id + ")";
			}
			if (!fueltype_id.equals("")) {
				StrSql += " AND item_fueltype_id IN (" + fueltype_id + ")";
			}
			
			StrSql += " GROUP BY ";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " modelbrnd";
			} else {
				StrSql += " modelid";
			}
			StrSql += " ) AS soitem ON soitem.";
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += "modelbrnd=";
			} else {
				StrSql += "modelid=";
			}
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += "model_brand_id";
			} else {
				StrSql += "model_id";
			}
			
			// order placed
			StrSql += " LEFT JOIN (SELECT"
					+ " SUM(orderplaced_count) AS orderplaced_count,"
					+ " orderplaced_model_id ";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " ,model_brand_id AS orderplacedmodel_brand_id";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_sales_orderplaced";
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = orderplaced_model_id";
			}
			StrSql += " WHERE 1= 1"
					+ " AND SUBSTR(orderplaced_date, 1,6) =  SUBSTR('" + starttime + "', 1, 6)";
			// + " AND orderplaced_date <= " + endtime + "";
			
			if (dr_groupby.equals("brand_id")) {
				StrSql += " GROUP BY model_brand_id"
						+ " ) AS orderplaced ON orderplaced.orderplacedmodel_brand_id = brand_id";
			} else {
				StrSql += " GROUP BY orderplaced_model_id"
						+ " ) AS orderplaced ON orderplaced.orderplaced_model_id = model_id";
			}
			
			// main filter
			// + " AND sold.so_retail_date != ''"
			// + " AND sold.so_retail_date <= " + starttime + ""
			// + " AND sold.so_active = 1 "
			StrSql += " WHERE 1 = 1 "
					+ StrSearch;
			if (dr_groupby.equals("model_id")) {
				StrSql += " AND model_active = '1' ";
			}
			
			StrSql += " GROUP BY " + dr_groupby;
			
			// if (model_id.contains(",") == false && !model_id.equals("")) {
			// StrSql += ",option_id";
			// }
			// if (model_id.contains(",") == false && !model_id.equals("")) {
			// StrSql += ",item_id";
			// }
			
			StrSql += " ORDER BY";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " brand_name";
			}
			else
				StrSql += " model_name";
			
			// SOP("StrSql---------/----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			StringBuilder singleSelect = null;
			StringBuilder multiSelect = new StringBuilder();
			multiSelect.append("&starttime=").append(starttime)
					.append("&endtime=").append(endtime)
					.append("&brand_ids=").append(brand_id)
					.append("&region_ids=").append(region_id)
					.append("&branch_ids=").append(branch_id)
					.append("&model_ids=").append(model_id)
					.append("&item_ids=").append(item_id)
					.append("&status_ids=").append(status_id)
					.append("&fueltype_ids=").append(fueltype_id)
					.append("&option_ids=").append(option_id)
					.append("&pending_deliverys=").append(pending_delivery)
					.append(" target=_blank");
			if (crs.isBeforeFirst()) {
				
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				if (dr_groupby.equals("model_id")) {
					Str.append("<th data-toggle=\"true\">Model</th>\n");
				}
				if (dr_groupby.equals("brand_id")) {
					Str.append("<th data-toggle=\"true\">Brand</th>\n");
				}
				// Str.append("<th>Opening Stock</th>\n");
				// Str.append("<th>Closing Stock</th>\n");
				Str.append("<th>Order Placed</th>\n");
				Str.append("<th>Purchased</th>\n");
				Str.append("<th>In-Transit</th>\n");
				Str.append("<th>0-30 Days</th>\n");
				Str.append("<th data-hide=\"phone\">31-50 Days</th>\n");
				Str.append("<th data-hide=\"phone\">51-75 Days</th>\n");
				Str.append("<th data-hide=\"phone\">76-100 Days</th>\n");
				Str.append("<th data-hide=\"phone\">101 - > Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
				Str.append("<th data-hide=\"phone\">Allocated</th>\n");
				Str.append("<th data-hide=\"phone\">Not-Allocated</th>\n");
				Str.append("<th data-hide=\"phone\">TAT</th>\n");
				Str.append("</tr></thead>\n");
				crs.last();
				int rowcount = crs.getRow();
				double counttotalamt = 0;
				int count = 0, total = 0;// , grandtotal = 0;
				
				crs.beforeFirst();
				while (crs.next()) {
					singleSelect = new StringBuilder();
					total_opening_stock += crs.getInt("openingstock");
					total_closing_stock += crs.getInt("closingstock");
					total_purchased += crs.getInt("purchased");
					total_order_placed += crs.getInt("orderplaced");
					total_intransit += crs.getInt("intransit");
					total_0_30days += crs.getInt("0-30days");
					total_31_50dys += crs.getInt("31-50days");
					total_51_75days += crs.getInt("51-75days");
					total_76_100days += crs.getInt("76-100days");
					total_101days += crs.getInt("101days");
					
					total_allocated += crs.getInt("allocated");
					total_non_allocated += crs.getInt("notallocated");
					total_tat += crs.getDouble("TAT");
					
					// total_openingstock_amt += crs.getDouble("openingstockamt");
					// total_closingstock_amt += crs.getDouble("closingstockamt");
					// total_invamt_orderplaced += crs.getDouble("orderplacedamt");
					total_invamt_purchased += crs.getDouble("purchasedamt");
					total_intransit_amt += crs.getDouble("intransitamt");
					// // SOP("total_invamt_orderplaced===" + crs.getDouble("orderplacedamt"));
					// // SOP("total_invamt_purchased===" + crs.getDouble("orderamt"));
					
					total_invamt_0_30days += crs.getDouble("0-30daysamt");
					total_invamt_31_50dys += crs.getDouble("31-50daysamt");
					total_invamt_51_75days += crs.getDouble("51-75daysamt");
					total_invamt_76_100days += crs.getDouble("76-100daysamt");
					total_invamt_101days += crs.getDouble("101daysamt");
					
					total = crs.getInt("0-30days") + crs.getInt("31-50days") + crs.getInt("51-75days")
							+ crs.getInt("76-100days") + crs.getInt("101days");
					// // SOP("total=====" + total);
					count++;
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>" + count + "</td>");
					Str.append("<td valign=top align=left>");
					if (dr_groupby.equals("model_id")) {
						if (model_id.contains(",") == false && !model_id.equals("")) {
							Str.append(crs.getString("item_name"));
							Str.append(" - ").append(crs.getString("option_name"));
						} else {
							Str.append(crs.getString("model_name"));
						}
					}
					if (dr_groupby.equals("brand_id")) {
						Str.append(crs.getString("brand_name"));
					}
					Str.append("</td>");
					
					// Group BY Models
					if (dr_groupby.equals("model_id")) {
						// Str.append("<td valign=top align=right>" + crs.getString("openingstock") + "</td>");
						// Str.append("<td valign=top align=right>" + crs.getString("closingstock") + "</td>");
						
						singleSelect.append("model_id=" + crs.getString("model_id"));
						
						if (model_id.contains(",") == false && !model_id.equals("")) {
							singleSelect.append("&item_id=" + crs.getString("item_id"));
						}
						if (model_id.contains(",") == false && !model_id.equals("")) {
							singleSelect.append("&option_id=" + crs.getString("option_id"));
						}
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&orderplaced=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("orderplaced")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&purchased=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("purchased")).append("</a></td>");
						
						Str.append("<td valign=top align=right>");
						Str.append(crs.getInt("intransit")).append("</td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days30=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("0-30days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days50=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("31-50days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days75=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("51-75days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days100=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("76-100days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days101=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("101days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&total=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(total).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&allocated=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("allocated")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&notallocated=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("notallocated")).append("</a></td>");
						
						Str.append("<td valign=top align=right>").append(crs.getString("TAT")).append("</a></td>");
						
						Str.append("</tr>\n");
						grandtotal += total;
					}
					
					// Group BY Brands
					if (dr_groupby.equals("brand_id")) {
						// Str.append("<td valign=top align=right>" + crs.getString("openingstock") + "</td>");
						// Str.append("<td valign=top align=right>" + crs.getString("closingstock") + "</td>");
						
						singleSelect.append("brand_id=" + crs.getString("model_brand_id"));
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&orderplaced=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("orderplaced")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&purchased=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("purchased")).append("</a></td>");
						
						Str.append("<td valign=top align=right>");
						Str.append(crs.getInt("intransit")).append("</td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days30=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("0-30days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days50=yes&");
						Str.append(multiSelect + ">");
						Str.append(crs.getString("31-50days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days75=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("51-75days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days100=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("76-100days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days101=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("101days")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&total=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(total).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&allocated=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("allocated")).append("</a></td>");
						
						Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&notallocated=yes&");
						Str.append(singleSelect).append(multiSelect + ">");
						Str.append(crs.getString("notallocated")).append("</a></td>");
						
						Str.append("<td valign=top align=right>");
						Str.append(crs.getString("TAT")).append("</a></td>");
						
						Str.append("</tr>\n");
						grandtotal += total;
					}
				}
				counttotalamt = total_invamt_0_30days + total_invamt_31_50dys + total_invamt_51_75days
						+ total_invamt_76_100days + total_invamt_101days;
				Str.append("<tr align=center>\n");
				
				// Group BY Models
				if (dr_groupby.equals("model_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_opening_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_closing_stock + "</a></b></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&orderplaced=yes&")
							.append(multiSelect + ">");
					Str.append(total_order_placed).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&purchased=yes&")
							.append(multiSelect + ">");
					Str.append(total_purchased).append("</a></td>");
					
					Str.append("<td valign=top align=right>");
					Str.append(total_intransit).append("</td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days30=yes&")
							.append(multiSelect + ">");
					Str.append(total_0_30days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days50=yes&")
							.append(multiSelect + ">");
					Str.append(total_31_50dys).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days75=yes&")
							.append(multiSelect + ">");
					Str.append(total_51_75days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days100=yes&")
							.append(multiSelect + ">");
					Str.append(total_76_100days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&days101=yes&")
							.append(multiSelect + ">");
					Str.append(total_101days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&total=yes&")
							.append(multiSelect + ">");
					Str.append(grandtotal).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&allocated=yes&")
							.append(multiSelect + ">");
					Str.append(total_allocated).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&model=yes&notallocated=yes&")
							.append(multiSelect + ">");
					Str.append(total_non_allocated).append("</a></td>");
					
					// Str.append("<td valign=top align=right>").append(total_tat / count).append("</a></td>");
					
					Str.append("<td valign=top align=right>").append(unescapehtml(IndDecimalFormat(deci.format((total_tat / count)))))
							.append("</a></td>");
					
				}
				
				// Group BY Brands
				if (dr_groupby.equals("brand_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total :</b></td>");
					
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_opening_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_closing_stock + "</a></b></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&orderplaced=yes&")
							.append(multiSelect + ">");
					Str.append(total_order_placed).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&purchased=yes&")
							.append(multiSelect + ">");
					Str.append(total_purchased).append("</a></td>");
					
					Str.append("<td valign=top align=right>");
					Str.append(total_intransit).append("</td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days30=yes&")
							.append(multiSelect + ">");
					Str.append(total_0_30days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days50=yes&")
							.append(multiSelect + ">");
					Str.append(total_31_50dys).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days75=yes&")
							.append(multiSelect + ">");
					Str.append(total_51_75days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days100=yes&")
							.append(multiSelect + ">");
					Str.append(total_76_100days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&days101=yes&")
							.append(multiSelect + ">");
					Str.append(total_101days).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&total=yes&")
							.append(multiSelect + ">");
					Str.append(grandtotal).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&allocated=yes&")
							.append(multiSelect + ">");
					Str.append(total_allocated).append("</a></td>");
					
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&brand=yes&notallocated=yes&")
							.append(multiSelect + ">");
					Str.append(total_non_allocated).append("</a></td>");
					
					Str.append("<td valign=top align=right>").append(unescapehtml(IndDecimalFormat(deci.format((total_tat / count)))))
							.append("</a></td>");
				}
				
				Str.append("</tr>\n");
				Str.append("<tr align=center>\n");
				
				// Group BY Models
				if (dr_groupby.equals("model_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total Value : </b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_opening_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_closing_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_orderplaced)))));
					// Str.append("</b></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_purchased)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>");
					Str.append(unescapehtml(IndDecimalFormat(deci.format(total_intransit_amt)))).append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_0_30days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_31_50dys)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_51_75days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_76_100days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_101days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>" + unescapehtml(IndDecimalFormat(deci.format((counttotalamt)))) + "</b></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right></td>");
				}
				
				// Group BY Brands
				if (dr_groupby.equals("brand_id")) {
					Str.append("<td valign=top align=right colspan=2><b>Total Value : </b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_opening_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b><a href=\"" + SearchURL + "?filter=yes\">" + total_closing_stock + "</a></b></td>");
					// Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_orderplaced)))));
					// Str.append("</b></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_purchased)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>");
					Str.append(unescapehtml(IndDecimalFormat(deci.format((total_intransit_amt))))).append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_0_30days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_31_50dys)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_51_75days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_76_100days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_invamt_101days)))));
					Str.append("</b></td>");
					Str.append("<td valign=top align=right><b>" + unescapehtml(IndDecimalFormat(deci.format((counttotalamt)))) + "</b></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right></td>");
					Str.append("<td valign=top align=right></td>");
					
				}
				
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				// stockageingdays.put("Opening Stock", total_opening_stock);
				// stockageingdays.put("Closing Stock", total_closing_stock);
				stockageingdays.put("Order Placed", total_order_placed);
				stockageingdays.put("Purchased", total_purchased);
				stockageingdays.put("In-Transit", total_intransit);
				stockageingdays.put("0-30 Days", total_0_30days);
				stockageingdays.put("31-50 Days", total_31_50dys);
				stockageingdays.put("51-75 Days", total_51_75days);
				stockageingdays.put("76-100 Days", total_76_100days);
				stockageingdays.put("101 Days", total_101days);
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
	
	public String TotalStockDetails() {
		StringBuilder Str = new StringBuilder();
		int count = 1;
		int totalcurrstock = 0;
		double totalcurrstockamt = 0;
		int totalincomingstock = 0;
		double totalincomingstockamt = 0;
		int totaloutgoingstock = 0;
		double totaloutgoingstockamt = 0;
		int totalclosingstock = 0;
		double totalclosingstockamt = 0;
		
		try {
			StrSql = " SELECT ";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " brand_id, brand_name,";
			} else {
				StrSql += " model_id, model_name,";
			}
			StrSql += " @currstock := SUM(IF(SUBSTR(vehstock_invoice_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
					+ " AND (so_vehstock_id IS NULL ) ,1,0)) AS currentstock,"
					+ " @currstockamt := SUM(IF(SUBSTR(vehstock_invoice_date, 1, 8) < SUBSTR('" + starttime + "', 1, 8)"
					+ " AND (so_vehstock_id IS NULL ) , vehstock_invoice_amount,0)) AS currstockamt,"
					
					+ " @instock := SUM(IF(vehstock_invoice_date != ''"
					+ " AND SUBSTR(vehstock_invoice_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR(vehstock_invoice_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),1 ,0)) AS incomingstock,"
					+ " @instockamt := SUM(IF(vehstock_invoice_date != '' "
					+ " AND SUBSTR(vehstock_invoice_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
					+ " AND SUBSTR(vehstock_invoice_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vehstock_invoice_amount,0)) AS incomingstockamt,"
					
					+ " @outstock := SUM(IF (SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8),1 ,0)) AS outgoingstock,"
					+ " @outgoingstockamt := SUM(IF (SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8), vehstock_invoice_amount,0)) AS outgoingstockamt";
			
			// + " COALESCE((@currstock + @instock - @outstock),0) AS closingstock,"
			// + " COALESCE((@currstockamt + @instockamt - @outstockamt),0) AS closingstockamt";
			
			StrSql += " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id "
					+ " AND option_optiontype_id = 1";
			if (dr_groupby.equals("brand_id")) {
				StrSql += " INNER JOIN axela_brand ON brand_id = model_brand_id";
			}
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1 "
					+ " AND so_retail_date !=''"
					+ " WHERE 1=1 "
					+ StrSearch
					+ " AND model_active = '1'";
			// SOP("dr_groupby==" + dr_groupby);
			if (dr_groupby.equals("brand_id")) {
				StrSql += " GROUP BY brand_id "
						+ " ORDER BY brand_name";
				
			} else {
				StrSql += " GROUP BY model_id "
						+ " ORDER BY model_name";
			}
			
			// SOPInfo("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#<br><br></th>\n");
				if (dr_groupby.equals("model_id")) {
					Str.append("<th>Model<br><br></th>\n");
				} else {
					Str.append("<th>Brand<br><br></th>\n");
				}
				
				Str.append("<th data-hide=\"phone\"><b>Opening Balance</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Inwards</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Outwards</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Closing Balance</b><br><br><span style='padding-left: 6%; padding-right: 10%'>Quantity</span><span style='float: right;'>Value</span></th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				
				while (crs.next()) {
					Str.append("<tbody><tr>\n");
					Str.append("<td style='text-align:center'>");
					Str.append(count++);
					Str.append("</td>\n");
					Str.append("<td>");
					
					if (dr_groupby.equals("model_id")) {
						Str.append(crs.getString("model_name"));
					} else {
						Str.append(crs.getString("brand_name"));
					}
					Str.append("</td>\n");
					// opeingbal
					Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(crs.getString("currentstock"));
					Str.append("</span><span style='float: right;'>\n");
					Str.append(IndDecimalFormat(deci.format(crs.getDouble("currstockamt"))));
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
					Str.append(IndDecimalFormat(deci.format(crs.getDouble("outgoingstockamt"))));
					Str.append("</span></td>\n");
					// closingbal
					Str.append("<td><span style='padding-left: 35%; padding-right: 10%'>");
					Str.append(crs.getInt("currentstock")
							+ crs.getInt("incomingstock")
							- crs.getInt("outgoingstock"));
					Str.append("</span><span style='float: right;'>\n");
					Str.append(IndDecimalFormat(deci.format(crs.getDouble("currstockamt")
							+ crs.getDouble("incomingstockamt")
							+ crs.getDouble("outgoingstockamt"))));
					Str.append("</span></td>\n");
					Str.append("</tr>\n");
					
					// total calculation
					totalcurrstock += crs.getDouble("currentstock");
					totalcurrstockamt += crs.getDouble("currstockamt");
					totalincomingstock += crs.getDouble("incomingstock");
					totalincomingstockamt += crs.getDouble("incomingstockamt");
					totaloutgoingstock += crs.getDouble("outgoingstock");
					totaloutgoingstockamt += crs.getDouble("outgoingstockamt");
					totalclosingstock += crs.getInt("currentstock")
							+ crs.getInt("incomingstock")
							- crs.getInt("outgoingstock");
					totalclosingstockamt += crs.getDouble("currstockamt")
							+ crs.getDouble("incomingstockamt")
							- crs.getDouble("outgoingstockamt");
					
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
			}
			else {
				Str.append("<center><b><font color=red>No Ageing Data Found!</font></b></center>");
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
		String color[] = {"#0d8edf", "#04d215", "#b0de0e", "#b0de0f", "#fcd202", "#ff9e01", "#ff6600", "#ff0f00"};
		
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
	
	public String PopulateFuelType(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype" + " where 1=1"
					+ " order by fueltype_name";
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
	
	public String PopulatePendingdelivery() {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=dr_pending_delivery_id  multiple=multiple class='form-control multiselect-dropdown' multiselect-dropdown id=dr_pending_delivery_id style=\"padding:10px\">");
		Str.append("<option value=1" + StrSelectdrop("1", pending_delivery) + ">Yes</option> \n");
		Str.append("<option value=2" + StrSelectdrop("2", pending_delivery) + ">No</option> \n");
		Str.append("</select>\n");
		return Str.toString();
		
	}
	
	public String PopulateGroupBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=model_id").append(StrSelectdrop("model_id", dr_groupby)).append(">Models</option>\n");
		Str.append("<option value=brand_id").append(StrSelectdrop("brand_id", dr_groupby)).append(">Brands</option>\n");
		return Str.toString();
	}
	
	private void StockAgeingDetails(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			HttpSession session = request.getSession(true);
			String model = PadQuotes(request.getParameter("model"));
			String brand = PadQuotes(request.getParameter("brand"));
			String brand_id = CNumeric(PadQuotes(RetrunSelectArrVal(request, "brand_id")));
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_ids"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_ids"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_ids"));
			String model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			String model_ids = PadQuotes(RetrunSelectArrVal(request, "model_ids"));
			String item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
			String item_ids = PadQuotes(RetrunSelectArrVal(request, "item_ids"));
			String fueltype_ids = PadQuotes(RetrunSelectArrVal(request, "fueltype_ids"));
			String option_id = CNumeric(PadQuotes(request.getParameter("option_id")));
			String option_ids = PadQuotes(RetrunSelectArrVal(request, "option_ids"));
			String status_ids = PadQuotes(RetrunSelectArrVal(request, "status_ids"));
			String pending_deliverys = PadQuotes(RetrunSelectArrVal(request, "pending_deliverys"));
			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String orderplaced = PadQuotes(request.getParameter("orderplaced"));
			String purchased = PadQuotes(request.getParameter("purchased"));
			String days30 = PadQuotes(request.getParameter("days30"));
			String days50 = PadQuotes(request.getParameter("days50"));
			String days75 = PadQuotes(request.getParameter("days75"));
			String days100 = PadQuotes(request.getParameter("days100"));
			String days101 = PadQuotes(request.getParameter("days101"));
			String total = PadQuotes(request.getParameter("total"));
			String grandtotal = PadQuotes(request.getParameter("grandtotal"));
			String allocated = PadQuotes(request.getParameter("allocated"));
			String notallocated = PadQuotes(request.getParameter("notallocated"));
			String curr_Date = ToLongDate(kknow());
			// // SOP("brand_ids===" + brand_ids);
			// // SOP("region_ids===" + region_ids);
			// // SOP("branch_ids===" + branch_ids);
			// // SOP("model_id===" + model_id);
			// // SOP("model_ids===" + model_ids);
			// // SOP("item_id===" + item_id);
			// // SOP("item_ids===" + item_ids);
			// // SOP("fueltype_ids===" + fueltype_ids);
			// // SOP("option_id===" + option_id);
			// // SOP("option_ids===" + option_ids);
			// // SOP("pending_deliverys===" + pending_deliverys);
			// // SOP("starttime===" + starttime);
			// // SOP("endtime===" + endtime);
			
			StrSearch = BranchAccess.replace("branch_id", "vehstock_branch_id"); // + " " + ExeAccess;
			// For multi-select
			// Brands
			// if (!brand_id.equals("0")) {
			// StrSearch += " AND branch_brand_id = " + brand_id + " ";
			// }
			if (!brand_ids.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrSearch += " AND vehstock_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_region_id IN (" + region_ids + "))";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrSearch += " AND vehstock_branch_id IN (" + branch_ids + ")";
			}
			// Models
			if (!model_id.equals("0")) {
				if (orderplaced.equals("yes")) {
					StrSearch += " AND model_id = " + model_id;
				} else {
					StrSearch += " AND item_model_id = " + model_id;
				}
			}
			if (!model_ids.equals("")) {
				StrSearch += " AND item_model_id IN (" + model_ids + ")";
			}
			// Items
			if (!item_id.equals("0")) {
				StrSearch += " AND item_id =" + item_id;
			}
			if (!item_ids.equals("")) {
				StrSearch += " AND item_id IN (" + item_ids + ")";
			}
			// Fuel-Type
			if (!fueltype_ids.equals("")) {
				StrSearch += " AND item_fueltype_id IN (" + fueltype_ids + ")";
			}
			// Options
			if (!option_id.equals("0")) {
				StrSearch = StrSearch + " AND vehstock_id IN (SELECT trans_vehstock_id FROM " + compdb(comp_id) + "axela_vehstock_option_trans WHERE trans_option_id = " + option_id + ")";
			}
			if (!option_ids.equals("")) {
				StrSearch = StrSearch + " AND vehstock_id IN (SELECT trans_vehstock_id FROM " + compdb(comp_id) + "axela_vehstock_option_trans WHERE trans_option_id = " + option_ids + ")";
			}
			// Pending Delivery
			if (pending_deliverys.contains("1,2")) {
				if (pending_deliverys.contains("1")) {
					StrSearch = StrSearch + " AND vehstock_delstatus_id != 6";
				}
				if (pending_delivery.contains("2")) {
					StrSearch = StrSearch + " AND vehstock_delstatus_id = 6";
				}
			}
			
			if (!status_ids.equals("")) {
				StrSearch += " AND vehstock_status_id IN (" + status_ids + ")";
			}
			
			// StrSearch = StrSearch + " AND vehstock_invoice_date <= " + starttime;
			
			// Group BY Models
			if (model.equals("yes")) {
				StrFilter += StrSearch;
				if (orderplaced.equals("yes")) {
					StrFilter += " AND SUBSTR(orderplaced_date, 1,6) =  SUBSTR('" + starttime + "', 1, 6)";
					// StrFilter += " AND orderplaced_date >= " + starttime + ""
					// + " AND orderplaced_date <= " + endtime + "";
				}
				if (purchased.equals("yes")) {
					// For showing 0 in link also Uncomment for revert back
					StrFilter += " AND SUBSTR(vehstock_invoice_date, 1, 6) =SUBSTR('" + starttime + "', 1, 6)";
					
				}
				if (days30.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 0"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 30 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days50.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 31"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 50 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days75.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 51"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 75 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days100.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 76"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 100 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days101.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 101 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (total.equals("yes") || grandtotal.equals("yes")) {
					StrFilter += " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime + " ";
				}
				
				if (allocated.equals("yes")) {
					StrFilter = StrFilter.replace("vehstock_branch_id", "so_branch_id");
					StrFilter += " AND so_retail_date = ''"
							+ " AND so_retail_date < " + starttime
							+ " AND so_active = 1"
							+ " AND so_vehstock_id != 0";
				}
				
				if (notallocated.equals("yes")) {
					StrFilter += " AND so_active = 1"
							+ " AND so_vehstock_id = 0 "
							+ " AND so_retail_date = '' ";
					
				}
				
			}
			// SOP("StrSearch==" + StrSearch);
			// Group BY Brands
			if (brand.equals("yes")) {
				StrFilter += StrSearch;
				if (!brand_id.equals("0")) {
					StrFilter += " AND branch_brand_id = " + brand_id;
				}
				if (orderplaced.equals("yes")) {
					StrFilter += " AND SUBSTR(orderplaced_date, 1, 6) =  SUBSTR('" + starttime + "', 1, 6)";
				}
				if (purchased.equals("yes")) {
					// For showing 0 in link also Uncomment for revert back
					StrFilter += " AND SUBSTR(vehstock_invoice_date, 1, 6) =SUBSTR('" + starttime + "', 1, 6)";
				}
				if (days30.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 0"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 30 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days50.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 31"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 50 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days75.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 51"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 75 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days100.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 76"
							+ " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) <= 100 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (days101.equals("yes")) {
					StrFilter += " AND DATEDIFF(" + starttime + ",vehstock_invoice_date) >= 101 "
							+ " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				if (total.equals("yes") || grandtotal.equals("yes")) {
					StrFilter += " AND (so_retail_date = ''"
							// + " OR so_retail_date > " + starttime
							+ ")"
							+ " AND vehstock_invoice_date <= " + starttime;
				}
				
				if (allocated.equals("yes")) {
					StrFilter += " AND (so_retail_date = ''"
							// + " OR so_retail_date < " + starttime
							+ " )"
							+ " AND so_active = 1"
							+ " AND so_vehstock_id != 0";
				}
				
				if (notallocated.equals("yes")) {
					StrFilter = StrFilter.replace("vehstock_branch_id", "so_branch_id");
					StrFilter += " AND so_active = 1"
							+ " AND so_vehstock_id = 0 "
							+ " AND so_retail_date = ''";
					
				}
			}
			// SOP("StrFilter===" + StrFilter);
			if (orderplaced.equals("yes")) {
				// SOP("StrFilter==" + StrFilter);
				SetSession("orderplacedstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../inventory/orderplaced-list.jsp?smart=yes"));
			} else if (allocated.equals("yes") || notallocated.equals("yes")) {
				if (notallocated.equals("yes")) {
					StrFilter = StrFilter.replace("vehstock_branch_id", "so_branch_id");
					StrFilter += " AND branch_branchtype_id = 1";
				}
				SetSession("sostrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			} else {
				SetSession("stockstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../inventory/stock-list.jsp?smart=yes"));
			}
			
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public String PopulateStockStatus() {
		
		String stringval = "";
		try {
			StrSql = "SELECT status_id, status_name "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status "
					+ " WHERE 1 = 1 "
					+ " GROUP BY status_id"
					+ " ORDER BY status_name";
			// SOP("StrSql-------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			
			stringval = stringval + "<select name=dr_status size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_status style=\"padding:10px\">";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("status_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("status_id"), status_ids);
					stringval = stringval + ">" + crs.getString("status_name") + "</option>\n";
				}
			}
			stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}
	
}
