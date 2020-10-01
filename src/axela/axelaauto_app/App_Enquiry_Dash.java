package axela.axelaauto_app;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class App_Enquiry_Dash extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String enquiry_branch_id = "0";
	public String branch_name = "";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_no = "";
	public String enquiry_date = "";
	public String date = "";
	public String enquiry_tradein_preownedvariant_id = "0";
	public String enquiry_age_id = "";
	public String enquiry_contact_id = "0";
	public String enquiry_customer_id = "0";
	public String enquiry_corporate_id = "0";
	public String customer_dnd = "";
	public String enquiry_close_date = "";
	public String closedate = "";
	public String enquiry_value_syscal = "";
	public String enquiry_budget_id = "";
	public String contact_mobile1 = "";
	public String enquiry_existingvehicle = "";
	public String contact_email1 = "";
	public String enquiry_ownership_id = "";
	public String contact_mobile2 = "";
	// public String enquiry_evaluation = "";
	public String enquiry_add_model_id = "0";
	public String enquiry_purchasemonth = "";
	public String enquiry_option_id = "0";
	public String enquiry_familymember_count = "";
	public String contact_email2 = "";
	public String enquiry_othercar = "";
	public String enquiry_corporate = "";
	public String stage_name = "";
	public String contact_fname = "";
	public String enquiry_currentemi = "";
	public String contact_lname = "";
	public String enquiry_preownedmodel_id = "0";
	public String enquiry_purchasemode_id = "";
	public String title_desc = "";
	public String contact_pin = "";
	public String enquiry_monthkms_id = "";
	public String contact_title_id = "0";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String branch_brand_id = "0";
	public String enquiry_customer_name = "";
	public String enquiry_occ_id = "0";
	public String enquiry_carusage_id = "0";
	public String enquiry_enquirycustomertype_id = "0";
	public String enquiry_drivetype = "0";
	public String enquiry_enquiryincome_id = "0";
	public String enquiry_monthlydrive_id = "0";
	public String enquiry_income_id = "";
	public String enquiry_fuelallowance = "";
	public String enquiry_desc = "";
	public String enquiry_refno = "";
	public String enquiry_action = "";
	public String enquiry_dmsno = "";
	public String enquiry_avpresent = "";
	public String enquiry_emp_id = "0";
	public String enquiry_manager_assist = "";
	public String enquiry_notes = "";
	// public String enquiry_priorityfollowup_id = "";
	public String enquiry_priorityenquiry_id = "0";
	public String priorityenquiry_desc = "";
	public String priorityenquiry_duehrs = "0";
	public String enquiry_status_id = "1";
	public String enquiry_status_desc = "";
	public String enquiry_status_date = "";
	public String statusdate = "";
	public String soe_name = "";
	public String sob_name = "";
	public String campaign_name = "";
	public int days_diff = 0;
	public String StrHTML = "";
	public String contact_city_id = "0";
	public String customer_zone_id = "";
	public String customer_company = "";
	public String customer_ind_id = "0";
	public String enquiry_entry_id = "0";
	public String enquiry_entry_date = "";
	public String enquiry_modified_id = "0";
	public String enquiry_modified_date = "";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String ref_emp_name = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String enquiry_lead_id = "0";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String config_sales_enquiry_refno = "";
	public String enquiry_model_id = "0";
	public String enquiry_item_id = "0";
	public String enquiry_preownedvariant_id = "0";
	public String enquiry_fueltype_id = "0";
	public String enquiry_prefreg_id = "0";
	public String enquiry_presentcar = "";
	public String enquiry_finance = "";
	public String enquiry_prefregyear = "";
	public String enquiry_prefmileage_id = "0";
	public String enquiry_extcolour_id = "0";
	public String enquiry_intcolour_id = "0";
	public String enquiry_enquirytype_id = "";
	public String enquiry_enquirycat_id = "";
	public String enquirytype_name = "";
	public String enquiry_buyertype_id = "";
	public String enquiry_compbrand_id = "";
	public String enquiry_loancompletionmonth = "";
	public String enquiry_compbrand_others = "";
	public String enquiry_compbrand_model = "";
	public String enquiry_compbrand_variant = "";
	public String enquiry_lostcase1_id = "0";
	public String enquiry_expectedprice = "";
	public String enquiry_lostcase2_id = "0";
	public String enquiry_lostcase3_id = "0";
	public String enquiry_expectation_id = "";
	public String compbrand_ids[];
	public String enquiry_value = "";
	public int enquirystatusid = 0;
	public String enquiry_kms = "";
	public int recperpage = 0;
	public String day = "", month = "", year = "";
	public String enquiry_loanfinancer = "";
	public String comp_id = "0";
	public String enquiry_quotedprice = "";
	public String emp_uuid = "";
	public String emp_all_exe = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String access = "0";

	// Start Nexa Fields
	public String enquiry_nexa_gender = "";
	public String enquiry_nexa_beveragechoice = "";
	public String enquiry_nexa_autocard = "";
	public String enquiry_nexa_fueltype = "";
	public String enquiry_nexa_specreq = "";
	public String enquiry_nexa_testdrivereq = "";
	public String enquiry_nexa_testdrivereqreason = "";
	// End Nexa Fields

	// Start Hyundai Fields
	public String enquiry_hyundai_chooseoneoption = "";
	public String enquiry_hyundai_kmsinamonth = "";
	public String enquiry_hyundai_membersinthefamily = "";
	public String enquiry_hyundai_topexpectation = "";
	public String enquiry_hyundai_finalizenewcar = "";
	public String enquiry_hyundai_modeofpurchase = "";
	public String enquiry_hyundai_annualincome = "";
	public String enquiry_hyundai_othercars = "";
	public String enquiry_hyundai_currentcars = "";
	public String enquiry_hyundai_dob = "";

	// / Start Ford Fields
	public String enquiry_ford_customertype = "";
	public String enquiry_ford_intentionpurchase = "";
	public String enquiry_ford_kmsdriven = "";
	public String enquiry_ford_newvehfor = "";
	public String enquiry_ford_investment = "";
	public String enquiry_ford_colourofchoice = "";
	public String enquiry_ford_cashorfinance = "";
	public String enquiry_ford_currentcar = "";
	public String enquiry_ford_exchangeoldcar = "";
	public String enquiry_ford_othercarconcideration = "";
	public String enquiry_ford_ex_make = "";
	public String enquiry_ford_ex_model = "";
	public String enquiry_ford_ex_derivative = "";
	public String enquiry_ford_ex_year = "";
	public String enquiry_ford_ex_odoreading = "";
	public String enquiry_ford_ex_doors = "";
	public String enquiry_ford_ex_bodystyle = "";
	public String enquiry_ford_ex_enginesize = "";
	public String enquiry_ford_ex_fueltype = "";
	public String enquiry_ford_ex_drive = "";
	public String enquiry_ford_ex_transmission = "";
	public String enquiry_ford_ex_colour = "";
	public String enquiry_ford_ex_priceoffered = "";
	public String enquiry_ford_ex_estmtprice = "";
	public String enquiry_ford_vistacontractnumber = "";
	public String enquiry_ford_nscordernumber = "";
	public String enquiry_ford_qcsid = "";
	public String enquiry_ford_qpdid = "";
	public String followuptab = "", crmtab = "";
	public Preowned_Variant_Check variantcheck = new Preowned_Variant_Check();
	// End Ford Fields

	// Start MB
	public String enquiry_mb_occupation = "";
	public String enquiry_mb_carusage = "";
	public String enquiry_mb_type = "";
	public String enquiry_mb_drivingpattern = "";
	public String enquiry_mb_income = "";
	public String enquiry_mb_avgdriving = "";
	public String enquiry_mb_currentcars = "";
	// End MB

	// Start preowned
	public String preowned_enquiry_id = "0";
	public String eval_offered_price = "0";
	public String eval_entry_date = "";
	public String fueltype_name = "";
	public String extcolour_name = "";
	public String intcolour_name = "";
	public String ownership_id = "";
	public String preowned_regno = "";
	public String preowned_kms = "";
	public String preowned_fcamt = "";
	public String preowned_manufyear = "";
	public String preowned_expectedprice = "";
	public String preowned_sub_variant = "";
	// End preowned

	// Start porsche need assessment fields
	public String enquiry_porsche_gender = "";
	public String enquiry_porsche_nationality = "";
	public String enquiry_porsche_language = "";
	public String enquiry_porsche_religion = "";
	public String enquiry_porsche_preferredcomm = "";
	public String enquiry_porsche_socialmediapref = "";
	public String enquiry_porsche_maritalstatus = "";
	public String enquiry_porsche_spousename = "";
	public String enquiry_porsche_spousedrive = "";
	public String enquiry_porsche_interest = "";
	public String enquiry_porsche_clubmembership = "";
	public String enquiry_porsche_financeoption = "";
	public String enquiry_porsche_insuranceoption = "";
	public String enquiry_porsche_vehicleinhouse = "";
	public String enquiry_porsche_householdcount = "";
	public String enquiry_porsche_contact_dob = "";
	public String enquiry_porsche_contact_anniversary = "";
	public String enquiry_porsche_industry = "";
	// End porsche need assessment fields

	// Start AMP need assessment fields
	public String enquiry_jlr_employmentstatus = "";
	public String enquiry_jlr_industry = "";
	public String enquiry_jlr_birthday = "";
	public String enquiry_jlr_gender = "";
	public String enquiry_jlr_occupation = "";
	public String enquiry_jlr_currentvehicle = "";
	public String enquiry_jlr_othermodelofinterest = "";
	public String enquiry_jlr_financeinterest = "";
	public String enquiry_jlr_noofchildren = "";
	public String enquiry_jlr_noofpeopleinhousehold = "";
	public String enquiry_jlr_highnetworth = "";
	public String enquiry_jlr_householdincome = "";
	public String enquiry_jlr_interests = "";
	public String enquiry_jlr_annualrevenue = "";
	public String enquiry_jlr_noofemployees = "";
	public String enquiry_jlr_accounttype = "";
	public String enquiry_jlr_enquirystatus = "";
	public String jlr_enquirystatus = "";
	// End AMP need assessment fields

	// Skoda Fields
	public String na_skoda_ownbusiness = "";
	public String na_skoda_companyname = "";
	public String na_skoda_financerequired = "";
	public String na_skoda_currentcarappxkmsrun = "";
	public String na_skoda_whatareyoulookingfor = "";
	public String na_skoda_numberoffamilymembers = "";
	public String na_skoda_whowilldrive = "";
	public String na_skoda_whoareyoubuyingfor = "";
	public String na_skoda_newcarappxrun = "";
	public String na_skoda_wherewillbecardriven = "";
	public String enquirydate = "";
	public String contact_jobtitle = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{

					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				msg = PadQuotes(request.getParameter("msg"));
				followuptab = PadQuotes(request.getParameter("followuptab"));
				crmtab = PadQuotes(request.getParameter("crmtab"));
				// // msgerror = PadQuotes(request.getParameter("msgerror"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				branch_brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_branch_id = branch_id"
						+ " WHERE 1=1"
						+ " AND enquiry_id = " + enquiry_id));
				if (branch_brand_id.equals("11")) {
					PopulateSkodaNeedAssessmentFields(enquiry_id, comp_id);
				}

				PopulateConfigDetails();
				PopulateFields(response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT "
					// +
					// " COALESCE((SELECT GROUP_CONCAT(brandsowned_compbrand_id) FROM "
					// + compdb(comp_id) + "axela_sales_enquiry_brandsowned"
					// +
					// " WHERE brandsowned_enquiry_id=enquiry_id),0) AS brand, "
					+ " COALESCE(CONCAT(ref.emp_name,' (', ref.emp_ref_no, ')'),'') AS ref_emp_name,"
					+ " contact_mobile1, enquiry_id, contact_mobile2, contact_phone1, contact_phone2, contact_email1, customer_name, contact_email2, contact_address, "
					+ " contact_city_id, contact_pin, branch_name, contact_fname, branch_code, branch_brand_id, enquiry_age_id, enquiry_option_id, enquiry_add_model_id, stage_name, enquiry_model_id, enquiry_item_id,"
					+ " enquiry_fueltype_id, enquiry_prefreg_id, enquiry_no, enquiry_age_id, enquiry_value, enquiry_presentcar, enquiry_dmsno,"
					+ " COALESCE(soe_name,'') AS soe_name,priorityenquiry_desc, priorityenquiry_duehrs, COALESCE(sob_name, '') AS sob_name, enquiry_branch_id, enquiry_notes,"
					+ " COALESCE(campaign_name, '') AS campaign_name, enquiry_title, enquiry_date, enquiry_close_date, enquiry_desc,"
					+ " enquiry_emp_id, enquiry_customer_id, contact_title_id, title_desc,contact_lname,enquiry_entry_id, enquiry_loanfinancer,"
					+ " enquiry_modified_id, enquiry_modified_date, enquiry_occ_id, enquiry_custtype_id, enquiry_enquirycat_id, enquiry_fuelallowance, enquiry_priorityenquiry_id, enquiry_loancompletionmonth,"
					// +
					// "  enquiry_monthlydrive_id, enquiry_compbrand_id, enquiry_compbrand_others,"
					+ " enquiry_expectedprice, enquiry_quotedprice,"
					// + " enquiry_evaluation,"
					+ " enquiry_status_id, enquiry_status_desc, enquiry_entry_date,"
					+ " enquiry_buyertype_id, enquiry_ownership_id, enquiry_existingvehicle, enquiry_purchasemonth, enquiry_currentemi, enquiry_kms,"
					+ " enquiry_purchasemode_id, enquiry_income_id, enquiry_familymember_count, enquiry_expectation_id, enquiry_othercar, enquiry_corporate,"
					+ " enquiry_enquirytype_id, enquiry_monthkms_id, enquiry_lostcase1_id, enquiry_lostcase2_id, enquiry_lostcase3_id, enquiry_corporate_id,"
					// Start Nexa Fields
					+ " enquiry_nexa_gender, enquiry_nexa_beveragechoice, enquiry_nexa_autocard, enquiry_nexa_fueltype, enquiry_nexa_specreq, "
					+ " enquiry_nexa_testdrivereq, enquiry_nexa_testdrivereqreason, "
					// End Nexa Fields

					// Start Hyundai Fields
					+ " enquiry_hyundai_chooseoneoption, enquiry_hyundai_kmsinamonth, enquiry_hyundai_membersinthefamily, "
					+ " enquiry_hyundai_topexpectation, enquiry_hyundai_finalizenewcar, enquiry_hyundai_modeofpurchase, enquiry_hyundai_annualincome, "
					+ " enquiry_hyundai_othercars, enquiry_hyundai_currentcars, enquiry_hyundai_dob, "
					// End Hyundai Fields

					// // Pre owned field
					+ " enquiry_preownedvariant_id,"
					// Start Ford Fields
					+ " enquiry_ford_customertype, enquiry_ford_intentionpurchase, enquiry_ford_kmsdriven, enquiry_ford_newvehfor, enquiry_ford_investment, enquiry_ford_colourofchoice,"
					+ " enquiry_ford_cashorfinance, enquiry_ford_currentcar, enquiry_ford_exchangeoldcar, enquiry_ford_othercarconcideration, enquiry_ford_ex_make,"
					+ " enquiry_ford_ex_model, enquiry_ford_ex_derivative, enquiry_ford_ex_year, enquiry_ford_ex_odoreading, enquiry_ford_ex_doors, enquiry_ford_ex_bodystyle,"
					+ " enquiry_ford_ex_enginesize, enquiry_ford_ex_fueltype, enquiry_ford_ex_drive, enquiry_ford_ex_transmission, enquiry_ford_ex_colour, enquiry_ford_ex_priceoffered,"
					+ " enquiry_ford_ex_estmtprice, enquiry_ford_vistacontractnumber, enquiry_ford_nscordernumber, enquiry_ford_qcsid, enquiry_ford_qpdid,"
					// End Ford Fields

					// Start MB Fields
					+ " enquiry_mb_occupation, enquiry_mb_carusage, enquiry_mb_type, enquiry_mb_drivingpattern, enquiry_mb_income, enquiry_mb_avgdriving, enquiry_mb_currentcars,"
					// End MB Fields
					// Pre-owned fields starts
					+ " COALESCE(preowned_enquiry_id,'0') AS preowned_enquiry_id,"
					+ " COALESCE(preowned_sub_variant,'') AS preowned_sub_variant,"
					+ " COALESCE((SELECT fueltype_name"
					+ " FROM "
					+ compdb(comp_id) + "axela_fueltype"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_fueltype_id = fueltype_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = preowned_enquiry_id"
					+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS fueltype_name,"

					+ " COALESCE((SELECT extcolour_name"
					+ " FROM axela_preowned_extcolour"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_extcolour_id = extcolour_id "
					+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS extcolour_name,"

					+ " COALESCE((SELECT intcolour_name"
					+ " FROM axela_preowned_intcolour"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_intcolour_id = intcolour_id "
					+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS intcolour_name,"
					+ " COALESCE(preowned_regno,'') AS preowned_regno,"
					+ " COALESCE(preowned_manufyear,'') AS preowned_manufyear,"
					+ " COALESCE((SELECT ownership_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_ownership_id = ownership_id "
					+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS ownership_id,"
					+ " COALESCE(preowned_kms,'') AS preowned_kms,"
					+ " COALESCE(preowned_fcamt,'') AS preowned_fcamt,"
					+ " COALESCE((SELECT ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_ownership_id = ownership_id "
					+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS ownership_name,"
					+ " enquiry_tradein_preownedvariant_id,"
					+ " COALESCE(eval_offered_price, 0) AS eval_offered_price, "
					+ " COALESCE(eval_entry_date, '') AS eval_entry_date,"
					+ " COALESCE(preowned_expectedprice,'') AS preowned_expectedprice,"
					// Pre-owned fields Ends

					// Start Porsche details
					+ " enquiry_porsche_gender, enquiry_porsche_nationality, enquiry_porsche_language,enquiry_porsche_industry,"
					+ " enquiry_porsche_religion, enquiry_porsche_preferredcomm, enquiry_porsche_socialmediapref,"
					+ " enquiry_porsche_maritalstatus, enquiry_porsche_spousename, enquiry_porsche_spousedrive,"
					+ " enquiry_porsche_interest, enquiry_porsche_clubmembership, enquiry_porsche_financeoption,"
					+ " enquiry_porsche_insuranceoption, enquiry_porsche_vehicleinhouse,enquiry_porsche_householdcount,"
					+ " contact_anniversary,"
					// End Porsche details

					// Start AMP fields
					+ " enquiry_jlr_employmentstatus, enquiry_jlr_industry, enquiry_jlr_birthday, enquiry_jlr_gender,"
					+ " enquiry_jlr_occupation, enquiry_jlr_currentvehicle, enquiry_jlr_othermodelofinterest,"
					+ " enquiry_jlr_financeinterest, enquiry_jlr_noofchildren, enquiry_jlr_noofpeopleinhousehold,"
					+ " enquiry_jlr_highnetworth, enquiry_jlr_householdincome, enquiry_jlr_interests, enquiry_jlr_annualrevenue,"
					+ " enquiry_jlr_noofemployees, enquiry_jlr_accounttype, enquiry_jlr_enquirystatus, contact_dob, contact_jobtitle"
					// End AMP fields

					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = enquiry_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ref ON ref.emp_id = enquiry_refemp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned ON preowned_enquiry_id = enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_sales_enquiry_budget ON budget_id = enquiry_budget_id "
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_sales_enquiry_prefmileage ON prefmileage_id = enquiry_prefmileage_id "
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_preowned_extcolour ON extcolour_id = enquiry_extcolour_id "
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_preowned_intcolour ON intcolour_id = enquiry_intcolour_id "
					+ " WHERE enquiry_id=" + enquiry_id
					+ BranchAccess
					+ ExeAccess;
			StrSql += " GROUP BY enquiry_id ";
			SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_id = crs.getString("enquiry_id");
					priorityenquiry_desc = crs.getString("priorityenquiry_desc");
					priorityenquiry_duehrs = crs.getString("priorityenquiry_duehrs");
					// enquiry_lead_id = crs.getString("enquiry_lead_id");
					ref_emp_name = crs.getString("ref_emp_name");
					enquiry_monthkms_id = crs.getString("enquiry_monthkms_id");
					enquiry_purchasemonth = strToShortDate(crs.getString("enquiry_purchasemonth"));
					enquiry_kms = crs.getString("enquiry_kms");
					contact_mobile1 = crs.getString("contact_mobile1");
					enquiry_expectedprice = crs.getString("enquiry_expectedprice");
					enquiry_value = crs.getString("enquiry_value");
					// enquiry_evaluation = crs.getString("enquiry_evaluation");
					enquiry_othercar = crs.getString("enquiry_othercar");
					enquiry_corporate = crs.getString("enquiry_corporate");
					enquiry_buyertype_id = crs.getString("enquiry_buyertype_id");
					enquiry_loanfinancer = crs.getString("enquiry_loanfinancer");
					enquiry_model_id = crs.getString("enquiry_model_id");
					enquiry_add_model_id = crs.getString("enquiry_add_model_id");
					enquiry_option_id = crs.getString("enquiry_option_id");
					enquiry_quotedprice = crs.getString("enquiry_quotedprice");
					enquiry_age_id = crs.getString("enquiry_age_id");
					enquiry_currentemi = crs.getString("enquiry_currentemi");
					enquiry_ownership_id = crs.getString("enquiry_ownership_id");
					enquiry_income_id = crs.getString("enquiry_income_id");
					enquiry_purchasemode_id = crs.getString("enquiry_purchasemode_id");
					enquiry_expectation_id = crs.getString("enquiry_expectation_id");
					enquiry_purchasemonth = strToShortDate(crs.getString("enquiry_purchasemonth"));
					enquiry_loancompletionmonth = crs.getString("enquiry_loancompletionmonth");
					// if (!enquiry_loancompletionmonth.equals("")) {
					// // enquiry_loancompletionmonth =
					// // enquiry_loancompletionmonth.replace('/', '-');
					// day = enquiry_loancompletionmonth.substring(0, 2);
					// month = enquiry_loancompletionmonth.substring(3, 5);
					// year = enquiry_loancompletionmonth.substring(6, 10);
					// closedate = day + "/" + month + "/" + year;
					// }
					enquiry_familymember_count = crs.getString("enquiry_familymember_count");
					// enquiry_preownedmodel_id = crs.getString("enquiry_preownedmodel_id");
					enquiry_customer_name = crs.getString("customer_name");
					contact_fname = crs.getString("contact_fname");
					// enquiry_preownedmodel_id = rs
					// .getString("enquiry_preownedmodel_id");
					enquiry_item_id = crs.getString("enquiry_item_id");
					enquiry_preownedvariant_id = crs.getString("enquiry_preownedvariant_id");
					enquiry_fueltype_id = crs.getString("enquiry_fueltype_id");
					enquiry_prefreg_id = crs.getString("enquiry_prefreg_id");
					enquiry_presentcar = crs.getString("enquiry_presentcar");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					enquiry_enquirycat_id = crs.getString("enquiry_enquirycat_id");
					// enquirytype_name = crs.getString("enquirytype_name");
					enquiry_title = crs.getString("enquiry_title");
					enquiry_existingvehicle = crs.getString("enquiry_existingvehicle");
					enquiry_no = "OPR" + crs.getString("branch_code") + crs.getString("enquiry_no");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					branch_name = crs.getString("branch_name");
					enquiry_date = ConvertShortDateToStr(strToShortDate(crs.getString("enquiry_date")));
					date = strToShortDate(enquiry_date);
					enquiry_close_date = crs.getString("enquiry_close_date");
					closedate = strToShortDate(enquiry_close_date);
					contact_jobtitle = crs.getString("contact_jobtitle");
					enquirydate = strToShortDate(enquiry_close_date);
					// if (!closedate.equals("")) {
					// // closedate = closedate.replace('/', '-');
					// day = closedate.substring(0, 2);
					// month = closedate.substring(3, 5);
					// year = closedate.substring(6, 10);
					// closedate = day + "/" + month + "/" + year;
					// }
					enquiry_desc = crs.getString("enquiry_desc");
					enquiry_emp_id = crs.getString("enquiry_emp_id");
					enquiry_customer_id = crs.getString("enquiry_customer_id");
					// enquiry_contact_id = crs.getString("enquiry_contact_id");
					// enquiry_refno = crs.getString("enquiry_refno");
					days_diff = (int) (getDaysBetween(ToShortDate(kknow()), enquiry_close_date));
					// enquiry_value_syscal =
					// crs.getString("enquiry_value_syscal");
					// enquiry_budget_id = crs.getString("enquiry_budget_id");
					stage_name = crs.getString("stage_name");
					// enquiry_avpresent = crs.getString("enquiry_avpresent");
					// enquiry_manager_assist =
					// crs.getString("enquiry_manager_assist");
					enquiry_status_id = crs.getString("enquiry_status_id");
					// enquiry_status_date =
					// crs.getString("enquiry_status_date");
					// statusdate = strToLongDate(enquiry_status_date);
					enquiry_status_desc = crs.getString("enquiry_status_desc");
					enquiry_priorityenquiry_id = crs.getString("enquiry_priorityenquiry_id");
					// enquiry_action = crs.getString("enquiry_action");
					enquiry_dmsno = crs.getString("enquiry_dmsno");
					enquiry_notes = crs.getString("enquiry_notes");
					enquiry_customer_name = crs.getString("customer_name");
					// customer_dnd = crs.getString("customer_dnd");
					soe_name = crs.getString("soe_name");
					sob_name = crs.getString("sob_name");
					campaign_name = crs.getString("campaign_name");
					// enquiry_finance = crs.getString("enquiry_finance");
					// enquiry_prefregyear =
					// crs.getString("enquiry_prefregyear");
					// enquiry_prefmileage_id =
					// crs.getString("enquiry_prefmileage_id");
					contact_title_id = crs.getString("contact_title_id");
					branch_brand_id = crs.getString("branch_brand_id");
					title_desc = crs.getString("title_desc");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_phone1 = crs.getString("contact_phone1");
					contact_phone2 = crs.getString("contact_phone2");
					contact_address = crs.getString("contact_address");
					contact_pin = crs.getString("contact_pin");
					contact_city_id = crs.getString("contact_city_id");
					// customer_zone_id = crs.getString("customer_zone_id");
					// customer_company = crs.getString("customer_company");
					// customer_ind_id = crs.getString("customer_ind_id");
					enquiry_occ_id = crs.getString("enquiry_occ_id");
					// enquiry_carusage_id =
					// crs.getString("enquiry_carusage_id");
					enquiry_enquirycustomertype_id = crs.getString("enquiry_custtype_id");
					enquiry_fuelallowance = crs.getString("enquiry_fuelallowance");
					// enquiry_drivetype = crs.getString("enquiry_drivetype");
					// enquiry_enquiryincome_id =
					// crs.getString("enquiry_enquiryincome_id");
					// enquiry_monthlydrive_id =
					// crs.getString("enquiry_monthlydrive_id");

					enquiry_lostcase1_id = crs.getString("enquiry_lostcase1_id");
					enquiry_lostcase2_id = crs.getString("enquiry_lostcase2_id");
					enquiry_lostcase3_id = crs.getString("enquiry_lostcase3_id");
					enquiry_corporate_id = crs.getString("enquiry_corporate_id");
					enquiry_entry_id = crs.getString("enquiry_entry_id");
					entry_by = Exename(comp_id, crs.getInt("enquiry_entry_id"));
					entry_date = strToLongDate(crs.getString("enquiry_entry_date"));
					enquiry_modified_id = crs.getString("enquiry_modified_id");

					if (!enquiry_modified_id.equals("0")) {
						modified_by = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id=" + enquiry_modified_id);
						modified_date = strToLongDate(crs.getString("enquiry_modified_date"));
					}
					// Start Nexa Fields
					enquiry_nexa_gender = crs.getString("enquiry_nexa_gender");
					enquiry_nexa_beveragechoice = crs.getString("enquiry_nexa_beveragechoice");
					enquiry_nexa_autocard = crs.getString("enquiry_nexa_autocard");
					enquiry_nexa_fueltype = crs.getString("enquiry_nexa_fueltype");
					enquiry_nexa_specreq = crs.getString("enquiry_nexa_specreq");
					enquiry_nexa_testdrivereq = crs.getString("enquiry_nexa_testdrivereq");
					enquiry_nexa_testdrivereqreason = crs.getString("enquiry_nexa_testdrivereqreason");
					// End Nexa Fields
					// Start Hyundai Fields
					enquiry_hyundai_chooseoneoption = crs.getString("enquiry_hyundai_chooseoneoption");
					enquiry_hyundai_kmsinamonth = crs.getString("enquiry_hyundai_kmsinamonth");
					enquiry_hyundai_membersinthefamily = crs.getString("enquiry_hyundai_membersinthefamily");
					enquiry_hyundai_topexpectation = crs.getString("enquiry_hyundai_topexpectation");
					enquiry_hyundai_finalizenewcar = unescapehtml(crs.getString("enquiry_hyundai_finalizenewcar"));
					enquiry_hyundai_modeofpurchase = crs.getString("enquiry_hyundai_modeofpurchase");
					enquiry_hyundai_annualincome = crs.getString("enquiry_hyundai_annualincome");
					enquiry_hyundai_othercars = crs.getString("enquiry_hyundai_othercars");
					enquiry_hyundai_currentcars = crs.getString("enquiry_hyundai_currentcars");
					enquiry_hyundai_dob = strToShortDate(crs.getString("enquiry_hyundai_dob"));

					// Start Ford Fields
					enquiry_ford_customertype = crs.getString("enquiry_ford_customertype");
					enquiry_ford_intentionpurchase = crs.getString("enquiry_ford_intentionpurchase");
					enquiry_ford_kmsdriven = crs.getString("enquiry_ford_kmsdriven");
					enquiry_ford_newvehfor = crs.getString("enquiry_ford_newvehfor");
					enquiry_ford_investment = crs.getString("enquiry_ford_investment");
					enquiry_ford_colourofchoice = crs.getString("enquiry_ford_colourofchoice");
					enquiry_ford_cashorfinance = crs.getString("enquiry_ford_cashorfinance");
					enquiry_ford_currentcar = crs.getString("enquiry_ford_currentcar");
					enquiry_ford_exchangeoldcar = crs.getString("enquiry_ford_exchangeoldcar");
					enquiry_ford_othercarconcideration = crs.getString("enquiry_ford_othercarconcideration");
					enquiry_ford_ex_make = crs.getString("enquiry_ford_ex_make");
					enquiry_ford_ex_model = crs.getString("enquiry_ford_ex_model");
					enquiry_ford_ex_derivative = crs.getString("enquiry_ford_ex_derivative");
					enquiry_ford_ex_year = crs.getString("enquiry_ford_ex_year");
					enquiry_ford_ex_odoreading = crs.getString("enquiry_ford_ex_odoreading");
					enquiry_ford_ex_doors = crs.getString("enquiry_ford_ex_doors");
					enquiry_ford_ex_bodystyle = crs.getString("enquiry_ford_ex_bodystyle");
					enquiry_ford_ex_enginesize = crs.getString("enquiry_ford_ex_enginesize");
					enquiry_ford_ex_fueltype = crs.getString("enquiry_ford_ex_fueltype");
					enquiry_ford_ex_drive = crs.getString("enquiry_ford_ex_drive");
					enquiry_ford_ex_transmission = crs.getString("enquiry_ford_ex_transmission");
					enquiry_ford_ex_colour = crs.getString("enquiry_ford_ex_colour");
					enquiry_ford_ex_priceoffered = crs.getString("enquiry_ford_ex_priceoffered");
					enquiry_ford_ex_estmtprice = crs.getString("enquiry_ford_ex_estmtprice");
					enquiry_ford_vistacontractnumber = crs.getString("enquiry_ford_vistacontractnumber");
					enquiry_ford_nscordernumber = crs.getString("enquiry_ford_nscordernumber");
					enquiry_ford_qcsid = crs.getString("enquiry_ford_qcsid");
					enquiry_ford_qpdid = crs.getString("enquiry_ford_qpdid");
					// End Ford Fields

					// Start MB Fields
					enquiry_mb_occupation = crs.getString("enquiry_mb_occupation");
					enquiry_mb_carusage = crs.getString("enquiry_mb_carusage");
					enquiry_mb_type = crs.getString("enquiry_mb_type");
					enquiry_mb_drivingpattern = crs.getString("enquiry_mb_drivingpattern");
					enquiry_mb_income = crs.getString("enquiry_mb_income");
					enquiry_mb_avgdriving = crs.getString("enquiry_mb_avgdriving");
					enquiry_mb_currentcars = crs.getString("enquiry_mb_currentcars");
					// End MB Fields

					// Start Preowned
					preowned_enquiry_id = crs.getString("preowned_enquiry_id");
					enquiry_tradein_preownedvariant_id = crs.getString("enquiry_tradein_preownedvariant_id");
					eval_offered_price = crs.getString("eval_offered_price");
					eval_entry_date = crs.getString("eval_entry_date");
					if (!eval_entry_date.equals("")) {
						eval_entry_date = strToLongDate(eval_entry_date);
					} else {
						eval_entry_date = "Not Evaluated.";
					}
					preowned_sub_variant = crs.getString("preowned_sub_variant");
					fueltype_name = crs.getString("fueltype_name");
					extcolour_name = crs.getString("extcolour_name");
					intcolour_name = crs.getString("intcolour_name");
					preowned_regno = crs.getString("preowned_regno");
					preowned_kms = crs.getString("preowned_kms");
					preowned_fcamt = crs.getString("preowned_fcamt");
					preowned_manufyear = crs.getString("preowned_manufyear");
					ownership_id = crs.getString("ownership_id");
					preowned_expectedprice = crs.getString("preowned_expectedprice");
					// End Preowned

					// Start Porsche Fields
					enquiry_porsche_gender = crs.getString("enquiry_porsche_gender");
					enquiry_porsche_nationality = crs.getString("enquiry_porsche_nationality");
					enquiry_porsche_industry = crs.getString("enquiry_porsche_industry");
					enquiry_porsche_language = crs.getString("enquiry_porsche_language");
					enquiry_porsche_religion = crs.getString("enquiry_porsche_religion");
					enquiry_porsche_preferredcomm = crs.getString("enquiry_porsche_preferredcomm");
					enquiry_porsche_socialmediapref = crs.getString("enquiry_porsche_socialmediapref");
					enquiry_porsche_maritalstatus = crs.getString("enquiry_porsche_maritalstatus");
					enquiry_porsche_spousename = crs.getString("enquiry_porsche_spousename");
					enquiry_porsche_spousedrive = crs.getString("enquiry_porsche_spousedrive");
					enquiry_porsche_interest = crs.getString("enquiry_porsche_interest");
					enquiry_porsche_clubmembership = crs.getString("enquiry_porsche_clubmembership");
					enquiry_porsche_financeoption = crs.getString("enquiry_porsche_financeoption");
					enquiry_porsche_insuranceoption = crs.getString("enquiry_porsche_insuranceoption");
					enquiry_porsche_vehicleinhouse = crs.getString("enquiry_porsche_vehicleinhouse");
					enquiry_porsche_householdcount = crs.getString("enquiry_porsche_householdcount");
					enquiry_porsche_contact_dob = strToShortDate(crs.getString("contact_dob"));
					enquiry_porsche_contact_anniversary = strToShortDate(crs.getString("contact_anniversary"));
					// End Porsche Fields

					// start AMP fields
					enquiry_jlr_employmentstatus = crs.getString("enquiry_jlr_employmentstatus");
					enquiry_jlr_industry = crs.getString("enquiry_jlr_industry");
					enquiry_jlr_birthday = strToShortDate(crs.getString("contact_dob"));
					enquiry_jlr_gender = crs.getString("enquiry_jlr_gender");
					enquiry_jlr_occupation = crs.getString("enquiry_jlr_occupation");
					enquiry_jlr_currentvehicle = crs.getString("enquiry_jlr_currentvehicle");
					enquiry_jlr_othermodelofinterest = crs.getString("enquiry_jlr_othermodelofinterest");
					enquiry_jlr_financeinterest = crs.getString("enquiry_jlr_financeinterest");
					enquiry_jlr_noofchildren = crs.getString("enquiry_jlr_noofchildren");
					enquiry_jlr_noofpeopleinhousehold = crs.getString("enquiry_jlr_noofpeopleinhousehold");
					enquiry_jlr_highnetworth = crs.getString("enquiry_jlr_highnetworth");
					enquiry_jlr_householdincome = crs.getString("enquiry_jlr_householdincome");
					enquiry_jlr_interests = crs.getString("enquiry_jlr_interests");
					enquiry_jlr_annualrevenue = crs.getString("enquiry_jlr_annualrevenue");
					enquiry_jlr_noofemployees = crs.getString("enquiry_jlr_noofemployees");
					enquiry_jlr_accounttype = crs.getString("enquiry_jlr_accounttype");
					enquiry_jlr_enquirystatus = crs.getString("enquiry_jlr_enquirystatus");
					jlr_enquirystatus = crs.getString("enquiry_jlr_enquirystatus");
					// end AMP fields
				}
			} else {
				// msg = "<br><br>No Opportunity found!";
				// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Opportunity!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateSkodaNeedAssessmentFields(String enquiry_id, String comp_id) {
		try {
			StrSql = "SELECT na_skoda_ownbusiness,"
					+ " na_skoda_companyname,"
					+ " na_skoda_financerequired,"
					+ " na_skoda_currentcarappxkmsrun,"
					+ " na_skoda_whatareyoulookingfor,"
					+ " na_skoda_numberoffamilymembers,"
					+ " na_skoda_whowilldrive,"
					+ " na_skoda_whoareyoubuyingfor,"
					+ " na_skoda_newcarappxrun,"
					+ " na_skoda_wherewillbecardriven"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
					// + " LEFT JOIN "
					+ " WHERE 1=1"
					+ " AND na_enquiry_id = " + enquiry_id;
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					na_skoda_ownbusiness = crs.getString("na_skoda_ownbusiness");
					na_skoda_companyname = crs.getString("na_skoda_companyname");
					na_skoda_financerequired = crs.getString("na_skoda_financerequired");
					na_skoda_currentcarappxkmsrun = crs.getString("na_skoda_currentcarappxkmsrun").replace("&lt;", "<").replace("&gt;", ">");
					na_skoda_whatareyoulookingfor = crs.getString("na_skoda_whatareyoulookingfor");
					na_skoda_numberoffamilymembers = crs.getString("na_skoda_numberoffamilymembers");
					na_skoda_whowilldrive = crs.getString("na_skoda_whowilldrive");
					na_skoda_whoareyoubuyingfor = crs.getString("na_skoda_whoareyoubuyingfor");
					na_skoda_newcarappxrun = crs.getString("na_skoda_newcarappxrun").replace("&lt;", "<").replace("&gt;", ">");
					na_skoda_wherewillbecardriven = crs.getString("na_skoda_wherewillbecardriven");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ " AND emp_sales='1' "
					+ " AND emp_active='1' "
					+ " AND (emp_branch_id = " + enquiry_branch_id
					+ " OR emp_id = 1 "
					+ " OR emp_id In (SELECT empbr.emp_id FROM " + compdb(comp_id)
					+ "axela_emp_branch empbr "
					+ " WHERE axela_emp.emp_id=empbr.emp_id "
					+ " AND empbr.emp_branch_id=" + enquiry_branch_id + "))"
					// + ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql------PopulateExecutive------"+StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("").append(StrSelectdrop(crs.getString("emp_id"), enquiry_emp_id)).append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateContactTitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc FROM " + compdb(comp_id) + "axela_title " + " WHERE 1 = 1 " + " ORDER BY title_desc ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCity() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT city_id, COALESCE(city_name,'') AS city_name " + "FROM " + compdb(comp_id) + "axela_city " + " WHERE 1 = 1 " + " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCity==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("city_id")).append("");
				Str.append(StrSelectdrop(crs.getString("city_id"), contact_city_id));
				Str.append(">").append(crs.getString("city_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateZone() {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("zone==="+zone_id);
			StrSql = "SELECT zone_id, concat(zone_name, ' - ', city_name, ' - ', state_name) AS zone_name" + " FROM " + compdb(comp_id) + "axela_zone" + " INNER JOIN " + compdb(comp_id)
					+ "axela_city ON city_id = zone_city_id" + " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id" + " ORDER BY state_name, city_name, zone_name";
			// SOP("StrSql==="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select</option>");
			// if (!state_id.equals("0")) {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("zone_id")).append("");
				Str.append(StrSelectdrop(crs.getString("zone_id"), customer_zone_id));
				Str.append(">").append(crs.getString("zone_name")).append("</option> \n");
			}
			// }
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT status_id, status_name " + " FROM " + compdb(comp_id) + "axela_sales_enquiry_status " + " WHERE 1 = 1 " + " ORDER BY status_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id")).append("");
				Str.append(StrSelectdrop(crs.getString("status_id"), enquiry_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_name, priorityenquiry_duehrs " + " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority " + " WHERE 1 = 1 "
					+ " ORDER BY priorityenquiry_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option  value=").append(crs.getString("priorityenquiry_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityenquiry_id"), enquiry_priorityenquiry_id));
				Str.append(">").append(crs.getString("priorityenquiry_name")).append(" (").append(crs.getString("priorityenquiry_duehrs")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob, config_sales_campaign, config_sales_enquiry_refno " + " FROM " + compdb(comp_id) + "axela_config," + compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1 AND emp_id = " + emp_id + "";
		// SOP(StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name" + " FROM " + compdb(comp_id) + "axela_inventory_item_model" + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " AND item_type_id = 1" + " WHERE model_active = '1'" + " AND model_sales = '1'" + " GROUP BY model_id" + " ORDER BY model_name";
			// SOP("StrSql------PopulateModel------"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id) + "axela_inventory_item" + " WHERE 1=1 AND item_type_id = 1 " + " AND item_model_id = " + enquiry_model_id
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), enquiry_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreOwnedModel(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name" + " FROM axela_preowned_model" + " WHERE 1=1" + " ORDER BY preownedmodel_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), enquiry_preownedmodel_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreOwnedModel() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name" + " FROM axela_preowned_model" + " WHERE 1=1" + " ORDER BY preownedmodel_name";
			// SOP("StrSql--"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), enquiry_preownedmodel_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreOwnedVariant() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name" + " FROM axela_preowned_variant" + " WHERE 1=1";
			// if (!preowned_preownedmodel_id.equals("0")) {
			// StrSql = StrSql + " AND variant_preownedmodel_id = " + enquiry_preownedmodel_id;
			// }
			StrSql = StrSql + " ORDER BY variant_name";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("").append(StrSelectdrop(crs.getString("variant_id"), enquiry_preownedvariant_id)).append(">")
						.append(crs.getString("variant_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name" + " FROM " + compdb(comp_id) + "axela_fueltype" + " WHERE 1=1" + " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("").append(StrSelectdrop(crs.getString("fueltype_id"), enquiry_fueltype_id)).append(">")
						.append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateAdditionalModel(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name " + " FROM " + compdb(comp_id) + "axela_inventory_item_model" + " WHERE 1=1" + " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = '1'" + " AND model_sales = '1'" + " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_add_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateOption(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, option_name" + " FROM " + compdb(comp_id) + "axela_vehstock_option" + " WHERE 1=1"
					// + " AND option_name != ''"
					+ " AND option_brand_id =" + branch_brand_id + "" + " GROUP BY option_id" + " ORDER BY option_id";
			// SOP("StrSql--------PopulateOption---------" +
			// StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id")).append("");
				Str.append(StrSelectdrop(crs.getString("option_id"), enquiry_option_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAge(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT age_id, age_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age" + " ORDER BY age_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("age_id")).append("");
				Str.append(StrSelectdrop(crs.getString("age_id"), enquiry_age_id));
				Str.append(">").append(crs.getString("age_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrefReg() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prefreg_id, prefreg_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg" + " WHERE 1=1" + " ORDER BY prefreg_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prefreg_id")).append("").append(StrSelectdrop(crs.getString("prefreg_id"), enquiry_prefreg_id)).append(">")
						.append(crs.getString("prefreg_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFinance() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", enquiry_finance)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_finance)).append(">No</option>\n");
		return Str.toString();
	}

	public String PopulateBudget() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT budget_id, budget_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_budget" + " WHERE 1=1" + " ORDER BY budget_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("budget_id")).append("").append(StrSelectdrop(crs.getString("budget_id"), enquiry_budget_id)).append(">")
						.append(crs.getString("budget_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRegYear() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>").append("<option value=1").append(StrSelectdrop("1", enquiry_prefregyear)).append(">2015 to 2008</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_prefregyear)).append(">Below 2008</option>\n");
		return Str.toString();
	}

	public String PopulateMileage() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prefmileage_id, prefmileage_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefmileage" + " WHERE 1=1" + " ORDER BY prefmileage_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prefmileage_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prefmileage_id"), enquiry_prefmileage_id));
				Str.append(">").append(crs.getString("prefmileage_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT extcolour_id, extcolour_name" + " FROM axela_preowned_extcolour" + " GROUP BY extcolour_id" + " ORDER BY extcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("extcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("extcolour_id"), enquiry_extcolour_id));
				Str.append(">").append(crs.getString("extcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT intcolour_id, intcolour_name" + " FROM axela_preowned_intcolour" + " GROUP BY intcolour_id" + " ORDER BY intcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("intcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("intcolour_id"), enquiry_intcolour_id));
				Str.append(">").append(crs.getString("intcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirytype_id, enquirytype_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_type" + " WHERE 1=1" + " ORDER BY enquirytype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirytype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirytype_id"), enquiry_enquirytype_id));
				Str.append(">").append(crs.getString("enquirytype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirycat_id, enquirycat_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_cat" + " GROUP BY enquirycat_id" + " ORDER BY enquirycat_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirycat_id")).append("").append(StrSelectdrop(crs.getString("enquirycat_id"), enquiry_enquirycat_id)).append(">")
						.append(crs.getString("enquirycat_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateOccupation() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT occ_id, occ_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ" + " WHERE 1=1" + " ORDER BY occ_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("occ_id")).append("");
				Str.append(StrSelectdrop(crs.getString("occ_id"), enquiry_occ_id));
				Str.append(">").append(crs.getString("occ_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCarUsage() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT carusage_id, carusage_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_carusage" + " WHERE 1=1" + " ORDER BY carusage_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("carusage_id")).append("");
				Str.append(StrSelectdrop(crs.getString("carusage_id"), enquiry_carusage_id));
				Str.append(">").append(crs.getString("carusage_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCustomerType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT custtype_id, custtype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype"
					+ " WHERE 1=1"
					+ " ORDER BY custtype_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("custtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("custtype_id"), enquiry_enquirycustomertype_id));
				Str.append(">").append(crs.getString("custtype_name")).append("</option>\n");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirycat_id, enquirycat_name"
					+ " FROM axela_sales_enquiry_cat"
					+ " WHERE 1=1"
					+ " ORDER BY enquirycat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirycat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirycat_id"), enquiry_enquirycat_id));
				Str.append(">").append(crs.getString("enquirycat_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCorporate(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT corporate_id, corporate_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_corporate"
					+ " WHERE 1=1"
					+ " AND corporate_active = 1 "
					+ " GROUP BY corporate_id"
					+ " ORDER BY corporate_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("corporate_id")).append("");
				Str.append(StrSelectdrop(crs.getString("corporate_id"), enquiry_corporate_id));
				Str.append(">").append(crs.getString("corporate_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDrivingPattern() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", enquiry_drivetype)).append(">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", enquiry_drivetype)).append(">Self</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_drivetype)).append(">Chauffeur</option>\n");
		return Str.toString();
	}

	public String PopulateIncome() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT income_id, income_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_income" + " WHERE 1=1" + " ORDER BY income_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("income_id")).append("");
				Str.append(StrSelectdrop(crs.getString("income_id"), enquiry_enquiryincome_id));
				Str.append(">").append(crs.getString("income_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateAvgKms() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT monthlydrive_id, monthlydrive_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_monthlydrive" + " WHERE 1=1" + " ORDER BY monthlydrive_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("monthlydrive_id")).append("");
				Str.append(StrSelectdrop(crs.getString("monthlydrive_id"), enquiry_monthlydrive_id));
				Str.append(">").append(crs.getString("monthlydrive_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	// ////
	public String PopulateCompBrand() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT compbrand_id, compbrand_name, if(compbrand_name!= 'Others',compbrand_name,'z') AS others" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_compbrand" + " WHERE 1=1"
					+ " ORDER BY others";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("compbrand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("compbrand_id"), enquiry_compbrand_id));
				Str.append(">").append(crs.getString("compbrand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase1() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase1_id, lostcase1_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1" + " WHERE 1=1" + " ORDER BY lostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), enquiry_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase2_id, lostcase2_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2" + " WHERE lostcase2_lostcase1_id = " + enquiry_lostcase1_id
					+ " ORDER BY lostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), enquiry_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3" + " WHERE lostcase3_lostcase2_id = " + enquiry_lostcase2_id
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), enquiry_lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String PopulateIndustry() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT ind_id, ind_desc " + " FROM " + compdb(comp_id) +
	// "axela_customer_ind " + " WHERE 1 = 1 " + " GROUP BY ind_id" +
	// " ORDER BY ind_desc ";
	// CachedRowSet crs =processQuery(StrSqlBreaker(StrSql));
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("ind_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("ind_id"), customer_ind_id));
	// Str.append(">").append(crs.getString("ind_desc")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }

	public String PopulateMonthKms(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT monthkms_id, monthkms_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms" + " ORDER BY monthkms_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("monthkms_id")).append("");
				Str.append(StrSelectdrop(crs.getString("monthkms_id"), enquiry_monthkms_id));
				Str.append(">").append(crs.getString("monthkms_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePurchaseMode(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT purchasemode_id, purchasemode_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode" + " ORDER BY purchasemode_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("purchasemode_id")).append("");
				Str.append(StrSelectdrop(crs.getString("purchasemode_id"), enquiry_purchasemode_id));
				Str.append(">").append(crs.getString("purchasemode_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateIncome(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT income_id, income_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_income" + " ORDER BY income_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("income_id")).append("");
				Str.append(StrSelectdrop(crs.getString("income_id"), enquiry_income_id));
				Str.append(">").append(crs.getString("income_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExpectation(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT expectation_id, expectation_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_expectation" + " ORDER BY expectation_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("expectation_id")).append("");
				Str.append(StrSelectdrop(crs.getString("expectation_id"), enquiry_expectation_id));
				Str.append(">").append(crs.getString("expectation_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBuyerType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT buyertype_id, buyertype_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype" + " ORDER BY buyertype_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("buyertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("buyertype_id"), enquiry_buyertype_id));
				Str.append(">").append(crs.getString("buyertype_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateOwnership(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ownership_id, ownership_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_ownership" + " ORDER BY ownership_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), enquiry_ownership_id));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreOwnedOwnership(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ownership_id, ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " ORDER BY ownership_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), ownership_id));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreownedManufYear() {
		StringBuilder Str = new StringBuilder();
		int currentyear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
		try {
			Str.append("<option value=0>Select</option>");
			for (int i = currentyear; i >= currentyear - 15; i--) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), preowned_manufyear));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCheck(String checkid) {
		try {
			if (!(checkid == null) && (checkid.equals("1"))) {
				return "Checked ";
			}
			return " ";
		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("AxelaAuto-App===" + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return " ";
		}
	}

	public String PopulateOccupation(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_occupation)).append(">Business</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_occupation)).append(">Professional</option>\n");
			Str.append("<option value=\"3\"").append(StrSelectdrop("3", enquiry_mb_occupation)).append(">Salaried</option>\n");
			Str.append("<option value=\"4\"").append(StrSelectdrop("4", enquiry_mb_occupation)).append(">Self-employed</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCarUsageConditions(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_carusage)).append(">Fleet</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_carusage)).append(">Gift</option>\n");
			Str.append("<option value=\"3\"").append(StrSelectdrop("3", enquiry_mb_carusage)).append(">Official</option>\n");
			Str.append("<option value=\"4\"").append(StrSelectdrop("4", enquiry_mb_carusage)).append(">Others</option>\n");
			Str.append("<option value=\"5\"").append(StrSelectdrop("5", enquiry_mb_carusage)).append(">Personal</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAdditionalType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_type)).append(">Corp.</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_type)).append(">Fleet</option>\n");
			Str.append("<option value=\"3\"").append(StrSelectdrop("3", enquiry_mb_type)).append(">Govt.</option>\n");
			Str.append("<option value=\"4\"").append(StrSelectdrop("4", enquiry_mb_type)).append(">Individual</option>\n");
			Str.append("<option value=\"5\"").append(StrSelectdrop("5", enquiry_mb_type)).append(">Others</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDrivingPattern(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_drivingpattern)).append(">Self</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_drivingpattern)).append(">Chauffeur</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAdditionalIncome(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_income)).append(">50-70 Lakhs</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_income)).append("><50 Lakhs</option>\n");
			Str.append("<option value=\"3\"").append(StrSelectdrop("3", enquiry_mb_income)).append(">>75 Lakhs</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateAvgDrivingkms(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select</option>\n");
			Str.append("<option value=\"1\"").append(StrSelectdrop("1", enquiry_mb_avgdriving)).append(">1000-2000</option>\n");
			Str.append("<option value=\"2\"").append(StrSelectdrop("2", enquiry_mb_avgdriving)).append(">2000-4000</option>\n");
			Str.append("<option value=\"3\"").append(StrSelectdrop("3", enquiry_mb_avgdriving)).append("><1000</option>\n");
			Str.append("<option value=\"4\"").append(StrSelectdrop("4", enquiry_mb_avgdriving)).append(">>4000</option>\n");

		} catch (Exception ex) {
			SOPError("AxelaAuto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
