package axela.inventory;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Stock_Status_Summary extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String BranchAccess;
	public String msg = "";
	public String vehstock_branch_id = "";
	public String delstatus_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_branch")));
			BranchAccess = GetSession("BranchAccess", request);

			try {
				CheckForm();
				if (msg.equals("")) {
					StrHTML = StockDetail();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	public void CheckForm() {
		msg = "";
		try {
			if (vehstock_branch_id.equals("0")) {
				msg = msg + "<br>Select Branch!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String StockDetail() {
		String SqlJoin = "";
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		int penddelnocount = 0;
		int penddelyescount = 0;
		int Totalcount = 0;
		int totalcarcount = 0;
		String StrStatus = "";
		try {

			StrStatus = "";
			StrSql = " SELECT status_name, status_id FROM " + compdb(comp_id) + "axela_vehstock_status WHERE 1 = 1 AND status_id != 0 ORDER BY status_name ";
			ResultSet rsstatus = processQuery(StrSql, 0);
			while (rsstatus.next()) {
				StrStatus = StrStatus + " (SELECT count(vehstock_id) FROM " + compdb(comp_id) + "axela_vehstock "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id AND vehstock_branch_id = " + vehstock_branch_id
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id AND so_active = '1' "
						+ " WHERE item_model_id = model_id"
						+ " AND so_id IS NULL"
						+ " AND vehstock_status_id = " + rsstatus.getString("status_id") + BranchAccess + ") AS penddelnocount" + rsstatus.getString("status_id") + ", "
						+ " (SELECT COUNT(vehstock_id) FROM " + compdb(comp_id) + "axela_vehstock "
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id AND vehstock_branch_id = " + vehstock_branch_id
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
						+ " AND so_active = '1'"
						+ " AND item_nonstock = '0'"
						+ " AND item_active = 1"
						+ " AND item_type_id != 1"
						+ " AND so_delivered_date = '' "
						+ " WHERE item_model_id = model_id"
						+ " AND vehstock_status_id = " + rsstatus.getString("status_id") + BranchAccess + ") AS penddelyescount" + rsstatus.getString("status_id") + ",";
			}
			// to know no of records depending on search
			StrSql = " SELECT " + StrStatus + "  model_name FROM " + compdb(comp_id) + "axela_inventory_item_model ";

			StrSql = StrSql
					+ " WHERE 1 = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("StrSql"+StrSql);
			// SOP("StrSql============" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			Totalcount = 0;
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-toggle=\"true\">Sl.#</th>\n");
			Str.append("<th>Model</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Pending Delivery</th>\n");
			rsstatus.beforeFirst();
			while (rsstatus.next()) {
				Str.append("<th data-hide=\"phone, tablet\">" + rsstatus.getString("status_name") + "</th>\n");
			}
			Str.append("<th data-hide=\"phone, tablet\">Total</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Grand Total</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count = count + 1;
					penddelnocount = 0;
					penddelyescount = 0;
					Str.append("<tr>\n");
					Str.append("<td align=center rowspan=2>" + count + ".</td>\n");
					Str.append("<td align=left rowspan=2><b>" + crs.getString("model_name") + "</b></td>\n");
					Str.append("<td valign=top align=left>No</td>\n");
					rsstatus.beforeFirst();
					while (rsstatus.next()) {
						Str.append("<td valign=top align=right>" + crs.getString("penddelnocount" + rsstatus.getString("status_id")) + "</td>");
						penddelnocount = penddelnocount + crs.getInt("penddelnocount" + rsstatus.getString("status_id"));
					}
					Str.append("<td align=right><b>" + penddelnocount + "</b></td>\n");
					rsstatus.beforeFirst();
					while (rsstatus.next()) {
						penddelyescount = penddelyescount + crs.getInt("penddelyescount" + rsstatus.getString("status_id"));
					}
					Str.append("<td align=right rowspan=2><b>" + (penddelnocount + penddelyescount) + "</b></td>\n");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left>Yes</td>\n");
					rsstatus.beforeFirst();
					while (rsstatus.next()) {
						Str.append("<td valign=top align=right>" + crs.getString("penddelyescount" + rsstatus.getString("status_id")) + "</td>");
					}
					Str.append("<td align=right><b>" + penddelyescount + "</b></td>\n");
					Str.append("</tr>\n");
					Totalcount = Totalcount + penddelnocount + penddelyescount;
				}
				Str.append("<tr align=center>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				Str.append("<td>&nbsp;</td>\n");
				rsstatus.beforeFirst();
				while (rsstatus.next()) {
					Str.append("<td>&nbsp;</td>\n");
				}
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>" + Totalcount + "</b></td>\n");
				Str.append("</tr>\n");
			} else {
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=" + count + "><br><br><br><br><b><center><font color=red>No Model(s) found!</font></b></center><br><br></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
			}
			rsstatus.close();
			crs.close();
			Str.append("</table>\n");
			Str.append("</div>\n");

			// // Delivery Pipeline
			StrSql = " SELECT delstatus_id, delstatus_name, COUNT(vehstock_id) AS vehstockcount "
					+ " FROM " + compdb(comp_id) + "axela_vehstock "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id AND so_active = '1'"
					+ " WHERE 1 = 1"
					+ " AND vehstock_branch_id = " + vehstock_branch_id
					+ " AND so_id IS NULL "
					+ " OR vehstock_id IN (SELECT stock.vehstock_id FROM " + compdb(comp_id) + "axela_vehstock stock "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = stock.vehstock_id "
					+ " WHERE 1 = 1"
					+ " AND so_active = '1'"
					+ " AND so_delivered_date = '')"
					+ " GROUP BY delstatus_id "
					+ " ORDER BY delstatus_rank";
			// SOP("StrSql====2===" + StrSql);
			CachedRowSet crs2 = processQuery(StrSql, 0);
			totalcarcount = 0;
			Str.append("<table class=\"table table-bordered table-hover\">");
			Str.append("<tr><td>\n");
			Str.append("<table class=\"table table-bordered table-hover\">");
			Str.append("<thead><tr>\n");
			Str.append("<th>Delivery Pipeline</th>\n");
			Str.append("<th>Cars</th>\n");
			Str.append("</tr></thead>\n");
			while (crs2.next()) {

				totalcarcount = totalcarcount + crs2.getInt("vehstockcount");
				Str.append("<tr>\n");
				Str.append("<td align=left>" + crs2.getString("delstatus_name") + "</td>\n");
				Str.append("<td align=right><a href=\"report-vehicle-stock-status.jsp?dr_delstatus_id=" + crs2.getString("delstatus_id") + "&dr_vehstock_branch=" + vehstock_branch_id
						+ "\" target=_blank >" + crs2.getString("vehstockcount") + "</a></td>\n");
				Str.append("</tr>\n");
				delstatus_id = crs2.getString("delstatus_id");

			}
			Str.append("<tr>\n");
			Str.append("<td align=right><b>Total:</b></td>\n");
			Str.append("<td align=right><a href=\"report-vehicle-stock-status.jsp?dr_vehstock_branch=" + vehstock_branch_id + "\" target=_blank ><b>" + totalcarcount + "</b></a></td>\n");
			crs2.close();
			Str.append("</tr>\n");
			Str.append("</table>\n");
			Str.append("</td></tr>\n");
			Str.append("</table>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";

		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
