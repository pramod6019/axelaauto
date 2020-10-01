package axela.sales;

//aJIt 11th January, 2013
//SATISH 23-FEB-2013

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Form_21_Print extends Connect {

	public String so_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String BranchAccess;
	public String ExeAccess;
	public String reportfrom = "", reportname = "";
	Connection conn = null;
	public List dataList = new ArrayList();

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				reportfrom = PadQuotes(request.getParameter("reportfrom"));
				reportname = PadQuotes(request.getParameter("reportname"));

				// SOP("reportfrom==" + reportfrom);
				// SOP("reportname==" + reportname);
				// if (ReturnPerm(comp_id, "emp_sales_order_access", request).equals("1")) {
				// Form_21(request, response, so_id, BranchAccess, ExeAccess, "pdf");
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = SOdetails(comp_id, request, response, BranchAccess, ExeAccess);
				report.doPost(request, response);
				// SOP("==" + report.reportfrom);

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			conn.setAutoCommit(true);
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
	}

	public List<Map> SOdetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String BranchAccess, String ExeAccess) {
		HashMap dataMap;
		try {
			StrSql = " SELECT"
					+ " so_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " CONCAT(contact_address, ', ', city_name, ', ', state_name, '- ',contact_pin) AS contact_address,"
					+ " contact_mobile1,"
					+ " CONCAT('Yamaha ',model_name) AS model_name,"
					+ " so_delivered_date,"
					+ " vehstock_chassis_no,"
					+ " vehstock_engine_no"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE so_id = " + so_id;

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				dataMap = new HashMap();
				dataMap.put("so_id", Integer.parseInt(crs.getString("so_id")));
				dataMap.put("contact_name", crs.getString("contact_name"));
				dataMap.put("contact_address", crs.getString("contact_address"));
				dataMap.put("contact_mobile1", crs.getString("contact_mobile1"));
				dataMap.put("model_name", crs.getString("model_name"));
				dataMap.put("vehstock_chassis_no", crs.getString("vehstock_chassis_no"));
				dataMap.put("vehstock_engine_no", crs.getString("vehstock_engine_no"));
				// SOP("12345=====" + strToShortDate(crs.getString("so_delivered_date")));
				dataMap.put("so_delivered_date", strToShortDate(crs.getString("so_delivered_date")));
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
