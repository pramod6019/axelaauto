// Sangita 22nd may 2013
// Added Executive Summary() & Modify - Ved Prakash (26th August 2013)
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "", StrHTML1 = "";
	public String comp_id = "0";
	public String dr_branch_id = "0";
	public String[] brand_ids, region_ids, branch_ids, type_ids;
	public String brand_id = "", region_id = "", branch_id = "", type_id = "";
	public Executive_Check execheck = new Executive_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
				// dr_branch_id = RetrunSelectArrVal(request, "dr_branch");
				brand_id = RetrunSelectArrVal(request, "dr_principal");
				brand_ids = request.getParameterValues("dr_principal");
				region_id = RetrunSelectArrVal(request, "dr_region");
				region_ids = request.getParameterValues("dr_region");
				type_id = RetrunSelectArrVal(request, "dr_type");
				type_ids = request.getParameterValues("dr_type");
				// SOP("region_ids-----------" + region_ids);
				// SOP("region_id-----------" + region_id);
				branch_id = RetrunSelectArrVal(request, "dr_branch");
				branch_ids = request.getParameterValues("dr_branch");
				if (!brand_id.equals("")) {
					StrSearch += " AND branch_brand_id in (" + brand_id + ") ";
				}
				if (!region_id.equals("")) {
					StrSearch += " AND branch_region_id in (" + region_id + ") ";
				}
				if (!branch_id.equals("")) {
					StrSearch = StrSearch += " AND branch_id in (" + branch_id + ") ";;
				}
				if (!type_id.equals("")) {
					StrSearch += " AND branch_branchtype_id in (" + type_id + ") ";
				}

				StrHTML = ExecutiveSummary(request);
				StrHTML1 = ExecutiveStatus();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ExecutiveStatus() {
		String count = "";
		int grandtotal = 0, grandactive = 0, grandinactive = 0;
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = " SELECT COUNT(DISTINCT(" + compdb(comp_id) + "axela_emp_branch.emp_id)) as 'total', "
					+ " COUNT(DISTINCT case when " + compdb(comp_id) + "axela_emp.emp_active = 1 then " + compdb(comp_id) + "axela_emp.emp_id end) as 'active',"
					+ " COUNT(DISTINCT case when " + compdb(comp_id) + "axela_emp.emp_active = 0 then " + compdb(comp_id) + "axela_emp.emp_id end) as 'inactive'"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_branch ON " + compdb(comp_id) + "axela_emp_branch.emp_id = " + compdb(comp_id) + "axela_emp.emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + compdb(comp_id) + "axela_emp_branch.emp_branch_id"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_branch_id = 0"
					+ " AND " + compdb(comp_id) + "axela_emp_branch.emp_id != 1"
					+ StrSearch
					+ " UNION "
					+ " SELECT COUNT(DISTINCT emp_id) as 'total',"
					+ " SUM(IF(emp_active = 1, 1, 0)) as 'active',"
					+ " SUM(IF(emp_active = 0, 1, 0)) as 'inactive'"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE emp_branch_id != 0 and emp_id != 1 "
					+ StrSearch;

			// SOP("StrSql---------ExecutiveStatus-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th >&nbsp;</th>\n");
				Str.append("<th data-toggle=\"true\">Total</th>\n");
				Str.append("<th data-hide=\"phone\">Active</th>\n");
				Str.append("<th data-hide=\"phone\">Inactive</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					grandtotal += crs.getInt("total");
					grandactive += crs.getInt("active");
					grandinactive += crs.getInt("inactive");
				}
				Str.append("<td><b>Executives<b>:</td>\n");
				Str.append("<td align=right><b>").append(grandtotal).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandactive).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandinactive).append("</b></td>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ExecutiveSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("");
			StrSql = "(SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " 'All Branches' branch_name, 1 AS rank "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1 "
					+ " AND emp_id != 1 "
					+ " AND emp_all_branches = 1)"
					+ " UNION "
					+ " (SELECT COALESCE(" + compdb(comp_id) + "axela_emp.emp_id, 0) as emp_id, "
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COALESCE('Head Office', '') as branch_name, 2 AS rank "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_branch ON " + compdb(comp_id) + "axela_emp_branch.emp_id = " + compdb(comp_id) + "axela_emp.emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + compdb(comp_id) + "axela_emp_branch.emp_branch_id"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_branch_id = 0"
					+ " AND " + compdb(comp_id) + "axela_emp_branch.emp_id != 1"
					+ " AND emp_active  = 1"
					+ StrSearch + ")"
					+ " UNION "
					+ " (SELECT emp_id,  CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " branch_name, 3 AS rank"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE emp_branch_id != 0 and emp_id != 1"
					+ " AND emp_active  = 1"
					+ StrSearch + ")"
					+ " ORDER BY rank, branch_name, emp_name";

			// SOP("StrSql----ExecutiveSummarcy-------" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			Str.append("<table class=\"table table-bordered\">\n");
			if (crs.isBeforeFirst()) {
				Str.append("<tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th><b>Executive</th>\n");
				Str.append("<th><b>Branch</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td>").append("<a href=\"../portal/executive-list.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append("\">").append(crs.getString("emp_name"));
					Str.append("</a></td>\n");
					Str.append("<td>").append(crs.getString("branch_name")).append("</td>\n");
					Str.append("</tr>\n");
				}
			} else {
				Str.append("<br><br><font color=red><b>No Executives(s) found!</b></font>");
			}
			Str.append("</table>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
