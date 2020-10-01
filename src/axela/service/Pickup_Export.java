package axela.service;
//Sangita 06 APRIL 2013
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Pickup_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "pickup-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportcount = ExecuteQuery("Select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("pickupstrsql", request).equals("")) {
					StrSearch = GetSession("pickupstrsql", request);
				}
				SOP("StrSearch===" + StrSearch);
				if (exportB.equals("Export")) {
					PickupDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PickupDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT pickup_id AS 'ID', branch_name AS Branch, pickuptype_name AS 'Pickup Type',"
					+ " pickup_mobile1 AS Mobile1, pickup_mobile2 AS 'Mobile2', pickup_add AS Address,"
					+ " pickup_landmark AS Landmark, COALESCE(veh_id, 0) AS 'Vehicle ID',"
					+ " COALESCE(veh_reg_no, '') AS 'Reg.No.', COALESCE(item_name, '') AS 'Item',"
					+ " emp_name AS Driver, COALESCE(DATE_FORMAT(pickup_time, '%d/%m/%Y %h:%i'),'') AS Time,"
					+ " CONCAT(DATE_FORMAT(pickup_time_from, '%d/%m/%Y %h:%i'), '-', DATE_FORMAT(pickup_time_to,'%h:%i')) AS Duration,"
					+ " IF(pickup_active=1,'YES','NO') AS Active, location_name AS Location"
					+ " FROM " + compdb(comp_id) + "axela_service_pickup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=pickup_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_pickup_type ON pickuptype_id = pickup_pickuptype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id = pickup_location_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = pickup_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " WHERE 1 = 1 " + StrSearch + BranchAccess + ExeAccess + ""
					+ " GROUP BY pickup_id ORDER BY pickup_id"
					+ " LIMIT " + exportcount;
			// SOP("StrSql(11)===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = PickupDetails" + StrSelectdrop("PickupDetails", printoption)
				+ ">Pickup Details</option>\n";
		return print;
	}
}
