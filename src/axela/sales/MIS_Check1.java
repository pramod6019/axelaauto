package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check1 extends Connect {

	public String branch_id = "", exe_id = "", location_id = "", crmtype_id = "", region_id = "";
	public String[] region_ids, branch_ids, location_ids, team_ids, exe_ids, model_ids, item_ids, crmdays_ids, crmconcern_ids, option_ids, soe_ids;
	public String exe_branch_id = "-1";
	public String StrHTML = "", ExeAccess = "";
	public String multiple = "", history = "", single = "";
	public String brand_id = "", model_id = "", option_id = "", soe_id = "";
	public String region = "", branches = "";
	public String branch = "";
	public String team = "";
	public String model = "", location = "", item = "", color = "";
	public String executives = "";
	public String team_id = "";
	// public String traffic = "", traffic_id = "";
	public String cat_id = "";
	public String comp_id = "0";
	public String soe = "", sob = "";
	public String crmfollowup = "";
	public String crm_days_id = "0";
	public String crmfollowupdays = "", crmdays = "", crmdays1 = "", crmdaysfollowup = "", esc = "", crmconcern = "";
	public String StrSql = "", exe = "", crmdaysfollowupsingle = "";
	public String crmdays_id = "0";
	public String include_preowned = "", preownedemp = "";
	public String salesmb = "", salesexecutive = "", salesexecutive_id = "0";
	public String discountbrand = "", discount_brand_id = "0";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {
			ExeAccess = GetSession("ExeAccess", request);
			brand_id = PadQuotes(request.getParameter("brand_id"));
			region_id = PadQuotes(request.getParameter("region_id"));
			region_ids = request.getParameterValues("region_id");
			branch_id = PadQuotes(request.getParameter("branch_id"));
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			team_id = PadQuotes(request.getParameter("team_id"));
			team_ids = request.getParameterValues("team_id");
			exe_id = PadQuotes(request.getParameter("exe_id"));
			exe_ids = request.getParameterValues("exe_id");
			model_id = PadQuotes(request.getParameter("model_id"));
			model_ids = request.getParameterValues("model_id");
			item_ids = request.getParameterValues("item_id");
			crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
			option_ids = request.getParameterValues("option_id");
			cat_id = PadQuotes(request.getParameter("cat_id"));
			crmtype_id = CNumeric(PadQuotes(request.getParameter("crmtype_id")));
			soe_id = PadQuotes(request.getParameter("soe_id"));
			soe_ids = request.getParameterValues("soe_id");
			include_preowned = CNumeric(PadQuotes(request.getParameter("include_preowned")));
			salesmb = PadQuotes(request.getParameter("salesmb"));

			region = PadQuotes(request.getParameter("region"));
			// branches = PadQuotes(request.getParameter("branches")); ///s
			branch = PadQuotes(request.getParameter("branch"));
			team = PadQuotes(request.getParameter("team"));
			executives = PadQuotes(request.getParameter("executives"));
			model = PadQuotes(request.getParameter("model"));
			location = PadQuotes(request.getParameter("location"));
			item = PadQuotes(request.getParameter("item"));
			color = PadQuotes(request.getParameter("color"));
			multiple = PadQuotes(request.getParameter("multiple")).trim();
			single = PadQuotes(request.getParameter("single")).trim();
			history = PadQuotes(request.getParameter("history"));
			soe = PadQuotes(request.getParameter("soe"));
			sob = PadQuotes(request.getParameter("sob"));
			crmfollowup = PadQuotes(request.getParameter("crmfollowup"));
			crmfollowupdays = PadQuotes(request.getParameter("crmfollowupdays"));
			crmdays = PadQuotes(request.getParameter("crmdays"));
			crmconcern = PadQuotes(request.getParameter("crmconcern"));
			crmdaysfollowup = PadQuotes(request.getParameter("crmdaysfollowup"));
			crmdaysfollowupsingle = PadQuotes(request.getParameter("crmdaysfollowupsingle"));
			esc = PadQuotes(request.getParameter("esc"));
			preownedemp = PadQuotes(request.getParameter("preownedemp"));
			salesexecutive = PadQuotes(request.getParameter("salesexecutive"));
			salesexecutive_id = PadQuotes(request.getParameter("dr_sale_executive"));
			discountbrand = PadQuotes(request.getParameter("discountbrand"));
			discount_brand_id = CNumeric(PadQuotes(request.getParameter("discount_brand_id")));
			if (brand_id.length() > 1 && brand_id.contains(",")) {
				brand_id = CleanArrVal(brand_id);
			}
			if (model_id.length() > 1 && model_id.contains(",")) {
				model_id = CleanArrVal(model_id);
			}
			if (region_id.length() > 1 && region_id.contains(",")) {
				region_id = CleanArrVal(region_id);
			}
			if (branch_id.length() > 1 && branch_id.contains(",")) {
				branch_id = CleanArrVal(branch_id);
			}
			if (team_id.length() > 1 && team_id.contains(",")) {
				team_id = CleanArrVal(team_id);
			}
			if (exe_id.length() > 1 && exe_id.contains(",")) {
				exe_id = CleanArrVal(exe_id);
			}
			if (exe_branch_id.length() > 1 && exe_branch_id.contains(",")) {
				exe_branch_id = CleanArrVal(exe_branch_id);
			}
			if (soe_id.length() > 1 && soe_id.contains(",")) {
				soe_id = CleanArrVal(soe_id);
			}

			GetValues(request, response);
			// //SOP("crmdaysfollowup-------------------" + crmdaysfollowup);
			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}
			if (branch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (team.equals("yes")) {
				StrHTML = PopulateTeams(branch_id, team_ids, comp_id, request);
			}
			if (model.equals("yes")) {
				StrHTML = PopulateModels(brand_id, model_ids, comp_id, request);
			}
			if (location.equals("yes")) {
				StrHTML = PopulateLocation(branch_id, comp_id, request);
			}
			if (item.equals("yes")) {
				StrHTML = PopulateVariants(model_id, item_ids, comp_id, request);
			}
			if (color.equals("yes")) {
				StrHTML = PopulateColor(model_id, option_ids, comp_id, request);
			}
			if (executives.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}
			if (!cat_id.equals("")) {
				StrHTML = PopulateItems();
			}
			if (crmdaysfollowup.equals("yes") && esc.equals("yes")) {
				StrHTML = PopulateCRMDays1(comp_id, crmdays_id, crmtype_id, branch_id, brand_id);
			}
			if (crmdays.equals("yes")) {
				StrHTML = PopulateCRMDays(comp_id, crmdays_ids, crmtype_id, brand_id);
			}
			if (crmconcern.equals("yes")) {
				StrHTML = PopulateConcern(comp_id, crmconcern_ids, crmtype_id);
			}
			if (crmdaysfollowupsingle.equals("yes")) {
				if (brand_id.equals("0") || brand_id.equals("")) {
					if (!branch_id.equals("0") && !branch_id.equals("")) {
						brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id =" + branch_id));
					}
				}
				StrHTML = new Report_CRMFollowup().PopulateCRMDays(comp_id, crmtype_id, crmdays_id, branch_id, brand_id);
			}

			if (crmdaysfollowupsingle.equals("multi")) {
				if (brand_id.equals("0") || brand_id.equals("")) {
					if (!branch_id.equals("0") && !branch_id.equals("")) {
						brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id =" + branch_id));
					}
				}
				StrHTML = new Report_CRMFollowup().PopulateCRMDaysMulti(comp_id, crmtype_id, crmdays_ids, branch_id, brand_id);
			}
			if (region.equals("crmfollowupregion")) {
				StrHTML = new Report_CRMFollowup().PopulateRegion(brand_id, region_id, comp_id, request);
			}
			if (branch.equals("crmfollowupbranch")) {
				StrHTML = new Report_CRMFollowup().PopulateBranches(brand_id, region_id, branch_id, comp_id, request);
			}
			if (discountbrand.equals("yes") && !discount_brand_id.equals("0")) {
				StrHTML = new Update_Discount().PopulateModel(discount_brand_id, comp_id);
			}
			if (branch.equals("dmsdetails")) {
				StrHTML = new Report_DMS_Details().PopulateBranches(brand_id, region_id, branch_id, comp_id, request);
			}
			// commented by gopal(PopulateSalesExecutives was calling two times from all report page)

			// if (!exe_branch_id.equals("") && !crmdays.equals("yes") && !crmdaysfollowup.equals("yes") && !crmdaysfollowupsingle.equals("yes") && !team.equals("yes")) {
			// //SOP("coming...2");
			// StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			// }
			// SOP("exe_branch_id==" + exe_branch_id);
			if (executives.equals("crmfollowupexe")) {
				// SOP("team_id=====" + team_id);
				StrHTML = new Report_CRMFollowup().PopulateSalesExecutives(exe_branch_id, team_id, comp_id);
			}

			if (!exe_branch_id.equals("") && history.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}
			if (soe.equals("yes")) {
				StrHTML = PopulateSoe();
			}
			if (sob.equals("yes")) {
				StrHTML = new Report_Monitoring_Board().PopulateSob(soe_id, comp_id, request);
			}

			if (salesmb.equals("yes")) {
				StrHTML = new Report_Monitoring_Board().PopulateOrderBy(comp_id, include_preowned);
			}

			if (preownedemp.equals("yes") && !branch_id.equals("")) {
				StrHTML = new Team_Update().PopulatePreownedExecutives(comp_id, branch_id);
			}

			if (salesexecutive.equals("yes")) {
				StrHTML = PopulateEmp(team_id, salesexecutive_id, comp_id, request);
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
			StrSql = "SELECT soe_id, soe_name  "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_soe_id = soe_id "
					+ " WHERE 1 = 1"
					+ " AND enquiry_branch_id = " + exe_branch_id + " "
					+ " GROUP BY soe_id "
					+ " ORDER BY soe_name ";
			// //SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_soe\" id=\"dr_soe\" class=\"textbox\" multiple=\"multiple\" size=\"10\" style=\"width:250px\">\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, concat(item_name,' (',item_code,')') AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id=item_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_salescat ON salescat_id=trans_salescat_id "
					+ " WHERE item_active = '1'";

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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id IN (1, 2) "
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("StrSql======" + StrSql);
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

	public String PopulateCategory(String id[], String comp_id, String cat_id, String active, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "", BranchAccess = "";
		BranchAccess = GetSession("BranchAccess", request);
		try {
			StrSql = "SELECT cat_id, cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_cat_id = cat_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1";

			if (!active.equals("")) {
				StrSql = StrSql + " AND cat_active = " + active + "";
			}
			if (!cat_id.equals("")) {
				StrSql = StrSql + " AND cat_id != " + cat_id + "";
			}

			StrSql += BranchAccess + " GROUP BY cat_id" + " ORDER BY cat_rank, cat_id";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_cat id=dr_cat class='form-control multiselect-dropdown' multiple=multiple size=10 \"  style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(ArrSelectdrop(Integer.parseInt(crs.getString("cat_id")), id));
				Str.append(">").append(crs.getString("cat_name"));
				Str.append(":</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
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
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// //SOP("StrSql------PopulateRegion-----" + StrSql);
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
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
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
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateExecutives();PopulateTeams();PopulateCRMDays();\" >");
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
					+ " WHERE 1 = 1 "
					+ " AND model_sales = 1"
					+ " AND model_active = '1'"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-------PopulateModels------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name='dr_model' size=10 multiple=multiple class='form-control multiselect-dropdown' id='dr_model' style=\"padding:10px\""
					+ " onChange=\"PopulateVariants();PopulateColor();\" >\n>";
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
					+ " WHERE 1 = 1 "
					+ " AND model_sales = 1"
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

			stringval = stringval + "<select name=dr_variant size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_variant style=\"padding:10px\">";
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

	public String PopulateColor(String brand_id, String[] option_ids, String comp_id, HttpServletRequest request) {
		String stringval = "";
		try {

			StrSql = "SELECT option_id, CONCAT(option_name, ' (', option_code, ')') AS option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN axela_brand ON brand_id = option_brand_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = brand_id"
					+ " WHERE option_optiontype_id = 1";
			if (!model_id.equals("")) {
				StrSql += " AND model_id in (" + model_id + ") ";
			}
			StrSql += " GROUP BY option_id"
					+ " ORDER BY option_name";
			// SOP("StrSql-------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);

			stringval = stringval + "<select name=dr_color size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_color style=\"padding:10px\">";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("option_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("option_id"), option_ids);
					stringval = stringval + ">" + crs.getString("option_name") + "</option>\n";
				}
			}
			stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
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
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE 1=1 "
					+ " AND team_active = 1";
			if (!branch_id.equals("0")) {
				StrSql += " AND team_branch_id IN (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "team_branch_id")
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query in check team check ==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_team class=form-control id=dr_team onchange='PopulateExecutives();'><option value = 0>Select</option>\n");

			} else {
				Str.append("<select name=dr_team size=10 id=dr_team class='form-control multiselect-dropdown' multiple=multiple "
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

	public String PopulateSalesExecutives(String team_id, String[] exe_ids, String comp_id, HttpServletRequest request) {

		if (!team_id.equals("") && exe_branch_id.equals("-1")) {
			exe_branch_id = "";
		}
		if (team_id.equals("")) {
			team_id = "0";
		}

		String ExeAccess = GetSession("ExeAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = '1' "
					+ " AND emp_sales = '1'"
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " AND emp_branch_id IN (" + exe_branch_id + ")";
			}

			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql==ccccc== in PopulateSalesExecutive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=form-control><option value = 0>Select</option>\n");
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
			SOPError("Axelaauto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCRMDays(String comp_id, String[] crmdays_ids, String crmdays_crmtype_id, String brand_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmdays_id, concat(crmdays_daycount, crmdays_desc) AS crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1=1 ";
			if (!branch_id.equals("") && !branch_id.equals("0")) {
				StrSql += " AND branch_id IN ( " + branch_id + ")";
			}
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND crmdays_brand_id IN ( " + brand_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";
			// SOP("StrSql------check-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_crmdays_id name=dr_crmdays_id multiple=multiple class='form-control multiselect-dropdown' size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmdays_id")).append("");
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
			Str.append("<select id=dr_crmconcern_id name=dr_crmconcern_id multiple=multiple class='form-control multiselect-dropdown' size=10>");
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

	public String PopulateCRMDays1(String comp_id, String crmdays_id, String crmdays_crmtype_id, String branch_id, String brand_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmdays_id, concat(crmdays_daycount, crmdays_desc) AS crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1 = 1";
			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSql += " AND branch_id IN  (" + branch_id + ")";
			}
			if (!brand_id.equals("0") && !brand_id.equals("")) {
				StrSql += " AND crmdays_brand_id IN  (" + brand_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";
			// SOP("StrSql-----end------------" + StrSql);
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

	public String PopulateManufacturer(String[] carmanuf_id, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		String Str = "";

		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name"
					+ " FROM axela_preowned_manuf"
					+ " WHERE 1 = 1";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str = Str + "<select name=dr_manufacturer size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_manufacturer style=\"padding:10px\""
					+ " onChange=\"PopulateModels();\" >\n>";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str = Str + "<option value=" + crs.getString("carmanuf_id") + "";
					Str = Str + ArrSelectdrop(crs.getInt("carmanuf_id"), carmanuf_id);
					Str = Str + ">" + crs.getString("carmanuf_name") + "</option>\n";
				}
			}
			Str = Str + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str;
	}

	public String PopulatePreownedModels(String carmanuf_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		// String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("carmanuf_ids===" + carmanuf_id);
		if (carmanuf_id.equals("")) {
			carmanuf_id = "0";
		}

		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1 ";
			// if (!carmanuf_id.equals("0")) {
			StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			// }
			// StrSql += BranchAccess;
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_model size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_model style=\"padding:10px\">\n>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("preownedmodel_id"));
					Str.append(ArrSelectdrop(crs.getInt("preownedmodel_id"), model_ids));
					Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLocation(String branch_id, String comp_id, HttpServletRequest request) {
		SOP("location");
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE 1=1";
			if (!branch_id.equals("")) {
				StrSql += " AND location_branch_id IN ( " + branch_id + ")";
			}
			StrSql += " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql==loc==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location\" id=\"dr_location\" multiple=multiple class='form-control multiselect-dropdown'>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("location_id"), location_ids));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEmp(String team_id, String sale_executive, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE 1 = 1"
					+ " AND emp_active = '1' "
					+ " AND emp_sales = '1'"
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " AND emp_branch_id IN (" + exe_branch_id + ")";
			}

			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("PopulateEmp==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_sale_executive\" name=\"dr_sale_executive\" class=\"form-control\">");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), sale_executive));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
