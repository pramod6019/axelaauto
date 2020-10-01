package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Item_Check extends Connect {

	public String StrSql = "";
	public String model_id = "0";
	public String model_brand_id = "0";
	public String comp_id = "0";
	public String StrHTML = "", StrSearch = "";
	public String vehstock_id = "0";
	public String q = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!CNumeric(GetSession("emp_id", request)).equals("0")) {
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				q = PadQuotes(request.getParameter("q"));
				q = q.replaceAll("nbsp", "&");
				if (!vehstock_id.equals("0")) {
					StrSql = "SELECT branch_brand_id "
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_branch_id = branch_id"
							+ " WHERE vehstock_id = " + vehstock_id + "";
					model_brand_id = ExecuteQuery(StrSql);
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

			StrSql = "SELECT option_id, option_code, option_name, optiontype_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
					+ " WHERE option_brand_id = " + model_brand_id
					+ StrSearch + "";

			if (!vehstock_id.equals("0")) {
				StrSql += " AND option_optiontype_id > 0";
			}
			StrSql += " GROUP BY option_id"
					+ " ORDER BY option_name"
					+ " LIMIT 6";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" >\n");
				Str.append("<thead><tr align=\"center\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>Option ID</th>\n");
				Str.append("<th>Option Code</th>\n");
				Str.append("<th>Option Name</th>\n");
				Str.append("<th>Option Type</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					Str.append("\n<tr valign=top onclick=\"PopulateItem(").append(crs.getString("option_id")).append(",'");
					Str.append(crs.getString("option_code")).append("');\">");
					Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("option_id")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("option_code")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("option_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("optiontype_name")).append("</td>\n");
					Str.append("<td valign=\"top\">");
					Str.append("<a href=\"stock-options.jsp?add=yes&option_id=").append(crs.getString("option_id"));
					Str.append("&vehstock_id=").append(vehstock_id).append("\">Add Options</a>\n");
					Str.append("</td>\n");
					Str.append("</tr></tbody>\n");
				}
				Str.append("</table></div>\n");
				crs.close();
			} else {
				Str.append("<b><font color=\"#ff0000\">No Items Found!</font></b>");
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
