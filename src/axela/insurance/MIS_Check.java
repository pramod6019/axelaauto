package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check extends Connect {

	public String StrHTML = "", ExeAccess = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String insurfollowup_sob_id = "", brand_id = "", model_id = "", region_id = "", branch_id = "", insur_emp_id = "";
	public String carmanuf_id = "", preowned_model_id = "";
	public String modelmultiplecheck = "", preownedmodelmulti = "", reassignmodel = "", preownedmodel = "";
	public String[] brand_ids, region_ids, branch_ids, model_ids, insur_emp_ids, field_emp_ids;
	public String[] carmanuf_ids, preowned_model_ids;
	public String region = "", branch = "", model = "", insur = "", insur_emp = "", fieldemp_id = "";
	public String containerid = "", emp_id = "0";
	public String insurcampaign = "", insurenquiry_branch_id = "0";
	public String insurexecutives = "", fieldexe = "";
	public String prospectcount = "";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		insurfollowup_sob_id = PadQuotes(request.getParameter("insurfollowup_sob_id"));
		modelmultiplecheck = PadQuotes(request.getParameter("modelmultiplecheck"));
		preownedmodelmulti = PadQuotes(request.getParameter("preownedmodelmulti"));
		prospectcount = PadQuotes(request.getParameter("prospectcount"));
		String veh_branch_id = PadQuotes(request.getParameter("veh_branch_id"));

		brand_id = PadQuotes(request.getParameter("brand_id"));
		region_id = PadQuotes(request.getParameter("region_id"));
		branch_id = PadQuotes(request.getParameter("branch_id"));
		model_id = PadQuotes(request.getParameter("model_id"));
		insur_emp_id = PadQuotes(request.getParameter("insur_emp_id"));

		brand_id = CleanArrVal(brand_id);
		region_id = CleanArrVal(region_id);
		branch_id = CleanArrVal(branch_id);
		model_id = CleanArrVal(model_id);
		insur_emp_id = CleanArrVal(insur_emp_id);

		brand_ids = request.getParameterValues("brand_id");
		region_ids = request.getParameterValues("region_id");
		branch_ids = request.getParameterValues("branch_id");
		model_ids = request.getParameterValues("model_id");
		insur_emp_ids = request.getParameterValues("insur_emp_id");
		containerid = request.getParameter("containerid");

		region = PadQuotes(request.getParameter("region"));
		branch = PadQuotes(request.getParameter("branch"));
		model = PadQuotes(request.getParameter("model"));
		preownedmodel = PadQuotes(request.getParameter("preownedmodel"));
		insur = PadQuotes(request.getParameter("insur"));
		insur_emp = PadQuotes(request.getParameter("insur_emp"));
		insurcampaign = PadQuotes(request.getParameter("insurcampaign"));

		carmanuf_id = PadQuotes(request.getParameter("carmanuf_id"));
		carmanuf_id = CleanArrVal(carmanuf_id);
		carmanuf_ids = request.getParameterValues("carmanuf_id");
		preowned_model_id = PadQuotes(request.getParameter("dr_preowned_model"));
		preowned_model_ids = request.getParameterValues("dr_preowned_model");
		insurenquiry_branch_id = request.getParameter("insurenquiry_branch_id");
		insurexecutives = PadQuotes(request.getParameter("insurexecutives"));
		fieldexe = PadQuotes(request.getParameter("fieldexe"));
		fieldemp_id = RetrunSelectArrVal(request, "dr_field_exe");
		field_emp_ids = request.getParameterValues("dr_field_exe");

		if (!comp_id.equals("0")) {
			ExeAccess = GetSession("ExeAccess", request);
			if (modelmultiplecheck.equals("yes")) {
				StrHTML = PopulateModels(model_id, model_ids, containerid, comp_id, request);
			}
			if (insurfollowup_sob_id.equals("yes")) {
				String insurfollowup_soe_id = CNumeric(PadQuotes(request.getParameter("insurfollowup_soe_id")));
				// SOP("insurfollowup_soe_id====" + insurfollowup_soe_id);
				StrHTML = new Assign_Prospect().PopulateSOB(insurfollowup_sob_id, insurfollowup_soe_id, comp_id);
			}
			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}
			if (branch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (model.equals("yes")) {
				StrHTML = PopulateModel(brand_id, model_ids, branch_id, comp_id, request);
			}
			if (insur.equals("yes")) {
				StrHTML = PopulateInsurEmp(insur_emp_ids, branch_id, comp_id, request);
			}
			if (preownedmodelmulti.equals("yes")) {
				StrHTML = PopulatePreOwnedModels(preowned_model_id, preowned_model_ids, containerid, comp_id, request);
			}
			if (insurcampaign.equals("yes")) {
				if (!insurenquiry_branch_id.equals("")) {
					StrHTML = new Insurance_Enquiry_Dash().PopulateCampaign(insurenquiry_branch_id, comp_id);
				}
				if (!veh_branch_id.equals("")) {
					StrHTML = new Insurance_Enquiry_Update().PopulateCampaign(veh_branch_id, comp_id);
				}
			}
			if (preownedmodel.equals("yes")) {
				StrHTML = PopulatePreownedModel(carmanuf_id, model_ids, comp_id, request);
			}

			if (insurexecutives.equals("yes")) {
				StrHTML = PopulateInsurEmp(insur_emp_ids, branch_id, comp_id, request);
			}

			if (fieldexe.equals("yes")) {
				StrHTML = PopulateFieldExecutive(comp_id, branch_id, field_emp_ids, request);
			}

			if (prospectcount.equals("yes") && !insurenquiry_branch_id.equals("")) {
				StrHTML = new Assign_Prospect().PopulateProspectCountByBucket(insurenquiry_branch_id, comp_id, request);
			}

		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulatePrincipal(String brand_id, String[] brand_ids, String comp_id, HttpServletRequest request) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("brand query =====" + StrSql);
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

	public String PopulateModels(String model_id, String[] model_ids, String containerid, String comp_id, HttpServletRequest request) {

		if (brand_id.length() > 1 && brand_id.contains(",")) {
			brand_id = CleanArrVal(brand_id);
		}
		String stringval = "";
		try {
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " WHERE 1=1 "
					+ " AND model_sales=1"
					+ " AND model_active = '1'";
			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id IN (" + brand_id + ")";
			}

			StrSql += " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("StrSql-------PopulateModels------" + StrSql);
			CachedRowSet crs = processQuery(StrSql);

			stringval = "<select name=\"" + containerid + "\" id=\"" + containerid + "\" multiple=\"multiple\" class=\"form-control insurmultiselect hidden\" >";

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
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return stringval;
	}

	public String PopulateRegion(String brand_id, String[] region_ids, String comp_id, HttpServletRequest request) {
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

			// SOP("StrSql------PopulateRegion-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control multiselect-dropdown' style='padding: 10px' multiple=multiple size=10 onchange=\"PopulateBranches();\" >");
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
					+ " AND branch_branchtype_id = 6"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				// SOP("brand_id----" + brand_id);
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch_id id=dr_branch_id class='form-control multiselect-dropdown' style='padding: 10px' multiple=multiple size=10 onchange=\"PopulateInsurEmp();PopulateFieldExecutives();\" >");
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

	public String PopulateInsurEmp(String insur_emp_ids[], String branch_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					+ " WHERE emp_active = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " AND emp_insur = 1";
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("Insurance Exe===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_insur_emp_id id=dr_insur_emp_id class='form-control multiselect-dropdown' multiple=multiple size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), insur_emp_ids));
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

	public String PopulateModel(String brand_id, String[] model_ids, String branch_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";

		try {
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1=1 ";
			if (!branch_id.equals("")) {
				// SOP("branch_id===" + branch_id);
				StrSql += " AND branch_id IN (" + (branch_id) + ") ";
			}
			StrSql += BranchAccess;

			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-------PopulateModel------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_model_id size=10 multiple=multiple class=form-control id=dr_model_id>\n>";
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

	public String PopulateManufacturer(String[] carmanuf_ids, String carmanuf_id, String comp_id, HttpServletRequest request) {
		String stringval = "";

		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name"
					+ " FROM axela_preowned_manuf"
					+ " WHERE 1 = 1";
			CachedRowSet crs = processQuery(StrSql, 0);
			//
			// stringval = stringval + "<select name=dr_manufacturer size=10 multiple=multiple class=form-control id=dr_manufacturer style=\"padding:10px\""
			// + " onChange=\"PopulateModels();\" >\n>";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("carmanuf_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("carmanuf_id"), carmanuf_ids);
					stringval = stringval + ">" + crs.getString("carmanuf_name") + "</option>\n";
				}
			}
			// stringval = stringval + "</select>";
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return stringval;
	}

	public String PopulatePreOwnedModels(String preowned_model_id, String[] preowned_model_ids, String containerid, String comp_id, HttpServletRequest request) {
		String stringval = "";
		if (carmanuf_id.equals("")) {
			carmanuf_id = "0";
		}
		SOP("preowned_model_id==33=" + preowned_model_id);
		SOP("preoned_model_ids==33=" + preowned_model_ids);
		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1 ";
			StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("containerid===" + containerid);
			stringval = "<select name=\"" + containerid + "\" id=\"" + containerid + "\" multiple=\"multiple\" class=\"form-control multiselect-dropdown\" >";

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stringval = stringval + "<option value=" + crs.getString("preownedmodel_id") + "";
					stringval = stringval + ArrSelectdrop(crs.getInt("preownedmodel_id"), preowned_model_ids);
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

	public String PopulatePreownedModel(String carmanuf_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		String stringval = "";
		if (carmanuf_id.equals("")) {
			carmanuf_id = "0";
		}
		SOP("model_ids===" + model_id);
		try {
			StrSql = "SELECT preownedmodel_id,"
					+ " COALESCE ( CONCAT( carmanuf_name, ' ', preownedmodel_name, ' ' ), '' ) AS preownedmodel_name "
					+ " FROM axela_preowned_model "
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1 ";
			// if (!carmanuf_id.equals("0")) {
			StrSql += " AND carmanuf_id IN(" + carmanuf_id + ")";
			// }
			StrSql += BranchAccess;
			StrSql += " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			stringval = stringval + "<select name=dr_model id=dr_model class='form-control multiselect-dropdown' multiple=multiple size=10 >";
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

	public String PopulateFieldExecutive(String comp_id, String branch_id, String[] field_emp_ids, HttpServletRequest request) {
		// SOP("Field comp id===" + comp_id);
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					+ " WHERE 1=1"
					+ " AND emp_fieldinsur = 1"
					+ " AND emp_active = 1";
			if (!branch_id.equals(""))
			{
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";

			// SOP("PopulateFieldExecutive-==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name='dr_field_exe' id='dr_field_exe' size='10' multiple='multiple' class='form-control multiselect-dropdown'>");
			// Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), field_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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

	public String PopulateListInsuranceExecutives(String[] exe_ids, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\" >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
