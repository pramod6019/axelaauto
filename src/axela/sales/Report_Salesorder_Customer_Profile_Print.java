package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JRException;
import cloudify.connect.Connect;

public class Report_Salesorder_Customer_Profile_Print extends Connect {

	public String brand_id = "0";
	public String StrSql = "", msg = "", reportfrom = "", format = "pdf";
	public String StrHTML = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String invoice_id = "0";
	public String branch_city_id = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String StrSearch = "";
	public String so_id = "";
	public String dr_report = "";
	// private int Rectangle;
	public String testdrive_out_time = "";
	public String emp_id;
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String attachment = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, JRException {

		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		emp_id = CNumeric(GetSession("emp_id", request));

		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			reportfrom = "report-salesorder-customer-profile";
			// SOP("reportfrom------------" + reportfrom);
			// crs.getString("testdrive_in_time").equals(""))
			JasperReport report = new JasperReport();
			report.reportfrom = "/sales/reports/" + reportfrom;
			report.parameters = BuildParameters();
			report.dataList = CustomerProfile();
			report.doPost(request, response);

		}
	}

	public Map BuildParameters() {
		Connection conn = connectDB();
		try {
			parameters.put("REPORT_CONNECTION", conn);
		}

		catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			}
		}

		return parameters;
	}

	public List<Map> CustomerProfile() throws IOException {
		HashMap dataMap;
		String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";

		try {
			StrSql = "SELECT"
					+ " COALESCE(customer_name,'') AS customer_name,"
					+ " COALESCE(customer_mobile1,'') AS customer_mobile1,"
					+ " COALESCE(customer_phone1,'') AS customer_phone1,"
					+ " COALESCE(customer_email1,'') AS customer_email1,"
					+ " COALESCE(customer_email2,'') AS customer_email2,"
					+ " COALESCE(customer_address,'') AS customer_address,"
					+ " COALESCE(voucher_billing_add,'') AS voucher_billing_add,"
					+ " COALESCE(branch_name, '') AS branch_name,"
					+ " CONCAT(model_name,' ',item_name) AS ownedcar"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_so_id = so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE so_id = " + so_id;

			// SOP("StrSql=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap = new HashMap();
				// enquiry_details

				dataMap.put("customer_name", crs.getString("customer_name"));
				dataMap.put("customer_mobile1", crs.getString("customer_mobile1"));
				dataMap.put("customer_phone1", crs.getString("customer_phone1"));
				dataMap.put("customer_email1", crs.getString("customer_email1"));
				dataMap.put("customer_email2", crs.getString("customer_email2"));
				dataMap.put("customer_address", crs.getString("customer_address"));
				dataMap.put("branch_name", crs.getString("branch_name"));
				dataMap.put("voucher_billing_add", crs.getString("voucher_billing_add"));
				dataMap.put("ownedcar", unescapehtml(crs.getString("ownedcar")));
				// if (crs.getString("branch_logo").equals("")) {
				// dataMap.put("branch_logo", "");
				// } else {
				// dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				// }
				dataList.add(dataMap);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return dataList;
	}
}
