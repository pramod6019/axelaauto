package axela.sales;
//aJIt 07th June, 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Salesorder_Payment_Track extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String add = "";
	public String addB = "";
	public String financeAddB = "";
	public String msg = "";
	public String finance_msg = "";
	public String so_id = "0";
	public String status = "";
	public String StrSql = "";
	public String StrSql1 = "";
	public String track_financecomp_id = "0";
	public String track_entry_id = "0", track_entry_date = "";
	public String entry_by = "";
	public String contact_id = "0", contact_name = "";
	public String customer_id = "0", customer_name = "";
	public String so_date = "", sodate = "";
	public String QueryString = "";
	public String payment_from = "";
	public String days = "0";
	public String received_by_id = "";
	public String received_by_name = "";
	public String so_amt_date = "";
	public String so_track_receivedby_id = "";
	public String payment_chk_msg = "";
	public String calculateB = "";
	public String so_finstatus_id = "0", pre_so_finstatus_id = "0";
	public String so_finstatus_date = "";
	public String so_finstatus_emp_id = "0";
	public int no_days = 0;
	public int track_count = 0;
	public int so_amt_list = 0;
	public int so_sum_amt = 0;
	public int track_prev_count = 0;
	public int so_amt = 0;
	public int payment_track_total = 0;
	public Connection conntx = null;
	public Statement stmttx = null;
	public String payment_track_amt = "0";
	public String finnancetrans_desc = "";
	ArrayList track_prev_date = new ArrayList();
	ArrayList<String> track_prev_amt = new ArrayList<String>();
	ArrayList<String> track_prev_fincomp_id = new ArrayList<String>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				calculateB = PadQuotes(request.getParameter("txt_calculate"));
				financeAddB = PadQuotes(request.getParameter("txt_add_status"));
				msg = PadQuotes(request.getParameter("msg"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				QueryString = request.getQueryString();

				if (!so_id.equals("0") && add.equals("yes")) {
					PopulateSODetails();
				}
				if (add.equals("yes")) {
					status = "Add";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						if (ReturnPerm(comp_id, "emp_sales_order_add", request).equals("1")) {
							track_entry_id = emp_id;
							track_entry_date = ToLongDate(kknow());
							GetValues(request, response);
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else if (!calculateB.equals("Calculate") && !financeAddB.equals("Add")) {
								response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Payment Track added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SQLException {
		so_finstatus_id = PadQuotes(request.getParameter("dr_so_finstatus_id"));
		finnancetrans_desc = PadQuotes(request.getParameter("txt_finnancetrans_desc"));
		if (finnancetrans_desc.length() > 255) {
			finnancetrans_desc = finnancetrans_desc.substring(0, 254);
		}
		so_date = PadQuotes(request.getParameter("txt_payment_from"));
		no_days = Integer.parseInt((request.getParameter("dr_days")));
		for (track_count = 1; track_count <= no_days; track_count++) {

			so_amt_date = PadQuotes(request.getParameter("txt_payment_track_from" + track_count));
			if (so_amt_date.equals("")) {
				payment_chk_msg = payment_chk_msg + "<br>Enter Date in " + track_count + " Row!";
			} else if (!so_amt_date.equals("") && !isValidDateFormatShort(so_amt_date)) {
				payment_chk_msg = payment_chk_msg + "<br>Enter valid Date in " + track_count + " Row!";
			} else {
				so_amt_date = ConvertShortDateToStr(so_amt_date);
			}

			so_track_receivedby_id = PadQuotes(request.getParameter("dr_receivedby" + track_count));

			if (so_track_receivedby_id.equals("0")) {
				payment_chk_msg = payment_chk_msg + "<br>Select Received By in " + track_count + " Row!";
			}

			if (!PadQuotes(request.getParameter("txt_payment_amt" + track_count)).equals("")) {
				so_amt_list = Integer.parseInt(request.getParameter("txt_payment_amt" + track_count));
				so_sum_amt = so_sum_amt + Integer.parseInt(request.getParameter("txt_payment_amt" + track_count + ""));
			} else {
				payment_chk_msg = payment_chk_msg + "<br>Enter Amount in " + track_count + " Row!";
			}

			if (!received_by_id.equals("") && !PadQuotes(request.getParameter("txt_payment_amt" + track_count)).equals("") && !calculateB.equals("Calculate") && !financeAddB.equals("Add")) {
				AddPayementTrack();
			}
		}

		if (!calculateB.equals("Calculate") && !financeAddB.equals("Add")) {
			if (so_sum_amt != 0 && so_sum_amt != (so_amt)) {
				payment_chk_msg = payment_chk_msg + "<br>Amount Mismatch!";
			}
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		track_entry_date = PadQuotes(request.getParameter("txt_track_entry_date"));
	}

	protected void CheckForm() {

		if (!payment_chk_msg.equals("")) {
			msg = msg + payment_chk_msg;
		}
	}

	protected void CheckFinanceForm() {
		if (finnancetrans_desc.equals("")) {
			finance_msg = "Error!<br>Enter Finance Description!";
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		if (!calculateB.equals("Calculate") && !financeAddB.equals("Add")) {
			CheckForm();
		}

		if (financeAddB.equals("Add")) {
			CheckFinanceForm();
			if (finance_msg.equals("")) {
				AddSOFinanceStatus();
				finance_msg = "Finance Status Added successfully!";
				finnancetrans_desc = "";
			}
		}

		if (msg.equals("") && !calculateB.equals("Calculate") && !financeAddB.equals("Add")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_so_payment_track"
						+ " WHERE track_so_id = " + so_id + "";
				stmttx.addBatch(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_payment_track"
						+ " (track_so_id,"
						+ " track_fincomp_id,"
						+ " track_date,"
						+ " track_amt,"
						+ " track_entry_id,"
						+ " track_entry_date)"
						+ " VALUES";
				StrSql = StrSql + StrSql1;

				stmttx.addBatch(StrSql);

				stmttx.executeBatch();
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					msg = "<br>Transaction Error!";
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					msg = "<br>Transaction Error!";
					SOPError("connection rollback...");
				}
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void AddSOFinanceStatus() {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_finance_trans"
				+ " (finnancetrans_so_id,"
				+ " finnancetrans_finstatus_id,"
				+ " finnancetrans_desc,"
				+ " finnancetrans_entry_id,"
				+ " finnancetrans_entry_date)"
				+ " VALUES"
				+ " (" + so_id + ","
				+ " " + so_finstatus_id + ","
				+ " '" + finnancetrans_desc + "',"
				+ " " + emp_id + ","
				+ " '" + ToLongDate(kknow()) + "')";
		updateQuery(StrSql);

		StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
				+ " SET"
				+ " so_finstatus_id = " + so_finstatus_id + ","
				+ " so_finstatus_date = " + ToLongDate(kknow()) + ","
				+ " so_finstatus_emp_id = " + emp_id + ""
				+ " WHERE so_id = " + so_id + "";
		updateQuery(StrSql);
	}

	public String PopulateFinanceTrans() {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COALESCE(finstatus_name, '') AS finstatus_name,"
					+ " finnancetrans_finstatus_id, finnancetrans_desc,"
					+ " finnancetrans_entry_id, finnancetrans_entry_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_trans"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_status ON finstatus_id = finnancetrans_finstatus_id"
					+ " WHERE finnancetrans_so_id = " + so_id + ""
					+ " GROUP BY finnancetrans_id"
					+ " ORDER BY finnancetrans_entry_date DESC";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
				Str.append("<tr>\n<th>#</th>\n");
				Str.append("<th>Status</th>\n");
				Str.append("<th>Description</th>\n");
				Str.append("<th>Entry By</th>\n");
				Str.append("<th>Entry Date</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td align=\"center\">").append(count);
					Str.append("</td>\n<td aign=\"left\">").append(crs.getString("finstatus_name"));
					Str.append("</td>\n<td aign=\"left\">").append(crs.getString("finnancetrans_desc"));
					Str.append("</td>\n<td aign=\"left\">").append(Exename(comp_id, crs.getInt("finnancetrans_entry_id")));
					Str.append("</td>\n<td aign=\"center\">").append(strToLongDate(crs.getString("finnancetrans_entry_date")));
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String GetPaymentTrackDetails(String so_id, String days, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COUNT(DISTINCT track_id) FROM " + compdb(comp_id) + "axela_sales_so_payment_track"
					+ " WHERE track_so_id =" + so_id + "";

			if (Integer.parseInt(days) == Integer.parseInt(ExecuteQuery(StrSql))) {
				if (calculateB.equals("Calculate") || days.equals("0")) {
					days = "1";
					calculateB = "Calculate";
				} else if (financeAddB.equals("Add")) {
					financeAddB = "Add";
				} else {
					track_prev_count = 1;
					StrSql = "SELECT track_date, track_amt, track_fincomp_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so_payment_track"
							+ " WHERE track_so_id = " + so_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);

					while (crs.next()) {
						track_prev_date.add(strToShortDate(crs.getString("track_date")));
						track_prev_amt.add(crs.getString("track_amt"));
						track_prev_fincomp_id.add(crs.getString("track_fincomp_id"));
					}
					crs.close();
				}
			}

			if (!days.equals("0")) {
				so_date = ConvertShortDateToStr(so_date);
				Str.append("<div class=\"table-bordered table-hover\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Payment Date</th>\n");
				Str.append("<th>Received By</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				for (int i = 1; i <= Integer.parseInt(days); i++) {
					if (track_prev_count != 1 || so_sum_amt != 0) {
						payment_track_amt = PadQuotes(request.getParameter("txt_payment_amt" + i + ""));
					} else if (track_prev_count == 1) {
						payment_track_amt = track_prev_amt.get(i - 1);
					}
					payment_track_total = payment_track_total + Integer.parseInt(CNumeric(payment_track_amt));
					Str.append("<td valign=top align=center>").append(i).append("</td>\n");
					Str.append("<LINK REL='STYLESHEET' TYPE='text/css' HREF='../Library/theme1/jquery-ui.css'>");
					Str.append("<script type='text/javascript' src='../Library/jquery.js'></script>");
					Str.append("<script type='text/javascript' src='../Library/jquery-ui.js'></script>");
					Str.append("<script>");
					Str.append("$(function() {");
					Str.append("$('#txt_payment_track_from").append(i).append("' ).datepicker({");
					Str.append("showButtonPanel: true,");
					Str.append("dateFormat: 'dd/mm/yy',");
					Str.append("showOn : 'focus'});");
					Str.append("});");
					Str.append("</script>");
					Str.append("<td><input type=\"text\" size='11' maxlength='10' class=\"form-control\" name=\"txt_payment_track_from").append(i);
					Str.append("\" id=\"txt_payment_track_from").append(i).append("\" value=\"");
					if (!PadQuotes(request.getParameter("txt_payment_track_from" + i + "")).equals("")) {
						Str.append(PadQuotes(request.getParameter("txt_payment_track_from" + i + "")));
					} else if (track_prev_count == 1) {
						Str.append(track_prev_date.get(i - 1));
					} else {
						Str.append(AddDayMonthYear(strToShortDate(so_date), i, 0, 0, 0));
					}
					if (track_prev_count != 1 || so_sum_amt != 0) {
						received_by_id = PadQuotes(request.getParameter("dr_receivedby" + i + ""));
					} else if (track_prev_count == 1) {
						received_by_id = track_prev_fincomp_id.get(i - 1);
					}
					Str.append("").append("\">");
					Str.append("</td >\n").append("<td>").append("<select name='dr_receivedby").append(i);
					Str.append("' id='dr_receivedby").append(i).append("' class='form-control'>");
					Str.append(PopulateRecievedBy()).append("</select>");
					Str.append("</td>\n<td><input type=\"text\"  class=\"form-control\" onKeyUp=\"toInteger('txt_payment_amt").append(i);
					Str.append("');track_results();\" name=\"txt_payment_amt").append(i).append("\" id=\"txt_payment_amt").append(i);
					Str.append("\" value=\"").append(payment_track_amt).append("\">");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("<tr>");
				Str.append("<td colspan='3' align='right'><b>Total:</b></td>\n");
				Str.append("<td><b><span id='track_total'>").append(IndFormat(Integer.toString(payment_track_total))).append("</span></b></td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>");
				Str.append("<td colspan='3' align='right'><b><font color=\"red\">Balance:</font></b></td>\n");
				Str.append("<td><b><font color=\"red\"><span id='track_balance'>").append(IndFormat(Integer.toString(so_amt - payment_track_total))).append("</span></font></b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListReceiptData() {
		StringBuilder Str = new StringBuilder();
		double receipt_total = 0.00;
		int count = 0;

		StrSql = "SELECT receipt_id, receipt_branch_id, CONCAT('RCT', branch_code, receipt_no) AS receipt_no,"
				+ " receipt_date, customer_id, customer_name, receipt_amount, emp_id, branch_name,"
				+ " COALESCE(invoice_id, 0) AS invoice_id, receipt_active, receipt_entry_id,"
				+ " receipt_entry_date, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
				+ " FROM " + compdb(comp_id) + "axela_invoice_receipt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_invoice ON invoice_id = receipt_invoice_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = invoice_so_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = receipt_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = receipt_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = receipt_emp_id"
				+ " WHERE so_id = " + so_id + ""
				+ " GROUP BY receipt_id"
				+ " ORDER BY receipt_id DESC";
		// SOP("StrSql=="+StrSqlBreaker(StrSql));

		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Invoice</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Sales Consultant</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					receipt_total = receipt_total + crs.getDouble("receipt_amount");
					Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center><a href=../invoice/receipt-list.jsp?receipt_id=");
					Str.append(crs.getInt("receipt_id")).append(">").append(crs.getString("receipt_id")).append("</a></td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("receipt_no"));
					if (crs.getString("receipt_active").equals("0")) {
						Str.append("<br><font color=red><b>Inactive</b></font>");
					}
					Str.append("</td>\n<td valign=top align=left>");
					if (!crs.getString("invoice_id").equals("0")) {
						Str.append("<a href=../invoice/invoice-list.jsp?invoice_id=").append(crs.getInt("invoice_id")).append(">Invoice ID: ").append(crs.getString("invoice_id")).append("</a>");
					} else {
						Str.append("");
					}
					Str.append("</td>\n<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("</td>\n<td valign=top align=center>").append(strToShortDate(crs.getString("receipt_date")));
					Str.append("</td>\n<td valign=top align=right>").append(IndFormat(crs.getString("receipt_amount")));
					Str.append("</td>\n<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td valign=top align=left nowrap><a href=\"../invoice/receipt-update.jsp?update=yes&receipt_id=");
					Str.append(crs.getString("receipt_id")).append(" \" target=_parent>Update Receipt</a>");
					Str.append("<br><a href=\"../invoice/receipt-print.jsp?receipt_id=").append(crs.getString("receipt_id"));
					Str.append("\" target=_blank>Print Receipt</a></td>\n</tr>\n");
				}
				Str.append("<tr>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n");
				Str.append("<td align=\"right\"><b>Total: </b></td>\n<td align=\"right\"><b>").append(IndFormat(Double.toString(receipt_total)))
						.append("</b></td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n</tr>\n");
				Str.append("<tr>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n");
				Str.append("<td align=\"right\"><b><font color=\"red\">Balance: </font></b></td>\n<td align=\"right\"><b><font color=\"red\">")
						.append(IndFormat(Double.toString(so_amt - receipt_total))).append("</font></b></td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");;
			} else {
				Str.append("<br><br><font color=red><b>No Receipt(s) found!</b></font><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateDays() {
		StringBuilder Str = new StringBuilder();
		for (int i = 1; i < 31; i++) {
			Str.append("<option value=").append(i);
			Str.append(StrSelectdrop(Integer.toString(i), Integer.toString(no_days))).append(">");
			Str.append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateFinanceStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT finstatus_id, finstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_status"
					+ " WHERE finstatus_active = '1'"
					+ " GROUP BY finstatus_id"
					+ " ORDER BY finstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("finstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("finstatus_id"), so_finstatus_id));
				Str.append(">").append(crs.getString("finstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateRecievedBy() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fincomp_id, fincomp_name, fincomp_active"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = '1'"
					+ " GROUP BY fincomp_id"
					+ " ORDER BY fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), received_by_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateSODetails() {
		try {
			StrSql = "SELECT COUNT(DISTINCT track_id) AS dayscount, so_contact_id, so_customer_id,"
					+ " customer_name, so_date, so_grandtotal, so_grandtotal, so_fincomp_id, fincomp_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " so_finstatus_id, so_finstatus_date, so_finstatus_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_payment_track ON track_so_id = so_id"
					+ " WHERE so_id = " + so_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				so_finstatus_date = strToShortDate(crs.getString("so_finstatus_date"));
				so_finstatus_emp_id = crs.getString("so_finstatus_emp_id");
				contact_id = crs.getString("so_contact_id");
				customer_id = crs.getString("so_customer_id");
				contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("so_contact_id") + "\">" + crs.getString("contact_name") + "</a>";
				customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("so_customer_id") + "\">" + crs.getString("customer_name") + "" + "</a>";
				sodate = strToShortDate(crs.getString("so_date"));
				so_date = sodate;
				so_amt = (int) crs.getDouble("so_grandtotal");
				received_by_id = crs.getString("so_fincomp_id");
				received_by_name = crs.getString("fincomp_name");
				no_days = crs.getInt("dayscount");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddPayementTrack() throws SQLException {

		StrSql1 += "(" + so_id + ", "
				+ " " + so_track_receivedby_id + ","
				+ " '" + so_amt_date + "',"
				+ " " + so_amt_list + ","
				+ " " + track_entry_id + ","
				+ " '" + track_entry_date + "')";
		if (track_count < no_days) {
			StrSql1 = StrSql1 + ",";
		}
	}
}
