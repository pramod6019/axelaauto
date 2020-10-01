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

public class Ticket_Maruti_SA_HomeVisit_Print extends Connect {

	public String ticket_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String emp_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", reportname = "";
	public String smart = "";
	public long quote_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			SOP("reportfrom==" + reportfrom);
			// reportfrom = "veh-quote-print";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			emp_id = CNumeric(GetSession("emp_id", request));
			SOP("comp_id====" + comp_id);
			if (!comp_id.equals("0")) {
				if (!GetSession("ticketstrsql", request).equals("")) {
					StrSearch = GetSession("ticketstrsql", request);
				}
				SOP("StrSearch==222==" + StrSearch);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				smart = PadQuotes(request.getParameter("smart"));
				if (reportfrom.equals("")) {
					reportfrom = PadQuotes(request.getParameter("reportfrom"));
				}
				reportname = PadQuotes(request.getParameter("reportname"));
				emp_id = CNumeric(GetSession("emp_id", request));
				if ("yes".equals(smart)) {
					if (!GetSession("ticketstrsql", request).equals("")) {
						StrSearch = GetSession("ticketstrsql", request);
					}
				} else if (!ticket_id.equals("0")) {
					StrSearch = " AND ticket_id = " + ticket_id;
				}
				// if (!StrSearch.equals("")) {
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/service/reports/" + reportfrom;
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = TicketDetails(comp_id, request, response, ticket_id, BranchAccess, ExeAccess);
				report.doPost(request, response);
			}
			// }
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
	public List<Map> TicketDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String ticket_id, String BranchAccess, String ExeAccess) throws IOException {

		HashMap dataMap;
		try {
			StrSql = "SELECT"
					+ " comp_logo,"
					+ " veh_reg_no, veh_chassis_no, veh_engine_no, preownedmodel_name, jc_no, jc_kms,"
					+ " customer_name, CONCAT(contact_address,', ',city_name,', ',state_name,', ',contact_pin) AS contact_address,"
					+ " contact_mobile1, contact_phone1, contact_email1,COALESCE(ticket_desc,'') AS ticket_desc,"
					+ " emp_name, jctype_name, jc_time_in"
					+ " FROM " + compdb(comp_id) + "axela_service_jc "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_jc_id = jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp"
					+ " WHERE ticket_id = " + ticket_id
					+ BranchAccess
					+ ExeAccess;
			SOP("StrSql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				dataMap.put("comp_logo", CompLogoPath() + crs.getString("comp_logo"));
				// SOP("Comp_logo==========" + CompLogoPath() + crs.getString("comp_logo"));
				dataMap.put("veh_reg_no", crs.getString("veh_reg_no"));
				dataMap.put("model_name", crs.getString("preownedmodel_name"));
				dataMap.put("veh_engine_no", crs.getString("veh_engine_no"));
				dataMap.put("veh_chassis_no", crs.getString("veh_chassis_no"));
				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_phone1", crs.getString("contact_phone1"));
				dataMap.put("contact_email1", crs.getString("contact_email1"));
				dataMap.put("ticket_desc", crs.getString("ticket_desc"));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("jc_time_in", strToLongDate(crs.getString("jc_time_in")));
				dataMap.put("jc_no_date", crs.getString("jc_no") + " & " + strToShortDate(crs.getString("jc_time_in")));
				dataMap.put("jctype_name", crs.getString("jctype_name"));
				dataMap.put("jc_kms", crs.getString("jc_kms") + " Kms");
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
