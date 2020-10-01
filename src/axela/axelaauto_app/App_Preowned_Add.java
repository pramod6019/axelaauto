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
import axela.preowned.Preowned_Quickadd;
import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class App_Preowned_Add extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	// public String emp_preowned_add = "0";
	public String emp_uuid = "";
	public String preowned_title_id = "";
	public String preowned_fname = "";
	public String preowned_lname = "";
	public String preowned_jobtitle = "";
	public String preowned_id = "0";
	public String branch_id = "0";
	public String preowned_customer_id = "0";
	public String preowned_contact_id = "0";
	public String preowned_title = "";
	public String preowned_desc = "";
	public String contact_jobtitle = "";
	public String preowned_date = "";
	// public String customer_zone_id = "0";
	// public String preowned_preownedcat_id = "";
	public String preowned_preownedtype_id = "";
	public String preowned_refemp_id = "0";
	// public String preowned_preownedmodel_id = "0";
	// public String preowned_preownedvariant_id = "0";
	public String preowned_fueltype_id = "0";
	public String preowned_manufyear = "";
	public String preowned_regno = "";
	public String preowned_ownership_id = "0";
	public String preowned_prefreg_id = "0";
	public String preowned_presentcar = "";
	public String preowned_finance = "";
	public String preowned_budget_id = "0";
	public String preowned_prefregyear = "";
	public String preowned_prefmileage_id = "0";
	// public String preowned_extcolour_id = "0";
	// public String preowned_intcolour_id = "0";
	public String preowned_refno = "";
	// public String preowned_close_date = "";
	// public String close_date = "";
	public String preowned_emp_id = "0";
	public String preowned_campaign_id = "0";
	public String preowned_soe_id = "0";
	public String preowned_sob_id = "0";
	// public String preowned_refno = "";
	public String preowned_notes = "";
	// public String preowned_custtype_id = "0";
	public String preowned_buyertype_id = "0";
	public String preowned_preownedcat_id = "0";
	public String preowned_entry_id = "0";
	public String preowned_entry_date = "";
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
	public String team_id = "0";
	public String contact_id = "0";
	public String ExeAccess = "";
	public String preownedteam_id = "0";
	public String brandconfig_preowned_email_enable = "0";
	public String brandconfig_preowned_email_exe_enable = "0";
	public String brandconfig_preowned_email_sub = "";
	public String brandconfig_preowned_email_format = "";
	public String brandconfig_preowned_email_exe_sub = "";
	public String brandconfig_preowned_email_exe_format = "";
	public String brandconfig_preowned_sms_enable = "0";
	public String brandconfig_preowned_sms_exe_enable = "0";
	public String brandconfig_preowned_sms_exe_format = "";
	public String brandconfig_preowned_sms_format = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_customer_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_preowned_refno = "";
	public String config_preowned_campaign = "";
	public String config_preowned_soe = "";
	public String emp_role_id = "";
	public String config_preowned_sob = "";
	public String preowned_variant_id = "0";
	public String model_name = "", city_name = "";
	public String preowned_sub_variant = "0";
	public String branch_preowned_brochure_email_enable = "";
	public String branch_preowned_brochure_email_format = "";
	public String branch_preowned_brochure_email_sub = "";
	public String send_contact_email = "";
	public String crmfollowup_crm_emp_id = "0";
	public String crmfollowupdays_daycount = "";
	public String crmfollowup_followup_time = "";
	public String add = "";
	public String preowned_close_date = "";
	public String comp_id = "0";
	public String preowned_branch_id = "0", emp_all_exe = "";
	public String branchtype_id = "0";
	public String preowned_preownedvariant_id = "0";
	public String preowned_budget = "";
	public String preowned_tradein_preownedvariant_id = "0";
	public String team_preownedbranch_id = "0";
	public String enquiry_id = "0";
	public String precrmfollowup_crm_emp_id = "0";
	public String precrmfollowupdays_daycount = "";
	public String precrmfollowup_followup_time = "";
	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();
	public Preowned_Variant_Check variantcheck = new Preowned_Variant_Check();

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
				preowned_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				BranchAccess = GetSession("BranchAccess", request);
				// BranchAccess = BranchAccess.replace("branch_id", "preowned_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				if (preowned_branch_id.equals("0")) {
					preowned_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (preowned_branch_id.equals("0")) {
						preowned_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 AND branch_branchtype_id = 2 LIMIT 1");
					}
				}
				// / branch_id = preowned_branch_id;
				emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
				addB = PadQuotes(request.getParameter("add_button1"));
				msg = PadQuotes(request.getParameter("msg"));
				// contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
				// preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				preowned_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				// preowned_variant_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_model_id")));
				branchtype_id = CNumeric(PadQuotes(request.getParameter("branchtype_id")));
				preowned_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_emp_id")));
				// emp_preowned_add = ReturnPerm(comp_id, "emp_preowned_access", request);
				// if (emp_preowned_add.equals("0")) {
				// response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				// }
				PopulateConfigDetails();
				if (!preowned_contact_id.equals("0")) {
					// / PopulateContactCustomerDetails();
				}
				if (!addB.equals("yes")) {
					contact_mobile1 = "91-";
					preowned_emp_id = emp_id;
					// team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id"
					// + " FROM " + compdb(comp_id) + "axela_sales_team_exe"
					// + " WHERE teamtrans_emp_id = " + emp_id));
					preowned_date = strToShortDate(ToShortDate(kknow()));
					preowned_close_date = strToShortDate(ToShortDate(kknow()));
					StrSql = "SELECT preownedteamtrans_team_id"
							+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe"
							+ " WHERE preownedteamtrans_emp_id = " + emp_id;
					// SOP("StrSql===" + StrSql);
					preownedteam_id = CNumeric(ExecuteQuery(StrSql));
					if (!enquiry_id.equals("0")) {
						StrSql = "SELECT enquiry_soe_id, enquiry_sob_id, enquiry_campaign_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
								+ " WHERE enquiry_id = " + enquiry_id;
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							preowned_soe_id = crs.getString("enquiry_soe_id");
							preowned_sob_id = crs.getString("enquiry_sob_id");
							preowned_campaign_id = crs.getString("enquiry_campaign_id");
						}
						crs.close();
					}
					if (!preowned_branch_id.equals("0")) {
						StrSql = "SELECT city_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " WHERE branch_id = " + preowned_branch_id + "";
						contact_city_id = ExecuteQuery(StrSql);
					}
				} else if (addB.equals("yes")) {
					String addmsg = "Pre-Owned added successfully!";
					GetValues(request, response);
					// CheckForm();
					if (msg.equals("")) {
						AddPreowned();
					}
					if (msg.equals("")) {
						msg = addmsg;
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-preowned-list.jsp?preowned_id=" + preowned_id + "&msg=" + msg));
					} else if (!msg.equals("")) {
						msg = "" + unescapehtml(msg);
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
	public void AddPreowned() {
		try {
			Preowned_Quickadd preowned = new Preowned_Quickadd();
			preowned.emp_id = emp_id;
			// -----customer n contact-------
			preowned.preowned_branch_id = branch_id;
			preowned.customer_name = preowned_fname + " " + preowned_lname;
			preowned.contact_fname = preowned_fname;
			preowned.contact_lname = preowned_lname;
			// enq.customer_zone_id = "4";
			preowned.contact_mobile1 = contact_mobile1;
			preowned.contact_mobile2 = contact_mobile2;
			preowned.contact_city_id = contact_city_id;
			preowned.preowned_soe_id = preowned_soe_id;// 22
			preowned.preowned_sob_id = preowned_sob_id;// 40
			// preowned.team_id = team_id;
			preowned.contact_address = "";
			preowned.contact_pin = "";
			preowned.preowned_close_date = strToShortDate(ToShortDate(kknow()));
			preowned.preowned_variant_id = preowned_variant_id;

			preowned.contact_email1 = contact_email1;
			preowned.contact_email2 = "";
			preowned.contact_phone1 = "";
			preowned.preowned_fcamt = "0";
			// preowned.preowned_buyertype_id = preowned_buyertype_id;
			// preowned.preowned_preownedcat_id = preowned_preownedcat_id;
			preowned.preowned_contact_id = "0";
			preowned.comp_id = comp_id;
			preowned.contact_title_id = preowned_title_id;
			// enq.lead_fname = preowned_fname;
			// enq.lead_lname = preowned_lname;
			// enq.lead_jobtitle = "";

			// -----config-------
			preowned.comp_email_enable = comp_email_enable;
			preowned.comp_sms_enable = comp_sms_enable;
			preowned.branch_email1 = branch_email1;
			preowned.config_email_enable = config_email_enable;
			preowned.config_sms_enable = config_sms_enable;
			preowned.config_preowned_refno = config_preowned_refno;
			preowned.config_preowned_campaign = config_preowned_campaign;
			preowned.config_preowned_soe = config_preowned_soe;
			preowned.config_preowned_sob = config_preowned_sob;
			preowned.config_customer_dupnames = config_customer_dupnames;
			preowned.brandconfig_preowned_email_enable = brandconfig_preowned_email_enable;
			preowned.brandconfig_preowned_email_format = brandconfig_preowned_email_format;
			preowned.brandconfig_preowned_email_sub = brandconfig_preowned_email_sub;
			preowned.brandconfig_preowned_email_exe_enable = brandconfig_preowned_email_exe_enable;
			preowned.brandconfig_preowned_email_exe_sub = brandconfig_preowned_email_exe_sub;
			preowned.brandconfig_preowned_email_exe_format = brandconfig_preowned_email_exe_format;
			preowned.brandconfig_preowned_sms_enable = brandconfig_preowned_sms_enable;
			preowned.brandconfig_preowned_sms_format = brandconfig_preowned_sms_format;
			preowned.brandconfig_preowned_sms_exe_enable = brandconfig_preowned_sms_exe_enable;
			preowned.brandconfig_preowned_sms_exe_format = brandconfig_preowned_sms_exe_format;
			preowned.emp_name = emp_name;
			preowned.emp_email1 = emp_email1;
			preowned.emp_email2 = emp_email2;
			if (!emp_email2.equals("")) {
				emp_email_formail = emp_email1 + "," + emp_email2;
			} else {
				emp_email_formail = emp_email1;
			}
			preowned.emp_email_formail = emp_email_formail;
			preowned.emp_email1 = emp_email1;
			preowned.emp_mobile2 = emp_mobile2;
			preowned.preowned_branch_id = preowned_branch_id;
			preowned.emp_email1 = emp_email1;
			preowned.emp_email2 = emp_email2;
			preowned.emp_name = emp_name;
			preowned.emp_mobile1 = emp_mobile1;
			preowned.emp_mobile2 = emp_mobile2;
			preowned.preowned_entry_date = ToLongDate(kknow());
			preowned.preowned_fueltype_id = preowned_fueltype_id;
			preowned.preowned_manufyear = preowned_manufyear;
			preowned.preowned_regno = preowned_regno;
			preowned.preowned_branch_id = preowned_branch_id;
			preowned.preowned_customer_id = preowned_customer_id;
			preowned.preowned_contact_id = preowned_contact_id;
			preowned.preowned_title = preowned_title;
			preowned.preowned_sub_variant = preowned_sub_variant;
			preowned.preowned_extcolour_id = "0";
			preowned.preowned_intcolour_id = "0";
			preowned.preowned_options = "";
			preowned.preowned_date = strToShortDate(ToShortDate(kknow()));
			preowned.preowned_variant_id = preowned_variant_id;
			preowned.preowned_fcamt = "0";
			preowned.preowned_noc = "";
			preowned.preowned_funding_bank = "";
			preowned.preowned_loan_no = "";
			preowned.preowned_insur_date = "";
			preowned.preowned_insurance_id = "0";
			preowned.preowned_ownership_id = preowned_ownership_id;
			preowned.preowned_regdyear = "";
			preowned.preowned_invoicevalue = "0";
			preowned.preowned_kms = "0";
			preowned.preowned_expectedprice = "0";
			preowned.preowned_quotedprice = "0";
			preowned.preowned_close_date = strToShortDate(ToLongDate(kknow()));
			preowned.preownedteam_id = preownedteam_id;
			preowned.preowned_emp_id = preowned_emp_id;
			preowned.preowned_enquiry_id = "0";
			// preowned.preowned_preowned_id = preowned_id;
			preowned.preowned_preownedstatus_id = "1";
			preowned.preowned_preownedstatus_date = "";
			preowned.preowned_preownedstatus_desc = "";
			preowned.preowned_prioritypreowned_id = "1";
			preowned.preowned_notes = "";
			preowned.preowned_desc = "";
			preowned.preowned_entry_id = emp_id;

			preowned.AddPreownedFields();
			msg = preowned.msg;
			preowned_id = preowned.preowned_id;
			// if (!preowned_tradein_preownedvariant_id.equals("0") && !team_preownedbranch_id.equals("0") && !team_preownedemp_id.equals("0")) {
			// preowned.preowned_tradein_preownedvariant_id = preowned_tradein_preownedvariant_id;
			// preowned.team_preownedemp_id = team_preownedemp_id;
			// preowned.team_preownedbranch_id = team_preownedbranch_id;
			// preowned.AddPreOwnedEnquiry();
			// }
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (preowned_contact_id.equals("0")) {

			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
			preowned_title_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_title_id")));
			preowned_fname = PadQuotes(request.getParameter("txt_preowned_fname"));
			preowned_lname = PadQuotes(request.getParameter("txt_preowned_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_mobile2 = PadQuotes(request.getParameter("txt_contact_mobile2"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		}
		preowned_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		// contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_city_id")));
		preowned_preownedtype_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedtype_id")));
		// preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		preowned_sub_variant = PadQuotes(request.getParameter("txt_preowned_sub_variant"));
		preowned_prefreg_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_prefreg_id")));
		preowned_presentcar = PadQuotes(request.getParameter("txt_preowned_presentcar"));
		preowned_finance = CNumeric(PadQuotes(request.getParameter("dr_preowned_finance")));
		preowned_budget = CNumeric(PadQuotes(request.getParameter("txt_preowned_budget")));
		preowned_tradein_preownedvariant_id = CNumeric(PadQuotes(request.getParameter("preowned_tradein_preownedvariant_id")));
		preowned_date = strToShortDate(ToShortDate(kknow()));
		preowned_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_fueltype_id")));
		preowned_manufyear = CNumeric(PadQuotes(request.getParameter("txt_preowned_manufyear")));
		preowned_regno = (PadQuotes(request.getParameter("txt_preowned_regno")));
		preowned_ownership_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_ownership_id")));
		preowned_soe_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_soe_id")));
		preowned_sob_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sob_id")));
		preowned_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_campaign_id")));
		preowned_buyertype_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_buyertype_id")));
		preowned_preownedcat_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedcat_id")));
		preowned_prefregyear = CNumeric(PadQuotes(request.getParameter("dr_preowned_prefregyear")));
		// preowned_desc = PadQuotes(request.getParameter("txt_preowned_desc"));
		preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		preowned_sub_variant = PadQuotes(request.getParameter("txt_preowned_sub_variant"));
		// team_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_team")));
		preowned_buyertype_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_buyertype_id")));
		// preowned_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_emp_id")));
		preownedteam_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_team")));
		SOP("preownedteam_id====" + preownedteam_id);

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

	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedtype_id, preownedtype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_type" + " GROUP BY preownedtype_id" + " ORDER BY preownedtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedtype_id"), preowned_preownedtype_id));
				Str.append(">").append(crs.getString("preownedtype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateItem(String preowned_model_id, String comp_id) {
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

	public String PopulateTeam(String preowned_branch_id, String preownedteam_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedteam_id, preownedteam_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_team"
					+ " WHERE preownedteam_branch_id = " + preowned_branch_id
					+ " GROUP BY preownedteam_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_team\" id=\"dr_preowned_team\" class=\"dropdown form-control\" onchange=\"PopulateExecutive();\">");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedteam_id"));
				Str.append(StrSelectdrop(crs.getString("preownedteam_id"), preownedteam_id));
				Str.append(">").append(crs.getString("preownedteam_name"))
						.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedExecutives(String branch_id, String team_id, String preowned_emp_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_preowned = 1"
					+ " AND emp_active = 1"
					+ " AND emp_branch_id = " + branch_id;
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id in (SELECT preownedteamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe "
						+ " WHERE preownedteamtrans_team_id =" + team_id + ")";
			}
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + " "
					+ " AND leave_active = 1 )";
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("PopulatepreTeamExecutives ==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_emp_id\" id=\"dr_preowned_emp_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), preowned_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
				Str.append(StrSelectdrop(crs.getString("soe_id"), preowned_soe_id));
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
				Str.append(StrSelectdrop(crs.getString("sob_id"), preowned_sob_id));
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
			preowned_date = strToShortDate(ToShortDate(kknow()));
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1"
					+ " AND camptrans_branch_id = " + branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ConvertShortDateToStr(preowned_date) + "',1,8)"
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ConvertShortDateToStr(preowned_date) + "',1,8)"
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), preowned_campaign_id));
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
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), preowned_fueltype_id));
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

	public String PopulateFuel() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " from " + compdb(comp_id) + "axela_fueltype"
					+ " order by fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), preowned_fueltype_id));
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

	public String PopulateOwnership() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select ownership_id, ownership_name"
					+ " from " + compdb(comp_id) + "axela_preowned_ownership"
					+ " order by ownership_id";
			Str.append("<option value=0>Select</option>");
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), ""));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
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
		Str.append("<option value=1").append(StrSelectdrop("1", preowned_finance)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", preowned_finance)).append(">No</option>\n");
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_preowned_soe, config_preowned_sob, config_preowned_refno,"
				+ " config_email_enable, config_sms_enable, comp_email_enable,"
				+ " config_preowned_campaign, config_customer_dupnames, comp_sms_enable,"
				+ " COALESCE(branch_email1, '') AS branch_email1,"
				+ " COALESCE (brandconfig_preowned_email_enable,'') AS brandconfig_preowned_email_enable,"
				+ " COALESCE (brandconfig_preowned_email_sub,'') AS brandconfig_preowned_email_sub,"
				+ " COALESCE (brandconfig_preowned_email_format,'') AS brandconfig_preowned_email_format,"
				+ " COALESCE (brandconfig_preowned_email_exe_enable,'') AS brandconfig_preowned_email_exe_enable,"
				+ " COALESCE (brandconfig_preowned_email_exe_sub,'') AS brandconfig_preowned_email_exe_sub,"
				+ " COALESCE (brandconfig_preowned_email_exe_format,'') AS brandconfig_preowned_email_exe_format,"
				+ " COALESCE (brandconfig_preowned_sms_enable,'') AS brandconfig_preowned_sms_enable,"
				+ " COALESCE (brandconfig_preowned_sms_format,'') AS brandconfig_preowned_sms_format,"
				+ " COALESCE (brandconfig_preowned_sms_exe_enable,'') AS brandconfig_preowned_sms_exe_enable,"
				+ " COALESCE (brandconfig_preowned_sms_exe_format,'') AS brandconfig_preowned_sms_exe_format,"
				+ " COALESCE(emp.emp_email1, '') AS emp_email1,"
				+ " COALESCE(emp.emp_email2, '') AS emp_email2,"
				+ " COALESCE(emp.emp_name, '') AS emp_name,"
				+ " COALESCE(emp.emp_mobile1, '') AS emp_mobile1,"
				+ " COALESCE(emp.emp_mobile2, '') AS emp_mobile2,"
				+ " COALESCE(branch_id, 0) AS branch_id"
				+ " FROM " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp, "
				+ compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + preowned_branch_id + ""
				+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + preowned_emp_id + ""
				+ " WHERE admin.emp_id = " + emp_id + "";
		SOP("configdetails-------------------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_preowned_refno = crs.getString("config_preowned_refno");
				config_preowned_campaign = crs.getString("config_preowned_campaign");
				config_preowned_soe = crs.getString("config_preowned_soe");
				config_preowned_sob = crs.getString("config_preowned_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_preowned_email_enable = crs.getString("brandconfig_preowned_email_enable");
				brandconfig_preowned_email_format = crs.getString("brandconfig_preowned_email_format");
				brandconfig_preowned_email_sub = crs.getString("brandconfig_preowned_email_sub");
				brandconfig_preowned_email_exe_enable = crs.getString("brandconfig_preowned_email_exe_enable");
				brandconfig_preowned_email_exe_sub = crs.getString("brandconfig_preowned_email_exe_sub");
				brandconfig_preowned_email_exe_format = crs.getString("brandconfig_preowned_email_exe_format");
				brandconfig_preowned_sms_enable = crs.getString("brandconfig_preowned_sms_enable");
				brandconfig_preowned_sms_format = crs.getString("brandconfig_preowned_sms_format");
				brandconfig_preowned_sms_exe_enable = crs.getString("brandconfig_preowned_sms_exe_enable");
				brandconfig_preowned_sms_exe_format = crs.getString("brandconfig_preowned_sms_exe_format");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
				preowned_branch_id = crs.getString("branch_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App==== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
