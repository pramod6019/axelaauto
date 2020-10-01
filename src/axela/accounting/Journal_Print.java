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

public class Journal_Print extends Connect {

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
			SOP("dopost");
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_journal_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
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
				report.dataList = JournalDetails();
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

	public List<Map> JournalDetails() throws IOException {

		HashMap dataMap;
		String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		String enquiry_income_id = "", testdrive_fb_taken = "";
		int enquiry_familymember_count = 0;
		try {
			StrSql = "SELECT"
					+ " voucher_id, voucher_date, voucher_amount,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " voucher_narration,"
					+ " vouchertype_label,"
					+ " vouchertrans_amount, vouchertrans_dc,"
					+ " (SELECT customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_id = vouchertrans_customer_id) AS customer_name,"
					+ " branch_add, branch_pin, branch_phone1, branch_mobile1, branch_email1, branch_invoice_name,"
					+ " COALESCE(branchcity.city_name,'') as city_name,"
					+ " COALESCE(branchstate.state_name,'')  as state_name,"
					// + " COALESCE( branchcountry.country_name ,'') as branch_country,"
					+ " comp_name,"
					+ " comp_logo"
					+ " company_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branchcity  ON  branchcity.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id, " + compdb(comp_id) + "axela_comp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id =" + comp_id
					+ " WHERE 1=1"
					+ " AND voucher_id =" + voucher_id
					// + " AND branch_company_id =" + comp_id
					+ " ORDER BY voucher_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("CreditenoteDetails====2==========" + StrSqlBreaker(StrSql));

			StrSql = "SELECT emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + emp_id
					+ " AND emp_active = '1'";
			emp_name = ExecuteQuery(StrSql);
			emp_name = emp_name.toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
			while (crs.next()) {
				dataMap = new HashMap();
				String contact = "", comp_name = "";
				double debit_tol = 0.0, credit_tol = 0.0;
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
				dataMap.put("comp_db", unescapehtml(compdb(comp_id)));
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("voucher_no", unescapehtml(crs.getString("voucher_no")));
				dataMap.put("voucher_id", unescapehtml(voucher_id));
				dataMap.put("voucher_amount", crs.getString("voucher_amount"));
				dataMap.put("contact", unescapehtml(contact));
				dataMap.put("voucher_narration", crs.getString("voucher_narration"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				if (crs.getString("vouchertrans_dc").equals("1")) {
					dataMap.put("debit_amt", crs.getDouble("vouchertrans_amount"));

				}
				if (crs.getString("vouchertrans_dc").equals("0")) {
					dataMap.put("credit_amt", crs.getDouble("vouchertrans_amount"));

				}
				dataMap.put("vouchertype_label", crs.getString("vouchertype_label"));
				dataMap.put("comp_name", unescapehtml(comp_name));
				dataMap.put("emp_name", unescapehtml(emp_name));

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
