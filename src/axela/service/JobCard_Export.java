package axela.service;
/*
 *@author GuruMurthy TS 30 JAN 2012
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class JobCard_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "jobcard-export.jsp";

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
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("jcstrsql", request).equals("")) {
					StrSearch = GetSession("jcstrsql", request);
				}

				if (exportB.equals("Export")) {
					JobCardDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void JobCardDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT COALESCE(jc_id, 0) AS 'ID',"
					+ " COALESCE(branch_name, '') AS 'Branch',"
					+ " COALESCE(jc_no, '') AS 'Job Card No.',"
					+ " COALESCE(jc_ro_no, '') AS 'RO No.',"
					+ " COALESCE(veh_id, 0) AS 'Vehicle ID',"
					+ " COALESCE(veh_reg_no, '') AS 'Reg. No.',"
					+ " COALESCE(IF(veh_iacs = 1, 'Yes', 'No'), '') AS 'IACS',"
					+ " COALESCE(customer_name, '') AS 'Customer',"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS 'Contact',"
					+ " COALESCE(contact_mobile1, '') AS 'Mobile 1',"
					+ " COALESCE(contact_mobile2, '') AS 'Mobile 2',"
					+ " COALESCE(contact_email1, '') AS 'Email 1',"
					+ " COALESCE(contact_email2, '') AS 'Email 2',"
					+ " COALESCE(variant_name, '') AS 'Item',"
					+ " COALESCE(jctype_name, '') AS 'Type',"
					+ " COALESCE(jccat_name, '') AS 'Category',"
					+ " COALESCE(jc_netamt, '') AS 'Net Amount',"
					+ " COALESCE(jc_discamt, '') AS 'Discount Amount',"
					+ " COALESCE(jc_totaltax, '') AS 'Total Tax',"
					+ " COALESCE(jc_grandtotal, '') AS 'Grand Total',"
					+ " COALESCE(CONCAT(jc_bill_address, ', ', jc_bill_city, ' - ', jc_bill_pin, ', ', jc_bill_state)) AS 'Billing Address',"
					+ " COALESCE(CONCAT(jc_del_address, ', ', jc_del_city, ' - ', jc_del_pin, ', ', jc_del_state)) AS 'Delivery Address',"
					+ " COALESCE(jc_title, '') AS 'Job Card Title',"
					+ " COALESCE(jc_cust_voice, '') AS 'Customer Voice',"
					+ " COALESCE(jc_terms, '') AS 'Terms',"
					+ " COALESCE(jc_instructions, '') AS 'Instructions',"
					+ " COALESCE(priorityjc_name, '') AS 'Priority',"
					+ " COALESCE(CONCAT(emp_name, '(', emp_ref_no, ')'), '') AS 'Service Advisor',"
					// +
					// " COALESCE(CONCAT(tech.emp_name, '(', tech.emp_ref_no, ')'), '') AS 'Technician',"
					+ " COALESCE(DATE_FORMAT(jc_time_in, '%d/%m/%Y'), '') AS 'JobCard Date',"
					+ " COALESCE(DATE_FORMAT(jc_time_promised, '%d/%m/%Y %H:%i'), '') AS 'Promised Time',"
					+ " COALESCE(DATE_FORMAT(jc_time_ready, '%d/%m/%Y %H:%i'), '') AS 'Ready Time',"
					+ " COALESCE(DATE_FORMAT(jc_time_out, '%d/%m/%Y %H:%i'), '') AS 'Delivered Time',"
					+ " COALESCE(DATE_FORMAT(jc_time_posted, '%d/%m/%Y %H:%i'), '') AS 'Posted Invoice Date',"
					+ " COALESCE(IF(jc_active = 1, 'Yes', 'No'), '') AS Active,"
					+ " COALESCE(jc_notes, '') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " WHERE 1 = 1 " + StrSearch + BranchAccess + ExeAccess + ""
					+ " GROUP BY jc_id"
					+ " ORDER BY jc_id";
			SOP("StrSql===jcexport===" + StrSql);
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
		String print = "<option value = JobCardDetails" + StrSelectdrop("JobCardDetails", printoption) + ">Job Card Details</option>\n";
		return print;
	}
}
