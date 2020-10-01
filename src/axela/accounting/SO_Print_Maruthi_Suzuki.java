package axela.accounting;

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

public class SO_Print_Maruthi_Suzuki extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String tax_check = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	public String emp_id = "0";
	public String enquiry_id = "0", total_amt = "", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "";
	public String msg = "";
	public String msg1 = "";
	public long voucher_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucher_authorize = CNumeric(ExecuteQuery("SELECT voucher_authorize"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE 1=1"
						+ " AND voucher_id =" + voucher_id));

				if (voucher_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}

				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				// SOPInfo("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = SalesOrderDetails();
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
	public List<Map> SalesOrderDetails() throws IOException {

		HashMap dataMap;
		String custPh = "";
		String cusMblel = "", emailID = "";
		try {
			StrSql = "SELECT"
					// + " voucher_date, "
					+ " DATE_FORMAT(voucher_date,'%d-%b-%Y') as voucher_date,"
					+ " voucher_id, voucher_so_id, voucher_jc_id, voucher_amount,"
					+ " voucher_no,"
					+ " COALESCE((SELECT CONCAT(so_prefix, so_no) AS so_no"
					+ " FROM " + compdb(comp_id) + "axela_sales_so where so_id = voucher_so_id limit 1),'') AS  so_no,"
					+ " SUM( vouchertrans_qty ) AS vouchertrans_qty,"
					+ " @discount := COALESCE(( SELECT disc.vouchertrans_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans disc"
					+ " WHERE disc.vouchertrans_option_id = vouchertrans_rowcount"
					+ " AND disc.vouchertrans_discount = 1 "
					+ " AND disc.vouchertrans_voucher_id = " + voucher_id
					+ " LIMIT 1 ), 0 ) AS discount,"
					+ " @vouchervalue := (voucher_amount - @discount) AS vouchervalue,"
					+ " @taxrate := COALESCE(( SELECT customer_rate"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans taxtable"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = taxtable.vouchertrans_tax_id"
					+ " WHERE taxtable.vouchertrans_option_id = vouchertrans_rowcount "
					+ " AND taxtable.vouchertrans_tax = 1 "
					+ " AND taxtable.vouchertrans_voucher_id = " + voucher_id
					+ " LIMIT 1 ), 0 ) AS tax,"
					+ " @taxble_amt:=(vouchertrans_price * @taxrate)/100 as taxable_amt,"
					+ " voucher_customer_id," + " COALESCE(fincomp_name,'') AS fincomp_name,"
					+ " COALESCE(veh_chassis_no,'') AS veh_chassis_no,"
					+ " COALESCE(veh_engine_no,'') AS veh_engine_no,"
					+ " COALESCE(veh_modelyear,'') AS veh_modelyear,"
					+ " voucher_consignee_add, voucher_billing_add, voucher_narration,"
					+ " voucher_notes, voucher_entry_id, vouchertrans_cheque_bank,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_date, vouchertype_label,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount,"
					+ " vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
					+ " CONCAT(vouchertype_prefix, vouchertype_suffix, voucher_no ) AS voucher_no,"
					+ " COALESCE((SELECT CONCAT(so_prefix, so_no) AS so_no"
					+ " FROM " + compdb(comp_id) + "axela_sales_so where so_id = voucher_so_id limit 1),'') AS  so_no,"
					+ " COALESCE((SELECT jc_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc where jc_id = voucher_jc_id limit 1),'') AS  jc_no,"
					+ " voucher_gatepass, voucher_lrno, voucher_cashdiscount,"
					+ " voucher_turnoverdisc, branch_add, branch_vat, branch_pin, branch_cst, branch_invoice_name, branch_vat, branch_pan,"
					+ " branch_phone1,voucher_consignee_add, branch_phone2, branch_mobile1, branch_mobile2,"
					+ " branch_email1, branch_email2, branch_invoice_name, comp_logo,"
					+ " comp_name, customer_id, customer_name, customer_pan_no, customer_address, customer_code, customer_landmark,"
					+ " customer_pin, customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_phone1, customer_phone2, customer_email1, customer_email2,"
					+ " CONCAT( contact_fname, ' ', contact_lname ) AS contact_name,"
					+ " title_desc, COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (location_name, '') AS location_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms,"
					+ " jobtitle_desc, comp_name, option_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_so_id = so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
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
			// SOPInfo(" =====ford=====print====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap = new HashMap();
				voucher_amount = (long) crs.getDouble("voucher_amount");
				total_amt = IndianCurrencyFormatToWord(voucher_amount).toUpperCase();
				// + rs.getString("branch_country") + ".";
				String customer_address = "", branch_address = "", branch_email = "";

				if (!crs.getString("branch_add").equals("")) {
					branch_address = crs.getString("branch_add") + ", " + crs.getString("city_name");
				}
				if (!crs.getString("branch_pin").equals("")) {
					branch_address += (crs.getString("branch_pin"));
				}
				SOP("branch_address-====" + branch_address);
				if (!crs.getString("branch_email1").equals("")) {
					branch_email = crs.getString("branch_email1");
				} else {
					branch_email = crs.getString("branch_email2");
				}
				if (!crs.getString("customer_address").equals("")) {
					customer_address = crs.getString("customer_address");
					if (!crs.getString("cust_city").equals("")) {
						customer_address = customer_address + ", " + crs.getString("cust_city");
					}
					customer_address = customer_address + " - " + crs.getString("customer_pin");
					if (!crs.getString("cust_state").equals("")) {
						customer_address = customer_address + ", " + crs.getString("cust_state") + ".";
					}
					if (!crs.getString("customer_landmark").equals("")) {
						customer_address = customer_address + "<br>Landmark: " + crs.getString("customer_landmark");
					}
				}
				dataMap.put("branch_address", branch_address);
				dataMap.put("branch_email", branch_email);
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_cst", crs.getString("branch_cst"));
				dataMap.put("branch_vat", crs.getString("branch_vat"));
				dataMap.put("branch_pan", crs.getString("branch_pan"));
				dataMap.put("fincomp_name", crs.getString("fincomp_name"));
				dataMap.put("customer_address", customer_address);
				dataMap.put("customer_id", crs.getString("customer_id"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("voucher_id", unescapehtml(voucher_id));
				dataMap.put("voucher_no", crs.getString("voucher_no"));
				dataMap.put("so_no", crs.getString("so_no"));
				dataMap.put("voucher_date", crs.getString("voucher_date"));
				dataMap.put("comp_ID", Integer.parseInt(comp_id));
				dataMap.put("vouchertrans_qty", crs.getDouble("vouchertrans_qty"));
				dataMap.put("vouchervalue", crs.getDouble("vouchervalue"));
				dataMap.put("taxable_amt", crs.getDouble("taxable_amt"));
				dataMap.put("total_amt", crs.getDouble("voucher_amount"));
				dataMap.put("voucher_amount", toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount"))) + " Only");
				dataMap.put("veh_chassis_no", crs.getString("veh_chassis_no"));
				dataMap.put("veh_engine_no", crs.getString("veh_engine_no"));
				dataMap.put("veh_modelyear", crs.getString("veh_modelyear"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("option_name", crs.getString("option_name"));
				dataMap.put("date", strToLongDate(ToLongDate(kknow())));
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
