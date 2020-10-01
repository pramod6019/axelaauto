package axela.insurance;

////Saiman 11th Feb 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Insurance_Enquiry_Dash extends Connect {

	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String insurenquiry_branch_id = "0";
	public String branch_name = "";
	public String branch_brand_id = "0";
	public String insurenquiry_id = "0";
	public String date = "";
	public String insurenquiry_variant_id = "0";
	public String insurenquiry_reg_no = "0";
	public String insurenquiry_engine_no = "0";
	public String insurenquiry_chassis_no = "0";
	public String insurenquiry_contact_id = "0";
	public String insurenquiry_customer_id = "0";

	public String insurenquiryfollowup_customer_id = "";
	public String insurenquiryfollowup_customer_name = "";
	public String insurenquiryfollowup_contact_id = "";
	public String insurenquiryfollowup_contact_name = "";
	public String insurenquiryfollowup_contact_mobile = "";
	public String insurenquiryfollowup_variant = "";

	public String customer_name = "";
	public String insurenquirydate = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_email2 = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_pin = "";
	public String contact_title_id = "0";
	public String contact_address = "";
	public String contacts = "";
	public String insurenquiry_customer_name = "";
	public String insurenquiry_desc = "";
	public String insurenquiry_emp_id = "0", insurenquiry_refemp_id = "0";
	public int days_diff = 0;
	public String StrHTML = "";
	public String contact_city_id = "";
	public String insurenquiry_entry_id = "0";
	public String insurenquiry_entry_date = "";
	public String insurenquiry_modified_id = "0";
	public String insurenquiry_modified_date = "";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String ref_emp_name = "";
	public String comp_id = "0";
	// public String BranchAccess = "";
	public String ExeAccess = "";
	public String curryear = "", year1 = "";
	public String customerdetail = "";
	// For Followup
	public String status = "";
	public String followupHTML = "";
	public String followup_followup_time = "";
	public String followup_time = "", followuptime = "";
	public String followup_entry_id = "0";
	public String followup_entry_time = "";
	public String followup_desc = "";
	public String followup_emp_id = "0";
	public String followup_followuptype_id = "0";
	public String followup_feedbacktype_id = "0";
	public String followupdesc_id = "0";
	public String followupdesc_name = "";
	public String followup_id = "0";
	// public String delete = "";
	// public String submitB = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int PageCount = 10, recperpage = 0;
	public int PageCurrent = 0;
	public String docmsg = "";
	public String crmfollowupHTML = "";
	public String crmfollowup_id = "0", crm = "";
	public String crm_id = "0";
	public String psf_id = "0", psf = "", pbf = "", psfmsg = "", emp_all_exe = "";

	public String insurenquiry_sale_date = "", insurenquirysaledate = "";
	public String insurenquiry_renewal_date = "", renewal_date = "";;
	public String insurenquiry_insurtype_id = "";
	public String insurenquiry_previouscompname = "";
	public String insurenquiry_previousyearidv = "";
	public String insurenquiry_previousgrosspremium = "";
	public String insurenquiry_previousplanname = "";
	public String insurenquiry_policyexpirydate = "";
	public String insurenquiry_currentidv = "";
	public String insurenquiry_premium = "";
	public String insurenquiry_premiumwithzerodept = "";
	public String insurenquiry_compoffered = "";
	public String insurenquiry_plansuggested = "";
	public String insurenquiry_ncb = "";
	// public String insurenquiry_address = "";
	public String insurenquiry_soe_id = "0";
	public String insurenquiry_sob_id = "0";
	public String insurenquiry_campaign_id = "0";
	public String insurenquiry_notes = "";

	public String insurenquiry_insurstatus_id = "1";
	public String insurenquiry_insurstatus_desc = "";
	public String insurenquiry_insurstatus_date = "";
	public String insurelostcase1_id = "", soe_name = "", sob_name = "", campaign_name = "";

	// Insurance Followup
	public String insurenquiryfollowup_id = "0";
	public String submitB = "";
	public String addContactB = "";
	public String delete = "";
	// public String loaddata = "yes";
	public String StrSearch = "", service = "", insurance = "";
	public String insurfollowup_contactable_id = "0";
	public String disposition_id = "0", dr_inspection_id = "0", dr_inspection_result_id = "0", dr_appoint_verification_id = "0";
	public String dr_field_exe_id = "0", txt_remarks = "";
	public String addInsurFollowup = "", followup_info = "", veh_insurtype_id = "", veh_insur_notinterested_id = "";
	public String veh_insur_previouscompname = "";
	public String veh_insur_previousyearidv = "";
	public String veh_insur_previousgrosspremium = "";
	public String veh_insur_previousplanname = "";
	public String veh_insur_policyexpirydate = "";
	public String veh_insur_currentidv = "";
	public String veh_insur_premium = "";
	public String veh_insur_premiumwithzerodept = "";
	public String veh_insur_compoffered = "";
	public String veh_insur_plansuggested = "", veh_insur_name = "", veh_insur_variant = "", veh_insur_reg_no = "", veh_insur_chassis_no = "";
	public String veh_insur_ncb = "", veh_insur_address = "", insur_pickupaddress;
	public String[] insurfollowup_offer_id;
	public String insurfollowup_entry_time = "0";
	public String insurcustomerdetail = "";
	public String currenttimevalidate = "";
	public String insurfollowup_entry_id = "0";
	public String insurenquiry_insurgift_id = "";
	public String feedback_desc = "";
	public String txt_insurfollowup_time = "";
	public String dr_feedbacktype_id = "0", dr_nextfollowup_type_id = "0";
	public String insur_msg = "";

	public int rowcount = 0;
	public String dr_followup_disp1_id = "0";
	public String dr_followup_disp1 = "";
	public String dr_followup_disp1_mandatory = "";
	public String dr_followup_disp2_id = "0";
	public String dr_followup_disp2 = "";
	public String dr_followup_disp2_comments = "";
	public String dr_followup_disp2_mandatory = "";
	public String dr_followup_disp3_id = "0";
	public String dr_followup_disp3 = "";
	public String dr_followup_disp3_comments = "";
	public String dr_followup_disp3_mandatory = "";
	public String dr_followup_disp4_id = "0";
	public String dr_followup_disp4 = "";
	public String dr_followup_disp4_comments = "";
	public String dr_followup_disp4_mandatory = "";
	public String dr_followup_disp5_id = "0";
	public String dr_followup_disp5 = "";
	public String dr_followup_disp5_comments = "";
	public String dr_followup_disp5_mandatory = "", txt_next_followup_time = "", txt_followup_comment = "";
	public String dr_followup_disp_nextfollowup = "";
	public String dr_contactable_id = "0";

	public String[] arr = new String[6];

	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				// SOP("coming..");
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				msg = unescapehtml(PadQuotes(request.getParameter("msg")));
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				dr_feedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_feedbacktype_id")));
				dr_nextfollowup_type_id = CNumeric(PadQuotes(request.getParameter("dr_nextfollowup_type_id")));
				feedback_desc = PadQuotes(request.getParameter("txt_feedback_desc")).trim();
				followup_id = CNumeric(PadQuotes(request.getParameter("followup_id")));

				PopulateFields(response);
				// SOP("1");

				ListCustomerDetails(comp_id, insurenquiry_id, "");
				// SOP("2");
				currenttimevalidate = ToLongDate(kknow());
				addInsurFollowup = PadQuotes(request.getParameter("add_insurfollowup_button"));
				GetInsurDetails(insurenquiry_id, comp_id);
				// SOP("3");
				followup_info = ListInsuranceFollowup(comp_id, insurenquiry_id, emp_id);
				// SOP("4");

				if (delete.equals("yes") && !insurenquiryfollowup_id.equals("0") && emp_id.equals("1")) {
					DeleteInsurFollowupFields();
					response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
				}
				// SOP("5");

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			if (!comp_id.equals("0")) {

				StrSql = "SELECT "
						+ " insurenquiry_id,"
						+ " insurenquiry_branch_id,"
						+ " branch_brand_id,"
						+ " branch_name,"
						+ " insurenquiry_date,"
						+ " COALESCE ( insurenquiry_emp_id,0) AS insurenquiry_emp_id,"
						+ " insur.emp_name,"
						+ " insurenquiry_refemp_id,"
						+ " COALESCE(ref.emp_name, '') AS refemp,"
						+ " insurenquiry_variant_id,"
						+ " insurenquiry_reg_no,"
						+ " insurenquiry_chassis_no,"
						+ " insurenquiry_engine_no,"
						+ " insurenquiry_sale_date,"
						+ " insurenquiry_renewal_date, "
						+ " insurenquiry_insurtype_id,"
						+ " insurenquiry_previouscompname, "
						+ " insurenquiry_previousyearidv,"
						+ " insurenquiry_previousgrosspremium, "
						+ " insurenquiry_previousplanname,"
						+ " insurenquiry_policyexpirydate, "
						+ " insurenquiry_currentidv,"
						+ " insurenquiry_premium, "
						+ " insurenquiry_premiumwithzerodept,"
						+ " insurenquiry_compoffered, "
						+ " insurenquiry_plansuggested, "
						+ " insurenquiry_ncb,"
						// + " insurenquiry_address, "
						+ " insurenquiry_soe_id, "
						+ " insurenquiry_sob_id,"
						+ " insurenquiry_campaign_id, "
						+ " variant_name,"
						+ " insurenquiry_customer_id,"
						+ " customer_name,"
						+ " insurenquiry_contact_id,"
						+ " contact_title_id,"
						+ " title_desc,"
						+ " contact_fname,"
						+ " contact_lname,"
						+ " contact_mobile1,"
						+ " contact_mobile2,"
						+ " contact_email1,"
						+ " contact_email2,"
						+ " contact_phone1,"
						+ " contact_phone2,"
						+ " contact_address,"
						+ " contact_pin,"
						+ " contact_city_id,"
						+ " insurenquiry_entry_id,"
						+ " insurenquiry_entry_date,"
						+ " insurenquiry_modified_id,"
						+ " insurenquiry_modified_date,"
						+ " insurenquiry_notes,"
						+ " insurenquiry_insurstatus_id,"
						+ " insurenquiry_insurstatus_date,"
						+ " insurenquiry_insurstatus_desc,"
						+ " insurenquiry_insurlostcase1_id,"
						+ "  COALESCE (soe_name,'') AS soe_name,"
						+ " COALESCE (sob_name, '') AS sob_name,"
						+ " COALESCE (campaign_name, '') AS campaign_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp insur ON insur.emp_id = insurenquiry_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ref ON ref.emp_id = insurenquiry_refemp_id"
						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = insurenquiry_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON soe_id = insurenquiry_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = insurenquiry_campaign_id"
						+ " WHERE 1 = 1"
						+ " AND insurenquiry_id = " + insurenquiry_id
						// + BranchAccess
						+ ExeAccess.replace("emp_id", "insurenquiry_emp_id");
				StrSql += " GROUP BY insurenquiry_id ";
				SOP("Dash-----PopulateFields-------" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						insurenquiry_id = crs.getString("insurenquiry_id");
						insurenquiry_branch_id = crs.getString("insurenquiry_branch_id");
						branch_name = crs.getString("branch_name");
						branch_brand_id = crs.getString("branch_brand_id");
						date = strToShortDate(crs.getString("insurenquiry_entry_date"));
						insurenquiry_desc = crs.getString("insurenquiry_notes");
						insurenquiry_variant_id = crs.getString("insurenquiry_variant_id");
						insurenquiry_reg_no = crs.getString("insurenquiry_reg_no");
						insurenquiry_chassis_no = crs.getString("insurenquiry_chassis_no");
						insurenquiry_engine_no = crs.getString("insurenquiry_engine_no");

						insurenquirysaledate = strToShortDate(crs.getString("insurenquiry_sale_date"));
						insurenquiry_emp_id = crs.getString("insurenquiry_emp_id");
						renewal_date = strToShortDate(crs.getString("insurenquiry_renewal_date"));
						insurenquiry_insurtype_id = crs.getString("insurenquiry_insurtype_id");
						insurenquiry_previouscompname = crs.getString("insurenquiry_previouscompname");
						insurenquiry_previousyearidv = crs.getString("insurenquiry_previousyearidv");
						insurenquiry_previousgrosspremium = crs.getString("insurenquiry_previousgrosspremium");
						insurenquiry_previousplanname = crs.getString("insurenquiry_previousplanname");
						insurenquiry_policyexpirydate = strToShortDate(crs.getString("insurenquiry_policyexpirydate"));
						insurenquiry_currentidv = crs.getString("insurenquiry_currentidv");
						insurenquiry_premium = crs.getString("insurenquiry_premium");
						insurenquiry_premiumwithzerodept = crs.getString("insurenquiry_premiumwithzerodept");
						insurenquiry_compoffered = crs.getString("insurenquiry_compoffered");
						insurenquiry_plansuggested = crs.getString("insurenquiry_plansuggested");
						insurenquiry_ncb = crs.getString("insurenquiry_ncb");
						// insurenquiry_address = crs.getString("insurenquiry_address");
						insurenquiry_soe_id = crs.getString("insurenquiry_soe_id");
						insurenquiry_sob_id = crs.getString("insurenquiry_sob_id");
						insurenquiry_campaign_id = crs.getString("insurenquiry_campaign_id");
						insurenquiry_notes = crs.getString("insurenquiry_notes");

						insurenquiry_emp_id = crs.getString("insurenquiry_emp_id");
						insurenquiry_refemp_id = crs.getString("insurenquiry_refemp_id");
						ref_emp_name = crs.getString("refemp");
						insurenquiry_customer_id = crs.getString("insurenquiry_customer_id");
						insurenquiry_contact_id = crs.getString("insurenquiry_contact_id");
						insurenquiry_customer_name = crs.getString("customer_name");
						contact_title_id = crs.getString("contact_title_id");
						contact_fname = crs.getString("contact_fname");
						contact_lname = crs.getString("contact_lname");
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

						insurenquiry_insurstatus_id = crs.getString("insurenquiry_insurstatus_id");
						insurenquiry_insurstatus_desc = crs.getString("insurenquiry_insurstatus_desc");
						insurenquiry_insurstatus_date = crs.getString("insurenquiry_insurstatus_date");
						insurelostcase1_id = crs.getString("insurenquiry_insurlostcase1_id");
						soe_name = crs.getString("soe_name");
						sob_name = crs.getString("sob_name");
						campaign_name = crs.getString("campaign_name");

						insurenquiry_entry_id = crs.getString("insurenquiry_entry_id");
						entry_by = Exename(comp_id, crs.getInt("insurenquiry_entry_id"));
						entry_date = strToLongDate(crs.getString("insurenquiry_entry_date"));
						insurenquiry_modified_id = crs.getString("insurenquiry_modified_id");
						if (!insurenquiry_modified_id.equals("0")) {
							modified_by = Exename(comp_id, Integer.parseInt(insurenquiry_modified_id));
							modified_date = strToLongDate(crs.getString("insurenquiry_modified_date"));
						}
					}
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListPolicy(String comp_id, String insurenquiry_id) {

		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			try {
				int count = 0;
				StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, insurpolicy_insurenquiry_id,"
						+ " COALESCE(insurenquiry_id, 0) AS insurenquiry_id,"
						+ " COALESCE(insurenquiry_reg_no, 0) AS insurenquiry_reg_no, insurpolicy_contact_id,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " insurpolicy_date,"
						+ " COALESCE(policytype_name, '') AS policytype_name,"
						+ " insurpolicy_policy_no, inscomp_name,"
						+ " insurpolicy_premium_amt, insurpolicy_idv_amt, insurpolicy_od_amt, insurpolicy_od_discount, insurpolicy_payout,"
						+ " insurtype_name, insurpolicy_customer_id, insurpolicy_start_date, insurpolicy_end_date, insurpolicy_entry_date, customer_name,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_id, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(preownedmodel_name, '') AS preownedmodel_name, insurpolicy_active,"
						+ " COALESCE(variant_name, '') AS variant_name, insurpolicy_emp_id,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";

				StrSql += " FROM " + compdb(comp_id) + "axela_insurance_policy"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurpolicy_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = insurpolicy_inscomp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurpolicy_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurpolicy_insurenquiry_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
						+ " WHERE 1 = 1"
						+ " AND insurpolicy_insurenquiry_id=" + insurenquiry_id;

				StrSql += " GROUP BY insurpolicy_id"
						+ " ORDER BY insurpolicy_id DESC";

				// SOP("StrSql====" + StrSql);

				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<div class=\"portlet box  \">");
				Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Policy</div>");
				Str.append("</div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"tab-pane\" id=\"\">");

				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th>Insurance Enquiry ID</th>\n");
					Str.append("<th>Insurance</th>\n");
					Str.append("<th style=\"width:200px;\" data-hide=\"phone\">Customer</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Type</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Term</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Amount</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Executive</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
				}
				else {
					Str.append("<div align='right'><a href=\"../insurance/insurance-update.jsp?add=yes&insurenquiry_id=")
							.append(insurenquiry_id).append("\">").append("Add New Policy...").append("</a></div>");
					Str.append("<center><br><br><font color=red><b>No Policy(s) found!</b></font></center>");
				}

				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("insurpolicy_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("insurpolicy_id") + ");'");
					Str.append(" style='height:100px'>\n");
					Str.append("<td >").append(count).append("</td>\n");
					Str.append("<td nowrap>").append(crs.getString("insurpolicy_id")).append("</td>\n");
					Str.append("<td nowrap>");
					Str.append("<a href=\"../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=").append(crs.getString("insurpolicy_insurenquiry_id")).append("\">");
					Str.append(crs.getString("insurpolicy_insurenquiry_id")).append("</a>").append("</td>\n");
					Str.append("<td nowrap>Company: ").append(crs.getString("inscomp_name"));
					Str.append("<br>Policy: ").append(crs.getString("policytype_name"));
					Str.append("<br>Policy No.: ").append(crs.getString("insurpolicy_policy_no"));
					if (crs.getString("insurpolicy_active").equals("0")) {
						Str.append("<br><font color=\"red\">[Inactive]</font>");
					}
					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("insurpolicy_customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("insurpolicy_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("insurpolicy_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}
					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}
					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("insurpolicy_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>\n<td>").append(crs.getString("insurtype_name")).append("</td>\n");

					if (!crs.getString("insurpolicy_entry_date").equals("")) {
						Str.append("<td>").append(strToShortDate(crs.getString("insurpolicy_entry_date"))).append("</td>\n");
					}

					Str.append("<td nowrap>");
					if (!crs.getString("insurpolicy_start_date").equals("")) {
						Str.append(strToShortDate(crs.getString("insurpolicy_start_date"))).append("-").append(strToShortDate(crs.getString("insurpolicy_end_date"))).append(" ");
					}
					String startdate = crs.getString("insurpolicy_start_date").substring(0, 8);
					String enddate = crs.getString("insurpolicy_end_date").substring(0, 8);
					if (Long.parseLong(enddate) < Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=\"red\">[Expired]</font>");
					} else if (Long.parseLong(startdate) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=\"blue\">[Future Insurance]</font>");
					}
					Str.append("</td>\n<td nowrap>");
					if (!crs.getString("insurpolicy_premium_amt").equals("0")) {
						Str.append("Premium Amount: ").append(crs.getString("insurpolicy_premium_amt")).append("<br>");
					}
					if (!crs.getString("insurpolicy_idv_amt").equals("0")) {
						Str.append("IDV Amount: ").append(crs.getString("insurpolicy_idv_amt")).append("<br>");
					}
					if (!crs.getString("insurpolicy_od_amt").equals("0")) {
						Str.append("OD Amount: ").append(crs.getString("insurpolicy_od_amt")).append("<br>");
					}
					if (!crs.getString("insurpolicy_od_discount").equals("0")) {
						Str.append("OD Discount: ").append(crs.getString("insurpolicy_od_discount")).append("<br>");
					}
					if (!crs.getString("insurpolicy_payout").equals("0")) {
						Str.append("Payout Amount: ").append(crs.getString("insurpolicy_payout"));
					}
					Str.append("</td>\n<td>");
					Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("insurpolicy_emp_id")).append("\">");
					Str.append(crs.getString("emp_name")).append("</a>");
					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branch_name")).append("</a>");
					Str.append("</td>\n<td nowrap>");
					Str.append("<a href=\"insurance-update.jsp?update=yes&insurpolicy_id=").append(crs.getString("insurpolicy_id"))
							.append("\">Update Insurance</a>");
					Str.append("<br><a href=\"insurance-docs-list.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">List Documents</a>");
					Str.append("</td>\n</tr>\n");
				}

				crs.beforeFirst();
				if (crs.isBeforeFirst()) {
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
				}

				Str.append("</div>\n");
				Str.append("</div>\n");
				Str.append("</div>\n");

				crs.close();
				// SOP("So order====" + Str);
				crs.close();
			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String ListHistory(String comp_id) {

		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0")) {
			StrSql = "SELECT " + compdb(comp_id) + "axela_insurance_history.*,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " emp_id "
					+ " FROM " + compdb(comp_id) + "axela_insurance_history "
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = history_insurenquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id "
					+ " WHERE history_insurenquiry_id = " + insurenquiry_id + ""
					+ " ORDER BY history_id desc";
			// SOP("StrSql------ListHistory------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
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
						// enquiry_title = crs.getString("enquiry_title");
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
					+ " WHERE 1=1 and emp_insur='1' AND emp_active='1' "
					+ "" + ExeAccess
					+ " GROUP BY emp_id " + " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), insurenquiry_emp_id));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void CheckForm() {
		msg = "";
		int followupcount = 0;
		if (followup_emp_id.equals("0")) {
			msg = msg + "<br>No Sales Consultant allocated!";
		}
		followupcount = Integer.parseInt(ExecuteQuery("SELECT COUNT(followup_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " WHERE followup_enquiry_id = " + insurenquiry_id));

		if (status.equals("Update") && followup_feedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Action Taken!";
		}
		if (status.equals("Update") && followup_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
	}

	public String PopulateCampaign(String insurenquiry_branch_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1 "
					+ " AND camptrans_branch_id = " + insurenquiry_branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('" + ToLongDate((kknow())) + "', 1, 8) "
					+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8) "
					// + " AND branch_id =" + booking_veh_branch_id
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// SOP("PopulateCampaign-------" + StrSql);
			// //SOP("insurance_veh_branch_id==" + insurenquiry_branch_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insurenquiry_campaign_id\" id=\"dr_insurenquiry_campaign_id\" "
					+ " onChange=\"SecurityCheck('dr_insurenquiry_campaign_id',this,'hint_dr_insurenquiry_campaign_id');\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), insurenquiry_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
						.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			// //SOP("Str==" + Str);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1 = 1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id = " + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), insurenquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + insurenquiry_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), insurenquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateInsurType(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY insurtype_id"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Insurance Type</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(Selectdrop(crs.getInt("insurtype_id"), insurenquiry_insurtype_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
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
			StrSql = "SELECT insurstatus_id, insurstatus_name "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_status "
					+ " WHERE 1 = 1 "
					+ " ORDER BY insurstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("insurstatus_id"), insurenquiry_insurstatus_id));
				Str.append(">").append(crs.getString("insurstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateGift() {

		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT insurgift_id, insurgift_name "
					+ " FROM " + compdb(comp_id) + "axela_insurance_gift "
					+ " WHERE 1 = 1 "
					+ " ORDER BY insurgift_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select Gift</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurgift_id")).append("");
				Str.append(StrSelectdrop(crs.getString("insurgift_id"), insurenquiry_insurgift_id));
				Str.append(">").append(crs.getString("insurgift_name")).append("</option>\n");
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
			StrSql = "SELECT insurlostcase1_id, insurlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY insurlostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurlostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("insurlostcase1_id"), insurelostcase1_id));
				Str.append(">").append(crs.getString("insurlostcase1_name")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String GetInsurDetails(String insurenquiry_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT  COALESCE(insurenquiry_previouscompname,'') AS insurenquiry_previouscompname,"
				+ " COALESCE(insurenquiry_previousyearidv,'') AS insurenquiry_previousyearidv,"
				+ " COALESCE(insurenquiry_previousgrosspremium,'') AS insurenquiry_previousgrosspremium,"
				+ " COALESCE(insurenquiry_previousplanname,'') AS insurenquiry_previousplanname,"
				+ " COALESCE(insurenquiry_policyexpirydate,'') AS insurenquiry_policyexpirydate,"
				+ " COALESCE(insurenquiry_currentidv,'') AS insurenquiry_currentidv,"
				+ " COALESCE(insurenquiry_premium,'') AS insurenquiry_premium,"
				+ " COALESCE(insurenquiry_premiumwithzerodept,'') AS insurenquiry_premiumwithzerodept,"
				+ " COALESCE(insurenquiry_compoffered,'') AS insurenquiry_compoffered,"
				+ " COALESCE(insurenquiry_plansuggested,'') AS insurenquiry_plansuggested,"
				+ " CONCAT(	title_desc,' ', contact_fname,' ',contact_lname	) AS veh_contactname,"
				+ " COALESCE(insurenquiry_variant,'') AS insurenquiry_variant,"
				+ " COALESCE(insurenquiry_ncb,'') AS insurenquiry_ncb,"
				+ " COALESCE(insurenquiry_reg_no,'') AS insurenquiry_reg_no,"
				+ " COALESCE(insurenquiry_chassis_no,'') AS insurenquiry_chassis_no,"
				+ " COALESCE(insurenquiry_insurgift_id,'') AS insurenquiry_insurgift_id,"
				+ " COALESCE(contact_address,'') AS contact_address,"
				+ " insurenquiry_pickupaddress "
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id "
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id"
				+ " WHERE 1 = 1"
				+ " AND insurenquiry_id = " + insurenquiry_id;
		// SOP("StrSql==GetInsurDetails==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				veh_insur_previouscompname = crs.getString("insurenquiry_previouscompname");
				veh_insur_previousyearidv = crs.getString("insurenquiry_previousyearidv");
				veh_insur_previousgrosspremium = crs.getString("insurenquiry_previousgrosspremium");
				veh_insur_previousplanname = crs.getString("insurenquiry_previousplanname");
				veh_insur_policyexpirydate = strToShortDate(crs.getString("insurenquiry_policyexpirydate"));
				veh_insur_currentidv = crs.getString("insurenquiry_currentidv");
				veh_insur_premium = crs.getString("insurenquiry_premium");
				veh_insur_premiumwithzerodept = crs.getString("insurenquiry_premiumwithzerodept");
				veh_insur_compoffered = crs.getString("insurenquiry_compoffered");
				veh_insur_plansuggested = crs.getString("insurenquiry_plansuggested");
				veh_insur_name = crs.getString("veh_contactname");
				veh_insur_variant = crs.getString("insurenquiry_variant");
				veh_insur_ncb = crs.getString("insurenquiry_ncb");
				veh_insur_reg_no = crs.getString("insurenquiry_reg_no");
				veh_insur_chassis_no = crs.getString("insurenquiry_chassis_no");
				veh_insur_address = crs.getString("contact_address");
				insur_pickupaddress = crs.getString("insurenquiry_pickupaddress");
				insurenquiry_insurgift_id = crs.getString("insurenquiry_insurgift_id");

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListInsuranceFollowup(String comp_id, String insurenquiry_id, String emp_id) {

		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT"
				+ " insurenquiryfollowup_id,"
				+ " insurenquiryfollowup_insurenquiry_id,"
				+ " insurenquiryfollowup_followup_time,"
				+ " insurenquiryfollowup_disp1,"
				+ " insurenquiryfollowup_disp2, "
				+ " insurenquiryfollowup_disp3, "
				+ " insurenquiryfollowup_disp4, "
				+ " insurenquiryfollowup_disp5, "
				+ " insurenquiryfollowup_comments,"
				+ " insurenquiryfollowup_nextfollowup_time,"
				+ " insurenquiryfollowup_entry_id,"
				+ " insurenquiryfollowup_entry_time,"
				+ " COALESCE (insurenquiryfollowup_modified_id, 0) AS insurenquiryfollowup_modified_id,"
				+ " COALESCE (insurenquiryfollowup_modified_time, 0) AS insurenquiryfollowup_modified_time"
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
				+ " WHERE insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id
				+ " ORDER BY insurenquiryfollowup_followup_time DESC";

		// SOP("Str====ListInsuranceFollowup==== " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-bordered table-responsive\" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition 1</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition 2</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition 3</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition 4</th>\n");
				Str.append("<th data-hide=\"phone\">Disposition 5</th>\n");
				Str.append("<th data-hide=\"phone\">Comments</th>\n");
				Str.append("<th data-hide=\"phone\">Next Follow-up Time</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Entry By</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("insurenquiryfollowup_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br/><a href=\"../insurance/new-vehicle-dash-insurancefollowup.jsp?");
						Str.append("Delete=yes&insurenquiry_id=").append(crs.getString("insurenquiryfollowup_insurenquiry_id")).append("&insurenquiryfollowup_id=");
						Str.append(crs.getString("insurenquiryfollowup_id")).append("\">Delete Follow-up</a>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_disp1")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_disp2")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_disp3")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_disp4")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_disp5")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("insurenquiryfollowup_comments")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(strToLongDate(crs.getString("insurenquiryfollowup_nextfollowup_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">");
					Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurenquiryfollowup_entry_id"))));
					Str.append("<br>").append(strToLongDate(crs.getString("insurenquiryfollowup_entry_time")));
					Str.append("&nbsp;</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				// Str.append("<br><br><br><br><center><font color=\"red\"><b>No Follow-up found!</b></font></center><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void DeleteInsurFollowupFields() {

		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " WHERE insurenquiryfollowup_id = " + insurenquiryfollowup_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateFeedbackType(String comp_id, String dr_feedbacktype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurfeedbacktype_id, insurfeedbacktype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_feedback_type"
					+ " WHERE	1 = 1"
					+ " GROUP BY insurfeedbacktype_id"
					+ " ORDER BY insurfeedbacktype_name";
			// SOP("Feed===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurfeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("insurfeedbacktype_id"), dr_feedbacktype_id));
				Str.append(">").append(crs.getString("insurfeedbacktype_name")).append("</option>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void ListCustomerDetails(String comp_id, String insurenquiry_id, String type) {

		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0")) {

			StrSql = " SELECT customer_id, customer_name, contact_id, "
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, "
					+ " contact_mobile1, contact_mobile2, variant_name,"
					+ " insurenquiry_sale_date, insurenquiry_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE insurenquiry_id = " + insurenquiry_id
					+ " GROUP BY insurenquiry_id";
			// SOP("StrSql-----3111-- " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				while (crs.next()) {

					insurenquiryfollowup_customer_id = crs.getString("customer_id");
					insurenquiryfollowup_customer_name = crs.getString("customer_name");
					insurenquiryfollowup_contact_id = crs.getString("contact_id");
					insurenquiryfollowup_contact_name = crs.getString("contacts");

					if (!crs.getString("contact_mobile1").equals("")) {
						insurenquiryfollowup_contact_mobile = crs.getString("contact_mobile1");
					}
					if (!crs.getString("contact_mobile2").equals("") && !insurenquiryfollowup_contact_mobile.equals("")) {
						insurenquiryfollowup_contact_mobile = crs.getString("contact_mobile1");
					}

					insurenquiryfollowup_variant = crs.getString("variant_name");

				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateOffer(String comp_id, String[] dr_offer_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insuroffer_id, insuroffer_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_offer"
					+ " WHERE 1 = 1"
					+ " GROUP BY insuroffer_id"
					+ " ORDER BY insuroffer_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<input type=\"checkbox\" id=\"dr_offer_id\" name=\"dr_offer_id\" value='").append(crs.getString("insuroffer_id")).append("'");
				Str.append(ArrSelectCheck(crs.getInt("insuroffer_id"), dr_offer_id));
				Str.append(">").append(crs.getString("insuroffer_name")).append("&nbsp;&nbsp;");
			}
			// SOP("Str===" + Str.toString());

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateContactable(String dr_contactable_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT insurdisposition_id, insurdisposition_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_parent_id = 0"
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurdisposition_id"));
				Str.append(StrSelectdrop(crs.getString("insurdisposition_id"), dr_contactable_id));
				Str.append(">").append(crs.getString("insurdisposition_name")).append("</option>\n");
			}
			// SOP("Str===" + Str.toString());

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurFollowupFields(String comp_id, String parentid, int divid) {

		StringBuilder Str = new StringBuilder();
		String fieldvalue = "";
		divid++;
		try {

			StrSql = " SELECT insurdisposition_id, insurdisposition_name,"
					+ " insurdisposition_comments,"
					+ " insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_active = 1"
					+ " AND insurdisposition_parent_id = " + parentid
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				if (divid == 1) {
					Str.append("<div class='form-element1 form-element-padding text-right'></div>");
				} else {
					Str.append("<div class='form-element1 form-element-padding text-right'>&nbsp; </div>");
				}
				crs.beforeFirst();

				if (divid == 1) {
					Str.append("<div class='form-element2 form-element-padding text-left'>");
					Str.append("Contactable");
					Str.append("<font color='red'>*</font>:");
				} else {
					String StrSql1 = " SELECT insurdisposition_mandatory, insurdisposition_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE 1 = 1"
							+ " AND insurdisposition_active = 1"
							+ " AND insurdisposition_id = " + parentid;
					CachedRowSet crs1 = processQuery(StrSql1);
					if (crs1.isBeforeFirst()) {
						while (crs1.next()) {
							Str.append("<div class='form-element10 form-element-padding text-left'>");
							Str.append(crs1.getString("insurdisposition_name"));
							if (crs1.getString("insurdisposition_mandatory").equals("1")) {
								Str.append("<font color='red'>*</font>");
							}
							Str.append(": ");
						}
						crs1.close();
					}

				}

				Str.append("<select name=dr_followup_disp").append(divid).append(" id=dr_followup_disp").append(divid);
				Str.append(" onchange='hidefields(this.id);populatefolllowupfields(this.id,\"disprow" + divid + "\");'>");
				Str.append("<option value=\"\">Select</option>\n");
				while (crs.next()) {
					Str.append("<option" + " value=\"").append(crs.getString("insurdisposition_id")).append("\" ");
					Str.append(StrSelectdrop(crs.getString("insurdisposition_id"), fieldvalue));
					Str.append(">").append(crs.getString("insurdisposition_name")).append("</option>\n");
				}
				Str.append("</select></div>");
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateNextFollowupField(String comp_id, String parentid, int divid) {

		StringBuilder Str = new StringBuilder();
		divid++;
		try {

			StrSql = " SELECT insurdisposition_id, insurdisposition_name,"
					+ " insurdisposition_comments,"
					+ " insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_active = 1"
					+ " AND insurdisposition_id = " + parentid
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next() && crs.getString("insurdisposition_nextfollowup").equals("1")) {
					if (crs.getString("insurdisposition_nextfollowup").equals("1")) {
						Str.append("<div class='form-element1 form-element-padding text-right'></div>");
						Str.append("<div class=\" form-element2 form-element-padding\">"
								+ "Next Follow-up Time<font color='red'>*</font>:<input name=\"txt_next_followup_time\"  id=\"txt_next_followup_time\"  class=\"form-control datetimepicker\""
								+ "onmouseover=\"$('.datetimepicker').bootstrapMaterialDatePicker({ format : 'DD/MM/YYYY HH:mm',switchOnClick : true});\""
								+ " type='text'/>");
						Str.append("</div>");
					}
					break;
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCommentsField(String comp_id, String parentid, int divid) {

		StringBuilder Str = new StringBuilder();
		divid++;
		try {

			StrSql = " SELECT insurdisposition_id, insurdisposition_name,"
					+ " insurdisposition_comments,"
					+ " insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_active = 1"
					+ " AND insurdisposition_id = " + parentid
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_name";
			SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next() && crs.getString("insurdisposition_comments").equals("1")) {
					Str.append("<div class='row'></div>");
					Str.append("<div class='form-element2 text-right'>Comments :</div>");
					Str.append("<div class=\" form-element4\">");
					Str.append("<textarea name=txt_followup_comment").append(" id=txt_followup_comment");
					Str.append(" cols=\"80\" rows=\"5\" class=\"form-control text-left\" ");
					Str.append("onKeyUp=\"charcount('").append("txt_followup_comment").append("', 'span_txt_followup_comment");
					Str.append("','<font color=red>({CHAR} characters left)</font>',").append("'255'");
					Str.append(")\">").append("</textarea>");
					Str.append("<span_txt_followup_comment").append("\"> (").append("255").append(" Characters)</span>");
					Str.append("</div>");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInstructionField(String comp_id, String parentid, int divid) {

		StringBuilder Str = new StringBuilder();
		divid++;
		try {

			StrSql = " SELECT insurdisposition_id, insurdisposition_name,"
					+ " insurdisposition_comments,"
					+ " insurdisposition_instructions,"
					+ " insurdisposition_nextfollowup "
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE 1 = 1"
					+ " AND insurdisposition_active = 1"
					+ " AND insurdisposition_id = " + parentid
					+ " GROUP BY insurdisposition_id"
					+ " ORDER BY insurdisposition_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next() && !crs.getString("insurdisposition_instructions").equals("")) {
					if (!crs.getString("insurdisposition_instructions").equals("")) {
						Str.append("<div class='row'></div>");
						Str.append("<div class='form-element2 text-right'>Instructions :</div>");
						Str.append("<div class=\" form-element4 text-left\">");
						Str.append(crs.getString("insurdisposition_instructions"));
						Str.append("</div>");
					}
					break;
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request) {
		// rowcount = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("rowcount").replace("disprow", ""))));
		dr_followup_disp1_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp1")));
		dr_followup_disp2_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp2")));
		dr_followup_disp3_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp3")));
		dr_followup_disp4_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp4")));
		dr_followup_disp5_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp5")));
		txt_followup_comment = PadQuotes(request.getParameter("txt_followup_comment"));
		txt_next_followup_time = ConvertLongDateToStr(PadQuotes(request.getParameter("txt_next_followup_time")));
		insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
		emp_id = CNumeric(GetSession("emp_id", request));
	}

	public String FollowupFieldValidate(String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		String Strsql = "";
		try {
			Strsql = "SELECT insurdisposition_name, insurdisposition_mandatory"
					+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
					+ " WHERE insurdisposition_id = " + dr_followup_disp1_id;
			crs = processQuery(Strsql);
			while (crs.next()) {
				dr_followup_disp1 = crs.getString("insurdisposition_name");
				dr_followup_disp1_mandatory = crs.getString("insurdisposition_mandatory");
			}
			if (dr_followup_disp1_id.equals("0")) {
				insur_msg += "<br>Select Contactable!";
			}
			if (!dr_followup_disp2_id.equals("0")) {
				Strsql = "SELECT insurdisposition_name, insurdisposition_mandatory,"
						+ " insurdisposition_nextfollowup "
						+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
						+ " WHERE insurdisposition_id = " + dr_followup_disp2_id;
				crs = processQuery(Strsql);
				while (crs.next()) {
					dr_followup_disp2 = crs.getString("insurdisposition_name");
					dr_followup_disp2_mandatory = crs.getString("insurdisposition_mandatory");
					dr_followup_disp_nextfollowup = crs.getString("insurdisposition_nextfollowup");
				}
				crs.close();
			} else {
				if (dr_followup_disp1_mandatory.equals("1") && dr_followup_disp2_id.equals("0")) {
					insur_msg += "<font color='red'><b><br> Select child value for Contactable !</b></font>";
				}
			}

			if (!dr_followup_disp3_id.equals("0")) {
				Strsql = "SELECT insurdisposition_name, insurdisposition_mandatory,"
						+ " insurdisposition_nextfollowup "
						+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
						+ " WHERE insurdisposition_id = " + dr_followup_disp3_id;
				crs = processQuery(Strsql);
				while (crs.next()) {
					dr_followup_disp3 = crs.getString("insurdisposition_name");
					dr_followup_disp3_mandatory = crs.getString("insurdisposition_mandatory");
					if (!dr_followup_disp_nextfollowup.equals("1")) {
						dr_followup_disp_nextfollowup = crs.getString("insurdisposition_nextfollowup");
					}
				}
				crs.close();
			} else {
				if (dr_followup_disp2_mandatory.equals("1") && dr_followup_disp3_id.equals("0")) {
					insur_msg += "<font color='red'><b><br> Select child value for " + dr_followup_disp2 + "!</b></font>";
				}
			}

			if (!dr_followup_disp4_id.equals("0")) {
				Strsql = "SELECT insurdisposition_name, insurdisposition_mandatory,"
						+ " insurdisposition_nextfollowup "
						+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
						+ " WHERE insurdisposition_id = " + dr_followup_disp4_id;
				crs = processQuery(Strsql);
				while (crs.next()) {
					dr_followup_disp4 = crs.getString("insurdisposition_name");
					dr_followup_disp4_mandatory = crs.getString("insurdisposition_mandatory");
					if (!dr_followup_disp_nextfollowup.equals("1")) {
						dr_followup_disp_nextfollowup = crs.getString("insurdisposition_nextfollowup");
					}
				}
				crs.close();
			} else {
				if (dr_followup_disp3_mandatory.equals("1") && dr_followup_disp4_id.equals("0")) {
					insur_msg += "<font color='red'><b><br> Select child value for " + dr_followup_disp3 + "!</b></font>";
				}
			}
			if (!dr_followup_disp5_id.equals("0")) {
				Strsql = "SELECT insurdisposition_name, insurdisposition_mandatory,"
						+ " insurdisposition_nextfollowup"
						+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
						+ " WHERE insurdisposition_id = " + dr_followup_disp5_id;
				crs = processQuery(Strsql);
				while (crs.next()) {
					dr_followup_disp5 = crs.getString("insurdisposition_name");
					dr_followup_disp4_mandatory = crs.getString("insurdisposition_mandatory");
					if (!dr_followup_disp_nextfollowup.equals("1")) {
						dr_followup_disp_nextfollowup = crs.getString("insurdisposition_nextfollowup");
					}
				}
				crs.close();
			} else {
				if (dr_followup_disp4_mandatory.equals("1") && dr_followup_disp5_id.equals("0")) {
					insur_msg += "<font color='red'><b><br> Select child value for " + dr_followup_disp4 + "!</b></font>";
				}
			}

			if (dr_followup_disp_nextfollowup.equals("1")) {
				if (txt_next_followup_time.equals("")) {
					insur_msg += "<font color='red'><b><br>Select Next-Follow-up Time!</b></font>";
				} else if (!txt_next_followup_time.equals("")) {
					if (Long.parseLong(txt_next_followup_time) <= Long.parseLong(ToLongDate(kknow()))) {
						insur_msg = insur_msg + " <br> Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
					} else {
						String followuptime = ExecuteQuery("SELECT insurenquiryfollowup_followup_time "
								+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup "
								+ " WHERE insurenquiryfollowup_disp1 =''"
								+ " AND insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id
								+ " ORDER BY insurenquiryfollowup_followup_time DESC");
						if (!followuptime.equals("") && !txt_next_followup_time.equals("")) {
							if (Long.parseLong(txt_next_followup_time) < Long.parseLong(followuptime)) {
								insur_msg = insur_msg + " <br> Follow-up time should be greater than previous follow-up time!";
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return insur_msg;
	}
	public String AddInsuranceFollowup(String comp_id, HttpServletRequest request) {
		GetValues(request);
		insur_msg = FollowupFieldValidate(comp_id);
		try {
			if (insur_msg.equals("")) {
				StrSql = "SELECT insurenquiryfollowup_id FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
						+ " WHERE 1=1"
						+ " AND insurenquiryfollowup_disp1 = ''"
						+ " AND insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id;
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					StrSql = " UPDATE " + compdb(comp_id) + "axela_insurance_enquiry_followup"
							+ " SET insurenquiryfollowup_disp1 = '" + dr_followup_disp1 + "',"
							+ " insurenquiryfollowup_disp2 = '" + dr_followup_disp2 + "',"
							+ " insurenquiryfollowup_disp3 = '" + dr_followup_disp3 + "',"
							+ " insurenquiryfollowup_disp4 = '" + dr_followup_disp4 + "',"
							+ " insurenquiryfollowup_disp5 = '" + dr_followup_disp5 + "',"
							+ " insurenquiryfollowup_comments = '" + txt_followup_comment + "',"
							+ " insurenquiryfollowup_nextfollowup_time = '" + txt_next_followup_time + "',"
							+ " insurenquiryfollowup_modified_id = '" + emp_id + "',"
							+ " insurenquiryfollowup_modified_time = '" + ToLongDate(kknow()) + "'"
							+ " WHERE insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id
							+ " AND insurenquiryfollowup_disp1=''";
					// SOP("StrSql==111==" + StrSql);
					updateQuery(StrSql);
				} else {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
							+ " (insurenquiryfollowup_insurenquiry_id,"
							+ " insurenquiryfollowup_followup_time,"
							+ " insurenquiryfollowup_disp1,"
							+ " insurenquiryfollowup_disp2,"
							+ " insurenquiryfollowup_disp3,"
							+ " insurenquiryfollowup_disp4,"
							+ " insurenquiryfollowup_disp5,"
							+ " insurenquiryfollowup_comments,"
							+ " insurenquiryfollowup_nextfollowup_time,"
							+ " insurenquiryfollowup_entry_id,"
							+ " insurenquiryfollowup_entry_time)"
							+ " VALUES"
							+ " (" + insurenquiry_id + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + dr_followup_disp1 + "',"
							+ " '" + dr_followup_disp2 + "',"
							+ " '" + dr_followup_disp3 + "',"
							+ " '" + dr_followup_disp4 + "',"
							+ " '" + dr_followup_disp5 + "',"
							+ " '" + txt_followup_comment + "',"
							+ " '" + txt_next_followup_time + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("Insert insur==222==" + StrSql);
					updateQuery(StrSql);
				}
				if (!txt_next_followup_time.equals("")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
							+ " (insurenquiryfollowup_insurenquiry_id,"
							+ " insurenquiryfollowup_followup_time,"
							+ " insurenquiryfollowup_entry_id,"
							+ " insurenquiryfollowup_entry_time)"
							+ " VALUES"
							+ " (" + insurenquiry_id + ","
							+ " '" + txt_next_followup_time + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "')";
					updateQuery(StrSql);
					// SOP("Insert insur==333==" + StrSql);
				}

				// response.sendRedirect(response.encodeRedirectURL("../insurance/insurance-enquiry-dash.jsp?insurenquiry_id=" + insurenquiry_id + "#tabs-2"));
				return "Follow-up added successfully!";
			} else {
				return "<font color='red'><b>Error!</b></font>" + insur_msg;
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
}
