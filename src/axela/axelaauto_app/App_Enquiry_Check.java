package axela.axelaauto_app;
///// divya
// modified by sn 6, 7 may 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Enquiry_Check extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String lead_id = "0";
	public String contact_mobile = "";
	public String contact_email = "";
	public String team_id = "0";
	public String enquiry_branch_id = "0";
	public String enquiry_model_id = "0";
	public String update = "";
	public String campaign = "";
	public String quote_model_id = "0";
	public String executive = "";
	public String team = "", lostcaseid1 = "0", lostcaseid2 = "0";
	public String enquiry_date = "";
	public String comp_id = "0";
	// public String enquiry_preownedmodel_id = "0";
	public String model = "", vehquote = "";
	public String BranchAccess = "", ExeAccess = "";
	public String city_id = "0", model_id = "0", city = "", img = "", enquiry_type_id = "0";
	public String region = "", brand_id = "";
	public String lostcase2 = "", lostcase3 = "";
	public String crm_lostcase1_id = "", crm_lostcase2_id = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		comp_id = CNumeric(GetSession("comp_id", request));
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		contact_mobile = PadQuotes(request.getParameter("contact_mobile"));
		contact_email = PadQuotes(request.getParameter("contact_email"));
		team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
		enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("enquiry_branch_id")));
		team = PadQuotes(request.getParameter("team"));
		executive = PadQuotes(request.getParameter("executive"));
		campaign = PadQuotes(request.getParameter("campaign"));
		model = PadQuotes(request.getParameter("model"));
		enquiry_date = PadQuotes(request.getParameter("enquiry_date"));
		// enquiry_model_id = CNumeric(PadQuotes(request.getParameter("enquiry_model_id")));
		// enquiry_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("enquiry_preownedmodel_id")));
		update = PadQuotes(request.getParameter("update"));
		city = PadQuotes(request.getParameter("city"));
		city_id = PadQuotes(request.getParameter("city_id"));
		enquiry_model_id = CNumeric(PadQuotes(request.getParameter("enquiry_model_id")));
		img = PadQuotes(request.getParameter("image"));
		enquiry_type_id = CNumeric(PadQuotes(request.getParameter("type_id")));
		lostcaseid1 = CNumeric(PadQuotes(request.getParameter("caseid1")));
		lostcaseid2 = CNumeric(PadQuotes(request.getParameter("caseid2")));
		lostcase2 = PadQuotes(request.getParameter("lostcase2"));
		crm_lostcase1_id = CNumeric(PadQuotes(request.getParameter("crm_lostcase1_id")));

		lostcase3 = PadQuotes(request.getParameter("lostcase3"));
		crm_lostcase2_id = CNumeric(PadQuotes(request.getParameter("crm_lostcase2_id")));
		quote_model_id = CNumeric(PadQuotes(request.getParameter("quote_model_id")));
		region = PadQuotes(request.getParameter("region"));
		brand_id = PadQuotes(request.getParameter("brand_id"));
		// brand_id = CleanArrVal(brand_id);
		vehquote = request.getParameter("vehquote") + "";
		// // // demo = PadQuotes(request.getParameter("demo"));
		App_Enquiry_Add enquiry = new App_Enquiry_Add();
		if (!contact_mobile.equals("") && contact_mobile.length() == 13) {
			StrHTML = SearchContactMobile();
		}
		if (!contact_email.equals("")) {
			StrHTML = SearchContactEmail();
		}
		if (region.equals("yes"))
		{
			StrHTML = PopulateRegion(comp_id, brand_id);
		}

		if (executive.equals("yes")) {
			StrHTML = new App_Enquiry_Add().PopulateSalesExecutives(enquiry_branch_id, team_id, "", comp_id, request);
		}

		if (lostcase2.equals("yes")) {
			StrHTML = PopulateLostCase2(comp_id, "0", crm_lostcase1_id);
		}

		if (lostcase3.equals("yes")) {
			StrHTML = PopulateLostCase3(comp_id, "0", crm_lostcase2_id);
		}

		if (!quote_model_id.equals("0") && vehquote.equals("yes")) {
			App_Veh_Quote_Add quote = new App_Veh_Quote_Add();
			StrHTML = quote.PopulateItem(quote_model_id, comp_id);
			quote = null;
		}

		if (!enquiry_model_id.equals("0")) {
			StrHTML = PopulateItem();
		}

		// if (!enquiry_preownedmodel_id.equals("0")) {
		// StrHTML = PopulateVariant();
		// }
		if (!lostcaseid1.equals("0")) {
			StrHTML = LostCaste2();
		}

		if (!lostcaseid2.equals("0")) {
			StrHTML = LostCaste3();
		}

		if (!city_id.equals("") && city.equals("yes")) {
			// Account_Update account = new Account_Update();
			// StrHTML = new Customer_Update().PopulateZone(city_id);
		}
		if (img.equals("yes") && !enquiry_model_id.equals("0") && !enquiry_type_id.equals("0")) {
			App_Share_Images image = new App_Share_Images();
			StrHTML = image.PopulateCategory(enquiry_model_id, enquiry_type_id);
			image = null;
		}
	}

	public String SearchContactMobile() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquiry_id, status_name, emp_name, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " WHERE (contact_mobile1 = '" + contact_mobile + "' OR contact_mobile2 = '" + contact_mobile + "') "
					// + " AND enquiry_branch_id = " + enquiry_branch_id
					+ " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id, "
					+ " contact_id DESC"
					+ " LIMIT 10 ";
			// SOP("StrSql======================" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br>"
							// + "<a href=../sales/app-enquiry-list.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append(">"
							+ "<b>");
					Str.append(crs.getString("emp_name"))
							.append(": ID ").append(crs.getString("enquiry_id")).append(": " + crs.getString("branch_name")).append(" = ")
							.append(crs.getString("status_name")).append("</b>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Axelaauto-App==" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String SearchContactEmail() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquiry_id, status_name, emp_name, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " WHERE (contact_email1 = '" + contact_email
					+ "' OR contact_email2 = '" + contact_email + "') "
					// + " AND enquiry_branch_id = " + enquiry_branch_id
					+ " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id, "
					+ " contact_id DESC"
					+ " LIMIT 10 ";
			// SOP("StrSql==============" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><b>")
							.append(crs.getString("emp_name")).append(": ID ")
							.append(crs.getString("enquiry_id")).append(": " + crs.getString("branch_name")).append(" = ")
							.append(crs.getString("status_name")).append("</b>");
					Str.append("<br>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_type_id = 1"
					+ " AND item_model_id = " + enquiry_model_id + ""
					+ " ORDER BY item_name";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<SELECT name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"form-control\"" + " onChange=\"SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			Str.append("</SELECT>\n");
			crs.close();
			// Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"selectbox\">");
			// Str.append("<option value=0>Select</option>");
			// while (crs.next()) {
			// Str.append("<option value=").append(crs.getString("item_id")).append("");
			// Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			// }
			// crs.close();
			// Str.append("</select>"); ////////
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateVariant() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name" + " FROM axela_preowned_variant"
					+ " WHERE 1=1" + " ORDER BY variant_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=\"dr_enquiry_preownedvariant_id\" id=\"dr_enquiry_preownedvariant_id\" class=\"form-control\" onChange=\"SecurityCheck('dr_enquiry_preownedvariant_id',this,'hint_dr_enquiry_preownedvariant_id');\" >");
			Str.append("<option value=0>select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(">").append(crs.getString("variant_name")).append("</option> \n");
			}
			Str.append("</SELECT>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String LostCaste2() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase2_id, lostcase2_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2" + " WHERE 1=1" + " AND lostcase2_lostcase1_id = " + lostcaseid1
					+ " ORDER BY lostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=\"dr_enquiry_lostcase2_id\" id=\"dr_enquiry_lostcase2_id\" class=\"form-control\"" + " onChange=\"populateCase3()\">\n");

			Str.append("<option value=0>select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id"));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			Str.append("</SELECT>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App====" + this.getClass().getName());
			SOPError("Axelaauto-App====" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();

	}

	public String LostCaste3() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3" + " WHERE 1=1" + " AND lostcase3_lostcase2_id = " + lostcaseid2
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=\"dr_enquiry_lostcase3_id\" id=\"dr_enquiry_lostcase3_id\" class=\"form-control\"" + " onChange=\"StatusUpdate()\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id"));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();

	}

	public String PopulateTeamExecutives(String branch_id, String team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 and emp_sales='1'"
					+ " AND emp_active='1' AND (emp_branch_id = " + branch_id
					+ " OR emp_id = 1 "
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id "
					+ " AND empbr.emp_branch_id=" + branch_id + "))";
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id=" + team_id + ")";
			}
			StrSql = StrSql + " GROUP BY emp_id " + " ORDER BY emp_name ";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_emp_id\" id=\"dr_enquiry_emp_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), ""));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulateRegion(String comp_id, String brand_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name"
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id"
					+ " ORDER BY region_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class=\"row\" id=\"regionHint\">")
					.append("<table class=\"table\" id=\"one\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Region").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallregion_id\" style=\"position:relative; top:3px\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("region_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input type=\"checkbox\" class=\"caseregion_id icheck\" name=\"chk_region_id\" value=\"").append(crs.getString("region_id")).append("\"></td>\n")
						// .append("<input type=\"checkbox\" class=\"caseregion_id icheck\" name=\"chk_region_id\" value=\"" + crs.getString("region_id") + " \">").append("</td>\n")
						.append("</tr>\n");

			}

			Str.append("</table>\n")
					.append("</div>\n");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2(String comp_id, String lostcase2_id, String lostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// SOP("lostcase2_id====" + lostcase2_id);
			StrSql = "SELECT lostcase2_id, lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_lostcase1_id = " + CNumeric(lostcase1_id)
					+ " ORDER BY lostcase2_name";
			// SOP("strsql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_crm_lostcase2_id\" id=\"dr_crm_lostcase2_id\" class=\"form-control\" onchange=\"populateLostCase3()\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3(String comp_id, String lostcase3_id, String lostcase2_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_lostcase2_id = " + CNumeric(lostcase2_id)
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_crm_lostcase3_id\" id=\"dr_crm_lostcase3_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
