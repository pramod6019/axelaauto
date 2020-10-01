package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDriveColor_Model_Check extends Connect {

	public String model_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			model_id = PadQuotes(request.getParameter("model_id")).trim();
			StrHTML = PopulateItem();
		}
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select item_id, item_name "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on  model_id = item_model_id "
					+ " where item_model_id = " + model_id + " and item_type_id=1  order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\" >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				// Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
