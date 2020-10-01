package axela.insurance;

/**
 * @Dilip Kumar 19 APR 2013
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String dr_branch_id = "0", branch_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_vehicle_access, emp_insurance_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));

				if (branch_id.equals("0")) {
					dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				} else {
					dr_branch_id = branch_id;
				}

				if ((!dr_branch_id.equals("0"))) {
					StrSearch = " and branch_id = " + dr_branch_id;
					StrHTML = EnquiryTriggerStatus(request);
				}
				// StrHTML += getDepartment();

				if (dr_branch_id.equals("0")) {
					msg = msg + "<br>Select Branch!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String EnquiryTriggerStatus(HttpServletRequest request) {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">");
			Str.append("<thead><tr>\n");
			Str.append("<tr align=center>\n");
			Str.append("<th data-hide=\"phone\"><b>Level 1</b></th>\n");
			Str.append("<th data-hide=\"phone\"><b>Level 2</b></th>\n");
			Str.append("<th data-hide=\"phone\"><b>Level 3</b></th>\n");
			Str.append("<th data-hide=\"phone\"><b>Level 4</b></th>\n");
			Str.append("<th data-hide=\"phone\"><b>Level 5</b></th>\n");
			Str.append("</tr>");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			Str.append("<tr align=center>\n");
			for (int i = 1; i <= 5; i++) {
				StrSql = " SELECT insurenquiry_id, insurenquiryfollowup_id,"
						+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
						+ BranchAccess
						+ " WHERE 1=1"
						+ " AND insurenquiryfollowup_disp1 = ''"
						+ " AND insurenquiryfollowup_followup_time <= '" + ToLongDate(kknow()) + "'"
						+ " AND insurenquiryfollowup_trigger=" + i + ""
						+ StrSearch + ExeAccess
						+ " GROUP BY insurenquiryfollowup_id ORDER BY emp_name";
				// SOP("StrSql===" + StrSql);
				CachedRowSet crs2 = processQuery(StrSql, 0);
				if (crs2.isBeforeFirst()) {
					Str.append("<td valign=top align=left >");
					while (crs2.next()) {
						Str.append("<a href=\"javascript:remote=window.open('insurance-enquiry-dash.jsp?insurenquiry_id=")
								.append(crs2.getString("insurenquiry_id"))
								.append("#tabs-2")
								.append("','insurenquirydash','');remote.focus();\">")
								.append(crs2.getString("insurenquiry_id"))
								.append(": ")
								.append(crs2.getString("emp_name"))
								.append("</a><br>");
					}
					Str.append("</td>");
				} else {
					Str.append("<td valign=top align=center>--<br><br></td>");
				}
				crs2.close();
			}
			Str.append("</tbody>\n");
			Str.append("</table>");
			Str.append("</div>\n");
			// Str.append(getDepartment());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getDepartment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " ORDER BY priorityinsurfollowup_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\"><br>\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th style=\"text-align:center\" data-toggle=\"true\" colspan=9>Insurance Follow-up Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>#</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>Priority</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>Description</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Due Hours</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 1</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 2</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 3</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 4</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 5</b></td>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityinsurfollowup_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityinsurfollowup_desc")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_duehrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_trigger1_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_trigger2_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_trigger3_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_trigger4_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityinsurfollowup_trigger5_hrs")).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
