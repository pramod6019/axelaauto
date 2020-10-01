package axela.service;

/*
 * @author $at!sh 06-march-2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String reportURL = "";
	public String ListLink = "<a href=jobcard-list.jsp?smart=yes>Click here to List Job Cards</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
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
						StrSearch = " AND SUBSTR(jc_entry_date,1,8) >= substr('" + starttime + "',1,8) ";
					}

					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(jc_entry_date,1,8) <= substr('" + endtime + "',1,8) ";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND jc_branch_id=" + dr_branch_id;
					}

					StrSearch += " AND branch_active = 1"
							+ " AND jc_active = 1";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						SetSession("jcstrsql", StrSearch, request);
						StrHTML = JobCardSummary(request);
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
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg += "<br>Enter valid End Date!";
				endtime = "";
			}
		}
	}

	public String JobCardSummary(HttpServletRequest request) {
		int jc_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " branch_id, COUNT(jc_id) AS servicecount"
					+ " FROM  " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " WHERE 1 = 1" + StrSearch + BranchAccess + ExeAccess.replace("emp_id", "jc_emp_id") + ""
					+ " GROUP BY branch_id"
					+ " ORDER BY branchname";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<b>Job Card Summary</b><br>");
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th width=\"20%\">Job Card Count</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					jc_count += crs.getInt("servicecount");
					Str.append("<tr>\n<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"right\">").append(crs.getString("servicecount"));
					Str.append("</td>\n</tr>\n");
				}
				Str.append("<tr>\n<td align=\"right\"><b>Total: </b></td>\n");
				Str.append("<td align=\"right\"><b>").append(jc_count).append("</b></td>\n");
				Str.append("</tr>\n");
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
