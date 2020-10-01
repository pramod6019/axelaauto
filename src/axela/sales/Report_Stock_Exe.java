package axela.sales;

//aJIt 26th June 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Stock_Exe extends Connect {

	public String StrHTML = "";
	public String LinkExportPage = "../sales/executive-stock-status-export.jsp?";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrOption = "";
	public String StrTeam = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0", vehstocklocation_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String vehstock_status_id = "0";
	public String pending_delivery = "";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "";
	public String vehstock_access = "";
	public String brand_id = "0";
	public String vehstock_branch_id = "0";
	public String team_id = "0", fueltype_id = "0";
	public String go = "";
	public String emp_all_exe = "";
	public String ExeAccess = "";
	public String emp_stock_ageing = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	// //public String exporttype = "pdf";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			// SOP("branch_id=====" + branch_id);
			BranchAccess = GetSession("BranchAccess", request);
			// SOP("BranchAccess---------------------" + BranchAccess);
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			brand_id = CNumeric(PadQuotes(request.getParameter("drop_brand_id")));
			// SOP("brand_id===" + brand_id);
			// if (brand_id.equals("0")) {
			// StrSql += "SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch"
			// + " INNER JOIN axela_brand ON brand_id = branch_brand_id"
			// + " WHERE 1=1 "
			// + " AND branch_active = '1'"
			// + " AND branch_branchtype_id = '1'"
			// + BranchAccess;
			// // SOP("StrSql===" + StrSql);
			// brand_id = CNumeric(ExecuteQuery(StrSql));
			// }
			// SOP("brand_id==22=" + brand_id);
			vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
			vehstocklocation_id = CNumeric(PadQuotes(request.getParameter("dr_loc_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
			option_id = CNumeric(PadQuotes(request.getParameter("dr_option_id")));
			delstatus_id = CNumeric(PadQuotes(request.getParameter("dr_delstatus_id")));
			vehstock_status_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_status_id")));
			pending_delivery = PadQuotes(request.getParameter("dr_pending_delivery_id"));
			vehstock_blocked = PadQuotes(request.getParameter("dr_blocked"));
			order_by = PadQuotes(request.getParameter("dr_order_by"));
			team_id = CNumeric(PadQuotes(request.getParameter("dr_team_id")));
			fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_fuel_id")));
			go = PadQuotes(request.getParameter("submit_button"));
			// if (branch_id.equals("0")) {
			// vehstock_branch_id =
			// CNumeric(PadQuotes(request.getParameter("dr_branch")));
			// } else {
			// vehstock_branch_id = branch_id;
			// }
			vehstock_access = ReturnPerm(comp_id, "emp_stock_access", request);
			// if (!brand_id.equals("") && !brand_id.equals("-1")) {
			// StrSearch += " AND vehstock_blocked = " + vehstock_blocked + "";
			// }

			// StrSearch += ExeAccess;
			emp_stock_ageing = ExecuteQuery("SELECT emp_stock_ageing FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + emp_id + "");

			if (!emp_id.equals("1")) {
				String exestock_view = CNumeric(ExecuteQuery("SELECT brandconfig_exestockview"
						+ " FROM " + compdb(comp_id) + "axela_brand_config"
						+ " WHERE brandconfig_brand_id = " + brand_id));
				if (exestock_view.equals("0")) {
					StrSearch = StrSearch + " AND stockbranch.branch_region_id IN "
							+ " (SELECT region.branch_region_id FROM " + compdb(comp_id) + "axela_branch region "
							+ "WHERE 1=1"
							+ BranchAccess.replace("branch_id", "region.branch_id") + ")";
				}
			}

			if (!model_id.equals("0")) {
				StrSearch = StrSearch + " AND model_id = " + model_id + "";
				if (!item_id.equals("0")) {
					StrSearch = StrSearch + " AND vehstock_item_id = " + item_id + "";
				}
				if (!option_id.equals("0")) {
					StrSearch = StrSearch + " AND option_id = " + option_id + "";
				}
			}

			// if (comp_id.equals("1009")) {
			// if (branch_id.equals("1") || branch_id.equals("2")
			// || branch_id.equals("3")
			// || branch_id.equals("4")) {
			// StrSearch = StrSearch + " AND vehstocklocation_id <= 10 ";
			// } else if (branch_id.equals("5")) {
			// StrSearch = StrSearch + " AND vehstocklocation_id >= 11 ";
			// }
			// }

			if (!vehstocklocation_id.equals("0")) {
				StrSearch = StrSearch + " AND vehstocklocation_id = " + vehstocklocation_id + "";
				if (!item_id.equals("0")) {
					StrSearch = StrSearch + " AND vehstock_item_id = " + item_id + "";
				}
				if (!option_id.equals("0")) {
					StrSearch = StrSearch + " AND option_id = " + option_id + "";
				}
			}
			if (vehstock_access.equals("0")) {
				StrSearch += " AND vehstock_blocked = 0";
				StrSearch += " AND vehstock_status_id != 3";
			}

			if (!delstatus_id.equals("0")) {
				StrSearch += " AND vehstock_delstatus_id = " + delstatus_id + "";
			}
			if (!vehstock_status_id.equals("0")) {
				StrSearch += " AND vehstock_status_id = " + vehstock_status_id + "";
			}

			if (!vehstock_branch_id.equals("0") && !pending_delivery.equals("1")) {
				StrSearch += " AND stockbranch.branch_id = " + vehstock_branch_id + "";
			}

			if (!brand_id.equals("0")) {
				StrSearch += " AND stockbranch.branch_brand_id = " + brand_id + "";
			}

			if (pending_delivery.equals("")) {
				pending_delivery = "2";
			}

			if (pending_delivery.equals("0")) {
				StrSearch += " AND (so_delivered_date = '' OR so_id IS NULL)";
				// StrSearch += " AND (vehstock_id NOT IN (SELECT so_vehstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_active = 1)"
				// + " OR vehstock_id IN (SELECT so_vehstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_delivered_date = ''"
				// + " AND so_active = 1))";

			} else if (pending_delivery.equals("1")) {
				StrSearch += " AND so_delivered_date = ''";
				// StrSearch += " AND vehstock_id IN (SELECT so_vehstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_delivered_date = ''  AND so_active = 1)";
				if (!vehstock_branch_id.equals("0")) {
					StrSearch += " AND so_branch_id = " + vehstock_branch_id + "";
				}

			} else if (pending_delivery.equals("2")) {
				StrSearch += " AND so_id IS NULL";
				// StrSearch += " AND vehstock_id NOT IN (SELECT so_vehstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_active = 1)";
			}
			if (!fueltype_id.equals("0")) {
				StrSearch += " AND item_fueltype_id = " + fueltype_id + "";
			}
			if (!vehstock_blocked.equals("") && !vehstock_blocked.equals("-1")) {
				StrSearch += " AND vehstock_blocked = " + vehstock_blocked + "";
			}
			if (!StrSearch.equals("")) {
				SetSession("executivestockstatusstrsql", StrSearch, request);
			}
			if (!order_by.equals("") && !order_by.equals("0")) {
				if (order_by.equals("1")) {
					StrOrder = ", vehstock_invoice_date";
				}
				if (order_by.equals("2")) {
					StrOrder = ", so_stockallocation_time";
				}
				if (order_by.equals("3")) {
					StrOrder = ", so_retail_date";
				}
				if (order_by.equals("4")) {
					StrOrder = ", status_name";
				}
				if (order_by.equals("5")) {
					StrOrder = ", vehstock_engine_no";
				}
				if (order_by.equals("6")) {
					StrOrder = ", quotecount DESC";
				}
			} else if (order_by.equals("") || order_by.equals("0")) {
				// StrOrder =
				// " status_id, model_name, prod_name, vehstock_invoice_date ";
				StrOrder = ", vehstock_invoice_date ";
			}

			if (!team_id.equals("0") && !pending_delivery.equals("2")) {
				StrSearch += " AND teamtrans_team_id = " + team_id + "";
			}

			if (go.equals("Go")) {
				vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				CheckForm();
				try {
					if (msg.equals("")) {
						StrHTML = StockDetail(comp_id);
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	public void CheckForm() {
		msg = "";
		try {
			if (vehstock_branch_id.equals("0")) {
				// msg = msg + "<br>Select Branch!";
			}
			if (model_id.equals("0")) {
				// msg = msg + "<br>Select Model!";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String StockDetail(String comp_id) {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		String optionnames = "";
		int grandsoamount = 0;
		int grandreceiptamount = 0;
		int Totalcount = 0;
		try {
			// to know no of records depending on search
			updateQuery("SET  group_concat_max_len = 100000000");
			StrSql = "SELECT vehstock_id,"
					+ " vehstock_parking_no, vehstock_vehstocklocation_id,"
					+ " COALESCE(vehstocklocation_name, '') AS vehstocklocation_name, vehstock_comm_no, COALESCE(vehstock_notes,'') AS vehstock_notes, vehstock_chassis_no, vehstock_engine_no,"
					+ " COALESCE(IF(vehstock_invoice_date = '', '99999999999999', vehstock_invoice_date), '') AS vehstock_invoice_date,"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
					+ " item_id, model_id,  model_name, vehstock_item_id, vehstock_ex_price, delstatus_name,"
					+ " vehstock_status_id, status_name, vehstock_pdi_date, COALESCE(vehstock_intransit_damage, '') AS vehstock_intransit_damage,"
					+ " COALESCE (vehstock_incentive,0) AS vehstock_incentive,"
					+ " vehstock_rectification_date, vehstock_blocked, vehstock_stockpriority_id,"
					+ " COALESCE(vehstockpriority_rank, 1000) AS vehstockpriority_rank,"
					+ " COALESCE(vehstockpriority_name, '') AS vehstockpriority_name,"
					+ " COALESCE(CONCAT(option_name, ' (', option_code, ')'),'') AS option_name, ";
			if (brand_id.equals("56")) {
				StrSql += " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE 1 = 1"
						+ " AND trans_vehstock_id = vehstock_id), '') AS optionnames,";
			}
			StrSql += " COALESCE(option_id, 0) as option_id,"
					+ " COALESCE(sobranch.branch_id, 0) as sobranch_id, COALESCE(CONCAT(sobranch.branch_name, ' (', sobranch.branch_code, ')'), '') AS sobranchname,"
					+ " COALESCE(stockbranch.branch_id, 0) as stockbranch_id, COALESCE(CONCAT(stockbranch.branch_name, ' (', stockbranch.branch_code, ')'), '') AS stockbranchname,"
					+ " COALESCE(COUNT(DISTINCT quote_id), 0) AS quotecount,"
					+ " COALESCE(so_id, '0') AS so_id,"
					+ " IF(COALESCE(so_no, 0) != '0', COALESCE(CONCAT(stockbranch.branch_code, so_no), ''),'') AS so_no,"
					+ " COALESCE(so_date, '') AS so_date,"
					+ "	COALESCE (so_retail_date, '') AS so_retail_date,"
					+ " so_emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " COALESCE(so_stockallocation_time, '') AS so_stockallocation_time,"
					+ " COALESCE(customer_id, 0) AS customer_id,"
					+ " COALESCE(customer_name, '') AS customer_name, COALESCE(so_grandtotal, 0) AS so_grandtotal,"
					+ " COALESCE((SELECT SUM(voucher_amount) FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_active = 1 AND voucher_so_id = so_id AND voucher_vouchertype_id = 9 ), 0)"
					+ " AS so_receiptamount,"
					+ " COALESCE(vehstockgatepass_id,'0') AS vehstockgatepass_id"

					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch stockbranch ON stockbranch.branch_id = vehstock_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
					+ " AND status_id != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch sobranch ON sobranch.branch_id = so_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_priority ON vehstockpriority_id = vehstock_stockpriority_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_gatepass ON vehstockgatepass_vehstock_id = vehstock_id"
					+ " AND vehstockgatepass_in_kms = 0"
					+ " WHERE 1 = 1 "
					+ " AND delstatus_id != 6"
					+ "	AND option_optiontype_id = 1"
					+ StrSearch
					+ StrOption + ""
					+ " GROUP BY vehstock_id"
					+ " ORDER BY vehstockpriority_rank ASC" + StrOrder;
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			int soamount = 0;
			int receiptamount = 0;
			int invoicedays = 0;
			int stockallocationdays = 0;
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th >Stock ID</th>\n");
				Str.append("<th>Variant</th>\n");
				Str.append("<th>Colour</th>\n");
				// if (brand_id.equals("56")) {
				// Str.append("<th data-hide=\"phone\">Commission No.</th>\n");
				// }
				Str.append("<th data-hide=\"phone\">Chassis Number</th>\n");
				Str.append("<th data-hide=\"phone\">Engine No.</th>\n");
				if (!emp_stock_ageing.equals("0") || emp_id.equals("1")) {
					Str.append("<th data-hide=\"phone\">Invoice Date</th>\n");
				}
				Str.append("<th data-hide=\"phone\">Retail Date</th>\n");
				Str.append("<th>Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">PDI</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Quote Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">SO Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Receipt Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Location</th>\n");
				Str.append("<th data-hide=\"phone\">Branch</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {

					Totalcount = Totalcount + 1;
					count = count + 1;
					grandsoamount = (int) (grandsoamount + crs.getDouble("so_grandtotal"));
					soamount = (int) (soamount + crs.getDouble("so_grandtotal"));
					grandreceiptamount = (int) (grandreceiptamount + crs.getDouble("so_receiptamount"));
					receiptamount = (int) (receiptamount + crs.getDouble("so_receiptamount"));
					if (!crs.getString("vehstock_invoice_date").equals("99999999999999")) {
						invoicedays = (int) Math.round(getDaysBetween(crs.getString("vehstock_invoice_date"), ToLongDate(kknow())));
					} else {
						invoicedays = 0;
					}
					if (invoicedays < 45) // invoicedays>=0 &&
					{
						bgcol = "#ffffff";
					} else if (invoicedays >= 45 && invoicedays <= 74) {
						bgcol = "orange";
					} else if (invoicedays > 74) {
						bgcol = "ffb2b2";
					}

					if (crs.getString("vehstock_status_id").equals("2")) {
						bgcol = " ";
					}
					if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
						bgcol = "#b5b2ff";
					}
					Str.append("<tr bgcolor=").append(bgcol).append(">\n");
					Str.append("<td valign=top align=center >").append(count);
					Str.append("</td>\n");
					Str.append("<td valign=top align=center><a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\">")
							.append(crs.getString("vehstock_id"))
							.append("</a>");
					if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
						Str.append("<br><font color=\"#ff0000\">").append(crs.getString("vehstockpriority_name")).append("</font>");
					}
					if (crs.getString("vehstock_blocked").equals("1")) {
						Str.append("<br><font color=#ff0000>Blocked</font>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("item_name"));
					if (!crs.getString("vehstock_comm_no").equals("") && !crs.getString("vehstock_comm_no").equals("0")) {
						Str.append("<br>Comm No.: " + crs.getString("vehstock_comm_no"));
					}
					Str.append("</td>\n");

					if (brand_id.equals("56")) {
						optionnames = crs.getString("optionnames");
						Str.append("<td valign=top align=left>");
						for (int i = 0; i < optionnames.split(",").length; i++) {
							Str.append(optionnames.split(",")[i] + "<br>");
						}
						Str.append("</td>\n");
						// // Str.append("<td valign=top align=left nowrap>").append(crs.getString("vehstock_comm_no"));
						// Str.append("</td>\n");
					} else {
						Str.append("<td valign=top align=left>").append(crs.getString("option_name")).append("</td>\n");
					}

					Str.append("<td valign=top align=left nowrap>").append(crs.getString("vehstock_chassis_no"));
					if (!crs.getString("vehstock_incentive").equals("0.0")) {
						Str.append("<br>Incentive Amount : ").append(IndDecimalFormat(deci.format(crs.getDouble("vehstock_incentive"))));
					}
					if (!crs.getString("vehstock_intransit_damage").equals("")) {
						Str.append("<br>").append(crs.getString("vehstock_intransit_damage"));
					}
					if (!crs.getString("vehstock_notes").equals("")) {
						Str.append("<br>").append(crs.getString("vehstock_notes"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("vehstock_engine_no"));
					Str.append("</td>\n");
					if (!emp_stock_ageing.equals("0") || emp_id.equals("1")) {
						Str.append("<td valign=top align=center>");
						if (!crs.getString("vehstock_invoice_date").equals("99999999999999")) {
							Str.append(strToShortDate(crs.getString("vehstock_invoice_date"))).append("<br>").append(invoicedays).append(" Days");
						}
						Str.append("</td>\n");
					}
					Str.append("<td valign=top align=center>").append(strToLongDate(crs.getString("so_retail_date")) + "</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("status_name") + "</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("delstatus_name") + "</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("vehstock_pdi_date")) + "</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("quotecount").equals("0")) {
						Str.append("<a href=veh-quote-list.jsp?search=yes&txt_search=").append("dr_search=3&search_button=Search>").append(crs.getString("quotecount")).append("</a>");
					}// ////crs.getString("vehstock_comm_no")).append("&
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("so_id").equals("0")) {
						if (!crs.getString("so_no").equals("")) {
							Str.append("<a href=veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(">SO ID: ").append(crs.getString("so_id")).append("</a>");
						}
						if (!crs.getString("sobranch_id").equals("0")) {
							Str.append("<br/>" + crs.getString("sobranchname"));
						}
						if (!crs.getString("so_date").equals("")) {
							Str.append("<br/>").append(strToShortDate(crs.getString("so_date")));
							Str.append("<br/>").append(Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow())))).append(" Days");
						}

						if (!crs.getString("so_stockallocation_time").equals("")) {
							Str.append("<br/>Allocation:");
							Str.append("<br/>").append(Math.round(getDaysBetween(crs.getString("so_stockallocation_time"), ToShortDate(kknow())))).append(" Days");
						} else {
							Str.append("<br/>Allocation :");
							Str.append("<br/>").append("0 Days");
						}
						if (!crs.getString("customer_name").equals("")) {
							Str.append("<br/>").append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_id")).append(")");
						}
						if (!crs.getString("so_no").equals("")) {
							Str.append("<br><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("so_emp_id")).append(">").append(crs.getString("emp_name")).append("</a>");
						}
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append("").append(IndDecimalFormat(deci.format(crs.getDouble("so_grandtotal")))).append("");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_receiptamount").equals("")) {
						Str.append("").append(IndDecimalFormat(deci.format(crs.getDouble("so_receiptamount")))).append("");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("vehstocklocation_name"));

					if (crs.getString("so_id").equals("0")) {
						Str.append("<br><br> <a href=veh-quote-add.jsp?quote_stock_id=" +
								crs.getString("vehstock_id") + ">" + "Add Quote" + "</a>");
					}

					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("stockbranch_id")).append("\">").append(crs.getString("stockbranchname")).append("</a>");
					if (!crs.getString("vehstockgatepass_id").equals("0")) {
						Str.append("</br><b>" + "In Transit </b>");
					}

					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(IndFormat(soamount + "")).append("</b></td>\n");
				Str.append("<td align=right><b>").append(IndFormat(receiptamount + "")).append("</b></td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("</tr>\n");
			} else {
				Str.append("<tr align=center>\n");
				Str.append("<td><br><br><br><br><b><font color=red>No Stock(s) found!</font></b><br><br></td>\n");
				Str.append("</tr>\n");
			}
			crs.close();
			Str.append("<tr align=center>\n<td align=right colspan=16><b>Total Stock: ").append(Totalcount).append(" " + "<br>Grand Total Sales Order Amount: ").append(IndFormat(grandsoamount + ""))
					.append("<br>Grand Total Receipt Amount: ").append(IndFormat(grandreceiptamount + "")).append("</b></td>\n</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateModel(String principle_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ BranchAccess
					+ " WHERE model_active = '1' "
					+ " AND model_sales = '1' ";
			if (!principle_id.equals("0"))
			{
				StrSql += " AND model_brand_id = " + principle_id;
			}
			if (!branch_id.equals("0"))
			{
				StrSql += " AND branch_id = " + branch_id;
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-----model-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_model_id\" class=\"form-control\" id=\"dr_model_id\" onChange=\"PopulateItem(this.value);\">");
			Str.append("<option value=0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">" + crs.getString("model_name") + "</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLocation(String brand_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {

			if (brand_id.equals("0") && branch_id.equals("0")) {
				Str.append("<select id=\"dr_team_id\" name=\"dr_team_id\" class=\"form-control\">");
				Str.append("<option value =0>Select</option>");
				Str.append("</select>");
			} else {
				StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
						+ " WHERE 1 = 1";
				if (!brand_id.equals("0")) {
					StrSql += " AND vehstocklocation_branch_id IN ("
							+ " SELECT branch_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_brand_id = " + brand_id + " )";
				}

				if (!branch_id.equals("0")) {
					StrSql += " AND vehstocklocation_branch_id = " + branch_id;
				}
				StrSql += " ORDER BY vehstocklocation_name";
				// SOP("StrSql Location----" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<select name=\"dr_loc_id\" class=\"form-control\" id=\"dr_loc_id\">");
				Str.append("<option value=0> Select </option>");
				while (crs.next()) {
					Str.append("<option value=" + crs.getString("vehstocklocation_id"));
					Str.append(StrSelectdrop(crs.getString("vehstocklocation_id"), vehstocklocation_id));
					Str.append(">" + crs.getString("vehstocklocation_name") + "</option> \n");
				}
				Str.append("</select>");
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id"
					+ " WHERE item_type_id = 1"
					+ " AND model_sales = 1 AND model_active = 1"
					+ " AND item_active = 1"
					+ " AND model_id = " + model_id + ""
					+ " AND vehstock_id NOT IN (SELECT so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so "
					+ " WHERE so_active =1 AND so_delivered_date!='')"
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_item_id\" name=\"dr_item_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateColour(String principle_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, CONCAT(option_name, ' (', option_code, ')') AS option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option";
			if (!branch_id.equals("0")) {
				StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_brand_id = option_brand_id ";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = trans_vehstock_id"
					+ " AND so_active = 1"
					+ " WHERE 1 = 1 "
					+ " AND so_id IS NULL";
			if (!branch_id.equals("0")) {
				StrSql += " AND branch_id = " + branch_id;
			}
			if (!principle_id.equals("0")) {
				StrSql += " AND option_brand_id = " + principle_id;
			}
			if (principle_id.equals("56")) {
				StrSql += " AND option_optiontype_id = 1";
			}

			StrSql += " GROUP BY option_id"
					+ " ORDER BY option_name";
			// SOP("StrSql---color----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_option_id\" name=\"dr_option_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id")).append("");
				Str.append(StrSelectdrop(crs.getString("option_id"), option_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus(String comp_id) {
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " WHERE delstatus_id != 0"
					+ " ORDER BY delstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("delstatus_id") + "";
				group = group + StrSelectdrop(crs.getString("delstatus_id"), delstatus_id);
				group = group + ">" + crs.getString("delstatus_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStockStatus(String comp_id) {
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
					+ " WHERE status_id != 0"
					+ " ORDER BY status_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("status_id") + "";
				group = group + StrSelectdrop(crs.getString("status_id"), vehstock_status_id);
				group = group + ">" + crs.getString("status_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePendingdelivery() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", pending_delivery)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", pending_delivery)).append(">Yes</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", pending_delivery)).append(">No</option> \n");
		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", order_by)).append(">Invoice Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", order_by)).append(">Allocation Date</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", order_by)).append(">Retail Date</option> \n");
		Str.append("<option value=4").append(StrSelectdrop("4", order_by)).append(">Status</option> \n");
		Str.append("<option value=5").append(StrSelectdrop("5", order_by)).append(">Engine No.</option> \n");
		Str.append("<option value=6").append(StrSelectdrop("6", order_by)).append(">Quote Count</option> \n");
		return Str.toString();
	}

	public String PopulateBlocked() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1").append(StrSelectdrop("-1", vehstock_blocked)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", vehstock_blocked)).append(">Blocked</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", vehstock_blocked)).append(">Not Blocked</option> \n");
		return Str.toString();
	}

	public String PopulateFuelType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1=1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch(String principle_id, String comp_id) {
		// String BranchAccess = "";
		StringBuilder stringval = new StringBuilder();
		String SqlStr = "Select branch_id, branch_name, branch_code"
				+ " from " + compdb(comp_id) + "axela_branch main"
				// + " INNER JOIN axela_brand ON brand_id = branch_brand_id"
				+ " where main.branch_active = 1"
				+ " AND main.branch_branchtype_id = 1 ";
		if (!principle_id.equals("0")) {
			SqlStr += " AND main.branch_brand_id = " + principle_id;
		}
		if (!emp_id.equals("1") && !BranchAccess.equals("")) {
			SqlStr += " AND main.branch_brand_id IN ( SELECT branch.branch_brand_id FROM " + compdb(comp_id) + "axela_branch branch WHERE 1=1 AND branch.branch_brand_id " + BranchAccess + ")";
		}
		SqlStr += " ORDER BY main.branch_brand_id, main.branch_name";
		// SOP("SqlStr---12121212-----" + SqlStr);
		CachedRowSet crs = processQuery(SqlStr, 0);
		try {
			stringval
					.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateTeam(drop_brand_id.value,this.value); PopulateModel('',this.value);PopulateColour(drop_brand_id.value,this.value);PopulateLocation(drop_brand_id.value,this.value);\">");
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), vehstock_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulatePrincipal() {
		StringBuilder Str = new StringBuilder();
		// SOP("brand_id==" + brand_id);
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = '1'"
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql-------22222222222-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTeam(String brand_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			if (brand_id.equals("0") && branch_id.equals("0")) {
				Str.append("<select id=\"dr_team_id\" name=\"dr_team_id\" class=\"form-control\">");
				Str.append("<option value =0>Select</option>");
				Str.append("</select>");
			} else {
				String Strsql = "SELECT team_id, team_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_team"
						+ " WHERE 1 = 1";
				if (!brand_id.equals("0")) {
					Strsql += " AND team_branch_id IN ("
							+ " SELECT branch_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_brand_id = " + brand_id + " )";
				}
				if (!branch_id.equals("0")) {
					Strsql += " AND team_branch_id = " + branch_id;
				}
				Strsql += " AND team_active = 1 "
						+ " ORDER BY team_name";
				// SOP("SqlStr----team---" + Strsql);
				CachedRowSet crs = processQuery(Strsql, 0);
				Str.append("<select id=\"dr_team_id\" name=\"dr_team_id\" class=\"form-control\">");
				Str.append("<option value =0>Select</option>");
				// SOP("brand_id------" + brand_id);
				// SOP("branch_id------" + branch_id);
				// if (!brand_id.equals("0") && !branch_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("team_id")).append("");
					Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
					Str.append(">").append(crs.getString("team_name")).append("</option>\n");
					// }
				}
				Str.append("</select>");
				crs.close();
			}

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
}