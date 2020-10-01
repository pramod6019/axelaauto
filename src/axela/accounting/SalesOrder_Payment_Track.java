package axela.accounting;

//aJIt 07th June, 2013

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Payment_Track extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String add = "";

	public String addB = "";
	public String msg = "";
	public String voucher_id = "0", vouchertype_id = "0";
	public String status = "";
	public String StrSql = "";
	public String StrValue = "";
	public String track_financecomp_id = "0";
	public String track_entry_id = "0", track_entry_date = "";
	public String entry_by = "";
	public String contact_id = "0", contact_name = "";
	public String customer_id = "0", customer_name = "";
	public String voucher_date = "", voucherdate = "";
	public String QueryString = "";
	public String payment_from = "";
	public String days = "0";
	public String received_by_id = "0";
	public String received_by_name = "";
	public String voucher_amt_date = "";
	public String voucher_track_receivedby_id = "0";
	public String payment_chk_msg = "";
	public String calculateB = "";
	public int no_days = 0;
	public int track_count = 0;
	public int voucher_amt_list = 0;
	public String voucher_remarks_list = "";
	public int voucher_sum_amt = 0;
	public int track_prev_count = 0;
	public double voucher_amt = 0;
	public int payment_track_total = 0;
	public Connection conntx = null;
	public Statement stmttx = null;
	public String payment_track_amt = "0";
	public String payment_track_remarks = "";
	ArrayList track_prev_date = new ArrayList();
	ArrayList<String> track_prev_amt = new ArrayList<String>();
	ArrayList<String> track_prev_remarks = new ArrayList<String>();

	// ArrayList<String> track_prev_financecomp_id = new ArrayList<String>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			comp_id = CNumeric(GetSession("emp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				calculateB = PadQuotes(request.getParameter("txt_calculate"));
				msg = PadQuotes(request.getParameter("msg"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				QueryString = request.getQueryString();

				if (!voucher_id.equals("0") && add.equals("yes")) {
					PopulateSalesOrderDetails();
				}

				if (add.equals("yes")) {
					status = "Add";
					if (addB.equals("yes")) {
						if (ReturnPerm(comp_id, "emp_voucher_add", request).equals(
								"1")) {
							GetValues(request, response);
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else if (!calculateB.equals("Calculate")) {
								response.sendRedirect(response
										.encodeRedirectURL("voucher-list.jsp?voucher_id="
												+ voucher_id
												+ "&msg=Payment Track added successfully!"
												+ msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		voucherdate = PadQuotes(request.getParameter("txt_payment_from"));
		no_days = Integer.parseInt((request.getParameter("dr_days")));
		for (track_count = 1; track_count <= no_days; track_count++) {
			voucher_amt_date = PadQuotes(request
					.getParameter("txt_payment_track_from" + track_count));
			if (voucher_amt_date.equals("")) {
				payment_chk_msg += "<br>Enter Date in " + track_count + " Row!";
			} else if (!voucher_amt_date.equals("")
					&& !isValidDateFormatShort(voucher_amt_date)) {
				payment_chk_msg += "<br>Enter valid Date in " + track_count
						+ " Row!";
			} else {
				voucher_amt_date = ConvertShortDateToStr(voucher_amt_date);
			}

			voucher_track_receivedby_id = PadQuotes(request
					.getParameter("dr_receivedby" + track_count));

			if (voucher_track_receivedby_id.equals("0")) {
				payment_chk_msg += "<br>Select Received By in " + track_count
						+ " Row!";
			}

			if (!PadQuotes(
					request.getParameter("txt_payment_amt" + track_count))
					.equals("")) {
				voucher_amt_list = Integer.parseInt(PadQuotes(request
						.getParameter("txt_payment_amt" + track_count)));
				voucher_remarks_list = PadQuotes(request
						.getParameter("txt_payment_remarks" + track_count));
				if (voucher_remarks_list.length() > 255) {
					voucher_remarks_list = voucher_remarks_list.substring(0,
							254);
				}
				voucher_sum_amt += Integer.parseInt(request
						.getParameter("txt_payment_amt" + track_count + ""));
			} else {
				payment_chk_msg += "<br>Enter Amount in " + track_count
						+ " Row!";
			}

			if (!PadQuotes(
					request.getParameter("txt_payment_amt" + track_count))
					.equals("") && !calculateB.equals("Calculate")) {
				AddPayementTrack();
			}
		}

		if (!calculateB.equals("Calculate")) {
			if (voucher_sum_amt != 0 && voucher_sum_amt != (voucher_amt)) {
				payment_chk_msg += "<br>Amount Mismatch!";
			}
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		track_entry_date = PadQuotes(request
				.getParameter("txt_track_entry_date"));
	}

	protected void CheckForm() {
		if (!payment_chk_msg.equals("")) {
			msg += payment_chk_msg;
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		if (!calculateB.equals("Calculate")) {
			CheckForm();
		}

		if (msg.equals("") && !calculateB.equals("Calculate")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "DELETE FROM  " + compdb(comp_id) + "axela_acc_payment_track"
						+ " WHERE track_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_payment_track"
						+ " (track_voucher_id,"
						+ " track_date," + " track_amt," + " track_remarks,"
						+ " track_entry_id," + " track_entry_date)" + " VALUES";
				StrSql += StrValue;
				// SOP("StrSql==insert INTO " + compdb(comp_id) + "payment track ==" + StrSqlBreaker(StrSql));
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
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
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

	public String GetPaymentTrackDetails(String voucher_id, String days,
			HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT COUNT(DISTINCT track_id)"
					+ " FROM  " + compdb(comp_id) + "axela_acc_payment_track"
					+ " WHERE track_voucher_id = " + voucher_id + "";
			// SOP("StrSql==track="+StrSqlBreaker(StrSql));

			if (Integer.parseInt(days) == Integer
					.parseInt(ExecuteQuery(StrSql))) {
				if (calculateB.equals("Calculate") || days.equals("0")) {
					days = "1";
					calculateB = "Calculate";
				} else {
					track_prev_count = 1;
					StrSql = "SELECT track_date, track_amt, track_remarks"
							+ " FROM  " + compdb(comp_id) + "axela_acc_payment_track"
							+ " WHERE track_voucher_id = " + voucher_id + "";
					SOP("StrSql==prev=" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);

					while (crs.next()) {
						track_prev_date.add(strToShortDate(crs
								.getString("track_date")));
						track_prev_amt.add(crs.getString("track_amt"));
						track_prev_remarks.add(crs.getString("track_remarks"));
						// track_prev_financecomp_id.add(crs.getString("track_financecomp_id"));
					}
					crs.close();
				}
			}
			// SOP("days==="+days);
			if (!days.equals("0")) {
				voucher_date = ConvertShortDateToStr(voucherdate);
				Str.append("\n<table width=\"100%\" border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Payment Date</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Remarks</th>\n");
				Str.append("</tr>\n");
				SOP(days);
				for (int i = 1; i <= Integer.parseInt(days); i++) {
					if (track_prev_count != 1 || voucher_sum_amt != 0) {
						payment_track_amt = PadQuotes(request
								.getParameter("txt_payment_amt" + i + ""));
					} else if (track_prev_count == 1) {
						payment_track_amt = track_prev_amt.get(i - 1);
					}

					if (track_prev_count != 1 || voucher_sum_amt != 0) {
						payment_track_remarks = PadQuotes(request
								.getParameter("txt_payment_remarks" + i + ""));
					} else if (track_prev_count == 1) {
						payment_track_remarks = track_prev_remarks.get(i - 1);
					}
					payment_track_total += Integer
							.parseInt(CNumeric(payment_track_amt));
					Str.append("<tr>\n<td valign=\"top\" align=\"center\">")
							.append(i);
					// Str.append("<LINK REL='STYLESHEET' TYPE='text/css' HREF='../Library/theme1/jquery-ui.css'>");
					// Str.append("<script type='text/javascript' src='../Library/jquery.js'></script>");
					// Str.append("<script type='text/javascript' src='../Library/jquery-ui.js'></script>");
					Str.append("<script>");
					Str.append("$(function() {");
					Str.append("$('#txt_payment_track_from").append(i)
							.append("' ).datepicker({");
					Str.append("showButtonPanel: true,");
					Str.append("dateFormat: ' dd/mm/yy ',");
					Str.append("showOn : 'focus'});");
					Str.append("});");
					Str.append("</script>");
					Str.append(
							"</td>\n<td valign=\"top\"><input type=\"text\" size='11' maxlength='10' class=textbox name=\"txt_payment_track_from")
							.append(i);
					Str.append("\" id=\"txt_payment_track_from").append(i)
							.append("\" value=\"");
					if (!PadQuotes(
							request.getParameter("txt_payment_track_from" + i))
							.equals("")) {
						Str.append(PadQuotes(request
								.getParameter("txt_payment_track_from" + i)));
					} else if (track_prev_count == 1) {
						Str.append(track_prev_date.get(i - 1));
					} else {
						Str.append(AddDayMonthYear(
								strToShortDate(voucher_date), i - 1, 0, 0, 0));
					}

					if (track_prev_count != 1 || voucher_sum_amt != 0) {
						// received_by_id =
						// PadQuotes(request.getParameter("dr_receivedby" + i));
					} else if (track_prev_count == 1) {
						// received_by_id = track_prev_financecomp_id.get(i -
						// 1);
					}
					// Str.append("\"></td>\n<td valign=\"top\"><select name='dr_receivedby").append(i);
					// Str.append("' id='dr_receivedby").append(i).append("' class=\"selectbox\">");
					// Str.append(PopulateRecievedBy()).append("</select>\n");
					Str.append(
							"\"></td>\n<td valign=\"top\"><input type=\"text\"  class=\"textbox\" onKeyUp=\"toInteger('txt_payment_amt")
							.append(i);
					Str.append("');track_results();\" name=\"txt_payment_amt")
							.append(i).append("\" id=\"txt_payment_amt")
							.append(i);
					Str.append("\" value=\"").append(payment_track_amt)
							.append("\">\n");
					Str.append(
							"</td>\n<td valign=\"top\"><textarea name=\"txt_payment_remarks")
							.append(i).append("\" id=\"txt_payment_remarks")
							.append(i);
					Str.append("\" cols=\"30\" rows=\"3\" class=\"textbox\">")
							.append(payment_track_remarks)
							.append("</textarea>\n");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td colspan=\"2\" align=\"right\"><b>Total:</b></td>\n");
				Str.append("<td><b><span id=\"track_total\">")
						.append(IndFormat(Integer.toString(payment_track_total)))
						.append("</span></b></td>\n");
				Str.append("<td>&nbsp;</td>\n</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td colspan=\"2\" align=\"right\"><b><font color=\"red\">Balance:</font></b></td>\n");
				Str.append(
						"<td><b><font color=\"red\"><span id=\"track_balance\">")
						.append(IndFormat(Double.toString((voucher_amt)
								- payment_track_total)))
						.append("</span></font></b></td>\n");
				Str.append("<td>&nbsp;</td>\n</tr>\n</table>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String ListReceiptData() throws PropertyVetoException, SQLException,
			IOException {
		StringBuilder Str = new StringBuilder();
		double receipt_total = 0.00;
		int count = 0;
		StrSql = "SELECT receipt_id, receipt_branch_id, CONCAT('RCT', branch_code, receipt_no) AS receipt_no,"
				+ " receipt_date, customer_id, customer_name, receipt_amount, emp_id, branch_name,"
				+ " COALESCE(invoice_id, 0) AS invoice_id, receipt_active, receipt_entry_id,"
				+ " receipt_entry_date, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
				+ " FROM  " + compdb(comp_id) + "axela_invoice_receipt"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = receipt_voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = receipt_customer_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = receipt_branch_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = receipt_emp_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_invoice ON invoice_id = receipt_invoice_id"
				+ " WHERE voucher_id = "
				+ voucher_id
				+ ""
				+ " GROUP BY receipt_id" + " ORDER BY receipt_id DESC";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=\"center\">\n<th colspan=\"10\">Receipts</th>\n</tr>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Invoice</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					receipt_total += crs.getDouble("receipt_amount");

					Str.append("<tr>\n<td valign=\"top\" align=\"center\">")
							.append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\"><a href=../invoice/receipt-list.jsp?receipt_id=");
					Str.append(crs.getInt("receipt_id")).append(">")
							.append(crs.getString("receipt_id"))
							.append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(
							crs.getString("receipt_no"));
					if (crs.getString("receipt_active").equals("0")) {
						Str.append("<br><font color=\"red\"><b>Inactive</b></font>");
					}
					Str.append("</td>\n<td valign=\"top\" align=\"left\">");
					if (!crs.getString("invoice_id").equals("0")) {
						Str.append("<a href=invoice-list.jsp?invoice_id=")
								.append(crs.getInt("invoice_id"));
						Str.append(">Invoice ID: ")
								.append(crs.getString("invoice_id"))
								.append("</a>");
					}
					Str.append("&nbsp;</td>\n<td valign=\"top\" align=\"left\"><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">")
							.append(crs.getString("customer_name"))
							.append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(
							strToShortDate(crs.getString("receipt_date")));
					Str.append("</td>\n<td valign=\"top\" align=\"right\">")
							.append(IndFormat(crs.getString("receipt_amount")));
					Str.append("</td>\n<td valign=\"top\" align=\"center\">");
					Str.append(ExePhotoOval(crs.getString("emp_photo"),
							crs.getString("emp_gender"), "50"));
					Str.append("<a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">")
							.append(crs.getString("emp_name"))
							.append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap><a href=\"../invoice/receipt-update.jsp?update=yes&receipt_id=");
					Str.append(crs.getString("receipt_id")).append(
							" \" target=_parent>UPDATE  " + compdb(comp_id) + "Receipt</a>");
					Str.append(
							"<br><a href=\"../invoice/receipt-print.jsp?receipt_id=")
							.append(crs.getString("receipt_id"));
					Str.append("\" target=\"_blank\">Print Receipt</a></td>\n</tr>\n");
				}
				Str.append("<tr>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n");
				Str.append(
						"<td align=\"right\"><b>Total: </b></td>\n<td align=\"right\"><b>")
						.append(IndFormat(Double.toString(receipt_total)));
				Str.append("</b></td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n</tr>\n");
				Str.append("<tr>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n");
				Str.append("<td align=\"right\"><b><font color=\"red\">Balance: </font></b></td>\n<td align=\"right\"><b><font color=\"red\">");
				Str.append(IndFormat(Double.toString(voucher_amt
						- receipt_total)));
				Str.append("</font></b></td>\n<td>&nbsp;</td>\n<td>&nbsp;</td>\n</tr>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=\"red\"><b>No Receipt(s) found!</b></font><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateDays() {
		StringBuilder Str = new StringBuilder();
		for (int i = 1; i < 31; i++) {
			Str.append("<option value=").append(i);
			Str.append(
					StrSelectdrop(Integer.toString(i),
							Integer.toString(no_days))).append(">");
			Str.append(i).append("</option>\n");
		}
		return Str.toString();
	}

	// public String PopulateRecievedBy() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT fin fincomp_name, fincomp_active"
	// + " FROM  "+compdb(comp_id)+"axela_acc_voucher_finance_comp"
	// + " WHERE fincomp_active = '1'"
	// + " GROUP BY fincomp_id"
	// + " ORDER BY fincomp_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// Str.append("<option value=\"0\">Select</option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("fincomp_id"));
	// Str.append(StrSelectdrop(crs.getString("fincomp_id"), received_by_id));
	// Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }

	public void PopulateSalesOrderDetails() {
		try {
			StrSql = "SELECT COUNT(DISTINCT track_id) AS dayscount, voucher_contact_id, voucher_customer_id, voucher_date,"
					+ " customer_name, voucher_amount, "
					// + " voucher_finance fincomp_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					// +
					// " LEFT JOIN  "+compdb(comp_id)+"axela_acc_voucher_finance_comp ON fincomp_id = voucher_financecomp_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_acc_payment_track ON track_voucher_id = voucher_id"
					+ " WHERE voucher_id = " + voucher_id + "";
			// SOP("StrSql===so==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_id = crs.getString("voucher_contact_id");
				customer_id = crs.getString("voucher_customer_id");
				contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
						+ crs.getString("voucher_contact_id")
						+ "\">"
						+ crs.getString("contact_name") + "</a>";
				customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
						+ crs.getString("voucher_customer_id")
						+ "\">"
						+ crs.getString("customer_name") + "" + "</a>";
				voucher_date = crs.getString("voucher_date");
				voucherdate = strToShortDate(voucher_date);
				voucher_amt = crs.getDouble("voucher_amount");
				// received_by_id = crs.getString("voucher_financecomp_id");
				// received_by_name = crs.getString("fincomp_name");
				no_days = crs.getInt("dayscount");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void AddPayementTrack() throws SQLException {
		StrValue += "(" + voucher_id + ","
				+ "'" + voucher_amt_date + "',"
				+ "" + voucher_amt_list + ","
				+ "'" + voucher_remarks_list + "',"
				+ " " + emp_id + ","
				+ " " + ToLongDate(kknow()) + ")";
		if (track_count < no_days) {
			StrValue += ",";
		}
	}
}
