package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SO_Delivered extends Connect {

	public String StrHTML = "";
	public String StrTitle = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String dr_branch_id = "0";
	public String StrSearch = "";
	public String go = "";
	public String StrSql = "", having = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, model_ids;
	public String branch_id = "", BranchAccess = "", exe_id = "", brand_id = "", region_id = "";
	public String msg = "", ExeAccess = "";
	public String so_id = "";
	public String model_id = "";
	public String order_by = "";
	public String so_open = "", status_id = "0", vehstock_status_id = "0";
	public String StrOrder = "";
	public String dr_date = "", drdate = "";
	public String StrDate = "";
	public String emp_all_exe = "";
	StringBuilder strSOID = new StringBuilder();
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			BranchAccess = GetSession("BranchAccess", request);
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			go = PadQuotes(request.getParameter("submit_button"));
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			try {
				GetValues(request, response);
				CheckForm();
				if (!status_id.equals("") && !status_id.equals("0")) {
					StrSearch = StrSearch + " AND delstatus_id = " + status_id;
				}
				if (!vehstock_status_id.equals("") && !vehstock_status_id.equals("0")) {
					StrSearch = StrSearch + " AND status_id = " + vehstock_status_id;
				}
				if (!order_by.equals("") && !order_by.equals("0")) {
					if (order_by.equals("1")) {
						StrOrder = " so_date ";
					}
					if (order_by.equals("2")) {
						StrOrder = " so_delivered_date ";
					}
					if (order_by.equals("3")) {
						StrOrder = " vehstock_delstatus_id ";
					}
					if (order_by.equals("4")) {
						StrOrder = " emp_name, emp_id ";
					}
					if (order_by.equals("5")) {
						StrOrder = " so_mga_amount ";
					}
					if (order_by.equals("6")) {
						StrOrder = " so_insur_amount ";
					}
					if (order_by.equals("7")) {
						StrOrder = " so_ew_amount ";
					}
					if (order_by.equals("8")) {
						StrOrder = " so_finance_amt ";
					}

				} else if (order_by.equals("") || order_by.equals("0")) {
					StrOrder = " so_date";
				}

				if (!so_open.equals("") && !so_open.equals("-1")) {
					StrSearch = StrSearch + " AND so_open = " + so_open;
				}

				if (dr_date.equals("1")) {
					StrDate = "so_date";
					StrTitle = "Sales Orders";
				} else if (dr_date.equals("2")) {
					StrDate = "so_promise_date";
					StrTitle = "Sales Order Promised";
				} else if (dr_date.equals("3")) {
					StrDate = "so_delivered_date";
					StrTitle = "Sales Order Delivered";
				}

				if (!starttime.equals("")) {
					StrSearch = StrSearch + " AND substr(" + StrDate + ", 1 ,8) >= substr('" + starttime + "',1,8) AND " + StrDate + " != ''";
				}
				if (!endtime.equals("")) {
					StrSearch = StrSearch + " AND substr(" + StrDate + ", 1 ,8) <= substr('" + endtime + "',1,8) AND " + StrDate + " != ''";
				}
				if (!brand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id in (" + region_id + ") ";
				}
				if (!branch_id.equals("")) {
					StrSearch += " AND branch_id in (" + branch_id + ") ";
				}
				if (!model_id.equals("") && !model_id.equals("0")) {
					StrSearch = StrSearch + " AND item_model_id IN ( " + model_id + ")";
				}
				// if (!branch_id.equals("")) {
				// StrSearch = StrSearch + " AND so_branch_id IN(" + branch_id + ")";
				// }
				// else {
				// msg = msg + "<br>Select Branch!!";
				// }
				if (go.equals("Go")) {

					// //if (dr_branch_id.equals("0")) {
					// msg = msg + "<br>Select Branch!";
					// }

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = SalesOrderDetail();
						if (!strSOID.toString().equals("")) {
							SetSession("sostrsql", " AND (" + strSOID.toString() + ")", request);
						}
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		so_open = PadQuotes(request.getParameter("dr_so_open"));
		status_id = PadQuotes(request.getParameter("dr_status_id"));
		vehstock_status_id = PadQuotes(request.getParameter("dr_vehstock_status_id"));
		dr_date = PadQuotes(request.getParameter("dr_date"));
		drdate = dr_date;
		if (dr_date.equals("1")) {
			having = "";
		} else if (dr_date.equals("2")) {
			having = " having (so_delivered_date = '')";
		} else if (dr_date.equals("3")) {
			having = " having (delstatus_id = 5)";
		} else if (dr_date.equals("")) {
			dr_date = "3";
			having = " having (delstatus_id = 5)";
		}
		order_by = PadQuotes(request.getParameter("dr_order_by"));
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
			msg = msg + "<br>Select End Date!";
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

	public String SalesOrderDetail() {
		StringBuilder Str = new StringBuilder();
		double grandsoamount = 0.0;
		double grandreceiptamount = 0.0;
		double total_mga_sales = 0.0, total_insur_amount = 0.0, total_extwarranty = 0.0, total_fincases = 0.0;
		int Totalcount = 0;

		try {
			StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN ( " + brand_id + ")";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN ( " + region_id + ")";
			}
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN ( " + branch_id + ")";
			}

			StrSql += " ORDER BY branch_name";
			// SOP("sales order details===" + StrSql);

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				int count = 0;

				Str.append("<div>\n");
				while (crs1.next()) {
					Str.append("<center><b>");
					Str.append(crs1.getString("branch_name"));
					Str.append("</b></center>");

					total_mga_sales = 0.0;
					total_insur_amount = 0.0;
					total_extwarranty = 0.0;
					total_fincases = 0.0;

					StrSql = "SELECT so_id, so_enquiry_id, so_quote_id, customer_id, customer_name, contact_id,"
							+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname, ' (', contact_id, ')') AS contact_name,"
							+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id,"
							+ " so_date, so_retail_date, so_promise_date, so_delivered_date,"
							+ " COALESCE(delstatus_id, 0) AS delstatus_id,"
							+ " COALESCE(delstatus_name, '') AS delstatus_name,"
							+ " COALESCE(status_name, '') AS status_name,"
							+ " COALESCE(vehstock_id, 0) AS vehstock_id,"
							+ " item_name, item_id, so_payment_date, so_grandtotal,"
							+ " so_mga_amount, so_insur_amount, so_ew_amount, so_finance_amt, "

							+ " COALESCE((SELECT SUM(voucher_amount)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_so_id = so_id"
							+ " AND voucher_vouchertype_id = 9"
							+ " AND voucher_active = 1"
							+ " AND voucher_authorize = 1"
							+ " GROUP BY voucher_so_id"
							+ "), 0) AS receiptamount,"

							+ " COALESCE((SELECT voucher_date"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_so_id = so_id"
							+ " AND voucher_vouchertype_id = 9"
							+ " AND voucher_active = 1"
							+ " AND voucher_authorize = 1"
							+ " ORDER BY voucher_date DESC LIMIT 1"
							+ "), '') AS lastreceiptdate,"

							+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, branch_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
							+ " WHERE so_active = '1'"
							+ " AND branch_id = " + crs1.getString("branch_id")
							+ BranchAccess
							+ StrSearch
							+ ExeAccess;

					StrSql += " GROUP BY so_id"
							+ " ORDER BY " + StrOrder + ""
							+ " LIMIT 500";
					// SOPInfo("StrSql===" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					double soamount = 0;
					double receiptamount = 0;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					if (crs.isBeforeFirst()) {
						Str.append("<thead><tr>\n");
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\"><b>Sales Order</b></th>\n");
						Str.append("<th data-toggle=\"true\"><b>Customer</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Sales Manager</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>SO Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Tentative Delivery Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Retail Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Delivered Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Delivery Status</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Stock Status</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Stock</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Item</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Payment Date</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>SO Amount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Receipt Amount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Last Receipt</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\" title=\"Accessories\">Access</th>\n");
						Str.append("<th data-hide=\"phone, tablet\" title=\"Insurance\">Ins</th>\n");
						Str.append("<th data-hide=\"phone, tablet\" title=\"Extended Warranty\">EW</th>\n");
						Str.append("<th data-hide=\"phone, tablet\" >Fin Cases</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							total_mga_sales += crs.getDouble("so_mga_amount");
							total_insur_amount += crs.getDouble("so_insur_amount");
							total_extwarranty += crs.getDouble("so_ew_amount");
							total_fincases += crs.getDouble("so_finance_amt");

							if (count == 0) {
								strSOID.append(" so_id = ").append(crs.getString("so_id"));
							} else {
								strSOID.append(" OR so_id = ").append(crs.getString("so_id"));
							}
							Totalcount = Totalcount + 1;
							count = count + 1;
							grandsoamount = grandsoamount + crs.getDouble("so_grandtotal");
							soamount = soamount + crs.getDouble("so_grandtotal");
							grandreceiptamount = grandreceiptamount + crs.getDouble("receiptamount");
							receiptamount = receiptamount + crs.getDouble("receiptamount");
							Str.append("<tr>\n");
							Str.append("<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=left nowrap>");
							if (!crs.getString("so_id").equals("")) {
								Str.append("<a href=../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(" target=_blank>SO ID: ").append(crs.getString("so_id"))
										.append("</a>");
							}
							Str.append("<br><a href=../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("so_enquiry_id")).append(" target=_blank>Enquiry ID: ")
									.append(crs.getString("so_enquiry_id")).append("</a>");
							Str.append("<br><a href=../sales/veh-quote-list.jsp?quote_id=").append(crs.getString("so_quote_id")).append(" target=_blank>Quote ID: ")
									.append(crs.getString("so_quote_id"))
									.append("</a>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">");
							Str.append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_id")).append(")</a><br/>");
							Str.append("<a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append(">");
							Str.append(crs.getString("contact_name")).append("</a>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
									.append("</a>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_promise_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_promise_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_promise_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_retail_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_retail_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_retail_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_delivered_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_delivered_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_delivered_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=left>").append(crs.getString("delstatus_name"));

							Str.append("</td>\n");
							Str.append("<td valign=top align=left>").append(crs.getString("status_name")).append("</td>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=left>");
							if (!crs.getString("vehstock_id").equals("0")) {
								Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\">Stock ID: ").append(crs.getString("vehstock_id"))
										.append("</a><br/>");
								// Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\">Comm. No.: ").append(crs.getString("vehstock_comm_no")).append("</a>");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=left><a href=../inventory/inventory-item-list.jsp?item_id=");
							Str.append(crs.getString("item_id")).append(">").append(crs.getString("item_name")).append("</a>");
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_payment_date").equals("")) {
								Str.append(strToShortDate(crs.getString("so_payment_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_payment_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=right>");
							if (!crs.getString("so_grandtotal").equals("")) {
								Str.append(IndFormat(deci.parse(crs.getString("so_grandtotal")) + "")).append("");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=right>");
							if (crs.getDouble("receiptamount") != 0) {
								Str.append(IndFormat(deci.parse(crs.getDouble("receiptamount") + "") + "")).append("");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("lastreceiptdate").equals("")) {
								Str.append(strToShortDate(crs.getString("lastreceiptdate"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("lastreceiptdate"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>\n");
							// mga_sales IndFormat(deci.parse(crs.getString("so_finance_amt")) + "")
							Str.append("<td>").append(IndFormat(deci.parse(crs.getString("so_mga_amount")) + "")).append("</td>");
							// maruti_insurance
							Str.append("<td>").append(IndFormat(deci.parse(crs.getString("so_insur_amount")) + "")).append("</td>");
							// extwarranty
							Str.append("<td>").append(IndFormat(deci.parse(crs.getString("so_ew_amount")) + "")).append("</td>");
							// fincases
							Str.append("<td>").append(IndFormat(deci.parse(crs.getString("so_finance_amt")) + "")).append("</td>");

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
						Str.append("<td>&nbsp;</td>\n");
						Str.append("<td align=right><b>").append(IndFormat(deci.parse(soamount + "") + "")).append("</b></td>\n");
						Str.append("<td align=right><b>").append(IndFormat(deci.parse(receiptamount + "") + "")).append("</b></td>\n");
						Str.append("<td>&nbsp;</td>\n");

						Str.append("<td>" + IndFormat(deci.parse(total_mga_sales + "") + "") + "</td>\n");
						Str.append("<td>" + IndFormat(deci.parse(total_insur_amount + "") + "") + "</td>\n");
						Str.append("<td>" + IndFormat(deci.parse(total_extwarranty + "") + "") + "</td>\n");
						Str.append("<td>" + IndFormat(deci.parse(total_fincases + "") + "") + "</td>\n");
						Str.append("</tr>\n");
					} else {
						Str.append("<tr align=center>\n");
						Str.append("<td><br><br><br><br><b><center><font color=red>No Sales Order(s) found!</font></b></center><br><br></td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				}
				// Str.append("</td>");
				// Str.append("</tr>");
				// Str.append("<tr align=center><td align=right><b>Total Sales Orders: ").append(Totalcount);
				Str.append("<div style=\"text-align:right\"><b>Total Sales Orders:");
				Str.append(Totalcount).append("</div>");
				Str.append("<br><div style=\"text-align:right\"><b>Grand Total Sales Order Amount: ");
				Str.append(IndFormat(deci.parse(grandsoamount + "") + "")).append("</div>");
				Str.append("<br><div style=\"text-align:right\"><b>Grand Total Receipt Amount:");
				Str.append(IndFormat(deci.parse(grandreceiptamount + "") + "")).append("</div>");

				// Str.append("<br><div style=\"text-align=:right\">Grand Total Sales Order Amount: ").append(IndFormat(grandsoamount
				// + "")).append("</div>");
				// Str.append("<br><div style=\"text-align=:right\">Grand Total Receipt Amount: ").append(IndFormat(grandreceiptamount
				// + "")).append("</div>");
				Str.append("</div>");
				// Str.append("</div>");
				// Str.append("</body>\n");
				// Str.append("</HTML>\n");
			} else {
				Str.append("<font color=red><b>No Branch(s) found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " ORDER BY delstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("delstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("delstatus_id"), status_id));
				Str.append(">").append(crs.getString("delstatus_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStockStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
					+ " ORDER BY status_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id")).append("");
				Str.append(StrSelectdrop(crs.getString("status_id"), vehstock_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDate() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", dr_date)).append(">Sales Order Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_date)).append(">Sales Order Tentative Delivery Date</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_date)).append(">Sales Order Delivered Date</option> \n");
		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", order_by)).append(">Sales Order Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", order_by)).append(">Sales Order Delivered Date</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", order_by)).append(">Sales Order Delivery Status</option> \n");
		Str.append("<option value=4").append(StrSelectdrop("4", order_by)).append(">Execuitve Name</option> \n");
		Str.append("<option value=5").append(StrSelectdrop("5", order_by)).append(">Access</option> \n");
		Str.append("<option value=6").append(StrSelectdrop("6", order_by)).append(">Ins</option> \n");
		Str.append("<option value=7").append(StrSelectdrop("7", order_by)).append(">EW</option> \n");
		Str.append("<option value=8").append(StrSelectdrop("8", order_by)).append(">Fin Cases</option> \n");

		return Str.toString();
	}

	public String PopulateOpen() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1").append(StrSelectdrop("-1", so_open)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", so_open)).append(">Yes</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", so_open)).append(">No</option> \n");
		return Str.toString();
	}
}
