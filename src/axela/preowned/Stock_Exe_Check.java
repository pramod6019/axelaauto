package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Stock_Exe_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String preowned_model_id = "0";
	public String variant = "";
	public String preowned_branch_id = "0";
	public String branch = "", branch_preowned = "";
	public String comp_id = "0";
	public String brand_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			preowned_model_id = CNumeric((PadQuotes(request.getParameter("preowned_model_id"))));
			variant = PadQuotes(request.getParameter("variant"));
			preowned_branch_id = PadQuotes(request.getParameter("branch_id"));
			branch = PadQuotes(request.getParameter("branch"));
			branch_preowned = PadQuotes(request.getParameter("branch_preowned"));
			brand_id = CNumeric((PadQuotes(request.getParameter("brand_id"))));

			if (preowned_branch_id.length() > 1) {
				preowned_branch_id = CleanArrVal(preowned_branch_id);
			}
			if (variant.equals("yes")) {
				StrHTML = new Preowned_Stock_Status().PopulateVariant(preowned_model_id);
			}
			if (branch.equals("yes")) {
				// StrHTML = new Preowned_Stock_Status().PopulateLocation(comp_id);
			}
			// if (branch_preowned.equals("yes")) {
			// StrHTML = new Preowned_Stock_Status().PopulateBranch(brand_id, comp_id);
			// }
			// else if (upholstery.equals("yes")) {
			// StrHTML = new Report_Stock_Exe().PopulateUpholstery(model_id);
			// } else if (packages.equals("yes")) {
			// StrHTML = new Report_Stock_Exe().PopulatePackage(model_id);
			// }
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
