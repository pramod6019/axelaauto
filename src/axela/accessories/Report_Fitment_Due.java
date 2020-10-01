package axela.accessories;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Fitment_Due extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, exe_ids;
	public String brand_id = "", region_id = "", exe_id = "";
	public String order_by = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch = "0";
	public String go = "";
	public String ExeAccess = "";
	public String branch_name = "";
	public String StrSearch = "";
	// public String SearchURL = "report-accessories-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.accessories.MIS_Check mischeck = new axela.accessories.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				if (!header.equals("no")) {
					emp_id = CNumeric(GetSession("emp_id", request));
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					go = PadQuotes(request.getParameter("submit_button"));
					if (start_time.equals("")) {
						start_time = strToShortDate(ToShortDate(kknow()));
					}
					if (end_time.equals("")) {
						end_time = strToShortDate(ToShortDate(kknow()));
					}
				}
				if (!header.equals("no")) {
					if (go.equals("Go")) {
						GetValues(request, response);
						CheckForm();
						BranchAccess = BranchAccess.replace("branch_id", "voucher_branch_id");
						ExeAccess += ExeAccess;
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
						}
						if (!exe_id.equals("")) {
							StrSearch += " AND voucher_emp_id IN (" + exe_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListFitmentDueDetails();
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		order_by = request.getParameter("dr_order_by");
		if (order_by.equals("0")) {
			order_by = " emp_name ";
		}

	}

	protected void CheckForm() {
		msg = "";
		if (brand_id.equals("")) {
			msg += "<br>Select Brand!";
		}

		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);

			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListFitmentDueDetails() {
		try {
			StringBuilder Str = new StringBuilder();

			StrSql = " SELECT"
					+ " COALESCE(preorder.voucher_id,'0') AS preorder_id,"
					+ " COALESCE(preorder.voucher_date,'') AS preorder_date,"
					+ " COALESCE(so_id,'0') AS so_id, so_promise_date,"
					+ " customer_id, customer_name, contact_id, contact_mobile1, contact_phone1, contact_phone2, contact_mobile2, contact_email2,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, contact_email1,"
					+ " COALESCE(vehstock_id, '0') AS vehstock_id,"
					+ " IF(voucher_fitted = 0 , '0', '1') AS fitmentstatus,"
					+ " so_branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " COALESCE(preorder.voucher_amount,'0') AS preorder_amount,"
					+ " COALESCE(invoiceamount, 0.00) AS invoiceamount, COALESCE(receiptamount, 0.00) AS receiptamount,"
					+ " receiptamountauthorize,"
					+ " REPLACE(COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
					+ " WHERE tagtrans_customer_id = customer_id ), '' ),',','') AS tag"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher preorder"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = preorder.voucher_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN ("
					+ " SELECT"
					+ " invoice.voucher_preorder_id,"
					+ " SUM(invoice.voucher_amount) AS invoiceamount,"
					+ " SUM(receipt.voucher_amount) AS receiptamount,"
					+ " SUM(IF (receipt.voucher_authorize = 1, receipt.voucher_amount,	0)) AS receiptamountauthorize"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher invoice"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher receipt ON receipt.voucher_invoice_id = invoice.voucher_id"
					+ " AND receipt.voucher_vouchertype_id = 9"
					+ " WHERE invoice.voucher_vouchertype_id = 6"
					+ " AND invoice.voucher_active = 1"
					+ " GROUP BY invoice.voucher_preorder_id"
					+ " ) AS tblvoucherreceipt ON tblvoucherreceipt.voucher_preorder_id = preorder.voucher_id"
					+ " WHERE 1 = 1"
					+ " AND preorder.voucher_vouchertype_id = 27"
					+ " AND preorder.voucher_so_id != 0"
					+ " AND so_delivered_date = ''"
					+ " AND SUBSTR(preorder.voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(preorder.voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)  ";
			StrSql += BranchAccess
					+ ExeAccess
					+ StrSearch
					+ " GROUP BY preorder.voucher_id "
					+ " ORDER BY " + order_by;

			SOP("ListFitmentDueDetails-------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 1;

				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><b>Pre-Order ID</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Pre-Order Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Fitment Status</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>SO ID</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Tentative Date</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Customer</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Stock ID</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Branch</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Pre-Order Amount</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Invoice Amount</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Receipt Amount</b></th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("preorder_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("preorder_id") + ");' style='height:200px'>");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=center><a href=../accounting/voucher-list.jsp?vouchertype_id=27&voucherclass_id=27&voucher_so_id=")
							.append(crs.getString("so_id")).append(">").append(crs.getString("preorder_id"))
							.append("</a></td>");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("preorder_date"))).append("</td>");
					if (crs.getString("fitmentstatus").equals("0")) {
						Str.append("<td valign=top align=center>").append("NO").append("</td>");
					} else {
						Str.append("<td valign=top align=center>").append("YES").append("</td>");
					}
					Str.append("<td valign=top align=center>").append("<a href=../sales/veh-salesorder-list.jsp?so_id=")
							.append(crs.getString("so_id")).append(" target=_blank>")
							.append(crs.getString("so_id")).append("</a><br/>").append("</td>\n");
					if (!crs.getString("so_promise_date").equals("")) {
						Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("so_promise_date"))).append("</td>");
					}
					Str.append("<td>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contactname")).append("</a>");
					if (!crs.getString("contact_phone1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone1"), 10, "T", crs.getString("preorder_id")))
								.append(ClickToCall(crs.getString("contact_phone1"), comp_id));
					}
					if (!crs.getString("contact_phone2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone2"), 10, "T", crs.getString("preorder_id")))
								.append(ClickToCall(crs.getString("contact_phone2"), comp_id));
					}
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("preorder_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("preorder_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("preorder_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}
					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("preorder_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}

					// Populating Tags in Enquiry list
					Str.append("<br><br>");
					String Tag = crs.getString("tag");
					Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='border: 1px solid aliceblue;top:-16px; background:");
					Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
					Tag = ReplaceStr(Tag, "StartName", "");
					Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
					Str.append(Tag);
					// Tags End

					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("vehstock_id").equals("0")) {
						Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\"><br>Stock ID: ")
								.append(crs.getString("vehstock_id")).append("</a></b>");
					}
					Str.append("</td>");
					Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("so_branch_id")).append("\">")
							.append(crs.getString("branch_name")).append("</a></td>\n");
					// Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					// Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td valign=top align=right>").append(crs.getString("preorder_amount")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("invoiceamount")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("receiptamount")).append("</td>");
					// Str.append("<td valign=top align=right>").append(crs.getString("receiptamountauthorize")).append("</td>");
					Str.append("</tr>");
					count++;
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><center><b>No Reports found!</b></center></font><br>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_order_by\" class=\"form-control\" id=\"dr_order_by\">");
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=so_promise_date").append(StrSelectdrop("so_promise_date", order_by)).append(">Tentative Date</option> \n");
		Str.append("<option value=preorder_date").append(StrSelectdrop("preorder_date", order_by)).append(">Pre-Order Date</option> \n");
		Str.append("<option value=fitmentstatus").append(StrSelectdrop("fitmentstatus", order_by)).append(">Fitment Status</option> \n");
		Str.append("</select>");
		return Str.toString();
	}

}
