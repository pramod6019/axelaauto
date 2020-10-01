package axela.accessories;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Accessories_Monitoring_Board extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, exe_ids;
	public String brand_id = "", region_id = "", exe_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch = "0";
	public String go = "";
	public String dr_totalby = "", dr_orderby = "0";
	public String ExeAccess = "";
	public String branch_name = "";
	public String StrSearch = "";
	public String SearchURL = "report-accessories-monitoring-board-search.jsp";
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
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					CheckForm();
					if (go.equals("Go")) {
						BranchAccess = BranchAccess.replace("branch_id", "emp_branch_id");
						ExeAccess += ExeAccess;
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							StrSearch += " AND branch_id IN (" + branch_id + ")";
						}
						if (!exe_id.equals("")) {
							StrSearch += " AND emp_id IN (" + exe_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListAccessoriesMonitoringBoard();
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
		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));
		if (dr_totalby.equals("0") && dr_totalby.equals("")) {
			dr_totalby = "emp_id";
		}

		dr_orderby = PadQuotes(request.getParameter("dr_orderby"));

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
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
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListAccessoriesMonitoringBoard() {
		try {

			StringBuilder Str = new StringBuilder();
			StringBuilder appendStr = new StringBuilder();
			appendStr.append(SearchURL)
					.append("?starttime=" + starttime).append("&endtime=" + endtime)
					.append("&brand_id=" + brand_id).append("&region_id=" + region_id).append("&dr_branch=" + dr_branch);

			StrSql = "SELECT emp_id, emp_name, emp_ref_no,";

			if (dr_totalby.equals("emp_branch_id")) {
				StrSql += " branch_name,";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " region_name,";
			}
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " brand_name,";
			}
			StrSql += " COALESCE (SUM( preordercount ), 0) AS preordercount,"
					+ " COALESCE (SUM( preorderamount ), 0) AS preorderamount,"
					+ " COALESCE (SUM( fitmentcount ), 0) AS fitmentcount,"
					+ " COALESCE (SUM( fitmentdue ), 0) AS fitmentdue,"
					+ " COALESCE (SUM( invoicecount ), 0) AS invoicecount,"
					+ " COALESCE (SUM( invoiceamount ), 0) AS invoiceamount,"
					+ " COALESCE (SUM( receiptamount ), 0) AS receiptamount"
					+ " FROM " + compdb(comp_id) + "axela_emp";
			// Branch
			if (!brand_id.equals("") || !branch_id.equals("") || !region_id.equals("") || dr_totalby.equals("emp_branch_id")
					|| dr_totalby.equals("branch_region_id") || dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
			}
			// Region
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			// Brand
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}
			StrSql += " LEFT JOIN ("
					+ " SELECT COUNT( DISTINCT voucher_id) as preordercount,"
					+ " SUM(IF ( vouchertrans_rowcount = 0 "
					+ " AND vouchertrans_option_id = 0, vouchertrans_amount, 0 ) ) AS preorderamount,"
					+ " COUNT(DISTINCT CASE WHEN voucher_fitted = 1 THEN voucher_id END) AS fitmentcount,"
					+ " COUNT(DISTINCT CASE WHEN voucher_fitted = 0 THEN voucher_id END) AS fitmentdue,"
					+ " voucher_id AS preorder_id, voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " AND voucher_vouchertype_id = 27";
			if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE 1 = 1"
					+ " AND vouchertrans_option_id = 0"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
					+ StrSearch.replace("emp_id", "voucher_emp_id").replace("branch_id", "voucher_branch_id")
					+ " GROUP BY voucher_emp_id"
					+ " ) AS tblvoucher ON tblvoucher.voucher_emp_id = emp_id"
					+ " LEFT JOIN ("
					+ " SELECT"
					+ " COUNT( voucher_id) AS invoicecount,"
					+ " SUM( voucher_amount) AS invoiceamount,"
					+ " voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher";
			if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id ";
			}
			StrSql += " WHERE 1 = 1"
					+ " AND voucher_vouchertype_id = 27"
					+ " AND voucher_id IN ( SELECT voucher_preorder_id FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_vouchertype_id = 6 )"
					+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
			StrSql += StrSearch.replace("emp_id", "voucher_emp_id").replace("branch_id", "voucher_branch_id")
					+ " GROUP BY voucher_emp_id"
					+ " ) AS tblvoucherinvoice ON tblvoucherinvoice.voucher_emp_id = emp_id"
					+ " LEFT JOIN ("
					+ " SELECT"
					+ " SUM(receipt.voucher_amount) AS receiptamount,"
					+ " preorder.voucher_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher preorder"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher invoice ON invoice.voucher_preorder_id = preorder.voucher_id"
					+ " AND invoice.voucher_vouchertype_id = 6"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher receipt ON receipt.voucher_invoice_id = invoice.voucher_id"
					+ " AND receipt.voucher_vouchertype_id = 9";
			if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preorder.voucher_branch_id ";
			}
			StrSql += " WHERE preorder.voucher_vouchertype_id = 27"
					+ " AND preorder.voucher_active = 1 "
					+ " AND SUBSTR(preorder.voucher_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(preorder.voucher_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
					+ StrSearch.replace("emp_id", "preorder.voucher_emp_id").replace("branch_id", "preorder.voucher_branch_id")
					+ " GROUP BY preorder.voucher_emp_id"
					+ " ) AS tblvoucherreceipt ON tblvoucherreceipt.voucher_emp_id = emp_id "
					+ " WHERE 1 = 1 "
					+ " AND emp_active = 1 ";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_active = 1 ";
			}
			StrSql += " AND emp_id != 1 "
					+ StrSearch;
			if (dr_totalby.equals("emp_id")) {
				StrSql += " GROUP BY emp_id  ";
			} else if (dr_totalby.equals("branch_region_id")) {
				StrSql += " GROUP BY branch_region_id  ";
			} else if (dr_totalby.equals("emp_branch_id")) {
				StrSql += " GROUP BY branch_id  ";
			} else if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " GROUP BY branch_brand_id  ";
			}

			if (dr_orderby.equals("0")) {
				StrSql += " ORDER BY emp_name";
			}
			else {
				StrSql += " ORDER BY " + dr_orderby + " DESC";
			}

			SOP("ListAccessoriesMonitoringBoard----" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 1;
				int total_preorder = 0;
				double total_preorderamount = 0;
				int total_fitmentcount = 0;
				int total_fitmentdue = 0;
				int total_invoicecount = 0;
				double total_invoiceamount = 0;
				double total_receiptamount = 0;

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				String title_head = "", id = "", total_by = "";
				switch (dr_totalby) {
					case "branch_brand_id" :
						title_head = "Brand";
						total_by = "brand";
						break;
					case "branch_region_id" :
						title_head = "Region";
						total_by = "region";
						break;
					case "emp_branch_id" :
						title_head = "Branch";
						total_by = "branch";
						break;
					default :
						title_head = "Executives";
						total_by = "emp";
						break;
				}

				Str.append("<th title=" + title_head + ">" + title_head + "</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Pre Order Count</th>\n");
				Str.append("<th data-hide=\"phone\">Pre Order Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Fitment Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Fitment Due</th>\n");
				Str.append("<th data-hide=\"phone\">Invoice Count</th>\n");
				Str.append("<th data-hide=\"phone\">Invoice Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Receipt Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {

					total_preorder += Integer.parseInt(crs.getString("preordercount"));
					total_preorderamount += Double.parseDouble(crs.getString("preorderamount"));
					total_fitmentcount += Integer.parseInt(crs.getString("fitmentcount"));
					total_fitmentdue += Integer.parseInt(crs.getString("fitmentdue"));
					total_invoicecount += Integer.parseInt(crs.getString("invoicecount"));
					total_invoiceamount += Double.parseDouble(crs.getString("invoiceamount"));
					total_receiptamount += Double.parseDouble(crs.getString("receiptamount"));

					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					switch (dr_totalby) {
						case "emp_branch_id" :
							Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
							id = branch_id;
							break;
						case "branch_region_id" :
							Str.append("<td align=left>").append(crs.getString("region_name")).append("</td>\n");
							id = region_id;
							break;
						case "branch_brand_id" :
							Str.append("<td align=left>").append(crs.getString("brand_name")).append("</td>\n");
							id = brand_id;
							break;
						default :
							Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
							Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append(" (");
							Str.append(crs.getString("emp_ref_no")).append(")</a></td>");
							id = emp_id;
							break;
					}
					Str.append("<td valign=top align=right>").append(crs.getString("preordercount")).append("</td>");
					Str.append("<td valign=top align=right>" + crs.getString("preorderamount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("fitmentcount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("fitmentdue") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("invoicecount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("invoiceamount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("receiptamount") + "</td>");
					Str.append("</tr>");
					count++;
				}
				if (count > 2) {
					Str.append("<tr>");
					Str.append("<td valign=top align=right colspan=2>Total: </td>");
					Str.append("<td valign=top align=right>" + total_preorder + "</td>");
					Str.append("<td valign=top align=right>" + deci.format(total_preorderamount) + "</td>");
					Str.append("<td valign=top align=right>" + total_fitmentcount + "</td>");
					Str.append("<td valign=top align=right>" + total_fitmentdue + "</td>");
					Str.append("<td valign=top align=right>" + total_invoicecount + "</td>");
					Str.append("<td valign=top align=right>" + deci.format(total_invoiceamount) + "</td>");
					Str.append("<td valign=top align=right>" + deci.format(total_receiptamount) + "</td>");
					Str.append("</tr>");
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
	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();

		Str.append("<option value=emp_id").append(StrSelectdrop("emp_id", dr_totalby)).append(">Executive</option>\n");
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=emp_branch_id").append(StrSelectdrop("emp_branch_id", dr_totalby)).append(">Branches</option>\n");

		return Str.toString();
	}

	public String PopulateOrderBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=emp_id").append(StrSelectdrop("executive", dr_orderby)).append(">Executive</option>\n");
		Str.append("<option value=preordercount").append(StrSelectdrop("preordercount", dr_orderby)).append(">Pre Order Count</option>\n");
		Str.append("<option value=preorderamount").append(StrSelectdrop("preorderamount", dr_orderby)).append(">Pre Order Amount</option>\n");
		Str.append("<option value=fitmentcount").append(StrSelectdrop("fitmentcount", dr_orderby)).append(">Fitment Count</option>\n");
		Str.append("<option value=fitmentdue").append(StrSelectdrop("fitmentamount", dr_orderby)).append(">Fitment Due</option>\n");
		Str.append("<option value=invoicecount").append(StrSelectdrop("invoicecount", dr_orderby)).append(">Invoice Count</option>\n");
		Str.append("<option value=invoiceamount").append(StrSelectdrop("invoiceamount", dr_orderby)).append(">Invoice Amount</option>\n");
		Str.append("<option value=receiptamount").append(StrSelectdrop("receiptamount", dr_orderby)).append(">Receipt Amount</option>\n");

		return Str.toString();
	}
}
