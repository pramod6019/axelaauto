package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_DuplicateVoucher_No extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";

	public String StrSearch = "";
	public String all = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String comp_id = "0";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "0";
	public String vouchertype_name = "";
	public String QueryString = "";
	public String customer_id = "0";
	public String so_id = "0";
	public String voucher_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				StrHTML = Listdata();

			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		StrSql = "SELECT vouchertype_name,branch_invoice_prefix, branch_invoice_suffix,"
				+ " branch_receipt_prefix, branch_receipt_suffix, branch_bill_prefix, vouchertype_id, "
				+ " vouchertype_prefix, vouchertype_suffix,"
				+ " CONCAT( branch_name, '(', branch_id, ')' ) AS branch_name, voucher_no,"
				+ " COUNT(voucher_no)  as duplicatevoucher_no";

		CountSql = "SELECT COUNT(DISTINCT(voucher_id))";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE 1 = 1"
				+ " AND voucher_no !=0"
				+ " GROUP BY vouchertype_id,"
				+ " voucher_branch_id,"
				+ " voucher_no HAVING COUNT(voucher_no) > 1";
		StrSql += SqlJoin;
		CountSql += SqlJoin;
		// SOP("StrSql==111==" + StrSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

		// SOP("TotalRecords======" + TotalRecords);

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " Voucher(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "report-duplicatevoucher-no.jsp?" + QueryString
					+ "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			// SOP("StrSql=222==" + StrSql);
			crs = processQuery(StrSql, 0);

			try {

				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>Voucher Type</th>\n");
				Str.append("<th data-hide=\"phone\">Branch Name</th>\n");
				Str.append("<th data-hide=\"phone\">Voucher No</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Duplicate Voucher No Count.</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					String voucher_nofx = "";
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("vouchertype_name")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("branch_name")).append("</td>\n");
					Str.append("<td valign=top align=center>");
					if (!crs.getString("voucher_no").equals("0")) {
						if (crs.getString("vouchertype_id").equals("6")) {
							voucher_nofx = crs.getString("branch_invoice_prefix") + crs.getString("voucher_no") + crs.getString("branch_invoice_suffix");
							Str.append(voucher_nofx);

						} else if (crs.getString("vouchertype_id").equals("9")) {
							voucher_nofx = crs.getString("branch_receipt_prefix") + crs.getString("voucher_no") + crs.getString("branch_receipt_suffix");
							Str.append(voucher_nofx);
						} else if (crs.getString("vouchertype_id").equals("7")) {

							voucher_nofx = crs.getString("branch_bill_prefix") + crs.getString("voucher_no");
							Str.append(voucher_nofx);
						} else {
							voucher_nofx = crs.getString("vouchertype_prefix") + crs.getString("voucher_no") + crs.getString("vouchertype_suffix");
							Str.append(voucher_nofx);
						}
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=center>");
					String link = "voucher-list.jsp?vouchertype_id=" + crs.getString("vouchertype_id") + "&voucherclass_id=" + crs.getString("vouchertype_id")
							+ "&1_dr_field=2-text&1_dr_param=2-text&1_txt_value_1=" + voucher_nofx
							+ "&1_dr_filter=and&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1";
					Str.append("<a href=" + link + " target=_blank>" + crs.getString("duplicatevoucher_no") + "</a>");
					Str.append("</td>\n");

					// SOP("StrSql_Summary======" + StrSql_Summary);

				}
				crs.close();
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>");

			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {

			RecCountDisplay = "<br><br><br><br><font color=red><b>No Voucher(s) Found!</b></font><br><br>";
		}
		return Str.toString();
	}
}
