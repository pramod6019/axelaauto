//Vidyanandan S
package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Cash_Flow extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String comp_id = "0";

	public String start_date = "";
	public String end_date = "";

	public String startdate = "";
	public String enddate = "";
	public String period_id = "1";
	public String go = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	double inflow = 0.00, outflow = 0.00, difference = 0.00, alie1dramt = 0.00, alie234dramt = 0.00;
	double inflowtottal = 0.00, outflowtotal = 0.00;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// comp_id = "2017";
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = AddDayMonthYear(DateToShortDate(kknow()), 0, 0, -3, 0);
				enddate = DateToShortDate(kknow());
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					if (msg.equals("")) {
						StrHTML = Listdata();
					}
				} else {
					CheckForm();
					StrHTML = Listdata();
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";

		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
			}
		}
		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!startdate.equals("") && !enddate.equals("") && Long.parseLong(start_date) > Long.parseLong(end_date)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}

		if (period_id.equals("0")) {
			msg += "<br>Select Period!";
		}

		if (!start_date.equals("") && end_date.equals("")) {
			msg = msg + "<br>Select End Date!";
		} else if (start_date.equals("") && !end_date.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		period_id = CNumeric(PadQuotes(request.getParameter("dr_period")));
	}

	public String Listdata() {
		int count = 0;
		double vouchertrans_amount = 0.00;
		double alie1cramt = 0.00, alie234cramt = 0.00;
		String period = "", vouchertrans_dc = "";
		StringBuilder Str = new StringBuilder();
		String date = strToShortDate(ToLongDate(kknow()));

		StrSql = "SELECT"
				+ " voucher_id, voucher_date, vouchertrans_paymode_id, vouchertrans_dc,"
				+ " vouchertrans_amount, customer_name, accgroup_alie "
				+ " FROM  " + compdb(comp_id) + " axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_customer ON customer_id = vouchertrans_customer_id"
				// + " INNER JOIN  " + compdb(comp_id) + " axela_acc_subgroup ON accsubgroup_id = customer_accgroup_id"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_group ON accgroup_id = customer_accgroup_id"
				// + " INNER JOIN  "+compdb(comp_id)+"axela_branch on branch_id = voucher_branch_id"
				+ " WHERE 1=1"
				+ " AND vouchertrans_paymode_id != 0"
				+ " AND SUBSTR(voucher_date, 1, 8) >= SUBSTR(" + start_date + ", 1, 8)"
				+ " AND SUBSTR(voucher_date, 1, 8) <= SUBSTR(" + end_date + ", 1, 8)"
				+ " ORDER BY voucher_date, voucher_id";
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			CachedRowSet rscopy = crs.createCopy();
			// if (crs.isBeforeFirst()) {
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("");
			Str.append("<th data-hide=\"phone\">#</th>\n");
			Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Period</th>\n");
			Str.append("<th>In Flow</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Out Flow</th>\n");
			Str.append("<th data-hide=\"phone, tablet\">Net Flow</th>\n");
			Str.append("</tr></thead>\n");
			if (period_id.equals("1")) {
				Str.append(CashflowMonthly(rscopy));
			} else {
				Str.append(CashflowQuarterly(rscopy));
			}
			Str.append("<tbody><tr>\n");
			Str.append("<td align='right'>&nbsp;</td>\n");
			Str.append("<td align='right'><b>Total:</b></td>\n");
			Str.append("<td align='right'><b>" + deci.format(inflowtottal) + "</b></td>\n");
			Str.append("<td align='right'><b>" + deci.format(outflowtotal) + "</b></td>\n");
			Str.append("<td align='right'><b>" + deci.format(inflowtottal - outflowtotal) + "</b></td>\n");
			Str.append("</tr></tbody>\n");
			crs.close();
			Str.append("</table>\n");
			// } else {
			// Str.append("<br><br><br><br><font color=red><b>No Records Found!</b></font><br><br>");
			// }
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	public String CashflowMonthly(CachedRowSet crs) {
		StringBuilder Str = new StringBuilder();
		String date = "00000000";
		int count = 0;
		try {
			CachedRowSet rscopy1 = crs.createCopy();
			while (crs.next()) {
				if (!date.substring(1, 6).equals(crs.getString("voucher_date").substring(1, 6))) {
					count++;
					CalcMonthlytotal(crs.getString("voucher_date").substring(1, 6), rscopy1);
					Str.append("<tbody><tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					Str.append("<td align='center'>").append(FormatDate(crs.getString("voucher_date"), "MMM yyyy")).append("</td>\n");
					Str.append("<td align='right'>").append(deci.format(inflow)).append("</td>\n");
					Str.append("<td align='right'>").append(deci.format(outflow)).append("</td>\n");
					Str.append("<td align='right'>").append(deci.format(inflow - outflow)).append("</td>\n");
					Str.append("</tr><tbody>\n");
				}
				date = crs.getString("voucher_date");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void CalcMonthlytotal(String month, CachedRowSet rscopy) {
		inflow = 0.00;
		outflow = 0.00;
		try {
			while (rscopy.next()) {
				if (month.equals(rscopy.getString("voucher_date").substring(1, 6))) {
					if (rscopy.getString("accgroup_alie").equals("1")) {
						if (rscopy.getString("vouchertrans_dc").equals("1")) {
							inflow += rscopy.getDouble("vouchertrans_amount");
						} else {
							outflow += rscopy.getDouble("vouchertrans_amount");
						}
					} else {
						if (rscopy.getString("vouchertrans_dc").equals("1")) {
							outflow += rscopy.getDouble("vouchertrans_amount");
						} else {
							inflow += rscopy.getDouble("vouchertrans_amount");
						}
					}
				}
			}
			inflowtottal += inflow;
			outflowtotal += outflow;
			rscopy.beforeFirst();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String CashflowQuarterly(CachedRowSet crs) {
		StringBuilder Str = new StringBuilder();
		int i = 0;
		int j = 2;
		String date = "00000000000000";
		String year = "00000000000000";
		int count = 0;
		try {
			CachedRowSet rscopy1 = crs.createCopy();
			while (crs.next()) {
				if (!date.substring(0, 6).equals(crs.getString("voucher_date").substring(0, 6))
						&& Long.parseLong(crs.getString("voucher_date").substring(0, 6)) >= Long.parseLong(ConvertShortDateToStr(AddDayMonthYear(startdate, 0, 0, i, 0)).substring(0, 6))
						&& Long.parseLong(crs.getString("voucher_date").substring(0, 6)) <= Long.parseLong(ConvertShortDateToStr(AddDayMonthYear(startdate, 0, 0, j, 0)).substring(0, 6))) {
					count++;
					CalcQuarterlytotal(ConvertShortDateToStr(AddDayMonthYear(startdate, 0, 0, i, 0)), ConvertShortDateToStr(AddDayMonthYear(startdate, 0, 0, j, 0)), rscopy1);
					if (Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) >= 1 && Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) <= 3) {
						Str.append("<tbody><tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td align='center'>").append("Jan - Mar " + FormatDate(crs.getString("voucher_date"), "yyyy")).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(outflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow - outflow)).append("</td>\n");
						Str.append("</tr>\n");
					} else if (Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) >= 4 && Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) <= 6) {
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td align='center'>").append("Apr - Jun " + FormatDate(crs.getString("voucher_date"), "yyyy")).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(outflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow - outflow)).append("</td>\n");
						Str.append("</tr>\n");
					} else if (Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) >= 7 && Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) <= 9) {
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td align='center'>").append("July - Sept " + FormatDate(crs.getString("voucher_date"), "yyyy")).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(outflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow - outflow)).append("</td>\n");
						Str.append("</tr>\n");
					} else if (Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) >= 10 && Integer.parseInt(crs.getString("voucher_date").substring(4, 6)) <= 12) {
						Str.append("<tr>\n");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						Str.append("<td align='center'>").append("Oct - Dec " + FormatDate(crs.getString("voucher_date"), "yyyy")).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(outflow)).append("</td>\n");
						Str.append("<td align='right'>").append(deci.format(inflow - outflow)).append("</td>\n");
						Str.append("</tr><tbody>\n");
					}
					i += 3;
					j += 3;
				}
				date = crs.getString("voucher_date");
				year = crs.getString("voucher_date");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void CalcQuarterlytotal(String month1, String month2, CachedRowSet rscopy) {
		inflow = 0.00;
		outflow = 0.00;
		try {
			while (rscopy.next()) {
				if (Long.parseLong(rscopy.getString("voucher_date")) >= Long.parseLong(month1)
						&& Long.parseLong(rscopy.getString("voucher_date")) <= Long.parseLong(month2)) {
					if (rscopy.getString("accgroup_alie").equals("1")) {
						if (rscopy.getString("vouchertrans_dc").equals("1")) {
							inflow += rscopy.getDouble("vouchertrans_amount");
						} else {
							outflow += rscopy.getDouble("vouchertrans_amount");
						}
					} else {
						if (rscopy.getString("vouchertrans_dc").equals("1")) {
							outflow += rscopy.getDouble("vouchertrans_amount");
						} else {
							inflow += rscopy.getDouble("vouchertrans_amount");
						}
					}
				}
			}
			inflowtottal += inflow;
			outflowtotal += outflow;
			rscopy.beforeFirst();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePeriod() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value='1'").append(Selectdrop(1, period_id)).append(">Monthly</option>");
		Str.append("<option value='2'").append(Selectdrop(2, period_id)).append(">Quarterly</option>");
		return Str.toString();
	}
}
