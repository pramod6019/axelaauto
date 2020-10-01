package axela.preowned;
//
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Preowned_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String exportpage = "preowned-export.jsp";

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
				if (!GetSession("preownedstrsql", request).equals("")) {
					StrSearch = GetSession("preownedstrsql", request);
					// SOP("StrSearch=="+StrSearch);
				}
				if (exportB.equals("Export")) {
					PreownedDetails(request, response);

				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PreownedDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT preowned_id AS ID,"
					+ " COALESCE(preowned_no,'') AS 'No.',"
					+ " CONCAT(branch_name,' (', branch_code, ')') AS Branch,"
					+ " if(preowned_date != '',DATE_FORMAT(preowned_date, '%d/%m/%Y'),'') AS 'Date',"
					+ " if(preowned_close_date != '',DATE_FORMAT(preowned_close_date, '%d/%m/%Y'),'') AS 'Closing Date',"
					+ " CONCAT(preownedmodel_name,' (', preownedmodel_id, ')') AS 'Model Name',"
					+ " COALESCE(variant_name,'') AS 'Variant',"
					+ " COALESCE(customer_name,'') AS 'Customer Name',"
					+ " COALESCE(title_desc,' ',contact_fname,' ',contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS Mobile1,"
					+ " contact_mobile2 AS Mobile2,"
					+ " contact_phone1 AS Phone1,"
					+ " contact_phone2 AS Phone2,"
					+ " contact_email1 AS Email1,"
					+ " contact_email2 as Email2,"
					+ " COALESCE(preowned_title,'') AS 'Title',"
					+ " COALESCE(preowned_desc,'') AS 'Description',"
					+ " COALESCE(preowned_sub_variant,'') AS 'Sub Variant',"
					+ " COALESCE(extcolour_name, '') AS 'Exterior',"
					+ " COALESCE(intcolour_name, '') AS 'Interior',"
					+ " COALESCE(preowned_options,'') AS 'Options',"
					+ " COALESCE(preowned_manufyear,'') AS 'Manufactured Year',"
					+ " COALESCE(preowned_regdyear,'') AS 'Registered Year',"
					+ " COALESCE(preowned_regno,'') AS 'Registration No.',"
					+ " COALESCE(preowned_kms,'') AS 'Kms',"
					+ " COALESCE(fueltype_name,'') AS 'Fuel Type',"
					+ " COALESCE(preowned_fcamt,'') AS 'Foreclosure Amt',"
					+ " COALESCE(preowned_funding_bank,'') AS 'Funding Bank',"
					+ " COALESCE(preowned_loan_no,'') AS 'Loan No.',"
					+ " COALESCE(insurance_name,'') AS 'Insurance Type',"
					+ " COALESCE(ownership_name,'') AS 'Ownership',"
					+ " COALESCE(preowned_expectedprice,'') AS 'Expected Price',"
					+ " COALESCE(preowned_quotedprice,'') AS 'Quoted Price',"
					+ " CONCAT(preowned.emp_name, ' (', preowned.emp_ref_no, ')') AS 'Pre-Owned Consultant',"
					+ " CONCAT(sales.emp_name, ' (', sales.emp_ref_no, ')') AS 'Sales Consultant',"
					// + " coalesce(emp_name,'') as 'Employee Name',"
					+ " COALESCE(campaign_name,'') AS 'Campaign Name',"
					+ " COALESCE(sob_name,'') AS 'Source Of Enquiry',"
					+ " COALESCE(soe_name,'') AS 'Source Of Business',"
					+ " COALESCE(preownedstatus_name,'') AS 'Status Name',"
					+ " COALESCE(prioritypreowned_name,'') AS 'Priority Name',"
					+ " COALESCE(preowned_notes,'') AS Notes"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_emp preowned ON preowned.emp_id = preowned_emp_id"
					+ "	INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ "	INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ "	INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_preowned_status ON preownedstatus_id = preowned_preownedstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id "
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id "
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = preowned_campaign_id"
					+ "	LEFT JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
					+ "	LEFT JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = preowned_fueltype_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_preowned_insurance ON insurance_id = preowned_insurance_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_preowned_ownership ON ownership_id = preowned_ownership_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_preowned_priority ON prioritypreowned_id = preowned_prioritypreowned_id"
					+ " WHERE 1 = 1 "
					+ StrSearch + ""
					+ " GROUP BY preowned_id"
					+ " ORDER BY preowned_id DESC" + ""
					+ " LIMIT " + exportcount;
			// SOP("StrSql-----------------" + StrSql);
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
		print = print + "<option value = Pre-OwnedDetails" + StrSelectdrop("PreownedDetails", printoption) + ">Pre-Owned Details</option>\n";
		return print;
	}
}
