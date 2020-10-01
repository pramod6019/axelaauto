package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_SO_Pendingdelivery extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrFilter = "";
	public String StrSql = "";
	public String branch_id = "", BranchAccess = "";
	public String branch_name = "";
	public String msg = "";
	public String so_id = "";
	public String go = "", filter = "";
	public String model_id = "", brand_id = "", region_id = "", team_id = "", exe_id = "", dr_finstatus_id = "";
	public String order_by = "";
	public String vehstock_allocate = "0";
	public String deliverystatus_id = "", fueltype_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, deliverystatus_ids, fueltype_ids, dr_finstatus_ids;
	public String TeamJoin = "";
	public String dr_branch_id = "0", executive_id = "0";
	public String StrOrder = "", ExeAccess = "";
	public String emp_all_exe = "";
	static DecimalFormat deci = new DecimalFormat("#.###");
	public MIS_Check1 mischeck = new MIS_Check1();
	StringBuilder strpendingso = new StringBuilder();
	public String exportcount = "", exportB = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			BranchAccess = GetSession("BranchAccess", request);
			emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			filter = PadQuotes(request.getParameter("filter"));
			exportB = PadQuotes(request.getParameter("btn_export"));
			exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");

			if (branch_id.equals("0")) {
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			} else {
				dr_branch_id = branch_id;
			}

			if (filter.equals("yes")) {
				RedirectSOList(request, response);
			}

			GetValues(request, response);

			if (!team_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				mischeck.branch_id = branch_id;
				TeamJoin = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
				StrSearch = StrSearch + " AND team_id IN (" + team_id + ")";
			}

			if (!exe_id.equals("")) {
				StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + " )";
			}

			if (!model_id.equals("")) {
				StrSearch = StrSearch + " AND item_model_id IN(" + model_id + ")";
			}

			// if (!status_id.equals("") && !status_id.equals("-1") && !status_id.equals("0")) {
			// StrSearch = StrSearch + " AND delstatus_id = " + status_id;
			// }

			if (!order_by.equals("") && !order_by.equals("0")) {
				if (order_by.equals("1")) {
					StrOrder = " so_date";
				}

				if (order_by.equals("2")) {
					StrOrder = " vehstock_delstatus_id";
				}

				if (order_by.equals("3")) {
					StrOrder = " emp_name, emp_id";
				}
			} else if (order_by.equals("") || order_by.equals("0")) {
				StrOrder = " so_date";
			}
			if (vehstock_allocate.equals("1")) {
				StrSearch += " AND so_vehstock_id > 0";
			} else if (vehstock_allocate.equals("2")) {
				StrSearch += " AND so_vehstock_id = 0";
			} else if (vehstock_allocate.equals("3")) {
				StrSearch += " AND so_retail_date != ''";
			} else if (vehstock_allocate.equals("4")) {
				StrSearch += " AND so_retail_date = ''";
			} else if (vehstock_allocate.equals("5")) {
				StrSearch += " AND so_retail_date = ''"
						+ " AND so_vehstock_id !=0 ";
			} else if (vehstock_allocate.equals("6")) {
				StrSearch += " AND so_din_del_location != 0 ";
			} else if (vehstock_allocate.equals("7")) {
				StrSearch += " AND so_invoice_request = 1 ";
			}

			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ") ";
			}
			if (!branch_id.equals("")) {
				StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
			}
			if (!branch_id.equals("")) {
				StrSql = "SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_id IN(" + branch_id + ")";
				branch_name = ExecuteQuery(StrSql);
			}
			if (!deliverystatus_id.equals("")) {
				StrSearch += " AND vehstock_delstatus_id IN (" + deliverystatus_id + ")";
			}
			if (!fueltype_id.equals("")) {
				StrSearch += " AND item_fueltype_id IN (" + fueltype_id + ")";
			}
			go = PadQuotes(request.getParameter("submit_button"));

			try {
				if (go.equals("Go")) {
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = PendingDetails();
						// SOP("StrHTML====>>>" + StrHTML);
						if (!strpendingso.toString().equals("")) {
							SetSession("sostrsql", " AND (" + strpendingso.toString() + ")", request);
						}
					}
				}

				if (exportB.equals("Export")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("") && ReturnPerm(comp_id, "emp_export_access", request).equals("1")) {
						BookingDetails(request, response);
					}
					else {
						response.sendRedirect(AccessDenied());
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
		CheckSession(request, response);

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		// status_id = PadQuotes(request.getParameter("dr_status_id"));
		order_by = PadQuotes(request.getParameter("dr_order_by"));
		vehstock_allocate = CNumeric(PadQuotes(request.getParameter("dr_stock_allocate")));

		deliverystatus_id = RetrunSelectArrVal(request, "dr_deliverystatus_id");
		deliverystatus_ids = request.getParameterValues("dr_deliverystatus_id");

		fueltype_id = RetrunSelectArrVal(request, "dr_fuel_id");
		fueltype_ids = request.getParameterValues("dr_fuel_id");

		dr_finstatus_id = RetrunSelectArrVal(request, "dr_finstatus_id");
		dr_finstatus_ids = request.getParameterValues("dr_finstatus_id");
	}

	protected void CheckForm() {
		if (!branch_id.equals("")) {
			StrSearch += " AND so_branch_id IN( " + branch_id + ")";
		}
		if (!region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + region_id + ") ";
		}
		if (brand_id.equals("")) {
			msg += "<br>Select Brand!";
		}
	}

	public String PendingDetails() {
		StringBuilder Str = new StringBuilder();
		double grandsoamount = 0.0;
		String finstatus_color = "";
		double grandreceiptamount = 0.0;
		int Totalcount = 0;
		try {
			Str.append("<div>\n");
			Str.append("<center><b>");
			Str.append(branch_name);
			Str.append("</b></center>");

			StrSql = "SELECT so_id, so_enquiry_id, so_quote_id, customer_id, customer_name,"
					+ " so_finstatus_id, so_retail_date,"
					+ " COALESCE(so_vehstock_id, 0) AS so_vehstock_id,"
					+ " COALESCE(vehstock_comm_no, '') AS vehstock_comm_no,"
					+ " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id,"
					+ " so_date, so_promise_date, so_delivered_date,"
					+ " COALESCE (so_stockallocation_time, '') AS so_stockallocation_time, "
					+ " COALESCE(delstatus_id, 0) AS delstatus_id,"
					+ " COALESCE(fintype_name, '') AS fintype_name,"
					+ " COALESCE(finstatus_name, '') AS finstatus_name,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(fueltype_name, '') AS fueltype_name,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_id, so_payment_date, so_grandtotal,"
					+ " COALESCE (accvoucher.totalrecamount,	'0') AS totalrecamount,"
					+ " COALESCE (accvoucher.authtotalrecamount,'0') AS authtotalrecamount,"
					+ " COALESCE (accvoucher.voucher_date, '') AS lastreceiptdate,"
					+ " so_notes, so_finstatus_desc,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, branch_id,"
					+ " COALESCE(tblcrm.ticket_id,'0') AS ticket_id,"
					+ " COALESCE(tblcrm.crmconcern_desc,'') AS crmconcern_desc,"
					+ " REPLACE(COALESCE ( ("
					+ " SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
					+ " WHERE tagtrans_customer_id = customer_id ), '' ),',','') AS tag,"
					+ " so_authorize_pdi,"
					+ " so_authorize_accessories,"
					+ " so_authorize_accounts,"
					+ " so_authorize_insurance,"
					+ " so_authorize_registration,"
					+ " so_authorize_deliverycoordinator,"
					+ " so_din_del_location "

					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_type ON fintype_id = so_fintype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN ( SELECT SUM(voucher_amount) AS totalrecamount,"
					+ " SUM( IF ( voucher_authorize = 1, voucher_amount, 0 ) ) AS authtotalrecamount,"
					+ " MAX(voucher_date) AS voucher_date, voucher_so_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1 = 1"
					+ " AND voucher_active = 1"
					+ " AND voucher_vouchertype_id = 9"
					+ " GROUP BY voucher_so_id ) AS accvoucher ON accvoucher.voucher_so_id = so_id "
					+ " LEFT JOIN (SELECT GROUP_CONCAT(ticket_id) AS ticket_id,"
					+ " GROUP_CONCAT(crmconcern_desc) AS crmconcern_desc,"
					+ " crm_so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_crm_id = crm_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id"
					+ " WHERE 1=1"
					+ " AND crmdays_crmtype_id = 2"
					+ " GROUP BY crm_so_id "
					+ " ) AS tblcrm ON tblcrm.crm_so_id = so_id "
					+ TeamJoin
					+ " WHERE so_active = '1'"
					+ " AND so_delivered_date = ''";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			if (!dr_finstatus_id.equals("")) {
				StrSql += " AND so_finstatus_id IN (" + dr_finstatus_id + ")";
			}

			StrSql += BranchAccess
					+ ExeAccess + StrSearch + ""
					+ " GROUP BY so_id"
					// + " HAVING (totalrecamount < so_grandtotal)"
					+ " ORDER BY " + StrOrder + ""
					+ " LIMIT 3000";

			// SOPInfo("PendingDetails---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			double soamount = 0.0;
			double totalrecamount = 0.0;
			Str.append("<div class=\"  table-bordered\">\n");

			if (crs.isBeforeFirst()) {

				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><b>Sales Order</b></th>\n");
				Str.append("<th data-toggle=\"true\"><b>Customer</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Sales Consultant</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>SO Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Tentative Delivery Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Retail Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Delivery Status</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Delivery Process</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Variant</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Fuel Type</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Stock</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Payment Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>SO Amount</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Receipt Amount</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Authorized Receipt Amount</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Last Receipt</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Finance Type</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Finance Status</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Remarks</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>PBF Concern</b></th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (count == 1) {
						strpendingso.append(" so_id = ").append(crs.getString("so_id"));
					} else {
						strpendingso.append(" or so_id = ").append(crs.getString("so_id"));
					}
					Totalcount = Totalcount + 1;
					grandsoamount = grandsoamount + crs.getDouble("so_grandtotal");
					soamount = soamount + crs.getDouble("so_grandtotal");
					grandreceiptamount = grandreceiptamount + crs.getDouble("totalrecamount");
					totalrecamount = totalrecamount + crs.getDouble("totalrecamount");

					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count);
					Str.append("</td>\n");
					Str.append("<td valign=top align=left nowrap>");
					if (!crs.getString("so_id").equals("")) {
						Str.append("<a href=../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(" target=_blank>SO ID: ").append(crs.getString("so_id"))
								.append("</a><br/>");
					}
					if (!crs.getString("so_enquiry_id").equals("0")) {
						Str.append("<a href=../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("so_enquiry_id")).append(" target=_blank>Enquiry ID: ").append(crs.getString("so_enquiry_id"))
								.append("</a><br/>");
					}
					if (!crs.getString("so_quote_id").equals("0")) {
						Str.append("<a href=../sales/veh-quote-list.jsp?quote_id=").append(crs.getString("so_quote_id")).append(" target=_blank>Quote ID: ").append(crs.getString("so_quote_id"))
								.append("</a>");
					}
					if (!crs.getString("ticket_id").equals("0")) {
						Str.append("<br><a href=../sales/report-so-pendingdelivery.jsp?filter=yes&ticket_id=").append(crs.getString("ticket_id")).append(" target=_blank>PBF Ticket ID: ")
								.append(crs.getString("ticket_id"))
								.append("</a>");
						// Str.append("<br>PBF Ticket ID: " + crs.getString("ticket_id"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">");
					Str.append(crs.getString("customer_name")).append("</a></br>");

					// Tags Starts
					String Tag = crs.getString("tag");
					Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='border: 1px solid aliceblue;top:-16px; background:");
					Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
					Tag = ReplaceStr(Tag, "StartName", "");
					Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
					Str.append("</br></br>" + Tag);
					// Tags End

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
						Str.append(strToShortDate(crs.getString("so_retail_date")));
					}
					Str.append("&nbsp;</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("delstatus_name"));
					Str.append("</td>\n");

					Str.append("<td valign=top align=left nowrap>");
					// SOP("so_din_del_location===" + crs.getString("so_din_del_location"));
					if (crs.getString("so_din_del_location").equals("0")) {
						Str.append("No DIN</br>");
					}

					Str.append("PDI: ");
					if (crs.getString("so_authorize_pdi").equals("1")) {
						Str.append("<i class='fa fa-check'></i>");
					} else {
						Str.append("<i class='fa fa-times'></i>");
					}
					Str.append("</br>");
					Str.append("Accessories: ");
					if (crs.getString("so_authorize_accessories").equals("1")) {
						Str.append("<i class='fa fa-check'></i>\n");
					} else {
						Str.append("<i class='fa fa-times'></i>\n");
					}

					Str.append("</br>");
					Str.append("Accounts: ");
					if (crs.getString("so_authorize_accounts").equals("1")) {
						Str.append("<i class='fa fa-check'></i>\n");
					} else {
						Str.append("<i class='fa fa-times'></i>\n");
					}
					Str.append("</br>");
					Str.append("Insurance: ");
					if (crs.getString("so_authorize_insurance").equals("1")) {
						Str.append("<i class='fa fa-check'></i>\n");
					} else {
						Str.append("<i class='fa fa-times'></i>\n");
					}
					Str.append("</br>");
					Str.append("Registration: ");
					if (crs.getString("so_authorize_registration").equals("1")) {
						Str.append("<i class='fa fa-check'></i>\n");
					} else {
						Str.append("<i class='fa fa-times'></i>\n");
					}
					Str.append("</br>");
					Str.append("Delivery Coordinator: ");
					if (crs.getString("so_authorize_deliverycoordinator").equals("1")) {
						Str.append("<i class='fa fa-check'></i>\n");
					} else {
						Str.append("<i class='fa fa-times'></i>\n");
					}

					Str.append("</td>\n");

					Str.append("<td valign=top align=left><a href=../inventory/inventory-item-list.jsp?item_id=");
					Str.append(crs.getString("item_id")).append(">").append(crs.getString("item_name")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("fueltype_name"));
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("so_vehstock_id").equals("0")) {
						Str.append("Stock ID: ").append(crs.getString("so_vehstock_id")).append("<br>");
						if (!crs.getString("vehstock_comm_no").equals("") && !crs.getString("vehstock_comm_no").equals("0")) {
							Str.append("Stock Comm. No.: ").append(crs.getString("vehstock_comm_no")).append("<br>");
						}
						Str.append("Chassis No: ").append(crs.getString("vehstock_chassis_no"));
					} else {
						Str.append("&nbsp;");
					}
					if (!crs.getString("so_stockallocation_time").equals("") && !crs.getString("so_vehstock_id").equals("0")) {
						Str.append("<br/>Allocation: ");
						Str.append("<br/>").append(Math.round(getDaysBetween(crs.getString("so_stockallocation_time"), ToShortDate(kknow())))).append(" Days");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("so_payment_date").equals("")) {
						Str.append(strToShortDate(crs.getString("so_payment_date"))).append("<br>");
						Str.append(Math.round(getDaysBetween(crs.getString("so_payment_date"), ToShortDate(kknow())))).append(" Days");
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append(IndFormat(deci.parse(crs.getString("so_grandtotal")) + ""));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("totalrecamount").equals("")) {
						Str.append(IndFormat(deci.parse(crs.getString("totalrecamount")) + ""));
					}
					Str.append("<td valign=top align=right>");
					if (!crs.getString("authtotalrecamount").equals("")) {
						Str.append(IndFormat(deci.parse(crs.getString("authtotalrecamount")) + ""));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("lastreceiptdate").equals("")) {
						Str.append(strToShortDate(crs.getString("lastreceiptdate"))).append("<br>");
						Str.append(Math.round(getDaysBetween(crs.getString("lastreceiptdate"), ToShortDate(kknow())))).append(" Days");
					}

					if (crs.getString("so_finstatus_id").equals("1") && getDaysBetween(crs.getString("so_date"), ToLongDate(kknow())) > 2) {
						finstatus_color = "red";
					} else if (crs.getString("so_finstatus_id").equals("2") && getDaysBetween(crs.getString("so_date"), ToLongDate(kknow())) > 3) {
						finstatus_color = "red";
					} else if (crs.getString("so_finstatus_id").equals("3") && getDaysBetween(crs.getString("so_date"), ToLongDate(kknow())) > 4) {
						finstatus_color = "red";
					} else if (crs.getString("so_finstatus_id").equals("4") && getDaysBetween(crs.getString("so_date"), ToLongDate(kknow())) > 5) {
						finstatus_color = "red";
					} else if (crs.getString("so_finstatus_id").equals("5") && getDaysBetween(crs.getString("so_date"), ToLongDate(kknow())) > 6) {
						finstatus_color = "red";
					} else {
						finstatus_color = "black";
					}

					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("fintype_name"));
					Str.append("</td>\n");
					Str.append("<td valign=top align=left><font color=\"").append(finstatus_color).append("\">");
					Str.append(crs.getString("finstatus_name")).append("</font>");
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("so_notes"));
					if (!crs.getString("so_finstatus_desc").equals("")) {
						Str.append("</br></br> <b>Finance Description:  </b>" + crs.getString("so_finstatus_desc"));
					}
					Str.append("</td>\n");
					Str.append("<td>\n");
					if (!crs.getString("crmconcern_desc").equals("")) {
						Str.append(crs.getString("crmconcern_desc"));
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
				Str.append("<td align=right><b>").append(IndFormat(deci.format(soamount) + "")).append("</b></td>\n");
				Str.append("<td align=right><b>").append(IndFormat(deci.format(totalrecamount) + "")).append("</b></td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("</tr>\n");
			} else {
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<tr align=center>\n");
				Str.append("<td><br><br><br><br><b><font color=red>No Sales Order(s) found!</font></b><br><br></td>\n");
				Str.append("</tr>\n");
			}
			crs.close();
			Str.append("</table>\n");
			Str.append("</td>\n");
			Str.append("</tr>\n");
			Str.append("<div style=\"text-align:right\"><b>Total Sales Orders:");
			Str.append(Totalcount).append("</div>");
			Str.append("<br><div style=\"text-align:right\"><b>Grand Total Sales Order Amount: ");
			Str.append(IndFormat(deci.format(grandsoamount) + "")).append("</div>");
			Str.append("<br><div style=\"text-align:right\"><b>Grand Total Receipt Amount:");
			Str.append(IndFormat(deci.format(grandreceiptamount) + "")).append("</div>");
			// Str.append("<tr align=center>\n<td align=right><b>Total Sales Orders: ").append(Totalcount);
			// Str.append("<br>Grand Total Sales Order Amount: ").append(IndFormat(grandsoamount
			// + ""));
			// Str.append("<br>Grand Total Receipt Amount: ").append(IndFormat(grandreceiptamount
			// + ""));
			// Str.append("</td>\n");
			// Str.append("</tr>\n");
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
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + " axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutive() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'";

			if (!emp_id.equals("1")) {
				StrSql += " " + ExeAccess + "";
			}
			StrSql = StrSql + " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				// Str.append("<option value=").append(crs.getString("emp_id")).append("");
				// Str.append(StrSelectdrop(crs.getString("emp_id"),
				// executive_id));
				// Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				// Str.append(")</option> \n");
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(" )").append("</option> \n");
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

	public String PopulateModel() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDeliveryStatus(String[] deliverystatus_ids, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " WHERE delstatus_id != 0"
					+ " GROUP BY delstatus_id"
					+ " ORDER BY delstatus_name";
			// SOP("StrSql-----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("delstatus_id"));
				Str.append(ArrSelectdrop(crs.getInt("delstatus_id"), deliverystatus_ids));
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

	public String PopulateFuelType(String[] fueltype_ids, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(ArrSelectdrop(crs.getInt("fueltype_id"), fueltype_ids));
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

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", order_by)).append(">Sales Order Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", order_by)).append(">Sales Order Delivery Status</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", order_by)).append(">Execuitve</option> \n");
		return Str.toString();
	}

	public String PopulateStockAllocate() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", vehstock_allocate)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", vehstock_allocate)).append(">Stock Allocated</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", vehstock_allocate)).append(">Stock Not Allocated</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", vehstock_allocate)).append(">With Retail Date</option> \n");
		Str.append("<option value=4").append(StrSelectdrop("4", vehstock_allocate)).append(">Without Retail Date</option> \n");
		Str.append("<option value=5").append(StrSelectdrop("5", vehstock_allocate)).append(">Stock allocated Without Retail Date</option> \n");
		Str.append("<option value=6").append(StrSelectdrop("6", vehstock_allocate)).append(">Delivery Process</option> \n");
		Str.append("<option value=7").append(StrSelectdrop("7", vehstock_allocate)).append(">Invoice Requested</option> \n");
		return Str.toString();
	}

	public String PopulateFinanceStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT finstatus_id, finstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_status"
					+ " WHERE finstatus_active = 1"
					+ " GROUP BY finstatus_id"
					+ " ORDER BY finstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("finstatus_id"));
				Str.append(ArrSelectdrop(crs.getInt("finstatus_id"), dr_finstatus_ids));
				Str.append(">").append(crs.getString("finstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateStatus() {
	// try {
	// StringBuilder Str = new StringBuilder();
	// StrSql = "SELECT delstatus_id, delstatus_name"
	// + " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
	// + " ORDER BY delstatus_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getInt("delstatus_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("delstatus_id"), status_id));
	// Str.append(">").append(crs.getString("delstatus_name")).append("</option> \n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	protected String BookingDetails(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ";

			if (brand_id.equals("7")) {
				StrSql += " 'Kairali' AS 'Dealer Name',"
						+ " branch_code AS 'Dealer Code',"
						+ " branch_name AS 'Branch',"
						+ " COALESCE(enquiry_ford_qcsid, 0) AS 'QCS ID', "
						+ " COALESCE(enquiry_ford_qpdid, 0) AS 'Cupid ID',"
						+ " 'Alive' AS 'Booking Status',"
						+ " customer_name AS 'Customer Name',"
						+ " customer_mobile1 AS 'Mobile No.',"
						+ " SUBSTR(so_date, 7, 2) AS 'Date',"
						+ " SUBSTR(so_date, 5, 2) AS 'Month',"
						+ " SUBSTR(so_date, 1, 4) AS 'Year',"
						+ " COALESCE(so_notes, '') AS 'Booking Remarks',"
						+ " COALESCE(model_name, '') AS 'Brand',"
						+ " COALESCE(item_name, '') AS 'Model',"
						+ " COALESCE(option_name, '') AS 'Colour',"
						+ " COALESCE(veh_engine_no, '') AS 'VIN alloted',"
						+ " DATE_FORMAT(so_promise_date,'%d/%m/%Y') AS 'Actual Delivery  date',"
						+ " '' AS 'ESB Sold',"
						+ " IF(so_mga_amount=0,'',so_mga_amount) AS 'Accessories Sold Value',"
						+ " '' AS 'Booking is against a Declared Vehicle (Yes / No)',"
						+ " COALESCE(enquiry_ford_nscordernumber, '') AS 'April Order Bank (NSC Order no)',"
						+ " COALESCE(enquiry_ford_vistacontractnumber, '') AS 'Vista Contract No'";
			} else {
				StrSql += " so_id AS 'SO ID',"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch',"
						+ " COALESCE(CONCAT(so_prefix, so_no), '') AS 'SO NO',"
						+ " IF(so_date != '', DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date',"
						+ " IF(so_retail_date != '', DATE_FORMAT(so_retail_date, '%d/%m/%Y %h:%i'), '') AS 'Retail Date',"
						+ " IF(so_delivered_date != '', DATE_FORMAT(so_delivered_date, '%d/%m/%Y %h:%i'), '') AS 'Delivered Date',"
						+ " COALESCE(so_netamt, '') AS 'SO Net Amount',"
						+ " IF(so_active = 1, 'Yes', 'No') AS 'Active',"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname, ' (', contact_id, ')') AS 'Contact',"
						+ " COALESCE(contact_mobile1, '') AS 'Contact Mobile1',"
						+ " COALESCE(contact_mobile2, '') AS 'Contact Mobile2',"
						+ " COALESCE(contact_email1, '') AS 'Contact Email1',"
						+ " COALESCE(contact_email2, '') AS 'Contact Email2',"
						+ " COALESCE(contact_phone1, '') AS 'Contact Phone1',"
						+ " COALESCE(contact_phone2, '') AS 'Contact Phone2',"
						+ " COALESCE(CONCAT(customer_name, ' (', customer_id, ')'), '') AS 'Customer',"
						+ " COALESCE(e.emp_name, '') AS 'Employee Name',"
						+ " COALESCE(e.emp_ref_no, '') AS 'EMP REF NO',"
						+ " COALESCE(t.emp_name, '') AS 'Team Leader',"
						+ " COALESCE(t.emp_ref_no, '') AS 'EMP REF NO',"
						+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS Item,"
						+ " COALESCE(option_name, '') AS 'Booking Colour',"
						+ " COALESCE(so_booking_amount, 0) AS 'Booking Amount',"
						+ " COALESCE(bank_name, '') AS 'Finance Bank',"
						+ " COALESCE(so_notes, '') AS 'Notes'";
			}

			StrSql += " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp t on t.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = so_finstatus_bank_id";

			if (brand_id.equals("7")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_so_id = so_id";
			}

			StrSql += " WHERE 1 = 1"
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ StrSearch
					+ " AND so_active =1"
					+ " AND so_retail_date = ''"
					+ " GROUP BY so_id"
					+ " ORDER BY so_id DESC"
					+ " LIMIT " + exportcount + "";
			// SOP("StrSql-------bookingsdetails------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				new ExportToXLSX().Export(request, response, crs, "BookingDetails", comp_id);
			} else {
				msg = "No Records To Export!";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	private void RedirectSOList(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			String ticket_ids = PadQuotes(RetrunSelectArrVal(request, "ticket_id"));
			// ticket
			if (!ticket_ids.equals("")) {
				StrFilter += " AND ticket_id IN (" + ticket_ids + ") ";
			}
			SetSession("ticketstrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../service/ticket-list.jsp?smart=yes"));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}