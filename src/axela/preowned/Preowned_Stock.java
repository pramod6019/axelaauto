package axela.preowned;
//Dilip Kumar 02 Jul 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Stock extends Connect {

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
	public String StrSql = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=preowned-stock-list.jsp?smart=yes>Click here to List Stock</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND SUBSTR(preowned_date,1,8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(preowned_date,1,8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " AND preowned_branch_id = " + dr_branch_id;
					}
					StrSearch = StrSearch + " AND branch_active = '1' ";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("preownedstockstrsql", StrSearch, request);
						StrHTML = PreownedStockSummary(request);
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
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
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

	public String PreownedStockSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int preownedstockcount = 0;
		StrHTML = "";
		try {
			StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " COUNT(preowned_id) AS preownedstockcount"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " WHERE 1 = 1"
					+ StrSearch + BranchAccess + ExeAccess.replace("emp_id", "preownedstock_emp_id") + ""
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Stock Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table table-bordered table-hover\">");
				Str.append("<tr align=center>\n");
				Str.append("<th data-toggle=\"true\">Branch</th>\n");
				Str.append("<th width=20% data-hide=\"phone, tablet\">Pre-Owned Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					preownedstockcount = preownedstockcount + crs.getInt("preownedstockcount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a></td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("preownedstockcount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=left><b>Total:  &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td align=right><b>").append(preownedstockcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tr></table></div></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
