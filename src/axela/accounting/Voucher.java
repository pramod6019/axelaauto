package axela.accounting;

// vidyanandan
// Shivaprasad 13 Jan 2015

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Voucher extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String comp_id = "0";
	public String accessories = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String EnableSearch = "1";
	public String vouchertypename = "";
	public String vouchertype_name = "";
	public String param1 = "";
	public String param2 = "";
	public String param3 = "";
	public String start_time = "";
	public String end_time = "";
	public String starttime = "";
	public String endtime = "";
	public String ExportPerm = "";
	public String smart = "", access = "";
	public String go = "";
	public String ListLink = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				accessories = PadQuotes(request.getParameter("accessories"));
				// param1 = PadQuotes(request.getParameter("income"));
				// param2 = PadQuotes(request.getParameter("expense"));
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				start_time = PadQuotes(request.getParameter("txt_starttime"));
				end_time = PadQuotes(request.getParameter("txt_endtime"));
				if (start_time.equals("")) {
					start_time = DateToShortDate(kknow());
				}
				if (end_time.equals("")) {
					end_time = DateToShortDate(kknow());
				}

				param1 = CNumeric(PadQuotes(request.getParameter("param1")));
				param2 = CNumeric(PadQuotes(request.getParameter("param2")));
				param3 = CNumeric(PadQuotes(request.getParameter("param3")));
				SOP("coming");
				if (!(ReturnPerm(comp_id, "emp_acc_voucher_access", request).equals("1")))
				{
					if (param3.equals("20")) {
						access = "emp_acc_grn_access";
					} else if (param3.equals("24")) {
						access = "emp_acc_purchase_return_access";
					} else if (param3.equals("23")) {
						access = "emp_acc_sales_return_access";
					} else if (param3.equals("5")) {
						access = "emp_acc_quote_access";
					} else if (param3.equals("6")) {
						access = "emp_acc_invoice_access";
					} else if (param3.equals("7")) {
						access = "emp_acc_bill_access";
					} else if (param3.equals("9")) {
						access = "emp_acc_receipt_access";
					} else if (param3.equals("10")) {
						access = "emp_acc_credit_note_access";
					} else if (param3.equals("11")) {
						access = "emp_acc_debit_note_access";
					} else if (param3.equals("12")) {
						access = "emp_acc_purchase_order_access";
					} else if (param3.equals("15")) {
						access = "emp_acc_payment_access";
					} else if (param3.equals("16")) {
						access = "emp_acc_expense_access";
					} else if (param3.equals("18")) {
						access = "emp_acc_journal_access";
					} else if (param3.equals("19")) {
						access = "emp_acc_contra_access";
					} else if (param3.equals("21")) {
						access = "emp_acc_purchase_invoice_access";
					} else if (param3.equals("27")) {
						access = "emp_preorder_access,emp_acc_preorder_access,emp_preorder_access,emp_sales_order_add,emp_stock_add";
					}
				}
				else {
					access = "emp_acc_voucher_access";
				}

				SOP("access===" + access);

				CheckPerm(comp_id, access, request, response);

				if (!param3.equals("0")) {
					CachedRowSet crs = processQuery("SELECT vouchertype_name "
							+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_type"
							+ " WHERE vouchertype_id = " + param3, 0);
					while (crs.next()) {
						vouchertype_name = crs.getString("vouchertype_name");
					}
					crs.close();
					StrSearch += " AND vouchertype_id = " + param3;
				}
				// else if (param1.equals("yes")) {
				// vouchertype_name = "Income";
				// StrSearch += " AND (vouchertype_voucherclass_id = 102 OR vouchertype_voucherclass_id = 103)";
				// } else if (param2.equals("yes")) {
				// vouchertype_name = "Expense";
				// StrSearch += " AND (vouchertype_voucherclass_id = 109 OR vouchertype_voucherclass_id = 110 )";
				// }

				ListLink = "<a href=../accounting/voucher-list.jsp?smart=yes>Click here to List "
						+ vouchertype_name + "(s)</a>";
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch += " AND SUBSTR(voucher_date,1,8) >= SUBSTR('"
								+ starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(voucher_date,1,8) <= SUBSTR('"
								+ endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND voucher_branch_id=" + dr_branch_id;
					}
					StrSearch += " AND branch_active = '1'"
							+ " AND voucher_active = 1";
					if (!msg.equals("")) {
						msg = "<br>Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("voucherstrsql", StrSearch, request);
						StrHTML = VoucherSummary();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
		}
	}
	protected void CheckForm() {
		msg = "";
		if (start_time.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!start_time.equals("")) {
			if (isValidDateFormatShort(start_time)) {
				starttime = ConvertShortDateToStr(start_time);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
			}
		}
		if (end_time.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!end_time.equals("")) {
			if (isValidDateFormatShort(end_time)) {
				endtime = ConvertShortDateToStr(end_time);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}
		if (!starttime.equals("") && !endtime.equals("")
				&& isValidDateFormatShort(start_time)
				&& isValidDateFormatShort(start_time)) {
			if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		start_time = PadQuotes(request.getParameter("txt_starttime"));
		end_time = PadQuotes(request.getParameter("txt_endtime"));
		param1 = PadQuotes(request.getParameter("param1"));
		param2 = PadQuotes(request.getParameter("param2"));
		param3 = CNumeric(PadQuotes(request.getParameter("param3")));
		String date = "";
		if (start_time.equals("") || end_time.equals("")) {
			date = ToShortDate(kknow());
		}
		if (start_time.equals("")) {
			start_time = ReportStartdate();
		}
		if (end_time.equals("")) {
			end_time = ReportStartdate();
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request
					.getParameter("dr_branch_id")));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
	}

	public String VoucherSummary() {
		StringBuilder Str = new StringBuilder();
		DecimalFormat deci = new DecimalFormat("#.##");
		int voucher_count = 0;
		double nettotal = 0;
		try {
			StrSql = " SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " COUNT(voucher_id) AS vouchercount, COALESCE(SUM(voucher_amount), 0) AS total"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " WHERE 1 = 1"
					+ StrSearch
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			// SOP("StrSql-==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<b>" + vouchertype_name + " Summary</b><br>");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Branch</th>\n");
				Str.append("<th width=20%>" + vouchertype_name
						+ " Count</th>\n");
				Str.append("<th width=20%>Amount</th>\n");
				Str.append("</tr><thead>\n");
				while (crs.next()) {
					voucher_count += crs.getInt("vouchercount");
					nettotal += crs.getDouble("total");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append(
							"<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=")
							.append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(
							crs.getString("vouchercount")).append("</td>\n");
					Str.append("<td valign=top align=right> ").append(
							IndFormat(crs.getString("total"))).append("</td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(voucher_count).append(
						"</b></td>\n");
				Str.append("<td align=right><b>").append(
						IndFormat(deci.format(nettotal))).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
