package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check1 extends Connect {

	public String branch_id = "", exe_id = "", crmtype_id = "", region_id = "", zone_id = "", jccat_id = "", jctype_id = "";
	public String[] region_ids, zone_ids, crm_emp_ids, branch_ids, team_ids, exe_ids, model_ids, tech_emp_ids, item_ids,
			jc_emp_ids, psfdays_ids, dr_ticket_owner_ids, jccat_ids, jctype_ids, jcpsfconcern_ids;
	public String exe_branch_id = "";
	public String StrHTML = "", ExeAccess = "";
	public String multiple = "", history = "", single = "";
	public String multiplecheck = "";
	public String brand_id = "", model_id = "", ticket_brand_id = "";
	public String region = "", zone = "", branches = "", jccat = "", jctype = "";
	public String branch = "", allbranch = "";
	public String multiplecheckregion = "";
	public String multiplecheckbranch = "";
	public String team = "";
	public String model = "", item = "", field = "", fieldscheck = "";
	public String executives = "";
	public String team_id = "";
	// public String traffic = "", traffic_id = "";
	public String cat_id = "";
	public String comp_id = "0";
	public String soe = "";
	public String crmfollowup = "";
	public String psf = "", psfdays = "", days_id = "", ticketdays = "", tickettype_id = "", dr_branch_id = "", jcpsfexecutive = "";
	public String crmfollowupdays = "", crmdays = "", crmdays1 = "", advisor = "", technician = "", crmexe = "", serviceadvisor = "";
	public String StrSql = "", exe = "", singlebranch = "", ticketOwner = "", salesandservicebranch = "";
	public String emp_id = "0", jcpsfconcern = "";
	// public axela.service.Service_Target_List serTarget = new axela.service.Service_Target_List();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {

			brand_id = PadQuotes(request.getParameter("brand_id"));
			jccat = PadQuotes(request.getParameter("jccat"));
			jctype = PadQuotes(request.getParameter("jctype"));
			region_id = PadQuotes(request.getParameter("region_id"));
			zone_id = PadQuotes(request.getParameter("zone_id"));
			model_id = PadQuotes(request.getParameter("model_id"));
			brand_id = CleanArrVal(brand_id);
			model_id = CleanArrVal(model_id);
			region_id = CleanArrVal(region_id);
			branch_id = PadQuotes(request.getParameter("branch_id"));
			jcpsfexecutive = PadQuotes(request.getParameter("jcpsfexecutive"));
			// // SOP("branch_id==" + branch_id);
			emp_id = CNumeric(PadQuotes(request.getParameter("dr_executives")));
			if (branch_id.length() > 1 && branch_id.contains(",")) {
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
			if (zone_id.length() > 1 && zone_id.contains(",")) {
				zone_id = CleanArrVal(zone_id);
			}
			region = PadQuotes(request.getParameter("region"));
			zone = PadQuotes(request.getParameter("zone"));
			branch = PadQuotes(request.getParameter("branch"));
			allbranch = PadQuotes(request.getParameter("allbranch"));
			singlebranch = PadQuotes(request.getParameter("singlebranch"));
			ticketOwner = PadQuotes(request.getParameter("ticketOwner"));
			multiplecheckbranch = PadQuotes(request.getParameter("multiplecheckbranch"));
			multiplecheckregion = PadQuotes(request.getParameter("multiplecheckregion"));
			team = PadQuotes(request.getParameter("team"));
			executives = PadQuotes(request.getParameter("executives"));
			model = PadQuotes(request.getParameter("model"));
			crmexe = PadQuotes(request.getParameter("crmexe"));
			advisor = PadQuotes(request.getParameter("advisor"));
			serviceadvisor = PadQuotes(request.getParameter("serviceadvisor"));
			technician = PadQuotes(request.getParameter("technician"));
			model_ids = request.getParameterValues("model_id");
			item = PadQuotes(request.getParameter("item"));
			item_ids = request.getParameterValues("item_id");
			cat_id = PadQuotes(request.getParameter("cat_id"));
			ExeAccess = GetSession("ExeAccess", request);
			crmtype_id = CNumeric(PadQuotes(request.getParameter("crmtype_id")));
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			ticket_brand_id = PadQuotes(request.getParameter("ticket_brand_id"));
			if (exe_branch_id.length() > 1) {
				exe_branch_id = CleanArrVal(exe_branch_id);
			}
			multiple = PadQuotes(request.getParameter("multiple")).trim();
			multiplecheck = PadQuotes(request.getParameter("multiplecheck")).trim();
			single = PadQuotes(request.getParameter("single")).trim();
			history = PadQuotes(request.getParameter("history"));
			soe = PadQuotes(request.getParameter("soe"));
			crmfollowup = PadQuotes(request.getParameter("crmfollowup"));
			psf = PadQuotes(request.getParameter("psf"));
			crmfollowupdays = PadQuotes(request.getParameter("crmfollowupdays"));
			crmdays = PadQuotes(request.getParameter("crmdays"));
			// crmdays1 = PadQuotes(request.getParameter("crmdays"));
			psfdays = PadQuotes(request.getParameter("psfdays"));
			ticketdays = PadQuotes(request.getParameter("ticketdays"));
			tickettype_id = PadQuotes(request.getParameter("tickettype_id"));
			dr_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			salesandservicebranch = PadQuotes(request.getParameter("salesandservicebranch"));
			field = PadQuotes(request.getParameter("field"));
			fieldscheck = PadQuotes(request.getParameter("fieldscheck"));
			jcpsfconcern = PadQuotes(request.getParameter("jcpsfconcern"));
			GetValues(request, response);
			// // SOP("region--------" + region);
			// // SOP("exe_branch_id===" + exe_branch_id);
			// // SOP("advisor===" + advisor);
			// // SOP("tec===" + technician);

			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}

			if (zone.equals("yes")) {
				StrHTML = PopulateZone(brand_id, region_ids, zone_ids, comp_id, request);
			}

			if (multiplecheckregion.equals("yes")) {
				StrHTML = PopulateRegionMultySelect(brand_id, region_ids, comp_id, request);
			}

			if (branch.equals("yes")) {
				// // SOP("zone_id===" + zone_id);
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (multiplecheckbranch.equals("yes")) {
				StrHTML = PopulateBranchesMultySelect(brand_id, region_id, branch_ids, comp_id, request);
			}

			if (team.equals("yes")) {
				StrHTML = PopulateTeams(branch_id, team_ids, comp_id, request);
			}

			if (crmexe.equals("yes")) {
				StrHTML = PopulateCRM(crm_emp_ids, branch_id, comp_id, request);
			}
			if (model.equals("yes")) {
				// // SOP("model_ids--------" + model_ids);
				// // SOP("branch_id==" + branch_id);
				StrHTML = PopulateModels(brand_id, model_ids, branch_id, comp_id, request);
			}
			if (item.equals("yes")) {
				StrHTML = PopulateVariants(model_id, item_ids, comp_id, request);
			}
			if (executives.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}

			if (!cat_id.equals("")) {
				StrHTML = PopulateItems();
			}
			if (psfdays.equals("yes")) {
				StrHTML = PopulatePSFDays(psfdays_ids, branch_id, comp_id);
			}
			if (!exe_branch_id.equals("") && !crmdays.equals("yes") && !team.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}

			if (!exe_branch_id.equals("") && history.equals("yes")) {
				StrHTML = PopulateSalesExecutives(team_id, exe_ids, comp_id, request);
			}

			if (soe.equals("yes")) {
				StrHTML = PopulateSoe();
			}

			if (!exe_branch_id.equals("") && psf.equals("yes")
					|| (!exe_branch_id.equals("0") && crmfollowup.equals("yes"))) {
				// // SOP("crmfollowup=fteammmm=");
				StrHTML = PopulateTeam();
			}
			if (ticketdays.equals("yes")) {
				StrHTML = new Ticket_List().PopulateDays(comp_id, tickettype_id, ticket_brand_id);
			}
			// SOP("ticketdays===" + ticketdays);
			// SOP("multiple===" + multiple);
			if (ticketdays.equals("yes") && multiple.equals("yes")) {
				if (tickettype_id.length() > 1) {
					tickettype_id = CleanArrVal(tickettype_id);
				}

				if (ticket_brand_id.length() > 1) {
					ticket_brand_id = CleanArrVal(ticket_brand_id);
				}
				StrHTML = new Report_Ticket_Status().PopulateDays(comp_id, tickettype_id, ticket_brand_id);
			}

			if (advisor.equals("yes")) {
				// // SOP("1");
				StrHTML = PopulateAdviser(jc_emp_ids, exe_branch_id, comp_id, request);
			}

			if (technician.equals("yes")) {
				StrHTML = PopulateTechnician(tech_emp_ids, exe_branch_id, comp_id, request);
			}

			if (serviceadvisor.equals("yes")) {
				StrHTML = PopulateEmp(emp_id, branch_id, comp_id);
			}
			if (singlebranch.equals("yes")) {
				StrHTML = PopulateBranch(brand_id, region_id, zone_id, branch_id, comp_id, request);
			}

			if (allbranch.equals("yes")) {
				StrHTML = new axela.service.Report_Ticket_Status().PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}

			if (ticketOwner.equals("yes")) {
				StrHTML = PopulateTicketOwner(dr_ticket_owner_ids, exe_branch_id, comp_id);
			}

			if (salesandservicebranch.equals("yes")) {
				StrHTML = PopulateServiceSalesBranch(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (jccat.equals("yes")) {
				StrHTML = PopulateJobCardCategory(jccat_ids, brand_id, comp_id, request);
			}

			if (jctype.equals("yes")) {
				StrHTML = PopulateJobCardType(jctype_ids, brand_id, comp_id, request);
			}

			if (jcpsfexecutive.equals("yes")) {
				StrHTML = new axela.service.Report_JobCard_PSF().PopulatePSFExecutive(branch_id, comp_id);
			}
			// for temporary checking only later has to be changed to new axela.service.Report_Monitoring_Board()
			if (field.equals("yes")) {
				StrHTML = new axela.service.Report_Monitoring_Board_Temp().PopulateOrderBy(comp_id, fieldscheck);
			}

			if (jcpsfconcern.equals("yes")) {
				StrHTML = PopulatePsfConcern(comp_id, jcpsfconcern_ids);
			}
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{

		// // SOP("crmdays_id--------mis--2------" + crmdays_id);
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select soe_id, soe_name  " + " from " + compdb(comp_id)
					+ "axela_soe " + " inner JOIN " + compdb(comp_id)
					+ "axela_sales_enquiry on enquiry_soe_id = soe_id "
					+ " where 1=1 and enquiry_branch_id=" + exe_branch_id + " "
					+ " group by soe_id " + " order by soe_name ";
			// // SOP("PopulateTeam query ==== " + StrSql);
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
			SOPError("Axelaauto== " + this.getClass().getName());
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
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// // SOP("PopulateTeam query =====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("brand_id"), brand_ids));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
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
					+ " WHERE 1=1 "
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// // SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_region' id='dr_region' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateBranches();PopulateBranch();PopulateZone();\" >");
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

	public String PopulateRegionMultySelect(String brand_id, String[] region_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1=1 AND branch_active = 1  "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// // SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control' multiple=multiple size=10 onchange=\"PopulateBranches();\" >");
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
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id in (" + zone_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// // SOP("StrSql------PopulateBranches-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch_id' id='dr_branch_id' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateAdviser();PopulateTech();Populatepsfdays();PopulateModels();PopulateCRMDays();\" style=\"padding:10px\">");

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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchesMultySelect(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// // SOP("StrSql------PopulateBranches-- multy---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch_id id=dr_branch_id class='form-control' multiple='multiple' size=10 >");
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModels(String brand_id, String[] model_ids, String branch_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		if (brand_id.equals("")) {
			brand_id = "0";
		}

		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = carmanuf_id"
					+ " WHERE 1 = 1 ";
			if (!brand_id.equals("1") && branch_id.equals("")) {
				StrSql += " AND carmanuf_id IN(" + brand_id + ")";
			}

			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN(" + branch_id + ")";
			}
			StrSql += BranchAccess;
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// // SOP("StrSql==model===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_model_id size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_model_id style=\"padding:10px\">\n>");
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
			// // SOP("StrSql-------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_variant size=10 multiple=multiple class=textbox id=dr_variant style=\"width:250px\">";
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
			// // SOP("PopulateTeam query in check team check ==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_team class=selectbox id=dr_team onchange=PopulateExecutives();><option value = 0>Select</option>\n");

			} else {
				Str.append("<select name=dr_team size=10 id=dr_team class=textbox multiple=multiple style=\"width:250px\" "
						+ " onChange=\"PopulateExecutives();\" >\n");
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
					+ " AND emp_sales = '1'"
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " and emp_branch_id in (" + exe_branch_id + ")";
			}

			if (!team_id.equals("0")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// // SOP("StrSql==ccccc== in PopulateSalesExecutive" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=selectbox><option value = 0>Select</option>\n");
			} else {
				Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
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

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name" + " from " + compdb(comp_id)
					+ "axela_sales_team" + " where team_branch_id = "
					+ exe_branch_id + " group by team_id"
					+ " order by team_name";
			// // SOP("StrSql====team check=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_team id=dr_team class=selectbox onchange=\"PopulateExecutives();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id"))
						.append("");
				Str.append(">").append(crs.getString("team_name"))
						.append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePSFDays(String psfdays_ids[], String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT psfdays_id, concat(psfdays_daycount, psfdays_desc) AS psfdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_psfdays_id = psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id"
					+ " WHERE 1 = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " GROUP BY psfdays_id"
					+ " ORDER BY psfdays_daycount";
			// // SOP("strsql==PSFDays=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			Str.append("<select name=dr_crmdays_id id=dr_crmdays_id class='form-control multiselect-dropdown' multiple=multiple size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psfdays_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("psfdays_id"), psfdays_ids));
				Str.append(">").append(crs.getString("psfdays_desc")).append("</option> \n");
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

	public String PopulateAdviser(String jc_emp_ids[], String branch_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// // SOP("branch_id===" + branch_id);
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE emp_active = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " AND emp_service = 1";
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// // SOP("Adviser===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_jc_emp_id' id='dr_jc_emp_id' class='form-control multiselect-dropdown' multiple='multiple' size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), jc_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
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

	public String PopulateCRM(String crm_emp_ids[], String branch_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_emp_id = emp_id"
					+ " WHERE emp_active = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			// + " AND emp_crm = 1"
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// // SOP("StrSql--PopulateCRM----" + StrSql);
			Str.append("<select name=dr_emp_id id=dr_emp_id class='form-control multiselect-dropdown' multiple=multiple size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), crm_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
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

	public String PopulateTechnician(String tech_emp_ids[], String branch_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// // SOP("Coming in Tech...");
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					// + " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE emp_active = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " AND emp_technician = 1";
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// // SOP("Technician======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_tech_emp_id\" id=\"dr_tech_emp_id\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("emp_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("emp_id"), tech_emp_ids));
					Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
				}
			}
			Str.append("</select>");

			crs.close();
			// // SOP("Str==" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateZone(String brand_id, String[] region_ids, String[] zone_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT zone_id, zone_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_zone"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_zone_id = zone_id"
					+ " WHERE 1=1"
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY zone_id "
					+ " ORDER BY zone_name ";

			// // SOP("StrSql------PopulateZone-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_zone' id='dr_zone' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateBranches();PopulateBranch();\" >");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("zone_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("zone_id"), zone_ids));
					Str.append(">").append(crs.getString("zone_name")).append("</option> \n");
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

	public String PopulateEmp(String emp_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (branch_id.equals("0")) {
			branch_id = "-1";
		}
		Str.append("<select name=\"dr_executives\" class=\"form-control\" id=\"dr_executives\" onChange=\"document.form1.submit()\">");
		Str.append("<option value=\"0\">Select</option>");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND emp_branch_id IN(" + branch_id + ")";
			}
			StrSql += ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// // SOP("StrSql emp====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");

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

	public String PopulateBranch(String brand_id, String region_id, String zone_id, String branch_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (1,3)"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id in (" + zone_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// // SOP("StrSql------PopulateBranch-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch_id' id='dr_branch_id' class='form-control'  size=10 onchange=\"PopulateAdviser();PopulateTech();PopulateCRMDays(); PopulateModel();Populatepsfdays();\" style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				Str.append("<option value=0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
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

	public String PopulateTicketOwner(String[] dr_ticket_owner, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (branch_id.equals("0")) {
			branch_id = "-1";
		}
		Str.append("<select name=\"dr_ticket_owner\" class='form-control multiselect-dropdown' multiple='multiple' id=\"dr_ticket_owner\" >");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " AND emp_ticket_owner = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND (emp_branch_id IN(" + branch_id + ") OR emp_branch_id = 0) ";
			}
			StrSql += ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// // SOP("StrSql emp====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), dr_ticket_owner));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
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

	public String PopulateServiceSalesBranch(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (1, 2, 3)"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id in (" + zone_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// // SOP("StrSql------PopulateBranches-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch_id' id='dr_branch_id' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateAdviser();PopulateTech();Populatepsfdays();PopulateModels();PopulateCRMDays();\" style=\"padding:10px\">");
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobCardCategory(String jc_cat_ids[], String brand_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// // SOP("Coming in Tech...");
		try {
			StrSql = "SELECT jccat_id, jccat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
					+ " WHERE 1 = 1";
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}

			StrSql += " GROUP BY jccat_id "
					+ " ORDER BY jccat_name ";

			// // SOP("JobCardCategory======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jccat_id\" id=\"dr_jccat_id\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("jccat_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("jccat_id"), jc_cat_ids));
					Str.append(">").append(crs.getString("jccat_name")).append("</option> \n");
				}
			}
			Str.append("</select>");

			crs.close();
			// // SOP("Str==" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobCardType(String jc_type_ids[], String brand_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// // SOP("Coming in Tech...");
		try {

			StrSql = "SELECT jctype_id, jctype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
					+ " WHERE 1 = 1";
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			StrSql += " GROUP BY jctype_id "
					+ " ORDER BY jctype_name";
			// SOP("Type======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jctype_id\" id=\"dr_jctype_id\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("jctype_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("jctype_id"), jc_type_ids));
					Str.append(">").append(crs.getString("jctype_name")).append("</option> \n");
				}
			}
			Str.append("</select>");

			crs.close();
			// // SOP("Str==" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
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

	public String PopulatePsfConcern(String comp_id, String[] jcpsfconcern_ids) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jcpsfconcern_id, jcpsfconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
					+ " WHERE 1=1 "
					+ " GROUP BY jcpsfconcern_id"
					+ " ORDER BY jcpsfconcern_desc";
			// SOP("StrSql------PopulatepsfConcern-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=dr_jcpsfconcern_id name=dr_jcpsfconcern_id multiple=multiple class='form-control multiselect-dropdown' size=10>");
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jcpsfconcern_id")).append("");
				// Str.append(crs.getString("crmdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("jcpsfconcern_id"), jcpsfconcern_ids));
				Str.append(">").append(crs.getString("jcpsfconcern_desc")).append("</option> \n");
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

}
