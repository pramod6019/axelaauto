package axela.inventory;
//Sree Venkatesh 17th june 2013
// $at!sh 15th july 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_ABC extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "";
	public String BranchAccess = "";
	public String go = "";
	public String ExeAccess = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String dr_location_id = "";
	public String branch_id = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	DecimalFormat perc = new DecimalFormat("0.000");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = (session.getAttribute("emp_id")).toString();
				BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
				branch_id = PadQuotes(request.getParameter("dr_branch"));
				// SOP("branch_Acess=="+BranchAccess);
				ExeAccess = CheckNull(session.getAttribute("ExeAccess"));
				go = PadQuotes(request.getParameter("submit_button"));
				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					StrSearch = StrSearch + " AND SUBSTR(voucher_date,1,8)>=SUBSTR('" + starttime + "',1,8)"
							+ " AND SUBSTR(voucher_date,1,8)<=SUBSTR('" + endtime + "',1,8)";

					if (!branch_id.equals("0")) {
						StrSearch = StrSearch + " AND voucher_branch_id = " + branch_id;
					}
					if (!dr_location_id.equals("0")) {
						StrSearch = StrSearch + " AND voucher_location_id = " + dr_location_id;
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = "()";
						StrHTML = ListTarget();
					}
				}
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// System.out.print("..............str_date" + starttime);
		branch_id = PadQuotes(request.getParameter("dr_branch"));
		if (branch_id.equals("")) {
			branch_id = "0";
		}
		dr_location_id = PadQuotes(request.getParameter("dr_location"));
		if (dr_location_id.equals("")) {
			dr_location_id = "0";
		}
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
			start_time = starttime;
		} else if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				start_time = starttime;
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
			end_time = endtime;
		} else if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && isValidDateFormatShort(strToShortDate(starttime)) && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				end_time = endtime;
			}
		}

		// if (!endtime.equals("") && !starttime.equals("") && isValidDateFormatShort(strToShortDate(starttime)) && isValidDateFormatShort(strToShortDate(endtime))) {
		// if (Long.parseLong(endtime) > Long.parseLong(ConvertShortDateToStr(AddDayMonthYear(strToShortDate(starttime), 0, 0, 1, 0)))) {
		// msg = msg + "<br>Difference of Start Date and End Date should not be greater than 1 month!";
		// }
		// }
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		if (!branch_id.equals("")) {
			try {
				StrSql = "SELECT location_id, location_name, location_code"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + branch_id + ""
						+ " GROUP BY location_id"
						+ " ORDER BY location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), dr_location_id));
					Str.append(">").append(crs.getString("location_name")).append(" (");
					Str.append(crs.getString("location_code")).append(")");
					Str.append("</option> \n");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String ListTarget() {
		StringBuilder Str = new StringBuilder();
		int count = 0;

		StrSql = "SELECT"
				+ " item_id, item_name, sum(vouchertrans_qty) AS qty,"
				+ " ( sum(vouchertrans_price) / sum(vouchertrans_qty) ) AS itemprice,"
				+ " @total := sum(vouchertrans_price) AS totalvalue,"
				+ " ( SELECT ( sum( axela_acc_voucher_trans.vouchertrans_price"
				+ " ) / sum(vouchertrans_item.vouchertrans_price) ) * 100"
				+ " FROM"
				+ " " + compdb(comp_id) + "axela_acc_voucher_trans AS vouchertrans_item"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item AS item ON item.item_id = vouchertrans_item.vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_item.vouchertrans_voucher_id"
				+ " WHERE"
				+ " vouchertrans_item.vouchertrans_item_id != 0"
				+ StrSearch
				+ " AND voucher_vouchertype_id = 6"
				+ " AND item.item_active = 1"
				+ " AND item_type_id !=1"
				+ " AND item_nonstock =0"
				+ " ) AS totalusagepercent"
				+ " FROM"
				+ " " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_id = vouchertrans_voucher_id"
				+ " WHERE"
				+ " vouchertrans_item_id != 0"
				+ StrSearch + ""
				+ " AND voucher_vouchertype_id = 6"
				+ " AND item_type_id !=1"
				+ " AND item_active = 1"
				+ " AND item_nonstock =0"
				+ " GROUP BY"
				+ " item_id"
				+ " ORDER BY"
				+ " totalvalue DESC"
				+ " LIMIT 1000";

		// SOP("StrSql===" + StrSqlBreaker(StrSql));

		try {
			Float countpercent = 0.0f;
			double cumulativeperA = 0.00, cumulativeperB = 0.00, cumulativeperC = 0.00;
			int A = 0, B = 0, C = 0;
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					if (cumulativeperA < 70.00) {
						cumulativeperA += crs.getDouble("totalusagepercent");
						A++;
					} else if (cumulativeperB < 20.00) {
						cumulativeperB += crs.getDouble("totalusagepercent");
						B++;
					} else {
						cumulativeperC += crs.getDouble("totalusagepercent");
						C++;
					}
				}

				if (cumulativeperC == 0.00) {
					if (cumulativeperB < 11.00) {
						cumulativeperC = cumulativeperB;
						cumulativeperB = 0.00;
						C = B;
						B = 0;
					}

					if (cumulativeperA < 21.00) {
						cumulativeperB += cumulativeperA;
						cumulativeperA = 0.00;
						B = A;
						A = 0;
					}
				}

				crs.beforeFirst();

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");

				Str.append("<th>#</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th>Quantity</th>\n");
				Str.append("<th>Item Price</th>\n");
				Str.append("<th>Total Value</th>\n");
				Str.append("<th>Total Value(%)</th>\n");
				Str.append("<th>Cumulative Total Value(%)</th>\n");
				Str.append("<th>ABC Class</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					countpercent = Float.parseFloat(perc.format(countpercent)) + Float.parseFloat(perc.format(crs.getDouble("totalusagepercent")));
					Str.append("<tr>\n");
					Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top><a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getInt("item_id")).append("\">").append(crs.getString("item_name"))
							.append("</a></td>");
					Str.append("<td valign=top align=right>").append(crs.getString("qty")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(IndFormat(deci.format(crs.getDouble("itemprice")))).append("</td>");
					Str.append("<td valign=top align=right>").append(IndFormat(deci.format(crs.getDouble("totalvalue")))).append("</td>");
					Str.append("<td valign=top align=right>").append(perc.format(crs.getDouble("totalusagepercent"))).append("</td>");

					if (count <= A) {
						Str.append("<td valign=top align=right>").append(perc.format(cumulativeperA)).append("</td>");
					} else if (count <= A + B) {
						Str.append("<td valign=top align=right>").append(perc.format(cumulativeperB)).append("</td>");
					} else {
						Str.append("<td valign=top align=right>").append(perc.format(cumulativeperC)).append("</td>");
					}

					if (count <= A) {
						Str.append("<td valign=top align=center>").append("<font color='red'><b>A</b></font>").append("</td>");
					} else if (count <= A + B) {
						Str.append("<td valign=top align=center>").append("<font color='red'><b>B</b></font>").append("</td>");
					} else {
						Str.append("<td valign=top align=center>").append("<font color='red'><b>C</b></font>").append("</td>");
					}

					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<font color=red><b>No Job Cards Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
