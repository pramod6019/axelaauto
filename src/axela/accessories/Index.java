package axela.accessories;
//Murali 21st jun
//divya
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String brand_id = "", region_id = "", branch_id = "0";

	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String StrLibrary = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ticketchart_data = "";
	public String callchart_data = "";
	public String jcprioritychart_data = "";
	public String servicefollowupescchart_data = "";
	public String psfchart_data = "";
	public String insurancechart_data = "";
	public int TotalRecords = 0;
	// days
	public String preordercount = "0";
	public String preorderamount = "0";
	public String invoicecount = "0";
	public String invoiceamount = "0";
	// months
	public String preordercountmonth = "0";
	public String preorderamountmonth = "0";
	public String invoicecountmonth = "0";
	public String invoiceamountmonth = "0";
	// years
	public String preordercountquarter = "0";
	public String preorderamountquarter = "0";
	public String invoicecountquarter = "0";
	public String invoiceamountquarter = "0";

	// days
	public String preordercountold = "0";
	public String preorderamountold = "0";
	public String invoicecountold = "0";
	public String invoiceamountold = "0";
	// months
	public String preordercountmonthold = "0";
	public String preorderamountmonthold = "0";
	public String invoicecountmonthold = "0";
	public String invoiceamountmonthold = "0";
	// years
	public String preordercountquarterold = "0";
	public String preorderamountquarterold = "0";
	public String invoicecountquarterold = "0";
	public String invoiceamountquarterold = "0";

	// refresh All AJAX
	public String refreshAll = "", cards = "";

	public String datetype = "", StrHTML = "";
	public String curr_date = ToShortDate(kknow());
	public String back_date = "";
	public String curr_month = "", back_month = "";
	public String startquarter = "", endquarter = "";
	public String back_startquarter = "", back_endquarter = "";

	public axela.accessories.MIS_Check mischeck = new axela.accessories.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_preorder_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				smart = PadQuotes(request.getParameter("smart"));

				refreshAll = PadQuotes(request.getParameter("refreshAll"));

				cards = PadQuotes(request.getParameter("cards"));

				filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
				filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
				filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));

				if (smart == null) {
					smart = "";
				}

				if (msg.equals("") && refreshAll.equals("")) {

					if (smart == null) {
						smart = "";
					}
				} else if (msg.equals("")) {

					if (refreshAll.equals("yes") && cards.equals("yes")) {
						populateconfigdetails();
						StrHTML = AccessoriesDetails(comp_id);
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 14 "
				+ " AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		// SOP("StrSql===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"  \">\n");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String AccessoriesDetails(String comp_id) {
		String new_value = "", old_value = "";
		try {
			CachedRowSet crs = null;

			curr_date = curr_date.substring(0, 8);
			curr_month = ToShortDate(kknow()).substring(0, 6);

			if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 1 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 3) {
				startquarter = ToShortDate(kknow()).substring(0, 4) + "01";
				endquarter = ToShortDate(kknow()).substring(0, 4) + "03";
			} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 4 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 6) {
				startquarter = ToShortDate(kknow()).substring(0, 4) + "04";
				endquarter = ToShortDate(kknow()).substring(0, 4) + "06";
			} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 7 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 9) {
				startquarter = ToShortDate(kknow()).substring(0, 4) + "07";
				endquarter = ToShortDate(kknow()).substring(0, 4) + "09";
			} else {
				startquarter = ToShortDate(kknow()).substring(0, 4) + "10";
				endquarter = ToShortDate(kknow()).substring(0, 4) + "12";
			}

			StrSql = "SELECT"
					+ " COALESCE (SUM(preorder_count), 0) AS preorder_count,"
					+ " COALESCE (SUM(preorder_amount), 0) AS preorder_amount,"
					+ " COALESCE (SUM(preorder_count_month), 0) AS preorder_count_month,"
					+ " COALESCE (SUM(preorder_amount_month), 0) AS preorder_amount_month,"
					+ " COALESCE (SUM(preorder_count_quarter), 0) AS preorder_count_quarter,"
					+ " COALESCE (SUM(preorder_amount_quarter), 0) AS preorder_amount_quarter,"
					+ " COALESCE (SUM(invoice_count), 0) AS invoice_count,"
					+ " COALESCE (SUM(invoice_amount), 0) AS invoice_amount,"
					+ " COALESCE (SUM(invoice_count_month), 0) AS invoice_count_month,"
					+ " COALESCE (SUM(invoice_amount_month), 0) AS invoice_amount_month,"
					+ " COALESCE (SUM(invoice_count_quarter), 0) AS invoice_count_quarter,"
					+ " COALESCE (SUM(invoice_amount_quarter), 0) AS invoice_amount_quarter,"
					+ " COALESCE (SUM(preorder_count_old), 0) AS preorder_count_old,"
					+ " COALESCE (SUM(preorder_amount_old), 0) AS preorder_amount_old,"
					+ " COALESCE (SUM(preorder_count_month_old), 0) AS preorder_count_month_old,"
					+ " COALESCE (SUM(preorder_amount_month_old), 0) AS preorder_amount_month_old,"
					+ " COALESCE (SUM(preorder_count_quarter_old), 0) AS preorder_count_quarter_old,"
					+ " COALESCE (SUM(preorder_amount_quarter_old), 0) AS preorder_amount_quarter_old,"
					+ " COALESCE (SUM(invoice_count_old), 0) AS invoice_count_old,"
					+ " COALESCE (SUM(invoice_amount_old), 0) AS invoice_amount_old,"
					+ " COALESCE (SUM(invoice_count_month_old), 0) AS invoice_count_month_old,"
					+ " COALESCE (SUM(invoice_amount_month_old), 0) AS invoice_amount_month_old,"
					+ " COALESCE (SUM(invoice_count_quarter_old), 0) AS invoice_count_quarter_old,"
					+ " COALESCE (SUM(invoice_amount_quarter_old), 0) AS invoice_amount_quarter_old"
					+ " FROM  " + compdb(comp_id) + "axela_emp";
			// Branch
			if (!filter_brand_id.equals("") || !filter_branch_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
			}

			StrSql += " LEFT JOIN ("
					+ " SELECT"

					// Current Date
					+ " COUNT(DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 8) = '" + curr_date + "' THEN"
					+ " voucher_id END"
					+ " ) AS preorder_count,"

					+ " SUM(IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) = '" + curr_date + "',"
					+ " vouchertrans_amount, 0 )) AS preorder_amount,"

					+ " COUNT(DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 6) = '" + curr_month + "'"
					+ " THEN voucher_id END"
					+ " ) AS preorder_count_month,"

					+ " SUM(IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 6) = '" + curr_month + "',"
					+ " vouchertrans_amount, 0 ) ) AS preorder_amount_month,"

					+ " COUNT( DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 8) >= '" + startquarter + "'"
					+ " AND SUBSTR(voucher_date, 1, 8) <= '" + endquarter + "' THEN"
					+ " voucher_id END ) AS preorder_count_quarter,"

					+ " SUM( IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) >= '" + startquarter + "'"
					+ " AND SUBSTR(voucher_date, 1, 8) <= '" + endquarter + "',"
					+ " vouchertrans_amount, 0 ) ) AS preorder_amount_quarter,"

					// Previous Date
					+ " COUNT(DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 8) = '" + back_date + "' THEN"
					+ " voucher_id END"
					+ " ) AS preorder_count_old,"

					+ " SUM(IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) = '" + back_date + "',"
					+ " vouchertrans_amount, 0 )) AS preorder_amount_old,"

					+ " COUNT(DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 6) = '" + back_month + "'"
					+ " THEN voucher_id END) AS preorder_count_month_old,"

					+ " SUM( IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 6) = '" + back_month + "',"
					+ " vouchertrans_amount, 0 ) ) AS preorder_amount_month_old,"

					+ " COUNT( DISTINCT CASE WHEN"
					+ " voucher_id"
					+ " AND SUBSTR(voucher_date, 1, 8) >= '" + back_startquarter + "'"
					+ " AND SUBSTR(voucher_date, 1, 8) <= '" + back_endquarter + "' THEN"
					+ " voucher_id END ) AS preorder_count_quarter_old,"

					+ " SUM( IF ( vouchertrans_rowcount = 0 AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) >= '" + back_startquarter + "'"
					+ " AND SUBSTR(voucher_date, 1, 8) <= '" + back_endquarter + "',"
					+ " vouchertrans_amount, 0 ) ) AS preorder_amount_quarter_old,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " AND voucher_vouchertype_id = 27";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE 1 = 1"
					+ " AND vouchertrans_option_id = 0";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}

			StrSql += " GROUP BY voucher_emp_id ) AS tblpreorder ON tblpreorder.voucher_emp_id = emp_id"

					+ " LEFT JOIN ("
					+ " SELECT"
					+ " COUNT(voucher_id) AS invoice_count,"
					+ " SUM(voucher_amount) AS invoice_amount,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 8) = " + curr_date;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoice ON tblvoucherinvoice.voucher_emp_id = emp_id"

					+ " LEFT JOIN ("
					+ " SELECT "
					+ " COUNT(voucher_id) AS invoice_count_month,"
					+ " SUM(voucher_amount) AS invoice_amount_month,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 6) >= " + curr_month;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoicemonth ON tblvoucherinvoicemonth.voucher_emp_id = emp_id"

					+ " LEFT JOIN ("
					+ " SELECT"
					+ " COUNT(voucher_id) AS invoice_count_quarter,"
					+ " SUM(voucher_amount) AS invoice_amount_quarter,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(voucher_date, 1, 6) <= " + endquarter;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoicequarter ON tblvoucherinvoicequarter.voucher_emp_id = emp_id ";

			StrSql += " LEFT JOIN ("
					+ " SELECT"
					+ " COUNT(voucher_id) AS invoice_count_old,"
					+ " SUM(voucher_amount) AS invoice_amount_old,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 8) = " + back_date;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoiceold ON tblvoucherinvoiceold.voucher_emp_id = emp_id"

					+ " LEFT JOIN ("
					+ " SELECT "
					+ " COUNT(voucher_id) AS invoice_count_month_old,"
					+ " SUM(voucher_amount) AS invoice_amount_month_old,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_branch_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 6) >= " + back_month;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoicemonthold ON tblvoucherinvoicemonthold.voucher_emp_id = emp_id"

					+ " LEFT JOIN ("
					+ " SELECT"
					+ " COUNT(voucher_id) AS invoice_count_quarter_old,"
					+ " SUM(voucher_amount) AS invoice_amount_quarter_old,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ("
					+ " SELECT voucher_preorder_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE voucher_vouchertype_id = 6"
					+ " AND voucher_active = 1"
					+ " AND SUBSTR(voucher_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(voucher_date, 1, 6) <= " + back_endquarter;
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND voucher_branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoicequarterold ON tblvoucherinvoicequarterold.voucher_emp_id = emp_id ";

			// SOP("StrSql====AccessoriesDetails===" + StrSql);

			crs = processQuery(StrSql);
			while (crs.next()) {
				preordercount = crs.getString("preorder_count");
				preorderamount = crs.getString("preorder_amount");
				invoicecount = crs.getString("invoice_count");
				invoiceamount = crs.getString("invoice_amount");

				preordercountmonth = crs.getString("preorder_count_month");
				preorderamountmonth = crs.getString("preorder_amount_month");
				invoicecountmonth = crs.getString("invoice_count_month");
				invoiceamountmonth = crs.getString("invoice_amount_month");

				preordercountquarter = crs.getString("preorder_count_quarter");
				preorderamountquarter = crs.getString("preorder_amount_quarter");
				invoicecountquarter = crs.getString("invoice_count_quarter");
				invoiceamountquarter = crs.getString("invoice_amount_quarter");

				// ----------------------------------NEW Values-------------------------------
				new_value = preordercount + "," + preordercountmonth + "," + preordercountquarter + ","
						+ preorderamount + "," + preorderamountmonth + "," + preorderamountquarter + ","
						+ invoicecount + "," + invoicecountmonth + "," + invoicecountquarter + ","
						+ invoiceamount + "," + invoiceamountmonth + "," + invoiceamountquarter;
				// ----------------------------------------------------------------------------

				preordercountold = crs.getString("preorder_count_old");
				preorderamountold = crs.getString("preorder_amount_old");
				invoicecountold = crs.getString("invoice_count_old");
				invoiceamountold = crs.getString("invoice_amount_old");

				preordercountmonthold = crs.getString("preorder_count_month_old");
				preorderamountmonthold = crs.getString("preorder_amount_month_old");
				invoicecountmonthold = crs.getString("invoice_count_month_old");
				invoiceamountmonthold = crs.getString("invoice_amount_month_old");

				preordercountquarterold = crs.getString("preorder_count_quarter_old");
				preorderamountquarterold = crs.getString("preorder_amount_quarter_old");
				invoicecountquarterold = crs.getString("invoice_count_quarter_old");
				invoiceamountquarterold = crs.getString("invoice_amount_quarter_old");

				// ---------------------------Old Values-----------------------------

				old_value = preordercountold + "," + preordercountmonthold + "," + preordercountquarterold + ","
						+ preorderamountold + "," + preorderamountmonthold + "," + preorderamountquarterold + ","
						+ invoicecountold + "," + invoicecountmonthold + "," + invoicecountquarterold + ","
						+ invoiceamountold + "," + invoiceamountmonthold + "," + invoiceamountquarterold;

				// ------------------------------------------------------------------
			}
			crs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_value + ":" + old_value;
	}
	public void populateconfigdetails() {

		curr_date = curr_date.substring(0, 8);
		curr_month = ToShortDate(kknow()).substring(0, 6);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		back_month = ToLongDate(cal.getTime()).substring(0, 6);
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		back_date = ToLongDate(cal.getTime()).substring(0, 8);

		if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 1 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 3) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "01";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "03";

			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "10";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "12";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 4 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 6) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "04";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "06";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "01";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "03";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 7 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 9) {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "07";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "09";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "04";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "06";
		} else {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "10";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "12";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "07";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "09";
		}

	}
}
