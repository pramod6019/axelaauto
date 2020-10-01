package axela.invoice;
//Divya 5th dec

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Branch_List4;
import cloudify.connect.Connect;

public class Invoice extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=../accounting/voucher-list.jsp?vouchertype_id=6&voucherclass_id=6&smart=yes>Click here to List Invoices</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_voucher_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));

				double duehours = 9;
				new Branch_List4().DueTime("20130316150000", 9.3, 18.3, duehours, "0", "1", "1", "1", "1", "1", "1");

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(voucher_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(voucher_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and voucher_branch_id=" + dr_branch_id;
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("voucherstrsql", StrSearch, request);
						StrHTML = InvoiceSummary(request);
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
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
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
			msg = msg + "<br>Select End Date!";
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

	public String InvoiceSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		DecimalFormat deci = new DecimalFormat("#.##");
		int voucher_count = 0;
		double nettotal = 0;
		try {
			StrSql = " SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname, "
					+ " count(voucher_id) as invoicecount, coalesce(sum(voucher_amount),0) as total "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class on voucherclass_id = vouchertype_voucherclass_id"
					+ " INNER join " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id "
					+ " WHERE 1 = 1 and voucher_active = '1' and branch_active = '1'"
					+ " AND vouchertype_id = 6 AND vouchertype_voucherclass_id = 6 "
					+ StrSearch + BranchAccess + ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY branch_id ORDER BY branchname";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
			//	Str.append("<b>Invoice Summary</b><br>");
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">"
						+ "<div class=\"caption\" style=\"float: none\">Invoice Summary</div></div>"
						+ "<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered\">");
				Str.append("<th>Branch</th>\n");
				Str.append("<th>Invoice Count</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					voucher_count = voucher_count + crs.getInt("invoicecount");
					nettotal = nettotal + crs.getDouble("total");
					Str.append("<tr>\n");
					Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td>").append(crs.getString("invoicecount")).append("</td>");
					Str.append("<td> ").append(IndFormat(crs.getString("total"))).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td><b>Total: </b></td>\n");
				Str.append("<td><b>").append(voucher_count).append("</b></td>\n");
				Str.append("<td><b>").append(IndFormat(deci.format(nettotal))).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</table></div></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
