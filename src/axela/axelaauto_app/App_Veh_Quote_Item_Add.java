package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Veh_Quote_Item_Add extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String msg = "", status = "Add";
	public String option_group_id = "0";
	public String itemmaster_id = "0", configItems = "";;
	StringBuffer configItemsBuffer = new StringBuffer();
	public String line = "";
	public String itemadd = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// CheckPerm(comp_id, "emp_sales_order_cancel", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				itemmaster_id = PadQuotes(request.getParameter("itemmaster_id"));

			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateGroups() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT group_id, group_name"
				+ " FROM " + compdb(comp_id) + "axela_inventory_group";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("group_id"), option_group_id));
				Str.append(">").append(crs.getString("group_name")).append("</option>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
