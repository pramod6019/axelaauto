package axela.service;

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

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class JobCard_Print extends Connect {

	public String comp_id = "0";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String jc_id = "0";
	public String jc_netamt = "";
	public String jc_fuel_guage = "";
	public double jc_grandtotal = 0.0;
	public String total_disc = "";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String emp_id = "0";
	public String enquiry_id = "0", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", emp_name1 = "", format = "pdf";
	public int fguage = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	PdfWriter writer;
	Image img;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// SOP(System.getProperty("java.class.path"));
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				BranchAccess = CheckNull(GetSession("BranchAccess", request));
				ExeAccess = CheckNull(GetSession("ExeAccess", request));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				if (ReturnPerm(comp_id, "emp_service_jobcard_access", request).equals("1")) {
					JobCardDetails();
				}
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/service/reports/" + reportfrom;
				// SOP("reportfrom11======" + report.reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = JobCardDetails();
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
	public List<Map> JobCardDetails() throws IOException {

		HashMap dataMap;
		String enquiry_buyertype_id = "", enquiry_monthkms_id = "", enquiry_priorityenquiry_id = "";
		String enquiry_income_id = "", testdrive_fb_taken = "";
		int enquiry_familymember_count = 0;
		try {
			StrSql = "SELECT jc_time_in, jc_id, CONCAT('JC', branch_code, jc_no) AS jc_no, jc_discamt,"
					+ " jc_netamt, comp_name, branch_add, branch_pin, branch_phone1, branch_invoice_name,"
					+ " branch_mobile1, branch_email1, branch_logo, comp_name, jc_notes, jc_bill_address,"
					+ " jc_bill_city, jc_bill_pin, jc_bill_state, jc_del_address, jc_del_city, jc_del_pin,"
					+ " jc_del_state, jc_terms, jc_grandtotal, jc_cust_voice, jc_customer_id, emp_name,"
					+ " jc_fuel_guage, jc_veh_id,"
					+ " COALESCE(veh_reg_no,'') AS veh_reg_no,"
					+ " COALESCE(veh_chassis_no,'') AS veh_chassis_no,"
					+ " COALESCE(veh_kms,'0') AS veh_kms,"
					+ " customer_name,"
					+ " customer_address, customer_pin,"
					+ " COALESCE(branch_city.city_name, '') AS city_name, "
					+ " COALESCE(jobtitle_desc,'') AS jobtitle_desc,"
					+ " COALESCE(branch_state.state_name, '')  AS state_name,"
					+ " COALESCE(customer_city.city_name, '') AS acc_city,"
					+ " COALESCE(customer_state.state_name, '') AS acc_state, emp_phone1, emp_mobile1, emp_email1"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON  branch_city.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city customer_city ON customer_city.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE jc_id = " + jc_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY jc_id"
					+ " ORDER BY jc_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
//			SOP("TrackingDetails====2==========" + StrSqlBreaker(StrSql));
			// Document document = new Document();
			// if ("file".equals("file")) {
			// File f = new File(CachePath(comp_id));
			// if (!f.exists()) {
			// f.mkdirs();
			// }
			// writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "JobCard_" + jc_id + ".pdf"));
			// }
			//
			// document.open();
			// PdfContentByte cb = writer.getDirectContent();
			// Barcode128 code128 = new Barcode128();
			// code128.setCodeType(Barcode128.CODE128);
			// code128.setCode(jc_id);
			// img = code128.createImageWithBarcode(cb, null, null);
			while (crs.next()) {
				dataMap = new HashMap();
				jc_netamt = crs.getString("jc_netamt");
				jc_fuel_guage = crs.getString("jc_fuel_guage");
				fguage = Integer.parseInt(jc_fuel_guage);
				jc_grandtotal = crs.getDouble("jc_grandtotal");
				total_disc = crs.getString("jc_discamt");
				emp_name1 = crs.getString("emp_name").toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
				emp_name = crs.getString("emp_name");
				String amt_word = toTitleCase(NumberToWordFormat((int) crs.getDouble("jc_grandtotal")));
				String contact = "", comp_name = "";
				comp_name = "M/s. " + unescapehtml(crs.getString("branch_invoice_name"));
				// comp_name += "M/s. " + crs.getString("branch_invoice_name");
				contact += crs.getString("branch_add") + ",";
				contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
				contact += crs.getString("state_name") + ".";
				if (!crs.getString("branch_phone1").equals("")) {
					contact += "\n" + crs.getString("branch_phone1");
				}

				if (!crs.getString("branch_mobile1").equals("")) {
					contact += "\n" + crs.getString("branch_mobile1");
				}
				String comp = "";
				comp = "For M/s. " + crs.getString("branch_invoice_name");
				comp += "\n";
				comp += "\n";
				comp += "\n";
				comp += "\n" + crs.getString("emp_name");
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

				// customer details
				dataMap.put("jc_time_in", strToShortDate(crs.getString("jc_time_in")));
				dataMap.put("jc_id", crs.getString("jc_id"));
				dataMap.put("veh_reg_no", crs.getString("veh_reg_no"));
				dataMap.put("veh_chassis_no", crs.getString("veh_chassis_no"));
				dataMap.put("jc_veh_id", crs.getString("jc_veh_id"));
				dataMap.put("comp_name", comp_name);
				dataMap.put("contact", unescapehtml(contact));
				dataMap.put("veh_kms", crs.getString("veh_kms"));
				dataMap.put("jc_fuel_guage", crs.getString("jc_fuel_guage"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_address", crs.getString("customer_address"));
				dataMap.put("acc_city", crs.getString("acc_city"));
				dataMap.put("customer_pin", crs.getString("customer_pin"));
				dataMap.put("acc_state", crs.getString("acc_state"));
				dataMap.put("jc_customer_id", crs.getString("jc_customer_id"));
				dataMap.put("jc_grandtotal", crs.getDouble("jc_grandtotal"));
				dataMap.put("jc_terms", crs.getString("jc_terms"));
				dataMap.put("jc_grandtotal", crs.getDouble("jc_grandtotal"));
				dataMap.put("jobtitle_desc", crs.getString("jobtitle_desc"));
				dataMap.put("emp_email1", crs.getString("emp_email1"));
				dataMap.put("emp_name1", emp_name1);
				dataMap.put("amt_word", amt_word);
				dataMap.put("emp_name", emp_name);
				dataMap.put("comp", comp);
				// dataMap.put("img", new Chunk(img, 0, -20));

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
