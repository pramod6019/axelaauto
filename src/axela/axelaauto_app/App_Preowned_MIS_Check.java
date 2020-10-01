package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class App_Preowned_MIS_Check extends Connect {

	public String branch_id = "", crmtype_id = "", region_id = "";
	public String[] region_ids, branch_ids, model_ids, item_ids, crmdays_ids;
	String[] preowned_emp_id, preowned_team_id;
	public String brand_id = "";
	public String region = "";
	public String branch = "";
	public String[] team_ids, exe_ids;
	public String exe_branch_id = "0";
	public String StrHTML = "", ExeAccess = "", team = "";
	public String executives = "", team_id = "", exe_id = "", StrSql = "";
	public String single = "", exe = "";
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
			executives = PadQuotes(request.getParameter("executives")).trim();
			single = PadQuotes(request.getParameter("single")).trim();
			team = PadQuotes(request.getParameter("team"));
			exe = PadQuotes(request.getParameter("exe"));
			region = PadQuotes(request.getParameter("region"));
			// branches = PadQuotes(request.getParameter("branches")); ///s
			branch = PadQuotes(request.getParameter("branch"));
			exe_branch_id = CNumeric(PadQuotes(request.getParameter("exe_branch_id")));
			// GetValues(request, response);
			if (region.equals("regionpss")) {
				StrHTML = new App_Preownedstock_Search().PopulateRegion(brand_id, comp_id);
			}
			if (branch.equals("branchpss")) {
				StrHTML = new App_Preownedstock_Search().PopulateBranch(brand_id, region_id, comp_id);
			}
		}
	}
	// protected void GetValues(HttpServletRequest request,
	// HttpServletResponse response) throws ServletException, IOException
	// {
	// preowned_team_id = request.getParameterValues("dr_team");
	// preowned_emp_id = request.getParameterValues("dr_executive");
	// }

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
