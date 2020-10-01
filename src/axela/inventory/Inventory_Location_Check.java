package axela.inventory;
//aJIt 16th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Location_Check extends Connect {

	public String branch_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			branch_id = PadQuotes(request.getParameter("branch_id"));
			if (!branch_id.equals("")) {
				StrHTML = (PopulateLocation());
			}
		}
	}
	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, IF(location_code != '', CONCAT(location_name, ' (',location_code, ')'), location_name) AS location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location\" id=\"dr_location\"  class=\"form-control selectbox\" onChange=\"document.form1.submit();\">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(">").append(crs.getString("location_name")).append("</option> \n");
			}
			Str.append("</select>");
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
