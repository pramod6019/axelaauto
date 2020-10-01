package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Stock_Status extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "", BranchAccess = "";
	public String msg = "";
	public String so_id = "";
	public String model_id = "";
	public String status_id = "";
	public String pending_delivery = "";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "";
	public String delstatus_id = "";
	public String vehstock_branch_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
			status_id = CNumeric(PadQuotes(request.getParameter("dr_status_id")));
			pending_delivery = PadQuotes(request.getParameter("dr_pending_delivery_id"));
			vehstock_blocked = PadQuotes(request.getParameter("dr_blocked"));
			order_by = PadQuotes(request.getParameter("dr_order_by"));
			delstatus_id = CNumeric(PadQuotes(request.getParameter("dr_delstatus_id")));
			vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_branch")));

			if (!vehstock_branch_id.equals("0")) {
				StrSearch = StrSearch + " and vehstock_branch_id = " + vehstock_branch_id;
			}
			if (!model_id.equals("0")) {
				StrSearch = StrSearch + " and model_id = " + model_id;
			}
			if (!status_id.equals("0")) {
				StrSearch = StrSearch + " and vehstock_status_id = " + status_id;
			}
			if (!pending_delivery.equals("") && !pending_delivery.equals("0")) {
				StrSearch = StrSearch + " and pendingdelivery = " + pending_delivery;
			}
			if (!delstatus_id.equals("0")) {
				StrSearch = StrSearch + " and vehstock_delstatus_id = " + delstatus_id;
			}
			if (!vehstock_blocked.equals("") && !vehstock_blocked.equals("-1")) {
				StrSearch = StrSearch + " and vehstock_blocked = " + vehstock_blocked;
			}
			if (!order_by.equals("") && !order_by.equals("0")) {
				if (order_by.equals("1")) {
					StrOrder = " vehstock_invoice_date ";
				}
				if (order_by.equals("2")) {
					StrOrder = " vehstock_arrival_date ";
				}
				if (order_by.equals("3")) {
					StrOrder = " status_name ";
				}
				if (order_by.equals("4")) {
					StrOrder = " quotecount ";
				}
			} else if (order_by.equals("") || order_by.equals("0")) {
				StrOrder = " vehstock_status_id, model_name, item_name, vehstock_invoice_date ";
			}

			try {
				CheckForm();
				if (msg.equals("")) {
					// StrSearch = StrSearch + BranchAccess;
					SetSession("stockstrsql", StrSearch, request);
					StrHTML = StockDetail();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	public void CheckForm() {
		msg = "";
		try {
			if (vehstock_branch_id.equals("0")) {
				msg = msg + "<br>Select Branch!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String StockDetail() {
		String SqlJoin = "";
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		int grandcarcost = 0;
		int grandexshowroomprice = 0;
		int grandfinanceamt = 0;
		int grandretailfinanceamt = 0;
		int grandsoamount = 0;
		int grandreceiptamount = 0;
		int Totalcount = 0;

		try {
			Str.append("\n <table cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr><td align=center>");
			StrSql = " Select * from (  (Select vehstock_id, vehstock_parking_no, vehstock_location_id, vehstocklocation_name, vehstock_chassis_no, vehstock_engine_no, "
					+ " if(vehstock_invoice_date='','99999999999999',vehstock_invoice_date) as vehstock_invoice_date,vehstock_invoice_no,vehstock_invoice_amount,vehstock_delstatus_id, "
					+ " vehstock_arrival_date,vehstock_pi_date, item_name, item_code, model_id, model_name,so_delivered_date, "
					+ " vehstock_item_id, vehstock_ex_price, delstatus_name, "
					+ " vehstock_status_id, status_name, 2 as pendingdelivery, vehstock_intransit_damage, "
					+ " vehstock_rectification_date, vehstock_blocked,  CONCAT(branch_name,' (',branch_code,')') as branchname, branch_id, "
					+ " count(distinct quote_id) as quotecount, "
					+ " 0 as so_id, '' as so_no, '' as so_date, 0 as customer_id, '' as customer_name,"
					+ " 0 as so_grandtotal, 0 as so_receiptamount,"
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=1 and option_vehstock_id=vehstock_id limit 1),'') as paintwork, "
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=2 and option_vehstock_id=vehstock_id limit 1),'') as upholstery, "
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=3 and option_vehstock_id=vehstock_id limit 1),'') as optionname "
					+ " FROM " + compdb(comp_id) + "axela_vehstock "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_location on vehstocklocation_id = vehstock_vehstocklocation_id "
					+ " inner join " + compdb(comp_id) + "axela_branch on vehstock_branch_id = branch_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_vehstock_id = vehstock_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
					+ " inner join " + compdb(comp_id) + "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_status on status_id = vehstock_status_id and status_id!=0 "
					+ " left join " + compdb(comp_id) + "axela_sales_quote on quote_vehstock_id = vehstock_id"
					+ " left join " + compdb(comp_id) + "axela_sales_so on so_vehstock_id = quote_vehstock_id "
					+ " where 1=1 " + StrSearch + " and vehstock_id not in "
					+ " (select so.so_vehstock_id "
					+ " from " + compdb(comp_id) + "axela_sales_so so"
					+ " where so.so_active='1') group by vehstock_id)  UNION "
					+ " (Select vehstock_id, vehstock_chassis_no,vehstock_parking_no,vehstocklocation_id, vehstocklocation_name, vehstock_engine_no,"
					+ " if(vehstock_invoice_date='','99999999999999',vehstock_invoice_date) as vehstock_invoice_date,vehstock_invoice_no,vehstock_invoice_amount,vehstock_delstatus_id, "
					+ " vehstock_arrival_date,vehstock_pi_date,item_name, item_code, model_id, model_name,so_delivered_date, "
					+ " vehstock_item_id, vehstock_ex_price, delstatus_name, "
					+ " vehstock_status_id, status_name, 2 as pendingdelivery, vehstock_intransit_damage, "
					+ " vehstock_rectification_date, vehstock_blocked,  CONCAT(branch_name,' (',branch_code,')') as branchname, branch_id, "
					+ " count(distinct quote_id) as quotecount, "
					+ " so_id, concat(branch_code,so_no) as so_no, so_date, customer_id, customer_name,so_grandtotal, "
					+ " coalesce((select sum(receipt_amount) from " + compdb(comp_id) + "axela_invoice_receipt "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_invoice ON invoice_id = receipt_invoice_id "
					+ " where so_id = invoice_so_id),0) as so_receiptamount, "
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=1 and option_vehstock_id=vehstock_id limit 1),'') as paintwork, "
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=2 and option_vehstock_id=vehstock_id limit 1),'') as upholstery, "
					+ " coalesce((select group_concat(distinct concat('<div title=\"',item_small_desc,'\" style=\"display: inline\">',item_code,'</div>') order by item_code separator ', ') "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_item_id = item_id "
					+ " where item_optiontype_id=3 and option_vehstock_id=vehstock_id limit 1),'') as optionname "
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " inner join " + compdb(comp_id) + "axela_vehstock_location on vehstocklocation_id = vehstock_vehstocklocation_id "
					+ " inner join " + compdb(comp_id) + "axela_branch on vehstock_branch_id = branch_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_vehstock_id = vehstock_id "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
					+ " inner join " + compdb(comp_id) + "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id "
					+ " inner join " + compdb(comp_id) + "axela_vehstock_status on status_id = vehstock_status_id and status_id!=0 "
					+ " left join " + compdb(comp_id) + "axela_sales_so on so_vehstock_id = vehstock_id "
					+ " left join " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id "
					+ " left join " + compdb(comp_id) + "axela_sales_quote on quote_vehstock_id = vehstock_id "
					+ " where so_active = '1'" + StrSearch + ""
					+ " group by vehstock_id) ) as t ";

			// StrSql = StrSql + " where 1=1 ";

			StrSql = StrSql + " order by " + StrOrder;// order by status_name, model_name, item_name, vehstock_invoice_date";
			// SOP("StrSql============" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			int exshowroomprice = 0;
			int invoiceamount = 0;
			int soamount = 0;
			int receiptamount = 0;
			String blocked = "";
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			if (crs.isBeforeFirst()) {
				Str.append("<tr align=center>\n");
				Str.append("<td width=5%><b>Sl.#</b></td>\n");
				// Str.append("<td><b>Comm. No.</b></td>\n");
				Str.append("<td><b>Chassis No.</b></td>\n");
				Str.append("<td><b>Engine No.</b></td>\n");
				Str.append("<td><b>Paint Work</b></td>\n");
				Str.append("<td><b>Upholstery</b></td>\n");
				Str.append("<td><b>Options</b></td>\n");
				Str.append("<td><b>Package</b></td>\n");
				Str.append("<td><b>Location</b></td>\n");
				Str.append("<td><b>Park. No.</b></td>\n");
				Str.append("<td><b>Invoice No.</b></td>\n");
				Str.append("<td><b>Invoice Date</b></td>\n");
				Str.append("<td><b>Pro Forma Date</b></td>\n");
				Str.append("<td><b>Car Cost</b></td>\n");
				Str.append("<td><b>Arrival Date</b></td>\n");
				Str.append("<td><b>Delivery Status</b></td>\n");
				Str.append("<td><b>Delivery Date</b></td>\n");
				Str.append("<td><b>Status</b></td>\n");
				Str.append("<td><b>Nature of Damage</b></td>\n");
				Str.append("<td><b>Quote Count</b></td>\n");
				Str.append("<td><b>Sales Order</b></td>\n");
				Str.append("<td><b>SO Amount</b></td>\n");
				Str.append("<td><b>Receipt Amount</b></td>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					Totalcount = Totalcount + 1;
					count = count + 1;
					grandcarcost = grandcarcost + crs.getInt("vehstock_invoice_amount");
					invoiceamount = invoiceamount + crs.getInt("vehstock_invoice_amount");
					grandexshowroomprice = grandexshowroomprice + crs.getInt("vehstock_ex_price");
					exshowroomprice = exshowroomprice + crs.getInt("vehstock_ex_price");
					grandsoamount = grandsoamount + crs.getInt("so_grandtotal");
					soamount = soamount + crs.getInt("so_grandtotal");
					grandreceiptamount = grandreceiptamount + crs.getInt("so_receiptamount");
					receiptamount = receiptamount + crs.getInt("so_receiptamount");
					if (crs.getString("vehstock_status_id").equals("1")) {
						bgcol = "#dbebff";
					} else if (crs.getString("vehstock_status_id").equals("2")) {
						bgcol = "#ffdfdf";
					} else if (crs.getString("vehstock_status_id").equals("3")) {
						bgcol = "#caffd8";
					} else if (crs.getString("vehstock_status_id").equals("4")) {
						bgcol = "#eeeeee";
					} else if (crs.getString("vehstock_status_id").equals("5")) {
						bgcol = "#cccccc";
					}
					Str.append("<tr bgcolor=" + bgcol + ">\n");
					Str.append("<td valign=top align=center ><b>" + count + ".</b></td>\n");
					// Str.append("<td valign=top align=center><a href=\"stock-update.jsp?Update=yes&vehstock_id=" + crs.getString("vehstock_id") + " \"target=_blank\">" +
					// crs.getString("vehstock_comm_no") +
					// "</a><br>ID: " + crs.getString("vehstock_id") + "");
					// if (crs.getString("vehstock_blocked").equals("1")) {
					// Str.append("<br><font color=#ff0000>Blocked</font>");
					// }
					// Str.append("</td>");
					Str.append("<td valign=top align=center nowrap>" + crs.getString("vehstock_chassis_no"));
					if (crs.getString("vehstock_blocked").equals("1")) {
						Str.append("<br><font color=#ff0000>Blocked</font>");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left>" + crs.getString("vehstock_engine_no") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("item_name") + " (" + crs.getString("item_code") + ")</td>");
					Str.append("<td valign=top align=left>" + crs.getString("paintwork") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("upholstery") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("optionname") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("vehstocklocation_name") + "</td>");
					if (!crs.getString("vehstock_parking_no").equals("0")) {
						Str.append("<td valign=top align=right>" + crs.getString("vehstock_parking_no") + "</td>");
					} else {
						Str.append("<td valign=top align=right>&nbsp;</td>");
					}
					Str.append("<td valign=top align=right>" + crs.getString("vehstock_invoice_no") + "</td>");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("vehstock_invoice_date").equals("99999999999999")) {
						Str.append(strToShortDate(crs.getString("vehstock_invoice_date")) + "<br>" + Math.round(getDaysBetween(crs.getString("vehstock_invoice_date"), ToShortDate(kknow()))) + " Days");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("vehstock_pi_date").equals("")) {
						Str.append(strToShortDate(crs.getString("vehstock_pi_date")) + "<br>" + Math.round(getDaysBetween(crs.getString("vehstock_pi_date"), ToShortDate(kknow()))) + " Days");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right>" + IndFormat(crs.getString("vehstock_invoice_amount")) + "</td>");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("vehstock_arrival_date").equals("")) {
						Str.append(strToShortDate(crs.getString("vehstock_arrival_date")) + "<br>" + Math.round(getDaysBetween(crs.getString("vehstock_arrival_date"), ToShortDate(kknow()))) + " Days");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=left>" + crs.getString("delstatus_name") + "</td>");
					Str.append("<td valign=top align=left>" + strToShortDate(crs.getString("so_delivered_date")) + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("status_name") + "</td>");
					Str.append("<td valign=top align=left>" + crs.getString("vehstock_intransit_damage") + "");
					if (!crs.getString("vehstock_rectification_date").equals("")) {
						Str.append("<br>Rectification Date: " + strToShortDate(crs.getString("vehstock_rectification_date")) + "");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right>" + crs.getString("quotecount") + "</td>");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("so_no").equals("")) {
						Str.append("<a href=salesorder-list.jsp?so_id=" + crs.getString("so_id") + ">SO ID: " + crs.getString("so_id") + "</a>");
					}
					if (!crs.getString("so_date").equals("")) {
						Str.append("<br>" + strToShortDate(crs.getString("so_date")) + "<br>" + Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow()))) + " Days");
					}
					if (!crs.getString("customer_name").equals("")) {
						Str.append("<br>" + crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append("" + IndFormat(crs.getString("so_grandtotal")) + "");
					}
					Str.append("</td>");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_receiptamount").equals("")) {
						Str.append("" + IndFormat(crs.getString("so_receiptamount")) + "");
					}
					Str.append("</td>");
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
				Str.append("<td><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(IndFormat(invoiceamount + "")).append("</b></td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td align=right><b>" + IndFormat(soamount + "") + "</b></td>\n");
				Str.append("<td align=right><b>" + IndFormat(receiptamount + "") + "</b></td>\n");
				Str.append("</tr>\n");
			} else {
				Str.append("<tr align=center>\n");
				Str.append("<td><br><br><br><br><b><font color=red>No Stock(s) found!</font></b><br><br></td>\n");
				Str.append("</tr>\n");
			}
			Str.append("</table>\n");
			Str.append("</td></tr>");
			Str.append("<tr align=center><td align=right><b>Total Stock: " + Totalcount + " <br>Total Car Cost: " + IndFormat(grandcarcost + "") + ""
					+ "<br>Total Financed Amount: " + IndFormat(grandfinanceamt + "") + "<br>Total Retail Financed Amount: " + IndFormat(grandretailfinanceamt + "")
					+ "<br>Total Sales Order Amount: " + IndFormat(grandsoamount + "") + "<br>Total Receipt Amount: " + IndFormat(grandreceiptamount + "") + "</b></td></tr>");
			Str.append("</table>");
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

	public String PopulateModel() {
		try {
			StrSql = "SELECT model_id, model_name FROM " + compdb(comp_id) + "axela_inventory_item_model order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("model_id") + "";
				group = group + StrSelectdrop(crs.getString("model_id"), model_id);
				group = group + ">" + crs.getString("model_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus() {
		try {
			StrSql = "SELECT status_id, status_name FROM " + compdb(comp_id) + "axela_vehstock_status where 1=1 and status_id!=0 order by status_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("status_id") + "";
				group = group + StrSelectdrop(crs.getString("status_id"), status_id);
				group = group + ">" + crs.getString("status_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePendingdelivery() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0" + StrSelectdrop("0", pending_delivery) + ">Select</option> \n");
		Str.append("<option value=1" + StrSelectdrop("1", pending_delivery) + ">Yes</option> \n");
		Str.append("<option value=2" + StrSelectdrop("2", pending_delivery) + ">No</option> \n");
		return Str.toString();

	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0" + StrSelectdrop("0", order_by) + ">Select</option> \n");
		Str.append("<option value=1" + StrSelectdrop("1", order_by) + ">Invoice Date</option> \n");
		Str.append("<option value=2" + StrSelectdrop("2", order_by) + ">Arrival Date</option> \n");
		Str.append("<option value=3" + StrSelectdrop("3", order_by) + ">Status</option> \n");
		Str.append("<option value=4" + StrSelectdrop("4", order_by) + ">Quote Count</option> \n");
		return Str.toString();

	}

	public String PopulateBlocked() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1" + StrSelectdrop("-1", vehstock_blocked) + ">Select</option> \n");
		Str.append("<option value=0" + StrSelectdrop("0", vehstock_blocked) + ">Not Blocked</option> \n");
		Str.append("<option value=1" + StrSelectdrop("1", vehstock_blocked) + ">Blocked</option> \n");
		return Str.toString();

	}

	public String PopulateDeliveryStatus() {
		try {
			StrSql = "SELECT delstatus_id, delstatus_name FROM " + compdb(comp_id) + "axela_sales_so_delstatus order by delstatus_rank";
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
