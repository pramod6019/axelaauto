package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Dash_Check extends Connect {

	public String exe_branch_id = "";
	public String multiple = "";
	public String team_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String str = "";
	public String branch_id;
	public String ExeAccess = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			ExeAccess = GetSession("ExeAccess", request);
			exe_branch_id = PadQuotes(request.getParameter("exe_branch_id")).trim();
			team_id = PadQuotes(request.getParameter("team_id")).trim();
			multiple = PadQuotes(request.getParameter("multiple")).trim();
			StrHTML = PopulateSalesExecutives();
		}
	}

	public String PopulateSalesExecutives() {
		try {
			String StrSql = "";
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales='1' ";
			if (!exe_branch_id.equals("")) {
				StrSql = StrSql + "AND  (emp_branch_id = " + exe_branch_id + " OR emp_id = 1"
						+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
						+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
						+ " AND empbr.emp_branch_id = " + exe_branch_id + ")) ";
			}
			StrSql = StrSql + ExeAccess + "";
			// SqlStr = " SELECT emp_id, CONCAT(emp_name, ' (',emp_ref_no,')') AS emp_name"
			// + " FROM " + compdb(comp_id) + "axela_emp "
			// + " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
			// + " WHERE 1=1 AND emp_active='1' and emp_sales='1' ";
			// if (!exe_branch_id.equals("")) {
			// SqlStr = SqlStr + " AND (emp_branch_id=0 OR emp_branch_id=" + exe_branch_id + ")";
			// }
			if (!team_id.equals("")) {
				if (team_id.endsWith(",")) {
					team_id = team_id.substring(0, team_id.length() - 1);
				}
				StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			// SOP("Campaign_dash_check query in PopulateExecutive" + SqlStr);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (multiple.equals("yes")) {
				Str.append("<select name=dr_executive id=dr_executive class=\"form-control\" multiple=\"multiple\" size=10 style=\"width:250px\">");
			} else {
				Str.append("<select name=dr_executive id=dr_executive class=\"form-control\"><option value = 0>Select</option>\n");
			}
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("emp_id") + ">" + (crs.getString("emp_name")) + "</option> \n");
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
