package axela.sales;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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

public class Veh_Quote_Print_Joshi extends Connect {

	public String quote_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	// Option 1
	public String quote_fin_option1 = "";
	public String quote_fin_loan1 = "";
	public String quote_fin_tenure1 = "";
	public String quote_fin_adv_emi1 = "";
	public String quote_fin_emi1 = "";
	public String quote_fin_baloonemi1 = "";
	public String quote_fin_fee1 = "";
	public String quote_fin_downpayment1 = "";

	// Option 2
	public String quote_fin_option2 = "";
	public String quote_fin_loan2 = "";
	public String quote_fin_tenure2 = "";
	public String quote_fin_adv_emi2 = "";
	public String quote_fin_emi2 = "";
	public String quote_fin_baloonemi2 = "";
	public String quote_fin_fee2 = "";
	public String quote_fin_downpayment2 = "";

	// Option 3
	public String quote_fin_option3 = "";
	public String quote_fin_loan3 = "";
	public String quote_fin_tenure3 = "";
	public String quote_fin_adv_emi3 = "";
	public String quote_fin_emi3 = "";
	public String quote_fin_baloonemi3 = "";
	public String quote_fin_fee3 = "";
	public String quote_fin_downpayment3 = "";

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
		HashMap dataMap1;
		String custPh = "";
		String cusMblel = "", emailID = "";
		// SOP("coming..." + BranchLogoPath(comp_id));
		String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		try {
			StrSql = " SELECT"
					+ " quote_id,"
					+ " quote_date,"
					+ " quote_exprice,"
					+ " quote_netamt,"
					+ " COALESCE (quote_fin_option1, '') AS quote_fin_option1,"
					+ " COALESCE (quote_fin_loan1, '') AS quote_fin_loan1,"
					+ "	COALESCE (quote_fin_tenure1, '') AS quote_fin_tenure1,"
					+ "	COALESCE (quote_fin_adv_emi1, '') AS quote_fin_adv_emi1,"
					+ "	COALESCE (quote_fin_emi1, '') AS quote_fin_emi1,"
					+ "	COALESCE (quote_fin_baloonemi1, '') AS quote_fin_baloonemi1,"
					+ "	COALESCE (quote_fin_fee1, '') AS quote_fin_fee1,"
					+ "	COALESCE (quote_fin_downpayment1, '') AS quote_fin_downpayment1,"
					+ "	COALESCE (quote_fin_option2, '') AS quote_fin_option2,"
					+ "	COALESCE (quote_fin_loan2, '') AS quote_fin_loan2,"
					+ "	COALESCE (quote_fin_tenure2, '') AS quote_fin_tenure2,"
					+ "	COALESCE (quote_fin_adv_emi2, '') AS quote_fin_adv_emi2,"
					+ "	COALESCE (quote_fin_emi2, '') AS quote_fin_emi2,"
					+ "	COALESCE (quote_fin_baloonemi2, '') AS quote_fin_baloonemi2,"
					+ "	COALESCE (quote_fin_fee2, '') AS quote_fin_fee2,"
					+ "	COALESCE (quote_fin_downpayment2, '') AS quote_fin_downpayment2,"
					+ "	COALESCE (quote_fin_option3, '') AS quote_fin_option3,"
					+ "	COALESCE (quote_fin_loan3, '') AS quote_fin_loan3,"
					+ "	COALESCE (quote_fin_tenure3, '') AS quote_fin_tenure3,"
					+ "	COALESCE (quote_fin_adv_emi3, '') AS quote_fin_adv_emi3,"
					+ "	COALESCE (quote_fin_emi3, '') AS quote_fin_emi3,"
					+ "	COALESCE (quote_fin_baloonemi3, '') AS quote_fin_baloonemi3,"
					+ "	COALESCE (quote_fin_fee3, '') AS quote_fin_fee3,"
					+ "	COALESCE (quote_fin_downpayment3, '') AS quote_fin_downpayment3,"
					+ " branch_invoice_name,"
					+ " branch_add,"
					+ " branch_pin,"
					+ " COALESCE (branch_city.city_name, '') AS branch_city_name,"
					+ " branch_quote_terms,"
					+ " branch_logo,"
					+ " COALESCE (branch_state.state_name, '') AS branch_state_name,"
					// +" branch_phone1, "
					// +" branch_mobile1, "
					// +" branch_email1, "
					+ " COALESCE (item_name,'') AS main_item_name,"
					// + " brand_name,"
					+ " customer_name"
					+ " FROM"
					+ " " + compdb(comp_id) + "  axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON branch_city.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					// + " INNER JOIN axelaauto.axela_brand axela_brand ON axela_brand.brand_id = branch_brand_id"
					+ " WHERE quote_id = " + quote_id + BranchAccess + ExeAccess.replace("emp_id", "exe.emp_id") + ""
					+ " GROUP BY quote_id "
					+ " ORDER BY quote_id DESC";
			SOP("StrSql=========Joshi" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StrSql = "SELECT COALESCE (item_name, '') AS item_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item "
					+ " ON item.item_id = invitem.quoteitem_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom "
					+ " ON uom_id = item.item_uom_id AND quoteitem_option_group = 'Optional Packages' "
					+ " WHERE 1 = 1 "
					+ " AND quoteitem_rowcount = 0 "
					+ " AND invitem.quoteitem_quote_id =" + quote_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "exe.emp_id") + "";
			// SOP("optional_item_package====" + StrSql);
			String optional_item_package = ExecuteQuery(StrSql);

			while (crs.next()) {
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("quote_id", Integer.parseInt(crs.getString("quote_id")));
				dataMap.put("quote_date", strToShortDate(crs.getString("quote_date")));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("branch_add", crs.getString("branch_add") + ", "
						+ crs.getString("branch_city_name") + ", " + crs.getString("branch_state_name") + "-" + crs.getString("branch_pin"));
				dataMap.put("main_item_name", crs.getString("main_item_name"));
				dataMap.put("quote_exprice", IndFormat(crs.getString("quote_exprice")));
				dataMap.put("quote_netamt", IndFormat(crs.getString("quote_netamt")));
				// dataMap.put("branch_mobile1", crs.getString("branch_mobile1"));
				// dataMap.put("branch_email1", crs.getString("branch_email1"));

				dataMap.put("optional_item_package", optional_item_package);
				dataMap.put("quote_fin_option1", crs.getString("quote_fin_option1"));
				dataMap.put("quote_fin_loan1", crs.getString("quote_fin_loan1"));
				dataMap.put("quote_fin_tenure1", crs.getString("quote_fin_tenure1"));
				dataMap.put("quote_fin_adv_emi1", crs.getString("quote_fin_adv_emi1"));
				dataMap.put("quote_fin_emi1", crs.getString("quote_fin_emi1"));
				dataMap.put("quote_fin_baloonemi1", crs.getString("quote_fin_baloonemi1"));
				dataMap.put("quote_fin_fee1", crs.getString("quote_fin_fee1"));
				dataMap.put("quote_fin_downpayment1", crs.getString("quote_fin_downpayment1"));

				dataMap.put("quote_fin_option2", crs.getString("quote_fin_option2"));
				dataMap.put("quote_fin_loan2", crs.getString("quote_fin_loan2"));
				dataMap.put("quote_fin_tenure2", crs.getString("quote_fin_tenure2"));
				dataMap.put("quote_fin_adv_emi2", crs.getString("quote_fin_adv_emi2"));
				dataMap.put("quote_fin_emi2", crs.getString("quote_fin_emi2"));
				dataMap.put("quote_fin_baloonemi2", crs.getString("quote_fin_baloonemi2"));
				dataMap.put("quote_fin_fee2", crs.getString("quote_fin_fee2"));
				dataMap.put("quote_fin_downpayment2", crs.getString("quote_fin_downpayment2"));

				dataMap.put("quote_fin_option3", crs.getString("quote_fin_option3"));
				dataMap.put("quote_fin_loan3", crs.getString("quote_fin_loan3"));
				dataMap.put("quote_fin_tenure3", crs.getString("quote_fin_tenure3"));
				dataMap.put("quote_fin_adv_emi3", crs.getString("quote_fin_adv_emi3"));
				dataMap.put("quote_fin_emi3", crs.getString("quote_fin_emi3"));
				dataMap.put("quote_fin_baloonemi3", crs.getString("quote_fin_baloonemi3"));
				dataMap.put("quote_fin_fee3", crs.getString("quote_fin_fee3"));
				dataMap.put("quote_fin_downpayment3", crs.getString("quote_fin_downpayment3"));

				if (!quote_fin_option1.equals("")) {

				}

				// branch_logo = branch_logo + crs.getString("branch_logo");
				// InputStream input = new FileInputStream(branch_logo);
				dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				// dataMap.put("brand_name", crs.getString("brand_name"));
				dataMap.put("branch_quote_terms", unescapehtml(crs.getString("branch_quote_terms")));
				dataMap.put("reportname", reportname);
				// SOPInfo("reportname===" + reportname);
				dataList.add(dataMap);
			}

			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return dataList;
	}

	public String GeneratePDFQuote(String quote_id, HttpServletRequest request, HttpServletResponse response) throws SQLException {
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
				reportname = "PRO-FORMA INVOICE";

				report.reportfrom = contextpath + "/sales/reports/" + "veh-quote-print-joshi.jasper";

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
				// if (conn != null) {
				// conn.close();
				// }
				// SOP("coming...to generate pdf,,,joshi");
			} catch (Exception ex) {
				SOPError("Axelaauto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				conn.setAutoCommit(true);
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			}
		}
		return attachment;
	}
}
