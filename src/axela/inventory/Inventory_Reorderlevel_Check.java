package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Inventory_Reorderlevel_Check extends Connect {

	public String StrSql = "", msg = "";
	public String comp_id = "0";
	public String id = "0", StrHTML = "", StrSearch = "", auto = "0", leaddays = "0", reorderlevel = "0", location_id = "0",
			BranchAccess = "", cat_id = "0", branch_id = "0",
			list = "", location = "", update = "";

	public Inventory_Report_ReorderLevel obj = new Inventory_Report_ReorderLevel();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request) + "";
			StrSearch = session.getAttribute("reorderStrSearch") + "";
			id = CNumeric(PadQuotes(request.getParameter("item_id")));
			auto = CNumeric(PadQuotes(request.getParameter("auto")));
			leaddays = CNumeric(PadQuotes(request.getParameter("leaddays")));
			reorderlevel = CNumeric(PadQuotes(request.getParameter("reorderlevel")));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			list = PadQuotes(request.getParameter("list"));
			update = PadQuotes(request.getParameter("update"));
			location = PadQuotes(request.getParameter("location"));
			obj.comp_id = comp_id;
			obj.BranchAccess = BranchAccess;
			obj.StrSearch = StrSearch;
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				if (msg.equals("")) {
					if (location.equals("yes")) {
						StrHTML = obj.PopulateLocation(comp_id, branch_id, location_id);
					}
					if (update.equals("yes")) {
						obj.UpdateReorderleve(comp_id, id, auto, leaddays, reorderlevel, location_id);
						StrHTML = obj.ListData();
					}
				} else {
					StrHTML = msg;
					// SOP("");
				}
			} else {
				StrHTML = "SignIn ";
			}

		}

	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
