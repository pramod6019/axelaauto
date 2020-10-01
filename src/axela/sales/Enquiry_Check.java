package axela.sales;

// divya
// modified by sn 6, 7 may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Check extends Connect {

	public String StrSql = "";

	public String StrSearch = "";
	public String StrHTML = "";
	public String contact_id = "0";
	public String comp_id = "0";
	public String contact_mobile = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String team_id = "0";
	public String active = "";
	public String enquiry_branch_id = "0";
	public String enquiry_model_id = "0";
	public String update = "";
	public String executive = "", team = "", exe_transfer = "", model = "";
	public String item = "", enquiry = "";
	public String lostcase2 = "";
	public String crm_lostcase1_id = "";
	public String lostcase3 = "";
	public String crm_lostcase2_id = "";
	public String dr_enquiry_sob_id = "";
	public String enquiry_sob_id = "0";
	public String branch_id = "0";
	public String model_id = "0";
	public String enquiry_team_id = "0";
	public String enquiry_emp_id = "0";
	public String brand_id = "0";
	public String BranchAccess = "";
	public String source_of_enquiry = "";
	// public String enquiry_preownedmodel_id = "";

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
			contact_mobile = PadQuotes(request.getParameter("contact_mobile"));
			contact_phone = PadQuotes(request.getParameter("contact_phone"));
			contact_email = PadQuotes(request.getParameter("contact_email"));
			team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
			active = CNumeric(PadQuotes(request.getParameter("active")));
			enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("enquiry_branch_id")));
			executive = PadQuotes(request.getParameter("executive"));
			team = PadQuotes(request.getParameter("team"));
			item = PadQuotes(request.getParameter("item"));
			enquiry = PadQuotes(request.getParameter("enquiry"));
			enquiry_model_id = CNumeric(PadQuotes(request.getParameter("enquiry_model_id")));
			dr_enquiry_sob_id = PadQuotes(request.getParameter("dr_enquiry_sob_id"));
			// enquiry_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("enquiry_preownedmodel_id")));
			lostcase2 = PadQuotes(request.getParameter("lostcase2"));
			crm_lostcase1_id = CNumeric(PadQuotes(request.getParameter("crm_lostcase1_id")));

			lostcase3 = PadQuotes(request.getParameter("lostcase3"));
			crm_lostcase2_id = CNumeric(PadQuotes(request.getParameter("crm_lostcase2_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
			exe_transfer = PadQuotes(request.getParameter("exe_transfer"));
			model = PadQuotes(request.getParameter("model"));
			update = PadQuotes(request.getParameter("update"));

			source_of_enquiry = PadQuotes(request.getParameter("source_of_enquiry"));

			// SOP("dr_enquiry_sob_id===" + dr_enquiry_sob_id);
			if (dr_enquiry_sob_id.equals("yes")) {
				String enquiry_soe_id = CNumeric(PadQuotes(request.getParameter("enquiry_soe_id")));
				// SOP("s1====" + s1);
				StrHTML = PopulateSob(enquiry_soe_id, comp_id);
			}

			if (!contact_mobile.equals("") && contact_mobile.length() == 13) {
				// SOP("contact_mobile=====" + contact_mobile);
				StrHTML = SearchContactMobile();
			}
			// SOP("contact_phone--" + contact_phone);
			if (!contact_phone.equals("") && contact_phone.length() == 14) {
				// SOP("contact_phone--" + contact_phone);
				StrHTML = SearchContactPhone();
			}
			if (!contact_email.equals("")) {
				StrHTML = SearchContactEmail();
			}

			if (executive.equals("yes")) {
				StrHTML = new Enquiry_Quickadd().PopulateSalesExecutives(enquiry_branch_id, team_id, "", active, comp_id, request);
			}
			if (executive.equals("exe")) {
				enquiry_branch_id = PadQuotes(request.getParameter("dr_branch"));
				StrHTML = new Enquiry_List().PopulateExecutive(enquiry_branch_id, comp_id);
			}
			if (source_of_enquiry.equals("yes")) {
				enquiry_emp_id = PadQuotes(request.getParameter("dr_enquiry_emp_id"));
				StrHTML = new Enquiry_Quickadd().PopulateSoe(comp_id, enquiry_emp_id);
			}
			if (item.equals("yes")) {
				StrHTML = PopulateItem();
			}
			if (item.equals("yes") && enquiry.equals("yes")) {
				// SOP("11111111");
				StrHTML = new Enquiry_Quickadd().PopulateItem(enquiry_model_id, comp_id);
			}
			if (lostcase2.equals("yes")) {
				// SOP("inside");
				StrHTML = new CRM_Update().PopulateLostCase2(comp_id, "0", crm_lostcase1_id);
			}
			if (lostcase3.equals("yes")) {
				StrHTML = new CRM_Update().PopulateLostCase3(comp_id, "0", crm_lostcase2_id);
			}

			if (!brand_id.equals("0")) {
				StrHTML = new Enquiry_Transfer_Check().PopulateBranch(brand_id, comp_id);
			}
			if (!brand_id.equals("0") && model.equals("yes")) {
				StrHTML = new Enquiry_Transfer_Check().PopulateModel(brand_id, comp_id);
			}
			if (!model_id.equals("0")) {
				StrHTML = new Enquiry_Transfer_Check().PopulateItem(model_id, comp_id);
			}
			if (!team_id.equals("0") && !exe_transfer.equals("")) {
				StrHTML = new Enquiry_Transfer_Check().PopulateSalesExecutives(team_id, comp_id);
			}
			if (team.equals("yes") && active.equals("0")) {
				StrHTML = new Enquiry_Quickadd().PopulateTeam(enquiry_branch_id, "", comp_id);
			}
			if (team.equals("yes") && !exe_transfer.equals("")) {
				StrHTML = new Enquiry_Transfer_Check().PopulateTeam(branch_id, comp_id);
			}
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
					+ " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id, "
					+ " contact_id DESC"
					+ " LIMIT 10 ";
			// SOP("StrSql------SearchContactMobile---==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("</br><b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><a href=../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("enquiry_id"))
							.append(">" + "<b>").append(crs.getString("emp_name"))
							.append(": ID ").append(crs.getString("enquiry_id")).append(": " + crs.getString("branch_name")).append(" = ")
							.append(crs.getString("status_name")).append("</b></a>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String SearchContactPhone() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT enquiry_id, status_name, emp_name, contact_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " WHERE (contact_phone1 = '" + contact_phone + "'"
					+ " OR contact_phone2 = '" + contact_phone + "')"
					+ " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id, "
					+ " contact_id DESC"
					+ " LIMIT 10 ";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><a href=../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("enquiry_id"))
							.append(">" + "<b>").append(crs.getString("emp_name"))
							.append(": ID ").append(crs.getString("enquiry_id")).append(": " + crs.getString("branch_name")).append(" = ")
							.append(crs.getString("status_name")).append("</b></a>");
					// Str.append("<br><a href=../sales/enquiry-quickadd.jsp?contact_id=").append(crs.getString("contact_id")).append(">"
					// +
					// "<b>").append(crs.getString("emp_name")).append(": enquiry ID ").append(crs.getString("enquiry_id")).append(" = ").append(crs.getString("status_name")).append("");
					// Str.append(" (" +
					// crs.getString("branchname")).append(")</b></a>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
					+ " GROUP BY enquiry_id "
					+ " ORDER BY enquiry_id, "
					+ " contact_id DESC"
					+ " LIMIT 10 ";
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><a href=../sales/enquiry-list.jsp?enquiry_id=")
							.append(crs.getString("enquiry_id")).append(">" + "<b>")
							.append(crs.getString("emp_name")).append(": ID ")
							.append(crs.getString("enquiry_id")).append(": " + crs.getString("branch_name")).append(" = ")
							.append(crs.getString("status_name")).append("</b></a>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1"
					+ " AND item_type_id = 1 ";
			if (!update.equals("yes")) {
				StrSql = StrSql + " AND item_active = '1'";
			}
			StrSql = StrSql + " AND item_model_id = " + enquiry_model_id
					+ " ORDER BY item_name";
			// SOP(" PopulateItem---enq check-------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSob(String enquiry_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("enquiry_soe_id==" + enquiry_soe_id);
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + enquiry_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_sob_id\" id=\"dr_enquiry_sob_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), enquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
