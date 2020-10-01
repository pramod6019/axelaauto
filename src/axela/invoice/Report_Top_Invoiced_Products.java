package axela.invoice;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import cloudify.connect.Connect;

public class Report_Top_Invoiced_Products extends Connect {

	public String StrSql = "";
	public String start_time = "", starttime = "";
	public String end_time = "", endtime = "";
	public String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "";
	public String[] team_ids, exe_ids, model_ids, cat_ids, brand_ids, branch_ids, item_ids;
	public String team_id = "", exe_id = "", model_id = "", cat_id = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String StrSearch = "", oppr_Model = "", item_Model = "", item_id = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public String go = "";

	public String ExeAccess = "";
	public String NoDetails = "";
	public String comp_id = "0";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			if (starttime.equals("")) {
				starttime = strToShortDate(ToShortDate(kknow()));
			}
			if (endtime.equals("")) {
				endtime = strToShortDate(ToShortDate(kknow()));
			}
			// BranchAccess = GetSession("BranchAccess", request);
			// ExeAccess = GetSession("ExeAccess", request);
			go = PadQuotes(request.getParameter("submit_button"));
			if (go.equals("Go")) {
				GetValues(request, response);
				CheckForm();

				if (!brand_id.equals("") && branch_id.equals("")) {
					branch_id = ReturnBranchids(brand_id, comp_id);
					SOP("branch_id===" + branch_id);
				}

				if (!branch_id.equals("")) {
					StrSearch += " AND voucher_branch_id IN (" + branch_id + ")";
				}
				if (!model_id.equals("")) {
					StrSearch += " AND item_model_id IN (SELECT item_model_id  FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_model_id IN (" + model_id + "))";
				}

				if (!item_id.equals("")) {
					StrSearch += " AND item_id IN (" + item_id + ")";
				}

				if (!team_id.equals("")) {
					StrSearch += " AND emp_id in (SELECT teamtrans_emp_id"
							+ " FROM  " + compdb(comp_id) + "axela_sales_team_exe"
							+ " WHERE teamtrans_team_id IN (" + team_id + "))";
				}
				if (!exe_id.equals("")) {
					StrSearch += " AND emp_id IN (" + exe_id + ")";
				}
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				} else {
					StrHTML = ListTarget();
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
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				start_time = ConvertShortDateToStr(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
			}
		}

		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				end_time = ConvertShortDateToStr(endtime);

				if (!starttime.equals("") && !endtime.equals("") && isValidDateFormatShort(starttime)
						&& Long.parseLong(start_time) > Long.parseLong(end_time)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = ToLongDate(AddHoursDate(StringToDate(end_time), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}
	}

	public String ListTarget() {
		int qty = 0;
		double price = 0.00, disc = 0.00, grandtotal = 0.00;
		double total = 0.00;
		StringBuilder Str = new StringBuilder();
		try {
			// invoice block
			StrSql = "select  item_id, item_name, item_code,"
					+ " @qty:=sum(trans.vouchertrans_qty) as invqty,"
					+ " @amount:=sum(trans.vouchertrans_price) as invprice,"
					+ " @disc:=(COALESCE((select sum(disc.vouchertrans_price) from  " + compdb(comp_id) + "axela_acc_voucher_trans disc"
					+ " WHERE disc.vouchertrans_voucher_id = voucher_id"
					+ " AND disc.vouchertrans_discount=1 AND disc.vouchertrans_item_id = item_id),0)) as invdisc"
					// + " @amount-@disc  as invtotal"
					+ " from  " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans trans on trans.vouchertrans_item_id=item_id"
					// + " INNER JOIN  " + compdb(comp_id) + "axela_inventory_cat on cat_id=item_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher on voucher_id=trans.vouchertrans_voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id= voucher_branch_id"
					+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp on emp_id= voucher_emp_id"
					+ " WHERE 1=1"
					+ " AND vouchertype_id = 6"
					+ " AND vouchertype_voucherclass_id = 6 AND voucher_active=1"
					+ " AND trans.vouchertrans_discount = 0 AND trans.vouchertrans_tax = 0"
					+ " AND  substr(voucher_date,1,8)>=substr('" + start_time + "',1,8)"
					+ " AND substr(voucher_date,1,8)<substr('" + end_time + "',1,8)"
					+ " " + StrSearch
					+ " GROUP BY item_id"
					+ " ORDER BY item_id"
					+ " DESC LIMIT  1000";
			// SOP("StrSql in invoice list==========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th align=center data-hide=\"phone,tablet\" data-ignore=\"true\">#</th>\n");
				Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item ID</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Item</th>\n");
				Str.append("<th data-hide=\"phone\">Quantity</th>\n");
				Str.append("<th data-hide=\"phone\">Price</th>\n");
				Str.append("<th data-hide=\"phone\">Discount</th>\n");
				Str.append("<th>Sales Amount</th>\n");
				Str.append("</tr></thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name = item_name + " (" + crs.getString("item_code") + ")";
					}
					qty = qty + (int) crs.getDouble("invqty");
					price = price + (int) crs.getDouble("invprice");
					disc = disc + (int) crs.getDouble("invdisc");
					total = ((crs.getDouble("invprice") - crs.getDouble("invdisc")) * crs.getDouble("invqty"));
					grandtotal = grandtotal + total;
					Str.append("<tr>\n");
					Str.append("<td align=center>" + count + "</td>\n");
					Str.append("<td align=center>" + crs.getString("item_id") + "</td>\n");
					Str.append("<td><a href=../inventory/inventory-item-list.jsp?item_id=" + crs.getString("item_id") + ">" + item_name + "</a></td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("invqty")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("invprice")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(crs.getDouble("invdisc")) + "</td>\n");
					Str.append("<td align=right>" + deci.format(total) + "</td>\n");
					Str.append("</tr>\n");

				}
				Str.append("<tr align=center>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right>&nbsp;</td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right>" + deci.format(qty) + "</td>\n");
				Str.append("<td align=right>" + deci.format(price) + "</td>\n");
				Str.append("<td align=right>" + deci.format(disc) + "</td>\n");
				Str.append("<td align=right>" + deci.format(grandtotal) + "</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				NoDetails = "0";
			} else {
				NoDetails = "1";
			}
			if (NoDetails.equals("1")) {
				Str.append("<font color=red><b>No Items Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
