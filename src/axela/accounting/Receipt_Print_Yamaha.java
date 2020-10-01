package axela.accounting;

//Bhavya 19th Apr 2016

import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import cloudify.connect.Connect;

public class Receipt_Print_Yamaha extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "9";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public String voucher_paymode = "", voucher_remarks = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	public String emp_id = "0";
	public String enquiry_id = "0", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "";
	public String msg = "";
	public String msg1 = "";
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_receipt_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				voucher_authorize = CNumeric(PadQuotes(request.getParameter("voucher_authorize")));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));

				voucher_authorize = CNumeric(ExecuteQuery("SELECT voucher_authorize"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_vouchertype_id = 9"
						+ " AND voucher_id =" + voucher_id));

				if (voucher_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}

				if (!comp_id.equals("0") && !voucher_authorize.equals("0")) {
					BranchAccess = CheckNull(GetSession("BranchAccess", request));
					ExeAccess = CheckNull(GetSession("ExeAccess", request));
					enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
					brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
					voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
					vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));

					conn = connectDB();
					JasperReport report = new JasperReport();

					if (brand_id.equals("7")) {
						reportfrom = "receipt-print-ford";
						report.reportfrom = "/accounting/reports/" + reportfrom;
					}
					else {
						report.reportfrom = "/accounting/reports/" + reportfrom;
					}
					SOP("reportfrom==" + reportfrom);
					// report.parameters.put("REPORT_CONNECTION", conn);
					String receipt_terms = PadQuotes(ExecuteQuery("SELECT COALESCE (config_receipt_terms, '') AS config_receipt_terms FROM " + compdb(comp_id) + "axela_config"));

					report.parameters.put("html_text", receipt_terms);

					report.dataList = ReceiptDetails();
					report.doPost(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}

	}
	public List<Map> ReceiptDetails() throws IOException {

		HashMap dataMap;
		try {
			StrSql = "SELECT"
					+ " voucher_date,"
					+ " voucher_id, voucher_amount, voucher_customer_id, voucher_consignee_add, voucher_billing_add, "
					+ " voucher_narration, voucher_notes,vouchertype_name, COALESCE(voucher_so_id,'0') AS voucher_so_id,"
					+ " vouchertrans_cheque_bank,"
					+ " COALESCE(vouchertrans_cheque_no,'') AS vouchertrans_cheque_no,"
					+ " DATE_FORMAT(vouchertrans_cheque_date,'%d-%b-%Y') AS vouchertrans_cheque_date,"
					+ " vouchertrans_paymode_id, vouchertype_label,"
					+ " paymode_name, voucher_authorize,"
					+ " vouchertrans_bank_id, vouchertrans_transaction_no,"
					+ " COALESCE (vouchertrans_cheque_branch,'') AS vouchertrans_cheque_branch,"
					+ " IF(COALESCE (fincomp_name, '') != '', COALESCE (fincomp_name, ''),'') AS bank_name,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " branch_add, branch_pin, branch_phone1, branch_phone2, branch_mobile1, branch_email1, branch_invoice_name,"
					+ " COALESCE (branchcity.city_name, '') AS city_name,"
					+ " comp_logo, comp_name,"
					+ " customer_name, customer_address, customer_pin,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id =" + voucherclass_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = vouchertrans_bank_id"
					+ " INNER JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id ,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE voucher_id = " + voucher_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("TrackingDetails====2==========" + StrSqlBreaker(StrSql));

			crs.beforeFirst();

			while (crs.next()) {
				dataMap = new HashMap();

				String cheque_no = "", branch_address = "", branch_phone = "";

				if (!crs.getString("branch_add").equals("")) {
					branch_address = crs.getString("branch_add") + ", " + crs.getString("city_name");
				}
				if (!crs.getString("branch_pin").equals("")) {
					branch_address += (crs.getString("branch_pin"));
				}

				if (!crs.getString("branch_phone1").equals("")) {
					branch_phone = crs.getString("branch_phone1");
				} else {
					branch_phone = crs.getString("branch_phone2");
				}
				dataMap.put("paymode_name", crs.getString("paymode_name") + " Receipt");
				dataMap.put("branch_address", branch_address);
				dataMap.put("branch_phone", branch_phone);
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("voucher_amount", crs.getDouble("voucher_amount"));
				dataMap.put("voucher_narration", crs.getString("voucher_narration"));

				if (crs.getString("vouchertrans_paymode_id").equals("2")) {
					cheque_no = "Cheque No. :" + crs.getString("vouchertrans_cheque_no") + "  " + crs.getString("vouchertrans_cheque_date");
				} else if (crs.getString("vouchertrans_paymode_id").equals("3")) {
					cheque_no = "Card Number : " + crs.getString("vouchertrans_cheque_no");

				} else if (crs.getString("vouchertrans_paymode_id").equals("4") || crs.getString("vouchertrans_paymode_id").equals("5") || crs.getString("vouchertrans_paymode_id").equals("6")) {
					cheque_no = "Transaction No : " + crs.getString("vouchertrans_cheque_no");
				} else if (crs.getString("vouchertrans_paymode_id").equals("1")) {
					cheque_no = "Cash";
				}
				dataMap.put("cheque_no", cheque_no);
				if (!crs.getString("bank_name").equals("")) {
					dataMap.put("bank_name", "Bank Name : " + crs.getString("bank_name"));
					dataMap.put("bank_name1", crs.getString("bank_name"));
				} else {
					dataMap.put("bank_name1", "");
				}
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("voucher_no", unescapehtml(crs.getString("voucher_no")));
				dataMap.put("total_amount", toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount"))) + " Only");
				dataList.add(dataMap);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return dataList;
	}
}
