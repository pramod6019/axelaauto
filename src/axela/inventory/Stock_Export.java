package axela.inventory;
//Murali 2nd july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Stock_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "", ExeAccess = "";
	public String exportpage = "stock-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("stockstrsql", request).equals("")) {
					StrSearch = GetSession("stockstrsql", request);
					StrSearch += BranchAccess.replace("branch_id", "vehstock_branch_id");
				}
				// SOP("exporttype-----" + exporttype);
				if (exportB.equals("Export")) {
					StockDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void StockDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT  vehstock_id AS 'Stock ID', branch_name AS 'Dealer Location',"
					+ " model_name AS 'Model', "
					+ " item_name AS 'Variant Desc', "
					+ " item_code AS 'Variant Code',"
					+ " COALESCE(option_name,'') AS 'Exterior', "
					+ " COALESCE(option_code,'') AS 'Exterior Code',"
					+ " vehstock_modelyear AS 'Model Year', "
					+ " vehstock_chassis_prefix AS 'Chassis Prefix',"
					+ " vehstock_comm_no AS 'Commission No.',"
					+ " vehstock_chassis_no AS 'Chassis No.',"
					+ " vehstock_engine_no AS 'Engine No.', "
					+ " vehstock_fastag AS 'FASTag ID', "
					+ " COALESCE(so_id, '' ) AS 'SO ID',"
					+ " if(so_date != '',DATE_FORMAT(so_date, '%d-%b-%y'),'') AS 'SO Date',"
					+ " IF(so_retail_date !='', DATE_FORMAT(so_retail_date, '%d/%m/%Y'), '') AS 'Retail Date',"
					+ " COALESCE(DATE_FORMAT(so_stockallocation_time, '%d/%m/%Y'),'') AS 'Allocation Date',"
					+ " IF(so_id !='', COALESCE(DATEDIFF(CONCAT(DATE_FORMAT(CURDATE(), '%Y%m%d'),'000000'), so_stockallocation_time),''),'') AS 'Allocation Ageing',"
					+ " delstatus_name AS 'Veh. Status', "
					+ " COALESCE(vehstocklocation_name,'') AS 'Location',  COALESCE(vehstock_parking_no,'') AS 'Parking Number',"
					+ " COALESCE(vehstock_invoice_no,'') AS 'Invoice No.',"
					+ " if(vehstock_invoice_date != '',DATE_FORMAT(vehstock_invoice_date, '%d-%b-%y'),'') AS 'Invoice Date',"
					+ " if(vehstock_invoice_date != '', DATEDIFF(concat(date_format(CURDATE(), '%Y%m%d'),'000000'),"
					+ " vehstock_invoice_date),'') AS 'Days', vehstock_invoice_amount AS 'Invoice Amount',"
					+ " if(vehstock_pdi_date != '',DATE_FORMAT(vehstock_pdi_date, '%d/%m-%Y'),'') AS 'PDI Date', "
					+ " IF(vehstock_pdi_date !='', COALESCE(DATEDIFF('" + ToLongDate(kknow()) + "', vehstock_pdi_date),''),'') AS 'PDI Ageing',"
					+ " @pdigatepassdate:=COALESCE((SELECT DATE_FORMAT(vehstockgatepass_entry_date, '%d/%m/%Y') "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass "
					+ " WHERE vehstockgatepass_vehstock_id = vehstock_id"
					+ " ORDER BY vehstockgatepass_entry_date ASC"
					+ " limit 1 ),'') AS 'PDI Gatepass Date',"
					+ " COALESCE((SELECT DATEDIFF('" + ToLongDate(kknow()) + "',DATE_FORMAT(STR_TO_DATE(@pdigatepassdate,'%d/%m/%Y'),'%Y%m%d000000'))), '') AS 'PDI Gatepass Ageing',"
					+ " if(vehstock_arrival_date != '',DATE_FORMAT(vehstock_arrival_date, '%d-%b-%y'),'') AS 'Arrival Date',"
					+ " COALESCE(concat(customer_name,' (',customer_id,')'),'') AS Customer, status_name AS 'Status Name',"
					+ " if(vehstock_blocked=1, 'Yes','No') AS 'Blocked',"
					+ " vehstock_intransit_damage AS 'Intransit Damage',"
					+ " if(vehstock_rectification_date != '',DATE_FORMAT(vehstock_rectification_date, '%d/%m/%Y'),'') AS 'Rectification Date',"
					+ " branch_name AS 'Branch', vehstock_notes AS 'Notes'"
					+ " FROM "
					+ "(SELECT vehstock_id, vehstock_branch_id, vehstock_invoice_date, vehstock_blocked,"
					+ " vehstock_ex_price, vehstock_item_id, vehstock_chassis_prefix, vehstock_delstatus_id,"
					+ " vehstock_comm_no, vehstock_chassis_no, vehstock_engine_no, vehstock_fastag, vehstock_vehstocklocation_id, vehstock_status_id,"
					+ " vehstock_stockpriority_id,vehstock_invoice_no,vehstock_ordered_date,vehstock_invoice_amount,vehstock_notes,"
					+ " vehstock_modelyear,vehstock_parking_no,vehstock_pdi_date,vehstock_arrival_date,vehstock_intransit_damage,"
					+ " vehstock_rectification_date,vehstock_entry_date,vehstock_modified_date,"
					+ " COALESCE (so_customer_id,0) AS so_customer_id,"
					+ " COALESCE (so_contact_id,0) AS so_contact_id, COALESCE (so_id, '') AS so_id,"
					+ " COALESCE (so_date, '') AS so_date, COALESCE (so_delivered_date, '') AS so_delivered_date,"
					+ " COALESCE (so_retail_date, '') AS so_retail_date,"
					+ " COALESCE (so_stockallocation_time, '')AS so_stockallocation_time"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1 GROUP BY vehstock_id ) AS stock "

					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id and option_optiontype_id = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE 1 = 1" + StrSearch
					+ " GROUP BY vehstock_id ORDER BY vehstock_id desc limit 5000";
			// SOP("StrSql--stock-export-------------------" + StrSqlBreaker(StrSql));
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = StockDetails" + StrSelectdrop("StockDetails", printoption) + ">Stock Details</option>\n";
		return print;
	}
}
