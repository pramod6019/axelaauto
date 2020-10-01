package axela.preowned;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Preowned_Stock_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String exportpage = "preowned-stock-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("preownedstockstrsql", request).equals("")) {
					StrSearch = GetSession("preownedstockstrsql", request);
					// SOP("StrSearch=="+StrSearch);
				}
				if (exportB.equals("Export")) {
					PreownedStockDetails(request, response);

				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PreownedStockDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedstock_id AS 'Stock ID',"
					+ " COALESCE(so_id, 0) AS 'Sales Order ID',"
					+ " COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date', "
					+ " CONCAT(precustomer.customer_name, ' (', precustomer.customer_id, ')') AS 'Baught from',"
					+ " COALESCE(preownedtype_name, '') AS 'Type', "
					+ " preowned_title AS 'Title',"
					+ " preownedmodel_name AS 'Model',"
					+ " variant_name AS 'Variant',"
					+ " COALESCE(preowned_kms, '') AS 'Mileage',"
					+ " COALESCE(CONCAT(socustomer.customer_name, ' (', socustomer.customer_id, ')'), '') AS 'Customer to whom we sold',"
					+ " COALESCE(preownedstock_purchase_amt, 0) AS 'Cost = WDV / Buying Price',"
					// + " COALESCE(preowned_kms, '') AS 'Capitalisation Date',"
					+ " COALESCE(DATE_FORMAT(preownedstock_date, '%d/%m/%Y'), '') AS 'Stock Date',"
					+ " COALESCE(DATE_FORMAT(preownedstock_puttosale_date, '%d/%m/%Y'), '') AS 'Put to Sale Date',"
					+ " COALESCE(preowned_fcamt, 0) AS 'Funding Amount',"
					+ " COALESCE(preowned_funding_bank, '') AS 'Funding Bank',"
					+ " COALESCE(preowned_loan_no, '') AS 'Loan No.',"
					+ " CONCAT(pretitle.title_desc, ' ', precontact.contact_fname, ' ', precontact.contact_lname) AS 'Contact',"
					+ " preownedstatus_name as 'Status Name',"
					+ " preownedstock_purchase_amt AS 'Purchase Amount', "
					+ " COALESCE(preownedstock_refurbish_amt, 0) AS 'Refurbishment Cost',"
					+ " if(preownedstock_date != '',DATE_FORMAT(preownedstock_date, '%d/%m/%Y'),'') AS 'Purchase Date',"
					+ " preownedstock_selling_price AS 'Selling Price',"
					+ " COALESCE(CONCAT(soemp.emp_name, ' (', soemp.emp_ref_no, ')'), '') AS 'SM',"
					+ " COALESCE(CONCAT(preemp.emp_name, ' (', preemp.emp_ref_no, ')'), '') AS 'Procurement Manager',"
					+ " preowned_regno AS 'Regn. No',"
					+ " preownedstock_chassis_no AS 'Vin No',"
					+ " preownedstock_engine_no AS 'Engine No',"
					+ " COALESCE(extcolour_name,'') AS 'Color',"
					+ " preowned_manufyear AS 'Model Year',"
					+ " COALESCE(intcolour_name,'') AS 'Interior', "
					+ " COALESCE(preowned_options, '') AS 'Package', "
					+ " COALESCE(preownedlocation_name, '') AS 'Location',"
					+ " preowned_expectedprice AS 'Expected Price',"
					+ " preowned_quotedprice AS 'Quoted Price',"
					+ " COALESCE(so_grandtotal, 0) AS 'SO Price', "
					// + " @receipt := coalesce((select sum(receipt_amount) from " + compdb(comp_id)
					// + "axela_invoice_receipt where receipt_active='1' and receipt_so_id = so_id), 0) AS 'Receipt Amount', "
					// + " @receiptperc := coalesce(round((@receipt/so_grandtotal)*100,2) ,0) as 'Receipt %',"
					// + " COALESCE(date_format((select receipt_date FROM " + compdb(comp_id) + "axela_invoice_receipt"
					// + " WHERE receipt_active='1' AND @receiptperc>=80 AND receipt_so_id=so_id LIMIT 1), '%d/%m/%y'),'') as '80% Date',"
					+ " coalesce(so_grandtotal-@receipt ,0) AS 'OutStanding',"
					// + " '' AS 'Delivery Status',"
					+ " COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), '') AS 'Delivery Date', "
					+ " preownedstock_engine_no AS 'Engine No.',"
					+ " preownedstock_comm_no AS 'Commission No.',"
					+ " CONCAT(preemp.emp_name, ' (', preemp.emp_ref_no, ')') AS 'Pre-Owned Consultant',"
					+ " CONCAT(sales.emp_name, ' (', sales.emp_ref_no, ')') AS 'Sales Consultant',"
					+ " CONCAT(branch_name,' (', branch_code, ')') AS 'Branch',"
					+ " COALESCE(preowned_notes,'') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
					+ " LEFT JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preemp on preemp.emp_id = preowned_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock_status ON preownedstatus_id = preownedstock_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_type ON preownedtype_id = preownedstock_preownedtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location ON preownedlocation_id = preownedstock_preownedlocation_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER join " + compdb(comp_id) + "axela_customer precustomer on precustomer.customer_id = preowned_customer_id"
					+ " INNER join " + compdb(comp_id) + "axela_customer_contact precontact on precontact.contact_id = preowned_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title pretitle ON pretitle.title_id = precontact.contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales on sales.emp_id = preowned_sales_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_preownedstock_id = preownedstock_id AND so_active=1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer socustomer on socustomer.customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact socontact on socontact.contact_id = so_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp soemp ON soemp.emp_id = so_emp_id"
					+ " WHERE 1 = 1 " + StrSearch + ""
					+ " GROUP BY preownedstock_id"
					+ " ORDER BY preownedstock_id DESC" + ""
					+ " LIMIT " + exportcount;

			SOP("StrSql===" + StrSql);
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
		print = print + "<option value = PreownedDetails" + StrSelectdrop("PreownedDetails", printoption) + ">Pre Owned Details</option>\n";
		return print;
	}
}
