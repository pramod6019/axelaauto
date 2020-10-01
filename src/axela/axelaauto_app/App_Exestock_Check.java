package axela.axelaauto_app;
///aJIt 20th June, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Exestock_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String model_id = "0";
	public String brand_id = "0";
	public String branch_id = "0";
	public String comp_id = "0";
	public String item_id = "0";
	public String region_id = "0";
	public String item = "", model = "", branch = "";
	public String colour = "";
	public String team = "";
	public String region = "";
	public String emp_id = "0", BranchAccess = "", ExeAccess = "", fueltype_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		App_Exestock_Search exe = new App_Exestock_Search();
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			model_id = CNumeric((PadQuotes(request.getParameter("model_id"))));
			item_id = CNumeric((PadQuotes(request.getParameter("item_id"))));
			brand_id = CNumeric((PadQuotes(request.getParameter("brand_id"))));
			branch_id = CNumeric((PadQuotes(request.getParameter("branch_id"))));
			model = PadQuotes(request.getParameter("model"));
			item = PadQuotes(request.getParameter("item"));
			colour = PadQuotes(request.getParameter("colour"));
			team = PadQuotes(request.getParameter("team"));
			branch = PadQuotes(request.getParameter("branch"));
			region = PadQuotes(request.getParameter("region"));
			region_id = CNumeric((PadQuotes(request.getParameter("region_id"))));
			if (branch.equals("yes")) {
				StrHTML = PopulateBranch(brand_id, region_id, comp_id);
			}
			if (item.equals("yes")) {
				StrHTML = PopulateItem(model_id, comp_id);
			} else if (colour.equals("yes")) {
				// StrHTML = new Report_Stock_Exe().PopulateColour(item_id);
			}
			if (team.equals("yes")) {
				StrHTML = PopulateTeam(branch_id, comp_id);
			}
			if (model.equals("yes")) {
				StrHTML = PopulateModel(brand_id, branch_id, comp_id);
			}

			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, comp_id);

			}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulateModel(String brand_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ BranchAccess
					+ " WHERE model_active = '1' "
					+ " AND model_sales = '1' ";
			if (!brand_id.equals("0"))
			{
				StrSql += " AND model_brand_id = " + brand_id;
			}
			if (!branch_id.equals("0"))
			{
				StrSql += " AND branch_id = " + branch_id;
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_model_id\" class=\"form-control\" id=\"dr_model_id\" onChange=\"PopulateItem(this.value);\">");
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">" + crs.getString("model_name") + "</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch(String principle_id, String region_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		String SqlStr = "SELECT branch_id, branch_name, branch_code"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_active = 1"
				+ BranchAccess;
		if (!principle_id.equals("0")) {
			SqlStr += " AND branch_brand_id = " + principle_id;
		}
		if (!region_id.equals("0")) {
			SqlStr += " AND branch_region_id =" + region_id;
		}
		SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
		CachedRowSet crs = processQuery(SqlStr, 0);
		try {
			stringval.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateTeam(this.value); PopulateModel(this.value);\">");
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), ""));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateRegion(String principle_id, String comp_id) {
		StringBuilder str3 = new StringBuilder();
		StrSql = "SELECT region_id, region_name"
				+ " FROM " + compdb(comp_id) + "axela_branch_region"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
				+ BranchAccess;
		if (!principle_id.equals("")) {
			StrSql += " AND branch_brand_id IN (" + principle_id + ") ";
		}
		StrSql += " GROUP BY region_id"
				+ " ORDER BY region_name";
		SOP("StrSql========" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			str3.append("<select name=\"dr_region\" id=\"dr_region\" class=\"form-control\" onchange=\"PopulateBranch(this.value);\">");
			str3.append("<option value =0>Select</option>");
			while (crs.next()) {
				str3.append("<option value=").append(crs.getString("region_id")).append("");
				str3.append(StrSelectdrop(crs.getString("region_id"), ""));
				str3.append(">").append(crs.getString("region_name")).append("</option>\n");
			}
			str3.append("</select>");
			crs.close();
			return str3.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateTeam(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT team_id, team_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + branch_id
					+ " ORDER BY team_name";
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<select id=\"dr_team_id\" name=\"dr_team_id\" class=\"form-control\">");
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("team_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("team_id"), ""));
				stringval.append(">").append(crs.getString("team_name")).append("</option>\n");
			}
			stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id"
					+ " WHERE item_type_id = 1"
					+ " AND model_sales = 1 AND model_active = 1"
					+ " AND item_active = 1"
					+ " AND model_id = " + model_id + ""
					+ " AND vehstock_id NOT IN (SELECT so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so "
					+ " WHERE so_active =1 AND so_delivered_date!='')"
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_item_id\" name=\"dr_item_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
