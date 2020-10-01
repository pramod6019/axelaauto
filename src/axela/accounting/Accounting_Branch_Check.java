package axela.accounting;
//Bhagwan Singh 24th April, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Accounting_Branch_Check extends Connect {

	public String sales_branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "";
	public String branch_location = "";
	public String config_inventory_location_name = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				sales_branch_id = CNumeric(PadQuotes(request.getParameter("sales_branch_id")));
				config_inventory_location_name = GetSession("config_inventory_location_name", request);
				branch_location = PadQuotes(request.getParameter("branch_location"));
				// SOP("emp_branch_id=="+emp_branch_id);
				if (branch_location.equals("yes")) {
					StrHTML = PopulateInventoryLocation();
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}

	public String PopulateInventoryLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select id=\"dr_location_id\" name=\"dr_location_id\" class=\"form-control\" >\n");
			Str.append("<option value=\"0\">Select " + config_inventory_location_name + "</option>\n");
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + sales_branch_id + "";
			// SOP("Str==PopulateInventoryLocation=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id")).append(">");
				Str.append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
