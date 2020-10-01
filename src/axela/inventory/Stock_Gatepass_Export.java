package axela.inventory;
//Bhagwan Singh 19th Feb

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Stock_Gatepass_Export extends Connect {
	
	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String smart = "";
	public String ExportPerm = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "stock-gatepass-export.jsp";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				smart = PadQuotes(request.getParameter("smart"));
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("stockgatepassstrsql", request).equals("")) {
					StrSearch = GetSession("stockgatepassstrsql", request);
				}
				if (exportB.equals("Export")) {
					StockGatepassDetails(request, response);
				}
				// SOP("stockstrsql==" + StrSearch);
			}
			
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public void StockGatepassDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			StrSql = "SELECT vehstockgatepass_vehstock_id AS 'Stock ID', vehstockgatepass_id AS 'Gatepass ID',"
					+ " item_name AS 'Variant Desc', item_code as 'Variant Code',"
					+ " vehstock_chassis_no AS 'Chassis No.', CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch',"
					+ " DATE_FORMAT(vehstockgatepass_time, '%d/%m/%y %h:%i:%s') AS 'Gatepass Time',"
					// + " vehstockgatepass_from_location_id, vehstockgatepass_to_location_id,"
					+ " CONCAT(fromloc.vehstocklocation_name, ' (', fromloc.vehstocklocation_id, ')') AS 'From Location',"
					+ " CONCAT(toloc.vehstocklocation_name, ' (', toloc.vehstocklocation_id, ')') AS 'To Location',"
					// + " branch_id,"
					+ " COALESCE(vehstock_invoice_amount,0) AS 'Invoice Amount',"
					+ " vehstockgatepass_stockdriver_id AS 'Driver ID', vehstockdriver_name AS 'Driver Name',"
					+ " COALESCE(vehstockgatepass_notes, '') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = vehstockgatepass_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_driver ON vehstockdriver_id = vehstockgatepass_stockdriver_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location fromloc ON fromloc.vehstocklocation_id = vehstockgatepass_from_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location toloc ON toloc.vehstocklocation_id = vehstockgatepass_to_location_id"
					+ " WHERE 1 = 1" + StrSearch
					+ " GROUP BY vehstockgatepass_id"
					+ " ORDER BY vehstockgatepass_id desc"
					+ " limit 5000";
			// SOP("StrSql--export-" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A3", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public String PopulatePrintOption() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =StockGatepassDetails").append(StrSelectdrop("StockDetails", printoption)).append(">Stock Gatepass Details</option>\n");
		return Str.toString();
	}
}
