package axela.insurance;
/*
 *@author Satish 2nd march 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Insurance_Enquiry_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportpage = "insurance-enquiry-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("insurstrsql", request).equals("")) {
					StrSearch = GetSession("insurstrsql", request);
				}

				if (exportB.equals("Export")) {
					InsuranceEnquiryDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void InsuranceEnquiryDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = " SELECT "
					+ " insurenquiry_id AS 'ID', "
					+ " customer_name AS 'Customer', "
					+ " CONCAT(title_desc, ' ', contact_fname, contact_lname) AS 'Contact', "
					+ " COALESCE(customer_mobile1, '') AS 'Mobile1', "
					+ " COALESCE(customer_mobile2, '') AS 'Mobile2', "
					+ " COALESCE(customer_mobile3, '') AS 'Mobile3', "
					+ " COALESCE(customer_mobile4, '') AS 'Mobile4', "
					+ " COALESCE(customer_mobile5, '') AS 'Mobile5', "
					+ " COALESCE(customer_mobile6, '') AS 'Mobile6', "
					+ " COALESCE(customer_email1, '') AS 'Email1', "
					+ " COALESCE(customer_email2, '') AS 'Email2', "
					+ " COALESCE(customer_phone1, '') AS 'Phone1', "
					+ " COALESCE(customer_phone2, '') AS 'Phone2', "
					+ " CONCAT(customer_address, ', ', city_name, ' - ', customer_pin) AS 'Address', "
					// + " preownedmodel_name AS 'Model', "
					+ " insurenquiry_variant AS 'Variant', "
					+ " insurenquiry_reg_no AS 'Reg. No.', "
					+ " insurenquiry_chassis_no AS 'Vin. No.', "
					+ " insurenquiry_engine_no AS 'Engine No.', "
					+ " insurenquiry_modelyear AS 'Model Year',"
					+ " COALESCE (insurenquiry_previouscompname, '') AS 'Previous Company NAME',"
					+ " COALESCE (insurenquiry_previousyearidv, '') AS 'Previous IDV',"
					+ " COALESCE (insurenquiry_previousgrosspremium , '')AS 'Previous Premium',"
					+ " COALESCE (insurenquiry_previousplanname, '') AS 'Previous Plane Name',"
					+ " COALESCE (insurenquiry_policyexpirydate, '') AS 'Policy Expiry Date',"
					+ " COALESCE (insurenquiry_currentidv, '') AS 'InsuranceCurrentIDV',"
					+ " COALESCE (insurenquiry_premium, '') AS 'Premium',"
					+ " COALESCE (insurenquiry_premiumwithzerodept, '') AS 'Premium With Zero Dept.',"
					+ " COALESCE (insurenquiry_compoffered, '') AS 'Company Offered',"
					+ " COALESCE (insurenquiry_plansuggested, '') AS 'Plane Suggested',"
					+ " COALESCE (insurenquiry_ncb, '') AS 'NCB',"
					+ " IF (insurfollowup_giftcombo = 1, 'Yes', 'No') AS 'Gift Combo',"
					+ " IF (insurfollowup_rs1000coupon = 1, 'Yes', 'No') AS 'Rs. 1000',"
					+ " IF (insurfollowup_servicecoupon = 1, 'Yes', 'No') AS 'Service Coupon',"
					+ " COALESCE(insurenquiry_currentidv,'') AS InsuranceCurrentIDV,"
					+ " COALESCE(DATE_FORMAT(insurenquiry_sale_date,'%d/%m/%Y'), '') AS 'Sale Date', "
					+ " COALESCE(emp_name, '') AS 'Insurance Executive', "
					+ " COALESCE(branch_name, '') AS 'Branch', "
					+ " IF(branch_active=1,'Yes','No') AS 'Active', "
					+ " insurenquiry_notes AS 'Notes' "
					+ " FROM " + compdb(comp_id) + " axela_insurance_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + " axela_branch ON branch_id = insurenquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_title ON title_id = contact_title_id "
					// + " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
					// + " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_city ON city_id = customer_city_id "
					+ " LEFT JOIN " + compdb(comp_id) + " axela_emp ON emp_id = insurenquiry_emp_id "
					+ " WHERE 1 = 1 "
					+ StrSearch + ""
					+ " GROUP BY insurenquiry_id "
					+ " ORDER BY insurenquiry_id DESC";

			SOP("StrSql========" + StrSql);

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
		print = print + "<option value = InsuranceEnquiryDetails" + StrSelectdrop("InsuranceEnquiryDetails", printoption) + ">Insurance Enquiry Details</option>\n";
		return print;
	}
}
