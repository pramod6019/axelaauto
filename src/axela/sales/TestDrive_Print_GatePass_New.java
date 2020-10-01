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

public class TestDrive_Print_GatePass_New extends Connect {

	public String brand_id = "0";
	public String StrSql = "", msg = "", reportfrom = "", format = "pdf";
	public String StrHTML = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String invoice_id = "0";
	public String branch_city_id = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String StrSearch = "";
	public String salesgatepass_id = "";
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
			salesgatepass_id = CNumeric(PadQuotes(request.getParameter("salesgatepass_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			reportfrom = "testdrive-print-gatepass-new";
			// SOP("reportfrom------------" + reportfrom);
			if (ReturnPerm(comp_id, "emp_testdrive_access", request).equals("1")) {
				if (testdrive_out_time.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id)
							+ "axela_sales_testdrive" + " SET"
							+ " testdrive_out_time = '" + ToLongDate(kknow())
							+ "'," + " testdrive_mileage_entry_id = '" + emp_id
							+ "', " + " testdrive_mileage_entry_date = '"
							+ ToLongDate(kknow()) + "' "
							+ " WHERE testdrive_id = " + salesgatepass_id + " ";
					updateQuery(StrSql);
				}
				// crs.getString("testdrive_in_time").equals(""))
				StrSearch = " AND testdrive_id = " + salesgatepass_id + "";
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
					+ " salesgatepass_id,"
					+ " salesgatepass_gatepasstype_id,"
					+ " salesgatepasstype_name,"
					+ " salesgatepass_testdriveveh_id,"
					+ " salesgatepass_fromtime,"
					+ " salesgatepass_totime,"
					+ " salesgatepass_from_branch_id,"
					+ " CONCAT(frombranch.branch_name, ' (',frombranch.branch_code,')') AS frombranchname,"
					+ " frombranch.branch_logo AS branch_logo,"
					+ " salesgatepass_to_branch_id,"
					+ " testdriveveh_notes,"
					+ " CONCAT(tobranch.branch_name, ' (',tobranch.branch_code,')') AS tobranchname,"
					+ " salesgatepass_driver_id,"
					+ " CONCAT(item_name, '(',(case when item_code='' then item_id else item_code end),')') AS item_name,"
					+ " model_name,"
					+ " testdriveveh_name,"
					+ " COALESCE(emp_name,'') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_gatepass"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = salesgatepass_testdriveveh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON  model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_gatepass_type ON salesgatepasstype_id = salesgatepass_gatepasstype_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch frombranch ON frombranch.branch_id = salesgatepass_from_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch tobranch ON tobranch.branch_id = salesgatepass_to_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = salesgatepass_driver_id"
					+ " WHERE 1 = 1"
					+ " AND salesgatepass_id = " + salesgatepass_id;

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOPInfo("GatePassDetails=====" + StrSql);
			while (crs.next()) {
				dataMap = new HashMap();
				// enquiry_details

				dataMap.put("salesgatepass_id", crs.getString("salesgatepass_id"));
				dataMap.put("salesgatepasstype_name", unescapehtml(crs.getString("salesgatepasstype_name")));
				dataMap.put("salesgatepass_testdriveveh_id", unescapehtml(crs.getString("salesgatepass_testdriveveh_id")));
				dataMap.put("salesgatepass_fromtime", strToLongDate(crs.getString("salesgatepass_fromtime")));
				dataMap.put("salesgatepass_totime", strToLongDate(crs.getString("salesgatepass_totime")));
				dataMap.put("testdriveveh_notes", unescapehtml(crs.getString("testdriveveh_notes")));
				dataMap.put("from_branch", unescapehtml(crs.getString("frombranchname")));
				dataMap.put("to_branch", unescapehtml(crs.getString("tobranchname")));
				dataMap.put("emp_name", unescapehtml(crs.getString("emp_name")));
				dataMap.put("item_name", unescapehtml(crs.getString("item_name")));
				dataMap.put("model_name", unescapehtml(crs.getString("model_name")));
				dataMap.put("testdriveveh_name", unescapehtml(crs.getString("testdriveveh_name")));
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
