package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check extends Connect {

	public String branch_id = "", crmtype_id = "", region_id = "";
	public String[] region_ids, branch_ids, model_ids, item_ids, crmdays_ids, crmconcern_ids;
	public String[] preowned_emp_id, preowned_team_id;
	public String brand_id = "";
	public String region = "";
	public String branch = "";
	public String precrmfollowupdays_precrmtype_id = "";
	public String crm_days_id = "0";
	public String crmdays_id = "0";
	public String model = "", item = "", color = "";
	public String[] team_ids, exe_ids, carmanuf_ids;
	public String exe_branch_id = "-1";
	public String StrHTML = "", ExeAccess = "", team = "", preownedteam = "";
	public String precrmtype_id = "";
	public String executives = "", team_id = "", exe_id = "", carmanuf_id = "";
	public String StrSql = "", crmdaysfollowup = "", crmdaysfollowupsingle = "";
	public String single = "", exe = "", crmconcern = "", crmdays = "";
	public String comp_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {
			brand_id = PadQuotes(request.getParameter("brand_id"));
			region_id = PadQuotes(request.getParameter("region_id"));
			branch_id = PadQuotes(request.getParameter("branch_id"));
			team_id = PadQuotes(request.getParameter("team_id"));
			exe_id = PadQuotes(request.getParameter("exe_id"));
			carmanuf_id = PadQuotes(request.getParameter("carmanuf_id"));
			precrmtype_id = PadQuotes(request.getParameter("precrmtype_id"));
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
			executives = PadQuotes(request.getParameter("executives"));
			single = PadQuotes(request.getParameter("single")).trim();
			team = PadQuotes(request.getParameter("team_id"));
			preownedteam = PadQuotes(request.getParameter("preownedteam"));
			exe = PadQuotes(request.getParameter("exe"));
			crmdaysfollowup = PadQuotes(request.getParameter("crmdaysfollowup"));
			crmdays = PadQuotes(request.getParameter("crmdays"));
			crmdaysfollowupsingle = PadQuotes(request.getParameter("crmdaysfollowupsingle"));
			crmconcern = PadQuotes(request.getParameter("crmconcern"));
			region = PadQuotes(request.getParameter("region"));
			branch = PadQuotes(request.getParameter("branch"));
			model = PadQuotes(request.getParameter("model"));

			if (exe_branch_id.length() > 1 && exe_branch_id.contains(",")) {
				exe_branch_id = CleanArrVal(exe_branch_id);
			}

			GetValues(request, response);

			if (brand_id.length() > 1 && brand_id.contains(",")) {
				brand_id = CleanArrVal(brand_id);
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

			if (carmanuf_id.length() > 1 && carmanuf_id.contains(",")) {
				carmanuf_id = CleanArrVal(carmanuf_id);
			}
			GetValues(request, response);

			if (!exe_branch_id.equals("0") && exe.equals("yes")) {
				StrHTML = new Preowned_TestDrive_Cal().PopulateExecutive(exe_branch_id, comp_id);
			}

			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}

			if (model.equals("yes")) {
				StrHTML = PopulateModels(model_ids, comp_id, request);
			}

			if (crmconcern.equals("yes")) {
				StrHTML = PopulateConcern(comp_id, crmconcern_ids, crmtype_id);
			}

			if (crmdays.equals("yes")) {
				StrHTML = PopulateCRMDays(comp_id, crmdays_ids, crmtype_id, branch_id);
			}

			if (crmdaysfollowupsingle.equals("yes")) {
				exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
				StrHTML = new Report_PreownedCRMFollowup().PopulatePreownedCRMDays(comp_id, exe_branch_id);
			}

			if (branch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}

			if (executives.equals("yes")) {
				// SOP("branch_id==2==" + branch_id);
				StrHTML = PopulatePreownedExecutives(branch_id, team_id, comp_id);
			}
			if (region.equals("regionpss")) {
				StrHTML = new Preowned_Stock_Status().PopulateRegion(brand_id, region_ids, comp_id, request);
			}
			if (branch.equals("branchpss")) {
				StrHTML = new Preowned_Stock_Status().PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (crmdaysfollowup.equals("yes")) {
				StrHTML = PopulateCRMDays1(comp_id, crmdays_id, precrmfollowupdays_precrmtype_id, exe_branch_id);
			}
			if (preownedteam.equals("yes")) {
				StrHTML = PopulatePreownedTeams(branch_id, team_ids, comp_id, request);
			}

			if (model.equals("model")) {
				StrHTML = PopulatePreownedModels(carmanuf_id, model_ids, comp_id, request);
			}

			if (executives.equals("preownedexe")) {
				StrHTML = PopulatePreownedExecutivesTeam(team_id, exe_ids, comp_id);
			}

		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		preowned_team_id = request.getParameterValues("dr_team");
		preowned_emp_id = request.getParameterValues("dr_executive");
		crm_days_id = CNumeric(PadQuotes(request.getParameter("dr_crmfollowupdays")));
		crmdays_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_id")));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (2)"
					+ BranchAccess
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("brand_id"), brand_ids));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id IN (2)"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_region' id='dr_region' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateBranches();\" >");
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
			SOPError("Axelaauto===" + this.getClass().getName());
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
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (2)"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==branch_ids==" + branch_ids);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch' id='dr_branch' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateExecutives();PopulatePreownedTeams();PopulateAdviser();PopulateTech();Populatepsfdays();PopulateCRMDays();\">");
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

	public String PopulateTeam(String exe_branch_id, String team_ids[], String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedteam_id, preownedteam_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE 1=1";
			if (!CNumeric(exe_branch_id).equals("0")) {
				StrSql += " AND preownedteam_branch_id=" + exe_branch_id + " ";
			}
			StrSql += " GROUP BY preownedteam_id "
					+ " ORDER BY preownedteam_name ";
			// SOP("StrSql-------check------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_team\" id=\"dr_team\" class=\"form-control\" multiple=\"multiple\" size=\"10\" "
					+ " onchange=ExeCheck();\n>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedteam_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("preownedteam_id"), team_ids));
				Str.append(">").append(crs.getString("preownedteam_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedTeams(String branch_id, String[] team_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		if (branch_id.equals("")) {
			branch_id = "0";
		}
		try {
			StrSql = "SELECT preownedteam_id, preownedteam_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_team "
					+ " WHERE 1 = 1 ";
			if (!branch_id.equals("0")) {
				StrSql += " AND preownedteam_branch_id IN (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "preownedteam_branch_id")
					+ " GROUP BY preownedteam_id "
					+ " ORDER BY preownedteam_name ";

			// SOP("StrSql team=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name='dr_preownedteam' class='form-control multiselect-dropdown' id='dr_preownedteam' onchange='PopulateExecutives();'><option value = 0>Select</option>\n");

			} else {
				Str.append("<select name='dr_preownedteam'  id='dr_preownedteam' class='form-control multiselect-dropdown' multiple='multiple' "
						+ " onChange=\"PopulateExecutives();\">");
			}
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("preownedteam_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("preownedteam_id"), team_ids));
					Str.append(">").append(crs.getString("preownedteam_name")).append("</option> \n");
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

	public String PopulatePreownedExecutives(String exe_branch_id, String team_ids, String comp_id) {

		StringBuilder Str = new StringBuilder();
		// SOP("exe_ids===" + exe_ids);
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_team_exe on preownedteamtrans_emp_id = emp_id"
					+ " WHERE 1 = 1 "
					+ " AND emp_active = 1 "
					+ " AND emp_preowned = 1"
					+ ExeAccess;
			if (!exe_branch_id.equals("") && !exe_branch_id.equals("0")) {
				StrSql = StrSql + " AND emp_branch_id IN (" + exe_branch_id + ")";
			}

			if (!team_ids.equals("") && !team_id.equals("0")) {
				StrSql = StrSql + " AND preownedteamtrans_team_id IN (" + team_ids + ")";
			}

			if (!exe_id.equals("")) {
				StrSql = StrSql + " AND preownedteamtrans_emp_id IN (" + exe_ids + ")";
			}

			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql==ccccc== in PopulateSalesExecutive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name='dr_executive' id='dr_executive' class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCRMDays(String comp_id, String[] crmdays_ids, String precrmfollowupdays_precrmtype_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("exe_branch_id--------" + exe_branch_id);
			StrSql = "SELECT precrmfollowupdays_id, concat(precrmfollowupdays_daycount, precrmfollowupdays_desc) as precrmfollowupdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = precrmfollowupdays_brand_id"
					+ " WHERE 1 = 1 ";
			if (!exe_branch_id.equals("") && !exe_branch_id.equals("0")) {
				StrSql += " AND branch_id IN ( " + exe_branch_id + ")";
			}
			if (!precrmfollowupdays_precrmtype_id.equals("0")) {
				StrSql += " AND precrmconcern_precrmtype_id =" + precrmfollowupdays_precrmtype_id + "";
			}
			StrSql += " GROUP BY precrmfollowupdays_id"
					+ " ORDER BY precrmfollowupdays_daycount";
			// SOP("StrSql------check-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_crmdays_id name=dr_crmdays_id multiple=multiple class=form-control size=10>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmfollowupdays_id")).append("");
				// Str.append(crs.getString("precrmfollowupdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("precrmfollowupdays_id"), crmdays_ids));
				Str.append(">").append(crs.getString("precrmfollowupdays_desc")).append("</option> \n");
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

	public String PopulateCRMDays1(String comp_id, String precrmfollowupdays_id, String crmdays_crmtype_id, String exe_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT precrmfollowupdays_id, concat(precrmfollowupdays_daycount, precrmfollowupdays_desc) as precrmfollowupdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = precrmfollowupdays_brand_id"
					+ " WHERE 1 = 1";
			if (!exe_branch_id.equals("0") && !exe_branch_id.equals("")) {
				StrSql += " AND branch_id IN  (" + exe_branch_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY precrmfollowupdays_id"
					+ " ORDER BY precrmfollowupdays_daycount";
			// SOP("StrSql-----end------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_crmdays_id1\" name=\"dr_crmdays_id1\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmfollowupdays_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmfollowupdays_id"), precrmfollowupdays_id));
				Str.append(">").append(crs.getString("precrmfollowupdays_desc")).append("</option> \n");
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
			StrSql = "SELECT precrmconcern_id, precrmconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_concern"
					+ " WHERE 1=1 ";
			if (!crmdays_crmtype_id.equals("0")) {
				StrSql += " AND precrmconcern_precrmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY precrmconcern_id"
					+ " ORDER BY precrmconcern_desc";
			// SOP("StrSql------PopulateConcern-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_precrmconcern_id name=dr_precrmconcern_id multiple=multiple class=form-control size=10>");
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("precrmconcern_id")).append("");
				// Str.append(crs.getString("crmdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("precrmconcern_id"), crmconcern_ids));
				Str.append(">").append(crs.getString("precrmconcern_desc")).append("</option> \n");
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

	public String PopulateModels(String[] model_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";

		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1 = 1 "
					// + " AND model_sales=1"
					// + " AND model_active = '1'"
					+ BranchAccess;
			// if (!brand_id.equals("")) {
			// StrSql += " AND model_brand_id in (" + brand_id + ") ";
			// }
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql-------PopulateModels------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_model size=10 multiple=multiple class=form-control id=dr_model style=\"padding:10px\""
					+ " onChange=\"PopulateVariants();PopulateColor();\" >\n>";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("preownedmodel_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("preownedmodel_id"), model_ids);
					stringval = stringval + ">" + crs.getString("preownedmodel_name") + "</option>\n";
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

	public String PopulatePreownedExecutivesTeam(String team_ids, String[] exe_ids, String comp_id) {
		if (team_id.equals("")) {
			team_id = "0";
		}
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = emp_id"
					+ " WHERE 1 = 1 "
					+ " AND emp_active = 1 "
					+ " AND emp_preowned = 1"
					+ ExeAccess;

			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " AND emp_branch_id IN (" + exe_branch_id + ")";
			}

			if (!team_ids.equals("")) {
				StrSql = StrSql + " AND preownedteamtrans_team_id IN (" + team_ids + ")";
			}

			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("PopulatePreownedExecutive--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10 style=\"padding:10px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedModels(String carmanuf_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		if (carmanuf_id.equals("")) {
			carmanuf_id = "0";
		}

		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1 ";
			if (!carmanuf_id.equals("0")) {
				StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			}
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

}
