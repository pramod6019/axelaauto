package axela.inventory;
//aJIt 11th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_PReturn extends Connect {

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

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_return_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = PadQuotes(request.getParameter("dr_branch"));
				dr_location_id = CNumeric(PadQuotes(request.getParameter("dr_location")));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if (branch_id.equals("0")) {
						msg = msg + "<br>Select Branch!";
					}
					if (dr_location_id.equals("0")) {
						msg += "<br>Select Location!";
					}
					if (!branch_id.equals("0")) {
						StrSearch = StrSearch + " and po.voucher_branch_id = " + branch_id;
					}
					if (!dr_location_id.equals("0")) {
						StrSearch = StrSearch + " and pr.voucher_location_id = " + dr_location_id;
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
		StrSql = "SELECT po.voucher_id, concat(branch_code,po.voucher_id) as po_no,"
				+ " concat(branch_name,' (',branch_code,')') branch_name, po.voucher_date, po.voucher_delivery_date,"
				+ " po.voucher_ref_no, po.voucher_authorize_time, po.voucher_authorize_id, po.voucher_customer_id,"
				+ " po.voucher_amount, customer_name, po.voucher_active, pr.voucher_id, pr.voucher_date,"
				+ " prtrans.vouchertrans_qty, cat_name, cat_id, item_name, item_id, item_code,"
				+ " ouruom.uom_name, po.voucher_delivery_date, supuom.uom_name, supuom.uom_ratio, potrans.vouchertrans_alt_qty"
				+ " from " + compdb(comp_id) + "axela_acc_voucher pr"
				+ " inner join " + compdb(comp_id) + "axela_acc_voucher_trans prtrans ON prtrans.vouchertrans_voucher_id = pr.voucher_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id =  prtrans.vouchertrans_item_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_cat_pop on cat_id = item_cat_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_uom ouruom on ouruom.uom_id = item_uom_id"
				+ " inner join " + compdb(comp_id) + "axela_acc_voucher po ON po.voucher_id = prtrans.vouchertrans_multivoucher_id"
				+ " inner join " + compdb(comp_id) + "axela_acc_voucher_trans potrans ON potrans.vouchertrans_voucher_id = po.voucher_id"
				+ " AND potrans.vouchertrans_item_id = prtrans.vouchertrans_item_id"
				+ " inner join " + compdb(comp_id) + "axela_inventory_uom supuom on supuom.uom_id = potrans.vouchertrans_alt_uom_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = po.voucher_customer_id"
				+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = po.voucher_branch_id"
				+ " where SUBSTR(pr.voucher_entry_date, 1, 8) >= SUBSTR(" + starttime + ",1, 8)"
				+ " AND  SUBSTR(pr.voucher_entry_date, 1, 8) < SUBSTR(" + endtime + ",1, 8)"
				+ " AND pr.voucher_vouchertype_id = 24"
				+ StrSearch + BranchAccess
				+ " GROUP BY po.voucher_id, pr.voucher_id"
				+ " ORDER BY po.voucher_id, pr.voucher_id";
		// SOP("StrSql===purchase return===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Purchase Order</th>\n");
				Str.append("<th>Supplier</th>\n");
				Str.append("<th data-hide=\"phone\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Supplier Qty</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Conversion Factor</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Our Qty</th>\n");
				Str.append("<th   data-hide=\"phone, tablet\">Return Date</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tbody>");
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append("<b><a href=\"../accounting/voucher-list.jsp?voucher_po_id=").append(crs.getString("po.voucher_id")).append("\">ID: ").append(crs.getString("po.voucher_id"))
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
					Str.append("<td valign=top align=right>").append(crs.getString("potrans.vouchertrans_alt_qty")).append(" ").append(crs.getString("supuom.uom_name")).append("</td>");
					Str.append("</td><td valign=top align=right>").append(crs.getString("supuom.uom_ratio")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getInt("prtrans.vouchertrans_qty")).append(" ").append(crs.getString("ouruom.uom_name")).append("</td>");
					Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("pr.voucher_date")));
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<center><font color=red><b>No Returns found!</b></font></center>");
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

				StrSql = "SELECT location_id, IF(location_code != '', CONCAT(location_name, ' (',location_code, ')'), location_name) AS location_name"
						+ " from " + compdb(comp_id) + "axela_inventory_location"
						+ " where location_branch_id = " + branch_id + ""
						+ " group by location_id"
						+ " order by location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<select name=\"dr_location\" id=\"dr_location\" class=\"form-control\">");
				Str.append("<option value = 0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), dr_location_id));
					Str.append(">").append(crs.getString("location_name")).append("</option> \n");
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
