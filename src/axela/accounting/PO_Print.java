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

public class PO_Print extends Connect {

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
	public String enquiry_id = "0", total_amt = "0", brand_id = "0";
	public long voucher_amount = 0;
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
			CheckPerm(comp_id, "emp_acc_voucher_access," + " emp_acc_purchase_order_access", request, response);
			// SOP("reportform====" + reportfrom);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = unescapehtml(PadQuotes(request.getParameter("dr_report")));
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = PurchaseOrderDetail();
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
	public List<Map> PurchaseOrderDetail() throws IOException {

		HashMap dataMap;
		String custPh = "";
		String cusMblel = "", emailID = "";

		// String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		// String enquiry_income_id = "", testdrive_fb_taken = "";
		// int enquiry_familymember_count = 0;
		try {
			StrSql = "SELECT"
					+ " voucher_date, voucher_id, voucher_so_id, voucher_amount, voucher_customer_id,"
					+ " voucher_consignee_add, voucher_billing_add, voucher_narration,"
					+ " voucher_notes, voucher_entry_id, vouchertrans_cheque_bank,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_date, vouchertype_label,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount,"
					+ " vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
					+ " voucher_no, vouchertype_prefix, vouchertype_suffix,"
					+ " voucher_gatepass, voucher_lrno, voucher_cashdiscount,"
					+ " voucher_turnoverdisc, branch_add, branch_vat, branch_pin,"
					+ " branch_phone1, branch_phone2, branch_mobile1, branch_mobile2,"
					+ " branch_email1, voucher_amount, branch_email2, branch_invoice_name, comp_logo,"
					+ " comp_name, location_name, customer_name, customer_pan_no, customer_address, customer_code,"
					+ " customer_pin, customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_phone1, customer_phone2, customer_email1, customer_email2,"
					+ " CONCAT( contact_fname, ' ', contact_lname ) AS contact_name,"
					+ " title_desc, COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms,"
					+ " jobtitle_desc, comp_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE"
					+ " voucher_id = " + voucher_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " GROUP BY"
					+ " voucher_id"
					+ " ORDER BY"
					+ " voucher_id DESC";
			// SOP("StrSql=========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_name = crs.getString("emp_name").toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
				// SOP("emp_name=====" + emp_name);
				voucher_amount = (long) crs.getDouble("voucher_amount");
				// SOP("voucher_amount====" + voucher_amount);
				total_amt = IndianCurrencyFormatToWord(voucher_amount).toUpperCase();

				// SOP("total_amt===" + total_amt.toUpperCase());
				dataMap = new HashMap();
				String contact = "", comp_name = "";
				comp_name += crs.getString("comp_name");
				contact += crs.getString("branch_add") + ",";
				contact += "\n" + crs.getString("city_name") + " - ";
				contact += crs.getString("branch_pin") + ",\n";
				contact += crs.getString("state_name") + ",\n";
				// + rs.getString("branch_country") + ".";
				if (!crs.getString("branch_phone1").equals("")) {
					contact += "\n" + crs.getString("branch_phone1");
					if (!crs.getString("branch_phone2").equals("")) {
						contact += ", " + crs.getString("branch_phone2");
					}
				}
				if (!crs.getString("branch_mobile1").equals("")) {
					contact += "\n" + crs.getString("branch_mobile1");
					if (!crs.getString("branch_mobile2").equals("")) {
						contact += ", " + crs.getString("branch_mobile2");
					}
				}
				if (!crs.getString("branch_vat").equals("")) {
					dataMap.put("branch_vat", crs.getString("branch_vat"));
				}
				if (vouchertype_id.equals("114") || vouchertype_id.equals("102")) {

					dataMap.put("voucher_custref_no", unescapehtml(crs.getString("voucher_custref_no")));
				}
				// if (!crs.getString("customer_code").equals("")) {
				//
				// dataMap.put("customer_code", crs.getString("customer_code"));
				// }
				if (!crs.getString("customer_name").equals("")) {

					dataMap.put("customer_name", crs.getString("customer_name"));
				}
				if (!crs.getString("voucher_billing_add").equals("")) {

					dataMap.put("voucher_billing_add", unescapehtml(crs.getString("voucher_billing_add")));
				}
				// Mobile
				if (!crs.getString("customer_mobile1").equals("")) {
					cusMblel = "Mobile No.: "
							+ crs.getString("customer_mobile1");

					if (!crs.getString("customer_mobile2").equals("")) {
						cusMblel += ", " + crs.getString("customer_mobile2");

					}
					dataMap.put("cusMblel", cusMblel);
				}
				// Phone
				if (!crs.getString("customer_phone1").equals("")) {
					custPh = "Phone No.: "
							+ crs.getString("customer_phone1");
					if (!crs.getString("customer_phone2").equals("")) {
						custPh += ", " + crs.getString("customer_mobile2");
					}
					dataMap.put("custPh", custPh);
				}
				// Email
				if (!crs.getString("customer_email1").equals("")) {
					emailID = "Email ID: "
							+ crs.getString("customer_email1");
					if (!crs.getString("customer_email2").equals("")) {
						emailID += ", " + crs.getString("customer_email2");
					}
					dataMap.put("emailID", emailID);
				}
				// ccc dataMap.put("voucher_so_id", crs.getString("voucher_so_id"));
				// SOP("voucher_so_id=====" + crs.getString("voucher_so_id"));
				// ccccdataMap.put("customer_pan_no", crs.getString("customer_pan_no"));
				dataMap.put("comp_ID", Integer.parseInt(comp_id));
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("voucher_ref_no", unescapehtml(crs.getString("voucher_ref_no")));
				dataMap.put("voucher_no", unescapehtml(crs.getString("voucher_no")));
				dataMap.put("voucher_id", unescapehtml(voucher_id));
				// dataMap.put("voucher_amount", crs.getString("voucher_amount"));
				dataMap.put("contact", unescapehtml(contact));
				// dataMap.put("voucher_narration", crs.getString("voucher_narration"));
				dataMap.put("vouchertype_label", crs.getString("vouchertype_label"));
				dataMap.put("comp_name", unescapehtml(comp_name));
				dataMap.put("emp_name", unescapehtml(emp_name));
				dataMap.put("vouchertype_prefix", crs.getString("vouchertype_prefix"));
				dataMap.put("vouchertype_suffix", crs.getString("vouchertype_suffix"));
				dataMap.put("location_name", crs.getString("location_name"));
				dataMap.put("voucher_amount", crs.getString("voucher_amount"));
				dataMap.put("total_amt", total_amt);
				// SOP("location_name===" + crs.getString("location_name"));

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
