package axela.service;
//aJIt

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Veh_Duplicate_Status extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public String type = "";
	public String brand_id = "";
	public static String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "", dr_duplicateby_id = "";
	public String branch_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String go = "";
	public Date date = new Date();
	public String SearchURL = "report-veh-duplicate-status.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "duplicate_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand_id")));
				dr_duplicateby_id = CNumeric(PadQuotes(request.getParameter("dr_duplicateby_id")));
				type = PadQuotes(request.getParameter("type"));
				// SOP("type===" + type);

				if (!type.equals("")) {
					ListVehicles(request, response);
				}

				if (dr_duplicateby_id.equals("1") || dr_duplicateby_id.equals("0")) {
					StrHTML = ListVehRegno();
				} else if (dr_duplicateby_id.equals("2")) {
					StrHTML = ListVehChassisno();
				}
				if (StrHTML.equals("")) {
					StrHTML = "<br><b><font color=red>No vehicles found!</font></b>";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListVehRegno() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT veh_reg_no,"
					+ " COALESCE(count(veh_id), 0) AS vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh";
			if (!brand_id.equals("0")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON item_model_id = model_id";
			}
			StrSql += " WHERE 1=1"
					+ " AND veh_reg_no != ''";
			if (!brand_id.equals("0")) {
				StrSql += " AND model_brand_id = " + brand_id;
			}

			StrSql += " GROUP BY veh_reg_no"
					+ " HAVING count(veh_id)>1"
					+ " ORDER BY count(veh_id) DESC ";
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\" style='width: 800px;'>\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-hide=\"phone\">Registration No.</th>\n");
				Str.append("<th data-hide=\"phone\">Count</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					String id = crs.getString("veh_reg_no");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("veh_reg_no")).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\"><a href=").append(SearchURL).append("?type=regno.").append("&value=" + id + " target=_blank>").append(crs.getString("vehcount"))
							.append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");

				// Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget").append("&value=" + id + " target=_blank>")
				// .append(unescapehtml(IndDecimalFormat(deci.format((labour_target))))).append("</a></b></td>\n");

			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListVehChassisno() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT veh_chassis_no,"
					+ " COALESCE(count(veh_id), 0) AS vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh";
			if (!brand_id.equals("0")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON item_model_id = model_id";
			}
			StrSql += " WHERE 1=1";

			if (!brand_id.equals("0")) {
				StrSql += " AND model_brand_id = " + brand_id;
			}

			if (comp_id.equals("1009")) {
				StrSql += " AND veh_chassis_no != ''"
						+ " AND veh_engine_no != ''"
						+ " GROUP BY CONCAT( veh_chassis_no, '-', veh_engine_no )";
			} else {
				StrSql += " AND veh_chassis_no != ''"
						+ " GROUP BY veh_chassis_no";
			}

			StrSql += " HAVING count(veh_id) > 1"
					+ " ORDER BY count(veh_id) DESC ";
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\" style='width: 800px;'>\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-hide=\"phone\">Chassis No.</th>\n");
				Str.append("<th data-hide=\"phone\">Count</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					String id = crs.getString("veh_chassis_no");
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("veh_chassis_no")).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\"><a href=").append(SearchURL).append("?type=chassisno.").append("&value=" + id + " target=_blank>")
							.append(crs.getString("vehcount")).append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateBrand() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1" + BranchAccess
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>All Brands</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(Selectdrop(crs.getInt("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDuplicateBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", dr_duplicateby_id)).append(">Registration No.</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_duplicateby_id)).append(">Chassis No.</option>\n");

		return Str.toString();
	}

	// SetSession("vehstrsql", StrSearch, request);
	// response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));

	public void ListVehicles(HttpServletRequest request, HttpServletResponse response) {
		String value = PadQuotes(request.getParameter("value"));
		String type = PadQuotes(request.getParameter("type"));
		try {
			if (type.equals("regno.")) {
				StrSearch += " AND veh_reg_no = '" + value + "'";
			} else if (type.equals("chassisno.")) {
				StrSearch += " AND veh_chassis_no ='" + value + "'";
			}
			SetSession("vehstrsql", StrSearch, request);
			response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
