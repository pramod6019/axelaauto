package axela.inventory;
//aJIt 10th October, 2012

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_ItemMaster extends Connect {

	public String StrHTML = "";
	public String StrSearch = "", StrSql = "";
	public String cat_id = "";
	public String date = "";
	public String comp_id = "0";
	public String msg = "", go = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				date = strToShortDate(ToLongDate(kknow()));
				cat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("Go")) {
					if (!cat_id.equals("0") && !cat_id.equals("")) {
						StrSearch = " and item_cat_id = " + cat_id + "";
					} else {
						msg = "Select Category!";
					}
					if (msg.equals("")) {
						StrHTML = ItemDetails(request);
					} else {
						StrHTML = "<center><font color='red'><b>" + msg + "</b></font></center>";
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String ItemDetails(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			StrSql = "SELECT item_id, item_name, item_code, item_cat_id, item_small_desc, item_big_desc,"
					+ " if(item_perishable=1,'Yes','No') as item_perishable, uom_name, item_eoq,"
					+ " cat_id, cat_name"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " inner join " + compdb(comp_id) + "axela_inventory_cat_pop on cat_id = item_cat_id"
					+ " inner join " + compdb(comp_id) + "axela_inventory_uom on item_uom_id = uom_id"
					+ " where item_nonstock = '0'"
					+ " AND item_active = '1'"
					+ " AND item_type_id !=1"
					+ StrSearch
					+ " group by item_id"
					+ " order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				crs.first();
				Str.append("<table class=\"table\">");
				Str.append("<tr><td align=center><b>").append(crs.getString("cat_name")).append("</b></td></tr>");
				Str.append("<tr><td align=center>");
				crs.beforeFirst();
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th align=center><b>Item ID</b></th>\n");
				Str.append("<th align=center><b>Item Name</b></th>\n");
				Str.append("<th align=center><b>Item Code</b></th>\n");
				Str.append("<th align=center><b>Small Description</b></th>\n");
				Str.append("<th align=center><b>Big Description</b></th>\n");
				Str.append("<th align=center><b>Perishable</b></th>\n");
				Str.append("<th align=center><b>EOQ</b></td>\n");
				Str.append("<th align=center><b>UOM</b></td>\n");
				Str.append("</tr></thead>");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>");
					Str.append("<td align=center valign=top>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td align=left valign=top>").append(crs.getString("item_name")).append("</td>");
					Str.append("<td align=left valign=top>").append(crs.getString("item_code")).append("</td>");
					Str.append("<td align=left valign=top>").append(crs.getString("item_small_desc")).append("</a></td>");
					Str.append("<td align=left valign=top>").append(crs.getString("item_big_desc")).append("</td>");
					Str.append("<td align=center valign=top>").append(crs.getString("item_perishable")).append("</td>");
					Str.append("<td align=right valign=top>").append(crs.getString("item_eoq")).append("</td>");
					Str.append("<td align=left valign=top>").append(crs.getString("uom_name")).append("</td>");
					Str.append("</tr></tbody>");
				}

			} else {
				Str.append("<center><font color=red><b>No Items Found!</b></font></center></td></tr>");
			}
			crs.close();
			Str.append("</table>");
			Str.append("</table>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
