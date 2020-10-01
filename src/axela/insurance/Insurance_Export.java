package axela.insurance;
//Dilip Kumar 13th APR, 2013

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Insurance_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String exportpage = "insurance-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("insurancestrsql", request).equals("")) {
					StrSearch = GetSession("insurancestrsql", request);
				}
				if (exportB.equals("Export")) {
					ContractDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ContractDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT insurpolicy_id AS 'ID', "
					+ " COALESCE(insurpolicy_insurenquiry_id,'') AS 'Insurance Enquiry ID', "
					+ " branch_name AS 'Branch', "
					+ " insurtype_name AS 'Type', "
					+ " DATE_FORMAT(insurpolicy_date,'%d/%m/%Y') AS 'Date', "
					+ " DATE_FORMAT(insurpolicy_start_date, '%d/%m/%Y') AS 'Start Time', "
					+ " DATE_FORMAT(insurpolicy_end_date, '%d/%m/%Y') AS 'End Time', "
					+ " policytype_name AS 'Policy Name', "
					+ " insurpolicy_policy_no AS 'Policy No.', "
					+ " COALESCE(customer_name,'') AS 'Customer', "
					+ " CONCAT(title_desc,' ', contact_fname,' ', contact_lname) AS 'Contact',"
					+ " insurpolicy_premium_amt AS 'Premium Amount',"
					+ " insurpolicy_idv_amt AS 'IDV Amount',"
					+ " insurpolicy_od_amt AS 'OD Amount',"
					+ " insurpolicy_od_discount AS 'OD Discount',"
					+ " insurpolicy_payout AS 'Payout',"
					+ " insurpolicy_desc AS 'Descripition', "
					+ " insurpolicy_terms AS 'Terms', "
					+ " CONCAT(emp_name, '(', emp_ref_no, ')') AS 'Executive',"
					+ " IF(insurpolicy_active = 1, 'Yes', 'No') AS 'Active', "
					+ " insurpolicy_notes AS 'Notes' "
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
					+ " WHERE 1 = 1" + StrSearch + BranchAccess + ""
					+ " GROUP BY insurpolicy_id"
					+ " ORDER BY insurpolicy_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A4", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =InsuranceDetails" + StrSelectdrop("InsuranceDetails", printoption) + ">Insurance Details</option>\n";
		return print;
	}
}
