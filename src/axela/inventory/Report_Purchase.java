package axela.inventory;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Purchase extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String msg = "", BranchAccess = "";
	public String emp_id = "";
	public String StrHTML = "";
	public String[] brand_ids, region_ids, branch_ids, location_ids;
	public String brand_id = "", region_id = "", branch_id = "", location_id = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "", StrSearch = "";
	public MIS_Check mischeck = new MIS_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access, emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "voucher_branch_id");
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND voucher_branch_id IN (SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_region_id IN (" + region_id + "))";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
					}
					if (!location_id.equals("")) {
						StrSearch += " AND location_id IN (" + location_id + ")";
					}
					if (msg.equals("")) {
						StrHTML = PurchaseDetails();
					} else {
						msg = "Error" + msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		location_id = RetrunSelectArrVal(request, "dr_location");
		location_ids = request.getParameterValues("dr_location");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				end_time = strToShortDate(endtime);
				if (!starttime.equals("") && !endtime.equals("")
						&& Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			}
		}
	}

	public String PurchaseDetails() {
		StringBuilder Str = new StringBuilder();
		int count = 1;
		double totalunits = 0;
		double totalprice = 0;
		double totalpurchaseprice = 0;
		double totaldiscount = 0;
		double totaltax = 0;
		double totalamount = 0;
		try {
			StrSql = "SELECT CONCAT(branch_invoice_prefix,voucher_no,branch_invoice_suffix) As InvoiceNo, voucher_id, voucher_date,"
					+ " COALESCE (customer_name, '') AS Suppiler,"
					+ " COALESCE (SUM(vouchertrans_qty), 0) AS units,"
					+ " SUM( CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_price END ) AS price,"
					+ " voucher_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_branch_id = voucher_branch_id "
					+ " WHERE item_type_id != 1"
					+ StrSearch
					+ " AND voucher_vouchertype_id = 21"
					+ " AND voucher_active = '1'"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_date DESC";

			SOP("StrSql==PurchaseDetails=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" style=\"border-collapse:unset\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#<br><br></th>\n");
				Str.append("<th>Invoice No<br><br></th>\n");
				Str.append("<th>Invoice Date<br><br></th>\n");
				Str.append("<th data-hide=\"phone\">Suppiler</th>\n");
				Str.append("<th data-hide=\"phone\">Units</th>\n");
				Str.append("<th data-hide=\"phone\">Purchase Price</th>\n");
				Str.append("<th data-hide=\"phone\">Amount</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tbody><tr>\n");
					Str.append("<td style='text-align:center'>");
					Str.append(count++);
					Str.append("</td>\n");
					// Invoice No
					Str.append("<td align=\"center\">");
					Str.append("<a href=\"../accounting/voucher-list.jsp?voucher_id=").append(crs.getString("voucher_id")).append("\">");
					Str.append(crs.getString("InvoiceNo"));
					Str.append("</td>\n");
					// Invoice Date
					Str.append("<td align=\"left\">");
					Str.append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n");
					// Suppiler
					Str.append("<td align=\"left\">");
					Str.append(crs.getString("Suppiler"));
					Str.append("</td>\n");
					// Units
					Str.append("<td align=\"right\">");
					Str.append(crs.getString("units"));
					Str.append("</td>\n");
					// Purchase Price
					Str.append("<td align=\"right\">");
					Str.append(IndDecimalFormat(deci.format(crs.getDouble("price"))));
					Str.append("</td>\n");
					// Amount
					Str.append("<td align=\"right\">");
					Str.append(IndDecimalFormat(deci.format(crs.getDouble("voucher_amount"))));
					Str.append("</td>\n");
					// total calculation
					totalunits += Double.parseDouble(crs.getString("units"));
					totalpurchaseprice += Double.parseDouble(crs.getString("price"));
					totalamount += Double.parseDouble(crs.getString("voucher_amount"));
				}
				Str.append("<tr>");
				Str.append("<td valign=top align=right colspan=4><b>");
				Str.append(" Total:</b></td>");

				Str.append("<td align=\"right\"><b>");
				Str.append(IndDecimalFormat(deci.format(totalunits)));
				Str.append("</b></td>\n");

				Str.append("<td align=\"right\"><b>");
				Str.append(IndDecimalFormat(deci.format(totalpurchaseprice)));
				Str.append("</b></td>\n");

				Str.append("<td align=\"right\"><b>");
				Str.append(IndDecimalFormat(deci.format(totalamount)));
				Str.append("</b></td>\n");

				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table >");
			} else {
				Str.append("<center><b><font color=red>No Purchase Data Found!</font></b></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
