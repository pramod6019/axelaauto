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

public class Veh_Quote_Print_New extends Connect {

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
	static DecimalFormat deci = new DecimalFormat("0.00");

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
				// SOP("reportfrom===" + reportfrom);
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
		// SOP("coming..." + BranchLogoPath(comp_id));
		try {
			StrSql = "SELECT quote_date, quote_id, CONCAT('QT', branch_code, quote_no) AS quote_no, quote_discamt,"
					+ " comp_name, branch_name, branch_add, branch_pin, branch_code, branch_invoice_name,"
					+ " COALESCE(branch_city.city_name,'') AS branch_city_name , branch_quote_terms, branch_logo,"
					+ " branch_state.state_name, branch_phone1, branch_mobile1, branch_email1, branch_logo,"
					+ " quote_desc, quote_exprice, quoteitem_total, quote_netamt, quote_terms,"
					+ " quote_grandtotal, COALESCE(item_name,'') AS item_name, quoteitem_qty, quoteitem_price,"
					+ " exe.emp_name as emp_name, COALESCE(exe.emp_phone1,'') as emp_phone1,"
					+ " COALESCE(exe.emp_mobile1,'') as emp_mobile1, COALESCE(exe.emp_email1,'') as emp_email1, enquiry_id,"
					+ " CONCAT('ENQ', branch_code, enquiry_no) AS enquiry_no, jobtitle_id, comp_logo,\n"
					+ " COALESCE ( ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'TCS'"
					+ " GROUP BY quoteitem_option_group, quoteitem_quote_id ), 0.00 ) AS quoteitem_tcs_price,"
					+ " COALESCE ( ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'Road Tax'"
					+ " GROUP BY quoteitem_option_group, quoteitem_quote_id ), 0.00 ) AS quoteitem_roadtax_price,"
					+ " COALESCE ( ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'Insurance'"
					+ " GROUP BY quoteitem_option_group, quoteitem_quote_id ), 0.00 ) AS quoteitem_insur_price,"
					+ " COALESCE ( ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'Essential Kit'"
					+ " GROUP BY quoteitem_option_group, quoteitem_quote_id ), 0.00 ) AS quoteitem_esskit_price,"
					+ " COALESCE ( ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE 1 = 1"
					+ " AND quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'Handling Charges'"
					+ " GROUP BY quoteitem_option_group, quoteitem_quote_id ), 0.00 ) AS quoteitem_handling_price,"
					+ " COALESCE(vehstock_comm_no,'') AS vehstock_comm_no, fueltype_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
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
					+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = exe.emp_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp teamlead on teamlead.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock  ON vehstock_id = quote_vehstock_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE quote_id = " + quote_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "exe.emp_id") + ""
					+ " GROUP BY quote_id"
					+ " ORDER BY quote_id DESC";
			// SOP("StrSql=====new====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap.put("reportname", reportname);
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("comp_logo", CompLogoPath() + crs.getString("comp_logo"));
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
				dataMap.put("quote_id", crs.getString("quote_id"));
				dataMap.put("quote_date", strToShortDate(crs.getString("quote_date")));
				dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				dataMap.put("branch_name", crs.getString("branch_name"));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_add", crs.getString("branch_add") + ", "
						+ crs.getString("branch_city_name") + "-" + crs.getString("branch_pin"));
				dataMap.put("branch_quote_terms", unescapehtml(crs.getString("branch_quote_terms")));
				// dataMap.put("branch_city_name", crs.getString("branch_city_name"));
				// dataMap.put("branch_pin", crs.getString("branch_pin"));
				dataMap.put("branch_phone1", crs.getString("branch_phone1"));
				dataMap.put("branch_mobile1", crs.getString("branch_mobile1"));
				dataMap.put("branch_email1", crs.getString("branch_email1"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("emp_mobile1", crs.getString("emp_mobile1"));

				quote_amount = (long) crs.getDouble("quote_netamt");
				total_amt = IndianCurrencyFormatToWord(quote_amount).toUpperCase();

				dataMap.put("vehstock_comm_no", crs.getString("vehstock_comm_no"));
				dataMap.put("fueltype_name", crs.getString("fueltype_name"));

				dataMap.put("quoteitem_tcs_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_tcs_price")) + ""));
				dataMap.put("quoteitem_roadtax_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_roadtax_price")) + ""));
				dataMap.put("quoteitem_esskit_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_esskit_price")) + ""));
				dataMap.put("quoteitem_insur_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_insur_price")) + ""));
				dataMap.put("quoteitem_handling_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_handling_price")) + ""));
				dataMap.put("quote_exprice", IndDecimalFormat(deci.format(crs.getDouble("quote_exprice"))));
				dataMap.put("quote_discamt", IndDecimalFormat(deci.format(crs.getDouble("quote_discamt")) + ""));
				dataMap.put("quote_grandtotal", IndDecimalFormat(deci.format(crs.getDouble("quote_grandtotal")) + ""));
				dataMap.put("total_amt", total_amt);
				dataMap.put("main_item_name", crs.getString("item_name"));
				dataMap.put("main_quoteitem_qty", crs.getString("quoteitem_qty"));
				dataMap.put("main_quoteitem_price", IndDecimalFormat(deci.format(crs.getDouble("quoteitem_total"))));
				String itemname = ExecuteQuery("SELECT COALESCE(item_name,'') AS item_name FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
						+ " WHERE 1 = 1"
						+ " AND quoteitem_quote_id =" + crs.getString("quote_id"));
				if (itemname.equals("")) {
					dataMap.put("itemname", "");
				}
				// SOPInfo("reportname===" + reportname);
				dataList.add(dataMap);
			}
			// SOP("dataMap=====new====" + dataMap.toString());
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
				reportname = "Proforma Invoice";

				report.reportfrom = contextpath + "/sales/reports/" + "veh-quote-print-new.jasper";
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
