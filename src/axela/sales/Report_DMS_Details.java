package axela.sales;
// dilip kumar

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_DMS_Details extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String dmsstartdate = "", dms_start_date = "";
	public String dmsenddate = "", dms_end_date = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String branch_id = "", brand_id = "", region_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String filterby_id = "";
	public String BranchAccess = "";
	public String dr_branch_id = "0";
	public String msg = "", ExeAccess = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				exportB = PadQuotes(request.getParameter("btn_export"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));

				if (dms_start_date.equals("")) {
					dms_start_date = strToLongDate(ToLongDate(kknow()));
				}

				if (dms_end_date.equals("")) {
					dms_end_date = strToLongDate(ToLongDate(kknow()));
				}

				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				if (exportB.equals("Export")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						DMSReport(request, response);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		dmsstartdate = PadQuotes(request.getParameter("txt_dms_start_date"));
		dmsenddate = PadQuotes(request.getParameter("txt_dms_end_date"));
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = PadQuotes(request.getParameter("dr_principal"));
		// brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		branch_ids = request.getParameterValues("dr_branch");

		filterby_id = PadQuotes(request.getParameter("dr_filterby"));

	}

	protected void CheckForm() {
		msg = "";

		if (dmsstartdate.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!dmsstartdate.equals("")) {
			if (isValidDateFormatLong(dmsstartdate)) {
				dmsstartdate = ConvertLongDateToStr(dmsstartdate);
				dms_start_date = strToLongDate(dmsstartdate);
			} else {
				msg = msg + "<br>Enter Valid Start Time!";
				dmsstartdate = "";
			}
		}
		if (dmsenddate.equals("")) {
			msg = msg + "<br>Select End Time!<br>";
		}
		if (!dmsenddate.equals("")) {
			if (isValidDateFormatLong(dmsenddate)) {
				dmsenddate = ConvertLongDateToStr(dmsenddate);
				if (!dmsstartdate.equals("") && !dmsenddate.equals("") && Long.parseLong(dmsstartdate) > Long.parseLong(dmsenddate)) {
					msg = msg + "<br>Start Time should be less than End Time!";
				}
				dms_end_date = strToLongDate(dmsenddate);

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				dmsenddate = "";
			}
			if (!dmsstartdate.equals("")) {
				if (getDaysBetween(dmsstartdate, dmsenddate) > 31) {
					msg = msg + "<br>Date duration cannot exceed 31 Days!";
				}
			}
		}

		if (brand_id.equals("")) {
			msg += msg + "<br>Select Brand!";
		}
		if (branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (!brand_id.equals("0")) {
			StrSearch += " AND branch_brand_id = " + brand_id + "";
		}
		if (!region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + region_id + ") ";
		}

		if (!branch_id.equals("")) {
			StrSearch += " AND enquiry_branch_id IN(" + branch_id + ")";
		}
		// else {
		// msg = msg + "<br>Select Branch!";
		// }
	}

	public void DMSReport(HttpServletRequest request, HttpServletResponse response) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT enquiry_id AS ID,"
					+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.', customer_name AS 'Customer Name',"
					+ " COALESCE(title_desc, '') AS 'Contact Title',"
					+ " COALESCE(contact_fname, '') AS 'Contact First Name',"
					+ " COALESCE(contact_lname, '') AS 'Contact Last Name',"
					// + " CONCAT(title_desc, ' ',contact_fname, ' ',contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS Mobile1, contact_mobile2 AS Mobile2,"
					+ " contact_phone1 AS Phone1, contact_phone2 AS Phone2, contact_email1 AS Email1,"
					+ " contact_email2 AS Email2, "
					+ " IF(enquiry_hyundai_dob != '', date_format(enquiry_hyundai_dob, '%d/%m/%y'), '') AS 'Date Of Birth',"
					+ " contact_address AS Address,"
					+ " IF (contact_address != '',city_name,'') AS 'City',"
					+ " IF (contact_address != '',contact_pin,'') AS 'Pin Code',"
					+ " IF (contact_address != '',state_name,'') AS 'State',"
					// + " COALESCE(city_name, '') AS 'City',"
					// + " COALESCE(contact_pin, '') AS 'Pin Code',"
					// + " COALESCE(state_name, '') AS 'State',"
					+ " enquiry_no AS 'Enquiry No.', branch_name AS Branch,"
					+ " IF(enquiry_date != '', date_format(enquiry_date, '%d/%m/%y'), '') AS 'Date',"
					+ " IF(enquiry_close_date != '', date_format(enquiry_close_date, '%d/%m/%y'), '') AS 'Closing Date',"
					+ " COALESCE((SELECT IF(so_delivered_date != '', date_format(so_delivered_date, '%d/%m/%y'), '') "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_enquiry_id = enquiry_id),'') AS 'Delivery Date',"

					+ " COALESCE(enquiry_title, '') AS Title, COALESCE(enquiry_value, '0') AS Value,"
					+ " COALESCE(enquiry_desc, '') AS Description,"
					+ " COALESCE(enq.emp_name,'') AS 'Employee Name',"
					+ " concat(manemp.emp_name,' (', manemp.emp_ref_no, ')') AS 'Team Leader',"
					+ " model_name AS Model, item_name AS Item,"
					+ " COALESCE((select option_name FROM " + compdb(comp_id) + "axela_vehstock_option WHERE option_id = enquiry_option_id), '') AS 'Colour',"
					+ " COALESCE((select age_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age WHERE age_id = enquiry_age_id), '') AS 'Age',"
					+ " COALESCE((select occ_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ WHERE occ_id = enquiry_occ_id), '') AS 'Occupation',"
					+ " COALESCE((select custtype_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype WHERE custtype_id = enquiry_custtype_id), '') AS 'Type of Customer',"
					+ " COALESCE(enquiry_fuelallowance, '') AS 'Fuel Allowance',"
					+ " COALESCE(enquirycat_name, '') AS 'Category',"
					+ " COALESCE(corporate_name, '') AS 'Corporate',";

			if (brand_id.equals("2"))
			{
				// Understand Your Customer fields for maruthi
				StrSql += " COALESCE((select monthkms_name FROM " + compdb(comp_id)
						+ "axela_sales_enquiry_add_monthkms WHERE monthkms_id = enquiry_monthkms_id), '') AS 'How many kilometers you drive in a month',"
						+ " COALESCE((select purchasemode_name FROM " + compdb(comp_id)
						+ "axela_sales_enquiry_add_purchasemode WHERE purchasemode_id = enquiry_purchasemode_id), '') AS 'What will be your mode of purchase?',"
						+ " COALESCE((select income_name FROM " + compdb(comp_id)
						+ "axela_sales_enquiry_add_income WHERE income_id = enquiry_income_id), '') AS 'What is your approximate annual household income (rs)?',"
						+ " COALESCE(enquiry_familymember_count, '0') AS 'How many members are there in your family?',"
						+ " COALESCE((select expectation_name FROM " + compdb(comp_id)
						+ "axela_sales_enquiry_add_expectation WHERE expectation_id = enquiry_expectation_id), '') AS 'What is top most priority expectations FROM the car?',"
						+ " COALESCE(enquiry_othercar, '') AS 'Any other car you have in mind?',"

						// Exchange Details for maruthi
						+ " COALESCE((select buyertype_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype WHERE buyertype_id = enquiry_buyertype_id), '') AS 'Type of buyer',"
						+ " COALESCE((select ownership_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_ownership WHERE ownership_id = enquiry_ownership_id), '') AS 'Ownership',"
						+ " COALESCE(enquiry_existingvehicle, '') AS 'Existing Vehicle Make',"
						+ " COALESCE(enquiry_purchasemonth, '') AS 'Purchase Month', COALESCE(enquiry_loancompletionmonth, '') AS 'Loan Completion',"
						+ " COALESCE(enquiry_currentemi, '0') AS 'Current EMI', COALESCE(enquiry_loanfinancer, '') AS 'Financer Name',"
						+ " COALESCE(enquiry_kms, '0') AS Kms, COALESCE(enquiry_expectedprice, '0') AS 'Expected Price',"
						+ " COALESCE(enquiry_quotedprice, '0') AS 'Quoted Price',";

			}
			if (brand_id.equals("6"))
			{
				// Need Assessment fields for Hyundai
				StrSql += " COALESCE(enquiry_hyundai_currentcars, '') AS 'Previous Car',"
						+ "enquiry_hyundai_chooseoneoption AS 'Please choose one option',"
						+ "enquiry_hyundai_kmsinamonth AS 'How many kilometers you drive in a month?',"
						+ "enquiry_hyundai_membersinthefamily AS 'How many members are there in your family?',"
						+ "enquiry_hyundai_topexpectation AS 'What is your top most priority expectation from your car?',"
						+ "enquiry_hyundai_finalizenewcar AS 'By when are you expecting to finalize your new car?',"
						+ "enquiry_hyundai_modeofpurchase AS 'What will be your mode of purchase?',"
						+ "enquiry_hyundai_annualincome AS 'What is your appropriate annual household income (INR)?',"
						+ "enquiry_hyundai_othercars AS 'Which other cars are you considering?',";
				// Exchange Details for Hyundai
				// StrSql += ""
				// + "enquiry_hyundai_ex_model AS 'Model / Variant',"
				// + "enquiry_hyundai_ex_manufyear AS 'Year of Manufacture',"
				// + "enquiry_hyundai_ex_purchasedate AS 'Purchase Month / Year',"
				// + "enquiry_hyundai_ex_owner AS 'Owner',"
				// + "enquiry_hyundai_ex_loancompletion AS 'Loan completion Month / Year (if any)',"
				// + "enquiry_hyundai_ex_kmsdone AS 'Kms. Done',"
				// + "enquiry_hyundai_ex_financer AS 'Financer Name',"
				// + "enquiry_hyundai_ex_expectedprice AS 'Expected Price (INR)(A)',"
				// + "enquiry_hyundai_ex_quotedprice AS 'Quoted Price (INR)(B)',";
			}
			if (brand_id.equals("7"))
			{
				// Need Assessment fields for ford
				StrSql += "enquiry_ford_customertype AS 'Type of Customer',"
						+ "enquiry_ford_intentionpurchase AS 'Intention to purchase',"
						+ "enquiry_ford_kmsdriven AS 'No. of Kms Driven Every Day?',"
						+ "enquiry_ford_newvehfor AS 'New Vehicle for Self or Someone else?',"
						+ "enquiry_ford_investment AS 'Amount of investment in new car?',"
						+ "enquiry_ford_colourofchoice AS 'Any specific colour choice?',"
						+ "enquiry_ford_cashorfinance AS 'Cash / Finance?',"
						+ "enquiry_ford_currentcar AS 'Which Car you Driving now?',"
						+ "enquiry_ford_exchangeoldcar AS 'Do you want to Exchange your old car?',"
						+ "enquiry_ford_othercarconcideration AS 'Which Other cars you considering?',";

				// Trade-in Details fields for ford

				StrSql += "enquiry_ford_ex_make AS 'Make',"
						+ "enquiry_ford_ex_model AS 'Model',"
						+ "enquiry_ford_ex_derivative AS 'Derivative',"
						+ "enquiry_ford_ex_year AS 'Year',"
						+ "enquiry_ford_ex_odoreading AS 'Odo KM reading',"
						+ "enquiry_ford_ex_doors AS 'Doors',"
						+ "enquiry_ford_ex_bodystyle AS 'Body Style',"
						+ "enquiry_ford_ex_enginesize AS 'Engine Size',"
						+ "enquiry_ford_ex_fueltype AS 'Fuel Type',"
						+ "enquiry_ford_ex_drive AS 'Drive',"
						+ "enquiry_ford_ex_transmission AS 'Transmission',"
						+ "enquiry_ford_ex_colour AS 'Colour',"
						+ "enquiry_ford_ex_priceoffered AS 'Price Offered',"
						+ "enquiry_ford_ex_estmtprice AS 'Estimated Price',";
			}
			if (brand_id.equals("10"))
			{
				// Customer Profile fields for Maruti-Nexa
				StrSql += "enquiry_nexa_gender AS 'Gender',"
						+ "enquiry_nexa_beveragechoice AS 'Beverage Choice',"
						+ "enquiry_nexa_autocard AS 'Interested in Autocard',"
						+ "enquiry_nexa_fueltype AS 'Fuel Type',"
						+ "enquiry_nexa_specreq AS 'Specific requirement from the car',"
						+ "enquiry_nexa_testdrivereq AS 'Test Drive Required',"
						+ "enquiry_nexa_testdrivereqreason AS 'Reason',"

						// // Exchange Details for Maruti-Nexa
						+ " COALESCE((select buyertype_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype WHERE buyertype_id = enquiry_buyertype_id), '') AS 'Type of buyer',"
						+ " COALESCE((select ownership_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_ownership WHERE ownership_id = enquiry_ownership_id), '') AS 'Ownership',"
						+ " COALESCE(enquiry_existingvehicle, '') AS 'Existing Vehicle Make',"
						+ " COALESCE(enquiry_purchasemonth, '') AS 'Purchase Month', COALESCE(enquiry_loancompletionmonth, '') AS 'Loan Completion',"
						+ " COALESCE(enquiry_currentemi, '0') AS 'Current EMI', COALESCE(enquiry_loanfinancer, '') AS 'Financer Name',"
						+ " COALESCE(enquiry_kms, '0') AS Kms, COALESCE(enquiry_expectedprice, '0') AS 'Expected Price',"
						+ " COALESCE(enquiry_quotedprice, '0') AS 'Quoted Price',";
			}
			if (brand_id.equals("11"))
			{
				// Need Assesment fields for Skoda
				StrSql += " COALESCE(na_skoda_ownbusiness, '') AS 'Own a Business',"
						+ " COALESCE(na_skoda_companyname, '') AS 'Company Name',"
						+ " COALESCE(contact_jobtitle, '') AS 'Job Title',"
						+ " COALESCE(na_skoda_financerequired, '') AS 'Finance Required',"
						+ " IF(enquiry_close_date != '', date_format(enquiry_close_date, '%d/%m/%y'), '') AS 'How soon is the purchase',"
						+ " COALESCE(GROUP_CONCAT(CONCAT(curr.carmanuf_name,' - ',cur.preownedmodel_name,' - ',variant_name)), '') AS 'Current Cars',"
						+ " COALESCE(na_skoda_currentcarappxkmsrun, '') AS 'Approximate kms run',"
						+ " COALESCE(na_skoda_whatareyoulookingfor, '') AS 'What are you looking for in your car',"
						+ " COALESCE(na_skoda_numberoffamilymembers, '') AS 'Number of Family Members',"
						+ " COALESCE(na_skoda_whowilldrive, '') AS 'Who will drive the car',"
						+ " COALESCE(na_skoda_whoareyoubuyingfor, '') AS 'Who are you buying the car for',"
						+ " COALESCE(na_skoda_newcarappxrun, '') AS 'Approximately how many kms in a day will the car be run',"
						+ " COALESCE(na_skoda_wherewillbecardriven, '') AS 'Where will the car be driven mostly',";
			}
			if (brand_id.equals("56"))
			{
				// Need Assesment fields for Porsche
				StrSql += " enquiry_porsche_gender AS 'Gender',"
						+ " enquiry_porsche_nationality AS 'Nationality',"
						+ " enquiry_porsche_language AS 'Language',"
						+ " enquiry_porsche_religion AS 'Religion',"
						+ " enquiry_porsche_preferredcomm AS 'Preferred Communication',"
						+ " enquiry_porsche_socialmediapref AS 'Social Media Preference',"
						+ " enquiry_porsche_maritalstatus AS 'Marital Status',"
						+ " enquiry_porsche_spousename AS 'Spouse Name',"
						+ " enquiry_porsche_spousedrive AS 'Does Spouse Drive',"
						+ " enquiry_porsche_interest AS 'Interests',"
						+ " enquiry_porsche_clubmembership AS 'Club Membership',"
						+ " enquiry_porsche_financeoption AS 'Interested In Financing',"
						+ " enquiry_porsche_insuranceoption AS 'Interested In Insurance',"
						+ " enquiry_porsche_vehicleinhouse AS 'Number Of Vehicle In House',"
						+ " COALESCE(GROUP_CONCAT(CONCAT(curr.carmanuf_name,' - ',cur.preownedmodel_name,' - ',variant_name)), '') AS 'Current Cars',"
						+ " enquiry_porsche_householdcount AS 'Persons In Household',"
						+ " IF(contact_dob != '', date_format(contact_dob, '%d/%m/%y'), '') AS 'DOB',"
						+ " IF(contact_anniversary != '', date_format(contact_anniversary, '%d/%m/%y'), '') AS 'Anniversary',"
						+ " enquiry_porsche_industry AS 'Industry',";

			}
			if (brand_id.equals("60"))
			{
				// Need Assesment fields for JLR
				StrSql += " enquiry_jlr_employmentstatus AS 'Employement Status',"
						+ " enquiry_jlr_industry AS 'Industry',"
						+ " IF(contact_dob != '', date_format(contact_dob, '%d/%m/%y'), '') AS 'Birthday',"
						+ " enquiry_jlr_gender AS 'Gender',"
						+ " enquiry_jlr_occupation AS 'Occupation',"

						+ " COALESCE ((SELECT GROUP_CONCAT(CONCAT( curr.carmanuf_name, ' - ', cur.preownedmodel_name, ' - ', variant_name ) )"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " LEFT JOIN axela_preowned_variant ON variant_id = currentcars_variant_id"
						+ " LEFT JOIN axela_preowned_model cur ON cur.preownedmodel_id = variant_preownedmodel_id"
						+ " LEFT JOIN axela_preowned_manuf curr ON curr.carmanuf_id = cur.preownedmodel_carmanuf_id"
						+ " WHERE 1 = 1"
						+ " AND currentcars_enquiry_id = enquiry_id),'') AS 'Current Vehicles',"

						+ " COALESCE(GROUP_CONCAT(CONCAT(othr.carmanuf_name,' - ',oth.preownedmodel_name)), '') AS 'Other Model Interest',"
						+ " IF(enquiry_jlr_financeinterest = 1, 'Yes', 'No' ) AS 'Finance Interest',"
						+ " enquiry_jlr_noofchildren AS 'No. of Children',"
						+ " enquiry_jlr_noofpeopleinhousehold AS 'No. of People in Household',"
						+ " IF(enquiry_jlr_highnetworth = 1, 'Yes', 'No' ) AS 'High Net Worth',"
						+ " enquiry_jlr_householdincome AS 'Household Income',"
						+ " enquiry_jlr_interests AS 'Interests',"
						+ " enquiry_jlr_annualrevenue AS 'Annual Revenue',"
						+ " enquiry_jlr_noofemployees AS 'No. of Employees',"
						+ " enquiry_jlr_accounttype AS 'Account Type',"
						+ " enquiry_jlr_enquirystatus AS 'Enquiry Status',"
						+ " COALESCE((SELECT feedbacktype_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type ON feedbacktype_id = followup_feedbacktype_id"
						+ " WHERE followup_enquiry_id = enquiry_id"
						+ " AND followup_feedbacktype_id != 0"
						+ " ORDER BY followup_id DESC LIMIT 1), '') AS 'Action Taken',"
						+ " IF (testdrive_fb_taken != '1',date_format(testdrive_time,'%d/%m/%Y %H:%i'),'') AS 'Test Drive Planned Time',"
						+ " IF (testdrive_fb_taken != '1',testdriveveh_name,'') AS 'Test Drive Planned Variant',"
						+ " IF (testdrive_fb_taken != '1',location_name,'') AS 'Test Drive Planned Location',"
						+ " IF (testdrive_fb_taken = '1',testdrivestatus.status_name,'') AS 'Test Drive Feedback Status',";

			}
			// ////////////MB///////
			if (brand_id.equals("55"))
			{
				// /////////////////////MB fields Starts
				StrSql += " COALESCE(enquiry_mb_occupation, '') AS 'Occupation',"
						+ " COALESCE(enquiry_mb_carusage, '') AS 'Car Usage',"
						+ " COALESCE(enquiry_mb_type, '') AS 'Car Type',"
						+ " COALESCE(enquiry_mb_drivingpattern, '') AS 'Driving Pattern',"
						+ " COALESCE(enquiry_mb_income, '') AS 'Income',"
						+ " COALESCE(enquiry_mb_avgdriving, '') AS 'Average Driving',"
						+ " COALESCE(enquiry_mb_currentcars,'') AS 'Current Cars',";
				// /////////////////////MB fields Ends
			}
			StrSql += " enquirystatus.status_name AS Status,"
					+ " stage_name AS Stage, COALESCE(enquiry_status_desc, '') AS 'Status Description',"
					+ " priorityenquiry_name AS 'Priority Name',"
					+ " COALESCE(soe_name, '') AS 'Source Of Enquiry', COALESCE(sob_name, '') AS 'Source Of Business',"
					+ " COALESCE(campaign_name, '') AS 'Campaign Name',"
					+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.', "
					+ " COALESCE(enquiry_notes, '') AS 'Notes',";

			StrSql += " IF(followup_followup_time != '',DATE_FORMAT(followup_followup_time, '%d/%m/%Y %H:%i'),'') AS 'Follow-up Time', "
					+ " COALESCE(followuptype_name, '') AS 'Follow-up Type',"
					+ " followup_desc AS 'Follow-up Description',"
					// + " COALESCE(followupdesc_name, '') AS 'Feedback Type',"
					+ " COALESCE((SELECT DATE_FORMAT(followupnext.followup_followup_time, '%d/%m/%Y %H:%i') "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup AS followupnext"
					+ " WHERE followupnext.followup_enquiry_id = enquiry_id"
					+ " AND followupnext.followup_followup_time > " + compdb(comp_id) + "axela_sales_enquiry_followup.followup_followup_time limit 1), '') AS 'Next Follow-up',"
					// +
					// " COALESCE(crmfollowup_followup_time, '') AS 'CRM Follow-up Time',"
					// +
					// " COALESCE(crmfollowupdays_daycount, '') AS 'CRM Follow-up Days',"
					// +
					// " COALESCE(crmfeedbacktype_name, '') AS 'CRM Feedback Type',"
					// +
					// " COALESCE(crmfollowup_desc, '') AS 'CRM Follow-up Description',"
					+ " COALESCE(testdriveveh_name, '') AS 'Test Drive Taken Model',"
					+ " COALESCE(entry.emp_name,'') AS 'Entry By',"
					+ " IF(enquiry_entry_date != '', date_format(enquiry_entry_date, '%d/%m/%y'), '') AS 'Entry Date'";

			if (brand_id.equals("60")) {
				StrSql = DMSReportJLR();
			}

			StrSql += " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp enq ON enq.emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status enquirystatus ON enquirystatus.status_id = enquiry_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup ON followup_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = enquiry_entry_id"

					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_na ON na_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_corporate ON corporate_id = enquiry_corporate_id "
					+ " LEFT JOIN  axela_sales_enquiry_cat ON enquirycat_id = enquiry_enquirycat_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manemp ON manemp.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive ON testdrive_enquiry_id = enquiry_id "
					+ " AND SUBSTR(testdrive_time,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
					+ " AND SUBSTR(testdrive_time,1,12) <= '" + dmsenddate.substring(0, 12) + "'"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_enquiry_id = enquiry_id "
					// + " LEFT JOIN axela_preowned_variant ON variant_id = currentcars_variant_id"
					// + " LEFT JOIN axela_preowned_model cur ON cur.preownedmodel_id = variant_preownedmodel_id"
					// + " LEFT JOIN axela_preowned_manuf curr ON curr.carmanuf_id = cur.preownedmodel_carmanuf_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_othermodels ON othermodels_enquiry_id = enquiry_id "
					+ " LEFT JOIN axela_preowned_model oth ON oth.preownedmodel_id = othermodels_preownedmodel_id"
					+ " LEFT JOIN axela_preowned_manuf othr ON othr.carmanuf_id = oth.preownedmodel_carmanuf_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id";

			if (brand_id.equals("60")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status testdrivestatus ON testdrivestatus.status_id = testdrive_fb_status_id";
			}

			StrSql += " WHERE enquiry_id > 0";
			// + " AND SUBSTR(followup_followup_time,1,12) >= " + dmsstartdate.substring(0, 12)
			// + " AND SUBSTR(followup_followup_time,1,12) <= " + dmsenddate.substring(0, 12)
			if (filterby_id.equals("newenquiry")) {
				StrSql += " AND ((SUBSTR(CONCAT(SUBSTR(enquiry_date,1,8),'',SUBSTR(enquiry_entry_date,9,6)),1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(CONCAT(SUBSTR(enquiry_date,1,8),'',SUBSTR(enquiry_entry_date,9,6)),1,12) <= '" + dmsenddate.substring(0, 12) + "'))";
			} else if (filterby_id.equals("follow-up")) {
				StrSql += " AND ((SUBSTR(followup_followup_time,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(followup_followup_time,1,12) <= '" + dmsenddate.substring(0, 12) + "')"
						+ " OR (SUBSTR(followup_modified_time,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(followup_modified_time,1,12) <= '" + dmsenddate.substring(0, 12) + "')"
						+ " OR (SUBSTR(enquiry_status_date,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(enquiry_status_date,1,12) <= '" + dmsenddate.substring(0, 12) + "'))"
						+ " AND SUBSTR(enquiry_date, 1, 12) < " + dmsstartdate.substring(0, 12);
			} else if (filterby_id.equals("testdrive")) {
				StrSql += " AND testdrive_fb_taken = 1 "
						+ " AND ((SUBSTR(testdrive_fb_entry_date,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(testdrive_fb_entry_date,1,12) <= '" + dmsenddate.substring(0, 12) + "'))";
			} else {
				StrSql += " AND ((SUBSTR(followup_followup_time,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(followup_followup_time,1,12) <= '" + dmsenddate.substring(0, 12) + "')"
						+ " OR (SUBSTR(followup_modified_time,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(followup_modified_time,1,12) <= '" + dmsenddate.substring(0, 12) + "')"
						+ " OR (SUBSTR(enquiry_status_date,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(enquiry_status_date,1,12) <= '" + dmsenddate.substring(0, 12) + "')"
						+ " OR (SUBSTR(enquiry_lastupdatetime,1,12) >= '" + dmsstartdate.substring(0, 12) + "'"
						+ " AND SUBSTR(enquiry_lastupdatetime,1,12) <= '" + dmsenddate.substring(0, 12) + "'))";
			}

			StrSql += BranchAccess + StrSearch
					+ " GROUP BY enquiry_id"
					+ " ORDER BY followup_followup_time LIMIT 3000";
			SOP("StrSql====" + StrSql);
			crs = processQuery(StrSql, 0);
			new ExportToXLSX().Export(request, response, crs, "DMSReport", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String DMSReportJLR() {
		StrSql = "SELECT enquiry_id AS 'Enquiry ID',"
				+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.',"
				+ " enquiry_jlr_accounttype AS 'Account Type',"
				+ " customer_name AS 'Customer Name',"
				+ " COALESCE(title_desc, '') AS 'Contact Title',"
				+ " COALESCE(contact_fname, '') AS 'Contact First Name',"
				+ " COALESCE(contact_lname, '') AS 'Contact Last Name',"
				+ " COALESCE((select custtype_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype WHERE custtype_id = enquiry_custtype_id), '') AS 'Type of Customer',"
				+ " enquiry_jlr_employmentstatus AS 'Employement Status',"
				+ " enquiry_jlr_industry AS 'Industry',"
				+ " IF(enquiry_hyundai_dob != '', date_format(enquiry_hyundai_dob, '%d/%m/%y'), '') AS 'Date Of Birth',"
				+ " enquiry_jlr_gender AS 'Gender',"
				+ " enquiry_jlr_occupation AS 'Occupation',"
				+ " enquiry_jlr_enquirystatus AS 'Lead Status',"
				+ " COALESCE(soe_name, '') AS 'Source Of Enquiry',"
				+ " COALESCE(campaign_name, '') AS 'Campaign Name',"
				+ " COALESCE(enquiry_desc, '') AS Description,"

				+ " COALESCE((SELECT feedbacktype_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type ON feedbacktype_id = followup_feedbacktype_id"
				+ " WHERE followup_enquiry_id = enquiry_id"
				+ " AND followup_feedbacktype_id != 0"
				+ " ORDER BY followup_id DESC LIMIT 1), '') AS 'Action Taken',"

				+ " contact_mobile1 AS Mobile1, contact_mobile2 AS Mobile2,"
				+ " contact_email1 AS Email1,"
				+ " COALESCE(enquiry_title, '') AS Title,"
				+ " model_name AS Model, item_name AS Item,"
				+ " COALESCE((select option_name FROM " + compdb(comp_id) + "axela_vehstock_option WHERE option_id = enquiry_option_id), '') AS 'Colour',"
				+ " COALESCE(enquiry_value, '0') AS Budget,"

				+ " COALESCE ((SELECT GROUP_CONCAT(CONCAT( curr.carmanuf_name, ' - ', cur.preownedmodel_name, ' - ', variant_name ) )"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
				+ " LEFT JOIN axela_preowned_variant ON variant_id = currentcars_variant_id"
				+ " LEFT JOIN axela_preowned_model cur ON cur.preownedmodel_id = variant_preownedmodel_id"
				+ " LEFT JOIN axela_preowned_manuf curr ON curr.carmanuf_id = cur.preownedmodel_carmanuf_id"
				+ " WHERE 1 = 1"
				+ " AND currentcars_enquiry_id = enquiry_id),'') AS 'Current Vehicles',"

				+ " COALESCE ((SELECT GROUP_CONCAT(CONCAT(othercarmanuf.carmanuf_name, ' - ', othervariant.preownedmodel_name))"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_othermodels"
				+ " LEFT JOIN axela_preowned_model othervariant ON othervariant.preownedmodel_id = othermodels_preownedmodel_id"
				+ " LEFT JOIN axela_preowned_manuf othercarmanuf ON othercarmanuf.carmanuf_id = othervariant.preownedmodel_carmanuf_id"
				+ " WHERE 1 = 1"
				+ " AND othermodels_enquiry_id = enquiry_id),'') AS 'Other Model Interest',"

				+ " enquiry_jlr_interests AS 'Interests',"
				+ " IF(enquiry_jlr_financeinterest = 1, 'Yes', 'No' ) AS 'Finance Interest',"
				+ " contact_address AS Address,"
				+ " IF (contact_address != '',city_name,'') AS 'City',"
				+ " IF (contact_address != '',contact_pin,'') AS 'Pin Code',"
				+ " IF (contact_address != '',state_name,'') AS 'State',"
				+ " contact_phone1 AS Phone1, contact_phone2 AS Phone2,"
				+ " contact_email2 AS Email2, "

				+ " COALESCE((select age_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age WHERE age_id = enquiry_age_id), '') AS 'Age',"
				+ " enquiry_jlr_noofchildren AS 'No. of Children',"
				+ " enquiry_jlr_annualrevenue AS 'Annual Revenue',"
				+ " enquiry_jlr_noofpeopleinhousehold AS 'No. of People in Household',"
				+ " enquiry_jlr_noofemployees AS 'No. of Employees',"
				+ " enquiry_jlr_householdincome AS 'Household Income',"
				+ " IF(enquiry_jlr_highnetworth = 1, 'Yes', 'No' ) AS 'High Net Worth',"
				+ " IF(enquiry_date != '', date_format(enquiry_date, '%d/%m/%y'), '') AS 'Created Date',"
				+ " IF(enquiry_close_date != '', date_format(enquiry_close_date, '%d/%m/%y'), '') AS 'Closing Date',"
				+ " COALESCE((select occ_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ WHERE occ_id = enquiry_occ_id), '') AS 'Occupation',"
				+ " IF (testdrive_fb_taken != '1',date_format(testdrive_time,'%d/%m/%Y %H:%i'),'') AS 'Test Drive Planned Time',"
				+ " IF (testdrive_fb_taken != '1',testdriveveh_name,'') AS 'Test Drive Planned Variant',"
				+ " IF (testdrive_fb_taken != '1',location_name,'') AS 'Test Drive Planned Location',"
				+ " IF (testdrive_fb_taken = '1',testdrivestatus.status_name,'') AS 'Test Drive Feedback Status',"

				+ " IF(followup_followup_time != '',DATE_FORMAT(followup_followup_time, '%d/%m/%Y %H:%i'),'') AS 'Follow-up Time', "
				+ " COALESCE(followuptype_name, '') AS 'Follow-up Type',"
				+ " followup_desc AS 'Follow-up Description',"
				+ " COALESCE((SELECT DATE_FORMAT(followupnext.followup_followup_time, '%d/%m/%Y %H:%i') "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup AS followupnext"
				+ " WHERE followupnext.followup_enquiry_id = enquiry_id"
				+ " AND followupnext.followup_followup_time > " + compdb(comp_id) + "axela_sales_enquiry_followup.followup_followup_time limit 1), '') AS 'Next Follow-up',"
				+ " COALESCE(testdriveveh_name, '') AS 'Test Drive Taken Model',"
				+ " COALESCE(entry.emp_name,'') AS 'Entry By',"
				+ " IF(enquiry_entry_date != '', date_format(enquiry_entry_date, '%d/%m/%y'), '') AS 'Entry Date',"
				+ " COALESCE(enq.emp_name,'') AS 'Lead Owner',"
				+ " concat(manemp.emp_name,' (', manemp.emp_ref_no, ')') AS 'Team Leader',"
				+ "  branch_name AS Branch,"
				+ " stage_name AS Stage,"
				+ " enquirystatus.status_name AS 'Enquiry Status',"
				+ "  COALESCE(enquiry_status_desc, '') AS 'Status Description',"

				+ " COALESCE((SELECT IF(so_delivered_date != '', date_format(so_delivered_date, '%d/%m/%y'), '') "
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_enquiry_id = enquiry_id),'') AS 'Delivery Date',"
				+ " priorityenquiry_name AS 'Priority Name',"
				+ " COALESCE(sob_name, '') AS 'Source Of Business',"
				+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.', "
				+ " COALESCE(enquiry_notes, '') AS 'Notes',"
				+ " enquiry_no AS 'Enquiry No.',"
				+ " COALESCE(enquiry_fuelallowance, '') AS 'Fuel Allowance',"
				+ " COALESCE(enquirycat_name, '') AS 'Category',"
				+ " COALESCE(corporate_name, '') AS 'Corporate'";
		return StrSql;

	}

	public String PopulatePrincipal(String brand_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 1"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=''>").append("Select").append("");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String branch_id, String comp_id, HttpServletRequest request) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateExecutives();PopulateTeams();PopulateCRMDays();\">");
			Str.append("<option value='0'>Select</option>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(StrSelectdrop(crs.getInt("branch_id") + "", branch_id));
					Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFilterBy(String filterby_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"newenquiry\" " + StrSelectdrop(filterby_id, "newenquiry") + ">New Enquiry</option>");
			Str.append("<option value=\"follow-up\" " + StrSelectdrop(filterby_id, "follow-up") + ">Follow-up</option>");
			Str.append("<option value=\"testdrive\" " + StrSelectdrop(filterby_id, "testdrive") + ">Test Drive</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
