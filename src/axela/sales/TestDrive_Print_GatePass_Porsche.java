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

public class TestDrive_Print_GatePass_Porsche extends Connect {
	
	public String brand_id = "0";
	public String StrSql = "", msg = "", reportfrom = "", format = "pdf";
	public String StrHTML = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String invoice_id = "0";
	public String branch_city_id = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String StrSearch = "";
	public String testdrive_id = "";
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
			testdrive_id = CNumeric(PadQuotes(request
					.getParameter("testdrive_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			reportfrom = "testdrive-print-gatepass-porsche";
			// SOP("reportfrom------------" + reportfrom);
			if (ReturnPerm(comp_id, "emp_testdrive_access", request).equals("1")) {
				if (testdrive_out_time.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id)
							+ "axela_sales_testdrive" + " SET"
							+ " testdrive_out_time = '" + ToLongDate(kknow())
							+ "'," + " testdrive_mileage_entry_id = '" + emp_id
							+ "', " + " testdrive_mileage_entry_date = '"
							+ ToLongDate(kknow()) + "' "
							+ " WHERE testdrive_id = " + testdrive_id + " ";
					updateQuery(StrSql);
				}
				// crs.getString("testdrive_in_time").equals(""))
				StrSearch = " AND testdrive_id = " + testdrive_id + "";
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				report.parameters = BuildParameters();
				report.dataList = TestDriveDetails();
				report.doPost(request, response);
			} else {
				response.sendRedirect(AccessDenied());
			}
			
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
	
	public List<Map> TestDriveDetails() throws IOException {
		HashMap dataMap;
		String branch_logo = "../../media/axelaauto_" + comp_id + "/branchlogo/";
		
		try {
			StrSql = "SELECT "
					+ " testdrive_id,"
					+ " customer_name, "
					+ " CONCAT(title_desc,' ',contact_fname,' ', contact_lname) AS contactname,"
					+ " COALESCE(CONCAT(customer_address,', ',city_name,' - ', customer_pin,'.'),'') AS customer_address,"
					+ " customer_mobile1,"
					+ " customer_phone1,"
					+ " model_name,"
					+ " testdriveveh_regno,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_from,'%d/%m/%Y'), '') AS testdrive_date,"
					+ " location_name,"
					+ " branch_invoice_name,branch_logo,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_from,'%d/%m/%Y %H:%m'), '') AS testdrive_time_from,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_to,'%d/%m/%Y %H:%m'), '') AS testdrive_time_to,"
					+ " COALESCE(DATE_FORMAT(testdrive_out_time,'%d/%m/%Y %H:%m'), '') AS testdrive_out_time,"
					+ " testdrive_license_no,"
					+ " testdrive_license_issued_by,"
					+ " COALESCE(DATE_FORMAT(testdrive_license_valid,'%d/%m/%Y'), '') AS testdrive_license_valid,"
					+ " COALESCE(driver_name,'') AS driver,"
					+ " COALESCE(manager.emp_name,'') AS manager,"
					+ " testdrive_out_kms,"
					+ " testdrive_in_kms"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id   "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id   "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id =  testdrive_testdriveveh_id   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = testdrive_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS manager ON manager.emp_id= team_emp_id  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver ON driver_id= testdrive_out_driver_id"
					+ " WHERE testdrive_out_time!='' "
					+ StrSearch
					+ BranchAccess
					+ " GROUP BY testdrive_id"
					+ " ORDER BY branch_name, customer_name";
			
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOPInfo("GatePassDetails=====" + StrSql);
			while (crs.next()) {
				dataMap = new HashMap();
				// enquiry_details
				
				dataMap.put("testdrive_id", crs.getString("testdrive_id"));
				dataMap.put("customer_name", unescapehtml(crs.getString("customer_name")));
				dataMap.put("contactname", unescapehtml(crs.getString("contactname")));
				dataMap.put("customer_address", crs.getString("customer_address"));
				dataMap.put("customer_mobile1", unescapehtml(crs.getString("customer_mobile1")));
				dataMap.put("customer_phone1", unescapehtml(crs.getString("customer_phone1")));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("veh_regno", unescapehtml(crs.getString("testdriveveh_regno")));
				dataMap.put("testdrive_date", PadQuotes(crs.getString("testdrive_date")));
				dataMap.put("location_name", crs.getString("location_name"));
				dataMap.put("branch_invoice_name", crs.getString("branch_invoice_name"));
				dataMap.put("testdrive_time_from", unescapehtml(crs.getString("testdrive_time_from")));
				dataMap.put("testdrive_time_to", unescapehtml(crs.getString("testdrive_time_to")));
				dataMap.put("testdrive_out_time", unescapehtml(crs.getString("testdrive_out_time")));
				dataMap.put("testdrive_license_no", unescapehtml(crs.getString("testdrive_license_no")));
				dataMap.put("testdrive_license_issued_by", unescapehtml(crs.getString("testdrive_license_issued_by")));
				dataMap.put("testdrive_license_valid", unescapehtml(crs.getString("testdrive_license_valid")));
				dataMap.put("driver", unescapehtml(crs.getString("driver")));
				dataMap.put("testdrive_out_kms", unescapehtml(crs.getString("testdrive_out_kms")));
				dataMap.put("testdrive_in_kms", unescapehtml(crs.getString("testdrive_in_kms")));
				if (crs.getString("branch_logo").equals("")) {
					dataMap.put("branch_logo", "");
				} else {
					dataMap.put("branch_logo", BranchLogoPath(comp_id) + crs.getString("branch_logo"));
				}
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
