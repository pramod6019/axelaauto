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

public class TestDrive_Print_GatePass_MB extends Connect {
	
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
			if (brand_id.equals("55")) {
				reportfrom = "testdrive-print-gatepass-mb";
			} else if (brand_id.equals("6")) {
				reportfrom = "testdrive-print-gatepass-hyundai";
			} else if (brand_id.equals("7")) {
				reportfrom = "testdrive-print-gatepass-ford";
			} else if (brand_id.equals("9")) {
				reportfrom = "testdrive-print-gatepass-honda";
			} else if (brand_id.equals("51")) {
				reportfrom = "testdrive-print-gatepass-volvo";
			} else {
				reportfrom = "testdrive-print-gatepass-maruti";
			}
			// SOP("reportfrom------------------" + reportfrom);
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
				report.dataList = InvoiceDetails();
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
	public List<Map> InvoiceDetails() throws IOException {
		HashMap dataMap;
		try {
			StrSql = "Select enquiry_id, concat(title_desc,' ',contact_fname,' ', contact_lname) as contactname,"
					+ " coalesce(concat(customer_address,', ',city_name,' - ', customer_pin,'.'),'') as customer_address,"
					+ " customer_phone1, customer_mobile1,branch_invoice_name, testdriveveh_name, location_name,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_from,'%d/%m/%Y %h:%i'), '') AS testdrive_time_from,"
					+ " COALESCE(DATE_FORMAT(testdrive_time,'%d/%m/%Y %h:%i %p'), '') AS testdrive_time,"
					+ " COALESCE(DATE_FORMAT(testdrive_time_to,'%d/%m/%Y %h:%i'), '') AS testdrive_time_to,"
					+ " testdrive_out_kms, testdrive_in_kms, testdrive_license_no, testdrive_license_address, testdrive_license_issued_by,"
					+ " testdrive_license_valid,  concat(testdriveemp.emp_name, ' (',testdriveemp.emp_ref_no,')') as executive,"
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
					+ " inner join " + compdb(comp_id) + "axela_emp as testdriveemp on testdriveemp.emp_id = testdrive_emp_id  "
					+ " left join " + compdb(comp_id) + "axela_sales_testdrive_driver  on driver_id = testdrive_out_driver_id"
					// + " inner join " + compdb(comp_id) +
					// "axela_emp as driveremp on driveremp.emp_id= testdrive_out_driver_id"
					+ ", " + compdb(comp_id) + "axela_comp  "
					+ " where  testdrive_out_time!='' " + StrSearch + " " + BranchAccess + ""
					+ " group by testdrive_id order by branch_name, customer_name";
			
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("GatePassDetails=====" + StrSqlBreaker(StrSql));
			while (crs.next()) {
				dataMap = new HashMap();
				// customer details
				String city_name = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + crs.getString("branch_city_id") + "");
				String branch_invoice_name = ExecuteQuery("SELECT branch_invoice_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id=" + crs.getString("branch_id") + "");
				// SOP(crs.getString("testdrive_time"));
				String shorttime = crs.getString("testdrive_time").substring(11);
				// SOP("shorttime===" + shorttime);
				String str = unescapehtml(""
						+ "\n\n  I, " + crs.getString("contactname") + ","
						+ " Son/Daughter of Mr. ..................................."
						+ " ( Father's Name ), residing at\n"
						+ "  " + crs.getString("customer_address") + "\n "
						+ " hereby agree & confirm that:\n\n"
						+ " 1.\tI am aware of all potential risks in the test drive or ride of car "
						+ " and shall undertake the drive or ride at \tmy sole risk & responsibility.\n\n"
						+ " 2.\tI shall not hold M/s " + crs.getString("branch_invoice_name") + " Pvt. Ltd., or its employees or any individual connected with the \ttest drive"
						+ " or ride responsible to any loss/injury caused to me or any occupants in the car.\n\n"
						+ " 3.\tI hereby declare that I hold a valid driving license (License No. mentioned above) as per the Indian \troad rules and regulations.\n\n"
						+ " 4.\tI have not consumed /shall not consume any kind of alcohol and / or other drugs before or during the \ttrial session in progress.\n\n"
						+ " 5.\tI have not been prescribed medication for any reason.\n\n"
						+ " 6.\tThe M/s " + crs.getString("branch_invoice_name") + " Pvt. Ltd., , its employees shall in no circumstances whatsoever to liable for \tany death,"
						+ " personal injury, sickness, accident , loss, delay, discomfort, increased expenses, \tconsequential loss,and / or damage or any"
						+ " misadventure howsoever caused to me or any \toccupants in the car.\n\n"
						+ " 7.\tI undertake to comply with all the relevant Road Traffic Rules and Regulations during the test drive.\n\n"
						+ " I acknowledge and accept the condition that my participation in test drive or and ride is entirely on my risk and by submitting this"
						+ " declaration I waive all rights of legal action and recourse for any accident or damage incurred in conjunction with the event against"
						+ " " + crs.getString("branch_invoice_name") + " Pvt. Ltd., I further  undertake to"
						+ " handle and drive the dealership vehicle with care.\n\n");
				// SOP("str======111" + str);
				dataMap.put("city_name", city_name);
				dataMap.put("branch_invoice_name", branch_invoice_name);
				dataMap.put("contactname", unescapehtml(crs.getString("contactname")));
				dataMap.put("customer_address", unescapehtml(crs.getString("customer_address")));
				dataMap.put("customer_phone1", unescapehtml(crs.getString("customer_phone1")));
				dataMap.put("customer_mobile1", unescapehtml(crs.getString("customer_mobile1")));
				dataMap.put("veh_name", unescapehtml(crs.getString("testdriveveh_name")));
				dataMap.put("location_name", unescapehtml(crs.getString("location_name")));
				dataMap.put("testdrive_time_from", unescapehtml(crs.getString("testdrive_time_from").substring(11)));
				dataMap.put("testdrive_time_to", unescapehtml(crs.getString("testdrive_time_to").substring(11)));
				dataMap.put("testdrive_in_kms", unescapehtml(crs.getString("testdrive_in_kms")));
				dataMap.put("testdrive_out_kms", unescapehtml(crs.getString("testdrive_out_kms")));
				
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
				dataMap.put("date", strToLongDate(ToLongDate(kknow())));
				dataMap.put("str", str);
				dataMap.put("testdrive_time1", crs.getString("testdrive_time").substring(0, 16));
				dataMap.put("testdrive_time", crs.getString("testdrive_time").substring(0, 10));
				dataMap.put("shorttime", shorttime);
				
				// SOP("testdrive_time==========" + crs.getString("testdrive_time").substring(11));
				
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
