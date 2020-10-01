package axela.axelaauto_app;

//aJIt 26th June 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Exestock_List extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrOption = "";
	public String StrTeam = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0", vehstock_vehstocklocation_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String vehstock_status_id = "0";
	public String pending_delivery = "";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "";
	public String vehstock_access = "";
	public String brand_id = "0";
	public String vehstock_branch_id = "0", vehstock_region_id = "0", fueltype_id = "0";
	public String team_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String CountSql = "";
	public int TotalRecords = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			brand_id = CNumeric(PadQuotes(request.getParameter("drop_brand_id")));
			vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			vehstock_region_id = CNumeric(PadQuotes(request.getParameter("dr_region")));
			model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
			vehstock_vehstocklocation_id = CNumeric(PadQuotes(request.getParameter("dr_loc_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
			option_id = CNumeric(PadQuotes(request.getParameter("dr_option_id")));
			delstatus_id = CNumeric(PadQuotes(request.getParameter("dr_delstatus_id")));
			vehstock_status_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_status_id")));
			pending_delivery = PadQuotes(request.getParameter("dr_pending_delivery_id"));
			vehstock_blocked = PadQuotes(request.getParameter("dr_blocked"));
			order_by = PadQuotes(request.getParameter("dr_order_by"));
			team_id = CNumeric(PadQuotes(request.getParameter("dr_team_id")));
			fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_fuel_id")));
			go = PadQuotes(request.getParameter("go_button"));
			vehstock_access = ReturnPerm(comp_id, "emp_stock_access", request);
			// if (!brand_id.equals("") && !brand_id.equals("-1")) {
			// StrSearch += " AND vehstock_blocked = " + vehstock_blocked + "";
			// }

			if (!emp_id.equals("1")) {
				StrSearch = StrSearch + " AND vehstockbranch.branch_region_id IN "
						+ " (SELECT region.branch_region_id FROM " + compdb(comp_id) + "axela_branch region WHERE 1=1" + BranchAccess.replace("branch_id", "region.branch_id") + ")";
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
			// StrSearch = StrSearch + " AND location_id <= 10 ";
			// } else if (branch_id.equals("5")) {
			// StrSearch = StrSearch + " AND location_id >= 11 ";
			// }
			// }

			if (!vehstock_vehstocklocation_id.equals("0")) {
				StrSearch = StrSearch + " AND vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + "";
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
				StrSearch += " AND vehstockbranch.branch_id = " + vehstock_branch_id + "";

			}

			if (!vehstock_region_id.equals("0")) {
				StrSearch += " AND vehstockbranch.branch_region_id = " + vehstock_region_id + "";

			}

			if (!brand_id.equals("0")) {
				StrSearch += " AND vehstockbranch.branch_brand_id = " + brand_id + "";
			}

			if (pending_delivery.equals("")) {
				pending_delivery = "2";
			}

			if (pending_delivery.equals("0")) {
				StrSearch += " AND (vehstock_id NOT IN (SELECT so_vehstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_active = 1)"
						+ " OR vehstock_id IN (SELECT so_vehstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_delivered_date = ''"
						+ " AND so_active = 1))";
				// + " " + BranchAccess.replace("branch_id", "so_branch_id")
				// + ExeAccess.replace("emp_id", "so_emp_id") +

			} else if (pending_delivery.equals("1")) {
				StrSearch += " AND vehstock_id IN (SELECT so_vehstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_delivered_date = ''  AND so_active = 1)";

				if (!vehstock_branch_id.equals("0")) {
					StrSearch += " AND so_branch_id = " + vehstock_branch_id + "";
				}
				// StrSearch += " AND so_active = 1" +
				// BranchAccess.replace("branch_id", "so_branch_id")
				// + ExeAccess.replace("emp_id", "so_emp_id") + ")";

			} else if (pending_delivery.equals("2")) {
				StrSearch += " AND vehstock_id NOT IN (SELECT so_vehstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_active = 1)";
			}

			if (!vehstock_blocked.equals("") && !vehstock_blocked.equals("-1")) {
				StrSearch += " AND vehstock_blocked = " + vehstock_blocked + "";
			}
			if (!fueltype_id.equals("0")) {
				StrSearch += " AND item_fueltype_id = " + fueltype_id + "";
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
					StrOrder = ", status_name";
				}
				if (order_by.equals("4")) {
					StrOrder = ", vehstock_engine_no";
				}
				if (order_by.equals("5")) {
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
						// SOP("comp_id================" + comp_id);
						StrHTML = StockDetail(comp_id);
					}
				} catch (Exception ex) {
					SOPError("Axelaauto-App== " + this.getClass().getName());
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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String StockDetail(String comp_id) {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		int grandsoamount = 0;
		int grandreceiptamount = 0;
		try {
			// to know no of records depending on search
			StrSql = "SELECT vehstock_id,"
					// + " vehstock_comm_no,"
					+ " vehstock_parking_no, vehstock_vehstocklocation_id,"
					+ " COALESCE(vehstocklocation_name, '') AS vehstocklocation_name, vehstock_chassis_no, vehstock_engine_no,"
					+ " IF(vehstock_invoice_date = '', '99999999999999', vehstock_invoice_date) AS vehstock_invoice_date,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_id, model_id,  model_name, vehstock_item_id, vehstock_ex_price, delstatus_name,"
					+ " vehstock_status_id, status_name, vehstock_pdi_date, vehstock_intransit_damage,"
					+ " vehstock_rectification_date, vehstock_blocked, vehstock_stockpriority_id,"
					+ " COALESCE(vehstockpriority_rank, 1000) AS vehstockpriority_rank,"
					+ " COALESCE(vehstockpriority_name, '') AS vehstockpriority_name,"
					+ " COALESCE(CONCAT(option_name, ' (', option_code, ')'),'') AS option_name, "
					+ " COALESCE(option_id, 0) as option_id,"
					+ " COALESCE(sobranch.branch_id, 0) as sobranch_id, COALESCE(CONCAT(sobranch.branch_name, ' (', sobranch.branch_code, ')'), '') AS sobranchname,"
					+ " vehstockbranch.branch_id as vehstockbranch_id, CONCAT(vehstockbranch.branch_name, ' (', vehstockbranch.branch_code, ')') AS vehstockbranchname,"
					+ " COUNT(DISTINCT quote_id) AS quotecount,"
					+ " COALESCE(so_id, '0') AS so_id,"
					+ " IF(COALESCE(so_no, 0) != '0', CONCAT(vehstockbranch.branch_code, so_no), '') AS so_no,"
					+ " COALESCE(so_date, '') AS so_date,"
					+ " COALESCE(so_stockallocation_time, '') AS so_stockallocation_time,"
					+ " COALESCE(customer_id, 0) AS customer_id,"
					+ " COALESCE(customer_name, '') AS customer_name, COALESCE(so_grandtotal, 0) AS so_grandtotal,"
					// + " COALESCE((SELECT SUM(receipt_amount) FROM " +
					// compdb(comp_id) + "axela_invoice_receipt"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_invoice ON invoice_id = receipt_invoice_id"
					// + " WHERE invoice_so_id = so_id), 0)
					+ " 0 AS so_receiptamount"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch vehstockbranch ON vehstockbranch.branch_id = vehstock_branch_id"
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
					+ " WHERE 1 = 1"
					+ " AND delstatus_id != 6 "
					+ "	AND option_optiontype_id = 1"
					+ StrSearch
					+ StrOption + ""
					+ " GROUP BY vehstock_id"
					+ " ORDER BY vehstockpriority_rank ASC "
					+ StrOrder;

			// CountSql = "SELECT COUNT(DISTINCT(vehstock_id))";
			// TotalRecords = Integer.parseInt(ExecuteQuery(StrSql));
			// SOP("TotalRecords--" + TotalRecords);
			// totalcount = TotalRecords / 25;
			// StrSql += LimitRecords(TotalRecords,
			// Integer.toString(pagecurrent));
			SOP("StrSql=============" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			int soamount = 0;
			int receiptamount = 0;
			int invoicedays = 0;
			int vehstockallocationdays = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<tr>\n")
						.append("<th>#</th>")
						.append("<th>Stock ID </th>")
						.append("<th>Variant </th>")
						.append("<th>Colour </th>")
						.append("<th>Status </th>")
						.append("<th>Delivery Status</th>")
						.append("<th>Location </th>")
						.append("<th>Branch </th>")
						// .append("<th> </th>")
						.append("</tr>");

				while (crs.next())
				{
					count = count + 1;
					grandsoamount = grandsoamount + (int) crs.getDouble("so_grandtotal");
					soamount = soamount + (int) crs.getDouble("so_grandtotal");
					grandreceiptamount = grandreceiptamount + (int) crs.getDouble("so_receiptamount");
					receiptamount = receiptamount + (int) crs.getDouble("so_receiptamount");
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
						bgcol = "#ffb2b2";
					}

					if (crs.getString("vehstock_status_id").equals("2")) {
						bgcol = "green";
					}
					if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
						bgcol = "#b5b2ff";
					}
					// Str.append("<tr>\n")
					Str.append("<tr bgcolor=").append(bgcol).append(">\n")
							.append("<td>").append(count).append("</td>\n")
							.append("<td>").append("<a href=\"callurlapp-exestock-details.jsp?vehstock_id=")
							.append(crs.getString("vehstock_id"))
							.append("&brand_id=" + brand_id)
							.append("\"").append(">")
							.append(crs.getString("vehstock_id")).append("</a>\n");
					if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
						Str.append("<br><font color=\"#ff00\">").append(crs.getString("stockpriority_name")).append("</font>");
					}
					Str.append("</td>\n")
							.append("<td>").append(crs.getString("item_name")).append("</td>\n")
							.append("<td>").append(crs.getString("option_name")).append("</td>\n")
							.append("<td>").append(crs.getString("status_name")).append("</td>\n")
							.append("<td>").append(crs.getString("delstatus_name")).append("</td>\n")
							.append("<td>").append(crs.getString("vehstocklocation_name"));
					if (crs.getString("so_id").equals("0")) {
						Str.append("<br><br> <a href=\"callurlapp-veh-quote-add.jsp?quote_stock_id=" +
								crs.getString("vehstock_id") + "&comp_id=" + comp_id + "\" > Add Quote  </a>");
					}

					Str.append("</td>\n")
							.append("<td>").append(crs.getString("vehstockbranchname")).append("</td>\n")
							// .append("<td>").append("<a href=\"callurlapp-exestock-details.jsp?vehstock_id=")
							// .append(crs.getString("vehstock_id")).append("\"").append(">View Details</a>\n")
							// .append("</td>\n")
							.append("</tr>\n");

				}
				// // if (jscrollcount == (TotalRecords - 1)) {
				// Str.append("</div>");
				// }

			}
			// SOP("count------" + count);
			else {
				Str.append("<div class=\"container\" align=\"center\"><b><h4>&nbsp;</h4>\n").append("No Stock(s) Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
