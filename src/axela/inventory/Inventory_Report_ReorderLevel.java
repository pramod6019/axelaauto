package axela.inventory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_ReorderLevel extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "", StrSql = "";
	public String location_id = "", branch_id = "";
	public String BranchAccess;
	public String cat_id = "";
	public String date = "";
	public String msg = "";
	public String go = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access,emp_po_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = PadQuotes(GetSession("BranchAccess", request));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_location_id")));
				SOP("LOCATION ID::"+location_id);
				cat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				go = PadQuotes(request.getParameter("submit_button"));
				date = ReportStartdate();
				if (go.equals("Go")) {
					checkForm();
					if (msg.equals("")) {
						StrHTML = ListData();
					} else {
						StrHTML = "<center><font color='red'><b>" + msg + "</b></font></center>";
					}
				}
				session.setAttribute("reorderStrSearch", StrSearch);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void checkForm() {
		if (branch_id.equals("0")) {
			msg = "Select Branch!<br>";
		} else {
			StrSearch = StrSearch + " and branch_id = " + branch_id + "";
		}

		if (location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		} else {
			StrSearch = StrSearch + " and location_id = " + location_id + "";
		}

		if (!cat_id.equals("0")) {
			StrSearch = StrSearch + " and item_cat_id = " + cat_id + "";
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {

			StrSql = "SELECT"
					+ " item_id, item_code, item_name, uom_name,"
					+ " stock_reorder_auto, stock_reorder_leaddays,"
					+ " stock_reorderlevel, stock_reorderlevel_auto, "
					+ " location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON item_uom_id=uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id =item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON  stock_location_id = location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " WHERE item_active = 1"
					+ " AND item_nonstock = '0'"
					+ " AND item_type_id !=1"
					+ StrSearch
					+ BranchAccess
					+ " group by item_id"
					+ " order by item_name";

			// SOP("StrSql==reorderlevel===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("");
				crs.first();
				Str.append("<center><b>").append(crs.getString("location_name")).append("</b></center>");
				Str.append("");
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><b>Item ID</b></th>\n");
				Str.append("<th data-hide=\"phone\"><b>Item Name</b></th>\n");
				Str.append("<th><b>Item Code</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Auto</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Lead Days</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Reorder Level</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>Auto Reorder Level</b></th>\n");
				Str.append("<th data-hide=\"phone, tablet\"><b>UOM</b></th>\n");
				Str.append("</tr></thead>");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align= center valign=top height=20>").append(count).append("</td>");
					Str.append("<td align= center valign=top>").append(crs.getString("item_id")).append("</td>");
					Str.append("<td align= left valign=top>").append(crs.getString("item_name")).append("</td>");
					Str.append("<td  valign=top align=left>").append(crs.getString("item_code")).append("</td>");
					Str.append("<td align= right valign=top>")
							.append("<input type='checkbox' id='chk_stock_reorder_auto_" + crs.getString("item_id") + "' "
									+ "name='chk_stock_reorder_auto_" + crs.getString("item_id") + "' " + PopulateCheck(crs.getString("stock_reorder_auto")) + " onclick='SubmitFormAndUpdatre("
									+ crs.getString("item_id") + ")'></input>").append("</td>");

					Str.append("<td align= right valign=top>")
							.append("<input type='text' id='txt_stock_reorder_leaddays_" + crs.getString("item_id") + "' "
									+ "name='txt_stock_reorder_leaddays_" + crs.getString("item_id") + "' class='form-control' value='" + crs.getString("stock_reorder_leaddays")
									+ "' size='5' maxlenght='3' onkeyup='SubmitFormAndUpdatre(" + crs.getString("item_id") + ")'></input>").append("</td>");

					Str.append("<td align= right valign=top>").append("<input type='text' id='txt_stock_reorderlevel_" + crs.getString("item_id") + "' "
							+ "name='txt_stock_reorderlevel_" + crs.getString("item_id") + "' class='form-control' value='" + crs.getString("stock_reorderlevel") + "' size='5' maxlenght='3'"
							+ "onkeyup='SubmitFormAndUpdatre(" + crs.getString("item_id") + ")'></input>")
							.append("</td>");

					Str.append("<td align= right valign=top>")
							.append(crs.getString("stock_reorderlevel_auto")).append("</td>");

					Str.append("<td align= left valign=top>").append(crs.getString("uom_name")).append("</td>");
					Str.append("</tr>");
					Str.append("</tbody>\n");
				}
				Str.append("</table>");
				Str.append("</div>");

			} else {
				Str.append("<center><font color=red><b>No Item(s) found!</b></font></center>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void UpdateReorderleve(String comp_id, String item_id, String auto, String leaddays, String reorderlevel, String location_id) {
		StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_stock"
				+ " SET stock_reorder_auto = " + auto + ","
				+ " stock_reorder_leaddays = " + leaddays + ","
				+ " stock_reorderlevel = " + reorderlevel
				+ " WHERE stock_item_id = " + item_id
				+ " AND stock_location_id = " + location_id;
		// SOP("StrSql==update===" + StrSql);
		updateQuery(StrSql);
	}
	public String PopulateLocation(String comp_id, String branch_id, String location_id) {
		StringBuilder Str = new StringBuilder();
		if (!branch_id.equals("")) {
			try {
				StrSql = "SELECT location_id, location_name, location_code"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + branch_id + ""
						+ " GROUP BY location_id"
						+ " ORDER BY location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<select name=dr_vehstock_location_id class=form-control id=dr_vehstock_location_id>");
				Str.append("<option value = 0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), location_id));
					Str.append(">").append(crs.getString("location_name")).append(" (");
					Str.append(crs.getString("location_code")).append(")");
					Str.append("</option> \n");
				}
				Str.append("</select>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
}
