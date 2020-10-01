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

public class Ticket_General_Print extends Connect {

	public String ticket_id = "0";
	public String comp_id = "0";
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
	public String emp_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", reportname = "";
	public long quote_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
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
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				reportname = PadQuotes(request.getParameter("reportname"));
				reportfrom = PadQuotes(request.getParameter("reportfrom"));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/service/reports/" + reportfrom;
				// SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				// SOPInfo("reportfrom===12==" + reportfrom);
				report.dataList = TicketDetails(comp_id, request, response, ticket_id, BranchAccess, ExeAccess);
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
	public List<Map> TicketDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String ticket_id, String BranchAccess, String ExeAccess) throws IOException {

		HashMap dataMap = new HashMap();
		String branch_logo = "";
		// SOP("coming..." + BranchLogoPath(comp_id));
		// String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		try {
			StrSql = " SELECT"
					+ " branch_logo,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)) AS contact_name,"
					+ " COALESCE(CONCAT(contact_address, ', ', city_name, ', ', state_name, ',',contact_pin)) AS contact_address,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " preownedmodel_name,"
					+ " veh_reg_no,"
					+ " jc_ro_no,"
					+ "	jc_time_in,"
					+ " ticket_jc_id,"
					+ "	ticket_entry_date,"
					+ "	COALESCE(adv.emp_name,'') AS serviceadv,"
					+ "	COALESCE(tech.emp_name,'') AS technician,"
					+ "	COALESCE(jcpsfconcern_desc,'') AS jcpsfconcern,"
					+ "	COALESCE(jc_cust_voice,'') AS custvoice,"
					+ " comp_logo"
					+ " FROM " + compdb(comp_id) + "  axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp adv ON adv.emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id"

					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp"
					+ " WHERE ticket_id = " + ticket_id
					+ " GROUP BY jc_id"
					+ BranchAccess
					+ ExeAccess;
			// + " GROUP BY ticket_id "
			// + " ORDER BY ticket_id DESC";
			SOP("StrSql=========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap.put("ticket_id", Integer.parseInt(ticket_id));
				dataMap.put("contact_name", crs.getString("contact_name"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("contact_mobile2", crs.getString("contact_mobile2"));
				dataMap.put("model_name", crs.getString("preownedmodel_name"));
				dataMap.put("veh_reg_no", crs.getString("veh_reg_no"));
				dataMap.put("jc_ro_no", crs.getString("jc_ro_no"));
				dataMap.put("jc_time_in", strToLongDate(crs.getString("jc_time_in")));
				dataMap.put("ticket_entry_date", strToShortDate(crs.getString("ticket_entry_date")));
				dataMap.put("serviceadv", crs.getString("serviceadv"));
				dataMap.put("technician", crs.getString("technician"));
				dataMap.put("jcpsfconcern", crs.getString("jcpsfconcern"));
				dataMap.put("custvoice", crs.getString("custvoice"));

				if (!crs.getString("branch_logo").equals("")) {
					branch_logo = BranchLogoPath(comp_id) + crs.getString("branch_logo");
				}
				else if (!crs.getString("comp_logo").equals("")) {
					branch_logo = CompLogoPath() + crs.getString("comp_logo");
				}

				dataMap.put("branch_logo", branch_logo);
				dataMap.put("comp_id", Integer.parseInt(comp_id));
				dataMap.put("jc_id", Integer.parseInt(crs.getString("ticket_jc_id")));
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
