package axela.inventory;

/**
 * @author Gurumurthy TS 25 FEB 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Vehicle_Stock_Validate extends Connect {

	public String StrHTML = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String StrOrder = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_stock_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			StrOrder = " status_id, model_name, item_name, vehstock_invoice_date";
			StrHTML = StockDetail();
		}

	}

	public String StockDetail() {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select vehstock_id,\n"
					+ " COALESCE(item_code,'') as item_code, vehstock_chassis_no, vehstock_engine_no,\n"
					+ " if(vehstock_invoice_date = '', '99999999999999', vehstock_invoice_date) as vehstock_invoice_date,\n"
					+ " COALESCE(item_name, '') as item_name,\n"
					+ " COALESCE(item_id, 0) as item_id,\n"
					+ " COALESCE(model_id, 0) as model_id,\n"
					+ " COALESCE(model_name, '') as model_name,\n"
					+ " COALESCE(vehstocklocation_name, '') as vehstocklocation_name, vehstock_parking_no, vehstock_item_id, vehstock_invoice_amount, vehstock_ex_price,\n"
					+ " COALESCE(status_id, 0) as status_id,\n"
					+ " COALESCE(status_name, '') as status_name, 2 as pendingdelivery,\n"
					+ " vehstock_intransit_damage, vehstock_blocked, vehstock_invoice_no, vehstock_rectification_date,\n"
					+ " COALESCE(delstatus_name, '') as delstatus_name,\n"
					+ " CONCAT(branch_name, ' (', branch_code, ')') as branchname, branch_id,\n"
					+ " COALESCE(option_name ,'') as colour\n"
					+ " FROM " + compdb(comp_id) + "axela_vehstock\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on vehstock_branch_id = branch_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = vehstock_item_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus on delstatus_id = vehstock_delstatus_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_status on status_id = vehstock_status_id and status_id != 0\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans on trans_vehstock_id = vehstock_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option on option_id = trans_option_id and option_optiontype_id = 1 \n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location on vehstocklocation_id = vehstock_vehstocklocation_id\n"
					+ " WHERE"
					+ " item_name is null or model_name is null or vehstock_delstatus_id = 0 or vehstock_status_id = 0 or option_name is null or vehstocklocation_id = ''"
					+ " AND item_nonstock = '0'"
					+ " AND item_active = 1"
					+ " AND item_type_id !=1"
					+ " GROUP BY vehstock_id"
					+ " ORDER BY " + StrOrder + "";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			int count = 0;
			String blocked;
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Stock ID</th>\n");
				Str.append("<th data-hide=\"phone\">Item</th>\n");
				Str.append("<th data-hide=\"phone\">Item Code</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Colour</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" >Chassis No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Engine No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Location</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Park. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Invoice No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Invoice Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Car Cost</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Blocked</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Nature of Damage</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					if (crs.getString("status_id").equals("1")) {
						bgcol = "#dbebff";
					} else if (crs.getString("status_id").equals("2")) {
						bgcol = "#ffdfdf";
					} else if (crs.getString("status_id").equals("3")) {
						bgcol = "#caffd8";
					} else if (crs.getString("status_id").equals("4")) {
						bgcol = "#eeeeee";
					} else if (crs.getString("status_id").equals("5")) {
						bgcol = "#cccccc";
					}
					Str.append("<tr bgcolor=").append(bgcol).append(">\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\">");
					Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\">");
					Str.append(crs.getString("vehstock_id")).append("</a>");
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"LEFT\">");
					if (!crs.getString("item_name").equals("")) {
						Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
						Str.append(crs.getString("item_name")).append("</a>");
					}
					Str.append("&nbsp;</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\">").append(crs.getString("item_code"));
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"left\">").append(crs.getString("colour"));
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\">").append(crs.getString("vehstock_chassis_no"));
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"center\">").append(crs.getString("vehstock_engine_no"));
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"left\">").append(crs.getString("vehstocklocation_name"));
					Str.append("</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"right\">");
					if (!crs.getString("vehstock_parking_no").equals("0")) {
						Str.append(crs.getString("vehstock_parking_no"));
					}
					Str.append("&nbsp;</td>\n");
					Str.append("<td height=\"20\" valign=\"top\" align=\"right\">").append(crs.getString("vehstock_invoice_no"));
					Str.append("</td>\n");
					Str.append("<td height=20 valign=top align=center>");
					if (!crs.getString("vehstock_invoice_date").equals("99999999999999")) {
						Str.append(strToShortDate(crs.getString("vehstock_invoice_date"))).append("<br>");
						Str.append(Math.round(getDaysBetween(crs.getString("vehstock_invoice_date"), ToShortDate(kknow())))).append(" Days");
					}
					Str.append("&nbsp;</td>\n");
					Str.append("<td height=20 valign=top align=right>").append(IndFormat(crs.getString("vehstock_invoice_amount")));
					Str.append("</td>\n");
					Str.append("<td height=20 valign=top align=left>").append(crs.getString("delstatus_name"));
					Str.append("</td>\n");
					Str.append("<td height=20 valign=top align=left>").append(crs.getString("status_name"));
					if (crs.getString("vehstock_blocked").equals("1")) {
						blocked = "Yes";
					} else {
						blocked = "No";
					}
					Str.append("</td>\n");
					Str.append("<td height=20 valign=top align=left>").append(blocked);
					Str.append("</td>\n");
					Str.append("<td height=20 valign=top align=left>").append(crs.getString("vehstock_intransit_damage"));
					if (!crs.getString("vehstock_rectification_date").equals("")) {
						Str.append("<br>Rectification Date: ").append(strToShortDate(crs.getString("vehstock_rectification_date")));
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
			} else {
				Str.append("<tr align=\"center\">\n");
				Str.append("<td><br><br><br><br><b><font color=\"red\">No Stock(s) found!</font></b><br><br></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
			}
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
