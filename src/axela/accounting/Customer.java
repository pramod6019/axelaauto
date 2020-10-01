package axela.accounting;
//saiman 25th sept

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Customer extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";

	public String StrHTML = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String param1 = "";
	public String param2 = "";
	public String param3 = "";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String config_customer_name = "";
	public String ListLink = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_customer_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				config_customer_name = GetSession("config_customer_name", request);

				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				ListLink = "<a href=\"customer-list.jsp?smart=yes\">Click here to List " + config_customer_name + "s</a>";

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND SUBSTR(customer_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}

					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(customer_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND customer_branch_id = " + dr_branch_id + "";
					}
					StrSearch += " AND customer_active = '1'"
							+ " AND branch_active = '1' ";

					if (!msg.equals("")) {
						msg = "Error!<br>" + msg;
					}

					if (msg.equals("")) {
						SetSession("custstrsql", StrSearch, request);
						StrHTML = CustomerSummary(request);
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
		start_time = PadQuotes(request.getParameter("txt_starttime"));
		end_time = PadQuotes(request.getParameter("txt_endtime"));
		String date = "";
		if (start_time.equals("") || end_time.equals("")) {
			date = ToShortDate(kknow());
		}
		if (start_time.equals("")) {
			start_time = ReportStartdate();
		}
		if (end_time.equals("")) {
			end_time = ReportStartdate();
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
			if (dr_branch_id.equals("")) {
				dr_branch_id = "0";
			}
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (start_time.equals("")) {
			msg = msg + "Select Start Date!<br>";
		} else {
			if (isValidDateFormatStr(start_time)) {
				starttime = ConvertShortDateToStr(start_time);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
			}
		}

		if (end_time.equals("")) {
			msg = msg + "Select End Date!<br>";
		} else {
			if (isValidDateFormatStr(end_time)) {
				endtime = strToShortDate(end_time);
				if (!start_time.equals("") && isValidDateFormatStr(start_time) && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}
	}

	public String CustomerSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int custcount = 0;
		StrHTML = "";
		try {
			StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " COUNT(customer_id) AS custcount"
					+ " FROM  " + compdb(comp_id) + " axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_state on state_id= city_state_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_country on country_id = state_country_id"
					// + " INNER JOIN  "+compdb(comp_id)+"axela_soe on soe_id = customer_soe_id"
					// + " INNER JOIN  "+compdb(comp_id)+"axela_sob on sob_id = customer_sob_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_soe ON soe_id = customer_soe_id"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_sob ON sob_id = customer_sob_id"
					+ " WHERE 1 = 1" + StrSearch + BranchAccess
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>" + config_customer_name + " Summary</b><br>");
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th width=\"20%\">" + config_customer_name + " Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					custcount += crs.getInt("custcount");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
					Str.append(crs.getString("branchname")).append("</a>");
					Str.append("</td>\n<td valign=\"top\" align=\"right\">").append(crs.getString("custcount"));
					Str.append("</td>\n</tr>\n");
				}
				Str.append("<tr>\n<td align=\"right\"><b>Total:</b></td>\n");
				Str.append("<td align=\"right\"><b>").append(custcount).append("</b></td>\n");
				Str.append("</tr>\n</table>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String SessionDateFormat(String strDateTime, String format) {
	// String strOutDt = "";
	// try {
	// if (isValidDateFormatStr(strDateTime)) {
	// Date dttemp = new SimpleDateFormat("yyyyMMddHHmmss").parse(strDateTime);
	// strOutDt = new SimpleDateFormat(format).format(dttemp);
	// return strOutDt;
	// } else {
	// return "";
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	//
	// }
}
