package axela.sales;
//divya
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=veh-salesorder-list.jsp?smart=yes>Click here to List Sales Orders</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND substr(so_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND substr(so_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND so_branch_id=" + dr_branch_id;
					}
					StrSearch = StrSearch + " AND branch_active = '1' and so_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("sostrsql", StrSearch, request);
						StrHTML = SalesOrderSummary(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String SalesOrderSummary(HttpServletRequest request) {
		DecimalFormat fmt = new DecimalFormat("#.##");
		StringBuilder Str = new StringBuilder();
		int so_count = 0, del_count = 0;
		double nettotal = 0;
		try {
			StrSql = " SELECT branch_id, CONCAT(branch_name,' (', branch_code, ')') AS branchname,"
					+ " (SELECT COUNT(del.so_id) FROM " + compdb(comp_id) + "axela_sales_so del"
					+ " WHERE 1 = 1 "
					+ StrSearch.replace("so_date", "del.so_delivered_date") + ""
					+ " AND del.so_branch_id = branch_id"
					+ " AND del.so_active=1"
					+ " AND del.so_delivered_date !='') AS delcount,"
					+ " COUNT(so_id) AS socount,"
					+ " COALESCE(sum(so_grandtotal),0) AS total"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_branch_id = branch_id"
					+ StrSearch
					+ BranchAccess
					+ ExeAccess.replace("emp_id", "so_emp_id") + ""
					+ " WHERE branch_active='1'"
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			// SOP("StrSql--------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Sales Order Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th style=\"text-align:center\">Branch</th>\n");
				Str.append("<th style=\"text-align:center\">Deliveries</th>\n");
				Str.append("<th style=\"text-align:center\">Sales Order Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" style=\"text-align:center\">Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					so_count = so_count + crs.getInt("socount");
					del_count = del_count + crs.getInt("delcount");
					nettotal = nettotal + crs.getDouble("total");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("delcount")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("socount")).append("</td>");
					Str.append("<td valign=top align=right> ").append(IndFormat(fmt.parse(crs.getString("total")) + "")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left><b>Total: &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right><b>").append(del_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(so_count).append("</b></td>\n");
				Str.append("<td align=right><b> ").append(IndFormat(fmt.format(nettotal))).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
