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

public class OneTriumph_Receipt_Print extends Connect {

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
			// CheckPerm(comp_id, "emp_acc_receipt_access", request, response);
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
					report.reportfrom = "/accounting/reports/" + reportfrom;
					report.parameters.put("REPORT_CONNECTION", conn);
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
		String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		String enquiry_income_id = "", testdrive_fb_taken = "";
		int enquiry_familymember_count = 0;
		String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		try {
			StrSql = "SELECT voucher_date, voucher_id, voucher_amount, voucher_customer_id,"
					+ " voucher_consignee_add, voucher_billing_add, COALESCE(voucher_so_id,'0') as voucher_so_id,"
					+ " voucher_narration, voucher_notes,vouchertype_name,"
					+ " vouchertrans_cheque_bank, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date, vouchertrans_paymode_id, vouchertype_label,"
					+ " paymode_name, voucher_authorize,"
					+ " vouchertrans_bank_id, vouchertrans_transaction_no,"
					+ " COALESCE (vouchertrans_cheque_branch,'') AS vouchertrans_cheque_branch,"
					+ " IF(COALESCE (fincomp_name, '') != '', COALESCE (fincomp_name, ''),'Other Bank') AS bank_name,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " branch_add, branch_pin, COALESCE(branchcity.city_name,'') AS branch_city_name ,"
					+ " branch_phone1, branch_mobile1,  branch_email1, branch_invoice_name,"
					+ " branch_logo, comp_logo, comp_name,"
					+ " customer_id, customer_name, customer_address, customer_pin, customer_mobile1,"
					+ " COALESCE(branchcity.city_name,'') as city_name,"
					+ " COALESCE(branchstate.state_name,'') as state_name,"
					+ " COALESCE(customercity.city_name,'') as acc_city,"
					+ " COALESCE( customerstate.state_name ,'') as acc_state,"
					+ " exe.emp_name as emp_name,"
					+ " COALESCE(exe.emp_phone1,'') as emp_phone1,"
					+ " COALESCE(exe.emp_mobile1,'') as emp_mobile1,"
					+ " COALESCE(exe.emp_email1,'') as emp_email1,"
					+ " COALESCE(teamlead.emp_name,'') as tl_emp_name,"
					+ " COALESCE(teamlead.emp_mobile1,'') as tl_emp_mobile1,"
					+ " COALESCE(teamlead.emp_email1,'') as tl_emp_email1\n,"
					+ " jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id =" + voucherclass_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = vouchertrans_bank_id"
					+ " INNER JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = exe.emp_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp teamlead on teamlead.emp_id = team_emp_id, "
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE voucher_id = " + voucher_id + BranchAccess + " " + ReplaceStr(ExeAccess, "emp_id", "exe.emp_id") + ""
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("ReceiptDetails====2==========" + StrSqlBreaker(StrSql));

			crs.beforeFirst();

			while (crs.next()) {
				dataMap = new HashMap();

				msg1 = "\nReceived a sum of Rs. " + (int) crs.getDouble("voucher_amount") + "/- (Rupees "
						+ toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount")))
						+ " Only/-) by ";

				msg1 += crs.getString("paymode_name");

				if (crs.getString("vouchertrans_paymode_id").equals("2")) {
					msg1 += " vide cheque number " + crs.getString("vouchertrans_cheque_no")
							+ " dated " + strToShortDate(crs.getString("vouchertrans_cheque_date"))
							+ " drawn on " + crs.getString("bank_name") + "";
				} else if (crs.getString("vouchertrans_paymode_id").equals("3")) {
					msg1 += " number " + crs.getString("vouchertrans_cheque_no") + " of "
							+ crs.getString("bank_name") + "";

					if (!crs.getString("vouchertrans_transaction_no").equals("")) {
						msg1 += " Transaction No.: " + crs.getString("vouchertrans_transaction_no");
					}
				} else if (!crs.getString("vouchertrans_paymode_id").equals("1")) {

					msg1 += " through " + crs.getString("bank_name") + "";

					if (!crs.getString("vouchertrans_transaction_no").equals("")) {
						msg1 += " Transaction No.: " + crs.getString("vouchertrans_transaction_no");
					}
				}

				if (!po_id.equals("0")) {
					msg1 += " towards PO ID " + crs.getString("voucher_id") + " dated " + strToShortDate(crs.getString("voucher_date")) + ".\n\n\n";
				}

				msg1 += " towards " + config_customer_name + "Receipt ID: " + crs.getString("voucher_id") + " dated " + strToShortDate(crs.getString("voucher_date"));

				if (!crs.getString("voucher_so_id").equals("0")) {
					msg1 += " for SO ID: " + crs.getString("voucher_so_id");
				}

				if (crs.getString("vouchertrans_paymode_id").equals("2") && !crs.getString("vouchertrans_cheque_branch").equals("")) {
					msg += " IN Branch" + crs.getString("vouchertrans_cheque_branch") + " ";
				}

				msg1 += ".\n\n\n";

				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", crs.getString("customer_address") + ", "
						+ crs.getString("acc_city") + "-"
						+ crs.getString("customer_pin") + ", "
						+ crs.getString("acc_state"));
				dataMap.put("customer_mobile1", crs.getString("customer_mobile1"));
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("voucher_id", crs.getString("voucher_id"));
				dataMap.put("voucher_amount", df.format(crs.getDouble("voucher_amount")));
				dataMap.put("customer_id", crs.getString("customer_id"));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_add", crs.getString("branch_add") + ", "
						+ crs.getString("branch_city_name") + "-" + crs.getString("branch_pin"));
				dataMap.put("branch_mobile1", crs.getString("branch_mobile1"));
				dataMap.put("branch_email1", crs.getString("branch_email1"));
				dataMap.put("team_lead_name", crs.getString("tl_emp_name"));
				dataMap.put("team_lead_mob", crs.getString("tl_emp_mobile1"));
				dataMap.put("team_lead_email", crs.getString("tl_emp_email1"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("emp_mobile1", crs.getString("emp_mobile1"));
				dataMap.put("msg1", msg1);
				dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));

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
