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

public class Veh_Salesorder_PDI_Print extends Connect {

	public String so_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String emp_id = "0";
	public String enquiry_id = "0", total_amt = "", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", reportname = "";
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	static DecimalFormat deci = new DecimalFormat("#.###");

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
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				reportname = PadQuotes(request.getParameter("reportname"));
				reportfrom = PadQuotes(request.getParameter("reportfrom"));
				emp_id = CNumeric(GetSession("emp_id", request));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				// SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				// SOPInfo("so_id===12==" + so_id);
				report.dataList = PdiDetails(comp_id, request, response, so_id, BranchAccess, ExeAccess);
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

	public List<Map> PdiDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String so_id, String BranchAccess, String ExeAccess) throws IOException {

		HashMap dataMap = new HashMap();
		// SOP("coming..." + BranchLogoPath(comp_id));
		try {
			StrSql = "SELECT so_branch_id, branch_name, so_date, so_emp_id, emp_name,"
					+ " COALESCE(item_name, '') AS item_name, COALESCE(model_name, '') AS model_name,"
					+ " COALESCE(option_name, '') AS option_name, "
					+ " COALESCE (IF(so_offer_loyalty_bonus != 0, 'YES', ''), '') AS so_loyalty,"
					+ " COALESCE (so_offer_loyalty_bonus, '') AS so_offer_loyalty_bonus,"
					+ " COALESCE (IF(so_exchange = 1, 'YES', ''), '') AS so_exchange,"
					+ " COALESCE (so_offer_exchange_bonus, '') AS so_offer_exchange_bonus,"
					+ " COALESCE (IF(so_offer_corporate != 0, 'YES', ''), '') AS so_corporate,"
					+ " COALESCE (so_offer_corporate, '') AS so_offer_corporate,"
					+ " COALESCE (IF(so_din_del_location = 1, 'Customer Place', IF(so_din_del_location = 2, 'Showroom', '')), '') AS so_din_del_location,"
					+ " so_grandtotal, so_exchange_amount, vehstock_chassis_no, "

					+ " so_exprice, so_insur_amount, so_mga_amount, "
					+ " so_ew_amount, so_booking_amount, so_finance_amt,"

					+ " customer_name, customer_pin, customer_phone1, customer_mobile1, customer_email1,"
					+ " so_promise_date, COALESCE(bank_name, '') AS bank_name, "
					+ " COALESCE(so_pan, '') AS so_pan, COALESCE(fincomp_name, '') AS fincomp_name,"
					+ " COALESCE (( SELECT SUM(voucher_amount) FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1 = 1"
					+ " AND voucher_vouchertype_id = 9"
					+ " AND voucher_so_id = " + so_id
					+ " GROUP BY voucher_so_id ), 0.00) AS additional_payment,"
					+ " COALESCE ((SELECT	COUNT(item_name) FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE	soitem_so_id = " + so_id
					+ " AND soitem_option_group_id = 1),0) AS additional_offer"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = so_finstatus_bank_id"
					+ " WHERE 1 = 1"
					+ " AND so_id = " + so_id;
			SOP("StrSql=====new====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap.put("reportname", reportname);
				dataMap.put("comp_id", Integer.parseInt(comp_id));

				dataMap.put("so_id", so_id);
				dataMap.put("branch_name", crs.getString("branch_name"));
				dataMap.put("so_date", strToShortDate(crs.getString("so_date")));
				dataMap.put("bank_name", crs.getString("bank_name"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("option_name", unescapehtml(crs.getString("option_name")));

				dataMap.put("so_grandtotal", IndFormat(deci.parse(crs.getString("so_grandtotal")) + ""));
				dataMap.put("so_exchange_amount", IndFormat(deci.parse(crs.getString("so_exchange_amount")) + ""));
				dataMap.put("so_din_del_location", crs.getString("so_din_del_location"));
				dataMap.put("vin_no", crs.getString("vehstock_chassis_no"));

				dataMap.put("so_exprice", IndFormat(deci.parse(crs.getString("so_exprice")) + ""));
				dataMap.put("so_insur_amount", IndFormat(deci.parse(crs.getString("so_insur_amount")) + ""));
				dataMap.put("so_mga_amount", IndFormat(deci.parse(crs.getString("so_mga_amount")) + ""));
				dataMap.put("so_ew_amount", IndFormat(deci.parse(crs.getString("so_ew_amount")) + ""));
				dataMap.put("so_booking_amount", IndFormat(deci.parse(crs.getString("so_booking_amount")) + ""));
				dataMap.put("so_finance_amt", IndFormat(deci.parse(crs.getString("so_finance_amt")) + ""));

				dataMap.put("so_loyalty", crs.getString("so_loyalty"));
				dataMap.put("so_offer_loyalty_bonus", IndFormat(deci.parse(crs.getString("so_offer_loyalty_bonus")) + ""));
				dataMap.put("so_exchange", crs.getString("so_exchange"));
				dataMap.put("so_offer_exchange_bonus", IndFormat(deci.parse(crs.getString("so_offer_exchange_bonus")) + ""));

				dataMap.put("so_corporate", crs.getString("so_corporate"));
				dataMap.put("so_offer_corporate", IndFormat(deci.parse(crs.getString("so_offer_corporate")) + ""));

				dataMap.put("additional_payment", IndFormat(deci.format(crs.getDouble("additional_payment"))));

				dataMap.put("additional_offer", crs.getString("additional_offer"));
				SOP("additional_offer====" + crs.getString("additional_offer"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_pin", crs.getString("customer_pin"));
				dataMap.put("customer_phone1", crs.getString("customer_phone1"));
				dataMap.put("customer_mobile1", crs.getString("customer_mobile1"));
				dataMap.put("customer_email1", crs.getString("customer_email1"));
				dataMap.put("so_promise_date", strToLongDate(crs.getString("so_promise_date")));
				dataMap.put("so_pan", crs.getString("so_pan"));
				dataMap.put("fincomp_name", crs.getString("fincomp_name"));

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
		// SOP("contextpath===" + contextpath);
		if (!quote_id.equals("")) {
			BranchAccess = "";
			Map parameters = new HashMap();
			try {
				JasperReport report = new JasperReport();
				conn = connectDB();
				parameters.put("REPORT_CONNECTION", conn);
				reportname = "DELIVERY CUM PDI & ACCESSORY FITMENT REQUISITION";

				report.reportfrom = contextpath + "/sales/reports/" + "veh-salesorder-pdi-print.jasper";
				// SOP("so_id ===" + so_id);
				// SOP("comp_id ===" + comp_id);
				// SOP("report.reportfrom===" + report.reportfrom);
				// String report = "veh-quote-print";
				dataList = PdiDetails(comp_id, request, response, so_id, BranchAccess, ExeAccess);
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
