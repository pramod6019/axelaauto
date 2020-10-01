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

public class OneTriumph_SO_Print extends Connect {

	public String voucher_id = "0";
	public long quote_amount = 0;
	public String quote_id = "0";

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
	public String enquiry_id = "0", total_amt = "", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "", reportname = "", total_item_space = "";
	public String msg = "";
	public long voucher_amount = 0;
	public int total_item = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			// CheckPerm(comp_id, "emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));

				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));

				reportname = "INVOICE";

				voucher_authorize = CNumeric(ExecuteQuery("SELECT voucher_authorize"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_vouchertype_id = 6"
						+ " AND voucher_id =" + voucher_id));
				quote_id = CNumeric(PadQuotes(ExecuteQuery("SELECT quote_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
						+ " WHERE voucher_id = " + voucher_id)));
				total_item = Integer.parseInt(CNumeric(PadQuotes(ExecuteQuery("SELECT count(1) as total_item"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.quoteitem_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
						+ " WHERE 1=1"
						+ " AND quoteitem_rowcount = 0"
						+ " AND invitem.quoteitem_quote_id = " + quote_id
						+ " ORDER BY"
						+ " quoteitem_option_group_tax,"
						+ " quoteitem_id"))));

				while (total_item-- >= 0) {
					total_item_space += "<br>";
				}
				total_item_space += "<br>";
				if (voucher_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;

				// SOP("reportfrom===" + reportfrom);
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

		HashMap dataMap = new HashMap();
		HashMap dataMap1;
		String custPh = "";
		String cusMblel = "", emailID = "";
		// SOP("coming..." + BranchLogoPath(comp_id));
		String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		try {
			StrSql = "SELECT quote_date, quote_id, CONCAT('QT', branch_code, quote_no) AS quote_no,"
					+ " quote_discamt, comp_name, branch_add, branch_pin, branch_code, COALESCE(branch_city.city_name,'') AS branch_city_name , branch_quote_terms, branch_logo,"
					+ " branch_state.state_name, branch_phone1, branch_mobile1, branch_email1, branch_logo,"
					+ " quote_fin_option1, quote_fin_loan1, quote_fin_tenure1, quote_fin_adv_emi1,"
					+ " quote_fin_emi1, quote_fin_fee1, quote_fin_downpayment1, quote_fin_baloonemi1,"
					+ " quote_fin_baloonemi2, quote_fin_baloonemi3, quote_fin_option2, quote_fin_loan2,"
					+ " quote_fin_tenure2, quote_fin_adv_emi2, quote_fin_emi2, quote_fin_fee2,"
					+ " quote_fin_downpayment2, quote_fin_option3, quote_fin_loan3, quote_fin_tenure3,"
					+ " quote_fin_adv_emi3, quote_fin_emi3, quote_fin_fee3, quote_fin_downpayment3,"
					+ " comp_name, quote_desc, quote_exprice, quote_netamt, customer_address, customer_mobile1, jobtitle_desc,"
					+ " COALESCE(customer_city.city_name,'') AS acc_city, customer_pin, COALESCE(customer_state.state_name,'') AS acc_state,"
					+ " quote_ship_address, quote_ship_city, quote_ship_pin, quote_ship_state, quote_terms,"
					+ " quote_grandtotal, branch_invoice_name, customer_id, customer_name, exe.emp_name as emp_name, COALESCE(item_name,'') AS item_name, quoteitem_qty, quoteitem_price,"
					+ " COALESCE(exe.emp_phone1,'') as emp_phone1, COALESCE(exe.emp_mobile1,'') as emp_mobile1, COALESCE(exe.emp_email1,'') as emp_email1, enquiry_id, CONCAT('ENQ', branch_code, enquiry_no) AS enquiry_no,"
					+ " jobtitle_id,\n"
					+ " COALESCE(teamlead.emp_name,'') as tl_emp_name, COALESCE(teamlead.emp_mobile1,'') as tl_emp_mobile1, COALESCE(teamlead.emp_email1,'') as tl_emp_email1\n"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON branch_city.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city customer_city ON customer_city.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = quote_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = exe.emp_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp teamlead on teamlead.emp_id = team_emp_id, "
					// + compdb(comp_id) + "axela_comp\n"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE quote_id = " + quote_id + BranchAccess + ExeAccess.replace("emp_id", "exe.emp_id") + ""
					+ " GROUP BY quote_id"
					+ " ORDER BY quote_id DESC";
			// SOP("StrSql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				quote_amount = (long) crs.getDouble("quote_netamt");
				total_amt = IndianCurrencyFormatToWord(quote_amount).toUpperCase();
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", crs.getString("customer_address") + ", "
						+ crs.getString("acc_city") + "-"
						+ crs.getString("customer_pin") + ", "
						+ crs.getString("acc_state"));
				dataMap.put("customer_mobile1", crs.getString("customer_mobile1"));
				dataMap.put("quote_date", strToShortDate(crs.getString("quote_date")));
				dataMap.put("voucher_date", strToShortDate(PadQuotes(ExecuteQuery("SELECT voucher_date"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id))));
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("quote_id", crs.getString("quote_id"));
				dataMap.put("voucher_id", voucher_id);
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
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
				dataMap.put("quote_exprice", IndFormat(crs.getString("quote_exprice")));
				dataMap.put("quote_discamt", IndFormat(crs.getString("quote_discamt")));
				dataMap.put("quote_grandtotal", IndFormat(crs.getString("quote_grandtotal")));
				dataMap.put("total_amt", total_amt);
				dataMap.put("main_item_name", crs.getString("item_name"));
				dataMap.put("main_quoteitem_qty", crs.getString("quoteitem_qty"));
				dataMap.put("main_quoteitem_price", IndFormat(crs.getString("quoteitem_price")));
				dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				dataMap.put("branch_quote_terms", unescapehtml(crs.getString("branch_quote_terms")));
				SOP("total_item_space====" + total_item_space);
				dataMap.put("total_item_space", total_item_space);
				dataMap.put("reportname", reportname);
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
