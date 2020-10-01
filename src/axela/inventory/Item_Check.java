package axela.inventory;
//aJIt 23rd july
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Check extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String brand_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String q = "", model = "";
	public Inventory_Report_PriceBook pricebook = new Inventory_Report_PriceBook();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			model = PadQuotes(request.getParameter("model"));
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				// ///////// Search items
				if (!q.equals("") && !q.equals("0")) {
					StrHTML = ListPrepaidItems();
				}
			} else {
				StrHTML = "Session Expired!";
			}
			if (!brand_id.equals("0") && model.equals("yes"))
			{
				StrHTML = pricebook.PopulateModel(comp_id, brand_id);
			}
		}

	}
	public String ListPrepaidItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " and (item_id like '%" + q + "%'"
					+ " or item_name like '%" + q + "%'"
					+ " or item_code like '%" + q + "%'"
					+ ")";

			StrSql = "SELECT item_id, item_name, item_code,"
					+ " item_loyaltycard_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " WHERE item_active = '1'"
					+ " and item_loyaltycard_id = 0 " + StrSearch
					+ " group by item_id "
					+ " order by item_name"
					+ " limit 6";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th>Qty</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					Str.append("\n<tr>");
					Str.append("\n<td valign=top align=left>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td valign=top align=left>").append(item_name).append("</td>");
					Str.append("<td valign=top align=center><input type=\"text\" id=\"").append(crs.getString("item_id")).append("\" name=\"txt_item\"></td>");
					Str.append("<td valign=top align=center><a href=\"javascript:AddPrepaidItem(").append(crs.getString("item_id")).append(");\">Add Item</a></td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
				crs.close();
			} else {
				Str.append("<b><font color=#ff0000>No Items Found!</font></b>");
			}

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
