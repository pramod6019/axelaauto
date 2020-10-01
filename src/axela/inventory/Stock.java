package axela.inventory;
//@Bhagwan Singh 11 feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=stock-list.jsp?smart=yes>Click here to List Stock</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND SUBSTR(vehstock_invoice_date , 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(vehstock_invoice_date ,1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND vehstock_branch_id =" + dr_branch_id;
					}
					StrSearch = StrSearch + " AND branch_active = '1' ";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("stockstrsql", StrSearch, request);
						StrHTML = StockSummary(request);
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
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
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			boolean t2 = isValidDateFormatShort(starttime);
			if (t2 == true) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else if (t2 == false) {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			boolean t2 = isValidDateFormatShort(endtime);
			if (t2 == true) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else if (t2 == false) {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String StockSummary(HttpServletRequest request) {
		int stockcount = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT branch_id, CONCAT(branch_name,' (', branch_code, ')') AS branchname,"
					+ " COUNT(vehstock_id) AS stockcount"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " WHERE 1 = 1 " + StrSearch + BranchAccess
					+ " GROUP BY branch_id "
					+ " ORDER BY branchname ";
			// SOP("StrSql---"+StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"portlet box \">");
				// Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
				// Str.append("<div class=\"caption\" style=\"float: none\">Stock Summary</div></div>");
				// Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"table-responsive table-bordered \">");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr align=center>\n");
				Str.append("<th>Branch Name</th>\n");
				Str.append("<th >Stock Count</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					stockcount = stockcount + crs.getInt("stockcount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("stockcount")).append("</td>");
					Str.append("</tr>");
				}
				crs.close();
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(stockcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody></table></div>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
