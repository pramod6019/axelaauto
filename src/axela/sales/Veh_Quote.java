package axela.sales;
//Murali 2nd july
//divya
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote extends Connect {

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
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=veh-quote-list.jsp?smart=yes>Click here to List Quotes</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);
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
						StrSearch = " and substr(quote_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(quote_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and quote_branch_id=" + dr_branch_id;
					}
					StrSearch = StrSearch + " and branch_active = '1' and quote_active = '1'";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("quotestrsql", StrSearch, request);
						StrHTML = QuoteSummary(request);
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

	public String QuoteSummary(HttpServletRequest request) {
		DecimalFormat fmt = new DecimalFormat("#.##");
		int quote_count = 0;
		double nettotal = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname,"
					+ " count(quote_id) as quotecount, coalesce(sum(quote_grandtotal),0) as total "
					+ " from " + compdb(comp_id) + "axela_sales_quote "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = quote_branch_id "
					+ " where 1 = 1 and quote_enquiry_id != 0"
					+ StrSearch + BranchAccess + ExeAccess.replace("emp_id", "quote_emp_id") + ""
					+ " group by branch_id order by branchname";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Quote Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr>\n");
				Str.append("<th style=\"width:60%; text-align:center\">Branch&nbsp;&nbsp;&nbsp;</th>\n");
				Str.append("<th style=\"width:20%; text-align:center\">Quote Count</th>\n");
				Str.append("<th style=\"width:20%; text-align:center\">Amount</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					quote_count = quote_count + crs.getInt("quotecount");
					nettotal = nettotal + crs.getDouble("total");
					Str.append("<tr>");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("quotecount")).append("</td>");
					Str.append("<td valign=top align=right> ").append(IndFormat(crs.getString("total"))).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=left><b>Total: &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right><b>").append(quote_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(IndFormat(fmt.format(nettotal))).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tr></table></div></div>");
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
