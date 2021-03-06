package axela.sales;
//aJIt 20th June, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Stock_Exe_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String model_id = "0";
	public String principle_id = "0";
	public String branch_id = "0";
	public String comp_id = "0";
	public String item_id = "0";
	public String item = "", model = "", branch = "", location = "";
	public String colour = "";
	public String team = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		Report_Stock_Exe exe = new Report_Stock_Exe();
		if (!comp_id.equals("0")) {
			model_id = CNumeric((PadQuotes(request.getParameter("model_id"))));
			item_id = CNumeric((PadQuotes(request.getParameter("item_id"))));
			principle_id = CNumeric((PadQuotes(request.getParameter("principle_id"))));
			branch_id = CNumeric((PadQuotes(request.getParameter("branch_id"))));
			model = PadQuotes(request.getParameter("model"));
			item = PadQuotes(request.getParameter("item"));
			colour = PadQuotes(request.getParameter("colour"));
			team = PadQuotes(request.getParameter("team"));
			branch = PadQuotes(request.getParameter("branch"));
			location = PadQuotes(request.getParameter("location"));

			if (branch.equals("yes")) {
				StrHTML = exe.PopulateBranch(principle_id, comp_id);
			}
			if (item.equals("yes")) {
				StrHTML = exe.PopulateItem(model_id, comp_id);
			}

			if (colour.equals("yes")) {
				StrHTML = exe.PopulateColour(principle_id, branch_id, comp_id);
			}
			if (team.equals("yes")) {
				StrHTML = exe.PopulateTeam(principle_id, branch_id, comp_id);
			}
			if (model.equals("yes")) {
				StrHTML = exe.PopulateModel(principle_id, branch_id, comp_id);
			}
			if (location.equals("yes")) {
				StrHTML = exe.PopulateLocation(principle_id, branch_id, comp_id);
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
