package axela.service;

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

public class Jobcard_SalesInvoice_Print_Yamaha extends Connect {

	public String voucher_id = "0";
	public String jc_id = "0";
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
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", jobcard_authorize = "";
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
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				SOP("jc_id===" + jc_id);
				jobcard_authorize = CNumeric(ExecuteQuery("SELECT jc_auth"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE 1=1"
						+ " AND jc_id =" + jc_id));

				if (jobcard_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}

				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/service/reports/" + reportfrom;
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
		String formdetails = "";
		try {
			StrSql = "SELECT"
					+ " voucher_date, "
					+ " voucher_id, voucher_so_id, voucher_jc_id, voucher_amount,"
					+ " voucher_no,"
					+ " COALESCE(CONCAT('JC', branch_code, jc_no), '') AS jc_no,"
					+ " COALESCE(jctype_name, '') AS jctype_name,"
					+ " COALESCE(jc_reg_no, '') AS jc_reg_no,"
					+ " COALESCE(veh_chassis_no,'') AS veh_chassis_no,"
					+ " COALESCE(veh_engine_no,'') AS veh_engine_no,"
					+ " model_name,"
					+ " voucher_consignee_add, voucher_billing_add, voucher_narration,"
					+ " voucher_notes, voucher_entry_id, vouchertrans_cheque_bank,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_date,"
					+ " voucher_ref_no, voucher_custref_no,"
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
					+ " jobtitle_desc, comp_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = voucher_jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE"
					+ " voucher_jc_id = " + jc_id
					+ " GROUP BY"
					+ " voucher_jc_id"
					+ " ORDER BY"
					+ " voucher_jc_id DESC";
			// SOPInfo(" =====yamaha=====print====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap = new HashMap();
				voucher_amount = (long) crs.getDouble("voucher_amount");
				total_amt = IndianCurrencyFormatToWord(voucher_amount).toUpperCase();
				// + rs.getString("branch_country") + ".";
				String customer_address = "", branch_address = "", branch_email = "", branch_phone = "";

				if (!crs.getString("branch_add").equals("")) {
					branch_address = crs.getString("branch_add") + ", " + crs.getString("city_name");
				}
				if (!crs.getString("branch_pin").equals("")) {
					branch_address += (crs.getString("branch_pin"));
				}

				if (!crs.getString("branch_mobile1").equals("")) {
					branch_phone += "\n" + crs.getString("branch_mobile1");
					if (!crs.getString("branch_mobile2").equals("")) {
						branch_phone += ", " + crs.getString("branch_mobile2");
					}
				}
				if (!crs.getString("branch_email1").equals("")) {
					branch_email = crs.getString("branch_email1");
				} else if (!crs.getString("branch_email2").equals("")) {
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
				// Mobile
				if (!crs.getString("customer_mobile1").equals("")) {
					cusMblel = "Mobile No.: "
							+ crs.getString("customer_mobile1");

					if (!crs.getString("customer_mobile2").equals("")) {
						cusMblel += ", " + crs.getString("customer_mobile2");

					}
					dataMap.put("cusMblel", cusMblel);
				}

				dataMap.put("branch_address", branch_address);
				dataMap.put("branch_email", branch_email);
				dataMap.put("branch_phone", branch_phone);
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_cst", crs.getString("branch_cst"));
				dataMap.put("customer_address", customer_address);
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("jc_id", unescapehtml(jc_id));
				dataMap.put("voucher_jc_id", unescapehtml(jc_id));
				dataMap.put("voucher_no", crs.getString("voucher_no"));
				dataMap.put("jc_no", crs.getString("jc_no"));
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("comp_ID", Integer.parseInt(comp_id));
				dataMap.put("jctype_name", crs.getString("jctype_name"));
				dataMap.put("jc_reg_no", crs.getString("jc_reg_no"));
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("total_amt", crs.getDouble("voucher_amount"));
				dataMap.put("voucher_amount", toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount"))) + " Only");
				dataMap.put("veh_chassis_no", crs.getString("veh_chassis_no"));
				dataMap.put("veh_engine_no", crs.getString("veh_engine_no"));
				dataMap.put("emp_name", crs.getString("emp_name"));
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
