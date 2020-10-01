package axela.sales;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import cloudify.connect.Connect;

public class Veh_Quote_Print extends Connect {

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
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "", reportname = "";
	public String msg = "";
	public String msg1 = "";
	public long quote_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// reportfrom = "veh-quote-print";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			emp_id = CNumeric(GetSession("emp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				reportname = PadQuotes(request.getParameter("reportname"));
				reportfrom = PadQuotes(request.getParameter("reportfrom"));
				emp_id = CNumeric(GetSession("emp_id", request));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				// / SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				// SOPInfo("reportfrom===12==" + reportfrom);
				report.dataList = QuoteDetails(comp_id, request, response, quote_id, BranchAccess, ExeAccess);
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
	public List<Map> QuoteDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String quote_id, String BranchAccess, String ExeAccess) throws IOException {

		HashMap dataMap = new HashMap();
		String beforetax = "", aftertax = "";
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
					+ " WHERE quote_id = " + quote_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "exe.emp_id") + ""
					+ " GROUP BY quote_id"
					+ " ORDER BY quote_id DESC";
			SOP("StrSql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				quote_amount = (long) crs.getDouble("quote_grandtotal");
				total_amt = IndianCurrencyFormatToWord(quote_amount).toUpperCase();
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", unescapehtml(PadQuotes(crs.getString("customer_address"))) + ", "
						+ crs.getString("acc_city") + "-"
						+ crs.getString("customer_pin") + ", "
						+ crs.getString("acc_state"));
				dataMap.put("customer_mobile1", crs.getString("customer_mobile1"));
				dataMap.put("quote_date", strToShortDate(crs.getString("quote_date")));
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("quote_id", crs.getString("quote_id"));
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
				dataMap.put("customer_id", crs.getString("customer_id"));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_add", unescapehtml(PadQuotes(crs.getString("branch_add"))) + ", "
						+ crs.getString("branch_city_name") + "-" + crs.getString("branch_pin"));
				// dataMap.put("branch_city_name", crs.getString("branch_city_name"));
				// dataMap.put("branch_pin", crs.getString("branch_pin"));
				dataMap.put("branch_mobile1", crs.getString("branch_mobile1"));
				dataMap.put("branch_email1", crs.getString("branch_email1"));
				dataMap.put("team_lead_name", crs.getString("tl_emp_name"));
				dataMap.put("team_lead_mob", crs.getString("tl_emp_mobile1"));
				dataMap.put("team_lead_email", crs.getString("tl_emp_email1"));
				dataMap.put("jobtitle_desc", crs.getString("jobtitle_desc"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("emp_mobile1", crs.getString("emp_mobile1"));
				dataMap.put("quote_exprice", IndFormat(df.format(Double.parseDouble((crs.getString("quote_exprice"))))));
				dataMap.put("quote_discamt", IndFormat(df.format(Double.parseDouble((crs.getString("quote_discamt"))))));
				dataMap.put("quote_grandtotal", IndFormat(df.format(Double.parseDouble((crs.getString("quote_grandtotal"))))));
				dataMap.put("total_amt", total_amt);
				dataMap.put("main_item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("main_quoteitem_qty", crs.getString("quoteitem_qty"));
				dataMap.put("main_quoteitem_price", IndFormat(df.format(Double.parseDouble((crs.getString("quoteitem_price"))))));

				StrSql = "SELECT"
						+ " COALESCE(invitem.quoteitem_qty,'') AS beforetax"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_name = quoteitem_option_group"
						+ " WHERE 1=1"
						+ " AND quoteitem_rowcount = 0"
						+ " AND group_aftertax = 0"
						+ " AND invitem.quoteitem_quote_id =" + crs.getString("quote_id");

				beforetax = ExecuteQuery(StrSql);
				dataMap.put("beforetax", beforetax);
				StrSql = "SELECT"
						+ " COALESCE(invitem.quoteitem_qty,'') AS beforetax"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_name = quoteitem_option_group"
						+ " WHERE 1=1"
						+ " AND quoteitem_rowcount = 0"
						+ " AND group_aftertax = 1"
						+ " AND invitem.quoteitem_quote_id =" + crs.getString("quote_id");
				aftertax = ExecuteQuery(StrSql);
				dataMap.put("aftertax", aftertax);
				if (crs.getString("branch_logo").equals("")) {
					dataMap.put("branch_logo", "");
				} else {
					dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				}
				dataMap.put("branch_quote_terms", unescapehtml(crs.getString("branch_quote_terms")));
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
	public String GeneratePDFQuote(String quote_id, HttpServletRequest request, HttpServletResponse response) {
		String attachment = CachePath(comp_id) + "Quote-" + quote_id + ".pdf";
		ServletContext servletcontext = request.getServletContext();
		String contextpath = servletcontext.getRealPath(File.separator);
		SOP("contextpath===" + contextpath);
		if (!quote_id.equals("")) {
			BranchAccess = "";
			Map parameters = new HashMap();
			try {
				JasperReport report = new JasperReport();
				conn = connectDB();
				parameters.put("REPORT_CONNECTION", conn);
				reportname = "PROFORMA INVOICE";

				if (brand_id.equals("151")) {
					report.reportfrom = contextpath + "/sales/reports/" + "veh-quote-print-one-triumph.jasper";
				}
				else {
					report.reportfrom = contextpath + "/sales/reports/" + "veh-quote-print.jasper";
				}
				// SOP("brand_id ===" + brand_id);
				// SOPInfo("report.reportfrom===" + report.reportfrom);
				// String report = "veh-quote-print";
				dataList = QuoteDetails(comp_id, request, response, quote_id, BranchAccess, ExeAccess);
				JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
				File reportFile = new File(report.reportfrom);
				JasperPrint jasperprint;
				jasperprint = JasperFillManager.fillReport(report.reportfrom, parameters, jrBeanCollectionDataSource);
				JasperExportManager.exportReportToPdfFile(jasperprint, attachment);
				dataList.clear();
				if (conn != null) {
					conn.close();
				}
				// SOP("coming...to generate pdf");
			} catch (Exception ex) {
				SOPError("Axelaauto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return attachment;
	}
}
