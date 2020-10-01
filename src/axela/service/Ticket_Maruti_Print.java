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

public class Ticket_Maruti_Print extends Connect {

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

				// when directly say print from ticket list
				if (StrSearch.equals("")) {
					StrSearch = " AND ticket_ticketstatus_id IN (1,2,6,5,7)";
				}

				if (!StrSearch.equals("")) {
					conn = connectDB();
					JasperReport report = new JasperReport();
					report.reportfrom = "/service/reports/" + reportfrom;
					report.parameters.put("REPORT_CONNECTION", conn);
					report.dataList = TicketDetails(comp_id, request, response, ticket_id, BranchAccess, ExeAccess);
					report.doPost(request, response);
				}
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
	public List<Map> TicketDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String ticket_id, String BranchAccess, String ExeAccess) throws IOException {

		HashMap dataMap;
		String branch_logo = "";
		int sl_no = 0;
		// SOP("coming..." + BranchLogoPath(comp_id));
		// String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		try {
			StrSql = " SELECT"
					+ " ticket_id,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)) AS contact_name,"
					+ " COALESCE(CONCAT(contact_address, ', ', city_name, ', ', state_name, ',',contact_pin)) AS contact_address,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " preownedmodel_name,"
					+ " veh_reg_no,"
					+ " jc_kms,"
					+ "	jc_time_in,"
					+ " jctype_name,"
					+ " ticket_jc_id,"
					+ "	ticket_entry_date, jcpsf_followup_time,"
					+ "	COALESCE(adv.emp_name,'') AS serviceadv,"
					+ "	COALESCE(tech.emp_name,'') AS technician,"
					+ "	COALESCE(jcpsfconcern_desc,'') AS jcpsfconcern,"
					+ "	COALESCE(jc_cust_voice,'') AS custvoice,"
					+ " COALESCE(jccat_name,'') AS ticketgroup, COALESCE(jcpsf_desc,'') AS jcpsf_desc"
					+ " FROM " + compdb(comp_id) + "  axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp adv ON adv.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id"

					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = psfdays_jccat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					// + " WHERE ticket_id IN (13766, 13791 , 21322, 21319) "
					+ " WHERE 1=1"
					+ " " + StrSearch
					+ BranchAccess
					+ ExeAccess
					+ " GROUP BY ticket_id"
					+ " ORDER BY ticket_id";
			SOPInfo("StrSql=====ticket maruti print====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				dataMap.put("sl_no", ++sl_no);
				dataMap.put("ticket_id", Integer.parseInt(crs.getString("ticket_id")));
				dataMap.put("contact_name", crs.getString("contact_name"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_mobile2", crs.getString("contact_mobile2"));
				dataMap.put("model_name", crs.getString("preownedmodel_name"));
				dataMap.put("veh_reg_no", crs.getString("veh_reg_no"));
				dataMap.put("jc_kms", crs.getString("jc_kms") + " Kms");
				dataMap.put("jc_time_in", strToLongDate(crs.getString("jc_time_in")));
				dataMap.put("jctype_name", crs.getString("jctype_name"));
				dataMap.put("ticket_entry_date", strToLongDate(crs.getString("ticket_entry_date")));
				dataMap.put("jcpsf_followup_time", strToLongDate(crs.getString("jcpsf_followup_time")));
				dataMap.put("serviceadv", crs.getString("serviceadv"));
				dataMap.put("technician", crs.getString("technician"));
				dataMap.put("jcpsfconcern", crs.getString("jcpsfconcern"));
				dataMap.put("custvoice", crs.getString("custvoice"));
				dataMap.put("jc_id", Integer.parseInt(crs.getString("ticket_jc_id")));
				dataMap.put("ticketgroup", crs.getString("ticketgroup"));
				dataMap.put("jcpsf_desc", crs.getString("jcpsf_desc"));
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
