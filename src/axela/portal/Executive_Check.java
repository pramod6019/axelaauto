package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Team_Update;
import cloudify.connect.Connect;

public class Executive_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String branch_id = "", multiple = "", executive = "";
	public String region_id = "", brand_id = "", type_id = "", jobtitle_id = "", department_id = "";
	public String[] exe_ids, region_ids, branch_ids, type_ids, department_ids;
	public String region = "", branch = "";
	public String branch_update = "", preownedteam = "";
	public String dr_title = "";
	public Team_Update team = new Team_Update();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				brand_id = PadQuotes(request.getParameter("brand_id"));
				region_id = PadQuotes(request.getParameter("region_id"));
				jobtitle_id = PadQuotes(request.getParameter("jobtitle_id"));
				department_id = PadQuotes(request.getParameter("department_id"));
				type_id = PadQuotes(request.getParameter("type_id"));
				brand_id = CleanArrVal(brand_id);
				type_id = CleanArrVal(type_id);
				region_id = CleanArrVal(region_id);
				jobtitle_id = CleanArrVal(jobtitle_id);
				department_id = CleanArrVal(department_id);
				multiple = PadQuotes(request.getParameter("multiple")).trim();
				branch_update = PadQuotes(request.getParameter("branch_update"));
				preownedteam = PadQuotes(request.getParameter("preownedteam"));
				branch_id = PadQuotes(request.getParameter("branch_id"));
				dr_title = PadQuotes(request.getParameter("dr_title"));
				if (dr_title.length() > 1 && dr_title.contains(",")) {
					dr_title = CleanArrVal(dr_title);
				}
				// branches = PadQuotes(request.getParameter("branches"));
				// if (branch_id.length() > 1 && branch_id.contains(",")) {
				// branch_id = CleanArrVal(branch_id);
				// }
				if (!branch_id.equals("") && jobtitle_id.equals("") && department_id.equals("")) {
					StrHTML = PopulateExecutives();
				}
				region = PadQuotes(request.getParameter("region"));
				branch = PadQuotes(request.getParameter("branch"));
				executive = PadQuotes(request.getParameter("executive"));

				if (region.equals("yes")) {
					StrHTML = PopulateRegion(brand_id, region_ids, comp_id, request);
				}
				if (!jobtitle_id.equals("") || !department_id.equals("")) {
					StrHTML = PopulateExecutives(request, branch_id, department_id, jobtitle_id, exe_ids, comp_id);
				}

				if (preownedteam.equals("yes")) {
					StrHTML = team.PopulatePreownedExecutives(comp_id, branch_id);
				}
				// if (branch.equals("yes")) {
				// StrHTML = PopulateBranches(brand_id, region_id,
				// branch_ids, comp_id, request);
				// }
				// if (branch.equals("yes")) {
				// StrHTML = PopulateBranches(brand_id, region_id,
				// branch_ids, comp_id, request);
				// }

				if (branch.equals("yes")) {
					StrHTML = PopulateBranches(brand_id, region_id, type_id, branch_ids, comp_id, request);
				}
				if (executive.equals("yes") && !dr_title.equals("")) {
					StrHTML = PopulateExecutive(dr_title, branch_id, request, comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			String field[] = branch_id.split(",");
			branch_id = "";
			for (int i = 0; i < field.length; i++) {
				if (!field[i].equals("")) {
					branch_id += field[i] + ",";
				}
			}
			if (field.length > 0) {

				branch_id = branch_id.substring(0, branch_id.length() - 1);

				StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1";
				if (!branch_id.equals("")) {
					StrSql = StrSql + " and emp_branch_id IN (" + branch_id + ")";
				}
				if (!department_id.equals("")) {
					StrSql = StrSql + " and emp_department_id IN (" + department_id + ")";
				}
				if (!jobtitle_id.equals("")) {
					StrSql = StrSql + " and emp_jobtitle_id IN (" + jobtitle_id + ")";
				}

				StrSql = StrSql + " AND emp_active = 1"
						+ " group by emp_id"
						+ " order by emp_name";
				// SOP("PopulateExecutives StrSql===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (multiple.equals("yes")) {
					Str.append("<select name='dr_executive' id='dr_executive' class='form-control multiselect-dropdown' multiple=\"multiple\" size=20>");
				}
				else if (branch_update.equals("yes")) {
					Str.append("<select name='dr_team_preownedemp_id' id='dr_team_preownedemp_id' class='selectbox'><option value = 0>Select</option>\n");
				} else {
					Str.append("<select name='dr_executive' id='dr_executive' class='selectbox'><option value = 0>Select</option>\n");
				}
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</option>\n");
				}
				Str.append("</select>");
				crs.close();
			} else {
				Str.append("<select name='dr_executive' size='15' multiple='multiple' class='form-control' id='dr_executive'></select>");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP("Str.toString()===" + Str.toString());

		return Str.toString();
	}
	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query =====" + StrSql);
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
					+ " WHERE 1=1 AND branch_active = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateBranches();page_submit();\">");
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
	public String PopulateBranchType(String[] type_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branchtype_id, branchtype_name"
					+ " FROM axela_branch_type"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_branchtype_id = branchtype_id"
					+ " WHERE 1=1 "
					+ " AND branch_active = 1"
					+ BranchAccess
					+ " GROUP BY branchtype_id"
					+ " ORDER by branchtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branchtype_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("branchtype_id"), type_ids));
				Str.append(">").append(crs.getString("branchtype_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateBranches(String brand_id, String region_id, String type_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			if (!type_id.equals("")) {
				StrSql += " AND branch_branchtype_id in (" + type_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"page_submit();\">");
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

	public String PopulateExecutives(HttpServletRequest request, String Branch_id, String department_id, String jobtitle_id, String exe_ids[], String comp_id) {

		String ExeAccess = GetSession("ExeAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = emp_branch_id"
					+ " WHERE 1=1 "
					+ " AND emp_active = '1'";
			if (!Branch_id.equals("")) {
				StrSql += " AND emp_branch_id IN (" + Branch_id + ")";
			}
			if (!department_id.equals("")) {
				StrSql += " AND emp_department_id IN (" + department_id + ")";
			}
			if (!jobtitle_id.equals("")) {
				StrSql += " AND emp_jobtitle_id IN (" + jobtitle_id + ")";
			}
			if (AppRun().equals("1")) {
				StrSql += " AND emp_id != '1'";
			}
			StrSql += ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";

			SOP("PopulateExecutives--------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10>");
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

	public String PopulateJobTitle(String[] jobtitle_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);
			StrSql = "SELECT jobtitle_id, jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_jobtitle "
					+ " ORDER BY jobtitle_desc";
			// SOP("PopulateJobTitle query =====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jobtitle_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("jobtitle_id"), jobtitle_ids));
				Str.append(">").append(crs.getString("jobtitle_desc")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDepartment(String[] department_ids, String department_id, String branch_id, String region_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);

			StrSql = "SELECT department_id, department_name"
					+ " FROM " + compdb(comp_id) + "axela_emp_department "
					+ " ORDER BY department_name";

			// SOP("PopulateExecutives--------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_department id=dr_department class='form-control multiselect-dropdown' multiple=\"multiple\" onChange='PopulateExecutives();' size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("department_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("department_id"), department_ids));
				Str.append(">").append(crs.getString("department_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}

	public String PopulateExecutive(String emp_jobtitle_id, String branch_id, HttpServletRequest request, String comp_id) {
		String BranchAccess = GetSession("BranchAccess", request);
		String ExeAccess = GetSession("BranchAccess", request);
		String field[] = emp_jobtitle_id.split(",");
		emp_jobtitle_id = "";
		if (field.length != 0) {
			for (int i = 0; i < field.length; i++) {
				if (!field[i].equals("")) {
					emp_jobtitle_id += field[i] + ",";
				}
			}
			emp_jobtitle_id = emp_jobtitle_id.substring(0, emp_jobtitle_id.length() - 1);
		} else {
			emp_jobtitle_id = "0";
		}
		try {
			StrSql = " SELECT emp_id,emp_name,emp_ref_no "
					+ "FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id "
					+ " WHERE  emp_active='1'"
					+ " AND emp_device_fcmtoken != ''"
					+ " AND emp_device_os != ''"
					+ BranchAccess
					+ ExeAccess;
			if (!branch_id.equals("-1")) {
				StrSql = StrSql + " AND emp_branch_id IN ( " + branch_id + " ) ";
			}
			if (!emp_jobtitle_id.equals("")) {
				StrSql = StrSql + " AND emp_jobtitle_id IN ( " + emp_jobtitle_id + " ) ";
			}
			StrSql = StrSql + " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			String Exec = "<select name='dr_executive' id='dr_executive' class='form-control multiselect-dropdown' multiple=\"multiple\">";
			while (crs.next()) {
				Exec = Exec + "<option value=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</option> \n";
			}
			crs.close();
			return Exec + "</select>";
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
