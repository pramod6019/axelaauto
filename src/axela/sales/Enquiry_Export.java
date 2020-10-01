package axela.sales;
//Murali 2nd july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Enquiry_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "", ExeAccess = "";
	public String exportpage = "enquiry-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			// HttpSession session = request.getSession(true);
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
				if (!GetSession("enquirystrsql", request).equals("")) {
					// enquiry_emp_id is passed FROM list enq
					if (GetSession("enquirystrsql", request).contains("enquiry_emp_id")) {
						StrSearch = GetSession("enquirystrsql", request).replace("enquiry_emp_id", "emp.emp_id");
					}
					// emp_id is passed from monitoring board search query
					else {
						StrSearch = GetSession("enquirystrsql", request).replace(" emp_id", " emp.emp_id")
								.replace(" emp_active", " emp.emp_active")
								.replace(" emp_branch_id", " emp.emp_branch_id");
						StrSearch = StrSearch.replace("teamtrans_emp.emp_id", "teamtrans_emp_id");
					}
					StrSearch += BranchAccess + ExeAccess.replace("emp_id", "emp.emp_id");
					// SOP("StrSearch----1----" + StrSearch);
				}
				if (exportB.equals("Export")) {
					EnquiryDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void EnquiryDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT enquiry_id AS ID,"
					+ " customer_name AS 'Customer',"
					+ " COALESCE(title_desc, '') AS 'Contact Title',"
					+ " COALESCE(contact_fname, '') AS 'Contact First Name',"
					+ " COALESCE(contact_lname, '') AS 'Contact Last Name',"
					// + " CONCAT(contact_fname,' ', contact_lname) AS 'Contact',"
					+ " contact_mobile1 AS 'Mobile 1',"
					+ " contact_mobile2 AS 'Mobile 2',"
					+ " contact_email1 AS 'Email 1',"
					+ " contact_email2 AS 'Email 2',"
					+ " IF(enquiry_hyundai_dob != '', date_format(enquiry_hyundai_dob, '%d/%m/%y'), '') AS 'Date Of Birth',"
					+ " COALESCE(contact_pin, '') AS 'Pin Code',"
					+ " CONCAT(customer_address,', ', city_name,', ', customer_pin,IF(customer_landmark!='',CONCAT( ', ', customer_landmark),'') ) AS 'Address',"
					+ " CONCAT('ENQ',enquiry_no) AS 'Enquiry No.',"
					+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.',"
					+ " COALESCE(concat(manemp.emp_name,' (', manemp.emp_ref_no, ')'),'') AS 'Team Leader',"
					+ " CONCAT(branch_name,' (', branch_code, ')') AS 'Branch',"
					+ " COALESCE(region_name, '') AS 'Region',"
					+ " IF(enquiry_date != '',DATE_FORMAT(enquiry_date, '%d/%m/%Y'),'') AS 'Date',"
					+ " IF(enquiry_close_date != '',DATE_FORMAT(enquiry_close_date, '%d/%m/%Y'),'') AS 'Closing Date',"
					+ " IF(so_delivered_date != '', DATE_FORMAT(so_delivered_date, '%d/%m/%y'), '') AS 'Delivery Date',"
					+ " COALESCE(enquiry_title,'') AS 'Title',"
					+ " COALESCE(enquiry_value,'') AS Value,"
					+ " COALESCE(enquiry_desc,'') AS Description,"
					+ " COALESCE(emp.emp_name,'') AS 'Sales Consultant Name',"
					+ " COALESCE(main.model_name,'') AS Model,"
					+ " COALESCE(item_name,'') AS Variant,"
					+ " COALESCE(additional.model_name,'') AS 'Additional Model',"
					+ " COALESCE(enquiry_hyundai_currentcars, '') AS 'Previous Car',"
					+ " COALESCE(enquirycat_name, '') AS 'Category',"
					+ " COALESCE(corporate_name, '') AS 'Corporate',"
					+ " COALESCE(option_name,'') AS 'Colour',"
					+ " COALESCE((SELECT custtype_name"
					+ " FROM " + compdb(comp_id)
					+ "axela_sales_enquiry_add_custtype WHERE custtype_id = enquiry_custtype_id), '') AS 'Type of Customer',"
					+ " COALESCE((SELECT occ_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_occ WHERE occ_id = enquiry_occ_id), '') AS 'Occupation',"
					+ " COALESCE(enquiry_fuelallowance,'') AS 'Fuel Allowance',"
					+ " COALESCE((SELECT monthkms_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_monthkms"
					+ " WHERE monthkms_id = enquiry_monthkms_id), '') AS 'How many kilometers you drive in a month',"
					+ " COALESCE((SELECT purchasemode_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_purchasemode"
					+ " WHERE purchasemode_id = enquiry_purchasemode_id), '') AS 'What will be your mode of purchase?',"
					+ " COALESCE((SELECT income_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_income"
					+ " WHERE income_id = enquiry_income_id), '') AS 'What is your approximate annual household income (rs)?',"
					+ " COALESCE(enquiry_familymember_count,'') AS 'How many members are there in your family?',"
					+ " COALESCE((SELECT expectation_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_expectation"
					+ " WHERE expectation_id = enquiry_expectation_id), '') AS 'What is top most priority expectations FROM the car?',"
					+ " COALESCE(enquiry_othercar,'') AS 'Any other car you have in mind?',"
					+ " COALESCE((SELECT buyertype_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_buyertype"
					+ " WHERE buyertype_id = enquiry_buyertype_id), '') AS 'Type of buyer',"
					+ " COALESCE((SELECT ownership_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_add_ownership"
					+ " WHERE ownership_id = enquiry_ownership_id), '') AS 'Ownership',"
					+ " COALESCE(enquiry_existingvehicle,'') AS 'Existing Vehicle Make',"
					+ " COALESCE(enquiry_purchASemonth,'') AS 'PurchASe Month',"
					+ " COALESCE(enquiry_loancompletionmonth,'') AS 'Loan Completion',"
					+ " COALESCE(enquiry_currentemi,'') AS 'Current EMI',"
					+ " COALESCE(enquiry_loanfinancer,'') AS 'Financer Name',"
					+ " COALESCE(enquiry_kms,'') AS 'Kms',"
					+ " COALESCE(enquiry_expectedprice,'') AS 'Expected Price',"
					+ " COALESCE(enquiry_quotedprice,'') AS 'Quoted Price',"

					// /////////////////////MB fields Starts
					+ " COALESCE(enquiry_mb_occupation, '') AS 'Occupation',"
					+ " COALESCE(enquiry_mb_carusage, '') AS 'Car Usage',"
					+ " COALESCE(enquiry_mb_type, '') AS 'Car Type',"
					+ " COALESCE(enquiry_mb_drivingpattern, '') AS 'Driving Pattern',"
					+ " COALESCE(enquiry_mb_income, '') AS 'Income',"
					+ " COALESCE(enquiry_mb_avgdriving, '') AS 'Average Driving',"
					+ " COALESCE(enquiry_mb_currentcars,'') AS 'Current Cars',"
					// /////////////////////MB fields Ends

					+ " COALESCE((SELECT status_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_status"
					+ " WHERE status_id = enquiry_status_id),'') AS 'Status Name',"
					+ " COALESCE((SELECT stage_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_enquiry_stage"
					+ " WHERE stage_id = enquiry_stage_id),'') AS 'Stage Name',"
					+ " COALESCE(enquiry_status_desc,'') AS 'Status Description',"

					// Added Home Visit Column

					+ " COALESCE (( SELECT 'Done'"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " WHERE followup_enquiry_id = enquiry_id"
					+ " AND followup_feedbacktype_id = 9 LIMIT 1),"
					+ " 'Not Done') AS 'Home Visit',"

					+ " IF(testdrive_fb_taken = 1,'Taken','Not Taken') AS 'Test Drive',"
					+ " COALESCE((SELECT lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE lostcase1_id = enquiry_lostcase1_id),'') AS 'Lost Case1',"
					+ " COALESCE((SELECT lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_id = enquiry_lostcase2_id),'') AS 'Lost Case2',"
					+ " COALESCE((SELECT lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_id = enquiry_lostcase3_id),'') AS 'Lost Case3',"
					+ " COALESCE(priorityenquiry_name,'') AS 'Priority Name',"
					+ " COALESCE((SELECT campaign_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " WHERE campaign_id = enquiry_campaign_id),'') AS 'Campaign Name',"
					+ " COALESCE((SELECT soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " WHERE soe_id = enquiry_soe_id),'') AS 'Source Of Enquiry',"
					+ " COALESCE((SELECT sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " WHERE sob_id = enquiry_sob_id),'') AS 'Source Of Business',"
					+ " COALESCE(enquiry_qcsno,'') AS 'QCS No.',"
					+ " COALESCE((SELECT emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = enquiry_entry_id),'') AS 'Entry By',"
					+ " COALESCE((SELECT DATE_FORMAT(followup_entry_time, '%d/%m/%Y %H:%i')"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " WHERE followup_enquiry_id = enquiry_id AND followup_desc = '' LIMIT 1), '') AS 'Last Follow-up Entry Time',"
					+ " COALESCE(fueltype_name, '') AS 'Fuel Type',"
					+ " COALESCE(enquiry_notes,'') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " LEFT JOIN  axela_sales_enquiry_cat ON enquirycat_id = enquiry_enquirycat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model main ON main.model_id = enquiry_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_corporate ON corporate_id = enquiry_corporate_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id "
					+ " LEFT join " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT join " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id "
					+ " LEFT join " + compdb(comp_id) + "axela_emp manemp on manemp.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model additional ON additional.model_id = enquiry_add_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = enquiry_option_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id"
					+ " WHERE 1=1 " + StrSearch + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC" + " "
					+ " LIMIT " + exportcount;
			// SOP("StrSql--------enq export----" + StrSql);

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
		print = print + "<option value = EnquiryDetails" + StrSelectdrop("EnquiryDetails", printoption) + ">Enquiry Details</option>\n";
		return print;
	}
}
