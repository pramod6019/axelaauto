package axela.axelaauto_app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.preowned.Preowned_Quickadd;
import axela.preowned.Preowned_Variant_Check;
import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

public class App_Enquiry_Add extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	// public String emp_enquiry_add = "0";
	public String emp_uuid = "";
	public String enquiry_title_id = "";
	public String enquiry_fname = "";
	public String enquiry_lname = "";
	public String enquiry_jobtitle = "";
	public String enquiry_id = "0";
	public String branch_id = "0";
	public String enquiry_customer_id = "0";
	public String enquiry_contact_id = "0";
	public String enquiry_title = "";
	public String enquiry_desc = "";
	public String contact_jobtitle = "";
	public String enquiry_date = "";
	// public String customer_zone_id = "0";
	// public String enquiry_enquirycat_id = "";
	public String enquiry_enquirytype_id = "";
	public String enquiry_refemp_id = "0";
	// public String enquiry_preownedmodel_id = "0";
	// public String enquiry_preownedvariant_id = "0";
	public String enquiry_fueltype_id = "0";
	public String enquiry_prefreg_id = "0";
	public String enquiry_presentcar = "";
	public String enquiry_finance = "";
	public String enquiry_budget_id = "0";
	public String enquiry_prefregyear = "";
	public String enquiry_prefmileage_id = "0";
	// public String enquiry_extcolour_id = "0";
	// public String enquiry_intcolour_id = "0";
	public String enquiry_refno = "";
	// public String enquiry_close_date = "";
	// public String close_date = "";
	public String enquiry_emp_id = "0";
	public String enquiry_campaign_id = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_sob_id = "0";
	// public String enquiry_refno = "";
	public String enquiry_notes = "";
	// public String enquiry_custtype_id = "0";
	public String enquiry_buyertype_id = "0";
	public String enquiry_enquirycat_id = "0";
	public String enquiry_entry_id = "0";
	public String enquiry_entry_date = "";
	public String branch_brand_id = "1";
	public String branch_name = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String emp_id = "0";
	public String emp_name = "";
	public String emp_email_formail = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String title_desc = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_email2 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String contact_city_id = "0";
	public String contact_pin = "";
	public String state_id = "0";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String enquiry_team_id = "0";
	public String contact_id = "0";
	public String ExeAccess = "";
	public String brandconfig_enquiry_email_enable = "";
	public String brandconfig_enquiry_email_sub = "";
	public String brandconfig_enquiry_email_format = "";
	public String brandconfig_enquiry_email_exe_enable = "";
	public String brandconfig_enquiry_email_exe_format = "";
	public String brandconfig_enquiry_email_exe_sub = "";
	public String brandconfig_enquiry_sms_enable = "";
	public String brandconfig_enquiry_sms_format = "";
	public String brandconfig_enquiry_sms_exe_format = "";
	public String brandconfig_enquiry_sms_exe_enable = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sales_enquiry_refno = "";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String emp_role_id = "";
	public String config_sales_sob = "";
	public String enquiry_model_id = "0";
	public String model_name = "", city_name = "";
	public String enquiry_item_id = "0";
	public String brandconfig_enquiry_brochure_email_enable = "";
	public String brandconfig_enquiry_brochure_email_format = "";
	public String brandconfig_enquiry_brochure_email_sub = "";
	public String send_contact_email = "";
	public String crmfollowup_crm_emp_id = "0";
	public String crmfollowupdays_daycount = "";
	public String crmfollowup_followup_time = "";
	public String add = "";
	public String enquiry_close_date = "";
	public String comp_id = "0";
	public String enquiry_branch_id = "0", emp_all_exe = "";
	public String branchtype_id = "0";
	public String enquiry_preownedvariant_id = "0", team_preownedemp_id = "0";
	public Preowned_Variant_Check variantcheck = new Preowned_Variant_Check();
	public String enquiry_budget = "";
	public String enquiry_tradein_preownedvariant_id = "0";
	public String team_preownedbranch_id = "0";
	public String branchcount = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			SetSession("comp_id", comp_id, request);
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				BranchAccess = GetSession("BranchAccess", request);
				// BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				// emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				if (enquiry_branch_id.equals("0")) {
					enquiry_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (enquiry_branch_id.equals("0")) {
						enquiry_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 AND branch_branchtype_id IN (1, 2) LIMIT 1");
					}
				}
				branch_id = enquiry_branch_id;
				enquiry_emp_id = emp_id;
				emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
				addB = PadQuotes(request.getParameter("add_button1"));
				msg = PadQuotes(request.getParameter("msg"));
				// contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
				// enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				// enquiry_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
				branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
				branchcount = CNumeric(ExecuteQuery("SELECT COUNT(DISTINCT branch_id)"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_active = 1 "
						+ " AND branch_branchtype_id IN (1, 2)"));
				// emp_enquiry_add = ReturnPerm(comp_id, "emp_enquiry_access", request);
				// if (emp_enquiry_add.equals("0")) {
				// response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				// }
				PopulateConfigDetails();
				if (!addB.equals("yes")) {
					contact_mobile1 = "91-";
					enquiry_emp_id = emp_id;
					enquiry_team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_emp_id = " + emp_id));
					enquiry_date = strToShortDate(ToShortDate(kknow()));
					enquiry_close_date = strToShortDate(ToShortDate(kknow()));
				} else if (addB.equals("yes")) {
					String addmsg = "Enquiry added successfully!";
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						AddEnquiry();
					}
					if (msg.equals("")) {
						msg = addmsg;
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-enquiry-list.jsp?enquiry_id=" + enquiry_id + "&msg=" + msg));
					} else if (!msg.equals("")) {
						msg = "" + unescapehtml(msg);
						response.sendRedirect(response.encodeRedirectURL("showtoast" + msg));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App==== " + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	public void AddEnquiry() {
		try {
			Enquiry_Quickadd enq = new Enquiry_Quickadd();
			contact_city_id = ExecuteQuery("SELECT city_id"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_city_id=city_id"
					+ " WHERE branch_id=" + branch_id);

			enq.emp_id = emp_id;
			// -----customer n contact-------
			enq.enquiry_branch_id = branch_id;
			enq.customer_name = enquiry_fname + " " + enquiry_lname;
			enq.contact_fname = enquiry_fname;
			enq.contact_lname = enquiry_lname;
			// enq.customer_zone_id = "4";
			enq.contact_mobile1 = contact_mobile1;
			enq.contact_mobile2 = contact_mobile2;
			enq.contact_city_id = contact_city_id;
			enq.enquiry_soe_id = enquiry_soe_id;// 22
			enq.enquiry_sob_id = enquiry_sob_id;// 40
			enq.enquiry_emp_id = enquiry_emp_id;
			enq.enquiry_team_id = enquiry_team_id;
			enq.contact_address = "";
			enq.contact_pin = "";
			enq.contact_email1 = contact_email1;
			enq.contact_email2 = "";
			enq.contact_phone1 = "";
			enq.enquiry_buyertype_id = enquiry_buyertype_id;
			enq.enquiry_enquirycat_id = enquiry_enquirycat_id;
			enq.enquiry_contact_id = "0";
			enq.comp_id = comp_id;
			enq.contact_title_id = enquiry_title_id;
			// enq.lead_fname = enquiry_fname;
			// enq.lead_lname = enquiry_lname;
			// enq.lead_jobtitle = "";

			// -----config--------
			enq.config_sales_soe = config_sales_soe;
			enq.config_sales_sob = config_sales_sob;
			enq.config_sales_campaign = config_sales_campaign;
			enq.config_sales_enquiry_refno = config_sales_enquiry_refno;
			enq.comp_email_enable = comp_email_enable;
			enq.config_email_enable = config_email_enable;
			enq.branch_email1 = branch_email1;
			enq.brandconfig_enquiry_email_enable = brandconfig_enquiry_email_enable;
			enq.brandconfig_enquiry_email_format = brandconfig_enquiry_email_format;
			enq.brandconfig_enquiry_email_sub = brandconfig_enquiry_email_sub;
			enq.emp_email_formail = emp_email_formail;
			enq.emp_mobile1 = emp_mobile1;
			enq.emp_mobile2 = emp_mobile2;
			enq.brandconfig_enquiry_email_exe_format = brandconfig_enquiry_email_exe_format;
			enq.brandconfig_enquiry_email_exe_sub = brandconfig_enquiry_email_exe_sub;
			enq.comp_sms_enable = comp_sms_enable;
			enq.config_sms_enable = config_sms_enable;
			enq.brandconfig_enquiry_email_enable = brandconfig_enquiry_email_enable;
			enq.brandconfig_enquiry_email_format = brandconfig_enquiry_email_format;
			enq.brandconfig_enquiry_email_sub = brandconfig_enquiry_email_sub;
			enq.brandconfig_enquiry_email_exe_enable = brandconfig_enquiry_email_exe_enable;
			enq.brandconfig_enquiry_email_exe_format = brandconfig_enquiry_email_exe_format;
			enq.brandconfig_enquiry_email_exe_sub = brandconfig_enquiry_email_exe_sub;
			enq.brandconfig_enquiry_sms_enable = brandconfig_enquiry_sms_enable;
			enq.brandconfig_enquiry_sms_format = brandconfig_enquiry_sms_format;
			enq.brandconfig_enquiry_sms_exe_enable = brandconfig_enquiry_sms_exe_enable;
			enq.brandconfig_enquiry_sms_exe_format = brandconfig_enquiry_sms_exe_format;
			enq.brandconfig_enquiry_brochure_email_enable = brandconfig_enquiry_brochure_email_enable;
			enq.brandconfig_enquiry_brochure_email_format = brandconfig_enquiry_brochure_email_format;
			enq.brandconfig_enquiry_brochure_email_sub = brandconfig_enquiry_brochure_email_sub;
			enq.emp_email1 = emp_email1;
			// -----enquiry-------
			enq.enquiry_title = enquiry_title;
			enq.enquiry_desc = "";
			enq.enquiry_date = strToShortDate(ToLongDate(kknow()));
			enq.enquiry_model_id = enquiry_model_id;
			enq.enquiry_item_id = enquiry_item_id;
			enq.enquiry_close_date = strToShortDate(ToLongDate(kknow()));
			enq.enquiry_campaign_id = enquiry_campaign_id;

			enq.enquiry_notes = enquiry_notes;
			enq.enquiry_finance = "0";
			enq.enquiry_enquirytype_id = "1";
			enq.enquiry_tradein_preownedvariant_id = enquiry_tradein_preownedvariant_id;
			enq.branch_brand_id = branch_brand_id;
			if (branchtype_id.equals("1")) {
				enq.enquiry_enquirytype_id = "1";
			}
			if (branchtype_id.equals("2")) {
				enq.enquiry_enquirytype_id = "2";
			}
			// enq.enquiry_enquirycat_id = "1";
			// enq.enquiry_prefregyear = enquiry_prefregyear;
			enq.enquiry_entry_id = emp_id;
			enq.enquiry_entry_date = ToLongDate(kknow());

			// --------preowned---------
			// enq.enquiry_preownedmodel_id = enquiry_preownedmodel_id;
			// enq.enquiry_preownedvariant_id = enquiry_preownedvariant_id;
			enq.enquiry_budget = enquiry_budget_id;
			if (branchtype_id.equals("2")) {
				enq.enquiry_preownedvariant_id = enquiry_preownedvariant_id;
				enq.enquiry_fueltype_id = enquiry_fueltype_id;
				enq.enquiry_prefreg_id = enquiry_prefreg_id;
				enq.enquiry_presentcar = enquiry_presentcar;
				enq.enquiry_budget = enquiry_budget;
				enq.enquiry_finance = enquiry_finance;
				// enq.enquiry_prefregyear = enquiry_prefregyear;
			}
			// enq.enquiry_prefregyear = enquiry_prefregyear;
			// enq.enquiry_prefmileage_id = enquiry_prefmileage_id;
			// enq.enquiry_extcolour_id = enquiry_extcolour_id;
			// enq.enquiry_intcolour_id = enquiry_intcolour_id;

			enq.AddEnquiryFields();
			msg = enq.msg;
			enquiry_id = enq.enquiry_id;
			// if (!enquiry_tradein_preownedvariant_id.equals("0") && !team_preownedbranch_id.equals("0") && !team_preownedemp_id.equals("0")) {
			// enq.enquiry_tradein_preownedvariant_id = enquiry_tradein_preownedvariant_id;
			// enq.team_preownedemp_id = team_preownedemp_id;
			// enq.team_preownedbranch_id = team_preownedbranch_id;
			// enq.AddPreOwnedEnquiry();
			// }
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CheckForm() throws SQLException {
		if (enquiry_title_id.equals("0")) {
			msg = msg + "Select Title!<br>";
		}
		if (enquiry_fname.equals("")) {
			msg = msg + "Enter Name!<br>";
		}
		if (contact_mobile1.equals("")) {
			msg = msg + "Enter Mobile No.!<br>";
		}
		if (!enquiry_team_id.equals("0") && !enquiry_tradein_preownedvariant_id.equals("0")) {
			StrSql = "SELECT team_preownedbranch_id, team_preownedemp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_id = " + enquiry_team_id;
			// SOP("StrSql-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				team_preownedbranch_id = crs.getString("team_preownedbranch_id");
				team_preownedemp_id = crs.getString("team_preownedemp_id");
			}
			if (team_preownedbranch_id.equals("0")) {
				msg = msg + "<br>Pre-Owned Branch is not configured for this Team!";
			}
			if (team_preownedemp_id.equals("0")) {
				msg = msg + "<br>Pre-Owned Consultant is not configured for this Team!";
			}
			crs.close();
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (enquiry_contact_id.equals("0")) {

			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
			enquiry_title_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_title_id")));
			enquiry_fname = PadQuotes(request.getParameter("txt_enquiry_fname"));
			enquiry_lname = PadQuotes(request.getParameter("txt_enquiry_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			// contact_email2 = PadQuotes(request.getParameter("txt_contact_email2"));
			// contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
			// contact_phone2 = PadQuotes(request.getParameter("txt_contact_phone2"));
			// contact_address = PadQuotes(request.getParameter("txt_contact_address"));
			// contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
			// enquiry_close_date = PadQuotes(request.getParameter("txt_enquiry_close_date"));
			// enquiry_desc = PadQuotes(request.getParameter("txt_enquiry_desc"));
			// contact_jobtitle = PadQuotes(request.getParameter("txt_contact_jobtitle"));
			if (Integer.parseInt(branchcount) > 1) {
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
			}
			// contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_city_id")));
			enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_enquirytype_id")));
			enquiry_preownedvariant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
			enquiry_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_fueltype_id")));
			enquiry_prefreg_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_prefreg_id")));
			enquiry_presentcar = PadQuotes(request.getParameter("txt_enquiry_presentcar"));
			enquiry_finance = CNumeric(PadQuotes(request.getParameter("dr_enquiry_finance")));
			enquiry_budget = CNumeric(PadQuotes(request.getParameter("txt_enquiry_budget")));
			enquiry_tradein_preownedvariant_id = CNumeric(PadQuotes(request.getParameter("enquiry_tradein_preownedvariant_id")));
		}

		enquiry_date = strToShortDate(ToShortDate(kknow()));

		// enquiry_enquirytype_id = PadQuotes(request.getParameter("dr_enquiry_enquirytype_id"));
		// enquiry_enquirycat_id = PadQuotes(request.getParameter("dr_enquiry_enquirycat_id"));
		enquiry_soe_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_soe_id")));
		enquiry_sob_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_sob_id")));
		enquiry_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_campaign_id")));
		enquiry_buyertype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_buyertype_id")));
		enquiry_enquirycat_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_enquirycat_id")));
		enquiry_prefregyear = CNumeric(PadQuotes(request.getParameter("dr_enquiry_prefregyear")));
		// enquiry_desc = PadQuotes(request.getParameter("txt_enquiry_desc"));
		enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
		enquiry_item_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_item_id")));
		enquiry_team_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_team")));
		enquiry_buyertype_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_buyertype_id")));
		enquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_emp_id")));
		enquiry_notes = PadQuotes(request.getParameter("txt_enquiry_notes"));

	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id IN (1, 2)";
			SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"),
						branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code"))
						.append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	public String PopulateTitle(String contact_title_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title" + " WHERE 1 =  1";
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel(String enquiry_model_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1=1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = '1' "
					+ " AND model_sales = 1";
			// if (!CNumeric(enquiry_model_id).equals("0")) {
			// StrSql += " AND model_id = " + enquiry_model_id + "";
			// }
			StrSql += " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirytype_id, enquirytype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_type" + " GROUP BY enquirytype_id" + " ORDER BY enquirytype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirytype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirytype_id"), enquiry_enquirytype_id));
				Str.append(">").append(crs.getString("enquirytype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateItem(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1"
					+ " AND item_type_id = 1 ";
			// if (!update.equals("yes")) {
			StrSql = StrSql + " AND item_active = '1'";
			// }
			StrSql = StrSql + " ORDER BY item_name";
			// SOPInfo("StrSql-------PopulateItem---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBuyerType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT buyertype_id, buyertype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype" + " WHERE 1=1" + " ORDER BY buyertype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("buyertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("buyertype_id"), enquiry_buyertype_id));
				Str.append(">").append(crs.getString("buyertype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquirycat_id, enquirycat_name"
					+ " from axela_sales_enquiry_cat"
					+ " group by enquirycat_id"
					+ " order by enquirycat_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("enquirycat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("enquirycat_id"), enquiry_enquirycat_id));
				Str.append(">").append(crs.getString("enquirycat_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String branch_id, String enquiry_team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + branch_id
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), enquiry_team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSalesExecutives(String branch_id, String enquiry_team_id, String enquiry_emp_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			emp_id = CNumeric(GetSession("emp_id", request));
			StrSql = " SELECT emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales = '1'"
					+ " AND emp_branch_id = " + branch_id;
			if (!enquiry_team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id IN (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id =" + enquiry_team_id + ")";
			}
			StrSql += " AND emp_active = '1' ";

			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow())
					+ " AND leave_active = 1 )";
			// Only for super admin
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id,"
						+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id = 1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select class=\"form-control\" name=\"dr_enquiry_emp_id\" id=\"dr_enquiry_emp_id\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), enquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id" + " WHERE 1=1"
					+ " AND empsoe_emp_id=" + emp_id + "" + " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), enquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSob(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob" + " GROUP BY sob_id" + " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), enquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCampaign(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			enquiry_date = strToShortDate(ToShortDate(kknow()));
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1"
					+ " AND camptrans_branch_id = " + branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8)"
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8)"
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), enquiry_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFuelType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), enquiry_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
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
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFinance(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", enquiry_finance)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", enquiry_finance)).append(">No</option>\n");
		return Str.toString();
	}

	public void AddPreOwnedEnquiry() {
		try {
			Preowned_Quickadd preowned = new Preowned_Quickadd();
			preowned.comp_id = comp_id;
			preowned.emp_id = emp_id;
			preowned.preowned_branch_id = team_preownedbranch_id;
			preowned.preowned_customer_id = enquiry_customer_id;
			preowned.preowned_contact_id = enquiry_contact_id;
			preowned.preowned_title = "New Pre-Owned";
			preowned.preowned_sub_variant = "";
			preowned.preowned_extcolour_id = "0";
			preowned.preowned_intcolour_id = "0";
			preowned.preowned_options = "";
			preowned.preowned_date = strToShortDate(ToShortDate(kknow()));
			preowned.preowned_variant_id = enquiry_tradein_preownedvariant_id;
			preowned.preowned_fcamt = "0";
			preowned.preowned_noc = "";
			preowned.preowned_funding_bank = "";
			preowned.preowned_loan_no = "";
			preowned.preowned_insur_date = "";
			preowned.preowned_insurance_id = "0";
			preowned.preowned_ownership_id = "0";
			preowned.preowned_regdyear = "";
			preowned.preowned_manufyear = "";
			preowned.preowned_invoicevalue = "0";
			preowned.preowned_kms = "0";
			preowned.preowned_regno = "";
			preowned.preowned_expectedprice = "0";
			preowned.preowned_quotedprice = "0";
			preowned.preowned_fueltype_id = "0";
			preowned.preowned_close_date = strToShortDate(ToLongDate(kknow()));
			preowned.preowned_emp_id = team_preownedemp_id;
			preowned.preowned_sales_emp_id = enquiry_emp_id;
			preowned.preowned_enquiry_id = enquiry_id;
			preowned.preowned_preownedstatus_id = "1";
			preowned.preowned_preownedstatus_date = "";
			preowned.preowned_preownedstatus_desc = "";
			preowned.preowned_prioritypreowned_id = "1";
			preowned.preowned_notes = "";
			preowned.preowned_desc = "";
			preowned.preowned_entry_id = emp_id;
			preowned.preowned_entry_date = ToLongDate(kknow());
			preowned.PopulateConfigDetails();
			preowned.PopulateContactCustomerDetails();
			preowned.AddPreownedFields();
			// msg += preowned.msg;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT "
				+ " config_sales_soe,"
				+ " config_sales_sob,"
				+ " config_sales_enquiry_refno,"
				+ " config_email_enable,"
				+ " config_sms_enable,"
				+ " comp_email_enable,"
				+ " comp_sms_enable,"
				+ " config_sales_campaign,"
				+ " branch_brand_id,"
				+ " COALESCE (branch_branchtype_id, 0) AS branch_branchtype_id,"
				+ " COALESCE (branch_email1, '') AS branch_email1,"
				+ " branch_pin,"
				+ " COALESCE (brandconfig_enquiry_email_enable,'') AS brandconfig_enquiry_email_enable,"
				+ " COALESCE (brandconfig_enquiry_email_format,'') AS brandconfig_enquiry_email_format,"
				+ " COALESCE (brandconfig_enquiry_email_sub,'') AS brandconfig_enquiry_email_sub,"
				+ " COALESCE (brandconfig_enquiry_email_exe_enable,'') AS brandconfig_enquiry_email_exe_enable,"
				+ " COALESCE (brandconfig_enquiry_email_exe_sub,'') AS brandconfig_enquiry_email_exe_sub,"
				+ " COALESCE (brandconfig_enquiry_email_exe_format,'') AS brandconfig_enquiry_email_exe_format,"
				+ " COALESCE (brandconfig_enquiry_sms_enable,'') AS brandconfig_enquiry_sms_enable,"
				+ " COALESCE (brandconfig_enquiry_sms_format,'') AS brandconfig_enquiry_sms_format,"
				+ " COALESCE (brandconfig_enquiry_sms_exe_enable,'') AS brandconfig_enquiry_sms_exe_enable,"
				+ " COALESCE (brandconfig_enquiry_sms_exe_format,'') AS brandconfig_enquiry_sms_exe_format,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_enable,'') AS brandconfig_enquiry_brochure_email_enable,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_format,'') AS brandconfig_enquiry_brochure_email_format,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_sub,'') AS brandconfig_enquiry_brochure_email_sub,"
				+ " COALESCE (emp.emp_email1, '') AS emp_email1,"
				+ " COALESCE (emp.emp_email2, '') AS emp_email2,"
				+ " COALESCE (emp.emp_name, '') AS emp_name,"
				+ " COALESCE (emp.emp_mobile1, '') AS emp_mobile1,"
				+ " COALESCE (emp.emp_mobile2, '') AS emp_mobile2,"
				+ " config_email_enable,"
				+ " config_sms_enable,"
				+ " comp_email_enable,"
				+ " comp_sms_enable,"
				+ " config_sales_campaign,"
				+ " config_customer_dupnames,"
				+ " COALESCE (team_preownedbranch_id, 0) AS team_preownedbranch_id,"
				+ " COALESCE (team_preownedemp_id, 0) AS team_preownedemp_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + enquiry_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = " + enquiry_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id,"
				+ compdb(comp_id) + "axela_config,"
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1"
				+ " AND branch_id = " + branch_id;
		// SOP("configdetails-------------------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			while (crs.next()) {
				branchtype_id = crs.getString("branch_branchtype_id");
				comp_email_enable = crs.getString("comp_email_enable");
				branch_brand_id = crs.getString("branch_brand_id");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				// config_sales_lead_for_enquiry = crs.getString("config_sales_lead_for_enquiry");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_enquiry_email_enable = crs.getString("brandconfig_enquiry_email_enable");
				brandconfig_enquiry_email_format = crs.getString("brandconfig_enquiry_email_format");
				brandconfig_enquiry_email_sub = crs.getString("brandconfig_enquiry_email_sub");
				brandconfig_enquiry_email_exe_enable = crs.getString("brandconfig_enquiry_email_exe_enable");
				brandconfig_enquiry_email_exe_format = crs.getString("brandconfig_enquiry_email_exe_format");
				brandconfig_enquiry_email_exe_sub = crs.getString("brandconfig_enquiry_email_exe_sub");
				brandconfig_enquiry_sms_enable = crs.getString("brandconfig_enquiry_sms_enable");
				brandconfig_enquiry_sms_format = crs.getString("brandconfig_enquiry_sms_format");
				brandconfig_enquiry_sms_exe_enable = crs.getString("brandconfig_enquiry_sms_exe_enable");
				brandconfig_enquiry_sms_exe_format = crs.getString("brandconfig_enquiry_sms_exe_format");
				brandconfig_enquiry_brochure_email_enable = crs.getString("brandconfig_enquiry_brochure_email_enable");
				brandconfig_enquiry_brochure_email_format = crs.getString("brandconfig_enquiry_brochure_email_format");
				brandconfig_enquiry_brochure_email_sub = crs.getString("brandconfig_enquiry_brochure_email_sub");
				// branch_city_id = crs.getString("branch_city_id");
				branch_pin = crs.getString("branch_pin");
				team_preownedbranch_id = crs.getString("team_preownedbranch_id");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				team_preownedemp_id = crs.getString("team_preownedemp_id");
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
