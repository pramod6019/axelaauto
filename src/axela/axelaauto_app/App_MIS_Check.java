package axela.axelaauto_app;
///// divya
// modified by sn 6, 7 may 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_MIS_Check extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String region = "";
	public String branch = "";
	public String model = "";
	public String executive = "";
	public String brand_id = "";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public String region_id = "", branch_id = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		// SOP("session ID=====app-mis=======" + session.getId());
		comp_id = CNumeric(GetSession("comp_id", request));
		BranchAccess = GetSession("BranchAccess", request);
		// SOP("BranchAccess------111----" + BranchAccess);
		ExeAccess = GetSession("ExeAccess", request);
		// emp_all_exe = GetSession("emp_all_exe", request);
		// if (emp_all_exe.equals("1"))
		// {
		// ExeAccess = "";
		// }
		region = PadQuotes(request.getParameter("region"));
		branch = PadQuotes(request.getParameter("branch"));
		model = PadQuotes(request.getParameter("model"));
		executive = PadQuotes(request.getParameter("executive"));
		brand_id = PadQuotes(request.getParameter("brand_id"));
		region_id = PadQuotes(request.getParameter("region_id"));
		branch_id = PadQuotes(request.getParameter("exe_branch_id"));

		if (region.equals("yes"))
		{
			StrHTML = PopulateRegion(brand_id, comp_id, BranchAccess);
		}
		if (branch.equals("yes"))
		{
			StrHTML = Populatebranch1(brand_id, region_id, comp_id, BranchAccess);
		}
		if (model.equals("yes"))
		{
			StrHTML = PopulateModel(brand_id, comp_id);
		}
		if (executive.equals("yes"))
		{
			StrHTML = PopulateExecutive(branch_id, comp_id, BranchAccess, ExeAccess);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulateBrand(String comp_id, String BranchAccess) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"one\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Brand").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallbrand_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")

					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("brand_name"))
						.append("</td>\n")
						.append("<td class=\"\" style=\"float:right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casebrand_id icheck\" onChange=\"PopulateRegion();PopulateBranches();PopulateModels();\" name=\"chk_brand_id\" id=\"chk_brand_id\" value=\"")
						.append(crs.getString("brand_id"))
						.append("\"></td>\n")
						// .append("<input type=\"checkbox\" class=\"casebrand_id icheck\" name=\"chk_brand_id\" value=\"" + crs.getString("brand_id") + " \">").append("</td>\n")
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

	public String PopulateRegion(String brand_id, String comp_id, String BranchAccess) {

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
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallregion_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("region_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"caseregion_id icheck\" onChange=\"PopulateBranches()\" name=\"chk_region_id\" id=\"chk_region_id\" value=\"")
						.append(crs.getString("region_id")).append("\"></td>\n")
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

	public String PopulateExecutive(String branch_id, String comp_id, String BranchAccess, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ " AND emp_sales='1'"
					+ " AND emp_active='1'";
			if (!branch_id.equals("")) {
				StrSql += " AND emp_branch_id IN (" + branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "emp_branch_id")
					+ ExeAccess;
			StrSql += " GROUP BY emp_id ";
			StrSql += " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"two\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Executives").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallexec_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("emp_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"caseexec_id icheck\" name=\"chk_executive_id\" id=\"chk_executive_id\" value=\""
								+ crs.getString("emp_id") + " \">").append("</td>\n")
						.append("</tr>\n");
			}
			Str.append("</table>\n")
					.append("</div>\n");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateModel(String brand_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=model_brand_id"
					+ " AND item_type_id = 1"
					+ " WHERE model_active = '1'"
					+ " AND model_sales = '1'";

			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}

			StrSql += " GROUP BY model_id"
					+ " ORDER BY model_brand_id, model_name";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"one\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Model").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallmodel_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("model_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casemodel_id icheck\" name=\"chk_model_id\" id=\"chk_model_id\" value=\"")
						.append(crs.getString("model_id")).append("\"></td>\n")
						// .append("<input type=\"checkbox\" class=\"casemodel_id icheck\" name=\"chk_model_id\" value=\"" + crs.getString("model_id") + " \">").append("</td>\n")
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
	public String Populatebranch1(String brand_id, String region_id, String comp_id, String BranchAccess) {

		StringBuilder Str = new StringBuilder();
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active='1' "
					+ BranchAccess;

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"one\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Branch").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallbranch_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("branch_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casebranch_id icheck\" onChange=\"PopulateExecutives()\" name=\"chk_branch_id\" id=\"chk_branch_id\" value=\"")
						.append(crs.getString("branch_id")).append("\"></td>\n")
						// .append("<input type=\"checkbox\" class=\"casebranch_id icheck\" name=\"chk_branch_id\" value=\"" + crs.getString("branch_id") + " \">").append("</td>\n")
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
}
