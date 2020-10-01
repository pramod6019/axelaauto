package axela.inventory;
//aJIt 10th October, 2012

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_GRN extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String item_code = "", item_name = "", supplier_id = "";
	public String StrHTML = "";
	public String dr_location_id = "", branch_id = "";
	public String StrSearch = "";
	public String BranchAccess;
	public String go = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access,emp_grn_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = PadQuotes(request.getParameter("dr_branch"));
				dr_location_id = PadQuotes(request.getParameter("dr_location"));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if (branch_id.equals("0")) {
						msg = msg + "<br>Select Branch!";
					}
					if (!branch_id.equals("0")) {
						StrSearch = StrSearch + " and po.voucher_branch_id = " + branch_id;
					}
					if (!dr_location_id.equals("0")) {
						StrSearch = StrSearch + " and grn.voucher_location_id = " + dr_location_id;
					}
					if (!item_code.equals("")) {
						StrSearch = StrSearch + " and item_code like '" + item_code + "%'";
					}
					if (!item_name.equals("")) {
						StrSearch = StrSearch + " and item_name like '" + item_name + "%'";
					}
					if (!supplier_id.equals("")) {
						StrSearch = StrSearch + " and po.voucher_customer_id like '" + supplier_id + "%'";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListData();
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
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
		supplier_id = PadQuotes(request.getParameter("txt_supplier_id"));
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}

		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT COALESCE(po.voucher_id, '') AS po_voucher_id, COALESCE(concat(branch_code,po.voucher_id),'') AS po_no,"
				+ " concat(branch_name,' (',branch_code,')') branch_name, COALESCE(po.voucher_date,'') AS po_voucher_date, po.voucher_delivery_date,"
				+ " COALESCE(po.voucher_ref_no,'')AS po_voucher_ref_no, po.voucher_authorize_time, po.voucher_authorize_id, COALESCE(po.voucher_customer_id,'') AS po_voucher_customer_id,"
				+ " po.voucher_amount, customer_name, voucherclass_id, vouchertype_id, po.voucher_active, grn.voucher_id, COALESCE(grn.voucher_delivery_date,'') AS grn_voucher_delivery_date,"
				+ " grntrans.vouchertrans_qty, cat_name, cat_id, item_name, item_id, item_code,"
				+ " COALESCE(ouruom.uom_name,'') AS ouruom_uom_name, COALESCE(grntrans.vouchertrans_qty,'') AS grntrans_vouchertrans_qty, COALESCE(po.voucher_delivery_date,'') AS po_voucher_delivery_date, COALESCE(supuom.uom_name,'') AS supuom_uom_name, potrans.vouchertrans_alt_qty,"
				+ " ouruom.uom_ratio, COALESCE(supuom.uom_ratio,'') AS supuom_uom_ratio,"
				+ " potrans.vouchertrans_qty"
				+ " from "
				+ compdb(comp_id) + "axela_acc_voucher grn"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans grntrans ON grntrans.vouchertrans_voucher_id = grn.voucher_id"
				+ " inner join " + compdb(comp_id) + "axela_acc_voucher po on po.voucher_id = grn.voucher_po_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = grntrans.vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = po.voucher_id"
				+ " INNER JOIN  axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id "
				+ " inner join " + compdb(comp_id) + "axela_inventory_cat_pop on cat_id = item_cat_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_uom ouruom on ouruom.uom_id = item_uom_id"
				+ " inner join " + compdb(comp_id) + "axela_acc_voucher_trans potrans ON potrans.vouchertrans_item_id = grntrans.vouchertrans_item_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_uom supuom on supuom.uom_id = potrans.vouchertrans_alt_uom_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = po.voucher_customer_id"
				+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = po.voucher_branch_id"
				+ " where grn.voucher_entry_date >= '" + starttime + "'"
				+ " and grn.voucher_entry_date < '" + endtime + "'"
				+ StrSearch
				+ BranchAccess + ""
				+ " GROUP BY grn.voucher_id";
		// SOP("strsql==grn===" + StrSqlBreaker(StrSql));
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
				Str.append("<th data-hide=\"phone\">Supplier Qty</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Conversion Factor</th>\n");
				Str.append("<th data-hide=\"phone\">Our Qty</th>\n");
				Str.append("<th data-hide=\"phone\">Delivery Date</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tbody>\n");
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<b><a href=\"../accounting/voucher-list.jsp?voucherclass_id=").append(crs.getString("voucherclass_id=")).append("&vouchertype_id")
							.append(crs.getString("vouchertype_id"))
							.append("</a></b><br>PO No.: ")
							.append(crs.getString("po_no")).append("");
					if (!crs.getString("po_voucher_ref_no").equals("0")) {
						Str.append("<br>Ref. No.: ").append(crs.getString("po_voucher_ref_no")).append("");
					}
					Str.append("<br>").append(strToShortDate(crs.getString("po_voucher_date"))).append("</td>");
					Str.append("<td align=left valign=top><a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("po_voucher_customer_id")).append("&tag=vendors>")
							.append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td valign=top><a href=\"inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">" + "").append(crs.getString("item_name")).append(" (")
							.append(crs.getString("item_code")).append(")</a>");
					Str.append("<br><a href=\"inventory-cat-list.jsp?cat_id=").append(crs.getString("cat_id")).append("\">" + "").append(crs.getString("cat_name")).append("</a>");
					Str.append("</td>");
					Str.append("<td valign=top align=right>").append(df.format((crs.getDouble("grntrans_vouchertrans_qty") / crs.getDouble("supuom_uom_ratio")))).append(" ")
							.append(crs.getString("supuom_uom_name")).append("</td>");
					Str.append("<td valign=top align=right>").append(""/* crs.getString("poitem_convfactor") */).append("</td>");
					Str.append("<td valign=top align=right>").append(df.format(crs.getDouble("grntrans_vouchertrans_qty"))).append(" ").append(crs.getString("ouruom_uom_name")).append("</td>");
					Str.append("<td valign=top align=center>ETA: ").append(strToShortDate(crs.getString("po_voucher_delivery_date")));
					if (Math.round(getDaysBetween(crs.getString("po_voucher_delivery_date"), crs.getString("grn_voucher_delivery_date"))) < 0) {
						Str.append("<br><b><font color=blue>GRN: ").append(strToShortDate(crs.getString("grn_voucher_delivery_date"))).append("<br>Delay by: ")
								.append(Math.round(getDaysBetween(crs.getString("po_voucher_delivery_date"), crs.getString("grn_voucher_delivery_date")))).append("</font></b>");
					}
					if (Math.round(getDaysBetween(crs.getString("po_voucher_delivery_date"), crs.getString("grn_voucher_delivery_date"))) > 0) {
						Str.append("<br><b><font color=red>GRN: ").append(strToShortDate(crs.getString("grn_voucher_delivery_date"))).append("<br>Delay by: ")
								.append(Math.round(getDaysBetween(crs.getString("po.voucher_delivery_date"), crs.getString("grn_voucher_delivery_date")))).append(" </font></b>");
					}
					if (Math.round(getDaysBetween(crs.getString("po_voucher_delivery_date"), crs.getString("grn_voucher_delivery_date"))) == 0) {
						Str.append("<br><b>GRN: ").append(strToShortDate(crs.getString("grn_voucher_delivery_date"))).append("<br>Delay by: ")
								.append(Math.round(getDaysBetween(crs.getString("po_voucher_delivery_date"), crs.getString("grn_voucher_delivery_date")))).append("</b>");
					}
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<center><font color=red><b>No GRN found!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
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
				Str.append("<option value = 0> Select </option>");
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
