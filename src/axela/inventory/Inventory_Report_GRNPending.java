package axela.inventory;
//aJIt 11th October, 2012

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_GRNPending extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public static String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String item_code = "", item_name = "";
	public String StrHTML = "";
	public String dr_location_id = "", branch_id = "";
	public String StrSearch = "";
	public String go = "";
	public String BranchAccess;
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = PadQuotes(request.getParameter("dr_branch"));
				dr_location_id = PadQuotes(request.getParameter("dr_location"));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);

				if (go.equals("Go")) {

					CheckForm();
					if (!branch_id.equals("0")) {
						StrSearch = StrSearch + " and po.voucher_branch_id = " + branch_id;
					}
					if (!dr_location_id.equals("0")) {
						StrSearch = StrSearch + " and po.voucher_location_id = " + dr_location_id;
					}
					if (!item_code.equals("")) {
						StrSearch = StrSearch + " and item_code like '" + item_code + "%'";
					}
					if (!item_name.equals("")) {
						StrSearch = StrSearch + " and item_name like '" + item_name + "%'";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListPendingGRN();
					}
				}

			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		branch_id = PadQuotes(request.getParameter("dr_branch"));
		if (branch_id.equals("")) {
			branch_id = "0";
		}
		dr_location_id = PadQuotes(request.getParameter("dr_location"));
		if (dr_location_id.equals("")) {
			dr_location_id = "0";
		}
		item_code = PadQuotes(request.getParameter("txt_item_code"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (dr_location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		}
	}

	public String ListPendingGRN() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT potrans.vouchertrans_item_id, po.voucher_id, cat_id, cat_name, item_id, item_name,"
				+ " item_code, potrans.vouchertrans_item_id, concat(branch_code, po.voucher_id) AS po_no,"
				+ "  po.voucher_date, po.voucher_ref_no, ouruom.uom_name, ouruom.uom_ratio, supuom.uom_name,"
				+ " supuom.uom_ratio, po.voucher_customer_id, customer_name,"
				+ " (potrans.vouchertrans_alt_qty * supuom.uom_ratio ) AS poqty,"
				+ " potrans.vouchertrans_qty, po.voucher_location_id,"
				+ " COALESCE ((SELECT sum(grntrans.vouchertrans_qty)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans grntrans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher grn ON grn.voucher_id = grntrans.vouchertrans_voucher_id"
				+ " WHERE grntrans.vouchertrans_item_id = potrans.vouchertrans_item_id"
				+ " AND grn.voucher_po_id = po.voucher_id"
				+ " AND grn.voucher_location_id = po.voucher_location_id"
				+ " AND grn.voucher_vouchertype_id = 20),0) AS received,"
				+ " COALESCE ((SELECT sum( poreturntrans.vouchertrans_qty)"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher poreturn"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans poreturntrans ON poreturntrans.vouchertrans_voucher_id = poreturn.voucher_id"
				+ " WHERE poreturntrans.vouchertrans_item_id = potrans.vouchertrans_item_id AND poreturntrans.vouchertrans_voucher_id = po.voucher_id"
				+ " AND poreturn.voucher_location_id = po.voucher_location_id"
				+ " AND poreturn.voucher_vouchertype_id = 24),0) AS returned"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans potrans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = potrans.vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ouruom ON ouruom.uom_id = item_uom_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom supuom ON supuom.uom_id = potrans.vouchertrans_alt_uom_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = potrans.vouchertrans_voucher_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = po.voucher_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = po.voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = po.voucher_customer_id"
				+ " WHERE po.voucher_active = '1'"
				+ " AND po.voucher_vouchertype_id = 12"
				+ " AND po.voucher_branch_id = " + branch_id
				+ " AND po.voucher_location_id = " + dr_location_id
				+ " GROUP BY po.voucher_id,"
				+ " potrans.vouchertrans_item_id"
				+ " HAVING (received - returned) < poqty"
				+ " ORDER BY po.voucher_id,"
				+ " potrans.vouchertrans_item_id";
		// SOP("StrSql==grnpending===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Purchase Order</th>\n");
				Str.append("<th>Supplier</th>\n");
				Str.append("<th data-hide=\"phone\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Supplier Qty</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Supplier Pending</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Conversion Factor</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Our Qty</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Our Pending</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<b><a href=\"inventory-po-list.jsp?po_id=").append(crs.getString("po.voucher_id")).append("\">ID: ").append(crs.getString("po.voucher_id"))
							.append("</a></b><br>PO No.: ")
							.append(crs.getString("po_no")).append("");
					if (!crs.getString("po.voucher_ref_no").equals("0")) {
						Str.append("<br>Ref. No.: ").append(crs.getString("po.voucher_ref_no")).append("");
					}
					Str.append("<br>").append(strToShortDate(crs.getString("po.voucher_date"))).append("</td>");
					Str.append("<td align=left valign=top><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("po.voucher_customer_id")).append("&tag=vendors>")
							.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td valign=top><a href=\"inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">" + "").append(crs.getString("item_name")).append(" (")
							.append(crs.getString("item_code")).append(")</a>");
					Str.append("<br><a href=\"inventory-cat-list.jsp?cat_id=").append(crs.getString("cat_id")).append("\">" + "").append(crs.getString("cat_name")).append("</a>");
					Str.append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("poqty")).append(" ").append(crs.getString("supuom.uom_name")).append("</td>");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("poqty") - crs.getDouble("received") + crs.getDouble("returned"))).append(" ")
							.append(crs.getString("supuom.uom_name"));
					Str.append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("supuom.uom_ratio")).append("</td>");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("poqty"))).append(" ").append(crs.getString("ouruom.uom_name")).append("</td>");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("poqty") - crs.getDouble("received") + crs.getDouble("returned"))).append(" ")
							.append(crs.getString("ouruom.uom_name"));
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<center><font color=red><b>No Items found!</b></font></center>");
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
				Str.append("<select name=\"dr_location\" id=\"dr_location\" class=\"form-control selectbox\">");
				Str.append("<option value = 0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), dr_location_id));
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
