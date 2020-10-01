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

public class TestDrive_Print_GatePass1 extends Connect {
	
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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JRException {
		
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		emp_id = CNumeric(GetSession("emp_id", request));
		
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			
			if (brand_id.equals("6")) {
				reportfrom = "testdrive-print-gatepass-hyundai";
			} else if (brand_id.equals("7")) {
				reportfrom = "testdrive-print-gatepass-ford";
			} else if (brand_id.equals("9")) {
				reportfrom = "testdrive-print-gatepass-honda";
			} else if (brand_id.equals("51")) {
				reportfrom = "testdrive-print-gatepass-volvo";
			}
			else if (brand_id.equals("153")) {
				reportfrom = "testdrive-print-gatepass-harley";
			}
			else {
				reportfrom = "testdrive-print-gatepass-maruti";
			}
			// SOP("reportfrom------------" + reportfrom);
			if (ReturnPerm(comp_id, "emp_testdrive_access", request).equals("1")) {
				if (testdrive_out_time.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
							+ " SET"
							+ " testdrive_out_time = '" + ToLongDate(kknow()) + "',"
							+ " testdrive_mileage_entry_id = '" + emp_id + "', "
							+ " testdrive_mileage_entry_date = '" + ToLongDate(kknow()) + "' "
							+ " WHERE testdrive_id = " + testdrive_id + " ";
					updateQuery(StrSql);
				}
				// crs.getString("testdrive_in_time").equals(""))
				StrSearch = " AND testdrive_id = " + testdrive_id + "";
				JasperReport report = new JasperReport();
				report.reportfrom = "/sales/reports/" + reportfrom;
				report.parameters = BuildParameters();
				report.dataList = GetePassDetails();
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}
		
		return parameters;
	}
	public List<Map> GetePassDetails() throws IOException {
		HashMap dataMap;
		try {
			StrSql = "Select enquiry_id, enquiry_date, concat(title_desc,' ',contact_fname,' ', contact_lname) as contactname,"
					+ " coalesce(concat(customer_address,', ',city_name,' - ', customer_pin,'.'),'') as customer_address,"
					+ " customer_phone1, customer_mobile1, customer_email1, testdrive_testdriveveh_id, testdriveveh_name, location_name,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_from,'%d/%m/%Y'), '') AS testdrive_time_from,"
					+ " COALESCE(DATE_FORMAT(testdrive_out_time,'%d/%m/%Y'), '') AS testdrive_out_time,"
					+ " COALESCE(emp_name, '') AS emp_name,"
					+ " testdrive_out_kms, testdrive_in_kms, testdrive_license_no, testdrive_license_address, testdrive_license_issued_by,"
					+ " testdrive_license_valid,  concat(testdriveemp.emp_name, ' (',testdriveemp.emp_ref_no,')') as executive,"
					+ " testdrive_notes, "
					
					// for harleydavidson
					+ " testdrive_time,"
					+ " COALESCE(testdrive_time_from, '') AS testdrive_from_time,"
					+ " COALESCE(testdrive_time_to, '') AS testdrive_to_time,"
					+ " COALESCE(testdrive_out_time, '') AS testdrive_time_out,"
					+ " COALESCE(testdrive_in_time, '') AS testdrive_time_in,"
					
					// +
					// " driveremp.emp_name as driver, testdriveveh_regno , comp_logo  "
					+ " COALESCE(driver_name,'') AS driver, testdriveveh_regno, branch_city_id, branch_id, branch_logo  "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive  "
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
					+ " inner Join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id   "
					+ " left join " + compdb(comp_id) + "axela_city on city_id = customer_city_id   "
					+ " inner Join " + compdb(comp_id) + "axela_title on title_id = contact_title_id   "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id  "
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id =  testdrive_testdriveveh_id   "
					+ " inner join " + compdb(comp_id) + "axela_emp as testdriveemp on testdriveemp.emp_id= testdrive_emp_id  "
					+ " left join " + compdb(comp_id) + "axela_sales_testdrive_driver  on driver_id= testdrive_out_driver_id"
					// + " inner join " + compdb(comp_id) +
					// "axela_emp as driveremp on driveremp.emp_id= testdrive_out_driver_id"
					+ ", " + compdb(comp_id) + "axela_comp  "
					+ " where  testdrive_out_time!='' " + StrSearch + " " + BranchAccess + ""
					+ " group by testdrive_id order by branch_name, customer_name";
			
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("GatePassDetails=====" + StrSql);
			while (crs.next()) {
				dataMap = new HashMap();
				
				// TestDrive_Gatepass_Update drive_Gatepass_Update = new TestDrive_Gatepass_Update();
				// drive_Gatepass_Update.salesgatepass_gatepasstype_id = "1";
				// drive_Gatepass_Update.salesgatepass_testdriveveh_id = crs.getString("testdrive_testdriveveh_id");
				// drive_Gatepass_Update.salesgatepass_fromtime = strToLongDate(crs.getString("testdrive_from_time"));
				// drive_Gatepass_Update.salesgatepass_totime = strToLongDate(crs.getString("testdrive_to_time"));
				// drive_Gatepass_Update.salesgatepass_from_branch_id = crs.getString("branch_id");
				// drive_Gatepass_Update.salesgatepass_to_branch_id = crs.getString("branch_id");
				// drive_Gatepass_Update.salesgatepass_driver_id = "0";
				// drive_Gatepass_Update.salesgatepass_notes = crs.getString("testdrive_notes");
				// drive_Gatepass_Update.emp_id = emp_id;
				// SOP("coming..");
				// drive_Gatepass_Update.AddFields(comp_id);
				
				// customer details
				String city_name = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + crs.getString("branch_city_id") + "");
				String branch_invoice_name = ExecuteQuery("SELECT branch_invoice_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id=" + crs.getString("branch_id") + "");
				dataMap.put("city_name", city_name);
				dataMap.put("branch_invoice_name", branch_invoice_name);
				dataMap.put("contactname", unescapehtml(crs.getString("contactname")));
				dataMap.put("customer_address", unescapehtml(crs.getString("customer_address")));
				dataMap.put("customer_phone1", unescapehtml(crs.getString("customer_phone1")));
				dataMap.put("customer_mobile1", unescapehtml(crs.getString("customer_mobile1")));
				dataMap.put("veh_name", unescapehtml(crs.getString("testdriveveh_name")));
				dataMap.put("emp_name", crs.getString("emp_name"));
				dataMap.put("location_name", unescapehtml(crs.getString("location_name")));
				dataMap.put("testdrive_time_from", unescapehtml(crs.getString("testdrive_time_from")));
				// for harleydavidson
				
				dataMap.put("enquiry_id", crs.getString("enquiry_id"));
				dataMap.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
				dataMap.put("testdrive_date", strToShortDate(crs.getString("testdrive_time")));
				dataMap.put("testdrive_from_time", strToLongDate(crs.getString("testdrive_from_time")));
				dataMap.put("testdrive_to_time", strToLongDate(crs.getString("testdrive_to_time")));
				dataMap.put("testdrive_time_out", strToLongDate(crs.getString("testdrive_time_out")));
				dataMap.put("testdrive_time_in", strToLongDate(crs.getString("testdrive_time_in")));
				dataMap.put("customer_email1", crs.getString("customer_email1"));
				
				dataMap.put("testdrive_out_time", unescapehtml(crs.getString("testdrive_out_time")));
				dataMap.put("testdrive_in_kms", unescapehtml(crs.getString("testdrive_in_kms")));
				dataMap.put("testdrive_license_no", unescapehtml(crs.getString("testdrive_license_no")));
				dataMap.put("testdrive_license_address", unescapehtml(crs.getString("testdrive_license_address")));
				dataMap.put("testdrive_license_issued_by", unescapehtml(crs.getString("testdrive_license_issued_by")));
				dataMap.put("testdrive_license_valid", unescapehtml(crs.getString("testdrive_license_valid")));
				dataMap.put("executive", unescapehtml(crs.getString("executive")));
				dataMap.put("driver", unescapehtml(crs.getString("driver")));
				dataMap.put("veh_regno", unescapehtml(crs.getString("testdriveveh_regno")));
				dataMap.put("branch_city_id", crs.getString("branch_city_id"));
				dataMap.put("branch_id", crs.getString("branch_id"));
				dataMap.put("branch_logo", crs.getString("branch_logo"));
				dataMap.put("ch_yes", "");
				dataMap.put("ch_no", "");
				dataMap.put("ch_dealership", "");
				dataMap.put("ch_customerplace", "");
				dataMap.put("ch_yes1", "");
				dataMap.put("ch_no1", "");
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
