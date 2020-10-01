package axela.inventory;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import cloudify.connect.Connect;

public class Report_Vehicle_Stock_Variant_Summary extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] team_ids, exe_ids, model_ids, item_ids, brand_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", item_id = "", brand_id = "";
	public String StrHTML = "";
	public String region_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "", dr_branch_id = "";
	public String StrSearch = "", enquiry_Model = "", vehstock_item = "";
	public String brandSearch = "", branchSearch = "", modelSearch = "", itemSearch = "", teamSearch = "", exesearch = "";
	public String go = "";
	public String ExeAccess = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_opportunity_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					BranchAccess = BranchAccess.replaceAll("branch_id", "vehstock_branch_id").replace(")", "  OR enquiry_branch_id=0)") + " ";
					// StrSearch = ExeAccess.replaceAll("emp_id", "enquiry_emp_id").replace(")", "  OR enquiry_emp_id=0)") + " ";
					// if (!exe_id.equals("")) {
					// StrSearch += " AND enquiry_emp_id IN (" + exe_id + ")";
					// }
					if (!brand_id.equals("") && branch_id.equals("")) {
						branch_id = ReturnBranchids(brand_id, comp_id);
					}

					if (!branch_id.equals("")) {
						branchSearch += " AND vehstock_branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						modelSearch += " AND item_model_id IN (SELECT item_model_id  FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_model_id IN (" + model_id + "))";
					}

					if (!item_id.equals("")) {
						itemSearch += " AND vehstock_item_id IN (" + item_id + ")";
					}

					if (!team_id.equals("")) {
						teamSearch += " AND so_emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!exe_id.equals("")) {
						exesearch += " AND so_emp_id IN (" + exe_id + ")";
						// + " FROM " + compdb(comp_id) + "axela_emp_branch"
						// + " WHERE emp_branch_id IN (" + branch_id + ")"
						// + " AND " + compdb(comp_id) + "axela_emp_branch.emp_id = " + compdb(comp_id) + "axela_sales_so.so_emp_id)";
						// SOP("strsearch=====" + branchSearch);
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						StrHTML = ListData();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		item_id = RetrunSelectArrVal(request, "dr_variant");
		item_ids = request.getParameterValues("dr_variant");
	}

	protected void CheckForm() {
		msg = "";

	}

	public String ListData() {
		String StrLoc = "";
		String StrCol = "";
		String LocFilter = "";
		String branchid = "";
		StringBuilder Str = new StringBuilder();
		StringBuilder StrRow = new StringBuilder();
		try {
			StrSql = " SELECT vehstocklocation_id, vehstocklocation_name, branch_id, branch_name, "
					+ " COUNT(DISTINCT branch_id) AS 'loccount' "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstocklocation_branch_id"
					+ " WHERE branch_active = '1'";
			// SOP("branch_id==" + branch_id);
			if (!branch_id.equals(""))
			{
				StrSql += " AND vehstocklocation_branch_id IN (" + branch_id + ")";
			}
			StrSql += " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// SOP("StrSql---location--" + StrSqlBreaker(StrSql));
			ResultSet rsloc = processQuery(StrSql, 0);
			while (rsloc.next()) {
				if (!rsloc.getString("branch_id").equals(branchid)) {
					StrLoc += " (SELECT COUNT(DISTINCT so_id) FROM " + compdb(comp_id) + "axela_sales_so "
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id "
							+ " AND soitem_rowcount != 0"
							+ " WHERE so_active = 1 AND so_delivered_date='' "
							+ " AND soitem_item_id = item_id"
							+ " AND so_branch_id = " + rsloc.getString("branch_id") + ""
							+ teamSearch
							+ exesearch
							+ ") AS '" + rsloc.getString("branch_name") + " SOCount',";
					branchid = rsloc.getString("branch_id");
				}
				// StrLoc += " SUM(IF(vehstocklocation_id=" + rsloc.getString("vehstocklocation_id") + " AND vehstock_branch_id=" + rsloc.getString("branch_id") + ",1,0)) as '" +
				// rsloc.getString("vehstocklocation_name")+
				// "',";
				StrLoc += " SUM(IF(vehstock_branch_id=" + rsloc.getString("branch_id") + ",1,0)) as '" + rsloc.getString("branch_name") + "',";
				LocFilter += rsloc.getString("vehstocklocation_id") + ",";

			}
			LocFilter = " AND vehstock_vehstocklocation_id IN (" + LocFilter.substring(0, LocFilter.length() - 1) + ")";
			// SOP("LocFilter-------" + LocFilter);
			StrSql = " SELECT option_id, option_code, option_name, brand_name, "
					+ " COUNT(DISTINCT option_id) AS  'optcount' "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans  ON trans_option_id = option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type  ON optiontype_id = option_optiontype_id"
					+ " INNER JOIN axela_brand  ON brand_id = option_brand_id"
					+ " WHERE 1=1"
					+ " GROUP BY option_id"
					+ " ORDER BY option_code";
			// SOP("StrSql-----option---" + StrSqlBreaker(StrSql));
			ResultSet rscolour = processQuery(StrSql, 0);

			while (rscolour.next()) {
				StrCol += " SUM(IF(trans_option_id=" + rscolour.getString("option_id") + LocFilter + ",1,0)) as '" + rscolour.getString("option_name") + "',";
			}
			StrCol = StrCol.substring(0, StrCol.length() - 1);
			// SOP("StrCol------" + StrCol);
			StrSql = " SELECT item_id, item_name, item_code, " + StrLoc + StrCol
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id "
					+ " AND vehstock_delstatus_id !=6"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans  ON trans_vehstock_id = vehstock_id"
					+ " WHERE item_active = '1'"
					+ " AND item_nonstock = '0'"
					+ " AND item_type_id = '1'"
					// + " AND vehstock_id NOT IN (SELECT so_vehstock_id from " + compdb(comp_id) + "axela_sales_so WHERE so_active = '1' AND so_delivered_date!='') "
					+ modelSearch
					+ itemSearch
					+ " GROUP BY item_id "
					+ " ORDER BY item_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Variant</th>\n");
				branchid = "";
				rsloc.beforeFirst();
				while (rsloc.next()) {
					if (!rsloc.getString("branch_id").equals(branchid)) {
						Str.append("<th>" + rsloc.getString("branch_name") + " SO Count</th>\n");
						branchid = rsloc.getString("branch_id");
					}
					Str.append("<th data-hide=\"phone, tablet\">" + rsloc.getString("branch_name") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone, tablet\">Total SO Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Stock Total</th>\n");
				rscolour.beforeFirst();
				while (rscolour.next()) {
					Str.append("<th data-hide=\"phone, tablet\">" + rscolour.getString("option_code") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone, tablet\">Colours Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> loctotal = new HashMap<>();
				HashMap<String, Integer> branchtotal = new HashMap<>();
				branchid = "";
				rsloc.beforeFirst();
				while (rsloc.next()) {
					for (int i = 1; i <= rsloc.getInt("loccount"); i++) {
						if (!rsloc.getString("branch_id").equals(branchid)) {
							branchtotal.put("'" + rsloc.getString("branch_name") + " SOCount'", 0);
							branchid = rsloc.getString("branch_id");
						}
						loctotal.put(rsloc.getString("branch_name"), 0);

					}
				}

				HashMap<String, Integer> colourstotal = new HashMap<>();
				rscolour.beforeFirst();
				while (rscolour.next()) {
					for (int i = 1; i <= rscolour.getInt("optcount"); i++) {
						colourstotal.put(rscolour.getString("option_name"), 0);
					}
				}
				crs.last();
				int count = 1;
				int sototal = 0;
				int rowsototal = 0;
				int grandsototal = 0;
				int stocktotal = 0;
				int rowstocktotal = 0;
				int grandstocktotal = 0;
				int colourtotal = 0;
				int rowcoltotal = 0;
				int grandcoltotal = 0;
				crs.beforeFirst();
				while (crs.next()) {
					// count++;
					sototal = 0;
					rowsototal = 0;
					stocktotal = 0;
					rowstocktotal = 0;
					colourtotal = 0;
					rowcoltotal = 0;
					StrRow.setLength(0);
					StrRow.append("<tr>");
					StrRow.append("<td valign=top align=center>").append(count).append("</td>");
					StrRow.append("<td valign=top align=left>").append(crs.getString("item_name")).append("</td>");
					branchid = "";
					rsloc.beforeFirst();
					while (rsloc.next()) {
						for (int i = 1; i <= rsloc.getInt("loccount"); i++) {
							if (!rsloc.getString("branch_id").equals(branchid)) {
								sototal += crs.getInt(rsloc.getString("branch_name") + " SOCount");
								StrRow.append("<td align=right>");
								if ((crs.getInt(rsloc.getString("branch_name") + " SOCount") == 0))
								{
									StrRow.append("&nbsp");
								}
								else {
									StrRow.append("" + crs.getInt(rsloc.getString("branch_name") + " SOCount") + "");
								}
								StrRow.append("</td>");

								branchtotal.put("'" + rsloc.getString("branch_name") + " SOCount'",
										branchtotal.get("'" + rsloc.getString("branch_name") + " SOCount'") + crs.getInt(rsloc.getString("branch_name") + " SOCount"));
								branchid = rsloc.getString("branch_id");
							}
							stocktotal += crs.getInt(rsloc.getString("branch_name"));
							StrRow.append("<td align=right>");
							if ((crs.getInt(rsloc.getString("branch_name")) == 0))
							{
								StrRow.append("&nbsp");
							} else {
								StrRow.append("" + crs.getInt(rsloc.getString("branch_name")) + "");
							}
							StrRow.append("</td>");
							loctotal.put(rsloc.getString("branch_name") + "", loctotal.get(rsloc.getString("branch_name") + "") + crs.getInt(rsloc.getString("branch_name")));
						}
					}
					rowstocktotal += stocktotal;
					grandstocktotal += rowstocktotal;

					rowsototal += sototal;
					StrRow.append("<td align=right>");
					if (rowsototal == 0) {
						StrRow.append("&nbsp");
					} else {
						StrRow.append(rowsototal);
					}
					StrRow.append("</td>");
					StrRow.append("<td align=right>");
					if (rowstocktotal == 0) {
						StrRow.append("&nbsp");
					} else {
						StrRow.append(rowstocktotal);
					}
					StrRow.append("</td>");
					rscolour.beforeFirst();
					int optcount = 0;
					while (rscolour.next()) {
						optcount += rscolour.getInt("optcount");
						for (int i = 1; i <= rscolour.getInt("optcount"); i++) {
							colourtotal += crs.getInt(rscolour.getString("option_name"));
							StrRow.append("<td align=right>");
							if ((crs.getInt(rscolour.getString("option_name")) == 0))
							{
								StrRow.append("&nbsp");
							}
							else {
								StrRow.append("" + crs.getInt(rscolour.getString("option_name")) + "");
							}
							StrRow.append("</td>");
							colourstotal.put(rscolour.getString("option_name"), colourstotal.get(rscolour.getString("option_name")) + crs.getInt(rscolour.getString("option_name")));
						}
					}
					grandsototal += sototal;
					rowcoltotal += colourtotal;
					grandcoltotal += rowcoltotal;
					StrRow.append("<td align=right>");

					if (rowcoltotal == 0) {
						StrRow.append("&nbsp");
					} else {
						StrRow.append(rowcoltotal);
					}

					StrRow.append("</td>");
					StrRow.append("</tr>\n");
					if (rowsototal > 0 || rowstocktotal > 0) {
						++count;
						Str.append(StrRow);
					}
				}

				Str.append("<tr>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				branchid = "";
				rsloc.beforeFirst();
				while (rsloc.next()) {
					for (int i = 1; i <= rsloc.getInt("loccount"); i++) {
						if (!rsloc.getString("branch_id").equals(branchid)) {
							Str.append("<td align=right><b>" + branchtotal.get("'" + rsloc.getString("branch_name") + " SOCount'") + "</b></td>");
							branchid = rsloc.getString("branch_id");
						}
						Str.append("<td align=right><b>" + loctotal.get(rsloc.getString("branch_name") + "") + "</b></td>");

					}
				}
				Str.append("<td align=right>").append("<b>").append(grandsototal).append("</b>").append("</td>");
				Str.append("<td align=right>").append("<b>").append(grandstocktotal).append("</b>").append("</td>");

				rscolour.beforeFirst();
				while (rscolour.next()) {
					for (int i = 1; i <= rscolour.getInt("optcount"); i++) {
						Str.append("<td align=right>").append("<b>" + colourstotal.get(rscolour.getString("option_name") + "") + "</b>").append("</td>");
					}
				}

				Str.append("<td align=right>").append("<b>").append(grandcoltotal).append("</b>");
				Str.append("</td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<center><font color=red><b>No Details Found!</b></font></center>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
