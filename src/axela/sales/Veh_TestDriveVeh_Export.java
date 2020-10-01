package axela.sales;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToXLSX;

public class Veh_TestDriveVeh_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String report = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "vehicle-testdrive-export.jsp";
	DecimalFormat df = new DecimalFormat("0.00");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			SOP("coming..");
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				report = PadQuotes(request.getParameter("report"));

				if (!GetSession("testdrivevehilesql", request).equals("")) {
					StrSearch = GetSession("testdrivevehilesql", request);
					StrSearch += BranchAccess.replace("branch_id", "veh_branch_id");
				}

				if (report.equals("TestDriveVehicleList") && exportB.equals("Export")) {
					testDriveVehicleList(request, response);
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void testDriveVehicleList(HttpServletRequest request, HttpServletResponse response) {
		try {

			printoption = "TestDriveVehicleList";

			StrSql = "SELECT veh_name AS 'Name', veh_regno AS 'Vehicle Reg. No.',"
					+ " COALESCE (model_name, '') AS 'Model Name',"
					+ " COALESCE (item_name, '') AS 'Variant',"
					+ " COALESCE (DATE_FORMAT(veh_service_start_date,'%d/%m/%Y'), '') AS  'Service Start Date',"
					+ " COALESCE (DATE_FORMAT(veh_service_end_date,'%d/%m/%Y'), '') AS 'Service End Date',"
					+ " CONCAT( branch_name, ' (', branch_code, ')' ) AS 'Branch Name'"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
					+ " WHERE 1 = 1"
					+ StrSearch
					+ " ORDER BY veh_id DESC"
					+ " LIMIT " + exportcount + "";

			// SOP("StrSql-------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String PopulatePrintOption() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =TestDriveVehicleList").append(StrSelectdrop("TestDriveVehicleList", printoption)).append(">Test Drive Vehicle List</option>\n");
		return Str.toString();
	}
}
