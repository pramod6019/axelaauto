package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SO_Promised extends Connect {

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
	public String critical = "";
	public String order_by = "";
	public String dr_branch_id = "0", executive_id = "0";
	public String StrOrder = "", ExeAccess = "";
	StringBuilder strpendingso = new StringBuilder();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_report_access,emp_mis_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			model_id = PadQuotes(request.getParameter("dr_model_id"));
			status_id = PadQuotes(request.getParameter("dr_status_id"));
			critical = PadQuotes(request.getParameter("dr_critical"));
			order_by = PadQuotes(request.getParameter("dr_order_by"));
			executive_id = PadQuotes(request.getParameter("dr_executive_id"));

			if (!executive_id.equals("") && !executive_id.equals("0")) {
				StrSearch += " and so_emp_id = " + executive_id;
			}
			if (!model_id.equals("") && !model_id.equals("0")) {
				StrSearch += " and model_id = " + model_id;
			}
			if (!status_id.equals("") && !status_id.equals("-1") && !status_id.equals("0")) {
				StrSearch += " and delstatus_id = " + status_id;
			}
			if (!critical.equals("") && !critical.equals("-1")) {
				StrSearch += " and so_critical = " + critical;
			}
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
				StrOrder = " model_name, item_name, so_date";
			}

			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			if (branch_id.equals("0")) {
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			} else {
				dr_branch_id = branch_id;
			}
			if (!dr_branch_id.equals("0")) {
				StrSearch += " and so_branch_id = " + dr_branch_id + "";
			} else {
				msg = msg + "<br>Select Branch!";
			}
			try {
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (msg.equals("")) {
					StrHTML = PendingDetails();
				}
				SetSession("sostrsql", strpendingso.toString(), request);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PendingDetails() {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		int grandsoamount = 0;
		int grandinvoiceamount = 0;
		int grandreceiptamount = 0;
		int Totalcount = 0;
		try {
			StrSql = "Select branch_id, concat(branch_name,' (',branch_code,')') as branch_name"
					+ " from " + compdb(comp_id) + "axela_branch"
					+ " where branch_active = '1' " + BranchAccess;
			if (!dr_branch_id.equals("0")) {
				StrSql += " and branch_id = " + dr_branch_id;
			}
			StrSql += " order by branch_name";

			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<HTML>");
				Str.append("<HEAD>");
				Str.append("<LINK REL=\"STYLESHEET\" TYPE=\"text/css\" HREF=\"Library/portal-style.css\">");
				Str.append("</HEAD>");
				Str.append("<body bgColor=\"#ffffff\" leftmargin=\"0\" rightmargin=\"0\" topmargin=\"0\" bottommargin=\"0\">");
				Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs1.next()) {
					Str.append("<tr align=center><td align=center><br><b>").append(crs1.getString("branch_name")).append("</b></td></tr>");
					Str.append("<tr><td align=center>");

					StrSql = "Select so_id, concat('SO',branch_code,so_no) as so_no, so_enquiry_id,"
							+ " coalesce(CONCAT('QT',branch_code, quote_no),'') as quote_no, so_quote_id,"
							+ " coalesce(CONCAT('ENQUIRY',branch_code, enquiry_no),'') as enquiry_no,"
							+ " customer_id, customer_name, so_date, so_promise_date, so_delivered_date,"
							+ " so_payment_date, COALESCE(delstatus_id, 0) AS delstatus_id, so_retail_date,"
							+ " COALESCE(delstatus_name, '') AS delstatus_name, so_grandtotal,"
							+ " group_concat(distinct(item_name) separator '<br>') item_name,"
							+ " coalesce(model_name,'') as model_name, COALESCE((select sum(invoice_grandtotal)"
							+ " from " + compdb(comp_id) + "axela_invoice"
							+ " where invoice_so_id = so_id and invoice_active = '1'),0) as invoiceamount,"
							+ " COALESCE((select sum(receipt_amount)"
							+ " from " + compdb(comp_id) + "axela_invoice_receipt"
							+ " where receipt_invoice_id = invoice_id and receipt_active = '1'),0) as receiptamount,"
							+ " COALESCE(max(receipt_date),'') as lastreceiptdate,"
							+ " coalesce(CONCAT(emp_name,' (',emp_ref_no,')'),'') as emp_name, emp_id,"
							+ " CONCAT(branch_name,' (',branch_code,')') as branchname, branch_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " inner join " + compdb(comp_id) + "axela_branch on so_branch_id = branch_id"
							+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
							+ " inner join " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
							+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
							+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
							+ " LEFT join " + compdb(comp_id) + "axela_vehstock on vehstock_id = so_vehstock_id"
							+ " LEFT join " + compdb(comp_id) + "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id"
							+ " left join " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
							+ " left join " + compdb(comp_id) + "axela_sales_quote on quote_id = so_quote_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = so_enquiry_id"
							+ " left join " + compdb(comp_id) + "axela_invoice on invoice_so_id = so_id and invoice_active = '1'"
							+ " left join " + compdb(comp_id) + "axela_invoice_receipt on receipt_invoice_id = invoice_id and receipt_active = '1'"
							+ " where so_active = '1'"
							+ " and branch_id = " + crs1.getString("branch_id") + " " + StrSearch + ""
							+ " group by so_id"
							+ " having (so_delivered_date <= so_promise_date)"
							+ " order by " + StrOrder + ""
							+ " limit 100";
					// SOP("StrSql==" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					int soamount = 0;
					int invoiceamount = 0;
					int receiptamount = 0;
					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					if (crs.isBeforeFirst()) {
						Str.append("<tr align=center>\n");
						Str.append("<td width=5%><b>#</b></td>\n");
						Str.append("<td><b>Sales Order</b></td>\n");
						Str.append("<td><b>Quote</b></td>\n");
						Str.append("<td><b>Enquiry</b></td>\n");
						Str.append("<td><b>Customer</b></td>\n");
						Str.append("<td><b>Sales Consultant</b></td>\n");
						Str.append("<td><b>SO Date</b></td>\n");
						Str.append("<td><b>Tentative Delivery Date</b></td>\n");
						Str.append("<td><b>Retail Date</b></td>\n");
						Str.append("<td><b>Delivered Date</b></td>\n");
						Str.append("<td><b>Delivery Status</b></td>\n");
						Str.append("<td><b>Item</b></td>\n");
						Str.append("<td><b>Payment Date</b></td>\n");
						Str.append("<td><b>SO Amount</b></td>\n");
						Str.append("<td><b>Invoice Amount</b></td>\n");
						Str.append("<td><b>Receipt Amount</b></td>\n");
						Str.append("<td><b>Last Receipt</b></td>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							if (count == 0) {
								strpendingso.append(" and so_id = ").append(crs.getString("so_id"));
							} else {
								strpendingso.append(" or so_id = ").append(crs.getString("so_id"));
							}
							Totalcount = Totalcount + 1;
							count = count + 1;
							grandsoamount = grandsoamount + crs.getInt("so_grandtotal");
							soamount = soamount + crs.getInt("so_grandtotal");
							grandinvoiceamount = grandinvoiceamount + crs.getInt("invoiceamount");
							invoiceamount = invoiceamount + crs.getInt("invoiceamount");
							grandreceiptamount = grandreceiptamount + crs.getInt("receiptamount");
							receiptamount = receiptamount + crs.getInt("receiptamount");
							if (crs.getString("delstatus_id").equals("1")) {
								bgcol = "#dbebff";
							} else if (crs.getString("delstatus_id").equals("2")) {
								bgcol = "#ffdfdf";
							} else if (crs.getString("delstatus_id").equals("3")) {
								bgcol = "#caffd8";
							} else if (crs.getString("delstatus_id").equals("4")) {
								bgcol = "#eeeeee";
							} else if (crs.getString("delstatus_id").equals("5")) {
								bgcol = "#cccccc";
							} else {
								bgcol = "#ffffff";
							}
							Str.append("<tr bgcolor=").append(bgcol).append(">\n");
							Str.append("<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=left nowrap>");
							if (!crs.getString("so_no").equals("")) {
								Str.append("<a href=salesorder-update.jsp?update=yes&so_id=").append(crs.getString("so_id")).append(" target=_blank>SO ID: ").append(crs.getString("so_id"))
										.append("</a>");
								Str.append("<a href=salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(" target=_blank><br>").append(crs.getString("so_no")).append("</a>");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=left nowrap>");
							if (!crs.getString("quote_no").equals("")) {
								Str.append("<a href=quote-update.jsp?update=yes&quote_id=").append(crs.getString("so_quote_id")).append(" target=_blank>Quote ID: ")
										.append(crs.getString("so_quote_id")).append("</a>");
								Str.append("<a href=quote-list.jsp?quote_id=").append(crs.getString("so_quote_id")).append(" target=_blank><br>").append(crs.getString("quote_no")).append("</a>");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=left nowrap>");
							if (!crs.getString("enquiry_no").equals("")) {
								Str.append("<a href=enquiry-update.jsp?update=yes&enquiry_id=").append(crs.getString("so_enquiry_id")).append(" target=_blank>Enquiry ID: ")
										.append(crs.getString("so_enquiry_id")).append("</a>");
								Str.append("<a href=enquiry-list.jsp?enquiry_id=").append(crs.getString("so_enquiry_id")).append(" target=_blank><br>").append(crs.getString("enquiry_no"))
										.append("</a>");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">");
							Str.append(crs.getString("customer_name")).append("</a>");
							Str.append("</td>");
							Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
									.append("</a>");
							if (!crs.getString("enquiry_no").equals("")) {
								Str.append("<br><a href=\"javascript:remote=window.open('enquiry-dash.jsp?pop=yes&enquiry_id=");
								Str.append(crs.getString("so_enquiry_id")).append("','enquirypop','');remote.focus();\">");
								Str.append(crs.getString("enquiry_no")).append("</a>");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_date").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("so_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_promise_date").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("so_promise_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_promise_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_retail_date").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("so_retail_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_retail_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_delivered_date").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("so_delivered_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_delivered_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=left>").append(crs.getString("delstatus_name")).append("</td>");

							Str.append("<td valign=top align=left>").append(crs.getString("item_name")).append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("so_payment_date").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("so_payment_date"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("so_payment_date"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");

							Str.append("<td valign=top align=right>");
							if (!crs.getString("so_grandtotal").equals("")) {
								Str.append("").append(IndFormat(crs.getString("so_grandtotal"))).append("");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=right>");
							if (!crs.getString("invoiceamount").equals("")) {
								Str.append("").append(IndFormat(crs.getString("invoiceamount"))).append("");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=right>");
							if (!crs.getString("receiptamount").equals("")) {
								Str.append("").append(IndFormat(crs.getString("receiptamount"))).append("");
							}
							Str.append("</td>");
							Str.append("<td valign=top align=center>");
							if (!crs.getString("lastreceiptdate").equals("")) {
								Str.append("").append(strToShortDate(crs.getString("lastreceiptdate"))).append("<br>");
								Str.append(Math.round(getDaysBetween(crs.getString("lastreceiptdate"), ToShortDate(kknow())))).append(" Days");
							}
							Str.append("</td>");
							Str.append("</tr>\n");
						}
						crs.close();
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
						Str.append("<td align=right><b>").append(IndFormat(soamount + "")).append("</b></td>\n");
						Str.append("<td align=right><b>").append(IndFormat(invoiceamount + "")).append("</b></td>\n");
						Str.append("<td align=right><b>").append(IndFormat(receiptamount + "")).append("</b></td>\n");
						Str.append("<td>&nbsp;</td>\n");
						Str.append("</tr>\n");
					} else {
						Str.append("<tr align=center>\n");
						Str.append("<td><br><br><br><br><b><font color=red>No Sales Order(s) found!</font></b><br><br></td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</table>\n");
				}
				Str.append("</td></tr>");
				Str.append("<tr align=center><td align=right><b>Total Sales Orders: ").append(Totalcount);
				Str.append(" <br>Grand Total Sales Order Amount: ").append(IndFormat(grandsoamount + ""));
				Str.append("<br>Grand Total Invoice Amount: ").append(IndFormat(grandinvoiceamount + "")).append("");
				Str.append("<br>Grand Total Receipt Amount: ").append(IndFormat(grandreceiptamount + "")).append("");
				Str.append("</td></tr>");
				Str.append("</table>");
				Str.append("</body>\n");
				Str.append("</HTML>\n");
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
			StrSql += " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option> \n");
			}
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

	public String PopulateCritical() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1").append(StrSelectdrop("-1", critical)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", critical)).append(">Critical</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", critical)).append(">Not Critical</option> \n");
		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", order_by)).append(">Sales Order Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", order_by)).append(">Sales Order Delivery Status</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", order_by)).append(">Execuitve Name</option> \n");
		return Str.toString();
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
}
