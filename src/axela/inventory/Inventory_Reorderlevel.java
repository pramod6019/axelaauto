package axela.inventory;
/*saiman 28th june 2012 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Reorderlevel extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String update = "";
	public String RecCountDisplay = "";
	public String updateB = "";
	public String StrSql = "", Strsearch = "";
	public String msg = "";
	public String po_id = "";
	public String stock_reorderlevel = "";
	public int stock_current_qty = 0;
	public Connection conntx = null;
	Statement stmttx = null;
	public String stock_id = "";
	public String testtrans_modified_id = "", testtrans_modified_by = "";
	public String StrHTML = "";
	public int count = 0;
	public String BranchAccess = "";
	public String status = "";
	public String stock_item_id = "";
	public String stock_poitem_id = "";
	public String stock_location_id = "", branch_id = "";
	public String poitem_supplier_qty = "", i = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access, emp_report_access, emp_item_access, emp_reorder_level_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				updateB = PadQuotes(request.getParameter("update_button"));
				status = PadQuotes(request.getParameter("status"));
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				stock_location_id = CNumeric(PadQuotes(request.getParameter("dr_location")));
				if (!stock_location_id.equals("0")) {
					branch_id = ExecuteQuery("SELECT location_branch_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " WHERE location_id = " + stock_location_id + "");
				}
				StrHTML = ListItems(request);
				if (updateB.equals("Update")) {
					CheckPerm(comp_id, "emp_reorder_level_edit", request, response);
					CheckForm(request, response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						UpdateReorderLevel(request);
						response.sendRedirect(response.encodeRedirectURL("inventory-reorderlevel.jsp?status=add&dr_location=" + stock_location_id + "&msg=Reorder Level Updated successfully!"));
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CheckForm(HttpServletRequest request, HttpServletResponse response) {
		msg = "";
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		stock_location_id = CNumeric(PadQuotes(request.getParameter("dr_location")));
		if (stock_location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		int temp = Integer.parseInt(PadQuotes(request.getParameter("count")));
		while (temp > 0) {
			stock_reorderlevel = PadQuotes(request.getParameter("txt_stock_reorderlevel" + temp + ""));
			if (!isNumeric(stock_reorderlevel)) {
				msg = msg + "<br>Stock Reorder Level must be Numeric!";
				break;
			}
			temp--;
		}
	}

	public void UpdateReorderLevel(HttpServletRequest request) {
		count = Integer.parseInt(PadQuotes(request.getParameter("count")));
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			while (count > 0) {
				stock_id = PadQuotes(request.getParameter("stock_id" + count + ""));
				stock_item_id = PadQuotes(request.getParameter("item_id" + count + ""));
				stock_reorderlevel = PadQuotes(request.getParameter("txt_stock_reorderlevel" + count + ""));

				if (!stock_location_id.equals("0") && !stock_reorderlevel.equals("0") && stock_id.equals("0")) {
					stmttx.addBatch("INSERT INTO " + compdb(comp_id) + "axela_inventory_stock"
							+ " (stock_location_id,"
							+ " stock_item_id,"
							+ " stock_current_qty,"
							+ " stock_reorderlevel,"
							+ " stock_entry_id,"
							+ " stock_entry_date)"
							+ " values"
							+ " (" + stock_location_id + ","
							+ " " + stock_item_id + ","
							+ " " + stock_current_qty + ","
							+ " " + stock_reorderlevel + ","
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "')");
				} else if (!stock_location_id.equals("0") && !stock_reorderlevel.equals("0")) {
					stmttx.addBatch("UPDATE " + compdb(comp_id) + "axela_inventory_stock"
							+ " SET"
							+ " vehstock_reorderlevel = " + stock_reorderlevel + ""
							+ " WHERE stock_id = " + stock_id + "");
				}
				count--;
			}
			stmttx.executeBatch();
			conntx.commit();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListItems(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		String StrJoin = "";
		int count = 0;

		StrSql = "SELECT cat_id, COALESCE(location_branch_id,'') AS location_branch_id, cat_name,"
				+ " item_id, item_name, item_code,"
				+ " uom_name, COALESCE(stock_id, '') AS stock_id,"
				+ " COALESCE(stock_reorderlevel, 0) AS stock_reorderlevel";

		StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_item"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
				+ " AND stock_location_id = " + stock_location_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id "
				+ " WHERE 1 = 1"
				+ " AND  item_nonstock = '0'" + BranchAccess
				+ " GROUP BY cat_id, item_id"
				+ " ORDER BY cat_name, item_name";

		StrSql = StrSql + StrJoin;
		// SOP("StrSql=="+StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst() && !stock_location_id.equals("0")) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Category</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Reorder Level</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">UOM</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					stock_reorderlevel = PadQuotes(request.getParameter("txt_stock_reorderlevel" + count + ""));
					stock_item_id = PadQuotes(request.getParameter("stock_item_id" + count + ""));
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=left><a href=\"inventory-cat-list.jsp?cat_id=");
					Str.append(crs.getString("cat_id")).append("\">").append(crs.getString("cat_name")).append("</a>");
					Str.append("</td>");
					Str.append("<td valign=top align=left><a href=\"inventory-item-list.jsp?item_id=");
					Str.append(crs.getString("item_id")).append("\">").append(crs.getString("item_name"));
					if (!crs.getString("item_code").equals("")) {
						Str.append(" (").append(crs.getString("item_code")).append(")");
					}
					Str.append("</a></td>\n");
					Str.append("<td valign=top align=center>");
					Str.append("<input type=text id=txt_stock_reorderlevel").append(count);
					Str.append(" name=txt_stock_reorderlevel").append(count);
					Str.append(" size=10 maxlength=10 onKeyUp=\"javascript:toInteger('txt_stock_reorderlevel");
					Str.append(count).append("')\" class=form-control value=");
					Str.append(crs.getString("stock_reorderlevel")).append(">" + "<input type=hidden value=");
					Str.append(crs.getInt("stock_reorderlevel")).append(" name=stock_reorderlevel");
					Str.append(count).append("><input type=hidden value=").append(crs.getString("stock_id"));
					Str.append(" name=stock_id").append(count).append("><input type=hidden value=");
					Str.append(crs.getInt("item_id")).append(" name=item_id").append(count).append("></td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("uom_name")).append("</td>");
				}
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("<input type=hidden value=").append(count).append(" name=count>\n");
			} else {
				i = "NO";
				Str.append("<center><font color=red><b>No Stock found for this Location!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		if (!branch_id.equals("")) {
			try {

				StrSql = "SELECT location_id, location_name, location_code"
						+ " from " + compdb(comp_id) + "axela_inventory_location"
						+ " where location_branch_id = " + branch_id + ""
						+ " group by location_id"
						+ " order by location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), stock_location_id));
					Str.append(">").append(crs.getString("location_name")).append(" (");
					Str.append(crs.getString("location_code")).append(")");
					Str.append("</option> \n");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
}
