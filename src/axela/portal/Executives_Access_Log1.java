// Ved Prakash (28th August 2013)
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Access_Log1 extends Connect {

	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String search = "";
	public String submitB = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess = "";
	public String dr_branch_id = "";
	public String startdate = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				startdate = PadQuotes(request.getParameter("txt_starttime"));

				if (startdate.equals("")) {
					startdate = strToShortDate(ToLongDate(kknow()));
				}
				search = PadQuotes(request.getParameter("submit_button"));
				if (search.equals("Go")) {
					msg = "";
					dr_branch_id = PadQuotes(request.getParameter("dr_branch_id")); // Executive
					if (!dr_branch_id.equals("-1")) {
						StrSearch += BranchAccess + " AND emp_branch_id = " + dr_branch_id + "";
					}
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						StrHTML = ExecutiveLogSummary1();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (startdate.equals("")) {
			msg = "<br>Select Start Date!";
		} else {
			if (!isValidDateFormatShort(startdate)) {
				msg = "<br>Enter Valid Start Date!";
			}
		}
	}

	public String ExecutiveLogSummary1() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			StrSql = "SELECT emp_id, emp_name, COALESCE(branch_id, 0) AS branch_id,"
					+ " COALESCE(branch_name, 'Head Office') AS branchname,"
					+ " IF(log_signin_time != '', DATE_FORMAT(MIN(log_signin_time), '%H:%i'), '') AS signintime,"
					+ " IF(log_signout_time != '', DATE_FORMAT(MAX(log_signout_time), '%H:%i'), '') AS signouttime"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON emp_branch_id = branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_log ON emp_id = log_emp_id"
					+ " WHERE SUBSTR(log_signin_time, 1, 8) = " + ConvertShortDateToStr(startdate).substring(0, 8) + StrSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY branchname, emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th data-hide=\"phone\">First Sign In</th>\n");
				Str.append("<th data-hide=\"phone\">Last Sign Out</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td width=\"5%\" align=\"center\">").append(count);
					Str.append("</td>\n");
					Str.append("<td align=\"left\">");
					if (!crs.getString("branchname").equals("Head Office")) {
						Str.append("<a href=branch-summary.jsp?branch_id=").append(crs.getString("branch_id"));
						Str.append(">").append(crs.getString("branchname")).append("</a>\n");
					} else {
						Str.append(crs.getString("branchname"));
					}
					Str.append("</td>\n");
					Str.append("<td align=\"left\">").append("<a href=\"../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append("\">").append(crs.getString("emp_name"));
					Str.append("</a></td>\n");
					Str.append("<td align=\"center\">");
					if (!crs.getString("signintime").equals("")) {
						Str.append(crs.getString("signintime"));
					} else {
						Str.append("--");
					}
					Str.append("</td>\n");
					Str.append("<td align=\"center\">");
					if (!crs.getString("signouttime").equals("")) {
						Str.append(crs.getString("signouttime"));
					} else {
						Str.append("--");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = '1'" + BranchAccess + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=-1").append(StrSelectdrop("-1", dr_branch_id)).append(">All Branches</option>\n");
			Str.append("<option value=0").append(StrSelectdrop("0", dr_branch_id)).append(">Head Office</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), dr_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (");
				Str.append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
