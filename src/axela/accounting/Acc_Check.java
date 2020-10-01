package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Acc_Check extends Connect {

	public String branch_id = "", exe_id = "", crmtype_id = "", region_id = "";
	public String[] region_ids, branch_ids, team_ids, exe_ids, model_ids, item_ids, crmdays_ids, crmconcern_ids;
	public String exe_branch_id = "";
	public String StrHTML = "", ExeAccess = "";
	public String multiple = "", history = "", single = "";
	public String brand_id = "", model_id = "";
	public String region = "", branches = "", mulbranch = "";
	public String branch = "";
	public String team = "";
	public String model = "", item = "";
	public String executives = "", branchexecutives = "";
	public String team_id = "";
	// public String traffic = "", traffic_id = "";
	public String cat_id = "";
	public String comp_id = "0";
	public String soe = "";
	public String crmfollowup = "";
	public String crm_days_id = "0";
	public String crmfollowupdays = "", crmdays = "", crmdays1 = "", crmdaysfollowup = "", esc = "", crmconcern = "";
	public String StrSql = "", exe = "", crmdaysfollowupsingle = "";
	public String crmdays_id = "0";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {

			brand_id = PadQuotes(request.getParameter("brand_id"));
			region_id = PadQuotes(request.getParameter("region_id"));
			model_id = PadQuotes(request.getParameter("model_id"));
			brand_id = CleanArrVal(brand_id);
			model_id = CleanArrVal(model_id);
			region_id = CleanArrVal(region_id);
			branch_id = PadQuotes(request.getParameter("branch_id"));
			crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
			if (branch_id.length() > 1) {
				branch_id = CleanArrVal(branch_id);
			}
			team_id = PadQuotes(request.getParameter("team_id"));
			if (team_id.length() > 1) {
				team_id = CleanArrVal(team_id);
			}
			exe_id = PadQuotes(request.getParameter("exe_id"));
			if (exe_id.length() > 1) {
				exe_id = CleanArrVal(exe_id);
			}
			region = PadQuotes(request.getParameter("region"));
			// branches = PadQuotes(request.getParameter("branches")); ///s
			branch = PadQuotes(request.getParameter("branch"));
			mulbranch = PadQuotes(request.getParameter("mulbranch"));
			team = PadQuotes(request.getParameter("team"));
			executives = PadQuotes(request.getParameter("executives"));
			model = PadQuotes(request.getParameter("model"));
			model_ids = request.getParameterValues("model_id");
			item = PadQuotes(request.getParameter("item"));
			item_ids = request.getParameterValues("item_id");
			cat_id = PadQuotes(request.getParameter("cat_id"));
			ExeAccess = GetSession("ExeAccess", request);
			crmtype_id = CNumeric(PadQuotes(request.getParameter("crmtype_id")));
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			if (exe_branch_id.length() > 1) {
				exe_branch_id = CleanArrVal(exe_branch_id);
			}
			multiple = PadQuotes(request.getParameter("multiple")).trim();
			single = PadQuotes(request.getParameter("single")).trim();
			history = PadQuotes(request.getParameter("history"));
			soe = PadQuotes(request.getParameter("soe"));
			crmfollowup = PadQuotes(request.getParameter("crmfollowup"));
			crmfollowupdays = PadQuotes(request.getParameter("crmfollowupdays"));
			crmdays = PadQuotes(request.getParameter("crmdays"));
			crmconcern = PadQuotes(request.getParameter("crmconcern"));
			crmdaysfollowup = PadQuotes(request.getParameter("crmdaysfollowup"));
			crmdaysfollowupsingle = PadQuotes(request.getParameter("crmdaysfollowupsingle"));
			esc = PadQuotes(request.getParameter("esc"));

			GetValues(request, response);
			// SOP("crmdaysfollowup-------------------" + crmdaysfollowup);
			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}

			if (branch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (mulbranch.equals("yes")) {
				StrHTML = PopulateBranchesld(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (team.equals("yes")) {
				StrHTML = PopulateTeams(branch_id, team_ids, comp_id, request);
			}

			if (model.equals("yes")) {
				StrHTML = PopulateModels(brand_id, model_ids, comp_id, request);
			}
			if (item.equals("yes")) {
				StrHTML = PopulateVariants(model_id, item_ids, comp_id, request);
			}
			if (executives.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}
			if (branchexecutives.equals("yes")) {
				StrHTML = PopulateBranchExecutives(exe_branch_id, exe_ids, comp_id, request);
			}

			if (!cat_id.equals("")) {
				StrHTML = PopulateItems();
			}
			if (crmdaysfollowup.equals("yes") && esc.equals("yes")) {
				// SOP("exe_branch_id--------------" + exe_branch_id);
				// SOP("crmtype_id--------------" + crmtype_id);
				StrHTML = PopulateCRMDays1(comp_id, crmdays_id, crmtype_id, exe_branch_id);
			}
			if (crmdays.equals("yes")) {
				StrHTML = PopulateCRMDays(comp_id, crmdays_ids, crmtype_id, branch_id);
			}
			if (crmconcern.equals("yes")) {
				StrHTML = PopulateConcern(comp_id, crmconcern_ids, crmtype_id);
			}
			if (!exe_branch_id.equals("") && !crmdays.equals("yes") && !crmdaysfollowup.equals("yes") && !crmdaysfollowupsingle.equals("yes") && !team.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}

			if (!exe_branch_id.equals("") && history.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}

			if (model.equals("manageprincipalsupport")) {
				brand_id = PadQuotes(request.getParameter("brand_id"));
				StrHTML = new ManagePrincipalSupport_Update().PopulateModel(model_id, brand_id, comp_id);
			}

			if (soe.equals("yes")) {
				StrHTML = PopulateSoe();
			}
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		crm_days_id = CNumeric(PadQuotes(request.getParameter("dr_crmfollowupdays")));
		crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select soe_id, soe_name  " + " from " + compdb(comp_id)
					+ "axela_soe " + " inner JOIN " + compdb(comp_id)
					+ "axela_sales_enquiry on enquiry_soe_id = soe_id "
					+ " where 1=1 and enquiry_branch_id=" + exe_branch_id + " "
					+ " group by soe_id " + " order by soe_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_soe\" id=\"dr_soe\" class=\"textbox\" multiple=\"multiple\" size=\"10\" style=\"width:250px\" "
					+ "  >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"))
						.append("");
				Str.append(">").append(crs.getString("soe_name"))
						.append("</option> \n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select item_id, concat(item_name,' (',item_code,')') as item_name"
					+ " from "
					+ compdb(comp_id)
					+ "axela_inventory_item "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_inventory_salescat_trans on trans_item_id=item_id "
					+ " left join "
					+ compdb(comp_id)
					+ "axela_inventory_salescat on salescat_id=trans_salescat_id "
					+ " where item_active = '1'";

			if (!cat_id.equals("")) {
				if (cat_id.endsWith(",")) {
					cat_id = cat_id.substring(0, cat_id.length() - 1);
				}
				StrSql = StrSql + " and salescat_id in (" + cat_id + ")";
			}
			StrSql = StrSql + " order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_item id=dr_item class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"))
						.append(" >").append(crs.getString("item_name"))
						.append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("PopulatePrincipal query======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("brand_id"), brand_ids));
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

	public String PopulateRegion(String brand_id, String[] region_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1=1 AND branch_active = 1  "
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateBranches();\"  style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("region_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("region_id"), region_ids));
					Str.append(">").append(crs.getString("region_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("branch_ids-----///----------" + branch_ids);
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class='form-control' multiple='multiple' size=10 onchange=\"PopulateExecutives();PopulateTeams();PopulateCRMDays();\" style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
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

	public String PopulateBranchesld(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("branch_ids-----///----------" + branch_ids);
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranchesld==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple='multiple' size=10 >");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
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

	// onchange of Branch PopulateExecutives
	public String PopulateBranch(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("branch_ids-----///----------" + branch_ids);
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateExecutives();\" style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
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

	public String PopulateModels(String brand_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";

		try {
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1=1 "
					+ " AND model_active = '1'"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-------PopulateModels------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_model size=10 multiple=multiple class=form-control id=dr_model style=\"padding:10px\""
					+ " onChange=\"PopulateVariants();\" >\n>";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("model_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("model_id"), model_ids);
					stringval = stringval + ">" + crs.getString("model_name") + "</option>\n";
				}
			}
			stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

	public String PopulateVariants(String model_id, String[] item_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";
		try {
			StrSql = "SELECT item_id, item_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1=1 "
					+ " AND model_sales=1"
					+ " AND model_active = '1'"
					+ " AND item_active = '1'"
					+ " AND item_type_id = '1'"
					+ BranchAccess;
			if (!model_id.equals("")) {
				StrSql += " AND item_model_id in (" + model_id + ") ";
			}
			StrSql += " GROUP BY item_id"
					+ " ORDER BY model_name, item_name";
			// SOP("StrSql-------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_variant size=10 multiple=multiple class=form-control id=dr_variant style=\"padding:10px\">";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("item_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("item_id"), item_ids);
					stringval = stringval + ">" + crs.getString("item_name") + "</option>\n";
				}
			}
			stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

	public String PopulateTeams(String branch_id, String[] team_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		if (branch_id.equals("")) {
			branch_id = "0";
		}
		try {
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where 1=1 ";
			if (!branch_id.equals("0")) {
				StrSql += " AND team_branch_id in (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "team_branch_id")
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query in check team check ==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_team class=form-control id=dr_team onchange=PopulateExecutives();><option value = 0>Select</option>\n");

			} else {
				Str.append("<select name=dr_team size=10 id=dr_team class=form-control multiple=multiple "
						+ " onChange=\"PopulateExecutives();\" style=\"padding:10px\">\n");
			}
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("team_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
					Str.append(">").append(crs.getString("team_name")).append("</option> \n");
				}
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranchExecutives(String branch_id, String[] exe_ids, String comp_id, HttpServletRequest request) {

		String ExeAccess = GetSession("ExeAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = '1' "
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " and emp_branch_id in (" + exe_branch_id + ")";
			}

			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql==ccccc== in PopulateSalesExecutive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=form-control ><option value = 0>Select</option>\n");
			} else {
				Str.append("<select name=dr_executive id=dr_executive class=form-control multiple=\"multiple\" size=10 style=\"padding:10px\">");
			}
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateSalesExecutives(String team_id, String[] exe_ids, String comp_id, HttpServletRequest request) {

		if (team_id.equals("")) {
			team_id = "0";
		}
		String ExeAccess = GetSession("ExeAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = '1' "
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " and emp_branch_id in (" + exe_branch_id + ")";
			}

			if (!team_id.equals("0")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql==ccccc== in PopulateSalesExecutive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=form-control ><option value = 0>Select</option>\n");
			} else {
				Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"padding:10px\">");
			}
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateCRMDays(String comp_id, String[] crmdays_ids, String crmdays_crmtype_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("exe_branch_id--------" + exe_branch_id);
			StrSql = "SELECT crmdays_id, concat(crmdays_daycount, crmdays_desc) as crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1=1 ";
			if (!exe_branch_id.equals("") && !exe_branch_id.equals("0")) {
				StrSql += " AND branch_id IN ( " + exe_branch_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";
			// SOP("StrSql------check-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_crmdays_id name=dr_crmdays_id multiple=multiple class=form-control size=10>");
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmdays_id")).append("");
				// Str.append(crs.getString("crmdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("crmdays_id"), crmdays_ids));
				Str.append(">").append(crs.getString("crmdays_desc")).append("</option> \n");
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

	public String PopulateConcern(String comp_id, String[] crmconcern_ids, String crmdays_crmtype_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmconcern_id, crmconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_concern"
					+ " WHERE 1=1 ";
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmconcern_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmconcern_id"
					+ " ORDER BY crmconcern_desc";
			// SOP("StrSql------PopulateConcern-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_crmconcern_id name=dr_crmconcern_id multiple=multiple class=form-control size=10>");
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmconcern_id")).append("");
				// Str.append(crs.getString("crmdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("crmconcern_id"), crmconcern_ids));
				Str.append(">").append(crs.getString("crmconcern_desc")).append("</option> \n");
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

	public String PopulateCRMDays1(String comp_id, String crmdays_id, String crmdays_crmtype_id, String exe_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmdays_id, concat(crmdays_daycount, crmdays_desc) as crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1 = 1";
			if (!exe_branch_id.equals("0") && !exe_branch_id.equals("")) {
				StrSql += " AND branch_id IN  (" + exe_branch_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";
			// SOP("StrSql-----end------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_crmdays_id1\" name=\"dr_crmdays_id1\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmdays_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmdays_id"), crmdays_id));
				Str.append(">").append(crs.getString("crmdays_desc")).append("</option> \n");
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
