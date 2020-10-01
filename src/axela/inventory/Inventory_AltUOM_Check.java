package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Inventory_AltUOM_Check extends Connect {

	public String comp_id = "";
	public String uom_id = "";
	public String StrSql = "";
	public String StrHTML = "";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				uom_id = PadQuotes(request.getParameter("uom_id"));
				if (!uom_id.equals("")) {
					StrHTML = new Inventory_Item_Update().PopulateAltUOM(comp_id, uom_id);
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
