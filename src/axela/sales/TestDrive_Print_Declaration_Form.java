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

public class TestDrive_Print_Declaration_Form extends Connect {

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
			testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			reportfrom = "testdrive-print-declaration-form";
			// SOP("reportfrom------------" + reportfrom);
			if (ReturnPerm(comp_id, "emp_testdrive_access", request).equals("1")) {
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
			StrSql = "SELECT"
					+ " COALESCE(CONCAT(branch_name, ' (',branch_code,')'),'') AS branchname,"
					+ " branch_logo AS branch_logo,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " COALESCE(contact_jobtitle,'') AS contact_jobtitle, "
					+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
					+ " COALESCE(contact_email1,'') AS contact_email1,"
					+ " COALESCE(CONCAT(contact_address,',',city_name,',',contact_pin,',',contact_landmark),'') AS  contact_address,"
					+ " COALESCE(testdriveveh_name,'') AS testdriveveh_name,"
					+ " COALESCE(CONCAT(item_name, '(',(case when item_code='' then item_id else item_code end),')'),'') AS item_name,"
					+ " COALESCE(model_name,'') AS model_name,"
					+ " COALESCE(testdriveveh_regno,'') AS testdriveveh_regno,"
					+ " COALESCE(testdrive_out_kms,'') AS testdrive_out_kms,"
					+ " COALESCE(driver_name,'') AS driver_name,"
					+ " COALESCE(testdrive_license_no,'') AS testdrive_license_no,"
					+ " COALESCE(testdrive_license_valid,'') AS testdrive_license_valid"
					+ " FROM " + compdb(comp_id) + " axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_sales_testdrive_driver ON driver_id = testdrive_out_driver_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_branch ON branch_id = enquiry_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + " axela_inventory_item_model ON  model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + " axela_city ON city_id = contact_city_id"
					+ " WHERE 1 = 1"
					+ StrSearch;

			SOPInfo("GatePassDetails=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				dataMap = new HashMap();
				// enquiry_details

				dataMap.put("branchname", crs.getString("branchname"));
				dataMap.put("contact_name", unescapehtml(crs.getString("contact_name")));
				dataMap.put("contact_jobtitle", unescapehtml(crs.getString("contact_jobtitle")));
				dataMap.put("contact_mobile1", unescapehtml(crs.getString("contact_mobile1")));
				dataMap.put("contact_email1", unescapehtml(crs.getString("contact_email1")));
				dataMap.put("contact_address", unescapehtml(crs.getString("contact_address")));
				dataMap.put("testdriveveh_name", crs.getString("testdriveveh_name"));
				dataMap.put("testdriveveh_regno", crs.getString("testdriveveh_regno"));
				dataMap.put("item_name", crs.getString("item_name"));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("testdrive_license_valid", strToLongDate(crs.getString("testdrive_license_valid")));
				dataMap.put("testdrive_out_kms", crs.getString("testdrive_out_kms"));
				dataMap.put("testdrive_license_no", unescapehtml(crs.getString("testdrive_license_no")));
				dataMap.put("testdriveveh_name", unescapehtml(crs.getString("testdriveveh_name")));
				dataMap.put("item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("driver_name", unescapehtml(crs.getString("driver_name")));
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
