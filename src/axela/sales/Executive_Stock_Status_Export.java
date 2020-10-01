package axela.sales;
//Murali 2nd july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Executive_Stock_Status_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "", ExeAccess = "";
	public String exportpage = "../sales/executive-stock-status-export.jsp";

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
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("executivestockstatusstrsql", request).equals("")) {
					StrSearch = GetSession("executivestockstatusstrsql", request);
					StrSearch += BranchAccess;
					// + ExeAccess.replace("emp_id", "emp.emp_id");
				}
				if (exportB.equals("Export")) {
					ExecutiveStockStatusDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ExecutiveStockStatusDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT vehstock_id AS 'ID',"
					// + " vehstock_comm_no,"
					+ " COALESCE(vehstock_parking_no, '')  AS 'Stock Parking No.',"
					+ " COALESCE(vehstock_vehstocklocation_id,0) AS 'Stock Location ID',"
					+ " COALESCE(vehstocklocation_name, '') AS 'Location Name',"
					+ " COALESCE(vehstock_chassis_no,'') AS 'Stock Chasses No.',"
					+ " COALESCE(vehstock_engine_no , '') AS 'Stock Engine No.',"
					+ " COALESCE(DATE_FORMAT(vehstock_invoice_date, '%d/%m/%Y'), '') AS 'Stock Invoice Date',"
					+ " COALESCE(DATE_FORMAT(vehstock_ordered_date, '%d/%m/%Y'), '') AS 'Stock Ordered Date',"
					+ " COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), '') AS 'Stock Delivered Date',"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS 'Item Name',"
					+ " COALESCE(item_id,0) AS 'Item ID',"
					+ " COALESCE(model_id,'') AS 'Model ID',"
					+ " COALESCE(model_name,'') AS 'Model Name',"
					+ " vehstock_item_id AS 'Stock Item ID', vehstock_ex_price AS 'Stock Ex. Price', delstatus_name AS 'Delstatus Name',"
					+ " vehstock_status_id AS 'Stock Status ID', status_name AS 'Status Name',"
					+ " COALESCE(DATE_FORMAT(vehstock_pdi_date, '%d/%m/%Y'),'') AS 'Stock PDI Date',"
					+ " COALESCE(vehstock_intransit_damage, '') AS 'Stock Intransit Damage',"
					+ " COALESCE(DATE_FORMAT(vehstock_rectification_date, '%d/%m/%Y'), '') AS 'Stock Rectification Date',"
					+ " vehstock_blocked AS 'Stock Blocked',"
					+ " COALESCE(CONCAT(option_name, ' (', option_code, ')'),'') AS 'Option Name', COALESCE(option_id,'') AS 'Option ID',"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch Name',"
					+ " branch_id as 'Branch ID', COALESCE(COUNT(DISTINCT quote_id),0) AS 'Quotecount',"
					+ " COALESCE(so_id, '0') AS 'SO ID',"
					+ " COALESCE (soemp.emp_name, '') AS 'SO Executive',"
					+ " COALESCE (teamemp.emp_name, '') AS 'Manager',"
					+ " COALESCE(IF(COALESCE(so_no, 0) != '0', CONCAT(branch_code, so_no), ''), '') AS 'SO No.',"
					+ " COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date',"
					+ " COALESCE(DATE_FORMAT(so_stockallocation_time, '%d/%m/%Y %h:%i'), '') AS 'SO Stock Allocation Time',"
					+ " COALESCE(customer_id, 0) AS 'Customer ID',"
					+ " COALESCE(customer_name, '') AS 'Customer Name', COALESCE(so_grandtotal, 0) AS 'SO Grandtotal',"
					+ " COALESCE((SELECT SUM(voucher_amount) FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_active = 1 AND voucher_so_id = so_id AND voucher_vouchertype_id = 9 ), 0)"
					+ " AS 'SO Receiptamount'"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch stockbranch ON branch_id = vehstock_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
					+ " AND status_id != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp soemp ON soemp.emp_id = so_emp_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp teamemp ON teamemp.emp_id = team_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_vehstock_id = vehstock_id"
					+ " WHERE 1 = 1 AND delstatus_id!=6 " + StrSearch
					+ " GROUP BY vehstock_id"
					+ " ORDER BY vehstock_id"
					+ " LIMIT " + exportcount;
			// SOP("StrSql====" + StrSql);
			// SOP("StrSearch.===" + StrSearch);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A2", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = ExecutiveStockStatusDetails" + StrSelectdrop("ExecutiveStockStatusDetails", printoption) + ">Sales Consultant Stock Status Details</option>\n";
		return print;
	}
}
