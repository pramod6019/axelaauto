package axela.insurance;
//Dilip Kumar 13th APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String ListLink = "<a href=insurance-list.jsp?smart=yes>Click here to List Policy</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_policy_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				// String str = "9690960778111";
				// SOP("=="+str.substring(0, 10));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and substr(insurpolicy_date,1,8) >= substr('" + starttime + "',1,8) ";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and substr(insurpolicy_date,1,8) <= substr('" + endtime + "',1,8) ";
					}
					if (!dr_branch_id.equals("0")) {
						StrSearch = StrSearch + " and insurpolicy_branch_id=" + dr_branch_id;
					}
					StrSearch = StrSearch + " and insurpolicy_active = '1' and branch_active = '1' ";

					if (!msg.equals("")) {
						msg = "<br>Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("insurancestrsql", StrSearch, request);
						StrHTML = InsuranceSummary(request);
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
			dr_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
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
		} else {
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
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

		if (!starttime.equals("") && !endtime.equals("") && isValidDateFormatShort(strToShortDate(starttime)) && isValidDateFormatShort(strToShortDate(endtime))) {
			if (Long.parseLong(starttime) > Long.parseLong(endtime)) {
				msg = msg + "<br>Start Date should be less than End date!";
			}
		}
	}

	public String InsuranceSummary(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int insurancecount = 0;

		try {
			StrSql = " SELECT CONCAT(branch_name, ' (', branch_code, ')') as branchname,"
					+ " COUNT(insurpolicy_id) AS insurancecount"
					+ " FROM  " + compdb(comp_id) + "axela_insurance_policy"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurpolicy_branch_id"
					+ " WHERE 1 = 1 " + StrSearch + BranchAccess
					+ " GROUP BY  branch_id"
					+ " ORDER BY branchname";

			// SOP("StrSql===="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<br><b>Policy Summary</b><br>");
				Str.append("<div class=\"table-bordered\">\n");
				Str.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th width=20%>Policy Count</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					insurancecount = insurancecount + crs.getInt("insurancecount");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("branchname")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(crs.getString("insurancecount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(insurancecount).append("</b></td>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
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
