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

public class DebitNote_Print extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
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
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf";
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
			CheckPerm(comp_id, "emp_acc_debit_note_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				emp_id = CNumeric(GetSession("emp_id", request));
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				formatdigit_id = GetSession("formatdigit_id", request);
				config_format_decimal = GetSession("config_format_decimal", request);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = DebitNoteDetails();
				report.doPost(request, response);
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

	public List<Map> DebitNoteDetails() throws IOException {

		HashMap dataMap;
		String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		String enquiry_income_id = "", testdrive_fb_taken = "";
		int enquiry_familymember_count = 0;
		try {
			StrSql = "SELECT comp_name, comp_logo, voucher_id,"
					+ " CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
					+ " COALESCE (vouchertype_id, '') AS vouchertype_id,"
					+ " voucherclass_id, vouchertype_name,"
					+ " COALESCE (voucher_amount, '') AS voucher_amount,"
					+ " COALESCE (voucher_billing_add, '') AS voucher_billing_add,"
					+ " COALESCE (voucher_consignee_add, '') AS voucher_consignee_add,"
					+ " COALESCE (voucher_notes, '') AS voucher_notes,"
					+ " COALESCE (voucher_narration, '') AS voucher_narration,"
					+ " voucher_date, vouchertrans_cheque_bank, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date, vouchertrans_paymode_id,"
					+ " COALESCE (paymode_name, '') AS paymode_name,"
					+ " CONCAT( branch_name, ' (', branch_code, ')' ) AS branch_name,"
					+ " COALESCE (branch_add, '0') AS branch_add,"
					+ " COALESCE (branch_phone1, '0') AS branch_phone1,"
					+ " COALESCE (branch_email1, '0') AS branch_email1,"
					+ " COALESCE (branch_mobile1, '0') AS branch_mobile1,"
					+ " COALESCE (branch_pin, '0') AS branch_pin,"
					+ " COALESCE (voucher_customer_id, '0') AS voucher_customer_id,"
					+ " COALESCE (customer_id, '0') AS customer_id,"
					+ " COALESCE (customer_name, '') AS customer_name,"
					+ " COALESCE (customer_address, '') AS customer_address,"
					+ " COALESCE (customer_pin, '') AS customer_pin,"
					+ " COALESCE (customer_code, '') AS customer_code,"
					+ " COALESCE (title_desc, '') AS title_desc,"
					+ " COALESCE (contact_fname, '') AS contact_fname,"
					+ " COALESCE (contact_lname, '') AS contact_lname,"
					+ " COALESCE (CONCAT( emp_name, ' (', emp_ref_no, ')' ) ) AS emp_name,"
					+ " COALESCE (emp_photo, '') AS emp_photo,"
					+ " COALESCE (emp_sex, '') AS emp_sex,"
					+ " COALESCE (emp_id, '') AS emp_id,"
					+ " COALESCE (emp_phone1, '') AS emp_phone1,"
					+ " COALESCE (emp_mobile1, '') AS emp_mobile1,COALESCE (emp_email1, '') AS emp_email1,"
					+ " COALESCE (jobtitle_desc, '') AS jobtitle_desc, COALESCE (vouchertype_prefix, '') AS vouchertype_prefix,"
					+ " COALESCE (vouchertype_suffix, '') AS vouchertype_suffix,"
					+ " COALESCE ( IF ( vouchertype_id = 9, ("
					+ " SELECT customer_ledgertype FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
					+ " WHERE"
					+ " vouchertrans_voucher_id = voucher_id"
					+ " AND vouchertrans_dc = 1 LIMIT 1 ),"
					+ " IF ( vouchertype_id = 15, ( SELECT customer_ledgertype"
					+ " FROM "
					+ compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
					+ " WHERE vouchertrans_voucher_id = voucher_id AND vouchertrans_dc = 0 LIMIT 1"
					+ " ), 0 ) ), 0 ) AS paymodeid,"
					+ " COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE"
					+ " 1 = 1"
					+ " AND voucher_id = " + voucher_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_voucherclass_id = " + voucherclass_id
					+ " GROUP BY"
					+ " voucher_id";

			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("DebitenoteDetails====2==========" + StrSqlBreaker(StrSql));

			while (crs.next()) {
				dataMap = new HashMap();
				emp_name = crs.getString("emp_name").toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
				// msg1 details
				msg1 = "Dear Sir\n";

				msg1 += "\nWe have debited to your Account Rupees. " + (int) crs.getDouble("voucher_amount") + "/- (Rupees "
						+ toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount")))
						+ " Only/-) ";

				if (crs.getString("vouchertrans_paymode_id").equals("2")) {
					msg1 += " vide cheque number " + crs.getString("vouchertrans_cheque_no") + " dated " + strToShortDate(crs.getString("vouchertrans_cheque_date")) + " drawn on "
							+ crs.getString("vouchertrans_cheque_bank") + "";
				}

				if (!po_id.equals("0")) {
					msg1 += " towards PO ID " + crs.getString("voucher_id") +
							" dated " + strToShortDate(crs.getString("voucher_date"))
							+ ".\n\n\n";
				} else {
					msg1 += " towards " + config_customer_name + " ID " +
							crs.getString("voucher_customer_id") + " dated " +
							strToShortDate(crs.getString("voucher_date")) + ".\n\n\n";
				}

				// // if (vouchertype_id.equals("10")) {
				// vouchertype_name = crs.getString("vouchertype_name");
				// SOP("vouchertype_name==" + vouchertype_name);
				// // }
				// contact details

				String contact = "", comp_name = "";
				comp_name += crs.getString("comp_name");
				contact += crs.getString("branch_add") + ",";
				contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
				contact += crs.getString("state_name") + ".";
				if (!crs.getString("branch_phone1").equals("")) {
					contact += "\n" + crs.getString("branch_phone1");
				}
				if (!crs.getString("branch_mobile1").equals("")) {
					contact += "\n" + crs.getString("branch_mobile1");
				}
				// below company details
				String comp = "";
				comp = "For M/s. " + crs.getString("comp_name");
				comp += "\n" + "";
				comp += "\n" + "";
				comp += "\n" + "";
				comp += "\n" + crs.getString("jobtitle_desc");
				if (!crs.getString("emp_phone1").equals("")) {
					comp += "\n" + crs.getString("emp_phone1");
				}

				if (!crs.getString("emp_mobile1").equals("")) {
					comp += "\n" + crs.getString("emp_mobile1");
				}

				if (!crs.getString("emp_email1").equals("")) {
					comp += "\n" + crs.getString("emp_email1");
				}

				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("voucher_no", unescapehtml(crs.getString("voucher_no")));
				dataMap.put("voucher_amount", crs.getString("voucher_amount"));
				dataMap.put("voucher_customer_id", voucher_customer_id);
				dataMap.put("voucher_id", CNumeric(crs.getString("voucher_id")));
				dataMap.put("contact", unescapehtml(contact));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", crs.getString("customer_address"));
				dataMap.put("cust_city", crs.getString("cust_city"));
				dataMap.put("cust_state", crs.getString("cust_state"));
				dataMap.put("msg1", msg1);
				dataMap.put("vouchertype_name", crs.getString("vouchertype_name"));
				dataMap.put("comp", unescapehtml(comp));
				dataMap.put("comp_name", unescapehtml(comp_name));
				dataMap.put("emp_name", unescapehtml(emp_name));
				dataMap.put("voucher_narration", crs.getString("voucher_narration"));

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
