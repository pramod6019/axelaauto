package axela.inventory;
//aJIt 23rd july
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Check extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String vehstock_item_id = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			vehstock_item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
			// SOP("vehstock_id--"+vehstock_item_id);
			if (!vehstock_item_id.equals("0")) {
				StrHTML = ListItemColor();
			}
		}

	}

	public String ListItemColor() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT colour_id, CONCAT(colour_name,' (',colour_code,')') as colour_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_colour"
					+ " WHERE colour_item_id = " + vehstock_item_id
					+ " GROUP BY colour_id"
					+ " ORDER BY colour_name";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_stock_colour_id class=selectbox id=dr_stock_colour_id>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("colour_id")).append("");
				Str.append(">").append(crs.getString("colour_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
