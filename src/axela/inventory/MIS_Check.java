package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check extends Connect {
	public String branch_id = "", location_id = "", region_id = "";
	public String[] region_ids, branch_ids, location_ids, model_ids, item_ids, cat_ids, billtype_ids;
	public String exe_branch_id = "-1";
	public String StrHTML = "", ExeAccess = "";
	public String multiple = "", history = "", single = "";
	public String brand_id = "", model_id = "", option_id = "";
	public String region = "", branches = "";
	public String branch = "";
	public String team = "";
	public String model = "", location = "", item = "", cat = "", billtype = "";
	public String cat_id = "", billtype_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {
			ExeAccess = GetSession("ExeAccess", request);
			brand_id = PadQuotes(request.getParameter("brand_id"));
			region_id = PadQuotes(request.getParameter("region_id"));
			region_ids = request.getParameterValues("region_id");
			branch_id = PadQuotes(request.getParameter("branch_id"));
			model_id = PadQuotes(request.getParameter("model_id"));
			model_ids = request.getParameterValues("model_id");
			item_ids = request.getParameterValues("item_id");
			cat_id = PadQuotes(request.getParameter("cat_id"));
			cat_ids = request.getParameterValues("cat_id");
			billtype_id = PadQuotes(request.getParameter("billtype_id"));
			billtype_ids = request.getParameterValues("billtype_id");
			region = PadQuotes(request.getParameter("region"));
			branch = PadQuotes(request.getParameter("branch"));
			cat = PadQuotes(request.getParameter("cat"));
			billtype = PadQuotes(request.getParameter("billtype"));
			model = PadQuotes(request.getParameter("model"));
			location = PadQuotes(request.getParameter("location"));
			item = PadQuotes(request.getParameter("item"));
			multiple = PadQuotes(request.getParameter("multiple")).trim();
			single = PadQuotes(request.getParameter("single")).trim();
			history = PadQuotes(request.getParameter("history"));
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
			if (cat_id.length() > 1 && cat_id.contains(",")) {
				cat_id = CleanArrVal(cat_id);
			}
			if (multiple.equals("yes") && region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}
			if (multiple.equals("yes") && branch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}
			if (multiple.equals("yes") && model.equals("yes")) {
				StrHTML = PopulateModels(brand_id, model_ids, comp_id, request);
			}
			if (multiple.equals("yes") && location.equals("yes")) {
				StrHTML = PopulateInventoryLocation(branch_id, location_ids, comp_id, request);
			}
			if (multiple.equals("yes") && item.equals("yes")) {
				StrHTML = PopulateVariants(model_id, item_ids, comp_id, request);
			}
			if (multiple.equals("yes") && cat.equals("yes")) {
				StrHTML = PopulateCategory(brand_id, cat_ids, comp_id, "0", "1", request);
			}
			if (multiple.equals("yes") && billtype.equals("yes")) {
				StrHTML = Populatebilltype(comp_id, request);
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axelaauto.axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id IN (1, 2,3) "
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
	public String PopulateBranches(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2,3)"
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
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateInventoryLocation();\"  style=\"padding:10px\">");
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
	public String PopulateRegion(String brand_id, String[] region_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id IN (1,2,3)"
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
	public String PopulateModels(String brand_id, String[] model_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

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

			Str.append("<select name='dr_model' size=10 multiple=multiple class='form-control multiselect-dropdown' id='dr_model' style=\"padding:10px\""
					+ " onChange=\"PopulateVariants();\" >\n>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("model_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
					Str.append(">").append(crs.getString("model_name")).append("</option> \n");
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
	public String PopulateVariants(String model_id, String[] item_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
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

			Str.append("<select name=dr_variant size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_variant style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("item_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("item_id"), item_ids));
					Str.append(">").append(crs.getString("item_name")).append("</option> \n");
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
	public String PopulateCategory(String brand_id, String catids[], String comp_id, String cat_id, String active, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String BranchAccess = GetSession("BranchAccess", request);
		try {
			StrSql = "SELECT cat_id, cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_cat_id = cat_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1"
					+ " AND item_type_id!=4";
			if (!active.equals("")) {
				StrSql += " AND cat_active = " + active + "";
			}
			if (!cat_id.equals("")) {
				StrSql += " AND cat_id != " + cat_id + "";
			}
			if (!brand_id.equals("")) {
				StrSql += " AND brand_id in (" + brand_id + ") ";
			}
			StrSql += BranchAccess
					+ " GROUP BY cat_id"
					+ " ORDER BY cat_name";
			SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_cat id=dr_cat size=10 multiple=multiple class='form-control multiselect-dropdown'  style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("cat_id"));
					Str.append(ArrSelectdrop(Integer.parseInt(crs.getString("cat_id")), catids));
					Str.append(">").append(crs.getString("cat_name"));
					Str.append(":</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public String PopulateInventoryLocation(String branch_id, String location_ids[], String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request).replace("branch_id", "location_branch_id");
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE 1=1"
					+ BranchAccess;
			if (!branch_id.equals("")) {
				StrSql += " AND location_branch_id IN ( " + branch_id + ")";
			}
			StrSql += " GROUP BY location_id"
					+ " ORDER BY location_name";
			SOP("StrSql==loc==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location\" id=\"dr_location\" multiple=multiple class='form-control multiselect-dropdown'>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("location_id"), location_ids));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
	public String Populatebilltype(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT billtype_id, billtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bill_type"
					+ " ORDER BY billtype_name";
			// SOP("PopulateBillType==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jctrans_billtype\" id=\"dr_jctrans_billtype\" multiple=multiple class='form-control multiselect-dropdown'>\n");

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("billtype_id"));
				Str.append(ArrSelectdrop(crs.getInt("billtype_id"), billtype_ids));
				Str.append(">").append(crs.getString("billtype_name")).append("</option>\n");
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

}
