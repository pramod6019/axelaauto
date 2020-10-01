package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Brochure_Check extends Connect {

	public String brochure_model_id = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrHTML = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			brochure_model_id = PadQuotes(request.getParameter("dr_model")).trim();
			StrHTML = PopulateItem();
		}
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		// SOP("item_id==="+item_id);
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id= item_model_id"
					+ " ORDER BY item_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in check=="+StrSql);
			// Str.append("<option value=\"0\">General</option>");
			// Str.append("<option value=\"0\" ").append(StrSelectdrop("0", item_id)).append(">General</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
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
