package axela.inbound;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class MIS_Check extends Connect {

	public String branch_id = "", exe_id = "", region_id = "";
	public String[] region_ids, branch_ids, exe_ids;
	public String exe_branch_id = "";
	public String StrHTML = "", ExeAccess = "";
	public String multiple = "", history = "", single = "";
	public String multiplecheck = "";
	public String brand_id = "";
	public String region = "", branches = "";
	public String branch = "";
	public String multiplecheckregion = "";
	public String multiplecheckbranch = "";
	public String executives = "";
	public String comp_id = "0";
	public String dr_branch_id = "";
	public String StrSql = "", exe = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));

		if (!comp_id.equals("0")) {

			brand_id = PadQuotes(request.getParameter("brand_id"));
			region_id = PadQuotes(request.getParameter("region_id"));
			brand_id = CleanArrVal(brand_id);
			region_id = CleanArrVal(region_id);
			branch_id = PadQuotes(request.getParameter("branch_id"));
			if (branch_id.length() > 1) {
				branch_id = CleanArrVal(branch_id);
			}
			exe_id = PadQuotes(request.getParameter("exe_id"));
			if (exe_id.length() > 1) {
				exe_id = CleanArrVal(exe_id);
			}
			region = PadQuotes(request.getParameter("region"));
			branch = PadQuotes(request.getParameter("branch"));
			multiplecheckbranch = PadQuotes(request.getParameter("multiplecheckbranch"));
			multiplecheckregion = PadQuotes(request.getParameter("multiplecheckregion"));
			executives = PadQuotes(request.getParameter("executives"));
			ExeAccess = GetSession("ExeAccess", request);
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			if (exe_branch_id.length() > 1) {
				exe_branch_id = CleanArrVal(exe_branch_id);
			}
			dr_branch_id = PadQuotes(request.getParameter("exe_branch_id"));
			GetValues(request, response);

			if (region.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}

			if (multiplecheckregion.equals("yes")) {
				StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
			}

			if (multiplecheckbranch.equals("yes")) {
				StrHTML = PopulateBranches(brand_id, region_id, branch_ids, comp_id, request);
			}

			if (executives.equals("yes")) {
				StrHTML = PopulateExecutives(exe_ids, comp_id, request);
			}

		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query =====" + StrSql);
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
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1  "
					// + " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateBranches();\" >");
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
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch' id='dr_branch' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateExecutives();\">");
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
		String stringval = "";

		try {
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1=1 ";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + (branch_id) + ") ";
			}
			StrSql += BranchAccess;

			if (!brand_id.equals("")) {
				StrSql += " AND model_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-------PopulateModels------" + StrSqlBreaker(StrSql));
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

	public String PopulateExecutives(String[] exe_ids, String comp_id, HttpServletRequest request) {

		String ExeAccess = GetSession("ExeAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " WHERE 1 = 1 "
					+ " AND emp_active = '1' "
					+ ExeAccess;
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + " and emp_branch_id in (" + exe_branch_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";

			// SOP("StrSql==ccccc== in PopulateSalesExecutive" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (single.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=selectbox><option value = 0>Select</option>\n");
			} else {
				Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple='multiple' size=10>");
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
