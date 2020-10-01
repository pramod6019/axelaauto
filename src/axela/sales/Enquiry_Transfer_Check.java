package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Transfer_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "", successmsg = "", transfer = "";
	public String enquiry_id = "0";
	public String comp_id = "0";
	public String emp_id = "0";
	public String preowned_branch_id = "0";
	public String preowned_id = "0";
	public String enquiry_team_id = "0";
	public String enquiry_model_id = "0";
	public String enquiry_emp_id = "0";
	public String brand_id = "0", branch_id = "0 ", model_id = "0", item_id = "0", team_id = "0", executive_id = "0";
	String BranchAccess = "";
	CachedRowSet crs = null;
	axela.sales.Enquiry_Check check = new axela.sales.Enquiry_Check();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				transfer = PadQuotes(request.getParameter("button"));
				BranchAccess = GetSession("BranchAccess", request);
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
				executive_id = CNumeric(PadQuotes(request.getParameter("executive_id")));

				if (!enquiry_id.equals("0") && !brand_id.equals("0") && !branch_id.equals("0")
						&& !model_id.equals("0") && !item_id.equals("0") && !team_id.equals("0") && !executive_id.equals("0")) {
					if (transfer.equals("Transfer")) {
						PopulateEnquiryFields(response);
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}

	public void PopulateEnquiryFields(HttpServletResponse response) {
		StrHTML = "";
		try {
			StrSql = "SELECT enquiry_branch_id, enquiry_customer_id, enquiry_contact_id,"
					+ "	enquiry_enquirytype_id, enquiry_title, enquiry_desc, enquiry_date, "
					+ " enquiry_model_id, enquiry_item_id, enquiry_close_date, "
					+ " enquiry_value_syscal, enquiry_avpresent, enquiry_manager_assist, "
					+ " enquiry_preownedvariant_id, enquiry_tradein_preownedvariant_id,"
					+ " enquiry_fueltype_id, enquiry_prefreg_id, enquiry_presentcar,"
					+ " enquiry_finance, enquiry_value, enquiry_emp_id, enquiry_team_id,"
					+ " enquiry_stage_id, enquiry_soe_id, enquiry_sob_id, enquiry_campaign_id,"
					+ " enquiry_status_id, enquiry_status_date, enquiry_status_desc,"
					+ " enquiry_priorityenquiry_id, enquiry_notes, enquiry_qcsno,"
					+ " enquiry_dmsno, enquiry_buyertype_id, enquiry_enquirycat_id, "
					+ " enquiry_custtype_id, enquiry_entry_id, enquiry_entry_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " WHERE enquiry_id=" + enquiry_id;
			StrSql += " GROUP BY enquiry_id ";
			// SOP("Dash-----PopulateFields-------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Enquiry_Quickadd enqadd = new Enquiry_Quickadd();
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enqadd.comp_id = comp_id;
					enqadd.emp_id = emp_id;
					enqadd.enquiry_branch_id = branch_id;// crs.getString("enquiry_branch_id");
					enqadd.enquiry_customer_id = crs.getString("enquiry_customer_id");
					enqadd.enquiry_contact_id = crs.getString("enquiry_contact_id");
					enqadd.enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					enqadd.enquiry_title = crs.getString("enquiry_title");
					enqadd.enquiry_desc = crs.getString("enquiry_desc");
					enqadd.enquiry_date = strToShortDate(ToShortDate(kknow()));
					enqadd.enquiry_model_id = model_id; // crs.getString("enquiry_model_id");
					enqadd.enquiry_item_id = item_id;// crs.getString("enquiry_item_id");
					enqadd.enquiry_close_date = strToShortDate(ToShortDate(kknow()));
					enqadd.enquiry_value_syscal = crs.getString("enquiry_value_syscal");
					enqadd.enquiry_avpresent = crs.getString("enquiry_avpresent");
					enqadd.enquiry_manager_assist = crs.getString("enquiry_manager_assist");
					enqadd.enquiry_preownedvariant_id = crs.getString("enquiry_preownedvariant_id");
					enqadd.enquiry_tradein_preownedvariant_id = crs.getString("enquiry_tradein_preownedvariant_id");
					enqadd.enquiry_fueltype_id = crs.getString("enquiry_fueltype_id");
					enqadd.enquiry_prefreg_id = crs.getString("enquiry_prefreg_id");
					enqadd.enquiry_presentcar = crs.getString("enquiry_presentcar");
					enqadd.enquiry_finance = crs.getString("enquiry_finance");
					enqadd.enquiry_budget = crs.getString("enquiry_value");
					enqadd.enquiry_emp_id = executive_id; // crs.getString("enquiry_emp_id");
					enqadd.enquiry_team_id = team_id; // crs.getString("enquiry_team_id");
					enqadd.enquiry_stage_id = crs.getString("enquiry_stage_id");
					enqadd.enquiry_soe_id = crs.getString("enquiry_soe_id");
					enqadd.enquiry_sob_id = crs.getString("enquiry_sob_id");
					enqadd.enquiry_campaign_id = crs.getString("enquiry_campaign_id");
					enqadd.enquiry_status_id = crs.getString("enquiry_status_id");
					enqadd.enquiry_status_date = crs.getString("enquiry_status_date");
					enqadd.enquiry_status_desc = crs.getString("enquiry_status_desc");
					enqadd.enquiry_priorityenquiry_id = crs.getString("enquiry_priorityenquiry_id");
					enqadd.enquiry_notes = crs.getString("enquiry_notes");
					enqadd.enquiry_qcsno = crs.getString("enquiry_qcsno");
					enqadd.enquiry_dmsno = crs.getString("enquiry_dmsno");
					enqadd.enquiry_buyertype_id = crs.getString("enquiry_buyertype_id");
					enqadd.enquiry_enquirycat_id = crs.getString("enquiry_enquirycat_id");
					enqadd.enquiry_custtype_id = crs.getString("enquiry_custtype_id");
					enqadd.enquiry_entry_id = emp_id;
					enqadd.enquiry_entry_date = ToLongDate(kknow());
					enqadd.PopulateConfigDetails();
					enqadd.PopulateContactCustomerDetails(response);
					enqadd.AddEnquiryFields();

					StrHTML = enqadd.msg;
					enquiry_id = enqadd.enquiry_id;
					if (!enquiry_id.equals("0") && StrHTML.equals("")) {
						successmsg = "<font color=\"#ff0000\">Enquiry added successfully!</font>";
						StrHTML += "<br><a href=../sales/enquiry-list.jsp?enquiry_id=" + enquiry_id + ">" + successmsg + "</a>";
					}
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateBrand(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(Selectdrop(crs.getInt("brand_id"), brand_id));
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

	public String PopulateBranch(String brand_id, String comp_id) {
		StringBuilder str = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ "	AND branch_branchtype_id IN (1,2)";
			if (!brand_id.equals("")) {
				SqlStr += "	AND branch_brand_id IN (" + brand_id + ")";
			}
			SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr-----PopulateBranch------" + StrSqlBreaker(SqlStr));
			CachedRowSet crs = processQuery(SqlStr, 0);
			str.append("<select name=dr_enquiry_branch_id id=dr_enquiry_branch_id class=form-control onChange='PopulateTeam();'>");
			str.append("<option value =0>Select</option>");
			while (crs.next()) {
				str.append("<option value=").append(crs.getString("branch_id")).append("");
				str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				str.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			str.append("</select>");
			return str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateModel(String brand_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1 = 1";
			if (!brand_id.equals("0")) {
				StrSql += " AND model_brand_id = " + brand_id;
			}
			StrSql += " AND model_active = 1 "
					+ " AND model_sales = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("SqlStr-----PopulateModel------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_enquiry_model_id id=dr_enquiry_model_id class=form-control  onChange='PopulateItem();'>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1 "
					+ " AND item_type_id = 1 "
					+ " AND item_active = 1"
					+ " AND item_model_id = " + enquiry_model_id
					+ " ORDER BY item_name";
			// SOP(" PopulateItem----/-PopulateItem------" + StrSql);
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + branch_id
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			// SOP("StrSql-------PopulateTeam---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_team_id\" id=\"dr_enquiry_team_id\" class=\"form-control\" onChange=\"PopulateExecutive();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), enquiry_team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSalesExecutives(String team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales = 1";
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id in (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id=" + team_id + ")";
			}
			StrSql += " AND emp_active = 1 "
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("SqlStr----PopulateSalesExecutives-------" + StrSqlBreaker(StrSql));
			Str.append("<select name=\"dr_enquiry_emp_id\" id=\"dr_enquiry_emp_id\" class=\"dropdown form-control\" onChange='PopulateExecutive();''>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), enquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
