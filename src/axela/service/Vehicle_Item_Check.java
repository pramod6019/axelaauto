package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Item_Check extends Connect {

	public String StrSql = "";
	public String model_id = "0", branch_id = "", model = "";
	public String model_brand_id = "0";
	public String StrHTML = "", StrSearch = "";
	public String veh_id = "";
	public String comp_id = "0";
	public String q = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				branch_id = PadQuotes(request.getParameter("branch_id"));
				branch_id = CleanArrVal(branch_id);
				model = PadQuotes(request.getParameter("model"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				if (!veh_id.equals("0")) {
					StrSql = "SELECT item_model_id FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_variant_id = item_id"
							+ " WHERE veh_id = " + veh_id + "";
					model_id = ExecuteQuery(StrSql);
					model_brand_id = ExecuteQuery("SELECT model_brand_id from " + compdb(comp_id) + "axela_inventory_item_model"
							+ " WHERE model_id = " + model_id);
					// SOP("model_brand_id----------------------" +
					// model_brand_id);
				}
				if (!branch_id.equals("") && model.equals("yes")) {
					StrHTML = new Report_PsfFollowup_Analysis().PopulateModels(branch_id, comp_id);
				}
				if (!q.equals("")) {
					StrHTML = SearchItems();
				}
			}
		}
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSearch = " AND (option_id like '%" + q + "%'"
					+ " OR option_code like '%" + q + "%'"
					+ " OR option_name like '%" + q + "%')";

			StrSql = "SELECT option_id, option_code, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_brand_id = " + model_brand_id
					+ StrSearch + "";

			if (!veh_id.equals("0")) {
				StrSql = StrSql + " AND option_optiontype_id > 0";
			}
			StrSql = StrSql + " GROUP BY option_id"
					+ " ORDER BY option_name"
					+ " LIMIT 6";
			// SOP("StrSql12--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				/*
				 * Str.append( "<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">" ); Str.append("<tr align=center>\n");
				 */
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Item ID</th>\n");
				Str.append("<th>Item Name</th>\n");
				Str.append("<th data-hide=\"phone\">Category</th>\n");
				Str.append("<th data-hide=\"phone\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("\n<tr valign=top >");
					Str.append("<td valign=top align=center>").append(crs.getString("option_id")).append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("option_code")).append("</td>");
					Str.append("<td valign=top align=left>").append(crs.getString("option_name")).append("</td>");
					Str.append("<td valign=top ><a href=\"vehicle-options.jsp?add=yes&option_id=").append(crs.getString("option_id")).append("&veh_id=").append(veh_id).append(" \">Add Options</a>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs.close();
			} else {
				Str.append("<b><font color=#ff0000>No Items Found!</font></b>");
			}
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
