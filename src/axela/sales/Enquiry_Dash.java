package axela.sales;

//
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.customer.Customer_Tags_Check;
import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Enquiry_Dash extends Connect {

	public String StrSql = "";
	public String msg = "", crmmsg = "", crmticketmsg = "";
	public String branch_id = "0";
	public String enquiry_branch_id = "0";
	public String branch_name = "";
	public String branch_brand_id = "0";
	public String enquiry_id = "0";
	public String enquiry_title = "";
	public String enquiry_no = "";
	public String enquiry_date = "";
	public String date = "";
	public String enquiry_contact_id = "0";
	public String enquiry_customer_id = "0";
	static String enquiry_temp_customer_id = "0";
	public String enquiry_close_date = "";
	public String enquirydate = "";
	public String enquiry_value_syscal = "";
	public String enquiry_value = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_email2 = "";
	public String stage_name = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_pin = "";
	public String contact_jobtitle = "";
	public String contact_title_id = "0";
	public String contact_address = "";
	public String enquiry_customer_name = "";
	public String enquiry_desc = "";
	public String enquiry_qcsno = "";
	public String enquiry_avpresent = "";
	public String enquiry_emp_id = "0";
	public String enquiry_manager_assist = "";
	public String enquiry_custtype_id = "0";
	public String enquiry_enquirycat_id = "0";
	public String enquiry_corporate_id = "0";
	public String enquiry_dmsno = "";
	public String enquiry_dms = "";
	public String enquiry_notes = "";
	public String enquiry_priorityenquiry_id = "0";
	public String priorityenquiry_desc = "";
	public String priorityenquiry_duehrs = "0";
	public String enquiry_status_id = "1";
	public String enquiry_status_desc = "";
	public String enquiry_status_date = "";
	public String statusdate = "";
	public String enquirytype_name = "";
	public String vehtype_name = "";
	public String enquiry_lostcase1_id = "";
	public String enquiry_lostcase2_id = "";
	public String enquiry_lostcase3_id = "";
	public int days_diff = 0;
	public String StrHTML = "";
	public String contact_city_id = "";
	public String enquiry_entry_id = "0";
	public String enquiry_entry_date = "";
	public String enquiry_modified_id = "0";
	public String enquiry_modified_date = "";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String ref_emp_name = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String enquiry_lead_id = "0";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String config_sales_enquiry_refno = "";
	public String enquiry_model_id = "0";
	public String enquiry_item_id = "0";
	public String enquiry_add_model_id = "0";
	public String enquiry_option_id = "0";
	public String soe_name = "";
	public String curryear = "", year1 = "";
	public String customerdetail = "";
	// /////understand customer //////--//////------------------
	public String enquiry_age_id = "";
	public String enquiry_occ_id = "";
	// public String enquiry_custid = "";
	public String enquiry_fuelallowance = "";
	public String enquiry_tradein_preownedvariant_id = "0";
	public String enquiry_monthkms_id = "";
	public String enquiry_purchasemode_id = "";
	public String enquiry_income_id = "";
	public String enquiry_familymember_count = "";
	public String enquiry_expectation_id = "";
	public String enquiry_othercar = "";
	public String enquiry_corporate = "";
	public String enquiry_buyertype_id = "";
	public String enquiry_enquirytype_id = "";
	public String enquiry_ownership_id = "";
	public String enquiry_existingvehicle = "";
	public String enquiry_purchasemonth = "";
	public String enquiry_loancompletionmonth = "";
	public String enquiry_currentemi = "";
	public String enquiry_loanfinancer = "";
	public String enquiry_kms = "";
	public String enquiry_expectedprice = "";
	public String enquiry_quotedprice = "";
	// public String enquiry_evaluation = "";
	// ////end of customer///////////
	// public String enquiry_preownedmodel_id = "0";
	public String enquiry_preownedvariant_id = "0";
	public String enquiry_fueltype_id = "0";
	public String enquiry_prefreg_id = "0";
	public String enquiry_presentcar = "";
	public String enquiry_finance = "";

	public String status = "";
	public String followupHTML = "";
	public String followup_followup_time = "";
	public String followup_time = "", followuptime = "";
	public String followup_entry_id = "0";
	public String followup_entry_time = "";
	public String followup_desc = "";
	public String followup_emp_id = "0";
	public String followup_followuptype_id = "0";
	public String followup_feedbacktype_id = "0", followupfeedbacktype_id = "0";
	public String followupdesc_id = "0";
	public String followupdesc_name = "";
	public String followup_id = "0";
	public String delete = "";
	public String submitB = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int PageCount = 10, recperpage = 0;
	public int PageCurrent = 0;
	public String docmsg = "";
	public String crmfollowupHTML = "";
	public String crmfollowup_id = "0", crm = "";
	public String crm_id = "0";
	public String psf_id = "0", psf = "", pbf = "", psfmsg = "";

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

	// Start Ford Fields
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
	public String emp_all_exe = "";
	// End Ford Fields

	// Start MB Fields
	public String enquiry_mb_occupation = "";
	public String enquiry_mb_carusage = "";
	public String enquiry_mb_type = "";
	public String enquiry_mb_drivingpattern = "";
	public String enquiry_mb_income = "";
	public String enquiry_mb_avgdriving = "";
	public String enquiry_mb_currentcars = "";
	public String followup_followupstatus_id = "0";
	// End MB Fields

	// Start preowned
	public String preowned_enquiry_id = "0";
	public String eval_offered_price = "0";
	public String eval_entry_date = "";
	public String fueltype_name = "";
	public String extcolour_name = "";
	public String intcolour_name = "";
	public String ownership_id = "";
	public String team_name = "";
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
	public String enquiry_porsche_industry = "";
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
	// End porsche need assessment fields

	// Start JLR need assessment fields
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
	public String enq_jlr_enquirystatus = "";
	public String jlr_enquirystatus = "";
	public String jlr_enquirystatus_update = "";
	// End JLR need assessment fields

	// Start JLR need assessment fields
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

	// End JLR need assessment fields

	public Enquiry_Dash_Customer Customer_dash = new Enquiry_Dash_Customer();
	public Preowned_Variant_Check variantcheck = new Preowned_Variant_Check();

	public Customer_Tags_Check tagcheck = new Customer_Tags_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				msg = PadQuotes(request.getParameter("msg"));
				crmmsg = PadQuotes(request.getParameter("crmmsg"));
				crmticketmsg = PadQuotes(request.getParameter("crmticketmsg"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				curryear = SplitYear(ToLongDate(kknow()));
				year1 = (Integer.parseInt(curryear) + 10) + "";
				docmsg = PadQuotes(request.getParameter("docmsg"));
				crm_id = CNumeric(PadQuotes(request.getParameter("crm_id")));
				followup_id = CNumeric(PadQuotes(request.getParameter("followup_id")));

				branch_brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_branch_id = branch_id"
						+ " WHERE 1=1"
						+ " AND enquiry_id = " + enquiry_id));
				if (branch_brand_id.equals("11")) {
					PopulateSkodaNeedAssessmentFields(enquiry_id, comp_id);
				}
				// PopulatePorscheNeedAssessmentFields(comp_id, enquiry_id);
				PopulateConfigDetails();
				PopulateFields(response);
				// SOP("Tags=====" + tags);
				followupHTML = ListFollowup(enquiry_id);
				customerdetail = ListCustomerDetails();
				psfmsg = PadQuotes(request.getParameter("psfmsg"));
				crm = PadQuotes(request.getParameter("crm"));
				psf = PadQuotes(request.getParameter("psf"));
				pbf = PadQuotes(request.getParameter("pbf"));
				psf_id = CNumeric(PadQuotes(request.getParameter("psf_id")));
				crmfollowup_id = CNumeric(PadQuotes(request.getParameter("crmfollowup_id")));

				if ("yes".equals(delete) && !followup_id.equals("0")
						&& emp_id.equals("1")) {
					DeleteFields();
					response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id));
				}

				if ("yes".equals(delete)
						&& emp_id.equals("1")
						&& (crm.equals("yes") || psf.equals("yes") || pbf.equals("yes"))) {
					// //if(AppRun().equals("0")) {
					DeleteCustomCRMFields(crm_id);

					if (crm.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id
								+ "&psfmsg=CRM Follow-up deleted successfully!"));
					} else if (pbf.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id
								+ "&psfmsg=PBF deleted successfully!"));
					} else if (psf.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id
								+ "&psfmsg=PSF deleted successfully!"));
					}
				}

				if ("Submit".equals(submitB)) {
					PopulateFields(response);
					GetValues(request);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						followup_entry_id = emp_id;
						followup_entry_time = ToLongDate(kknow());
						if (status.equals("Add")) {
							AddFields();
						} else {
							UpdateFields();
						}
						response.sendRedirect(response.encodeRedirectURL("enquiry-dash.jsp?enquiry_id=" + enquiry_id
								+ "&msg=Follow-up added successfully!#tabs-2"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListCustomerDetails() {

		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT"
					+ " customer_id,"
					+ " customer_name,"
					+ " contact_id,"
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts,"
					+ " contact_mobile1, contact_mobile2, model_name, item_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id=enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " WHERE followup_enquiry_id = " + enquiry_id
					+ " GROUP BY customer_id";
			// SOP("ListCustomerDetails----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				while (crs.next()) {
					Str.append("<div class=\"container-fluid \">");
					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\"text-right col-md-6 control-label\" style=\"top: -1px;\">Customer: &nbsp;</label>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>");
					Str.append("</div>");
					Str.append("</div>");

					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\"text-right col-md-2 control-label\" style=\"top: -1px;\">Contact: &nbsp;</label>");
					Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contacts") + "</a>");
					Str.append("</div>");
					Str.append("</div>");
					Str.append("</div>");

					//
					Str.append("<div class=\"container-fluid \">");
					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\" text-right col-md-6 control-label\" style=\"top: -1px;\">Mobile1: &nbsp;</label>");
					Str.append(crs.getString("contact_mobile1")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					Str.append("</div>");
					Str.append("</div>");

					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\" text-right col-md-2 control-label\" style=\"top: -1px;\">Mobile2: &nbsp;</label>");
					Str.append(crs.getString("contact_mobile2"));
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}
					Str.append("</div>");
					Str.append("</div>");
					Str.append("</div>");

					//
					Str.append("<div class=\"container-fluid \">");
					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\" text-right col-md-6 control-label\" style=\"top: -1px;\">Model: &nbsp;</label>");
					Str.append(crs.getString("model_name"));
					Str.append("</div>");
					Str.append("</div>");

					Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
					Str.append("<div class=\"form-group\">");
					Str.append("<label class=\" text-right col-md-2 control-label\" style=\"top: -1px;\">Variant: &nbsp;</label>");
					Str.append(crs.getString("item_name"));
					Str.append("</div>");
					Str.append("</div>");
					Str.append("</div>");

				}
				crs.close();

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}

		}
		return Str.toString();
	}

	protected void UpdateFields() {
		if (!comp_id.equals("0")) {
			if (msg.equals("")) {
				try {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " SET "
							+ " followup_followupstatus_id = " + followup_followupstatus_id + ","
							+ " followup_feedbacktype_id = " + followup_feedbacktype_id + ","
							+ " followup_desc = '" + followup_desc + "',"
							+ " followup_modified_id = " + emp_id + ", "
							+ " followup_modified_time = '" + ToLongDate(kknow()) + "'"
							+ " WHERE followup_desc=''"
							+ " AND followup_enquiry_id = " + enquiry_id;
					// SOP("StrSql====update enq follow=======" + StrSql);
					updateQuery(StrSql);
					if (branch_brand_id.equals("60")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_jlr_enquirystatus = '" + enq_jlr_enquirystatus + "'"
								+ " WHERE enquiry_id = " + enquiry_id;
						updateQuery(StrSql);

						if (!jlr_enquirystatus.equals(enq_jlr_enquirystatus)) {
							String history_actiontype = "Enquiry Status";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue) "
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + jlr_enquirystatus + "',"
									+ " '" + enq_jlr_enquirystatus + "')";
							updateQuery(StrSql);
							jlr_enquirystatus_update = "no";
						}
					}
					if (enquiry_status_id.equals("1")) {
						// assigning the variable to add homevishit followup
						followupfeedbacktype_id = followup_feedbacktype_id;
						// setting the feedback type and desc empty for new followup to add
						AddFields();

						if (followupfeedbacktype_id.equals("9") && msg.equals("")) {
							String date = ToLongDate(kknow());
							new Enquiry_Quickadd().AddCustomCRMFields(enquiry_id, date, "homevisit", comp_id);
						}
					}
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	protected void DeleteFields() {
		if (!comp_id.equals("0"))
		{
			if (msg.equals("")) {
				try {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
							+ " WHERE followup_id = " + followup_id + "";
					updateQuery(StrSql);
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	public void AddFields() {
		if (!comp_id.equals("0")) {
			CheckForm();
			if (msg.equals("")) {
				try {
					if (!status.equals("add")) {
						followup_desc = "";
						followup_feedbacktype_id = "0";
					}
					// StrSql = "UPDATE " + compdb(comp_id) +
					// "axela_sales_enquiry_followup "
					// + " SET followup_desc= '" + followup_desc + "',"
					// + " followup_entry_id = " + followup_entry_id + ", "
					// + " followup_entry_time = '" + followup_entry_time + "' "
					// + " WHERE followup_desc='' AND followup_enquiry_id = " +
					// enquiry_id;
					// updateQuery(StrSql);
					StrSql = " INSERT"
							+ " INTO " + compdb(comp_id) + "axela_sales_enquiry_followup "
							+ " ( "
							+ " followup_enquiry_id, "
							+ " followup_emp_id, "
							+ " followup_followuptype_id, "
							+ " followup_followup_time, "
							+ " followup_feedbacktype_id, "
							+ " followup_desc, "
							+ " followup_entry_id, "
							+ " followup_entry_time, "
							+ " followup_trigger) "
							+ " VALUES "
							+ "("
							+ "'" + enquiry_id + "',"
							+ "" + followup_emp_id + ", "
							+ "" + followup_followuptype_id + ", "
							+ " '" + ConvertLongDateToStr(followup_followup_time) + "', "
							+ "" + followup_feedbacktype_id + ", "
							+ " '" + followup_desc + "', "
							+ "" + followup_entry_id + ", "
							+ " '" + followup_entry_time + "', "
							+ "0 "
							+ ")";
					// SOP("StrSql=====" + StrSql);
					updateQuery(StrSql);

					if (branch_brand_id.equals("60") && !jlr_enquirystatus_update.equals("no")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_jlr_enquirystatus = '" + enq_jlr_enquirystatus + "'"
								+ " WHERE enquiry_id = " + enquiry_id;
						updateQuery(StrSql);

						if (!jlr_enquirystatus.equals(enq_jlr_enquirystatus)) {
							String history_actiontype = "Enquiry Status";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue) "
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + jlr_enquirystatus + "',"
									+ " '" + enq_jlr_enquirystatus + "')";
							updateQuery(StrSql);
						}
					}
					// for setting enquiry priority
					EnquiryPriorityUpdate(comp_id, enquiry_id);
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	protected void GetValues(HttpServletRequest request) {
		// followupdesc_id = CNumeric(PadQuotes(request.getParameter("dr_followup_desc")));
		followup_followupstatus_id = CNumeric(PadQuotes(request.getParameter("dr_followupstatus")));
		followup_feedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_followup_feedbacktype_id")));
		enq_jlr_enquirystatus = PadQuotes(request.getParameter("drop_enquiry_jlr_status"));
		followup_desc = PadQuotes(request.getParameter("txt_followup_desc"));
		followup_followuptype_id = PadQuotes(request.getParameter("dr_followuptype"));
		followup_followup_time = PadQuotes(request.getParameter("txt_followup_time"));
		// Start Nexa Fields
		enquiry_nexa_gender = PadQuotes(request.getParameter("dr_enquiry_nexa_gender"));
		enquiry_nexa_beveragechoice = PadQuotes(request.getParameter("dr_enquiry_nexa_beveragechoice"));
		enquiry_nexa_autocard = PadQuotes(request.getParameter("dr_enquiry_nexa_autocard"));
		enquiry_nexa_fueltype = PadQuotes(request.getParameter("dr_enquiry_nexa_fueltype"));
		enquiry_nexa_specreq = PadQuotes(request.getParameter("dr_enquiry_nexa_specreq"));
		enquiry_nexa_testdrivereq = PadQuotes(request.getParameter("dr_enquiry_nexa_testdrivereq"));
		enquiry_nexa_testdrivereqreason = PadQuotes(request.getParameter("txt_enquiry_nexa_testdrivereqreason"));
		// End Nexa Fields

		// Start Hyundai Fields
		enquiry_hyundai_chooseoneoption = PadQuotes(request.getParameter("dr_enquiry_hyundai_chooseoneoption"));
		enquiry_hyundai_kmsinamonth = PadQuotes(request.getParameter("dr_enquiry_hyundai_kmsinamonth"));
		enquiry_hyundai_membersinthefamily = PadQuotes(request.getParameter("dr_enquiry_hyundai_membersinthefamily"));
		enquiry_hyundai_finalizenewcar = PadQuotes(request.getParameter("dr_enquiry_hyundai_finalizenewcar"));
		enquiry_hyundai_modeofpurchase = PadQuotes(request.getParameter("dr_enquiry_hyundai_modeofpurchase"));
		enquiry_hyundai_annualincome = PadQuotes(request.getParameter("dr_enquiry_hyundai_annualincome"));
		enquiry_hyundai_membersinthefamily = PadQuotes(request.getParameter("dr_enquiry_hyundai_membersinthefamily"));
		enquiry_hyundai_othercars = PadQuotes(request.getParameter("dr_enquiry_hyundai_othercars"));
		enquiry_hyundai_currentcars = PadQuotes(request.getParameter("txt_enquiry_hyundai_currentcars"));
		enquiry_hyundai_dob = PadQuotes(request.getParameter("txt_enquiry_hyundai_dob"));
		// End Hyundai Fields

		// Start Ford Fields
		enquiry_ford_customertype = PadQuotes(request.getParameter("dr_enquiry_custtype_id"));
		enquiry_ford_intentionpurchase = PadQuotes(request.getParameter("dr_enquiry_ford_intentionpurchase"));
		enquiry_ford_kmsdriven = PadQuotes(request.getParameter("txt_enquiry_ford_kmsdriven"));
		enquiry_ford_newvehfor = PadQuotes(request.getParameter("dr_enquiry_ford_newvehfor"));
		enquiry_ford_investment = PadQuotes(request.getParameter("txt_enquiry_ford_investment"));
		enquiry_ford_colourofchoice = PadQuotes(request.getParameter("dr_enquiry_ford_colourofchoice"));
		enquiry_ford_cashorfinance = PadQuotes(request.getParameter("dr_enquiry_ford_cashorfinance"));
		enquiry_ford_currentcar = PadQuotes(request.getParameter("txt_enquiry_ford_currentcar"));
		enquiry_ford_exchangeoldcar = PadQuotes(request.getParameter("dr_enquiry_ford_exchangeoldcar"));
		enquiry_ford_othercarconcideration = PadQuotes(request.getParameter("txt_enquiry_ford_othercarconcideration"));
		enquiry_ford_ex_make = PadQuotes(request.getParameter("txt_enquiry_ford_ex_make"));
		enquiry_ford_ex_model = PadQuotes(request.getParameter("txt_enquiry_ford_ex_model"));
		enquiry_ford_ex_derivative = PadQuotes(request.getParameter("txt_enquiry_ford_ex_derivative"));
		enquiry_ford_ex_year = PadQuotes(request.getParameter("dr_enquiry_ford_ex_year"));
		enquiry_ford_ex_odoreading = PadQuotes(request.getParameter("txt_enquiry_ford_ex_odoreading"));
		enquiry_ford_ex_doors = PadQuotes(request.getParameter("dr_enquiry_ford_ex_doors"));
		enquiry_ford_ex_bodystyle = PadQuotes(request.getParameter("dr_enquiry_ford_ex_bodystyle"));
		enquiry_ford_ex_enginesize = PadQuotes(request.getParameter("txt_enquiry_ford_ex_enginesize"));
		enquiry_ford_ex_fueltype = PadQuotes(request.getParameter("dr_enquiry_ford_ex_fueltype"));
		enquiry_ford_ex_drive = PadQuotes(request.getParameter("dr_enquiry_ford_ex_drive"));
		enquiry_ford_ex_transmission = PadQuotes(request.getParameter("dr_enquiry_ford_ex_transmission"));
		enquiry_ford_ex_colour = PadQuotes(request.getParameter("dr_enquiry_ford_ex_colour"));
		enquiry_ford_ex_priceoffered = PadQuotes(request.getParameter("txt_enquiry_ford_ex_priceoffered"));
		enquiry_ford_ex_estmtprice = PadQuotes(request.getParameter("txt_enquiry_ford_ex_estmtprice"));
		enquiry_ford_vistacontractnumber = PadQuotes(request.getParameter("txt_enquiry_ford_vistacontractnumber"));
		enquiry_ford_nscordernumber = PadQuotes(request.getParameter("txt_enquiry_ford_nscordernumber"));
		enquiry_ford_qcsid = PadQuotes(request.getParameter("txt_enquiry_ford_qcsid"));
		enquiry_ford_qpdid = PadQuotes(request.getParameter("txt_enquiry_ford_qpdid"));
		// End Ford Fields
	}

	public String PopulateFollowupDesc(String comp_id) {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT feedbacktype_id, feedbacktype_name "
					+ "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type "
					+ "WHERE 1=1 "
					+ "ORDER BY feedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("feedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("feedbacktype_id"), followup_feedbacktype_id));
				Str.append(">").append(crs.getString("feedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFollowuptype(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name "
					+ "FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup_type "
					+ "WHERE 1=1 "
					+ "ORDER BY followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("followuptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), followup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListFollowup(String enquiry_id) throws SQLException,
			IOException {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT * FROM ("
					+ " (SELECT followup_id, followup_enquiry_id, followup_followup_time,followup_desc, "
					+ " followup_entry_time, followup_entry_id, followup_emp_id,"
					+ " followuptype_name, followup_modified_id, followup_modified_time,";
			if (branch_brand_id.equals("55")) {
				StrSql += " COALESCE (followupstatus_name, '') AS followupstatus_name,";
			}
			StrSql += " COALESCE(feedbacktype_name, '') AS feedbacktype_name,"
					+ " CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by,"
					+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by,"
					+ " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = followup_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = followup_entry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type ON feedbacktype_id = followup_feedbacktype_id";
			if (branch_brand_id.equals("55")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_status ON followupstatus_id = followup_followupstatus_id ";
			}
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = followup_modified_id"
					+ " WHERE 1 = 1"
					+ " AND followup_enquiry_id = " + enquiry_id
					+ " )"
					+ " UNION "
					+ " (SELECT 0, crm_enquiry_id, crm_followup_time, crm_desc,"
					+ " crm_entry_time, crm_entry_id, crm_emp_id, 'CRM Follow-up',"
					+ " crm_modified_id, crm_modified_time,";
			if (branch_brand_id.equals("55")) {
				StrSql += " '',"; // for status
			}
			StrSql += " COALESCE(crmfeedbacktype_name,'') AS crmfeedbacktype_name, CONCAT(exe.emp_name,' (',exe.emp_ref_no,')') AS followup_by,"
					+ " CONCAT(entry.emp_name,' (',entry.emp_ref_no,')') AS entry_by,"
					+ " COALESCE(CONCAT(modified.emp_name,' (',modified.emp_ref_no,')'), '') AS modified_by  "
					+ " FROM " + compdb(comp_id) + "axela_sales_crm "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = crm_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp entry ON entry.emp_id = crm_entry_id "
					+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modified ON modified.emp_id = crm_modified_id "
					+ " WHERE 1 = 1 and crm_enquiry_id = " + enquiry_id
					+ " AND crm_desc !='')" + " ) AS t "
					+ " ORDER BY followup_followup_time ";
			// SOP("StrSql---------ListFollowup---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = 0;
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
					Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th width=5% data-toggle=\"true\">#</th>\n");
					Str.append("<th>Time</th>\n");
					Str.append("<th width=\"10%\">Follow-up Type</th>\n");
					Str.append("<th data-hide=\"phone\">Follow-up Description</th>\n");
					Str.append("<th width=\"10%\" data-hide=\"phone\">Feedback Type</th>\n");
					if (branch_brand_id.equals("55")) {
						Str.append("<th data-hide=\"phone\">Status</th>\n");
					}
					Str.append("<th data-hide=\"phone\">Sales Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Feedback by</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Entry By</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>");
						Str.append("<td valign=top align=left >").append(strToLongDate(crs.getString("followup_followup_time")));
						if (emp_id.equals("1") && !crs.getString("followup_id").equals("0")) {
							Str.append("<br><a href=\"enquiry-dash.jsp?Delete=yes&enquiry_id=")
									.append(crs.getString("followup_enquiry_id")).append("&followup_id=")
									.append(crs.getString("followup_id")).append(" \">Delete Follow-up</a>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("followuptype_name")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("followup_desc")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("feedbacktype_name")).append("</td>");
						if (branch_brand_id.equals("55")) {
							Str.append("<td valign=top align=left >").append(crs.getString("followupstatus_name")).append("</td>");
						}

						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_emp_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_emp_id"))
									.append(">").append(crs.getString("followup_by")).append("</a>");

						}
						Str.append("&nbsp;</td>");
						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_modified_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_modified_id"))
									.append(">").append(crs.getString("modified_by")).append("</a>");
							Str.append("<br>").append(strToLongDate(crs.getString("followup_modified_time")));
						}
						Str.append("&nbsp;</td>");
						Str.append("<td valign=top align=left >");
						if (!crs.getString("followup_entry_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
									.append(crs.getString("followup_entry_id"))
									.append(">").append(crs.getString("entry_by")).append("</a>");
							Str.append("<br>").append(strToLongDate(crs.getString("followup_entry_time")));
						}
						Str.append("&nbsp;</td>");
						Str.append("</tr>");

						if (crs.getString("followup_desc").equals("")) {
							status = "Update";
						}
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				} else {
					status = "Add";
					msg = "<br><font color=red><b>No follow-up found!</b></font>";
					// Str.append(msg);
				}
				// / condition removed in according to provide followup for closed enq also
				if (status.equals("")) { // && enquiry_status_id.equals("1")
					status = "Add";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		// SOP("Follow Up======" + Str);
		return Str.toString();
	}
	protected void PopulateFields(HttpServletResponse response) {

		try {
			if (!comp_id.equals("0"))
			{
				StrSql = "SELECT " + compdb(comp_id) + "axela_sales_enquiry.*, "
						+ " COALESCE(CONCAT(ref.emp_name,' (', ref.emp_ref_no, ')'),'') AS ref_emp_name,"
						+ " COALESCE(enquirytype_name, '') AS enquirytype_name, "
						+ " COALESCE(vehtype_name, '') AS vehtype_name,"
						+ " customer_name, contact_title_id, contact_fname, contact_lname, contact_jobtitle, "
						+ " contact_mobile1, contact_mobile2,  contact_phone1, contact_phone2, contact_email1, contact_email2, contact_address, "
						+ " contact_city_id, contact_pin, "
						+ " branch_name, branch_code, branch_brand_id, stage_name, priorityenquiry_desc, priorityenquiry_duehrs,"
						+ " COALESCE(soe_name,'') AS soe_name, enquiry_corporate_id,"
						// + " enquiry_evaluation, "
						// Start Nexa Fields
						+ " enquiry_nexa_gender,"
						+ " enquiry_nexa_beveragechoice, enquiry_nexa_autocard, enquiry_nexa_fueltype, enquiry_nexa_specreq, "
						+ " enquiry_nexa_testdrivereq, enquiry_nexa_testdrivereqreason, "
						// End Nexa Fields

						// Start Hyundai Fields
						+ " enquiry_hyundai_chooseoneoption, enquiry_hyundai_kmsinamonth, enquiry_hyundai_membersinthefamily, "
						+ " enquiry_hyundai_topexpectation, enquiry_hyundai_finalizenewcar, enquiry_hyundai_modeofpurchase, enquiry_hyundai_annualincome, "
						+ " enquiry_hyundai_othercars, enquiry_hyundai_currentcars, enquiry_hyundai_dob, "
						// End Hyundai Fields

						// Start Ford Fields
						+ " enquiry_ford_customertype, enquiry_ford_intentionpurchase, enquiry_ford_kmsdriven, enquiry_ford_newvehfor, enquiry_ford_investment, enquiry_ford_colourofchoice,"
						+ " enquiry_ford_cashorfinance, enquiry_ford_currentcar, enquiry_ford_exchangeoldcar, enquiry_ford_othercarconcideration, enquiry_ford_ex_make,"
						+ " enquiry_ford_ex_model, enquiry_ford_ex_derivative, enquiry_ford_ex_year, enquiry_ford_ex_odoreading, enquiry_ford_ex_doors, enquiry_ford_ex_bodystyle,"
						+ " enquiry_ford_ex_enginesize, enquiry_ford_ex_fueltype, enquiry_ford_ex_drive, enquiry_ford_ex_transmission, enquiry_ford_ex_colour, enquiry_ford_ex_priceoffered,"
						+ " enquiry_ford_ex_estmtprice, enquiry_ford_vistacontractnumber, enquiry_ford_nscordernumber, enquiry_ford_qcsid, enquiry_ford_qpdid, "
						// End Ford Fields
						// Start MB fields
						+ " enquiry_mb_occupation, enquiry_mb_carusage, enquiry_mb_type, enquiry_mb_drivingpattern, "
						+ " enquiry_mb_income, enquiry_mb_avgdriving, enquiry_mb_currentcars, "
						// End MB fields
						// Pre-owned fields starts
						+ " COALESCE(preowned_enquiry_id,'0') AS preowned_enquiry_id,"
						+ " COALESCE(preowned_sub_variant,'') AS preowned_sub_variant,"
						+ " COALESCE((SELECT fueltype_name"
						+ " FROM " + compdb(comp_id) + "axela_fueltype"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_fueltype_id = fueltype_id "
						// + " INNER JOIN " + compdb(comp_id) +
						// "axela_sales_enquiry ON enquiry_id = preowned_enquiry_id"
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
						+ " COALESCE(preowned_kms,'') AS preowned_kms,"
						+ " COALESCE(preowned_fcamt,'') AS preowned_fcamt,"
						+ " COALESCE(preowned_manufyear,'') AS preowned_manufyear,"
						+ " COALESCE((SELECT ownership_id"
						+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_ownership_id = ownership_id "
						+ " WHERE  preowned_enquiry_id = " + enquiry_id + "),'') AS ownership_id,"
						+ " enquiry_tradein_preownedvariant_id,"
						+ " COALESCE(eval_offered_price, 0) AS eval_offered_price, "
						+ " COALESCE(eval_entry_date, '') AS eval_entry_date,"
						+ " COALESCE(preowned_expectedprice,'') AS preowned_expectedprice,"
						// Pre-owned fields Ends

						// Start Porsche details
						+ " enquiry_porsche_gender, enquiry_porsche_nationality, enquiry_porsche_language, enquiry_porsche_industry,"
						+ " enquiry_porsche_religion, enquiry_porsche_preferredcomm, enquiry_porsche_socialmediapref,"
						+ " enquiry_porsche_maritalstatus, enquiry_porsche_spousename, enquiry_porsche_spousedrive,"
						+ " enquiry_porsche_interest, enquiry_porsche_clubmembership, enquiry_porsche_financeoption,"
						+ " enquiry_porsche_insuranceoption, enquiry_porsche_vehicleinhouse, enquiry_porsche_householdcount,"
						+ " contact_anniversary, contact_dob,"
						// End Porsche details

						// Start AMP fields
						+ " enquiry_jlr_employmentstatus, enquiry_jlr_industry, enquiry_jlr_birthday, enquiry_jlr_gender,"
						+ " enquiry_jlr_occupation, enquiry_jlr_currentvehicle, enquiry_jlr_othermodelofinterest,"
						+ " enquiry_jlr_financeinterest, enquiry_jlr_noofchildren, enquiry_jlr_noofpeopleinhousehold,"
						+ " enquiry_jlr_highnetworth, enquiry_jlr_householdincome, enquiry_jlr_interests, enquiry_jlr_annualrevenue,"
						+ " enquiry_jlr_noofemployees, enquiry_jlr_accounttype, enquiry_jlr_enquirystatus, contact_dob,"
						// End AMP fields

						+ " COALESCE((SELECT team_name"
						+ " FROM " + compdb(comp_id) + " axela_sales_team"
						+ " INNER JOIN " + compdb(comp_id) + " axela_sales_team_exe ON teamtrans_team_id = team_id"
						+ " WHERE teamtrans_emp_id = enquiry_emp_id),'') AS team_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
						// + " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = enquiry_emp_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ref ON ref.emp_id = enquiry_refemp_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id "
						+ " LEFT JOIN axela_veh_type ON vehtype_id = model_vehtype_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned ON preowned_enquiry_id = enquiry_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id"
						+ " WHERE enquiry_id=" + enquiry_id
						+ BranchAccess
						+ ExeAccess.replace("emp_id", "enquiry_emp_id");
				StrSql += " GROUP BY enquiry_id ";
				// SOP("Dash-----PopulateFields-------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						ref_emp_name = crs.getString("ref_emp_name");
						enquiry_id = crs.getString("enquiry_id");
						enquiry_lead_id = crs.getString("enquiry_lead_id");
						enquiry_model_id = crs.getString("enquiry_model_id");
						enquiry_item_id = crs.getString("enquiry_item_id");
						enquiry_add_model_id = crs.getString("enquiry_add_model_id");
						enquiry_option_id = crs.getString("enquiry_option_id");
						enquiry_age_id = crs.getString("enquiry_age_id");
						enquiry_occ_id = crs.getString("enquiry_occ_id");
						enquiry_custtype_id = crs.getString("enquiry_custtype_id");
						enquiry_enquirycat_id = crs.getString("enquiry_enquirycat_id");
						// SOP("enquiry_enquirycat_id---" + enquiry_enquirycat_id);
						enquiry_corporate_id = crs.getString("enquiry_corporate_id");
						// enquiry_custid = crs.getString("enquiry_custid");
						enquiry_fuelallowance = crs.getString("enquiry_fuelallowance");
						enquiry_monthkms_id = crs.getString("enquiry_monthkms_id");
						enquiry_purchasemode_id = crs.getString("enquiry_purchasemode_id");
						enquiry_income_id = crs.getString("enquiry_income_id");
						enquiry_familymember_count = crs.getString("enquiry_familymember_count");
						enquiry_expectation_id = crs.getString("enquiry_expectation_id");
						enquiry_othercar = crs.getString("enquiry_othercar");
						enquiry_corporate = crs.getString("enquiry_corporate");
						enquiry_buyertype_id = crs.getString("enquiry_buyertype_id");
						enquiry_ownership_id = crs.getString("enquiry_ownership_id");
						enquiry_existingvehicle = crs.getString("enquiry_existingvehicle");
						enquiry_purchasemonth = strToShortDate(crs.getString("enquiry_purchasemonth"));
						enquiry_loancompletionmonth = crs.getString("enquiry_loancompletionmonth");
						enquiry_currentemi = crs.getString("enquiry_currentemi");
						enquiry_loanfinancer = crs.getString("enquiry_loanfinancer");
						enquiry_kms = crs.getString("enquiry_kms");
						enquiry_expectedprice = crs.getString("enquiry_expectedprice");
						enquiry_quotedprice = crs.getString("enquiry_quotedprice");
						// enquiry_evaluation = crs.getString("enquiry_evaluation");
						enquiry_title = crs.getString("enquiry_title");
						enquiry_no = "ENQ" + crs.getString("branch_code") + crs.getString("enquiry_no");
						enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");

						enquirytype_name = crs.getString("enquirytype_name"); // vehtype_name
						enquirytype_name = enquirytype_name.replace("Car", crs.getString("vehtype_name"));
						// vehtype_name = crs.getString("vehtype_name");

						// enquiry_preownedmodel_id =
						// crs.getString("enquiry_preownedmodel_id");
						enquiry_preownedvariant_id = crs.getString("enquiry_preownedvariant_id");
						enquiry_fueltype_id = crs.getString("enquiry_fueltype_id");
						enquiry_prefreg_id = crs.getString("enquiry_prefreg_id");
						enquiry_finance = crs.getString("enquiry_finance");
						enquiry_presentcar = crs.getString("enquiry_presentcar");
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						branch_name = crs.getString("branch_name");
						branch_brand_id = crs.getString("branch_brand_id");
						// SOP("branch_brand_id-------" + branch_brand_id);
						enquiry_date = ConvertShortDateToStr(strToShortDate(crs.getString("enquiry_date")));
						date = strToShortDate(enquiry_date);
						enquiry_close_date = crs.getString("enquiry_close_date");
						enquirydate = strToShortDate(enquiry_close_date);
						enquiry_desc = crs.getString("enquiry_desc");
						enquiry_emp_id = crs.getString("enquiry_emp_id");
						enquiry_customer_id = crs.getString("enquiry_customer_id");
						enquiry_temp_customer_id = enquiry_customer_id;
						enquiry_contact_id = crs.getString("enquiry_contact_id");
						enquiry_qcsno = crs.getString("enquiry_qcsno");
						days_diff = (int) (getDaysBetween(ToShortDate(kknow()), enquiry_close_date));
						enquiry_value_syscal = crs.getString("enquiry_value_syscal");
						enquiry_value = crs.getString("enquiry_value");
						stage_name = crs.getString("stage_name");
						enquiry_avpresent = crs.getString("enquiry_avpresent");
						enquiry_manager_assist = crs.getString("enquiry_manager_assist");
						enquiry_status_id = crs.getString("enquiry_status_id");
						enquiry_status_date = crs.getString("enquiry_status_date");
						statusdate = strToLongDate(enquiry_status_date);
						enquiry_status_desc = crs.getString("enquiry_status_desc");
						enquiry_lostcase1_id = crs.getString("enquiry_lostcase1_id");
						enquiry_lostcase2_id = crs.getString("enquiry_lostcase2_id");
						enquiry_lostcase3_id = crs.getString("enquiry_lostcase3_id");
						enquiry_priorityenquiry_id = crs.getString("enquiry_priorityenquiry_id");
						priorityenquiry_desc = crs.getString("priorityenquiry_desc");
						priorityenquiry_duehrs = crs.getString("priorityenquiry_duehrs");
						enquiry_notes = crs.getString("enquiry_notes");
						enquiry_customer_name = crs.getString("customer_name");
						contact_title_id = crs.getString("contact_title_id");
						contact_fname = crs.getString("contact_fname");
						contact_lname = crs.getString("contact_lname");
						contact_jobtitle = crs.getString("contact_jobtitle");
						contact_phone1 = crs.getString("contact_phone1");
						contact_mobile1 = crs.getString("contact_mobile1");
						contact_email1 = crs.getString("contact_email1");
						contact_mobile2 = crs.getString("contact_mobile2");
						contact_email2 = crs.getString("contact_email2");
						contact_phone1 = crs.getString("contact_phone1");
						contact_phone2 = crs.getString("contact_phone2");
						contact_address = crs.getString("contact_address");
						contact_pin = crs.getString("contact_pin");
						contact_city_id = crs.getString("contact_city_id");
						enquiry_dmsno = crs.getString("enquiry_dmsno");
						enquiry_dms = crs.getString("enquiry_dms");
						soe_name = crs.getString("soe_name");
						enquiry_entry_id = crs.getString("enquiry_entry_id");
						entry_by = Exename(comp_id, crs.getInt("enquiry_entry_id"));
						entry_date = strToLongDate(crs.getString("enquiry_entry_date"));
						enquiry_modified_id = crs.getString("enquiry_modified_id");
						if (!enquiry_modified_id.equals("0")) {
							modified_by = Exename(comp_id, Integer.parseInt(enquiry_modified_id));
							modified_date = strToLongDate(crs.getString("enquiry_modified_date"));
						}
						followup_emp_id = crs.getString("enquiry_emp_id");
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
						enquiry_hyundai_annualincome = unescapehtml(crs.getString("enquiry_hyundai_annualincome"));
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

						enquiry_tradein_preownedvariant_id = crs.getString("enquiry_tradein_preownedvariant_id");

						// Start MB fields
						enquiry_mb_occupation = crs.getString("enquiry_mb_occupation");
						enquiry_mb_carusage = crs.getString("enquiry_mb_carusage");
						enquiry_mb_type = crs.getString("enquiry_mb_type");
						enquiry_mb_drivingpattern = crs.getString("enquiry_mb_drivingpattern");
						enquiry_mb_income = crs.getString("enquiry_mb_income");
						enquiry_mb_avgdriving = crs.getString("enquiry_mb_avgdriving");
						enquiry_mb_currentcars = crs.getString("enquiry_mb_currentcars");
						// End MB fields

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

						// start JLR fields
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
						// end JLR fields

						// Start Evaluation
						eval_offered_price = crs.getString("eval_offered_price");
						eval_entry_date = crs.getString("eval_entry_date");
						if (!eval_entry_date.equals("")) {
							eval_entry_date = strToLongDate(eval_entry_date);
						} else {
							eval_entry_date = "Not Evaluated.";
						}
						// End Evaluation
						// Start Preowned
						preowned_enquiry_id = crs.getString("preowned_enquiry_id");
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
						// team start
						team_name = crs.getString("team_name");
						// team end
					}
				} else {
					// //msg = "<br><br>No Enquiry found!";
					response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String ListDocs(String enquiry_id, String PageCurrents,
			String QueryString, int recperpage, String comp_id)
			throws SQLException,
			IOException {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			int TotalRecords = 0;
			String CountSql = "";
			String SqlJoin = "";
			int StartRec = 0;
			int EndRec = 0;
			String PageURL = "";
			int PageListSize = 10;
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			StrSql = " SELECT doc_id, doc_value, doc_title, doc_remarks ";

			CountSql = " SELECT Count(doc_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry_docs "
					+ " WHERE 1=1 "
					+ " and doc_enquiry_id=" + enquiry_id + "";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			// if (!(StrSearch.equals(""))) {
			// StrSql = StrSql + StrSearch;
			// CountSql = CountSql + StrSearch;
			// }
			// Str.append("<div class=\"table-responsive table-bordered\">\n");
			// Str.append("<TABLE width=\"100%\" border=\"0\" align=\"center\" cellPadding=\"0\" cellSpacing=\"0\">");
			// Str.append("<TR>");
			// Str.append("<TD align=\"center\" vAlign=\"top\">");
			// Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\" >");
			// Str.append("<TBODY>");
			// Str.append("<tr>");
			Str.append("<div align=\"right\"><a href=\"../portal/docs-update.jsp?add=yes&enquiry_id=")
					.append(enquiry_id).append("\" >Add New Document...</a></div><br>");
			Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Document(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "enquiry-dash-docs.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY doc_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
				Str.append("<tr>");
				Str.append("<td align=\"center\"><font color=\"#ff0000\" ><b> " + docmsg + "</b></font></td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td align=\"center\"><strong>" + RecCountDisplay + "</strong></td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td align=\"center\">" + PageNaviStr + "</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td height=\"200\" valign=\"top\" align=\"center\">");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead>\n");
				CachedRowSet crs = processQuery(StrSql, 0);

				try {
					int count = StartRec - 1;
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center ><b>").append(count).append(".</b></td>\n");
						if (!crs.getString("doc_value").equals("")) {
							if (!new File(EnquiryDocPath(comp_id)).exists()) {
								new File(EnquiryDocPath(comp_id)).mkdirs();
							}
							File f = new File(EnquiryDocPath(comp_id)
									+ crs.getString("doc_value"));
							Str.append("<td valign=top align=left ><a href=../Fetchdocs.do?" + "enquiry_doc_id=")
									.append(crs.getString("doc_id")).append("><b>")
									.append(crs.getString("doc_title")).append(" (")
									.append(crs.getString("doc_id"))
									.append(")</a></b> (")
									.append(ConvertFileSizeToBytes(FileSize(f)))
									.append(")<br> ")
									.append(crs.getString("doc_remarks"))
									.append("</td>\n");
						} else {
							Str.append("<td valign=top align=left ><b>")
									.append(crs.getString("doc_title")).append(" (")
									.append(crs.getString("doc_id"))
									.append(") (0 Bytes)</b><br> ")
									.append(crs.getString("doc_remarks"))
									.append("</td>\n");
						}
						if (!enquiry_id.equals("0")) {
							Str.append("<td valign=top align=left ><a href=\"../portal/docs-update.jsp?update=yes&enquiry_id=")
									.append(enquiry_id).append("&doc_id=")
									.append(crs.getString("doc_id"))
									.append("\">Update Document</a></td>\n");
						}
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</table>\n");
				} catch (Exception ex) {
					SOPError("AxelaAuto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
				Str.append("</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td align=\"center\">").append(PageNaviStr).append("</td>");
				Str.append("</tr>");
			} else {
				docmsg = "";
				Str.append("<tr><td height=\"200\" valign=\"top\" align=\"center\"><br><br><br><br><font color=red><b>No Document(s) found!</b></font><br><br></td></tr>");
			}
			Str.append("</table>");
		}
		return Str.toString();
	}

	public String ListTestDrive(String comp_id) throws SQLException,
			IOException {
		String confirmed = "";
		String action_info = "";
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT testdrive_id, testdriveveh_id, testdriveveh_name, branch_code,"
					+ " COALESCE(model_name, '') AS  model_name,"
					+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
					+ " COALESCE(testdrive_notes, '') AS testdrive_notes, "
					+ " testdrive_doc_value,"
					+ " enquiry_id,"
					+ " branch_id, CONCAT(branch_name,' (',branch_code,')') AS branchname, "
					+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') AS emp_name, "
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms, "
					+ " testdrive_fb_taken,"
					+ " COALESCE(testdrive_fb_status_id,'0') AS testdrive_fb_status_id,"
					+ " COALESCE(status_name, '') AS status_name,"
					+ " testdrive_fb_status_comments,"
					+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
					+ " testdrive_fb_insurance, testdrive_fb_insurance_comments  "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id= testdrive_fb_status_id"
					+ " WHERE 1=1 AND testdrive_enquiry_id=" + enquiry_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "enquiry_emp_id") + ""
					+ " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_id desc";
			// SOP("ListTestDrive------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				Str.append("<div align=\"right\"><a href=\"../sales/testdrive-update.jsp?add=yes&amp;enquiry_id="
						+ enquiry_id + "\" >Add Test Drive...</a></div><br>");
				if (crs.isBeforeFirst()) {
					int count = 0;
					Str.append("\n <table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Test Drive Details</th>\n");
					Str.append("<th data-hide=\"phone\">Feedback</th>\n");
					if (branch_id.equals("0")) {
						Str.append("<th>Branch</th>\n");
					}
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						if (crs.getString("testdrive_confirmed").equals("0")) {
							confirmed = "<font color=red><b>[Not Confirmed]</b></font>";
						} else {
							confirmed = "";
						}
						Str.append("<tr>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td align=center valign=top><a href=testdrive-list.jsp?testdrive_id=")
								.append(crs.getInt("testdrive_id")).append(">")
								.append(crs.getString("testdrive_id"))
								.append("</a>").append("</td>\n");

						Str.append("</td>\n");
						Str.append("<td  valign=top align=left>Model: <b>")
								.append(crs.getString("model_name")).append("</b>");
						if (crs.getString("testdrive_type").equals("1")) {
							Str.append("<br><b>Main Test Drive</b>");
						} else {
							Str.append("<br><b>Alternate Test Drive</b>");
						}
						Str.append("<br>Vehicle: <b><a href=../sales/managetestdrivevehicle.jsp?veh_id=")
								.append(crs.getString("testdriveveh_id")).append(">")
								.append(crs.getString("testdriveveh_name"))
								.append("</b></a>");
						Str.append("<br>Test Drive Time: <b>")
								.append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");
						if (!crs.getString("testdrive_time_from").equals("")) {
							Str.append("<br>Duration: ")
									.append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
						}
						Str.append("<br>Location: ").append(
								crs.getString("location_name"));
						Str.append("<br>Sales Consultant: <a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>");
						if (!crs.getString("testdrive_notes").equals("")) {
							Str.append("<br>Notes: ").append(crs.getString("testdrive_notes")).append("<br>");
						}
						Str.append(confirmed);
						Str.append("</td>");
						Str.append("<td valign=top align=left nowrap>\n");
						if (crs.getString("testdrive_fb_taken").equals("1")) {
							Str.append("Test Drive Taken");
							if (!crs.getString("status_name").equals("")) {
								Str.append("<br>")
										.append(crs.getString("status_name")).append("<br>")
										.append(crs.getString("testdrive_fb_status_comments"));
							} else {
								if (crs.getDouble("testdrive_fb_budget") != 0) {
									Str.append("<br>Budget: ")
											.append(crs.getString("testdrive_fb_budget")).append("<br>");
								}
								if (crs.getString("testdrive_fb_finance").equals("1")) {
									Str.append("<br>Finance Required<br>%age: ")
											.append(crs.getString("testdrive_fb_finance_amount"));
								} else {
									Str.append(
											"<br>Finance Not Required<br>Comments: ")
											.append(crs.getString("testdrive_fb_finance_comments"));
								}
								if (crs.getString("testdrive_fb_insurance").equals("1")) {
									Str.append("<br>Insurance Required");
								} else {
									Str.append("<br>Insurance Not Required<br>Comments: "
											+ crs.getString("testdrive_fb_insurance_comments"));
								}
							}
						}
						if (crs.getString("testdrive_fb_taken").equals("2")) {
							Str.append("Test Drive not taken");
						}
						if (!crs.getString("testdrive_doc_value").equals("")) {
							File f = new File(TestDriveDocPath(comp_id) + crs.getString("testdrive_doc_value"));
							Str.append("<br><a href=../Fetchdocs.do?image_type=testdrive&testdrive_id=")
									.append(crs.getString("testdrive_id"))
									.append("><b>Test Drive Out Pass (")
									.append(ConvertFileSizeToBytes(FileSize(f)))
									.append(")</b></a>");
						}
						Str.append("&nbsp;</td>\n");
						if (branch_id.equals("0")) {
							Str.append("<td valign=top align=left nowrap ><a href=../portal/branch-summary.jsp?branch_id="
									+ crs.getInt("branch_id") + ">" + crs.getString("branchname") + "</a></td>");
						}

						Str.append("<td nowrap align=center>");
						action_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"testdrive-update.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
								+ "&enquiry_id=" + crs.getString("enquiry_id")
								+ " \">Update Test Drive</a></li>"
								+ "<li role=presentation><a href=\"testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Feedback</a></li>"
								+ "<li role=presentation><a href=\"testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Mileage</a></li>";
						if (crs.getString("testdrive_fb_taken").equals("1")) {
							action_info += "<li role=presentation><a href=\"testdrive-cust-feedback.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
									+ "\">Customer Feedback</a></li>";
						}
						if (!crs.getString("testdrive_out_time").equals("")) {
							action_info += "<li role=presentation><a href=../portal/docs-update.jsp?add=yes&testdrive_id=" + crs.getString("testdrive_id")
									+ "&enquiry_id=" + crs.getString("enquiry_id")
									+ " > Upload Document</a></li>";
						}
						action_info += "<li role=presentation><a href=\"../portal/new-docs-update.jsp?add=yes&testdrive_id=" + crs.getString("testdrive_id")
								+ "\" >Add Driving Licence</a></li>";

						if (branch_brand_id.equals("55")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-mb.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (branch_brand_id.equals("11")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-skoda.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (branch_brand_id.equals("56")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-porsche.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (branch_brand_id.equals("60")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-JLR.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else if (branch_brand_id.equals("151")) {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass-onetriumph.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						} else {
							action_info += "<li role=presentation><a target=_blank href=testdrive-print-gatepass1.jsp?brand_id=" + branch_brand_id
									+ "&testdrive_id=" + crs.getString("testdrive_id")
									+ " >Print Gate Pass</a></li>";
						}
						action_info += "</ul></div></center></div>";
						Str.append(action_info);
						action_info = "";

						// if (branch_brand_id.equals("153")) {
						// Str.append("<br><a href=\"../sales/testdrive-print-gatepass1.jsp?brand_id=" + branch_brand_id + "&testdrive_id="
						// + crs.getString("testdrive_id")
						// + "\" target=_blank>Print Gate Pass</a><br>");
						// }
						// else {
						// Str.append("<br><a href=\"../sales/testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id="
						// + crs.getString("testdrive_id")
						// + "&target=" + Math.random() + "\" target=_blank>Print Gate Pass</a><br>");
						// }

						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("<tbody>\n");
					Str.append("</table>");
				} else {
					Str.append("<center><br><br><font color=red><b>No Test Drive(s) found!</b></font></center>");
				}
				crs.close();

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String ListPreownedTestDrive(String comp_id) throws SQLException,
			IOException {
		String confirmed = "";
		DecimalFormat deci = new DecimalFormat("#");
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT testdrive_id, branch_code,"
					+ " COALESCE(preownedmodel_name, '') AS  preownedmodel_name,"
					+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
					+ " COALESCE(testdrive_notes, '') AS testdrive_notes, "
					+ " testdrive_doc_value,"
					+ " branch_id, CONCAT(branch_name,' (',branch_code,')') AS branchname, "
					+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') AS emp_name, "
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms, "
					+ " testdrive_fb_taken,"
					+ " COALESCE(testdrive_fb_status_id,'0') AS testdrive_fb_status_id,"
					+ " COALESCE(status_name, '') AS status_name,"
					+ " testdrive_enquiry_id, testdrive_preownedstock_id, testdrive_fb_status_comments,"
					+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
					+ " testdrive_fb_insurance, testdrive_fb_insurance_comments  "
					+ " FROM " + compdb(comp_id) + "axela_preowned_testdrive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id = testdrive_location_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = testdrive_preownedstock_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id "
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id "
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = testdrive_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id = testdrive_fb_status_id"
					+ " WHERE 1=1 "
					+ " AND testdrive_enquiry_id=" + enquiry_id
					+ BranchAccess + ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_id desc";
			// SOP("StrSql=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				Str.append("<div align=\"right\"><a href=\"../preowned/preowned-testdrive-update.jsp?add=yes&amp;enquiry_id="
						+ enquiry_id + "\" >Add Pre-Owned Test Drive...</a></div></br>");
				if (crs.isBeforeFirst()) {
					int count = 0;
					Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\" >#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th >Pre-Owned Test Drive Details</th>\n");
					Str.append("<th data-hide=\"phone\" >Feedback</th>\n");
					if (branch_id.equals("0")) {
						Str.append("<th data-hide=\"phone\">Branch</th>\n");
					}
					Str.append("<th data-hide=\"phone\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						if (crs.getString("testdrive_confirmed").equals("0")) {
							confirmed = "<font color=red><b>[Not Confirmed]</b></font>";
						} else {
							confirmed = "";
						}
						Str.append("<tr>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td align=center valign=top><a href=preowned-testdrive-list.jsp?testdrive_id=")
								.append(crs.getInt("testdrive_id")).append(">")
								.append(crs.getString("testdrive_id"))
								.append("</a>").append("</td>\n");

						Str.append("</td>\n");
						Str.append("<td  valign=top align=left>Model: <b>")
								.append(crs.getString("preownedmodel_name")).append("</b>");
						if (crs.getString("testdrive_type").equals("1")) {
							Str.append("<br><b>Main Test Drive</b>");
						} else {
							Str.append("<br><b>Alternate Test Drive</b>");
						}
						Str.append("<br>Vehicle: <b><a href=../preowned/preowned-stock-list.jsp?vehstock_id=")
								.append(crs.getString("testdrive_preownedstock_id")).append(">")
								.append(crs.getString("preownedmodel_name"))
								.append("</b></a>");
						Str.append("<br>Test Drive Time: <b>")
								.append(strToLongDate(crs.getString("testdrive_time"))).append("</b>");
						if (!crs.getString("testdrive_time_from").equals("")) {
							Str.append("<br>Duration: ")
									.append(PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1")).append("");
						}
						Str.append("<br>Location: ").append(
								crs.getString("location_name"));
						Str.append("<br>Sales Consultant: <a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>");
						if (!crs.getString("testdrive_notes").equals("")) {
							Str.append("<br>Notes: ").append(crs.getString("testdrive_notes")).append("<br>");
						}
						Str.append(confirmed);
						Str.append("</td>");
						Str.append("<td valign=top align=left nowrap>\n");
						if (crs.getString("testdrive_fb_taken").equals("1")) {
							Str.append("Test Drive Taken");
							if (!crs.getString("status_name").equals("")) {
								Str.append("<br>")
										.append(crs.getString("status_name")).append("<br>")
										.append(crs.getString("testdrive_fb_status_comments"));
							} else {
								if (crs.getDouble("testdrive_fb_budget") != 0) {
									Str.append("<br>Budget: ")
											.append(crs.getString("testdrive_fb_budget")).append("<br>");
								}
								if (crs.getString("testdrive_fb_finance").equals("1")) {
									Str.append("<br>Finance Required<br>%age: ")
											.append(crs.getString("testdrive_fb_finance_amount"));
								} else {
									Str.append(
											"<br>Finance Not Required<br>Comments: ")
											.append(crs.getString("testdrive_fb_finance_comments"));
								}
								if (crs.getString("testdrive_fb_insurance").equals("1")) {
									Str.append("<br>Insurance Required");
								} else {
									Str.append("<br>Insurance Not Required<br>Comments: "
											+ crs.getString("testdrive_fb_insurance_comments"));
								}
							}
						}
						if (crs.getString("testdrive_fb_taken").equals("2")) {
							Str.append("Test Drive not taken");
						}
						if (!crs.getString("testdrive_doc_value").equals("")) {
							File f = new File(TestDriveDocPath(comp_id) + crs.getString("testdrive_doc_value"));
							Str.append("<br><a href=../Fetchdocs.do?image_type=preownedtestdrive&testdrive_id=")
									.append(crs.getString("testdrive_id"))
									.append("><b>Test Drive Out Pass (")
									.append(ConvertFileSizeToBytes(FileSize(f)))
									.append(")</b></a>");
						}
						Str.append("&nbsp;</td>\n");
						if (branch_id.equals("0")) {
							Str.append("<td valign=top align=left nowrap ><a href=../portal/branch-summary.jsp?branch_id="
									+ crs.getInt("branch_id") + ">" + crs.getString("branchname") + "</a></td>");
						}
						Str.append("<td valign=top align=left nowrap ><a href=\"../preowned/preowned-testdrive-update.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
								+ "&enquiry_id=" + crs.getString("testdrive_enquiry_id")
								+ " \">Update Test Drive</a>"
								+ "<br><a href=\"../preowned/preowned-testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Feedback</a>"
								+ "<br><a href=\"../preowned/testdrive-mileage.jsp?testdrive_id=" + crs.getString("testdrive_id")
								+ "\">Update Mileage</a>" + "");
						if (crs.getString("testdrive_fb_taken").equals("1")) {
							Str.append("<br><a href=\"../preowned/testdrive-cust-feedback.jsp?update=yes&testdrive_id=" + crs.getString("testdrive_id")
									+ "\">Customer Feedback</a>");
						}
						if (!crs.getString("testdrive_out_time").equals("")) {
							Str.append("<br><a href=../portal/docs-update.jsp?add=yes&testdrive_id=" + crs.getString("testdrive_id")
									+ "&enquiry_id=" + crs.getString("testdrive_enquiry_id")
									+ " > Upload Document</a>");
						}
						Str.append("<br><a href=\"../portal/new-docs-update.jsp?add=yes&type=preowned&testdrive_id=")
								.append(crs.getString("testdrive_id")).append("\" >Add Driving Licence</a>");
						if (branch_brand_id.equals("153")) {
							Str.append("<br><a href=\"../sales/testdrive-print-gatepass1.jsp?brand_id=" + branch_brand_id + "&testdrive_id="
									+ crs.getString("testdrive_id")
									+ "\" target=_blank>Print Gate Pass</a><br>");
						}
						else {
							Str.append("<br><a href=\"../sales/testdrive-print-gatepass.jsp?exporttype=pdf&report=gatePass&testdrive_id="
									+ crs.getString("testdrive_id")
									+ "&target=" + Math.random() + "\" target=_blank>Print Gate Pass</a><br>");
						}

						Str.append("</td>\n");
						Str.append("</tr>\n");
						Str.append("</tbody>\n");
						Str.append("</table>");
					}

				} else {
					Str.append("<br><br><font color=red><b>No Test Drive(s) found!</b></font>");
				}
				crs.close();

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String ListSO(String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			int count = 0;
			StrSql = " SELECT so_id, so_branch_id, concat('SO',branch_code, so_no) AS so_no,"
					+ " so_date, so_quote_id, customer_id, customer_name, contact_id,"
					+ " so_netamt, so_discamt, so_totaltax, so_grandtotal, so_mga_amount,"
					+ " so_active, so_entry_id, so_entry_date,"
					+ " concat(emp_name,' (', emp_ref_no, ')') AS emp_name, emp_id, branch_name, so_branch_id,"
					+ " GROUP_CONCAT(item_name SEPARATOR '<br>') AS items"
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " WHERE 1=1"
					+ " AND so_enquiry_id = " + enquiry_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "so_emp_id") + ""
					+ " GROUP BY so_id"
					+ " ORDER BY so_id desc";
			// SOP("Item : " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				// Str.append("<div class=\" table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
				if (crs.isBeforeFirst()) {
					Str.append("<thead><tr align=center>\n");
					Str.append("<th data-toggle=\"true\" >#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th data-hide=\"phone\">No.</th>\n");
					Str.append("<th data-hide=\"phone\">Customer</th>\n");
					Str.append("<th >Date</th>\n");
					Str.append("<th data-hide=\"phone\">Items</th>\n");
					Str.append("<th data-hide=\"phone\">Amount</th>\n");
					Str.append("<th data-hide=\"phone\">Sales Consultant</th>\n");
					Str.append("<th data-hide=\"phone\">Action</th>\n");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>\n");
					String quotecheck = "";
					while (crs.next()) {
						Str.append("<input id='txt_so_id' name='txt_so_id' value=" + crs.getString("so_id") + " hidden/>\n");
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>");
						Str.append("<td valign=top align=center><a href=veh-salesorder-list.jsp?so_id=")
								.append(crs.getInt("so_id")).append(">")
								.append(crs.getString("so_id")).append("</a>").append("</td>");
						Str.append("<td valign=top align=center>").append(crs.getString("so_no")).append("");
						if (crs.getString("so_active").equals("0")) {
							Str.append("<br><font color=red><b>Inactive</b></font>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=")
								.append(crs.getInt("customer_id"))
								.append(">")
								.append(crs.getString("customer_name"))
								.append("</a></td>");

						Str.append("<td valign=top align=center >")
								.append(strToShortDate(crs.getString("so_date")))
								.append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("items"))
								.append("</td>");
						Str.append("<td valign=top align=right nowrap>Net Total: ")
								.append(IndDecimalFormat(crs.getString("so_netamt")))
								.append("");
						if (!crs.getString("so_totaltax").equals("0")) {
							Str.append("<br>Tax: ")
									.append(IndDecimalFormat(crs.getString("so_totaltax")))
									.append("</b>");
						}
						Str.append("<br><b>Total: ")
								.append(IndDecimalFormat(crs.getString("so_grandtotal")))
								.append("</b>");
						Str.append("<br></td>\n");
						Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a></td>");
						if (!crs.getString("so_quote_id").equals("0")) {
							quotecheck = "&quote_id=" + crs.getString("so_quote_id");
						}
						Str.append("<td valign=top align=left nowrap><a href=\"veh-salesorder-update.jsp?update=yes")
								.append(quotecheck).append("&so_id=").append(crs.getString("so_id"))
								.append(" \">Update Sales Order</a>"
										+ "<br>" + "<a href=\"veh-salesorder-authorize.jsp?so_id=").append(crs.getString("so_id"))
								.append(" \">Authorize</a>"
										+ "<br>" + "<a href=\"veh-salesorder-doc-list.jsp?so_id=").append(crs.getString("so_id"))
								.append(" \">List Documents</a>"
										+ "<br>" + "<a href=\"veh-salesorder-wf-doc-list.jsp?so_id=").append(crs.getString("so_id"))
								.append(" \">List Workflow Documents</a>"
										+ "<br>" + "<a href=\"../accounting/voucher-list.jsp?voucherclass_id=6&vouchertype_id=6&voucher_so_id=").append(crs.getString("so_id"))
								.append("\" target='_blank'>List Invoices</a>"
										+ "<br>" + "<a href=\"../accounting/so-update2.jsp?add=yes&checkinvoice=yes&vouchertype_id=6&voucherclass_id=6"
										+ "&vouchertrans_customer_id=" + crs.getString("customer_id")
										+ "&span_cont_id=" + crs.getString("contact_id")
										+ "&dr_voucher_rateclass_id=1&dr_executive=" + crs.getString("emp_id")
										+ "&voucher_so_id=" + crs.getString("so_id") + "&so_branch_id=" + crs.getString("so_branch_id")).append("\" target='_blank'>Add Invoice</a>");
						Str.append("</td>\n");
						Str.append("</tr>" + "\n");

					}
				} else {
					Str.append("<input id='txt_so_id' name='txt_so_id' value='0' hidden/>\n");
					Str.append("<tr><td height=\"200\" align=\"center\" vAlign=\"top\"><center><br><br><font color=red><b>No Sales Order(s) found!</b></font></center></td></tr>");
				}
				Str.append("<tbody>\n");
				Str.append("</table>");
				crs.close();
			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
	public String ListQuotes(String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			int count = 0;
			StrSql = "SELECT quote_id, quote_branch_id, concat('QT', branch_code, quote_no) AS quote_no, quote_date, "
					+ " customer_id, customer_name, quote_netamt, "
					+ " quote_totaltax, quote_grandtotal, quote_auth, quote_active, quote_entry_id, quote_entry_date,"
					+ "	COALESCE(so_id, 0) AS so_id, COALESCE(so_active, '0') AS so_active,"
					+ " CONCAT(emp_name,' (', emp_ref_no, ')') AS emp_name, emp_id, branch_name,branch_brand_id, "
					+ " GROUP_CONCAT(item_name SEPARATOR '<br>') AS items "
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = quote_enquiry_id AND so_active = '1'"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id "
					+ " WHERE 1=1 AND quote_enquiry_id =" + enquiry_id
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "quote_emp_id") + ""
					+ " GROUP BY quote_id "
					+ " ORDER BY quote_id desc ";
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				// Str.append("<div class=\"table-responsive table-bordered\">\n");
				// Str.append("<TABLE  class=\"table table-responsive\" data-filter=\"#filter\">");
				// Str.append("<tr>");
				Str.append("<div align=\"right\"><a href=\"../sales/veh-quote-add.jsp?add=yes&amp;enquiry_id="
						+ enquiry_id + "\" >Add New Quote...</a></div><br>");
				// Str.append("</td>");
				// Str.append("</tr>");
				// Str.append("<TR>");
				// Str.append("<TD height=\"200\" align=\"center\" vAlign=\"top\" >");
				// + "<%=mybean.StrHTML%>"
				if (crs.isBeforeFirst()) {
					// Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
					Str.append("<thead><tr align=center>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th data-hide=\"phone\">No.</th>\n");
					Str.append("<th data-hide=\"phone\">Customer</th>\n");
					Str.append("<th data-hide=\"phone\">Date</th>\n");
					Str.append("<th >Items</th>\n");
					Str.append("<th data-hide=\"phone\">Amount</th>\n");
					Str.append("<th data-hide=\"phone\">Sales Consultant</th>\n");
					Str.append("<th data-hide=\"phone\">Action</th>\n");
					Str.append("</tr></thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>");
						Str.append("<td valign=top align=center><a href=veh-quote-list.jsp?quote_id=")
								.append(crs.getInt("quote_id")).append(">")
								.append(crs.getString("quote_id")).append("</a>").append("</td>");
						Str.append("<td valign=top align=center>").append(crs.getString("quote_no")).append("");
						if (crs.getString("quote_active").equals("0")) {
							Str.append("<br><font color=red><b>Inactive</b></font>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=")
								.append(crs.getInt("customer_id")).append(">")
								.append(crs.getString("customer_name")).append("</a></td>");
						Str.append("<td valign=top align=left>")
								.append(strToShortDate(crs.getString("quote_date"))).append("</td>");
						Str.append("<td valign=top align=left>")
								.append(crs.getString("items")).append("</td>");
						Str.append("<td valign=top align=left nowrap>Net Total: ")
								.append(IndDecimalFormat(crs.getString("quote_netamt"))).append("");
						if (!crs.getString("quote_totaltax").equals("0")) {
							Str.append("<br>Tax: ")
									.append(IndDecimalFormat(crs.getString("quote_totaltax"))).append("</b>");
						}
						Str.append("<br><b>Total: ")
								.append(IndDecimalFormat(crs.getString("quote_grandtotal"))).append("</b>");
						Str.append("<br></td>\n");
						Str.append("<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a></td>");
						// if (crs.getString("branch_brand_id").equals("56")) {
						// Str.append("<li role=presentation><a href=\"veh-quote-update-new.jsp");
						// } else {
						// Str.append("<li role=presentation><a href=\"veh-quote-update.jsp");
						// }
						//
						// Str.append("?update=yes&quote_id=" + crs.getString("quote_id")).append(" \">Update Quote</a></li>");

						Str.append("<td valign=top align=left nowrap>");
						if (crs.getString("branch_brand_id").equals("56")) {
							Str.append("<a href=\"veh-quote-update-new.jsp?update=yes&quote_id=");
						} else {
							Str.append("<a href=\"veh-quote-update.jsp?update=yes&quote_id=");
						}
						Str.append(crs.getString("quote_id"))
								.append("&customer_id=").append(crs.getString("customer_id"))
								.append(" \" target=_parent>Update Quote</a>");
						if ((!crs.getString("so_id").equals("0") && crs.getString("so_active").equals("0")) || crs.getString("so_id").equals("0")) {
							Str.append("<br><a href=\"veh-salesorder-update.jsp?add=yes&quote_id=").append(crs.getString("quote_id"))
									.append(" \"> Add Sales Order</a>");
						} else {
							Str.append("<br><a href=\"veh-salesorder-list.jsp?quote_id=").append(crs.getString("quote_id"))
									.append(" \"> List Sales Order</a>");
						}
						if (crs.getString("quote_auth").equals("1")) {
							Str.append("<br><a href=\"veh-quote-email.jsp?quote_id=")
									.append(crs.getString("quote_id"))
									.append(" \">Email Quote</a>"
											+ "<br>" + "<a href=\"veh-quote-print.jsp?&quote_id=").append(crs.getString("quote_id"))
									.append("&target=").append(Math.random()).append("\" target=_blank>Print Quote</a>" + "");
						}
						Str.append("</td></tr>\n");
					}
				} else {
					Str.append("<tr><td height=\"200\" align=\"center\" valign=\"top\"><center><br><br><font color=red><b>No Quote(s) found!</b></font></center></td></tr>");
				}
				crs.close();
				Str.append("<tbody>\n");
				Str.append("</table>");
			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		// SOP("Str.toString()==" + Str.toString());
		return Str.toString();
	}

	public String ListHistory(String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT " + compdb(comp_id) + "axela_sales_enquiry_history.*,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,emp_id, enquiry_title "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_history "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = history_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id "
					+ " WHERE history_enquiry_id = " + enquiry_id + ""
					+ " ORDER BY history_id desc";
			// SOP("StrSql------ListHistory------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				/*
				 * Str.append("<div class=\"table-responsive table-bordered\">\n" ); Str.append( "<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n" );
				 * Str.append("<tr>"); Str.append("<td width=\"30%\" align=\"right\">&nbsp;"); Str.append("</td>"); Str.append("</tr>"); Str.append("<TR>"); Str
				 * .append("<TD height=\"200\" align=\"center\" vAlign=\"top\" >" );
				 */
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">Date</th>");
					Str.append("<th>Action By</th>");
					Str.append("<th data-hide=\"phone, tablet\">Type of Action</th>");
					Str.append("<th data-hide=\"phone, tablet\">New Value</th>");
					Str.append("<th data-hide=\"phone, tablet\"> Old Value</th>");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						enquiry_title = crs.getString("enquiry_title");
						Str.append("<tr>\n");
						Str.append("<td valign=top align=left >")
								.append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getString("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>").append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_actiontype")).append(" </td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_newvalue")).append("</td>");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("history_oldvalue")).append("</td>");
						Str.append("</tr>" + "\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>");
				} else {
					Str.append("<div align=center><br><br><font color=red><b>No History(s) found!</b></font></div>");
				}
				crs.close();
				/*
				 * Str.append("</td>"); Str.append("</tr>"); Str.append("</table>"); Str.append("</div>");
				 */// /

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
	public String PopulateExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 AND emp_sales='1' AND emp_active='1' "
					// + " and (emp_branch_id = "
					// + enquiry_branch_id
					// + " or emp_id = 1 "
					// + " or emp_id in (select empbr.emp_id from "
					// + compdb(comp_id)
					// + "axela_emp_branch empbr "
					// + " where "
					// + compdb(comp_id)
					// +
					// "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id="
					// + enquiry_branch_id + "))"
					+ "" + ExeAccess
					// + " and emp_id = " + enquiry_emp_id
					+ " GROUP BY emp_id " + " ORDER BY emp_name ";
			// SOP("populate executice: " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), enquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateContactTitle(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT title_id, title_desc "
					+ " FROM " + compdb(comp_id) + "axela_title " + " where 1 = 1 "
					+ " ORDER BY title_desc ";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCity() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT city_id,"
					+ " COALESCE(city_name,'') AS city_name "
					+ "FROM " + compdb(comp_id) + "axela_city "
					// " INNER JOIN " + compdb(comp_id) +
					// "axela_customer_contact ON city_id=contact_city_id " +
					+ " WHERE 1 = 1 "
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("city_id")).append("");
				Str.append(StrSelectdrop(crs.getString("city_id"), contact_city_id));
				Str.append(">").append(crs.getString("city_name")).append("</option> \n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateCity(String city_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT city_id,"
					+ " COALESCE(city_name,'') AS city_name "
					+ "FROM " + compdb(comp_id) + "axela_city "
					// " INNER JOIN " + compdb(comp_id) +
					// "axela_customer_contact ON city_id=contact_city_id " +
					+ " WHERE 1 = 1 "
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("city_id")).append("");
				Str.append(StrSelectdrop(crs.getString("city_id"), contact_city_id));
				Str.append(">").append(crs.getString("city_name")).append("</option> \n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT status_id, status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status "
					+ " WHERE 1 = 1 "
					+ " ORDER BY status_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id")).append("");
				Str.append(StrSelectdrop(crs.getString("status_id"), enquiry_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLostCase1(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase1_id, lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY lostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), enquiry_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLostCase2(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase2_id, lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_lostcase1_id = " + CNumeric(enquiry_lostcase1_id)
					+ " ORDER BY lostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), enquiry_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLostCase3(String comp_id) {

		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_lostcase2_id = " + CNumeric(enquiry_lostcase2_id)
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), enquiry_lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryPriority(String comp_id) {
		StringBuilder Str = new StringBuilder();

		// Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_desc, priorityenquiry_duehrs "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " WHERE 1 = 1 "
					+ " ORDER BY priorityenquiry_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option  value=").append(crs.getString("priorityenquiry_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityenquiry_id"), enquiry_priorityenquiry_id));
				Str.append(">").append(crs.getString("priorityenquiry_desc")).append(" (")
						.append(crs.getString("priorityenquiry_duehrs")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob, config_sales_campaign, config_sales_enquiry_refno "
				+ " FROM " + compdb(comp_id) + "axela_config,"
				+ compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1"
				+ " AND emp_id = " + emp_id + "";
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1 "
					+ " AND model_active = 1"
					+ " AND model_sales = 1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " ORDER BY model_name";
			// SOP("Model----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
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

	public String PopulateItem(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1"
					+ " AND item_type_id = 1 "
					+ " AND item_model_id = " + enquiry_model_id
					// + " AND item_active = 1" //commented as discussed because old enquiries with inactive item is showing wrong item name
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), enquiry_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateAdditionalModel(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1=1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					+ " ORDER BY model_name";
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
			StrSql = "SELECT option_id, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE 1=1"
					// + " AND option_name != ''"
					+ " AND option_brand_id =" + branch_brand_id + ""
					+ " GROUP BY option_id"
					+ " ORDER BY option_id";
			// SOP("StrSql--------PopulateOption---------" + StrSql);
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

	public String PopulateCustomerType(String comp_id) {
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
				Str.append(StrSelectdrop(crs.getString("custtype_id"), enquiry_custtype_id));
				Str.append(">").append(crs.getString("custtype_name")).append("</option>\n");
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
			StrSql = "SELECT age_id, age_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age"
					+ " ORDER BY age_id";
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

	public String PopulateOccupation(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT occ_id, occ_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ"
					+ " ORDER BY occ_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("occ_id")).append("");
				Str.append(StrSelectdrop(crs.getString("occ_id"), enquiry_occ_id));
				Str.append(">").append(crs.getString("occ_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateMonthKms(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT monthkms_id, monthkms_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms"
					+ " ORDER BY monthkms_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("monthkms_id")).append("");
				Str.append(StrSelectdrop(crs.getString("monthkms_id"), enquiry_monthkms_id));
				Str.append(">").append(crs.getString("monthkms_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePurchaseMode(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT purchasemode_id, purchasemode_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode"
					+ " ORDER BY purchasemode_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("purchasemode_id")).append("");
				Str.append(StrSelectdrop(crs.getString("purchasemode_id"), enquiry_purchasemode_id));
				Str.append(">").append(crs.getString("purchasemode_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateIncome(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT income_id, income_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_income"
					+ " ORDER BY income_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("income_id")).append("");
				Str.append(StrSelectdrop(crs.getString("income_id"), enquiry_income_id));
				Str.append(">").append(crs.getString("income_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExpectation(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT expectation_id, expectation_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_expectation"
					+ " ORDER BY expectation_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("expectation_id")).append("");
				Str.append(StrSelectdrop(crs.getString("expectation_id"), enquiry_expectation_id));
				Str.append(">").append(crs.getString("expectation_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBuyerType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT buyertype_id, buyertype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " ORDER BY buyertype_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("buyertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("buyertype_id"), enquiry_buyertype_id));
				Str.append(">").append(crs.getString("buyertype_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateOwnership(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ownership_id, ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_ownership"
					+ " ORDER BY ownership_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), enquiry_ownership_id));
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

	// public String PopulatePreOwnedModel(String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT preownedmodel_id, preownedmodel_name"
	// + " FROM axela_preowned_model" + " WHERE 1=1"
	// + " ORDER BY preownedmodel_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("preownedmodel_id"),
	// enquiry_preownedmodel_id));
	// Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
	// }
	// crs.close();
	//
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulatePreOwnedVariant(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1=1";
			// if (!preowned_preownedmodel_id.equals("0")) {
			// StrSql = StrSql + " AND variant_preownedmodel_id = " +
			// enquiry_preownedmodel_id;
			// }
			StrSql = StrSql + " ORDER BY variant_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(StrSelectdrop(crs.getString("variant_id"), enquiry_preownedvariant_id));
				Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFuelType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1=1"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), enquiry_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrefReg(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prefreg_id, prefreg_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg"
					+ " WHERE 1=1" + " ORDER BY prefreg_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prefreg_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prefreg_id"), enquiry_prefreg_id));
				Str.append(">").append(crs.getString("prefreg_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFinance(String comp_id) {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", enquiry_finance)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_finance)).append(">No</option>\n");
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void CheckForm() {
		msg = "";
		int followupcount = 0;
		if (branch_brand_id.equals("55") && followup_followupstatus_id.equals("0") && status.equals("Update")) {
			msg += "<br>Select Current Follow-up Status!";
		}
		if (followup_emp_id.equals("0")) {
			msg += "<br>No Sales Consultant allocated!";
		}
		followupcount = Integer.parseInt(ExecuteQuery("SELECT COUNT(followup_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " WHERE followup_enquiry_id = " + enquiry_id));

		if ((branch_brand_id.equals("56") || branch_brand_id.equals("11")) && followupcount > 0) {
			if (enquiry_option_id.equals("0")) {
				msg += "<br>Colour is not Selected!";
			}
			msg += new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
		} else if (followupcount > 1) {
			if (enquiry_option_id.equals("0") && !branch_brand_id.equals("1") && !branch_brand_id.equals("200") && !branch_brand_id.equals("60")) {
				msg += "<br>Colour is not Selected!";
			}
			if (!branch_brand_id.equals("60")) {
				msg += new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
			}
		}

		// if (branch_brand_id.equals("60") && followupcount > 0) {
		// if (enquiry_option_id.equals("0")) {
		// msg += "<br>Colour is not Selected!";
		// }
		// msg += new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
		// } else if (followupcount > 1) {
		// if (enquiry_option_id.equals("0") && !branch_brand_id.equals("1") && !branch_brand_id.equals("200")) {
		// msg += "<br>Colour is not Selected!";
		// }
		// msg += new Enquiry_Dash_Methods().CheckEnquiryFields(enquiry_id, branch_brand_id, comp_id);
		// }

		if (status.equals("Update") && followup_feedbacktype_id.equals("0")) {
			msg += "<br>Select Action Taken!";
		}
		if (status.equals("Update") && followup_desc.equals("")) {
			msg += "<br>Enter Description!";
		}
		if (status.equals("Update") && enq_jlr_enquirystatus.equals("") && branch_brand_id.equals("60")) {
			msg += "<br>Select Enquiry Status!";
		}
		if (enquiry_status_id.equals("1")) {

			if (followup_followup_time.equals("")) {
				msg += "<br>Select Follow-up Time!";
			} else {
				if (!isValidDateFormatLong(followup_followup_time)) {
					msg += "<br>Enter Valid Follow-up Time!";
				}
			}

			followuptime = ExecuteQuery("SELECT followup_followup_time "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " WHERE followup_desc = ''"
					+ " AND followup_enquiry_id = " + enquiry_id);
			if (!followup_followup_time.equals("") && isValidDateFormatLong(followup_followup_time)
					&& !followuptime.equals("") && isValidDateFormatLong(strToLongDate(followuptime))) {

				if (Long.parseLong(ConvertLongDateToStr(followup_followup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
					msg += "<br>Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
				}

				// if
				// (Long.parseLong(ConvertLongDateToStr(followup_followup_time))
				// < Long.parseLong(followuptime)) {
				// msg +=
				// "<br>Follow-up time should be greater than previous follow-up time!";
				// }
			}

			if (!followup_followup_time.equals("") &&
					Long.parseLong(ConvertLongDateToStr(followup_followup_time)) >= Long.parseLong(ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), 31, 0, 0, 0)))) {
				msg += "<br>Enquiry Follow-up time can't exceed 30 days!";
			}

			if (followup_followuptype_id.equals("0")) {
				msg += "<br>Select Follow-up Type!";
			}
			SOP("msg====" + msg);
			// if (!enquiry_hyundai_dob.equals("") && !isValidDateFormatLong(enquiry_hyundai_dob)) {
			// msg += "<br>Select Valid DOB!";
			// SOP("msg==" + msg);
			// }

		}
		// if (msg.equals("") && (!followupdesc_id.equals("0") || !followup_desc.equals(""))) {
		// if (!followupdesc_id.equals("0") && !followup_desc.equals("")) {
		// followup_desc = followupdesc_name + "<br/>" + followup_desc;
		// } else if (!followupdesc_id.equals("0") && followup_desc.equals("")) {
		// followup_desc = followupdesc_name;
		// }
		// }
	}
	public String ListCRMFollowup(String enquiry_id, String veh_so_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		if (!comp_id.equals("0"))
		{
			try {
				// Str.append("<div class=\"portlet box\">\n");
				// Str.append("<div class=\"portlet-title\" style=\"text-align: center\"><div class=\"caption\" style=\"float: none\">CRM Follow-up</div></div> \n");
				// Str.append("<div class=\"portlet-body portlet-empty\"><div class=\"tab-pane\" id=\"\">\n");
				// Str.append("<div class=\"table-responsive\">\n");

				if (!psfmsg.equals("")) {
					Str.append("<div align=center valign=top><font color=\"#ff0000\"><b>"
							+ psfmsg + "</b></font></div>");
				}
				if (!enquiry_id.equals("0")) {
					StrSql = " SELECT customer_id, customer_name, contact_id,"
							+ " COALESCE(concat(title_desc,' ', contact_fname, ' ', contact_lname), '') AS contacts, contact_mobile1, item_name"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " WHERE crm_enquiry_id = " + enquiry_id
							+ " GROUP BY customer_id";
					crs = processQuery(StrSql, 0);
					while (crs.next()) {

						Str.append("<div class=\"container-fluid \">");
						Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
						Str.append("<div class=\"form-group\">");
						Str.append("<label class=\"text-right col-md-6 control-label\" style=\"top: -1px;\">Customer: &nbsp;</label>");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>");
						Str.append("</div>");
						Str.append("</div>");

						Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
						Str.append("<div class=\"form-group\">");
						Str.append("<label class=\"text-right col-md-2 control-label\" style=\"top: -1px;\">Contact: &nbsp;</label>");
						Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("contacts") + "</a>");
						Str.append("</div>");
						Str.append("</div>");
						Str.append("</div>");

						//
						Str.append("<div class=\"container-fluid \">");
						Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
						Str.append("<div class=\"form-group\">");
						Str.append("<label class=\" text-right col-md-6 control-label\" style=\"top: -1px;\">Mobile1: &nbsp;</label>");
						Str.append(crs.getString("contact_mobile1")).append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						Str.append("</div>");
						Str.append("</div>");

						Str.append("<div class=\"form-body col-md-6 col-sm-6\">");
						Str.append("<div class=\"form-group\">");
						Str.append("<label class=\" text-right col-md-2 control-label\" style=\"top: -1px;\">Variant: &nbsp;</label>");
						Str.append(crs.getString("item_name"));
						Str.append("</div>");
						Str.append("</div>");
						Str.append("</div>");

					}

					Str.append("");
					crs.close();
				}
				// //////// Start of CRM Details////////////
				int count = 0;
				int type = 0;
				StrSql = " SELECT crm_id, crm_emp_id, crm_enquiry_id, crm_so_id,"
						+ " COALESCE(so_active,'') AS so_active,"
						+ " crm_followup_time, crm_desc, "
						+ " crm_entry_time, crm_entry_id, crm_modified_id,"
						+ " crm_modified_time,"
						+ " COALESCE(crmfeedbacktype_name, '') AS crmfeedbacktype_name,"
						+ " COALESCE (CASE WHEN crm_satisfied = 0 THEN '' END,"
						+ " CASE WHEN crm_satisfied = 1 THEN 'Satisfied' END,"
						+ " CASE WHEN crm_satisfied = 2 THEN 'Dis-Satisfied' END,'') AS crm_satisfied,"
						+ " crm_crmdays_id, crmdays_daycount, crmdays_desc, crmtype_id, crmtype_name,"
						+ " COALESCE(crm.emp_id, 0) AS crmemp_id,"
						+ " COALESCE(CONCAT(crm.emp_name,' (',crm.emp_ref_no,')'),'') AS crmemp_name,"
						+ " COALESCE(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') AS entry_by, "
						+ " COALESCE(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') AS modified_by "
						+ " FROM " + compdb(comp_id) + "axela_sales_crm"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
						+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS crm ON crm.emp_id = crm_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS e ON e.emp_id = crm_entry_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS m ON m.emp_id = crm_modified_id"
						+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id"
						+ " WHERE 1 = 1 ";
				if (!enquiry_id.equals("0")) {
					StrSql += " AND crm_enquiry_id = " + enquiry_id;
				}
				if (!veh_so_id.equals("0")) {
					StrSql += " AND so_id =" + veh_so_id
							+ " AND crmdays_crmtype_id != '1'";
				}
				StrSql += " GROUP BY crm_id"
						+ " ORDER BY crmdays_crmtype_id, crm_followup_time ";
				crs = processQuery(StrSql, 0);
				// SOP("StrSql===" + StrSql);
				// Str.append("<tr> <td colspan=4 align=center valign=top>&nbsp;</td></tr>");

				if (crs.isBeforeFirst()) {
					if (!veh_so_id.equals("0")) {
						Str.append("<div><center><label><b>Sales Follow-up</b></label></center></div>");
					}
					// Str.append("<div class=\" table-bordered table-hover\">\n");
					Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Time</th>\n");
					Str.append("<th data-hide=\"phone\">SO ID</th>\n");
					Str.append("<th>Type</th>\n");
					Str.append("<th data-hide=\"phone\">Days</th>\n");
					Str.append("<th data-hide=\"phone\">Feedback Type</th>\n");
					Str.append("<th data-hide=\"phone\">Description</th>\n");
					Str.append("<th data-hide=\"phone\">CRM Executive</th>\n");
					Str.append("<th data-hide=\"phone\">Entry By</th>\n");
					Str.append("<th data-hide=\"phone\">Update</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						// crmfollowup_id = crs.getString("crmfollowup_id");
						count++;
						if (type != crs.getInt("crmtype_id")) {
							Str.append("<tr><td align=center><b>" + crs.getString("crmtype_name") + " Follow-up");
							if (crs.getString("so_active").equals("0")) {
								Str.append("<font color='red'>&nbsp;(Inactive)</font>");
							}
							Str.append("</b></td></tr>\n");
							type = crs.getInt("crmtype_id");
							count = 1;
						}
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>");
						Str.append("<td valign=top align=center >").append(strToLongDate(crs.getString("crm_followup_time")));
						if (emp_id.equals("1")) {
							Str.append("<br><a href=\"enquiry-dash.jsp?Delete=yes&crm=yes&enquiry_id=").append(crs.getString("crm_enquiry_id"))
									.append("&crm_id=").append(crs.getString("crm_id")).append(" \">Delete CRM Follow-up</a>");
						}
						Str.append("</td>");

						Str.append("<td valign=top align=center>");
						if (!crs.getString("crm_so_id").equals("0")) {
							Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=")
									.append(crs.getString("crm_so_id")).append("\">").append(crs.getString("crm_so_id")).append("</a>");
						} else {
							Str.append("&nbsp;");
						}

						Str.append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("crmtype_name")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("crmdays_daycount"))
								.append(crs.getString("crmdays_desc")).append("</td>\n");
						Str.append("<td valign=top align=center >").append(crs.getString("crmfeedbacktype_name"));
						Str.append("<br>" + crs.getString("crm_satisfied")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("crm_desc")).append("</td>");
						Str.append("<td valign=top align=left >").append("<a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("crmemp_id")).append(">").append(crs.getString("crmemp_name")).append("</a>");
						Str.append("</td>");
						Str.append("<td valign=top align=left >");
						if (!crs.getString("crm_entry_id").equals("0") && crs.getString("crm_modified_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("crm_entry_id"))
									.append(">").append(crs.getString("entry_by")).append("</a>");
							Str.append("<br>").append(strToShortDate(crs.getString("crm_entry_time"))).append("");
						}
						if (!crs.getString("crm_modified_id").equals("0")) {
							Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("crm_modified_id")).append(">")
									.append(crs.getString("modified_by")).append("</a>");
							Str.append("<br>").append(strToShortDate(crs.getString("crm_modified_time"))).append("");
						}
						Str.append("&nbsp;</td>");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=../sales/crm-update.jsp?update=yes&crm_id=").append(crs.getInt("crm_id"))
								.append(">Update " + crs.getString("crmtype_name") + " Follow-up</a>");
						if (!crs.getString("crm_entry_id").equals("0")) {
							Str.append("<br><a href=../sales/crm-print.jsp?crm_id=").append(crs.getInt("crm_id")).append("&target=")
									.append(Math.random()).append("\" target=_blank>Print " + crs.getString("crmtype_name") + "</a>" + "");
						}

						// + "</a>");
						Str.append("</td>");
						Str.append("</tr>");
					}

				} else {
					Str.append("<center><font color=red><b>No CRM Follow-up found!</b></font></center>");
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>");

				// SOP("CRM-Follow-up=====" + Str);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	public String ListPreOwnedFollowup(String enquiry_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT preownedfollowup_id, preownedfollowup_preowned_id, preownedfollowup_followup_time, preownedfollowup_desc, "
				+ " preownedfollowup_entry_time, preownedfollowup_entry_id,"
				+ " COALESCE(preownedfeedbacktype_name,'') as preownedfeedbacktype_name,"
				+ " COALESCE(preownedfollowuptype_name,'') AS preownedfollowuptype_name, CONCAT(emp_name,' (',emp_ref_no,')') as entry_by"
				+ " FROM " + compdb(comp_id) + "axela_preowned_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedfollowup_preowned_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preownedfollowup_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup_type ON preownedfollowuptype_id = preownedfollowup_preownedfollowuptype_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup_feedback_type ON preownedfeedbacktype_id = preownedfollowup_preownedfeedbacktype_id"
				+ " WHERE 1 = 1 "
				+ " AND preowned_enquiry_id = " + enquiry_id;
		// SOP("StrSql111------------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\" align=center>#</th>\n");
				Str.append("<th align=center>Time</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Follow-up Type</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Follow-up Description</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" align=center>Entry by</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>");
					Str.append("<td>").append(strToLongDate(crs.getString("preownedfollowup_followup_time")));
					if (emp_id.equals("1") && !crs.getString("preownedfollowup_id").equals("0")) {
						Str.append("<br><a href=\"enquiry-dash.jsp?Delete=yes&preowned_id=").append(crs.getString("preownedfollowup_preowned_id")).append("&preownedfollowup_id=")
								.append(crs.getString("preownedfollowup_id")).append(" \">Delete Follow-up</a>");
					}
					Str.append("</td>");
					Str.append("<td>").append(crs.getString("preownedfollowuptype_name")).append("</td>");
					Str.append("<td>").append(crs.getString("preownedfollowup_desc")).append("</td>");
					Str.append("<td>").append(crs.getString("preownedfeedbacktype_name")).append("</td>");
					Str.append("<td>");
					if (!crs.getString("preownedfollowup_entry_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getInt("preownedfollowup_entry_id")).append(">").append(crs.getString("entry_by")).append("</a>");
						Str.append("<br>").append(strToLongDate(crs.getString("preownedfollowup_entry_time"))).append("");
					}
					Str.append("&nbsp;</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<center><font color=red><b>No Follow-up found!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void DeleteCustomCRMFields(String crm_id) {
		// if (msg.equals("")) {
		if (!comp_id.equals("0"))
		{
			try {
				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_crm_trans"
						+ " WHERE crmcftrans_crm_id = " + crm_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_crm" + ""
						+ " WHERE crm_id =" + crm_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
