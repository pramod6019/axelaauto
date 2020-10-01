package axela.accounting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class SubGroup_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";

	public String principle_id = "0";
	public String accsubgroup_id = "0";
	public String comp_id = "0";

	public String subgroup = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			Report_LedgerStatement exe = new Report_LedgerStatement();
			principle_id = CNumeric((PadQuotes(request.getParameter("principle_id"))));
			accsubgroup_id = CNumeric((PadQuotes(request.getParameter("accsubgroup_id"))));
			subgroup = PadQuotes(request.getParameter("subgroup"));
			// if (subgroup.equals("yes")) {
			// StrHTML = exe.PopulateSubGroups(principle_id, comp_id);
			// }
			// StrHTML = new Report_Stock_Exe().PopulateColour(item_id);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
