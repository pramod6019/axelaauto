package axela.accounting;
//Murali 2nd july
//JEET 21 NOV 2014

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Voucher_Export extends Connect {

	public String emp_id = "0";
	public String printoption = "";
	public String report = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String vouchertype_id = "0";

	// public String config_sales_oppr_name = "";
	public String comp_module_realtor = "";
	public String comp_module_promoter = "";
	public String exportpage = "voucher-export.jsp";
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	private String param3 = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exportcount = ExecuteQuery("select comp_export_count FROM  " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				param3 = CNumeric(PadQuotes(request.getParameter("param3")));
				if (!param3.equals("0")) {
					vouchertype_id = param3;
				}
				exportB = PadQuotes(request.getParameter("btn_export"));
				// SOP("exportB====" + exportB);
				report = PadQuotes(request.getParameter("report"));
				// SOP("report====" + report);
				// report = "VoucherDetails";

				if (!GetSession("voucherstrsql", request).equals("")) {
					StrSearch += GetSession("voucherstrsql", request);
					// SOP("StrSearch=====" + StrSearch);
				}
				if (report.equals("VoucherDetails") && exportB.equals("Export")) {
					VoucherDetails(request, response);
				} else if (report.equals("VoucherItemDetails") && exportB.equals("Export")) {
					VoucherItemDetails(request, response);
				} else if (report.equals("TallyFormat") && exportB.equals("Export")) {
					VoucherTallyFormat(request, response);
				} else if (report.equals("ItemWise") && exportB.equals("Export")) {
					VoucherItemWise(request, response);
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void VoucherDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select voucher_id AS 'ID',"
					+ " vouchertype_name AS 'Name',"
					+ " COALESCE(concat(vouchertype_prefix,' ', voucher_no,' ', vouchertype_suffix) ,'') AS 'No.',"
					+ " COALESCE(item_hsn,'') AS 'HSN Code',"
					+ " COALESCE(voucher_ref_no, '') AS 'Reference No.',"
					+ " COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),'') AS 'Date' ,"
					+ " COALESCE(voucher_amount,0.00) AS 'Amount' ,"
					+ " COALESCE(voucher_narration,'') AS 'Narration',"
					+ " COALESCE(customer_name,'') AS 'Ledger',"
					+ " COALESCE(customer_name,'') AS 'Customer Name',"
					+ " COALESCE(CONCAT(title_desc,' ',contact_fname,' ',contact_lname),'') AS 'Contact Name',"
					+ " COALESCE(voucher_downpayment,0) AS 'Downpayment',"
					+ " COALESCE(concat(branch_name,' (', branch_code, ')'),'') as 'Branch',"
					+ " IF(voucher_active = 1, 'Yes', 'No') AS 'Active',"
					+ " COALESCE(voucher_notes,'')AS 'Notes'"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id";
			if (vouchertype_id.equals("9")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id";
			} else {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id "
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " WHERE 1 = 1"
					+ " AND voucher_active=1"
					+ StrSearch // comented for testing
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_id LIMIT " + exportcount;

			SOP("StrSql===voucher==" + StrSqlBreaker(StrSql));
			prepmap.clear();
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				SOP("printoption" + printoption);
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void VoucherItemWise(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT COALESCE (voucher_id, 0) AS 'ID',"
					+ " COALESCE (voucher_jc_id, '') AS 'JC ID',"
					+ " '' AS 'Bill Type',"
					+ " '' AS 'VIN',"
					+ " CONCAT( branch_invoice_prefix, voucher_no, branch_invoice_suffix ) AS 'Bill No.',"
					+ " COALESCE (voucher_ref_no, '') AS 'Ref No.',"
					+ " COALESCE ( DATE_FORMAT(voucher_date, '%d/%m/%Y'), '' ) AS 'Bill Date',"
					+ " COALESCE (customer.customer_id, '') AS 'Customer ID',"
					+ " CONCAT( COALESCE (customer.customer_name, ''), ' (', COALESCE (customer.customer_id, '0'), ')' ) AS 'Customer',"
					+ " COALESCE (branch_code, '') AS 'Branch Code',"
					+ " COALESCE ( customer.customer_address, '' ) AS 'Address',"
					+ " COALESCE (dtTax.state_name, '') AS 'State',"
					+ " COALESCE ( customer.customer_gst_no, '' ) AS 'GSTIN',"
					+ " '' AS 'Svc Emp Code',"
					+ " '' AS 'RCATEG Code',"
					+ " '' AS 'RO No.',"
					+ " COALESCE (emp.emp_name, '') AS 'Service Advisor',"
					+ " COALESCE (techemp.emp_name, '') AS 'Technician',"
					+ " COALESCE ( ROUND(voucher_amount, 2), 0.00 ) AS 'Bill Amount',"
					+ " COALESCE (dtTax.item_name, '') AS 'Item',"
					+ " COALESCE ( IF ( dtTax.item_type_id != 4, dtTax.item_hsn, IF (dtTax.item_type_id = 4, dtTax.item_sac, '') ), '' ) AS 'HSN/SAC',"
					+ " COALESCE (dtTax.item_code, '0') AS 'ItemCode',"
					+ " COALESCE (vouchertrans_qty, '0') AS 'Qty',"
					+ " ROUND( ( vouchertrans_amount - ( vouchertrans_discountamount * vouchertrans_qty ) ), 2 ) AS 'Price',"
					+ " COALESCE ( ROUND( vouchertrans_discountamount, 2 ), 0 ) AS 'Discount',"

					+ " COALESCE ( ROUND( dtTax.parts28, 2 ), 0.00 ) AS 'Parts 28%',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST28, 2 ), 0.00 ) AS 'SGST',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST28, 2 ), 0.00 ) AS 'CGST',"
					+ " COALESCE ( ROUND( dtTax.PartsIGST28, 2 ), 0.00 ) AS 'IGST',"

					+ " COALESCE ( ROUND( dtTax.parts18, 2 ), 0.00 ) AS 'Parts 18%',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST18, 2 ), 0.00 ) AS 'SGST',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST18, 2 ), 0.00 ) AS 'CGST',"
					+ " COALESCE ( ROUND( dtTax.PartsIGST18, 2 ), 0.00 ) AS 'IGST',"

					+ " COALESCE ( ROUND( dtTax.parts12, 2 ), 0.00 ) AS 'Parts 12%',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST12, 2 ), 0.00 ) AS 'SGST',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST12, 2 ), 0.00 ) AS 'CGST',"
					+ " COALESCE ( ROUND( dtTax.PartsIGST12, 2 ), 0.00 ) AS 'IGST',"

					+ " COALESCE ( ROUND( dtTax.parts5, 2 ), 0.00 ) AS 'Parts 5%',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST5, 2 ), 0.00 ) AS 'SGST',"
					+ " COALESCE ( ROUND( dtTax.PartsSGST5, 2 ), 0.00 ) AS 'CGST',"
					+ " COALESCE ( ROUND( dtTax.PartsIGST5, 2 ), 0.00 ) AS 'IGST',"

					+ " COALESCE ( ROUND( dtTax.PartsCess15, 2 ), 0.00 ) AS 'Parts Cess 15%',"
					+ " COALESCE ( ROUND( dtTax.PartsCess3, 2 ), 0.00 ) AS 'Parts Cess 3%',"
					+ " COALESCE ( ROUND( dtTax.PartsCess1, 2 ), 0.00 ) AS 'Parts Cess 1%',"

					+ " COALESCE ( ROUND( dtTax.Labour18, 2 ), 0.00 ) AS 'Labour 18%',"
					+ " COALESCE ( ROUND( dtTax.LabourSGST18, 2 ), 0.00 ) AS 'SGST',"
					+ " COALESCE ( ROUND( dtTax.LabourSGST18, 2 ), 0.00 ) AS 'CGST',"
					+ " COALESCE ( ROUND( dtTax.LabourIGST18, 2 ), 0.00 ) AS 'IGST'"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans maintrans ON maintrans.vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer customer ON customer.customer_id = voucher_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_service_jc jc ON jc.jc_id = voucher_jc_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = jc.jc_emp_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp techemp ON techemp.emp_id = jc.jc_technician_emp_id"
					+ " LEFT JOIN ( SELECT vouchertrans_voucher_id, vouchertrans_rowcount, vouchertrans_item_id,"
					+ " vouchertrans_customer_id, state_name,"
					+ " item_code, item_name, item_type_id, item_hsn, item_sac,"

					+ " COALESCE ( IF ( item_type_id != 4 AND tax.customer_taxtype_id IN (3, 4, 5) AND tax.customer_rate IN (14, 28),"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS parts28,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 14,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsSGST28,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 14,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsCGST28,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id != branchcity.city_state_id AND tax.customer_taxtype_id = 5 AND tax.customer_rate = 28,"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS PartsIGST28,"

					+ " COALESCE ( IF ( item_type_id != 4 AND tax.customer_taxtype_id IN (3, 4, 5) AND tax.customer_rate IN (9, 18),"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS parts18,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 9,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsSGST18,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 9,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsCGST18,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id != branchcity.city_state_id AND tax.customer_taxtype_id = 5 AND tax.customer_rate = 18,"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS PartsIGST18,"

					+ " COALESCE ( IF ( item_type_id != 4 AND tax.customer_taxtype_id IN (3, 4, 5) AND tax.customer_rate IN (6, 12),"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS parts12,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 6,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsSGST12,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 6,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsCGST12,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id != branchcity.city_state_id AND tax.customer_taxtype_id = 5 AND tax.customer_rate = 12,"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS PartsIGST12,"

					+ " COALESCE ( IF ( item_type_id != 4 AND tax.customer_taxtype_id IN (3, 4, 5) AND tax.customer_rate IN (2.5, 5),"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS parts5,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 2.5,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsSGST5,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 2.5,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS PartsCGST5,"
					+ " COALESCE ( IF ( item_type_id != 4 AND customercity.city_state_id != branchcity.city_state_id AND tax.customer_taxtype_id = 5 AND tax.customer_rate = 5,"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS PartsIGST5,"

					+ " COALESCE ( IF ( tax.customer_taxtype_id = 6 AND tax.customer_rate = 15, vouchertrans_amount, 0.00 ), '0.00' ) AS partsCess15,"
					+ " COALESCE ( IF ( tax.customer_taxtype_id = 6 AND tax.customer_rate = 3, vouchertrans_amount, 0.00 ), '0.00' ) AS partsCess3,"
					+ " COALESCE ( IF ( tax.customer_taxtype_id = 6 AND tax.customer_rate = 1, vouchertrans_amount, 0.00 ), '0.00' ) AS partsCess1,"

					+ " COALESCE ( IF ( item_type_id = 4 AND tax.customer_taxtype_id IN (3, 4, 5) AND tax.customer_rate IN (9, 18),"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS Labour18,"
					+ " COALESCE ( IF ( item_type_id = 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 9,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS LabourSGST18,"
					+ " COALESCE ( IF ( item_type_id = 4 AND customercity.city_state_id = branchcity.city_state_id AND tax.customer_taxtype_id IN (3, 4) AND tax.customer_rate = 9,"
					+ " SUM(vouchertrans_amount) / 2, 0.00 ), '0.00' ) AS LabourCGST18,"
					+ " COALESCE ( IF ( item_type_id = 4 AND customercity.city_state_id != branchcity.city_state_id AND tax.customer_taxtype_id = 5 AND tax.customer_rate = 18,"
					+ " SUM(vouchertrans_amount), 0.00 ), '0.00' ) AS LabourIGST18"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer tax ON tax.customer_id = vouchertrans_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer cust ON cust.customer_id = voucher_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = cust.customer_city_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_state ON state_id = customercity.city_state_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = cust.customer_city_id"
					+ " WHERE vouchertrans_tax = '1'"
					+ " GROUP BY vouchertrans_voucher_id, vouchertrans_rowcount, vouchertrans_item_id"
					+ " ) AS dtTax ON dtTax.vouchertrans_voucher_id = voucher_id"
					+ "	AND dtTax.vouchertrans_rowcount = maintrans.vouchertrans_rowcount"
					+ " AND dtTax.vouchertrans_item_id = maintrans.vouchertrans_item_id";

			StrSql += " WHERE 1 = 1"
					+ " AND branch_active = '1'"
					+ "	AND voucher_active = '1'"
					+ "	AND maintrans.vouchertrans_rowcount != 0"
					+ " AND maintrans.vouchertrans_option_id = 0"
					+ StrSearch
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY maintrans.vouchertrans_id, maintrans.vouchertrans_item_id"
					+ " ORDER BY voucher_id DESC, vouchertrans_id ASC"
					+ " LIMIT " + exportcount;
			// SOP("StrSql===VoucherItemWise==" + StrSqlBreaker(StrSql));
			prepmap.clear();
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				// SOP("printoption" + printoption);
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public void VoucherTallyFormat(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT vouchertype_name AS 'Voucher Name / Voucher type',"
					+ " COALESCE ( concat( vouchertype_prefix, ' ', voucher_no, ' ', vouchertype_suffix ), '' ) AS 'Voucher No',"
					+ " COALESCE ( DATE_FORMAT(voucher_date, '%d/%m/%Y'), '' ) AS 'Voucher Date',"
					+ " COALESCE (customer.customer_name, '') AS 'Customer Name',"
					+ " COALESCE (customer.customer_id, '') AS 'Customer ID',"
					+ " COALESCE (veh_chassis_no, '') AS 'Vin No.',"
					+ " COALESCE ( customer.customer_address, '' ) AS 'Address',"
					+ " COALESCE (state_name, '') AS 'State',"
					+ " COALESCE ( customer.customer_mobile1, '' ) AS 'Mobile No',"
					+ " COALESCE ( customer.customer_pan_no, '' ) AS 'PAN',"
					+ " COALESCE ( customer.customer_gst_no, '' ) AS 'GSTIN/UIN',"
					+ " COALESCE (voucher_amount, 0.00) AS 'Amt',"
					+ " COALESCE (ledger.ledgername,'') AS 'Ledger',"
					+ " COALESCE (voucher_ref_no, '') AS 'Reference No.',"
					+ " COALESCE (voucher_narration, '') AS 'Narration',"
					+ " COALESCE (branch_name, '') AS 'Branch',"
					+ " IF ( COALESCE (voucher_advance, '0') = '1', 'Advance', 'Balance Amount' ) AS 'Advance/BalanceAmount'"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans main ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer customer ON customer.customer_id = voucher_customer_id"
					+ " LEFT JOIN ( SELECT vouchertrans_voucher_id, customer_name AS ledgername"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " WHERE	vouchertrans_dc = '1' AND customer_ledgertype != '0' GROUP BY vouchertrans_voucher_id )"
					+ " AS ledger ON ledger.vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer.customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = location_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = veh_branch_id"
					+ " WHERE 1 = 1 AND vouchertype_id = 9"
					+ " AND voucher_active=1"
					+ " AND voucher_authorize=1"
					+ " AND vouchertype_voucherclass_id = 9"
					+ StrSearch // commented for testings
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY voucher_id ORDER BY voucher_id DESC LIMIT " + ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp WHERE comp_id=" + comp_id);

			// SOP("StrSql===TallyFormat==" + StrSqlBreaker(StrSql));
			prepmap.clear();
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				SOP("printoption" + printoption);
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void VoucherItemDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_id AS 'ID',"
					+ " vouchertype_name AS 'Name',"
					+ " COALESCE(CONCAT(vouchertype_prefix,' ', voucher_no,' ', vouchertype_suffix) ,'') AS 'No.',"
					+ " COALESCE(voucher_ref_no, '') AS 'Reference No.',"
					+ " if(voucher_date != '',DATE_FORMAT(voucher_date, '%d/%m/%Y'),'') AS 'Date' ,"
					+ " voucher_amount AS 'Amount' ,"
					+ " voucher_narration AS 'Narration',"
					+ " COALESCE(customer_name,'') AS 'Ledger',"
					+ " COALESCE(customer_name,'') AS 'Customer Name',"
					+ " COALESCE(CONCAT(title_desc,' ',contact_fname,' ',contact_lname),'') AS 'Contact Name',"
					+ " COALESCE(voucher_downpayment,0) AS 'Downpayment',"
					+ " COALESCE(CONCAT(branch_name,' (', branch_code, ')'),'') AS 'Branch',"
					+ " IF(voucher_active=1, 'Yes', 'No') AS 'Active',"
					+ " voucher_notes AS 'Notes',"
					+ " vouchertrans_id AS 'Vouchertrans ID',"
					+ " vouchertrans_item_id AS 'Item ID', "
					+ " COALESCE(item_name,'') AS 'Item', "
					+ " vouchertrans_qty 'Qty.',"
					+ " vouchertrans_amount AS 'Amount',"
					+ " COALESCE(paymode_name,'') AS 'Payment By',"
					+ " COALESCE(vouchertrans_cheque_no,0) AS 'Check/Card No.',"
					+ " COALESCE(DATE_FORMAT(voucher_date, '%d/%m/%Y'),'') AS 'Payment Date',"
					+ " COALESCE(vouchertrans_cheque_bank,'') AS 'Bank',"
					+ " COALESCE(vouchertrans_cheque_branch,'') AS 'Bank Branch',"
					+ " IF(vouchertrans_reconciliation=1,'Yes','No') AS 'Reconcile',"
					+ " COALESCE(DATE_FORMAT(vouchertrans_reconciliation_date, '%d/%m/%Y'),'') AS 'Reconcile Date'"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer trans ON trans.customer_id = vouchertrans_customer_id"
					+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " WHERE 1 = 1"
					+ " AND voucher_active=1"
					+ StrSearch // commented for testings
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "voucher_emp_id") + ""
					+ " GROUP BY vouchertrans_id"
					+ " ORDER BY voucher_id, vouchertrans_id";

			// SOP("StrSql===item===" + StrSqlBreaker(StrSql));
			prepmap.clear();
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
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

	public String PopulatePrintOption(String vouchertype_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=VoucherDetails").append(StrSelectdrop("VoucherDetails", printoption)).append(">Voucher Details</option>\n");
		if (vouchertype_id.equals("9")) {
			Str.append("<option value=TallyFormat").append(StrSelectdrop("TallyFormat", printoption)).append(">Tally Format</option>\n");
		}
		if (vouchertype_id.equals("6") || vouchertype_id.equals("7")) {
			Str.append("<option value=ItemWise").append(StrSelectdrop("ItemWise", printoption)).append(">Item Wise</option>\n");
		}

		// Str.append("<option value=VoucherItemDetails").append(StrSelectdrop("VoucherItemDetails", printoption)).append(">Voucher Item Details</option>\n");
		return Str.toString();
	}

}
