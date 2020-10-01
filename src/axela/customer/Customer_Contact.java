//@Bhagwan Singh 11 feb 2013
package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Customer_Contact extends Connect {

	public String msg = "";
	public String start_time = "";
	public String starttime = "";
	public String end_time = "";
	public String endtime = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=customer-contact-list.jsp?smart=yes>Click here to List Contact</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_contact_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(contact_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(contact_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and customer_branch_id = " + dr_branch_id;
					}
					StrSearch = StrSearch + " and contact_active = '1' and branch_active = '1' ";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("contactstrsql", StrSearch, request);
						StrHTML = CustomerContactSummary(request);

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

	public String CustomerContactSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int contactcount = 0;
		try {
			StrSql = "SELECT branch_id, concat(branch_name,' (', branch_code, ')') as branchname,"
					+ " count(contact_id) as contactcount"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id"
					+ " WHERE 1 = 1" + StrSearch + BranchAccess
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Contact Summary</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\"><table class=\"table\">");
				Str.append("<tr>\n");
				Str.append("<th style=\"width:50%; text-align:right\">Branch &nbsp;&nbsp;&nbsp;</th>\n");
				Str.append("<th>Contact Count</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					contactcount = contactcount + crs.getInt("contactcount");
					Str.append("<td align=right><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname"))
							.append("</a>&nbsp;&nbsp;&nbsp;</td>\n");
					Str.append("<td>").append(crs.getString("contactcount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total: &nbsp;&nbsp;&nbsp;</b></td>\n");
				Str.append("<td><b>").append(contactcount).append("</b></td>\n");
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
