package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Item_Check extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String q = "", group_id = "", get_option_type = "";
	public String StrHTML = "", StrSearch = "";
	public String vehstock_id = "", itemmaster_id = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
				get_option_type = PadQuotes(request.getParameter("get_option_type"));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				itemmaster_id = CNumeric(PadQuotes(request.getParameter("itemmaster_id")));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				if (!q.equals("")) {
					StrHTML = SearchItems();
				}
				if (!group_id.equals("0") && get_option_type.equals("yes")) {
					StrHTML = GetOptionType();
				}
			}
		}

	}

	public String GetOptionType() {
		String type_name = "";
		StrSql = "SELECT group_type FROM " + compdb(comp_id) + "axela_inventory_group"
				+ " WHERE group_id = " + group_id + "";
		if (ExecuteQuery(StrSql).equals("1")) {
			type_name = "Default";
		} else if (ExecuteQuery(StrSql).equals("2")) {
			type_name = "All Selected";
		} else if (ExecuteQuery(StrSql).equals("3")) {
			type_name = "Multi Select";
		}
		return type_name;
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " AND (item_id LIKE '%" + q + "%'"
					+ " OR item_name LIKE '%" + q + "%'"
					+ " OR item_code LIKE '%" + q + "%'"
					+ " OR cat_name LIKE '%" + q + "%')";

			StrSql = "SELECT item_id, item_name, item_code, COALESCE(cat_name, '') AS cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
					+ " WHERE item_active = '1' AND item_id != " + itemmaster_id + StrSearch + "";
			if (!vehstock_id.equals("0")) {
				StrSql = StrSql + " AND item_optiontype_id > 0";
			}
			StrSql = StrSql + " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 6";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th data-hide=\"phone\">Category</th>\n");
				Str.append("<th data-hide=\"phone\">Action</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					Str.append("\n<tr valign=top onclick=\"PopulateItem(").append(crs.getString("item_id")).append("," + "'").append(item_name).append("');\">");
					Str.append("<td valign=top align=center>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td valign=top align=left>").append(item_name).append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("cat_name")).append("</td>");
					if (vehstock_id.equals("0")) {
						Str.append("<td valign=top align=center><a href=\"javascript:PopulateItem(").append(crs.getString("item_id"));
						Str.append("," + "'").append(item_name).append("');\">Select</a></td>");
					} else {
						Str.append("<td valign=top ><a href=\"stock-options.jsp?add=yes&item_id=").append(crs.getString("item_id"));
						Str.append("&vehstock_id=").append(vehstock_id).append(" \">Add Options</a>\n");
					}
					Str.append("</tr>");
					Str.append("</tbody>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
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
