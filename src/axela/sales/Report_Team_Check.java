package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Team_Check extends Connect {

	public String exe_branch_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrHTML = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			exe_branch_id = CNumeric(PadQuotes(request.getParameter("exe_branch_id")));
			StrHTML = PopulateTeam();
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team ";
			if (!CNumeric(exe_branch_id).equals("0")) {
				StrSql += " WHERE team_branch_id=" + exe_branch_id + " ";
			}
			StrSql += " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query in check team check ==========" + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);
			//
			Str.append("<select name=\"dr_team\" id=\"dr_team\" class=\"form-control\" multiple=\"multiple\" size=\"10\" "
					+ " onChange=\"ExeCheck();\" >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(">").append(crs.getString("team_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
